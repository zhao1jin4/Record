=====Adobe CQ-5.5

http://dev.day.com
http://dev.day.com/docs/en.html

JavaDoc
http://dev.day.com/docs/en/cq/current/javadoc/			day
http://www.day.com/maven/jsr170/javadocs/jcr-2.0/index.html     JCR-2

http://dev.day.com/docs/en/cq/current/widgets-api/index.html   JS库   EXTJS风格的


JSR 283   Java Content Repository (JCR)-2.0 实现是Apache Jackrabbit
web content management (WCM) 
Apache Felix 是OSGI容器 ,eclipse插件开发也使用OSGI
Apache Sling  使用JCR的实现(如Jackrabbit),开始是Day公司的
RESET=Representational State Transfer

---安装
要设置JAVA_HOME是32位的JRE6 或 JDK6 ,不支持7
cq5-author-4502.jar 和 license.properties 在相同目录 



CRXDE 基于eclipse的开发工具

端口号是多少会读文件名-p后的数字,如cq5-author-p6754.jar,如被占用,也会自动增长,看日志吧

http://localhost:4502/admin/   登录   admin/admin  是一个Servlet engine(CQSE),也可使用其它的
http://localhost:4502/crx/    (crx) = Content Repository Extreme 2.2 是实现JCR
	Content Explorer工具的 开发放在 /apps目录 ,可使用的基组件在 /libs/foundation/components,有carousel要知道所有的 ,不能修改/libs目录
http://localhost:4502  WCM

http://localhost:4502/system/console     Apache Felix Web Console  ->Recent requests->clear


http://localhost:4502/content/    ---对应--- Content Exploer工具的/content目录 ---对应---   主页的WCM/WebSites中建立的Page ,中也有Resource Type
http://localhost:4502/content/geometrixx/en/services.html  /content/geometrixx/en/services/jcr:content/par/text_6 中有text的值就是页面上的值


Debug模式启动,端口是30303
crx-quickstart\server\server.bat - debug socket 
crx-quickstart\server\start -d   (Linux)


CRXIDE Lite 推荐的目录结构  
apps/<name>/components
apps/<name>/components/page
apps/<name>/templates 
apps/<name>/src			存OSGI bundles
apps/<name>/install		存编译的OSGI

CRXIDE Lite 在templates目录中建立 Template,
	属性Label是显示在CRXIDE中的名
	属性Ranking表示,主页的WebSites中建立页时模板显示在列表中的第几位
	属性Resource Type:输入components目录中要建立的component
	Content Explorer中看建立的属性

components目录中要建立组件后,可以双击.jsp文件来修改

Content Explorer中/etc/crxde/profiles/default/crxde:paths	中加其它目录,如/content,以便CRXDE可以使用,要重启服务

 
.jsp 要求
<%@include file="/libs/foundation/global.jsp" %>
就可以使用   <sling:defineObjects/>  <cq:defineObjects/>  标签 ,也可使用JSTL

<%=properties.get("jcr:title") %> 
<%=currentPage.getTitle() %>
<%=currentPage.getName() %>
<%=currentPage.getPath() %>
<%=currentPage.getDepth() %>

<%=currentNode.getName() %>
<%=currentNode.getPath() %>
<%=currentNode.getDepth() %> 

<%=properties.get("jcr:title","NO TITLE") %>
<%=currentNode.getProperty("jcr:title").getString() %>



<head>
	<cq:include script="/libs/wcm/core/components/init/init.jsp"/> <!--有<cq:includeClientLib, 会出现右边的面板(Sidekick) -->
</head>

<cq:include script="myScript.jsp"/>

建立 property ,Name写sling:resourceSuperType, value写 foundation/components/page ,CRXDE要打开properties视图来看
就可以用<cq:include script="head.jsp"/>,因/libs/oundation/components/page/下有head.jsp,Sidekick中会有Page Properties...

WCM/Tools/Desgins/ 目下建立Page,可以使用界面上的copy复制static.css,image目录,会放在/etc/designs/<建立的>/目录中,可以修改.css
Sidekick中会有Page Properties...->Advance->Desgin中选择建立的,美工立即生效

<%@include file="/libs/foundation/global.jsp"%>
<%@page import ="java.util.Iterator,
com.day.text.Text,
com.day.cq.wcm.api.PageFilter,
com.day.cq.wcm.api.Page,
com.day.cq.commons.Doctype,
org.apache.commons.lang.StringEscapeUtils"
%>
<%
Page navRootPage=currentPage.getAbsoluteParent(2);
if(navRootPage == null  && currentPage !=null)
	navRootPage=currentPage;

