http://www.open-std.org/jtc1/sc22/wg21/   The C++ Standards Committee
Ritchie 研制的C语言

C/C++ 可以开发 WebAssembly (wasm)

https://docs.microsoft.com/zh-cn/cpp/cpp/?view=msvc-160
https://docs.microsoft.com/zh-cn/cpp/cpp/cpp-language-reference?view=msvc-160    C++
https://docs.microsoft.com/zh-cn/cpp/standard-library/cpp-standard-library-reference?view=msvc-160  STL ，看下来以c开头的头文件里面都没什么东西 
 
http://cplusplus.com/ 参考手册 有c++11
https://en.cppreference.com/w/ 参考手册 有C++11, C++14, C++17, C++20, C++23  
http://cppds.com/ 中文的

Compiler Explorer 在线在编译执行c++工具，可以看生成的汇编
https://godbolt.org/

CDT 比 VSCode 好的地方是 鼠标滑过C的函数上，会有文档提示，但struct或C++类，类中的函数就不行了
============Clang LLVM
clang 发单是以k开头的 ，意思为（使）叮当地响

https://clang.llvm.org/
https://releases.llvm.org/download.html 
二进制在 
https://github.com/llvm/llvm-project/releases
	amd64 x64 架构的linux版本只有 ubuntu, 还有freebsd,apple-darwin,windows

Clang 5完全支持c++17(编译选项  	-std=c++17 )，后面版本的状态 
https://clang.llvm.org/cxx_status.html 

Clang 背后的 LLVM (Low Level Virtual Machine) 像 Swift、Rust 等语言都选择了以 LLVM 为后端。
	
	 
windows版本的LLVM-13.0.0-win64.exe   安装后,安装后里面有clang++ ,clang-cl 命令, clangd 语言服务器 
	可以直接  clang++ 或 clang .\hello.cpp -o hello.exe 立即成功(不加-o输出a.exe), -g 输出调试信息
	
	clang++ -g  -std=c++17 src/hello.cpp -o bin/hello #在windows 下，如安装了vs2019用 -std=c++11 就会报错
	clang++ -std=c++17 -stdlib=libc++ e:\vscode_workspace\clang_demo\src\hello.cpp -o bin/hello --debug ,警告未使用 -stdlib=libc++
	 
	lldb 调试(对应gdb)，依赖于python36.dll 
	 https://www.python.org/downloads/windows/ 可以找到windows下的老版本二进制
	
	lldb hello.exe
	(lldb) 或者这里 file hello.exe 类似于gbd，也可用 target create hello
	lldb -h 有帮助示例
	lldb -p <pid>
    lldb -n <process-name>
	lldb -c /path/to/core 
	(lldb) process attach --pid 7232 
	(lldb) process attach --name test 
	(lldb) source list # 源码列表
	(lldb) list 10 #查看源码第10行
	
	(lldb) breakpoint list 		#缩写为 br l
	(lldb) br set -n  main    #-n函数名
	(lldb) br s -f hello.cpp -l 6 # --file --line如果位置不存在,WARNING 那么locations=0，无效
	(lldb) help breakpoint set  #看有-f,-l -n
	(lldb) br del 2 			#删除第2个断点
	(lldb) breakpoint disable 1 #禁用
	(lldb) breakpoint enable 1  #启用
	
	(lldb) watchpoint list 		#条件断点
	
	(lldb) process launch hello  #也可使用run 命令,windows 版本python 提示没有 encodings 模块,设置 PYTHONPATH=D:\Application\Python36\Lib
	
	(lldb) process launch -- abc #启动程序 -- 后是程序参数
	(lldb) step #进入
	(lldb) c 	#继续运行Continue
	(lldb) n 	#或next,代码级逐步执行
	(lldb) p xx #或print,或expression, call,打印你想看的变量，或者p 回车，输入表达式，再回车结速
	
	(lldb)th list #thread list线程列表 ，help thread
	(lldb)thread backtrace 当前线程的调用栈
	(lldb)th se 29 #线程选择
	(lldb) thread continue 
	(lldb) thread step-in    
	(lldb) thread step-over  
	(lldb) thread step-out
	(lldb) 直接回车是上次执行的命令，如thread step-in 
	
	(lldb) memory read -f x -s 1 -c 20 &a  #-f 格式 x十六进制	,-s 1字节大小为1，-c 20 显示20*-s的字节，变a的地址
	(lldb)command alias xx yy #定义别名
	(lldb)command unalias xx #取消定别名
	(lldb)settings list  #显示所有设置
	
	lldb 命令行调试，目前不能看到源码？？？只能用vscode调试可以看到源码

clang-cl  被设计用来兼容Visual C++ 的编译器，cl.exe

	兼容cl的用法
	clang-cl /? 
	clang-cl   /c  hello.cpp 生成hello.obj ,可以加  /I "xx\include"
		 /c    Compile only

输入lld命令提示
lld is a generic driver.
Invoke ld.lld (Unix), ld64.lld (macOS), lld-link (Windows), wasm-ld (WebAssembly) instead



lib=C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.29.30133\lib\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.19041.0\ucrt\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.19041.0\um\x64

或powershell用
$env:lib='C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.29.30133\lib\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.19041.0\ucrt\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.19041.0\um\x64'

lld-link  hello.obj 默认生成 hello.exe
	可用 /out:bin/hello.exe 来修改, 也有 /LIBPATH:"C:\temp\glut\lib"

clang++ -c src/hello.cpp  -o bin/hello.o
	-c   Only run preprocess, compile, and assemble steps #和gcc一样
clang++ bin/hello.o -shared -fPIC -o bin/libhello.so  #和gcc一样
也有类型的 -I -L 选项，虽然-help中没有-l 但linux测试是正常的


但windows测试不行 ,-ladd 认为是找 add.lib， https://blog.csdn.net/x_studying/article/details/53561101  ?????
clang -g --target=x86_64 -o bin/hello.exe .\src\hello.cpp    #加了  --target=x86_64 后找不到 iostream  ?????




zypper install clang11  
	安装了 libstdc++6-devel-gcc7-7.5.0+r278197-4.25.1.x86_64 
#zypper install clang11-devel 这个应该没用


clang++ -v 显示有gcc版本是7

clang++ --version


	
--hello.cpp
#include<iostream>
using namespace std;
int main(int argc, char* argv[])
{
    if(argc>=2){
      cout<<argv[1]<<endl;
    }
	int max=0;
    max++;
    cout<<"hello"<<endl;
        int input;
    cout<<"please input ";
    cin>>input;
    cout<<"input="<<input;
}



clang++ hello.cpp -o hello   #报找不到iostream
clang++  hello.cpp -o hello   报 'stdlib.h' file not found
export CPLUS_INCLUDE_PATH=/usr/include/c++/7:/usr/include/c++/7/x86_64-suse-linux:/usr/include 这个变量的目录是有先后顺序的
clang++ -g  -std=c++11 hello.cpp -o hello   报crtbegin.o没有,用  zypper install gcc7 解决了
 


clang++  -save-temps hello.cpp  -o hello  会保留.s(汇编语言)文件 



zypper install lldb11  
zypper install llvm11 

#zypper install llvm11-devel 后有llvm-config ,是对于include "llvm/xx" 的情况，使用opt命令
	llvm-config --cxxflags
	llvm-config --ldflags

sudo zypper install java-11-openjdk-devel 
clang++ -c TestCPPNative.cpp  -I /usr/lib64/jvm/java-11-openjdk-11/include -I /usr/lib64/jvm/java-11-openjdk-11/include/linux/


clang -g --target=x86_64 -o bin/hello.exe .\src\hello.cpp    #加了  --target=x86_64 后找不到 iostream?????


setbuf(stdout, NULL);//CDT 工具必须调用这个才立即输出到控制台,全局的
//fflush(stdout);//CDT 工具必须调用这个才立即输出到控制台，局部的

============C/C++基础

int max(x,y) //函数声明无参数类型
int x;		  //加类型
int y;
{
        if (x>y) return x;
        else return y;
}

int swapN(c,d);//c,d任意写,声明同void swapN(int,int);//
//...
swapN(a,b)//实现同int swapN(int a,int b){},默认返回int,方法可以没有return 
int a,b;
{
}


char str[]="I am Happy"; //系统加上一个'\0'
sizeof(str);

++ 和 *同级,   *p++ 和 *(p++) 相同  

int a[3][4];
*(*(a+0)+1) 表示a[0][1]的值
*(a[i]+j)或者*(*(a+i)+j)  是a[i][j]的值

int x12=(*(x+1))[2];//x[1][2]

int x01=*(*x+1)+2;//值为a[0][1]+2=4,注意错的
int x12_error1=*(x+1)[2];//错的


int (*p)[4]  //表示指针变量 ,指向4个int的数组,p先与*结合表示是一个指针
int *p[4]//指针数组,每个元素都是指针,main 中 char *argv[]
int (*p)(int,int)//指向函数的指针  p=fun; (*p)(1,2);来调用
int *a(int x,int y )//返回指针的函数,有参数好区分


&引用声明符不可变的,声明初始化
int a1,a2;
int &b=a1;//b和a1 是同一个变量,b是个别名
int &b=a2;//重名错误,声明时初始化,


extern 函数外部定义，范围从定义开始到文件未，如果在定义点前想引用,引用前用 extern 来声明
		在一文件中定义 extern 变量，另一文件中用 extern 来声明
		//extern只是引用，定义是不要加extern

#ifdef xxx  // #if xxxx
#else
#endif

//const放*前 是指针指向常对象,不可改变我指向的值
//const放*后 是常指针,不要改变指向

const char * pl 或者  char const * pl//指向常对象的指针,不可改变我指向的值
const char c[]="boy";

如果形参是指向非const型变量的指针,实参只能用的指向非const的变量的指针

指向const型的指针 ,可以指任何的但不可以改

const 的属性只能通过初始化参数列表初始化

类名 const 对象名(参数)//定义常对象,也可const放最前,不能修改数据成员，不能调用非const的成员函数
void get_time() const//常函数,可以仿问对象成员不可以修改,如果一定要修改,成员要声明为 mutable 关键字

类中的成员为 const int hour;//只能通过参数初始化表,进行初始化

Time t1(10,20,15),t2;
Time * const ptrl=&t1; //指向对象的常指针,不能改变指向
ptrl=&t2//错误的



 int min=INT_MIN;//#include <climits>
 int max=INT_MAX;
	 
 
float *p=new float(3.14);
delete p;
char * x=new char[10]
delete []x; 

C语言中使用<malloc.h>
	void* malloc(unsigned long)
	free 

union P //共用体,最长的
{
	int grade;
	char posistion[10];
}
union P p2; //使用时最前的union可有可无

enum Fruit {apple, pear=2, orange, banana} frt1;
enum Fruit frt2;//C使用
Fruit frt3;//C++使用
frt2=(Fruit)2;
if(frt2==pear)
{
	printf("pear\n");
}

typedef struct	
{
	int month;
	int day;
	int year;
} DATE;

typedef int NUM[100] ;//NUM来代替　int[100]
typedef int (*PONTER)()//PONTER来代替指向函数的指针

void Student::display();//类外定义成员函数  类名::方法名

inline int max(int ,int)//编译时代码直接嵌入到主调函数中

C++/Java　中重载函数可以返回类型可以不同

函数模板　
template<typename T>　或者用 template<class T>　//无;
就可以用
T max(T a,T b,T c)
{

}
template<class T1,typename T2> //定义多个

float area(float r=6.5);//函数声明有默认参数，如有多参必须在最右边


参数初始化表　
Box:Box(int h,int w,int len):height(h),widht(w),lenght(len){}//height是类的成员
不可以用Box box1();但可以用Box box1;

也可以有默认参数,Box:Box(int h=1,int w=1,int len=1)
就不可以用Box box1;但可以用Box box1();,不能再重载

Student stu[2]={Student(1,2),Student(1,2)};//对象数组,无名

(*pt).hour和pt->hour相同
void (Time::*p2)();//定义,加Time:: 指向类的成员函数的指针
p2=&Time::get_time;//赋值 ，加&
Time t1(10,13,56);
(t1. *p2)();//调用


Student stu1,stu2;
stu1=stu2;//把stu2的数据成员赋值给 stu1的数据成员，成员中不能包括动态分配的的数据，给已有的
Box box2(box1)//对象的复制，是生成新的
//不必手写，相当于Box::Box(const Box &b); 
Box box2=box1;//C++的写方法


一个类的 friend 函数 可以仿问这个类的 private 成员,类中声明 friend 函数(一般自己的类作为参数，可是其它类名::方法名)，外部定义函数

friend 类名 ;//友元类，是单向的，不会传递

类的构造函数使用template的话，实例类的要 类<实类型> 对象(xx)

template <class numtype>
Compare(numtype a,numtype b) //类内的构造，Compare<int> c(1,2);
numtype Compare <numtype>::max();//类外定义 ,多了<numtype>

template <class T1,class T2>
Compare(T1 a,T2 b)
Compare<int,double> obj;



Complex operator + (Complex & c2 );//类中声明,把　operator +　当一个函数名
Complex Complex::operator + (Complex & c2 )
{
}
//只能对已有的C++ 运算符重载，** C++中没有不行的,不能改优先级，不能改操作个数，不能有默认的参数，不能全是C++标准类型
不能重载的有 ".", "*" ,"::","sizeof","?:"
成员函数，第一个必须是本类型
友元函数，运算符左侧对应第一个参数，

赋值运行符，下标运行符，函数调用运算符，必须定义为类的成员函数，不能是友元
++　重载　如函数中有一个 int 型的形参，就是后置，Timer operator ++(int)//后置, Timer++


istream & operator >>(istream & ,自定义类&); //只可定义友元函数，普通函数，不能是类函数，因参数，返回值都是istream不是本类
//>>输入　,  <<输出，以从左到右>>


Complex c1(3.5);
c1+Complex(2.5)//进行+重载，Complex(2.5)无名，以对象数组一样

类中
operator double ()  //把 对象　转换成double
{
	return deal;//类中定义属性float deal;
}


ambignous　不明确的

多继承时 class C:public A,public B,两个父类有同多方法时使用 c.A::display();

class A: protected B //只会把父类属性，方法变小(public ->protected)不会变大，同名的可以覆盖,和Java 相反
{
};


Student1::Student1(int n,int a):Student(n)//只可定义时，声明时不包括父类
{

}
Student1::Student1(int n,int a):Student(n),age(a){}//age是子类Student1的属性，先是父类，再子类


class B:virtual public A
构造 B(int n):A(n)
class C:virtual public A
构造 C(int n):A(n)
class D:public A,C //Ｄ中只有一份Ａ属性和函数,所有从Ａ继承要都要加virtual
构造 D(int n):A(n),B(n),C(n);

父类函数前加 virtual 可以多态，当一个成员函数是virtual子类的同名函数都是virtual

delete 时不会执行父类的析构函数,父类的析构函数声明为 virtual 就可以

virtual float area()=0; //是纯虚函数，子类中也是纯虚的，不能被调用，有纯虚的函数的类是抽象类，不能建立对象




class Base
{
	static const int b;
};
const int Base::b=15;  //加int初始化const

void test(Base * p,Base::*f)指针只可指向类的
{
	(p->*f)();//只能这样写
}


//逗号表达式,Java中是没有逗号表达式的
int a=0,b=0;
b=(a=2*6,a-4,a+15);//只把第一个给a,
printf("%d\n",a);//12
printf("%d\n",b);//27


%6.2f  表示2位小数点,  共6位,如数值超出,实际输出,如小于,在前补空格

变量名不能方法名相同
sum(int k)
{
	static int x=0;//static 一直存在,不会被覆盖
	return (x+=k);
}


unsigned short int 同 unsigned short

struct person
{
char name[10];
}group[3]={{"abc"},{"def"},{"ghi"}};
struct person * p_list=group;
printf("%c%c%c\n",	(*(p_list+1)).name[0],//g
				(*(p_list++)).name[1],//b ,这里的++只影响前面的,不影响后面的
				(*(p_list+1)).name[2]);//f
