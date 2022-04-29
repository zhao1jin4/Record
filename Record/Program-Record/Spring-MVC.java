 Spring 5.0 WebFlux 基于 Reactive Stream ,而Reactive Stream 已经有JDK9的Flow实现了，刚出来就过时了
 也是基于 Reactor 
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
<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver">
		<property name="viewClass" value="org.springframework.web.servlet.view.JstlView"></property>
		<property name="prefix" value="/WEB-INF/jsp/"></property>
		<property name="suffix" value=".jsp"></property>
</bean>

<bean  class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
	<property name="mappings">
		<value><!-- 使用 value,也可使用props-->
			/helloWorld.mvc				=helloWorldCntroller
			login.mvc					=loginController <!-- 可以不用加/ -->
			register.mvc				=enterRegisterController <!-- UrlFilenameViewController,找register名字做为view -->
	 	</value>
	</property>
</bean>

<bean id="helloWorldCntroller" class="spring_jsp.HelloWorldController" />
<bean  id="loginController"  class="org.springframework.web.servlet.mvc.ParameterizableViewController">
	<property name="viewName" value="login"></property> <!-- 可不用自己建立的Controller,而直接进入login.jsp页面的方式-->
</bean>
<bean  id="enterRegisterController"  class="org.springframework.web.servlet.mvc.UrlFilenameViewController"/><!-- 视图名  就是配置的URL的部分  -->


<bean name="/helloWorldBeanName.mvc" class="spring_jsp.HelloWorldController" />
<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
<!--   以是 name="/helloWorldBeanName.mvc" , 是使用 BeanNameUrlHandlerMapping  -->
  

<!--基于 InternalResourceViewResolver 就不用像 UrlBasedViewResolver 指定viewClass-->
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



<!-- 国际化  基于JDK的实现 -->
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
	<property name="basename" value="conf.messages"/>  <!-- 包名格式 文件名要有 messages_en.properties 和 messages_zh.properties -->
</bean>
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
	<property name="basename" value="classpath:/conf/messages"/> <!-- 也可  WEB-INF/messages ，使用属性basenames可配置多个-->
</bean>


<fmt:setBundle basename="message"/>  <!-- JSTL -->
<fmt:setLocale value="zh_CN"/>

<fmt:message key="employee_query"/>
<spring:message code="employee_id"/>
<spring:message code="title" arguments="王2,张2"   />

@Autowired
private MessageSource messageSource;

Locale locale=request.getLocale();
//locale=Locale.CHINESE;
String i18nStr=messageSource.getMessage("title",new Object[] {"张","王"} , locale);
System.out.println("i18nStr="+i18nStr);

<!-- 全局错误页 定义 
也可
 	@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
-->
<bean  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
	<!-- 
	<property name="defaultErrorView">
		<value>error/failure</value>
	</property>
	 -->
	<property name="statusCodes"> <!-- 404 500 不好使？？？ 只能web.xml 和defaultErrorView -->
		<props>
			<prop key="error/serverError">500</prop>
			<prop key="error/notFound">404</prop>
		</props>
	</property>
	<property name="exceptionMappings">
		<props>
			<prop key="org.springframework.web.multipart.MaxUploadSizeExceededException" >error/showError</prop>
			<prop key="java.sql.SQLException">error/showDBError</prop> <!-- 优先于web.xml的配置 -->
			<prop key="java.lang.RuntimeException">error/showError</prop>
		</props>
	</property>
	<!--commons logging 的  LogFactory.getLog(loggerName);
	如不配置这个，Controller抛异常错误不会在日志中显示  -->   
	<property name="warnLogCategory" value="SpringExceptionResolver"></property> 
</bean>
<mvc:resources mapping="/error/**" location="/WEB-INF/views/error/" />

<% Exception ex = (Exception)request.getAttribute("Exception"); //只对 defaultErrorView 指定的有值
%>



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
<form enctype="multipart/form-data">

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


 //JSON无效
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
@RequestMapping("/forward")
public String forward()
{
	return "forward:/other/returnVoid.mvc"; 
}
public String forwardServlet(HttpServletRequest request,HttpServletResponse response)
{
	try {
		request.getRequestDispatcher("/session.jsp").forward(request, response);
		//注意后面的代码还是会被执行的,但最终显示的页是RequestDispatcher的不是返回的view
		System.out.println(1/0); 
	} catch ( Exception e) {  
		e.printStackTrace();
	}
	return "forward:/other/returnVoid.mvc"; 
}
	
@RequestMapping("/returnObject") //默认根据请求路径来生成viewName
//public Employee returnObject()
//public List<Employee> returnObject()
public ModelMap returnObject()
{//如返回是一个类,会放在ModelAndView的Model中,以Employee类名为key,生成为employee,如为List<Employee>生成的key为employeeList
	
	Employee e=new Employee();
	e.setFirst_name("李");
	e.setLast_name("四");

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
		<bean class="spring_jsp.extention.MyInterceptor"/> <!-- 继承自 HandlerInterceptorAdapter -->
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


 
@Configuration 
public class  Configuration implements WebMvcConfigurer { 
    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(new LogInterceptor()); 
    }
}
 
public class LogInterceptor implements HandlerInterceptor {
    Logger log= LoggerFactory.getLogger(HandlerInterceptor.class); 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle:handler={} ,ParameterMap={},ContentLength={}", handler,
                ToStringBuilder.reflectionToString(request.getParameterMap()),request.getContentLength());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle handler={} modelAndView={},ContentType={}",handler,modelAndView,response.getContentType());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

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


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
	
}

