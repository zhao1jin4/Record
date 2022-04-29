Ratpack 构建简单高效的 HTTP 微服务 ,基于Netty 来开发


微服务 要满足如下4个要求
1.根据业务模块划分服务器
2.每个服务器独立部署
3.轻量API调用
4.良好的高可用

也就是说可以没有监控， 注册中心，断路器 保证高可用
https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#common-application-properties

========================Spring Boot

src/main/resources/banner.txt  即修改默认的Banner,可在线生成 http://patorjk.com/software/taag/

SpringApplication newRun= new SpringApplication(Application.class); 
newRun.setBannerMode(Banner.Mode.OFF);
newRun.run(args);


spring-boot-devtools 可以实现页面和代码的热部署
<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>

http://start.spring.io/

spring-boot-autoconfigure.jar 中有 META－INF/spring.factories,定义格式为  全接口(@)名=全类名  有Initializers,Listeners 的Auto Configure区定义了很多@ 和对应的配置类

也可以自定义

mybatis-spring-boot-autoconfigure.jar(看源码) 有有用 @EnableConfigurationProperties
@EnableAutoConfiguration 中有 @Import({AutoConfigurationImportSelector.class}) ，这个类是实现了ImportSelector接口 有selectImports方法 返回的字串数组中的类名会被初始化，里面有META-INF/spring.factories
												SpringFactoriesLoader.loadFactoryNames()扩展spring用的类	
			 中有 @AutoConfigurationPackage 
			 		的 registerBeanDefinitions方法中  默认扫描启动类所在的包下的主类与子类的所有组件

spring-boot-start-web 默认使用 logback
<dependency>
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-slf4j-impl</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
	
	
spring-boot-start-freemarker , spring-boot-start-activemq,spring-boot-start-test,spring-boot-start-logging  
mybatis-spring-boot-starter
spring-boot-start-xx.jar 没有源码,只有配置 META－INF/spring.providers 只用来管理依赖

SpringApplication 加载 application.properties 或applicaion.yml  (先读yml再读properties)优先级依次为
1. classpath 根
2. classpath /config 包
3. 当前目录   (也就是说application.properties配置文件可.jar外的同级目录)
4. 当前目录下的 config目录

在application.yml再建一个配置文件 , 语法是三个横线
 
也可不叫application.properties  
$ java -jar myproject.jar --spring.config.name=myproject 
也可同时指定位置和名字
$ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties	

System.setProperty("spring.config.name", "sec-application");//会覆盖默认的application,除非,分隔加上默认的
//System.setProperty("spring.config.location", "classpath:/jsp/jsp-application.properties");
SpringApplication.run(MainSpringBootSecurity.class, args);//jar 启动 可以登录 ，war启动不行

#logging.file=my.log  日志输入到当前目录下的文件名
#logging.file=/tmp/springBoot.log

#新版本用 
logging.file.name=/tmp/springBoot.log	#LOG_FILE 变量



logging.level.root=DEBUG
logging.level.mybatis.dao=DEBUG 

logging.logback.rollingpolicy.max-file-size=200MB
logging.logback.rollingpolicy.max-history=7

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

@Value("${my-props.enable}")//yml中yes 变"true"
private String enable; 

//    @Value("${my-props.listProp2}")   //@Value 不能用于集合,List,Map是不行的
//    private List<String> listProp2 = new ArrayList<>(); 

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
 都是使用了@Conditional
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
@ConditionalOnProperty(name="useMyprop",havingValue="yes",matchIfMissing=true)//当配置useMyprop=yes时成立
//当matchIfMissing=true  表示如没有配置条件成立，默认matchIfMissing=false



@Conditional(SqlLogCondition.class)
public class SQLLogInterceptor implements Interceptor {}


public class SqlLogCondition implements Condition {
	@Value("${mylatch.sql.log:x}")//拿不到值
	private String sqlLogStr;
	@Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String configed=context.getEnvironment().getProperty("mylatch.sql.log");
		boolean res="true".equalsIgnoreCase(configed);
        return res;
    }
}



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
//如不指定class=,包名要和src/main/java里的包名一样，为了找到 @SpringBootApplication

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
---spring boot junit 5
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationTests {
    @Test
    public void contextLoads(){
    }
}







生成pom.xml 中有
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.3.5.RELEASE</version> <!-- 2.2.1.RELEASE 2.3.5.RELEASE  -->
	<relativePath/> 
</parent>
 
<!-- <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	 默认tomcat,如要用jetty 
-->
	 
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
	<configuration>
		<mainClass>config.MainApplication</mainClass>
	</configuration>
</plugin> 
<!-- 当有多个@SpringBootApplication的类时才需要加mainClass配置
	 mvn spring-boot:run 来启动,打包直接mvn package就是可执行jar包
	 (会生成 x.jar.original,在MANIFEST.MF中没有指定Main-Class,里面没有jar包)
	自己的东西在BOOT-INF目录中,有lib目录和classes目录
	新版本2.1 是可以带resources下所有的东西，老版 1.4.2 本就不会带所有的
	//File file=org.springframework.util.ResourceUtils.getFile("classpath:config/my.ini");//打成jar包就不行了
	ClassPathResource classPathResource = new org.springframework.core.io.ClassPathResource("config/my.ini");
	InputStream inputStream = classPathResource.getInputStream(); 
-->

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

可以国际化
spring.messages.cache-duration=36000
#for freemarker
spring.messages.basename=freemarker.error_messages,freemarker.form_messages

*.mvc 不能加载.js,.jpg,.html ???

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
spring.thymeleaf.suffix=.xhtml

*.mvc 不能加载.js,.jpg,.html ???
国际化不行？？？


---spring boot jsp
<packaging>war</packaging>     //要打成我war包 , 或右击项目run as -> tomcat


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


 <dependency> <!-- 为了使用JSTL库才要 -->
  <groupId>javax.servlet</groupId>
  <artifactId>jstl</artifactId>
