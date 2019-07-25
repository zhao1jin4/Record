-----------Flex-4.6 ActionScript 3
http://help.adobe.com/zh_CN/flex/using/index.html  


<s:Application creationComplete="init(event)" ><!-- creationComplete 加载完成后调用方法,必须是event  -->
	<fx:Script>
		<[![CDATA  
		function inti(event:FlexEvent)
		{
			trace("debug");//在控制台显示信息　，调试模式
		}
		]]> 
	</fx:Script>
</s:Application>

const 定义常量
internal(default)	同一包可见

var a:int=2;
trace (a is int);//true  ,is 类似于 instanceof
trace (a is Number);//true ,Number类
var num:uint=1.5;//uint 类型,可以赋值,只存取整数部分
new Array("a","b",1);
var  my:ArrayCollection=new mx.collections.ArrayCollection( [{name:"lisi",age:20} ]);
my.addItem(null);
my.length;
支持 ? :    
for (var pro in myObject)
	trace(myObject[pro]) ;//像JS

var array:Array=new Array("a","b");
for (var i:String in array)//在for的后不能再使用i做为变量
{
	trace(i);//0 虽然是String,但还是0,1
} 
for each (var j:String in array)
{
	trace(j);//"a",这是和for in的不同
}			  
			 
<s:Button>
使用flex的事件
<fx:Metadata>  做为一个组件使用,在标签中有myEvent事件属性,加事件处理函数,type的值也可是自定义事件
		[Event(name="myEvent" ,type="flash.events.Event")]     
</fx:Metadata>	 
this.dispatchEvent(new Event("myEvent"));



自定义事件,可能传递数据 
import flash.events.Event;
public class YourEvent extends Event 构造方法中要有super("evtType",true)//第二个参数bubbles一定要true


HGroup,VGroup布局
<s:layout>
	<s:VerticalLayout gap="20" horizontalAlign="center"  verticalAlign="middle"/>
</s:layout>

<s:CheckBox label="checkBox"></s:CheckBox>

Alert.yesLabel="是";
Alert.noLabel = "不";
Alert.cancelLabel="取消"
Alert.show("内容","标题",Alert.YES|Alert.NO|Alert.CANCEL,this,function (evt:CloseEvent):void
						{
							var which = evt.detail;
							trace ('close is clicked:'+which );
						});

 var num:int=Number("123");//转换类型
 
[Bindable]
var comboData:ArrayCollection = new ArrayCollection([{label:"please select"},{label:"aa"},{label:"bb"}]);
<s:ComboBox dataProvider="{comboData}" labelField="label" selectedIndex="0"></s:ComboBox><!--可以输入的 下拉列表-->

[Bindable]
var dataGrid:ArrayCollection = new ArrayCollection([  {id:10,label:"aa" }, {id:20,label:"bb" } ]);
<s:DataGrid dataProvider="{dataGrid}" alternatingRowColors="[#CCCCCC,#FFFFFF]" > 
	<s:columns>
		<s:ArrayList>
			<s:GridColumn dataField="id" headerText="标识"/>
			<s:GridColumn dataField="label" headerText="名字"/>
			<s:GridColumn   labelFunction="columnLabelFunction"  headerText="ID_名字"/>
		</s:ArrayList>
	</s:columns>
</s:DataGrid>
function columnLabelFunction(item:Object, column:GridColumn):String
{
	return item.id+"_"+item.label;
}

var listData:ArrayCollection = new ArrayCollection([ { price:200,img:'img/smile.gif' }]);
<s:List width="80" height="80" dataProvider="{listData}" horizontalScrollPolicy="on"
		labelFunction="listLabelFunction">  <!--itemRenderer="component.PhoneItem" 另一个组件,包.-->
	<s:layout>
		<s:HorizontalLayout/>
	</s:layout>
</s:List>
function listLabelFunction(item:Object):String
{
	 return "手机:"+item.price;
} 



			
<s:Panel title="标题栏" width="100" height="200" > <!-- width="200"  -->
	<s:layout>
		<s:VerticalLayout gap="20" horizontalAlign="center"  verticalAlign="middle" />  
	</s:layout>
	<s:HGroup paddingLeft="5" paddingRight="5"  horizontalAlign="center" verticalAlign="middle" width="100%">
		<s:Button label="test" />
		<s:Button label="test" />
	</s:HGroup>
	<s:HGroup paddingLeft="5" paddingRight="5"  horizontalAlign="center" verticalAlign="middle" width="100%">
		<s:Button label="test" />
		<s:Button label="test" />
	</s:HGroup>
</s:Panel>

		 

<fx:Binding destination="text2.text" source="text1.text" /> <!-- bind 方法 -->
<s:TextInput  id="text1"   />  displayAsPassword="true"
<s:TextInput  id="text2"  />  

myHttpService.send();
<fx:Declarations>
	<s:HTTPService id="myHttpService" 
					url="http://127.0.0.1:8080/J_FlexService/readXML"  
					result="httpservice1_resultHandler(event)"  fault="httpservice1_faultHandler(event)" //FaultEvent
					resultFormat="e4x" requestTimeout="2000" >
		<s:request> <!--传参数,可使用{ } ,也可用send({param:'paramValue'}) 对象做为参数来传递,s:request就无效了-->
			<param>paramValue</param>
		</s:request>
	</s:HTTPService>
</fx:Declarations>
<s:DropDownList dataProvider="{httpService.lastResult.root.tree}" labelField="name"></s:DropDownList> //.lastResult.后的root是XML的根标签

protected function httpservice1_resultHandler(event:ResultEvent):void
{
	var result:XML=event.result as XML; //只对指定resultFormat="e4x" ,强转才是对的
	for each (var xml:XML in event.result.children() ) //..des 
	{
		var obj:Object=new Object();
		obj.id=xml.@id;
	}
	trace(result); 
}
var sort:Sort=new Sort();//排序
var field:SortField=new SortField("id");
sort.fields=new Array(field); 
dataGridProvider.sort=sort;
dataGridProvider.refresh();

var newNode:XML=<tree/>;//构造XML对象;
newNode.@name=textInput.text;//@name属性
var node:XML=XML(myTree.selectedItem);//强转
Alert.show(node.toXMLString(),"title");//toXMLString
node.appendChild(newNode);
			
	


var timer:Timer=new Timer(2000);//加第二个参数共发两次
timer.addEventListener(TimerEvent.TIMER,timerFunction);
timer.start();
t.stop();
t.runing 
function timerFunction(event:TimerEvent):void
{
	var num:String=(Math.random()*100).toFixed(0);//转成字符小数精度
}
Math.random(); //0~1之间

表格样式
<mx:AdvancedDataGrid >
	<mx:columns>
		<mx:AdvancedDataGridColumn headerText="标识style"  styleFunction="columnStyle"/>
	
	表格中显示图片
	<mx:AdvancedDataGridColumn   headerText="图片"> <!--也可放在mx:DataGridColumn,但过时 -->
				<mx:itemRenderer>
					<fx:Component>
						<mx:Image source="img/smile.gif"/>  
					</fx:Component>
				</mx:itemRenderer>
	</mx:AdvancedDataGridColumn >
	
function columnStyle(data:Object, column:AdvancedDataGridColumn):Object
{
	return {color:0xff0000,fontWeight:"bold"};
}

public function set setImage(img:String):void  //做为组件时set 表示可以在标签中设置属性值,要public,属性也可
override 关键字放在方法前,用于重写父类的方法

s:名称空间包含　　flashx.* 和 spark.* 包

C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\flex-config.xml 文件中<namespaces>下的值
对应配置文件中名称空间和*_manifest.xml的文件一一对应,*_manifest.xml文件中有标签和类的一一对应
自定义名称空间　xmlns:my="my.*" ,my.*对应包

	
<fx:Style source="style.css"/>
css文件中的 global{} 样式是对全部的flex应用
s|Panel 是对<s:Panel>定义样式 ,s是名称空间 @namespace s "library://ns.adobe.com/flex/spark";
可以使用自己组件的名称空间,做CSS  @namespace com "component.*";
com|LoginForm s|Button   
{
	chromeColor:#000000;/*背景色*/
	color:#00ff00;
}
com|LoginForm s|Button:up   /* 会覆盖com|LoginForm s|Button  */
{
	color:#ff0000;
}
com|LoginForm s|Button:over 
{
	color:#ffffff;
}
com|LoginForm s|Button:down
{
	color:#0000ff;
}
#Myid 
{
}
.myClass
{
}
mx|LinkButton,s|CheckBox
{
	color:#00ff00;
}
继承样式 一个组件的字体大小　不设置　会继承自所在面板的字体大小


<s:Form>  大写的
	<s:FormItem label="日期: ">
		<mx:DateChooser change=""/>   
	</s:FormItem>
</s:Form>

dateChooser.addEventListener(CalendarLayoutChangeEvent.CHANGE, );

flash builder 的提示中三个蓝方块表示是样式,绿圆是属性,闪电是事件

spark 组件把 behavior,layout,styles,skin 分离出来,而mx组件没有,所有有更多的属性


<s:Label styleName="lableStyle"  rotation="90"  />



<s:Rect  radiusX="15"> 圆角 topRightRadiusX=3
appearance 视图中单击 project theme 栏中的Spark链接,选择主题后,所有的风格全变了
也可在项目->属性->Flex Theme中选择


<s:Panel   skinClass="skin.MyVerticalPanel" /> skin包MyVerticalPanel.mxml自定义组件的布局外观
中MyVerticalPanel.mxml中
<s:SparkSkin  根标签
	<s:states> <!-- [HostComponent("spark.components.Panel")]  提示要这些state-->		
		<s:State name="normal"/>
		<s:State name="disabled"/>
		<s:State name="disabledWithControlBar"/>
		<s:State name="normalWithControlBar"/>
	</s:states>
	<fx:Metadata>
		[HostComponent("spark.components.Panel")]  
	</fx:Metadata>	<!--表示在<Panel中使用skinClass-->	
	<s:Label id="titleDisplay"  <!-- 如是Panel必须有一个叫titleDisplay,类型IDisplayText的子类,可以查Doc的 Skin parts部分 其它的地方可以使用{titleDisplay.}-->
	<s:Group id="contentGroup" <!--  每个必须有一个叫contentGroup类型是Group -->
	
	hostComponent属性可以仿问父类(这里是Panel,也可自定义)
	
