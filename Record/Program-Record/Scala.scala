函数式编程 FP
Scala-2.12.4  不支持JDK9


eclipse_4.5_plugin_Scala IDE-4.4.1   可以dropsin安装  
eclipse_4.6.3_plugin_Scala IDE-4.5   dropsin安装无效  about->Installation details...按钮 ->Configuration标签->View Error Log 按钮 
(不能help安装,只能MarketPlace在线安装版本老点,或者输入网址在线安装 支持play  )


高性应该比Java 高, 运行在JVM上,也就是最后还是生成.class文件

Apache Kafka  是使用这种语言写的
Spark 也有 Scala的支持
Play Framework (Java & Scala)构建Web应用 ,是基于 Akka (message-driven) 文档是epub格式 ,用Adobe Digital Editor-4.5打开后,但不能复制代码,使用Calibre 就可以复制,还可前进后退,点右上角可以切换翻页和滚页模式,还有Mac 版本的
Akka 线程创建　

环境变量
SCALA_HOME=C:\Program Files (x86)\scala\
PATH=%PATH%;%SCALA_HOME%\bin

scala  命令
scala> 可写scala 语言,如有tab会提示

 var √ =scala.math.sqrt _  // √ 可以做变量名,但要加空格  (微软拼音 按v 数学 找的)   
   var res = √ (4.0)
   println(res);
   //下面这些都可做变量名
   var ~ = -1
   var ! = 1   
   //var @ = 2//不行
   //var # = 3//不行
   var $ = 4
   var % = 5
   var ^ = 6
   var & = 7
   var * = 8
   var ** = 88
   var - = 11
   var + = 12
   var | = 13
   var / = 14
   var \ = 15
   var < = 1
   var <= = 1
   var > = 1
   var >= = 1
   var ? = 1
   //var : = 16//不行
   //()[]{}.,;'`" 这些不行的
   
   
object HelloWorld {
  def main(args: Array[String]): Unit = {
  if(args.length>0)
  {  println(args(0))  //数组使用()来取值,而且0开始的带的参数,不是程序名
     args.mkString("\n")
	 assert(args==null)
  } 
  println("Hello, world!")
  }
}
提示 defined module HelloWorld  
scala>  HelloWorld.main(Array()) 来执行
>:quit 或者  :q 来退出

|如输入错误可按两次回车取消

一元操作符(unary)   -2.0转换成方法调用 (2.0).unary_-  
 //+-!~ 是4个前置操作符
  var a=3;
  var x1= -a; //同   a.unary_-
  var x2= a.unary_-;
  var x3= a.unary_+;
  var x4= a.unary_~;
  var b=true;
  var x5=b.unary_!
  
,可以当作前缀操作符用的标识符只有+，-，!和~
如你定义了名为unary_!的方法，就可以像!p
class User2
{
	var age=3;
	 def unary_!()//自己实现 !u
	{
	  this.age+=10;
	}
}
var u=new User2();
!u;
println(u.age);

eq 同java的 == 比较地址，相反的有ne


scalac  命令可以编译

scalac -d classes HelloWorld.scala
scala -cp classes HelloWorld 执行

fsc -help
fsc HelloApp.scala   第一次执行fsc时，会创建一个绑定在你计算机端口上的本地服务器后台进程,后台进程去编译
fsc -shutdown  来关闭

如果 exends App   就不用写main方法了 ,如果想访问命令行参数的话就不能用它

object HelloWorld extends App {  // object 是对单例类的对象
  println("Hello, world!")
}


可写在linux shell中
#!/bin/sh

    exec scala "$0" "$@"
!#
    object HelloWorld extends App {
      println("Hello, world!")
    }
    HelloWorld.main(args)

注意 !#前不能有空格

//默认情况 Scala 总会引入 java.lang._ , scala._ 和 Predef._ 
import java.util.{ Date, Locale };  //多个类写一行
import java.text.DateFormat  //行结尾的;可有可元,如无使用换行
import java.text.DateFormat._   //用_表示导入所有
import java.util.{HashMap => JavaHashMap} // 重命名成员

import 可放在def中

import access.{_}  //同 import access._
import java.{sql => S} //名称S引用了java.sql包
import java.util.{Map => _, _} //引用了除Map之外的所有


