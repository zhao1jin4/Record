目前是apache下的项目，最新版本3.0.0

Idea 使用 tools -> groovy console...

---test.groovy
class MyTest{ //可与文件名不相同
	static main(args) {
		println "hello world in groovy"  //可没有分号，可没有括号 
	}
}
使用命令 groovy test.groovy
也可  groovy -e "println 'hello world in commandline'"


会加载操作系统的用户主目录 ~/.groovy/lib中的所有.jar
eclipse中右击项目->groovy->add groovy library from classpath(对单独建立groovy项目是有效果)
右击.groovy文件->Run as -> Groovy Console 后有界面

class HelloGroovy  //可与文件名不相同
{

    static def repeat(val, repeat=5)
	{
	     for(i in 0..<repeat)
		 {
			 println val
	     }
    }
    static main(args)
     {
        println "hello world in groovy"  //没有分号
        
//        String message = "Hello World"
//        println message
        
        def message = "Hello World"
        //for(i in 0..5){
        for(i in 0..<5){
            println message.class
        }
        
        repeat ("test1213");
        
        def range = 0..4
        println range.class
        assert range instanceof List
        
        def coll = ["Groovy", "Java", "Ruby"]
        assert  coll instanceof Collection
        assert coll instanceof ArrayList
        
		coll.add("Python")
        coll << "Smalltalk"
        coll[5] = "Perl"
        
        assert coll[1] == "Java"
        
//       def numbers = [1,2,3,4]
//        assert numbers + 5 == [1,2,3,4,5]
//        assert numbers - [2,3] == [1,4]
        
		def numbers = [1,2,3,4]
		assert numbers.join(",") == "1,2,3,4"
		numbers << 9 //向集合增加元素
		print numbers.get(2)
		
		assert [1,2,3,4,3].count(3) == 2
		assert ["JAVA", "GROOVY"] == ["Java", "Groovy"]*.toUpperCase()
         
         
        def hash = [name:"Andy", "VPN-#":45]
        assert hash.getClass() == java.util.LinkedHashMap
        hash.put("id", 23)
        assert hash.get("name") == "Andy"
        
        hash.dob = "01/29/76"
        assert hash.dob == "01/29/76"
        assert hash["name"] == "Andy"
        hash["gender"] = "male"
        assert hash.gender == "male"
        assert hash["gender"] == "male"
        
        def acoll = ["Groovy", "Java", "Ruby"]
        for(Iterator iter = acoll.iterator(); iter.hasNext();){
         println iter.next()
        }
        
        acoll.each{
            println it
           }
           
       acoll.each{ value ->
        println value
       }
	   
	   hash.each{ key, value ->
		   println "${key} : ${value}"
		  }
	   
	   def excite = { word ->
		   return "${word}!!"
		  }
	   assert "Groovy!!" == excite("Groovy")
	   assert "Java!!" == excite.call("Java")
	   
	   def sng = new Song(name:"Le Freak", artist:"Chic", genre:"Disco")
	   def sng3 = new Song()
	   sng3.name = "Funkytown"
	   sng3.artist = "Lipps Inc."
	   sng3.setGenre("Disco")
			   
	   assert sng3.getArtist() == "Lipps Inc."
	   
	   sng3.setGenre "Disco"  //可不加()
//	   assert sng3.genre == "Disco"
	   assert sng3.genre == "DISCO"
	   
	   print sng3
	 }//main
}



class Song 
{
	def name
	def artist
	def genre
	String toString(){
		"${name}, ${artist}, ${genre}"
	   }
	def getGenre(){
		genre?.toUpperCase() //?是为空的判断
	   }
}

//闭包
def b1={
	println "hello"
}
def mymethod(Closure closure )//不要导入包
{
	//closure.call()
	closure()
}
mymethod(b1)

//带参数的闭包
def b2={
   String name ,int age  ->
        println "hello ${name} age:${age}"
}
def mymethod2(Closure closure )//不要导入包
{
    closure("lisi",123)
}
mymethod2(b2)

//默认参数it，当没有参数声明时
def b3={ 
    println "hello ${it}"
}
b3("lisi")

//owner,delegate,this 
def b4={  
	println "this=${this}" //this 表示闭包定义所在类
	println "owner=${owner}"//owner 表示闭包定义所在类或对象
	println "delegate=${delegate}"//delegate 可以修改指向，默认值就是owner 
}
b4.call()

//修改delegate
class Student
{
    String name
    def pretty ={ "My Name  is ${name}"}
    String toString()
    {
        pretty.call()
    }
}
class Teacher{
    String name
}
def stu=new Student(name:"lisi")
def tech=new Teacher(name:"jack");
stu.pretty.delegate=tech
stu.pretty.resolveStrategy=Closure.DELEGATE_FIRST //如Teacher找不到name再从Student找
//stu.pretty.resolveStrategy=Closure.DELEGATE_ONLY//如找不到报错
println stu.toString()








.java 代码 
String scriptText = " if(\"1111\".equals(ctx.getTransId())){return true}else{return false}";  
Binding binding = new Binding();  
binding.setVariable("ctx", ctx);//java对象
GroovyShell shell = new GroovyShell(binding);  
Object o = shell.evaluate(scriptText);  
