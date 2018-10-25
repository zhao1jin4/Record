JavaEE 6 API,tutorial

JSF-2.2 使用Apache的MyFaces来做
-----web.xml
  <context-param>
    <param-name>javax.faces.CONFIG_FILES</param-name>
    <param-value>/WEB-INF/faces-config.xml</param-value><!-- 默认值 -->
  </context-param>
  <servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
    <load-on-startup>0</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>*.jsf</url-pattern>
  </servlet-mapping>
  
  <context-param>
    <param-name>facelets.BUILD_BEFORE_RESTORE</param-name>
    <param-value>true</param-value>
  </context-param>
  
  
<!--
  <listener>
    <listener-class>org.apache.myfaces.webapp.StartupServletContextListener</listener-class>
  </listener>
  
  
  <filter>
    <filter-name>MyFacesExtensionsFilter</filter-name>
    <filter-class>org.apache.myfaces.component.html.util.ExtensionsFilter</filter-class>
    <init-param>
      <param-name>maxFileSize</param-name>
      <param-value>20m</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>MyFacesExtensionsFilter</filter-name>
    <url-pattern>*.jsf</url-pattern>
  </filter-mapping>
-->
 

sun的实现 没有<listener>和<filter>
---------faces-config.xml
<faces-config
    xmlns="http://java.sun.com/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd"
    version="2.0">
	<managed-bean>
		<managed-bean-name>student</managed-bean-name>
		<managed-bean-class>org.zh.jsf.vo.Student</managed-bean-class>
		<managed-bean-scope>session</managed-bean-scope><!-- request,session -->
		<managed-property><!-- 这里也可以设置属性 -->
			<property-name>loginId</property-name>   
			<property-class>java.lang.String</property-class>
			<value>lisi</value>
			<!--
			<map-entries>
				<key-class></key-class>
				<value-class></value-class>
				<map-entry>
					<key></key>
					<value></value>
				</map-entry>
				<map-entry>
					<key></key>
					<value></value>
				</map-entry>
			</map-entries>
			<list-entries>
				<value-class></value-class>
				<value></value>
				<value></value>
			</list-entries>
			  --> 
		</managed-property>
	</managed-bean>
	<navigation-rule>
		<from-view-id>/jsp/hello.jsp</from-view-id>
		<navigation-case>
			<from-action>#{student.login}</from-action>
			<from-outcome>success</from-outcome>
			<to-view-id>/jsp/welcome.jsp</to-view-id>
		</navigation-case>
	</navigation-rule>
</faces-config>

<%@taglib prefix="ui" uri="http://java.sun.com/jsf/facelets"%>
 
hello.jsp 
	<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
	<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
	<f:view>
		<h:form>
			<h3>请输入用户名</h3>
			<h:inputText value="#{student.name}"/>
			<h:commandButton  action="success"/>
		</h:form>
	</f:view>

访问时 hello.jsf方式来访问jsp页面(也就是说.jsp文件不能放在/WEB-INF下)


welcome.jsp
	<h:outputText value="#{student.name}"></h:outputText> 欢迎你!

报错No saved view state could be found for the view identifier:的原因就
是得新启动服务器后不要按F5来刷新,要在地址栏按回车,虽然地址看上去一样,但不一样,


//国际化修改时注意:有五个地方配置,1.properties文件名,2.界面,3.faces-config.xml,4.Converter类,5.实体类
//英文应该只用en,而不使用en_US,

<converter>
	<converter-id>LocaleConverter</converter-id>
	<converter-class>org.zh.jsf.converter.LocaleConverter</converter-class>
</converter>
<application>
	<locale-config>
		<default-locale>en</default-locale>
		<supported-locale>zh_CN</supported-locale>
	</locale-config>
	<message-bundle>org.zh.jsf.resource.message</message-bundle>
</application>
<validator>
	<validator-id>PasswordValidator</validator-id>
	<validator-class>org.zh.jsf.validator.PasswordValidator</validator-class>
