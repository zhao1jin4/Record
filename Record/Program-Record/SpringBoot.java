Ratpack 构建简单高效的 HTTP 微服务 ,基于Netty 来开发


微服务 要满足如下4个要求
1.根据业务模块划分服务器
2.每个服务器独立部署
3.轻量API调用
4.良好的高可用

也就是说可以没有监控， 注册中心，断路器 保证高可用

========================Spring Boot

spring-boot-devtools 可以实现页面和代码的热部署
<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>

http://start.spring.io/

spring-boot-autoconfigure.jar 中有 META－INF/spring.factories,定义格式为  全接口(@)名=全类名  有Initializers,Listeners 的Auto Configure区定义了很多@ 和对应的配置类
mybatis-spring-boot-autoconfigure.jar(看源码) 有有用 @EnableConfigurationProperties
@EnableAutoConfiguration 中有 @Import({AutoConfigurationImportSelector.class}) ，这个类是实现了ImportSelector接口 有selectImports方法 返回的字串数组中的类名会被初始化，里面有META-INF/spring.factories
												SpringFactoriesLoader.loadFactoryNames()扩展spring用的类	
			 中有 @AutoConfigurationPackage 
			 		的 registerBeanDefinitions方法中  默认扫描启动类所在的包下的主类与子类的所有组件

spring-boot-start-freemarker , spring-boot-start-activemq,spring-boot-start-web,spring-boot-start-test,spring-boot-start-logging  
mybatis-spring-boot-starter
spring-boot-start-xx.jar 没有源码,只有配置 META－INF/spring.providers 只用来管理依赖

SpringApplication 加载 application.properties 或applicaion.yml 的顺序
		A /config subdirectory of the current directory
		The current directory
		A classpath /config package
		The classpath root

在application.yml再建一个配置文件 , 语法是三个横线
 
也可不叫application.properties  
$ java -jar myproject.jar --spring.config.name=myproject  
也可同时指定位置和名字
$ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties	

#logging.file=my.log  日志输入到当前目录下的文件名
logging.file=/tmp/springBoot.log
logging.level.root=DEBUG
logging.level.mybatis.dao=DEBUG 

Spring Tool Suite 可以建立Spring starter project ,可选Maven(默认) 或 Gradle,Web组中选web  会自动建立项目
在resource的目录(classpath)下有
templates 目录放ftl文件 
static 目录 放css,js,图片
application.properties 是空的


myprop.name=prop_name_test
myprop.desc=descripion for ${myprop.name}
myprop.random.value=${random.value} 
myprop.random.int=${random.int} 
myprop.random.long=${random.long} 
myprop.random.int10=${random.int(10)} 
myprop.random.int10_20=${random.int[10,20]} 

@Value("${myprop.name:default_val}")
private String name; 

@Value("${myprop.random.value}")
private String random; //随机字串

@Value("${myprop.random.int}")
private int randomInt;

@Value("${myprop.random.long}")
private long randomLong;

@Value("${myprop.random.int10}")//# 10以内的随机数
private int randomIn10;

@Value("${myprop.random.int10_20}")//10-20的随机数
private int randomInt10_20;

java -jar xxx.jar --server.port=8888   也可以修改参数,会覆盖application.properties

application-dev.properties：开发环境
application-test.properties：测试环境
application-prod.properties：生产环境
至于哪个具体的配置文件会被加载，需要在application.properties文件中通过spring.profiles.active属性来设置，其值对应{profile}值
java -jar xxx.jar --spring.profiles.active=test
java org.springframework.boot.loader.JarLauncher  --spring.profiles.active=test  如解压

