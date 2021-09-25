https://docs.microsoft.com/en-us/
https://docs.microsoft.com/en-us/cpp/

MFC=Mirosoft Foundation Class
http://msdn.microsoft.com/en-us/library/ff468925%28v=VS.85%29.aspx   windows SDK
http://msdn.microsoft.com/zh-cn/library/d06h2x6e.aspx				 MFC 最新的类库,只是部分中文

Windows SIM (System Image Manager)  建立无人安装windows 的应答文件 ,创建基于 UEFI 的硬盘分区   包含在 ADK 
Windows Assessment and Deployment Kit (Windows ADK)

Windows Driver Kit (WDK, formerly DDK)  ,  WDK 8.1 Update (for Windows 8.1, 8, and 7 drivers) 依赖于 VS 2013 
WinDbg 
Windows Remote Debugging (Windows RD) 


vs2015.ent_enu.iso\packages\vcRedistD14\VC_redist.x64.exe				Visual C++ Redistributable Packages for Visual Studio 2015
vs2015.ent_enu.iso\packages\dotNetFramework\dotNetFx-x86-x64-AllOS-ENU.exe  版本 4.6

Tools->Error lookup 可以输入错误号,查看错误文本描述 C:\Program Files\Microsoft Visual Studio 10.0\Common7\Tools\errlook.exe 


VC2010 设置环境变量
	   path=C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\bin;C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE;
		lib=C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\lib;C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Lib
	include=C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\include;C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Include


VS2015  update 3 最后一版ISO文件(可能是最后一个运行在win7上)
	http://download.microsoft.com/download/6/4/7/647EC5B1-68BE-445E-B137-916A0AE51304/vs2015.ent_enu.iso
	http://download.microsoft.com/download/B/8/F/B8F1470D-2396-4E7A-83F5-AC09154EB925/vs2015.ent_chs.iso
	断网 激活密钥：HM6NR-QXX7C-DFW2Y-8B82K-WTYJV
	安装时不能选择安装目录,只选择C++,安装过程很慢,不如VC2010快,安装后有(C有MFC,ATL,CLR),C#,VB都有并且可以用,其它的虽然有但不能用
	VS 2015 带的Anroid SDK(要选择安装) 在 C:\Program Files (x86)\Android\android-sdk
	
	VS 2015 安装后的启动路径  C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE\devenv.exe
	VC2015 设置环境变量(可不设)
	   path=C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin;C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE
	    lib=C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\lib;C:\Program Files (x86)\Microsoft SDKs\Windows\v7.1A\Lib;
	include=C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\include;C:\Program Files (x86)\Microsoft SDKs\Windows\v7.1A\Include;

	//C:\Program Files (x86)\Windows Kits\10\Include\
	//C:\Program Files (x86)\Microsoft SDKs\Windows\ 有更高版本的 v10.0A\bin\NETFX 4.6.1 Tools目录
	
	
	Help->add remove help content 打开Help View-2.2,看到MSDN默认安装位置  C:\ProgramData\Microsoft\HelpLibrary2\Catalogs\VisualStudio14 
		把VisualStudio14重命名,把下载的离线MSDN解压在这,有中文的
	help->set help preference->lanch in help view (文档内容中英文和HelpView的界面语言相同)
		
	

VS 2017.15.8 (可win10上)下载离线版本 ，有Community版本
	 C++ desktop development  	安装时多选  MFC (在Visual Studio扩展开发组选中,再选中MFC 会安装C#.Net)
		
	也可双击出现界面选择要安装组件，选择下载保存目录，在安装的时候有选择语言包，不选择中文就行
	
	Tools->enviroment->international setting(区域设置),只有安装时的语言
	
	D:\Program\Microsoft Visual Studio\2017\Community\Common7\IDE\devenv.exe 是启动文件(默认安装在C:\Program Files (x86)\下)
	   path=D:\Program\Microsoft Visual Studio\2017\Community\VC\Tools\MSVC\14.15.26726\bin\Hostx64\x64
		lib=D:\Program\Microsoft Visual Studio\2017\Community\VC\Tools\MSVC\14.15.26726\lib\x64
	include=D:\Program\Microsoft Visual Studio\2017\Community\VC\Tools\MSVC\14.15.26726\include
	
	 
	安装后就不能多增加组件了，再安装总是提示安装目录不为空???
	不能建立MFC项目，BUG????????????????
	
Visual Studio 2019  Version 16.5 (安装要求.NetFramework 4.6 及更高版本)

Visual Studio 2019  C++ build tools 
https://visualstudio.microsoft.com/visual-cpp-build-tools/ 下载 vs_buildtools_xxx.exe  
	选 使用C++桌面开发-> 可选里 只选中MSVCv142 - VS 2019 C++ (可选windows 10 SDK)  ，大小为6.86GB
	默认下载在 C:\ProgramData\Microsoft\VisualStudio\Packages (实际上是安装的解压目录,一个包下载在哪?)
	默认安装在 C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools 安装后大小为2.36GB , 有VC#目录大小只为3M 
	下拉选择download all then install
	安装中提示 Dot Net版本为4.7.2,安装 263 个包 (可在windows 7中安装)


	windows 10 SDK,可以安装在 windows 7 SP1 上,
	默认目录在	C:\Program Files (x86)\Microsoft SDKs\Windows Kits\10 	只有9MB
				C:\Program Files (x86)\Windows Kits\10\					大小1.46GB
					Lib\10.0.19041.0\ucrt
					Lib\10.0.19041.0\ucrt_enclave
					Lib\10.0.19041.0\um 
					
					ucrt=Universal C Runtime
					um=非托管
					enclave被包围的领土 


	设置环境变量，PATH是追加的
	   PATH=C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Tools\MSVC\14.28.29910\bin\Hostx64\x64  (里面有cl)
	    lib=C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Tools\MSVC\14.28.29910\lib\x64
			C:\Program Files (x86)\Windows Kits\10\Lib\10.0.19041.0\ucrt\x64
	include=C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Tools\MSVC\14.28.29910\include
			C:\Program Files (x86)\Windows Kits\10\Include\10.0.19041.0\ucrt
	或者
	开始搜索Developer,找到 Developer Command Prompt for VS 2019 输入 cl 命令
	
	
对 C++11, C++14, C++17，C++20 支持情况
https://docs.microsoft.com/en-us/cpp/overview/visual-cpp-language-conformance?view=msvc-160


------------	
项目删无用目录<Solution_Name>.VC.db 和所有的Debug目录


#pragma comment (lib, "glew32.lib") //cl编译器读.lib

cl /I "C:\temp\glut\include" /c  helloOpenGL.c
/D同gcc的-D
/Zi  生成完整的调试信息 ( Information )
/EHs 同步异常处理模型 (Exception Handling)

link helloOpenGL.obj /LIBPATH:"C:\temp\glut\lib"  //如果要生成.dll文件加  /DLL
/OUT:Test.exe 	输出文件名

.C 大C结尾的文件在cl编译器被认为是C,要用.cpp才是C++

dumpbin xxx.dll文件

VC 2010  编译openGL时报LINK : fatal error LNK1123: failure during conversion to COFF: file invalid or corrupt
项目-->工程属性->配置属性-> 清单工具->输入和输出->嵌入清单，选择[否] 

----------------------VS2010 - 2015 工具使用
Tools->options->Enviroment->General -> color theme :Dark

可以像用world 一样 ctrl+拖动变动字体大小 (只针对当前文件)
Tools->options->Enviroment->Fonts and Colors 默认下拉选的是Text Editor 设置Font:12

控制台字体 选Output window ,如菜单或者Solution Explorer要下拉选择Enviroment Font

Tools->options->Text Editor->C/C++->在 Display中 选中 Line Numbers

当输入 "->" 时有提示
代码提示 edit->inteliSense->列出成员(ctrl+J)用在点后,
						 参数信息ctrl+shift+space,　
						 完成单词 (alter + -> 始终有效)

格式化代码 ,ctrl+a 全选 edit->advance->format selection(ctrl+k,ctrl+F)
注释 ,选择代码 edit->advance->comment和uncomment ,是使用 /* */的形式

view -> Navigate Backward ctrl+-
view -> Navigate Forward  ctrl+shift+-

windows ->reset window layout
Debug->windows->breakpoints
工具栏上的"查找"按钮,可以查指定目录的指定类型的文件内容,也可当前Resolution,也可当前文档


VS2010 MSDN的安装方法
把VS2010.iso中的ProductDocumentation目录复制到D盘下(因为要生成解压目录,所以这个目录不能删,可以删Packages目录中的cab压缩包),
安装VS2010后在help->manage help setting->选择D:\ProductDocumentation->单击install content from disk链接->选择 D:\ProductDocumentation\HelpContentSetup.msha文件
->取消不用的文档如C#,F#,VB,Team Foundation,SharePoint,Lifecycle,	Office,JSCript(.NetFramework 4)
->点Update按钮,安装后

VS2010中help->view help 后(注意选择Y/N,可能会打开浏览器,如果安装完SP1后应该不会了,开始菜单中有documentation 项)可以在index标签中查找,
也可Visual Stuido->Visual Studio Languages->Visual C++->Visual C++ Reference->Visual C++ Libarary reference->MFC Reference->MFC Classes

安装MSDN后,最好不删ProductDocumentation目录,如果VS 2010 重新安装ProductDocumentation 目录要重新复制


VS 2010 目录C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\atlmfc\src\mfc放源码
VS 2010 view->classView

VC Solution 的目录中,右击Solution->clean 会把Debug,Release目录中的所有内容删除
		可以把ipch目录删除,*.sdf文件是记录的是所有.h文件的索引,也可以删除,但打开项目时还要重新建立
显示空白字符
Edit->Advance->View White space

右击项目->properites->Configuration Properties->General->Character Set中选择Use Unicdoe Character Set,函数参数字串都要加L,如选择use Muli-Byte Character Set,中文就不用加L

VS2010 右击项目->Configuration Properties->General->Platform Toolset 中 V11修改为V100 
VS2013 右击项目->Retarget to windows 8.1

VS2010 属性面板非默认值怎么变成粗体显示????????

#ifdef __cplusplus
#endif

DebugBreak (); 相当于加断点
//=========================Win32 C API
线程的消息队列中的消息 MSG的结构体
程序和操作系统之间通信的格式,
typedef struct tagMSG
{
		HWND  ,//handle windows,窗口句柄
		UNIT message, //有WM_*  (window message)开头的常量
		WPARAM wParam,//整型,附加参数
		LPARAM lParam,//整型,附加参数
		DWROD time,//双16位整数
		POINT pt,//光标位置
}MSG;

HICON//图标句柄,
HCURSOR //光标包柄
HINSTANCE

WM开头的(window message)
WM_KEYDOWN//键盘按下,会接收到 WM_CHAR,查看文档会所按键的ASCII存入wParam
WM_LBUTTINDOWN//左键按下



windows为每个应用程序创建一个消息队列(先进先出),当windows 得到消息时放入队列,应用程序去取消息


windows 的main函数

int WINAPI WinMain(HINSTANCE,hInstance,  //当前实例号,如多个相同的窗口
					HINSTANCE hpervInstance, //previous  这个参数总是NULL,即没用的
					LPSTR lpCmdLine  ,//comand line,LP(long pointer),命令行的参数 
					int nCmdShow )//如何显示,最大化显示SW_MAXIMIZE,SW_SHOWMAXIMIZED,还是隐藏显示
//VS2010中设置命令行参数,右击项目->properties->在左侧中Configuration Properties->Debugging->有Command Arguments设置参数		





1.设计一个窗口类 
typedef struct _WNDCLASS
{
	UNINT style,		//CS_*  (Class Style)开头的  
	WNDPROC  lpfnWndProc,  //lp=long point,fn=function函数指针,WindowProc 类型的,每种Class Style有一个回调函数
	int		cbClsExtra,//类的额外的内存(所有窗口共享),通常0
	int		cbWndExtra,//窗口额外的内存,通常0
	HINSTANCE hInstance ,//当前实例号,如多个相同的窗口
	HICON	hIcon,//loadIcon()
	HCUSOR hCursor,//LoadCursor()
	HBRUSH hbrBackground,//H=Handle ,br=brush 背景色,GetStockObject()
	LPCTSTR  lpszMenuName,//CT=Const和_T宏,sz=size,菜单
	LPCTSTR  lpszClassName//起类名 ,RegisterClass和CreateWindow使用

}WNDCLASS,


WNDCLASS wndclass;
wndclass.style=CS_HREDRAW | CS_VREDRAW  //当窗口大小变化时,要重画(水平,垂直)
如要去除用style & ~CS_VREDRAW


//函数返回HICON ,最左上角的图标
loadIcon(
HINSTANCE hInstance ;//如使用标准图标(lpIconName),即windows自带的图标,这个要为NULL
LPCTSTR lpIconName;		//,可用IDI_ERROR 的常量 IDI_* (Identifier Icon) CT(const)
)

LoadCursor
CreateIcon
CreateWindow
DestroyWindow

LoadCursor ()//同loadIcon
//ID=identifier,C=Cusor,IDC_ARROW,IDC_*

wndclass.hbrBackground=(HBRUSH)GetStockObject(DKGRAY_BRUSH);//DK=Dark ,WHITE_BRUSH,*_BRUSH
//返回HGDIOBJ(GDI Graphci Device Interface),要强转 


2.注册窗口类
RegisterClass(
CONST WNDCLASS* xx    //WNDCLASS 的实例wndclass
)

3.创建窗口
HWND CreateWindow(
LPCTSTR lpClassName,//之前注册的类名 lpszClassName 的值,因为注册过
CPCTSTR lpWindowName,//标题栏显示
DWORD dwStyle,//WS_* windows Style,WS_OVERLAPPEDWINDOW有多个 | 组成的 ,WS_SYSMENU点图标出来的最小化和关闭菜单...,OVERLAPPED有标题栏和边框,WS_THICKFRAME 可调窗口大小,
int x,//如设置为CW_USEDEFAULT,会忽略y的值 ,CW_*=CreateWindow
int y,
int nWidth,//如设置为CW_USEDEFAULT会忽略height的值
int nHeight,
HWND hWndParent,
HMENU hMenu,
HINSTANCE hInstance, //WinMain函数传入的
LPVOID lpParam //void 指针,产生WM_CREATE消息时,
)//CreateWindow会产生WM_CREATE消息

4.显示及更新窗口
BOOL ShowWindow(
	HWND hWnd,
    int nCmdShow//SW_SHOWMAXIMIZED ,SW_* =ShowWindow
);
5.更新窗口,当有改变时有用
UpdateWindow(HWND hWnd);

//消息循环
GetMessage 从thread message queue 中取消息
BOOL GetMessage(          
	LPMSG lpMsg, //MSDN 上显示的是[out],输出参数
    HWND hWnd,//哪个窗口的,NULL表示当前线程的任何窗口的消息
    UINT wMsgFilterMin,//只取部分消息,如WM_KEYFIRST ,WM_MOUSEFIRST  
    UINT wMsgFilterMax//WM_KEYLAST ,WM_MOUSELAST  ,如min 和max 都是0,则取所有的消息
);
TranslateMessage(&msg) //把WM_KEYDOWN 转换成WM_CHAR消息(不影响原来的消息,会产生新的WM_CHAR消息), 即ASCII码
DispatchMessage(&msg) //分发给窗口处理函数去处理
//示例
MSG msg;
while(GetMessage(&msg,NULL,0,0))//这里并不是很好,应该在单独的线程中,没有消息时阻塞
{//如是WM_QUIT消息返回0
	TranslateMessage(&msg);
	DispatchMessage(&msg)
}


WNDCLASS 的 lpfnWndProc 要的回调函数形式
LRESULT CALLBACK WindowProc(
	HWND hwnd,
    UINT uMsg,
    WPARAM wParam,//第一个参数
    LPARAM lParam//第二个参数
);
 
//提示窗口
int MessageBox( //返回值是IDYES,ID*
	HWND hWnd,//父窗口是哪个
    LPCTSTR lpText,
    LPCTSTR lpCaption,
    UINT uType//MB_OK MB_*=MessageBOx
);


HDC //Handle  Device Context,DC和显示驱动(无论什么显卡都是一样的函数)交互
HDC GetDC(HWND);//画在哪个窗口,得到一个HDC

每次窗口发变化时重画,会产生WM_PAINT消息

BOOL TextOut( HDC hdc,int nXStart,  int nYStart, LPCTSTR lpString, 
  int cbString // number of characters
);

int ReleaseDC( HWND hWnd, HDC hDC);//和GetDC成对
//窗口重画时产生 WM_PAINT 消息
HDC BeginPaint( //可以得到一个HDC ,只能在 WM_PAINT 事件中使用
  HWND hwnd, 
  LPPAINTSTRUCT lpPaint//[out]  PAINTSTRUCT pt,传&pt ,系统去维护
);

BOOL EndPaint(	//和BeginPaint成对 ,只能在 WM_PAINT 事件中使用
  HWND hWnd, 
  CONST PAINTSTRUCT *lpPaint
);
GetDC和ReleaseDC 是一对,不能在WM_PAINT中使用

在写比较 == 时(IDYES==xxx) ,通常常量放前面,不易把 == 写成 =

点x 的关闭按钮时,或者alt+f4 会生产 WM_CLOSE消息
DestroyWindow() 调用后窗口已经没有了,会发送WM_DESTROY and WM_NCDESTROY 消息,通常应该在WM_CLOSE做提示,而不是在WM_DESTROY

PostQuitMessage(退出码传Wparam)//会产生 WM_QUIT 消息 放在线程的消息队列中

GetMessage()时如取的是 WM_QUIT 则返回0,while终止

DefWindowProc(hwnd,uMsg,wParam,lParam);//对我不关心的消息,让DefWindowProc, Def=Default 一定要加 在switch case 的default中



CALLBACK 定义为  __stdcall ,和__cdecl是两种不同的函数调用约定,对函数参数传递顺序,和 栈的清除有差异
__cdecl  是VC++默认的, 动态参数函数是 __cdecl 如printf
VS2010 中右击项目->Properties->Configuration Properties->C/C++ ->Advanced->中有一个 Calling Convention 的默认值是__cdecl,
可以下拉修改为__stdcall,还有一种__fastcall
cdecl 全称??????()
convention 协定, 习俗,   

#include <windows.h>

//=========================
sprintf();

C++中 struct 中可以有函数,而C语言中不可以

class 中默认是 private
struct 中默认是 public ,也可以加 private:

class Point , Point p可以调用构造函数

函数的重载,与返回值无关,参数最好不能有默认值
this->x=y; // this指针

class{};后一定要加;

public :
	Fish():Animal(2,3),a(2)//对a 初值,
	{
	
	}
private const int a;

子类的方法中调用父类的被覆盖的方法用Animal::beath();//作用域　
纯虚函数 virtual void fun()=0;


int a=5;
int &b=a;//b是a的别名，必须定义时初始化,&在定义时,与类型 int & 一起是别名，函数传参fun(int &a)
运行对话框中可以拖入文件


//防止重复声明
#ifndef XX
#define XX　1//可不加１
....
#endif 

//=========MFC============
//---------MFC源码分析
建MFC项目->选Single document,Project style选择MFC standard

生成代码的CMainFrame:CFrameWnd (2010 是CFrameWndEx) ,CFrameWnd:CWnd
			CXXAPP:CWinApp	(2010 是CWinAppEx)
			CXXDoc:CDocument
			CXXVIew:CView		CView:CWnd	
MSDN中都有一个Hierarchy Chart图表

MFC的源代码,搜索WinMain
C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\atlmfc\src\mfc\APPMODEULE.CPP
Active Template Library (ATL)
APPMODEULE.CPP文件 _tWinMain是宏定义 就是WinMain,选中右击-> F12 go to definition

CXXApp theApp 是一个全局的变量定义,即在main函数外面定义,没有在任何方法中 -> 所以 先进入CXXApp构造函数-> 再进入 _tWinMain 函数
与以前的hInstance的类似,theApp只有能一个

搜索CWinApp(CWinAppEx在afxwinappex.cpp中) ,在AppCore.cpp文件,CWinApp的构造函数,是一个带参数的,而且头文件中有默认值NULL
	CWinApp的构造函数中有 pThreadState->m_pCurrentWinThread = this;//保存了this指针,是指向子类的,即theApp

_tWinMain调用AfxWinMain函数,在WINMAIN.CPP中//Afx=Application Framwork 
CWinApp* aApp =AfxGetApp()//以Afx开头的是全局函数,指针是指向子类的,即theApp,是刚保存的this
aApp->InitApplication();
CWinThread *pThread->AfxGetThread();  //指针也是指向子类的,class CWinApp : public CWinThread
pThread->InitInstance();//虚函数,会调自己的InitInstance
pThread->Run()完成了消息循环

//--注册窗口
AfxEndDeferRegisterClass() 在WINCORE.cpp中,注册窗口类,(是在CView或者下面的CFrameWnd::PreCreateWindow中调用的)
//SDI=Single Document Interface,MDI=Multi 
调用了AfxRegisterClass(就在当前文本中) 又调用了::RegisterClass(VS2010是::AfxCtxRegisterClass)已经定义很多窗口类
后面调用了CMainFrame : CFrameWnd :CWnd 有一个PreCreateWindow函数 ,中有CFrameWnd::PreCreateWindow(c)的调用, 
会先调(VS2010是CFrameWndEx在afxframewndex.cpp),CFrameWnd::PreCreateWindow 在WINFRM.cpp中
		又调了AfxDeferRegisterClass()方法在AFXIMPL.H中被重定义为 AfxEndDeferRegisterClass()
//SDI单文档中先调用 AfxEndDeferRegisterClass, 再调用 PreCreateWindow (中调了 AfxEndDeferRegisterClass ),被调用了两次
//SDI 在CXXApp->InitInstance()中调用ProcessShellCommand()是就完成了注册(也是说它调用了AfxEndDeferRegisterClass),不要再跟了
//正常应是先PreCreateWindow,再AfxEndDeferRegisterClass

//--创建窗口
WINCORE.CPP有CWnd::CreateEx是被WINFRM.cpp中CFrameWnd::Create所调用,CreateEx是CWnd的方法,CreateEx不是虛函数(VS 2010是虛函数),
	//子类覆盖调用了父类,
	其实是调用了CWnd::CreateEx函数中又[又]调用了 PreCreateWindow(CREATESTRUCT &cs ),cs是引用(别名),是个虚函数
		调用前CREATESTRUCT有默认值,如果自己修改了,在产生窗口之前,使用自定义窗口外观
	调用的是CMainFrame的 PreCreateWindow ,参数要CREATESTRUCT结构体,CREATESTRUCT与CreateWindowEx(是CreateWindow的Ex扩展)函数的参数相同,只是顺序相反

//后面CFrameWnd::LoadFrame函数中调用了CFrameWnd::Create函数
//--显示窗口
CXXAPP:CwinApp里有m_pMainWnd指针,指向CMainFrame
m_pMainWnd->ShowWindow(SW_SHOW);
m_pMainWnd->UpdateWindow();
//--消息循环
在WINMAIN.cpp中pTHread->Run(); 
CWinThread *pTHread 

THRDCORE.cpp 中CWinThread::Run()中使用循环,调用PumpMessage() 注释中WM_QUIT ->
	又调用了AfxInternalPumpMessage() ->又调用了::GetMessage 和 ::TranslateMessage和::DispatchMessage
	//::表示使用全局函数,平台SDK函数
WINCORE.cpp中 AfxEndDeferRegisterClass 函数中 wndcls.lpfnWndProc = DefWindowProc; //这里不是全局函数::,使用消息映射

CDocument	数据处理作用,
CView 也是窗口,数据显示作用 ,在CMainFrame 的上面,
CXXApp只能有一个

CXXApp::InitInstance()方法中使用CSingleDocTemplate把View,Document,Frame组合一起
						AddDocTemplate(pDocTemplate)
CAboutDlg:public CDialog (VS2010是 CDialogEx)//帮助->关于... 菜单项的弹出窗口

MFC 类的一些函数,就不用再传HWND引用,因为存在CWnd类中m_hWnd成员中,当窗口销毁时(只是HWND的引用空间销毁),CWnd并没有销毁,要看作用域

CreateWindowEx 比 CreateWindow 多了一个参数  DWORD dwExStyle, 值是 WS_EX_*
---------- 
自己的界面代码写在CMainFrame::OnCreate函数中的最后,(WM_CREATE消息的响应)
BOOL Create(
   LPCTSTR lpszCaption,	//用 _T("title")
   DWORD dwStyle,		//BS_* =ButtionStyle,BS_DEFPUSHBUTTON(默认可按的) 可以和 windows style WS_CHILD (子窗口)一起使用 
   const RECT& rect,	//CRECT 结构体,或者传CRect类
   CWnd* pParentWnd,	//可以用this
   UINT nID				
);//MSDN中 Initialization 部分的函数
CButton 不能定义为局部变量,到} 时会析构,CButton::Create函数
VS2010工具 ,在Class View中可以右击类->add ->add Variable 加变量,通常名字前加 m_ ,m_button要放在类的属性中,不能是方法局部变量
要m_button.ShowWindow(SW_SHOW)SW_SHOWNORMAL,才可以显示
0,0原点是菜单栏的下方,如有工具栏就在工具栏上方,放在CXXView中就可以,但没有OnCreate函数,建立

