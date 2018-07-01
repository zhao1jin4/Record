https://github.com/mybatis/



如用like 
1、MySQL ：LIKE CONCAT('%',#{empname},'%' ) 或者 LIKE CONCAT('%',‘${empname}’,'%' )
2、Oracle：LIKE '%'||#{empname}||'%'


mybatis-3-config.dtd 和 mybatis-3-mapper.dtd 在 org.apache.ibatis.builder.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties resource="org/zhaojin/mybatis/jdbc.properties">
		<property name="username" value="sa" />
		<property name="password" value="errorpassword" /> <!--  低先级优，被.properties覆盖 -->
	</properties>
	<settings>
		<setting name="jdbcTypeForNull" value="NULL"/> <!--为 @Insert  中 #{_req.id} 的值是null,DB中也是null,如不配置则报错-->
		<setting name="cacheEnabled" value="true" /> <!--  启用缓存 -->
		<setting name="lazyLoadingEnabled" value="true" />
		<setting name="multipleResultSetsEnabled" value="true" />
		<setting name="useColumnLabel" value="true" />
		<setting name="useGeneratedKeys" value="false" />
		<setting name="defaultExecutorType" value="SIMPLE" /> <!-- 可以为 BATCH -->
		<setting name="defaultStatementTimeout" value="25000" /> 
	</settings>
	
	<typeAliases>
		<typeAlias alias="EmployeeDept" type="mybatis_xml.EmployeeDept"/>
		<package name="mybatis_xml"/> <!-- 加了这个 就可以直接写  resultType="Employee"-->
	</typeAliases>
<!--   
	<typeHandlers>
		<typeHandler javaType="String" jdbcType="VARCHAR"
			handler="org.zhaojin.mybatis.ExampleTypeHandler" />
	</typeHandlers>
	<objectFactory type="org.zhaojin.mybatis.ExampleObjectFactory">
		<property name="someProperty" value="100" />
	</objectFactory>
	<plugins>
		<plugin interceptor="org.zhaojin.mybatis.ExamplePlugin">
			<property name="someProperty" value="100" />
		</plugin>
	</plugins>
-->
	<plugins>
		<plugin interceptor="mybatis_annotation.SQLLogInterceptor" />
	</plugins>
	
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="${driver}" />
				<property name="url" value="${url}" />
				<property name="username" value="${username}" />
				<property name="password" value="${password}" />
			</dataSource>
		</environment>
	</environments>
	<mappers>
		<mapper resource="org/zhaojin/mybatis/vo/EmployeeMapper.xml" />
	</mappers>
</configuration>


//也可在 配置log4j
# MyBatis 输出日志SQL和参数 
#log4j.logger.java.sql=debug,stdout

log4j.additivity.java.sql.Connection=false
log4j.logger.java.sql.Connection=debug,stdout

#log4j.logger.java.sql.PreparedStatement=debug,stdout
#批量insert时PreparedStatement会很多

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class SQLLogInterceptor implements Interceptor 
{
    public Object intercept(Invocation invocation) throws Exception
    {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql =statementHandler.getBoundSql();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
System.out.println("sql:==>"+boundSql.getSql());
            StringBuffer param = new StringBuffer(400);
            param.append("[");
			
			 //--为 @ 配置
            Field fieldDel =statementHandler.getClass().getDeclaredField("delegate");
            fieldDel.setAccessible(true);
            BaseStatementHandler delegate = (BaseStatementHandler) fieldDel.get(statementHandler);
            fieldDel.setAccessible(false);
            //
            Field fieldMap =delegate.getClass().getSuperclass().getDeclaredField("mappedStatement");
            fieldMap.setAccessible(true);
            MappedStatement mappedStatement = (MappedStatement) fieldMap.get(delegate);
            fieldMap.setAccessible(false);
            //
            Configuration configuration = mappedStatement.getConfiguration();
           //TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();//no use
           //上为了得到 Configuration
            Object parameterObject = boundSql.getParameterObject();
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            //--上 为 @ 配置
			
            for (int i = 0; i < parameterMappings.size(); i++) 
            {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();
                Object value =null;
                if (boundSql.hasAdditionalParameter(propertyName))
                {
                    value = boundSql.getAdditionalParameter(propertyName); 
                }else
                {
                	value=metaObject.getValue(propertyName);  //对使用了Spring集成的
					if(value instanceof char[])//对parameterType="int"或者"string"配置中用#{value},要么是Spring的MapperFactoryBean的一个参数类型为int,String的情况
                	{
                	    char[] valueArray=(char[])value;
                	    value=new String(valueArray);
                	}
                }
                param.append(propertyName).append(":").append(value).append(",");
            }
			param.deleteCharAt(param.lastIndexOf(","));
            param.append("]");
System.out.println("sql param:==>"+param.toString());
            return invocation.proceed();
    }
	@Override
	public Object plugin(Object target) {
		 return Plugin.wrap(target, this);
	}
}

可以传递一个List或Array类型的对象作为参数,MyBatis会自动的将List或Array对象包装到一个Map对象中,List类型对象会使用list作为键名,而Array对象会用array作为键名。
parameterType="list" 或者不写也可以

<if test="list != null and list.size()>0" >
	<foreach collection="list" item="item"   open="("  separator="or" close=")" >
			  BILL_CODE=#{item}
	</foreach>
</if>
	
<!-- parameterType="string" 和  parameterType="int"
   如使用MyBatis的session方法调用,如selectOne(),要使用#{value},如使用Spring的MapperFactoryBean叫什么都可
-->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.zhaojin.mybatis.ns">
	<select id="selectEmployee" parameterType="int" resultType="Employee">  <!-- 对应<typeAliases>配置 -->
		select id as id ,username as username,password as password ,birthday as birthday from employee where id =
		#{id}
	</select>
	<resultMap id="selectEmpMap" type="EmployeeDept">	<!-- 对应<typeAliases>配置  -->
		<id property="id" column="id" />
		<result property="username" column="username" />
		<result property="department" column="dept" />
	</resultMap>
	
	<sql id="employee_field">e.id as id ,e.username as username,d.dep_name as dept </sql>
	
	<select id="selectEmpDept" parameterType="int" resultMap="selectEmpMap"> <!--  resultMap="selectEmpMap" -->
		select 
		  <include refid="employee_field"/>
		from employee e ,department d 
		where e.department_id =d.id and e.id=#{id}
	</select>
	
	<select id="selectEmpDeptHashMap" parameterType="int" resultType="hashmap"> <!-- resultType="hashmap" -->
		select  <include refid="employee_field"/>
		from employee e ,department d 
		where e.department_id =d.id and e.id=#{id}
	</select>
	
	<select id="dynSelectEmployee" parameterType="Employee" resultType="Employee" >  
		select id as id ,username as username,password as password ,birthday as birthday 
		from employee
		<where>
			<!--  使用   &gt;=  和  &lt;= 
			<if test="username != null and username != '' ">username = #{username} </if>  
			<if test="department_id != null">AND department_id = #{department_id} </if>
			 -->
			<choose>
				<when test="username != null">username = #{username}</when>
				<when test="department_id != null">department_id = #{department_id}</when>
				<otherwise>department_id = 10</otherwise>
			</choose>
		</where>
	</select>
	<!-- 
	<insert id="insertEmployee" parameterType="Employee" >
		insert into employee (id,username,password,birthday,department_id)
		values(#{id}, #{username}, #{password}, #{birthday},#{department_id});
	</insert>
	 -->
	<insert id="insertEmployee" parameterType="Employee" >
	  <selectKey keyProperty="id" resultType="int" order="AFTER">  
          SELECT last_insert_id ()
      </selectKey>
	  
	 <selectKey keyProperty="id" resultType="int" order="BEFORE"> <!-- MySQL --> 
			select cast(rand()*100 as SIGNED )
		</selectKey>
		
		insert into employee 
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<if test="id != null">id,</if>
            <if test="username != null">username,</if>
            <if test="password != null">password,</if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
        	<if test="id != null">#{id},</if>
            <if test="username != null">#{username},</if>
            <if test="password != null">#{password},</if>
        </trim>
	</insert>
	<update id="updateEmployee" parameterType="Employee">
		update Employee
		<set>
			<trim suffixOverrides=",">
				<if test="username != null">username=#{username},</if>
				<if test="password != null">password=#{password},</if>
				<if test="birthday != null">birthday=#{birthday},</if>
				<if test="department_id != 0">department_id=#{department_id}</if>
			</trim>
		</set>
			where id=#{id}
	</update>
</mapper>	

<if test=' name=="你好" '>  用 == 做相等判断，字串使用双引号
<if test="signDateStr !=null and signDateStr !=''" >

得到mysql 的自增主键
	
<insert id="save" parameterType="org.xx.ApplicationPO" useGeneratedKeys="true"   keyProperty="id" >
		
SqlSession session;
protected void setUp() throws Exception 
// {io 

	String resource = "org/zhaojin/mybatis/Configuration.xml";
	Reader reader = Resources.getResourceAsReader(resource);
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);//Properties,Enviroment
	session = sessionFactory.openSession();  //可传 TransactionIsolationLevel
	//session=sessionFactory.openSession(ExecutorType.BATCH, false);//像是开新的连接，在连接池外的，像是
	//session.clearCache();
}
protected void tearDown() throws Exception {
	session.close();
}



