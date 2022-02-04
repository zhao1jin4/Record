

----------------------------------Gradle
下载地址 https://gradle.org/releases/
https://docs.gradle.org/current/dsl/index.html
http://avatarqing.github.io/Gradle-Plugin-User-Guide-Chinese-Verision/

可使用sdkman来安装
sdk install gradle 6.3

eclipse marketplace 插件 buildship gradle integration 3.1.2(STS 3.9.10自带)
Spring 和 Android使用 ,可以构建 C++


GRADLE_HOME  环境变量
bin 目录入 PATH 环境变量下,初次运行 gradle 命令会在~\.gradle下生成文件

spring 有 gradle 示例
项目中有 src\main\java\包名  (同 maven,web项目手工增加webapp目录 apply plugin: 'war')
项目中有 build.gradle 文件 
		如有 apply plugin: 'java' 或者 
				plugins {
					id 'java'
				}
		
gradle tasks 命令可看到所有可用的 build任务
就可用  gradle build 命令构建 ,会在当前目录下生成 build\libs,build\classes目录 
  
  
---build.gradle  文件

Gradle设置全局仓库 创建文件 ~/.gradle/init.gradle 
 或者命令行加 -I or --init-script 指定初始化脚本
 或GRADLE_HOME/init.d/目录以.gradle结尾的文件　　
    eclipse配置 local installation directory:　(/init.d/xx.gradle) 优先于项目中的 build.gradle
    idea 项目配置 Gradle Home:(/init.d/xx.gradle)    优先于项目中的 build.gradle
   
	allprojects {
		repositories {
			mavenLocal()
			maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
			maven { url 'http://mirrors.163.com/maven/repository/maven-public/' }  //可 https, 好一些,可浏览
			maven { url 'https://maven.aliyun.com/nexus/content/repositories/jcenter'}
			maven { url "$rootDir/../node_modules/react-native/android" }  // react-native 的 android 版本 jar 包maven仓库位置
		}
	}
---上为何没用???  可能因为自己的项目有 allprojects 的配置
/*//不能放在allprojects中
plugins {
    id 'java'
}
*/
apply plugin: 'java' 
//apply plugin: 'war'  //打成war包，可以和 'java'插件一起存在
//apply plugin: 'com.android.application'  //Android 


group 'org.example'
//setting.gradle 文件中有 rootProject.name=xx
version '1.0-SNAPSHOT'
sourceCompatibility = 1.8

mainClassName = 'hello.HelloWorld'  //可以使用  ./gradlew run 来运行

repositories { 
	//增加镜像  放最前，有顺序的
	mavenLocal()
	maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
	maven { url 'http://mirrors.163.com/maven/repository/maven-public/' }
	//maven{ url 'http://maven.aliyun.com/nexus/content/repositories/jcenter'} 
	//mavenCentral()//可点击进入，是一个方法
	//jcenter()  //会从  https://jcenter.bintray.com/com/ 下载 
}
dependencies {
    compile group: 'org.springframework', name: 'spring-core', version: '5.1.2.RELEASE'
    compile("org.webjars:sockjs-client:1.0.2") //第二种写法
	providedCompile group: 'javax.servlet', name: 'javax.servlet-api', version: '4.0.1'
    providedCompile group: 'javax.servlet', name: 'jsp-api', version: '2.0'
	testCompile group: 'junit', name: 'junit', version: '4.12'
	classpath 'com.android.tools.build:gradle:3.2.0' //Adnroid
} 
jar {
    baseName = 'my-project'  //生成jar包的名字为my-project-0.1.0.jar
    version =  '0.1.0'
}
// task后是定义的任务名
task wrapper (type:Wrapper)  // 就可以直接使用 gradle wrapper ,而不用加--gradle-version 5.2
{
	gradleVersion = 5.4 //只可有一个小数点
}

--setting.gradle
rootProject.name = 'myGradleGroovy'

