https://developer.apple.com/
https://developer.apple.com/library/mac/navigation/
https://developer.apple.com/library/ios/navigation/


Foundation Framework 是ObjectiveC的一些基础类,在Mac OS和 iOS中都有效
Mac OS X 和 iOS 区别只是在Cocoa和Cocoa Touch的不同,其它的是一样的

UI_* 表示UIKit ,iOS上的用户图形包
NS_* = NextStep
IB_* = InterfaceBuilder,如 IBOutlet ,IBAction
CG_* = CoreGraphic
CF_* = CoreFoundation
CA_* = CoreAnimation
CI-* = CoreImage
CM_* = CoreMotion
QT_* = QuickTime
GK-* = GameKit
CB_* = CoreBluetooth

UltraISO可以转换Mac OS X 10.7.0.dmg,也可使用dmg2img命令,到Mac OS X 10.8.dmg 就只能用UltraISO了
工具->加载到虚拟光驱，
工具->格式转换成iso,就可以变成windows认的


免费的Notepad++自带Objective-C的支持,建立文件后"语言"->Objective-C
设置.m .h文件关联Objective-C
Setting->Style Configurator...->选择Objective-C->在User ext.:中输入m h


Notepad++插件NppExec,把NppExec.dll复制到Notepad++\plugins目录下
	Plugins -> NppExec -> Execute...
	输入cmd /c "gcc -o $(CURRENT_DIRECTORY)$(NAME_PART) $(FULL_CURRENT_PATH) -ID:/GNUstep/GNUstep/System/Library/Headers -fconstant-string-class=NSConstantString -LD:/GNUstep/GNUstep/System/Library/Libraries -lobjc -lgnustep-base"
	点击"Save..."按钮保存，命名为"CompileLinkOC"
	输入cmd /c "$(CURRENT_DIRECTORY)$(NAME_PART).exe" 保存为"ExecuteOC"
	Plugins -> NppExec ->Advanced Options...
	在Associated script:下面下拉框中选择刚建立的"CompileLinkOC",点击"Add/Modify"按钮将它们依次添加到Menu items下面的列表中
	选择 "Place to the Macros submenu"复选框。
	菜单Macro可以看到"CompileLinkOC"
	 

免费的gvim自带.m文件的高亮显示,
免费的eclipse加CDT插件好像不能识别.m,或用google的objectiveclipse(eclipse Juno),未测试
 

GNUSetup Windows的安装
gnustep-msys-system-0.29.0-setup.exe
gnustep-core-0.29.1-setup.exe
gnustep-devel-1.4.0-setup.exe

默认加的PATH
D:\program\GNUstep\GNUstep\System\Tools;D:\program\GNUstep\bin

在安装完的Readme.doc中说,要先安装 GNUstep System ,再安装 GNUstep Core,否则不能正常工作,我安装时未按这个顺序也OK
三个安装包都安装在同一相目录下(D:\GNUstep)

START -> Programs -> GNUstep -> Shell  ,就是启动MSYS,shell的根目录位于D:\GNUstep\msys\1.0

$ gcc --version			是4.6.1

make要设置环境变量 GNUSTEP_MAKEFILES=D:\Program\GNUstep\GNUstep\System\Library\Makefiles

$ vim helloworld.m   	.m(代表模块)
#import <Foundation/Foundation.h>  
int main (int argc, const char *argv[]) 
{    
  NSAutoreleasePool *pool=[[NSAutoreleasePool alloc] init];      
  NSLog(@"Hello World!");    
  [pool drain];    
  return 0; 
 }   
 
$ vim  GNUmakefile
include $(GNUSTEP_MAKEFILES)/common.make 
TOOL_NAME = helloworld
helloworld_OBJC_FILES = helloworld.m 
include $(GNUSTEP_MAKEFILES)/tool.make 

$ make
会生成obj目录里机有可执行的.exe文件,测试OK


方法二
D:\GNUstep\msys\1.0\home目录下建立helloworld.m文件,或者使用vim /home/helloworld.m
$ cd /home
$ gcc -o helloworld helloworld.m -I/GNUstep/System/Library/Headers -fconstant-string-class=NSConstantString -L/GNUstep/System/Library/Libraries -lobjc -lgnustep-base

测试OK
D:\Program\GNUstep\GNUstep\System\Library\Headers
D:\Program\GNUstep\GNUstep\System\Library\Libraries

如果不使用shell,使用cmd也可以,测试OK
GNUSetup会自动加PATH环境变量,注意和自己有的MinGW的PATH最好不要放在一起