VS2010工具 ,可右击CXXView->Class Wizard...->Message标签->选择WM_CREATE ->Add Handler...按钮,生成了OnCreaet方法->Edit Code按钮
	删除时最好也要用工具来做
0,0原点是在正常区域

CButton.Create函数中 dwStyle 参数中加 WS_VISIBLE 后就不用调用 ShowWindow 方法了
CXXView中使用GetParent()得到父窗口,即是CMainFrame,和在CMainFrame中的OnCreate方法效果是一样的
BS_AUTORADIOBUTTON 单选 ,BS_CHECKBOX 复选,BS_AUTO3STATE 可灰的复选

=====4 画
CMainFrame 不能捕获到鼠标单击的消息,CXXView是可以的,因为CXXView在CMainFrame上面

VS2010 Class Wizard...增加消息处理函数后
在CXXView.h  中加了 afx_msg void OnLButtonDown(UINT nFlags, CPoint point); 函数的声明
在CXXView.cpp中的 BEGIN_MESSAGE_MAP  和 END_MESSAGE_MAP 之间加了  ON_WM_LBUTTONDOWN() 的宏
				用来关联消息和响应函数,可是宏并没指定哪个函数,是规定必须指定的函数名吗,也可能是默认名字?????????

如父类把所有的消息对应一个虚函数,每个子类都要实现所有的虚函数,都有一个虚表,因类很多,这样浪费内存,MFC不使用
MFC使用消息映射,MFC内部维护一张表,根据产生消息找到对应带窗口句柄,再找到对应的窗口的指针,指针传给基类,基类调用WINCORE.cpp中的CWnd::WindowProc函数,定义为虚函数,在消息循环时会被调用
	又调用了OnWndMsg函数,有判断收到的消息是什么消息的代码,会看这个消息有没有响应函数,方法是在.h文件有没有afx_msg 和 在.cpp中BEGIN_MESSAGE_MAP有没有,如果有就调用

CXXView中使用HDC hdc= ::GetDC(m_hWnd) // CWnd派生出来的都有一个m_hWnd 成员是HWND类型,this是CWnd*,类型是不同的
::MoveToEx(HDC hdc,  int X,int Y, LPPOINT lpPoint);//移动点到一个新位置 ,保留老的位置
::LineTo( HDC hdc,int nXEnd, int nYEnd )//画线
记得要::ReleaseDC(...);

CDC类(Class DC ),画图相关的,,MSDN 的member有m_hDC 保存句柄 
使用CWnd的GetDC()返回一个 CDC * 
CDC *  h_cdc;
h_cdc->MoveTo();不是MoveToEx,参数是POINT结构体,可以传CPoint类

CClientDC(CWnd* )类,可传 this,GetParent(),继承自CDC,会自动调用GetDC和ReleaseDC,就不用手工调用了
CWindowDC (GetParent())类继承自CDC,会自动调用GetWindowDC, ReleaseDC,了不用手工调用了,可以仿问非客户区(只应用程序本身)
GetDesktopWindow() 得到桌面窗口,可以任何地方画

CPen(PS_SOLID,2,RGB(0,255,0)) ,第二个参数是宽度, 要 COLORREF,使用RGB ,三个255是白色,光学
 PS_*=PenStyle,PS_DASH 虚线 和 PS_DOT 点线 笔的宽度要<=1 

dc.SelectObject(CPen *) 使用新画笔,返回之前的老画笔,参数也可以是*CBrush,CPoint,CBitmap 

CBrush(COLORREF) 
CDC::FillRect(CRect(p1,p2), CBrush*)//要LPCRECT可以用CRect,因已经传入brush* ,就不用SelectObject


VS2010工具中从Class view 切换到 Resource View窗口->右击Icon目录(任何都可)->add Resource....->选择Bitmap->Import...后会自动创建Bitmap目录
																				 ->如单击New按钮,则是手工画
		或者右击Bitmap->InsertBitmap
 Resource View中可以看资源ID,即目录中的名字是IDB_BITMAP1
CBitmap bitmap;
bitmap.LoadBitmap(IDB_BITMAP2);//MSDN中如有initialized部分函数,
CBrush brush(&bitmap) ;

默认有一个白色的画刷填充
透明的画刷 HBRUSH GetStockObject(NULL_BRUSH);
CBrush* 	CBrush::fromHandle(HBRUSH)//static 的,把句柄转换为指针
CBrush* transparent=CBrush::FromHandle((HBRUSH)GetStockObject(NULL_BRUSH));
dc.SelectObject(transparent);
dc.Rectangle(CRect(p1,p2));//要使用SelectObject,因没有传 bush*

static 变量必须先初始化在,在类的外部也可以初始化 int Point::x=0;(中int必须要有)

WM_MOUSEMOVE,有没有拖动要自己判断
x=y=5;
绘画模式，CDC::SetROP2(R2_BLACK) 函数 ROP=Reverse Of Pixel   ,R2_MERGENOTPEN,反色,前景色与背景色混合模式

========5 文本
CWnd::CreateSolidCaret(int nWidth,int nHeight) 光标插入符,如宽和高为０，使用系统定义的
得到系统的，GetSystemMetrics()
//caret ^符号
ShowCaret ()//初始是隐藏

CDC::GetTextMetrics(LPTEXTMETRIC 指针接收值);//当前字体的信息

//TEXTMETRIC的成员,tm=TextMetric
tmAscent		//是英文格的上三行，	g,h
tmDescent		//是英文格的最后一行   
tmHeight		//=tmAscent+tmDescent
tmAVeCharWidth	//平均宽度值
tmMaxCharWidth	//最大宽度值		w比i要宽


CWnd::CreateCaret(CBitmap* ) //图形的插入符,光标闪动的是一个图片
CBitmap不要定义为局部变量,会析构,资源类


窗口重画时 产生 WM_PAINT 会调用OnDraw
OnDraw(CDC* pDC )//pDC要取消注释
{
	pDC->TextOut(x,y,"");
}

CString str类 可变长度，有重载+,=,+=  (C语言使用char * )
str.LoadString(UINT nID)使用资源ID
VS2010工具中 view->Resource View->String Table中建立,

路径城 (path  bracket), 和 SelectClipPath 一起使用
CDC::BeginPath()
	... 画一个区域,如Rectangle  
CDC::EndPath()//关闭,

CSize CDC::GetTextExtent (const CString &str )//返回字串在屏幕上所占用的宽,高,如有字间距
CSize 与SIZE结构体 可以互换使用(C/C++的结构体和类可以互换)
CDC::SelectClipPath(RGN_DIFF) //RGN_*=region ,写在EndPath()之后,
RGN_DIFF 表示以后画的不会影响Path中的图形
RGN_AND  表示以后画的只会影响Path中的图形

static SetCaretPos(POINT) //移动光标位置 
CString 的Empty()清空字串
'\r' ^M 是回车CR  ,MSDN查ASCII
'\b' ^H 是退格BS

COLORREF  dc.getBKColor()//得到背景色 BK=background
COLORREF dc.setTextColor(COLORREF )//返回上次的

CString 有Left(n),GetLength();
CString += (WCHAR)nChar;//中文,WM_CHAR消息的生成的响应函数,参数UINT nChar表示哪个键

CFont font;
font.CreatePointFont( int nPointSize, LPCTSTR lpszFaceName)//_T("楷体")
//系统字体C:\Windows\Fonts ,或 VS2010工具中Tools->Option...->Enviroment->Font and Color中 列出了所有的字体,不一定都支持
dc.SelectObject(&font);//设置字体

使用 CEditView类 和 CRichEditView类


dc.DrawText(const CString & str,LPRECT lpRect,UNIT nFormat) //卡拉OK 平滑的变色字
 //rect只对矩形范围内字符显示 ,nFormat的值DT_*=DrawText, DT_LEFT左对齐

//定时器,每隔X毫秒,调用一次 
CWnd 的 UINT_PTR  SetTimer(id,毫秒,回调函数) //如执行成功返回值与id同,发送WM_TIMER消息,带id
回调函数如设置为NULL,WM_TIMER会放入消息队列,CWnd会处理,要手工加消息响应函数OnTimer(UINT_PTR nIDEvent)
回调函数原形(VS2010是变的),传参是注意不能是类的函数
void CALLBACK TimerProc(
			HWND hWnd, // handle of CWnd that called SetTimer
			UINT nMsg, // WM_TIMER
			UINT_PTR nIDEvent // timer identification,只对自己的定时器做处理 
			DWORD dwTime // system time
			);


==============6 菜单 
VS2010工具中 Resource View ->Menu->IDR_MAINFRAME 双击打开,在最右边可以双击,或者右击->insert new
右击菜单->properties出现属性窗口 Popup属性为true,则不可修改ID属性,Popup为false才可修改ID属性
可以右击菜单->add event handler..也可在class wizard中(Commands标签中选择ID,消息中选择COMMAND ),Class中选择CMainFrame

用ClasWizar删除,会注释代码
AfxMessageBox(_T(""));//同类的MessageBox,和::MessageBox

如果同一个菜单项在每个类中都加处理函数,最先响应 View->Doc->CMainFrame->App, 如果有一个类响应了,后面的类就不做了

标准消息(除WM_COMMAND外,WM_*,如WM_CHAR),从CWnd继承的类可以接收这类消息
命令消息(WM_COMMAND) 来自菜单,工具栏按钮的消息, 从CCmdTarget 继承的类可以接收这类消息,CWnd:CCmdTarge ,通过wParam参数传递 ,使用ON_COMMAND宏做消息映射
通知消息(WM_COMMAND),控件消息,列表框的选,是为向父窗口通知,从CCmdTarget 继承的类可以接收这类消息,

CWinApp和 CDocument是继承自 CCmdTarget ,所以不能接收标准消息

CMainFrame先收到命令消息交给子窗口CXXView ->如果没有响应 CView再交给Doc->
如果Doc也没有响应交回给View->View再交回CMainFrame->如MainFrame没有处理交给CXXApp
     Frame->View->Doc
App<-Frame<-View<-


CMenu* one=GetMenu();//返回的是菜单栏
//VS2010 在CMainFrame的OnCreate方法中返回的是NULL,
//原因是建立项目时在User Interface Features的界面中单选默认是Use a Menu bar and toolbar,侧在OnCreate方法中没有建立菜单
//应该选择 Use a classic menu


CMenu*  CWnd::GetMenu()->GetSubMenu(0) //GetSubMenu(0)是"文件"菜单 ,再根据ID或位置才能找到新建
CMenu 封装了 HMENU 
CMenu 的CheckMenuItem(UINT nIDCheckItem,  UINT nCheck )//复选
	如nCheck的值为MF_BYCOMMAND,nIDCheckItem表示菜单ID号
	如nCheck的值为MF_BYPOSITION,nIDCheckItem表示菜单的索引号,从0开始
	MF_CHECKED或MF_UNCHECKED可以 和 MF_BYCOMMAND或MF_BYPOSITION  进行|操作
								MF_*=Menu Function

BOOL SetDefaultItem(//菜单项以粗体显示
   UINT uItem,
   BOOL fByPos = FALSE //如为FALSE则uItem是ID号,如TRUE则uItem是索引
);
注意分隔线也占一个索引
SetDefaultItem 一个下拉菜单列表只有最后一次调用的才是粗体显示的

BOOL SetMenuItemBitmaps(//带图标的菜单 
   UINT nPosition,
   UINT nFlags,//参数也有MF_BYCOMMAND,MF_BYPOSITION,
   const CBitmap* pBmpUnchecked,//选中的图形,
   const CBitmap* pBmpChecked //未选中的图形
);
位图宽高大小应使用下面
GetSystemMetrics(SM_CXMENUCHECK)//13,得到操作系统信息,SM_*=SystemMetric, SM_CXMENUCHECK 菜单图标X宽
GetSystemMetrics(SM_CYMENUCHECK)//13,Y高度

CString 有Format();像printf