if(navRootPage != null )
{
	Iterator<Page> children=navRootPage.listChildren(new PageFilter(request));
%>
<ul>
<%	while(children.hasNext())
	{
		Page child=children.next();
		log.info("Child Page [{}] found .",child.getTitle());//在crx-quickstart\logs\error.log文件中,CRXDE的console的Server Log中也有
%>
	<li>
	    <a href="<%=child.getPath()%>.html">  <%=StringEscapeUtils.escapeXml(child.getTitle()) %>  </a>
	</li>
  <%}%>
</ul>
<%}%>


 <cq:include path="topnav" resourceType="training/components/topnav"/>  
 在coponent中建立Dialog(cq:Dialog) ,名字默认是dialog,不能改,生成的子级items(cq:TabPanel)\items(cq:WidgetCollection)\tab1( cq:Panel),可以修改属性title为显示的值
 
右击tab1->new->node,输入items类型为cq:WidgetCollection
右击items->new->node,输入titile类型为cq:Widget
右击titile->new->property,Name中输入"name",value:中输入"./title"  对应<%=properties.get("title",currentPage.getTitle())%>,对应于当前componet范围内
右击titile->new->property,Name中输入"fieldLabel",value:中输入 xxx
右击titile->new->property,Name中输入"xtype",value:中输入textfield,可选textare,sizefield(多少px * 多少px),pathfield(输入时会有路径提示),multifield,datefield,colorfield


建立组件时 Supper Source Type : foundation/components/parbase   处理二进制
复制 /libs/foundation/components/image/dialog/items/image ,看其它属性,有属性 xtype的值为smartimage ,Sidekick中下方选择Design Mode,可以修改图片

<%
String home=Text.getAbsoluteParent(currentPage.getPath(),2);
Resource res=currentStyle.getDefiningResource("fileReference");
if(res==null)
{
    res=currentStyle.getDefiningResource("file");	//currentStyle对应design_dialog,image节点的name的值是./file
}
log.error("path is:"+currentStyle.getPath());
%>
<a href="<%=home %>.html">
<%
if(res==null)
{
%>
	Home Page Placeholder
<%
}
else
{//对加了图片
	Image img=new Image(res);
	img.setItemName(Image.NN_FILE,"file");
	img.setItemName(Image.PN_REFERENCE,"fileReference");
	img.setSelector("img");
	img.setDoctype(Doctype.fromRequest(request));
	img.setAlt("Home Page Placeholder");
	img.draw(out);
}
%>


cq:Dialog 必须命名为"dialog",在component中用于Design的必须为 "design_dialog",Widget Collection,通常命令为"items"
如使用design_dialog ,可以全局内容(存在/etc/designs)做编辑

<cq:include path="breadcrumb" resourceType="foundation/components/breadcrumb"/>  path=""的值只做设计时的显示和ContentExplorer中的名字,会页面导航 像  首页->产品->xx->

 /libs/foundation/components/parsys  是paragraph

<cq:include path="par" resourceType="foundation/components/parsys" />   在设计模式中选择可以放入的组件,因编辑模式中拖放



类型为cq:Widget的属性name为./xx,xtype属性是selection 表示多选一(当type属性为select是下拉列表,否则为单选组),子节点的cq:WidgetCollection类型的名字要为options,
		再子节点为nt:unstructured ,可加text属性的值做显示,value属性做传值,即用properties.get("xx")

建立组件时的group的值是显示在SideKick中的组

class XX  extends AbstractImageServlet{
	@Override
	protected Layer createLayer(ImageContext context) throws RepositoryException, IOException 
	{
		return null;
	}
}

复制  /libs/foundation/components/textimage/dialog/items/tab2 和 tab3 ,tab2有属性值为./image/xxxx,对应代码  new Image(resource,"image");
复制  /libs/foundation/components/textimage/cq:editConfig 为拖动使用 下级有必须命名的 cq:dropTargets类型为nt:unstured 
<%@page import ="com.day.cq.wcm.foundation.Image"%>
<%
 Image image=new Image(resource,"image");
image.setSelector(".img");
String text=properties.get("text","TEXT NA");
String path=currentStyle.get("path","PATH NA");//是得到design_dailog中的name属性值为path
%>
<h2><%=path %></h2>
<%image.draw(out); %>


xtype属性的值为 richtext

xtype属性为pathfield的cq:Widget的属性为
regex属性的值是正则表达式
regexText是正则验证失败时的信息
rootPath是浏览时就选择的目录

sling:resourceType 的值要是可以找到的.jsp

建立组件时的 Allowed Parents:中写 */*parsys                    ####*/

