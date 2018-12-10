下载   http://repo.spring.io/libs-release/

<properties>
	<spring.version>5.0.5.RELEASE</spring.version>
	<spring-security.version>5.0.5.RELEASE</spring-security.version>
</properties>
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.0.RELEASE</version>
</parent>
<dependencies>

	<dependency>
		<groupId>org.aspectj</groupId>
		<artifactId>aspectjweaver</artifactId>
		<version>1.7.4</version>
	</dependency>
		
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-aspects</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-aop</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context-support</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-jms</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-test</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-tx</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-orm</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-oxm</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-web</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-websocket</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-webmvc</artifactId>
		<version>${spring.version}</version>
	</dependency>
 
	<!-- security -->
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-web</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-taglibs</artifactId>
		<version>${spring.version}</version>
	</dependency>	
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-config</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-crypto</artifactId>
		<version>${spring.version}</version>
	</dependency>
	
	<!-- ldap -->
	<dependency>
		<groupId>org.springframework.ldap</groupId>
		<artifactId>spring-ldap-core</artifactId>
		<version>2.3.1.RELEASE</version>
	</dependency>

	<!-- boot  要parent-->
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
	<dependency>
		<groupId>org.springframework.session</groupId>
		<artifactId>spring-session-data-redis</artifactId>
		<version>2.0.4.RELEASE</version>
	</dependency>

	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-webmvc-portlet</artifactId>
		<version>4.3.18.RELEASE</version>  
	</dependency>
	
	<!-- data -->
	<dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-hadoop</artifactId>
        <version>2.1.2.RELEASE</version>
    </dependency>
	<dependency>
	  <groupId>org.springframework.data</groupId>
	  <artifactId>spring-data-mongodb</artifactId>
	  <version>2.1.1.RELEASE</version>
	</dependency>
	
	<dependency>
		<groupId>org.springframework.data</groupId>
		<artifactId>spring-data-redis</artifactId>
		<version>2.0.5.RELEASE</version>
	</dependency>
	 <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-neo4j</artifactId>
        <version>5.1.0.RELEASE</version>
    </dependency>
	
	<dependency>
	  <groupId>org.springframework.data</groupId>
	  <artifactId>spring-data-solr</artifactId>
	  <version>1.2.0.RELEASE</version>
	</dependency>
	<dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-rest-webmvc</artifactId>
        <version>2.3.0.RELEASE</version>
    </dependency>
	<dependency>
		<groupId>org.springframework.data</groupId>
		<artifactId>spring-data-jpa</artifactId>
		<version>1.7.2.RELEASE</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework.data</groupId>
	  <artifactId>spring-data-keyvalue</artifactId>
	  <version>1.2.1.RELEASE</version>
	</dependency> 
	
	<!-- other -->
	<dependency>
		<groupId>org.springframework.batch</groupId>
		<artifactId>spring-batch-core</artifactId>
		<version>3.0.6.RELEASE</version>
	</dependency>
	<!--
	<dependency>
		<groupId>org.springframework.retry</groupId>
		<artifactId>spring-retry</artifactId>
		<version>1.1.2.RELEASE</version>
	</dependency>
	-->
	
	<dependency>
        <groupId>org.springframework.hateoas</groupId>
        <artifactId>spring-hateoas</artifactId>
        <version>0.19.0.RELEASE</version>
    </dependency>
	<dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
        <version>2.1.5.RELEASE</version>
    </dependency>
	
</dependencies>
 
doc URL
http://docs.spring.io/spring-framework/
http://docs.spring.io/spring-batch/
http://docs.spring.io/spring-data/
http://docs.spring.io/spring-security/site/docs/
http://docs.spring.io/spring-hateoas

spring-data-solr

CRUD(Create,Retrieve,Update,Delete)

web.xml中
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>/WEB-INF/application-dao.xml /WEB-INF/application-web.xml</param-value>
	<!-- 多个用空格分隔,可用classpath:,或者spring_*.xml -->
</context-param>
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>


<context-param>
	<param-name>log4jConfigLocation</param-name>
	<param-value>/WEB-INF/log4j.properties</param-value>
</context-param>
<listener>
	<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
</listener>

<filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>GBK</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
</filter>
<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>*.mvc</url-pattern>
</filter-mapping>

spring tools suite 3.x 新建文件选 spring/Spring Bean Definition 可以选择名称空间
Intellij Idea 没有

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xmlns:util="http://www.springframework.org/schema/util"
    xmlns:tool="http://www.springframework.org/schema/tool"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xmlns:lang="http://www.springframework.org/schema/lang"
    xmlns:jms="http://www.springframework.org/schema/jms"
    xmlns:task="http://www.springframework.org/schema/task"
    xmlns:cache="http://www.springframework.org/schema/cache"
    xmlns:oxm="http://www.springframework.org/schema/oxm"
    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xmlns:websocket="http://www.springframework.org/schema/websocket"
    xmlns:security="http://www.springframework.org/schema/security"
    xmlns:redis="http://www.springframework.org/schema/redis"
    xmlns:mongo="http://www.springframework.org/schema/data/mongo"
    xmlns:jpa="http://www.springframework.org/schema/data/jpa"
	xmlns:neo4j="http://www.springframework.org/schema/data/neo4j"
    xmlns:hdp="http://www.springframework.org/schema/hadoop"	
    xmlns:batch="http://www.springframework.org/schema/batch"
	xmlns:rabbit="http://www.springframework.org/schema/rabbit"
	xmlns:ldap="http://www.springframework.org/schema/ldap"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:c="http://www.springframework.org/schema/c"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context.xsd
                        http://www.springframework.org/schema/aop
                        http://www.springframework.org/schema/aop/spring-aop.xsd
                        http://www.springframework.org/schema/tx
                        http://www.springframework.org/schema/tx/spring-tx.xsd
                        http://www.springframework.org/schema/util
                        http://www.springframework.org/schema/util/spring-util.xsd
                        http://www.springframework.org/schema/tool
                        http://www.springframework.org/schema/tool/spring-tool.xsd
                        http://www.springframework.org/schema/jee
                        http://www.springframework.org/schema/jee/spring-jee.xsd
                        http://www.springframework.org/schema/jms
                        http://www.springframework.org/schema/jms/spring-jms.xsd
                        http://www.springframework.org/schema/task
                        http://www.springframework.org/schema/task/spring-task.xsd
                        http://www.springframework.org/schema/cache
                        http://www.springframework.org/schema/cache/spring-cache.xsd
                        http://www.springframework.org/schema/lang
                        http://www.springframework.org/schema/lang/spring-lang.xsd
                        http://www.springframework.org/schema/jdbc
                        http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
                        http://www.springframework.org/schema/oxm
                        http://www.springframework.org/schema/oxm/spring-oxm.xsd
                        http://www.springframework.org/schema/mvc
                        http://www.springframework.org/schema/mvc/spring-mvc.xsd
                        http://www.springframework.org/schema/websocket
                        http://www.springframework.org/schema/websocket/spring-websocket.xsd
                        http://www.springframework.org/schema/security
                        http://www.springframework.org/schema/security/spring-security.xsd
                        http://www.springframework.org/schema/redis
                        http://www.springframework.org/schema/redis/spring-redis.xsd
                        http://www.springframework.org/schema/data/mongo
                        http://www.springframework.org/schema/data/mongo/spring-mongo.xsd 
						http://www.springframework.org/schema/data/jpa
                        http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
						http://www.springframework.org/schema/data/neo4j    
                        http://www.springframework.org/schema/data/neo4j/spring-neo4j.xsd
						http://www.springframework.org/schema/hadoop
                        http://www.springframework.org/schema/hadoop/spring-hadoop.xsd
                        http://www.springframework.org/schema/batch
                        http://www.springframework.org/schema/batch/spring-batch.xsd
						http://www.springframework.org/schema/rabbit 
                        http://www.springframework.org/schema/rabbit/spring-rabbit.xsd
						http://www.springframework.org/schema/ldap 
						http://www.springframework.org/schema/ldap/spring-ldap.xsd">
    
						
 :p 表示 property
 :c 表示 contruction 
 
eclipse提示选择	#xsinsp	生成	 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				#schemaLoc 生成	 xsi:schemaLocation="{namespace} {location}"
也有对应的名称空间,也就是说可以从这里复制

						
JSP中使用Spring
ServletContext context=request.getSession().getServletContext();//this.getServletConfig().getServletContext()
ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);//获取失败抛出异常,看源码调用了 getWebApplicationContext
	WebApplicationContextUtils.getWebApplicationContext(sc);//获取失败返回null
		

FileSystemResource ("xml文件");
ClassPathResource ("xml文件");
 
<beans>
	<import resource="...xml文件">//在<bean>之前

	<bean id="" name="" 中的id的值不可有/符号,而name的值是可以有/符号的
	
	<!--静态工厂实例 -->
	<bean id="driverClass" class="java.lang.Class" factory-method="forName"> 
        <constructor-arg><value>${jdbc.driverClass}</value></constructor-arg>
    </bean>
	
	<!--实例工厂实例 -->
    <bean id="driverInstance" class="java.lang.Class" factory-method="newInstance" factory-bean="driverClass" depends-on="driverClass" />

	scope="prototype" 每次从容器中取都是新实例  
	scope="singleton"
	scope="session"
	
	lazy-init="true" ,	@Lazy 只在第一次使用时才创建实例,而不是在容器启动时创建实例
	abstract="true" 
	autowire="byName" 
	depends-on="driverClass"  @DependsOn

<map>
	<entry key="one" value="1111"></entry>
</map>
<util:map>
	<entry key="two" value="222"></entry>
</util:map>

<!-- 放在property外部,可以指定id,class -->
<util:map id="myMaps" map-class="java.util.TreeMap" key-type="java.lang.String" value-type="java.lang.Integer"> 
	<entry key="one" value="1111"></entry>
	<entry key="two" value="222"></entry>
</util:map>

<set>
	<value>set one</value>
</set>
<util:set>
	<value>set two</value>
</util:set>

<props>
	<prop key="max">200</prop>
</props>
<!-- util:properties 就是用 PropertiesFactoryBean -->
<util:properties>
	<prop key="max">200</prop>
</util:properties>
<util:properties id="jdbcProperties" location="classpath:jdbc.properties"/>
 
<!--list 和数组都使用-->
<list>
	<value>list one</value>
</list>
<util:list>
	<value>list two</value>
</util:list>

<constructor-arg index="0"  type="java.lang.Integer" value="20"/>
<constructor-arg  name="age" value="20"/>


<jee:jndi-lookup id="dataSource" jndi-name="jdbc/MyDataSource"/>


<context:annotation-config /> <!--使用 annotation方式来注入,就可以使用@Autowire 默认是按type,  @Resource(name="")是JDK的, 默认是按与属性名相同 -->
<context:annotation-config/> 将隐式地向 Spring 容器注册 AutowiredAnnotationBeanPostProcessor , CommonAnnotationBeanPostProcessor , PersistenceAnnotationBeanPostProcessor 以及 equiredAnnotationBeanPostProcessor


javax.annotation.Resource;
 <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"/>来处理@Resource

@Autowired(required=true) @Qualifier("myhome")//@Autowired默认是按类型,required=true表示必须注入,使用@Qualifier表示按名称

<!-- 该 BeanPostProcessor 将自动起作用，对标注 @Autowired 的 Bean 进行自动注入 　->
<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/>

@Repository("") //Dao层,id的值默认为类名,第一个字母变小写
@Service //服务层
@Scope("prototype")//多实例
--Java标准
@PostConstruct //放在方法前,相当于init-method,是javax.annotation.PostConstruct,可以加多个
@PreDestroy //放在方法前,相当于destory-method,是javax.annotation.PreDestroy ,要单例,调用AbstractApplicationContext 的close();
@Inject  javax.inject.Inject(如要想要标识加@Named(""),可放在setter方法参数前) 相当于 @AutoWire 按 byType自动注入
@Resource 默认按 byName 自动注入
@Named("myBean") javax.inject.Named 相当于 @Component 和 @Qualifier
@Singleton javax.inject.Singleton 相当于 @Scope("singleton")


<aop:aspectj-autoproxy /> <!-- 就可以使用@的方式做代理配置 ,@Aspect,@Pointcut,@Before -->
<bean id="abcService" class="spring_annotation.ABCService"></bean>
<bean id="myInterceter" class="spring_annotation.MyIntercepter"></bean>

//@方式
@Aspect
public class MyIntercepter 
{	
	@Before("execution(* spring_annotation.ABCService.*(..))")//OK ,execution后不能有空格,不用@Pointcut
	public void myBefore()
	{
		System.out.println("业务方法之前");
	}
	
	@Before("execution(* spring_annotation.ABCService.*(..)) && args(username)")//username要和参数中一致
	public void myBefore(String username)
	{
		System.out.println("业务方法之前,得到参数:"+username);
	}
	
	@AfterReturning("execution(* spring_annotation.ABCService.*(..))")
	public void myAfterReturning()
	{
		System.out.println("业务方法之后@AfterReturning");
	}
	
	@AfterReturning(pointcut="execution(* spring_annotation.ABCService.*(..))",returning="result")//result与方法参数对应
	public void myAfterReturning(String result)//对业务方法返回String的
	{
		System.out.println("业务方法之后@AfterReturning,得到返回值:"+result);
	}
	
	@After("execution(* spring_annotation.ABCService.*(..))")//在@AfterReturning之后,相当于在finally块中
	public void myAfter()
	{
		System.out.println("业务方法 最终 @After");
	}
	
	@AfterThrowing("execution(* spring_annotation.ABCService.*(..))")//相当于在catch块中
	public void myException()
	{
		System.out.println("业务异常的拦截");
	}
	
	@AfterThrowing(pointcut="execution(* spring_annotation.ABCService.*(..))",throwing="exception")//相当于在catch块中
	public void myException(Exception exception)
	{
		System.out.println("业务异常的拦截,异常原因是:"+exception.getMessage());
	}
	