@EnableWebMvc  同  <mvc:annotation-driven/>
@Configuration
 
 
<!--  为@Valid  要加 hibernate-validator-.jar -->
<mvc:annotation-driven  validator="validator" />
<bean id="validator"  class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
	<property name="providerClass"  value="org.hibernate.validator.HibernateValidator"/>
	<property name="validationMessageSource" ref="messageSource"/> <!-- 如果不加默认到 使用classpath下的 ValidationMessages.properties -->
</bean>

就可以用
@Autowired
private Validator validator; //配置了 LocalValidatorFactoryBean

LocaleContextHolder.setLocale(locale);//Spring 会自动切换验证错误的语言

	
import javax.validation.ConstraintValidator;
public class EqualAttributesValidator implements ConstraintValidator<EqualAttributes, Object>
{
	private String firstAttribute;
	private String secondAttribute;
	@Override
	public void initialize(final EqualAttributes constraintAnnotation)
	{ 
		firstAttribute = constraintAnnotation.value()[0];
		secondAttribute = constraintAnnotation.value()[1]; 
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
    //@DecimalMin
    //@DecimalMax(value = "12.35")
    private int age;
	
    @Pattern(regexp = "^[_.0-9a-z-]+@([0-9a-z][0-9a-z-]+.)+[a-z]{2,3}$", message = "{validation.email_format}")
    private String email;
}
//@Valid 只可对表单提交式
public String login(@Valid @ModelAttribute("accountForm") Account account ,BindingResult result)
{
	if(result.hasErrors())
	{
		for(ObjectError err:result.getAllErrors())
		{
			System.out.println(err.getObjectName()+"=="+err.getDefaultMessage());
		}
				//代码中的国际化方法,也可 @Autowired private MessageSource messageSource;
//			ServletContext servletContext=request.getServletContext();
//			WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);  //web.xml中要必须有ContexLoaderListener注册过
//			Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);  
//		    String title = applicationContext.getMessage("title",getRequiredWebApplicationContext, locale);  
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



//JS端做一次encodeURI(),服务端SpringMVC自动做decodeURI转换为中文

------------------------Freemarker 


FreeMarker不支持集群应用
	把序列化的东西都放到了Session,request等,但如果将应用放到集群中，就会出现错误
	

freemarker 文件改编码要UTF-8
freemarker 文件中加<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
在classpath上放置一个文件 freemarker.properties，加入
	default_encoding=UTF-8
	locale=zh_CN
	
可以自己写个类实现 ViewResolver接口,注册到Spring中,FreeMarkerViewResolver 和 InternalResourceViewResolver 不配置suffix 
返回视图就要加扩展名,如根据扩展名选择视图,就可以在项目使用同时使用.jsp和.ftl
	
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
<#assign seq = ['王1', '张1']> 
带参数的国际化消息:<@spring.messageArgs "title", seq/> <BR/>
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

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.10.3</version>
</dependency>

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
			<ref bean="mappingJackson2HttpMessageConverter" />
			<bean class="org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter"/> <!-- XML 为 @ResponseBody produces="application/xml" -->
			<bean class="org.springframework.http.converter.StringHttpMessageConverter"/>  <!--为 @ResponseBody 的 text/*     */--> 
			<!-- <bean class="org.springframework.http.converter.FormHttpMessageConverter"/>   application/x-www-form-urlencoded  -->
		</list>
	</property>
	<property name="cacheSeconds" value="0" />
	<property name="webBindingInitializer">
		<bean class="spring_jsp.extention.MyWebBindingInitializer" />
	</property>
</bean>


<!-- 	<bean id="mappingJackson2HttpMessageConverter"  class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/> JSON 简配置-->
<bean id="mappingJackson2HttpMessageConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"> <!--  produces="application/json" -->
		<property name="objectMapper" ref="jacksonObjectMapper2"> </property>
	</bean> 
	<!--  对enum类型中有的自定义属性会忽略，只转换每一个分号前的值  -->
	<bean id="jacksonObjectMapper2" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
        <property name="targetObject">
            <bean class="com.fasterxml.jackson.databind.ObjectMapper"> 
                 <property name="dateFormat">
                    <bean class="java.text.SimpleDateFormat">
                        <constructor-arg type="java.lang.String" value="yyyy-MM-dd HH:mm:ss" />
                    </bean>
                </property>
					<property name="timeZone">  <!-- 对Timestamp类型 -->
						<bean class="java.util.TimeZone"  factory-method="getTimeZone" >
							<constructor-arg value="GMT+8"/> 
						</bean>
					</property>
                </property>
               <!-- 为null字段时不显示 
                <property name="serializationInclusion"> 
                    <value type="com.fasterxml.jackson.annotation.JsonInclude.Include">NON_NULL</value>
                </property>
                -->
                 <!-- null值显示，自定义为显示空串，但对日期类型服务端可转换为null -->
	           	<property name="serializerProvider">  
	                <bean class="com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl">  
	                    <property name="nullValueSerializer">  
	                        <bean class="spring_jsp.extention.JsonNullEmptySerializer"></bean>  
	                    </property>  
	                </bean>  
	            </property>
            </bean>
        </property>
        <property name="targetMethod" value="configure" />
        <property name="arguments">
            <list>
                <value type="com.fasterxml.jackson.databind.DeserializationFeature">FAIL_ON_UNKNOWN_PROPERTIES</value>
                <value>false</value><!--  反序列化遇到未知属性不报异常 -->
            </list>
        </property>
    </bean>
		  
		  
<!-- 也可以这样配置 　@Valid 是有效的，但converter无效？？？
 <mvc:annotation-driven>
 	<mvc:message-converters>
 		<ref bean="mappingJackson2HttpMessageConverter" />
 	</mvc:message-converters>
 </mvc:annotation-driven>
-->

public class JsonNullEmptySerializer extends JsonSerializer<Object> {  
    @Override  
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)  
            throws IOException, JsonProcessingException {  
        jgen.writeString("");  
    }  
} 

	
//MyWebBindingInitializer -> @ControllerAdvice -> @Controller 
public class MyWebBindingInitializer implements WebBindingInitializer 
{
	@Override
	public void initBinder(WebDataBinder binder) {
		System.out.println("调用 MyWebBindingInitializer ");
		//binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));//JSON格式无效
		binder.registerCustomEditor(Date.class,new  MyPropertyEditor());
	}
}

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
public class  Employee{
	//JSON 类级别的单独日期格式化  方式一
	@DateTimeFormat(pattern="yyyy-MM-dd") //是将String转换成Date，一般前台给后台传值时用
	@JsonFormat(pattern="yyyy-MM-dd")//将Date转换成String 一般后台传值给前台时, 对Timestamp类型要加 timezone="GMT+8"
	private Date birthDay;

	// JSON 类级别的单独日期格式化 方式二
	@JsonSerialize(using = MyDateJsonSerializer.class)    
	public Date getBirthDay() {
		return birthDay;
	}
	@JsonDeserialize(using = MyDateJsonDeserializer.class)    
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
 }
//
public class MyDateJsonSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeString(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
}
public class MyDateJsonDeserializer extends JsonDeserializer<Date>
{    
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)  
    {    
        try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(jp.getText());
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return null;
    }    
}  




	
@Controller
@RequestMapping(value={"/json","/json2"}) //可以一个Controller多个URL
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
		
		
		@RequestMapping(value="/paramList",method=RequestMethod.POST)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody 
		//如@RequestBody  List<Employee> emps 可以进方法，Spring3 List中的是Map类型
		//@RequestBody  Employee[] emps  是可以的
		//public EmployeeResult paramList(HttpServletRequest request, @RequestBody  Employee[] emps)//OK
		public EmployeeResult paramList(HttpServletRequest request, @RequestBody  List<Employee> emps)//Spring5 是实体类 OK ,Spring3 是Map
		{
			System.out.println(emps.size());
			//System.out.println(emps.length);
		
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
		data:JSON.stringify(
					{employee_id:'123',first_name:'李四'
					//,birthDay:'2010-03-15 18:10:20'  
						,birthDay:'' //服务端为null(只用全局配置和只用类级配置)
					}), 
		//data:'{"employee_id":123,"first_name":"李四"}',//OK,内部Key必须是字串 ,不能JS object
		success:function(response)
		{
			alert("success:"+response.underEmp[0].birthDay); 
		}
	};
	$.ajax (config);
}