<cq:include path="userinfo" resourceType="foundation/components/userinfo"/> 有显示登录的用户名和 Sign out
<cq:include path="toolbar" resourceType="foundation/components/toolbar"/> 在设计模式,有一个HTML List的复选框,会读取名为"toolbar"页下的有其它页,是一个链接区,类似页底的"关于我们"
<cq:include path="timing" resourceType="foundation/components/timing"/>可以记录组件/脚本执行时间
<cq:include path="iparsys" resourceType="foundation/components/iparsys"/> 会有界面
--------手机  
/libs/wcm/mobile/components/emulators ,目录结构和自己的目录结构一样

Wireless Universal Resource FiLe(WURLF)  根据手机的user_agent头信息,来知道手机的参数信息
/libs/wcm/mobile/wurfl/wurfl.xml
sling:resourceSuperType 为 wcm/mobile/components/emulators/base


建立目录 <project>/components/emulators/ 建立组件myemulator(cq:Component)
建立名为css类型为cq:ClientLibraryFolder的Node

wcm/mobile/components/emulators/blackberry/css/css.txt  
#base=source   表示找emulator.css文件要在source目录中找
emulator.css

复制 wcm/mobile/components/emulators/blackberry/css 
修改 emulator.css中的
#cq-emulator.blackberry 为 #cq-emulator.myemulator
#cq-emulator-content.blackberry 为 #cq-emulator-content.myemulator

如要支持触膜,滚动,建立节点名为cq:emulatorConfig,类型为 nt:unstructured 
属性名	 类型		值
canRotate  Boolean    true
touchScrolling	Boolean	true

WCM/Tools/Mobile/Device Groups/双击 Smart Phones->Edit按钮->Emulator标签->增加自己的myEmulator
http://localhost:4502/etc/mobile/useragent-test.html  设置User-Agent

 
sling:resourceSuperType 为 foundation/components/search

---国际化  
建立节点名字必须命名为"i8n"(类型为sling:Folder), 如在项目目录下,就只对当前项目生效,如在组件下就只对当前组件生效
建立子节点en(类型为sling:Folder)->选中en->Mixin按钮(只能使用CRXDE Lite或Content Explorer)->选择 mix:language->OK save all就加了属性->再建立属性jcr:language(类型为String)值为en
再建立子节点fieldLabel(名字任意)(类型为sling:MessageEntry) ->增加属性名为sling:key,类型String,值为i18n-title ->增加属性名为sling:message,类型String,值为Enter Title Here
界面修改fieldLabel的值为i18n-title 
 
 
---自定义xtype
在项目下建立 widgets (名任意)节点(类型cq:ClientLibraryFolder)
/etc/clientlibs   ,/etc/目录不可被客户端仿问
注册 ,复制/lib/foundation/components/page/headlibs.jsp 到自己的项目 ,加入<cq:includeClientLib js="training.widgets"/>

<项目>/widgets/js.txt
#base=files
training.js

<项目>/widgets/files(sling:Folder)/training.js
Training={};
Training.Selection=CQ.Ext.extend(CQ.form.CompositeField,   //文档上没有CQ.Ext.extend,但能执行过
{
	text:"default text",
	constructor:function(config)
	{
		if(config.text!=null)
			this.text=config.text;
		var defaults=
		{
			height:"auto",
			border:false,
			style:"padding:0;margin-bottom:0;",
			layoutConfig:
			{
				labelSeparator:CQ.themes.Dialog.LABEL_SEPARATOR
			},
			defaults:
			{
				msgTarget:CQ.themes.Dialog.MSG_TARGET
			}
		};
		CQ.Util.applyDefaults(config,defaults);
		Training.Selection.superclass.constructor.call(this,config);
		this.selectionForm=new CQ.Ext.form.TimeField(
		{
			name:this.name,
			hideLabel:true,
			anchor:"100%",
			intDate:new Date()
		});
		this.add(this.selectionForm);
	},
	processRecord:function(record,path)//没用???
	{
		this.selectionForm.setValue(record.get(this.getName()));
	}
});
CQ.Ext.reg("trainingSelection",Training.Selection);//xtype中就可以使用trainingSelection 了


--
右击项目目录->build->create bundle
会生成.bnd文件和一个.java,用来当启动和停止时被通知

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
 private static final Logger log = LoggerFactory.getLogger(Activator.class);
  public void start(BundleContext context) throws Exception {
        log.info(context.getBundle().getSymbolicName() + " started");
   }
	public void stop(BundleContext context) throws Exception {
        log.info(context.getBundle().getSymbolicName() + " stopped");
     }
}
建立.java,右击.java->build->compile
右击.bnd->build->build bundle,就可以在.jsp中import使用


