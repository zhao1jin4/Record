
-----------------------------Applet
只能是JApplet,不能是JFrame ,JApplet中可加JButton...  
<applet archive="mylib.jar,myDeps.jar" 　 CODE="MyJApplet.class"　width="500" height="500" ></applet>

HtmlConverter:将<apple>标签转换成 java插件 <object>标签


---------------FCKeditor
复制fckeditor/editor目录,fckeditor.js,fckconfig.js,fckstyles.xml,fcktemplates.xml
复制三个jar包+ core包

删除 WebRoot\editor\lang 目录下不需要的语言，如保留中文和英文还有 fcklanguagemanager.js 文件

删除 \WebRoot\editor\skins 目录下不需要的皮肤文件，有三种皮肤，可根据需要进行删除

删除 \WebRoot\editor\_source目录

打开/项目/fckconfig.js修改 FCKConfig.DefaultLanguage = 'zh-cn' ;





edit 文件夹挎贝到项目中的根目录下
ckconfig.js、fckeditor.js、fckstyles.xml、fcktemplates.xml 文件也挎贝到项目中的根目录下

FCKeditor-java 中的lib 
还要一个slf4j 中的一个simple

<servlet-class>
	net.fckeditor.connector.ConnectorServlet
</servlet-class>
<load-on-startup>1</load-on-startup>

<url-pattern>/fckeditor/editor/filemanager/connectors/*</url-pattern>   <!-- */-->



<script type="text/javascript">
	function FCKeditor_OnComplete(editorInstance)  //页面(FCKeditor)加载完成
	{
		window.status = editorInstance.Description;//完成
	}
</script>
<%
		FCKeditor fckEditor = new FCKeditor(request, "EditorDefault");//另一页用这个parameterName来取字串
%>
<li><FCK:check command="CompatibleBrowser" /></li>  //显示是否启用...没试用吧..多语言可???
<li><FCK:check command="FileBrowsing" /></li>
<li><FCK:check command="FileUpload" /></li>

方法一
<%
	fckEditor.setValue("This is some <strong>sample text</strong>. You are using <a href=\"http://www.fckeditor.net\">FCKeditor</a>.");
	out.println(fckEditor);
%>




方法二 ,标签形式
<FCK:editor instanceName="EditorDefault">
	<jsp:attribute name="value">This is some <strong>sample text
		</strong>. You are using <a href="http://www.fckeditor.net">
		FCKeditor</a>.
	</jsp:attribute>
</FCK:editor>







<FCK:editor id="content"  /> //传参数的名




l. 将FCKeditor目录下及子目录下所有以“_”下划线开头的文件夹删除   
2. 还可以将editor/skins目录下的皮肤文件删除，只留下default一套皮肤（如果你不需要换皮肤的话）xx\editor\skins
3. 还可以将editor/lang目录下文件删除，只保留en.js, zh-cn.js, zh.js文件（英文，简体中文，繁体中文一般应该够用了）xx\editor\lang

前台让普通用户也能使用FCKEditor，要注意相关安全问题。工具集Basic

 toolbarSet="Default"：工具集名称，即出现在页面上的工具条上的工具按钮，参考fckconfig.js，默认值是Default。


--------
官方看的

方法一   
<script type="text/javascript" src="fckeditor/fckeditor.js">


<script type="text/javascript">
var oFCKeditor=new FCKEditor('FCKeditor1');
oFCKeditor.BasePath="/fckeditor/";   //最后一定要有/,所在的目录 一般是/project/fckeditor/
oFCKeditor.Create();
</script>

方法二


<script type="text/javascript">
	window.onload=function()
	{
		var oFCKeditor =new FCKeditor('MyTextArea');
		oFCKeditor.BasePath='/project/fckeditor/';
		oFCKeditor.ReplaceTextarea();
	}
</script>


<textarea id="MyTextArea" name="MyTextArea">this is </textarea>



---------
Width
Height
Value	初始值
ToolbarSet (Default,Basic)
BasePath



构造方法 (instancename,width,height,toolbarSet,value)

JSP 标签方法
<%@taglib uri="" prefix="FCK" %>
<FCK:editor instanceName="xx" basePath="fckeditor" value="xx"/>
value 必须要有,也要有值,否则NullPointException




