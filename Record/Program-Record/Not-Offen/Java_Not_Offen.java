
---------------------------------Mina 
Apache 项目基于java nio

sample中的 gettingstarted　是服务端

事件驱动
Handler 中处理响应事件
Filter,FilterChain //日志，压缩，数据转换，黑名单

Service
	Connector  客户
	Acceptor 服务


//服务端
IoAcceptor acceptor = new NioSocketAcceptor(); //UDP 用 NioDatagramAcceptor

acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
//acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
//acceptor.getFilterChain().addLast(  "codec", new ProtocolCodecFilter(  new SumUpProtocolCodecFactory(true)));//是自定义类  extends DemuxingProtocolCodecFactory 
 
acceptor.setHandler( new TimeServerHandler() );//自己的回调
acceptor.getSessionConfig().setReadBufferSize( 2048 );
acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );//空闲10秒后调用Handler sessionIdle方法 
acceptor.bind( new InetSocketAddress(9123));


//客户端
NioSocketConnector connector = new NioSocketConnector();// UDP 用 NioDatagramConnector
 connector.setConnectTimeoutMillis(3000);
   
//connector.getFilterChain().addLast("black",  new BlacklistFilter());
connector.getFilterChain().addLast("logger", new LoggingFilter());
//connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
//connector.getFilterChain().addLast(  "codec", new ProtocolCodecFilter(  new SumUpProtocolCodecFactory(false)));//是自定义类  extends DemuxingProtocolCodecFactory 
 
connector.setHandler(new ClientSessionHandler());//自己的回调
  
ConnectFuture future = connector.connect(new InetSocketAddress(9123));
future.addListener( new IoFutureListener<ConnectFuture>()
		{
            public void operationComplete(ConnectFuture future) //会第一次执行
            {
                if( future.isConnected() )
                {
                	IoSession session =future.getSession();
                    IoBuffer buffer = IoBuffer.allocate(8);
                    //buffer.putLong(85);
                    buffer.putChar('L');
                    buffer.putChar('E');
                    buffer.putChar('N');
                    buffer.flip();
                    session.write(buffer);//另一端不会立即收到请求，要在外部的session.write才写
                } else {
                   System.out.println("Not connected...exiting");
                }
            }
        });
future.awaitUninterruptibly();
IoSession session = future.getSession();
session.setAttribute(sessionCountKey,0);//session只可在自己这一端可以仿问
WriteFuture write= session.write("客户端第二次写Header");//这里才开始写ConnectFuture中 Listener的session.write 再和这里 一起到服务端的
write.addListener(new IoFutureListener<IoFuture>() 
{
	@Override
	public void operationComplete(IoFuture fure) {
		System.out.println("客户端第二次写Header完成");
	}
}) ;
 
session.getCloseFuture().awaitUninterruptibly();//阻， 另一端关闭时这里关闭
connector.dispose();

class ClientSessionHandler extends IoHandlerAdapter
{
	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	{
	    cause.printStackTrace();
	}
	public void messageReceived( IoSession session, Object message ) throws Exception
	{
	    int count=Integer.parseInt(session.getAttribute(MinaClient.sessionCountKey).toString());
		session.setAttribute(MinaClient.sessionCountKey , ++count);
		if(count > 3)
		{
			session.write("quit");
			return;
		}else
		{
			 String str = message.toString();
		    System.out.println("客户端 receive is:"+str);
		    session.write("hello 你好！");
		    System.out.println("客户端已经写了hello");
		}
	}
	public void messageSent(IoSession session, Object message) throws Exception {//调用了write方法调用这个,不一定发送
		 System.out.println("messageSent: session="+session.getId()+",message="+message);
	}
	public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	{
	    System.out.println( "IDLE " + session.getIdleCount( status ));
	}
}
public class SumUpProtocolCodecFactory extends DemuxingProtocolCodecFactory {
   public SumUpProtocolCodecFactory(boolean server) 
   {
	   if (server) {
		  super.addMessageDecoder(AddMessageDecoder.class);//implements MessageDecoder
	      super.addMessageEncoder(ResultMessage.class, ResultMessageEncoder.class);//自己的类T Serializable , implements MessageEncoder<T>
	  }
   }
}

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





==================================Rhino   JS可以运行在JAVA中
D:\Program\java_lib>java -cp js.jar org.mozilla.javascript.tools.shell.Main
Rhino 1.7 release 2 2009 03 22
js>



//对表示式求值 rhino 
Context cx = Context.enter();   
try  
{   
  Scriptable scope = cx.initStandardObjects();   
  String str = "9*(1+2)";   
  Object result = cx.evaluateString(scope, str, null, 1, null);   
  double res = Context.toNumber(result);   
  System.out.println(res);   
}   
finally  
{   
  Context.exit();   
}   


js>load('c:/temp/test.js');  
js>load('c:\\temp\\test.js');
js>add(1,2);
function add (a,b)   
{   
 return a+b;
}


java -cp js.jar org.mozilla.javascript.tools.debugger.Main   就可以看到调试器的界面了。


js文件运行的速度，可以把它编译为class文件：
java -cp js.jar org.mozilla.javascript.tools.jsc.Main c:/temp/test.js