object FrenchDate {
  def main(args: Array[String]) {
    val now = new Date 
    val df = getDateInstance(LONG, Locale.FRANCE)
    println(df format now)
  }
}


Unit 		表示无值，和其他语言中void等同。
Nothing 	在Scala的类层级的最低端；它是任何其他类型的子类型。 Nothing的一个用处是它标明了不正常的终止。 如 Predef对象有一个error方法 
Any 	 	是所有其他类的超类
AnyRef  	是Scala里所有引用类(reference class)的基类, 实际就是类java.lang.Object的别名 ,定义了附加的eq方法
Null
None

Any有两个子类：AnyVal和AnyRef
Scala类与Java类不同在于它们还继承自一个名为ScalaObject的特别的记号特质

 
RichByte
RichShort
RichInt
RichLong
RichChar
RichString
RichFloat
RichDouble
RichBoolean

0 max 5 
0 min 5 
-2.7 abs 
-2.7 round 
1.5 isInfinity 
(1.0 / 0) isInfinity
true
4 to 6  
"bob" capitalize 
"robert" drop 2   //  "bert"


 val name_ : Int=1;//变量名以_结尾的话 : 前要有空格

你不能写Thread.yield()因为yield是Scala的保留字。
然而，你仍可以在反引号里引用方法的名称，例如Thread.`yield`() 

import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting

var changeBuffer=ArrayBuffer [Int]()
changeBuffer+=1
changeBuffer+=2
changeBuffer++=Array(3,4,5,6)
changeBuffer.insert(0, 100,101)
println(changeBuffer)
changeBuffer.trimEnd(2) //删最后两个
changeBuffer.remove(0)
 println(changeBuffer)
var  array= changeBuffer.toArray

for(i <-0 until (array.length,2)) //2步长
  print(array(i)+",")
println("max="+array.max)       
println("sum="+array.sum)
Sorting.quickSort(array);

println(array.mkString("---", ",","==="))


var addArray= for(i <-array ) yield  i+10
println(addArray.mkString( "," ))


//变量
var myVar : String = "Foo" 

//多行字符串用   三个双引号  来表示分隔符
val foo = """第一行 
			  第二行
			  第n行"""

// 元组给变量赋值
val (myVar1: Int, myVar2: String) = Tuple2(40, "Foo")

val pair = (99, "Luftballons")  //元组
println(pair._1) //开头
println(pair._2)

val (id: Int, _)=pair  //可不要元组中的元素
   
//Scala没有静态成员,单例对象用object关键字替换了class ,定义object 名与class名相同时叫伴生对象,不与伴生同名的对象叫孤立对象
//单例对象不带参数,不能用new 

没有指定任何的修饰符，则默认为 public

在嵌套类情况下，外层类甚至不能访问被嵌套类的private成员

protected 成员 因为它只允许保护成员在定义了该成员的的类的子类中被访问。
而在java中，用protected关键字修饰的成员，除了定义了该成员的类的子类可以访问，同一个包里的其他类也可以进行访问。

def add(b: Byte): Unit = {//方法参数的一个重要特征是它们都是val
     // b += 1 // 编译不过，因为b是val 
	   b+1 //如果没有发现任何显式的返回语句，Scala方法将返回方法中最后一个计
    }
def f(): Unit = "this String gets lost"// 可以把任何类型转换为Unit
def g() { "this String gets lost too" } //没有 = ,定义结果类型为Unit 
def h() = { "this String gets returned!" }//有= ,返回 类型为String

y=x +  //以 + 结尾表示命令并没有结束 ,比字串连接
2

var a = 0; //可不写;
 
for( a <- 1 to 10){  //左箭头 <- 用于为变量 a 赋值, to包括右边界10
 println( "Value of a: " + a );
}

  for( a <- 1.to( 10)){    // str.subString(2) 也可以写成 str subString 2 形式
	 println( "Value of a: " + a );
  }
 for( a <- 1 to 10 if a%2==0){    //条件
 println( "Value %2 : " + a );
}