//结果是gbf  , C 语言的函数调用是从右向左压入栈
//最后一个参数计算值 放入函数栈,第二个参数指针的改变不会影响最后一个参数的值

++ ,--只能对变量,不能对表达式使用,如不能(-a)++;
全局int变量未初始化默认0,局部变量不是的

-----


try
{
	...可能throw a;//a=1
}
catch (double)
{
}catch (int)
{
}catch (...) //　表示其它任何的
{
	throw ;
}


try
{
	
}
catch (exception& e)
{
	cerr << "exception caught: " << e.what() << endl;
}

//double triangle(double x)throw(int,double)//,c++17 不能在throw()里面加int等(可能是int,也能是double)
double triangle(double x) throw()//这个函数不能throw出来


namespace Televesion
{
}
namespace TV=Televesion;//别名

using TV::Student;

//printf的原型
int printf(const char *format, ...); //...
 
 
 
 
int min(int a,int b)
{
	return a<b?a:b;
}
int (*returnFuncInPointer(int a))(int,int)//返回指向函数的指针
{
	 int (*p) (int ,int);//定义指向函数的针;
	 p=min;
	 return p;
}
//或者用
typedef int (*FuncInPointer) (int ,int);
FuncInPointer  returnFuncInPointer2(int a)
{
	return returnFuncInPointer(22);
}


int (*returnArrayInPointer(int a))[2] //函数的返回值 是指向 int[2] 的指针;
{
	int (*data)[2];//是指针,指向 int[2] 的指针;
	data = (int(*)[2])malloc(2*sizeof(int));//这里是的强转写法(int(*)[2]),无名指针
	(*data)[0] = 1;
	(*data)[1] = 2;
	free(data);//这里为了测试,返回的值不能使用
	return data;
}
//或者用
typedef int(*ArrayInPointer)[2] ;
ArrayInPointer returnArrayInPointer2(int a)//这样可以的
{
	return returnArrayInPointer(1);
}

int * array[5];//是数组,里存放int* 类型的指针
int age=20;
array[0]=&age;

int chars[5]={0,1,2};
int (*pointer)[5];//是指针,指向int[5]的数组
pointer = &chars;
printf("%d",(*pointer)[2]);//2

//--
FuncInPointer arrayCom[5];//是数组,里存放指针向函数的指针
arrayCom[0]=min;
int res=arrayCom[0](7,8);
//方法2
int (* arrayCom2[5]) (int,int);//是数组,里存放指针向函数的指针
arrayCom2[0]=min;
res=arrayCom2[0](7,8);
 
 
 
 
 
//----特殊用法 1
	struct myclass {
	  int operator()(int a) {return a;}
	} myobject;
	int x = myobject (0);

//----特殊用法2
#include <iostream>
using namespace std;
class Tdate
{
	public :
		int month;
		int & getm() //int & 引用，可以Tdate d,d.getm()=3可给函数赋值  只C++
		{
			return month;//中不能有对 month=20;的操作,否则赋值无效,必须返回类的属性
		}
};
int main()
{
	Tdate t;
	t.getm()=4;//赋值
	cout<<t.getm()<<endl;
	cout<<t.month<<endl;
	return 1;
}











int *p ;
*p=9;//很危险，p没有指向，任意值

  
class MyClass
{
public:
   MyClass( int num );
}

MyClass obj = 10; //编译器自动将整型转换为MyClass类对象，实际上等同于
	MyClass temp(10);
	MyClass obj = temp;

避免这种自动转换的功能,构造函数前加 explicit(清楚的),
	  
MyClass obj = 10;//就失败
 

//如父类有一个纯虚函数,virtual int xx()=0;子类继承如不覆盖它,仍为纯虚函数,不能创建对象

typeof(found) a;//声明一个变量a，和found是同一个类型
vector<string> collection;
cout<<"typeid的值:"<<typeid(collection).name()<<endl;//要 #include <typeinfo>
cout << typeid(int).name() << endl;
cout << typeid(a).name() << endl;

set_unexpected (myunexpected);//某个函数出现异常，而该异常未被列到异常列表，则myunexpected函数被系统自动调用。
	

============C标准库
http://www.sgi.com/tech/stl/    Standard Template Library C库的在线参考手册
http://www.cplusplus.com/reference/ 有STL  容器,算法,流,string,标准C库(多),有示例代码
http://msdn.microsoft.com/zh-cn/library/hh875057

#include <stdlib.h>
#include<stdio.h>
system("ps aux &"); 执行系统命令 启动进程 ,要一个SHELL
exit(0);


#include <stdio.h> 
#include <iostream> 
#include <time.h> 
using namespace std; 
#define MAX 100 
int main(int argc, char* argv[]) 
{ 
srand( (unsigned)time( NULL ) );//srand()函数产生一个以当前时间开始的随机种子 
for (int i=0;i<10;i++) 
cout<<rand()%MAX<<endl;//MAX为最大值，其随机域为0~MAX-1 
return 0; 
}

-------C 文件读写
FILE* fp=fopen("c:/my.txt","w");//如出错返回NULL
	r(read): 读
	w(write): 写 ,存在覆盖
	a(append): 追加 ,文件不存在则建立

	r+ 		读写打开存在文件
	w+ 		读写建立文件,存在覆盖
	a+ 		文件不存在则建立,写只在尾,但可fseek,rewind到其它位置去读

	t(text): 文本文件，可省略不写
	b(banary): 二进制文件

flush(fp);//
FILENAME_MAX //文件名最长为 
FOPEN_MAX //一次最多打开文件数

FILE *p=freopen("c:/my.txt","w",FILE *);//一般用于stdin,stdout,stderr的重定向

错误返回EOF
fclose(fp);
remove("c:/my.txt");//删除文件
rename("c:/my.txt","c:/my_1.txt");//重命名文件
FILE * f=tmpfile();//以wb+方式
char * tmpname(NULL);//返回不同一个文件名字串,内部静态数组的指针
char * tmpname(char s[L_tmpnam]);//存入s中,至少L_tmpnam空间

void setbuf (FILE* stream, char*　buf)
//缓冲区长度 BUFSIZ 缺省值为512字节,如buf为NULL不带缓冲

int setvbuf(FILE * stream,char * buf,int mode ,size_t size);
//_IOFBF (full),_IOLBF(line),_IONBF(not)
//打开文件后,用户可建立自己的文件缓冲区,而不使用fopen()函数打开文件设定的默认缓冲区,用malloc

vprintf,vfprintf,vsprintf 由va_start初初始化,由va_arg调用
#include <sdtarg.h.

fscanf,scanf

fgetc,fgets,fputc,fputs
int getc(FIFLE *);
int getchar();同 getc(stdin);
gets
putc,putchar,puts


int ungetc(int c,FILE * stream)//把字符 c 写到stream中,下次读时返回c,不能写EOF,如错误返EOF

size_t fread(void *data,size_t size size_t n,FIEL* stream)
从stream中最多读n个长度为size的对象到data中,返回读入数
size_t fwrite(void * data,size_t size ,size_t n,FIEL * stream);

fseek(FILE* p,long offset,int origin)//SEEK_SET,SEEK_CUR当前位置,SEEK_END
ftell(FILE* )//返回当前位置,出错返回-1
int fgetpos(FIEL* ,fpos_t * p);//存位置到p,出错返回非0值
fsetpos(FIEL* ,const fpos_t * p);


feof()
-------

 
getchar,putchar,

strcat(char[],const char []);
strcpy(char[],const char []);
strcmp() //第一个参数 大于第二个参数 返回1 
strlen()

char str[1024];
gets(str);//字串
strstr(str,"child");//找child在str中第一次出现的指针位置


#include <string.h>
char *strcpy(char *dest,char *src);//返回*dest ,把src复制到dest

,strncpy,strcat,strncat,strcmp,strncmp,
char * strchr(char * cs, int c) ;//返回字串cs中c最后一次出现的位置指针,没有NULL

strpbrk(const char* cs , const char* ct);
//返回 字符串ct中任意字符第一次出现在字符串cs中位置指针
strstr(const char* cs , const char* ct);
//返回 字符串ct中第一次出现在字符串cs中位置指针


char *str="Linux was first";
char *t1="Air";//in
printf("第一次出现包含子串中的字符的位置%d\n",strcspn(str,t1));//1,split n,

char *string1 = "1234567890";
char *string2 = "A123DC8";
int length=strspn(string1, string2);//3
printf("第一次出现与字串不相同的索引:%d\n", length);


char input[16] = "abc,d~123,tt";
char *p;
p = strtok(input, ",~");//string token
while(p)
{
	printf("以,分隔的:%s\n", p);
	p = strtok(NULL, ",~");//第二次开始要设置为NULL,才会对第一次的值做多次
}



strlen
memcpy,memmove,memcmp,
memchr (cons void * cs,int c, size_t n)//返回c在cs中第一次出现的位置指针,如果前n个找不到,返回NULL
memset


 
char a[5];
memset(a,'w',5);//将a里的值全部变成'w'
 
int  a[5];
如果用memset(a,1,20);
就是对a指向的内存的20个字节进行赋值，每个都用ASCII为1的字符去填充，转为二进制后，1就是00000001,占一个字节。一个INT元素是4字节，
合一起就是 1 00000001 00000001 00000001，就等于16843009，就完成了对一个INT元素的赋值了。

memcpy(void *dest, void *src, unsigned int count);
由src所指内存区域复制count个字节到dest所指内存区域。(string.h)
 
C 中的标准库函数 malloc、free、calloc 和 realloc//free释放
malloc、calloc、free、memcpy 和 memset 
 
realloc 如果是扩大内存分配的话，成功的话还可以保证原来那部分大小的内存不被修改。如果是减少内存分配的话，成功的话也可以保证调整后的那部分大小的内存跟原来的内容一样。
		先使用一个临时指针保存重新分配的结果，并且检查是否NULL，然后如果没问题再交给原来的指针。


// htons的就是host to net short的意思，这个函数就是把以电脑上数据存储顺序的无符号短整型数转换成以网络上的数据存储顺序的短整型数。 
//原因是intel cpu上的无符号短整型数1000（0X03E8）在内存中的字节存储顺序为0xe8,0x03，但是网络上传输的字节顺序是0x03,0xe8。 
//htons一般是转换端口号中的整数的


//htonl()
// ntohs()  和 ntohl()
atoi(将字符串转换成整型数)//ascii to int
strtol(将字符串转换成长整型数)
返向转用sprintf

对 send() 和 recv() 的调用在默认的情况下都是阻塞的
perror("");//const char*
fprintf(stdout, "\n");

//------assert.h
int * myInt = NULL;
assert (myInt==NULL);
//------ctype.h  c是char的缩写
isalnum()//是否为字母数字
isalpha()//是否为字母
iscntrl()//是否为控制字符, \n为控制字符
isdigit()//是否为数字

//isgraph(c);//是否为可正常显示的字符
//islower是否是小写
//isprint 是否可见的
//ispunct 是否为标点
//isspace是否为空白
//isupper是否为大写字母
//isxdigit是否为16进制数
//tolower,toupper转换大小写
//------errno.h
//cerrno 当程序错误时,这个变量会被赋值
//------float.h 不知道做什么用????

//------iso646.h 可以不包含,默认是加入的像stdlib.h
//	  macro	operator//定入以下的宏
//	  and		&&
//	  and_eq	&=
//	  bitand	&
//	  bitor		|
//	  compl		~
//	  not		!
//	  not_eq	!=
//	  or		||
//	  or_eq		|=
//	  xor		^
//	  xor_eq	^=

 int a=1;
 if (a==1 bitor a++==5) //or
	 printf("成立a=%d\n",a);
  else
	 printf("不成立a=%d\n",a);

//------ limits.h
 int max =INT_MAX;
 printf("%d\n",max);
 max++;
 printf("溢出%d\n",max);
 
//------ locale.h
time_t rawtime;
struct tm * timeinfo;

time ( &rawtime );//把当前的时间截写入
timeinfo = localtime ( &rawtime );//转为当地时间,可以读年月日等

printf ("Locale is: %s\n", setlocale(LC_ALL,"") );//"", 环境默认的locale,"C"是最小的locale

printf ("Locale is: %s\n", setlocale(LC_ALL,"") );//"", 环境默认的locale,"C"是最小的locale
struct lconv * lc;
lc = localeconv ();
printf ("Currency symbol is: %s\n-\n",lc->currency_symbol);//货币符号
//------time.h
char buffer [80];
strftime (buffer,80,"%Y-%m-%d %H:%M:%S , %Z ",timeinfo);//把日期转字串,%Y-%m-%d %H:%M:%S  ,%X表示%H:%M:%S ,%Z 为CST或China Standard Time
 
int t=clock();
printf ("从程序启动到现在花费了%d clicks ,%f 秒\n",t,((float)t)/CLOCKS_PER_SEC);

double dif = difftime (end,start);//计算两个时间点有多少秒

void makeTime()
{
	time_t timestamp;
	struct tm * t_info;
	time  ( &timestamp );
	t_info = localtime ( &timestamp );
	t_info->tm_year = 2012 - 1900;//从1900 年开始
	t_info->tm_mon = 9 - 1;//值的范围0-11
	t_info->tm_mday = 32;
	time_t x=  mktime( t_info );//会忽略 tm_wday 和 tm_yday 并设置成正确的值 ,其它属性没设置范围的限制,像tm_mday没以31天或更大
					  //功能和localtime 反的
	printf ("星期%d", t_info->tm_wday);//值的范围0-6,0是星期日

}
			
time_t timestamp;
time ( &timestamp );
struct tm * tm_now = gmtime ( &timestamp );//转成GMT时间
printf ("GMT 北京+8 时间:  %02d:%02d\n", (tm_now->tm_hour + 8)%24, tm_now->tm_min);//%02d可显示00,而不是一个0

 
//------ math.h
printf("%f\n",exp(3));//e的3次方
printf("%f\n",pow(10.0,3));//10的3次方
printf("%e\n",101.3);//%e 科学计算法输出
printf("%d\n",abs(-8.6));//%e 科学计算法输出
printf("%f\n",ceil(-8.6));//ceil 向大了取
printf("%f\n",ceil(8.6));
printf("%f\n",floor(-8.6));//floor 向小了取
printf("%f\n",floor(8.6));
printf("%f\n",sqrt(64));//开平方根
printf("%f\n",log(30));//以e为底的对数
printf("%f\n",log10(100));//以10为底的对数
printf("%f\n",sin(M_PI/2));//sin,Pai=3.14

//------ setjmp.h 是C中的异常处理机制,不能很好地支持C++中面向对象的语义(VC++),使用try{}catch代替
//在longjmp被调用执行的那个函数作用域中，绝对不能够存在局部变量形式的对象(也即在堆栈中的对象，longjmp执行时还没有被析构销毁)，否则这些对象将得不到析构的机会
jmp_buf env;
int val=0;
val=setjmp(env); //如果被修改可以表示有异常
printf ("val is %d\n",val);//会执行两次
if (!val)
  longjmp(env, 1);//会跳到 setjmp 处,把1做为返回值 给val ,longjmp 到 setjmp中不能有局部变量
  
//------signal.h
void terminate (int param)
{
	if(SIGINT==param)
		printf ("Get Signal NO:%d\n",param);
}
void (*prev_fn)(int);
prev_fn = signal (SIGINT,terminate);//注册发到信号时的处理函数，函数声明头 void xxx(int param)
//处理函数如是 SIG_DFL,表示使用默认,SIG_IGN 表示忽略
if(prev_fn== SIG_ERR)
{
	printf("errno:%d",errno );
}
raise(SIGINT);//发出信号，MinGW ,VC测试 OK

//------stdarg.h
int testarg(const wchar_t * cstrParams, ...)//chart*
{
	int count, i;
	va_list marker;
	count = wcslen(cstrParams);//strlen
	va_start( marker, cstrParams);//信息存到va_list,表示cstrParams参数后面的做为动态参数
	for (i = 0; i < count; i++)
	{
		const char * cstrStrParam;
		int nNumberParam;
		switch (cstrParams[i])
		{
            case 'I':
                nNumberParam = va_arg( marker, int);//va_arg 是会va_list中存入按 int 顺序取
                wprintf(L"Param %d: int type, value = %d\n", i, nNumberParam);//wprintf(L
                break;
            case 'S':
                cstrStrParam = va_arg( marker, const wchar_t *);//char*
                wprintf(L"Param %d: string type, value = '%ls'\n", i, cstrStrParam);//%s
                break;
		}
	}
	va_end( marker);//释放va_list内存
	return 0;
}
testarg(L"ISI", 3,  L"3 & 4",4);//L

