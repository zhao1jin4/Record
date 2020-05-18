//------OAuth2
https://developer.android.google.cn/training/id-auth/authenticate#java



//------Ksoap 2
//测试 http://localhost:8080/J_CXF_Spring/ws/HelloWorld?wsdl
public     String URL="http://10.103.35.146:8080/J_CXF_Spring/ws/HelloWorld";//不能使用127.0.0.和localhost
public     String ACTION="";//看生成的WSDL <soap:operation soapAction="" 
public static final String NAMESPACE="http://server.spring.cxf.zh.org/";
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
//===============Cordova  Anroid 访问真机

设置PATH 环境变量为android-sdk-windows\tools 目录(有android命令) 和 android-sdk-windows\platform-tools目录(有adb命令)
设置ANT_HOME,PATH
有一个cordova-android-master\VERSION文件看版本
https://www.apache.org/dist/cordova/ 下载zip

 
---3.5

$cd  cordova-android-3.5.0\cordova-android\framework
$android update project -p . -t android-19    #android-19是看到的
$ant jar			#当前目录生成 cordova-3.5.0.jar


导入 eclipse->import 要使用 android/exist -> platforms\android 目录,
可以把A_Cordova_35_CordovaLib项目删,即目录CordovaLib ,也可删cordova目录(有命令), 在libs中增加cordova-3.5.0.jar(如使用cordova run android 会自动build,不要放jar)

project->clean...

------------离线 
E:\Program\cordova-android-6.3.0\package\bin\create.bat E:/A_Cordova_7 org.zh.cordova7 A_Cordova_7  
生成的带 org\apache\cordova 源码 是AndroidStudio项目， 指定了 Grale 2.3.3 版本 修改本项目及CordovaLib 的target=android-26 为自己的 android 版本, 也编译不了 依赖下载有问题，可能是Gradle版本原因？？

avdmanager.bat list target 看到SDK 版本26
cd E:\Program\cordova-android-6.3.0\package\framework

-------------在线
要下载安装 node-v8.6 的msi安装包, 运行 npm install -g cordova 在线安装
命令安装到 
 %HOMEPATH%\AppData\Roaming\npm\cordova 是PATH位置
 %HOMEPATH%\AppData\Roaming\npm\node_modules\cordova\bin\cordova

 %HOMEPATH%\AppData\Roaming\npm\node_modules\cordova\node_modules 有全部的模块



安装后就可用cordova 命令

cordova create E:/tmp/A_Cordova_7 org.zh.cordova7 A_Cordova_7   

cd E:/tmp/A_Cordova_7
cordova platform add android		##日志显示cordova-android@^6.2.3 和 Android target: android-25 ,这是最新的   
cordova platform add android@^5.0.0    指定SDK版本 

E:\tmp\A_Cordova_7\platforms\android 是项目目录,有org.apache的代码 , 有gradlew.bat , 能用AndroidStudio打开 
 
 指定了 Grale 2.3.3 版本 修改本项目及CordovaLib 的target=android-25 为自己的 android 版本, 也编译不了 依赖下载有问题，可能是Gradle版本原因？？
 如打开项目时自动下载Gradle就可以build,提示升级Grale插件从2.3.3到3.3，如Instanct Run

#cordova platform rm android
#cordova platform add ios
#cordova platform add windows
#cordova platform add ubuntu
#cordova platform add osx

cordova platform ls
cordova requirements  要求SDK Platform  for API level android-25 (7.1.1)

cordova build 		找android命令,gradle 下载很多maven库,会下载Android SDK Platform 25,    生成gen,bin,ant-gen,ant-build目录 
			会把 A_Cordova_35\www 覆盖到 A_Cordova_35\platforms\android\assets\www
			会把 A_Cordova_35\plugins 覆盖到 A_Cordova_35\platforms\android\assets\www\plugins
			会把 A_Cordova_35\config.xml 覆盖到 A_Cordova_35\platforms\android\res\xml\config.xml

#cordova build ios  只build指定平台
#cordova build windows8
#cordova build ubuntu

#cordova emulate android 启动模拟器

#cordova run android 运行 使用 \platforms\android\ant-build\A_Cordova35-debug-unaligned.apk
#cordova run ubuntu  只运行指定平台

 
以下两相bin目录不要删
platforms\android\cordova\node_modules\shelljs\bin
platforms\android\cordova\node_modules\.bin