fckconfig.js配置文件
一般自己写一个(如myconfig.js)来覆盖它
FCKConfig.CustomConfigurationPath='/myconfig.js';//对所有的有效


var oFCKeditor =new FCKeditor('FCKeditor1');
oFCKeditor.Config("myfonfig.js");
//oFCKeditor.Config["CustomConfigurationPath"]="myfonfig.js";
oFCKeditor.Create();

IE 中ctrl +F5 ,强制刷新 ,Firefox 中 ctrl+shift+R



自定义工具栏,   提制FCKConfig.ToolbarSets["Default"]=  改一改 放在自己的配置文件中
oFCKeditor.ToolbarSet="My"


加字体, FCKConfig.FontName="" 复制改一改  UTF-8编码

改回车
FCKConfig.EnterMode='p'
FCKConfig.ShiftEnterMode='br'


加表情
FCKConfig.SmilePath
FCKConfig.SmileImages
FCKConfig.SmileColumns
FCKConfig.SmileWindowWidth
FCKConfig.SmileWindowHeight

shift+enter 是<br>
enter 是<p>



如表情太多,窗口太大,看源码后,改文件fck_smiley.html文件 <body style="overflow:hidden"> 改scroll或者auto
和注释 dialog.SetAutoSize{true} ;行



尽量使用相对路径,或者使用BasePath,或者FCKConfig.EditorPath



文件上传
web.xml  中加servlet ,net.fckeditor.connector.ConnectorServlet
		load-on-startup   1

		/fckeditor/editor/filemanager/connectors/ 下所有的

在classpath中新建一个文件fckeditor.properties 中写入connector.useActionImpl=net.fckeditor.requestcycle.impl.UserActionImpl
看java中的文档


中文文件名乱码　，看源文件frmupload.html  ,复制ConnectorServlet ,改web.xml 中为自己的Servlet
改其中doPost方法,在parseRequest(request)之前加,upload.setHeaderEncoding("UTF-8");


----文件夹名,乱码  request.getParameter("NewFolderName");改为 new String(request.getParameter("NewFolderName").getBytes("iso-8859-1"),"UTF-8");
	//应该用URIDecode吧




不能使用中文图片,修改Tomcat server.xml 中的 在8080端口 加入 URIEncoding="UTF-8" 属性,也会对 文件名乱码有影响,不推荐


源码中 改pathToSave 变量(File 类型)    中的参数filename



---上传文件类型
服务端
 connector.resourceType.file.extensions.allowed    要用| 分隔

默认配置在fckeditor-java-core-2.4.jar中的net.fckeditor.handlers.default.properties 文件
在classpath  中定义fckeditor.properties 

connector.resourceType.image.extensions.allowed=  ,来加扩展


客户端 fckconfig.js 中FCKConfig.ImageUploadAllowedExtensions   来加扩展
--文件上传大小限制
改文件源码 加一else if(uplFile.getSize()>10*1024){ ur=new UploadResponse(24)}//24  自己定义的错误码

图片上传,文件上传都要改的才行的


var oFCKeditor=new FCKEditor('FCKeditor1');  //FCKeditor1做表单名字







--------

edit 文件夹挎贝到项目中的根目录下
ckconfig.js、fckeditor.js、fckstyles.xml、fcktemplates.xml 文件也挎贝到项目中的根目录下

FCKeditor-java 中的lib 
还要一个slf4j 中的一个simple

<servlet-class>
	net.fckeditor.connector.ConnectorServlet
</servlet-class>
<load-on-startup>1</load-on-startup>

<url-pattern>/fckeditor/editor/filemanager/connectors/*</url-pattern>   <!-- */-->



<script type="text/javascript">
	function FCKeditor_OnComplete(editorInstance)  //页面(FCKeditor)加载完成
	{
		window.status = editorInstance.Description;//完成
	}
</script>
<%
		FCKeditor fckEditor = new FCKeditor(request, "EditorDefault");//另一页用这个parameterName来取字串