spring.profiles.active=dev
server.servlet.context-path=/J_SpringBoot
 /*
@ConditionalOnBean，仅在当前上下文中存在某个bean时，才会实例化这个Bean。
@ConditionalOnClass，某个class位于类路径上，才会实例化这个Bean。
@ConditionalOnExpression，当表达式为true的时候，才会实例化这个Bean。
@ConditionalOnMissingBean，仅在当前上下文中不存在某个bean时，才会实例化这个Bean。
@ConditionalOnMissingClass，某个class在类路径上不存在的时候，才会实例化这个Bean。
@ConditionalOnNotWebApplication，不是web应用时才会实例化这个Bean。
@AutoConfigureAfter，在某个bean完成自动配置后实例化这个bean。
@AutoConfigureBefore，在某个bean完成自动配置前实例化这个bean。
*/
//@ConditionalOnBean(RedisTemplate.class)//存在某个Bean时
//@ConditionalOnMissingBean(CacheManager.class) 
//@ConditionalOnJava(range=ConditionalOnJava.Range.EQUAL_OR_NEWER,value=JavaVersion.NINE)//当Java版本>=9.0
@ConditionalOnProperty(name="useMyprop",havingValue="yes",matchIfMissing=true)//如没有配置，就是havingValue配置的值yes

@PropertySource(value = "classpath:test.properties") 
@ConfigurationProperties(prefix = "my") //推荐增加 spring-boot-configuration-processor
@Component
public class ConfigBean {

    private String name;   //自动被设置指定文件中my.name的值
    private int age;
}

//入口类
@SpringBootApplication  //内部使用了 @ComponentScan,也就是说可以扫这个类所在包的子包
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}


---spring boot junit


//测试 OK
@RunWith(SpringRunner.class)
@SpringBootTest(classes=MybatisSpringBoot.class) //本类的所在包名无所谓
//@SpringBootConfiguration
@ContextConfiguration
public class SpringBootJunitTest {

	@Autowired
	private  UserMapper userMapper;
	
	@Test
	public void testMyBatis() {
		List<User> list= userMapper.selectAll();
		System.out.println(list);
	}

}

生成pom.xml 中有
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.1.1.RELEASE</version> <!-- 2.0.2.RELEASE 2.0.7.RELEASE 2.1.1.RELEASE -->
	<relativePath/> 
</parent>
 
<!--<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	 默认tomcat,如要用jetty  -->
	 
	 <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
		<exclusions>
		  <exclusion>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
		  </exclusion>
		</exclusions>
	  </dependency>
	  <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-jetty</artifactId>
	  </dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>



<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-starter-amqp</artifactId>  
</dependency>

<plugin>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
<!-- 可打包成.jar -->
<plugin> <!-- 可打包成.war包没有web.xml要加 -->
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-war-plugin</artifactId>
	<configuration>
		<failOnMissingWebXml>false</failOnMissingWebXml>
	</configuration>
</plugin>

@RestController
public class DemoResetController {
	
	@RequestMapping("sayHello")
	public String sayHello() { //可返回Map,List,对象(可不实现Serializable),页面显示JSON
		return "hello world";
	}
}

-- spring boot  freemarker 
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
默认是启用freemarker 下面都是默认值
spring.freemarker.enabled=true
spring.freemarker.cache=false
spring.freemarker.content-type=text/html
#spring.freemarker.charset=UTF-8  #offical api old error
spring.freemarker.encoding=UTF-8
spring.freemarker.template-loader-path=classpath:/templates/
spring.freemarker.prefix=
spring.freemarker.suffix=.ftl


templates 目录下放ftl文件即可，如有图片,js,css放static目录 使用@Controller 返回ModelAndView 即可

---spring boot 	thymeleaf
thymeleaf和freemarker只同时打开一个maven

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

默认是启用thymeleaf
spring.thymeleaf.enabled=true
#spring.thymeleaf.cache=true
spring.thymeleaf.cache=false
spring.thymeleaf.servlet.content-type=text/html
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML

spring.thymeleaf.prefix=classpath:/templates/ 
#spring.thymeleaf.suffix=.html
spring.thymeleaf.suffix=.xhtml

总是报视图找不到？？？？？？？

---spring boot jsp

spring.mvc.view.suffix=.jsp
spring.mvc.view.prefix=/WEB-INF/jsp/
#i18n  
spring.messages.basename=jsp.error_messages,jsp.form_messages
spring.messages.cache-duration=36000
 

<packaging>war</packaging>    要用tomcat启动才行

 <dependency> <!-- 为了使用JSTL库才要 -->
  <groupId>javax.servlet</groupId>
  <artifactId>jstl</artifactId>
