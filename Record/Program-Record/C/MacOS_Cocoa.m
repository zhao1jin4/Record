https://developer.apple.com/library/mac/navigation/


Mac openGL是使用的 GLUT,可以使用POSIX 的多线程


//==============Xcode

Xcode -> 偏好设置 ->Components->Documentation  中可以下载的.docset手册	

在Mac上安装Charles 来得到下载URL, 就看不有用的东西了
以.xar结尾的文件,Mac可以使用BetterZip解压,windows下可以使用7zip解
 
--------	
Xcode-8.2.1 页面提示只可在appstore在线安装
https://developer.apple.com/download/more/ 可以下载离线xip压缩包
Xcode_8.2.1.xip  大小 4.2GB ,解压后约10 GB  

要求Xcode 8.2.1 requires a Mac running macOS 10.11.5 or later. 
Xcode 8.2.1 includes SDKs for iOS 10.2, watchOS 3.1, macOS 10.12.2, and tvOS 10.1. 

Mac 下双击.xip文件解压,用Archive Utility来做,解压后就一个Xcode.app,
但是在当前目录直接解压,如想改变解压目录,就先单独打开Archive Utility->Preferences->save expand files 选保存目录
Mac 命令 xattr -d Xcode_8.xip (无用的)
windows下的 7-zip可以打开

Xcode-8.0  自带OSX,iOS文档,可以离线打开,看树也有Swift和JavaScript,但没有示例文档,
Preferences -> Component->Documentation-> 下只有 Guid and Sample Code
---Xcode-8.0 的 Guid and Sample Code 就一个文件
https://developer.apple.com/library/downloads/docset-index.dvtdownloadableindex   ,下载时注意要在 XCode对应的版里
有对应的docset下载URL,是dmg文件,里面是.pkg,使用Pacifist查看默认安装在 / 下

https://devimages.apple.com.edgekey.net/docsets/20160913/031-77711-A.dmg
大小 1.06 GB
在线位置  https://developer.apple.com/library/content/navigation/

mkdir -p  ~/Library/Developer/Shared/Documentation/DocSets/ 
如要Finder中显示用户主目录的 ~/Library/ 用命令	chflags nohidden ~/Library/

使用 Pacifist 看.pkg文件默认安装 / 下

cd /Volumes/ConceptualDocset
installer -pkg ConceptualDocset.pkg -target ~/Library/Developer/Shared/Documentation/DocSets/  报错,6.4版本是可以的

只能 双击pkg安装界面中修改安装目录~/Library/Developer/Shared/Documentation/DocSets/, 要密码(生成文件更高权限),在目录中生成com.apple.adc.documentation.docset文件
但 Xcode中还是显示要下载安装,打开文档也没有看到多余的东西????



cd Xcode.app/Contents/Developer/Documentation/DocSets/
 默认有个 com.apple.adc.documentation.docset  大小101.7MB，把它先移走，再把下载的移过来
 sudo chown -R -P devdocs com.apple.adc.documentation.docset   有devdocs这个用户和组
 也没什么效果？？？？


---------

不安装docset 也可以直接使用alt+点击 类名，看文档 


Xcode 把文件更新取消

Xcode 建立项目时不要选择 git  版本控制,生成的历史不知道在哪里删,导到用过的资源,删除,不能再建立或使用

Xcode 只是一个.app文件,其实是文件夹,sudo xcode-select -switch /Applications/Xcode.app
Xcode->Open Developer Tool->FileMerge 文件比较器


preferences中的编码默认是UTF-8
设置字体, preference->Font&Colors,左侧选择一个Theme,右侧command+a全选,点下方的 T 设置字体,双击字体号生效

command + 单击类/方法  进入看
Xocde在写代码时,可以按ctrl键,来提示
option+鼠标单击类显示Doc , 光标位置放在要提示的位置,按esc键可提示方法参数

editor->Show completions,代码提示
view->show tool bar

右击xcode .m 文件 ->reveavl in project navigator ,可以关联到文件在项目列表中的位置

