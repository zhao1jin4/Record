
=============================EJB
javax.ejb.Local;
javax.ejb.Remote;

javax.ejb.Stateful;
javax.ejb.Stateless;
javax.ejb.Singleton;

javax.ejb.EJB;
javax.ejb.LocalBean;
javax.ejb.Startup;

@LocalBean

@Local  

@EJB 放属性前,注入,已经@Singleton 或 @Stateless
@Startup 容器启动时加载

//--Session Bean 的三种状态
@Stateful
@Stateless 
@Singleton 单例类
---
@Stateless  放实现类前 ,实现接口 StatelessSessionBean (命名规范 在接口名后加Bean,也可不实现接口)
@Remote  放在接口前 (StatelessSession)
		也可放在实现前@Remote (StatelessSessionBean.class)

//客户端代码在容器内
InitialContext contex = new InitialContext();
//GlassFish容器
StatelessSession sless = (StatelessSession) contex.lookup("enterprise.hello_stateless_ejb.StatelessSession");// 全包类名
//java:global/automatic-timer-ejb/StatelessSessionBean  //java:global以/分隔,最后一个包,两者不能切换????
//接口要服务端和客户端都要有,包名相同,(服务端部署.jar ,EJB)


EJBContainer c = EJBContainer.createEJBContainer();
Context ic = c.getContext();
Object o= ic.lookup("java:global/ejb-embedded/SimpleEjb");

import javax.ejb.Timer;
import javax.ejb.Schedule;

@Schedule(second="*/3", minute="*", hour="*", info="Automatic Timer Test")
public void test_automatic_timer(Timer t)
{
	System.out.println("Canceling timer " + t.getInfo() + " at " + new Date());
	if(count++>10)
	{
		 t.cancel();
		 System.out.println("Done");
	}
}
	
eclipseEE 中在J2EE-> Enterprise Application Project会产生META-INF\application.xml

ear包中的APP-INF\lib目录放jar
ear包中的META-INF目录中的application.xml中
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE application PUBLIC "-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN" "http://java.sun.com/dtd/application_1_3.dtd">
<application>
  <display-name>Foundation</display-name>
   <module>
    <web>								<!-- Web -->
      <web-uri>MDM.war</web-uri>
      <context-root>MDM</context-root>
    </web>
  </module>
  <module>
    <ejb>PARP-MessageHandlerEJB.jar</ejb> <!-- EJB -->
  </module>
  <module>
    <connector>mailconnector.rar</connector> <!-- JCA -->
  </module>
</application>


---老的方式 .jar/META-INF/ebj-jar.xml  
<ejb-jar xmlns="http://xmlns.jcp.org/xml/ns/javaee" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         metadata-complete="false" 
         version="3.2" 
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd">
  <display-name>JavaMailMDB</display-name>
  <enterprise-beans>
    <message-driven>
      <display-name>JavaMailMDB</display-name>
      <ejb-name>JavaMailMDB</ejb-name>
      <ejb-class>samples.connectors.mailconnector.ejb.mdb.JavaMailMessageBean</ejb-class>
      <messaging-type>samples.connectors.mailconnector.api.JavaMailMessageListener</messaging-type>
      <transaction-type>Container</transaction-type>
    </message-driven>   
  </enterprise-beans>
</ejb-jar>

import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@MessageDriven(messageListenerInterface=JavaMailMessageListener.class,
        name="JavaMailMDB",
        activationConfig = {
			@ActivationConfigProperty(propertyName = "serverName", propertyValue = "localhost")//配置新的属性值
			}
		)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JavaMailMessageBean implements MessageDrivenBean, JavaMailMessageListener //自定义接口
{
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void onMessage(javax.mail.Message message) {//自定义接口方法
	}
	public void ejbRemove() {
    }
	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		this.mdc = mdc;
    }
}

=============================WebSphere full profile for Developer 8.5 版本
C:\Program Files (x86)\IBM\WebSphere\AppServer\bin\ProfileManagement下
开始 ->WebSphere Customerization Toolbox(wct.ba) 工具 ,Profile Management Tool(pmt.bat)

C:\Program Files (x86)\IBM\WebSphere\AppServer\profiles\AppSrv01    安装时有LDAP的东西

就在C:\Program Files (x86)\IBM\WebSphere\AppServer\bin\ProfileManagement\wct.bat 或 pmt.bat 有界面的
建立profile时只能选择一个特性如SCA,可在建后Augment再加其它特性如XML

