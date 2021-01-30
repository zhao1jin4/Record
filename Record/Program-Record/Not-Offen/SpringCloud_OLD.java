
-- ribbon+restTemplate 
ribbon是一个客户端负载均衡
-- Netflix开源了Hystrix组件, 断路器模式

客户端(服务端) 改个端口到 8763 再启动一个  service-hi 就有两个服务了

再建立一个客户端   Client Side Load Balancing (Ribbon)
 
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>>
 
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>


<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<!-- http://localhost:8762/actuator -->
 
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
<dependency>
	<groupId>com.netflix.hystrix</groupId>
	<artifactId>hystrix-javanica</artifactId>
	<version>1.5.18</version>
</dependency>
<dependency> 
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-turbine</artifactId>
</dependency> 

@SpringBootApplication
@EnableDiscoveryClient 
//@EnableHystrix //断路器(如果几台中一台服务不可用,就把这台服务器隔离了,就仿问不到这台机器了) Hystrix后加的
@EnableCircuitBreaker //可以替代 @EnableHystrix
@EnableHystrixDashboard //maven 加 spring-boot-starter-actuator 就可以仿问 http://localhost:8764/hystrix  提示 http://hostname:port/turbine/turbine.stream 
//文本框中输入 http://localhost:8764/hystrix.stream ( http://localhost:8764/actuator/hystrix.stream),2000ms,Monitor Stream按钮 ->Greenwitch 版本 OK

//在另一个窗口 http://localhost:8764/hi?name=lisi 时,上一个窗口有图表 

//对这个服务的负载方式默认的轮循可修改为随机
@RibbonClient(name="SERVICE-HI",configuration=config.Config.class)
public class ServiceRibbonApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceRibbonApplication.class, args);
	}
	@Bean
	@LoadBalanced //(spring-cloud-starter-ribbon) ribbon 和 @LoadBalanced 一起用      客户端都可做负载均衡,相当于 dubbo consumer 
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
//包名不能在@SpringBootApplication所在的包下,或 @ComponentScan(excludeFilters= {@ComponentScan.Filter(type=FilterType.ANNOTATION,value=我的自定义@.class)} )