<s:BasicLayout/> 是默认的,使用x,y的绝对定位
<s:TitleLayout/> orientation属性决定布局方向,可取值为,columns,rows,以方格布局,
使用最宽或者最高的组件做为空间的大小
不可见的容器标签 <s:Group id="myGroup" >可以使用它来对组件做布局 ,DataGroup
<s:BorderContainer borderColor="#66" conrnerRadius="5" borderWeight="2">

加滚动条<s:Scroller viewport="{myGroup}"/>  或者<s:Scroller> <s:Group> </s:Group> </s:Scroller>包围的形式

框架初始化事件 
preinitialize->initialize->creationComplete ,show(从不可见到可见) 


evt.bubbles //是否冒泡
evt.target  //目标对象
evt.currentTarget//当前目录
evt.currentTarget//当前对象
evt.eventPhase //所处阶段 
捕获阶段 EventPhase.CAPTURING_PHASE
目标阶段 EventPhase.AT_TARGET
冒泡阶段 EventPhase.BUBBLING_PHASE


默认情况下捕获阶段是关闭的,
this.addEventListener("myEvent",myEventHandler2,false,2);//数小的优先级高
useCapture 如为true只处理捕获阶段,不处理目标阶段和冒泡阶段
		       false 只处理目标阶段和冒泡阶段,不处理捕获阶段,要处理合部调用两次
priority是对同一个事件注册不同的方法


ArrayCollection 可以排序,过滤
ArrayList 是flex4新加的
Flash Builder中的Network Monitor面板


<s:HTTPService  url="employee.xml" /> 就是默认包的文件,标签有 lastResult属性,result事件处理函数参数类ResultEvent的result属性
	 resultFormat="object|array|xml|e4x|flashvars|text"  ,object是默认的
 
RPC 中请的求的如果不是在本域中,要crossdomain.xml,必须位于服务器的根目录中,即http://127.0.0.1:8080/crossdomain.xml
Tomcat的conf/server.xml中 <Context docBase="J_BlazeDS" path="/" />  webapps/J_BlazeDS

<?xml version="1.0" encoding="UTF-8"?>  
<!DOCTYPE cross-domain-policy SYSTEM "http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd" >  
<cross-domain-policy>  
    <site-control permitted-cross-domain-policies="all" />  
    <allow-access-from domain="*" />  
    <allow-http-request-headers-from domain="*" headers="*"/>  
</cross-domain-policy>  

以下是官方的示例
<cross-domain-policy> 
    <site-control permitted-cross-domain-policies="by-content-type"/> 
    <allow-access-from domain="www.friendOfFoo.com"/> 
    <allow-access-from domain="*.foo.com"/> 
    <allow-access-from domain="105.216.0.40"/> 
</cross-domain-policy>

FlashBuilder 4.6中有WebService的标签代码模板

<!-- s:WebService里加result,所有的函数都使用这个,加s:operation的event后这里被覆盖,因用了load,失败只调用s:WebService中的fault-->
<s:WebService id="webService" wsdl="http://localhost:9000/helloWorld?wsdl"  load="webService.sayHi()" fault="Alert.show(event.fault.faultString,'WebService失败')">
	<s:operation name="sayHi" resultFormat="object"  result="webService_resultHandler(event)" fault="Alert.show(event.fault.faultString,'sayHi失败')">
		<s:request xmlns="http://server.webservice.zh.org/" >
			<text>Machael</text>
		</s:request>
	</s:operation>
</s:WebService>
load事件WSDL加载后调用,返回值在result事件中
Flex以ArrayCollection来表示数据

也可这样调用 webService.sayHi.lastResult;
加result事件处理函数,但要event.result as ArrayCollection


必须有wsdl才行,大量数据不适用

[Bindable]放在类前所有public 的属性,get set 方法都有绑定功能

FlashBuilder 4.6中有Data/Service标签,可选择BlazeDS,HTTP,DataService,PHP,WebService,XML
如选择WebService会生成 包.类  ,下划线开头的类不要修改,FlashBuilder会自动取数据修改,非下划线开头的类可以修改
界面中可以右击->Configure return type...   设置返回集合中的类名,会生成 valueObjects包
<fx:Declarations>下
	<s:DateTimeFormatter id="dateTimeFormatter"   dateTimePattern="yyyy-MM-dd HH:mm:ss" />
	<s:CurrencyFormatter id="currencyFormatter"   useCurrencySymbol="true" currencySymbol="￥"  decimalSeparator="."  fractionalDigits="2"  />
	<s:CurrencyValidator  id="currencyValidator" source="{currency}"  property="text"  /> <!--trigger="{submit}" triggerEvent="click"-->

dateTimeFormatter.format(date)//:Date
currencyFormatter.format(str)//:String
	
<s:FormItem label="金额：" required="true">
	<s:TextInput id="currency" />
	<s:helpContent>
		<s:Label  text="￥999,999.00"  baseline="24"/>
		<s:Button label="?" x="120"  width="30" baseline="24"/>
	</s:helpContent>
</s:FormItem>
<s:FormItem >
	<s:Button label="submit" id="submit"  click="Validator.validateAll([currencyValidator])"/> 
	<!-- click 或者trigger    validateAll返回ValidationResultEvent的Array,如果成功Arrary length为0-->
</s:FormItem>

NumberFormatter

focusOut事件 
图
<mx:ColumnChart dataProvider="" itemClick=""/>  ChartItemEvent, event.hitData.item as VO 

flex4新的绑定 @{ } 是双向的绑定, 如有类.属性,除变量名加[Bindable],类属性也要加[Bindable]

Data/Service标签中建立好的数据可以拖放到设计界面中的组件上,弹出绑定对话框
绑定后右击组件->Generate Detail Form...
<fx:Declarations>
	<s:ArrayList id="myArrayList">
		<fx:String>张三</fx:String>
		<fx:String>李四</fx:String>
		<s:Image source="img/help.gif" /> <!-- s:BitmapImage-->
		<s:Image source="img/new.gif" />
	</s:ArrayList>


<s:DataGroup dataProvider="{myArrayList}" 
		   itemRendererFunction="myItemRendererFunction" > 
	<!-- itemRenderer="spark.skins.spark.DefaultComplexItemRenderer" DefaultItemRenderer 或者是自定义的组件,以<s:ItemRender开头,有{data}-->
	<s:layout>
		<s:VerticalLayout/>
	</s:layout>
</s:DataGroup>

function myItemRendererFunction(item:Object):IFactory
{
	if(item is String)
		return new ClassFactory(DefaultItemRenderer);//可是自定义组件 基于 <s:ItemRender 中要有{data} 对应字符数据
	else
		return new ClassFactory(DefaultComplexItemRenderer);
}

<s:DataGroup> 有itemRenderer属性,也可以itemRenderer的子标签,是一样的

<fx:Declarations>
	<s:ArrayList id="myArrayList2">
		<fx:Object name="张三2" img="img/smile.gif"/>
		<fx:Object name="李四2" img="img/new.gif"/>
		<fx:Object name="王五-------------------long" img="img/new.gif"/>
	</s:ArrayList>

<s:DataGroup dataProvider="{myArrayList2}">
	<s:layout>
		<s:HorizontalLayout/>
	</s:layout>
	<s:itemRenderer><!--这个是属性i  这里面的可以定义成一个组件-->
		<fx:Component>
			<s:ItemRenderer> <!--这个是类 I -->
				<s:states>
					<s:State name="normal"/>
					<!--<s:State name="hovered"/>  鼠标滑过-->
				</s:states>
				<s:VGroup>
					<s:Label text="{data.name}" /><!-- 必须是{data} -->
					<s:Image   source="{data.img}"/>
				</s:VGroup>
			</s:ItemRenderer>
		</fx:Component>
	</s:itemRenderer>
</s:DataGroup>


s:List 和 s:ComboBox 都可用itemRender

<s:DataGrid dataProvider="{myArrayList2}"   selectionChange="Alert.show(event.currentTarget.selectedItem.birthDate)" editable="true" >
	<!--选中行时响应 GridSelectionEvent有selectedItem属性,  单元格可以修改,双击默认以文本输入框修改 -->
	<s:typicalItem>
		<s:DataItem name="====-========="  />	<!--以给定值的长度  做为name数据列的宽度显示,只可一个s:DataItem ,可再加其它属性 -->
	</s:typicalItem>
	<s:columns>
		<s:ArrayList>
			<s:GridColumn  dataField="name" headerText="名字"/>
			<s:GridColumn   headerText="文字与图片">
			<s:itemRenderer>
				<fx:Component>
					<s:GridItemRenderer> <!--可以做成组件s:GridItemRenderer为根,  放在s:GridColumn中的itemRenderer属性,s:itemRenderer就不用了-->
						<s:VGroup height="40">
							<s:Label text="{data.name}" /><!-- 必须是{data} -->
							<s:Image   source="{data.img}"/>					
						</s:VGroup>
					</s:GridItemRenderer>
				</fx:Component>
			</s:itemRenderer>
		</s:GridColumn>
		<s:GridColumn   headerText="简单修改"  dataField="workYear" editable="true" rendererIsEditable="true"  ><!--rendererIsEditable="true" 表示双击后以指定的组件来修改-->
			<s:itemRenderer>
				<fx:Component>
					<s:GridItemRenderer>
						<s:NumericStepper value="{data.workYear}" maximum="50	"/>
					</s:GridItemRenderer>
				</fx:Component>
			</s:itemRenderer>
		</s:GridColumn>
		<s:GridColumn   headerText="显示修改" dataField="workYear" editable="true"    >
			<s:itemEditor> 
				<fx:Component>
					<s:GridItemEditor> <!--可以做成组件s:GridItemEditor为根 -->
						<s:NumericStepper value="@{value}" maximum="50	"/>  <!--用@{value} 代表dataField里指定的值 -->
					</s:GridItemEditor>
				</fx:Component>
			</s:itemEditor>
		</s:GridColumn>
		</s:ArrayList>
	</s:columns>
