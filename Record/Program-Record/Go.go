Google 运行了数百万行 Python 代码，YouTube 及其 API 的前端服务器代码主要是用 Python 语言开发的，运行的是 CPython 2.7。Google 多年来一直优化 Python 代码，但始终有一个问题没有解决：并发工作负荷。Google 调查了其它 Python 运行时，但每一种都有利有弊，在解决并发性能的同时会引入新的问题。

Google 因此尝试用 Go 语言实现了一个替代运行时优化实时服务。这个项目被称为 Grumpy，将 Python 代码转译到 Go 程序，在 Go 运行时中运行，结果相当不错。

Grumpy 不支持C扩展，没有 CPython 的全局解释器锁——它被认为是影响并发性能的主要瓶颈。                  



但有些人是放弃Go 语言的   2009年,2010年发布的语言,也挺久了,未火起来了



https://golang.org/  要翻墙才能出去(Google 的语言) 
支持多个平台,windows ,linux(有linux ARMv6),mac,freeBSD


windows 1.9.2 安装后会自动设置环境变量
GOROOT=C:\Go
PATH 增加 C:\Go\bin

go version


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
https://github.com/GoClipse/goclipse.github.io/archive/master.zip    for China

eclipse Marketplace

Installation 设置目录(同GOROOT) C:\Go
用goClipse,如有包源文件要放相应目录下,会在bin目录下生成.exe来执行

GoLand 2018.1.2 (IntellijIdea)