%>
<li><FCK:check command="CompatibleBrowser" /></li>  //显示是否启用...没试用吧..多语言可???
<li><FCK:check command="FileBrowsing" /></li>
<li><FCK:check command="FileUpload" /></li>
<%
	fckEditor.setValue("This is some <strong>sample text</strong>. You are using <a href=\"http://www.fckeditor.net\">FCKeditor</a>.");
	out.println(fckEditor);
%>




方法二 ,标签形式
<FCK:editor instanceName="EditorDefault">
	<jsp:attribute name="value">This is some <strong>sample text
		</strong>. You are using <a href="http://www.fckeditor.net">
		FCKeditor</a>.
	</jsp:attribute>
</FCK:editor>



<FCK:editor id="content"  />//传参数的名


l. 将FCKeditor目录下及子目录下所有以“_”下划线开头的文件夹删除   
2. 还可以将editor/skins目录下的皮肤文件删除，只留下default一套皮肤（如果你不需要换皮肤的话）xx\editor\skins
3. 还可以将editor/lang目录下文件删除，只保留en.js, zh-cn.js, zh.js文件（英文，简体中文，繁体中文一般应该够用了）xx\editor\lang

前台让普通用户也能使用FCKEditor，要注意相关安全问题。工具集Basic

 toolbarSet="Default"：工具集名称，即出现在页面上的工具条上的工具按钮，参考fckconfig.js，默认值是Default。



-----------------------上-FCKeditor

===============================japerreport=========================


iReport设计工具   会在用户主目录中生成一个.ireport 文件夹

option->选项->语言->
build->compile->保存文件.jrxml
			生成.java
build->jreview
build->执行报表 (右上角的按钮)
build->pdf view
option->setting->external program->pdf >选择pdf浏览器   xls 是excel 格式的
build->compile
build->执行报表

java2d 像翻书一样



ireport wizard->
data->connections/datasource
如不是mysql 要把jar包放入lib目录,再重启ireport
完成后
可以右击字段->proerty->text field->表达式是绿色表示成功,蓝色表示失败,可点按钮进行编辑


option->setting->backup->no
		compiler->default compilation directory


PDF(itext)默认是不显示中文的
下载插件,http://iextpdf.sourceforge.net/
iTextAsian.jar(ireport 中有的)
选中文字->propety->font-> 改PDF font name 为STSong-Light   .下方的PDF Encoding 改为UCS2-H (Chinese Simplified)   H代表水平   V代表垂直


TILE 只在第一页的上面显示
pageHeader 在每页上面的都有(在 Title 下)

pageFooter  (last page footer)
detail

column Hear
column footer

summary  最后一页的的下面

框红色是错误的(选中时)  黄色是正确的


build ->set connect active  是显示所有的(build->)
data ->report qurey->写 select  语句

F 图标  $F{username} 显示username 字段的数据->右击property->text field->绿是对,蓝是错,可双击下方中的


 view->feild/variables(new java.uitl.Date())
$V{name}   name是变量名  可以从左侧(Document Structure)拖过来
或者从 libaray窗口中拖 page x of y ,curentDate



$P{xx}   parameter 中有一个 选择 user a prompt   如是String 类型要加" "   运行时要你输入值 


右击 $F ->propery ->在 ..中可以按条件来显示返回 boolean (用java来写,中可$F{username}) new java.lang.oolean("".equlse($F{username}))  ,但还是会占用行的


============================================resin 配置
设置端口 
<server-default>
      <!-- The http port -->
      <http address="*" port="8080"/> 

加项目 
 <host id="" root-directory=".">  
 <web-app id="/test"   
 root-directory="你的工程webapp路径（就是你工程WEB-INF文件夹的上一级）"  
 temp-dir="你的工程webapp路径/WEB-INF/temp"/>  
 </host>  

有DataSource 例子

       - The JDBC name is java:comp/env/jdbc/test
 <database>
   <jndi-name>jdbc/mysql</jndi-name>
   <driver type="org.gjt.mm.mysql.Driver">
	 <url>jdbc:mysql://localhost:3306/test</url>
	 <user></user>
	 <password></password>
	</driver>
	<prepared-statement-cache-size>8</prepared-statement-cache-size>
	<max-connections>20</max-connections>
	<max-idle-time>30s</max-idle-time>
  </database>