取消自动换行 Preferences -> Text Editing -> Indentation 选项卡 -> 去掉 Wrap lines to editor with  前的复选即可 
					显示行号 复选line numbers
不支持使用tab缩进  ,选中使用 cmmand + [ 和 cmmand + ] 缩进
	选中程序块,然后command+/,  每行前面加//的方式注释

建立项目时,有一个复选use Automatic Reference Counting(ARC),就不用再写release ,retain,编译器自动加这些代码

单击项目进入项目属性->PROJECT中的项目名选中->Apple LLVM Compiler 4.1-Language卷栏中,Objective-C Automatic Reference Counting(ARC)设置Yes/No
在TARGES中选中项目->Build Phases 标签->Compile Sources标签,把所有要参与编译的原文件加入	

加库  选中项目Target->Build Phrase->Link Binary with Library->加库
----

加应用图标,拖图标文件文件(MacOS应用只支持icns格式,iOS可支持png/jpe)到Xcode的Supporting Files目录下(也可右击项->add file to ""),在对话框中复选Copy Item ,打开XXX-info.plist文件 设置icon file属性(如没有增加)值为图片文件名

Symbol Navigator面板显示 所有的类及中的方法,可快速定位方法,Search Navigator可以查找方法

右击文件->Reveal in project navigator,关联选中
右击文件->Refactor->Rename

Build Setting->Search Paths栏->Header Search Path->Debug/Release中 设置.h文件所在目录 ,有Library Search Paths

项目中建立子项目,父项目 Targetr 的Build Phrase->Target Dependencies 增加子项目,如果子项目是库也可在Link Binary With Libraries 增加子项目的.a文件引用


Target中的Build Settings ->Packaging->Wraper Extension 设置生成的文件的扩展名

Xcode中 双指左右拖动可以向前/向后切换文件,像Safari 

修改拖入组件在大纲中的显示名字，在Identifier Inspector->Document->Label中修改

---产品
项目的.plist文件中Localization native development region默认为English修改为Auto
			Required background modes的item 0设置为App provides Voice Over IP Services
			Application is background only设置为NO

Build Setting中的设置，Targets中共有4列,Resoved列是最后设置的值，图标与Target的图标相同的列(Resovled列一般使用这个列),图标与Project图标相同(修改这个列对应上面的Project中也会修改),iOS Default列是默认值
Linking->Other link flag->Debug/Release中设置 －ObjC -force_load "path/to/file.a"

Search Paths-> 修改Always Search User Paths为Yes
								Header Search Paths中设置.h所在路径
Activecures->Build Active Architecture Only to 设置Yes
							Supported Platforms 下拉others...->把iphonesimulator删除,就不可用模拟器了
							Valid Architectures中有armv7s
Deployement->Targeted Deivce Family 设置为iPad,
							Use Separate Strip 设置为Yes
---
如何区分 方法是自定义的,还是实现delegate的方法的可选的,必须的,还是覆盖父类的 ???? ,也不能每个方法上按alt+单击啊
对过时的API 没有提示 ???
---只 Mac OS
传参数
菜单product->Manage Schemes->选择一个项目,点击Edit,arguments标签可以传参数和设置环境变量,Diagnostics标签有一些复选项

Build Settings －> Base SDK 中选择OS X 10.8

---只 iOS
Xcode ->Open Developer Tool->iOS Simulator

Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.0.sdk/ 是SDK目录
~/Library/Application Support/iPhone Simulator/6.0/Applications/  下生成.app

Xcode使用Simulator时如error: failed to attach to process ID 0 ,清理这两个目录
~/Library/Application Support/iPhone Simulator/6.1/Applications
~/Library/Developer/Xcode/DerivedData


iPhone Configruation Utility安装在/Applications/Utilities/目录下

指南针,不能自动旋转屏幕,要在.plist文件中的Supported Interface orientations和(iPad)两个中删除只留一个Portrait(bottom home button)

