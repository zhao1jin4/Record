 

https://kotlinlang.org/docs/tutorials/command-line.html
https://github.com/JetBrains/kotlin/releases/tag/v1.3.31   离线版本 github　上下载很慢，后面总是失败 (有kotlin-native)


 在线安装方法
 curl -s https://get.sdkman.io | bash
 sdk install kotlin　　//下载不了
 
 
Kotlin Native　能编译成本地代码,不用JVM,基于 LLVM (apple用这个)
可以把Kotlin编译成JavaScript. 推荐的方法是使用Gradle 


Eclipse Marketplace  搜索 Kotlin ,目前最新的是Kotlin Plugin for Eclipse 0.8.13(Kotlin 1.2.60)
不能执行kts文件,debug不能鼠标看变量的值,有些看不到源码,如listOf(),

Intellij IDEA 的官方(jetbrains)语言
/opt/ideaIU-2018.2.6-no-jdk/idea-IU-182.5107.16/plugins/Kotlin/kotlinc/bin
chmod 755 ./kotlin  ./kotlinc
./kotlin -version  版本是 1.2.51




fun main(args: Array<String>) {
    println("Hello, world!")
}


fun main(args: Array<String>) {
    if (args.size == 0) {
        println("Please provide a name as a command-line argument")
        return
    }
    println("Hello, ${args[0]}!")
}

fun main(args: Array<String>) {
    for (name in args)
        println("Hello, $name!")
}

kotlinc hello.kt -include-runtime -d hello.jar //如变量从未使用,会报warn,ecipse,idea就不会
   -include-runtime  让 .jar 文件包含 Kotlin 运行库,可以java -jar hello.jar
  
   供其他 Kotlin 程序使用，可无需包含 Kotlin 的运行库

kotlin -classpath hello.jar HelloKt    //HelloKt 为编译器为 hello.kt 文件生成的默认类名

脚本语言使用，文件后缀名为 .kts
--list_forlders.kts
 import java.io.File
 
 val folders = File(args[0]).listFiles { file -> file.isDirectory() }
 folders?.forEach { folder -> println(folder) }
// idea 可以运行,eclipse就不行报java.lang.ClassNotFoundException: gnu.trove.THashMap
  
kotlinc -script list_folders.kts 



默认导入包
kotlin.*
kotlin.annotation.*
kotlin.collections.*
kotlin.comparisons.*
kotlin.io.*
kotlin.ranges.*
kotlin.sequences.*
kotlin.text.*


	
	vars(1,2,3,4,5)  //变长参数
	println()
 
 //不可变的val ,同scala
	val sumLambda: (Int, Int) -> Int = {x,y -> x+y}//类似scala/java,但更繁锁
    println(sumLambda(1,2))
	
	var a = 1
	val s1 = "a is $a" //模板中变量值
	println(s1)
	
	a = 2
	// 模板中的方法
	val s2 = "${s1.replace("is", "was")}, but now is $a"
	println(s2)
	
	
	//类型后面加?表示可为空
	var age: String? = "23" 
	//抛出空指针异常
	val ages = age!!.toInt()
	
	age=null
	//不做处理返回 null
	val ages1 = age?.toInt()
	//age为空返回-1
	val ages2 = age?.toInt() ?: -1
	
	getStringLength("123")
 
	for (i in 1..4) print(i) // 输出“1234”
	// downTo step  
	for (i in 1..4 step 2) print(i) // 输出“13”
	for (i in 4 downTo 1 step 2) print(i) // 输出“42”
	for (i in 1 until 10)     // i in [1, 10) 排除了 10
		println(i)



val oneMillion = 1_000_000 //同Java
	
   //Kotlin 中没有基础数据类型，只有封装的数字类型
	//三个等号 === 表示比较对象地址
	var num1: Int = 10000
    println(num1 === num1) // true，值相等，对象地址相等

    //经过了装箱，创建了两个不同的对象
    val boxedA: Int? = num1 //定义一个变量 Kotlin 帮你封装了一个对象
    val anotherBoxedA: Int? = num1

    //虽然经过了装箱，但是值是相等的，都是10000
    println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
    println(boxedA == anotherBoxedA) // true，值相等
	
	
	
	val by: Byte = 1 // OK, 字面值是静态检测的
	//val num2: Int = by // 错误 
	val num2: Int = by.toInt() // OK

