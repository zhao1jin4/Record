=========Liferay Portal 6.2 Community Edition GA1 
使用基于Tomcat7的 下载 bundled with tomcat
Liferay IDE-2.1.1 是elipse插件
还要下载 plugins SDK 和 portal javadoc


初始配置保存在 liferay-portal-6.2.0-ce-ga1\portal-setup-wizard.properties

默认是使用HSQL 数据, 也可以在 http://localhost:8080/ 中配置 生成文件保存在 liferay-portal-6.2-ce-ga2/portal-setup-wizard.properties 
liferay-portal-6.2.0-ce-ga1\tomcat-7.0.42\lib  放 mysql-jdbc.jar
liferay-portal-6.2.0-ce-ga1\tomcat-7.0.42\webapps\ROOT\WEB-INF\classes  下建立 portal-ext.properties
建立了 180 个表 没有前缀
# MySQL
jdbc.default.driverClassName=com.mysql.jdbc.Driver
jdbc.default.url=jdbc:mysql://localhost3306/liferay62?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false
jdbc.default.username=liferay62
jdbc.default.password=liferay62
# Oracle
#jdbc.default.driverClassName=oracle.jdbc.driver.OracleDriver
#jdbc.default.url=jdbc:oracle:thin:@localhost:1521:xe
#jdbc.default.username=liferay62
#jdbc.default.password=liferay62


jdbc.default.maxIdleTime=600
jdbc.default.maxPoolSize=10
jdbc.default.minPoolSize=2
jdbc.default.jndi.name=jdbc/LiferayPool

liferay-portal-6.2-ce-ga2\tomcat-7.0.42\webapps\中直接删自己不使用portlet目录,也可删  welcome-theme ,calendar-portlet  ,opensocial-portlet

建立liferay  portlet项目  也要做 ant D:\Program\liferay-plugins-sdk-6.2.0\portlets\build.xml 会使用maven下载到 liferay-plugins-sdk-6.2.0\.ivy\ 目录下

cd D:\Program\liferay-plugins-sdk-6.2.0\portlets  (eclipse建立liferay项目也在这个目录下)
create.bat my-greeting2 "My Greeting2"  使用ANT建立项目,会生成目录my-greeting2-portlet(本来没有目录)

在建立的项目ant deploy  会连接Maven再线下载jsf.jar和其它bridge.jar到.ivy\ 目录下,
不能下载要修改配置liferay-plugins-sdk-6.2.0\ivy-settings.xml 中修改chain中jboss-nexus项对应的路径为正确的路径 http://oss.sonatype.org/content/repositories/releases/ 没有最新版本,只能版本自己的项目生成ivy.xml文件
http://oss.sonatype.org/content/repositories/snapshots/ 有新版本但要加-SNAPSHOT
(可以删build.xml,ivy.xml,weblogic.xml,faces-confgi.xml,修改web.xml,修改portlet.xml)

liferay-portal-6.2.0-ce-ga1\tomcat-7.0.42\lib\ext  下有 portlet2.0.jar也有MySQL的jar
liferay-portal-6.2.0-ce-ga1\tomcat-7.0.42\webapps下保留ROOT,自己建立的项目需要marketplace-portlet 其它的全删

如是eclipse建立项目会在liferay-portal-6.2.0-ce-ga1\tomcat-7.0.42\conf\Catalina\localhost下建立XML文件

6.2插件建立server时虽然显示只有6.1版本(无论是配置6.1的server还是6.2的server),但实际上不能部署6.1的项目报Log4j类强转错误,

6.2 界面中 Add(+)->Applications-> My Sample (是liferay-display.xml文件中配置的国际化) 标签下有自己的项目

import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;

import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("view")
class XXController
{
	@RenderMapping  //对应@RequestMapping("view"),这里是进入前显示要调用的
	public String xxx(RenderRequest renderRequest, RenderResponse renderResponse, Model model)//Render显示
	{
		HttpServletRequest req1=PortalUtil.getHttpServletRequest(renderRequest);
		HttpServletRequest req2=PortalUtil.getOriginalServletRequest(req1);
		req2.getParameter("userName");
				
		return "viewName";
	}
	@RenderMapping(params = "addUserRender=toAddUserPage")
	public String toAddUserPage(RenderRequest renderRequest, RenderResponse renderResponse) {
		return "add";
	}
	
