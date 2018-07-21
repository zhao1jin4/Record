
========================Spring Cloud

Intellij Idea 建立 spring initialir 项目->Cloud Discory -> eureka server  会自动生成pom.xml



版本名是伦敦地铁站的名字，字母表的顺序
最新的 Dalston  版本 

https://springcloud.cc/spring-cloud-dalston.html

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.2.RELEASE</version>
</parent>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Dalston.SR1</version>  
			<!-- Dalston.SR4    要和spring-boot-starter-parent  1.5.2.RELEASE 对应
				Finchley.RELEASE  要和spring-boot-starter-parent 2.0.1.RELEASE 对应 
			-->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
		<!--  如果不想使用<parent>标签时,用这个
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>1.5.2.RELEASE</version>
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
    
	
	
	<!-- Finchley.RELEASE -->
	<dependency> 
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
	<!-- Finchley.RELEASE -->
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
	
	
	<!--  Dalston.SR4 自动依赖 spring-cloud-starter-eureka-client   -->
	<dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId> 
	</dependency>
	
	<!--  Dalston.SR4 自动依赖  spring-cloud-netflix-eureka-server  -->
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka-server</artifactId> 
	</dependency>
	
		
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-hystrix</artifactId>
	</dependency> 
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
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


Spring Cloud Config
Spring Cloud Netflix 		第三方的Netflix OSS 开源的 集成
Spring Cloud Bus  			使用 RabbitMQ 或Kafka
Spring Cloud for Cloud Foundry
	Spring Cloud Cloud Foundry Service Broker
Spring Cloud Cluster
Spring Cloud Consul
Spring Cloud Security		使用OAuth2
Spring Cloud Sleuth			第三方的分布式跟踪解决方案
Spring Cloud Stream		 	为Kafka和Rabbit MQ提供Binder实,基于 Spring Integration
	Spring Cloud Stream App Starters
Spring Cloud Task
	Spring Cloud Task App Starters
Spring Cloud Zookeeper
Spring Cloud for Amazon Web Services
Spring Cloud Connectors
Spring Cloud CLI
Spring Cloud Contract
一共15个子项目   加单独的 Spring Cloud Data Flow
 	
	
---Eureka server 
https://github.com/Netflix/Eureka

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
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
	  
//通过eureka.client.registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server.	  
运行后可以仿问 http://localhost:8761  有界面  ，是No instances available

--Eureka client(server)

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient  //相当于  dubbo provider 
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

}

文件 bootstrap.yml

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8762
  #port: 8763
spring:
  application:
    name: service-hi

启动后	服务端localhost:8761  就有名为 service-hi 的Instances 
测试
http://localhost:8762/hi?name=forezp

-- ribbon+restTemplate 
-- Netflix开源了Hystrix组件, 断路器模式

客户端(服务端) 改个端口到 8763 再启动一个  service-hi 就有两个服务了

再建立一个客户端   Client Side Load Balancing (Ribbon)
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
</dependency>

@SpringBootApplication
@EnableDiscoveryClient //(spring-cloud-starter-ribbon)  //向服务中心注册 ribbon  和 @LoadBalanced 一起用,  客户端都可做负载均衡,相当于 dubbo consumeer

@EnableHystrix //断路器(如果几台中一台服务不可用,就把这台服务器隔离了,就仿问不到这台机器了) Hystrix后加的
@EnableHystrixDashboard // 就可以仿问 http://localhost:8764/hystrix 
//maven加spring-boot-starter-actuator ,http://localhost:8764/hystrix 文本框中输入 http://localhost:8764/hystrix.stream ,2000ms,Monitor Stream 会loading
//在另一个窗口 http://localhost:8764/hi?name=forezp 时,上一个窗口有图表
public class ServiceRibbonApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceRibbonApplication.class, args);
	}
	@Bean
	@LoadBalanced //表明这个restRemplate开启负载均衡的功能
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
	
	 @HystrixCommand(fallbackMethod = "hiError")//Hystrix后加的
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class);  //这里直接SERVICE-HI用名字来连, 用在方法级别
    }
	
	//Hystrix后加的
    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }
}
@RestController
public class HelloControler {
    @Autowired
    HelloService helloService;
    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name){
        return helloService.hiService(name);
    }
}
--application.yml文件
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8764
spring:
  application:
    name: service-ribbon
	
