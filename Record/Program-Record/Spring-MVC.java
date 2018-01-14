	
=========================Spring MVC
<servlet>
	<servlet-name>spring_mvc</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring_annotation.xml /WEB-INF/spring_freemarker.xml</param-value>
		<!--  多个用空格分隔,可用classpath:,或者spring_*.xml,如不配置默认是/WEB-INF/[servlet-name]-servlet.xml -->
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>spring_mvc</servlet-name>
	<url-pattern>*.mvc</url-pattern>
</servlet-mapping>

	<!-- Enables use of HTTP methods PUT and DELETE,默认参数名: _method -->
 	<filter>
		<filter-name>httpMethodFilter</filter-name>
		<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
	</filter> 
	
	  <!-- 权限 -->
	<listener>
	  <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class> <!--implements ServletRequestListener -->
	</listener>
	
	
1.contextConfigLocation 多个文件用逗号分隔, 如有相同bean  使用最后的
	默认会在/WEB-INF目录下找 [servlet-name]-servlet.xml配置文件
2.conetxtClass
	必须是WebapplicationContext 的实现类
	默认是XMLWebapplicationContext
3.namespace 
	默认是[servlet-name]-servlet

----annotation 的配置方式
   <context-param>
        <param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>
    <!-- 多个配置用逗号或空格分隔  标记 @Configuration 类 ,也可以是包名 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>com.acme.AppConfig</param-value>
    </context-param>
    <!-- Bootstrap the root application context  -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

 
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </init-param>
        <!-- 多个配置用逗号或空格分隔  标记 @Configuration 类 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.web.MvcConfig</param-value>
        </init-param>
    </servlet>
     <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/app/*</url-pattern>   */
    </servlet-mapping>

<%@taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" 		uri="http://java.sun.com/jstl/fmt"%>
<%@taglib prefix="spring"	uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>


public class HelloWorldController implements Controller
{
	protected final Log logger = LogFactory.getLog(getClass());
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		String nowx = (new java.util.Date()).toString();
		//return new ModelAndView("/helloWorld", "now", nowx);
		return new ModelAndView("helloWorld", "now", nowx);//可以不用加"/",对于使用UrlBasedViewResolver,找/WEB-INF/jsp/helloWorld.jsp
		//return new ModelAndView("/WEB-INF/jsp/helloWorld.jsp", "now", nowx);//对没有配置UrlBasedViewResolver的方式
	}
}

<bean  class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
	<property name="mappings">
		<value><!-- 使用 value,也可使用props-->
			/helloWorld.mvc				=helloWorldCntroller
			login.mvc					=loginController <!-- 可以不用加/ -->
			register.mvc				=enterRegisterController <!-- UrlFilenameViewController,找register名字做为view -->
			/delegateMulti/*			=delegateMulti 								*/
		</value>
	</property>
</bean>

<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver">
		<property name="viewClass" value="org.springframework.web.servlet.view.JstlView"></property>
		<property name="prefix" value="/WEB-INF/jsp/"></property>
		<property name="suffix" value=".jsp"></property>
</bean>
<bean id="helloWorldCntroller" class="spring_jsp.HelloWorldController" />
<bean  id="loginController"  class="org.springframework.web.servlet.mvc.ParameterizableViewController">
	<property name="viewName" value="login"></property> <!-- 可不用自己建立的Controller,而直接进入login.jsp页面的方式-->
</bean>
<bean  id="enterRegisterController"  class="org.springframework.web.servlet.mvc.UrlFilenameViewController"/>
<!-- 视图名  就是配置的URL的部分  -->
	

 
<!-- 只对特定的controller -->
<bean id="propertiesMethodNameResolver" class="org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver" >
	<property name="mappings">
		<props> <!-- 使用props也可使用 value -->
			<prop key="/showTeacherDetailById.mvc">queryTeacherDetailById</prop>
			<prop key="/showAllTeacher.mvc">queryAllTeacher</prop> 
		</props>
	</property>
</bean>
  
public class StudentMultiActionController extends MultiActionController
{
	//MultiActionController 方法格式 public (ModelAndView | Map | String | void) actionName(HttpServletRequest request, HttpServletResponse response, [,HttpSession] [,AnyObject]);
	public ModelAndView queryStudentDetailById(HttpServletRequest request,HttpServletResponse respnose) 
	{}
}
<bean id="internalPathMethodNameResolver" class="org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver" />
<!-- 根据请求路径名对应方法名-->	
<bean id="studentMultiActionController" class="spring_jsp.StudentMultiActionController">
<property name="methodNameResolver" ref="internalPathMethodNameResolver"/>
</bean>
	
	
<!--
	ControllerClassNameHandlerMapping初始化要在Controller之前
	对 InternalPathMethodNameResolver的Multi规则是 StudentMultiActionController 地址为studentmultiaction.mvc要全小写
	因为是Multi的所有要加/queryStudentDetailById,
	studentmultiaction/queryStudentDetailById.mvc?student_id=123,注意.mvc在最后一个
	ParameterMethodNameResolver不行的???????
-->
<bean  class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping">
</bean>

<!-- 对 MultiActionController 就不行了 -->
<bean  class="org.springframework.web.servlet.mvc.support.ControllerBeanNameHandlerMapping"/>
<bean name="helloWorldABC.mvc" class="spring_jsp.HelloWorldController" />
<!-- 也可以是 name="/helloWorldABC.mvc",就不用配置 ControllerBeanNameHandlerMapping ,应该是使用BeanNameUrlHandlerMapping-->


