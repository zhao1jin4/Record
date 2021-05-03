
============Shiro (安全)  
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-core</artifactId>
  <version>1.5.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-web</artifactId>
  <version>1.5.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-spring</artifactId>
  <version>1.5.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-ehcache</artifactId>
  <version>1.5.2</version>
</dependency>

encoder-1.2.2.jar(org.owasp), ehcache-2.9.0.jar (net.sf)

UsernamePasswordToken token = new UsernamePasswordToken("user", "pass");
Subject currentUser = SecurityUtils.getSubject();
currentUser.login(token);//当使用外部系统验证成功后告诉Shiro已经登录



//iniLogin("classpath:shiro_main/shiro.ini");
//iniLogin("classpath:shiro_main/shiro-realm.ini");
//iniLogin("classpath:shiro_main/shiro-cryptography.ini");
//--- 
//iniLogin("classpath:shiro_main/shiro-permisson.ini");
//hasRole(); 
//hasPermission();//没有缓存，每次都重新取数据
//---
iniLogin("classpath:shiro_main/shiro-permisson-realm.ini");
hasRole();
hasPermission();
 

public static void iniLogin(String configFile) {
	Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(configFile);
	org.apache.shiro.mgt.SecurityManager securitManger = factory.getInstance();
	SecurityUtils.setSecurityManager(securitManger);
	Subject subject = SecurityUtils.getSubject();
	
	org.apache.shiro.session.Session  session=subject.getSession();
	session.setAttribute("myAttr", "myVal");
	System.out.println(session.getAttribute("myAttr"));
	
	UsernamePasswordToken token = new UsernamePasswordToken("lisi", "123");
	token.setRememberMe(true);
	
	try {
		subject.login(token);// 看源码
	} catch (UnknownAccountException userError) {
		System.err.println("user not exits");
	} catch (IncorrectCredentialsException passError) {
		System.err.println("password error ");
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println("login OK?" + subject.isAuthenticated());
	subject.logout();
	System.out.println("login OK?" + subject.isAuthenticated());
}
public static void hasRole()
{  
	Subject subject = SecurityUtils.getSubject();
	Object obj=subject.getPrincipal();//就是认证时放SimpleAuthenticationInfo中的UserInfo类
	//如使用的是UsernamePasswordToken，这就是字串用户名
		
	System.out.println(subject.hasRole("role1"));//看源码
	List<String> list=new ArrayList<>();
	list.add("role1");
	list.add("role2");
	list.add("role3");
	System.out.println(Arrays.toString(subject.hasRoles(list))); //返回boolean数组
	System.out.println(subject.hasAllRoles(list));
	//subject.checkRole("role3");//如没角色报错
}
public static void hasPermission()
{
	Subject subject = SecurityUtils.getSubject();
	System.out.println(subject.isPermitted("user:delete"));//看源码
	System.out.println(subject.isPermittedAll("user:delete","user:update"));
	subject.checkPermission("user:query");//如没 报错
}
 
public static void encPassword() {
	String password="123";
	Md5Hash md5Hash=new Md5Hash(password);
	System.out.println(md5Hash);//散列算法有MD5 和SHA
	
	Md5Hash md5Hash1=new Md5Hash(password,"saltKey");//加盐
	System.out.println(md5Hash1);
	
	Md5Hash md5Hash2=new Md5Hash(password,"saltKey",3);//散列3次
	System.out.println(md5Hash2);//3e751882a57e7f803dcc9c47eeda7be2
	
	/* ini config file
	org.apache.shiro.authc.credential.HashedCredentialsMatcher credntialMatcher=new HashedCredentialsMatcher();
	credntialMatcher.setHashAlgorithmName("md5");
	credntialMatcher.setHashIterations(3);
	
	EncPasswordRealm realm=new EncPasswordRealm();
	realm.setCredentialsMatcher(credntialMatcher);
	*/
} 
---shiro_main/shiro.ini
[users]
#comment
lisi=123
wang=456


---shiro_main/shiro-realm.ini
myRealm=shiro_main.MyRealm
securityManager.realms=$myRealm


// AuthorizingRealm 带认证和制授权 
public class MyRealm extends AuthorizingRealm
{
	@Override
	public String getName() {
		return "MyRealm";
	}
	// 授权  (对于配置 shiro-realm.ini 文件)
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//String username=(String)principals.getPrimaryPrincipal();//可是放session中的对象 ,这里用户名
		UserInfo userInfo=(UserInfo)principals.getPrimaryPrincipal();
		
		//模拟查数据库为用户加角色，权限,不是使用配置文件方式
		
		List<String> roles=new ArrayList<>();
		roles.add("role1");
		
		List<String> permissions=new ArrayList<>();
		permissions.add("user:create");
		
		SimpleAuthorizationInfo auInfo=new SimpleAuthorizationInfo();
		auInfo.addRoles(roles);
		auInfo.addStringPermissions(permissions);//有addObjectPermissions
		return auInfo;
	}
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("MyRealm,"+token);
		String username=(String)token.getPrincipal();
		//查数据库
		if(!"lisi".equals(username))//模拟无此用户
			return null;
		
		// (对于配置 shiro.ini 文件)
		String password="123"; 
		UserInfo userInfo=new UserInfo();
		userInfo.setUserAccount(username);
		userInfo.setFullName("李四");
		userInfo.setMobilePhone("130111122222");
		userInfo.setUserId("1001");
		SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(userInfo,password,getName());
		//SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(username,password,getName());//username可是放session中的对象 ,这里用户名
		
		// (对于配置 shiro-cryptography.ini 文件)
		String saltKey="saltKey";
		Md5Hash md5Hash2=new Md5Hash(password,saltKey,3);//散列3次
		String saltPassword=md5Hash2.toString();//模拟数据库取出的
		
		SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(username,
				saltPassword,ByteSource.Util.bytes(saltKey), //盐是什么要告诉shiro,3次在配置文件中
				getName());
				
		return authInfo;
	}
}

