gopher  囊地鼠
2009年11月 GO语言第一个版本发布。
2012年3月 第一个正式版本Go1.0发布。

https://golang.org/  要翻墙才能出去(Google 的语言) 
支持多个平台,windows ,linux(有linux ARMv6),mac,freeBSD

https://golang.google.cn 国内可以仿问的
https://gomirrors.org/ ( https://goproxy.io/ )

https://golang.google.cn/pkg/ 文档基本上每个 方法都有示例代码，非常好

https://www.manning.com/books/go-web-programming 是GO Web 编程书的源码 ，还有其它很多书的源码 



windows 1.14 安装后会自动设置系统环境变量
PATH 增加 C:\Go\bin
#GOROOT=C:\Go

go version
go env

执行速度和C相近,不是纯面向对象的，没有class,可以模拟继承

支持高并发(协程coroutine(协同程序),GO语言命名为Goroutine，又称微线程，使用go关键字启动，一台机器可以启动成千上万的协程,靠channel实现)，自动利用多核CPU
拉圾回收，支持函数返回多个值，没有try...catch功能，直接支持UTF-8
有未使用的导入，会报错


函数或if后的{不能单独放一行(不爽)!!!  分号可有可无 , if 后的()可有可无 ,if后的{不能省略


----
gRPC 框架为HTTP/2而设计,使用protobuf进行序列化,类似Dubbo,支持众多的开发语言,协议写在了.proto文件,形式像CORBA的idl文件
protobuf 


支持reflect,http,json, 调用C
MongoDB 官方带 Go Driver
ZeroMQ 官方介绍github上的三个项目

MySQL 官方不提供Go的Connector
RabbitMQ 官方不提供Go的Connector
redis/kafka Go支持 都是github上的
读写excel 或 生成pdf 使用github项目

kubernetes的restful使用的是github上的 https://github.com/emicklei/go-restful

动态网页不行??? 只能 restful 前后端分离 ???  ,表单提交,文件上传 下载(http 包???),mvc框架???
依赖管理服务??国内的网络有防火墙?? (go module ) 

没有跨平台的图表界面 ??? 还是linux的GTK ??? 主要用于后台服务

依赖为何都是要源码,全是github或者google.golang.org上的 ，为何没有类似.a包可以依赖?? 没有中央仓库管理.a包吗??

是否可以远程调试


对于go关键字的多协程的调试，并发测试，vscode和goland都不是很好,打的断点并不一定会停止

----Eclipse 4.6 以上插件 goClipse 依赖于 CDT 9.0以上  只安装 Main Feature组即可
As of 2017, Goclipse is no longer actively maintained

http://goclipse.github.io/
https://github.com/GoClipse/goclipse/blob/latest/documentation/Installation.md   下载


https://github.com/GoClipse/goclipse.github.io/archive/master.zip   
 解压后eclipse中help->install-> Add->Local 指向release目录　
	2019-02下载的是0.16.1v201607版本
    2020-05下载的是0.16.1v201607版本 太老了
	
help->install-> 写入	http://goclipse.github.io/releases/ 也是  0.16.1v201607版本

eclipse Marketplace

preferences->go->Installation 设置目录(同GOROOT) C:\Go
用goClipse,如有包源文件要放相应目录下,会在bin目录下生成.exe来执行

项目编码要为UTF-8，文件也是

但在goClipse中 右击文件 run as -> go applcation ,如是在src目录下不能运行,必须在src目录中子目录才行

debug时依赖gdb命令 ,安装Cygwin的gdb后,调试有时中文乱码??? 控制台显示有缓存，显示顺序有问题 ???
----eclipse插件 CodeMix (把vscode中的功能移到eclipse中)
----LiteIDE
有百度网盘，像是国产的，界面有点像eclipse，其实没有任何关系使用Qt开发的
打断点，只能点工具栏按钮，不太方便
工具栏的BR表示BuildRun不能Debug, 要Build->Start Debug(F5) ,Debug->step over(F10)  启动调试后，有工具按钮在下方

Tools->Manage GOPATH/moudles...-> system GOPATH 有显示，修改了环境变量要重启LiteIDE，也可在use Custom GOPATH中增加自己的

如一个目录有两个文件都有main函数是不行的

debug时不能鼠标滑过看变量，必须在watch 中手工增加变量，不方便

terminal  窗口中的文字 不能复制粘贴，不方便 


---GoLand 2018.2.4 (IntellijIdea) 收费
	Settings-> Go -> GoPATH 下有 global GOPATH (默认是~/go) 配置 
	
	右击 xx_test.go -> create TestXX (方法名) in xx.go(文件名) ,
	如运行报找不到方法, Settings-> Go -> GoPATH 下project GOPATH ,增加要把项目目录(代码要在src目录下)
	debug时不依赖gdb
	
	右击文件Run->打开运行配置框，Run Kind 默认是package,可以选File，还有Directory
	 
	
	可以读到标准输入
	可以同时调试两个文件

---Visual Studio Code(vscode) 的Go  Extension
	go env -w GOPROXY=https://goproxy.cn,direct 
	go env -w  GO111MODULE=on  表示不下载源码 %USERPROFILE%\go\src下没有新增,%USERPROFILE%\go\pkg\mod(也有源码)和%USERPROFILE%\go\bin有新增

	打开.go文件时  
	提示 gopls 命令找不到,使用 go get -v  golang.org/x/tools/go/gopls 被墙了,如设置了go env -w GOPROXY=  也是可以的
		https://github.com/golang/tools/tree/master/gopls
		cd tools-master\go\gopls 执行 go install 
		
		#又提示要github.com/jba/templatecheck@v0.5.0 (https://github.com/jba/templatecheck)
		#下载  "https://proxy.golang.org/github.com/jba/templatecheck/@v/v0.5.0.mod"  失败
		#go env -w GOPROXY=https://goproxy.cn,direct 再来
	提示 go-outline  命令找不到,使用  go get -v github.com/ramya-rao-a/go-outline 安装,如设置了go env -w GOPROXY=  也是可以的
		 使用vscode的install按钮是可以的
		( 
			就一个main.go文件
			又提示 不认导入路径  golang.org/x/tools/go/buildutil , get连接失败，被墙了
			https://github.com/golang/tools/tree/master/go/buildutil 中有
			项目下载下来,cd tools-master\go\buildutil 执行 go install  
			再go get -v github.com/ramya-rao-a/go-outline 就可以了, ~\go\bin下就有go-outline.exe
		)
	
	#打开.go文件 -> 调试启动按钮 提示 "dlv" 找不到.使用  go get -v github.com/go-delve/delve/cmd/dlv 安装,~\go\bin下就有dlv.exe
	#	(//上可以成功，这部分不用
	#		https://github.com/go-delve/delve/tree/master/cmd/dlv  是可浏览的
	#		也可下载后进入目录  go install
	#	)
	新版本用 dlv-dap，支持远程调试，使用是一样的
	
	
	
	#写代码时 类.时  提示 gocode 找不到 ,  go get -v github.com/uudashr/gopkgs/v2/cmd/gopkgs  安装 
	
	
	
	安装后,Debug视图->create a launch.json file 链接->Go: Launch file 或者Go: Launch package, 生成.vscode/launch.json
	{ 
		"version": "0.2.0",
		"configurations": [
			  {
				"name": "Launch file",
				"type": "go",
				"request": "launch",
				"mode": "debug",
				"program": "${file}"
			}
		]
	}
	当Go: Launch package时，一个目录（工作区）只可有一个main函数 
	  {
            "name": "Launch Package",
            "type": "go",
            "request": "launch",
            "mode": "debug",
            "program": "${workspaceFolder}"
       }
	   
	debug时不依赖gdb 
	go help get ,显示 -v verbose,-u update

同时debug两个go文件(如服务端和客户端),调用一个进行中,再启动另一个调试,CALL STACK 视图显示多个进程 不行????
 launch.json中增加如下,测试不行???
  { 
    "version": "0.2.0",
    "configurations": [
		{
            "name": "Client",
            "type": "go",
            "request": "launch",
            "mode": "debug",
            "program": "${workspaceFolder}/src/adv/mynet/myrpc_client.go"
        },
        {
            "name": "Server",
            "type": "go",
            "request": "launch",
            "mode": "debug",
            "program": "${workspaceFolder}/src/adv/mynet/myrpc_server.go"
        }
    ],
    "compounds": [
        {
            "name": "Server/Client",
            "configurations": [
                "Server",
                "Client"
            ],
            //"preLaunchTask": "${defaultBuildTask}"
        }
    ] 
	只能文件在不同目录，启动两个vscode来做

 

Go不是在terminal窗口中运行，而是在Debug Console 窗口运行，所以读不到标准输入？？
可能lauch.json中为 "type": "go",如C++的 为"cppdbg"是在terminal窗口中运行的

fmt.Printf("请输入两个数\n")
var x, y int
fmt.Scan(&x, &y)
	
import (
	"bufio"
	"fmt"
	"os"
)
fmt.Print("请输入")
in:=bufio.NewReader(os.Stdin)
str,_:=in.ReadString('\n'); //这里才可开始读,以\n做为结束
fmt.Print(str)




Settings->Extension->go-> 搜索gopath中有Go:Gopath 下方有Edit in settins.json链接（提示覆盖环境变量），点击进入
C:\Users\<user>\AppData\Roaming\Code\User\settings.json 文件，增加了 "go.gopath": ""
提示还有一个Go:Infer(推断，推理;) Gopath,只有当Go:Toggle workspace trust flag

运行也会自动保存 
	Task:Save Before Run默认为always,修改为never,无效？
	Go:build on save 默认是package修改为off,无效？
	
保存时自动格式化注释，
		Editor:Format On Save 默认是没有选中的，但还是能在保存时自动格式化注释，如何取消？
		%USERPFORILE%\AppData\Roaming\Code\User\settings.json 增加  "editor.formatOnSave":  false 也无效
		
一保存就删除错误的导入，取消？？
原来是 Go: Format Tool  配置的作用，没办法不设置,至少选择一个

---

gofmt 用来格式化代码 


gccgo-9  依赖于 libgo14
gccgo-9  ./hello.go -o hello 如不加 -o 默认输出 a.out

go-9 version 显示go version go1.12.2
使用同google 的 go, 如 go-9 ./hello.go
 
linux下的 go 和 go1.9 软件包的描述为 A compiled, garbage-collect
 
---hello.go
package main //main函数所在文件的包名必须是main,同一文件夹的go文件包名必须全一样,建议用文件夹名

import "fmt"

func main() { //这个括号不能换行
   fmt.Println("Hello, World!")
}

linux GCC也可以编译GO

go run hello.go  来运行
go run -n hello.go 显示详细日志，有建立目录$WORK/xx,执行compile命令,buildid命令,link命令生成.a归档文件,.exe文件
go run -work hello.go 可以看到WORK的值，即使用的目录 %TMP%/go-buil-xxx目录

go build hello.go  当前目录下生成.exe文件(有main方法的),可使用-o 修改输出目录，如不指定文件名，就是当前目录下全部
#如go build所在目录没有main方法，只是检查有效性，不生成任何东西

go install 如有main会把.exe放在$GOPATH/bin目录下，如没有main生成$GOPATH\pkg\windows_amd64\包名\xx.a (GoLand工具是项目的bin,go env显示项目目录是在GOPATH的第一个的值)
对于同一个包中，如有多个同名方法，不能执行go build 和go install，但可以分别go run要求有main,
go install -n
go install -work 每次都是变的

go get -x github.com/xxx/xx 能看到有用git clone命令
go doc net/http 显示所有声明
go doc fmt Printf 某个函数
go list 列出当前安装的包
go fmt 格式化代码


工作区目录默认为 $HOME/go
可使用 GOPATH 环境变量做修改(不能是安装目录)，模块下载(go get命令)在此目录下的 pkg/mod/ 目录中
go env GOPATH
export GOPATH=$(go env GOPATH)
#export GOPATH=H:/GO_PATH

go env 查看有
set GOENV=C:\Users\dell\AppData\Roaming\go\env 表示这个文件(unix式的\n换行)记录了 go env -w 的值 

$ mkdir -p $GOPATH/src/github.com/user/hello 
cd $GOPATH/src/github.com/user/hello 
---vi hello.go
package main

import "fmt"

func main() {
	fmt.Println("Hello, world.")
}
---  
$ go install github.com/user/hello 可以其它地方运行，使用$GOPATH变量，会生成 $GOPATH/bin/
也可 cd $GOPATH/src/github.com/user/hello  再 go install 



$ mkdir $GOPATH/src/github.com/user/stringutil 
cd $GOPATH/src/github.com/user/stringutil 
--vi reverse.go 
//包名和目录名建议相同,这里不同
package stringutil1 

func init(){ //init函数在导入包，GO语言自动调用，做初始化工作，和main参数一样，不能有参数和返回值
	//每个包中都可以有init,可以重复出现,顺序是从上到下，同一文件夹按文件名排序，不同包按导入顺序
	//如main依赖A ->B -> C ,执行顺序为C->B->A->main
 	fmt.Println("stringutil1包Init函数初始了")
}
func init(){
	fmt.Println("stringutil1包Init函数初始2")
}

//大写开头 表示可被其它包导入使用，小写开头表示，只为自己包使用
func Reverse(s string) string {
	r := []rune(s)
	for i, j := 0, len(r)-1; i < len(r)/2; i, j = i+1, j-1 {
		r[i], r[j] = r[j], r[i]
	}
	return string(r)
}
type Persion struct {
	Name string
	age int //小写开头包外不可见
}

任何地方 go build github.com/user/stringutil
或者在 源码目录中 go build
不产生新文件 ，把编译好的包放在本地的build cache
---vi hello.go
package main //可执行的包名必须是main
import (
	 f "fmt" //导入别名
	"github.com/user/stringutil" //就可引用刚刚的库,是文件夹名,应该放在 $GOPATH/src/github.com/user/stringutil
	// _ "github.com/user/stringutil" //表示只为执行包中的init函数
) 
func main() {  //这个括号不能换行
	f.Println(stringutil1.Reverse("!oG ,olleH"))//引用的是包名
	
	var p=stringutil1.Persion{Name:"lisi"}//只能键传值
	f.Printf("name=%s\n ",p.Name)
	//f.Println(" age=%d",p.age)//age访问不到
	
}
func init(){
	f.Println("main包Init函数初始")//在main方法前执行
}


---  
-- vi $GOPATH/src/github.com/user/stringutil/reverse_test.go  文件名以 _test.go 结尾 
package stringutil

import "testing"  //包名是testing

func init(){
	fmt.Println("stringutil1包Init函数初始Test")// xx_test.go 不会被main调用
}

//另一种函数类型为 func BenchmarkXxx(b* testing.B)
func TestReverse(t *testing.T) {  //函数名TestXxx，参数t *testing.T
	if testing.Short() { //结合 -short选项
		t.Skip("跳过测试")
	}
	cases := []struct {
		in, want string
	}{
		{"Hello, world", "dlrow ,olleH"},
		{"Hello, 世界", "界世 ,olleH"},
		{"", ""},
	}
	for _, c := range cases {
		got := Reverse(c.in)
		if got != c.want {
			t.Errorf("Reverse(%q) == %q, want %q", c.in, got, c.want)
		}
	}
}


--
set GO111MODULE=off
任何地方 go test github.com/user/stringutil
或进入目录 go test

go test github.com/user/stringutil -v -cover -short 
-parallel 2




//------------示例代码

//如一行多写条语句用;分隔
	var a = "中文"
	var b string = "english" //类型前没有:
	var c bool
	println(a, b, c) //变量被使用

	var myint = 10
	myint2 := 10 //前面不能是已经定义的变量

	println(myint, myint2)

	//--data type
	var myint8 int8 = 10
	var myint16 int16 = 10
	var myint32 int32 = 10
	var myint64 int64 = 10
	println(myint8, myint16, myint32,myint64)

	var myuint8 uint8 = 10
	var myuint16 uint16 = 10
	var myuint32 uint32 = 10
	var myuint64 uint64 = 10
	println(myuint8, myuint16, myuint32,myuint64)

	var myfloat32 float32 = 10
	var myfloat64 float64 = 10
	println(myfloat32, myfloat64)//+1.000000e+001　,也有计算不准的情况

	var mybyte byte = 0x20 //类似 uint8
	var myrune rune = 10//类似 int32
	var myuint uint=10;//32 或 64 位
	var myint3 int=10;//与 uint 一样大小


	println(mybyte, myrune,myuint,myint3)

	//var myptr uintptr=&myuint;
	//println(myptr)

	var x,y=20,20
	println(x, y)

	var x1,y1 uint16 =30,30
	println(x1, y1)

	a1,b1:=4,5 //只能出现在函数中
	println(a1, b1)

	const a2,b2,c2=2,4,"22" //常量

	var //一般用于声明全局变量
	(
		width uint32
		height uint32
	)
	println(width, height)
	//string 属于值类型

	//常量还可以用作枚举：
	const (
		Unknown = 0
		Female = 1
		Male = 2
	)

	const (
		myio = iota //0
		myio2 //1
		myio3 //2
		myioR = iota  //3
		myioR1//4

	)
	fmt.Println(myio,myio2,myio3,myioR,myioR1)

	const (
		i=1<<iota
		j=3<<iota
		k // 3<<2
		l // 3<<3。
	)
	fmt.Println("i=",i) //1
	fmt.Println("j=",j) //6
	fmt.Println("k=",k) //12
	fmt.Println("l=",l)//24
//&取址，*指针，有struct 同C ,有goto,只有for循环

   //switch
	var marks int = 90
	var grade string = "B"
	switch marks {
		case 90: grade = "A"
		case 80: grade = "B"
		case 50,60,70 : grade = "C"
		default: grade = "D"
	}

	switch {
	case grade == "A" :
		fmt.Printf("优秀!\n" )
	case grade == "B", grade == "C" :
		fmt.Printf("良好\n" )
	case grade == "D" :
		fmt.Printf("及格\n" )
	case grade == "F":
		fmt.Printf("不及格\n" )
	default:
		fmt.Printf("差\n" );
	}
	fmt.Printf("你的等级是 %s\n", grade );
   
   var xIterface interface{}
	switch i := xIterface.(type) {  //强转为type，表示可用switch判断类型，后也可用i继续调用子类
	case nil: //匹配nil
		fmt.Printf(" x 的类型 :%T", i)//%T类型
	case int:
		fmt.Printf("x 是 int 型")
	case float64:
		fmt.Printf("x 是 float64 型")
	case func(int) float64:
		fmt.Printf("x 是 func(int) 型")
	case bool, string:
		fmt.Printf("x 是 bool 或 string 型")
	default:
		fmt.Printf("未知型")
	}
   //使用 fallthrough 会强制执行后面的 case 语句，fallthrough 不会判断下一条 case 的表达式结果是否为 true。
	switch {
	case false:
		fmt.Println("1、case 条件语句为 false")
		fallthrough
	case true:
		fmt.Println("2、case 条件语句为 true")
		fallthrough
	case false:
		fmt.Println("3、case 条件语句为 false") //这个会被执行
		fallthrough
	case true:
		fmt.Println("4、case 条件语句为 true")
	case false:
		fmt.Println("5、case 条件语句为 false")
		fallthrough
	default:
		fmt.Println("6、默认 case")
	}
	
	//---for
	for a := 0; a < 10; a++ {
		fmt.Printf("a 的值为: %d\n", a)
	}
	var size int = 5
	var iter int=1
	for iter < size {
		iter++
		fmt.Printf("iter 的值为: %d\n", iter)
	}
	numbers := [6]int{1, 2, 3, 5} //数组
	for i,x:= range numbers {
		fmt.Printf("第 %d 位 x 的值 = %d\n", i,x)
	}
	//----function
	fmt.Println(max(200,120)) //都是自己的函数
	strF, strT := swap("Mahesh", "Kumar")
	fmt.Println(strF, strT)
   
   var num1 int = 100
	var num2 int= 200
	fmt.Printf("交换前，num1 的值 : %d\n", num1 )
	fmt.Printf("交换前，num2 的值 : %d\n", num2 )
	swapPtr(&num1, &num2)
	fmt.Printf("交换后，num1 的值 : %d\n", num1 )
	fmt.Printf("交换后，num2 的值 : %d\n", num2 )
   
   
	getSquareRoot := func(x float64) float64 {
		return math.Sqrt(x)
	}
	fmt.Println(getSquareRoot(9))
   //闭包
	nextNumber := getSequence()
	fmt.Println(nextNumber())//1
	fmt.Println(nextNumber())//2
	fmt.Println(nextNumber())//3

	nextNumber1 := getSequence()
	fmt.Println(nextNumber1())//1
	fmt.Println(nextNumber1())//2
   
	//----array
	var balance = [...]float32{1000.0, 2.0, 3.4, 7.0, 50.0}
	fmt.Println(balance)
	var emptyArray [10]int
	var ii int
	for ii=0;ii<10;ii++{
		emptyArray[ii] = ii + 100
	}

	var myArray = [3][4]int{
		{0, 1, 2, 3} ,
		{4, 5, 6, 7} ,
		{8, 9, 10, 11}}
	fmt.Println(myArray)


	//----struct
	fmt.Println(Books{"Go 语言", "li", "Go 语言教程", 6495407})
	 	// 忽略的字段为 0 或 空
	fmt.Println(Books{title: "Go 语言", author: "li"})

	var book1 Books   //使用类型总是在后面

	book1.title = "Go 语言"
	book1.author = "li"
	book1.subject = "Go 语言教程"
	book1.book_id = 6495407
	printBook(book1)
	printBookPtr(&book1)

	var struct_pointer *Books
	struct_pointer = &book1;
	fmt.Println(struct_pointer.author)//不同于C 不是->

	var c1 Circle
	c1.radius = 10.00
	fmt.Println("圆的面积 = ", c1.getArea())//结构体中的方法
   
	//---切片("动态数组")
	var numbers2 = make([]int,3,5)//make函数创建 len,cap
	printSlice(numbers2)

	slice2 :=[] int {1,2,3 }//和数组的不同是  []中为空，即没有...和数字
	printSlice(slice2)

	var myIntArray  = [...] int {10,20,30}
	sliceFromArray := myIntArray[0:3] //[startIndex:endIndex]
	//sliceFromArray := myIntArray[0:]
	//sliceFromArray := myIntArray[:3]
	printSlice(sliceFromArray)

	var empSlice []int
	if(empSlice == nil){ //ObjectiveC
		fmt.Printf("切片是空的")
	}
	fmt.Println("sliceFromArray[1:3] ==", sliceFromArray[1:3])//20,30 从索引(包含) 到索引(不包含)
	var sliceFromArrayRes []int  = append(sliceFromArray, 50,60)//增加无素
	fmt.Println("sliceFromArrayRes =", sliceFromArrayRes)

	newSlice := make([]int, len(sliceFromArrayRes), (cap(sliceFromArrayRes))*2) //len,cap函数
	copy(newSlice,sliceFromArrayRes) //拷贝 dest,src
	printSlice(newSlice)
   
	strFormat := `
    Cannot proceed, the divider is zero.
    dividee: %d
    divider: 0
	` //保留原来的换行符
	return fmt.Sprintf(strFormat, de.dividee)


//闭包　返回一个函数
func getSequence() func() int {
	i:=0
	return func() int {
		i+=1
		return i
	}
}
type Books struct
{
	title string
	author string
	subject string
	book_id int
}
type Circle struct {
	radius float64
}

//属于 Circle结构体中叫方法
func (c Circle) getArea() float64 {
	return 3.14 * c.radius * c.radius
}

func max(num1, num2 int) int {//func 如{在新的一行就报错
	var result int

	if (num1 > num2) {
		result = num1
	} else {
		result = num2
	}
	return result
}
//函数返回多个值
func swap(x, y string) (string, string) {
	return y, x
}
func swapPtr(x *int, y *int) {
	var temp int
	temp = *x
	*x = *y
	*y = temp
}
//func getAverage(arr []int, size int) float32 //函数参数数组，像C

func printBook( book Books ){
	fmt.Printf( "Book title : %s\n", book.title);
	fmt.Printf( "Book author : %s\n", book.author);
	fmt.Printf( "Book subject : %s\n", book.subject);
	fmt.Printf( "Book book_id : %d\n", book.book_id);
}
func printBookPtr( book *Books ){
	fmt.Printf( "Book title : %s\n", book.title);
	fmt.Printf( "Book author : %s\n", book.author);
	fmt.Printf( "Book subject : %s\n", book.subject);
	fmt.Printf( "Book book_id : %d\n", book.book_id);
}

func printSlice(x []int){
	fmt.Printf("len=%d cap=%d slice=%v\n",len(x),cap(x),x)
}

func Factorial(n uint64)(result uint64) { //返回参数名
	if (n > 0) {
		result = n * Factorial(n-1)
		return result
	}
	return 1
}

package main
import "fmt"
func main() {
	//这是我们使用range去求一个slice的和。使用数组跟这个很类似
	nums := []int{2, 3, 4}
	sum := 0
	for _, num := range nums {
		sum += num
	}
	fmt.Println("sum:", sum)
	//在数组上使用range将传入index和值两个变量。上面那个例子我们不需要使用该元素的序号，所以我们使用空白符"_"省略了。有时侯我们确实需要知道它的索引。
	for i, num := range nums {
		if num == 3 {
			fmt.Println("index:", i)
		}
	}
	//range也可以用在map的键值对上。
	kvs := map[string]string{"a": "apple", "b": "banana"}
	for k, v := range kvs {
		fmt.Printf("%s -> %s\n", k, v)
	}
	//range也可以用来枚举Unicode字符串。第一个参数是字符的索引，第二个是字符（Unicode的值）本身。
	for i, c := range "go" {
		fmt.Println(i, c)
	}
}

//----Map 无序的　是使用 hash 表来实现的
var countryCapitalMap map[string]string  //map[key_data_type]value_data_type
countryCapitalMap = make(map[string]string)

countryCapitalMap [ "France" ] = "Paris"
countryCapitalMap [ "Italy" ] = "罗马"
countryCapitalMap [ "Japan" ] = "东京"
countryCapitalMap [ "India " ] = "新德里"

 for country := range countryCapitalMap {
	fmt.Println(country, "首都是", countryCapitalMap [country])
}

//查看元素在集合中是否存在
captial, ok := countryCapitalMap [ "美国" ]
if (ok) {
	fmt.Println("美国的首都是", captial)
} else {
	fmt.Println("美国的首都不存在")
}
countryCapitalMap2 := map[string]string{"France": "Paris", "Italy": "Rome", "Japan": "Tokyo", "India": "New delhi"}
delete(countryCapitalMap2, "France")//自带的delete函数
fmt.Println(countryCapitalMap2)



import (
	"fmt"
	"time"
)

func say(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond) //等待
		fmt.Println(s)
	}
}

func main() {
	//协程coroutine(协同程序),GO语言命名为Goroutine
	//main函数叫主Goroutine ,如主Goroutine结束，所有子的Goroutine也被中止了
		 
	//每一个用户线程 和内核线程是一对一的关系 （Java/C++使用的方式）
	//多个用户线程 对应 一个内核线程(只能用一个核，但减少切换开销)，是一对多的关系 ,如一个线程在阻塞中，其它所有线程都不会被调度到，修改为非阻塞式库，在要阻塞前让出CPU，通知其它用户线程
	//多个用户线程 对应 多个内核线程 是多对多的关系，(减少用户内核切换开销)可在运行时动态关联，当一个内核线程上的一个用户线程阻塞，这个内核线程上的其它用户线程，会被调试到其它内核线程上 (GO使用，自己实现了一个运行调试器)
	/*schedular实现有4个结构
	 Sched
	Machine(由操作系统管理的，线程，用来运行Goroutine)
	Processor 维护了Goroutine队列,从N:1,到N:M，个会申请批量资源
	Goroutine 如需要资源先向Processor申请
	
	CSP模型=communicating Sequential Process ,GO语言的CSP是用gorouting和channel实现
	channel 先进先出（队列）,底层也是用mutex，只是功能更强，类似  unix 的pipe
	*/
	
	go say("world") //启动新协程，函数有返回值也会被忽略
	say("hello")
}

//---channel
func sum(s []int, c chan int) {
	fmt.Printf("channel=%T,%v\n",c,c)//%v显示值内存地址，按内存地址传递
	sum := 0
	for _, v := range s {
		sum += v
	}
	c <- sum // 把 sum 发送到通道 c,如没有读也是阻塞的，容量就是一个,同一时间无论读写，只有一个能操作
}

func main() {
	s := []int{7, 2, 8, -9, 4, 0}

	c := make(chan int) //建立channel用来做协程间的通讯, 默认情况下，通道是不带缓冲区的,是阻塞式的 ,可以保证同一时间只有一个goroutine取channel数据
	fmt.Printf("channel=%T,%v\n",c,c)//%v显示值内存地址，按内存地址传递
	
	go sum(s[:len(s)/2], c)
	go sum(s[len(s)/2:], c)
	time.Sleep(2*time.Second)//测试写一个值也要等
	x, y := <-c, <-c // 从通道 c 中接收,如没有人写,这里阻塞，像 ArrayBlockingQueue

	fmt.Println(x, y, x+y)
	
	//c1:= make(chan string)
	//c1 <- "hello" //如只有读或写的一方，就会报死锁错误
}


//---range channel

func fibonacci(n int, c chan int,done chan bool) {
	x, y := 0, 1
	for i := 0; i < n; i++ {
		c <- x
		fmt.Println("写了",x)
		x, y = y, x+y
	}
	close(c)//关闭channel,表示没有更多的数据了
	done<-true
}

func main() {
	c := make(chan int, 10)//10个缓冲区,可以写<=10个，不会阻塞
	done := make(chan bool)
	go fibonacci(cap(c), c,done) //slice 的cap函数 容量

	//如果上面的 c 通道不关闭，那么 range 函数就报错
	//for i := range c { //range 来(遍历)取 channel,是在关闭之后退出
	//	fmt.Println(i)
	//}
	//--方式二
	for {
		time.Sleep(100*time.Millisecond)
		v,ok := <- c
		fmt.Printf("len=%d,cap=%d,ok=%t\n",len(c),cap(c),ok)//%t 对boolean类型
		if ok {//true表示没有关闭
			fmt.Println("读了",v)
		}else {
			break;
		}
	}
	//-----阻塞方式 ，多传一个chan,
	<- done //可以不接收变量，这种方式阻塞，只会传一个值过来
}
//---select
   
import (
	"fmt"
	"time"
)
func Chann(ch chan int, stopCh chan bool) {
	for j := 0; j < 10; j++ {
		ch <- j
		time.Sleep(time.Second)//暂停一秒
	}
	stopCh <- true
}

func main() {
	ch := make(chan int)
	c := 0
	stopCh := make(chan bool)

	go Chann(ch, stopCh) //开协程

	for {
		select //select会随机执行一个(其它的被忽略了)可运行的case(全是通道)。如果没有case可运行，再看是否有default，有就执行，如没有default将阻塞，直到有case可运行。
		{//这个可以换行
		case c = <-ch:
			fmt.Println("Recvice", c)
			fmt.Println("channel")
		case s := <-ch:  //:表示一个新的变量
			fmt.Println("Receive", s)
		case _ = <-stopCh: //9个之前，一直没有值
			goto end
		}
	}
end:
}
//---error

import (
	"errors"
	"fmt"
)

// 定义一个 DivideError 结构
type DivideError struct {
	dividee int
	divider int
}

/*
Go 语言通过内置的错误接口
type error interface {
	Error() string
}*/
// 实现 `error` 接口,有错误时调用这个方法
//属于 DivideError结构体中的方法,可以是指针
func (de *DivideError) Error() string {
	strFormat := `
    Cannot proceed, the divider is zero.
    dividee: %d
    divider: 0
	` //保留原来的换行符
	return fmt.Sprintf(strFormat, de.dividee)
}

// 定义 `int` 类型除法运算的函数
func Divide(varDividee int, varDivider int) (int,  error) {
	if varDivider == 0 {
		dData := DivideError{
			dividee: varDividee,//结构初始化可指定key
			divider: varDivider,
		}
		return 0,&dData
	} else {
		return varDividee / varDivider, nil
	}
}

func main() {
	// 正常情况
	if result, err  := Divide(100, 10); err == nil {//err 变量范围是这个if块
		fmt.Println("100/10 = ", result)
	}
	// 当被除数为零的时候会返回错误信息
	if _, err := Divide(100, 0); err != nil {
		fmt.Println("errorMsg is: ", err)
	}
	
	_, err:= Sqrt(-1)
	if err != nil {
		fmt.Println(err)
	}
}

func Sqrt(f float64) (float64, error) {
	if f < 0 {
		var err error  =fmt.Errorf("%f不能<0",f)
		//var err error =errors.New("math: square root of negative number")//errors是Go的返回一个error
		return 0,err
	}
	return f/2 , nil
}

cn:="中"
man:=true;
fmt.Printf("type=%T,cn=%q,a(ASCII)=%d man=%t\n",cn,cn,'a',man)
fmt.Printf("%c\n", cn) //%c 中文有点复杂
fmt.Printf("%c\n", 'a')
fmt.Printf("%c\n", 97)

//fmt.Printf("请输入两个数\n")
//var x,y int
//fmt.Scan(&x,&y)
//fmt.Printf("输入为x=%d,y=%d\n",x,y)


//fmt.Printf("请输入一个整数，一个小数\n")
//var w int
//var h float32
//fmt.Scan(&w,&h)
//fmt.Printf("输入为w=%d,h=%f\n",w,h) //发现小数22.123精度不准


fmt.Printf("请输入一段字\n")
reader:=bufio.NewReader(os.Stdin)
s1,err:=reader.ReadString('\n')
if  err == nil {
	fmt.Printf("读到为=%s\n",s1) //
}

 //同JS
 func (a,b int ){
	 fmt.Println(a,b)
 }(10,20)
 
a1:=10
defer calcSum(a1,20,30) //defer表示要所在函数main执行完成,在退出前再执行声明的函数calcSum,但参数已经传递了,只是晚点执行
fmt.Printf("first\n")
a1+=100;
defer calcSum(a1,60) //如有多个defer，执行是是栈的方式，后进先出
fmt.Printf("main method end\n")
 
//--- 
func calcSum(nums ... int ) {//接收可变参数
	fmt.Printf("nums=%d\n",nums)
}



import (
	"fmt"
	"strings"
) 
func main() {
	fmt.Println(strings.Contains("seafood", "foo"))
	fmt.Println(strings.HasPrefix("Gopher", "Go"))
	fmt.Println(strings.ToLower("Gopher"))
	fmt.Println(strings.Count("cheese", "e"))
	fmt.Print(strings.Trim("¡¡¡Hello, Gophers!!!", "!¡"))
	fmt.Println(strings.ReplaceAll("oink oink oink", "oink", "moo"))

	fmt.Println("ba" + strings.Repeat("na", 2))
	fmt.Printf("%q\n", strings.Split("a,b,c", ","))//%q安全去除单/双引号
	s := []string{"foo", "bar", "baz"}
	fmt.Println(strings.Join(s, ", "))
}

import (
	"fmt"
	"strconv"
)
func main() {
	//fmt.Println("a"+3)//error
	var num,err=strconv.Atoi("20")
	if err == nil{
		fmt.Println(num*3)
	}
	s := strconv.Itoa(-42)
	fmt.Println(s+"123")
	
	b, err := strconv.ParseBool("true")
	f, err := strconv.ParseFloat("3.1415", 64)//float64
	i, err := strconv.ParseInt("-42", 10, 64)//10进制，int64
	u, err := strconv.ParseUint("42", 10, 64)
	fmt.Println(b,f,i,u)

	s1 := strconv.FormatBool(true)
	s2 := strconv.FormatFloat(3.1415, 'E', -1, 64)
	s3 := strconv.FormatInt(-42, 16)//16进制
	s4 := strconv.FormatUint(42, 16)
	fmt.Println(s1,s2,s3,s4)
}

---继承

type Human struct {
	name string
	age int
	weight float32
}
type Student struct {
	Human //没有名字，就是名字和类一样，同JS,也可以成为提升字段
	grade string
	age float32 //同名，类型变了
}

type Move interface{ //接口只能定义方法声明(也可没有方法，像java的Serialize)，可以做为方法参数
	walk()
}
type Move2 interface{
	walk()//不同接口可有同名方法，哪些有实现呢
}
type Advance interface{
	Move //类似struct来继承，可以加多个
	work()
}

func printInterface(m Move ){
	if s,ok:=m.(*Student);ok { //断言，强转为子类,接口的所有方法在结构体中都有
		s.walk()
	}else {
		m.walk();
	}
	//方试二 强转为子类
	switch s:=m.(type) {
		case  *Student:
	  		s.walk()
		default:
			m.walk();
	}
}
func noNameInterface(m interface{}){ //匿名空接口
	fmt.Println(m) //fmt.Println的参数也是空接口
}
func (h  Human )walk(){
	fmt.Println(h.name +"is is human , walk")
}
func (h  Student )walk(){
	fmt.Println(h.name +"is is Student , walk")
}

func (h  Human )work(){
	h.age=33
	fmt.Println(h.name +"is is human , working")
}
func (h * Student )work(){ //属于不同的结构体的方法可以同名，这里是重写父类方法，指针传递的是地址,里面修改会影响外面
	h.age=33.5
	fmt.Println(h.name +"is is Student , working")
}

func main(){
	p:=Human{name:"张三",age:20}
	fmt.Println(p.name,p.age)

	s:=Student{Human:Human{name:"李同学",age:18},grade:"A"}

	p.work()
	s.work()
	fmt.Println(p.name,p.age)
	fmt.Println(s.Human.name,s.grade)
	fmt.Println(s.name,s.age,s.grade)//可以直接访问name，模拟了继承

	s.Human.walk() ;//子类可以调用父类的方法

	printInterface(&s)//如声明是指针，传递要为地址
	printInterface(s);
	printInterface(p);

	var  m Move =p //可以指向子类,模拟多态
    m.walk()

	s.work()
	var s1 *Student  =&s
	s1.work()

	noNameInterface(123);
	noNameInterface("abc");

	map1:=make(map[string]interface{})
	map1["name"]="李四"
	map1["age"]=20 //值可以是任何类型

	slice1:=make([]interface{},0,10) //切换数组任意类型
	slice1=append(slice1,"李四",30)
}

---panic goroute 
func main() {
	defer myPrint("main is defer begin")
	myFunc();
	defer myPrint("main is defer end")
	myPrint("main is end")
} 
func myPrint(str string){
	fmt.Println(str)
}

func myFunc(){
	defer myPrint("myFunc defer begin ")//相当于 finally
	panic("Oh error!")//后面的代码不被执行，但会执行已经defer的, 相当于throw new Exception
	fmt.Println("in myFunc")
	defer myPrint("myFunc defer end ")
}

func date_api(){

	now:=time.Now()
	fmt.Println("now=",now)

	var year,month,day=now.Date();
	fmt.Printf("year=%d,month=%d,day=%d\n",year,month,day)

	var hour,minite,second=now.Clock();
	fmt.Printf("hour=%d,minite=%d,second=%d\n",hour,minite,second)

	fmt.Printf("timestamp=%d\n",now.Unix())//时间截，从1970年1月1日0时开始到这个时间的秒数

	//afterAdd:=now.Add(time.Minute)//加时分秒
	afterAdd:=now.AddDate(0,0,-3)//加年月日
	fmt.Printf("afterAdd=%s\n",afterAdd)
	fmt.Println(now.Before(afterAdd))

	t := time.Date(2009, time.November, 10, 23, 0, 0, 0, time.UTC)
	fmt.Printf("Go launched at %s\n", t.Local())

	var iso_time_format string="2006-01-02 15:04:05" //看文档一定要是2006年01月02日15:04:05来定义格式,而不是%Y或yyyy的格式
	//很容易看错了,是个坑,按1,2,3(下午),4,5,6(年)来记忆
	//date->string
	fmt.Printf(t.Format(iso_time_format))

	//string->date
	str:="2009-11-10 23:00:00"
	beginTime,err := time.Parse(str,iso_time_format);
	if err!=nil {
		fmt.Printf("error=%s\n", err)
	}else {
		fmt.Printf("beginTime=%s\n", beginTime)
	}

	rand.Seed(time.Now().Unix())
	randNum:=10+rand.Intn(10)*3//Intn(10)取0-9
	fmt.Println("10-30 random=%d",randNum)

	//time.Sleep(time.Duration(randNum))
	fmt.Println("3秒后退出")
	time.Sleep(3*time.Second)//暂时3秒 
	
	//定时器,只触发一次
	fmt.Println("定时器,now=",time.Now());
	mytimer:=time.NewTimer(3*time.Second)
	go func(){
		ch1:=mytimer.C //C 是一个 chann
		//ch2:=time.After(3*time.Second) //源码就是 return NewTimer(d).C
		fmt.Println(<-ch1) //会阻塞3秒,显示3秒后的时间
	}()

	time.Sleep(2*time.Second)
	var ok bool =mytimer.Stop();//可以提前停止
	if ok {
		fmt.Println("成功取消定时器");
	}
	
	//---ticker 多次触发定时
	timer2 := time.NewTicker( time.Duration(time.Second*2))
	defer timer2.Stop()
	for {
		<- timer2.C
		fmt.Println("这用for每隔2秒执行一次")
	}
	
}

-----文件操作

import (
	"bufio"
	"fmt"
	"io"
	"io/ioutil"
	"os"
	"path"
	"path/filepath"
	"strings"
)
func  main()  {
	var myPath string ="D:/tmp/my.txt"
	var myDir string ="d:/tmp/aa"

	//f,err:=os.Open(myPath) //默认是只读的
	f,err:=os.OpenFile(myPath,os.O_APPEND,os.ModePerm)//os.O_APPEND,os.O_WRONLY | os.O_RDONLY
	if err!=nil {
		fmt.Println(err)
		if ins,ok:=err.(*os.PathError);ok {
			fmt.Printf("打开文件%s 错误原因为%s,OP=%s",ins.Path,ins.Err,ins.Op)
		}
		return;
	}
	fmt.Println(f.Name())
	buf:=make([]byte,64,64)
	f.Seek(6,io.SeekStart)//路过前n字节个开始读（如单数有可能中文切了)，中文不行？？？
	//io.SeekCurrent , io.SeekEnd
	for {
		len,err:=f.Read(buf)
		//f.ReadAt()//从指定位置来读
		if len==0  || err==io.EOF {
			break;
		}
		fmt.Print(string(buf[:len]))
	}

	defer f.Close()

	fileInfo,err:=os.Stat(myPath)
	if err!=nil {
		fmt.Println(err)
		return;
	}
	fmt.Printf("IdDir=%t,size=%d,modTime=%s\n",fileInfo.IsDir(),fileInfo.Size(),fileInfo.ModTime())
	fmt.Printf("mod=%s\n",fileInfo.Mode())
	fmt.Printf("IsAbs=%t\n",filepath.IsAbs(myPath)) //是否绝对路径,filepath.Abs()得到绝对路径

	fmt.Printf("Join=%s\n",path.Join(myPath,".."))

	os.Mkdir(myDir,os.ModePerm) //MkdirAll

	f1,err:=os.Create(myDir+"/myfile.txt")
	if err!=nil {
		fmt.Println("建立文件错误，做删除",err)
		os.Remove(myDir+"myfile.txt")//os.RemoveAll()
		return;
	}
	fmt.Println("向文件写内容")
	f1.WriteString("abcde")
	f1.Write([]byte("ABCD"))
	//f1.WriteAt()//指定位置写
	defer f1.Close()

	//copyFile("D:/tmp/my.txt","D:/tmp/my2.txt");//自已的方法
	//copyFileInMem("D:/tmp/my.txt","D:/tmp/my2.txt");//自已的方法
	//copyUseBufio("D:/tmp/my.txt","D:/tmp/my2.txt");
	recursiveShowDir("D:/tmp/",1)


	fmt.Print("请输入")
	in:=bufio.NewReader(os.Stdin) 
	str,_:=in.ReadString('\n');//这里才可开始读,以\n做为结束
	fmt.Print(str)


}
func copyFile(fromFile string,toFile string)(int64,error){
	from,err:=os.OpenFile(fromFile,os.O_RDONLY,os.ModePerm)
	if err!=nil { //每个文件API调用都要if判断错误，确实不太好
		return 0,err;
	}

	to,err:=os.OpenFile(toFile,os.O_WRONLY|os.O_CREATE,os.ModePerm)
	if err!=nil {
		return 0,err;
	}
	buf:=make([]byte,64,64)
	return io.CopyBuffer(to,from,buf)//提高性能，加缓存
	//return io.Copy(to,from)
}
func copyFileInMem(fromFile string,toFile string)(int,error) {
	r1:=strings.NewReader("ABC123中文")
	data,err:=ioutil.ReadAll(r1)
	fmt.Printf("%s\n",data)
	//--
	tmpfile,err:=ioutil.TempFile("d:/tmp/","checkbill_*.txt")//会把*替换为随机数
	defer os.Remove(tmpfile.Name())
	defer  tmpfile.Close()
	tmpfile.Write(data);

	//===
	bs,err:=ioutil.ReadFile("D:/tmp/my.txt") //一次性读入内存，不适合文件过大,源码是调用的readAll方法
	if err!=nil {
		return 0,err;
	}
	os.Create(toFile)
	err=ioutil.WriteFile("D:/tmp/my2.txt",bs,os.ModePerm)
	if err!=nil {
		return 0,err;
	}
	return len(bs),nil
}
func copyUseBufio(fromFile string,toFile string)(int,error){
	from,err:=os.OpenFile(fromFile,os.O_RDONLY,os.ModePerm)
	if err!=nil {
		return 0,err;
	}
	defer  from.Close()
	reader:=bufio.NewReader(from)
	//---
	//buf:=make([]byte,64,64)
	//for {
	//	len,err:=reader.Read(buf)
	//	if len==0  || err==io.EOF {
	//		break;
	//	}
	//	fmt.Print(string(buf[:len]))
	//}
	//---按行读
	for{
		//data,flag,err:=reader.ReadLine()//底层的
		//fmt.Printf("flag=%t,err=%s,data=%s",flag,err,string(data))
		//---
		data, err := reader.ReadString('\n') //文件内容必须是UTF-8，否则中文乱码
		//还有
		//data, err := reader.ReadBytes('\n')//文件内容必须是UTF-8，否则中文乱码
		// data, err :=reader.ReadByte()
		if err == io.EOF {
			break
		}
		//fmt.Printf("err=%s,data=%s", err, string(data)) //对应ReadBytes
		fmt.Printf("err=%s,data=%s", err, data) //对应ReadString
	}
	//--写
	to,err:=os.OpenFile(toFile,os.O_WRONLY|os.O_CREATE,os.ModePerm)
	if err!=nil {
		return 0,err;
	}
	defer to.Close()

	writer:=bufio.NewWriter(to)
	len,err:=writer.WriteString("hello中文")
	writer.Flush()//必须手工调用写缓冲
	return len,err
}
func recursiveShowDir(dir string,level int)(int,error){
	fileInfos,err:=ioutil.ReadDir(dir)//相当于在目录下 ls
	if(err!=nil){
		return 0,err;
	}
	tree:="|-"
	for i:=0;i<level;i++ {
		tree="| "+tree
	}
	for _,item:= range fileInfos {
		fmt.Printf("%s %s/%s\n",tree,dir,item.Name()) //中文可以的
		if(item.IsDir()){
			recursiveShowDir(dir+"/"+item.Name(),level+1)
		}
	}
	return 0,nil
}
//-----------runime
import (
	"fmt"
	"runtime"
	"time"
)
func init(){
	runtime.GOMAXPROCS(8)
	fmt.Println("CPU核数=",runtime.NumCPU())
}
func main() {
	 //runtim相当于JVM 编译出来的程序是运行在runtime上，负责内存分配，拉圾回收，反射，goroutine,channel
	 fmt.Println("GOROOT="+runtime.GOROOT())

	fmt.Println("OS="+runtime.GOOS)//windows,darwin
	go func(){
		for i:=0;i<10;i++{
			fmt.Println(i)
		}
	}()
	runtime.Gosched()//让出CPU

	go myfunc();

	time.Sleep(3*time.Second)
}
func myfunc(){
	defer  fmt.Println("myfunc defer")
	//return //后面有代码不会报错
	runtime.Goexit()//退出goroutine,所在方法必须是用go启动的,还会执行defer
	fmt.Println("myfunc done")
}
------锁
func main() {
	var mutex sync.Mutex  //也有RWMutext,也能检查到死锁(如有时候没有UnLock)，比java牛
	//不要共享内存的方式去通讯，而是以通讯的方式共享内存,应该用channel
	var global int=10;
	go func(){
		//go run -race bad_lock.go 如不加锁，会显示警告
		var isDone bool=false;
		for !isDone{
			mutex.Lock()
			if global>0 {
				time.Sleep(10*time.Millisecond)
				global--;//可以仿问到外部的变量
				fmt.Println("No Name库存少了一个",global)
			}else {
				isDone=true;
			}
			mutex.Unlock()
		}
	}()

	var isDone bool=false;
	for !isDone{
		mutex.Lock()
		if global>0 {
			time.Sleep(10*time.Millisecond)
			global--;//可以仿问到外部的变量
			fmt.Println("main库存少了一个",global)
		}else {
			isDone=true
		}
		mutex.Unlock()
	}
	time.Sleep(2*time.Second)
}
----wait

var waitG=sync.WaitGroup{}
func main() {
	waitG.Add(2)//如这里是3,会报死锁，这个比java牛，知道所有的goroutine已经结束但还>0
	go func(){
		for i:=1;i<10;i++{
			time.Sleep(4)
			fmt.Println("no name func i=",i)
		}
		waitG.Done() //源码就是Add(-1)
	}()
	go subFunc() 
	waitG.Wait()//主协程 等所有子协程执行完，像Java的CountDownLatch
}
func subFunc(){
	defer  waitG.Done()
	for i:=1;i<10;i++{
		time.Sleep(2)
		fmt.Println("subFunc i=",i)
	}
	fmt.Println("myfunc done")
}
----单向通道

func main() {
	all := make(chan int )
	go readData(all)
	go writeData(all,10)

	//以下代码没什么作用
	writeChan := make(chan <- int )//只能写，不能读
	readChan := make(<- chan  int )//只能读，不能写
	go writeData(writeChan,20)
	go readData(readChan)

	time.Sleep(30*time.Second)
}
func readData(readChan <- chan  int){//只能读，不能写
	fmt.Println("准备读")
	read:= <-readChan //如是单向的这个执行后，不会执行后面的代码
	//readChan <-  1//报错
	fmt.Println("读到了",read)
}
func writeData(writeChan chan <-  int,num int){//只能写，不能读
	fmt.Println("准备写")
	writeChan <-  num //如是单向的这个执行后，不会执行后面的代码
	//readErr:= <- writeChan//报错
	fmt.Println("写了",num)
}
----reflect
func main() {
	//Go是静态语言，编译时类型已经确定,动态类型要求是接口,每个接口会记录(pair)值(ValueOf)，和类型(TypeOf,即%T)

	var pi float64 =3.14 //任何类型都可以看到是一空接口类型，如float32也是
	fmt.Println("type=",reflect.TypeOf(pi)) //TypeOf参数如是空接口返回nil
	fmt.Println("value=",reflect.ValueOf(pi)) //ValueOf参数如是空接口返回0
	//接口变量 -> 反射对象
	var valReflect reflect.Value = reflect.ValueOf(pi)
	fmt.Println("is float32=",valReflect.Kind() == reflect.Float64)
	fmt.Println("Type=",valReflect.Type() )
	fmt.Println("Float=",valReflect.Float() )//如float32类型精度不准3.140000104904175，默认是float64

	// 反射对象 -> 接口变量
	convertVal:=valReflect.Interface().(float64)//对已知类型做强转
	fmt.Println("convertVal=",convertVal)

	//valReflect.SetFloat(5.18) //值传递的，不可直接修改
	//--指针,如要修改值一定要使用指针
	var valPointerReflect reflect.Value = reflect.ValueOf(&pi)
	fmt.Println("valPointerReflect=",valPointerReflect)//是地址
	fmt.Println("valType=",valPointerReflect.Type()) //*float64

	//Elem修改值
	valElem:=valPointerReflect.Elem()//如不是指针这里报错
	fmt.Println("Elem=",valElem)//是值
	fmt.Println("CanSet=",valElem.CanSet())//是否可以修改值
	valElem.SetFloat(4.18) //修改值
	fmt.Println("after reflect set =",pi)

	//
	convertPointer:=valReflect.Interface().(float64) //不能是*float64
	fmt.Println("convertPointer=",convertPointer)//还是3.14 ???
 
}

type Persion struct {
	Name string
	Age int
}
func (p Persion) Say(msg string){ //可以是 (p Persion) 也可是 (p *Persion)
	fmt.Println("say:",msg)
}
func (p Persion) PrintInfo(){ //方法名要大写
	fmt.Printf("name=%s,age=%d\n",p.Name,p.Age)
}
func   printFunc(){
	fmt.Println("printFunc Invoked")
}
func  calcFunc(a int,b int)string{
	res:= a + b
	var resStr=strconv.Itoa(res)
	fmt.Printf("calc %d+%d=%d",a,b,res)
	return resStr;
}

func main() {
	var p1  Persion = Persion{"李四",28}
	showReflect(p1)
	var p2 *Persion=&p1
	fmt.Printf("p2.Name=%s,(*p2).Name=%s\n",p2.Name,(*p2).Name)//都可以

	changeFieldByReflect(p2)//struct指针修改值
	
	//----无参数函数调用
	reflectFunc:=reflect.ValueOf(printFunc)
	fmt.Println("printFunc Kind=",reflectFunc.Kind() )//func，必须是func才能调用Call
	fmt.Println("printFunc Type=", reflectFunc.Type())//func()

	emptySlice:=make([]reflect.Value,0)
	resSlice:=reflectFunc.Call(emptySlice ) //调用方法，空参数传 nil 或 空切片
	fmt.Println("resSlice=",resSlice)
	
	//----有参数函数调用
	reflectFunc=reflect.ValueOf(calcFunc)
	fmt.Println("reflectFunc .Kind=  ",reflectFunc.Kind() )//func
	fmt.Println("reflectFunc Type= ", reflectFunc.Type())// func(int, int) string

 	paramSlice:= []reflect.Value{reflect.ValueOf(10),reflect.ValueOf(20)}
	resSlice=reflectFunc.Call(paramSlice ) //调用方法，空参数传 nil 或 空切片
	fmt.Println("resSlice=",resSlice)
	res:=resSlice[0].Interface().(string) //Interface函数返回空接口强转为string
	fmt.Println("res=",res)

	///---对象的方法
	//refType:=reflect.TypeOf(p2)
	refValue:=reflect.ValueOf(p2)//指针
	fmt.Printf("refValue =%v \n",refValue)
	//refElem :=refValue.Elem()//指针的指针才要这一步
	valMethod:=refValue.MethodByName("PrintInfo") //方法名一定要大写
	fmt.Printf("valMethod =%v \n",valMethod)	  //%v显示值内存地址
	fmt.Printf("valMethod Kind=%s,Type=%s \n",valMethod.Kind(),valMethod.Type())

	emptySlice=make([]reflect.Value,0)
	valMethod.Call(emptySlice ) //调用方法，空参数传 nil 或 空切片

	valMethod=refValue.MethodByName("Say");
	paramSlice1:= []reflect.Value{reflect.ValueOf("参数1	")}
	valMethod.Call(paramSlice1 )
}
func showReflect(common interface {}){
	refType:=reflect.TypeOf(common)
	fmt.Println("Name=",refType.Name())//Persion
	fmt.Println("Kind=",refType.Kind())//struct还有slice ,map,

	refValue:=reflect.ValueOf(common)
	fmt.Println("Type=",refValue.Type())//main.Persion
	//fmt.Println("refValue=",refValue)//{李四 28}
	fmt.Printf("---Field\n")
	for i:=0;i<refType.NumField();i++ {
		field:=refType.Field(i)
		val:=refValue.Field(i).Interface()//Interface()得到值
		fmt.Printf("index=%d,type=%s,name=%s,value=%v\n",field.Index,field.Type,field.Name,val) //%v显示通用类型的值
	}

	ageField,ok:=refType.FieldByName("Age")//Type根据字段名取
	if !ok {
		fmt.Printf("没有Age字段\n")
	}else {
		fmt.Printf("Age type =%s\n",ageField.Type)
	}

	ageField2:=refValue.FieldByName("Age")//Value根据字段名取
	fmt.Printf("Age =%d\n",ageField2)
	
	fmt.Printf("---Type Method\n")
	for i := 0; i < refType.NumMethod(); i++ {
		method := refType.Method(i)
		fmt.Printf("TypeOf Name=%s,Type=%s V=%v \n", method.Name, method.Type, method.Type)
	}
	
}

func changeFieldByReflect( p2 *Persion){
	refType:=reflect.TypeOf(p2)
	refValue:=reflect.ValueOf(p2)
	fmt.Println("Type=",refValue.Type())//*main.Persion
	fmt.Println("val Kind =",refValue.Kind())//ptr
	fmt.Println("typ Kind =",refType.Kind())//ptr
	if refValue.Kind() == reflect.Ptr {
		valElem:=refValue.Elem()
		if valElem.CanSet() {
			fldAge:=valElem.FieldByName("Age")
			fldAge.SetInt(30)//结构体的属性大写字母开头的才可写
			fmt.Println("after change age =",p2.Age)
		}
	}
}

//----------struct  tag
type cat struct {
	Name string
	// 带有结构体tag的字段
	Type int `json:"type" id:"100"`
	//` 开始和结尾的字符串。这个字符串在Go语言中被称为 Tag（标签）。一般用于给字段添加自定义信息，方便其他模块根据信息进行不同功能的处理
	//键与值使用冒号分隔，值用双引号括起来；键值对之间使用一个空格分隔

}

ins := cat{Name: "mimi", Type: 1}
typeOfCat := reflect.TypeOf(ins)
for i := 0; i < typeOfCat.NumField(); i++ {
	fieldType := typeOfCat.Field(i)
	// 输出成员名和tag
	fmt.Printf("name: %v  tag: '%v'\n", fieldType.Name, fieldType.Tag)
}
// 通过字段名, 找到字段类型信息
if catType, ok := typeOfCat.FieldByName("Type"); ok {
	// 从tag中取出需要的tag
	fmt.Println(catType.Tag.Get("json"), catType.Tag.Get("id"))
	if val, ok := catType.Tag.Lookup("id"); ok {
		fmt.Println("结构体标签id的值为", val)
	} else {
		fmt.Println("结构体标签不存在")
	} 
}

//---IsNil  isValid
// *int的空指针
var a *int
fmt.Println("var a *int:", reflect.ValueOf(a).IsNil())
s := struct{}{}
// 尝试从结构体中查找一个不存在的字段
fmt.Println("不存在的结构体成员:", reflect.ValueOf(s).FieldByName("").IsValid())
// 尝试从结构体中查找一个不存在的方法
fmt.Println("不存在的结构体方法:", reflect.ValueOf(s).MethodByName("").IsValid())
// 实例化一个map
m := map[int]int{}
// 尝试从map中查找一个不存在的键
fmt.Println("不存在的键：", reflect.ValueOf(m).MapIndex(reflect.ValueOf(3)).IsValid())
//---CanAddr
{
	x := 2                   // value type variable?
	a := reflect.ValueOf(2)  // 2 int no
	b := reflect.ValueOf(x)  // 2 int no
	c := reflect.ValueOf(&x) // &x *int no
	d := c.Elem()            // 2 int yes (x)
	fmt.Println(a.CanAddr()) // "false"
	fmt.Println(b.CanAddr()) // "false"
	fmt.Println(c.CanAddr()) // "false"
	fmt.Println(d.CanAddr()) // "true"
	//Addr() 类似于语言层&操作
	//Elem()  类似于语言层*操作

}
//---TypeOf再New()返回Value
{
	var a int
	// 取变量a的反射类型对象
	typeOfA := reflect.TypeOf(a)
	// 根据反射类型对象创建类型实例
	aIns := reflect.New(typeOfA)
	// 输出Value的类型和种类
	fmt.Println(aIns.Type(), aIns.Kind())
}


//---slice 反射
var intSlice = []int{256, 512, 1024}
intSliceElemValue := reflect.ValueOf(&intSlice).Elem()
if intSliceElemValue.CanSet() {
	newSliceValue := []int{2560, 5120, 10240}
	newVale := reflect.ValueOf(newSliceValue)
	intSliceElemValue.Set(newVale)
	fmt.Println("NewSliceVal =", intSlice)
}
{
	var intSlice = []int{256, 512, 1024}
	intSliceValue := reflect.ValueOf(intSlice)
	e := intSliceValue.Index(0) //Index函数
	if e.CanSet() {
		e.SetInt(2560)
		fmt.Println("NewVal =", intSliceValue)
	}
}
//----map反射
m1 := map[string]int{"a": 1, "b": 2, "c": 3}
m2 := map[string]int{"a": 1, "c": 3, "b": 2}
fmt.Println(reflect.DeepEqual(m1, m2))

iter := reflect.ValueOf(m1).MapRange()
for iter.Next() {
	k := iter.Key()
	v := iter.Value()
	fmt.Println("key=", k, "value=", v)
}

mapVal := reflect.ValueOf(m1)
if mapVal.MapIndex(reflect.ValueOf("a")).IsValid() { //SetMapIndex
	mapVal.SetMapIndex(reflect.ValueOf("a"), reflect.ValueOf(11)) //SetMapIndex
	fmt.Println(m1)
}

 
########通道相关：
func (v Value) Send(x reflect.Value)// 发送数据（会阻塞），v 值必须是可写通道。

func (v Value) Recv() (x reflect.Value, ok bool) // 接收数据（会阻塞），v 值必须是可读通道。

func (v Value) TrySend(x reflect.Value) bool // 尝试发送数据（不会阻塞），v 值必须是可写通道。

func (v Value) TryRecv() (x reflect.Value, ok bool) // 尝试接收数据（不会阻塞），v 值必须是可读通道。

func (v Value) Close() // 关闭通道
//-----channel反射 

all := make(chan int)
go writeData(all, 20)
go readData(all)

time.Sleep(30 * time.Second)
 
func readData(readChan <-chan int) { //只能读，不能写
	fmt.Println("准备读")
	refReadChan := reflect.ValueOf(readChan)
	if read, ok := refReadChan.Recv(); ok { //Recv
		fmt.Println("读到了", read)
	}
	fmt.Println("读到结束")
}
func writeData(writeChan chan<- int, num int) { //只能写，不能读
	fmt.Println("准备写")
	refWriteChan := reflect.ValueOf(writeChan)
	refWriteChan.Send(reflect.ValueOf(num)) //Send
	fmt.Println("写了", num)
}




----DNS解析
addr,err:=net.LookupHost("www.baidu.com1")
	if(err!=nil){
		fmt.Println(err)
		if ins,ok:=err.(*net.DNSError); ok {
			fmt.Printf("timeout=%t\n",ins.Timeout())
		}
	}
	fmt.Printf("addr=%s\n",addr)
-------RPC
----服务端

type MyService struct { 
} 
//要用大写方法名,参数只可两个，有一个是指针类型的输出,可以都做成结构体
//(s MyService)或者 (s *MyService) 
func (s *MyService) Calc(req int ,resp *int )  error{
	*resp = req+1
	return nil
} 
func main(){
	service:=new (MyService)
	//err:=rpc.Register(service) //注册服务，默认服务名 为结构体名MyService
	err:=rpc.RegisterName("MyService",service) //指定服务名
	if err!=nil {
		fmt.Print("注册失败=",err)
		return
	}
	rpc.HandleHTTP() //使用HTTP协议来访问
	//上下这两段，代码没有联系，但运行确实关联上了
	listen,err:=net.Listen("tcp","0.0.0.0:8181")//端口
	if err!=nil {
		fmt.Print("监听失败=",err)
		return;
	}
	fmt.Print("服务启动了")
	http.Serve(listen,nil)//启动服务器，指定监听端口 
}
----客户端 
func main(){
	client,err:=rpc.DialHTTP("tcp","127.0.0.1:8181")//连接远程
	if err!=nil {
		fmt.Print("连接失败=",err)
		return
	}
	var req int =20
	var resp *int ;//Go会申请空间
	//格式注册的   服务名.方法名
	err=client.Call("MyService.Calc",req,&resp)//同步调用远程，响应是指针的指针
	if err!=nil {
		fmt.Print("调用失败=",err)
		return
	}
	fmt.Println("resp=",*resp)//取地址
//--
	var respAsync *int ;
	asynCall:=client.Go("MyService.Calc",req,&respAsync,nil) //异步调用远程
	asynDone:= <- asynCall.Done //读通道
	fmt.Println("asynDone=",asynDone)
	fmt.Println("asynRes=",*respAsync)//取地址 
}


import (
	"fmt"
	"net/http"
	"net/url"
	"io/ioutil"
)
//---http client
func printRes(resp *http.Response, err error  ){
	if err != nil {
		fmt.Print("error:",err)
		return
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	fmt.Printf("body=%s \n",body)
}

func main() {
	{
		resp, err := http.Get("http://localhost:8080/J_JavaEE/index.jsp")
		printRes(resp, err)
		resp, err  = http.PostForm("http://localhost:8080/J_JavaEE/receiveForm",
			url.Values{"username": {"lisi"}, "password": {"123"}})
		printRes(resp, err)
	}
	{
		client := &http.Client{
			//CheckRedirect: redirectPolicyFunc,
		}
		resp, err := client.Get("http://localhost:8080/J_JavaEE/index.jsp")
		printRes(resp, err)

		req, err := http.NewRequest("POST", "http://localhost:8080/J_JavaEE/receiveForm", nil)
		req.Header.Add("If-None-Match", `W/"wyzzy"`)//请求头
		resp, err  = client.Do(req) //发起请求
		printRes(resp, err)
	}

}
//----http server

func simpleHttp() {
	http.HandleFunc("/bar", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
	})
	http.HandleFunc("/test", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "测试, %q", html.EscapeString(r.URL.Path))
	})

	log.Fatal(http.ListenAndServe(":8080", nil))
	//http://localhost:8080/bar
	//http://localhost:8080/test
}