function jQueryJSONListRequest()  
{
	var paramList=[];
	for(var i=0;i<3;i++)
	{
		var emp=
		{
			employee_id:'50'+i,
			first_name:'李四'+i
			,birthDay:'2010-03-15 18:10:20'  
			//,birthDay:'' //服务端为null(只用全局配置和只用类级配置)
		 };
		paramList.push(emp);
	}
}

spring-web-4.0.6.RELEASE.jar/META-INF/services/javax.servlet.ServletContainerInitializer 中是 SpringServletContainerInitializer (实现了标准的servlet类 ServletContainerInitializer ),
 内部读取了 实现 WebApplicationInitializer  类(spring自己 为不使用web.xml设计)
public class MyWebApplicationInitializer implements  WebApplicationInitializer
{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException
	{
		String configClazz=String.join(",", MvcConfig.class.getName(),MyBatisConfig.class.getName());
		Dynamic dync=servletContext.addServlet("spring_mvc", DispatcherServlet.class);
		dync.setLoadOnStartup(1);
		dync.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, configClazz);
		dync.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		dync.addMapping("*.mvc");
		//servletContext.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		//servletContext.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, configClazz);
		//servletContext.addListener(ContextLoaderListener.class);
	}
}

//相当于web.xml
public class SpringMVCInitializer extends AbstractAnnotationConfigDispatcherServletInitializer 
{ 
	//spring容器
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {
//				Config.class
				} ;
	}
	//servletContext
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return  new Class[] {
//				WebConfig.class
		};
	}
	//url-mapping
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
}

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
    System.out.println("===========应用到所有@RequestMapping注解的方法，processUnauthenticatedException在其抛出异常类为:"+e.getClass()+",原因为:"+e.getMessage());
    return "showError"; //返回一个逻辑视图名
    }
	
	//地址栏要数字传字符即Controller方法参数@PathVariable("page")int pageNO接收数字会报MethodArgumentTypeMismatchException 
    @ExceptionHandler(MethodArgumentTypeMismatchException.class) 
    @ResponseBody
    public  Map<String, Object> typeMismatch(NativeWebRequest request,HttpServletResponse response, RuntimeException e) {
        System.out.println("===========应用到所有@RequestMapping注解的方法，typeMismatch在其抛出异常类为:"+e.getClass()+",原因为:"+e.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "900");
        map.put("reason", "不可转换为数字的字符");
        return map;
        //return "{status:'900',reason:'不可转换为数字的字符'}";//如返回字符串中文不支持？？？
    }
	//js中的salary写字符转服务端@RequestBody Employee 的数字 报HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class) 
    @ResponseBody
    public  Map<String, Object> notReadable(NativeWebRequest request,HttpServletResponse response, RuntimeException e) {
    	System.out.println("===========应用到所有@RequestMapping注解的方法，notReadable在其抛出异常类为:"+e.getClass()+",原因为:"+e.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "901");
        map.put("reason", "JSON不可转换为数字的字符");
        return map;
        //return "{status:'900',reason:'不可转换为数字的字符'}";//如返回字符串中文不支持？？？
    }
}
@RequestMapping(method=RequestMethod.POST,value="/asyncUpload")
//web.xml中所有的filter,和用的servlet加 <async-supported>true</async-supported>
public Callable<String> processUpload(@RequestParam("img") final MultipartFile file) { 
 //@RequestParam("img") CommonsMultipartFile file  
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
	map.put("nullObj",null);//null值可不显示，也可显示为空串，对日期类型传给到服务端为null
	map.put("reason", "原因");
	return map;
	//return "{status:'1231231',reason:'原因'}";//字符串中文支持
}