</validator>

public class PasswordValidator implements Validator 
{
	public void validate(FacesContext facesContext, UIComponent uicomponent, Object passwordobj)throws ValidatorException 
	{
       String password=(String)passwordobj;
       if(password.length()<6)
       {
    	   Application application=facesContext.getApplication();
    	   String messageFileName=application.getMessageBundle();
    	   Locale locale=facesContext.getViewRoot().getLocale();
    	   ResourceBundle  resourceBundle=ResourceBundle.getBundle(messageFileName,locale);
    	   String passwordValidatorError=resourceBundle.getString("PassworValidator");
    	   FacesMessage errorMessage=new FacesMessage(FacesMessage.SEVERITY_ERROR,passwordValidatorError,passwordValidatorError);
           throw new ValidatorException(errorMessage);
       }
	}
}
public class LocaleConverter implements Converter
{
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException
	{
		Locale locale = new Locale(Constant.EN);
		if (value.equals(Constant.EN))
			return locale;
		else if (value.equals(Constant.ZH))
		{
			locale = new Locale(Constant.ZH);
			return locale;
		}
		return locale;
	}
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException
	{
		if (value.toString().equals(Constant.EN))
			return Constant.EN;
		else if (value.toString().equals(Constant.ZH))
			return Constant.ZH;
		return Constant.EN;
	}
}

public class LocaleValueChangeListener implements ValueChangeListener 
{
	public void processValueChange(ValueChangeEvent event) throws AbortProcessingException 
	{
       System.out.println("LocaleValueChanger::processValueChange调用了");
	}
}
public class LoginListener implements ActionListener
{
	public void processAction(ActionEvent event) throws AbortProcessingException 
	{
        System.out.println("LoginListener::processAction 被调用了");
	}
}

<h:from id="myform" >
	<h:panelGrid columns="3" border="1" cellspacing="2" cellpadding="1">
	
		<h:inputText id="age"   value="#{student.age}">
			<f:validateLongRange minimum="20" maximum="80"/>
		</h:inputText>
		<h:message for="age" style="color:red"></h:message> <!-- 会自己接收JSF生成的类型转换错误 -->
		
		<h:inputSecret id="password" required="false" rendered="true" value="#{student.password}">
			<f:validator validatorId="PasswordValidator"/>
		</h:inputSecret><!-- required="false" 如为true,更新国际化无效 -->
		
		<h:commandButton  value="#{msg.login}" action="#{student.login}">
			<f:actionListener type="org.zh.jsf.listener.LoginListener"/> <!-- 可配置多个 -->
		</h:commandButton>
		
		<h:selectOneMenu value="#{student.locale}">
			<f:selectItem  itemValue="en" itemLabel="#{msg.Enghlis}"/>
			<f:selectItem  itemValue="zh_CN" itemLabel="#{msg.Chinese}"/>
			<f:converter converterId="LocaleConverter"/>
			<f:valueChangeListener type="org.zh.jsf.listener.LocaleValueChangeListener"/>
		</h:selectOneMenu>
	</h:panelGrid>
	
-----------------spring 集成JSF
在faces-config.xml中
	<application>
		<el-resolver>org.springframework.web.jsf.el.WebApplicationContextFacesELResolver</el-resolver> 
		<!--el-resolver是对JSF-2.1,如JSF1.x 是variable-resolver标签,值为org.springframework.web.jsf.DelegatingVariableResolver-->
	</application>
	
使用Spring
ApplicationContext context=FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
context.getBean("xx");

	
	
FacesContext facesContext=FacesContext.getCurrentInstance();
ResponseStream response= facesContext.getResponseStream();
ELContext elContext=facesContext.getELContext();

ExternalContext externalContext= facesContext.getExternalContext();
externalContext.getRealPath("/");//D:\program\eclipse_java_workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\J_JSF-JPA-Spring\
externalContext.getRequestContextPath();// /J_JSF-JPA-Spring
externalContext.getResourcePaths("/WEB-INF/");

