
=========================Spring Security  5.0.5
 
CAS (JA-SIG Central Authentication Service)流行的开源单点登录系统  org.springframework.security.cas
	https://github.com/apereo/cas  gradle 构建
OpenID		OpenID4Java			org.springframework.security.openid
ACL
Kerberos

http://www.family168.com/tutorial/springsecurity3/html/preface.html

    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-config</artifactId>
    </dependency>
    
 <dependencyManagement>
  	<dependencies>
  	   <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-bom</artifactId>
            <version>5.2.1.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
  </dependencyManagement>

/*
 springSecurity初始化  对应于web.xml配置
源码入口类 WebSecurityConfiguration 的 springSecurityFilterChain 方法 
  有这个类就不能在web.xml中配置 springSecurityFilterChain
 servlet-3 Spring 自己的  WebApplicationInitializer
 */
public class SecurityWebApplicationInitializer
	extends AbstractSecurityWebApplicationInitializer {

	public SecurityWebApplicationInitializer() {
		//		super(WebSecurityConfig.class);//如果当前没Spring,SpringMVC这里放配置类，如有注释
	}
}

xml配置debug得到的是 FilterChainProxy 官方文档强调了filter顺序

reference/springsecurity.html 是指南的首页
有示例代码
 
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
   
   
   AuthenticationManager用来验证登录，AccessDecisionManager用来验证权限 

网上查源码 HttpSecurityBeanDefinitionParser 中的registerFilterChainProxyIfNecessary方法中 最后一行
registerAlias(BeanIds.FILTER_CHAIN_PROXY,  //org.springframework.security.filterChainProxy
				BeanIds.SPRING_SECURITY_FILTER_CHAIN);//对应于web.xml中的 springSecurityFilterChain
	在 new 	AuthenticationConfigBuilder 时注册了很多Filter
	在 new HttpConfigurationBuilder 时注册了很多Filter
	  都使用 SecurityFilters 类定义了Filter,按这个顺序在排序 都存在 DefaultSecurityFilterChain 类中
	<security:http pattern="/js/*" security="none"></security:http> 也是创建了 DefaultSecurityFilterChain

spring会自动生成登录页,也可自己指定 <form-login login-page="/login.jsp"
				username-parameter="j_username"
			    password-parameter="j_password"
			    login-processing-url="/j_spring_security_check"

/>必须是 
	
	${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
	<!--  默认三个参数名是username,password,remember-me   提交默认 /login-->
	<form action="../j_spring_security_check">
		<input type="text"   name="j_username">
		<input type="password" name="j_password">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	    <input type="checkbox"  name="j_remember-me" />
	</form>

国际化在 spring-security-core-5.0.5.jar/org/spring/framework/security/message.properites  
AbstractUserDetailsAuthenticationProvider.badCredentials 的key是登录失败的错误信息,覆盖默认的 
 使用${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}显示

<bean id="tokenRepository" class="org.springframework.security.web.csrf.CookieCsrfTokenRepository"
	p:cookieHttpOnly="false"/

<security:ldap-server />  为测试用途,在应用内部启动一个内嵌LDAP服务器,比配置一个 Apache Directory Server 要简单得多


<security:http auto-config="true" use-expressions="true" > <!-- 新版本无 access-denied-page="/auth/denied.mvc" -->
	<security:intercept-url pattern="/auth/login.mvc" access="permitAll"  requires-channel="http"  method="GET"/>  <!-- permitAll是 SecurityExpressionRoot类中的方法名字 ,有http,https,any-->
	<security:intercept-url pattern="/main/test*"  access="hasAnyRole('ROLE_USER','ROLE_ADMIN')" />
	<security:intercept-url pattern="/main/admin.mvc" access="hasRole('ROLE_ADMIN')"/>
	<security:intercept-url pattern="/main/common.mvc" access="hasRole('ROLE_USER')"/>
	<!--方式一  ,使用浏览器弹出对框的方式输入用户,密码, <security:http-basic />  -->
	<!-- 方式二,自定义页面 -->	
	<security:form-login login-page="/auth/login.mvc"
		 authentication-failure-url="/auth/login.mvc?error=true" 
			     default-target-url="/main/common.mvc"
				  authentication-success-handler-ref="myAuthenticationSuccessHandler"
				 /><!-- myAuthenticationSuccessHandler implements AuthenticationSuccessHandler 会替代  default-target-url 的值 -->
	
	<security:logout  invalidate-session="true" 
					  logout-success-url="/auth/login.mvc" 
					  logout-url="/securityLogout"/><!--  对应页面中的退出的链接不用.mvc , 无效的原因是LogoutFilter源码要求POST提交  -->
	<security:session-management invalid-session-url="/auth/invalidSession.mvc">		
		<security:concurrency-control max-sessions="1" error-if-maximum-exceeded="true"/>
		<!--  max-sessions="1"   可防止一个用户登录多次， error-if-maximum-exceeded="true" 表示不可以第二次登录(false则是可以，前面登录的退出)，如有跳到form的 authentication-failure-url 
		 如第二次登录是用remember-me 返回401或者定义session-authentication-error-url
		 要配<listener-class>HttpSessionEventPublisher -->
		<!-- 跳到  authentication-failure-url 指定面,使用 ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}显示
		国际化 ConcurrentSessionControlAuthenticationStrategy.exceededAllowed  
		在 spring-security-core-5.0.5.jar/org/spring/framework/security/message.properites  
		-->
	</security:session-management>
	<security:csrf token-repository-ref="tokenRepository"/> <!--  对应于 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> -->
	
	<security:remember-me remember-me-parameter="j_remember-me" user-service-ref="myDBUserService"  remember-me-cookie="my_remember-me-cookie" />
			<!--  data-source-ref="dataSource"  remember-me-cookie 默认 "remember-me-cookie"  -->
	
	<security:custom-filter ref="myFilter" after="FIRST" /> <!-- Servlet Filter 可以使用before="FIRST" ,"ANONYMOUS_FILTER" ...-->

