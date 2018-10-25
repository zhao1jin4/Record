
JS 格式化工具eclipse可做 
JS 代码变量压缩混肴, java -jar yuicompressor-2.4.8.jar my.js -o my-min.js --charset utf-8
JS解压 eclispe有时不行的,用iBox在线工具http://tool.lu/js/    但变量名是a,b没办法了


-------Antechinus工具
	ctrl+/  生成可折叠的块
	IE->internet选项->高级->取消两个禁止调试
---------Firefox  使用 
Firefox 工具->选项->内容 禁用JavaScript 然后刷新页面就行了
firefox ->firebug中以选中一个区域 (如是encodeURI("test.jsp?username=张三")) ,右击->add Watch
---------Chrome  使用
about:memory
调试JS 按Ctrl+Shift+J 快捷键弹出开发工具
-----IE使用
工具-->Internet选项-->高级 ,将（显示每个脚本错误的通知）一项的勾去掉   
工具-->internet选项->高级->总是以 UTF-8 发送URL ,打勾


在JS中写debugger;  

苹果机中用JS debug
defaults write com.apple.Safari IncludeDebugMenu 1
可以显示 safari的debug菜单->打开"Log Javascript Exceptions" 



============Ajax
 

属性
	onreadystatechange//属性为只写,其它属性为只读
	readState		
		0未初始化
		1初始化
		2发送数据
		3数据传送中
		4传送完成
	responseBody    //将回应信息正文以unsinged byte 数组形式返回
	responseStream
	responseText
	responseXML
	status			//404 ,500,503
	statusText
方法
	abort   取消当前请求
	getAllResponseHeaders
	getResponseHeader
	open("GET","test.jsp",false);
	send(null);
	setRequestHeader

POST 方式
	open("POST","test.jsp",false);//要用send加参数 ,第三参数是否异步,false同步
	setRequestHeader("Content-Type","application/x-www-form-urlencoded")//open方法后 调用   
	send("key1=value1&key2=value2");

//示例1
if(window.XMLHttpRequest) //Mozilla,firefox,opera,safari,IE7,IE8 浏览器
{ 
	http_request = new XMLHttpRequest();
	if (http_request.overrideMimeType)
	{
		http_request.overrideMimeType('text/xml');//设置MiME类别
	}
}
else if (window.ActiveXObject) // IE浏览器
{ 
	try 
	{
		http_request = new ActiveXObject("Msxml2.XMLHTTP");//IE6.0
			//new ActiveXObject("Msxml2.XMLHTTP");//慎用
	} catch (e) 
	{
		try 
		{
			http_request = new ActiveXObject("Microsoft.XMLHTTP"); //IE5.5
		} catch (e) {}
	}
}
//示例2
function createXmlHttp()
{
	if (window.XMLHttpRequest)
	{
	  xmlHttp=new XMLHttpRequest();// code for IE7, Firefox, Opera, etc.
	}
	else if (window.ActiveXObject)
	{
	  xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");// code for IE6, IE5
	}
	if (xmlHttp==null)
	{
		  alert("Your browser does not support XMLHTTP.");
	}
	return xmlHttp;
}
function ajaxJSONRequest()//OK
{
	var xmlHttp=createXmlHttp();
	xmlHttp.onreadystatechange=function()
	{
		if(xmlHttp.readyState!=4) 
			return;
		if(xmlHttp.status!=200)
		{
			alert("error:"+xmlHttp.statusText);
		 	return;
		}
		alert("success:"+xmlHttp.responseText);
	}
	xmlHttp.open("POST", root+"/json/queryEmployeeVO.mvc", false);//是否异步,true 异步,false同步
	xmlHttp.setRequestHeader("Content-Type","application/json;charset=UTF-8");//请求是JSON 
	xmlHttp.send('{"employee_id":123,"first_name":"李四"}');
}