func index(writer http.ResponseWriter, request *http.Request) { //debug时这个方法可能会因超时被重复调用
	fmt.Println("---begin index")
	var t = template.New("layout")
	t = template.Must(t.ParseFiles("templates/index.html", "templates/private.navbar.html"))

	data := struct {
		Title string
		Items []string
	}{
		Title: "My page",
		Items: []string{
			"My photos",
			"My blog",
		},
	}
	t.ExecuteTemplate(writer, "layout", data)
	fmt.Println("---end index")
}
func login(writer http.ResponseWriter, request *http.Request) {
	fmt.Println("---begin login")
	var email = request.PostFormValue("email")
	fmt.Println("email=", email)

	cookie, err := request.Cookie("_session_id")
	if cookie != nil && err == nil {
		fmt.Println("_session_id=", cookie.Value)
	} else {
		cookie := http.Cookie{
			Name:     "_session_id",
			Value:    "123abc",
			HttpOnly: true,
		}
		http.SetCookie(writer, &cookie) //响应头并没有Set-Cookie

	}
	writer.Write([]byte("cookie 保存了"))
	//http.Redirect(writer, request, "index?id=123", 302) //客户端跳转带不了cookie?
	fmt.Println("---end login")
}

func upload(w http.ResponseWriter, r *http.Request) {
	var maxUploadSize int64 = 1024 * 1024 * 2 //2MB
	r.Body = http.MaxBytesReader(w, r.Body, maxUploadSize)
	if err := r.ParseMultipartForm(maxUploadSize); err != nil {
		fmt.Println("---FILE_TOO_BIG")
		panic(err)
	}
	var user_id = r.FormValue("user_id") //GET
	fmt.Println("---user_id=", user_id)
	fileType := r.PostFormValue("type")
	file, _, err := r.FormFile("uploadFile")
	if err != nil {
		panic(err)
	}
	defer file.Close()
	fileBytes, err := ioutil.ReadAll(file)

	filetype := http.DetectContentType(fileBytes)
	if filetype != "image/jpeg" && filetype != "image/jpg" &&
		filetype != "image/gif" && filetype != "image/png" &&
		filetype != "application/pdf" {
		fmt.Println("INVALID_FILE_TYPE")
		return
	}

	fileName := "my_file_name"
	newPath := filepath.Join("d:/tmp", fileName)
	fmt.Printf("FileType: %s, File: %s\n", fileType, newPath)
	newFile, err := os.Create(newPath)

	defer newFile.Close()
	if _, err := newFile.Write(fileBytes); err != nil {
		fmt.Printf("CANT_WRITE_FILE")
		return
	}
	w.Write([]byte("SUCCESS"))
}

func downloadHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm() //解析url传递的参数，对于POST则解析响应包的主体（request body）
	//注意:如果没有调用ParseForm方法，下面无法获取表单的数据
	fileName := r.Form["filename"]            //filename  文件名
	path := "d:/tmp/"                         //文件存放目录
	fileNames := url.QueryEscape(fileName[0]) // 防止中文乱码
	w.Header().Add("Content-Type", "application/octet-stream")
	w.Header().Add("Content-Disposition", "attachment; filename=\""+fileNames+"\"")
	//---
	http.ServeFile(w, r, path+fileName[0])
	//---
	/*
		file, err := os.Open(path + fileName[0])
		if err != nil {
			fmt.Println(err)
			return
		}
		defer file.Close()
		content, err := ioutil.ReadAll(file)
		if err != nil {
			fmt.Println("Read File Err:", err.Error())
		} else {
			w.Write(content)
		}
	*/
}
func templateHttp() {

	mux := http.NewServeMux()
	files := http.FileServer(http.Dir("public"))                //相对当前文件的public目录放js,image等文件
	mux.Handle("/static/", http.StripPrefix("/static/", files)) //请求static转到public目录
	// http://localhost:8081/static/go-logo-blue.svg
	mux.HandleFunc("/index", index)
	mux.HandleFunc("/login", login)
	mux.HandleFunc("/upload", upload)
	mux.HandleFunc("/download", downloadHandler)
	// http://localhost:8081/index
	// http://localhost:8081/login
	// http://localhost:8081/upload
	//http://localhost:8081/download?filename=中文.txt  文件内容要为UTF-8
	server := &http.Server{
		Addr:           "0.0.0.0:8081",
		Handler:        mux,
		ReadTimeout:    time.Duration(3 * int64(time.Second)),
		WriteTimeout:   time.Duration(2 * int64(time.Second)),
		MaxHeaderBytes: 1 << 20,
	}
	server.ListenAndServe()
}
//---index.html
{{ define "layout" }}

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{.Title}}</title>
  </head>
  <body>
    <div data_kind="top">
      {{ template "navbar" . }}
      <form action="login" method="post">
         <input type="text" name="email"/>
         <input type="submit" value="提交"></input>
      </form> 
    </div> 
   
  </body>
