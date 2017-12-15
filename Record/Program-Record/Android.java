
Kotlin 成为 Android 官方开发语言

--------------OMA
www.openmobilealliance.org  ->TECHNICAL INFORMATION->Current Release->OMA Device Management V2.0

Android 手机安全 , 禁用应用仿问网络 , 读通讯录 , 读短信 , 读文件系统 , 读位置 , 禁用注册3G网络启动／停止监听 , 禁用推送


-------------Android 更新版本 下载,  

Dalvik VM是Android平台的核心组成部分之一，它的名字来源于冰岛一个名为Dalvik的
小渔村。Dalvik VM并不是一个Java虚拟机，它没有遵循Java虚拟机规范，不能直接执行Java
的Class文件，使用的是寄存器架构而不是JVM中常见的栈架构。但是它与Java又有着千丝万
缕的联系，它执行的dex（Dalvik Executable）文件可以通过Class文件转化而来



windows/mac 下载多选  Intel x86 Emulator Accelerator(HAXM installer)( linux不用的)) 
		HAXM=Hardware Accelerated Execution Manager
		运行 android-sdk-windows\extras\intel\Hardware_Accelerated_Execution_Manager\silent_install.bat  (也可intelhaxm-android.exe)
	    如启动模拟器 要求BIOS 打开 Virtualization Technology(VT)  VT-x 
	
	下载多选 Android support Library,Google Repository
	
	
Android SDK Manager -> Settings ->HTTP Proxy Server 输入  mirrors.neusoft.edu.cn (yourid.repository.bugly.qq.com),在HTTP Proxy Port 80 
并且选中 Force  复选框->Close , Packages->Reload 

http://www.androiddevtools.cn/ 有 可配置在SDK Manager 中镜像

大连东软信息学院镜像服务器地址 
http://mirrors.neusoft.edu.cn 端口：80   下有 android目录  下还有其它的镜像站点,如centos , ubuntu-releases
	   
 yourid.repository.bugly.qq.com 80 可用的,但不可http://访问    版本会更新,要重新启动
 mirrors.dormforce.net 80  电子科技大学 可用的,但不可http://访问
 
https://mirrors.tuna.tsinghua.edu.cn/help/AOSP/      Android Open Source Project (AOSP)

翻墙方法 
免费的   1. lattern 蓝灯 (可上google,但下载Android SDK 还要设置镜像地址mirrors.neusoft.edu.cn )  https://lanterncn.cn/
 
---------------上  Android 更新版本 下载
 
 ADT-23.0.7_为SDK Tools r24.1.2设计的_最后google官方版本2015-08 
----- Android Studio
  在 ~/.AndroidStudio2.3/ 

 默认安装目录 C:\Users\zhaojin\AppData\Local\Android\sdk 
  默认工作区  C:\Users\zhaojin\AndroidStudioProjects
 如不带SDK 启动时向导中修改SDK位置,或者取消后,在小窗口中Configure->SDK Manager,中配置目录名如有中文显示为方块
 
基于IntelliJ IDEA ,使用openJDK8 ,Gradle构建
像Word一样的单文档,一个Studio只能打开一个项目,也可以建立Module,都是在gradel子目录中,都可以单独运行,使用同一个gradle构建,生成gradlew 等文件
 
第一个项目的名字是默认生成的,如mobile,可以重命名,会修改settings.gradle中的内容

 File->Other Settings->Default Project Structure 中设置Android SDK位置,Android NDK,JDK 
 File-> Setting... ->Appearance & Behavior->System Settings-> Android SDK 可以下载SDK，镜像要在建立模拟器时下载，工具栏上也有的
 
 File-> Project Structure ...

设置菜单字体大小   File->Setting...->Appearance & Behavior-> Appearance -> Override defaul fonts by (not recommaned) 后面设置为14 (字体SimHei)

设置编辑器字体大小 File->Setting...->Editor -> Color & Fonts->Font-> Save As ...后设置字体,16
设置控制台器字体大小 File->Setting...->Editor -> Color & Fonts->Console Font  16


<project>\App\build\generated\source\r\debug\<package>\R.java
<project>\App\build\generated\source\r\AndroidTest\debug\<package>\R.java

Tools > Android > Android Device Monitor 会启动tools\monitor  可以传文件 

build.gradle文件在android括号中加
 productFlavors
            {
                    arrogant{  //有目录名为arrogant与main同级的,类似有测试版本,生产版本 
                        applicationId 'com.my.arro'
                    }
                    friendly{
                        applicationId 'com.my.fri'
                    }
                    obsequious {
                        applicationId 'com.my.obs'
                    }
            }
最外层加
task printVariantNames{
    doLast{
        android.applicationVariants.all{
            variant -> println variant.name
        }
    }
}

执行 gradlew printVariantNames  (也可以使用 gradlew pVN 的形式)结果就是arrogantDebug,arrogantRelease及friendly,obsequious

右击目录(libs)的jar包->add as library...
建立项目自带的libs目录,放入jar包不会在AndroidStudio中显示,要重启才生效

多个jar都有 META-INF/LICENSE 会报 DuplicateFileException 
build.gradle中  android { }内部增加
  packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }
	

LLDB 是 a next generation, high-performance debugger. 
CMake 是 cross-platform  , build, test and package
GPU Debugging tools



----- 
 android-sdk-windows\tools\monitor.bat 会启动界面 ,即老的ADT插件里的DDMS,有File Explorer
 
tools\android.bat update sdk --no-ui  新版本会调用 tools\bin\sdkmanager  --update  没有界面，使用Android Studio
新版本建议使用sdkmanager.bat avdmanager.bat 命令

sdkmanager.bat   --list  显示同界面所有sdk列表

老版本的 tools_r25.2.3-windows\android.bat  还是有界面的
 
tools\bin\sdkmanager --update --proxy=http  --proxy_host=mirrors.neusoft.edu.cn  --proxy_port=80 --no_https   还是找不到.xml文件
Android Studio 配置成这个 http://mirrors.neusoft.edu.cn/android/repository/repository-12.xml


emulator: ERROR:This AVD's configuration is missing a kernel file!!
是因为android所在路径太长了


http://developer.android.com/develop/index.html
samples\android-19\legacy\ApiDemos  运行后 app,drawable,view中的是可见的,Content,Preference,Animation
 

添加环境变量 ANDROID_SDK_HOME
PATH指向tools 目录,tools/bin/ ,platform-tools目录下有adb命令

Android 使用的是　dalvik 虚拟机

AVD（Android Virtual Device）
OMS(Open Mobile System)
DDMS(Dalvik Debug Monitor Service)

按"HOME"->点屏幕中的箭头->"Settings"->Language & Input ->locale (有谷歌拼音输入法)->中文简体,
Setting->Language & Input ->Language->选择 中文(简体) ,  界面变成中文
设置->声音->音量 默认没有铃声,把.mp3上传到自己的目录中,可以使用拖动的方式,如 /mnt/sdcard,重新启动,"音乐"播放器->歌曲 可以自动检测到
点击播放->按menu->用做铃声

设置->日期和时间->取消 自动确定时区 和 自动确定日期时间(使用网络),时区选择 中国标准时间(北京)GMT+8
设置->应用程序->选择后,可以卸载

相机拍照默认目录 SD卡/DCIM/Camera/

ctrl+f12 模拟器变为横向,要关金山词霸

"Menu" 显示自己程序结果

长按Home键->显示所有正在运行的程序,可以一次性清空,模拟器在选一个长按->delete


run configuration...新建一个Android,来运行
右击Android 项目->运行,如已经启动模拟器,会自动安装apk包

导入sample,新建 Android项目->选择create project from exist source ,选目录后,会自动写Package name:的值



-------android命令
列出模拟器类型:	 android list targets 老命令
使用 			avdmanager.bat list target 看到SDK 版本

创建模拟器:	 android create avd --target 1 --name myAVD   --skin QVGA
			>Do you wish to create a custom hardware profile [no]
			android list targets的结果id: 1 or "android-17"
	会生成C:\Documents and Settings\Administrator\.android\avd\myAVD.avd目录 和 myAVD.ini文件
		  C:\Users\Administrator\.android\avd
	也可以在eclipse中 SDK and AVD Manager来创建
	QVGA 是在android-sdk-windows\platforms\android-15\skins目录下放外观的主题

列出自己创建的模拟器：android list avd  
删除模拟器 android  delete avd --name myAVD 


-------emulator命令
指定用什么模拟器启动：emulator -debug avd_config -avd myAVD
							-partition-size 256  system/data分区大为256M

mksdcard 64M c:\temp\my.img    制作SD卡
C:\Users\Administrator\.android\avd\myAVD.avd\sdcard.img 默认目录
emulator -sdcard  c:\temp\my.img -avd  myAVD

emulator -avd myAVD  -http-proxy 172.52.17.184:8080   -dns-server 10.103.33.51 
emulator @myAVD -http-proxy 172.52.17.184:8080   -dns-server 10.103.33.51


-------adb命令
platform-tools\adb
adb(Android Debug Bridge)


adb 监听5037端口 ,如不能在eclipse中启动,重启机器
adb start-server
adb kill-server

adb devices 显示连接的android设备或者模拟器
adb -s emulator-5554 get-state   #指定一个设备或者模拟器
adb get-state

adb push <local> <remote>
adb pull <remote> <local>
启动模拟器后安装软件包用 adb install c:\ poker80.apk


首先启动Android模拟器
#adb  shell
#cd  data			//也可 cd data/app
#cd  app
#ls
显示com.xxx.apk包 ,eclipse的bin目录下是生成的
#rm  xxx.apk  卸载

也可在Eclipse的DDMS的File Explorer来做

安装软件 adb -s emulator-5554 install -r  c:\my.apk   (-r reinstall)
卸载软件 adb uninstall com.can.myandroid (这是package名,没有.apk)
		 adb uninstall -k <package名> 卸载软件但是保留配置和缓存文件

AndroidStuiod生成的命令
adb push E:\tmp\A_Cordova_7\platforms\android\build\outputs\apk\android-debug.apk /data/local/tmp/org.zhaojin.cordova7
adb shell pm install -r  /data/local/tmp/org.zhaojin.cordova7
adb shell am start -n "org.zhaojin.cordova7/org.zhaojin.cordova7.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER


adb logcat *:W 显示日志,比在eclipse中显示的消息要长,会一直监控


真机Wifi可以设置代理,长按连接的wifi->修改网络->复选显示高级选项,代理设置为手动,输入IP和端口,用户名密码要在自带浏览器或Chrome中输入,都可以保存
Firefox代理 的about:config->输入proxy搜索->network.proxy.http 设置IP,network.proxy.http_port 对应端口,设置network.proxy.type为1,默认是5

真机Wifi可以设置VPN,如客户端是Cisco AnyConnect的VPN,android选择类型为IPSec Xauth PSK,输入服务器的地址,IPSec标识符的值为Group的值,如NN,IPSec预共享密钥的值为Group的密码,即NN的密码,保存后再连接输入用户名 和 密码前缀+动态密码


模拟器设置上网代理
Settings->WIRELESS & NEWORKS下的More...->Mobile Networks->Access Point Names(APN)->选择已有的->有设置proxy,port,username,password

真机要打开debug, adb devices才可看到,设置->开发人员选项-> 选中 “USB调试”
android 4.2 以后版本打开调试 Settings > About phone 点 Build number 七次,再前面的屏幕中有 Developer options

buildTools多一个版本为23.0.3 是为NetBeans插件NBAndroid准备的

----真机得到root权限,bootloader加锁(刷机用)不影响得到root权限     未试?????
设置中关闭 快速启动
拔掉usb线,正常关机,然后按着  音量下键  + 开机键  不放约10秒 会进入bootloader (华为的bootloader加锁了进不了,得到root是不影响的)
然后连接电脑
fastboot devices  
fastboot oem unlock  [解锁密码]   , 要在官方网申请
fastboot boot cm-hero-recovery.img 	手机进入了recovery模式
 检查  fastboot oem get-bootinfo
 
----打包
aapt package -m -J gen -M AndroidManifest.xml -S res -I D:\android-sdk-windows\platforms\android-16\android.jar
//这句命令主要是重新自动生成R.java文件
jar -cvf testNDK.jar *
dx --dex --output=D:/temp/classes.dex  D:/temp/testNDK.jar //把jar包转换成android手机能够运行的dex文件,
//都要指定为绝对路径
aapt package -u -x -f -M AndroidManifest.xml -S res -I D:\android-sdk-windows\platforms\android-16\android.jar -F testNDK.apk   //生成应用程序apk文件
//字符没有国际化报错,去-z就OK
aapt add testNDK.apk classes.dex  //.dex文件压缩到apk文件，最终生成可运行的apk应用程序