</s:DataGrid>



<mx:Accordion  resizeToContent="true"> 子级放多个s:NavigatorContent,每个可以做成组件, 像老板的QQ
	<s:NavigatorContent label="标题11"> ...


<mx:TabNavigator  是标签页的形式
<mx:ViewStack  id="myViewStack" 是没有切换功能的,要用下面两个
<s:TabBar  dataProvider="{myViewStack}"/>
<s:ButtonBar  dataProvider="{myViewStack}" />

 state 视图,不同的state就是不同的页面

<s:Application currentState=""
	<s:Button onClick="currentState=''"
<s:Panel includeIn="myState1,myState2" 
		或者使用excludeIn="myState3,myState4" 
<s:Button  x="" x.myState1="" click.mySate1="" />
<s:states> <!-- 必须放在定义要么放在组件最前,要么放在组件最后,不可放在中间-->
	<s:State name="mystate1" />
	<s:State name="mystate2" />
</s:states>


width="%" 是以父容器为参考的
<s:Panel minWidth="" maxWidth=""
<s:BasicLayout 
	baseline="" top  bottom left right   如果组件大小已经到了minWidth或者minHeight ,right或bottom就无效了
	horizontalCenter verticalCenter  不使用x,y,设计视图中可以做

spark.effects.包下的动画
<fx:Declarations>
	<s:Animate id="myAnimate"  target="{myMoveTextInput}" ><!-- targes={[,]}-->
		<s:SimpleMotionPath property="x"  valueFrom="0" valueTo="50" />
		<s:SimpleMotionPath property="y"  valueFrom="0" valueTo="20" />
		<s:SimpleMotionPath property="width"   valueBy="20" /> <!--基于当前的值增加20 -->
	</s:Animate>
	<s:Sequence  id="mySequence" target="{myMoveTextInput}"   duration="500">
		<s:Move  xBy="20"/>
		<s:Move  xBy="-20"/>
		<s:Move  xBy="20"/>
		<s:Move  xBy="-20"/>
	</s:Sequence>
	<s:Parallel></s:Parallel>
</fx:Declarations>

<s:transitions>
	<s:Transition  fromState="mystate1" toState="mystate2" autoReverse="true">  
	<!-- autoReverse="true" RemoveAction效果是不对的,必须再加一个s:Transition -->
		<s:Sequence>
			<s:Parallel targets="{[mystate1Button]}" >
				<s:Fade/>
				<s:Move  xTo="-20" />
				<s:Resize target="{transitionTextInput}" widthFrom="100" widthTo="200" />
			</s:Parallel>
			<s:RemoveAction targets="{[mystate1Button]}"  /> <!--可选的mystate2Button,mystate2Label -->
			<s:Parallel targets="{[mystate2Button,mystate2Label]}">
				<s:AddAction/>
				<s:Fade/>
				<s:Move  yFrom="-30"/>
			</s:Parallel>
		</s:Sequence>
	</s:Transition>
</s:transitions>

<s:Label alignmentBaseline="ideographicBottom" <!-- 对齐时底部以p,y的中间为准 -->
http://labs.adobe.com/technologies/textlayout/demos/TextLayout.swf

<s:FormItem label="金额："  sequenceLabel="1)">   显示为  1)金额 并不排序
<s:TextInput id="email"  prompt="请输入邮箱" requiredIndicatorSource="img/a.gif" errorIndicatorSource="img/b.gif" />
		prompt初始灰色显示,单击消失,requiredIndicatorSource必填的图标,errorIndicatorSource错误的图标
<fx:Style>
	@namespace s "library://ns.adobe.com/flex/spark";
	@namespace mx "library://ns.adobe.com/flex/mx";
			
	s|Form{
		skinClass:ClassReference("spark.skins.spark.StackedFormSkin");/*在Form的前面显示错误消息*/
	}
	s|FormHeading
	{
		skinClass:ClassReference("spark.skins.spark.StackedFormHeadingSkin");
	}
	s|FormItem{
		skinClass:ClassReference("spark.skins.spark.StackedFormItemSkin");/*标签和组件各一行共 两行*/
	}
</fx:Style>

<s:SkinnableContainer skinClass="" /> <!-- 以s:Skin 为根 -->

Catalyst可以导出为FXG文件,可以做为flex的组件使用,如skin

s:SkinnableDataContainer 比起s:DataGroup 多skinClass属性,是以s:Skin为根的 (SparkSkin -> Skin)
要有一个id="dataGroup" 类型是DataGroup的Skin Parts  ,必须还要指定itemRender


Flash Builder->Project ->export release build,可以复选view Source,swf运行时右击->view source有目录结构,可以下载
输出目录为bin-release而不是bin-debug  ,文件小很多


动态样式
myLabel.setStyle("color","red");
trace(myLabel.getStyle("fontSize"));

var lableStyleDeclaration:CSSStyleDeclaration =StyleManager.getStyleDeclaration(".lableStyle");//global
trace(lableStyleDeclaration.getStyle("fontSize"));

list.dragEnabled=true;//<s:List 可拖入,拖出数据
list.dropEnabled=true;
list.allowMultipleSelection=true;


全局 常量　　Infinity，-Infinity　NaN undefined
int.MIN_VALUE ,int.MAX_VALUE

try { }
catch (err:Error)  //异常的最高类
{err.message}

var p:Parent=new Parent() ;//dynamic的类可以动态增加属性方法
p.name="lisi";
p.age=20;
p.display=function ():String
		{
			return p.name+":"+p.age;
		}
trace(p.display());	
var student:Object =new Object();//Object是一个dynamic,	public dynamic class Object
student.name="李四";
var o:Object={name:"xx",age:22};

ctrl+shif+c 对<s:进行注释

typeof(message) 结果可能是"string",number,boolean// 如是Object,不是很准

<fx:Declarations>
	<fx:Model id="mybooks">
		<books>
			<book>
				<name>张三</name>
				<author>Flex</author>
			</book>
		</books>
	</fx:Model>
</fx:Declarations>

不使用标签的动态绑定
1.BindingUtils.bindProperty(sliderText,"text",hSlider,"value");//后面的单向绑定前面的
2.BindingUtils.bindSetter(myfunc,hSlider,"value");//hSlider变化时调用函数,
public function myfunc(val:String):void
{
	label_util.text=val;
}	
<s:Label text="{sayHello(myTextInput.text)}" />  //{}中是方法的返回值

C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\themes 目录下有主题 
flex-config.xml文件中修改<theme>部分
	
--------FABridge
右击项目->create ajax bridge
C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\javascript\fabridge\samples

--flex中加入
<fx:Declarations>
	<bridge:FABridge/>   //FABridge.as
</fx:Declarations>


<script type="text/javascript" src="fabridge/javascript/FABridge.js" ></script>
<script type="text/javascript" src="fabridge/swfobject/swfobject.js"></script> 使用google的swfObject库引入swf

<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		id="appTest" width="300" height="200"
		codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
	<param name="movie" value="../../bin-debug/app.swf" />
	<param name="flashvars" value="bridgeName=b_app"/>
	<param name="quality" value="high" />
	 <param name="allowScriptAccess" value="always" />
	
	<embed src="../../bin-debug/app.swf" quality="high"
		width="300" height="200" name="appTest" 
		align="middle"
		play="true"
		loop="false"
		quality="high"
		allowScriptAccess="always"
		type="application/x-shockwave-flash"
		pluginspage="http://www.adobe.com/go/getflashplayer" 
		flashvars="bridgeName=b_app">
	</embed>
</object>
---JS嵌入swf
var flashvars = {};
flashvars.bridgeName = "b_app";//这个是var flexApp=FABriage.b_app.root();//root()方法
var params = { allowscriptaccess : "always" };  //文件是swf仿问JS权限
var attributes = {};
swfobject.embedSWF("../../bin-debug/app.swf", "flashcontent", "400", "400", "9.0.124", "", flashvars, params, attributes);
<div id="flashcontent"></div>



flexApp.testFunc()//flex中的testFunc方法
flexApp.getCheck()//flex中的<s:CheckBox id="check"/>
flexApp.getButton().addEventListener("click", callback);//flex中的<s:Button id="button" 在JS中增加Flex按钮的事件响应JS代码

--Flex4代码
var grid:spark.components.DataGrid=new spark.components.DataGrid();
var col1:spark.components.gridClasses.GridColumn=new spark.components.gridClasses.GridColumn();
col1.dataField="apple";
col1.headerText="苹果";
grid.columns=new mx.collections.ArrayList([col1]);
grid.dataProvider=new mx.collections.ArrayCollection([{apple:1},{apple:2}]);
grid.addEventListener(GridSelectionEvent.SELECTION_CHANGE,function (event:GridSelectionEvent):void
					{
						Alert.show(event.currentTarget.selectedItem.apple);//selectedItem
					});
panel.addElement(grid);
--使用JS代码实现上面的flex4代码,在flex4中的mxml页面中要import所有可能用到的类
var flexApp=FABridge["b_app"];//必须放在函数中,FABriage在刚刚载入时要初始化,才能可使用
//var flexRoot=FABridge["b_app"].root();
var grid =flexApp.create("spark.components.DataGrid");
var col1 =flexApp.create("spark.components.gridClasses.GridColumn");
col1.setDataField("apple");
col1.setHeaderText("苹果");
try{
	//grid.setColumns( [col1] );//不可以
	var cols =flexApp.create("mx.collections.ArrayCollection");//ArrayCollection,ArrayList
	cols.setSource([col1]);
	grid.setColumns(cols);
}catch(e){alert(e.message);}

