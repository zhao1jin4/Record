
JS 格式化工具eclipse可做 
JS 代码变量压缩混肴, java -jar yuicompressor-2.4.8.jar my.js -o my-min.js --charset utf-8
JS解压 eclispe有时不行的,用iBox在线工具http://tool.lu/js/    但变量名是a,b没办法了


-------Antechinus 工具
	ctrl + /  生成可折叠的块/
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
			///Firefox textContent;
			var first=objXml.childNodes[0].firstChild.textContent;//firefox 是的结果是\n (如是格式化的XML)  
			status=objXml.getElementsByTagName("status")[0].textContent;//firefox 是的结果是OK
			if(status=="OK")
			{
				var des=objXml.getElementsByTagName("description")[0].textContent;
				var decode=decodeURI(des);
				document.getElementById("result").innerHTML=decode;
			}
			//IE text,Edge没有text;
			//first=objXml.childNodes[0].firstChild.text;//IE的结果是OK
			//status=objXml.getElementsByTagName("status")[0].text;//IE的结果是OK
 
			var xmlDoc;
			if (window.ActiveXObject)
			  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			else if (document.implementation.createDocument)
			  xmlDoc=document.implementation.createDocument("","",null);//Edge ,Firefox,Chrome
			else
			  alert('Your browser cannot handle this script');
			xmlDoc.async=false;
			//xmlDoc.load(objXml);//过时的，Edge 报错 , Chrome 报错 , x
			 
						
	};	
 
	/* 
	xmlHttp.open("GET", "xmlServlet?username="+encodeURI(encodeURI('李')), true);//是否异步,true 异步,false同步
	xmlHttp.send(null);//GET 用null  发送
	*/
	
	xmlHttp.open("POST", "xmlServlet", true);//是否异步,true 异步,false同步
	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");//open方法后 调用
	xmlHttp.send("username="+encodeURI(encodeURI('李')));	
	//js两次encodeURI，java一次URLDecoder.decode  (不做req.setCharacterEncoding("UTF-8"))
	//js一次encodeURI， java不用 URLDecoder.decode 做req.setCharacterEncoding("UTF-8") 
	
}
//如果返回的XML文件中属性，JS使用 getAttribute("id")



<ul id="ul">
</ul>
<script type="text/javascript">
	var element  = document.getElementById('ul');  
	var fragment = document.createDocumentFragment();//创建文档fragment
	var browsers = ['Firefox', 'Chrome', 'Opera', 
	    'Safari', 'Internet Explorer'];
	
	browsers.forEach(function(browser) {
	    var li = document.createElement('li');
	    li.textContent = browser;
	    fragment.appendChild(li);
	});
	
	element.appendChild(fragment);
	
	//----DOMParser (String->dom)
	var strXml="<root><status>OK</status><description>%E4%B8%AD%E6%96%87</description></root>";
	let parser = new DOMParser(),
	myDoc = parser.parseFromString(strXml, "application/xml");
	des=myDoc.getElementsByTagName("description")[0].textContent;
	var decode=decodeURI(des);
	console.log(decode);
	//--XMLSerializer  (dom->String)
	var s = new XMLSerializer(); 
	 var str = s.serializeToString(myDoc);
	 console.log(str);
	 //---
	//doc = document.implementation.createDocument(namespaceURI, qualifiedNameStr, documentType); 返回 XMLDocument
	var doc = document.implementation.createDocument ('http://www.w3.org/1999/xhtml', 'html', null);
	var body = document.createElementNS('http://www.w3.org/1999/xhtml', 'body');
	body.setAttribute('id', 'abc');
	doc.documentElement.appendChild(body);
	alert(doc.getElementById('abc')); // [object HTMLBodyElement]
	
</script>
//-------java   Dom
response.setContentType("text/xml;charset=UTF-8"); 
response.setCharacterEncoding("UTF-8");

req.setCharacterEncoding("UTF-8");
String username=req.getParameter("username");
//js两次encodeURI，java一次URLDecoder.decode  (不做req.setCharacterEncoding("UTF-8"))
//js一次encodeURI， java不用 URLDecoder.decode 做req.setCharacterEncoding("UTF-8") 
 
System.out.println("username:"+ username);
System.out.println("username decode:"+URLDecoder.decode(username,"UTF-8"));

TransformerFactory tFactory = TransformerFactory.newInstance();
System.getProperty("javax.xml.transform.TransformerFactory");// org.apache.xalan.processor.TransformerFactoryImpl

Transformer transformer = tFactory.newTransformer();// new StreamSource("")可传文件,可把Stream->Source
transformer.setOutputProperty("encoding", "UTF-8"); // OutputKeys.ENCODING
transformer.setOutputProperty(OutputKeys.INDENT, "yes");// 只换行不缩进
transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");// 不要XML声明
transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");// 对于使用使用Xalan-J,要和indent=yes一起使用

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();// javax
DocumentBuilder db = factory.newDocumentBuilder();
Document doc = db.newDocument();// 建立
Element root = doc.createElement("root");
//Attr attr = doc.createAttribute("name");
//attr.setValue("lisi");
//root.setAttributeNode(attr);

Element status = doc.createElement("status");
status.setTextContent("OK");
root.appendChild(status); 

Element description = doc.createElement("description");
description.setTextContent("中文");
root.appendChild(description); 

doc.appendChild(root);

DOMSource source = new DOMSource(doc);// 如写Element 会丢失namespace,dom4j会保留namespace
StreamResult stream = new StreamResult(response.getOutputStream());
transformer.transform(source, stream);
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

--- XMLHttpRequest  upload
https://developer.mozilla.org/zh-CN/docs/Web/API/FormData/Using_FormData_Objects
<form enctype="multipart/form-data" method="post" name="fileinfo">  
	选择文件:  <input type="file" id="ipt-file" name="attache1"/>
	<button type="button" id="btn-upload">上传</button>
</form>
	
//var formData = new FormData();//自带类
//formData.append("attache1", input.files[0]);
var form = document.forms.namedItem("fileinfo");//所有的form元素name="fileinfo"的
var formData = new FormData(form);//也可构造函数输入form元素 


var xhr = new XMLHttpRequest();
console.log(xhr.upload); //是XMLHttpRequestUpload
 /*
xhr.onreadystatechange = function() {
	if (xhr.readyState === 4 && xhr.status === 200) {
		console.log(xhr.responseText);  
		info.innerHTML = xhr.responseText;
	}
};
*/
xhr.onload = function(oEvent) {   //XMLHttpRequest  onload 要在send前调用  相当于onreadystatechange
   if (xhr.status == 200) {
	   info.innerHTML = "Uploaded!";
   } else {
	   info.innerHTML = "Error " + oReq.status + " occurred when trying to upload your file.<br \/>";
   }
 };
 
//事件还有loadstart，abort，error，load（结果成功），timeout，loadend（结果成功或失败）
//会一次性读到浏览器缓存中！！！！
xhr.upload.addEventListener("progress", function(event) {
	if(event.lengthComputable){
		progressDIV.style.width = Math.ceil(event.loaded * 100 / event.total) + "%";
	}
}, false);

xhr.open("POST", "./uploadServlet3"); 
 
xhr.send(formData);

=========== CORS
------ 简单请求
XMLHttpRequest 不加请求头成功
<div id="out"></div>
 
var xhr = new XMLHttpRequest();  
xhr.onreadystatechange = function() {
	if (xhr.readyState === 4 ) 
	{
		if( xhr.status === 200)
			document.querySelector("#out").innerText=xhr.responseText;
		else
		{
			alert("error status code="+xhr.status+",statusText="+xhr.statusText);
		}
	}
};
var remoteUrl="http://127.0.0.1:8080/S_HTML5CSS3/cors";//这里假设127.0.0.1是远程,浏览器用localhost打开
xhr.open("POST",remoteUrl , false);//是否异步,true 异步,false同步

//以下几个头设置，firefox/chrome被禁止, 浏览器自动设置 
//xhr.setRequestHeader("Access-Control-Request-Method","POST"); 
//xhr.setRequestHeader("Access-Control-Request-Headers","content-type,mycors");
//xhr.setRequestHeader("Origin","http://xxx:8080/");
//xhr.setRequestHeader("Referer","http://xxx:8080/S_HTML5CSS3/pureAjax/crossDomain1.html"); 

//如xhr不设置任何Header是可以请求到服务端的
//服务返回加头response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//服务端 response.setContentType("application/json;charset=UTF-8");没有影响
xhr.send(JSON.stringify({"name":"李四"}));

--服务端
//response.addHeader("Access-Control-Allow-Origin", "*");//“*”号表示允许任何域向我们的服务端提交请求：
//这里假127.0.0.1是远程,浏览器用localhost打开
response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");

//Cookie可以包含在请求中，一起发给服务器,默认情况下，Cookie不包括在CORS请求之中
//必须在AJAX请求中打开withCredentials属性。var xhr = new XMLHttpRequest(); xhr.withCredentials = true;
//如果要发送Cookie，Access-Control-Allow-Origin就不能设为星号
//response.addHeader("Access-Control-Allow-Credentials", "true");


//XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段：Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma
//如果想拿到其他字段，就必须在Access-Control-Expose-Headers里面指定
//response.addHeader("Access-Control-Expose-Headers", "FooBar");



------非简单请求

如 XMLHttpRequest  加请求头 Content-Type : application/json
xhr.setRequestHeader("Content-Type","application/json;charset=UTF-8");//open方法后 调用   

//---对 非简单请求的CORS请求，会在正式通信之前，增加一次HTTP查询请求，称为"预检"请求（preflight）。
//"预检"请求用的请求方法是OPTIONS,服务端取Origin，Access-Control-Request-Method，Access-Control-Request-Headers做验证成功加http头