---用CLI 在线 管理插件
cd D:/Program/eclipse_android_workspace/A_Cordova_35

自带 cordova-plugin-whitelist  插件

cordova plugin search camera 打开网址 http://cordova.apache.org/plugins/?q=camera

cordova plugin add cordova-plugin-camera 
cordova plugin add cordova-plugin-geolocation
cordova plugin add cordova-plugin-globalization
cordova plugin add cordova-plugin-battery-status
cordova plugin add cordova-plugin-contacts
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-file-transfer
cordova plugin add cordova-plugin-inappbrowser
cordova plugin add cordova-plugin-media
cordova plugin add cordova-plugin-media-capture
cordova plugin add cordova-plugin-network-information
cordova plugin add cordova-plugin-splashscreen
cordova plugin add cordova-plugin-vibration
cordova plugin add cordova-plugin-device 


Device Motion  和 Device Orientation  过时使用HTML5
 
<项目目录>\node_modules 目录 , plugins 目录 , platforms\android\assets\www\plugins目录中 有增加
<项目目录>\config.xml			文件中有增加
<项目目录>\platforms\android 是android的工作目录

cordova plugin ls  查看已安装的插件

cordova plugin rm  cordova-plugin-camera    删插件



------- cordova plugin add cordova-plugin-camera  v6.2.3  (7)

res/xml/config.xml 新增加
<feature name="Camera">
	<param name="android-package" value="org.apache.cordova.camera.CameraLauncher" />
</feature>
 
多了org.apache.cordova.camera包,有CameraLauncher 类

AndroidManifest.xml 新增加
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


platforms\android\assets\www\ 下多了 plugins\cordova-plugin-camera\www
		Camera.js,CameraConstants.js,CameraPopoverHandle.js,CameraPopoverOptions.js