//	对于Int和Long类型 位操作 
//	shl(bits) – 左移位 (Java’s <<)
//	shr(bits) – 右移位 (Java’s >>)
//	ushr(bits) – 无符号右移位 (Java’s >>>)
//	and(bits) – 与
//	or(bits) – 或
//	xor(bits) – 异或
//	inv() – 反向
	
	decimalDigitValue('6')
	
	 //[1,2,3]
    val arr = arrayOf(1, 2, 3)
    //[0,2,4]
    val b = Array(3, { i -> (i * 2) })  //size,i是下标
    println(arr[0])
    println(b[1])
	val validNumbers: IntArray = intArrayOf(1, 2, 3)
	validNumbers[0] = validNumbers[1] + validNumbers[2]
	
	val text = """
    |多行字符串 
    |abc
    """.trimMargin()// trimMargin 前置空格删除 
    println(text)
	
	 val price = """
    ${'$'}9.99 
    """  //$ 字符（它不支持反斜杠转义）
    println(price)
 
 //------when
	num1=2
	when (num1) {  //when 
		1 -> print("x == 1")
		2 -> print("x == 2") 
		else -> {  //else也有->
		print("x 不是 1 ，也不是 2")
		}
	}
    when (num1) {
	    in 1..10 -> print("x is in the range") //in 
	    in validNumbers -> print("x is valid")
	    !in 10..20 -> print("x is outside the range")  //!in
	    else -> print("none of the above")
    }
	

	var str="prefix"
	var res= when(str) {
	    is String -> str.startsWith("prefix")
	    else -> false
	}
	print(res)
	
	 
	val itemsSet = setOf("apple", "banana", "kiwi")//setOf
	when {
        "orange" in itemsSet -> println("juicy")
        "apple" in itemsSet -> println("apple is fine too")
    }
	
	var array: Array<String> = arrayOf("123","455")//arrayOf
 
 //---for
	for ((index, value) in array.withIndex()) {   //类似go
	    println("the element at $index is $value")
	}
	
	val items = listOf("apple", "banana", "kiwi")//listOf
	for (item in items) {
	 println(item)
	}

	for (index in items.indices) {//indices
	 println("item at $index is ${items[index]}")
	}
	//同Java一样的while,do..while,continue,break
	loop@ for (i in 1..100) {//标签
	    for (j in 1..100) {
	        if (j==3 && i==2) break@loop
	    }
	} 
	val ints = intArrayOf(1, 2, 3, 0, 4, 5, 6) //intArrayOf
   /*
	ints.forEach { //forEach接收一个lamda
        if (it == 0) return //要用it变量拿到值,这里的return 是main返回了
        print(it)
    }
     //123 
	*/
	ints.forEach lit@ {
	    if (it == 0) return@lit //这个只是返回了lamda
		//return@a 1  //意为"从标签 @a 返回 1"
	    print(it)
	} //123456
	 
	ints.forEach {
		if (it == 0) return@forEach //也可 函数同名
	    print(it)
	}
	 ints.forEach(fun(value: Int) { //像JS一样 传一个函数
        if (value == 0) return
        print(value)
    })


	//---class 
	Greeter("World!").greet()//实例化没有new 

	Greeter("World!").greet()//实例化没有new  
	var person: Person = Person()
	person.lastName = "wang"
	println("lastName:${person.lastName}")
	person.no = 9
	println("no:${person.no}")
	person.no = 20
	println("no:${person.no}")

	var p2: Person = Person("li",3)
	//DontCreateMe();//不能实例化

	val demo = Outer.Nested().foo() //外部类.嵌套类().嵌套类方法()/属性
	println(demo)    // == 2

	val demo0 = Outer().Inner().foo()
	println(demo0) //   1
	val demo2 = Outer().Inner().innerTest()
	println(demo2)   

	//inner关键字，外部类要加(),格式  外部类().内部类().
	val demo0 = Outer().Inner().foo()
	println(demo0)
	val demo2 = Outer().Inner().innerTest()   
	println(demo2)

	var test = Test();
	test.setInterFace(object : TestInterFace { //匿名内部类
		override fun test() {
		    println("对象表达式创建匿名内部类的实例")
		}
	});
    
 
