https://javaee.github.io/glassfish/documentation Java EE 8 many
https://javaee.github.io/javaee-spec/javadocs/  JavaEE 8 API

<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>


==================================web.xml
<context-param>
	<param-name>life-name</param-name>
	<param-value>LifeServler的值</param-value>
</context-param>
<listener>
 	<listener-class>myservlet.listener.MyContextAttributeListener</listener-class>
</listener>
<error-page><!-- JSP中 可有  isErrorPage="true" ,request.getAttribute("javax.servlet.error.exception"); -->
	<error-code>404</error-code>
	<location>/WEB-INF/jsp/error/notFound.jsp</location>
</error-page>
<error-page>
	<exception-type>java.lang.RuntimeException</exception-type>
	<location>/error.jsp</location>
</error-page>
<session-config>
    <session-timeout>20</session-timeout><!-- 20分 -->
</session-config>
 <jsp-config>
	<taglib>
		<taglib-location>/WEB-INF/tlds/customTag.tld</taglib-location>
		<taglib-uri>/mytag</taglib-uri>
	</taglib>
 </jsp-config>
 
 
<servlet>
	<servlet-name>myJsp</servlet-name>
	<jsp-file>/security/develop/index.jsp</jsp-file>
	 <run-as>
		<role-name></role-name>
	</run-as>
	<security-role-ref>
		<role-name>manager</role-name><!-- 代码中使用的  request.isUserInRole("manager")-->
		<role-link>develop</role-link><!-- 容器中使用的 -->
	</security-role-ref>
</servlet>
<servlet-mapping>
	<servlet-name>myJsp</servlet-name>
	<url-pattern>/devIndex</url-pattern>
</servlet-mapping>
<!-- <transport-guarantee> 对http是 NONE,对https是 CONFIDENTIAL,INTEGRAL ,<url-pattern> 不能对特定路径???  -->
<security-constraint>
	<web-resource-collection>
		<web-resource-name>market Area</web-resource-name>
		<url-pattern>*.html</url-pattern>
		<http-method>GET</http-method>
		<http-method>POST</http-method>
	</web-resource-collection>
	<auth-constraint>
		<role-name>market</role-name>
	</auth-constraint>
	<user-data-constraint>
		<transport-guarantee>NONE</transport-guarantee> 
	</user-data-constraint>
</security-constraint>
<security-constraint>
	<web-resource-collection>
		<web-resource-name>develop Area</web-resource-name>
		<url-pattern>*.jsp</url-pattern>
		<http-method>GET</http-method>
		<http-method>POST</http-method>
	</web-resource-collection>
	<auth-constraint>
		<role-name>develop</role-name>
	</auth-constraint>
	<user-data-constraint>
		<transport-guarantee>NONE</transport-guarantee> 
	</user-data-constraint>
</security-constraint>

<security-role>
	<role-name>market</role-name>
</security-role>
<security-role>
	<role-name>develop</role-name>
</security-role>

<!-- BASIC,FORM 只可使用一种
配置tomcat-users.xml
<role rolename="market"/>
<role rolename="develop"/>
<user username="lisi" password="123" roles="develop"/>
<user username="zhang" password="123" roles="market"/>

密码保存在数据库 tomcat/docs/realm-howto.html
create table users (
  user_name         varchar(15) not null primary key,
  user_pass         varchar(15) not null
);
create table user_roles (
  user_name         varchar(15) not null,
  role_name         varchar(15) not null,
  primary key (user_name, role_name)
);

jdbc.jar 放入tomcat/lib

<Realm className="org.apache.catalina.realm.JDBCRealm"
	  driverName="org.h2.Driver" connectionURL="jdbc:h2:tcp://localhost/~/test" connectionName="sa" connectionPassword =""
	   userTable="users" userNameCol="user_name" userCredCol="user_pass"
   userRoleTable="user_roles" roleNameCol="role_name"/>
   
insert into users(user_name,user_pass) values('lisi','123');
insert into users(user_name,user_pass) values('zhang','123');
insert into user_roles(user_name,role_name) values('lisi','develop');
insert into user_roles(user_name,role_name) values('zhang','market'); 
-->
 
<!--BASIC 方式 ,会弹出输入用户名密码 ,在<realm-name>中不能写中文??? ,没有注销方法????
<login-config>
	<auth-method>BASIC</auth-method>
	<realm-name>BASIC access.jsp use lisi to login,基本验证</realm-name>
</login-config>
-->
<!--FORM 不能使用response.encodeURL(), 规范要求action="j_security_check" name="j_username" name="j_password" -->
<login-config>
	<auth-method>FORM</auth-method>
	<form-login-config>
		<form-login-page>/security/loginForm.html</form-login-page>
		<form-error-page>/error.jsp</form-error-page>
	</form-login-config> 
</login-config>
 
==================================Servlet
HttpServlet//单实例,最好不要加属性,要用synchroized 或 final的常量
public void init(ServletConfig config) {//如不加load-on-startup,第一次请求时初始化 
	ServletContext contex=config.getServletContext();
	String contexParam=contex.getInitParameter("life-name");
}
public void destroy() {}
//service方法默认实现是来决定调用doGet还是doPost方法

//禁用Cookie,IE中tools->internet options->privacy->advanced->选中override automatic cookie handling,再选两个block
//对localhost无效,要使用本机IP
HttpSession session=request.getSession();
Object sessionObj=session.getAttribute("sessionObj");
if(sessionObj==null)
{
	MySessionUser u=new MySessionUser();
	session.setAttribute("sessionObj", u);
}
//如有禁用Cookie使用response.encodeURL("");会自动加jsessionid的参数
request.getRequestDispatcher(response.encodeURL("/ok.jsp")).forward(request, response);
<form action="<%=response.encodeURL("life")%>"   ,危险可以把链接发给其它去点
		
if("POST".equals(request.getMethod()))
	request.setCharacterEncoding("UTF-8");//对<form method="post"生效,如是get无效,

String username=request.getParameter("username");
if("GET".equals(request.getMethod()))
	username=new String(username.getBytes("iso8859-1"),"UTF-8");
//javac -encoding GBK XX.java

session.isNew();
session.getId();//jsessionid为键的值 ,<form action="abc;jsessionid=xxx"
session.getCreationTime();
session.getLastAccessedTime();
session.getMaxInactiveInterval();
session.invalidate();//注销
		
response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"系统繁忙");//SC=Server Code
response.sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION,"未授权");
response.sendError(HttpServletResponse.SC_NOT_FOUND,"找不到页");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"出现错误");
response.sendRedirect("/ok.jsp");
Cookie cookie = new Cookie("cookiename","cookievalue");
response.addCookie(cookie);

//动态网页中禁止缓存
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

request.getRequestDispatcher("/ok.jsp").forward(request, response);//后面的不会被执行
request.getRequestDispatcher("/ok.jsp").include(request, response);
request.getCookies();
	
PrintWriter writer =response.getWriter();
writer.println("<h2>中国</h2>");
writer.close();//如是被其它Servlet,调用request.getRequestDispatcher("/ok.jsp").include(),就不要关闭


其它的Listener
ServletContextListener
ServletContextAttributeListener
ServletRequestAttributeListener
ServletRequestListener
HttpSessionAttributeListener	attributeAdded,attributeRemoved,attributeReplaced
HttpSessionListener  sessionCreated,sessionDestroyed

Listener->Filter->Servlet(load-on-startup)加载顺序

==================================Servlet 3.0

@WebServlet(urlPatterns={"/page1","page2"}, initParams = {@WebInitParam(name = " default_market " ,  value = " NASDAQ " )})
//@WebInitParam(name = " default_market " , value = " NASDAQ " )
public class MyServlet3 extends HttpServlet {}

<servlet-class>
	<async-supported>true</async-supported>
或者
@WebServlet( asyncSupported=true

AsyncContext ctx=request.startAsync(); //走的Filter也要异步支持
ctx.addListener(new AsyncListener()
				{
					public void onComplete(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onComplete  ");
					}
					public void onError(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onError ");
					}
					public void onStartAsync(AsyncEvent event) throws IOException
					{
						System.out.println("MyAsyncServlet onStartAsync");
					}
					public void onTimeout(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onTimeout");
					}  
				});
context.complete();//会调用  Listener的complete方法



@WebListener
public class MyContxtListener3 implements ServletContextListener 
{
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context=event.getServletContext();
		
		//动态注册Servlet 
		ServletRegistration.Dynamic dynServ=context.addServlet("myServName", MyServlet3.class);
		dynServ.setInitParameter("myparam", "myvalue");//servlet重写init方法，用ServletConfig取
		dynServ.addMapping("/dynServ","/dynServ2");
		dynServ.setAsyncSupported(true);
		
		//动态注册Filter
		FilterRegistration.Dynamic  dynFilter=context.addFilter("myFileterName",MyFilter3.class); 
		dynFilter.setAsyncSupported(true);
		dynFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST) , true, "/async");//true=isMatchAfter 
		
 	}
}

@WebFilter(filterName = "myFilter", urlPatterns = { "/dynServ","/test1" },asyncSupported=true)
public class MyFilter3 implements Filter{}

文件上传的原生支持
@MultipartConfig(maxFileSize=50*1024*1024) //用于支持文件上传 enctype="multipart/form-data"

req.setCharacterEncoding("UTF-8");

Collection<Part> parts=req.getParts();//包括表单参数 <input type="file" multiple />
for(Part file1:parts) 
{
			String fname=file1.getSubmittedFileName();
			if(fname==null)//表单域
				continue;
}

Part file1=req.getPart("attache1");//是<input type=file name="attache1" 
String name=file1.getName();//attache1 
String pairs=file1.getHeader("content-disposition");
String nameHeader="filename=";
String path=pairs.substring(pairs.indexOf(nameHeader)+nameHeader.length()+1,pairs.lastIndexOf("\""));
if(!"".equals(path))
{
	String filename=path.substring(path.lastIndexOf("\\")+1);//只IE是带C:\,和req.setCharacterEncoding("UTF-8")中文 OK
	file1.write("d:/temp/"+filename);//一个Part要调用一次write
}

InputStream input=file2.getInputStream();
String param=req.getParameter("username");
System.out.println(new String(param.getBytes("iso8859-1"),"UTF-8"));//中文 OK
----无web.xml
public interface WebParameter {    
    public void loadInfo(ServletContext servletContext) throws ServletException;    
}  
public class ServletParameter implements WebParameter {    
    @Override    
    public void loadInfo(ServletContext servletContext) throws ServletException {    
        ServletRegistration.Dynamic testServlet=servletContext.addServlet("test","servlet3_new.nowebxml.TestServlet");    
        testServlet.setLoadOnStartup(1);    
        testServlet.addMapping("/nowebxml");    
    }    
}
//(不能是web项目的META-INF) 是classpath 或者 WEB-INF/lib/xxx.jar/META-INF/services/javax.servlet.ServletContainerInitializer 中写实现 implements ServletContainerInitializer 全类名
@HandlesTypes(WebParameter.class)    
public class WebConfiguration implements ServletContainerInitializer {    
    @Override    
    public void onStartup(Set<Class<?>> webParams, ServletContext servletCtx)throws ServletException 
   { //webParams 的值为项目中所有实现  @HandlesTypes(WebParameter 的类
        if (webParams != null)
        {    
            for (Class<?> paramClass : webParams) 
            {    
                if (!paramClass.isInterface() && !Modifier.isAbstract(paramClass.getModifiers()) &&    
                        WebParameter.class.isAssignableFrom(paramClass)) //是多余的判断
                {    
                    try {    
                        ((WebParameter) paramClass.newInstance()).loadInfo(servletCtx);    
                    }    
                    catch (Throwable ex) {    
                        throw new ServletException("Failed to instantiate WebParam class", ex);    
                    }    
                }    
            } 
        } 
    } 
}

x.jar/META-INF/resources/ 被视为web根目录,如下
x.jar/META-INF/resources/WEB-INF/web.xml
x.jar/META-INF/resources/WEB-INF/web-fragment.xml  即 web.xml的模块化
	根元素为<web-fragment>
	<name></name> <!-- 表示模块名称  -->
	  <ordering>
              <before>
                    <others/>   <!-- 表示第一个加载 -->
              </before>
			<!--  <after><name>A</name></after> 表示比A后面加载  -->
       </ordering>
	   
	   
==================================Servlet 4 要 Tomcat 9
Tomcat 9 配置打开HTTP 2 

tomcat9/logs/localhost_access_log.x.txt  显示是 HTTP/2.0
Chrome network 标签打开protocol列值是h2
//Chrome工具 自带的工具 chrome://net-internals/  -> HTTP/2->点表格中的ID列的值->复选一行
 看报文    HTTP2_SESSION_RECV_PUSH_PROMISE 有 
	  --> id = 1
      --> promised_stream_id = 2
	  
//---PushBuilder
PushBuilder pushBuilder=req.newPushBuilder();//如果不支持返回null
if (pushBuilder != null)
{  //push 非阻塞
   pushBuilder.path("img/bing.png").push();
   pushBuilder.path("js/md5.js").push(); 
} 
getServletContext().getRequestDispatcher("/servlet4.jsp").forward(req, resp);