--platforms\android\assets\www\cordova_plugins.js  文件中
cordova.define('cordova/plugin_list', function(require, exports, module) {
	module.exports = [ 下新增
		 {
        "id": "cordova-plugin-camera.Camera",
        "file": "plugins/cordova-plugin-camera/www/CameraConstants.js",
        "pluginId": "cordova-plugin-camera",
        "clobbers": [
            "Camera"
        ]
    },
    {
        "id": "cordova-plugin-camera.CameraPopoverOptions",
        "file": "plugins/cordova-plugin-camera/www/CameraPopoverOptions.js",
        "pluginId": "cordova-plugin-camera",
        "clobbers": [
            "CameraPopoverOptions"
        ]
    },
    {
        "id": "cordova-plugin-camera.camera",
        "file": "plugins/cordova-plugin-camera/www/Camera.js",
        "pluginId": "cordova-plugin-camera",
        "clobbers": [
            "navigator.camera"
        ]
    },
    {
        "id": "cordova-plugin-camera.CameraPopoverHandle",
        "file": "plugins/cordova-plugin-camera/www/CameraPopoverHandle.js",
        "pluginId": "cordova-plugin-camera",
        "clobbers": [
            "CameraPopoverHandle"
        ]
    }
	
	];
	module.exports.metadata = { 下新增
		"cordova-plugin-camera": "2.4.1",
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
	
--- cordova plugin add cordova-plugin-splashscreen
android系统在config.xml中加
<!--  位于  res/drawable*/devices.png   -->
<preference name="SplashScreen" value="devices" />
<preference name="SplashScreenDelay" value="3000" />

--- cordova plugin add org.apache.cordova.vibration
navigator.notification.vibrate(1500);
//wait时间,vibrate时间,.....
navigator.notification.vibrateWithPattern([100, 500,1000, 2000] , 3);//重复3次
navigator.notification.cancelVibration();



 
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
 

----管理插件使用plugman
npm install -g plugman   必须有 git  命令
plugman --platform android --project D:\Program\eclipse_android_workspace\A_Cordova_3 --plugin <name|url|path> 

	
------------------插件开发
res/xml/config.xml中加
<feature name="MyPlugin">
	<param name="android-package" value="org.zh.cordova7.MyPlugin" />
	<param name="onload" value="true" />
</feature>
//---Adnroid本地
package org.zh.cordova3;
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
$./bin/create D:/Program/eclipse_android_workspace/A_Cordova org.zh A_Cordova   
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
Cordova-2.7/iOS/cordova-ios-master/bin/create ~/Program/Xcode-4.5_workspace/iOS_Cordova org.zh iOS_Cordova

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

=============React Native
最新版本 0.59.4  要求使用JDK 8(可以OpenJDK 8)

写JS会生成 iOS 和 Android 的本地代码,但各平台还有差异，可以做到热更新(缓存转换的本地程序,只有当服务端更新变化时更新)

live update 热更新服务器 微软的 CodePush（现在叫App Center），React Native中文网的pushy 都是第三方服务器，网络慢，安全性，稳定性等问题，如搭建自己的只能单独开发


注意：不要使用 cnpm！cnpm 安装的模块路径比较奇怪，packager 不能正常识别！
npm install -g react-native-cli
react-native init  A_ReactNative 可加参数 --version X.XX.X
生成文件中android和ios目录 
cd A_ReactNative
有可以连接真机或者虚拟机后
react-native run-ios  (如用Xcode-9.2启动是可以的，cmd+r=reload,cmd+d=dev menu，linux下的vmware不能传送cmd键)
react-native run-android  (如用Android Studio不行的) ，会启动一个node.js服务 会执行 adb reverse tcp:8081 tcp:8081
查询是否启动方法 adb reverse --list

也可使用 expo-cli (和react-native-cli兼容)
	npm install -g expo-cli
	expo init AwesomeProject
	cd AwesomeProject
	npm start  或者 expo start

Python2
如不使用 Android 可使用 Nuclide(facebook 的不能下载) + Atom (可调试)

vi $HOME/.bash_profile (可以没有)
	export ANDROID_HOME=$HOME/Android/Sdk
	export PATH=$PATH:$ANDROID_HOME/emulator
	export PATH=$PATH:$ANDROID_HOME/tools
	export PATH=$PATH:$ANDROID_HOME/tools/bin
	export PATH=$PATH:$ANDROID_HOME/platform-tools


---MainActivity.java
import com.facebook.react.ReactActivity; 
public class MainActivity extends ReactActivity { 
    @Override
    protected String getMainComponentName() {
        return "A_ReactNative";//对应 AppRegistry.registerComponent的第一个参数的名字
    }
}

---MainApplication.java
import android.app.Application;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this)
		{
					@Override
					public boolean getUseDeveloperSupport() {
							return BuildConfig.DEBUG;
					}
	
					@Override
					protected List<ReactPackage> getPackages() {
							return Arrays.<ReactPackage>asList(
											new MainReactPackage()
							);
					}
	
					@Override
					protected String getJSMainModuleName() {
							return "index";
					}
  };
		
 //ReactApplication的方法 
  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
  }
}
<activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />

----app.json
{
  "name": "A_ReactNative",
  "displayName": "A_ReactNative"
}
-----index.js

import {AppRegistry} from 'react-native'; 
import {name as appName} from './app.json';
//import App from './App';
//AppRegistry.registerComponent(appName, () => App);


// import AppFlex from './AppFlex';
// AppRegistry.registerComponent(appName, () => AppFlex);


// import AppView from './AppView';
// AppRegistry.registerComponent(appName, () => AppView); 

// import AppText from './AppText';
// AppRegistry.registerComponent(appName, () => AppText);

// import AppTouchable from './AppTouchable';
// AppRegistry.registerComponent(appName, () => AppTouchable); 
 
//网络图片不行？？
import AppImage from './AppImage';
AppRegistry.registerComponent(appName, () => AppImage);
 
//事件不太对
// import AppScrollView from './AppScrollView';
// AppRegistry.registerComponent(appName, () => AppScrollView);

//
// import AppFlatList from './AppFlatList'; //ListView过时了用 FlatList 或 SectionList
// AppRegistry.registerComponent(appName, () => AppFlatList);

//------未成功
// import AppNavigation from './AppNavigation'; //Navigator已经没了 npm install --save react-navigation   
// AppRegistry.registerComponent(appName, () => AppNavigation);

//也没有TabBarIOS


---App.js
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <Text style={styles.instructions}>{instructions}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
----AppFlex.js

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native'; 
type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <View style={styles.child1}>
        
        </View>  
        <View style={styles.child2}>
        
        </View>
      </View>
    );
  }
}