fun sum(a: Int, b: Int): Int {//返回类型同scala
    return a + b
}
fun plus(a: Int, b: Int) = a - b // = 可无返回类型
public fun multi(a: Int, b: Int) = a * b   //: Int

fun printSum(a: Int, b: Int): Unit {  //Unit
    print(a + b)
}
fun vars(vararg v:Int){ // 变长参数 vararg 
    for(vt in v){
        print(vt)
    }
}

fun getStringLength(obj: Any): Int? { //Any ,返回值可为null
  if (obj is String) {//类型判断 
    return obj.length 
  }else if (obj !is String) // !is
    return null
  return null
}
fun decimalDigitValue(c: Char): Int {
    if (c !in '0'..'9')
        throw IllegalArgumentException("Out of range")
    return c.toInt() - '0'.toInt()
}

class Greeter(val name: String) { //constructor可省略 
//class  Greeter constructor(val name: String) {
   fun greet() { 
      println("Hello, $name")
   }
}
class Person  constructor(firstName: String) { 
    var lastName: String = "zhang"
        get() = field.toUpperCase()  　//field特殊字段 
        set

    var no: Int = 100
       // get() = field
		get   //可以简写
        set(value) {
            if (value < 10) {  　
                field = value
            } else {
                field = -1     　
            }
        }
    var heiht: Float = 145.4f
        private set
	//lateinit var obj: Greeter // lateinit 延迟初始化，第一次使用时
	init { //主构造函数,idea被没被调用了,eclipse不能编译???
        println("FirstName is $firstName")
    }
	constructor (name: String, age:Int) : this(name) {//次构造函数，this调用另一构造函数
        println("构造函数")
    }
}
 
class DontCreateMe private constructor () {//构造器，私有，单例
}
class Customer(val customerName: String = "")//val默认值

open class Base {  //open 允许它可以被继承
    open fun f() {}// 允许子类重写
}

abstract class Derived : Base() { //继承用:类似C++,抽象成员在类中不存在具体的实现
    override abstract fun f()　//override　 类/方法是abstract
}
class Outer {
    private val bar: Int = 1
    class Nested {             // 嵌套类
        fun foo() = 2
    }
    var v = "成员属性"
    inner class Inner { //inner 内部类
        fun foo() = bar  // 访问外部类成员
        fun innerTest() {
            var o = this@Outer //获取外部类的成员变量
            println("内部类可以引用外部类的成员，例如：" + o.v)
        }
    }
}




classModifier: 类属性修饰符，标示类本身特性。
abstract    // 抽象类  
final       // 类不可继承，默认属性
enum        // 枚举类
open        // 类可继承，类默认是final的
annotation  // 注解类
accessModifier: 访问权限修饰符

private    // 仅在同一个文件中可见
protected  // 同一个文件中或子类可见
public     // 所有调用的地方都可见
internal   // 同一个模块中可见

Kotlin 中所有类都继承该 Any 类 ,同scala

Any 默认提供了三个函数： 
equals() 
hashCode() 
toString()
注意：Any 不是 java.lang.Object。


open class Person(var name : String, var age : Int){//open的类也可有构造函数

}
//如子类没有主构造函数，则必须在每一个二级构造函数中用 super 关键字初始化基类
class Student2 : Person {
	constructor(name : String, age : Int) : super(name,age)
	{
	} 
	constructor(name : String, age : Int,  no: String): super(name,age)
	{
	}
}