如果想让父模块配置可以所有子模块去用，父模块最外层增加 allprojects {}
	IDEA可以建立子模块项目(右击项目->new -> module) ,setting.gradle文件中自动增加 include 'childTwo'(多个模块就多行)
	自己的模块间相互引用 在dependencies中增加 compile project(":childTwo")
  
会自动下载其它依赖的包,在~/.gradle\caches\modules-2\files-2.1目录下
				%HOMEPATH%\.gradle\caches\modules-2\files-2.1

新建环境变量 GRADLE_USER_HOME=D:/GRADLE_REPO (不能和MAVEN仓库共用位置) gradle目录格式为 org.springframework 是一个目录名
IDEA 对已经有的Maven仓库会优先使用 Maven setting中配置的目录，再使用Gradle目录
IDEA 配置 Service directory path:　会在目录创建caches\modules-2\files-2.1  
IDEA,AndroiStudio 会读系统级别环境变量 GRADLE_USER_HOME 
  
eclipse (不读环境变量)要手工配置gradle user home:　会在目录创建caches\modules-2\files-2.1

IDEA 的gradle视图(同maven)->展开tasks->build->双击jar/war
eclipse 的gradle tasks 视图->展开build->双击jar,在build/libs目录生成
 
分析依赖 
IDEA 的Gradle视图 <项目> -> Task -> help -> dependencies 执行后控制台显示

 
//wrapper
空项目目录下 gradle wrapper --gradle-version 5.4 会生成 gradlew(gradle Wrapper) 可执行文件和gradle/wrapper目录,下有gradle-wrapper.properties文件，包含下载gradle对应版本bin包的URL
就可以执行 ./gradlew build 来构建项目

gradle wrapper --gradle-version 7.2 报 Your project should have a 'settings.gradle(.kts)'

//kotlin
gradle init --type java-application
 Select build script DSL:
   1: groovy
   2: kotlin　如选择这个生成的是 build.gradle.kts , settings.gradle.kts
 后面还会提示选择Junit 还是TestNG
 
 --------------build.gradle.kts
 和　build.gradle　格式不同
 plugins {　
    java
 }
 dependencies {　
    implementation("com.google.guava:guava:27.0.1-jre")
    testImplementation("org.testng:testng:6.14.3")
 }
 
application {
    mainClassName = "org.myproj.App"
}
val test by tasks.getting(Test::class) {
    useTestNG()
}

--no-daemon 可禁用 gradle服务进程， 测试每次使用日志显示starting deamon

https://docs.gradle.org/current/userguide/build_lifecycle.html
三个阶段  Intilization,Configuration,Execution

task ("myTaskName",{
	println "configuration myTaskName" 
	doLast({ //src\core-api\org\gradle\api\Task.java中的doLast方法,
		//不执行并放在执行列表未尾，对应的有doFirst
		println "exeuction myTaskName" 
	})
})

执行使用 gradle myTaskName

任何找不到的方法(如上task)，都会去src\core-api\org\gradle\api\Project.java中找
buildscript调用Project类的buildscript方法，参数是闭包

project.childProjects
project.parent
println("ROOT PROJECT ${this.rootProject.name}")
this.buildDir
this.getProjectDir()//可以是子项目目录
this.getRootDir()


for(int i=0;i<5;i++)//灵活性
{	//缩写方式
	task ("myTaskName"+i)
	{
		def inner=i;
		println "configuration myTaskName ${inner}" 
		doLast {  
			println "exeuction myTaskName ${inner}" 
		} 
	}
}
//缩写方式
// 10.times  //或者
//(1..10).each
(1..<10).each //不包含10
{	i ->
	task ("myTaskName"+i)
	{
		def inner=i;
		println "configuration myTaskName ${inner}" 
		doLast {  
			println "exeuction myTaskName ${inner}" 
		} 
	}
}