</html>

{{ end }}
//---private.navbar.html
{{ define "navbar" }}
 
    <div  data_kind="content">
      {{range .Items}}<div>{{ . }}</div>{{else}}<div><strong>no rows</strong></div>{{end}}
    </div>
 
{{ end }}


//---------https server
http.HandleFunc("/bar", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
	})
server := http.Server{
	Addr:    ":8080",
	Handler: nil,
}
/*
	openssl genrsa -out server.key 2048
	set OPENSSL_CONF=D:\Application\OpenSSL-1.1.1h_win32\OpenSSL-1.1.1h_win32\openssl.cnf
	openssl req -new -x509 -key server.key -out server.crt -days 3650
	请求 https://127.0.0.1:8080 显示不安全
*/
err := server.ListenAndServeTLS("d:/tmp/server.crt", "d:/tmp/server.key")
if err != nil {
	panic(err)
}

//---------https client
tr := &http.Transport{
	TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
}
client := &http.Client{Transport: tr}
resp, err := client.Get("https://127.0.0.1:8080/bar")

if err != nil {
	fmt.Println("error:", err)
	return
}
defer resp.Body.Close()
body, err := ioutil.ReadAll(resp.Body)
fmt.Println(string(body))


//----------flag 命令行参数
env := flag.String("env", "dev", "环境") //命令行启动，读参数
pageSize := flag.Int("size", 30, "页大小")
flag.Parse()
fmt.Println("env=", *env, "size=", *pageSize)
// go run src/arg_md5_regexp.go --help 显示参数是一个-,但如果参数过多，其实要用两个-
//go run src/arg_md5_regexp.go -env=test -size=10
//go run src/arg_md5_regexp.go --env=test --size=10
//------md5
myMd5 := md5.New()
myMd5.Write([]byte("内容"))
result := myMd5.Sum([]byte(""))
fmt.Printf("result=%x", result)
 