//servlet4.jsp 中有引用  img/bing.png 和 js/md5.js 文件

//---HttpServletMapping
@WebServlet({"/path/*", "*.ext"})

 HttpServletMapping mapping = request.getHttpServletMapping();
MappingMatch extension= MappingMatch.EXTENSION;
MappingMatch path= MappingMatch.PATH;
String map = mapping.getMappingMatch().name();
//如请求是 path/servlet4 值是PATH
//如请求是 servlet4.ext 值是EXTENSION

String value = mapping.getMatchValue();
//如请求是 path/servlet4 值是 servlet4
//如请求是 servlet4.ext  值是 servlet4

String pattern = mapping.getPattern();
//如请求是 path/servlet4 值是 /path/*
//如请求是 servlet4.ext 值是*.ext

String servletName = mapping.getServletName();//servlet4_new.Servlet4Mapping

--servlet 4 other
新加两个类 HttpFilter extends GenericFilter //看源码其实很简单


int sessionTimeout=req.getServletContext().getSessionTimeout();
req.getServletContext().setSessionTimeout(sessionTimeout+10);

String enc=req.getServletContext().getRequestCharacterEncoding();
req.getServletContext().setRequestCharacterEncoding("UTF-8");

req.getServletContext().addJspFile("servletName", "/jspFile");
ServletRegistration reg= req.getServletContext().getServletRegistration("servletName");//servlet 3
reg.getInitParameters();
reg.getMappings();

==================================JSP 2.0
内置对象
page,pageContext,request,response,session,out,exception
application 是 ServletContext
config  是  ServletConfig

request.getLocalPort();

String realpath=request.getSession().getServletContext().getRealPath("/");//有项目名
String project=request.getContextPath();// 只项目名/
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ project ;

request.getRequestDispatcher("/pages/xxx.jsp").forward(request,response);

out.clear();
out = pageContext.pushBody();
可以防止 java.lang.IllegalStateException: getOutputStream() has already been called for this response 

//验证码的生成
BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
Graphics g = image.getGraphics();
//...
g.drawImage(new ImageIcon("x.gif").getImage(),0,0,width,height,null);//可以放大,缩小图片
g.dispose();
response.setContentType("image/jpeg");
ImageIO.write(image, "JPEG", response.getOutputStream());

	
<%@include file="one.jsp" %> <!--指令, 先引入文件再编译,可能有变量名重定义的错误 -->
<jsp:include page="one.jsp">
	<jsp:param name="" value=""/>
</jsp:include><!--动作 , 引入文件的执行后的静态结果 -->


<jsp:useBean id="my" class="myservlet.MySessionUser" scope="request" >
</jsp:useBean>
<jsp:setProperty property="*" name="my"/> <!-- * 表示请求的参数的名与Bean的属性名对应 ,自动传入-->
<jsp:setProperty property="name" name="my" value="lisi"/> 
name的值是:<jsp:getProperty property="name" name="my"/>

--jspx其实就是以xml语法来书写jsp的文件
<?xml version="1.0" encoding="utf-8"?>
<jsp:root version="2.0" 
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core" >
	
	<jsp:directive.page import="javax.portlet.PortletPreferences"/>
	<jsp:directive.page import="java.util.Map"/>
	<jsp:declaration>
		int i;
	</jsp:declaration>
	
	<jsp:directive.include file="common.jsp"/>   会有变量重定义 
	<jsp:include page="menu.jsp" />
	
	<jsp:scriptlet>
	  String greeting =  "greeting";
	</jsp:scriptlet>
	 
	<jsp:expression>greeting</jsp:expression>
	
	<jsp:text>这里是内部文本 &#x20; 是空格 </jsp:text> <!-- 可不使用jsp:text -->
  	这里是外部文本
	
	jspx中使用<!-- -->就可注释服务端标签,因是XML
	不能使用泛型,//的java注释也不行
	 
</jsp:root>
==================================EL 表达式
+,-,*,/或div,%或mod
${xx.id} 或 ${xx["id"]}
(== 或 eq),(!= 或 ne),(< 或 lt),(> 或 gt),(<= 或 lt),(>= 或 gt),(&& 或 and),(|| 或 or) ,(! 或 not)
${xx eq null} 
${empty xx}   ${not empty  xx}
${A?B:C} //如A为true返回B,否则返回C
${23*(5-2)} 

EL中隐含对象,pageContext,pageScope,requestScope,sessionScope,applicationScope,param,paramValues,header,headerValues,cookie,initParam
${pageContext.servletContext.serverInfo}
${pageContext.request.requestURL}
${pageContext.request.contextPath}
${pageContext.response.characterEncoding}
${pageContext.session.createTime}
${header["User-Agent"]}//包含一些特殊字符,一定要使用[ ]

${sessionScope.user[data]}中data 是一个变量
${param.name}		//request.getParameter("name");
${paramValues.name}	//request.getParameterValues("name")
${header.name)		//request.getHeader(name)。
${headerValues.name} //request.getHeaderValues(name)。
${cookie.name.value} 
initParam		//ServletContext.getInitparameter(String name)

${user.username}//会按page->request->session->application 顺序查找
instanceof  在EL中是键字

String referer=request.getHeader("referer")
if(referer==null ||   !referer.startsWith("http://localhost:8080/J_JavaEE"))//防盗链,本次请求是人哪个URL过来的
{
	 response.sendRedirect("steal.html");  
	 return;  
}

---可自定义EL表达式函数,类的方法要是 public static 的
TLD文件中
<function>
	<name>toGBK</name>
	<function-class>myservlet.tag.MyELFunc</function-class>
	<function-signature>java.lang.String toGBK(java.lang.String,java.lang.String)</function-signature>
</function>
自定义的EL表达式函数toGBK:${you:toGBK("你好abc123","ISO8859-1")}<br/>

==================================JSTL中
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
	<version>1.2</version>
</dependency>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
-----------c标签库
<c:forTokens items="1,2,3,4,5,6,7,8,9" delims="," var="num">
	<option value="${num }">${num }</option>
</c:forTokens>

<!-- <c:forEach   varStatus的值是 LoopTagStatus  
		status.index,status.count,status.first,status.last, 
		status.begin,status.end, status.current-->
<c:forEach items="${messages}" var="item" begin="0" end="9" step="1" varStatus="var">
		<c:if test="${var.index % 2 == 0}">
		*
		</c:if>
	${item}<br>
</c:forEach>

Map map = new HashMap();
map.put("a","12345"); 
map.put("b","abcde");
<c:forEach items="${map}" var="mymap" > //可以对MAP
		<c:out value="${mymap.key}" /> <c:out value="${mymap.value}" />
</c:forEach>
<c:forEach items="${dataSet}" var="row" varStatus="status"  >
	<tr>
		<td>${status.index}</td> 
		<td>${row['username']}</td><!-- 对Map -->
		<td>${row.password}</td>
		<td><c:out value="${row.age}"/></td>
	</tr>
</c:forEach>
	
<c:if test="${x==6}">
</c:if>

<c:choose>
	<c:when test="${user.role == 'member'}">x</c:when>
	<c:otherwise>y</c:otherwise>
</c:choose>
 
 
<c:set target="${myMap}" property="lisi" value="李四"/> <!-- 可以对Map -->
<c:out value="${myMap.lisi}"></c:out>
<c:remove var="myMap" scope="page"/>
<c:catch var="ex">
	<%
		int i=5;
	int j=0;
	int k=0;
	k=j/k;
	%>
</c:catch>
<br/>异常信息为:
<c:out value="${ex.message}"></c:out> 

<div style="border-style:solid;border-width:5pt; border-color:blue" >
	<c:import url="sessionForm.jsp"></c:import>