//------stddef.h
	struct mystruct {
	  char singlechar;
	  char arraymember[10];
	  char anotherchar;
	};
	printf ("offsetof(mystruct,singlechar) is %d\n",offsetof(mystruct,singlechar));//offset of,返回 size_t 表示字段在结构体中的位置
	printf ("offsetof(mystruct,arraymember) is %d\n",offsetof(mystruct,mystruct::arraymember));//1,写法多样
	printf ("offsetof(mystruct,anotherchar) is %d\n",offsetof(mystruct,anotherchar));//11

	//两个指针减法操作的结果是  ptrdiff_t,有可能是负数
	int buff[4];
	ptrdiff_t diff = (&buff[3]) - buff; // diff = 3
	printf ("两者之间有%d个字节\n",diff);
	diff = buff -(&buff[3]); //  -3
	printf ("两者之间有%d个字节\n",diff);
	  
//------stdint.h   C++11
//------stdio.h
	FILE * f=tmpfile(); //以wb+ 模式建立一个临时文件,可以保证文件名不是已有的,并打开,在close时删除
	printf ("文件名是: %s\n",f->_tmpfname);
	char * content ="testABC中国";
	int len=strlen(content);
	fwrite((void*)content,1,len,f);
	fclose(f);

	char bufferFile [L_tmpnam];//长度最小值 宏L_tmpnam
	tmpnam (bufferFile);//返回一个文件名
	printf ("带参的临时文件名:%s\n",bufferFile);
	char * pointer = tmpnam (NULL);//如果传NULL使用返回值,下次调用会覆盖这部分区域
	printf ("NULL参的临时文件名: %s\n",pointer);

	 /*
	char * from="d:/temp/abc.tmp";
	char * to="d:/temp/abc123.tmp";
	if(rename(from,to)!=0)  //文件重命名
	{
	  printf (" errno:%d,%s\n",errno,strerror(errno));//显示errno对应的消息
	  //perror("文件重命名 失败原因为--");//最近一次错误的解释,
	}
	if(remove(to))//删除文件
	{
	   printf (" errno:%d,%s\n",errno,strerror(errno));//显示errno对应的消息
	  //perror("删除文件  失败原因为--");
	}

	freopen ("d:/temp/Clog.txt","w",stdout);//先关闭myfile.txt与stdout关联,再建立单向关联,向stdout写操作,改为向文件写,
	printf ("This sentence is redirected to a file.");
	printf ("#------文件中的内容,为后面测试用.");
	fclose (stdout);

	FILE* openFile=fopen("d:/temp/open.txt","r");
	FILE* reopenFile=freopen ("d:/temp/reopen.txt","r",openFile); //从openFile中读 修改为 从reopen.txt中读 , r和w混用无效果
	if(openFile!=NULL || openFile!=NULL)
	{
		char text[20];
		fread((void*)text,1,20,openFile);
		printf ("读到的是:%s",text);
		fclose (openFile);
		fclose (reopenFile);
	}

	//void setbuf ( FILE * stream, char * buffer );//full,为文件指定缓冲区,如为NULL,则禁用缓冲,要在open之后,操作之前来调用,必须是数组,最小为 BUFSIZ
	//int setvbuf ( FILE * stream, char * buffer, int mode, size_t size );//line 缓冲, mode指定_IOLBF,如果字符为换行符,清缓存
	setvbuf(stdout,NULL,_IONBF,0);//_IONBF表示不缓存

	//fprintf(),fscanf(),sprinf(),sscanf(),
	//vprintf 使用va_list做参数,使用stdarg.h做处理 ,还有vfprintf,vsprintf
	//%-10.10s : 表示左对齐 (-), 最小字符数(10), 最大字符数(.10),  (s) .
	printf("%-*s", 10, "abc");// 表示输出字符串左对齐输出10，如果字符串不够20个，以空格补齐
	puts("请输入一串字符\n");

	char ch= getc(stdin);//fgetc
	putc(ch,stdout);//fputc 可能EOF返回

	char input[1024];
	gets(input);//从stdin中读一行(\n结束)字串存到str,NULL,可用feof,ferror
	puts(input);//向stdout中输出
	//对应的有fgetc,fgets
	puts("请输入一个字符\n");
	ch=getchar();//从stdin
	putchar(ch);//向stdout

	*/
	FILE * pFile;
	int c;
	char buf [255];
	pFile = fopen ("d:/temp/myfile.txt","rt");
	if (pFile==NULL) perror ("Error opening file");
	else {
		while (!feof (pFile))
		{
		  c=getc (pFile);
		  if (c == '#')
			ungetc ('@',pFile); //把@放回内存中当前位置(对前面已经getc),下次就可以读到,并不会修改文件
		  else
			ungetc (c,pFile);
		  fgets (buf,255,pFile);
		  fputs (buf,stdout);
		  memset(buf,0,255);

		}
	}
//-------------------------------string.h
	char test[] = "memmove can be very useful......";
	puts (test+20);
	puts (test+15);
	memmove (test+20,test+15,11);//是复制不会把源删除，内存重叠是可以的，memcpy　内存不能重叠
	puts (test);

	char str2[5];
	memset(str2,'x',sizeof(str2));
	strncpy (str2,test,4); //strncpy多一个长度参数,要手动加'\0'
	str2[4]='\0';
	puts (str2);

	char str3[10];
	memset(str3,'x',sizeof(str3));
	char m[3];
	memset(m,'x',sizeof(m));
	m[0]='a';
	m[1]='b';
	m[2]='\0';

	strcpy (str3,"this ");//strcpy会把'\0'也复制,
	strcat (str3,m);//strcat　把第二个字串符加到第一个字串后面，会把第一个串'\0'覆盖,第二个串要有'\0'
	puts (str3);

	char m1[3];
	memset(m1,'x',sizeof(m1));
	m1[0]='u';
	m1[1]='v';
	strncat (str3,m1,2);// strncat 多一个长度参数,会自动加'\0'
	puts (str3);

	setlocale(LC_ALL,"chs");
	strcoll("噢","啊");//使用 setlocale 比较大小,即中文的汉语拼音
	//还有memcmp,strcmp,strncmp


	//size_t strxfrm ( char * destination, const char * source, size_t num );//setlocale,transform and copy
	//可传destination为NULL,num为0,只要返回的汉字的长度
	setlocale(LC_ALL,"C");
	//char* source = "1234567890 abc";
	char* source = "中国";
	char des[100];
	len = strxfrm(des, source, 50); //和strcpy很像,有什么特别的用途吗??
	printf("len:%d\n",len);
	printf("des:%s\n",des);

	//---
	char * pch;
	char str_chr [] = "Example string";
	pch = (char*) memchr (str_chr, 'p', strlen(str_chr));//和bsearch很像
	if (pch!=NULL)
		printf ("'p' found at position %d.\n", pch- str_chr +1);
	else
		printf ("'p' not found.\n");
	//---

	char str_tosearch[] = "This is a sample string";
	char * pch_found;
	pch_found=strchr(str_tosearch,'s');//chr=character 在内存中搜索char,返回找到的所在内存地址
	while (pch_found!=NULL)
	{
		printf ("found at %d\n",pch_found - str_tosearch+1);
		pch_found=strchr(pch_found+1,'s');
	}
	//---
	pch_found=strrchr(str_tosearch,'s'); //只找最后一次现char的地址
	printf ("Last occurence of 's' found at %d \n",pch_found - str_tosearch +1);
	//--
	char key[] = "aeiou";
	printf ("Vowels in '%s': ",str_tosearch);
	pch_found = strpbrk (str_tosearch, key);//和strchr 一样只是可以传多个char
	while (pch_found != NULL)
	{
		printf ("%c " , *pch_found);
		pch_found = strpbrk (pch_found+1,key);
	}
	//--
	printf("\n");
	pch_found = strstr (str_tosearch,"sample");//substring 子串的地址
	strncpy (pch_found,"simple",6);//修改字串
	puts (str_tosearch);
//------stdlib.h
	float val=atof ("2.3");//ascii to float
	printf("atof 结果%f\n",val*2);
	//对应有atoi,atol,

	char * pEnd;
	double d1 = strtod ("12.3def",&pEnd);//第二个参数返回解析错误的字串
	printf("strtod 结果%f,终止字串%s\n",d1 * 2,pEnd);
	//对应的有strtol,strtoul

	int r=0;
	for(int i=0;i<3;i++)
	{
		r=rand();
		printf("10-100的随机数%d\n" ,r%10+90);//10-100
	}

	//calloc和malloc,参数不同
	printf ("操作系统环境变量path: %s\n",getenv ("PATH"));//操作系统环境变量
	i=system ("dir");//执行操作系统命令,返回值0表示执行成功
	printf ("The value returned was: %d.\n",i);

	div_t divresult;
	divresult = div (38,5);//同样的还有ldiv(long div)
	printf ("38 div 5 => %d, remainder %d.\n", divresult.quot, divresult.rem);

	long res=labs(-23423l);//long abs
	printf ("labs:%d\n",res);


//----stdlib 中文处理 mblen mbtowc wctomb
     	
	setlocale (LC_ALL, "chs");  //设置为简体中文环境
	//setlocale (LC_ALL, "enu");  //设置为英文环境
	int max=MB_CUR_MAX;//LC_ALL=chs时是2 , LC_ALL=enu时是1
	int count=mblen("中",max);//LC_ALL=chs时是2 , LC_ALL=enu时是1
	printf ("one CNS character lenght: %d\n",count);//1或2
	//---mbstowcs
	wchar_t * wsExpect=L"CHINA";
	char * enStr="CHINA";
	dSize=strlen(enStr);
	wchar_t dBuf[dSize  + 1 ] ;//多一个空间存放终止符
	wmemset(dBuf, 0, dSize + 1);//wmemset,最后一个是\0终止,在 wchar.h文件中
	int nRet=mbstowcs(dBuf, enStr, sSize);
	wprintf(L"char* ENU String, MUST not have CHS->  wchar_t* ,result : %ls\n", dBuf);
	//---wcslen,wcstombs
	wchar_t * wFrom=L"Chinese";
	int wLen=wcslen(wFrom);//7 正常
	char  byte[wLen*2 +1];
	byte[wLen*2 +1]='\0';
	nRet=wcstombs(byte,wFrom,wLen*2);//不可有中文,因为放不下
	printf("wchar_t* ENU String,MUST not have CHS->char* ,result : %s\n", byte);
	
//-------------------------------wchar.h   C++11 C11
	void readFile()
	{
		FILE * pFile;
		wint_t wc;//wint_t类型
		int n = 0;
		pFile=fopen ("c:/temp/c_file.txt","r");//文件名不支持中文
		if (pFile!=NULL)
		{
		do {
			  wc = fgetwc (pFile);//fgetwc函数
			  if (wc == L'$') //加L
				  n++;
			  wprintf (L"%lc",wc);//是%lc
		}while(wc != WEOF);//WEOF
		fclose (pFile);
		wprintf (L"\nThe file contains %d dollar sign characters ($).\n",n);//wprintf函数
		}
	
	}
	 
	//----------wchar_t 与　数　转换
	wchar_t result[20];
	swprintf(result,L"%ld",-123.33); 
	swprintf(result,20,L"%ld",-123.33); 
	wprintf (L"number  ->　wchar_t* ,use  swpritnf ,result:%ls \n",result);

	double num=wcstod(L"-456.33",NULL);//wchar_t ->  double数字
	wprintf (L"wchar_t* -> double: %lf\n",num);

	long int res=wcstol(L"123",NULL,10);//10进制
	wprintf (L"wchar_t* -> long1: %ld\n",res);

	wchar_t data[10]={L"456"};
	res=wcstol(data,NULL,10);
	wprintf (L"wchar_t* -> long2: %ld\n",res);

	//------单个 btowc
	wchar_t w=btowc('c');//byte to wchar,要当于L,是动态的
	wprintf (L"char ->wchar_t : %lc\n",w);

	
	
	//-----mbsrtowcs      char* 中文  ->  wchar_t *的转换
	//const char *str      = "Hello世界";
	const char *str        = "世界Hello";//9+1
	int str_len=strlen(str);//vc是9，MinGW是11(一个汉字3个)
	wchar_t    *wstr = (wchar_t *)calloc(str_len, sizeof(wchar_t));	//分配空间
	//wchar_t wstr[str_len];
	mbstate_t  state1;
	memset(&state1, 0, sizeof(state1));
	int rtn=mbsrtowcs(wstr, &str, str_len, &state1);//多个r,restartable
	printf("char* CNS  -> wchar_t* : %ls\n", wstr);
	free(wstr);

	//-----wcsrtombs  wchar_t*必须英文 ->  char*
	char to[50];
	const wchar_t* from=L"Chinese";
	mbstate_t state2;
	rtn=wcsrtombs(to,&from,50,&state2);//不支持中文,必须英文
	printf("wchar_t* MUST ENU  ->  char*  : %s\n", to);
 
//-------------------------------wctype.h   C++11 C11
	int result=iswalnum(L'a');//wal=world alpha
	result=iswalnum(L'中');
	result=iswdigit(L'3');
	result=iswdigit(L'A');//a-f或者A-F,十六进制
	//以上返回0表示假,正数表示真

	 int i=0;
	 const wchar_t *str = L"Test String.\n";//vc一定要加const
	  wchar_t c;
	  wctype_t check = wctype("lower");
	  /*
	  "alnum"	alphanumerical character	iswalnum
	  "alpha"	letter character	iswalpha
	  "blank"	blank character	iswblank
	  "cntrl"	control character	iswcntrl
	  "digit"	decimal digit character	iswdigit
	  "graph"	character with graphical representation	iswgraph
	  "lower"	lowercase letter character	iswlower
	  "print"	printable character	iswprint
	  "punct"	punctuation character	iswpunct
	  "space"	white-space character	iswspace
	  "upper"	uppercase letter character	iswupper
	  "xdigit"	hexadecimal digit character	iswxdigit
	  */

	  wctrans_t trans = wctrans("toupper");//只可传tolower和toupper,同towlower和	towupper
	  while (str[i])
	  {
		c = str[i];
		if (iswctype(c,check))
			c = towctrans(c,trans);
		putwchar (c);
		i++;
	  }

------------------C++ 流

cerror 可输出到显示器上
clog　不经缓冲区的,输出到显示器上

cin.get();
cin.getline();
cin.eof();
cin.peek();
cin.putback();
cin.ignore();

ASCII文件
#include <fstream>
ofstream outfile("1.dat",ios::out);
outfile.open();
outfile.close();

二进制文件
read();
write();

gcount();
istream tellg();//返回输入文件指针	//g get
istream seekg();//移动输入文件指针

ostream tellp();//返回输出文件指针  //p put
ostream seekp();//移动输出文件指针


#include <strstream>
strstream
 
 iomanip 格式输出头文件
#include <iomanip>

double f =3.14159;
cout << setprecision (0) << f << endl;
cout << fixed;
cout << setprecision (9) << f << endl;

3.14159
3.141590000
 
cout << setw(10) << 100 << endl; 输出宽度为10，默认右对齐

