
=========================Spring Security 3.1 (老的是ACegi)　安全   5.0.5
要使用Spring 3.x
CAS (JA-SIG Central Authentication Service)流行的开源单点登录系统  org.springframework.security.cas
OpenID		OpenID4Java			org.springframework.security.openid
ACL
Kerberos

http://www.family168.com/tutorial/springsecurity3/html/preface.html

reference/springsecurity.html 是指南的首页
有示例代码
spring-security-samples-contacts-3.1.0.RELEASE.war
spring-security-samples-tutorial-3.1.0.RELEASE.war

spring-security-web-3.1.0.RELEASE.jar
spring-security-core-3.1.0.RELEASE.jar
spring-security-config-3.1.0.RELEASE.jar
spring-security-taglibs-3.1.0.RELEASE.jar

web.xml
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext*.xml</param-value>
	</context-param>

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>		*/
    </filter-mapping>


spring会自动生成登录页,也可自己指定 <form-login login-page="/login.jsp"/>必须是 
	
	${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
	<form action="../j_spring_security_check">
		<input type="text"   name="j_username">
		<input type="password" name="j_password">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>

国际化在 spring-security-core-3.1.x.jar中org.spring.framework.security下
AbstractUserDetailsAuthenticationProvider.badCredentials的key是登录失败的错误信息,覆盖默认的 
 ${sessionScope.	.message}显示

<bean id="tokenRepository" class="org.springframework.security.web.csrf.CookieCsrfTokenRepository"
	p:cookieHttpOnly="false"/

<security:ldap-server />  为测试用途,在应用内部启动一个内嵌LDAP服务器,比配置一个 Apache Directory Server 要简单得多


<security:http auto-config="true" use-expressions="true" access-denied-page="/auth/denied.mvc" >
	<security:intercept-url pattern="/auth/login.mvc" access="permitAll"  requires-channel="http"  method="GET"/>  <!-- permitAll是 SecurityExpressionRoot类中的方法名字 ,有http,https,any-->
	<security:intercept-url pattern="/main/test*"  access="hasAnyRole('ROLE_USER','ROLE_ADMIN')" />
	<security:intercept-url pattern="/main/admin.mvc" access="hasRole('ROLE_ADMIN')"/>
	<security:intercept-url pattern="/main/common.mvc" access="hasRole('ROLE_USER')"/>
	<!--方式一  ,使用浏览器弹出对框的方式输入用户,密码, <security:http-basic />  -->
	<!-- 方式二,自定义页面 -->	
	<security:form-login login-page="/auth/login.mvc"
		 authentication-failure-url="/auth/login.mvc?error=true" 
			     default-target-url="/main/common.mvc"/>
	
	<security:logout  invalidate-session="true" 
					  logout-success-url="/auth/login.mvc" 
							  logout-url="/auth/logout.mvc"/><!-- 对应页面中的退出的链接 -->
	<security:session-management>		
		<security:concurrency-control max-sessions="1" error-if-maximum-exceeded="true"/> <!-- 只可有一个会话用户在线, 要配<listener-class>HttpSessionEventPublisher -->
		<!-- 跳到  authentication-failure-url 指定面,使用 ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}显示
		国际化 ConcurrentSessionControlAuthenticationStrategy.exceededAllowed  -->
	</security:session-management>
	
	<security:csrf token-repository-ref="tokenRepository"/> <!--  对应于 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> -->
</security:http>
	
<!-- 用户,角色,密码的存储方式 -->
<bean id="customUserDetailsService" class="org.liukai.tutorial.service.CustomUserDetailsService"/>	<!--  implements UserDetailsService --> 
 <bean id="myUserDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl"> 
	<property name="dataSource" ref="dataSource"/>