for( a <- 1 until 10){ //until不包括右边界10
 println( "Value of a: " + a );
} 
var b = 0;
for( a <- 1 to 3; b <- 1 to 3){
	println( "Value of a: " + a );
	println( "Value of b: " + b );
}

var numList = List(1,2,3,4,5,6); //List不可变的

for( a <- numList ){
	println( "Value of a: " + a );
}
for( a <- numList
   if a != 3; if a < 8 ){//过滤数据

	println( "Value of a: " + a );
}
      
 val loop = new Breaks;  //break方式scala.util.control.Breaks
  loop.breakable {
	 for( a <- numList){
		println( "Value of a: " + a );
		if( a == 4 ){
		   loop.break;
		}
	 }
  }
val filesHere = (new java.io.File("./src")).listFiles
def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines.toList

 def grep(pattern: String) = 
	 for { file <- filesHere 
			   if file.isFile; //多个条件用;
			   if file.getName.endsWith(".scala") 
		   line <- fileLines(file)  //双重循环
			   trimmed = line.trim  //中间变量
			  if trimmed.matches(pattern)
	  } 
	 println(file + ": " + trimmed) 
grep(".*gcd.*")


   printEveryChar("a","b","c")
      
     def printEveryChar(c:String*)=    //动态参数个数
     {
        c.foreach( (x:String) => println(x) ) //=>函数文本
//      c.foreach( x => println(x) )
        //c.foreach(println)//如果只由一个参数
        
        //c.foreach{ x => println(x) }
        
        for(x<-c) //for 遍历
          println(x)
     }
	 
	 printEveryInt(10 to 15:_*) //特殊用法
     def printEveryInt(i:Int*) 
     { 
        println(i)
     }
	 
    def defaultParam(name:String="world"):String= //参数默认值
     {
       return "hello " +name
     }
     println(defaultParam("wang"))
     println(defaultParam())
	 
    def addInt( a:Int, b:Int ) : Int =  //定义函数,参数必须要有:及类型
   {
      var sum:Int = 0
      sum = a + b
      return sum //可不写return
   }
   println(addInt(b=3,a=4)) //可以使用参数名修改顺序
   
   def add2(x:Int) (y:Int) =x+y //多参数写法2,如果函数只有一行代码可以不写{}
   println(add2(5)(3))
  
    var addVar=( a:Int, b:Int ) => a+b
    println(addVar(5,3))
	
	def printMe( ) : Unit = {  //定义函数返回Unit 相当于void
      println("Hello, Scala!")
   }
   printMe  //无参数 调用 可以不加()
    def printMe2   = { //无参数 定义 可以不加()
      println("Hello, Scala2!")
   }
   printMe2
	 

	val buf = new StringBuilder;
	buf += 'a' //char 用一个+
	buf ++= "bcdef"  //String的字串用两个+
	println( "buf is : " + buf.toString );

	println("hello".concat(" world ")) //字串连接
	println("hello"+" world ")  //字串连接

	var formatStr= printf("inteter value =%d ,buf=%s",factor,buf);//格式化字串
	
	var i=3;
    i+=1;
	i=i+1;
   //scala中没有++i,也没有i++,一定要发用i+=1或者i=i+1
   
	var z:Array[String] = new Array[String](3) //声明方式
    var z1 = new Array[String](3)//声明方式2
    z(0) = "Runoob"; //同 z.update(0, "Runoob");
	 
	var z3 = Array("Runoob", "Baidu", "Google") //实际上调用apply方法的可变参数
	  
	 var myMatrix = ofDim[Int](3,3) //Array.ofDim 二维数组
	var twoMatrix=new Array[Array[Int]](10);
      for( i <- 0 until  twoMatrix.length)
      {
        twoMatrix(i)=new Array[Int](i+1)//第二维是变长的
      }
      
	
	concat( myList1, myList2) //合并数组
	 var myList3 =myList1++ myList2
	 var zipList=for ( (item1, itme2) <-  myList1 zip  myList2) yield item1 + itme2
     println(zipList mkString "_" )
	   
	// range() 方法最后一个参数为步长，默认为 1：
    var myList11 = range(10, 20, 2)
    var myList22 = range(10,20)
	
	var x22 = x1:::x12;//List有个叫 ::: 的方法实现叠加功能
	var theList =77::x12 //把新元素放在已有List之前，返回结果List
	println(theList(2));//下标0开头
		
	var list=1::2::3::Nil //Nil有::
	//List没有append，使用 :: 再reverse 或ListBuffer 提供append再toList
    //scala.collection.mutable.ListBuffer 
   
     println( theList.reverse)
     println(theList.isEmpty) 
     println(theList.length) 
     println(theList.head)//返回第一个
     println(theList.last)//返回最后一个
     println(theList.init)//返回除最后一个以外
     println(theList.tail)//返回除第一个以外
     println(theList.drop(2));//返回去前2个的列表
     println(theList.dropRight(2));//返回去后2个的列表
     println(theList.map(s=> s+1));
     println(theList.count(s=> s>7));
     println(theList.filter(s=> s>7));
     println(theList.mkString(","));
    println(theList.sortWith((s,t)=> s<t));//小到大
     println(theList.forall(s=>s>7));//全部成功返回true
     println(theList.exists(s=>s>7))
    theList.foreach(n=>print(n+" "));
   
   
    var set1 = Set(1,3,5,7) //调用apply方法
    set1+=8 //加元素，可变和不可变都有+= ,不变的返回新的
    set1.+=(9) //也可这样写
    println("set1="+set1)
   
    //var map1 = Map("one" -> 1, "two" -> 2, "three" -> 3) 
    var map1 = Map[String,Int]()
    map1 +=("four"->4)//加元素
    println("map1="+map1)
    println(map1.getOrElse("nine", "empty")) 
   
    //var map1 = Map("one" -> 1, "two" -> 2, "three" -> 3) //是immutable.Map 不可改变的
    var map1 = Map[String,Int]()
    //map1 +=("four"->4)//加元素  生成一个新的Map
    map1 +=("four".->(4)) //实际上是方法名为  -> ,可写成"four".->(4)  , ->可以任何对象
    println("map1="+map1)
    println(map1("four"))//取元素 ,同     map1.apply("four")
	
	var mapChange= scala.collection.mutable.Map (("one" , 1), ("two", 2), ("three" , 3))
    mapChange("four")=400;//同  mapChange.update("four", 400);
    mapChange -=("four" ) 
    
    var mapSort= scala.collection.immutable.SortedMap("one" -> 1, "two" -> 2)
    
  var list=List("one","two","three")
  list match
  {
    case Nil=>0
    case h::t=>println("head="+h+",tail="+t);  //head=one,tail=List(two, three)
  }

  
  
	val 与 var 不同的是, val指定了值后不能再次赋值,相当于final
	
	 
	printPoint   

    def printPoint{ //单例对象
         println ("x 的坐标点 : ");
         println ("y 的坐标点 : " );
      }
   

   
 // 私有构造方法
