Swift 3

http://m.swift51.com/swift3.0/  中文教程


Xcode 8.2.1 看Swift 版本是2.3
.//Contents/Developer/Toolchains/Swift_2.3.xctoolchain/usr/bin/swift  -v  
.//Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/swift  -v 

没有指针,不用引用计数




import Foundation

print("hello\n world")  //没有println

var a = 1
let pageSize = 20  		//常量

var he = "hello"  		//可自动类型推断
var wo:String = "world"
var num:Int = 100
print(he+wo+"the num=\(num) ") //数字和字串相连  \(num)

var arr:Any=["one","two",100,2.5]  //数组类型可混用
var emptyArray = [Int]() //只可固定类型的空数组
var emptyArray1 = Array<Int>()
var emptyArray2 = [Int]()
var emptyArray3:Array<Int> = []
var emptyDictionary1 = Dictionary<Int,String>()
var emptyDictionary2 = [Int:String]()

var dic=["key1":"value1","key2":"value2"]
dic["newKye"]="newValue"


for index in 0...10{		//3个...
    emptyArray.append( index )
}

for value in emptyArray{
    print(value)  // 自带\n的
}

var i=0
while i<emptyArray.count
{
    print(emptyArray[i])
    i=i+1  //新版本没有 ++
}

for (key,val) in dic{   //遍历字典
    print("\(key)=\(val)")
}


func sayHello(name:String) //函数
{
    print("helo \(name)")
}

sayHello(name:"lisi")

func  getNums() ->(Int,Int) //返回多个值的类型
{
    return (2,3)
}

let (a:Int,b:int)=getNums()

var say=sayHello
say("wang")


class Hi
{
    func sayHi()
    {
        print("hi ")
    }
}

var hi=Hi()
hi.sayHi()

class ChildHi:Hi
{
    var _name:String
    init(name:String)  //构造方法
    {
        self._name=name
    }
    override func sayHi()
    {
        print("hello \(self._name)")
    }
}


var childHi=ChildHi(name:"lisi")
childHi.sayHi()