--
WCM/website中右击页->workflow...->选择Publish example 填写其它->start,就有一个流图标
WCM/workflow->在Model标签中双击publish exmamle
WCM/inbox 显示有一个流程,选中->点complete按钮->流程消失->website中的流图标也消失,变为published
import javax.jcr.Node;//有多个包的
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;//有多个包的

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

@Component
@Service
@Properties( {
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "A sample workflow process implementation."),
		@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
		@Property(name = "process.label", value = "My Sample Workflow Process") })
public class MyProcess implements WorkflowProcess
{
	private static final String TYPE_JCR_PATH="JCR_PATH";
	
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args)throws WorkflowException 
	{
		WorkflowData workflowData=item.getWorkflowData();
		if(workflowData.getPayloadType().equals(TYPE_JCR_PATH))
		{
			String path=workflowData.getPayload().toString()+"/jcr:content";
			try{
				Node node=(Node)session.getSession().getItem(path);
				if(node!=null)
				{
					node.setProperty("approved", readArguments(args));
					session.getSession().save();
				}
			}catch(RepositoryException e)
			{
				throw new WorkflowException(e.getMessage(),e);
			}
		}
	}
	private boolean readArguments(MetaDataMap args)
	{
		String argument=args.get("PROCESS_ARGS","false");
		return argument.equalsIgnoreCase("true");
	}
}

编译时 报Only a type can be imported. org.apache.felix.　　忽略，直接右击.bnd->build->build bundle 会在项目目录下的/install目录下生成xx.jar

WCM/workflow->Models标签中点new按钮->输入名Approval后出现在列表中,双击->在图中双击step1->User/Group标签中选择contribute组,在工具箱中的workflow组中拖Process Step
	双击在Process中选择.java生成的My Sample Workflow Process->save
WCM/website中右击页->workflow中就有建立的Approval了，在inbox中complete时会调用.java代码,会在content/<点击页>/jcr:content中加属性approved

---
/crx/下在packge mananger
建立一个包，起名->edit->filters->add filter 把自己建立过的路径加入,包括/etc/workflow及其它 -> more下的build->就可以下载导入到其它同版的CQ中
/apps/training
/libs/foundation/components/breadcrumb
/etc/designs/trainingDesign
/etc/workflow
/content/traing_site



日志分析(firefox有工具)
crx-quickstart\logs\request.log 有请求的响应时间
crx-quickstart\opt\helpers>java -jar rlog.jar -n 10 ../../logs/request.log  查看前10个请求响就时间最长的

------------未试的
com.day.cq.wcm.api.PageManager
ResourceType首先 搜 /apps ,再次 /libs
SlingPostServlet,SlingHttpServletRequest request
request.getResource().getResourceType();
request.getRequestPathInfo().getSelectorString();
request.getRequestPathInfo().getExtension();
ResourceResolver.getResource(String )
PageMananger x getPage("")

<cq:editConfig>
<cq:childEditConfig>


 <sling:include resource="%=par%"/>
 默认包含CSS为 "static.css"
	
AbstractImageServlet
WCMUtil
ImageHelper
Font
Layer

xtype 设为 cqinclude
path 为 /libs/foundation/components/image/dialog/items.infinity.json
类型为 cq:EditConfig 
类型为 cq:dropTargetConfig
cq:emulatorConfig


javax.jcr.Session		用Node的getSession();
javax.jcr.Workspace		用Sesssion的getWorkspace();
javax.jcr.query.QueryManager	用Workspace的getQueryManager();
		Query 	createQuery(String statement, String language) 
javax.jcr.query.Query			  
		QueryResult 	execute() 
javax.jcr.query.QueryResult
		RowIterator 	getRows() 
 
 
<cq:includeClientLib> 是对com.day.cq.widget.HtmlLibraryManager类的包装

<cq:includeClientLib> 的categories == HtmlLibraryManager的writeIncludes 逗号分隔的js或css
<cq:includeClientLib> 的theme == HtmlLibraryManager的writeThemeInclude 逗号分隔的js或css
<cq:includeClientLib> 的js == HtmlLibraryManager的writeJsInclude 逗号分隔的js
<cq:includeClientLib> 的css == HtmlLibraryManager的writeCssInclude 逗号分隔的css 
<cq:includeClientLib> 的themed  只对纯js,或只对纯css


修改文件名为  cq5-publish-4502.jar  安装后为publish端