</div>
<c:url value="life" var="myUrl"><!--,如以/开头,自动加项目名, 会话跟踪 不行的????esponse.encodeURL(-->
	<c:param name="username" value="李"></c:param>
</c:url>

<c:redirect url="/myhttp.action" context="/J_Struts2"> <!--Tomcat的 <Context 加 crossContext="true" -->
	<c:param name="myParam" value="test123" />  
</c:redirect> <!-- 后面的不会被执行 -->
<%
	System.out.println("JPS的尾部");
%>


-----------fmt标签库
<fmt:setBundle basename="message" scope="session" var="mybindle"/>
${mybindle.locale}

<fmt:setLocale value="zh_CN"/>
<fmt:setLocale value="en_US"/>

<fmt:message bundle="${mybindle}" key="CanNotEmpty">
	<fmt:param>
		<fmt:message bundle="${mybindle}" key="username"/>
	</fmt:param>
</fmt:message>

<fmt:requestEncoding value="GBK"/>
<fmt:setTimeZone value="GMT+8:00"/> <br/>

<fmt:formatNumber value="12.3" pattern=".000" /> <!-- DecimalFormat -->   <br/>
<fmt:formatNumber value="123456.789" pattern="#,#00.0#"/> <!--整数最少两位,小数最少1位 -->
 
number:<fmt:parseNumber value="456.78" integerOnly="true" type="number"></fmt:parseNumber>   <br/>
currency:<fmt:parseNumber value="$456.78" type="currency" parseLocale="en_US"></fmt:parseNumber><br/>
percent:<fmt:parseNumber value="75%" type="percent" ></fmt:parseNumber>  <br/> 

<fmt:formatDate value="<%=new java.util.Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/> <br/>
<fmt:parseDate value="2012-11-22 12:33:22" pattern="yyyy-MM-dd HH:mm:ss" var="myDate"></fmt:parseDate> <br/>
 
-----------sql 标签库
 
<sql:setDataSource  url="jdbc:h2:tcp://localhost/~/test" driver="org.h2.Driver"  user="sa" password="" var="myDataSource"/>
<c:catch var="sqlEx">
	<sql:update sql="drop  table student" dataSource="${myDataSource}" />
</c:catch>
<sql:update sql="create table student (id int,name varchar2(20),age int,birthday date)" dataSource="${myDataSource}" />

<c:forEach begin="1" end="5" step="1" var="i">
	<sql:update sql="insert into  student (id,name,age ,birthday )values(?,?,?,?)" dataSource="${myDataSource}"  >
		<sql:param value="${i}"></sql:param>
		<sql:param value="lisi__${i}"></sql:param>
		<sql:param value="${22+i}"></sql:param>
		<sql:dateParam value="${myDate}"></sql:dateParam>
	</sql:update>
</c:forEach>

<!-- javax.servlet.jsp.jstl.sql.Result -->
<sql:query sql="select * from student where id>? or birthday <?" dataSource="${myDataSource}" maxRows="20" var="students">
	<sql:param value="1"></sql:param>
	<sql:dateParam  value="${myDate}"></sql:dateParam>
</sql:query>

<c:forEach var="row" items="${students.rows}"> <!--rows, rowsByIndex -->
	<c:out value="${row.name}"/> ,
	<c:out value="${row.age}"/> ,
	<c:out value="${row.birthday}"/> <br/>
</c:forEach>
<sql:transaction dataSource="${myDataSource}" isolation="serializable">
	<sql:update sql="update student set age=age+1 where id=1"/>
	<sql:update sql="update student set age=age-1 where id=2"/>
</sql:transaction>
 
 
-----------xml 标签库
<c:import url="student.xml" var="xmlDoc"></c:import>
<x:parse  doc="${xmlDoc}" var="studentDoc"></x:parse>
<x:out select="$studentDoc/students/student[@id>1]/teacher"/> <br/>
<x:out select="$studentDoc//*[name()='teacher'][1]" escapeXml="false"/> <!-- 1开始 -->
 
<br/> set:
<x:set var="teacher" select="$studentDoc//teacher"/>
<x:out select="$teacher"/>

<br/> if:
<x:if select="$studentDoc//student[@name='lisi']">
	<x:out select="$studentDoc/students/student/@name"/>
</x:if>

<br/> choose:
<x:choose>
	<x:when select="$studentDoc//student[@name='lisi']">lisi</x:when>
	<x:when select="$studentDoc//student[@name='wang']">wang</x:when>
	<x:otherwise>其它的</x:otherwise>
</x:choose>

<br/> forEach:
<x:forEach select="$studentDoc//student" var="student" varStatus="status">
	${status.index} ,<x:out select="$student/@name"/> <br/>
</x:forEach> 

<br/>transform:
<c:set var="xmltext">
  <books>
    <book>
      <name>Padam History</name>
      <author>ZARA</author>
      <price>100</price>
    </book>
    <book>
      <name>Great Mistry</name>
      <author>NUHA</author>
      <price>2000</price>
    </book>
  </books>
</c:set>

<c:import url="book.xsl" var="xslt"/>
<x:transform xml="${xmltext}" xslt="${xslt}"/>
<hr/>
<c:import url="book.xml" var="xml"/>
<x:transform doc="${xml}" xslt="${xslt}"/> <!-- doc=和xml=是一样的 -->
-----------fn标签库
contains:${fn:contains("zhangsan","san")} <br/>
containsIgnoreCase:${fn:containsIgnoreCase("zhangsan","SAN")}<br/>
startsWith :${fn:startsWith("zhangsan","zhan")} <br/>
endsWith :${fn:endsWith("zhangsan","san")} <br/>
indexOf :${fn:indexOf("zhangsan","san")} <br/>
replace :${fn:replace("zhangsan","san","123")} <br/>
substring :${fn:substring("zhangsan",7,-1)} <br/>
substringBefore :${fn:substringBefore("zhangsan","sa")} <br/>
substringAfter :${fn:substringAfter("zhangsan","ang")} <br/>
split :<c:set value='${fn:split("zhang;li",";")}' var="names" />
	<c:forEach items="${names}" var="item">
		${item}<br/>
	</c:forEach> <br/>
<%
	String[] welcome=new String[]{"wlcome","you","to","china"};
request.setAttribute("array",welcome);
%>
join :${fn:join(array," ")} <br/> 	
toLowerCase :${fn:toLowerCase("Good")} <br/> 	
toUpperCase :${fn:toUpperCase("Good")} <br/>
trim :${fn:trim(" do it ")} <br/>  
escapeXml :${fn:escapeXml("<br/>")} <br/>  
length :${fn:length("zhang")} <br/> 
length :${fn:length(array)} <br/>  <!-- 也可是List -->
	
	
==================================自定义标签
//类必须在包下
public class MyEmptyTag implements Tag
{
	private PageContext pageContext;
	private Tag parent;
	public void setPageContext(PageContext page) {
		pageContext=page;
	}
	public void setParent(Tag tag) {//如没父标签设置为null
		parent=tag;
	}
	public Tag getParent() {
		return parent;
	}
	public int doStartTag() throws JspException {
		return Tag.SKIP_BODY;
	}
	public int doEndTag() throws JspException {
		JspWriter writer=pageContext.getOut();
		try {
			writer.println("我的自定义标签的输出");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//不要writer.close();
		return Tag.EVAL_PAGE;//JSP的剩余部分继续执行
	}
	public void release() {
	}
}

public class MyBodyTag extends BodyTagSupport
{
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED; //必须是EVAL_BODY_BUFFERED才可用bodyContent对象
		//Servlet容器会缓存实例
	}
	public int doEndTag() throws JspException {
		JspWriter writer=bodyContent.getEnclosingWriter();//TagSupport的成员 
	}
}

MyIteratorTag  extends TagSupport{

public class MySimpleTagSupport  extends SimpleTagSupport{//JSP2.0,Servlet容器不会缓存
	private JspFragment body;
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public void setParent(JspTag parent) {//如没有父不会被调用
	}
	public void setJspBody(JspFragment jspBody) {//如没有体不会被调用
		this.body=jspBody;
	}
	public void doTag() throws JspException, IOException {
		JspContext context=getJspContext();//SimpleTagSupport中的方法
		JspWriter writer=context.getOut();
		writer.println(name);
		writer.println(",");
		body.invoke(null);//null是当前输出流
	}
}
public class MyMaxSimpleTagSupport  extends SimpleTagSupport implements DynamicAttributes
				
customTag.tld 会自动搜索.war/WEB-INF/的所有*.tld 和 .jar/META-INF/所有的*.tld
<?xml version="1.0" encoding="UTF-8" ?>
<taglib xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"
	version="2.0">
	<display-name>My Custom Tag</display-name>
	<tlib-version>1.0</tlib-version>
	<short-name>my</short-name>
	<uri>http://zh.org/tags</uri> 
	<tag>
		<name>hello</name>
		<tag-class>myservlet.tag.MyEmptyTag</tag-class>
		<body-content>empty</body-content> <!-- empty 空标签,JSP   -->
	</tag>
	<tag>
		<name>iterator</name>
		<tag-class>myservlet.tag.MyIteratorTag</tag-class>
		<body-content>JSP</body-content>
		<attribute>
			<name>var</name>
			<required>true</required>
			<rtexprvalue>false</rtexprvalue> <!-- false -->
		</attribute>
		<attribute>
			<name>items</name>
			<required>true</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<variable> <!-- 迭代的中间变量 -->
			<name-from-attribute>var</name-from-attribute>
			<variable-class>myservlet.tag.ValueLabelBean</variable-class>
			<scope>NESTED</scope><!--NESTED  -->
		</variable>
	</tag>
	<tag>
		<name>welcom</name>
		<tag-class>myservlet.tag.simple.MySimpleTagSupport</tag-class>
		<body-content>tagdependent</body-content> <!--  不能是JSP,因 setJspBody(JspFragment), tagdependent  -->
		<attribute>
			<name>name</name>
			<required>true</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
	</tag>
	<tag>
		<name>max_ex</name>
		<tag-class>myservlet.tag.simple.MyMaxSimpleTagSupport</tag-class>
		<body-content>empty</body-content>
		<dynamic-attributes>true</dynamic-attributes>
		<variable>
			<name-given>max</name-given> <!--对应于 context.setAttribute("max" -->
			<variable-class>Integer</variable-class>
			<declare>true</declare>
			<scope>AT_END</scope><!--AT_END ,AT_BEGIN -->
		</variable>
	</tag>
</taglib>

<%@taglib  prefix="my" uri="/WEB-INF/tlds/customTag.tld" %>
<my:iterator var="item" items="${allBean}">
	${item["label"]}  ==  <jsp:getProperty property="value" name="item"/>  <br/>
</my:iterator>

 extends  TagSupport //实现了IterationTag,Tag
	pageContext.getOut();//TagSupport的成员 
==================================标签文件
<%@taglib tagdir="/WEB-INF/tags/" prefix="tg" %>
扩展名是*.tag,*.tagx(使用XML语法),.war会自动搜索/WEB-INF/tags的子目录,.jar是/META-INF/tags/所有*.tag,*.tagx
.tag中有jspContext隐含对象,是PageContext的基类,没有page,pageContext,exception其它同JSP

hello:<tg:hello/> <!-- hello是hello.tag文件名的前部分 -->

welcome:
<tg:welcome >
	<jsp:attribute name="user">李四</jsp:attribute>
	<jsp:body>欢迎</jsp:body>
</tg:welcome> <br/>

escapeXml: 
<tg:toHTML escapeHtml="true">
	<font color="red" >这是true</font>
</tg:toHTML><br/>
<tg:toHTML escapeHtml="false">
	<font color="red" >这是false</font>
</tg:toHTML><br/>

variable 测试:
 <tg:my_var num1="100" num2="2002" num3="303" >
    <jsp:attribute name="great">
        <font color="red">SUM：${sum}</font>
    </jsp:attribute>
    <jsp:attribute name="less">
        <font color="blue">SUM：${sum} </font>
    </jsp:attribute>
</tg:my_var>
--hello.tag
<%@tag pageEncoding="UTF-8" %>
这是第一个tag的测试

--welcome.tag
<%@attribute name="user" required="true" fragment="true" %> 
<!-- 如 fragment="true" ,那么rtexprvalue="true" type="javax.servlet.jsp.tagext.JspFragment" 的值是固定的 ,不能被指定-->
<jsp:invoke fragment="user" /> ,<jsp:doBody/><!-- 只可在.tag文件中使用,都可指定 var或 varReader(只可一个)来保存结果,scope-->
---toHTML.tag
<c:choose>
	<c:when test="${escapeHtml}"> <!-- 这里可以直接得到 @attribute,在jspContext中 -->
		<jsp:doBody var="content"/>
		 <%  out.println(toHtml((String)jspContext.getAttribute("content")));    %>
	</c:when>
	<c:otherwise>
		<jsp:doBody/>
	</c:otherwise>
</c:choose>
 ---my_var.tag
<%@tag pageEncoding="UTF-8" body-content="scriptless" dynamic-attributes="numColumn"%>
<%@ attribute name="great" fragment="true" %>
<%@ attribute name="less" fragment="true" %>
<%@ variable name-given="sum" variable-class="java.lang.Integer" %>
<c:if test="${not empty numColumn}">
	<c:forEach items="${numColumn}" var="num">
		<c:set var="sum" value="${num.value + sum}" />
	</c:forEach>
	<c:if test="${sum >= 1000}" >
		<jsp:invoke fragment="great" />
	</c:if>
	<c:if test="${sum < 1000}" >
		<jsp:invoke fragment="less" />
	</c:if>  
</c:if>
==================================HTTP 断点继传 (下载)
服务器返回的HTTP头有 Accept-Ranges=bytes
浏览器请求的HTTP头有 RANGE: bytes=2000070-
服务器返回的HTTP头 是206 ,Content-Range=bytes 2000070-106786027/106786028

HttpURLConnection  httpConnection.setRequestProperty("RANGE","bytes=2000070");
RandomAccessFile  seek(2020) ,read,write
--
下载文件设置HTTP头,文件名
response.reset();
response.setContentType("application/msexcel");//tomcat的conf/web.xml中
response.setHeader("Content-disposition","inline;filename=workbook.xls");//inline显示在浏览器中

response.setContentType("application/x-msdownload");
response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));//attachment会提示下载
ServletOutputStream output=response.getOutputStream();
output.close();//可能不要关闭
	
==================================JNDI
Context对象的createSubContext(Name name)
		createSubContext(String name)
		bind("name",Object)
		rebind("anme",Object)
		unbind("name")或Name
		close()

要把C:\bea\weblogic90\server\lib下的weblogic.jar放classpath
Hashtable env=new Hashtable();
env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
env.put(Context.PROVIDER_URL,"t3://localhost:7001");
env.put(Context.SECURITY_PRINCIPAL,"username");
env.put(Context.SECURITY_CREDENTIALS,"password");
Context ctx=new InitialContext(env);//如果在容器中就不用传参数了，只对main方法中


web.xml中
	<env-entry>
		<env-entry-name>MyCompany</env-entry-name>
		
		<env-entry-type>java.lang.String</env-entry-type>//或是基本类型的封闭类
		<env-entry-value>aa</env-entry-value>
	</env-entry>

Context ctx=new InitialContext();
String c=(String)ctx.lookup("java:comp/env/MyCompany");


context.lookup("java:comp/env/jdbc/Modeling")

对应一个JNDIname  weblogic中的  数据源变时，只改weblogic，不用改，web.xml
和java 代码，在容器是配置时要ref这个name ?????????


实现数据分离
如果web应用由Servlet容器管理的某个JNDI Resource，
必须在web.xml中声明对这个JNDI Resource的引用。

web.xml
<resource-ref>
	<res-ref-name>jdbc/Modeling</res-ref-name> 
	<res-type>javax.sql.DataSource</res-type>
	<res-auth>Container</res-auth>
</resource-ref>

weblogic.xml
<resource-description>
	<res-ref-name>jdbc/Modeling</res-ref-name>
	<jndi-name>jdbc/Modeling</jndi-name>
</resource-description>


---web.xml
<resource-env-ref>
	<resource-env-ref-name>jms/StockQueue</resource-env-ref-name>
	<resource-env-ref-type>javax.jms.Queue</resource-env-ref-type>
</resource-env-ref>

=================================java mail
发送邮件服务器
	SMTP:Simple Mail Transfer Protocol
接收邮件服务器 
	POP3 :  Post Office Protocol 3，不存放在服务器上，不可在线阅读邮件服务器上的邮件
	IMAP :Internet Message Access Protocol   可在线阅读邮件服务器上的邮件，存储在服务器上

	POP3默认端口110 SMTP默认端口25 IMAP默认端口143
	
	
cmd命令  telent smtp.163.com 25 
	以下不能输错,不能贴粘.
	
	HELO smtp.163.com 	命令 
	250 hz-b-163smtp1.163.com 9561591f-d7ff-4bd5-876a-9fefcf7846e5  返回250表示OK 
	auth login 			命令 
	334 VXNlcm5hbWU6 9561591f-d7ff-4bd5-876a-9fefcf7846e5 
	USER emhhbzFqaW40	 命令 是用户名的Base64 3
	PASS xxxxxx 		命令 是密码的Base64,这里就过不去了!!!!
	后面还有　 
	MAILFROM:XXX@163.COM 
	RCPTTO:XXX@163.COM 
	DATA 
	354 End data with .
	QUIT
	--------另一套
	 EHLO zhaopinpc
	 250-hz-b-163smtp2.163.com
	 250-mail
	 250-PIPELINING
	 250-8BITMIME
	 250-AUTH LOGIN PLAIN
	 250-AUTH=LOGIN PLAIN
	 250 SARTTLS 1ad55c7f-d850-4a65-8d61-3ba8f024113f
	 AUTH LOGIN

	 

javaMail中的URLName(String)
	格式为：   协议名称：//用户名：密码@邮件服务器/
pop3://username:password@163.com/
	 

	 
javax.mail.Session类 getStore("imap")//imaps,pop3,pop3s
下为抽象类
javax.mail.Store 有一个static getFolder()
		.connect(host,usrname,password)
javax.mail.Folder 
	getFolder("xx")
	open(Folder.READ_WRITE)

	getMessageCount() 共有邮件数量
	getUnreadMessageCount()//未读邮件

javax.mail.Message 的子类MimeMessage(Session ) 如mes
		setRecipients(Message.RecipientsType.TO,IternetAddress[] xx)	
				Message.RecipientsType.CC(抄送) BCC密送
		setFrom()
		setText()
		setSubject()
	Transport.send(mes)


javax.mail.Address 的子类在internet包下的 InternetAddress
javax.mail.Transport 发送 static send(Message)


BodyPart 的.getDispostion 如返回是Part.ATTACHMENT或是Part.INNLINE 说明是附件,如是null是正文


javax.mail.Authenticator 抽象类只有getPasswordAuthentication()方法可被 重写
	protected javax.mail.PasswordAuthentication getPasswordAuthentication()
	{ 
		return new javax.mail.PasswordAuthentication(user,password); 
	}


javax.mail.Session 的getDefalutInstance(Properties,Autenticatior)
			
			getnstance(Properties,Autenticatior)

MimeMessge构造方法(Session)
InternetAddress.parse(String 用逗号分隔（comma）如第二参数为true则可以用空格分隔) 返回一个InternetAddresss[]
MimeMessage的setRecipients(Message.RecipientType.TO,Address[]) Address的子类是InternetAddress
	     setRecipients(Message.RecipientType.TO,String address)
		setSubject("")
		setFrom(Address)
		setText("  ")



javax.mail.Transport是抽象类有一个static send(Message子类MimeMessage)

MimeBodyPart
可以通过Session对象的getTransport
java.util.ResourceBundle.getBundle("basename",Locale.SIMPLIFIED_CHINESE)


Javax.mail.internet.MimeMessage
	InternetAddress的.getType()返回在jar包下的javamail.default.address.map文件中的key  (值是协议)
	MerakMail软件
	
	  Properties  props = System.getProperties();
  props.put("mail.transport.protocol", "smtp");
  props.put("mail.store.protocol", "imap");
  props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
  props.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");
  props.put("mail.smtp.host", hostname);

  
  
示例
	static Properties prop=	new Properties();
	static{
		try {
			prop.load(new FileInputStream("C:/temp/mail.properties"));
		} catch ( Exception e) {
			e.printStackTrace();
		}  
	}
	public static final String username=prop.getProperty("username");  // 是@sina.com 前面的部分
	public static final String password=prop.getProperty("password"); 
	public static final String mailTo =prop.getProperty("mailTo");   // 带@的
	public static final String filterFromMailAddr=prop.getProperty("filterFrom");//xx@sina.com
	public static final String addrHost="163.com";
	public static final String pop3Host="pop3.163.com";
	public static final String smtpHost="smtp.163.com";
	public static final String imapHost="imap.163.com";
	
	public static void receivePop3Mail() throws Exception
	{
		Properties props = new Properties();
		Session recesession = Session.getInstance(props, null);
		recesession.setDebug(true);
		Store store = recesession.getStore("pop3");
		store.connect(pop3Host, username, password);
		
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);
		int count = inbox.getMessageCount();

	 
		// 设置过滤规则，对接收的e-mail进行过滤，
		SearchTerm st = new OrTerm(new SubjectTerm("笔记"), new FromStringTerm(filterFromMailAddr));//OrTerm 是或的原则
		Message[] filtermsg = inbox.search(st);
		// 对被过滤出的e-mail设置删除标志
		for (int i = 0; i < filtermsg.length; i++) 
		{
			Message msg = filtermsg[i];
			if (msg != null) 
			{
				// 得到被过滤出的e-mail的标题
				String filterTitle = msg.getSubject();
				System.out.println("被过滤的邮件:" + filterTitle);
				
				// 设置删除标记
				msg.setFlag(Flags.Flag.DELETED, true);//修改进入[已删除]列表,但不是在邮箱中是已删除???
			}
		}
		System.out.println("收件箱中总共有" + (count - filtermsg.length) + "封e-mail");
		 
		
		//列表显示出来
	    for(int j=1;j<=count;j++)
	    {
		      Message message=inbox.getMessage(j);
		      //如果不是待删除的e-mail就显示出来
		      if(message.isSet(Flags.Flag.DELETED))
		    	  continue;
		      
	         String title=message.getSubject();
	         System.out.println("---------邮件标题:"+title);
	         //------------邮件细节
	         Address[] address=message.getFrom();//javax.mail.internet.InternetAddress
	         if(address!=null)
	        	  for(int i=0;i<address.length;i++)
	            	 System.out.print("发件人:"+((InternetAddress)address[i]).getAddress());
	         
	         Date sentdate=message.getSentDate();
	         if(sentdate!=null)
	        	 System.out.print("发出的时间:"+sentdate.toString());
	         
	         address=message.getRecipients(Message.RecipientType.TO);
	         if(address!=null)
	            for(int i=0;i<address.length;i++)
	            	 System.out.print("收件人:"+address[i]);
	         
	         address=message.getRecipients(Message.RecipientType.CC);
	         if(address!=null)
	            for(int i=0;i<address.length;i++) 
	            	System.out.print("抄送人:"+address[i]);
	         
	        //如果是一个多部分内容的e-mail
	        if(message.isMimeType("multipart/*"))
	        {
	           //获得代表该e-mail的多部分内容的Multipart对象
	           Multipart multipart = (Multipart)message.getContent();
	           //依次获取Multipart对象的每个部分
	           for(int i = 0;i < multipart.getCount();i++)
	           {
	             //得到每个部分的属性
	             Part p = multipart.getBodyPart(i);
	             String disposition = p.getDisposition();
	             
	             //如果该部分中是附件内容，则输出该附件的下载链接
	             if ((disposition != null) &&(disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE)))
	             {
	                String filename=p.getFileName();
	                filename=javax.mail.internet.MimeUtility.decodeText(filename);//中文OK
	                System.out.println("符件:"+filename+",mime:"+p.getContentType());
	   	         
	                int num=message.getMessageNumber();
	                System.out.println("邮件索引:"+num);

	                InputStream input=p.getInputStream();//下载附件
	              }else if(disposition==null)
                 {
                    if(p.isMimeType("text/plain"))
                    {
                    	  System.out.print("只处理的文本:"+p.getContent());
                    }else   if(p.isMimeType("text/html"))
                    {   
                    	System.out.println("=====HTMLcontent:"+p.getContent());
                    }else//如type:multipart/alternative
                    {
                    	System.out.println("=====not text/plain or text/html ===type:"+p.getContentType()+"\n----content:"+p.getContent());
                    }
                 }
               }
	        }else if(message.isMimeType("text/plain"))  //如果是普通文本形式的e-mail，则显示其详细内容
            {
            	System.out.print("邮件文本:"+message.getContent());
            }else//几乎没很少有这种情况
            {
            	System.out.print("========other ContentType:"+message.getContentType());
            }
         } 
 		inbox.close(true);
 		store.close();
	}
	public static void receiveIMAPMail() throws MessagingException 
	{
		Properties props = System.getProperties();
		Session sess = Session.getInstance(props, null);
		sess.setDebug(true);
		Store st = sess.getStore("imap");//还可是 imaps
		st.connect(imapHost, username, password);
		
		Folder fol = st.getFolder("INBOX");
		if (fol.exists())
		{
			for (Folder f : fol.list()) 
			{
				System.out.printf("-----box:%s", f.getName());//只有一个INBOX
			}
			fol.open(Folder.READ_ONLY);
			Message[] msgs=fol.getMessages();
			for (Message m : msgs) 
			{
				System.out.printf("-----/n来自%s /n标题%s/n大小%d/n",convertAddress2String(m.getFrom()), m.getSubject(), m.getSize());
			}
			fol.close(false);
		} 
		st.close();
	}
	public static void sendHTMLAttachmentMail() throws MessagingException // TestOK
	{
		String subject = "subject from javamail 标题";
		String bodyText = "hello !,this is java mail test body 正文.";
		String attachment = "c:/temp/图片.jpg";
		boolean isSendAttach=true;

		
		Properties props = new Properties();
		Session sendsession = Session.getInstance(props, null);
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.auth", "true");// 设置SMTP服务器需要权限认证
		sendsession.setDebug(true);
		Message message = new MimeMessage(sendsession);
		message.addHeader("Content-type", "text/html");//对HTML格式的邮件
		
		message.setFrom(new InternetAddress(username + "@" + addrHost));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
		message.setSubject(subject);
		message.setSentDate(new Date());

		if (isSendAttach) 
		{
			// 建立第一部分：文本正文
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8><div style='color:red;font-size:50px'>" + bodyText+"</div>", "text/html;charset=UTF-8");//对HTML格式的邮件
			// 建立多个部分Multipart实例
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			
			
			// 建立第二部分：附件
			messageBodyPart = new MimeBodyPart();
			// 获得附件
			DataSource source = new FileDataSource(attachment);
			// 设置附件的数据处理器
			messageBodyPart.setDataHandler(new DataHandler(source));
			// 设置附件文件名
			
			String fileName=source.getName(); 
			try {
				fileName=MimeUtility.encodeText(fileName);//在收件箱中的中文OK
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			messageBodyPart.setFileName(fileName); 
			
			
			// 加入第二部分
			multipart.addBodyPart(messageBodyPart);
			// 将多部分内容放到e-mail中
			message.setContent(multipart);
		}  
		
		message.saveChanges();
		Transport transport = sendsession.getTransport("smtp");
		transport.connect(smtpHost, username, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

================================JMS  
<dependency>
    <groupId>javax.jms</groupId>
    <artifactId>jms</artifactId>
    <version>1.1</version>
</dependency>

SoupUI 可JMS


多台weblogic域 JMS通信 要域"密码"一样,这里的密码是在console界面中的第一项即"域名"->security标签->下方的Advanced->Credential:处的密码

Queue 和 Topic 都继承Destination
TopicSubscriber 和 QueueReceiver 继承自 MessageConsumer
QueueSender 和 TopicPublisher 继承自 MessageProducer

//weblogic JMS 通用部分
String url = "t3://localhost:7001";
String jndiConnectionFactory = "jms/myFactory";
String jndiQueue = "jms/myQueue";
String jndiTopic = "jms/myTopic";
boolean transacted = false;
Properties properties = new Properties();
properties.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
properties.put(Context.PROVIDER_URL,url);
Context context = new InitialContext(properties);
Object lookupFactory = context.lookup(jndiConnectionFactory);

//===MyQueueSender.java  OK
//---父类通用的
ConnectionFactory factory =(ConnectionFactory)lookupFactory;
Queue queue = (Queue)context.lookup(jndiQueue);
Connection connection =factory.createConnection();
connection.start();
Session session = connection.createSession(transacted,  Session.AUTO_ACKNOWLEDGE);
MessageProducer producer  = session.createProducer(queue);

TextMessage textMessage = session.createTextMessage();
textMessage.clearBody();
textMessage.setText("MessageProducer's  Message");
producer.send(textMessage);//OK,weblogic监视Messages Current列+1
if (transacted)
{
	session.commit();
}
producer.close();
session.close();
connection.close();
//--子类
QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) lookupFactory;
Queue queue = (Queue)context.lookup(jndiQueue);
QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
queueConnection.start();
QueueSession queueSession = queueConnection.createQueueSession(transacted, Session.AUTO_ACKNOWLEDGE);
QueueSender queueSender = queueSession.createSender(queue);
TextMessage textMessage = queueSession.createTextMessage();
textMessage.clearBody();
textMessage.setText("QueueSender's Message");
queueSender.send(textMessage);//OK ,weblogic监视Messages Current列+1
if (transacted)
{
	queueSession.commit();
}
queueSender.close();
queueSession.close();
queueConnection.close();

//===MyQueueReceiver.java OK
//---父类通用的
Object obj = context.lookup(jndiQueue);
Queue queue = (Queue) obj;
ConnectionFactory factory =(ConnectionFactory)lookupFactory;
Connection connection =factory.createConnection();
connection.start();
Session session = connection.createSession(transacted,  Session.AUTO_ACKNOWLEDGE);
MessageConsumer consumer  = session.createConsumer(queue);

//---
//Message tmpMsg=consumer.receiveNoWait();//OK
//System.out.println("MessageConsumer get is:"+ tmpMsg);
//---
consumer.setMessageListener(new MessageListener(){		
	public void onMessage(Message message) {
		if (message instanceof TextMessage)
		{
			TextMessage textMessage = (TextMessage) message;
			try
			{
				System.out.println("MessageListener get is:"+ textMessage.getText());
			}catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
	}});
MyQueueReceiver msgRcvr = new MyQueueReceiver();
synchronized(msgRcvr){ msgRcvr.wait(100000);}  
//------

if (transacted)
{
	session.commit();
}
consumer.close();
session.close();
connection.close();

//--子类
QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) lookupFactory;
Object obj = context.lookup(jndiQueue);
Queue queue = (Queue) obj;
QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
queueConnection.start();
QueueSession queueSession = queueConnection.createQueueSession(transacted,  Session.AUTO_ACKNOWLEDGE);
QueueReceiver queueReceiver = queueSession.createReceiver(queue);

QueueBrowser browser = queueSession.createBrowser(queue);//只看不取 OK
Enumeration msgs = browser.getEnumeration();
while (msgs.hasMoreElements()) 
{
 TextMessage msg = (TextMessage)msgs.nextElement();
System.out.println("QueueBrowser get is: " + msg.getText());
}
//--------
//			TextMessage textMessage=(TextMessage)queueReceiver.receive();//会阻塞 ,只读一个继续,可while,OK
//			System.out.println("QueueReceiver get is:"+ textMessage.getText());
//--------
 queueReceiver.setMessageListener(new MessageListener(){		//异步 OK, 会读所有的
	public void onMessage(Message message) {
		if (message instanceof TextMessage)
		{
			TextMessage textMessage = (TextMessage) message;
			try
			{
				System.out.println("MessageListener get is:"+ textMessage.getText());
			}catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
	}});
MyQueueReceiver msgRcvr = new MyQueueReceiver();
synchronized(msgRcvr){ msgRcvr.wait(100000);}  
//------
queueReceiver.close();     
queueSession.close();     
queueConnection.close();  


//==============MyTopicSubscriber.java  weblogic 有示例的 
//离线topic的要求一定要配置一个JMS store
//---parent  离线/在线 OK
ConnectionFactory connectionFactory = (ConnectionFactory) lookupFactory;
Topic topic = (Topic)context.lookup(jndiTopic);

Connection connection = connectionFactory.createConnection();
connection.setClientID("client-name-1"); 
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 
TopicSubscriber  consumer = session.createDurableSubscriber(topic, "my-sub-name-1"); 
connection.start();
Message msg=consumer.receive();
System.out.println("parent get is:"+msg);
//---child  离线/在线 OK
TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) lookupFactory;
Topic topic = (Topic)context.lookup(jndiTopic);
TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();

topicConnection.setClientID("client-name");
TopicSession topicSession = topicConnection.createTopicSession(transacted, Session.AUTO_ACKNOWLEDGE);
TopicSubscriber topicSubscriber=topicSession.createDurableSubscriber(topic, "my-sub-name"); //第二个参数是唯一标识这个TopicSubscriber (java 进程)的名字,对应于PERSISTENT的topic

//会在weblogic的Monitor->Durable Subscribers下建立的,离线也可取消息,之后connection.start();
//TopicSubscriber topicSubscriber= topicSession.createSubscriber(topic);//必须在线可取消息
topicConnection.start();
topicSubscriber.setMessageListener(new MessageListener() 
{
	public void onMessage(Message msg)
	{
		if(msg instanceof TextMessage)
		{
			TextMessage t=(TextMessage)msg;
			try
			{
				System.out.println("Topic get is:"+t.getText());
			} catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
	}});

MyTopicSubscriber my=new MyTopicSubscriber();
synchronized(my){my.wait(100000);}    
topicSubscriber.close();	 
topicSession.close();     
topicConnection.close();     
//==============MyTopicPublisher.java 
//---parent  离线/在线 OK
ConnectionFactory connectionFactory = (ConnectionFactory) lookupFactory;
Topic topic = (Topic)context.lookup(jndiTopic);
Connection connection = connectionFactory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 

MessageProducer producer = session.createProducer(topic); 
producer.setDeliveryMode(DeliveryMode.PERSISTENT); //设置保存消息 ,这个可以不写的，如先subscript是durable的，这里就放到durable里
connection.start(); //设置完了后，才连接  

TextMessage msg=session.createTextMessage();
msg.clearBody();
msg.setText("Test Message!!!");
producer.send(msg); 
//---child  离线/在线 OK
TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) lookupFactory;
Topic topic = (Topic)context.lookup(jndiTopic);
TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
TopicSession topicSession = topicConnection.createTopicSession(transacted, Session.AUTO_ACKNOWLEDGE);
TopicPublisher topicPublisher= topicSession.createPublisher(topic);
topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);//topic可持久化,Producer级的,发的消息都是可持 久化
topicConnection.start();

TextMessage textMessage=topicSession.createTextMessage();
textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);//消息级的 持久化
textMessage.setText("topicPublisher's Message");
topicPublisher.publish(textMessage);