</dependency> 

<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<scope>provided</scope>  
</dependency> 
<dependency>
	<groupId>javax.servlet.jsp</groupId>
	<artifactId>javax.servlet.jsp-api</artifactId>
	<version>2.3.3</version>
	<scope>provided</scope>
</dependency>

spring.mvc.view.suffix=.jsp
spring.mvc.view.prefix=/WEB-INF/jsp/
#i18n  
spring.messages.basename=jsp.error_messages,jsp.form_messages
spring.messages.cache-duration=36000
 
#spring.resources.static-locations=
#默认值为classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/
#spring.web.resources.static-locations 默认值为 classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/
 

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



#--config for *.mvc
spring:
  mvc:
    pathmatch:
      use-suffix-pattern: false
      use-registered-suffix-pattern: true
    contentnegotiation:
      favor-path-extension: false 
//--配置*.mvc
	@Bean
	public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		bean.addUrlMappings("*.mvc");
		return bean;
	} 
	//还有 FilterRegistrationBean ,ServletListenerRegistrationBean 对应于servlet api
	
以上两个一起配置*.mvc不会有打不开.js的问题

--spring boot  redis
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

application.properties中加

spring.redis.database=0
spring.redis.host=127.0.0.1 
spring.redis.port=6379
#--cluster和上面的host,port选一个
#spring.redis.cluster.nodes=127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005
#spring:
#  redis:  
#    cluster:
#      nodes:
#      - 127.0.0.1:7000
#      - 127.0.0.1:7001
#      - 127.0.0.1:7002
#      - 127.0.0.1:7003
#      - 127.0.0.1:7004
#      - 127.0.0.1:7005

#sentinel OK
#spring.redis.sentinel.master=mymaster
#spring.redis.sentinel.nodes=127.0.0.1:26379,127.0.0.1:26380

spring.redis.password=  

#cache1 and cache2 caches with a time to live of 10 minutes:
spring.cache.cache-names=cache1,cache2
spring.cache.redis.time-to-live=10m



spring.cache.type=REDIS
spring.cache.redis.use-key-prefix=true
spring.cache.redis.key-prefix=AppOne::  #如没有定义CacheManager生效,如定义了computePrefixWith方法


@SpringBootApplication 下加
@EnableCaching//Redis
 
 
 
@Value("${spring.cache.redis.key-prefix:MyApp::}")
private String prefixKey;

@Value("${spring.cache.redis.user-key-prefix:true}")
private boolean usePrefixKey;
  //这个对 @Cachable有用
 @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){ 
   //缓存配置对象
   RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
 
   redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) //设置缓存的默认超时时间：30分钟
//                .disableCachingNullValues()             //如果是空值，不缓存
           .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))         //设置key序列化器
           .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer())) //设置value序列化器
  		 
		 .computePrefixWith(cacheName->{
			//return "MyApp::"+cacheName;
			return   (usePrefixKey ? String.format("%s", prefixKey) : "")  + cacheName;
		})
		 ; 
   return RedisCacheManager
           .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
           .cacheDefaults(redisCacheConfiguration).build();
  }  
private RedisSerializer<Object> valueSerializer() {
//如缓存取出是 LinkedHashMap 强转 Book 要用 GenericJackson2JsonRedisSerializer
// redis中保存为 {"@class":"redis_single_cluster.Book","bookName":"book11Long"}
//带泛型,但时间是以时间戳的方式保存，存redis中不重要
		//GenericJackson2JsonRedisSerializer jackson=   new GenericJackson2JsonRedisSerializer( ); 
		
//---二选一
		Jackson2JsonRedisSerializer jackson=new Jackson2JsonRedisSerializer(Object.class);
		 ObjectMapper mapper=new ObjectMapper();
		 mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		 mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));//对Timestamp类型
		 mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL);//不显示null
		 
		 /* 加上这句，保存的json为 
		   [
			"redis_single_cluster.Book",
			{
				"bookName": "book11Long"
			}
			]
		*/
		 mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
		jackson.setObjectMapper(mapper);
		
		
		return jackson; 
}
 
  //---不同缓存过期时间 
@Bean
public RedisCacheManagerBuilderCustomizer myRedisCacheManagerBuilderCustomizer() {
	return (builder) -> builder
			.withCacheConfiguration("short_cache",
					RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10)))
			.withCacheConfiguration("long_cache",
					RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1)))
			;

 }
	 
@Bean //这个对@cache 没用
	public RedisTemplate<String, Object> buildRedisTemplate(RedisConnectionFactory connectionFactory,MyStringSerializer myStringSerializer ) {
		 
 	
	      
//		Jackson2JsonRedisSerializer<Object> jackson=new Jackson2JsonRedisSerializer<>(Object.class);
//		 ObjectMapper mapper=new ObjectMapper();
//		 mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		 mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));//对Timestamp类型
////			 mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL);//不显示null
//		 jackson.setObjectMapper(mapper);
		 
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	       redisTemplate.setKeySerializer(myStringSerializer);//支持Prefix
//	       redisTemplate.setValueSerializer(jackson);
//	       redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//	       redisTemplate.setHashValueSerializer(jackson);
	       redisTemplate.setConnectionFactory(connectionFactory);
	       return redisTemplate;
	   }


@Component
public class MyStringSerializer implements RedisSerializer<String> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.cache.redis.key-prefix:MyApp::}")
	private String prefixKey;

	private final Charset charset;

	public MyStringSerializer() {
		this(Charset.forName("UTF8"));
	}

	public MyStringSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	@Override
	public String deserialize(byte[] bytes) {
		String saveKey = new String(bytes, charset);
		int indexOf = saveKey.indexOf(prefixKey);
		if (indexOf > 0) {
			saveKey = saveKey.substring(indexOf);
		}
		return saveKey;
	}

	@Override
	public byte[] serialize(String string) {
		String key = prefixKey + string;
		return key.getBytes(charset);
	}
}



