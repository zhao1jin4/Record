
=========================Spring Portlet 1
Spring portlet 只支持 JSR 168 (Portlet 1.0),而现在的是 JSR 286 (Portlet 2.0)

//可以使用eclipse集成pluto,要双击pluto->选择use tomcat installation->选择webapps目录,要在META-INF/建立contex.xml写<Context crossContext="true" />
//能否被pluto admin界面被检测到,是因为web.xml中<url-pattern>/PlutoInvoker/x 对应的org.apache.pluto.container.driver.PortletServlet
//lib不要加pluto中已经有的.jar包,刚启动,是点pluto admin界面进入登录界面的,可能有错误,再点一次就OK

是pluto中的jsp报错, 
java.lang.ClassCastException: org.springframework.web.servlet.support.JstlUtils$SpringLocalizationContext cannot be cast to java.lang.String
pluto-2.0.3\webapps\pluto\WEB-INF\themes\pluto-default-theme.jsp 未部的 2003-<fmt:formatDate value="${now}" pattern="yyyy"/>删除


只供编译使用
portlet-api_2.0_spec-1.0.jar
pluto-taglib-2.0.3.jar

--web.xml
<!-- 设定Spring的根上下文 -->
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>/WEB-INF/applicationContext.xml</param-value>
</context-param>
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
	
//把 PortletRequest/PortletResponse 转换到 HttpServletRequest/HttpServletResponse
<servlet>
	<servlet-name>ViewRendererServlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.ViewRendererServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>ViewRendererServlet</servlet-name>
	<url-pattern>/WEB-INF/servlet/view</url-pattern>
</servlet-mapping>

<servlet>
	<servlet-name>SpringTestPortlet1</servlet-name>
	<servlet-class>org.apache.pluto.container.driver.PortletServlet</servlet-class>
	<init-param>
		<param-name>portlet-name</param-name>
		<param-value>SpringTestPortlet1</param-value><!-- 除了portlet.xml中相同外,还要和<url-pattern>/PlutoInvoker/后面部分也要相同 -->
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>SpringTestPortlet1</servlet-name>
	<url-pattern>/PlutoInvoker/SpringTestPortlet1</url-pattern> <!-- 如使用Pluto2.0.3 必须以/PlutoInvoker/开头-->
</servlet-mapping>


---portlet.xml 
<portlet-app
    xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
    version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd
                        http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd">
<portlet>
		<portlet-name>SpringTestPortlet1</portlet-name>
		<display-name>SpringTestPortlet1</display-name>
		<portlet-class>org.springframework.web.portlet.DispatcherPortlet</portlet-class> <!--Spring的-->
		<init-param>
			<name>contextConfigLocation</name>		//该文件中配置的bean只对这个portlet可见
			<value>/WEB-INF/springtest-portlet1.xml</value>	<!-- 默认是/WEB-INF/下的[portlet-name]-portlet.xml, -->
		</init-param>
		<supports>
			<mime-type>text/html</mime-type>
			<portlet-mode>view</portlet-mode>
			<portlet-mode>edit</portlet-mode>
			<portlet-mode>help</portlet-mode>
		</supports>
		<portlet-info>
			<title>SpringTestPortlet1</title>
		</portlet-info>
	</portlet>
</portlet-app>

--spring.xml
<bean id="defaultExceptionHandler" class="org.springframework.web.portlet.handler.SimpleMappingExceptionResolver">
	<property name="order" value="10" />
	<property name="defaultErrorView" value="error" />
	<property name="exceptionMappings">
		<props>
			<prop key="javax.portlet.UnavailableException">unavailable</prop>
			<prop key="java.lang.Exception">error</prop>
		</props>
	</property>
</bean>
<bean class="org.springframework.web.portlet.handler.PortletModeHandlerMapping">
	<property name="portletModeMap">
		<map><!-- key为当前的portlet模式（比如：'view', 'edit', 'help'）  -->
			<entry key="view" value-ref="myFormController" /><!--   myFormController -->
		</map>
	</property>
</bean>
 
<!-- 必须 使用 portletMultipartResolver 做id或name --> 
<bean id="portletMultipartResolver" class="org.springframework.web.portlet.multipart.CommonsPortletMultipartResolver">
	<property name="maxUploadSize" value="167772160"/><!-- 20M 以byte为单位-->
</bean>