----shiro-cryptography.ini
[main]
credntialMatcher=org.apache.shiro.authc.credential.HashedCredentialsMatcher
credntialMatcher.hashAlgorithmName=md5
credntialMatcher.hashIterations=3

myRealm=shiro_main.EncPasswordRealm
myRealm.credentialsMatcher=$credntialMatcher
securityManager.realms=$myRealm

----shiro-permisson.ini
[users]
#comment
lisi=123,role1,role2
wang=456,role1

[roles]
#表示对资源user有create权限
role1=user:create,user:update
role2=user:delete
role3=user:*
role4=user:update:101
role5=user:*:101
#第三列101表示实例,像某条记录
 
----shiro web

web.xml
  <context-param>
  	<param-name>shiroEnviromentClass</param-name>
  	<param-value>org.apache.shiro.web.env.IniWebEnvironment</param-value>
  </context-param>
   <context-param>
  	<param-name>shiroConfigLocations</param-name>
  	<param-value>classpath:shiro_web/shiro.ini</param-value>
  </context-param>
   <!--  读上面两个param-name,调用IniWebEnvironment的 init 方法会创建SecurityManager 放 servletContext 中供Filter使用 -->
  <listener>
  	<listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
  </listener>
  
  <filter>
  	<filter-name>shiro_web</filter-name>
  	<filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>shiro_web</filter-name>
  	<url-pattern>/*</url-pattern>  */
	<dispatcher>REQUEST</dispatcher>
    <dispatcher>FORWARD</dispatcher>
    <dispatcher>INCLUDE</dispatcher>
    <dispatcher>ERROR</dispatcher>
  </filter-mapping>

  
// anon 定义在 DefaultFilter 中
   
---shiro_web/shiro.ini  
[main]
authc.loginUrl=/login.ser
#如没登录跳转页,要和登录表单提交地址一样才知道从哪取到用户名密码
#authc就是 FormAuthenticationFilter  页中用户名要为username,密码为passsword,修改方法(类中有) 
authc.usernameParam=j_username
authc.passwordParam=j_password
authc.rememberMeParam=j_rememberMe

roles.unauthorizedUrl=/noRole.jsp
#如没有要求的角色跳转页
perms.unauthorizedUrl=/noPerm.jsp
#如没有权限的角色跳转页
logout.redirectUrl=/web/login.jsp
#退出后的跳转页

