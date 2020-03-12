http://msdn.microsoft.com/zh-cn/library/default.aspx   在线文档

SQL Server2008的NetFramework3.5SP1 在光盘位置   /x86/redist/DotNetFrameworks/dotNetFx35setup

如果先安装VS2008 ,再安装IIS-5,是不能用的
1.cd C:\WINDOWS\Microsoft.NET\Framework\v2.0.50727
  cd C:\WINDOWS\Microsoft.NET\Framework\v4.0.30319
2.aspnet_regiis.exe -i
就OK了


VS 2008 底部的面板 手工拖成最小,就打不开了,窗口->重置窗口布局
IIS中可直接右击网页->浏览,会自动打开IE,自动输入地址


C# (C Sharp,扩展名为cs)
C# 和C++  4 个+ ,2个+
Vista 之后,将停止windows API的更新

格式化代码 
1.删} 再加上
2.ctrl+a,ctrl+k+f
3.ctrl+k+d

using System;  //可以不写,默认是引入的
[新建控制台应用程序]
static void Main(string[] args) //程序入口点


alt+shift+f10 提示using

ctrl +k+w	完成代码
ctrl +k+p			提示参数

ctrl +j		完成代码
ctrl+shift+space	提示参数


优秀的CS  WebService 报表 开发

CLR 公共语言运行库 


命令行的方式
C:\WINDOWS\Microsoft.NET\Framework\v3.5\Csc.exe   编译器s

C# 和C 一样    是大小写敏感

--HelloWorld.cs
using System;
namespace HelloWorld
{
	class Program
	{
		static void Main(string[] args)
		{
			Console.WriteLine("Hello World!");		
		}
	}
}
---
Console.WriteLine("\nWhat is your name? ");
var name = Console.ReadLine();
var date = DateTime.Now;
Console.WriteLine($"\nHello, {name}, on {date:d} at {date:t}!");
Console.Write("\nPress any key to exit...");
Console.ReadKey(true);
--


/// 是XML 注释

System.Console.WriteLine("hh");
System.Console.WriteLine("{0} is not {1}",i,j);
int ch=Console.Read();  读一个字符
string str=Console.ReadLine(); 


using 后加 名称空间




int 是System.Int32

readonly 只读的变量 
virtual ,abstract


bool 布尔类型
string 
ulong 
值类型 存在 栈区     zhan  

引用类型 存在 堆区中 是用new 分配的


对值类型   如声明一个变量，再对它赋值 ，内存会产生两个 



decimal 128位 是double 64位 的两倍

有struct 结构体 和C 一样
enum WeekName
{
	星期一=1,
	星期二=2
}

@"this is c:\"  //用@ 可以包含一些特殊字符


Convert.ToBoolean(xx);
		.ToInt32(xx);


int val=100;
Object o=val;  //自动 装箱,拆箱


public const double PI=3.14;


internal 同一程序集中才可以仿问

public
protected internal  只可派生的或者当前程序集的
protected  只可包含类_或派生类
internal	只可当前程序集(同一个namespace )
private		只可包含类	


protected internal int m; //要继承, Project 中  可以仿问
internal int j;//同一个 namespace/Project 中  可以仿问


工具上有一个 [查看类关系]按钮 ,可以看UML 类图
工具栏上,[注释选中行] 按钮
右击->设置为启动项目
右击自己建立的 应用程序->属性->启动对像中 选择哪个Main方法

bool 默认是 false;

代码重购  改变量名时,后有一个小红方框,移上->[带预览的重命名]



  
static 构造函数 只执行一次,初始化 static 的变量 ，而且在最先执行,也可以和 非 static 构造函数并存
struct 可以有构造方法
{
};

String x="dxx";
x[2];//可以得到某个字符

int max(int a,ref int b ) //按引用传递 定义要ref 
int x=5;
max(3,ref x);//使用要ref 



Solution -> Project -> class 



(x==y)?xx:yy





#region 运算符重载
public static bool operator ==(MyVe one,MyVe two)//要用static 
{

}
#endregion 


//定义了== 的重载 也要定义 != 才可以,还有例如>=,<=  和>,<　　　,必须成对出现



++ ,--


//索引器


class Address
{
   internal string city;
   internal int code;
}
class Person
{
	public string name;
    public Address[] add;
	public Person()
	{
        this.name = "初始值";
        add = new Address[2];
	}
	public string Title             //就像用属性一样用它
	{
		get
		{
            return this.name;
		}
	}
	public Address this[int index]       //可以是string index,就可以用 对象[0], 对象["lisi"]
	{ 
		//可做一些对index判断
		get
		{
			return add[index];
		}
		set
		{
			 add[index]=value;//value  是传过来的值
		}
	}

    public  Address this[string index]       //可以是string index,就可以用 对象[0], 对象["lisi"]
    {
    
        get
        {
            foreach (Address temp in add)// foreach
            {
                if (temp.city == index)
                    return temp;
            }
            return null;
        }
       
    }
};
			Person p = new Person();
            Console.WriteLine(p.Title);
            Address home = new Address();
            home.city = "harbin";
            home.code = 10;
            p[0] = home;
            Console.WriteLine(p.[0].city);
			Console.WriteLine(p.["harbin"].city);




System.String
new String('c',3);//ccc
     private string[] str = new string[] { "x", "y" };//[] 只能放在string 后,不能放在str后

