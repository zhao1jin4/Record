https://microservices.io/

========================Spring Cloud
版本名是伦敦地铁站的名字，字母表的顺序 
 
 
netflix 的 eureka 闭源，netflix 的 hystrix 不维护  推荐 Resilience4j 
https://docs.spring.io/spring-cloud-netflix/docs/2.2.6.RELEASE/reference/html/			Modules In Maintenance Mode
Zuul，Ribbon，Hystrix-xx，turbine-xx 都是维护模式，不包括 Euraka 




Consul
Zookeeper 也可配置 
Config 
Bus  			使用 RabbitMQ 或Kafka 
netflix
	Ribbon -> loadbalancer (上层为 OpenFeign) 
	zuul   -> Gateway
Circuit Breaker
	Hystrix	->  Resilience4j  (上层为 circuitbreaker)
Security		使用OAuth2
Sleuth			第三方的分布式跟踪解决方案 zipkin

Spring Cloud Kubernetes
	


Intellij Idea 建立 spring initialir 项目->Cloud Discory -> eureka server  会自动生成pom.xml
 
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.5.RELEASE</version>
</parent>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Hoxton.SR9</version>  
			<!-- 
				Hoxton.SR9  	要和 spring-boot 2.3.5.RELEASE
				 2020.0.0	  	要和 spring-boot 2.4.1 去了 netflix-xx 如zuul，turbine，hystrix ,ribbon(Eureka还有) 用Junit5,
			-->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
		<!--  如果不想使用<parent>标签时,用这个
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>2.2.1.RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
		-->
		
 </dependencies>
	 
	
</dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency> 
	 
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency> 
	 
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
	
	<!-- eureka-client 和 eureka-server 都要httpcore-->
	<dependency>
		<groupId>org.apache.httpcomponents</groupId>
		<artifactId>httpcore</artifactId>
		<version>4.4.10</version>
	</dependency> 
		 
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
	</dependency>
	<!-- hystrix 和 zuul都要 -->
	<dependency>
		<groupId>com.netflix.hystrix</groupId>
		<artifactId>hystrix-javanica</artifactId>
		<version>1.5.18</version>
	</dependency>
	 
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
	</dependency>
	 
	
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-server</artifactId>
	</dependency>
	
	
	
</dependencies>

打包插件
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
--- 

---Eureka server 
CAP  任何分布式系统只可同时满足二点，没法三者兼顾。
一致性(Consistency)  所有节点在同一时间具有相同的数据，强一致
可用性(Availability)  保证每个请求不管成功或者失败都有响应 
分隔容忍(Partition tolerance)  系统中任意信息的丢失或失败不会影响系统的继续运作 

Eureka 是 AP(一致性弱) Consul，zooKeeper，etcd 都是 CP(牺牲可用性)
 
BASE 
Basically Available基本可用。	可以部分服务不可用，但核心服务要可用。
Soft state软状态。 				状态可以有一段时间不同步，异步，如状态为支付中。
Eventually consistent最终一致。 可以一断时间内不一致，如写主，从不能及时看到最新，但等一会即好，而不是强一致。

 
消费者和eureka每30秒一次心跳，消费者缓存 
是一种客户端发现，像zookeeper也是

服务端发现有consul+nginx  (Consul Template 生成 nginx.conf 文件， 然后 命令运行 NGINX 重新加载配置文件)

https://github.com/Netflix/Eureka

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-netflix-eureka-server</artifactId>
</dependency>

<!-- 如用新的JDK9 要加 -->
 <dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
  <groupId>org.glassfish.jaxb</groupId>
  <artifactId>jaxb-runtime</artifactId>
  <version>2.3.1</version>
</dependency>
		
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;  

@SpringBootApplication
@EnableEurekaServer    // 相当于  dubbo zookeepr
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

--文件application.yml  注释同properties用#,可行中
server:
  port: 8761

eureka:
#  server:
#     enable-self-preservation: false #关闭自保护,有自保护的原因有些注册的节点没有收到心跳正常应该是去除，因自保护原因，eureka没有去除，可能是eureka自身的原因导致没有必跳
#     eviction-interval-timer-in-ms: 20 #默认60秒

  instance:
  #  lease-renewal-interval-in-seconds: 30 #影响自保护
  #  lease-expiration-duration-in-seconds: 90  #影响自保护

    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
	  # defaultZone: http://user:password@localhost:8761/eureka
      #登录要密码 ，pom增加spring-boot-starter-security,yaml配置与上相同的用户密码 ,或者实现DiscoveryClientOptionalArgs 

spring:
  security: 
    user:
      name: user
      password: password
	  
//通过eureka.client.registerWithEureka:false 和 fetchRegistry:false来表明自己是一个eureka server.	
不是一个client，eureka做群集时不能加这两个配置

运行后可以仿问 http://localhost:8761  有界面  ，是No instances available

--Eureka client(server)

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient  //相当于  dubbo provider ,也可以使用通用的 @EnableDiscoveryClient
@RestController
public class ServiceHiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceHiApplication.class, args);
	}

	@Value("${server.port}")
	String port;
	
	@RequestMapping("/hi")
	public String home(@RequestParam String name) {
		return "hi "+name+",i am from port:" +port;
	}

	@Autowired
	private EurekaClient eurekaClient;
	
	//http://127.0.0.1:8763/services  http://127.0.0.1:8762/services
	@RequestMapping("/services") 
	public String serviceUrl() {
		//SERVICE-HI要大写
	    InstanceInfo instance = eurekaClient.getNextServerFromEureka("SERVICE-HI", false);//只取第一个
	    return instance.getHomePageUrl();
	}
	
	@Autowired
	private DiscoveryClient discoveryClient;
	//请求  http://localhost:8763/services2
    @GetMapping("/services2")
    public List<String> serviceUrl2() {
        List<ServiceInstance> list = discoveryClient.getInstances("SERVICE-HI");
        List<String> services = new ArrayList<>();
        if (list != null && list.size() > 0 ) {
            list.forEach(serviceInstance -> {
                services.add(serviceInstance.getUri().toString());
            });
        }
        return services;
    }
}

