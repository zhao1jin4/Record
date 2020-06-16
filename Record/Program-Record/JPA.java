
根据表结构生成@Entity类
idea 中 view -> tool windows -> persistence 选中一个项目 右击->Generate persistence mapping -> by database schema 选择表可以生成带@Entity的类
eclipse要eclipse jee才有data source explorer视图(sts3,sts4没有),可以建立JPA项目,右击项目-> JPA Tools->generate entries from tales 选择表(可以增加级联表)可以生成带@Entity的类
 
Hibernate 5.4 支持JDK 8 或 11 实现JPA2.2

log4j2.xml 配置显示 SQL 参数

<Logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"  additivity="false">
	<AppenderRef ref="Console"/>
	<AppenderRef ref="RollingFile"/>
</Logger>
<Logger name="org.hibernate.type.descriptor.sql.BasicExtractor" level="TRACE"  additivity="false">
	<AppenderRef ref="Console"/>
	<AppenderRef ref="RollingFile"/>
</Logger>

===============JPA=========================
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>5.4.17.Final</version>
</dependency>

<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-hikaricp</artifactId>
	<version>5.4.17.Final</version>
</dependency>
 
<dependency>
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-slf4j-impl</artifactId>
  <version>2.11.1</version>
</dependency>

JPA 没有缓存

都在javax.persitence. 包下

META-INF/persitence.xml 文件
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
        version="2.0">
	<persistence-unit name="MyJPA" transaction-type="RESOURCE_LOCAL"> <!-- JTA,RESOURCE_LOCAL -->
	    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
		 
	    <!-- 
	    <jta-data-source>jndi/myjta</jta-data-source>
	    <mapping-file>META-INF/orm.xml</mapping-file>
	    <exclude-unlisted-classes/>
	   -->
	    <class>jpa.one2one.IdCard</class>
	    <class>jpa.one2one.Person</class>
	    <class>jpa.single.UserBean</class>
		<properties>
		<!-- Hibernate 的配置不能有 value属性,而JPA必须有value属性,Environment,AvailableSettings 类中有所有的hibernate.开头的属性 
 		    <property name="hibernate.dialect" value="org.hibernate.dialect.Oracle10gDialect"/>
 			<property name="hibernate.connection.driver_class" value="oracle.jdbc.driver.OracleDriver" />
			<property name="hibernate.connection.url" value="jdbc:oracle:thin:@localhost:1521:XE" />
			<property name="hibernate.connection.username" value="hr" />
 			<property name="hibernate.connection.password" value="hr" />
			-->
			<!-- 
			<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
			<property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test" />
            <property name="javax.persistence.jdbc.user" value="sa" />
            <property name="javax.persistence.jdbc.password" value="" /> -->
            
        	<!--  使用c3p0
			<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
			<property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/mydb?characterEncoding=UTF-8&amp;serverTimezone=UTC" />
            <property name="javax.persistence.jdbc.user" value="zh" />
            <property name="javax.persistence.jdbc.password" value="123" /> 
			<property name="hibernate.connection.provider_class" value="org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider"/>
			<property name="hibernate.c3p0.max_size" value="15"/>
			<property name="hibernate.c3p0.min_size" value="1"/>
			<property name="hibernate.c3p0.timeout" value="1800"/>
			<property name="hibernate.c3p0.max_statements" value="1800"/>
			<property name="hibernate.c3p0.idle_test_period" value="3000"/>
			<property name="hibernate.c3p0.acquire_increment" value="1"/>
			<property name="hibernate.c3p0.validate" value="false"/>
			 -->
			 <!--  使用 Hikari -->
			 <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
			 <property name="hibernate.connection.provider_class" value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider"/>
             <property name="hibernate.connection.driver_class" value="com.mysql.cj.jdbc.Driver" /> 
             <property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/mydb?characterEncoding=UTF-8&amp;serverTimezone=UTC" /> <!-- Mapped to Hikari’s jdbcUrl setting -->
             <property name="hibernate.connection.username" value="zh" />
             <property name="hibernate.connection.password" value="123" />  
            <property name="hibernate.connection.autocommit" value="false" />  
			
			<property name="hibernate.show_sql" value="true"/>
			<property name="hibernate.hbm2ddl.auto" value="update"/> <!-- update, create, create-drop ,validate-->
			<property name="hibernate.format_sql" value="true"/>
			<property name="hibernate.max_fetch_depth" value="3" />	
			<property name="hibernate.use_sql_comments" value="true"/>
			<!--  
			<property name="hibernate.archive.autodetection" value="class, hbm" />
			默认是class, hbm,如项目中有.hbm.xml日志中显示Detect class: true; detect hbm: true ,可修改为false
			-->
			<property name="hibernate.archive.autodetection" value="false" />
			<!--
			<property name="hibernate.ejb.cfgfile" value="hibernate.cfg.xml" /> 
				日志文件中是有的,最好打开,并<hibernate-mapping auto-import="false">,
			-->
			<property name="hibernate.cache.provider_class" value="org.hibernate.cache.NoCacheProvider" />
		 
 		</properties>
	</persistence-unit>