int readWriteTextFile()
{
	//------------写 文件文本
	ofstream outfile;
	outfile.open("d:/temp/f1.dat",ios::out|ios::binary|ios::trunc);//ios::trunc覆盖和建立,ios::app,
	if(!outfile)//失败返回0
	{
		cerr<<"文件打开失败"<<endl;
		return -1;
	}

	outfile<<"这是使用C++写的文件内容 \r\n 第二"<<endl;
	outfile<<"第三行"<<endl;
	outfile.put('Z');

	string cpp_data="write方法";//是C++语言的方式 string
	int len=cpp_data.length();
	const char * data =cpp_data.c_str();//带'\0'
	//const char* data =cpp_data.data();//不带'\0'

	//char* data ="write方法";
	//int len=strlen(data);//是C语言的方式,对于使用了using namespace,要在里面#include


	outfile.close();
	//------------按行读文本文件
		ifstream file1{"d:/temp/f1.dat"};
		vector<string> lines{};
		string line{};
		while(getline(file1,line)){
			cout<<line<<endl;
			lines.push_back(line);
		}
		file1.close();
	//------------读文本 文件

	ifstream infile;
	infile.open("d:/temp/f1.dat",ios::in);

	char * str=new char[50];
	infile>>str; //以endl结束
	cout<<"从文件中读到的内容:"<<str<<endl;
	infile>>str;
	cout<<"2:"<<str<<endl;
	infile>>str;
	cout<<"3:"<<str<<endl;

	delete[] str;

	char x;
	infile.get(x);
	cout<<"rn:="<<x<<endl;
	infile.get(x);
	cout<<"z:="<<x<<endl;

	//len++;//为'\0'
	while(infile.get(x))//有单字节,有双字节
	{
		if(--len<0)
			break;
		cout<<"o:="<<x<<endl;
	}
	infile.close();

	return 1;
}

int readWriteBinaryFile()
{
	//------------写 二进制文本
	ofstream outfile;
	outfile.open("d:/temp/f1.dat",ios::out|ios::binary|ios::trunc);//ios::trunc覆盖和建立,ios::app,
	if(!outfile)//失败返回0
	{
		cerr<<"文件打开失败"<<endl;
		return -1;
	}
	struct Student{//可无名
		int id;
		char name[10];
		char sex;
	}lisi[2]={{100,"李四",'T'},{101,"李四2",'F'}};
	outfile.write((char*)&lisi[0],sizeof(lisi[0]));
	outfile.write((char*)&lisi[1],sizeof(lisi[1]));
	outfile.close();

	//------------读二进制 文件

	ifstream infile;
	infile.open("d:/temp/f1.dat",ios::in);
	Student readData;

	infile.read((char*)&readData,sizeof(Student));
	cout<<readData.id<<":"<<readData.name<<":"<<readData.sex<<endl;
	infile.read((char*)&readData,sizeof(Student));
	cout<<readData.id<<":"<<readData.name<<":"<<readData.sex<<endl;

	int size=(sizeof(Student));
	infile.seekg(-size,ios::cur);//还有ios::beg,ios::end
	int current=infile.tellg();
	cout<<"current:"<<current<<endl;
	infile.read((char*)&readData,sizeof(Student));
	cout<<readData.id<<":"<<readData.name<<":"<<readData.sex<<endl;

	infile.close();


	return 1;
} 
 
  
------------C++ 其它
http://www.cplusplus.com/reference/std/





------------C++ STL container
#include<vector>
#include <list>

vector<int> the_vector;
vector<int>::iterator the_iterator;
for (int i=0; i < 10; i++)
the_vector.push_back(i);//向尾部加
for (the_iterator = the_vector.begin();the_iterator != the_vector.end();the_iterator++ )
{
	cout <<*the_iterator << endl;
}
the_vector.assign(8,20);//修改vector长度为8,值全是20
cout <<the_vector[0] << endl;//20
cout <<the_vector[7] << endl;//20

vector<int> the_vector2;
the_vector2.assign(the_vector.begin(),the_vector.end());//复制到the_vector2中



vector< vector<int> > Array(10);//第一维必须给初始大小,二维大小可不给,也可给
vector< vector<int> > Array( 10, vector<int>(0) ); //


vector< vector<int> > Array;
vector< int > line;
Array.push_back( line );
Array.push_back( line );  //两次push_back并不是一个对象

 
 
Lists将元素按顺序储存在链表中. 与 向量(vectors)相比, 它允许快速的插入和删除，但是随机访问却比较慢. 

assign() 给list赋值 
back() 返回最后一个元素 
begin() 返回指向第一个元素的迭代器 
clear() 删除所有元素 
empty() 如果list是空的则返回true 
end() 返回末尾的迭代器 
erase() 删除一个元素 
front() 返回第一个元素 
get_allocator() 返回list的配置器 
insert() 插入一个元素到list中 
max_size() 返回list能容纳的最大元素数量 
merge() 合并两个list 
pop_back() 删除最后一个元素 
pop_front() 删除第一个元素 
push_back() 在list的末尾添加一个元素 
push_front() 在list的头部添加一个元素 
rbegin() 返回指向第一个元素的逆向迭代器 
remove() 从list删除元素 
remove_if() 按指定条件删除元素 
rend() 指向list末尾的逆向迭代器 
resize() 改变list的大小 
reverse() 把list的元素倒转 
size() 返回list中的元素个数 
sort() 给list排序 
splice() 合并两个list 
swap() 交换两个list 
unique() 删除list中重复的元素 
 
 
vector<int> the_vector; //是数组
vector<int>::iterator the_iterator;
for (int i=3; i < 9; i++)
	the_vector.push_back(i);//向尾部加
the_iterator = the_vector.begin();
cout << "first:" << *the_iterator << endl;
for (the_iterator = the_vector.begin();the_iterator != the_vector.end();the_iterator++ )
{
	cout <<*the_iterator << endl;
}
cout <<"数组 vector的size:"<<the_vector.size()<< endl;
cout <<"数组 vector的max_size："<<the_vector.max_size()<< endl;

cout <<"数组 vector的front："<<the_vector.front()<< endl;//取得第一个
cout <<"vector的back："<<the_vector.back()<< endl;//取最后一个元素

cout <<"vector的end-1："<<*(the_vector.end()-1)<< endl;//iterator到未尾

//		cout <<"vector的empty："<<the_vector.empty()<< endl;//是否为空
//		the_vector.clear();//清空

vector<int> the_vector2;
the_vector2.assign(the_vector.begin(),the_vector.end());//the_vector复制到the_vector2中
cout <<"assign复制后的vector2[1]的值："<<the_vector2[1] << endl;

the_vector2.assign(8,20);//修改vector长度为8,值全是20
cout <<"assign修改后"<<the_vector2[0] << endl;//20
cout <<"assign修改后"<<the_vector2[7] << endl;//20
the_vector.insert(the_vector.begin()+2 ,33);
cout <<"assign insert 33后"<<the_vector[2] << endl;

cout <<"assign insertthe_vector2后"  << endl;
the_vector.insert(the_vector.begin()+2,the_vector2.begin(),the_vector2.end());
for (the_iterator = the_vector.begin();the_iterator != the_vector.end();the_iterator++ )
{
	cout <<*the_iterator << endl;
}
//the_vector.pop_back();//从后删
vector< vector<int> > Array(10);//第一维必须给初始大小,二维大小可不给,也可给


//--------C++ list 和vector区别
vector<int> v;
list<int> l;
for (int i=0; i<8; i++)     //往v和l中分别添加元素
{
	v.push_back(i);
	l.push_back(i);
}

cout << "v[2] = " << v[2] << endl;
//cout << "l[2] = " << l[2] << endl;       //编译错误,list没有重载[]
cout << (v.begin() < v.end()) << endl;
//cout << (l.begin() < l.end()) << endl;   //编译错误,list::iterator没有重载<或>
cout << *(v.begin() + 1) << endl;

vector<int>::iterator itv = v.begin();
list<int>::iterator itl = l.begin();
itv = itv + 2;
//itl = itl + 2;                  //编译错误,list::iterator没有重载+
itl++;itl++;                    //list::iterator中重载了++，只能使用++进行迭代访问。
cout << *itv << endl;
cout << *itl << endl;


//--------list vector 删除方法名不同
cout <<"list size"<<l.size()<< endl;
l.remove(2);
cout <<"remove后list size"<<l.size()<< endl;

v.erase(v.begin()+2);


l.push_front(300);//list 双向的链表,只list可从表头加元素,vector没有这方法
cout <<l.front()<<endl;
l.pop_front();//从表头删除一个元素,再调用 front();

//vector有at(i)来仿问元素,其实是基于数组实现

//遍历与list同,使用iterator
int myints[] = {75,23,65,42,13,75};
list<int> mylist (myints,myints+6);//from,to
list<int>::iterator it;
cout << "list 双向的链表  不排序的,可有相同的";
for ( it=mylist.begin() ; it != mylist.end(); it++ )
cout << " " << *it;


//---set 排序的，不允许重复
set<int> myset;
set<int>::iterator set_iter;
set_iter=myset.begin();
myset.insert (set_iter,25);
myset.insert (set_iter,24);

for (int i=1; i<=5; i++)
  myset.insert(i);
int mapints[]= {5,10,15};
myset.insert (mapints,mapints+3);

cout << "排序的没有重复元素的Set  " << *set_iter;
for (set_iter=myset.begin(); set_iter!=myset.end(); set_iter++)
 cout << " " << *set_iter;
 
//--multiset 排序的，允许重复

//---stack,
stack<int> s;
s.push(123);
s.push(456);
while(!s.empty())
{
	cout <<"\n statck 顶:"<<s.top()<< endl;
	s.pop();//选top()再pop
}

//-- map ,
map<char,int> mymap;
map<char,int>::iterator map_iter;
mymap['b'] = 100;
mymap['a'] = 200;
mymap['c'] = 300;

mymap.insert ( pair<char,int>('a',100) );//失败,而不是覆盖
mymap.insert ( pair<char,int>('z',200) );

map_iter = mymap.begin();//end()也是一样的,因是按key排序的
mymap.insert (map_iter, pair<char,int>('x',300));
mymap.insert (map_iter, pair<char,int>('y',400));
cout<<"是排序的 map,有相同key失败   "<<endl;
for ( map_iter=mymap.begin() ; map_iter != mymap.end(); map_iter++ )
cout << (*map_iter).first << " => " << (*map_iter).second << endl;//first是key,second是value,默认按key排序的

cout <<"使用[]仿问"<<mymap['b']<<endl;
cout <<"使用find仿问"<< mymap.find('b')->second<<endl;

//--multimap
multimap<char,int> mymultimap;
multimap<char,int>::iterator multimap_iter;
mymultimap.insert (pair<char,int>('b',20));
mymultimap.insert (pair<char,int>('a',10));
mymultimap.insert (pair<char,int>('b',150));
cout<<"可以有相同的key,也是排序的 multimap   "<<endl;
for ( multimap_iter=mymultimap.begin() ; multimap_iter != mymultimap.end(); multimap_iter++ )
  cout << (*multimap_iter).first << " => " << (*multimap_iter).second << endl;

//--C++11中新加 unordered_map,unordered_multimap,unordered_set,unordered_multiset,forward_list,array
//  http://www.cplusplus.com/reference/stl/unordered_map/


//---queue,队列,FIFO (first in ,first out)
//---deque 双向的

//--bitset
bitset<10> bac (120ul);          // 10是长度,unsigned long

bitset<4> first (string("1001"));
bitset<4> second (string("0011"));
cout << (first^=second) << endl;          // 1010 (XOR,assign)
cout << (first&=second) << endl;          // 0010 (AND,assign)
cout << (first|=second) << endl;
//其它的按位操作<<=,>>=,<<,>>,==,!=,^,&,|,~


bitset<5> mybits (string("01011"));
cout << boolalpha; //把0,1以true,flase显示

cout << "bitset 有1吗" << mybits.any() << endl;
cout << "bitset 没有1吗" << mybits.none() << endl;
cout << "bitset 含有1的个数" << mybits.count() << endl;
cout << "bitset 总数" <<  mybits.size()  <<endl;

cout << "bitset []索引从右边开始?" <<  mybits[0]  <<endl;
cout << "bitset test 索引从右边开始?:" <<  mybits.test(0)  <<endl;
mybits[0]=1;
cout<<mybits<<endl;
cout << "bitset []" <<  mybits[0]  <<endl;

cout << "bitset set:" << mybits.set(0,0) << endl;//返回修改的,第几个,是几,索引,0开始
cout << "bitset test:" <<  mybits.test(0)  <<endl;//读


bitset<5> bits (string("01011"));
cout << "bits 的索引从右边开始?:\n";
for (size_t i=0; i<bits.size(); ++i)
 cout << bits.test(i) << endl;//

bool s123=true;//有bool ,true,false

cout << bits.flip(2) << endl;//只对索引为2的位取反  	,索引从右边开始??  01111
cout << bits.flip() << endl;//同~操作符      			,索引从右边开始??  10000

  

------------C++ STL algorithm
int STL_Algorithm()
{
	int myints[] = {32,71,12,45,26,80,53,33};
	  vector<int> myvector (myints, myints+8);
	  vector<int>::iterator it;
	  // using default comparison (operator <):
	  sort (myvector.begin(), myvector.begin()+4);           //(12 32 45 71)26 80 53 33

	  // using function as comp
	  sort (myvector.begin()+4, myvector.end(), myfunction); // 12 32 45 71(26 33 53 80)

	  // using object as comp
	  sort (myvector.begin(), myvector.end(), myobject);     //(12 26 32 33 45 53 71 80)

	  // print out content:
	  cout << "myvector contains:";
	  for (it=myvector.begin(); it!=myvector.end(); ++it)
		cout << " " << *it;



	  //----stable_sort 会保留相等元素的位序
	  cout <<"stable_sort 会保留相等元素的位序"<< endl;

		double mydoubles[] = {3.14, 1.41, 2.72, 4.67, 1.73, 1.32, 1.62, 2.58};

		vector<double> myvector_2;
		vector<double>::iterator it_2;

		myvector_2.assign(mydoubles,mydoubles+8);//不修改原集合的内容来排序

		cout << "using default comparison:";
		stable_sort (myvector_2.begin(), myvector_2.end());
		for (it_2=myvector_2.begin(); it_2!=myvector_2.end(); ++it_2)
		  cout << " " << *it_2;

		myvector_2.assign(mydoubles,mydoubles+8);

		cout << "\nusing 'compare_as_ints' :";
		stable_sort (myvector_2.begin(), myvector_2.end(), compare_as_ints);
		for (it_2=myvector_2.begin(); it_2!=myvector_2.end(); ++it_2)
		  cout << " " << *it_2;

	   return 1;
}
 
------------
len=sizeof(char *);//VC2012中指针,32位时=4,64位时=8
len=sizeof(int *);//VC2012中指针,32位时=4,64位时=8
也就是说可以通过 sizeof(指针) , 来得到程序是以多少位编译的

==========C11 / C++11 
https://gcc.gnu.org/projects/cxx0x.html
-std=c++11

//-----------友元是模板
template<class W>
class Q
{
  static const int I = 2;
public:
  friend W;
};

struct B
{
  int ar[Q<B>::I];
};

//-----------非静态成员,初始化
struct ABC {
  int i = 42;
} a; // initializes a.i to 42



//----using
// 模板别名
template <class T> using Ptr = T*;
Ptr<int> ip;  // decltype(ip) is int*

//Map的值是变类型的，老的方式
template <typename T>
//typedef map<int,T> mapType;//不能直接这样,必须放在struct里
struct  MyMap{
	typedef map<int,T> mapType;
};
MyMap<int>::mapType map1;
MyMap<double>::mapType map2;

//Map的值是变类型的，新的方式
template <typename T>
using MMap=map<int,T>;


using typeint=int; //using 别名，像typedef
typeint aliseInt=3;

typedef int (*funcptr)(int,int);
using  funptr1=int(*)(int,int);//using可读性好



//Map的值是变类型的，老的方式
template <typename T>
struct  MyMap{
	typedef map<int,T> mapType;
};
MyMap<int>::mapType map1;
MyMap<double>::mapType map2;

//Map的值是变类型的，新的方式
template <typename T>
using MMap=map<int,T>



using Parent::Parent; //就把父类所有的构造函数继承下来



//-----模板默认值
template <class T=long>
void templateDefault(T t=123){
	cout<<t<<endl;
}
templateDefault("123");//如没有指定模板类型，可根据参数推算出为string
	
	
	
//-----------
struct A {
  A(int);
  A(): A(42) { } // delegate to the A(int) constructor
};

 
#include <iostream>
#include <vector>
#include <map>
using namespace std;
  