HttpServletRequest request=(HttpServletRequest)externalContext.getRequest(); //可以得到Servlet的Request
HttpSession session=(HttpSession)externalContext.getSession(false);

externalContext.getContext();
externalContext.getSessionMap();
externalContext.getApplicationMap();
externalContext.getInitParameterMap();
externalContext.getRequestParameterMap();
externalContext.getRequestHeaderMap();
externalContext.getRequestCookieMap();
externalContext.getRequestLocale();

externalContext.setResponseContentType(contentType);
externalContext.setResponseContentLength(length)
externalContext.setRequestCharacterEncoding(encoding)
externalContext.setResponseHeader(name, value);

externalContext.encodeResourceURL("");
externalContext.dispatch("");



facesContext.addMessage("passwordError", errorMessage);//用<h:messages></h:messages>可以接收


<h:selectOneRadio layout="pageDirection">		<!-- layout="pageDirection" 坚向 <input type="radio"> -->
<h:selectManyCheckbox layout="pageDirection">	<!--  <input type="checkbox"> -->
<h:selectBooleanCheckbox/>

<h:selectOneMenu> 		<!-- <select size="1" multiple="none"><option> -->
<h:selectOneListbox>	<!-- <select size="5" multiple="none"><option> -->
<h:selectManyListbox>	<!-- <select size="5" multiple="multiple"><option> -->
<h:selectManyMenu>		<!-- <select size="1" multiple="multiple" ><option> -->


HtmlSelectOneListbox listbox=new HtmlSelectOneListbox();//对应  <h:selectOneListbox>


<h:selectOneRadio id="teacher" immediate="true" > 			
	<f:selectItems value="#{teacher.allTeacherArray}"/>     对应  public SelectItem[] getAllTeacherArray()
</h:selectOneRadio> 	

<h:selectManyListbox id="courses"><!-- <select size="5" multiple="multiple"><option> -->  可加 value="#{course.selected}"  对应 public List<String> getSelected()
	<f:selectItems value="#{course.allCourseList}"/>		 对应  public List<SelectItem> getAllCourseList()
</h:selectManyListbox>



//表单中特殊列
ExternalContext eContext=FacesContext.getCurrentInstance().getExternalContext();
Map<String,String> param=eContext.getRequestParameterMap();
Map<String,String[]> params=eContext.getRequestParameterValuesMap();
String teacher=	param.get( "myform:teacher");//没有setter方法也可以
String[] courses=params.get( "myform:courses");//<h:form id="myform">	<h:selectManyListbox id="courses">
		

<h:inputText id="birthDay"   value="#{student.birthDay}"> 
	<f:convertDateTime pattern="yyyy-MM-dd"/>
</h:inputText>



temporaryDirectory = (File)request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
//work\Catalina\localhost\J_JSF-sun-upload

<h:graphicImage  url="../img/logo3w.png" alt="欢迎" height="95" >
	<f:attribute name="width" value="275"/>
</h:graphicImage>

<f:facet name="header" > <!-- header是关键字 ,生成thead标签 -->
	<h:outputText  value="标题 独占一行"/>
</f:facet>

<h:panelGroup>1</h:panelGroup><!--独占一个单元格 -->

<f:loadBundle basename="org.zh.jsf.resource.message" var="msg"/>
<h:outputText value="#{msg.username}:"/>  <h:outputText value="#{msg['username']}" />
 
<%---可用国际化消息,可以使用#{ }  --%>
<h:outputFormat value="你有{0}个未处理的邮件">
	<f:param value="三"></f:param>
</h:outputFormat>
		
		
		
		
<%--
<h: dataTable> 可以列举的对象包括：array, List, java.sql.ResultSet, javax.servlet.jsp.jstl.sql.Result, javax.faces.model.DataModel（抽象类）。更复杂的表格，可以通过重构DataModel来实现， 
 --%>