//---正则
//reg := regexp.MustCompile(`(1{1}[0-9]{10}){1}`)
reg := regexp.MustCompile(`(1[0-9]{10})+`)
matched := reg.FindAllStringSubmatch("my phone 13012345678 in book,your is 18011112222", -1)
fmt.Printf("matched=%v \n", matched) //%v	the value in a default format

re := regexp.MustCompile(`a(x*)b`)
fmt.Printf("%q\n", re.FindAllStringSubmatch("-ab-", -1))
fmt.Printf("%q\n", re.FindAllStringSubmatch("-axxb-", -1))
fmt.Printf("%q\n", re.FindAllStringSubmatch("-ab-axb-", -1))
fmt.Printf("%q\n", re.FindAllStringSubmatch("-axxb-ab-", -1))



//----------json
//encoding/json 包性能比较低,ffjson (https://github.com/pquerna/ffjson) 性能高些,API使用是一样的
array := [5]int{1, 2, 3, 4, 5}
//array
jsonByte, err := json.Marshal(array)
if err != nil {
	fmt.Println("error")
	return
}
fmt.Println(string(jsonByte))
//---map
pair := make(map[string]float32)
pair["age"] = 30
jsonByte, err = json.Marshal(pair)
if err != nil {
	fmt.Println("error")
	return
}
fmt.Println(string(jsonByte))
//---struct 只有大写字母开头属性才会转换
type Person struct {
	Name     string `json:"fullName"` //定义输出名字
	Birthday time.Time
	Weight   float32
}
lisi := Person{
	Name:     "lisi",
	Birthday: time.Date(2009, time.November, 10, 23, 0, 0, 0, time.UTC),
	Weight:   70.5,
}
jsonByte, err = json.Marshal(lisi)
if err != nil {
	fmt.Println("error")
	return
}
fmt.Println(string(jsonByte)) //{"fullName":"lisi","Birthday":"2009-11-10T23:00:00Z","Weight":70.5}
var emptyInter interface{}
json.Unmarshal(jsonByte, &emptyInter)
fmt.Printf("unmashal=%v \n", emptyInter) //结果是一个Map
//unmashal=map[Birthday:2009-11-10T23:00:00Z Weight:70.5 fullName:lisi]
 