启用或者禁用菜单
UINT EnableMenuItem(
   UINT nIDEnableItem,
   UINT nEnable //MF_BYCOMMAND,MF_BYPOSITION, MF_ENABLED,MF_DISABLED | MF_GRAYED 
);
MF_DISABLED表示不能用,但不为灰 MF_GRAYED为灰但可以使用,应两个一起使用
使用EnableMenuItem时 要在CMainFrame::CMainFrame构造函数中加 m_bAutoMenuEnable=FALSE;才行
m_bAutoMenuEnable=FALSE//MFC不会使用命令更新机制,要自己维护(加UPDATE_COMMAND_UI响应函数),如某个菜单项变化的显示 

SetMenu(NULL)//删菜当前菜单栏
CMenu menu;  //应定义为类的成员
menu.LoadMenu(ID)//
SetMenu(&menu);//可动态换菜单
menu.Detach()//当menu是局部变量,一定要Detach(),把HMENU和CWnd 断开,destroy窗口时不报错



//谁捕获UPDATE_COMMAND_UI消息,就自动创建一个CCmdUI对象,仅用于弹出式菜单项,不能应用于永久显示的菜单栏上的菜单
让剪切变可用 classWizard 中找剪切ID_EDIT_CUT,消息选 UPDATE_COMMAND_UI
生成的函数	CMainFrame::OnUpdateEditCut(CCmdUI*pCmdUI)
if(ID_FILE_NEW==pCmdUI->m_nID)//m_nID 成员,判断菜单项是不是自己的
if(0==pCmdUI->m_nIndex) //m_nIndex成员,工具栏的索引和菜单项的索引可能不同,最好用ID号
pCmdUI->Enable(TRUE)//启用
pCmdUI->Enable(FALSE)//禁用
如菜单项项和工具栏的按钮修改一个,两个同时更新,只要取相同的ID

derive起源(派生)

右键菜单,在右键单击的消息处理函数中加
弹出使用CMenu::TrackPopupMenu(TPM_LEFTALIGN,x,y,CWnd* own)//新MSDN的lpRect参数已被忽略,有TrackPopupMenuEx新方法
TPM_*=TrackPopupMenu,TPM_RIGHTBUTTON 可以使用左右键选择菜单
x,y表示的是screen coordinates即从显示器左上角开始为0,0
ClientToScreen(&point)//从客户区坐标转屏幕坐标 ,point参数进出的参数
在Resource View 中建立菜单时的菜单栏是不显示的
菜单项增加COMMAND消息处理
右键子窗口先收到,如没有响应才交给父窗口(当然要TrackPopupMenu方法own 指向父窗口)

//动态增加菜单
BOOL AppendMenu(
   UINT nFlags,
   UINT_PTR nIDNewItem = 0,
   LPCTSTR lpszNewItem = NULL 
);
nFlags可为MF_STRING,如nFlags设置为MF_POPUP是没有ID的,nIDNewItem的值为菜单的HMENU

CMenu menu;//如是局部变量,记得要Detach();
menu.CreatePopupMenu()//创建空的
GetMenu()->AppendMenu(MF_POPUP,(UINT_PTR)menu.m_hMenu,_T("append动态菜单"));//m_hMenu成员,在菜单栏中加一个菜单
//m_hMenu保存句柄,所有的都有一个保存的句柄
menu->AppendMenu(MF_STRING,ID_MYDYN_MENUITEM,_T("动态的菜单项1"));
menu->AppendMenu(MF_STRING,112,_T("动态的菜单项2"));
menu.Detach();

BOOL InsertMenu(
   UINT nPosition,
   UINT nFlags,//可为MF_BYCOMMAND,MF_BYPOSITION,如有 MF_POPUP, nIDNewItem是Popup菜单的HMENU
   UINT_PTR nIDNewItem = 0,
   LPCTSTR lpszNewItem = NULL 
);
GetMenu()->InsertMenu(2,MF_BYPOSITION|MF_POPUP,(UINT_PTR)menu.m_hMenu,_T("insert动态菜单"));
MF_BYCOMMAND,是在指定位置的前面

DeleteMenu(..);

//动态菜单加事件处理函数
Resource.h文件中加入自己的菜单ID
	#define ID_MYDYN_MENUITEM 1001 //注意,开头数字最好是已经有的,否则再生成其它代码时,会把这个删除
CMainFrame.h  部分中加入
	afx_msg void OnMyDynMenuItem();
CMainFrame.cpp 的 BEGIN_MESSAGE_MAP  部分中加入
	ON_COMMAND(ID_MYDYN_MENUITEM, &CMainFrame::OnMyDynMenuItem) //复制已有的修改一下
void OnMyDynMenuItem()
{
	//...
}
如不在CMainFrame中仿问菜单 如View中要调用GetParent()->GenMenu();
GetParent()->DrawMenuBar();//是CWnd的函数,对刚刚对菜单栏增加的菜单,要刷新才能显示
Invalidate(TRUE)//TRUE 擦背景,View重画

CString 的Find()//返回找到第一个字符的索引
集合类 CStringArray 类似于 CObArray,因为CStringArray 的构造方法带LPCTSTR参数的,而CString有操作符重载 LPCTSTR
	 有Add(x),GetAt(2),GetSize();

CMainFrame类的函数OnCommand是virtual的,只要我们重写,就不会让消息向下传给View类

classWizard中有virtual Function 标签可以重写 OnCommand ,第一个参数WPARAM 中的低字节low-order是菜单项的commandID
LOWORD(wParam)宏来取low-order,HIWORD()

CView*  GetActiveView();//可以在CMainFrame中的函数中得到CXView类的指针,就可以得到View中的成员

在#include "XXView.h"时,报一个不正常的错误,说少;,其实是引用了Doc类,没有再#include "XXDoc.h"

==============7 CDialog
Resource 视图中右击任何->add Resource ->Dialog->修改ID属性//也可只右击 Dialog
模态对话框显示时,必须关闭后才能做其它事
非模态对话框在显示时,可以做其它事

设计的关联类,双击对话框 建立新的类(或者选择对话框project->new class...,或者在 classWizard中的add class...下拉选择MFC class,base class中选择CDialogEx)
生成的类,构造方法中向父类传了Dialog设计的ID,来实现关联
 
CDialog的DoModal()方法 //模态对话框,可是局部变量 ,因程序会暂停,必须关闭才可继续,关闭要用EndDialog方法,点OK窗口销毁

CDialog的Create(id,CWnd* )//非模态 ,如果CWnd是NULL,使用当前Dialog的父窗口
非模态必须用 ShowWindow(SW_SHOW),不能是局部变量
可以定义为指针,放在堆内存中,在析构时销毁内存,点OK时只是隐藏,并没有销毁,必须覆盖OnOK,和 OnCancel方法,调用 DestroyWindow()

MessageBox(_T("hell"));//把"hell"  转 LPTSTR
或者
CString x;
x="hello";
MessageBox(x);

右击新加的按钮->add Event Handler,CButton的消息是 BN_CLICKED (通告消息),
CButton btn;
btn.Create("aa",BS_DEFPUSHBUTTON |WS_VISIBLE|WS_CHILD,CRect(0,0,100,100),this,123);

CButton也是CWnd也有 DestroyWindow()方法,
m_hWnd来判断窗口是已经建立还是消毁

CWnd::GetDlgItem(int nID);//得到Dialog中组件,传ID
GetWindowText(CString &save);//如在静态文本中调用
SetWindowText //修改文本

CString str;
if(GetDlgItem(IDC_NUMBER1)->GetWindowText(str),str=="NUMBER1")//,号表达式 ,CString可以用 ==
{}
Static Text(静态文本),默认ID是 IDC_STATIC,不能接收消息,修改Notify属性为true,才可变成动态的
 
virtual int GetDlgItemText(//组合GetDlgItem和GetWindowText
   int nID,
   LPTSTR lpStr,
   int nMaxCount 
) const;
GetDlgItemText(ID,LPTSTR,10);
SetDlgItemText

int a=atoi("12") //asci to integer 字符转数字
itoa("22",rec,10)//数字转字符,10进制

wchar_t result[20];
swprintf(result,L"%ld",-123.33);//数转字符用swpritnf

wchar_t str1[]={L"-123.33"};
double num1=wcstod(str1,NULL);

-----VC CString相关转换
从char* 到CString ,使用构造方法,CString str(char *)//不能有中文
从wchar_t * 到CString ,使用构造方法,不要在局部方法中使用数组,要使用 new 
从CString 到 wchar_t 转换 const wchar_t * w_str=str.GetString();//是不可修改的
CString类的GetBuffer() 方法返回的字串,是可以修改的,但在调用CString的其它方法之前必须要调用 ReleaseBuffer()
如把char*当二进制,可以强转到wchar_t*

//char*中文->wchar_t*中文
CString str=_T("abc中文");//因是宏,不能为变量
wchar_t* wcn=str.GetBuffer();
-----


GetDlgItemInt(ID,*是否成功,是否有符号) //会自动转换成整数,可负数,如不是数字会提示的

右击输入框->classWizard ->Member Variable 标签->可以为已有ID的控件关联一个变量,类型可是int,
 在DoDataExchange(CDataExchange* pDX)方法中生成代码了新代码
DDX_Text(pDX, IDC_EDIT1, m_num1); //DDX_开头系列函数,DDX=Dialog Data Exchange,用于将组件和变量关联 
DDV_MinMaxInt(pDX, m_mum1, 1, 100);//可设置数值范围,DDV=Dialog Data Vaildation

DoDataExchange 是由框架调用,关联输入框不更新时,要使用UpdateData,来重新取值
BOOL UpdateData(
   BOOL bSaveAndValidate = TRUE 
);
UpdateData(TRUE) //TRUE 正在重新得到数据
c=a+b;
UpdateData(FALSE)//FALSE表示正在初始化(放值)
因为文本框使用绑定int变量,在文本框中第一个字符输入字母时,会提示"Enter an Integer"(VS2010英文版),如果第一个是数字后面是字母报错
对设置范围的变量,如超出也会报错

可同时为一个CEdit 关联变量和另一个CEdit 
DDX_Control(pDX, IDC_EDIT1, m_edit1);


