http://www.w3school.com.cn/jquery/index.asp
http://api.jquery.com/

Apatana 3.1

--eclipse插件Spket
preferences->Spket->JavaScript Prfiles->new...->输入jQuery->选择jQuery->Add File 按钮->  选择jQuery-1.7.2.js 
对.js文件open with ->spket javascript editor-> 可以按ctrl拖动放大缩小字体

Hplus 美工使用的工具

Chrome 可以看到某个元素是否被动态增加了事件,有一个EventListener标签,对jquery增加的也很准,而edge则不是很准(Firefox 无)
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
		error:function(e){ alert(e.responseText) } 
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
 
=================================================jQueryUI
http://docs.jquery.com/UI

主题 css文件和images是同一级目录中

<link type="text/css" rel="stylesheet" 	href="themes/smoothness/jquery-ui.css"  />
<link type="text/css" rel="stylesheet"  href="themes/smoothness/jquery.ui.theme.css"/>
<script type="text/javascript" src="js/jquery-1.9.1.js" ></script> <!--不能使用<script src=""/>的简单形式-->
<script type="text/javascript" src="js/jquery-ui-1.10.1.custom.js"></script>

<script src="js/external/jquery.mousewheel.js"></script> <!-- 对spinner可以用滑轮 -->
<script src="js/external/globalize.js"></script> <!-- 对spinner可以用$-->


$(".block").draggable();//css选择器,DIV可拖动的
$(".block").draggable({helper:"clone"});//拖动后，复制出新的

$(".block").droppable(
	accept:function(t)//可以把什么拖入
	{
		return $(t).hasClass("mycss");
	},
	drop:function()//拖入后做什么
	{
		//$(this).xx
	}
); 
 

UI插件的demo中selectable->serialize.html中设置<li 加 value=""
取值用$("#selectable li").get(index).value;


firebug中可以在 css 中 ,选中增加,修改css

.select();
.trigger("focus").trigger("select");
.keyup(function(event){
	var code=event.which;//
});

.children();
.siblings();