//配置 Jaxb2RootElementHttpMessageConverter
@RequestMapping(value = "/responseBodyXML", method = RequestMethod.GET, produces="application/xml")
@ResponseBody
// @PostMapping(value = "/responseBodyXML",consumes = "text/html;charset=UTF-8",produces = "text/html;charset=UTF-8")
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

	
	
-------------mockITO  MockMvc 

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

@RunWith(SpringJUnit4ClassRunner.class)  
//@ActiveProfiles({"test"})
//@Transactional
@ContextConfiguration(locations={
		"classpath:test_mockito/spring-mockmvc.xml",
		})
public class MockITO_MockTest  {
//	@InjectMocks //会进入方法体中
//	@Autowired
	//MockitoJUnitRunner x; Mockito的类
	
	@Mock //  可以不是 Spring的Bean 
	private MyServiceBean myServiceBean;  


	ThreadLocalRandom random = ThreadLocalRandom.current();
	@Before
	public void setup() {
		myServiceBean = mock(MyServiceBean.class);//可以不是 Spring的Bean 
	}
 
	@Test
	//@Sql("init.sql")
	public void testService() throws Exception  //测试 OK 
	{ 
		//spring-mockmvc.xml 只有	<context:component-scan base-package="test_mockmvc"></context:component-scan>
		List<Product> dataSet=new ArrayList<Product>();
		for(int i=0;i<3;i++)
		{
			Product product=new Product();
			product.setId(10+i);
			product.setName("产品"+i);
			product.setType("生活用品");
			dataSet.add(product);
		}
		when(myServiceBean.queryData(any(Product.class))).thenReturn(dataSet); 
		List<Product>  res=myServiceBean.queryData(new Product());//没有真实调用 
		System.out.println(res);
		
		reset(myServiceBean);//重置指定的bean的所有录制  
		
		res=myServiceBean.queryData(new Product());
		System.out.println(res);
		//模拟抛异常
		try {
			when(myServiceBean.insertData(ArgumentMatchers.anyList())).thenThrow(RuntimeException.class);
			myServiceBean.insertData(Arrays.asList(new Product()));
		}catch(Exception e)
		{
			System.err.println("error have :"+e );
		}
	}
	 
	@Test
	public void testRestTemplate() 
	{ 
		RestTemplate restTemplate = new RestTemplate();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockServer.expect(requestTo("/greeting")).andRespond(withSuccess()); 
		
		String res=restTemplate.getForObject("/greeting", String.class);//没有真的请求
		System.out.println(res);//没有返回值 ，但没有报错
		
		mockServer.verify();
	}
}
@RunWith(SpringJUnit4ClassRunner.class)  
//@ActiveProfiles({"test"})
//@Transactional
@WebAppConfiguration //可以注入 WebApplicationContext
@ContextConfiguration(locations={
		"classpath:test_mockito/spring-mockmvc.xml",
		})
public class MockMVCTest  {

//	@InjectMocks //会进入方法体中
//	@Autowired
	//MockitoJUnitRunner x; Mockito的类
	@Autowired
	private WebApplicationContext wac;

	public MockMvc mockMvc; //org.springframework.test.web.servlet.MockMvc 
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
	} 
	@Test
	//@Sql("init.sql")
	public void testRealRequest()throws Exception  
	{
		//请求自身的服务
		ResultActions resultActions = mockMvc.perform(
					post("/json/queryEmployeeVO.mvc")
					.characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"employee_id\":123,\"first_name\":\"李四1\"}")
				)//perform是真实的调用了
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json"))
				//.andExpect(content().contentType("application/json")) //如用这个报 expected:<application/json> but was:<application/json;charset=UTF-8>
				
				.andExpect(jsonPath("$.underEmp[0].first_name").value("li"))
				//依赖 com.jayway.jsonpath.Predicate ,   json-smart-2.3.jar , asm-1.0.2.jar(conflict-lib)
				.andExpect(jsonPath("$.underEmp[?(@.first_name == 'li')]").exists())  
				.andDo(print());
		MvcResult mvcResult = resultActions.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		System.out.println(result);
	}
	 
}
----造 request 对象
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test_mockRequest/spring-test.xml"})
/**
XML文件中只有
<context:component-scan base-package="spring_jsp.annotation" /> 
<mvc:annotation-driven  validator="validator"   />
 */
