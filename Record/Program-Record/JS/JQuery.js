http://www.w3school.com.cn/jquery/index.asp
http://api.jquery.com/

Apatana 3.1
--eclipse插件Spket
preferences->Spket->JavaScript Prfiles->new...->输入jQuery->选择jQuery->Add File 按钮->  选择jQuery-1.7.2.js 
对.js文件open with ->spket javascript editor-> 可以按ctrl拖动放大缩小字体

Hplus 美工使用的工具

Chrome 可以看到某个元素是否被动态增加了事件,有一个EventListener标签,对jquery增加的也很准,而edge则不是很准(Firefox 无)

----jquery其它插件
Highcharts ,HighStock 收费的
jqPlot 免费的
Sparklines  免费的
	https://omnipotent.net/jquery.sparkline/#s-about
InsDep
https://www.insdep.com/
---css
#myid 
{
	border:1px solid red;
	background-imgage:url();
	background-repeat:repeat-x;
	background-position:bottom;
}

border-collapse:collapse

ul,li
{
	list-style:none;//去ul和li前的小点
	padding:0;//去缩进,IE不行的要加margin:0
	margin:0;
}

text-decoration:none;//去<a>的下划线
padding-left:20px;//左缩进
background-position:3px center;//x,y
display:block; 充满整个区域//IE不认,只能用inline-block

<li style="float:left"  //把ul下的li变成横向,其它元素会紧跟最后一个,自己在后面元素的左边,可right,none
clear:left //移动自己,使左边没有float元素,可right,none,both

 CSS渐变