//----------context
package main

import (
	"context"
	"fmt"
	"time"
)

func main() {
	// 传递带有超时的上下文
	// 告诉阻塞函数在超时结束后应该放弃其工作。
	ctx, cancel := context.WithTimeout(context.Background(), 50*time.Millisecond)
	defer cancel()
	select {
	case <-time.After(1 * time.Second):
		fmt.Println("overslept")
	case <-ctx.Done():
		fmt.Println(ctx.Err()) // 终端输出"context deadline exceeded"
	}
}
//----------socket
---tcp_server.go
package main

import (
	"fmt"
	"net"
)

//Go语言将 Non-Block + I/O多路复用 “复杂性”隐藏在Runtime中了
//只需在每个连接对应的goroutine中以“block I/O”的方式对待socket处理即可
func main() {
	l, err := net.Listen("tcp", ":8888")
	if err != nil {
		fmt.Println("listen error:", err)
		return
	}

	for {
		conn, err := l.Accept() //会阻塞
		defer conn.Close()
		if err != nil {
			fmt.Println("accept error:", err)
			break
		}

		go handleConn(conn)
	}
}
func handleConn(conn net.Conn) {
	buf := make([]byte, 1024)
	len, err := conn.Read(buf)
	if err != nil {
		fmt.Println("Read error:", err)
		panic(err)
	}
	fmt.Println("readed :", string(buf[0:len]))

}

