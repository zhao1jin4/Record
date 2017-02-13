//------Google Map
android-sdk-windows\add-ons\addon-google_apis-google-16

https://developers.google.com/maps/documentation/android/hello-mapview?hl=zh-CN
 
用户主目录/.android/debug.keystore 是用于开发使用自动生成的
keytool -list -v -keystore  debug.keystore  -storepass  android  查看信息MD5的值,复制到
https://developers.google.com/android/maps-api-signup?hl=zh-CN 生成API key

<uses-permission android:name="android.permission.INTERNET"/>
<application 
   <uses-library android:name="com.google.android.maps" />


<com.google.android.maps.MapView
 	    android:layout_width="fill_parent" 
 	    android:layout_height="fill_parent"
 	    android:enabled="true"
 		android:clickable="true"
 		android:apiKey="@string/map_api_key"
 	    />
extends MapActivity

Overlay 抽象类,是透明的图层
ItemizedOverlay 是Overlay的子类,可以放很多的 OverlayItem 标记
MapView 有getOverlayes 把自己的加进去

MapView mapView = (MapView) findViewById(R.id.mapView);
mapView.setBuiltInZoomControls(true);//使用缩放工具
projection =mapView.getProjection();

List <Overlay> allOverlay=mapView.getOverlays();
allOverlay.add(new PointOverlay(begin));//把自己的加进去

MapController controller=mapView.getController();
controller.animateTo(begin);
controller.setZoom(12);


PointOverlay extends  Overlay
{
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		projection.toPixels(geoPoint, point);//把GeoPoint纬经度 转换为屏幕坐标	
		//画已有图
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Paint paint=new Paint();
		canvas.drawBitmap(bitmap , point.x , point.y , paint);
		
		//画线
		Paint paint=new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);
		Path path=new Path();
		path.moveTo(begin.x,begin.y);
		path.lineTo(end.x,end.y);
		canvas.drawPath(path, paint);
	}
	
}

//------OAuth 
signpost-commonshttp4-1.2.1.2.jar
signpost-core-1.2.1.2.jar
http://www.oauth.net  基于HTTP协议
2.0还是Draft阶段

安全认证协议,防止用户输入的用户名密码,被开发者盗用

1.Service Provider 
2.User 用户
3.Consumer 是程序
4.Protected Resources
流程 User ->使用应用程序 Consumer->请求Service Provider取私人信息->问User是否可以让Consumer仿问你的资源

像是网上购买DELL电脑,DELL不能得到我的银行密码,

Service Provider要提供下列三种URL
1.Request Token URL
2.User Authorization URL
3.Acces Token URL

Signpost　实现，官方说明，在Android中使用时，要使用CommonsHttpOAuth* 代替　DefaultOAuth*

OAuthConsumer consumer;
OAuthProvider provider;
consumer= new CommonsHttpOAuthConsumer("key","secret");
provider= new CommonsHttpOAuthProvider("requestURL","accesURL","authorizeURL");

String url=provider.retrieveRequestToken(consumer, "myproto://good");
//把RequestToken写入consumer,返回accessToken的URL
//回调对应Android的Activity的URL，是在用户输入用户名，密码后发启动哪个Activity
//Signpost即会调　intent.setData(Uri.parse("myproto://good"));

String reqToken= consumer.getToken();//返回RequestToken
System.out.println(reqToken);
Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));//启动流览器
intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| //如果Activity已经在History stack 的顶部,那么不启动Activity
				Intent.FLAG_ACTIVITY_NO_HISTORY);//新的Activity不保存在History stack中
this.startActivity(intent);
			
<activity
	android:name=".MyActivity"
	android:label="@string/title_activity_main" 
	android:launchMode="singleTask"> <!-- 表示是单例类 ,不会调用onCreate方法,还是调用OnNewIntent方法--> 　
	<intent-filter>
		<action android:name="android.intent.action.VIEW" /> <!-- 对应new Intent(Intent.ACTION_VIEW,...) -->
		 <category android:name="android.intent.category.DEFAULT" />
		 <category android:name="android.intent.category.BROWSABLE" />
		 <data  android:scheme="myproto" android:host="good"/>
	</intent-filter>