Firefox:  background: -moz-linear-gradient(top,  #ccc,  #000);//线性渐变,从顶部开始,开始颜色是#ccc
IE:		filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#cccccc', endColorstr='#000000');//效果同上
				GradientType  1:默认值,水平渐变. 0:垂直渐变
border:1px solid black;
border-bottom:0; //CSS是有先后顺序的
	

position:relative;//absolute
top:-1px;

z-index:100;//间提必须position是absolute或者relative

var jsForm=document.getElementById("newForm");
$(jsForm);//来把JS变量转换为jQuery变量


document.getElementById("newForm").reset();
$('#newForm')[0].reset() ; //[0]来把jQuery变量转换为JS变量
  
---------
$('li').filter(':even').css('background-color', 'red');
$("div:has(p)")

$("form input") 找到表单中所有的 input 元素,子级的子级
$("form > input") 找到表单中第一层子级 input 元素
$("label + input") 匹配所有跟在 label 后面的 input 元素
$("form ~ input") 找到所有与表单同辈的 input 元素
$("tr:visible") 
$(element).is(":visible")
 [attribute!=value]  等价于 :not([attr=value])
$("input[name^='news']")  查找所有 name 以 'news' 开始的 input 元素
$("input[name$='letter']") 查找所有 name 以 'letter' 结尾的 input 元素
$("input[name*='man']")  查找所有 name 包含 'man' 的 input 元素
 $("[href!='#']")		所有 href 属性的值不等于 "#" 的元素
	
$(":text"); 同 $("input[type='text']") 
类似的有 
:input  包括 <select>,<textarea>,<button>
:text
:password
:radio
:checkbox
:submit
:image
:reset
:button
:file
:enabled
:disabled
:checked
:selected

:hidden
:visible
:empty


firefox ->firebug中以选中一个区域 (如是encodeURI("test.jsp?username=张三")) ,右击->add Watch
		HTML->Lagout 选择后界面会出现标尺

<script type="text/javascript" src="js/jquery-1.9.1.js"></script><!--不能使用<script src=""/>的简单形式-->
<script type="text/javascript">
<!--
	var jqueryObj=$("#myid");
	var username=jqueryObj.val();
	//jQuery.get("url",data,callback)
	$.get("xxx?name="+username,null,mycallback);//使用jQuery.get()方法

	function mycallback(data)
	{
		alert(data);
		var divObj=$("#mydiv");//经常使用,要保存,提示性能
		divObj.html(data);//返回纯文本

		var jqueryObj=${data};//XML
		var msg=jqueryObj.children().text();//<xx>abc</xx> ,把该元素的所有子节点的文本合并在一起
		divObj.html(msg)
	}

	.click(fun);
	.keyup(fun);
	.addClass('css');
	.removeClass('css');
	
	//看文档
	$.ajax
	({
		type:"POST",
		url:"test.jsp",
		data:"name=lisi",
		async:false,//是否异步
		dataType:"xml",//服务器端一定要返回XML,可不要这个
		success:mycallback , //只对成功完成的,complete完成 ==4,error
		error:function(e) // 新版本是三个参数, Function( jqXHR jqXHR, String textStatus, String errorThrown )
		//textStatus 像error，errorThrown值像Bad Request
		{ 
			alert(e.responseText);//是HTML文本
		} 
	});
	 function mycallback(data)
     { } 
	//IE缓存  ,每次是传一个参数=日期
//internet选项->高级->总是以 UTF-8 发送URL ,打勾
//中文服务端tomcat  把iso8859-1 转换成 UTF-8,就可以显示了(firefox),IE中不行用encodeURI("test.jsp?username=张三")

方法二
	1.请求中有中文乱码 客户->服务
	JS页面用encodeURI(encodeURI("test.jsp?username=张三"));//两次encodeURI,传给服务器会做一次decode,再手工一次
	服务用 URLDecoder.decode(request.getParameter("username"),"UTF-8");//做一次decode
	
	2.响应中有中文 服务->客户
	 服务端用 obj.put("username",URLEncoder.encode("李","UTF-8"));
	 JS端用  alert(decodeURI(json[i].username));



Servlet端
JSONArray array = new JSONArray();
JSONObject obj = new JSONObject();
obj.put("username",URLEncoder.encode("李","UTF-8"));
obj.put("password", 123);
array.add(obj);
resp.getWriter().write(array.toString());
//[{"username":"李","password":123}]

var fields = $( "form" ).serializeArray();//select多选和checkbox会生成多个相同的key
console.log(JSON.stringify(fields));

 var formData=$("#theQueryForm").serialize();//就是组装成name=val&age=22的形式
 $( "form" ).submit(function( event ) {
  console.log( $( this ).serializeArray() );
  }
  //全局
	$.ajaxSetup({
		//timeout: 3000,
		beforeSend: function (xhr)
		{
			//显示oading
		},
		complete: function (xhr, status)
		{
			//隐藏loading
			if(status=='success')
			{

			}else
			{

			}
		}
	});
	
	
	
  function resetForm(jqForm){
	$(':input',"#queryForm")
	 //jqForm.find(':input')
	.not(':button, :submit, :reset, :hidden') //多个可用,分隔，查询也可，如$("#theBills,#signTime").val('');
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
}


$.ajax({
   type: "POST",
   url:"listServlet",
   dataType:"json",
 //data: "name=John&location=Boston",  
 //data:formData
   success: function(json)
   {
		$(json).each(function(i)//里每个是对象返回数组的index
		{ 
				alert(decodeURI(json[i].username));
				alert(json[i].password); 
		});
   }
});
 	
		
var config=
{
	type: 'POST',
	contentType:'application/json;charset=UTF-8',//请求是JSON   
	dataType: 'json',//响应是JSON
	url:root+'/json/queryEmployeeVO.mvc' ,
	//data:JSON.stringify({employee_id:'123',first_name:'李四'}),//OK
	data:'{"employee_id":123,"first_name":"李四"}',//OK,内部Key必须是"
	success:function(response)
	{
		alert("success:"+response.allClothes[1]);
	}
};
$.ajax (config);


$("input[name='myName']")//<input name="myName"/>


// 跨域 jsonp 
	$(document).ready(function()
	{ 
		$.ajax({
			 type: "get",
			 async: true,
			 url: "http://127.0.0.1:8080/S_jQuery/jQuery/crossDomainJsonp",
			 //url: "http://localhost:8080/S_jQuery/jQuery/crossDomainJsonp",
			 data:{flight:"FLT001"},
			 dataType: "jsonp",
			 jsonp: "callback",//默认为:callback，传到服务端的参数名
			 jsonpCallback:"flightHandler",//传到服务端的参数值,即函数名(也可不传jquery自动生成名字),JS端生成这个函数调用success
			 success: function(json){
				 alert('您查询到航班'+json.code+'信息：票价： ' + json.price + ' 元，余票： ' + json.tickets + ' 张。');
			 },
			 error: function(){
				 alert('fail');
			 }
		 });
	 });
Servlet端
		String callback=request.getParameter("callback");//flightHandler 
		String flight=request.getParameter("flight"); 
  
		//页面中jsonpCallback:flightHandler
		String strFunc=callback+
		"({ code: '"+flight+
		 "',  price: 1780,"+
		"    tickets: 5"+
		"});";
		response.getWriter().write(strFunc);
		
 
 
 
//-->
</script>

<html>
	<input type ="text" id="myid"/> <div id="mydiv" ></div>
<html>


<SCRIPT LANGUAGE="JavaScript">
<!--
var obj={name:"lisi",age:22}//定义一个对象

XMLHttpRequest跨域问题(firefox),把有的以http://开头的url 改为一个代理
function convertURL(url)
{
	if(url.substring(0,7)=="http://")
	{
		url=url.replace("?","&");
	}
	return url="Proxy?url="+url;
}


删除表格行 $("td").parent().remove();

---
$("h2 a"); //可以选把所有的<h2><a>x</a></h2>或者<h2>xx<a>x</a></h2>
$.trim("  123 ");//函数

$(document).ready(function(){..}) //window.onload只可一个窗口中使用
//或者简写为
$(function(){...})

$("<p>sdf</p>") //创建DOM 无素

$("ul li:has(a)").addClass("mycssClass")//表示只对有<ul><li><a>加css

$("p:lt(2)").addClass("mycssClass")//表示对前2个<p>加css ,0开头

$("em:eq(1)").attr("title");//<em>元素的第2个的title属性
$("span").text("xx")//<span>标签中设值

$("button:gt(0)").attr("disabled","disabled");//设置大于第一个<button>元素的属性disabled的值是disabled

$("div").addClass("myclass1 myclass2");//加多个CSS

$("p").click(function()  //可以是一个数组，对所有都加click,中的this,指当前点击的
{
	$(this).toggleClass("mycssClass");//这里this 表示<p>,开关
})  //<p>xx</p> 的单击事件

$("#selectGender").find("option:selected").text()
$("p:first").text();//第一个<p>,只要文本,不要标签
$("p:first-child")
$("p:last").html("<a>x<a>")
$("texarea:first").val(""); 
$("img.eq(0)").clone().appendTo($("p"))//复制,是前(子)加到后(父)中

$("img").bind("click",function(){ //加事件
$("#myspanId").append("xxx");
}).bind()	//可多次bind

unbind("click",myFunc)//必须保存函数名,删事件

$("#myPageIndex")[0].tagName

$("p").show(); 
$("p").hide();


$("p").show(3000);//动画 , 效果不好
$("p").hide(3000);
$("p")fadeIn(3000);
$("p")fadeOut(3000);
slideUp(3000);//和show不同的是,如果是多个对像,并不成组,是各自的中心变化
slideDown(3000);

$.browser对象,//浏览器
$.browser.msie 
$.browser.mozilla	
$.browser.safari
$.browser.opera
以上都是 boolean类型
$.browser.version	firefox是rv:内核版本

标准W3c盒子模型,width和heigth,不包括padding-left,border-left,但IE盒子模型是包括的
从内到外 ,padding-left,border-left,margin-left

$.boxModel?"标准W3c盒子模型":"IE";//查检盒子模型

对IE 如加 <!DOCTYPE 声明就会变以为标准W3c盒子模型
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhml1/DTD/xhtml1-transitional.dtd">


var myArray={"one","two"};
$.each(myArray,function(iNum,value))//iNum是序号　,value

var myArray={one:1,two:2};
$.each(myArray,function(property,value))//property是属性名　,value

var myArray[2,4,8];
var res=$.grep(myArray,function(value)
						{return value>4 });//grep函数

var myArray=["a","b"];
myArray.join();//数组转字串 "a,b"
var res=$.map(myArray,function(value,index)//map函数,自定义数组转换字串,也可只第一个参数value,function(value){..}
						{return value+inex ;});

$("mydiv").load(xx.jsp);//ajax请求
$.post("xxx?name="+username,[data],mycallback,type);//data可以是对象

$.ajaxSetup(//ajax全局设置,以后再用$.ajax()做时可以省写这里的参数
{
url:"xx.jsp"
})



--------------------------
$("#myid1").click(function()
	{
		$("#myid2").trigger('click');
	});


创建表单
$("<input type='text'>");//相当于document.createElement

$("#x").val("xx");//可以修改值
.html()//同innerHTML,可取,可设
.text()//同innerText
.val() //同value,可取,可设

.width();//jQuery的设和取
.css();//样式,可取,可设
$("#xx").css("opacity",0.6);//跨浏览器？？

<div class="myredCSS myBlueCSS"/>可同时多个CSS

大多数jQuery 方法的调用后,返回的还是jQuery的对像 ,.xxx().yy()

在 <td><input type="text"/></td> 中text的click事件中返回false(不会向上传播),在text点击就不会触发td的click事件

//---测试end()
$('ul.first').find('.foo').css('background-color', 'red')//因现在当前是'.foo',如end()后变为'ul.first'
		.end().find('.bar').css('background-color', 'green');

$(".scroll-table input[type='checkbox']" ).each(function(i){
				$(this).prop("checked",true)  
			});
			
			//也可以
			.each(function(){
				i=$(this).index();
			});
			
var selectedBox="";
function fullCheck(contrl)
{
	selectedBox="";
	$(".scroll-table input[type='checkbox']" ).each(function(i){
		if(i==0) //是控制其它的开关的
			return;
		$(this).prop("checked",contrl.checked); //attr第三次调用就不行了???? 可能要和 removeAttr一起用,用 prop
		if(contrl.checked)
		{
			selectedBox+=$(this).parent().next().html()+",";
		}
	});
}




jQuery请求下载文件 
 function exportExcel()
 {
	 var form=$("#theQueryForm").clone();
	 form[0].action='<%=path%>/op/baseInfo/loadingCode/download.do';
	 form.attr("style","display:none");
	 $("body").append(form); 
	 form.submit();
	  form.remove();
 }

  $(document).keydown(function(event)
  {
     if(event.keyCode == 37){
		alert('37 左方向键');
	}else if (event.keyCode == 39){
		alert('39 右方向键');
	}
 });
  
-----------extend
//extend(dest,src1,src2,src3...);//它的含义是将src1,src2,src3...合并到dest中,
	var ori={};
	var result=$.extend(ori,{name:"Tom",age:21},{name:"Jerry",sex:"Boy"});
	console.log('ori='+JSON.stringify(ori)+",result="+JSON.stringify(result));
	
	
	$.extend({
		  hello:function(){console.log('hello');}
		  });//就是将hello方法合并到jquery的全局对象中。
	$.hello();
		  
	$.fn.extend({
	  hello2:function(){console.log('hello2');} //该方法将src合并到jquery的实例对象中去，如:
	 });
	
	$("#myDiv").hello2();
	//$("#myDiv").hello();//报错
	
	
	$.extend({net:{}});
	$.extend($.net,{ //　 这是在jquery全局对象中扩展一个net命名空间。
		   hello:function(){console.log('hello.net');}
		  })
	$.net.hello();
	
	
	//extend(boolean,dest,src1,src2,src3...)//  第一个参数boolean代表是否进行深度拷贝
	var result=$.extend( false, {},  
			{ name: "John", location:{city: "Boston",county:"USA"} },  
			{ last: "Resig", location: {state: "MA",county:"China"} }  //只要最后一个 location
   ); 
	console.log('dep copy=false,result='+JSON.stringify(result));
	//{"name":"John","location":{"state":"MA","county":"China"},"last":"Resig"}
	
	var result=$.extend( true, {},  
			{ name: "John", location:{city: "Boston",county:"USA"} },  
			{ last: "Resig", location: {state: "MA",county:"China"} }  
   ); 
	console.log('dep copy=true,result='+JSON.stringify(result));  //两location合并，中相同的county使用最后面的
	//{"name":"John","location":{"city":"Boston","county":"China","state":"MA"},"last":"Resig"}
	

=================================================jQuery插件 DataTables  表格
hadoop yarn使用这个  在bootstrap中


=================================================jQuery插件  validationEngine  2.6.4

<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/jquery.validationEngine-zh_CN.js"></script>

<link type="text/css" rel="stylesheet" href="css/validationEngine.jquery.css"/>

$(function (){
	$("#formID").validationEngine("attach",{ 
		promptPosition:"centerRight", 
		scroll:false 
	}); 
});
function ajaxRequest()
{
	var isPass=$("#formID").validationEngine('validate');
	if(isPass)
		alert('ajax request done');
}
<form id="formID"  action="http://www.baidu.com"  method="post"> 
	<fieldset>
		<legend> Required!</legend>
		<label>
			<span>Field is required : </span>
			<input value="" class="validate[required] text-input" type="text" name="req" id="req" />
		</label> <br/>
		<label>
			<span>maxSize 40: </span>
			<input type="text"  name="BillCode" class="validate[required,maxSize[40]]" />
		</label> <br/>
		<label>
			<span>email : </span>
			<input type="text"   name="email"   class="validate[custom[email]]" />
		</label> <br/>
		<label>
		<span>正则表左式要用custom,只数字字，最长8位，最小为0 : </span>
			<input type="text"   name="name"  class="validate[required,min[0.00],maxSize[8],custom[onlyNumberSp]]" />
		</label> <br/>
		<pre>	
			如要扩展custom,在语言文件中加入
			"onlyLetter": {
				"regex": /^[a-zA-Z\ \']+$/,
				"alertText": "* Letters only"
			},
		</pre>
	</fieldset>
	<input  type="submit"  value="Validate &amp; Send the form!"/>  <br/>
	<input  type="button" onclick="ajaxRequest()"  value="ajax request"/>
</form>
=================================================jQuery插件 fileupload  未测试
<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="fileupload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="fileupload/jquery.fileupload.js"></script>

$(function(){
	//如果放在初始化中，选择文件 时双击可提交请求   
	uploadImg();
});

 function uploadImg()
{
  $("#returnInput_upload").fileupload({
	   url:'http://127.0.0.1:8081/J_JavaEE/uploadServlet3',
	   maxChunkSize: 20480000, // 20MB 文件尺寸限制
	   limitMultiFileUploads:3, //一次最多允许上传的文件数
	   limitMultiFileUploadSize:20480000,// 20MB 文件尺寸限制
	   acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
	   loadImageMaxFileSize:20480000,// 20MB 文件尺寸限制
	   imageMinWidth:200, //图片最小宽度
	   imageMinHeight:150, //图片最小高度
	   imageMaxWidth: 2048,//超过此宽度的将被裁切至此宽度
	   imageMaxHeight: 1536,//超过此高度的将被裁切至此高度
	   imageCrop: true,//是否执行裁切
	   messages : function(){
		   acceptFileTypes : '123456789'
	   },
	   done:function(event,data){
			alter('返回数据'+data.result);
		}
	 });
} 
	 

 <input type=file id="returnInput_upload" name="attache1" multiple/>  选择文件 时双击可提交请求 ,要和服务器在一个项目中
 
 <input type=button  value="上传图片" onclick="uploadImg()"/>	 
  如果不放在初始化中， 没有效果

	 
=================================================jQuery插件 cookie  未测试	 


================================================= jQuery插件 barcode 条形码 
<script type="text/javascript" src="js/jquery-barcode-2.0.1.js"></script>
function genCode()
{
	//$("div[id*='mycode']")
	 $("#mycode").barcode("CB024B5PQ71", "code128",{barWidth:3, barHeight:130});
}

<div style='margin:0 auto;' id='mycode' style='text-align: center'> </div>
<button onclick="genCode()" >生成条形码</button>

=================================================jQuery插件 jqplot 图表  国内现在访问不了官方
基于Canvas 
<link rel="stylesheet" type="text/css" href="css/jquery.jqplot.css" />
<script language="javascript" type="text/javascript" src="js/jquery-1.7.2.js"></script>
<script language="javascript" type="text/javascript" src="js/jquery.jqplot.js"></script>
=================================================jQuery插件 Highcharts 图表

<!-- 不能使用简单标签的 <script src=""/>的形式 ,注意顺序 -->
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/highcharts.src.js"></script> 
<script type="text/javascript" src="js/highcharts-more.src.js"></script>
<script type="text/javascript" src="js/modules/exporting.src.js"></script>

有时钟示例,油箱表示例,时时更新图表示例

开源的，还有highstock, highmaps(收费的) 最新5.0.14版本 , 2017快有6的版本
 
=================================================jQuery插件Highstock 股票
带一个特殊的水平滚动条
=================================================jQuery插件Sparklines 小图表

=================================================jQuery 目前找不到 BPMN2 的插件

