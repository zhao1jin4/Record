
引用第三方jar，不使用网络，而是本地的jar包,libs目录和src目录同级
implementation fileTree(includes: ["*.jar"],dir: 'libs')
 
 
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

环境变量 ANDROID_SDK_ROOT=C:\Users\dell\AppData\Local\Android\Sdk
设置PATH 有 %ANDROID_SDK_ROOT%\tools\bin 目录(有avdmanager,sdkmanager命令) 和 %ANDROID_SDK_ROOT%\platform-tools目录(有adb命令)
 gradle
 
有一个cordova-android-master\VERSION文件看版本
cordova-android Version 	Supported Android API-Levels 	Equivalent Android Version
10.X.X 						 22 - 30 						5.1 - 11.0.0
9.X.X 						 22 - 29 						5.1 - 10.0.0
------------离线
https://www.apache.org/dist/cordova/ 
	tools 		中下载 cordova-11.0.0.tgz 
	platforms	中下载 cordova-android-10.1.2.tgz   
	plugins		中下载 
 
#D:\Application\cordova-11.0.0\package\bin\cordova.cmd  create . org.zh.cordova11 A_Cordova_11  (源码使用node命令) 
报Cannot find module 'loud-rejection/register' 还是用在线吧

cd  cordova-android-10.1.2\package\framework
gradle build  #有要求build tools版本 30.0.3,会安装 %USERPROFILE%\AppData\Local\Android\Sdk\platforms\android-30
会生成 build/output/aar/framework-release.aar 和 framework-debug.aar

 
-------------在线
npm install -g cordova 
命令安装到 
D:\Application\node-v12.16.1-win-x64\node_modules\cordova\
D:\Application\node-v12.16.1-win-x64\cordova.cmd 是PATH位置  
安装后就可用cordova 命令，cordova --version 显示11.0.0
  
cordova create D:/tmp/A_Cordova_11 org.zh.cordova11 A_Cordova_11
cd D:/tmp/A_Cordova_11
cordova platform add android		##日志显示 cordova-android@^10.1.1 和   Android target: android-30
#cordova platform add android@^10.1.1   指定SDK版本

D:\tmp\A_Cordova_11\platforms\android 是项目目录,有CordovaLib项目是org.apache的代码
   生成的build.gradle代码是 classpath "com.android.tools.build:gradle:${cordovaConfig.AGP_VERSION}" 
		#cdv-gradle-config.json 中 "AGP_VERSION": "4.2.2"
	app\build.gradle代码是  gradleVersion = cordovaConfig.GRADLE_VERSION
		#cdv-gradle-config.json  中"GRADLE_VERSION": "7.1.1" 
	生成的	repositories.gradle  
	
build.gradle 	文件中有 apply from: 'CordovaLib/cordova.gradle'
app/build.gradle 文件中有 implementation(project(path: ":CordovaLib"))
settings.gradle 文件中有 include ":CordovaLib"

   用 AndroidStudio-2021.1 打开报 C:\Users\xx\AppData\Local\Temp\wrapper_init1.gradle' appears to be corrupted.
   修改项目使用指向gradle-7.2-bin的解压目录(而不是gradle-wrapper.properties，也没有这个文件)后正常，可安装到手机
   
   使用命令行cd platforms\android , gradle build (7.2版本) 可以编译成功

#cordova platform rm android
#cordova platform add ios
#cordova platform add electron

cordova platform ls
cordova requirements(要求在项目目录下运行)  显示已经安装设置的gradle,android sdk

cordova build 	(用 gradle-7.2 就报错)
			找android命令,gradle 下载很多maven库,会下载SDK Platform
			会把  \www 			覆盖到  \platforms\android\app\assets\www
			会把  \plugins		覆盖到  \platforms\android\app\assets\www\plugins
			会把  \config.xml	覆盖到  \platforms\android\app\res\xml\config.xml

#cordova build ios  只build指定平台

#cordova emulate android 启动模拟器

cordova run android  会下载gradle-7.1.1-all.zip 要求有环境变量  ANDROID_SDK_ROOT / JAVA_HOME (1.8.161+) / PATH

本来空项目可以正常运行的
-----在执行全部 cordova plugin add xx 后报错？？
AndroidManifest.xml
<!--
    android:name="org.apache.cordova.camera.FileProvider" 找不到这个类，修改为
        android:name="androidx.core.content.FileProvider"
-->

config.xml
<!--
    <param name="android-package" value="org.apache.cordova.file.FileUtils" /> 找不到这个类，修改为
   <param name="android-package" value="android.os.FileUtils" />