启动服务
可用开始菜单,或者services.msc中的IBM Websphere
以管理员运行cmd ,AppSrv01\bin\startServer.bat server1
日志在:AppSrv01\logs\server1\startServer.log 和 stopServer.log
开始菜单中的admin console , http://localhost:9060/ibm/console   可以不输入任何东西直接登入,也可随便输入

AppSrv01\bin>serverStatus.bat -all 
也可用
serverStatus.bat -all -user admin -password admin  
显示服务器名:server1

AppSrv01\bin>stopServer.bat server1 停止服务


C:\Program Files (x86)\IBM\WebSphere\AppServer\profiles\AppSrv01\installedApps\<主机名>Node01Cell\  是ear解压目录 xx.ear


服务器->WebSphere Application Server-> server1 > 右则的 "Java 和进程管理"下的"进程定义" > 右则的 "Java 虚拟机" >右则的 "定制属性",可增加如user.dir的系统属性,也可设置Xmx等


服务器->WebSphere Application Server->点击server1进入->点击 通信下的[端口] ,查看
	BOOTSTRAP_ADDRESS 	my-PChostname  	2809  
	WC_adminhost 	*   	9060   
	WC_defaulthost 	*   	9080   

环境->共享库->选择有节点值和服务器值的那一项->新建->起名all_lib,输入 类路径 是所有.jar包所在的目录,可以有多行
应用程序->企业应用程序->进入项目->进入-> 引用下的"共享库引用" ,复择项目-> 点[引用共享库] 按钮,选择自己建立的all_lib
	

资源->JDBC->JDBC提供程序->下拉选择选带有节点值和服务器值 ->新建->数据库类型：Oracle/SQL Server/DB2,没有MySQL只能自定义,实现类名:com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource
		类路径中可以输入绝对的.jar路径 
资源->JDBC->数据源->新建-> 下拉选择选带有节点值和服务器值,JNDI名称不要带有"/"如 jndi_mysql->要选择现有的JDBC提供程序,选择刚刚建立的->到"设置安全性别名"页中有"全局 J2C 认证别名"点入->新建设置用户名密码
		[组件管理的认证的别名]  [容器管理的认证别名] 选择刚刚建立的
资源->JDBC->数据源->选建立的->其他属性中的"定制属性"->URL->输入jdbc:mysql://localhost:3306/databasename, 可以测试连接如不成功,就重启服务器

如建立数据源是Oracle的要在"定制属性"中手动增加user,password属性,测试OK


Properties prop=new Properties();
prop.put(Context.INITIAL_CONTEXT_FACTORY,"com.ibm.websphere.naming.WsnInitialContextFactory");
prop.put(Context.PROVIDER_URL, "iiop://localhost:2809/");
InitialContext ctx=new InitialContext(prop);
DataSource ds=(DataSource)ctx.lookup("jndi_mysql");//不要有/字符,不能加java:xx
Connection conn=ds.getConnection();

C:\Program Files (x86)\IBM\WebSphere\AppServer\runtimes\com.ibm.ws.admin.client_8.5.0.jar  大小50MB ,com.ibm.ws.orb_8.5.0.jar 大小2M 
要用IBM的JAVA_HOME=C:\Program Files (x86)\IBM\WebSphere\AppServer\java,是1.6.0版本


部署war包

[应用程序]->[资产]->[导入]->选择war包,怎么是上传不了?????
[应用程序]->[企业级应用程序 ]->[新建]->输入名称->[添加资产]->选择刚才建立的->下一步->下一步->下一步->下一步->改[上下文根 ]为web路径->下一步->[完成]->单击[保存]连接
[应用程序]->[企业级应用程序 ]  中可以看到部署的 ，选中->启动按钮
[WebSphere 企业应用程序]中也可以安装war包  选择安装的war项目  ->"Web 模块的上下文根",可以修改 /xx
http://localhost:9080/[上下文根 ]

没有eclipse插件,只能用 Rational Software Architect for WebSphere(RSA更全有UML) 或 Rational Application Developer for WebSphere(RAD)
RAD建立web项目要生成xxxEAR项目,才可在RAD的集成的websphere加入项目,ear包只加了war包,可以没有application.xml
---RAD自动建立war包/WEB-INF/ibm-web-ext.xml
<?xml version="1.0" encoding="UTF-8"?>
<web-ext		 xmlns="http://websphere.ibm.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://websphere.ibm.com/xml/ns/javaee http://websphere.ibm.com/xml/ns/javaee/ibm-web-ext_1_0.xsd" version="1.0">

	<reload-interval value="3"/>
	<enable-directory-browsing value="false"/>
	<enable-file-serving value="true"/>
	<enable-reloading value="true"/>
	<enable-serving-servlets-by-class-name value="false" />