class Marker private(val color:String) {

  println("创建" + this)
  
  override def toString(): String = "颜色标记："+ color   //重写toString()方法 
  
}

// 伴生对象，与类共享名字，可以访问类的私有属性和方法
object Marker{

// 单例函数调用，省略了.(点)符号  
println(Marker getMarker "blue")  
}


trait Equal {  //trait同JDK8的接口
  def isEqual(x: Any): Boolean
  def isNotEqual(x: Any): Boolean = !isEqual(x)
}

abstract class Account
 {
   def save
 }
 class Point(xc: Int, yc: Int) extends Account with Equal { // 如已经有一个extends 后面就用with 一定以extends开始
  @BeanProperty //生成getter/setter
  var x: Int = xc
  var y: Int = yc
  def isEqual(obj: Any) =
    obj.isInstanceOf[Point] &&   //Any 类型的特殊方法isInstanceOf
    obj.asInstanceOf[Point].x == x // Any 类型的强转
	
def save  //父类是abstract的，可以不加 override
   {
     println("save 100")
	 println(classOf[Point]);//Predef 中的
   }
def matchTest(x: Any ): Any  = x match {   //match 相当于switch
      case 1 => "one"
      case "two" => 2 //可是任意类型 ,每个case带break语意
      case y: Int => "scala.Int"+y  //可做类型匹配
      case _ => "many"  //最后的通配
   }
	
}
trait TraitException extends Exception
{
 
}
//   class MyFrame extends JFrame with TraitException //这里不能同时extends JFrame类和Exception类
//   {
//     
//   }
class MyException extends IOException with TraitException  // IOException 和 TraitException都继承自Exception类是可以的
{
}