测试  http://localhost:8764/hi?name=forezp 	  发现间隔调用 8762 和 8763 ,因加了 @LoadBalanced
   
---客户端一ribbon  用在方法级别 
  @EnableDiscoveryClient  (spring-cloud-starter-ribbon)
		  
		  @LoadBalanced 放在 new RestTemplate()上  
		  方法中 restTemplate.getForObject("http://SERVICE-HI/hi")
		  

---客户端二feign  用在类别级 使用ribbon 即也用 EnableDiscoveryClient
  @EnableDiscoveryClient
  @EnableFeignClients    (spring-cloud-starter-feign)  

	  接口类
	  @FeignClient(value = "service-hi" ) 
		
	    接口方法
		@RequestMapping(value = "/hi",method = RequestMethod.GET)   //这里的hi 与服务端的hi对应
		String sayHiFromClientOne(@RequestParam(value = "name") String name); //这里的name 与服务端的name对应
  
---feign   如不使用ribbon+restTemplate,就使用feign,其实Feign已经使用Ribbon
-- 
@SpringBootApplication
@EnableDiscoveryClient  
@EnableFeignClients //增加

public class ServiceFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceFeignApplication.class, args);
	}
}

@FeignClient(value = "service-hi"   //同@LoadBalanced 服务名在这里
, fallback = SchedualServiceHiHystric.class )//Hystrix后加的,调用服务全断   不会调用对应的错误实现类的方法???? 
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi { //Hystrix后加的,没用？？？
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
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
	
http://localhost:8765/hi?name=forezp  也是 port:8762 和 port:8763切换

-- Zuul  默认和Ribbon结合   route and filter    
@EnableZuulProxy  //新增加的
@EnableEurekaClient
@SpringBootApplication
public class ServiceZuulApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceZuulApplication.class, args);
	}
}
application.yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8769
spring:
  application:
    name: service-zuul