</activity>
	
protected void onNewIntent(Intent intent) //对应launchMode="singleTask" 
{
	super.onNewIntent(intent);
	Uri uri=intent.getData();
	String oauth_verifier=uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
	provider.retrieveAccessToken(consumer, oauth_verifier);//发送Access请求
	String accessToken=consumer.getToken();
	String accessTokenSecret=consumer.getTokenSecret();
}
	

//------OAuth 2.0
resource owner  最终用户
resource server	 是API服务器 使用access token,返回保护的资源
client			应用
authorization server 保存用户密码的服务器

//------Ksoap 2
//测试 http://localhost:8080/J_CXF_Spring/ws/HelloWorld?wsdl
public     String URL="http://10.103.35.146:8080/J_CXF_Spring/ws/HelloWorld";//不能使用127.0.0.和localhost
public     String ACTION="";//看生成的WSDL <soap:operation soapAction="" 
public static final String NAMESPACE="http://server.spring.cxf.zhaojin.org/";
public static final String METHOD="sayHi";

 Button btn= null;
 TextView txt=null;
 String  resultTxt;
@Override
public void onCreate(Bundle savedInstanceState) 
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.ksoap2);
  
	txt=(TextView)this.findViewById(R.id.werserviceTxt);
	btn= (Button)this.findViewById(R.id.werserviceBtn);
   
	btn.setOnClickListener(new Button.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			final CountDownLatch latch=new CountDownLatch(1);
			Runnable run=new Runnable()
				{
					@Override
					public void run()//网络操作不能在主线程中,报错
					{
						SoapObject obj=new SoapObject(NAMESPACE,METHOD);
						obj.addProperty("arg0", "来自 Ksoap2");//方法参数名CXF如不设置,是arg0 , 中文
						
						SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);//CXF应该是1.1版本
						envelope.bodyOut=obj;
						envelope.setOutputSoapObject(obj);//和上面一样吧
						envelope.dotNet=false;//不是C# .Net
						
						HttpTransportSE transport=new HttpTransportSE(URL);
						transport.debug=true;
						try {
							transport.call(ACTION, envelope);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						}
						SoapObject result=(SoapObject)envelope.bodyIn;//得到返回结果
						resultTxt=result.getProperty(0).toString();
						
						//txt.setText(result.getProperty(0).toString());//在子线程中不能仿问
						//Toast.makeText(KSoap2MainActivity.this,"返回:"+result.getProperty(0), Toast.LENGTH_LONG);//在子线程中不能仿问
						
						latch.countDown();
					}
				};
			Thread t=new Thread(run);
			t.start();
		
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			txt.setText(resultTxt);
		}
	});
}


//===============Cordova  Anroid 访问真机

设置PATH 环境变量为android-sdk-windows\tools 目录(有android命令) 和 android-sdk-windows\platform-tools目录
设置ANT_HOME,PATH
有一个cordova-android-master\VERSION文件看版本
https://www.apache.org/dist/cordova/ 下载zip
---3.5
$cd  cordova-android-3.5.0\cordova-android\framework
$android update project -p . -t android-19    #android-19是android list targets看到的
$ant jar			#当前目录生成 cordova-3.5.0.jar

------------离线 
 cordova-android-3.5.0\cordova-android\bin\create.bat  D:/A_Cordova_35 org.zhaojin.cordova35 A_Cordova_35


-------------在线
要下载安装node.js的msi安装包, 运行 npm install -g cordova 在线安装
安装到 C:\Users\zhaojin\AppData\Roaming\npm\node_modules\cordova
安装后就可用cordova 命令

cordova create D:/Program/eclipse_android_workspace/A_Cordova_35 org.zhaojin.cordova35 A_Cordova_35  要下载 
cd D:/Program/eclipse_android_workspace/A_Cordova_35
cordova platform add android		##只platforms\android 目录有用
#cordova platform rm android
#cordova platform add ios
#cordova platform add windows8
#cordova platform add ubuntu
#cordova platform add osx