	@Around("execution(* spring_annotation.ABCService.*(..))")
	public void myAround(ProceedingJoinPoint point) throws Throwable //必须有参数ProceedingJoinPoint
	{
		System.out.println("进入 @Around");//在@Before之后
		//可做权限判断
		point.proceed();//必须调用这个方法,才可调用到业务方法
		System.out.println("退出 @Around"); //在@AfterReturning之后,@After之前 
	}
}

<alias name="myInterceter" alias="abcInterceter"/> <!-- 别名 -->
<bean id="myInterceter" class="spring_tag_aop.MyIntercepter"></bean>
//标签配置方式
 <aop:config proxy-target-class="true"> <!--使用cglib代理-->
	<aop:aspect id="myAspect"  ref="abcInterceter"><!-- 引用别名 -->
		<aop:pointcut id="myPointcut" expression="execution(* spring_tag.ABCService.*(..))" />
		<!-- expression的写法,返回值 可以是 !void ,参数可以是 (java.lang.String,..)  ,* spring_tag..*.*(..),两点 表示子级包,所有 类所有方法-->
		<aop:before pointcut-ref="myPointcut" method="myBefore"/>
		<aop:after-returning pointcut-ref="myPointcut" method="myAfterReturning"/>
		<aop:after-throwing pointcut-ref="myPointcut" method="myException"/>
		<aop:after pointcut-ref="myPointcut" method="myAfter"/>
		<aop:around pointcut-ref="myPointcut" method="myAround"/> 
		<!--MyIntercepter 中的方法 public void myAround(ProceedingJoinPoint point)
		point.getTarget().getClass().getSimpleName()
		point.getSignature().getName()
		-->
	</aop:aspect>
</aop:config>


对自定义的@做拦截
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Caches {
	String prefixKey() default ""; //缓存key前缀
}

@Aspect
public class AnnoAspect 
{	
	@Pointcut("@annotation(spring_aop_aspectj_anno.Caches)")  
    public void setCached(){}
	
	@Around("setCached()")//OK
	//@Around("@annotation(spring_aop_aspectj_anno.Caches)")//OK
	public Object aroundMethod(ProceedingJoinPoint point) 
	{
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Caches caches=method.getAnnotation(Caches.class);
		Object result = null;  
		if(StringUtils.isNotBlank(caches.prefixKey()))
		 {
			  System.out.println("config prefixKey="+caches.prefixKey());
		 }
		 try 
		 {
		 	System.out.println("before @Caches");
			result = point.proceed();
			System.out.println("after @Caches");
		} catch (Throwable e) { 
			e.printStackTrace();
		}   
        return result;  
    }  
}

<aop:aspectj-autoproxy /> 
<bean id="myInterceter" class="spring_aop_aspectj_anno.AnnoAspect"></bean> 
<bean id="annoService" class="spring_aop_aspectj_anno.AnnoService"></bean> 
	
或者 

@Configuration
@EnableAspectJAutoProxy
class Config
{
	@Bean
	public AnnoAspect newAspect()
	{
		return new AnnoAspect();
	}
	@Bean
	public AnnoService newService()
	{
		return new AnnoService();
	}
}



//对象复制
 private static ConcurrentHashMap<String,BeanCopier> cache=new ConcurrentHashMap<String, BeanCopier>();
 public static <T> T copyBeanProperties(Object sourceObj, T target, boolean useConverter)
 {
	if(sourceObj==null || target==null) 
		return null;
	
	String key=sourceObj.getClass().getSimpleName()+target.getClass().getSimpleName();
	BeanCopier copier = cache.get(key);
	if(copier==null){
		copier = BeanCopier.create(sourceObj.getClass(), target.getClass(), useConverter);
		cache.putIfAbsent(key, copier);
	}
	copier.copy(sourceObj, target, null);//是调用的getter/setter方法,双方不同类都有匹配不上的字段也可正常用
	//网上查cglib 的BeanCopier 性能还不错 ,还有commons beanutils
	return target;
}
------Hibernate 3 集成

LocalSessionFactoryBean
	mappingLocations
	
	<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory"/>
	</bean>

	<bean id="userService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
		<property name="target" ref="userServiceTarget"/>
		
	    <property name="transactionManager" ref="transactionManager"/>
	    <property name="transactionAttributes">
	      <props>
	        <prop key="registUser">PROPAGATION_REQUIRED</prop>
	      </props>
	    </property>
 	</bean>
------
BeanWrapper接口  不推荐
可以像简单的ognl表达式一样


<bean destroy-method="" init-method="初始化bean的方法"


数组用<list>
<map> <entry key="" (value="")>(<ref bean="">) </entry>
<set> 的<value>或<ref bean="">
<props><prop>

autowire="byType或,byName或 constructor或autodetect,default"


org.springframework.beans.factory.InitializingBean 接口一个afterPropertiesSet()会在所有初始化完成后被调用

org.springframework.beans.factory.DisposableBean 接口一个distroy()方法  或 distroy-method

 
============================================孙鑫

(国际化) AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
	<property name="basename" 是en_US前的部分名字

使用bean可以是BeanWrapper(不推荐),BeanFactory ,ApplicationContext
(接口)BeanFactory f=new XmlBeanFactory(new FileSystemResource(new File||String path))
FileSystemXmlApplicationContext


MessageSource 的子类ApplicationContext的
		getMessage(String code, Object[] args, String defaultMessage,Locale locale)
		properties 文件中的key,{0}或多个的数组，找不到key时的默认值，Locale.US
		如不指定默认值时会抛出NoSuchMessageException


native2ascii -encoding utf-8(源文件的编码) source.properties source_zh.properties 


实现 ApplicationEventPublisherAware ,spring会给你 ApplicationEventPublisher

监听
org.springframework.context.ApplicationEvent抽象类无抽象方法
org.springframework.context.ApplicationListener接口的一个方法onApplicationEvent(ApplicationEvent event) 

org.springframework.context.AplicationContextAware接口的一个方法setApplicationContext(ApplicationContext applicationContext)
ApplicationContext的publishEvent(ApplicationEvent event)



ApplicationContext 定义了一个getSource(String location )返回一个Resource 可以用getInputStream()方法
 字符串用"file:C:/test.dat"或"classpath:test.dat". 并不一定存在，exists() 来验证

Resource 的	实现类有ClassPathResource 和FileSystemResource
当用FileSystemXmlApplicationContext ，会使用FileSystemResource


GenericApplicationContext 的getResource("classpath:hello_en.properties").getFileName()//也可是file:E/xx,或者WEB-INF/xx
也可实现ResourceLoaderAware接口,
或者Bean类的属性是Resource,配置是可以传file:或者classpth:开头的字串

observer观测者 设计模式,即监听
ApplicatonContextAware spring会给你注入ApplicationContext()是一个接口注入的列子（如doPost(HttpServletRequest)）
ApplicationListener.getResource();得到的是实现ApplicationEvent类的构造方法supper(Object o)的o
 
 
 JDK 5.0支持import static 后是类的static 的属性；
 
1. ContextClosedEvent(ApplicationContext已关闭)
2. ContextRefreshedEvent(ApplicationContext已被初始化或被刷新)
3. RequestHandledEvent (只对Spring 的MVC有效)
 
 如即实现了 InitializingBean 接口，和<bean init-method="">, 会先执行接口的方法，再执行init-method的方法
  所有的bean 都初始化了再如上的调用
 ApplicationContext的实现类有一个close()方法 ,销毁所有的bean,会调用DisposableBean的destroy 方法
 
 
 单例spring 才能动管理bean 可以销毁bean 
  如即实现了 DisposableBean 接口，和destroy-method, 会先执行接口的方法，再执行destory-method的方法
  为了不和spring偶合在一起(实现接口),可以使用init-method和destroy-method
  如果sigleton=false还是看不到destroy-method指定的方法的执行,因spring 不能管理它
 BeanFactoryAware接口的一个BeanFactoryAware（）ApplicationContextAware同，会有创建这个bean的BeanFactory引用
 
 BeanNameAware spring  会把bean的名字(id)注入给你setBeanName(String name) ,在所有bean setXXX后，init-mehtod前和InitializingBean前

 BeanClassLoaderAware
 
 
 1.bean 构造
 2.setXXX
 3.BeanNameAware 的 setBeanName(String name)会把配置中的bean 的id 传给name
 4.BeanFactoryAware 的 setBeanFactory()  ==ApplicationContextAware
 5.BeanPostProcess 的 postProcessBeforeInitialization();
 6.InitializingBean 的 afterProperitesSet();
 7.自定义初始化 init-method
 8.BeanPostProcessor 的 postProcessAfterInitializaion();
 9.DisposableBean 的 destroy()
 10.自定义的销毁化方法 destroy-method
 
 
 BeanPostProessor在用ApplicationContext  时会自动检测是不是BeanPostProcessor，是就注册
 			BeanFactory 要手工注册ConfigurableBeanFactory类的addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
 			BeanFactory只有getBean才会初始化Bean,ApplicationContext 是直接初始化全部，（lazy="true"除外）
 	 Object postProcessAfterInitialization(Object bean, String beanName) 

	 正常应该返回bean
 	 bean是在<bean 中配置的类，beanName  是id 		
 	  
 BeanFactory在getBean的时候会返回实例,而ApplicationContext会在初始化时全部实例
 
 用BeanFactory手工注册
	1. BeanFactoryPostProcessor 的实现（PropertyPlaceholderConfigurer） 接口的一个postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory子类是 XmlBeanFactory ）
 	实例化任何的Bean之前，读完配置数据并修改它
 在配置文件的值中	用${key}

  (BeanFactory)手工设 1)PropertyPlaceholderConfigurer的 setLocation(Resource location)     --properties 文件位置
						2)postProcessBeanFactory(beanfactory)																					file:xxx

  	 PropertyResourceConfigurer ,PropertyPlaceholderConfigurer.BeanNameAutoProxyCreator是实现类
	2. 可在bean实例任何bean前，可读，修改配置
 	 Applicationcontext 会自动检测 如BeanFactoryPostProcessor没有会到VM变量里找(-Dkey=value)
 PropertyPlaceholderConfigurer	 setSystemPropertiesMode(int systemPropertiesMode) 三个值要写数字，查看文档，
		
setSystemPropertiesModeName （String ）直接用大写字串
	 SYSTEM_PROPERTIES_MODE_FALLBACK 如properties 文件没有，去系统属性   默认
	 SYSTEM_PROPERTIES_MODE_NEVER 从不到系统属性
	 SYSTEM_PROPERTIES_MODE_OVERRIDE  先是系统属性，再properties文件

 PropertyPlaceholderConfigurer 有一个location（file:xx.properties,classpath:xx.properties）	 属性指定properties 文件的位置。就可以用${key} 设在<value>里
 不用配置id=""
 PropertyOverrideConfigurer相比上一个，可以无值（<bean>中不配置属性的值,在properties 文件用beanName.properties=xx）即默认值，
 在properties 文件中要beanName.key的形式来使用，如不存在，则会使用applicationContext的配置(优先使用properties文件中的,不推荐)
 
 java.beans包下类 PropertyEditorSupport implements PropertyEditor
 	有一个setAsText（String text）根据字串生成对象,在里面调用this.setValue(Object)
org.springframework.beans.factory.config.CustomEditorConfigurer 文档有列子  配到spring 中
 <property name="customEditors">
     <map>
      <entry key="java.util.Date">
         <bean class="mypackage.MyCustomDateEditor"/> <!--也有spring的 CustomDateEditor-->,如使用BeanWapper,要调用registerCustomEditor来注册
       </entry>


 	<value>
 	<idref local=""/>　<!-- 新版本不能local ,可以用bean-->
 	
 	
 	<bean class="FactoryBean实现" 
 时返回的是实现FactoryBean接口的getObject()方法的对象
 如想得到FactoryBean本身要在getBean("&beanName");就不会调用getObject()
 	
 	Proxy.newInstance(对象.getClass().getClassLoader,Class接口,InvocationHandler )
 	java.reflect.InvocationHandler
 	
 Class a;
a.isAssignableFrom(Class b) 
如a是b父类(接口)返回true,

	ProxyFactoryBean  setProxyInterfaces(String[] interfaceNames)
		即调用了JDK的Proxy.newProxyInstance 可以不写，但是类要implements 接口，如类没有实现接口要加cblib.jar
									setTarget(Object target)
									setInterceptorNames(String[] interceptorNames)    --MethodBeforAdvice

 标记为final 的方法不能被通知
 
 
 MehtodInterceptor 接口，是aopalliance的，中方法public Object invoke(MethodIvocation invocation)
 {
 	xx.class.isAssignalbeFrom(invocation.getMethod().getDeclaringClass())
 	invocation.proceed();
 }
 	
 
-----实现cglib的 MethodInterceptor
Class myc=null;
Enhancer enhancer=new Enhancer();
	public Object getInstrumentedClass(Class clz)
	{
		myc=clz;
		
		enhancer.setSuperclass(clz);
		
		enhancer.setCallback(this);
		return enhancer.create();
	}
	public Object intercept(Object o, Method method, Object[] args,
			MethodProxy proxy) throws Throwable
	{
		System.out.println("调用日志方法" + method.getName());
		//Object result = proxy.invokeSuper(o, args);
	
		//Object result = method.invoke(myc.newInstance(), args);
		Object result = proxy.invoke(myc.newInstance(), args);
		return result;
	}
----- aopalliance 的  MethodInterceptor
	
	implements org.aopalliance.intercept.MethodInterceptor
	{	public Object invoke( MethodInvocation invocation)  throws Throwable 
		{
			if(Ilogin.class.isAssignableFrom(invocation.getMethod().getDeclaringClass()))//可不调用目标方法,可返回特殊值
			{
			}
			System.out.println("mehtod before invoke;");
			Object o=invocation.proceed();
			System.out.println("mehtod after invoke;");
			return o;
		}
	}
