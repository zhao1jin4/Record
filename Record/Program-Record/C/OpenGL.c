 
http://www.mesa3d.org/
  
http://www.opengl.org/resources/libraries/glut/
http://www.opengl.org/sdk/docs/man4/ 4.4 在线手册
http://www.opengl.org/sdk/docs/man3/ 3.3 在线手册

-----freeglut,安装测试OK
rpm -ivh /mnt/iso/suse/x86_64/freeglut-devel-2.8.0-3.1.2.x86_64.rpm 
rpm -ivh /mnt/iso/suse/x86_64/libglut3-2.8.0-3.1.2.x86_64.rpm
rpm -ivh /mnt/iso/suse/noarch/xorg-x11-devel-7.6-37.1.1.noarch.rpm  有太多的依赖关系，使用yast2

#include <GL/glut.h>
#include <GL/glu.h>
#include <GL/gl.h> 

gcc -o helloOpenGL helloOpenGL.c -lglut -lGL -lGLU

注意和windows不同的是GL是大写的
-----


Debug 要把*.dll文件放在　PATH中
扩展名是大写的C表示cpp,是用g++编译
opengl32.dll和glu32.dll 位于 C:\WINDOWS\system32\

右击项目->properties->C/C++ Build -> Tool chain Editor 中可以　查看/修改　当前使用的编译器  MacOSX GCC/MinGW /CygWin/LinuxGCC/Microsoft Visual C++


---common.h
#ifndef COMMON_H_
#define COMMON_H_

	#ifdef __APPLE__
		#include <GLUT/GLUT.h>
	#elif defined(_WIN32)
		#pragma comment (lib, "glew32.lib") //cl编译器读.lib
		#include <GL/glew.h> //注意顺序
		#include <GL/glut.h>
		#include <windows.h> //...
	#else
		#include <GL/glut.h>
	#endif
	
	#include <stdio.h>
	#include <stdlib.h>
	#include <math.h>

#endif /* COMMON_H_ */

---chapter2/chapter2_bounce.c文件中
#if defined(_MSC_VER) //对cl编译器
	#include "../common.h"
#endif 
namespace chapter2_bounce
{
	#include "../common.h"
}

如在windows上使用 高于openGL-1.1版本的
gcc -L 只加 glew-1.10.0-win32\glew-1.10.0\bin\Release\Win32\glew32.dll 就OK
-glew32




------windows 64bit 要机器上下载 Cygwin-1.7.28,下载日期是 2014-03-10,已经选择的有

openssh-6.5p1-1
vim-7.4.182-1

gcc-core-4.8.2-3
mingw-gcc-core-4.7.3-1
mingw64-i686-gcc-core-4.8.2-1
mingw64-x86_64-gcc-core-4.8.2-1
mingw64-x86_64-gcc-4.8.2-1

gcc-g++-4.8.2-3	
mingw-gcc-g++--4.7.3-1 
mingw64-i686-gcc-g++-4.8.2-1
mingw64-x86_64-gcc-g++-4.8.2-1

*mingw64* 这里也会自动下载相关联的(如headers,pthreads,runtime,binutils),安装时可以所有的全部选择安装

gdb-7.6.50-4

make-4.0-2   devel GNU
automake-1.14-1

autoconf-2.69-2

---以下可选
groff 
binutils

libxml2 
libxml2-devel 
libxml2-doc 

libxslt 
libxslt-devel 
libxslt-doc 
---------Cygwin下编译openGL   win7 64 OK
cygwin的一些命令
cygpath -u D:/cygwin/bin/make  会返回/usr/bin/make
cygpath -m /usr/bin/make  	   会返回D:/cygwin/bin/make
cygcheck -c cygwin  显示当前Cygwin 安装的版本	


PATH=C:\cygwin\bin

把*.lib文件复制到 C:\cygwin\lib
把glut.h复制到C:\cygwin\usr\include\w32api\

gcc -o helloOpenGL helloOpenGL.c -lglut32 -lglu32 -lopengl32 -I glutdlls37beta -L glutdlls37beta/

Debug时记得要把.dll 文件放入PATH中

Cygwin 错误的提示是中文乱码,不能定位到文件中,加环境变量 LANG=en 就OK了
右击项目->properties->C/C++ Build -> Enviroment->Add ...

Debug默认是不能显示源码的,报错,/cygdrive/d/Program/
要设置 Window -> Preferences -> C/C++ -> Debug->Source Lookup Path -> Add->Path Mapping->Add->
输入 \cygdrive\d	对应 d:\,就OK了
 