function getXML()
{
	var xmlHttp=createXmlHttp();
	xmlHttp.onreadystatechange=function() //在send之前
	{
			if(xmlHttp.readyState!=4) return;
			if(xmlHttp.status!=200)
			{
				alert("Problem retrieving XML data");
			 	return;
			}

			/*
			//---firefox OK， <?xml　和　response.setContentType("text/xml;charset=UTF-8");　　都可有可无
			var doc=xmlHttp.responseXML.documentElement;//IE 一定不能有<?xml，也 OK
			var status=doc.getElementsByTagName("status");
			alert(status[0].textContent);//firefox是textContent
			*/



			//----IE 测试OK  一定不能有<?xml . 要服务端一定要有　response.setContentType("text/xml;charset=UTF-8");
			var objXml=xmlHttp.responseXML;
			//objXml.childNodes[0].firstChild.textContent;//firefox 是的结果是OK
			//objXml.getElementsByTagName("status")[0].textContent;//firefox 是的结果是OK
			//objXml.childNodes[0].firstChild.text;//IE的结果是OK
			//objXml.getElementsByTagName("status")[0].text;//IE的结果是OK


			var xmlDoc;
			if (window.ActiveXObject)
			  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			else if (document.implementation.createDocument)
			  xmlDoc=document.implementation.createDocument("","",null);
			else
			  alert('Your browser cannot handle this script');
			xmlDoc.async=false;
			xmlDoc.load(objXml); //xmlDoc有一个parseError.reason属性
			alert(xmlDoc.getElementsByTagName("status")[0]);
			//.getAttribute("id");
			var status=xmlDoc.getElementsByTagName("status")[0].text;//IE 是text属性
						
	};	


	/*
	//firefox true is OK,false not work
	//IE true,false OK,
	xmlHttp.open("GET", "xmlServlet?username="+encodeURI(encodeURI('李')), true);//是否异步,true 异步,false同步
	xmlHttp.send(null);//GET 用null  发送
	*/
	
	xmlHttp.open("POST", "xmlServlet", true);//是否异步,true 异步,false同步
	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");//open方法后 调用
	xmlHttp.send("username="+encodeURI(encodeURI('李')));
}
//如果返回的XML文件中属性，JS使用 getAttribute("id")

//-------java  DOM4j
String username=req.getParameter("username");
System.out.println("username:"+URLDecoder.decode(username,"UTF-8"));

Document document = DocumentHelper.createDocument();
Element root = document.addElement("root");
Element status=root.addElement("status");
Element desc=root.addElement("description");
status.addText("OK");
desc.addText(URLEncoder.encode("中文","UTF-8"));
//desc.addAttribute("id","1");
OutputFormat xmlFormat = new OutputFormat();  
xmlFormat.setEncoding("UTF-8");  
XMLWriter xmlWriter;
xmlWriter = new XMLWriter(resp.getWriter(),xmlFormat);
xmlWriter.write(document);  
xmlWriter.close(); 
//-------java

//中文OK
<root><status>OK</status><description>%E4%B8%AD%E6%96%87</description></root>

 

//请求后
xmlhttp.getResponseHeader('Last-Modified');

JS解析XML
// java 代码 response.setContentType("text/xml;charset=utf-8")

nodeType == 1 是ELEMENT
（节点1）parNode.insertBefore(tbody（要加的节点）,parNode（哪个的节点的前面）.firstChild); 


var text = (new XMLSerializer()).serializeToString(element)
XML 文档或 Node 对象转化或“序列化”为未解析的 XML 标记的一个字符串。
IE 不支持 XMLSerializer 对象

==============上 Ajax

window.onerror=function()
{
	alert("error");
}

try
{
}catch(exception)
{
	var str="";
	for (var i in exception)
		str+=i+":"+exception[i]+"\n";
	alert(str);

}


//在浏览器中的返回铵钮中加地地址,返回时页面不刷新
history.pushState({}, "页面标题", "xxx.html");

history.pushState
history.replaceState


//只能输入半角
<input type="text" onkeyup="value=value.replace(/[^\u0000-\u00FF]/g,'')"  
	onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\u0000-\u00FF]/g,''))"/>

var strTmp = "我们是全角字符。wo men shi ban jiao zi fu.";
for (var i=0; i<strTmp.length; i++) {
	if (strTmp.charCodeAt(i) > 128)
		window.alert("全角字符：" + strTmp.charAt(i));
	else
		window.alert("半角字符：" + strTmp.charAt(i));
}
str="中文;；Ａ" 
alert(str.match(/[\u0000-\u00FF]/g))  //半角
alert(str.match(/[\u4E00-\u9FA5]/g))  //中文
alert(str.match(/[\uFF00-\uFFFF]/g))  //全角



if (window.clipboardData) {   //windows
	 window.clipboardData.setData("Text",s);  
}   



--------- IE和Firefox区别
firefox中如要实现parentElement<a>在<td>中,用 parentNode
要知道自己这个td 在table里的第几个呢,用  cellIndex,rowIndex
firefox用childNodes,不要用cells(j),firstChild

filter: alpha(opacity=xx); 只IE
opacity:0.3; 对Firefox,Opera

style.width=1px;只IE
size=1; //IE和Firefox,但最小是1

对firefox来说style.posLeft相对父容器，style.left才是绝对,而IE则都是绝对的

所有同时加;opacity:0.3;filter: alpha(opacity=30);
----
firefox可用  console.log("测试日志"); 显示debug console中
debugger;


 


=================================JavaScript
try{
	abc
}catch(e)
{
	alert(e);
}

//多浏览器
if(navigator.userAgent.indexOf("MSIE")>0) 
{ 
	 alert("MSIE"); 
}else if(isFirefox=navigator.userAgent.indexOf("Firefox")>0)
{ 
	alert("Firefox"); 
}else if(isSafari=navigator.userAgent.indexOf("WebKit")>0)
{ 
	 alert("WebKit ,is Safari or Chrome"); 
}
<script language="javascript" type="text/javascript">
//建立对象几种方式
//1.
var json={name:"json"};
alert(json.name);

