
Dubbo 3.x 版本开始 用protobuf了，也是使用proto3文件生成代码，抄gRPC了
-----------------apache dubbo 2.7.x
(包名org.apache.dubbo开头)
https://github.com/apache/dubbo
https://github.com/apache/dubbo-admin
http://dubbo.apache.org/en-us/  
http://start.dubbo.io  里面用的还是com.alibaba 不是最新的 (2019-11-20)

2.7.4.1 有使用Unsafe类，如JDK11报 because module java.base does not export jdk.internal.misc  要用JDK1.8
2.7.6 版本依赖一个alibaba的 spring-context-support-xx.jar 先不升级
2.7.8 版本之前的hession2有远程执行代码的安全问题 	
 <dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-config-spring</artifactId>
    <version>2.7.4.1</version>
</dependency>
 <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-registry-zookeeper</artifactId>
   <version>2.7.4.1</version>
</dependency>

<dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-rpc-dubbo</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  
  <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-remoting-netty4</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  
  <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-serialization-hessian2</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo-configcenter-zookeeper</artifactId>
      <version>2.7.4.1</version>
  </dependency> 
  
//--common
package apache_dubbo27;
public interface GreetingService {
    String sayHi(String name);
}
//--server

如使用spring.xml 不能使用 dubbo-2.7.4.1.jar (xml名称空间为alibabatech)
 dubbo-config-api-2.7.4.1.jar
	dubbo-common-2.7.4.1.jar
	dubbo-rpc-api-2.7.4.1.jar
		dubbo-rpc-dubbo-2.7.4.1.jar
		dubbo-remoting-api-2.7.4.1.jar
		dubbo-rpc-injvm-2.7.4.1.jar
		dubbo-monitor-api-2.7.4.1.jar
		dubbo-remoting-netty4-2.7.4.1.jar
		dubbo-serialization-api-2.7.4.1.jar
		dubbo-serialization-hessian2-2.7.4.1.jar
			hessian-lite-3.2.6.jar(alibaba的hession2)
	dubbo-registry-api-2.7.4.1.jar
		dubbo-registry-zookeeper-2.7.4.1.jar
	dubbo-configcenter-api-2.7.4.1.jar
	dubbo-configcenter-zookeeper-2.7.4.1.jar
	dubbo-remoting-zookeeper-2.7.4.1.jar
	dubbo-cluster-2.7.4.1.jar
	
package apache_dubbo27_server;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig; 
import apache_dubbo27.GreetingService; 
import java.util.concurrent.CountDownLatch;
public class Application {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");
    public static void main(String[] args) throws Exception {
        ServiceConfig<GreetingService> service = new ServiceConfig<>();
        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        service.setInterface(GreetingService.class);
        service.setRef(new GreetingsServiceImpl());
        service.export();
        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}

package apache_dubbo27_server;
import apache_dubbo27.GreetingService;
public class GreetingsServiceImpl implements GreetingService {
    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}
//--client
package apache_dubbo27_client;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import apache_dubbo27.GreetingService;
public class Application { 
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");
    public static void main(String[] args) {
        ReferenceConfig<GreetingService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingService.class);
		
		reference.setRetries(0);
        reference.setTimeout(5*1000);
        reference.setCheck(false); 
		
        GreetingService service = reference.get();
        String message = service.sayHi("dubbo");
        System.out.println(message);
    }
}

如使用spring 加 dubbo-config-spring-2.7.4.1.jar

QoS，全称为Quality of Service, 于动态的对服务进行查询和控制(对服务进行动态的上下线 ) 默认端口是22222

可telnet仿问也 http仿问 curl -i http://localhost:22222/ls
---dubbo.properties文件 
dubbo.application.qos.enable=true
dubbo.application.qos.port=33333
dubbo.application.qos.accept.foreign.ip=false   #是否允许远程访问

如xml配置方式
  <dubbo:application name="demo-provider">
    <dubbo:parameter key="qos.enable" value="true"/>
    <dubbo:parameter key="qos.accept.foreign.ip" value="false"/>
    <dubbo:parameter key="qos.port" value="33333"/>
  </dubbo:application>
  