Cygwin中必须加#include <windows.h>,而Gnusetup可以部分不加的

右击项目->properties->C/C++ Build -> Settings -> 在Cygwin C++ Linker -> Libaraies->
在 l  中 增加三项,glut32,glu32,opengl32

---------MinGW  编译 win7 64OK

g++ -o simple simple.c -lopengl32 -lglu32 -lglut32   -D_STDCALL_SUPPORTED   在XP上才可通过,注意_STDCALL_SUPPORTED
g++ -o simple simple.c -lopengl32 -lglu32 -lglut32   -D_STDCALL_SUPPORTED -D_M_IX86  -DGLUT_DISABLE_ATEXIT_HACK

mingw-get-inst-20111118   选择gcc,g++自动带gdb 

set PATH=D:\program\MinGW\bin
set LIBRARY_PATH=D:\program\MinGW\lib
set C_INCLUDEDE_PATH=D:\program\MinGW\include
set CPLUS_INCLUDE_PATH=D:\program\MinGW\lib\gcc\mingw32\4.6.1\include;D:\program\MinGW\include
以下是CDT 自动识别的
D:\program\MinGW\lib\gcc\mingw32\4.6.1\include\c++\mingw32
D:\program\MinGW\lib\gcc\mingw32\4.6.1\include\c++
D:\program\MinGW\lib\gcc\mingw32\4.6.1\include\c++\backward
D:\program\MinGW\lib\gcc\mingw32\4.6.1\include-fixed

把glut.h复制到C:\MinGW\include\GL
把glut32.lib复制到C:\MinGW\lib

gcc -o helloOpenGL helloOpenGL.c -D_STDCALL_SUPPORTED   glut32.lib -lopengl32 -lglu32  
手工运行OK
 
右击项目->properties->C/C++ Build -> Settings -> MinGW C++ Linker -> Libaraies->在 l  中 增加三项
-lopengl32
-lglut32 
-lglu32

提示加 -fpermissive

------------------GNUSetup中也可gcc g++
默认加的PATH
D:\program\GNUstep\GNUstep\System\Tools;D:\program\GNUstep\bin

GNUSetup也可以使用gcc编译CDT中的 openGL debug 也是OK,也可编译C++
只加默认的PATH就可以了,CDT会自动加相当的include 

C是(32位机)
D:\program\GNUstep\include
D:\program\GNUstep\lib\gcc\mingw32\4.6.1\include
D:\program\GNUstep\lib\gcc\mingw32\4.6.1\include-fixed

C++ 多加的是(32位机)
D:\program\GNUstep\lib\gcc\mingw32\4.6.1\include\c++
D:\program\GNUstep\lib\gcc\mingw32\4.6.1\include\c++\backward
D:\program\GNUstep\lib\gcc\mingw32\4.6.1\include\c++\mingw32


命令行编译也OK,只加默认的PATH就可以了
D:\program\GNUstep\bin\gcc -o helloOpenGL helloOpenGL.c glut32.lib -lopengl32 -lglu32  //OK

D:\program\GNUstep\bin\g++ -o CPP_STL.exe CPP_STL.cpp  //OK

GNUSetup可以和Cyginw混合使用
eclipse CDT建立项目后->右击项目->properties->tool chain edit 中多了一个MacOSX GCC,但不能使用

---------VC++  win7 64OK
VS 2008 tools->options->Projects and Solutions tab ->VC++ Directories 已经过时
VS 2010 右击项目->Configuration Properties->VC++ Directories ->Library Directories 右则下拉Edit...-> *.lib目录
															->Include Directory	*.h目录
															->Executable Directory  放 *.dll 无效果

glut.h  放在  C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Include\gl
*.lib   放在　C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Lib
*.dll 　放在　PATH设置的值中，也可放在　C:\WINDOWS\system32　中 (自带opengl32.dll)


VC2010 设置环境变量
path=C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE;C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\bin;
lib=C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Lib;C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\lib
include=C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Include;C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\include

cl /I "C:\temp\glut\include" /c  helloOpenGL.c
link helloOpenGL.obj /LIBPATH:"C:\temp\glut\lib"  //如果要生成.dll文件加  /DLL
set path=%path%;C:\temp\glut\bin
测试OK