	@ActionMapping(params = "addUserAction=addUser")
	public void yyy(ActionRequest actionRequest, ActionResponse actionResponse, Model model,   //Action表单提交
		@ModelAttribute("userForm") User userForm) 
	{
		 
		actionRequest.setAttribute("user", user);
		actionResponse.sendRedirect(genStr(actionRequest));
		 
	}
	
	public String genStr(PortletRequest portletRequest)
	 {
		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String portletName = PortalUtil.getPortletId(portletRequest);
		long plid = themeDisplay.getPlid();
		// Create and form a new request by the parameters.
		PortletURL aPortletURL = PortletURLFactoryUtil.create(portletRequest,portletName, plid, PortletRequest.RENDER_PHASE);
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();
		// RenderMapping request, forward the add.jsp page.
		params.put("addUserRender", new String[] { "toAddUserPage" });
		MapUtil.merge(aPortletURL.getParameterMap(), params);
		aPortletURL.setParameters(params);
		return aPortletURL.toString();
	}


}
<bean id="handlerMapping" class="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping" />


<portlet:renderURL var="addUserRender">
	<portlet:param name="addUserRender" value="toAddUserPage" />
</portlet:renderURL>
<a href="${addUserRender}">Add</a>

<portlet:actionURL var="addUserAction">
	<portlet:param name="addUserAction" value="addUser" />
</portlet:actionURL>
<form name="userForm" action="${addUserAction}" method="post">
</form>

JS 方法
Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>'); //刷新当前Portlet

<portlet-app xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
	version="2.0">
	<portlet>
		<portlet-name>searchUserPortlet</portlet-name>
		<display-name>SearchUserPortlet</display-name>
		<portlet-class>org.springframework.web.portlet.DispatcherPortlet</portlet-class>
		<init-param>
			<name>contextConfigLocation</name>
			<value>/WEB-INF/searchUserPortlet-portlet.xml</value>
		</init-param>
		<expiration-cache>0</expiration-cache>
		<supports>
			<mime-type>text/html</mime-type>
			<portlet-mode>view</portlet-mode>
		</supports>
		<portlet-info>
			<title>SearchUserPortlet Demo</title>  <!-- 拖入后,portlet自身的标题 -->
			<short-title>SearchUserPortlet Demo</short-title>
			<keywords>SearchUserPortlet Demo</keywords>
		</portlet-info>
	</portlet>
</portlet-app>


---6.1
以下文件eclipse插件会自动生成
---liferay-display.xml
<?xml version="1.0"?>
<!DOCTYPE display PUBLIC "-//Liferay//DTD Display 6.1.0//EN" "http://www.liferay.com/dtd/liferay-display_6_1_0.dtd">
<display>
	<category name="SearchUserResultPortlet"> <!-- 是卷栏的标题,也可国际化的串-->
		<portlet id="searchUserResultPortlet" /> <!-- 必须与 portlet.xml中的 <portlet-name>searchUserResultPortlet</portlet-name> 相同-->
	</category>
</display>


----liferay-portlet.xml
<?xml version="1.0"?>
<!DOCTYPE liferay-portlet-app PUBLIC "-//Liferay//DTD Portlet Application 6.1.0//EN" "http://www.liferay.com/dtd/liferay-portlet-app_6_1_0.dtd">
<liferay-portlet-app>
	<portlet>
		<portlet-name>searchUserResultPortlet</portlet-name><!-- 要和 portlet.xml中<portlet-name>searchUserPortlet</portlet-name>  -->
		<icon>/images/icon.png</icon>
		<instanceable>true</instanceable>  <!-- 在一个页中,是可以多个portlet实例 -->
		<header-portlet-css>/css/main.css</header-portlet-css>
		<header-portlet-javascript>/js/jquery-1.7.2.min.js</header-portlet-javascript>
	</portlet>
</liferay-portlet-app>

6.2版本
<liferay-portlet-app>
	<portlet>
		<portlet-name>greeting</portlet-name>
		<icon>/icon.png</icon>
		<requires-namespaced-parameters>false</requires-namespaced-parameters>
		<ajaxable>false</ajaxable>
		<header-portlet-css>/css/main.css</header-portlet-css>
		<css-class-wrapper>greeting-portlet</css-class-wrapper>
	</portlet>
	<role-mapper>
		<role-name>administrator</role-name>
		<role-link>Administrator</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>guest</role-name>
		<role-link>Guest</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>power-user</role-name>
		<role-link>Power User</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>user</role-name>
		<role-link>User</role-link>
	</role-mapper>