@Autowired  
private RedisTemplate<String,String> redisTemplate;  

/*incrby counter 10  加10
  incrbyfloat counter 0.5 后变小数，但如果再加一次就变整数11而不是11.0,
程序端要用  RedisTemplate<String, Double>，不能用  RedisTemplate<String, Object>，类型转会现Integer转为Double错误
 */

@Cacheable("cacheList") //Redis ,返回Bean 一定要Serializable
//redis中的key为"cacheList::SimpleKey []" ,type命令显示类型为string

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
public Object useRedisTemplate() {
	UserVO user=new UserVO();
	user.setId(32);
	user.setUsername("李");
	user.setBirthday(new Date());
	
	ValueOperations<String, Object> ops=redisTemplate.opsForValue();
	ops.set("myKey", user); 
	
	return ops.get("myKey"); 
}


	//Redis中就是key的前缀:: ,MyRedis::key1是string类型，里面是二进制的,自己定义的RedisTemplate没用,要用CacheManager
	@Cacheable(cacheNames="MyRedis", key="#isbn.rawNumber")//使用isbn的一个属性当做key
	public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
	{
		System.out.println("invoke findBook" );
		if("key1".equals(isbn.getRawNumber()))
		{
			Book book1=new Book();
			book1.bookName="book1";
			return book1;
		}
		return null;
	}
	 
@CacheEvict(cacheNames="cacheList",allEntries = true )  //清这个名称cacheList::开头的所有键 
@CacheEvict(cacheNames="MyRedis", key="#isbn.rawNumber") //清 MyRedis::xx 的一个键
	
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
 
---spring boot hazelcast
spring.hazelcast.config=classpath:config/my-hazelcast.xml
如不配置 在classpath中或者当前工作目录  找 hazelcast.xml,hazelcast.yaml,hazelcast.config

有 com.hazelcast.client.config.ClientConfig 的Bean




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
	//findBy开头 官方文档 搜索Supported keywords   参考 https://docs.spring.io/spring-data/jpa/docs/2.3.1.RELEASE/reference/html/#reference 
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

日志提示 persistence unit 'default'，
日志提示 spring.jpa.open-in-view 默认开启
spring.datasource.separator=/;  默认是;如有存储过程可以自定义



--存储过程 官方示例，在mysql上只部分成功，User就没什么用
# this for mysql
delimiter /

DROP procedure IF EXISTS plus1inout
/
CREATE procedure plus1inout (IN arg int, OUT res int)  
BEGIN  
	set res = arg + 1; 
END
/
 
@Entity 
public class User {
	@Id @GeneratedValue
	private Long id;
}
public interface UserRepository extends CrudRepository<User, Long> {
  
	@Procedure
	Integer plus1inout(Integer arg);
}

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryIntegrationTests {

	@Autowired UserRepository repository;
	@Test
	public void invokeDerivedStoredProcedure() {//OK
		Integer res1=repository.plus1inout(1);
		System.out.println(res1);
		Assertions.assertEquals(res1,2);
	}

	 
	@Autowired EntityManager em;
	@Test
	public void plainJpa21() { //OK

		StoredProcedureQuery proc = em.createStoredProcedureQuery("plus1inout");
		proc.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		proc.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);

		proc.setParameter(1, 1);
		proc.execute();
		
		Object obj=proc.getOutputParameterValue(2);
		System.out.println(obj);
		Assertions.assertEquals(obj, (Object) 2);
	}
}

--JAP Example 官方示例
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserRepository extends CrudRepository<User, Long>, QueryByExampleExecutor<User> {}

@Test
public void countBySimpleExample() {
	Example<User> example = Example.of(new User(null, "White", null));
	assertThat(repository.count(example)).isEqualTo(3L);
}
@Test
public void ignorePropertiesAndMatchByAge() { 
	Example<User> example = Example.of(flynn, matching().  
			withIgnorePaths("firstname", "lastname")); 
	assertThat(repository.findOne(example)).contains(flynn);
}
@Test
public void substringMatching() {

	Example<User> example = Example.of(new User("er", null, null), matching(). //
			withStringMatcher(StringMatcher.ENDING));

	assertThat(repository.findAll(example)).containsExactly(skyler, walter);//在数据库中过虑
}
@Test
public void matchStartingStringsIgnoreCase() {

	Example<User> example = Example.of(new User("Walter", "WHITE", null), matching(). //
			withIgnorePaths("age"). //
			withMatcher("firstname", startsWith()). //
			withMatcher("lastname", ignoreCase()));//SQL是lower(user0_.lastname)这种性能差
	assertThat(repository.findAll(example)).containsExactlyInAnyOrder(flynn, walter);
}
@Test
public void valueTransformer() {
	Example<User> example = Example.of(new User(null, "White", 99), matching(). //
			withMatcher("age", matcher -> matcher.transform(value -> Optional.of(Integer.valueOf(50)))));//常量 user0_.age=50 覆盖了99
	assertThat(repository.findAll(example)).containsExactly(walter);
}
----mapping class官方示例

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
	@Query(nativeQuery = true)
	List<SubscriptionSummary> findAllSubscriptionSummaries();
 
	@Query(nativeQuery = true)
	List<SubscriptionProjection> findAllSubscriptionProjections(int lessId);//匹配:lessId参数
}

@NamedNativeQueries({
		@NamedNativeQuery(name = "Subscription.findAllSubscriptionSummaries", //模板类名.方法名
				query = "select product_name as productName, count(user_id) as subscriptions from subscription group by product_name order by productName",  
				resultSetMapping = "subscriptionSummary"),

		@NamedNativeQuery(name = "Subscription.findAllSubscriptionProjections", 
				query = "select product_name as product, count(user_id) as usageCount from subscription where user_id < :lessId "//:lessId传参数 
						+ " group by product_name order by product") })