VC 2010  编译openGL时报LINK : fatal error LNK1123: failure during conversion to COFF: file invalid or corrupt
项目-->工程属性->配置属性-> 清单工具->输入和输出->嵌入清单，选择[否] 

------CDT使用VC++
.C 大C结尾的文件在cl编译器被认为是C,要用.cpp才是C++
不用加 glut32 ...,不用加-D ,不能使用debug

---------Linux下
---linux下的 freeglut和glut类似 
 
zypper install freeglut-devel


rpm -ivh /mnt/iso/suse/x86_64/freeglut-devel-2.8.0-3.1.2.x86_64.rpm 
rpm -ivh /mnt/iso/suse/x86_64/libglut3-2.8.0-3.1.2.x86_64.rpm
rpm -ivh /mnt/iso/suse/noarch/xorg-x11-devel-7.6-37.1.1.noarch.rpm  有太多的依赖关系，使用yast2
  						
windows下 手工放glut.h到GL目录	(同linux)	会自动 #include <GL/gl.h> 和 #include <GL/glu.h>
Linux下 FreeGLUT 是 #include <GL/glut.h> 会自动 #include <GL/gl.h> 和 #include <GL/glu.h>
Mac下 是 #include <GLUT/GLUT.h> 			会自动 #include <OpenGL/gl.h> 和 #include <OpenGL/glu.h>  要加 GLUT.framework 和 OpenGL.framework
	g++  -framework GLUT -framework OpenGL  helloOpenGL.c -o helloOpenGL
	CDT->C/C++ Build->Settings->Linker->Miscellaneous->在Linker flags中加入 -framework GLUT -framework OpenGL  测试OK
	Tool Chain Editor 为MacOS GCC 应该有/Library/Framework 和 /System/Library/Framework 的include


lib是加  
-lglut
-lGL
-lGLU

gcc -o helloOpenGL helloOpenGL.c -lglut -lGL -lGLU  测试OK


---------Netbeans 7 和Cygwin_4.x 或者 MinGW (不是TDM)
hello程序可以Debug 
//应该不用安装netbeans-opengl-pack,
右击项目->属性->生成->链接器->库 右则的 ...->增加选项按钮->选择 其它选项 ,分别加入-lglu32,-lglut32,-lopengl32
可以使用netbeans debug,可以使用netbeans运行
库的复制同CDT的配置

Netbeans默认使用UTF-8来打开文件,如是GBK要修改C++项目目录\nbproject\project.xml
<!--<sourceEncoding>UTF-8</sourceEncoding>-->
<sourceEncoding>GBK</sourceEncoding>
修改后要重新启动Netbeans才生效


建立文件夹是逻辑的
格式->完成代码
-----------Mac 下
安装Command line tools后有 /System/Library/Frameworks/GLUT.framework
GLUT.framework有 <GLUT/GLUT.h> 会自动引入 <OpenGL/gl.h>,<OpenGL/glu.h>,有 #pragma comment (lib, "glut32.lib") ,依赖于OpenGL.framework

g++  -framework GLUT -framework OpenGL  helloOpenGL.c -o helloOpenGL 
CDT->C/C++ Build->Settings->Linker->Miscellaneous->在Linker flags中加入 -framework GLUT -framework OpenGL  测试OK
Tool Chain Editor 为MacOS GCC 应该有/Library/Framework 和 /System/Library/Framework 的include
---------

Microsoft  的 DirectX  
GDI(Graphics Device Interface)
跨平台的语言的OpenGL4.1

Gnome 和 KDE  都是基于 X Window ,X Window 的 OpenGL 扩展--GLX ,(GtkGLExt 所使用的 GLX ),windows 可以用 WGL , Mac OS用 CGL  (Core), 


Android 3D 使用 openGL ES-2.0 (Embeded System)  ,开发环境android-NDK,Cygwin+CDT

光效三要素  Ambient 环境(全局的阴影区),Diffuse 散射(过渡区),Specular 高光(亮区)

GL_FLAT  面的颜色是多边形的最后一个顶点的颜色 ,无反射,不真实
GL_SMOOTH 默认的,面有多个顶点的颜色

CRT(cathode ray tube) 阴极  射线 管


ARB (Archtecture Review Board)

结构体 GLclampf   代表颜色强度
函数 glColor3f(...) ,3f表示使用了3个浮点参数,Color是根,只给glColor表示函数族

用单精度速度快

openGLUT    Utility tookit 3.7 要下载的
openGLU     是标准