topicPublisher.close();
topicSession.close();     
topicConnection.close();     

如果有webloigc 中有	Durable的Topic,那么只有Durable的Subscriber可以收到，

=============================JTA
Context ctx=new InitialContext();
UserTransaction ut=(UserTransaction)ctx.looup("javax.transaction.UserTransaction");
ut.begin();
...
ut.commit();

@Inject
UserTransaction userTransaction;

在类前加
@Transactional(value = Transactional.TxType.MANDATORY) //表示调用类的方法必须在 ut.begin();以后
@Transactional(value = Transactional.TxType.NEVER)//表示调用类的方法不能在 ut.begin();以后
@Transactional(value = Transactional.TxType.NOT_SUPPORTED)//有没有 ut.begin();都可
@Transactional(value = Transactional.TxType.REQUIRED)//有没有 ut.begin();都可
@Transactional(value = Transactional.TxType.REQUIRES_NEW)//有没有 ut.begin();都可,会自动建立事务 @Inject UserTransaction userTransaction;
@Transactional(value = Transactional.TxType.SUPPORTS)//有没有 ut.begin();都可

@javax.transaction.TransactionScoped  //类前,表示类的方法要在事务中被调用,同一事务中类的所有对象都相同,不同事务类的对象不同

@javax.annotation.PreDestroy 放在方法前

