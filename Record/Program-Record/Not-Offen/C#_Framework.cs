
===================NUnit   .Net 的单元测试工具

加引用 时选择 nunit.framework,

using NUnit.Framework;

如一个类是测试类，在类前加		[TestFixure]
 测试方法前加					[Test]



Assert.AreEqual();
Assert.Greater();


Nunit 界面中 file ->open Project ->选择测试类生成的.exe文件

file->reload test //


方法前加	[ExpectedException(typeof (DiviedByZeroException))]   表示如果方法抛出DiviedByZeroException异常时，测试是OK的
			期待异常


右击项目－》调试－》选择 [启动名部程序]－》选择NUnit的执行文件

方法前加 [Setup]   ,在每个[Test] 方法之前都会来初始化
方法前加 [TearDown],在每个[Test] 方法之后都会来清理  

方法前加 [TestFixtureSetup]		,只为测试类执行一次的初始化
方法前加 [TestFixtureTearDown]  ,只为测试类执行一次的清理

方法前加 [Ignore("this is ignore reason")]  表示这个方法不会被测试，变黄色 

可以用[Test,Ignore] 
也可以用[Test][Ignore]
[Explicit] 显示的 ,表示，要单独选择这个方法来测试，如果选类不会测试
[Category("group01")]  会在NUnit界面中有categroy标签





Int32.MinValue  //最小值

StreamReader sr=new StreamReader(“c:/xx.txt”); //IO 流
string line=sr.ReadLine(); //读一行
string [] nums=line.Split(null); //只是由空格分隔
int res=Int32.Parse(nums[0]);//字符转数字

List<int> list =new List<int>();
list.Add(res);
int[] myarr=list.AoArray();//List 转成数组
sr.close();




Assert.IsNotNull(x);

选中类的字段->重构->封装字段->  确定   ，会生成set {} get{}

格式化代码  
1.编辑->高级->设置文档的格式 ctrl+k,ctrl+d
2.最后一个 } 删除后，再加上
3.选中所有的代码-》ctrl+x -> ctrl+v


SQL Server中 建用户时，不要选择[强制实施密码策略]   用户要用sysadmin 服务器角色

using System.Data.SqlClient;
using System.Data;

using Oracle.DataAccess.Client;//10gXe 名称空间
string str ="Data Source=xe;User Id=hr;Password=hr;";//OK ,xe中可以指向其它数据库,

string connStr = "server=localhost;uid=myuser;pwd=mypass;database=test";			  //OK
string connStr = "Data Source=localhost;Initial Catalog=cdb;User ID=sa;Password=sa";  //OK
string connStr = "Data Source=localhost;Initial Catalog=cdb;Integrated Security=True" //OK windows身份验证

SqlConnection  conn=new SqlConnection(connStr);
conn.Open();
string sql="insert into student (username) values (@myname)";
SqlCommand command =new SqlCommand(sql,conn);
command.Parameters.Add(new SqlParameter("@myname",SqlDbType.VarChar));
command.Parameters["@myname"].Value="lisi";
command.ExecuteNonQuery();  
conn.Close();

SqlDataReader reader =command.ExecuteReader();//如sql="select username from student";
if(reader.Read())//while
{
	Object obj=reader["username"];
}




NUnit   调试 在VS中  www.testdriven.net    TestDriven.Net工具 自带了NUnit
右击->Run Test   
右击->Test With->Debuger ，可加断点

测试覆盖率(百分之多少的代码是被测试过的)
右击->Test With->Coveriage   (%)
 
======================webORB   Flex 的 amf================
SWC文件位于 weborbassets\wdm\weborb.swc

.Net中新一个 类库
Environment.MachineName;
Environment.Version.ToString();
Environment.OSVersion.Platform.ToString();
Environment.OSVersion.Version.ToString();

复制生成的dll文件到 C:\Inetpub\wwwroot\weborb30\bin目录 ,也可以使用Deployment标签



Flex 中选择 ASP.NET 后,要自己手工在compiler选项中加 -services C:\Inetpub\wwwroot\weborb30\WEB-INF\flex\services-config.xml

如不能运行Logon.aspx
1.cd C:\WINDOWS\Microsoft.NET\Framework\v2.0.50727
2.aspnet_regiis.exe -i
才会有 Management标签

documentation->Real-Time Messageing只有.Net才有

RTMP 规范 (Real-time Maessage Protocal)
	 <channels>
          <channel ref="weborb-rtmp" />
        </channels>