Map<String,Object> param=new HashMap<String,Object>(); //如何传数字做运算,pageNo * pageSize???
param.put("department_id", 10);
List<Integer> ids=new ArrayList<Integer>();
ids.add(101);
ids.add(102);
param.put("employee_ids", ids);
		
 <delete id="deletePartEmployeeOfDept" parameterType="map">  
	delete  employee 
	where department_id=#{department_id}  and id in
	 <foreach collection="employee_ids" index="index" item="employee_id" open="(" separator="," close=")">
		#{employee_id}  <!--  #{employee_id,jdbcType=CHAR} -->
	</foreach>
</delete>
	
	
 
Employee employee = (Employee) session.selectOne("org.zhaojin.mybatis.ns.selectEmployee", 101);
EmployeeDept userdept = (EmployeeDept) session.selectOne("org.zhaojin.mybatis.ns.selectEmpDept", 101);
HashMap usermap = (HashMap) session.selectOne("org.zhaojin.mybatis.ns.selectEmpDeptHashMap", 101);
 
Employee param=new Employee();
param.setId(105);
//param.setUsername("Java");
param.setBirthday(Calendar.getInstance().getTime());
param.setPassword("123");
param.setDepartment_id(10);
List<Employee> emps2 =  session.selectList("org.zhaojin.mybatis.ns.dynSelectEmployee", param); 
//内存分页,必须要用<where>才行,H2支持的, new RowBounds( (current-1)*pageSize , pageSize)
 