</persistence>

//如没有配置<provider>会查所有classpath下(在hibernate-core-5.4.17.Final.jar中)的META-INF/services/javaxpersitence.spi.PersistenceProvider文件,有写Provider实现类,可复制
 
Map configOverrides = new HashMap();
//configOverrides.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect ");
configOverrides.put("hibernate.ejb.cfgfile", "hibernate.cfg.xml");//日志文件 中有hibernate.cfg.xml

EntityManagerFactory factory=persitence.createEntityManagerFactory("MyJPA",configOverrides)//MyJPA是persitence.xml中配置的
//Map<String,Object> map=emf.getProperties();//就是在persistence.xml中配置的属性，这里应该可以put
EntityManager em=factory.createEntityManager();
EntityTransaction transaction=em.getTrasaction();
trasaction.begin();

em.persitence(user);//保存对象
em.find(UserBean.class,1);
em.merge(user);
em.clear();
em.refresh(user);//重新从DB取新记录,CascadeType.REFRESH


Student lisi= em.find(Student.class, 1); //相当于Hibernate的get
System.out.println("使用JAP find的学生:"+lisi.getName());

Student wang= em.getReference(Student.class, 2);//相当于Hibernate的load 
System.out.println("使用JAP getReference的学生:"+wang.getName());
		
//Query query=em.createQuery("from Student s where s.name=?",Student.class);//JPSqL
//query.setParameter(1, "lisi");//JPA都是setParameter,JPA和JDBC一样是1开始的,而Hibernate是0开始的,也可在?后加1,?1表示这个位置就是1

Query query=em.createQuery("from Student s where s.name=:myname",Student.class);
query.setParameter("myname", "lisi");
query.setFirstResult(0);
query.setMaxResults(20);
List<Student> list=query.getResultList();//JPA是getResultList

Query sqlQuery=em.createNativeQuery("select count(*) from JPA_STUDENT s,JPA_TEACHER t where s.teacher_id=t.id and t.name=:t_name ");
sqlQuery.setParameter("t_name" , "teacher1");
Object count=sqlQuery.getSingleResult();//JAP是getSingleResult
sqlQuery.setFirstResult(2);
sqlQuery.setMaxResults(20);
System.out.println("JPQL Query查询teacher1老师的学生数:"+count);

sqlQuery=em.createNativeQuery("select  * from JPA_TEACHER ",Teacher.class);
list=sqlQuery.getResultList();
for (Object o :list)
{
	System.out.println("JPQL Query 老师有:"+((Teacher)o).getName());//返回Object[]
}

//批量更新可使用 update 
query=em.createQuery("update Student s set s.name=:my_name where s.id=?1");
query.setParameter("my_name" , "学生XYZ");
query.setParameter(1 ,1);//可以混合使用?1和:标签做参数 
query.executeUpdate();