//2.
var obj=new Object();
obj.name="Object";
alert(obj.name);

//3.	
function Person()
{
	this.name="function->this";
	this.getInfo=function(){
		alert(this.name);
		}
}
var p=new Person();
p.getInfo();

//4.
function Man()
{
}
Man.prototype.name="function->prototype";
var m=new Man();
alert(m.name);
//全部可以用.prototype加

</script>
 

<script  type="text/javascript">
//继承方式1 prototype
function Parent()
{
	this.p="in Parent";
}
function Child()
{
	this.c="in Child";
}
Child.prototype=new Parent(); //prototype来做
var obj=new Child();
alert(obj.p);
alert(obj.c);


//继承方式2 ,function
function People(username)
{
	this.username=username;
	this.sayhello=function(){
		alert(this.username);
		}
}
function Man(username,password)
{
	this.method=Parent;
	this.method(username);//调用父类方法
	delete this.method; //删除这个属性/方法
	
	this.password=password;
	this.sayhello=function(){
		alert(this.password);
		}
}
var m=new Man("Man","Man123");
m.sayhello();

//继承方式3 ,Function的call方法
function Animal(username)
{
	this.username=username;
	this.sayhello=function(){alert(this.username);}
}
function Cat(username,password)
{
	Animal.call(this,username);//传参方法,第一参数传子类this,后面是父类的参数
	this.password=password;
	this.sayhello=function(){
		alert(this.password);
		}
}
var ca=new Cat("Cat","Cat123");
ca.sayhello();

//继承方式4 ,Function的apply方法
function Water(username)
{
	this.username=username;
	this.sayhello=function(){alert(this.username);}
}
function Cola(username,password)
{
	Animal.apply(this,new Array(username));//apply参数以数组传
	this.password=password;
	this.sayhello=function(){
		alert(this.password);
		}
}
var co=new Cola("Cola","Cola123");
co.sayhello();
//多种混合使用
function Paper(hello)
{
	this.hello=hello;
}
Paper.prototype.sayhello=function(){
		alert(this.hello);
	}
function Book(hello,world)

	Paper.call(this,hello);//调用父类方法
	this.world=world;
}
Book.prototype=new Paper();//继承父类
Book.prototype.sayworld=function(){
	alert(this.world);
}
var book=new Book("hi","this is a book");
book.sayhello();
book.sayworld();


//闭包    f2函数，就是闭包
//f1中的局部变量n一直保存在内存中，并没有在f1调用后被自动清除。 f2被赋给了一个全局变量
  function f1(){

    var n=999;

    nAdd=function(){n+=1}

    function f2(){
      alert(n);
    }

    return f2;

  }

  var result=f1();

  result(); // 999

  nAdd();//有全局变量,则可以直接调用

  result(); // 1000
  
   

</script>

 

testObj = {
prop2 : "hello2",
prop3 : new Array("helloa",1,2)
}
for(x in testObj) 
	alert( x + "-" + testObj[ x ] )
var prop3 = testObj["prop3"];
alert(typeof(prop3));	//[Object]
alert(eval(prop3)[1]);	//1
alert(typeof(eval(prop3)[1]));//number


删除数组，把length=0就可以了

new Date();

var dateFrom=Date.parse('2011-10-10T14:48:00');  //字串转日期的格式 
var dateTo=Date.parse('2012-10-10 14:48:00');
getDate()   //1 ~ 31
getMonth() 	//月份 (0 ~ 11)。
getFullYear() 	//四位数字返回年份
getHours() 	//小时 (0 ~ 23)。
getMinutes() 	//分钟 (0 ~ 59)。
getSeconds() 	//秒数 (0 ~ 59)。
getMilliseconds() 	//毫秒(0 ~ 999)。

now.setHours(0,0,0,0);//清时分秒，毫秒


toUpperCase();

<script  type="text/javascript">
  document.onkeydown=function(event)
  {
	 var e = event || window.event || arguments.callee.caller.arguments[0];
	if(e && e.keyCode==27){ // 按 Esc 
		alert('Esc'); 
	} 
   if(e && e.keyCode==65){ // 按 a 
		var keyCode= e.keyCode ; 
		var keyValue = String.fromCharCode(e.keyCode); 
		alert("code: " + keyCode + " val: " + keyValue);  //val:A
	}
 }; 
 
 document.onkeydown=function(event)
 { 
    if (event.keyCode == 13 && event.ctrlKey) {
       alert("你按下了ctrl+enter");
    }
	 if (event.keyCode == 32 && event.ctrlKey&& event.altKey&& event.shiftKey) {
        alert("你按下了ctrl+alt+shift+space");
     }
 }
	 