windows 下 .dll 文件要放在 PATH 环境变量目录下,
VC中加载.lib文件  Tools->optional->VC++...
VC 中建立空白项目 不要选择  precomiple header,并选中   empty project


#include <windows.h>
#include <gl/gl.h>
#include <gl/glu.h>
#include "glut.h"

GLUT_SINGLE 单缓冲
GLUT_RGB
GLUT_DOUBLE 双缓冲

glutWireSphere(radius,slices,stacks);//slices 是经度,stacks 是纬度

tetrahedron 四面体
octahedron 八面体
dodecahedron 十二面体
granularity 粒度


Z 轴 近的是正值 ,远的是负值


GL_LINE_STRIP 带条
GL_LINES ,两个配对,第一线和第二线要相连必须有相同点,如果是奇数个点,最后一个丢弃

stipple 点画线
glEnable(GL_LINE_STIPPLE) 虚线

glLineStipple(放大因子,pattern);
放大因子(负数无效)
16位的pattern是反向的,即最后一位对应最开始显示的   (原因是使用了左移)


默认三角形是的逆时针绕法是正面对着 ,如要修改 glFront(GL_CW); //CCW 是逆时针 Counter ClockWise

cull 剔除 
quad 四方院
glPolygonStipple();填图,32*32   ,4*8   4*8

glGetFloatv   v表示void参数指针

glRotate(角度,从原点到x,y,z 的直线);