//--lock
Student lisi=(Student)em.find(Student.class, 2);
em.refresh(lisi, LockModeType.PESSIMISTIC_WRITE);//悲观锁,会加select ... for upate
lisi.setXxxx
//---
em.clear();
Teacher teacher1001=(Teacher)em.find(Teacher.class, 1); //乐观锁 ,对应有@version ,会加ver=?
teacher1001.setXxx

trasaction.commit();
em.close();



import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_USER",indexes = {@Index(name = "IDX_user_name",  columnList="name", unique = true)})
@GenericGenerator(name="myuuid",strategy="uuid")//只有Hibernate的方式
public class UserBean
{
	@Id
	@GeneratedValue(generator="myuuid")//只有Hibernate的方式
	private String id;//为uuid的String类型 
 
	private String name;
}

//=========一对一
@Entity
@Table(name = "T_PERSON")
public class Person
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)//可以放在getXxx前,也可放在属性前 
	public Integer getId()
	{
		return id;
	}
	@Column(length = 10, nullable = false,name="FULL_NAME")
	public String getName()
	{
		return name;
	}
	
	@OneToOne(mappedBy = "person",cascade = CascadeType.ALL)//有mappedBy是主方
	@JoinColumn(name = "IDCARD_ID")
	public IdCard getIdCard()
	{
		return idCard;
	}
}
//---
@Entity
@Table(name = "T_IDCARD")
public class IdCard
{
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH },
			  optional = false, fetch = FetchType.EAGER)//从方来加 optional = false
 	@JoinColumn(name = "PERSON_ID",nullable=false,foreignKey =@ForeignKey(name="FK_IDCARD_PERSON"))
	//生产的索引名是随机的，和外键名不同？？学生表不是的
	public Person getPerson()
	{
		return person;
	}

}


//=================================school_jpa包
//---
@NamedQuery(name="getStudentByTeacher",query="from Student s where s.teacher.id=:teacher_id")

@Entity
@Table(name = "JPA_STUDENT",indexes = {@Index(name = "IDX_JPA_Student_name",  columnList="name", unique = true)})

//方式一SINGLE_TABLE
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.CHAR) //对于SINGLE_TABLE
//@DiscriminatorValue("C")//对于SINGLE_TABLE 

//方式二 JOINED
@Inheritance(strategy=InheritanceType.JOINED)

public class Student implements Serializable 
{

	@Id
	@GeneratedValue
    private int id;
	  
	private String name;
	    
	 
    @Enumerated(EnumType.STRING)//ORDINAL 是枚举索引值,STRING是枚举名字
    @Column(length=6,nullable=false)//enum应该不能为空的
    private Gender gender=Gender.MALE;//默认值 ,是enum类型的
    
    @Temporal(TemporalType.DATE)//日期格式
    private Date birthday;
    
    @Formula("floor((current_date()-birthday)/365)")//只Hibernate有,如birthday为null,会报错,Null给age,如是Oracle使用nvl(floor((current_date-birthday)/365),0)
	//floor((sysdate-birthday)/365) 可Oracle和H2,如H2函数要加(),可是sysdate()或者current_date())  ,floor(MONTHS_BETWEEN(sysdate,birthday)/12) 只Oracle
    private int age;
    
	 //@Embedded 可以不加
    private Favorite fav;
	
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},optional=true)//从方optional=true表示一个学生可以没有老师
    @JoinColumn(name="TEACHER_ID",nullable=false,foreignKey =@ForeignKey(name="FK_JPA_STUDENT_TEACHER"))
   private  Teacher teacher;
    
    @OneToOne(mappedBy="stu",fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE})//有mappedBy是主方
    private Address addr;
    
    //--------------多对多 方式一 ,没有Score实体类,没有其它字段 ,