var swingNames = JavaImporter();
swingNames.importPackage(Packages.javax.swing);
function createComponents() 
{
    with (swingNames) 
	{
		new JLabel("");
//或者用完整java 包名

System.getProperty("user.dir") //是当前目录


Context cx = Context.enter()
 Scriptable scope=cx.initStandardObjects();
Object result = cx.evaluateString(scope, jsContent, filename, 1, null);//1 是 lineno,从第几行开始执行吗?


Scriptable global=null;
ContextFactory.getGlobal().call(new ContextAction()
{
	public Object run(Context cx)
	{
		global = new ImporterTopLevel(cx);
		Scriptable wrapped = Context.toObject(beanObject, global);
		global.put("DV", global, wrapped); //JS中用DV 来表示beanObject对象,即DV.getXX() 就是 beanObject.getXX()
		return null;
	}
});


ContextFactory.getGlobal().call(new ContextAction()
{
	public Object run(Context cx)
	{
		return cx.evaluateString(global, jsContent, null, 0, null);
	}
});

 

=====================CORBA  JDK9 remove=====================
CORBA（Common Object Request Broker Architecture）是为了实现分布式计算
只有CORBA是真正跨平台的，RMI 只能用Java
它通过一种叫IDL（Interface Definition Language）的接口定义语言，能做到语言无关，

客户方叫IDL Stub（桩）, 在服务器方叫IDL Skeleton（骨架）    ,由IDL 编译器生成
双方又要通过而ORB（Object Request Broker，对象请求代理）总线通信

ORB还要负责将调用的名字、参数等编码成标准的方式(称Marshaling)  传输  ,Unmarshaling,这整个过程叫重定向，Redirecting

IIOP（Internet Inter-ORB Protocol）


http://www.omg.org/spec/CORBA/    CORBA-3.2 	November 2011
Object Management Group (OMG)

orbacus 是corba-2.6 的开源实现,支持C++/Java 
http://web.progress.com/en/orbacus/documentation_432.html  orbacus doc


即Web浏览器通过下载Java Applet形式的CORBA客户方程序
标志产品Orbix是一个基于库的CORBA规范实现,又推出了Orbix的Java版本OrbixWeb



hello.idl文件内容
module corba
{
module helloApp
{
  interface Hello
  {
    string sayHello();
    oneway void shutdown();
  };
 }; 
};
//idlj -fall Hello.idl


idlj hello.idl 相当于 idlj -fclient hello.idl 
idlj -fclient -fserver hello.idl
idlj -fall hello.idl
idlj -fallTIE Hello.idl 如果使用这个,会多生成会一个HelloPOATie.java文件,只在写Server类时有点不一样

//idlj命令后会生成corba/helloApp目录
//client Stub:		_HelloStub.java,HelloHelper.java,HelloHolder.java,HelloOperations.java
//server Skeleton: Hello.java,HelloPOA.java						  	,HelloOperations.java,

Portable Object Adapter (POA)

//HelloOperations->Hello->_HelloStub
//HelloOperations->HelloPOA
父->子

import org.omg.CosNaming.NameComponent;
Common Object Services (COS) 


手工写 实现类 
class HelloImpl extends HelloPOA {
	private ORB orb;
	public void setORB(ORB orb_val) {
	    orb = orb_val; 
	  }
	  public String sayHello() {
	    return "\nHello world !!\n";
	  }
	  public void shutdown() {
	    orb.shutdown(false);//服务端退出,为客户端调用
	  }
	}
手工写Server类
ORB orb = ORB.init(args, null);
POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
rootpoa.the_POAManager().activate();
HelloImpl helloImpl = new HelloImpl();
helloImpl.setORB(orb); 

//--使用idlj -fall Hello.idl对应的方法
//org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
//Hello href = HelloHelper.narrow(ref);
//--使用idlj -fallTIE Hello.idl 对应的方法
HelloPOATie tie = new HelloPOATie(helloImpl, rootpoa);
Hello href = tie._this(orb);
//-- 
org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");//对于使用orbd也可以用"TNameService"表示是Transient
NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
String name = "Hello";
NameComponent path[] = ncRef.to_name( name );
ncRef.rebind(path, href);
orb.run();//会一直阻塞,除非调用  orb.shutdown(

手工写Client类
ORB orb = ORB.init(args, null);
org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");//对于使用orbd也可以用"TNameService"表示是Transient
NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
String name = "Hello";
helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
System.out.println(helloImpl.sayHello());//会调用_HelloStub的sayHello方法,中有_invoke方法 -> 会调用服务端的HelloPOA的_invoke方法,中有调用自己实现的方法(HelloPOATie)
helloImpl.shutdown();//客户端调用服务端退出

orbd -ORBInitialPort 11050 -ORBInitialHost localhost //启动ORBA 服务器,会生成orb.db目录
java HelloServer -ORBInitialPort 11050 -ORBInitialHost localhost 
java HelloClient -ORBInitialPort 11050 -ORBInitialHost localhost
//测试OK
也可使用  tnameserv -ORBInitialPort 11050
orbd 命令 Transient Naming Service 和 a Persistent Naming Service,
tnameserv 命令 (Transient Naming Service)  
 
服务端的另一种方式Tie,使用idlj -fallTIE Hello.idl
 
 
 
 
 
 
module simpleDemo
{
	interface grid 
	{
		 readonly attribute short height; 
		 readonly attribute short width; 
		 void set(in short row, in short col, in long value);
		 long get(in short row, in short col); 
	};
};


Dynamic Invocation Interface (DII) 和 Dynamic Skeleton Interface (DSI)
(DII)和（DSI）是用来支持客户在不知道服务器对象的接口的情况下也能调用服务器对象。
Basic Object Adapter(BOA, 基本对象适配器)
Portable Object Adapter（POA，可移植对象适配器）  ,最新的ORB产品一般都支持POA
Server方的实现对象称为Servant

	自己写类gridImpl extends  gridPOA

自己写Server类
org.omg.CORBA.ORB			global_orb	= ORB.init (args,null); //1.init

//2.
org.omg.CORBA.Object		poa_obj		= global_orb.resolve_initial_references("RootPOA");
org.omg.PortableServer.POA	root_poa	= org.omg.PortableServer.POAHelper.narrow(poa_obj);
byte[] grid_oid = root_poa.activate_object(grid);				//自已的POA实现类
org.omg.CORBA.Object ref = root_poa.create_reference_with_id(grid_oid, gridHelper.id());
String stringified_ref = global_orb.object_to_string(ref);//保存引用,
//2
org.omg.PortableServer.POAManager poa_manager = root_poa.the_POAManager();
poa_manager.activate(); //3.
global_orb.run();//4

真正跨机器、跨平台的分布式应用中
通常使用Naming Servic，	要启动Naming Service守护进程??????????????

使用file based  ,Client和Server在同一台机器上时才是可行的


//cleint
ORB orb=ORB.init (args,null);
org.omg.CORBA.Object obj_ref=orb.string_to_object(String ...); 
								string_to_object("relfile:/Hello.ref");//指定文件名中的,当前目录下
grid gridProxy =gridHelper.narrow (obj_ref);//就可使用了
				 gridHelper.narrow(obj_ref);//多次也是同一个Servant对象,后面会覆盖前面的,不建议出多个

....
orb.shutdown(true);		//


IDL 可以不定义 Module 使用jidl命令生成代码,CORBA开源产品 ORBacus-4.3.4
jidl  --package hello  Hello.idl　　

生成的POA类的_this方法 ,生成接口


ORBacus 能存储为HTML文件. 这通常用在Client 是一个Java Applet的情况下???????????

// Server and Client
java.util.Properties props = System.getProperties();
props.put("org.omg.CORBA.ORBClass", "com.ooc.CORBA.ORB");//OB.jar
props.put("org.omg.CORBA.ORBSingletonClass","com.ooc.CORBA.ORBSingleton");

orb = org.omg.CORBA.ORB.init(args, props);
((com.ooc.CORBA.ORB)orb).destroy();


启动Server时  java -Xbootclasspath/p:%CLASSPATH%  hello.Server   // /p=prepend 在开始处加 /a=append

IDL 语法
	数据类型
short
unsigned short
long
unsigned long
long long
unsigned long long
float
double
long double
char
wchar
string
boolean
octet
any


摒弃int 类型在不同平台上取值范围不同带来的多义性的问题。
IDL提供2 字节 (short)、 4 字节 (long) 和 8 字节 (long long) 的整数类型。

boolean 值只能是 TRUE 或 FALSE。

octet 是 8 位类型 ,octet 在地址空间之间传送时不会有任何表示更改

any	类似于C++ 的自我描述数据类型void *


typedef
enum 
struct  
union 

识别联合
enum PressureScale{customary,metric};
 
union BarometricPressure switch (PressureScale) { //short、long、long long , char、boolean , enumeraton
 case customary :
    float Inches; //可以是任何类型
 case metric :
 default:
    short CCs;
};

常数 const  不能是 any 类型或用户定义的类型,不能有混合的类型表达式,可以 0xff

用户异常
exception DIVIDE_BY_ZERO {
 string err;
};
 
interface someIface {
 long div(in long x, in long y) raises(DIVIDE_BY_ZERO);
};

数组 ,typedef long shares[1000];//不支持[] 中无数字
string 类型是一种特殊的序列

struct ofArrays {
 long anArray[1000];
}; 
必须出现 typedef 关键字，除非指定的数组是结构的一部分
下标从 1 开始,数组下标,不能动态修改下标
typedef sequence<long> Unbounded;
typedef sequence<long, 31> Bounded;

wstring
module States { 
	//不能加属性,方法
 module Pennsylvania {  //可以嵌套,

}
}

JOB-4.3.4\ob\demo\echo 和hello  示例



======================ActiveMQ   JMS
<dependency>
	 <groupId>org.apache.activemq</groupId>
	 <artifactId>activemq-core</artifactId>
	 <version>5.7.0</version>
 </dependency>

ActiveMQ是一个JMS Provider的实现,tomcat 使用JMS 

JMeter做性能测试的文档
http://activemq.apache.org/jmeter-performance-tests.html

启动ActiveMQ服务器 bin\activemq.bat start  stop

http://localhost:8161/admin    admin/admin (配置在/conf/jetty-realm.properties)  可以看有,创建Queue ,Topic,Durable Topic Subscribers
http://localhost:8161/camel
http://localhost:8161/demo
端口配置在jetty.xml中

启动日志中有　tcp://lizhaojin:61616　    端口配置在activemq.xml中

日志中提示 JMX URL: service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi


看log4j.properties日志在data目录中

方法2（在JVM中嵌套启动）：
cd example

ant embedBroker

ant consumer
ant producer

ant topic-listener
ant topic-publisher



----集成web项目------启动OK
activemq-all-5.4.2.jar
activemq-web-5.4.2.jar

web.xml中
 <context-param>  
	<param-name>brokerURI</param-name>  
	<param-value>/WEB-INF/activemq.xml</param-value>  
 </context-param>  
 <listener>  
	<listener-class>org.apache.activemq.web.SpringBrokerContextListener</listener-class>  
 </listener>  


activemq.xml
 <beans  
   xmlns="http://www.springframework.org/schema/beans"  
   xmlns:amq="http://activemq.apache.org/schema/core"  
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
   xsi:schemaLocation="
   http://www.springframework.org/schema/beans 
   http://www.springframework.org/schema/beans/spring-beans-2.0.xsd  
   http://activemq.apache.org/schema/core 
   http://activemq.apache.org/schema/core/activemq-core-5.2.0.xsd     
   http://activemq.apache.org/camel/schema/spring 
   http://activemq.apache.org/camel/schema/spring/camel-spring.xsd">  
   
    <bean id="oracle-ds" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">  
       <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"/>  
       <property name="url" value="jdbc:oracle:thin:@localhost:1521:xe"/>  
       <property name="username" value="hr"/>  
       <property name="password" value="hr"/>  
       <property name="maxActive" value="20"/>  
       <property name="poolPreparedStatements" value="true"/>  
     </bean>  
   
     <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost">  
         
      
     </broker>  
   
 </beans>  

---集成Tomcat------------


<Resource
    name="jms/FailoverConnectionFactory"
    auth="Container"
    type="org.apache.activemq.ActiveMQConnectionFactory"
    description="JMS Connection Factory"
    factory="org.apache.activemq.jndi.JNDIReferenceFactory"
    brokerURL="failover:(tcp://localhost:61616)?initialReconnectDelay=100&amp;maxReconnectAttempts=5"
   brokerName="localhost"
    useEmbeddedBroker="false"/>

<Resource name="jms/topic/MyTopic"
    auth="Container"
    type="org.apache.activemq.command.ActiveMQTopic"
    factory="org.apache.activemq.jndi.JNDIReferenceFactory"
    physicalName="MY.TEST.FOO"/>
   
failover transport是一种重新连接机制，用于建立可靠的传输。
此处配置的是一旦ActiveMQ broker中断，Listener端将每隔100ms自动尝试连接，直至成功连接或重试5次连接失败为止。
 

---集成 Spring Tomcat------------OK
只 activemq-all-5.3.2.jar 放/WEB-INF/lib

Tomcat目录下的conf/context.xml

<Resource name="jms/ConnectionFactory"   
  auth="Container"     
  type="org.apache.activemq.ActiveMQConnectionFactory"   
  description="JMS Connection Factory"  
  factory="org.apache.activemq.jndi.JNDIReferenceFactory"   
  brokerURL="vm://localhost"   
  brokerName="LocalActiveMQBroker"/>  
   
<Resource name="jms/Queue"   
auth="Container"   
type="org.apache.activemq.command.ActiveMQQueue"  
description="my Queue"  
factory="org.apache.activemq.jndi.JNDIReferenceFactory"   
physicalName="FOO.BAR"/>  


spring.xml
 <bean id="jmsConnectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean">  
         <property name="jndiName" value="java:comp/env/jms/ConnectionFactory"></property>  
 </bean>  
 <bean id="jmsQueue" class="org.springframework.jndi.JndiObjectFactoryBean">  
	 <property name="jndiName" value="java:comp/env/jms/Queue"></property>  
 </bean>  
 <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">  
	 <property name="connectionFactory" ref="jmsConnectionFactory"></property>  
	 <property name="defaultDestination" ref="jmsQueue"></property>  
 </bean>  

 <bean id="sender" class="activemq_web.Sender">  
	 <property name="jmsTemplate" ref="jmsTemplate"></property>  
 </bean>  

 <bean id="receive" class="activemq_web.Receiver"></bean>  
 <bean id="listenerContainer"  class="org.springframework.jms.listener.DefaultMessageListenerContainer">  
	 <property name="connectionFactory" ref="jmsConnectionFactory"></property>  
	 <property name="destination" ref="jmsQueue"></property>  
	 <property name="messageListener" ref="receive"></property>  
 </bean>  
-----使用Spring标签
<jee:jndi-lookup id="jmsConnectionFactory" jndi-name="java:comp/env/jms/ConnectionFactory" />
<jee:jndi-lookup id="jmsQueue" jndi-name="java:comp/env/jms/Queue" />

<bean id="receive" class="activemq_web.ReceiverListener"></bean>
<jms:listener-container connection-factory="jmsConnectionFactory">
	<jms:listener destination="jmsQueue" ref="receive"/>
</jms:listener-container>
	
如使用ActiveMQ 
<bean id="jmsConnectionFactory" class="org.apache.activemq.pool.PooledConnectionFactory" destroy-method="stop">
    <property name="connectionFactory">
      <bean class="org.apache.activemq.ActiveMQConnectionFactory">
        <property name="brokerURL">
          <value>tcp://localhost:61616</value>
        </property>
      </bean>
	 
    </property>
  </bean>
  
<!--    也可以用  
    <bean id="jmsConnectionFactory2" class="org.springframework.jms.connection.SingleConnectionFactory">
        <property name="targetConnectionFactory" >
		    <bean  class="org.apache.activemq.ActiveMQConnectionFactory">
				<property name="brokerURL" value="tcp://localhost:61616" />
				<property name="userName" value="#{jms['mq.username']}" />
				<property name="password" value="#{jms['mq.password']}" />
				<property name="sendTimeout" value="10000" />  <!-- 如果不设置,会一直卡住好多个小时 -->
		    </bean>
        </property>
		
    </bean>
  -->  


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Sender
{
	private JmsTemplate jmsTemplate;
	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}
	public void send(final String text)
	{
		
		 
		System.out.println("---Send:" + text);
		jmsTemplate.send(new MessageCreator()
		{
			public Message createMessage(Session session) throws JMSException
			{
				return session.createTextMessage(text);
			}
		});
		
		 Map<String,Object> msg=new HashMap<String,Object> ();
		 msg.put("isSuccess", "true");
		 jmsTemplate.convertAndSend(msg);
		 
	}
}
//--
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.MapMessage;

public class Receiver implements MessageListener
{
	public void onMessage(Message message)
	{
		try
		{
			if (message instanceof TextMessage)
			{
				TextMessage text = (TextMessage) message;
				System.out.println("Receive:" + text.getText());
				
			}else if (message instanceof MapMessage)
			{
				MapMessage mapMsg=(MapMessage)message;
				System.out.println(" Receive Map Names is:"+ mapMsg.getMapNames()); 
			}
		} catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
}
ApplicationContext ctx = new ClassPathXmlApplicationContext("spring_jms_beans.xml");
JSP中
<%
ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletConfig().getServletContext());
Sender send=(Sender)ctx.getBean("sender");
send.send("hello");
%>
//------------------------------ OK
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
public class MainApp 
{
	public static void main(String[] args) throws Exception
	{
		// apache-activemq-5.11.1\bin\activemq.bat start 来启动
		//ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;  //failover://tcp://localhost:61616
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = factory.createConnection();
		connection.start();
		//在容器中,一个connection只能创建一个活的session,否则异常
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//boolean transacted, int acknowledgeMode　
		//对不在JTA事务中(如在JTA事务中,参数失效,commit,rollback,也失败,依赖于JTA事务),如transacted为true使用session.rollback();或 session.commit();   acknowledgeMode参数被忽略
		Topic topic= new ActiveMQTopic("testTopic");//动态建立 , 也可使用new ActiveMQQueue("testQueue")
		//Topic topic= session.createTopic("testTopic");
		// queue=session.createQueue("testQueue");
		MessageConsumer comsumer1 = session.createConsumer(topic);
		comsumer1.setMessageListener(new MessageListener()
		{
			public void onMessage(Message m) {
				try {
					System.out.println("Consumer1 get " + ((TextMessage)m).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//创建一个生产者，然后发送多个消息。
		MessageProducer producer = session.createProducer(topic);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		for(int i=0; i<10; i++)
		{
			producer.send(session.createTextMessage("Message:" + i));
		}
		producer.close();
	}
}
============ActiveMQ 的集群

	  
activemq5.9.0 开始 , activemq的集群实现方式取消了传统的Master-Slave方式 , 增加了基于 zookeeper + leveldb 的实现方式
http://activemq.apache.org/replicated-leveldb-store.html

activemq.xml
brokerName属性设置为统一的
<broker brokerName="broker" ... >
  ...
 <persistenceAdapter>
    <replicatedLevelDB
      directory="${activemq.data}/leveldb"
      replicas="3"
      bind="tcp://0.0.0.0:0"
      zkAddress="my-pc:2181,192.168.2.145:2181,192.168.2.146:2181"
      hostname="my-pc"
      sync="local_disk"
      zkPath="/activemq/leveldb-stores"
      />
</persistenceAdapter>
  ...
</broker>
hostname属性值配置本机的值
 
 
 
客户端使用
<bean class="org.apache.activemq.ActiveMQConnectionFactory">
	<property name="brokerURL">
	  <value>failover:(tcp://localhost:61616,tcp://otherIP:61616)</value>
	  <property name="userName" value="hrbb" />
	 <property name="password" value="hrbb" />
	</property>
</bean>

activemq.xml
如要设置用户名,密码,在 <systemUsage> 标签后加
<plugins> 
	<simpleAuthenticationPlugin>
		<users>
			<authenticationUser username="hrbb"  password="hrbb"  groups="users"/>
		</users>
	</simpleAuthenticationPlugin>
</plugins>


======================
 
--------------------------------------------Liferay-6.2 CE
下载 bundled with tomcat
下载 Liferay IDE-2.1.1 是elipse插件
下载 plugins SDK 和 portal javadoc

cd liferay-plugins-sdk-6.2-ce-ga2-20140319114139101\liferay-plugins-sdk-6.2\portlets
ant 会下载 liferay-plugins-sdk-6.2\.ivy目录中
create.bat my-greeting2 "My Greeting2" 建立项目(没有eclipe的东西) ,eclipse 中 import->liferay->liferay project from existing source,右击项目有liferay组(是插件新生成的),如果在project facades中取消了portlet,就没有办法再加上了
改回方法
.settings\org.eclipse.wst.common.project.facet.core.xml 中加   <installed facet="liferay.portlet" version="6.0"/>
.settings\中新加 org.eclipse.wst.common.project.facet.core.prefs.xml 文件
<root>
  <facet id="liferay.portlet">
    <node name="libprov">
      <attribute name="provider-id" value="com.liferay.ide.eclipse.plugin.portlet.libraryProvider"/>
    </node>
  </facet>
</root>


控制面板中可修改语言 
在 http://localhost:8080/ 中配置DB,语言,生成文件保存在 liferay-portal-6.2-ce-ga2/portal-setup-wizard.properties 
liferay-portal-6.2-ce-ga2\tomcat-7.0.42\lib  放 jdbc.jar
liferay-portal-6.2-ce-ga2\tomcat-7.0.42\webapps\ROOT\WEB-INF\classes  下建立 portal-ext.properties

# MySQL
jdbc.default.driverClassName=com.mysql.jdbc.Driver
jdbc.default.url=jdbc:mysql://localhost:3306/liferay62?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false
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

建立了 180 个表 没有前缀

liferay-portal-6.2-ce-ga2\tomcat-7.0.42\webapps\中直接删自己不使用portlet目录,也可删  welcome-theme ,calendar-portlet  ,opensocial-portlet
eclipse 中修改liferay 的 -Xmx1024m 为 -Xmx512m

---/WEB-INF/liferay-hook.xml
<hook>
	<language-properties>Language_en_US.properties</language-properties>
	<language-properties>Language_zh_CN.properties</language-properties>
</hook>

---/WEB-INF/liferay-display.xml
<display>
	<category name="category.mysample"> <!-- 国际化Key -->
		<portlet id="portlet62_add" /><!-- 必须与 portlet.xml中的 <portlet-name>portlet62_add</portlet-name> 相同-->
	</category>
</display>
---/WEB-INF/liferay-portlet.xml
<liferay-portlet-app>
	<portlet>
		<portlet-name>portlet62_add</portlet-name> <!-- 必须与 portlet.xml中<portlet-name>portlet62_add</portlet-name> 相同- -->
		<icon>/icon.png</icon>
		<ajaxable>true</ajaxable>
		<instanceable>true</instanceable> <!-- 在一个页中,是可以多个portlet实例 -->
		<header-portlet-css>/css/main.css</header-portlet-css>
		<header-portlet-javascript>/js/jquery-1.7.2.min.js</header-portlet-javascript>
	</portlet>
</liferay-portlet-app>	





控制面板中 建立Site Template ,建立Page template
建立Site 基于Site Template,

界面中 Add(+)->Applications-> My Sample (是liferay-display.xml文件中配置的国际化) 标签下有自己的项目


liferay 62 不能在jsp中仿问session 
--------------------------------------------上 Liferay


--------------------------------------------pluto 不升级,JDK8不可用
pluto(放射性检查计,冥王星) 的角色,用户
Java Specification Request(JSR)
Apache Pluto-2.0.3 , 实现portlet 2 Container 即 JSR-286 ,使用Tomcat-7.0.21


不要把pluto中已有的.jar放在自己的项目中,只供编译使用
portlet-api_2.0_spec-1.0.jar
pluto-taglib-2.0.3.jar
//可以使用eclipse集成pluto,要双击pluto->选择use tomcat installation->选择webapps目录,要在META-INF/建立contex.xml写<Context crossContext="true" />
//能否被pluto admin界面被检测到,是因为web.xml中<url-pattern>/PlutoInvoker/x 对应的org.apache.pluto.container.driver.PortletServlet
//界面pluto admin的page操作就是修改pluto/WEB-INF/conf/pluto-portal-driver-config.xml

启动后使用  http://127.0.0.1:8080/pluto/portal  ,使用用户 pluto,密码pluto登录,即tomcat-users.xml中的配置,看项目web.xml配置
带一个testsuite 项目,有portlet配置可以做复制用

----在纯净的Tomcat中的改变 OK
context.xml中多加
	<Context sessionCookiePath="/">
tomcat-users.xml 默认有
  <role rolename="pluto"/>
  <user username="pluto" password="pluto" roles="pluto,tomcat,manager"/>
  
conf\Catalina\localhost 默认有pluto.xml,testsuite.xml,主要是为配置 crossContext="true"
	<Context path="pluto" docBase="../PlutoDomain/pluto-portal-2.0.3.war" crossContext="true"></Context>
	<Context path="testsuite" docBase="../PlutoDomain/pluto-testsuite-2.0.3.war" crossContext="true"></Context>

把pluto-2.0.3\PlutoDomain\pluto和testsuite复制
还要加.jar到tomcat-6/lib
	pluto-container-api-2.0.3.jar
	pluto-container-driver-api-2.0.3.jar
	portlet-api_2.0_spec-1.0.jar
	pluto-taglib-2.0.3.jar
	ccpp-1.0.jar

	如报找不到org.apache.pluto.driver.PortalStartupListener ,把生成的删除再启动就OK,在pluto项目中pluto-portal-driver.jar
----
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

--------------------------------------------上 pluto


----------SVNANT是subeclipse项目,在ANT中使用SVN
---------------------------------Apache  Continuum   1.4.1 
apache-continuum-1.4.1\bin\continuum.bat console  启动 
http://127.0.0.1:8080/continuum/

continuum.bat install/remove 以管理员运行,安装为windows服务名为 "Apache Continuum"

$CONTINUUM_HOME/contexts/continuum.xml 配置SMTP,DB,可JNDI
可以安装到Tomcat  中做为一个项目,下载apache-continuum-1.4.1.war
但要有以下3个JNDI
mail/Session
jdbc/continuum
jdbc/users


<Context path="/continuum" docBase="/path/to/continuum-webapp-1.4.1.war">

  <Resource name="jdbc/users"
            auth="Container"
            type="javax.sql.DataSource"
            username="sa"
            password=""
            driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
            url="jdbc:derby:database/users;create=true" />

  <Resource name="jdbc/continuum"
            auth="Container"
            type="javax.sql.DataSource"
            username="sa"
            password=""
            driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
            url="jdbc:derby:database/continuum;create=true" />

  <Resource name="mail/Session"
            auth="Container"
            type="javax.mail.Session"
            mail.smtp.host="localhost"/>
</Context>


配置${appserver.base} 
-Dappserver.base=/path/to/continuum-base

如是Tomcat中加
  (export)set CATALINA_OPTS="-Dappserver.home=$CATALINA_HOME -Dappserver.base=$CATALINA_HOME"

  
  
  
  

-------------------------------DisplayTag 表格 分页
web.xml

<!--  
<filter>
	<filter-name>ResponseOverrideFilter</filter-name>
	<filter-class>org.displaytag.filter.ResponseOverrideFilter</filter-class>
</filter>

<filter-mapping>
	<filter-name>ResponseOverrideFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<filter-mapping>
	<filter-name>ResponseOverrideFilter</filter-name>
	<url-pattern>*.jsp</url-pattern>
</filter-mapping>
-->
 

 

id="codeTable 第一个作用是每个List的子对象的名，第二个生成<table id="">
 

<display:table name="blacklist" id="blacklistTable" defaultsort="1"   partialList="true" requestURI="/pages/sysInfoMgmt/codeMgmt/blacklist.do?method=query"
  size="resultSize" pagesize="${PAGE_SIZE}" >
	  <display:column property="number" title="${phoneNO}" style="text-align:center"  sortable="true" group="1"/>


加一个 group="1" 表示对第一列 不显示重复的 
sortable="true" 可排序的

<display:table defaultsort="1"  对第一列默认是排序的

有个问题是多次分页后 requestURI 后的参数会多次的重复




<display:table name="strategyList" requestURI="/pages/Notice/queryStrategy.do" pagesize="5" uid="al">
${al.strategeID}
也可以使用uid


uid 最好不要用,在和id一起使用没有办法得到正确的页号,编码参数错误
 

-----------classpath下的 displaytag_zh.properties  可以参考 org.displaytag.properties.displaytag.properties
basic.empty.showtable=true
basic.show.header=true
basic.msg.empty_list=没有可以显示的数据 
basic.msg.empty_list_row=<tr   align="center" class="empty"><td colspan="{0}">没有可以显示的数据</td></tr> 
paging.banner.onepage=<span class="pagelinks">[第一页/前一页] 									{0} 					[下一页/最后一页 ] 					</span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条	&nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.first=<span class="pagelinks">[第一页/前一页] 									{0} [ <a href="{3}">下一页</a>/ <a href="{4}">最后一页</a>] </span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.last=<span class="pagelinks">[<a href="{1}">第一页</a>/ <a href="{2}">前一页</a>] {0} 					[下一页/最后一页] 					</span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页<input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.full=<span class="pagelinks">[<a href="{1}">第一页</a>/ <a href="{2}">前一页</a>] {0} [ <a href="{3}">下一页</a>/ <a href="{4}">最后一页 </a>]</span> &nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.placement = bottom
paging.banner.group_size=5
    
paging.banner.item_name=记录  
paging.banner.items_name=记录 
paging.banner.page.selected=当前第<strong>{0}</strong>页
paging.banner.no_items_found=<span   class="pagebanner">没找到{0}   .</span>   
paging.banner.one_item_found=<span   class="pagebanner">共找到1条{0},当前显示所有的{0}   .</span>   
paging.banner.all_items_found=<span   class="pagebanner">共找到{0}条{1},当前显示所有的{2}.</span> 
paging.banner.some_items_found=<span   class="pagebanner">共找到{0}条{1},当前显示{2}到{3}条.</span>   

export.banner=<div   class="exportlinks">数据:   {0}</div>   
export.banner.sepchar=|     
export.types=csv excel xml pdf
export.excel=true
export.csv=true
export.xml=true
export.pdf=true
export.excel.label=my export excel
export.excel.filename=the_exported_excel.xls
#export.excel.include_header=true
#export.excel.class=display_tag.ExcelView
#export.pdf.class=display_tag.PdfView


#locale.resolver=org.displaytag.localization.I18nStrutsAdapter
#locale.provider=org.displaytag.localization.I18nStrutsAdapter


项group_size 表示多页时中间最多8个链接页,数字后不要有空格

<display:table export="true"
	
	....
	<display:setProperty name="export.pdf" value="true" />
</display:table>





分页新加　每页x条，到x页
-----JS 
function displaytagURL() {  //for go page
	var reg = /-p=\d{0,}/;
	var url=document.getElementById("hd").value.replace(reg,"-p=" + document.getElementById("tz").value);
	window.location=url;
}

function loadSelect(inSize)
{
	var pagesizeSel = document.getElementById("idpagesize");
	for (var i=0; i<pagesizeSel.options.length; i++) 
	{
		if( pagesizeSel.options[i].value == inSize )
		{
			pagesizeSel.options[i].selected="selected";
			return ;
		}
	}
}
function dealfoward() //for change page size
{
	var value = document.getElementById("idpagesize").value;
	if(document.getElementById("hd"))
	{	
		var url=document.getElementById("hd").value.replace(/-p=\d{0,}/,"-p=1");
		if( /\&pageSize=/.test(url) )
			location.href = url.replace(/\&pageSize=\d{0,}/, "&pageSize=" + value);
		else
			location.href = url+"&pageSize=" + value;
	}else
	{
		//for one page show all the record
		var url = window.location.href;  //<FORM method="GET"
		//debugger;
		if( /\&pageSize=/.test(url) )
			url = url.replace(/\&pageSize=\d{0,}/, "&pageSize=" + value);
		else
			url = url+"&pageSize=" + value;
		
		if(/-p=\d{0,}/.test(url))
			url = url.replace(/-p=\d{0,}/,"-p=1");
		else
			url+="&-p=1";
		
		window.location.href=url;
	}
}


<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
 
<display:table name="myList" id="myTable"  partialList="true" requestURI="/tablePageServlet.ser?action=query"
	size="resultSize" pagesize="${sessionScope.SESSION_PAGE_SIZE}" >
	  <display:column title="${title_name}" property="name" style="text-align:center"  sortable="true" group="1"/>
	  <display:column title="日期" property="date" />
	  <display:column title="日期" > ${myTable.date}  </display:column>
	   
</display:table>

如去 partialList="true" 多加  commons-collections-3.2.1.jar
URL 总是有重复的,应该和requestURI中有参数的原因

<script type="text/javascript">
	loadSelect(${sessionScope.SESSION_PAGE_SIZE});
</script>

-----Java
@WebServlet("/tablePageServlet.ser")
public class TablePageServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println(request.getParameter("action"));//总是init
	 	int pageNo=getPageNO(request,"myTable");
		int pageSize=getSessionPageSize(request);
		List data=generateData(pageNo,pageSize);
		request.setAttribute("myList", data);
		request.setAttribute("resultSize",data.size());
		request.setAttribute("title_name","姓名");
		request.getRequestDispatcher("display_tag.jsp").forward(request, response);
	}
	public List  generateData(int pageNo,int pageSize)
	{
		List dataList=new ArrayList();
		for(int i=pageNo;i<pageNo + pageSize + 3 ;i++)
		{
			VO vo=new VO();
			vo.setName("名"+i);
			vo.setDate("2013年");
			dataList.add(vo);
		}
		return dataList;
	}
	//---放入基类中
	protected int  getPageNO(HttpServletRequest request,String tableId)
	{
		int pageNo=1;
		String name = new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE);//服务端接收传来的页号
		if(request.getParameter(name)!=null)
		{
			try{
				pageNo = Integer.parseInt(request.getParameter(name));//display tag 中分页按钮请求才有
			}catch(NumberFormatException e)
			{
				pageNo=1;
			}
		}
		return pageNo;
	}
	protected  int  getSessionPageSize(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		int pageSize=Constant.DEFAULT_PAGE_SIZE;
	    String reqSize=request.getParameter("pageSize");
	    if(reqSize!=null && ! "".equals(reqSize))
	    {
	    	pageSize=Integer.parseInt(reqSize.toString());
	    	session.setAttribute(Constant.SESSION_PAGE_SIZE,pageSize);
	    }else
	    {
	    	 Object sessionPage =session.getAttribute(Constant.SESSION_PAGE_SIZE);
	 	    if(sessionPage!=null)
	 	    	pageSize=Integer.parseInt(sessionPage.toString());
	 	    else
	 	    	session.setAttribute(Constant.SESSION_PAGE_SIZE,pageSize);
	    }
	    return pageSize;
	}
}

 
------全部数据的排序
新排序功能的实现方法：
<display:table  sort="external" defaultsort="1" defaultorder="descending"
	表示使用displaytag的外部排序功能，默认对第一列降序排列显示
	<display:column  sortable="true"   如加 sortName="xx" 就要在display:table中加 sort="external"
 
// 获取外部排序列 
String strSortName = new ParamEncoder("myTable").encodeParameterName(TableTagParameters.PARAMETER_SORT);
String sortName = request.getParameter(strSortName);
String strOrder = new ParamEncoder("myTable").encodeParameterName(TableTagParameters.PARAMETER_ORDER);
String order = request.getParameter(strOrder);//order为升序还是降序(1为升序  2为降序)
String dbOrder="";
if("1".equals(order))
	dbOrder="asc";
else if("2".equals(order))
	dbOrder="desc";
 
---------
因为displaytag和struts2一起使用导致的，由于displaytag生成的参数中带“-”，而struts2中接受的参数中默认又不允许有“-”，
只要将，devMode设置为false就不会报这个错了，这个的确可以解决该问题。


根据当前页的数据 List .size()是否为0 ,解决删除当前页所有的项,翻页的Bug

=======================