/*

const styles = StyleSheet.create({
  container: { 
    margin:30,
    width:300,
    height:500,
    backgroundColor:"yellow",
    // flexDirection 默认是竖向column  http://www.runoob.com/css3/css3-flexbox.html
    flexDirection:"row", 
    justifyContent: 'center', //横向
    alignItems: 'center', //竖向 
  },
  child1: {
    width:100,
    height:100, 
    backgroundColor:"green",
  },
  child2: {
    width:100,
    height:100, 
    backgroundColor:"blue",
  },
});
*/

//flex :1 表示子组件要以把父组件填满,如有多个组件flex :1均分,如大于1按总数的百分比
const styles = StyleSheet.create({
  container: { 
    flex :1,
    margin:30, 
    backgroundColor:"yellow", 
  },
  child1: {
    flex :1, //只是高度上点1/3
    backgroundColor:"green",
  },
  child2: {
    flex :2, //只是高度上点2/3
    backgroundColor:"blue",
  },
});

----AppView.js

// view 类似于div  https://reactnative.cn/docs/view/
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native'; 
type Props = {};
export default class App extends Component<Props> {
  render() {
     
    return (
      /*在开始xml前要这样注释
      <Text style={styles.myCenter}>酒店</Text>
      */ 
     
      <View style={[styles.container,styles.myCenter]  /*  多个样式使用数组 ,已经在{}中的注释 */}> 
        <View style={[styles.item,styles.myCenter,styles.myFlex]}>
          <Text >酒店</Text>
        </View>  
       {/* 
       在xml中这样注释
       */ }
        <View style={[styles.item,styles.myFlex,styles.leftRightBorder]}>
          <View style={[styles.myCenter,styles.myFlex,styles.bottomBorder]}>
            <Text >海外酒店</Text>
          </View>  
          <View style={[styles.myFlex,styles.myCenter]}>
            <Text  >物价酒店</Text>
          </View>  
        </View>
        <View style={[styles.item,styles.myFlex]}>
          <View  style={[styles.myCenter,styles.myFlex,styles.bottomBorder]} >
            <Text  >团购</Text>
          </View>  
          <View style={[styles.myCenter,styles.myFlex]} >
            <Text  >客栈</Text>
          </View>  
        </View> 
      </View> 
    );
  }
}
 
const styles = StyleSheet.create({
  container: {  
    marginTop:30, 
    backgroundColor:"grey",  
    flexDirection:"row",
  }, 
  myFlex:{
    flex:1
  },
  myCenter:{
    alignItems: 'center',
    justifyContent: 'center',
  },
  item:{
    backgroundColor:"orange",
    margin:1,
    height:80,
    borderRadius:5,
    flexDirection:"column",
  } ,
  leftRightBorder:{
    borderLeftWidth:1,
    borderRightWidth:1,
    borderColor:"white",
  },bottomBorder:{
    borderBottomWidth:1,
    borderColor:"white",
  } 
});
----AppText.js
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
 
import Header from './AppTextHeader';
import Body from './AppTextBody'; 

type Props = {};
//export default
 class App extends Component<Props> {
  render() { 
      var allNews=[
        "1.这是第一条，这是第一条，这是第一条，这是第一条，这是第一条，这是第一条，这是第一条，这是第一条，这是第一条，这是第一条",
        "2.这是第二条"
      ]
    return (
      <View style={{flex:1}}> 
        <Header></Header> 
        <Body news={allNews}></Body>
        <Text>hello2</Text>
     </View>
    );
  }
}
module.exports=App;
	
----AppTextHeader.js

mport React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native'; 

type Props = {};

//export default
 class Header extends Component<Props> {
  render() {
     
    return (
      <View style={styles.title}>
        <Text style={{fontSize:25,fontWeight:"bold",textAlign:"center"}}>
          <Text style={{color:"blue"}}>网易</Text>
          <Text style={{color:"red",backgroundColor:"yellow"}}>新闻</Text>
          <Text>有态度</Text>
        </Text>
      </View>
    );
  }
}
 
const styles = StyleSheet.create({ 
title:{
  marginTop:25,
  height:40,
  borderBottomWidth:1,
  borderBottomColor:"red",
  alignItems:"center"
} 
});

module.exports=Header;

----AppTextBody.js
 
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native'; 

type Props = {};