-----

	ProxyFactoryBean  getObject()
	ProxyFactory 	getProxy()
		if(factory.adviceIncluded(Advice advice) )
			factory.removeAdvice(Advice advice) 

	MethodBeforeAdvice
	AfterReturningAdvice 可以访问返回值,但不可修改 
	ThrowsAdvice
	 
	
	return invocation.proceed();
 
ThrowsAdvice 这个接口没有方法,但必须实现的方法,[可选的]
	public void afterThrowing([Method method,String[] args,Object  target,] Throwable throwable) 
	返回后调用,只能仿问异常,不能阻止异常

PointCut 接口
	ClassFilter getClassFilter() //对哪些类      
	MethodMatcher getMethodMatcher() //对哪些方法     

ClassFilter 接口, ClassFilter.TRUE 对所有的类都应用

MethodMatcher接口,MethodMatcher.TRUE 对所有的方法都应用

	boolean matches(Method method, Class targetClass) 如果返回真，调用isRuntime，只被调用一次
	boolean isRuntime()  如果返回真，动态的，调用三参的matches
	 		如果返回徦，静态的，永远不会调用三参的matches，通知总是被执行，只被调用一次
	boolean matches(Method method, Class targetClass, Object[] args) 
 			isRuntime()  如果返回真，在每次的方法调用，都会调用此方法，会根据方法参数判断

 			如果是真，调用通知

大多数是静态的


Advisor (Spring独有的,就是Aspect) 是ADVICE和 POINTCUT
PointcutAdvisor  extends Advisor
	一般的一个Advisor一个只包含一个Pointcut和一个Advice

abstract  StaticMethodMatcherPointcut extends StaticMethodMatcher(两个方法是final的)
实现boolean matches(Method method, Class targetClass)MethodMatcher中的


NameMatchMethodPointcut extends StaticMethodMatcherPointcut 只是方法不是对类名(可以使用*)
 void setMappedName(String mappedName)  哪些方法名应用Advice,setAdvice(Advice)
 void setMappedNames(String[] mappedNames) 
 
 NameMatchMethodPointcutAdvisor    Advice,setAdvice(Advice)
jdk1.4 以后用 org.springframework.aop.support.JdkRegexpMethodPointcut
RegexpMethodPointcutAdvisor

org.springframework.aop.support.DefaultPointcutAdvisor

 接口 org.aopalliance.intercept.MethodInterceptor extends org.aopalliance.intercept.Interceptor
 接口 org.aopalliance.intercept.Interceptor extends org.aopalliance.aop.Advice {}

NameMatchMethodPointcut()的 addMethodName(String name)和 setMappedName(String mappedName) 

ProxyFactoryBean的addAdvisor(advisor),addAdvice(Advice)，setInterceptorNames
																
	DefaultTransactionDefinition定义一些常量，如	PROPAGATION_REQUIRED									
TransactionProxyFactoryBean
1.target
2.transactionManager   DataSourceTransactionManager (dataSource属性) PlatformTransactionManager
3.transactionAttributes  <props>
							<prop key="insert*">PROPAGATION_REQUIRED</prop>
							<prop key="get*">PROPAGATION_REQUIRED,readOnly</prop>
						</props>

----引入
接口 IntroductionInterceptor extends org.aopalliance.MehtodInterceptor,DynamicIntroductionAdvice 是类级别，不是方法级别，不能和任何方法使用

被引入的对象要实现 IntroductionInterceptor 和要引入的接口
Invocation.proceed()是拦截器链继续执行，invocation.getMethod().invoke(this,invocation.getArguments())是调用这个方法

类 DefaultIntroductionAdvisor 实现了 IntroductionAdvisor, 
DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class clazz)//第一个参数传,要引入的拦截器继承自 DelegatingIntroductionInterceptor,可实现多个要引入的接口
//如引入多个接口,不要传第二个参数
继承自 DelegatingIntroductionInterceptor 实现多个要引入的接口,重写invoke方法
delegate 委派 就不需要实现方法boolean implementsInterface(Class intf)  {return 要引入的接口.class.isAssignableFrom(clazz)}
public Object invoke(MethodInvocation invocation)
{
	return supper.invoke(invocation)//会自动判断调用的是引入的方法(invocation.getMethod().invoke(this,invocation.getArguments()))还是自己的方法(invocaiton.proceed())
}


org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator (是一个BeanPostProcessor)
			setBeanNames(String[] beanNames) 必须使用ApplicationContext
			setInterceptorNames  //是Bean的名字,类实现 MethodInterceptor
  如在setBeanNames里有我要的Bean,getBean("")或者对指定的beanName在拦截,它会帮你生成代理并应用intercepter
<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator"
	p:beanNames="*Service" p:interceptorNames="myMethodInterceptor" p:optimize="false" />
<bean id="myMethodInterceptor" class="spring_aop_tag.MyMethodInterceptorImpl" /> <!-- 实现implements MethodInterceptor  -->
	
  
DefaultAdvisorAutoProxyCreator,自动代理,只能用于 Advisor 自动判断你调用bean的方法,对应的接口,在所有的advisor有没有,必须使用ApplicationContext


DataAccessException 是RuntimeException的子类


org.springframework.jndi.JndiObjectFactoryBean JNDI得到数据源setJndiName(String jndiName)

MathContxt  BigDecimal有一个round(MathContext mc) 

MathContext mc=new MathContext(5) HALF_UP 
BigDecimal bd=new BibDecimal(12.43);
db.round(mc).floatValue();

------------------JDBC 支持
对于 extends JdbcDaoSupport,要注入DataSource,就可以使用this.getJdbcTemplate()

//把文件拖入cmd中全路径
JdbcTemplate
 execute(PreparedStatementCreator psc, PreparedStatementCallback action)会执行Creator创建PreparedStatement,再执行PreparedStatement返回一个Ojbect
 update(String sql, PreparedStatementSetter pss)
  update(String sql, Object[] args)           
 
 jdbcTemplate.setMaxRows(getPageSize());
 
 实现接口SqlProvider  只有一个方法String getSql()  会在抛出异常时显示执行的SQL脚本,Prepared是有??
 
JdbcTemplate 的
  int[] batchUpdate(String[] sql) 
 	int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) 
  
BatchPreparedStatementSetter的 setValues(PreparedStatement ps, int i) 
		i 是当前执行的第几个SQL ,实现用switch(i) case 0:..... 判断  0开始，小于i;
	
Object query(String sql, Object[] args, ResultSetExtractor rse) 
			Object extractData(ResultSet rs)  要先rs.next();  可返回一个对象，也可以多条记录放list
     
void query(String sql, Object[] args, RowCallbackHandler rch) 
		RowCallbackHandler的一个方法  void processRow(ResultSet rs)  rs不用next(),要在方法外部声明list


List query(String sql, Object[] args, RowMapper rowMapper)  
					RowMapper 中用存储过程输出参数
					Object mapRow(ResultSet rs, int rowNumber)  rs不用next(),使用rs.getXX 生成对象并返回,spring自动放入List
					

类 RowMapperResultSetExtractor(chm没有的) 实现了ResultSetExtractor 
 RowMapperResultSetExtractor(RowMapper rowMapper, int rowsExpected) rowsExpected预期返回行数，优化用的
 			Object extractData(ResultSet rs)  返回是一个List原代码中
 
 org.springframework.dao.support.DataAccessUtils
 			静态方法	Object uniqueResult(Collection results)  维一的结果在Collections中，多了会抛异常
 				
 JdbcTemmplate的 queryForXxxxx()
 CallableStatementCallback 存储过程
 
 org.springframework.jdbc.object.SqlUpdate()--对象包
 	 	 	
	update(Object[] params) 
		SqlUpdate前要先(所有的object 	包的都要做这些步骤，可没有参数声明，参数值)
 				1.setDataSource(...) ,
 				2.setSql(..),
 				3.declareParameter(new SqlParameter(Types.INTEGER) )//声明参数类型，可多次为多个参数
														SqlParameter(int type 如 java.sql.Types.INTEGER)
				4.compile()
				5.update(...)//传参数值
				
SqlQuery 重写 protected abstract  RowMapper newRowMapper(Object[] parameters, Map context)   很少使用
直接用抽象类MappingSqlQuery的抽象方法mapRow(ResultSet rs, int rowNum) 
	execute()返回List
	
SqlInOutParamter,SqlOutParameter,SqlParameter[是两个的父类]

继承自 StoredProcedure  ,setFunction(true),declareParameter(SqlParameter param) 
如对 Oracle 的sysdate  
1.要先继承自StoreProcedure
2.setDataSource()
3.setFunction(true);
4.setSql("");
5.declareparameter(new SqlOutparameter("map 中的key" ,Types.DATA));
6.compile();
7.Map ou=execute(new HashMap());由于sysdate没有输入参数为空的Hashmap ,返回的Map中的key 

SqlFunction 类只是执行简单的函数，queryForXXX(JdbcTemplate)

OracleSequenceMaxValueIncrementer
MySQLMaxValueIncrementer(DataSource ds, String incrementerName, String columnName) incrementerName表名
			nextIntValue,nextFloatValue,nextStringValue
要单独建立一张表(上方配置为该表，向信息表加数据时，nextInt，前提是两表开始时数据的主键要对应)，一个字段表记录信息表的id 

SqlUpdate的 int update(Object[] args, KeyHolder generatedKeyHolder) 
		Mysql 的语法，SELECT last_insert_id ();
		java.sql.Statement 的方法 ResultSet getGeneratedKeys() //JDBC 驱动实现的方法
		
		SqlUpdate的方法int update(Object[] params, KeyHolder generatedKeyHolder)  	 更新后把主键值传给KeyHolder
		
		KeyHolder 接口的实现类GeneratedKeyHolder          getKey().longValue() 来获取最后一个主键

<jdbc:embedded-database type="H2">
	<jdbc:script location="classpath:test.sql"/>
</jdbc:embedded-database>

<jdbc:initialize-database data-source="dataSource">
	<jdbc:script location="classpath:test.sql"/>
</jdbc:initialize-database>

--------------
LocalSessionFactoryBean configLocation(Resource )	用classpath: file: 配置