注释@使用方式 如使用放 dubbo-metadata-report-api-2.7.4.1.jar
//--anno Server
public class SpringAnnoDubboServer {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
        System.in.read();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "apache_dubbo27_server.anno")
    @PropertySource("classpath:/apache_dubbo27_server/anno/dubbo-provider.properties")
    static class ProviderConfiguration {
        @Bean
        public RegistryConfig registryConfig() {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress("zookeeper://127.0.0.1:2181");
            return registryConfig;
        }
    }
}

@Service //Dubbo的
public class DemoServiceImpl implements DemoService {

}
-- dubbo-provider.properties
dubbo.application.name=dubbo-demo-annotation-provider
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880




//--anno Client
public class SpringAnnoDubboClient {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        DemoService service = context.getBean("demoServiceComponent", DemoServiceComponent.class);
        String hello = service.sayHello("world");
        System.out.println("result :" + hello);
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "apache_dubbo27_client.anno")
    @PropertySource("classpath:/apache_dubbo27_client/anno/dubbo-consumer.properties")
    @ComponentScan(value = {"apache_dubbo27_client.anno"})
    static class ConsumerConfiguration {

    }
} 
@Component("demoServiceComponent")
public class DemoServiceComponent implements DemoService {
    @Reference() //Dubbo的 check=false
    private DemoService demoService;

    @Override
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }

    @Override
    public CompletableFuture<String> sayHelloAsync(String name) {
        return null;
    }
}  
---dubbo-consumer.properties
dubbo.application.name=dubbo-demo-annotation-consumer
dubbo.registry.address=zookeeper://127.0.0.1:2181

--- Dubbo容错
Failover Cluster 模式  失败自动切换，当出现失败，重试其它服务器。(缺省)
	通常用于读操作,通过retries=”2”来设置重试次数(不含第一次) 
Failfast Cluster 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
Failsafe Cluster 失败安全，出现异常时，直接忽略。 通常用于写入审计日志等操作。
Failback Cluster 失败自动恢复，后台记录失败请求，定时重发。
Forking Cluster 并行调用多个服务器，只要一个成功即返回。 可通过forks=”2”来设置最大并行数。
Broadcast Cluster 广播调用所有提供者，逐个调用，任意一台报错则报错

------------alibaba dubbo  2.6.0   

2.6.0 版本(包名alibaba开头)的Dubbo ops组里的 dubbo-admin-2.0.0\WEB-INF\dubbo.properties 默认值如下
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.admin.root.password=root
dubbo.admin.guest.password=guest

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>dubbo</artifactId>
	<version>2.6.0</version>
</dependency>

----2.6.0 版本的Dubbo ops组里的 dubbo-springboot
 
----
zkclient
javassist
依赖 netty 3(org.jboss.netty) 和 4(io.netty.) 版本 ,mina

Dubbo将自动加载classpath根目录下的dubbo.properties，
可以通过JVM启动参数：-Ddubbo.properties.file=xxx.properties 改变缺省配置位置。

System.setProperty("dubbo.properties.file", "alibaba/dubbo/client/dubbo.properties");


---client/dubbo.properties

dubbo.application.name=MyProject1
dubbo.protocol.name=dubbo 

dubbo.registry.address=zookeeper://127.0.0.1:2181
#dubbo.registry.address=zookeeper://192.168.16.125:2181?backup=192.168.16.126:2181
#dubbo.spring.config=classpath*:alibaba/dubbo/dubbo-client.xml,classpath*:alibaba/dubbo/dubbo-server.xml

#for client
dubbo.reference.timeout=55000

#--only connect specical IP,only for dev enviroment   or url="127.0.0.1:20884" 

dubbo.reference.dubboFacade.url= dubbo://127.0.0.1:20884
 
---server/dubbo.properties
dubbo.application.name=MyProject1
dubbo.protocol.name=dubbo
dubbo.protocol.port=20884
dubbo.protocol.serialization=hessian2	
# dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json ,hessian2 序列化不支持反序列化 java.util.EnumSet ,可用kryo

#dubbo.protocol.heartbeat=60000

dubbo.registry.address=zookeeper://127.0.0.1:2181
#dubbo.registry.address=zookeeper://192.168.16.125:2181?backup=192.168.16.126:2181
#dubbo.spring.config=classpath*:alibaba/dubbo/dubbo-client.xml,classpath*:alibaba/dubbo/dubbo-server.xml