打开iTunes ,command + o 打开生成的.app, 会生成在/Users/zhaojin/Music/iTunes/iTunes Media/Mobile Applications/下有生成的.ipa文件
iTunes 中选择左侧的Apps项,后把自己生成的.ipa拖入(file->Add to library->选择.ipa),左侧DEVICES栏中选择自己的设备->复选Sync Apps->apply按钮->提示会覆盖->Apply/Sync按钮->提示中点Don not Authorized

Bundle identifier用来区分不同的应用,如多个两个应用使用相同Bundle identifier
如修改Bundle identifier在模拟器上不能测试,只是黑屏,只要撤消修改,启动模拟器把原来已有的应用删除,再设置正确的值就就可在模拟器上测试了
如真机上报Could not change executable premissions on the application,要把原应用删除再安装

---发布真机应用
修改项目的iOS版本以真机版本对应,Targets中Build Settings->Deployment栏->iOS deployment target中选择正确的版本

应用ID
Apple官方网上,下载.mobileprovision文件,是配置关联的设备ID
KeyChain Access -> Certificate Assistant ->Request a Certificate from a Certificate Authority->输入email(可以不是appleID)和名字信息 ->生成CertificateSigningRequest文件
Apple官方网上,上传CertificateSigningRequest文件,生成cer文件,下载本地,导入KeyChain Access,展开,右击导入的->export Xxx->生成.p12私有文件(要输入密码)

推送证书:


签名Provision :先安装.mobileprovision(会打开iPhone Configuration Utility),再安装.p12文件(会打开KeyChain,要密码的)

Targets 项目->Build Settings->Code Signing卷栏->在Release/Debug中选iPhone Distrution(默认是iPhone Developer),在子级的Any iOS SDK中选iPhone Distribution对应的证书
Product->Edit Schema->Archive->Build Configuration中选Release,上面Destination下拉选择真实的iPad
Product->archive 开始编译(应用的-info.plist文件中的Bundle identifier的值要和证书中的值一样),编译成功后会打开Organier窗口的Archives标签
->点Distrubte...->选择Save for Enterprise or Ad-Hoc Develoyment->Code Signing Identify:中默认(是本机上的证书对应的项)->保存为.ipa文件

如提示timed out waiting for app to launch,可以看到背景色,是证书的原因,不可以调试,但包可以安装到真机上

打开iPad的开发功能,Xcode->Orgnization->XXX's Ipad ->中打开（如找不到XXX's iPad 就说明CommandLineTool版本过低）,运行按钮就可以选择XXX's iPad

//============== 上 Xcode
//==============Interface Builder 组件属性
IB开头的类表示InterfaceBuilder,如 IBOutlet ,IBAction

Xcode把Interface Builder集成在一起了 ,是用来设计界面的工具 

ctrl+左键拖动 = 右键拖动
按钮上右键->菜单中有事件关联的方法
 
设置界面中选中文本框->Binding Inspector面板->选中binding to 下拦选择Key Value Coding App Delegate,在Model Key Path 中输入自己的属性名,如count
(可选 复选Conditional Sets Editable),就可使用[self setValue:20 forKey:@"count"]


Object Library 库中选择->Cocao->Controls 里有基本的 按钮,文本框
Object Library 库中选择->Cocao->Object & Controls ->Object 把它拖到左边工具栏中
在Identify Inspector面板中->Class中输入已经建立的类名,右击它,可以把类中的 IBOutlet属性,IBAction方法,可关联到界面中的组件

Object Library 库中选择->Cocao->Windows & Menu->有Windows组件可以拖入,可以在Identify Inspector面板中->Class中输入自己的类继承自NSWindow

可以打开两个窗口,在设计界面右键拖动到另一个窗口的.h代码中,弹出对话框输入变量名,会自动生成代码

---Mac
的Mac界面设计文件为.xib
事件关联可从 按钮 拖向 File's Owner,也可从 File's Owner 拖向 按钮,右击组件或者  File's Owner 弹出菜单