function addTableRow()
{
	var tableId=document.getElementById("myTableId");
	
	//var myrow=tableId.insertRow();//只对IE
	var myrow= document.createElement("tr");
	
	//var c1=myrow.insertCell();//只对IE
	var c1=document.createElement("td");
	var c2=document.createElement("td");
	var c3=document.createElement("td");
	myrow.appendChild(c1);
	myrow.appendChild(c2);
	myrow.appendChild(c3);
	tableId.appendChild(myrow);
	
	c1.innerHTML="<font color='blue'>22</font>";
	c2.innerHTML="<font color='blue'>lisi</font>";
	c3.innerHTML='<input type="button" value="删除" onclick="deleteRowItem(this)">';
}
function deleteRowItem(btn)
{
	var tr=btn.parentElement.parentElement;//btn.parentNode;
	tr.parentElement.removeChild(tr);
}


function deleteAllTableRow()
{
	var tableId=document.getElementById("myTableId");
	while(tableId.hasChildNodes())
	{
		tableId.removeChild(tableId.lastChild);
	}
}
function createSelect()
{
	var mySelect=document.getElementById("mySelect");
	
	var oOption = document.createElement("OPTION");
	oOption.text ='工工工工';
	oOption.value = '1';

	mySelect.options.add(oOption);//用 options.add()
	var opt=new Option("文本","值");
	mySelect.options.add(opt);
}
function deleteOptions()
{
	var mySelect=document.getElementById("mySelect");
	mySelect.options.length=0;//清空
}
function dynamicAddEvent()
{
	var myLabe=document.getElementById("myLabel");
	if(window.addEventListener) //Firefox,Chrome 执行顺序11->12
	{
		//element.addEventListener(type,listener,useCapture);
		myLabe.addEventListener('click',function(){alert('Firefox,Chrome 11')}, false);//这是click
		myLabe.addEventListener('click',function(){alert('Firefox,Chrome 12')}, false);
	}else//IE 执行顺序22 -> 21
	{
		myLabe.attachEvent('onclick', function(){alert('IE 21');});//这是onclick
		myLabe.attachEvent('onclick', function(){alert('IE 22');});
	}
}

function batchFunction()
{
	for(var i=0;i<5;i++) 
	{ 
	    var f= function(i)//function可在另一个function中
	   {   
	        return function()
			{   
	                alert(i);   
	        }//可以return 一个函数
	  	} 
	    // document.getElementById("btn"+i).onclick = Function("alert('hello');");//非动态,JS通用的,大写F,可用eval
	   if(window.addEventListener)
	   {
			document.getElementById("btn"+i).addEventListener("click",f(i),false);
			//document.getElementById("btn"+i).addEventListener("click",new Function("abc("+i+")"),false); //大写Function
	   }else
	   {
		   document.getElementById("btn"+i).attachEvent("onclick",f(i));
		   //document.getElementById("btn"+i).attachEvent("onclick",new Function("abc("+i+")")); //大写F
	   }
	    
	}
}
function parseXML()
{
	var xmlDoc;
	if (window.ActiveXObject)//IE
	{
	  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	}
	else if (document.implementation.createDocument)//Firefox
	{
	  xmlDoc=document.implementation.createDocument("","",null);
	//document.implementation.createDocument (namespaceURI, qualifiedNameStr, DocumentType);
	}
	else
	{
	  alert('Your browser cannot handle this script');
	}
	xmlDoc.async=false;
	xmlDoc.load("../assets/cd_catalog.xml");

	/*
	var txt=document.getElementById("xmlData").value;//<textarea>
	var parser=new DOMParser();//只对Firefox
	var xmlDoc=parser.parseFromString(txt,"text/xml");//返回一个Document 对象
	*/
	
	var allCD=xmlDoc.getElementsByTagName("cd");
	for(var i=0;i<allCD.length;i++)
	{
		alert(allCD[i].firstChild.nodeValue+",attribute price="+allCD[i].getAttribute("price"));
	}
}

//onerror 事件捕获网页中的错误,三个参数来调用：msg（错误消息）、url（发生错误的页面的 url）、line（发生错误的代码行）。)
//如果返回值为 false，则在控制台 (JavaScript console) 中显示错误消息
onerror=handleErr;
function handleErr(msg,url,line)
{
	alert('网页中有错误,行号:'+line+',原因:'+msg);
	return false;
}

</script>
document.getElementsByClassName('red test'); 返回数组
document.getElementById('main').getElementsByClassName('test');
document.getElementsByTagName("a")[0].getAttribute("target");
document.getElementsByTagName("BUTTON")[0].hasAttribute("onclick");
document.body.hasAttributes() //false

document.getELementsByName();//只document有
getElementsByTagName元素有

children 是无素
childNodes 有无素和空格的文本
hasChildNodes('')
childElementCount

firstChild 
firstElementChild 

nextSibling
previousSibling 
nextElementSibling
previousElementSibling

removeChild(x)
appendChild(x)
Element (nodeType = 1):
Text (nodeType = 3):
Document (nodeType = 9):


如代码中要写多行表示一句代码,用\ ,如 
"xx \    
"

浏览器不允许跨域的访问。