--服务端代码
@Override
protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String origin=req.getHeader("Origin");//http://localhost:8080
	String method=req.getHeader("Access-Control-Request-Method");//POST
	String headers=req.getHeader("Access-Control-Request-Headers");//content-type
	if(origin.contains("localhost") && "POST".equals(method) && headers.contains("content-type") )
	{
		resp.addHeader("Access-Control-Allow-Methods", "POST");
		resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
	}
}





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
	this.method=People;
	this.method(username);//调用父类方法(通过里面的this完成的)
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

//----Object.create继承
const person = {
  isHuman: false,
  printIntroduction: function () {
    console.log(`My name is ${this.name}. Am I human? ${this.isHuman}`);
  }
};
const me = Object.create(person);
me.name = "Matthew"; // "name" is a property set on "me", but not on "person"
me.isHuman = true; // inherited properties can be overwritten
me.printIntroduction();
// expected output: "My name is Matthew. Am I human? true"

//----
// Shape - 父类(superclass)
function Shape() {
  this.x = 0;
  this.y = 0;
}
// 父类的方法
Shape.prototype.move = function(x, y) {
  this.x += x;
  this.y += y;
  console.info('Shape moved.');
};
// Rectangle - 子类(subclass)
function Rectangle() {
  Shape.call(this); // call super constructor.
}

// 子类续承父类  （这两句 rect instanceof Shape 是对的值，rect.move可调用后面才被prototype的）
Rectangle.prototype = Object.create(Shape.prototype);
Rectangle.prototype.constructor = Rectangle;//constructor构造器

var rect = new Rectangle();
console.log('Is rect an instance of Rectangle?',  rect instanceof Rectangle); // true
console.log('Is rect an instance of Shape?',  rect instanceof Shape); // true
rect.move(1, 1); // Outputs, 'Shape moved.'

//----继承到多个对象
function SuperClass()
{
	this.s="Super";
}
function OtherSuperClass()
{
	this.o="Other";
}
function MyClass() {
     SuperClass.call(this);
     OtherSuperClass.call(this);
} 
// 继承一个类
MyClass.prototype = Object.create(SuperClass.prototype);
// 混合其它
Object.assign(MyClass.prototype, OtherSuperClass.prototype);//把OtherSuperClass原型上的函数拷贝到 MyClass原型上，使 MyClass 
// 重新指定constructor
MyClass.prototype.constructor = MyClass;

MyClass.prototype.myMethod = function() {
    console.log("myMethod");
};
var my =new MyClass();
console.log(my.s);
console.log(my.o);
 my.myMethod();

Object.assign 是在 ES2015 

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
  

  


----bind
   
this.a = 1;
var module = {
    a : 2,
    getA:function() {
    return this.a;    
    }
};
console.log("in method="+module.getA());//2

var getA1 = module.getA;
// getA在外部调用，此时的this指向了全局对象
console.log("in global="+getA1());//1

// 再把getA1方法绑定到module环境上
var getA2 = getA1.bind(module);//bind有返回值而且是一个函数，而clall无返回值
console.log("after bind="+getA2()); //2 


 
   
  
  
  
function initTable()
{
	//var myTable=document.getElementById("myTable");
	var myTable=document.querySelector("#myTable");
	for(var i=0;i<10;i++)
	{
		var row={myid:i,name:"name"+i};
		var tr=document.createElement("tr");
		
		var htmlName=nameFormater(row.name,row,i); 
		tr.innerHTML="<td>"+i+"</td><td>"+htmlName+"</td>"
		myTable.appendChild(tr);
	}
}
//下面是easyUI代码
var saveFunc=[];
function nameFormater(value,row,index)
{
	var key='btnClick'+row.myid;
	saveFunc[key]=rowClick.bind(this,row);//row也会存到数组中,第一个参数this必传
	var  strFunc="saveFunc['"+key+"'](this)";//这里this可以省略
	return '<button onclick="'+strFunc+'">'+value+'</button>';
	//return '<button onclick="rowClick(row)">'+value+'</button>';//这样单击时得到不row的值，要存起来
}
function rowClick(row)
{
	console.log(row);
}
</script>
<body  onload="initTable();"> 
	<table id="myTable"> 
	</table>
</body> 
 

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

//addEventListener 第3个参数 useCapture 默认值为false， 如true使用Capture方式，如false是Bubbling
第3个参数也可以是一个对象选项
	capture:  Boolean
	once:  Boolean 在添加之后最多只调用一次
	passive: Boolean 设置为true时，表示 listener 永远不会调用 preventDefault()

//js阻止事件冒泡二选一 ( 捕获阶段: 外-》里 , 冒泡阶段: 里-》外)
//evt.cancelBubble = true;
//evt.stopPropagation();

 //js阻止 (链接跳转/表单中button提交) 默认行为，没有停止冒泡
 evt.preventDefault()
 


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
	var txt=document.getElementById("xmlData").value;//<textarea>
	var parser=new DOMParser();//Firefox/chrome可以
	var xmlDoc=parser.parseFromString(txt,"text/xml");//返回一个Document 对象
	 
	
	var allCD=xmlDoc.getElementsByTagName("cd");
	for(var i=0;i<allCD.length;i++)
	{
		alert(allCD[i].firstChild.nodeValue+",attribute price="+allCD[i].getAttribute("price"));
	}
	var s = new XMLSerializer(); 
	var str = s.serializeToString(xmlDoc);
	console.log(str);
}

//--JSON stringify

JSON.stringify([NaN, null, Infinity]); // '[null,null,null]'
JSON.stringify(new Date(2006, 0, 2, 15, 4, 5)) 
//'"2006-01-02T15:04:05.000Z"'

JSON.stringify({ x: [10, undefined, function(){}, Symbol('')] }); 
//'{"x":[10,null,null,null]}' 

JSON.stringify([new Set([1]), new Map([[1, 2]]), new WeakSet([{a: 1}]), new WeakMap([[{a: 1}, 2]])]);
//'[{},{},{},{}]'

//数组下标做key
JSON.stringify([new Int8Array([1]), new Int16Array([1]), new Int32Array([1])]);
//'[{"0":1},{"0":1},{"0":1}]'
JSON.stringify([new Uint8Array([1]), new Uint8ClampedArray([1]), new Uint16Array([1]), new Uint32Array([1])]);
//'[{"0":1},{"0":1},{"0":1},{"0":1}]'
JSON.stringify([new Float32Array([1]), new Float64Array([1])]);
//'[{"0":1},{"0":1}]'

new Uint8ClampedArray([-1]) //如果你指定一个在 [0,255] 区间外的值，它将被替换为0或255,最接近它的整数
new Uint8Array([-1]) //变为[255]

//toJSON()
JSON.stringify({ x: 5, y: 6, toJSON(){ return this.x + this.y; } });
//'11'

var obj = {
  data: 'data',
  toJSON (key) {
      if (key)
          return `Now I am a nested object under key '${key}'`;
      else
          return this;
  }
};
JSON.stringify(obj);
//'{"data":"data"}'
JSON.stringify({ obj })
//'{"obj":"Now I am a nested object under key 'obj'"}'
JSON.stringify([ obj ])
//'["Now I am a nested object under key '0'"]'


//Non-enumerable properties:
JSON.stringify( Object.create(null, { x: { value: 'x', enumerable: false }, y: { value: 'y', enumerable: true } }) );
//'{"y":"y"}'

//JSON.stringify({x: 2n});
//TypeError: BigInt value can't be serialized in JSON
//整数字面量后面加 n 的方式定义一个 BigInt

const hugeHex = BigInt("0x1fffffffffffff");
//9007199254740991n
typeof hugeHex === 'bigint'; 


function replacer(key, value) {
// Filtering out properties
if (typeof value === 'string') {
  return undefined;
}
return value;
}
var foo = {foundation: 'Mozilla', model: 'box', week: 45, transport: 'car', month: 7};
JSON.stringify(foo, replacer);//第二个参数
//'{"week":45,"month":7}'

JSON.stringify({ uno: 1, dos: 2 }, null, '\t');//第三个参数缩进使用tab键.可修改为两个空格
//-----




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
getElementsByTagName 元素有
getElementsByClassName()

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

 

Array 的 join 方法把数组里的每一个元素用指定的字符来连接成一个长的字符串
 
var  nodes = new Array();
nodes[0]=0;
nodes[1]=1; 
var  nodesStr = nodes.join(",");   
alert(nodesStr); // 0,1

escape("\"") 方法 是转换为%xx 的形式


var x={name :"lisi",age:23} //对象
var y=["list",123] //数组

https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array
var arr=new Array();
arr.push(xxx);
arr.indexOf(xxx);
arr.splice(pos, 1); //指定位置删一个

var val=new Function("x","y","var sum ;sum=x*6 ;return sum;")//最后一个参数是计算
alert(val(5,6));

在 function 中有一个 arguments 数组可得到所有的函数参数  arguments.length
with(xx)
{
	a //相当于xx.a
}

document.layers 是NetScape 浏览器
document.all 是IE
navigator.appName


alert(null==undefined);  //返回true