<!-- 关联父类,只select -->
<resultMap id="employeeWithDept" type="Employee">
	<id property="id" column="e_id" />
	<result property="username" column="e_name"/>
	<association property="department" column="department_id" javaType="Department">
		<id property="id" column="d_id"/>
		<result property="name" column="d_name"/>
	</association>
</resultMap>
<select id="selectEmloyeeWithDept" parameterType="int" resultMap="employeeWithDept">
	select e.id as e_id, 
		e.username as e_name,
		d.id as d_id ,
		d.dep_name as d_name
	from employee e left outer join department d 
	on e.department_id = d.id
	where e.id = #{id}
</select>

<!-- 关联子集合,只select -->
<resultMap id="departmentWithEmps" type="Department">
	<id property="id" column="id" />
	<result property="name" column="d_name"/>
	<collection property="emps" ofType="Employee">
		<id property="id" column="e_id"/>
		<result property="username" column="e_name"/>
	</collection>
</resultMap>
<select id="selectDepartmentWithEmps" parameterType="int" resultMap="departmentWithEmps">
	select e.id as e_id, 
		e.username as e_name,
		d.id as d_id ,
		d.dep_name as d_name
	from department d   left outer join  employee e
	on e.department_id = d.id
	where d.id = #{id}
</select>


<!-- discriminator -->
<resultMap id="EmployeeKind" type="Employee">
	<id property="id" column="id" />
	<result property="username" column="username"/>
	<discriminator javaType="int" column="employee_type">
		<case value="1" resultType="GoodEmployee">
			<result property="raiseSalary" column="raise_salary" />
		</case>
		<case value="2" resultType="BadEmployee">
			<result property="deductSalary" column="deduct_salary" />
		</case>
	</discriminator>
</resultMap>

-------annotation
SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
sessionFactory.getConfiguration().addMapper(UserDao.class);//加带Annotation的接口


@CacheNamespace(size = 512) //定义在该命名空间内允许使用内置缓存
public interface UserDao {
	@SelectKey(statement="select cast(rand()*100 as SIGNED )", keyProperty="userId", before= true, resultType= int.class)//call identity()
	@Insert("insert into user(user_Name,password,comment) values(#{userName},#{password},#{comment})")//参数是User类,#{}中的类的属性名
    public int insert(User user123);
    
    @Update("update user set user_Name=#{userName},password=#{password},comment=#{comment} where user_Name=#{userName}")
    public int update(User user123);
    
    @Delete("delete from user where user_Name=#{userName}")//一个#{}并且一个参数时,自动对应
    public int delete(String userName123);
    
    @Delete("delete from user where user_Name=#{userName} and password=#{password}") //多个参数的对应方法
    public int deleteByTwo(@Param("userName")String userName123,@Param("password")String password123);//不能使用同名重载方法的方式
    
    //@Select("select user_Name as userName ,PASSWORD,COMMENT from user order by user_Name asc") //别名要和类的属性名一样
    @Select("select * from user order by user_Name asc")
    @Results(value = {
		@Result(property="userName", column="user_Name")//对使用select *的方式,只配置不相同
   })
    @Options(useCache = false,flushCache = true)//默认是有cache的(只配置 useCache = false无用的),与配置cacheEnabled无关
    public List<User> selectAll();
    
    @Select("select count(*) c from user;")
    public int countAll();
    
    @Select("select * from user where user_Name=#{userName}")
    @Results(value = {
    		@Result(property="userName", column="user_Name")
       })
    public User findByUserName(String userName123);
    
//-------provider
    @SelectProvider(type = SqlProvider.class, method = "getCount")
    @Options(useCache = true, flushCache = false, timeout = 10000)//flushCache = false表示下次查询时不刷新缓存
    public int providerGetCount();
   