ListDataModel<PerInfo>  listModel=new ListDataModel<PerInfo>();
listModel.setWrappedData(userList); 
ArrayDataModel


getperInfoAll 中的类必须是 public class的
<h:dataTable id="dt1" value="#{tagBean.perInfoAll}" var="item"
	bgcolor="#F1F1F1" border="10" cellpadding="5" cellspacing="3"
	width="50%" dir="LTR" frame="hsides" rules="all"
	summary="This is a JSF code to create dataTable."
	
	first="0"  rows="4"  
	rowClasses="oddColumn,evenColumn"
	>
	<f:facet name="header">
		<h:outputText value="This is 'dataTable' demo" />
	</f:facet>
	<h:column>
		<f:facet name="header">
			<h:outputText value="name" />
		</f:facet>
		<h:outputText value="#{item.name}"></h:outputText>
	</h:column>
	<h:column>
		<f:facet name="header">
			<h:outputText value="male" />
		</f:facet>
		<h:selectBooleanCheckbox  value="#{item.male}"/>
		<h:outputText value="#{item.male}"></h:outputText>
	</h:column>
	<f:facet name="footer">
		<h:outputText value="表尾,可分页吗？" />
	</f:facet>
</h:dataTable>		
		

<%--  总是报 java.lang.NullPointerException: charsetName  
	<h:outputLink value="myUrl" charset="gbk" >
    	<f:param name="id" value="100" ></f:param>
    	<f:param name="t" value="200" ></f:param>
    	跳到
    </h:outputLink>
--%>

登录验证
public class PermissionPhraseLister implements PhaseListener {

	public void afterPhase(PhaseEvent event) 
	{
		FacesContext context = event.getFacesContext();
		
		String viewId=context.getViewRoot().getViewId();// 是 /jsp/testTag.jsp 或/jsp/login.jsp
		boolean isloginPage = viewId.lastIndexOf("login") > -1 ? true : false;
		
		boolean isLoggedIn=false;
		Object loginId=context.getExternalContext().getSessionMap().get("loginId");
		if (loginId!=null)
			isLoggedIn=true;
		
		if (!isloginPage && !isLoggedIn)
		{
			NavigationHandler navigation = context.getApplication().getNavigationHandler();
			navigation.handleNavigation(context, null, "logout");//<from-outcome>logout
		}
	}
	public void beforePhase(PhaseEvent event) {
	}
	public PhaseId getPhaseId() 
	{
		   return PhaseId.RESTORE_VIEW;
	}
}
faces-config.xml
<navigation-rule>
  <from-view-id>*</from-view-id>
    <navigation-case>
      <from-outcome>logout</from-outcome>
      <to-view-id>/jsp/login.jsp</to-view-id>
    </navigation-case>
</navigation-rule>
<lifecycle>
	<phase-listener>org.zh.jsf.listener.PermissionPhraseLister</phase-listener>
</lifecycle>


自定义标签错误消息，使用.jar中的文件
myfaces-api-2.1.8.jar\javax.faces.Message_en.properties
myfaces-api-2.1.8.jar\javax.faces.Message_zh_CN.properties


private UIInput textInput=null;
public String clickButton()
{
	textInput.setValue("哈哈,这是Binding更新之后的值");
	return null;
} 
<h:form>
	<h:inputText value="binding更新前的值" binding="#{tagBean.textInput}"/>
	<h:commandButton action="#{tagBean.clickButton}" value="测试binding"/>
</h:form>

 <h:commandLink action="courseAdd.faces" >
	<f:verbatim>增加新课程</f:verbatim>
</h:commandLink>
 
