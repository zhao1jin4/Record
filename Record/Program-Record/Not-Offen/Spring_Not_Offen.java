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