文件 bootstrap.yml

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
	  #defaultZone: http://user:password@localhost:8761/eureka #如eureka要密码这样不行？？
   instance:	
		prefer-ip-address: true #显示在列表中的链接地址以IP显示，而不是主机名
		#Greewich版本变为 preferIpAddress 
		instance-id: ${spring.application.name}:${server.port} #显示在列表中以的格式
		#Greewich版本变为 instanceId
		#appname:service-hi-x  #默认是 spring.application.name		
server:
  port: 8762
  #port: 8763
spring:
  application:
    name: service-hi

启动后	服务端localhost:8761  就有名为 service-hi 的Instances 
测试
http://localhost:8762/hi?name=lisi


---客户端二feign  用在类别级  即也用 EnableDiscoveryClient
	 
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>


  @EnableDiscoveryClient
  @EnableFeignClients 

	  接口类
	  @FeignClient(value = "service-hi" ) 
		
	    接口方法
		@RequestMapping(value = "/hi",method = RequestMethod.GET)   //这里的hi 与服务端的hi对应
		String sayHiFromClientOne(@RequestParam(value = "name") String name); //这里的name 与服务端的name对应
  
---feign   使用 Ribbon 或 LoadBalancer
-- 
@SpringBootApplication
@EnableDiscoveryClient  
@EnableFeignClients(
	//basePackages= {"com.xx"}
)//增加


public class ServiceFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceFeignApplication.class, args);
	}
}

@FeignClient(value = "service-hi"   //同@LoadBalanced 服务名在这里
	url="http://127.0.0.1:8762/",//直接连接
//configuration= {config.FooConfiguration.class},//不能和@SpringBootApplication在同一包下
, fallback = SchedualServiceHiHystric.class //Hystrix后加的,调用服务全断   不会调用对应的错误实现类的方法???? 
//fallbackFactory=SchedualServiceHiFactory.class //方式二
)
public interface SchedualServiceHi {
	 
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
	
 
 	//对使用  configuration= {config.FooConfiguration.class},feign.Contract.Default
   // @RequestLine("GET /feignMVC/{owner}/") //参数加{} 
   //	String feignMVC(@Param("owner") String owner);//使用@Param ,service-hi的参数也是{}
   
   
}

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi { //Hystrix后加的,没用？？？
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}

//feign.hystrix.FallbackFactory
//org.springframework.cloud.openfeign.FallbackFactory 2020版本
@Component
public class SchedualServiceHiFactory  implements FallbackFactory<SchedualServiceHi>  {

	@Override
	public SchedualServiceHi create(Throwable cause) {
		return new SchedualServiceHi() {
		 @Override
		    public String sayHiFromClientOne(String name) {
		        return "sorry "+name;
		    }
			public String feignMVC(String owner) {
				 return "sorry feignMVC "+owner;
			}
		} ;
	}
}
@RestController
public class HiController {

    @Autowired
    SchedualServiceHi schedualServiceHi;
   
   @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualServiceHi.sayHiFromClientOne(name);
    }
	//对使用  configuration= {config.FooConfiguration.class},feign.Contract.Default
	@RequestLine("GET /feignMVC/{owner}/")
	String feignMVC(@Param("owner") String owner);
	
}
@Configuration
public class FooConfiguration
		implements RequestInterceptor  //feign传header
{
    @Bean
    public Contract feignContract() {
		//要使用 @RequestLine("GET /hi/{name}/")
        return new feign.Contract.Default();
    }
	
	/* 再加如下相配置，就可以有详细的openFeign请求返回日志  
	logging:
	  level:
       #feogn日志以什么级别监视那个接口
        com.xxFeignService: debug
	 */
	@Bean 
    feign.Logger.Level feignLoggerLevel(){
        return feign.Logger.Level.FULL;
    }
	
	
	 
	//打印 openfeign 返回json日志
	@Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;
    @Bean
    public Decoder feignDecoder() {
        return new MyRestfulLogGzipDecoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters))));
    }
	
	 
	@Override
	public void apply(RequestTemplate template) { 
		System.out.println("===openFeign 请求参数为："+ template.queryLine());//?param=123456
		//feign传header
	    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
	    //attributes 是null原因为openfeign 启用了hystrix,切换了Thread,导致threadLocal中的东西丢失，导致RequestContextHolder.getRequestAttributes()为null
	    //attributes 是null原因为openfeign 启用了 circuitbreaker, 使用了线程池，
	    //源码 初始化在FeignClientFactoryBean，最后实例化jdk代理handler类FeignCircuitBreakerInvocationHandler，当调用时会到FeignCircuitBreakerInvocationHandler -> Resilience4JCircuitBreaker
       
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                 String name = headerNames.nextElement();//Content-length 不能传
                if(name.equalsIgnoreCase("AuthenToken") )
                {
                    String values = request.getHeader(name);
                    template.header(name, values);//不会替换已经存在的，会增加重复的
                }
            }
			 //template.header("Accept-Encoding", "gzip"); //测试用
            System.out.println("feign interceptor header:"+template);
        }
	}
	
	//eureka用户名密码
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }
    //为某个feign禁用hystrix
//    @Bean
//	@Scope("prototype")
//	public Feign.Builder feignBuilder() {
//		return Feign.builder();
//	}

}
//打印 openfeign 返回json日志,如服务端返回gzip做解码
public final class MyRestfulLogGzipDecoder implements Decoder { 
    final Decoder delegate;

    public MyRestfulLogGzipDecoder(Decoder delegate) {
        Objects.requireNonNull(delegate, "Decoder must not be null. ");
        this.delegate = delegate;
    }


    /**
        feign.compression.response.enabled=true 如加了这个配置，可不用实现Decoder做gzip解码,但要求feign接口修改返回类型为ResponseEntity<byte[]>
		feign.compression.response.useGzipDecoder=true
     */
    @Override
    public Object decode(Response response, Type type) throws IOException {
        String resultStr = null;
		Collection<String> encodingStrings = (Collection)response.headers().get("content-encoding"); 
		if (null != encodingStrings && encodingStrings.contains("gzip")) {//对于部署到k8s上的情况
			resultStr = IOUtils.toString(new GZIPInputStream(response.body().asInputStream()), "utf-8");
			
		}else { 
			resultStr = IOUtils.toString(response.body().asInputStream(), "utf-8");//StandardCharsets.UTF_8
		}
		System.out.println("====openFeign response json :"+resultStr);
		// 回写body,因为response的流数据只能读一次，这里回写后重新生成response
		return delegate.decode(response.toBuilder().body(resultStr, StandardCharsets.UTF_8).build(), type);
    }
}


eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8765
spring:
  application:
    name: service-feign
	
feign: 
  client:
    config:
      default: #可是feigName
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: basic
 hystrix:
    enabled: false
    #enabled: true #会使用新线程，threadLocal丢失，RequestContextHolder.getRequestAttributes()为null,除非修改Hystrix使用SEMAPHORE
    
  circuitbreaker: #pom.xml要加spring-cloud-starter-circuitbreaker-resilience4j，可能要2020版本
    enabled: true 
	
#  httpclient:
#    enabled: true
#  okhttp:
#    enabled: false

  #compression:
    #response:
    #  enabled: true #如加了这个配置，可不用实现Decoder做gzip解码
    #request:
    #  enabled: true #如实现RequestInterceptor了再加这个可能没用
      	  
http://localhost:8765/hi?name=lisi  也是 port:8762 和 port:8763切换

 
feign的 max-connections 默认200

----config 分布式配置中心 
Git 好处可看历史版本(读无密码),也可从zookeeper,consul,svn,vault,filesystems,jdbc上读

--server 端
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>

import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		/* cd E:/tmp/git_config
		   git init (本地 file:/ 也可不做这步)
		   E:\tmp\git_config\repos\config-client-dev.yml  文件中有 foo: bar-dev
		    
		  http://127.0.0.1:8888/master/config-client-dev.yml  (已经是解密的,如不想 spring.cloud.config.server.encrypt.enabled=false) 即/{label}/{application}-{profile}.yml格式
		  http://127.0.0.1:8888/config-client/dev 返回一个json格式 即 /{application}/{profile}[/{label}]
		  */
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}

-application.properties 
spring.application.name=config-server
server.port=8888
 
#spring.cloud.config.server.git.uri=https://github.com/forezp/SpringcloudConfig/
#spring.cloud.config.server.git.searchPaths=respo
#spring.cloud.config.label=master
#spring.cloud.config.server.git.username=
#spring.cloud.config.server.git.password=

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: file:/E:/tmp/git_config 
          searchPaths: repos
      label: master

-如果使用eureka, 使用服务ID,而不使用IP(要先启动一个8889的eureka )
eureka.client.serviceUrl.defaultZone=http://localhost:8889/eureka/


#一定要写在bootstrap.yml中
#encrypt:
#  key: foo
  
#加密 （ 下载JDK8JCE） 默认有jdk1.8.0_161\jre\lib\security\policy\unlimited复制到上级目录下 
#curl localhost:8888/encrypt -d mysecret
#curl localhost:8888/decrypt -d xxxxxxxxxxxxxxxxx

#----如非对称加密
#keytool -genkeypair -alias mytestkey -keyalg RSA     -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US"   -keypass changeme -keystore server.jks -storepass letmein
#提示使用PKCS12
#keytool -importkeystore -srckeystore server.jks -destkeystore server.jks -deststoretype pkcs12
  
encrypt:
  keyStore:
    location: classpath:/server.jks
    password: letmein
    alias: mytestkey
    secret: changeme
#curl localhost:8888/encrypt -d mysecret 返回东西变的很多	

#如config服务端请求 http://127.0.0.1:8888/master/config-client-dev.yml 要密码，pom增加spring-boot-starter-security
  security: 
    user:
      name: user
      password: password	
	
测试 http://localhost:8888/foo/dev 有返回JSON表示可以从客户端取   {name}/{profile}

https://github.com/forezp/SpringcloudConfig/ 中 respo 目录下 又个文件config-client-dev.properties文件中有一个属性：
foo = foo version 3

文件名格式
/{application}/{profile}[/{label}]    			// /]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties


--client端
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>

@SpringBootApplication
@RestController
public class ConfigClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
	@Value("${foo}")
	String foo;
	@RequestMapping(value = "/hi")
	public String hi(){
		return foo;
	}
}

-bootstrap.yml
server:
  port: 8881

spring:
  application:
    name: config-client

#这部分配置要在bootrap.yml中有效，如application.yml中无效
  cloud:
    config:
      label: master
      profile: dev
      #uri: http://localhost:8888/ 
	  uri: http://user:password@localhost:8888/   #如config服务端要密码
      username: user #会覆盖uri中有用户名
      password: password
      #读文件格式 {application}-{profile}.properties/yml #对单仓库配置多个微服务 
 #多仓库

-如果使用eureka, 使用服务ID,而不使用IP(要先启动一个8889的eureka )
#spring.cloud.config.uri= http://localhost:8888/
eureka.client.serviceUrl.defaultZone=http://localhost:8889/eureka/
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
#-for config use eureka,use config-server  replace localhost:8888
#如config服务连接不上就启动失败
spring.cloud.config.fail-fast=true
#pom中可加 spring-retry 和 spring-boot-starter-aop 还可配置spring.cloud.config.retry.*

dev开发环境配置文件
test测试环境
pro正式环境


http://localhost:8881/hi  返回  foo version 3

http://localhost:8889/ 显示有两个config server

---bus config client端
在标有 @EnableEurekaClient 的类上再增加一个  @RefreshScope

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-kafka</artifactId>
</dependency>
 
 报 rabbit,kafka, and no default binder has been set 是因为同时配了两个, 如spring-cloud-stream-binder-rabbitmq.jar中有  META-INF/spring.binder
用下面配置也不行 
#spring.cloud.stream.bindings.input.binder=kafka
spring.cloud.stream.bindings.output.binder=rabbit
只能去掉一个
spring.cloud.bus.enabled=true
spring.cloud.bus.trace.enabled=true  #can use  /trace
management.endpoints.web.exposure.include=bus-refresh

多的配置 spring boot
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
#建用户zh，还要在管理界面中分配用户可Can access virtual hosts /
spring.rabbitmq.username=zh
spring.rabbitmq.password=123
#management.security.enabled=false

 POST 请求 (Firefox的 RESTClient) 
		 http://localhost:8881/actuator/bus-refresh( Content-Type : application/json) 
						  也可部分刷新加参数如 /bus-refresh/customers:9000  (service ID) 或  /bus-env/customers:** 
(config client端) 如更改Git配置,不用重启服务这样也能刷新 (MQ 广播配置文件的更改) 