#perms.enabled=false
#表示不使用这个过滤器

[users] 
lisi=123,adminRole
wang=456,queryRole

[roles]
adminRole=employee:*
queryRole=employee:query

[urls]
#=后是过滤器的顺序 ，路径也是从上到下的顺序匹配
/js/**=anon
/img/**=anon
/web/main.jsp=anon
#/web/login.jsp=anon 
#可以不用配置 logout.redirectUrl已经做了

#anon 是shiro的匿名过滤器
#authc 过滤器 表示必须要登录 ,roles 角色过滤器 

/employee/create.ser=authc,roles[adminRole]
/employee/query.ser=authc,roles[queryRole]
/web/query.jsp=authc,roles[adminRole]
#.jsp也能拦截的,web.xml配置了FORWARD拦截不到

/employee/delete.ser=perms[employee:delete]
#perms 权限过滤器

/logout.ser=logout
#logout 退出过滤器,会清session,页面直接请求这个地址,不用自己实现

#登录表单提交地址要authc
/login.ser=authc
#/**=authc



 

*/

AnonymousFilter anon;
FormAuthenticationFilter authc;
RolesAuthorizationFilter roles;
PermissionsAuthorizationFilter perms;
LogoutFilter logout;


@WebServlet("/login.ser")
public class LoginServlet extends HttpServlet 
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		/*
		#登录表单提交地址要authc
		/login.ser=authc
		#/**=authc
		*/
		String exceptClassName=(String) request.getAttribute("shiroLoginFailure");
		if(exceptClassName!=null)
		{
			if(UnknownAccountException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","用户名不存在");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","密码错误");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","系统异常");
			}
		}
		//如登录成功会直接跳到登录前的页面，如没有登录前的页面默认请求/
		request.getRequestDispatcher("/web/login.jsp").forward(request, response);
	}		
}
----- /web/login.jsp
<form action="<%=request.getContextPath()%>/login.ser" method="post">
	<section>
		<label for="j_username">Username</label> <input  name="j_username" type="text" />
	</section>
	<section>
		<label for="j_password">Password</label> <input  name="j_password" type="password" />
	</section>
	<section>
		<label for="j_rememberMe">remeber me?</label> <input  name="j_rememberMe" type="checkbox" />
	</section>
	<section>
		<input type="submit" value="Login" />
	</section>
</form>

----main.jsp
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<shiro:guest>
	你没有登录，点击 <a href="<%=request.getContextPath()%>/login.ser">这里</a> 登录
</shiro:guest>  <br/>

<a href="${pageContext.request.contextPath}/logout.ser">logout   </a> <br/>

Hello, <shiro:principal/> <br/>
Hello, <%= SecurityUtils.getSubject().getPrincipal()  %> <br/>

<shiro:hasPermission name="employee:delete">
	<a href="<%=request.getContextPath()%>/employee/delete.ser">delete employee ,Permission employee:delete</a> <br/>
</shiro:hasPermission>
<shiro:lacksPermission name="employee:delete">
	你没有 employee:delete 权限
</shiro:lacksPermission>

<shiro:hasRole name="adminRole">
	<a href="<%=request.getContextPath()%>/employee/create.ser">add new employee , adminRole</a> <br/>
</shiro:hasRole>

----shiro spring  

--web.xml
 <filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <init-param>
        <param-name>targetFilterLifecycle</param-name>
        <param-value>true</param-value>
    </init-param>
 </filter>
   
  <filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/*</url-pattern>   */
  </filter-mapping>

  <servlet>
    <servlet-name>spring_mvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring_mvc.xml</param-value><!--  MVC相关的配置  -->
	</init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>spring_mvc</servlet-name>
    <url-pattern>*.mvc</url-pattern>
  </servlet-mapping>
    
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring_shiro.xml</param-value><!-- Shiro相关的Bean注入配置  -->
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

 ---spring_mvc.xml
    <context:annotation-config/>
    <context:component-scan base-package="shiro_spring_xml" />
    <mvc:annotation-driven/> 
    <mvc:default-servlet-handler/>
	<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/WEB-INF/jsp/" p:suffix=".jsp" />
	<bean  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
		<property name="exceptionMappings">
			<props>
				<prop key="org.apache.shiro.authz.UnauthorizedException">redirect:/noPerm.jsp</prop>
				<prop key="org.apache.shiro.authz.UnauthenticatedException">redirect:/login.mvc</prop>  
			</props>
		</property>
	</bean>
	<!-- 使用  @RequiresRoles,@RequiresPermissions  生效   -->
	<bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/> 
 	<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" depends-on="lifecycleBeanPostProcessor">
 		<property name="proxyTargetClass" value="true"></property>
 	</bean>
  <!-- 方式二  对spring aop版本有要求
  	<aop:config proxy-target-class="true"></aop:config>
   -->