--weblogic中使用JTA,要在Oracle数据库服务器上启用 XA 
@$ORACLE_HOME/rdbms/admin/xaview.sql   (以sys用户执行)
grant select on v$xatrans$ to public (or <user>);
grant select on pending_trans$ to public;
grant select on dba_2pc_pending to public;
grant select on dba_pending_transactions to public;
grant execute on dbms_system to <user>;


import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public static XADataSource newOracleXADataSource() throws Exception//OK
{
	OracleXADataSource oracleXADS=new OracleXADataSource();//oracle.jdbc.xa.client.
	oracleXADS.setURL("jdbc:oracle:thin:@127.0.0.1:1521/XE");
	oracleXADS.setUser("hr");
	oracleXADS.setPassword("hr");
	oracleXADS.setPortNumber(1521);
	oracleXADS.setDatabaseName("XE");
	oracleXADS.setServerName("127.0.0.1");
	return oracleXADS;
}
	
public static XADataSource newH2XADataSource()//OK
{
	JdbcDataSource h2XADS=new JdbcDataSource();//org.h2.jdbcx.
	h2XADS.setURL("jdbc:h2:tcp://localhost:9092/test");
	h2XADS.setUser("sa");
	h2XADS.setPassword("sa");
	return h2XADS;
}
	
	
public XADataSource newMySQLXADataSource()
{
	MysqlXADataSource mysqlXADS=new MysqlXADataSource(); //com.mysql.jdbc.jdbc2.optional.
	mysqlXADS.setUser("root");
	mysqlXADS.setPassword("root");
	String url="jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8";
	mysqlXADS.setUrl(url);
	mysqlXADS.setURL(url);
	mysqlXADS.setPort(3306);
	mysqlXADS.setPortNumber(port);
	mysqlXADS.setServerName("127.0.0.1");
	mysqlXADS.setDatabaseName("test");
	return mysqlXADS;
}
public void executeInJndiConnection(Connection conn) throws SQLException
{
   PreparedStatement pstmt = conn.prepareStatement("update oracle_score set score=score-1 where id=1"); 
   //PreparedStatement pstmt = conn.prepareStatement("insert into oracle_score(id,score)values(1,88)");//使用主键重复的错误测试
	pstmt.executeUpdate();//如有错误,这里就抛出,无论是否设置setAutoCommit(false)
	pstmt.close();
}
public void executeInNewConnection(Connection conn ) throws SQLException
{
	PreparedStatement pstmt = conn.prepareStatement("update mysql_score set score=score+1 where id=1"); 
	pstmt.executeUpdate();
	pstmt.close();
}
 
//.class要在weblogic的容器内才行
TransactionManager trans = null;
Connection jndiConn = null;
Connection newConn = null;
try {
	Properties properties = new Properties();
	properties.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
	properties.put(Context.PROVIDER_URL, "t3://127.0.0.1:7001");
	Context context = new InitialContext(properties);
	
	Object jndiObj= context.lookup("jdbc/oracleXA"); 
	//weblogic配置时一定要选择XA JDBC driver,driver自动配置为oracle.jdbc.xa.client.OracleXADataSource
	DataSource jndiDataSource = (DataSource)jndiObj;//不可强转到XADataSource
	jndiConn = jndiDataSource.getConnection();
	//jndiConn.setAutoCommit(false); //可以不加的
	trans = (TransactionManager)context.lookup("javax.transaction.UserTransaction");//返回的也是UserTransaction,
	trans.begin();  
	
	XADataSource xaDataSource = newMySQLXADataSource();//这个数据源是通过JDBC配置的
	XAConnection xaConn = xaDataSource.getXAConnection();
	XAResource xaRes = xaConn.getXAResource();   
	trans.getTransaction().enlistResource(xaRes);//需要主动的加入到当前的事务中      
	//如.class不在weblogic中则报错You may enlist a resource only on a server.
	//就是说weblogic的JTA TransactionManager只能在代码(war或者ear)发布到weblogic的server上才能使用.
	newConn = xaConn.getConnection();
	//newConn.setAutoCommit(false);//可以不加的
	executeInNewConnection(newConn); 
	executeInJndiConnection(jndiConn);//通过在weblogic上配置的DataSource,会自动加入到当前的事务中. 
	trans.commit();
	System.out.println("OK!transaction manager commited!");
} catch (Exception e)
{    
	 e.printStackTrace();
	try {
		trans.rollback();
		System.out.println("Exception!rollback transactions managed by traMgr.");
	} catch (Exception e1) {
		e1.printStackTrace();
	} 
}finally{
	try {
		jndiConn.close();
		newConn.close();
	} catch (SQLException e) {e.printStackTrace();}     
}



//-----.class不在容器中,但只一个连接????
//是用“两步提交协议”来提交一个事务分支：
 public void  test()throws Exception
{
	XADataSource xaDS;
	XAConnection xaCon;
	XAResource xaRes;
	Xid xid;
	Connection con;
	Statement stmt;
	int ret;
	xaDS = newOracleXADataSource();
	xaCon = xaDS.getXAConnection();
	xaRes = xaCon.getXAResource();
	con = xaCon.getConnection();
	stmt = con.createStatement();
	xid = new MyXid(100, new byte[]{0x01}, new byte[]{0x02});//实现Xid接口的类, 类用来标识事务
	try 
	{
		xaRes.start(xid, XAResource.TMNOFLAGS);
		stmt.executeUpdate("insert into  h2_score(id,score) values(1,70)");
		xaRes.end(xid, XAResource.TMSUCCESS);
		ret = xaRes.prepare(xid);
		if (ret == XAResource.XA_OK) 
			xaRes.commit(xid, false);
	}
	catch (XAException e) 
	{
		e.printStackTrace();
	}
	finally
	{
		stmt.close();
		con.close();
		xaCon.close();
	}
}




=============================Portlet
portlet 1 规范JSR-168
portlet 2 规范JSR-286

---web.xml

<filter>
	<filter-name>servletFilter</filter-name>
	<filter-class>pluto_portlet.ServletFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>servletFilter</filter-name>
	<url-pattern>/portal/*</url-pattern>  */<!-- 路径只能是/portal/ 开头 -->
</filter-mapping>


为 pluto
<servlet>
	<servlet-name>changeCaseServ</servlet-name>
	<servlet-class>org.apache.pluto.container.driver.PortletServlet</servlet-class>
	<init-param>
		<param-name>portlet-name</param-name>
		<param-value>ChangeCasePortlet</param-value> <!-- 这里除了是portlet.xml中的值,还要与  <url-pattern>/PlutoInvoker/的值一样才行 -->
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>changeCaseServ</servlet-name>
	<url-pattern>/PlutoInvoker/ChangeCasePortlet</url-pattern><!-- 路径只能是/PlutoInvoker/ 开头 -->
</servlet-mapping>