----Sleuth  足迹，警犬，侦探 
Spring Cloud Sleuth 可以结合 Zipkin，将信息发送到 Zipkin



---service-hi项目
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>

 
	
import brave.sampler.Sampler; //brave-5.4.3.jar	
@Bean
public Sampler defaultSampler() {
	return Sampler.ALWAYS_SAMPLE;
}
//配置 spring.sleuth.sampler.probability (默认是0.1 是10%). 
 

@Autowired
private RestTemplate restTemplate;

@Bean
public RestTemplate getRestTemplate(){
	return new RestTemplate();
}

@RequestMapping("/hi")
public String callHome(){
	LOG.log(Level.INFO, "calling trace service-hi  ");
	return restTemplate.getForObject("http://localhost:8989/miya", String.class);
}
@RequestMapping("/info")
public String info(){
	LOG.log(Level.INFO, "calling trace service-hi ");

	return "i'm service-hi";

}
---application.properties
server.port=8988
spring.application.name=service-hi


#Finchley version
spring.zipkin.baseUrl=http://localhost:9411	
#对于zipkin服务加了spring.security.basic.enabled=true和spring.security.user=user
#spring.zipkin.baseUrl=http://user:pass@localhost:9411	

#1.0 链路数据100%收集到zipkin-server,default value 0.1 
spring.sleuth.sampler.probability=1.0 
spring.sleuth.web.client.enabled=true

---service-miya 项目 为了两个项目有相互调用,配置和maven是一样的
@RequestMapping("/hi")
public String home(){
	LOG.log(Level.INFO, "hi is being called");
	return "hi i'm miya!";
}

@RequestMapping("/miya")
public String info(){
	LOG.log(Level.INFO, "info is being called");
	return restTemplate.getForObject("http://localhost:8988/info",String.class);
}

@Autowired
private RestTemplate restTemplate;

@Bean
public RestTemplate getRestTemplate(){
	return new RestTemplate();
}

server.port=8989 
#Finchley version
spring.zipkin.baseUrl=http://localhost:9411
spring.application.name=service-miya
 
启动后 http://localhost:9411/ 有界面
 请求 http://localhost:8989/miya    后  Find Traces按钮有显示， 可点请求历史的标题，看详情，可点Dependencies标签
 请求 http://localhost:8988/hi  后 Find Traces按钮有显示， 
 
-----eureka-server 冗余来增加可靠性，当有一台服务器宕机了，服务并不会终止，因为另一台服务存有相同的数据
echo 127.0.0.1 peer1 >>  C:\Windows\System32\drivers\etc\hosts
echo 127.0.0.1 peer2 >>  C:\Windows\System32\drivers\etc\hosts
java EurekaServerApplication --spring.profiles.active=peer1
java EurekaServerApplication --spring.profiles.active=peer2
两个服务可同时启动，相互依赖,启动中间有连接错误可忽略

http://127.0.0.1:8761
http://127.0.0.1:8769  
每个页面中 registered-replicas列 和  available-replicas列 是对方机器的

---application-peer1.yml
	
server:
  port: 8761

spring:
  profiles: peer1
eureka:
  instance:
    hostname: peer1
  client:
    serviceUrl:
      defaultZone: http://peer2:8769/eureka/
--application-peer2.yml
	
server:
  port: 8769

spring:
  profiles: peer2
eureka:
  instance:
    hostname: peer2
  client:
    serviceUrl:
      defaultZone: http://peer1:8761/eureka/

--客户端连接用 
eureka:
  client:
    serviceUrl:
      defaultZone: http://peer2:8769/eureka/,http://peer1:8761/eureka/
 
-----Docker 
有单独的Docker.txt
windows 下要
net user docker  /add /active:yes /expires:never /passwordchg:yes /fullname:"the-docker" /comment:"docker used"
net localgroup "docker-users" docker /add

windows CE Stable 版本 要启用Hyper-V,win10自带(官方说win10家庭版没有Hyper-V,),但要启用,像IIS一样启用 ,在开始-> windows Accessories->Administrative Tools->Hyper-V Manager
自动建立了名为 MobyLinuxVM ,目录在 C:\Users\Public\Documents\Hyper-V\

 docker --version  				Docker version 17.03.1-ce, build c6d412e
 docker-compose --version		docker-compose version 1.11.2, build f963d76f
 docker-machine --version  		docker-machine version 0.10.0, build 76ed2a6
 
  
命令 docker ps , docker version,   docker info

右击右下角的图标->settings...->General 标签中 复选 Expose Deamon on tcp://localhost:2375 without TLS

Dockerfile 文件

FROM frolvlad/alpine-oraclejdk8:slim					//使用哪个镜像源	:后是个标签名 格式 FROM <image> 或  FROM <image>:<tag>,docker images可看到, 如不指定标签默认使用latest镜像
VOLUME /tmp   											//目录具有持久化存储数据的功能，
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar			//复制
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]			//容器启动时执行的命令
EXPOSE 8761 											//容器设置对外的端口号


</build>中加
	<plugin>
		<groupId>com.spotify</groupId>
		<artifactId>docker-maven-plugin</artifactId>
		<version>0.4.3</version>
		<configuration>
			<imageName>forezp/${project.artifactId}</imageName>
			<dockerDirectory>src/main/docker</dockerDirectory>
			 <!-- github官方推荐使用dockerfile-maven
				CentOS 7 
				打开/usr/lib/systemd/system/docker.service文件，修改ExecStart这行 
				ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
				(原来没-H参数，启动后就监听2375端口)
			  -->
			<dockerHost>http://172.16.35.35:2375</dockerHost>
			
			<resources>
				<resource>
					<targetPath>/</targetPath>
					<directory>${project.build.directory}</directory>
					<include>${project.build.finalName}.jar</include>
				</resource>
			</resources>
		</configuration>
	</plugin>
mvn package docker:build  构建docker镜像,会仿问2375端, 要等一会才行


docker run -p 8761:8761 --name my_name -t forezp/eureka-server   看  localhost:8761	//-p :前部分是主机端口,也可加IP,即 -t 127.0.0.1:8760:8761