public class ControllerMockRequestTest 
{
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext context;
    @Autowired
    private JSONController jsonController;
    
    @Before  
    public void setUp(){
        String contextPath="/J_SpringMVC";
		context=new MockServletContext();
		context.setContextPath(contextPath);
         
        request = new MockHttpServletRequest(context);    
        request.setCharacterEncoding("UTF-8");
        
        String rootPath=request.getServletContext().getRealPath("/");
        System.out.println("fs rootPath="+rootPath);
        
        String reqContextPath=request.getServletContext().getContextPath();
        System.out.println("ContextPath="+reqContextPath);
        
        request.setRequestURI("http://127.0.0.1:/J_SpingMVC/page.mvc");
        System.out.println("RequestURI="+request.getRequestURI());//如不set就返回空串
       
        request.setContextPath(contextPath); 
        response = new MockHttpServletResponse();
		 
    }
    @Test
    public void testWithSpringRequestObject() {
        HttpSession session = request.getSession(true);
        session.setAttribute("currentDate", new Date());
        Employee emp=new Employee ();
        
        System.out.println("local="+request.getLocale() );
		//真实的请求了，但使用了spring的mock的Request
        EmployeeResult res= jsonController.queryEmployeeVO(request, emp);
        System.out.println(res);
    }
} 

testNG 和 Spring 见Spring.java

=========================上 Spring MVC
 


------------ RestTemplate
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
 mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	 
 SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();  
 requestFactory.setConnectTimeout(8000);// 设置超时
 requestFactory.setReadTimeout(8000);
 RestTemplate template=new RestTemplate(requestFactory);
 //默认解析日期格式为 yyyy-MM-dd'T'HH:mm:ss.SSSZ， RestTemplate 没有全局设置地方，只能在每个属性上加 @JsonFormat
 
 //读源码的方式来修改
template.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter(mapper)));
		 //或者
//		 template.getMessageConverters().forEach(i->{
//			 if(i instanceof MappingJackson2HttpMessageConverter) {//使用XML中有配置
//				 MappingJackson2HttpMessageConverter jackson= (MappingJackson2HttpMessageConverter)i;
//				 jackson.setObjectMapper(mapper);
//			 }
//		 });
 
 
HttpHeaders headers = new HttpHeaders();   
headers.add("Accept", MediaType.APPLICATION_JSON.toString());   
HttpEntity<Map<String,String>> headerAndBody = new HttpEntity<>(null, headers);
Map<String,Object> map=new HashMap<>(); 
map.put("name", "lisi"); 
map.put("age", 29); 
ResponseEntity< Greeting> res= template.exchange(webRoot+"/rest/get.mvc?name={name}&age={age}",HttpMethod.GET, headerAndBody,Greeting.class,map);
//ResponseEntity< Greeting> res= template.exchange(webRoot+"/rest/get.mvc?name={x}&age={y}",HttpMethod.GET, headerAndBody,Greeting.class,"lisi",29);

//这不能强制为String,可能是有MappingJackson2HttpMessageConverter ??报`java.lang.String` out of START_OBJECT
//ResponseEntity< String> res= template.exchange(webRoot+"/rest/get.mvc?name={name}&age={age}",HttpMethod.GET, headerAndBody,String.class,map);
ResponseEntity< Map> res= template.exchange(webRoot+"/rest/get.mvc?name={name}&age={age}",HttpMethod.GET, headerAndBody,Map.class,map);//可强制为Map
 
 

//拦截器读一次，使用处不能再读结果了，RestTemplate必须加特殊处理
RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

//RestTemplate 支持增加拦截器 ,自己的类实现了 implements ClientHttpRequestInterceptor
restTemplate.setInterceptors(Collections.singletonList(new RestLogInterceptor()));  


HttpHeaders headers = new HttpHeaders();
MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
headers.setContentType(type);
headers.add("Accept", MediaType.APPLICATION_JSON.toString());
String json="{\"id\":11,\"content\":\"abc\"}";
HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
Map<String,Object> uriVariables=new HashMap<String,Object>();
uriVariables.put("id", "101"); 
//Greeting result = restTemplate.postForObject(webRoot+"/rest/post.mvc?id={id}",  formEntity, Greeting.class,uriVariables);//POST可以URL参数
//Map result = restTemplate.postForObject(webRoot+"/rest/post.mvc?id={id}",  formEntity, Map.class,uriVariables);//可强制为Map

//这可以强制为String 
String result = restTemplate.postForObject(webRoot+"/rest/post.mvc?id={id}",  formEntity, String.class,uriVariables);

System.out.println(result);


 


//POST form FORM_URLENCODED 使用  MultiValueMap，还要加 AllEncompassingFormHttpMessageConverter，类型不能为String.class

restTemplate.setMessageConverters(Arrays.asList( new MappingJackson2HttpMessageConverter(mapper),new AllEncompassingFormHttpMessageConverter())); 
 
HttpHeaders headers = new HttpHeaders(); 
MediaType type = MediaType.APPLICATION_FORM_URLENCODED;
headers.setContentType(type);
headers.add("Accept", MediaType.APPLICATION_JSON.toString());  
MultiValueMap<String,String> map=new LinkedMultiValueMap<>();
map.add("username","xxx"); 
HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(map, headers);
//String result = restTemplate.postForObject(webRoot+"/form/post.mvc",  formEntity, String.class); 
Greeting result = restTemplate.postForObject(webRoot+"/form/post.mvc",  formEntity, Greeting.class);  //不能为String.class,如为Object.class返回LinkedHashMap
System.out.println("read second time:"+result);