gcc -o helloworld helloworld.m -ID:/Program/GNUstep/GNUstep/System/Library/Headers -fconstant-string-class=NSConstantString -LD:/Program/GNUstep/GNUstep/System/Library/Libraries -lobjc -lgnustep-base

加 -g 编译可以使用gdb来调试
gdb helloworld
	>pwd 	//看当前目录
	>file xxxpro (要debug的程序)
	>breake helloworld.m:20//设断点20行  ,也可只 20
	>delete 1	// 删第一个断点
	>run
	>print i		//显示i变量值
	>po(print-object) xx
	>n(next)		//(不进函数内部)
	>step		//单步执行(进函数内部)
	>finish 	//从方法中出去
	>c(continue) //运行到下个断点
	>list		//附近代码
	>l(list) 10	//10行附近代码
	>set variable p=0	//改变变量p的值为0
	>frame			//打印当前帧,光标所要执行的位置
	>bt(backtrace)		//显示当前在哪个文件的多少行，哪个函数
	>info break		//查看所有断点,有号
	>delete 1	// 删第一个断点
	>break 21 if value==div //条件断点
	>condition 1 value==div //对已经设置的断点号,设置停止条件
	>info locals 		//显示所有的当前的局部变量
	>quit 

	file GnuSetup的msys要 pwd看当前目录 ./home/	
	clear FILENAME:NUM        删除断点。
	po (print-object) [txtName text]
	查看数组
	po [myArray objectAtIndex:index]

ctrl+d 删除光标字符,ctrl+b向后,+ctrl+f向前,ctrl+a到头,ctrl+e到尾

#program mark ----tag  //是为方便的可以找到用的,书签

//=============Objective-C
是依赖于C/C++的,是完全兼容C的
Objective-C 不支持命名空间

OC中所有的关键字都是以@开头
BOOL ,TRUE,FALSE,YES,NO
for(xx in xxx)

基类 NSObject ,只支持单继续,@protocol 实现接口
@try{}
@catch{}
@finally{}

id 类型 ,可认为是void*
nil类型,可认为是null

所有的函数都是虚函数

在函数中可以使用 NSLog(@"%s",__FUNCTION__);打印当前所在的函数名

//-----------dog.h文件中声明
#import <Foundation/Foundation.h>  //编译会知道导入了一次,就不会再导入第二次了
@interface Dog:NSObject{
	//只能写字段,不能写函数
	@public 
		int age;
	@protected  //OC默认是@protected,也可是@public,@private
		int ID;
	@private 
		float _price;
}
//这里写函数,OC叫消息,所有的函数全是没有作用域的,即@public的,可以把函数不声明在.h文件
//+表示类的方法,即static,-表示对象的方法,:后表示参数
-(void) setAge:(int)newAge;
-(int) getAge;

@property(readonly)float price;//自动生成setPrice , price 方法的声明
//默认是readwrite,可以是readonly

-(id)init;//要返回id
-(id)initWithOther:(int)newAge :(float)newPrice;//:前是标签,如没有是匿名标签,第一个:前的是函数名,第二个:前要有空格
-(void)print:(int)newID andAge:(int)newAge; 
-(void)print:(int)newID andPrice:(float)newPrice;//OC函数重载不区分类型,要使用不同标签来区分
@end

//---------dog.m文件中实现使用 ,m表示moudle
#import "dog.h" 
@implementation Dog
-(id)initWithOther:(int)newAge :(float)newPrice//:前是标签,如没有是匿名标签,第一个:前的是函数名,第二个:前要有空格
{
	self=[super init];
	age=newAge;
	_price=newPrice;
	return self;
}
-(id)init
{
	return [self initWithOther:5 :100.0f];
}

 
@synthesize  price = _price;//使用指定属性自动生成 setPrice , price 的方法实现,也可不指定名字相同
@end


//----------main.m
#import <Foundation/Foundation.h>
#import "dog.h"
int main(int argc,const char* argv[])
{
	//@autoreleasepool {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];  
		NSLog(@"hello");
		//建立对象 
		Dog *dog =[Dog alloc];//所有的对象必须加* ,也表示一个引用,也表示一个指针,alloc 是NSObject的方法
		[dog init];
		//或者用一行函数的两次调用   Dog *dog =[[Dog alloc]init];
		[dog initWithOther:5 :200];//调用传参
		int age=[dog getAge];
		printf("age is:%d\n",age);
		
		dog.age=10;//相当于调用[dog setAge:10] ,age属性是private的
		int age1=dog.getAge;
		//dog.setAge=20;//错误
		//age1=dog.age;//错误
		NSLog(@"age1 is:%d\n",age1);
		
		[dog print:1 andAge:5];//带标签的传参
		[dog print:1 andPrice:105.0f];
		[dog  release];//销毁对象
	[pool release];  	
	//}
 	return 0;
}