keytool -genkey -alias android123 -keyalg RSA -validity 20000 -keystore android123.keystore -dname "CN=lisi,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN"  -keypass androidkeypass --storepass androidkeystorepass
jarsigner -verbose -keystore android123.keystore -signedjar testNDK_sigined.apk -storepass androidkeystorepass -keypass androidkeypass -sigfile CERT -digestalg SHA1 -sigalg MD5withRSA testNDK.apk android123
//生成新的testNDK_sigined.apk,在META-INF目录中有CERT.SF,CERT.RSA签名文件,无签名的apk是无法安装
// ./docs/tools/publishing/app-signing.html
jarsigner -verify -verbose -certs testNDK_sigined.apk  //验证签名,要有jar verified.如有Warning:忽略
//文件与eclipse插件产生的apk包中不一样

//如eclipse export的apk,使用adb install -r d:/temp/xxx.apk安装
zipalign -v 4 testNDK_sigined.apk testNDK_align.apk   ('4' provides 32-bit alignment,-f overwrite)
zipalign -c -v testNDK_align.apk  (-c check)


dex反编译
1.D:\Program\android-sdk-windows\platform-tools\dexdump.exe  可反编译apk包中的dex文件
	启动模拟器　
	 
	adb push c:\temp\classes.dex /data/local //把dex文件上传模拟器
	adb shell
	dexdump /data/local/classes.dex

2. 第三方 dedexer 工具 生成的不是java代码
	java -jar ddx.jar -d <directory> <dex file>
	java -jar ddx1.11.jar -D -o -d C:\temp\dex_out C:\temp\classes.dex

3.google code的dex2jar
	dex2jar-0.0.7-SNAPSHOT>dex2jar c:\temp\classes.dex
	会生成.jar,再把里面的class文件反编译 OK

4.apktool http://code.google.com/p/android-apktool/downloads/list
解压apktool1.4.3.tar.bz2						中有apktool.jar,
解压apktool-install-windows-r04-brut1.tar.bz2	中有apktool.bat,aapt.exe
所有文件都放在同一个目录下
apktool d XXX.apk （目标文件夹）      反编译 geek.apk到文件夹test
apktool d *.apk ./src  测试OK
 

======手机HTML,JS,CSS调试方法

AndroidManifest.xml    <application  android:debuggable="true">   相当于在IDE中打开debug



-------使用Chrome,在android 上远程调度 ,手机可以通过 USB 连接电脑仿问网络
https://developers.google.com/chrome-developer-tools/docs/remote-debugging

windows 要安装USB driver(win7 进资源管理器->找到自己的手机(Android Composite ADB) 右击->更新驱动程序软件->浏览选择  <sdk>\extras\google\usb_driver\ ,下载时要选择Google USB Driver)
Ubuntu 打开调试  建立文件 /etc/udev/rules.d/51-android.rules    (udev rule)
	SUBSYSTEM=="usb", ATTR{idVendor}=="0bb4", MODE="0666", GROUP="plugdev" 
	#0bb4 是手机厂商ID, 	Huawei 是 12d1
	#,MODE读写权限,GROUP是操作系统的组
chmod a+r /etc/udev/rules.d/51-android.rules

---手机
	打开  USB Debugging 模式, 如为4.2以上版本 Settings > About phone -> Build number ,点7次返回上个屏幕有  Developer options
	手机Chrome中->设置->开发者工具->USB网页调试->打开 (新版本中没了???)
	
---电脑
	adb forward tcp:9919 localabstract:chrome_devtools_remote
	Chrome 的URL中输入 about:inspect  (也可 工具->检查设备),要可以看到自己的手机中Chrome打开的页
	
	Chrome http://localhost:9919/ 也打开手机中的网页
	
	port forward 按钮->弹出对话框中,每一行是一对端口转发,第一列的android手机上的端口号8080,第二列是电脑上的IP:端口,localhost:8181,复选enable port forwarding
	
	在手机中chrome输入127.0.0.1:8080 (只127.0.0.1)会跳到电脑chrome的 localhost:8181,手机USB直接连接电脑,可不用wifi同网断 
	可在电脑中输入手机网页,打开,点页旁边的inspect ,但好像一直打不开????
 

------Firefox 调试 OK
https://developer.mozilla.org/en-US/docs/Tools/Remote_Debugging/Firefox_for_Android

手机和电脑在同一个wifi网络中,手机中输电脑IP(也可外部如m.taobao.com)
USB连线手机

--Anroid 中打开 开发调试
Firefox for Android中点菜单->设置->开发者工具->打开远程调试
	about:config 中搜索  devtools 查看 devtools.debugger.remote-enabled 已经修改为true了,port默认6000 

--PC 中 
Tools->Web Developer->Toggle Tools 单击设置按钮->最下面的enable remote debugging已经复选 
		about:config 中查  devtools.debugger.remote-enabled 已经修改为true了,port默认6000 

adb forward tcp:6000 localfilesystem:/data/data/org.mozilla.firefox/firefox-debugger-socket
adb forward tcp:6000 tcp:6000
tools->web developer->connect...连接后android手机有提示是否连接,PC的firefox中可以看到Android中的Firefox打开的网页,点击后可在PC的DeveloperTools中打JS断点调试

------

R类是Resource的缩写
android.util.Log.v ,d,i,w,e 对应下面
VERBOSE、DEBUG、INFO、WARN、ERROR


package org.zhaojin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestActivity extends Activity
{
	private static final String TAG = "HelloAndroid";//只是显示在logcat的tag列中
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);//不要省略

		TextView tv = new TextView(this);
		tv.setText("Hello, Android");//会修改main.xml

		Log.v(TAG, "VERBOSE");
		Log.d(TAG, "DEBUG");
		Log.i(TAG, "INFO");
		Log.w(TAG, "WARN");
		Log.e(TAG, "ERROR");
		setContentView(R.layout.main);//对应main.xml
	}
}
debug运行,DDMS视图下 logCat标签中显示不同颜色的信息

可以打断点


R.java不能被修改,加了新资源,会自动更新
常量的名字都与res文件夹中的文件名相同，所存储的是该项目所有资源的索引


Android应用程序由以下4个模块构造而成

Activity		一个界面可以有多个Activity	
Intent			页面跳转用的,程序切换,传数据
Content Provider
Service			后台程序



每当一个Activity（或者操作系统）要执行一个操作时，它将创建出一个Intent的对象,
这个Intent对象可描述你想做什么，你想处理什么数据，数据的类型，以及一些其他信息。
而Android则会和每个Application所暴露的intent-filter的数据进行比较，


一个活动（Activity）通常就是一个单独的屏幕
Intent这个特殊类实现在Activity与Activity之间的切换

典型的动作类型有：MAIN(程序的入口点)、VIEW、PICK、EDIT等

				Intent intent = new Intent();  
                intent.setClass(Activity01.this, Activity02.class);  //Activity01.this返回Context
                startActivity(intent);  
                Activity01.this.finish(); //结束(关闭)自己

感兴趣的事件发生时会使用NotificationManager通知用户
IntentReceiver在AndroidManifest.xml中注册，但也可以在代码中使用Context的 this.registerReceiver()进行注册
<receiver> BroadcastReceiver

SQLite数据库


Android自身也提供了现成的content provider：Contacts、Browser、CallLog、Settings、MediaStore
ContentResolver  query()、insert()、update()等
ContentProvider正是用来解决在不同的应用包之间共享数据的工具。


drawable目录 精度:高（hdpi），中（mdpi）和低（ldpi）,
要注意的是drawable目录下的文件名  和 values目录的xx文件中的<drawable name="red">#7f00</drawable> 不能有相同的名字的(drawable)


自动生成的 R 的一个作用是,如果res目录中的资源 没有在项目中引用,打包时不会包含res目录中未使用的资源 
assests目录中的文件不会生成R类的引用,一定会被打包

res 下的其它目录 anim,xml(getResource().getXML()),raw (不会被编译)



density 

Resources r = this.getContext().getResources();  
String appname= ((String) r.getString(R.string.app_name));  

getApplication().getResources().getString(R.string.app_name);

-----AndroidManifest.xml中用 
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="org.zhaojin"
      android:versionCode="1"
      android:versionName="1.0.0">
<application android:icon="@drawable/ic_launcher" 
				android:label="@string/app_name"> //对如果其下级<activity 没有设置的label的,使用这个做为默认
	 <activity android:name=".MainActivity"     //.表示在当前包下的类名,可以省略".",如在当前子包下不能省略".",如.main.MainActivity
		 <category android:name="android.intent.category.LAUNCHER" /> //要出现在启动程序列表中

<TextView    
    android:layout_width="fill_parent"   
    android:layout_height="wrap_content"   
    android:text="@string/hello" 
    /> 

-----string.xml中  values
<resources>
    <string name="hello">Hello World, MyActivity!</string>



-----main.xml中 layout
 <Button 
     android:id="@+my/you"  //会生成R.my.you //my是内部类




setContentView(R.layout.main);  
Button button = (Button) findViewById(R.id.button1);  
button.setOnClickListener(start);
OnClickListener start =new OnClickListener()  
 {  
	 public void onClick(View v)  
	 {     
		 startService(new Intent("com.yarin.Android.MUSIC"));  //对应于<service中
		// Activity01.this.startService(new Intent("com.yarin.Android.MUSIC"));  
	 }  
 };  



<service android:name=".MusicService"> 
             <intent-filter> 
                 <action android:name="com.yarin.Android.MUSIC" />  //对应于代码中
                 <category android:name="android.intent.category.default" /> 
             </intent-filter> 
 </service>     



 public class MusicService extends Service  
 {
	//也可重写onCreate方法
	 public void onStart(Intent intent, int startId)  
     {  
		 super.onStart(intent, startId);  
         MediaPlayer player = MediaPlayer.create(this, R.raw.test);//raw目录下的test.mp3  
         player.start();  
		
     }  
  
     public void onDestroy()  
     {  
        super.onDestroy();  
        player.stop(); 
		//player.pause();
	   //player.seekTo(sec);
       //player.getDuration();
       //player.getCurrentPosition();
     }  
	 //onStart->onResume  onPause->onStop->onDestroy  onRestart
 }
player= new MediaPlayer();
player.setDataSource(Environment.getExternalStorageDirectory()+"/ring.mp3");//从文件系统读
player.prepare();//缓冲 //MediaPlayer.create()的方式不要再调用prepare()

mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//构造器实例化MediaPlayer时这样调用
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
 				mediaPlayer.start();
			}
		});
		
player.reset();

 
<intent-filter> 表示描述Activity 能做什么事

Activity如果不是前台系统会把它停止

失去了焦点(能看见)是pase状态,如内存不够用,会kill
如看不见是stop状态,
onCreate... onStart->onResume-> .. ->onPause->onStop ...onDestroy()
onPause->onStop  ... onRestart->onStart->onResume
onPause->onResume

Uri uri=Uri.parse("http://www.baidu.com");
Intent i=new Intent(Intent.ACTION_VIEW,uri);//使用系统的浏览器
startActivity(i)

在Activity中传数据用Bundle

Bundle bundle=new Bundle();
Intent intent=new Intent();
bundle.putString("key","value");
intent.putExtras(bundle);
//或者用 intent.putExtra("username","张三");
intent.setClass(Curr.class,To.class);
startActivity(intent);


String a=null;
a+="a";//结果是nulla
//取数据 
Bundle bundle=this.getIntent().getExtras();
bundle.getString("key");//用完应该删的吧,存放其它数据,size(),clear();

如是intent.putExtra("username", "张三");也用上的取数据



 <application
   <provider  android:name=".PersonProvider" android:authorities="this.is.a.id"></provider>
   
   	URI 由content://< android:authorities="this.is.a.id">/<路径/<ID>> 
	
 extends ContentProvider 自己的应用可以向其它应用提供数据的查询,也可修改本应用数据

 实现getType时,如果Uri表示操作的是集合类型,返回以"vnd.android.cursor.dir/"开头,
		如是非集合数据返回"vnd.android.cursor.item/"开头  ,可以参考StructuredName.CONTENT_ITEM_TYPE

this.getContext().getContentResolver().query(uri,...)来仿问ContentProvider
Uri a=ContentUris.withAppendedId(uri, 2);//在原路径下加 /2

this.getContext().getContentResolver().notifyChange(uri, null);//在Provider中如有改变可以发生通知

对uri监听的都会知道
this.getContext().getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler()));//MyContentObserver extends ContentObserver 重写onChange方法

联系人的修改要加权限
 <manifest
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
//联系人存放在/data/data/com.android.providers.contacts/databases/contacts2.db文件中的raw_contacts和data表中
ContentResolver contentResolver = getContentResolver();    
Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);  //能得到ID,和名字,因只查raw_contacts表
while(cursor.moveToNext())   
{   
	 int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);//或者使用PhoneLookup.DISPLAY_NAME       
	 String contact = cursor.getString(nameFieldColumnIndex);   
	 
	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
	Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, //data表为电话的记录
					  null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);  
	String allNumber="";
	while(phones.moveToNext())
	{
		String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		allNumber=allNumber+phoneNumber+",";
	}
	phones.close();

	String allEmail="";
	Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,  
							 ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);  
	while (emails.moveToNext())
	{  
		String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		allEmail=allEmail+email+",";
	} 
	emails.close();

	string += ( contact+":number="+allNumber+"Email="+allEmail+"\n");  
}  
cursor.close();  
		 