class NoParamClass{}
val x=new   NoParamClass //无参数可不写()
 
 样例类，自动有 apply,unapply  
case class Person(name: String, age: Int)  //可用于  match case中

val alice =  Person("Alice", 25)//无new 调用apply

alice match {
        //case Person("Alice", 25) => println("Hi Alice!")
		case Person(name, age) => //调用unapply方法 
               println("Age: " + age + " year, name: " + name + "?")
	}	


import scala.util.matching.Regex
val pattern = "Scala".r   //返回 Regex
val str = "Scala is Scalable and cool"
println(pattern findFirstIn      str)//可以省略. ( ) 用空白分隔	
println(pattern replaceFirstIn(str, "Java"))
{
   val pattern = new Regex("(S|s)cala")
  val str = "Scala is scalable and cool"
  
  println((pattern findAllIn str).mkString(","))// 使用逗号 , 连接返回结果

}

 
 try {
         val f = new FileReader("input.txt")
      } catch {
         case _: FileNotFoundException => {//可用_
            println("Missing file exception")
         }
         case ex: IOException => {
            println("IO Exception")
         }
      } finally {
         println("Exiting finally...")
      }

def f(): Int = try { return 1 } finally { return 2 }// 调用f()产生结果值2 
def g(): Int = try { 1 } finally { 2 }  //调用g()产生1

 def unapply1(str: String): Option[(String, String)] = {  //Option用法,表示可有可无,有的话用Some,没有用None 
      val parts = str split "@"
      if (parts.length == 2){
         Some(parts(0), parts(1))   // Some 和 None 都继承自Option
         Some( "aa","aa11" ) //后面会覆盖前面的
      }else{
         None
      }
   }


object ApplyUnApply {
   def main(args: Array[String]) 
   {
        val x = ApplyUnApply(5) //会调用apply方法
      println(x)  //打印的是10
      println(x.getClass())//int
      x match
      {
        // case 10  => println("值是10")  
         case ApplyUnApply(num)  //如果match中 unapply 被调用,  num 是调用unapply 返回的,函数返回类型Option
                 => println(x + " 是 " + num + " 的两倍！")  
         case _ => println("无法计算")
      }
	  //-----------
     var name:String="wang wu";
    //var name:String="wang li sun";
     //var name:String="wang aa bb sun";
     name match
     {
       case Name(first,last)=> println("unapplySeq="+first);
       case Name(first,middle,last)=> println("unapplySeq="+middle);
       case Name(first,"aa","bb",last)=> println("unapplySeq="+last);
     }
	  
   }
   
   def apply(x: Int) = x*2
     
   def unapply(z: Int): Option[Int] =  //z 是10 ,返回类型Option
     if (z%2==0) 
       Some(z/2)
     else 
       None
}
object Name
{
  def unapplySeq(input :String):Option[Seq[String]]= // unapplySeq变长自动调用
  {
    if(input.trim()=="") 
      None
    else
      Some(input.trim().split("\\s+"));
  }
}

print("请输入  : " )
val line =  scala.io.StdIn.readLine //()可以省略 
println("谢谢，你输入的是: " + line)
StdIn.readInt();


//因.scala文件是UTF8的,中文文件件名也是UTF8的

var source=scala.io.Source.fromFile("c:/tmp/hello_utf8.txt" )
//因.scala文件是UTF8的,中文文件件名也是UTF8的
source.foreach{ 
 print 
}
source.close();

 var strArray=scala.io.Source.fromString("123 1234").mkString.split("\\s+");
 var nums=strArray.map(_.toDouble);
 
for(line <-scala.io.Source.fromURL("http://www.hao123.com").getLines() )
        //getLines返回Iterator[String] 用<-来遍历 
{ 
 println(line.length + ":" + line) 
}