@Controller
@RequestMapping("view")//只Annotation方式 
//@Resource(name="myFormController")
public class MyFormController 
{ 
	@InitBinder
	protected void initBinder(PortletRequest request,PortletRequestDataBinder binder) throws Exception 
	{
		binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));//日期格式 
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());//对图片是byte[]的附件做上传
	}
	//---------
	@RenderMapping
	public String search(RenderRequest renderRequest,RenderResponse renderResponse, Model model) 
	{
		return "anno/myform";
	}
	
	@ActionMapping
	public void submitForm(ActionRequest actionRequest,ActionResponse actionResponse, 
			Model model, @ModelAttribute("myform") MyForm myForm) throws Exception
	{
		Date birthday=myForm.getBirthday();
		System.out.println(birthday);
		
		//对图片是byte[]的附件做上传
		if(myForm.getByteFile().length>0)
		{
			System.out.println("FileUploadBean:Length===" + myForm.getByteFile().length);
			byte[] buff = myForm.getByteFile();
			FileOutputStream out = new FileOutputStream(new File("c:/temp/ByteFile.upload"));//原始文件名是??
			out.write(buff);
			out.close();
		}
		
		//------------------------------------------
		if(myForm.getMultiFile().getSize()>0)
		{
			//对于MultipartFile的方式
//			actionRequest.setCharacterEncoding("UTF-8");//没用的
			String filename=myForm.getMultiFile().getOriginalFilename();//中文文件名OK
			byte[] buff=new byte[1024];
			FileOutputStream out=new FileOutputStream(new File("c:/temp/"+filename));
			 InputStream  input=myForm.getMultiFile().getInputStream(); 
			  while(input.read(buff)!=-1) 
			  {
				  out.write(buff); 
			  }
			  out.close();
			  input.close(); 
		}

		  //对于List<MultipartFile>
		 for( MultipartFile photo:myForm.getPhotos()) 
		 {
			 if(photo.getSize()>0)
			 {
				 byte[] buff=new byte[1024];
				 FileOutputStream out=new FileOutputStream(new File("c:/temp/"+photo.getOriginalFilename()));
				 InputStream input=photo.getInputStream(); 
//				 photo.getContentType();
//				 photo.getSize();
				 while(input.read(buff)!=-1) 
				  {
					  out.write(buff); 
				  }
				 out.close();
				 input.close(); 
			 }
		 }
		 actionResponse.setRenderParameter("action", "resultRender");//像是链式服务端重定向
	}
	@RenderMapping(params = "action=resultRender")
	public String toAddUserPage(RenderRequest renderRequest,
			RenderResponse renderResponse) {
		return "anno/result";
	}
	
	@ActionMapping(params = "action=deleteAction")
	public void deleteByID(ActionRequest actionRequest,ActionResponse actionResponse, 
			Model model, @RequestParam(value = "id") String id) throws Exception
	{
		System.out.println("得到:ID="+id);
	}
}

<portlet:renderURL var="resultRender">
	<portlet:param name="action" value="resultRender" />
</portlet:renderURL>
<a href="${resultRender}">resultRender</a>


<portlet:actionURL var="deleteAction">
	<portlet:param name="action" value="deleteAction" />
	<portlet:param name="id" value="123" />
</portlet:actionURL>
<a href="${deleteAction}">deleteAction</a>
	
<!-- name="myform"  commandName="myform"   modelAttribute="myform 都OK-->
<form method="post" action="<portlet:actionURL/>" enctype="multipart/form-data" modelAttribute="myform" >

每个 DispatcherPortlet 都有自己的 WebApplicationContext
接口org.springframework.web.portlet.mvc.Controller
{	void handleActionRequest(request,response) //动作阶段处理动作请求
	ModelAndView handleRenderRequest(request,response) //显示阶段应该处理显示请求，并返回合适的模型和视图
}

Spring 针对 JSR-168 Portlet 新增了 globalSession 和 session 两种 bean scope
<bean id="globalSessionTestBean" class="springportal.bean.TestBean"  scope="globalSession" />//仅仅在基于 portlet 的 Web 应用中才有意义


--jsp
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<portlet:actionURL var="actionURL" />
<form action="${actionURL}"   >
<%--不能用<form:form>因为路径不是以/pluto开头,而是自己的项目开头
<form commandName="addressBook" action="${actionURL}"  >
 --%>
 
<a href="<portlet:renderURL portletMode="view" windowState="normal"/>">-Home -</a>
 
  
<bean id="my" class="spring_portlet.MyAbstractController">
	<property name="cacheSeconds" value="20"/> <!-- 1表示表示不改变缺省的缓存,0不缓存结果 -->
	<property name="requireSession" value="true"/>
	<property name="synchronizeOnSession" value="true"/>
	<property name="renderWhenMinimized" value="true"/>
</bean>

<bean id="wrappingController" class="org.springframework.web.portlet.mvc.PortletWrappingController">
	<property name="portletClass" value="sample.MyPortlet"/>
	<property name="portletName" value="my-portlet"/>
	<property name="initParameters">
		<value> config=/WEB-INF/my-portlet-config.xml</value>
	</property>
</bean>

请求参数来控制映射。这个参数的缺省名是 'action'，可以通过 'parameterName' 属性来改变。
<bean id="parameterHandlerMapping" class="org.springframework.web.portlet.handler.ParameterHandlerMapping"/>
	<property name="parameterName" value="action"/> 
    <property name="parameterMap">
        <map>
            <entry key="add" value-ref="addItemHandler"/>
            <entry key="edit" value-ref="editItemHandler"/>
            <entry key="delete" value-ref="deleteItemHandler"/>
        </map>
    </property>
</bean>

<bean id="portletModeParameterHandlerMapping" class="org.springframework.web.portlet.handler.PortletModeParameterHandlerMapping">
    <property name="portletModeParameterMap">
        <map>
            <entry key="view"><!-- 'view' portlet模式 -->
                <map>
                    <entry key="add" value-ref="addItemHandler"/>
                    <entry key="edit" value-ref="editItemHandler"/>
                    <entry key="delete" value-ref="deleteItemHandler"/>
                </map>
            </entry>
            <entry key="edit"><!-- 'edit' portlet模式 --> 
                <map>
                    <entry key="prefs" value-ref="prefsHandler"/>
                    <entry key="resetPrefs" value-ref="resetPrefsHandler"/>
                </map>
            </entry>
        </map>
    </property>
</bean>
 
 
----------只用annotation配置失败????

<!-- 对只能Annotation配置-->
<mvc:annotation-driven /> <!-- 可不要 -->
 

@Controller
@RequestMapping("view")//对只能Annotation配置
public class LeftPortletController
{
	@RenderMapping
	public String defaultPage(RenderRequest renderRequest,
			RenderResponse renderResponse, Model model) {
		return "Left";
	}
}
=========================上 Spring Portlet 1