</liferay-portlet-app>

----liferay-plugin-package.properties
name=searchUserResultPortlet
module-group-id=liferay
module-incremental-version=1
tags=
short-description=
long-description=
change-log=
page-url=http://www.liferay.com
author=Liferay, Inc.
licenses=LGPL
liferay-versions=6.1.0+
-----以下是6.2生成的
portal-dependency-jars=commons-beanutils.jar,commons-collections.jar,commons-digester.jar,commons-fileupload.jar,commons-io.jar,commons-lang.jar
speed-filters-enabled=false


6.2 多生成的liferay-hook.xml
<!DOCTYPE hook PUBLIC "-//Liferay//DTD Hook 6.2.0//EN" "http://www.liferay.com/dtd/liferay-hook_6_2_0.dtd">
<hook>
	<language-properties>Language_en_US.properties</language-properties>
</hook>
 6.2生成JSF的portlet.xml
<portlet-app xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd" version="2.0">
	<portlet>
		<portlet-name>greeting</portlet-name>
		<display-name>Greeting</display-name>
		<portlet-class>javax.portlet.faces.GenericFacesPortlet</portlet-class>
		<init-param>
			<name>javax.portlet.faces.defaultViewId.view</name>
			<value>/views/view.xhtml</value>
		</init-param>
		<expiration-cache>0</expiration-cache>
		<supports>
			<mime-type>text/html</mime-type>
		</supports>
		<portlet-info>
			<title>Greeting</title>
			<short-title>Greeting</short-title>
			<keywords>Greeting</keywords>
		</portlet-info>
		<security-role-ref>
			<role-name>administrator</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>guest</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>power-user</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>user</role-name>
		</security-role-ref>
	</portlet>
</portlet-app>

6.2生成的JSF　xhtml
<f:view
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets"
>
	<h:head />
	<h:body>
		<h:outputText value="#{i18n['greeting-hello-world']}" /> <!-- 可以直接用i18n -->
	</h:body>
</f:view>
6.2生成的 faces-config.xml
<lifecycle>
		<phase-listener>com.liferay.faces.util.lifecycle.DebugPhaseListener</phase-listener>
</lifecycle>
	
生成的HTML和JS全是一行的,没办法debug


Velocity 中的变量对应 
$portletDisplay = 	com.liferay.portal.theme.PortletDisplay
$htmlUtil = 		com.liferay.portal.kernel.util.HtmlUtil
$portletURLFactory= com.liferay.portlet.com.liferay.portlet.PortletURLFactory 的实现类
$company=			com.liferay.portal.model.Company 的实现类
$user=				com.liferay.portal.model.Group 的实现类
$request=			javax.servlet.http.HttpServletRequest
$page
$writer
$theme
自定义宏  #language


在6.1中修改log在public page有log标签
建立Site Template 
建立Page template 后,再建立页时
Site Template中加Page,可没有Page Template,也可有PageTemplate,注意复选框,有一个会在page修改时也修改page template

Page Template和Site Template都可以修改页中的portlet
使用先使用Template生成后,如果再修改Template将不会影响生成的


使用Site Template 生成Site,也可以有自己的Page
User登录后可以有自己的Page

建立theme项目 在_diff目录中放自己要修改的文件,如_diff/css/custom.css,_diff/templates/portal_normal.vm


建立liferay  layout 项目生成目录 liferay-plugins-sdk-6.2.0\layouttpl
建立liferay  theme 项目生成目录 liferay-plugins-sdk-6.2.0\themes
 
--标题国际化 自定义key,全局文件
---liferay-hook.xml
<?xml version="1.0"?>
<!DOCTYPE hook PUBLIC "-//Liferay//DTD Hook 6.2.0//EN" "http://www.liferay.com/dtd/liferay-hook_6_2_0.dtd">
<hook>
	<language-properties>Language_en_US.properties</language-properties>
</hook>
<display>
	<category name="category.mysample"> <!-- 国际化Key -->
		<portlet id="portlet62_table" />
	</category>
</display>

<portlet>
	<title>portlet62_table_title</title><!-- 国际化Key -->

classpath下的Language_en_US
portlet62_table_title=Hello Portlet62 Table!
category.mysample=my samples



Liferay6.2 版本中不能在JSP中仿问Session



支持Solr ,cluster,jcr