增加联系人
import android.provider.ContactsContract.Contacts.Data;//注意有两个Data

ContentValues values = new ContentValues();
//首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId 
Uri rawContactUri = this.getContext().getContentResolver().insert(RawContacts.CONTENT_URI, values);
long rawContactId = ContentUris.parseId(rawContactUri);
//往data表入姓名数据
values.clear();
values.put(Data.RAW_CONTACT_ID, rawContactId); 
values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);//内容类型
values.put(StructuredName.GIVEN_NAME, "李天山");
this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
//往data表入电话数据
values.clear();
values.put(Data.RAW_CONTACT_ID, rawContactId);
values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
values.put(Phone.NUMBER, "13921009789");
values.put(Phone.TYPE, Phone.TYPE_MOBILE);
this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
//往data表入Email数据
values.clear();
values.put(Data.RAW_CONTACT_ID, rawContactId);
values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
values.put(Email.DATA, "liming@itcast.cn");
values.put(Email.TYPE, Email.TYPE_WORK);
this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		
		
		
		
//批量添加,处于同一个事务中
//文档位置：reference\android\provider\ContactsContract.RawContacts.html
ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
int rawContactInsertIndex = ops.size();
ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
		.withValue(RawContacts.ACCOUNT_TYPE, null)
		.withValue(RawContacts.ACCOUNT_NAME, null)
		.build());
//文档位置：reference\android\provider\ContactsContract.Data.html
ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
		.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
		.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
		.withValue(StructuredName.GIVEN_NAME, "赵薇")
		.build());
ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
		 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
		 .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
		 .withValue(Phone.NUMBER, "13671323809")
		 .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
		 .withValue(Phone.LABEL, "手机号")
		 .build());
ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
		 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
		 .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
		 .withValue(Email.DATA, "liming@itcast.cn")
		 .withValue(Email.TYPE, Email.TYPE_WORK)
		 .build());

ContentProviderResult[] results = this.getContext().getContentResolver()
	.applyBatch(ContactsContract.AUTHORITY, ops);
for(ContentProviderResult result : results)
{
	Log.i("ContactChangeTest", result.uri.toString());
}		
		
		
		
		
		 
//查出所有的电话本记录
Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);//能得到ID,和名字,因只查raw_contacts表
startManagingCursor(c); //过时
ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, c, 
			  new String[] {PhoneLookup.DISPLAY_NAME, PhoneLookup.NUMBER }, //
			  new int[] { android.R.id.text1, android.R.id.text2 }
			,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER 
			  );
setListAdapter(adapter);//extend ListActivity



CallLog.Calls.CONTENT_URI;
Uri.withAppendedPath();
Cursor cur=managedQuery(uri,null,null,null,null);



setTitle("");//页面的标题

父Activity重写 onActivityResult(request_code,result_code,data);
//表示当这个Acitivity开其它的子Activity,当子Activity结束时finish()调用这个方法
//request_code用来区分哪个子,表示执行的结果是什么

startActivityForResult(inent,request_code);//自己不finish(),等子的结果,request_code这里自己区分哪个Activity

子Activity
setResult(RESULT_OK,intent);//resultCode,子Activity向父Activity传执行结果和数据
finish();//结束子,返回到父 ,

getIntent().getExtras()//一直保存在应该中,clear(),放值在debug时,一个变量Map是null,用代理,取值不是null


 manifest.xml中<action android:name="android.intent.action.MAIN" />  代表开始应用的第一个窗口

<TextView
<EditText		拖选(显示未选择但要做)->双击->有菜单->输入法
<CheckBox
	isChecked()
	getText()


<RadioGroup		checkedButton="@+id/lunch" >   这里有+
	<RadioButton
            android:text="breakfast"
            android:id="@+id/breakfast"
            />
	<RadioButton
            android:text="lunch"
            android:id="@id/lunch" />		这里没有+
</RadioGroup>
	clearCheck()//清除选择的

<Switch
        android:textOn="打开"
        android:textOff="关闭" 
        android:text="开关"
		/>
 
<ToggleButton
        android:textOn="打开" 
        android:textOff="关闭"
  />
aspnCountries = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new ArrayList<String>());//要封装的数据
aspnCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner_c.setAdapter(aspnCountries);

ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);//界面中没有单选按钮的,界面小,不会自动选中上次的值
R.array.countries是values/arrays__.xml中,values下与文件名没有关系就是array可以对应<string-array
	<string-array name="countries">
        <item>China2</item>

mAdapter=ArrayAdapter.createFromResource(this, R.array.Planets,android.R.layout.simple_spinner_dropdown_item);// 界面中有单选按钮的,界面大,会自动选中上次的值
    <string-array name="Planets">
        <item>Mercury</item>

 ----------net
spinner.setAdapter(mAdapter);
spinner.setOnItemSelectedListener(new OnItemSelectedListener()
	{
	        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) 
			{
				 SpinnerActivity.this.mSelection = parent.getItemAtPosition(pos).toString();
				 //仿问外部类
			}
	});

---- 新版本Android Studio Junit 测试项目	
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class) //如加这个，运行时会启动模拟器安装apk
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("org.zhaojin.thejunit", appContext.getPackageName());
    }
}





---- 老版本ADT测试项目
    <application>
        <uses-library android:name="android.test.runner" />
    </application>

  <instrumentation android:name="android.test.InstrumentationTestRunner"
      android:targetPackage="com.example.android.snake"
      android:label="Snake sample tests">
  </instrumentation>  
  <instrumentation android:targetPackage= 必须 和 开头的<manifest package= 相等
其它包里的类只要extends AndroidTestCase都可以被测试,类中的方法必须以test开头
  
package com.example.android.snake
 extends ActivityInstrumentationTestCase2<Snake> 
---- 

layout.xml中
	<com.example.android.snake.SnakeView ( extends TileView)

<activity android:name="Snake"
        android:theme="@android:style/Theme.NoTitleBar"
        android:screenOrientation="portrait"
        android:configChanges="keyboardHidden|orientation">



<android:typeface="serif"    //serif截线,sans 无 ,monospace单间隔

android:textSize="16sp"	 //单位 文字用sp(scaled比例像素),dip(device independent设备独立像素),px, 不常用的有pt(point),in(inches),mm
android:padding="10dip"  
android:textColor="#ffffff" 


---------------
adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, );//String[] 一维数组
COUNTRIES

<AutoCompleteTextView 		setAdapter(adapter)  

<DatePicker  DatePicker  init(2009, 5, 17, null);
	init(int year, int monthOfYear, int dayOfMonth, DatePicker.OnDateChangedListener onDateChangedListener)
	
<TimePicker  TimePicker  tp.setIs24HourView(true);//24小时显示
	setOnTimeChangedListener (TimePicker.OnTimeChangedListener 

<AnalogClock 带长短针的

<DigitalClock

<Chronometer 计时器
	start(),stop(),
	chrno.setBase(SystemClock.elapsedRealtime());
	chrno.setFormat("格式化后的为:%s");//%s
	chrno.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
		@Override
		public void onChronometerTick(Chronometer chronometer) {
			Log.d("TimePickerActivity",chronometer.getText().toString());//是格式化后的字串
		}
	});
	
<ProgressBar	//默认是圆形进度条,有动画的
 style="?android:attr/progressBarStyleSmall 
  style="?android:attr/progressBarStyle
<ProgressBar 
        style="?android:attr/progressBarStyleHorizontal"  //水平进度条样式 @android:style/Widget.ProgressBar.Horizontal
        android:layout_width="200dip"
        android:max="100"
        android:progress="50"
        android:secondaryProgress="75" />
	
滑杠   
<SeekBar 
        android:max="100" 
        android:thumb="@drawable/seeker"//是拖动滑杠的小按钮图标
        android:progress="50"/>

<RatingBar   ratingBarStyleSmall="true" /> 五角星，可评级，可选半个，默认5个星
<ImageView android:src="@drawable/eoe"  //png,jpg,gif
<ImageButton   android:src="@drawable/play"
---
<FrameLayout 标签
	<TextView android:id="@+id/view1"
TabHost tabHost = getTabHost();
LayoutInflater.from(this).inflate(R.layout.tab_demo,tabHost.getTabContentView(), true);
tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(R.id.view1));

---
<TabHost>
	<TabWidget  android:id="@android:id/tabs"/>
	<FrameLayout android:id="@android:id/tabcontent">
		  <LinearLayout  android:id="@+id/page1">
		  ...
		  </LinearLayout>
		 <LinearLayout  android:id="@+id/page2">
		  ...
		  </LinearLayout>
	</FrameLayout>
</TabHost>

TabHost tabHost = (TabHost) this.findViewById(R.id.myTabhost);
tabHost.setup();//会去读 android:id="@android:id/tabcontent" , android:id="@android:id/tabs"

TabSpec spec1=tabHost.newTabSpec("tag1");
spec1.setIndicator("第一页",getResources().getDrawable(R.drawable.ic_launcher));//标题一起的图标,minSdkVersion="15" 时无效果
//pec1.setIndicator(View xxx);//可以自定义drawable背景,<selector  ><item android:state_pressed="true"
spec1.setContent(R.id.page1);
tabHost.addTab(spec1);
tabHost.setCurrentTab(0);

---

<ImageSwitcher		//显示大图片
              android:layout_alignParentTop="true"
              android:layout_alignParentLeft="true" />

 
Activity 的  requestWindowFeature(Window.FEATURE_NO_TITLE);//窗口无标题

ImageSwitcher
	.setFactory(this); //ViewSwitcher.ViewFactory实现makeView方法 
	public View makeView() 
	{
			ImageView i = new ImageView(this);//Context
			i.setBackgroundColor(0xFF000000);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  // FILL_PARENT 过时的
	
		   return i;
	}

	.setImageResource(int res);
	.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));//Context
	.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
 

ImageView
	i.setImageResource(
	i.setBackgroundResource(
	i.setAdjustViewBounds(true);
	i.setPadding(8, 8, 8, 8);
---
<GridView
    android:numColumns="auto_fit"
    android:verticalSpacing="10dp"
    android:horizontalSpacing="10dp"
    android:columnWidth="90dp"
    android:stretchMode="columnWidth"
    android:gravity="center"
/> 
i.setLayoutParams(new GridView.LayoutParams(85, 85));//w,h




//模拟器不能振动
---Notification ,可拖任务栏查看
mNotificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
PendingIntent pending = PendingIntent.getActivity(this, int request_code,new Intent(this, ActivityMain.class), int flag);

Notification notification = new Notification.Builder(this)
        .setContentTitle("通知栏的标题")
        .setContentText("通知栏的文本")
        // .setLargeIcon( )
        .setSmallIcon(R.drawable.face_1)
        .setContentIntent(contentIntent)//跳到intent
        .build();
		
// 100ms延迟后，振动250ms，停止100ms后振动500ms
notification.vibrate = new long[] { 100, 250, 100, 500 };

notification.defaults=Notification.DEFAULT_SOUND;
					Notification.DEFAULT_VIBRATE
					Notification.DEFAULT_ALL

mNotificationManager.notify(int id, notification);
mNotificationManager.cancel(int id);

---toast,弹出提示, 过后就不能再看了
<FrameLayout android:background="@android:drawable/toast_frame" //自带的

LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View view=vi.inflate(frameLayout_id, null);//inflate使充气(于轮胎、气球等), (使)膨胀

Toast toast = new Toast(this);
toast.setView(view);
toast.setDuration(Toast.LENGTH_SHORT);//Toast.LENGTH_LONG
toast.show();


<RelativeLayout
	<EditText android:background="@android:drawable/editbox_background"  //自带的
			  android:layout_below="@id/label" //对RelativeLayout ,在指定的下面
			  android:layout_alignParentRight="true"
			  android:layout_toLeftOf="@id/cancel"  //指定自己在cancel的左边
			  android:layout_alignTop="@id/cancel"
			  android:layout_marginLeft="10dip"  //也有px单位,android:textSize="22sp"

LinearLayout layoutMain = new LinearLayout(this);
layoutMain.setOrientation(LinearLayout.HORIZONTAL);
setContentView(layoutMain);

RelativeLayout layoutLeft = (RelativeLayout) inflate.inflate(R.layout.left, null)//什么时候要inflate????

<TableLayout  >
		<TableRow>
			<EditText  android:password="true"   //如只可以数字 android:numeric="integer"
AndroidManifest.xml中
<activity android:name=".ActivityLayout"
			android:label="演示混合Layout布局"> //如是MAIN的就Activity,这个是应用的标题,也是标题栏的内容
</activity>

动态生成布局  ,有的布局是固定的(在XML文件中),有的要手工编码动态改变
LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
RelativeLayout layoutLeft = (RelativeLayout) inflate.inflate(R.layout.left, null);//返回View,把配置文件的固定界面转换为类View,可调用addView
RelativeLayout layoutRight = (RelativeLayout) inflate.inflate(R.layout.right, null);

RelativeLayout.LayoutParams relParam = new RelativeLayout.LayoutParams(
		RelativeLayout.LayoutParams.WRAP_CONTENT, //w
		RelativeLayout.LayoutParams.WRAP_CONTENT);//h

linearLayout.addView(layoutLeft, 100, 100);//View,w,h
linearLayout.addView(layoutRight, relParam);




listView = new ListView(this);
listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, new String[]{}));	
												  android.R.layout.simple_list_item_1 //如是这个,下面的CHOICE_MODE_MULTIPLE没有效果的

