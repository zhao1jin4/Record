===============================Flex Builder 3

http://livedocs.adobe.com/flex/3_cn/langref/package-summary.html  //在线中文文档
Flex Builder 3 可以选中类 按F1就出现对应的文档,不用在线,还有其它的教程


Array 的 pop  删最后个,shift删第一个，unshift 把元素放在开头
		slice(startIndex,endIndex)  返回部分，不修改原
		splice(startIndex,deleteCount,..) 修改数组
			deleteCount 为０不删，不给删到尾
		-1 是数组的最后一个元素
sort(Array.NUMERIC|Array.DESCENDING) 排序
reverse();
push ({name:"lisi",age:20})//数据是对象　　　length 


arguments 和JS 中

uint 无符号整型

XML  XMLList 

观察者模式  Listener


buttion.addEventListener(MouseEvent.CLICK,doSomeThing,useCapture默认是false);//关闭捕获,如是ture 下两相阶段不会被处理 

internal fucntion onPress(evt:MouseEvent):void

1 捕获阶段 (EventPhase.CAPTURING_PHASE)。
2 目标阶段 (EventPhase.AT_TARGET)。
3 冒泡阶段 (EventPhase.BUBBLING_PHASE


evt.bubbles //是否冒泡
evt.target  //目标对象
evt.currentTarget//当前目录
evt.eventPhase //所处阶段   1,2,3 
evt.currentTarget//当前对象






第三个参数 priority 数值越大,优先级越高,   对同一个按钮加多个相同的单击事件,用不同的方法
第四个参数 weakReference  ,如是true ,会被垃圾回收器,来释放资源

EventDispatcher
	button.addEventListnener("myEvent",doSome);//自定义事件


	button.dispatchEvent(new Event("myEvent",true,false))//放在事件流中,其它组件如也监听myEvent,会响应
								if(evt.currentTarget==evt.target)	

	evt.type 事件名
	
Event preventDefault 取消默认
	isDefaultPrevented():Boolean 

evt.stopImmediatePropagation();
evt.preventDefault();


List addChild (Label)   Label -> DisplayObjectContainer->DisplayObject

----Flex4中无效
var css:CSSStyleDeclaration=new CSSStyleDeclaration("mystyle")
css.setStyle("color","blue");//0xffffff
css.setStyle("font",15);
mx.styles.StyleManager.setStyleDeclaration("Panel",css,true);//针对Panel类型,立即生效,也可用.开头的名字
----Flex4中无效
backgroundImage: Embed(source="smoke_bg.jpg");   //Embed  导入图片

compc命令
项目->属性->Flex Compler->addtional compiler arguments 中加入-theme Smoke.css    (路径是相对src目录的)

创建自己的主题 (swc文件)改文件后 用  (eclipse文档中有)
compc -input-file mycss.css c:/mycss.css(指全路径) 
	-input-file img.jpg c:/img.jpg(指全路径) 
	-include-classes DedomClass
	-o c:/mystyle.swc 
配置文件编译
compc -load-config myconfig.xml
查flex_4.6_help有很多资料

horizontalGap每个组件间的距离
paddingLeft 所有的组件和ApplicationControlBar的距离
dock="true"  锁定到顶部

<mx:ApplicationControlBar paddingLeft="" horizontalGap="" dock="true"
 
<mx:Panel> 比 <mx:Canvas>多一个 title  ,多一个status

<mx:TitleWindow showCloseButton="true" close="closeMyself()"  //只有标签有close事件,类没有

var win:TitleWindow=new TitleWindow();
win.title="标题";
var p:Point =new Point();
var p1:Point =win.localToGlobal(p);

PopUpManager.addPopUp(win,this,true);

PopUpManager.removePopUp(this);


<mx:VBox
<mx:HBox
<mx:HDividedBox  liveDragging="true"  时时显示拖动效果
<mx:VDividedBox

<mx:Form>
	<mx:FormHeading>
	</mx:FormHeading>
	<mx:FormItem>
	</mx:FormItem>
</mx:Form>



property 指定对象的属性名,进行验证
<mx:StringValidator   source="{myid}" property="text"  minLength="3" maxLength="20" tooLongError="too long" tooShortError="too short">
</mx:StringValidator>
<mx:DateValidator source="{}" formatError="error" property="text">
</mx:DateValidator>

<mx:NumberValidator integerError="not a number" invalid="Alter.show('error')" minValue="20" maxValue="100" domain="int" property="text">
</mx:NumberValidator>
 domain="real"

<mx:EmailValidator></mx:EmailValidator>




<mx:Tile direction="horizontal">
	...
</mx:Tile>

<mx:Grid>
	<mx:GridRow >
		<mx:GridItem colSpan="2" rowSpan="2">
		.....
		</mx:GridItem>
	</mx:GridRow>
</mx:Grid>


<mx:According>   QQ面板  selectedIndex=0
<mx:TabNavigator>  标签面板 selectedIndex=0
<mx:ViewStack>            selectedChild=myid
 


-------
<mx:EmailValidator trigger="{myButton}" triggerEvent="click" source="{myemail}" property="text" valid="Alert.show('success!!!')" />

<mx:DateValidator allowedFormatChars="/" 

<mx:CurrencyValidator precision="2"  两位小数 可加$
<mx:NumberValidator domain="text" 

<mx:RegExpValidator valid="handleResult(event)" invalid="handleResult(event)"
				eventObj:ValidationResultEvent

	eventObj.type == ValidationResultEvent.VALID

xResult:RegExpValidationResult=eventObj.results[0]
xResult.matchedIndex
xResult.matchedString 


<mx:SetProperty
<mx:SetStyle 

<mx:states>
	<mx:State name="" 后可以用设计视图
		click="currentState=''"
<mx:AddChild creationPolicy="all/none/auto"  position="before/after/firstChild/lastChild"

<mx:SetEventHandler target="{}" name="click" handlerFunction="" //handler=""
	<mx:handler>navigateToURL(new URLRequest(""))</mx:handler>

<mx:Transition fromState="*" toState="xx"

<mx:AreaSeries  form="segment|curve|

<mx:horizontalAxisRenderers>
 <mx:AxisRenderer axis="{haxis}" canDropLabels="true"/>   防止下方的图标过挤
</mx:horizontalAxisRenderers>

[Embed(source='../xxx.swf')]    不可以是flv格式的  ，可嵌入字体
[Bindable]
public var swf:Class;


<mx:Repeater 


<mx:PrintDataGrid    多页打印 
	dataProvider=
	validateNextPage==true?
	nextPage();



includeInLayout=false; 

validateNow();   //验证组件布局

var print:FlexPrintJob=new FlexPrintJob();
print.pageWidth=
print.pageHeight=
print.addObject(xx)

Application.application.addChild(xx)
			.removeChild(xx)
print.send();



OLAP   使用


----------------FABriage 有例子
HTML 中加swf  ,<object >
		<param name="flashvars" value="brigeName=b_app" />  对应as 文件中的FABriage.b_app.root();


<mx:DateGridColumn editable="true" itemEditor="mx.controls.NumericStepper" editorDataField="value"  />
				   itemRender=""

<mx:List id="myList" initialize="init();"
myList.itemRenderer = new ClassFactory(XX:Class);  //在XX的mxml 中{data.yy}  data表示DataProvider 的每个对象,后再用属性
		      TextInput
查itemRenderer 文档上有例子

itemFunction="myFun" //回调函数 声明方法在文档中有的
myFun(item:Object,column:DataGridColumn)
	if(item.hasOwnProperty("mypro")


sortCompareFunction=""
函数返回 -1 obj1< obj2
ObjectUtil.numbericCompare(xx,yy)
ObjectUtil.stirngCompare(xx,yy,true)大小写敏感?




<xm:XML id="tests" source="my.xml"/>  有根再有<test>
<mx:XMLListCollection  source={tests.test}/>

function myLabeFunc(item:XML,column:DataGridColumn):String
{
	return item.*.(@name==column.dataField).text(); 


}


var my:SortField    =new SortField()
	my.compareFunction=myFunc;
	my.desending=true;
var mySort:Sort=new Sort();
	mySort.fields=[my];

XMLListCollection 的sort =mySort;
		     refresh();


ArrayCollection 有一个filterFunction=myFun;
	refresh();   //刷新邦定
myFunc(item:Object):Boolean


<mx:Application viewSourceURL="xx/yy.html"/>  显示Flex的源代码

event:FaultEvent 
event.fault.faultString
evnet.fault.message



<mx:HTTPService showBusyCursor="true" falt="myFunc(event)"  url="xxx.jsp" result="xx=yy"
	request 是传URL的参数 
	result 事件(ResultEvent 有一个属性是result 是返回的值) event.result.xmlroot.xxx

send()方法去发送请求  ,只method="GET",POST



URLVariables 是一个动态类   文档有例子 

var urlV=URLVariables    文档有例子 
urlV.xx=yy;
var req=URLRequest("http://xx");
req.data=urlV;
req.method="POST" //URLRequestMehtod.POST
flash.net.navigateToURL(req,"_self");//"_top", "_self", or "_parent" _blank  //改变浏览器的地址


hcolor = dg.getStyle("headerColors")[0];//返是一个长的数字要处理它
Numer(hcolor).toString(16);



DataGridColumn 如有 labelFunction 要用反射才行啊


<param name="flashvars" value="bridgeName=example"/>
var flexApp = FABridge.example.root();
flex的id="myid" 地JS中用flexApp.getMyid();
属性myattr用getMyattr()或者用setMyattr(xx)



------------------------从DataGrid生成 Excel ,向JSP发<table>可以有样式, JSP文件中
<%@page language="java" pageEncoding="GBK"%>
<%
request.setCharacterEncoding("UTF-8");
response.setHeader("Content-disposition","attachment;filename=xx.xls");
out.print(request.getParameter("htmltable")); //里的值是HTML的<table>内容
%>

也可以使用类似于mysql 备份的文件格式,tab键(\t) 来分隔列,回车符来分隔行(不过没有样式)
  System.setClipboard(TSVString);  //也可用复制的方式


<mx:WebService wsdl="http://xxx.com/xxxService.aspx?wsdl" useProxy="false" >
	<mx:operatioin 	name="xxxSeriveMethod" resultFormat="object" result="myFunc(event)" fault="myFaultFunc(event)">
						<!-- 还有一个concurrency="multiple|singel|last"  多个请求同时发出时,如服务器响应时间较长,last只响应最后一个-->
		<mx:request>
			<Word>{xx.text}</World>     <!-- 这是WebService 的的描述方法的参数名 -->
		</mx:request>
	</mx:operation>
</mx:WebService>


WebService 使用代码的方式 看文档
Using WebService components 


EventDispatcher  有dispatchEvent(XXEvent)
		有addEventListener(XXEvent,XXFunc)

FileReference .browse(可加过虑器FileFilter数组("显示标签","*.jpe;*.png"));来打开文件选择对话框   //是flash 样式的
		.upload(URLRequest("http://xxxx"))  
FileReference 加监听 addEventListener(Event.SELECT,)
			Event.COMPLETE
			ProgressEvent.PROGRESS    有bytesLoaded ,bytesTotal  
		

<mx:Model  id=myModel>
<xx>
	<yy>{""}</yy>
</xx>

可以用myModel.yy=zz;  来修改值 

FileReference .download(new URLRequest("new"),"saveASName")

FileReferenceList browse 可一次选择多个文件












===========================amfphp=========================================================

AMF Action Message Format  二进制

----------------方法一
amfphp 目录在service  目录中加入php类,(文件名和类名一样)
http://localhost/amfphp/browser            会显示后加的Service   可以用做调试,测试用吧

方法一 NetConnection 和 Responder , gateway.php
方法二 常用 <mx:RemoteObject  和<mx:method ,编译 -services "service-config.xml"
	

Flex中
import flash.net.Responder;
import flash.net.NetConnection;
var conn:NetConnection=new NetConnection();   
conn.connect("http://localhost/amfphp/gateway.php");
conn.call("ClassName.method",new Responder(myFuncResult,myFuncFault ),str );
		PHP服务器端类名.方法名          成功返回后调用的方法	失败返回后调用的方法  传给服务器端方法参数,可多个
	public function myFuncResult(res:Object):void{}

//ClassName.method PHP中没有static
写的PHP文件放在/amfphp/services 目录下 ,下又有一个amfphp\DiscoveryService.php 是为browser来使用发现其它的服务



传中文乱码　	gateway.php 中改编码
加注释		$gateway->setCharsetHandler("utf8_decode", "ISO-8859-1", "ISO-8859-1");　
取消注释	$gateway->setCharsetHandler( "iconv", "big5", "big5" );//utf-8


//改gbk,gb2312,utf-8如是Mysql  要set names GBK/UTF8 ,要一致才行
//explorer中可以测试中文 sayHello是OK的



//不要使用高版本的Appser,(PHP5.2.3)---OK
<?
class MyUsers
{
	function getUsersByAge($min,$max)
	{
		$conn=mysql_connect("localhost","root","root");
		if(!$conn)
			return "连接失败";
		mysql_select_db("php");
		mysql_query("set names gbk");					//注意要和gateway.php 文件中的一样才行(utf8)
		$array=array();
		$sql="select * from student where age>".$min." and age<". $max;
		$result=mysql_query($sql);//$conn
		if($result)
		{	
			while($data=mysql_fetch_array($result,MYSQL_ASSOC))	//只取字段名,MYSQL_NUM 字段顺序,MYSQL_BOTH 两个都有
			{
				array_push($array,$data);
			}
			mysql_close($conn);
			return $array; //直接返回$result ,flex 中是ArrayCollection
		}else
			mysql_close($conn);
			return "没有数据";
	}
}
?>
  Flex中
function myFuncResult(result:Object):void
{
	if (result is Array)
		grd.dataProvider=result as Array;　//也可不转
//或使用下面的 import mx.utils.ArrayUtil;
//	grd.dataProvider = new ArrayCollection( ArrayUtil.toArray(result) );
//	grd.dataProvider=result;
}

----------------方法二  常用 
ampPHP 使用
	source="MyClassName"   servives 目录下的php 类名
	 destination="amfphp" 是 services-config.xml  中的
<mx:RemoteObject  id="service" fault="myFuncFault(event)" source="MyClassName" destination="amfphp" >  
	<mx:method name="myMethod" result="MyFuncSucess(event)"/> <!--为了不同的方法使用不同的result处理  -->
<mx:RemoteObject>

//因配置文件中是*　所以要 source来指定类

service.getOperation("myMethod").send("方法参数")//调用方法

function MyFuncSucess(evt:ResultEvent){}
function myFuncFault(evt:FaultEvent){}


http://localhost/amfphp/brower/service-config.xml 是来复制的   放在src目录下
注意要改gateway.php的位置 <endpoint uri="http://localhost/amfphp/gateway.php"  


默认已经加了包　rpc.swc　和　flex.swc
加编译参数  -services "service-config.xml"






Flex类和PHP类的属性 名字,类型 要一致
转过来时的对象多了一属性mx_internal_id

-----可不加
放在Flex的VO前
[RemoteClass(aliase="myphp.MyPhpClass")] //表示flex中这个类 和 php的myphp.MyClass是对应的(service 目录下有mypack/MyPhpClass.php)


php类中最后一个属性
var $_explicitType="flex.MyFlexClass";   //这个PHP类和Flex 中flex.MyFlexClass 对应
--------


PHP 用/**   */注释在amfphp中的explorer有显示

require_one "./Persion.php"; //PHP 包含文件,$p=new Person(); $p->username



$p=array();
for ($i=0;$i<count($p);$i++)
{	
	$t=new Person();
	$p[]=$t; //向数组是加元素
}
注意php文件中 <? 前不能有空格,回车 否则报BadVersion

===========================blazeDS=========================================================
观察者模式     发布者/订阅者    服务器向客户端 推的模式

双连接   1. 服务端-> 客户端   2.客户端->服务端    传输完成 释放   HTTP-1.1
In IE, the maximum number of connections per session is two
In Firefox, the maximum number of connections per session is eight
<mx:NumberFormatter id="xx" precision="2"/>

-------flex中
stockMap = new Object();
stockMap.hasOwnProperty(username);//是否有 属性
stockMap[symbol];// symbol 是一个String  变量,相当于加点 ,加属性

自定义类  extends Label 
{
	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
	{
		super.updateDisplayList(unscaledWidth, unscaledHeight);
		var g:Graphics = graphics;//graphics 是Label的属性类型是flash.display.Graphics
		g.clear();

invalidateDisplayList();会调用 updateDisplayList()

    

自定义类  extends flex.messaging.client.FlexClientOutboundQueueProcessor;
	 {
		public FlushResult flush(List outboundQueue)//重写方法
		{
			FlushResult flushResult = new FlushResult();
			flushResult.setNextFlushWaitTimeMillis(100);//可从FlexContext中得到
			flushResult.setMessages(new ArrayList(outboundQueue));
		}
		 public void initialize(ConfigMap properties) //flex.messaging.config.ConfigMap 是LinkedHashMap
		{
			int x = properties.getPropertyAsInt("flush-delay", -1);//<flush-delay> ,如未定义,使用默认值-1

 <channel-definition
	<properties>
		<flex-client-outbound-queue-processor  class="自定义类">
			<properties>
				<flush-delay>5000</flush-delay>
			</properties>


import flex.messaging.FlexContext;
import flex.messaging.client.FlexClient;
FlexClient flexClient = FlexContext.getFlexClient();
flexClient.setAttribute(name, value);
flexClient.getAttributeNames();
flexClient.getAttribute("");

----上java中

<default-channels>
    <channel ref="my-streaming-amf"/>
    <channel ref="my-polling-amf"/>
</default-channels>
如果my-streaming-amf失败,会再试my-polling-amf


BookVO(event) 或者用 event as BookVO 进行强转


==============================Spring 集成 BlazeDS
不需要配置BlazeDS的MessageBrokerServlet

<servlet>
    <servlet-name>Spring MVC Dispatcher Servlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/config/web-application-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>



<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:flex="http://www.springframework.org/schema/flex"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
           http://www.springframework.org/schema/flex 
           http://www.springframework.org/schema/flex/spring-flex-1.0.xsd">

-----方法一  <flex: 标签一定要RC2版本才行的

<flex:message-broker services-config-path="classpath*:services-config.xml"/>
//默认是/WEB-INF/flex/services-config.xml



<flex:message-broker mapping-order="" disable-default-mapping="false">
	<flex:mapping pattern="/messagebroker/*" />
	<flex:message-interceptor ref="myMessageInterceptor"/>	<!--实现org.springframework.flex.core.MessageInterceptor 接口 -->
	<flex:config-processor ref="myConfigProcessor" />	<!--实现org.springframework.flex.messaging.config.MessageBrokerConfigProcessor接口 -->
	<flex:exception-translator ref="myExceptionTranslator"/><!--实现org.springframework.flex.core.ExceptionTranslator 接口-->
</flex:message-broker>



 <flex:remoting-destination ref="myService" />   //flex中的 destination="myService"

以下可能不行的
<mx:AMFChannel id="myamf" uri="/test-server/spring/messagebroker/amf"/>    
<mx:ChannelSet id="channelSet" channels="{[myamf]}"/>    
<mx:RemoteObject id="srv"   destination="myService" channelSet="{channelSet}"/>    
<mx:DataGrid dataProvider="{srv.getMyEntities.lastResult}"/>  



------方法二
// RC2版本的<flex: 一定要在service-config.xml 中 <services>最后中加入OK, M1版本的<bean也要加
		<default-channels>
			<channel ref="my-amf"/>
		</default-channels>

<bean id="_messageBroker" class="org.springframework.flex.core.MessageBrokerFactoryBean" >//M1版本是messaging.MessageBrokerFactoryBean
	<property name="servicesConfigPath" value="classpath*:services-config.xml" /> 
	<!-- 可省 默认/WEB-INF/flex/services-config.xml-->
</bean>

以下是默认的
	<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
		<property name="mappings">
			<value>
			/ *=_messageBroker
			</value>
		</property>
	</bean>
	<!-- Dispatches requests mapped to a MessageBroker 还要一个中间人 -->
	<bean class="org.springframework.flex.servlet.MessageBrokerHandlerAdapter"/>//M1版本是messaging.servlet.MessageBrokerHandlerAdapter

	
	//flex中的 destination="myspring"  
	<bean id="myspring" class="org.springframework.flex.remoting.RemotingDestinationExporter">  //M1版本是.messaging.remoting.FlexRemotingServiceExporter
		<property name="messageBroker" ref="_messageBroker"></property>
		<property name="service" ref="hello"></property>
	</bean>

<mx:RemoteObject id="he" destination="myspring"/>
AS中用 he.sayHello();


===========================WebORB for Java
java -jar xx.jar来安装服务器
到服务器安装目录 java -jar weborb.jar 来启动服务器 占用8080端口


<destination id="GenericDestination">
	<properties>
	    <source>*</source>
	</properties>
</destination> 
AS中使用
	var remote:RemoteObject=new RomteObject("GenericDestination");//GenericDestination配置在remote-config.xml中的
	remote.source="org.BasicSerivce";//类名
	remote.addEventListener( FaultEvent.FAULT, myFaultFUnc );
	remote.MethodName.addEventListener(ResultEvent.RESULT,myOKFunc);
	remote.MethodName(1);//来调用 

也可用 	<mx:RemoteObject 

复制weborb.war到tomcat的webapps中 启动后有示例文档
	Management->Services->weborb.jar->weborb->examples->BaseService 有AS3代码,Cairngorm代码,PureMVC代码

//独立运行的webORB,把自己开发的jar包放在webapps目录下,
//webORB运行在tomcat中,把jar包放在WEB-INF/lib下,Management->Services->会看到自己放的jar包

SWC文件位于 weborbassets\wdm\weborb.swc

支持JMS消息


---使用google的 IFrame  嵌入网页 
	http://code.google.com/p/flex-iframe/
	<mx:Application  xmlns:local="*" 	
	<local:IFrame 	id="myFrame" width="100%"  height="100%" source="about.html"/>

 import flash.external.ExternalInterface; 
==============================Cairngorm

import com.adobe.cairngorm.model.IModelLocator;
import com.adobe.cairngorm.control.CairngormEventDispatcher ;
import com.adobe.cairngorm.control.CairngormEvent;
import com.adobe.cairngorm.control.FrontController;
import mx.rpc.IResponder;
import com.adobe.cairngorm.commands.ICommand;


	

模型中
	implements ModelLocator 

控制中
	extends FrontController 
		addCommand("",ICommand子类)//来加多个Command

视图中	//真Eevnt数据,把 Event 传给 Command	
	CairngormEventDispatcher.getInstance().dispatchEvent(myevent) ;
	或者用 
	myevent.dispatch();
	

Event 子类 extends CairngormEvent
	super("")//构造函数中来标识Event,加数据

Command子类
	public function execute ( event : CairngormEvent ) : void
	{
		//中使用(Delegate)处理逻辑,IResponse子类  处理结果
	}

Delegate类中有
	//构造转入(IResponder),
	var service:Object=ServiceLocator.getInstance().getRemoteObject(""); //getHTTPService,getWebSerivce
	
	//方法中
	var result:Object= service.doMyMethod(1,2);
	result.addResponder(responder)//addResponder, 会自动调用IResponder的result(),falt()


import com.adobe.cairngorm.commands.SequenceCommand;
import mx.rpc.AsyncToken;
import mx.rpc.Responder;

SequenceCommand 给nextCommand赋值,可以用executeNextCommand() 来跳转到另一个Command

var service:Object=ServiceLocator.getInstance().getHTTPService("");
var params:Object=new Object();
params.xx=1;

var token:AsyncToken=service.send(params);
var responder:Responder= new mx.rpc.Responder(resFun,falFun);  //Responder(result:Function, fault:Function),类型是Function
token.addResponder(responder);  

service.url


//JSONDecoder 在as3corelib中要从google上下
var x:Array=new JSONDecoder("").getValue();




==============================PureMVC

import org.puremvc.as3.interfaces.IProxy;
import org.puremvc.as3.patterns.proxy.Proxy;

import org.puremvc.as3.interfaces.ICommand;
import org.puremvc.as3.patterns.command.SimpleCommand;
	
import org.puremvc.as3.interfaces.IFacade;
import org.puremvc.as3.patterns.facade.Facade;

import org.puremvc.as3.interfaces.INotification;


Facade子类 extends Facade implements IFacade //做成单例
	{
		override protected function initializeController():void
		{
			super.initializeController();
			registerCommand( "Facade子类常量String",  Command子类 );//来加多个Command
		}
	}


视图中用
	sendNotification( "Facade子类常量String", 要传数据对象 );//来触发Command


Command子类 extends SimpleCommand implements ICommand
	{
		override public function execute(note:INotification):void
		{
			//父类的facade
			facade.registerProxy( new Proxy子类() );
			facade.registerMediator( new Mediator子类());
		}

	}

Proxy子类//处理数据 extends Proxy implements IProxy
	super("proxyid",数据)//构造中

	sendNotification("xx", 数据);//来通知视图,数据处理完成


Mediator子类 //处理显示 extends Mediator implements IMediator
	super("Mediatorid",数据)//构造中

	override public function listNotificationInterests():Array
	{
		return ["xx","yy"];
	}

	override public function handleNotification(note:INotification):void
	{
		switch( note.getName() )
		{
			case "xx":
				break;
			case "yy":
				break;
		}
	}
	//父类的facade
	facade.retrieveProxy("proxyid")//也可得到Proxy来数据处理





===================================英文视频

C:\Program Files\Adobe\Flex Builder 3\sdks\3.1.0\frameworks\flex-config.xml  中有namespace 可用在<mx:Applicaiont namespace中
C:\Program Files\Adobe\Flex Builder 3\sdks\3.1.0\frameworks\mxml-manifest.xml　有mxml和AS3的组件对应关系

<mx:Application 
	backgroundGradientAlphas="[1.0, 1.0]" backgroundGradientColors="[#868686, #262525]"  这个可以通过设计Fill栏,从上到下


不要import  flash包，它在flash player  中使用

ArrayCollection常用 ，object,xml,flashvars,e4x,array

<mx:HTTPService  fault="" requestTimeout=""
<mx:DataGrid alternatingItemColors="[#CCCCCC,#AAAAAA]"  表格的交差颜色
<mx:Application layout="vertical" pageTitle="浏览器上的标题<title>" 
Canvas,VBox,HBox

class 和属性前都可以加[bindable]

"{parentDocument}"

自定义事件extends Event是，要重写clone方法
override clone


itemClick事件的event.itemRender.data里存数据

<mx:DataGridColumn itemRenderer="mx.controls.CheckBox"> 或者用下面，或者用自定义的组件
<mx:ItemRender>
	<mx:Component>
		<mx:Image ...


光标定一个括号 ctrl+shift+p 后定位另一们括号上
选中代码 alter + 方向键
工具栏上有一个按钮   刀形 (Mark occurences) 高亮显示所有选中的变量 

右击文件-》 compare with ->local history 
Validator.validateAll(ArrayCollection中是Validator);

<mx:RmoteObject id="xx" destination="service-config.xml中的"  fault="">
	<mx:Method name="getMyMehtod" result=""  >
		<arg1>{text1.text}</arg1>
		<arg2>{text2.text}</arg2>
xx.getMyMethod();
xx.getOperation("getMyMehtod").send();//会覆盖mxml中的参数

为前加 [RemoteClass (alias="javapack.JavaClass")]

mx.formatters.Formatter 的子类 DateFormatter
<mx:DateFormatter 日期格式化
	.format(date) 

<mx:DataGridColumn lableFunction="myformat"
		myformat(item:Object, column:DataGridColumn):String

<mx:PhoneFormatter  formatString="(###) ###-####"
var reg:RegExp=/-/g;  和JS一样的


mx.managers.DragManager  都是静态方法
<mx:DataGrid dragEnabled="true"　dragMoveEnabled="true" 移动而不是复制　，拖时按ctrl也是复制
<mx:List dropEnabled="true" labelField="要显示对象的属性"


<mx:ViewStack id="content" 有一个numChildren 属性 selectedIndex="0" selectedChild=myid resizeToContent="true"
<mx:Button click="content.selectedIndex = numChildren - 1"/> // numChildren 是ViewStack的属性，不是content.numChildren

<mx:ButtonBar dataProvider={content}   //content里的每一个要有lable做为显示按钮的字
    LinkBar,TabBar

VerticalScrollPolicy="off"
绝对定位 Application,Panel,Canvas
设计视图有一个  show Surrounding container按钮

只有绝对才可重叠

top ,left,right ,bottom是约束一个组件和上边界始终保持一定距离(可以使用设计视图)

<mx:Text horizontalCenter="0" 位于水平中心 
<mx:Text left="20" right="contentColumn1:20" height="100%
<mx:Text left="contentColumn2:20" right="contentColumn2:20">大写来约束
	<mx:text>sadfa</mx:text> 小写t 里面是文本
<mx:constraintColumns>
	<mx:constraintColumn id="contentColumn1" width=33%>
	<mx:constraintColumn id="contentColumn2" width=33%>
	<mx:constraintColumn id="contentColumn3" width=33%>
</mx:constraintColumns>
<mx:contraintRows>
	<mx:contraintRow id="contentRow1" height="60%"></mx:contraintRow>
	<mx:contraintRow id="contentRow2" height="40%"></mx:contraintRow>
</mx:contraintRows>



[Embed(source='')]
[Bindable]
private var Img:Class;  //Class,多次使用创建多个实例
mx.core.BitmapAsset  //图片
mx.core.MovieClipAsset //swf

[Bindable]
private var img2:MovieClipAsset=new Img();


<mx:Button upSkin="@Embed('xx.swf')" //文字显示在上面
	upIcon="@Embed('xx.swf')"  //会和文字正常显示
	overSkin		鼠标滑过时的图片,如放在css文件中是 overSkin:Embed('xx.swf'),没有@

用多次@Embed 是产生多个实例

自定义容器
	1.新建电影剪辑，画左上角对准中心
	2.选中库中的电影剪辑，commands->convert Symbol to Flex Container
			会新增加一个FlexContentHolder电影剪辑，容器的内容区
	3.双击自定剪辑，把FlexContentHolder拖入舞台,改变大小,位置在自己的容器内
	//右击电影剪辑-》Linkage..->Base class确定是mx.flash.ContainerMovieClip
	4.publish 成swc

	AS3中可以使用　自定义容器.content=xxx;//来加子容器,只能给一个值，标签中也是
	
	可申缩的(最小化)currentState="帧标签名"

firewok 中->command ->flex skining->new flex skin->Button, 会看见不同图层,绘制时,注意图层,不用管位置，背景和文字
firewok 中->command ->flex skining->export flex skin到指定目录(firework保存为png,生成多个png文件Button_upSkin.png....)

photoshop是在file->scripts-> 中 导出每个层
illustrator是file->scripts->flex skin->中　swf文件
flash 中新建一个flex skins模板  (编辑修改帧标签对应的状态)//导出swc文件中的library.swf可以预览,也可在flex的css设计视图

flex中 右击src->import...->artwirk->选择刚才导出的图像文件夹　或者是swc文件,在copy artwork to subfolder 选择自己项目的文件夹
				->可以选择已有的css文件，自动生成下列CSS
CSS中
Button
{
	downSkin:Embed(source="Button_downSkin.png");
	overSkin:Embed(source="xx.swf" ,scaleGridLeft="2",scaleGridRight="2",scaleGridTop="",scaleGridButton="") 
	//通过设计视图，Button样式,刷新按钮,Edit Scale Grid按钮,在按钮放大时,边界不会变大,
	upSkin:ClassReference("com.xxxx");//Shape子类,对swc文件,是电影剪辑的同名
}

VBox中有 visible="true"的东西 改变vbox会有滚动条,加属性includeInLayout="false"就没有滚动条 

flex help 有Available display filters 和　About behaviors->available effects/trigger　文章

 
 

import flash.display.Graphics;
ProgrammaticSkin子类 extends  mx.skin.ProgrammaticSkin
{
	override protected function updateDisplayList(unscaleWidht:Number,unscaleHeight:Number):void
	{
		super.updateDisplayList(unscaleWidht,unscaleHeight);
		graphics.clear();

	}

}

<mx:VBox borderSkin="com.ProgrammaticSkin子类"/>

ButtonSkin子类 extends mx.skins.halo.ButtonSkin
{
	override protected function updateDisplayList(unscaleWidht:Number,unscaleHeight:Number):void
	{
		super.updateDisplayList(unscaleWidht,unscaleHeight);

		switch(name) //有name属性
		{
		case:"upSkin" :
			//如改背景色
			break;
		case:"overSkin" :
			break;
		case:"downSkin" :
			break;
		}

css 中的 
Button
{
	upSkin:ClassReference("com.ButtonSkin子类");
	overSkin:ClassReference("com.ButtonSkin子类");
	downSkin:ClassReference("com.ButtonSkin子类");
}



对象(Shape,Button)的transform属性  =Transform 的matrix属性 = Matrix (看文档有算法)
Shape 类　


[Style(name="newButtonSkin" ,type="Class")]
自定义组件 extends FormItem
{ private var newButtonSkin:Class;
override protected function createChildren():void  //createChildren是UIComponent类的
{
	super.createChilder();
	newButton:Button=new Button();
	newButtonSkin=getStyle("newButtonSkin")
	//newButtonSkinCSS是CCS文件中.formItemButtons{newButtonSkin:ClassReferences("xx.ButtonSkin子类")}　和　[Style(name="newButtonSkin"
	//mxml中styleName="formItemButtons"
	newButton.setStyle("upSkin",newButtonSkin);

	rawChildren.addChild(newButton);//rawChildren是　Container的属性，IChildList类型

提供　set xx方法　在mxml文件中改属性会提示的　
override protected function updateDisplayList(unscaleWidth:Number,unscaledHeigth:Number)//DisplayList
{
	var containerWidth:int=unscaleWidth + _newButtonWidth;
	super (containerWidth,unscaledHeigth);
	var xpos:int=containerWidth - _newButtonWidth; //unscaleWidth
	var ypos:int;
	newButton.move(xpos,ypos);//不能使用newButton.x ,move方法会发出　move 事件，来刷新屏幕

}


-------



data->import webservice->可以
加库时　可以选择Runtime Share Library
file ->export ->release version

new ->mxml Module->可选择appliction的mxml(父) ,<mx:Module>为根　
	var myParent=parent;//是<mx:ModuleLoader ，只能这样，否则下xx()方法有错误
	myParent.parent.xx();
	//可以使用parent　与父通讯　


application来引用模型
<mx:ModuleLoader url="MyModule.swf" id="my"/>   　//延迟加载子模型，不是一次全下载完成
			 my.child  //可以和子通讯

　
sdks\3.1.0\frameworks\projects\framework\default.css文件
(ADL命令)AIR Debug Launcher  
fdb 命令是调试用的　　mxmlc -debug=true　
			    


菜单的XML属性中有 type="check"  type="radio"

Z 深度  广告//move(x,y)和Timer
Container 的setChildIndex(DisplayObject,int);//0是最下面的,numChildren是最上面的,设计视图先放的在下面,0向上加
if( newIndex >= 0 && newIndex < idParentCanvas.numChildren ) {
	idParentCanvas.setChildIndex(idChildCanvas, newIndex );

		

反射
import flash.utils.getDefinitionByName;
as 语句是无异常的类型转换. 如果转换失败那么目标变量将被设置成 null
var ClassReference:Class = getDefinitionByName("flash.text.TextField") as Class;

实例化所引用的类, 并设置一些属性

var instance:TextField = new ClassReference() as TextField;
instance.autoSize = "left";
instance.text = "我通过 getDefinitionByName 动态创建";

最后添加到场景中并显示

addChild(instance);

//把文本复制到剪贴版
 System.setClipboard(richTextEditor.text);
 //安全原因不能读贴板  ,TextArea自带的可以用,(生成ctrl+v事件  不行的)

focusManager.setFocus(textInput); //设置焦点
this.stage.focus=textInput;//另一个设置点


-----
//拖动组件　产生新显示
 public function mouseDown(event:MouseEvent):void
 { 
	this.uiComponent = event.currentTarget; //保存句柄
	var bd:BitmapData = ImageSnapshot.captureBitmapData( UIComponent( uiComponent ) ); 
	targetImage = new Image();  
	targetImage.source = new Bitmap(bd); 

	Ax = event.localX; //localX组件自己的坐标

  public function mouseMove(event:MouseEvent):void
  { 
              targetImage.x = event.stageX-Ax; //stageX 舞台的坐标


this.cursorManager.setBusyCursor();
CursorManager.setCursor(Circle);//自定义光标
	public class Circle extends Shape  //Sprite
	{
		public  function Circle()
		{
		super();
		this.graphics.lineStyle( 1 , 0x000000 , 1 );
		//this.graphics.drawCircle( 0, 0 , 10 );
		this.graphics.moveTo(-10, 0);
		this.graphics.lineTo(10, 0);
		this.graphics.moveTo(0, -10);
		this.graphics.lineTo(0, 10);
		}
	}

DisplayObject blendMode属性  是布尔运算
通过flash自定义一个可申缩的容器
Flex 图表的放大    <mx:Zoom  拖动放大


Security.allowDomain("*");

一个div显示在该flash的上面一层
FLASH优先级比DIV高, 所以会挡住DIV的.
<object>
	<param name="wmode" value="Opaque">
		"Window" 在 Web 页上用影片自己的矩形窗口来播放应用程序，并且始终位于最顶层。
		"Opaque" 显示页面上位于它后面的内容。
		"Transparent"使 HTML 页的背景可以透过应用程序的所有透明部分显示出来，并且可能会降低动画性能。



------------Red5
exe安装Red5后,services.msc中有服务,有red5.jar和src.zip,war中没有的
http://localhost:5080/demos/

复制C:\Program Files\Red5\doc\templates\下的myapp目录
修改red5-web.properties文件中的 webapp.contextPath=/testRed5web
	exe安装后监听5080端口 
doc下有HOWTO-NewApplications.txt

MyJava类 exends org.red5.server.adapter.ApplicationAdapter
{

	public double add(double a,double b)
	{
		return a+b;
	}
}
配置文件red5-web.xml只有exe安装后有的,新增
<bean id="web.handler" class="MyJava类" singleton="true">
</beans>

web.xml中改 这项只有war中有,在C:\Program Files\Red5\doc\templates下的也有的
<context-param>
	<param-name>webAppRootKey</param-name>
	<param-value>/</param-value>  ###值是web项目的根
</context-param>


flash 8 中写
nc=new NetConnection();
nc.connect("rtmp://localhost/myapp");
nc.onStatus=function(obj)
{
	for (e in obj)
	{
		trace(e+":"+obj[e]);
	}
}
nc.onResult=function (obj)
{
	trace("result is "+obj);
}
nc.call("add",nc,1,2);//Red5 可以调用Java方法

多次运行,使用同一个Java对象实例

把自己的java程序打成jar包,放入classpath中
flash 的发布设置->flash->protect from import 保护导入

exe安装的目录conf/red5.properties 中rtmpt.port=8088可改的
------------Red5


		