get 表单方式,传送的数据量较小，不能大于2KB
	
.cloneNode(true);//true 也是复制子节点

winow.event.srcElement.

表单时文件中的内容一定是点击浏览选择的才行
如form放在table中不行的

window.location='initAdd?id=${id}';
window.location.href='';
window.location.reload();//刷新

=====位置坐标
style.offsetTop=event.srcElement.offsetTop
style.posLeft=event.x-offsetWidth/2

------event.clientX (firefox),(ie__event.x)
------event.target(firefox),(ie__event.srcElement)

<SCRIPT type="text/javascript">
function fclick(obj){
   with(obj){
     style.posTop=event.srcElement.offsetTop
     
     var x=event.x-offsetWidth/2
     if(x<event.srcElement.offsetLeft)
	x=event.srcElement.offsetLeft
     if(x>event.srcElement.offsetLeft+event.srcElement.offsetWidth-offsetWidth)
	x=event.srcElement.offsetLeft+event.srcElement.offsetWidth-offsetWidth

     style.posLeft=x;
   }
}
</script>
<form method="post" action="www.google.cn" enctype="multipart/form-data">
	<input type="text" id="f_file" > <input type="button" onmouseover="fclick(t_file)" value="选择上传文件,不好用,有纯CSS的,只练习JS">
	<input type="file" id="t_file" name="upload" style="position:absolute;filter:alpha(opacity=30);width:30px;"  onchange="f_file.value=this.value">
<br>	<input type="submit" value="提交">
</form>

-----
<SCRIPT type="text/javascript">
/*
	function getRecursiveDistance(element)//递归调用
	{	
		if(element.tagName=="BODY")
		{
			var zero=new Object();
			zero.width=0;
			zero.height=0;
			return zero;
		}
		var res=getRecursiveDistance(element.parentElement);
		res.width+=element.offsetLeft;
		res.height+=element.offsetTop;
		return res;
	}
	window.onload=function()
	{
		var file=document.getElementsByName("file")[0];
		var presume_b=document.getElementById("presume_b");

		var res=getRecursiveDistance(presume_b);

		file.style.left=res.width;
		file.style.top=res.height;
	};
*/
	function fileAlign(evt)
	{
		var file=document.getElementsByName("upload")[0];
		var presume=document.getElementById("presume");
		
		file.style.width=0;
		file.style.height=presume.style.height;

		file.style.left=evt.clientX - presume.clientWidth/2; //OK
		file.style.top=evt.clientY - presume.clientHeight/2;
		//file.style.top=evt.clientY - evt.target.clientHeight;//target 是file
	}
</script>
<form method="post" action="www.google.cn" enctype="multipart/form-data">
	<input type="text" id="presume" size="20"  maxlength="20"> <input type="button" onmouseover="fileAlign(event);" value="choose file...">
	<input type="file"  name="upload" size="1" style="position:absolute;opacity:0.3;filter: alpha(opacity=30);width:1px;"  onchange="presume.value=this.value">
<br><input type="submit" value="submit now">
</form>
 

evt.clientX 相对于窗口客户区域的 x 坐标，其中客户区域不包括窗口自身的控件和滚动条
evt.screenX 相对于用户屏幕的 x 坐标
evt.offsetX 相对于触发事件的对象的 x 坐标。

offsetLeft  Html元素相对于自己的offsetParent元素的位置 
clientLeft 是的组件边的厚度

offsetWidth是多加厚度的宽
clientWidth是没有加厚度的宽,即可使用的,不包含滚动条和边框部分, 值小于 offsetWidth

scrollLeft 是组件左边在滚动条不可见的长度(IE),scrollTop

scrollWidth 元素完整内容的宽度,包含当前没有被显示出来的部分,scrollHeigth
scroll 值为yes,no,auto

select 元素的 selectedIndex 属性，options

 

Array的　join方法把数组里的每一个元素用指定的字符来连接成一个长的字符串

escape 方法


var x={name :"lisi",age:23} //对象
var y=["list",123] //数组

https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array
var arr=new Array();
arr.push(xxx);
arr.indexOf(xxx);
arr.splice(pos, 1); //指定位置删一个

var val=new Function("x","y","var sum ;sum=x*6 ;return sum;")//最后一个参数是计算
alert(val(5,6));

在function 中有一个 arguments 数组可得到所有的函数参数  arguments.length
with(xx)
{
....//相当于xx.
}

document.layers 是NetScape 浏览器
document.all 是IE
navigator.appName


alert(null==undefined);  //返回true