setMappingResources(String[]是映射文件路径
setMappingLocations(Resource[] mappingLocations)  spring配置用classpath:/com/User.hbm.xml
setMappingDirectoryLocations(Resource[]  在指定文件夹下的文件自动加载（hbm.xml）


HibernateTemplate , setSessionFactory(SessionFactory)或用构造器注入，就可以得到一个HibernateTemplate
HibernateCallback 中的方法时不用对Session进行事物 操作	，关闭操作


继承自HiberanteDaoSupport ,可以getSession(boolean );不存在Session创建吗？

SessionFactoryUtils.getSession(Spring中的)
Session getSession(SessionFactory sessionFactory, boolean allowCreate) 

(Hibernate的)Session getCurrentSession()  ;
-------
PlatFormTransactionManger  -->DataSourceTransactionManager,HibernateTransactionManger,JtaTransactionManager, 
	事务的边界是方法
TransactionDefinition
	1.PROPAGATION_MANDATORY 必须有事务,如没有在事务中报错
	2.PROPAGATION_NESTED 如当前事务存在，再新产生一个事务,否则建立事务 和PROPAGATION_REQUIRES_NEW  一样,使用savepoint实现
	3.PROPAGATION_NEVER 不能在事务中执行，否则报错
	4.PROPAGATION_REQUIRED 如当前已经在事务中，加入这个事务,否则建立事务
	5.PROPAGATION_NOT_SUPPORTED 如当前已经在事务中，事务会被挂起
	6.PROPAGATION_SUPPORTS  如当前已经在事务中，加入这个事务,否则在没有事务中执行

TransactionDefinition 的下些常量 的子接口 TransactionAttribute
可取值同 JDBC,常量的值也相同,默认取决于数据库
	ISOLATION_READ_COMMITTED
	ISOLATION_SERIALIZABLE  同 Connection.TRANSACTION_SERIALIZABLE
	
TransactionProxyFactoryBean
	setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) 
	setTransactionManager(DataSourceTransactionManager)
	
	接口 TransactionAttributeSource 的实现类 MatchAlwaysTransactionAttributeSource(对当前的类的所有的方法应用相同的事务) 的设置是
	PROPAGATION_REQUIRED ,ISOLATION_DEFAULT ,readOnly。如有多个，则第一个方法的设置常量顺序要全部一样
																								setTransactionAttribute()可以改变默认设置		
		
	ProxyTransactionManager的setProxyTargetClass(boolean)true表示代理的是一个目标类而不是一个接口,(target属性指定)
				setProxyInterfaces(String[] interfaceNames) 
			 
DefaultTransactionAttribute 可以修改  MatchAlwaysTransactionAttributeSource 的默认事物属性
	setIsolationLevelName,  
	setPropagationBehaviorName


NameMatchTransactionAttributeSource   方法setProperties(Properties 中的key是方法名，value是对应的设置（如PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly）
可在TransactionProxyFactoryBean 中用transactionAttributes属性
TransactionIntercepter 是MethodIntercepter (aopalliance) 的实现类
			1.PlatformTransactionManager 
			2.TransactionAttributeSource 或是一个 Properties

TransactionAttributeSourceAdvisor 实现了 Ordered
			setTransactionInterceptor(TransactionInterceptor )，可能有空指针，用构造注入


DefaultAdvisorAutoProxyCreator 自动代理  要用ApplicationContext  才可用

DataSourceUtils
SessionFactoryUtils


PlatformTransactionManager
	TransactionStatus setRollbackOnly() ,isCompleted() ,isNewTransaction()  


MethodMapTransactionAttributeSource 可以对包.类.方法 应用事务属性
		setMehtodMap（Map）key是包.类.方法 
		
		spring 对会抛出RuntimeException（uncheck）自动进行rollback
		如是非RuntimeException ,则不会自动rollback;要在setMethodMap(Map)中的Map的value 中的增加（，-异常类型）如（,-MyCheckException）
				-表示如有异常回滚
				+表示如有异常不要回滚
		1.PROPAGATION,1.ISOLATION,readOnly,-MyException

<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource" />
</bean>

<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
	<property name="transactionManager" ref="transactionManager" />
	<property name="propagationBehaviorName" value="PROPAGATION_REQUIRED"></property>
	<property name="isolationLevelName" value="ISOLATION_SERIALIZABLE"></property>  ISOLATION_DEFAULT
</bean>

TransactionTemplate(PlatformTransactionManager transactionManager)
TransactionTemplate(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) 老的文档没有的
transactionTemplate.execute(new TransactionCallback ()  //或者使用 TransactionCallbackWithoutResult
								{
									Object doInTransaction(TransactionStatus status)  
									{
										status.createSavePoint();//如有错误会回滚事务,最外execute要try catch
										
										status.setRollbackOnly();//就可以回滚事务
									}  
								});
								

事务自动提交，回滚（也是RuntimeException 和Error）sunxin是非RuntimeException也可回滚

 ----事务 标签方式
  <bean id="fooService" class="x.y.service.DefaultFooService"/>
  <tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
      <tx:method name="get*" read-only="true"/>
	  <tx:method name="update*" propagation="REQUIRED" isolation="REPEATABLE_READ"/>
			<!-- 
			READ_UNCOMMITTED
			READ_COMMITTED
			REPEATABLE_READ
			SERIALIZABLE
			 -->
			 
      <tx:method name="*"/>
    </tx:attributes>
  </tx:advice>

  <aop:config>
    <aop:pointcut id="fooServiceOperation" expression="execution(* x.y.service.FooService.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="fooServiceOperation"/>
  </aop:config>
  
第一个*( 可以!void)表示返回类型
execution(* x.y.service.*Service.*(..))表示在x.y.service.包中的所有以Service结尾的类的所有方法
execution(* x.y.service.ddl.DefaultDdlManager.*(..))

<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"/>
</bean>
<tx:advice id="txAdvice" transaction-manager="txManager">
  <tx:attributes>
	 <tx:method name="get*" read-only="false" rollback-for="NoProductInStockException"/>自定入异常非RuntimeException  read-only="false"
	 <tx:method name="*"/> propagation="NEVER"
  </tx:attributes>
</tx:advice>
 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
 
 ----事务 Annotation方式

 @org.springframework.transaction.annotation.Transactional
public class DefaultFooService implements FooService 

<bean id="fooService" class="x.y.service.DefaultFooService"/>

<tx:annotation-driven transaction-manager="txManager"/> 就可以使用@Transactional
如已有的id 是 transactionManager 则不用配置transaction-manager,默认值
@Transactional 注解可以被应用于接口定义和接口方法、类定义和类的 public 方法上

如在接口上使用 @Transactional 注解，设置了基于接口的代理时它才生效 , 不推荐
<tx:annotation-driven proxy-target-class="false"/>  proxy-target-class默认值false,JDK基于接口的代理 , 如true使用cglib代理


//isolation默认依赖于数据库, propagation默认是Propagation.REQUIRED.
@Transactional(propagation = Propagation.REQUIRES_NEW ,isolation=Isolation.READ_COMMITTED,timeout=10,
			rollbackFor=IOException.class,rollbackForClassName="FileNotFoundException",noRollbackFor=IOException.class)
	 //timeout单位(秒)
 
checked异常默认不会回滚
unchecked(RuntimeException)异常默认会事务回滚

@Transactional(rollbackFor=Exception.class) //要符加,才会回滚,也可加 noRollbackFor=RuntimeException.class

@Transactional(propagation=Propagation.NOT_SUPPORTED)//不需要事务


对标有 	  @Transactional 的方法，调用这个方法所在类不能是本类调用，否而事务不起作用  

-----
import org.aspectj.lang.ProceedingJoinPoint; ProceedingJoinPoint exposes the proceed(..) method in order to support around advice in @AJ aspects 
1. toShortString());Returns an abbreviated string representation of the join point. 
2. proceed();Proceed with the next advice or target method invocation .
import org.springframework.util.StopWatch;
import org.springframework.core.Ordered;



  <context:load-time-weaver/>
  
-----

<context:component-scan base-package="spring3.config"/> <!-- 不用配置 bean标签 -->

@Configuration  //代替部分spring配置中XML,<bean id=""
@Import({ServiceConfig.class})//中的类也有@Configuration  
@PropertySource("classpath:jdbc.properties")// 写到Environment中, /WEB-INF
public class AppConfig 
{
	@Inject 
	Environment env;
	
	@Bean //<bean id=""
	public UserDAO userDAO() {
		env.getProperty("jdbc.username")
	}

	private @Value("#{jdbcProperties.url}") String jdbcUrl;
	
    @Value("#{now.time}") String timestamp;//beanId.property
	
	@Bean 
	public Date now() {
		return new Date();
	}
	
	@Value("#{systemProperties['os.name']}") String home;
	
	private @Value("#{jdbcProperties.password}") String password;//<util:properties id="jdbcProperties"
	 
	public void setDatabaseName(String dbName){}
	//private @Value("${jdbc.username}") String jdbcUser; // <context:property-placeholder 中的值
}

ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService service1= context.getBean(UserService.class);






---------其它标签

<idref  local="beanHome" />   <!--idref 只是传字串,仿止输错,   -->
<ref parent="beanHome"  /> <!-- parent 是在parent的BeanFactory , local表示只在当前xml文件中,bean表示可以在其它xml文件中 -->
<property name="user.age" value="28"></property> <!-- 支持多级．的配置方式　 -->

//父子BeanFactory
DefaultListableBeanFactory   parent = new DefaultListableBeanFactory(); 
new XmlBeanDefinitionReader(parent).loadBeanDefinitions(new ClassPathResource("springtest/parent.xml")); 
  
DefaultListableBeanFactory   child = new DefaultListableBeanFactory(parent); 
new XmlBeanDefinitionReader(child).loadBeanDefinitions(new ClassPathResource("springtest/child.xml")); 

<!-- lookup-method -->
<bean id="anotherBean" class="springtest.AnotherBean" scope="prototype" />
<bean id="myLookup" class="springtest.MyLookupBean">
	<lookup-method name="newAnotherBean" bean="anotherBean"/>
</bean>
public class MyLookupBean 
{
	//spring会重写这个方法,<lookup-method 
	public  AnotherBean newAnotherBean()//protected,public 都可
	{
		return null;
	}
	public void doWithAnotherBean()
	{
		AnotherBean another=newAnotherBean();//调用spring的方法
		System.out.println(another.hashCode());
	}
}

<!-- replace-method -->
<bean id="myValueCalculaterReplacer" class="springtest.MyValueCalculatorReplacer" scope="prototype" />
<bean id="myValueCalculater" class="springtest.MyValueCalculator">
	<replaced-method name="computeValue" replacer="myValueCalculaterReplacer"><!-- name 的值是方法名 -->
		<arg-type>java.lang.String</arg-type> <!-- 方法签名,可是String,java.lang.String -->
	</replaced-method>
</bean>
public class MyValueCalculatorReplacer implements MethodReplacer{
	public Object reimplement(Object obj, Method method, Object[] args)	throws Throwable {
		String str=(String)args[0];
		return str+"789";//这里做替换
	}
}

<!-- PropertyPathFactoryBean -->
<bean id="readAge" class="org.springframework.beans.factory.config.PropertyPathFactoryBean">
	<property name="targetBeanName" > 
		<idref bean="beanParent"/><!-- 只是传字串  -->
	</property>
	<property name="propertyPath" value="user.age"/>
</bean>
<bean id="readAge2" class="org.springframework.beans.factory.config.PropertyPathFactoryBean">
	<property name="targetObject" >
		<ref parent="beanParent"/>
	</property>
	<property name="propertyPath" value="user.age"/>
</bean>
<bean id="beanParent.user.age" class="org.springframework.beans.factory.config.PropertyPathFactoryBean"/>

<!-- FieldRetrievingFactoryBean -->
<bean id="readFeild" class="org.springframework.beans.factory.config.FieldRetrievingFactoryBean">
	<property name="staticField" value="java.sql.Connection.TRANSACTION_SERIALIZABLE"></property>
</bean>
<bean id="java.sql.Connection.TRANSACTION_SERIALIZABLE" class="org.springframework.beans.factory.config.FieldRetrievingFactoryBean" />

<!-- MethodInvokingFactoryBean -->
<bean id="calender" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean" >
	<property name="staticMethod" value="java.util.Calendar.getInstance"/>
</bean>
<bean id="props" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean" >
	<property name="targetClass" value="java.lang.System"/>
	<property name="targetMethod" value="getProperties"></property>
</bean>
<bean id="javaVersion" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean" >
	<property name="targetObject" ref="props"/>
	<property name="targetMethod" value="getProperty"/>
	<property name="arguments" >
		<list>
			<value>java.version</value>
		</list>
	</property>
</bean>

=========================Spring JDBC
<bean id="nativeJdbcExtractor"    class="org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor" />
<bean id="oracleLobHandler"  class="org.springframework.jdbc.support.lob.OracleLobHandler" lazy-init="true">
	<property name="nativeJdbcExtractor">
		<ref bean="nativeJdbcExtractor" />
	</property>
</bean>
 
<bean id="sessionFactory"  class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
	<property name="lobHandler" ref="oracleLobHandler" />

---------也可以用

 <bean id="lobHandler" class="org.springframework.jdbc.support.lob.DefaultLobHandler">  
     <bean id="sessionFactory" class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">  
 <property name="lobHandler" ref="lobHandler">  
     </property>  
 </bean>


=========================Spring Hibernate 
<!-- 要配置在struts2的Filter之前 -->
<filter>  
	<filter-name>OpenSessionInViewFilter</filter-name>   
	<filter-class>org.springframework.orm.hibernate4.support.OpenSessionInViewFilter</filter-class>  
	<!-- singleSession默认为true,若设为false则等于没用OpenSessionInView -->  
	<init-param>  
		<param-name>singleSession</param-name>  
		<param-value>true</param-value>  
	</init-param>  
</filter>  
<filter-mapping>
    <filter-name>OpenSessionInViewFilter</filter-name>
    <url-pattern>/*</url-pattern>  */
</filter-mapping>

 <!--在服务器运行过程中，Spring不停的运行的计划任务和OpenSessionInViewFilter，
	使得Tomcat反复加载对象而产生框架并用时可能产生的内存泄漏，
	则使用 IntrospectorCleanupListener 作为相应的解决办法  -->
<listener>
	<listener-class>org.springframework.web.util.IntrospectorCleanupListener</listener-class>
</listener>

import org.springframework.orm.hibernate3.support.BlobByteArrayType;
public class ImmutableBlobByteArrayType extends BlobByteArrayType {

	public Object deepCopy(Object value) 
	{
			return value;
	}
	public boolean isMutable() 
	{
		return false;
	}
}
作为 	Hibernate 类型为Blob
<property name="pkgBin"	column="pkg_bin" type="com.ci.domain.types.ImmutableBlobByteArrayType"/>	

private byte[] pkgBin;


hibernate.jdbc.use_streams_for_binary 设为 true，以确保自动开启流功能

----Spring配置Hibernate4
DAO注入sessionFactory,再用getSessionFactory().getCurrentSession();

<bean id="employeeDao" class="spring_db_hibernate.EmployeeDao">
	<property name="sessionFactory" ref="mySessionFactory"/>
</bean>
public class EmployeeDao 
{
	private SessionFactory sessionFactory;//org.hibernate.SessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	this.getSessionFactory().getCurrentSession().save(employee);
}		
<bean id="transactionManager" class="org.springframework.orm.hibernate4.HibernateTransactionManager">
	<property name="sessionFactory" ref="mySessionFactory"/>
</bean>

<bean id="mySessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
	<property name="dataSource" ref="dataSource" />
	<property name="mappingLocations">
		<list>
			<value>classpath:/spring_db_hibernate/*.hbm.xml</value>
		</list>
	</property>
	<property name="hibernateProperties">
		<props>
			<prop key="hibernate.show_sql">true</prop>
			<prop key="hibernate.hbm2ddl.auto">update</prop>
			<prop key="hibernate.dialect">${hibernate.dialect}</prop>
		</props>
		<!-- <value>hibernate.dialect=${hibernate.dialect}</value>
		 -->
	</property>
</bean>

=========================Spring JPA
	<!--  只可是 META-INF/persistence.xml 
	<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalEntityManagerFactoryBean">
		 <property name="persistenceUnitName" value="MyJPA" ></property>
		 <property name="persistenceProvider" >
			<bean class="org.hibernate.ejb.HibernatePersistence"></bean>
		 </property>
	</bean>
	 --> 
	 
 <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"> 
	<property name="persistenceXmlLocation"  value="classpath:spring_db_jpa/META-INF/persistence.xml"></property>
	 <!--
	<property name="dataSource" ref="dataSource"></property>
	 <property name="packagesToScan" value="spring_db_jpa"></property> 
	  <property name="persistenceUnitName" value="MyJPA" ></property>
	 <property name="persistenceProvider" >
		<bean class="org.hibernate.ejb.HibernatePersistence"></bean>
	 </property>
	  -->
	<property name="loadTimeWeaver" >
		<bean class="org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver"></bean>
	</property>
</bean>

<bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />
<context:component-scan base-package="spring_db_jpa"/>

//可在Dao类中加
@PersistenceContext
public EntityManager em; //JPA

在视图层有效,防延迟加载
<!-- 要配置在struts2的Filter之前 -->
<filter>  
	<filter-name>OpenEntityManager</filter-name>  
	<filter-class>org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter</filter-class>  
</filter>
<filter-mapping>
    <filter-name>OpenEntityManager</filter-name>
    <url-pattern>/*</url-pattern>  */
</filter-mapping>

=========================spring C3p0 
	<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="location" value="jdbc.properties"></property>
	</bean>
	
	<!-- context:标签方式 -->
	<!-- <context:property-placeholder location="jdbc.properties"/>  -->
		
	<bean id="poolConfig" class="com.mchange.v2.c3p0.PoolConfig">
        <property name="acquireRetryAttempts">		<value>10</value></property>
        <property name="acquireRetryDelay">			<value>1000</value></property>
        <property name="automaticTestTable">		<value>C3P0_TEST_TABLE</value></property>
        <property name="checkoutTimeout">			<value>10000</value></property>
        <property name="idleConnectionTestPeriod">	<value>10</value></property>
        <property name="maxPoolSize">				<value>20</value></property>
        <property name="maxStatements">				<value>200</value></property>
        <property name="maxStatementsPerConnection"><value>20</value></property>
    </bean>

    <!-- Register the database driver -->
    <bean id="driverClass" class="java.lang.Class" factory-method="forName">
        <constructor-arg><value>${jdbc.driverClass}</value></constructor-arg>
    </bean>
    <bean id="driverInstance" class="java.lang.Class" factory-method="newInstance" factory-bean="driverClass" depends-on="driverClass" />

    <!-- Create the c3p0 unpooled ds -->
    <bean id="unpooledDataSource" class="com.mchange.v2.c3p0.DataSources" factory-method="unpooledDataSource" depends-on="driverInstance" >
        <constructor-arg index="0"><value>${jdbc.url}</value></constructor-arg>
        <constructor-arg index="1"><value>${jdbc.username}</value></constructor-arg>
        <constructor-arg index="2"><value>${jdbc.password}</value></constructor-arg>
    </bean>

    <!-- Create the pooled data source to actually use -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.DataSources" factory-method="pooledDataSource" depends-on="unpooledDataSource">
        <constructor-arg><ref bean="unpooledDataSource"/></constructor-arg> <!-- ref,idref 新版本不能用local-->
        <constructor-arg><ref bean="poolConfig"/></constructor-arg>
    </bean>

----示例c3p0配置
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
		<property name="driverClass" value="${jdbc.driver}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
                <!-- 初始化连接池时连接数量为5个 -->
		<property name="initialPoolSize" value="5" />
                <!-- 允许最小连接数量为5个 -->
		<property name="minPoolSize" value="5" />
                <!-- 允许最大连接数量为20个 -->
		<property name="maxPoolSize" value="20" />
                <!-- 允许连接池最大生成100个PreparedStatement对象 -->
		<property name="maxStatements" value="100" />
                <!-- 连接有效时间，连接超过3600秒未使用，则该连接丢弃 -->
		<property name="maxIdleTime" value="3600" />
                <!-- 连接用完时，一次产生的新连接步进值为2 -->
		<property name="acquireIncrement" value="2" />
                <!-- 获取连接失败后再尝试10次，再失败则返回DAOException异常 -->
		<property name="acquireRetryAttempts" value="10" />
                <!-- 获取下一次连接时最短间隔600毫秒，有助于提高性能 -->
		<property name="acquireRetryDelay" value="600" />
                <!-- 检查连接的有效性，此处小弟不是很懂什么意思 -->
		<property name="testConnectionOnCheckin" value="true" />
                <!-- 每个1200秒检查连接对象状态 -->
		<property name="idleConnectionTestPeriod" value="1200" />
                <!-- 获取新连接的超时时间为10000毫秒 -->
		<property name="checkoutTimeout" value="10000" />
	</bean>

	<dependency>
		<groupId>com.mchange</groupId>
		<artifactId>c3p0</artifactId>
		 <version>0.9.5.2</version> 
	</dependency> 
	
	ComboPooledDataSource dataSource= new  ComboPooledDataSource();
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

<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource"> 测试用,产品时用第三方的
	<property name="driverClassName" ><value>${jdbc.driverClass}</value></property>
	<property name="url" value="${jdbc.url}"></property>
	<property name="username" value="${jdbc.username}"></property>
	<property name="password" value="${jdbc.password}"></property>
</bean>

<!-- 用于连接多个DB -->
<bean id="dynamicDataSource" class="spring_db_jdbc.DynamicDataSource">
	<property name="targetDataSources">
		<map key-type="java.lang.String">
			<entry value-ref="testDataSource" key="testDataSource" />
			<entry value-ref="dataSource" key="dataSource" />
		</map>
	</property>
	<property name="defaultTargetDataSource" ref="testDataSource" />
</bean>

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return "testDataSource";   //这是可以动态选择JDBC数据库的
        //return "dataSource"; 
    }
}
=========================  Druid 见 MyBatis
 