#cordova build 		生成gen,bin,ant-gen,ant-build目录 
			会把 A_Cordova_35\www 覆盖到 A_Cordova_35\platforms\android\assets\www
			会把 A_Cordova_35\plugins 覆盖到 A_Cordova_35\platforms\android\assets\www\plugins
			会把 A_Cordova_35\config.xml 覆盖到 A_Cordova_35\platforms\android\res\xml\config.xml

#cordova build ios  只build指定平台
#cordova build windows8
#cordova build ubuntu

#cordova emulate android 启动模拟器

#cordova run android 运行 使用 \platforms\android\ant-build\A_Cordova35-debug-unaligned.apk
#cordova run ubuntu  只运行指定平台


导入 eclipse->import 要使用 android/exist -> platforms\android 目录,
可以把A_Cordova_35_CordovaLib项目删,即目录CordovaLib ,也可删cordova目录(有命令), 在libs中增加cordova-3.5.0.jar(如使用cordova run android 会自动build,不要放jar)

project->clean...
 
以下两相bin目录不要删
platforms\android\cordova\node_modules\shelljs\bin
platforms\android\cordova\node_modules\.bin

---用CLI 在线 管理插件
cd D:/Program/eclipse_android_workspace/A_Cordova_35

cordova plugin add org.apache.cordova.camera
cordova plugin add org.apache.cordova.device 
cordova plugin add org.apache.cordova.device-motion
cordova plugin add org.apache.cordova.device-orientation
cordova plugin add org.apache.cordova.geolocation
cordova plugin add org.apache.cordova.globalization
cordova plugin add org.apache.cordova.battery-status
cordova plugin add org.apache.cordova.contacts
cordova plugin add org.apache.cordova.dialogs
cordova plugin add org.apache.cordova.file
cordova plugin add org.apache.cordova.file-transfer
cordova plugin add org.apache.cordova.inappbrowser
cordova plugin add org.apache.cordova.media
cordova plugin add org.apache.cordova.media-capture
cordova plugin add org.apache.cordova.network-information
cordova plugin add org.apache.cordova.splashscreen
cordova plugin add org.apache.cordova.vibration

---  cordova plugin add org.apache.cordova.camera  要在线下载到plugins 和 platforms\android\assets\www\plugins目录中
cordova plugin ls  查看已安装的插件
cordova plugin rm org.apache.cordova.camera   删插件

A_Cordova_35\plugins\android.json中 config_munge 中有增加<feature>为工具使用的,不重要

res/xml/config.xml 新增加
<feature name="Camera">
    <param name="android-package" value="org.apache.cordova.camera.CameraLauncher" />
</feature>

多了org.apache.cordova.camera包,有CameraLauncher 类

AndroidManifest.xml 新增加
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


platforms\android\assets\www\ 下多了 plugins\org.apache.cordova.camera\www\ (eclise中看不到的)
		Camera.js,CameraConstants.js,CameraPopoverHandle.js,CameraPopoverOptions.js
--platforms\android\assets\www\cordova_plugins.js  文件中
cordova.define('cordova/plugin_list', function(require, exports, module) {
	module.exports = [ 下新增
		{
			"file": "plugins/org.apache.cordova.camera/www/CameraConstants.js",
			"id": "org.apache.cordova.camera.Camera",
			"clobbers": [
				"Camera"
			]
		},
		{
			"file": "plugins/org.apache.cordova.camera/www/CameraPopoverOptions.js",
			"id": "org.apache.cordova.camera.CameraPopoverOptions",
			"clobbers": [
				"CameraPopoverOptions"
			]
		},
		{
			"file": "plugins/org.apache.cordova.camera/www/Camera.js",
			"id": "org.apache.cordova.camera.camera",
			"clobbers": [
				"navigator.camera"
			]
		},
		{
			"file": "plugins/org.apache.cordova.camera/www/CameraPopoverHandle.js",
			"id": "org.apache.cordova.camera.CameraPopoverHandle",
			"clobbers": [
				"CameraPopoverHandle"
			]
		}
	];
	module.exports.metadata = { 下新增
	 "org.apache.cordova.camera": "0.3.0"
	}
});
---开发
function getPhoto(source) {
	navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50, 
								destinationType: destinationType.FILE_URI,
								sourceType: source });
}
 function onPhotoURISuccess(imageURI) {
	var largeImage = document.getElementById('largeImage');
	largeImage.style.display = 'block';
    largeImage.src = imageURI;
}
function onFail(message) {
	alert('Failed because: ' + message);
}
android模拟器第一次安装可以选择 ,两个是一样的
<button onclick="getPhoto(pictureSource.PHOTOLIBRARY);">From Photo Library</button><br>
<button onclick="getPhoto(pictureSource.SAVEDPHOTOALBUM);">From Photo Album</button><br> 
<img style="display:none;" id="largeImage" src="" />