gcc -c dog.m main.m -ID:/Program/GNUstep/GNUstep/System/Library/Headers -fconstant-string-class=NSConstantString -LD:/Program/GNUstep/GNUstep/System/Library/Libraries -lobjc -lgnustep-base
gcc -o dog dog.o  main.o -ID:/Program/GNUstep/GNUstep/System/Library/Headers -fconstant-string-class=NSConstantString -LD:/Program/GNUstep/GNUstep/System/Library/Libraries -lobjc -lgnustep-base
 
self.age和age是不同的,有点后面的就是函数

retainCount ,一个类,产生多少个对象,内存有多少被引用
alloc 分配置存后为1 ,retain 在原来基础上加1, release 在原来基础上减1 ,直到retainCount为0,内存就会自动被释放

NSMutableString *str=[[NSMutableString alloc]init];
[str retain];//2
[str retain];//3
int count=[str retainCount];
NSLog(@"retainCount:%d\n",count);//3
[str release];//2
[str release];//1
[str release];//0




//NSObject 有dealloc方法,是当retainCount为0时自动调用
-(void) dealloc
{
	//NSLog(@"Person 的 dealloc 被调用");
	NSLog(@"Person   dealloc invoked!");
	[super dealloc];
	[_book release];//把自己引用其它的对象也release
}

-(void) setBook:(Book*)newBook{
	if(_book!=newBook)//是否被相同的调用两次
	{
		[_book release];//是否被不相同的调用两次
		_book=newBook;
		[newBook retain];
		//或者用一行 _book=[newBook retain];
	}	
}

//----
//@property(assign)是默认的
@property(retain) Book *book; //@synthesize 生成生成方法getter/setter实现,内部自动对计数器加减

//-person.m
@synthesize  book=_book; 
-(void) dealloc
{
	[super dealloc];
	self.book=nil;//对应@property(retain),自动减一
}

//---
@property(copy) Book * _book;//生成setBook方法是把传来的复制一份,形式为[_book copy]

//--数组
NSMutableArray *array=[[NSMutableArray alloc]init];
int i;
for(i=0;i<5;i++)
{
	Book *temp =[[Book alloc]init];
	temp.ID=i;//ID是基本类型不用release
	[array addObject:temp];//向数组加元素
	[temp release];//记得release
}
for (Book *b in array)
{
	NSLog(@"book ID is %d",[b ID]);
}
[array release];


也可以使用C语言的数组,如 id _objs[5];
类 NSInteger,NSUInteger,


//调用autorelease方法,会把自己放入,离自己最近的释放池中,栈顶的池
NSAutoreleasePool *pool2 = [[NSAutoreleasePool alloc] init];
	Book *other =[[[Book alloc]init]autorelease];//以后不可再使用other,只会在pool release时把这个才会被release
	//...其它地方可以用 other
	Person *zhao=[[Person alloc]init];
	[zhao setBook:[[[Book alloc]init]autorelease]]; //特殊用法,如是release是错误的
	//zhao.book=[[[Book alloc]init]autorelease]; 
	[zhao release];

[pool2 release];

对没有调用alloc方法的对象,如类的静态方法,不用做release,内部是使用过autorelease的

//------协议 
//类似于C++的纯虚函数,Java中的接口,只写在.h中
@protocol MyMouseProtocol <NSObject> //至少要继承NSObject
	@optional //表示方法可以不实现,是默认的
		-(void)onMove:(int)x y:(int)y;//方法的标签和名字可以取相同值
	@required //表示方法必须实现,
		-(void)onClick:(id)event;
@end

@protocol MyKeyProtocol <NSObject>  
	@optional  
		-(void)onKeyDown:(int)code;
@end

@class Computer;//@interface的定义使用@class声明
@interface Programmer :NSObject<MyMouseProtocol,MyKeyProtocol>//实现协议
{
	Computer *_computer;
}
	-(void)otherBussiness;
	-(void)dealloc
@end



@protocol MyMouseProtocol;//声明
@interface Computer:NSObject
{
	id<MyMouseProtocol> delegate;
}
@property(assign) id<MyMouseProtocol> delegate;//id 只是一个数,地址值不用retain,要为id<>和属性要一样
@end	
	
//.m实现方法
@implementation Programmer :NSObject //不能加 <MyMouseProtocol,MyKeyProtocol>,也可不加 :NSObject
	
	@synthesize computer=_computer;
	-(void)dealloc
	{
		printf("Programmer dealloc! \n");
		[super dealloc];
		self.computer=nil;//声明为@property(retain)
	}
	
	-(void)setComputer:(Computer *)computer
	{
		if(_computer!=computer)
		{
			[_computer release];
			_computer=computer;
			//[computer retain]; //声明为@property(retain)不要做这步
		 	//[_computer setDelegate:self];//GnuSetup加NSTimer不能调用这个
			//computer.delegate=self;
		}
	}