=========================spring 单元测试
 AbstractTransactionalDataSourceSpringContextTests
 
 
@RunWith(SpringJUnit4ClassRunner.class)

//@TestExecutionListeners({CustomTestExecutionListener.class}) //implements TestExecutionListener,加了就不能自动注入了
//@ContextConfiguration(classes = {AppConfig.class, TestConfig.class})
//@ContextConfiguration("classpath:spring_junit/springJunitEasyMock.xml") //可注入 ApplicationContext;
@ContextConfiguration
@ActiveProfiles("dev") //对应@Profile("dev")
public class TestSpringJunit {
	@Autowired
	private ApplicationContext ctx; //是GenericApplicationContext
	
	@Resource(name="userDao") //在xml中找id="userDao",并赋下面的属性
	private UserDAO userDAO;
	
	@BeforeClass
	public static void init()//必须是static
	{
		System.getProperties().put("test-groups", "unit-tests");
	}
	@AfterClass
	public static void destory()
	{
	}
	  @Profile("dev")//同<beans profile="dev"
	  @Configuration //指定这个 @ContextConfiguration可以不指定配置
	  static class Config 
	  {
	        @Bean(name="userDao")
	        public UserDAO newUserDAO() {
	        	UserDAO userDAO = new UserDAOImpl();
	            return userDAO;
	        }
	 }
	  
	@Test
	@Timed(millis=3000)
	@Repeat(1)
	@IfProfileValue(name="test-groups", values={"unit-tests", "integration-tests"})
	public void testSaveUseContext() {
		UserDAO userDao=(UserDAO)ctx.getBean("userDao");
		userDao.save("李");
	}
	
	@Test
	public void testSave() {
		this.userDAO.save(new User());
	}
	
 	@Test
	public void testMockDynamicChange() 
	{
		String beanName="userDao";
		DefaultListableBeanFactory  factory=(DefaultListableBeanFactory)ctx.getBeanFactory();//是DefaultListableBeanFactory
		 
		 UserDAOImpl newObj=new UserDAOImpl();
		 newObj.changeTest("newOjb");
		 
//		 UserDAO newObj=UserDAOMock.generateMockObject("li");
		 
		 factory.removeBeanDefinition(beanName);
		 factory.registerSingleton(beanName, newObj); //不会修改已经注入的
		 
		 UserDAOImpl newBean=(UserDAOImpl)ctx.getBean(beanName);
		 Assert.assertSame("newOjb", newBean.getTest());//OK
		 
		 Assert.assertSame("newOjb", userDAO.getTest());//Fail,还是原来的Mock对象,如何修改它????
		
	}
}
 
<beans profile="dev">
    <context:property-placeholder
		location="classpath*:common/*.properties, classpath*:dev/*.properties" />
	<import resource="classpath*:common/applicationContext-*.xml"/>
	<import resource="classpath*:dev/applicationContext-*.xml"/>
</beans>

web.xml中
  <context-param>
    <param-name>spring.profiles.default</param-name>
    <param-value>dev</param-value>
  </context-param>

  
--web 不行的??

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  //可以注入 WebApplicationContext 要和  @ContextConfiguration 一起使用
@ContextConfiguration 
public class TestSpringJunitWithWeb 
{
	@Autowired
	private WebApplicationContext wac;
	// class body...
}
 
org.springframework.util.Assert.notNull(obj,"error,obj is null");
	isTrue()
=========================mockMVC 不行的??

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public MockMvc mockMvc;
mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	
ResultActions resultActions = mockMvc.perform(
				post("/employee/list/1.mvc")
						.characterEncoding("UTF-8")
						.contentType(MediaType.APPLICATION_JSON)
						//.content(json)
						)
				.andExpect(status().isOk())
				.andDo(print());

MvcResult mvcResult = resultActions.andReturn();

String result = mvcResult.getResponse().getContentAsString();


=========================spring JNDI
JMS见ActiveMQ
<bean id="jndiTemplate" class="org.springframework.jndi.JndiTemplate">
	<property name="environment">
		<props>
		   <prop key ="java.naming.factory.initial">weblogic.jndi.WLInitialContextFactory</prop>
		   <prop key ="java.naming.provider.url">t3://localhost:7001</prop>
		</props>
	</property>
</bean>
<bean id="jmsTopicConnectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean">
	<property name="jndiTemplate">
		<ref bean="jndiTemplate" />
	</property>
	<property name="jndiName">
		<value>TopicConnectionFactory</value>
	</property>
</bean>
<bean id="destination" class="org.springframework.jndi.JndiObjectFactoryBean">
	<property name="jndiTemplate">
		<ref bean="jndiTemplate" />
	</property>
	<property name="jndiName">
		<value>jobNotificationTopic</value>
	</property>
</bean>

<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
	<property name="connectionFactory">
		<ref bean="jmsTopicConnectionFactory" />
	</property>
	<property name="defaultDestination">
		<ref bean="destination" /> <!--javax.jms.Destination-->
	</property>
	<property name="receiveTimeout">
		<value>30000</value>
	</property>
</bean>

=========================Spring mail
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

JavaMailSenderImpl sender = new JavaMailSenderImpl();
sender.setHost("mail.host.com");
MimeMessage message = sender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(message, true);
helper.setTo("test@host.com");
helper.setText("Check out this image!");
FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
helper.addAttachment("CoolImage.jpg", file);
sender.send(message);
//--------
SimpleMailMessage mail = new SimpleMailMessage();
mail.setFrom("abcd@163.com");
mail.setTo("abcd@gmail.com");
mail.setSubject(" 测试spring Mail");
mail.setText("hello,java");
mailSender.send(mail);
//--------

Properties prop=new Properties();
prop.put("mail.smtp.auth", "true");
mailSender.setJavaMailProperties(prop);


MimeMessagePreparator preparator = new MimeMessagePreparator()
			{
				public void prepare(MimeMessage mimeMessage) throws Exception
				{	
					mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("aa@sina.com"));
					mimeMessage.setFrom(new InternetAddress("mail@mycompany.com"));
					mimeMessage.setText("hello");
				}
			};
mailSender.send(preparator);



=========================Spring Timer
 
<bean  id="myTimerTask" class="spring.quartz.MyTimerTask"/> <!-- extends TimerTask  -->
<bean  id="myTask" class="spring.quartz.MyTask"/>
<bean  id="myTimerTaskProxy" class="org.springframework.scheduling.timer.MethodInvokingTimerTaskFactoryBean"><!-- 就不用实现Job接口了 -->
	<property name="arguments" value="pdf"/>
	<property name="targetObject" ref="myTask"/>
	<property name="targetMethod" value="doSomething"/>
</bean>  
<bean id="mySchedule" class="org.springframework.scheduling.timer.ScheduledTimerTask">
	<property name="delay" value="1000"/>
	<property name="period" value="3000"/>
	<property name="fixedRate" value="true"/>
	<!--<property name="timerTask" ref="myTimerTask"/>  -->
	<property name="timerTask" ref="myTimerTaskProxy"/>
</bean>
<bean   class="org.springframework.scheduling.timer.TimerFactoryBean">
	<property name="scheduledTimerTasks">
		<list>
			<ref local="mySchedule"/>
		</list>
	</property>
</bean> 


=========================Spring Quartz
<!-- 方式一 -->
<bean id="mytask" class="spring.quartz.MyTask"/>
<bean id="myJobDeatail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
	<property name="targetObject" ref="mytask"/>
	<property name="targetMethod" value="frequencyCheck"/>
	<property name="concurrent" value="false" />  <!-- 单线程,如果第一个调度卡到第二次调度,现在过了第二次时间,第一个调度又继续,第二个调度还可以继续执行 -->
</bean> 
<bean id="mySimpleTrigger" class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean"><!-- SimpleTriggerBean 不可用于Quartz2.0 -->
	<property name="startDelay"  value="3000"/>
	<property name="repeatInterval"  value="1000"/>
	<property name="jobDetail"  ref="myJobDeatail"/>
</bean>
<!-- 方式二 -->
 <bean id="myJob" class="org.springframework.scheduling.quartz.JobDetailFactoryBean"><!-- JobDetailBean 不可用于Quartz2.0 -->
	<property name="jobClass"  value="spring.quartz.MyQuartzJob"/><!-- implements Job -->
	<property name="jobDataAsMap" >
		<map>
			<entry key="data" value="sping配置的数据"/>
		</map>
	</property>
</bean>
<bean id="myCronTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean"><!-- SimpleTriggerBean 不可用于Quartz2.0 -->
	<property name="cronExpression" value="3/5 * 8-17 * * ?"></property>
	<property name="jobDetail"  ref="myJob"/> 
</bean>
<util:properties id="quartzProperties" location="classpath:quartz.properties"/>
<bean id="schedulerFactory"   class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
	 <property name="quartzProperties" ref="quartzProperties"/> <!-- 如使用了Spring不会默认读 classpath下的quartz.properties , 要配置quartzProperties属性 -->
	<property name="triggers">
		<list>
			<!-- <ref local="mySimpleTrigger"/>  -->
			<ref local="myCronTrigger"/>
		</list>
	</property>
	<!-- 持久化,quartz-2.1.6\docs\dbTables\[数据库].sql, tables_mysql_innodb.sql 示例在quartz-2.1.6\examples\example10\quartz.properties -->
	<property name="configLocation" value="classpath:spring/quartz/quartz.properties"/>
</bean> 

Quartz内存泄漏
-----
@Scheduled(fixedDelay=5000)
public void doBusAtRate()

@Scheduled(cron="*/5 * * * * MON-FRI")
public void doBusCrond()

@Async
public void asyncReturnSomething() 
	