afterEvaluate({ //在所有的配置(configuration阶段就是build.gralde执行)完成后执行，对应的有beforeEvaluate
    println "my afterEvaluate"
})

gradle.buildFinished { //所有生命周期执行完
 println(" my buildFinished")
}
gradle.beforeProject {//对每个子模块
    println(" my beforeProject")
}
gradle.afterProject {
    println(" my afterProject")
}


task ("myFirst")
{ 
		println "configuration myFirst " 
		doLast {  
			println "exeuction myFirst " 
		} 
} 
//Task类的dependsOn("taskName") 任务依赖
task ("mySecond")
{ 
	dependsOn("myFirst")
	println "configuration mySecond " 
	doLast {  
		println "exeuction mySecond " 
	} 
}

//自定义插件类可放在build.gradle文件中,用于重复利用
//groovy
 class MyPlugin implements Plugin<Project> {
     @Override
     void apply(Project project) {
        //实现,task要修改为project.task
         project.task ("myPluginTask")//建立Task
		 {
			 println "configuration myPluginTask "
			 doLast {
				 println "exeuction myPluginTask "
			 }
		 }
     }
 }
 
//也可放在buildSrc/src/main/java/ 目录中，buildSrc目录可被build.gradle找到
//java
package org.xx;
 class MyPlugin implements Plugin<Project> {
    public void apply(Project project) {  //实现
		//task要修改为project.task
        project.task ("myPluginTask", (task1)->{  //建立Task
            System.out.println ( "configuration myPluginTask ");
            task1.doLast((task2)->{
                    System.out.println ( "exeuction myPluginTask ");
            });

        });
		if(project.getPlugins().hasPlugin("java"))
        {
            System.out.println ("In MyPlugin query has java plugin");
        }
		project.getExtensions().create("myExtension", MyExtension.class); 
  
//      StringUtils.isNotBlank("");//这里如何引用第三方库???? buildSrc/build.gradle没用???
  }
}
class MyExtension
{
    String department; 
    public MyExtension() {
    } 
    public String getDepartment() {
        return department;
    } 
    public void setDepartment(String department) {
        this.department = department;
    }
}

apply plugin: MyPlugin //使用插件,apply方法在ApplyAware类中(Project有继承)
//等同于 apply([plugin:MyPlugin])//如参数只一个map可去[]

或者  buildSrc\src\main\resources\META-INF\gradle-plugins\org.xx.properties 写入 implementation-class=org.xx.MyPlugin
再 apply plugin: 'org.xx'


myExtension{
    department = 'IT' //可用=或空格分隔
}
 
执行使用 gradle myPluginTask


public class MyDefaultTask extends DefaultTask {  
    private String mygroup;
    public MyDefaultTask() {
         setGroup("org.xx");
    }
    @TaskAction //在执行阶段，类似的doFirst,doLast运行在里
    public void doAction()
    {
        mydoLast();
    }
     public void mydoLast()
    {
        MyExtension myPluginTask=(MyExtension) getProject().getExtensions().getByName("myExtension");
        System.out.println("in MyDefaultTask get department is "+myPluginTask.getDepartment());
    }
}
task myDefTask(type: MyDefaultTask) {//可能要import

}
执行使用 gradle myDefTask



//如在build.gradle文件中使用第三方库(或者使用插件)要的buildscript中加
buildscript {
    repositories {
        mavenLocal()
        //mavenCentral()
        #maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }#老的
		#maven { url 'https://maven.aliyun.com/repository/public'} #也会使用http
		maven { allowInsecureProtocol = true
				url 'https://maven.aliyun.com/repository/public' }
    } 
    dependencies {
		//由compile变为classpath
        classpath group: 'org.apache.commons', name: 'commons-lang3', version: '3.8.1'
    }
}
//if(org.apache.commons.lang3.StringUtils.isNoneBlank(""))或者
import org.apache.commons.lang3.StringUtils
if(StringUtils.isNoneBlank(""))
{

}

