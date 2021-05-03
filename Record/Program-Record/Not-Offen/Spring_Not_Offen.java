
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-jms</artifactId>
		<version>${spring.version}</version>
	</dependency>
	

	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-oxm</artifactId>
		<version>${spring.version}</version>
	</dependency>
	


﻿<!--  batch  -->
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
	  <groupId>org.springframework.data</groupId>
	  <artifactId>spring-data-solr</artifactId>
	  <version>1.2.0.RELEASE</version>
	</dependency>
	
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-webmvc-portlet</artifactId>
		<version>4.3.18.RELEASE</version>  
	</dependency>
	
=========================Spring Struts1
struts-config.xml 文件中加入
    <plug-in className="org.springframework.web.struts.ContextLoaderPlugIn"> 
	<set-property property="contextConfigLocation" value="/WEB-INF/applicationContext.xml"/>
    </plug-in>


	在  action中不用写 type="org.springframework.web.struts.DelegatingActionProxy"属性
	<controller>
		<set-property property ="processorClass" value="org.springframework.web.struts.DelegatingRequestProcessor"/>
	</controller>


<bean depends-on="beanName"
<bean 
	<lookup-Method name="methodName" bean="注入给方法的返回类型"
	
<bean 
	<replace-Method name="被替换的方法名"  replacer="实现_MethodReplacer_接口"	
	
=========================Spring WebService 15.9
	
 由于Spring Web Service是基于Spring MVC的, 在web.xml中添加如下servlet,
并在WEB-INF下建立SpringMVC的默认配置文件spring-ws-servlet.xml:


<servlet>       
    <servlet-name>spring-ws</servlet-name>        
    <servlet-class>org.springframework.ws.transport.http.MessageDispatcherServlet</servlet-class>  
	 <init-param>
            <param-name>transformWsdlLocations</param-name>
            <param-value>true</param-value>
     </init-param>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:/spring-ws-context.xml</param-value>
	</init-param>

</servlet>  
<servlet-mapping>  
    <servlet-name>spring-ws</servlet-name>  
    <url-pattern>/*</url-pattern>    */
</servlet-mapping>   


spring-ws-servlet.xml:
 <bean class="org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping">
	<property name="endpointMap">  
		 <map>  
			 <entry key="{http://www.fuxueliang.com/ws/hello}HelloRequest" >        
				 <ref bean="helloEndpoint" />  
			 </entry>  
		 </map>  
	 </property>
	//表示 如是http://mycompany.com/hr/schemas}名称空间的HolidayRequest元素,则对应调用<bean id=holidayEndpoint 的
	//也可以用 mappings 属性
	 <property name="mappings">
		<props>
			<prop key="{http://www.fuxueliang.com/ws/hello}HelloRequest">helloEndpoint</prop>
		</props>
	</property>
	<property name="interceptors">
		<bean class="org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor"/>
	</property>
</bean>






//表示生成  http://localhost:8080/spring-ws/holidayService/holiday.wsdl  以.wsdl结尾, 而不是?wsdl,
<bean id="holiday" class="org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition">
	<property name="schema" ref="schema"/>
	<property name="portTypeName" value="HumanResource"/>
	<property name="locationUri" value="/holidayService/"/>
	<property name="targetNamespace" value="http://mycompany.com/hr/definitions"/>
</bean>
<bean id="schema" class="org.springframework.xml.xsd.SimpleXsdSchema">
	<property name="xsd" value="/WEB-INF/hr.xsd"/>
</bean>

hr.xsd中是<wsdl:types>中的定义 ,文件中的开始标签是<xsd:schema 


<bean id="hello" class="org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition">  
         <property name="wsdl" value="/WEB-INF/hello.wsdl"/>  
</bean> 
hello.wsdl文件里就是完整的wsdl文件

 
 extends AbstractDomPayloadEndpoint // 服务端 要手工解析XML
		也有 AbstractDom4jPayloadEndpoint 和 AbstractJDomPayloadEndpoint
			

//客户端
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;


String NAMESPACE_URI = "http://www.fuxueliang.com/ws/hello";
String PREFIX = "tns";
String url = "http://localhost:8080/SpringWS_w3c";

SOAPMessage request = MessageFactory.newInstance().createMessage();
SOAPEnvelope envelope = request.getSOAPPart().getEnvelope();
Name helloRequestName = envelope.createName("HelloRequest", PREFIX, NAMESPACE_URI);
SOAPBodyElement helloRequestElement = request.getSOAPBody().addBodyElement(helloRequestName);
helloRequestElement.setValue("Rondy.F");

SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
SOAPMessage response = connection.call(request, url);
if (!response.getSOAPBody().hasFault())
{
	 envelope = response.getSOAPPart().getEnvelope();
	Name helloResponseName = envelope.createName("HelloResponse", PREFIX, NAMESPACE_URI);
	Iterator childElements = response.getSOAPBody().getChildElements(helloResponseName);
	SOAPBodyElement helloResponseElement = (SOAPBodyElement) childElements.next();
	String value = helloResponseElement.getTextContent();
	System.out.println("Hello Response [" + value + "]");
} else
{
	SOAPFault fault = response.getSOAPBody().getFault();
	System.err.println("SOAP Fault Code :" + fault.getFaultCode());
	System.err.println("SOAP Fault String :" + fault.getFaultString());
}

从SpringMVC提供的那么多的Controller就可以看出来.这次也不例外,jibx, castor, xmlBeans, jaxb, xstream全给你准备了

2, EndPointMapping也提供了很多选择,包括method, 还有注解的方式


<bean class="org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping">
<bean class="org.springframework.ws.server.endpoint.adapter.MarshallingMethodEndpointAdapter">
      <constructor-arg ref="marshaller"/>
</bean>

<bean id="marshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller"
          p:contextPath="xxx" />

http://www.springbyexample.org/examples/simple-spring-web-services.html


=======================上 SpringWebService
=======================Spring iBatis2
DataAccessException

org.springframework.orm.ibatis.SqlMapClientFactoryBean
1.dataSource
2.configLocation ,setSqlMapClientProperties(Properties 如resource="aa.properties")


Dao类继承自SqlMapClientDaoSupport,setSqlMapClient()从SqlMapClientFactoryBean
	this.getSqlMapClientTemplate().queryForList();
	.execute(SqlMapClientCallback action) 
	SqlMapClientCallback的 Object doInSqlMapClient(com.ibatis.sqlmap.client.SqlMapExecutor executor) //SqlMapExecutor是SqlMapClient的父类有select ,delete ,update,queryForList,
=======================上  Spring iBatis2	


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

=========================






 =========================Spring HATEOAS
(HATEOAS) Hypermedia as the Engine of Application State
//依赖于 objenesis-2.1.jar


import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Greeting extends ResourceSupport {
    private final String content;
    @JsonCreator
    public Greeting(@JsonProperty("content") String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
}

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@Controller
public class GreetingController
{
    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping("/greeting")
    @ResponseBody
    public HttpEntity<Greeting> greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        Greeting greeting = new Greeting(String.format(TEMPLATE, name));
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
        
        //依赖于 objenesis-2.1.jar
        //linkTo 生成 ("href":) 放入 add 到 _links  中
        // methodOn(GreetingController.class).greeting(name) 生成  "href":"http://localhost:8080/J_SpringMVC/greeting?name=World"
        // withSelfRel 生成 Link对象 生成  "rel":"self" 
        /* 
        	{"content":"Hello, World!",
        		"links":
        			[{
        				"rel":"self",
        				"href":"http://localhost:8080/J_SpringMVC/greeting?name=World"
        			}]
        	}
        */
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }
}


@RestController 返回 Greeting 类时,是以JSON显示
如要以XML返回,返回类要有默认构造器,返回类加@XmlRootElement  (可选方法上加@ResponseBody)



==============Swagger 框架  1.x(SpringMVC)
swagger-springmvc-1.0.2.jar
swagger-models-1.0.2.jar
swagger-annotations-1.3.11.jar
guava-17.0.jar
swagger-spring-mvc-ui-0.4.jar  不可自定义界面




import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
@Controller
@RequestMapping(value = "swaggerController")
public class SwaggerController {
	@RequestMapping(value = "test", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "测试接口", httpMethod = "POST", notes = "测试接口", response = ResponseModel.class)
    public @ResponseBody ResponseModel newPlan(@ApiParam(required = true) @RequestBody  RequestModel request)
    {
		System.out.println("StartTime="+request.getStartTime());
		ResponseModel resp=new ResponseModel();
		resp.setData("123");
		resp.setErrorMessage("成功");
		return resp;
    }
}

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
@Configuration
@EnableSwagger
public class SwaggerConfig
{
    private SpringSwaggerConfig springSwaggerConfig;
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
    {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() throws IOException
    {
        Properties prop = new Properties();
        String pathString = this.getClass().getClassLoader().getResource("/").getPath();
        pathString+="properties/apiInfo.properties";
        InputStream in = new FileInputStream(pathString);
        prop.load(in);
        in.close();
        ApiInfo apiInfo = new ApiInfo("项目标题",  "项目描述", "官方URL",  "联系人aa@sina.com", null, null);
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo).includePatterns(".*?");
    }
}
import com.fasterxml.jackson.annotation.JsonProperty;
public class RequestModel     {

    @JsonProperty("StartTime")
    private Date startTime; 
    
    @JsonProperty("Status")
   	private  String status;
           
    @JsonProperty("ID")
	private  Long id;
}
properties/apiInfo.properties


http://127.0.0.1:8080/J_SpringMVC/sdoc.jsp 
 页面中使用 http://127.0.0.1:8080/J_SpringMVC/api-docs.mvc  或者修改   <url-pattern>/</url-pattern>

页面中 Data Type 组选    Model Schema 