</bean>
<security:authentication-manager>
	<security:authentication-provider user-service-ref="customUserDetailsService" >
		<!--方式一,自定义取用户密码方式,使用user-service-ref="customUserDetailsService" ,就不可有XML子节点
			如 user-service-ref='myUserDetailsService' 同 <jdbc-user-service data-source-ref= > -->
	
		<!-- 方式二,存在DB中 
		<security:jdbc-user-service data-source-ref="dataSource"/>
		-->
		<!--方式三,写在配置文件中 
		<security:user-service> 
			<security:user name="user" authorities="ROLE_USER" password="ee11cbb19052e40b07aac0ca060c23ee"/>
			<security:user name="admin" authorities="ROLE_USER,ROLE_ADMIN" password="21232f297a57a5a743894a0e4a801fc3"/>
		</security:user-service>
		-->	
		<security:password-encoder hash="bcrypt"/> 
	</security:authentication-provider>
</security:authentication-manager>
<!-- 使用 @Secure("ROLE_USER") 要 secured-annotations="enabled" ,
	使用@PreAuthorize或 @PostAuthorize要打开 pre-post-annotations="enabled"-->
<security:global-method-security  secured-annotations="enabled" pre-post-annotations="enabled"/>
public class CustomUserDetailsService implements UserDetailsService {
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException 
	{
		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(2);
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		String bcryptPass= BCrypt.hashpw("123", BCrypt.gensalt());
		UserDetails user=new User("lisi","{bcrypt}"+bcryptPass, true, true, true, true, authList);
		return user;
	} 
}

String bcryptPass= org.springframework.security.crypto.bcrypt.BCrypt.hashpw("user", BCrypt.gensalt()); 
boolean isOK=BCrypt.checkpw("user", bcryptPass);


在JSP页中
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authentication property="name" /> <!--得到登录使用的用户名  --> <br/>

<!-- access="hasPermission(#domain,'read') or hasPermission(#domain,'write')" -->
<security:authorize  access="hasRole('ROLE_ADMIN')">  
	<a href="<%=request.getContextPath()%>/main/admin.mvc"> Go AdminPage </a><br />
</security:authorize>

Java代码中
@Secured({"ROLE_USER"})//要在接口中有效,表示要调用这个方法要用ROLE_USER角色,要有配置<security:global-method-security  secured-annotations="enabled"
public String getHello();

@PreAuthorize("hasAuthority('ROLE_ADMIN')")//要有配置<security:global-method-security pre-post-annotations="enabled",支持表达式,要有角色
public void initAdmin();

@PostAuthorize("isAnonymous()")//要有配置<security:global-method-security pre-post-annotations="enabled",只可是未登录用户才可调用
public void destroy();

//---SQL 为 JDBC implementation of the UserDetailsService (JdbcDaoImpl) 或 <jdbc-user-service data-source-ref= >
create table users(
      username varchar_ignorecase(50) not null primary key,
      password varchar_ignorecase(50) not null,
      enabled boolean not null);

create table authorities (
  username varchar_ignorecase(50) not null,
  authority varchar_ignorecase(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username));
  create unique index ix_auth_username on authorities (username,authority);
  
insert into users(username,password,enabled)values('user','ee11cbb19052e40b07aac0ca060c23ee',1);//密码是user的MD5
insert into users(username,password,enabled)values('admin','21232f297a57a5a743894a0e4a801fc3',1);

insert into authorities(username,authority)values('user','ROLE_USER');
insert into authorities(username,authority)values('admin','ROLE_ADMIN');
insert into authorities(username,authority)values('admin','ROLE_USER');
//---  
 
<authentication-provider ref='myAuthenticationProvider'/>
								实现AuthenticationProvider接口
 
	<remember-me/>

<http>
	<intercept-url pattern="/secure/**" access="hasRole('ROLE_USER')" requires-channel="https"/>有"http", "https" 或 "any"
	通过HTTP尝试访问"/secure/**"匹配的网址，他们会先被重定向到HTTPS网址下

	<port-mappings>
		  <port-mapping http="9080" https="9443"/>
	</port-mappings>
 
 
	如使用了remember-me，返回“未认证”(402)错误，可以在session-management 中添加 session-authentication-error-url属性