--android 真机
function capturePhoto() {
	navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 50,
								destinationType: destinationType.DATA_URL });
}

function capturePhotoEdit() {
	navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 20, allowEdit: true,
							destinationType: destinationType.DATA_URL });
}
function onPhotoDataSuccess(imageData) {
	var smallImage = document.getElementById('smallImage');
	smallImage.style.display = 'block';
	smallImage.src = "data:image/jpeg;base64," + imageData;
}
<button onclick="capturePhoto();">Capture Photo</button> <br>
<button onclick="capturePhotoEdit();">Capture Editable Photo</button> <br>
<img style="display:none;width:60px;height:60px;" id="smallImage" src="" />
 
---cordova plugin add org.apache.cordova.device 
config.xml 新增加
<feature name="Device">
	<param name="android-package" value="org.apache.cordova.device.Device" />
</feature>
多了org.apache.cordova.camera包,有Device类

platforms\android\assets\www\ 下多了 plugins\org.apache.cordova.device\www\device.js (eclise中看不到的)
platforms\android\assets\www\cordova_plugins.js 中 module.exports = [ 中增加
    {
        "file": "plugins/org.apache.cordova.device/www/device.js",
        "id": "org.apache.cordova.device.device",
        "clobbers": [
            "device"
        ]
    }
	module.exports.metadata = { 中增加
	 "org.apache.cordova.device": "0.2.10"
	}

-开发,android模拟器OK
function onDeviceReady() {
	var element = document.getElementById('deviceProperties');

	element.innerHTML = 'Device Name: '     + device.name     + '<br />' +  
						'Device Cordova: '  + device.cordova + '<br />' +   
						'Device Platform: ' + device.platform + '<br />' + 
						'Device UUID: '     + device.uuid     + '<br />' +  
						'Device Model: '    + device.model     + '<br />' +  
						'Device Version: '  + device.version  + '<br />';   
}
<p id="deviceProperties">Loading device properties...</p>

---	cordova plugin add org.apache.cordova.device-motion
	navigator.compass.getCurrentHeading(compassSuccess, compassError);
	
--- cordova plugin add org.apache.cordova.device-orientation
 function onSuccess(acceleration) {
        alert('Acceleration X: ' + acceleration.x + '\n' +
              'Acceleration Y: ' + acceleration.y + '\n' +
              'Acceleration Z: ' + acceleration.z + '\n' +
              'Timestamp: '      + acceleration.timestamp + '\n');
	}
	 function onError() {
		alert('onError!');
	}
	 navigator.accelerometer.getCurrentAcceleration(onSuccess, onError);

	//---连继
	var watchID = null;
	function startWatch() {
		var options = { frequency: 3000 };
		watchID = navigator.accelerometer.watchAcceleration(onSuccess, onError, options);
	}
     function stopWatch() {
        if (watchID) {
            navigator.accelerometer.clearWatch(watchID);
            watchID = null;
        }
    }
   function onSuccess(acceleration) {
        var element = document.getElementById('accelerometer');
        element.innerHTML = 'Acceleration X: ' + acceleration.x + '<br />' +
                            'Acceleration Y: ' + acceleration.y + '<br />' +
                            'Acceleration Z: ' + acceleration.z + '<br />' +
                            'Timestamp: '      + acceleration.timestamp + '<br />';
    }
	
	
--- cordova plugin add org.apache.cordova.geolocation
--- cordova plugin add org.apache.cordova.globalization
 navigator.globalization.getPreferredLanguage(
    	    	    function (language) {alert('language: ' + language.value + '\n');},
    	    	    function () {alert('Error getting language\n');}
    	    	);//zh-CN
				
	  navigator.globalization.getDatePattern(
	        function (date) { alert('pattern: ' + date.pattern + '\n'); },
	        function () { alert('Error getting pattern\n'); },
	        { formatLength: 'short', selector: 'date and time' }
	    );//short 是 yyyy-M-d HH:mm
		
	//用 getDatePattern
	 navigator.globalization.stringToDate(
			    '2014-12-24',
			    function (date) {alert('month:' + date.month +
			                           ' day:'  + date.day   +
			                           ' year:' + date.year  + '\n');},
			    function () {alert('Error getting date\n');},
			    {selector: 'date'}
			);
			
--- cordova plugin add org.apache.cordova.battery-status
--- cordova plugin add org.apache.cordova.contacts
--- cordova plugin add org.apache.cordova.dialogs
	function onPrompt(results) {
		alert("You selected button number " + results.buttonIndex + " and entered " + results.input1);
	}
 navigator.notification.prompt(
    		        'Please enter your name',  // message
    		        onPrompt,                  // callback to invoke
    		        'Registration',            // title
    		        ['Ok','Exit'],             // buttonLabels
    		        'Jane Doe'                 // defaultText
    		    );
 navigator.notification.alert(
    	    	    'You are the winner!',  // message
    	    	    alertDismissed,         // callback
    	    	    'Game Over',            // title
    	    	    'Done'                  // buttonName
    	    	);
				
--- cordova plugin add org.apache.cordova.file
	function showAndroidVar()
   {
    	//ios and android
    	out("applicationDirectory="+  cordova.file.applicationDirectory);
    	out("applicationStorageDirectory="+  cordova.file.applicationStorageDirectory);
    	out("dataDirectory="+  cordova.file.dataDirectory);
    	
    	//only android
    	out( "externalApplicationStorageDirectory="+ cordova.file.externalApplicationStorageDirectory) 
	 	out( "externalDataDirectory="+  cordova.file.externalDataDirectory );
		out( "externalCacheDirectory="+  cordova.file.externalCacheDirectory );
   }
   
   
--- cordova plugin add org.apache.cordova.file-transfer
	 function fileUploadAndroidiOS()
    {
    	var uri = encodeURI("http://"+localIP+":8080/J_JavaEE/upload");
    	var fileURL="storage/sdcard0/e2fsck_log/Info.txt";
    	
    	var options = new FileUploadOptions();
    	options.fileKey="file";
    	options.fileName=fileURL.substr(fileURL.lastIndexOf('/')+1);
    	options.mimeType="text/plain";

    	var headers={'headerParam':'headerValue'};

    	options.headers = headers;

    	var ft = new FileTransfer();
    	ft.onprogress = function(progressEvent) {
    	    if (progressEvent.lengthComputable) {
    	      loadingStatus.setPercentage(progressEvent.loaded / progressEvent.total);
    	    } else {
    	      loadingStatus.increment();
    	    }
    	};
    	ft.upload(fileURL, uri, win, fail, options);
    }
	
    function fileUpload()
    {
    	var uri = encodeURI("http://"+localIP+":8080/J_JavaEE/upload");
    	var fileURL="/storage/sdcard0/e2fsck_log/Info.txt";
    	
    	var options = new FileUploadOptions();
    	options.fileKey = "file";
    	options.fileName = fileURL.substr(fileURL.lastIndexOf('/') + 1);
    	options.mimeType = "text/plain";

    	var params = {};
    	params.value1 = "test";
    	params.value2 = "param";

    	options.params = params;

    	var ft = new FileTransfer();
    	ft.upload(fileURL,uri, win, fail, options);
    }
    function download()
    {
    	var fileURL="/storage/sdcard0/downloadFromJ_JavaEE.txt";
    	var fileTransfer = new FileTransfer();
    	var uri = encodeURI("http://"+localIP+":8080/J_JavaEE/download");

    	fileTransfer.download(
    	    uri,
    	    fileURL,
    	    function(entry) {
    	        out("download complete: " + entry.toURL());
    	    },
    	    function(error) {
    	    	out("download error source " + error.source);
    	    	out("download error target " + error.target);
    	    	out("upload error code" + error.code);
    	    },
    	    false,
    	    {
    	        headers: {
    	           // "Authorization": "Basic dGVzdHVzZXJuYW1lOnRlc3RwYXNzd29yZA=="
    	        }
    	    }
    	);
    }
	
--- cordova plugin add org.apache.cordova.inappbrowser
	var ref = window.open('http://apache.org', '_blank', 'location=yes');//全屏新的
    ref.addEventListener('loadstart', function(event) { alert(event.url); });//点新的链接时(不包含图片,JS,CSS)
--- cordova plugin add org.apache.cordova.media
	 function myErr(err)  //可以查看源码JS文件
    {
        console.log("recordAudio():Audio Error: "+ err.code);
		if(err.code==MediaError.MEDIA_ERR_ABORTED) //1
			alert('MEDIA_ERR_ABORTED');
		else if(err.code==MediaError.MEDIA_ERR_NETWORK)//2
			alert('MEDIA_ERR_NETWORK');
		else if(err.code==MediaError.MEDIA_ERR_DECODE)//3
			alert('MEDIA_ERR_DECODE');
		else if(err.code==MediaError.MEDIA_ERR_NONE_SUPPORTED)//4
			alert('MEDIA_ERR_NONE_SUPPORTED');
    }
    function playAudio() {
    	var url = "myrecording.mp3";
        var my_media = new Media(url, function () { console.log("playAudio():Audio Success"); },myErr  );
        my_media.play();
        setTimeout(function () {
            media.pause();
        }, 10000);
    }
    function recordAudio() {
        var src = "myrecording.mp3";
        var mediaRec = new Media(src, //就是默认目录下,android是 /storage/sdcard0下 ,如已存在会覆盖
            function() {  console.log("recordAudio():Audio Success");//I级别
            		},myErr );
        mediaRec.startRecord();
        setTimeout(function() {
            mediaRec.stopRecord();
        }, 10000);
    }
--- cordova plugin add org.apache.cordova.media-capture
	 navigator.device.capture.captureVideo(captureSuccess, captureError, {limit: 2});
	//---
	var captureSuccess = function(mediaFiles) {
    	    var i, path, len;
    	    for (i = 0, len = mediaFiles.length; i < len; i += 1) {
    	        path = mediaFiles[i].fullPath;
    	        alert("captureSound file in "+path);
    	    }
    	};
	var captureError = function(error) {
		navigator.notification.alert('Error code: ' + error.code, null, 'Capture Error');
	};
	navigator.device.capture.captureAudio(captureSuccess, captureError, {limit:2});//打开android自已的工具
		
--- cordova plugin add org.apache.cordova.network-information
 document.addEventListener("offline", onOffline, false);//当3G,或者wifi网络打开,或者关闭时
    function onOffline() {
    	alert("offline");
    }
    document.addEventListener("online", onOnline, false);
    function onOnline() {
    	alert("online");
    }
    function checkConnection() {
        var networkState = navigator.connection.type;
        var states = {};
        states[Connection.UNKNOWN]  = 'Unknown connection';
        states[Connection.ETHERNET] = 'Ethernet connection';
        states[Connection.WIFI]     = 'WiFi connection';
        states[Connection.CELL_2G]  = 'Cell 2G connection';
        states[Connection.CELL_3G]  = 'Cell 3G connection';
        states[Connection.CELL_4G]  = 'Cell 4G connection';
        states[Connection.CELL]     = 'Cell generic connection';
        states[Connection.NONE]     = 'No network connection';
        alert('Connection type: ' + states[networkState]);
    }
	
--- cordova plugin add org.apache.cordova.splashscreen
android系统在config.xml中加
<!--  位于  res/drawable*/devices.png   -->
<preference name="SplashScreen" value="devices" />
<preference name="SplashScreenDelay" value="3000" />

--- cordova plugin add org.apache.cordova.vibration
navigator.notification.vibrate(1500);
//wait时间,vibrate时间,.....
navigator.notification.vibrateWithPattern([100, 500,1000, 2000] , 3);//重复3次
navigator.notification.cancelVibration();

----管理插件使用plugman
npm install -g plugman   必须有 git  命令
plugman --platform android --project D:\Program\eclipse_android_workspace\A_Cordova_3 --plugin <name|url|path> 

	
------------------插件开发
res/xml/config.xml中加
<feature name="MyPlugin">
	<param name="android-package" value="org.zhaojin.cordova3.MyPlugin" />
	<param name="onload" value="true" />
</feature>
//---Adnroid本地
package org.zhaojin.cordova3;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class MyPlugin extends CordovaPlugin 
{
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		//初始化
	}
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
	    if ("echo".equals(action)) {
	    	String message = args.getString(0); 
			//JS并不在UI线程,而是在WebCore线程
	    	/* cordova.getActivity().runOnUiThread(new Runnable() {//与UI交互
	              public void run() {
	            	  //...
	                  callbackContext.success(); // Thread-safe.
	              }
	          });
	    	  cordova.getThreadPool().execute(new Runnable() {//开新的线程,而不阻塞WebCore线程
	              public void run() {
	                 // ...
	                  callbackContext.success(); // Thread-safe.
	              }
	          });
	    	  */
	    	this.echo(message, callbackContext);
	        //callbackContext.success();
	        return true;
	    }
	    return false; //返回 false 失败,方法找不到.
	}
	//私有的
	private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) { 
            callbackContext.success("hello "+message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
 window.echo = function(str, callback) 
	{
	/*
			格式第一个参数是执行成功调用的函数
			第二个参数执行失败调用的函数
			第三个本地类,与<feature name="MyPlugin">的值匹配
			第四个本地方法
			第五个本地方法的参数
	*/
		cordova.exec(callback, function(err){ callback('Nothing to echo.'); },
					"MyPlugin", "echo", [str] );
	};
			
function myplugin()
{
	window.echo("lisi",function(res){
				alert("result is:"+res);
				});
}
 <button onclick="myplugin()">invoke my plugin</button>
 
 
 
---2.7-dev
在cordova-android-master/framework/中建立libs目录并放入 commons-codec-1.8.jar,修改create脚本中commons-codec为1.8

$cd  /cordova-android-master/framework
$android update project -p . -t android-17    #android-17是android list targets看到的
$ant jar			#当前目录生成cordova-dev.jar

$cd /cordova-android-master   
$./bin/create D:/Program/eclipse_android_workspace/A_Cordova org.zhaojin A_Cordova   
加了libs/cordova-dev.jar,xml/config.xml,assets/www

---
自己新建一个空android项目test把.project和.class文件复制到A_Cordova目录中,修改.project文件中项目名字,就可用eclipse->import
项目属性中使用android Api,不要使用google API

config.xml中配置开始页  <content src="index.html" /> 也可http://host
<access origin="*" /> 配置可仿问的外部URL,外部域
//---Android Plugin



//===============Cordova  iOS 访问真机
Cordova-3.5 要Xcode-5.x

1.建立项目使用 create命令  空目录 包名 项目名
Cordova-2.7/iOS/cordova-ios-master/bin/create ~/Program/Xcode-4.5_workspace/iOS_Cordova org.zhaojin iOS_Cordova

加入了子项目(CordovaLib),引入了很多的framework,config.xml,www目录

MainViewController.m中的webViewDidFinishLoad方法是UIWebViewDelegate的实现


//---iOS Plugin
 window.echo = function(str, callback) {
            /*
            格式第一个参数是执行成功调用的函数
            第二个参数执行失败调用的函数
            第三个本地类,与<plugin name=""的值匹配
            第四个本地方法
            第五个本地方法的参数
            */
            cordova.exec(callback, function(err) {
                 callback('Nothing to echo.');
                 }, "Service", "echo", [str]);
            };

            //------Objective-C本地
            /*
             config.xml中增加 <plugin name="Service" value="ZHServicePlugin" />
             只一个实例,生命周期与UIWebView同,在第一次调用时实例化,如加onload="true"启动就初始化
             
             #import <Cordova/CDV.h>
            ZHServicePlugin: CDVPlugin,也可覆盖其它方法,如 pause, resume, 

             - (void)echo:(CDVInvokedUrlCommand*)command //方法签名
             {
                 CDVPluginResult* pluginResult = nil;
                 NSString* myarg = [command.arguments objectAtIndex:0];
                 
                 if (myarg != nil) 
                {
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
                    //静态方法,也可带参数messageAsXxx,String, Int, Double, Bool, Array, Dictionary, ArrayBuffer, and Multipart
                    //messageAsArrayBuffer要使用NSData* 做参数,JS端也是NSData*
                    //messageAsMultipart要使用NSArray* 做参数,
                } else {
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Arg was null"];
                }
                 [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                //commandDelegate 在CDVPlugin.h中,sendPluginResult方法是线程安全的
             }

             */

//---------------Baidu Map
要生成keystore并保存下来
keytool -genkey -alias lisi      -keystore C:/temp/clientKeystore  -dname "CN=lisi,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN"     -keypass lisikeypass     --storepass clientkeystorepass
keytool -list -v -keystore C:/temp/clientKeystore -storepass clientkeystorepass 来查看  SHA1的值  C9:68:E9:39:60:40:B5:43:03:F4:A5:4A:66:8B:69:3E:59:55:85:3D

示例的包名要固定 org.zhaojin.baiduMap

public static final String strKey = "";

申请key失败???
 

//--------------zxing
mvn install 生成 core-3.0.0-SNAPSHOT.jar 

//---android 生成二维码(QR)
final int BLACK = 0xFF000000;
final int WHITE = 0xFFFFFFFF; 
int size=350;
String contentString="http://www.baidu.com";
Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();    
hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   
BitMatrix matrix = new MultiFormatWriter().encode(contentString, BarcodeFormat.QR_CODE, size, size,hints);  
int width = matrix.getWidth();  
int height = matrix.getHeight();  
int[] pixels = new int[width * height];  
  
for (int y = 0; y < height; y++) 
{  
	for (int x = 0; x < width; x++) 
	{  
		if (matrix.get(x, y))  
			pixels[y * width + x] = BLACK;  
		else
			pixels[y * width + x] = WHITE; 
	}  
}    
 //android 没有 java.awt.image.BufferedImage 要使用  android.graphics.Bitmap
Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);  
bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//(int[] pixels, int offset, int stride, int x, int y, int width, int height) 
qrImageView.setImageBitmap(bitmap);

//---写手机上
if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
{
	File filename= new File(Environment.getExternalStorageDirectory(), "qrCode.png");
	try {
		FileOutputStream outStream=new FileOutputStream(filename);
		//FileOutputStream outStream = GenerateQRCodeActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);//MODE_APPEND,MODE_WORLD_READABLE,MODE_WORLD_WRITEABLE
		bitmap.compress(CompressFormat.PNG, 80, outStream);
		outStream.flush();
		outStream.close();
	} catch (Exception e) {
		Toast.makeText(GenerateQRCodeActivity.this, "错误发生,原因为:"+e.getMessage(), Toast.LENGTH_LONG).show();
		return ;
	}
	Toast.makeText(GenerateQRCodeActivity.this, "二维码图片已保存到:"+filename.getAbsolutePath(), Toast.LENGTH_LONG).show();
}
//---android 解析二维码(QR)


//---android 解析条形码



//---android  推送消息

MQTT协议

XMPP协议实现androidpn 

 