@SqlResultSetMapping( 
		name = "subscriptionSummary", 
		classes = @ConstructorResult(targetClass = SubscriptionSummary.class, 
				columns = { 
						@ColumnResult(name = "productName", type = String.class),
						@ColumnResult(name = "subscriptions", type = long.class) 
				}))
@Entity 
public class Subscription {

	private   @Id @GeneratedValue Long id = null;
	private String productName;
	private long userId;
	
}
interface SubscriptionProjection {
	String getProduct(); //只get方法
	long getUsageCount();
}

public class SubscriptionSummary { 
	String product;
	Long usageCount;
	public SubscriptionSummary(String product, Long usageCount) {
		this.product = product;
		this.usageCount = usageCount;
	}
}
---jpa 自定义 repo 官方示例
@Entity
@NamedQuery(name = "User.findByTheUsersName", query = "from User u where u.username = ?1")
public class User extends AbstractPersistable<Long> {
	@Column(unique = true)
	private String username;
	private String firstname;
	private String lastname;
	public User(Long id) {
		this.setId(id);//父类方法
	}
	public User() {
		this(null);
	}
}
interface UserRepositoryCustom {
	List<User> myCustomBatchOperation();
}
public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom { 
	//非标准方法名，定义在@NamedQuery
	User findByTheUsersName(String username); 
	//指定JPSQL
	@Query("select u from User u where u.firstname = :firstname")
	List<User> findByFirstname11(String firstname);//变量名传参数
}
class UserRepositoryImpl implements UserRepositoryCustom {
	@PersistenceContext //JPA包可注入
	private EntityManager em;
 
	public List<User> myCustomBatchOperation() {
		CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
		criteriaQuery.select(criteriaQuery.from(User.class));
		return em.createQuery(criteriaQuery).getResultList();
	}
}
@Profile("jdbc") //对于复杂的查询，用JPA不好做的情况，直接用JDBC，还可以mapping
@Component("userRepositoryImpl")
class UserRepositoryImplJdbc extends JdbcDaoSupport implements UserRepositoryCustom {
	private static final String COMPLICATED_SQL = "SELECT * FROM User";
	@Autowired
	public UserRepositoryImplJdbc(DataSource dataSource) {
		setDataSource(dataSource);
	}
	public List<User> myCustomBatchOperation() {
		return getJdbcTemplate().query(COMPLICATED_SQL, new UserRowMapper());
	}
	private static class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setLastname(rs.getString("lastname"));
			user.setFirstname(rs.getString("firstname"));
			return user;
		}
	}
}
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("jdbc") // 打开这个就是JDBC的实现
public class RepositoryCustomTests {
	@Autowired UserRepository repository;
	@Test
	public void saveAndFindByLastNameAndFindByUserName() {
		User user = new User();
		user.setUsername("foobar");
		user.setLastname("lastname");
		user.setFirstname("li");
		user = repository.save(user);
		List<User> users2 = repository.findByFirstname11("li");
		assertThat(users2).contains(user);
		assertThat(user).isEqualTo(repository.findByTheUsersName("foobar"));
	}
	@Test
	public void testCustomMethod() {
		User user = new User();
		user.setUsername("username");
		user = repository.save(user);
		List<User> users = repository.myCustomBatchOperation();
		assertThat(users).contains(user);
	}
}
--spring boot  mybatis 


<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>2.1.3</version>
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
	spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
	spring.datasource.username=root
	spring.datasource.password=root
	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	 就不用下面了
	
	//#references doc 84.1 Configure a Custom DataSource
	@Bean(name = "dataSource")
	@Qualifier(value = "dataSource")
	@Primary
	@ConfigurationProperties(prefix = "c3p0")
	public DataSource dataSource() {
		 return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
		 //DataSourceBuilder 优先使用hikari
		 //return DataSourceBuilder.create().url("").username("").password("").build();
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
-- spring boot jms artemis
<dependency>  
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-starter-artemis</artifactId>  
</dependency>
<dependency>
  <groupId>javax.jms</groupId>
  <artifactId>javax.jms-api</artifactId>
   <!--  2.0.1 -->
</dependency> 
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-jms</artifactId>
</dependency>



spring.jms.cache.session-cache-size=5

spring.artemis.mode=native
spring.artemis.host=127.0.0.1
spring.artemis.port=61616
spring.artemis.user=input
spring.artemis.password=input

#spring.artemis.pool.enabled=true
spring.artemis.pool.max-connections=50




@SpringBootApplication
@EnableJms
public class Application {
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }
    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type"); 
        return converter;
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        // Send a message with a POJO - the template reuse the message converter
        System.out.println("Sending an email message.");
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }
}
@Component
public class Receiver {
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }
}
public class Email  
{ 
	private String email; 
	private String titile; 
	public Email() {
	}
	//getter/setter
}


-- spring boot jms activemq
<dependency>  
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-starter-activemq</artifactId>  
</dependency>



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

@Component
public class MyApplicationRunner implements ApplicationRunner  {
	//ApplicationRunner 和 CommandLineRunner 都是在SpringApplication.run( )完成之前 调用
	@Override
	public void run(ApplicationArguments args) throws Exception {
		 System.out.println("===SpringBoot初始化完成，开始做自己的逻辑====");
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
    //http://localhost:8081/springboot_kafka/send?topic=t1&key=test1&data=hello122
    @RequestMapping("/send") 
    @ResponseBody
    String send(String topic, String key, String data) {
        template.send(topic, key, data);//也可加分区参数
        return "success";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }

    @KafkaListener(id = "t1", topics = "t1")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

    @KafkaListener(id = "t2", topics = "t2")  //也可单独配置groupId
    //自动创建topic,只在某一个broker上，即使配置了多个bootstrap-servers
    public void listenT2(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

}
---spring boot websocket
spring-boot-starter-websocket

--　https://spring.io/guides/gs/messaging-stomp-websocket/　有示例代码
https://github.com/callicoder/spring-boot-websocket-chat-demo  有示例代码
使用

SockJS  首先用webSocket,如果失败再偿试用其它协议
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
								.withSockJS() 
								;
    } 
}
@Controller
public class GreetingController { 
    @MessageMapping("hello")
    @SendTo("topic/greetings")//返回的对象广播到所有订阅的
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
   <version>3.5.0</version>
</dependency>

<link href="webjars/bootstrap/4.2.1/css/bootstrap.min.css" rel="stylesheet">
 <script src="webjars/jquery/3.5.0/jquery.min.js"></script>
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
//另一个示例 https://github.com/callicoder/spring-boot-websocket-chat-demo 
//可向session中存信息 ,@EventListener, messagingTemplate

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker

	}
}
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}


