
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
 