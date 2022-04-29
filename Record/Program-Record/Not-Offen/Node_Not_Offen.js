

=========Cordova-7  windows
最新版本在 windows/MacOS X 下的已经deprecate(Electron 做为替代,web技术开发跨三大平台桌面应用，使用Python2.7.x) ,支持Android,iOS

windows 10 要用 Visual Studio 2015 

npm install -g cordova
cordova create E:/tmp/VS2015_Cordova7 org.zh VS2015_Cordova7
cd E:/tmp/VS2015_Cordova7
cordova platform add  windows      

VS2015打开CordovaApp.sln 

日志文件中报错 The source completed without providing data to receive.
警告: 程序集绑定日志记录被关闭。
要启用程序集绑定失败日志记录，请将注册表值 [HKLM\Software\Microsoft\Fusion!EnableLog] (DWORD)设置为 1   ，修改为1也没用


修改代码要在CordovaApp项目下，要先cordova plugin add 再写自己的代码，否则自己的代码会丢失！！！
 
windows 10 下
右击 win8.1 项目 -> set as startup project  (如选择 windows 10 (UAP) 就不行???)

 
----win10 支持的插件 
 

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


cordova plugin ls
 
----cordova plugin add cordova-plugin-device 
	document.addEventListener("deviceready", onDeviceReady, false);
	function onDeviceReady() {
		var element = document.getElementById('deviceProperties');
		element.innerHTML = 'Device Cordova: '  + device.cordova + '<br />' +   //3.5.0
							'Device Platform: ' + device.platform + '<br />' +  //windows8
							'Device UUID: '     + device.uuid     + '<br />' +  //
							'Device Model: '    + device.model     + '<br />' +  //Win64
							'Device Version: '  + device.version  + '<br />';   //6.3.xxx
	}

-----cordova plugin add cordova-plugin-dialogs
	function alertDismissed() {
		out("you clicked the button");
	}

	function showAlert( )
	{
	  navigator.notification.alert(
				'You are the winner!',  // message
				alertDismissed,         // callback
				'Game Over',            // title
				'Done'                  // buttonName
			);
    }
	
-----cordova plugin add cordova-plugin-network-information
	document.addEventListener("offline", onOffline, false);//当3G,或者wifi网络打开,或者关闭时
    function onOffline() {
    	out("offline");
    }
    document.addEventListener("online", onOnline, false);
    function onOnline() {
    	out("online");
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
        out('Connection type: ' + states[networkState]);
    }
-----cordova plugin add cordova-plugin-camera


-----cordova plugin add cordova-plugin-geolocation


 如使用jquery.js 报用innerHTML不安全,要求使用toStaticHTML或createElement
 实际是appendChild()在断点处停止，这样jqueryMobile等插件就用不了
https://msdn.microsoft.com/en-us/library/windows/apps/hh849625.aspx
 
======================