    @SelectProvider(type = SqlProvider.class, method = "getByUserName")  
    @Options(useCache = true, flushCache = false, timeout = 10000)
    @Results(value = {
  		@Result(property="userName", column="user_Name")
    })
    public  User providerGetByUserName(@Param("_username") String username);
    
    @SelectProvider(type = SqlProvider.class, method = "getAll")
    @Results(value = {
    		@Result(property="userName", column="user_Name")
       })
    public  List<User> providerGetAll();//要 User implements Serializable
    
    @InsertProvider(type = SqlProvider.class, method = "insertSql")  
	@SelectKey(statement="select cast(rand()*100 as SIGNED )", keyProperty="testUser.userId", before= true, resultType= int.class)
    public void  providerAddData(@Param("testUser")User u);
  
    @UpdateProvider(type = SqlProvider.class, method = "updateSql")  
    int providerUpadteData(@Param("testUser") User testUser);
     
    @DeleteProvider(type = SqlProvider.class, method = "deleteSql")  
    public void providerDelete(@Param("_username") String username);  
}


@SelectProvider(type = TransCodeSqlProvider.class, method = "getRecordCountByMap")
public long getRecordCountByMap(Map<String,Object> params);
 
import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;  //过时的
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;  
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;  
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;  
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;  
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;  
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;  
import static org.apache.ibatis.jdbc.SqlBuilder.SET;  
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;  
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;  

import org.apache.ibatis.jdbc.SQL;	
public class SqlProvider
{
	
	 public String getRecordCountByMap(Map<String,String> params)//加参数
	 {
		 SQL sql=new SQL();
		 sql.SELECT("count(*)")
		 .FROM("USER");
		 if(params.get("userName")!=null)
		 {
			 sql.WHERE(" USER_NAME=#{userName}");//,javaType=string,jdbcType=VARCHAR
		 }
		 return sql.toString();
		 //----
//		 return new SQL() {{  //表示匿名类exends SQL 在构造器中调用 
//			 SELECT("count(*)");
//			 FROM("USER");
//			 }}.toString();
	 }
	 public String   queryByPage (Map<String,String> params) //Oracle,H2
	 {
		 SQL sql=new SQL();
		 sql.SELECT( "USERID ,USER_NAME ");
		 if(params.get("userName")!=null)
		 {
			 sql.FROM("( select tmp.*, rownum row_num from USER  tmp   where  rownum  <= #{end}   and USER_NAME = #{userName}  ) page");
		 }else
		 {
			 sql.FROM("( select tmp.*, rownum row_num from USER  tmp   where  rownum  <= #{end} ) page");
		 }
		 sql.WHERE("page.row_num >=  #{start}" ) ;
	    return sql.toString();
	 }
	 public String insertSql()
	{
		return new SQL()
		{{
	        INSERT_INTO("USER");  
	        VALUES("USER_NAME", "#{testUser.userName,javaType=string,jdbcType=VARCHAR}");  
	        VALUES("PASSWORD", "#{testUser.password,javaType=string,jdbcType=VARCHAR}");
	        VALUES("COMMENT", "#{testUser.comment,javaType=string,jdbcType=VARCHAR}"); 
		}}.toString();  
	}
	public String updateSql()
	{
		return new SQL()
		{{
	        UPDATE("USER");  
	        SET("PASSWORD = #{testUser.password,javaType=string,jdbcType=VARCHAR}");
	        SET("COMMENT = #{testUser.comment,javaType=string,jdbcType=VARCHAR}");  
	        WHERE("USER_NAME = #{testUser.userName,javaType=string,jdbcType=VARCHAR}");  
	    }}.toString();    
	}
	public String deleteSql()
	{
		return new SQL()
		{{ 
	        DELETE_FROM("USER");  
	        WHERE("USER_NAME = #{_username,javaType=string,jdbcType=VARCHAR}");  
		 }}.toString(); 
	}
}

 create_time >= #{beginTime,jdbcType=TIMESTAMP} and create_time <= #{endTime,jdbcType=TIMESTAMP}
  
 jdbcType=DATE 只有年月日，没有时分秒  
 取值对应于 org.apache.ibatis.type.JdbcType中的值 其实就是  java.sql.Type中的值
 
 Long deleted;  
 ,jdbcType=NUMERIC      NUMERIC,INTEGER
 
<if test="deleted !=null ">   
	AND s.BL_DELETE=#{deleted}      
</if>
	
 //--分页 ,Map 参数
@SelectProvider(type = SqlProvider.class, method = "getRecordCountByMap")
public long getRecordCountByMap(Map<String,String> params);

@SelectProvider(type = SqlProvider.class, method = "queryByPage")
@Results(value = {
		@Result(property="userName", column="user_Name")
   })
public  List<User>  queryByPage(Map<String,String> params);
	
@UpdateProvider
@DeleteProvider

@Insert("insert into table3 (id, name) values(#{nameId}, #{name})")
@SelectKey(statement="call next value for TestSequence", keyProperty="nameId", before=true, resultType=int.class)
int insertTable3(Name name);