-->
	 
 
-----以下未成功
注释
build.gradle 	文件中有 apply from: 'CordovaLib/cordova.gradle'
app/build.gradle 文件中有 implementation(project(path: ":CordovaLib"))
settings.gradle 文件中有 include ":CordovaLib"

增加
app/libs目录 放 framework-debug.aar
app/build.gradle 中在android{}中增加

    repositories {
        flatDir {
            dirs 'libs' // aar目录
        }
    }
dependencies {}中增加
    implementation(name: 'framework-debug', ext: 'aar')
-----
 
 
 
以下两相bin目录不要删
platforms\android\cordova\node_modules\shelljs\bin
platforms\android\cordova\node_modules\.bin

---用CLI 在线 管理插件
cd D:/tmp/A_Cordova_11

自带 cordova-plugin-whitelist  插件

cordova plugin search camera 
网址 http://cordova.apache.org/plugins/?q=camera

cordova plugin add cordova-plugin-camera
cordova plugin add cordova-plugin-geolocation

cordova plugin add cordova-plugin-battery-status


cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-inappbrowser 
cordova plugin add cordova-plugin-network-information
cordova plugin add cordova-plugin-splashscreen
cordova plugin add cordova-plugin-vibration
cordova plugin add cordova-plugin-device

/*
cordova plugin add cordova-plugin-file   //新的，但版本太新了，media，media-capture提示
The Android Persistent storage location now defaults to "Internal". Please check this plugin's README to see if your app
	lication needs any changes in its config.xml.

	If this is a new application no changes are required.

	If this is an update to an existing application that did not specify an "AndroidPersistentFileLocation" you may need to
	add:

		  "<preference name="AndroidPersistentFileLocation" value="Compatibility" />"

	to config.xml in order for the application to find previously stored files.

	Adding cordova-plugin-file to package.json

*/
/*
cordova plugin add cordova-plugin-file-transfer 提示，新的文档上没有这个了,Java类还报错(自动安装了 cordova plugin add cordova-plugin-file )
	The Android Persistent storage location now defaults to "Internal". Please check this plugin's README to see if your application needs any changes in its config.xml.

	If this is a new application no changes are required.

	If this is an update to an existing application that did not specify an "AndroidPersistentFileLocation" you may need to
	add:

		  "<preference name="AndroidPersistentFileLocation" value="Compatibility" />"

	to config.xml in order for the application to find previously stored files.

	Adding cordova-plugin-file-transfer to package.json

cordova plugin remove cordova-plugin-file-transfer
cordova plugin remove cordova-plugin-file
*/

cordova plugin add cordova-plugin-media //(自动安装了 cordova-plugin-file 要求的版本 )
		
	The Android Persistent storage location now defaults to "Internal". Please check this plugin's README to see if your application needs any changes in its config.xml.

	If this is a new application no changes are required.

	If this is an update to an existing application that did not specify an "AndroidPersistentFileLocation" you may need to
	add:

		  "<preference name="AndroidPersistentFileLocation" value="Compatibility" />"

	to config.xml in order for the application to find previously stored files.

	Adding cordova-plugin-media to package.json

cordova plugin add cordova-plugin-media-capture 
 
cordova plugin add cordova-plugin-screen-orientation

#cordova plugin add cordova-plugin-contacts  新的官方文档没有了
#cordova plugin add cordova-plugin-globalization 新的官方文档没有了
Device Motion  过时使用HTML5


<项目目录>\node_modules 目录 
<项目目录>\config.xml	文件中有增加
<项目目录>\platforms\android 是android的工作目录, platforms\android\app\src\main\assets\www\plugins 目录中 有增加

cordova plugin ls  查看已安装的插件

cordova plugin rm  cordova-plugin-camera    删插件



------- cordova plugin add cordova-plugin-camera

package.json 中增加
	"cordova-plugin-camera": {
		"ANDROIDX_CORE_VERSION": "1.6.+"
	  },
node_modules 目录中增加

plugins/fetch.json {}中增加
	 "cordova-plugin-camera": {
		"source": {
		  "type": "registry",
		  "id": "cordova-plugin-camera@6.0.0"
		},
		"is_top_level": true,
		"variables": {}
	  },
plugins/android.json   "installed_plugins": 下增加
    "cordova-plugin-camera": {
      "ANDROIDX_CORE_VERSION": "1.6.+",
      "PACKAGE_NAME": "org.zh.cordova11"
    },
	