@MessageMapping("/chat.addUser")
@SendTo("/topic/public")
public ChatMessage addUser(@Payload ChatMessage chatMessage,
						   SimpMessageHeaderAccessor headerAccessor) {
	// Add username in web socket session
	headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
	return chatMessage;
}

---spring boot schedular

@SpringBootApplication
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

@Component
public class SchedulerTask {
    private int count=0;
    @Scheduled(cron="*/6 * * * * ?")
    private void process(){
        System.out.println("this is scheduler task runing  "+(count++));
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
}

@EnableAsync 是为  @Async
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

---
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId> spring-boot-starter-security</artifactId>
</dependency> 
 <dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-web</artifactId> 
</dependency>


spring.security.user.name=user
spring.security.user.password=pass

就有登录页面

--- spring boot oauth2
 <dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>


--- OAuth2 server
--- OAuth2 client 
	在start.spring.io中有OAuth2 client,OAuth2 resource server 

---
		 <dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId> spring-boot-starter-data-elasticsearch</artifactId>
		</dependency> 

----tomcat
server:
  tomcat:
    uri-encoding: UTF-8    默认值为 UTF-8
    max-threads: 200
    max-connections: 10000
    min-spare-threads: 10
    accept-count: 100
---upload 
spring.servlet.multipart.max-file-size  默认1MB
spring.servlet.multipart.max-request-size  默认10MB



<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency> 

<br/>upload <br/>
<form method="post" action="/J_SpringBoot/multiUpload" enctype="multipart/form-data">
	<input type="text" name="fileCount"><br>
    
    <input type="file" name="attach"><br>
    <input type="file" name="attach"><br>
    
    <input type="submit" value="提交">
</form> 


@PostMapping("/multiUpload")
@ResponseBody
public MyResponse multiUpload(HttpServletRequest request) {
	String id=request.getParameter("id");
	System.out.println("id="+id);
	String resMsg="";
	MultipartHttpServletRequest  multiPart=(MultipartHttpServletRequest) request;
	String fileCount=multiPart.getParameter("fileCount");
	System.out.println("fileCount="+fileCount);
	List<MultipartFile> files = multiPart.getFiles("attach");
	String filePath = "D:/tmp/";//当/tmp时spring boot下  /不是当前盘符的根，
	//而是 C:\Users\Administrator\AppData\Local\Temp\tomcat.8081.8781972415112657868\work\Tomcat\localhost\J_SpringBoot\
	for (int i = 0; i < files.size(); i++) {
		MultipartFile file = files.get(i);
		if (file.isEmpty()) {
			resMsg += "上传第" + (i++) + "个文件失败";
		}
		String fileName = file.getOriginalFilename();

		File dest = new File(filePath + fileName);
		try {
			file.transferTo(dest);
			System.out.println("第" + (i + 1) + "个文件上传成功");
		} catch (IOException e) {
			 e.printStackTrace();
			 resMsg += "上传第" + (i++) + "个文件失败";
		}
	}
	return new MyResponse(200,resMsg); 
} 

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency> 

spring.jackson.date-format  = yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone    = GMT+8
spring.jackson.default-property-inclusion=ALWAYS
#JsonInclude.Include.ALWAYS
#JsonInclude.Include.NON_NULL
		
#spring.web.resources.static-locations default classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/
 
//---@Valid 生效，JSON日期格式生效
public Employee queryEmployeeValid	( @Valid  Employee emp)  
{
}

@RestControllerAdvice
public class MyGlobalException {
	@ExceptionHandler(BindException.class)
	public Object commonException(BindException exception )
	{
		System.err.println(exception);
		BindingResult result=exception.getBindingResult() ;
		List<String> errList=new ArrayList<>();
		if(result.hasErrors())
		{
			for(ObjectError err:result.getAllErrors())
			{
				System.out.println(err.getObjectName()+"=="+err.getDefaultMessage());
				errList.add(err.getDefaultMessage());
			} 
		}
		return errList;
	}
	@ExceptionHandler(Exception.class)
	public Object commonException(Exception exception )
	{
		System.err.println(exception);
		return "Exception返回"+exception.getMessage();
	}
}


----从http头中取用户信息
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderUser {

}


@RequestMapping(value="/headerUser",method=RequestMethod.POST)
@ResponseStatus(HttpStatus.OK)
@ResponseBody 
public UserEntity  headerUser( @HeaderUser UserEntity headerUser) 
//扩展注解@HeaderUser 在 HeaderUserResolver ,SpringMVC项目中不行，SpringBoot项目就可，@RequestBody 
{
	headerUser.setName(headerUser.getName()+"___");
	return headerUser;
}


@Configuration
public class WebMvcConfig implements WebMvcConfigurer 
{
	@Resource
	private HeaderUserResolver headerUserResolver;
	@Override
	public void  addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		//pringMVC项目中调用不到，SpringBoot项目就可
		argumentResolvers.add(headerUserResolver);
       
    } 
} 

@Component 
public class HeaderUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return // parameter.getParameterType().isAssignableFrom( UserEntity.class) &&
        		parameter.hasParameterAnnotation(HeaderUser.class);
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {

        String openId = request.getHeader("openId"); 
        Assert.notNull(openId, "http头中没openId！"); 
        UserEntity user=new UserEntity();
        //模拟数据库
        user.setName("lisi-openid-"+openId);
        return user;
    }

}