</security:http>
	
	
<form method="post" action="<%=request.getContextPath()%>/securityLogout">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<input type="submit" value="退出登录提交"/>
</form>
	
	
<!-- 用户,角色,密码的存储方式 -->
<bean id="customUserDetailsService" class="org.liukai.tutorial.service.CustomUserDetailsService"/>	<!--  implements UserDetailsService --> 
 <bean id="myUserDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl"> 
	<property name="dataSource" ref="dataSource"/>
</bean>
<security:jdbc-user-service id="myDBUserService"  data-source-ref="dataSource"/>

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
		
		String bcryptPass= BCrypt.hashpw("123", BCrypt.gensalt());//每次生成的结果不一样，但可以认证成功
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
      username varchar(50) not null primary key, -- varchar_ignorecase
      password varchar(50) not null,
      enabled boolean not null);

create table authorities (
  username varchar(50) not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username));
  create unique index ix_auth_username on authorities (username,authority);
  
insert into users(username,password,enabled)values('user','ee11cbb19052e40b07aac0ca060c23ee',1);//密码是user的MD5
insert into users(username,password,enabled)values('admin','21232f297a57a5a743894a0e4a801fc3',1);

insert into authorities(username,authority)values('user','ROLE_USER');
insert into authorities(username,authority)values('admin','ROLE_ADMIN');
insert into authorities(username,authority)values('admin','ROLE_USER');
-- for <remember-me data-source-ref="dataSource">
create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null);
								
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
 


<global-method-security>
   <protect-pointcut expression="execution(* com.mycompany.*Service.*(..))" access="ROLE_USER"/>
</global-method-security>


自定AccessDecisionManager 来验证权限
<global-method-security access-decision-manager-ref="myAccessDecisionManagerBean">
<http					access-decision-manager-ref="myAccessDecisionManagerBean">

AccessDecisionManager的默认实现有3个
AffirmativeBased  只要有一个通过，就通过，全部弃权（abstain）也通过
ConSensusBased 少数服从多数 如相同并不等于0，根据属性决定
UnanimousBased   有一个拒绝就算拒绝，全部弃权根据属性决定


public class MyAccessDecisionManager implements AccessDecisionManager
{
	//authentication用户信息，有权限 
	//object 仿问的资源 ，
	//configAttributes 资源要哪些权限
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

	}
	//....
}	

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
	 		
				
//外部系统验证登录,告诉SpringSecurity验证成功
<security:password-encoder  ref="myPassWordEncoder"  /> //不加密实现
 @Autowired
 AuthenticationManager authenticationManager=null; //<security:authentication-manager id="authenticationManager"> 用来验证登录

 UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken("username","password");
 token.setDetails(new WebAuthenticationDetails(request));
 Authentication auth=authenticationManager.authenticate(token);
 //会调用<security:authentication-provider user-service-ref="customUserDetailsService" > loadUserByUsername
//和 PasswordEncoder 的matches

 SecurityContext context= SecurityContextHolder.getContext();
 context.setAuthentication(auth);//告诉Spring Security 登录完成 ??
 SessionAuthenticationStrategy authenticationStrategy=new NullAuthenticatedSessionStrategy();
 authenticationStrategy.onAuthentication(auth, request, response);//告诉Spring Security 登录完成???
//不能让spring认为已经登录了？？？？，如权限不够会报 Access is denied
//有用的系统这里是SessionFixationProtectionStrategy，而我是CompositeSessionAuthenticationStrategy？？？？



@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()//允许跨域
			.authorizeRequests()
//			.antMatchers("/r1/*").hasAnyAuthority("") 
//			.antMatchers("/r2/*").hasRole("")
//			.antMatchers("/r3/*").access("hasAuthority('p1') and hasAuthority('p2') ")
			
			.antMatchers("/r/*").authenticated()//这个地址必须登录
			.anyRequest().permitAll() //其它的允许
		.and()
			.formLogin() //可以表单来登录
			.loginPage("/login-view") //登录页
	    	.loginProcessingUrl("/login") //登录页提交的地址
			.successForwardUrl("/login-success")//登录成功地址
//	        .failureUrl("/login.jsp?authentication_error=true")
	 .and().logout()
	 	.logoutUrl("")//注销请求地址