#for server
pref.log.time.max.limit=500


dubbo-client.xml  
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">
    <dubbo:reference id="dubboFacade"  interface="alibaba.dubbo.server.DubboFacade" check="false" retries="0"/> 
	<!-- 
  	 直连的单个配置 ,和dubbo.properties不同的是,只对某个有效
	 <dubbo:reference id="dubboFacade"  interface="alibaba.dubbo.server.DubboFacade" check="false" retries="0" url="127.0.0.1:20884" />
	-->	
	
	<dubbo:reference id="dubboGroupVersionFacade"  interface="alibaba.dubbo.server.DubboGroupVersionFacade" check="false" retries="0" 
	 group="group1"  version="1.0"
	 />
	 
   <dubbo:application name="AppNameInXml"/>
   <!-- 同  dubbo.application.name  -->
   
   <dubbo:registry address="zookeeper://127.0.0.1:2181" />
   <!-- 同  dubbo.registry.address=zookeeper://127.0.0.1:2181    官方文档还可以注册到redis上  
			register="true" 是否注册上zookeeper上,通过直连 
			file="dubboregistry/op-baseinfo-provider.properties"  Dubbo缓存文件
			 check="false" 半闭注册中心启动时检查  ,如果服务端没有启动,客户端不能能启动 
   -->
   
</beans>

dubbo-server.xml 接口方法参数类 implements Serializable
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">	
	
 
	<dubbo:service interface="alibaba.dubbo.server.DubboFacade" ref="dubboFacdeImpl"/>
	<!-- registry="$RegisterId" -->
	
	<bean id="dubboFacdeImpl"  class="alibaba.dubbo.server.DubboFacadeImpl" />
	
	<dubbo:service interface="alibaba.dubbo.server.DubboGroupVersionFacade" ref="dubboGroupVersionFacadeImpl"
 	 group="group1"  version="1.0"/>
 	 <!--  group="group1" 可 相同的接口多个不同的实现，用group区分
	 version="1.0.0" 当一个接口的实现，出现不兼容升级时(即删改属性，加是兼容的)，可以用版本号过渡，版本号不同的服务相互间不引用
	 -->
	<bean id="dubboGroupVersionFacadeImpl"  class="alibaba.dubbo.server.DubboGroupVersionFacadeImpl" />
 	
	
	
	<!--  
 	dubbo.protocol.name=dubbo
	dubbo.protocol.port=20884
    dubbo.protocol.serialization=hessian2	
    # dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json ,hessian2 序列化不支持反序列化 java.util.EnumSet 
	kryo(官方版本报错,pingan版本不报错)
	
  	<dubbo:protocol name="dubbo" port="20884"  serialization="kryo"/>
  -->
  
</beans>

可以使用 telent 127.0.0.1 28004 连接上用 ls看所有提供服务

dubbo consumer 负载均衡策略
1.Random  随机　按权重  默认 
2.RoundRobin 轮循 按权重
3.LeastActive  最少活跃调用数,相同活跃数的随机  ,调用前后计数差,慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大
4.ConsistentHash  一致性Hash  缺省只对第一个参数Hash，
	如果要修改，请配置<dubbo:parameter key="hash.arguments" value="0,1" />
	缺省用160份虚拟节点，如果要修改，请配置<dubbo:parameter key="hash.nodes" value="320" />
界面上有随机，轮询，最少并发


dubbo协议使用默认Hessian二进制序列化(netty),也可使用 kryo,通讯使用mina
Hessian协议   Dubbo缺省内嵌Jetty作为服务器实现
Thrift是Facebook捐给Apache
 
 
dubbo main 方法 com.alibaba.dubbo.container.Main 可实现安全关机,用JDK的ShutdownHook,
如kill 不带-9 Provider方可以先不接收请求,如有任务等待完成,Consumer方,如有请求没有返回的等待
eclipse启动用  com.alibaba.dubbo.container.Main 参数传 -Ddubbo.properties.file=alibaba/dubbo/server/dubbo.properties -Ddubbo.spring.config=classpath:alibaba/dubbo/server/dubbo-server.xml
