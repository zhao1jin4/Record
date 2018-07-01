
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