var datas =flexApp.create("mx.collections.ArrayCollection");
datas.setSource( [ { apple: 12}, { apple: 7} ]);
grid.setDataProvider(datas);
grid.addEventListener("selectionChange", function(event) 
				{
					alert(event.getTarget().getSelectedItem().apple);
				} );
flexRoot.getPanel().addElement(grid);	

--网络连接 URLLoader,URLRequest,URLVariables
//URLLoader doc中的示例
//params.txt is a local file that includes: firstName=Tom&lastName=Jones
var lbl:TextField = new TextField();
var urlRequest:URLRequest = new URLRequest("params.txt");
var urlLoader:URLLoader = new URLLoader();
urlLoader.dataFormat = URLLoaderDataFormat.VARIABLES;
urlLoader.addEventListener(Event.COMPLETE, urlLoader_complete);
urlLoader.load(urlRequest);
 
function urlLoader_complete(evt:Event):void {
    lbl.text = urlLoader.data.lastName + "," + urlLoader.data.firstName;
    addChild(lbl);
}

//URLVariables doc中的示例
var url:String = "http://www.[yourDomain].com/application.jsp";
var request:URLRequest = new URLRequest(url);
var variables:URLVariables = new URLVariables();
variables.exampleSessionId = new Date().getTime();
variables.exampleUserLabel = "guest";
request.data = variables;
navigateToURL(request);//可加第二个参数"_self","_blank","_parent","_top" flash.net包中的,sendToURL




var loader:URLLoader= new URLLoader();;
var request:URLRequest = new URLRequest("urlLoaderExample.txt");
try {
	loader.load(request);
} catch (error:Error) 
{
	textArea.text ="URL Loader错误" ;
}
loader.addEventListener(Event.COMPLETE,function(evt:Event):void
{
	//var data:URLLoader=evt.target as URLLoader;
	var completeLoader:URLLoader=URLLoader(evt.target);
	textArea.text += "\nEvent.COMPLETE 值" + completeLoader.data;
	var vars:URLVariables = new URLVariables(completeLoader.data);
	textArea.text += "____answer is:" + vars.answer;
});
loader.addEventListener(Event.OPEN, function(evt:Event):void
{
	textArea.text+="\nEvent.OPEN 值"+evt;
});
loader.addEventListener(ProgressEvent.PROGRESS, function(evt:ProgressEvent):void
{
	textArea.text+= "\nEvent.PROGRESS 值 loaded:" + evt.bytesLoaded + " total: " + evt.bytesTotal ;
	var percent:int=100 * (evt.bytesLoaded / evt.bytesTotal);
	progressBar.setProgress(percent,100) ;
});

loader.addEventListener(HTTPStatusEvent.HTTP_STATUS,function(evt:HTTPStatusEvent):void
{
	textArea.text+="\nHTTPStatusEvent.HTTP_STATUS值"+evt;
});

loader.addEventListener(SecurityErrorEvent.SECURITY_ERROR,function(evt:SecurityErrorEvent):void
{
	textArea.text+="\nSecurityErrorEvent.SECURITY_ERROR 值"+evt;
});
loader.addEventListener(IOErrorEvent.IO_ERROR, function(evt:IOErrorEvent):void
{
	textArea.text+="\nIOErrorEvent.IO_ERROR 值"+evt;
});
-------文件上传
var uploadFile:FileReference = new FileReference();
uploadFile.addEventListener(Event.SELECT, function(evt:Event):void{stateText = "选择了文件 " + uploadFile.name;});
uploadFile.addEventListener(Event.COMPLETE,  function(evt:Event):void{stateText = "上传完毕";});
uploadFile.addEventListener(ProgressEvent.PROGRESS,  function(evt:ProgressEvent):void
{
	stateText += "已上传 " + (100 * (evt.bytesLoaded / evt.bytesTotal)) + "%\n";
});
uploadFile.addEventListener(IOErrorEvent.IO_ERROR,function(evt:IOErrorEvent):void //服务端错误
{
	Alert.show("上传失败:"+evt);
});
var request:URLRequest = new URLRequest("http://localhost:8080/FlexService/flexupload");
uploadFile.upload(request);//服务器端的错误,这里用try 无效,要addEventLister
--下载
var downloadFile:FileReference = new FileReference();
var request:URLRequest = new URLRequest("http://localhost:8080/FlexService/flexdownload");//动态和静态都 OK
request.method=URLRequestMethod.GET;
request.data=new URLVariables("filename=xx.txt");
downloadFile.download(request,"defaultName.txt");

在 devguid.pdf 手册中 可以FileFilter ,多个同时上传FileReferenceList
var textTypes:FileFilter = new FileFilter("Text Files (*.txt, *.rtf)", "*.txt; *.rtf");
var fileRefList:FileReferenceList = new FileReferenceList();//Event.SELECT 事件时变为FileReferenceList(event.target);
fileRefList.browse(new Array(textTypes));
fileRefList.fileList;一个一个上传

<mx:ProgressBar id="progressBar" maximum="100" minimum="0" mode="manual" />

var dispatcher:EventDispatcher=new EventDispatcher(); 
dispatcher.dispatchEvent(new Event("M"));



ECMAScript for XML (E4X)
XML,
XMLList,
XMLListCollection(XMLList)//只是XMLList的一种封装,可以构造也用source属性赋XMLList
<s:HTTPService id="httpServiceE4x" url="employees.xml" result="httpserviceE4x_resultHandler(event)"  resultFormat="e4x"/>
var dataXML:XMLListCollection=new XMLListCollection();
function httpserviceE4x_resultHandler(event:ResultEvent):void
{
	var xmlList:XMLList=event.result.employee; //e4x
	dataXML.source=xmlList;
	//event.result.employees.employee as ArrayCollection;//默认是ArrayCollection
}
dataXML.refresh();
dataXML.filterFunction=myfun;//或者给null
myfun(item:XML):Boolean
{
	XMLList list= item.(firstname=="lisi" || lastname=="lisi");//E4X操作符  "." ,"[]"是数组,"@"是属性 ,".."是派生
	//<firstname>
}

<s:TextInput enter=""/> 表示按回车时响应的事件 


C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\flash-unicode-table.xml  记录UTF8字符集的表示范围
[Embed(source="assets/迷你简彩蝶.ttf",fontName="myFont",mimeType="application/x-font")]
private var MyFont:Class;

<fx:Style>
	@font-face
	{
		 fontFamily:"youFont";
		 src:url("assets/长城广告体繁体.TTF");
	}
</fx:Style>

<s:Label fontFamily="myFont" text="Embed嵌入字体ABC123" fontSize="30"/>
<s:Label fontFamily="youFont" text="CSS嵌入字体ABC123" fontSize="30"/>


<fx:Declarations>
	<s:Sequence id="myFadeIn">
		<s:Parallel>
			<s:Fade  alphaFrom="0.6" alphaTo="1.0"  duration="1000"/>
			<s:Resize widthTo="200" heightTo="100"/>
		</s:Parallel>
	</s:Sequence>
</fx:Declarations>		
<s:Image rollOverEffect="Fade"  alpha="0.6"  source="img/help.gif"/> <!--鼠标滑入的效果,Fade使用默认的设置-->
<s:Image rollOverEffect="{myFadeIn}"  rollOutEffect="{myFadeOut}" alpha="0.6"  source="img/help.gif"/>


	
----------与Flash Professional,Illustrator一起工作
Flash Professional CS5 中一帧由两个电影剪辑,图的左对上角对与"+"(中心点)对齐, 右击电影剪辑->Properties...->展开Advanced->
选中 Export for ActionScript->Class的值默认与库名相同(man)->Base class是 flash.display.MovieClip->OK,提示自动生成->OK
File->publish setting...->只选择swf->Publish
flex4.6中 icon="@Embed(source='xx.swf',symbol='man')"  或者用　icon="@Embed('xx.swf#man')" 


自定义组件Flash Professional CS5中,
1.电影剪辑要有帧标签up,over,down,disabled,左上角是中心点,第一帧一定要有stop();
2.库中电影剪辑->commands->convert Symbol to Flex Component->在Output窗口中提示Publish成SWC文件,库中会多一个Flex CommponentBase
3.查看 ,右击电影剪辑->Properties...->展开Advanced->Export for ActionScript是选中的,Class的值默认与库名相同(Man),但Base class是 flash.display.UIMovieClip
4.File->publish settings...->要选中SWC->publish
所导出的swc文件放在libs目录中

flex中
<mx:TabNavigator >
	<s:NavigatorContent icon="Man">  //Man 是生成swc文件中的电影剪辑链接的类名
		
<fx:Style>
	.artButton
	{
		upSkin:Embed(source="libs/button_up.jpg");/*只对mx组件有效,可以通过Illustrator(未测试),在flex中import...->MX Skin Artwork 后生成*/
		overSkin:Embed(source="libs/button_over.jpg");
	}
</fx:Style>	
<mx:Button  styleName="artButton" label="修改MX外观"/> 

在flex中import...->MX Skin Artwork 也可导入swf或者swc,
在Flash Professional中create from template->more->Flex skins选择要修改的如ScrollBar,修改后再publish,也会建立CSS
mx|HScrollBar //生成的也是只对mx组件有效,测试好像效果不对
{
	downArrowSkin: Embed(skinClass="HScrollBar_downArrowSkin");
}


//Sprite是容器有addChild();
public class MyShap extends Shape
{
	public function MyShap()
	{
		graphics.clear();
		graphics.lineStyle(1,0x000000);//black
		graphics.beginFill(0xff0000,0.6);
		graphics.moveTo(50,0);
		graphics.lineTo(100,50);
		graphics.lineTo(100,0);
		graphics.lineTo(50,0);
		graphics.endFill();
		graphics.beginGradientFill(GradientType.LINEAR,[0xff0000,0x00ff00],[0.8,0.8],[0,255]);
		graphics.drawCircle(100,150,100);
		graphics.endFill();
	}
}
private var drawShap:MyShap=new MyShap();	
<s:Image source="{drawShap}" />