<bean id="paramMethodNameResolver"   class="org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver" >
	<property name="paramName" value="doMethod" /><!-- 根据doMethod参数 决定方法名-->
</bean>
<bean id="clazzMultiActionController" class="spring_jsp.ClazzMultiActionController">
	<property name="methodNameResolver" ref="paramMethodNameResolver"/>
</bean>

 
 <!-- 不用继承自MultiActionController,使用 delegate方式,默认是InternalPathMethodNameResolver-->
  <bean id="delegateMulti" class="org.springframework.web.servlet.mvc.multiaction.MultiActionController">
 	<property name="delegate">
 		 <bean class="spring_jsp.MyDelegateMulti"></bean>
 	</property>
 </bean>


<!--基于UrlBasedViewResolver,就不用指定viewClass-->
<bean  class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/jsp/" />
	<property name="suffix"  value=".jsp"/>
</bean>
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" 
				p:prefix="/WEB-INF/jsp/" p:suffix=".jsp"/><!-- p:表示property标签 -->
加了上面的就可以用
return new ModelAndView("hello");
否则用
return new ModelAndView("/WEB-INF/jsp/hello.jsp", "now", nowx);



<!-- 国际化 -->
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
	<property name="basename" value="messages"/> 
</bean>
 
<fmt:setBundle basename="message"/>
<fmt:setLocale value="zh_CN"/>

<fmt:message key="employee_query"/>
<spring:message code="employee_id"/>


web.xml中<error-page> 比Spring中的优先级要高
<bean id="exceptionResolver" class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
	<property name="defaultErrorView">
		<value>failure</value>
	</property>
	<property name="exceptionMappings">
		<props>
			<prop key="java.sql.SQLException">showDBError</prop>
			<prop key="java.lang.RuntimeException">showError</prop>
		</props>
	</property>
</bean>

<% Exception ex = (Exception)request.getAttribute("Exception"); %>



AcceptHeaderLocaleResolver
SessionLocaleResolver
CookieLocaleResolver
		包含了三个属性，cookieName、cookiePath 和cookieMaxAge


国际化时的资源文件中的key 
typeMismatch			 //找不表单名的,输入不能转换的数据
typeMismatch.percentage   //percentage 是表单名


setSessionForm(true) //在session保存表单,不是每次请求建一个表单对象,

setValidateOnBinding  (false)//是否在表单绑定时进行验证,false 使用下面方法验证
onBindAndValidate(HttpServletRequest request, Object command, BindException errors) 
{
	errors.setNestedPath("account")//原先account.firstname在验证时只给出firstname 
}
Map referenceData(...)//加数据向模型,以Map传递给视图 

ValidationUtils.rejectIfEmplty(Error,)
 
formBackingObject //方法 是打开页面,或者返回时调用 ,返回的是表单对应的commandClass,表单显示值
 
 public class MyPropertyEditor extends PropertyEditorSupport //表单的日期如何转换,也可以使用CustomDateEditor
{
	SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(! "".equals(text))
				super.setValue(this.dateFormat.parse(text));//调用setValue存
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getAsText() {
		Object o=getValue();//getValue调用出
		Date date=(Date)o;
		String str=dateFormat.format(date);
		return str;
	}
}  
//-------文件上传  下载
<form enctype="multipart/form-data>

 <!-- 必须是 id="multipartResolver"  DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME -->  
<bean  id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">  
	<property name="defaultEncoding" value="UTF-8"></property><!-- 中文文件名 OK 加了CharacterEncodingFilter-->
	<property name="maxUploadSize" value="10000000"/> <!--单位是 bytes  可能会抛 org.springframework.web.multipart.MaxUploadSizeExceededException (是RuntimeException)-->