@Configuration
public class Config {
	@Bean
	public IRule ribbonRule(IClientConfig config) {
		return new RandomRule();//ribbon随机请求
	}
}

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
	
	 @HystrixCommand(fallbackMethod = "hiError")//Hystrix后加的
    public String hiService(String name) {
		 //restTemplate 上有 @LoadBalanced 负载均衡客户端
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class); 
		//这里直接SERVICE-HI用名字来连,不区分大小写, 用在方法级别
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
	@GetMapping("/feignMVC/{owner}/")//针对@FeignClient中加	configuration= {config.FooConfiguration.class},/
	public String feignMVC(@PathVariable("owner") String owner)
	{
		return "hello "+owner+",i am from port:" +port;
	}
	 @Autowired
	private LoadBalancerClient loadBalancer;
    @RequestMapping(value = "/choose")
    public String hi( ){
    	//默认是轮循,已经修改为随机，但SERVICE-HI要用大写
	   ServiceInstance serviceInstance = this.loadBalancer.choose("SERVICE-HI");
	   return serviceInstance.getHost()+":"+serviceInstance.getPort();
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
	
#management.endpoints.web.exposure.include: hystrix.stream
management:
  endpoints:
    web:
      exposure:
        include: "*"
      cors:
        allowed-origins: "*"
        allowed-methods: "*"
#Cross-origin resource sharing (CORS)  from spring boot

#16.4 Customizing the Ribbon Client by Setting Properties#优先级高于@RibbonClient(name="SERVICE-HI",configuration=config.Config.class)
service-hi:
  ribbon: 
     NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
#    listOfServers: 127.0.0.1:8762     #  对ribbon不使用eureka
# 对 ribbon不使用eureka
#ribbon:
#  eureka:
#   enabled: false 


   
测试  http://localhost:8764/hi?name=forezp 	  发现间隔调用 8762 和 8763 ,因加了 @LoadBalanced
   
---客户端一ribbon  用在方法级别 
  @EnableDiscoveryClient  (spring-cloud-starter-netflix-ribbon)
		  
		  @LoadBalanced 放在 new RestTemplate()上  
		  方法中 restTemplate.getForObject("http://SERVICE-HI/hi")
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
	//为开启Hystrix ，使用新的ThreadLocal导致中的数据丢失，导致RequestContextHolder.getRequestAttributes()为null（修改为使用SEMAPHORE），
	@Bean
	public SetterFactory setterFactory(){
		SetterFactory setterFactory =new SetterFactory() {
			@Override
			public HystrixCommand.Setter create(Target<?> target, Method method) {

				String groupKey = target.name();
				String commandKey = Feign.configKey(target.type(), method);

				HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
						//设置统计指标60秒为一个时间窗口
						.withMetricsRollingStatisticalWindowInMilliseconds(1000 * 60)
						//超过80%失败率
						.withCircuitBreakerErrorThresholdPercentage(80)
						//操作5个开启短路器
						.withCircuitBreakerRequestVolumeThreshold(5)
						//设置线程隔离
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
						//设置断路器的开启时间为60秒
						.withCircuitBreakerSleepWindowInMilliseconds(1000 * 60);

				return HystrixCommand.Setter
						.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
						.andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
						.andCommandPropertiesDefaults(setter);
			}
		};
		return setterFactory;
	}
	
	
	
	
	
-- Zuul 是服务端的负载均衡器，是使用ribbon
 默认和Ribbon结合   route and filter    
被spring cloud gatewary所替代
 
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
<dependency>
	<groupId>com.netflix.hystrix</groupId>
	<artifactId>hystrix-javanica</artifactId>
	<version>1.5.18</version>
</dependency>


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
  #prefix: /api
  #http://localhost:8769/api/api-a/hi?name=lisi 
  #strip-prefix: false #如加上 是找controller为/api/api-a/hi
  #禁用zuul filter
 #zuul.<SimpleClassName>.<filterType>.disable=true 
  MyFilter:
    pre:
      disable: false
  routes:
    api-a:
      path: /api-a/**				#*/
      serviceId: service-ribbon
	#api-a:
    #  path: /api-a/**
    #  url: http://localhost:8769/
	#地址写死,不会做负载，不会HystrixCommand
	#---
	#service-ribbon: /api-a/** #方式二 格式<application-name>:/路径/**     */
    api-b:
      path: /api-b/**				#*/
      serviceId: service-feign
	  
如请求路径以是/zuul/*   */格式可以跳过DispatcherServlet ，如上传大小的限制
-- application.yml 
feign:
  hystrix:
    enabled: true
#  httpclient:
#    enabled: true
#  okhttp:
#    enabled: false 

#对第一次请求就是失败，原因第一次请求返回时间过长
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 60000
ribbon:
  ConnectTimeout: 3000
	: 60000  

禁用某个filter
  zuul.<SimpleClassName>.<filterType>.disable=true
  
curl -v -H "Transfer-Encoding: chunked" \
    -F "file=@mylarge.iso" localhost:9999/zuul/simple/file
	
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
http://localhost:8769/api-a/hi?name=lisi
http://localhost:8769/api-b/hi?name=lisi&token=123
http://localhost:8769/service-ribbon/hi?name=lisi  默认这个可以仿问 如不加路由 routes:别名 配置
	
@Component
class MyFallbackProvider implements FallbackProvider {
    @Override
    public String getRoute() {
        return "service-hi"; //当zuul路由这个服务不可以仿问时，显示fallback getBody()的返回值 
    }
    @Override
    public ClientHttpResponse fallbackResponse(String route, final Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }
            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }
            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }
            @Override
            public void close() {
            }
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("fallback".getBytes());
            }
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}









----- 
Hystrix Turbine将每个服务Hystrix Dashboard数据进行了整合(Hystrix被替代，Turbine也没什么发展了)
 
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-turbine</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-netflix-turbine</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>

@EnableEurekaClient
@EnableDiscoveryClient
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker 
@EnableTurbine 注解包含了@EnableDiscoveryClient注解
 //文本框中输入 http://localhost:8764/turbine.stream
 
--application.yml
spring:
  application.name: service-turbine
server:
  port: 8769
  
#security.basic.enabled: false

management:
  endpoints:
    web:
      exposure:
        include: "*"
      cors:
        allowed-origins: "*"
        allowed-methods: "*"
#Cross-origin resource sharing (CORS)  from spring boot 

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

  #combine-host: true
  combineHostPort: true
  instanceUrlSuffix: 
    default: actuator/hystrix.stream
  
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
	  
http://localhost:8763/hystrix 或 http://localhost:8762/hystrix
http://localhost:8764/turbine.stream 做为monitor的地址
Hosts 值为2，下面有两组server


https://projects.spring.io/spring-cloud/spring-cloud.html#_turbine
https://projects.spring.io/spring-cloud/spring-cloud.html#_circuit_breaker_hystrix_dashboard