$(".main >a").click(funciton(){
	$(this).next("ul");
	
}
<ul class=".main">
	<a></a>
	<ul></ul>
<ul>

.show();//jQuery的方法 和 display:none ,display:block一样
.show(300);//有动画的
.show("slow");//normal fast
.hide();
.toggle();//从hide 和 show切换,可加"normal"参数
.animate();
.slideDown();//可加"normal"参数
.slideUp();
.slideToggle();

.hover(function,function);//鼠标滑入,第一参数是进入,第二个是出去
也有mouseover(function)和mouseout(function)
var timeoutID=setTimeout(function(){},300);
clearTimeout(timeoutID);//可以取消定时

$("div").eq(2);//取所有的div的第三个元素,和get();

$("").load("xx.jsp H2");//也是选择器,只要页面中的H2标签
.bind("click",function);//addEvent
.bind("ajaxtStart",function);//jQuery定义的事件ajaxtStart,ajaxtStop

--性能
不在循环中调用 append(x);,而拼字串,结束后html(x);


冒泡到父级节点
$("#entryform input").bind("focus", function(){
    $(this).addClass("selected");
});
而用,很多元素同一个事件监听
$("#entryform").bind("focus", function(e){
    var $cell = $(e.target); // e.target 捕捉到触发的目标元素
    $cell.addClass("selected");
});
$("textarea").focus(function () {
  })  

$(document).ready();//还没下载完成就执行,页面一直是载入中的状态
$(window).load(function(){
	// 页面完全载入后才初始化的jQuery函数.
});

ID选择器的速度是最快的

jQuery选择器中有一个这样的选择器，它能指定上下文。
jQuery( expression, context );//能缩小选择器在DOM中搜索的范围


$("p").live("click",function(){ //改成live方式,慎用 .live()方法（应该说尽量不要使用）
	 alert( $(this).text() );
 });
$("button").click(function()
{
	$("<p>this is second p</p>").appendTo("body");
});



.is(":checked");//方法

$( "input:checked" ).length
$( "input[type=checkbox]" )
$('#searchResTable').find("input[type=radio]:checked").length


使用join()来拼接字符串,比用 " +  " 来优化性能，

$.ajax({
   type: "POST",
   contentType:"application/x-www-form-urlencoded;charset=utf-8" //可以解决中文,就不用escape("中") 
});
jQuery.js也要修改 application/x-www-form-urlencoded 为 application/x-www-form-urlencoded;charset=utf-8


escape 方法不能够用来对统一资源标示码 (URI) 进行编码。对其编码应使用 encodeURI 和encodeURIComponent 方法。



$("p.test").hide() - 隐藏所有 class="test" 的段落
$("ul li:first") 每个 <ul> 的第一个 <li> 元素 
$("[href$='.jpg']") 所有带有以 ".jpg" 结尾的属性值的 href 属性 
$("div#intro .head") id="intro" 的 <div> 元素中的所有 class="head" 的元素 

<div class="ui-icon ui-icon-minusthick">减号图标</div>
<div class="ui-icon ui-icon-plusthick">加号图标</div>
<div class="ui-icon ui-icon-arrowthick-2-n-s">南北宽箭头图标</div>
<div class="ui-icon ui-icon-circle-arrow-e">带圆底,东箭头图标</div>
<div class="ui-icon ui-icon-circle-arrow-s">带圆底,南箭头图标</div>
<div class="ui-icon ui-icon-circle-check">带圆底,对号图标</div>
<div class="ui-icon ui-icon-triangle-1-s">南三角图标</div>
<div class="ui-icon ui-icon-close">X关闭图标</div>
<div class="ui-icon ui-icon-gear">齿轮图标</div>
<div class="ui-icon ui-icon-locked">锁图标</div>
<span class="ui-icon ui-icon-volume-on" ></span><!-- 声音图标 -->
<span class="ui-icon ui-icon-signal" ></span> <!-- 无线信号图标 -->
<span class="ui-icon ui-icon-pencil"></span> <!-- 吸色图标 -->
<div class="ui-icon ui-icon-alert"> 叹号图标</div>
<span class="ui-icon ui-icon-disk"></span>软盘图标
<span class="ui-icon ui-icon-zoomin"></span>放大图标
<span class="ui-icon ui-icon-zoomout"></span>缩小图标
<span class="ui-icon ui-icon-print"></span>打印图标
<!-- 其它的看.css文件中,和图片中的图标-->
----日期组件
 <input type="text" id="datepicker" size="30"/></p>
 
$( "#datepicker" ).datepicker();//不能第一次使用 "option"方式
$( "#datepicker" ).datepicker( "option", "dateFormat", "yy-mm-dd");//两个y,就会显示四位年,可mm/dd/yy
$( "#datepicker" ).datepicker( "option", "showAnim", "clip" );//clip效果不错

$( "#datepicker1" ).datepicker({
	dateFormat: "yy-mm-dd",
	showAnim : "clip",
	changeMonth: true,//下拉选择,月
	changeYear: true, //下拉选择,年
	showButtonPanel: true,//有今天,关闭按钮,没有清除按钮???

	showWeek: true,//显示周 
	firstDay: 1,//周1是第一个
	
	showOn: "button",//以点击图标的方式显示
	buttonImage: "images/calendar.gif",//图像位置
	buttonImageOnly: true
});

$( "#datepicker" ).datepicker({ minDate: -20, maxDate: "+1M +10D" });//只可选最多前20天,后12个月+10天

$( "#datepicker3" ).datepicker();//,可显示在div中 ,中文????

----autocomplete
<style>
	/* 对.ui-autocomplete ,overflow-y: auto;加垂直滚动条 */
	.ui-autocomplete {
		max-height: 100px;
		overflow-y: auto;
		overflow-x: hidden;
	}
</style>
$( "#tags" ).autocomplete(//<input type="text"
		{ 
			source: availableTags ,//["ActionScript","C++"]
			select: function( event, ui ) {//选择的处理函数
				 alert("select :"+ui.item.label );//ui.item.label
			},
			change: function( event, ui ) {
				if(ui.item == null){ //如没有选择就提示，清空输入的 
					$( "#tags" ).val(""); 
				}
			}
		}
);

服务端 resp.setContentType("application/json; charset=utf-8");//解决返回中文问题

var cache = {};
//var  root="/J_AjaxServer";
var  root="http://localhost:8080/J_AjaxServer";//localhost 或者 127.0.0.1是不同的，client和server要相同
$( "#remote" ).autocomplete({//<input type="text"
		minLength: 2,//至少输入2个字符才开始搜索
		 delay: 500,
		source: function( request, response ) 
		{//函数原形
			var term = request.term;//输入的值
			if ( term in cache ) {//自己的缓存
				response( cache[ term ] );
				return;
			}
			//使用$.ajax请求
			$.ajax({
			   type: "POST",
			   url:root+"/queryLanguage_JSON?myMaxRows=12&myStartWith="+request.term,//request.term输入的值
			   dataType:"json",
			   success: function(json)
			   {
				   var res= $.map( json , function( item )//$.map 自定义数组转换字串
										{
											return {
												label: decodeURI(item.label),//JSONE格式{label:x  ,value:y}
												value: item.value
											}
										}
									);
					cache[ term ]=res ;//放入自己的缓存
					response(res);//传入的回调函数
			   }
			});
		},
		change: function( event, ui )
		{ 
			if(ui.item == null){//如没有选择就提示，清空输入的 
				$( "#remote" ).val(""); 
			}
		},
		select: function( event, ui ) 
		{ 
			 alert("select :"+ui.item.label+"val :"+ui.item.value); 
		},
		focus: function( event, ui ) {//鼠标滑过结果项上时
			$( "#remote" ).val( ui.item.label ); 
			return false;
		},
		/* 
		open: function() {//结果打开时
			alert("open ");
		},
		close: function() {//结果关闭时
			alert("close ");
		}
		
		,create: function( event, ui ) { //初始化后调用 
			alert("create ");
		},
	  search: function( event, ui ) {//开始搜索时
		  alert("search ");
	  } 
		*/
		
		//数据 太多不能出现滚动条
});


-----button
<script type="text/javascript">
		$(function() {
 
			$( "input[type=submit],input[type=button],input[type=reset],button" ).button();
 
			$( "#check" ).button();
			$( "#format" ).buttonset();
		});
		
		$(function() {
			$( "#myRadio" ).buttonset();
		});
		
		$(function() {
			$( "button:first" ).button({
				icons: {
					primary: "ui-icon-locked"
				},
				text: false
			}).next().button({
				icons: {
					primary: "ui-icon-locked"
				}
			}).next().button({
				icons: {
					primary: "ui-icon-gear",// 齿轮
					secondary: "ui-icon-triangle-1-s"//向下的键头
				}
			}).next().button({
				icons: {
					primary: "ui-icon-gear",
					secondary: "ui-icon-triangle-1-s"
				},
				text: false
			});
		});
	</script>
	
	
	<style>
		.ui-menu { 
				position: absolute; /* 是浮在上面的   */
				width: 100px; 
			 }
	</style>
	<script>
	$(function() {
		$( "#rerun" )
			.button()
			.click(function() {
				alert( "Running the last action" );
			})
			.next()//#select
				.button({
					text: false,
					icons: {
						primary: "ui-icon-triangle-1-s"
					}
				})
				.click(function() {
					var menu = $( this ).parent().next().show().position({ //$(this).parent().next()是<ul>
						my: "left top",//当前的<ul>元素的左顶 与 #select 的左底对齐
						at: "left bottom",
						of: this //#select
					});
					//在其它地方点击,菜单会被隐藏
					$( document ).one( "click", function() {//one ,一次性的事件处理函数,只会被执行一次,因每次单击都会被注册
						menu.hide();
					});
					return false;
				})
				.parent()
					.buttonset()
					.next()
						.hide()
						.menu();//menu()操作对<ul>元素,子级<li>是菜单项,再级<ul>是子菜单
	});
	</script>
<input type="checkbox" id="check" /><label for="check">Toggle</label>
	
	<!--  必须有 id="x" -->
	<div id="format">
		<input type="checkbox" id="check1" /><label for="check1">B</label>
		<input type="checkbox" id="check2" /><label for="check2">I</label>
		<input type="checkbox" id="check3"/><label for="check3">U</label>
	</div>
	<!--  必须有 id="x" -->
	<div id="myRadio">
		<input type="radio" id="radio1" name="group" /><label for="radio1">Choice 1</label>
		<input type="radio" id="radio2" name="group" checked="checked" /><label for="radio2">Choice 2</label>
		<input type="radio" id="radio3" name="group" /><label for="radio3">Choice 3</label>
	</div>
	 	
 	<button>Button with icon only</button>
	<button>Button with icon on the left</button>
	<button>Button with two icons</button>
	<button>Button with two icons and no text</button>


	<!-- 弹出菜单 -->
	<div class="ui-widget-header ui-corner-all"> <!-- 工具栏式 -->
		<div>
			<button id="rerun">Run last action</button>
			<button id="select">Select an action</button>
		</div>
		<ul>
			<li><a href="#">Open...</a></li>
			<li><a href="#">Save</a></li>
			<li><a href="#">Delete</a></li>
		</ul>
	</div>
	<div>这个不会被推开</div>

----position函数
<div id="targetElement">
  <div class="positionDiv" id="position3"></div>
  <div class="positionDiv" id="position4"></div>
</div>
$("#position3").position({ //我的右中 与 #targetElement的右底对齐
  my: "right center", //先 左右(中),后 顶底(中),
  at: "right bottom",
  of: "#targetElement",
  using:animate//修改位置时一个回调函数
});
function animate( to ) {//带top,left属性的对象参数传入
	$( "#log").text( to.top+","+to.left );
	$( this ).stop( true, false ).animate( to );
}  	
$(document).mousemove(function(ev){
  $("#position4").position({ //div没有加z-index
    my: "left bottom",
    of: ev,
    //offset: {top:50,left:-50}
    collision: "fit" //在拖放到浏览器的边界时的变化方式,默认是flip
  });
});
 
$(function() {
	//滚动条动时图像也动
	$( window ).scroll(function() {
		 if($("#loadingImage").css("display") == "block")
		 {
				var vtop = (document.body.clientHeight  - $("#loadingImage").height())/2; 
				var vleft = (document.body.clientWidth - $("#loadingImage").width())/2; 
				var sTop = $(document).scrollTop(); 
				var sLeft = $(document).scrollLeft(); 
				$("#loadingImage").offset( { top: vtop+sTop , left: vleft+sLeft});
		 }
	});
});
//window.onscroll = function(){ }
	
//----加不可操作背景
var bgGreyDiv=$("<div></div>");
bgGreyDiv.css("z-index","2").css("position","absolute").css("background-color","grey").css("opacity",0.2)
.css("width",$(document).width()).css("height",$(document).height());
$(document.body).append(bgGreyDiv);
bgGreyDiv.offset( { top: 0, left:0});
 
/* 
var bgGreyDiv=$("<div style='background:grey;opacity:0.2;z-index:2;position:absolute;left:0;top:0'></div>");
bgGreyDiv.css("width",$(document).width()).css("height",$(document).height());
$(document.body).append(bgGreyDiv);
*/

//----屏幕中心
$("#loadingImage").css("z-index","2").css("position","absolute");
var vtop = ( document.body.clientHeight  - $("#loadingImage").height())/2; 
var vleft = (document.body.clientWidth - $("#loadingImage").width())/2; 
var sTop = $(document).scrollTop(); 
var sLeft = $(document).scrollLeft(); 
$("#loadingImage").offset( { top: vtop+sTop , left: vleft+sLeft});
$("#loadingImage").css("display","block");


----
$( "#name" ).addClass( "ui-state-error" );//红边和底
$( ".validateTips" ).addClass( "ui-state-highlight" );//黄边和底
<fieldset>
	<label for="name">Name</label>
	<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
</fieldset>
<p class="validateTips">All form fields are required.</p>
<table class="ui-widget ui-widget-content">
	<thead>
		<tr class="ui-widget-header ">
			<th>Name</th>
			<th>Password</th>
		</tr>
	</thead>
</table>
----drag
//$( "#draggable" ).draggable();
$( "#draggable" ).draggable({
	start: function() {
	},
	drag: function() {
	},
	stop: function() {
	},
	snap: true,//自动捕获到其它可拖动的边角
	grid: [ 50,50 ]//每50个单位跳动一,和snap为true只可以一个生效
});
$( "#draggable_x" ).draggable({ axis: "y" });//只可在y方向移动
$( "#draggable_y" ).draggable({ axis: "x" });
$( "#draggable3" ).draggable({ containment: "#containment-wrapper"});
$( "#draggable5" ).draggable({ containment: "parent" });//只可在父容器中移动

$( "#draggable_help" ).draggable({ opacity: 0.7, helper: "clone" });//产生一个新影,原不动
$( "#draggable_help_my" ).draggable({
	cursor: "move",
	cursorAt: { top: -12, left: -20 },
	helper: function( event ) {
		return $( "<div class='ui-widget-header'>I'm a custom helper</div>" );
	}
});

$( "#set div" ).draggable({ stack: "#set div" });//stack 的所有元素,是自动调整,当然拖动的放在其它的前面
$( "#draggable_handle" ).draggable({ handle: "p" });//#draggable_handle中的<p>才可拖动
$( "#draggable_handle_not" ).draggable({ cancel: "p.ui-widget-header" });//#draggable_handle中的<p>不能拖动
	
$( "#sortable" ).sortable();
$( "#draggable_sort" ).draggable({
	connectToSortable: "#sortable",//可以拖到一个排序区
	helper: "clone"
});
$( "ul, li" ).disableSelection();//li文本不可被选择

<ul id="sortable" style="list-style-type: none"> <!-- 不要项目前的 "." -->
---------drop
$( "#draggable" ).draggable();
$( "#droppable, #droppable-inner" ).droppable({
	accept: "#draggable",//接收只定元素,可用":not(#draggable)""
	greedy: true,//防止父级容器接收
	activeClass: "ui-state-hover",
	hoverClass: "ui-state-active",
	drop: function( event, ui ) {//ui.draggable.得到拖入的元素
		$( this )
			.addClass( "ui-state-highlight" )
			.find( "> p" )
				.html( "Dropped!" );
	}
});
$( "#draggable_revert" ).draggable({ revert: "valid" });//不可拖入droppable的地方
$( "#draggable_revert2" ).draggable({ revert: "invalid" });//必须拖到可droppable的地方

$.fn.left=function(using){};//像是扩展jQuery的,参数可不传的,后可用$( "img:eq(0)" ).left();
.stop( [clearQueue ] [, jumpToEnd ] );//停止当前动画,两个boolean参数
$.easing  如何用????
-----------progressbar
$(function() {
	var progressLabel = $( ".progress-label" );//是里面的DIV的CSS
	var progressbarDiv=$( "#progressbarDiv" );
	progressbarDiv.progressbar({
		value: 37,
		change: function() {
			progressLabel.text( progressbarDiv.progressbar( "value" ) + "%" );
		},
		complete: function() {
			progressLabel.text( "Complete!" );
		}
	});
	progressbarValue =$( "#progressbarDiv" ).find( ".ui-progressbar-value" );//只是这个CSS,要在建立后
	progressbarValue.css({"background": 'yellow'});
	function doProgress() 
	{
		var val = $( "#progressbarDiv" ).progressbar( "value" ) || 0;//取值
				  $( "#progressbarDiv" ).progressbar( "value", val + 1 );//修改,转换为数字
		if ( val < 99 ) 
			setTimeout( doProgress, 1000 );
	}
	doProgress();
});
function modify()
{
	var txt=$("#downloaded").val();
	var num=parseInt(txt);//必须转换为数字
	$( "#progressbarDiv" ).progressbar( "option", {value: num});//option有于修改,必须是建立后
}
function notConfirm()
{ 
	$( "#progressbarNoValue" ).progressbar({value: 37});
	$( "#progressbarNoValue" ).progressbar( "option", "value", false );//不确定进度值的
}
-----resizable
.ui-resizable-se {bottom: 17px;}/*south east方向的键头 ,向上移动,对textarea无效*/

$( "#resizable_also" ).resizable();
$( "#resizable" ).resizable({
			//aspectRatio: 16 / 9, //约束纵横比
			maxHeight: 350,
			maxWidth: 450,
			minHeight: 150,//使用时要与初始大小同,最好不要与aspectRatio同时使用
			minWidth: 200,
			grid: 50,
			alsoResize: "#resizable_also"//对另一个可resizable()做连动
		});
$( "#resizable_in" ).resizable({
	containment: "#container", //高度出去的,与<h3>有关
	//ghost: true,//ghost不能与containment 一起使用,Bug!!!
});
/* 
$( "#container" ).resizable({ //不能对已是containment的DIV再resizable,(即不能实现父容器大小变,子容器大小也变) Bug!!!
	//alsoResize: "#resizable_in" 
});
*/
$( "#resizable_ghost" ).resizable({
	ghost: true,
	animate: true,
	helper: "ui-resizable-helper"//自己的CSS
});
-----slider
#slider .ui-slider-range { background: red; }  /* 修改 slider外观*/
#slider .ui-slider-handle { border-color: red; }

//$( "#slider" ).slider();
$( "#slider" ).slider({
	range: "min",//"min"小数值区高亮 ,"max"大数值区高亮
	orientation: "vertical",
	value:100,
	min: 0,
	max: 500,
	//step: 50,
	slide: function( event, ui ) {//滑动过程中
		$( "#amount" ).text( "slide$" + ui.value );//ui带value属性
	},
	change:function( event, ui ) {//滑动完成
		$( "#amount" ).text( "change$" + ui.value );
	}
});
$( "#amount" ).text( "$" + $( "#slider" ).slider( "value" ) );//取值
//-----
$( "#slider-range" ).slider({
	range: true,
	min: 20,
	max: 500,
	values: [ 75, 300 ],//values数组
	slide: function( event, ui ) {
		$( "#amount_range" ).text( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );//ui带values数组
	}
});
$( "#amount_range" ).text( "$" + $( "#slider-range" ).slider( "values", 0 ) + //values 传索引,取值
	" - $" + $( "#slider-range" ).slider( "values", 1 ) );

$( "#mySelect" ).change(function() {
	$( "#slider" ).slider( "value", this.selectedIndex + 1 );//修改值 ,会调用change:的处理函数
});

 
-----sortable

/* 控制拖动时,产生虚线背景,颜色同前景色,!important 提高CSS优先级 */
.ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important;  }
.ui-sortable-placeholder * { visibility: hidden; }