logs 目录会有access.log 生成   仿问日志文件

resin 一定log4j.jar 而tomcat 只要commons-loggin.jar  就可以了



----linux 下  默认有一个空的 6800
<cluster>
	<srun   server-id= "a "   host= "127.0.0.1 "   port= "6802 "/>
	<srun   server-id= "b "   host= "127.0.0.1 "   port= "6803 "/>
</cluster>
启动文件：
/usr/local/resin/bin/httpd.sh     -pid   /usr/local/resin/a     -server   a   start 






----eclipse 信成


给httpd加上运行参数:-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=12345 ,远程调试端口一般是8453

接着打开Debug dialog
右键点击Remote Java Application->New
Connection Properties:
Host:127.0.0.1
Port:12345
点Debug即可


2：
引用
打开Debug dialog

右键点击Java Application->New

Main class:
com.caucho.server.resin.Resin

Program arguments:
-conf "D:\resin-3.1.7a\conf\resin.conf"

VM arguments:
-Dresin.home="D:\resin-3.1.7a"
-Djava.util.logging.manager=com.caucho.log.LogManagerImpl

Classpath: 中加入
Resin_Library(所有lib目录下的)
tools.jar  (JDK目录下的)
点Debug即可
 
---------------apache VFS
FileSystemManager fsManager = VFS.getManager();//OK

DefaultFileSystemManager manager = new DefaultFileSystemManager();
manager.addProvider("sftp", new SftpFileProvider());
manager.addProvider("zip", new ZipFileProvider());
manager.addProvider("file", new DefaultLocalFileProvider());
manager.setFilesCache(new DefaultFilesCache());
manager.init();//OK


FileSystemOptions opts = new FileSystemOptions();
SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
FileObject sftp = fsManager.resolveFile( "sftp://zh:lzj@10.39.101.238/",opts ); //只有sftp要opts,使用JSCH
sftp.getChildren();
sftp.getName().getURI() ;
sftp.getName().getBaseName();
if (!sftp.exists()) 
	sftp.createFolder();//对文件所在路径的目录存存会创建 

FileObject local=fsManager.resolveFile("C:/test.txt");
sftp.copyFrom(local, Selectors.SELECT_FILES);//upload
local.copyFrom(sftp, Selectors.SELECT_FILES);//download

-----------------------------Java Web Start
javaws -viewer
javaws -offline

<a href="aa.jnlp">aa</a>  

aa.jnlp文件
<?xml version="1.0" encoding="UTF-8"?> 
<jnlp codebase="file:///c:/"> 
    <information> 
	<title>HelloWorld</title> 
        <vendor>AA Corporation</vendor> 
	<description>HelloWorld Test Example for WebStart.</description> 
	<homepage href="http://127.0.0.1:8081/webstart/index.html"/> 

        <!--icon 只支持GIF/JPEG格式，其它格式无效--> 
        <icon href="./winxp.gif"/> 
        <icon kind="splash" href="./music.bmp"/> 

        <!-- 允许离线启动，可以使用javaws -offline命令--> 
        <offline-allowed/> 
    </information> 
    <resources> 
	 <j2se version="1.5+"/> 
        <jar href="./helloworld.jar"/> 
    </resources> 
    <application-desc main-class="jws.HelloWorld"/>   
			<!-- 是一个可以启动 JFrame 的main类-->
</jnlp> 


----------------------OSCache 缓存 JSP
值得注意的是OSCache的filter应该放在struts或webwork 的Action的前面,这样用户触发一个url地址就不会经过框架语言,才会起到完全缓存的效果


<filter>
<filter-name>osCache</filter-name>
<filter-class>com.opensymphony.oscache.web.filter.CacheFilter</filter-class>
<init-param>
	  <param-name>time</param-name> 
	  <param-value>3600</param-value>
   </init-param>
   <init-param>
	  <param-name>scope</param-name>
	  <param-value>application</param-value>
   </init-param>
</filter>

<filter-mapping>
  <filter-name>osCache</filter-name>
  <url-pattern>/query/defaultQuery.action</url-pattern>
</filter-mapping>


