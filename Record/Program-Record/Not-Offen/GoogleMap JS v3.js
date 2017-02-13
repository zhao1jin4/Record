http://ditu.google.cn/  在线

Google Maps JavaScript API V3

V3: The Solution for Maps Applications for both the Desktop and Mobile Devices

http://code.google.com/apis/maps/documentation/javascript/
http://code.google.com/intl/zh-CN/apis/maps/documentation/javascript/

http://code.google.com/intl/zh-CN/apis/maps/documentation/javascript/basics.html	请注意：此版本的 Google Maps Javascript API 不再需要 API 密钥！


检测用户位置

	
 var myOptions = {
    zoom: 6,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);//<div id="map_canvas"></div>

 if(navigator.geolocation)  //新版本的浏览器正开始支持 W3C Geolocation 标准。此标准是 HTML5 的一部分
 {
    browserSupportFlag = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
      map.setCenter(initialLocation);
    }, function() {
      alert('not support');
    });
}

 latitude   纬度
 longitude	经度,
 
 您的应用程序是否在使用传感器（例如 GPS 定位器）确定用户的位置
 <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> <!--不能使用<script/>的简单形式--> sensor=true
 
 iPhone 和 Android 设备,地图的 <div> 设置为具有 100% 的宽度属性和高度属性
  var useragent = navigator.userAgent;
 if (useragent.indexOf('iPhone') != -1 || useragent.indexOf('Android') != -1 ) {
 
 
 <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  全屏模式显示该地图，且用户不能调整地图的大小
 
 <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&language=ja">		language=zh 中文覆盖浏览器的首选语言设置
 
 
 <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&region=GB">	英国&region=GB  ,CN
 
 
 http://maps.google.com/maps/api/js
 加载的其他库，您可以指定 libraries 参数，并向该参数传递相关库的名称。可将多个库指定为以英文逗号分隔的列表
 当前可用的库有：geometry ,adsense ,
 panoramio (将 Panoramio 照片图层添加到 Maps API 应用程序中)

请求 Maps JavaScript API 的 google.maps.geometry 库：
<script type="text/javascript" src="http://maps.google.com/maps/api/js?libraries=geometry&sensor=true_or_false"></script>



HTTPS 
<script src="https://maps-api-ssl.google.com/maps/api/js?v=3&sensor=true_or_false" type="text/javascript"></script>

异步
页面完全加载后再加载 Maps API（使用 window.onload）,传递callback=

纬度会限定在 -90 度和 +90 度之间，而经度会限定在 -180 度和 + 180 度之间
上海人民广场位于北纬31度23分,东经121度47分
北京的经度是东经116°28′纬度是北纬39°48′ 

纬度（正：北纬　负：南纬）latitude  
经度（正：东经　负：西经）longitude  
 

function initialize() {
  var myLatlng = new google.maps.LatLng(31.11, 121.29);//上海 , 以 {纬度, 经度} 的顺序传递
  var myOptions = {
    zoom: 8,//缩放 0 相当于将地球地图缩小到最低程度,可到21
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
}
  
function loadScript() {
  var script = document.createElement("script");
  script.type = "text/javascript";
  script.src = "http://maps.google.com/maps/api/js?sensor=false&callback=initialize";//callback=方法名
  document.body.appendChild(script);
}
window.onload = loadScript;

夜间（开发）版本,用 v=3 或省略 v 参数来指定	包含公开发布的任何错误修复和新增地图项
编号版本，用 v=3.number 表示  http://code.google.com/p/gmaps-api-issues/wiki/JavascriptMapsAPIv3Changelog    有3.7
发行新的编号版本时，将会“冻结”之前的发行版本，这表示我们不再对其进行任何代码更改更新


<!DOCTYPE html> 声明将应用程序声明为 HTML5。


    ROADMAP，用于显示 Google Maps 默认的普通二维图块。
    SATELLITE，用于显示拍摄的图块。
    HYBRID，用于同时显示拍摄的图块和突出特征（道路、城市名）图块层。					(混合物)
    TERRAIN，用于显示自然地形图块，自然地形图块中会显示高度和水体特征（山脉、河流等）。	(地形, 地面, 地域, 地带)


在 google.maps.event 命名空间中注册 addListener() 事件处理程序接收这些事件时执行相应的代码
用户事件（例如鼠标事件或键盘事件）,google.maps.Marker 对象可以侦听以下用户事件
    'click'
    'dblclick'
    'mouseup'
    'mousedown'
    'mouseover'
    'mouseout'

MVC 状态更改,如当地图的缩放级别更改后，API 将会触发地图上的 zoom_changed 事件
var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

google.maps.event.addListener(map, 'zoom_changed', function() 
		{
		    ...
		});

var marker = new google.maps.Marker({
  position: myLatlng, // var myLatlng = new google.maps.LatLng(-25.363882,131.044922);
  map: map,
  title:"Hello World!"
});
google.maps.event.addListener(marker, 'click', function() {
map.setZoom(8);
});

要尝试检测视口中的更改，请务必使用特定的 bounds_changed 事件
只有在系统强制性地更改了视口后，getBounds() 才能报告有用的结果

用户界面事件会传递事件参数,用户界面 'click' 事件通常传递包含 latLng 属性的 MouseEvent
MVC 状态更改不会在它们的事件中传递参数。

 google.maps.event.addListener(map, 'click', function(event) 
	{
		placeMarker(event.latLng);
	});

function placeMarker(location) 
{
  var marker = new google.maps.Marker(
		{
			position: location, 
			map: map
	   });
  map.setCenter(location);
}




var southWest = new google.maps.LatLng(-31.203405,125.244141);
var northEast = new google.maps.LatLng(-25.363882,131.044922);
var bounds = new google.maps.LatLngBounds(southWest,northEast);
map.fitBounds(bounds);
southWest.lng();
southWest.lat();



 var infowindow = new google.maps.InfoWindow(
      { content: "message" //单击marker,弹出消息
		//,position:new google.maps.LatLng(-25.363882,131.044922);
        });
  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(map,marker);
	});


//zoom_changed  事件,MVC 状态更改的某一属性
zoomLevel = map.getZoom();//事件修改属性
infowindow.setContent("Zoom: " + zoomLevel);//可以动态的修改,完成后显示
if (zoomLevel == 0) 
  map.setZoom(10);



<body onload=""> 事件为 window 事件，表明 window 元素下的 DOM 分层已经全部构建并呈现完毕。
HTML与JavaScript 分隔开来
 google.maps.event.addDomListener(window, 'load', initialize);//不写在任何function中,监听 DOM 事件
 
 
 new google.maps.Map(document.getElementById("map_canvas"), 
 {
	disableDefaultUI:true //构造Map的选项有一个 ,disableDefaultUI:true  ,就可以不要默认地图自带的控件,鼠标滑轮还可以缩放
	,panControl:true
	,zoomControl:true
	,mapTypeControl:true
	,scaleControl:true
	,streetViewControl:true
	,overviewMapControl:true
	
  });
 
 
 
 