docker ps 		显示 0.0.0.0:8761 表示主机端口 ->8761/tcp 表示容器端口 ,  NAMES 列的值是容器名
docker top  <NAMES/CONTAINER ID列的值>     能看到完整执行命令
docker logs -f <NAMES/CONTAINER ID列的值>  	
docker stop  <NAMES/CONTAINER ID列的值>
docker ps -l  最近一次创建的容器(停止的也可看到)
docker start <NAMES/CONTAINER ID列的值>
docker restart <NAMES/CONTAINER ID列的值>
docker rm  <NAMES/CONTAINER ID列的值>
docker rm -f  <NAMES/CONTAINER ID列的值> 停止加删除
docker port   <NAMES/CONTAINER ID列的值>   

docker inspect <NAMES/CONTAINER ID列的值>     容器信息


docker images  查看所有镜像 
docker pull <镜像名>:<标签>   事先下载镜像,默认执行时没有才下载
docker search httpd  在线搜索镜像名

docker tag <image_id> <镜像源名(repository name) : 新的标签名(tag)>   //为image_id加一个新的标签,一个image_id可以有多个标签,image_id是 docker images中的

docker port  <NAMES/列的值>  <内部端口8761>			//返回主机端口



当前目录中有 docker-compose.yml , 
version: '3'
services:
  eureka-server:
    image: forezp/eureka-server
    restart: always
    ports:
      - 8761:8761

  service-hi:
    image: forezp/service-hi
    restart: always
    ports:
      - 8763:8763
可用 docker-compose up 一次启动两个 ,直接ctrl+c两个全停了


使用 Dockerfile 文件 和  docker build <Dockerfile所在目录> 命令 创建一个新的镜像

更新镜像后
 docker commit -m="描述信息" -a="作者" <容器ID> <要创建的目标镜像名>
 

---consul
使用go语言开发
分布式系统的服务注册和发现、配置等
服务健康监测
key/value 存储

https://www.consul.io/downloads.html 
下载 win zip包,就一个consul命令
 https://github.com/hashicorp/consul/ 
consul agent -dev 启动
http://localhost:8500

---


<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>

application.yml 文件

spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:	
		#healthCheckPath: /${management.contextPath}/health    //变量, 
        healthCheckPath: /consul-miya/health					//有404错误???
		
        healthCheckInterval: 15s
        instance-id: consul-miya
  application:
    name: consul-miya
server:
  port: 8502
------------------consul-config
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-consul-config</artifactId>
</dependency>

@RestController
@RefreshScope //当在consul界面中有配置修改了，这个值立即被刷新
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;
    // http://127.0.0.1:8081/yaml
    @GetMapping("/yaml")
    public String getConfigInfo() {
        return configInfo;
    }
}

----bootstrap.yml
spring:
  application:
    name: configConsulApp
  profiles:
    active: dev 
  cloud:
    consul:
      host: localhost
      port: 8500
      config: 
        enabled: true 
        prefix: root_config #所有key的前缀，显示为目录
        defaultContext: apps #默认值 application
        profileSeparator: '-'  
        format: YAML
        data-key: data #如为yaml一定设置这个
        #consul kv put root_config/apps-dev/data   config:info:myval  进入界面修改类型为yaml
  
  
   


-----------------gateway 
gateway 很好的支持异步，而zuul仅支持同步,gateway对比zuul多依赖了spring-webflux

lb://  协议 

 <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class Application {
	
	// 请求 http://127.0.0.1:9999/get 转到  http://httpbin.org/get 返回的数据有Hello:world
	//curl --dump-header - --header 'Host:www.hystrix.com' http://localhost:9999/delay/3   结果显示fallback
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	//编码方式
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        String httpUri = uriConfiguration.getHttpbin();
        return builder.routes()
            .route(p -> p
                .path("/get")
                .filters(f -> f.addRequestHeader("Hello", "World"))
                .uri(httpUri))//可用lb://
            .route(p -> p
                .host("*.hystrix.com")
                .filters(f -> f
                    .hystrix(config -> config
                        .setName("mycmd")
                        .setFallbackUri("forward:/fallback")))
                .uri(httpUri))
            .build();
    } 
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
  
}
 
@ConfigurationProperties
class UriConfiguration {
    
    private String httpbin = "http://httpbin.org:80";

    public String getHttpbin() {
        return httpbin;
    }

    public void setHttpbin(String httpbin) {
        this.httpbin = httpbin;
    }
}
--
server:
  port: 9999
---spring  cloud test  WireMock


<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-contract-stub-runner</artifactId>
	<exclusions>
		<exclusion>
			<artifactId>spring-boot-starter-web</artifactId>
			<groupId>org.springframework.boot</groupId>
		</exclusion>
	</exclusions>
</dependency>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {"httpbin=http://localhost:${wiremock.server.port}"})
@AutoConfigureWireMock(port = 0)
public class ApplicationTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void contextLoads() throws Exception {
		//Stubs
		stubFor(get(urlEqualTo("/get"))
				.willReturn(aResponse()
					.withBody("{\"headers\":{\"Hello\":\"World\"}}")
					.withHeader("Content-Type", "application/json")));
		stubFor(get(urlEqualTo("/delay/3"))
			.willReturn(aResponse()
				.withBody("no fallback")
				.withFixedDelay(3000)));

		webClient
			.get().uri("/get")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.headers.Hello").isEqualTo("World");

		webClient
			.get().uri("/delay/3")
			.header("Host", "www.hystrix.com")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				response -> assertThat(response.getResponseBody()).isEqualTo("fallback".getBytes()));
	}
}
  
  
 --- predicate  断言，它决定了请求会被路由到哪个router 中
spring:
  cloud:
    gateway:
      routes:
#      - id: after_route
#        uri: http://httpbin.org:80
#        predicates:
#        - After=2017-01-20T17:42:47.789+08:00[Asia/Shanghai]

# After = org.springframework.cloud.gateway.handler.predicate.AfterRoutePredicateFactory
# 日期格式是 java.time.ZonedDateTime.parse(str)可解析的
# 当请求的时间在这个配置的时间之后，请求会被路由到 http://httpbin.org:80
#测试 http://localhost:9999/get 跳到  http://httpbin.org:80/get
 
  
  
      - id: header_route
        uri: http://httpbin.org
        predicates:
        - Header=X-Request-Id, \d+
#当请求的Header中有X-Request-Id的header名，且header值为数字时，请求会被路由到配置的 uri. 
#测试用 curl -H 'X-Request-Id:1' localhost:9999/get

      - id: cookie_route
        uri: http://httpbin.org
        predicates:
        - Cookie=chocolate, ch.p
        