//POST form MultiPart  中的字段 和上传文件   
{
	String path="http://127.0.0.1:8081/J_SpringBoot/multiUpload?id={id}";
	
	HttpHeaders headers = new HttpHeaders(); ;
	headers.add("Accept", MediaType.APPLICATION_JSON.toString());
	headers.add("Content-Type",MediaType.MULTIPART_FORM_DATA_VALUE);
	
	//数据用 MultiValueMap 
	 MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
	 data.add("fileCount", "两个"); 
	  
	 File file1=new File("D:/bak/hello.json");
	 File file2=new File("D:/bak/abc.json");
	 //自己的MultiPartInputStreamResource extends InputStreamResource，用来上传附件，重写getFilename(),contentLength()
	 Resource resource1 = new  MultiPartInputStreamResource(new FileInputStream(file1),file1.getName(),file1.length() );
	 Resource resource2 = new  MultiPartInputStreamResource(new FileInputStream(file2),file2.getName(),file2.length() );
	 data.add("attach", resource1);
	 data.add("attach", resource2);
		
	 HttpEntity<MultiValueMap<String, Object>> headerAndBody = new HttpEntity<>(data,headers);      
	 
 
	Map<String,Object> uriVariables=new HashMap<String,Object>();
	uriVariables.put("id", "101");
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	mapper.setTimeZone(TimeZone.getTimeZone("GMT+8")); 
	restTemplate.setMessageConverters(Arrays.asList( new MappingJackson2HttpMessageConverter(mapper)));
	restTemplate.getMessageConverters().add(new AllEncompassingFormHttpMessageConverter());//POST FORM
	restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter()); //POST MultiPart ,数据用 MultiValueMap 
 
	//两种方式  如上传文件是UTF8，项目文件是GBK，文件内容Interceptor日志乱码
	
	ResponseEntity<Map>  response= restTemplate.exchange(path, HttpMethod.valueOf("POST"), headerAndBody,Map.class,uriVariables);
	System.out.println("response:"+response);
}


public class RestLogInterceptor implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add("Authorization", "xx");
		System.out.println("rest template before request method:"+request.getMethod()+",url==>"+request.getURI());
		System.out.println("rest template before request header==>"+headers);
		
		//可能有文件上传的 byte[],如比较大图片不太好处理了，解析HTTP有点麻烦
		//Content-Disposition: form-data; name="file"; filename="8.jpg"
		//Content-Type: image/jpeg
		//Content-Length: 33418
		System.out.println("rest template before request body ==>"+new String(body,"UTF-8"));  
		ClientHttpResponse resp = execution.execute(request, body);
		System.out.println("rest template after resp Code==<"+resp.getRawStatusCode());
		
		if(resp.getRawStatusCode()==200) { 
			System.out.println("rest template after resp header==<"+resp.getHeaders()); 
			String ser = getHeader(resp, "Wechatpay-Serial");
			
			//拦截器读一次，使用处不能再读结果了，RestTemplate必须加特殊处理
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resp.getBody(),"UTF-8"));
	        String line = bufferedReader.readLine();
	        while (line != null) {
	            inputStringBuilder.append(line);
	            inputStringBuilder.append('\n');
	            line = bufferedReader.readLine();
	        }
	        System.out.println("rest template after resp body==<"+inputStringBuilder.toString());
		}
		return resp;
	}
	private String getHeader(ClientHttpResponse resp, String head) {
		List<String> values = resp.getHeaders().get(head);
		if (values!=null && values.size() > 0)
			return values.get(0);
		else
			return null;
	}
}
{
	//非正常的rest请求的响应为text/html 的 兼容处理
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	mapper.setTimeZone(TimeZone.getTimeZone("GMT+8")); 
	//restTemplate.setMessageConverters(Arrays.asList( new MyMappingJackson2HttpMessageConverter(mapper)));
	restTemplate.setMessageConverters(Arrays.asList( new MyStringHttpMessageConverter())); 
	 //ResourceHttpMessageConverter 处理二进制的
}
/**用于支持text/html的返回，这支持json解析，但如果返回错误非json就不行了
 */
public	class MyMappingJackson2HttpMessageConverter extends  MappingJackson2HttpMessageConverter { 
	public MyMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		List<MediaType> supportedMediaTypes =new ArrayList<>  ();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		this.setSupportedMediaTypes(supportedMediaTypes);
	}
}
/**用于支持text/html的返回，对于 MyMappingJackson2HttpMessageConverter 不能处理错误，自己解析json
 */
public	class MyStringHttpMessageConverter extends  StringHttpMessageConverter { 
	public MyStringHttpMessageConverter( ) { 
		List<MediaType> supportedMediaTypes =new ArrayList<>  ();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(MediaType.ALL);
		super.setSupportedMediaTypes(supportedMediaTypes);
		super.setDefaultCharset(StandardCharsets.UTF_8);
	}
}
------------ Spring整合Servlet

 <!--Spring整合Servlet  Filter类中就可以注入Spring容器中的类 , WebApplicationContextUtils 不如这种方便 -->
   <filter>
    <filter-name>myFilterWithSpring</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>myFilterWithSpring</filter-name>
    <url-pattern>/*</url-pattern> */
  </filter-mapping>
  