---spring_shiro.xml
<bean id="hashedCredentialsMatcher" class ="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
	<property name="hashAlgorithmName" value="md5"></property>
	<property name="hashIterations" value="3"></property> 
</bean>
<bean id="mySpringRealm" class="shiro_spring.MySpringRealm" >
	<property name="credentialsMatcher" ref="hashedCredentialsMatcher"></property>
</bean>
<bean id="mySpringRealm2" class="shiro_spring.MySpringRealm2" >
	<property name="credentialsMatcher" ref="hashedCredentialsMatcher"></property>
</bean>
<bean id="myModularAuthen" class="org.apache.shiro.authc.pam.ModularRealmAuthenticator">
	<property name="authenticationStrategy" >
		<bean class="org.apache.shiro.authc.pam.AllSuccessfulStrategy"></bean>
		<!-- 默认 AtLeastOne 只要一个realm成功就算成功
		<bean class="org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy"></bean>
		<bean class="org.apache.shiro.authc.pam.FirstSuccessfulStrategy"></bean>
		 -->
	</property>
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
</bean>
<bean id="myModularAuthor" class="org.apache.shiro.authz.ModularRealmAuthorizer">
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
</bean>

<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
	<property name="cacheManager" ref="cacheManager"></property>
	<!-- 
	<property name="realm" ref="mySpringRealm" />
	 -->
	 <!-- 
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
	 -->
	 <property name="authenticator" ref="myModularAuthen"></property>
	 <property name="authorizer" ref="myModularAuthor"></property>  
</bean>

<bean id ="cacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
	<property name="cacheManager" ref="ehcacheManager" ></property>
</bean>

<bean id="ehcacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	<property name="configLocation" value="classpath:shiro_spring_xml/ehcache.xml"></property>
	<property name="shared" value="true"></property>
</bean>

<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
	<property name="securityManager" ref="securityManager" />
	<property  name="loginUrl" value="/login.mvc"/> 
	<property name="successUrl" value="/main.mvc"/> 
	<property name="unauthorizedUrl" value="/noPerm.jsp"/>
	<property name="filters"> 
		<util:map> 
			 <entry key="authc" value-ref="formAuthenticationFilter" />
		</util:map> 
	</property> 
	<property name="filterChainDefinitions">
		<value>
			#=后是过滤器的顺序 ，路径也是从上到下的顺序匹配
			/js/**=anon
			/img/**=anon
			/main.mvc=anon
			/logout.mvc=logout
			#logout 退出过滤器,会清session,页面直接请求这个地址,不用自己实现
			
		<!-- /employee/create.mvc=authc,roles[adminRole]
			/employee/query.mvc=authc,roles[queryRole]
			/employee/delete.mvc=perms[employee:delete]
		-->
			#登录表单提交地址要authc
			/login.mvc=authc
			#/**=authc
		</value>
		*/
	</property>
</bean>

 
<bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
	<property name="securityManager" ref="securityManager"/>
</bean>
	
<bean id="formAuthenticationFilter"    class="org.apache.shiro.web.filter.authc.FormAuthenticationFilter">
	<property name="usernameParam" value="j_username" />
	<property name="passwordParam" value="j_password" />
	<property name="rememberMeParam" value="j_rememberMe"/> 
</bean> 
 