void func(const vector<int> &vi)  
{  
    // vector<int>::const_iterator ci=vi.begin();  
	auto ci=vi.begin(); 
}
void f(int){
	cout<<"f int"<<endl;
} //#1  
void f(char *){
	cout<<"f char"<<endl;
}//#2  



int main() 
{
	//C++03  
    f(0); //调用的是哪个 f?测试是int
    //C++11  
    f(nullptr); //毫无疑问，调用的是 #2


   std::string s("hello");   
	int m=int();
	std::string s1="hello";  
	int a=5;  

	int arr[4]={0,1,2,3};  
	struct tm today={0};  //tm是一个已经定义的struct

	struct S { 
		int x;  
		S(): x(0){}//构造函数使用成员进行初始化：
	};
 
	
 }

int* b = new int[3] { 1, 2, 0 };//初始化
int array[]{1,2,3};
int array1[]={1,2,3};
int* b1 = new int[] { 1, 2, 0 };
		

//声明了一个二维数组，它的有2列，行数还不确定。
int **a = new int*[2];
//这个二维数组的列数确定了是5；
for (int i = 0; i < 2; i++)
{
	a[i] = new int[5];  //里面每个都要 delete[] a[i];
}
//delete[] a;


 //二维数组的创建8行10列
int(*p) = new int[80];
int(*px)[10] = (int(*)[10])p;
for (int i = 0;i < 8;i++)
{
	for (int j = 0;j < 10;j++)
	{
		px[i][j] = data++;
	}
}
delete []p;
			

				
class Init {
public:
	int num=1;
	Init(int i){

	}
};
//老的功能
Init init3(1);//构造器
Init init4=1;//自动调用 构造器,类型转换
//新的功能
Init init1{1};
Init init2={1};

	
class C 
{  
   int a=7;    //C++11 only  这和Java一样
   //auto x=3.14; //auto不能用于类的非静态成员初始化
   static constexpr auto num=3.14;//必须是static  constexpr
   public:  
	   C(); 
}; 
		
struct A1  
{  
 A1()=default; //C++11  
 virtual ~A1()=default; //C++11  
};  



long long my64;
unsigned long long mypos64;
char16_t cn='中';
char32_t cn32='中';


	
class MyNumber
{
public:
    MyNumber(const std::initializer_list<int> &v) {
		cout<<v.size()<<endl;		
        for (const auto &itm : v) {//新的遍历,复制出一个新的,只在第一次计算出长度,可加引用&，如不想被修改加const
            mVec.push_back(itm);
        }
		//initializer_list的第二种遍历
		for(auto it=v.begin();it!=v.end();++it)//
		{
			cout<<*it<<endl;
		}
    }
  void print() {
    for (auto itm : mVec) {
        std::cout << itm << " ";
    }
	
  }
private:
    std::vector<int> mVec;
};

{ //以前的作法
	int a[] = {0, 1, 2, 3};
	std::vector<int> vec(a, a+sizeof(a));//首尾指针
	//或
	std::vector<int> v;
	v.push_back(1);
}
std::vector<int> v = { 1, 2, 3, 4 };
//initializer_list 初始化列表
MyNumber myNum = { 1, 2, 3, 4 };
myNum.print();  // 1 2 3 4

//class（要无虚函数，无基数）,struct,union, 无私有或保护成员，都可用初始化列表
struct Point{
	int x;
	long y;
	Point(int x){
	}
} //p{200,200}
p2{200};//有构造函数就只能调用构造函数了

struct T1
{
	int x;
	double y;
private:
	int z; //private的不能用初始化列表，
};
struct T2
{
	T1 t1;
	long x1;
	double y1;
};
T2 t2{ {}, 520, 13.14 };// {}表示不做初始化



//int myint{5.0}; //从大double到int不可以
double myint{5};
//int myint3={5.0}; //从大double到int不可以
double myint2={5};
int pi(3.14);//可从大到小

string longstr="<html> \
			</html>\
		";
cout<<longstr.c_str()<<endl;//string用c_str(),显示的前面空格还是有的,输出并没有换行 每行加\来分隔
//R
string path=R"(c:\tmp\my.txt)"; //使用R"(  )"   
cout<<path<<endl; //有没有c_str() 输出\t都是制表符

string bodystr=R"(<body> 
			</body>
		)"; //输出有换行
cout<<bodystr.c_str()<<endl;
path=R"win(c:\tmp\my.txt)win"; //()前后的必须使用相同字符，只做备注使用
cout<<path<<endl;


//	const double const_pi=3.14;
//	constexpr double two_pi=2*const_pi;//报错,编译阶段确定的值，const_pi不能确定，如函数参数
//	int n=2;
//	constexpr double n_pi=n*const_pi;//报错
	constexpr double pi2=3.14*2;
	constexpr double pi3=pi2*2;
	//pi3=33;//不可修改
	
constexpr int sumByNum(int num){
	int res=0;
	for(int i=0;i<num;i++)
		res+=i;
	return res;
}
constexpr int sum=sumByNum(5);//函数也要声明constexpr

volatile int safeVar ;//同java

----auto 

class ClassInt{
public:
	static int get(){
		return 22;
	}
};
class ClassString{
public:
	static string get(){
		return "hello中";
	}
};
template<class T> void printAuto()
{
	auto res=T::get(); //auto动态类型
	cout<<res<<endl;
}	 
template<class T,class U> auto  addAuto(T a,U b) -> decltype(a+b) //返回类型是auto,通过decltype计算出，后加->(返回类型不能是decltype因还不知道模板类型)
{
	 return a+b;
}

auto x=0; //0 是 int 类型，所以 x 也是 int 类型
auto c='a'; //char
auto d=0.5; //double
auto national_debt=14400000000000LL;//long long
auto& ref=d;//ref是d的别名
ref=33;
auto* ptr=&d;
auto  ptr1=&d;

const auto pi =3.14;
auto p2=pi; //不能带const类型，是double类型

volatile int tickets=10;
auto tickets2 =tickets;//不能带volatile类型，是int类型

//函数参数不能是auto
//auto myarray[]={1,3,4};//auto不可初始化数组
 int myarray[]={1,3,4};
 auto youArray=myarray;//已经初始化的可以
//vector<auto> myv=vi; //auto不用于替代模板

 map<int,string> mymap;
 mymap.insert(make_pair(1,"one"));//make_pair
 //map<int,string>::iterator map_iter=mymap.begin() ;//非常长，用auto简单
 auto map_iter=mymap.begin();
 for (; map_iter != mymap.end(); map_iter++ )
	 cout << (*map_iter).first << " => " << (*map_iter).second << endl;

 printAuto<ClassInt>();
 printAuto<ClassString>();

 auto res=addAuto(100,0.8);//double
auto res2=addAuto(100L,(short)20);//long

----decltype

const int& leftValueRef(int a, int b)//左值表示可以取地址的,locator value
{
    return  a < b?a:b;
}
const int&& rightValueRef(int a, int b)//右值,read value，如2，“aabc",即不是变量，不能取地址
{
	 return  2;
}

const vector<int> vi;
typedef decltype (vi.begin()) CIT;   //新的操作符 decltype 可以从一个表达式中“俘获”其结果的类型并“返回”
CIT another_const_iterator;

int var1=1;
int var2=2;
int var3;
decltype(var1+var2) var4; 
decltype(var3=var1+var2) var5=var1;

decltype (leftValueRef(1,2)) myLeft=22;//不会调用函数,类型为 const int&
decltype (rightValueRef(1,2)) myRight=22;//类型为 const int&&

const int mynum=2;
decltype(mynum) mynum2=3;//const int
class Myclass{
public:
	int num;
};
const Myclass my{};
decltype ((my.num)) mynumRef=mynum;//多加一层()为引用类型,如类有const,volatil 也要加上,类型为const int&
 

----lamada
vector<int> myvec{ 3, 2, 5, 7, 3, 2 };
vector<int> lbvec(myvec);
//<algorithm>
sort(lbvec.begin(), lbvec.end(), [](int a, int b) -> bool { return a < b; });   // Lambda表达式 
cout << "lambda expression:" << endl;
for (int it : lbvec)
	cout << it << ' ';

int a = 123;
auto f = [a] { cout << a << endl; };//最前面的方括号[]来明确指明其内部可以访问的外部变量
f(); // 输出：123

//或通过“函数体”后面的‘()’传入参数
[](int a){cout << a << endl;}(123);
{
	int a = 123;
	auto f = [a] { cout << a << endl; };//类似闭包，会保存变量值,无参数可省略()
	a = 321;
	f(); // 输出：123
	
	a = 123;
	auto f1 = [&a] { cout << a << endl; };//引用传递，同函数参数
	a = 321;
	f1(); // 输出：321
	
	//隐式捕获有两种方式，分别是[=] 表示值 和 [&]表示引用
	a = 123;
	auto f3 = [=] { cout << a << endl; };    // 值捕获
	a = 321;
	f3(); // 输出：123

	a = 123;
	auto f4 = [&] { cout << a << endl; };    // 引用捕获
	a = 321;
	f4(); // 输出：321
	
	int b=30;
	[=,&a](int i) mutable { //=,&a表示除了a变量引用，还有this，其它全是值传递
		b++; //mutable才可修改
		cout << a << endl;
	}(10);//传参数
	
	[]()  mutable->  float{ //->可有可无，后面是返回类型,也可省
			 return 1.3f;
		 };

	function<MyStruct(int)> f2 =[] (int i) mutable -> MyStruct{ // 如省 ()-> 报错
		return {i,3};//参数列表时，返回类型不能省
	};
	// 绑定可调用函数
	function<int(int)> ff3 = bind([](int a) {return a; }, placeholders::_1);
	int res=ff3(20);//20传给placeholders::_1 再传给 a,再调用
	
	//对于没有捕获任何变量的 lambda 表达式，还可以转换成一个普通的函数指针：
	using func_ptr = int(*)(int);
	func_ptr fx = [](int a)
	{
		return a;
	};
	
}


//final 关键字放在函数后面，并函数只可是类的，必须是重写了父类的virtual函数，表示不可再被子类重写
//final 关键字放在类后面 表示不可被继承
//override 关键字放在函数后面 表示 重写了父类的virtual函数，可有可无(和final没有先后顺序)，易读

list<list<string>> l;// 其中的 >> ，原来中间必须有空格 "> >",现在可没有空格

----函数包装,bind
using funcptr=int(*)(int,int);
class MyClass{
public :
	int num;
	//类对象转换为函数指针
	operator funcptr(){
		return avg; //必须是static函数
	}
	/*
	//VC报错,有了  operator funcptr() 不可用 operator ()
	//MinGW可以
	operator ()(int a){
		return a+1;
	}
	//VC报错,MinGW可以
	operator ()(int a,string b){
		cout<<b<<endl;
		return a ;
	} 
	 //VC报错,MinGW中 先执行这，再执行 operator funcptr
	operator ()(int a,int b){ 
			cout<<b<<endl;
			return a ;
		}
	*/
	int max(int a,int b){

	}
	static int avg(int a,int b){

	}
};
int myadd(int a,int b){
	return a+b;
}

funcptr ptr0=MyClass::avg;//static的
funcptr ptr1=&MyClass::avg;//&有没有都可
MyClass my;
int res=my(11,22);//类对象转换为函数指针 和 运算符重载，调用哪个？测试下来调用运算符重载
cout<<res<<endl;
res=my(11);//运算符重载
res=my(22,"abc");

using intPtr= int MyClass::*;//指向类的int属性的指针
intPtr p1=&MyClass::num;
my.*p1=22;//属性名，可以是动态的

using classFuncPtr= int (MyClass::*)( int,int);//指向类的函数的指针
classFuncPtr maxPtr = &MyClass::max;
res=(my.*maxPtr)(11,33);
funcptr ptr3=my;//类对象转换为函数指针

//函数包装，#include <functional>
function<int(int,int)> f1  = MyClass::avg;
function<int(int,int)> f2  = ptr3;
function<int(int,int)> f3  = my;
function<int(int,int)> f4= myadd;

//bind
res=bind(myadd,1,3)();//类似JS 的 ECMA 新版本功能
res=bind(myadd,placeholders::_1,20)(10);//placeholders::_1表示第一个参数，使用实参数
res=bind(myadd,placeholders::_1,20)(10,30);//已经绑定了，值不能再修改了，

class Test
{
public:
	Test(const Test& a) : m_num(new int(*a.m_num))
    {
        cout << "copy & construct" << endl;
    }
	Test(Test&& a) : m_num(a.m_num)
    {
    	//只VC调用到？？？
    	a.m_num=nullptr;//两个指向同一地址，防止一个析构回了，另一个不能用
 	    cout << "move && construct" << endl;
    }
	
	void output(int x, int y)
	{
		cout << "x: " << x << ", y: " << y << endl;
	}
	int m_number = 100;
};
Test t;
// 绑定类成员函数
function<void(int, int)> ff1 =
	bind(&Test::output, &t, placeholders::_1, placeholders::_2);//第二个实例要的，后三个是Bind参数
// 绑定类成员变量(公共)
function<int&(void)> ff2 = bind(&Test::m_number, &t);

ff1(520, 1314); //实例函数
ff2() = 2333;//修改属性  

----右值  move,forward

Test getTest(){
	Test t ;
	return t;
}

//lvalue 是 locator value 的缩写，rvalue 是 read value 的缩写
//左值是指存储在内存中、有明确存储地址（可取地址）的数据；
//右值是指可以提供数据值的数据（不可取地址）； 右值引用（ R-value reference），标记为 &&

int && a1 =10;
//int && b=a1;//报错 a有名是左值引用 ，用 move
int && b=move(a1);//对于list就不用复制了

Test   t2 =t1;// 需要拷贝，调用了复制构造函数
Test  t00 = getTest();//GCC因为有赋值没有析构,但VC调用了Test(Test&& a)再调用析构
Test t;
Test x;
//decltype(x) && v1 = t;          // error
decltype(x) && v2 = move(t);    // 转换 左值  到 右值引用,没有复制动作

//常量左值引用是一个万能引用类型
const Test& t33 = Test();
const Test& t44 = x;

{
	//auto && 要推算
	int x = 520, y = 1314;
	auto&& v1 = x;
	auto&& v2 = 250;
	auto&& v3 = v2;
	
	const int& c=10;
	const int&& d=10;
	auto && v4=c;//v4类型为 const int &,只一个&
	auto && v5=d;//v5类型为 const int &,只一个&
			
	//decltype(x)&& v3 = y;   // error,因decltype不用推算
	cout << "v1: " << v1 << ", v2: " << v2 << endl;
}


void testForward(T && v)//当 左值 和 左值引用 传来，变为左值引用
{
	printValue(v); //已经命名，全是左值，即使参数是右值，右值引用
	printValue(move(v));//move转换为右值，全是右值
	printValue(forward<T>(v));//forward模板 当T为左值引用类型时，v将被转换为T类型的左值，其它都是右值
	cout << endl;
}
//T && 要推算	
testForward(forward<int>(num));//右值传过去，forward模板 当T为左值引用类型时，t将被转换为T类型的左值，其它都是右值
testForward(forward<int&>(num));//左值引用传过去
testForward(forward<int&&>(num));//右值传过去，forwar d模板 当T为左值引用类型时，t将被转换为T类型的左值，其它都是右值






-----shared_ptr
#include <memory>
//unique_ptr(计数永远为1) 、shared_ptr(引用计数)、weak_ptr （不会增加计数，监视shared_ptr）三种智能指针

//构造函数初始化
shared_ptr<int> ptr1(new int(520));//int 型的堆内存
cout << "ptr1 引用计数: " << ptr1.use_count() << endl;//1

//数组
shared_ptr<char> ptr2(new char[12]);
cout << "ptr2 " << ptr2.use_count() << endl;//1

int *p = new int;
shared_ptr<int> p1(p);
//shared_ptr<int> p2(p);// error, 编译不会报错, 运行会出错，即同一块内存，不可被两个管理
shared_ptr<int> p2(p1);//另一个指针初始化 
shared_ptr<int> p3 = p1;//赋值初始化

