
------------
CXF 3.2.0 版本实现 JAX-RS 2.1 Final 

RESTEasy (jBoss项目)实现了 JAX-RS 2.1 规范 
Jersey  (毛线衫) 扩展 JAX-RS  2.0 规范，可以和2.1版本的sse(Server Send Event)功能一起用
javax.ws.rs.sse包(Server Send Event)是JAX-RS 2.1的功能

------------CXF OpenApiFeature 
http://cxf.apache.org/docs/openapifeature.html

CXF 3.3.2  也支持OpenApi 3.0.x 

<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-rs-service-description-openapi-v3</artifactId>
    <version>3.2.4</version>
</dependency>
CXF 3.2.x  才可用 ，可生成OpenAPI v3.0 的文档
 
 
代码配置
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
 
...
 
final OpenApiFeature feature = new OpenApiFeature();
feature.setContactEmail("cxf@apache.org");
feature.setLicense("Apache 2.0 License");
feature.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");           
feature.setSecurityDefinitions(Collections.singletonMap("basicAuth",new SecurityScheme().type(Type.HTTP)));


==================================CXF SOAP Webservice

Java API for XML Web Services (JAX-WS) 
JAXB  (Java API for Xml  Binding)
JAXP (Java API for Xml  Parsing)

Universal Description, Discovery and Integration (UDDI)
World Wide Web Consortium (W3C)    //Consortium 联盟

CXF_HOME=/opt/Java/CXF
ANT_HOME=/opt/Java/Ant
export CLASSPATH=.:$CXF_HOME/lib/cxf-manifest-incubator.jar:./build/classes


sample 目录下的java_first_jaxws  运行 OK
sample 目录下的java_first_pojo
 

用wsdl2java -p org.zh  HelloWorld.wsdl //生成在指定包名
 
//接口和实现类要在同一个包下,因为名称空间,生成的wsdl不能有<wsdl:import location=
 








  <properties> 
	  <cxf.version>3.4.2</cxf.version>
  </properties>

 
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-bindings-soap</artifactId>
  <version>${cxf.version}</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-bindings-xml</artifactId>
  <version>${cxf.version}</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-databinding-jaxb</artifactId>
  <version>${cxf.version}</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-databinding-aegis</artifactId>
  <version>${cxf.version}</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-frontend-jaxrs</artifactId>
  <version>${cxf.version}</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-frontend-jaxws</artifactId>
  <version>${cxf.version}</version>
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-rs-client</artifactId>
  <version>3.4.2</version> 
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-transports-http-jetty</artifactId>
  <version>${cxf.version}</version> 
</dependency>


===== sample 目录下的 java_first_jaxws 
------HelloWorld.java
package org.zh.cxf;
import javax.jws.WebParam;
import javax.jws.WebService;
@WebService
public interface HelloWorld
{
	String sayHi(@WebParam(name = "text")String text);
}

--------HelloWorldImpl.java
package org.zh.cxf;
import javax.jws.WebService;

@WebService(endpointInterface = "org.zh.cxf.HelloWorld", serviceName = "helloSer")
public class HelloWorldImpl implements HelloWorld
{
	public String sayHi(String text)
	{
		return "Hello " + text;
	}
}

----------Server.java
package org.zh.cxf.server;
import javax.xml.ws.Endpoint;
public class Server
{
	public static void main(String[] args)
	{
		System.out.println("Starting Server");
		HelloWorldImpl implementor = new HelloWorldImpl();
		String address = "http://localhost:8000/helloWorld";// 网址的 地址　
		Endpoint.publish(address, implementor);
		System.out.println("Server started.");
	}
}

-------------ClientJAXWS.java
package org.zh.cxf.client;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.zh.cxf.HelloWorld;
public class ClientJAXWS 
{															
    private static final QName SERVICE_NAME = new QName("http://cxf.zh.org_1/",  "HelloSer_xx"); //无所谓是什么
    private static final QName PORT_NAME    = new QName("http://cxf.zh.org/", "HelloWorldPort"); //PORT 不可以变,包名返写
    public static void main(String[] args)
    {
		Service service = Service.create(SERVICE_NAME);
		String endpointAddress = "http://localhost:8000/helloWorld";
		service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);//测试用http://localhost:8000/helloWorld?wsdl
		HelloWorld hw = service.getPort(HelloWorld.class);
		System.out.println(hw.sayHi("XXX"));
    }
}
//java_first_jaxws  对象

===== 上 java_first_jaxws 

wsimport  -s c:/tmp/ws_code -p org.zh.ws -encoding utf8 -keep  http://localhost:8000/helloWorld?wsdl  
生成webservice代码 -s的目录要是已经存的
生成的类有 @WebServiceClient 注释的类 new出来
	构造器传URL可带?wsdl,也可不带,和new QName 参数同@WebServiceClient
就可以getXxxPort().method(xx)
@WebEndPoint 在getXxxPort()方法前