for(line <-scala.io.Source.fromFile("c:/tmp/hello_utf8.txt" ).getLines().toList)
{
 println("-"*4 +line);// 字符可以*
}
 var out= new PrintWriter("c:/tmp/out.txt");
  out.printf("%5d 10.2f",20.asInstanceOf[AnyRef],34.5.asInstanceOf[AnyRef]);
  out.write( "%5d 10.2f".format(20,34.5));//更好用
  out.close();
  
  
def subdirs(dir:File):Iterator[File]=
{
	var children=dir.listFiles().filter(_.isDirectory());
	children.toIterator ++ children.toIterator.flatMap(subdirs _) // Iterator 可以 ++ 
}
for(d <- subdirs(new File("c:/temp")))
  println(d);
  
  
@SerialVersionUID(12L) //可以不写就生成默认的
class User extends Serializable{

}

var a=if(x>0)1 else 0  //if 可以有返回值
val firstArg = if (!args.isEmpty) args(0) else ""
	val friend = firstArg match   //match 同  if 可以有返回值
	  { case "salt" => "pepper" 
		 case "chips" => "salsa" 
		 case "eggs" => "bacon" 
		 case _ => "huh?" 
	  }
 
var (m,n)=(1,2)  //多变量赋值
   
var s:String=_     // _ 表示 未给赋初值,声明要加类型
 
var name="Lisi"
println( name.exists(_.isUpper))
  
var num =BigInt(5);//BigInt就是使用BigInteger实现的
 
 
 require(d != 0)  //Predif 包中
 
implicit def intToRational(x: Int) = new Rational(x)  //自动把整数转换为Rational分数的隐式转换
 

def testImplicitParam(implicit name2:String)
{
	println(name2)
}
implicit val name1="the implicit name!!"  //不根据变量名字

testImplicitParam

 
 
 
def greet() {
println("hi") 
}
println( greet() == () )//返回true 

var line = "" 
while ((line = scala.io.StdIn.readLine()) != "") // 不起作用 
println("Read: "+ line)
 
 
 //最大公约数
def gcd(x: Long, y: Long): Long = if (y == 0) x else gcd(y, x % y)
 
def processFile(filename: String, width: Int) 
{ 
	def processLine(filename:String, width:Int, line:String) //把函数定义在另一个函数中,仅在包含它的代码块中可见
	{ 
		if (line.length > width) print(filename+": "+line) 
	} 
	val source = scala.io.Source.fromFile(filename) 
	for (line <- source.getLines)
	{ 
	  processLine(filename, width, line)
	}
}

val f = (_: Int) + (_: Int)//将扩展成带两个参数的函数
val  sum= f(5, 10)


def curriedSum(x: Int)(y: Int) = x + y
val onePlus = curriedSum(1)_   //_是 第二个参数列表的占位
println( onePlus(2) )
	
def twice(op: Double => Double, x: Double) = op(op(x))
println(   twice(x => x + 1, 5) )
println(   twice(_ + 1, 5) )//最简式
	 
def sum(a: Int, b: Int, c: Int) = a + b + c
val a = sum _ //三个缺失整数参数
println(  a(1, 2, 3) )  //实际上是应用了apply方法
println( a.apply(1, 2, 3) )
val b = sum(1, _: Int, 3)
println( b(2) )


def makeIncreaser(more: Int) = (x: Int) => x + more//每次函数被调用时都会创建一个新闭包
val inc1 = makeIncreaser(1)
println(inc1(10))



private def filesHere = (new java.io.File("./src")).listFiles
def filesMatching(query: String, matcher: (String, String) => Boolean) =
{
	for (file <- filesHere; if matcher(file.getName, query)) yield file
}
def filesEnding(query: String) = filesMatching(query, _.endsWith(_))//第一个 _ 对应matcher的第一个参数file.getName,等同下面
println(filesEnding(".scala").mkString("\n"))



递归函数 
def approximate(guess: Double): Double =
 if (isGoodEnough(guess)) guess 
	else approximate(improve(guess)) 
使用while循环编写 
def approximateLoop(initialGuess: Double): Double =
 { var guess = initialGuess 
	while (!isGoodEnough(guess)) 
		guess = improve(guess) 
	guess 
}
 
 两种approximate版本哪个更好？就简洁性和避免var而言，第一个，函数式的胜出。
 