$("#sortable1,#sortable2").sortable({
		placeholder: "ui-state-highlight",
		connectWith: ".connectedSortable",//与另一个sortable 可相互拖动自动排列
		dropOnEmpty: false ,//如果另一个sortable 是空的,则不可放入
		items: "li:not(.ui-state-disabled)",
		cancel: ".not_moved"
	}).disableSelection();
//------------------		
$( "#sortable_tab1, #sortable_tab2" ).sortable().disableSelection();
var $tabs = $( "#tabs" ).tabs();
var $tab_items = $( "ul:first li", $tabs ).droppable({
	accept: ".connectedSortable_tab li",
	hoverClass: "ui-state-hover",
	drop: function( event, ui ) {
		var $item = $( this );//this是被.droppable的tab标签页
		var $list = $( $item.find( "a" ).attr( "href" ) )//二级
			.find( ".connectedSortable_tab" );

		ui.draggable.hide( "slow", function() {
			$tabs.tabs( "option", "active", $tab_items.index( $item ) );//显示指定的标签页
			$( this ).appendTo( $list ).show( "slow" );//this是ui.draggable的列表项
		});
	}
});

-----spinner 数字上下微调

<script src="js/external/jquery.mousewheel.js"></script> <!-- 对spinner可以用滑轮 -->
<script src="js/external/globalize.js"></script> <!-- 对spinner可以用$-->