----------正则表达式
replace 方法
RegExp.input			//传入的字符
RegExp.leftContext属性 ($`)	//匹配左边的
RegExp.rightContext 属性 ($')	//匹配右边的
RegExp.lastMatch属性 ($&)	//最后的全匹配
RegExp.lastParen 属性 ($+)	//最后一个()
RegExp.$1			//()中的

"hello wang".replace("wang","li")//用法同java
"hello wang".split(" ")//用法同java

var reg_phone =/^0{0,1}(13[0-9]|15[0-9])[0-9]{8}$/i;
var one_phone="13011112222";
//alert(reg_phone.test(one_phone));//true

var reg_mail =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-z]{2,3})$/;
var one_mail="abc@test.com";
alert(reg_mail.test(one_mail));//true


var regs =/(13[0-9]|15[0-9])[0-9]{8},/ig; //g,用match方法时返回数组
var many="13011112222,13811112222,";
var matched;
matched=many.match(regs);
alert(matched[0]);
alert(matched[1]);

var item;
while ((item = regs.exec(many)) != null)
   alert(item.index + "=" + item[0]);
   
   
   
   
<input type="text"  onKeypress="return checkNum(event)" size="3" maxlength="3"/>
function checkNum(event)
{
	var res= /[\d]/.test(event.key);
	return res|| event.keyCode==8|| event.keyCode==37|| event.keyCode==39|| event.keyCode==46;
	//46 = Delete, 8 backspace , 37 left ,39 rigth
}
----------

window.open('queryProp?id='+parentId ,'','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=750,height=470,left=80,top=40');


JavaScript 的 === 会多一个比较类型

switch(x)
{
case "1":
	....
	break;
default :
	....
}
这里要比较x==="1" 成功才行的,可用强转换 var y=String(x);,Number(x),Boolean(0);//false


var wi=treeTD.width;//10% 把百分数转实际数
var num=wi.substring(0,wi.length-1);
if(num.length==1)
	w='0'+w;
 w=num/100;
screen.width *w;//screen对象




JS 父子页面传递参数
var obj = new Object();
obj.name="johon";
window.showModalDialog("test.htm",obj,"dialogWidth=200px;dialogHeight=100px");
---test.htm
<script>
var obj = window.dialogArguments
alert("您传递的参数为：" + obj.name)
</script>

str =window.showModalDialog("test.htm",,"dialogWidth=200px;dialogHeight=100px");
alert(str);

可以通过window.returnValue向打开对话框的窗口返回信息，当然也可以是对象



 window.print(); //windows下 调出打印窗口,打印当前document,可以用window.open只打印当前窗口
事件
  window.onbeforeprint
  window.onafterprint
CSS
  page-break-before
  page-break-after
  page-break-inside
 
<BODY>
	<SCRIPT>
	alert(document.body.scrollWidth); //<SCRIPT>必须放在<BODY>中才可仿问 document.body,但值scrollWidth不对
	</SCRIPT>
</body>

<body onload="alert(document.body.scrollWidth)">//只有在onload中,scrollWidth才是所有工作区


---DIV 背景变灰,加载图标
<body onload="body=document.body;">
function showLoader()
{
	var div=document.createElement("DIV");
	div.id="largeDiv";
	//div.style.display='block';//default is show
	//div.style.left=0;// same effect as posLeft
	//div.style.top=0;
	div.style.posLeft=0;
	div.style.posTop=0;
	div.style.position='absolute';
	div.style.backgroundColor='grey';
	div.style.filter='alpha(opacity=30)';
	div.style.zIndex=200;//DIV宽高也会影响滚动条;   HTML CSS中 { z-index : vOrder } 

	var scrollH=body.scrollHeight;
	var scrollW=body.scrollWidth;

	var clientH=body.clientHeight;
	var clientW=body.clientWidth;
	

	div.style.width=(scrollW<clientW)?clientW:scrollW;
	div.style.height=(scrollH<clientH)?clientH:scrollH;
	body.appendChild(div);

	var	imgDiv=document.createElement("DIV");
	imgDiv.id="smallDiv";
	imgDiv.style.position='absolute';
	imgDiv.style.zIndex=201;
	imgDiv.style.posLeft=clientW/2;
	imgDiv.style.posTop=clientH/2;
	
	var img=document.createElement("IMG");
	img.id="img";
	img.src="load_01.gif";
	imgDiv.appendChild(img);
	body.appendChild(imgDiv);

	window.setTimeout(hideLoader, 3000);
	//clearTimeout(handler);
}
	 
function hideLoader()
{
	document.getElementById("img").style.display='none';
	document.getElementById("smallDiv").style.display='none';
	document.getElementById("largeDiv").style.display='none';
}
	
---方案2


#loading {
    width:100%;
    position:absolute;
    z-index:100;
    background-color:#F7F9FC;
    line-height:25px;
    text-align:center;
    font-size:11pt;
    display:none;
    opacity:0.5;
}

<div id="loading">
    <div id="loadingContent">  <img src="loading.gif" alt=""/>正在加载数据,请稍候... </div>
</div>

$(function(){

    $("#loading").css("height",$(document).height());
    $("#loadingContent").css("padding-top",$(document).height()/2);
 
    $.ajaxSetup({
        //timeout: 3000,
        beforeSend: function (xhr)
        {
			$("#loading").show();
        },
        complete: function (xhr, status)
        {
            $("#loading").hide();
        }
    });

});




https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Map
//-------Map
var myMap = new Map();
 
var keyObj = {},
    keyFunc = function () {},
    keyString = "a string";
 
// 添加键
myMap.set(keyString, "和键'a string'关联的值");
myMap.set(keyObj, "和键keyObj关联的值");
myMap.set(keyFunc, "和键keyFunc关联的值");
 
myMap.size; // 3
 
// 读取值
myMap.get(keyString);    // "和键'a string'关联的值"
myMap.get(keyObj);       // "和键keyObj关联的值"
myMap.get(keyFunc);      // "和键keyFunc关联的值"
 
myMap.get("a string");   // "和键'a string'关联的值"
                         // 因为keyString === 'a string'
myMap.get({});           // undefined, 因为keyObj !== {}
myMap.get(function() {}) // undefined, 因为keyFunc !== function () {}


myMap.set(NaN, "not a number");

myMap.get(NaN); // "not a number"
for (var [key, value] of myMap) {
  console.log(key + " = " + value);
}
for (var key of myMap.keys()) {
  console.log(key);
}
for (var value of myMap.values()) {
  console.log(value);
}
for (var [key, value] of myMap.entries()) {
  console.log(key + " = " + value);
}

myMap.forEach(function(value, key) {
  console.log(key + " = " + value);
}, myMap)

var kvArray = [["key1", "value1"], ["key2", "value2"]];
var myMap = new Map(kvArray);
//console.log(uneval([...myMap])); //uneval只对firefox有效，结果为 [["key1", "value1"], ["key2", "value2"]]
//console.log(uneval([...myMap.keys()])); 

keys() 返回一个新的 Iterator 对象,有next()

 	
//===========WeakMap
//key 只能是 Object 类型。 原始数据类型 是不能作为 key 的
//WeakMap中,每个键对自己所引用对象的引用是 "弱引用"
	var wm1 = new WeakMap(),
    wm2 = new WeakMap(),
    wm3 = new WeakMap();
var o1 = {},
    o2 = function(){},
    o3 = window;

wm1.set(o1, 37);
wm1.set(o2, "azerty");
wm2.set(o1, o2); // value可以是任意值,包括一个对象
wm2.set(o3, undefined);
wm2.set(wm1, wm2); // 键和值可以是任意对象,甚至另外一个WeakMap对象
wm1.get(o2); // "azerty"
wm2.get(o2); // undefined,wm2中没有o2这个键
wm2.get(o3); // undefined,值就是undefined

wm1.has(o2); // true
wm2.has(o2); // false
wm2.has(o3); // true (即使值是undefined)

wm3.set(o1, 37);
wm3.get(o1); // 37

wm3.get(o1); // undefined,wm3已被清空
wm1.has(o1);   // true
wm1.delete(o1);
wm1.has(o1);   // false

//=========Set
const set1 = new Set([1, 2, 3,3, 4, 5]);

console.log(set1.has(1));//true
console.log(set1.has(6));//false

let mySet = new Set();

mySet.add(1); // Set(1) {1}
mySet.add(5); // Set(2) {1, 5}
mySet.add(5); // Set { 1, 5 }
mySet.add("some text"); // Set(3) {1, 5, "some text"}
var o = {a: 1, b: 2};
mySet.add(o);

mySet.add({a: 1, b: 2}); // o 指向的是不同的对象，所以没问题

mySet.has(1); // true
mySet.has(3); // false
mySet.has(5);              // true
mySet.has(Math.sqrt(25));  // true
mySet.has("Some Text".toLowerCase()); // true
mySet.has(o); // true

mySet.size; // 5

mySet.delete(5);  // true,  从set中移除5
mySet.has(5);     // false, 5已经被移除

mySet.size; // 4, 刚刚移除一个值
console.log(mySet); // Set {1, "some text", Object {a: 1, b: 2}, Object {a: 1, b: 2}}

//迭代整个set
//按顺序输出：1, "some text" 
for (let item of mySet) console.log(item);

//按顺序输出：1, "some text" 
for (let item of mySet.keys()) console.log(item);

//按顺序输出：1, "some text" 
for (let item of mySet.values()) console.log(item);

//按顺序输出：1, "some text" 
//(键与值相等)
for (let [key, value] of mySet.entries()) console.log(key);

 

//转换Set为Array  
var myArr = Array.from(mySet); // [1, "some text"]

//如果在HTML文档中工作，也可以：
mySet.add(document.body);
mySet.has(document.querySelector("body")); // true

//Set 和 Array互换
mySet2 = new Set([1,2,3,4]);
mySet2.size; // 4
[...mySet2]; // [1,2,3,4] 用...(展开操作符)操作符将Set转换为Array

 
set2 = new Set([ 2,4,6]);

//intersect can be simulated via 
var intersection = new Set([...set1].filter(x => set2.has(x)));

//difference can be simulated via
var difference = new Set([...set1].filter(x => !set2.has(x)));

//用forEach迭代
mySet.forEach(function(value) {
	console.log(value);
});
 
for (var elem of mySet) {
	console.log(elem);
}

//===========WeakSet

//只能存放对象引用, 不能存放值,而 Set 对象都可以.
//如果没有其他的变量或属性引用这个对象值, 则这个对象值会被当成垃圾回收掉. 正因为这样, WeakSet 对象是无法被枚举的, 没有办法拿到它包含的所有元素
var ws = new WeakSet();
var obj = {};
var foo = {};

ws.add(window);
ws.add(obj);

ws.has(window); // true
ws.has(foo);    // false, 对象 foo 并没有被添加进 ws 中 

ws.delete(window); // 从集合中删除 window 对象
ws.has(window);    // false, window 对象已经被删除了
 
 
//========Array

function myFunction(value, index) {
  demoP.innerHTML = demoP.innerHTML + "index[" + index + "]: " + value + "<br>"; 
}
var arr=["one","two"];
(function (array){
	 array.forEach( myFunction);//Array 的 forEach
})(arr); //像匿名内部函数 

=========HTML5 JS
 
window.localStorage.setItem('value', area.value);
window.localStorage.getItem('value');
--
var db = window.openDatabase("DBName", "1.0", "description", 5*1024*1024); //5MB
db.transaction(function(tx) {
  tx.executeSql("SELECT * FROM test", [], successCallback, errorCallback);
});

--
var idbRequest = window.indexedDB.open('Database Name');
idbRequest.onsuccess = function(event) {
  var db = event.srcElement.result;
  var transaction = db.transaction([], IDBTransaction.READ_ONLY);
  var curRequest = transaction.objectStore('ObjectStore Name').openCursor();
  curRequest.onsuccess = ...;
};

--
// <html manifest="cache.appcache">
window.applicationCache.addEventListener('updateready', function(e) {
  if (window.applicationCache.status == window.applicationCache.UPDATEREADY) {
	window.applicationCache.swapCache();
	if (confirm('A new version of this site is available. Load it?')) {
	  window.location.reload();
	}
  }
}, false);


//myDiv.style.cssText;显示所有有值的CSS
 

if ("WebSocket" in window)
  {
	 alert("WebSocket is supported by your Browser!");
	  var ws = new WebSocket("ws://localhost:8080/J_AjaxServer/webSocket");//tomcat支持
	 ws.onopen = function()
	 {
		ws.send("Message to send");
		alert("Message is sent...");
	 };
	 ws.onmessage = function (evt) 
	 { 
		var received_msg = evt.data;
		alert("Message is received...");
	 };
	 ws.onclose = function()
	 { 
		alert("Connection is closed..."); 
	 };
  }
 

淘宝触屏版 touchstart, touchend, touchmove
多点触击涉及到三个事件：gesturestart、gesturechange、gestureend

document.addEventListener("touchmove" 





Time:  <div id="foo"></div> 
<button onclick="start()">Start Server Send</button>
function start() {
	//服务器端把 "Content-Type" 报头设置为 "text/event-stream"。
	var eventSource = new EventSource("serverSend");
	eventSource.onmessage = function(event) {
		//服务端返回字串，格式data:xx
		document.getElementById('foo').innerHTML += event.data+" <br/>";
	};
	eventSource.onopen=function(event)
	{
		document.getElementById("foo").innerHTML+=  "onopen <br/>";
	};
	eventSource.onerror=function(event)
	{
		document.getElementById("foo").innerHTML+=  "onerror"+event+" <br/>";
	};
}
</script>

@WebServlet(urlPatterns="/serverSend")
public class ServerSendServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//content type must be set to text/event-stream
		response.setContentType("text/event-stream");	
		//encoding must be set to UTF-8
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write("data: "+ System.currentTimeMillis() +"\n\n");
		writer.close();
	}
}



up:  <div id="up"></div> 
down:  <div id="down"></div> 
<button onclick="startMulti()">Start Multi Server Send</button>
function startMulti()
{
	var eventSource = new EventSource("serverSendMulti");
	eventSource.addEventListener('up_vote',//对应服务端的 event:up_vote\n
		function(event) {
			document.getElementById('up').innerHTML = event.data;
		}, false);
	eventSource.addEventListener('down_vote', 
		function(event) {
			document.getElementById('down').innerHTML = event.data;
		}, false);
}
@WebServlet(urlPatterns="/serverSendMulti")
public class ServerSendMultiServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		int upVote = 0;
		int downVote = 0;
		upVote = upVote + (int) (Math.random() * 10);
		downVote = downVote + (int) (Math.random() * 10);
		writer.write("event:up_vote\n");
		writer.write("data: " + upVote + "\n\n");
		writer.write("event:down_vote\n");
		writer.write("data: " + downVote + "\n\n");
		writer.flush();
		writer.close();
	}
} 




======ECMA Script 2016  2017 