</web-ext>
---RAD自动建立war包/WEB-INF/ibm-web-bnd.xml
<?xml version="1.0" encoding="UTF-8"?>
<web-bnd  			xmlns="http://websphere.ibm.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://websphere.ibm.com/xml/ns/javaee http://websphere.ibm.com/xml/ns/javaee/ibm-web-bnd_1_0.xsd" version="1.0">
	<virtual-host name="default_host" />
</web-bnd>
---
RAD的集成的websphere不能直接增加war包项目,可先在webphere中部署war包,同样的项目在RAD中修改java文件可以生效,但修改jsp,xml没有效果???

RAD集成后webpshere的目录在runtimes\base_v7\下

base_v7\profiles\AppSrv01\installedAssets,是在Business-level applications建立联时生成的,目录中的所有的.jar是解压形式(profile中要urgment加SCA特性)
base_v7\lib\ext
base_v7\profiles\AppSrv01\config\cells\<HostName>Node03Cell\resources.xml  有配置的DataSource
base_v7\profiles\AppSrv01\config\cells\<HostName>Node03Cell\nodes\<HostName>Node03\servers\server1\server.xml 中配置maximumHeapSize,如太大会导致服务启不来,要修改

Application servers > server1->Communications 中的Ports中有占用的所有的端口

---建立JMS Queue
Service integration->Buses->new一个叫 InternalJMS ,进入刚建立的 InternalJMS->Bus members ->Add 一个->单择Server->如是第一次建立要重启Server,检查建立的bus memeber中的server,进入确认为绿箭头(started)
进入刚建立的 InternalJMS ->Destinations ->new 一个,选择Queue,命名 myQueueDest

和Resources -> JMS ->Queue建立是一样的效果
Resources -> JMS -> JMS Providers -> Default messaging provider->Queues ->new 一个,Bus name选择上面建立的 InternalJMS,Queue name选择上面建立的 myQueueDest
---建立JMS QueueFactory
Resources -> JMS -> JMS Providers -> Default messaging provider->Queue connection factories->new 一个,在Bus name中选择上面建立的InternalJMS,
			Provider Endpoints中写localhost:7277:BootstrapBasicMessaging(对Factory和Activation specification) ,7277是没被占用的,格式为<IP Address of the Host Machine>:<SIB_ENDPOINT_ADDRESS >:BootstrapBasicMessaging

---Queue错误关联
在Bus中建立两个Queue 为 my_queue 和 my_queue_error
进入 my_queue,在Exception destination中单选Specify,输入 my_queue_error,修改Maximum failed deliveries per message为3
Resources->JMS -> Activation specifications ->选scope ,new->Default messaging provider ->输入Destination JNDI name为 my_queue,
		在Automatically stop endpoints on repeated message failure中复选Enable,Sequential failed message threshold 设3
---

		
=======================GlassFish 4
Oracle Enterprise Pack for Eclipse  中有 GlassFish 的插件

C:\glassfish4\glassfish\bin\asadmin.bat start-domain domain1 有日志提示4848端口 
C:\glassfish4\glassfish\domains\domain1\logs\server.log

C:\glassfish4\glassfish\bin\asadmin.bat stop-domain domain1

http://127.0.0.1:8080/ 有连接进入  http://localhost:4848/  GlassFish 控制台
http://127.0.0.1:8080/<部署的应用名>

C:/glassfish4/glassfish/docs/quickstart.html 有所有的端口

GlassFish 控制台 ->应用程序->部署->选择war包
GlassFish 控制台 ->配置->Server config->JVM设置->查看调试选项 端口为 9009,复选 "调试" 中的 "启用" ->保存->提示重启服务
右击项目->debug as -> Debug configuration ...->建一个 Java Remote Application->端口写 9009,检查项目的选择,就可Debug Web项目


建立数据源,资源->JDBC->先建立JDBC连接池,JDBC.jar放在C:\glassfish4\glassfish\domains\domain1\lib\ext,重启服务后再"试通"(如果先放mysql.jar再启建立连接池时设置的属性会变多)
	再建立JDBC资源
C:\glassfish4\glassfish\modules\有javax的其它jar包
C:\glassfish4\glassfish\modules\glassfish-naming.jar ,eclipse引入后会自动引用其它的.jar
//必须把编译后的class运行在glassfish中才行
Properties props=new Properties();
props.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.enterprise.naming.SerialInitContextFactory");
props.put(Context.PROVIDER_URL,"localhost:3700");//可加iiop://
InitialContext ctx=new InitialContext(props);
DataSource ds=(DataSource)ctx.lookup("jndi_mysql");//不用加java:xxx
Connection conn=ds.getConnection();