var share:SharedObject=SharedObject.getLocal("MyAllContact");//保存在浏览中
share.clear();
share.data.myContact=contactName.text;
share.flush();
if(share.size>0)
	contactName.text=share.data.myContact;


var glow:GlowFilter=new GlowFilter(0x0000ff,0.7,10,10);//color,alpha,blurX,blurY
if(glowButton.filters.length==0)
	glowButton.filters=[glow];
else
	glowButton.filters=[];

---------------国际化
C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\flex-config.xml文件中的默认值
<locale>
	<locale-element>en_US</locale-element>
</locale>
<allow-source-path-overlap>false</allow-source-path-overlap>
<library-path>
	<path-element>libs</path-element>   
	<path-element>locale/{locale}</path-element>
<／library-path>


copylocale en_US zh_CN  命令后
C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\locale


!和#都是注释只能是行首,资源文件可以用"=",":"来分隔
D:\program\eclipse_flex4_workspace\F_MyPractise\locale\zh_CN\message.properties
D:\program\eclipse_flex4_workspace\F_MyPractise\locale\en_US\message.properties
#图片名也是区分大小写的
!另\表示一行未完
logo=Embed("assets/logo_en.gif")　
username=username {0} \
good me!
two:two
bgColor=0xff0000


mxmlc编译选项加　-locale=en_US,zh_CN -source-path=D:\program\eclipse_flex4_workspace\F_Locale\locale\{locale}
可选 -include-resource-bundles=global,message

function showAllBundle():void //Debug用
{
	for each (var locale:String in resourceManager.getLocales())
	{
		allBundle.text += "****************************************locale: " + locale + "\n";
		for each (var bundleName:String in resourceManager.getBundleNamesForLocale(locale)) 
		{
			allBundle.text += "--------------------------------------bundleName: " + bundleName + "\n";
			/*
			var bundle:ResourceBundle = ResourceBundle(resourceManager.getResourceBundle(locale, bundleName));
			for (var key:String in bundle.content) 
			{
				allBundle.text +=  key + "=" + bundle.content[key]  + "\n";
			}
			*/	
		}
	
	}        
}
function combobox1_changeHandler(event:IndexChangeEvent):void 
{
	var comb:ComboBox= event.target as ComboBox;
	this.resourceManager.localeChain=[comb.selectedItem]; //["en_US"];//改变当前语言    
}
			
function button1_clickHandler(event:MouseEvent):void
{
	var newBundle:ResourceBundle = new ResourceBundle("en_US", "message");
	newBundle.content["lang"] = "lang___BY Programe";
	resourceManager.addResourceBundle(newBundle);
	resourceManager.update();         
}
<fx:Metadata>
	[ResourceBundle("message")]
</fx:Metadata>
[Bindable]
private var locales:ArrayCollection = new ArrayCollection([ "zh_CN","en_US" ]);
<s:ComboBox dataProvider="{locales}"  change="combobox1_changeHandler(event)"/>
<s:Image source="{resourceManager.getClass('message','logo')}"/>	<!-- getClass-->
<s:Label text="{resourceManager.getString('message','username',['张三'])}"   />
<s:Label text="{resourceManager.getString('message','two')}"   />
<s:Label  text="______" backgroundColor="{resourceManager.getUint('message', 'bgColor')}"/><!-- getUint-->
<mx:DateChooser></mx:DateChooser> <!--DateChooser默认是不会根据当前语言而改变的-->
<mx:DateChooser dayNames="['日','一','二','三','四','五','六']" 
			    monthNames="['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']" >
</mx:DateChooser>
<s:Button  label="动态修改国际化" click="button1_clickHandler(event)" />
根据showAllBundle查看到
bundleName: SharedResources
	monthNames=January,February,March,April,May,June,July,August,September,October,November,December
bundleName: controls
	dayNamesShortest=S,M,T,W,T,F,S

看到C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\locale\automation_agent_rb.swc\locale\zh_CN\controls.properties
	有dayNamesShortest=日,一,二,三,四,五,六
到C:\Program Files\Adobe\Adobe Flash Builder 4.6\sdks\4.6.0\frameworks\locale\framework_rb.swc\locale\zh_CN\SharedResources.properties
	看到monthNames的值为英文的
	monthNames=一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月


--LocalConnection server
localConnection=new LocalConnection();//只是一台机器上的多个swf 或Air的通信
localConnection.send("_myConnection","doSomethingInAir",sendText.text);//第一个连接名连名必须以_开头,与[Client]端相同，第二个是[Client]端方法名，第三个参数

--LocalConnection client
localConnection=new LocalConnection();
localConnection.client=this;//表示客户接收端
localConnection.connect("_myConnection");//连接名必须以_开头,与[Server]端相同
public  function doSomethingInAir(text:String):void //被 [Server]端调用 
{
	clientText.text="从服务端收到:"+text
}

--右建菜单
var menuItem1:ContextMenuItem = new ContextMenuItem("详细", true);
menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function (evt:ContextMenuEvent)
{
	Alert.show("菜单项被点击");
});

var menu:ContextMenu= new ContextMenu();
menu.hideBuiltInItems(); //隐藏放大,打印...
menu.customItems = [menuItem1];
menu.addEventListener(ContextMenuEvent.MENU_SELECT, function(evt:ContextMenuEvent):void
{
	label.text="单击了右键,出了菜单";
}); 
dataGrid.contextMenu=menu;

---web swf的复制
this.addEventListener(KeyboardEvent.KEY_UP,keyFunc);//必须有焦点才会响应

this.stage.focus=richTextEditor;
focusManager.setFocus(textInput);//textInput可以,richTextEditor报错的
var keyboard:KeyboardEvent =new KeyboardEvent(KeyboardEvent.KEY_UP);
keyboard.ctrlKey=true;
keyboard.charCode=118;
this.dispatchEvent(keyboard);//ctrl+v

private function keyFunc(evt:KeyboardEvent):void 
{
	Alert.show(""+evt.charCode);//c=99,v=118
	if(evt.ctrlKey && evt.charCode == 118)//ctrl+v
	{
	}else if(evt.ctrlKey && evt.charCode == 99)//ctrl+c
	{
		System.setClipboard(myLabel.text);
	}
}

----------BlazeDS 4
Flash Builder 和 JavaEE eclipse集成方法
方法一、先建立Flex项目,在JavaEE eclispe中import,project->property->project facade  修改为dynamic web 
		建立新的文件夹flex-src,重新设置flex的源码目录
方法二、先建立Java Web项目,复制BlazeDS模板,再建立Flex项目时选择服务技术为Java BlazeDS,
		Root folder:D:\program\eclipse_flex4_workspace\J_BlazeDS\WebContent
		Root URL:http://localhost:8080/J_BlazeDS
		Context root:/J_BlazeDS        //这个只为swf文件使用
		Output folder:D:\program\eclipse_flex4_workspace\J_BlazeDS\WebContent\F_BlazeDS-debug
		
		查看Flex项目属性的Flex Server标签中修改值
		Flex Complier标签中多加了 -services [services-config.xml文件位置] ,
		-services 这里是绝对位置也就是说一个Flex项目对应一个Java项目,否则很容易忘记修改!!!!!!!!!!!

也就是说services-config.xml文件是服务端和客户端共用的文件
		
blazeds-turnkey 版本 tomcat 默认是8400 端口
ds-console.war  监控的应用
blazeds.war	WEB-INF/flex文件夹,lib文件夹 ,web.xml是用来复制的
blazeds-turnkey-4.0.1.21287\resources\fds-ajax-bridge\javascript\FDMSLib.js  只有turnkey中有,可用JS来调用BlazeDS,依赖于FABrige
blazeds-turnkey-4.0.1.21287\resources\fds-ajax-bridge\actionscript\FDMSBase.as
AMF(Action Message Format)

Java端
----WEB-INF/web.xml中有
<listener>
	<listener-class>flex.messaging.HttpFlexSession</listener-class>