Scala编译器可以应用一个重要的优化。
在它们最后一个动作调用自己的函数，被称为尾递归：tail recursive
。Scala编译器检测到尾递归就用新值更新函数参数，然后把它替换成一个回到函数开头的跳转
   
   
   
 def withPrintWriter(file: java.io.File)(op: java.io.PrintWriter => Unit)
{ val writer = new java.io.PrintWriter(file)
  try { op(writer) } finally { writer.close() } 
}
val file = new java.io.File("c:/temp/date.txt") 
withPrintWriter(file) { writer => writer.println(new java.util.Date) } //参数可以用{}传


var assertionsEnabled = true 
def myAssert(predicate: () => Boolean) =   //叫名参数
  if (assertionsEnabled && !predicate()) 
	throw new AssertionError

myAssert(() => 5 > 3)//
//myAssert(5 > 3)  //编译报错

def byNameAssert(predicate: => Boolean) =   //省了() ，但还和平常的多了=> ,有先后的差别
{ print("in byNameAssert")
  if (assertionsEnabled && !predicate) throw new AssertionError //这里第二步才开始产生true
}  
byNameAssert(5 > 3) 

def boolAssert(predicate: Boolean) = 
{     print("in boolAssert")
	if (assertionsEnabled && !predicate) throw new AssertionError
}
 boolAssert(5 > 3)//这里第一步产生true
 /*
 boolAssert(x / 0 == 0)
 byNameAssert(x / 0 == 0)
 */
 
 
 class WontCompile {
      private var f = 0 
      //def f = 1 // 编译不过，因为字段和方法重名,因为无参数函数，调用时可以不写（），如果这样就区分不出哪个了
   }
   
class ArrayElement( // 请注意，小括号
    val contents: Array[String] )  //即是构造参数,又是属性
    extends AnyRef
class LineElement(s: String) extends ArrayElement(Array(s))//继承时初始化,像C++
   
-- order 
   
X extends Ordered[Rational] {  //实现compare方法，就可以排序，自动实现了 > ,<,<=,>= , 没有为你定义equals方法
	def compare(that: Rational) = (this.numer * that.denom) - ( that.numer * this.denom)
} 
   
---super

abstract class IntQueue //trait 或 abstract class 都可
{  
  def put(x: Int)
} 
trait Incrementing extends IntQueue
{ 
  println("constructor Incrementing"); // trait 构造器
  
  abstract override def put(x: Int) //必须在trait中，必须是 abstract override 
  {
    super.put(x + 1) //在trait中super其实是子类的实现
  } 
}
trait Filtering extends IntQueue 
{ abstract override def put(x: Int) 
  {
    if (x >= 0)
      super.put(x) //同 super[IntQueue].put(x);
  }
}
import scala.collection.mutable.ArrayBuffer
class BasicIntQueue   extends IntQueue  //也要 extends IntQueue 
{
  val buf = new ArrayBuffer[Int]
  def put(x: Int)
  { 
    buf+=x
    println(" put in BasicIntQueue ")
  } 
}
class MyQueue extends BasicIntQueue with Incrementing with Filtering //1,2
//class MyQueue extends BasicIntQueue with Filtering with Incrementing   // 0,1,2
//次序非常重要。 最右侧特质的方法首先被调用。如果那个调用了super，它调用其左侧特质的方法
//即当数为0时,Filter未调到super,即当数为1时,Filter 调到super再向左调用Incrementing，因又调了super，再向左调BasicIntQueue

var testSuper:MyQueue=new MyQueue()
testSuper.put(-1);
testSuper.put(0);
testSuper.put(1);