listView.setItemsCanFocus(true);
listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


 SimpleAdapter adapter = new SimpleAdapter(this, List<Map<String, Object>> data,//自定两列表格
				R.layout.list_item,  //每一行是这个视图,
				new String[] { "姓名", "性别" },//从data的key值是"姓名"的取出值放入下面组件中view1,"性别"放view2
				new int[] {	R.id.mview1, R.id.mview2 }//是list_item视图中组件 ____系统的android.R.id.text1和android.R.id.text2
				);
				
	可动态修改
Map<String, Object> newRow=new HashMap<String, Object>();
newRow.put("姓名",“张”);
newRow.put("性别",“男”);
listData.add(newRow);
listAdapter.notifyDataSetChanged();
		    			
//android.jar包中XML的不是纯文本,可以用eclipse打开
	android.R.layout.simple_list_item_1 //是一列的 ,使用时用android.R.id.text1 (源码是android:id="@android:id/text1")
	android.R.layout.simple_list_item_2 //是两列的,
 

SimpleCursorAdapter 使用像 SimpleAdapter 传Cursor做为数据,并要求Cursor必须有"_id"结果列,原因是SQLite建议表的主键列名使用"_id"

listView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) 
			{
				setTitle(parent.getItemAtPosition(position).toString());//是Map(SimpleAdapter中的List<Map<String, Object>>), 这里parent是ListView
				//parent.getItemAtPosition(position)//如果SimpleCursorAdapter,返回的是Cursor,定位到position
			}	
		});



extends ListActivity 	覆盖onListItemClick
setListAdapter( )

<ListView android:id="@id/android:list"		//系统的名字,如果数据为空会找@id/android:empty(ListActivity中做的)
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"/>
<TextView android:id="@id/android:empty"	//系统的名字
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:text="对不起，没有数据显示"/>  

listView.setOnItemSelectedListener(itemSelectedListener);//上下键来选择时,

----Menu
按手机上的menu菜单时
重写Activity的onCreateOptionsMenu,onOptionsItemSelected

public boolean onCreateOptionsMenu(Menu menu)//只第一次创建,按手机上的menu菜单时,编码菜单
{
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST, 0, "显示button1");//显示在屏幕的最下方中间的n行
		menu.add(0, Menu.FIRST+1, 0, "显示button2");//第一个参数是groupId,第三个是Order

		MenuItem item=menu.findItem(Menu.FIRST+1);
		//item.setIcon(icon) //可以设置menu按钮的背景
		return true;
}

public boolean onMenuItemSelected(int featureId, MenuItem item) {  //按手机上的menu菜单时 响应 第1次
		return super.onMenuItemSelected(featureId, item);//会响应 onOptionsItemSelected  
}
public boolean onOptionsItemSelected(MenuItem item)  //按手机上的menu菜单时 响应 第2次
{
	switch (item.getItemId()) 
	{
	case Menu.FIRST: 
		button1.setVisibility(View.VISIBLE);//INVISIBLE
		break;
	case Menu.FIRST+1: 
		break;

	}
	return super.onOptionsItemSelected(item);//可以return true
}
--xml 定义菜单
public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem item=menu.findItem(R.id.menu_item1);
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(XMLMenu.this, "被单击了菜单项__"+item.getTitle(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
}

res/menu目录
main.xml
<menu xmlns:android="http://schemas.android.com/apk/res/android" >
	<item android:id="@+id/menu_item1" 
	    	android:title="@string/button1" 
	    	android:icon="@drawable/ic_launcher"
	    	android:showAsAction="always"/> <!-- 显示在顶部区,不显示在菜单中 -->
	
	<item android:id="@+id/file"
	        android:title="文件" > 
	        <menu><!--  只可一级的子菜单 -->
		  	 <item android:id="@+id/menu_sub_item1" android:title="@string/button1" android:onClick="myOnSubMenuItemClicked"/> onClick 会覆盖 onOptionsItemSelected 
	         </menu>
	</item>
</menu>
public void myOnSubMenuItemClicked(MenuItem item)
{
}
	
---上下文菜单 

onCreate 方法中要 registerForContextMenu(listView);	
 
//  要调用 super.onMenuItemSelected

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) //基于 GridView或ListView 的右像菜单 
	{
		getMenuInflater().inflate(R.menu.context, menu);
		super.onCreateContextMenu(menu, v, menuInfo); 
	}
	 @Override
	public boolean onContextItemSelected(MenuItem item) {//基于 GridView或ListView 的右像菜单 
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		String value=adapter.getItem(info.position);
		switch (item.getItemId()) {
		case R.id.context_item1:
			Toast.makeText(XMLMenu.this, "ContextItemSelected 被单击了菜单项__"+value, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

---弹出菜单
<Button android:id="@+id/btn_popupMenu"
		android:text="点击弹出菜单"
		android:onClick="showPopup" />  
	public void showPopup(View v) {  //菜单位于按钮附近
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.popup, popup.getMenu());
	    popup.show();
	}
--
private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.action_mode, menu);
	        return true;
	    }
	    // Called each time the action mode is shown. Always called after onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.action_menu_item1:
	            	Toast.makeText(ActionModeMenu.this, "Action Mode 被单击了菜单项__"+item.getTitle(), Toast.LENGTH_SHORT).show();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	        }
	    }
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	        mActionMode = null;
	    }
	};
	
	
	 listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					 int position, long id) 
			{
				if (mActionMode != null) {
		            return false;
		        }
		        mActionMode =startActionMode(mActionModeCallback);//长按ListView的item时在标题区显示菜单
		        listView.setSelected(true);
		        return true;
			}
		});
		
		
		
		

<ScrollView 可以拖动的

<activity android:theme="@android:style/Theme.Holo.Dialog" >


FragmentTransaction ft = getFragmentManager().beginTransaction();
Fragment prev = getFragmentManager().findFragmentByTag("My_Dialog");
if (prev != null) {//现在这个(标识)对放框是否已经显示,如是删它
	ft.remove(prev);
}
ft.addToBackStack(null);

//-------
DialogFragmentMain newFragment = new DialogFragmentMain();//自己的类
Bundle args = new Bundle();
args.putInt("num", mStackLevel);
newFragment.setArguments(args);
newFragment.show(ft, "My_Dialog");
 class DialogFragmentMain extends DialogFragment 
 {
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        int mNum = getArguments().getInt("num");
        Log.i("DialogFragmentMain","(mNum-1)%6="+(mNum-1)%6);

        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;//背景透明
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;//背景透明,可以点后面的按钮
            case 4: style = DialogFragment.STYLE_NORMAL; break;//进入单独Activity,不是浮动的
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_text_entry, container, false);
        return v;
    }
}


DialogAlertFragmentMain newFragment = new DialogAlertFragmentMain();//自己的类
Bundle args = new Bundle();
args.putInt("title", R.string.alert_dialog_two_buttons_title);
newFragment.setArguments(args);
newFragment.show(getFragmentManager(), "11111112222222dialog");

class DialogAlertFragmentMain extends DialogFragment 
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setMessage(R.string.alert_dialog_two_buttons2_msg)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	  Log.i("DialogAlertFragmentMain", "Positive click!");
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	  Log.i("DialogAlertFragmentMain", "Negative  click!");
                        }
                    }
                )
                .create();
    }
}
##过时的(使用DialogFragment)  showDialog(id)自定义标识ID,会调用当前Activity的OnCreateDialog(id)后再调onPrepareDialog(int id, Dialog dialog),要重写

OnCreateDialog方法中
AlertDialog.Builder builder = new AlertDialog.Builder(this);//Context
builder.setIcon(res);
builder.setTitle(res);
builder.setMessage(res);
builder.setPositiveButton(res,);//确定
	  //setNegativeButton(...)//取消
	  //setNeutralButton  //中立的
builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            					public void onClick(DialogInterface dialog, int whichButton)
	            				{
            						if( AlertDialog.BUTTON_POSITIVE == whichButton )
            							Log.w(TAG, "确定 is clicked");  
	            				}
            				});
builder.setView(要经过inflater.inflate才是View)//就不用Message,对可输入框等复杂情况
builder.create()返回Dialog ;

builder.show();

<EditText android:textAppearance="?android:attr/textAppearanceMedium" />

ProgressDialog dialog = new ProgressDialog(context);//圆形动画






SharedPreferences p =Context c.getSharedPreferences("filename", MODE_WORLD_READABLE);//不存在创建
																Context.MODE_PRIVATE 
String name = p.getString("key", "default");//取
p.edit()
	.putString("key","newValue")//存
	.commit();

SharedPreferences.Editor e = p.edit();
e.putInt("one",1);
e.commit()
p.getInt("x",defaultVal);
p.contains("x");
保存在 /data/data/<package name>/shared_prefs/<fileName>.xml   文件明文的,内容是
<map>
	<string name="key">newValue</string>
</map>
//如果要仿问其它的项目下的文件(但其它项目必须是 MODE_WORLD_READABLE 才可以仿问),使用
Context otherContext=getContext().createPackageContext("package.name",Context.CONTEXT_IGNORE_SECURITY)
//也可以使用Activity的getPreferences(mode)来得到SharedPreferences,文件名是Activity的类名

extends SQLiteOpenHelper//abstract 的,构造时可传数据名,如是null是在内存中
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	public void onCreate(SQLiteDatabase db) 
	{
	
		db.execSQL("CREATE TABLE _mytable( _id text not null,_name text not null);");//SQLite自增使用autoincrement关键字
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
			db.execSQL("DROP TABLE IF EXISTS _mytable");//也可以update,delete,insert
			onCreate(db);
	}
	//DML可以使用?,动态传参 

SQLiteDatabase db= SQLiteOpenHelper  .getWritableDatabase(); //.getReadableDatabase();
					.open();
					.close();//会关闭所有的打开的数据库
ContentValues v = new ContentValues();
		v.put("_id", "");//数据库字段名,值
		v.put("_name", "");	
db.insert("_mytable", null, v);

db.delete("_mytable", "_name = 'lisi'", null);//args
db.update("_mytable", v,"_name = 'lisi'", null);
Cursor cur =db.query(...) //db.rawQuery("select ... ?",arg)
cur.getCount();
cur.moveToFirst();
cur.getInt(cur.getColumnIndex("_id"));
cur.close();

db.setTransactionSuccessful();
db.beginTransaction();
db.endTransaction();


据库文件位于/data/data/你的程序的包名/databases/中,名字是构造SQLiteOpenHelper时传的

--ContentProvider

--
sendBroadcast("MY_BROADCAST");//广播出去

AndroidManifest.xml中 注册Receiver
<receiver android:name="MyBroadcastReceiver"> 
	<intent-filter>
		<action android:name="MY_BROADCAST"/>
	</intent-filter>
</receiver>

MyBroadcastReceiver extends BroadcastReceiver
	public void onReceive(Context context, Intent intent)
	{
	}




 <service  android:name=".TestService" /><!-- android:process=":remote" android:enabled="true" ????????? -->
 如果配置为 android:process=":remote",则在onServiceConnected方法中是BinderProxy
 
TestService extends Service 
{
	public void onCreate(){}
	
	public IBinder onBind(Intent i){}//返回的Bunder是用来向Activity提供Service中的方法调用.
	public boolean onUnbind(Intent i) {}
	public void onRebind(Intent i){}
	
	public void onStart(Intent intent, int startId){}
	
	public void onDestroy(){}
}
startService( new Intent(this, TestService.class)); // onCreate->onStart 
stopService(new Intent(this, TestService.class));//onDestroy 
//Service自杀stopSelf();//会调用 onDestroy()

onRebind//记录日志

onCreate->onBind ... onUnbind->onDestroy //如是bind

MyServiceConnection extends  ServiceConnection() 
 {
	public void onServiceConnected(ComponentName className, IBinder binder) {} //会先调用Service的onBind方法返回Binder,为了调用Service中的方法
	public void onServiceDisconnected(ComponentName className) { }  
}
MyServiceConnection conn=new MyServiceConnection();
Intent intent = new Intent(this, TestService.class);
bindService(intent,conn,Context.BIND_AUTO_CREATE);//bind时create 服务

unbindService(conn);

Toast.makeText(MyActivity.this, "Service connected", Toast.LENGTH_SHORT).show(); //简单的Toast


Intent intent = new Intent(MainActivity.this, MainReceiver.class);//没有用sendBroadcast可以发给注册过的Receiver,<receiver android:name=".MainReceiver">
PendingIntent p_intent = PendingIntent.getBroadcast(MainActivity.this, requestCode, intent, 0);
AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),	p_intent);//RTC(Real-Time Clock)
am.cancel(p_intent)


