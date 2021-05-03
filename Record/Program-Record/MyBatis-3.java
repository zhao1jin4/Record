https://github.com/mybatis/
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.4</version>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.4</version>
</dependency>


<dependency>
    <groupId>org.mybatis.dynamic-sql</groupId>
    <artifactId>mybatis-dynamic-sql</artifactId>
    <version>1.1.4</version>
</dependency>



如用like 
1、MySQL ：LIKE CONCAT('%',#{empname},'%' ) 或者 LIKE CONCAT('%',‘${empname}’,'%' )
2、Oracle：LIKE '%'||#{empname}||'%'

<bind name="pattern" value="'%' + _parameter.getUsername() + '%'" /> 

<bind name="pattern" value="'%' + username  + '%'" /> <!-- 如为Map参数 ,如为null是不行的， 不能放在<if >中使用 -->
username  like #{pattern} 

MySQL 有bit类型做boolean  
MyBatis有JdbcType.BIT   存取到java的 boolean类型测试OK
 
 
 
@Configuration
@PropertySource( value = "classpath:/mybatis-jdbc.properties")
@MapperScan(basePackages = "org.project.mapper")
public class MyBatisConfig
{
	@Autowired 
	Environment env;
	
	@Value("${jdbc.url}")
	private String jdbcUrl;
	
	@Value("${jdbc.driver}")
	private String driver;
	
	@Value("${jdbc.username}")
	//@Value("${username}")取值为系统用户名,属性文件中不能为username
	private String username;
	
	@Value("${jdbc.password}")
	private String password;
	
	@Bean
	public DataSource dataSource()
	{
//		String username=env.getProperty("username");//取值为系统用户名??
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driver);
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
	
		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}
	@Bean  //@MapperScan中doc提示的JDBC事务
	public DataSourceTransactionManager transactionManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception
	{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		return sessionFactory.getObject();
	}
}


mybatis-3-config.dtd 和 mybatis-3-mapper.dtd 在 org.apache.ibatis.builder.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties resource="org/zh/mybatis/jdbc.properties">
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
		
		<setting name="logImpl" value="STDOUT_LOGGING" /> <!-- 会显示数据库执行SQL日志-->
	</settings>
	
	<typeAliases>
		<typeAlias alias="EmployeeDept" type="mybatis_xml.EmployeeDept"/>
		<package name="mybatis_xml"/> <!-- 加了这个 就可以直接写  resultType="Employee"-->
	</typeAliases>
<!--   
	<typeHandlers>
		<typeHandler javaType="String" jdbcType="VARCHAR"
			handler="org.zh.mybatis.ExampleTypeHandler" />
	</typeHandlers>
	<objectFactory type="org.zh.mybatis.ExampleObjectFactory">
		<property name="someProperty" value="100" />
	</objectFactory>
	<plugins>
		<plugin interceptor="org.zh.mybatis.ExamplePlugin">
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
		<mapper resource="org/zh/mybatis/vo/EmployeeMapper.xml" />
	</mappers>
</configuration>


//也可在 配置log4j
# MyBatis 输出日志SQL和参数 
#log4j.logger.java.sql=debug,stdout

log4j.additivity.java.sql.Connection=false
log4j.logger.java.sql.Connection=debug,stdout

#log4j.logger.java.sql.PreparedStatement=debug,stdout
#批量insert时PreparedStatement会很多

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class  }) })
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
			if(parameterMappings.size()==0)
            {
            	return invocation.proceed();
            }
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