---WEB-INF/portlet.xml
<?xml version="1.0" encoding="UTF-8"?>
<portlet-app
    xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
    version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd
                        http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd">
	<portlet>
      <portlet-name>ChangeCasePortlet</portlet-name>
      <display-name>Change Case Portlet</display-name>
      <portlet-class>pluto_portlet.ChangeCasePortlet</portlet-class>
      <supports>
         <mime-type>text/html</mime-type>
         <portlet-mode>view</portlet-mode>
      </supports>
	  <resource-bundle>pluto_portlet.changeCase-message</resource-bundle> <!-- 每个portlet单独文件国际化,<title>中的值 -->
      <portlet-info>
         <title>ChangeCasePortlet</title>
      </portlet-info>
	  <portlet-preferences>
			<preference>
				<name>my-pref-name</name>
				<value>my-pref-value</value>
			</preference>
			<preference>       
				<name>company</name>
				<value>abc_firm</value>
			</preference> 
		</portlet-preferences>
   </portlet>

	<filter>
		<filter-name>MyAllFilter</filter-name>
		<filter-class>pluto_portlet.filter.MyAllFilter</filter-class>
		<lifecycle>ACTION_PHASE</lifecycle>
		<lifecycle>RENDER_PHASE</lifecycle>
		<lifecycle>EVENT_PHASE</lifecycle>
		<lifecycle>RESOURCE_PHASE</lifecycle>
	</filter>
	<filter-mapping>    
		<filter-name>MyAllFilter</filter-name>
		<portlet-name>my*</portlet-name>
	</filter-mapping>
   
   	<!-- 
   <default-namespace>http://company.com</default-namespace> 可有可无
    -->
classpath下的pluto_portlet/changeCase-message_en_US.properties,但在pluto中,中文乱码????
javax.portlet.title=my javax changeCase portlet title
javax.portlet.title.<display-name>=  优先级高
<portlet-info><title>做国际化key = 优先级低


动作 Action 阶段和呈现Render阶段.
在单个请求的整个处理过程中,动作阶段只会被执行一次,而显示阶段可能会被执行多次

GenericPortlet实现了Portlet,PortletConfig,EventPortlet,ResourceServingPortlet

MyPortlet implements Portlet 

import javax.portlet.PortletPreferences;
MyGenericPortlet extends GenericPortlet
{	//视图模式时调用 doView() 方法
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException{
		//-----------
		PortletSession  session=  request.getPortletSession();
		session.setAttribute("portletSessionScopeTest", "portletSessionScopeTestValue" );
		PortletContext context_1= session.getPortletContext();
		//-----------
		PortletContext context = getPortletConfig().getPortletContext();
		context.getRequestDispatcher(VIEW_JSP).include(request, response);
	}
		//点提交时调用 processAction() 方法
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
			throws PortletException, java.io.IOException {
		//-------
		PortletPreferences prefs = actionRequest.getPreferences();
		prefs.setValue("greeting", "hello in javacode");
		prefs.store();//store is not allowed during RENDER phase.
		String  greet=(String)prefs.getValue("greeting", "Hello! this is default value");
		String my_pref_name = prefs.getValue("my-pref-name", null);//配置在portlet.xml
		Map<String, String[]> mapData=prefs.getMap(); //是只从portlet.xml中取的结果
		
		System.out.println("greet="+greet);
		System.out.println("my_pref_name="+my_pref_name);
		System.out.println("mapData="+mapData);
		//-------
		
	    String newCase = actionRequest.getParameter("case");//得到参数
		
		actionResponse.setRenderParameter("textBox", textBox);//设置传参数
	}
}	


<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>表示可以在JSP中使用 
renderRequest
resourceRequest
actionRequest
eventRequest

renderResponse
resourceResponse
actionResponse
eventResponse

portletConfig
portletSession
portletSessionScope			${portletSessionScope.xx}
portletPreferences
portletPreferencesValues  是 Map<String, String[]> 类型 , 同 portletPreferences.getMap()
 -->

<br/>
EL portletPreferences 1 = ${portletPreferences.getValue("greeting","default value")}  <br/>
EL portletPreferences 2 = ${portletPreferences.map["company"][0]} <br/>
EL portletPreferences 3 = ${portletPreferencesValues["company"][0]} <br/>
 
portletSessionScope=<c:out value="${portletSessionScope.portletSessionScopeTest}"/><br /> 
portletSession=<%=portletSession.getAttribute("portletSessionScopeTest")%> <br/>
 
<%
	String textBox = renderRequest.getParameter("textBox");
%>
<FORM  action="<portlet:actionURL/>"> 表示调用GenericPortlet的processAction方法

ResourceServingPortlet 不建议直接仿问资源,如href="/portlet/myResource.jsp",而没有Portlet的功能,<portlet:resourceURL/>触发

<a href="<portlet:resourceURL/>">Click me to request Resource URL</a><!-- 触发 ResourceServingPortlet 的 serveResource 方法 -->

PortletContext contex=portletConfig.getPortletContext();
portletConfig.getPortletName();//<portlet-name>的值
PortletRequestDispatcher portletRequestDispatcher = contex.getRequestDispatcher("/portlet/eventSender.jsp");
portletRequestDispatcher.include(renderRequest, renderResponse);
String myParam=portletConfig.getInitParameter("myParam");
<portlet>中
	<init-param>
	  <name>myParam</name>
	  <value>myValue in init-param</value>
	</init-param>

事件
发送方 actionResponse.setEvent("myComplexEvent",myEvent );//OK
	   actionResponse.setEvent(new QName("http://zh.org","myComplexEvent","my"),myEvent );//OK
接收方 EventReceiverPortlet  implements Portlet,EventPortlet 有方法 processEvent
		Event event = eventRequest.getEvent();//接收并处理事件
发送方<portlet>中
	<supported-publishing-event>
		<qname xmlns:my="http://zh.org">my:myComplexEvent</qname>
		<!--<name>myComplexEvent</name>-->
	</supported-publishing-event>
接收方<portlet>中
	<supported-processing-event>
 		<qname xmlns:my="http://zh.org">my:myComplexEvent</qname>
		<!--<name>myComplexEvent</name>-->
	</supported-processing-event>
根下
	<event-definition>   
		<qname xmlns:my="http://zh.org">my:myComplexEvent</qname>
		<!--<name>myComplexEvent</name>-->
		<value-type>pluto_portlet.MyComplexEvent</value-type>
	</event-definition>
	
公共参数
String mapCity=actionRequest.getParameter("mapCity");//如是中文是经过&#99999;编码的,中文OK
actionResponse.setRenderParameter("mapCity", mapCity+"_One");//是Render

<portlet>中
	<supported-public-render-parameter>mapCity</supported-public-render-parameter>
根下
<public-render-parameter>
	<identifier>mapCity</identifier><!-- 参数名 -->
</public-render-parameter>



首先进行 Servlet 过滤，其次是 Portlet 过滤

四种类型的过滤器
EventFilter 	拦截 processEvent 方法
ActionFilter 	拦截 processAction 方法
RenderFilter	拦截 render 方法
ResourceFilter 	拦截 serveResource 方法

String id=actionRequest.getWindowID();
//在 Portal 容器中布局同一个 Portlet 多次的情况下，windowID 可以用来区分同一个 Portlet 的不同窗口

actionResponse.setPortletMode(PortletMode.VIEW);//转换视图调用对应的GenericPortlet 的 doEdit,doView 方法

<a href='<portlet:renderURL portletMode="view" windowState="normal"/>' > -Home-</a>
windowState 还有 MAXIMIZED 和 MINIMIZED

<supports>
	<mime-type>text/html</mime-type>
	<portlet-mode>view</portlet-mode>
	<portlet-mode>edit</portlet-mode>
</supports>

-----<portlet:namespace/> ???

-----文件上传
portlet 只要使用 org.apache.commons.fileupload.portlet.PortletFileUpload; 其它的不变
pluot中文问题??? &#19981

-----文件下载
public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException, IOException {
		//----下载
		//ResourceURL downloadURL =resourceResponse.createResourceURL();
		byte[] content="this is a download test__这是一个下载测试".getBytes("UTF-8")
		resourceResponse.reset();
		resourceResponse.setContentType("application/x-msdownload");
		resourceResponse.setContentLength(content.length);
		//文件名如何设置 ????
		resourceResponse.getPortletOutputStream().write(content);
//		PrintWriter writer =resourceResponse.getWriter();
//		PortletRequestDispatcher portletRequestDispatcher=resourceRequest.getPortletSession().getPortletContext().getRequestDispatcher(HELP_JSP);
//	    portletRequestDispatcher.include(resourceRequest, resourceResponse);//也是.getWriter()不能和getPortletOutputStream()一起使用
	
	}
<a href="<portlet:resourceURL/>">download a file </a>
resourceResponse.setContentType("application/x-msdownload");//OK

Servlet 中使用 response.setContentType("text/xml; charset=UTF-8");

-----问题
pluto jsp页面中不能中文????

my.setMyname("中国");//Event Sender到Receiver后中文乱码???
actionResponse.setEvent(new QName("http://zh.org","myComplexEvent"),my );//<qname>无效????
<supported-publishing-event>
	<!-- <qname>无效????
	<qname xmlns:key="http://zh.org">myComplexEvent</qname>
	 -->
</supported-publishing-event>



=============================Batch

JavaEE带的示例

.war\WEB-INF\classes\META-INF\batch-jobs\PayrollJob.xml

<job id="payroll" xmlns="http://xmlns.jcp.org/xml/ns/javaee" version="1.0">
    <step id="process">
        <chunk item-count="3">
            <reader ref="SimpleItemReader"></reader>   <!-- 对应 @Named("SimpleItemReader"), 类要 extends AbstractItemReader  从哪读要做的记录 -->
            <processor ref="SimpleItemProcessor"></processor> <!-- implements ItemProcessor  处理读到的记录 -->
            <writer ref="SimpleItemWriter"></writer> <!-- extends AbstractItemWriter  存储处理结果-->
        </chunk>
		<partition>
            <mapper ref="PayrollPartitionMapper"/> <!-- implements PartitionMapper  -->
        </partition>
    </step>
</job>

@Inject
private JobContext jobContext; //EJB 容器实现

Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());

JobOperator jobOperator = BatchRuntime.getJobOperator();
long executionID = jobOperator.start("PayrollJob", props); //每一个名字是xml文件名,提交一个Job,AbstractItemReader 就被调用了

for (JobInstance jobInstance : jobOperator.getJobInstances("payroll", 0, Integer.MAX_VALUE-1))//取全部, "payroll" 对应xml文件中的 <job id="payroll"
{
	for (JobExecution jobExecution : jobOperator.getJobExecutions(jobInstance)) 
	{
	    jobExecution.getJobName()// payroll
		jobExecution.getExecutionId()
		jobExecution.getBatchStatus()//emnu 的类型 BatchStatus.COMPLETED
		jobExecution.getExitStatus()
		jobExecution.getStartTime()
		jobExecution.getEndTime()
	}
}

 return new PartitionPlanImpl()
        {
            @Override
            public int getPartitions() {
                return 5;
            }
            @Override
            public Properties[] getPartitionProperties() {
			}
		}
==================webSocket
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/myMsgWebSocket",
		decoders = {MyMessageDecoder.class}, //implements Decoder.Text<T> 请求时如@OnMessage方法的第一个参数是自定义类型,把String->T
		encoders = { MyMessageEncoder.class  }) //implements Encoder.Text<T>   T->String
public class MyMessageWebSocket { 
   private static Set<Session> peers = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());
    @OnOpen
    public void onOpen(Session session) {
        peers.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        peers.remove(session);
    }
    @OnMessage
    public void shapeCreated(MyMessage message, Session client) //如第一个参数是自定义类型会使用decoders和encoders,如String就不用
				throws IOException, EncodeException 
	{
		Set<Session> opendSessions=client.getOpenSessions();//所有打开的
        for (Session otherSession : peers) 
		{
            if ( otherSession.equals(client)) 
			{
                //otherSession.getBasicRemote().sendText(message);//发String消息到客户端
            	otherSession.getBasicRemote().sendObject(message);//要Encoder ,T->String
				client.getUserProperties().put("key",obj);//是个Map
				Object stored=client.getUserProperties().get("key");
            }
        }
    }
}

//编程式部署，放入其它的启动初始化方法中
ServerEndpointConfig.Builder.create(MyServerEchoEndpoint.class, "/echo").build();

---websocket java 做 client
public class MyClientEndpoint extends Endpoint  
{
	@Override
	public void onOpen(final Session session, EndpointConfig config) 
	{
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(String message) {
				System.out.println("客户端收到消息:"+message);	
			}
		}); 
	}
}

//代码最好也运行在容器中
WebSocketContainer container = ContainerProvider.getWebSocketContainer();//实现 META-INF/services/javax.websocket.ContainerProvider 
Session session=container.connectToServer(MyClientEndpoint.class,   //收到服务端消息的回调类
		ClientEndpointConfig.Builder.create().build(),
		new URI("ws://localhost:8080/J_JavaEE/myMsgWebSocket"));
session.getBasicRemote().sendText("obj_type123:data_123客户端发送的Test消息");
//session.getBasicRemote().sendObject(new MyMessage("obj_type","123_data"));//报错 因使用的 Decoder.Text 
Thread.sleep(3*1000);
session.close();
	
=============================Concurrent
@Resource
ManagedExecutorService executor;

@Resource
ManagedScheduledExecutorService scheduledExecutor;

@Resource
ManagedThreadFactory factory;

//--
InitialContext ctx = new InitialContext();
ManagedExecutorService executor = (ManagedExecutorService) ctx.lookup("java:comp/DefaultManagedExecutorService");