---Spring Boot Shiro
https://shiro.apache.org/spring-boot.html
 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-spring-boot-web-starter</artifactId>
  <version>1.5.2</version>
</dependency>

未测试成功


--shiro.ini
[users] 
lisi=123,adminRole
wang=456,queryRole

[roles]
adminRole=employee:*
queryRole=employee:query

[main]
authc.usernameParam=j_username
authc.passwordParam=j_password
authc.rememberMeParam=j_rememberMe

--application.properties
shiro.enabled=true
shiro.web.enabled=true
shiro.annotations.enabled=true
shiro.loginUrl=/initLoginNoFilter
#shiro.successUrl=/ 
shiro.successUrl=/main

 #no effect ??
shiro.unauthorizedUrl=/noPerm.html
#no permisson find /error,can not define /error myself



//日志显示  要么有Realm 的@Bean 要么放 shiro.ini文件(不能两个一起使用)
//要求 在classpath下(src/main/resources/shiro.ini)或META-INF下(src/main/resources/META-INF/shiro.ini) 一定要有帐户数据
@Bean
public Realm realm(HashedCredentialsMatcher credentialsMatcher)  
{ 
	MySpringRealm realm=new MySpringRealm();
	realm.setCredentialsMatcher(credentialsMatcher);
	return realm;
}
@Bean
public HashedCredentialsMatcher credentialsMatcher()
{
	HashedCredentialsMatcher credentialsMatcher=new HashedCredentialsMatcher();
	credentialsMatcher.setHashAlgorithmName("md5");
	credentialsMatcher.setHashIterations(3);
	return credentialsMatcher;
}


@Bean  //这个权限缓存有效果
protected CacheManager cacheManager() {
	return new MemoryConstrainedCacheManager();
}
@Bean
public ShiroFilterChainDefinition shiroFilterChainDefinition() {
	DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
	chainDefinition.addPathDefinition("/**/*.html", "anon");
	chainDefinition.addPathDefinition("/**/*.js", "anon");
	chainDefinition.addPathDefinition("/**/*.jpg", "anon");
	chainDefinition.addPathDefinition("/main", "anon");
	chainDefinition.addPathDefinition("/test", "anon");
	//chainDefinition.addPathDefinition("/initLoginNoFilter", "anon");
	chainDefinition.addPathDefinition("/submitLoginNoFilter", "anon"); 
	chainDefinition.addPathDefinition("/logout", "logout");
	chainDefinition.addPathDefinition("/login", "authc"); //不会验证用户名密码？？可能不认j_username？
	chainDefinition.addPathDefinition("/**", "authc"); 
	return chainDefinition;
}






---Spring Boot Admin
用来做监控用的
在start.spring.io 中有 spring boot admin client/server

https://codecentric.github.io/spring-boot-admin/current/



<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>de.codecentric</groupId>
			<artifactId>spring-boot-admin-dependencies</artifactId>
			<version>2.2.1</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>

----Spring Boot Admin 服务端
 <!-- 测试下来j可选的
 <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
  -->
<dependency>
	<groupId>de.codecentric</groupId>
	<artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>

@EnableAdminServer //增加
@SpringBootApplication //也可使用@Configuration 和 @EnableAutoConfiguration
public class MyAdminServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyAdminServerApplication.class, args);
	}
} 

--application.yml
spring:
  application:
    name: admin-server
server:
  port: 8769

http://localhost:8769 自动进入 http://localhost:8769/applications
当户端连接上来，就可以看到客户端的很多信息,内存,线程,环境变量,日志级别调整,JMX,所有/actuator开头地址 的说明
Metrics标签中可选择很多项 ，增加做监控



--服务端发邮件
 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
--properties
spring.mail.host=		 #SMTP server host
spring.mail.port=  		 #SMTP server port
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true	#properties后的是JavaMail的属性,设置SMTP服务器需要权限认证

#ssl
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
spring.mail.properties.mail.smtp.socketFactory.port=465
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.starttls.enable=true

spring.boot.admin.notify.mail.enabled=true  #默认值true
spring.boot.admin.notify.mail.to=admin@example.com
spring.boot.admin.notify.mail.from=
spring.boot.admin.notify.mail.template= #默认值"classpath:/META-INF/spring-boot-admin-server/mail/status-changed.html"
spring.boot.admin.notify.mail.ignore-changes= #格式为<from-status>:<to-status> 默认值"UNKNOWN:UP"

--yml
#spring boot admin mail ,testOK
spring:
  boot:
    admin:
      notify:
        mail:
          to: xx@163.com
          from: xx@sina.com  
          enabled:  true  #默认值true
#        template:
#        ignore-changes: 
  mail:
    host: smtp.sina.com    #SMTP server host
#    port:      #SMTP server port
    username: xx
    password: 123
    properties: #properties后的是JavaMail的属性,设置SMTP服务器需要权限认证
      smtp:
        auth:true  
#ssl
#      smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
#      smtp.socketFactory.port=465
#      smtp.ssl.enable=true
#      smtp.starttls.enable=true



--Spring Boot Admin客户端
如果有 Spring Cloud Discovery (如Eureka) 就不需要Spring Boot Admin客户端了
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
 <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>de.codecentric</groupId>
	<artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>

spring.boot.admin.client.url=http://localhost:8769    #是指向 spring Boot Admin Server 
management.endpoints.web.exposure.include=*   
#暴露微服务的所有监控端口,生产环境应该只暴露部分

@SpringBootApplication
public class MyAdminClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAdminClientApplication.class, args);
	}
}
@Configuration
public   class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		//保证actuator路径可以被访问
        http.authorizeRequests().anyRequest().permitAll()  
            .and().csrf().disable();
    }
}
=== 有Eureka 
不使用Spring Boot Admin客户端 ,自己的应用
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency> 
<!--  spring boot admin 要可以访问 , /actuator ,  /actuator/health -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