</listener>
<servlet>
	<servlet-name>MessageBrokerServlet</servlet-name>
	<display-name>MessageBrokerServlet</display-name>
	<servlet-class>flex.messaging.MessageBrokerServlet</servlet-class>
	<init-param>
		<param-name>services.configuration.file</param-name>
		<param-value>/WEB-INF/flex/services-config.xml</param-value>
   </init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>MessageBrokerServlet</servlet-name>
	<url-pattern>/messagebroker/*</url-pattern>
</servlet-mapping>



----WEB-INF/flex/services-config.xml中有
<system>
        <redeploy>
            <enabled>false</enabled> 表示如果BlazeDS检测到一些XML配置文件被修改,会自动重新部署
			
----WEB-INF/flex/proxy-config.xml中有
<properties>
	<connection-manager>
		<max-total-connections>100</max-total-connections>
		<default-max-connections-per-host>2</default-max-connections-per-host>//每个客户端最多可以有2个

----WEB-INF/flex/messaging-config.xml中有	
<default-channels>
	<channel ref="my-polling-amf"/>
</default-channels>
----WEB-INF/flex/remoting-config.xml中有  ,文件不同<service class=""是不同的
<default-channels>
   <channel ref="my-amf"/>
</default-channels>
新增
<destination id="hello">
	<properties>
		<source>org.zh.flex.HelloWorld</source>
		<scope>application</scope> <!--session,request-->
	</properties>
</destination> 
	

Flex端
<fx:Declarations>
	<s:RemoteObject id="remoteObj" destination="hello" result="remoteObj_resultHandler(event)"/><!-- result是异步的 -->
	<s:RemoteObject id="remoteObj_1" destination="hello" >
		<s:method  name="sayHello" result="remoteObj_resultHandler(event)" > <!--可以每个方法有自己的处理函数-->
			<s:arguments>
				<arg1>张三</arg1>
			</s:arguments>
		</s:method>
	</s:RemoteObject>
</fx:Declarations>	
protected function remoteObj_resultHandler(event:ResultEvent):void
{
	var res:String=event.result as String;
	Alert.show(res);
}	
<s:Button label="张三测试RemoteObject"  click="remoteObj.sayHello('张三');"/>
<s:Button label="张三测试RemoteObject标签参数"  click="remoteObj_1.sayHello.send();"/>
<s:Button label="李四测试RemoteObject标签参数可被覆盖"  click="remoteObj_1.sayHello('李四');"/>

<s:RemoteObject id="srv" destination="product"/>
<s:DataGrid dataProvider="{srv.getProducts.lastResult}" width="50%" height="300"/>  //lastResult ,另会自动转换对象集合
<s:Button label="RemoteObject得到List对象数据" click="srv.getProducts()"/>

var p:MyProduct=new MyProduct();
p.productId=200;
p.name="修改后的产品名";
p.price=78.90;
srv.updateProduct(p);//参数是一个类

[RemoteClass(alias="org.zh.flex.Product")]  //flex类传递到java类名匹配,也对不同包下相同类名
public class MyProduct

-----
<s:HTTPService id="httpService" destination="proxyGetProduct" useProxy="true" />
<!--如使用了useProxy="true",对应在是在prox-config.xml中配置,也可不加-->

<s:DataGrid dataProvider="{httpService.lastResult.products.product}" width="50%" height="200"/> 
<s:Button label="httpService得到XML数据" click="httpService.send()"/><!--因是url配置,没有方法名 -->

proxy-config.xml 中加的配置
<destination id="proxyGetProduct">   
	<properties>
		<url>/{context.root}/getProducts.jsp</url><!-- 只有在proxy中才能用url -->
	</properties>
</destination>
-----
proxy-config.xml 中加的配置
<destination id="proxySayHello">
	<properties>
		<wsdl>http://localhost:9000/helloWorld?wsdl</wsdl>
		<soap>http://localhost:9000/helloWorld</soap>
	</properties>
	<adapter ref="soap-proxy"/>
</destination>

<s:WebService id="proxyWebService"  destination="proxySayHello" useProxy="true" showBusyCursor="true" fault="Alert.show(event.fault.faultString,'proxySayHello失败')">
	<!-- 初始化时就连接 -->
	<s:operation name="sayHi"  resultFormat="object" result="Alert.show('sayHi response is:'+event.result)" fault="Alert.show(event.fault.faultString,'sayHi失败')">
		<s:request xmlns="http://server.webservice.zh.org/" >
			<text>Machael</text>
		</s:request>
	</s:operation>
</s:WebService>
<mx:Button label="WebServcie代理" click="proxyWebService.sayHi()"/>
-----动态Consumer,读service-config.xml信息
//在flex端可读service-config.xml中的信息
var channel:Object=ServerConfig.getChannel("my-polling-amf");
if (channel is AMFChannel || channel is HTTPChannel)//用 is判断
{
	/*
	<channel-definition id="my-polling-amf" class="mx.messaging.channels.AMFChannel">
		<endpoint url="http://{server.name}:{server.port}/{context.root}/messagebroker/amfpolling" class="flex.messaging.endpoints.AMFEndpoint"/>
		<properties>
			<polling-enabled>true</polling-enabled>
			<polling-interval-seconds>4</polling-interval-seconds>
		</properties>
	</channel-definition>
	*/
	trace("\n endpoint="+channel.endpoint);//http://localhost:8080/J_BlazeDS/messagebroker/amfpolling
	trace("\n id="+channel.id);//my-polling-amf
	trace("\n pollingEnabled="+channel.pollingEnabled);//true
	trace("\n pollingInterval="+channel.pollingInterval);//4000,毫秒
	var fullClassStr:String=flash.utils.getQualifiedClassName(channel)
}
trace("\n fullClassStr="+fullClassStr);//if外也可仿问变量,值为 mx.messaging.channels::AMFChannel

var channelSet:ChannelSet = ServerConfig.getChannelSet("feedInfo");// <destination id="feedInfo">中的Channel
var channels:Array=channelSet.channelIds;

//动态建立Consumer
dynConsumer = new Consumer();
dynConsumer.destination = "feedInfo";
//consumer.subtopic = "";
dynConsumer.channelSet = new ChannelSet(channels);
dynConsumer.addEventListener(MessageEvent.MESSAGE, feedConsumer_messageHandler);
dynConsumer.subscribe();
--取消
dynConsumer.unsubscribe();
dynConsumer.removeEventListener(MessageEvent.MESSAGE, feedConsumer_messageHandler);
dynConsumer.channelSet.disconnectAll();
-----服务器推
messaging-config.xml中加的配置
	<destination id="feed"/>
services-config.xml中默认的配置
	<channel-definition id="my-polling-amf" 
		<polling-interval-seconds>4</polling-interval-seconds>  <!-- 时间会长点-->
		
		
<s:HTTPService id="feedRequest" url="http://127.0.0.1:8080/J_BlazeDS/feedServlet"  result="Alert.show('请求结果'+event.statusCode);"/>
<!-- 要   http://127.0.0.1:8080/crossdomain.xml    <Context docBase="J_BlazeDS" path="/" />-->
<s:Consumer id="feedConsumer" destination="feed" message="feeLabel.text=event.message.body.toString()"/>
<s:Button label="订阅服务器推(生产)消息" click="feedConsumer.subscribe();"/>
<s:Button label="启动种子" click="feedRequest.send({type:'start',timestamp:new Date().time});"/><!-- 防止浏览器缓存 -->
<s:Button label="停止种子" click="feedRequest.send({type:'stop',timestamp:new Date().time});"/>
<s:Label id="feeLabel"/>

Java服务端
import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
Random random=new Random(10);
String clientId= UUIDUtils.createUUID();
for (int i = 0; i < 100; i++)
{
	float feedVal=random.nextFloat();
	AsyncMessage msg = new AsyncMessage();
	msg.setDestination("feed");
	msg.setClientId(clientId);
	msg.setMessageId(UUIDUtils.createUUID());
	msg.setTimestamp(System.currentTimeMillis());
	msg.setBody(feedVal);
	msgBroker.routeMessageToService(msg, null);
	System.out.println("发送了"+feedVal);
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
}
-----
messaging-config.xml中加的配置
	<destination id="chat"/>
	
creationComplete时调用 consumer.subscribe();

protected function sendMsg():void
{
	 var msg:IMessage=new AsyncMessage();
	 msg.body.myKey=textInput.text+"\n";
	 producer.send(msg);
}
protected function consumer_messageHandler(event:MessageEvent):void
{
	var msg:IMessage=event.message;
	textArea.text+=msg.body.myKey;
}
<s:Consumer id="consumer" destination="chat" message="consumer_messageHandler(event)"/>
<s:Producer id="producer" destination="chat" />

<s:TextArea id="textArea" width="50%"  height="200"></s:TextArea>
<s:HGroup>
	<s:TextInput id="textInput"/>
	<s:Button label="Flex端Producer生产消息"  click="sendMsg()"/>
</s:HGroup>

-----JMS OK
protected function jmsConsumer_messageHandler(event:MessageEvent):void
{
	var msg:IMessage=event.message;
	jmsTextArea.text += msg.body.toString()+"\n";
}

protected function jmsProducer_channelConnectHandler(event:ChannelEvent):void
{
	Alert.show("jms producer 连接成功!");	
}
protected function sendJMSTopic():void
{
	var msg:IMessage=new AsyncMessage();
	msg.body=jmsTextInput.text+"\n";
	jmsTopicProducer.send(msg);
}
protected function sendJMSQueue():void
{
	var msg:IMessage=new AsyncMessage();
	msg.body=jmsTextInput.text+"\n";
	jmsQueueProducer.send(msg);
}
<s:HTTPService id="jmsRequest" url="http://127.0.0.1:8080/J_BlazeDS/jmsServlet"  result="Alert.show('请求结果:'+event.statusCode);"/>
<s:ChannelSet id="channelSet" >
	<s:AMFChannel url="http://localhost:8080/J_BlazeDS/messagebroker/amfpolling" />
	<s:StreamingAMFChannel  url="http://localhost:8080/J_BlazeDS/messagebroker/streamingamf"/>
</s:ChannelSet>
<s:Consumer  id="jmsQueueConsumer"  destination="jmsQueue" channelSet="{channelSet}"  message="jmsConsumer_messageHandler(event)" fault="Alert.show('失败信息:'+event.faultString)"/>
<s:Producer id="jmsQueueProducer" destination="jmsQueue" channelConnect="jmsProducer_channelConnectHandler(event)" fault="Alert.show('失败信息:'+event.faultString)"/>
<s:Consumer  id="jmsTopicConsumer"  destination="jmsTopic" channelSet="{channelSet}"  message="jmsConsumer_messageHandler(event)" fault="Alert.show('失败信息:'+event.faultString)"/>
<s:Producer id="jmsTopicProducer" destination="jmsTopic" channelConnect="jmsProducer_channelConnectHandler(event)" fault="Alert.show('失败信息:'+event.faultString)"/>
		

<s:Button label="启动JMS种子Topic,Queue" click="jmsRequest.send({type:'start',timestamp:new Date().time});"/>
<s:Button label="停止JMS种子Topic,Queue" click="jmsRequest.send({type:'stop',timestamp:new Date().time});"/>

<s:Label text="接受到的JMS feed:"/>
<s:TextArea id="jmsTextArea"/>
<s:TextInput id="jmsTextInput"/>

<s:Button label="订阅JMS Queue消息" click="jmsQueueConsumer.subscribe();"  />
<s:Button label="取消订阅JMS Queue消息" click="jmsQueueConsumer.unsubscribe();"  />
<s:Button label="flex产生JMS Queue消息" click="sendJMSQueue()"  />

<s:Button label="订阅JMS Topic消息" click="jmsTopicConsumer.subscribe();"  />
<s:Button label="取消订阅JMS Topic消息" click="jmsTopicConsumer.unsubscribe();"  />
<s:Button label="flex产生JMS Topic消息" click="sendJMSTopic()"  />

messaging-config.xml 	
	打开了<adapter-definition id="jms" class="flex.messaging.services.messaging.adapters.JMSAdapter"/> 的注释 