//全写方式,简写原因是delegate修改成了参数
buildscript //看源码文档参数是 ScriptHandler
{
    ScriptHandler scriptHandler ->
        scriptHandler.repositories { //看源码文档参数是 RepositoryHandler
            RepositoryHandler repositories ->
            repositories.mavenLocal()
            repositories.maven {
				name 'dev'
                url 'http://maven.aliyun.com/nexus/content/groups/public/'
				//私有仓库用户名密码
                credentials {
                      username = 'user1'
                      password = '123'
                 }
            }
            repositories.jcenter()
        } 
}


对于apply plugin: 'org.xxx' 是找buildscript的classpath(gradle-api-xxx.jar或者buildSrc/src/main/resources目录)中META-INF/gradle-plugins/org.xxx.properties文件中
有implementation-class=XXX实现类

在Android中 apply plugin: 'com.android.application'  就在buildscript依赖的 classpath 'com.android.tools.build:gradle:3.5.3'
	有implementation-class=com.android.build.gradle.AppPlugin


this.getAllprojects().eachWithIndex { Project project, int i ->
    if(i==0){
        println "===root project ${project.name}"
    }else
    {
        println "-project ${project.name}"
    }
}

project("app") {//参数为项目名
    Project project->
        //为指定模块做配置
        apply plugin: 'java'
        println "module project name=${project.name}"
}

allprojects
{ 
    group 'org.example'
}
println project("app").group  //来测试

subprojects {//不包括父项目
    Project project ->
    if(project.plugins.hasPlugin("com.android.library"))
    {
        //apply from: '../myCommonExt.gradle' //引用配置
		apply from: file('myCommonExt.gradle') //当前目录找
    }
}

ext{//定义扩展属性 
     minSdkVersion=27
}
println this.minSdkVersion //可以直接使用ext定义的变量
println project("app").minSDKVersion //子项目中也继承父项目的ext属性，相当于放在subprojects中

//建立gradle.properties文件写入 isLoadTest=false ,所有项目都可直接引用，注意不能和已有的名字重复
//可以写入settings.gradle中(源码类为Settings类),是在初始化阶段中执行
if(hasProperty('isLoadTest')?isLoadTest.toBoolean():false)
    println "has LOADTEST"
	//include "xx"
else
    println "NO LOADTEST"

	
copy{
    from file('my.jks')
    into getRootProject().getBuildDir()
	//exclude()
	//rename {}
}

 fileTree('build/classes'){ 
    FileTree fileTree->
         fileTree.visit {
            FileTreeElement element->
            println "filename=${element.file.absolutePath}"
         }
 }

排除 jar包
compile('org.apache.commons:commons-lang3:3.8.1'){//写法不能是group:''
		exclude group:'junit' ,module:'junit' 
	}
 
调用系统命令
task mycmd {
    doLast
    {
        exec{
            try{
               //executable 'bash'
                //args '-c' 'ls -l /tmp'
                
				//windows命令测试成功
                executable 'cmd'
                args '/c','dir d:'
            }catch(GradleException e)
            {
                println e.getMessage()
            }
        }
    }
}
//tasks.findByName
//tasks.getByName
tasks.create(name:'myTask'){ //tasks是TaskContainer类型
	setGroup('myGroup')//显示在Idea工具的Gradle视图中，按文件夹分组，默认是other中
    setDescription("myDescription")
    println 'myTask'
} 

task myTask2(group:'myGroup2',description:'desc 2'){
    println 'myTask2 '
    doLast {
        println 'myTask2  doLast 1'
    }
}
myTask2.doLast {//doLast可放外面,先执行里面，再执行这，如是doFirst就是相反的，按队列顺序
    println 'myTask2  doLast 2'
}