</dependency> 

// SpringBootServletInitializer 实现 Spring自己的  WebApplicationInitializer  类
public class ServletInitalizer extends SpringBootServletInitializer {
    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(JSPApplication.class);
    }
}
src/webapp/WEB-INF/js/index.jsp 
<c:out value="c_cout_hello"/>

国际化： <spring:message code="welcome" arguments="小王,2018"   /> <br/>
提示：<spring:message code="try"    /> <br/>
  
@ServletComponentScan (basePackages= {"jsp"}) //就可以自动把@WebServlet,@WebListener,@WebFilter 等servlet自动注册 
 
  
@Autowired
private MessageSource messageSource;

  Locale locale = RequestContextUtils.getLocale(request);
 this.messageSource.getMessage("try", null, Locale.CHINESE); //war 启动OK，jar就不行
	

--spring boot  redis
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

application.properties中加
#spring.redis.host=127.0.0.1
spring.redis.host=172.16.37.42
spring.redis.port=6379
spring.redis.password=  

@SpringBootApplication 下加
@EnableCaching//Redis
 
 
@Autowired  
private RedisTemplate<String,String> redisTemplate;  

@Cacheable("cacheList") //Redis ,返回Bean 一定要Serializable
@RequestMapping("cachePage")
public List showList() {
	System.out.println("调用了showList方法 ");
	List<UserVO> list=new ArrayList<>();
	
	UserVO user=new UserVO();
	user.setId(32);
	user.setUsername("李");
	user.setBirthday(new Date());
	list.add( user); 
	
	UserVO user2=new UserVO();
	user2.setId(322);
	user2.setUsername("李2");
	user2.setBirthday(new Date());
	list.add( user2); 
	return list;
} 
	

@RequestMapping("redisTemplate")
public String useRedisTemplate() {
	ValueOperations<String, String> ops=	redisTemplate.opsForValue();
	ops.set("myKey", "my中文 ");
	return ops.get("myKey");
}
	
--redis cluster  
不行？？？？


--spring boot  session redis
<dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId>
	<version>2.0.4.RELEASE</version>
</dependency>


application.properties加

#created Bean named springSessionRepositoryFilter ,replacing   HttpSession
spring.session.store-type=redis
server.servlet.session.timeout=30m

再加redis的配置就可以了 , HttpSession setAttribute getAttribute 实际存在了redis中

#也可手工编码配置 ,顺序优于 application.properties
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
public class SessionConfig {
	@Bean
	public JedisConnectionFactory connectionFactory() {
		RedisStandaloneConfiguration standConfig=new RedisStandaloneConfiguration();
		standConfig.setHostName("192.168.56.101");
		standConfig.setPort(6379);
		standConfig.setPassword(RedisPassword.of("redisPass"));
		standConfig.setDatabase(0);
		JedisConnectionFactory factory2=new JedisConnectionFactory(standConfig) ;
		return factory2;
	}
}
 
 
---spring boot mongodb
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>


#spring.data.mongodb.uri=mongodb://zh:123@127.0.0.1:27017/reporting 
# or 
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27017
spring.data.mongodb.database=reporting
#spring.data.mongodb.authentication-database=
spring.data.mongodb.username=zh
spring.data.mongodb.password=123

import org.springframework.data.mongodb.MongoDbFactory;
import com.mongodb.client.MongoDatabase;


@Autowired
private  MongoDbFactory mongo;

MongoDatabase db = mongo.getDb();

---spring boot neo4j

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-neo4j</artifactId>
</dependency>

<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpcore</artifactId>
	<version>4.4.10</version>
</dependency>
<dependency>  
	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-ogm-http-driver</artifactId>
	<version>3.1.4</version>
</dependency>
 
spring.data.neo4j.uri=http://127.0.0.1:7474
spring.data.neo4j.username=neo4j
spring.data.neo4j.password=myneo4j
 
@Autowired
private org.neo4j.ogm.session.Session session;




@EnableNeo4jRepositories
public interface Neo4jMovieRepository extends Neo4jRepository<Movie, Long> {
	