===javax.ws.rs.  CXF支持
=============================JSON
<dependency>
    <groupId>javax.json</groupId>
    <artifactId>javax.json-api</artifactId>
    <version>1.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.1</version>
</dependency>

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonStructure;
//--生成
JsonBuilderFactory bf = Json.createBuilderFactory(null);
JsonStructure struct =bf.createObjectBuilder()
            .add("firstName", "John")
            .add("age", 25)
            .add("address", bf.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
			).build();
			
JsonArray array=bf.createArrayBuilder()
	.add(bf.createObjectBuilder()
		.add("type", "home")
		.add("number", "212 555-1234"))
	.add(bf.createObjectBuilder()
		.add("type", "fax")
		.add("number", "646 555-4567"))
	.build();
System.out.println(struct.toString());  //{...}
System.out.println(array.toString());//[{...},{...}]
//--解析
String inStr="[{\"type\":\"home\",\"number\":\"212 555-1234\"},{\"type\":\"fax\",\"number\":\"646 555-4567\"}]";
JsonParser parser = Json.createParser(new StringReader(inStr));
while(parser.hasNext()) 
{
	Event e = parser.next();
	if (e == Event.KEY_NAME)
	{
		if (parser.getString().equals("number")) 
		{
			parser.next();
			System.out.println(parser.getString());
		}  
	}
}


=============================Interceptor
/WEB-INF/beans.xml 

<!-- 是 JavaEE 的 interceptors 用途 -->
<beans xmlns="http://java.sun.com/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/beans_1_0.xsd">
    <interceptors>
     <class>javaee_intercepter.LoggingInterceptorDetail</class>
    </interceptors>           
</beans>

import javax.inject.Inject;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface LoggingInterceptor {
}

@LoggingInterceptor
@Interceptor
public class LoggingInterceptorDetail {
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        System.out.println("BEFORE: " + context.getParameters());
        Object result = context.proceed();
        System.out.println("AFTER: " + context.getMethod());
        return result;
    }
}

@LoggingInterceptor
public class ShoppingCart {
	 public void addItem(String item) {
        items.add(item);
    }
	public void checkout() {
        System.out.println(items + " checking out");
    }
}

@Inject ShoppingCart cart;
cart.checkout(); //会被拦截
cart.addItem(item);


=============================Event    glassfish中的weld-osgi-bundle.jar
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.event.Event;
//注册事件的观察
public void onPrintAndBind(@Observes @BindIt PrintEvent event) {// @BindIt (有@Qualifier)和 PrintEvent 是自定义的
	System.out.println("Printing and binding " + event.getPages() + " pages");
}
public void onPrint(@Observes @Default PrintEvent event) {
	System.out.println("Printing " + event.getPages() + " pages");
}
//--产生事件
@Inject Event<PrintEvent> printEvent;
@Inject @BindIt Event<PrintEvent> printAndBindEvent;    
public void print(int pages) 
{
	PrintEvent event = new PrintEvent(pages);
	if (pages < 10)
		printEvent.fire(event);//产生
	else
		printAndBindEvent.fire(event);
}


=============================JCA (J2EE Connector Architecture)
SPI (Service Provider Interfaces) 
CCI (Common Client Interface)
Enterprise Information Systems (EIS)

Resource Adapter Archive (RAR)文件中(JAR 文件格式)

Resource Adapter 像 JDBC driver 一般是供应商提供 ,EIS像是DBMS

---ra.xml 老版本,新版本使用@
xx.rar/META-INF/ra.xml 
xx.rar/yy.jar中放 .class
	
<!DOCTYPE connector PUBLIC '-//Sun Microsystems, Inc.//DTD Connector 1.0//EN' 'http://java.sun.com/dtd/connector_1_0.dtd'>
<connector>
    <display-name>Hello World Sample</display-name>
    <vendor-name>Willy Farrell</vendor-name>
    <spec-version>1.0</spec-version>
    <eis-type>Hello World</eis-type>
    <version>1.0</version>
    <resourceadapter>
        <managedconnectionfactory-class>com.ibm.ssya.helloworldra.HelloWorldManagedConnectionFactoryImpl</managedconnectionfactory-class>
        <connectionfactory-interface>javax.resource.cci.ConnectionFactory</connectionfactory-interface>
        <connectionfactory-impl-class>com.ibm.ssya.helloworldra.HelloWorldConnectionFactoryImpl</connectionfactory-impl-class>
        <connection-interface>javax.resource.cci.Connection</connection-interface>
        <connection-impl-class>com.ibm.ssya.helloworldra.HelloWorldConnectionImpl</connection-impl-class>
        <transaction-support>NoTransaction</transaction-support>
        <reauthentication-support>false</reauthentication-support>
    </resourceadapter>
</connector>

import javax.security.auth.Subject;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.message.callback.PasswordValidationCallback;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.resource.cci.ConnectionSpec;

import javax.resource.spi.work.SecurityContext;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.Activation;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ActivationSpec;

@Connector(description = "Sample adapter using the JavaMail API", 
  displayName = "InboundResourceAdapter", 
  vendorName = "Sun Microsystems, Inc.", 
  eisType = "MAIL", 
  version = "1.0", 
  authMechanisms = { 
        @AuthenticationMechanism(authMechanism = "BasicPassword", 
          credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential) 
  }
  implements ResourceAdapter,Serializable
 {
	public void start(BootstrapContext ctx) //服务器启动时,或部署时调用 
	{	
		workManager = ctx.getWorkManager();
		workManager.scheduleWork(new Work()
			{
				 public void run() {
				 	endpoint = endpointFactory.createEndpoint(null);
					(samples.connectors.mailconnector.api.JavaMailMessageListener) endpoint).xx();
				 }
			}); 
	}
	public void endpointActivation(MessageEndpointFactory endpointFactory,ActivationSpec spec)//客户端激活,传入@Activation对应的类
	{//服务器启动时第二次调用
	}
 }
 
 
@Activation(messageListeners = {JavaMailMessageListener.class})
public class ActivationSpecImpl implements  ActivationSpec,Serializable  //ResourceAdapter的 endpointActivation 传入的
{
	@ConfigProperty()//可被外部EJB配置 @ActivationConfigProperty 修改,同一个接口 JavaMailMessageListener
    private String serverName = "";//不能为null
}

extends SecurityContext 
{
	public abstract void setupSecurityContext(CallbackHandler handler, Subject executionSubject,Subject serviceSubject)
	{
		executionSubject.getPrincipals().add(new PrincipalImpl(principalName));
		CallerPrincipalCallback cpc = new CallerPrincipalCallback(executionSubject, new PrincipalImpl(principalName));
		PasswordValidationCallback pvc =  new PasswordValidationCallback(executionSubject, userName, password.toCharArray());
		Callback callbackArray[] = new Callback[2];
		callbackArray[0]=cpc;
		callbackArray[1]=pvc;
		handler.handle(callbackArray);
		if(!pvc.getResult()){
			System.out.println("用户名验证失败");
		}
	}
}
class PrincipalImpl implements Principal
{
	 public String getName() {
        return name;
    }
}
implements WorkContextProvider
{
	 public List<WorkContext> getWorkContexts() {
        return workContexts;
    }
	getWorkContexts().add(SecurityContext x);
}

 implements ConnectionManager
 { 
	public Object allocateConnection(ManagedConnectionFactory mcf,ConnectionRequestInfo cxRequestInfo)//XX implements ConnectionRequestInfo
	{
		ManagedConnection mc = mcf.createManagedConnection(null, cxRequestInfo);
		return mc.getConnection(null, cxRequestInfo);
	}
}

PasswordCredential pc = new PasswordCredential(userName, password);
pc.setManagedConnectionFactory(mcf);

 PasswordCredential pc =  AccessController.doPrivileged(new PrivilegedAction<PasswordCredential>()
					{
                        public PasswordCredential run() 
						{
                            Set<PasswordCredential> creds = subject.getPrivateCredentials(PasswordCredential.class);
                            Iterator<PasswordCredential> iter = creds.iterator();
                            while (iter.hasNext()) 
							{
                                PasswordCredential temp = iter.next();
                                if (temp != null && temp.getManagedConnectionFactory() != null
												 && temp.getManagedConnectionFactory().equals(mcf)) {
                                    return temp;
                                }
                            }
                            return null;
                        }
                    });
					
import javax.mail.MailSessionDefinition;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.Referenceable;

@MailSessionDefinition(
        name = "java:app/env/TheMailSession",
        storeProtocol = "IMAP", 
        transportProtocol = "SMTP", 
        host = "localhost", 
        user = "joe", 
        password = "joe", 
        from = "daphu", 
        properties = {
                "mail.imap.class=com.sun.mail.imap.IMAPStore",
                "mail.smtp.class=com.sun.mail.smtp.SMTPTransport" 
        }
)
@ConnectionFactoryDefinition(name = "java:comp/env/eis/MyConnectionFactory", 
  description = "Connection factory against mail server", 
  interfaceName = "samples.connectors.mailconnector.api.JavaMailConnectionFactory",//实现类定义在 @ConnectionDefinition
  resourceAdapter = "#mailconnector", 
  minPoolSize = 2, 
  transactionSupport = TransactionSupportLevel.NoTransaction)
@WebServlet
class xx extends HttpServlet {
	@Resource(lookup="java:comp/env/eis/MyConnectionFactory")
	JavaMailConnectionFactory  connectionFactory ;
	
	@Resource(lookup="java:app/env/TheMailSession")
    Session session;
	
}

@ConnectionDefinition(connectionFactory = samples.connectors.mailconnector.api.JavaMailConnectionFactory.class, 
					connectionFactoryImpl = samples.connectors.mailconnector.ra.outbound.JavaMailConnectionFactoryImpl.class, 
					connection = samples.connectors.mailconnector.api.JavaMailConnection.class, 
					connectionImpl = samples.connectors.mailconnector.ra.outbound.JavaMailConnectionImpl.class)
					
				JavaMailConnectionFactoryImpl implements  Referenceable 
					
=============================Tomcat
catalina.bat jdpa start 启动8000 可远程调试

catalina.bat run 或 catalina.bat start
startup.bat

 
tomcat 8  tomcat-users.xml 中加配置
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<role rolename="manager-jmx"/>
	<role rolename="manager-status"/>
	<user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx,manager-status"/>
 
默认的安全只能使用 127.0.0.1 或者 localhost 如是本机IP就不行 cross-site request forgery (CSRF),
到tomcat8/conf/Catalina/localhost/目录下,打开manager.xml文件（没有就新建）添加下面内容  
<Context privileged="true" antiResourceLocking="false"   
         docBase="${catalina.home}/webapps/manager">  
             <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="^.*$" />  
</Context> 


web.xml修改也会被reload

<Context privileged="true" antiResourceLocking="false"
         docBase="${catalina.home}/webapps/manager">
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.0\.0\.1" />
</Context>



server.xml中
	<Connector port="8080"  protocol="HTTP/1.1" maxThreads="150" 
	<Host name="localhost"  appBase="webapps" 这个webapps可以改成其它路径

--------Tomcat/conf/web.xml中加修改listings 为true　就可以列目录了(默认是不可以的)
<init-param>
	<param-name>listings</param-name>
	<param-value>true</param-value>
</init-param>
--------	


在tomcat-7\conf\Catalina\localhost\建立 SpringPortlet.xml文件,内容
<Context  docBase="D:/program/eclipse_java_workspace/J_SpringPortlet/WebContent"
		 path="/SpringPortlet" reloadable="true"  crossContext="true">
	<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4" />
</Context>


META-INF目录下加context.xml文件
<Context crossContext="true" /> 
<!--为Tomcat使用,eclipse生成的conf/server.xml  <Context 中会加上crossContext="true"  -->

部署方法 
$CATALINA_BASE/conf/[enginename]/[hostname]/context.xml		named [webappname].xml 
$CATALINA_BASE/webapps/[webappname]/META-INF/context.xml
[enginename]是Catalina,[hostname]是localhost
 
 

---------配置DataSource ,jdbc.jar放在tomcat的lib目录下
方法一,要2步
	1) 在Tomcat 根目录下的conf\server.xml 配置Resource：
	<GlobalNamingResources>
		<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		description="DB Connection"  auth="Container"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4"/>
	</GlobalNamingResources>

	2) 在Tomcat 根目录下的conf\context.xml 配置:
	<ResourceLink name="myjdbc" global="jdbc/mydatasource" type="javax.sql.DataSourcer" />

	ctx.lookup("java:comp/env/myjdbc");
方法二
	整合在conf\context.xml 中配置:
	<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
	driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
	maxIdle="2" maxWait="5000" maxActive="4" />

	ctx.lookup("java:comp/env/jdbc/mydatasource");

server.xml 中 在文件未尾的</Host>之前加
	<Context docBase="D:/program/eclipse_java_workspace/J_SpringPortlet/WebContent"  
			 path="/J_SpringPortlet" reloadable="true"  crossContext="true">
		<!-- 
		<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4" />
		-->
	</Context>