@end


//使用
id<MyMouseProtocol> mouse=[[MyComputer alloc] init];
 
//GnuSetup不认 repondsToSelector
if( [mouse repondsToSelector:@selector( onMove:y: ) ])//判断是否实现了@optinoal的方法
{
	[mouse onMove:20 y:30]];
}
//或者
SEL sel=@selector(onMove:y:);//把方法转换成SEL类型
if( [mouse repondsToSelector:sel] )
{
	[mouse onMove:20 y:30]];
}
 
//[mouse onMove:20 y:30];//GnuSetup加NSTimer不能调用这个
//[mouse otherBussiness];//GnuSetup不能调用
[mouse release];
 



NSTimer* timer =[NSTimer scheduledTimerWithTimeInterval:1.0f target:self selector:@selector(timerFunc:) userInfo:nil repeats:YES];
//定时器,每隔1秒调用myFunction,  userInfo表示参数

-(void)timerFunc:(id)arg//要有(id)类型的参数
{
}
while(1)//timer之后为定时器可向下走
{
	[[NSRunLoop currentRunLoop] run];
}
//---category
像JS的prototype

把一个类的实现分放在不同文件中,实现方法不被外界仿问,类似于private
命名规范, 文件命名:    要扩展类名+扩展方法

如NSString+MyReverse.h
 @interface NSString(MyReverse)//要加( )才是category,里只可加方法,不能加属性
-(id) myreverse;
 @end

@implementation NSString(MyReverse)
-(id) myreverse
{
	NSUInteger len=[self length];//length属性
	NSMutableString * str=[NSMutableString stringWithCapacity:len];//stringWithCapacity 方法
	while(len>0)
	{
		unichar c=[self characterAtIndex:--len];//characterAtIndex 方法 , unichar 类型
		NLog(@"c is %C",c);//%C 是Unicode
		NSString *s=[NSString stringWithFormat:@"%C",c];//stringWithFormat 方法
		[str appendString:s];//appendString 方法
	}
	return str;
}
@end

NSString * str=@"中华人民共和国";
NSString * res=[str myreverse];//GnuSetup测试不行
NSLog(@"the myreverse result is:%@	",res);

//下面部分写在.m文件,而不写在.h文件中,实现方法私有
@interface My(Private)
-(void)privateMethod;
@end
 
@implementation My(Private)
 -(void) privateMethod
{
	printf("execute in privateMethod \n");
}
@end
//头文件中没有,也可调用,但要能猜到,只是写的时候不提示

//---blocks
//GnuSetup不支持^

 int(^OFunc)(int a);// 指向函数的指针  的 * 换为^
 OFunc(100);//调用方法
 
 typedef int (^MaxFunc)(int a,int b);
 block中对于默认的auto变量是只读的   ,其它的 register,static
 __block 关键字,变量成为全局范围的

//---
void (^myblock)(void)=NULL;
myblock=^(void)//实现
		{
			NSLog(@"in block method");
		};//要有;
NSLog(@"before invoke myblock");
myblock();//调用
NSLog(@"after invoke myblock");


//---
__block int sum=0;//全局变量
int (^myblock2)(int a,int b)=^(int a,int b)
		{
			
			sum=a+b;//不能仿问外部的auto变量,要定义为__block
			int c=a+b;
			return c;
		};
int res=myblock2(10,20);
NSLog(@"myblock2 ressult is %d ,sum is %d ",res,sum);


//---
typedef int (^SumBockT)(int a,int b);//和C类似
SumBockT myblockT=^(int a,int b)
				{
					int c=a+b;
					return c;
				};
int typeRes=myblockT(10,20);
NSLog(@"myblock2 typeRes is %d",typeRes);

[myObj myMethod:myblockT];//传实名
//------
void (^positionCallBack)(int x,int y);//声明block变量 ,
-(void) setPositionCallBack(  void (^)(int x,int y)  )callback;//声明函数参数为block,

-(void)dealloc
{
	printf("Computer dealloc! \n");
	[super dealloc];
	[positionCallBack release];
}
-(void) setPositionCallBack(  void (^)(int x,int y)  )callback
{
	[positionCallBack release];//也要release
	positionCallBack=[callback copy];//copy为防止销毁
}

[_computer setPositionCallBack:^(int x,int y){  //传递匿名函数实现
				printf("x=%d,y=%d",x,y);
			}];