<input id="spinner">

var spinner = $( "#spinner" ).spinner(
	{
		step: 0.01,//小数
		min: 5,
		start: 1000,
		max: 2500,
		numberFormat: "C", //才可在前加$
		spin: function( event, ui )//还有change,stop 响应回调,但要重新去取值,不传的
			{
				$("#logDiv").append( $("<p>").html("spin value:"+ui.value) );//ui.value得到值
			}
	});
$( "#spinner" ).spinner( "option", "culture", "en-US" );//en-US是$
	
if ( spinner.spinner( "option", "disabled" ) )//尾多一个d字母,取状态 
	spinner.spinner( "enable" );//修改
else  
	spinner.spinner( "disable" );
	
spinner.spinner( "value" )  //取值
spinner.spinner( "value", 5 );//设值

-----tabs
<div id="tabs">
	<ul><!-- 会自动加 ui-tabs-nav 样式 -->
		<li><a href="#tabs-1">标签名</a></li>
	</ul>
	<div id="tabs-1">
		<p>面板</p>
	</div>
</div>

var tabs =$( "#tabs" ).tabs({
			//event: "mouseover",//滑过打开标签
			collapsible: true,//选择标签后,再次点击可收缩,不能是mouseover
		});
tabs.find( ".ui-tabs-nav" ).sortable({//ui-tabs-nav是所有标签按钮所在栏,加拖动功能
			axis: "x",
			stop: function() {
				tabs.tabs( "refresh" );//更新显示,包括标签按钮
			}
		});

tabs.delegate( "span.ui-icon-close", "click", function() {//.delegate 组件内部 加事件 
	var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );//closest最近的,remove后还可取属性aria-controls,
}
tabs.bind( "keyup", function( event ) {
	var panelId = tabs.find( ".ui-tabs-active" ).remove().attr( "aria-controls" );//.ui-tabs-active找当前激活的标签页
}
//-----
$( document ).tooltip({//所有表单 ,带title属性的做为提示信息
	track: true//跟着鼠标动
});
var tooltips = $( "[title]" ).tooltip();//所有带title属性的标签
tooltips.tooltip( "open" );//open
$( "#open-event" ).tooltip({
	show: {
		duration: "fast"
	},
	hide: {
		effect: "hide"
	}
	position: {
		my: "left top",//tooltip的顶端　对应#open-event　的底
		at: "left bottom"
	},
	open: function( event, ui ) {//open处理函数
		ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "fast" );//ui.tooltip.
	},
	items: "img, [data-geo], [title]",
	content: function() {//content:自定义 ,提示内容
		var element = $( this );
		if ( element.is( "[data-geo]" ) ) { //is 的使用
			var text = element.text();
			return "<img class='map' alt='" + text +
			"' src='http://maps.google.com/maps/api/staticmap?" +
			"zoom=11&size=350x350&maptype=terrain&sensor=false&center=" +
			text + "'>";
		}
		if ( element.is( "[title]" ) ) { 
			return element.attr( "title" );//attr 的使用
		}
		if ( element.is( "img" ) ) {
			return element.attr( "alt" );
		}
	}
	
});
//-----	Dialog
$( "#dialog" ).dialog({
				height: 600,
				width: 1024,
				modal: true
				});
<div id="dialog" title="弹出窗口">
</div>

=================================================jQueryMobile
使用HTML5技术

官方网上 有一个设置工具codiqa ,拖放,修改属性后,就可下载生成的HTML,JS代码

    <meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; user-scalable=no;" />
<!DOCTYPE html> 
<html> 
	<head> 
	<title>My Page</title> 
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<meta charset="utf-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<link rel="stylesheet" href="css/jquery.mobile-1.4.3.css" /> 
	<script src="js/jquery-2.0.3.js"></script><!--不能使用<script src=""/>的简单形式-->
	<script src="js/jquery.mobile-1.4.3.js"></script>