System.Exception
				.Message
				.Source  //导致异常的对象/程序名
				.TargetSite  //抛出的方法
				.StackTrace
				.HelpLink=myhelp.txt
			
SystemException  所有异常的基类
ApplicationException 


RandException rank 等级

记录异常 (文件记录异常,数据库记录 ,邮件发送记录)



byte[] b=System.Text.Enocding.Default.GetBytes("C#语言");


byte[] b=System.Text.Encoding.Unicode.GetBytes("C#语言");




继承用:  
覆盖方法用  public override  string xxx(xx)







 <asp:SqlDataSource EnableCaching="True"  CacheDuration="60"  >秒    
页面缓存 <%@ OutputCache Duration="20"  VaryByParam="*" %> 
		//	如是*　如向页面传不同的参数值，缓存失效了
			VaryByParam="None"

aspx 文件中下有.cs文件,它:System.Web.UI.Page
方法中可以仿问 aspx页面中的id =DateTime.Now.ToString();//ToLongTimeString  	


Request.Params["username"]  //得到请求的参数


VaryByParam="username"  //只对这个参数变化,才缓存失效了

CacheProfile="myProfile"

if( this.Cache["mykey"]==null)
{
  this.Cache.Insert("mykey","value");//增加
  this.Cache.Add("mykey","value");
}



SqlCommand("select ...",new SqlConnection(""))

aspx中
	Session["username"]
	Response.Redirect("xx.aspx")
	Response.Write("<script>alert('good');</script>");

SqlServer 支持 select * from xx where id=@myid 来传参

得到WEB程序的要目录   "http://" + HttpContext.Current.Request.Url.Host;    //值是 http://localhost
				 
		
					HttpContext.Current.Request.Url.Port.ToString()  //80端口




委托
public delegate  int Mydelegate(int a,int b); //类似于指向函数的批针

public static int max(int p1,int p2)
{
	return p1>p2?p1:p2;
}
Mydelegate mydel=max;  //如不是static ，对象.方法; 可以做为函数参数来传递
mydel(2,4);//

//可以数组
Mydelegate[] operations=
{
	new Mydelegate(max),
	new Mydelegate(min) //可以用new 
};

//可以加，减，
Mydelegate mymax= new Mydelegate(max);
Mydelegate mymin=new Mydelegate(min);
Mydelegate myall=mymax+mymin;//
myall(3,5);
Mydelegate myall=myall-mymin;//
myall(3,5);

//匿名 ，不用写方法名了
Mydelegate my=delegate(int x,int y)  //返回 不是void,可以吗？？？？？ 
	{
		return x>y?x:y;
	};
my(10,20)








---------------------
C# 和C++  4 个+ ,2个+
Vista 之后,将停止windows API的更新
using System;  //可以不写,默认是引入的
[新建控制台应用程序]
static void Main(string[] args) //程序入口点

public
protected internal  只可派生的或者当前程序集的
protected  只可包含类_或派生类
internal	只可当前程序集(同一个namespace )
private		只可包含类	

(x==y)有?xx:yy

工具栏上,[注释选中行] 按钮


static 构造函数 只执行一次,而且在最先执行,也可以和 非 static 构造函数并存





===========
~析构   方法默认是 private ......... ,IDisposable 接口,
:this (xx) //调用本类的其它构造 
:base(xx) //调用父类的构造
int.Parse("") //字符转 整型
namespace 中可以有点,org.zh
namespace 中可以嵌套 namespace
名称空间别名  using MY=System.Text; //别名

新建项目->C#->类库
引用中加dll库->可以选中它,在[对象浏览器中查看]

不能方法的返回类型定义重载


继承使用 ":"

public string CustomerName//属性
{
	get {return "xx";}
	set {name= "xx";}
}

public static readonly PI=3.14
uint x,y; //同时定义多个

decimal x =0.0m


shift +alt +enter 全屏
override 
virtual 如果父类,和子类有相同的方法 ,父类中使用 virtual ,子类同名方法可用 base.MyMethod()
		只是父类是的方法 virtual 子类才可以 override


new 让派生成员替换基成员/方法
public new  void  Method() //子类中使用
public new  static  xxx="cc"; 

System.Object 
virtual ToString()
	GetHashTable()
	Equals(Object obj)
static	Equals(Object obj,Object obj2) //会检查null 
	ReferenceEqual(Object o1,Object o2);
	Type GetType();
	Object MemeberwiseClone();
	Finalize();//清理资源时

鼠标移动到 小方块上可以自动实例抽像方法


密封类 sealed  不能被继承的
密封方法 防止派生类对方法的重载,必须 override 父类的 virtual 方法才可以用 sealed,(即 sealed 和 override 一起使用)





如果一个类实现两相不同的namespace 的接口,方法名相同,可以  void   接口.方法名(xx){..}




string 是 System.String 有多种构造方法
	.Compare //方法
StringBuilder   多有空格,少会截断



==================================================


Application.EnableVisualStyles();
//XP 风格,还要设置按钮的FlatStyle 样式为System

Panel 有一个属性Dock 设置改变窗口时，Bottom
Button 的　Author属性

Panel 　的BorderStyle　改FixedSingle 会有边框，也可改颜色


shift + 方向改大小
ctrl 