std::shared_ptr<int>  p4 = std::move(p3); //p3转移给p4，另一个功能为转换为右值
// shared_ptr<int> p4(std::move(p3));//写法2
{
	//make_shared 初始化
  shared_ptr<int> ptr1 = make_shared<int>(520);
  shared_ptr<Test> ptr2 = make_shared<Test>();//调用类的构造函数
  shared_ptr<Test> ptr3 = make_shared<Test>(520);//调用类的构造函数的参数
}

shared_ptr<int> ptr5;
ptr5.reset(new int(250));//初始化

ptr2.reset();//取消这个指针的引用

int len = 128;
shared_ptr<char> ptr(new char[len]);
char* add = ptr.get();   // 得到指针的原始地址

// 自定义删除器函数，释放int型内存
void deleteIntPtr(int* p)
{
	delete p;
	cout << "int 型内存被释放了...";
}
shared_ptr<int> pd1(new int(250), deleteIntPtr);//删除函数
shared_ptr<int> pd2(new int[10], [](int* p) {delete[]p; });//删除数组
shared_ptr<int> pd3(new int[10], default_delete<int[]>());//default_delete 删除数组
----unique_ptr
unique_ptr<int> ptr1(new int(10));
//unique_ptr<int> ptr2 = ptr1;// error, 不允许将一个unique_ptr赋值给另一个unique_ptr


//unique_ptr没有use_count()

using func_ptr = void(*)(int*);//删除函数的类型
unique_ptr<int, func_ptr> ptr1(new int(10), [](int*p) {delete p; });
//unique_ptr<int, func_ptr> ptr2(new int(10), [&](int*p) {delete p; });	// error
unique_ptr<int, function<void(int*)>> ptr2(new int(10), [&](int*p) {delete p; });
---weak_ptr
//expired资源是否已经被释放
cout << "2. weak " << (wp5.expired() ? "is" : "is not") << " expired" << endl;

shared_ptr<int> sp1, sp2;
weak_ptr<int> wp;
sp1 = std::make_shared<int>(520);
wp = sp1;
//sp2 = wp.lock();//lock()取监测的 shared_ptr 对象，如新变量产生一个新的计数，2
//sp1 = wp.lock();//计数为1
 

//共享指向this特殊用法
struct Test2 :public enable_shared_from_this<Test2>
{
	shared_ptr<Test2> getSharedPtr()
	{
		//return shared_ptr<Test2>(this);//不能这样用,会析构两次
		return shared_from_this();//配合 继承自  enable_shared_from_this<Test2>使用
	}
	~Test2()
	{
		cout << "class2 Test is disstruct ..." << endl;
	}
}; 
shared_ptr<Test2> sp1(new Test2);
cout << "use_count: " << sp1.use_count() << endl;//1
shared_ptr<Test2> sp2 = sp1->getSharedPtr();
cout << "use_count: " << sp1.use_count() << endl;//2


//循环引用
struct TA;
struct TB;
struct TA
{
	weak_ptr<TB> bptr;//如为shared_ptr,循环引用,内存不能释放
	~TA()
	{
		cout << "class TA is disstruct ..." << endl;
	}
};
struct TB
{
	shared_ptr<TA> aptr;
	~TB()
	{
		cout << "class TB is disstruct ..." << endl;
	}
};
shared_ptr<TA> ap(new TA);
shared_ptr<TB> bp(new TB);
ap->bptr = bp;
bp->aptr = ap;


{ //--头文件 <ratio>
			 ratio<1,1000 > millisecond;// 代表的是 1/1000 秒，也就是 1 毫秒
}
{ //--头文件 <chrono> 慢性的，长期的
   chrono::hours h(1);                          // 一小时
	chrono::duration<int, ratio<1000>> ks(3);    // 3000 秒

	// chrono::duration<int, ratio<1000>> d3(3.5);  // error
	chrono::duration<double> dd(6.6);               // 6.6 秒
	//看源码有 using seconds	= duration<_GLIBCXX_CHRONO_INT64_T>;
 
	// 使用小数表示时钟周期的次数
	chrono::duration<double, std::ratio<1, 30>> hz(3.5);//时钟周期为 1/30 秒，共有 3.5 个时钟周期，所以 hz 表示的时间间隔为 1/30*3.5 秒


	std::chrono::milliseconds ms{3};         // 3 毫秒
	std::chrono::microseconds us = 2*ms;     // 6000 微秒

	//count()周期数
	std::cout <<  "3 ms duration has " << ms.count() << " ticks\n"//MinGW不能Debug
			  <<  "6000 us duration has " << us.count() << " ticks\n"//MinGW不能Debug
			  <<  "3.5 hz duration has " << hz.count() << " ticks\n";

	chrono::minutes t1(10);
	chrono::seconds t2(60);
	chrono::seconds t3 = t1 - t2;//duration 时钟周期不相同的时候，会先统一成一种时钟，然后再进行算术运算
	cout << t3.count() << " second" << endl;
}
{
	using namespace std::chrono;
  // 新纪元时间 1970.1.1 早08点
	system_clock::time_point epoch;

	duration<int, ratio<60*60*24>> day(1);
	// 新纪元1970.1.1时间 + 1天
	system_clock::time_point ppt(day);

	using dday = duration<int, ratio<60 * 60 * 24>>;
	// 新纪元1970.1.1时间 + 10天
	time_point<system_clock, dday> t(dday(10));

	// 系统当前时间
	system_clock::time_point today = system_clock::now();

	// 转换为time_t时间类型
	time_t tm = system_clock::to_time_t(today);
	cout << "今天的日期是:    " << ctime(&tm)<<endl;  // ctime 同  asctime (localtime (time)) 设置了当前时区

	time_t tm1 = system_clock::to_time_t(today+day);
	cout << "明天的日期是:    " << ctime(&tm1)<<endl;

	time_t tm2 = system_clock::to_time_t(epoch);
	cout << "新纪元时间:      " << ctime(&tm2)<<endl;

	time_t tm3 = system_clock::to_time_t(ppt);
	cout << "新纪元时间+1天:  " << ctime(&tm3)<<endl;

	time_t tm4 = system_clock::to_time_t(t);
	cout << "新纪元时间+10天: " << ctime(&tm4)<<endl;
}
{
	using namespace std::chrono;
	//steady_clock 相当于秒表，只要启动就会进行时间的累加，并且不能被修改，非常适合于进行耗时的统计。

	// 获取开始时间点
	steady_clock::time_point start = steady_clock::now();
	f();
	steady_clock::time_point last = steady_clock::now();
	// 计算差值
	auto dt = last - start;
	cout << "总共耗时: " << dt.count() << "纳秒" << endl;

	// 整数时长：要求 duration_cast
	auto int_ms = duration_cast<chrono::milliseconds>(dt);

	// 小数时长：不要求 duration_cast
	duration<double, ratio<1, 1000>> fp_ms = dt;
	cout << "f() took " << fp_ms.count() << " ms, "//在这 MinGW 不能debug
		<< "or " << int_ms.count() << " whole milliseconds\n";
}

----线程
//C++的多线程，在Debug打断点时只能让CPU选择线程，不能像Java的Eclipse一样，手工切到要运行的线程的断点上

linux下要加 -l pthread,原因为pthread不是linux的默认库

#include <thread>



class Oper {
public:
   void operator() (){
		cout<<"in operator (),in thread ..."<<endl;
	}
}; 
class OperWithParam {
	public:
	   void operator() (string msg){
			cout<<"in operator (),in thread ...,msg="<<msg<<endl;
		}
	};
	
Oper op;
thread t001(op);//要运算符重载()
//或者没有名字
thread t002((Oper()));

thread t003(OperWithParam(),"lisi");

void func(int num, string str)
{
	for (int i = 0; i < 4; ++i)
	{
		cout << "子线程: i = " << i << "num: "
			<< num << ", str: " << str.c_str() << endl;//CDT显示num=breakpoint-modifiedxxxxxx  很多东西,不正常？？

		auto now = chrono::system_clock::now();
		chrono::seconds sec(1);
		this_thread::sleep_until(now + sec);//time_point类型

		this_thread::sleep_for(sec); //duration 类型

		this_thread::yield();

	}
}
void func1()
{
	for (int i = 0; i < 10; ++i)
	{
		cout << "子线程: i = " << i << endl;
	}
}
cout << "主线程的线程ID: " << this_thread::get_id() << endl; //当前线程ID, this_thread 名称空间
thread t(func, 520, "you");
thread t1(func1);
cout << "线程t 的线程ID: " << t.get_id() << endl;
cout << "线程t1的线程ID: " << t1.get_id() << endl;
 //t.join();//如还没执行完毕，主线程阻塞
 //t1.join();
 t.detach();//不会阻塞,分离之后，在主线程退出之前，它可以脱离主线程继续独立的运行 ,主线程退出也会一并销毁创建出的所有子线程
 t1.detach();//detach后不能再join

 {
	thread t;
	cout << "before starting, joinable: " << t.joinable() << endl;//0
	t = thread(func1);
	cout << "after starting, joinable: " << t.joinable() << endl;//1
	t.join();
	cout << "after joining, joinable: " << t.joinable() << endl;//0
 }

int num = thread::hardware_concurrency();
cout << "CPU 核数: " << num << endl;

this_thread::sleep_for(chrono::seconds(5));

---
#include <mutex>
once_flag g_flag;
void do_once(int a, string b)
{
	cout << "name: " << b << ", age: " << a << endl;
}
int g_num = 0;  // 为 g_num_mutex 所保护
mutex g_num_mutex;
//timed_mutex g_num_mutex;//timed_mutex 有 try_lock_for() 阻塞一定的时间长度和 try_lock_until()阻塞到某一个指定的时间点

void do_something(int age, string name)
{
	static int num = 1;
	call_once(g_flag, do_once, age, name);//只调用一次函数do_once，后面是参数,测试下来多数是第一个进入的线程执行
	cout << "do_something() function num = " << num++ << endl;
		g_num_mutex.lock();
//	    g_num_mutex.try_lock();//不阻塞

 //chrono::seconds timeout(1);
//if (g_num_mutex.try_lock_for(timeout)){}


	for (int i = 0; i < 3; ++i)
	{
		++g_num;
		cout << " g_num = " << g_num << endl;
	}
	g_num_mutex.unlock();
	
	
	//使用哨兵锁管理互斥锁 lock_guard  构造时自动锁，退出作用域析构时自动解锁
	lock_guard<mutex> mylock(g_num_mutex);
		  
}


mutex mymutex1,mymutex2;
std::lock(mymutex1,mymutex2);//同时时间锁住至少两个
//...
//mymutex1.unlock();
//mymutex2.unlock();
//或者用
 lock_guard<mutex> myguard1(mymutex1,std::adopt_lock);//加参数std::adopt_lock, 因std::lock()已经做了
 lock_guard<mutex> myguard2(mymutex2,std::adopt_lock);

 //unique_lock 比lock_guard灵活,效率低一点,也是自动解锁,除了支持adopt_lock，还支持try_to_lock和defer_lock
 mutex mymutex3;
 unique_lock<mutex> myunique(mymutex3,std::try_to_lock);//前面不能已经lock

 mutex mymutex4;
 unique_lock<mutex> mydefer(mymutex4,std::defer_lock);
 mydefer.lock();//这开始才加锁

//unique_lock 虽然会自己解锁，但也支持手工解锁
 myunique.unlock();
 mydefer.unlock();
 //unique_lock 还支持继续再锁
 myunique.lock();
 mydefer.lock();
 
myunique.unlock();//多作的手工解锁也不会有错
mutex * my4=mydefer.release();//取消在构造时指定的mutex,如果已经加锁状态，要手工解锁mymutex4
my4->unlock();

mutex mymutex5;
unique_lock<mutex> mydefer5(mymutex5,std::defer_lock);
unique_lock<mutex> mydefer6(std::move(mydefer5));//mutex的所有权做转移


recursive_mutex 允许同一线程多次获得互斥锁,可递归调用,建议少用,转换非递归
recursive_timed_mutex m_re_time;  //也有lock_for,lock_until方法
 
---
#include <condition_variable>

 mutex m_mutex;  
condition_variable m_notEmpty;   //condition_variable
condition_variable m_notFull;    
 void put(const int& x)
{
	unique_lock<mutex> locker(m_mutex);  //unique_lock 包装一个 mutex,比lock_guard灵活,效率低一点
	while (m_queue.size() == m_maxSize)//用循环 可以以多次满的情况
	{
		cout << "任务队列已满, 请耐心等待..." << endl; 
		m_notFull.wait(locker);//还有wait_for和wait_until方法,第二个参数可传一个函数，返回true不阻塞，返回false阻塞(看源码),可防止假醒
	} 
	m_queue.push_back(x);
	cout << x << " 被生产" << endl; 
	m_notEmpty.notify_one(); //也有 notify_all()
}

int take()
{
	unique_lock<mutex> locker(m_mutex);
	while (m_queue.empty())
	{
		cout << "任务队列已空，请耐心等待。。。" << endl;
		m_notEmpty.wait(locker);
	} 
	int x = m_queue.front();
	m_queue.pop_front(); 
	m_notFull.notify_one();
	cout << x << " 被消费" << endl;
	return x;
}



//使用 condition_variable_any 可使用任何 mutex 

--#include <atomic>
 //--atomic 一个struct

atomic<int>   m_value(0);
m_value=20; //重载了=
m_value++; //重载了++

//通过 store() 和 load() 来读写原子对象内部的原始数据。
m_value.store(30); 
int res=m_value.load();

----promise
#include <future>
void TestFuture(promise<string> p){//如果参数形式为promise<string> & p,就要使用std:ref(p)传过来
	cout<<"sub thread begin wait"<<endl;
	this_thread::sleep_for(1s);
	p.set_value("calc value is 200");
	cout<<"sub thread after set value  "<<endl;
}
promise<string> p;
auto future=p.get_future();
cout<<"main thread start sub thread"<<endl;
auto thread1=thread(TestFuture,move(p));//move转换为右值
cout<<"main thread wait sub thread"<<endl;
string res=future.get();//阻塞
cout<<"main thread get  sub thread value"<<res<<endl;
thread1.detach();



---async
int asyncFunc(int a){
	cout<<"asyncFunc thread begin wait,thread id="<<this_thread::get_id()<<",a="<<a<<endl;
	this_thread::sleep_for(2s);//2s=duration,运算符重载了""s
	return 5;
}

//auto future1 = std::async(asyncFunc,20);//开线程，看源码, 看源码launch::async|launch::deferred 两个由系统自动选择
auto future1 = std::async(launch::deferred,asyncFunc,20);//deferred不会立即启动，而是在wait()或者get()时启动,还是使用调用者的线程做的

future1.wait();//等待线程，不返回结果，future也有 wait_for  和 wait_until方法

future_status status=future1.wait_for(std::chrono::seconds(1));
if(status == future_status::timeout){
	cout<<"wait for is timeout"<<endl;
}else if (status == future_status::ready){
	cout<<"wait for is  ready"<<endl;
}else if (status == future_status::deferred){
	cout<<"wait for is  deferred"<<endl;
}
//int val=future1.get();//如不调用wait(),会阻塞,future是转移数据，不能调用两次get()
//cout<<"async thread value"<<val<<endl;

cout<<"future1.valid="<<future1.valid()<<endl;//1
//std::shared_future<int> shared(move(future1));//move到右值,shared_feture是复制的，可多次读
std::shared_future<int> shared=future1.share();
cout<<"future1.valid="<<future1.valid()<<endl;//0
cout<<"shared="<<shared.get()<<endl;
cout<<"shared="<<shared.get()<<endl;


------注意的问题
class Obj{
	public:
		Obj(){
			cout<<"Obj constructor"<<endl;
		}
		Obj(int i):m_i(i){//整数转换为类
			cout<<"Obj conver from  m_i="<<m_i<<endl;
		}
		Obj(Obj & o ):m_i(o.m_i){
			cout<<"Obj copy constructor"<<endl;
		}
		~Obj(){
			cout<<"Obj deconstructor"<<endl;
		}
		int m_i=0; //可选的加mutable关键字
		static void thread_work_ptr( unique_ptr<int> ptr){
			ptr.reset(new int(250));//重新指定智能指针管理的原始内存
		}
	};