#请求带有cookie名为 chocolate, cookie值为ch.p 的请求将都会转发到uri       
#测试用 curl -H 'Cookie:chocolate=ch.p' localhost:9999/get 


      - id: host_route
        uri: http://httpbin.org
        predicates:
        - Host=**.somehost.org
#请求头中含有Host为www.somehost.org 或 beta.somehost.org 的请求将会被路由转发转发到配置的uri
#测试用     curl -H 'Host:www.somehost.org' localhost:9999/get  


#      - id: method_route
#        uri: http://httpbin.org
#        predicates:
#        - Method=GET
#所有的 GET 类型的请求都会路由转发到配置的uri     
#测试用    curl localhost:9999/get 如 POST用   curl -XPOST localhost:9999/get


      - id: host_route
        uri: http://example.org
        predicates:
        - Path=/foo/{segment}
#请求路径满足/foo/{segment}的请求将会匹配并被路由，比如/foo/1 、/foo/bar的请求
#测试用  curl localhost:9999/foo/bar

      - id: query_route
        uri: http://example.org
        predicates:
         - Query=foo, ba.
        
#请求中含有参数foo，并且foo的值匹配ba. 正则表达式
#测试用  curl localhost:9999?foo=bar

 

#----filters
      - id: add_request_header_route
        uri: http://httpbin.org
        predicates:
        - Before=2019-01-20T17:42:47.789+08:00[Asia/Shanghai]
        filters:
        - AddRequestHeader=X-Request-Foo, Bar
#AddRequestHeader=org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory
#会在请求头加上一对请求头，名称为X-Request-Foo，值为Bar
#测试用  curl localhost:9999/get (使用 filters 一定要有 predicates)
  

      - id: rewritepath_route
        uri: https://blog.csdn.net
        predicates:
        - Path=/foo1/**			#*/
        #- Path=/foo1/{segment}
        filters:
        - RewritePath=/foo1/(?<segment>.*),/$\{segment}
# yaml 语法  $\ 转换为 $ 
- 表示列表中的元素

#请求 localhost:9999/foo1/forezp ，此时会将请求转发到 https://blog.csdn.net/forezp 的页面
#不能有两个predicates 都符合  测试要关闭前的filter (add_request_header_route)


      - id: mycustome_filter
        uri: http://httpbin.org
        predicates:
         - Host=**.myCustHost.org
        filters:
        - RequestTime=true #自己的类
#自定义filterFactory，可配置使用 
#测试用     curl -H 'Host:www.myCustHost.org' localhost:9999/get  

 /*
自定义filterFactory   可用配置文件
    filters:
    - RequestTime=false
*/
@Bean
public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
return new RequestTimeGatewayFilterFactory();
} 



@Bean  //自定义  Filter   要编码
public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
	return builder.routes()
		.route(r ->
	 		r.host("*.hystrix.com") //相当于 predicates 测试用 curl --dump-header - --header 'Host:www.hystrix.com' http://localhost:9999/get 
			//r.path("/customer/**") //相当于 predicates 测试用 curl localhost:9999/customer/123  会请求到http://httpbin.org/customer/123肯定不存在，但日志有了

				.filters(f -> f.filter(new RequestTimeFilter())//自己的filter
						.addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
				.uri("http://httpbin.org:80")
				.order(0)
				.id("customer_filter_router")
		)
		.build();
}

// 自定义Filter 要实现GatewayFilter和Ordered  2个接口
public class RequestTimeFilter implements GatewayFilter, Ordered {
    private static final Log log = LogFactory.getLog(RequestTimeFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());//"pre"类型的过滤器
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);//"post"过滤器
                    if (startTime != null) {
                        log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );
    }
    @Override
    public int getOrder() {
        return 0; //值越大则优先级越低
    }
}


 
#spring.cloud.gateway.default-filters #GatewayFilter作用在所有路由上


//自定义 GlobalFilter 不需要在配置文件中配置，作用在所有的路由上
//curl --dump-header - localhost:9999/get
@Bean
public TokenFilter tokenFilter(){
	return new TokenFilter();
}

public class TokenFilter implements GlobalFilter, Ordered {

    Logger logger=LoggerFactory.getLogger( TokenFilter.class );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null || token.isEmpty()) {
            logger.info( "token is empty..." );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return -100;
    }
}


限流
计数器算法
1s 内的前10ms，已经通过了100个请求，那后面的990ms，只能眼巴巴的把请求拒绝，我们把这种现象称为“突刺现象”

漏桶算法

令牌桶算法


Redis和lua脚本实现了令牌桶的方式
 spring-cloud-gateway-core-2.0.2.RELEASE.jar/META-INF/scripts/request_rate_limiter.lua 
<!--  Redis RateLimiter  -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>



--gateway eureka

spring:
  application:
    name: sc-gateway-server
  cloud:
    gateway:
      discovery:
        locator:
          enabled: false                 #为true，表明gateway开启服务注册和发现的功能，false使用下面的routes配置
          lowerCaseServiceId: true       #是将请求路径上的服务名配置为小写
      routes:
      - id: service-hi
        uri: lb://SERVICE-HI           #lb://SERVICE-HI即service-hi服务的负载均衡地址
        predicates:
          - Path=/demo/**     # */ .discovery.locator.enabled 改为false 就可 localhost:8081/demo/hi?name=1323  
        filters:
          - StripPrefix=1       #自带的，去除第1个，转发之前将/demo去掉
          - RequestTime=true      #自定义FilterFactory





---
 
两个上下文共享一个Environment
Bootstrap属性的优先级高，因此默认情况下不能被本地配置覆盖

bootstrap context  用 bootstrap.yml 要先于 application.yml 被加载
application context 用 application.yml  (或者 .properties)



bootstrap.yml
spring:
  application:
    name: foo
  cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888}
	  
	  
这样会更好,设置spring.application.name（在bootstrap.yml或application.yml）

spring.cloud.bootstrap.enabled=false（例如在系统属性中）来完全禁用bootstrap 过程。

SpringApplication 或者 SpringApplicationBuilder

org.springframework.core.env.CompositePropertySource  优先级会高于  org.springframework.cloud.bootstrap.config.PropertySourceLocator



import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