main.m 中的 return NSApplicationMain(argc, (const char **)argv);增加Cocoa.framework,#import <Cocoa/Cocoa.h>
Target中的Build Settings ->Packaging->Info.plist File中设置.plist文件名为"<项目名>/<项目名>-Info.plist",路径是相对于.xcodeproj文件所在的路径
建立<项目名>-Info.plist文件,右击根->Property List Type->info.plist
保存后会提示输入,加Main nib file base name配置项(n=nextstep),(值没有.xib) (XML格式,ib=Interface Buidler)
加Principal class配置项的值为NSApplication, 如何做 simple.app/Contents/MacOS ???

.xib文件中拖一个View Controller在Attribute Inspector标签中的Nib Name属性:选择建立的xib(window)
.xib文件中拖一个Object,Identity Inspector中关联自己的Delegate, 的File's Owner的delegete属性关联自己的delegate,即是入口

选中一组控件,Editor->Edmbed in ->Box,会把这组控件放在一个Box中,可以整个Box放大是调整布局
选中透明区的窗口,属性面板中可以取消 resize 复选框,也有visible at lancth

设计器中的 点MainMenu,也可以把菜单项 像使用 按钮一样 关联 处理函数 

---iOS
iOS界面设计文件为.storyboard
事件关联可从 按钮 拖向 ViewController,也可从 ViewController 拖向 按钮 ,右击组件或者 ViewController 弹出菜单

main.m 中的 NSApplicationMain 会关联自己Delegate(.h中必须加@property (strong, nonatomic) UIWindow *window;),读<项目名>-info.plist文件,iOS应用会读Main storybaord file base name配置项MainStoryboard_iPhone.storyboard 和 MainStoryboard_iPad.storyboard
如.storyboard中有多个ViewController,以界面中有 "->" 图标的为入口,可拖动修改,.storyboard文件可关联自己的ViewController

//-手工建立xib项目,???
建立一个iOS empty application->new File->选User Interface中的window 保存为.xib文件->从Object & control中拖一个Object图标到左边的Objects栏中->修改Object图标关联的类为生成的Delegate类
把生成的Delegat.h中window属性修改为IBOutlet,右击Delegate图标->把其中window属性和window界面关联起来
File's Owner的 Custom Class 为 UIApplication
File's Owner的 delegate 与 App Delegate 关联起来

建立一个ObjectC class 基于UIView,建立一个ObjectC class 基于UIViewController
在Delegate.m 的 didFinishLaunchingWithOptions中是第二行加入ViewController,
    self.viewController = [[ZHMainViewController alloc] init];//实例化ViewController
    self.window.rootViewController = self.viewController;//指定ViewController
//---
建立Tab Bar 项目,拖入 View Controller ,使用ctrl+单击(或右击)Tab Bar Controller 到View Controller弹出菜单选择Relationship Segue组中 view controllers
会自动在Tab Bar Controller中加新的标签按钮,展开View Controller选中Tab Bar Item,在Attribute Inspector中修改Badge是提示信息数,Identifier修改图标类型
自己建立ViewController类,选中面板在Identifier Inspector->Custom class中关联自己的ViewController类

文本框的Attribute inspector->复选Auto Enable return key,开始未输入文本前enter是不可用的
右击文本框把Did End On Exit 事件关联,在文本框输入文本后按enter键盘才会消失,否则键盘不会消失
[txtName resignFirstResponder];//让键盘消失
[txtName becomeFirstResponder];//使用键盘



//===============Instruments 是性能监控工具
Xcode菜单->Open Developer Tool->Instruments

//=============Mac OS X ---Cocoa
Xcode中也有sys/socket.h ,pthread.h, 

GLKit 

Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk/System/Library/Frameworks/GLUT.framework

AppKit
 
自动 #import<Cocoa/Cocoa.h>
选中MainMenu.xib文件时入设计器,要点Window图标后在显示的窗口中设计界面
选择按钮,有一个Key Equivalent ,在框中按回车,设置快捷键
在把按钮右键拖动到 App Delegate 图标上,才有显示处理方法

#define singleObj [ZHUserBean singleInstance]  // #define的使用
#define ZHLog(str) NSLog(@"%@",str)