</bean>
@InitBinder	//需要处理Date的时候,自动调用这个方法,对JSON无效
public void initBinder(WebDataBinder binder)//要用 WebDataBinder
{
	//binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());//对图片是byte[]的附件做上传
	
	//为了页面提交的字串可以转换为Date类
	binder.registerCustomEditor(Date.class,new  MyPropertyEditor());
//		binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
}

 
@RequestMapping(value="/submitUpload",method=RequestMethod.POST)
public void sumbitUpload( @ModelAttribute("uploadForm") FileUploadBean bean) throws Exception
{   
	String des = bean.getDescription();
	//String des_utf8 = new String(des.getBytes("iso8859-1"), "UTF-8");
	System.out.println("DESPRI:===" + des);
	System.out.println("Date Format:===" + bean.getBirthday());

	/* 
	//对图片是byte[]的附件做上传
	System.out.println("FileUploadBean:Length===" + bean.getImg().length);
	if (bean != null)
	{
		byte[] buff = bean.getImg();
		FileOutputStream out = new FileOutputStream(new File("c:/temp/xx.upload"));//原始文件名是??
		out.write(buff);
		out.close();
	}
	  */
	//------------------------------------------
	//对于MultipartFile的方式
	//中文文件名OK
	  byte[] buff=new byte[1024];
	  FileOutputStream out=new FileOutputStream(new File("c:/temp/"+bean.getImg().getOriginalFilename()));
	  InputStream  input=bean.getImg().getInputStream(); 
	  while(input.read(buff)!=-1) 
	  {
		  out.write(buff); 
	  }
	  out.close();
	  input.close(); 
	 for( MultipartFile photo:bean.getPhotos())
	 {
		 out=new FileOutputStream(new File("c:/temp/"+photo.getOriginalFilename()));
		 input=photo.getInputStream(); 
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

@RequestMapping(value="/download",method=RequestMethod.GET)
public void download(@RequestParam("file_id")int file_id,HttpServletResponse response) throws Exception
{   
	byte[] buffer=new byte[1024];
	FileInputStream input =new FileInputStream (new File("c:/temp/中文文件名.txt"));
	response.setContentType("application/x-msdownload");
	//response.setHeader("Content-disposition","inline;filename=workbook.xls");//inline显示在浏览器中
	String cn_filename=new String("中文文件名.txt".getBytes("UTF-8"),Charset.forName("iso8859-1"));
//	response.setCharacterEncoding("UTF-8");//对header的文件名没用的
	response.addHeader("Content-Disposition", "attachment;filename="+cn_filename);//attachment会提示下载
	ServletOutputStream output=response.getOutputStream();
	int len;
	while((len=input.read(buffer))!=-1)
	{
		output.write(buffer,0,len);
	}
	output.close();//可以关闭
	input.close();
}



@Controller
@RequestMapping("/session") 
@SessionAttributes({"sessionAttr","sessionEmp"})//将ModelMap中的 sessionAttr,sessionEmp 放在session中
public class SessionController 
{
	@ModelAttribute("initEmployees")
	//向ModelMap中添加一个名为initEmployees的属性,值是方法的返回值
	//在任何请求处理前调用,使用带ModelMap参数的方法中可以得到,Session无关的
	public List<Employee> initAllEmployee()
	{
	}
	
	@RequestMapping(params="method=testParams1")// 请求URL为session.mvc?method=testParams1
	public String testParamsStep1(ModelMap model)
	{
		model.addAttribute("sessionAttr", "zhangsang");//如 设置@SessionAttributes("sessionAttr"),request和session中都有,否则只有request中有
		System.out.println("in testParamsStep1======="+model.get("initEmployees"));//可以得到初始化的数据, Session无关的
	}
	public String testParamsStep2(@ModelAttribute("sessionEmp") Employee emp1,ModelMap modelMap, BindingResult result, SessionStatus status)
	{   
		//接收session的方式可以用  @ModelAttribute("sessionEmp")或 参数ModelMap , BindingResult和SessionStatus可无
		//从在session 中找 sessionEmp 的值给 emp1 ,类型要一致 ,必须要确保 session 中有sessionEmp
		if (result.hasErrors())
		{}
		System.out.println("in testParamsStep2====得到session为==="+emp1.getLast_name());
		status.setComplete()//该Controller 所有放在 session 级别的模型属性数据将从 session 中清空
	}	
}



@InitBinder	//需要处理Date的时候,自动调用这个方法
public void initBinder(WebDataBinder binder)//要用 WebDataBinder
{
	System.out.println("调用initBinder");
	binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
}

@RequestMapping("/cookie_header")//参数顺序无关,按类型 和 @
public String readCookie(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			@CookieValue("JSESSIONID") String seession_id,@RequestHeader("user-agent") String agent)
{
	System.out.println("readCookie 得到JSESSIONDID="+seession_id+",user-agent="+agent+",session="+session);
	Enumeration emu=request.getHeaderNames();
	Object [] cook=request.getCookies();
	return "ok";
}
@RequestMapping("/returnVoid")
public void returnVoid()
{
	//如返回void,可使用out.println(""),默认根据请求路径来生成viewName,即 returnVoid.mvc
}
@RequestMapping("/redirect")
public String redirect()
{
	return "redirect:/other/returnVoid.mvc";//如返回  redirect:xx.mvc 表示是重定向
}
@RequestMapping("/returnObject") //默认根据请求路径来生成viewName
//public Employee returnObject()
//public List<Employee> returnObject()
public ModelMap returnObject()
{//如返回是一个类,会放在ModelAndView的Model中,以Employee类名为key,生成为employee,如为List<Employee>生成的key为employeeList
	
	Employee e=new Employee();
	e.setFirst_name("李");
	e.setLast_name("召进");

	Employee e2=new Employee();
	e2.setFirst_name("张");
	e2.setLast_name("三");
	List<Employee> list=new ArrayList<>();
	list.add(e);
	list.add(e2);
	
//		return e;
//		return list;
	
	ModelMap springMap=new ModelMap();
	springMap.put("myKey1", "myVale1");
	return springMap;
}

@RequestMapping("/list/{page}")//要使用employee/list/1
public ModelAndView listEmployee(@PathVariable("page")int pageNO , HttpServletRequest request)
{
}
public String submitQuery(Employee param,String otherParam )//表单参数可以单独参数同名传过来
{}
@RequestMapping("/webRequest")
public String myHandleMethod(WebRequest webRequest, Model model)//参数可以是WebRequest
{
	long lastModifiedTimestamp = 0;
	if(webRequest.checkNotModified(lastModifiedTimestamp))
	{
		return null;
	}
	return "/other/returnVoid";
}
<mvc:view-controller path="home.mvc" view-name="redirect:employee.mvc"/><!-- 可以path="/" -->

<mvc:interceptors>
	<mvc:interceptor>
		<mvc:mapping path="/employee/*"/>
		<bean class="org.zhaojin.interceptor.MyInterceptor"/> <!-- 继承自 HandlerInterceptorAdapter -->
	</mvc:interceptor>
</mvc:interceptors>

public class MyInterceptor extends HandlerInterceptorAdapter
{
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception //顺序3,请求完成
	{
		System.out.println("=====afterCompletion");
	}
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception
	{ //有ModelAndView
		System.out.println("=====postHandle");//顺序2
	}
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
				Object handler) throws Exception//顺序1
	{
		System.out.println("=====preHandle");
		boolean flag=true;
		return flag;//是否中断执行链
	}
}

类定义使用 @Component ,就配置<bean>了
@Component 有一个可选的入参，用于指定 Bean 的名称(一般不用的,byType,如果有继承可能要的吧)
@Repository、@Service 和 @Controller //持久层、业务层和控制层（Web 层）相对应 和 @Component 是等效的
Bean不是自己编写的类（如 JdbcTemplate , SessionFactoryBean 等），注释配置将无法实施

<context:component-scan base-package="com.baobaotao"/> 不能要 SimpleUrlHandlerMapping,可以将 <context:annotation-config/> 移除了
<context:include-filter type="regex"   expression="com\.baobaotao\.service\..*"/>  包含
<context:exclude-filter type="aspectj"  expression="com.baobaotao.util..*"/>	   排除
 
新的@Contoller和@RequestMapping注解支持类 
处理器映射R equestMappingHandlerMapping 和 处理器适配器 RequestMappingHandlerAdapter 组合
来代替Spring2.5开始的处理器映射 DefaultAnnotationHandlerMapping 和处理器适配器 AnnotationMethodHandlerAdapter 

<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter"/>
  

--Book
SimpleUrlHandlerMapping 可以加interceptors ,支持 HandlerInterceptor  和 WebRequestInterceptor.
		setInterceptors(Object[] interceptors)

DataBinder binder=new DataBinder(new Employee)//与<sping:bind>功能同
binder.getTarget() , binder.getErrors() ,getAllErrors中每一个是FieldError
 
------------------------JSP  标签
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- 使用spring:url ,c:url  /开头,会把项目名加上 -->
<a href="<spring:url value="/employee/update.mvc?id=${emp.employee_id}"/>">修改</a> 
<a href="<c:url value="/employee/delete.mvc?id=${emp.employee_id}"/>">删除</a>
				
<spring:bind path="mycommand.username" >
用户名: <input type="text" name="<c:out value="${status.expression}"/>" value="${status.value}" > <br>
</spring:bind>

<spring:bind path="command.*">
	<c:if test="${status.error}">
		<font color="#FF0000">所有的错误:
			<c:forEach items="${status.errorMessages}" var="item">
				<c:out value="${item}"/><br/>
			</c:forEach>
		</font>
	</c:if>
</spring:bind>	

org.springframework.web.servlet.support.BindStatus 类中的属性是为<spring:bind>标签 如isError() 对应于 ${status.error}

<spring:hasBindErrors />怎么用???

//----
<form:form commandName="mycommand" > 生成是method="POST"
	<spring:bind path="mycommand.username" ><!--   mycommand 可加,可不加 -->
	用户名: <input type="text" name="<c:out value="${status.expression}"/>" value="${status.value}" > <br>
	</spring:bind>
	出生日期: <form:input path="birthday" /> <br>  <!--  是path= ,mycommand不能加,-->
	表单中的错误:<form:errors path="*"  />
</form:form>


<form:input path="firstname"/>
<form:checkbox 对应String[] 也可是boolean ,value="1"
生成后有一个<input type="hidden"  做用的是如果没有选择 ,这个值不会提交,解决是在其后面加一个hidden名字是 checkbox名加_
<form:radiobutton path="sex" value="F">
<form:password path=""
<form:select path="" item=${all}>  或手工加 <form:option value=""/> 或用<form:options items="${countryList}" itemLable="name" itemValue="code"></form:options>
<form:textarea rows="20" cols="20">
<form:hidden>
<form:errors path="username"/>  会产生一个<span> 
			path="*"
			path="*usrname"



ResourceBundleThemeSource
	setBasenamePrefix(  //在classpath下找以前缀开关的  ,可国际化
	如文件位置是"/WEB-INF/classes/com/aa/bb/cc/cool.properties" 则	setBasenamePrefix("com.aa.bb.cc.cool");//加包名
id="themSource"
<spring:theme code="xxx"/>  


--表单　@ModelAttribute
//进入页前	
@RequestMapping(method = RequestMethod.GET)   
public String initForm(Model model)   //Model   OK  ModelMap OK
{    
	Account account = new Account();   
	model.addAttribute("account", account);   
}
//提交
@RequestMapping(method = RequestMethod.POST)   
public String login(@ModelAttribute("account") Account account)//表单对应的Bean属性最好不要用char类型,用String代替
{
}
<form:form commandName="account">  也可用  <form:form modelAttribute="account"
	<form:input  path="username" /><!-- 是属性名 -->



@EnableWebMvc
@Configuration
 
 
<!--  为@Valid  要加 hibernate-validator-.jar -->
<mvc:annotation-driven  validator="validator" />
<bean id="validator"  class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
	<property name="providerClass"  value="org.hibernate.validator.HibernateValidator"/>
	<property name="validationMessageSource" ref="messageSource"/> <!-- 如果不加默认到 使用classpath下的 ValidationMessages.properties -->
</bean>

import javax.validation.ConstraintValidator;
public class EqualAttributesValidator implements ConstraintValidator<EqualAttributes, Object>
{
	private String firstAttribute;
	private String secondAttribute;
	@Override
	public void initialize(final EqualAttributes constraintAnnotation)
	{
		Assert.notEmpty(constraintAnnotation.value());
		Assert.isTrue(constraintAnnotation.value().length == 2);
		firstAttribute = constraintAnnotation.value()[0];
		secondAttribute = constraintAnnotation.value()[1];
		Assert.hasText(firstAttribute);
		Assert.hasText(secondAttribute);
		Assert.isTrue(!firstAttribute.equals(secondAttribute));
	}

	@Override
	public boolean isValid(final Object object, final ConstraintValidatorContext constraintContext)
	{
		if (object == null)
			return true;
		try
		{
			final Object first = PropertyUtils.getProperty(object, firstAttribute);
			final Object second = PropertyUtils.getProperty(object, secondAttribute);
			return new EqualsBuilder().append(first, second).isEquals();
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualAttributesValidator.class)//自这定义的类
@Documented
public @interface EqualAttributes
{
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] value();
}

@EqualAttributes(message="{validation.passwordNotSame}",value={"password","rePassword"})//自定义验证
 public class Account
 {
	//@NotNull(message = "日期不能为空")//只是值为null,如为空串用这个无效
	@NotBlank( message = " username不能为空")//hibernate的,可以验证空串
	@Size(min = 3, max = 20, message = "{validation.username_length}")//国际化串中可以使用{min},{max}
    private String username;
	 
	@Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "{validation.password_complex}")
	private String password; 
	
	private String rePassword; 
	
	@Min(value=18,message="{validation.age_lessThan}")////国际化串中可以使用{value}
    @Max(value=70,message="{validation.age_greatTahn}")
    private int age;
	
    @Pattern(regexp = "^[_.0-9a-z-]+@([0-9a-z][0-9a-z-]+.)+[a-z]{2,3}$", message = "{validation.email_format}")
    private String email;
}
public String login(@Valid @ModelAttribute("accountForm") Account account ,BindingResult result)
{
	if(result.hasErrors())
	{
				//代码中的国际化方法,必须有ContexLoaderListener注册过
//			ServletContext servletContext=request.getServletContext();
//			WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);  
//			Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);  
//		    String title = applicationContext.getMessage("title",null, locale);  
//		    System.out.println("代码  国际化  title为:"+title);
		result.addError(new FieldError("emailSendForm","email","代码中验证表单表单的错误"));
		return "company_annotation/login";   
	}
}

@ModelAttribute("monthsNames")//可在页面中任何位置引用 ${monthsNames}
public  Map<Integer,String>  getMonthsNames()
{
	Map<Integer,String>  months = new HashMap<> ();
	final DateFormatSymbols dateSymbols = new DateFormatSymbols(Locale.getDefault());
	final String[] symbols = dateSymbols.getMonths();
	for (int i = 1; i < symbols.length; i++)
	{
		months.put(i,symbols[i - 1]);
	}
	return months;
}
<form:select path="month" items="${monthsNames}"></form:select> <!-- 可带回值的 -->


<form:select path="vehicleLineCode"   id="newVehicleLineCode" >   <%--  items="${allLines}" itemLabel="vehicleLineName"  itemValue="vehicleLineCode" --%>
	<form:option  value="">全部</form:option>
	<form:options items="${allLines}" itemLabel="vehicleLineName" itemValue="vehicleLineCode" />
</form:select>

----未测试
ThemeChangeInterceptor,LocaleChangeInterceptor, 
 

@RequestMapping(value = "/**/request")
public String passwordRequest(@Valid final ForgottenPwdForm form,BindingResult result)//ForgottenPwdForm必须实现 Serializable,有@Valid就必须要有 BindingResult,如form中long类型也要有
//List 没有实现  Serializable

@RequestMapping(value = "/xxx")
public String passwordRequest( RedirectAttributes   redirectAttributes)
{
	redirectAttributes.addAttribute("myTestredirectAttr", "中国");
	return redirect:/xx
}

@ExceptionHandler(Exception.class)
public String handleException(final Exception e)
{
}



@Required

------------------------Freemarker 
Freemarker 不能在群集上面发布应用

freemarker 文件改编码要UTF-8
freemarker 文件中加<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
在classpath上放置一个文件 freemarker.properties，加入
	default_encoding=UTF-8
	locale=zh_CN
	
<bean  class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
	<property name="viewClass" 		value="org.springframework.web.servlet.view.freemarker.FreeMarkerView"/>
	 <property name="suffix" 		value=".ftl" />
	 <property name="contentType" 	value="text/html;charset=UTF-8"/>
	 <property name="cache" 		value="false"/>
</bean>
<bean   class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
	<property name="templateLoaderPath" value="/WEB-INF/freemarker/" />
	<property name="freemarkerSettings">
		<props>
			<prop key="defaultEncoding">UTF-8</prop>
			<prop key="number_format">#</prop> <!-- 防止生成的数字带"," <@spring.formHiddenInput "form.id" />  -->
			<prop key="template_update_delay">3600</prop> <!--秒-->
		</props>
	</property>
	<property name="freemarkerVariables">
	   <map>
			<entry key="webRoot" value="/J_SpringMVC"></entry>   <!--就可在ftl中使用 ${webRoot} 得到上下的根来导入js-->
	   </map>
  </property>
  
</bean>	

<mvc:resources mapping="/js/**" location="/js/"/> <!-- 不会被拦截 -->

<#import "/spring.ftl" as spring/>
在org.springframework.web.servlet.view.freemarker包下

国际化消息:<@spring.message "title"/>
带默认值的国际化消息:<@spring.messageText "_title_", "这是默认值"/>

用  户  名:<@spring.formInput "form.username" 'class="mytext"'/> * <@spring.showErrors "<BR/>" "color:red" /> <br>
密    码: <@spring.formPasswordInput  "form.password"/> <br>
出生日期:<@spring.bind "form.birthday" />
		<input type="text" name="${spring.status.expression}"  value="${spring.status.value}">
年   龄:<@spring.formInput	"form.age" 'size="2" maxlength="2"'/>	  <br>
描   述:<@spring.formTextarea "form.remark" 'rows="6" cols="30"' />	  <br>
		<#assign genders =  {"T":"男","F":"女"} >
性   别:<@spring.formRadioButtons "form.gender" genders "<br/>"/>	  <br> <!-- 数据必须是Map<String,String>才行,gender的属性可以是boolean-->
		<@spring.formSingleSelect "form.gender" genders /> * <@spring.showErrors "<BR/>" "color:red" /> <br/>
HTML所在班级:<select name="clazz_id" >
			<#list allClazz?keys as key> 
				<option value="${key}">${allClazz[key]}</option>
			</#list>
		</select>  <br>
Freemarker所在班级 :<@spring.formSingleSelect "form.clazz_id" allClazz />  <br> <!--clazz_id的属性可以是int -->

	<@spring.formHiddenInput "form.id" /> <br>
	

<a href="<@spring.url "/registerFreemarker.mvc" />">再做一次,以/开头的带项目名</a>

Map<String,String> allMan=new HashMap<>();
allMan.put("true", "男");
allMan.put("false", "女");
mv.addObject("allMan", allMan);
<@spring.formSingleSelect "form.man" allMan />



List 没有实现  Serializable,测试验证时多选的验证无效

兴趣爱好:<@spring.formCheckboxes	 "form.interest_ids" allInterests "<br/>" 'class="mytext"'/>  <br>
多选 选修课程 :<@spring.formMultiSelect "form.course_ids" allCourses 'class="myselect"'/>  <br>
 
可以多选表单组件,不能使用?????

  
--------------------Spring MVC 其它视图POI的excel
 <bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
	<property name="basename" value="views"></property> <!-- views.properties文件,支持Excel等其它视图 -->
 </bean>
<bean name="/excel.mvc" class="spring_jsp.support.ExcelController">
	<property name="commandClass" value="spring_jsp.support.DataInfo"/>
	<property name="formView">
		<value>pdf_exel</value>
	</property>
	<property name="successView">
		<value>excelSong</value><!-- 配置在views.properties 文件中 -->
	</property>
</bean>
classpath下的 views.properties文件内容
excelSong.(class)=spring_jsp.support.ExcelView

AbstractExcelView  的buildExcelDocument(文档有列子)

public class ExcelView extends AbstractExcelView {
	public void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataInfo user = (DataInfo) model.get("excelData");
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		String username = user.getUsername();
		String username_utf8 = new String(username.getBytes("iso8859-1"),"UTF-8");
		cell0.setCellValue(new HSSFRichTextString(username_utf8));
		cell1.setCellValue(new HSSFRichTextString(user.getPassword()));
	}
}


--------------------Spring MVC 其它视图PDF
有些PDF不可复制的

在views.properties中要定义
[视图].url对应的值是该视图所对应的的URL
xx.url=/my.jsp 没试过	

reponseForm.class=org.springframework.web.servlet.view.freemarker.FreeMarkerView
reponseForm.url=xxx.ftl
reponseForm.exposeSpringMacroHelpers=true
  

--------------------Spring MVC 其它视图XML
<bean class="org.springframework.web.servlet.view.XmlViewResolver">
	<property name="cache" value="true"/>
	<property name="location" value="/WEB-INF/views.xml"/><!-- 默认是/WEB-INF/views.xml,内容是Spring <bean> -->
 </bean>
<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
<bean name="/viewXslt.mvc" class="spring_xslt.XsltController"/>

views.xml
<bean id="myXslt" class="spring_xslt.MyView"> <!-- ModelAndView 的视图名 myXslt-->
	<property name="stylesheetLocation" value="/WEB-INF/xslt/myData.xslt"></property>
	<property name="root" value="dataList"></property>
</bean>
public class MyView extends AbstractXsltView { // AbstractXsltView 过时的
	protected Source createXsltSource( Map model, String rootName, HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement(rootName);
			List words = (List) model.get("myDataList");
			for (Iterator it = words.iterator(); it.hasNext();) 
			{
				String nextWord = (String) it.next();
				Element wordNode = document.createElement("word");
				Text textNode = document.createTextNode(nextWord);
				wordNode.appendChild(textNode);
				root.appendChild(wordNode);
			}
			return new DOMSource(root);
		}
}

views.properties
#ResourceBundleViewResolver
#myXslt.(class)=spring_xml.MyView
#myXslt.stylesheetLocation=/WEB-INF/xsl/myData.xslt
#myXslt.root=dataList

myData.xslt
<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" omit-xml-declaration="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<title>Hello!</title>
			</head>
			<body>
				<h1>My Data</h1>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="word">
		<xsl:value-of select="." />
		<br />
	</xsl:template>
</xsl:stylesheet>

public class XsltController extends AbstractController {
	protected ModelAndView handleRequestInternal(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		Map map=new HashMap();
		List list=new ArrayList();
		list.add("张三");
		list.add("李四");
		map.put("myDataList", list);
		return new ModelAndView("myXslt",map);
	}
}

----请求和响应都是JSON
jackson-annotations-2.2.3.jar
jackson-core-2.2.3.jar
jackson-databind-2.2.3.jar

<!--ContentNegotiatingViewResolver 可选 的 -->
<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
	<property name="order" value="1" />
	<property name="defaultViews">
		<list>
			<bean class="org.springframework.web.servlet.view.json.MappingJackson2JsonView">
				<property name="prefixJson" value="false" />
			</bean>
		</list>
	</property>
</bean>

 <!-- RequestMappingHandlerAdapter  的配置影响　@Valid的有效性 ???  -->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
	<property name="messageConverters">
		<list>
		<!-- 	<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/> JSON 简配置-->
			<bean id="mappingJacksonHttpMessageConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
				<property name="objectMapper">
					<bean class="com.fasterxml.jackson.databind.ObjectMapper">
						<property name="dateFormat">
							<bean class="java.text.SimpleDateFormat">
								<constructor-arg type="java.lang.String" value="yyyy-MM-dd HH:mm:ss"/>  <!-- JSON到SpringMVC日期格式-->
							</bean>
						</property>
					</bean>
				</property>
			  </bean>
			<bean class="org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter"/> <!-- XML 为 @ResponseBody -->
			<bean class="org.springframework.http.converter.StringHttpMessageConverter"/>  <!--为 @ResponseBody 的 text/*     */--> 
			<!-- <bean class="org.springframework.http.converter.FormHttpMessageConverter"/>   application/x-www-form-urlencoded  -->
		</list>
	</property>
	<property name="cacheSeconds" value="0" />
	<property name="webBindingInitializer">
		<bean class="spring_jsp.extention.MyWebBindingInitializer" />
	</property>
</bean>

也可以这样配置
 <mvc:annotation-driven>
 	<mvc:message-converters>
 		<ref bean="mappingJacksonHttpMessageConverter" />
 	</mvc:message-converters>
 </mvc:annotation-driven>
	
	
//MyWebBindingInitializer -> @ControllerAdvice -> @Controller 
public class MyWebBindingInitializer implements WebBindingInitializer 
{
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		System.out.println("调用 MyWebBindingInitializer ");
		//binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		binder.registerCustomEditor(Date.class,new  MyPropertyEditor());
	}
}