==============================weblogic使用
http://edocs.weblogicfans.net/wls/docs92/index.html   weblogic 9.2在线文档
http://www.oracle.com/technetwork/middleware/weblogic/documentation/bea-084522.html  weblogic 10.3.3在线文档
http://download.oracle.com/docs/cd/E14571_01/wls.htm

https:443,7002(weblogic)


nohup ${DOMAIN_HOME}/bin/startWebLogic.sh $* 1> console.log 2>&1 &


WebLogic 9.x / 10.x /10gR3 均起作用。
-Dweblogic.threadpool.MinPoolSize=100
-Dweblogic.threadpool.MaxPoolSize=500

修改端口7001 到 88
也可以在界面中,AdminServer中可以修改
C:\bea\user_projects\domains\base_domain\config\config.xml
<server>
    <name>AdminServer</name>
    <listen-address></listen-address>
</server>
改为
<server>
    <name>AdminServer</name>
    <ssl>
      <enabled>false</enabled>
    </ssl>
    <listen-port>88</listen-port>
    <listen-address></listen-address>
</server>

<server>中使用ssl
<ssl>
      <name>MDMAdminServer</name>
      <enabled>true</enabled>
      <hostname-verification-ignored>true</hostname-verification-ignored>
      <listen-port>6002</listen-port>
</ssl>


WEB-INF目录下一定要有一个weblogic.xml文件 ,可以通过eclipse插件建立weblogic web project来生成 
开发模式 可以eclipse集成debug jsp和java文件


使用eclipse插件时注意，不要复选 disable automatic publish to server


//自动部署
可以通过 console来部署一个目录(有WEB-INF/web.xml,war包的解压),也可以是ear包的解压(中有很多war),
配置目录后,在<域目录>/config/config.xml中,可以修改
<app-deployment>
    <name>testWS</name>
    <target>AdminServer</target>
    <module-type>war</module-type>
    <source-path>G:\testWS</source-path>
    <security-dd-model>DDOnly</security-dd-model>
</app-deployment>
<app-deployment>
    <name>MSV-test</name>
    <target>AdminServer</target>
    <module-type>ear</module-type>
    <source-path>F:\work\MDM\smp_dev\MSV-test</source-path>
    <security-dd-model>DDOnly</security-dd-model>
</app-deployment>
都是在根下



ear包中的META-INF目录中的weblogic-application.xml
<?xml version="1.0"?>
<weblogic-application xmlns="http://www.bea.com/ns/weblogic/90" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<prefer-application-packages>
		<package-name>org.apache.xerces.*</package-name>
		<package-name>org.apache.commons.*</package-name>
		<package-name>org.apache.log4j.*</package-name>
	</prefer-application-packages>
</weblogic-application>


base_domain->Enviroment->servers->AdminServer->View JNDI Tree
base_domain->Services->JDBC->Data Source 建JNDI 数据源
base_domain->Services->Message-> JMS Modules->建立一个Module (jms配置文件在domain目录下的config中)->
建立ConnectionFactory时默认选择AdminServer不要选择subdeployment

建Topic,Queue时要建Subdeployment(用来指定哪个JMS Server上)
				 JMS Servers 可指定Persistent Store即保存到文件还是JDBC,指定target,哪个ManageServer,每个多一个migratable

Monitor->show Message 中可以设置一个selector的名字

建立JMS Modules时,会保存在[域]/config/自己配置的目录名/自己配置的文件名   (配置保存在这里)
也可把配置文件(META-INF/weblogic-application.xml,My-jms.xml)打在.ear包中

Subdeployment是指定JMS Module对应 JMSServer


要为Topic配置 Subdeployment,要target中有值
加入C:\bea\weblogic92\server\lib\weblogic.jar　到classpath中

多台weblogic域 JMS通信 要域"密码"一样,这里的密码是在console界面中的第一项即"域名"->security标签->下方的Advanced->Credential:处的密码

weblogic 有一种Distributed 的queue或者topic ,
	建立Distributed Queue不要复选 Allocate Members Uniformly ,会提示选择非Distributed的Destination,使用时配置成Distributed的JNDI, 起到对多个queue或topic的分发作用,如是queue会对中的每个成员发送消息,无论使用Listener，还是receive结果是乱的，而且一次还取不完(即会把Member中的一个收完)
	建立Distributed Queue复选 Allocate Members Uniformly,Member标签里是没有成员的,Monitoring也没有数据,但是可以使用JMS代码收到消息的,使用HermesJMS工具可以看到消息
		 对cluster 环境, 复选 Allocate Members Uniformly,默认会选cluster,和cluster中的所有Manage Server成员
		 对cluster 环境, 不要复选 Allocate Members Uniformly ,会提示选择非Distributed的Destination