--controller
@RequiresAuthentication
@RequiresRoles({"adminRole"})
//结合不显示按钮一起用更更安全(对未配置这个地址)
//也可用于service方法,如没权限报异常,要配置SimpleMappingExceptionResolver (unauthorizedUrl没用)
@RequestMapping(value = "/create" )
public void create( HttpServletRequest requset,HttpServletResponse response) throws Exception {
	UnauthorizedException exp;
	response.getWriter().write("employee/create");

}
@RequiresAuthentication
@RequiresPermissions({"employee:delete"})
@RequestMapping(value = "/delete" )
public void delete(  HttpServletResponse response ) throws Exception {
	response.getWriter().write("employee/delete");
}
//@RequiresGuest 


//spring把@Controller中的所有的@RequestMapping的方法
Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();
for(HandlerMethod method:methods.values())
{
	RequiresAuthentication auth=method.getMethodAnnotation(RequiresAuthentication.class);
	if(	auth!=null   )
		System.out.println("must be login");
	RequiresPermissions perm=method.getMethodAnnotation(RequiresPermissions.class);
	if(	perm!=null   )
		System.out.println("has perm "+Arrays.toString(perm.value()));
	RequiresRoles role=method.getMethodAnnotation(RequiresRoles.class);
	if(	role!=null   )
		System.out.println("has role "+Arrays.toString(role.value()));
}
--- MySpringRealm

//清除登录用户的角色权限信息，不是登录信息，要手动调用 
public void myClearCache() 
{
	PrincipalCollection principalCollection=SecurityUtils.getSubject().getPrincipals();
	super.clearCache(principalCollection);//super=AuthorizingRealm
}


<shiro:authenticated> <!-- 如不加 进入页时 报nullpoint -->
	Hello, <shiro:principal type="shiro_main.UserInfo" property="fullName"/>  相当于取session对象的属性名 	<br/>
</shiro:authenticated>

login.mvc 代码与 login.ser 代码相同
<form action="<%=request.getContextPath()%>/login.mvc" method="post">
</form>

也可以用 
<form action="<%=request.getContextPath()%>/submitLoginNoFilter.mvc" method="post">
</form>
Subject subject = SecurityUtils.getSubject();
UsernamePasswordToken token = new UsernamePasswordToken(username, password);
subject.login(token);//只能自己控制跳转页

------------shiro spring session


------------shiro 自身不支持 oautho2 ,支持JWT，手机端不支持存cookie
  {
	 DefaultWebSecurityManager manager =new DefaultWebSecurityManager();
//	 manager.setRealm(realm);
	 
	 //关闭session为手机端没有cookie，未测试 
	 DefaultSubjectDAO subjectDAO =new DefaultSubjectDAO();
	 DefaultSessionStorageEvaluator sessionStorageEvaluator=new DefaultSessionStorageEvaluator();
	 sessionStorageEvaluator.setSessionStorageEnabled(false);
	 subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
	 manager.setSubjectDAO(subjectDAO);
	 manager.setSubjectFactory(subjectFactory());//DefaultSubjectFactory
	}
	public   DefaultSubjectFactory subjectFactory( ) {
		//不确定还有什么 ？？
		 DefaultSubjectFactory factory=new DefaultSubjectFactory();
		return  factory;
	}
	
//为JWT，未测试 
//放到ShiroFilterFactoryBean中的filter属性中 把/**=authc 修改为这个jwt,其它所有用到的拦截器都要修改/增加新的
//已有的代码 Subject currentUser = SecurityUtils.getSubject().getPrincipal();无效？？？
// 手机端token， 持浏览器cookie只能分开接入？？？
public class NoSessionFilter extends BasicHttpAuthenticationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) 
	{
		//		String[] values=(String[])mappedValue;//当配置 /xx=role[read] 时
		HttpServletRequest req=(HttpServletRequest)request;
		String token =req.getParameter("token");//也可放在http header中
		Subject userInfo=null;//检查token在内存中有吗？登录成功后放Map(Redis带失效时间/DB)中key为随机生成/UUID的token值，value为subject 
		if(token==null)
		{
			System.out.println("未登录");
			return false;
		}
		return true;
	}
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		System.out.println("无权限");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write("{\"code\":503}");
		return false;
	}
}

