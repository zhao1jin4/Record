http://www.open-std.org/jtc1/sc22/wg21/   The C++ Standards Committee
Ritchie 研制的C语言
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

double triangle(double x)throw(int,double)//可能是int,也能是double
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
	swprintf(result,L"%ld",-123.33);//MinGW
	//swprintf(result,20,L"%ld",-123.33);//Cygwin
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
	int str_len=strlen(str);//怎么会是11 ??????
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
	int result=iswalnum(L'a');
	result=iswalnum(L'中');
	result=iswdigit(L'3');
	result=iswdigit(L'A');//a-f或者A-F,十六进制
	//以上返回0表示假,正数表示真

	 int i=0;
	  wchar_t *str = L"Test String.\n";
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


//---set
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

//--C++11中新加 unordered_map,unordered_multimap,unordered_set,unordered_multiset,,,,,forward_list,,array
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


//----------- 模板别名
template <class T> using Ptr = T*;
Ptr<int> ip;  // decltype(ip) is int*


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
    f(0); //调用的是哪个 f?
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

	auto x=0; //0 是 int 类型，所以 x 也是 int 类型
	auto c='a'; //char
	auto d=0.5; //double
	auto national_debt=14400000000000LL;//long long


	const vector<int> vi;  
	typedef decltype (vi.begin()) CIT;   //新的操作符 decltype 可以从一个表达式中“俘获”其结果的类型并“返回”
	CIT another_const_iterator;
 }

int* a = new int[3] { 1, 2, 0 };
	
class C 
{  
   int a=7;    //C++11 only  这和Java一样
   public:  
	   C(); 
};


		
struct A  
{  
 A()=default; //C++11  
 virtual ~A()=default; //C++11  
};  

========== C++14
https://gcc.gnu.org/projects/cxx1y.html
-std=c++14

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


B+Tree (Oracle使用,Oracle的行锁??)
Log-Structured Merge-Trees (HBase使用)