// 加  stax2-api-3.1.4.jar 和  woodstox-core-5.0.3.jar
URL url=new URL("http://localhost:9000/helloWorld?wsdl");
QName HELLOWORLDIMPLSERVICE_QNAME = new QName("http://server.hw.demo/", "HelloWorldImplService");
HelloWorldImplService service=new  HelloWorldImplService(url,HELLOWORLDIMPLSERVICE_QNAME);
User u=new User();
u.setName("test生成代码");
String res=service.getHelloWorldImplPort().sayHiToUser(u);
System.out.println(res);

javax.xml.ws.spi.Provider 如不加CXF使用JDK的
cxf-rt-frontend-jaxws-xx.jar/META-INF/services/有文件 javax.xml.ws.spi.Provider 内容为实现类 org.apache.cxf.jaxws22.spi.ProviderImpl
也可以使用 System.setProperty("javax.xml.ws.spi.Provider","com.sun.xml.internal.ws.spi.ProviderImpl");
 
使用ServiceLoader 来找

------------方法参数是对象 

nill  不想, 不愿
marshal 整顿, 排列,

					XmlAdapter<ValueType,BoundType>  //ValueType - knows(@XmlType) , BoundType -doesn't know 


UserAdapter extends XmlAdapter<UserImpl, User> 


@XmlJavaTypeAdapter(UserAdapter.class)
public interface User {}

@XmlType(name = "User")
public class UserImpl implements User


看WSDL文件
<wsdl:definitons name="默认是实现类+Service" targetNamespace="http://包名反">
	<wsdl:types /> 对应方法的参数类型,和返回值 类型,sayHelloReponse是返回类型 ,type对应下面的定义,里的name="arg0"  "return "可以改的
	在接口类的方法中参数前加(@WebParam(name="xx"),返回参数前加@WebResult(name="yyy")
	
	<wsdl:message />  请求的参数,返回的值包装成message element是上type中定义的
	
	<wsdl:portType/>  接口  方法是operation 
	<wsdl:binding/>  literal 文字的,
	<wsdl:service/>  对应多个<wsdl:binding/> ,每个对应一个地址
</wsdl:definitons>





@WebMethod
javax.jws.OneWay 方法只有输入参数,没有输出参数
javax.jws.soap

SOAPBinding.Style.DOCUMENT
SOAPBinding.Use.LITERAL


JAXB (Java API for XML Binding)
JavaObject ->XML 叫 marshal整顿
 JAXB 注释


@XmlRootElement  //java类
@XmlElement
@XmlAccessorType 
@XmlTransient  当前属性不要转换成XML
@XmlJavaTypeAdapter



@XmlRootElement(name="myroot" ,namespace="http://mynamespace") //根节点,必须的
@XmlAccessorType(XmlAccessType.FIELD) 
public class Boy
{
	 @XmlElement(name="theName")
	private  String name; //没有初始化开始为null不会在XML中显示  

	@XmlElement  
	private int age=10;  
	
	@XmlElementWrapper(name="favorites")
    @XmlElement(name="favorite")  
    private List<String> favorites;  
	
    @XmlJavaTypeAdapter(MyXMLAdapter.class)    // 是对接口的,如是类就不用了
	private Address addr; 
 
	@XmlElement(name="theFamily")  
    private Family family; 
}
@XmlAccessorType(XmlAccessType.FIELD)
public class Family
{
    @XmlElement(name="theFather")  
    private String father;
    
    @XmlElement(name="theMather")  
    private String mather;
}

 
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
JAXBContext context=JAXBContext.newInstance(Boy.class); //类名,也可以是包名,下的所有的类
Marshaller marshaller=context.createMarshaller();	//Java->XML的
Unmarshaller unmarshaller=context.createUnmarshaller(); //XML->Java的
Boy boy=new Boy();
//body.setName("张三");
marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);//true 不要<?xml 头
marshaller.marshal(boy,System.out);//如字段值为null,标签就不会出现,如何要出现????
//---可marshal到 SOAPBody
 SOAPMessage soapRequest= MessageFactory.newInstance().createMessage();
 SOAPBody   soapBody=soapRequest.getSOAPBody();
 marshaller.marshal(body,soapBody);

Boy boy2=(Boy)unmarshaller.unmarshal(new StringReader("<ns2:myroot xmlns:ns2='http://mynamespace'><name>U</name></ns2:myroot>"));//可以是很多类型的XML

 
XmlAccessType.FIELD     自动把  非static,非transient属性 行转换 ,包括 private 属性 ,也可显示指定其它地方@
XmlAccessType.PROPERTY  自动把  getter/setter 行转换 ,也可显示指定其它地方@ 
XmlAccessType.PUBLIC_MUMBER  



如果类的属性是一个复杂类型,要在定义一个类 extends XMLAdapter<String,Address>  第一个值类型(XML),第二个绑定类型(JAVA),
即 public String marshal(Address add)   //Java->XML  是 marshal
	return "xx" //不用写<> ,多字段可根据自己来分隔,如,