<destination id="jmsQueue">  
	<adapter ref="jms" />  
	 <properties>  
		<jms>  
			<connection-factory>java:comp/env/jms/flex/ConnectionFactory</connection-factory>  
			<destination-type>Queue</destination-type>  
			<destination-jndi-name>java:comp/env/jms/flex/simplequeue</destination-jndi-name>  
			<message-type>javax.jms.TextMessage</message-type>
			<delivery-mode>NON_PERSISTENT</delivery-mode> <!--queue NON_PERSISTENT --> 
			<message-priority>DEFAULT_PRIORITY</message-priority>  
			<acknowledge-mode>AUTO_ACKNOWLEDGE</acknowledge-mode>  
			<initial-context-environment>  
				<property>  
					<name>Context.SECURITY_PRINCIPAL</name>  
					<value>anonymous</value>  
				</property>  
				<property>  
					<name>Context.SECURITY_CREDENTIALS</name>  
					<value>anonymous</value>  
				</property>  
				<property>  
					<name>Context.INITIAL_CONTEXT_FACTORY</name>  
					<value>org.apache.activemq.jndi.ActiveMQInitialContextFactory</value>  
				</property>  
				<property>  
					<name>Context.PROVIDER_URL</name>  
					<value>tcp://localhost:61616</value>  
				</property>  
			</initial-context-environment>  
		</jms>  
	</properties>  
	<channels>  
		<channel ref="my-amf" />
	</channels>
</destination>
<destination id="jmsTopic">  
	<adapter ref="jms" />  
	<properties>  
		<!--这里的配置是最关键的，只有durable属性设计为true才能实现持久化订阅-->  
		<server>  
			<durable>true</durable>  
		</server>  
		<jms>  
			<connection-factory>java:comp/env/jms/flex/ConnectionFactory</connection-factory>  
			<destination-type>Topic</destination-type>  
			<destination-jndi-name>java:comp/env/jms/flex/simpletopic</destination-jndi-name>  
			<message-type>javax.jms.TextMessage</message-type>
			<!--    -->
			<delivery-mode>PERSISTENT</delivery-mode>  
			<message-priority>DEFAULT_PRIORITY</message-priority>  
			<acknowledge-mode>AUTO_ACKNOWLEDGE</acknowledge-mode>  
			<initial-context-environment>  
				<property>  
					<name>Context.SECURITY_PRINCIPAL</name>  
					<value>anonymous</value>  
				</property>  
				<property>  
					<name>Context.SECURITY_CREDENTIALS</name>  
					<value>anonymous</value>  
				</property>  
				<property>  
					<name>Context.INITIAL_CONTEXT_FACTORY</name>  
					<value>org.apache.activemq.jndi.ActiveMQInitialContextFactory</value>  
				</property>  
				<property>  
					<name>Context.PROVIDER_URL</name>  
					<value>tcp://localhost:61616</value>  
				</property>  
			</initial-context-environment>  
		</jms>  
	</properties>  
	<channels>  
		 <channel ref="my-amf" />
	</channels>
</destination>  
	
tomcat的context.xml文件<Context >下 的 内容
    <Resource name="jms/flex/ConnectionFactory"  
        type="org.apache.activemq.ActiveMQConnectionFactory"  
        description="JMS Connection Factory"  
        factory="org.apache.activemq.jndi.JNDIReferenceFactory"  
        brokerURL="tcp://localhost:61616"  
        brokerName="LocalActiveMQBroker" />
   
	<Resource name="jms/flex/simpletopic"  
        type="org.apache.activemq.command.ActiveMQTopic"  
        description="my Topic"  
        factory="org.apache.activemq.jndi.JNDIReferenceFactory"  
        physicalName="FlexTopic" />  
    
    <Resource name="jms/flex/simplequeue"  
        type="org.apache.activemq.command.ActiveMQQueue"  
        description="my Queue"  
        factory="org.apache.activemq.jndi.JNDIReferenceFactory"  
        physicalName="FlexQueue" />  
        
tomcat server.xml中
	<Context docBase="J_BlazeDS" path="/" /> 
	
要双击apache-activemq-5.5.1\bin\activemq.bat 来启动  
--java common
String jndiConnectionFactory = "java:comp/env/jms/flex/ConnectionFactory";
String jndiTopic = "java:comp/env/jms/flex/simpletopic";
String jndiQueue = "java:comp/env/jms/flex/simplequeue";
String factory="org.apache.activemq.jndi.ActiveMQInitialContextFactory";
String url = "tcp://localhost:61616";
String username="anonymous";
String password="anonymous";

Properties properties = new Properties();
properties.put(Context.INITIAL_CONTEXT_FACTORY,factory);
properties.put(Context.PROVIDER_URL,url);
properties.put(Context.SECURITY_PRINCIPAL,username);
properties.put(Context.SECURITY_CREDENTIALS,password);

Context context = new InitialContext(properties);
Object lookupFactory = context.lookup(jndiConnectionFactory);
ConnectionFactory connectionFactory = (ConnectionFactory) lookupFactory;
Connection connection = connectionFactory.createConnection();


//---queue
boolean transacted = false;
Session session = connection.createSession(transacted,  Session.AUTO_ACKNOWLEDGE);
Destination queue = (Destination)context.lookup(jndiQueue);
MessageProducer producer  = session.createProducer(queue);
connection.start();
Random random=new Random(10);
while(runing)
{
	float feedVal=random.nextFloat();
	
	TextMessage textMessage = session.createTextMessage();
	textMessage.clearBody();
	textMessage.setText(String.valueOf(feedVal));
	System.out.println("JMS Queue:"+feedVal);
	producer.send(textMessage);
	Thread.sleep(1000);
}
producer.close();
session.close();
connection.close();

//---topic
boolean transacted = true;
Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE); 
Destination topic = (Destination)context.lookup(jndiTopic);
MessageProducer producer = session.createProducer(topic); 
//producer.setDeliveryMode(DeliveryMode.PERSISTENT); //设置保存消息 ,这个可以不写的，如先subscript是durable的，这里就放到durable里
connection.start(); //设置完了后，才连接  

Random random=new Random(10);
while(runing)
{
	float feedVal=random.nextFloat();
	TextMessage textMessage=session.createTextMessage();
	textMessage.setText(String.valueOf(feedVal));
	producer.send(textMessage);
	if (transacted)
	{
		session.commit();//不提交的话只在 .close();提交,就有缓存
	}
	System.out.println("JMS Topic:"+feedVal);
	Thread.sleep(1000);
}
producer.close();
session.close();     
connection.close();    
-------
services-config.xml中加了
<services>
  	<service class="org.zh.flex.DynamicCheckBootstrapService" id="dynamicProductService" />
  
public class DynamicCheckBootstrapService extends AbstractBootstrapService 
{
	@Override
	public void initialize(String id, ConfigMap properties) 
	{
		//这里可以用来做数据库是否连接成功的检查
		System.out.println("  Service 的 initialize 的自定义工作");
		
		RemotingService remotingService = (RemotingService) getMessageBroker().getService("remoting-service");//remote-config.xml中的id的值
	    RemotingDestination destination = (RemotingDestination) remotingService.createDestination(id);//id的值是dynamicProductService
	   //对应<service class="org.zh.flex.DynamicCheckService" id="dynamicProductService" />
        destination.setSource("org.zh.flex.ProductService");
        /*相当于
  	  	<destination id="dynamicProductService">
	  		 <properties>
	  			<source>org.zh.flex.ProductService</source>
         */
	}
	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
}

[Bindable]
var dynRemote:RemoteObject;
protected function  dynamicRemote():void
{
	var channel:AMFChannel = new AMFChannel("my-amf", "../messagebroker/amf");
	var channelSet:ChannelSet = new ChannelSet();
	channelSet.addChannel(channel);
	dynRemote=new RemoteObject();
	dynRemote.destination="dynamicProductService";
	dynRemote.channelSet = channelSet;
	dynRemote.getProducts();
}
<s:DataGrid id="dynamicTable" width="50%" height="150" dataProvider="{dynRemote.getProducts.lastResult}"/>
<s:Button label="动态建立RemoteObject连接动态Destination" click="dynamicRemote()"/>



<s:RemoteObject id="remoteObject"  destination="myDes" source="org.zh." endpoint="http://" >  //source...
remoteObject.[remoteMethoddName].lastResult
默认Flex以ArrayCollection来表示数据
也可加result事件处理函数,但要event.result as ArrayCollection

--------BlazeDS  Spring
看spring-flex-2.5.2的reference
spring-flex-1.5.2.RELEASE\samples\spring-flex-testdrive\testdrive\src  只这个下面是有Java示例代码
blazeds-turnkey-4.0.1.21287\tomcat\webapps\samples-spring\WEB-INF\src和flex-src