//使用时
UserDao userDao = session.getMapper(UserDao.class);
userDao.insert(user);//user中有值

//---typeHandler

//有时要 Job implements Serializable
@Select("select * from job_history where user_Id=#{_id}")
@Results(value = {
		@Result(property="requirement", column="job_requirement",
				javaType=java.util.List.class,jdbcType=JdbcType.VARCHAR,typeHandler=MyXMLTypeHandler.class),
		@Result(property="jobTitle", column="job_title" )
   })
public List<Job> getJobsByUserId(@Param("_id") int userid);


@Select("select * from job_history where start_date = #{startDate}")  
@Results(value = {
		@Result(property="requirement", column="job_requirement",
				javaType=java.util.List.class,jdbcType=JdbcType.VARCHAR,typeHandler=MyXMLTypeHandler.class),
		@Result(property="jobTitle", column="job_title" )
   })
//如oracle 是date 类型,使用java.sq.Date 会使用索引,而util.Date不会 !!!
//如 oracle 是 timestamp 类型无论是util.Date还是sql.Date都使用索引
//而MySQL可以直接传字串就OK
public List<Job> getJobByStartDate(@Param("startDate") java.util.Date startDate);


@InsertProvider
//@Insert("insert into job_history(job_requirement,job_title,user_id) values(#{requirement,javaType=java.util.List,jdbcType=VARCHAR,typeHandler=mybatis.annotation.MyXMLTypeHandler},#{jobTitle},1)")
public void saveJob(Job job);

/*
<useSkills> 
 <name>JavaEE</name>
 <name>Oracle</name>
</useSkills> 
*/
public class MyXMLTypeHandler extends BaseTypeHandler<List<String>>
{
	public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType type) throws SQLException
	{
		if(parameter ==null)
		{
			ps.setString(i, null);
			return ;
		}
		StringBuffer result=new StringBuffer();
		result.append("<useSkills>");
		for(int k=0;k<parameter.size();k++)
		{
			result.append("<name>").append(parameter.get(k)).append("</name>");
		}
		result.append("</useSkills>");
		ps.setString(i, result.toString());
	}

	public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException
	{
		 String xmlString=rs.getString(columnName);
		return parseXml2Object(xmlString);
	}

	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException
	{
		  String xmlString=rs.getString(columnIndex);
		  return parseXml2Object(xmlString);
	}

	public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
	{
		  String xmlString= cs.getString(columnIndex);
		  return parseXml2Object(xmlString);
	}
	private List<String> parseXml2Object(String xmlString)
	{
		List<String>  result=new ArrayList<String>();
		
		SAXReader reader=new SAXReader();
		Document doc=null;
		try
		{
			StringReader strReader=new StringReader(xmlString);
			doc = reader.read(strReader);
		} catch (DocumentException e)
		{
			e.printStackTrace();
		}
		Element root=doc.getRootElement();
		//System.out.println("根节点名："+root.getName());
		List<Element> names=root.elements();
		for(Iterator<Element> it= names.iterator();it.hasNext();)
		{
			Element name= it.next();
			result.add(name.getText());
		}
		return result;
	}
}
//---typeHandler
	
-----c3p0
ComboPooledDataSource dataSource= new  ComboPooledDataSource();//c3p0
dataSource.setDriverClass("org.h2.Driver");
dataSource.setJdbcUrl("jdbc:h2:tcp://localhost/~/test");
dataSource.setUser("sa");
dataSource.setPassword("");
dataSource.setInitialPoolSize(5);
dataSource.setMinPoolSize(5);
dataSource.setMaxPoolSize(20);
dataSource.setMaxStatements(100);
dataSource.setMaxIdleTime(3600);
dataSource.setAcquireIncrement(2);
dataSource.setAcquireRetryAttempts(10);
dataSource.setAcquireRetryDelay(600);
dataSource.setTestConnectionOnCheckin(true);
dataSource.setIdleConnectionTestPeriod(1200);
dataSource.setCheckoutTimeout(10000);
	
TransactionFactory transactionFactory = new JdbcTransactionFactory();
Environment environment =new Environment("development", transactionFactory, dataSource);
Configuration configuration = new Configuration(environment);
configuration.addMapper(BlogMapper.class);
configuration.setCacheEnabled(true);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

-----Druid
https://github.com/alibaba/druid

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.1.10</version>
</dependency>