context.startService(new Intent(context, NotifyService.class));


AlertDialog alert = builder.create();
alert.setMessage("");
alert.setButton("登陆", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				alert.dismiss();//把当前对话框消失
			}
		});
alert.show();




SAX解析XML
SAXParserFactory factory = SAXParserFactory.newInstance();
SAXParser parser = factory.newSAXParser();
XMLReader xmlreader = parser.getXMLReader();

xmlreader.setContentHandler(extends DefaultHandler());

URL url = new URL(urlString);
InputSource is = new InputSource(url.openStream());    
xmlreader.parse(is);


中国联通实现3G的是 WCDMA,世界上大部分使用,中国电信3G的实现是CDMA2000,日朝北美使用,中国移动 TD-SCDMA自由知识产权
2.5G手机使用GPRS

AVD 中浏览器中双击放大页面

@string/hello 除了国际化,也是为了对大篇幅的文字节省内存

AVD的标题栏上的数字5554可以当做是手机号码,另一个AVD显示的电话号码15555215554,多个AVD可以互拔号,会监听那个端口
在文本输入框中输入号码 进行拔号
Intent intent = new Intent();
//intent.setAction("android.intent.action.CALL"); //查看源码时<action android:name="android.intent.action.CALL" >
intent.setAction(Intent.ACTION_CALL);
intent.setData(Uri.parse("tel:15555215554"));
或者用 new Intent(Intent.ACTION_CALL,Uri.parse("tel:15555215554"));
startActivity(intent);

Manifest.xml中配置
<uses-permission android:name="android.permission.CALL_PHONE"/>  //查看源码时<activity android:permission="android.permission.CALL_PHONE"
"android.permission.CALL_PHONE"记录在 anroid.Manifest.permission. 包中.CALL_PHONE常量的值中

真实手机接入电脑后,使用一个工具(screenmonitor\asm.jar 只能看,不能操作),可双在电脑的显示器中看到真实手机的屏幕
使用前要增加 platform-tools目录到PATH环境变量中

文件读写
Environment.getExternalStorageDirectory();//得到SD卡的目录
Context context=this.getContext();
FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE|Context.MODE_APPEND);
FileInputStream inStream = context.openFileInput(filename);//filename是对/data/<package>/files/目录中的文件 

//判断sdcard是否存在于手机上
if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))

PullXML
解析
XmlPullParser parser = Xml.newPullParser();
parser.setInput(inputStream, "UTF-8");
int eventType = parser.getEventType();
while (eventType != XmlPullParser.END_DOCUMENT) 
{
	switch (eventType) {
		case XmlPullParser.START_DOCUMENT:
		case XmlPullParser.START_TAG:
		break;
		case XmlPullParser.END_TAG:
		break;
	}
	eventType = parser.next();
}
生成
XmlSerializer serializer=Xml.newSerializer();
serializer.setOutput(out,"UTF-8");
serializer.startDocument("UTF-8",true);
serializer.startTag(null, "persons");

serializer.startTag(null, "person");
serializer.attribute(null,"id",Integer.toString(p.getId()));
serializer.startTag(null, "name").text(p.getName()).endTag(null, "name");
serializer.endTag(null, "person");

serializer.endTag(null, "persons");
serializer.endDocument();
//输出的是没有换行的格式



访问网格要加权限 
<uses-permission android:name="android.permission.INTERNET"/>
java.net.URL对象.openConnection().setRequestMethod("GET")
 Android内部对字符使用UTF-8,要URLEncoder.encode("UTF-8"),Tomcat使用iso8859-1编码
如URLConnection为"POST",还要调用setDoOutput(true);getOutputStream()来传送参数

android.graphics.Bitmap
Bitmap map=BitmapFactory.decodeByteArray(data, offset, length);
bitmap.compress(CompressFormat.JPEG, 100, outStream);//压缩输出,写出到文件

TextView没有滚动条,在<TextView>外部 加一个<ScrollView>就可以了

new org.json.JSONArray(String"[{},{}]").length(),有.getJSONObject(i),就可以用getInt(""),或getString("");

//JDK
URL url=new URL("http://127.0.0.1:8080/test/index.jsp");
HttpURLConnection conn=(HttpURLConnection)url.openConnection();
conn.setRequestMethod("POST");
conn.setDoOutput(true);
conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
String param="name="+URLEncoder.encode("张三","UTF-8")+"&age=25" ;
byte[] data=param.getBytes();
conn.setRequestProperty("Content-Length",Integer.toString(data.length));
OutputStream output=conn.getOutputStream();
output.write(data);
output.flush();
output.close();
int response=conn.getResponseCode();

//HttpClient
BasicNameValuePair pair=new org.apache.http.message.BasicNameValuePair ("name","李四");
List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
param.add(pair);
UrlEncodedFormEntity formEntity=new UrlEncodedFormEntity(param,"UTF-8");
HttpPost post=new HttpPost("http://127.0.0.1:8080/test/index.jsp");
post.setEntity(formEntity);
DefaultHttpClient client=new DefaultHttpClient();
HttpResponse response=client.execute(post);
int code=response.getStatusLine().getStatusCode();

//https:和 Cookie使用 HttpClient


文件上传浏览器的请求的HTTP头 
Content-Type:multipart/form-data;boundary=<--123>
Content-Disposition:form-data;name="formname"; filename="d:/aa.xml"

体中的每部分的分隔字符要比HTTP头中声明的部分前面多"--",在最后也是以"--"结束的

contentType   "application/octet-stream" 二进制
contentType   "application/soap+xml;charset=utf-8" WebService

Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);	       
OutputStream outStream = socket.getOutputStream();
socket.getInputStream()


多线程断点续传下载
HttpURLConnection 
	getContentLength();
	setRequestProperty("Range","bytes=2048-4096");//设置HTTP请求头,从指定位置下载
RandomAccessFile   ("file.zip","rwd")//d不缓存,直接写入数据
	setLength();

UI线程使用ThreadLocal中存了一个Queue
UI界面中的数据修改,只有UI线程的修改,UI线程才会更新界面,如果是其它线程修改界面的数据,界面不能被更新

Handler handler=new Handler ()//写在主线程中类的变量,会把当前线程的消息队列 关联到 自己的父线程的消息队列(UI线程)
{
	//重写
	public void handleMessage(Message msg)//UI线程取出消息,执行这里的代码,即是在UI线程中执行的, Handler handler_cc=msg.getTarget();
	{
		msg.obj;//是obtainMessage(id,obj);放入的
	}
};

//以下写在子线程中
Message msg=new Message();//msg=handler.obtainMessage(id,obj);//建立消息的两种方式
msg.getData().putString("key",val);
msg.what=1;//为了有多个消息在handleMessage方法中区分
msg.obj="消息关联的其它信息";
handler.sendMessage(msg);//发送到UI线程的消息队列

//第二种写法
Message empMsg=handler.obtainMessage(2);//.sendTarget();
empMsg.sendToTarget();

handler.removeMessages( xx.what);

上传的断点续传使用Socket  自已定义规则
socket.getInputStream() 用于发送请求后,再接受响应

pushbackInputStream.read(array)//PushbackInputStream
pushbackInputStream.unread(array,1,1);  //PushBack缓冲区没有满，就可以使用unread()将数据推回,待下一次可以重新读取

//显示意图,知道哪个Activity类名
Intent intent = new Intent();  
intent.setClass(Activity01.this, Activity02.class); //方法一

Intent intent1=new Intent(Activity01.this,Activity02.class);//方法二

Intent intent2=new Intent();
intent2.setComponent(new ComponentName(Activity01.this,Activity02.class));//方法三

//隐式意图,implicit,explicit明确的,看不到Activity
Intent intent = new Intent();  
//intent.setAction("my.intent.Example");
intent.setAction("my.intent.AnotherExample");//任何一个就OK
intent.addCategory("my.category.AnotherExample");
//intent.setData(Uri.parse("myrmi://localhost:8080/test"));
//intent.setType("image/gif");//会清除前面调用setData()
intent.setDataAndType(Uri.parse("myrmi://localhost:8080/test"), "image/gif");
startActivity(intent);//系统会增加 android.intent.category.DEFAULT

<intent-filter> 
	 <action android:name="my.intent.Example" />
	 <action android:name="my.intent.AnotherExample" /> 
	 <category android:name="android.intent.category.DEFAULT" />
	 <category android:name="my.category.AnotherExample" /> 
	 <data  android:scheme="myrmi" android:host="localhost"/>
	 <data  android:mimeType="image/gif"/>
</intent-filter> 	
	
<intent-filter>下可有多个<action>和<category> 只要有一个 <action>和<category>对应就OK


 <activity android:name="Activity03" android:theme="@android:style/Theme.Dialog">

在DDMS视图中,Emulator Control 中Incoming number:中输入电话号码:5554,选中Voice是电话,SMS短信,再单击"call"

Activity的onSaveInstanceState(Bundle) 和  onRestoreInstanceState(Bundle),
当这个Activity运行,如果有突然的电话接入,系统内在不足是可能会杀死这个Activity,
为防止数据丢失,重写这两个方法,Bundle会写入磁盘中
onPause->onSaveInstanceState  (测试时按Home键,或按ctrl+f12)


当手机收到短信时,会发出一个广播为
<action android:name="android.provider.Telephony.SMS_RECEIVED"/>

Object[] pdus=(Object[])intent.getExtras().get("pdus");
for(Object pdu:pdus)
{
	byte[]pduMsg=(byte[])pdu;
	SmsMessage sms=SmsMessage.createFromPdu(pduMsg);//soon deprecated 
	String mobile=sms.getOriginatingAddress();//发来的号码
	String msg=sms.getMessageBody();
	Date date= new  Date(sms.getTimestampMillis());
	String mobile=sms.getOriginatingAddress();//发来的号码
	String msg=sms.getMessageBody();
	Date date= new  Date(sms.getTimestampMillis());
}
<manifest
	<uses-permission android:name="android.permission.RECEIVE_SMS" />


Activity.this.sendOrderedBroadcast(intent,permission),是按声明receiver的优先级来接受的,如何设置优先级????????????android:permission="",
高级别可以再向广播中加入新数据(Intent),
也可以中止广播向后传递,BroadcastReceiver.this.abortBroadcast();	


<action android:name="android.intent.action.BATTERY_CHANGED"/>电池电量变化时的广播

<action android:name="android.intent.action.BOOT_COMPLETED"/>开机启动的广播
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

在广播接受的处理程序不能超过10秒,会报无响应的错误,
不能使用子线程(接受者生命周期短,为空进程,子线程未完成时就可能被杀死),要使用Service


TelephonyManager telManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
telManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
String imei = telManager.getDeviceId();//IMEI
String sn = tm.getSimSerialNumber();//序列号
MyPhoneStateListener extends  PhoneStateListener 
{
	public void onCallStateChanged(int state, String incomingNumber) 
	{
		switch (state)
		{
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://接电话时
			break;
			case TelephonyManager.CALL_STATE_RINGING://只有来电话时,incomingNumber才有效
				break;
		}
	}
}
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>


---AIDL Android Interface Definination Language 类似 Corba的idl
Android的多个应用,是多个进程传递对象,通信使用AIDL,JavaEE使用RMI
在包org.zhaojin.aidl下,建立IDownloadService.aidl文件后,会在自动在gen下生成代码,
有 asInterface(android.os.IBinder obj)方法
package org.zhaojin.aidl;
interface IDownloadService
{
	void download(String path);
}
//非Java 基于类型做参数时,要加in,out,inout
如果传递的是自定义对象,要implements Parcelable  //Parcel包裹
自定义类型中必须有一个名为CREATOR 静态成员,要求实现Parcelable.Creator<T>接口
----播放视频,Mp3
<uses-permission android:name="android.permission.RECORD_AUDIO"/>

<SurfaceView  /> 画布,当有一个新的Activity在前面时(如有电话打进来),SurfaceView会被销毁,
当电话结束时再回来,SurfaceView会被自动创建,但可能会比较晚,为了在SurfaceView创建后,再开始做事,
使用surfaceHolder.addCallback(new SurfaceHolder.Callback() 
			   	  	{	
			   		  public void surfaceDestroyed(SurfaceHolder holder) {}
						public void surfaceCreated(SurfaceHolder holder) {}
						public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}
			   	  	});

SurfaceView surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
player= MediaPlayer.create(this, R.raw.movie);//支持.mp4,或者用 new MediaPlayer(); reset(),setDataSource(),prepare()
SurfaceHolder surfaceHolder=surfaceView.getHolder();
surfaceHolder.setFixedSize(172, 144);//分辩率
//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//android 3.0版本以前要设置
player.setDisplay(surfaceHolder);
player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//player.reset();//After calling this method, you will have to initialize it again by setting the data source and calling prepare().
//player.prepare();//MediaPlayer.create()的方式不要再调用prepare()