platforms\android\android.json
	{
	  "xml": "<feature name=\"Camera\"><param name=\"android-package\" value=\"org.apache.cordova.camera.CameraLauncher\" /></feature>",
	  "count": 1
	}, 	
platforms\android\app\src\main/res/xml/config.xml 新增加
	<feature name="Camera">
		<param name="android-package" value="org.apache.cordova.camera.CameraLauncher" />
	</feature>
 

		
----platforms\android\app\src\main\assets\www\cordova_plugins.js  文件中
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
		"cordova-plugin-camera": "6.0.0",
	}
});
 
platforms\android\app\src\main\assets\www\plugins 下多了
	cordova-plugin-camera 目录 
	
多了org.apache.cordova.camera包,有CameraLauncher 类

AndroidManifest.xml 新增加
	MAIN  activity中增加
		<provider android:authorities="${applicationId}.cordova.plugin.camera.provider" android:exported="false" android:grantUriPermissions="true" android:name="org.apache.cordova.camera.FileProvider">
			<meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/camera_provider_paths" />
		</provider>
	
	<application>同级增加	
	<queries>
        <intent>
            <action android:name="android.media.action.IMAGE_CAPTURE" />
        </intent>
        <intent>
            <action android:name="android.intent.action.GET_CONTENT" />
        </intent>
        <intent>
            <action android:name="android.intent.action.PICK" />
        </intent>
        <intent>
            <action android:name="com.android.camera.action.CROP" />
            <data android:mimeType="image/*" android:scheme="content" />
        </intent>
    </queries>
	
platforms\android\app\src\main\res\xml\camera_provider_paths.xml
	<paths xmlns:android="http://schemas.android.com/apk/res/android">
		<cache-path name="cache_files" path="." />
	</paths>

build.gradle增加
  implementation "androidx.core:core:1.6.+"
  
android\project.properties增加
	cordova.system.library.1=androidx.core:core:1.6.+
 
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
 
 
	
-------- cordova plugin add org.apache.cordova.geolocation
--取一次
document.addEventListener("deviceready", onDeviceReady, false); 
function onDeviceReady() {
	navigator.geolocation.getCurrentPosition(onSuccess, onError);
} 
function onSuccess(position) {
	var element = document.getElementById('geolocation');
	element.innerHTML = 'Latitude: '           + position.coords.latitude              + '<br />' +
						'Longitude: '          + position.coords.longitude             + '<br />' +
						'Accuracy: '           + position.coords.accuracy              + '<br />' +
						 'Timestamp: '         + position.timestamp          + '<br />';
}

// onError Callback receives a PositionError object 
function onError(error) {
	alert('code: '    + error.code    + '\n' +
			'message: ' + error.message + '\n');
}
--取多次 
    document.addEventListener("deviceready", onDeviceReady, false);

    var watchID = null; 
    function onDeviceReady() { 
        var options = { enableHighAccuracy: true };
        watchID = navigator.geolocation.watchPosition(onSuccess, onError, options);
    } 
    
	var i=0;
    function onSuccess(position) {
        var element = document.getElementById('geolocation');
        element.innerHTML = 'Latitude: '  + position.coords.latitude      + '<br />' +
                            'Longitude: ' + position.coords.longitude     + '<br />' +
                             '次数: '(i++);
    }

    // clear the watch that was started earlier
    // 
    function clearWatch() {
        if (watchID != null) {
            navigator.geolocation.clearWatch(watchID);
            watchID = null;
        }
    }

    // onError Callback receives a PositionError object
    //
    function onError(error) {
      alert('code: '    + error.code    + '\n' +
            'message: ' + error.message + '\n');
    }



--- cordova plugin add org.apache.cordova.battery-status
 
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
				//out("capture video i:"+mediaFiles[i]);
				out("capture video i fullPath:"+mediaFiles[i].fullPath);
				out("capture video i name:"+mediaFiles[i].name);
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

--- cordova plugin add org.apache.cordova.vibration

// Vibrate for 3 seconds
navigator.vibrate(3000);


// Vibrate for 1 second
// Wait for 1 second
// Vibrate for 3 seconds
// Wait for 1 second
// Vibrate for 5 seconds
navigator.vibrate([1000, 1000, 3000, 1000, 5000]);