+(ZHUserBean*)singleInstance //单例模式
{
    static id single=nil;
    if(single == nil)
    {
        single=[[self alloc]init];
    }
    return single;
}


+(void)initialize//NSObject的方法,static方法
{
	//会记录在 ~/Library/Preferences/<包名>.<项目名>.plist,
    //包名是配置在 Supporting Files/<Project>－info.plist文件中bundle identifier中

	NSUserDefaults * userDef=[NSUserDefaults standardUserDefaults];
	NSString *setting = [NSString stringWithFormat:@"http://www.baidu.com\n" @"http://bing.cn"];
	NSDictionary *dict= [NSDictionary dictionaryWithObjectsAndKeys:setting,@"urls",nil];
	[userDef registerDefaults:dict];//加新的记录
}
-(void)awakeFromNib// 是　NSNibAwaking的protocol
{
	NSUserDefaults * userDef=[NSUserDefaults standardUserDefaults];
	NSString * urls=[userDef objectForKey:@"urls"];//取值
	[textURL setString:urls];
}

-(IBAction)openBrowserClicked:(id)sender
{
	NSString* urls=[textURL string];
	NSArray* array=[urls componentsSeparatedByString:@"\n"];
	for(NSString* url in array )
	{
		[[NSWorkspace sharedWorkspace]openURL:[NSURL URLWithString:url]];//使用浏览器打开URL
	}
}
-(void)applicationWillTerminate:(NSNotification *)notification//AppDelegate的方法
{
    //要在Dock中退出，而不是在Xcode中停止
	NSUserDefaults * userDef=[NSUserDefaults standardUserDefaults];
	[userDef setObject:[textURL string] forKey:@"urls"];
	[userDef synchronize];//更新值
}