var uri:String = ServerConfig.getChannel( "weborb-rtmp" ).endpoint; 
nc.connect( uri + "/Chat" );//对应 Applications 目录下的Chat
 
========================================IIS 插Tomcat

运行中输入inetmgr就可看到 IIS 的版本
http://localhost/iishelp 也可看到

IIS5.1 和apache-tomcat-6.0.14 成功

C:\temp\jakarta\ 下要有 4个文件,isapi_redirect-1.2.9.dll ,isapi_redirect.properties(可选reg文件),和两个配置文件

建如下的workers.properties文件：
	#让mod_jk模块知道TOMCAT
	workers.tomcat_home=E:\apache-tomcat-6.0.14		

	#让mod_jk模块知道JDK
	workers.java_home=C:\Program Files\Java\jdk1.6.0_12	

	#指定文件路径分隔符
	ps=\							
	worker.list=worker1

	#工作端口
	worker.worker1.port=8009

	#TOMCAt服务器地址（某JSP网站的ip地址）
	worker.worker1.host=localhost	
	
	#类型
	worker.worker1.type=ajp13
	
	#负载平衡因数
	worker.worker1.lbfactor=1				



创建如下的uriworkermap.properties文件：
	/*.jsp=worker1					####*/

	 #对应自己的TOMCAT 应用
	/axis2/*=worker1				####*/


方法一 isapi_redirect.properties文件名必须和ISAPI文件名一样(dll换成properties)，扩展名properties不能修改。此文件也可以使用配置到注册
	isapi_redirect-1.2.28.properties文件内容 OK的

	##jakarta为IIS下的虚拟目录
	extension_uri=/jakarta/isapi_redirect-1.2.28.dll

	##指定JK插件使用的日志文件
	log_file=C:\temp\jakarta\isapi.log

	##指定日志级别
	log_level=info

	##指定JK插件的工作文件
	worker_file=C:\temp\jakarta\workers.properties

	##指定JK插件的URL映射
	worker_mount_file=C:\temp\jakarta\uriworkermap.properties


方法二 修改注册表:创建HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Jakarta IsapiRedirector\1.0项;在1.0项下建立如下字符串:
	
	extension_uri		/jakarta/isapi_redirect-1.2.28.dll			
	log_file			C:\temp\jakarta\isapi.log					
	log_level			debug										
	worker_file			C:\temp\jakarta\workers.properties			
	worker_mount_file	C:\temp\jakarta\uriworkermap.properties		

也可以写成XX.reg 注册表文件,OK的
		Windows Registry Editor Version 5.00   
		[HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Jakarta Isapi Redirector\1.0]  
		"extension_uri"="/jakarta/isapi_redirect-1.2.28.dll"  
		"log_file"="C:\\temp\\jakarta\\isapi.log"  
		"log_level"="debug"  
		"worker_file"="C:\\temp\\jakarta\\workers.properties"  
		"worker_mount_file"="C:\\temp\\jakarta\\uriworkermap.properties" 




IIS中创建虚拟目录,名为"jakarta",路径为isapi_redirect-1.2.9.dll所在的路径
修改jakarta虚拟目录的属性.将其　执行权限　改为"脚本和可执行文件"(向导中　是　执行(如ISAPI 应用程序或 CGI)),


在网站上右键
在默认网站上右键---->属性---->ISAPI筛选里添加一个名为"jakarta"的筛选(名称任意,只是区分),文件为isapi_redirect-1.2.28.dll(要浏览)


重启IIS和TOMCAT,查看下刚才加入的筛选是否有一个绿色向上的箭头  (绿色箭头和虚拟目录无关,但仿问tomcat就要用了)
			http://localhost/axis2/					IIS中OK,会有isapi.log日志的

注意
	可能要设CATALINA_HOME,PATH,
	properties文件的注释#一定在放在首行
	IIS也要重启,


========================================IIS 插CGI perl 
在安装ActivePerl 时会create IIS虚拟目录 

右击默认网站->属性->主目录 标签-> 单击配置->添加

扩展名 .pl 
可执行文件:C:\Perl\bin\perl.exe %s %s

也要在建虚拟目录时,选中执行

一定要在后面加上" %s %s " ，不然没法执行cgi的
再添加.cgi就可以了

	如有下错误
	CGI Error
	The specified CGI application misbehaved by not returning a complete set of HTTP header
要在首行加 print "Content-type: text/html\n\n"; 