-------days上的doc
安装为windows 服务
必须先cd quickstart\opt\helpers
再instsrv.bat  cq5 , 但未成功???

 
默认的  crx-quickstart\repository\repository.xml
安装前建立 bootstrap.properties 文件  (与 cq5-<version>.jar and license.properties 在一起)
repository.home=repositoryRelocated
repository.config=repositoryRelocated/repository.xml

 
war文件中WEB-INF/web.xml 
	修改sling.run.modes 为publish
	打开 sling.home 并设置
	
	
修改OSGi admin密码 http://localhost:4502/system/console/configMgr ,登录后找到 Apache Felix OSGi Management Console 编辑,在Password中输入新的密码
修改CQ admin密码
	
	
==================Apache Sling 使用JCR的实现(如Jackrabbit)  开始是Day公司的

java -jar org.apache.sling.launchpad-6-standalone.jar 默认在当前目录下建立sling目录
sling\logs\error.log

http://localhost:8080/system/console/bundles  admin/admin   和CQ的一样

curl -u admin:admin -F"sling:resourceType=foo/bar" -F"title=some title" http://localhost:8080/content/mynode
-F 表示 form data ,foo/bar对应下面
后可以用 http://localhost:8080/content/mynode.json 查看
http://localhost:8080/system/console/requests  有历史

建立目录
curl -X MKCOL -u admin:admin http://localhost:8080/apps/foo
curl -X MKCOL -u admin:admin http://localhost:8080/apps/foo/bar
-X 命令 

---html.esp
<html>
  <body>
    <h1><%= currentNode.title %></h1>
  </body>
</html>

curl -u admin:admin -T html.esp http://localhost:8080/apps/foo/bar/html.esp
-T 上传 
 
http://localhost:8080/content/mynode.html 来测试


==================Apache Jackrabbit JCR的实现
java -jar jackrabbit-standalone-2.6.3.jar  可用--port 修改端口 , 仓库默认为当前目录下建立jackrabbit ,可用--repo修改
 http://localhost:8080

WebDAV   http://localhost:8080/repository/default/  可浏览 (nt:file) 和 (nt:folder)  ,要用户名密码,repository.xml中有配置admin和anonymous用户 

admin的密码是admin
anonymous的密码是空

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
Repository repository =new URLRemoteRepository("http://localhost:8080/rmi");
	

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;

import org.apache.jackrabbit.core.TransientRepository;

Repository repository = new TransientRepository(new File("d:/temp/jackrabit_repos"));
Session session = repository.login();//只引用 jackrabbit-standalone-2.6.3.jar 测试OK
try {
	String user = session.getUserID();
	String name = repository.getDescriptor(Repository.REP_NAME_DESC);
	System.out.println("Logged in as " + user + " to a " + name + " repository.");
} finally {
	session.logout();
}
----
Repository repository = new TransientRepository(new File("d:/temp/jackrabit_repos"));
Session session = repository.login(new SimpleCredentials("admin","admin".toCharArray()));
try {
	Node root = session.getRootNode();

	// Store content
	Node hello = root.addNode("hello");
	Node world = hello.addNode("world");
	world.setProperty("message", "Hello, World!");
	session.save();

	// Retrieve content
	Node node = root.getNode("hello/world");
	System.out.println(node.getPath());
	System.out.println(node.getProperty("message").getString());

	// Remove content
	root.getNode("hello").remove();
	session.save();
} finally {
	session.logout();
}
---
Repository repository = new TransientRepository(new File("d:/temp/jackrabit_repos"));
Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
try
{
	Node root = session.getRootNode();
	if (!root.hasNode("importxml"))
	{
		System.out.print("Importing xml... ");
		Node node = root.addNode("importxml", "nt:unstructured");
		InputStream is = ThirdHop.class.getResourceAsStream("/jcr_jackrabbit/data.xml");
		session.importXML(node.getPath(), is, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
		is.close();
		session.save();
		System.out.println("done.");
	}
	dump(root);
} finally
{
	session.logout();
}
private static void dump(Node node) throws RepositoryException
{
	System.out.println(node.getPath());
	if (node.getName().equals("jcr:system"))
		return;
	PropertyIterator properties = node.getProperties();
	while (properties.hasNext())
	{
		Property property = properties.nextProperty();
		if (property.getDefinition().isMultiple())
		{
			Value[] values = property.getValues();
			for (int i = 0; i < values.length; i++)
			{
				System.out.println(property.getPath() + " = " + values[i].getString());
			}
		} else
			System.out.println(property.getPath() + " = " + property.getString());
	}
	NodeIterator nodes = node.getNodes();
	while (nodes.hasNext())
		dump(nodes.nextNode());
}	
 