//vs2019,CDT工具一定要这作用域结束时,才会断点到子线程 .CDT有时会一直卡住也进不子子线程
void myPrint(const int & i,//如用&,要加const(int&是复制值，地址不同)
			 char * mychar,//如主线程提前结束空间释放，如子线程退出前就可能会有问题
			string mystr,
			const Obj &obj,//const 必须有
				Obj &obj1,
			    Obj * obj2
)
{
	cout<<"obj.m_id="<<obj.m_i<<endl;
	obj1.m_i=30;//detach 不安全的
	cout<<"obj2.m_id="<<(*obj2).m_i<<endl;//detach 不安全的

	cout<<"MyPrint i="<<i<<endl;
	cout<<"mychar="<< mychar<<endl;//detach 不安全的
	cout<<"mystr="<<mystr.c_str()<<endl;
	printf("mystr=%s\n",mystr.c_str());//mystr为string类型，printf用%s参数传mystr.c_str()
}

		
		
 int myvar=30;
char mychar[] ="this is my char array";
int myobjInt=3;

Obj obj(10);
thread t(myPrint,myvar,
		mychar,//指针不安全
	   string(mychar),//mychar[]到string是稳式转换，是先启动子线程再转换的，有可能主线已经结束，解决方法string(mychar)
	   myobjInt, //myobjInt 整数 转换 ,函数要加const ,根据日志发现在转换是在子线程中做的
	   std::ref(obj1),//传递引用
	   &obj //参数只能用  Obj* obj ,不能用 Obj &obj
);
t.join();
//t.detach();//可能会主线程结束了，内存回收了，里的又访问了东西，不安全
cout<<obj1.m_i<<endl;//被子线程修改过



unique_ptr<int> p1(new int(10));
//类中的方法，要是static,不能是某个对象的方法
thread t1(Obj::thread_work_ptr,std::move(p1));// 传指能针用move

/*
#include<stdio.h>//C老式的
#include<cstdio>//C新式的

<array> 头文件

noexcept 话在函数后面表示没有异常

explicit 明确的,显示的 ，implicit 不言明,隐式的
		explicit放在构造函数前，表示 不能发生相应的隐式类型转换


*/

========== C++14

int bin = 0b101010; //前缀0b或0B开头表示二进制
int bin1 = 0B101010;

int million=1'000'000; //千位分隔
int million2=1'00'0'000;//'随意位置


int auto square(double side){ //支持返回auto,对地复杂的模板类型，方便很多
	return side*side;
}

//windows下CDT环境， constexpr不能放main函数中，会导致不能运行,必须重启CDT才可？？为何c++11_new.cpp文件中sumByNum是好的？？
constexpr double pi=3.14;
constexpr double two_pai=2*pi;//编译时确定值，而不是运行时

//windows下CDT环境，constexpr函数不能开，会导致不能运行,必须重启CDT才可？？为何c++11_new.cpp文件中sumByNum是好的？？
constexpr int fib(int num){ //constexpr 表在函数上,编译时运行
		if(num==1)
			return 1;
		if(num==0)
			return 0;
		return fib(num-2)+fib(num-1);
	}
static_assert(fib(10)==55,"unexpected num."); //如果计算值不对，就会编译时报错，编译时运行



template<typename T>
constexpr T pi_type=T(3.14);//模板可以定义变量 

template<typename M>
constexpr M max_num=M(1000);

template<> //钻石语法,指定不同的值
constexpr char max_num<char>('Z');

cout<<pi_type<int><<endl;
cout<<max_num<float><<endl;
cout<<max_num<char><<endl;
		
[[deprecated("this is should not use ,we recommend use others")]]//编译时出这个warning信息
void oldFunc(){
	cout<<"oldFunc"<<endl;
}


#include <memory>
//智能指针的增强make_unique，要加<memory>头文件
auto pcat=make_unique<Cat>();//返回类型为unique_ptr，自动调用构造，结果时自动析构
//现在c++ 大多数情况不需要new和delete
auto p = make_unique<double[]>(10);//指向 10元素的double数组 的指针


auto second=1s;//1s=1秒
auto hour=1h;
auto minute=1min;
auto millsecond=1ms;

auto hello="world";//类型为 const  char * 
 auto hellos="world"s;//s类型为string

 chrono::milliseconds ten_sencond=10s;//做函数参数比int要好
 cout<<ten_sencond.count()<<endl;
 auto plus_sencond=ten_sencond+second;//重载了+，结果为单位小的
 cout<<plus_sencond.count()<<endl;

 std::pair point{2,5}; //pair 只可两个元素，模板可有可无
 std::pair<double,string> point2(2.2,"ss");
 cout<<point.first<<point.second<<endl;

 std::tuple  student3(10,"lisi",89.5);//要<tuple>头文件,tuple可多个元素
 tuple<int,string> student2(10,"lisi");//也可两个元素
 int id= std::get<0>(student2);
 string name=std::get<string>(student2);// 如只有一个string类型才可以这样用
 cout<<id<<name<<endl;




auto myAutoFunc = [](auto x, auto y) {return x + y;}; //可以参数是auto,返回值也是auto,不会出错
cout<<"myAutoFunc="<<myAutoFunc(2,3)<<endl;
auto myAutoFuncString = [](auto const& x, auto const& y) {return x + y;};//可加const&
cout<<"myAutoFuncString="<<myAutoFuncString("abc"s,"def"s)<<endl;



std::unique_ptr<int> ptr(new int(10));
auto lambda = [value = std::move(ptr)] {return *value;};//[]里可以做赋值
cout<<"lambda="<<lambda()<<endl;

auto pNum=make_unique<int>(64);
auto f2=[u{move(pNum)}](int a){//unique_ptr移动
//auto f2=[u =move(pNum)](int a){//写法2
	cout<<(*u)<<a<<endl;
};
f2(20);








int x = 4;
auto y = [&r = x, x = x+1]()->int {//r前加&才可修改，没指定类型。先执行逗号前，再执行逗号后
	//第一次 r=4,x=5
	r += 2;
	return x+2;
 }();  // Updates ::x to 6, and initializes y to 7.
 //结束时为什么x是6？？


========== C++17
gcc 8 以上
g++ -std=c++17
    -std=gnu++17  启用GNU扩展特性

namespace nestnamespace{//嵌入多层的namespace
	namespace xx{

	}
}
namespace nestnamespace::yy{//写法2 

}
	
vector<string> names={"wang","li","zhang","sun"};
{//作用域
	const auto it=find(begin(names),end(names),"wang");
	if(it != end(names)){
		*it="***";//候改它
	}
	printVector("after wang",names);
}
{
	const auto it=find(begin(names),end(names),"li");//可以继续叫it
	if(it != end(names)){
		*it="**";
	}
	printVector("after li",names);
}
//if()中使用;号多隔多条语句，使用最后一个做结果,效果同上,switch( ; )也可以这做用
if(const auto it=find(begin(names),end(names),"zhang");it != end(names)){
	*it="****";
}
printVector("after zhang",names);

if(const auto it=find(begin(names),end(names),"sun");it != end(names)){//也可以继续用变量it
	*it="***";
}
printVector("after sun",names);


for(int i=0;i<10;i++){

}
for(int i=0;i<10;i++){ //可以继续用变量i

}

template <typename T>
auto printTypeValue(T const& value){
	if constexpr (is_integral<T>::value)//编译时,当条件不符合的，会被丢掉，就不会报错
	//if(is_integral<T>::value)//运行时,编译时并不知道value是什么类型
	{
		cout<<"T is int "<<value<<endl;
	}else
	{
		//编译时,知道是string.length()，而不是int.length(),用constexpr不报错
		cout<<"T is not int "<<value.length()<<endl;
	}
}
printTypeValue(20);
printTypeValue("this is string"s);

map<string,int> mymap;//<map>
mymap.insert ({"chinese",100} );
auto mypair= mymap.insert ({"chinese",100} );
if(mypair.second)//如已经存在就失败，和java的存在覆盖是不一样的
{
	cout<<"put success"<<endl;
}else
{
	cout<<"put fail"<<endl;
	auto iter=mypair.first;//已存在的元素是什么
	//老的遍历方式
	for (auto  map_iter=mymap.begin() ; map_iter != mymap.end(); map_iter++ )
	{
		cout << (*map_iter).first << " => " << (*map_iter).second << endl;//first是key,second是value,默认按key排序的
	}

}
auto [position,success]= mymap.insert ({"chinese",100} ); //命名更加清晰
//新的遍历方式
for(const auto & [lang,score] : mymap)//命名更清晰
{
	cout<<"lang="<<lang<<",score="<<score<<endl;
}

void myfunction () throw () {//c++17 不能在throw()里面加int等
  throw  0.32;
}


scoped_lock my_scope_lock;//  <mutex>头文件
 
 
========== C++20   
https://gcc.gnu.org/projects/cxx-status.html#cxx20  验验阶段
https://docs.microsoft.com/en-us/cpp/overview/visual-cpp-language-conformance?view=msvc-160


-std=gnu++2a
-std=c++20 (use -std=c++2a in GCC 9 and earlier) 
gcc 11 版本还有两个不支持

struct rectangle{
	int weight : 3;//增加:3意思是3bit
	int height : 6=10;//可以加默认值

};
struct S{
	int i ;
	float f{};
	double d;
	S():d(1.0),i(2){};//warning上说d是最后初始化的,字段的最后，不受构造器的影响
}; 

void structParam(rectangle r)
{
	cout<<r.weight<<endl;
}
	 
cout<<sizeof(rectangle)<<endl;
rectangle r1{10,20};//老的方式,因设置:3,所以报warning溢出
rectangle r{weight:10,height:20};//初始化,ISO不允许GNU设计的

//S s{d:1.0,i:10};//报错,i和声明字段顺序不一样
structParam({weight:23,height:10});//{}传参数转换为struct

auto max_lamda=[]<typename T>(T x,T y){//lamda支持模板
	return x>y?x:y;
}; 
cout<<max_lamda(3,5)<<endl;




std::array data{1,2,3}; //<array>头文件C++11，以前就有的
auto total=0;
for(std::size_t pos=0;const auto v: data){ //for : 循环，可以在前加语句
	total+=(v*pos);
	pos++;
}

struct Obj{
	std::array<int,3> data={1,2,3};//初始化列表
	const auto &get_data(){ //可加&
		return data;
	}
};
Obj get_obj(){
	return Obj{};
}
//total=8
for(const auto v: get_obj().get_data()){//可直接用，有&
	total+=v;
}



struct Box{
	int x ;
	int y;
};
auto box1=Box{.x=1, .y=1};//designate 命名 ，属性前加.
auto box2=Box{.x{2}, .y=1};
//auto box3=Box{.x(3), .y=1}
auto box4=Box{ //在结构体初始化时可以 属性= 后面值为lamda
	.x=[](){
		return 1;
	}(),
	.y=1
};


template<typename T>
struct BoxTemp{
		T x ;
		int y;
	};
auto box5=BoxTemp<int>{
		.x=[](){
			return 1;
		}(),
		.y=1
	};



auto int_ptr=make_unique<int>(20);
auto printAddr=[](const auto &p){
	cout<<std::to_address(p)<<endl;//to_address转换为C的地址
};
printAddr(int_ptr);
int num=3;
printAddr(&num);
auto ages=vector<int>{20,30};
printAddr(ages.begin());


auto simple_lamda=[]{return 5;};//没有参数时可以没有()
auto simple_lamda2 = simple_lamda;//lamda可以复制
decltype(simple_lamda)  simple_lamda3 ;//decltype可用于lamda上
decltype([]{})  simple_lamda4 ;


//--示例1
auto file_clean =[](FILE * f){
	fclose(f);
	cout<<"the file closed"<<endl;
};
auto file_ptr=unique_ptr<FILE,decltype(file_clean)>(fopen("c:/tmp/a.txt","r"),file_clean);//decltype里可是lamda
//--示例2
//<set>头文件,第一个参数是初始化列表，第二参数比较器
std::set myset{ {Box{1,2}},[](const auto &left ,const auto &right){
			return left.x>left.x;
		}
 }; 
//模板第一个参数元素类型，第二个比较器
std::set<Box,decltype([](const auto &left ,const auto &right){
	return left.x>left.x;
})> myset2 ;



struct Base
{
	//virtual int get_value()=0; 是纯虚函数
	//const成员函数才能被一个const类对象调用
	//[[nodiscard]] 指定函数的返回值不应被丢弃。 如果调用方丢弃返回值，则编译器会生成警告
	//constexpr virtual 函数 一起使用
	[[nodiscard]] constexpr virtual int get_value() const =0;
};
struct Derived:Base
{
	[[nodiscard]] constexpr   int get_value()const override{
		//const成员函数不能使用非const变量
		return 5;
	}
};

//<array>头文件，C++11,模板第一是类型，第二个大小
const std::array<const Base *,5> data{&d1,&d2,&d3,&d4,&d5};


constexpr int const_dynamic(){
	if(std::is_constant_evaluated())//<type_traits>头文件,如果使用contexpr或const x= 接收这个函数值就是true
	//if constexpr(std::is_constant_evaluated())//是否在编译时间，条件一直成立
	{
		return 5;
	}else{
		return 10;
	}
}

//C++11
start = Numbers.begin() ; 
end = Numbers.end() ;
//remove函数要引入<algorithm>头文件,“需要被删除”的元素被移到了区间的尾部。它返回一个迭代器，指向第一个“需要被删除”的元素。
//begin()只要<vector>头文件,其实就是vec.begin();
// remove all elements from Numbers that match 10
last = remove(start, end, 10) ; 
cout << "Total number of elements removed from Numbers = "  << end - last << endl ;
cout << "有效的元素为of Numbers { " ;
for(it = start; it != last; it++)
	cout << *it << " " ;
cout << " }\n" << endl ;
//后面的并没有删除元素，只是修改了元素
for ( auto it=Numbers.begin() ; it != Numbers.end() ; it++ )
{
	cout << " " << *it;
}

//---
 for (std::vector<int>::iterator it = c.begin(); it != c.end(); ) {
	  if (*it % 2 == 0) {
		  it = c.erase(it);//遍历时删除 ，要重新赋值才行，返回iterator是跟随最后删除的元素
	  } else {
		  ++it;
	  }
  }

//新的方式
vector v2{1,2,4,5,6};
std::erase(v2,5);


struct NoConstructBox{
	int x ;
	int y;
	NoConstructBox()=delete; //防止有默认的构造
	NoConstructBox(int _x){
		x=_x;
	}
};
//NoConstructBox no_box{1,2};//C++11的功能,如有 NoConstructBox()=delete;这样报错
NoConstructBox no_box{1};




template<typename Callable,typename ... Param> //模板可以动态数
auto bind_values(Callable callable,Param ... param){
	//auto lamda = [=](){
	auto lamda = [callable=move(callable), ...param=move(param)](){//...放前面展开参数,多个值用逗号分隔
		return callable(param...);
	};
	return lamda;
}
int add(int one,int two){
	return one+two;
}
auto bind_func=bind_values(add,1,2);//如果个数不对，会编译报错
cout<<"bind_func:"<<bind_func()<<endl;



//bind_front 用来替代bind函数
//auto mybind=std::bind_front(add,1); //<functional>头文件,可只传部分参数
auto mybind=std::bind_front(&add,1); //&add
cout<<"bind_front:"<<mybind(2)<<endl;//补齐其它的

struct Man {
		int id;
		string name;
		auto operator <=> (const Man & m )const=default; //编译器生成比较，根据声明字段顺序比
		//如没有则 提示Man' is not derived from 'const std::optional<_Tp>'
	};
set<Man> peoples{{1,"lisi"},{2,"wang"}};//<set>头文件，set里的元素要有比较功能
Man m1{1,"lisi"};
Man m2{2,"wang"};
cout<< "m1 == m2?" <<( m1 == m2 )<<endl;



constexpr auto mysort(auto data){
	std::sort(begin(data),end(data));//std::sort
	return data;
}
constexpr auto sorted_data=mysort(std::array{20,44,52,78,65,44});
static_assert(std::is_sorted(begin(sorted_data), end(sorted_data)));//std::is_sorted
cout<<sorted_data[0]<<endl;