//    @ManyToMany(fetch=FetchType.LAZY,targetEntity=Course.class,cascade={CascadeType.PERSIST,CascadeType.MERGE})
//    @ForeignKey(name="FK_JPA_score_student")
//    @JoinTable(name="JPA_SCORE",joinColumns=@JoinColumn(name="student_id"),
//    				 	 inverseJoinColumns=@JoinColumn(name="course_id")  )
    @Transient //不与数据库交互
    private Set<Course> courses;
    //--------------多对多 方式二  两个一对多,有Sore实体类及映射,多score字段
    @OneToMany(mappedBy="student",cascade={CascadeType.PERSIST,CascadeType.MERGE},targetEntity=Score.class)
    private Set<Score>scores;
    //--------------
    
    @Lob
    @Basic(fetch=FetchType.LAZY)//大的字段延迟加载
    private byte[] photo;
   
    @Lob
    @Basic(fetch=FetchType.LAZY)
    private String remark;
}
//---
@Embeddable //类似于<component>
public class Favorite 
{
}
//---
@Entity

//方式一SINGLE_TABLE
//@DiscriminatorValue("G")//相当于<subclass discriminator-value="G"

//方式二 JOINED
@PrimaryKeyJoinColumn(name="student_id",foreignKey =@ForeignKey(name="FK_GOOD_STUDENT") )//相当于<joined-subclass
@Table(name="JPA_GOOD_STUDENT")

public class GoodStudent extends Student {
}
//---
@Entity
//方式一SINGLE_TABLE
//@DiscriminatorValue("L")

//方式二 JOINED
@PrimaryKeyJoinColumn(name="student_id",foreignKey =@ForeignKey(name="FK_LEAD_STUDENT") )//相当于<joined-subclass
@Table(name="JPA_LEADER_STUDENT")
public class LeaderStudent extends Student {
}
//---
@Entity
@Table(name="JPA_TEACHER")
public class Teacher implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
    @OneToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH} ,
    			fetch=FetchType.LAZY,mappedBy="teacher",targetEntity=Student.class)//有mappedBy是主方
    private Set<Student> students;
    
    @Version
    private int ver;
}
//---
@Entity
@Table(name="JPA_COURSE")
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String name;
    
	//-------------- 多对多 方式一 ,没有Score实体类,没有其它字段 ,
    //@ManyToMany(mappedBy="courses",fetch=FetchType.LAZY,targetEntity=Student.class,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    //@ForeignKey(name="FK_JPA_score_course")
    @Transient //不与数据库交互
    private Set<Student>students;
    //--------------多对多 方式二  两个一对多,有Sore实体类及映射,多score字段
    @OneToMany(mappedBy="course",cascade={CascadeType.PERSIST,CascadeType.MERGE},targetEntity=Score.class)
    private Set<Score>scores;
}

//---
@Entity
@Table(name="JPA_ADRESS")
public class Address 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@OneToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},optional=true,fetch=FetchType.EAGER)//address必须要有对应的student
	@JoinColumn(name="stu_id",foreignKey =@ForeignKey(name="FK_JPA_ADDRESS_STUDENT") ,nullable=false)//和optional=true类似
 	private Student stu;
}


//--------------多对多 方式二  两个一对多,有Sore实体类及映射,多score字段
@Entity
@Table(name="JPA_SCORE")
public class Score implements Serializable 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ManyToOne(optional=false,cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="student_id",nullable=false,foreignKey =@ForeignKey(name="FK_JPA_score_student") ) 
	private Student student;
	
	@ManyToOne(optional=false,cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="corse_id",nullable=false,foreignKey =@ForeignKey(name="FK_JPA_score_course") )
	private Course course;
	
	private float score;
}	

抽象类前加
@MappedSuperclass


em.createNamedQuery("HistoryEntry.findByEntity")//"JPSQL"类似HQL，对应于 @NamedQuery(name = "HistoryEntry.findByEntity", query = "
	.setParameter("xx",yy);

JPA  没有加索引 功能
	没有缓存
 