//File videoFile = new File(Environment.getExternalStorageDirectory(), "movie.mp4");//模拟器是在/mnt/sdcard
//mediaPlayer.setDataSource(videoFile.getAbsolutePath());
mediaPlayer.setDataSource(new FileInputStream(new File("/storage/sdcard0/bluetooth/movie.mp4.mp4")).getFD());//机内部存储
player.start();//播放 OK



player.setLooping(true);
AudioManager audioMgr=(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//音量正常
audioMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);//静音,只是图标有变化,无效果
audioMgr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//振动
audioMgr.adjustVolume(AudioManager.ADJUST_LOWER, 0);//减小音量
audioMgr.adjustVolume(AudioManager.ADJUST_RAISE, 0);//增大音量

	

Window window = getWindow();
requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题,必须在setContentView之前完成
window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//高亮
   

WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
Display display = wm.getDefaultDisplay();
display.getWidth();//手机屏幕大小
display.getHeight();



重写Activity的onKeyDown 方法	
boolean onKeyDown(int keyCode, KeyEvent event) //不要在最后还调用super.onKeyDown();返回true 不会向后传递事件
{
if(event.getRepeatCount()==0){
	switch (keyCode) 
	{
	case KeyEvent.KEYCODE_BACK://返回
 
	}
}
}	
<LinearLayout android:gravity="right" >	里面的组件右对齐

<!-- SD卡权限 -->
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	
	
new OnClickListener()  
{  
 public void onClick(View v)  
 {     
   switch(v.getId())//来区分是哪个按钮
   {
	case R.id.startVideo:
   }
 } 
} 
"Settings"->Language & Input->Language->中文(简体)		
文字国际化   复制vales文件夹为,values-zh和values-en ,如果要加国家,values-zh-rUS和values-en-rCN,注意有个r
图片国际化  放新的文件夹为drawable-zh,如果考虑h,m,l使用drawable-zh-rCN-hdpi

屏幕大小自动适应,要准备不同大小的屏幕的布局,建立模拟器时,设置不同的分辨率
复制layout目录为layout-480x320,layout-320x240  ,注意大数字放在前面, layout-land表示landscape放置
start...按钮启动模拟器时,会提示HVGA(320x480),QVGA(240x320)



样式 values目录中建立style.xml中
<resources>
    <style name="myStyle">
        <item name="android:textColor">#00ff00</item>
    </style>
	 <style name="largeStyle" parent="@style/myStyle">  <!-- 可以继承 -->
		<item name="android:textColor">#00ffff</item>
		<item name="android:shadowColor">?android:textColor</item><!-- ?表示引用其它的值 -->
	</style>
	<drawable name="red">#7f00</drawable>
	<color name="solid_red">#f00</color>
	<string-array name="countries">
		<item>China2</item>
		<item>Russia2</item>
	</string-array>
</resources>

<TextView style="@style/myStyle" //或者layout目录下的文件中
<application android:theme="@style/myStyle"  //Manifest.xml文件中
<activity android:theme="@android:style/Theme.Dialog" //系统自带的主题
	setTheme(R.style.defaultStyle);//代码中


LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

---网页做UI
使用WebView
webView=(WebView)this.findViewById(R.id.webView);
webView.addJavascriptInterface(new MyJavaScriptPlugin(),"myContact");//JS中使用myContact.method1来调用MyJavaScriptPlugin中method1方法
webView.getSettings().setJavaScriptEnabled(true);
webView.loadUrl("file:///android_asset/index.html") ;//android_asset对应assets目录,也可是http://
webView.loadUrl("javascript:show('"+param+"')"); //调用JS方法,JS代码中有错误不会报出,最好在其它地方测试好再复制过来


---
export ->Export Android Application-> 要keystore
---动画
文档API Guids->App Resources->resources Types->Animation
	
Property 动画目录
	res/animator/ 
//----Tween 动画

	res/anim/   Tween 动画目录
可以复制里面的例子,有名称空间
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:shareInterpolator="false" >
    <translate android:fromXDelta="0"
        		android:fromYDelta="0" 
        		android:toXDelta="200"
        		android:toYDelta="200"
        		 android:duration="3000" />
     <scale
        android:fromXScale="1.0"
        android:toXScale="2.0"
        android:fromYScale="1.0"
        android:toYScale="2.0"
        android:pivotX="50%" 
        android:pivotY="50%"
      	android:startOffset="3000"
        android:duration="5000"  /> <!-- startOffset等几秒后 -->
     <alpha
        android:fromAlpha="1"
        android:toAlpha="0.3" 
        android:duration="5000" />
     <rotate
        android:fromDegrees="0"
        android:toDegrees="720"
        android:pivotX="50%"
        android:pivotY="50%" 
        android:duration="5000"
        /><!--  50%是自身的长度的一半吗?   -->
</set>


Animation tweenAnim=AnimationUtils.loadAnimation(this,R.anim.tween_motion);
tweenAnim.setFillAfter(true);//动画结尾的形态做为以后显示的形态,而不是动画开始的形态

//手工写程序做动画
Animation transAnim=new TranslateAnimation(0,200,0,200);
transAnim.setDuration(3000);

Animation rotateAnim=new RotateAnimation(0f, 720f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
rotateAnim.setStartOffset(3000);
rotateAnim.setDuration(5000);

ImageView img=(ImageView)this.findViewById(R.id.anim_ImageView);
img.startAnimation(tweenAnim);

//img.startAnimation(transAnim);//如果把两个动画一起放???
//img.startAnimation(rotateAnim);


//----Frame动画

res/drawable/frame.xml

<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
    android:oneshot="false"> <!-- false表示一直重复播放 -->
    <item android:drawable="@drawable/girl_1" android:duration="200" />
    <item android:drawable="@drawable/girl_2" android:duration="200" />
    <item android:drawable="@drawable/girl_3" android:duration="200" />
    <item android:drawable="@drawable/girl_4" android:duration="200" />
    <item android:drawable="@drawable/girl_5" android:duration="200" />
    <item android:drawable="@drawable/girl_6" android:duration="200" />
    <item android:drawable="@drawable/girl_7" android:duration="200" />
    <item android:drawable="@drawable/girl_8" android:duration="200" />
    <item android:drawable="@drawable/girl_9" android:duration="200" />
</animation-list>

TextView textView=(TextView)this.findViewById(R.id.anim_TextView);
textView.setBackgroundResource(R.drawable.frame);
final AnimationDrawable animDrawable=(AnimationDrawable)textView.getBackground();

//getMainLooper().myQueue();//得到主线程的消息队列,Looper.myLooper().myQueue();当前线程的,可以不使用 
Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {//在所有消息队列都处理完成空闲时
					public boolean queueIdle() {
						animDrawable.start();//启动动画
						return false;//表示从消息队列中删除,即只做一次
					}
				});

在子线程中以Looper.prepare()开始,以Looper.loop()结束
handler.getLooper.quit();//在destory方法中

//----Activity 切换 动画
A_MainActivity.this.overridePendingTransition(R.anim.enter_activity, R.anim.leave_activity);

//----界面 切换 动画
layout文件中
 <ViewFlipper android:layout_width="fill_parent"
        		android:layout_height="fill_parent"
        		android:id="@+id/anim_flipper">
        
	<LinearLayout  android:orientation="vertical"
					android:layout_width="fill_parent"
					android:layout_height="fill_parent"
					android:background="#00FFFF"  >
		
		<TextView 	android:layout_width="fill_parent"
					android:layout_height="fill_parent"
				android:text="---------这是第一页--------"  />
	</LinearLayout>
	
	<LinearLayout  android:orientation="vertical"
					android:layout_width="fill_parent"
					android:layout_height="fill_parent"
					android:background="#0000FF"   >

		<TextView android:layout_width="fill_parent"
					android:layout_height="fill_parent"
					android:text="---------这是第二页--------" />
	</LinearLayout>
</ViewFlipper>
public boolean onTouchEvent(MotionEvent event)  //重写Activity的方法
{
	if(event.getAction() == MotionEvent.ACTION_DOWN)
		startX=event.getX();
	if(event.getAction() == MotionEvent.ACTION_UP)
	{
		if(event.getX() >startX)//到第二页   ->
		{
			flipper.setInAnimation(inL2RAnim);
			flipper.setOutAnimation(outL2RAnim);
			flipper.showNext();
			
		}else if(event.getX() < startX)//到第一页    <-
		{
			flipper.setInAnimation(inR2LAnim);
			flipper.setOutAnimation(outR2LAnim);
			flipper.showPrevious();
		}
	}
	//return true;
	return super.onTouchEvent(event);
}

//---9 Patch文件
文档API Guids->App Resources->resources Types->Drawable 

ninePatch格式图片基于PNG,可以透明,优点是可以对部分区域做自动伸缩
\android-sdk-windows\tools\draw9patch.bat  工具来处理基于PNG格式的图片
<TextView android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	 android:background="@drawable/text_background_9patch"
	  android:text="这是一个自动缩放的背景图片" />

  
//------------layer-list   多个文件合并显示
drawable/layer_list.xml文件
<layer-list  xmlns:android="http://schemas.android.com/apk/res/android" >
    <item> <!-- 最上面的显示在最底下 -->
      	<bitmap android:src="@drawable/album"  android:gravity="center" />
    </item>
    <item
        android:drawable="@drawable/compass1"
        android:id="@+id/photo"
        android:top="68dp"
        android:right="18dp"
        android:bottom="22dp"
        android:left="18dp" />
</layer-list>

LayerDrawable layer =(LayerDrawable)this.getResources().getDrawable(R.drawable.layer_list);
Drawable drawable =this.getResources().getDrawable(R.drawable.compass2);

layer.setDrawableByLayerId(R.id.photo, drawable);
img.setImageDrawable(layer);


<Button android:text="切换"  android:onClick="swith"/>
<!-- 调用自己Activity类中的方法名  public void swith(View v) -->

 
//------------selector
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:state_enabled="true" android:drawable="@drawable/button_selected" /> 
	<!--  只有当两个条件同时成立时才应用，即当组件按下&& 组件是可用的　显示对应的图片　 -->
    <item android:state_selected="false"  android:drawable="@drawable/button_normal" /> 
    <item android:drawable="@drawable/button_normal" /> <!-- 默认的要放在最后面 -->
 </selector>

//------------level-list
 <level-list  xmlns:android="http://schemas.android.com/apk/res/android" >
    <item android:drawable="@drawable/compass1"   android:minLevel="0"  android:maxLevel="10" />
    <item android:drawable="@drawable/compass"   android:minLevel="10"  android:maxLevel="20" />
    <!-- 像电池 显示,根据不同的值,显示不同的图片  -->
</level-list>
ImageView level_img=(ImageView)this.findViewById(R.id.level_img);
LevelListDrawable  levelDrawable=(LevelListDrawable)level_img.getDrawable();
levelDrawable.setLevel(15);//根据level  显示对应的照片

//------------transition
<transition xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/compass" />
    <item android:drawable="@drawable/compass1" />
    <!-- 图片之间的过度 -->
</transition>
Button btn=(Button)this.findViewById(R.id.button_transaction);
TransitionDrawable  transDrawable=(TransitionDrawable)btn.getBackground();
transDrawable.startTransition(5000);

//------------clip  

<clip xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/compass2"
    android:clipOrientation="horizontal"
    android:gravity="left" />
	
ClipDrawable drawable = (ClipDrawable) imageview.getDrawable();
drawable.setLevel(drawable.getLevel());//默认是0,全clip不可见的,1000是不clip是全可见的

//------------scale  
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/compass"
    android:scaleGravity="center_vertical|center_horizontal"
    android:scaleHeight="80%"
    android:scaleWidth="80%" />

ImageView scale_img=(ImageView)this.findViewById(R.id.scale_img);
ScaleDrawable  scale_drawable=(ScaleDrawable)scale_img.getDrawable();
scale_drawable.setLevel(scale_drawable.getLevel()+ 1000 );

//--------
 <activity  ...>
	<meta-data android:name="my.props.id" android:value="123"></meta-data>
	  <meta-data android:name="my.props.name" android:value="@string/app_name"></meta-data>
	  <meta-data android:name="my.props.name.R.id" android:resource="@string/app_name"></meta-data>
	  <!-- resource 是返回对应的R文件中的 R.string.app_name 的值 -->
   
   
ActivityInfo activityInfo= this.getPackageManager().getActivityInfo(new ComponentName(this,A_MainActivity.class),PackageManager.GET_META_DATA);
Bundle bundle=activityInfo.metaData;
String name=bundle.getString("my.props.name");
int id=bundle.getInt("my.props.id");
int name_R_id=bundle.getInt("my.props.name.R.id");
Toast.makeText(this,id+":"+name+":"+Integer.toHexString(name_R_id), Toast.LENGTH_LONG).show();

//-------------
一直按住桌面,会弹出菜单配背景,把桌面已有项可以一直按住后出现删除工具,拖向就删除
在列表面板中一直单击一个应用图标,可以放入桌面
桌面上也可拖动,切换面板


文档中 API Guid-> App Components ->App Widgets