----------正则表达式
replace 方法
RegExp.input			//传入的字符
RegExp.leftContext属性 ($`)	//匹配左边的`
RegExp.rightContext 属性 ($')	//匹配右边的'
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
	 console.log("key="+event.key);//event.key是真实的数字，字母
  	 console.log("kekeyCodey="+event.keyCode);//使用keyCode已经过时了,用code
  	 console.log("code="+event.code);//按1显示Digit1,按a显示KeyA
        	 
	var res= /[\d]/.test(event.key); 
	return res|| event.keyCode==8|| event.keyCode==37|| event.keyCode==39|| event.keyCode==46;
	//46 = Delete, 8 backspace , 37 left ,39 rigth
}
----------

window.open('queryProp?id='+parentId ,'','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=750,height=470,left=80,top=40');
子窗口中用 window.opener仿问父窗口 
window.opener.location.reload()
window.opener.parentFunction(window.opener.gId ) //调用父窗口函数，使用父窗口变量


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

	var handler=window.setTimeout(hideLoader, 3000);//也可以用 window.setInterval 
	//clearTimeout(handler); //window.clearInterval 
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


---cookie
//只可访问本域的cookie
//增加
document.cookie = "name=oeschger";
document.cookie = "favorite_food=tripe";

var exdate=new Date();
exdate.setDate(exdate.getDate()+2);
document.cookie="loginId=lisi;expires="+exdate.toGMTString()  ;

//清除
var exdate=new Date();
exdate.setDate(exdate.getDate()-2);
var ca = document.cookie.split(';');
for(var i=0; i<ca.length; i++) 
{
	var key=ca[i].split('=')[0];
	document.cookie= key+"=; expires="+exdate ;//删Cookie 日期设置当前时间以前
}

//---------像jQuery的选择器
var div=document.querySelector("#myIdDiv");//也要在执行前存在元素（即 不在方法中就要在HTML后）
console.log(div.innerHTML);

// <p> 元素的第一个
 document.querySelector("p").style.backgroundColor = "red";

// class="myClassDiv" 元素的第一个
document.querySelector(".myClassDiv").style.backgroundColor = "yellow";

 // <p class="example" > 的第一个元素 
  document.querySelector("p.example").style.backgroundColor = "blue";
 
 //<a> 元素中有 "target" 属性的第一个 
  document.querySelector("a[target]").style.border = "10px solid red";
  
 var photos = document.querySelector("input[type='file'][multiple]");
 
 //h2,h3 中文档中先找到哪一个
  document.querySelector("h2,h3").style.backgroundColor = "red";

 //<div class="user-panel main"> 下的 <input name="login"/>
  var el = document.querySelector("div.user-panel.main input[name='login']");
  el.value="找到的值";


   
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


//- shift 
//shift 第一个元素返回并从数组移除 
let myFish = ['angel', 'clown', 'mandarin', 'surgeon']; 
console.log('调用 shift 之前: ' + myFish);
//"调用 shift 之前: angel,clown,mandarin,surgeon" 
var shifted = myFish.shift();  
console.log('调用 shift 之后: ' + myFish); 
//"调用 shift 之后: clown,mandarin,surgeon"  
console.log('被删除的元素: ' + shifted); 
//"被删除的元素: angel"

//- unshift 
var arr1 = [0, 1, 2];
var arr2 = [3, 4, 5];
//将 arr2 中的元素插入到 arr1 的开头
Array.prototype.unshift.apply(arr1, arr2) // arr1 现在是 [3, 4, 5, 0, 1, 2]


//slice
var animals = ['ant', 'bison', 'camel', 'duck', 'elephant'];
console.log(animals.slice(2));
// expected output: Array ["camel", "duck", "elephant"]

console.log(animals.slice(2, 4));
//expected output: Array ["camel", "duck"]

const array1 = [1, 2, 3, 4];
const reducer = (accumulator, currentValue) => accumulator + currentValue;
// 1 + 2 + 3 + 4
console.log(array1.reduce(reducer)); //  10

// 5 + 1 + 2 + 3 + 4
console.log(array1.reduce(reducer, 5)); //  15


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


var person=prompt("请输入你的名字","李四");

	    

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
 
=====woker
var myWorker = new Worker("js/worker.js");
myWorker.postMessage([first.value,second.value]);
myWorker.onmessage = function(e) {
		console.log("received from worker:" +e.data);
		console.log('Main (myWorker.onmessage): Message received from worker');
	}
	
myWorker.terminate();
--worker.js
onmessage = function(e) {
	console.log('Worker: Message received:'+e.data);
	var workerResult = 'Result: ' + (e.data[0] * e.data[1]); 
	postMessage(workerResult);
}
----hash , history

<a id="myAnchor" href="/en-US/docs/HTMLHyperlinkElementUtils.href#Examples">Examples</a>
<script>
	var anchor = document.getElementById("myAnchor");
	console.log(anchor.hash); // Returns '#Examples'
</script>


let stateObj = {
	foo: "bar",
};
history.pushState(stateObj, "page 2", "bar.html");//最后一个参数是显示在浏览器地址栏上的，中间参数title,firefox忽略
let currentState = history.state;
console.log(currentState);
history.replaceState({foo1:"bar1"}, "page 3", "bar2.html");
console.log(history.state);

----FileReader readAsDataURL
<input id="browse" type="file" onchange="previewFiles()" multiple>
<div id="preview"></div>
	   
function previewFiles() 
{
	  var preview = document.querySelector('#preview');
	  var files   = document.querySelector('#browse').files;

	  function readAndPreview(file)
	  {
			// Make sure `file.name` matches our extensions criteria
			if ( /\.(jpe?g|png|gif)$/i.test(file.name) ) 
			{
				  var reader = new FileReader();
				/*
				reader.addEventListener("load", function () 
				{
				   var image = new Image();//动态建img
				   image.height = 100;
				   image.title = file.name;
				   image.src = this.result; //内容是  data:image/jpeg;base64,后面是Base64编码
				   preview.appendChild( image );
				 }, false);
				reader.readAsDataURL(file);
				*/
			   //方式二OK
			   reader.readAsArrayBuffer(file);
			   reader.onload=function(e){ 
					var bf = this.result;
					var blob = new Blob([bf],{type:"text/plain"});//Blob对象 
					var str = URL.createObjectURL(blob);
					
					 var image = new Image();//动态建img
					image.height = 100;
					image.title = file.name;
					image.src = str;
					preview.appendChild( image );
				}; 
			}
	  }

	  if (files)
	  {
		[].forEach.call(files, readAndPreview);
	  }

 } 
</script>
---- FileReader 大文件分块读 

file.size; //file即<input type="file"> 文件大小
blob = file.slice(start, start + me.step + 1); //分片
reader.abort();//停止读

=====URL.createObjectURL
<input type="file" id="take-picture" accept="image/*">
<img src="about:blank" alt="" id="show-picture">

var takePicture = document.querySelector("#take-picture"),
	showPicture = document.querySelector("#show-picture"); 

takePicture.onchange = function (event) { 
var files = event.target.files;
var  file = files[0];
var imgURL = window.URL.createObjectURL(file);
// 格式(blob)      blob:http://localhost:8080/5b543137-906b-40d5-8ba9-faf0739822c7 
showPicture.src = imgURL;  //firefox OK,Chrome不行？？？

// Revoke ObjectURL
URL.revokeObjectURL(imgURL);

---在线视频聊天
<video id="video" autoplay></video>
//var getUserMedia =  navigator.getUserMedia ;//Chrome OK,Edge OK,Firefox NO
//var getUserMedia =  navigator.mozGetUserMedia ;//Firefox OK
var getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);

    getUserMedia.call(navigator, 
   		{
	        video: true,
	        audio: true
	    }, 
		function(localMediaStream)
		{
	        var video = document.getElementById('video');
	        video.src = window.URL.createObjectURL(localMediaStream);//Chrome OK,Edge OK 但播放声音有很大的噪音,Firefox NO
	        
	        video.onloadedmetadata = function(e) 
	        {
	            console.log("Label: " + localMediaStream.label);//undefined
	            console.log("AudioTracks" , localMediaStream.getAudioTracks());
	            console.log("AudioTracks label" , localMediaStream.getAudioTracks()[0].label);
	            console.log("VideoTracks" , localMediaStream.getVideoTracks());
	            console.log("VideoTracks label" , localMediaStream.getVideoTracks()[0].label);
	        };
	    }, 
	    function(e) {
	        console.log('Reeeejected!', e);
	    });


---touch

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



//创建一个可写的,可枚举的,可配置的属性p
o2 = Object.create({}, {
  p: {
    value: 42, 
    writable: true,
    enumerable: true,
    configurable: true  //true表示属性(不是值)可删除(delete)
  } 
});
delete o2.p; //如configurable: false 删不掉，不会报错
console.log(o2.p);

//----object 的方法


var obj = { pasta: "spaghetti", length: 10 }; 
Object.freeze(obj); 
// Try to add a new property, and then verify that it is not added. 
obj.newProp = 50;//不会报错，但是不成功
console.log(obj.newProp);//undefined

console.log(Object.isFrozen(obj));//true

 

const object1 = {
  property1: 42
};

Object.seal(object1); //现有属性如可以写，就可以修改值，但不能加，删
object1.property1 = 33;
console.log(object1.property1);
// expected output: 33

delete object1.property1; // 不会报错，但是不成功
console.log(object1.property1);
// expected output: 33

object1.property2 = 44;//不会报错，但是不成功
console.log(object1.property2);//undefined

console.log(Object.isSealed(object1));
 

======ECMA Script 2016  2017 

//大写的Function 定义函数 同上
var sayHi  =  new Function("sName", "sMessage", "console.log(\"Hello \" + sName + sMessage);");
sayHi('lisi ',' good afternon');

function doAdd(iNum) {
	alert(iNum + 10);
 }
//function有length
console.log(doAdd.length);	//输出 "1"

console.log(doAdd.toString());//toString函数的源代码
//可以被重写
Function.prototype.toString = function() {
	  return "Function code hidden";
	}

	
	
	
	class Polygon 
	{
		constructor(w,h) {
			this.width=w;
			this.height=h;
		}
	}

	class Square extends Polygon 
	{
	  constructor(length) {
		super(length, length);//子类构造器中必须先调用super  
		this.name = 'Square';
	  }
	  get area() { 
		return this.height * this.width;
	  }
	 
	}
	
	var squ=new Square(20); 
	console.log(squ.area ); 
	console.log(squ.name); 
			 
	 class myDate extends Date 
	 {
	  	constructor() {
		    super();
		  }
		
		  getFormattedDate() {
		    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
		    return this.getDate() + "-" + months[this.getMonth()] + "-" + this.getFullYear();
		  }
	}
	 console.log(new myDate().getFormattedDate()); 
	
	
	
const set1 = new Set([1, 2, 3, 4, 5]);//同C
//const set1 = new Set([1, 2, 3]);//报错

{
	let name="111";//let只在代码块中有效
	var name2="222";
}
console.log(name);//空串
console.log(name2);


function add(...values) { //rest运算符（…）
   let sum = 0;

   for (var val of values) { //of
      sum += val;
   }

   return sum;
}

add(2, 5, 3) // 10



function push(array, ...items) { 
  items.forEach(function(item) {
    array.push(item);
    console.log(item);
  });
}
 
var a = [];
push(a, "a1", "a2", "a3", "a4"); 






function f(s1, s2, s3, s4, s5) {
 	console.log(s1 + s2 + s3 + s4 +s5);
}

var a = ["a2", "a3", "a4", "a5"];

f("a1", ...a)
// a1a2a3a4a5


// ES5
console.log(Math.max.apply(null, [14, 3, 77]))

// ES6
console.log(Math.max(...[14, 3, 77])) //展开语法(Spread syntax)

// 等同于
console.log(Math.max(14, 3, 77));


//任意对象只要部署了next方法，就可以作为遍历器，但是next方法必须返回一个包含value和done两个属性的对象

function makeIterator(array){
    var nextIndex = 0;
    
    return {
       next: function(){
           return nextIndex < array.length ?
               {value: array[nextIndex++], done: false} :
               {done: true};
       }
    }
}

var it = makeIterator(['a', 'b']);

console.log(it.next().value) // 'a'
console.log(it.next().value) // 'b'
console.log(it.next().done)  // true


//ECMAScript 6草案定义的generator函数，需要在function关键字后面，加一个星号。然后，函数内部使用yield语句，定义遍历器的每个成员
function* helloWorldGenerator() {
    yield 'hello'; //暂停执行
    yield 'world';
}
var hw = helloWorldGenerator();
console.log(hw.next())
//{ value: 'hello', done: false }

console.log(hw.next())
//{ value: 'world', done: false }
console.log(hw.next())
//{ value: undefined, done: true }

console.log(hw.next())
 //{ value: undefined, done: true }



//遍历器的本质，其实是使用yield语句暂停执行它后面的操作

function* f() {
  for(var i=0; true; i++) {
    var reset = yield i; //第一次next()到这返回0，第二次next()从后面开始执行，reset是方法有参数值
    if(reset) { i = -1; }
  }
}

var g = f();

console.log(g.next()); // { value: 0, done: false }
console.log(g.next()); // { value: 1, done: false }
console.log(g.next(true)); // { value: 0, done: false }

// 


function* fibonacci() {
	var previous = 0, current = 1; 
	//while (true)  
		//二选一
	for (;;)
	{ 
		/*
		var temp = previous; 
		previous = current; 
		current = temp + current; 
		*/
		//以上三句可简写成一句
		[previous, current] = [current, previous + current];
		
		yield current; 
		if(current>100)
			break;
	} 
} 

for (var i of fibonacci()) { //不是next()，再调一次也可
	console.log(i); 
} 
 
console.log("abc".repeat(3)); // "abcabcabc"


console.log(Array.from(document.querySelectorAll('*'))) //[ html, head, meta, meta, title, script ]
console.log(Array.of(1, 2, 3)) // Similar to new Array(...), but without special one-arg behavior
console.log([0, 0, 0].fill(7, 1)) // [0,7,7]
console.log([0, 0, 0,0].fill(7,1,2))// [0,7,0,0]
console.log([1,2,3].findIndex(x => x == 2)) // 1

var array1 = [1, 2, 3, 4, 5];
console.log(array1.copyWithin(0, 3, 4))//把下标为3到下标为4前(不包括下标 4,即只下标3的值4)复制到下标为0开始
// [4, 2, 3, 4, 5]
 
 array1 = ['A', 'B', 'C', 'D','E'];
 console.log(array1.copyWithin(1, 3)); //把下标为3开始到结尾(下标5前,即DE) 复 制到下标为1开始（BC）
 //[ "A", "D", "E", "D", "E" ]

let iter=["a", "b", "c"].entries()  // iterator [0, "a"], [1,"b"], [2,"c"]
for (var val of iter) {
	console.log(val);
}
iter=["a", "b", "c"].keys()  // iterator 0, 1, 2
for (var val of iter) {  
	console.log(val);
}
iter=["a", "b", "c"].values()  // iterator "a", "b", "c"
for (var val of iter) {  
	console.log(val);
}

class Point
{
	constructor(x,y) {
		this.x=x;
		this.y=y;
	}
}
var first = { name: "Bob" };
var last = { lastName: "Smith" };
 

var person = Object.assign(first, last); //object.html做继承
//(target, ...sources );将来自一个或多个源对象中的值复制到一个目标对象。
console.log(person);

//0b和0o表示  二进制和八进制  
console.log(0b111110111 === 503) // true
console.log(0o767 === 503) // true

var birth=new Date();
var Person = {
	  name: '张三',
	  //等同于birth: birth
	  birth,
	  // 等同于hello: function ()...
	  hello() { console.log('我的名字是', this.name); }
	};
console.log(Person.birth);

var f = v => v; //（=>）定义函数。
//上面的箭头函数等同于：
var f1 = function(v) {
    return v;
};
console.log(f(20));
console.log(f1(20));


var sum1 = (num1, num2) => num1 + num2;//多参数用() 同scala
//等同于
var sum2 = function(num1, num2) {
 return num1 + num2;
};
console.log(sum1(1,2));
console.log(sum2(1,2));



//如果箭头函数直接返回一个对象，必须在对象外面加上括号。
var getTempItem = id => ({ id: id, name: "Temp" });
console.log(getTempItem(20));



//正常函数写法
[1,2,3].map(function (x) {
  return x * x;
});

var arr=[1,2,3];
var arrRes=arr.map(function (x) {
	  x=x+1;
	  return x;
	});//是复制的形式，修改的是返回新的数组，原数组不变,用for of []可以修改原数据
	
// 箭头函数写法
[1,2,3].map(x => x * x);


var handler = {
	    id: "123456",
	    init: function() {
	        document.addEventListener("click",
	                event => this.doSomething(event.type), false);//this都绑定handler对象
	    },
	    doSomething: function(type) {
	        console.log("Handling " + type  + " for " + this.id);
	    }
	};
handler.init();


function Point1(x = 0, y = 0) {//函数参数的默认值
   this.x = x;
   this.y = y;
}

var p = new Point1(); 

//--模板字符串 
// 多行字符串
var str=`In JavaScript this is
 not legal.`

 console.log(str);

// 字符串中嵌入变量
var name = "Bob", time = "today";
`Hello ${name}, how are you ${time}?`

var x = 1;
var y = 2;
console.log(`${ x } + ${ y } = ${ x + y}`) 
// "1 + 2 = 3"


//for…in循环读取键名，for…of循环读取键值。

var engines = new Set(["Gecko", "Trident", "Webkit", "Webkit"]);
for (var e of engines) {
    console.log(e);
}
// Gecko
// Trident
// Webkit

var es6 = new Map();
es6.set("edition", 6);
es6.set("committee", "TC39");
es6.set("standard", "ECMA-262");
for (var [name, value] of es6) {
  console.log(name + ": " + value);
}
// edition: 6
// committee: TC39
// standard: ECMA-262

 //数组推导
var a1 = [1, 2, 3, 4];
//var a2 = [i * 2 for (i of a1)];//不支持???
//console.log(a2) // [2, 4, 6, 8]
 
//字符串推导不支持???
//[c for (c of 'abcde') if (/[aeiou]/.test(c))].join('') // 'ae' 

//多变量赋值
var [a, b, c] = [1, 2, 3];

var [foo, [[bar], baz]] = [1, [[2], 3]]

var [,,third] = ["foo", "bar", "baz"]

var [head, ...tail] = [1, 2, 3, 4]

console.log(tail);// [2,3,4]


var [missing = true] = [];
console.log(missing)
// true

var { x = 3 } = {};
console.log(x)
// 3


var { foo, bar } = { foo: "lorem", bar: "ipsum" };

console.log(foo) // "lorem"
console.log(bar) // "ipsum"
 
//从函数返回多个值
function example() {
    return [1, 2, 3];
}

var [a, b, c] = example();

 

function f2({p1=1, p2, p3}) {//默认值 
	console.log(p1)
 } 
f2({ p2:2,p3:2});


/------ Symbol 是ECMAScript 6 中新增的数据类型
 是一种基本数据类型
 
console.log(Symbol("foo") === Symbol("foo"));  //false


var sym = Symbol(); //不能使用 new
console.log(typeof sym);//"symbol"
var symObj = Object(sym); 
console.log(typeof symObj); // "object"

//1. 用方括号添回
var a = {};
a[sym] = 'abc';
//2. 在对象内部定义
var a = {
[sym]: 'abc'
};
//3. 用defineProperty添加
var a = {};
Object.defineProperty(a, sym, { value: 'abc' });

var ownSymbolArrasy=Object.getOwnPropertySymbols(a); 
console.log(ownSymbolArrasy);

console.log(Symbol('desc').description);
// expected output: "desc"

console.log(Symbol.iterator.description);//Symbol.iterator是内建的Symbol
// expected output: "Symbol.iterator"

console.log(Symbol.for('foo').description); //Symbol.for() 方法和  Symbol.keyFor() 方法从全局的symbol注册
// expected output: "foo"

console.log(Symbol('foo').description + 'bar');
// expected output: "foobar"


var globalSym = Symbol.for('foo'); // create a new global symbol
console.log(Symbol.keyFor(globalSym)); // "foo" 



var myIterable = {}//让对象可被迭代
myIterable[Symbol.iterator] = function* () {
    yield 1;
    yield 2;
    yield 3;
};

for(let value of myIterable) { //要可迭代必须要有方法名 Symbol.iterator，按要求实现
    console.log(value);
}
// 1
// 2
// 3

// 正则表达式 symbols,Symbol.match,Symbol.replace,Symbol.search,Symbol.split
const regexp1 = /foo/;  
regexp1[Symbol.match] = false; //正则当字串
console.log('/foo/'.startsWith(regexp1)); // true
console.log('/baz/'.endsWith(regexp1)); // false


var paragraph = 'The quick brown fox jumped over the lazy dog. It barked.';
var regex = /[A-Z]/g;
var found = paragraph.match(regex);

console.log(found);
// expected output: Array ["T", "I"]

class Search1 {
  constructor(value) {
    this.value = value;
  }
  [Symbol.search](string) { 
    return string.indexOf(this.value);//string=foobar,this.value=bar
  }
}
console.log('foobar'.search(new Search1('bar')));
//3

var paragraph = 'lazy dog. really lazy?'; 
var regex = /[^\w\s]/g;
console.log(paragraph.search(regex));//输出下标
// expected output: 8
console.log(paragraph[paragraph.search(regex)]);
// expected output: "."
 

class Replace1 {
  constructor(value) {
    this.value = value;
  }
  [Symbol.replace](string) {
    return `s/${string}/${this.value}/g`;
  }
}

console.log('foo'.replace(new Replace1('bar')));
// expected output: "s/foo/bar/g"


class Split1 
{
  constructor(value) 
  {
    this.value = value;
  }
  [Symbol.split](string)
  {
    var index = string.indexOf(this.value);
    var res= this.value + string.substr(0, index) + "/"
    		+ string.substr(index + this.value.length);
    return res;
   
  }
}

console.log('foobar'.split(new Split1('foo')));
// expected output: "foo/bar"


//其他 symbols,Symbol.hasInstance ,Symbol.isConcatSpreadable,Symbol.unscopables,Symbol.toPrimitive,Symbol.toStringTag,Symbol.species
class Array1 
{
  static [Symbol.hasInstance](instance) 
  {
    return Array.isArray(instance);
  }
}
console.log([] instanceof Array1);
// expected output: true


const alpha = ['a', 'b', 'c'];
const numeric = [1, 2, 3];
let alphaNumeric = alpha.concat(numeric);
console.log(alphaNumeric);
// expected output: Array ["a", "b", "c", 1, 2, 3]
numeric[Symbol.isConcatSpreadable] = false;
alphaNumeric = alpha.concat(numeric);
console.log(alphaNumeric);
// expected output: Array ["a", "b", "c", Array [1, 2, 3]]


const object1 = {
  property1: 42
};
 
object1[Symbol.unscopables] = {
  property1: true
};
 
with (object1) {
  //console.log(property1);
  // expected output: Error: property1 is not defined
}


//An object without Symbol.toPrimitive property.
var obj1 = {};
console.log(+obj1);     // NaN
console.log(`${obj1}`); // "[object Object]"
console.log(obj1 + ''); // "[object Object]"

// An object with Symbol.toPrimitive property.
var obj2 = {
  [Symbol.toPrimitive](hint) {
    if (hint == 'number') {
      return 10;
    }
    if (hint == 'string') {
      return 'hello';
    }
    return true;
  }
};
console.log(+obj2);     // 10        -- hint is "number"
console.log(`${obj2}`); // "hello"   -- hint is "string"
console.log(obj2 + ''); // "true"    -- hint is "default"

//Object.prototype.toString=函数 重写toString()
//Object.prototype.toString.call 检测对象类
console.log(Object.prototype.toString.call('foo'));     // "[object String]"
console.log(Object.prototype.toString.call([1, 2]));    // "[object Array]"
console.log(Object.prototype.toString.call(3));         // "[object Number]"
console.log(Object.prototype.toString.call(true));      // "[object Boolean]"
console.log(Object.prototype.toString.call(new Map()));       // "[object Map]"
console.log(Object.prototype.toString.call(function* () {})); // "[object GeneratorFunction]"
console.log(Object.prototype.toString.call(Promise.resolve())); // "[object Promise]"
class ValidatorClass {
  get [Symbol.toStringTag]() { //get 只读属性
    return 'Validator';
  }
}
console.log(Object.prototype.toString.call(new ValidatorClass()));
// expected output: "[object Validator]"



class MyArray extends Array {
	  // Overwrite MyArray species to the parent Array constructor
	  static get [Symbol.species]() { return Array; }//instanceof 返回指定类型,get 只读属性，static
	}
const myArray = new MyArray(1, 2, 3);
const mapped = myArray.map(x => x * x);

console.log(mapped instanceof MyArray); 
//expected output: false

console.log(mapped instanceof Array);
//expected output: true
//-----------

//---- apply方法  Function​.prototype​.apply() 
//call()方法的作用和 apply() 方法类似，区别就是call()方法接受的是 argument 列表，而apply()方法接受的是一个数组。
var numbers = [5, 6, 2, 3, 7];
var max = Math.max.apply(null, numbers);//第一个参数是thisArg，非严格模式下，则指定为 null 或 undefined 时会自动替换为指向全局对象
console.log(max);// expected output: 7

var array = ['a', 'b'];
var elements = [0, 1, 2];
array.push.apply(array, elements);
console.info(array); // ["a", "b", 0, 1, 2] 
/*
var array2 = [];
var array = ['a', 'b'];
var elements = [0, 1, 2];
array.push.apply(array2, elements);
console.info(array); //'a', 'b'
console.info(array2);//0, 1, 2 结果同 elements
*/


//-----------ECMAScript 2016	
	
var array1 = [1, 2, 3];
console.log(array1.includes(2));
// expected output: true
var pets = ['cat', 'dog', 'bat'];
console.log(pets.includes('cat'));
// expected output: true
console.log(pets.includes('at'));
// expected output: false

const uint8 = new Uint8Array([10, 20, 30, 40, 50]);
console.log(uint8.includes(20));
// expected output: true
// check from position 3
console.log(uint8.includes(20, 3));
// expected output: false

//-----------ECMAScript 2017
var an_obj = { 100: 'a', 2: 'b', 7: 'c' };
console.log(Object.values(an_obj)); // ['b', 'c', 'a']

// getFoo is property which isn't enumerable
var my_obj = Object.create({}, { getFoo: { value: function() { return this.foo; } } });
my_obj.foo = 'bar';
console.log(Object.values(my_obj)); // ['bar']

// non-object argument will be coerced to an object
console.log(Object.values('foo')); // ['f', 'o', 'o']	
	
	
//array like object with random key ordering
const anObj = { 100: 'a', 2: 'b', 7: 'c' };
console.log(Object.entries(anObj)); // [ ['2', 'b'], ['7', 'c'], ['100', 'a'] ]

//getFoo is property which isn't enumerable
const myObj = Object.create({}, { getFoo: { value() { return this.foo; } } });
myObj.foo = 'bar';
console.log(Object.entries(myObj)); // [ ['foo', 'bar'] ]

//non-object argument will be coerced to an object
console.log(Object.entries('foo')); // [ ['0', 'f'], ['1', 'o'], ['2', 'o'] ]

//returns an empty array for any primitive type, since primitives have no own properties
console.log(Object.entries(100)); // [ ]

//iterate through key-value gracefully
const obj = { a: 5, b: 7, c: 9 };
for (const [key, value] of Object.entries(obj)) {
console.log(`${key} ${value}`); // "a 5", "b 7", "c 9"
}

//Or, using array extras
Object.entries(obj).forEach(([key, value]) => {
console.log(`${key} ${value}`); // "a 5", "b 7", "c 9"
});


//Object to a Map
const obj11 = { foo: 'bar', baz: 42 }; 
const map11 = new Map(Object.entries(obj11));
console.log(map11); // Map { foo: "bar", baz: 42 }	
	
	
	
const str1 = 'Breaded Mushrooms';
console.log(str1.padEnd(25, '.'));//targetLength [, padString])
// expected output: "Breaded Mushrooms........"

const str2 = '200';
console.log(str2.padEnd(5));
// expected output: "200  "

console.log('abc'.padEnd(6, "123456")); // "abc123"

const object12 = {
  property1: 42
};

const descriptors1 = Object.getOwnPropertyDescriptors(object12);
console.log(descriptors1.property1.writable);
// expected output: true
console.log(descriptors1.property1.value);
// expected output: 42




o = {};
Object.defineProperty(o, "baz", {
  value: 8675309,
  writable: false,
  enumerable: false
});
d = Object.getOwnPropertyDescriptor(o, "baz");
// d {
//   value: 8675309,
//   writable: false,
//   enumerable: false,
//   configurable: false
// }


//----hasOwnProperty
o = new Object();
o.prop = 'exists';
function changeO() {
  o.newprop = o.prop;
  delete o.prop;
}
o.hasOwnProperty('prop');   // 返回 true
changeO();
o.hasOwnProperty('prop');   // 返回 false



var empty = {};
var val=Object.isExtensible(empty); // === true 
//preventExtensions变为不扩展
Object.preventExtensions(empty);
val=Object.isExtensible(empty); // === false
//密封对象是不可扩展.
var sealed = Object.seal({});
val=Object.isExtensible(sealed); // === false
// 冻结对象也是不可扩展.
var frozen = Object.freeze({});
val=Object.isExtensible(frozen); // === false


-
// simple array
var arr = ['a', 'b', 'c'];
console.log(Object.keys(arr)); // console: ['0', '1', '2']
// array like object
var obj = { 0: 'a', 1: 'b', 2: 'c' };
console.log(Object.keys(obj)); // console: ['0', '1', '2']
// array like object with random key ordering
var anObj = { 100: 'a', 2: 'b', 7: 'c' };
console.log(Object.keys(anObj)); // console: ['2', '7', '100']
// getFoo is a property which isn't enumerable
var myObj = Object.create({}, {
  getFoo: {
    value: function () { return this.foo; }
  } 
});
myObj.foo = 1;
console.log(Object.keys(myObj)); // console: ['foo']
val=Object.keys("foo");//["0", "1", "2"]   

//Object.entries  返回值给定对象自身可枚举属性的键值对数组。
{
	const object1 = { foo: 'bar', baz: 42 };
	console.log(Object.entries(object1)[1]);
	// expected output: Array ["baz", 42]
	const object2 = { 0: 'a', 1: 'b', 2: 'c' };
	console.log(Object.entries(object2)[2]);
	// expected output: Array ["2", "c"]
	val=Object.entries(object2);//  [ [0,a],[1,b],[2,c] ]
}

//是否相等
Object.is('foo', 'foo');     // true
Object.is(window, window);   // true
Object.is('foo', 'bar');     // false
Object.is([], []);           // false
Object.is(0, -0);            // false
Object.is(NaN, 0/0);         // true


{
	const object1 = {
			  a: 'somestring',
			  b: 42,
			  c: false
			};
	console.log(Object.values(object1));
	// expected output: Array ["somestring", 42, false]
	//getFoo is property which isn't enumerable
	var my_obj = Object.create({}, { getFoo: { value: function() { return this.foo; } } });
	my_obj.foo = 'bar';
	console.log(Object.values(my_obj)); // ['bar']
	// non-object argument will be coerced to an object
	console.log(Object.values('foo')); // ['f', 'o', 'o']
}	

{// constructor一个只读的原始数据类型，返回一构建函数
	var o = {};
	o.constructor === Object; // true
	var o = new Object;
	o.constructor === Object; // true
	var a = [];
	a.constructor === Array; // true
	var a = new Array;
	a.constructor === Array; // true
	var n = new Number(3);
	n.constructor === Number; // true
	function Tree(name) {
		  this.name = name;
		}
	var theTree = new Tree('Redwood');
	console.log('theTree.constructor is ' + theTree.constructor);//函数源码
	
	//构建器重写
	function Parent() {}; 
	function CreatedConstructor() {} 
	CreatedConstructor.prototype = Object.create(Parent.prototype); 
	CreatedConstructor.prototype.constructor = CreatedConstructor; // set right constructor for further using
	CreatedConstructor.prototype.create = function create() { 
	  return new this.constructor();
	} 
	new CreatedConstructor().create().create(); // it's pretty fine
	
	//构建器不重写
	function ParentWithStatic() {}
	ParentWithStatic.startPosition = { x: 0, y:0 };
	ParentWithStatic.getStartPosition = function getStartPosition() {
	  return this.startPosition;
	} 
	function Child(x, y) {
	  this.position = {
	    x: x,
	    y: y
	  };
	}
	Child.prototype = Object.create(ParentWithStatic.prototype); 
	//Child.prototype.constructor = Child;//这行注释 下面this.constructor.getStartPosition()才不会报错
	Child.prototype.getOffsetByInitialPosition = function getOffsetByInitialPosition() {
	  var position = this.position;
	  var startPosition = this.constructor.getStartPosition(); // error undefined is not a function, since the constructor is Child
	  return {
	    offsetX: startPosition.x - position.x,
	    offsetY: startPosition.y - position.y
	  }
	};
	new Child(1,2).getOffsetByInitialPosition();
}


//https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Promise
//--Promise 	用于表示一个异步操作
// Axios 是一个基于 Promise 的 HTTP 客户端

//要么调用resolve函数来将promise状态改成fulfilled，要么调用reject 函数将promise的状态改为rejected
var promise1 = new Promise(function(resolve, reject) {
setTimeout(function() {
  resolve('foo');//这个函数是then函数传过来的
}, 300);
});

//then方法包含两个参数：onfulfilled (fulfilled满足的)和 onrejected，它们都是 Function 类型
promise1.then(function(value) {
console.log(value);
// expected output: "foo"
});

console.log(promise1);
//expected output: [object Promise]

//https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Promise/catch
var p1 = new Promise(function(resolve, reject) {
	  resolve('Success');
	}); 
p1.then(function(value) {
  console.log(value); // "Success!"
  return Promise.reject('oh, no!');//会跳到cach里，不会路到第二参数的函数
  //一样的效果 
  //throw 'oh, no!';//会跳到cach里，不会路到第二参数的函数
}).catch(function(e) {
  console.log(e); // "oh, no!"
}).then(function(){
  console.log('after a catch the chain is restored');
}, function () {
  console.log('Not fired due to the catch');
});
console.log(p1); 


//抛出一个错误，大多数时候将调用catch方法
var p1 = new Promise(function(resolve, reject) {
  throw 'Uh-oh!';
}); 
p1.catch(function(e) {
  console.log(e); // "Uh-oh!"
}); 

// 在异步函数中抛出的错误不会被catch捕获到
var p2 = new Promise(function(resolve, reject) {
  setTimeout(function() {
    throw 'Uncaught Exception!';
  }, 1000);
});

p2.catch(function(e) {
  console.log(e); // 不会执行
});

// 在resolve()后面抛出的错误会被忽略
var p3 = new Promise(function(resolve, reject) {
  resolve();
  throw 'Silenced Exception!';
});
p3.catch(function(e) {
   console.log(e); // 不会执行
});

//创建一个新的 Promise ，且已决议
var pp1 = Promise.resolve("calling next");
var pp2 = pp1.catch(function (reason) {
    //这个方法永远不会调用
    console.log("catch p1!");
    console.log(reason);
});
pp2.then(function (value) {
    console.log("next promise's onFulfilled"); /* next promise's onFulfilled */
    console.log(value); /* calling next */
}, function (reason) {
	//这个不会调用
    console.log("next promise's onRejected");
    console.log(reason);
});




//---async
function resolveAfter2Seconds() {
  return new Promise(resolve => { 
    setTimeout(() => {
      resolve('resolved'); //调用 resolve参加函数 表示返回值 ，后面可能语句
    }, 2000);
  });
}

async function asyncCall() {//如resolved  返回一个Promise，如rejected 抛异常
  console.log('calling');
  var result = await resolveAfter2Seconds();
  //await表示同步等待执行结束,只能用于async函数内
  console.log(result);
  // expected output: 'resolved'
} 

asyncCall();
 
 Promise.all([resolveAfter2Seconds(), resolveAfter1Second()])//并发
	  .then((messages) => {
	    console.log(messages[0]); // slow
	    console.log(messages[1]); // fast
	  });

	  
function resolveAfter2Seconds(x) {
	  return new Promise(resolve => {
	    setTimeout(() => {
	      resolve(x);
	    }, 2000);
	  });
	}

//-- await 并发执行 写法
(async function(x) { // async function expression used as an IIFE
  var p_a = resolveAfter2Seconds(20);//已经启动定时器， 并发执行
  var p_b = resolveAfter2Seconds(30);
  return x + await p_a + await p_b; // 
})(10).then(v => {
  console.log(v);  // prints 60 after 2 seconds.
});
 
 
var AsyncFunction = Object.getPrototypeOf(async function(){}).constructor //特别
var a = new AsyncFunction('a', 
                          'b', 
                          'return await resolveAfter2Seconds(a) + await resolveAfter2Seconds(b);');
a(10, 20).then(v => {
  console.log(v); // prints 30 after 4 seconds
});

var add = async function(x) { // async function expression assigned to a variable
  var a = await resolveAfter2Seconds(20);//只有完全执行后才会继续向下执行,/同步执行
  var b = await resolveAfter2Seconds(30);
  return x + a + b;
};


------strict
"use strict"; //整个脚本文件开启严格模式，需要在所有语句之前放
//早期都是 相反的  Sloppy mode  懒散的，草率的

//给不可写属性赋值
var obj1 = {};
Object.defineProperty(obj1, "x", { value: 42, writable: false });

try {
	obj1.x = 9; // 抛出TypeError错误
} catch (e) {
  console.log(e); 
}
//给只读属性赋值
var obj2 = { get x() { return 17; } };
try {
	obj2.x = 5; // 抛出TypeError错误
} catch (e) {
  console.log(e); 
}


//给不可扩展对象的新属性赋值
var fixed = {};
Object.preventExtensions(fixed);
try {
	fixed.newProp = "ohai"; // 抛出TypeError错误
} catch (e) {
  console.log(e); 
}

try {
  Object.defineProperty(fixed, 'property1', {
    value: 42
  });
} catch (e) {
  console.log(e); 
}
-------
 <!-- import 语句只能在声明了 type="module" 的 script 的标签中使用 -->
<script  src="mymodule/main.js" type="module"></script>

---main.js
import  {area,circumference} from './circle.js';//测试下来  必须以./或../ 或/开头，必须以.js结尾
console.log("圆面积：" + area(4));
console.log("圆周长：" + circumference(14));

import  * as circle from './circle.js'; 
console.log("圆面积：" + circle.area(4));
console.log("圆周长：" + circle.circumference(14));


import myDefault,{ cube, foo, graph } from './my-module.js';//文件中只一个default,导入要放在最前面,这里可以改名
 graph.options = {
     color:'blue',
     thickness:'3px'
 }; 
 graph.draw();
 console.log(cube(3)); // 27
 console.log(foo);    // 4.555806215962888
 console.log(myDefault);
 
   
//动态import，只对Chrome有效，Firefox不行
//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/import
let filename = 'circle.js';  
import('./' + filename). then(module =>{
    console.log(module);
}).catch(err => {
    console.log(err.message); 
});


---circle.js
export function area(radius) {
  return Math.PI * radius * radius;
}

export function circumference(radius) {
  return 2 * Math.PI * radius;
}
-----my-moudle.js 
function cube(x) {
  return x * x * x;
}
const foo = Math.PI + Math.SQRT2;
var graph = {
    options:{
        color:'white',
        thickness:'2px'
    },
    draw: function(){
        console.log('From graph draw function');
    }
}
export { cube, foo, graph };

let k; 
export default k = 12;  //一个文件中只能有一个默认导出
 
-----
JSON.parse('{"one":"１"}');
 
 //https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams
function testURLSearchParams()
{
	var paramsString = "q=URLUtils.searchParams&topic=api";
	var searchParams = new URLSearchParams(paramsString);

	//Iterate the search parameters.
	for (let p of searchParams) {
	  console.log(p);//p是数组0元素是键，1元素是值
	}

	console.log(	searchParams.has("topic") === true   ); // true
	console.log(	searchParams.get("topic") === "api"  ); // true
	console.log(	searchParams.getAll("topic") ); // ["api"]
	console.log(	searchParams.get("foo") === null    ); // true
	console.log(	searchParams.append("topic", "webdev") );
	console.log(	searchParams.toString() ); // "q=URLUtils.searchParams&topic=api&topic=webdev"
	console.log(	searchParams.set("topic", "More webdev") );
	console.log(	searchParams.toString() ); // "q=URLUtils.searchParams&topic=More+webdev"
	console.log(	searchParams.delete("topic") );
	console.log(	searchParams.toString() ); // "q=URLUtils.searchParams"
	
	var paramsString2 = "?query=value";
	var searchParams2 = new URLSearchParams(paramsString2);
	console.log(	searchParams2.has("query") ); // true 

	var url = new URL("http://example.com/search?query=%40");
	var searchParams3 = new URLSearchParams(url.search);
	console.log(	searchParams3.has("query")  );// true
}

====fetch

//https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API 左侧菜单有很多文章
//https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch

//fetch('http://127.0.0.1:8080/S_HTML5CSS3/jsonGet') //不能跨域
//是异步的，fetch没有提供同步请求的方法

fetch('/S_HTML5CSS3/jsonGet') 
.then(function(response) { //多个then按顺序执行
  return response.json();
})
.then(function(myJson) {
  console.log(JSON.stringify(myJson));
});
 
 
 var formData = "answer=42&name=lisi";  //POST,"Content-Type": "application/x-www-form-urlencoded",服务端request可以的
 //var formData = new URLSearchParams("answer=42&name=lisi");//也可以
 
//var formData = new FormData(); //POST,"Content-Type": "application/x-www-form-urlencoded",服务端request拿不到数据  ？？？？可能FormData只能只对文件上传
//formData.append("answer",42); 
//formData.append("name","lisi");

 return fetch(url, {//*表示默认值
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      mode: "cors", // no-cors, cors, *same-origin
      cache: "no-cache", // *default,no-store, no-cache, reload, force-cache, only-if-cached
      credentials: "same-origin", // include, *same-origin, omit
      headers: {
         //"Content-Type": "application/json;charset=UTF-8",  
           "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8", 
      },
      redirect: "follow", // manual, *follow, error
      referrer: "no-referrer", // no-referrer, *client
      body: formData, // body data type must match "Content-Type" header
	   //可以是 Blob, BufferSource, FormData, URLSearchParams, or USVString
  })
  .then(
		//response => response.json()//简写方式，如debug报body stream is locked
		 response =>{  
		 	var res=response.json()//json()函数返回一个Promise,只可调用一次
	  	 	console.log(res);
		  	return res;//如=>后加{，则要return，才可再次then
	  		}	 
		)  // parses JSON response into native Javascript objects
  then(data => 
		{console.log("raw:"+data);
		console.log("JSON:"+JSON.stringify(data))
		}
	) // JSON-string from `response.json()` call
.catch(error => console.error(error));


  function uploadFile()
{
	var formData = new FormData();
	var photos = document.querySelector("input[type='file'][multiple]");
	formData.append('title', 'My Vegas Vacation');
	for (var i = 0; i < photos.files.length; i++) {
	  formData.append('attache1', photos.files[i]);
	}
	/*
	fetch('http://127.0.0.1:8080/S_HTML5CSS3/uploadServlet3', {
	  method: 'POST',
	  body: formData
	})
	.then(response => response.json())
	.then(response => console.log('Success:', JSON.stringify(response)))
	.catch(error => console.error('Error:', error));
	*/
	var myHeaders = new Headers();
	myHeaders.append("X-Custom-Header", "ProcessThisImmediately");
	myHeaders.delete("X-Custom-Header");
	console.log(myHeaders.has("Content-Type")); 
	console.log(myHeaders.get("Content-Length")); 
	var myInit = { method: 'POST',
	               headers: myHeaders,
	               body: formData,
	               //mode: 'cors',
	               //cache: 'default' 
	               }; 
	//mode: 'cors'加不加都可 跨域可以请求到服务端，但JS端会调用到catch中，
	var myRequest = new Request('http://127.0.0.1:8080/S_HTML5CSS3/uploadServlet3', myInit);
	//var myRequest = new Request('http://localhost:8080/S_HTML5CSS3/uploadServlet3', myInit);
	fetch(myRequest)
	.then(function(response) {
	  if(response.ok) {
		  //return response.blob();
		  //return response.json();
		  console.log('ok:', response)
		  return response;
	  }
	  throw new Error('Network response was not ok.');
	})
	.then(response => console.log('Success:', response.json()))
	.catch(error => console.error('Error:', error));

}
<input type="file" multiple />
<button type="button" onclick="uploadFile()" >upload</button>


<img id="myImg"></img>
//var myImage = document.querySelector("#myImg");//querySelector一定要在HTML后
var myImage = document.querySelector("img");
fetch('../assets/img_logo.gif').then(function(response) {
  return response.blob();//blob()方法 
}).then(function(myBlob) {
  var objectURL = URL.createObjectURL(myBlob);
  myImage.src = objectURL;
});

-------DataView
//https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/DataView 
/* 
DataView 视图是一个可以从 ArrayBuffer 对象中读写多种数值类型的底层接口，使用它时，不用考虑不同平台的字节序问题
little endian 小端字节序：低字节存于内存低地址；高字节存于内存高地址  (intel全部是这个) 
big endian 大端字节序：高字节存于内存低地址；低字节存于内存高地址 (网络字节序) 
*/
  // create an ArrayBuffer with a size in bytes
var buffer = new ArrayBuffer(16);

// Create a couple of views
var view1 = new DataView(buffer);
var view2 = new DataView(buffer,12,4); //from byte 12 for the next 4 bytes
//new DataView(buffer [, byteOffset [, byteLength]]) 
view1.setInt8(12, 42); // put 42 in slot 12

console.log(view2.getInt8(0));
// expected output: 42


var littleEndian = (function() {
  var buffer = new ArrayBuffer(2);
  new DataView(buffer).setInt16(0, 256, true /* 设置值时，使用小端字节序 */);
  // Int16Array 使用系统字节序（由此可以判断系统字节序是否为小端字节序）
  return new Int16Array(buffer)[0] === 256;
})();
console.log(littleEndian); // 返回 true 或 false


console.log("2**3="+2**3); //**表示次方
-------- Proxy
//https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Proxy
let handler = {
	get: function(target, name){//get函数格式
		return name in target ? target[name] : 37;
	}
};
let p = new Proxy({}, handler);
p.a = 1;
p.b = undefined;
console.log(p.a, p.b);    // 1, undefined
console.log('c' in p, p.c);    // false, 37
	
//----

let target = {};
let p1 = new Proxy(target, {});
p1.a = 37;   // 操作转发到目标
console.log(target.a);    // 37. 操作已经被正确地转发

//---
let validator = {
  set: function(obj, prop, value) {//set函数格式
	if (prop === 'age') {
	  if (!Number.isInteger(value)) {
		throw new TypeError('The age is not an integer');
	  }
	  if (value > 200) {
		throw new RangeError('The age seems invalid');
	  }
	} 
	// The default behavior to store the value
	obj[prop] = value;
  }
}; 
let person = new Proxy({}, validator);
person.age = 100;
console.log(person.age);  // 100
//person.age = 'young'; 
// 抛出异常: Uncaught TypeError: The age is not an integer
//person.age = 300; 
// 抛出异常: Uncaught RangeError: The age seems invalid

//---扩展构造函数
function extend(sup,base) {
  var descriptor = Object.getOwnPropertyDescriptor(
	base.prototype,"constructor"
  );
  base.prototype = Object.create(sup.prototype);//做继承，因做了修改，上面再执行没有返回值，后面要重新设置一次
  var handler = {
	construct: function(target, args) {  //construct 固定的构造函数名，new时被拦截，args以数组形式的参数
	  var obj = Object.create(base.prototype);
	  this.apply(target,obj,args);
	  return obj;
	},
	apply: function(target, that, args) {//自已的方法target没用上
	  sup.apply(that,args); //apply 参数是按数组传
	  base.apply(that,args);
	}
  };
  var proxy = new Proxy(base,handler);
  descriptor.value = proxy;//覆盖原来的方法
  Object.defineProperty(base.prototype, "constructor", descriptor);//重新设置
  return proxy;
}
var Person = function(name){
  this.name = name
};
var Boy = extend(Person, function(name, age) {
  this.age = age;
});
Boy.prototype.sex = "M";
var Peter = new Boy("Peter", 13);
console.log(Peter.sex);  // "M"
console.log(Peter.name); // "Peter"
console.log(Peter.age);  // 13

{
	p = new Proxy({}, {
	  has: function(target, prop) { //has 方法 用于 in操作
		console.log('called: ' + prop);
		return true;
	  }
	});
	console.log('a' in p); // "called: a"
						   // true
}


-------- Reflect
https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Reflect
方法里有示例


var d = Reflect.construct(Date, [1776, 6, 4]);//类型，参数
console.log(d instanceof Date); // true
console.log(d.getFullYear()); // 1776

function someConstructor() {}
var result = Reflect.construct(Array, [], someConstructor);//类型，参数，构造器
console.log(Reflect.getPrototypeOf(result));  
console.log(Array.isArray(result)); // true


const student = {};
Reflect.defineProperty(student, "name", {value: "Mike"}); //(target, propertyKey, attributes),返回是否成功
console.log(student.name); // "Mike"

var obj = { x: 1, y: 2 };
Reflect.deleteProperty(obj, "x"); // true
console.log(obj); // { y: 2 }
var arr = [1, 2, 3, 4, 5];
Reflect.deleteProperty(arr, "3"); // true
console.log(arr); // [1, 2, 3, , 5]
// 如果属性不存在，返回 true
var res=Reflect.deleteProperty({}, "foo"); // true
// 如果属性不可配置，返回 false
res=Reflect.deleteProperty(Object.freeze({foo: 1}), "foo"); // false



//Object
var obj = { x: 1, y: 2 };
var val=Reflect.get(obj, "x"); // 1
// Array
val=Reflect.get(["zero", "one"], 1); // "one"
// Proxy with a get handler
var x = {p: 1};
var obj = new Proxy(x, {
  get(target, k, r) { //target 就是x ,k是属性名，r是receiver即 Proxy或者继承Proxy的对象
	  return k + "bar"; 
  }
});
val=Reflect.get(obj, "foo"); // "foobar"


val=Reflect.has({x: 0}, "x"); // true
val=Reflect.has({x: 0}, "y"); // false
// 如果该属性存在于原型链中，返回true 
val=Reflect.has({x: 0}, "toString");
// Proxy 对象的 .has() 句柄方法
obj = new Proxy({}, {
  has(t, k) { return k.startsWith("door"); }
});
val=Reflect.has(obj, "doorbell"); // true
val=Reflect.has(obj, "dormitory"); // false




val=Reflect.ownKeys({z: 3, y: 2, x: 1}); // [ "z", "y", "x" ]
val=Reflect.ownKeys([]); // ["length"]
var sym = Symbol.for("comet");
var sym2 = Symbol.for("meteor");
var obj = {[sym]: 0, "str": 0, "773": 0, "0": 0,
           [sym2]: 0, "-1": 0, "8": 0, "second str": 0};
val=Reflect.ownKeys(obj);
// [ "0", "8", "773", "str", "-1", "second str", Symbol(comet), Symbol(meteor) ]
// Indexes in numeric order, 
// strings in insertion order, 
// symbols in insertion order

--------展开 ... (spread) ,
var obj1 = { foo: 'bar', x: 42 };
var obj2 = { foo: 'baz', y: 13 };
var clonedObj = { ...obj1 };
// 克隆后的对象: { foo: "bar", x: 42 }
var mergedObj = { ...obj1, ...obj2 };//相同属性后面覆盖前面的
// 合并后的对象: { foo: "baz", x: 42, y: 13 }




var obj1 = { foo: 'bar', x: 42 };
var obj2 = { foo: 'baz', y: 13 };
//const merge = ( ...objects ) => ( { ...objects } ); //同下
const merge = ( ...objects ) => { //参数是reset
		return  {...objects} //变成key为0,1的对象
	}; 
var mergedObj = merge ( obj1, obj2);
// Object { 0: { foo: 'bar', x: 42 }, 1: { foo: 'baz', y: 13 } }

 //在 ES2015+ 中，在对象中放一个类似 ComponentA 的变量名其实是 ComponentA: ComponentA 的缩写
 var ComponentA={a:1}
 console.log({ComponentA});

----dataset
<div id="user" data-id="1234567890" data-user="johndoe" data-date-of-birth>
	John Doe (html中用 - 式命名)
</div>

var el = document.querySelector('#user');
// el.id == 'user'
// el.dataset.id === '1234567890'
// el.dataset.user === 'johndoe'
// el.dataset.dateOfBirth === ''

el.dataset.dateOfBirth = '1960-10-03'; //JS驼峰命名

// 'someDataAttr' in el.dataset === false

el.dataset.someDataAttr = 'mydata';
// 'someDataAttr' in el.dataset === true


//----ECMAScript 2018
//扩展语法和reset参数 

var objOld={id:123,name:"lisi"};
let objClone = { ...objOld };//对象可以直接复制
console.log(objClone); 
 

//[Symbol.asyncIterator]属性
const myAsyncIterable = {
	    async* [Symbol.asyncIterator]() {// async*  函数内yield
	        yield "hello";
	        yield "async";
	        yield "iteration!";
	    }
	};
	(async () => {
	//全写为async function  (){
	    for await (const x of myAsyncIterable) {
	        console.log(x);
	        // expected output:
	        //    "hello"
	        //    "async"
	        //    "iteration!"
	    }
	})();

// for await...of
	const asyncIterable = {
		  [Symbol.asyncIterator]() { //[Symbol.asyncIterator]函数内返回对象
		    return {
		      i: 0,
		      next() { //每次迭代调用next()方法,返回Promise对象格式有属性value,done
		        if (this.i < 3) {
		          return Promise.resolve({ value: this.i++, done: false });
		        }

		        return Promise.resolve({ done: true });
		      }
		    };
		  }
		};
		(async function() {
		   for await (let num of asyncIterable) {
		     console.log(num);
		   }
		})();
		// 0
		// 1
		// 2
		
		async function* asyncGenerator() {
		  let i = 0;
		  while (i < 3) {
		    yield i++;
		  }
		}
		(async function() {
		  for await (let num of asyncGenerator()) {
		    console.log(num);
		  }
		})();
		// 0
		// 1
		// 2

async function* streamAsyncIterator(stream) //stream 参数
{
  const reader = stream.getReader();//Streams API 还是实验阶段
  try {
    while (true) {
      const { done, value } = await reader.read();//带done属性
      if (done) {
        return;
      }
      yield value;
    }
  } finally {
    reader.releaseLock();
  }
}
// Fetches data from url and calculates response size using the async generator.
async function getResponseSize(url) {
  const response = await fetch(url);
  // Will hold the size of the response, in bytes.
  let responseSize = 0;
  // The for-await-of loop. Async iterates over each portion of the response.
  for await (const chunk of streamAsyncIterator(response.body)) {
    // Incrementing the total response length.
    responseSize += chunk.length;
  }
  console.log(`Response Size: ${responseSize} bytes`);
  // expected output: "Response Size: 1071472"
  return responseSize;
}
getResponseSize('https://jsonplaceholder.typicode.com/photos');
// 共享内存
//var sab = new SharedArrayBuffer(1024);//chrome不认
//worker.postMessage(sab);

//Promise.prototype.finally()
let isLoading = true;
fetch("/S_HTML5CSS3/jsonGet").then(function(response) {
    var contentType = response.headers.get("content-type");
    //typeof(contentType) =='string'
    if(contentType && contentType.includes("application/json")) {//includes方法string也有
      return response.json();
    }
    throw new TypeError("Oops, we haven't got JSON!");
  })
  .then(function(json) { console.log('response json is '+json) })
  .catch(function(error) { console.error(error); /* this line can also throw, e.g. when console = {} */ })
  .finally(function() { isLoading = false; });

//RegExp/dotAll 还未实现
//const regex1 = new RegExp('foo', 's');//s表示Allows . to match newlines
//onsole.log(regex1.dotAll);//表示是否加了s标志
// expected output: true
 

 
//----ECMAScript 2019 
https://developer.mozilla.org/en-US/docs/Archive/Web/JavaScript/ECMAScript_Next_support_in_Mozilla#ECMAScript_2019

//Array的flat
const arr2 = [1, 2, [3, 4, [5, 6]]];
arr2.flat();
// [1, 2, 3, 4, [5, 6]]
const arr3 = [1, 2, [3, 4, [5, 6]]];
arr3.flat(2);
// [1, 2, 3, 4, 5, 6]
const arr4 = [1, 2, [3, 4, [5, 6, [7, 8, [9, 10]]]]];
arr4.flat(Infinity);
// [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

//Array的flatMap
{
	let arr1 = [1, 2, 3, 4];
	arr1.map(x => [x * 2]); 
	// [[2], [4], [6], [8]]
	arr1.flatMap(x => [x * 2]);
	// [2, 4, 6, 8]
	// only one level is flattened
	arr1.flatMap(x => [[x * 2]]);
	// [[2], [4], [6], [8]]
}
{
	let arr1 = ["it's Sunny in", "", "California"];
	arr1.map(x => x.split(" "));
	// [["it's","Sunny","in"],[""],["California"]]
	arr1.flatMap(x => x.split(" "));
	// ["it's","Sunny","in", "", "California"]
	
	var arr = [1, 2, 3, 4];
	arr.flatMap(x => [x, x * 2]);
	// is equivalent to
	arr.reduce((acc, x) => acc.concat([x, x * 2]), []);
	// [1, 2, 2, 4, 3, 6, 4, 8]
}
//Object.fromEntries()
{

	const entries = new Map([
	  ['foo', 'bar'],
	  ['baz', 42]
	]);
	const obj = Object.fromEntries(entries);
	console.log(obj);
	// expected output: Object { foo: "bar", baz: 42 } 
}
const greeting = '   Hello world!   ';
console.log(greeting.trimStart());
// expected output: "Hello world!   ";
console.log(greeting.trimEnd());
// expected output: "   Hello world!";


function sum(a, b) {
	  return a + b;
}
console.log(sum.toString()); //输出为 Function code hidden


console.log(Symbol('desc').description);
//expected output: "desc"
console.log(Symbol.iterator.description);
//expected output: "Symbol.iterator"
console.log(Symbol.for('foo').description);
//expected output: "foo"



--------ECMASCript   
https://www.ecma-international.org/