//输入输出关联Task执行顺序，链式
//TaskInputs 类可接收properties和文件， TaskOutputs 类可输出文件
ext{
    destFile=file("config.properties")
    if(destFile!=null && !destFile.exists())
    {
        destFile.createNewFile()
    }
}
task ("myGenFileOne")
{
    inputs.property("db.username",'root')
    inputs.property("db.password",'123')
    outputs.file this.destFile
    println "configuration myGenFileOne "
    doLast {
        def props=inputs.getProperties()
        def files=outputs.getFiles()
        files.each {
            File file->

                file.withWriter {
                    Writer writer->
                        println "append ${props.toString()} "
                        writer.append(props.toString())
                }
        }
        println "execution myGenFileOne "
    }
}
task ("myReadFileTwo") //没有依赖第一个？？？
{
    inputs.file destFile
    println "configuration myReadFileTwo "
    doLast
    {
        println "execution myReadFileTwo "
    }
}
gradle myReadFileTwo myGenFileOne  没效果 ???

//顺序用mustRunAfter
task ("myTaskA"){
    println 'config myTaskA'
    doLast {
        println 'exec myTaskA'
    }
}

task ("myTaskB"){
    mustRunAfter "myTaskA"
    println 'config myTaskB'
    doLast {
        println 'exec myTaskB'
    }
}
gradle myTaskB myTaskA 放一起执行才有效果

 
SourceSet , JavaSourceSet,AndroidSourceSet(可以修改resources,java,res,manifest,assets等目录)
//源码示例
sourceSets {
    main {
        java {
            exclude 'some/unwanted/package/**'
        }
    }
}
/*
//android 输出so文件默认输出目录为jni/libs 修改为libs
android.sourceSets {
 main{
     jniLibs.srcDirs=['libs']
 }
}
//android测试不行？
android.sourceSets {
    main{
        res.srcDirs=['src/main/res','src/main/res-icon']
    }
}
*/
android的配置android{ defaultConfig{}} 对应源码BaseExtension类(所有配置属性) defaultConfig 属性
	源码BaseVariant类是所有Task, 子类 ApplicationVariant



---gradle 发布jar到nexus仓库，包括源码  
https://docs.gradle.org/current/userguide/publishing_maven.html

 
apply plugin: 'maven-publish'//idea工具Gradle视图会多出publishing组
//以下两个可不加
//apply plugin: 'java-library'
//apply plugin: 'signing'


task sourcesJar(type: Jar) { 
    from sourceSets.main.allJava
    classifier = 'sources'
}
//执行 gradel sourcesJar生成<project>/build/libs/<project>-<version>.jar

task javadocJar(type: Jar) { //javadoc 生成后中文乱码???
    from javadoc
    classifier = 'javadoc'
}

publishing {
	  publications {
            maven(MavenPublication) {
            	 // groupId = project.group
	            // artifactId = project.name
	            // version = project.version
	            //如果不定义，则会按照以上默认值执行
                groupId = 'org.example'
                artifactId = 'my-example'
                version = '1.0-SNAPSHOT'

                from components.java
                artifact sourcesJar //要在前面定义
            	 artifact javadocJar
            }
     }
    repositories {
        maven { 
            url = "http://127.0.0.1:8081/repository/maven-snapshots/" //maven-snapshots的地址,要求版本以-SNAPSHOT结尾
            credentials {
                username = 'admin'
                password = 'admin123'
           }
        }
    }
}
idea Gradle视图 publishToMavenLocal 到本地maven仓库 测试成功
idea Gradle视图 publishMavenPublicationToMavenLocal 到远程maven仓库nexus 测试成功
  

---gradle 依赖统一版本，不是使用变量		
---gradle 各种环境配置文件


----
gradle installDist 生成 build/install/<projectName>
gradle distZip 生成build/distributions/<projectName>.zip包
gradle distTar 生成build/distributions/<projectName>.tar包
gradle assemble 生成build/distributions/<projectName>.tar包 和 .zip包



