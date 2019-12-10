https://golang.org/  要翻墙才能出去(Google 的语言) 
支持多个平台,windows ,linux(有linux ARMv6),mac,freeBSD

https://golang.google.cn 国内可以仿问的
https://gomirrors.org/ ( https://goproxy.io/ )

RabbitMQ , MongoDB 有 go客户端

windows 1.11.2 安装后会自动设置环境变量
GOROOT=C:\Go
PATH 增加 C:\Go\bin

go version


linux GCC也可以编译GO
---hello.go
package main

import "fmt"

func main() {
   fmt.Println("Hello, World!")
}

package名和func名要相同,和文件所在目录没关系 


go run hello.go  来运行
go build hello.go  可生成.exe文件

Eclipse 4.6 以上插件 goClipse 依赖于 CDT 9.0以上    http://goclipse.github.io/
https://github.com/GoClipse/goclipse/blob/latest/documentation/Installation.md   下载
https://github.com/GoClipse/goclipse.github.io/archive/master.zip    解压后eclipse中help->install 指向release目录　
	2019-02下的是201707版本
　
 
eclipse Marketplace

Installation 设置目录(同GOROOT) C:\Go
用goClipse,如有包源文件要放相应目录下,会在bin目录下生成.exe来执行

GoLand 2018.1.2 (IntellijIdea)

Visual Studio Code 的Go  Extension



工作区目录默认为 $HOME/go
可使用 GOPATH 环境变量做修改(不能是安装目录)，模块下载在此目录下的 pkg/mod/ 目录中
go env GOPATH
export GOPATH=$(go env GOPATH)

$ mkdir-p $GOPATH/src/github.com/user/hello 
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
package stringutil //包名必须和目录名相同
func Reverse(s string) string {
	r := []rune(s)
	for i, j := 0, len(r)-1; i < len(r)/2; i, j = i+1, j-1 {
		r[i], r[j] = r[j], r[i]
	}
	return string(r)
}
任何地方 go build github.com/user/stringutil
或者在 源码目录中 go build
不产生新文件 ，把编译好的包放在本地的build cache
---vi hello.go
package main //可执行的包名必须是main
import (
	"fmt"
	"github.com/user/stringutil" //就可引用刚刚的库
)

func main() {
	fmt.Println(stringutil.Reverse("!oG ,olleH"))
}
---  
-- vi $GOPATH/src/github.com/user/stringutil/reverse_test.go  文件名以 _test.go 结尾 
package stringutil

import "testing"  //包名是testing

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
	case nil:
		fmt.Printf(" x 的类型 :%T", i)
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
	//select
   
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
	fmt.Println(max(200,120))
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
	copy(newSlice,sliceFromArrayRes) //拷贝
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

//属于 Circle结构体中的方法
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
delete(countryCapitalMap2, "France")
fmt.Println(countryCapitalMap2)



import (
	"fmt"
	"time"
)

func say(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond) //线程等待
		fmt.Println(s)
	}
}

func main() {
	go say("world") //启动新线程
	say("hello")
}

//---channel
func sum(s []int, c chan int) {
	sum := 0
	for _, v := range s {
		sum += v
	}
	c <- sum // 把 sum 发送到通道 c
}

func main() {
	s := []int{7, 2, 8, -9, 4, 0}

	c := make(chan int) //建立channel用来做线程间的通讯, 默认情况下，通道是不带缓冲区的,是阻塞式的 
	go sum(s[:len(s)/2], c)
	go sum(s[len(s)/2:], c)
	x, y := <-c, <-c // 从通道 c 中接收

	fmt.Println(x, y, x+y)
}


//---range channel

func fibonacci(n int, c chan int) {
	x, y := 0, 1
	for i := 0; i < n; i++ {
		c <- x
		x, y = y, x+y
	}
	close(c)//关闭channel
}

func main() {
	c := make(chan int, 10)//10个缓冲区

	go fibonacci(cap(c), c) //slice 的cap函数 容量

	// range 函数遍历每个从通道接收到的数据，因为 c 在发送完 10 个
	// 数据之后就关闭了通道，所以这里我们 range 函数在接收到 10 个数据
	// 之后就结束了。如果上面的 c 通道不关闭，那么 range 函数就不
	// 会结束，从而在接收第 11 个数据的时候就阻塞了。

	for i := range c { //range 来(遍历)取 channel,是在关闭之后
		fmt.Println(i)
	}

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