	/**
	 * findOne 必须有一个结果，如无报错 
	 */
	Optional<Movie> findOneByTitle(@Param("one_title")String title);
	
	@Query("MATCH (e: lbl_Movie) WHERE e.one_title =~ ('.*'+{name}+'.*')  RETURN e")
    Collection<Movie> findByNameContaining(@Param("name") String name);
 
}
--spring boot  jpa
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

spring:
  jpa:
    generate-ddl: true
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update 
  datasource:
    username: zh
    password: 123
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mydb
    #使用hikari数据源
	
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class User {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long userid;
	@Column
	private String username;
	public User( ) { //一定要有默认构造器
		
	}
}
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	 
}	
@RestController
public class UserController {
	 @Autowired 
	 private UserRepository  userRepo; 
	
	 //http://127.0.0.1:8888/save/lisi
	 @PostMapping("/save/{username}")
	 public String saveUser(@PathVariable String username  )
	 {
		   userRepo.save(new User(username));
		   return "ok";
	 }
}
--spring boot  mybatis 


<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.3.0</version>
</dependency>
  
@SpringBootApplication
@MapperScan("mybatis.dao")  //MyBatis Mapper
@ComponentScan
public class MybatisSpringBoot {
	public static void main(String[] args) {
		SpringApplication.run(MybatisSpringBoot.class, args);
	}
}
  
#--hikari
#spring.datasource.hikari.

	/*
	加
	spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=true&useUnicode=true&amp;characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
	spring.datasource.username=root
	spring.datasource.password=root
	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	 就不用下面了
	
	//#references doc 84.1 Configure a Custom DataSource
	@Bean(name = "dataSource")
	@Qualifier(value = "dataSource")
	@Primary
	@ConfigurationProperties(prefix = "c3p0")
	public DataSource dataSource() {
		 return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
	}
	*/

	/*
	加 
	mybatis.mapper-locations=classpath:mapper/*Mapper.xml  
	 就不用下面了
	 
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());//数据源可通过其它方式取得
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*Mapper.xml"));//映射文件是resource/mybatis/目录下所有.xml文件,也可用application.properties
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	*/
	
 
mybatis.mapper-locations=classpath:mapper/*Mapper.xml             #也可程序中设置 */
mybatis.config-locations=classpath:mapper/config/Config.xml
#mybatis.type-aliases-package=mybatis.vo  #not effect ???

spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=true&useUnicode=true&amp;characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
spring.datasource.username=root
spring.datasource.password=root
#spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	
c3p0.jdbcUrl=jdbc:mysql://localhost:3306/mydb
c3p0.user=root
c3p0.password=root
c3p0.driverClassName=com.mysql.jdbc.Driver
 
 
 
 
 
 
 
--spring boot  rabbitmq

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=zh
spring.rabbitmq.password=123
#spring.rabbitmq.virtualHost=/  
spring.rabbitmq.virtual-host=/


@Configuration  
public class AmqpConfig {  
    public static final String EXCHANGE   = "spring-boot-exchange";  
    public static final String ROUTINGKEY = "spring-boot-routingKey";  
    @Bean  
    public ConnectionFactory connectionFactory() {  
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  
        connectionFactory.setAddresses("172.16.35.35:5672");  
        connectionFactory.setUsername("zh");  
        connectionFactory.setPassword("123");  
        connectionFactory.setVirtualHost("/");  
        connectionFactory.setPublisherConfirms(true); //必须要设置  
        return connectionFactory;  
    }  
  
