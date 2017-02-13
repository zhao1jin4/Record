eclipse_4.5_plugin_Scala IDE-4.4.1   可以dropsin安装  


高性应该比Java 高, 运行在JVM上,也就是最后还是生成.class文件

Apache Kafka  是使用这种语言写的
Spark 也有 Scala的支持


环境变量
SCALA_HOME=C:\Program Files (x86)\scala\
PATH=%PATH%;%SCALA_HOME%\bin

scala  命令
scala> 可写scala 语言,如有tab会提示

object HelloWorld {
  def main(args: Array[String]): Unit = {
  println("Hello, world!")
  }
}
提示 defined module HelloWorld  
scala>  HelloWorld.main(Array()) 来执行


scalac  命令可以编译

scalac -d classes HelloWorld.scala
scala -cp classes HelloWorld 执行


如果exends App就不用写main方法了

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

object FrenchDate {
  def main(args: Array[String]) {
    val now = new Date 
    val df = getDateInstance(LONG, Locale.FRANCE)
    println(df format now)
  }
}


Unit 		表示无值，和其他语言中void等同。
Nothing 	在Scala的类层级的最低端；它是任何其他类型的子类型。
Any 	 	是所有其他类的超类
AnyRef  	是Scala里所有引用类(reference class)的基类

//变量
var myVar : String = "Foo" 

//多行字符串用   三个双引号  来表示分隔符
val foo = """第一行 
			  第二行
			  第n行"""

//方法返回值是元组
val (myVar1: Int, myVar2: String) = Tuple2(40, "Foo")




没有指定任何的修饰符，则默认为 public

在嵌套类情况下，外层类甚至不能访问被嵌套类的private成员

protected 成员 因为它只允许保护成员在定义了该成员的的类的子类中被访问。
而在java中，用protected关键字修饰的成员，除了定义了该成员的类的子类可以访问，同一个包里的其他类也可以进行访问。


 
var a = 0;
 
for( a <- 1 to 10){  //左箭头 <- 用于为变量 a 赋值。
 println( "Value of a: " + a );
}

  for( a <- 1.to( 10)){    // str.subString(2) 也可以写成 str subString 2 形式
	 println( "Value of a: " + a );
  }
 for( a <- 1 to 10 if a%2==0){    //条件
 println( "Value %2 : " + a );
}

for( a <- 1 until 10){
 println( "Value of a: " + a );
} 
var b = 0;
for( a <- 1 to 3; b <- 1 to 3){
	println( "Value of a: " + a );
	println( "Value of b: " + b );
}

val numList = List(1,2,3,4,5,6);

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
  
   printVeryChar("a","b","c")
      
     def printVeryChar(c:String*)=    //动态参数个数
     {
        c.foreach( x => println(x) )
        //c.foreach{ x => println(x) }
     }
	 
    def defaultParam(name:String="world"):String= //参数默认值
     {
       return "hello " +name
     }
     println(defaultParam("wang"))
     println(defaultParam())
	 
    def addInt( a:Int, b:Int ) : Int =  //定义函数
   {
      var sum:Int = 0
      sum = a + b
      return sum
   }
   def printMe( ) : Unit = {  //定义函数返回Unit 相当于void
      println("Hello, Scala!")
   }
        
	var factor = 3  
	val multiplier = (i:Int) => i * factor   //闭包,函数指针,这里是用=>
	println( "muliplier(1) value = " +  multiplier(1) ) 

	val buf = new StringBuilder;
	buf += 'a' //char 用一个+
	buf ++= "bcdef"  //String的字串用两个+
	println( "buf is : " + buf.toString );

	println("hello".concat(" world ")) //字串连接
	println("hello"+" world ")  //字串连接

	var formatStr= printf("inteter value =%d ,buf=%s",factor,buf);//格式化字串
	
	var z:Array[String] = new Array[String](3) //声明方式
    var z1 = new Array[String](3)//声明方式2
    z(0) = "Runoob";
	
	 var myMatrix = ofDim[Int](3,3) //Array.ofDim 二维数组
	
	
	concat( myList1, myList2) //合并数组
	
	// range() 方法最后一个参数为步长，默认为 1：
    var myList11 = range(10, 20, 2)
    var myList22 = range(10,20)
	
	
	
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
  var x: Int = xc
  var y: Int = yc
  def isEqual(obj: Any) =
    obj.isInstanceOf[Point] &&   //Any 类型的特殊方法isInstanceOf
    obj.asInstanceOf[Point].x == x // Any 类型的强转
	
def save 
   {
     println("save 100")
   }
def matchTest(x: Any ): Any  = x match {   //match 相当于switch
      case 1 => "one"
      case "two" => 2 //可是任意类型 ,每个case带break语意
      case y: Int => "scala.Int"+y  //可做类型匹配
      case _ => "many"  //最后的通配
   }
	
}

case class Person(name: String, age: Int)  //可以用于  match case中
 
match {
            case Person("Alice", 25) => println("Hi Alice!")
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
         case ex: FileNotFoundException => {
            println("Missing file exception")
         }
         case ex: IOException => {
            println("IO Exception")
         }
      } finally {
         println("Exiting finally...")
      }


 def unapply1(str: String): Option[(String, String)] = {  //Option用法,表示可有可无,有的话用Some,没有用None 
      val parts = str split "@"
      if (parts.length == 2){
         Some(parts(0), parts(1))   // Some 和 None 都继承自Option
         Some( "aa","aa11" ) //后面会覆盖前面的
      }else{
         None
      }
   }


object TestExtractClass {
   def main(args: Array[String]) 
   {
      val x = TestExtractClass(5) //会调用apply方法
      println(x)  //打印的是10
      println(x.getClass())//int
      x match
      {
        // case 10  => println("值是10")  
         case TestExtractClass(num)  //如果match中 unapply 被调用,  num 是调用unapply 返回的,函数返回类型Option
                 => println(x + " 是 " + num + " 的两倍！")  
         case _ => println("无法计算")
      }
   }
   
   def apply(x: Int) = x*2
     
   def unapply(z: Int): Option[Int] =  //z 是10 ,返回类型Option
     if (z%2==0) 
       Some(z/2)
     else 
       None
}


print("请输入  : " )
val line =  scala.io.StdIn.readLine //()可以省略 
println("谢谢，你输入的是: " + line)

scala.io.Source.fromFile("c:/tmp/hello.txt" ).foreach{ 
 print 
}
 
 var a=if(x>0)1 else 0  //特殊写法
 
 
var (m,n)=(1,2)  //多变量赋值
   
var s:String=_     // _ 表示 未给赋初值,声明要加类型
      