<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close"> 
	 <property name="url" value="${jdbc.url}" />
	 <property name="username" value="${jdbc.username}" />
	 <property name="password" value="${jdbc.password}"/>
	 <property name="driverClassName" value="${jdbc.driver}"/>  
	 <property name="maxActive"><value>20</value></property>
	 <property name="initialSize"><value>1</value></property>
	 <property name="maxWait"><value>60000</value></property>
	 <property name="minIdle"><value>1</value></property>
	 <property name="timeBetweenEvictionRunsMillis"><value>60000</value></property>
	 <property name="minEvictableIdleTimeMillis"><value>300000</value></property>
	 <property name="validationQuery"><value>SELECT 'x'</value></property>
	 <property name="testWhileIdle"><value>true</value></property>
	 <property name="testOnBorrow"><value>false</value></property>
	 <property name="testOnReturn"><value>false</value></property>
	 <property name="poolPreparedStatements"><value>true</value></property>
	 <property name="maxOpenPreparedStatements"><value>20</value></property>
 
 	<property name="filters" value="stat" /> 
	<!-- value="stat"用于统计监控信息,stat别名映射配置信息保存在druid-xxx.jar!/META-INF/druid-filter.properties 可逗号配多个,也可使用proxyFilters引用bean,会合并 -->
	
	<!--
	<property name="connectionProperties" value="druid.stat.mergeSql=true" />
	<property name="connectionProperties" value="druid.stat.slowSqlMillis=5000" />
   
  	<property name="filters" value="log4j,mergeStat" /> 
	<property name="proxyFilters">
		<list>
			<ref bean="stat-filter" />
		</list>
	</property>
	-->
	
 </bean>

 <bean id="stat-filter" class="com.alibaba.druid.filter.stat.StatFilter">
	<property name="slowSqlMillis" value="10000" />
	<property name="logSlowSql" value="true" />
	<property name="mergeSql" value="true" /> 
	<!--  当多个相同的SQL不能参数时，变为一个SQL ? 形式, 也可在DruidDataSource用 <property name="filters" value="mergeStat" /> -->
</bean>


web.xml 中加
<servlet>
  <servlet-name>DruidStatView</servlet-name>
  <servlet-class>com.alibaba.druid.support.http.StatViewServlet</servlet-class>
   <init-param>  
	<!-- 允许清空统计数据 -->  
	<param-name>resetEnable</param-name>  
	<param-value>true</param-value>  
    </init-param>  
    <init-param>  
	<!-- 用户名 -->  
	<param-name>loginUsername</param-name>  
	<param-value>druid</param-value>  
    </init-param>  
    <init-param>  
	<!-- 密码 -->  
	<param-name>loginPassword</param-name>  
	<param-value>druid</param-value>  
	</init-param>
	<init-param>
  		<param-name>allow</param-name>
  		<param-value>127.0.0.1/24,128.242.128.1</param-value>
  	</init-param>
  	<init-param>
  		<param-name>deny</param-name>
  		<param-value>128.242.127.4</param-value>
  	</init-param>
	<init-param>
  		<param-name>resetEnable</param-name>
  		<param-value>false</param-value>
  	</init-param>
</servlet>
<servlet-mapping>
  <servlet-name>DruidStatView</servlet-name>
  <url-pattern>/druid/*</url-pattern>   */
</servlet-mapping>
 
 
 <filter>
  	<filter-name>DruidWebStatFilter</filter-name>
  	<filter-class>com.alibaba.druid.support.http.WebStatFilter</filter-class>
  	<init-param>
  		<param-name>exclusions</param-name>
  		<param-value>*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*</param-value>
  	</init-param>
	<init-param>
		<param-name>profileEnable</param-name>  <!-- 配置profileEnable能够监控单个url调用的sql列表。 -->
		<param-value>true</param-value>
	</init-param>

  </filter>
  <filter-mapping>
  	<filter-name>DruidWebStatFilter</filter-name>
  	<url-pattern>/*</url-pattern>  */
  </filter-mapping>
  
 http://127.0.0.1:8080/J_SpringMVC/druid  登录密码就是web.xml中配置的
 

-------MyBatis3 Spring集成
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
	  <!--myBatis自己的事务
      <property name="transactionFactory" >
            <bean class="org.apache.ibatis.transaction.managed.ManagedTransactionFactory">
            </bean>
        </property>
         -->
	<property name="configLocation" value="classpath:org/zhaojin/mybatis/Configuration.xml"></property>
	 <!--
	 <property name="configurationProperties">
        <props>
          <prop key="jdbcTypeForNull">NULL</prop>
        </props>
    </property>
	<property name="mapperLocations" value="classpath*:org/zhaojin/mybatis/vo/**/*.xml" />
	<property name="plugins">
		<list>
			<bean class="mybastis_spring.SQLLogInterceptor" />
		</list>
	</property>
	 -->
	    
</bean>
<!-- 你不能在Spring管理的SqlSession上调用SqlSession.commit()，SqlSession.rollback()或SqlSession.close()方法 -->

<!-- 继承 SqlSessionDaoSupport, 注入sqlSessionFactory ,就可以用getSqlSession().selectOne()-->
<bean id="dao" class="org.zhaojin.mybatis.spring.DaoImpl">
	<property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>

------------------
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean"
		lazy-init="false">
		<property name="dataSource" ref="dataSource" />
		<property name="mapperLocations" value="classpath:sqlmapper/*Mapper.xml" />
		<property name="configLocation" value="classpath:mybatis-config.xml" />
		<property name="plugins">
			<list>
				<bean class="xx.SQLLogInterceptor" />
			</list>
		</property>
	</bean>


   