    @Bean  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    //必须是prototype类型  
    public RabbitTemplate rabbitTemplate() {  
        RabbitTemplate template = new RabbitTemplate(connectionFactory());  
        return template;  
    }  
    /**  
     * 针对消费者配置  
     * 1. 设置交换机类型  
     * 2. 将队列绑定到交换机  
        FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念  
        HeadersExchange ：通过添加属性key-value匹配  
        DirectExchange:按照routingkey分发到指定队列  
        TopicExchange:多关键字匹配  
     */  
    @Bean  
    public DirectExchange defaultExchange() {  
        return new DirectExchange(EXCHANGE);  
    }  
    @Bean  
    public Queue queue() {  
        return new Queue("spring-boot-queue", true); //队列持久  ，不存在会自动创建Queue
    }  
    @Bean  
    public Binding binding() {  
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(AmqpConfig.ROUTINGKEY);  
    }  
    @Bean  
    public SimpleMessageListenerContainer messageContainer() {  
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());  
        container.setQueues(queue());  
        container.setExposeListenerChannel(true);  
        container.setMaxConcurrentConsumers(1);  
        container.setConcurrentConsumers(1);  
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认  
        container.setMessageListener(new ChannelAwareMessageListener() {  
            @Override  
            public void onMessage(Message message, Channel channel) throws Exception {  
                byte[] body = message.getBody();  
                System.out.println("receive msg : " + new String(body));  
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费  
            }  
        });  
        return container;  
    }  
  
}  
@Component  
public class Send implements RabbitTemplate.ConfirmCallback {  
  
    private RabbitTemplate rabbitTemplate;  
   
    @Autowired  
    public Send(RabbitTemplate rabbitTemplate) {  
        this.rabbitTemplate = rabbitTemplate;  
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容  
    }  
  
    public void sendMsg(String content) {  
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());  
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, AmqpConfig.ROUTINGKEY, content, correlationId);  
    }  
  
    /**  
     * 回调  
     */  
    @Override  
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {  
        System.out.println(" 回调id:" + correlationData.getId());  
        if (ack) {  
            System.out.println("消息成功消费");  
        } else {  
            System.out.println("消息消费失败:" + cause);  
        }  
    }  
  
}  


-- spring boot  非web程序
spring-boot-1.5.2.RELEASE.jar
spring-boot-autoconfigure-1.5.2.RELEASE.jar

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication  //等同用 @Configuration 和 , @EnableAutoConfiguration,@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) 
    {
        return args -> {//是CommandLineRunner 接口的一个run方法参数String[] 
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}

//-- web 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@EnableAutoConfiguration
public class SampleController {
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args); 
		//new SpringApplicationBuilder(SampleController.class).web(WebApplicationType.SERVLET).run(args);
    }
}

//方式二  web方式
SpringBootServletInitializer 实现 Spring自己的 WebApplicationInitializer  类

public class MyInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MyApplication.class);
	}

}

@SpringBootApplication 
@ImportResource({"classpath:config/spring.xml"})
public class MyApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}
}

---spring kafka

public class SpringKafkaCoding {
	String group="group2";
	String topic="test";
	@Test
	public void testAutoCommit() throws Exception {
		ContainerProperties containerProps = new ContainerProperties(topic);
		final CountDownLatch latch = new CountDownLatch(4);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
			@Override
			public void onMessage(ConsumerRecord<Integer, String> message) {
				System.out.println("received: " + message);
				latch.countDown();
			}
		});
		KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
		container.setBeanName("testAuto");
		container.start();//不会一直阻塞的
		Thread.sleep(1000); // wait a bit for the container to start
		KafkaTemplate<Integer, String> template = createTemplate();
		template.setDefaultTopic(topic);
		template.sendDefault(0, "foo");
		template.sendDefault(2, "bar");
		template.sendDefault(0, "baz");
		template.sendDefault(2, "qux");
		template.flush();
		assertTrue(latch.await(60, TimeUnit.SECONDS));
		container.stop();
		System.out.println("Stop auto");
	}
	private KafkaMessageListenerContainer<Integer, String> createContainer(ContainerProperties containerProps) {
		Map<String, Object> props = consumerProps();
		DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf, containerProps);
		return container;
	}

	private KafkaTemplate<Integer, String> createTemplate() {
		Map<String, Object> senderProps = senderProps();
		ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<Integer, String>(senderProps);
		KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
		return template;
	}

	private Map<String, Object> consumerProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}

	private Map<String, Object> senderProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
}



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class}) 
public class SpringKafkaAnnotaion 
{
	String topic="test";
	@Autowired
	private Listener listener;

	@Autowired
	private KafkaTemplate<Integer, String> template;