zuul:  //新增组
  routes:
    api-a:
      path: /api-a/**
      serviceId: service-ribbon
    api-b:
      path: /api-b/**
      serviceId: service-feign
	  
	  
--filter
@Component
public class MyFilter extends ZuulFilter{

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }
//    	pre：路由之前
//    	routing：路由之时
//    	post： 路由之后
//    	error：发送错误调用

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("ok");
        return null;
    }
}

测试
http://localhost:8769/api-a/hi?name=forezp
http://localhost:8769/api-b/hi?name=forezp&token=123

--config 分布式配置中心 从Git上读
-server 端
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}

-application.properties 
spring.application.name=config-server
server.port=8888
spring.cloud.config.server.git.uri=https://github.com/forezp/SpringcloudConfig/
spring.cloud.config.server.git.searchPaths=respo
spring.cloud.config.label=master
spring.cloud.config.server.git.username=
spring.cloud.config.server.git.password=

-如果使用eureka, 使用服务ID,而不使用IP(要先启动一个8889的eureka )
eureka.client.serviceUrl.defaultZone=http://localhost:8889/eureka/


测试 http://localhost:8888/foo/dev 有返回JSON表示可以从客户端取   {name}/{profile}

https://github.com/forezp/SpringcloudConfig/ 中又个文件config-client-dev.properties文件中有一个属性：
foo = foo version 3

文件名格式
/{application}/{profile}[/{label}]    			// /]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties


-client端
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

-bootstrap.properties
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8888/
server.port=8881
#read git  file format  =  {application}-{profile}.properties

-如果使用eureka, 使用服务ID,而不使用IP(要先启动一个8889的eureka )
#spring.cloud.config.uri= http://localhost:8888/
eureka.client.serviceUrl.defaultZone=http://localhost:8889/eureka/
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
#-for config use eureka,use config-server  replace localhost:8888

dev开发环境配置文件
test测试环境
pro正式环境


http://localhost:8881/hi  返回  foo version 3

http://localhost:8889/ 显示有两个config server

---bus config client端
在标有 @EnableEurekaClient 的类上再增加一个  @RefreshScope


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

多的配置
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
#建用户zh，还要在管理界面中分配用户可Can access virtual hosts /
spring.rabbitmq.username=zh
spring.rabbitmq.password=123
management.security.enabled=false

 
POST 请求 (Firefox的 RESTClient  )  http://localhost:8881/bus/refresh  (config client端) 如更改Git配置,不用重启服务这样也能刷新 (MQ 广播配置文件的更改)


----Sleuth

Twitter开源的Zipkin就是参考Google Dapper 论文而开发
Dapper  一个请求在系统中会经过多个子系统的处理，而且这些处理是发生在不同机器甚至是不同集群上的，当请求处理发生异常时，需要快速发现问题，并准确定位到是哪个环节出了问题 

https://github.com/openzipkin/zipkin/
docker run -d -p 9411:9411 openzipkin/zipkin
http://localhost:9411 

还有PINPOINT  (也是 Dapper)监控调用哪个链路出现了问题，如响应时间
https://github.com/naver/pinpoint


	<dependency>
		<groupId>io.zipkin.java</groupId>
		<artifactId>zipkin-server</artifactId>
	</dependency>

	<dependency>
		<groupId>io.zipkin.java</groupId>
		<artifactId>zipkin-autoconfigure-ui</artifactId>
	</dependency>
	
@SpringBootApplication
@EnableZipkinServer
public class ServerZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerZipkinApplication.class, args);
	}
}
server.port=9411

---service-hi项目
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>

	import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
	@Bean
	public AlwaysSampler defaultSampler(){
		return new AlwaysSampler();
	}
	
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
	server.port=8988
	spring.zipkin.base-url=http://localhost:9411
	spring.application.name=service-hi
---service-miya 项目
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
spring.zipkin.base-url=http://localhost:9411
spring.application.name=service-miya

 
启动后 http://localhost:9411/ 有界面
 请求 http://localhost:8989/miya    后  Find Traces按钮有显示，
 请求 http://localhost:8988/hi  后 Analyze Dependencies按钮有显示
-----eureka-server 冗余来增加可靠性，当有一台服务器宕机了，服务并不会终止，因为另一台服务存有相同的数据
测试失败

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
			 <!-- 
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
 
----- 
Hystrix Turbine将每个服务Hystrix Dashboard数据进行了整合
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-turbine</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-netflix-turbine</artifactId>
</dependency>

@EnableTurbine 注解包含了@EnableDiscoveryClient注解

spring:
  application.name: service-turbine
server:
  port: 8769
security.basic.enabled: false
turbine:
  aggregator:
    clusterConfig: default   # 指定聚合哪些集群，多个使用","分割，默认为default。可使用http://.../turbine.stream?cluster={clusterConfig之一}访问
  appConfig: service-hi,service-lucy  ### 配置Eureka中的serviceId列表，表明监控哪些服务
  clusterNameExpression: new String("default")
  # 1. clusterNameExpression指定集群名称，默认表达式appName；
  
  //后面待确认真的对吗?
  #此时：turbine.aggregator.clusterConfig需要配置想要监控的应用名称
  # 2. 当clusterNameExpression: default时，turbine.aggregator.clusterConfig可以不写，因为默认就是default
  # 3. 当clusterNameExpression: metadata['cluster']时，假设想要监控的应用配置了eureka.instance.metadata-map.cluster: ABC，则需要配置，同时turbine.aggregator.clusterConfig: ABC
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
	  
http://localhost:8763/hystrix 或 http://localhost:8762/hystrix

http://localhost:8769/turbine.stream 做为monitor的地址
Hosts 值为2，下面有两组server

---consul 
服务健康监测 ,key/value 存储
https://www.consul.io/downloads.html
下载 win zip包,就一个consul命令

consul agent -dev 启动
http://localhost:8500

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

---

Spring Cloud提供为两个库 
Spring Cloud Context
Spring Cloud Commons  有不同的实现,如有 Netflix  , Consul

如果使用openJDK可能要Java Cryptography Extension (JCE) local_policy.jar 和 US_export_policy.jar  放在JDK/jre/lib/security 目录中(Oracle JDK不用)

两个上下文共享一个Environment
Bootstrap属性的优先级高，因此默认情况下不能被本地配置覆盖

bootstrap context  用 bootstrap.yml
application context 用 application.yml  (或者 .properties)



bootstrap.yml
spring:
  application:
    name: foo
  cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888
	  }
	  
	  
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