//打印SQL日志 ,做通用分页功能用
{
//		Map<String,Object> param=new HashMap<>();
//		param.put("username", "li");
//		String mapperId="org.zh.mybatis.ns.queryAllEmployeeByPage";
//		RowBounds rowBounds=new RowBounds(offset,limit);//对于MySQL其实就是limit 对于查询中有 <collection> 是不准的
		//-------------
		Configuration  configuration=sessionFactory.getConfiguration();
		Collection<String> stateNames=configuration.getMappedStatementNames();
		MappedStatement mapStatment=configuration.getMappedStatement(mapperId);
	
		BoundSql boundSql=mapStatment.getBoundSql(param);
		String sql=boundSql.getSql();
		System.out.println("sql=>"+sql);//里面没有limit,使用 RowBounds
		String countSql="select count(*) from ("+sql+") A";
		System.out.println("count sql=>"+countSql);
		
		List<ParameterMapping> list=boundSql.getParameterMappings(); 
		MetaObject metaObject=configuration.newMetaObject(param);
		Map<String,Object> sqlParam=new HashMap<>();
		for(ParameterMapping parameterMapping :list)
		{
			Object value=metaObject.getValue(parameterMapping.getProperty());
			sqlParam.put(parameterMapping.getProperty(),value);
		}
		System.out.println("parm=>"+sqlParam);
		
		//org.apache.ibatis.annotations.ResultMap
		List<org.apache.ibatis.mapping.ResultMap> results=mapStatment.getResultMaps();
		if(results!=null&&results.size()>0)
		{
			Class<?> resClass=results.get(0).getType();
			System.out.println("resClass= "+resClass);
		}
		
		/*对应于配置 
		  <typeHandlers>
        		<typeHandler>
		 */
		TypeHandlerRegistry typeHandlerRegistry=	configuration.getTypeHandlerRegistry();
		boolean isHave=typeHandlerRegistry.hasTypeHandler(param.getClass());
		//<T>  
		List<Employee> res=sqlSessionTemplate.selectList(mapperId, param, rowBounds);
		
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
<mapper namespace="mybatis_xml.ns">
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
		<bind name="pattern" value="'%' + _parameter.getUsername() + '%'" /> <!-- _parameter 相当于内置变量 -->
		select id as id ,username as username,password as password ,birthday as birthday 
		from employee
		<where>
			<!--  使用   &gt;=  和  &lt;=  也可用  <![CDATA[    ]]>
			<if test="username != null and username != '' ">username = #{username} </if>  
			<if test="department_id != null">AND department_id = #{department_id} </if>
			 -->
			 
			 <if test="@mybatis_xml.MyOgnlUtil@isNotBlank(username)"> 
			 	or username = '${@mybatis_xml.MyOgnlUtil@map.get("j")}'
			 </if>
			  or  username  like #{pattern} 
			<!--
			<choose>
				<when test="username != null">username = #{username}</when>
				<when test="department_id != null">department_id = #{department_id}</when>
				<otherwise>department_id = 10</otherwise>
			</choose>
			-->
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
<if test="sex=='Y'.toString()"> 或者 <if test = 'sex== "Y"'>  不能使用 <if test="sex=='Y'">

得到mysql 的自增主键
	
<insert id="save" parameterType="org.xx.ApplicationPO" useGeneratedKeys="true"   keyProperty="id" >
		
SqlSession session;
protected void setUp() throws Exception 
// {io 

	String resource = "mybatis_xml/Configuration.xml";
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
	
	
 
Employee employee = (Employee) session.selectOne(namespace+".selectEmployee", 101);
EmployeeDept userdept = (EmployeeDept) session.selectOne(namespace+".selectEmpDept", 101);
HashMap usermap = (HashMap) session.selectOne(namespace+".selectEmpDeptHashMap", 101);
 
Employee param=new Employee();
param.setId(105);
//param.setUsername("Java");
param.setBirthday(Calendar.getInstance().getTime());
param.setPassword("123");
param.setDepartment_id(10);
List<Employee> emps2 =  session.selectList(namespace+".dynSelectEmployee", param); 
//内存分页,必须要用<where>才行,H2支持的, new RowBounds( (current-1)*pageSize , pageSize)
 
<!-- 关联父类 -->
<resultMap id="employeeWithDept" type="Employee">
	<id property="id" column="e_id" />
	<result property="username" column="e_name"/>
	<association property="department" column="department_id" javaType="Department">
		<id property="id" column="d_id"/>
		<result property="name" column="d_name"/>
	</association>
</resultMap>
<select id="selectEmloyeeWithDept" parameterType="int" resultMap="employeeWithDept">
	select 
		   e.id 		as e_id, 
			e.username 	as e_name,
			d.id 		as d_id ,
			d.dep_name 	as d_name
		from 
		employee e 
			left outer join 
		department d  on e.department_id = d.id
		where e.id = #{value}
</select>

<!-- 一对一的映射 -->
<resultMap id="employeeOne2OneDept" type="Employee">
	<id property="id" column="e_id" />
	<result property="username" column="e_name"/>
	<result property="department.id" column="d_id"/>
	<result property="department.name" column="d_name"/> 
</resultMap>
<select id="selectAllEmloyeeWithDept"   resultMap="employeeOne2OneDept">
	select 
		 e.id 		as e_id, 
		e.username 	as e_name,
		d.id 		as d_id ,
		d.dep_name as d_name
	from 
	employee e 
		left outer join 
	department d  on e.department_id = d.id
</select>
	
	
<!-- 关联子集合  -->
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
	where d.id = #{value}
</select>


<!-- 关联子集合,只select ，每一条记录 再做一次子级查询-->
<resultMap id="departmentWithSubQueryEmps" type="Department">
	<id property="id" column="id" />
	<result property="name" column="dep_name"/>
	<collection property="emps" ofType="Employee" select="queryEmployeeByDeptId" column="id"> <!-- select 对应哪个查询，column哪列值做为参数 -->
	</collection>
</resultMap>
<select id="queryEmployeeByDeptId"  resultType="Employee" >
	select e.*
	from employee e 
	where e.department_id = #{value}
</select>
<select id="selectDepartmentWithSubEmps"  resultMap="departmentWithSubQueryEmps">
	select d.*
	from department d 
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
    @Options(useCache = false,flushCache = FlushCachePolicy.FALSE)//默认是有cache的(只配置 useCache = false无用的),与配置cacheEnabled无关
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
    @Options(useCache = true, flushCache = FlushCachePolicy.FALSE, timeout = 10000)//flushCache = false表示下次查询时不刷新缓存
    public int providerGetCount();
   
    @SelectProvider(type = SqlProvider.class, method = "getByUserName")  
    @Options(useCache = true, flushCache = FlushCachePolicy.FALSE, timeout = 10000)
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


@Select("select u.user_name ,count(*) as works from  user u left join job_history j on u.userId=j.user_id group by u.user_name  having u.user_name=#{_username}")
@ResultMap(value = "joinData") //引用其它方法上  @Results(id="joinData"
public  JoinData getWorksResultMap(@Param("_username") String username); 

//示例@Many
@Select("select userId,user_Name,password,comment from user where user_Name='lisi'")
@Results({
		 @Result(property="userId",     column="userId"),
		 @Result(property="userName",     column="user_Name"),
		 @Result(property="jobs", column="userId",  javaType=List.class, //@Many时column 为(父表的)主键,javaType=List.class
					many=@Many(select = "mybatis_annotation.JobDao.getJobsByUserId"))
		 })
public User getUserCollectionJobs();


@Select("select * from user where userId=#{value}")
@Results(value = {
	@Result(property="userName", column="user_Name") 
})
public  User getUserById(int user_Id);



@UpdateProvider
@DeleteProvider

@Insert("insert into table3 (id, name) values(#{nameId}, #{name})")
@SelectKey(statement="call next value for TestSequence", keyProperty="nameId", before=true, resultType=int.class)
int insertTable3(Name name);



//使用时
UserDao userDao = session.getMapper(UserDao.class);
userDao.insert(user);//user中有值

//---typeHandler

//---JobDao
//有时要 Job implements Serializable
@Select("select * from job_history where user_Id=#{value}")//如参数是int要使用#{value}
@Results(value = {
		@Result(property="id", column="job_id" ),
		@Result(property="requirement", column="job_requirement",
				javaType=java.util.List.class,jdbcType=JdbcType.VARCHAR,typeHandler=MyXMLTypeHandler.class),
		@Result(property="jobTitle", column="job_title" )
   })
public List<Job> getJobsByUserId( int userid); 

//默认支持enum类型，是以名字name()返回

	
//示例@One
@Select("select job_id,job_title,user_Id from job_history where job_title like 'java%'")
@Results({
	 @Result(property="id",     column="job_id"),
	 @Result(property="jobTitle",     column="job_title"),
	 @Result(property="user", column="user_Id",  javaType=User.class, //@One 时column 是子表的外键
				one=@One(select="mybatis_annotation.UserDao.getUserById"))
	 })
public Job getJobAssociationUser();



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
dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
dataSource.setUser("zh");
dataSource.setPassword("123");
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
 
-----hikari 数据库连接池
比c3p0性能好

<dependency>
	 <groupId>com.zaxxer</groupId>
	 <artifactId>HikariCP</artifactId>
	 <version>3.3.1</version>
</dependency>

//不需要driver,rowSetFactory也不需要Driver
HikariConfig config = new HikariConfig();
config.setDriverClassName("com.mysql.cj.jdbc.Driver");
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("user1");
config.setPassword("user1");
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

//或者不需要Config
HikariDataSource ds = new HikariDataSource();
ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
ds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
ds.setUsername("user1");
ds.setPassword("user1");

	
	
//或者从配置文件读
HikariConfig config = new HikariConfig("/dbpool_hikari/hikari.properties");
HikariDataSource ds = new HikariDataSource(config);

<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
	<constructor-arg>
		<bean class="com.zaxxer.hikari.HikariConfig">
			<constructor-arg index="0" value="/dbpool_hikari/hikari.properties"></constructor-arg>
		</bean>
	</constructor-arg> 
</bean>

dataSourceClassName=com.mysql.cj.jdbc.MysqlXADataSource
#jdbcUrl=jdbc:mysql://localhost:3306/mydb
#dataSourceClassName or  jdbcUrl
dataSource.user=user1
dataSource.password=user1
dataSource.databaseName=mydb
dataSource.portNumber=3306
dataSource.serverName=localhost 


		
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
 
 
 
配置文件从远程http服务器中读取,数据库用户名密码就只有部分人知道
	 <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
		 init-method="init" destroy-method="close">
		 <property name="filters" value="config" />
		 <property name="connectionProperties" value="config.file=http://127.0.0.1/druid-pool.properties" />
	 </bean>
	 也可启动时指定 java -Ddruid.filters=config ....
 
数据库连接密码加密 
	java -cp druid-1.1.10.jar com.alibaba.druid.filter.config.ConfigTools <password>
	会输出
	privateKey:    
	publicKey: 
	password:
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
		 init-method="init" destroy-method="close">
		 <property name="url" value="${url}" />
		 <property name="username" value="${username}" />
		 <property name="password" value="${password}" />
		 <property name="filters" value="config" />
		 <property name="connectionProperties" value="config.decrypt=true;config.decrypt.key=${publickey}" />
	</bean>
	
也可重写 PropertyPlaceholderConfigurer 来加密指定配置字段  过时 使用 PropertySourcesPlaceholderConfigurer
public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private String[] encryptPropNames = { "usernameEnc", "passwordEnc" };
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) 
		{ 
			String decryptValue = DESUtil.getDecryptString(propertyValue);//自己的加密解密类
			return decryptValue;
		} else 
		{
			return propertyValue;
		}
	}
	private boolean isEncryptProp(String propertyName) 
	{
		for (String encryptpropertyName : encryptPropNames) 
		{
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}

springboot druid

https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter

<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid-spring-boot-starter</artifactId>
   <version>1.1.17</version>
</dependency>

spring.datasource.druid.url= # 或spring.datasource.url= 
spring.datasource.druid.username= # 或spring.datasource.username=
spring.datasource.druid.password= # 或spring.datasource.password=
spring.datasource.druid.driver-class-name= #或 spring.datasource.driver-class-name=


配置多数据源








----HikariCP 光 数据源
Spring Boot 优先使用
https://github.com/brettwooldridge/HikariCP

    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>3.4.1</version>
    </dependency>
    
HikariConfig config = new HikariConfig();
config.setDriverClassName("com.mysql.cj.jdbc.Driver"); //MySQL 8
config.setJdbcUrl("jdbc:mysql://localhost:3306/zabbix");
config.setUsername("zabbix");
config.setPassword("zabbix");
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

//源码是 new File的形式,不支持MySQL
//HikariConfig config = new HikariConfig("/mnt/vfat/NEW_/eclipse_java_workspace/J_MyBatisSpring/src/datasource/hikari/db.properties");
		
HikariDataSource ds = new HikariDataSource(config);



-------MyBatis3 Spring集成
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
	  <!--myBatis自己的事务
      <property name="transactionFactory" >
            <bean class="org.apache.ibatis.transaction.managed.ManagedTransactionFactory">
            </bean>
        </property>
         -->
	<property name="configLocation" value="classpath:mybatis_spring/Configuration.xml"></property>
	 <!--
	 <property name="configurationProperties">
        <props>
          <prop key="jdbcTypeForNull">NULL</prop>
        </props>
    </property>
	<property name="mapperLocations" value="classpath*:org/zh/mybatis/vo/**/*.xml" />
	<property name="plugins">
		<list>
			<bean class="mybastis_spring.SQLLogInterceptor" />
		</list>
	</property>
	 -->
	    
</bean>
<!-- 你不能在Spring管理的SqlSession上调用SqlSession.commit()，SqlSession.rollback()或SqlSession.close()方法 -->

<!-- 继承 SqlSessionDaoSupport, 注入sqlSessionFactory ,就可以用getSqlSession().selectOne()-->
<bean id="dao" class="mybatis_spring.DaoImpl">
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



import org.springframework.data.domain.PageRequest;



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
@Mapper  //这个没有时,@MapperScan也会生效

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
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.4.0</version>
</dependency>

--generatorConfig.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration   PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
                                      "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
	<!--maven插件就不用这个了-->
    <classPathEntry location="H:/MVN_REPO/mysql/mysql-connector-java/8.0.15/mysql-connector-java-8.0.15.jar" />
		
	<context id="MySQLTables" targetRuntime="MyBatis3"> 
		<!--targetRuntime可选值为 
			MyBatis3,
			MyBatis3Simple 没有生成XxxxSelective, 
			MyBatis3DynamicSql(默认,要mybatis-dynamic-sql包,即使是XMLMAPPER也没有XML文件)
			MyBatis3Kotlin  (要mybatis-dynamic-sql包,即使是XMLMAPPER也没有XML文件)-->
		<commentGenerator>
	        <property name="suppressDate" value="true"/>
	        <property name="suppressAllComments" value="false" />
	        <property name="addRemarkComments" value="true" /> <!-- 使用数据库的comment -->
	        
	    </commentGenerator> 
		<jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/mydb?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC"
			userId="user1"
			password="user1">
		</jdbcConnection>

		<javaTypeResolver >
		  <property name="forceBigDecimals" value="false" />
		</javaTypeResolver>

		<javaModelGenerator targetPackage="org.project.model" targetProject="src/main/java">
		  <property name="enableSubPackages" value="true" />
		  <property name="trimStrings" value="true" />
		</javaModelGenerator>

		<sqlMapGenerator targetPackage="mybatis"  targetProject="src/main/resources">
		  <property name="enableSubPackages" value="true" />
		</sqlMapGenerator>

		<javaClientGenerator type="XMLMAPPER" targetPackage="org.project.mapper"  targetProject="src/main/java">
		  <property name="enableSubPackages" value="true" />
		</javaClientGenerator>

	<!-- 二选一 , 1.3.7 版本 生成的使用类是  @org.apache.ibatis.annotations.Delete  
		 type="ANNOTATEDMAPPER" 也有type="MIXEDMAPPER" 混合式,复杂的SQL用XML(为targetRuntime="MyBatis3",当"MyBatis3Simple"时不行)
		<javaClientGenerator type="ANNOTATEDMAPPER" targetPackage="org.project.annotate"  targetProject="src/main/java">
		  <property name="enableSubPackages" value="true" />
		</javaClientGenerator>
	-->   

		<table  tableName="EMPLOYEE"  >
		  <property name="useActualColumnNames" value="true"/>
		<!-- 
		    <generatedKey column="ID" sqlStatement="MySQL" identity="true" />
		  生成  @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
		 -->
		  <columnOverride column="USERNAME" property="userName" />
		  <columnOverride column="BIRTHDAY" property="brithDay" jdbcType="datetime" />
		  <ignoreColumn column="PASSWORD" />
		</table>
		<table tableName="department" domainObjectName="DepartmentEntity"
			enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" 
			enableSelectByExample="false" selectByExampleQueryId="false">
			<property name="useActualColumnNames" value="false"/>
		</table>
		
	</context>
</generatorConfiguration>

要先建立  src/main/java 和  src/main/resources  目录
md  src\main\java
md  src\main\resources
java -jar mybatis-generator-core-1.4.0.jar  -configfile generatorConfig.xml -overwrite
 
当为MyBatis3时 如 enableXxxByExample都打开，用法如下
//@Autowired
DepartmentEntityMapper  departmentEntityMapper=null;

DepartmentEntityExample param=new DepartmentEntityExample();
param.createCriteria().andDepNameLike("%xx%");
departmentEntityMapper.selectByExample(param);
xml中有使用特殊<if test ="_parameter!=null">  
		
Maven插件方式
<plugin>
		<groupId>org.mybatis.generator</groupId>
		<artifactId>mybatis-generator-maven-plugin</artifactId>
		<version>1.4.0</version>
		<dependencies>
			<dependency>
				<groupId>mysql</groupId>
				<artifactId>mysql-connector-java</artifactId>
				<version>8.0.15</version>
			</dependency>
		</dependencies>
		<configuration> 
			<configurationFile>${basedir}/generatorConfig.xml</configurationFile> 
			<overwrite>true</overwrite>
		</configuration>
</plugin>
mvn mybatis-generator:generate

=====mybatis-dynamic-sql
https://github.com/mybatis/mybatis-dynamic-sql 源码的test 有示例

/*
create table SimpleTable (
   id int not null,
   first_name varchar(30) not null,
   last_name varchar(30) not null,
   birth_date date not null, 
   employed varchar(3) not null,
   occupation varchar(30) null,
   primary key(id)
);
*/
public class SimpleTableRecord {
    private Integer id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String employed;
    private String occupation;
//getter/setter
}
public final class SimpleTableDynamicSqlSupport {
    public static final SimpleTable simpleTable = new SimpleTable();
    public static final SqlColumn<Integer> id = simpleTable.id;
    public static final SqlColumn<String> firstName = simpleTable.firstName;
    public static final SqlColumn<String> lastName = simpleTable.lastName;
    public static final SqlColumn<Date> birthDate = simpleTable.birthDate;
    public static final SqlColumn<Boolean> employed = simpleTable.employed;
    public static final SqlColumn<String> occupation = simpleTable.occupation;
    public static final class SimpleTable extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);
        public final SqlColumn<String> firstName = column("first_name", JDBCType.VARCHAR);
        public final SqlColumn<String> lastName = column("last_name", JDBCType.VARCHAR);
        public final SqlColumn<Date> birthDate = column("birth_date", JDBCType.DATE);
        public final SqlColumn<Boolean> employed = column("employed", JDBCType.VARCHAR, "examples.simple.YesNoTypeHandler");
        public final SqlColumn<String> occupation = column("occupation", JDBCType.VARCHAR);
        public SimpleTable() {
            super("SimpleTable");
        }
    }
}
//@Mapper //作用？？？
public interface SimpleTableAnnotatedMapper {
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<SimpleTableRecord> insertStatement);

    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SimpleTableResult", value= {
            @Result(column="A_ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="first_name", property="firstName", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_name", property="lastName", jdbcType=JdbcType.VARCHAR),
            @Result(column="birth_date", property="birthDate", jdbcType=JdbcType.DATE),
            @Result(column="employed", property="employed", jdbcType=JdbcType.VARCHAR), //, typeHandler=YesNoTypeHandler.class
            @Result(column="occupation", property="occupation", jdbcType=JdbcType.VARCHAR)
    })
    List<SimpleTableRecord> selectMany(SelectStatementProvider selectStatement);

    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);
}
public static void selectSimpleDao( ) throws Exception
{
	String resource = "dynamic_sql/Configuration.xml";
	Reader reader = Resources.getResourceAsReader(resource);
	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);//Properties,Enviroment
	sqlSessionFactory.getConfiguration().addMapper(SimpleTableAnnotatedMapper.class);//加带Annotation的类
	//---
	try (SqlSession session = sqlSessionFactory.openSession()) {
		SimpleTableAnnotatedMapper mapper = session.getMapper(SimpleTableAnnotatedMapper.class);
		//SimpleTableDynamicSqlSupport.*;
		//org.mybatis.dynamic.sql.SqlBuilder.*;
		SelectStatementProvider selectStatement = select(id.as("A_ID"), firstName, lastName, birthDate, employed, occupation)
				.from(simpleTable)
				.where(id, isEqualTo(1))
				.or(occupation, isNull())
				.build()
				.render(RenderingStrategies.MYBATIS3);

		List<SimpleTableRecord> rows = mapper.selectMany(selectStatement);
	 /*
	   List<SimpleTableRecord> rows = mapper.selectMany(new  SelectStatementProvider() {
			@Override
			public Map<String, Object> getParameters()
			{
				Map<String, Object> param=new HashMap< >(); 
				param.put("id", 1);
				return param;
			}
			@Override
			public String getSelectStatement()
			{
				//所有参数要以parameters.开头
				return "select * from SimpleTable where id=#{parameters.id}";
			}
		});
		*/
		assertEquals(rows.size(), 3);
	}
}