</head> 
<body>
 
<div data-role="page">			<!--  data-role="page"-->
	<div data-role="header" data-position="fixed">	<!--  data-role="header"-->
		<h1>My Title</h1>
	</div>
	<div data-role="content">	<!--  data-role="header"-->
		<p>Hello world</p>		
	</div>
	
	<div data-role="footer" class="ui-bar" data-position="fixed"> <!-- class="ui-bar"  -->
		<a href="#" data-icon="plus">Add</a>
	</div>	
</div>

</body>
</html>

----

<!--默认主题色 data-theme="a"  a=灰色  b=黑色  -->

<!-- 第一个,每一次显示这个面-->
<div data-role="page" id="foo"> <!--  data-role="page" id="foo" -->
	<div data-role="header"> <!--   data-role="header" -->
		<h1>Foo</h1>
	</div>
	<div data-role="content"> <!--   data-role="content" -->
		<p>I'm first in the source order so I'm shown as the page.</p>		
		<p>View internal page called <a href="#bar">bar</a></p><!-- 切换页,指向id的值 -->
	</div>
	<div data-role="footer"> <!--   data-role="footer" -->
		<h4>Page Footer</h4>
	</div>
</div>


<p><a href="#popup" data-role="button" data-rel="dialog" data-transition="pop">Show page "popup" (as a dialog)</a></p>
<!-- data-rel="dialog" data-transition="pop" 弹出对话框 -->


<div data-role="page" id="popup" data-close-btn="right" data-corners="false"> <!--data-close-btn="right" 关闭按钮"x"的位置,可 none 不要 -->
 	<div data-role="header" data-theme="e">
		<h1>Dialog</h1>
	</div>
 	<div data-role="content" data-theme="d">	
		<h2>Popup</h2>
		<p>I have an id of "popup" on my page container and only look like a dialog because the link to me had a <code>data-rel="dialog"</code> attribute which gives me this inset look and a <code>data-transition="pop"</code> attribute to change the transition to pop. Without this, I'd be styled as a normal page.</p>		
		<p><a href="#one" data-role="button" data-inline="true" data-icon="back">Back to page "one"</a></p>	
			<!-- data-inline="true" 按钮长度是文本的长度, data-icon="back" 在按钮左显示返回图标
			其它图标有plus ,minus ,delete,arrow-l,arrow-r,arrow-u,arrow-d,check ,gear(齿轮)
			,refresh,forward,back ,grid ,star ,alert ,info ,home ,search,bars ,edit 
			-->
	</div>
	<div data-role="footer">
		<h4>Page Footer</h4>
	</div>
</div>


<a href="pageTransaction2.html" data-role="button"  data-transition="fade" data-inline="true">page</a>
<!-- data-transition="fade" 切换页面的动画效果 ,可使用flip,turn,slide(左右方式),slideup,slidedown-->


<script type="text/javascript">
	$(document).on("click", ".show-page-loading-msg", function() 
				{
					//$.mobile.loading( 'show', { theme: "b", text: "foo", textonly: true });
					$.mobile.loading( 'show', {
						text: 'foo',//加载界面的提示文本
						textVisible: true,
						theme: 'z',
						html: ""
					});
				})
			.on("click", ".hide-page-loading-msg", function() 
				{
					$.mobile.loading( 'hide' );
				});
</script>
<div data-role="controlgroup"><!--  data-role="controlgroup"  -->
	<button class="show-page-loading-msg" data-icon="arrow-r" data-iconpos="right">Default loader</button>
	<button class="hide-page-loading-msg" data-icon="delete" data-iconpos="right">Hide</button>
</div>

 <!--data-rel="popup" 点击后在中间显示提示文本,data-rel文档在哪???????? -->
<a href="#basic" data-rel="popup" data-role="button">Default popup</a>
<div id="basic" data-role="popup" ><!--  data-role="popup"  -->
  <p>I am a default popup</p>
</div>



<!--可收缩的面板 data-role="collapsible-set" data-theme="c" data-content-theme="d"  data-mini="true" 标题变小 data-corners="false" 使用方角-->
<div data-role="collapsible-set" data-theme="c" data-content-theme="d" data-mini="true" data-corners="false">
	<div data-role="collapsible"> <!-- data-role="collapsible" -->
		<h3>Section 1</h3>
		<p>I'm the collapsible content for section 1</p>
	</div>
	<div data-role="collapsible" data-collapsed-icon="gear" data-expanded-icon="delete"  data-iconpos="right"><!-- 单独指定图标 -->
		<h3>Section 2</h3>
		<p>I'm the collapsible content for section 2</p>
	</div>
	<div data-role="collapsible" data-theme="e" data-content-theme="a"><!-- 单独指定主题 -->
		<h3>Section 3</h3>
		<p>I'm the collapsible content for section 3</p>
	</div>
</div>


<!-- data-filter="true" data-filter-reveal="true" 可选项是隐藏的 data-filter-placeholder="Search cars..." -->
<ul data-role="listview" data-filter="true" data-filter-reveal="true" data-filter-placeholder="Search cars...">
	<li><a href="#">Acura</a></li>
	<li><a href="#">Audi</a></li>
	 <li data-filtertext="NASDAQ:AAPL Apple Inc."><a href="#">Apple</a></li> 
	<!-- data-filtertext=的值被输入时可选中这项 -->
</ul>

----------表单

<!--type="range" 显示jQuery Mobile的风格,而不是浏览实现 , data-track-theme="d" 是小圆点的主题色 ,data-highlight="true" 前面显示彩色 -->
<label for="slider-3">Slider:</label>
<input name="slider-3" id="slider-3" data-track-theme="d" data-theme="b" data-highlight="true" min="0" max="100" value="50" step="10" type="range">
    

<!--class="ui-icon-alt" 图标反色  class="ui-icon-nodisc"  data-iconshadow="false"  -->
<a href="#" class="ui-icon-alt"    data-role="button" 							data-theme="b" data-icon="home" data-iconpos="notext"  data-inline="true">Home</a>
<a href="#" class="ui-icon-nodisc" data-role="button" data-iconshadow="false" 	data-theme="b" data-icon="arrow-r"  data-iconpos="notext"  data-inline="true">No disc or shadow</a>

<!-- class="ui-disabled" -->
<a href="#" data-role="button" class="ui-disabled">Disabled anchor via class</a>
<button disabled="">Button with disabled attribute</button> <!-- disabled="" -->

<!-- 单个方式 -->
<label>
	<input name="checkbox-0 " type="checkbox">Check me
</label>

<!-- <fieldset>是HTML5标签  <fieldset data-role="controlgroup">  -->
 <fieldset data-role="controlgroup" data-type="horizontal"> <!-- 也可加data-type="horizontal" -->
	<legend>Vertical:</legend> <!-- <legend> -->
	
	<input name="checkbox-v-2a" id="checkbox-v-2a" type="checkbox">
	<label for="checkbox-v-2a">One</label>
	
	<input name="checkbox-v-2b" id="checkbox-v-2b" type="checkbox">
	<label for="checkbox-v-2b">Two</label>
 </fieldset>