<bean id="batchSqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate"> 
	<constructor-arg index="0" ref="sqlSessionFactory" />
	<constructor-arg index="1" value="BATCH" />
</bean>
<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate"> 
	<constructor-arg index="0" ref="sqlSessionFactory" />
</bean>
 
 batchSqlSessionTemplate.insert()默认是自动立即提交的
batchSqlSessionTemplate.commit(); 
batchSqlSessionTemplate.clearCache();

org.apache.ibatis.session.SqlSession batchSqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(  ExecutorType.BATCH, false);//auto-commit:false
	BaseDao batchBaseDao= batchSqlSession.getMapper(BaseDao.class);
	try{
		 for (Map<String, Object> row : allData)
		 {
			 batchBaseDao.insert(row);
		 }
		 batchSqlSession.commit();
		 batchSqlSession.clearCache();
	}catch(Exception ex)
	{
		LOG.error("mybatis batch insert error  ",ex);
		batchSqlSession.rollback();
	}finally{
		batchSqlSession.close();
	}
 
batchSqlSession.update()返回的是不正常的,所以不能用

 
 // 使用 insert into myTable () values(),(),()的形式速度最快 ,受mysql参数bulk_insert_buffer_size影响,如果values使用#{}绑定形式,则会比事务中还要慢
// ->  其次是在事务中做insert, -> 再次是使用BATCH的session -> SpringBatch的ItemWirter
 
<bean id="pagingItemReader" class="org.mybatis.spring.batch.MyBatisPagingItemReader"
   p:pageSize="10"
>
  <property name="sqlSessionFactory" ref="sqlSessionFactory" />
  <property name="queryId" value="StudentMapper.getAll" />   <!-- 如是 MapperFactoryBean的用类的全包名.方法名 , 如是@形式的,有 @Param("_e") 不行 ???    -->
</bean>

<bean id="batchItemWriter" class="org.mybatis.spring.batch.MyBatisBatchItemWriter">
   <property name="sqlSessionTemplate" ref="batchSqlSessionTemplate"></property>
  <property name="sqlSessionFactory" ref="sqlSessionFactory" />
  <property name="statementId" value="EmployeeMapper.insertEmployee" />
</bean>

<select id="getAll" resultType="Student" parameterType="map">
  select id,name,age,birthday from student 
  ORDER BY id 
  LIMIT #{_skiprows}, #{_pagesize}
</select> <!--  MyBatisPagingItemReader的属性有_page , _pagesize , _skiprows -->



//--分页读
MyBatisPagingItemReader<Employee> pagingItemReader
int pageSize=pagingItemReader.getPageSize();
//pagingItemReader.setPageSize(5);

Map<String,Object> params=new HashMap<>();
params.put("idGt",9000);
pagingItemReader.setParameterValues(params);
int initPageNo=pagingItemReader.getPage();//开始是0,第一次read()后变以1 ,如何指定从第几页开始读 ？？？

Employee  data=null; 
for (int i=0;i<pageSize  &&(data=pagingItemReader.read())!=null  ;i++)  //只要第一页
{
   System.out.println(data.getId()+"-" + data.getUsername());
}


//--批量写,没有transactionTemplate做事务速度快
MyBatisBatchItemWriter<Employee> batchItemWriter
List<Employee> items=...
batchItemWriter.setAssertUpdates(false);//如是insert设置为false
batchItemWriter.write(items);//看源码是使用  ExecutorType.BATCH的SqlSessionTemplate

--------MapperFactoryBean

public interface XMLEmployeeInterface {
	//MapperFactoryBean,会自动找与类名相同的位置XMLEmployeeInterface.xml,如找不到会使用已经有配置
	//配置的映射文件的namespace和本类全包名必须相同
	public int deleteByEmployeeId(int emp_id);//方法名要必须和XML配置文件中的名字一样
	
	//使用Spring的 MapperFactoryBean也可以用@Param向XML中传参数,XML中用paramterType="map"
	public int insertManyParam(@Param("id")long id,@Param("username")String username,@Param("password")String password,@Param("age")int age);

}

@CacheNamespace(size = 512)
public interface AnnoEmployeeInterface {
	@Delete("delete  employee   where id =#{_id}") //如一个参数类型是int,String 名字随便叫
	//public int deleteByEmployeeId(@Param("_id")int emp_id);
	public int deleteByEmployeeId(int emp_id);
}

<bean id="annoEmployeeDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
		<property name="mapperInterface" value="mybatis_spring.factorybean.AnnoEmployeeInterface" />
		<property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>

<bean id="xmlEmployeeDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
		<property name="mapperInterface" value="mybatis_spring.factorybean.XMLEmployeeInterface" />
		<property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>