日志目录C:\bea\user_projects\domains\base_domain\servers\AdminServer\logs

C:\bea\weblogic92\server\lib\ojdbc14.jar   OracleJDBC驱动


eclipse插件的停止服务器
C:/bea/jrockit_150_12/bin/java -classpath C:/bea/weblogic92/server/lib/weblogic.jar weblogic.Admin -url t3://localhost:7001 -username weblogic -password weblogic FORCESHUTDOWN AdminServer 
启动

对于Admin Serve域目录下->建文件：boot.properties 就可以在product mode下不用输入密码,OK
password=123456
username=weblogic
#文件未尾不能有空格
启动后把文件复制到domains\MotiveSMP\servers\AdminServer下,并生成目录security\boot.properties, 这里的文件是加密的,就可以删除明文的了

方法二,也可放在servers/AdminServer的security目录下,没有security目录要手工建立



加用户Security Realms->myrealm->Users and Groups

在weblogic.Xml中，加入 单位是秒,可以ear目录中的web项目的其它类,
如果有是线程里的代码则无效,如果是struts1使用了<tiles>也不行的
    <container-descriptor>
       <servlet-reload-check-secs>5</servlet-reload-check-secs>
       <prefer-web-inf-classes>true</prefer-web-inf-classes>
    </container-descriptor>



//产品模式转开发模式

setDomainEnv.cmd文件中修改set PRODUCTION_MODE=true 为set PRODUCTION_MODE=,即删true
setDomainEnv.sh文件中修改PRODUCTION_MODE="true"为PRODUCTION_MODE=""
set MEM_ARGS=-Xms256m -Xmx512m  设置JVM参数 

weblogic92\common\bin\quickstart	
C:\bea\weblogic92\common\bin\wlst.bat //交互命令
cd c:\bea\wlserver_10.0\common\bin  //控制台安装
Windows: config.cmd -mode=console
UNIX: sh config.sh -mode=console
  

 //静态安装
server922_win32.exe -mode=silent -silent_xml=silent.xml
silent.xml
	<bea-installer> 
		  <input-fields>
			  <data-value name="BEAHOME"                  value="c:/bea/" />
			  <data-value name="USER_INSTALL_DIR"         value="c:/bea//weblogic92" />
			  <data-value name=" WLW_INSTALL_DIR" value="c:/bea//workshop92" />
			  <data-value name="INSTALL_MERCURY_PROFILING_TOOLS" value="false"/>

			  <data-value name="INSTALL_NODE_MANAGER_SERVICE"   value="yes"  />
			  <data-value name="NODEMGR_PORT"                   value="5556" />
			  <data-value name="COMPONENT_PATHS" value="WebLogic Server/Server|WebLogic Server/Web Server Plug-Ins" />
			  <data-value name="INSTALL_SHORTCUT_IN_ALL_USERS_FOLDER"   value="yes"/>
		 
		</input-fields> 
	</bea-installer>


server/common/bin/config_builder 图形界面,用来创建weblogic模板
可以选择基于已有模板,也可选择domain目录,会生成user_templates目录下一个.jar文件

可以 control标签中停止Admin Server

Server标签->后加点 Customize 可以显示更多的列,有更多的信息

/weblogic92/common/bin/wlst.sh
或者用
/weblogic92/server/bin/setWLSEnv.sh 后java weblogic.WLST
>connect('weblogic','weblogic','t3://127.0.0.1:7001')
>ls()
>help()  提示hel('all')

Manage Server的logging标签有 Files to retain 7
>edit()
>cd('Servers')    ###cd('..')   cd ('Servers/AdminServer')
>cd('AdminServer') 
>pwd()
>cd('Log')
>cd('AdminServer')
>startEdit() ,相当界面上Lock&Edit
>set('FileCount','4') 刷新界面发现值变了
>save()  相当于点页的save按钮
>activate()  
>disconnect()
>exit()

会修改域的/config/conig.xml文件

java weblogic.WLST batch.txt 可读文件中的命令


weblogic92/samples/domains/wl_server/startWebLogic.sh 启动示例
weblogic92/samples/server/examples/build 有示例
weblogic92/samples/server/docs/ 有文档
deploy以后可以在Target标签中修改部署的目录