TextBox ．passwordChar
可选择多个组件复制其它的地方

可以增加->用户控件


Form 属性FormBorderStyle设置为None 没有标题栏　　
		Paint事件写函数名

using System.Drawing.Drawing2D;
using System.Drawing;

Rectangle.Inflact (-1,-1)	//放大,缩小，宽，高

RequiredFieldValidator  的属性ControlToValidate 设置为要验证的TextBox..,
								ErrorMessage未通过验证显示的信息
								Display="static" //始终占位,dynamic 如果有错误才占位,服务器端验证

CompareValidator  的属性ControlToValidate  
				ControlToCompare或者是ValueToCompare  ,
				Type 可是Integer,
				Oprator可是NotEqual,DateTypeCheck
RangeValidator 的MinmumValue,和maximumValue
RegularExpressionValidator 的validationexpression="^.d$"

CustomValidator   
			 ClientValidationFunction="myfun" //JavaScript验证函数
			 ServerValidate事件方法中　根据args.Value来判断，如果成功把args.IsValid=true 
<asp:ValidationSummary 
(Page)this.IsValidate //如果有任何一个未通过验证,返回false

this.ClientTarget = "uplevel";//客户端验证
this.ClientTarget = "downlevel";//服务器验证。




项目属性可以改Form图标


自定义控件可以新加属性set{},get{} ,默认是在杂项中,要
[Description("xxx"),Category("Apprearance")]    

,Apprearance是与现有组名一致,中文是外观