open class A {
    open fun f () { print("A") }
    fun a() { print("a") }
}

interface B {//同Java 8 方法可以有实现
    fun f() { print("B") } //接口的成员变量默认是 open 的
    fun b() { print("b") }
}

class C() : A() , B{ //多继承
    override fun f() {
        super<A>.f() //指定父类方法 
        super<B>.f()
    }
}
//可以用一个var属性重写一个val属性，但是反过来不行
interface Foo {
	// 接口中的属性只能是抽象的，不允许初始化值，接口不会保存属性值，实现接口时，必须重写属性：
    val count: Int
	var name:String
} 
class Bar1(override val count: Int,override var name: String ) : Foo
class Bar2 : Foo {
    override var count: Int = 0
	override var name: String = "123" 
}


class User(var name:String) 
fun User.Print(){ //类的扩展函数 ,像js的prototype
    println("用户名 $name")
}

open class ExtParent {
   open fun one() { println("ExtParent函数") }
}
class ExtChild: ExtParent() {
	 override fun one() { println("ExtChild函数") }
}
fun ExtParent.foo() = "parent"　
fun ExtChild.foo() = "child"　
fun ExtChild.one() = "one"
fun printFoo(c: ExtParent) {
    println(c.foo())  //传子类也是父类的方法 ，扩展函数不支持多态 
    println(c.one())//当  扩展函数  和 类函数 重名时 优先用类函数
}

//就可以null.toString();
fun Any?.toString(): String {//Any加?
    if (this == null) return "null" // 可用this
    return toString()
}
//对属性进行扩展,没有(),允许定义在类或者kotlin文件中，不允许定义在函数中,不允许被初始化，只能由显式提供的 getter 定义,扩展属性只能被声明为 val
//同Java的<T>
val <T> List<T>.lastIndex: Int
    get() = size - 1


class MyClass {
    companion object { }   // 生成伴生对像名为"Companion"
}
fun MyClass.Companion.foo() {
    println("伴随对象的扩展函数")
}
val MyClass.Companion.no: Int //伴随对象的扩展属性
    get() = 10
//通常扩展函数或属性定义在顶级包下，包之外的用import导入扩展的函数名

class ExtD {
    fun bar() { println("D bar") }
	fun bar1() { println("D bar1") }
}
class ExtC {
    fun baz() { println("C baz") }
    fun bar1() { println("C bar1") }  //  同名方法
    fun ExtD.foo() { //定义另一个类的扩展
		bar()   // 调用 D.bar ，另一个类的函数
		baz()   // 调用 C.baz  ，本类的函数
		bar1()  //同名默认调用ExtD
		this@ExtC.bar1()  //使用this@ 指定
	}
    fun caller(d: ExtD) {
        d.foo()   // 调用扩展函数
    }
}

//ExtC 被成为分发接受者，而 ExtD 为扩展接受者

open class D {
}

class D1 : D() {
}

open class TheC {
    open fun D.foo() {
        println("D.foo in C")
    }

    open fun D1.foo() {
        println("D1.foo in C")
    }

    fun caller(d: D) {
        d.foo()    
    }
}
class C1 : TheC() {
    override fun D.foo() {
        println("D.foo in C1")
    }
    override fun D1.foo() {
        println("D1.foo in C1")
    }
}
TheC().caller(D())   // 输出 "D.foo in C"
C1().caller(D())  // 输出 "D.foo in C1"   可以多态调用
TheC().caller(D1())  // 输出 "D.foo in C"


-------
/*
只包含数据的类，关键字为 data 根据所有声明的属性提取以下函数：
equals() / hashCode()
toString() 
componentN()  按属性声明顺序排列
copy()  
*/

data class User(val name: String, val age: Int)

val jack = UserData(name = "Jack", age = 1)
val olderJack = jack.copy(age = 2)//data 类型的类，copy复制时修改属性
println(jack)
println(olderJack)


val (name, age) = jack //像scala
println("$name, $age years of age")
//标准库提供了 Pair 和 Triple 