Session固定攻击，， 恶意攻击者可以创建一个session访问一个网站的时候，然后说服另一个用户登录到同一个会话上(比如，发送给他们一个包含了session标识参数的链接)
<http>
	<openid-login>
		<attribute-exchange>
		  <openid-attribute name="email" type="http://axschema.org/contact/email" required="true" />
		  <openid-attribute name="name" type="http://axschema.org/namePerson" />
		</attribute-exchange>
	</openid-login>
程序中取到配置
OpenIDAuthenticationToken token = (OpenIDAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
List<OpenIDAttribute> attributes = token.getAttributes();
<http>
	<custom-filter ref="" after="LAST" />可以使用before="FIRST" ,"ANONYMOUS_FILTER" ...


<global-method-security>
   <protect-pointcut expression="execution(* com.mycompany.*Service.*(..))" access="ROLE_USER"/>
</global-method-security>


自定AccessDecisionManager
<global-method-security access-decision-manager-ref="myAccessDecisionManagerBean">
<http					access-decision-manager-ref="myAccessDecisionManagerBean">


tutorial示例是带你入门的很好的
    <http pattern="/loggedout.jsp" security="none"/>
	<http use-expressions="true">
        <intercept-url pattern="/secure/extreme/**" access="hasRole('ROLE_SUPERVISOR')"/>  hasRole是SecurityExpressionRoot中的方法名字
Contacts例子，是一个很高级的例子


不需要特别配置一个Java Authentication and Authorization Service (JAAS)政策文件， 也不需要把Spring Security放到server的classLoader下
不需要把Spring Security放到server的classloader下


	
Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();//在代码中得到密码
										Authentication提供的重要方法是.getAuthorities()//角色
																		.getName();用户名
if (principal instanceof UserDetails) 

UsernamePasswordAuthenticationToken(user,password,new ArrayList 里new SimpleGrantedAuthority("ROLE_USER") )

AuthenticationException 的子类 BadCredentialsException


<user-service id="userDetailsService" properties="users.properties"/>
属性文件需要包含下面格式的内容

username=password,grantedAuthority[,grantedAuthority][,enabled|disabled]


FilterChainProxy


<http realm="字串是在显示在对话框上" >
	<http-basic/> 使用Base64编码,一但登录成功,其它的认证就不用了,不安全的


<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authentication property="name"></security:authentication> 得到登录用户名
<security:authorize ifAllGranted="ROLE_ADMIN,ROLE_USER" 
		<a href="<%=request.getContextPath()%>/main/admin"> Go AdminPage </a>
</security:authorize>


<http auto-config="true"  >就不会配置下面的了
<bean id="" class="org.springframework.security.web.FilterChainProxy">配置过虑器
		<security:filter-chain-map path-type="ant">
			<security:filter-chain filters="" pattern="/**"/>
		</security:filter-chain-map>
</bean>

<http>
	<security:logout 
				invalidate-session="true" 
				logout-success-url="/auth/login" 
				logout-url="/auth/logout"/> //这个是界面中手工注销的地址
				
				
//外部系统验证登录,告诉SpringSecurity验证成功
<security:password-encoder  ref="myPassWordEncoder"  /> //不加密实现
 @Autowired
 AuthenticationManager authenticationManager=null; //<security:authentication-manager id="authenticationManager">

 UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken("username","password");
 token.setDetails(new WebAuthenticationDetails(request));
 Authentication auth=authenticationManager.authenticate(token);
 //会调用<security:authentication-provider user-service-ref="customUserDetailsService" > loadUserByUsername

 SecurityContext context= SecurityContextHolder.getContext();
 context.setAuthentication(auth);	//告诉Spring Security 登录完成			
 SessionAuthenticationStrategy authenticationStrategy=new NullAuthenticatedSessionStrategy();
authenticationStrategy.onAuthentication(auth, request, response

=========================上 Spring Security
=========================Spring Security OAuth2 
<dependency>
    <groupId>org.springframework.security.oauth</groupId>
    <artifactId>spring-security-oauth</artifactId>
    <version>2.3.3.RELEASE</version>
</dependency>