//如果JSON日期 实体某个属性日期格式不一样 未测试????
@DateTimeFormat(pattern="yyyy-MM-dd")   
@JsonSerialize(using=JsonDateSerializer.class)  
private Date returnBillDepartureTime;

class JsonDateSerializer extends JsonSerializer<Date> 
 {
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
	{
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String value = dateFormat.format(date);
		 try {
			  gen.writeString(value);
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	}
}
	
@Controller
@RequestMapping("/json") 
public class JSONController //OK
{
		@RequestMapping(value="/queryEmployeeVO",method=RequestMethod.POST)
		@ResponseStatus(HttpStatus.OK)
	    @ResponseBody  //加这个表示只返回数据,不跳转页面(默认是和RequestMapping相同页)
		public EmployeeResult queryEmployeeVO	( @RequestBody  Employee emp)
		{
			System.out.println(emp.getEmployee_id());
		}
		@RequestMapping(value="/queryEmployeeVO2" , method=RequestMethod.POST)
	    public ResponseEntity<EmployeeResult> testJson2(HttpEntity<Employee> entity)
	    {  
			System.out.println(entity.getBody().getEmployee_id());
		}
}
function ajaxJSONRequest()//OK
{
	var xmlHttp=createXmlHttp();
	xmlHttp.onreadystatechange=function()
	{
		if(xmlHttp.readyState!=4) 
			return;
		if(xmlHttp.status!=200)
		{
			alert("error:"+xmlHttp.statusText);
		 	return;
		}
		alert("success:"+xmlHttp.responseText);
	}
	xmlHttp.open("POST", root+"/json/queryEmployeeVO.mvc", false);//是否异步,true 异步,false同步
	xmlHttp.setRequestHeader("Content-Type","application/json;charset=UTF-8");//请求是JSON 
	xmlHttp.send('{"employee_id":123,"first_name":"李四"}');
}
function jQueryJSONRequest() //OK
{
	var config=
	{
		type: 'POST',
		contentType:'application/json;charset=UTF-8',//请求是JSON   
		dataType: 'json',//响应是JSON
		url:root+'/json/queryEmployeeVO.mvc' ,
		//data:JSON.stringify({employee_id:'123',first_name:'李四'}),//OK  发送的必须是字串,不能JS object
		data:'{"employee_id":123,"first_name":"李四"}',//OK,内部Key必须是" 
		success:function(response)
		{
			alert("success:"+response.allClothes[1]);
		}
	};
	$.ajax (config);
}

spring-web-4.0.6.RELEASE.jar/META-INF/services/javax.servlet.ServletContainerInitializer 中是 SpringServletContainerInitializer,
手工实现 WebApplicationInitializer  类(为不使用web.xml设计)


@ControllerAdvice("spring_jsp.annotation")
//@ControllerAdvice(assignableTypes = {Controller.class, AbstractController.class})
//@ControllerAdvice(annotations = RestController.class)
public class BasePackageAdvice 
{
	 @ModelAttribute
	 public UserDetails newUser() 
	 {
        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");
        return new UserDetails();
    }
	 
    @InitBinder
    public void initBinder(WebDataBinder binder) 
    {
        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String processUnauthenticatedException(NativeWebRequest request, RuntimeException e) {
        System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出RuntimeException异常时执行");
        return "showError"; //返回一个逻辑视图名
    }
}
@RequestMapping(method=RequestMethod.POST,value="/asyncUpload")
//web.xml中所有的filter,和用的servlet加 <async-supported>true</async-supported>
public Callable<String> processUpload(@RequestParam("img") final MultipartFile file) {
	return new Callable<String>() {
		public String call() throws Exception {
			byte buff[]=new byte[1024];
			OutputStream out=new FileOutputStream(new File("c:/temp/"+file.getOriginalFilename()));
			 InputStream input=file.getInputStream(); 
			 while(input.read(buff)!=-1) 
			  {
				  out.write(buff); 
			  }
			 out.close();
			 input.close();
			return "ok";
		}
	};
}
@RequestMapping("/deferredResult")
@ResponseBody
public DeferredResult<String> deferredResult() 
{
	DeferredResult<String> deferredResult = new DeferredResult<String>();
	deferredResult.setResult("这是 DeferredResult数据");
	return deferredResult;
}

@RequestMapping(value = "/responseBody", method = RequestMethod.GET)
@ResponseBody
public String responseBody() {
	return "Hello World";
}
//配置,MappingJackson2HttpMessageConverter
@RequestMapping(value="/responseBodyJSON", produces="application/json")
@ResponseBody
public Map<String, Object> responseBodyJSON(HttpServletRequest request) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("status", "1231231");
	map.put("reason", "原因");
	return map;
}

//配置 Jaxb2RootElementHttpMessageConverter
@RequestMapping(value = "/responseBodyXML", method = RequestMethod.GET, produces="application/xml")
@ResponseBody
public UserDetails responseBodyXML() {
	UserDetails userDetails = new UserDetails();
	userDetails.setUserName("Krishna");
	userDetails.setEmailId("krishna@gmail.com");
	return userDetails;//类级加  @XmlRootElement
}



@RestController
//也可以用 两个组合
//@Controller
//@ResponseBody


-------------Hession
---web.xml
<!-- Hessian server 要注册  ContextLoaderListener-->
<servlet>
	<servlet-name>remoting</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<load-on-startup>1</load-on-startup> <!-- 找  /WEB-INF/remoting-servlet.xml  -->
</servlet>
<servlet-mapping>
	<servlet-name>remoting</servlet-name>
	<url-pattern>/remoting/*</url-pattern>    */
</servlet-mapping>


<!--  方式2 ,basicHessianExporter 是 spring bean 的 id -->
<servlet>
	<servlet-name>basicHessianExporter</servlet-name> 
	<servlet-class>org.springframework.web.context.support.HttpRequestHandlerServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>basicHessianExporter</servlet-name>
	<url-pattern>/remoting/hessianServer</url-pattern>
</servlet-mapping>


----spring.xml
<!-- =====  Hessian  server-->
	<bean id="baseHessianService" class="springmvc_hessian.BasicService">
	</bean>
	
	<!--  server  方式1  
	<bean name="/hessianServer" class="org.springframework.remoting.caucho.HessianServiceExporter">
	    <property name="service" ref="baseHessianService"/>
	    <property name="serviceInterface" value="springmvc_hessian.BasicAPI"/>
	</bean>
	 -->
	   
	 <!-- server  方式2 和     web.xml 中 HttpRequestHandlerServlet 配对   -->
	<bean id="basicHessianExporter" class="org.springframework.remoting.caucho.HessianServiceExporter">
	    <property name="service" ref="baseHessianService"/>
	    <property name="serviceInterface" value="springmvc_hessian.BasicAPI"/>
	</bean>
	
	
<!-- =====   Hessian  client-->
	<bean id="hessianServiceClient" class="org.springframework.remoting.caucho.HessianProxyFactoryBean">
	    <property name="serviceUrl" value="http://127.0.0.1:8080/J_SpringMVC/remoting/hessianServer"/>
	    <property name="serviceInterface" value="springmvc_hessian.BasicAPI"/>
	</bean>
	就可以注入   BasicAPI hessianServiceClient

=========================上 Spring MVC
 

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


==============Swagger框架

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

 使用 http://127.0.0.1:8080/J_SpringMVC/api-docs.mvc  或者修改   <url-pattern>/</url-pattern>

页面中 Data Type 组选    Model Schema 