tomcat下来进行配置时,<url-pattern>/query/defaultQuery.action</url- pattern>中不支持" * ",
resin 就可以这样来写,<url-pattern>/query/ *.action</url-pattern>

只要一个版本的 oscache

oscache.properties 文件 放入CLASSPATH 下

cache.memory  //值为true 或 false ，默认为true在内存中作缓存，


cache.persistence.class= DiskPersistenceListener //打开后,一定要加 cache.path 
cache.path  //磁盘缓存，windows \\ ,unix /




<cache:cache time="30">
        每30秒刷新缓存一次的日期: <%= new Date() %> 
 </cache:cache>

 <cache:cache key="testcache">
          手动刷新缓存的日期: <%= new Date() %> <p>
</cache:cache>
<a href="cache2.jsp">手动刷新</a>


cache2.jsp中
<cache:flush key="testcache" scope="application"/>

	一定要有scope 
------------------------




===============================eclipse 报表  BIRT =========================

设计生成的　.rptdesign　文件在 report design 的Perspective中右击.rptdesign　->report-> run report

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventadapter.DataItemEventAdapter;
import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
public class MyEvent extends DataItemEventAdapter
{
	@Override
	public void onRender(IDataItemInstance data, IReportContext reportContext) throws ScriptException
	{
		super.onRender(data, reportContext);
		if(((Double)data.getValue()).doubleValue()>1000)
		{
			data.getStyle().setColor("red");
		}
	}
}

plugins/org.eclipse.birt.core_2.x.jar　
plugins/org.eclipse.birt.report.engeen_2.x.jar

加断点
debug configuration...->新建一个report ->单择 Debug type为java,复选Open generated file when finished
			classpath标签->Add Projects...选择自己的项目，debug,会启动javaw

layout视图选中元素后->在 Property Editor->Event Handler->选择自己的类 extends DataItemEventAdapter 


eclipse 自带的 property editor 有更多的属性调整边框

可以右击layout视图中的组件->style->new style(CSS)
	outline窗口中可以双击修改style，也可右击



Char ->filter 中可以top n


runtime/org.eclipse.emf.common_2.6.jar
plugins/org.eclipse.birt.chart.engine_2.6.0.jar
plugins/org.eclipse.birt.core_2.6.0.jar  //--
plugins/org.eclipse.birt.report.engine_2.6.0.jar//--
plugins/org.eclipse.emf.ecore_2.6.0.jar
plugins/org.eclipse.birt.report.model_2.6.0.jar


<listener>
		<listener-class>
			org.eclipse.birt.chart.viewer.internal.listener.ChartServletContextListener
		</listener-class>
</listener>
<listener>
	<listener-class>
		org.eclipse.birt.chart.viewer.internal.listener.ChartHttpSessionListener
	</listener-class>
</listener>
	

WebViewerExample/WEB-INF/tlds中有birt.tld
birt.war中

---jsp==
Chart myLineChart = (Chart)request.getAttribute("myLineChart");

<chart:renderChart width="800" height="300" model="<%=myLineChart%>"></chart:renderChart>



扩展Birt ,eclipse插件开发  聚合函数  aggregation(聚合),accumulate(加)

新建eclipse-plugin项目
MANIFEST.MF界面的 dependencies标签->删所有,Add...->org.eclipse.birt.data
				Extensions标签->Add... ->org.eclipse.birt.data.aggregation
		->右击生成的(Aggregation)->new ->aggreation->输入名字OptimistcSum,和类名

点击顶部->给ID和name
类名 extends org.eclipse.birt.data.engine.api.aggregation.Aggregation//过时的

new org.eclipse.birt.data.engine.api.aggregation.Accumulator()
{
	public Object getValue() //最后的聚合结果
	{
		return null;
	}

	public void onRow(Object[] arg0) //每一行把参数传进来
	{
		
	}

}

右击自己的生成->new ->UIInfo ,是工具提示信息


=======================JBOSS 6 和4.2 使用 
run.bat -c default     (configuration)
shutdown.bat -S			
命令启动会使用JBOSS_HOME 环境变量

默认端口是 8080


最大启动时间修改
E:\eclipseEE\plugins\org.eclipse.jst.server.generic.jboss_1.x中修改文件
plugin.xml文件
所有的JOBSS版本中的startTimeout="50000"  加大,就可以启动了