/export default
 class Body extends Component<Props> {
  showMsg(msg) {
    alert(msg);
  }
  render() {
    var newsArray=[];
    for(var i in this.props.news)
    {
      var text=(
          //多个Text每个需要唯一的key,给ReactNative用的
          //numberOfLines 显示行数  
          //onPress 手指触摸事件  
          <Text onPress={this.showMsg.bind(this,this.props.news[i])} numberOfLines={2} key={i} 
            style={styles.new_item}> 
              {this.props.news[i]}
          </Text>
        );
        newsArray.push(text);
    }
    return (
      <View style={{flex:1}}> 
          <Text style={styles.new_title}>今日要闻</Text> 
          {newsArray}
      </View>
    );
  }
}
 
const styles = StyleSheet.create({  
 new_title:{
   fontWeight:"bold"
 },
 new_item:{
   fontSize:15,
   lineHeight:30
 }

});

module.exports=Body;

----AppTouchable.js
 
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,TouchableOpacity,TextInput} from 'react-native';

type Props = {};

//export default
 class App extends Component<Props> { 

   //不能在构造器中用setState(), 但可以 this.state = { counter: 0 },只有构造器可这么用,其它地方要用this.setState() 
  componentDidMount( ) 
  {   
    //setState有变化 
    this.setState((state, props) => {
      return {inputStr: ""};
    }); 

  }
  myOnChangeText(text){
    console.log("text="+text);   
    this.setState((state, props) => {//报 this.setState不存的原因是没有bind
      return {inputStr: text};
    }); 
     
  }
  myOnClickBtn(){
    console.log("state="+this.state); 
    alert(this.state.inputStr);
  }
  
  render() { 
     
    return (
     <View style={styles.container}>
       <TextInput style={styles.text} placeholder="请输入" 
           returnKeyType="search"  /* 输入法的回车显示的搜索 */ onChangeText={this.myOnChangeText.bind(this)}></TextInput>
       <TouchableOpacity onPress={this.myOnClickBtn.bind(this)}   /* TouchableOpacity 点击时会有白层在上面的效果,注释在<>中 */> 
         <View style={styles.btn}>
            <Text>搜索</Text>
         </View>
       </TouchableOpacity>
     </View>
    );
  }
}

const styles = StyleSheet.create({
  container:{
    flexDirection:"row",
    height:35, 
  },
  text:{
    borderWidth:1,
    borderColor:"black",
    width:220
  },
  btn:{ 
    width:55, 
    height:35, 
    backgroundColor:"gray",
    justifyContent:"center",
    alignItems:"center"
  }
});

 module.exports=App;

----AppImage.js
 
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,Image} from 'react-native'; 
 
type Props = {};

//export default
 class App extends Component<Props> {   
  /*resizeMode 当值为
      cover,保持宽高比,填满容器，可以超出容器
      contain,保持宽高比,在容器内
      stretch,不保持宽高比,填满容器
  */
  render() { 
      //请求外网的数据，Xcode要修改info.plist 文件 App Transport Security Settings 的默认子项Excption Domains删除，增加子项Allow Arbitrary Loads  设置为YES
     //本地图标Xcode要拖动 images.xcassets中
    return (
      <View style={styles.container}> 
         {/*
								 android自带的  <uses-permission android:name="android.permission.INTERNET" /> 网络图不显示？??
         https://reactjs.org/logo-og.png
         https://m.baidu.com/static/index/plus/plus_logo_web.png
         网络图不行?? Mac iOS模拟器也显示不了
         <View style={styles.img}> 
            <Image source={{uri:"https://reactjs.org/logo-og.png"}}></Image> 
        </View>
         */} 
        <View style={styles.img}>  
            {/* 
            ./f8.png 是在项目根级目录下,下级有android，ios目录,测试可显示
            */}
             <Image source={require("./f8.png")}></Image>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container:{
    flex:1,
    backgroundColor:"pink",
    alignItems: 'center',
    justifyContent: 'center',
  },
  img:{
    width:300,
    height:300,
    backgroundColor:"gray"
  }
});

 module.exports=App;


------AppScrollView.js
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,ScrollView,RefreshControl} from 'react-native';  
 
type Props = {};
 