	@Test
	public void testSimple() throws Exception {
	    template.send(topic, 0, "foo");//topic ,key,value
	    template.flush();
	    assertTrue(this.listener.latch1.await(10, TimeUnit.SECONDS));
	} 
}
 
@Configuration
@EnableKafka
class Config 
{
    @Bean
    ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() 
    {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
    	String group="group2";
    	
        Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
    }

    @Bean
    public Listener listener() {
        return new Listener();
    }

    @Bean
    public ProducerFactory<Integer, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() { 
        
        Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
    }
    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        return new KafkaTemplate<Integer, String>(producerFactory());
    }
}

class Listener 
{
    public final CountDownLatch latch1 = new CountDownLatch(1);
    @KafkaListener(id = "foo", topics = "test")
    public void listen1(String foo) {
    	System.out.println("receive "+foo);
        this.latch1.countDown();
    }

}
---spring boot kafka
spring.kafka.consumer.group-id=foo
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.bootstrap-servers=localhost:9092

spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer

@Controller
@EnableAutoConfiguration
public class SampleController {

    public static Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private KafkaTemplate<String, String> template;

    @RequestMapping("/send")//http://localhost:8081/send?topic=t1&key=test1&data=hello122
    @ResponseBody
    String send(String topic, String key, String data) {
        template.send(topic, key, data);
        return "success";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }

    @KafkaListener(id = "t1", topics = "t1")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

    @KafkaListener(id = "t2", topics = "t2")  //自动创建topic
    public void listenT2(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

}
---spring boot websocket
spring-boot-starter-websocket

--　https://spring.io/guides/gs/messaging-stomp-websocket/　有示例代码

使用
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>sockjs-client</artifactId>
    <version>1.1.2</version>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>stomp-websocket</artifactId>
    <version>2.3.3</version>
</dependency>

  
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("gs-guide-websocket")
								.addInterceptors(new HttpSessionHandshakeInterceptor())
								 //.setHandshakeHandler(handshakeHandler())
								.withSockJS() /浏览器不支持websocket用socketjs 模拟
								;
    } 
}
@Controller
public class GreetingController { 
    @MessageMapping("hello")
    @SendTo("topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    } 
}


<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>bootstrap</artifactId>
   <version>4.2.1</version>
</dependency>
<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>jquery</artifactId>
   <version>3.3.0</version>
</dependency>

<link href="webjars/bootstrap/4.2.1/css/bootstrap.min.css" rel="stylesheet">
 <script src="webjars/jquery/3.3.0/jquery.min.js"></script>
 <script src="webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
 <script src="webjars/stomp-websocket/2.3.3/stomp.min.js"></script>
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
                	classes=SockjsStompApp.class)
public class GreetingIntegrationTests {

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void getGreeting() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();

        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("topic/greetings", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Greeting.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Greeting greeting = (Greeting) payload;
                        try {
                            assertEquals("Hello, Spring!", greeting.getContent());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
                try {
                    session.send("app/hello", new HelloMessage("Spring"));
                } catch (Throwable t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };

        this.stompClient.connect("ws://localhost:{port}/J_SpringBoot/gs-guide-websocket", this.headers, handler, this.port);

        if (latch.await(3, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
            fail("Greeting not received");
        }

    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;


        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}

---spring boot security

spring-boot-starter-security

@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(User.withUsername("user")
				.password("{noop}password").roles("USER").build());
	}
}
//会建立  springSecurityFilterChain 的bean  对应于web.xml配置
public class SecurityWebApplicationInitializer
	extends AbstractSecurityWebApplicationInitializer {

	public SecurityWebApplicationInitializer() {
		super(WebSecurityConfig.class);
	}
}
--- spring boot oauth
 <dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>


--- OAuth2 server
--- OAuth2 client

---
		 <dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId> spring-boot-starter-data-elasticsearch</artifactId>
		</dependency> 
		
---upload 
spring.servlet.multipart.max-file-size
spring.servlet.multipart.max-request-size

----tomcat
server:
  tomcat:
    uri-encoding: UTF-8
    max-threads: 200
    max-connections: 10000
    min-spare-threads: 10
    accept-count: 100