if(positionCallBack)//block可以做判断
	positionCallBack(100,count);//调用

blocks还有很多其它的东西,
因完全兼容C,也可以使用C的函数指针


@proterty(nonatomic,strong)  nonatomic表示不考虑线程安全,默认是线程安全的,strong表示强引用

//------异常
//GNUSetup要加 -fobjc-exceptions
@try{
//...
}@catch(NSException * exception)
{
	NSLog(@"---NSException Reason is:%@",[exception reason]);
}@finally {  
	NSLog(@"---finally--done");
}
//好像不能有多个@catch,还有@throw

//==============================Foundation.h

//-----NSString
 NSString* hello=@"hello";
NSString* same=[NSString stringWithString:hello ];
NSLog(@"same =%@",same);

NSString* format=[[NSString alloc]initWithFormat:@"%@ length is:%d",@"lisi",[@"lisi" length]];//这里用,分隔
NSLog(@"format =%@",format);

NSString* lower=[@"LOCWER ME" lowercaseString];
NSString* capitalized=[@"good" capitalizedString];
NSLog(@"lower =%@",lower);
NSLog(@"capitalized =%@",capitalized); 

//对齐
NSString* username=[@"zhangsan" stringByPaddingToLength:20 withString:@"_" startingAtIndex:0];//GNUsetup,加在字串尾
NSLog(@"username=%@",username);
NSString* password=[@"123" stringByPaddingToLength:20 withString:@"_ " startingAtIndex:1 ];//startingAtIndex是从withString中第几个开始
NSLog(@"password=%@",password);

BOOL prefix=[@"http://www.sina.com.cn" hasPrefix:@"http"];
BOOL suffix=[@"http://www.sina.com.cn" hasSuffix:@".cn"];
NSLog(@"prefix=%d",prefix);
NSLog(@"suffix=%d",suffix);

//----------NSMutableString
NSMutableString * mutableStr2=[[NSMutableString alloc]initWithCapacity:50];
[mutableStr2 release];
NSMutableString * mutableStr1=[NSMutableString stringWithCapacity:40];
[mutableStr1 appendFormat:@"%@ hello",@"lisi"];//可变的
NSLog(@"mutableStr1 appendFormat=%@",mutableStr1);

[mutableStr1 appendString:@" !!!"];
NSLog(@"mutableStr1 appendString=%@",mutableStr1);

[mutableStr1 deleteCharactersInRange:NSMakeRange(mutableStr1.length - 3,3) ];//C风格的建立类NSMakeRange,第一个是位置,第二个是长度
NSLog(@"mutableStr1 deleteCharactersInRange=%@",mutableStr1);

[mutableStr1 insertString:@" world"  atIndex:[mutableStr1 length] ];
NSLog(@"mutableStr1 insertString=%@",mutableStr1);

[mutableStr1 replaceCharactersInRange:NSMakeRange(mutableStr1.length - 5,5) withString:@"good"];
NSLog(@"mutableStr1 insertString=%@",mutableStr1);

[mutableStr1 replaceOccurrencesOfString:@"lisi" withString:@"wang" options:NSCaseInsensitiveSearch range:NSMakeRange(0,mutableStr1.length)];
NSLog(@"mutableStr1 replaceOccurrencesOfString=%@",mutableStr1);

[mutableStr1 setString:@"this is new string"];
NSLog(@"mutableStr1 setString=%@",mutableStr1);

//----------NSArray
NSArray *array1  = [NSArray array];//一个空的
NSArray *array2  = [NSArray arrayWithArray:array1];

NSString* item=[NSString stringWithFormat:@"Good"];
         
NSDate *date=[NSDate date];//期时间
NSNumber *value=[NSNumber numberWithInt:32];

NSArray *array;
array = [NSArray arrayWithObjects: @"I", @"am",item,date,value, nil];//可任何对象类型
NSLog(@"array =%@",array);//NSArray可用%@


BOOL is=[array containsObject:@"am"];
NSLog(@"array containsObject? =%d",is); //打印1

int count=[array count ];
NSLog(@"array count=%d",count); 

id  last=[array lastObject ];
NSLog(@"array last=%@",last); //id可用%@

id  indexObj=[array objectAtIndex:1 ];
NSLog(@"array indexObj=%@",indexObj);

NSUInteger index1=[array indexOfObject:@"Good" ]; 
NSLog(@"array index1=%d",index1); 

NSUInteger index=[array indexOfObjectIdenticalTo:item ];//NSUInteger类型,因NSString是常量,相同串不会建多个(同Java),要是相同的内存对象,不是用isEqual方法做判断
NSLog(@"array index=%d",index); 