//下拉选择Objects& controllers 拖动Object到设计界面左侧工具栏上，属性面板选择关联的类名，如删除，选中按delete
//下拉选择Windows & Menus 可拖窗口，选中窗口在Attribute Inspector中取消复选Resize,Visible At Launch
-(IBAction)submitClicked:(id)sender
{
	NSLog(@"当前调用的方法是:%s",__PRETTY_FUNCTION__);

	singleObj.userName=[textUsername stringValue];
	singleObj.userPass=[textPassword stringValue];//NSSecureTextField
    
    NSLog(@"登录验证结果,%d",[singleObj checkLogin]);
    ZHLog(@"日志");
    
    //--调用系统命令 , 管道
	NSString* value=[NSString stringWithFormat:@"%@ %@" ,singleObj.userName,singleObj.userPass];
	NSArray* arguments =[NSArray arrayWithObject:value];
	NSPipe* pipe=[NSPipe pipe]; //Pipe
	NSFileHandle *file =[pipe fileHandleForReading];//使用临时文件来读管道
	
	NSTask* task =[[NSTask alloc]init];
	[task setLaunchPath:@"/bin/echo"];//调用系统命令，可用curl 命令做 webService client
	[task setArguments:arguments];
	[task setStandardOutput:pipe];//命令 标准输出 到 管道(是文件)
	[task launch];
	
	NSData* data=[file readDataToEndOfFile];//文件->管道->标准输出 ,读,如不启动任务，这里会阻塞
	NSString *string =[[[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding]autorelease];
	NSLog(@"get user and password is :%@",string);
	[task release];

 	//#progma mark -
    //#progma mark MyParseXML
   //mark 后的内容,只起标记作用,相当于标签,可以快速定位, 如是- 是分隔线,编译失败????
    
    //--解析XML
	NSString *xmlString=@"<root><userid>123</userid><age>30</age><birthDay>1988-08-08</birthDay></root";
	if([xmlString rangeOfString:@"error"].location == NSNotFound)
    {
        NSLog(@"验证成功");
    }
    
    NSError *error;
	NSXMLDocument *xmlDoc =[[NSXMLDocument alloc]initWithXMLString:xmlString options:NSXMLDocumentTidyXML error:&error];//&的使用
	NSXMLElement *root=[xmlDoc rootElement];
	NSArray* array=	[root nodesForXPath:@"//age" error:&error];
	NSString *age=[[array objectAtIndex:0]stringValue];
	NSLog(@"XML Parsed age=:%@",age);
	
	//--关闭窗口
	[NSApp endSheet:[sender window]];//sender window是过时的
	[[sender window] orderOut:self];

	//--弹Alert窗口
	NSAlert *alert=[[NSAlert alloc]init];
	[alert addButtonWithTitle:@"OK"];
	[alert setMessageText:@"我的提示噢！"];
	[alert setInformativeText:@"内容"];
	[alert setAlertStyle:NSWarningAlertStyle];
	
	[alert beginSheetModalForWindow:mainWindow modalDelegate:self	 didEndSelector:nil contextInfo:nil];

}
-(IBAction)cancelClicked:(id)sender //关闭窗口,sender 是按钮
{
	[NSApp endSheet:[sender window]];//sender window是过时的
	[[sender window] orderOut:self];
}
 -(void)mySelector:(NSNotification*)anotification //所有的文本框改变都会被调用
{
	NSUInteger size=[[textUsername stringValue] length];
	NSString* statusText=[NSString stringWithFormat:@"The Username count:%ld" ,size ];
	
	NSAttributedString *attr=[[NSAttributedString alloc]initWithString:statusText attributes:boldYellowDict];//修改文字外观
    [textStatus setAttributedStringValue:attr];
    
    //[textStatus setStringValue:statusText];
}
-(void)awakeFromNib //NSNibAwaking Protocol
{
	[[NSNotificationCenter defaultCenter]addObserver:self
											selector:@selector(mySelector:)
												name:NSTextDidChangeNotification
											  object:nil];//注册通知
	//设置颜色
	[textStatus setFont:[NSFont fontWithName:@"Message" size:18]];
	[textStatus setTextColor:[NSColor redColor]];
	
    boldYellowDict=[[NSDictionary alloc]initWithObjectsAndKeys:
                            [NSColor yellowColor],NSForegroundColorAttributeName,
                            [NSFont boldSystemFontOfSize:11.0],NSFontAttributeName,
                            nil];

}
- (void)dealloc
{
	[[NSNotificationCenter defaultCenter]removeObserver:self
												   name:NSTextDidChangeNotification
												 object:nil];//取消注册通知
	[boldYellowDict release];
	[super dealloc];
}


array = [[NSMutableArray alloc] init];
NSNumber *value = [array valueForKeyPath:@"@count"];
value = [array valueForKeyPath:@"@max.width"];//width是Array中的类的属性
value = [array valueForKeyPath:@"@min.width"];
value = [array valueForKeyPath:@"@sum.width"];


//Dock中显示新消息数
NSDockTile *dockTile = [NSApp dockTile];
[dockTile setBadgeLabel:@"10"];
[dockTile display];

-(NSMenu *)applicationDockMenu:(NSApplication *)sender //Dock上加菜单,NSApplicationDelegate,也可.xib中右击Application->dockMenu关联菜单

{
    NSMenu *menu = [[[NSMenu alloc] initWithTitle:@"DocTile Menu"]//修改默认标题
                    autorelease];
    NSMenuItem *item = [[[NSMenuItem alloc] initWithTitle:@"Hello"
                                                   action:@selector(hello) keyEquivalent:@"k"] autorelease];
    [menu addItem:item];
    return menu;
}

//加ExceptionHandling.framework
Build Settings->Apple LLVM compiler4.2 - Lanugae->Enable Objective-C Exceptions

[[NSExceptionHandler defaultExceptionHandler] setDelegate:self];
[[NSExceptionHandler defaultExceptionHandler] setExceptionHandlingMask: NSLogAndHandleEveryExceptionMask];

@try {
	@throw [[NSException alloc] initWithName:@"BadException" reason:@"Illegal State Detected" userInfo:nil];
	@throw [NSException exceptionWithName:@"BadException" reason:@"Illegal State Detected" userInfo:nil]; 
}@catch (NSException *e) {
		[[NSApplication sharedApplication] reportException:e];
}
//-(BOOL)exceptionHandler:(NSExceptionHandler *)sender shouldHandleException:(NSException *)exception mask:(NSUInteger)aMask
//{ }
- (BOOL)exceptionHandler:(NSExceptionHandler *)sender shouldLogException:(NSException *)exception mask:(NSUInteger)aMask {
	return YES;
}

//-------.framework
Targets->Build Setting->Installation Directory ，设置framework项目 的安装目录
Targets->Build Phases->Copy Headers->把.h文件移动到publics栏中

其它项目引用framework
Targets->Build Phases->Link Binary with Libraries栏中,可以从其它地方拖入到这里

/Library/Frameworks/Xxx.framework中的目录结构

./<project>  ->   ./Versions/Current/<project>
./Resources  ->  ./Versions/Current/Resources
./Headers  ->  ./Versions/Current/Headers
./Versions
./Versions/Current -> ./Versions/A
./Versions/A
./Versions/A/Headers 
./Versions/A/Resources/
./Versions/A/Resources/Info.plist ,strings文件,图片等
./Versions/A/<project>

Targets ->Build Phases-> Copy Headers->把.h拖入,才会放在framework文件中

//-------.dylib .a
Mac 的 DYLD_LIBRARY_PATH 环境变量相当于 Linux 的 LD_LIBRARY_PATH
.dylib 文件相当于 .so 文件
gcc add.o -dynamiclib -current_version 1.0  -o libadd.dylib  输出也是以lib开头使用时和linux一样,包括dlopen函数

//------pkg,mpkg安装包

//-------Firefox,Safari 插件
WebKit插件过时的,WebPlugIn 协议

Mac OS X 目录中多数文件以.plugin结尾的,插件检查顺序
~/Library/Internet Plug-Ins.
/Library/Internet Plug-Ins.

建立Application & Library ->Bundle 项目
Target中的Build Settings ->Packaging->Wraper Extension 设置生成的文件的扩展名plugin
info.plist文件的属性
	Bundle OS Type code(即原码为 CFBundlePackageType)  修改为 BRPL
	建立
	WebPluginMIMETypes 是Dictionary类型加自己的mime名字,类型也要是Dictionary中加一个key为WebPluginTypeDescription
	WebPluginName 
	WebPluginDescription 


XP_MACOSX 被定义为 1 
NPError NPP_New(NPMIMEType pluginType, NPP instance, uint16_t mode, int16_t argc, char* argn[], char* argv[], NPSavedData* saved)
{
	NPBool supportsCoreAnimation;
		if (browser->getvalue(instance, NPNVsupportsCoreAnimationBool, &supportsCoreAnimation) != NPERR_NO_ERROR)
			supportsCoreAnimation = FALSE;
		
	NPBool supportsCocoa;
	if (browser->getvalue(instance, NPNVsupportsCocoaBool, &supportsCocoa) != NPERR_NO_ERROR)
		supportsCocoa = FALSE;

	browser->setvalue(instance, NPPVpluginEventModel, (void *)NPEventModelCocoa);  
}



//-------GLUT
安装Command line tools后有 /System/Library/Frameworks/GLUT.framework
GLUT.framework有 <GLUT/GLUT.h> 会自动引入 <OpenGL/gl.h>,<OpenGL/glu.h>,有 #pragma comment (lib, "glut32.lib") ,依赖于OpenGL.framework

g++  -framework GLUT -framework OpenGL  helloOpenGL.c -o helloOpenGL 
CDT->C/C++ Build->Settings->Linker->Miscellaneous->在Linker flags中加入 -framework GLUT -framework OpenGL  测试OK
Tool Chain Editor 为MacOS GCC 应该有/Library/Framework 和 /System/Library/Framework 的include

#ifdef __APPLE__
    #include <GLUT/GLUT.h> 
#else
	#include "GL/glut.h"
#endif

 