public static void selectJoinDao( ) throws Exception
{

	UnpooledDataSource dsUnPool = new UnpooledDataSource("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/mydb", "zh", "123");
	PooledDataSource dsPool = new PooledDataSource("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/mydb", "zh", "123");

	DataSource ds=genHikariDataSource();

	//不使用Config.xml
	Environment environment = new Environment("test", new JdbcTransactionFactory(), ds);
	Configuration config = new Configuration(environment);
	config.addMapper(JoinMapper.class);//加Mapper  ,不能读XML的
	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
	sqlSessionFactory.getConfiguration().addMapper(EmployeeMapper.class); ;//加Mapper
	   try (SqlSession session = sqlSessionFactory.openSession()) {
        	JoinMapper mapper = session.getMapper(JoinMapper.class);
        	  
			SelectStatementProvider selectStatement = select(departmentEntity.id, departmentEntity.depName)
                    .from(departmentEntity, "d")
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
        	List<DepartmentEntity> depts= mapper.fetchMany(selectStatement);
        	System.out.println(depts);
        	   
        	 
        	 selectStatement = select(employee.id, employee.userName,employee.brithDay,employee.department_id)
                       .from(employee, "e")
                       .where(employee.id, isEqualTo(101))
                       .build()
                       .render(RenderingStrategies.MYBATIS3);
        	   
        	 Employee emp= mapper.fetchOne(selectStatement);
        	 System.out.println(emp);  
			 
			  Employee emp1=new Employee();
            emp1.setAge(20);
            emp1.setBrithDay(new Date());
            emp1.setId(8008);
            emp1.setUserName("kotlin");
            emp1.setEmployee_type(2);
            //int effectRow=employeeMapper.insert(emp1);//代码生成的
            //employeeMapper.updateByPrimaryKey(record); 
            //employeeMapper.deleteByPrimaryKey(id);
            
            InsertStatementProvider<Employee> insertStatement = insert(emp1)//值
                    .into(employee)
                    .map(employee.id).toProperty("id")
                    .map(employee.userName).toProperty("userName")//只有写的字段才要
                    .map(employee.employee_type).toProperty("employee_type")
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            System.out.println(insertStatement.getInsertStatement()); 
            //insert into employee (id, username, employee_type) values (#{record.id,jdbcType=INTEGER}, #{record.userName,jdbcType=VARCHAR}, #{record.employee_type,jdbcType=INTEGER})
            int effectRow=employeeMapper.insert(insertStatement);
            session.commit();//默认不是自动提交的
            
            UpdateStatementProvider updateStatement = update(employee)
                    .set(employee.userName).equalTo("kotlin2") 
                    .set(employee.brithDay).equalToNull()
                    .where(employee.id, isEqualTo(8008))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            System.out.println(updateStatement.getUpdateStatement()); 
            effectRow=employeeMapper.update(updateStatement);
			employeeMapper.updateByPrimaryKey(emp1);//里面用到了 UpdateDSLCompleter
			//低版本的mybatis不会生成XxxByPrimaryKey,也用不了
            //employeeMapper.updateByPrimaryKeySelective(record)
            session.commit();
			
			//动态条件
			 String fName="kotlinX";
            //String fName=null;
            UpdateDSL<UpdateModel>.UpdateWhereBuilder builder = update(employee)
                    .set(employee.userName).equalTo("kotlin3")
                    .where(employee.id, isEqualTo(8008));
            builder.and(employee.userName, isEqualTo(fName).when(Objects::nonNull));
            updateStatement = builder.build().render(RenderingStrategies.MYBATIS3);
            effectRow=employeeMapper.update(updateStatement);
            session.commit();
						
            
            DeleteStatementProvider deleteStatement = deleteFrom(employee)
                    .where(employee.id, isEqualTo(8008), and(employee.employee_type, isEqualTo(2)))
                    .or(employee.userName, isLikeCaseInsensitive("kot%"))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            System.out.println(deleteStatement.getDeleteStatement());  
            //delete from employee where (id = #{parameters.p1,jdbcType=INTEGER} and employee_type = #{parameters.p2,jdbcType=INTEGER}) or upper(username) like #{parameters.p3,jdbcType=VARCHAR}
            effectRow=employeeMapper.delete(deleteStatement);
            session.commit();
			 
			 
			 
		   //isIn子查询 ，isLessThan
           selectStatement = select(employee.userName,employee.age)
                    .from(employee, "a")
                    .where(employee.age, isIn(select(employee.age).from(employee).where(employee.department_id, isEqualTo(20))))
                    .and(employee.age, isLessThan(100))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
           System.out.println(selectStatement.getSelectStatement());
           List<Employee> res=  employeeMapper.selectMany(selectStatement);
           List<Employee> records = employeeMapper.select(SelectDSLCompleter.allRows());
			 
			 // group by, count(*) as count 没有having??
        	 selectStatement = select( departmentEntity.depName, count().as("count"),
        			 	SqlBuilder.max(employee.age).as("max_age"),
        			 	SqlBuilder.avg(employee.age).as("avg_age"),
        			 	SqlBuilder.min(employee.age).as("min_age")
        			 	)
                     .from(departmentEntity, "p").join(employee, "e").on(employee.department_id, equalTo(departmentEntity.id))
                     .groupBy(departmentEntity.depName)
                     .build()
                     .render(RenderingStrategies.MYBATIS3);
            System.out.println(selectStatement.getSelectStatement()); 
			
		 //如何做到 ？？？  <collection>多行记录映射成一条记录
          selectStatement = select(departmentEntity.id, departmentEntity.depName,employee.userName, employee.age, employee.brithDay)
                    .from(departmentEntity, "d")
                    .join(employee, "e").on(employee.department_id, equalTo(departmentEntity.id))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
           System.out.println(selectStatement.getSelectStatement()); 
       	   depts= mapper.selectMany(selectStatement);  
           System.out.println(depts)
		}
}
public interface JoinMapper {
  //@Many时column 为(父表的)主键,javaType=List.class
    @Results (value={
        @Result(column="id", property="id"),
        @Result(column="dep_name", property="depName"),
        @Result( many = @Many(select = "queryEmployeeByDeptId"),javaType = List.class,column = "id", property="employees")
    })   
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    List<DepartmentEntity> fetchMany(SelectStatementProvider selectStatement);
    
    
   @Select("select * from employee where department_id=#{value}")
   @Results(value = {
    		@Result(property="userName", column="username"),
    		@Result(property="age", column="age")
       })
   public List<Employee> queryEmployeeByDeptId(@Param("value")String deptId);
    
   
  //@One 时column 是子表的外键
   @Results(value = {
    		@Result(property="userName", column="username"),
    		@Result(property="age", column="age"),
    		@Result( one = @One(select = "queryDepartmentById"),javaType = DepartmentEntity.class,column = "department_id", property="department")
       })
   @SelectProvider(type=SqlProviderAdapter.class, method="select")
   public Employee  fetchOne( SelectStatementProvider selectStatement); 
   
   
   @Select("select * from department where id=#{value}")
   @Results (id="MyDepartment",value={
	        @Result(column="id", property="id"),
	        @Result(column="dep_name", property="depName"),
	    })  
   public DepartmentEntity  queryDepartmentById(int id);
   
 }