NSArray *one  = [NSArray arrayWithObjects: @"Bill",nil];
NSArray *two  = [NSArray arrayWithObjects: @"Bill",nil];
NSLog(@"one == tow?,%d",[one isEqualToArray:two]);

NSArray* students=[NSArray arrayWithObjects:@"Tom",@"Bill",nil];
NSString *strAllStudent=[students componentsJoinedByString:@","];
NSLog(@"joined is ,%@",strAllStudent);

         //----------NSMutableArray
 NSMutableArray *mutArray2 = [[NSMutableArray alloc]initWithCapacity:10];
 [mutArray2 release];
  
  NSMutableArray *mutArray1 = [NSMutableArray arrayWithCapacity:10];
 [mutArray1 addObject:@"Lisi"];
 [mutArray1 addObject:@"Wang"];
 [mutArray1 addObject:@"zhao"];
 NSLog(@"mutable array is ,%@",mutArray1);

 [mutArray1 addObjectsFromArray:students];
 [mutArray1 insertObject:@"abc" atIndex:2];
 NSLog(@"mutable array addObjectsFromArray is ,%@",mutArray1);
 
 [mutArray1 removeLastObject];
 [mutArray1 removeObject:@"Lisi"];
 [mutArray1 removeObjectAtIndex:0];
 [mutArray1 removeObjectIdenticalTo:@"Tom"];
  NSLog(@"mutable array after remove is ,%@",mutArray1);
 
 NSArray *toRemove =[NSArray arrayWithObjects:@"abc",@"Wang",nil];
 [mutArray1 removeObjectsInArray:toRemove];
 NSLog(@"mutable array arrayWithObjects is ,%@",mutArray1);
 
 NSArray *myData=[NSArray arrayWithObjects:@"One",@"two",@"three",@"four",@"five",nil];
 NSMutableArray *mutArray3 = [NSMutableArray arrayWithArray:myData];
 [mutArray3 removeObjectsInRange:NSMakeRange(2, 2)];
 NSLog(@"mutable array removeObjectsInRange is ,%@",mutArray3);
 
 [mutArray3 replaceObjectAtIndex:1 withObject:@"replaced"];
 NSLog(@"mutable array replaceObjectAtIndex is ,%@",mutArray3);
         
 [mutArray3 removeAllObjects];
 [mutArray3 setArray:myData];
  NSLog(@"mutable array removeAllObjects,setArray is ,%@",mutArray3);



//------集合类
//---
NSDictionary* dic1=[NSDictionary dictionary];
NSDictionary* dic2=[NSDictionary dictionaryWithObject:@"Bill" forKey:@"id_1"];
NSDictionary* dic3=[NSDictionary dictionaryWithDictionary:dic2];
NSLog(@"dic3 is :%@",dic3);

NSArray *objs = [NSArray arrayWithObjects: @"lisi", @"wang", nil];
NSArray *keys = [NSArray arrayWithObjects: @"id_2", [NSNumber numberWithInt:33], nil];
NSDictionary* dic4=[NSDictionary dictionaryWithObjects:objs forKeys:keys];//s
NSLog(@"dic4 is :%@",dic4);

NSDictionary *dic5 = [NSDictionary dictionaryWithObjectsAndKeys: @"value1", @"key1", @"value2", @"key2", nil]; // 动态参数
NSLog(@"dic5 is :%@",dic5);
keys=[dic5 allKeys];
NSLog(@"keys is :%@",keys);

id obj=[dic5 objectForKey:@"key2"];
NSLog(@"obj is :%@",obj);

//--------
NSMutableDictionary *mutableDict1 = [NSMutableDictionary dictionary];
NSMutableDictionary *mutableDict2 = [NSMutableDictionary dictionaryWithCapacity:10];
[mutableDict2 setObject:@"key1"   forKey: @"hello"];
[mutableDict2 setObject:@"123"   forKey: @"lisi"];
NSLog(@"mutableDict2 is :%@",mutableDict2);//这里key1怎么显示的是后面addEntriesFromDictionary修改的?????

[mutableDict2 addEntriesFromDictionary:dic5];
NSLog(@"mutableDict2 is :%@",mutableDict2);

[mutableDict2 setDictionary:dic5];
NSLog(@"mutableDict2 is :%@",mutableDict2);

[mutableDict2 removeObjectForKey:@"key1"];
NSLog(@"mutableDict2 is :%@",mutableDict2);

[mutableDict2 removeObjectsForKeys:keys];
NSLog(@"mutableDict2 removeObjectsForKeys is :%@",mutableDict2);
[mutableDict2 removeAllObjects];
//------ 
NSString * s=@"Student name:Hyman";
NSLog(@"s length %d",[s length]);