weblogic 自动的示例　PointBase数据库 
/weblogic92/common/eval/pointbase/tools/startPointBase.sh
database name:demo
port: 9092
user:PBPUBLIC
pass:PBPUBLIC, 
测试表SYSTABLES

startPointBaseConsole.sh 是一个图形界面


JDBC Datasource 也可以修改Target,Monitor标签中可以Test

Manage Sever->General->Server Start子标签,可以加CLASSPATH,也可以配置启动用户名密码

Manage Sever->Protocol->Channels->协议用Http->Listen Port:8008是新端口,External Listen Port:8001是对应已经存在的端口
原来使用8001的应用,也可以同使用8008端口

点域名->复选Enable Administration Port,Administration Port:9002,
原来的http://127.0.0.1:7001/  console  就不能用了,只可用https://127.0.0.1:9002



---使用https的设置
Manage Server打开 SSL Listen port Enabled,设置端口号

生成.jks 
keytool -genkey -v -alias dwkey -keyalg RSA -keysize 512
		-dname "CN=localhost,OU=Dizzyworld Web,O=dizzyworld\,Inc.,C=US" 
		-keypass dwkeypass -validity 365 -keystore dw_identity.jks 
		-storepass dwstorepass
生成.pem 给CA
keytool -certreq -v -alias dwkey -file dw_cert_request.pem -keypass dwkeypass
		-storepass dwstorepass -keystore dw_identity.jks

Server中有一个Keystores标签,默认是使用 Demo Identity and Demo Trust,现在使用Custom Identity and Java Standard Trust

Custom Identity Keystore:		是生成的.jks文件	
Custom Identity Keystore Type:	JKS
Custom Identity keystore passphrase:是keytool　-genkey -storepass的值
Trust栏中的密码自已写

SSL标签中
Identity and Trust Locations:选择KeyStores
Private Key Alias:是keytool -genkey -alias 的值
Private Key Passphrase:keytool -genkey  -keypass的值

--
	security Realm->User Lockout
	domain->security->unlock User 解锁用户
---证书

建立一临时目录
复制C:\bea\weblogic92\server\lib\CertGenCA.der和CertGenCAKey.der文件 

keytool -noprompt -import -trustcacerts -alias CA -file CertGenCA.der -keystore myKeyStore.jks -storepass password

	生成 myKeyStore.jks

CLASSPATH=C:\bea\weblogic92\server\lib\weblogic.jar
java  -cp C:\bea\weblogic92\server\lib\weblogic.jar utils.CertGen passowrd PCATCert PCATKey export PCAT
	PCAT是Admin的主机名

	生成 
	PCATCert.der
	PCATCert.pem
	PCATKey.der
	PCATKey.pem

java utils.CertGen password pingCert pingKey export ping
java utils.der2pem CertGenCAKey.der
java utils.der2pem CertGenCA.der
copy /b PCATCert.pem + CertGenCA.pem PCATCertChain.pem
copy /b pingCert.pem + CertGenCA.pem pingCertChain.pem
keytool -import -alias PCACert -file PCATCert.pem -keypass password -keystore myKeyStore.jks -storepass password
keytool -import -alias pingCert -file pingCert.pem -keypass password -keystore myKeyStore.jks -storepass password

--fail
java utils.ImportPrivateKey myKeyStore.jks password PCATKey password PCATCertChain.pem PCATKey.pem
java utils.ImportPrivateKey myKeyStore.jks password pingKey password pingCertChain.pem pingKey.pem
java utils.ValidateCertChain -jks PCATKey myKeyStore.jds password
java utils.ValidateCertChain -jks pingKey myKeyStore.jds password
keytool -list -keystore myKeyStore.jks -v
java utils.der2pem CertGenCAKey.der


复制myKeyStore.jks 到Admin的域目录下,和Manager的common/nodemanager目录下


---------------weblogic9.2 Cluster
F5,Radware是loadBlance硬件

http://edocs.weblogicfans.net/wls/docs92/cluster/setup.html

config.sh的界面配置 
在 Do you want to customize any of the following options?标签 选 .YES ->
配置 Aministratrion Server标签,默认AdminServer,7001 用来使用console的 ->
配置 Managed Servers标签 ,可加多个自定义名的服务器,使用未被使用的端口->
配置 Cluster标签 ,输一个 Cluster名,Mulicast Address任何输,Mulicat port任何未使用的,Cluster address 不写->
指定 Servers to Cluster标签,如有一个未选入Cluster,会到 Create Http poxry  Applications->
配置 Machines标签, Name* 的默认值是new_Machine_1,Node Manager Listener port 默认是 5556 ,可选Unix机器 ->
指定 Servers to Machines标签,