@EnableDiscoveryClient


eureka:
  client:
    registryFetchIntervalSeconds: 5 #spring boot admin 
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    #spring boot admin 
    leaseRenewalIntervalInSeconds: 10
    health-check-url-path: /actuator/health
    metadata-map:
      startup: ${random.int} 
   
#spring boot admin 
management:
  endpoints:
    web:
      exposure:
        include: "*"  
  endpoint:
    health:
      show-details: ALWAYS
--- 不使用Spring Boot Admin客户端,Spring Boot Admin服务端也要注册到 Eureka 上
@EnableDiscoveryClient

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
	  
=====Spring Boot Dubbo

https://github.com/seata/seata-samples/tree/master/seata-spring-boot-starter-samples   
示例中使用 dubbo-spring-boot-starter 

 <dependency>
	<groupId>org.apache.dubbo</groupId>
	<artifactId>dubbo</artifactId>
	<version>2.7.5</version>
	<exclusions>
		<exclusion>
			<artifactId>spring</artifactId>
			<groupId>org.springframework</groupId>
		</exclusion>
	</exclusions>
</dependency>
<dependency>
	<groupId>org.apache.dubbo</groupId>
	<artifactId>dubbo-spring-boot-starter</artifactId>
	<version>2.7.5</version>
</dependency>


<dependency>
	<groupId>com.alibaba.nacos</groupId>
	<artifactId>nacos-client</artifactId>
	<version>1.2.1</version>
</dependency>

或者zookeeper的curator

<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-framework</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-recipes</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-x-discovery</artifactId>
	<version>4.3.0</version>
</dependency>
		
--dubbo服务提供者要加 @EnableDubbo ,服务使用者可以不加 ,可以使用zookeeper或Nacos

@SpringBootApplication(scanBasePackages = {"springboot_dubbo.server_anno"})
@EnableDubbo(scanBasePackages = "springboot_dubbo.server_anno")
public class SpringBootDubboProviderMain {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootDubboProviderMain.class, args);
	}
}

@Service //Dubbo的
public class DemoServiceImpl implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public String sayHello(String name) {
        logger.info("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }
}
dubbo:
  application:
    name: my-dubbo-provider
  protocol: 
    name: dubbo
    port: 20881
  registry:
    id: my-dubbo-provider
    #address: nacos://127.0.0.1:8848
    address: zookeeper://127.0.0.1:2181
#  config-center:
#    address: nacos://127.0.0.1:8848
#  metadata-report:
#    address: nacos://127.0.0.1:8848

----服务使用端
@SpringBootApplication(scanBasePackages = {"springboot_dubbo.client_anno"})
public class SpringBootDubboConsumerMain {
	public static void main(String[] args) throws Exception {
		//http://127.0.0.1:8082/J_SpringBoot_DubboConsumer/client
		SpringApplication.run(SpringBootDubboConsumerMain.class, args);
	}
}

@Component("demoServiceComponent") //这是一个包装类
public class DemoServiceComponent implements DemoService {
    @Reference( ) //Dubbo的 check=false,lazy=true
    private DemoService demoService; 
    @Override
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }

}
@Controller
public class  DubboClientController {
	@Autowired
	private DemoServiceComponent service;
	@RequestMapping(path="/client",produces = "text/plain;charset=UTF-8")
    public  String  client( ) {
        String hello = service.sayHello("world");
        System.out.println("result :" + hello);
        return hello;
    }
}

dubbo:
  application:
    name: my-dubbo-consumer
  protocol:
    name: dubbo
  registry:
    #address: nacos://127.0.0.1:8848
    address: zookeeper://127.0.0.1:2181
#  config-center:
#    address: nacos://127.0.0.1:8848
#  metadata-report:
#    address: nacos://127.0.0.1:8848

=========Camel spring boot
 <dependencyManagement>
	 <dependencies>
		<dependency>
			<groupId>org.apache.camel.springboot</groupId>
			<artifactId>camel-spring-boot-dependencies</artifactId>
			<version>3.4.0</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.apache.camel.springboot</groupId>
	<artifactId>camel-spring-boot-starter</artifactId>
</dependency>

 <!-- Camel -->
<dependency>
	<groupId>org.apache.camel.springboot</groupId>
	<artifactId>camel-servlet-starter</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.camel.springboot</groupId>
	<artifactId>camel-jackson-starter</artifactId>
</dependency>


<dependency>
	<groupId>org.apache.camel.springboot</groupId>
	<artifactId>camel-swagger-java-starter</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
		</exclusion>
	</exclusions>
</dependency>
包含下面的
<dependency>
  <groupId>org.apache.camel</groupId>
  <artifactId>camel-swagger-java</artifactId>
  <version>3.4.0</version>
</dependency>


@Component
class RestApi extends RouteBuilder {

	@Override
	public void configure() {
		restConfiguration()
			.contextPath("/camel-rest-jpa")
			
			//swagger ,maven有camel-swagger-java，仿问  http://localhost:8888/camel-rest-jpa/api-doc 就有json返回了
			.apiContextPath("/api-doc")
			.apiProperty("api.title", "Camel REST API")
			.apiProperty("api.version", "1.0")
			.apiProperty("cors", "true")  
				
				
				.apiContextRouteId("doc-api")
				.port(env.getProperty("server.port", "8080"))
			.bindingMode(RestBindingMode.json);

		rest("/books").description("Books REST service")
			.get("/").description("The list of all the books")
				.route().routeId("books-api")
				.bean(Database.class, "findBooks")
				.endRest()
			.get("order/{id}").description("Details of an order by id")
				.route().routeId("order-api")
				.bean(Database.class, "findOrder(${header.id})");
	}
}

  
//全局异常处理 ，会自动找和抛出异常继承关系最近的
onException(Exception.class).handled(true).process(new Processor() {
	@Override
	public void process(Exchange exchange) throws Exception {
		Exception ex=exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
		System.out.println("处理了异常");
		ex.printStackTrace();
	}
 }).setBody(simple("{\"errorType\":${exception.class.name},\"errorMessaage\":${exception.message}")).end(); 