AndroidManifest.xml文件中 
 <receiver android:name=".MyAppWidgetProvider" >
	<intent-filter>
		<action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
	</intent-filter>
	<meta-data android:name="android.appwidget.provider" android:resource="@xml/my_widget" />
</receiver>
<!--   Widget Preview 是android 自带的一个应用,用于出现在  Widget Preview 中的列表中 -->



res/xml/my_widget.xml文件中
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="294dp"
    android:minHeight="72dp"
    android:updatePeriodMillis="0"
    android:initialLayout="@layout/my_widget_layout"
     >
</appwidget-provider>
 
public class MyAppWidgetProvider extends AppWidgetProvider   //Android Studio运行时，配置要把Lanch:默认的Default Activity修改为Nothing
{
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{	
		System.out.println("onUpdate被调用");
//		RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.my_widget_layout) ;
//		views.setTextViewText(R.id.textView, date);
//		appWidgetManager.updateAppWidget(appWidgetIds[0], views);
//		//不能使用线程来做更新,广播生命周期很短,使用Service
		context.startService(new Intent(context,TimerService.class));
	}
	public void onDeleted(Context context, int[] appWidgetIds) //删除时,调用这个方法(android4.1中没有手工删除的方法,是自动的),不用再配置 <receiver> 
	{
		System.out.println("onDelete被调用");
		context.stopService(new Intent(context,TimerService.class));
	}
	public void onDisabled(Context context)//删除最后一个时被调用  (没有测试到什么时候调用,可能要android清资源时)
	{
		System.out.println("onDisabled被调用");
	}
	public void onEnabled(Context context)  //第一次增加时时被调用
	{	
		System.out.println("onEnabled被调用");
	}
}



//SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
DateFormat format =DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);
String date=format.format(new Date());

RemoteViews views=new RemoteViews(getPackageName(),R.layout.my_widget_layout) ;
views.setTextViewText(R.id.textView, date);

Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:13011114444"));
PendingIntent pendingIntent=  PendingIntent.getActivity(getApplicationContext(),123,intent, 0);
views.setOnClickPendingIntent(R.id.textView, pendingIntent);//处理单击事件

AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(getApplicationContext());
appWidgetManager.updateAppWidget(new ComponentName(getApplicationContext(),MyAppWidgetProvider.class), views);//同类型的所有都会更新



//自定义标题
//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//android 4.1报错,在setContentView(...)方法前使用
//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

使用 <activity android:theme="titleThem" 方式
 <!--写的时候没有提示android:xxx,可能是为了兼容老版本 -->
<style name="titleBackground">
	<item name="android:background">@drawable/shape</item>
</style>
<style name="titleThem" parent="android:Theme"> <!-- 按钮风格变了 -->
	<item name="android:windowContentOverlay">@drawable/blue</item><!-- 这个看不到什么效果-->
	<item name="android:windowTitleSize">35dp</item>
	 <item name="android:windowTitleBackgroundStyle">@style/titleBackground</item>
</style>

	
android.jar自带的res/layout/screen_custom_title.xml	是二制文件


//------popup
//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
LayoutInflater inflater = this.getLayoutInflater();
View view=inflater.inflate(R.layout.popup,null);
popup=new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
window=findViewById(R.id.popupWindow);

//popup.showAsDropDown(button, button.getWidth(), button.getHeight());//显示一个组件在旁边,当是右键菜单
popup.showAtLocation(window, Gravity.BOTTOM|Gravity.LEFT, 10, 10);//使用父容器来定位

if(popup.isShowing())
	popup.dismiss();

popup.setAnimationStyle(R.style.popupAnim);	
<!--写的时候没有提示android:xxx,可能是为了兼容老版本 -->
<style name="popupAnim">
	<item name="android:windowEnterAnimation">@anim/in_l2r_flipper</item>
	<item name="android:windowExitAnimation">@anim/out_r2l_flipper</item>
</style>

//------internet仿问
<uses-permission android:name="android.permission.INTERNET" />

extends BaseAdapter//抽像类
{
	public int getCount(){}//要实现返回在组件中显示数据的长度
	public Object getItem(int position){}//要实现返回数据索引对应的数据
	long 	getItemId(int position)//为自己使用的id
	View 	getView(int position, View convertView, ViewGroup parent)//convertView可用做缓存
	{
		if(convertView==null)//第一次是null
			convertView= xxx;
		convertView.setTag();//因为findViewById性能很低,可第一次找到后保存到Tag中
		
	}
	
}
Uri.fromFile(file); 
imgView.setImageURI(uri);

extends AsyncTask<Params, Progress, Result>  //抽像类,第一个是参数类型,第二个是进度的类型,第三个是结果类型
{//内部使用 Handler 和 ThreadPool
	onPreExecute(){...}
	doInBackground(){...}  //手工调用publishProgress();时,才会调用 onProgressUpdate
	onPostExecute(){...}
	onProgressUpdate(Progress... values){...}
	
}
//外部调用 execute();
带和 Java一样的线程池 ,nio

listView.setOnScrollListener(new OnScrollListener() //滚动条事件
		{
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				switch (scrollState) 
				{
				case OnScrollListener.SCROLL_STATE_IDLE:
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					break;
				case OnScrollListener.SCROLL_STATE_FLING://表示滚动后正在做 惯性 滚动
					break;
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

listView.setAdapter之前增加页脚.listView.addFooterView(x);,setAdapter的源码中会封闭一个新对象
listView.removeFooterView(x);//如果是在特定的时候增加页脚,这里先删除

listView.getLastVisiblePosition();
arrayAdapter.notifyDataChanged();//组件会更新显示



//--traceview 工具进行性能分析
Debug.startMethodTracing("traceFile");//写入SD卡目录,/mnt/sdcard/
 
protected void onDestroy() 
{
	Debug.stopMethodTracing();//停止采集
	super.onDestroy();
}
Activity结束后,把文件导出,
android-sdk-windows\tools\traceview.bat traceFile.trace 有图形界面,图形

下面的的find中,查找时要输入全小写字母才可以!!!

Incl (Inclusive )
Incl Cpu Time:表示对应方法在运行时,被其它的调用的所有时间之和 , = Cpu Time * Calls
Cpu Time/Call: / 前面的部分 表示方法每次执行的时间,
Calls+RecurCalls/Total:   + 前面的部分 表示方法被调用的次数

Incl Cpu Time%:


Excl (Exclusive)表示如方法中调用了其它方法,其它方法不计算时间


//------
Drawable.createFromStream(new URL("").openSteram(),"src");
MyView extends View
{
	protected void onDraw(Canvas canvas) //重写
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		Paint paint=new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawCircle(50, 50, 25, paint);
		
		
		Rect rect=new Rect(20,100,200,300);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(rect, paint);
		
		paint.setColor(Color.RED);
		canvas.drawLine(100, 100, 200, 200, paint);
		
		Path path=new Path();
		path.moveTo(150,150);
		path.lineTo(50,350);
		canvas.drawPath(path, paint);
		
		RectF rectf=new RectF(22.2f,102.2f,180.5f,280.5f);
		canvas.drawRect(rectf, paint);
		
		
		paint.setTextSize(18);
		canvas.drawText("中华人民共和国", 20, 300, paint);
		
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		canvas.drawBitmap(bitmap , 50 , 500 , paint);
	}
}
    
<org.zhaojin.a_canvas.MyView 
	android:layout_width="fill_parent"
	android:layout_height="fill_parent"
/>

//--电池

public class BatteryReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) 
	{
		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
		{
			int level=intent.getIntExtra("level",0);
			int scale=intent.getIntExtra("scale",0);
			
			AlertDialog.Builder builder=	new AlertDialog.Builder(context)
			.setMessage("电量为:"+((level+0.0)/scale *100)+"%")
			.setNegativeButton("关闭",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.create();
			builder.show();
		}
	}
}

使用代码来作
context.registerReceiver(new BatteryReceiver(),new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

 

//-- TextSwither
TextSwitcher myTextSwitcher = (TextSwitcher) findViewById(R.id.myTextSwitcher);
SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.CHINESE);
String dataStr=format.format(new java.util.Date());
myTextSwitcher.setFactory(new MyViewFactory());
myTextSwitcher.setText(dataStr);//要在SetFactory之后调用
myTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(MyTextSwitcherActivity.this,android.R.anim.fade_in));
myTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MyTextSwitcherActivity.this,android.R.anim.fade_out));
				
class MyViewFactory implements ViewFactory
{
	@Override
	public View makeView() 
	{
		TextView txt=new TextView(MyTextSwitcherActivity.this);
		txt.setBackgroundColor(Color.WHITE);
		txt.setTextColor(Color.BLACK);
		txt.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		//要使用TextSwitcher.LayoutParams
		txt.setTextSize(30f);
		return txt;
	}
}
 

//--ZoomControls
 <ZoomControls 
	android:id="@+id/zoom_controls"
	android:layout_width="fill_parent"
	android:layout_height="fill_parent"
	android:gravity="bottom"
	/> 
zoom.setOnZoomInClickListener(new ZoomControls.OnClickListener() 
	{
			@Override
			public void onClick(View v) {
				zoomScale+=2;
				zoomText.setTextSize(zoomScale);
			}
	});
//--Matrix
3x3矩阵
----------------------------------------------
| scale_x 		| skew_x  		| translate_x |
----------------------------------------------
| skew_y  		| scale_y 		| translate_y |
----------------------------------------------
| perspective0 | perspective0	| scale 	  |
----------------------------------------------
 
-------------------------------
| cos@ 	| -sin@	| translate_x |
-------------------------------
| sin@ 	| cos@  | translate_y |
-------------------------------
| 0 	| 0		| 	scale	  |
-------------------------------


//=========只可真机测试
真机目录/storage/sdcard0(是机内存)/是主目录 ,有bluetooth,Download,Pictures/Screenshots

//---camera Mac上模拟器不能用
摄像头在一个时间里只能被一个Activity使用

<uses-permission android:name="android.permission.CAMERA"/>	
 <uses-feature android:name="android.hardware.camera" />
 <uses-feature android:name="android.hardware.camera.autofocus" />
 
<SurfaceView
 
//----录像
//--系统界面,可切换前后面的摄像头,没有声音??
Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
Uri outputFileUri = Uri.fromFile(new File( Environment.getExternalStorageDirectory(),"myMovie.mp4"));//"/storage/sdcard0/myMovie.mp4"
intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri );
intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // 范围0-1
intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,60);//1 minute
startActivityForResult(intent, 201);
       
//--自定义界面,只有后面的摄像头
recorder=new MediaRecorder(); //文档上有图, 任何时候可以使用reset()方法到initial状态
recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
recorder.setVideoSize(320, 240);
recorder.setVideoFrameRate(3); //每秒3帧
recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263); //设置视频编码方式
recorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//从麦克风收集声音
recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//3gp
recorder.setOutputFile("/mnt/sdcard/recorder.arm.3gp");
//getCacheDir();//文件会写在 /data/data/<package>/cache目录下,可以OnDestroy时删除
recorder.prepare();//缓冲
recorder.start();

recorder.stop();
recorder.release();
 

//--拍照
//--系统界面,可切换前后面的摄像头
Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
Uri outputFileUri = Uri.fromFile(new File( Environment.getExternalStorageDirectory(),"myImg.png"));//"/storage/sdcard0/myImg.gif"
intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri );
startActivityForResult(intent, 200);



if (!CameraPhotoActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	Toast.makeText(CameraPhotoActivity.this, "本机没有摄像头",Toast.LENGTH_LONG).show();
}

//Camera.getNumberOfCameras();
//CameraInfo cameraInfo = new CameraInfo();
//if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) //CameraInfo.CAMERA_FACING_FRONT
android.hardware.Camera  camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);//默认是CameraInfo.CAMERA_FACING_BACK

if(camera==null)
{
	Toast.makeText(CameraPhotoActivity.this, "摄像头不可用",Toast.LENGTH_LONG).show();
	return;
}
camera.setPreviewDisplay(surfaceView.getHolder());//通过SurfaceView显示取景画面
camera.startPreview();//开始预览
	
camera.stopPreview();
camera.release();
camera.autoFocus(null);//自动对焦
camera.takePicture(null, null, new Camera.PictureCallback(){onPictureTaken(byte[] data, Camera camera)});//拍照 . 处理过的,原始的,压缩的
//异步实现,就不会再预览了,要再次调用startPreview()要放在异步调用CallBack方法中



//---蓝牙
设置->蓝牙->菜单后有 Visibility timeout设置时间
要手工完成两个蓝牙配对,才有数据

<uses-permission android:name="android.permission.BLUETOOTH"/> <!-- 使用蓝牙的权限  -->
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/> <!--修改蓝牙操作的权限  -->

BluetoothAdapter adapter= BluetoothAdapter.getDefaultAdapter();//本机的蓝牙设备
if(adapter==null)
{
	Toast.makeText(BlueToothActivity.this, "本机没有蓝牙设备", Toast.LENGTH_LONG).show();
	return;
}
	
if(! adapter.isEnabled())
{
	Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//有界面提示用户使用蓝牙设备,打开蓝牙权限请求,如选Yes也提示Visible on
	startActivity(intent);
}