@Configuration
public class CustomPropertySourceLocator implements PropertySourceLocator {
	org.springframework.core.env.CompositePropertySource x; 
	org.springframework.boot.builder.SpringApplicationBuilder x1;
	org.springframework.cloud.bootstrap.BootstrapConfiguration i;
    @Override
    public PropertySource<?> locate(Environment environment) {
        return new MapPropertySource("customProperty",
                Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));
    }
}

.jar包/META-INF/spring.factories中加入
org.springframework.cloud.bootstrap.BootstrapConfiguration=sample.custom.CustomPropertySourceLocator

-------------spring cloud zookeeper  
spring-cloud-starter-zookeeper-all 中已经包含 spring-cloud-starter-zookeeper-discovery , spring-cloud-starter-zookeeper-config  

maven
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-all</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.12</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>

gradle
compile('org.springframework.cloud:spring-cloud-starter-zookeeper-all') {
  exclude group: 'org.apache.zookeeper', module: 'zookeeper'
}
compile('org.apache.zookeeper:zookeeper:3.4.12') {
  exclude group: 'org.slf4j', module: 'slf4j-log4j12'
}
----spring cloud  zookeeper 做服务发现 
服务端和客户端都要 增加依赖  spring-cloud-starter-zookeeper-discovery
如web项目要 spring-boot-starter-web 

@EnableDiscoveryClient


--.yaml文件
server:
#  port: 8081
   port: 8082
   
spring:
  application:
    name: testZookeeperApp 
  
  #服务端和客户端都要   
  cloud:
    zookeeper:
      connect-string: localhost:2181
		discovery
 			enabled:true
 		  


@Value("${spring.application.name}")
private String instanceName;

@Autowired
private DiscoveryClient discoveryClient;

//请求  http://localhost:8081/services 显示有多个服务注册上去
 @GetMapping("/services")
 public List<String> serviceUrl() {
     List<ServiceInstance> list = discoveryClient.getInstances(instanceName); 
     List<String> services = new ArrayList<>();
     if (list != null && list.size() > 0 ) {
         list.forEach(serviceInstance -> {
             services.add(serviceInstance.getUri().toString());
         });
     }
     return services;
 }
zookeeper 下   /services/testZookeeperApp 启动两个服务就有两个记录

测试成功  如服务变化，(如增加一个节点)，客户端要等一会才会做负载




-------------spring cloud zookeeper 分布式配置
启用分布式配置   增加依赖 org.springframework.cloud:spring-cloud-starter-zookeeper-config
 
应用名为 testApp 和 名为 dev 的profile

config/testApp,dev
config/testApp
config/application,dev
config/application


---- bootstrap.yml 不能是 application.yml
spring:
  application:
    name: testZookeeperApp
  profiles:
    active: dev 
    
  cloud:
    zookeeper:
    	connect-string: localhost:2181
      config:
        enabled: true
        #root: configuration # 默认值为 config
        defaultContext: apps  
        #profileSeparator: '::' # 默认值为,
        
    

默认存在 /config  名称空间(目录)下 ，root:来修改
默认profileSeparator 是,