[DefaultEvent("Click"]放在自定义组件上,表示默认事件

this.OnClick(e) //EventArgs e


组件可以放在顶层/底层 按钮

public event System.EventHandler myhandler;//定义事件
myhandler(this, EventArgs.Empty);//方法中调用这句来发出这个事件 


car.myhandler+=new EventHandler(mymethod)	 ;//按提示按tab键，监听发生的事件
public static void mythod (Object sender,EventArgs e)//调用方法
{
．．．．	
}


using System.Data.OleDb;
using System.Configuration;

OleDbConnection conn=new OleDbConnection(ConfigurationSettings.AppSettings["ConnString"])
OleDbCommand command=new OleDbCommand();
command.CommandString="select ..";
command.CommandType=CommandType.Text;
commnad.Connection=conn;
OleDbDataAdapter adapter =new OleDbDataAdapter(command);
DataSet set=new DataSet();
adapter.Fill(set);
set.Tables[0];//DataTable

添加=>应用程序配置文件App.config  ,Access数据库
<configuration>
	<appSettings>
		<add key="ConnString" value="Provider=Microsoft.Jet.OLEDB.4.0;Data Source=my.mdb"/>
	</appSettings>


//===
System.Configuration.ConfigurationManager.ConnectionStrings["connStr"];
<asp:SqlDataSource ID="SqlDataSource1" runat="server" 
            ConnectionString="<%$ ConnectionStrings:connStr %>" 

web.config 文件中
<configuration>
	<connectionStrings>
		<add name="connStr" connectionString="server=localhost;uid=sa;pwd=sa;database=cdb" />
	</connectionStrings>


	Application.StartupPath  //项目目录


添加新项目=>其它项目类型=>安装和部署->安装项目 ok->右击应用程序文件夹->添加->项目输出->选择项目OK
右击项目属性->输出文件名->msi文件

右击应用程序文件夹->添加->文件->选择ico文件
用户桌面右击=>创建快捷方式 ->属性中设置icon
项目属性AddRemoveProgramsIcon ,是添加/删除程序中显示的图标
		Author 
		Manufacturer 制造商 是安装时的默认目录
		ProductName 
		Title
右击项目->生成



========WebSerivce

UDDI 发现WebService

Post方式请求WebSerivce  ,public string sayHello(string username); //username 要的传参数名
<form id="form1" action="http://localhost/mywebservice/Service1.asmx/sayHello" method="post">
   <input type="text" name="username" />
   <input type="submit" />
</form>


WebService客户端，加web引用,可以点[显示所有文件]按钮,->Reference.map->Reference.cs中定义了代理,封装了调用过程

可以返回 DataSet /DataTable


<%@WebService language="C#" class="MySession"%>
方法前加[WebMethod (EnableSession=true)]就可使用 Session["xx"]

[WebMethod (Discription="this is a discription")]


部署时,要asmx  文件,和bin目录,web.config
右击asmx文件->查看标记

windows应用程序使用WebService ->添加Web引用
localhost.Service1 service = new localhost.Service1();
DataSet ds=service.getStudents();
dataGridView1.DataSource = ds.Tables[0];


using System.Runtime.Serialization.Formatters.Binary;
using System.IO;
using System.IO.Compression;

//序列化，网络速度快
DataSet ds = getStudents();
BinaryFormatter formatter = new BinaryFormatter();
MemoryStream ms = new MemoryStream();
formatter.Serialize(ms, ds);　//把DS序列化
byte[] buffer= ms.ToArray();



//压缩
  //---  
MemoryStream ms = new MemoryStream();
GZipStream compressedzipStream = new GZipStream(ms, CompressionMode.Compress, true);
compressedzipStream.Write(buffer, 0, buffer.Length);//全部
compressedzipStream.Close();


//解压缩
ms.Position = 0;//同一个MemoryStream,如不是同用 MemoryStream(byte[]);
GZipStream zipStream = new GZipStream(ms, CompressionMode.Decompress);
int buffer_size=1024;
byte []res=new byte[buffer.Length + buffer_size*5];//解压变大的
int readed=0;
int offset=0;
while (true)
{
	readed=zipStream.Read(res, offset, buffer_size);
	if(readed==0)
		break;
	offset=+readed;
}
zipStream.Close();



//反序列
BinaryFormatter formatter = new BinaryFormatter();
MemoryStream ms=new MemoryStream(buffer)//要反的
DataSet ds=formater.Deserialize(ms) as DataSet;


//========异步WebSerivce

VS2008中调试Web/WebService时,设置为主项目,启用调试按钮,会提示修改web.config文件
<compilation debug="true">



ds.Tables[0].Rows.Count;//行数

copy=SqlBulkCopy(conn)//类只能向 SQL Server 表写入数据
copy.DestinationTableName=mytable //要加内容表名
copy.WriteToServer(DataTable x);//x的内容追加到mytable表尾部
copy.Close();

SqlDataAdapter 的Fill(DataTable x)   ,是表结构  DataTable有一个Clear()
DataTable 的.Columns[i].ColumnName;
DataTable 的.Columns[i].SetOrdinal(i)//改变该字段的顺序
ds.Clear();
ds.Reset();
ds.Dispose();


GC.Collect();//垃圾回收

右击WebSerivce项目->发布->...->本地IIS->


TextBox 设置　multiline 为true

DataTable dt= conn.GetSchema("Tables") //所有的表
listBox1.DisplayMemeber="TABLE_NAME"  //ListBox listBox1,TABLE_NAME 是dt里的列名
listBox1.DataSource=dt;


每个WebSerivce的方法都有一个 [方法名]Completed 事件

myService.[方法名]Completed+= new [方法名]CompletedEventHandler(mymethod)
				mymethod中的参数为(	Object sender,[方法名]CompletedEventArgs  e)//e.Result

System.Enviroment.NewLine;//换行

myService.[方法名]Async()//异步调用WebService

DataRowView  row=(DataRowView)listBox1.items[listBox1.SelectIndex];//DataTable 的每一行是DataRowView
row["TABLE_NAME"]


//========动态WebService
static class //静态类
{
};

自动生成方法的注释 快捷键:::::::::

using System.IO;
using System.Configuration;
using CodeDom;
using CodeDom.Compiler;
using System.Net;
using System.Web.Services;   //添加引用System.Web.Service
using System.Web.Services.Description;
using Microsoft.CSharp;

string @namepsace="org.zh";
WebClient wc=new WebClient();
Stream stream=wc.OpenRead(url +"?WSDL");
ServiceDescription sd=ServiceDescription.Read(stream);
ServiceDescriptionImporter importer=new ServiceDescriptionImporter();
importer.AddServiceDescription(sd,"","");
CodeNamespace cn=new CodeNamespace(@namespace);

CodeCompileUnit unit=new CodeCompileUnit();
unit.Namespaces.Add(cn);
importer.Import(cn,unit);
CSharpCodeProvider provider=new CSharpCodeProvider();
ICodeCompiler compiler=provider.CreateCompilier();

CompilerParameters param=new CompilerParameters();
param.GenerateExecutable=false;
param.GenerateInMemory=true;
param.ReferencedAssemblies.Add("Sytem.dll");
param.ReferencedAssemblies.Add("Sytem.XML.dll"); //WebService 如用到了using System.xxx ,就要加库
param.ReferencedAssemblies.Add("Sytem.Web.dll");
param.ReferencedAssemblies.Add("Sytem.Data.dll");


CompilerResult result=comipler.CompileAssemblyFromDom(param,provider);
if(true==result.Errors.HasErrors)
{
	foreach (CompilerError err in result.Errors)
	{
		//处理错误
	}
}


System.Reflection.Assembly assembly =result.ComipledAssembly;
Type type=assembly.GetType(@namespace +"."+classname,true,true);//classname 类名
Object obj=Activator.CreateInstance(t);
System.Reflection.MethodInfo methodInfo=type.GetMethod(methodname)//methodname 方法名
return methodInfo.Invoke(obj,args);//args方法参数




//===============

自定义[Web用户控件]  *.ascx   
<%@ Control Language="C#" 
继承自 UserControl


<%@ Register TagPrefix="prefix" TagName ="Tag" Src="Hello.ascx"%>
这样使用<prefix:Tag runat="server"/>

外部要仿问自定控件内部,只能提供 public string get {},
	如有 set{} 就可以在使用定义标签 中加属性 <prefix:Tag runat="server" user=""/>


TagName 也可以用tagname asp是不区分大小写的


<%@ Application Inherits="MyObject" Description="xxs"
<%@ Import namespace="System.Data.SqlClient" %>// 一个Import只能一个 namespace


Global.asax文件  [全局应用程序类]  一个项目只能有一个Global.asax,内有 Session_Start...方法

web.config文件中
<configuration>
    <system.web>
		<compilation debug="false">								 <!--可以调试-->
		<customErrors mode="On" defaultRedirect="error.html">    <!--错误页面 -->
			<error statusCode="404" redirect="notfound.html"/>
		</customErrors>  
		<globalization  requestEncoding="gb2312" responseEncoding="gb2312"/>

		<authentication mode="Forms"  >//Passport,Windows
			 <forms loginUrl="" timeout=""/>
		</authentication> 





<asp:AdRotator ImageUrlField="" NavigateUrlField="单击时去的url" AlternateTextField="" KeywordFilter=""  />

<asp:AdRotator  AdvertisementFile="my.xml" BorderColor="black" BorderWidth=1 runat="server"/>
				 AdvertisementFile="~/my.xml"  //~表示项目录
下面是my.xml
<Advertisements>
  <Ad>
    <ImageUrl>./image/t2.gif</ImageUrl>
    <NavigateUrl>http://www.yesky.com</NavigateUrl>
    <AlternateText>欢迎访问t1！</AlternateText>
	<Impressions>20</Impressions>  <!-- 网页刷新时的显示的机率 -->
	<Keyword>mygroup</Keyword>  <!--KeywordFilter 的值来显示部分 -->
  </Ad>
</Advertisements>




消息队列



程序集，
<%@ Assembly Name="System.Xml.dll" %>
<%@ Assembly Src="sources/SomeSourceFile.cs" %>

web.config中
<compilation debug="true">
		<assemblies>
			<add assembly="System.Core,





aspx文件中可以用  <script runat="server" language="c#"> 来代替 <% %>
  public void Page_Load(Object Source, EventArgs E)
    HttpBrowserCapabilities bc= Request.Browser;
 bc.Browser 和 bc.Version  //可以得到用户客户端信息






System.Net.Sockets.TcpClient tcpc = new TcpClient();
tcpc.Connect(strServer,intPort); //可以测试那个机器有没有开什么端口
Stream s = tcpc.GetStream();
tcpc.Close();





-----------================

右击网页->设置为首页


查看页面的源码时发现有一个hidden的 __VIEWSTATE 存表单值
<%@Page 中加入 EnableViewState="false" ,一个页面只能有一个<%@page
 
Page的属性IsPostBack  可以防止每次执行Page_Load
第一次进入时反回false,再单击submit后变为true

myButton.Attibutes.Addd("onclick" ,"window.close()"); //myButton 是   <asp:Button ID="myButton"

ASP.NET  对象Server ,ObjectContext 
Response对象的Write(""),Redirect("");

Request["username"].ToString();//获取表单,POST
Request.Form.Get("password").ToString();//POST
Request.Form.["password"].ToString();//POST

Request.QueryString["age"].ToString(); //GET

<form runat="server">一个页面只能有一个runat="server"的form
Command的executeScaler()返回第一行,的第一列,Object类型,

<asp:HyperLink  不会传递给服务器
<asp:LinkButton 会传递给服务器
<asp:Button 有CommandName ,可以区分同一处理事件的不同按钮
<asp:ImageButton  事件参数 是 ImageClickEventArgs e ,e.X 是从图像左上角开始, 可以根据区域不同

SqlDataReader reader=sqlCommand.ExecuteReader(); //可while( reader.Read()){reader["num"].ToString()}
ListBox1.DataSource=reader;
ListBox1.DataTextField="";
ListBox1.DataValueField="";
ListBox1.DataBind();

<asp:Panel   Visible="False" 是一个容器  可以隐藏,来做注册的下一步
<asp:AdRotator  广告 
<asp:Calendar  SelectedDate属性.ToShortDateTime

this.File1.PostedFile.SaveAs("./upload");//表单只可以是Post才行的
this.File1.PostedFile.FileName//只是文件名字
Server.MapPath("upload")//得到服务器的绝对路径



Response.End();
Response.Cookies;


application 是 HttpApplicationState 它的实例


(Page)this.Application["xx"]=yy;
(Page)this.Application.Add("xx",yy);


属性Content,AllKyes,Count,StaticObject 
Lock() ,Unlock();

一个IIS 只有一个Server,但可以有多相Web项目即Application
Server 是 HttpServerUtility 的实例


Server.MapPath
Server.MachineName
Server.ScriptTimeout //请求超时值 
Server.Execute("")//去执行另一个页面返回结果
Server.Transfer("")//跳转页面,客户端的URL并不会改变
Server.HTMLEncode()  //HTMLDecode  可以在页面上显示< >
Server.URLEncode()	//URLDecode  可以在页面上显示 http:// ?  & 


session是 HttpSessionState 类的实例

Session.SessionID
Session.Timout
Session.Count //集合的个数



Context. //获取HTTP请求的信息 ,可以得到Server,Applicaion,Session,


DataSet 范围大于 DataTable -> DataRow/DataCoumn
dataSet.Tables.Add()

连接Oracle 可以用 System.Data.OleDb.OleDbConnection 也可以用 
ODBC 连接数据库


C#连接Oracle  ,要把OracleXE 安目录的bin下的Oracle.DataAccess.dll 加入到引用中

using Oracle.DataAccess.Client;//10gXe 名称空间
string str ="Data Source=xe;User Id=hr;Password=hr;";//OK ,xe中可以指向其它数据库,

//SQLServer
string connStr = "server=localhost;uid=myuser;pwd=mypass;database=test";			  //OK
string connStr = "Data Source=localhost;Initial Catalog=cdb;User ID=sa;Password=sa";  //OK
string connStr = "Data Source=localhost;Initial Catalog=cdb;Integrated Security=True" //OK windows身份验证


//事务
SqlTransaction trans=  conn.BeginTransaction(IsolationLevel.ReadCommitted,"savePoint");
trans.Save("savePoint1");
trans.Rollback("savePoint");
trans.Commit();
trans.Rollback();



OracleConnection conn = new OracleConnection(str);
conn.Open();
OracleCommand command = conn.CreateCommand();
command.CommandText = "select * from employees";
command.ExecuteReader();

OracleDataAdapter OracleDataReader

SQL> select userenv('language') from dual;  //本机10gxe版,不会出现乱码现像
	SIMPLIFIED CHINESE_CHINA.AL32UTF8

DataView view=new DataView(mydataset.Tables["employees"]); //使用 DataView 前必须设置 DataTable
view.RowFilter = "empolyee_id>120";
view.Sort = "salary ASC";
this.datagrid.DataSource = view;


SqlDataReader  不缓冲数据,占用数据库的连接
GetDateTime();GetXXX();


FileStream file = new FileStream(Server.MapPath("xx.xml"),FileMode.Open,FileAccess.Read);
StreamReader reader = new StreamReader(file);
DataSet dataSet = new DataSet();
dataSet.ReadXml(reader); //对XML文件,可以使用其它重载
new DataView(dataSet.Tables[0])

TextBox 的 AutoPostBack设置为true  //true失去光标焦点时,会产生TextChanged事件,false只有回车才行
Page.DataBind();//对页面上的所有的组件
Label 有一个DataBinding事件


<%#TextBox1.Text %>  //# 绑定表达式
<%#TextBox1.Text +" " %> //可以有运算符
<%#GetMyData() %> //方法绑定 



DataList1.DataSource=;
<asp:DataList id="DataList1" runat="server"> //数据组中
	<ItemTemplate>
		<%# (DataRowView)Container.DataItem["salary"] %>
		<%# DataBinder.Eval(Container.DataItem,"column_name","yy{0}")%>  //{0} 表示column_name的值 
			//,{0:c} currency ,{0:p} percent 无效的
	</ItemTemplate>
</asp:DataList>


DataTable table = new DataTable();
table.Columns.Add("num");//新列
DataRow row = table.NewRow();//新行
row["num"] = 2; 
table.Rows.Add(row); //要加才行


<asp:Repeater ID="Repeater1" runat="server">
	<ItemTemplate></ItemTemplate>
	<AlternatingItemTemplate></AlternatingItemTemplate> <!-- 不写绑定表达式的话,会少值的-->
	<HeaderTemplate></HeaderTemplate>
	<SeparatorTemplate></SeparatorTemplate>
	<FooterTemplate></FooterTemplate>
</asp:Repeater>




SqlDataAdapter adapter.SelectCommand=new SqlCommand("select * from employees")
adapter.Fill(dataSet,"tem_name");
dataSet.Tables["tem_name"]


System.Web.UI.WebControls.PagedDataSource page=new PagedDataSource();
page.DataSource=dataSet.Tables["employees"].DefaultView();
page.AllowPadding=true;  //充许分页
page.PageSize=20;
page.CurrentPageIndex=2; //从0开始的
page.PageCount; //总页数

Repeater1.DataSource = page;





<asp:DataList >
	<SelectedItemTemplate> 
	<EditItemTemplate>
</asp:DataList>
可以右击->自动套用模板 || 编辑模板 | 结束模板编辑

LinkButton 的 CommandName属性　
DataBinder.Eval(Container.DataItem,"firstname","{0:D}") // Date日期显示


DataList有一个ItemCommand事件,对一行有多个按钮事件的区分
  this.DataList1.SelectedIndex= e.Item.ItemIndex;
DataList有一个EditCommand,  (CommandName 为 "edit" ,或者用if)
  this.DataList1.EditItemIndex= e.Item.ItemIndex; (-1哪个也不编辑)
DataList有一个UpdateCommand,CancelCommand
		DeletedCommand

DataList1.DataKeyField="id"
DataList1.DataKeys[]
e.Item.FindControl("txt"); //一行中组件的id="txt"


如　CommandName="edit" 的按钮被点击，会触发oneditcommand，也可以在方法中判断//显示修改
CommandName="item"　会　onitemcommand  //查看
CommandName="update"　会　onupdatecommand//确认修改
CommandName="cancel"　会 oncancelcommand　//取消修改



 DataList1.DataKeyField = "id";
 string id = this.DataList1.DataKeys[e.Item.ItemIndex].ToString();//DataListCommandEventArgs e
 this.DataList1.EditItemIndex = e.Item.ItemIndex;
 this.DataList1.SelectedIndex=-1;  //否则默认是显示SelectedItemTemplate,-1 表示全部不显示/编辑


"{0:D}"
"{0:F}"
"{0:G}"
"{0:M}"
"{0:R}"
"{0:T}"
"{0:U}"
"{0:Y}"
"{0:N}"


<asp:DataGrid 分页没有用, 有一个PageIndexChanged事件 
		dataGrid1.CurrentPageIndex = e.NewPageIndex;

	ItemStyle中Height来设置行高 ,HeaderStyle,FooterStyle,PageStyle
	GridView1.Columns[0].Visible = false;//第一列不可见,但存在

	ItemDataBound事件  
		if (e.Item.ItemType == ListItemType.Item || e.Item.ItemType == ListItemType.AlternatingItem)
		{
		e.Item.Attributes.Add("onmouseover", "c=this.style.backgroundColor;this.style.backgroundColor='#0000ff'");//JS
		e.Item.Attributes.Add("onmouseout", "this.style.backgroundColor=c");//JS
		((LinkButton)(e.Item.Cells[0].Controls[0])).Attributes.Add("onclick","return confirm('delete?')") //删除加 alter
			
		}
超级链接列_HyperLinkColumn->URL字段_(id) ->URL字符串_(result.aspx?id={0})  //详细信息页
<asp:HyperLinkColumn DataNavigateUrlField="id" 
                DataNavigateUrlFormatString="result.aspx?id={0}" DataTextField="id" 
                HeaderText="详细" Target="_blank">

自动生成列会排序的,BoundColumn,会SortCommand事件
	e.SortExpression 是下面的值
<asp:BoundColumn SortExpression="age",一般是数据库字段
<asp:DataGrid  AllowSorting="True"  AllowPaging="True"

ViewState 存在客户端  form中有一个__VIEW来记录
if(ViewState["myOrder"]==null)
{
ViewState["myOrder"]="ASC";
}else if(ViewState["myOrder"].ToString()=="ASC")
{
	ViewState["myOrder"]="DESC";
}else if(ViewState["myOrder"].ToString()=="DESC")
{
	ViewState["myOrder"]="ASC";
}
dataSet.Tables["stu"].DefaultView.Sort=e.SortExpression+" "+ViewState["myOrder"].ToString();


属性生成器->列->删除||编辑/更新/取消
不要编辑的列可以只读 ReadOnly="True"
更新使用验证 选列-> 将列更转换为模板列->就可以编辑模板了,可以拖动Validator了
也可以把 编辑/更新/取消 ->将列更转换为模板列 选中更新按钮->CasesValidation="true" 表示使用验证

增加模板列加可以一个空列,可以其它中加 CheckBox,RadioButton
 foreach  (DataGridItem item in this.dataGrid1.Items)
{
	CheckBox box=(CheckBox)item.FindControl("chk");//id 为chk 
	if(box.Checked)
	{
		Response.Write(item.ItemIndex);
	}
	
}

新建项目时可以选择 [添加入解决方案]


System.Web.Security.FormsAuthentication.SignOut(); 

web.config中

//mycookiename是Cookie的名字  ,All 即加密,也传输验证
//不使用数据库验证网站
<system.web>
    <authentication  mode="Forms">
      <forms name="mycookiename" loginUrl="login.aspx" protection="All">
		<credentials passwordFormat ="Clear"> <!---明文传输,MD5 SHA1 下面的password是加密的-->
          <user name="user1" password="user1passwd"/> 
          <user name="user2" password="user2passwd"/>
        </credentials>
	  </forms>
    </authentication>
	<authorization>
      <allow users="user1,user2"/>
      <deny users="*"/> 
	  deny users="?"/><!--  ?代表匿名 -->
    </authorization>

System.Web.Security.FormsAuthentication.Authenticate("username","password");//来使用web.config的配置验证用户登录

	//如果验证成功把用户名保存到Cookie中
	System.Web.Security.FormsAuthentication.SetAuthCookie("username",false);//不跨浏览器保存Cookie
	//用户名保存到Cookie中,也转到登录前的页面,好像不行啊
	System.Web.Security.FormsAuthentication.RedirectFromLoginPage("username",false);


string enc=System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile("mypass","MD5")//生成加密

this.Button1.OnClientClick = "sayHello()"; //会调用页面中的JS 的函数
this.Button1.Attributes.Add("onclick", "sayHello()");//效果一样的也是JS


新建项目->Web安装项目,选中[Web应用程序文件夹]->属性VirtualDirectory   ,安装时的默认值 
右击Web应用程序文件夹->添加->项目输出-> 可以选择[主输出(bin目录的dll)],[内容文件],配置中选择 Release(发布版本),在对应项目->属性->生成中也要改为Release

项目中属性中可以更改输出 exe/msi文件路径

生成两个项目,注意顺序

==========国际化

<%@page中加 Culture = "en-US" UICulture= "en-US"

global.asax Session_Start文件中加入

using System.Globalization
CultureInfo ci=null;  // Culture 文化
try
{
	ci= CultureInfo.CreateSpecificCulture(Request.UserLanguages[0]);
}
catch
{
	ci = new CultureInfo("en-US");
}
Thread.CurrentThread.CurrentCulture = ci;
Thread.CurrentThread.CurrentUICulture = ci;


=========线程
using System.Threading;


=========

System.Collections.Hashtable
ArrayList 



decimal x=2.32m; //m结尾

checked   //如果溢出,CLR会抛异常
{
	byte b=255;
	b++ //默认unchecked会所溢出位丢掉
}    



int i=0;
i is Object//返回true

sizeof ,typeof


public static explicit operator RMB(float f) //explicit(清楚的)  ,把float 转换成 RMB,必须使用强制转换
{
	uint yuan = (uint)f;
	uint jiao = (uint)((f - yuan) * 10);
	uint fen = (uint)(((f - yuan) * 100) % 10);
	return new RMB(yuan, jiao, fen);
}
public static implicit operator float(RMB rmb)//implicit (暗示的)
{
	return rmb.Yuan + (rmb.Jiao/10.0f) + (rmb.Fen/100.00f);
}
RMB r3, r4;
float f = r3;// 隐式转换
r4 = (RMB)f;// 显式转换



//预处理指令
#define  LOGLEVEL
#undef  LOGLEVEL //必须放在C#源码的开头

#if LOGLEVEL && (OTHER==false)
	//...
#elif OTHER
	//...
#else 
	//...
#endif

#warning "this is a waring message"
#error "this is a error message"

#line 22 xx.cs
#line default //行号恢复默认


System.Text.RegularExpressions.Regex;
MatchCollection matches = Regex.Matches(str,pattern,RegexOptions.IgnoreCase);
            


//AllowMultiple表示是否可以出现多次,即两个[My]
//Inherited 是否也对继承的生效
//Property 只可以是有get{},set{}的属性  ,AttributeTargets.Method|AttributeTargets.Class
[AttributeUsage(AttributeTargets.Property,AllowMultiple=false,Inherited=false)]
class MyAttribute : Attribute  //生成[My]
{
	private string username;
	public MyAttribute(string username)
	{
		this.username = username;
	}
}




using System.Reflection;
System.Type type = typeof(int);
int i = 0;
System.Type type = i.GetType();
Type t=Type.GetType("System.Double");
MethodInfo method=t.GetMethod("ToString");
			method.Invoke();
t.GetEvents();
t.GetConstructor();
t.GetCustomAttributes(); //[My]

MemberInfo member=t.GetMember();//可以是构造,方法,属性,字段  t.InvokeMember()
FieldInfo field=t.GetField();
PropertyInfo property=t.GetProperty();



Assembly.Load("MyClass");


System.Diagnostics.Process.GetCurrentProcess();//当前进程


as 强转


//LINQ 查询表达式
int[] scores = new int[] { 97, 92, 81, 60 };
IEnumerable<int> scoreQuery =
            from score in scores
            where score > 80
            select score;




command.CommandType = CommandType.StoredProcedure;//存储过程

Page.ClientScript.RegisterStartupScript(this.GetType,"myKey","<script>alert('hello')</script>"); //

C:\WINDOWS\Microsoft.NET\Framework\v2.0.50727\System.Data.dll 文件是程序集












<asp:DataList >
	<SelectedItemTemplate> 
	<EditItemTemplate>
</asp:DataList>
可以右击->自动套用模板 || 编辑模板 | 结束模板编辑

LinkButton 的 CommandName属性　
DataBinder.Eval(Container.DataItem,"firstname","{0:D}") // Date日期显示


DataList有一个ItemCommand事件,对一行有多个按钮事件的区分
  this.DataList1.SelectedIndex= e.Item.ItemIndex;
DataList有一个EditCommand,  (CommandName 为 "edit" ,或者用if)
  this.DataList1.EditItemIndex= e.Item.ItemIndex; (-1哪个也不编辑)
DataList有一个UpdateCommand,CancelCommand
		DeletedCommand

DataList1.DataKeyField="id"
DataList1.DataKeys[]
e.Item.FindControl("txt"); //一行中组件的id="txt"







 <connectionStrings>
        <add name="tempdbConnectionString" connectionString="Data Source=localhost\MSSQLSERVER;Initial Catalog=tempdb;Integrated Security=True"
            providerName="System.Data.SqlClient" />
    </connectionStrings>


   <asp:SqlDataSource ID="SqlDataSource1" runat="server" 
        ConnectionString="<%$ ConnectionStrings:tempdbConnectionString %>" 
        SelectCommand="SELECT [id], [username] FROM [student]"></asp:SqlDataSource>




<asp:DataGrid  有一个PageIndexChanged事件
	ItemStyle中Height来设置行高 ,HeaderStyle,FooterStyle
GridView1.Columns[2].Visible = false;

System.DateTime.Now.ToString("yyyyMMddhhmmss") ;
===========文件上传
<form id="form1" runat="server" enctype="multipart/form-data">
	<asp:FileUpload ID="FileUpload1" runat="server" />
	//如何加图和字段
FileUpload1.SaveAs(Server.MapPath("files/"); + FileUpload1.FileName);//一句完成上传
          

 if (FileUpload1.HasFile)
		System.IO.Path.GetExtension(FileUpload1.FileName).ToLower();
===========文件下载
 string  filepath = "c:\\temp\\日.jpg";
int BUF_SIZE = 10;
byte[] buffer = new Byte[BUF_SIZE];

string filename = System.IO.Path.GetFileName(filepath);
System.IO.Stream iStream = new System.IO.FileStream(filepath, System.IO.FileMode.Open, System.IO.FileAccess.Read, System.IO.FileShare.Read);
long dataToRead = iStream.Length;

Response.ContentType = "application/octet-stream";
Response.AddHeader("Content-Disposition", "attachment; filename=" + filename);
while (dataToRead > 0)
{
	if (Response.IsClientConnected)
	{
		int length = iStream.Read(buffer, 0, BUF_SIZE);
		Response.OutputStream.Write(buffer, 0, length);
		Response.Flush();
		dataToRead = dataToRead - length;
	}
	else
	{
		dataToRead = -1;
	}
}




















