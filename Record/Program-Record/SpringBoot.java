
========================Spring Boot
http://start.spring.io/

spring boot读META－INF/spring.factories 文件


#logging.file=my.log  日志输入到当前目录下的文件名
logging.file=/tmp/springBoot.log
logging.level.root=DEBUG
logging.level.org.zhaojin.dao.mapper=DEBUG 

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


@PropertySource(value = "classpath:test.properties") 
@ConfigurationProperties(prefix = "my")
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
@SpringBootConfiguration
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
	<version>1.5.8.RELEASE</version>
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
默认是true
spring.freemarker.enabled=true

templates 目录下放ftl文件即可，如有图片,js,css放static目录 使用@Controller 返回ModelAndView 即可
---spring boot jsp

spring.mvc.view.suffix=.jsp
spring.mvc.view.prefix=/WEB-INF/jsp/


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
测试 OK

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
server.servlet.session.timeout=600 

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
  
 
	/*
	加
	spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=true&useUnicode=true&amp;characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
	spring.datasource.username=root
	spring.datasource.password=root
	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	 就不用下面了
	 
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
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	
c3p0.jdbcUrl=jdbc:mysql://localhost:3306/mydb
c3p0.user=root
c3p0.password=root
c3p0.driverClassName=com.mysql.jdbc.Driver
 
 
 
 
 
 
 
--spring boot  rabbitmq
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
		//new SpringApplicationBuilder(SampleController.class).web(true).run(args);
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