日志提示{name='zookeeper', propertySources=[ZookeeperPropertySource {name='config/testZookeeperApp,dev'}
/*
	 zkCli.sh 中
	 > create /config
	 > create /config/testZookeeperApp,dev
	 > create /config/testZookeeperApp,dev/zkPass 123 
	 
    > delete  /config/testZookeeperApp,dev/zkPass 
	 > create /config/testZookeeperApp,dev/zkPass  123  digest:myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=:crwda
	 # myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=是使用DigestAuthenticationProvider.generateDigest("myuser:mypass")生成的
	
	 > set  /config/testZookeeperApp,dev/zkPass 456 
	 */
	@Value("${zkPass}")
	String password;
	
	//请求  http://localhost:8081/zkConfig 测试成功
	@RequestMapping("/zkConfig")
	public String hi() {
		return "password="+password+" from "+port +",name="+instanceName;
	}
/*	
resources/META-INF/spring.factories
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
my.project.CustomCuratorFrameworkConfig
*/
@BootstrapConfiguration
public class CustomCuratorFrameworkConfig {
  @Bean  // 设置auth  密码测试成功
  public CuratorFramework curatorFramework() {
	    String ip ="127.0.0.1";
		String ipPort="127.0.0.1:2181";//可读配置
//		RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);//baseSleepTimeMs,  maxRetries 每次重试时间逐渐增加
//		RetryPolicy retryPolicy=new RetryNTimes(5,1000);//retryCount 最大重试次数，elapsedTimeMs
		RetryPolicy retryPolicy=new RetryUntilElapsed(5000,1000);//maxElapsedTimeMs最多重试多长时间,   sleepMsBetweenRetries 每次重试时间间隔
//		CuratorFramework client=CuratorFrameworkFactory.newClient(ipPort,500,5000, retryPolicy);
		
		List<AuthInfo> authInfos =new ArrayList<>();
		AuthInfo auth=new AuthInfo("digest", "myuser:mypass".getBytes());
		authInfos.add(auth);
		
		CuratorFramework client= CuratorFrameworkFactory.builder().connectString(ipPort)
		.sessionTimeoutMs(5000)
		.connectionTimeoutMs(5000)
//		.authorization("digest", "myuser:mypass".getBytes()) //同命令  addauth digest  myuser:mypass
		.authorization(authInfos)
		.retryPolicy(retryPolicy)
		.build();
		client.start();
		return client;
  }
  
POST 请求 /refresh  路径 来刷新，自动刷新（Zookeeper）还未实现。 测试没有这个路径？？？
  

---------Spring Cloud Circuit Breaker  
 Hoxton版本 开始用 Resilience4J  替代老的 hystrix 

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>

spring.cloud.circuitbreaker.resilience4j.enabled: true
 
@Service
public  class DemoControllerService {
    private RestTemplate rest;
    private CircuitBreakerFactory cbFactory;//实现类是 Resilience4J
    public DemoControllerService(RestTemplate rest, CircuitBreakerFactory cbFactory) {
        this.rest = rest;
        this.cbFactory = cbFactory;
    }
    public String hi(String name) {
    	//debug实现类是 Resilience4J
        return cbFactory.create("myid")
        		.run(
        			() -> rest.getForObject("http://"+MyController.remoteServiceName+"/hi?name="+name, String.class), 
        			throwable -> { throwable.printStackTrace(); return "fallback with Resilience4J"; }
        		 );
    }
}

----------------loadbalancer 
Client-Side Load-Balancing 
Hoxton版本开始 Ribbon变维护模式 使用 loadbalancer 

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
spring.cloud.loadbalancer.ribbon.enabled: false 或者 maven去除spring-cloud-starter-netflix-ribbon
 
原来的ribbon替换为 loadbalancer 使用方式是一样的
@Bean
@LoadBalanced //表明这个restRemplate开启负载均衡的功能
RestTemplate restTemplate() {
	return new RestTemplate();
}
----------------



--------------Spring Cloud Kubernetes
为方便spring-boot,或spring-cloud 运行在kubernetes中
文档中有提到Istio

使用io.fabric8组下的kubernetes-client 库连接kubernetes 这不是官方的



<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-kubernetes</artifactId>
</dependency>  @EnableDiscoveryClient 的实现

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-kubernetes-config</artifactId>
</dependency>  为ConfigMaps 和 Secrets及热加载

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-kubernetes-all</artifactId>
</dependency>
使用okhttp3


---Dockerfile
#FROM tomcat:9.0.34-jdk11  #源码是从 FROM openjdk:11-jdk (/usr/local/openjdk-11版本是11.0.7,/usr/local/tomcat)
FROM openjdk:11-jdk
RUN mkdir -p /app  /tmp/logs/
WORKDIR /app
COPY target/cloud-k8s.jar /app/
#ADD target/${JAR_FILE} /app/myservice.jar

VOLUME ["/tmp/logs/"]
EXPOSE 8081
CMD ["--spring.profiles.active=dev"]
ENTRYPOINT ["java","-jar","cloud-k8s.jar"]
---
docker build -t cloud-k8s:0.1 .

运行
docker container ls -a  显示用过的名字

docker run  -p 8080:8081 -v ~/logs:/tmp/logs -d --name my-cloud-k8s  cloud-k8s:0.1 
  -p 8080:8081  			#-p 本机端口:docker端口
  -v ~/logs:/tmp/logs 	#-v 本机目录:docker目录
  -d 后台运行
  
docker run --name my-cloud-k8s cloud-k8s:0.1
docker exec -it my-cloud-k8s bash
docker logs -f my-cloud-k8s
docker container start my-cloud-k8s
#docker container rm  my-cloud-k8s


--- vi k8s_cloud-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cloud-service
  namespace: my-ns
  labels:
    app: cloud-service
spec:
  replicas: 2
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 25%
  selector:
    matchLabels:
      app: cloud-service
  template:
    metadata:
      labels:
        app: cloud-service
    spec:
      containers:
        - name: cloud-service
          image:  centos71.com/library/cloud-service:1.0
          
          imagePullPolicy: Always
          #imagePullPolicy: IfNotPresent
         #env:
          ports:
            - containerPort: 8081
          readinessProbe:
            tcpSocket:
              port: 8081
            initialDelaySeconds: 30
            periodSeconds: 10
          volumeMounts:
            - mountPath: /app/tmp
              name: tmp-volume
      volumes:
        - name: tmp-volume
          emptyDir: { }
      #imagePullSecrets:
      #  - name: xxx

---
kubectl apply -f k8s_cloud-deployment.yml

---k8s_cloud-service.yml
apiVersion: v1
kind: Service
metadata:
  name: myk8s-app
  #namespace: my-ns
spec: 
   selector:  
    app: cloud-service 
   #clusterIP: 172.21.5.97 #可不指定动态分配，指定容易冲突
   type: ClusterIP
   ports: 
   - port: 9000 
     targetPort: 8081  

//@RefreshScope 或 @ConfigurationProperties  configmap修改时，会自动触发重启刷新( mode: event,strategy: restart_context) 

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

---application.yml
spring: 
  cloud:
    kubernetes:
      reload:
        enabled: true #--默认关闭 
        #strategy: refresh #--默认refresh 对 @ConfigurationProperties 或  @RefreshScope的做刷新
        strategy: restart_context
        monitoring-config-maps: true
        monitoring-secrets: true
        mode: event
        
#如 strategy: restart_context 要加如下 
management:
  endpoint:
    restart:
      enabled: true
  endpoints:
    web:
      exposure:
        include: restart 
---bootstrap.yml
spring:
  application:
    name: kubernetesApp
  profiles:
    active: dev
 
  cloud: 
    loadbalancer:
       ribbon:
         enabled: false 
    kubernetes: #k8s config可以覆盖spring boot的application.yml中的配置
      config:
        enabled: true  #--bootstrap
        name:  my-cloud-config  #--bootstrap  
        #(默认就是${spring.application.name},找kubernetes中哪个configMap,即kind: ConfigMap中对应有名为这个为metadata级下的name的值 
        namespace: my-ns
         
        #sources: #这里name和namespace可覆盖config下name和namespace
        #- namespace: n3 
        #  name: c3  
        
      secrets:
        enabled: true
        enable-api: true
        name: my-cloud-secret
        namespace: my-ns
        
        
      discovery:
        all-namespaces: true
        service-name: ${spring.application.name}
      loadbalancer:
        mode: SERVICE
     
		
		
--kubernetes-gateway项目
--bootstrap.yml
spring: 
  cloud: 
    kubernetes:  
      discovery:
        all-namespaces: true
        service-name: ${spring.application.name}
      loadbalancer:
        mode: SERVICE
---application.yml

spring:
  application:
    name: gateway-k8s
  cloud:
    gateway:
      discovery:
        #enabled: false #表示禁用 DiscoveryClient
        locator:
          enabled: false                 #为true，表明gateway开启服务注册和发现的功能，false使用下面的routes配置
          lowerCaseServiceId: true       #是将请求路径上的服务名配置为小写
      routes:
      - id: kubernetes-app
        #uri: lb://myk8s-app:9000    #是建服务的端口,在k8s上service为clusterIP 用lb不行，
        uri: http://myk8s-app:9000
        predicates:
          - Path=/remote/**     #.discovery.locator.enabled 改为false 就可  
        filters:
          - StripPrefix=1       #自带的，去除第1个，转发之前将/demo去掉

 
----------oauth2
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.1.5.RELEASE</version>
</dependency>




日志显示 默认default cache，可使用 Caffeine