<bean id="mySomeBus" class="spring_quartz.other.MySomeBus"/>
<task:scheduler id="myScheduler" pool-size="10" />
<task:executor id="myExecutor" pool-size="10" queue-capacity="25"/> <!-- 会建立  ThreadPoolTaskExecutor实例-->
<task:annotation-driven  executor="myExecutor" scheduler="myScheduler"/> <!-- @ 方式 -->
<!-- 标签方式
<task:scheduled-tasks scheduler="myScheduler">
	<task:scheduled ref="mySomeBus" method="doBusAtRate" fixed-rate="5000" />
	<task:scheduled ref="mySomeBus" method="doBusCrond" cron="*/5 * * * * MON-FRI" />
</task:scheduled-tasks>
 -->
=========================Spring concurrent 包 线程池 
不推荐,过时了
<!--
	<bean id="taskExecutor"
		class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
		<property name="corePoolSize" value="5" />
		<property name="maxPoolSize" value="10" />
		<property name="queueCapacity" value="25" />
	</bean>
 -->	
	<task:executor id="taskExecutor" pool-size="10" queue-capacity="25"/> <!-- 会建立  ThreadPoolTaskExecutor 实例-->
	<bean id="taskExecutorExample" class="spring_quartz.other.TaskExecutorExample">
		<constructor-arg ref="taskExecutor" /> <!-- TaskExecutor taskExecutor;taskExecutor.execute(new Runnable(){..}); -->
	</bean>

=========================Spring JMX
<bean id="mbeanServer" class="org.springframework.jmx.support.MBeanServerFactoryBean"> <!-- 用来建立MBeanServer -->
	<property name="locateExistingServerIfPossible" value="true"/>
</bean> 
<bean id="myBean" class="spring.jmx.MyBean"  lazy-init="true"/> <!--lazy-init 注册的是代理对象,第一次调用才会真的实例化 -->
<bean name="mydomain:myType=Standard" class="spring.jmx.Standard"/> <!-- 标准MBean ,对应于 autodetect -->

<bean class="org.springframework.jmx.export.MBeanExporter">
	<property name="registrationBehaviorName" value="REGISTRATION_FAIL_ON_EXISTING"/> <!--  REGISTRATION_FAIL_ON_EXISTING, REGISTRATION_IGNORE_EXISTING, REGISTRATION_REPLACE_EXISTING -->
	<property name="server" ref="mbeanServer" />
	<!-- 如果加了MetadataMBeanInfoAssembler这里的类也要有@ManagedResource
	<property name="beans">
		<map>
			<entry key="mydomain:myType=MyBean" value-ref="myBean"></entry>    
		</map>
	</property> -->
	<property name="assembler" ref="assembler" />
	<property name="namingStrategy" ref="namingStrategy" />
	<property name="autodetect" value="true" />  <!--要以MBean结尾的接口 ,<bean name="domain:key=val" -->
</bean>
<bean id="namingStrategy" class="org.springframework.jmx.export.naming.MetadataNamingStrategy"> <!-- 读@ManagedResource 中的objectName属性 -->
	<property name="attributeSource" ref="jmxAttributeSource"/>
</bean>
<bean id="jmxAttributeSource" class="org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource"/>
<bean id="assembler" class="org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler"><!-- 带@ManagedResource的类就不用注册到MBeanExporter -->
	<property name="attributeSource" ref="jmxAttributeSource"/>
</bean>
<bean id="testBean" class="spring.jmx.AnnotationTestBean"/>

@ManagedResource(objectName = "bean:name=testBean4", description = "My Managed Bean", log = true, logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "foo", persistName = "bar")
public class AnnotationTestBean//普通Bean
{
	@ManagedAttribute(description = "The Name Attribute", currencyTimeLimit = 20, defaultValue = "bar", persistPolicy = "OnUpdate")
	public void setName(String name) {
		this.name = name;
	}
	@ManagedOperation(description = "Add two numbers")
	@ManagedOperationParameters({
			@ManagedOperationParameter(name = "x", description = "The first number"),
			@ManagedOperationParameter(name = "y", description = "The second number") })
	public int add(int x, int y) {
		return x + y;
	}
}
AnnotationTestBean test=(AnnotationTestBean)context.getBean("testBean");
//mbserver.setAttribute(ObjectName.getInstance("bean:name=testBean4"), new Attribute("Age","23"));//readonly ,不能修改
mbserver.setAttribute(ObjectName.getInstance("bean:name=testBean4"), new Attribute("Name","小王"));
System.out.println("testBean Name:"+test.getName());

//如把程序打成.jar 包ClassPathXmlApplicationContext("spring*.xml")不支持使用通配符*
//如.jar运行机器不能连网,注意META-INF/spring.schemas和spring.handlers文件的合并

ApplicationContext context=new ClassPathXmlApplicationContext("spring/jmx/jmx_server_beans.xml");
MBeanServer mbserver=(MBeanServer)context.getBean("mbeanServer");

MyBean myBean=(MyBean)context.getBean("myBean");
System.out.println("MyBean LogLevel:"+myBean.getLogLevel());
mbserver.setAttribute(ObjectName.getInstance("mydomain:myType=MyBean"), new Attribute("LogLevel","DEBUG"));//是大写的LogLevel
System.out.println("MyBean LogLevel:"+myBean.getLogLevel());

Standard std=(Standard)context.getBean("mydomain:myType=Standard");
System.out.println("Standard MBean PageSize:"+std.getPageSize()); 

=========================Spring Express Language SpEL

ExpressionParser parser=new SpelExpressionParser();
Expression exp=parser.parseExpression("(5+2)*3");
System.out.println(exp.getValue());//21
//-----
User u=new User();
u.setUsername("张三");
EvaluationContext context=new StandardEvaluationContext(u);//根对象
exp=parser.parseExpression("username");//相当于u.getUsername();
System.out.println(exp.getValue(context));
System.out.println(exp.getValue(u));
//-----
 <property name="salary" value="#{T(java.lang.Math).random()*100}"></property>
<property name="username" value="#{systemProperties['os.name']}"></property>


parser.parseExpression("#primes.?[#this>10]")
parser.parseExpression("null?:'Unknown'")
parser.parseExpression("random number is #{T(java.lang.Math).random()}", new TemplateParserContext()).getValue(String.class);
 

=========================Spring Cache
java map


<bean id="simpleCacheManager" class="org.springframework.cache.support.SimpleCacheManager">
<property name="caches"> 
  <set> 
	<bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
	  p:name="default" />  
	 <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
          p:name="accountCache" /> 
  </set> 
</property> 
</bean> 

<cache:annotation-driven cache-manager="simpleCacheManager"/>

<!--  cache无效？？
  <cache:annotation-driven cache-manager="cacheManager"/>
   -->
<bean id="cacheManager" class="org.springframework.cache.support.CompositeCacheManager">
	<property name="cacheManagers">
		<list>
			<bean class="org.springframework.cache.support.NoOpCacheManager" />
			<ref bean="simpleCacheManager" />
		</list>
	</property>
	<property name="fallbackToNoOpCache" value="true" /> <!--  禁用缓存 @Cache*  -->
</bean> 


@Cacheable(value = "accountCache",key="#userName",condition="#userName.length() <= 4") 
public Account getAccountByName(String userName) {
	System.out.println("real query account." + userName);
	 return new Account( userName); 
}
@CacheEvict(value="accountCache",key="#account.getName()")
	 // Key 是SpEL 表达式，这里因为我们保存的时候用的是 account 对象的 name 字段
	 public void updateAccount(Account account) {
		 System.out.println("updateAccount");
} 
@CacheEvict(value = "accountCache", allEntries = true // 清空 accountCache 缓存
			,beforeInvocation=false) //beforeInvocation，缺省为 false 期间如果执行方法出现异常，则会导致缓存清空不被执行 
public void reload() {
	System.out.println("清空 accountCache 缓存");
}

@Cacheable(value="accountCache",key="#userName.concat(#password)") 
public Account getAccount(String userName,String password,boolean sendLog) {  
	System.out.println("getAccount multi param");
	return new Account( userName+password); 
}
//既要保证方法被调用，又希望结果被缓存
@CachePut(value="accountCache",key="#account.getName()") 
public Account updateAccount2(Account account) {  
	 System.out.println("updateAccount @CachePut ");
	 return new Account(account.getName()); 
} 
public Account getAccountByNameInvalid(String userName) {
	System.out.println("getAccountByNameInvalid." + userName);
	 return getAccountByName( userName); //相同的类里，相当于this ,这里不走AOP
}	
自定义 cache
<bean id="cacheManager" class="spring_cache_map.MyCacheManager">
    <property name="caches"> 
      <set> 
        <bean  class="spring_cache_map.MyCache"
          p:name="accountCache" /> 
      </set> 
    </property> 
  </bean> 
  
import org.springframework.cache.support.AbstractCacheManager; 
public class MyCacheManager extends AbstractCacheManager
{
  @Override 
  protected Collection<? extends MyCache> loadCaches() { 
    return this.caches; 
  } 
}

import org.springframework.cache.Cache;
public class MyCache implements Cache 

--cache ehcache
对方法如果每次的参数相同,缓存后,就不执行方法,直接返回缓存的结果
public class ReadOnlyCache {
	@Cacheable(value = "DEFAULT_CACHE" ,key="#param")//对应于ehcache.xm中的配置,#引用参数变量
	public String cacheTest(String param) throws Exception {
		Thread.sleep(1000 * 1);
		return "[" + param + "] processed : " + param;
	}
}
<bean id="myCache" class="spring_cache_ehcache.ReadOnlyCache"></bean>


<bean id="cacheManagerFactory"  class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean" 
	p:configLocation="classpath:/spring_cache_ehcache/ehcache.xml" /> 
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager" 
	p:cacheManager-ref="cacheManagerFactory" /> 

 <!-- @声明式    或用 @EnableCaching 和 @Configuration放在一起   -->
 <cache:annotation-driven cache-manager="cacheManager" />


<!-- xml声明式   DEFAULT_CACHE对应于ehcache.xm中的配置  	
     <cache:advice id="cacheAdvice" cache-manager="cacheManager">
		<cache:caching cache="DEFAULT_CACHE"> 
			<cache:cacheable method="cacheTest" key="#param"/>
		</cache:caching>
	</cache:advice>
	<aop:config>
		<aop:advisor advice-ref="cacheAdvice" pointcut="execution(* spring_cache_ehcache.ReadOnlyCache.*(..))"/>
	</aop:config>
 -->	
 
 
 
ehcache.xml
<ehcache>
	<diskStore path="java.io.tmpdir" />
	<defaultCache 				maxElementsInMemory="500" eternal="false" timeToIdleSeconds="300" timeToLiveSeconds="1200" overflowToDisk="true" />
	<cache name="DEFAULT_CACHE" maxElementsInMemory="5000" eternal="false" timeToIdleSeconds="500" timeToLiveSeconds="500" overflowToDisk="true" />
</ehcache> 

<!-- ehcache配置  2-
ehcache要指定名字cacheManagerName,否则可能mybatis使用ehcahce是默认名字,自己也是默认名字,高版本不可同名的存在两个 
->
    <bean id="cacheManager2" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
		<property name="cacheManagerName" value="myCacheManagerName"></property>  
        <property name="configLocation">
            <value>classpath:/spring_cache_ehcache/ehcache.xml</value>
        </property>
    </bean>
	<bean id="ehcache2" class="org.springframework.cache.ehcache.EhCacheFactoryBean">
        <property name="cacheManager">
            <ref local="cacheManager2"/>
        </property>
        <property name="cacheName">
            <value>DEFAULT_CACHE</value>
        </property>
    </bean>
 //--方式 2
Ehcache ehcache=(Ehcache)context.getBean("ehcache2"); 
ehcache.put(new Element("key", "obj"));
Element element=ehcache.get("key");
Object obj=element.getObjectValue();
String tmpDir=System.getProperty("java.io.tmpdir");// %HOMEPATH%\AppData\Local\Temp\
//		 Set<String> allKeys = new HashSet<String>();
//		 Map<Object, Element> localMap = ehCache.getAll(allKeys);

 
--- 还有 caffeine 和 jcache(ehcache 3.x) (JSR 107 )



=========================Spring OXM
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

is = new FileInputStream(FILE_NAME);
this.settings = (Settings) this.unmarshaller.unmarshal(new StreamSource(is));
			
os = new FileOutputStream(FILE_NAME);
this.marshaller.marshal(settings, new StreamResult(os));

			
<bean id="application" class="spring_oxm.Application">
	<property name="marshaller" ref="marshaller" />
	<property name="unmarshaller" ref="marshaller" />
</bean>
<oxm:jaxb2-marshaller id="marshaller">
	<oxm:class-to-be-bound name="spring_oxm.Settings"/> <!-- 类级加 @XmlRootElement-->
</oxm:jaxb2-marshaller>

=========================Spring Groovy
//除了要	groovy-2.1.8.jar 还要  asm-4.0.jar,检查.groovy文件要存在,不要编译,设置eclipse->project preference->输入src/spring_groovy/*.groovy

要使用单独项目只有依赖包的项目,其它包有影响 

---Messenger.groovy
package spring_groovy;
class GroovyMessenger implements Messenger {//实现java中的Messenger
	String message //没有分号
}

<lang:groovy id="messenger" script-source="classpath:spring_groovy/Messenger.groovy">
	<lang:property name="message" value="I Can Do The Frug" />
</lang:groovy>

--方式二
<lang:groovy id="messenger">
    <lang:inline-script>
        package spring_groovy;
		//import spring_groovy.Messenger;//实现java中的Messenger
		class GroovyMessenger implements Messenger { 
			String message
		}
    </lang:inline-script>
    <lang:property name="message" value="I Can Do The Frug" />
</lang:groovy>
 ---calculator.groovy
 class GroovyCalculator implements Calculator {
    int add(int x, int y) {
        x + y //没有return 
    }
	static main(args) {
	}
}
	

=========================Spring JCA