.jar包发布到 server/all/deploy目录 中

Context ct=new InitialConetxt();
Hello h=(Hello)ct.lookup("HelloBean/remote")  //.jar包，remote表示远程调用
//如是.ear包   是  EAR包名/类名/remote

在JBOSS服务中->JMX控制台->jboss.j2ee->下有刚部署的.jar
有JNDI 的名字(HelloBean)

看是否发布成功JBOSS->service=JNDIView->list->invoke->Global JNDI Namespace下可以看到

ANT 中 <property envirentment="env"/>
	后可用类似 ${env.JBOSS_HOME}
${ant.project.name}  是<project name="xx"
 




%JBOSS_HOME%\server\default\deploy\properties-service.xml
		<attribute name="Properties">
		user.dir=C:/my
		</attribute>


端口号查看可以在　http://localhost:8080/jmx-console/　-> jboss　-> service=Naming ->
					看JNDI端口是1099,RMI端口是1098
------配置数据源---　测试OK

	从%JBOSS_HOME%\docs\examples\jca中复制文件oracle-ds.xml或oracle-xa-ds.xml
	（如果要用事务数据源，复制oracle-ds.xml）到%JBOSS_HOME%\server\default\deploy中

	修改在 <jndi-name>的下一行加入	<use-java-context>false</use-java-context>　
	<jndi-name>OracleDS</jndi-name>
	<use-java-context>false</use-java-context>,如代码不在容器内返回javax.naming.Reference
	
	如果为true是要加java:/ 才行,context.lookup("java:/OracleDS"),要求代码要在JBOSS容器内(如Servlet,JSP)才可以得到
	启动时有日志

	%JBOSS_HOME%\server\default\conf默认有一个jndi.properties文件,不用修改,可以用作复制
	Properties props=new Properties();
	props.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");//类在client\jnp-client.jar中
	props.put(Context.PROVIDER_URL,"localhost:1099");//或者用   jnp://localhost:1099
	new InitialConetxt(props);
	context.lookup("OracleDS");
	
	//必须初始化 log4j,把ojdbc6.jar放在\server\default\lib目录下

	//加jar包时选择JOBSS安装目录的client文件夹下的所有的jar包

JBOSS 6版本有 http://localhost:8080/admin-console/  初始用户名 密码 是admin/admin
可以配置DataSource ,JMS,及总署
户名密码配置文件是在%JBOSS_HOME%\server\default\conf\props下的jmx-console-users.properties文件。



---对JOBSS-4.2的配置
	%JBOSS_HOME%\server\default\deploy\jboss-web.deployer\META-INF\jboss-service.xml
		<attribute name="UseJBossWebLoader">false</attribute>修改成true


	修改IIOP默认端口1099
	%JBOSS_HOME%\server\default\conf\jboss-service.xml 文件中找到
		<mbean code="org.jboss.naming.NamingService"
			<attribute name="Port">1099</attribute>

	%JBOSS_HOME%\server\default\deploy\jboss-web.deployer\server.xml
	<Connector port="8888"	更改监听端口

E:\jboss-5.0.0.GA\server\all\deploy\jbossweb.sar\server.xml 中改8080




======================JFreeChart=============================
jcommon-1.0.2.jar
jfreechart-1.0.9.jar

标题	==	title
主图叫	==	Plot
下方说明==	legend
//------柱状图 中文支持
Font font = new Font("SimSun",Font.BOLD|Font.ITALIC,20); 
TextTitle tt = chart.getTitle(); 
tt.setFont(font); 
chart.getLegend().setItemFont(font); 

CategoryPlot plot = (CategoryPlot)chart.getPlot();
CategoryAxis domainAxis = plot.getDomainAxis();
domainAxis.setTickLabelFont(font);
domainAxis.setLabelFont(font);
NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
numberaxis.setTickLabelFont(font);
numberaxis.setLabelFont(font);
//------
//------饼图中文支持
Font font = new Font("SimSun",Font.BOLD|Font.ITALIC,20); 
TextTitle tt = chart.getTitle(); 
tt.setFont(font); 
chart.getLegend().setItemFont(font); 
PiePlot pieplot = (PiePlot)chart.getPlot();
pieplot.setLabelFont(font);  
//------