const GLubyte* name = glGetString(GL_VENDOR); //返回负责当前OpenGL实现厂商的名字
const GLubyte* biaoshifu = glGetString(GL_RENDERER); //返回一个渲染器标识符，通常是个硬件平台
const GLubyte* OpenGLVersion =glGetString(GL_VERSION); //返回当前OpenGL实现的版本号
const GLubyte* gluVersion= gluGetString(GLU_VERSION); //返回当前GLU工具库版本
void RenderScene(void)
{
	//GL_PI
	glClear(GL_COLOR_BUFFER_BIT);

	glColor3f(1.0f, 0.0f, 0.0f);
	glRectf(-25.0f, 25.0f, 25.0f, -25.0f);

	glPushMatrix();
	glRotatef(角度,从原点到x,y,z 的直线);
	glPopMatrix();


	glGetFloatv(GL_LINE_WIDTH_RANGE,fSizes);//GLfloat fSizes[2];	
	glLineWidth(fSizes[0]);//线宽

	glGetFloatv(GL_POINT_SIZE_RANGE,sizes);//GLfloat sizes[2];
	glGetFloatv(GL_POINT_SIZE_GRANULARITY,&step);//GLfloat step;,粒度
	glPointSize(sizes[0]);

	glEnable(GL_LINE_STIPPLE);//虚线
	glLineStipple(放大因子,pattern);//放大因子负数无效,GLushort pattern = 0x5555;//16位最后一位对应最开始显示,101101  ,1是线,0是空
	//再用glBegin(GL_LINES)

	glBegin(GL_LINES);//两个点一对,GL_LINE_STRIP 连续的直线,GL_POINTS
	glVertex3f(x, y, z);//如果是奇数个点,最后一个丢弃  
	glutWireTeapot(30.0f);
	glEnd();


	glEnable(GL_POLYGON_STIPPLE);
	glPolygonStipple(fire);//多边形旋转后图案并不有旋转
	glBegin(GL_POLYGON);//使用多个图案填充多边形


	glEnable(GL_SCISSOR_TEST);
    glScissor(100, 100, 600, 400);//创建一个局部区域,剪刀
	...
    glDisable(GL_SCISSOR_TEST);

	//gluOrtho2D (GLdouble left, GLdouble right, GLdouble bottom, GLdouble top);

	int nModeMenu = glutCreateMenu(ProcessMenu);//代码的顺序
	glutAddMenuEntry("Solid",1);
	glutAddMenuEntry("Outline",2);
	glutAddMenuEntry("Points",3);

	int nEdgeMenu = glutCreateMenu(ProcessMenu);
	glutAddMenuEntry("On",4);
	glutAddMenuEntry("Off",5);

	int nMainMenu = glutCreateMenu(ProcessMenu);
	glutAddSubMenu("Mode", nModeMenu);
	glutAddSubMenu("Edges", nEdgeMenu);
	glutAttachMenu(GLUT_RIGHT_BUTTON);//右键菜单
	/*void ProcessMenu(int value)
	{
	switch(value)
		{
		case 1:
			glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);//GL_POINT,GL_FILL
			glEdgeFlag(TRUE);
	*/



	
	glClearStencil(2.5f);//0 is clearly,蒙板
    glEnable(GL_STENCIL_TEST);
	glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	glStencilFunc(GL_NEVER, 0x0, 0x0);// ??????????????????????
    glStencilOp(GL_INCR, GL_INCR, GL_INCR);//increment  ??????????????????????
	...背景	
	glStencilFunc(GL_NOTEQUAL, 0x1, 0x1);//!=
    glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
	glRectf(...)
	//glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_STENCIL);|GLUT_DEPTH




	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_CULL_FACE);//反面显示空,GL_DEPTH_TEST
	glDisable(GL_CULL_FACE);	

	glPolygonMode(GL_BACK,GL_LINE);//反面显示线
	glPolygonMode(GL_BACK,GL_FILL);

	glShadeModel(GL_FLAT);
	glFrontFace(GL_CW);//三角形 顺时针绕是正面的

	glPushMatrix();//保存
		glTranslatef(x,y,z);//对坐标系变换,
		glRotatef(angle,x,y,z);//原点到 x,y,z 的直线
		//太阳系示例,要先转再移动,不能反过来,否则就自转了???????????????????
		glScalef(x,y,z);
	glPopMatrix();
	glNormal3f(1.0f, 0.0f, 0.0f);//法线方向
	
	glLoadIdentity()//加载单位距阵matrix,恢复原点,没有变换发生,乘以单位距阵不做任何变化,每次变换模型(MODEL)距阵自乘
	//距阵内容x,y,z,w(缩放)
	glLoadMatrix()//glLoadMatrix (glFloat[4,4])  如是4x4的单位矩阵 同  glLoadIdentity()

	glEnable(GL_LIGHTING);
    glLightModelfv(GL_LIGHT_MODEL_AMBIENT,whiteLight);//GLfloat  whiteLight[] RGBA四个值,值范围0~1,全局环境光
    glLightfv(GL_LIGHT0,GL_AMBIENT,sourceLight);
    glLightfv(GL_LIGHT0,GL_DIFFUSE,sourceLight);
    glLightfv(GL_LIGHT0,GL_POSITION,lightPos);
    glEnable(GL_LIGHT0);
   
	glEnable(GL_COLOR_MATERIAL);
    glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);//会根据后面使用的glColor 函数来决定
	
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE,GLfloat [] RGBA);//材质的前面有什么属性,后面的物体使用这个属性

	glEnable(NORMALIZE);//单位法线1
	glEnable(GL_RESCALE_NORMALS);//缩放时法线长短
	
	glMaterialfv(GL_FRONT,GL_SPECULAR,xx);//四个值的GLfloat 的数组
	glMateriali(GL_FRONT,GL_SHININESS,128);//高光大小,值越大,光泽越明显

	glLightf(GL_LIGHT0,GL_SPOT_CUTOFF,60.0f);//聚光灯的角度
	glLightfv(GL_LIGHT0,GL_SPOT_DIRECTION,spotDir);//聚光灯的照的方向

	glPushAttrib(GL_LIGHTING_BIT);//保存灯的属性
	glPopAttrib();


	//Shadow    can not compile ?????

	//glMultMatrixf();//剩 


	glRasterPos2i(100, 50);// 坐标左小角为原点
	glBitmap( GLsizei width, GLsizei height, GLfloat xorig, GLfloat yorig, GLfloat xmove, GLfloat ymove, const GLubyte *bitmap );
		xorig和yorig是图片的心点,xmove,ymove是移动的//画完后,移动到相对当前的下一次开始的位置


	glTexImage1D( GLenum target, GLint level, GLint internalFormat, GLsizei width, GLint border, GLenum format, GLenum type, const GLvoid *pixels );
	target 必须为 GL_TEXTURE_1D
	level 详细级别,一般0
	internalFormat 1,3和4分别用于RGB,RGBA纹理图像
	format 可取值 GL_COLOR_INDEX,GL_LUMINANCE,GL_LUMINANCE_ALPHA,GL_RGBA,...
	type 可取GL_UNSIGNED_BYTE

	glTexImage1D()定义纹理

	纹理模式
	GL_MODULATE 会调整颜色亮度
	GL_DECAL 颜色光照不影响外观
	GL_BLEND

	glTexEnvi(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE)//纹理  2.1在线手册

	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);// 重复贴图
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT)//
	glEnable(GL_TEXTURE_2D);

	glGenTextures( GLsizei n, GLuint *textures );//保存纹理 ,n号码 
	glBindTexture( GL_TEXTURE_2D, GLuint texture );//绑定纹理
	glDeleteTextures( GLsizei n, const GLuint *textures);//删除保存纹理

	glPrioritizeTextures( GLsizei n, const GLuint *textures, const GLclampf *priorities );//priorities 从0.0 -1.0(最高,纹理不一定在内存中)

	glPixelZoom	(xfactor,yfactor)//负数翻转
	
	glTexCoord2f();//纹理坐标
	glTexGeni( GLenum coord, GLenum pname, GLint param );
	glTexGeni(GL_S,GL_TEXTURE_GEN_MODE,GL_OBJECT_LINEAR);

	glFlush();//一次处理队列中的所有代码(即这个函数),加快速度
	//glutSwapBuffers(); //和GLUT_DOUBLE
}
void ChangeSize(int w, int h)
{
    glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);//GL_PROJECTION后用 glOrtho 或 gluPersepctive
	glLoadIdentity();
		//Orthographic Projections正射投影
		// Establish clipping volume (left, right, bottom, top, near, far)//显示坐标系统的部分区域
		glOrtho( GLdouble left, GLdouble right, GLdouble bottom, GLdouble top, GLdouble near_val, GLdouble far_val );//near是正,far是负
		//glOrtho 函数修改现有的修剪空间，要先调用glLoadIdenty() 恢复坐标系

		//透视投影是近大远小
		gluPerspective (GLdouble fovy, GLdouble aspect, GLdouble zNear, GLdouble zFar);//fovy是可见区域的大小,范围大,物体小
								
	glMatrixMode(GL_MODELVIEW);//用于场景中的对象变换
    glLoadIdentity();
}
void TimerFunction(value)
{
	 glutPostRedisplay();
	 glutTimerFunc(33, TimerFunction, 1)
}
void SpecialKeys(int key, int x, int y)
{
	if(key == GLUT_KEY_UP)//GLUT_KEY_DOWN,GLUT_KEY_LEFT,GLUT_KEY_RIGHT
	
}
void MouseFunc(int button, int state, int x, int y)
{
	if(button == GLUT_LEFT_BUTTON)
		if(state == GLUT_DOWN)
}		
glutInit(&argc, argv);
glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);//GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH  ,GLUT_STENCIL
glutInitWindowSize(800,600);
glutInitWindowPosition(200,800);//设置窗口显示位置,在 CreateWindow之前
glutCreateWindow("Simple");
glutDisplayFunc(RenderScene);// param self