=========================Spring Reactor
 <dependency>
    <groupId>io.projectreactor.spring</groupId>
    <artifactId>reactor-spring-context</artifactId>
    <version>2.0.7.RELEASE</version>
</dependency>
<dependency>
    <groupId>io.projectreactor.spring</groupId>
    <artifactId>reactor-spring-core</artifactId>
    <version>2.0.7.RELEASE</version>
</dependency>





=========================Spring for Hadoop 2.5.0 (Data)
HbaseTemplate 
=========================Spring Data Redis

<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-redis</artifactId>
	<version>2.0.4.RELEASE</version>
</dependency>  <!-- 要spring 5.0 -->

import org.springframework.data.redis.cache.RedisCacheManager;

@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
	return RedisCacheManager.create(connectionFactory);
}

@Bean
public RedisConnectionFactory redisConnectionFactory( ) 
{
	RedisStandaloneConfiguration standConfig=new RedisStandaloneConfiguration();
	standConfig.setHostName("120.55.90.245");
	standConfig.setPort(6380);
	standConfig.setPassword(RedisPassword.of("test2016"));
	standConfig.setDatabase(0);
	JedisConnectionFactory factory2=new JedisConnectionFactory(standConfig) ;
	return factory2;
}
或用XML
<bean id="cacheManager" class="org.springframework.data.redis.cache.RedisCacheManager"  factory-method="create"  >
	<constructor-arg index="0" ref="jedisConnectionFactory"></constructor-arg>
</bean>
 <cache:annotation-driven cache-manager="cacheManager" />   
 
 
RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
RedisConnection conn=redisConnectionFactory.getConnection();
conn.del("MyGuava::key1".getBytes());
conn.close();


<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">  
   <property name="maxIdle" value="6"></property>  
   <property name="minEvictableIdleTimeMillis" value="300000"></property>  
   <property name="numTestsPerEvictionRun" value="3"></property>  
   <property name="timeBetweenEvictionRunsMillis" value="60000"></property>  
</bean>
 <bean id="jedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"  >
	<constructor-arg >
		<bean class="org.springframework.data.redis.connection.RedisStandaloneConfiguration">
			<property name="hostName" value="120.55.90.245"></property>
			<property name="port" value="6380"></property>
			<property name="password"  >
				<bean class="org.springframework.data.redis.connection.RedisPassword" factory-method="of" >
					<constructor-arg value="test2016"/>
				</bean>
			</property>
			<property name="database"  value="0"></property>
		</bean>
	</constructor-arg> 
</bean>
<bean id="stringRedisSerializer" class="org.springframework.data.redis.serializer.StringRedisSerializer"/>  
<bean id="jedisTemplate" class="org.springframework.data.redis.core.RedisTemplate"
	  p:connectionFactory-ref="jedisConnectionFactory">
	<property name="keySerializer" ref="stringRedisSerializer"/>  
    <property name="hashKeySerializer"  ref="stringRedisSerializer"/> 
 	<property name="valueSerializer">  
		 <!--   
		  <bean class="org.springframework.data.redis.serializer.JdkSerializationRedisSerializer"/>   
		  <bean class="org.springframework.data.redis.serializer.OxmSerializer"/> 不行的
		  <bean class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer"/>  
		  <ref bean="stringRedisSerializer"/>
		  -->
	   <bean class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer"/>   
	</property>  
 </bean>
 
import org.springframework.data.redis.core.ValueOperations;
 
RedisTemplate<String,Object> redisTemplate = context.getBean("jedisTemplate",RedisTemplate.class);  
//其中key采取了StringRedisSerializer  
//其中value采取JdkSerializationRedisSerializer   
ValueOperations<String, User> valueOper = redisTemplate.opsForValue();  
User u1 = new User("zhangsan",12);  
User u2 = new User("lisi",25);  
valueOper.set("u:u1", u1);  
valueOper.set("u:u2", u2);  
System.out.println(((User)valueOper.get("u:u1")).getName());  
System.out.println(((User)valueOper.get("u:u2")).getName());   


valueOper.set("key", 10);
DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("springdata_redis/checkandset.lua")));
redisScript.setResultType(Boolean.class);

boolean res= redisTemplate.execute(redisScript, Collections.singletonList("key"), 10, 20);//List keys,Object... args
System.out.println(res);

redisTemplate 就不用关闭连接

------checkandset.lua  ,LUA语言为了嵌入C/C++ 中
local current = redis.call('GET', KEYS[1])
 if current == ARGV[1]
   then redis.call('SET', KEYS[1], ARGV[2])
   return true
 end
 return false
------
Object val= stringRedisTemplate.execute(new RedisCallback<Object>() 
{
	public Object doInRedis(RedisConnection connection) throws DataAccessException 
	{
		Long size = connection.dbSize();
		//  如果使用StringRedisTemplate  则可以强转  StringRedisConnection
		((StringRedisConnection)connection).set("key", "value");
		return size;
	}
});



<bean id="listener" class="springdata_redis.subscribe1.MessageDelegateListenerImpl"/><!-- 有 public void onMessage(Object message) 方法-->
<bean id="jdkSerializer" class="org.springframework.data.redis.serializer.JdkSerializationRedisSerializer" />


 	<!--  新配置     -->
    <redis:listener-container>
        <redis:listener  ref="listener" serializer="jdkSerializer" method="onMessage" topic="chatroom" />
    </redis:listener-container>
 
 <!-- 传统配置  
    <bean id="messageListener" class="org.springframework.data.redis.listener.adapter.MessageListenerAdapter">
    	<property name="defaultListenerMethod" value="onMessage"></property>
        <property name="delegate" ref="listener" />
        <property name="serializer" ref="jdkSerializer" />
    </bean>
 
    <bean id="redisContainer" class="org.springframework.data.redis.listener.RedisMessageListenerContainer">
        <property name="connectionFactory" ref="jedisConnectionFactory"/>
        <property name="messageListeners">
            <map>
                <entry key-ref="messageListener">
                    <bean class="org.springframework.data.redis.listener.ChannelTopic">
                        <constructor-arg value="chatroom" />
                    </bean>
                </entry>
            </map>
        </property>
    </bean>
-->

redisTemplate.convertAndSend("chatroom", message);//listener的onMessage就会被调用
  
----
@Resource(name="redisTemplate")//可以把redisTemplate注入到  所有的 redisTemplate.opsForXxx 方法的返回类型      
private ListOperations<String, String> listOps;

listOps.leftPush(userId, url.toExternalForm());
 也可以直接用redisTemplate
redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());


方试二
@Autowired
private JedisPool jedisPool;

Jedis jedis = jedisPool.getResource();
//...
jedis.close();

方试三
因为ShardedJedisPool可以通过一致性哈希实现分布式存储


------------------	spring data redis  lettuce
 @Bean
 public LettuceConnectionFactory redisConnectionFactory()
 {
	return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
 }
public static void springDataLettuce() 
{
	 ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			 "springdata_redis_lettuce/spring_lettuce_client.xml");
	 LettuceConnectionFactory factory = context.getBean(LettuceConnectionFactory.class);
	 RedisConnection conn=factory.getConnection();
	 conn.set("key".getBytes(), "val".getBytes());
	 conn.close();
}

 
 <bean   class="org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory">
	<constructor-arg ref="redisStandaloneConfiguration"> <!-- 同Jedis的 -->
	</constructor-arg>
 </bean>
 
 <bean id="redisClient" class="io.lettuce.core.support.RedisClientFactoryBean">
	<property name="uri" value="redis://localhost:6379/1" />
	<!--  redis://password@localhost:6379/0 -->
	<!--   <property name="password" value="mypassword" /> -->   
</bean>
 
 
 
 
 
 
 
 
=========================Spring Data Mongodb 2.0.9




依赖 spring-data-commons-x.RELEASE.jar
public interface CustomerRepository extends MongoRepository<String, String> {
}

 
<!--
  <mongo:mongo-client  id="mongoClient" host="127.0.0.1" port="27017"  credentials="rootUser:rootPass@admin"  >
	<mongo:client-options connections-per-host="8"
			   threads-allowed-to-block-for-connection-multiplier="4"
			   connect-timeout="1000"
			   max-wait-time="1500" 
			   socket-keep-alive="true"
			   socket-timeout="1500"    />
</mongo:mongo-client>

二选一 --> 
<bean id="mongoClient" class="org.springframework.data.mongodb.core.MongoClientFactoryBean">
	<property name="credentials"  >
		<list>
			<bean class="com.mongodb.MongoCredential" factory-method="createCredential">
				<constructor-arg name="userName" value="zh"></constructor-arg>
				<constructor-arg name="password" value="123"></constructor-arg>
				<constructor-arg name="database" value="reporting"></constructor-arg>
			</bean>
		</list>
	</property>
	
	<!--
	<property name="host" value="localhost" />
	<property name="port" value="27017" />
	-->
	<!--  为事务用 -->
	<property name="replicaSetSeeds" > 
		<array>
			<bean class="com.mongodb.ServerAddress" >
				<constructor-arg index="0" value="127.0.0.1"/>
				<constructor-arg index="1" value="37017"/> 
			</bean>
			<bean class="com.mongodb.ServerAddress" >
				<constructor-arg index="0" value="127.0.0.1"/>
				<constructor-arg index="1" value="37018"/> 
			</bean>
			<bean class="com.mongodb.ServerAddress" >
				<constructor-arg index="0" value="127.0.0.1"/>
				<constructor-arg index="1" value="37019"/> 
			</bean>
		</array>
	</property>
</bean>


<mongo:db-factory  id="mongoDbFactory" dbname="reporting" mongo-ref="mongoClient"/>

<bean id="mongoMappingContext" class="org.springframework.data.mongodb.core.mapping.MongoMappingContext"> 
</bean>

<bean id="defaultMongoTypeMapper" class="org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper"> 
</bean>

<bean id="mappingConverter" class="org.springframework.data.mongodb.core.convert.MappingMongoConverter">
	<constructor-arg name="mongoDbFactory" ref="mongoDbFactory"/>
	 <constructor-arg name="mappingContext" ref="mongoMappingContext"/> 
	 <property name="typeMapper" ref="defaultMongoTypeMapper" ></property>
</bean>

<bean id="mongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
	<constructor-arg name="mongoDbFactory" ref="mongoDbFactory" />
	<constructor-arg name="mongoConverter" ref="mappingConverter"/>
</bean>
 
 <bean id="natureRepository"  class="springdata_mongodb.MyMongoRepositoryImpl">
	<property name="mongoTemplate" ref="mongoTemplate" />
</bean>
<!-- 方式二 -->
<mongo:repositories base-package="springdata_mongodb.repo"></mongo:repositories>
<mongo:mapping-converter base-package="springdata_mongodb.model" db-factory-ref="mongoDbFactory"></mongo:mapping-converter>


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class MyMongoRepositoryImpl {
	MongoTemplate mongoTemplate;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public List<Customer> getAllObjects() {
		return mongoTemplate.findAll(Customer.class);
	}

	public void saveObject(Customer c) {
		mongoTemplate.insert(c);
	}

	public Customer getObject(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)),
				Customer.class);
	}

	public UpdateResult updateObject(String id, String lastName) {
		return mongoTemplate.updateFirst(
				new Query(Criteria.where("id").is(id)),
				Update.update("lastName", lastName), Customer.class);
	}

	public void deleteObject(String id) {
		mongoTemplate.remove(new Query(Criteria.where("id").is(id)),
				Customer.class);
	}

	public void createCollection() {
		if (!mongoTemplate.collectionExists(Customer.class)) {
			mongoTemplate.createCollection(Customer.class);
		}
	}

	public void dropCollection() {
		if (mongoTemplate.collectionExists(Customer.class)) {
			mongoTemplate.dropCollection(Customer.class);
		}
	}
}

package springdata_mongodb.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="mo_customer")
public class Customer {
    @Id
    private String id;
	
	@Field(value="first_name")
    private String firstName;
	
	private String lastName;
	//getter/setter
}

repository.dropCollection();
repository.createCollection();

Customer lisi = new Customer("lisi", "李四");
lisi.setId("2");
repository.saveObject(lisi);
System.out.println("with id 2 " + repository.getObject("2"));

repository.updateObject("2", "五");
System.out.println(repository.getAllObjects());

repository.deleteObject("2");



//方式二  extends MongoRepository<Customer,String> //<Bean,ID>
MyCustomerRepository customerRepository = context.getBean(MyCustomerRepository.class);
for(int i=0;i<20;i++)
{
	Customer item = new Customer("lisi"+i, "李四"+i);
	customerRepository.save(item);  
}
Customer  cu=customerRepository.findByFirstNameAndLastName("lisi0" , "李四0");
List<Customer>  cus=customerRepository.findCustomersByTwoParam("lisi0" , "李四0");
	
package springdata_mongodb.repo;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface  MyCustomerRepository extends MongoRepository<Customer,String>//<Bean,ID>
{
	//JPA 命名规范   findBy (eclipse,idea都会提示)开头 关键字 And 相当于 where firstName= ? and lastName =?
	public  Customer  findByFirstNameAndLastName(String firstName,String lastName);
	
	@Query("{'first_name' : ?0 , 'lastName' : ?1}") //?0表示第一个参数
	public List<Customer> findCustomersByTwoParam(String first,String last);
	
} 

// 
MongoTemplate mongoTemplate =  context.getBean(MongoTemplate.class);
mongoTemplate.update(Customer.class).matching(query(where("first_name").is("li"))).
		apply(Update.update("lastName", "四22")).all() ; 

Criteria c=new Criteria();
c.orOperator(new Criteria[] {Criteria.where("first_name").regex("lisi")});
Query  query =new   Query(c); 
query.skip(2);
query.limit(10);//分页
Sort sort=new Sort(Sort.Direction.ASC,"createTime");
query.with(sort);

List<Customer> list=mongoTemplate.find(query, Customer.class);
long cnt=mongoTemplate.count(query, Customer.class);