@Component("myFilterWithSpring")
public class MyFilterWithSpring implements Filter
{
	@Autowired
	private Validator validator; //Filter类中就可以注入Spring容器中的类 ,WebApplicationContextUtils 不如这种方便
}

------------spring session redis
 
<dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId>
	<version>2.0.4.RELEASE</version>
</dependency>
 
	<bean  id="redisStandaloneConfiguration" class="org.springframework.data.redis.connection.RedisStandaloneConfiguration">
		<property name="hostName" value="127.0.0.1"></property>
		<property name="port" value="6379"></property>
		<property name="database"  value="0"></property>
		<!-- 
		<property name="password"  >
			<bean class="org.springframework.data.redis.connection.RedisPassword" factory-method="of" >
				<constructor-arg value="redisPass"/>
			</bean>
		</property>
		 -->
	</bean>
	 
	<bean id="connectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"  >
		<constructor-arg  ref="redisStandaloneConfiguration"> </constructor-arg> 
	</bean>

   <!--  二选一
	<bean class="org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory">
		<constructor-arg ref="redisStandaloneConfiguration">  </constructor-arg>
	</bean>
	 -->

	 
	 
  <context:annotation-config/> 
	<!-- 此配置可不用打开Spring ContextLoaderListener
		创建 springSessionRepositoryFilter,即是 SessionRepositoryFilter 的实例
	( @EnableRedisHttpSession 注解创建一个Bean名字为 springSessionRepositoryFilter 	)
	RedisHttpSessionConfiguration 类的父类是带@Configuration中有一个方法名叫 springSessionRepositoryFilter 创建的  
	-->
  <bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration">
      <property name="maxInactiveIntervalInSeconds" value="600"></property>
	  <!-- spring session redis 设置  10分钟过期 -->
	  <property name="redisNamespace" value="MyProject"></property> <!--redis 的key前缀为 MyProject:session -->
  </bean>
   

  <!--   spring session 总开关, 如项目中有使用spring security 要放在    springSecurityFilterChain 前面
		, 如项目中有使用 Shiro 要放在    shiroFilter 前面-->
  <filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>   */
  </filter-mapping>
  
  request.getSession().setAttribute("key","valu"); //就把session的值存放在redis中
  
   
<!-- 启用 HttpSessionListener 集成spring session会调用三次 sessionCreated ？？ -->
	<bean id="sessionEventHttpSessionListenerAdapter" class="org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter">
		<constructor-arg >
			<list>
				<bean class="myservlet.listener.MySessionListener"></bean>
				<!--  只能是 HttpSessionListener ,不用在 servlet 中配置
				<bean class="myservlet.listener.MySessionAttributeListener"></bean>
				 -->
			</list>
		</constructor-arg>
	</bean>
  	<bean class="org.springframework.session.data.redis.RedisOperationsSessionRepository">
  		<constructor-arg ref="jedisTemplate"></constructor-arg>
  	</bean>
	<bean id="stringRedisSerializer" class="org.springframework.data.redis.serializer.StringRedisSerializer"/>  
	<bean id="jedisTemplate" class="org.springframework.data.redis.core.RedisTemplate"
		  p:connectionFactory-ref="connectionFactory">
		<property name="keySerializer" ref="stringRedisSerializer"/>  
	    <property name="hashKeySerializer"  ref="stringRedisSerializer"/> 
	 	<property name="valueSerializer">
		   <bean class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer"/>   
		</property>  <!-- 配置成JSON spring session redis中值也是二进制 -->
	 </bean>
	 
@Bean
public MethodInvokingFactoryBean objectMapper()
{
	ObjectMapper mapper=new ObjectMapper();
	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));//对Timestamp类型
	mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL);//不显示null
	/*
	//自定义null值如何显示
	DefaultSerializerProvider.Impl iml=new DefaultSerializerProvider.Impl();
	iml.setNullValueSerializer(new JsonSerializer<Object>() {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException
		{
			gen.writeString("");  
		}});
	mapper.setSerializerProvider(iml);
	*/ 
	//代理
	MethodInvokingFactoryBean proxy=new MethodInvokingFactoryBean();
	proxy.setTargetObject(mapper);
	proxy.setTargetMethod("configure");
	proxy.setArguments(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);//false=反序列化遇到未知属性不报异常
	return  proxy;
}
@Bean
public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Autowired ObjectMapper objectMapper)
{
	MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
	converter.setObjectMapper(objectMapper);
	return converter;
}
@Bean
public RequestMappingHandlerAdapter requestMappingHandlerAdapter(@Autowired MappingJackson2HttpMessageConverter jsonConverter)
{
	RequestMappingHandlerAdapter handler=new RequestMappingHandlerAdapter();
	handler.setMessageConverters(Arrays.asList(jsonConverter,
			new Jaxb2RootElementHttpMessageConverter(),//xml
			new StringHttpMessageConverter()));
	handler.setCacheSeconds(0);
	//handler.setWebBindingInitializer(webBindingInitializer);
	return handler;
}
------------spring session hazelcast
spring session项目中

https://github.com/spring-projects/spring-session-bom/#spring-session-bom
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.session</groupId>
			<artifactId>spring-session-bom</artifactId>
			<version>Dragonfruit-RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>


<dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-hazelcast</artifactId>
 </dependency>

https://docs.spring.io/spring-session/docs/2.3.0.RELEASE/reference/html5/guides/java-hazelcast.html
 