Set<BluetoothDevice> devices=adapter.getBondedDevices();//本机已经配对的蓝牙设备
bluetoothDevice.getName()
bluetoothDevice.getAddress()//MAC地址

Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//可见的
intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,120);//可见时间,单位秒,最多300
startActivity(intent);//有Toast提示用户开启时间


IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);//只接收发现蓝牙的广播
this.registerReceiver(new BroadcastReceiver() 
		{
			public void onReceive(Context context, Intent intent) 
			{
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Toast.makeText(BlueToothActivity.this, "已扫描到蓝牙设备:"+device.getName()+"_"+device.getAddress(), Toast.LENGTH_LONG).show();
			}
		}
		, filter);
		
adapter.startDiscovery();//扫描其它蓝牙设备,是一个异步调用,每扫描到一个蓝牙,会发送一个广播
 

//---定位
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/> <!-- 定位权限 --> 
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

模拟器Emulator Control中可以发送经纬度来测试,在模拟器上方有一个GPS的图标

//模拟器可测试要send,真机测试要到室外才有信号,闪表示正在请求信号,请求到就不闪了
LocationManager manager= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 20, //3秒后或大于20米 做 一更次更新(通知listener)
		new LocationListener() 
		{ //使用GPS做Provider,也可使用NETWORK_PROVIDER,
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			public void onProviderEnabled(String provider) {
			}
			public void onProviderDisabled(String provider) {
			}
			
			public void onLocationChanged(Location location) 
			{
				location.getLatitude();// 纬度
				location.getLongitude();//经度
			}
		});


manager.getAllProviders();//所有的 provider有,真上测试的有network,passive,gps三个

Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//第一次取不到时返回null
double latitude = location.getLatitude();     //纬度
double longitude = location.getLongitude(); //经度
double altitude =  location.getAltitude();     //海拔

/*
Criteria criteria=new Criteria();//查找适合自已需要的 Provider
criteria.setAccuracy(Criteria.ACCURACY_FINE);
criteria.setAltitudeRequired(true);//要有 海拔高度 信息
criteria.setCostAllowed(false);//收费
criteria.setPowerRequirement(Criteria.POWER_MEDIUM);//电量使用

criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);//方位
criteria.setBearingRequired(true);

criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);//速度
criteria.setSpeedRequired(true);

String best= manager.getBestProvider(criteria, true);//最适合provider
*/
//-------WiFi
android.Manifest.permission有所有的权限 

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>


WifiManager wifiManager=(WifiManager) this.getSystemService(Service.WIFI_SERVICE);
wifiManager.setWifiEnabled(true);
int state=wifiManager.getWifiState();//WifiManager.WIFI_STATE_ENABLED

//---传感器
手机屏幕中心中原点,Z轴正方向是面向用户的,y轴正方向是向上(指向听筒),x轴正方向右,和OpenGL一样的

 <uses-feature android:name="android.hardware.sensor.accelerometer"  android:required="true" />

指南针,不能自动旋转屏幕
<activity android:name=".SensorActivity" android:screenOrientation="portrait"></activity>
 
Sensor.TYPE_ACCELEROMETER 加速度(重力)传感器
		 //因放在桌步动值是9.8,要做下面的算法才对,是从官方文档上复制的,使用linear_acceleration
		 final float alpha = 0.8f;
		 float[] gravity=new float[3];
		 float[] linear_acceleration=new float[3];
		 //重力加速度
		  gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		  gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		  gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
		//直线加速度,要减去重力加速度
		  linear_acceleration[0] = event.values[0] - gravity[0];
		  linear_acceleration[1] = event.values[1] - gravity[1];
		  linear_acceleration[2] = event.values[2] - gravity[2];

Sensor.TYPE_PROXIMITY  距离传感器,用于电话在耳边时,锁屏
	values[0];//
	sensor.getMaximumRange();//是5 ,就0或5两个值

Sensor.TYPE_LIGHT //灯光
 

List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);//手机中所有支持的传敏器

//重写OnResume
manager.registerListener(listener, orenSensor, SensorManager.SENSOR_DELAY_NORMAL);//采样频率
//重写OnPause中不取值,
manager.unregisterListener(listener, orenSensor)
listener=new SensorEventListener()
		{
			public void onAccuracyChanged(Sensor sensor, int accuracy) 
			{
			}
			public void onSensorChanged(SensorEvent event) 
			{
				int acc=event.accuracy;
			
				switch (event.sensor.getType())
				{
					case Sensor.TYPE_ACCELEROMETER://重力方向(正向放在桌上,是z)值约为9.8,球在迷宫里
						break;
 
				}
			}
		}
//---多点触摸
extends View
	public boolean onTouchEvent(MotionEvent event) //重写
	{
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			break;
			case MotionEvent.ACTION_MOVE:
			break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return super.onTouchEvent(event);
	}
	
imageView.setOnTouchListener(new MyTouchListener());	

private class MyTouchListener implements OnTouchListener
{
	public boolean onTouch(View v, MotionEvent event) 
	{

		
		//switch ( event.getAction()& MotionEvent.ACTION_MASK )
		switch (event.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
 			break;
		case MotionEvent.ACTION_MOVE:  //会多次被调用
			break;
		case MotionEvent.ACTION_POINTER_DOWN://对多点触摸
			break;
		case MotionEvent.ACTION_UP:
			break;	
		case MotionEvent.ACTION_POINTER_UP:
			break;	
		default:
			break;
		}
		return true; //返回true表示消费这个事件
	}
}
PointF point=new PointF();//坐标点类
Matrix matrix=new Matrix();//矩阵类，用于变换
matrix.set(previous);//基于上一次的变换
matrix.postScale(zoomX, zoomY,midPoint.x,midPoint.y );	
matrix.postTranslate(distanceX, distanceY);


//---震动
Vibrator vibrator=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
vibrator.
<uses-permission android:name="android.permission.VIBRATE"/>

<uses-permission android:name="android.permission.DEVICE_POWER" /> <!--要project-clean -->
<uses-permission android:name="android.permission.REBOOT"/> 

---
	 <uses-permission android:name="android.permission.REBOOT"/><!--要project-clean ,可能要得到root权限 -->
	 <uses-permission android:name="android.permission.DEVICE_POWER" />
	 
	 Intent intent = new Intent(Intent.ACTION_SHUTDOWN);//可以要得到root权限
	 
//=========上 只可真机测试



ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo netinfos[] = cm.getAllNetworkInfo();
for (NetworkInfo netinfo : netinfos)
{
	String log=netinfo.getTypeName() +  netinfo.getState();
}


verName =context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;


Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//所有Activities过虑条件

PackageManager pm = getPackageManager();
List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
ResolveInfo info = list.get(i);
CharSequence labelSeq = info.loadLabel(pm);//得到  <activity  android:label="xxx" 的实际值
Intent result = new Intent();
result.setClassName( info.activityInfo.applicationInfo.packageName //只包名
			,   info.activityInfo.name); //带包名和activity类名
		
		
=========人脸识别
android.media.FaceDetector

 
----root权限

--adb shell 以root权限运行
adb pull default.prop c:/temp
#adb -s  emulator-5554 pull default.prop c:/temp

---default.prop目前修改无效????
	ro.secure=1   #修改为0
	persist.service.adb.enable=0  #修改为1
	#ro.debuggable=0    #访真器为1,真机打开了debug也是0

adb -s A49947194A9B  shell
su	#切换为root,要手机已经root
#如提示Read-Only,adb shell
mount -o remount,rw rootfs /   

#chmod 777 /default.prop   
exit  #退shell

#adb -s A49947194A9B  push c:/temp/real/default.prop /default.prop

adb -s  A49947194A9B reboot  #或在shell中reboot

adb remount  #system分区从 只读 -> 可写 ,只有获得了root权限才可能运行

------------NDK
cygwin的一些命令
cygpath -u D:/cygwin/bin/make  会返回/usr/bin/make
cygpath -m /usr/bin/make  	   会返回D:/cygwin/bin/make
cygcheck -c cygwin  显示当前Cygwin 安装的版本	

g++ 不正常?????

JNI方式
java中的native方法,linux命名规范lib<somthing>.so,库会自动放到apk包中,并签名
java中使用System.loadLibrary("FileLoader");//表示使用libFileLoader.so文件
windows 使用Cygwin

在项目目录下建立文件 <project-path>/jni/Android.mk
可选的使用GNU make写自己的<project-path>/jni/Application.mk 文件

把D:\android-ndk-r7 入在PATH中,为了使用ndk-build命令方便
到自己的项目目录,运行ndk-build,(会读<>/jni/Android.mk文件)会生成目录obj,libs,
有libs/armeabi/libxxx.so,gdbserver文件,gdb.setup文件
eclipse ADT打包后的apk包中是在lib/armeabi/目录下(是lib没有s),中只有libxxx.so,gdbserver的文件



或者 ./ndk-build -C <project-path>

项目目录指AndroidManifest.xml文件所在的目录
ndk-build  clean    --> clean generated binaries
ndk-build  -B V=1   --> force complete rebuild, showing commands

ndk-gdb 用于调试(windows下只有shell文件,在cygwin上,要复制到cygwin的目录中,项目也要在cygwin目录中)
###AndroidManifest.xml中设置   <application android:debuggable="true" (有警告) ,必须使用ndk-build打包后才可用ndb-gdb来调试

$NDK/build/tools/build-platforms.sh  运行一次,(项目放在当前NDK下)


android-ndk-r7\platforms\android-14\arch-x86\usr\include
android-ndk-r7\platforms\android-14\arch-arm\usr\include


-------<project-path>/jni/Android.mk 的写法
//include $(call all-subdir-makefiles)  #在文件的首行,表示对所有的子目录中的Android.mk引入


LOCAL_PATH := $(call my-dir)
#,注释#必须是行中第一个字符Android.mk 必须以LOCAL_PATH开头,my-dir是NDK中已经定义的宏,返回当前目录,包括Android.mk文件的目录

include $(CLEAR_VARS)
#是NDK提供的变量,会消除很多  LOCAL_XXX 变量除了LOCAL_PATH

LOCAL_MODULE    := c-native
#模块名是唯一的,没有空格,如'foo'会生成'libfoo.so',如'libfoo',也是'libfoo.so'
LOCAL_SRC_FILES := c-native.c
#所有的C/C++ 源文件列表,不必加头文件,如果文件以 .cpp结尾,被认为C++文件,可以设置LOCAL_CPP_EXTENSION为 .cxx ,注意有.的

include $(BUILD_SHARED_LIBRARY)
#是NDK提供的变量,会读所有的LOCAL_XXX 变量,构建.so文件,BUILD_STATIC_LIBRARY生成静态



-------<project-path>/jni/Application.mk 的写法
<project-path>/jni/Application.mk这个目录下的文件会默认被$PROJECT/jni/Android.mk读取

也可NDK安装目录建立apps目录, $NDK/apps/<name>/Application.mk,使用时make APP=<name>
(不推荐的,老版本,以后可能会被删除),输出在$NDK/out/apps/<name>/下,APP_PROJECT_PATH可能被Application.mk使用

------------OpenGL ES  2.0

doc文档 Android Training链接->Advanced Training->Displaying Graphics with OpenGL ES 也有示例代码


<uses-feature android:glEsVersion="0x00020000" android:required="true" /><!--使用openGL ES2.0  -->
<!--  texture compression -->
<supports-gl-texture android:name="GL_OES_compressed_ETC1_RGB8_texture" />
<supports-gl-texture android:name="GL_OES_compressed_paletted_texture" />

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

private GLSurfaceView mGLView;

mGLView = new MyGLSurfaceView(this);
setContentView(mGLView);
		
public class MyGLSurfaceView extends GLSurfaceView {
	public MyGLSurfaceView(Context context) {
		super(context);
        setRenderer(new MyRenderer());
		//设置以下在模拟器中报错
        //setEGLContextClientVersion(2);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//有报错日志,不可显示内容
        //requestRender();
	}
}

public class MyRenderer implements GLSurfaceView.Renderer {
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {//环境建立时调用一次
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}
	public void onSurfaceChanged(GL10 gl, int width, int height) {//手机改变方向
		GLES20.glViewport(0, 0, width, height);
	}
	public void onDrawFrame(GL10 gl) {//每次刷新重画
	    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}
}




------------EGL
import javax.microedition.khronos.egl.*;

------------OpenSL ES

------------OpenMAX AL

============Ware 
如果已经在虚拟机中使用,还是应该使用ARM CPU

 报 You cannot combine swipe dismissal and the action bar 错时
 values-v14/styles.xml 中 修改  android:Theme.Holo.Light.DarkActionBar 为  android:Theme.DeviceDefault
 <style name="AppBaseTheme" parent="android:Theme.DeviceDefault">  
 </style> 
 
 双击主屏进入设置,向左/右 拖表示 返回/前进  也可向下拖快速设置
 
 
============TV
滚动鼠标滚轮(四个方向键),按回车
esc 键 返回
home 主页
 
 