public static void transactionSpring(ConfigurableApplicationContext context)
{ 
	MongoDbFactory  dbFactory=context.getBean(MongoDbFactory.class);
	MongoTemplate mongoTemplate =  context.getBean(MongoTemplate.class);
	
	MongoTransactionManager txManager=new MongoTransactionManager(dbFactory);
	TransactionTemplate txTemplate = new TransactionTemplate(txManager);         
	
	try {
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {     
				 
				Customer lisi = new Customer("li", "四"); 
				lisi.setId("2");
				mongoTemplate.insert(lisi); 
				
				if(1==1)
					 throw new RuntimeException("模拟回滚" ) ; //OK
					 
				OperHistory his = new OperHistory();
				his.setCreateTime(new Date());
				his.setDescription("登录操作");
				his.setIp("127.0.0.1");
				his.setModule("用户模块");
				his.setOperType("SELECT");
				his.setUserId("2");
				
				mongoTemplate.insert(his);  
			 
			}
		});
	
	}catch(Exception e)
	{
		System.out.println("出错事务回滚");	
	}
}



=========================Spring Data Neo4j
使用 neo4j-ogm-core.jar
ogm= Object-Graph mapping

=========================Spring Data Hadoop-2.1


========================Spring Batch
--- JDBC
     <bean id="jobLauncher"  class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
         <property name="jobRepository" ref="jobRepository" />
     </bean>
 
    <bean id="jobRepository" class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean">
        <property name="transactionManager" ref="transactionManager" />
    </bean>
  
    <bean id="transactionManager" class="org.springframework.batch.support.transaction.ResourcelessTransactionManager"/>
  
    <bean id="jdbcProcessor" class="spring_batch.jdbc.JDBCProcessor"/>
  
	<bean id="jdbcItemWriter"  class="org.springframework.batch.item.database.JdbcBatchItemWriter">
	    <property name="dataSource" ref="dataSource" />
	    <property name="sql"  value="insert into student (id,name,age)  values (:id,:name,:age)" /> <!-- :属性名 对应于 Student属性名 -->
	    <property name="itemSqlParameterSourceProvider">
	        <bean class="org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider" />
	    </property>
	</bean>
  
    <!--  page    如是MySQL第1页自动加  ORDER BY id ASC LIMIT 3,第二页自动加  AND ((id > 3)) ORDER BY id ASC LIMIT 3
    id是auto_increment 方式生成的,如有删除数据导致不连续的,就多读几次, 不影响批处理--> 
	<bean id="jdbcPageReader" class="org.springframework.batch.item.database.JdbcPagingItemReader">
		<property name="dataSource" ref="dataSource" />
		<property name="queryProvider">
			<bean class="org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean">
			     <property name="dataSource"  ref="dataSource"></property>
				<property name="selectClause" value="select id, name" />
				<property name="fromClause" value="from student" />
				<property name="whereClause" value="where age > :age" />
				<property name="sortKey" value="id" />
			</bean>
		</property>
		<property name="parameterValues">
			<map>
				<entry key="age" value="20" />
			</map>
		</property>
		<property name="pageSize" value="3" />
		<property name="rowMapper"  >
			<bean class="org.springframework.jdbc.core.BeanPropertyRowMapper">
			    <property name="mappedClass" value="spring_batch.Student"/>
			</bean>
		</property>
	</bean>
	
	 <batch:job id="jdbcPageJob">
         <batch:step id="pageStep">
             <batch:tasklet>
                 <batch:chunk reader="jdbcPageReader" writer="jdbcItemWriter" processor="jdbcProcessor"  commit-interval="1">
                 </batch:chunk>
             </batch:tasklet>
         </batch:step>
     </batch:job>
     
	
    <!--  cursor , 就是ResultSet , 根据写的sql查全部数据--> 
    <batch:job id="jdbcCursorJob">
         <batch:step id="cursorStep">
             <batch:tasklet>
                 <batch:chunk reader="jdbcCursorReader" writer="jdbcItemWriter" processor="jdbcProcessor"  commit-interval="1">
                 </batch:chunk>
             </batch:tasklet>
         </batch:step>
     </batch:job>

	<bean id="jdbcCursorReader" class="org.springframework.batch.item.database.JdbcCursorItemReader" scope="step">
	    <property name="dataSource" ref="dataSource" />
	    <property name="sql"   value="select id, name from student where id &lt; ?" />
	    <property name="rowMapper">
	        <bean class="org.springframework.jdbc.core.BeanPropertyRowMapper">
	            <property name="mappedClass" value="spring_batch.Student" />
	        </bean>
	    </property>
	    <property name="preparedStatementSetter" ref="paramStatementSetter" />
	</bean>
	
	<bean id="paramStatementSetter" class="org.springframework.batch.core.resource.ListPreparedStatementSetter"  scope="step">
	    <property name="parameters">
	        <list>
	            <value>#{jobParameters['id']}</value>
	        </list>
	    </property>
	</bean>
	

JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
Job job = (Job) context.getBean("jdbcCursorJob"); //jdbcPageJob
JobExecution result = launcher.run( job,
                    new JobParametersBuilder()
                            .addString("id", "10")
                            .toJobParameters()
                            );

//--------- <job> 依赖于 spring-retry-1.1.0.RELEASE.jar

<bean id="jobLauncher"  class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
	<property name="jobRepository" ref="jobRepository" />
</bean>
<bean id="jobRepository" class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean"/>

<bean id="transactionManager" class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />

<batch:job id="helloWorldJob">
	<batch:step  id="step_hello" next="step_world">
		<batch:tasklet  ref="hello" transaction-manager="transactionManager" />
	</batch:step>
	<batch:step  id="step_world" >
		<batch:tasklet  ref="world" transaction-manager="transactionManager" />
	</batch:step>
</batch:job>

<bean id="hello" class="spring_batch.HelloTasklet">
	<property name="message" value="Hello "/>
</bean>
<bean id="world" class="spring_batch.WorldTasklet">
	<property name="message" value="world "/>
</bean>
public class HelloTasklet implements Tasklet {
    private String message;
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext context)
            throws Exception {
        System.out.println(message);
        return RepeatStatus.FINISHED;
    }
}
JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
Job job = (Job) context.getBean("helloWorldJob");
JobExecution result = launcher.run(job, new JobParameters());
System.out.println(result.toString());


 
//---------  文件示例 
<beans:bean id="messageReader"  class="org.springframework.batch.item.file.FlatFileItemReader">
	<beans:property name="lineMapper" ref="lineMapper"/>
	<beans:property name="resource"   value="classpath:spring_batch/student.csv"/>
</beans:bean>

<beans:bean id="lineMapper" class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
	<beans:property name="lineTokenizer">
		<beans:bean  class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer"/>
	</beans:property>
	<beans:property name="fieldSetMapper">
		<beans:bean class="spring_batch.StudentMapper"/>
	</beans:property>
</beans:bean>
	
  <tasklet>
	   <chunk reader="messageReader" processor="messageProcessor"  writer="messageWriter" 
				commit-interval="5" retry-limit="2"  chunk-completion-policy=""> <!-- chunk-completion-policy表示 Step 的完成策略，即当什么情况发生时表明该 Step 已经完成 -->
		   <retryable-exception-classes>
			   <include class="java.net.ConnectException"/>
			   <exclude class="java.net.SocketTimeoutException"/>
		   </retryable-exception-classes>
		</chunk>
	</tasklet>
class StudentMapper implements FieldSetMapper<Student> 
implements ItemProcessor<Student, Message>
implements ItemWriter<Message>



 private static Object getProxyTargetObject(Object proxy) 
 {
	if(Proxy.isProxyClass(proxy.getClass()) )
    {		
		LOG.info("isProxyClass true ");
		Class  targetClass=proxy.getClass().getInterfaces()[0];
	}
	 if(!AopUtils.isAopProxy(proxy)) { //判断是否是代理类  
		 LOG.error("proxy 不是 AopProxy代理类 ");
		 return  proxy;
	}  
 
	try
	{
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");  
		h.setAccessible(true);  
		AopProxy aopProxy = (AopProxy) h.get(proxy);  
		Field advised = aopProxy.getClass().getDeclaredField("advised");  
		advised.setAccessible(true);  
		return  ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();  
	}catch(Exception ex)
	{
		 LOG.error("getProxyTargetObject error !!! ",ex);
	}
	return null;
}  

========================Spring RabbitMQ
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-amqp</artifactId>
    <version>2.0.4.RELEASE</version>
</dependency>

--server
 <!-- 建立  com.mj.test 的  Queue,Admin中建立用户
      <rabbit:connection-factory id="connectionFactory" username="guest" password="guest"
            host="localhost"
            port="5672"
            virtual-host="/"/>
             -->   
    <rabbit:connection-factory id="connectionFactory" username="zh" password="123"
                               host="172.16.35.35"
                               port="5672"
                               virtual-host="/"/>

   <rabbit:queue id="myQueueId1" name="myQueueName" durable="true" auto-delete="false" exclusive="false"/>


    <bean id="messageReceiver" class="spring_rabbitmq.simple.MyLitener"></bean>

    <rabbit:listener-container connection-factory="connectionFactory">
        <rabbit:listener queues="myQueueId1" ref="messageReceiver"/>
    </rabbit:listener-container>
	
	
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class MyLitener implements  MessageListener{
	@Override
	public void onMessage(Message message) {
			System.out.println("收到数据 body :" + new String(message.getBody()));
		System.out.println("收到数据 ReceivedRoutingKe :" + message.getMessageProperties().getReceivedRoutingKey() );
		System.out.println("收到数据 ReceivedExchange :" + message.getMessageProperties().getReceivedExchange());

	}
}

ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("spring_rabbitmq/simple/spring_rabbitmq_server.xml");
	
--client
   <rabbit:connection-factory id="connectionFactory" username="zh" password="123"
                               host="172.16.35.35"
                               port="5672"
                               virtual-host="/"/>

    <!--通过指定下面的admin信息，当前producer中的exchange和queue会在rabbitmq服务器上自动生成-->
    <rabbit:admin connection-factory="connectionFactory"/>

    <!--定义queue-->
    <rabbit:queue id="myQueueId2" name="myQueueName" durable="true" auto-delete="false" exclusive="false"/>

    <!-- 定义direct exchange，绑定myQueueName 自动建立exchange名为 myChange -->
    <rabbit:direct-exchange name="myChange" durable="true" auto-delete="false">
        <rabbit:bindings>
            <rabbit:binding queue="myQueueId2" key="hello"></rabbit:binding>
        </rabbit:bindings>
    </rabbit:direct-exchange>
    
    <!--定义rabbit template用于数据的接收和发送-->
    <rabbit:template id="myAmqpTemplate" connection-factory="connectionFactory" exchange="myChange"/>

	
	
ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("spring_rabbitmq/simple/spring_rabbitmq_client.xml");
AmqpTemplate amqpTemplate =(AmqpTemplate)ctx.getBean("myAmqpTemplate");
amqpTemplate.convertAndSend("hello", "xxx");//hello是Routing key ,对应xml配置key="hello"
System.out.println("发送了XXX");
ctx.close();//如果不关，就不退出 



======================Spring LADP
<dependency>
    <groupId>org.springframework.ldap</groupId>
    <artifactId>spring-ldap-core</artifactId>
    <version>2.3.2.RELEASE</version>
</dependency>

--未测试？？
<!--   
<bean id="ldapContextSource" class="org.springframework.ldap.core.support.LdapContextSource">
	<property name="url" value=""/>
	<property name="userDn" value=""/>
	<property name="password" value=""/>
	<property name="pooled" value=""/>
</bean>

 <bean id="ldapTemplate"  class="org.springframework.ldap.core.LdapTemplate">
	<property name="contextSource" value="ldapContextSource"/> 
	<property name="ignorePartialResultException" value="true"/> 
</bean>
-->
<ldap:context-source
	  url="ldap://localhost:389"
	  base="dc=example,dc=com"
	  username="cn=Manager"
	  password="secret" />

<ldap:ldap-template id="ldapTemplate" />

<bean id="personRepo" class="spring_ldap.PersonRepoImpl">
  <property name="ldapTemplate" ref="ldapTemplate" />
</bean>

public List<String> getAllPersonNames() {
  return ldapTemplate.search(
	 query().where("objectclass").is("person"),
	 new AttributesMapper<String>() {
		public String mapFromAttributes(Attributes attrs)
		   throws NamingException {
		   return attrs.get("cn").get().toString();
		}
	 });
}
   
======================AspectJ
.aj 文件
----拦截
public aspect SecurityAspect
{
	//定义切入 点
	private pointcut securityExecution():
			execution(public void BusinessLogic.businessMethod1());//声明连接点，BusinessLogic是接口，也可以不是
			
	before():securityExecution()
	{
		System.out.println("doSecurityCheck============");
	}
	
}


----
public aspect TransactionAspect
{
	private pointcut transactionExecution():
					execution(public void BusinessLogic.businessMethod1()) || 
					execution(public void BusinessLogic.businessMethod2()); 
	//拦截,在方法调用之前做一些事情
	before():transactionExecution()
	{
		System.out.println("Start............");
	}
	after():transactionExecution()
	{
		System.out.println("commit............");
	}
}

----引入　
//引入Product类没有implements Comparable接口
public aspect ProductComarableAspect
{
	declare parents : Product implements Comparable;
	public int Product.compareTo(Object o)
	{
		return (int)(this.getPrice() - ((Product)o).getPrice());
	}
}

----
public aspect ProductVlidatAspcet
{
	declare parents:Product extends IValidatable;　//interface 是接口，也可是abstract class
	
	public boolean Product.isOnSale()
	{
		return  this.getPrice()>0?true:false;
	}
}
----
public aspect World
{
	pointcut greeting() : execution(* Hello.sayHello(..));//格式
	after() returning() : greeting() //returning()
	{
		System.out.println(" World!");
	}
}


======================上 AspectJ

org.springframework.beans.BeanUtils.copyProperties(model, entity);// commons.beanutils 和 spring都有

//spring把@Controller中的所有的@RequestMapping的方法
Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();

----------------Kotlin