每个Machine有自己的NodeManager,选择操作系统
启动manager server,在windows 下可使用start startManagedWeblogic.cmd managed_1 就可以启动
start命令是开一个新的CMD窗口

/opt/bea/weblogic92/common/nodemanager/nodemanager.log

Machin可以加Server ,但Manager Server不能运行
NodeManager是启动Machine的,必须在运行,才可以在界面中远程启动ManageServer

建立Manage Server时不要写Lisenter Address ,如写127.0.0.1 ,只监听一个端口127.0.0.1:8001,则不能远程登录

----NodeManager管理,如Machine和AdminServer不在同一台机器上
setWLSEnv.cmd初始环境变量
weblogic92/server/bin/startNodeManager.sh  <本机IP> 5556 ###来启动
启动后weblogic92/common/nodemanager 目的是目录多一些文件 
java weblogic.WLST或者wlst 
>connect() 会提示输入AdminServer的用户名，密码，url
>help('nmEnroll')
>nmEnroll(r'c:/bea/weblogic92/common/nodemanager')  
##注意有个r (NodeManager enroll),把NodeManage服务器注册到AdminServer上,本地weblogic目录c:/bea/weblogic92/common/nodemanager中多了config和security目录
>exit()
再重新启动 ./startNodeManager.sh  <本机IP> 5556 ###来启动

注意,5556端口要和Admin端配置的相同,Machine的IP要<本机IP> ,Machine->Monitoring中监视状态要是Reachable,可以不用运行nmEnroll
也可以使用weblogic92/common/bin/startManagedWebLogic.sh 但没有stop
----




如做过cluster
JDBC Data Sources >JDBC Pool名->Targets ->可选择All servers in the cluster 和 Part of the cluster 表示数据源为哪些机器使用

Test JNDI 连接
Services->JDBC->DataSource->名字->Monitoring标签->Testing 子标签->Test DataSource 按钮



Cluster->点Cluster名 ->control标签->在下面 选择一个 Manager服务器 ->start 按钮 ->等大约4分钟
Server ->点Server名->control标签->.......
等当　Sate列变成RUNNING

启动Manage Server后  域目录下/servers/中会多出一个 managerServer目录,可以看log/[名].log日志,.out文件 Windows 看不了



----重新部署ear包 ,ManageServer 要一直启动的
只AdminServer上 可以仿问/console,只有ManageServer可仿问自己的项目
	Deployment->删除老的SMP.ear
在Deployment->增加新的SMP.ear
启动 SMP.ear

域目录下/servers/S_smp01/stage　目录 ,成功会自动复制Admin的Server.ear，如失败删除
----
	

启动 startManagedWebLogic.cmd managedserver1 http://localhost:7001
停止 stopManagedWebLogic.cmd managedserver1	  t3://localhost:7001 weblogic weblogic

增加 ManagerServer的JVM  在Server->名字->Server start 标签中修改 JVM参数

多播地址是介于 224.0.0.0 和 239.255.255.255 之间的 IP 地址
java -cp weblogic.jar  utils.MulticastTest -N AdminiServer -A 192.168.0.1 -P 7001   ##检测Multicast是否正常


./startManagedWebLogic.sh  Manage_slave http://192.168.11:7001 << EOF
>weblogic
>weblogic
>EOF


在主服务器上AdminServer 配置ManageServer IP为另一台机器,
	在console中不能连接另一台NodeManager来启动  <BEA-090482> <BAD_CERTIFICATE alert was received from????????
在次服务器上启动ManageServer ,使用C:\bea\weblogic92\common\bin\startManagedWebLogic.cmd,会在当前目录生成servers目录

---proxy
使用config.sh建立proxy,proxy 是不在cluster 中的
配置proxy的ManagerServer表示这台ManageServer是代理的(和其它的一样,默认Client Cert Proxy Enabled是没有选择的)，请求指向这个IP,可以分发给其它ManageServer
启动后在AdminServer的console中多了一个BEAProxy4_<cluster_name>_<proxy_name>的应用，在域目录下的apps目录中只有web.xml和weblogic.xml,是部署在proxy的ManagerServer下,同下

自己的应用部署应用时选择All servers in the cluster 而不是proxy，也不是adminServer

\samples\server\examples\src\examples\cluster\sessionrep\inmemrep\defaultProxyApp\WEB-INF\web.xml有示例
把这个项目单独发布到一个standalon的Server上 ,可能要打开不能同的流览器,或者断开服务器