-------
/*
sealed 修饰类，密封类可以有子类,所有的子类都必须要内嵌在密封类中
sealed 不能修饰 interface ,abstract class(会报 warning,但是不会出现编译错误)
密封类是枚举类的扩展
*/
sealed class Expr // sealed
data class Const(val number: Double) : Expr()//data 类， ：继承
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr() //object创建单例   ：继承

fun eval(expr: Expr): Double = when (expr) { //sealed子类用在 when 中像 scala
 //when下的所有的sealed项目必须全部增加，否则编译报错
    is Const -> expr.number  //sealed子类用 is
    is Sum -> eval(expr.e1) + eval(expr.e2)//不会递归调用
    NotANumber -> Double.NaN //object子类不用is , NaN
}

var res=eval(Const(3.0));
	println(res);//3.0
	
	var num1:Expr=Const(4.0);
	var num2:Expr=Const(5.0);
	var expr:Expr= Sum(num1,num2);
	println(expr);//显示 Sum(e1=Const(number=4.0), e2=Const(number=5.0))
	
	//println(Sum(Const(4.0),(Const(4.0))));//Sum(e1=Const(number=4.0), e2=Const(number=4.0))
	

sealed class  Operator {
    object Show: Operator()//定义在内部
    object Hide: Operator()
   class Translate(val x: Float,val y: Float): Operator()
} 
fun execute( op: Operator) = when (op) {
    Operator.Show -> println("Show")
    Operator.Hide -> println("Hide")
  is Operator.Translate -> println("Translate x=${op.x},y=${op.y}")  
}
execute(Operator.Show);
execute(Operator.Hide);
execute(Operator.Translate(10f,20f));  

---
//类泛型
class Box<T>(t: T) {
    var value = t
}
val box4 = boxIn<Int>(1)
val box5 = boxIn(1)     // 编译器会进行类型推断

//方法 泛型
fun <T> boxIn(value: T) = Box(value)


//约束上界(upper bound)
fun <T : Comparable<T>> sort(list: List<T>) { 
}
sort(listOf(1, 2, 3)) //Int 是 Comparable<Int> 的子类型


fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
	//多个上界约束条件，可以用 where 子句
    where T : CharSequence,
          T : Comparable<T>
{
    //lamda写法变化
    return list.filter { it > threshold }.map { it.toString() }
}


  
//out 协变,只能用作输出，可以作为返回值类型但是无法作为入参的类型
class GenOut<out A>(val a: A) {
    fun foo(): A {
        return a
    }
}
//in 逆变,只能用作输入，可以作为入参的类型但是无法作为返回值的类型：
class GenIn<in A>(a: A) {
    fun foo(a: A) {
    }
}


class A<T>(val t: T, val t2 : T, val t3 : T)
class Apple(var name : String)

//星号投射 <*>    <Any?> 代指了所有类型
val a1: A<*> = A(12, "String", Apple("苹果"))
val a2: A<Any?> = A(12, "String", Apple("苹果"))   //和a1是一样的
val apple = a1.t3    //参数类型为Any
println(apple)
val apple2 = apple as Apple   //强转成Apple类
println(apple2.name)
//使用数组
val l:ArrayList<*> = arrayListOf("String",1,1.2f,Apple("苹果"))
for (item in l){
    println(item)
}

---

enum class ColorEnum{
    RED,BLACK,BLUE,GREEN,WHITE //默认名称为枚举字符名，值从0开始
}