NSRange r =NSMakeRange(13,5);
r.location=13;
r.length=5;

NSLog(@"r.location %d",r.location);
NSLog(@"r.length %d",r.length);
NSLog(@"%@",[s substringWithRange:r]);

NSLog(@"%2$d years old is %1$@",@"Johne",25);//2$表示使用参数表中的第二个参数
//------文件IO操作
	NSFileManager *manager;
	manager = [NSFileManager defaultManager];

	NSString *home;
	home = [@"~" stringByExpandingTildeInPath];//在GNuSetup中的msys上  ~ 是C:\Documents and Settings\xxuser,会递归所有子目录

	NSDirectoryEnumerator *direnum;
	direnum = [manager enumeratorAtPath: home];

	NSMutableArray *files;
	files = [NSMutableArray arrayWithCapacity: 42];

	NSString *filename;
	while (filename = [direnum nextObject]) {
		if ([[filename pathExtension]  isEqualTo: @"jpg"]) {
			[files addObject: filename];
		}
	}
	/*
	//方式一
	NSEnumerator *fileenum;
	fileenum = [files objectEnumerator];

	while (filename = [fileenum nextObject]) {
		NSLog (@"%@", filename);
	}
	*/
	//方式二
	for (NSString *filename in files) {
			NSLog (@"%@", filename);
	}



NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
[dateFormatter setDateFormat:@"yyyy-MM-dd 'at' HH:mm"];

NSNumberFormatter *numberFormatter = [[[NSNumberFormatter alloc] init] autorelease];
NSNumber * number = [NSNumber numberWithDouble:-012.95];
[numberFormatter setFormat:@"$#,###.00;0.00;($#,##0.00)"];//;分隔的部分: 正数,0,负数
NSLog(@"____: %@", [numberFormatter stringFromNumber:number]);

	
//------Security 证书,加密
//--Coder
	protocol <NSCoding>
- (void) encodeWithCoder: (NSCoder *) coder {//是 NSCoding Protocol中的
    [coder encodeObject: name
           forKey: @"name"];
    [coder encodeInt: magicNumber
           forKey: @"magicNumber"];
}
- (id) initWithCoder: (NSCoder *) decoder {//是 NSCoding Protocol中的
    if (self = [super init]) {
        self.name = [decoder decodeObjectForKey: @"name"];
        self.magicNumber = [decoder decodeIntForKey: @"magicNumber"];
    }
    return (self);
}
- (NSString *) description { //是 NSObject Protocol 中的,当以"%@"输出时调用
}
NSData *freezeDried;
freezeDried = [NSKeyedArchiver archivedDataWithRootObject: thing1];//会调用encodeWithCoder方法
thing1 = [NSKeyedUnarchiver unarchiveObjectWithData: freezeDried];//会调用initWithCoder方法
//------与C/C++ 一起使用
wchar_t* convertNSStringToWChar(NSString* strName)//要free(),不能有%d ???
{
    const char  *cString;
    cString = [strName cStringUsingEncoding:NSUTF8StringEncoding];
    //char转wchar_t
    setlocale(LC_CTYPE, "UTF-8");
    int iLength = mbstowcs(NULL, cString, 0);
    wchar_t *stTmp = (wchar_t*)malloc((iLength +1)*2);
    memset(stTmp, 0, (iLength +1)*2);
    mbstowcs(stTmp, cString,iLength +1);
    stTmp[iLength] = L'\0';
    return stTmp;
}
NSString *converWCharToNSString(const wchar_t*  wchar)
{
    //wchar_t在mac intel下就是UTF-32LE，所以
    int len=(wcslen(wchar)+1)*4;//12,即3*4=12
    NSString *str = [[NSString alloc] initWithBytes:wchar length:len encoding:NSUTF32LittleEndianStringEncoding];
    return str;
}

//------基本图形 Quartz


//------多线程
NSCondition *ticketCondition;
int tickets = 100;
int count= 0;
+ (void)run{
    while (TRUE) {
        [ticketCondition lock];//正常,只可一个线程得到lock
        if(tickets > 0){
            [NSThread sleepForTimeInterval:0.5];
            count = 100 - tickets;
            NSLog(@"当前票数是:%d,售出:%d,线程名:%@",tickets,count,[[NSThread currentThread] name]);
            tickets--;
        }else{
            break;
        }
        [ticketCondition unlock];
    }
}

 [NSThread sleepForTimeInterval:0.5];
     ticketCondition = [[NSCondition alloc] init];
    NSThread *ticketsThreadone = [[NSThread alloc] initWithTarget:[MyObject class] selector:@selector(run) object:nil];
    [ticketsThreadone setName:@"Ticket-1"];
    [ticketsThreadone start];

    NSThread *ticketsThreadtwo = [[NSThread alloc] initWithTarget:[MyObject class] selector:@selector(run) object:nil];
    [ticketsThreadtwo setName:@"Ticket-2"];
    [ticketsThreadtwo start];
    //[NSThread detachNewThreadSelector:@selector(run) toTarget:self withObject:nil];
    
    //主线程等子线程
    while(1){
        [[NSRunLoop currentRunLoop] run];
    }
    
    
    