web.xml的内容如下，修改ip和端口。  
<web-app>
  <servlet>
    <servlet-name>HttpClusterServlet</servlet-name>
    <servlet-class>weblogic.servlet.proxy.HttpClusterServlet</servlet-class>
  <init-param>
    <param-name>WebLogicCluster</param-name>
    <param-value>10.130.2.108:7003|10.130.2.108:7005</param-value> 
  </init-param>
  <init-param>
      <param-name>verbose</param-name>
      <param-value>true</param-value>
   </init-param>
   <init-param>
      <param-name>DebugConfigInfo</param-name>
      <param-value>ON</param-value>
   </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>HttpClusterServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>HttpClusterServlet</servlet-name>
    <url-pattern>*.jsp</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>HttpClusterServlet</servlet-name>
    <url-pattern>*.htm</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>HttpClusterServlet</servlet-name>
    <url-pattern>*.html</url-pattern>
  </servlet-mapping>
</web-app>

weblogic.xml的内容为：
<weblogic-web-app>
  <context-root>/</context-root>
</weblogic-web-app>        

当verbose和DebugConfigInfo开的时候 http://proxyIP:proxyPort/aa.jsp?__WebLogicBridgeConfig 会显示参数
如看到一个IP,可能是NodeManager不通的原因

---Session复制，进入Weblogic console->所有的Manager Server写入相同的名字->cluster->Replication Group: 写入名字,	Preferred Secondary Group:写入名字
对weblogic cluster的Session 复制  Weblogic.xml 配置

--网上的
<session-descriptor>
	<persistent-store-type>replicated</persistent-store-type>
	<sharing-enabled>true</sharing-enabled>
  </session-descriptor>
<context-root>/</context-root>
---boobooke的weblgic 10   内存复制
<session-descriptor> 
	<timeout-secs>300</timeout-secs>
	<invalidation-interval-secs>60</invalidation-interval-secs>
	<persistent-store-type>replicated_if_clustered</persistent-store-type>
</session-descriptor>
部署时在Install Application Assisant步(选择cluster->all server后面)中要在Source accessibility中选择copy this application into every target for me


2.所有放入session的类都要序列化。

3.Customer customer = (Customer)session.getAttribute(KEY_CUSTOMER);  
customer.setName("funcreal");  
在单机环境下，session中的变量customer的name属性就被更改了

然而在集群环境下，仅仅这样做是不能触发session同步机制的。必须要把customer变量在重新放入session中，即：
session.setAttibute(KEY_CUSTOMER, customer);  

备份 session 状态,
	1.数据库复制（通过 JDBC ）
	2.基于文件的复制 
	3.内存中的复制 ,该服务器指定另外一台集群中的服务器作为辅助服务器来存储会话数据的复本

数据库复制(通过 JDBC)
	create table wl_servlet_sessions(
	wl_id varchar2(100) not null,
	wl_context_path varchar2(100) not null,
	wl_is_new char(1),
	wl_create_time number(20),
	wl_is_valid char(1),
	wl_session_values long raw,
	wl_access_time number(20),
	wl_max_inactive_interval integer,
	primary key (wl_id,wl_context_path)
	);

JDBC复制
<session-descriptor>  
	<timeout-secs>300</timeout-secs>
	<invalidation-interval-secs>60</invalidation-interval-secs>
	<persistent-store-type>jdbc</persistent-store-type>
	<persistent-store-pool>SessionDS</persistent-store-pool>   ###配置JDBC DataSource的名字
	<persistent-store-table>wl_servlet_sessions</persistent-store-table>
</session-descriptor>

一个连接断，会自动切换到另一个


-----------------上 weblogic9.2 Cluster
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

启动日志中有　tcp://myHost:61616　    端口配置在activemq.xml中

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
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public static String getPingYin(String src)
{

	char[] t1 = null;
	t1 = src.toCharArray();
	String[] t2 = new String[t1.length];
	HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	//t3.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
	t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	String t4 = "";
	int t0 = t1.length;
	try
	{
		for (int i = 0; i < t0; i++)
		{
			// 判断是否为汉字字符
			if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
			{
				t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
				t4 += t2[0]+" ";
			} else
				t4 += java.lang.Character.toString(t1[i])+" ";
		}
		// System.out.println(t4);
		return t4;
	} catch (BadHanyuPinyinOutputFormatCombination e1)
	{
		e1.printStackTrace();
	}
	return t4;
}
String cnStr = "中华人民共和国";
System.out.println(getPingYin(cnStr));