//	 	 .addLogoutHandler(logoutHandler)
//	 	.logoutSuccessHandler(logoutSuccessHandler)//退出前的清理工作 logoutSuccessUrl就无用了
	 	.logoutSuccessUrl(""); //注销后的地址
		
	}
}


@EnableGlobalMethodSecurity(//和Configuration放一起
			securedEnabled = true// 方法上做权限 @Secured的总开关 
			,prePostEnabled = true //方法上做权限@PreAuthorize,@PostAuthorize的总开关  
		) 
//@PreAuthorize("hasAnyAuthority('p1')")     @PreAuthorize("isAnonymous()")
//  @Secured("IS_AUTHENTICATED_ANONYMOUSLY")   @Secured("ROLE_XX") 建议放在controller方法 



<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-jwt</artifactId>
    <version>1.1.0.RELEASE</version>
</dependency>
=========================上 Spring Security
=========================Spring Security OAuth2 
https://projects.spring.io/spring-security-oauth/docs/Home.html   有文档和示例代码 有过时的

/oauth/authorize (the authorization endpoint)
/oauth/token(the token endpoint), 
/oauth/confirm_access (user posts approval for grants here)
/oauth/error (used to render errors in the authorization server)
/oauth/check_token (used by Resource Servers to decode access tokens)
/oauth/token_key (exposes public key for token verification if using JWT tokens).


<dependency>
    <groupId>org.springframework.security.oauth</groupId>
    <artifactId>spring-security-oauth2</artifactId>
    <version>2.4.0.RELEASE</version>  <!-- 2.4.0版本 使用Spring Security 5.2.x  很多security.oauth2.类都过时了-->
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
    <version>5.2.1.RELEASE</version>
</dependency>

---OAuth 2.0 模式1  Authorization Code 授权码

用户在第三方(美团)上点击 微信图标 ，第三方带client_id参数做重定向到微信官方 (Authorization Server)
	(如不是重定向第三方也可forward？ 但浏览器上网址是不可信的，手机app看不到地址,是不是可信的也不知道 ) 
用户在微信官方输入密码（同意取信息可是登录后）-> 微信官方返回授权码给用户浏览器  (每次返回授权码是不一样的)
用户浏览器 使用授权码请求第三方（期间就算被拦载也会很快被第三方使用，再用就无效，https不能对url加密） ->第三方使用用户的授权码（区分哪个用户，应该使用第三方client_id,secret） -> 微信官方 ->第三方的token
第三方使用token (第三方没必要保存下token) 向微信官方 (Resource Server)取用户数据

	同一微信用户，同一client_id每次都返回token会过期，在到期前可以使用refresh_token的值请求更新token
	因第三方根要保存用户数据如用户在第三方上的订单，可根据第一次请求查的数据做唯一条件

提供方，要临时保存授权码,token做校验使用

https://tools.ietf.org/html/rfc6749#page-24
--请求报文协议  使用 application/x-www-form-urlencoded

response_type 必须为 code
client_id
redirect_uri 可选的 手机客户端 okhttp 不能跳转
scope 可选的 取哪些数据
state 推荐 可以防止  cross-site request forgery （CSRF）

--响应报文协议 使用 application/x-www-form-urlencoded
code 授权码
state  是请求的值

--错误响应
error ：可取值为 invalid_request，unauthorized_client，access_denied，unsupported_response_type，invalid_scope，server_error，temporarily_unavailable
error_description：
error_uri
state 


--请求token报文协议
grant_type 必须是 authorization_code
code 			前向返回的授权码
redirect_uri
client_id
没有client_secret？？？

--返回示例
  HTTP/1.1 200 OK
  Content-Type: application/json;charset=UTF-8
  Cache-Control: no-store
  Pragma: no-cache

  {
    "access_token":"2YotnFZFEjr1zCsicMWpAA",
    "token_type":"example",
    "expires_in":3600,
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
    "example_parameter":"example_value"
  }

----模式2 client模式，没用用户参与,适合全后台服务，第三方向(是微信的内部服务器，或者向每个用户发通知消息)微信请求token 
https://tools.ietf.org/html/rfc6749#page-40
--请求报文
grant_type 必须是 client_credentials
scope
应该还有 client_id,client_secret 

----模式3 密码模式，第三方使用用户给的用户名和密码请求微信要token,这个几乎是没用的
----模式4 隐式Implicit模式 没有第三方后台的参与，说是不安全  

--------JSON Web Token (JWT) 
https://tools.ietf.org/html/rfc7519

token中存有用户信息，不必大量调用远程制授权服务验证token，资源服务可按算法自验证，不必连接授权服务验证
可能数据有点长


  使用 HMAC SHA-256 算法
  基于json ,使用加密，防止数据被修改
  
JSON Web Signature (JWS)
JSON Web Encryption (JWE)

三部分
Header 会以Base64编码 
     {"typ":"JWT",
      "alg":"HS256"}
payload 会以Base64编码 可扩展

Signature 把前两部的base64加在一起，和一个密钥，做加密，
















  