glutReshapeFunc(ChangeSize);//weight,height,ChangeSize在第一次也会调用
glutTimerFunc(33, TimerFunction, 1);//33豪秒后调用一次函数, 
glutSpecialFunc(SpecialKeys);//按键事件
glutMouseFunc(MouseFunc);//鼠标事件
	
glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

glutMainLoop();








============Shader Language (SL) 着色语言 
Windows的gl和glu的版本是1.1,不支持Shader要用glew,Linux是1.3支持的


GLuint glCreateShader(GLenum type); //type 是 GL_VERTEX_SHADER 或 GL_FRAGMENT_SHADER,返回标识

void glShaderSource(GLuint shader, GLsizei count, const GLchar **string, const GLint *length); //把Shader与代码关联
void glCompileShader(GLuint shader); //glGetShaderiv(GL_COMPILE_STATUS) 取状态,返回GL_TRUE表示成功

void glGetShaderInfoLog(GLuint shader, GLsizei bufSize, GLsizei *length, char *infoLog); //编译错误信息,长度调用 glGetShaderiv(GL_INFO_LOG_LENGTH)


GLuint glCreateProgram();//返回标识
void glAttachShader(GLuint program, GLuint shader); //一个shader可以同时链接到多个program,一个program有多个shader

void glDetachShader(GLuint program, GLuint shader);//为了修改shader要Detach

void glLinkProgram(GLuint program);//glGetProgramiv()(GL_LINK_STATUS) 取状态

void glGetProgramInfoLog(GLuint program, GLsizei bufSize, GLsizei *length, char *infoLog);

void glUseProgram(GLuint program);//当正在使用,可以链接,编译,分离