<fieldset data-role="controlgroup" data-mini="true">
	<legend>Vertical, mini sized:</legend>
	<input name="radio-choice-v-6" id="radio-choice-v-6a" value="on" checked="checked" type="radio">
	<label for="radio-choice-v-6a">One</label>
	<input name="radio-choice-v-6" id="radio-choice-v-6b" value="off" type="radio">
	<label for="radio-choice-v-6b">Two</label>
	<input name="radio-choice-v-6" id="radio-choice-v-6c" value="other" type="radio">
	<label for="radio-choice-v-6c">Three</label>
</fieldset>

<fieldset data-role="controlgroup" data-type="horizontal">
	<legend>Horizontal:</legend>
	<label for="select-h-2a">Select A</label>
	<!-- data-native-menu="false" 不使用浏览器主题-->
	<select name="select-h-2a" id="select-h-2a" data-native-menu="false" >
		<option value="#">One</option>
		<option value="#">Two</option>
		<option value="#">Three</option>
	</select>
	<label for="select-h-2b">Select B</label>
	<select name="select-h-2b" id="select-h-2b">
		<option value="#">One</option>
		<option value="#">Two</option>
		<option value="#">Three</option>
	</select>
</fieldset>

<select name="select-choice-8" id="select-choice-8" multiple="multiple" data-native-menu="false" data-icon="grid" data-iconpos="left">
	<option>Choose a few options:</option>
	<optgroup label="USPS">
		<option value="standard" selected="">Standard: 7 day</option>
		<option value="rush">Rush: 3 days</option>
		<option value="express">Express: next day</option>
		<option value="overnight">Overnight</option>
	</optgroup>
	<optgroup label="FedEx">
		<option value="firstOvernight">First Overnight</option>
		<option value="expressSaver">Express Saver</option>
		<option value="ground">Ground</option>
	</optgroup>
</select>

<!-- 切换开关  -->
<div data-role="fieldcontain"> <!-- data-role="fieldcontain"  -->
	<label for="slider2">Flip switch:</label>
	<select name="slider2" id="slider2" data-role="slider"><!--  data-role="slider" -->
		<option value="off">Off</option>
		<option value="on">On</option>
	</select>
</div>

<input  type="text" pattern="[0-9]*" /><!--  可验证,是jQueryMobile主题 -->
<input placeholder="Placeholder text..." type="search"><!-- 多搜索图标,多删除按钮 -->
<input data-clear-btn="true"  value="" type="number"><!-- data-clear-btn="true" 删除按钮 -->
<textarea cols="40" rows="8" name="textarea" id="textarea"></textarea> <!-- 是jQueryMobile主题 -->

----------
<div data-role="controlgroup" data-type="horizontal" data-mini="true">
	<!-- data-iconpos="notext" 不显示文本的按钮组-->
	<a href="#" data-role="button" data-iconpos="notext" data-icon="plus" data-theme="b">Add</a> 
	<a href="#" data-role="button" data-iconpos="notext" data-icon="delete" data-theme="b">Delete</a>
	<a href="#" data-role="button" data-iconpos="notext" data-icon="grid" data-theme="b">More</a>
</div>

<!-- class="ui-grid-solo" , 可class="ui-grid-a" ,间距 a>b>c>d -->
<div class="ui-grid-solo">
	<div class="ui-block-a"><button type="button" data-theme="b">More</button></div>
	<!--  class="ui-block-a" 第二列字母向后排 ,如再下一个ui-block-a是下一行-->
</div>


<ul data-role="listview" data-filter="true"> <!-- data-role="listview" 加data-filter="true" 会多一个输入框,过虑 -->
	<li><a href="#">Acura</a></li>
	<li><a href="#">Audi</a></li>
	<li><a href="#">BMW</a></li>
	<li><a href="#">Cadillac</a></li>
	<li><a href="#">Ferrari</a></li>
</ul>
<ol data-role="listview"> <!-- 带序号的1,2,... -->
    <li>Acura</li>
    <li>Audi</li>
</ol>
<ul data-role="listview" data-inset="true" data-divider-theme="d">
    <li data-role="list-divider">Mail</li> <!--  data-role="list-divider" 分组的 -->
    <li><a href="#">Inbox</a></li>
    <li><a href="#">Outbox</a></li>
    <li data-role="list-divider">Contacts</li>
    <li><a href="#">Friends</a></li>
    <li><a href="#">Work</a></li>
</ul>

<ul data-role="listview" data-count-theme="e" data-inset="true"> <!-- data-count-theme="c"  -->
    <li><a href="#">Inbox <span class="ui-li-count">12</span></a></li><!-- 计数 class="ui-li-count" -->
    <li><a href="#">Outbox <span class="ui-li-count">0</span></a></li>
</ul>

<ul data-role="listview" >
    <li><a href="#"><img src="assets/gf.png" alt="France" class="ui-li-icon ui-corner-none">France</a></li>
    <!-- class="ui-li-icon ui-corner-none" -->
</ul>

$(document).on("click", ".show-page-loading-msg", function() 
			{
				$.mobile.loading( 'show', {
					//html: "<font color='red'>这是HTML格式的</font>",
					html: "",//如有text的值,这个要为空
					text: 'foo',//加载界面的提示文本
					textVisible: true,
					textonly:false,
					theme: 'b'
				});
			})
		.on("click", ".hide-page-loading-msg", function() 
			{
				$.mobile.loading( 'hide' );
			});
<div data-role="controlgroup"> <!--  data-role="controlgroup"  -->
	<!-- data-iconpos="right"-->
	<button class="show-page-loading-msg" data-icon="arrow-r" data-iconpos="right" >Default loader</button>
	<button class="hide-page-loading-msg" data-icon="delete" data-iconpos="right" >Hide</button>
</div>


$(document).on("click", ".show-custom-loading-msg", function() 
		{
			 var html =$( this ).jqmData("html");//得到标签中data-html设置的值
			$.mobile.loading( 'show', {
				html: html,
				textVisible: true,
				textonly:false,
				theme: 'b'
			});
		})
<button class="show-custom-loading-msg" data-theme="c" data-textonly="true" data-textvisible="true" data-msgtext="Custom Loader" data-inline="true" 
		data-html="<span class='ui-bar ui-shadow ui-overlay-d ui-corner-all'><img src='assets/jquery-logo.png' /><h2>is loading for you ...</h2></span>" data-iconpos="right">Custom HTML</button>

<!-- 如果多于5个,就会一行显示两个,否则显示在一行上(于 data-grid="c"也有关) ,可用于header,footer中-->
<div data-role="navbar" class="ui-body-d ui-body"> <!--  data-role="navbar" class="ui-body-d ui-body" 使用d主题-->
	<ul>
		<li><a href="#" class="ui-btn-active">One</a></li> <!-- class="ui-btn-active" -->
		<li><a href="#">Two</a></li>
		<li><a href="#">Three</a></li>
		<li ><a href="#" data-icon="arrow-r" data-theme="e">Four</a></li>
	</ul>