---tcp_clent.go
package main

import (
	"fmt"
	"net"
	"time"
)

func main() {
	conn, err := net.DialTimeout("tcp", ":8888", 2*time.Second)
	if err != nil {
		fmt.Println("connect error:", err)
		return
	}
	defer conn.Close()
	conn.Write([]byte("你好 from client"))
}


---模板  "html/template" ， "text/template"
package main

import (
	"log"
	"text/template"

	//"html/template"
	"os"
)

func main() {
	//.代表传入的参数
	t1, err := template.New("foo").Parse(`{{define "T"}}Hello, {{.}}!{{end}}`)
	if err != nil {
		panic(err)
	} else {
		err = t1.ExecuteTemplate(os.Stdout, "T", "<script>alert('you have been pwned')</script>")
		//"html/template" 包结果对<做转义为&lt;
		//"text/template" 包结果对<不做处理
	}
	//管道
	t1, err = template.New("piple").Parse(`{{ 12.3456 | printf "%.2f"  }} `)
	if err != nil {
		panic(err)
	} else {
		err = t1.Execute(os.Stdout, "")
	}
	//with后面的要用双引号包起，表示这块区域的.表示这个值
	t1, err = template.New("alias").Parse(`cat is {{.}}, {{ with "arg" }}  temp arg  is {{.}} {{end}} `)
	if err != nil {
		panic(err)
	} else {
		err = t1.Execute(os.Stdout, "cat")
	}
	//变量
	t1, err = template.New("vari").Parse(`{{ range $key,$val:= . }} key= {{$key}} ,val={{$val}} {{end}} `)
	if err != nil {
		panic(err)
	} else {
		kvs := map[string]string{"a": "apple", "b": "banana"}
		err = t1.Execute(os.Stdout, kvs)
	}
	//if 有某个变量  gt
	t1, err = template.New("vari").Parse(`  
	{{ if . }}
      Number has value 
    {{ else }}
      Number is  empty
	{{ end }} 
	
	{{if gt .Age 18}}
	<p>hello, old man, {{.Name}}</p>
	{{else}}
	<p>hello,young man, {{.Name}}</p>
	{{end}} 
	`)
	if err != nil {
		panic(err)
	} else {
		type Person struct {
			Name string
			Age  int
		}
		p := Person{Name: "safly", Age: 30}
		err = t1.Execute(os.Stdout, p)
	}
	//模板 自定义函数
	funcMap := template.FuncMap{"fdate": formatDate} //自己的函数
	tFunc := template.New("customFUnc").Funcs(funcMap)
	tFunc, _ = tFunc.Parse("The date/time is {{ . | fdate }}")
	tFunc.Execute(os.Stdout, time.Now())


	/*block用类似于template语法的使用，但是不同的是block会有一个默认值，而template没有默认值
	<body>{{block "content" .}}This is the default body.{{end}}</body>
	如果你的content模板没有任何匹配的定义，将会显示默认的内容
	*/
	
	const tpl = `
	<!DOCTYPE html>
	<html>
		<head>
			<meta charset="UTF-8">
			<title>{{.Title}}</title>
		</head>
		<body>
			{{range .Items}}<div>{{ . }}</div>{{else}}<div><strong>no rows</strong></div>{{end}}
		</body>
	</html>`

	check := func(err error) {
		if err != nil {
			log.Fatal(err)
		}
	}
	t, err := template.New("webpage").Parse(tpl)
	check(err)

	data := struct {
		Title string
		Items []string
	}{
		Title: "My page",
		Items: []string{
			"My photos",
			"My blog",
		},
	}

	err = t.Execute(os.Stdout, data)
	check(err)

	noItems := struct {
		Title string
		Items []string
	}{
		Title: "My another page",
		Items: []string{},
	}

	err = t.Execute(os.Stdout, noItems)
	check(err)

	//--转义
	const s = `"Fran & Freddie's Diner" <tasty@example.com>`
	v := []interface{}{`"Fran & Freddie's Diner"`, ' ', `<tasty@example.com>`}

	fmt.Println(template.HTMLEscapeString(s))
	template.HTMLEscape(os.Stdout, []byte(s))
	fmt.Fprintln(os.Stdout, "")
	fmt.Println(template.HTMLEscaper(v...))

	fmt.Println(template.JSEscapeString(s)) //& 变 \u开头，‘ 变 \' ，< 变 \u开头
	template.JSEscape(os.Stdout, []byte(s))
	fmt.Fprintln(os.Stdout, "")
	fmt.Println(template.JSEscaper(v...)) //空格

	fmt.Println(template.URLQueryEscaper(v...)) //%形式
}
//---------gob

import (
	"bytes"
	"encoding/gob"
	"fmt"
	"io/ioutil"
)

type Post struct {
	Id      int
	Content string
	Author  string
}

// store data
func store(data interface{}, filename string) {
	buffer := new(bytes.Buffer)
	encoder := gob.NewEncoder(buffer)
	err := encoder.Encode(data)
	if err != nil {
		panic(err)
	}
	err = ioutil.WriteFile(filename, buffer.Bytes(), 0600)
	if err != nil {
		panic(err)
	}
}

// load the data
func load(data interface{}, filename string) {
	raw, err := ioutil.ReadFile(filename)
	if err != nil {
		panic(err)
	}
	buffer := bytes.NewBuffer(raw)
	dec := gob.NewDecoder(buffer)
	err = dec.Decode(data)
	if err != nil {
		panic(err)
	}
}

func main() {
	post := Post{Id: 1, Content: "Hello World!", Author: "Sau Sheong"}
	store(post, "d:/tmp/post1")
	var postRead Post
	load(&postRead, "d:/tmp/post1")
	fmt.Println(postRead)
}
//----------csv
package main

import (
	"encoding/csv"
	"fmt"
	"os"
	"strconv"
)

type Post struct {
	Id      int
	Content string
	Author  string
}

func main() {
	// creating a CSV file
	csvFile, err := os.Create("d:/tmp/posts.csv")
	if err != nil {
		panic(err)
	}
	defer csvFile.Close()

	allPosts := []Post{
		Post{Id: 1, Content: "Hello World!", Author: "Sau Sheong"},
		Post{Id: 2, Content: "Bonjour Monde!", Author: "Pierre"},
		Post{Id: 3, Content: "Hola Mundo!", Author: "Pedro"},
		Post{Id: 4, Content: "Greetings Earthlings!", Author: "Sau Sheong"},
	}

	writer := csv.NewWriter(csvFile) //csv
	for _, post := range allPosts {
		line := []string{strconv.Itoa(post.Id), post.Content, post.Author}
		err := writer.Write(line)
		if err != nil {
			panic(err)
		}
	}
	writer.Flush()

	// reading a CSV file
	file, err := os.Open("d:/tmp/posts.csv")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	reader := csv.NewReader(file) //csv
	reader.FieldsPerRecord = -1
	record, err := reader.ReadAll()
	if err != nil {
		panic(err)
	}

	var posts []Post
	for _, item := range record {
		id, _ := strconv.ParseInt(item[0], 0, 0)
		post := Post{Id: int(id), Content: item[1], Author: item[2]}
		posts = append(posts, post)
	}
	fmt.Println(posts[0].Id)
	fmt.Println(posts[0].Content)
	fmt.Println(posts[0].Author)
}


-------zip
package main

import (
	"archive/zip"
	"bytes"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os"
)

var zipFilePath = "d:/tmp/readme.zip"

func main() {
	writeZip()
	readZip()
}
func writeZip() {

	// Create a buffer to write our archive to.
	buf := new(bytes.Buffer)

	// Create a new zip archive.
	w := zip.NewWriter(buf)

	// Add some files to the archive.
	var files = []struct {
		Name, Body string
	}{
		{"readme.txt", "This archive contains some text files."},
		{"gopher.txt", "Gopher names:\nGeorge\nGeoffrey\nGonzo"},
		{"todo.txt", "Get animal handling licence.\nWrite more examples."},
	}
	for _, file := range files {
		f, err := w.Create(file.Name)
		if err != nil {
			log.Fatal(err)
		}
		_, err = f.Write([]byte(file.Body))
		if err != nil {
			log.Fatal(err)
		}
	}

	// Make sure to check the error on Close.
	err := w.Close()
	if err != nil {
		log.Fatal(err)
	}

	err = ioutil.WriteFile(zipFilePath, buf.Bytes(), os.ModePerm)
	if err != nil {
		panic(err)
	}
}
func readZip() {
	// Open a zip archive for reading.
	r, err := zip.OpenReader(zipFilePath)
	if err != nil {
		log.Fatal(err)
	}
	defer r.Close()

	// Iterate through the files in the archive,
	// printing some of their contents.
	for _, f := range r.File {
		fmt.Printf("Contents of %s:\n", f.Name)
		rc, err := f.Open()
		if err != nil {
			log.Fatal(err)
		}
		_, err = io.CopyN(os.Stdout, rc, 40)
		if err != nil && err != io.EOF {
			log.Fatal(err)
		}
		rc.Close()
		fmt.Println()
	}
}
//----------生成图片， 如验证码，二维码用第三方库
"image"
"image/gif"
"image/jpeg"
"image/png"

//----------base64
import (
	"encoding/base64"
	"fmt"
)
secretMessage := []byte("hello你好")
ciphertext := base64.StdEncoding.EncodeToString(secretMessage) //编码
fmt.Printf("base64 encode: %s\n", ciphertext)

decodeBytes, err := base64.StdEncoding.DecodeString(encodeString)//解码
if err != nil {
	log.Fatalln(err)
}
fmt.Println("decode res=", string(decodeBytes))

//-------nexus 私服 私有仓库
https://help.sonatype.com/repomanager3
https://help.sonatype.com/repomanager3/formats/go-repositories

nexus-3.5.1-02 OSS 版本没有 go(proxy)  官方文档提示写	https://gonexus.dev

export GOPROXY=https://gonexus.dev

如Go >= 1.13
go env -w GOPROXY=https://gonexus.dev,direct
go env -w GOPROXY=https://goproxy.cn,direct  下载模块的位置
编译示例项目  go build -o sample-controller  .


//---go modules
开启 go modules 功能
#1.12版本可以这个
#export GO111MODULE=on
# Set the GOPROXY environment variable
export GOPROXY=https://goproxy.io

cd go-demo
go mod init github.com/my/test   会创建go.mod文件

--go.mod
module  github.com/my/test
/*
require (
//多行用换行分隔
) 
*/
go 1.12
require github.com/you/test latest 

--

go mod edit -require github.com/you/test@latest     //@后为版本
会在go.mod 文件尾增加 require github.com/you/test latest  


go help mod tidy 看帮助 

go mod download 下载所有依赖
go mod tidy  (整洁的)  拉取缺少的模块，移除不用的模块 

go list -m all 显示所有依赖  (-m modules)
go list -m -json all 
go mod graph //打印模块依赖图
go mod verify //校验依赖
-----

//----------log  
import (
	"io"
	"log"
	"os"
) 
func main() {
	log.SetFlags(log.Llongfile | log.Lmicroseconds | log.Ldate)
	log.Println("这是一条很普通的日志。")
	v := "很普通的"
	log.Printf("这是一条%s日志。\n", v)
	//log.Fatalln("这是一条会触发fatal的日志。")//不会执行后面的代码
	//log.Panicln("这是一条会触发panic的日志。")//不会执行后面的代码

	//如何滚文件呢？
	to, err := os.OpenFile("d:/tmp/go.log", os.O_WRONLY|os.O_APPEND, os.ModePerm)
	if err != nil {
		panic(err)
	}
	logger := log.New(io.MultiWriter(to, os.Stdout), "<Prodcut_module>", log.Lshortfile|log.Ldate|log.Ltime)
	logger.Printf("这是一条%s日志。\n", v)
}