package outpkg {
  package inpkg { //像C# 格式
  
  
//可仿问 本类所在包 的 同级包

new _root_.three1pkg.Lanch()//如和外部文件包名同名,加_root_ 跳到顶级包开始找
 
lazy val rows=scala.io.Source.fromFile("c:/temp/hell.txt")  //lazy 延迟初始化    每次都要检查是否初始化，不是那高效
 
 scala -g:notailcalls 或者 scalc 下加入 ，会得到一个长长的信息
 def bang(x: Int): Int ={
	if( x == 0) 
		throw new Exception("bang!") 
	else 
		bang(x-1)
}
 bang(5) 
   
   class Persion(@BeanProperty var age:Int)//主构造器 也可 private
   {
      var _name:String="";
     def this(name:String)//辅助构造器
     {
       this(20);//调用主构造器,必须第一行调用
       _name=name;
       println(age);
     }
   }
   
   class MyProg
   {
     private val pros=new Properties;
     pros.load(new FileReader("my.properties"));
     //以上是主构造器
	}
	
	
	class Outer(name:String)
   { 
       out=> //指向Outer.this
      class Inner{
       def desc=out.name
      
     }
   }
   val outer=new Outer("lisi")//必须是val 
   //var in = out.new Inner();//Java做法
   var in = new outer.Inner ;//Scala做法
   println(in.desc)
   
   trait LoggedException {
     this:Exception => //只能是Exception或者子类  才能 extends 这个
       def log {
         println(getMessage());//getMessage() = this.getMessage() 
       }
   }
//   var j=new JFrame   with LoggedException{ //JFrame不是Exception的子类不行
//   }
   var i=new IOException  with LoggedException{
   }
   
   
    trait LogMethodException 
   {
     this: {  def getMessage():String  } => // 子类必须有  这个方法签名
   }
    var rightMethod=new IOException  with LogMethodException{
    }
//    var wrongMethod=new JFrame  with LogMethodException{//JFrame 没有getMessage方法 
//    }
   
extends App 类可以在运行加 -Dscala.time 可显示运行时间


object  ColorEnum extends Enumeration  
{
  //枚举类型是 ColorEnum.Value
  type ColorEnum=Value;//内部别名 ，外面就可以用ColorEnum,而不是ColorEnum.Value
  val Red,Blue=Value; 
  val Green=Value(30,"greenName");
  val Pink=Value("pinkName"); //id=31，最近的 + 1
}
doJudge(ColorEnum.Red);
doJudge(ColorEnum.withName("Blue"));//取某个
doJudge(ColorEnum(30)); 

printAllEnum();
def  doJudge(  color:ColorEnum.Value):Unit= //枚举类型是 ColorEnum.Value
{
if(color==ColorEnum.Red)
  println(color.id+"---"+color);//toString反回name
else
   println(color.id+"==="+color);
}

import clazz_test.ColorEnum._
def printAllEnum( ) 
{
 for(c <- ColorEnum.values)
 {
	var color:ColorEnum=c;//可以用ColorEnum,而不是ColorEnum.Value
	println(color.id+"  xxx  "+color);
 }
}



class Creture
{
  val range:Int =10
  var env:Array[Int]=new Array[Int](range);
}
//class Ant extends Creture
//{
//  override val range:Int=2; //没有效果,env长度是0
//}
 // scalac 中加 -Xcheckinit  未初始化字段被仿问就抛异常

class Ant extends  
{
   override val range:Int=2; //OK
} with Creture

println(new Ant().env.length)


  import sys.process._
    //如scala>下执行要多加import scala.language.postfixOps 
    var res:Int="ls -la .." !  // !就是  ProcessBuilder ProcessImplicits stringToProcess 执行shell , windows下不行
    //要有空行
    
    println("res="+res) //成功返回0，日志输出有结果，但拿不到 
    
    var resConole="ls -al .." !! // 返回字串结果
    
    println("resConole="+resConole)
    
    println("---pipe---" )
    "ls -al .." #| "grep scala" ! //  管道
   
    "ls -al .." #> new File("ls.txt") ! 
    
    "echo aaaaaaaaaaaaaaaa" #>> new File("ls.txt") !
    
    "ls -lrt" #>> new File("ls.txt") ! 
     
    println("---input file---" )
    "grep scala" #< new File("ls.txt") ! 
    
   println("---input URL---" )
    "grep jquery" #< new URL("http://www.scala-lang.org/download/") ! //不支持https
    
    
   // a #&& b  如果a成功 再执行b了 
   // a #|| b  如果a不成功再执行b了
   val p=Process("ls ",new File("/tmp"),("LANG","en_US"));//在指定目录，指定环境变量 执行命令
    p!