//------Socket网络
- (void) send_get {
	NSString *address = [NSString stringWithFormat:@"http://www.baidu.com"];
	NSURL *url = [NSURL URLWithString:address];
	
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
	[request setHTTPMethod:@"GET"];
    
    //异步 NSURLConnectionDelegate
    [NSURLConnection connectionWithRequest:request delegate:self ];//方式一
	//NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];//方式二
	if (data == nil) {
		data = [[NSMutableData alloc] init];
	}
	//[connection release];
    NSLog(@"----main method done.");
}

- (void) connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	[data setLength: 0];
}
- (void) connection:(NSURLConnection *)connection didReceiveData:(NSData *)inData
{
	[data appendData:inData];
}
- (void) connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
	[data release];
    NSLog(@"error = %@", error);
}
- (void) connectionDidFinishLoading:(NSURLConnection *)connection
{
	if (data != nil && data.length > 0) {
		NSString *value = [[[NSString alloc] initWithBytes:[data bytes] length:data.length encoding:NSUTF8StringEncoding] autorelease];
		NSLog(@"%@", value);
	}
	[data release];
}

    //  @WebServlet(urlPatterns={"/myPost"})
    //------POST 
    /*  
    NSURL *url = [NSURL URLWithString:@"http://127.0.0.1:8080/test/myPost"];
	NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] initWithURL:url] autorelease];
	[request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	[request setHTTPMethod:@"POST"];
    NSString *body = @"message=你好";
	NSData *postData = [[[NSData alloc] initWithBytes:[body UTF8String] length:[body length]] autorelease];//方式1 
	[request setHTTPBody:postData];
	//[request setHTTPBody:[httpBodyString dataUsingEncoding:NSUTF8StringEncoding]];
    
	NSHTTPURLResponse *response = nil;
	NSError *error = nil;
    
	[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];//同步
	
    NSLog(@"Status Code: %d", [response statusCode]);
    */
    //-----------POST 
  /*
    
    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] init] autorelease];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
   
    
    [request setURL:[NSURL URLWithString:@"http://127.0.0.1:8080/test/myPost"]];
    
    NSString *post  = [[NSString alloc] initWithFormat:@"message=%@",@"hello,world."];
  
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];//方式2
    [request setHTTPBody:postData];
     //[request setHTTPBody:[httpBodyString dataUsingEncoding:NSUTF8StringEncoding]];
    NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
  
  
    NSHTTPURLResponse *response = nil;
	NSError *error = nil;
    NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];//同步
    NSLog(@"Status Code: %d", [response statusCode]);
    
    [post release];
     */
   
    //文件上传 POST
    //------Get OK
    /* 
    MyHttpGet *get=[[MyHttpGet alloc]init];
    [get send_get];
   
    while(1)//为异步
    {
        [[NSRunLoop currentRunLoop] run];
    }
    */
    //-----Dictionary -> JSON  OK
    NSDictionary *song = [NSDictionary dictionaryWithObjectsAndKeys:@"i can fly",@"title",@"4012",@"length",@"Tom",@"Singer", nil];
    if ([NSJSONSerialization isValidJSONObject:song])
    {
        NSError *error;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:song options:NSJSONWritingPrettyPrinted error:&error];
        NSString *json =[[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        NSLog(@"json data:%@",json);
    }
    //----
//    NSURL * url=[NSURL URLWithString:@"http://127.0.0.1:8080/test/myJSON"];//Fail
//    NSData *data = [NSData dataWithContentsOfURL:url];
//----
    NSString *str=@"{ \"title\" : \"i can fly\",\"Singer\" : \"Tom\",\"length\" : \"4012\" }";
    NSData *data = [[[NSData alloc] initWithBytes:[str UTF8String] length:[str length]] autorelease]; //OK
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
    if (json == nil) {
        NSLog(@"json parse failed,error= %@\n",[error description]);
        return -1;
    }
    NSArray *songArray = [json objectForKey:@"Singer"];
    NSLog(@"song collection: %@\r\n",songArray);
    
//------XML,XSLT