web.xml中
<resource-ref>
 <res-ref-name>jdbc/Modeling</res-ref-name>
 <res-type>javax.sql.DataSource</res-type>
 <res-auth>Container</res-auth>
</resource-ref>  

Context initCtx = new InitialContext();

Context envCtx = (Context) initCtx.lookup("java:comp/env");
DataSource dataSource = (DataSource) envCtx.lookup("jdbc/Modeling");
con = dataSource.getConnection();
//--------
//DataSource dataSource = (DataSource) initCtx.lookup("java:comp/env/jdbc/Modeling");
//con = dataSource.getConnection();
 
 
---------启用Tomcat 的HTTPS协议
keytool -genkey -alias tomcat -keyalg RSA -keystore C:/temp/.keystore
提示输入密码,可以使用Tomcat的默认值changeit,一些其它的信息也要写
修改tomcat目录下的server.xml文件，去掉以下注释
<!--
<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
		   maxThreads="150" scheme="https" secure="true"
		   clientAuth="false" sslProtocol="TLS" />   // Transport Layer Security安全传输层协议(TLS),Secure Sockets Layer (SSL),
-->
并加入keystoreFile="C:/temp/.keystore"
默认是读用户主目录下的.keystore文件
	

---- Tomcat 9 打开HTTP 2(只能在https下运行)  
下载 tomcat-native-1.2.18-openssl-1.1.1-win32-bin.zip 解压后的bin/x64/下的 tcnative-1.dll 放PATH中能找到的地方(如Tomcat/bin下)
bin/openssl 命令可用 (Cygwin也有)
    openssl genrsa -out server.key 2048
    openssl rsa -in server.key -out server.key
    openssl req -new -x509 -key server.key -out ca.crt -days 3650
	
	浏览器认为是不可信的
	
把生成的文件放tomcat/conf目录
修改tomcat 9 server.xml中打开
<Connector port="8443" protocol="org.apache.coyote.http11.Http11AprProtocol"
               maxThreads="150" SSLEnabled="true" >
	<UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
	<SSLHostConfig>
		<Certificate certificateKeyFile="conf/localhost-rsa-key.pem"
					 certificateFile="conf/localhost-rsa-cert.pem"
					 certificateChainFile="conf/localhost-rsa-chain.pem"
					 type="RSA" />
	</SSLHostConfig>
</Connector>
修改证书路径 为
	    <Certificate certificateKeyFile="conf/server.key"
                         certificateFile="conf/ca.crt"/>

https://localhost:8443/ 访问项目就是HTTP2协议了,  注意是https ,端口是8443
Chrome network 标签打开protocol列值是h2
Firefox network 标签打开protocol列值是HTTP/2.0


tomcat9/logs/localhost_access_log.x.txt  显示是 HTTP/2.0
----
---tomcat路径文件名支持中文
<Connector port="8080" protocol="HTTP/1.1" 
    connectionTimeout="20000" redirectPort="8443" URIEncoding="utf-8"  />
以上URIEncoding="utf-8"是新加的


---tomcat路径不区分大小写
<Context  path="/mytest" docBase="D:\\Program\\apache-tomcat-6.0.18\\apache-tomcat-6.0.18\\webapps\\test" 
	caseSensitive="false"  reloadable="true"/>
注意这里的caseSensitive="false"

----web.xml <auth-method>FORM</auth-method> ,FORM和BASIC 的密码保存在数据库 tomcat/docs/realm-howto.html
	create table users (
	  user_name         varchar(15) not null primary key,
	  user_pass         varchar(15) not null
	);
	create table user_roles (
	  user_name         varchar(15) not null,
	  role_name         varchar(15) not null,
	  primary key (user_name, role_name)
	);
	
	jdbc.jar 放入tomcat/lib
	
	<Realm className="org.apache.catalina.realm.JDBCRealm"
	      driverName="org.h2.Driver" connectionURL="jdbc:h2:tcp://localhost/~/test" connectionName="sa" connectionPassword =""
	       userTable="users" userNameCol="user_name" userCredCol="user_pass"
	   userRoleTable="user_roles" roleNameCol="role_name"/>
	   
	insert into users(user_name,user_pass) values('lisi','123');
	insert into users(user_name,user_pass) values('zhang','123');
	insert into user_roles(user_name,role_name) values('lisi','develop');
	insert into user_roles(user_name,role_name) values('zhang','market');

	
	
Tomcat 日志文件格式
server.xml中在<Host>下有配置,并有提示文档在  /docs/config/valve.html
	   <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />
	%h - Remote host name (or IP address if enableLookups for the connector is false)
	%l - Remote logical username from identd (always returns '-')
	%u - Remote user that was authenticated (if any), else '-'	
	%t - Date and time, in Common Log Format
	%r - First line of the request (method and request URI)
	%s - HTTP status code of the response
	%b - Bytes sent, excluding HTTP headers, or '-' if zero
	
	示例
	35.8.33.21 - - [06/Jul/2016:00:00:00 +0800] "POST /loan-acct-web/remoting/madeLoanProcessQuerySimpleFacadehession HTTP/1.0" 200 987

linux 下Tomcat会一直向catalina.out这个文件写(windows下就不会)
catalian.sh 脚本中　对 CATALINA_OUT 的说明,
手工设置　CATALINA_OUT="$CATALINA_BASE"/logs/catalina.`date +"%Y-%m-%d"`.out　在判断后加,为了CATALINA_BASE存在值 ,这样只能在重启时是新文件

set CATALINA_OPTS=-Xms512M -Xmx512M -Xss1024k -XX:MaxPermSize=256M

--优化


maxThreads 客户请求最大线程数
 
compression="on"   打开压缩功能

关闭 AJP

下载apr ,Http11AprProtocol
HttpNioAprProtocol

内核参数

--安全
<Host  
	unpackWARs="true" autoDeploy="true"> 
关闭war自动部署 unpackWARs="false" autoDeploy="false"
隐藏tomcat版本
	解压catalina.jar之后按照路径\org\apache\catalina\util\ServerInfo.properties找到文件
修改listings 为 false

=========================JBOSS 7.1.1 使用  (后面新版本 改名为 WildFly)
LGPL 
https://docs.jboss.org/author/dashboard.action 有很的Doc,如JBoss AS 7.1 ,JBoss OSGi,Portlet Bridge
https://docs.jboss.org/author/display/JBASDOC/Home

wildfly-10.1.0.Final\bin\client\jboss-client.jar

安装JBOSS Tools 的eclipse插件时选择jBoss Web and Java EE Developement -> JBossAS Tools 就有启动停止的JBOSS AS-7.1的工具了
		jBoss Application Development -> freemarker IDE,Hibernate Tools
		jBoss Mobile Development(要jetty) -> Cordova Simulator , Mobile Browser Simulator
		jBoss Maven support
		
启动: bin/standalone.bat 			domain.bat  是对 Cluster的启动三个服务
关闭: Ctrl+C 或 jboss-cli.bat --connect --command=:shutdown 

http://127.0.0.1:8080 ->Administration Console  http://127.0.0.1:9990/console (启动日志也有提示)
提示要建立用户,使用bin/add-user.bat ,分为managent user(登录用的)和application user两种用户
 
management user->默认Realm (ManagementRealm)
建立后提示保存在
jboss-as-7.1.1.Final\standalone\configuration\mgmt-users.properties
jboss-as-7.1.1.Final\domain\configuration\mgmt-users.properties

application user->默认Realm (ApplicationRealm)  ,建立时密码不能和用户名相同
会要输入用户属于什么角色,建立的用户不能登录http://127.0.0.1:9990/console
jboss-as-7.1.1.Final\standalone\configuration\application-users.properties
jboss-as-7.1.1.Final\domain\configuration\application-users.properties


启动服务器后,双击jboss-cli.bat会连接9999端口,可以有很多的命令如connect,支持tab键提示命令,
如ls  /subsystem=deployment-scanner:read-resource(recursive=true) ,在按:和(时tab都会提示


配置debug模式
bin/standalone.conf
打开下面
#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n"
删 -XX:MaxPermSize(对JDK8)

部署
cp myapp.war $JBOSS_HOME/standalone/deployments/  
取消部署 
rm $JBOSS_HOME/standalone/deployments/myapp.war.deployed

重新部署删 .undeployed

x.war.pending

 

\jboss-as-7.1.1.Final\standalone\configuration\standalone.xml 可以修改监听端口
	 <socket-binding name="http" port="8080"/>  
配置deployment-scanner 只对standalone模式有效,开发时对修改的文件,每隔5秒更新

jboss-as-7.1.1.Final\standalone\configuration\standalone.xml 中 deployment-timeout="300"

部署CXF 报 Cannot publish wsdl to \standalone\data\wsdl\xx-web.war\XXService.wsdl
在JBoss-7/standalone/configuration/standalone.xml中要注释  <extension module="org.jboss.as.webservices"/> 
或者在 jboss-deployment-structure.xml 中加  <exclusions>  <module name="org.jboss.as.webservices" />

wild-fly-8.1 中报  Apache CXF library (cxf-xxx.jar) detected   jboss-deployment-structure.xml  中加 
<jboss-deployment-structure >
    <deployment>
        <exclude-subsystems>
            <subsystem name="webservices" />
        </exclude-subsystems>
    </deployment>
</jboss-deployment-structure>

JNDI 数据源
standalone.xml	修改
	<subsystem xmlns="urn:jboss:domain:datasources:1.0">
	驱动对应jboss-as-7.1.1.Final\modules\com\h2database\h2\main\module.xml
http://127.0.0.1:9990/console 界面可以看到,点右上角的Profile 后可以配置

Properties env = new Properties();
env.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());//jboss-as-7.1.1.Final\bin\client\jboss-client.jar  3M
env.put(Context.PROVIDER_URL, "remote://localhost:4447");
env.put(Context.SECURITY_PRINCIPAL,"user");//使用add-user.bat,Management User没用的,要Application User可以没有Role
env.put(Context.SECURITY_CREDENTIALS,"pass");
Context context=new InitialContext(env);//.class在容器外,要加参数

DataSource ds=(DataSource)context.lookup("datasources/ExampleDS");
Connection conn=ds.getConnection();
conn.close();
context.close();


java:comp - The namespace is scoped to the current component (i.e. EJB)
java:module - Scoped to the current module
java:app - Scoped to the current application
java:global - Scoped to the application server

java:jboss	服务器共享
java:/
java:jboss/exported 是jBoss 7.1, entries bound to this context are accessible over remote JNDI.

如果调用者的.class文件不在JBoss中只能用java:jboss/exported

//从standalone-full.xml中复制过来修改了java:/jboss为java:/
DataSource ds=(DataSource)context.lookup("datasources/ExampleDS");//.class容器中OK,配置是<datasource jndi-name="java:/datasources/ExampleDS" pool-name="ExampleDS">

	
JMS
复制standalone-full.xml 中的
	<extension module="org.jboss.as.messaging"/>
	...
	<subsystem xmlns="urn:jboss:domain:messaging:1.1">
	..
	<socket-binding name="messaging" port="5445"/>
	<socket-binding name="messaging-throughput" port="5455"/>
	  

ConnectionFactory factory = (ConnectionFactory)context.lookup("jms/RemoteConnectionFactory");//.class在容器外OK，对应配置java:jboss/exported/jms/RemoteConnectionFactory
Queue queue = (Queue)  context.lookup("jms/queue/test");//.class在容器外OK，对应配置java:jboss/exported/jms/queue/test

Context context=new InitialContext();//.class在容器内,不加参数
ConnectionFactory factory = (ConnectionFactory)context.lookup("ConnectionFactory");//.class在容器内OK,ConnectionFactory对应的配置是java:/ConnectionFactory
Queue queue = (Queue)  context.lookup("queue/test");//.class在容器内OK,queue/test对应的配置是 <entry name="queue/test"/>


Object str=context.lookup("java:global/mystring");//.class容器中OK
对应web.xml中
<env-entry>
	<env-entry-name>java:global/mystring</env-entry-name>
	<env-entry-type>java.lang.String</env-entry-type>
	<env-entry-value>Hello World</env-entry-value>
</env-entry>
 
WebRoot的WEB-INF 或 META-INF 下加 jboss-deployment-structure.xml
<jboss-deployment-structure>
    <deployment>
	    <dependencies>
	    	<module name="com.oracle"/> 
				<!-- 找对应jboss 的modules/com/oracle/main/module.xml 文件
					内容对应是 name="com.oracle" ,<resource-root path="properties"/>可是目录也可是jar包做classpath -->
	  	</dependencies>
        <!-- Exclusions allow you to prevent the server from automatically adding some dependencies -->
        <exclusions>
           <module name="org.apache.log4j" />
            <module name="org.slf4j" /> 
            <module name="org.slf4j.impl" />
            <module name="org.apache.cxf" />
			<module name="org.jboss.as.webservices" />
        </exclusions>
    </deployment>
</jboss-deployment-structure>


Https  服务建立
=========================