ChartFrame frame=new ChartFrame("公司人员",chart); //图片显示在Swing窗口中
frame.pack();
frame.setVisible(true);

---集成到web中
web.xml
 <servlet>
    <servlet-name>DisplayChart</servlet-name>
    <servlet-class>org.jfree.chart.servlet.DisplayChart</servlet-class>
 </servlet>
 <servlet-mapping>
     <servlet-name>DisplayChart</servlet-name>
     <url-pattern>/servlet/DisplayChart</url-pattern>
 </servlet-mapping>

 
String imgFilename=ServletUtilities.saveChartAsJPEG(chart, 800, 600, session);
String url=request.getContextPath()+"/servlet/DisplayChart?filename="+imgFilename;

<img src="<%=url%>" width="800" height="600"/>
---


DefaultCatagoryDataset(柱状图)
	setValue(double value,)

ChartFactory.createBarChart()
CategoryPlot plot =(CategoryPlot)  FreeChart.getPlot();
CatagoryAxis categoryAxis=plot.getDomainAxis();横坐标(柱状图)
categoryAxis.setCategoryLablePositions(CategoryLablePosition.UP_45)标签文件转 45度
		
NumberAxis=(NumberAxis)plot.getRangeAxis();数字坐标(柱状图)



ApplicationFrame("")
ChartPanel(JFreeChart) 是一个Panel;

LegendTitle xx= chart.getLegend(0);;


ChartUitlities.writeChartAsJPEG(OutputStream,chart,100,100);//输出图片



不推荐用 //Web 中
ServletUitlities.saveChartAsPNG();返回文件名字
	//保存在临时目录下(Tomcat的temp目录下)服务器上的文件会很多
 
org.jfree.chart.ChartFactory
org.jfree.chart.JFreeChart 的方法  Plot getPlot()  
org.jfree.chart.plot.Plot
org.jfree.data.general.DefaultPieDataset 的setValue("key",double xx);也可封装类


plot.setNoDataMessage("No data available");
jfreechart.setTitle("www.SenTom.net 网站访问统计表");
AbstractRenderer

org.jfree.chart.ChartPanel继承自JPanel
org.jfree.chart.ChartFrame继承自JFrame


JFreeChart createPieChart(java.lang.String title,
                                        PieDataset dataset,
                                        boolean legend, 下方的小的说明图例是否显示
                                        boolean tooltips,
                                        boolean urls)

JFreeChart chart1=ChartFactory.createBarChart3D("柱图","categoryAxisLabel","valueAxisLabel",   DefaultCategoryDataset ,PlotOrientation.VERTICAL,false,false,false);
		

JFreeChart chart1=ChartFactory.createBarChart3D(...
		CategoryPlot plot=chart1.getCategoryPlot();
		BarRenderer renderer1 = (BarRenderer) plot.getRenderer();// CategoryItemRenderer(Interface)

DatasetUtilities.createCategoryDataset(rowKeys[], columnKeys[], data);
ChartUtilities.writeChartAsJPEG(new FileOutputStream("D:\\chart1.jpg"), 0.9f, chart1, 400, 300, null);
ServletUtilities.saveChartAsJPEG(chart1,300,200,session);//HttpSession



(BarRenderer) plot.getRenderer();
BarRender.setItemMargin() 要addChangeListener(RendererChangeListener listener) 只一个方法rendererChanged(RendererChangeEvent event)




如要用%>做字串要用%/>来代替
<jsp:include 再有请求时才会引用指定的页面，而，<%@page include%>是

JavaBean
bound属性
PropertyChangeSupport(Object sourceBean) 
 	addPropertyChangeListener(PropertyChangeListener listener) 
removePropertyChangeListener(PropertyChangeListener listener) 
 	void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)  
constraint属性
	属性有没有约束，变化前可以阻止改变PropertyVetoException
	VetoChangeSupport
fireVetoableChange(String propertyName, Object oldValue, Object newValue) 
	

javax.servlet.jsp.tagext.TagExtraInfo

javax.servlet.jsp.tagext.VariableInfo