enum class ColorEnum2(val rgb: Int) { //可带参数
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

var color:ColorEnum=ColorEnum.BLUE
println(ColorEnum.values()) //以数组的形式，返回枚举值
println(ColorEnum.valueOf("RED"))
println(color.name)//像Java
println(color.ordinal)


//inline   reified 
inline fun <reified T : Enum<T>> printAllValues() {
	//用 enumValues<T>() 和 enumValueOf<T>() 函数以泛型的方式访问枚举类中的常量 
    print(enumValues<T>().joinToString { it.name })
}

printAllValues<RGB>()

----

open class A(x: Int) {
    public open val y: Int = x
}
interface B {
}
//象可以继承于某个基类，或者实现其他接口:
val ab: A = object : A(1), B {
    override val y = 15
}
val site = object { //类的定义直接得到一个对象
        var name: String = "李"
        var url: String = "baidu.com"
    }

class C {
    // 私有函数，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }
    // 公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    } 
    fun bar() {
        val x1 = foo().x        // 没问题
        //val x2 = publicFoo().x  // 错误：未能解析的引用“x”
    }
}

class Site {
    var name = "站点"
    object DeskTop{
        var url = "bing.com"
        fun showName(){
           // print{"desk legs $name"} // 错误，不能访问到外部类的方法和变量
        }
    }
} 
 var site = Site()
// site.DeskTop.url // 错误，不能通过外部类的实例访问到该对象
 Site.DeskTop.url // 正确


object DataProviderManager {
	var name:String="init";
    fun registerDataProvider ()  {
		println(name);
    } 
}　
DataProviderManager.registerDataProvider() //object 单例的所有方法都可直接引用
	var data1 = DataProviderManager 
	data1.name = "test"
	println(data1.name);
 
//类内部的对象声明可以用 companion 关键字标记，这样它就与外部类关联在一起，我们就可以直接通过外部类访问到对象的内部元素。
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}
val instance = MyClass.create()   // 访问到对象的内部元素


class MyClass2 {
    companion object { //可以省略掉该对象的对象名，然后使用 Companion 替代需要声明的对象名,关键字 companion 只能使用一次。
　
    }
} 
val x = MyClass2.Companion


interface Factory<T> {
    fun create(): T
}
class MyClass3 {
    companion object : Factory<MyClass3> { //companion object 可以继承
        override fun create(): MyClass3 = MyClass3()
    }
}
-----
interface Base {
    fun print()
}
class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}
// 通过关键字 by 建立委托类
class Derived(b: Base) : Base by b //  : Base by b
fun main(args: Array<String>) {
    val b = BaseImpl(10)
    Derived(b).print() // 输出 10
}


class Example {
    var p: String by Delegate() //属性委托, get,set方法 使用委托的类getValue，setValue方法  
}
import kotlin.reflect.KProperty
class Delegate {
	// getValue() 
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, 这里委托了 ${property.name} 属性"
    }
    //setValue() 
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
    }
}

val e = Example()
println(e.p)     // 访问该属性，调用 getValue() 函数
e.p = "123"   // 调用 setValue() 函数
println(e.p)

val lazyValue: String by lazy {  //延迟属性 Lazy
    println("computed!") //第一次调用 get() 会执行已传递给 lazy() 的 lamda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
    "Hello"
}
println(lazyValue)   
println(lazyValue)


import kotlin.properties.Delegates
class User {
    var name: String by Delegates.observable("初始值") {   //可观察属性 Observable
        prop, old, new ->
        println("属性 ${prop.name} 旧值：$old -> 新值：$new")
    }
}
val user = User()
user.name = "第一次赋值"
user.name = "第二次赋值"



class Site(val map: Map<String, Any?>) {
    val name: String by map  //把属性储存在map中
    val url: String  by map
}

	val site = Site(mapOf(　//实例化一个只读的Map
     "name" to "李四",
     "url"  to "baidu.com"
 )) 
 println(site.name)
 println(site.url)

 var map:MutableMap<String, Any?> = mutableMapOf(//可变的Map
            "name" to "abc",
            "url" to "123"
    )
map.put("name", "Google")
map.put("url", "www.google.com")
    

class Foo {
    var notNullBar: String by Delegates.notNull<String>() //属性 by Delegates.notNull<>
}


fun example(computeFoo: () -> Foo) {
    val memoizedFoo by lazy(computeFoo)//局部变量lazy初始化,lazy(xx） ???? ,by后还可以是一个函数调用 （提供委托） 
}


 