navigator.vibrate(0)//取消




 
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

	element.innerHTML = 'Device manufacturer: '  + device.manufacturer + '<br />' +   
						'Device Cordova: '  + device.cordova + '<br />' +   
						'Device Platform: ' + device.platform + '<br />' + 
						'Device UUID: '     + device.uuid     + '<br />' +  
						'Device Model: '    + device.model     + '<br />' +  
						'Device Version: '  + device.version  + '<br />';   
}
<p id="deviceProperties">Loading device properties...</p>
 

------- cordova plugin add cordova-plugin-splashscreen 新版本测试不行
android系统在config.xml中加  
<preference name="SplashScreenDelay" value="3000" />


--platforms\android\app\src\main\assets\res\screen\android\config.xml  或 res\screen\android\config.xml 
(res是和www同级的)

<platform name="android">
	<!--
        land short for landscape mode
        port short for portrait mode
         add the -night keyword in between the layout and size keywords of the image's density attribute value. E.g.: land-night-hdpi
    -->
    <splash src="res/screen/android/splash-port-hdpi.png" density="hdpi"/>
    <splash src="res/screen/android/splash-port-ldpi.png" density="ldpi"/>
    <splash src="res/screen/android/splash-port-mdpi.png" density="mdpi"/>
    <splash src="res/screen/android/splash-port-xhdpi.png" density="xhdpi"/>
    <splash src="res/screen/android/splash-port-xxhdpi.png" density="xxhdpi"/>

    <!-- Landscape -->
    <splash src="res/screen/android/splash-land-hdpi.png" density="land-hdpi" />
    <splash src="res/screen/android/splash-land-ldpi.png" density="land-ldpi" />
    <splash src="res/screen/android/splash-land-mdpi.png" density="land-mdpi" />
    <splash src="res/screen/android/splash-land-xhdpi.png" density="land-xhdpi" />
    <splash src="res/screen/android/splash-land-xxhdpi.png" density="land-xxhdpi" />
    <splash src="res/screen/android/splash-land-xxxhdpi.png" density="land-xxxhdpi" />

    <!-- Portrait -->
    <splash src="res/screen/android/splash-port-hdpi.png" density="port-hdpi" />
    <splash src="res/screen/android/splash-port-ldpi.png" density="port-ldpi" />
    <splash src="res/screen/android/splash-port-mdpi.png" density="port-mdpi" />
    <splash src="res/screen/android/splash-port-xhdpi.png" density="port-xhdpi" />
    <splash src="res/screen/android/splash-port-xxhdpi.png" density="port-xxhdpi" />
    <splash src="res/screen/android/splash-port-xxxhdpi.png" density="port-xxxhdpi" />
  
    <!-- Dark Mode -->
    <splash src="res/screen/android/splash-land-night-hdpi.png" density="land-night-hdpi" />
    <splash src="res/screen/android/splash-land-night-ldpi.png" density="land-night-ldpi" />
    <splash src="res/screen/android/splash-land-night-mdpi.png" density="land-night-mdpi" />
    <splash src="res/screen/android/splash-land-night-xhdpi.png" density="land-night-xhdpi" />
    <splash src="res/screen/android/splash-land-night-xxhdpi.png" density="land-night-xxhdpi" />
    <splash src="res/screen/android/splash-land-night-xxxhdpi.png" density="land-night-xxxhdpi" />

    <splash src="res/screen/android/splash-port-night-hdpi.png" density="port-night-hdpi" />
    <splash src="res/screen/android/splash-port-night-ldpi.png" density="port-night-ldpi" />
    <splash src="res/screen/android/splash-port-night-mdpi.png" density="port-night-mdpi" />
    <splash src="res/screen/android/splash-port-night-xhdpi.png" density="port-night-xhdpi" />
    <splash src="res/screen/android/splash-port-night-xxhdpi.png" density="port-night-xxhdpi" />
    <splash src="res/screen/android/splash-port-night-xxxhdpi.png" density="port-night-xxxhdpi" />
</platform>

/*
--- cordova plugin add org.apache.cordova.globalization 新的官方文档没有
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
*/		
----管理插件使用plugman
npm install -g plugman   必须有 git  命令
plugman --platform android --project D:\Program\eclipse_android_workspace\A_Cordova_3 --plugin <name|url|path> 

	
------------------插件开发
res/xml/config.xml中加
<feature name="MyPlugin">
	<param name="android-package" value="org.zh.cordova11.MyPlugin" />
	<param name="onload" value="true" />
</feature>
//---Adnroid本地
package org.zh.cordova11;
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
Cordova-11  要 Xcode 11.0 (the minimum required version) runs only on OS X version 10.14.4 (Mojave) 
 
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
 
 
 
