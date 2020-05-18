gopher  囊地鼠

https://golang.org/  要翻墙才能出去(Google 的语言) 
支持多个平台,windows ,linux(有linux ARMv6),mac,freeBSD

https://golang.google.cn 国内可以仿问的
https://gomirrors.org/ ( https://goproxy.io/ )

https://golang.google.cn/pkg/ 文档基本上每个 方法都有示例代码，非常好


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
MySQL官方不提供Go的Connector
RabbitMQ官方不提供Go的Connector
redis/kafka Go支持 都是github上的
读写excel 或 生成pdf 使用github项目

kubernetes的restful使用的是github上的 https://github.com/emicklei/go-restful

动态网页不行??? 只能 restful 前后端分离 ???  ,表单提交,文件上传 下载(http 包???),mvc框架???
依赖管理服务??国内的网络有防火墙?? (go module ) 

没有跨平台的图表界面 ??? 还是linux的GTK ??? 主要用于后台服务

依赖为何都是要源码,全是github或者google.golang.org上的 ，为何没有类似.a包可以依赖?? 没有中央仓库管理.a包吗??

是否可以远程调试

----Eclipse 4.6 以上插件 goClipse 依赖于 CDT 9.0以上  只安装 Main Feature组即可
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


---GoLand 2018.2.4 (IntellijIdea)
	Settings-> Go -> GoPATH 下有 global GOPATH (默认是~/go) 配置 
	
	右击 xx_test.go -> create TestXX (方法名) in xx.go(文件名) ,
	如运行报找不到方法, Settings-> Go -> GoPATH 下project GOPATH ,增加要把项目目录(代码要在src目录下)
	debug时不依赖gdb
	
---Visual Studio Code(vscode) 的Go  Extension
	打开.go文件时  提示 go-outline  命令找不到,使用  go get -v github.com/ramya-rao-a/go-outline 安装(就一个main.go文件)
		又提示 不认导入路径  golang.org/x/tools/go/buildutil , get连接失败，被墙了
	https://github.com/golang/tools/tree/master/go/buildutil 中有
	项目下载下来,cd tools-master\go\buildutil 执行 go install 
	再go get 就可以了, ~\go\bin下就有go-outline.exe
	
	go help get ,显示 -v verbose,-u update
		
	安装后,Debug视图->上方的设置按钮生成.vscode/launch.json
	{ 
		"version": "0.2.0",
		"configurations": [
			{
				"name": "Launch",
				"type": "go",
				"request": "launch",
				"mode": "auto",
				"program": "${fileDirname}",
				"env": {},
				"args": []
			}
		]
	}
	如没有生成launch.json,不能运行xx_test.go文件,如有则可以(文件在任何目录，没有设置GOPATH,代码没有放在src目录下)
	
	打开.go文件 -> 调试启动按钮，提示 "dlv" 找不到.使用  go get -v github.com/go-delve/delve/cmd/dlv 安装,要花点时间下载的
	~\go\bin下就有dlv.exe
	
	https://github.com/go-delve/delve/tree/master/cmd/dlv  是可浏览的
	也可  go install ...

	debug时不依赖gdb
---

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
	"github.com/user/stringutil" //就可引用刚刚的库,是文件夹名
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
任何地方 go test github.com/user/stringutil
或进入目录 go test


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
	switch i := xIterface.(type) { //特殊写法
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
		fmt.Println("3、case 条件语句为 false")
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
		fmt.Printf("len=%d,cap=%d,ok=%t\n",len(c),cap(c),ok)
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
//fmt.Printf("%c\n",cn)//%c有点复杂

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
	fmt.Printf("%q\n", strings.Split("a,b,c", ","))
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
	f, err := strconv.ParseFloat("3.1415", 64)
	i, err := strconv.ParseInt("-42", 10, 64)
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
	if s,ok:=m.(*Student);ok { //断言，强转为子类
		s.walk()
	}else {
		m.walk();
	}
	//方试二 强转为子类
	switch s:=m.(type) {
		case  *Student:
	  		s.walk()
		default:
			s.walk();
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

	s.walk();//子类可以调用父类的方法

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
	in:=bufio.NewReader(os.Stdin) //支持多个空格分隔都能读到
	str,_:=in.ReadString('\n');
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
	return io.CopyBuffer(to,from,buf)
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
		data,err:=reader.ReadString('\n') //还有 reader.ReadBytes('\n') ，reader.ReadByte()
		if err == io.EOF {
			break;
		}
		fmt.Printf("err=%s,data=%s",err,string(data))
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
		fmt.Printf("%s %s/%s\n",tree,dir,item.Name())
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
	//Go是静态语言，编译时类型已经确定,动态类型要求是接口,每个接口会记录(pair)值(ValueOf)，和类型(ValueOf,即%T)

	var pi float64 =3.14 //任何类型都可以看到是一空接口类型，如float32也是
	fmt.Println("type=",reflect.TypeOf(pi)) //TypeOf参数如是空接口返回nil
	fmt.Println("value=",reflect.ValueOf(pi)) //ValueOf参数如是空接口返回0
	//接口变量 -> 反射对象
	var valReflect reflect.Value = reflect.ValueOf(pi)
	fmt.Println("is float32=",valReflect.Kind() == reflect.Float64)
	fmt.Println("Type=",valReflect.Type() )
	fmt.Println("Float=",valReflect.Float() )//如float32类型精度不准3.140000104904175，默认是float64

	// 反射对象 -> 接口变量
	convertVal:=valReflect.Interface().(float64)//对已知类型
	fmt.Println("convertVal=",convertVal)

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
	res:=resSlice[0].Interface().(string)
	fmt.Println("res=",res)

	///---对象的方法
	//refType:=reflect.TypeOf(p2)
	refValue:=reflect.ValueOf(p2)//指针
	fmt.Printf("refValue =%v \n",refValue)
	//refElem :=refValue.Elem()//指针的指针才要这一步
	valMethod:=refValue.MethodByName("PrintInfo") //方法名一定要大写
	fmt.Printf("valMethod =%v \n",valMethod)
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
			fldAge.SetInt(30)
			fmt.Println("after change age =",p2.Age)
		}
	}
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







//-------nexus 私服 私有仓库
https://help.sonatype.com/repomanager3
https://help.sonatype.com/repomanager3/formats/go-repositories

nexus-3.5.1-02 OSS 版本没有 go(proxy)  官方文档提示写	https://gonexus.dev

export GOPROXY=https://gonexus.dev

如Go >= 1.13
go env -w GOPROXY=https://gonexus.dev,direct



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