<!-- 就不用每个类 配置 MapperFactoryBean  
	但  PropertyPlaceholderConfigurer 无用, 可以使用 PropertiesFactoryBean 和 SpEL 表达式来作为替代-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  <property name="basePackage" value="mybatis_spring.factorybean.dao" />  <!-- 逗号分隔多个 -->
</bean>
@MapperScan(basePackages ="mybatis_spring.factorybean.dao")

Class  targetClass=proxy.getClass().getInterfaces()[0]; // BaseDao 的子类

InvocationHandler invocation=Proxy.getInvocationHandler(proxy);//mybatis BaseDao,
MapperProxy p=(MapperProxy)invocation;

util:properties的实现类是PropertiesFactoryBean 
	
	
<mapper namespace="mybatis_spring.factorybean.XMLEmployeeInterface"> <!-- MapperFactoryBean, namespace对应 的类名要一样  -->
	<!-- parameterType="int" 和  parameterType="string"如使用MapperFactoryBean #{} 中的名字随便叫 ,如使用Session的方法要用#{value} -->
	<delete id="deleteByEmployeeId"  parameterType="int" >  
		delete  employee  
		where id = #{emp_id}
	</delete>
	
	<insert id="insertManyParam" parameterType="map">
       insert into employee(id,username,password,age) values(#{id},#{username},#{password},#{age})
    </insert>
</mapper>

--------事务
如用Spring 使用 org.springframework.transaction.support.TransactionTemplate
Spring集成 违返唯一键约束    org.springframework.dao.DuplicateKeyException extends DataIntegrityViolationException  


--------MyBatis Cache   

<configuration>
	<settings>
        <setting name="cacheEnabled" value="true"/> 
	</settings>
</configuration>

<mapper namespace="cxxx">  
    <!-- 以下两个<cache>标签二选一,第一个可以输出日志,第二个不输出日志 -->  
		<cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>  
   <!-- <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>  classpath 下加 ehcache.xml -->  
</mapper>  


@CacheNamespace(size = 512)

mybatis默认是启用cache的,如不使用cache  <select        useCache="false">


=====mybatis generator 生成 XML配置  或 @配置 

<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator</artifactId>
    <version>1.3.2</version>
</dependency>


--generatorConfig.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration   PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
                                      "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
     <classPathEntry location="D:/eclipse_java_workspace/WEB_LIB/WebContent/WEB-INF/lib/mysql-connector-java-5.1.21-bin.jar" />
     <context id="MySQLTables" targetRuntime="MyBatis3">
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/mydb?useUnicode=true&amp;characterEncoding=UTF-8"
			userId="user1"
			password="user1">
		</jdbcConnection>

		<javaTypeResolver >
		  <property name="forceBigDecimals" value="false" />
		</javaTypeResolver>

		<javaModelGenerator targetPackage="org.project.model" targetProject="project/src/main">
		  <property name="enableSubPackages" value="true" />
		  <property name="trimStrings" value="true" />
		</javaModelGenerator>

		<sqlMapGenerator targetPackage="mybatis"  targetProject="project/src/resources">
		  <property name="enableSubPackages" value="true" />
		</sqlMapGenerator>

		<javaClientGenerator type="XMLMAPPER" targetPackage="org.project.mapper"  targetProject="project/src/main">
		  <property name="enableSubPackages" value="true" />
		</javaClientGenerator>

	<!-- 二选一 , 1.3.2 生成的使用类是 过时的  SqlBuilder.BEGIN ,(现在是1.3.6版本)
		也有type="MIXEDMAPPER" 混合式,复杂的SQL用XML
        <javaClientGenerator type="ANNOTATEDMAPPER" targetPackage="org.project.annotate"  targetProject="project/src/main">
          <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
   -->   

		<table  tableName="EMPLOYEE"  >
		  <property name="useActualColumnNames" value="true"/>
		  <generatedKey column="ID" sqlStatement="MySQL" identity="true" />
		  <columnOverride column="USERNAME" property="userName" />
		  <columnOverride column="BIRTHDAY" property="brithDay" jdbcType="datetime" />
		  <ignoreColumn column="PASSWORD" />
		</table>
		<table tableName="department" domainObjectName="DepartmentEntity" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">
            <property name="useActualColumnNames" value="false"/>
        </table>
		
  </context>
</generatorConfiguration>

要先建立 project/src/main 和 project/src/resources  目录
java -jar mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite


<javaClientGenerator type="ANNOTATEDMAPPER" 

Maven插件方式
<plugin>
		<groupId>org.mybatis.generator</groupId>
		<artifactId>mybatis-generator-maven-plugin</artifactId>
		<version>1.3.6</version>
		<dependencies>
			<dependency>
				<groupId>mysql</groupId>
				<artifactId>mysql-connector-java</artifactId>
				<version>5.1.45</version>
			</dependency>
		</dependencies>
		<configuration> 
			 <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile> 
			<overwrite>true</overwrite>
		</configuration>
</plugin>
mvn mybatis-generator:generate
=====