WM_GETTEXT 取消息
::SendMessage((HWND) hWndControl, (UINT) WM_GETTEXT,  
			(WPARAM) wParam,      // 字符最大数
			(LPARAM) lParam      // 接收字符地址

wchar_t ch1[10];
::SendMessage(GetDlgItem(IDC_EDIT1)->m_hWnd,WM_GETTEXTEX,10,(LPARAM)ch1);
也可用 GetDlgItem(IDC_EDIT1)或者 关联的m_edit2 的SendMessage方法
m_edit1.SendMessage(WM_GETTEXT,10,(LPARAM)ch1);

wchar_t *ch3=new wchar_t[10];
swprintf(ch3,L"%lf",-10.3+20.5);
m_edit3.SendMessage(WM_SETTEXT,0,(LPARAM)ch3);//WM_SETTEXT WParam没有使用,设置为0

SendDlgItemMessage(ID,...);//相当于GetDlgItem(ID)->SendMessage(..);


EM_GETSEL 消息, 取得CEdit中选区中选中的蓝色块文本
	EM=Edit Message
	wParam 是开始位置 LPDWORD双字节的
	lParam  是结束位置
EM_SETSEL 消息 设置CEdit中的文本为部分文本不选中状态,结束位置如为-1,表示到尾部

EM_GETSEL 要先得到焦点才可以生效,用CWnd的SetFocus() 设置焦点

---
对话框设计窗口中双击按钮自动加消息关联,进入代码
Picture组件,ID值默认也是 IDC_STATIC, 是静态的不能接收消息,拖成一条线,修改Sunken(凹陷的,sink的过去分词)属性为true,有立体感,像分隔线


CRect 的方法 IsRectEmpty 是否宽或高长度为0
	IsRectNull 只是判断四个坐标是否为0

GetWindowRect(&receive) 返回窗口区大小,位置是相对于屏幕的坐标,使用ScreenToClient()//把屏幕坐标转换为当前窗口的相对坐标

SetWindowPos  和 DeferWindowPos 设置窗口,Z次序,位置,大小
BringWindowToTop 把窗口放Z次序的顶部
SetActiveWindow 激活窗口 //alter+esc键可切换窗口
SetForegroundWindow 设置前台窗口//线程相关

BOOL SetWindowPos(          HWND hWnd,
    HWND hWndInsertAfter,  //HWND_TOPMOST 最顶层窗口
    int X,
    int Y,
    int cx,
    int cy,
    UINT uFlags//SWP_NOMOVE 会忽略x和y参数,SWP_NOZORDER 会忽略hWndInsertAfter参数 ,SWP_NOSIZE会忽略cx和cy参数

);//SWP=SetWindowsPos

可设图片组件的Visible属性 为False
只有OK按钮的 Default Button属性是True ,按回车时就相当于按这个按钮,双击OK增加的处理函数中有调用父类的OnOK

SetWindowLong(hWnd,GWL_WNDPROC,newVal); //改变窗口属性,如果调用成功返回之前的数值
也有GetWindowLong//GWL_*=GetWindowLong,GWL_WNDPROC 修改窗口处理函数,要在显示之前调用

WM_INITDIALOG 通知消息(VS2010 class wizard找不到,要重写OnInitDialog虚方法,但MSDN中有),当对话框创建完成,显示之前发送

WINPROC preProc=(WINPROC)SetWindowLong(GetDlgItem(ID)->m_hWnd, GWL_WNDPROC,(LONG)myfunc);//是一个类型,

myfunc 函数的写法查 WNDCLASS
LRESULT CALLBACK myfunc(HWND hwnd,
    UINT uMsg,
    WPARAM wParam,
    LPARAM lParam
){
	//::GetNextWindow(hWnd,GW_HWNDNEXT 或者 GW_HWNDPREV ,当前窗口的上/下一个窗口
	//	GW_*=GetWindow
	::SetFocus(::GetNextWindow(hwnd,GW_HWNDNEXT));
}
CEdit文本框组件 有一个Muliline属性默认是 False,不能接收回车键
//测试时不要加断点,会失去焦点

HWND GetWindow(//比 GetNextWindow 功能要强
   UINT nCmd 
) 可有 GW_HWNDFIRST,GW_HWNDLAST,GW_HWDNEXT,GW_CHILD,GW_HWNDPREV 


HWND  ::GetNextDlgTabItem(HWND hDlg,
	HWND hCtl,//开始窗口
	BOOL bPrevious)//为TRUE前一个,FALSE下一个
第一个有有Tabstop属性为true的组件,文本框默认是true,标签是false
GetNextDlgTabItem(::GetParent(hwnd),hwnd,FALSE) 是按tab order顺序,循环

设置文框的multiline为false
OnOK函数中


CWnd   GetFocus()//返回当前得到焦点的窗口
GetFocus()->GetNextWindow()->SetFocus();//到最后一个是NULL,会报错
GetNextDlgTabItem(GetFocus())->SetFocus();
Dialog设计视图中Fortmat菜单->tab order 显示tab键的顺序,单击组件会改变序号

如果OK按钮设置DefaultButton为true,删除OK按钮,按回车时也会响应OnOK函数,自己再增加OK按钮时要ID必须为IDOK,只对相同ID的按钮

=================8  VS2010有新的 CMFCButton
新建一个 dialog baseed 的项目,没有Frame和Doc和Viwe类,只有AboutDlg,MyDlg,MyApp类

只可对对话框属性中有修改字体,对某个组件没有属性可以修改字体
class wizard...->Add Class...->Add MFC class新建一个类,基于CButton
VS2010 class wizard...->Class Name中选择 CXXDlg类->Member Variable标签中列出的已有按钮的ID,选择->Add Variable..->
		中只有CButton可以选择,没有自己建立的基于CButton的类????????,只能使用CButton建立后,修改.h文件CButton为自己的类就OK
在Class View视图中右击自己的类->add->add variabale->不复先control variabale,只能加基本类型的????

WM_MOUSEMOVE 消息
可在 OnInitDialog 方法中做初始化工具
按钮有Visable属性
ShowWindow(SW_HIDE);//隐藏窗口,SW_SHOW
 
新建resource->dialog->IDD_PROPPAGE_LARGE 属性页
	  属性页的默认属性Style是CHILD,System menu是false,border为Thin,  Disabled为True
	而对话框的默认属性Style是POPUP,System menu是true, border为Resize,Disabled为False

GroupBox 分组
CheckBox
ListBox
ComBox 下拦式,在Dialog Editor工具栏中默认显示的,上有一个test Dialog按钮可以测试,
	type属性:1.dropDown  可以输入
			2.sample  (要拖高一些)下方多了一列表,会列出所有值,选中自动输入	
			3.Drop List 不能输入,新建时要向下多拖点,也可建后点下拉箭头,放大

Classwizard向导,新建类,把属性页ID 与类关联，基类要选择 CPropertyPage  (标签页)
CPropertyPage 是继承自 CDialog

自己的类 继承 CPropertySheet( //CPropertySheet继承自CWnd
			  UINT nIDCaption,
			   CWnd* pParentWnd = NULL,//如为NULL父窗口就是应用程序主窗口
			   UINT iSelectPage = 0 //初如选择页
			)
	AddPage(CPropertyPage *)
	DoModal()//模态
	Create()//非模态
	
显示的是标签页(Tab)的形式,向导在调用DoModal()之前要调用 SetWizardMode(); 变成向导方式,有"上一步","下一步",但总是有效
CPropertySheet 类有一个 SetWizardButtons(PSWIZB_BACK |PSWIZB_NEXT | PSWIZB_FINISH | PSWIZB_DISABLEDFINISH )
						//PSWIZB_*=PropertySheet WizardButton
	覆盖  CPropertyPage 的virtual函数 OnSetActive 在基中调用SetWizardButtons方法
	
RadioButton的Goup值默认为false,classwizard中不可见,设置为true后classWizard变量中才可见
后面的单选按钮都和是这个一组,直到下一个(Tab Order的值)单选按钮的Goup为true则是另一组
	 构造函数初始为0,表示第一个被选中,应修改为-1,
直到下一个RadioButton为true就是另一组了

覆盖 CPropertyPage 的virtual函数  OnWizardNext(),OnWizardFinish(),OnWizardBack()
	返回0表示成功,可向下走. -1 表示失败,不可切换页
注意要调用 UpdateData(TRUE) //模态对话框调用时设置为FALSE正在初始化(放值),TRUE 正在重新得到数据

PropertyPage 重写OnInitDialog做初始化工作,
可以用GetDlgItem(IDC_LIST1)->AddString("北京");//CListBox 的 AddString()
CCombox 取消sort功能

CCombox的SetCurSel()设置当前选择的 0开始,对应的有 GetCurSel,
CCombox的GetLBText(int nIndex,CString& rString )//LB=ListBox 得到列表中的文本
 
CPropertySheet 的DoModal()如果是向导则返回值是  ID_WIZFINISH or IDCANCEL,否则是 IDOK or IDCANCEL 

点完成窗口消失,但CPropertySheet对象还在
View中调用 Invalidate()//窗口重画,调用OnDraw()调用

================9 修改外观
窗口创建之前修改外观,CMainFrame的 PreCreateWindow(CREATESTRUCT cs)//表示创建之前,在这里修改
	cs.cy,cs.xy宽高,
	cs.lpszName=_T("标题名");//无效因单文档(Sigle Document Interface=SDI)默认是有FWS_ADDTOTITLE和WS_OVERLAPPEDWINDOW,
	FWS_ADDTOTITLE表示把文档的标题做为窗口的标题,要style&= ~FWS_ADDTOTITLE  Frame使用document的标题 //FWS_*=Frame Window Style

MSDN 查 PreCreateWindow ->Changing the Styles of a Window Created by MFC->SDI

SetWindowLong(//可窗口创建之后来改
	hWnd,GWL_STYLE //GWL_*=GetWindowLong改变窗口属性,返回先前的,也可用GetWindowLong
SetWindowLong(hWnd,GWL_STYLE,GetWindowLong(hWnd,GWL_STYLE)& ~WS_MAXIMIZEBOX );//去最大化按钮


WNDCLASS wndcls;
wndcls.cbClsExtra=0;
wndcls.cbWndExtra=0;
wndcls.hbrBackgrund=(HBRUSH)GetStockObject(BLACK_BRUSH); //GRAY_BRUSH,br=brush,应该在View类PreCreateWindow中有效,可以cs.lpszClass="xx"
wndcls.hCursor=LoadCursor(NULL,IDC_HELP);//应该在View类中修改
wndcls.hIcon=LoadIcon(NULL,IDI_ERROR);
wndcls.hInstance=AfxGetInstanceHandle();//得到当前应用程序的句柄
wndcls.lpfnWndProc=::DefWindowProc;//要使用全局的,使用默认处理 
wndcls.lpszClassName=_T("xx");
wndcls.lpszMenuName=NULL;//CXXApp类InitInstance()创建菜单 
wndcls.style=CS_HREDRAW |CS_VREDRAW

RegisterClass(&wndcls);
cs.lpszClass=_T("xx");
背景和光标受View的影响,只在View的PreCreateWindow中加cs.lpszClass=_T("xx");

如只修改图标,就不用上面的步聚了,只用
LPCTSTR AFXAPI AfxRegisterWndClass(
   UINT nClassStyle,
   HCURSOR hCursor = 0,
   HBRUSH hbrBackground = 0,//默认NULL画刷,是透明的
   HICON hIcon = 0 
); //返回一个注册的类名
cs.lpszClass=AfxRegisterWndClass(CS_HREDRAW |CS_VREDRAW,0,0,LoadIcon(NULL,IDI_WARNING))//在Frame中光标,背景没用设0
cs.lpszClass=AfxRegisterWndClass(CS_HREDRAW |CS_VREDRAW,LoadCursor(NULL,IDC_HELP),(HBRUSH)GetStockObject(GRAY_BRUSH),0)//在View中,图标没用设0

::DWORD SetClassLong(HWND hWnd,
    int nIndex,
    LONG dwNewLong
);//全局的,对创建框架之后修改图标....,对应的有GetClassLong函数, GCL_*==GetClassLong
::SetClassLong(hWnd,GCL_HICON,(LONG)LoadIcon(NULL,IDI_ERROR));//在Frame中
::SetClassLong(hWnd,GCL_HBRBACKGROUND,(LONG)GetStockObject(GRAY_BRUSH));//在View中
::SetClassLong(hWnd,GCL_HCURSOR,LoadIcon(NULL,IDI_HELP));//在View中

在Resource中可import Icon
HICON LoadIcon(          HINSTANCE hInstance,
    LPCTSTR lpIconName
);
LoadIcon(AfxGetInstanceHandle(),MAKEINTRESOURCE(IDI_ICON1));//MAKEINTRESOURCE 把资源int型的ID转换为字串

CWinApp 有一个 m_hInstance 属性表示当前实例,CXXXApp继承自CWinApp,已经定义了全局变量 theApp
extern CXXXApp theApp;//在类中声明使用一个外部全局变量 
也可用全局的函数::AfxGetApp(); 返回一个 CWinApp *  

----toolbar
Resource视图下有Toolbar,按钮最好要与菜单项有相同的ID (如果没有对ID做消息处理,就是不可用的)
按钮分隔符,按住按钮向右拖点一下,就会多一点距离,删按钮只要拖出工具栏
读VS2010 生成的代码使用 CMFCToolbar 做工具栏(是可以切换界面主题的,VS2008中是没有的),老的是使用 CToolBar
class CMFCToolBar : public CMFCBaseToolBar //VS2010 新的
//class CToolBar : public CControlBar //VS2008
CMFCToolBar::Create()或CreateEx,再调用LoadToolBar()或者LoadBitmap (要加多一步调用SetButtons() )

使用 CMFCToolBar::ReplaceButton，插入一个特定按钮

工具栏设计后以图片形式保存的,在res/Toolbar.bmp文件中

CMFCToolBar(CToolbar)的enableDocking(CBRS_ALIGN_TOP)可以让工具栏,可以停靠//CBRS_*=Control BaR Style
CFrameWnd的enableDocking(CBRS_ALIGN_ANY)//让框架支持停靠的位置
DockPane(&myMFCToolBar);//新的方式
//DockContrlBar(&myToolBar);//老的方式

工具栏的ID号和菜单的ID号相同
myToolBar.IsWindowVisible()  
myToolBar.ShowWindow(SW_SHOW)//方式一,SW_HIDE//老的CToolBar隐藏再显示要调用 RecalcLayout() ,re calc 

如把工具栏拖出,漂浮时,再隐藏时无效,要调用 DockPane(&myMFCToolBar);//老的调用DockControlBar(&myToolBar);
但再显示并不是在原先拖出的位置,工具栏的名字使用什么方法设置??

ShowPane(&myMFCToolBar,!myMFCToolBar.ISVisible(),FALSE,TRUE);//方式二
//老ShowControlBar(&myToolBar,TRUE)//显示或　隐藏

为选菜单项加复选功能,加 UPDATE_COMMAND_UI 消息处理, CCmdUI * ->SetCheck(myMFCToolBar.ISVisible());
----状态栏
看CMainFrame的源码 ,VS2010新的是 CMFCStatusBar //老的CStatusBar
Create(this)
SetIndicators()的数组参数
	ID_SEPARATOR, 		//左边的提示部分
	IDS_MY_TIMER,//可加自己的
	ID_INDICATOR_CAPS,	//字母cap键提示,ID是在资源视图String table文件中定义
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,

CTime t=CTime::GetCurrentTime(); //得到系统当前时间
CString str=t.Format("%H:%M:%S");

CMFCStatusBar 和 CStatusBar都是有一样的方法CommandToIndex,SetPaneText
int index=CommandToIndex(IDS_MY_TIMER);//状态栏Indicator的ID -> 索引号
SetPaneText(index,"newString",TRUE);//设置状态栏的文本

SetPaneInfo(index,IDS_MY_TIMER,SBPS_NORMAL,width)//改变状态栏的窗口大小,SBPS_*=Status Bar Pane Style
GetItemRect(index,&rec)//得到指定窗格的大小,不能在OnCreate中调用,要初始化完成才能得到,
----进度条
CProgressCtrl m_progress;
		m_progress.Create(WS_CHILD|WS_VISIBLE|PBS_SMOOTH, CRect(100,100,200,120), this,123)//PBS_VERTICAL,PBS_*=Progress Bar Style
		//要定义为类的属性也不显示????
		m_progress.SetPos(50)//50%设置进度条的进度值

要在OnCreate执行完成以后做一些处理,可以自定义消息
#define UM_MY_PROGRESS (WM_USER+1) //要求大于 WM_USER 的值
.h文件中加入 afx_msg LRESULT OnMyProgress(WPARAM wParam, LPARAM lParam) 的函数声明
.cpp文件中BEGINE_MESSAGE_MAP 区域中加入 ON_MESSAGE(UM_MY_PROGRESS,&CMainFrame::OnMyProgress) //如是命令ON_COMMAND
LRESULT CMainFrame::OnMyProgress(WPARAM wParam, LPARAM lParam)//实现
{
   UNREFERENCED_PARAMETER(wParam);
   UNREFERENCED_PARAMETER(lParam);
	//这里加自己的代码
	return 0;
}
 
SendMessage(UM_MY_PROGRESS);//发送消息,会先处理消息响应(相当于调用了另一函数),再继续,是同步的
PostMessage(UM_MY_PROGRESS);//放在消息队列中,立即返回,是异步的

当窗口大小变化时,进度条也要跟随变化,使用WM_PAINT消息,在处理函数中可以使用CPaintDC dc(this)
m_progress.MoveWindow(rect)//移动进度条(窗口),或者用 SetWindowPos

m_progress.SetStep(10)//每步走多少,PBS_SMOOTH的效果好
m_progress.SetRange(0,100);
m_progress.StepIt()//进度条前进

(CMainFrame*)AfxGetMainWnd();

//CMainFrame有一个m_wndStatusBar是protected,如要在View中引用,改为public后报错???

CFrameWnd 的 SetMessageText(str)//方式一,设置状态栏文本
((CMainFrame* )AfxGetMainWnd())->SetMessageText(str);//View中不能使用(CMainFrame*)GetParent()

CFrameWnd 的 GetMessageBar()->SetWindowText(L"");//方式二,L和_T都可以
((CMainFrame* )AfxGetMainWnd())-> GetMessageBar()->SetWindowText(str);//View中不能使用(CMainFrame*)GetParent()

CWnd GetDescendantWindow( AFX_IDW_STATUS_BAR ,isPermanent)//找指定ID的子窗口,CMFCCSatusBar的Create默认的ID
//descendant后代 ,isPermanent 持久窗口 还是 临时窗口
---启动画面
================10
dc.SetPixel(x,y,RGB(255,0,0));//画一个像素点
dc.Ellipse(point1,point2);//椭圆

颜色选择对话框
MFCColorDialog 有COLORREF color=GetColor(),setCurrentColor(RGB(255,0,0))
//老的CColorDialog 有m_cc,修改时要多加dlg.m_cc.Flags |= CC_RGBINIT;

字体选择对话框
CFontDialog 有公用成员 CHOOSEFONT m_cf, 是一个结构体有一个成员  LPLOGFONT lpLogFont;->再找 LOGFONT 结构体中又有TCHAR lfFaceName[]

在自己的类中得到View类,使用((CMainFrame*)AfxGetMainWnd())->GetRigthPane();//CListView会有GetRigthPane
AfxGetMainWnd()->GetActiveView()  

if(!m_font.m_hObject)//关联资源的名柄
{
	//m_font.DeleteObject();//释放GDI资源
	m_font.CreateFontIndirectW(dlg.m_cf.lpLogFont);
}
pDC->SelectObject(&m_font);
LOGFONT t_font;
m_font.GetLogFont(&t_font);
CString strFont=t_font.lfFaceName;//从CFont中得到字体名
//MSDN有 CFontDialog 示例代码

当文本框内容改变时,就会发出 EN_CHANGE 消息,单选按钮有 BN_CLICKED 消息

WM_CTLCOLOR 消息的响应函数是 CWnd的OnCtlColor,可手工加自己的处理函数,对子控件的颜色使用父控件的
afx_msg HBRUSH OnCtlColor( //这个消息处理函数,会被每个子控件调用一次,会多次,
   CDC* pDC,
   CWnd* pWnd,
   UINT nCtlColor  //CTLCOLOR_BTN,(Button)  CTLCOLOR_*
);
CBrush 有operator HBRUSH,类型转换
GetDlgCtrlId();//得到当前窗口的ID,对某个控件设置颜色

CDC的 SetBKMode(TRANSPARENT);//可用于静态文本,对静态文本中的文字设置透明
//老的对文本框输入框中背景色为白色,要调用SetBKColor

VS 2010 class wizard在关联按钮到类时,只可以是CBbutton ,不可以是CMFCButton,及其它的

OnCtlColor方法中不能修改CButton的颜色,要自己写类继承自CButton  重写DrawItem( LPDRAWITEMSTRUCT lpDrawItemStruct )方法,
使用lpDrawItemStruct->hDC修改自己的主题,在方法结束前要还原自己的设置,MSDN 有示例代码 ,按钮设置Owner Drawn属性为True,对应代码中BS_OWNERDRAW 
 
---显示位图
CBitMap map;
map.LoadBitMap(IDB_BITMAP1);//jpg格式会失败的

CDC dcCompatible;
dcCompatible.CreateCompatibleDC(pDC);
dcCompatible.SelectObject(&map);

CRect rect;
GetClienttRec(&rect);

pDC->BitBlt(0,0,rect.Width(),rect.Height(),&dcCompatible,0,0,SRCCOPY);//从dcCompatible复制到pDC,1:1的比例
BOOL BitBlt(
   int x,
   int y,
   int nWidth,
   int nHeight,
   CDC* pSrcDC,
   int xSrc,
   int ySrc,
   DWORD dwRop   //可是 SRCCOPY
);
 
WM_ERASEBKGND 消息 ,在背景擦除时发送

BITMAP map_data;
map.GetBitMap(&map_data);
pDC->StretchBlt(0,0,rect.Width(),rect.Height(),&dcCompatible,0,0,map_data.bmWidth,map_data.bmHeight,SRCCOPY);//把原图右以缩放到目的大小

CDialogBar 是CWnd的子类,不是 Dialog

=============11
集合类 CPtrArray, 中的函数类似于 CObArray
CPaintDC 在构造时,自动 BeginPaint,析构时自动 EndPaint,也就是只能在WM_PAINT消息响应中才可以用CPaintDC

MFC源码中viewcore.cpp中有CView::OnPaint的响应函数,中先调用了OnPrepareDC()虚函数,再调用OnDraw();

void SetScrollSizes( //应在窗口建立后调用,可以在OnInitialUpadte的虚函数中调用,会在OnDraw之前
   int nMapMode,  //MM_TEXT,1像素,向下是y轴的增加,MM_* ,可使用SetMapMode()来修改
   SIZE sizeTotal,
   const SIZE& sizePage = sizeDefault,
   const SIZE& sizeLine = sizeDefault 
);
把原来继承自CView的修改为CScrollView ,要调用SetScrollSizes()才行
SetScrollSizes(MM_TEXT,CSize(800,600)); 

MSDN中 Coordinate Space and Transformation Reference

SetWorldTransform //世界坐标系空间 转换为 页面坐标系空间
//世界坐标系空间 ->页面坐标系空间->设备坐标系空间->物理空间
//世界坐标系空间 ->页面坐标系空间,CAD对图形的 移动,缩放,旋转,斜切,MSDN 的Using Coordinate Spaces and Transformations中有示例
//页面坐标系空间->设备坐标系空间(主要考虑的) ,页面空间中的矩形称窗口(像素,毫米单位),设备空间中的矩形称视口(设备单位)


当在View中画线,重画后线的位置发生变华,CScroll重写的OnPrepareDC()  ,源文件viewscrl.cpp,default 是MM_TEXT
中调用SetViewportOrg,修改了视口原点,  (SetWindowOrg 改变窗口原点)

在保存图形之前,调用OnPrepareDC(&dc),再调用 dc.DPtoLP(&point) //Device Point to Logic Point,对应的有LPtoDP();

方式二,使用CMetaFileDC,初始化调用Create();//如参数不是NULL,是存在文件中,否则在内存中
CMetaFileDC m_MetaFileDc;
m_MetaFileDc.Create();//放构造方法中
m_MetaFileDc.LineTo();//保存的是命令,不是数据,在OnLButtonUp

//OnDraw中
HMETATFILE h_metaFile=m_MetaFileDc.Close();
pDC->PlayMetaFile(h_metaFile);//使用pDC,可多次调用
m_MetaFileDc.Create();
m_MetaFileDc.PlayMetaFile(h_metaFile);//保存上一次的
::DeleteMetaFile(h_metaFile);

//CopyMetaFile 可以用作保存
HMETATFILE h_metaFile=m_MetaFileDc.Close();
CopyMetaFile(h_metaFile,L"myMetaFile.wmf");
m_MetaFileDc.Create();
::DeleteMetaFile(h_metaFile);
//GetMetaFile 可以用作打开
HMETATFILE h_metaFile=GetMetaFile(L"myMetaFile.wmf");//Enh
m_MetaFileDc.PlayMetaFile(h_metaFile);
::DeleteMetaFile(h_metaFile);//使用Enh
Invalidate();

//----有新的HENHMETATFILE, CreateEnhanced,GetEnhMetaFile,DeleteEnhMetaFile,CopyEnhMetaFile,PlayEnhMetaFile,DeleteEnhMetaFile
//文件名应使用.EMF扩展。
方式三,使用 兼容DC
m_compatibleDC.CreateCompatibleDC(&dc);

CRect rect;
GetClientRect(&rect);
CBitmap map;
map.CreateCompatibleBitmap(&dc,rect.Width(),rect.Height());
m_compatibleDC.SelectObject(&map);
m_compatibleDC.BitBlt(0,0,rect.Width(),rect.Height(),&dc,0,0,SRCCOPY);//对CreateCompatibleBitmap的位图要在SelectObject以后多加BitBlt()

=============12 文件
---win32 API

CreateFile();//可以打开设备文件,管道文件
HANDLE WINAPI CreateFile(
  __in          LPCTSTR lpFileName,
  __in          DWORD dwDesiredAccess, //GENERIC_EXECUTE  ,GENERIC_READ  ,GENERIC_WRITE 
  __in          DWORD dwShareMode, //FILE_SHARE_READ,FILE_SHARE_WRITE,FILE_SHARE_DELETE,如果是0不能共享
  __in          LPSECURITY_ATTRIBUTES lpSecurityAttributes,//如是NULL,表使用默认,
  __in          DWORD dwCreationDisposition,//CREATE_NEW,CREATE_ALWAYS,OPEN_EXISTING
  __in          DWORD dwFlagsAndAttributes,//FILE_ATTRIBUTE_NORMAL,FILE_ATTRIBUTE_HIDDEN,FILE_ATTRIBUTE_READONLY
  __in          HANDLE hTemplateFile //只对GENERIC_READ,和建立新的文件,这个参数才有效
);

HANDLE  handle=CreateFile(L"file.bin",GENERIC_WRITE,0,NULL,CREATE_NEW,FILE_ATTRIBUTE_NORMAL,NULL);

BOOL WINAPI WriteFile(  //有WriteFileEx
  __in          HANDLE hFile,
  __in          LPCVOID lpBuffer,
  __in          DWORD nNumberOfBytesToWrite,
  __out         LPDWORD lpNumberOfBytesWritten,
  __in          LPOVERLAPPED lpOverlapped //只对CreateFile时Attribute指定 FILE_FLAG_OVERLAPPED,异步写,立即返回
);

CloseHandle(handle);

BOOL WINAPI ReadFile(//ReadFileEx
  __in          HANDLE hFile,
  __out         LPVOID lpBuffer,
  __in          DWORD nNumberOfBytesToRead,
  __out         LPDWORD lpNumberOfBytesRead,
  __in          LPOVERLAPPED lpOverlapped
);
//----------------写
HANDLE handle=CreateFile(L"中文名.txt",GENERIC_WRITE,0,NULL,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,NULL);

//--char 英文  OK
char* data ="english"; 
int charLen=strlen(data) +1 ;

//--char* 中文 ,写OK
char* data ="english中文"; 
int charLen=strlen(data) +1 ;

//----wchar_t *中文 ,写当二进制文件做法,读是OK的,
//用notepad++ 打开显示OK,但用"记事本"打开显示不对???
//原因是notepad是以GBK编码打开的,但实际上是UTF8编码(wchar_t),不知道文件头中应该如何写编码让"记事本"可以认它?????

wchar_t* data =L"english中文";//共9个
int charLen=wcslen(data)*2;//是包括结尾符的
	
DWORD len;
WriteFile(handle,data,strlen(data),&len,NULL);

//----------------读
HANDLE handle=CreateFile(L"中文名.txt",GENERIC_READ,0,NULL,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,NULL);
DWORD actualLen;

//--英文 OK
char data[100];
ReadFile(handle,&data,100,&actualLen,NULL);
data[actualLen]='\0'; 
MessageBox(CString(data));//只有英文可以 把char*到CString

//--char *中文
char data[100];
ReadFile(handle,&data,100,&actualLen,NULL);//Debug时是得到的是正常中文
data[actualLen]='\0';
//MessageBox(CString(data)); //有中文是不行的,不知道如何char*中文->wchar_t* 中文 转换?????


wchar_t wData[50];
wmemset(wData,0,50);

mbstate_t state;
memset(&state,0,sizeof(state));

//const char* con_char=data;
const char* con_char="char*中文";

//mbsrtowcs(wData,&con_char,50,&state);//char*是正常中文 ->转换到wchar_t* ,失败???
//mbstowcs(wData,con_char,50);//,失败???

wData[actualLen/2]='\0';
MessageBox(CString(wData));

//----wchar_t *中文 ,写当二进制文件做法,读是OK的,
//用notepad++ 打开显示OK,但用"记事本"打开显示不对???
//原因是notepad是以GBK编码打开的,但实际上是UTF8编码(wchar_t),不知道文件头中应该如何写编码让"记事本"可以认它?????

wchar_t data[100];
ReadFile(handle,&data,100,&actualLen,NULL);
data[actualLen/2]='\0';
MessageBox(CString(data)); 

CloseHandle(handle);


---MFC
CFile(
   LPCTSTR lpszFileName,
   UINT nOpenFlags //CFile::modeCreate  ,CFile::modeNoTruncate,CFile::typeBinary ,CFile::modeWrite,CFile::modeRead 
);
.Read
.Write
.GetLenth()


//--写
CFile file(L"中文名.txt",CFile::modeCreate|CFile::modeWrite);//|CFile::modeNoTruncate|CFile::typeBinary 
char * data="中华人民共和国";
file.Write(data,strlen(data));
file.Close();

//--读
CFile file(L"中文名.txt",CFile::modeNoTruncate|CFile::modeRead);//|CFile::typeBinary 
int len=file.GetLength();//得到长度
char *data=new char[len+1];
int readed=file.Read(data,len);
data[readed+1]='\0';//可以有中文
file.Close();
MessageBox(CString(data));//不能有中文

//打开/保存 对话框
explicit CFileDialog(
   BOOL bOpenFileDialog, //TRUE是打开,FALSE是保存
   LPCTSTR lpszDefExt = NULL,//默认扩展名
   LPCTSTR lpszFileName = NULL,//初始文件名
   DWORD dwFlags = OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, //OPENFILENAME 结构体的Flags成员,OFN_*=OpenFileNames
   LPCTSTR lpszFilter = NULL,//过虑文件,文件类型的选择
   CWnd* pParentWnd = NULL,
   DWORD dwSize = 0,
   BOOL bVistaStyle = TRUE
);


CFileDialog fileDlg(TRUE);
fileDlg.m_ofn.lpstrTitle=L"我的打开对话框"; //m_ofn成员,是OPENFILENAME类型的,成员lpstrTitle修改标题栏文本
fileDlg.m_ofn.lpstrFilter=L"Text File(*.txt)\0All Files(*.*)\0*.*\0\0"; //\0是分隔显示和过滤功能,可多个,最后以\0\0结尾
fileDlg.DoModal();

fileDlg.m_ofn.lpstrDefExt=L"txt";//保存时的默认扩展名
fileDlg.GetFileName();
fileDlg.GetPathName();


BOOL WINAPI WriteProfileString(//Win32 API,读写Windows目录下的Win.ini文件, ;表示注释
  __in          LPCTSTR lpAppName,//表示头[my]
  __in          LPCTSTR lpKeyName,
  __in          LPCTSTR lpString
);

可以把代码放在 App类的InitInstance()方法中,可以和已有SetRegistryKey代码放在一起
DWORD WINAPI GetProfileString(//Win32 API
  __in          LPCTSTR lpAppName,
  __in          LPCTSTR lpKeyName,
  __in          LPCTSTR lpDefault,//不能为空,当key不存在时会把它复制到 lpReturnedString 中
  __out         LPTSTR lpReturnedString,//返回字串空间
  __in          DWORD nSize//返回空间大小
);

CString类的GetBuffer() 方法返回的字串,是可以修改的,但在调用CString的其它方法之前必须要调用 ReleaseBuffer()

CWinApp类的WriteProfileString,有W结尾方法,会把信息记录到注册表中
位置在 \HKEY_CURRENT_USER\Software\Local AppWizard-Generated Applications\<项目名>\<程序中appName名字>
因调用了 SetRegistryKey(_T("Local AppWizard-Generated Applications")); 可以手工修改它

CWinApp类的GetProfileString,有W结尾方法,格式为 CString str=this->GetProfileStringW(L"我的程序",L"李四",L"空");

LONG WINAPI RegCreateKey(//Win32 API ,如果注册表项已经存,打开它
  __in          HKEY hKey,//可以是 HKEY_LOCAL_MACHINE 
  __in          LPCTSTR lpSubKey,
  __out         PHKEY phkResult //返回句柄 
);
LONG WINAPI RegSetValue(
  __in          HKEY hKey,
  __in          LPCTSTR lpSubKey,//可以是NULL,即没有Key名,显示为第一项的(Default)或(默认)
  __in          DWORD dwType,//只可是REG_SZ ,如要其它类型使用 RegSetValueEx函数
  __in          LPCTSTR lpData,
  __in          DWORD cbData//空间大小
);

RegCloseKey( x );

LONG WINAPI RegQueryValue(
  __in          HKEY hKey,
  __in          LPCTSTR lpSubKey,
  __out         LPTSTR lpValue,//如果为NULL,lpcbValue返回数据的长度
  __in_out      PLONG lpcbValue
);
//---写注册表
HKEY key;
RegCreateKey(HKEY_LOCAL_MACHINE,L"SOFTWARE\\我的公司\\我的程序",&key);
CString val=_T("不常用的没名字的值");
RegSetValue(key,NULL,REG_SZ,val,val.GetLength());
RegCloseKey(key);

//---读注册表
HKEY key;
RegCreateKey(HKEY_LOCAL_MACHINE,L"SOFTWARE\\我的公司\\我的程序",&key);
long len;
RegQueryValue(key,NULL,NULL,&len);//为了得到长度
wchar_t *val=new wchar_t[len];
RegQueryValue(key,NULL,val,&len);//要传&len
MessageBox(val);
RegCloseKey(key);


LONG WINAPI RegSetValueEx(
  __in          HKEY hKey,
  __in          LPCTSTR lpValueName,
  DWORD Reserved,
  __in          DWORD dwType,//可以是REG_BINARY,REG_DWORD数值,等
  __in          const BYTE* lpData,
  __in          DWORD cbData
);

LONG WINAPI RegOpenKey(
  __in          HKEY hKey,
  __in          LPCTSTR lpSubKey,
  __out         PHKEY phkResult
);
LONG WINAPI RegQueryValueEx(
  __in          HKEY hKey,
  __in          LPCTSTR lpValueName,
  LPDWORD lpReserved,
  __out         LPDWORD lpType,
  __out         LPBYTE lpData,
  __in_out      LPDWORD lpcbData
);
DWORD age=30;
RegSetValueEx(key,L"年龄",NULL,REG_DWORD,(const BYTE*)&age,sizeof(DWORD));
//--
HKEY key;
RegOpenKey(HKEY_LOCAL_MACHINE,L"SOFTWARE\\我的公司\\我的程序",&key);//和RegCreateKey类似
DWORD len,type,val;
RegQueryValueEx(key,L"年龄",NULL,&type,(LPBYTE)&val,&len);//可以得到类型
CString str;
str.Format(L"age=%d,type=%d,len=%d",val,type,len);
MessageBox(str);
RegCloseKey(key);
//--
其它函数 RegDeleteKey
=============13
CArchive(//可把网络对象保存到文件中,serialization,必须使用CFile,一个文件只可有一个archive,重载了<<,>>
   CFile* pFile,
   UINT nMode,	//CArchive::load ,CArchive::store 要有与CFile的对应的读写权限
   int nBufSize = 4096,
   void* lpBuf = NULL //如不指定,系统自动生成,销毁
);
//--写
CFile file(L"serialize.bin",CFile::modeWrite|CFile::modeCreate);
CArchive archive(&file,CArchive::store);
int id=1001;
CString name=L"李四";
archive<<id<<name;//要用变量才知道是什么类型
//--读
CFile file(L"serialize.bin",CFile::modeRead);
CArchive archive(&file,CArchive::load);
int id;
CString name;
archive>>id>>name;
CString res;
res.Format(L"id=%d,name=%s",id,name);
MessageBox(res);


----
Doc中的 OnNewDocument 函数中,可以修改文档的标题 SetTitle(L""),
也可在Resource View->String Table->有一个IDR_MAINFRAME是以\n分隔的,第二个是空,可以加自己的文档标题,优先级低,
是被App类的InitInstance方法中使用一个 CSingleDocTemplate 读取IDR_MAINFRAME,即把Doc,Frame,View关联在一起的地方

IDR_MAINFRAME 中的串可以使用 GetDockString来得到
virtual BOOL GetDocString(
   CString& rString,//保存输出
   enum DocStringIndex index //可以是CDocTemplate::windowTitle,CDocTemplate::docName ...顺序与IDR_MAINFRAME的顺序同
) const;
CDocTemplate::filterName 和 CDocTemplate::filterExt 成对使用默认都是空 ,如File Text(*.txt) ,.txt 
CDocTemplate::regFileTypeId   把文件的扩展名注册到注册表中/HKEY_CLASSES_ROOT

OnNewDocument在启动时调用,在点新建按钮时也会调用,源码跟踪,CWinApp是间接从CCmdTarget继承的,可以接受Command消息,即菜单项消息
	appdlg.cpp中有CWinApp::OnFileNew() 是afx_msg形式的消息响应函数 -> CDocManager有一个指针链表(App类的InitInstance中调用AddDocTemplate就是到这个链表) 调用了OnFileNew(),
	在docmgr.cpp中 CDocManager::OnFileNew中有m_templateList是CPtrList类型,指针链表,中的m_templateList.GetCount>1 的判断中,因是单文档AddDocTemplate只调用一次,所以不成立.
	调用CDocTemplate的OpenDocumentFile(NULL)是纯虚函数,在子类中docsingl.cpp的CSingleDocTemplate,中有CreateNewDocuemtn,CreateNewFrame中也会建立View,这里三个对象的产生,
	后又调用了pDcouemnt->OnNewDocument是虚函数,即是自己Doc类的OnNewDocument,调用了父类OnNewDocument,又调用了DeleteContents()
	
Doc类有一个Serialize(CArchive &ar)方法,是在的打开和保存时调用,如果先保存,再打开同一文件时,打开就不会再调用Serialize,因已经在内存中了
	
OnFileOpen 的源码appdlg.cpp中CWinApp::OnFileOpen() ,在docmgr.cpp中有一行MatchDocType的调用如果已经打开,pOpenDocument就有值,后面就直接返回这个指针
	最后会调用自己Doc类的OnOpenDocument(如有),CDocument::OnOpenDocument有调用 DeleteContents()

对象的保存要
1. 类继承自CObject
2. 重写Serialize函数
3. 在类的定义.h文件中加入 DECLARE_SERIAL(MyShap) //在class MyShap{里面}
4. 要有不带参数的构造函数
5. 实现文件.cpp中 IMPLEMENT_SERIAL(MyShap,CObbject,1) //1 是版本号

Doc中得到View类
POSITION pos=GetFirstViewPosition();//后为GetNextView使用
MyView * view =(MyView*)GetNextView(pos);//为集合类使用,

调用Doc的Serialize方法,如有保存对象指针,ar>>对象指针,就会调用对象的Serialize
CObArray 支持  IMPLEMENT_SERIAL,会保存所有元素,直接调用 obArray.Serialize(ar);
CObArray源码是array_o.cpp

View类中有GetDocument,可以得到Doc

一个Doc可以和多个View关联,而一个View只能和一个Doc 相关联

在新建和打开时都调用了DeleteContents(),覆盖virtual函数,里面清自己的资源
CObArray的RemoveAt(i) //会改变集合的大小

=============14 Socket
windows只支持AF_INET
网络字节顺序高位先存格式,Intel CPU 使用低位先存

int WSAStartup(
  __in          WORD wVersionRequested,//版本,高字节是次版本号,低字节是主版本号,
  __out         LPWSADATA lpWSAData // WSADATA结构体的指针
);
WORD MAKEWORD(//生成版本号
    BYTE bLow,
    BYTE bHigh
);
typedef struct WSAData {  
WORD wVersion;//返回请求的版本
 WORD wHighVersion;//可支持的最高版本
 char szDescription[WSADESCRIPTION_LEN+1];//没用
 char szSystemStatus[WSASYS_STATUS_LEN+1];//没用
 unsigned short iMaxSockets; //不要使用它
 unsigned short iMaxUdpDg; //不要使用它
 char FAR* lpVendorInfo;//没用
} WSADATA,  *LPWSADATA;


//MSDN的初始化示例
WORD wVersionRequested;
WSADATA wsaData;
int err;
wVersionRequested = MAKEWORD( 1, 1 );//使用Socket 1.1版本
err = WSAStartup( wVersionRequested, &wsaData );
if ( err != 0 ) {
    return 1;
}
if ( LOBYTE( wsaData.wVersion ) != 1 || HIBYTE( wsaData.wVersion ) != 1 ) {
    WSACleanup( );
    return 1; 
}

//----Windows Socket 1.1 基于早期Berkeley 的Unix Socket,有可移植的API,UNIX在#include <sys/socket.h>
//MSDN 查socket Function
1. SOCKET WSAAPI socket(
  __in          int af, //可使用AF_INET,AF_INET6(对IPV6)
  __in          int type,//SOCK_STREAM 即TCP,SOCK_DGRAM 即UDP
  __in          int protocol//0表示自动选择协议
); //如错误返回INVALID_SOCKET,错误信息可调用WSAGetLastError

2. int bind(
  __in          SOCKET s,
  __in          const struct sockaddr* name,
  __in          int namelen//长度,因sockaddr的结构是不固定的
); //如错误返回 SOCKET_ERROR

struct sockaddr { //可以使用 sockaddr_in 来替换
        ushort  sa_family; //必须为 AF_INET
        char    sa_data[14];
};
struct sockaddr_in {
        short   sin_family;//必须为 AF_INET
        u_short sin_port;//要使用1024以上的端口号,要使用网络字节,htons转换
        struct  in_addr sin_addr;
        char    sin_zero[8];
};
typedef struct in_addr {
  union {
    struct { UCHAR s_b1,s_b2,s_b3,s_b4; } S_un_b;
    struct { USHORT s_w1,s_w2; } S_un_w;
    ULONG S_addr;//如使用 INADDR_ANY 表示对一台机器的多个网卡都接收,如只要一个IP,使用inet_addr函数,可以使用htonl
  } S_un;
} IN_ADDR, *PIN_ADDR, FAR *LPIN_ADDR;

unsigned long inet_addr(//返回值可给 S_addr
  __in          const char* cp //形式为 "192.168.0.1"
);
char* FAR inet_ntoa(//返回形式为 "192.168.0.1"  net to ascii
  __in          struct   in_addr in
);

u_long htonl( //host to net long ,转换为网络使用的
  __in          u_long hostlong
);
u_short htons(//是对short
  __in          u_short hostshort
);
 
3. 
int listen(
  __in          SOCKET s,
  __in          int backlog //等待连接队列的最大数,SOMAXCONN
);

4.
while(1)
{
	accept();
}
SOCKET accept(
  __in          SOCKET s,
  __out         struct sockaddr* addr,//得到客户端信息
  __in_out      int* addrlen //长度
);

5.
int send(
  __in          SOCKET s,
  __in          const char* buf, //二进制数据以 char*表示
  __in          int len,
  __in          int flags
);
int recv(
  __in          SOCKET s,
  __out         char* buf,
  __in          int len,
  __in          int flags
);
MSDN说明 要加 #include <Winsock2.h> link时加 Ws2_32.lib,或者Ws2_32.dll
//示例
#include <WinSock2.h> //在links设置中加Ws2_32.lib
#include <stdio.h>
WORD wVersionRequested;
WSADATA wsaData;
int err;
wVersionRequested = MAKEWORD( 1, 1 );//使用1.1版本
err = WSAStartup( wVersionRequested, &wsaData );
if ( err != 0 ) {
	return 1;
}
if ( LOBYTE( wsaData.wVersion ) != 1 ||
		HIBYTE( wsaData.wVersion ) != 1 ) {
	WSACleanup( );
	return 1; 
}
//---tcp server
SOCKET socketSer=socket(AF_INET,SOCK_STREAM,0);//windows的SOCKET,linux用int
sockaddr_in addr;//windows也可以使用大写SOCKADDR_IN
addr.sin_port=htons(8000);
addr.sin_family=AF_INET;
addr.sin_addr.S_un.S_addr=htonl(INADDR_ANY);//本机的所有网卡的IP都可以收到

bind(socketSer,(sockaddr*)&addr,sizeof(addr));
listen(socketSer,5);//队列中最多5个

sockaddr_in clientAddr;
int len=sizeof(clientAddr);
printf("waiting ...\n");
while(1)
{
	SOCKET socketClient;
	socketClient=accept(socketSer,(sockaddr*)&clientAddr,&len);
	char resp[50];
	sprintf(resp,"welcome %s !",inet_ntoa(clientAddr.sin_addr));
	send(socketClient,resp,strlen(resp)+1,0);//+1 为\0 ,MSG_OOB

	recv(socketClient,resp,50,0);
	printf("tcp server receive from client :%s\n",resp);
	closesocket(socketClient);
}
//---tcp client
SOCKET socketSer=socket(AF_INET,SOCK_STREAM,0);//windows的SOCKET,linux用int
sockaddr_in addr;//windows也可以使用大写SOCKADDR_IN
addr.sin_port=htons(8000);
addr.sin_family=AF_INET;
addr.sin_addr.S_un.S_addr=inet_addr("127.0.0.1");//服务的IP

int len =sizeof(addr);
connect(socketSer,(sockaddr*)&addr,len);

char data[50];
recv(socketSer,data,50,0);
printf("tcp client receive from server :%s \n",data);

sprintf(data,"this is client.");
send(socketSer,data,strlen(data)+1,0);

closesocket(socketSer);
WSACleanup( ); //
//---
UDP的服务端只bind就可以,不用listen,accept
int recvfrom(//UDP使用recvfrom,TCP使用recv, 会阻塞
  __in          SOCKET s,
  __out         char* buf,
  __in          int len,
  __in          int flags,
  __out         struct sockaddr* from,
  __in_out      int* fromlen
);//返回收到的字节数,错误返回SOCKET_ERROR 

int sendto(//UDP使用sendto,TCP使用send
  __in          SOCKET s,
  __in          const char* buf,
  __in          int len,
  __in          int flags,
  __in          const struct sockaddr* to,
  __in          int tolen
);
=============15  线程 Mutex
进程不执行任何东西,只是线程的容器,建立进程时,操作系统会自动为进程建立第一个线程,即主线程
每个进程有自己的独立的空间，一个进程的多个线程共享空间
设置，显示保护的系统文件，在C:盘下有一个pagefile.sys 大小为页面空间大小,比实际内存要大(即虚拟内存)

HANDLE WINAPI CreateThread(
  __in          LPSECURITY_ATTRIBUTES lpThreadAttributes,
  __in          SIZE_T dwStackSize,//会把值转换为page的最近的倍数,如是0使用默认
  __in          LPTHREAD_START_ROUTINE lpStartAddress,//函数入口地址 ,函数以下面的ThreadProc形式
  __in          LPVOID lpParameter,//传递给线程函数的值
  __in          DWORD dwCreationFlags,//如果为 CREATE_SUSPENDED ,线程建立后是暂停状态,调用ResumeThread,如是0创建后立即运行
  __out         LPDWORD lpThreadId//返回线程ID,NULL表示不要
);
page页面大小,32位CPU 使用的页面大小是4k

DWORD WINAPI ThreadProc(
  [in]                 LPVOID lpParameter
);

CloseHandle(xx);//线程是运行的,内核对象引用计数减1,当线程执行完后,再减1,就为0,会清资源,否则不会清资源,建立时先返回变1,再创建变2
Sleep(  __in  DWORD dwMilliseconds);//当前线程暂停多长时间,放弃占用的时间片
relinquish //放弃

HANDLE WINAPI CreateMutex( //mutex互斥,会记录拥有者的线程ID,和拥有次数,即调用了WaitForSingleObject几次
  __in          LPSECURITY_ATTRIBUTES lpMutexAttributes,
  __in          BOOL bInitialOwner,//TRUE 表示当前调用的线程是佣有者,其它线程不可用(计数器加1),FALSE则可用
  __in          LPCTSTR lpName//起的名字,NULL为匿名
);//如名字已经存在(不为匿名的),返回已有的,GetLastError 返回 ERROR_ALREADY_EXISTS,可处理一个软件只运行一个进程

//如没有ReleaseMutex,但线程退出,操作系统会自动为Mutex的计数器清0,拥有者ID清0
DWORD WINAPI WaitForSingleObject(//对Mutex,已经是拥有者可以多次调用,计数器加1
  __in          HANDLE hHandle,
  __in          DWORD dwMilliseconds//最长等待时间,0立即返回,INFINITE一直等
);//超时返回WAIT_TIMEOUT,失败返回NULL
//如果是 正常得到(signaled),即是手工清计数器到0,返回WAIT_OBJECT_0 ,Mutex的计数器>0,线程终止时,系统清的Mutex为0,计数器清0,返回WAIT_ABANDONED,可能是异常终止


BOOL WINAPI ReleaseMutex(//只能在当前线程中调用了WaitForSingleObject,才有效如其它线程调用了ReleaseMutex没用的,计数器减1,直到为0时其它线程可用
  __in          HANDLE hMutex
);


有IP地址控件,对应的类 CIPAddressCtrl  ,有GetAddress方法

BOOL AfxSocketInit( //MFC的,Socket版本是1.1,自动调用WSAStartup,WSACleanup ,可以在App的InitInstance中调用
   WSADATA* lpwsaData = NULL 
);//在afxsock.h中,可以放在stdafx.h中,不用链接库
 
=============16  线程  Event
nonsignaled  表示其它线程要等 
signaled 	其它线程不用等

HANDLE WINAPI CreateEvent(
  __in          LPSECURITY_ATTRIBUTES lpEventAttributes,
  __in          BOOL bManualReset,//应该设置为FASLE,只有一个线程可以得到event
  __in          BOOL bInitialState,//TRUE表示为 signaled,其它线程不用等
  __in          LPCTSTR lpName
);//失败返回NULL,如果名字已经存,GetLastError 返回 ERROR_ALREADY_EXISTS
WaitForSingleObject(_event,INFINITE)//对于事件,已经拥有,下一次不能再拥有,要等
//bManualReset如是TRUE,所有其它线程的 WaitForSingleObject(_event,INFINITE)都可以执行,如在WaitForSingleObject后再调用 ResetEvent其实并没有同步,没用

BOOL WINAPI ResetEvent(//设置为 nonsignaled ,其它线程要等
  __in          HANDLE hEvent
);
BOOL WINAPI SetEvent( //设置为 signaled ,其它线程不用等
  __in          HANDLE hEvent
);
CloseHandle(_event);//可以关闭

------CriticalSection
CRITICAL_SECTION 结构体

1. void WINAPI InitializeCriticalSection(//建立
  __out         LPCRITICAL_SECTION lpCriticalSection
);

2. void WINAPI EnterCriticalSection(//只可一个进入,如不能进入,会一直等到可以进入
  __in_out      LPCRITICAL_SECTION lpCriticalSection
);
3. void WINAPI LeaveCriticalSection(//
  __in_out      LPCRITICAL_SECTION lpCriticalSection
);
4. void WINAPI DeleteCriticalSection(
  __in_out      LPCRITICAL_SECTION lpCriticalSection
);
------
Mutex 和 Event 是内核对象,同步速度慢,可以在多个进程中的线程做同步
CriticalSection 是工作用户下,同步速度快,容易死锁

------异步Socket
应该要使用2.2   版本

int WSAAsyncSelect(
  __in          SOCKET s,
  __in          HWND hWnd,//接收消息的窗口名柄
  __in          unsigned int wMsg,//收到的消息,是自定义消息
  __in          long lEvent//只接收只定的消息,FD_READ
);

int WSAEnumProtocols(//得到系统已经安装的网络协议
  __in          LPINT lpiProtocols,//NULL 返回所有的协议
  __out         LPWSAPROTOCOL_INFO lpProtocolBuffer,//接收的协议
  __in_out      LPDWORD lpdwBufferLength//长度
);

SOCKET WSASocket( //windows的,不是通用的
  __in          int af,
  __in          int type,
  __in          int protocol,
  __in          LPWSAPROTOCOL_INFO lpProtocolInfo,//使用WSAEnumProtocols得到
  __in          GROUP g,//新的API是使用的
  __in          DWORD dwFlags//WSA_FLAG_OVERLAPPED,表示异步的,立即返回(类似于文件)
);

消息处理函数中
wParam 表示发生的socket ,低位 lParam表示 lEvent 的值如FD_READ,高位 lParam 有错误代码

int WSARecvFrom( //UDP
  __in          SOCKET s,
  __in_out      LPWSABUF lpBuffers,//要初始空间,并传len
  __in          DWORD dwBufferCount,//WSABUF数组长度
  __out         LPDWORD lpNumberOfBytesRecvd,//接收到的长度
  __in_out      LPDWORD lpFlags,
  __out         struct sockaddr* lpFrom,//可以是 SOCKADDR_IN
  __in_out      LPINT lpFromlen,
  __in          LPWSAOVERLAPPED lpOverlapped,
  __in          LPWSAOVERLAPPED_COMPLETION_ROUTINE lpCompletionRoutine //回调函数
);
void CALLBACK CompletionROUTINE(//回调函数形式
  IN DWORD dwError, 
  IN DWORD cbTransferred, 
  IN LPWSAOVERLAPPED lpOverlapped, 
  IN DWORD dwFlags
);

int WSASendTo(//UDP
  __in          SOCKET s,
  __in          LPWSABUF lpBuffers,
  __in          DWORD dwBufferCount,
  __out         LPDWORD lpNumberOfBytesSent,
  __in          DWORD dwFlags,
  __in          const struct sockaddr* lpTo,
  __in          int iToLen,
  __in          LPWSAOVERLAPPED lpOverlapped,
  __in          LPWSAOVERLAPPED_COMPLETION_ROUTINE lpCompletionRoutine
);


struct hostent* FAR gethostbyname(
  __in          const char* name
);
typedef struct hostent {  
char FAR* h_name; //主机名
char FAR  FAR** h_aliases;
short h_addrtype;  
short h_length;
char FAR  FAR** h_addr_list;//已经是网络字节顺序,可用 h_addr代表h_addr_list[0]
} HOSTENT

toAddr.sin_addr.S_un.S_addr=*((DWORD*)h_addr_list[0]);//DWORD是unsinged long ,S_addr是unsinged long

struct hostent* FAR gethostbyaddr(
  __in          const char* addr,//要是网络字顺序
  __in          int len, //对AF_INET是4个字节的IPv4地址
  __in          int type //AF_INET
);

gethostbyaddr((const char *)&fromAddr.sin_addr.S_un.S_add,4,AF_INET);
------示例
WORD wVersionRequested;
WSADATA wsaData;
int err;

wVersionRequested = MAKEWORD( 2, 2 );//使用2.2版本

err = WSAStartup( wVersionRequested, &wsaData );
if ( err != 0 ) {
	return ;
}
if ( LOBYTE( wsaData.wVersion ) != 2 ||
		HIBYTE( wsaData.wVersion ) != 2) {
	WSACleanup( );
	return ; 
}
SOCKET m_socket=0;
---

#define UM_SOCK_READ WM_USER+1 //用户自定义消息
ON_MESSAGE(UM_SOCK_READ,&CMFCAsyncSocketChatDialogDlg::OnMyRecvSock)

afx_msg LRESULT OnMyRecvSock(WPARAM wParam,LPARAM lParam);//自定义消息

LRESULT CMFCAsyncSocketChatDialogDlg::OnMyRecvSock(WPARAM wParam,LPARAM lParam)
{	
	//wParam表示哪个SOCKET,lParam高位是错误代码
	switch(LOWORD(lParam))//iEvent,如 FD_READ
	{
	case FD_READ:
		WSABUF buf;
		buf.len=200;
		buf.buf=new char[200];
		SOCKADDR_IN from;
		DWORD count=0;
		DWORD flag=0;
		int len=sizeof(SOCKADDR_IN);
		int ret=WSARecvFrom(m_socket,&buf,1,&count,&flag,(SOCKADDR*)&from,&len,NULL,NULL);
		if(SOCKET_ERROR==ret)
		{
			AfxMessageBox(L"WSARecvFrom 错误");
			delete [] buf.buf;//记得释放内存
			return false;
		}
		char * ip=inet_ntoa(from.sin_addr);
		wchar_t wIP[50];
		mbstowcs(wIP,ip,50);

		wchar_t wHost[50];
		hostent* ent=gethostbyaddr((char*)&from.sin_addr.S_un.S_addr,4,AF_INET);//AF_INET是IPv4,4个字节的IP地址
		mbstowcs(wHost,ent->h_name,50);;

		wchar_t *wContent = (wchar_t *)buf.buf;
		wchar_t wLineMsg[300];
		swprintf(wLineMsg,L"IP:%ls__host:%ls 说:%ls",wIP,wHost,wContent);//这里不能为%s,必须为%ls
		
		CString allMsg;
		GetDlgItemText(IDC_MY_RECV_TEXT,allMsg);
		allMsg +=wcscat(wLineMsg,L"\r\n");//Multiline设置为true
		SetDlgItemText(IDC_MY_RECV_TEXT,allMsg);
		delete [] buf.buf;//记得释放内存
		break;
	}
	return 1;
}
bool CMFCAsyncSocketChatDialogDlg::MyInitSocket(void)
{
	m_socket=WSASocket(AF_INET,SOCK_DGRAM,0,NULL,0,0);
	if(INVALID_SOCKET== m_socket )
	{
		AfxMessageBox(L"socket 错误");
		return false;
	}
	SOCKADDR_IN addr;
	addr.sin_family=AF_INET;
	addr.sin_port=htons(6000);
	addr.sin_addr.S_un.S_addr=ADDR_ANY;

	int bindVal=bind(m_socket,(SOCKADDR*)&addr,sizeof(SOCKADDR_IN));
	if(SOCKET_ERROR==bindVal)
	{
		AfxMessageBox(L"bind 错误");
		return false;
	}
	int selVal=WSAAsyncSelect(m_socket,m_hWnd,UM_SOCK_READ,FD_READ);
	if(SOCKET_ERROR==selVal)
	{
		AfxMessageBox(L"WSAAsyncSelect 错误");
		return false;
	}
	return true;
}
void CMFCAsyncSocketChatDialogDlg::OnBnClickedMySend()
{
	SOCKADDR_IN toAddr;
	toAddr.sin_family=AF_INET;
	toAddr.sin_port=htons(6000);
	
	CString hostname;
	GetDlgItemText(IDC_MY_HOST_TEXT,hostname);
	if(hostname=="")
	{
		DWORD ip;
		((CIPAddressCtrl*)GetDlgItem(IDC_IPADDRESS1))->GetAddress(ip);
		toAddr.sin_addr.S_un.S_addr=htonl(ip);
	}else
	{
		const wchar_t* wHost=(wchar_t*)hostname.GetBuffer();
		char host[50];
		wcstombs(host,wHost,50);//要转换
		HOSTENT* hostEnt =gethostbyname(host);
		DWORD dest;
		dest=*((DWORD*)hostEnt->h_addr_list[0]);//h_addr_list是网络字节
		toAddr.sin_addr.S_un.S_addr=dest;//S_addr是unsinged long ,DWORD是 unsinged long
	}
	
	wchar_t userData[200];
	GetDlgItemText(IDC_MY_SEND_TEXT,userData,200);
	
	char* sendData=(char*)userData;//对英文的数据,这里debug时,只显示一个母,要sendData[2]
	int len=wcslen(userData)*2+2;
	
	WSABUF buf;
	buf.buf=sendData;
	buf.len=len;

	DWORD sendCount;
	int rtn=WSASendTo(m_socket,&buf,1,&sendCount,0,(SOCKADDR*)&toAddr,sizeof(SOCKADDR_IN),NULL,NULL);
	if(SOCKET_ERROR == rtn)
	{
		AfxMessageBox(L"WSASendTo 错误");
		int err=WSAGetLastError();
		showError(err);
		return ;
	}
	SetDlgItemText(IDC_MY_SEND_TEXT,L"");
}
---
if(m_socket)
		closesocket(m_socket);
WSACleanup();
=============17 进程通信
---剪贴板
BOOL OpenClipboard( HWND hWndNewOwner );//如果已经打开,会失败
//hWndNewOwner不是剪贴板的拥有者,除非调用EmptyClipboard函数
BOOL CloseClipboard(VOID);
BOOL EmptyClipboard(VOID);//清空剪贴板数据,变为拥有者

HANDLE SetClipboardData(
//必须是拥有者,必须调用OpenClipboard,如果是在WM_RENDERFORMAT 和 WM_RENDERALLFORMATS 消息处理中不能调用OpenClipboard
	UINT uFormat,//CF_*ClipboardFormat ,CF_TEXT ASCII文本格式,CF_UNICODETEXT 
    HANDLE hMem//如为NULL,必须处理WM_RENDERFORMAT 和 WM_RENDERALLFORMATS 消息,当有取数据时,会发送(delay,大文件省内存)
	//hMem的值为 调用GlobalAlloc带 GMEM_MOVEABLE 参数
);

HGLOBAL WINAPI GlobalAlloc( //HGLOBAL就是HANDLE
  __in          UINT uFlags,//GMEM_MOVEABLE 不能和 GMEM_FIXED 一起使用,GMEM_FIXED表示返回的是指针,GMEM_MOVEABLE返回是句丙要使用GlobalLock返回指针
  __in          SIZE_T dwBytes
);
LPVOID WINAPI GlobalLock(//加锁,计数加1,并把句柄转换为指针,内存不能被移动
  __in          HGLOBAL hMem
);
BOOL WINAPI GlobalUnlock(//解锁,计数减1,
  __in          HGLOBAL hMem
);
HGLOBAL WINAPI GlobalReAlloc(
  __in          HGLOBAL hMem,
  __in          SIZE_T dwBytes,
  __in          UINT uFlags
);

BOOL IsClipboardFormatAvailable(UINT format);
HANDLE GetClipboardData(UINT uFormat);
---复制示例
if(OpenClipboard())//open, 如已经打开 再打就出错
{
	EmptyClipboard();//清空已有的数把
	HANDLE hClip=GlobalAlloc(GMEM_MOVEABLE,strLen*2 + 2);//返回HGLOBAL就是HANDLE

	wchar_t * clipData=(wchar_t*)GlobalLock(hClip);//lock
	wcscpy(clipData,userText.GetBuffer());//中文
	GlobalUnlock(hClip);//unlock

	SetClipboardData(CF_UNICODETEXT,hClip);// CF_TEXT只对ASCII
	CloseClipboard();//close
}
---粘贴示例
if(OpenClipboard())//open, 如已经打开 再打就出错
{
	if(IsClipboardFormatAvailable(CF_UNICODETEXT))
	{
		HANDLE hClip=GetClipboardData(CF_UNICODETEXT);// CF_TEXT只对ASCII
	
		wchar_t * clipData=(wchar_t*)GlobalLock(hClip);//lock
		SetDlgItemText(IDC_MY_PASTE_TEXT,CString(clipData));
		GlobalUnlock(hClip);//unlock
	}
	CloseClipboard();//close,正确的位置
}
---匿名管道
BOOL WINAPI CreatePipe(
  __out         PHANDLE hReadPipe,
  __out         PHANDLE hWritePipe,
  __in          LPSECURITY_ATTRIBUTES lpPipeAttributes,//如为NULL,子进程不能继承管道,因管道只能用于父子进程通信,不能为NULL
  __in          DWORD nSize//如为0使用默认大小
);
typedef struct _SECURITY_ATTRIBUTES {
	DWORD nLength;//指定为 sizeof(SECURITY_ATTRIBUTES)
	LPVOID lpSecurityDescriptor;//NULL表示使用默认的
	BOOL bInheritHandle;//TRUE可以继承句柄
} SECURITY_ATTRIBUTES, 

BOOL WINAPI CreateProcess(//建立进程,如没有加扩展名,会自动加.exe
  __in          LPCTSTR lpApplicationName,//可以是绝对路径,也可是当前目录(不会搜索),可以为NULL,值为lpCommandLine 空格分隔的第一个,如文件名中有空格要使用""
  __in_out      LPTSTR lpCommandLine,//如有程序名,会搜索System32目录(GetSystemDirectory),PATH,不能为const,可为NULL使用 lpApplicationName,
  __in          LPSECURITY_ATTRIBUTES lpProcessAttributes,
  __in          LPSECURITY_ATTRIBUTES lpThreadAttributes,
  __in          BOOL bInheritHandles,//TRUE,继承创建者(父进程)的句柄
  __in          DWORD dwCreationFlags,
  __in          LPVOID lpEnvironment,//NULL表示使用父进程,环境变量
  __in          LPCTSTR lpCurrentDirectory,//必须为绝对路径,如为NULL和父进程同
  __in          LPSTARTUPINFO lpStartupInfo,//窗口如何显示,STARTUPINFO结构体的dwFlags为STARTF_USESTDHANDLES,即只设hStdInput, hStdOutput, and hStdError(子进程的)
  __out         LPPROCESS_INFORMATION lpProcessInformation//是为 CloseHandle(),在父进程不使用时一定要CloseHandle,才会在子进程结束时清资源
);
typedef struct _PROCESS_INFORMATION {  
	HANDLE hProcess;
	HANDLE hThread;
	DWORD dwProcessId;
	DWORD dwThreadId;
} PROCESS_INFORMATION, 
//一般结构体都有cb成员,表示结构体的大小

ZeroMemomery类似于memset

HANDLE WINAPI GetStdHandle(
  __in          DWORD nStdHandle//可为 STD_ERROR_HANDLE
);
使用基于文件的ReadFile和WriteFile来读写
没有写,管理是空的时候,去读会阻塞

---pipe 示例
HANDLE m_hRead;
HANDLE m_hWrite;

void CMFCSinglePipeParentView::OnMyPipeCreate()
{
	SECURITY_ATTRIBUTES attr;
	attr.nLength=sizeof(SECURITY_ATTRIBUTES);
	attr.bInheritHandle=TRUE;
	attr.lpSecurityDescriptor=NULL;//使用默认的
	if(!CreatePipe(&m_hRead,&m_hWrite,&attr,0))//0使用默认大小
	{
		AfxMessageBox(L"建立Pipe失败");
		return ;
	}
	STARTUPINFO startInfo;
	ZeroMemory(&startInfo,sizeof(STARTUPINFO));//类似于memset
	startInfo.cb=sizeof(STARTUPINFO);//可以不调,一般是设的
	startInfo.dwFlags=STARTF_USESTDHANDLES;//如使用这个,只要设置hStdInput,hStdOutput,hStdError
	startInfo.hStdInput=m_hRead;//父进程的给子进程
	startInfo.hStdOutput=m_hWrite;
	startInfo.hStdError=GetStdHandle(STD_ERROR_HANDLE);

	PROCESS_INFORMATION processInfo;
	//当前目录是Solution的目录,即以 ./Debug/MFCSinglePipeParent 的方式来执行
	if(!CreateProcess(L"../Debug/MFCSinglePipeChild.exe",NULL,NULL,NULL,TRUE,0,NULL,NULL,&startInfo,&processInfo))
	{	
		long error=GetLastError();//要在CloseHandle之前调用
		AfxMessageBox(L"建立子进程失败");
		CloseHandle(m_hRead);
		CloseHandle(m_hWrite);
		m_hRead=NULL;//防止析构时,再次关闭
		m_hWrite=NULL;
		return ;
	} 
	CloseHandle(processInfo.hThread);//子进程的内核对象,计数减一
	CloseHandle(processInfo.hProcess);//进程中的主线程
	 
}
void CMFCSinglePipeParentView::OnMyPipeWirte()
{
	wchar_t *data =L"父进程测试数据"; 
	DWORD writed;
	if(! WriteFile(m_hWrite,data,wcslen(data)*2 + 2,&writed,NULL))
	{
		AfxMessageBox(L"写Pipe失败");
		return ;
	} 
}
void CMFCSinglePipeParentView::OnMyPipeRead()
{
	wchar_t buf[200];//OK
	DWORD readed;
	if(! ReadFile(m_hRead,buf,200*2,&readed,NULL))
	{
		AfxMessageBox(L"读Pipe失败");
		return ;
	} 
	AfxMessageBox(buf);
	 
}
//子进程
m_hRead=GetStdHandle(STD_INPUT_HANDLE);
m_hWrite=GetStdHandle(STD_OUTPUT_HANDLE);

---命名管道 
可以跨网络,一对一

HANDLE WINAPI CreateNamedPipe(
  __in          LPCTSTR lpName,//格式必须是\\.\pipe\<myname>  ,点表示本机,pipe是规定的名字
  __in          DWORD dwOpenMode,//PIPE_ACCESS_DUPLEX,是双向的,PIPE_ACCESS_INBOUND 只从客户端到服务器端
								//FILE_FLAG_OVERLAPPED,异步方式,会立即返回,ReadFileEx 和 WriteFileEx 只可在overlapped 方式下使用,
  __in          DWORD dwPipeMode,//PIPE_TYPE_BYTE ,是连续的,不能和 PIPE_READMODE_MESSAGE 一起使用,因没有消息定界符
								//PIPE_TYPE_MESSAGE,不是连续的,每个实例必须指定相同,有消息定界符
  __in          DWORD nMaxInstances,//每个实例必须指定相同,PIPE_UNLIMITED_INSTANCES,
  __in          DWORD nOutBufferSize,
  __in          DWORD nInBufferSize,
  __in          DWORD nDefaultTimeOut,//指定为NMPWAIT_USE_DEFAULT_WAIT用效,每个实例必须指定相同
  __in          LPSECURITY_ATTRIBUTES lpSecurityAttributes
);//失败返回 INVALID_HANDLE_VALUE 

BOOL WINAPI ConnectNamedPipe(//是服务端等待
  __in          HANDLE hNamedPipe,
  __in          LPOVERLAPPED lpOverlapped//如果是以FILE_FLAG_OVERLAPPED打开的,这个参数不能为NULL,并设置 hEvent,使用CreateEVent的手工方式,操作完成为有信号
  //如果不是FILE_FLAG_OVERLAPPED,会一直等待
);//如ERROR_IO_PENDING==GetLastError() ,并不是错误


BOOL WINAPI WaitNamedPipe(//等服务端有可用的连接
  __in          LPCTSTR lpNamedPipeName,//格式  \\<servername>\pipe\<pipename> ,可以用.表示本机
  __in          DWORD nTimeOut//如为NMPWAIT_USE_DEFAULT_WAIT表示使用CreateNamedPipe中指定的值, NMPWAIT_WAIT_FOREVER
  //NMP=NamedPipe
);
以后就可使用 CreateFile ,指定OPEN_EXISTING

--示例
服务端
void CMFCSinglePipeParentView::OnMyNamedPipeCreate()
{
	//pipe名字是必须的,点表示本机
	m_hNamedPipe = CreateNamedPipe(L"\\\\.\\pipe\\MyNamedPipe",
								PIPE_ACCESS_DUPLEX|FILE_FLAG_OVERLAPPED,
								PIPE_TYPE_BYTE|PIPE_READMODE_BYTE ,1,1024,1024,0,NULL);
	if(INVALID_HANDLE_VALUE == m_hNamedPipe)
	{
		AfxMessageBox(L"CreateNamedPipe 失败");
		m_hNamedPipe=NULL;
		return ;
	}
	HANDLE hEvent=CreateEvent(NULL,TRUE,FALSE,NULL);
	if(!hEvent)
	{
		AfxMessageBox(L"CreateNamedPipe 失败");
		CloseHandle(m_hNamedPipe);
		m_hNamedPipe=NULL;
		return;
	}
	OVERLAPPED lapped;
	ZeroMemory(&lapped,sizeof(OVERLAPPED));
	lapped.hEvent=hEvent;
	if(!ConnectNamedPipe(m_hNamedPipe,&lapped))//如果有FILE_FLAG_OVERLAPPED,必须设置OVERLAPPED的hEvent,操作完成有信号
	{
		if(ERROR_IO_PENDING != GetLastError())
		{
			AfxMessageBox(L"ConnectNamedPipe 失败");
			CloseHandle(m_hNamedPipe);
			m_hNamedPipe=NULL;
			CloseHandle(hEvent);
			return;
		}
	}
	if(WAIT_FAILED == WaitForSingleObject(hEvent,INFINITE))
	{
		AfxMessageBox(L"WaitForSingleObject 失败");
		CloseHandle(m_hNamedPipe);
		m_hNamedPipe=NULL;
		CloseHandle(hEvent);
		return;
	}
	CloseHandle(hEvent);//得到信息后就没用的
}

客户端使用
void CMFCSinglePipeChildView::OnMyNamedPipeConn()
{
	if(!WaitNamedPipe(L"\\\\127.0.0.1\\pipe\\MyNamedPipe",NMPWAIT_WAIT_FOREVER))
	{
		AfxMessageBox(L"WaitNamedPipe 失败");
		return ;
	}

	m_hNamedPipe=CreateFile(L"\\\\127.0.0.1\\pipe\\MyNamedPipe",GENERIC_READ|GENERIC_WRITE,0,NULL,
				OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,NULL);//使用OPEN_EXISTING
	if(INVALID_HANDLE_VALUE==m_hNamedPipe)
	{
		AfxMessageBox(L"CreateFile 失败");
		return ;
	}

}
----------邮槽
基于UDP,一对多,可跨网络,是单向的,数据不要太长,424字节以下
HANDLE WINAPI CreateMailslot(
  __in          LPCTSTR lpName,//\\.\mailslot\[path]name   ,mailslot是规定的名字
  __in          DWORD nMaxMessageSize,//0 表示任何大小
  __in          DWORD lReadTimeout,//MAILSLOT_WAIT_FOREVER
  __in_opt      LPSECURITY_ATTRIBUTES lpSecurityAttributes
);


void CMFCSinglePipeParentView::OnMyMailslotServer()
{
	 HANDLE hMailslot;
	 hMailslot=CreateMailslot(L"\\\\.\\mailslot\\MyMailSlot",0,MAILSLOT_WAIT_FOREVER,NULL);
	 if(INVALID_HANDLE_VALUE==hMailslot)
	 {
		AfxMessageBox(L"CreateMailslot 失败");
		return ;
	 }
	wchar_t buf[200];
	DWORD readed;
	if(! ReadFile(hMailslot,buf,200*2,&readed,NULL))//会阻塞
	{
		AfxMessageBox(L"读 Mailslot 失败");
		CloseHandle(hMailslot);
		return ;
	}
	AfxMessageBox(buf);
	CloseHandle(hMailslot);
}
void CMFCSinglePipeChildView::OnMyMailslotClient()
{
	 HANDLE hMailslot;
	 //不能使用127.0.0.1,基于UDP,广播方式,可以使用*,表示工作组或域中所有主机都会收到
	 hMailslot=CreateFile(L"\\\\*\\mailslot\\MyMailSlot",GENERIC_WRITE,FILE_SHARE_READ,NULL,
				OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,NULL);//OPEN_EXISTING,只可GENERIC_WRITE, FILE_SHARE_READ
	if(INVALID_HANDLE_VALUE==hMailslot)
	{
		AfxMessageBox(L"邮槽 CreateFile 失败");
		return ;
	}
	wchar_t *data =L"Mailslot Client 测试数据";
	DWORD writed;
	if(! WriteFile(hMailslot,data,wcslen(data)*2 + 2,&writed,NULL))
	{
		AfxMessageBox(L"写 Mailslot 失败");
		return ;
	} 
	CloseHandle(hMailslot);
}
=============18 ActiveX
ActiveX控件不能独运行,必须嵌入到其它应容器中使用,把常用的功能封装在一起,供其它人使用
有方法,属性,事件 三类  ,基于Com组件 , 使用ATL编写

建立ActiveX 类型的项目,生成一个XXXApp类 继承自 COleControlModule 类,继承自CWinApp类
XXXCtrl类继承自 COleControl 类,继承自 CWnd类 ,.h文件中有DECLARE_DISPATCH_MAP(),DECLARE_EVENT_MAP()
XXXPropPage类 类继承自 COlePropertyPage 继承自 CDialog ,有关联资源ID名

编译后在 Solution/Debug目录生成 XXX.ocx是最终文件,会自动注册.ocx文件到注册表

VS2010的 ActiveX Control Test Container 可以测试ActiveX的容器,位置在
C:\Program Files\Microsoft Visual Studio 10.0\Samples\1033VC2010Samples.zip 中的 C++\MFC\ole\TstCon 中的Solution打开编译运行

Edit->Insert New Control...->选择自己项目生成的(技巧:快速输入文件名),状态栏中可以看到,文件所在目录是自己Solution 的目录

regsvr32  XXX.ocx路径 		注册控件到注册表中,会调用 生成代码中有全局函数DllRegisterServer()
regsvr32 /u XXX.ocx路径		取消注册控件 ,会调用 生成代码中有全局函数DllUnregisterServer()
(技巧:可以把.ocx文件拖到 cmd窗口中,显示文件全路径)

XXXCtrl类的 OnDraw方法,可以加自己的代码
可以调用Invalidate()也可以调用 InvalidateControl()

VS2010中增加 Stock 属性,Class View视图->展开XXXLib->中的第一个"o-O"图标(表示是接口,不是以Events结尾的) 右击->add->add property
	Implementation type:选择为Stock,Property name: 下拉选择 BackColor或ForeColor
	
1. Stock 属性,有如字体,颜色
2. Ambient 属性不能被更改
3. Extended 一般有大小,屏幕位置
4. Cutstom 可开发自己新的属性

COleControl类的 OLE_COLOR GetBackColor( ); //可以得到设置的属性
COleControl类的  COLORREF TranslateColor(//把 OLE_COLOR 转换为 COLORREF
				   OLE_COLOR clrColor,
				   HPALETTE hpal = NULL 
				);
SetBKColor是设置文本的背景色

//OnDraw方法代码, 要在VB中测试,才可以看到属性并修改
CTime time=CTime::GetCurrentTime();//VS工具不提示有这个方法,但是有的
CString timeStr=time.Format("%H:%M:%S");
CBrush brush(TranslateColor(GetBackColor()));//GetBackColor得到用户设置的背景色属性,
pdc->FillRect(rcBounds, &brush);
pdc->SetTextColor(TranslateColor(GetForeColor()));
pdc->SetBkMode(TRANSPARENT);//文字背景透明,SetBkColor文字背景色,SetBackColor是属性
pdc->TextOutW(0,0,timeStr);
	
//要在ActiveX Control Test Container中测试,才生效
XXXCtrl类中有 BEGIN_PROPPAGEIDS( x,1) 的定义 ,是对右击控件->properties显示的对话框,以tab显示
	加入PROPPAGEID(CLSID_CColorPropPage) 颜色属性页的GUI,并修改 BEGIN_PROPPAGEIDS 的1为2,表示个数
 
//要在ActiveX Control Test Container中才可调用方法
用户自定义属性,Property type:选择SHORT,property name:输入控件中显示的属性名,如myinterval
如Implementation type:选择Member variable,会自动生成XXXCtrl类的成员,如m_myinterval,和Notification函数,如OnmyintervalChanged,当用户修改属性时调用的函数
如Implementation type:选择Get/Set methods,会自动生成XXXCtrl类的Get/Set函数

BEGIN_DISPATCH_MAP 中增加了 DISP_PROPERTY_NOTIFY_ID(... OnmyintervalChanged ...),而OnintervalChanged方法最后调用了SetMofiiedFlag();

KillTimer(1);//1是SetTimer中的标识
//VB中看不到myinterval属性???????????,VC中可以看到,但修改时报错??

属性页中的表单关联类的成员时,没有关联ActiveX控件的外部属性,放在DDX_之前,没有效果???????
XXXPropPage类的DoDataExchange方法中要手工加 DDP_Text(pDX,IDC_EDIT_INTERVAL,m_dlgInterval,_T("myinterval"));


Class View视图->展开XXXLib->中的第一个"o-O"图标(不是以Events结尾的) 右击->add->add function,只在ActiveX Control Test Container中测试才可看到方法

双击以Events结尾的"o-O" 图标,有source声明,会向容器发出事件,即控件调用容器方法
右击 XXXCtrl类 -> add->add event增加事件,有一些自带事件,如Click,也会在"o-O"图标->以Events结尾的类中生成方法声明//只在ActiveX Control Test Container中测试才行
自定义事件,在Event name:中输入事件名,自动填充同样的值到Internal name:

XXXCtrl类的DoPropExchange 函数中调用 ,PX_开头的函数,如PX_Short,让属性具有待久性,即可以保存属性值

void BoundPropertyChanged(//通知容器值更新
   DISPID dispid //值是"o-O" 图标->类->属性前的ID中的值
);

控件时运行,设计时 还是 运行时,使用 AmbientUserMode();//返回TRUE表示运行时,在ActiveX Control Test Container中 Option->Desgin Mode来切换

COleControl类的 以 Ambient开头的函数

建立VC的Dialog项目,做ActiveX客户端,Advance Feature中默认,有ActiveX选项,否则不能启动,右击设计界面对话框->Insert ActiveX control....->选择后就有 myinterval 属性了,但修改报错????????
右击ToolBox->选中ListView,就只显示图标了,右击ToolBox->Choose Items...->COM Components->选择后,组件就加到工具箱中了,
工具栏中有一个,用于测试的按钮,Properies面板上方有一铵钮,可以弹出对话框

组件就加到工具箱中了后(如像只有第一次行),右击 加入的ActiveX 组件->add variable ,会增加.h和.cpp文件,会在Dlg类的DoDataExchange方法中增加 DDX_Control(...);

=============19 库
静态库是.lib结尾
引入库是.lib结尾,被DLL导出的函数和变量的符号名,实际的在.dll中

Kernel32.dll
user32.dll
gdi32.dll

建立
C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\bin\dumpbin 看.dll文件导出了什么
C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\bin\vcvars32.bat 会设置PATH等,以便在其它地方使用dumpbin

mdbg (Managed debugger)调试命令,CLR调试
>r xxx.exe

dumpbin -exports abc.dll ,显示函数名字有多余符号,为重载
要在自己的方法前加 _declspec(dllexport) 的函数才会被导出,其它应用才可用,会生成.lib和.exp文件

dumpbin -imports abc.exe 或者.dll		用了哪些dll,依赖dll

使用函数前加 extern 声明,也可使用 _declspec(dllimport) ,速度快,知道是dll文件中的
links 加 XXX.lib文件 ,会生成.lib得到到使用项目的目录中,和.h在一起,因使用项目和DLL项目的生成文件都在Solution/Debug下,所以可以打到dll文件
.h文件中使用 _declspec(dllimport)只是为外部使用,可定义为XXX_API的宏,通用
#ifdef XXX_API
#else
	#define XXX_API  _declspec(dllimport) //为外部使用
#endif
XXX_API void testFun();
内部.c中
#define XXX_API  _declspec(dllexport)
#include "xxx.h"
//外部.c中就不用#define

导出一个类格式 class XXX_API My{};//priavte 依然是无法仿问的

在dll中得到调用者的窗口,使用 HWND  GetForegroundWindow() ,表示当前正在使用的窗口


导出一个类中的部分函数格式  
	class  My { 
	public:
		XXX_API void testFun(); //只导出 XXX_API的,虽然类名没有导出,但还是可以用类名定义
	};

如果不希望dll导出的函数名不发生变化(为重载),可在函数名前加 extern "C" ,C要大写,不同的编译器就可用,C语言程序就可以调用
但就不能导出类,如果定义和实现时在返回类型和函数名 中间加 _stdcall 声明,导出的函数名,还是有多余符号
.h文件中
#define XXX_API extern "C" _declspec(dllimport) 
内部.c中
#define XXX_API extern "C" _declspec(dllexport)


手写 模块定义文件 .def,导入到项目中
LIBRARY XXX   //对应于XXX.dll,可以查Doc的LIBRARY
EXPORTS   //导出的函数及符号名
testFunc  //也可用 <导出的外部函数名>=<内部函数名>,如使用_stdcall,也没有多余符号,就不用再加_declspec(xxx) 了
//也可以后面加 @2 表示自己指定编号2,如testFunc @2

HMODULE WINAPI LoadLibrary( //HMODULE 可以和 HINSTANCE 交换使用
  __in          LPCTSTR lpFileName //可以是.dll或是.exe
);//动态加载.dll,只要.dll就不用加.lib,也不用.h文件了,dumpbin -exports .exe 就看不到指定的.dll

FARPROC WINAPI GetProcAddress(// 得到dll中导出的函数的地址
  __in          HMODULE hModule,
  __in          LPCSTR lpProcName //函数名,要使用dumpbin -exports .dll ,注意名字改编,如使用ordinal表示的编码 MAKEINTERSOUCEA(3)
);//如果函数使用 _stdcall 返回的函数指针,typedef时也要加 _stdcall,即 typedef int(_stdcall *FuncMulti)(int a,int b)
 
 //2008Doc中搜DllMain Callback Function,测试加了这函数,调用者不能运行???????????
 BOOL WINAPI DllMain(//可选的
  _In_  HINSTANCE hinstDLL,//会传入句柄
  _In_  DWORD fdwReason,
  _In_  LPVOID lpvReserved
);//当dll被加载时被调用,不要实现太复杂

建立MFC DLL项目时,有一个MFC extension DLL的选项,表示MFC库也导出为dll

如果使用LoadLibrary后,以后就不用了可以使用 FreeLibrary,计数器减1,到0时释放空间

=============20 Hook
//放在Hook链的前面
HHOOK SetWindowsHookEx(
	int idHook,//WH_* ,WH_MOUSE,鼠标消息
    HOOKPROC lpfn,//如果dwThreadId为0或是其它进程的线程,这个过程(函数指针)必须是在.dll中
    HINSTANCE hMod,//dll的句柄,如时当前进程的线程,必须是NULL
    DWORD dwThreadId//相关的线程ID,如是0表示和当前桌面的所有线程都相关
);

DWORD WINAPI GetCurrentThreadId(void);//得到当前线程ID

//对WH_MOUSE的过程形式
LRESULT CALLBACK MouseProc(
	int nCode,
    WPARAM wParam,
    LPARAM lParam
);
//如果nCode<0,应该 返回 CallNextHookEx( xxx ) 的值,继续向后传递消息
//返回非零值表示对消息做了处理,就不会向下传递消息
 

LRESULT CALLBACK KeyboardProc(
	int code,
    WPARAM wParam, //表示虚拟键盘码,VK_* 表示,如VK_SPACE 表示空格键,VK_RETURN表示回车
    LPARAM lParam //Doc中的数据,并不表示数值为多少时是什么意思,而是在多少bit位时是什么意思,第29位(0开始)为1表示alt键按下
);

发送 WM_CLOSE 消息来关闭

BOOL UnhookWindowsHookEx(HHOOK hhk); //从HOOK链中删除

HMODULE WINAPI GetModuleHandle(//得到当前dll的句柄,也可以使用DllMain中得到
  __in          LPCTSTR lpModuleName//.dll或.exe,如没有扩展名自动加.dll,
);

使用SetWindowsPos(的wndTopMost ,显示为最顶层窗口  ,GetSystemMetrics(SM_CXSCREEN,SM_CYSCREEN得到屏幕的宽高

如在.dll使用全局变量 HWnd,对Hook当前桌面的所有线程都相关的消息函中做按键退出,如切换到cmd中窗口按键,应该也会退出,如切换到记事本就不会
如有修改全局变量,会先复制一份数据页,在复本上做修改

dumpbin -headers xx.dll 查看 SECTION HEADER 区,标准以.开头的名字,有Execute Read Write权限的说明,Summary区看有多少

#pragma data_seg("MySec");//建立一个新的Section,名字长度不能超过8个
这里放定义的全局变量,必须初始化
#pragma data_seg();

#pragma comment(linker,"/section:MySec,RWS");//共享Section,RWS为ReadWriteShare,使用dumpbin -headers xx.dll 查看会多一个Share
也可以在.def文件中加 ,可以查Doc的LIBRARY
SECTIONS
MySec READ WRITE SHARED    //READ WRITE SHARED 可以大写也可小写

因全局变量放在共享的Section中,如切换到记事本做按键就会退出

SetWindowsHookEx使用WH_GETMESSAGE,就可以得到WM_GETTEXT消息,在响应函数中得到文本框的值(密码破解)

===========得到,CPU,系统,当前程序的位数
SYSTEM_INFO info;
GetSystemInfo(&info);//GetNativeSystemInfo
int arc=info.wProcessorArchitecture;//CPU
if(PROCESSOR_ARCHITECTURE_INTEL==arc)//x86
{
	cout<<"GetSystemInfo 32bit CPU"<<endl;
}else if(PROCESSOR_ARCHITECTURE_AMD64==arc)//x64(AMD or intel)
{
	cout<<"GetSystemInfo 64bit CPU"<<endl;
}
cout<<"number of processor:"<<info.dwNumberOfProcessors<<endl;
//----------
BOOL is64bit=FALSE;
IsWow64Process(GetCurrentProcess(),&is64bit);//CPU
if(is64bit)
	cout<<"IsWow64Process 64bit CPU"<<endl;
else
	cout<<"IsWow64Process 32bit CPU"<<endl;

//----------
int len=0;
len=sizeof(char*);//VC2012 32位时是4,64位时是8,只是知道当前的程序是多少位编译的
len=sizeof(int *);//VC2012 32位时是4,64位时是8,只是知道当前的程序是多少位编译的
char * app=(len==4)?"32bit":"64bit";
cout<<"current application compiled is "<<app<<endl;

//----------
OSVERSIONINFO ver;//OSVERSIONINFO 或 OSVERSIONINFOEX
ver.dwOSVersionInfoSize=sizeof(OSVERSIONINFO);
GetVersionEx(&ver);
DWORD major=ver.dwMajorVersion;//5是xp,2000,2003 server,6是vista, 2008 server
DWORD min=ver.dwMinorVersion;//  XP是5.1,vista是6.0 , win7是6.1,win8是6.2
TCHAR* sp=ver.szCSDVersion;//Server Pack n

//如何知道windows的位数????
getchar();
 

=============edge 插件开发
https://code.msdn.microsoft.com/How-to-add-a-Hello-World-4af3463b

地址栏输入  about:flags
开发者设置 ->选中 启用开发人员扩展功能(这可能让设备处于危险之中) ->重启edge生效

...按钮->扩展->加载扩展->选择目录 

在一个目录中建立文件
---manifest.json  文件 
 {
  "author": "Microsoft OCOS Team",
  "description": "Get information of the active tab.",
  "icons":
    {
      "48": "icons/microsoft.png",
      "96": "icons/microsoft-96.png"
    },
  "manifest_version": 2,
  "name": "HelloWorld",
  "version": "1.0",
  "permissions": [
    "tabs"
  ],
  "browser_action": {
    "default_icon": {
      "30": "icons/microsoft-30.png"
    },
    "default_title": "HelloWorld",
    "default_popup": "GetTabInfo.html"
  }
}

---GetTabInfo.js 文件 
document.addEventListener("click", function(e) {
    if (!e.target.classList.contains("tabInfo")) {
        return;
    }

    var root = document.getElementById("info");
    root.innerHTML = "";
    browser.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        browser.tabs.get(tabs[0].id, function (tab) {
            var node = document.createElement("div");
            var textnode = document.createTextNode("Url: " + tab.url);
            node.appendChild(textnode);
            root.appendChild(node);
            var node2 = document.createElement("div");
            var textnode2 = document.createTextNode("Title: " + tab.title);
            node2.appendChild(textnode2);
            root.appendChild(node2);
        });
        root.style.display = "block";
    });
});
----GetTabInfo.html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
      <link rel="stylesheet" href="GetTabInfo.css" />
  </head>
	<!-- copy from  https://code.msdn.microsoft.com/How-to-add-a-Hello-World-4af3463b-->
  <body>
    <div class="tabInfo">Get Tab Info</div>
    <div id="info" style="display:none"></div>
    <script src="GetTabInfo.js" ></script>
  </body>
</html>

--GetTabInfo.csshtml {
  width: 350px;
}

.tabInfo {
  margin: 3% auto;
  padding: 4px;
  text-align: left;
  font-size: 1.5em;
  background-color: #E5F2F2;
  cursor: pointer;
}

.tabInfo:hover {
  background-color: #CFF2F2;
}
#info{
    border:2px solid black;
}

目录文件 icons/microsoft.png   microsoft-30.png  microsoft-96.png

...按钮->扩展->就有一个HelloWorld插件了， 可以开启显示在地址栏旁显示按钮




=============ATL　COM
http://msdn.microsoft.com/zh-cn/library/t9adwcde.aspx
http://msdn.microsoft.com/en-us/library/ms221145%28v=vs.85%29.aspx  SafeArray相关函数
http://msdn.microsoft.com/en-us/library/ms680586%28v=vs.85%29.aspx  Co开头函数
http://msdn.microsoft.com/en-us/library/aa367088%28v=vs.85%29.aspx　MIDL

COM 是可多语言的
ATL 是为快速开发COM,ATL只能用于C++,ATL 比 MFC 小

CComObject 是继承自一个template的

template <class T>
T* pt=static_cast<T*>(this)
使用方法
CComObject<MyBase> *p=new CComObject<MyBase>;
p->myMethod();
delete p;
虚函数,性能低

COM接口  (图形,IUnkown画在左上角,其它在左边)
自定义接口:从IUnkown 派生,快,但受限于编程语言,
HRESULT QueryInterface(...) ,返回值可用SUCCEEDED(有S_*),FAILED(有E_*),
InterlockedIncrement(&count)系统函数,做引用计数加一
InterlockedDecrement (&count)系统函数,做引用计数减一 ,当为0时delete资源
#include <initguid.h>
DEFINE_GUID(xxx) //只可定义一次

派发接口:脚本程序如JavaScript,可以使用,是IDispatch接口实现,IDL描述
IDispatch接口的GetIDsOfNames ,返回函数的DISPID
IDispatch接口的Invoke
速度慢

双重接口 也是从IDispatch接口派生,vbtl


COM组件信息在注册表 HKEY_CLASSES_ROOT\CLSID键下,要写入
	ProgID		记忆名称
	InprocServer32	进程内部,是dll文件名称
	VersionIndependentProgID 组件最新版本
	LocalServer32	是exe文件名称
DLL中要有 DllRegisterServer和DllUnregisterServer函数,使用regsvr32 来注册

Tools->Create GUID 一个,占16个字节 ,
CoCreateGuid 函数
IsEqualCLSID
IsEqualGUID
IsEqualIID

CATID (category组件类别) 在HKEY_CLASSES_ROOT\Component Categories键下
ICatRegister , ICatInformation函数,就不用操作注册表了
要一个CATEGORYINFO
typedef struct tagCATEGORYINFO {
  CATID   catid;
  LCID    lcid;
  OLECHAR szDescription[128];
} CATEGORYINFO;

CCM(Component Categories Manager)系统类别


IDL,被MIDL编译器,使用interface 定义class
开头的[ .. ]是接口属性
函数参数可用 [in] ,[out]
用object修饰 id,返回HRESULT,

对virtual的类,每一个子类都有一个vptr和vtable
因使用vtable中的指针,所以COM中不能有属性
COM是与语言无关,调用组件者,创建组件用 CoCreateInstance 参数要CLSID ,IClassFactory ,返回指向vtable的指针

COM要求在改变一个接口时,要新加一个如以Ex结尾

COM的数据类型 OLECHAR ,在windows平台上是wchar_t
COM的数据类型 BSTR,是一个带长度前缀的OLECHAR数组的指针, 以NULL结束, 必须使用SysAllocString 和 SysFreeString

wchar_t wsz[]=L"aa";
BSTR bstr;
bstr=SysAllocString(wsz);


CComBSTR是封装类,有成员m_str,
HRESULT PutName(BSTR pNewValue);//声明
CComBSTR bstrName=OLESTR("aa");
PutName(bstrName);//转换为 BSTR
_bstr_t 类 转换



VARIANT va;
::VariantInit(&va);
int a=2010;
va.vt=VT_I4;//varable type
va.lVal=a;

VariantClear ,VariantChangeType,VariantCopy

CComVariant  对VARIANT 的封装

_variant_t 是用于COM 的 VARIANT 类

COM的 SAFEARRAY 类型
IDL中的声明
	interface IMyInterface IUnKnow
	{
		HRESULT GetArray([out,retval] SAFEARRAY(long)* pArray);
	}
long表示元素类型


typedef struct FARSTRUCT tagSAFEARRAY {
  unsigned short cDims; //数组维数
  unsigned short fFeatures;//如何分配置和释放标志
  unsigned short cbElements;//大小
  unsigned short cLocks;//锁,计数器
  void HUGEP* pvData;//数据区
  SAFEARRAYBOUND rgsabound[1];//每维数组的大小,是可变大小的vector
} SAFEARRAY;
 

typedef struct tagSAFEARRAYBOUND {
  ULONG cElements;//这个维度的元素数
  LONG  lLbound;//娄组的下界
} SAFEARRAYBOUND;


SAFEARRAYBOUND rbsabound[2];
rbsabound[0].cElements=3;
rbsabound[0].lLbound=0;
rbsabound[1].cElements=4;
rbsabound[1].lLbound=0;
//相当于C int x[3][4];
SAFEARRAY * psb=::SafeArrayCreate(VT_I4,2,rbsabound);
long nIndex[]={2,1}
long nElem;
::SafeArrayGetElement(psa,nIndex,nElem);//得到[1][2],nIndex是返顺序
::SafeArrayDestory();　

CComSafeArray类的封装
CComSafeArrayBound b1(2,0);
CComSafeArrayBound b2(3,0)
CComSafeArrayBound b3(4,0)
CComSafeArrayBound rgBounds[]={b1,b2,b3}//[2][3][4]
CComSafeArray<int> sArray(&rgBounds,3);

CComSafeArray的函数　MultiDimSetAt 和 MultiDimGetAt
int rgIndexElement[]={1,2}//[2][1]
long val;
sArray.MultiDimGetAt(rgIndexElement,val);//rgIndexElement反的
智能指针,封装了 QueryInterface/Release, CoCreateInstance/Release,
CComPtr ,CcomQIPtr
CoInitialize(NULL);
CComPtr<Imath> ptrMath;
HRESULT hr=ptrMath.CoCreateInstance();
..
ptrMath.Release();
::CoUninitialize();

COM标准接口　IUnkown,
前三个函数要是 QueryInterface,AddRef,Release,
QueryInterface 会返回S_OK ,E_NOINTERFACE,要使用SUCCEEDED,FAILED宏
AddRef,Release,当计数为0时,自己自动销毁

CoInitialize/CoInitializeEx
CoUninitialize
CoGetClassObject //得到类厂实例,客户(容器)使用
CoCreateInstance/CoCreateInstanceEx
CoRegisterClassObject  //只对exe可调用
DllCanUnloadNow //容器调用
DllGetClassObject //容器调用

  HRESULT CoCreateInstance(
  _In_   REFCLSID rclsid,
  _In_   LPUNKNOWN pUnkOuter,
  _In_   DWORD dwClsContext,//CLSCTX_INPROC_SERVER,在同一进程中dll实现,CLSCTX_LOCAL_SERVER不同进程,exe实现
  _In_   REFIID riid,
  _Out_  LPVOID *ppv
);
  
对使用.exe实现,进程通信使用LPC,基于RPC实现,使用Proxy/Stub 来做,建立ATL项目时,要选择 allow merging of proxy/stub

IMarshal 接口

ATL把基于dll的COM和基于exe的COM差异屏蔽

包容
CComObejctRootEx ,FinalContruct,FinalReleases

聚合
GetControllingUnknown( )函数;//前面必须DECLARE_GET_CONTROLLING_UNKNOWN
在BEGIN_COM_MAP中声明 公开的函数
	COM_INTERFACE_ENTRY_AGGREGATE(...)
IDL文件中
library 组件库
coclass定义一个组件	
==========