@Column(nullable=false,name="")

hibernate 包 @GenericGenerator(name="myuuid" strategy="uuid")


@Inheritance(strategy=Inheritancetype.SINGLE_TABLE)   //多类在一表
@DiscriminatorColumn(name="type");
@DiscriminatorValue("o");

@ManyToOne()   //表示本类和　get方法前的关系
@JoinCOlumn(name="dep_id")//可以不写的JPA默认是属性名_id ,而hibernate 只是属性名


@OneToMany (casecade={CaseCadeType.MERGE,CascadeType.PERSIT},fetch=FetchType.LAZY,mappedBy="depart")// 怎么知道集合中,每个类的哪个属性是我呢
	如类不是用<xx>范型,还要加 targetEntity=M.class




	/*
-- MySQL 存储过程
delimiter //
CREATE PROCEDURE teacherid_count  ( 
    IN teacherId INT,  
    OUT Count INT  
 )  
 BEGIN  
     SELECT COUNT(*) INTO  Count  
     FROM jpa_student t 
     WHERE t.TEACHER_ID = teacherId ;
 END//
delimiter ;
 
 */
EntityManagerFactory emf =  Persistence.createEntityManagerFactory("MyJPA");
EntityManager entityManager = emf.createEntityManager();
   
StoredProcedureQuery query = entityManager.createStoredProcedureQuery( "teacherid_count");
query.registerStoredProcedureParameter( "teacherId", Long.class, ParameterMode.IN);
query.registerStoredProcedureParameter( "Count", Long.class, ParameterMode.OUT);

query.setParameter("teacherId", 1L);

query.execute();
Long Count = (Long) query.getOutputParameterValue("Count");
System.out.println("Count="+Count);


-- 返回做映射
@NamedNativeQueries({
	@NamedNativeQuery(name = "find_person_card", 
			query = "select p.id as pid ,p.full_name,i.id as carid ,i.cardno  from t_idcard i,t_person p  where i.person_id=p.id and p.full_name like :name", 
			resultSetMapping = "person_with_card")
})
@SqlResultSetMapping(name = "person_with_card", entities = 
		{
				@EntityResult(entityClass = M_Person.class, 
				fields = 
				{
				  @FieldResult(name = "id", column = "pid"),
				  @FieldResult(name = "name", column = "full_name"), 
				}),
				@EntityResult(entityClass = M_IdCard.class, fields =
				{ @FieldResult(name = "id", column = "carid"),
				  @FieldResult(name = "cardno", column = "cardno"), 
				}) 
		})//如有一对一关系，测试下来不行的

@Entity
@Table(name = "M_PERSON")
public class M_Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Column(length = 10, nullable = false, name = "FULL_NAME")
	private String name;
}

---主键增长方式，oracle sequence,mysql表记录


===============上 JPA=========================
   

---spring 集成
  <dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-jpa</artifactId>
  </dependency>


<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-releasetrain</artifactId>
      <version>Neumann-SR1</version>  <!-- 使用spring 5.2.7 -->
      <scope>import</scope>
      <type>pom</type>
    </dependency>
  </dependencies>
</dependencyManagement>

---queryDSL  JPA

<querydsl.version>4.3.1</querydsl.version>


<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-jpa</artifactId>
  <version>${querydsl.version}</version>
</dependency>

 <plugin>
	<groupId>com.mysema.maven</groupId>
	<artifactId>apt-maven-plugin</artifactId>
	<version>1.1.3</version>
	<executions>
	  <execution>
		<goals>
		  <goal>process</goal>
		</goals>
		<configuration>
		  <outputDirectory>target/generated-sources/java</outputDirectory>
		  <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
		</configuration>
	  </execution>
	</executions>
	<dependencies>
	  <dependency>
		<groupId>com.querydsl</groupId>
		<artifactId>querydsl-apt</artifactId>
		<version>${querydsl.version}</version>
	  </dependency>
	</dependencies>
  </plugin>