@EnableHazelcastHttpSession 
@Configuration
public class HazelcastHttpSessionConfig {
	@Bean
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();
		config.setInstanceName("myHazelInst") ;//对应于管理页的clusterName
		MapAttributeConfig attributeConfig = new MapAttributeConfig()
				.setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
				.setExtractor(PrincipalNameExtractor.class.getName());
		config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME) 
				.addMapAttributeConfig(attributeConfig).addMapIndexConfig(
						new MapIndexConfig(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));
		return Hazelcast.newHazelcastInstance(config); 
	}
}


------------spring websocket 

//@方式
@Configuration 
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler/{ID}")
        .addInterceptors(new HttpSessionHandshakeInterceptor())
        //.setAllowedOrigins("*")
        ;
    }
    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
}

<!-- XML 配置方式   
	测试方法 http://localhost:8080/J_SpringMVC/myHandler/ID=1.mvc 
	 返回 Can "Upgrade" only to "WebSocket".
-->
<websocket:handlers>
	<websocket:mapping path="/myHandler/{ID}" handler="myHandler"/>
	<websocket:handshake-interceptors>
		<bean class="org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor"/>
	</websocket:handshake-interceptors>
</websocket:handlers>
<bean id="myHandler" class="springweb_websocket.MyHandler"/>

public class MyHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
    	
		System.out.println("收到的消息"+message.getPayload());
    	try {
    		session.sendMessage( new TextMessage("Spring websocket 的消息")); 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

var ws = new WebSocket("ws://localhost:8080/J_SpringMVC/myHandler/ID=1.mvc");
ws.onopen = function()
{
  ws.send("JS 端 消息");
  alert("Message is sent...");
};
ws.onmessage = function (evt) 
{ 
  var received_msg = evt.data;
  alert("Message is received:"+received_msg);
};
ws.onclose = function()
{ 
  alert("Connection is closed..."); 
};

--sockJS　
SockJS  首先用webSocket,如果失败再偿试用其它协议
 https://github.com/sockjs/sockjs-client/
      的 dist目录有　sockjs.js　sockjs.map 文件 版本1.4.0  
 <dependency>
    <groupId>org.webjars</groupId>
    <artifactId>sockjs-client</artifactId>
    <version>1.1.2</version>
</dependency>

--STOMP (Simple Text Oriented Messaging Protocol) 　
 实现的有 Spring 有实现服务端 和 客户端 
 http://stomp.github.io/implementations.html
 服务端的实现有　RabbitMQ　  					规范 1.0 ,1.1, 1.2 版本　
 ActiveMQ 的NIO版本  Artemis 　	规范 1.0 ,1.1, 1.2 版本　
 客户端Java实现(也有服务端实现)  Stampy    		规范 1.2 版本
  http://mrstampy.github.io/Stampy/
  <dependency>
   <groupId>asia.stampy</groupId>
   <artifactId>stampy-NETTY-client-server-RI</artifactId>
   <version>1.0-RELEASE</version>
  </dependency>
 客户端JS实现   stomp.js    规范 1.0 , 1.1 版本 
 
  主播的实现技术
  http://jmesnil.net/stomp-websocket/doc/  实现了STOMP 1.0 和 1.1  有下载　 stomp.js　和 stomp.min.js 
  

<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>stomp-websocket</artifactId>
    <version>2.3.3</version>
</dependency>

见SpringBoot 

------------spring mvc cors

 <!-- 可配置多个mvc:mapping ,也可使用CorsRegistry在代码级配置
<mvc:cors>
	 <mvc:mapping path="/cors/**"  
		allowed-origins="http://localhost:8080, http://127.0.0.1:8080"  
		allowed-methods="GET, POST"  
		allowed-headers="content-type"
		allow-credentials="false"  
		max-age="123" />  
</mvc:cors>
-->

//@Configuration //注释就关闭
//@EnableWebMvc
public class CorseWebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	/*也可以使用xml配置
		<mvc:cors>
			 <mvc:mapping path="/cors/**"  
		        allowed-origins="http://localhost:8080, http://127.0.0.1:8080"  
		        allowed-methods="GET, POST"  
		        allowed-headers="content-type"
		        allow-credentials="false"  
		        max-age="123" />  
		   <!-- 可配置多个mvc:mapping -->
		</mvc:cors>
        */
        registry.addMapping("/cors/**")
            .allowedOrigins("http://127.0.0.1:8080","http://localhost:8080")
            .allowedMethods("GET", "POST")
            .allowedHeaders("content-type")
            //.exposedHeaders("header1", "header2")
            .allowCredentials(true).maxAge(3600);
    }
	 //方式二 使用spring的 Filter 
     @Bean
     public CorsFilter corseFilter()
     {
    	UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
    	CorsConfiguration config=new CorsConfiguration();
    	config.setAllowCredentials(true);
    	config.setAllowedHeaders(Arrays.asList("content-type"));
    	config.setAllowedMethods(Arrays.asList("OPTIONS","GET","POST"));
    	config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:8080","http://localhost:8080"));
    	//config.setMaxAge(maxAge);//
    	source.registerCorsConfiguration("/cors/**", config);
    	return new CorsFilter(source);//spring 4.2的新功能
		//因为已经有*.mvc的配置了，不用url-mapping
    	//spring security有 Cors 的支持
     }
	 
}
----
HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
WebApplicationContext webApplicationContext=WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());




		