</div>

 <!-- data-icon="custom" 自定义图标方式,使用css -->
<style>
	#chat .ui-icon { background:  url(assets/09-chat2.png); }
	#email .ui-icon { background:  url(assets/18-envelope.png) 50% 50% no-repeat;  }
</style>
<div data-role="navbar">
	<ul>
		<li><a href="#" id="chat" data-icon="custom">Chat</a></li>
		<li><a href="#" id="email" data-icon="custom">Email</a></li>
	</ul>
</div>

--- 弹出面板
<a href="#leftpanel3" data-role="button" data-inline="true" data-mini="true">Overlay</a>
<!--data-role="panel" 显示时可按esc键退出
	data-position="left" ,可right
	data-display="overlay" 浮上面,reveal 藏在后,push两个排在一起-->
<div data-role="panel" id="leftpanel3" data-position="left" data-display="overlay" data-theme="a">
	<a href="#demo-links" data-rel="close" data-role="button" data-theme="a" data-icon="delete" data-inline="true">Close panel</a>
</div>

---查看放大图片
<!-- data-position-to="window"  -->
<a href="#popupSydney" data-rel="popup" data-position-to="window" data-transition="fade"><img class="popphoto" src="assets/sydney.jpg" alt="Sydney, Australia" style="width:10%;height:10%"></a>
<!-- data-overlay-theme="a"  -->
<div data-role="popup" id="popupSydney" data-overlay-theme="a" data-theme="d" data-corners="false">
	<a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a><img class="popphoto" src="assets/sydney.jpg" alt="Sydney, Australia">
	<!-- data-rel="back" class="ui-btn-right" -->
</div>



<div data-role="collapsible-set" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" 
<div data-role="popup" data-overlay-theme="a"  data-dismissible="false"  必须操作才可退出
<!-- data-rel="popup" data-position-to="window"窗口中心,可是origin,可是#指定的位置 -->


<table data-role="table" data-mode="columntoggle" class="ui-body-d ui-shadow table-stripe ui-responsive" 
	data-column-btn-theme="b" data-column-btn-text="Columns to display..." data-column-popup-theme="a"> <!-- 用于选择显示的列 -->


=================================================jQuery插件 easyUI-1.3.5
1.5 有免费版本带源码

GPL License,开源,布局,树,表格,表单(select可输入选择)


<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/icon.css" />
<script type="text/javascript" src="${webRoot}/js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="${webRoot}/js/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="${webRoot}/js/easyui-lang-zh_CN.js"></script>  <!-- 导入后验证等的信息就为中文了 -->
 
//---表单  
$.extend($.fn.validatebox.defaults.rules, 
	{
		equals://自定义方法
		{
			validator: function(value,param)
			{
				return value == $(param[0]).val();
			},
			message: '两次输入的密码不相同'
		}
	});
$(function()
{
	 //JS动态增加的表单做验证
   $("<input type='text' name='other'/>").validatebox({
	required: true,
	validType: 'email'
	}).appendTo($('form'));
});

function submitForm(){
	$('form').form('submit');
}

<div class="icon-tip" style="width:20px;height:20px"></div>灯图标
<div class="easyui-panel" title="New Topic">
  <form id="ff"> <!-- 有form是为验证 -->
	password: <input id="pwd" name="pwd" type="password" class="easyui-validatebox" data-options="required:true">
	repassword:<input id="rpwd" name="rpwd" type="password" class="easyui-validatebox"  required="required" validType="equals['#pwd']">
	email:<input class="easyui-validatebox" type="text" name="email" data-options="required:true,validType:'email'">
	language:<select class="easyui-combobox" name="language"> <!-- 可以输入查询-->
			</select>
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">Submit</a>		
  </form>
 </div>
//---日期
<input class="easyui-datetimebox" required style="width:150px">
//---表格
var mytoolbar = [{
		text:'增加',
		iconCls:'icon-add',
		handler:myInsert
	},
	'-'//分隔线
	,{
		text:'刷新',
		iconCls:'icon-reload',
		handler:function(){alert('刷新')}
	}];

var myLanguages=
[{
	 langValue:"C",
	 langLabel:"C 语言"
 },
 {
	 langValue:"Java",
	 langLabel:"Java 语言"
 },
 {
	 langValue:"PLSQL",
	 langLabel:"PLSQL 语言"
 }];
 
$(function(){
	$('#dg').datagrid({
		queryParams: {
			my_custome_param: 'my-easyUI-value'
		}
	} );
	
	for(index in myLanguages)
	{
		var opt=$("<option>").val( myLanguages[index].langValue ).html( myLanguages[index].langLabel);
		$("#commonlang").append(opt);
	}
	//easyUI 的 <select>是不同的
	$("#lang").combobox({
			data:myLanguages,//url:xxx
			valueField:'langValue',
			textField:'langLabel'
		});
});

//表格级的,刷新
function myBeforeEdit(index,row)
{
	row.editing = true;//自已新定义的属性
	updateActions(index);
}
function myAfterEdit(index,row)
{
	$.ajax
	({
		url:'../easyUI/updateData',
		type:"POST",
		dataType:"json",
		data:{
				command:(row.id)?"update":"save",
				id:row.id,
				username:row.username,
				language:row.language,
			}, 
		success:function(response)
		{
			if(response.statusCode==200)
			{
				if(! row.id) //save
				{
					row.id=response.additionObject;//newId
					updateActions(index);
				}
				$.messager.alert('提示','操作成功','info');
				/* jQueryUI
				$("<div>操作成功</div>").dialog({
					 modal: true,
					 buttons: {
						 Ok: function() {
							$( this ).dialog( "close" );
						 }
					 }
				 });
				*/
			}
		}
	});
	row.editing = false;
	updateActions(index);
}
function myCancelEdit(index,row)
{
	row.editing = false;
	updateActions(index);
}
function updateActions(index)
{
	$('#dg').datagrid('updateRow',{
		index: index,
		row:{}
	});
}
//--行级的
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}
function myEditrow(target){
	$('#dg').datagrid('beginEdit', getRowIndex(target));//调用onBeforeEdit:myBeforeEdit
}
function mySaveUpdateRow(target){
	$('#dg').datagrid('endEdit', getRowIndex(target));//调用onAfterEdit:myAfterEdit
}
function myCancelUpdateRow(target){
	$('#dg').datagrid('cancelEdit', getRowIndex(target));
}
function myInsert()
{
	var row = $('#dg').datagrid('getSelected');
	if (row){
		var index = $('#dg').datagrid('getRowIndex', row);//返回选中行的数据对象
	} else {
		index = 0;
	}
	$('#dg').datagrid('insertRow', {
		index: index,
		row:{
			isMan:"true",
			language:"Java",
			birthday:"2014-01-01"
		}
	});
	$('#dg').datagrid('selectRow',index);//选中指定行
	$('#dg').datagrid('beginEdit',index);
}
function myDeleterow(target){
	$.messager.confirm('确认','你真的要删除吗?',function(r)
	{
		if (r)
		{
			$.ajax
			({
				url:'../easyUI/updateData',
				type:"POST",
				dataType:"json",
				data:
				{
					id: $('#dg').datagrid('getRows')[ getRowIndex(target)].id , //getRows返回的是所有数据
					command:"delete",
				},
				success:function(response)
				{
					if(response.statusCode==200)
						$.messager.alert('提示','操作成功','info');
				}
			});
			$('#dg').datagrid('deleteRow', getRowIndex(target));//放后面
		}
	});
}
//--格式化显示
function myGenderFormatter(value,row,index)
{
	if (row.isMan){
		return "男";
	} else {
		return "女";
	}
}
function myActionFormatter(value,row,index)
{
	if (row.editing){//加  class="easyui-linkbutton"没效果 ???
		var s = '<a href="#" onclick="mySaveUpdateRow(this)">Save</a> ';
		var c = '<a href="#" onclick="myCancelUpdateRow(this)">Cancel</a>';
		return s+c;
	} else {
		var e = '<a href="#" onclick="myEditrow(this)">Edit</a> ';
		var d = '<a href="#" onclick="myDeleterow(this)">Delete</a>';
		return e+d;
	}
}