//exception是抛出的异常类

onException(RuntimeException.class).handled(true)
.setHeader(Exchange.HTTP_RESPONSE_CODE,constant(400))
.setBody(simple("{\"errorType\":${exception.class.name},\"errorMessaage\":${exception.message}}"))
// \"stackTrace\":${exception.stackTrace}是不行的
.end();
		
		
		
		
rest("/student").id("student_id").description("student api desc") .consumes("application/json").produces("application/json")
		.post("/save").description("save student").type(Student.class).outType(CommonResponse.class).to("bean:studentService?method=save(${body})")
		.post("/save1").description("save student").type(Student.class).outType(CommonResponse.class)
							.route().bean(studentService,"save").endRest()
		.post("/getById/{stuId}").description("get student").type(Student.class).outType(CommonResponse.class).to("direct:getById")
		.post("/asyncGen").description("asyncGen  ").
			param().name("regenerate").type(RestParamType.query).dataType("boolean").defaultValue("true").endParam()
			.type(Student.class).outType(CommonResponse.class).to("seda:asyncGen")
		; //还可继续.type()
		 //inOnly过时了 
	
	from("direct:getById").to("bean:studentService?method=getById(${header.stuId})");//也可用全部参数 ${headers}
	from("seda:asyncGen").to("bean:studentService?method=asyncGen(${body},${header.regenerate})");
	
public CommonResponse getById(int id) {
	
}	
public CommonResponse asyncGen(Student stu,boolean isRegenerate )
{
}
	
---单元测试
camel:
  springboot:
    name: CamelRestJpa
  component:
    servlet:
      mapping:
        contextPath: /camel-rest-jpa/*    --  */
		
@Autowired
private TestRestTemplate restTemplate;
	
@Test
public void newOrderTest() { 
	ResponseEntity<Order> response = restTemplate.getForEntity("/camel-rest-jpa/books/order/1", Order.class);
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
}
@Test
public void booksTest() {
	ResponseEntity<List<Book>> response = restTemplate.exchange("/camel-rest-jpa/books",
		HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
		});
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
}		

----camel swagger
<dependency>
  <groupId>org.apache.camel</groupId>
  <artifactId>camel-swagger-java</artifactId>
  <version>3.5.0</version>
</dependency>
------swagger
https://github.com/springfox/springfox
https://github.com/springfox/springfox-demos  是使用Gradle

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>

import springfox.documentation.oas.annotations.EnableOpenApi;
@EnableOpenApi  //启用
@SpringBootApplication
public class  Application { 
 // http://127.0.0.1:8081/J_SpringBoot_swagger/swagger-ui/index.html 
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("products")
@Api(tags = "商品接口")
public class ProductsController {
	private static Logger LOG = LoggerFactory.getLogger(ProductsController.class);

	@ApiOperation("用户列表")
    @GetMapping("/users")
    public List<User> list(@ApiParam("查看第几页") @RequestParam int pageIndex,
                           @ApiParam("每页多少条") @RequestParam int pageSize) {
        List<User> result = new ArrayList<>();
        result.add(new User("aaa", 50, "北京", "aaa@ccc.com"));
        result.add(new User("bbb", 21, "广州", "aaa@ddd.com"));
        return result;
    }
	
}


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("用户基本信息")
public class User {

    @ApiModelProperty("姓名") 
    private String name;
    
    
    @ApiModelProperty("年龄") 
    private Integer age;
}

------redisson

<dependency>
	 <groupId>org.redisson</groupId>
	 <artifactId>redisson-spring-boot-starter</artifactId>
	 <version>3.14.1</version>
 </dependency> 
 依赖于 spring-data-redis,redisson-spring-data-23 和 spring-boot版本	2.3.x
 

spring:
  redis:
    database: 0
    
    #host: 192.168.42.129
    #port: 6379
    #password: redisPass
	 #--cluster
    cluster:
      nodes:
      - 127.0.0.1:7000
      - 127.0.0.1:7001
      - 127.0.0.1:7002
      - 127.0.0.1:7003
      - 127.0.0.1:7004
      - 127.0.0.1:7005
      
如是properties文件用 #spring.redis.cluster.nodes=127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005


	  
就可以用 

@Autowired
private RedissonClient  redissonClient;
@Autowired
private RedisTemplate   redisTemplate;



@EnableCaching
    @Bean
       CacheManager cacheManager(RedissonClient redissonClient) {
           Map<String, CacheConfig> config = new HashMap<String, CacheConfig>(); 
           // create "testMap" cache with ttl = 24 minutes and maxIdleTime = 12 minutes
           config.put("MyRedis", new CacheConfig(24*60*1000, 12*60*1000));//MyRedis 是一个hash,永不过期
           return new RedissonSpringCacheManager(redissonClient, config,new JsonJacksonCodec());   //为@Cacheable,key是带 \"
       }
	就可以
	//MyRedis 一个hash,永不过期,有一个key为 key1
	@Cacheable(cacheNames="MyRedis", key="#isbn.rawNumber")//使用isbn的一个属性当做key
	 
使用redisson 把	 session保存redis
 <dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-core</artifactId> 
</dependency>
 <dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId> 
</dependency>	  
@Configuration
@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {  
     @Bean
     public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
         return new RedissonConnectionFactory(redisson);
     } 
  }  
  //key 格式为 spring:session:sessions:<session-id> 的hash,里有 sessionAttr:myAttr,值是序列化的

------







#如客户端请求头Accept-Encoding: 没有gzip不会返回压缩
server.compression.enabled: true #启动响应response,响应头有 Content-Encoding: gzip
#server.compression.mime-types: 默认支持很多，不必设置
server.compression.min-response-size: 1 #默认2KB