@XmlJavaTypeAdapter(MyXMLAdapter.class)   //get前  是对接口的,如是类就不用了
getAddr(){...}





----------ClientW3C.java
package org.zh.cxf.client;
import java.util.Iterator;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
public class ClientW3C
{
	public static void main(String[] args) 
	{
		String NAMESPACE_URI = "http://cxf.zh.org/";
		String PREFIX = "tns";
		String URL = "http://localhost:8000/helloWorld";
		String REQ_NAME="sayHi";
		String RES_NAME="sayHiResponse";
		String PARAM_NAME="text";
		String HELLO="Jack";
		try {
			SOAPMessage requestMsg = MessageFactory.newInstance().createMessage();
			requestMsg.setProperty(SOAPMessage.WRITE_XML_DECLARATION,"true");
			requestMsg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
			SOAPEnvelope envelope = requestMsg.getSOAPPart().getEnvelope();
			/*
			 {//方式一
				Name helloRequestName = envelope.createName(REQ_NAME, PREFIX, NAMESPACE_URI);
				
				SOAPBodyElement helloRequestElement = requestMsg.getSOAPBody().addBodyElement(helloRequestName);
				SOAPElement param=	helloRequestElement.addChildElement(PARAM_NAME);
				param.setValue(HELLO);
				
				requestMsg.writeTo(System.out);
				System.out.println();
	//			<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	//				<SOAP-ENV:Header/>
	//				<SOAP-ENV:Body>
	//					<tns:sayHi xmlns:tns="http://server.webservice.zh.org/">
	//						<text>Jack</text>
	//					</tns:sayHi>
	//				</SOAP-ENV:Body>
	//			</SOAP-ENV:Envelope>
			}*/
			{//方式二
				SOAPElement  ns=envelope.addNamespaceDeclaration(PREFIX, NAMESPACE_URI);//加名称空间
				Name helloRequestName = envelope.createName(PREFIX+":"+REQ_NAME);//使用
				
				SOAPBodyElement helloRequestElement = requestMsg.getSOAPBody().addBodyElement(helloRequestName);
				SOAPElement param=	helloRequestElement.addChildElement(PARAM_NAME);
				param.setValue(HELLO);
				
				requestMsg.writeTo(System.out);
//				<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
//						xmlns:tns="http://cxf.zh.org/">
//						<SOAP-ENV:Header />
//						<SOAP-ENV:Body>
//							<tns:sayHi>
//								<text>Jack</text>
//							</tns:sayHi>
//						</SOAP-ENV:Body>
//					</SOAP-ENV:Envelope>
				System.out.println();
			}
			SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
			  //这里可以设置超时时间
			 URL url = new URL( null,  "http://127.0.0.1:8080",  new URLStreamHandler()
			 { 
				 @Override
				 protected URLConnection openConnection(URL url) throws IOException 
				 {
					 URL clone_url = new URL(url.toString());
					 HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url.openConnection();
					 clone_urlconnection.setConnectTimeout(10000);
					 clone_urlconnection.setReadTimeout(10000);
					 return clone_urlconnection ;
				 }
			});
			SOAPMessage responseMsg = connection.call(requestMsg, URL);
			
			responseMsg.writeTo(System.out);
			System.out.println();
//			<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
//				<soap:Body>
//					<ns1:sayHiResponse xmlns:ns1="http://server.webservice.zh.org/">
//						<return>Hello Jack</return>
//					</ns1:sayHiResponse>
//				</soap:Body>
//			</soap:Envelope>
			if (!responseMsg.getSOAPBody().hasFault())
			{
				envelope = responseMsg.getSOAPPart().getEnvelope();
				Name helloResponseName = envelope.createName(RES_NAME, PREFIX, NAMESPACE_URI);
				Iterator childElements = responseMsg.getSOAPBody().getChildElements(helloResponseName);
				SOAPBodyElement helloResponseElement = (SOAPBodyElement) childElements.next();
				String value = helloResponseElement.getTextContent();
				System.out.println("\nHello Response [" + value + "]");
			} else
			{
				SOAPFault fault = responseMsg.getSOAPBody().getFault();
				System.err.println("SOAP Fault Code :" + fault.getFaultCode());
				System.err.println("SOAP Fault String :" + fault.getFaultString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




private SOAPMessage convertString2SOAP(String classPathFile) throws Exception 
{
	BufferedReader reader=new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(classPathFile)));
	String line;
	StringBuffer soapBuffer=new StringBuffer();
	while((line=reader.readLine())!=null)
	{
		soapBuffer.append(line).append("\n");
	}
	 SOAPMessage reqMsg = MessageFactory.newInstance().createMessage(new MimeHeaders(), new ByteArrayInputStream(soapBuffer.toString().getBytes(Charset.forName("UTF-8"))));
	 reqMsg.saveChanges();
	 //reqMsg.writeTo(System.out);
	 return reqMsg;
}

private String convertSOAP2String(SOAPMessage msg) throws Exception
{
	SOAPPart soapPart=msg.getSOAPPart();//如设置返回报文的字符集,让下一步可成功执行????
    Document doc = soapPart.getEnvelope().getOwnerDocument();
	Source sourceContent=new DOMSource(doc);
	//Source sourceContent = msg.getSOAPPart().getContent();//方式二,无中文问题
	
	StringWriter output = new StringWriter();
	Transformer   transformer=TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
	transformer.transform(sourceContent, new StreamResult(output));
	//System.out.println(output.toString());
	return output.toString();
}

============CXF自己的
服务器-----------不用web服务器的,对使用@的对象,java规范标准处理 
	cxf-rt-transports-http-jetty-3.0.2.jar
	woodstox-core-asl-4.4.1.jar	
	stax2-api-3.1.4.jar
	
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
JaxWsServerFactoryBean factory=new JaxWsServerFactoryBean (); //CXF自己的类
factory.setServiceClass(HelloWorld.class);
factory.setServiceBean(new HelloWorldImpl());//实现
factory.setAddress("http://localhost:8080/HelloWorld");
factory.getInInterceptors().add(new LoggingInInterceptor());//会打印出收到客户端HTTP的请求信息
factory.getOutInterceptors().add(new LoggingOutInterceptor());//会打印出发送到客户端HTTP的响应信息
Server server=factory.create();	//CXF自己的类
//server.start();//可以不写
//测试用http://localhost:8080/HelloWorld?wsdl

客户端
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
JaxWsProxyFactoryBean factory =new JaxWsProxyFactoryBean(); //CXF自己的类
factory.setAddress("http://localhost:8080/HelloWorld");
factory.setServiceClass(HelloWorld.class);//接口
HelloWorld hello=(HelloWorld)factory.create();		//-----
System.out.println(hello.sayHello("lisi"));

//只在HelloWorld和HelloWorldImpl前加@WebService


addHandler(getHandles()) 

千万别调用Interceptor内部的成员变量,每个Interceptor都有可能运行在不同的线程中
InterceptorChain




POJO的服务,类可以不实现接口,可不加@ 
	IHelloWorld helloimpl =new HelloWorldImpl();//都是不加@的	
	ServerFactoryBean  server=new ServerFactoryBean(); //CXF自己的类
	server.setServiceClass(IHelloWorld.class);
	server.setAddress("http://localhost:9000/hello");
	server.setServiceBean(helloimpl);
	Server s=server.create();
		//s.start();//可以不加这个，可以对不加@的类 发布WebService
POJO的客户 ClientProxyFactoryBean factory=new ClientProxyFactoryBean();//CXF自己的类
		factory.setAddress("http://localhost:9000/hello");
		factory.setServiceClass(IHelloWorld.class);
		IHelloWorld hello=(IHelloWorld)factory.create();
		System.out.println(hello.sayHello("wang"));
  

=============Spring集成
---web.xml-----------
	<servlet>
		<servlet-name>CXFServlet</servlet-name>
		<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>CXFServlet</servlet-name>
		<url-pattern>/ws/*</url-pattern>   <!-- */-->
	</servlet-mapping>
	
	
package org.zh.cxf.spring;  
//client 和server必须是相同的包名
 @WebService  
 public interface HelloWorld 
 {
	 String sayHi(String text);  
	 public List<User> sayHitoUser(User user,User user2); 
 }
 
package org.zh.cxf.spring;
//client 和server必须是相同的包名
public class User
{	
	int id;
	String name;
	// getter,setter
}	
---------server-beans.xml
cxf-2.x.jar/schemas/jaxws.xsd,jaxrs.xsd
cxf-core-3.0.2.jar  的依赖
	cxf-rt-frontend-jaxws-3.0.2.jar
	cxf-rt-frontend-simple-3.0.2.jar
	cxf-rt-wsdl-3.0.2.jar
	cxf-rt-databinding-aegis-3.0.2.jar
	cxf-rt-bindings-soap-3.0.2.jar
	cxf-rt-transports-http-3.0.2.jar
	cxf-rt-databinding-jaxb-3.0.2.jar
	
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"  
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
       xmlns:jaxws="http://cxf.apache.org/jaxws"  
       xsi:schemaLocation=" http://www.springframework.org/schema/beans 
							http://www.springframework.org/schema/beans/spring-beans.xsd  
							http://cxf.apache.org/jaxws 
							http://cxf.apache.org/schemas/jaxws.xsd">
<!--  <import resource="classpath:META-INF/cxf/cxf-extension-soap.xml" />   新版本不必加这个-->     
	 <import resource="classpath:META-INF/cxf/cxf.xml" />  
      <import resource="classpath:META-INF/cxf/cxf-servlet.xml" />  
     
	 <!-- 这个应该是用于来做 非标准对象的  ,不必加@XmlType -->
       <bean id="aegisBean" class="org.apache.cxf.aegis.databinding.AegisDatabinding" />  
       <bean id="jaxWsServiceFactoryBean" class="org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean">  
          <property name="wrapped" value="true" />  
          <property name="dataBinding" ref="aegisBean" />  
       </bean>  
	    <!-- 测试使用  http://localhost:8080/J_CXF_Spring/ws -->
      <!-- 引用id使用# -->
      <bean id="hello" class="org.zh.cxf.spring.server.HelloWorldImpl" />  
       <jaxws:endpoint  implementor="#hello" address="/HelloWorld">  
         <jaxws:serviceFactory>  
             <ref bean="jaxWsServiceFactoryBean"/>  
         </jaxws:serviceFactory>
		  <jaxws:inInterceptors>
     	 	<bean class="org.apache.cxf.interceptor.LoggingInInterceptor"/>
    	</jaxws:inInterceptors>
		<jaxws:outInterceptors>
	       <bean  class="org.apache.cxf.interceptor.LoggingOutInterceptor"/>
	    </jaxws:outInterceptors>
		
      </jaxws:endpoint>  

	<!--  只标准的 -->
    <bean id="myServiceImpl" class="org.zh.cxf.spring.MyWebServcieImpl">
    </bean>
     <jaxws:server serviceClass="org.zh.cxf.spring.MyWebServcie"
                  address="/myWebServicee">
        <jaxws:features>
            <bean class="org.apache.cxf.feature.LoggingFeature"/>
        </jaxws:features>
        <jaxws:serviceBean>
            <ref bean="myServiceImpl"/>
        </jaxws:serviceBean>
    </jaxws:server> 
  </beans>  
  
 ----spring-client.xml
<?xml version="1.0" encoding="UTF-8"?>
<beans	xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		xmlns:jaxws="http://cxf.apache.org/jaxws"
		xsi:schemaLocation="http://www.springframework.org/schema/beans 
						http://www.springframework.org/schema/beans/spring-beans-2.0.xsd  
    					http://cxf.apache.org/jaxws 
    					http://cxf.apache.org/schema/jaxws.xsd">
	<bean id="aegisBean" class="org.apache.cxf.aegis.databinding.AegisDatabinding" />
	<bean id="serviceFactoryBean" class="org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean">
		<property name="dataBinding" ref="aegisBean" />
	</bean>

	<bean id="client" class="org.zh.cxf.spring.HelloWorld" factory-bean="clientFactory"
		factory-method="create" />
	
	<bean id="clientFactory" class="org.apache.cxf.frontend.ClientProxyFactoryBean">
		<property name="serviceFactory" ref="serviceFactoryBean" />
		<property name="serviceClass" value="org.zh.cxf.spring.HelloWorld" />
		<property name="address" value="http://localhost:8080/J_CXF_Spring/ws/HelloWorld" />
	</bean>
</beans>   
  
private static ClassPathXmlApplicationContext context;
@BeforeClass
public static void beforeClass()
{
	context = new ClassPathXmlApplicationContext(new String[] { "org/zh/cxf/spring/client/client-beans.xml" });
}
@AfterClass
public static void afterClass()
{
	context = null;
}
@Test
public void testSayHiWithSpringConfig() throws Exception
{
	HelloWorld client = (HelloWorld) context.getBean("client");
	String response = client.sayHi("Joe");
	assertEquals("Hello Joe", response);///OK
	
	User u=new User();
	u.setId(22);
	u.setName("lisi");
	
	User u2=new User();
	u2.setId(333);
	u2.setName("www");
	
	List<User> list=client.sayHitoUser(u,u2);
	System.out.println(list.get(0).getName());
	System.out.println(list.get(1).getName());
//	assertEquals("Hello [22,lisi][333,www]", client.sayHitoUser(u,u2));//OK
} 



spring 集成列子
---------测试类OK
public final class ClientTest 
{
	private static ClassPathXmlApplicationContext context;
	@BeforeClass
	public static void beforeClass()
	{
		context = new ClassPathXmlApplicationContext(new String[]	{ "demo/spring/client/client-beans.xml" });
	}
	@AfterClass
	public static void afterClass()
	{
		context = null;
	}
	@Test
	public void testSayHiWithSpringConfig() throws Exception
	{
		HelloWorld client = (HelloWorld) context.getBean("client");
		String response = client.sayHi("Joe");
		assertEquals("Hello Joe", response);
	}
}
---web.xml
<servlet>
    <servlet-name>CXFServlet</servlet-name>
    <servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>CXFServlet</servlet-name>
    <url-pattern>/ws/*</url-pattern> */
  </servlet-mapping>
  
-------------client-beans.xml
<beans xmlns="http://www.springframework.org/schema/beans"  
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
       xmlns:jaxws="http://cxf.apache.org/jaxws"  
       xsi:schemaLocation=" http://www.springframework.org/schema/beans 
							http://www.springframework.org/schema/beans/spring-beans-3.1.xsd    
							http://cxf.apache.org/jaxws 
							http://cxf.apache.org/schemas/jaxws.xsd">
	<bean id="aegisBean" class="org.apache.cxf.aegis.databinding.AegisDatabinding" />

	<bean id="serviceFactoryBean" class="org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean">
		<property name="dataBinding" ref="aegisBean" />
	</bean>

	<bean id="client" class="demo.spring.HelloWorld" factory-bean="clientFactory"  	factory-method="create" />

	<bean id="clientFactory" class="org.apache.cxf.frontend.ClientProxyFactoryBean">
		<property name="serviceFactory" ref="serviceFactoryBean" />
		<property name="serviceClass" value="org.zh.cxf.spring.HelloWorld" />
		<property name="address" 	value="http://localhost:8080/J_CXF_Spring/ws/HelloWorld" />
	</bean>
	
	<bean id="jaxMyServiceClient" class="org.zh.cxf.spring.MyWebServcie" factory-bean="jaxMyServiceClientFactory" factory-method="create"/>
	<bean id="jaxMyServiceClientFactory" class="org.apache.cxf.jaxws.JaxWsProxyFactoryBean">
		<property name="serviceClass" value="org.zh.cxf.spring.MyWebServcie"/>
		<property name="address" value="http://localhost:8080/J_CXF_Spring/ws/myWebService"/>
	</bean>
--------server-eans.xml
<beans xmlns="http://www.springframework.org/schema/beans"  
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
       xmlns:jaxws="http://cxf.apache.org/jaxws"  
       xsi:schemaLocation=" http://www.springframework.org/schema/beans 
							http://www.springframework.org/schema/beans/spring-beans-3.1.xsd    
							http://cxf.apache.org/jaxws 
							http://cxf.apache.org/schemas/jaxws.xsd"> 
     
	 <!--  <import resource="classpath:META-INF/cxf/cxf-extension-soap.xml" />   新版本不必加这个-->
       <import resource="classpath:META-INF/cxf/cxf.xml" />  <!--cxf.jar 中的 -->
       <import resource="classpath:META-INF/cxf/cxf-servlet.xml" />  
     
       <bean id="jaxWsServiceFactoryBean"  class="org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean">  
          <property name="wrapped" value="true" />  
          <property name="dataBinding" ref="aegisBean" />  
       </bean>  
    
       <bean id="aegisBean"  class="org.apache.cxf.aegis.databinding.AegisDatabinding" />  
    
        <!-- 方法1 -->
      <jaxws:endpoint   
          implementor="org.zh.cxf.spring.server.HelloWorldImpl" address="/HelloWorld">  
          <jaxws:serviceFactory>  
              <ref bean="jaxWsServiceFactoryBean"/>  
          </jaxws:serviceFactory>  
       </jaxws:endpoint>  
       
         <!-- 方法2  引用id使用# -->
       <bean id="hello" class="org.zh.cxf.spring.server.HelloWorldImpl" />  
        <jaxws:endpoint  
          implementor="#hello" address="/HelloWorld">  
          <jaxws:serviceFactory>  
              <ref bean="jaxWsServiceFactoryBean"/>  
          </jaxws:serviceFactory>  
       </jaxws:endpoint>  
	 
	 <!--  只标准的 -->
	<bean id="myServiceImpl" class="org.zh.cxf.spring.MyWebServcieImpl">
	</bean>
	<jaxws:server  serviceClass="org.zh.cxf.spring.MyWebServcie"  address="/myWebService">
	    <jaxws:features>
	        <bean class="org.apache.cxf.feature.LoggingFeature"/>
	    </jaxws:features>
	    <jaxws:serviceBean>
	        <ref bean="myServiceImpl"/>
	    </jaxws:serviceBean>
	</jaxws:server>
		
	
  </beans>  
 
 ---
  package org.zh.cxf.spring;  
 @WebService  
 public interface HelloWorld 
 {  
	String sayHi(String text);  
 } 
---
@WebService(endpointInterface = "org.zh.cxf.spring.HelloWorld")
public class HelloWorldImpl implements HelloWorld
{
	public String sayHi(String text)
	{
		return "Hello " + text;
	}
}


//==spring集成
服务端用<jaxws:endpoint implementor="#otherBeanID" 或者用org.zz

client端可以用<bean>也可以用
	也可以用 <jaxws:client id="" serviceClass="test.IHello" address="http://localhost:8080/xx" />



//==客户端异步调用

String payload=
		"<ns1:sayHello xmlns:ns1='http://zh.org/'>" +
			"<arg0>X</arg0>"+
		"</ns1:sayHello>";

	QName serviceName=new QName("http://zh.org/","HelloWorldImplService");
	javax.xml.ws.Service service=javax.xml.ws.Service.create(new URL("http://127.0.0.1:8000/myHelloWorld?wsdl"),serviceName);
	QName portName =new QName("http://zh.org/","HelloWorldImplPort");//默认Port名，看 ?wsdl
	Dispatch<Source> dispatch=service.createDispatch(portName,Source.class,javax.xml.ws.Service.Mode.PAYLOAD);//PAYLOAD只发SOAP中的body,MESSAGE发整个SOAP
	
	Map<String, Object> requestContext = dispatch.getRequestContext();
	requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
			
	Source msg=new SAXSource(new InputSource(new StringReader(payload)));
	
	//Source source=dispatch.invoke(msg);//可用同步 invoke(msg);
//方法一 将来(Futrue) 异步
	Response<Source> responseSource= dispatch.invokeAsync(msg);
	
	Source response=responseSource.get();//会等侍
	Transformer transformer=TransformerFactory.newInstance().newTransformer();
	transformer.transform(response, new StreamResult(System.out));
			
//方法二  回调异步
	dispatch.invokeAsync(msg, new AsyncHandler<Source>()//返回Furture
	{
		public void handleResponse(Response<Source> res)
		{
			try {
				Source response= res.get();
				Transformer transformer=TransformerFactory.newInstance().newTransformer();
				transformer.transform(response, new StreamResult(System.out));
			} 
			 catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	 

//---
@WebServiceProvider()
@ServiceMode(value=Service.Mode.MESSAGE)
//PAYLOAD只发SOAP中的body,MESSAGE发整个SOAP
public class CalcPlusServiceProvider implements Provider<DOMSource> {
	@Resource
    protected WebServiceContext wsContext;
    public DOMSource invoke(DOMSource request) {
		  MessageContext mc = wsContext.getMessageContext();
          String path = (String)mc.get(Message.PATH_INFO);
          String query = (String)mc.get(Message.QUERY_STRING);//cxf的Message,  ?id=1234
          String httpMethod = (String)mc.get(Message.HTTP_REQUEST_METHOD);
	}
}
<jaxws:endpoint id="calcPlusService" implementor="net_cxf.CalcPlusServiceProvider" address="/calcPlus" />
//使用SOAPMessage方式发请求

//--本地启动方式  要  cxf-rt-bindings-xml-3.0.2.jar
Endpoint e = Endpoint.create(HTTPBinding.HTTP_BINDING, new CalcPlusServiceProvider());
String address = "http://localhost:9000/calcPlus";
e.publish(address);



----
@WebServiceRef

==================================CXF  RESTful Web Services   javax.ws.rs. 
javax.ws.rs-api-2.0.1.jar
cxf-rt-rs-client-3.0.2.jar
cxf-rt-frontend-jaxrs-3.0.2.jar

使用firfox 的 poster
 

//http://localhost:8080/J_Java_EE/rs/generator

@ApplicationPath("/rs") //URL的前缘,如为"/",http://localhost:8080/J_Java_EE/generator
public class ConfigApplication extends Application 
{
    public Set<Class<?>> getClasses() 
    {
        Set<Class<?>> set = new HashSet<>();
        set.add(GeneratorResource.class); //解析类带有@Path
		set.add(ResponseFilter.class); //implements ContainerResponseFilter
		set.add(ValidCharacterInterceptor.class);//implements ReaderInterceptor ,对方法JS有参数传入到Java方法String的参数时
		set.add(MessageWriter.class);//@Provider XX implements MessageBodyWriter<T> 如果返回的不是Response的自己定义类如何输出
        return set;
    }
    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{ put(JsonGenerator.PRETTY_PRINTING, true); }};  //表示匿名类exends HashMap 在构造器中调用 
    }
}

@Path("/generator")
public class GeneratorResource 
{
    @GET
    @Produces(MediaType.APPLICATION_JSON) //也可其它格式 , @Produces("text/html"
    public StreamingOutput doGet() {
        return new StreamingOutput() {
            public void write(OutputStream os) {
                //要输入JSON格式,可使用json API
            }
        };
    }
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void doPost(JsonObject structure) {
        System.out.println(structure);
    }
}


@GET
public void getMessage(@Suspended final AsyncResponse asyncResponse, final @HeaderParam("request-id") String requestId) 
{	//开线程
	asyncResponse.resume(  Response.ok().entity(message).header("request-id", responseWrapper.getId()).build()  );
	//asyncResponse.resume("Sent!");
}

public class ResponseFilter implements ContainerResponseFilter 
{
    private final AtomicReference<Date> date = new AtomicReference<Date>();
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException 
	{
        Date currentDate = new Date();
        final Date lastDate = date.getAndSet(currentDate);
        containerResponseContext.getHeaders().add("previous-response", lastDate == null ? "this is the first response":lastDate.toString());
        containerResponseContext.getHeaders().add("this-response", currentDate.toString());
    }
}
public class ValidCharacterInterceptor implements ReaderInterceptor
{
    @Override
    public Object aroundReadFrom(ReaderInterceptorContext readerInterceptorContext) throws IOException, WebApplicationException 
    {
        final InputStream originalInputStream = readerInterceptorContext.getInputStream();
        readerInterceptorContext.setInputStream(new InputStream() 
        {
            @Override
            public int read() throws IOException {
                boolean isOk;
                int b;
                do {
                    b = originalInputStream.read();
                    isOk = b == -1 || Character.isLetterOrDigit(b) || Character.isWhitespace(b) || b == ((int) '.');
                } while (!isOk);
                return b;
            }
        });
        try {
            return readerInterceptorContext.proceed();
        } finally {
            readerInterceptorContext.setInputStream(originalInputStream);
        }
    }
}

@Provider //javax.ws.rs.ext.Provider
public class MessageWriter implements MessageBodyWriter<Message> {
    public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType) {
        return clazz == Message.class;
    }
    public long getSize(Message message, Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType) {
        return -1;
    }
    public void writeTo(Message message, Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream ostream) throws IOException, WebApplicationException {
        ostream.write(message.toString().getBytes());
    }
}

@Path("{msgNum}")//像Spring的 @PathVariable
@GET
public Message getMessage(@PathParam("msgNum") int msgNum)
{
}

@Context
private UriInfo ui;
URI msgURI = ui.getRequestUriBuilder().path("11").build();//在请求路径后加 /11
Response.created(msgURI).build();

---代码启服务
	JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
	sf.setResourceClasses(CustomerService.class);
	sf.setResourceProvider(CustomerService.class,   new SingletonResourceProvider(new CustomerService()));
	sf.setAddress("http://localhost:9000/");  //也可以是ws:  ,如是https:还要做其它工作
	sf.create();

//如路径为/customerservice/orders/223/products/323
//返回Order类配置了@XmlRootElement ,有方法配置了@Path("products/{productId}/")
//如服务端没有加  @Produces 返回类型 ,请求时加HTTP头    "Accept" 信息  text/xml,application/xml 服务端自动适应
//@Produces(MediaType.APPLICATION_XML)  //TEXT_XML
@Path("/orders/{orderId}/")     //可以把@只配置在接口上
public Order getOrder(@PathParam("orderId") String orderId) {

}

@Produces(MediaType.TEXT_XML)
@GET
@Path("/getCustomerById/{id}/")
public Response getCustomerById(@PathParam("id") String id)
{
	Customer c = customers.get(idNumber);
	return Response.ok(c).build(); //返回Response,Customer有@XmlRootElement
}

@GET
@Path("/showAccept")
@Produces("text/*")
public String showHeaderAccept(@HeaderParam("Accept") String accept  )//接受http头
{
	System.out.println("----accept: " + accept);
	return accept;
}

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:jaxws="http://cxf.apache.org/jaxws"
	xmlns:jaxrs="http://cxf.apache.org/jaxrs"
	xmlns:cxf="http://cxf.apache.org/core" 
	xsi:schemaLocation=" http://www.springframework.org/schema/beans 
							http://www.springframework.org/schema/beans/spring-beans-4.1.xsd    
							http://cxf.apache.org/jaxws 
							http://cxf.apache.org/schemas/jaxws.xsd
							http://cxf.apache.org/jaxrs
							http://cxf.apache.org/schemas/jaxrs.xsd
							http://cxf.apache.org/core
      						http://cxf.apache.org/schemas/core.xsd   ">
							
<import resource="classpath*:META-INF/cxf/cxf.xml" />
<import resource="classpath*:META-INF/cxf/cxf-servlet.xml" />
<jaxrs:server  address="/">
	<jaxrs:serviceBeans>
		<bean class="jax_rs_spring2.HelloWorld" /> <!-- 类上有@Path("/hello"), 不支持@ApplicationPath("/chat")  -->
	</jaxrs:serviceBeans>
	
 
	 <jaxrs:features>
		 <cxf:logging/>
	</jaxrs:features>
	
	<jaxrs:providers>
		<bean class="org.apache.cxf.jaxrs.provider.JAXBElementProvider" /><!--  可选的  -->
		 <bean class="jax_rs_spring2.MyExceptionMapper"/>
	</jaxrs:providers>
		
</jaxrs:server>

public class MyExceptionMapper implements ExceptionMapper<Exception> {
    public Response toResponse(Exception exception) {
    	System.out.println("----in ExceptionMapper");
		exception.printStackTrace()
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
    }
}
支持OSGI,可以和SpringSecurity使用,也可以为websocket提供服务


----client 未测试 
Client client = ClientBuilder.newBuilder().newClient();
WebTarget target = client.target("http://localhost:8080/rs");
target = target.path("service").queryParam("a", "avalue");
 
Invocation.Builder builder = target.request();
Response response = builder.get();
//Book book = builder.get(Book.class);



--可 spring
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
SaleService service = JAXRSClientFactory.create("http://localhost:8080/v1", SaleService.class);
WebClient.client(service).accept(MediaType.APPLICATION_XML);// 一定需要
	 
		
---spring client
import org.springframework.web.client.RestTemplate;

<jaxws:client  id="myClient"  serviceClass="jax_rs_spring.SaleService" address="http://localhost:8080/v1" ></jaxws:client>					

---WADL = Web Application Description Language
wadl2java

==========上 ws.rs


 