function mySearch()
{
	 $('#dg').datagrid('load',{
		date_from: $('#date_from').datebox('getValue'),//日期类型得值 
		date_to: $('#date_to').datebox('getValue'),
		 lang:$('#lang').val(),
		 user:$('#user').val()
		 });
	 
}
function myBarRemoveByCheckBox() //table 的 singleSelect:true
{
	var checkedItems = $('#dg').datagrid('getChecked');//得到所有的checkbox选择的行(是修改后的数据)
	var deleteIds = [];
	$.each(checkedItems, function(index, item){
		deleteIds.push(item.id);
	});               
	console.log(deleteIds.join(","));
}
所有的iconCls 的取值在icon.css中	
<div id="tb" style="padding:5px;height:auto">
	<div style="margin-bottom:5px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true"></a> <!-- 加 plain="true" 按钮没有立体感 -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"></a>
	</div>
	<div><!-- 因使用JSON不能用form -->
		User:	<input id="user" type="text">
		Date From:	<input id="date_from" type="text" style="width:90px" class="easyui-datebox">
		To: 		<input  id="date_to"  type="text" style="width:90px" class="easyui-datebox">
		Language: 
		<select id="lang" class="easyui-combobox" panelHeight="auto" style="width:100px">
			<option value="java">Java</option>
			<option value="c">C</option>
			<option value="plsql">PLSQL</option>
		</select>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="mySearch()" >搜索</a>
	</div>
</div>
 
<table id="dg" title="表格数据" style="width:1200px;height:300px" 
	data-options="
			rownumbers:true,
			singleSelect:true,
			autoRowHeight:false,
			collapsible:true,
			pagination:true,
			pageSize:20,
			pageList:[10,20,30,50],
			url:'../easyUI/queryJsonData',
			method:'get',
			onBeforeEdit:myBeforeEdit,
			onAfterEdit:myAfterEdit,
			onCancelEdit:myCancelEdit,
			toolbar:'#tb'"> <!-- toolbar:mytoolbar 读全局变量  mytoolbar ,toolbar:'#tb' 引用 DIV   -->
	<thead>
		<tr>
			<th data-options="checkbox:true"></th> <!-- 选择的复选框 -->
			<th field="id" width="80">ID</th> <!-- field的值是JSON对象的属性名 -->
			<th field="username" width="100" data-options="editor:{type:'validatebox',options:{required:true}}">用户名</th>
			<th field="language" width="100" data-options="editor:{
						type:'combobox',
						options:{
							valueField:'langValue',
							textField:'langLabel',
							data:myLanguages,
							required:true
						}
					}">用语言</th>
			<th field="salary" width="80"  data-options="editor:{type:'numberbox',options:{precision:1,required:true}}">工资</th> <!--editor:'numberbox'  -->
			<th field="isMan" width="80" data-options="formatter:myGenderFormatter,editor:{type:'checkbox',options:{on:'true',off:'false'}}">是否为男</th>
			<th field="birthday" width="90"  data-options="editor:{type:'datebox',options:{required:true}}">生日</th>
			<th field="comment" width="120"  data-options="editor:'textarea'">comment</th>
			<th field="action"  width="120" data-options="formatter:myActionFormatter" >操作</th>
		</tr>
	</thead>
</table>
Selection Mode:
<select onchange="$('#dg').datagrid({singleSelect:(this.value==0)})">
	<option value="0">Single Row</option>
	<option value="1">Multiple Rows</option>
</select><br/>
SelectOnCheck: <input type="checkbox" checked onchange="$('#dg').datagrid({selectOnCheck:$(this).is(':checked')})"><br/>
CheckOnSelect: <input type="checkbox" checked onchange="$('#dg').datagrid({checkOnSelect:$(this).is(':checked')})">

--服务代码
String my_custome_param=request.getParameter("my_custome_param");

int pageNO=Integer.parseInt(request.getParameter("page"));
int pageSize=Integer.parseInt(request.getParameter("rows"));
long start=(pageNO-1)*pageSize+1;//pageNO*pageSize-pageSize+1
long end=pageNO*pageSize;
//DataGrid返回JSON格式
{"total":"28","rows":[  //固定
	{"id":101}
]}
--
JSONObject obj = new JSONObject();
obj.put("statusCode", 200);
obj.put("statusMessage","成功");

if("save".equals(command))
{
	long newId=500+(long)(400*Math.random());//500-900
	obj.put("additionObject",newId);
}
response.getWriter().write(obj.toString());
=================================================eastUI Extension
-----detailView
<script type="text/javascript" src="datagrid-detailview.js"></script>

$(function(){
$('#dg').datagrid({
	view: detailview,
	detailFormatter:function(index,row){
		return '<div class="ddv"></div>';
	},
	onExpandRow: function(index,row){
		var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');//getRowDetail扩展方法
		ddv.panel({
			border:false,
			cache:true,
			href:'dataGrid_rowEditDetail.jsp?index='+index,
			onLoad:function(){
				$('#dg').datagrid('fixDetailRowHeight',index);//fixDetailRowHeight扩展方法
				$('#dg').datagrid('selectRow',index);
				$('#dg').datagrid('getRowDetail',index).find('form').form('load',row);
			}
		});
		$('#dg').datagrid('fixDetailRowHeight',index);
	}
});
});
		
		
//form中的保存时调用的方法
function saveItem(index){
	var row = $('#dg').datagrid('getRows')[index];
	var url = row.isNewRecord ? webRoot+'/easyUI/updateData?command=extensionSaveNew' :  webRoot+'/easyUI/updateData?command=extensionSaveEdit&id='+row.id;
	$('#dg').datagrid('getRowDetail',index).find('form').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(data){
			data = eval('('+data+')');
			data.isNewRecord = false;
			$('#dg').datagrid('collapseRow',index);
			$('#dg').datagrid('updateRow',{
				index: index,
				row: data
			});
		}
	});
}


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