LifecycleFactory factory= (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
Lifecycle lifecycle =factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
lifecycle.addPhaseListener(...)

<f:phaseListener type="org.zh.jsf.listener.TagPhraseLister"/>
 
panelGroup 标签生成一个 <div> 或 <span>
panelGrid 标签生成<table><tr><td>


所有标签的属性
binding         所有组件            绑定至UIComponent
rendered        所有组件            是否显示组件
immediate      输入、命令组件       是否为即时事件 



faces-config.xml中未用到的
	<component></component>
	<render-kit> </render-kit>
	<referenced-bean></referenced-bean>

	
JSF框架中定义了六个phrase：
PhaseId.RESTORE_VIEW,
PhaseId.APPLY_REQUEST_VALUES, 
PhaseId.PROCESS_VALIDATIONS,
PhaseId.UPDATE_MODEL_VALUES,
PhaseId.INVOKE_APPLICATION,
PhaseId.RENDER_RESPONSE
		 




=======================================Trinidad 文件上传	
使用MyFaces 的UI-Component Sets中的 Trinidad 项目,支持JSF2.0  

//修改名为faces
<servlet-name>faces</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
</servlet>

//新增
<filter>
    <filter-name>trinidad</filter-name>
    <filter-class>org.apache.myfaces.trinidad.webapp.TrinidadFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>trinidad</filter-name>
	<servlet-name>faces</servlet-name>
</filter-mapping>
<context-param>
	<!-- Maximum memory per request (in bytes) -->
	<param-name>org.apache.myfaces.trinidad.UPLOAD_MAX_MEMORY</param-name>
	<!-- Use 500K -->
	<param-value>512000</param-value>
</context-param>
<context-param>
	<!-- Maximum disk space per request (in bytes) -->
	<param-name>org.apache.myfaces.trinidad.UPLOAD_MAX_DISK_SPACE</param-name>
	<!-- Use 5,000K -->
	<param-value>5120000</param-value>
</context-param>
<context-param>
	<param-name>org.apache.myfaces.trinidad.UPLOAD_TEMP_DIR</param-name>
	<param-value>c:/temp/TrinidadUploads/</param-value>
</context-param>

<servlet>
	<servlet-name>resources</servlet-name>
	<servlet-class>org.apache.myfaces.trinidad.webapp.ResourceServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>resources</servlet-name>
	<url-pattern>/adf/*</url-pattern>   */
</servlet-mapping>
 <!-- 一定要加这个Servlet,否则JS报 _submitFormCheck is not defined -->
 
  
  faces-config.xml中
 <application>
    <default-render-kit-id>org.apache.myfaces.trinidad.core</default-render-kit-id>
 </application>
 <!-- 一定要加,否则<tr:inputFile 不能显示出来  -->
  
<%@taglib prefix="tr" uri="http://myfaces.apache.org/trinidad"%>
<!--表单中的文件,如果有其它表单验证失败,文件中的路径消失,多个文件不能同时上传????????-->
<h:form  enctype="multipart/form-data" > <!--没有设置method,默认为method="post" -->
	<tr:inputFile label="属性文件:"  	value="#{student.uploadFile}" />
	<h:commandButton value="上传" action="#{student.upload}"></h:commandButton>
</h:form>


org.apache.myfaces.trinidad.model.UploadedFile uploadFile;
	
public String upload()//测试 OK
{
	System.out.println("name====:"+this.name);//这里是可以得到其它表单的值的

	inputStream input=this.uploadFile.getInputStream();
	FileOutputStream output;
	output = new FileOutputStream(new File("d:/temp/"+this.uploadFile.getFilename()));
	byte[] buffer=new byte[1024*10];
	int len=0;
	while((len=input.read(buffer))!=-1 )
	{
		output.write(buffer,0,len);
	}
	output.close();
	input.close();
}


<h:form enctype="multipart/form-data" >
	只适用于form中没有要验证的其它表单,只可一个一个的上传<br/>
	<tr:inputFile label="监听文件1:"   valueChangeListener="#{backingFileUpload.upload}"/>
	<h:commandButton value="后台上传这个文件"/>    
</h:form>	

//只是个类,没有实现任何类
public void upload(ValueChangeEvent event)//要带ValueChangeEvent参数
{
	UploadedFile file = (UploadedFile) event.getNewValue();
	FacesContext context = FacesContext.getCurrentInstance();
	FacesMessage message = new FacesMessage( "成功上传文件:" + file.getFilename()+ "共(" + file.getLength() + "字节)");
	context.addMessage(event.getComponent().getClientId(context), message);
	InputStream input =	file.getInputStream();
	FileOutputStream output = new FileOutputStream("d:/temp/"+file.getFilename());
	byte[] buffer=new byte[1024*10];
	int len=0;
	while((len=input.read(buffer))!=-1)
	{
		output.write(buffer, 0, len);
	}
	input.close();
	output.close();
}

=======================================portlet-bridge 的 Myfaces实现
Java Specification Request(JSR)

apache的pluto 2.0.3 项目实现portlet 2 container规范 ,即JSR-286,JBoss 也有实现
JSR-168 是portlet 1的规范

Myfaces的子项目Portlet Bridge实现了 Portlet Bridge规范,即JSR-329,JSR-301 ,JBoss 也有实现
JSR-329: Portlet 2.0 Bridge for JavaServer Faces 1.2
JSR-301: Portlet 1.0 Bridge for JavaServer Faces 1.2

//可以使用eclipse集成pluto,要双击pluto->选择use tomcat installation->选择webapps目录,要在META-INF/建立contex.xml写<Context crossContext="true" />
//能否被pluto admin界面被检测到,是因为web.xml中<url-pattern>/PlutoInvoker/x 对应的org.apache.pluto.container.driver.PortletServlet
//lib不要加pluto中已经有的.jar包

---web.xml
<!-- 以下手工加的,为放在pluto2.0.3中使用(不可有portlet-api_2.0_spec-1.0.jar) -->
<servlet>
	<servlet-name>myBlank</servlet-name>
	<servlet-class>org.apache.pluto.container.driver.PortletServlet</servlet-class>
	<init-param>
		<param-name>portlet-name</param-name>
		<param-value>portlet-bridge-blank</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>myBlank</servlet-name>
	<url-pattern>/PlutoInvoker/portlet-bridge-blank</url-pattern>
</servlet-mapping>


文件 /WEB-INF/portlet.xml
<portlet-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"                
xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd
                    http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd" id="BridgeDemo" version="2.0">

<portlet>
    <description lang="EN">MyFaces Portlet Bridge Tempate Portlet</description>
    <portlet-name>portlet-bridge-blank</portlet-name>
    <display-name lang="EN">MyFaces Portlet Bridge Template</display-name>
    <portlet-class>javax.portlet.faces.GenericFacesPortlet</portlet-class>

    <init-param>
      <name>javax.portlet.faces.defaultViewId.view</name> 
      <value>/index.jsp</value>
    </init-param>
    
	<init-param>
	  <name>javax.portlet.faces.defaltViewId.edit</name>
	  <value>/edit.jsp</value>
	</init-param>

	<init-param>
	  <name>javax.portlet.faces.defaultViewId.help</name>
	  <value>/help.jsp</value>
	</init-param>
	
    <supports>
      <mime-type>text/html</mime-type>
	   <portlet-mode>edit</portlet-mode>
      <portlet-mode>view</portlet-mode>
      <portlet-mode>help</portlet-mode>
   </supports>

    <supported-locale>en</supported-locale>

    <portlet-info>
      <title>MyFaces Portlet Bridge Template</title>
      <short-title>Bridge Template</short-title>
    </portlet-info>
  </portlet>
  
</portlet-app>


<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>

<%--  还不知道怎么用呢
 <portlet:renderURL var="resultRender">
	<portlet:param name="action" value="resultRender" />
</portlet:renderURL>
<a href="${resultRender}">resultRender</a>


<portlet:actionURL var="deleteAction">
	<portlet:param name="action" value="deleteAction" />
	<portlet:param name="id" value="123" />
</portlet:actionURL>
<a href="${deleteAction}">deleteAction</a>
            
             --%>


	