constexpr auto get_value_inner(){
	int * i=new int{5};//没有delete
	return i;
}
constexpr auto get_value(){
	int * i=new int{}; //声明为constexpr类型的,如果接收这个函数的返回值使用constexpr，在这用new分配内存，就必须在constexpr的范围内(编译时间内)delete
	delete i;
	//嵌套函数里没有delete,后面也要补delete
	auto p=get_value_inner();
	auto val=*p;
	delete p;//这补的
	//如果先delete p;再auto val=*p;编译时就会报错
	
	return val;
//		return 1;
}
int val=get_value();
constexpr int val2=get_value();//如函数内部用new，就必须在编译时间做delete


//只可以匹配两个float参数传进来，不能是两个int(模板做类型限制)
//floating_point 浮点,_v=value (std::enable_if_t 中_t=type)
template<typename Float>
auto myadd(const Float x,const Float y) requires std::is_floating_point_v<Float>{
	return x+y;
}
//--方式二
template<typename T>
concept floating_point=std::is_floating_point_v<T>;
template<floating_point Float>
auto myadd2(const Float x,const Float y) {
	return x+y;
}
//--方式三
template<typename T>
concept floating_point2=std::is_floating_point_v<T>;
//				auto myadd3(const floating_point2 auto x,const floating_point2 auto  y) {//加auto,参数可以是不同类型小数
floating_point2 auto myadd3(const floating_point2 auto x,const floating_point2 auto  y) {//返回也可修改
	return x+y;
}

 
myadd(3.1,4.1);
myadd2(3.1,4.1);
myadd3(1.5,1.6f);//可以是不同类型小数

constexpr int const_method(){
	return 22;
}
constinit static auto i=4; //constinit 编译时初始化 ,加static 因为本地变量不能为const
constinit static auto i1 = const_method();//如函数没 constexpr 会报错



template<typename FuncObj>
auto use_func(FuncObj obj)
{
	return obj();
}
template<typename FuncObj>
auto use_func2(FuncObj obj)
	requires requires {&FuncObj:: operator();} //不能传 函数名 即 指向函数的指针
{
	return obj();
}
template<typename T> concept has_call_operator=requires {&T:: operator();};//要以;结尾

template<typename FuncObj>
auto use_func3(FuncObj obj)
	requires  has_call_operator<FuncObj> //简化为使用concept
{
	return obj();
}
int empty_func(){
	return 1;
}

use_func([](){});
use_func(empty_func);
use_func2([](){});
//use_func2(empty_func);//编译失败， 不匹配规则，不能传 函数名 即 指向函数的指针
use_func3([](){});
//use_func3(empty_func);//编译失败


struct [[nodiscard("struct no receive value^^^")]]  NoDis{ //函数调用后的返回这个struct ,也要有接收值，否则warning ,[[nodiscard]]也可放struct前面

	[[nodiscard]]  NoDis(int){ 
	 }
};
NoDis get_no_dis(){
	return NoDis(3);
}

//NoDis(2);
get_no_dis();//这没有接收有warning
//NoDis s=get_no_dis();



void jthread_job(std::stop_token token){
	while(!token.stop_requested()){//判断停止消息
		std::this_thread::sleep_for(1ms);
		cout<<"jthread job"<<endl;
	}
}
jthread t1(jthread_job);//jthread
std::this_thread::sleep_for(10ms);
t1.request_stop();//停止消息
t1.join();

cout<<"5的二进制1的个数为"<<std::bitset<32>(5).count()<<endl; //<bitset>头文件
cout<<"3的二进制1的个数为(无符号整数) popcount 为"<< std::popcount(3u) <<endl; //<bit>头文件，只可是unsign 整数类型，数字后加u
cout<<"5和7的中间值为"<<std::midpoint(5,7)<<endl;//<numeric>头文件中 ,5+7/2
const int nums[]={1,2,3};
std::array<int,3> myarray=std::to_array(nums);//std::to_array
std::to_array("22");



//likely和unlikely(linux内核中有自带的宏定义)，我们可给编译器一种暗示，即该分支条件被满足的概率比较大或比较小。而编译器利用这一信息优化其机器指令，
if(argc>1) [[unlikely]] //对应的有   [[likely]]
	cout<<"argc>1"<<endl;
else  [[likely]]
	cout<<"argc<=1"<<endl;

std::cout<<"pi   派的值为"<<std::setprecision (30)<<std::numbers::pi<<std::endl;//<numbers>头文件中的名称空间为 std::numbers
std::cout<<"pi_v double派"<<std::setprecision (30)<<std::numbers::pi_v<long double><<std::endl;//发现值和上面的不一样，这个比下面的要准一点

std::vector<int> scores{66,77,88};
cout<<scores[log,2]<<endl;//逗号分隔数组，warning提示过时,返回下标为2的元素



throw std::logic_error("logic error");
//consteval 类似于constexpr constinit
 

//========== trivially  
//----------1
	template<typename Contained>
	struct Optional{

		union { Contained data; };//union的特别用法,没有名字???
		//Contained data;

		bool initialized=false;

		constexpr Optional &operator=(Contained && data){
			this->data=std::move(data);//仿问data??
			initialized=true;
			return *this;
		}
		~Optional()=default;
		/*
		 //加这个 static_assert 失败
		~Optional(){
			if(initialized){
				data.~Contained();//仿问data?? 显示调用析构函数
			}
		}
		*/
	};

//----------2
	template<typename Type>
	struct Optional_nontrivial{
		~Optional_nontrivial(){
			static_cast<Type *>(this)->data.~Contained();//static_cast函数强转
		}
	};
	struct Optional_trivial{

	 };

	template<typename Contained>
	struct Optional2: std::conditional_t<std::is_trivially_destructible_v<Contained>,//用继承 conditional_t条件
										Optional_trivial,//条件成立时用
										Optional_nontrivial<Optional<Contained>>//条件不成立时用
										>
	{
		union { Contained data; };

		bool initialized=false;

		constexpr Optional2 &operator=(Contained && data){
			this->data=std::move(data);
			initialized=true;
			return *this;
		}

	};
	//--------3
	template<typename Contained>
	struct Optional3
	{
		union { Contained data; };

		bool initialized=false;

		constexpr Optional3 &operator=(Contained && data){
			this->data=std::move(data);
			initialized=true;
			return *this;
		}
		//新的,使用concept去约束 析构
		constexpr ~Optional3() requires( ! std::is_trivially_destructible_v<Contained>)
		{
			if(initialized){
				data.~Contained();
			}
		}
		constexpr ~Optional3()=default;
	};


Optional<int> obj;
obj=5;

//断言失败影响编译(如自己实现~Optional(){}就会失败)
static_assert(std::is_trivially_destructible_v<Optional<int>>);//trivially 平凡地(即简单数据类型)，要<type_traits>头文件
//static_assert(std::is_trivially_destructible_v<Optional<string>>);//失败，string是简单数据类型

static_assert( std::is_trivially_destructible_v<Optional2<int>>);
static_assert(!std::is_trivially_destructible_v<Optional2<string>>);

static_assert( std::is_trivially_destructible_v<Optional3<int>>);
static_assert(!std::is_trivially_destructible_v<Optional3<string>>);
//==========
enum struct MyColor{ //enum后可加struct
	red=1,
	green=2,
	blue=3,
};
string to_string_color(const MyColor c){
//		switch(c){
//		case MyColor::red:
//			return "red";
//		case MyColor::green:
//			return "green";
//		case MyColor::blue:
//			return "blue";
//		return "unkown";
//		}

	//新的方式,类似于import，可用于函数中
	using enum MyColor;
	switch(c){
	case red://省了前缀
		return "red";
	case green:
		return "green";
	case  blue:
		return "blue";
	return "unkown";
	}
}
namespace myns{
	using enum MyColor;//可用namespace中
}
struct WrapColor{
	using enum MyColor;//可用于struct中
	MyColor used=red;
}; 
MyColor use_color=myns::red;

consteval int sqr(int n){//consteval
	return n*n;
}
int res=sqr(100);//函数使用consteval,参数不能是变量
int x=10;
//res = sqr(x);//参数是变量错误

void log(const std::string_view message,  //string_view
		 const std::source_location location =
			   std::source_location::current())// <source_location>头文件
{
	std::cout << "file: "
			  << location.file_name() << "("
			  << location.line() << ":"
			  << location.column() << ") `"
			  << location.function_name() << "`: "
			  << message << '\n';
}

log("Hello world!");



//--<ranges> 头文件 像java stream api
auto const ints = {0,1,2,3,4,5};
auto even = [](int i) { return 0 == i % 2; };
auto square = [](int i) { return i * i; }; 
//老的方式,函数嵌套，第一个参数是输入
//std::views::filter ,std::views::transform
for (int i : std::views::transform(std::views::filter(ints, even), square)) {
	std::cout << i << ' ';
}
std::cout << '\n';
//管道当输入
for (int i : ints | std::views::filter(even) | std::views::transform(square)) {
	std::cout << i << ' ';
}
}


//--类似Java CyclicBarrier
void TestBar(int i,barrier<>*bar){
	cout<<"sub thread begin wait"<<endl;
	this_thread::sleep_for(1s);
	//bar->wait(bar->arrive());//当有3个线程时才开始同时执行
	 bar->arrive_and_wait();//合并两个函数
	 //也有 bar->arrive_and_drop(); 不常用
	cout<<"sub thread do job at same time,thread_id="<<this_thread::get_id()<<endl;

}
int count=3;
barrier bar(count);
for(int i=0;i<count;i++)
{
	thread th(TestBar,i,&bar);
	th.detach();
} 

this_thread::sleep_for(5s);


class Person{
public:
	Person(int age):age(age){};
	bool operator == (const Person & p){ //如重载了== 就不用重载!=
		return p.age == age;
	}
private :
	int age;

};
Person p1{11},p2{22};
cout<< (p1!=p2) <<endl;//如重载了== 就不用重载!= ,要加()
cout<< (p1==p2) <<endl;

//--<concepts> 头文件， 比模板好的是，只能用指定的类型，而不是所有的类型
template<typename T>
concept MyInt=same_as<T,int> ||same_as<T,long> ||same_as<T,long long>||same_as<T,unsigned int>||same_as<T,unsigned long>;
template<MyInt T>
T add(T a,T b){
	return a+b;
}
int res=add(2,3);//不能使用float,double



Warning中有 -pedantic 选项(卖弄学问的)


https://en.cppreference.com/w/cpp/language/coroutines
Coroutine 定义在 <coroutine> 头文件中

std::coroutine_handle<X>//struct可带模板

std::suspend_always //普通struct
std::suspend_never  //普通struct


throw std::runtime_error("xx");
 

auto myfunc(std::jthread& out) {
	
}
std::jthread out;
co_await myfunc(out ); //等待coroutiner执行有返回值，不一定执行完

co_yield  10;	暂停执行返回一个值(co_yield 关键字)
co_return 5;	完成执行返回一个值(co_return 关键字)


//---module 模块 像JDK9,都还是实验阶段
GCC要加编译选项 -fmodules-ts
VC2019 要项目属性->C/C++->Language->Enable Experimental下拉选择module

========== C++23  验验阶段
GCC 11 版本还有一个不支持

C++23 features that do not reflect the final standard

========== boost   C++ 标准库,MySQL有使用这个库



==========数据结构
//--链表
线性表: 地址连续的
单向链表,单向循环链表,
双向链表,双向循环链表,
栈:(表达式) 实现迷宫(ITSHOW),递归,
	实现表达式,要运行算的顺序不变,前缀式:运算符放前,先找两个要运算的再找前的运算符 二叉树的前序遍历
									后缀式:是正确的运算符顺序,先找运算符,再找前两个 二叉树的后序遍历
队列 ,哨兵节点


循环队列,满的条件是 (rear+1)%size==front,空的条件是rear==front

//--树
满二叉树,完全二叉树
先序遍历,中序遍历,后序遍历,只要有序遍历和另一个,就可以求另一个
复制二叉树,求二叉树深度,节点数

所有的树都可以转成二叉树,二叉树左子树是树的第一个左子树,右邻居做右子树(二叉树根无右子树,用来接另一颗二叉树)

第n层二叉树中,最多有2^(n -1) 个节点,
深度为n的二叉树,最多有(2^n) -1 个节点


二叉树  叶子数 = 2度节点数+1
树	叶子数 = 所有节点度数之和+1
完全二叉树 n个节点的深度为log2N +1 

快速排序 递归深度最多log2N,存诸开销O(log2N),比较次数O(nlog2N)

拓扑排序,如有的零件必须要有其它的零件才可组成,如课程要有基础课程,
方法,选择一个入度为0的节点,删除该顶点关联的弧,即头节点的入度-1,
如图中没有入度为0的节点,说明有环
 

平衡二叉排序树(左节点<父节点<右节点),左右子数的高度至多差一,平衡因子定义为左子树的高度 - 右子数的高度 
被加节点的父节点向上提
保证查找性能为O(log2N),

1.LL 左孩子的左子数插入,左平衡因子变为2,根节点下降为左子树的右边,新根节点的原右子为树指向为老根节点的左子树(提高一层)
2.RR 右孩子的右子数插入,右平衡因子变为-2,根节点下降为右子树的左边,新根节点的原左子为树指向为老根节点的右子树(提高一层)
3.LR 左孩子的右子数插入,		(提高二层)
4.RL 右孩子的左子数插入,		(提高二层)


树叶子数 = ( n(k-1)+1 )/k  (k度) ,(n个节点)
树叶子数 = ( n(k-1)+1 )  (k度) ,(n非叶子节点)



//--矩阵
特殊矩阵的压缩
1.对称矩阵 a[i][j]=a[j][i] (对角线全存)  n阶矩阵压缩后要使用的空间 (n^2)/2  +  n/2 (主对角线) = (n(n+1))/2
公式 (压缩后的数组索引k ,ij为矩阵压缩前的行列)
k=( i(i-1)/2  ) + j   当(i>=j)
k=( j(j-1)/2  ) + i   当(i<j)

2.上三角矩阵,其它处可全是0,或全是常数c ,对角线要存,空间 ((n(n+1))/2) +1

k=  ( (i(i-1))/2  ) x (2n-i+2) +j-i+1	当(i<=j)
k=  (n(n+1)/2)+1			当(i>j)

3.下三角矩阵
k=  ( i(i-1)/2  ) + j	当(i>=j)
k=  ( n(n+1)/2  ) + 1	当(i<j)


稀疏矩陈的压缩存储  对一个二维数组(矩阵) 中记录非零值所在 x,y,值 ,这个数据的行数,列数,非零个数
矩陈的相乘
//--图
n个节点无向图 的连接线数最多 (n(n-1))/2
哈夫曼树(二叉树)对所有现存节点,找两个最小的节点,建立一个新节点值为它们之和,使用这个新节点,把原两个节点去除,重复直到所有节点都使用完
图的最小生成树 , 任取一个节点,找与关联权重最小的边对应的节点,要求不能构成环,再对所有找过的中，再重复找,n个节点最多有 n-1 条边

结点的路径长度:从根结点到该结点的路径上分支的数目。
带权路径长度WPL,叶子节点乘以它的[路径长度]，然后 累加起来就是啦。

二叉树的个数和节点的关系是：S（个数） = n!-1  ,n是节点数


散列表
散列函数,根据传入用于计算散列地址,表示散列表的地址
同义词,表示不同键值k1和k2,使用散列函数计算后得到相同的列表的地址,称为冲突
开散列表使用二维单链表,第一维存放key,第二维每个链表存所有的同义词,称同义词子表
闭散列表第一维是使用数组

堆排序,用一维数组做完全二叉树,把要排序的放入二叉树,从上到下,从左到右
	从n/2的节点开始(最后一个有叶子的父节点),以该节点为根做判定树重建,该子树的根节点为最小的,
	再对前一个节点为根做判定树重建,直到完全二叉要的根为最小值,把根节点与最后一个节点交换,
	输出最小节点,删除最小节点与二叉树的连接,再重复到根节点为第二个最小节点...


BTree  
R-tree
Log-Structured Merge-Trees (HBase使用)