//----------mail
 报 unencrypted connection 表SMTP服务器必须配置SSL
 报 x509: certificate relies on legacy Common Name field, use SANs or temporarily enable Common Name matching with GODEBUG=x509ignoreCN=0
 
GO1.15   X509 CommonName， 过时
https://golang.google.cn/doc/go1.15#commonname
用SAN证书 SAN(Subject Alternative Name)
 



openssl genrsa -out ca.key 4096

--ca.conf
[ req ]
default_bits       = 4096
distinguished_name = req_distinguished_name

[ req_distinguished_name ]
countryName                 = Country Name (2 letter code)
countryName_default         = CN
stateOrProvinceName         = State or Province Name (full name)
stateOrProvinceName_default = JiaDin
localityName                = Locality Name (eg, city)
localityName_default        = Shanghai
organizationName            = Organization Name (eg, company)
organizationName_default    = circle
commonName                  = Common Name (e.g. server FQDN or YOUR name)
commonName_max              = 64
commonName_default          = domain.com

--

 openssl req   -new   -sha256   -out ca.csr   -key ca.key   -config ca.conf
  openssl x509 \
  -req \
  -days 3650 \
  -in ca.csr \
  -signkey ca.key \
  -out ca.crt

---server.conf
[ req ]
default_bits       = 2048
distinguished_name = req_distinguished_name
req_extensions     = req_ext

[ req_distinguished_name ]
countryName                 = Country Name (2 letter code)
countryName_default         = CN
stateOrProvinceName         = State or Province Name (full name)
stateOrProvinceName_default = Shanghai
localityName                = Locality Name (eg, city)
localityName_default        = JiaDin
organizationName            = Organization Name (eg, company)
organizationName_default    = circle
commonName                  = Common Name (e.g. server FQDN or YOUR name)
commonName_max              = 64
commonName_default          = domain.com

[ req_ext ]
subjectAltName = @alt_names

[alt_names]
DNS.1   = *.domain.com
IP      = 10.10.3.193

---

openssl genrsa -out server.key 2048

    openssl req \
      -new \
      -sha256 \
      -out server.csr \
      -key server.key \
      -config server.conf


openssl x509   -req   -days 3650   -CA ca.crt   -CAkey ca.key   -CAcreateserial   -in server.csr   -out server.pem  -extensions req_ext   -extfile server.conf

报 x509: certificate signed by unknown authority

cp ca.crt  /usr/share/pki/trust/anchors
update-ca-certificates 即可



---my_offical_email_simple.go 

import (
	"flag"
	"fmt"
	"log"
	"net/smtp"
)
	flag_fromaddr := flag.String("fromaddr", "dell-pc.domain.com", "邮件发件人@后地址")
	flag_fromuser := flag.String("fromuser", "user1", "邮件用户名")

	flag_frompass := flag.String("frompass", "user1", "邮件密码")
	flag_smtphost := flag.String("smtphost", "dell-pc.domain.com", "SMTP邮件服务器")
	flag_mailto := flag.String("mailto", "", "收件人")
	flag.Parse()
	fmt.Println("fromuser=", *flag_fromuser, ",fromaddr=", *flag_fromaddr, ",frompass=", *flag_frompass,
		",smtphost=", *flag_smtphost, ",mailto=", *flag_mailto)

	if *flag_mailto == "" {
		fmt.Println("mailto 为空")
		return
	}
//go run src/go-api/email/my_offical_email_complex.go   -mailto wang@163.com

	var (
		from = *flag_fromuser + "@" + *flag_fromaddr
		msg  = []byte("To: " + *flag_mailto + "\r\n" + //邮件中显示的收件人
			"Subject: discount Gophers!\r\n" + //邮件主题
			"Content-Type: text/html; charset=UTF-8\r\n" + //HTML
			"\r\n" +
			"This is the email body<h2>html h2</h2>")
		recipients = []string{*flag_mailto}
	)
	//go run src/go-api/email/my_offical_email_simple.go -fromaddr dell-pc.domain.com -mailto wang@163.com
	//和java一样，from不能用user1@domain.com 要用 user1@dell-pc.domain.com

/* 
// variables to make ExamplePlainAuth compile, without adding
// unnecessary noise there.
var (
	from       = "user1@dell-pc.domain.com"
	msg  = []byte("To: recipient@example.net\r\n" + //邮件中显示的收件人
		"Subject: discount Gophers!\r\n" + //邮件主题
		"Content-Type: text/html; charset=UTF-8\r\n" + //HTML
		"\r\n" +
		"This is the email body<h2>html h2</h2>") 
	recipients = []string{"wang@163.com"}
)
*/
func main() {

	// hostname is used by PlainAuth to validate the TLS certificate.
	hostname := "dell-pc.domain.com"
	auth := smtp.PlainAuth("", "user1", "user1", hostname)

	err := smtp.SendMail(hostname+":25", auth, from, recipients, msg)//SSL也要用25号端口
	// 没有附件
	if err != nil {
		log.Fatal(err)
	}
}

测试成功

--postfix/main.cf  TLS  不同java
smtpd_use_tls =yes
#smtpd_tls_loglevel = 0
smtpd_tls_CAfile =
smtpd_tls_CApath = /home/dell/Documents/postfix_tls_go/ca.crt
smtpd_tls_cert_file =/home/dell/Documents/postfix_tls_go/server.pem
smtpd_tls_key_file = /home/dell/Documents/postfix_tls_go/server.key

------ 以下配置同 java mail
--postfix/main.cf  
mydomain = domain.com
home_mailbox = Maildir/
mydestination = $myhostname, localhost.$mydomain,localhost
myhostname = dell-pc.domain.com
mynetworks_style = subnet
mynetworks=10.10.0.0/16,127.0.0.0/8
inet_interfaces = all
inet_protocols = all

smtpd_sasl_auth_enable = yes
smtpd_sasl_type = cyrus 
smtpd_sasl_path = smtpd 
---

zypper install cyrus-sasl-saslauthd
saslauthd -a pam /shadow   # 建操作系统可登录用户 user1/user1
 
/etc/postfix/master.cf 文件中放开 
smtps     inet  n       -       n       -       -       smtpd 
  -o smtpd_tls_wrappermode=yes
  -o smtpd_sasl_auth_enable=yes

还要在 /etc/services 中增加 
smtps              465/tcp  

/etc/hosts
10.10.3.193    dell-pc
10.10.3.193    dell-pc.domain.com
 
 
 
 
---my_offical_email_complex.go
package main

import (
	"flag"
	"fmt"
	"log"
	"net/smtp"
)

func main() {
	flag_fromaddr := flag.String("fromaddr", "dell-pc.domain.com", "邮件发件人@后地址")
	flag_fromuser := flag.String("fromuser", "user1", "邮件用户名")

	flag_frompass := flag.String("frompass", "user1", "邮件密码")
	flag_smtphost := flag.String("smtphost", "dell-pc.domain.com", "SMTP邮件服务器")
	flag_mailto := flag.String("mailto", "", "收件人")
	flag.Parse()
	fmt.Println("fromuser=", *flag_fromuser, ",fromaddr=", *flag_fromaddr, ",frompass=", *flag_frompass,
		",smtphost=", *flag_smtphost, ",mailto=", *flag_mailto)

	if *flag_mailto == "" {
		fmt.Println("mailto 为空")
		return
	}	
	
	
	
	// Connect to the remote SMTP server.
	c, err := smtp.Dial("dell-pc.domain.com:25")
	if err != nil {
		log.Fatal(err)
	}

	// Set the sender and recipient first
	if err := c.Mail("user1@dell-pc.domain.com"); err != nil {
		log.Fatal(err)
	}
	if err := c.Rcpt(*flag_mailto ); err != nil {
		log.Fatal(err)
	}

	// Send the email body.
	wc, err := c.Data()
	if err != nil {
		log.Fatal(err)
	}
	_, err = fmt.Fprintf(wc, "To: recipient@example.net\r\n"+ //邮件中显示的收件人
		"Subject: go email complex title!\r\n"+ //邮件主题
		"Content-Type: text/html; charset=UTF-8\r\n"+ //HTML
		"\r\nThis is the email body<h2>html</h2>")
	if err != nil {
		log.Fatal(err)
	}
	err = wc.Close()
	if err != nil {
		log.Fatal(err)
	}

	// Send the QUIT command and close the connection.
	err = c.Quit()
	if err != nil {
		log.Fatal(err)
	}
}
//---邮件带附件官方文档提示用  mime/multipart  包



import (
	"bytes"
	"encoding/base64"
	"io/ioutil"
	"log"
	"net/smtp"
	"strings"
	"time"
)

//带附件的邮件测试成功
// define email interface, and implemented auth and send method
type Mail interface {
	Auth()
	Send(message Message) error
}

type SendMail struct {
	user     string
	password string
	host     string
	port     string
	auth     smtp.Auth
}

type Attachment struct {
	name        string
	contentType string
	withFile    bool
}

type Message struct {
	from        string
	to          []string
	cc          []string
	bcc         []string
	subject     string
	body        string
	contentType string
	attachment  Attachment
}

func main() {
	var mail Mail
	mail = &SendMail{user: "user1", password: "user1", host: "dell-pc.domain.com", port: "25"}
	message := Message{from: "user1@dell-pc.domain.com",
		to:          []string{"xxx@163.com"},
		cc:          []string{},
		bcc:         []string{},
		subject:     "HELLO WORLD",
		body:        "这是邮件体<h2>大字</h2>",
		contentType: "text/html;charset=utf-8",
		attachment: Attachment{
			name:        "test.jpg",
			contentType: "image/jpg",
			withFile:    true,
		},
	}
	mail.Send(message)
}

func (mail *SendMail) Auth() {
	mail.auth = smtp.PlainAuth("", mail.user, mail.password, mail.host)
}

func (mail SendMail) Send(message Message) error {
	mail.Auth()
	buffer := bytes.NewBuffer(nil)
	boundary := "GoBoundary"
	Header := make(map[string]string)
	Header["From"] = message.from
	Header["To"] = strings.Join(message.to, ";")
	Header["Cc"] = strings.Join(message.cc, ";")
	Header["Bcc"] = strings.Join(message.bcc, ";")
	Header["Subject"] = message.subject
	Header["Content-Type"] = "multipart/mixed;boundary=" + boundary
	Header["Mime-Version"] = "1.0"
	Header["Date"] = time.Now().String()
	mail.writeHeader(buffer, Header)

	body := "\r\n--" + boundary + "\r\n"
	body += "Content-Type:" + message.contentType + "\r\n"
	body += "\r\n" + message.body + "\r\n"
	buffer.WriteString(body)

	if message.attachment.withFile {
		attachment := "\r\n--" + boundary + "\r\n"
		attachment += "Content-Transfer-Encoding:base64\r\n"
		attachment += "Content-Disposition:attachment\r\n"
		attachment += "Content-Type:" + message.attachment.contentType + ";name=\"" + message.attachment.name + "\"\r\n"
		buffer.WriteString(attachment)
		defer func() {
			if err := recover(); err != nil {
				log.Fatalln(err)
			}
		}()
		mail.writeFile(buffer, "/home/dell/Pictures/"+message.attachment.name) //本地文件
	}

	buffer.WriteString("\r\n--" + boundary + "--")
	smtp.SendMail(mail.host+":"+mail.port, mail.auth, message.from, message.to, buffer.Bytes())
	return nil
}

func (mail SendMail) writeHeader(buffer *bytes.Buffer, Header map[string]string) string {
	header := ""
	for key, value := range Header {
		header += key + ":" + value + "\r\n"
	}
	header += "\r\n"
	buffer.WriteString(header)
	return header
}

// read and write the file to buffer
func (mail SendMail) writeFile(buffer *bytes.Buffer, fileName string) {
	file, err := ioutil.ReadFile(fileName)
	if err != nil {
		panic(err.Error())
	}
	payload := make([]byte, base64.StdEncoding.EncodedLen(len(file)))
	base64.StdEncoding.Encode(payload, file)
	buffer.WriteString("\r\n")
	for index, line := 0, len(payload); index < line; index++ {
		buffer.WriteByte(payload[index])
		if (index+1)%76 == 0 {
			buffer.WriteString("\r\n")
		}
	}
}


---testing包 自动测试 ，go test 命令
单元测试覆盖率