blazeds-bin-4.0.1.21287\blazeds-spring\WEB-INF 是用来复制的
web.xml中使用Spring MVC的总控制器,不需要配置BlazeDS的MessageBrokerServlet
	<context-param>
		<param-name>log4jConfigLocation</param-name>
		<param-value>classpath:log4j.properties</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
	</listener>
	
	<listener>
        <listener-class>flex.messaging.HttpFlexSession</listener-class>
    </listener>

	<context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/*-config.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>flex</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>/WEB-INF/spring-flex.xml</param-value> 
		</init-param><!-- 不配置默认是找 flex-servlet.xml文件-->
        <load-on-startup>1</load-on-startup>
    </servlet>
 	<servlet-mapping>
        <servlet-name>flex</servlet-name>
        <url-pattern>/messagebroker/*</url-pattern>
     </servlet-mapping>
	 
spring-flex.xml文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:flex="http://www.springframework.org/schema/flex"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context 
		http://www.springframework.org/schema/context/spring-context-3.0.xsd
		http://www.springframework.org/schema/flex 
		http://www.springframework.org/schema/flex/spring-flex-1.5.xsd"><!--加spring-context, 修改spring-flex-1.5.xsd -->
 
 	<context:component-scan base-package="org.zh.flex.spring"/>
	<context:annotation-config />
	
	<flex:message-broker><!-- services-config-path="/WEB-INF/flex/services-config.xml"是默认值,可 services-config-path="classpath*:services-config.xml" -->
	  	<flex:mapping pattern="/*" /> <!-- 如web.xml是/messagebroker/* 则这里就是/* ,如web.xml是/* 则这里就是/messagebroker/*  -->
	    <flex:remoting-service default-channels="my-amf"/>
	    <flex:message-service default-channels="my-polling-amf" />
		<flex:secured />
	</flex:message-broker>
	 
	<!-- 使用了annotation就不用这个,否则报  productService 已有   -->
	<bean id="myProductService"  class="org.zh.flex.spring.SpringProductService">
	</bean>
	<flex:remoting-destination  destination-id="productService" ref="myProductService" channels="my-polling-amf"/> 
					<!-- 如不指定destination-id ,默认值为ref的值 -->


package org.zh.flex.spring;					
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;			
/*
@Service  
@RemotingDestination(value="productService") 
*/ 
public class SpringProductService 
{
	//@RemotingInclude  
	public List<Product> getProducts()  
	{
		List<Product> list = new ArrayList<Product>();
		for (int i=0;i<5;i++) 
		{
			list.add(new Product(i,
					"name"+i,
					10.25+i));
		}
		return list;
	}
}				


<s:ChannelSet id="channelSet" >
	<s:AMFChannel url="http://localhost:8080/J_BlazeDS-Spring/messagebroker/amf" />
</s:ChannelSet>
<s:RemoteObject id="srv" destination="productService" channelSet="{channelSet}"/> 
<!--使用channelSet或者使用 endpoint="http://localhost:8080/J_BlazeDS-Spring/messagebroker/amf" -->

<s:DataGrid dataProvider="{srv.getProducts.lastResult}" width="50%" height="300"/> 
<s:Button label="Spring集成RemoteObject得到List对象数据" click="srv.getProducts()"/>

现在和示例不同的是 没有channelSet或者endpoint是不行的,除services-config.xml的其它文件还要有???????????
--spring flex,jms 不用配置tomcat,也不用启动ActiveMQ
<!-- id="connectionFactory"是必须 的 -->
<bean id="connectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
	<property name="brokerURL" value="vm:(broker:(tcp://localhost:61616)?persistent=false)?marshal=false"/>
</bean>
 <bean id="chatTopic" class="org.apache.activemq.command.ActiveMQTopic">
	<constructor-arg value="jms/springTopic"/>
</bean>
<flex:jms-message-destination id="jms-chat" jms-destination="chatTopic" />


private function messageHandler(message:IMessage):void
{
	log.text += message.body.userId + ": " + message.body.chatMessage + "\n";	
}
protected function application1_creationCompleteHandler(event:FlexEvent):void
{
	consumer.subscribe();
}
private function send():void
{
	var message:IMessage = new AsyncMessage();
	message.body.userId = userId.text;
	message.body.chatMessage = msg.text;
	producer.send(message);
	msg.text = "";
}
<mx:ChannelSet id="cs">
	<mx:StreamingAMFChannel url="http://localhost:8080/J_BlazeDS-Spring/messagebroker/streamingamf"/>
	<mx:AMFChannel url="http://localhost:8080/J_BlazeDS-Spring/messagebroker/amflongpolling"/>
	<mx:AMFChannel url="http://localhost:8080/J_BlazeDS-Spring/messagebroker/amfpolling"/>
</mx:ChannelSet>
<mx:Producer id="producer" destination="jms-chat" channelSet="{cs}"/>
<mx:Consumer id="consumer" destination="jms-chat" channelSet="{cs}" message="messageHandler(event.message)"/>

<s:TextArea id="log" width="50%" height="50%" borderVisible="true"/>
<s:HGroup>
	<s:TextInput id="userId" width="100"/>
	<s:TextInput id="msg" width="500"/>
</s:HGroup>
<s:Button label="Send" click="send()"/> 





--使用Spring提供的MessageTemplate
<flex:message-destination id="simple-feed" />
<bean id="messageTemplate" class="org.springframework.flex.messaging.MessageTemplate" />
<bean id="mySpringMessage" class="org.zh.flex.spring.MySpringMessage">
	<constructor-arg ref="messageTemplate"></constructor-arg>
	<flex:remoting-destination />
</bean>
	
	
public class MySpringMessage 
{
	MessageTemplate template;
	public  MySpringMessage(MessageTemplate template) 
	{
		this.template = template;
	}  
	public void sendMsg()
	{
		final Random random=new Random();//内部类仿问外类的属性,加final
		final int begin=555;
		final int end=559;
		int index = begin + random.nextInt(end - begin + 1);
		template.send("simple-feed",index);
		template.send(new AsyncMessageCreator() 
					{
		                public AsyncMessage createMessage() 
		                {
		                    AsyncMessage msg = template.createMessageForDestination("simple-feed");
		                    msg.setHeader("myheader", "IBM");
		                    int val = begin + random.nextInt(end - begin + 1);
		                    msg.setBody(Integer.valueOf(val));
		                    return msg;
		                }
		            });
	}
	
}
private function springMessageHandler(message:IMessage):void
{
	if(message.headers.hasOwnProperty("myheader"))
		log.text += message.headers.myheader+ ":";	
	log.text += message.body+ "\n";	
}
<s:RemoteObject id="simpleFeedStarter" destination="mySpringMessage" channelSet="{cs}"/>
<s:Consumer id="springConsumer" destination="simple-feed" channelSet="{cs}" message="springMessageHandler(event.message)"/>

<s:Button label="subscrib spring destination" click="springConsumer.subscribe()"/> 
<s:Button label="spring remoteObject sendMsg" click="simpleFeedStarter.sendMsg()"/> <!-- 远程的方法 sendMsg() -->	
		
		
-------------------Air
Air应用 是<s:WindowedApplication 标签开头的,会生成一个与.mxml同名的.xml配置文件
<s:WindowedApplication  pageTitle="我的Air测试"
						width="500" height="300"
						backgroundAlpha="0.5" 默认是不行的,
要修改同名.xml配置文件
<systemChrome>none</systemChrome>
<transparent>true</transparent>
当export ->Release Build为.air文件,会要数字签名,可点create...创建,会生成文件*.p12文件

private function copy(text:String):void
{
	Clipboard.generalClipboard.clear();//Clipboard只对Air可行
	Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, text);
}

private function paste():String
{
	if(Clipboard.generalClipboard.hasFormat(ClipboardFormats.TEXT_FORMAT))
	{
		return String(Clipboard.generalClipboard.getData(ClipboardFormats.TEXT_FORMAT));
	} else 
		return null;
}

----File
import flash.filesystem.File;
import flash.filesystem.FileMode;
import flash.filesystem.FileStream;
private function writeFile():void
{
	var file:File = new File("c:\\temp\\writeByAir.txt");//若没有此文件就创建它  
	var stream:FileStream = new FileStream();
	stream.open(file, FileMode.WRITE);
	stream.writeUTFBytes(textArea.text);  
	stream.close(); 
}
private function deleteFile():void
{
	var file2:File = new File("c:\\temp\\writeByAir.txt");
	if(file2.exists)
		file2.deleteFile()
}
private function copyFile():void
{
	var source:File = new File(File.applicationDirectory.nativePath + "/test.txt");
	var dest:File = new File(File.applicationDirectory.nativePath + "/test_2.txt");
	source.addEventListener(Event.COMPLETE,function()
				{
						Alert.show("拷贝文件完成")
				});
	source.copyToAsync(dest,true);//异步
}
private function listFile():void
{
	var dir:File = new File(File.applicationDirectory.nativePath);
	var files:Array=dir.getDirectoryListing();
	for(var i:int=0;i<files.length;i++)
	{
		var item:File=files[i];
		textArea.text+=item.name+"\n";
	}
}
private function readFile():void
{
	var file2:File =File.desktopDirectory.resolvePath("test.txt");
	trace(file2.nativePath);//C:\Documents and Settings\476425\Desktop\test.txt
	trace(file2.exists);//false
	
	trace(File.applicationStorageDirectory.nativePath);//C:\Documents and Settings\xxxx\Application Data\F.Air\Local Store
	var nativePath=File.applicationDirectory.nativePath;//src编译后的目录下,bin-debug
	var file:File = new File(nativePath + "/test.txt");//
	var stream:FileStream = new FileStream(); 
	stream.open(file,FileMode.READ);
	textArea.text = stream.readUTFBytes(stream.bytesAvailable);
	stream.close();   
}
<s:TextArea id="textArea" width="300" height="200" />

--------Cairngorm 3
--------FUnit  Flash Builder 4 自带的

性能测试:SoupUI 可以发 AMF 
--------Open Source Media Framework (OSMF)  for Flex 4.0
flex 4.6 自带的 org.osmf.*包下的
--------Flash Media Server, Red5  流媒体服务器
RTMFP协议Real Time Media Flow Protocol	
--------stage3D 的实现  Away3D
编译时加-swf-version=13
最新的playerglobal.swc 和 flash player 11.x
在/html-template/index.template.html中的swfObject对象中新增加如下参数(48行下面)
params.wmode = "direct";
--------Zend AMF (amfphp,WebORB,PureMVC)

快速排序
public function qSort(arr:Array):void
{
		quickSort(arr, 0, arr.length - 1);
}


private function quickSort(data:Array, left:int, right:int):void
{
		var temp:Number = data[left];
		var p:int = left;
		var i:int = left, j:int = right;

		while (i <= j)
		{
				while (data[j] >= temp && j >= p)
						j--;
				if (j >= p)
				{
						data[p] = data[j];
						p = j;
				}

				while (data[i] <= temp && i <= p)
						i++;
				if (i <= p)
				{
						data[p] = data[i];
						p = i;
				}
		}
		data[p] = temp;
		if (p - left > 1)
		{
				quickSort(data, left, p - 1);
		}
		if (right - p > 1)
		{
				quickSort(data, p + 1, right);
		}
}