//export default
 class App extends Component<Props> {    
  render() { 
    /* ScrollView要一个高度 ,也在在父级元素约束
   
    */ 
    return (
      <View style={styles.container}> 
          <ScrollView 
               showsVerticalScrollIndicator={true} 
                onScrollBeginDrag={this.myBeginDrag}
                onScrollEndDrag={this.myEndDrag}
                onMomentumScrollBegin={this.myBeginMove}
                onMomentumScrollEnd={this.myEndMove}
                refreshControl={
                  <RefreshControl refreshing={false} onRefresh={this.myOnRefresh}></RefreshControl>
                }
           > 
                 {/*  fefreshControll 就是刷新时顶部转的那个图标 refreshing={false}表示开始不显示
                 android版本显示的不好看，图标是显示浮在上面
                  只对iOS平台的属性 tintColor="red" title="我的正在刷新"
                  */}
            <View style={styles.view_item}></View>
            <View style={styles.view_item}></View>
            <View style={styles.view_item}></View> 
          </ScrollView>
      </View>
    );
  }
  myBeginDrag(){
    console.log("myBeginDrag");//开始拖动时调用
  }
  myEndDrag(){
    console.log("myEndDrag");
  }
  myBeginMove(){
    console.log("myBeginMove");//为何和myEndDrag 一起被用？  拖动到边界时没有弹跳？？
  }
  myEndMove(){
    console.log("myEndMove");//为何和myEndDrag 一起被用？
  }
  myOnRefresh(){
    console.log("myOnRefresh");
  }
}

const styles = StyleSheet.create({
  container:{
    flex:1,
    backgroundColor:"yellow", 
  }, 
  view_item:{
    margin:10,
    height:300,  
    backgroundColor:"red"
  }

});

 module.exports=App;

-----AppFlatList.js

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,FlatList} from 'react-native';  
 
type Props = {};
 
var allEmployees=[
  {
    name:"张三",
    age:20
  },
  {
    name:"李四",
    age:30
  },
  {
    name:"王五",
    age:24
  },
];
//export default
 class App extends Component<Props> {   
  
  render() 
  {  
    return (
      <FlatList
        data={allEmployees}
        renderItem={
            ({item}) => 
              <View style={{height:30 ,flexDirection:"row"}}>   {/*  key={item.age}没用？？ */}
               <View style={{backgroundColor:"yellow",borderBottomWidth:1,borderColor:"gray"}}><Text>name:{item.name} </Text></View>  
               <View style={{backgroundColor:"pink",borderBottomWidth:1,borderColor:"gray"}}><Text>age:{item.age} </Text></View>
              </View>
          }
      />
    );
  } 

}
 
const styles = StyleSheet.create({ 
 
 });

 module.exports=App;

-----AppNavigation.js
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';  

//npm install --save react-navigation
//3.9.1
import {createStackNavigator, createAppContainer} from 'react-navigation';








//---------------Baidu Map
要生成keystore并保存下来
keytool -genkey -alias lisi      -keystore C:/temp/clientKeystore  -dname "CN=lisi,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN"     -keypass lisikeypass     --storepass clientkeystorepass
keytool -list -v -keystore C:/temp/clientKeystore -storepass clientkeystorepass 来查看  SHA1的值  C9:68:E9:39:60:40:B5:43:03:F4:A5:4A:66:8B:69:3E:59:55:85:3D

示例的包名要固定 org.zh.baiduMap

public static final String strKey = "";

申请key失败??? 
 
//------------selendroid 自动化测试 Selenium的 androidDriver
是ebay的  http://selendroid.io/

<dependency>
  <groupId>io.selendroid</groupId>
  <artifactId>selendroid-client</artifactId>
  <version>0.17.0</version>
</dependency
还有很多其它依赖



//---android  推送消息

MQTT 协议 即时通讯协议

XMPP 协议(聊天工具)实现 androidpn 

在线视频直播

蓝牙低功耗(BLE)
OKhttp = httpClient
Retrofit = httpClient
MVP ~ MVC
Android图片缓存之Glide 

Flutter是谷歌的移动UI框架，可以快速在iOS和Android上构建高质量的原生用户界面 免费、开源  使用 Dart语言开发
Weex 是阿里开源的一款跨平台移动开发工具，Vue版React Native
ligGDX Java游戏开发框架，运行在 Android ,iOS
Xamarin  用Ｃ#开发,生成Ａndroid ,iOS代码
 
 
 
