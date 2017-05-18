
==============================Dom4j
---------Dom4j 写
Document document = DocumentHelper.createDocument();
Element root = document.addElement("root");
Element status=root.addElement("status");
status.addText("OK");

model.addAttribute("id","1")
	.addAttribute("name", "lisi");
System.out.println( document.asXML());//有<?xml 
System.out.println(root.asXML());//无<?xml  
root.write(response.getWriter());//是一行的,Servlet中
//以上　　没有<?xml头
 
  
FileWriter fileWriter = new FileWriter("c:/temp/hello.xml");  
//OutputFormat xmlFormat = new OutputFormat();  
OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
xmlFormat.setEncoding("GBK");  
xmlFormat.setIndent(true);//"或者是几个空格"
XMLWriter xmlWriter = new XMLWriter(fileWriter,xmlFormat);//是格式好的,response.getWriter()
xmlWriter.write(document);//有<?xml
//xmlWriter.write( root );//无<?xml
xmlWriter.close(); 

---------Dom4j SAX读
Document document = DocumentHelper.parseText(xmlSoapResponse.toString());
Element soapenvNode = document.getRootElement().element("Body").element("respone1");//dom4j会保留名称空间,w3c的不会

FileReader in= new FileReader( "c:/temp/output.xml" );//或者使用StringReader
SAXReader reader=new SAXReader();
Document doc=reader.read(in);//无<?xml,有<?xml 都可解析
java.util.List list=root.selectNodes("//author[@name='James']");//要org/jaxen/

Element root=doc.getRootElement();
System.out.println("根节点名："+root.getName());
//root.elementByID("")
List<Element> authors=root.elements();
//可 root.element("子一级").element("子二级")
for(Iterator<Element> it= authors.iterator();it.hasNext();)
{
	Element author= it.next();
	Attribute attr=author.attribute("name");
	System.out.println("属性："+attr.getValue());
	System.out.println("文本："+author.getText());
	
}
in.close();
//-------dom4j DOM读
DOMReader domReader=new DOMReader();
org.dom4j.Document doc4j=domReader.read(doc);//org.w3c.dom.Document -> org.dom4j.Document
//doc4j.accept(new MyVisitor());//方式一
Element root=doc4j.getRootElement();//方式二
java.util.List list=root.selectNodes("//author[@name='James']");//要org/jaxen/
for(int i=0;i<list.size();i++)
{
	System.out.println(((Element)list.get(i)).getData());
}
class MyVisitor extends VisitorSupport
{
	public void visit(Attribute node) {
		System.out.println("属性："+node.getName()+"="+node.getValue());
	}
	public void visit(Element node) {
		if(node.isTextOnly())
			System.out.println("元素："+node.getName()+" > "+node.getText());
		else
			System.out.println("元素："+node.getName());
	}
	public void visit(ProcessingInstruction node) {//指令,如引用XSL
		System.out.println("头："+node.getTarget());
	}
}
==============================xerces
xercesImpl.jar/META-INF/services/javax.xml.parsers.DocumentBuilderFactory文件中记录着DocumentBuilderFactory实现类

---------xerces 读    //不能忽略缩进空白??????????
org.apache.xerces.parsers.DOMParser  parser = new DOMParser();//xerces 
InputStream input=TestXerces.class.getResourceAsStream("/testxml/rule.xml");
InputSource source=new InputSource(input);
parser.parse(source);//parser.parse("employees.xml");
org.w3c.dom.Document doc1=parser.getDocument();
  
---------xerces 写  
//输出格式良好,过时的的,推荐用DOM Level 3 LSSerializer 或者 JAXP's Transformation 
OutputFormat   outputFormat   =   new   OutputFormat("XML","gb2312",true);  
FileWriter   fileWriter=new   FileWriter(new File("test.xml"));  
XMLSerializer   xmlSerializer=new   XMLSerializer(fileWriter,outputFormat);  
xmlSerializer.asDOMSerializer();  
xmlSerializer.serialize(result.getDocumentElement());  
fileWriter.close();   

org.w3c.dom.Document myDoc=new org.apache.xerces.dom.DocumentImpl();//建立

---------xalan
TransformerFactory   tFactory=TransformerFactory.newInstance();
System.getProperty("javax.xml.transform.TransformerFactory");//org.apache.xalan.processor.TransformerFactoryImpl
//xx.jar/META-INF/services/javax.xml.transform.TransformerFactory文件
java org.apache.xalan.xslt.Process -IN student.xml -XSL student.xsl -OUT student.html

==============================LDAP 相关知识
Entry:
	添加一个Entry时，该Entry必须属于一个或多个objectclass ,每一个objectclass 规定了该Entry中必须要包含的属性，以及允许使用的属性。Entry所属的类型由属性objectclass规定
	
	DN(distinguished name) 用于唯一的标志Entry在directory中的位置
		根在最后面如ou=People,dc=example,dc=com,类似Unix的 /dc=com/dc=example/ou=People
	RDN(相对的) 对应的就是 ou=People
	root Entry只是一个特殊的Entry，它包含了目录服务器的配置信息，通常情况下，并不用来存储信息
	任何一个节点都可以包含信息，同时也可以是一个容器
Attribute: 	
	Entry中的属性值的特殊字符，必须进行转义
		# 转义为 \#
		， 转义为 \，
		+ 转义为 \+
		“ 转义为 \”
		\ 转义为 \\
		<  转义为 \<
		>  转义为  \>
		;  转义为  \;

建别名Entry，该Entry的object class必须是alias。而且其属性aliasedObjectName的值必须是该Entry所指向的Entry的DN
被引用的Entry被删除的话，该Entry就会指向一个错误的结果,可以使用LDAP URL或referral代替Alias Entry

LDAP 的探索选项
	a.base object
	b.search scope (Sub包含顶节点在内的一棵子树,Base 只搜索包含一个节点,Onelevel 搜索表示的节点下的所有直接子节点)
	g.filter参数,如*是通配符,也可用>,<=,!,如(age>21)或者(!(age<=21)), (|(sn=smith*)(age>21)), 匹配属性sn的值以smith开始,特殊字符转义
		属性值的特殊字符，必须进行转义
		*	转义为	\2A
		(	转义为	\28
		)	转义为	\29
		\	转义为	\5C
		NULL转义为	\00
	h.	return attributes 空表示返回所有属性。
		
		
LDAPV3 实现了SASL安全框架,一些用户只能查看特定的Entry，而不能修改	
LDIF  文本文件，用来描述目录数据,可导入导出,即使这两个目录服务器内部使用的是不同的数据库格式
	第一种用来描述Directory目录数据的
	第二种包含更新语句，用于更新现有的Directory条目数据
	文件内容 以dn:为一个单独部分,
1. 增加Entry
	dn:xxxxx
	changetype: add
	yyyyyy
2. 删除Entry
	dn:xxxxx
	changetype: delete
3. 修改Entry
	1)添加属性
		dn:xxx
		changetype: modify 
		add: mail		//mail属性
		mail: abc@xyz.com
	2)删除属性
		dn:xxx
		changetype: modify 
		delete: mail
	3)修改属性
		dn:xxx
		changetype: modify 
		replace: mail
		mail:  one@two.com
		mail:  one123@two.com
	多条更新属性语句之间用“-”分开,就用每次加dn:xxx了

4. Entry重命名或移动
	dn: xxxx
　　changetype: moddn　
	[newsuperior:如果要移动一条Entry则该项表示一个新的节点的DN]
　　[deleteoldrdn: ( 0 | 1 )该项表示是否要删除修改以前的RDN　０不删除]
	[newrdn:Entry的新RDN]


schema 定义数据存储结构,每个字段都有一个类型，以及一些约束条件
LDAP目录服务器的用户而言，一般不需要自己定义 schema

Attribute types 属性名称
	c	两位国家代码 如：中国:CN,美国:US
	co	国家的全名
	cn	通用名称
	dc	域名组件
	sn	姓，别名
	gn	gavenName
	o	组织名称
	ou	部门名称
	...
Attribute syntaxes  所存储的信息如何组织

object classes 每个Entry都必须至少属于一个object class。规定了该Entry可以存储那些属性
	|ObjectClass		| 必须属性	|
	|-------------------|-----------|
	|top				|  			| 所有类的基类
	|dcObject			|  			|
	|organizationalPerson|  		| 继承person,增加进也会自动所基类persion 加入
	|organizationalUnit|  			|
	|ads-jbpmPartition|  			|继承ads-partition
   inetOrgPerson 继承自organizationalPerson 继承自 person继承自top
 
==============================LADP Server Apache Directory Server
openLDAP没有windows的
//-apache的LDAP服务器项目ApacheDS,纯Java实现,可嵌入到你的应用程序,可与Spring一起使用
 ------apacheDS-2.0.0-M7 解压版本	没有server.xml
	
	\instances\default\conf\config.ldif ,这个文件修改后要导入才行
	dn: ads-transportid=ldap,    下面显示对应的配置端口
	ads-systemport: 10389
	
	apacheDS-2.0 和 apacheDS-1.5 自带的用户
	Bind DN or user:uid=admin,ou=system
	Bind password:secret

//--Directory Studio使用	
修改密码使用
在ou=system/uid=admin级下 ,显示objectClass=person(structual),userPassword中修改密码

右击Root DSE->new Context Entry->选择create entry from scratch->在obejctClass中选择dcObject,organization->下拉选择已存在的dc=example,dc=com->输入o属性值->finish

file->import-> LDIF into LDAP  , 或者右击xxx->import 


---unknow
	admin密码
	ads-chgPwdServicePrincipal: kadmin/changepw@EXAMPLE.COM
	
	ads-partitionSuffix: dc=example,dc=com
---unknow	
		
		
javax.naming.directory 
javax.naming.ldap

-----apacheds-1.5 的配置 
Apache Directory Server\instances\default\conf\server.xml
 <transports>
      <tcpTransport   改端口 默认是10389 (unencrypted or StartTLS) and 10636 (SSL).

一个Partition有一个Entry Tree有多个Entries
多个Partition之间无影响,名字为partition suffix

默认分区的实现是基于 JDBM 项目的 B+Trees	功能

server.xml 中的<partitions>下加入 ,可以把  <systemPartition>下的复制,修改id,suffix
<jdbmPartition id="example" cacheSize="100" suffix="dc=example,dc=com" optimizerEnabled="true"
                 syncOnWrite="true">
    <indexedAttributes>
    ...
    </indexedAttributes>
</jdbmPartition>

windows 服务的启动脚本
apacheds.exe -s conf\apacheds.conf "set.INSTANCE_HOME=C:\Program Files\Apache Directory Server\instances" set.INSTANCE=default "set.APACHEDS_HOME=C:\Program Files\Apache Directory Server"

Studio 可以右击DIT->import/export LDIF

在 <apacheDS id="apacheDS">下加入
	 <ldifDirectory>sevenSeasRoot.ldif</ldifDirectory> 启动服务自动导入指定文件 



程序增加分区
apacheds-jdbm-partition-1.5.7.jar
apacheds-jdbm-store-1.5.7.jar
apacheds-xdbm-base-1.5.7.jar
apacheds-core-api-1.5.7.jar
shared-ldap-0.9.19.jar


禁用匿名用户仿问
  <defaultDirectoryService 
        中修改   allowAnonymousAccess="true"  为false
						
==============================SNMP4J
	服务端使用Net-SNMP
----------------------------------Jetty

eclipse 插件 run-jetty-run  可以在eclipse中使用 jetty 做servlet容器

java -jar start.jar 启动服务器　　
http://localhost:8080/  
.war包 放到webapps目录下

RunJettyRun-1.8 插件 当eclipe认为是web项目(有建立Servlet的界面)才可run as ->jetty


----------- Maven服务器 Nexus OSS 
有跨平台的 -bundle.zip解压 

nexus-2.7.2-03\conf\nexus.properties 中有配置项目nexus-work 是 sonatype-work 目录,就是解压目录的 , 仓库存放位置 
nexus-2.7.2-03\bin\jsw\windows-x86-64\ 以管理员运行 install-nexus.bat ,再 start-nexus.bat 
nexus-2.14.1-01\bin\nexus.bat  会提示install (要以管理员运行,会在servces.msc中出现) ,start 

http://localhost:8081/nexus  默认有一个用户 admin  密码 admin123  
[Views/Repositories]->[Repositories]->选中仓库URL->[Configuration]-> 修改 Download Remote Indexes 设为 true


hosted 本地资源库 ,部署自己的构件  , 有上传.jar包功能 
proxy 代理资源仓库,它代理远程的公共资源仓库,如maven中央资源仓库
group 资源仓库组,用来合并多个hosted/proxy资源仓库,配置maven依赖资源仓库组

 可以新建仓库，建立用户指定角色，角色指定权限

 可以设置是否可以 deployment,是release的还是snapshot的
 
----------------------------------Maven

设置PATH环境变量  

mvn -version
mvn -e		full stack trace of the errors

如单元测试报错, 控制台没有原因,要进入target/surefire-report/中的txt文件 有错误 堆栈信息


http://search.maven.org/ 可以搜索 Maven 依赖包的配置 Version 例中

Maven的安装文件自带了中央仓库的配置, 打开jar文件$M2_HOME/lib/maven-model-builder-3.3.9.jar/org/apache/maven/model/pom-4.0.0.xml 有配置 
<repositories>  
    <repository>  
        <id>central</id>  
        <name>Maven Repository Switchboard</name>  
        <url>http://repo1.maven.org/maven2</url>  
        <layout>default</layout>  
        <snapshots>  
            <enabled>false</enabled>  
        </snapshots>  
    </repository>  
</repositories>  
 
----settings.xml
默认读配置文件 ${user.home}/.m2/settings.xml (用户级,和安装目录的做合并,相同项用户级高),可从安装目录复制
默认仓库是位于 ${user.home}/.m2/repository/ 即 C:\Users\zhaojin.li\.m2  MAVEN_REPO 的变量来修改
可以修改配置<localRepository>/path/to/local/repo/</localRepository>
设置 proxy ,但没说什么协议,如没有办法设置 http://主机:端口/文件  形式的代理 , [文件]的部分没办法给,)

<server>
  <id>my_libs_snapshot</id>  <!-- id对应 pom.xml中的 <distributionManagement> 中的Id的值   -->
  <username>xx</username>
  <password>yy</password>
</server>

<mirrors>
	<mirror>
		<id>mirrorId</id>  
		<mirrorOf>*</mirrorOf>  
		<!-- 可为 *   或者 env_development,central (https://repo.maven.apache.org/maven2/) 
		表示如果仿问 central 也要通过个url仿问 -->
		<url>http://192.168.0.141:8081/nexus/content/groups/public</url>    <!-- 应该是内网地址 -->
	</mirror>
</mirrors>

<profiles>
	<profile> 
	  <id>env_development</id>	<!-- 对应下面的 activeProfile -->
	  <repositories>
		<repository>
		  <snapshots>   <!-- snapshots -->
			<enabled>false</enabled>
		  </snapshots>
		  <id>my_libs_release</id>
		  <name>My Library Releases</name>
		  <url>${release_deployment_url}</url>   <!-- 是下面的 properties中的标签名,是自己项目的存放位置-->
		</repository>
		<repository>
		  <releases>	<!-- releases -->
			<enabled>false</enabled>
		  </releases>
		  <id>my_libs_snapshot</id>
		  <name>My Library Snapshots</name>
		  <url>${snapshot_deployment_url}</url>
		</repository>
	  </repositories>
	  <properties>
		<!-- <url>http://repo1.maven.org/maven2/</url>  这里是配置公用的第三方包 http://search.maven.org/#browse
					http://repo.spring.io/libs-release/
			-->
		<release_deployment_url>http://localhost:8080/my/repo</release_deployment_url>
		<snapshot_deployment_url>http://localhost:8080/my_snapshot/repo</snapshot_deployment_url>
	  </properties>
	</profile>
</profiles>

<activeProfiles>
	<activeProfile>env_development</activeProfile>   <!-- 对应上面 ,测试如不开mirror没有效果???? -->
</activeProfiles>

---------setting.xml示例    
	<servers>
		<!--  id对应 pom.xml中的 <distributionManagement> 中的Id的值  ( 官方说也有<repository> (测试pom.xml不能少配置)或者 <mirror>   )
		<server>
			<id>local_net_repo</id>  
			<username>hrbb</username>
			<password>pass123</password>
		</server> 
		--> 
	</servers> 
    
	<mirrors>
		<mirror>
			<id>nexusMirror</id>
			<mirrorOf>central</mirrorOf> <!-- central 或者 * -->
			<url>http://10.1.5.228:8081/nexus/content/groups/public</url>
		</mirror>
	</mirrors>

	<profiles>
		<profile>
			<id>centerProfile</id>
			<repositories>
				<repository>
					<id>public_net_repo</id>
					<url>http://repo1.maven.org/maven2/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
			</repositories>
		 </profile>
		 
		<profile>
			<id>nexusProfile</id>
			<repositories>
		 		<repository>
					<id>local_net_repo</id>
					<url>http://10.1.5.228:8081/nexus/content/groups/public</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
			</repositories>
		</profile>
		
	</profiles>
 
	 
	<activeProfiles>
		<activeProfile>nexusProfile</activeProfile>
	</activeProfiles>
	

----pom.xml 

Maven内置隐式变量 
${basedir} 项目根目录
${project.xxx} 当前pom文件的任意节点的内容 


依赖关系 
A-> L 1.0
B-> L 2.0
C->A,B 那么C最终依赖L 1.0 根据书写顺序A写在前面
如C 也依赖于 L 3.0 ,那么最终依赖L 3.0,依赖找路径最近的  ,应该使用dependenceMangement


groupId 是包名
artifactId 是自己的项目名

<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <!-- 对多个项目的分类,顶级是 <packaging>pom</packaging>
	<parent>
		<groupId>com.mycompany.myproject</groupId>
		<artifactId>myproject</artifactId>
		<version>1.0.0-SNAPSHOT</version>
		<relativePath>../pom.xml</relativePath>
	</parent>
  -->
  <name>MyProjectApp</name> <!-- 一些工具会显示为项目名,如NetBeans -->
  <groupId>com.mycompany.myproject.app</groupId>
  <artifactId>myproject_app</artifactId>  
  <version>1.1.0-SNAPSHOT</version> 	<!-- 如有父版本,这里不用指定 -->
  <packaging>jar</packaging>  <!-- jar,war,pom -->
  <!-- 对  <packaging>pom</packaging> 时module是项目名,如deploy facade时,应把parent级 pom中其它的module注释，在parent级做deploy
  <modules>
	    <module>myproject-facade</module>
  </modules>
  --> 
  <build> 
	<finalName>${project.artifactId}-${project.version}</finalName>
	<resources>
		<resource>
			<directory>src/main/resources</directory>
			<excludes>
				<exclude>dubbo.properties</exclude>
			</excludes>
		</resource>
	</resources> <!-- 或者使用下面的war插件 -->
	<plugins>
		 <plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-war-plugin</artifactId>
			<version>2.4</version>
			 <configuration>
				<packagingExcludes>**/dubbo.properties</packagingExcludes>
			</configuration>
		</plugin>
		
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-compiler-plugin</artifactId>
			<version>3.6.0</version>
			<configuration>
			  <verbose>true</verbose>
			  <fork>true</fork>
			  <executable>${JAVA_HOME}/bin/javac</executable>
			  <compilerVersion>1.8</compilerVersion>
			</configuration>
		  </plugin>
		  
		<!-- 如果为.jar 指定 Main-Class: com.   ,如使用了spring,运行的机器又不能上网要加AppendingTransformer  -->
		<plugin> 
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-shade-plugin</artifactId>
			<version>2.3</version>
			<executions>
				<execution>
					<phase>package</phase>  <!-- 在哪个生命周期之后执行 -->
					<goals>
						<goal>shade</goal>  <!-- 目标名,插件提供的,看插件文档 -->
					</goals>
					<configuration> <!-- 里的所有配置都是对应插件的属性名,@parameter -->
						<transformers>
							<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
								<mainClass>com.MainApp</mainClass>
							</transformer>
							<transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
								<resource>META-INF/spring.handlers</resource>
							</transformer>
							<transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
								<resource>META-INF/spring.schemas</resource>
							</transformer>
						</transformers>
					</configuration>
				</execution>
			</executions>
		</plugin>
 
		
		<plugin> <!-- 打包源码插件,生成-sources.jar  也可单独运行 mvn source:jar 但clean后就没了 -->
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-source-plugin</artifactId>
			<version>2.2.1</version>
			<executions>
				<execution>
					<id>attach-sources</id>
					<goals>
						<goal>jar</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<version>2.17</version>
			<configuration>
			  <includes>
				<include>test/coverage/RunAllCoverageTest.java</include> <!-- 指定入口类 @SuiteClasses -->
			  </includes>
			  <configuration>
				<skip>true</skip>  <!-- 配置这个 相当于-DskipTests=true -->
			  </configuration>
			</configuration>
      </plugin>
	</plugins>
  </build>
  <distributionManagement> <!-- 为mvn deploy时用使用id做对应  -->
		<repository>
			<id>releases</id>
			<url>http://10.1.5.228:8081/nexus/content/repositories/releases</url>
		</repository>
		<snapshotRepository>
			<id>snapshots</id>
			<url>http://10.1.5.228:8081/nexus/content/repositories/snapshots</url>
		</snapshotRepository>
	</distributionManagement>  

	<properties>  <!-- org.apache.maven.plugins  使用的-->
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> 
	</properties> 
	<dependencyManagement>
	  <dependencies>
		<dependency>
		  <groupId>junit</groupId>
		  <artifactId>junit</artifactId>
		  <version>4.12</version> <!--  可以不指定版本 在 <dependencyManagement> <dependencies> 中管理版本 -->
		  <scope>test</scope> <!-- 只my_app/src/test/java中类可访问到,  有 compile -->
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>servlet-api</artifactId>
			<version>2.5</version>
			<scope>provided</scope> <!-- 不会参与打包 -->
		</dependency> 
	  </dependencies>
	 </dependencyManagement>
	<dependencies>
		<dependency>
		  <groupId>junit</groupId>
		  <artifactId>junit</artifactId> <!-- 这里只配置两个,其它就可以使用统一的配置如version,scope -->
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>servlet-api</artifactId>
		 </dependency> 
	</dependencies>
  <!-- 防止报  Missing artifact jdk.tools:jdk.tools:jar:1.6      -->
	 <dependency>  
		<groupId>jdk.tools</groupId>
		<artifactId>jdk.tools</artifactId>
		<version>1.8</version>
		<scope>system</scope>
		<systemPath>${JAVA_HOME}/lib/tools.jar</systemPath>
	</dependency>
</project>

在test/java可以访问到依赖jar包中 my_app/src/main/java , my_app/src/main/resources

----
my_app/pom.xml
my_app/src/main/java
my_app/src/main/resources
my_app/src/test/java
my_app/src/test/resources
my-webapp/src/main/webapp/WEB-INF/

使用帮助 , 通常还是上网查方便
mvn help:describe -DgroupId=org.apache.maven.plugins -DartifactId=maven-surefire-plugin  -Ddetail

<scope> 如为 runtime 运行时依赖,如jdbc-driver，有provide,test,

生命周期 ,对应插件里的<executions> <execution>  	<phase>package</phase>  的配置
eclipse 中 全选所有项目->maven->update projext ... 会更新pom.xml中新的版本配置,下载依赖包(可关闭项目再打开)

在pom.xml文件所在的目录,执行mvn命令
  
mvn clean 删target目录
mvn compile  编译 			
mvn test 编译test目录并运行,
mvn package  打包,会先做 compile,对应pom.xml中的<packaging>jar</packaging> ,my-app/target  下生成.jar
mvn deploy -Dmaven.test.skip=true  会先做 package,把新包部署到远程Maven仓库 ,pom.xml要配置 <distributionManagement> 密码配置在setting.xml
	
mvn install  也可 eclipse 中 run as ->maven install 会打新包到本地仓库
mvn clean compile   可以几个目标一起执行
mvn package -e 查看错误信息
mvn clean package -Dmaven.test.skip=true    跳过编译测试类,生成.war包中的/lib/没有重复的.jar
mvn install -DskipTests     跳过test的执行，但要编译  
 

jenkins ->配置->构建后操作步骤->Sonar,Publish Coberutra Coverage Report
mvn clean package cobertura:cobertura   单独生成测试覆盖率报告   target/site/cobertura/index.html ,适用于每个项目的test 目录,只测试这个项目的main中的类
				  sonar:sonar 测试覆盖率报告(要单独的服务器)

eclipse插件 m2eclipse中右击项目->maven->update project defination会设置eclipse项目引用maven下载的.jar包
eclipse的preferences->maven->installation 要配置最新的Maven3.x版本,User Settings 中选择对应maven版本的配置文件settings.xml
eclipse的preferences->maven->可选择Download Artifact Sources/JavaDocs 



mvn archetype:generate  会提示输入groupId,groupId

会生成pom.xml做模板使用 ,也可以使用eclipse mavne插件,建立maven项目来选择
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

对web项目
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false
 
 
 

<project> <!-- 未测试 -->
   
   <build>
     
    <plugins>
      
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.6</version>
        <configuration>
          <instrumentation>
            <ignores>
              <ignore>com.example.boringcode.*</ignore>
            </ignores>
            <excludes>
              <exclude>com/example/dullcode/**/*.class</exclude>
              <exclude>com/example/**/*Test.class</exclude>
            </excludes>
          </instrumentation>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  
   
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.6</version>
      </plugin>
    </plugins>
  </reporting>
</project>
----------------------------------Gradle
Spring 和 Android使用 ,可以构建 C++

bin 目录入 PATH 环境变量下,初次运行 gradle 命令会在~\.gradle下生成文件

spring 有 gradle 示例
项目中有 src\main\java\包名  (同 maven)
项目中有 build.gradle 文件 
		如有 apply plugin: 'java' 

gradle tasks 命令可看到所有可用的 build任务
就可用  gradle build 命令构建 ,会在当前目录下生成 build\libs,build\classes目录 
  
  
---build.gradle  文件

//apply plugin: 'java' 
apply plugin: 'application'
mainClassName = 'hello.HelloWorld'  //可以使用  ./gradlew run 来运行

repositories {
	//mavenLocal()
	//jcenter()  //会从  https://jcenter.bintray.com/com/ 下载
	mavenCentral()
	maven { url 'http://repo.spring.io/libs-release/' }
	 
}
dependencies {
    compile 'org.springframework:spring-context:4.3.4.RELEASE'
	testCompile "junit:junit:4.12"
	//也有providedCompile
} 
jar {
    baseName = 'my-project'  //生成jar包的名字为my-project-0.1.0.jar
    version =  '0.1.0'
}
task wrapper (type:Wrapper)  // 就可以直接使用 gradle wrapper ,而不用加--gradle-version 3.2
{
	gradleVersion = 3.2 //只可有一个小数点
} 

会自动下载其它依赖的包,在~/.gradle\caches\modules-2\files-2.1目录下


gradle wrapper --gradle-version 3.2.1  会生成 gradlew 可执行文件和gradle/wrapper目录 在项目目录下,gradle-wrapper.properties文件中下载gradle对应版本的URL

就可以执行 ./gradlew build 来构建项目

 


----------------------------------ANT
ant build.xml
ant -buildfile myBuilde.xml  或者  -f 或 -file
	-d 是debug输出
	
<taskdef name="simpletask" classname="ant_xml_.MyTask" classpath="${build.dir}" />
	用于自定义一些任务，为了可以在同一个构建过程中使用编译过的任务，<taskdef>必须出现在编译之后。

可以重写execute方法，和init方法，也可加setSize，setDir(File dir)
log("进入自定义任务的excecute---" , Project.MSG_INFO);
Project proj=getProject();
File base=proj.getBaseDir();

<tstamp>
	<format property="myDate" pattern="yyyy-MM-dd HH:mm:ss"/>
</tstamp>	就可以用 "${DSTAMP}${TSTAMP}.war"
<property enviroment="env"/>  就可以使用  "${env.JAVA_HOME}"
<property file="aa.properties" prefix="props"/> 
内置的一些 ${os.name},${ant.java.version},${ant.file}构建文件的绝对路径
 *.* 表示当前目录下的所有文件
 **表示当前目录及所有子目录的所有文件

<simpletask size="30"   dir="${build.dir}">;


<project name="platform" default="all" basedir=".">
	<target name="all" description="build">
	 <javac  destdir="classes" srcdir="src"   ></javac>   <!--对没有修改的java文件不会再次编译-->
 	 <copy  todir="classes"  overwrite="no" > <!--对没有修改xml文件不会再次复制-->
	 	<fileset dir="src">
	 		<include name="*.xml"></include>
	 	</fileset>
	 </copy>
	</target>
</project>

可以弹出对话框,以下拉形式选择
<input validargs="${default.templates}" defaultvalue="${extgen.template.default}" addproperty="input.template">
Please choose a template for generation.
Press [Enter] to use the default value
</input>

可以弹出对话框,让用户输入
<input defaultvalue="${extgen.extension.name}" addproperty="input.name">
Please choose the name of your extension. It has to start with a letter followed by letters and/or numbers.
Press [Enter] to use the default value</input>

ANT JUINT

<macrodef  是定义任务的
		<sequential>  it can contain other Ant tasks.
----------------------------------Junit

Junit 要求

1.必须是public void 无参方法 ,
2.必须以test开头


assertEquals(x, y);
Assert.fail();
Assert.assertEquals(x,y);

Assert.fail() 如果程序执行到这里,停止测试,测试失败,后面的不会被执行



TestCase 中有
	setUp() ,	在每一个testXxxx方法执行前去调用,可能会多次调用 
	tearDown(),	在每一个testXxx方法后被调用


这两个方法在抛出异常时也会被调用,测试失败也会的



JUnit 4.0 有 只执行一次初始方法,销毁方法 
import static org.junit.Assert.assertEquals; 
 类不必继承自TestCase

@BeforeClass
public static void init()//必须是static
{
}
@AfterClass
public static void destory()
{
}

@Before  //不能是static的
public   void initObj() 
{
}

@Test
public void myTest()//不必以test开头
{
}


textui包  TestRunner类  run(Class testClass)   几个点,表示几个测试方法  
junit.textui.TestRunner.run(HelloTest.class);



java -cp .;junit-4.12.jar;hamcrest-core-1.3.jar  org.junit.runner.JUnitCore mytest.CalculatorTest  //会运行所有方法上有@Test

如使用 Main方法
Result result = JUnitCore.runClasses(CalculatorTest.class);
for (Failure failure : result.getFailures()) {
	System.out.println(failure.toString());
}
if (result.wasSuccessful()) {
	System.out.println("所有测试用列执行成功");
}
	
	
//junit.awtui.TestRunner.run(HelloTest.class);
//junit.swingui.TestRunner.run(HelloTest.class);
//awtui包 TestRunner类
//swingui包 TestRunner类



边界问题 数组没有元素


测试应该没有依赖性,一个测试方法不会依赖于另一个方法
public static void main (String[] args) 
{
		junit.textui.TestRunner.run (suite());
		junit.swingui.TestRunner.run(Junit3TestSuit.class);// 只对junit3
}

public static junit.framework.Test suite() {   
    return new JUnit4TestAdapter(ListTest.class); 
}


public static Test suite() //use eclipse,junit 4 ,not need main method
{
	return new TestSuite(ListTest.class);
}

TestSuite 和TestCase 都实现了Test 接口 	



示例
@RunWith(value = Parameterized.class)
public class CalculatorTest
{
	private int expected;
	private int para1;
	private int para2;
	@Parameterized.Parameters
	public static Collection<Integer[]> getParameters()//返回Collection
	{
		return Arrays.asList(new Integer[][] { { 3, 3, 2 }, // expected, para1, para2
											   { 1, 1, 1 } });// expected, para1, para2
	}
	public CalculatorTest(int expected, int para1, int para2)//构造函数的参数类型必须全一样 
	{
		this.expected = expected;
		this.para1 = para1;
		this.para2 = para2;
	}
	@Test
	public void testPlus()
	{
		int acutal=Math.max(para1, para2);
		assertEquals(expected, acutal);
	}
}

//TestSuit 
@RunWith(Suite.class)
@SuiteClasses({CalculatorTest.class})
public class CalculatorTestSuit 
{

}
---------------------------EasyMock
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
//---实例
IMocksControl control = EasyMock.createControl();//使用多个 Mock 对象
ResultSet mockResultSet = control.createMock(ResultSet.class);//调用顺序是不进行检查

mockResultSet.next();
EasyMock.expectLastCall().andReturn(true).times(3);//对next方法调3次返回true
EasyMock.expectLastCall().andReturn(false).times(1);//再调一次返回false

mockResultSet.getString(1);//对getString(1)方法调用的返回
EasyMock.expectLastCall().andReturn("DEMO_ORDER_001").times(1);
EasyMock.expectLastCall().andReturn("DEMO_ORDER_002").times(1);
EasyMock.expectLastCall().andReturn("DEMO_ORDER_003").times(1);

mockResultSet.getDouble(2);
EasyMock.expectLastCall().andReturn(350.0).times(1);
EasyMock.expectLastCall().andReturn(1350.0).times(1);
EasyMock.expectLastCall().andReturn(5350.0).times(1);

control.replay();//调用后mockResultSet就有值可用了

//...使用生成的Mock对象

control.verify();//验证方法调用真的完成了,如调用次数

//----
ResultSet strickMockResultSet = EasyMock.createStrictMock(ResultSet.class);//会检查调用方法的顺序
IMocksControl strictControl = EasyMock.createStrictControl();
ResultSet strickMockResultSet1 = strictControl.createMock(ResultSet.class);//会检查调用方法的顺序

ResultSet mockResultSet = EasyMock.createMock(ResultSet.class);//只需要一个 Mock 对象,调用顺序是不进行检查
mockResultSet.close();
EasyMock.expectLastCall().times(3, 5);//3-5次 atLeastOnce(),anyTimes()

EasyMock.expectLastCall().andStubReturn(mockResultSet);//方法的调用总是返回一个相同的值
SQLException exception=new SQLException();
EasyMock.expectLastCall().andThrow(exception);//预期异常抛出
EasyMock.expectLastCall().andStubThrow(exception);//设定抛出默认异常
	
mockStatement.executeQuery("SELECT * FROM sales_order_table");//有大小写的问题
mockStatement.executeQuery( EasyMock.anyObject().toString() );

EasyMock.reportMatcher(new SQLEquals(in));//注册自定义,implements IArgumentMatcher ,实现matches方法

//---Replay 状态
EasyMock.replay(mockResultSet);
control.replay();
//...

//为了避免生成过多的 Mock 对象，EasyMock 允许对原有 Mock 对象进行重用,如要重新初始化，我们可以采用 reset 方法
EasyMock.reset(mockResultSet);
control.reset();
//再mock方法,可使用spring中的getBean(),可动态修改IOC中的bean
EasyMock.replay();
control.replay();
EasyMock.verify(mockResultSet);
control.verify();

{
	//匹配指定参数
	mockObject.getProductPrice("tomota");
	EasyMock.expectLastCall().andStubReturn(45.0f);

	//匹配所有参数 放在最后  OK
	mockObject.getProductPrice(EasyMock.isA(String.class));
	EasyMock.expectLastCall().andStubReturn(55.0f);
}


---Spring 中的Bean做Mock
@Resource(name="myServiceBean")
private MyServiceBean bean;
	
IMocksControl mocksControl = EasyMock.createStrictControl();
MyDao mockDao=mocksControl.createMock(MyDao.class);//是class 要 objenesis.jar

EasyMock.expect(mockDao.insertData(dataSet)).andReturn((long)dataSet.size());

ReflectionTestUtils.setField(bean, "dao", mockDao, MyDao.class);//用spring提供的方法注入aurowired的字段
EasyMock.replay(mockDao);
EasyMock.verify(mockDao);

//调用
Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));

	
//也可使用BeanPostProcessor
public class DaoPostProcessor implements BeanPostProcessor 
{
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException 
	{
		if("myDao".equals(beanName) ||bean instanceof  MyDao)
		{
			IMocksControl mocksControl = EasyMock.createStrictControl();
			MyDao clientMock=mocksControl.createMock(MyDao.class);
			
			//---List中的Product要重写equals方法
			//EasyMock.expect(clientMock.insertData(dataSet)).andReturn((long)dataSet.size());
			//EasyMock.expect(clientMock.queryData(param)).andReturn(dataSet);
			
			//--不用重写equals方法
			clientMock.insertData(EasyMock.isA(List.class));//有isNull(List.class),基本类型EasyMock.anyLong()
			//clientMock.queryData(EasyMock.anyObject(Product.class));//anyObject匹配传入null,isA不能匹配null
//			clientMock.queryData(
//						(Product) EasyMock.or(EasyMock.isA(Product.class),
//									EasyMock.isNull() )
//						);
			EasyMock.expectLastCall().andStubReturn((long)dataSet.size());
			
			clientMock.queryData(EasyMock.isA(Product.class));
			EasyMock.expectLastCall().andStubReturn(dataSet);
			
			EasyMock.replay(clientMock);
			return clientMock;
		}else
			return bean;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		return bean;
	}
}
//调用
//---Product要重写equals方法
//Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));
//Assert.assertEquals(dataSet, bean.queryData(param));
//---Product不用重写equals方法
Assert.assertEquals(dataSet.get(0).getName(), bean.queryData(param).get(0).getName());
Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));
		
		

--------------------------iText
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SimpleBookmark;

Document document = new Document();
document.addTitle("title");
document.addAuthor("Author");
document.addKeywords("my ");
document.addCreator("myCreator ");

PdfWriter.getInstance(document, new FileOutputStream("C:\\HelloWorld.pdf"));
document.open();
document.add(new Paragraph("Hello World"));
document.close();
--表格
PdfPCell title=new PdfPCell(new  Paragraph(10,"hello world"))  //P 代表Paragraph,Paragraph可带字体
//new PdfPCell(new Phrase("Cell with rowspan 2"));
title.setColspan(2);
title.setRowspan(2);
title.setBackgroundColor(Color.RED);
PdfPTable table=new PdfPTable(int numColumns)
table.addCell(title)
document.add(table);
---图片
document.newPage();   //换页   
 //可以是绝对路径，也可以是URL
String strImg="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/google.png";
 Image img = Image.getInstance(strImg);   
 // Image image = Image.getInstance(new URL(http://xxx.com/logo.jpg));   
 img.setAbsolutePosition(0, 0);   
 document.add(img);

 -----读PDF
PdfReader reader = new PdfReader("d:/temp/mybatis.pdf");//读已经存在PDF
System.out.println(reader.getPdfVersion());
//---读写
PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("d:/temp/itext_out.pdf"));
//建立pdf后,ttf文件不存在也可以(可能是系统中有)
String ttf="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);
for (int i=1; i<=reader.getNumberOfPages(); i++)
{
	  //获得pdfstamper在当前页的上层打印内容。也就是说 这些内容会覆盖在原先的pdf内容之上。
	  PdfContentByte over = stamper.getOverContent(i);
	  //用pdfreader获得当前页字典对象。包含了该页的一些数据。比如该页的坐标轴信息。
	  PdfDictionary p = reader.getPageN(i);
	  //拿到mediaBox 里面放着该页pdf的大小信息。
	  PdfObject po =  p.get(new PdfName("MediaBox"));
	  System.out.println(po.isArray());
	  //po是一个数组对象。里面包含了该页pdf的坐标轴范围。
	  PdfArray pa = (PdfArray) po;
	  System.out.println(pa.size());
	  //看看y轴的最大值。
	  System.out.println(pa.getAsNumber(pa.size()-1));
	  //开始写入文本
	  over.beginText();
	  //设置字体和大小
	  over.setFontAndSize(font.getBaseFont(),10);
	  //设置字体的输出位置
	  over.setTextMatrix(107, 540);
	  //要输出的text
	  over.showText("我要加[终稿]字样 " + i);
	  over.endText();
	  //创建一个image对象。
	  String strImg="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/google.png";
	  Image image = Image.getInstance(strImg);
	  //设置image对象的输出位置pa.getAsNumber(pa.size()-1)。floatValue() 是该页pdf坐标轴的y轴的最大值
	  image.setAbsolutePosition(0,pa.getAsNumber(pa.size()-1).floatValue()-100);//0, 0, 841.92, 595.32
	  over.addImage(image);
	  //画一个圈。
	  over.setRGBColorStroke(0xFF, 0x00, 0x00);
	  over.setLineWidth(5f);
	  over.ellipse(250, 450, 350, 550);
	  over.stroke();

}
stamper.close();
//---
List list = SimpleBookmark.getBookmark ( reader ) ;
for ( Iterator i = list.iterator () ; i.hasNext () ; ) 
{
	showBookmark (( Map ) i.next ()) ;
}
private static void showBookmark ( Map bookmark ) 
{
	System.out.println ( bookmark.get ( "Title" )) ;
	ArrayList kids = ( ArrayList ) bookmark.get ( "Kids" ) ;
	if ( kids == null )
		return ;
	for ( Iterator i = kids.iterator () ; i.hasNext () ; ) 
	{
		showBookmark (( Map ) i.next ()) ;
	}
}	

----web中应用

<%		
	String user = request.getHeader("User-Agent");
	if(user.indexOf("MSIE") != -1 && user.indexOf("Windows") != -1)
	  {
		  out.print("<body leftMargin=\"0\" topMargin=\"0\" scroll=\"no\">");
		    out.print("<EMBED src=\"PDFPreview.pdf?type=pdf\" "
		      + "width=\"100%\" height=\"100%\" "
		      + "fullscreen=\"yes\" type=\"application/pdf\">");

	  } else
	  {
	    out.print("<body leftMargin=\"0\" topMargin=\"0\" scroll=\"no\">");
	    out.print("<script>document.location = 'PDFPreview.pdf?type=pdf';</script>");
	  }
%>

----Servlet的内容:
response.setContentType("application/pdf");
Document document = new Document();
ByteArrayOutputStream buffer = new ByteArrayOutputStream();

PdfWriter.getInstance(document, buffer);
document.open();
document.add(new Paragraph("Hello World"));
document.close();

DataOutput output = new DataOutputStream(response.getOutputStream());
byte[] bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
for (int i = 0; i < bytes.length; i++)
{
	output.writeByte(bytes[i]);
}
//web结束






中文不显示方法一 OK
//建立pdf后,ttf文件不存在也可以(可能是系统中有)
String ttf="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);

中文不显示方法二
//要加对应版本的,iTextAsian.jar  也不行???
BaseFont bf = BaseFont.createFont("STSong-Light,Bold", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);//可不加Bold
document.add(new Paragraph("混沌之神", new Font(bf)));

JFreeChart 和 iText
将 DefaultFontMapper mapper = new DefaultFontMapper();语句替换为
   AsianFontMapper mapper = new AsianFontMapper("STSong-Light","UniGB-UCS2-H");

   
   
======================PdfBox=============================
依赖于commons-logging,fontbox
<dependency>
  <groupId>org.apache.pdfbox</groupId>
  <artifactId>pdfbox</artifactId>
  <version>2.0.0</version>
</dependency>
---1.8 
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

---读
boolean sort = false;
String textFile = null;
String pdfFile = "D:/my/Spring_源码分析.pdf";
PDDocument document = PDDocument.load(new File(pdfFile));
if (pdfFile.length() > 4) {
	textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
}
// 文件输出流，写入文件到textFile
Writer output = new OutputStreamWriter(new FileOutputStream(textFile),"UTF-8");
// PDFTextStripper来提取文本
PDFTextStripper stripper = new PDFTextStripper();//可加GBK,但中文OK
stripper.setSortByPosition(sort);
stripper.setStartPage(1);
stripper.setEndPage(20);
// 调用PDFTextStripper的writeText提取并输出文本
stripper.writeText(document, output);
output.close();
document.close();
//---
PDFParser parser = new PDFParser(new RandomAccessFile(new File(pdfFile),"rw"));  
parser.parse();  
PDDocument pdfdocument = parser.getPDDocument();  
PDFTextStripper stripper = new PDFTextStripper();  
String result = stripper.getText(pdfdocument);  
System.out.println(result);  
---写
PDDocument document = new PDDocument();
PDPage page = new PDPage();
document.addPage( page );

// Create a new font object selecting one of the PDF base fonts
PDFont font = PDType1Font.HELVETICA_BOLD;//中文不行

// Create a new font object by loading a TrueType font into the document
//PDFont font = PDTrueTypeFont.loadTTF(document, "c:\\WINDOWS\\Fonts\\SIMHEI.TTF");//中文不正常

PDPageContentStream contentStream = new PDPageContentStream(document, page);

contentStream.beginText();
contentStream.setFont( font, 12 );
contentStream.newLineAtOffset( 100, 700 );
contentStream.showText( "Hello World_中__" );
contentStream.endText();
contentStream.close();
document.save( "d:/temp/Hello World.pdf");
document.close();

=================================Lucene-6.4================================
 最新的 luke-src-4.0.0 最近更新是2012年7月
 
 官方带的中文分词器 analyzers-smartcn  ,lucene-analyzers-smartcn-6.4.0.jar  大小  3.43M
 
 
	core/lucene-core-6.4.0.jar
	analysis/common/lucene-analyzers-common-6.4.0.jar
	analysis/smartcn/lucene-analyzers-smartcn-6.4.0.jar
	queryparser/lucene-queryparser-6.4.0.jar
	
// http://lucene.apache.org/core/6_4_0/index.html          Lucene demo, its usage, and sources
	
//Document可以看作是 数据库的一行记录，Field可以看作是数据库的字段

--建立索引
//Directory dir = new RAMDirectory();
Directory dir = FSDirectory.open(Paths.get("c:/tmp/testindex"));//是生成索引的目录名
Analyzer analyzer = new StandardAnalyzer();
IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
//每次建立索引文件名顺序号会增加,默认只保留最近的2个相邻的顺序号  (0-9a-z)
iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);//建立是 OpenMode.CREATE 
IndexWriter writer = new IndexWriter(dir, iwc);

 
Document doc = new Document();
// StringField不分词 查询时要输入完整的查询
// TextField是分词的

doc.add(new StringField("path","c:/temp/file1.txt",Field.Store.YES)); //索引,但不分词,YES 表示后面可以用doc.get("path");来得到
String fName= file.getFileName().toString();
if(fName.contains("."))
	  fName=fName.substring(0,fName.lastIndexOf("."));
doc.add(new TextField("name",fName,Field.Store.YES));
doc.add(new TextField("id", Integer.toString(i++),Field.Store.YES));
doc.add(new  StoredField("size",  file.toFile().length()));  
//doc.add(new Field("contents",  "我来自中国" , TextField.TYPE_STORED));
doc.add(new LongPoint("modified", lastModified));//是index的,但不store
doc.add(new StoredField("createTime",new Date().getTime()));//store的
 field.setBoost(1.2f);//默认1.0,建立索引时(更新索引不可)在原有基础上加权,为某些特定的内容,分值高,可做优先显示
 
//建立
doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))); 
if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
	writer.addDocument(doc);
} else { 
	writer.updateDocument(new Term("path", file.toString()), doc); // 其实是先删,再加,要影响数量就会查会到numDeletedDocs的值,
}
 
 writer.deleteDocuments(new Term("path", file.toString()));//windows下路径用\\分隔,Term精确查找,可用Query,
 //delete后 reader.numDocs(); 比  reader.maxDoc(); 少1  
 //writer.forceMergeDeletes();//没用??数maxDoc也没变少,numDeletedDocs也有值,实际是否执行由MergePolicy决定,默认是TieredMergePolicy
  writer.forceMerge(1); //当有当删除个数大于指定数才有用
  //writer.deleteUnusedFiles();//没用??数maxDoc也没变少,numDeletedDocs也有值
writer.close();
dir.close();



--使用索引搜索
public class MyFasterQueryParser extends QueryParser {
	public MyFasterQueryParser(String f, Analyzer a) {
		super(f, a);
	}
	@Override
	protected Query getFuzzyQuery(String field, String termStr, float minSimilarity)
			throws ParseException {
		throw new ParseException("由于性能原因禁用 FuzzyQuery  ");
	}
	@Override
	protected  Query getWildcardQuery(String field, String termStr) throws ParseException {
		throw new ParseException("由于性能原因禁用 WildcardQuery  ");
	}
	/*
	@Override
	protected   Query getRangeQuery(String field, String part1, String part2, boolean startInclusive, boolean endInclusive)throws ParseException {
	   // if(field.equals("size"))//报错???
	   // 	return  LongPoint.newRangeQuery(field,Long.parseLong(part1),Long.parseLong(part2));
		return super.newRangeQuery(field, part1, part2, startInclusive, endInclusive);
	}
	*/
}

 Directory directory = FSDirectory.open(Paths.get("c:/tmp/testindex"));//刚刚生成的索引目录
DirectoryReader oldReader = DirectoryReader.open(directory);
IndexReader   reader=DirectoryReader.openIfChanged(oldReader);//如果索引发生的改变,返回一个新的Reader(测试无用？？？),否则返回null
if(reader==null) 
	reader=oldReader;

if(reader.hasDeletions())
{
	System.out.println("maxDoc : "+reader.maxDoc());
	System.out.println("numDocs : "+reader.numDocs());
	System.out.println("numDeletedDocs : "+reader.numDeletedDocs());
}

IndexSearcher searcher = new IndexSearcher(reader);
Analyzer analyzer = new StandardAnalyzer();
QueryParser parser = new QueryParser( "contents", analyzer);//"contents"是上面建立的字段
//MultiFieldQueryParser parser =new MultiFieldQueryParser(new String[]{"fieldname","contents"},new SmartChineseAnalyzer());
//MyFasterQueryParser parser = new MyFasterQueryParser(field, analyzer);//禁用性能低的查询功能,如~,以*开头的通配
 
//Query query = parser.parse(line);//要搜索的内容
//Query query =parser.parse("name:LongPoint");//改变搜索域
//Query query =parser.parse("name:Long*");//通配
parser.setAllowLeadingWildcard(true);
Query query =parser.parse("name:*Point");//默认不能以*开头的通配，因为效率低，要setAllowLeadingWildcard开启
	  
parser.setDefaultOperator(Operator.AND); //所有的词都要出现
Query query = parser.parse("Morphology KStemFilter");//有空格默认就是OR
Query query = parser.parse("Morphology AND KStemFilter");//也可这样用AND,OR
Query query = parser.parse("(Morphology AND KStemFilter) OR　name:Long*");//使用()做多个组合
 
//Query query =parser.parse("-name:LongPoint +Morphology");//前面-表示不能出现,+表示要出现
//Query query =parser.parse("NOT name:LongPoint AND Morphology"); //NOT同-,AND同+ ,功能同上

//Query query =parser.parse("id:[2 TO 4 ]");//TO 必须大写,id是符类型的,但显示结果为什么多??
//Query query =parser.parse("id:{2 TO 4 ]");//大括号开区间,中括号闭区间,同数学

//Query query =parser.parse("\"fuzzy searches\"");//必须是两个词紧挨着,用""
//Query query =parser.parse("\"jakarta apache\"~10");//两个词间距离小于10的
//文档上说的
//Query query =parser.parse("roam~");//模糊查会显示 foam 或者 roams 
//Query query =parser.parse("/[mb]oat/");//会显示  "moat" 或者 "boat" 

//Query query =new TermRangeQuery("fieldname",new BytesRef("text"),new BytesRef("text10"),true,true); 

Calendar c=Calendar.getInstance();
c.set(Calendar.YEAR, 2016);
//Query query =  LongPoint.newRangeQuery("modified",c.getTime().getTime(),Calendar.getInstance().getTimeInMillis());

PrefixQuery query=new PrefixQuery(new Term("path","C:\\My\\software\\_java\\lucene-6.4.0\\lucene-6.4.0\\docs\\core\\org\\apache\\lucene\\document\\Long"));
//PrefixQuery query=new PrefixQuery(new Term("contents","LongPoint"));//这样是没用的
WildcardQuery query=new WildcardQuery(new Term("path","*LongPoint*"));

BooleanQuery.Builder builder= new BooleanQuery.Builder();
Term term=new Term("path","C:\\My\\software\\_java\\lucene-6.4.0\\lucene-6.4.0\\docs\\core\\org\\apache\\lucene\\document\\LongPoint.html");
builder.add(new TermQuery(term),Occur.MUST);//有MUST,SHOULD,MUST_NOT
Calendar c=Calendar.getInstance();
c.set(Calendar.YEAR, 2016);
builder.add(LongPoint.newRangeQuery("modified",c.getTime().getTime(),Calendar.getInstance().getTimeInMillis()),Occur.MUST);
BooleanQuery query=builder.build();


PhraseQuery.Builder builder = new PhraseQuery.Builder();
builder.setSlop(1);// 表示跳1个词 ， 如有 see this message ,先see,再message就可 查到
builder.add(new Term("contents", "see"));
builder.add(new Term("contents", "message"));
PhraseQuery query = builder.build();

FuzzyQuery query=new FuzzyQuery(new Term("contents","make"));//对于  make 可查出  mike或者 jake,可能对于查不到结果时
   
searcher.search(query, 100);// 前100个命中
//searcher.search(query, 100,Sort.INDEXORDER);//排序,还有Sort.RELEVANCE 
searcher.search(query, 100,new Sort(new SortField("size",SortField.Type.LONG)));//没效果????
searcher.search(query, 100,new Sort(new SortField("name",SortField.Type.STRING,true)));//反向排序 ,Sort中可传多个SortField,没效果????
//searcher.search(query, Collector);//Collector 文档有警告可能不兼容后期版本，可自定义排序和过滤词,聚合,如何做????
 //新版本没有search的Filter参数,也不能自定义评分


TopDocs results = searcher.search(query, 5);//最多显示少个结果
ScoreDoc[] hits = results.scoreDocs;
					results.totalHits;//如果实际结果大于最多显示,totalHits存实际结果

 while(results.scoreDocs.length>0)
{
	System.out.println("--------------------");
	ScoreDoc lastScoreDoc=null;
	for(ScoreDoc scoreDoc:results.scoreDocs )
	{
		lastScoreDoc=scoreDoc;
		 Document doc = searcher.doc(scoreDoc.doc);
		 System.out.println("score="+scoreDoc.score+",id="+(doc.get("id")) + ",path=" + doc.get("path"));
	}
	results=searcher.searchAfter( lastScoreDoc, query, hitsPerPage);  //可以使用searchAfter来分页
}

TermQuery tQuery=new TermQuery(new Term("fieldname","text"));
hits = isearcher.search(tQuery,  1000).scoreDocs;
		
//遍历hits
Document doc = searcher.doc(hits[i].doc);//hits[i].doc是一个docID
System.out.println("score="+hits[i].score);//和boost加权相关的

doc.get("path");//上面建立的

reader.close();
directory.close();

-------分词
常用分词器
  //this is my football ,  I  very like it
  Analyzer analyzer = new StandardAnalyzer();  // (my)(football)(i)(very)(like)
  Analyzer analyzerSimple= new SimpleAnalyzer();//(this)(is)(my)(football)(i)(very)(like)(it)
  Analyzer analyzerStop= new StopAnalyzer();//(my)(football)(i)(very)(like)
  Analyzer analyzerWhite= new WhitespaceAnalyzer();//(this)(is)(my)(football)(,)(I)(very)(like)(it)
  Analyzer analyzerCN= new  SmartChineseAnalyzer() ;// 官方带的中文分词器  lucene-analyzers-smartcn-6.3.0.jar

Analyzer analyzerCN= new  SmartChineseAnalyzer() ; //可传停用词,看源码默认停用词只是一些标点,词典在coredict.mem是二进制文件(还有一个bigramdict.mem没找到源码)
//这是我的足球，我非常喜欢它
TokenStream stream=analyzer.tokenStream("contents", "this is my football ,  I  very like it" );

OffsetAttribute offsetAttr=stream.addAttribute(OffsetAttribute.class);
PositionIncrementAttribute posiIncrementAttr=stream.addAttribute(PositionIncrementAttribute.class);
TypeAttribute typeAttr=stream.addAttribute(TypeAttribute.class);
CharTermAttribute charAttr=stream.addAttribute(CharTermAttribute.class);

stream.reset();//按TokenStream javadoc顺序做
while(stream.incrementToken())
{
  System.out.print(posiIncrementAttr.getPositionIncrement()+",");
  System.out.print(offsetAttr.startOffset()+"-"+offsetAttr.endOffset());
  System.out.print(typeAttr.type());//StandardAnalyzer是 <IDEOGRAPHIC>，其它看源码默认是word
  System.out.println("("+charAttr+")");
}
stream.end();
stream.close();//放 finally  中 
analyzer.close();

Analyzer 的方法 TokenStream tokenStream(String fieldName, Reader reader)   
  
TokenStream 的子类有 TokenFilter, Tokenizer,
先把读到的文本分词转成Tokenizer,再用多个 TokenFilter 去无用的如标点, am,are ,an ,the 

//自定义Analyzer
public class MyStopAnalyzer extends Analyzer{
	private CharArraySet stopWords;
	public MyStopAnalyzer(String stopWord[])
	{
		stopWords=StopFilter.makeStopSet(stopWord,true);
		stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new LowerCaseTokenizer();
        return new TokenStreamComponents(source, new StopFilter(source, stopWords));
		 
	} 
}
Analyzer myAnalyzer =new MyStopAnalyzer(new String[]{"this","is"});
TokenStream stream=myAnalyzer.tokenStream("contents", "this is my football ,  I  very like it" );

//可以使用同义词搜索
public class MySameAnalyzer extends Analyzer{
	//参考SmartChineseAnalyzer 的实现
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new HMMChineseTokenizer();
        return new TokenStreamComponents(tokenizer, new MySameTokenFilter(tokenizer));
	} 
}
public class MySameTokenFilter extends TokenFilter {
	private CharTermAttribute charTermAttr = null;
	private AttributeSource.State state=null;
	private  PositionIncrementAttribute posiIncrementAttr=null;
	private Stack<String> sameWordStack=null;
	Map<String,String[]> maps = new HashMap<String,String[]>();
	protected MySameTokenFilter(TokenStream input) {
		super(input);
		charTermAttr = this.addAttribute(CharTermAttribute.class);
		posiIncrementAttr=this.addAttribute(PositionIncrementAttribute.class);
		sameWordStack=new Stack<>();
		maps.put("中国",new String[]{"天朝","大陆"});
		maps.put("我",new String[]{"咱","俺"});
	}
	@Override
	public boolean incrementToken() throws IOException {
		/*
		 if (!input.incrementToken())
			return false;
		if(charTermAttr.toString().equals("中国"))
		{
			charTermAttr.setEmpty();
			charTermAttr.append("大陆");
		}
		if(charTermAttr.toString().equals("我"))
		{
			charTermAttr.setEmpty();
			charTermAttr.append("唵");
		}
		*/
		//---------------
		while(sameWordStack.size()>0)
		{
			String word=sameWordStack.pop();
			restoreState(state);
			charTermAttr.setEmpty();
			charTermAttr.append(word);
			posiIncrementAttr.setPositionIncrement(0);
			return true;
		}
		 if (!input.incrementToken())
				return false;
		 if(maps.containsKey(charTermAttr.toString()))
		 {
			for(String word:maps.get(charTermAttr.toString()))
			{
				sameWordStack.add(word);
			}
			state=captureState();
		}
		return true;
	}
}
---- HighLiger

highlighter/lucene-highlighter-6.4.0.jar 依赖 memory/lucene-memory-6.4.0.jar

 
String txt="我来自中国，中国面积很大,人很多";
			
//TermQuery query=new TermQuery(new Term("f","中国"));

QueryParser parser=new QueryParser("f",new SmartChineseAnalyzer()); 
Query query=parser.parse("中国 面积");

QueryScorer scorer=new QueryScorer(query);
Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);

//Highlighter highLiter =new Highlighter(scorer);//默认是<B> </B>
			
Formatter formatter=new SimpleHTMLFormatter("<span style='color:red'>","</span>");
Highlighter highLiter =new Highlighter(formatter,scorer);

highLiter.setTextFragmenter(fragmenter);
String highTxt=highLiter.getBestFragment(new SmartChineseAnalyzer(),"f",txt);
System.out.println(highTxt);//变为        <B>中国</B>
 

---- tika  用于打开各种不同格式文档 DOCX,XLS, PDF,HTML

java -jar tika-app-1.14.jar  有界面 -g
java -jar tika-app-1.14.jar  --help

//Maven 
  <dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>1.14</version>
  </dependency>
  <dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-parsers</artifactId>
    <version>1.14</version>
  </dependency>
//Gradle 
dependencies {
    runtime 'org.apache.tika:tika-parsers:1.14'
}

Parser parser=new AutoDetectParser();
InputStream input=null;
//String filePath="C:/tmp/spring-hateoas-reference-0.19.pdf";
String filePath="C:/_work/员工简历模板.xlsx";
try{
	input=new FileInputStream(new File(filePath));
	ParseContext context=new ParseContext();
	context.set(Parser.class, parser);
	BodyContentHandler handler=new BodyContentHandler();
	Metadata meta=new Metadata();
	meta.set(Metadata.RESOURCE_NAME_KEY, filePath);
	parser.parse(input ,handler , meta, context);
	String fileContent=handler.toString();
	for(String name:meta.names())
	{
		System.out.println(name +"," +meta.get(name));
	}
	System.out.println(fileContent);
}catch (Exception e) {
	e.printStackTrace();
}finally {
	IOUtils.closeQuietly(input);
}



//方式二，性能低
Tika tika =new Tika();
String fileContent=tika.parseToString(new FileInputStream(filePath),meta);


//doc.add(new TextField("contents",  new Tika().parse(new File(filePath))));//Lucene使用tika,不会Store
//doc.add(new Field("contents", new Tika().parse(new File(filePath)), TextField.TYPE_STORED));//报错,Reader不能使用Store
doc.add(new Field("contents", new Tika().parseToString(new File(filePath)), TextField.TYPE_STORED));

---- 数据被修改了,立即就可以查到
SearcherManager manager=new SearcherManager(directory,new SearcherFactory());//SearcherManager 官方doc说是线程安全的
IndexSearcher isearcher = manager.acquire();
manager.maybeRefresh();//官方doc说，应周期调用, 查询之前,在单独线程中,可以很反应到查询
//...
isearcher.search(query,  1000);
//...
manager.release(isearcher);//finally中做

----Hibernate Search 基于 Lucene
----Elastic Search  收费的，可下载(分布式，RESTful搜索引擎)  基于 Lucene
更适用于新兴的实时搜索应用

 当单纯的对已有数据进行搜索时，Solr更快。
 实时建立索引时, Solr会产生io阻塞，查询性能较差, Elasticsearch具有明显的优势。
 数据量的增加，Solr的搜索效率会变得更低，而Elasticsearch却没有明显的变化。
 从Solr转到Elasticsearch以后的平均查询速度有了50倍的提升。
 
 只支持JSON
 
bin\elasticsearch.bat   启动
 http://localhost:9200/
================================Solr-6.4

bin/solr start -e cloud -noprompt  ( SolrCloud example )启动两个节点,监听 8983 , 7574 端口 ,有zookeeper
	实际调用 solr-6.4.0\server\start.jar    , server/lib/中有jetty
	java -jar server/start.jar --help

控制台提示做了
	solr.cmd start -cloud -p 8983 -s "example\cloud\node1\solr"
	solr.cmd start -cloud -p 7574 -s "example\cloud\node2\solr" -z localhost:9983
	http://localhost:8983/solr/admin/collections?action=CREATE&name=gettingstarted&numShards=2&replicationFactor=2&maxShardsPerNode=2&collection.configName=gettingstarted

	POSTing request to Config API: http://192.168.27.1:8983/solr/gettingstarted/config
	
	
	{"set-property":{"updateHandler.autoSoftCommit.maxTime":"3000"}}

http://localhost:8983/solr/   有界面,看到cloud/collections 组中建立了名为 gettingstarted 

bin/post 只有linux的,如果是windows 使用 java  -Dc=gettingstarted -jar  example/exampledocs/post.jar docs/ 
bin/post -c gettingstarted docs/  对dos目录建立索引-c collection name

bin/solr stop -all
# bin/solr start -e techproducts

//XML
bin/post -c gettingstarted example/exampledocs/*.xml										*/
java -Dc=gettingstarted -jar  example/exampledocs/post.jar example/exampledocs/*.xml		*/

field  update = "add" | "set" | "inc" 
  
<add>
  <doc>
    <field name="employeeId">05991</field>
    <field name="office" update="set">Walla Walla</field>
    <field name="skills" update="add">Python</field>
  </doc>
</add>

<commit/>

//JSON
bin/post -c gettingstarted example/exampledocs/books.json
java -Dtype=application/json -Dc=gettingstarted -jar  example/exampledocs/post.jar example/exampledocs/books.json

//CSV
bin/post -c gettingstarted example/exampledocs/books.csv
java -Dtype=text/csv -Dc=gettingstarted -jar example/exampledocs/post.jar  example/exampledocs/books.csv

http://localhost:8983/solr/gettingstarted/browse 
http://localhost:8983/solr/gettingstarted/browse?q=manu:Belkin&fl=name,id,price&wt=json

q=video&sort=price desc&fl=name,id,price&wt=json
q表示查询什么,name:video表示对字段为name的列
fl的值表示返回的只要name,id
wt返回形式 json,xml,csv


---Data Import Handler (DIH)  从数据库导入
	example\example-DIH 
	bin/solr -e dih 启动

---SolrJ




----------------MongoDB 
 <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>3.4.0</version>
</dependency>
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongodb-driver-async</artifactId>
	<version>3.4.0</version>
</dependency>

mongodb-driver-3.4.0.jar
mongodb-driver-async-3.4.0.jar
	mongodb-driver-core-3.4.0.jar
bson-3.4.0.jar

http://mongodb.github.io/mongo-java-driver/3.4/
	
package nosql.mongodb;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
public class MongoTest
{
	public static void insert(MongoDatabase db)
	{
		MongoCollection<Document> coll = db.getCollection("bios");
		Document doc = new Document("name", "MongoDB").
                append("type", "database").
                append("count", 1).
                append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insertOne(doc);
		coll.createIndex(new Document("name", 1));
		
		db.createCollection("cappedCollection",
				  new CreateCollectionOptions().capped(true).sizeInBytes(0x100000));
	}
	public static void delete(MongoDatabase db) throws Exception
	{
		MongoCollection<Document> collection=db.getCollection("bios");
		DeleteResult rs=collection.deleteMany(new Document("name", "MongoDB"));//new ObjectId("");
		System.out.println("deleted rows:"+rs.getDeletedCount());
	}	
	public static void update(MongoDatabase db ) throws Exception
	{
		MongoCollection<Document> collection =db.getCollection("bios");
		UpdateResult rs=collection.updateOne(new Document("name", "MongoDB"), new Document("$set", new Document("count", 2)));
		System.out.println("update effect rows:"+rs.getModifiedCount());
	}
	public static void query(MongoDatabase db)
	{

		System.out.println("--test db all collection");
		MongoIterable<String> colls = db.listCollectionNames();//= show collections
		for (String s : colls) {
		    System.out.println(s);
		}
		
		System.out.println("-- bios collection   all Index");
		MongoCollection<Document> coll = db.getCollection("bios");
		ListIndexesIterable<Document> list=coll.listIndexes();
		for (Document o : list) {
		   System.out.println(o);
		}
		System.out.println("-- bios collection count:"+coll.count());
		
//		FindIterable<Document> cursor = coll.find();
//		Bson query = new BasicDBObject("name", "MongoDB");
		Bson query = new BasicDBObject("info.x",new BasicDBObject("$lte", 300) ).
										append("name", "MongoDB" );
		FindIterable<Document> iter = coll.find(query);
		MongoCursor<Document> cursor=iter.iterator();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} finally {
		   cursor.close();
		}
	}
	 
	public static void main(String[] args) throws Exception
	{
		//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//		                                      new ServerAddress("localhost", 27018),
//		                                      new ServerAddress("localhost", 27019)));
		List<MongoCredential> credentialList = new ArrayList<MongoCredential>();  
        MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());  
        credentialList.add(credential);  
        MongoClient mongoClient  = new MongoClient(Arrays.asList(new ServerAddress("10.1.5.226", 27017)), credentialList);  
		
		MongoDatabase db = mongoClient.getDatabase( "test" );//= use test
		 System.out.println("--all db");
		for (String s : mongoClient.listDatabaseNames()) {//= show dbs  ,如服务端打开 --auth 这里验证权限 
			   System.out.println(s);
		}
		 db.drop();
	   
		insert(db);
	   update(db);
	   query(db);
	   delete(db);
		
	  
		 
		 mongoClient.close();
	}
}
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;

//MongoClient client = MongoClients.create();//default server localhost on port 27017
//MongoClient client = MongoClients.create("mongodb://localhost");
//MongoClient client = MongoClients.create(new ConnectionString("mongodb://localhost"));

ClusterSettings clusterSettings = ClusterSettings.builder().hosts(Collections.singletonList((new ServerAddress("localhost",47017)))).build();
MongoClientSettings settings = MongoClientSettings.builder().clusterSettings(clusterSettings).build();
MongoClient client = MongoClients.create(settings);
		
//支持JDK8
collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));

Document document = new Document("x", 1);
collection.insertOne(document, new SingleResultCallback<Void>() {
   @Override
   public void onResult(final Void result, final Throwable t) {
	   System.out.println("Inserted!");
   }
});

document.append("x", 2).append("y", 3);
//模板是  UpdateResult
collection.replaceOne(Filters.eq("_id", document.get("_id")), document, 
	new SingleResultCallback<UpdateResult>() {
	   @Override
	   public void onResult(final UpdateResult result, final Throwable t) {
		   System.out.println(result.getModifiedCount());
	   }
   });	
//模板是  List<Document>
collection.find().into(new ArrayList<Document>(), 
	new SingleResultCallback<List<Document>>() {
		@Override
		public void onResult(final List<Document> result, final Throwable t) {
			System.out.println("Found Documents: #" + result.size());
		}
	});

Block<Document> printDocumentBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {//查询到的每个Document调用一次
			System.out.println(document.toJson());
		}
	};
SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
	@Override
	public void onResult(final Void result, final Throwable t) {//只在完成时调用一次
		System.out.println("Operation Finished!");
	}
};
collection.find().forEach(printDocumentBlock, callbackWhenFinished);

collection.find(Filters.gt("i", 50)).forEach(printDocumentBlock, callbackWhenFinished);
collection.find(Filters.exists("i")).sort(Sorts.descending("i")).first(printDocument);
collection.find().projection(excludeId()).first(printDocument);//Projections.excludeId

collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)),
		new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				System.out.println(result.getModifiedCount());
			}
		});
collection.updateMany(lt("i", 100), new Document("$inc", new Document("i", 100)),
		new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				System.out.println(result.getModifiedCount());
			}
		});

//模板是DeleteResult
collection.deleteOne(eq("i", 110), new SingleResultCallback<DeleteResult>() {
	@Override
	public void onResult(final DeleteResult result, final Throwable t) {
		System.out.println(result.getDeletedCount());
	}
});

collection.deleteMany(gte("i", 100), new SingleResultCallback<DeleteResult>() {
	@Override
	public void onResult(final DeleteResult result, final Throwable t) {
		System.out.println(result.getDeletedCount());
	}
});


SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() 
		{
		    @Override
		    public void onResult(final BulkWriteResult result, final Throwable t) {
		    	if(result !=null  && result.getMatchedCount()>0 )
		    	{
		    		  System.out.println("共"+result.getMatchedCount()+"条记录match,delete数:" +result.getDeletedCount() + 
		    				  	",insert数:"+result.getInsertedCount() +
		    				  	",modified数:"+result.getModifiedCount());
		    	}else 
		    	{
		    		  System.out.println( "BulkWriteResult is null or result.getMatchedCount()=0");
		    	}
		    }
		};

collection.bulkWrite(
	  Arrays.asList(new InsertOneModel<>(new Document("_id", 4)),
					new InsertOneModel<>(new Document("_id", 5)),
					new InsertOneModel<>(new Document("_id", 6)),
					new UpdateOneModel<>(new Document("_id", 1),
										 new Document("$set", new Document("x", 2))),
					new DeleteOneModel<>(new Document("_id", 2)),
					new ReplaceOneModel<>(new Document("_id", 3),
										  new Document("_id", 3).append("x", 4))),
	  new BulkWriteOptions().ordered(false),//批量不按顺序做,不加这个参数默认是按顺序做的
	  printBatchResult
);
   
   
--------------------------------------------pluto 不升级,JDK8不可用
pluto(放射性检查计,冥王星) 的角色,用户
Java Specification Request(JSR)
Apache Pluto-2.0.3 , 实现portlet 2 Container 即 JSR-286 ,使用Tomcat-7.0.21


不要把pluto中已有的.jar放在自己的项目中,只供编译使用
portlet-api_2.0_spec-1.0.jar
pluto-taglib-2.0.3.jar
//可以使用eclipse集成pluto,要双击pluto->选择use tomcat installation->选择webapps目录,要在META-INF/建立contex.xml写<Context crossContext="true" />
//能否被pluto admin界面被检测到,是因为web.xml中<url-pattern>/PlutoInvoker/x 对应的org.apache.pluto.container.driver.PortletServlet
//界面pluto admin的page操作就是修改pluto/WEB-INF/conf/pluto-portal-driver-config.xml

启动后使用  http://127.0.0.1:8080/pluto/portal  ,使用用户 pluto,密码pluto登录,即tomcat-users.xml中的配置,看项目web.xml配置
带一个testsuite 项目,有portlet配置可以做复制用

----在纯净的Tomcat中的改变 OK
context.xml中多加
	<Context sessionCookiePath="/">
tomcat-users.xml 默认有
  <role rolename="pluto"/>
  <user username="pluto" password="pluto" roles="pluto,tomcat,manager"/>
  
conf\Catalina\localhost 默认有pluto.xml,testsuite.xml,主要是为配置 crossContext="true"
	<Context path="pluto" docBase="../PlutoDomain/pluto-portal-2.0.3.war" crossContext="true"></Context>
	<Context path="testsuite" docBase="../PlutoDomain/pluto-testsuite-2.0.3.war" crossContext="true"></Context>

把pluto-2.0.3\PlutoDomain\pluto和testsuite复制
还要加.jar到tomcat-6/lib
	pluto-container-api-2.0.3.jar
	pluto-container-driver-api-2.0.3.jar
	portlet-api_2.0_spec-1.0.jar
	pluto-taglib-2.0.3.jar
	ccpp-1.0.jar

	如报找不到org.apache.pluto.driver.PortalStartupListener ,把生成的删除再启动就OK,在pluto项目中pluto-portal-driver.jar
----
 <servlet>
	<servlet-name>changeCaseServ</servlet-name>
	<servlet-class>org.apache.pluto.container.driver.PortletServlet</servlet-class>
	<init-param>
		<param-name>portlet-name</param-name>
		<param-value>ChangeCasePortlet</param-value> <!-- 这里除了是portlet.xml中的值,还要与  <url-pattern>/PlutoInvoker/的值一样才行 -->
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>changeCaseServ</servlet-name>
	<url-pattern>/PlutoInvoker/ChangeCasePortlet</url-pattern><!-- 路径只能是/PlutoInvoker/ 开头 -->
</servlet-mapping>

--------------------------------------------上 pluto
--------------------------------------------Liferay-6.2 CE
下载 bundled with tomcat
下载 Liferay IDE-2.1.1 是elipse插件
下载 plugins SDK 和 portal javadoc

cd liferay-plugins-sdk-6.2-ce-ga2-20140319114139101\liferay-plugins-sdk-6.2\portlets
ant 会下载 liferay-plugins-sdk-6.2\.ivy目录中
create.bat my-greeting2 "My Greeting2" 建立项目(没有eclipe的东西) ,eclipse 中 import->liferay->liferay project from existing source,右击项目有liferay组(是插件新生成的),如果在project facades中取消了portlet,就没有办法再加上了
改回方法
.settings\org.eclipse.wst.common.project.facet.core.xml 中加   <installed facet="liferay.portlet" version="6.0"/>
.settings\中新加 org.eclipse.wst.common.project.facet.core.prefs.xml 文件
<root>
  <facet id="liferay.portlet">
    <node name="libprov">
      <attribute name="provider-id" value="com.liferay.ide.eclipse.plugin.portlet.libraryProvider"/>
    </node>
  </facet>
</root>


控制面板中可修改语言 
在 http://localhost:8080/ 中配置DB,语言,生成文件保存在 liferay-portal-6.2-ce-ga2/portal-setup-wizard.properties 
liferay-portal-6.2-ce-ga2\tomcat-7.0.42\lib  放 jdbc.jar
liferay-portal-6.2-ce-ga2\tomcat-7.0.42\webapps\ROOT\WEB-INF\classes  下建立 portal-ext.properties

# MySQL
jdbc.default.driverClassName=com.mysql.jdbc.Driver
jdbc.default.url=jdbc:mysql://localhost:3306/liferay62?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false
jdbc.default.username=liferay62
jdbc.default.password=liferay62
# Oracle
#jdbc.default.driverClassName=oracle.jdbc.driver.OracleDriver
#jdbc.default.url=jdbc:oracle:thin:@localhost:1521:xe
#jdbc.default.username=liferay62
#jdbc.default.password=liferay62

jdbc.default.maxIdleTime=600
jdbc.default.maxPoolSize=10
jdbc.default.minPoolSize=2

jdbc.default.jndi.name=jdbc/LiferayPool

建立了 180 个表 没有前缀

liferay-portal-6.2-ce-ga2\tomcat-7.0.42\webapps\中直接删自己不使用portlet目录,也可删  welcome-theme ,calendar-portlet  ,opensocial-portlet
eclipse 中修改liferay 的 -Xmx1024m 为 -Xmx512m

---/WEB-INF/liferay-hook.xml
<hook>
	<language-properties>Language_en_US.properties</language-properties>
	<language-properties>Language_zh_CN.properties</language-properties>
</hook>

---/WEB-INF/liferay-display.xml
<display>
	<category name="category.mysample"> <!-- 国际化Key -->
		<portlet id="portlet62_add" /><!-- 必须与 portlet.xml中的 <portlet-name>portlet62_add</portlet-name> 相同-->
	</category>
</display>
---/WEB-INF/liferay-portlet.xml
<liferay-portlet-app>
	<portlet>
		<portlet-name>portlet62_add</portlet-name> <!-- 必须与 portlet.xml中<portlet-name>portlet62_add</portlet-name> 相同- -->
		<icon>/icon.png</icon>
		<ajaxable>true</ajaxable>
		<instanceable>true</instanceable> <!-- 在一个页中,是可以多个portlet实例 -->
		<header-portlet-css>/css/main.css</header-portlet-css>
		<header-portlet-javascript>/js/jquery-1.7.2.min.js</header-portlet-javascript>
	</portlet>
</liferay-portlet-app>	





控制面板中 建立Site Template ,建立Page template
建立Site 基于Site Template,

界面中 Add(+)->Applications-> My Sample (是liferay-display.xml文件中配置的国际化) 标签下有自己的项目


liferay 62 不能在jsp中仿问session 
--------------------------------------------上 Liferay

--------------------------------------------ActiveMQ , Kafaka在hadoop中
<dependency>
	 <groupId>org.apache.activemq</groupId>
	 <artifactId>activemq-core</artifactId>
	 <version>5.7.0</version>
 </dependency>

ActiveMQ是一个JMS Provider的实现,tomcat 使用JMS 

JMeter做性能测试的文档
http://activemq.apache.org/jmeter-performance-tests.html

启动ActiveMQ服务器 bin\activemq.bat start  stop

http://localhost:8161/admin    admin/admin (配置在/conf/jetty-realm.properties)  可以看有,创建Queue ,Topic,Durable Topic Subscribers
http://localhost:8161/camel
http://localhost:8161/demo
端口配置在jetty.xml中

启动日志中有　tcp://lizhaojin:61616　    端口配置在activemq.xml中

日志中提示 JMX URL: service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi


看log4j.properties日志在data目录中

方法2（在JVM中嵌套启动）：
cd example

ant embedBroker

ant consumer
ant producer

ant topic-listener
ant topic-publisher



----集成web项目------启动OK
activemq-all-5.4.2.jar
activemq-web-5.4.2.jar

web.xml中
 <context-param>  
	<param-name>brokerURI</param-name>  
	<param-value>/WEB-INF/activemq.xml</param-value>  
 </context-param>  
 <listener>  
	<listener-class>org.apache.activemq.web.SpringBrokerContextListener</listener-class>  
 </listener>  


activemq.xml
 <beans  
   xmlns="http://www.springframework.org/schema/beans"  
   xmlns:amq="http://activemq.apache.org/schema/core"  
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
   xsi:schemaLocation="
   http://www.springframework.org/schema/beans 
   http://www.springframework.org/schema/beans/spring-beans-2.0.xsd  
   http://activemq.apache.org/schema/core 
   http://activemq.apache.org/schema/core/activemq-core-5.2.0.xsd     
   http://activemq.apache.org/camel/schema/spring 
   http://activemq.apache.org/camel/schema/spring/camel-spring.xsd">  
   
    <bean id="oracle-ds" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">  
       <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"/>  
       <property name="url" value="jdbc:oracle:thin:@localhost:1521:xe"/>  
       <property name="username" value="hr"/>  
       <property name="password" value="hr"/>  
       <property name="maxActive" value="20"/>  
       <property name="poolPreparedStatements" value="true"/>  
     </bean>  
   
     <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost">  
         
      
     </broker>  
   
 </beans>  

---集成Tomcat------------


<Resource
    name="jms/FailoverConnectionFactory"
    auth="Container"
    type="org.apache.activemq.ActiveMQConnectionFactory"
    description="JMS Connection Factory"
    factory="org.apache.activemq.jndi.JNDIReferenceFactory"
    brokerURL="failover:(tcp://localhost:61616)?initialReconnectDelay=100&amp;maxReconnectAttempts=5"
   brokerName="localhost"
    useEmbeddedBroker="false"/>

<Resource name="jms/topic/MyTopic"
    auth="Container"
    type="org.apache.activemq.command.ActiveMQTopic"
    factory="org.apache.activemq.jndi.JNDIReferenceFactory"
    physicalName="MY.TEST.FOO"/>
   
failover transport是一种重新连接机制，用于建立可靠的传输。
此处配置的是一旦ActiveMQ broker中断，Listener端将每隔100ms自动尝试连接，直至成功连接或重试5次连接失败为止。
 

---集成 Spring Tomcat------------OK
只 activemq-all-5.3.2.jar 放/WEB-INF/lib

Tomcat目录下的conf/context.xml

<Resource name="jms/ConnectionFactory"   
  auth="Container"     
  type="org.apache.activemq.ActiveMQConnectionFactory"   
  description="JMS Connection Factory"  
  factory="org.apache.activemq.jndi.JNDIReferenceFactory"   
  brokerURL="vm://localhost"   
  brokerName="LocalActiveMQBroker"/>  
   
<Resource name="jms/Queue"   
auth="Container"   
type="org.apache.activemq.command.ActiveMQQueue"  
description="my Queue"  
factory="org.apache.activemq.jndi.JNDIReferenceFactory"   
physicalName="FOO.BAR"/>  


spring.xml
 <bean id="jmsConnectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean">  
         <property name="jndiName" value="java:comp/env/jms/ConnectionFactory"></property>  
 </bean>  
 <bean id="jmsQueue" class="org.springframework.jndi.JndiObjectFactoryBean">  
	 <property name="jndiName" value="java:comp/env/jms/Queue"></property>  
 </bean>  
 <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">  
	 <property name="connectionFactory" ref="jmsConnectionFactory"></property>  
	 <property name="defaultDestination" ref="jmsQueue"></property>  
 </bean>  

 <bean id="sender" class="activemq_web.Sender">  
	 <property name="jmsTemplate" ref="jmsTemplate"></property>  
 </bean>  

 <bean id="receive" class="activemq_web.Receiver"></bean>  
 <bean id="listenerContainer"  class="org.springframework.jms.listener.DefaultMessageListenerContainer">  
	 <property name="connectionFactory" ref="jmsConnectionFactory"></property>  
	 <property name="destination" ref="jmsQueue"></property>  
	 <property name="messageListener" ref="receive"></property>  
 </bean>  
-----使用Spring标签
<jee:jndi-lookup id="jmsConnectionFactory" jndi-name="java:comp/env/jms/ConnectionFactory" />
<jee:jndi-lookup id="jmsQueue" jndi-name="java:comp/env/jms/Queue" />

<bean id="receive" class="activemq_web.ReceiverListener"></bean>
<jms:listener-container connection-factory="jmsConnectionFactory">
	<jms:listener destination="jmsQueue" ref="receive"/>
</jms:listener-container>
	
如使用ActiveMQ 
<bean id="jmsConnectionFactory" class="org.apache.activemq.pool.PooledConnectionFactory" destroy-method="stop">
    <property name="connectionFactory">
      <bean class="org.apache.activemq.ActiveMQConnectionFactory">
        <property name="brokerURL">
          <value>tcp://localhost:61616</value>
        </property>
      </bean>
	 
    </property>
  </bean>
  
<!--    也可以用  
    <bean id="jmsConnectionFactory2" class="org.springframework.jms.connection.SingleConnectionFactory">
        <property name="targetConnectionFactory" >
		    <bean  class="org.apache.activemq.ActiveMQConnectionFactory">
				<property name="brokerURL" value="tcp://localhost:61616" />
				<property name="userName" value="#{jms['mq.username']}" />
				<property name="password" value="#{jms['mq.password']}" />
				<property name="sendTimeout" value="10000" />  <!-- 如果不设置,会一直卡住好多个小时 -->
		    </bean>
        </property>
		
    </bean>
  -->  


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Sender
{
	private JmsTemplate jmsTemplate;
	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}
	public void send(final String text)
	{
		
		 
		System.out.println("---Send:" + text);
		jmsTemplate.send(new MessageCreator()
		{
			public Message createMessage(Session session) throws JMSException
			{
				return session.createTextMessage(text);
			}
		});
		
		 Map<String,Object> msg=new HashMap<String,Object> ();
		 msg.put("isSuccess", "true");
		 jmsTemplate.convertAndSend(msg);
		 
	}
}
//--
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.MapMessage;

public class Receiver implements MessageListener
{
	public void onMessage(Message message)
	{
		try
		{
			if (message instanceof TextMessage)
			{
				TextMessage text = (TextMessage) message;
				System.out.println("Receive:" + text.getText());
				
			}else if (message instanceof MapMessage)
			{
				MapMessage mapMsg=(MapMessage)message;
				System.out.println(" Receive Map Names is:"+ mapMsg.getMapNames()); 
			}
		} catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
}
ApplicationContext ctx = new ClassPathXmlApplicationContext("spring_jms_beans.xml");
JSP中
<%
ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletConfig().getServletContext());
Sender send=(Sender)ctx.getBean("sender");
send.send("hello");
%>
//------------------------------ OK
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
public class MainApp 
{
	public static void main(String[] args) throws Exception
	{
		// apache-activemq-5.11.1\bin\activemq.bat start 来启动
		//ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;  //failover://tcp://localhost:61616
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = factory.createConnection();
		connection.start();
		//在容器中,一个connection只能创建一个活的session,否则异常
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//boolean transacted, int acknowledgeMode　
		//对不在JTA事务中(如在JTA事务中,参数失效,commit,rollback,也失败,依赖于JTA事务),如transacted为true使用session.rollback();或 session.commit();   acknowledgeMode参数被忽略
		Topic topic= new ActiveMQTopic("testTopic");//动态建立 , 也可使用new ActiveMQQueue("testQueue")
		//Topic topic= session.createTopic("testTopic");
		// queue=session.createQueue("testQueue");
		MessageConsumer comsumer1 = session.createConsumer(topic);
		comsumer1.setMessageListener(new MessageListener()
		{
			public void onMessage(Message m) {
				try {
					System.out.println("Consumer1 get " + ((TextMessage)m).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//创建一个生产者，然后发送多个消息。
		MessageProducer producer = session.createProducer(topic);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		for(int i=0; i<10; i++)
		{
			producer.send(session.createTextMessage("Message:" + i));
		}
		producer.close();
	}
}
============ActiveMQ 的集群

	  
activemq5.9.0 开始 , activemq的集群实现方式取消了传统的Master-Slave方式 , 增加了基于zookeeper+leveldb的实现方式
http://activemq.apache.org/replicated-leveldb-store.html

activemq.xml
brokerName属性设置为统一的
<broker brokerName="broker" ... >
  ...
 <persistenceAdapter>
    <replicatedLevelDB
      directory="${activemq.data}/leveldb"
      replicas="3"
      bind="tcp://0.0.0.0:0"
      zkAddress="my-pc:2181,192.168.2.145:2181,192.168.2.146:2181"
      hostname="my-pc"
      sync="local_disk"
      zkPath="/activemq/leveldb-stores"
      />
</persistenceAdapter>
  ...
</broker>
hostname属性值配置本机的值
 
 
 
客户端使用
<bean class="org.apache.activemq.ActiveMQConnectionFactory">
	<property name="brokerURL">
	  <value>failover:(tcp://localhost:61616,tcp://otherIP:61616)</value>
	  <property name="userName" value="hrbb" />
	 <property name="password" value="hrbb" />
	</property>
</bean>

activemq.xml
如要设置用户名,密码,在 <systemUsage> 标签后加
<plugins> 
	<simpleAuthenticationPlugin>
		<users>
			<authenticationUser username="hrbb"  password="hrbb"  groups="users"/>
		</users>
	</simpleAuthenticationPlugin>
</plugins>



---------------------------------POI xls,xlsxWorkbook webbook = null;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//写
FileOutputStream out =null;
boolean is2007=true;//true,false
if(is2007)
{
	//要多加poi-ooxml-3.8.x.jar,apache项目xmlbeans的xbean.jar,poi-ooxml-schemas-3.8-x.jar
	//webbook=new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
	webbook=new XSSFWorkbook();
	out=new FileOutputStream("c:/temp/workbook.xlsx");
}
else
{
	out = new FileOutputStream("c:/temp/workbook.xls");
	webbook=new HSSFWorkbook();
}
Sheet sheet = webbook.createSheet();
webbook.setSheetName(0, "我的第一个Sheet");

int default_width=sheet.getColumnWidth(1);//default_width=2048
sheet.setColumnWidth(1, default_width*2);

Row row = sheet.createRow(0);
Cell cell0 = row.createCell(0);
cell0.setCellValue( 10000 );
Cell cell1 = row.createCell(1);
cell1.setCellValue( "中国我爱你" );
webbook.write(out);
out.close();

//读
InputStream inp =file.getInputStream();
Workbook wb = WorkbookFactory.create(inp);
Sheet sheet = wb.getSheetAt(0);
Iterator<Row> i=sheet.rowIterator();
while(i.hasNext())
{
	//Row row = sheet.getRow(i++);
	Row row=i.next();
	Cell cell = row.getCell(0);
	double msisdn=cell.getNumericCellValue();
}


-------jpeg
BufferedImage buffImg=new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_RGB);//可缩放
Graphics2D g=buffImg.createGraphics();
g.drawImage(...);
g.dispose();
//CODEC的全称=Coder and Decoder ,apache
---------------------------------Log4j 1

－X号: X信息输出时左对齐；
   %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
   %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
   %r: 输出自应用启动到输出该log信息耗费的毫秒数
   %c: 输出日志信息所属的类目，通常就是所在类的全名
   %t: 输出产生该日志事件的线程名
   %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
   %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
   %%: 输出一个"%"字符
   %F: 输出日志消息产生时所在的文件名称
   %L: 输出代码中的行号
   %m: 输出代码中指定的消息,产生的日志具体信息
   %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
	 1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
	 2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
	 3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
	 4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。

log4j.rootLogger=warn,console
log4j.logger.apache_log4j=debug,console
log4j.additivity.apache_log4j=false


log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
#log4j.appender.console.Threshold=info
log4j.appender.console.layout.ConversionPattern=[OD]%-d{yyyy-MM-dd HH:mm:ss} [%c:%L] %m%n
log4j.appender.console.Encoding=UTF-8

log4j.appender.dailyRollingFile=org.apache.log4j.DailyRollingFileAppender
log4j.appender.dailyRollingFile.file=${log_home}/dailyRollingFile.log
log4j.appender.dailyRollingFile.DatePattern='.'yyyy-MM-dd

log4j.appender.rollingFile=org.apache.log4j.RollingFileAppender
log4j.appender.rollingFile.File=${log_home}/rollingFile.log
log4j.appender.rollingFile.Append=true
log4j.appender.rollingFile.MaxFileSize=20MB
log4j.appender.rollingFile.MaxBackupIndex=10


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">   
    <log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/' >   
        
        <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">   
            <layout class="org.apache.log4j.PatternLayout">   
                <param name="ConversionPattern"  value="[%d{MM-dd HH:mm:ss,SSS\} %-5p] [%t] %c{2\} - %m%n" />   
            </layout>   
        </appender>   
        <appender name="my_appender" class="org.apache.log4j.DailyRollingFileAppender">   
            <param name="File" value="${log_home}/log_xml.txt" />   
            <param name="DatePattern" value="'.'yyyy-MM-dd'.log'" />   
            <layout class="org.apache.log4j.PatternLayout">   
                <param name="ConversionPattern" value="[%d{MM-dd HH:mm:ss SSS\} %-5p] [%t] %c{3\} - %m%n" />   
            </layout>   
        </appender>   
        <logger name="apache_log4j" additivity="false">   <!--name的值是包名-->  
            <level value="debug" />   
            <appender-ref ref="my_appender" />   
        </logger>   
        <root>   
            <priority value="DEBUG"/>
            <appender-ref ref="STDOUT"/>
            <appender-ref ref="my_appender"/>
        </root>   
    </log4j:configuration>   

文件位置使用变量${log_home}/log_xml.txt  //OK
log4j.appender.file.file=${log_home}/log_properties.txt   //OK

System.setProperty("log_home",servletContext.getRealPath("/"));//在lisener,或者自动启动的Servlet
//${user.dir}
java -Dlog_home=c:/temp

PropertyConfigurator.configure(properties);//对properites配置文件

//DOMConfigurator.configure(xmlFile);//对XML配置文件
URL url=this.getClass().getResource("/log4j.xml");
DOMConfigurator.configure(url);

Logger log = Logger.getLogger(My.class);
log.setLevel(Level.DEBUG);//动态修改日志级别,只是对某一个类
Logger rootLog=Logger.getRootLogger();//根

---------------------------------Log4j 2
 
log4j-api-2.1.jar
log4j-core-2.1.jar
log4j-slf4j-impl-2.1.jar  如用slf4j

classpath下名为 log4j2.xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration xmlns:xi="http://www.w3.org/2001/XInclude"
	status="WARN" monitorInterval="30" > <!-- monitorInterval 修改自动加载,单位是秒,这个配置最小5 -->
	<!-- 
	<Properties>
		<Property name="filename">c:/tomcat_log/${sys:os.name}/log4j2_app.log</Property> <!--  也可用  $${sys:os.name} -->
		<Property name="LOG_HOME">c:/tomcat_log</Property>
	</Properties>
	 -->
	<xi:include href="log4j2-xinclude-props.xml" />
	     
	<Appenders>
		<Console name="Console" target="SYSTEM_OUT">
			<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
		</Console>
		<File name="File" fileName="${filename}">
			<PatternLayout>
				<pattern>%d %p %C{1.} [%t] %m%n</pattern>
			</PatternLayout>
		</File>
		<RollingFile name="RollingFile" fileName="${LOG_HOME}/${sys:os.name}/${date:yyyy-MM}_rollFile.log"
			filePattern="${LOG_HOME}/archive/${date:yyyy-MM}/app-%d{yyyy-MM-dd}-%i.log.gz">  <!-- $${date:yyyy-MM} 可以一个或者两个 $  -->
			<PatternLayout>
				<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
			</PatternLayout>
			<Policies>
				<TimeBasedTriggeringPolicy />
				<SizeBasedTriggeringPolicy size="10 MB" /> <!-- 大小 好像不准??? -->
			</Policies>
		</RollingFile>
		<NoSql name="mongodbAppender"> <!--加 log4j-nosql-2.1.jar mongo-java-driver-2.13.0-rc1.jar  -->
			<MongoDb databaseName="log4j2" collectionName="MyApp"
				server="127.0.0.1" username="log4j2Admin" password="log4j2" />
		</NoSql>
	</Appenders>
	<Loggers>
		<Logger name="apache_log4j2" level="TRACE"  additivity="false">
			<AppenderRef ref="Console"/>
			<AppenderRef ref="RollingFile"/>
		</Logger>
		<Root level="error">
			<AppenderRef ref="Console" />
		</Root>
	</Loggers>
</Configuration>

//%d 默认格式 同    %d{yyyy-MM-dd HH:mm:ss,SSS}
//%t thread
// %c{1.}  或  %logger{1.}  包名只用一个字母
// %p 或 %level{WARN=W, DEBUG=D, ERROR=E, TRACE=T, INFO=I}    

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

//代码指定配置文件位置 方式一
String configFile=new File("./src/apache_log4j2/log4j2.xml").getAbsolutePath();
System.setProperty("log4j.configurationFile", "file://"+configFile);

		
//代码指定配置文件位置 方式二
ConfigurationSource source = new ConfigurationSource(TestLog4j2.class.getResourceAsStream("/apache_log4j2/log4j2.xml"));  
Configurator.initialize(null, source);
//但 xi:include 容易找不到,必须为<xi:include href="src/apache_log4j2/log4j2-xinclude-props.xml" />

Logger logger = LogManager.getLogger(TestLog4j2.class);


	

---------------------------------commons logging 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
Log logger = LogFactory.getLog(XXX.class);

---------------------------------SLF4J
替代 Spring 使用的 commons-logging 加 jcl-over-slf4j-1.7.6.jar

使用SLF4J(Simple Logging Facade for Java)做日志,为多种日志框架,默认是log4j
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory; 

Logger logger = LoggerFactory.getLogger(My.class); 
logger.info("Today is {}, Temperature set to {}", new Object[]{new java.util.Date(), 20});

logger.error("文件找不到",new FileNotFoundException("/test.txt"));//传入Throwable,会在日志有异常堆栈

---------------------------------logback 依赖于 SLF4J
可以把 org.springframework.context 简化为o.s.c   ,INFO 变为 I , 日志量变少

依赖 slf4j-api-1.7.6.jar , logback-core-1.1.2.jar  ,logback-classic-1.1.2.jar  直接实现了SLF4J API
如果用<if condition ,要 Janino 库 janino-2.7.5.jar 依赖 commons-compiler-2.7.5.jar


级别包括：TRACE、DEBUG、INFO、WARN和ERROR
如果logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别
XML配置的级别还可以是 ALL和OFF。还可以是一个特殊的字符串“INHERITED”或其同义词“NULL”，表示强制继承上级的级别

logger的 additivity 为false,L的某个祖先P (即P.L) 设置叠加性标识为false，那么，L的输出会发送给
L与P之间（含P）的所有appender，但不会发送给P的任何祖先的appender

pattern,%thread 线程名 ,%-5level级别,%logger{32}名称, %msg日志,%n 换行


classpath下 找 logback-test.xml 如果找不到 再找 logback.xml,也不存在使用 BasicConfigurator 
//java -Dlogback.configurationFile=/path/to/config.xml  默认classpath下的logback.xml
String currentDir=new File(".").getAbsolutePath();// X:/eclipse_java_workspace/J_JavaThirdLib/
System.setProperty("logback.configurationFile",currentDir+"/src/logback/logback.xml");//要使用绝对路径

ConsoleAppender 输出用模式为    %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n   的PatternLayoutEncoder进行格式化。还有，根logger默认级别是DEBUG

logback-1.1.2\logback-examples\src\main\java\chapters\configuration\sample0.xml

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

Logger logger = LoggerFactory .getLogger("my.biz");
Logger logger2 = LoggerFactory .getLogger("my.biz");
System.out.println(logger==logger2);//相同的对象

Object date = new java.util.Date(); 
logger.debug("today is: {} ", date);

//诊断与logback相关的问题时,MyApp2.java
LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
StatusPrinter.print(lc);//<configuration debug="true">时可打印详细信息

 <configuration scan="true" scanPeriod="30 seconds"> 如配置修改默认 每隔一分钟扫描一次
 
 
Logger rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);


//程序指定配置文件
JoranConfigurator configurator = new JoranConfigurator(); 
configurator.setContext(lc); 
lc.reset(); 
configurator.doConfigure(currentDir+"/src/logback/logback.xml");  
StatusPrinter.printInCaseOfErrorsOrWarnings(lc);


示例 onConsoleStatusListener.xml   中配置    <statusListener class="ch.qos.logback.core.status.OnConsoleStatusListener" />
示例 contextName.xml  中配置   <contextName>myAppName</contextName>  用于区分不同的项目  %contextName
示例 containingConfig.xml  格式
	<!-- <include file="src/logback/includedConfig.xml" />  -->
	<include resource="logback/includedConfig.xml" /> 内容以 <included>为根

示例 variableSubstitution3.xml
	
XML中配置中的变量 ${myName}形式,myName为系统属性 ,logback自带一个${HOSTNAME},
也可在XML中定义变量 <property name="LOG_HOME" value="/MyApp/logs" />
 
<!-- <property file="src/logback/windows.properties" /> 相对于 应用程序运行的当前目录 ,或者绝对目录,如在windows下如果路径/开头,则是C:下 -->  
<property file="/My/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/logback/windows.properties" />
<property resource="logback/app.properties" /> <!--  resource 是从classpath路径中找文件,做为变量 -->

<!-- 也可用于DEV,SIT,UTA 环境的切换  ,日志报要 Janino 库 janino-2.7.5.jar 依赖 commons-compiler-2.7.5.jar -->
<if condition='property("os.name").contains("Windows")'>  
	<then>
		<property file="C:/My/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/logback/windows.properties" />
	</then>
	<else>
		<property file="/eclipse_java_workspace/J_JavaThirdLib/src/logback/linux.properties" />
	</else>  
</if>

变量值可再引用变量   :- 表示变量如未赋值使用后的面的默认值 
${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao.log

<define name="APP_SERVICEOUT_HOME" class="logback.ServcieOutPropertyDefiner"> <!-- 变量值来自程序 -->
	 <logHome>${LOG_HOME}</logHome>
</define>
public class ServcieOutPropertyDefiner implements  PropertyDefiner {
	private String logHome;
	public void setLogHome(String logHome) {
		this.logHome = logHome;
	}
	//这个方法返回值,作为配置
	@Override
	public String getPropertyValue() {
		return logHome+"/performance/";
	}
	//...其它方法
}


logback-examples\src\main\java\chapters\appenders\conf\logback-sizeAndTime.xml
<appender name="bizRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
	<file>${APP_BIZ_HOME}/biz.log</file>
	<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
		<fileNamePattern>${APP_BIZ_HOME}/biz-%d{yyyy-MM-dd}.%i.zip</fileNamePattern> <!-- .zip 可有可无,压缩会节约20倍的空间 一定要加%i-->
		<maxHistory>30</maxHistory> <!-- 最多保留30个文件 (不是天,因可能一天多个文件) -->
		<TimeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
			<MaxFileSize>200MB</MaxFileSize> <!-- 如.zip 是压缩前的大小 -->
		</TimeBasedFileNamingAndTriggeringPolicy>
	</rollingPolicy>
	<encoder>
		<pattern>[%contextName] %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	</encoder>
</appender>

<logger name="${CONTEXT_NAME}.dao" level="INFO" additivity="false" >  <!-- Dao层的日志只写文件,不写其它的地方 -->
	<appender-ref ref="daoRolling" />
</logger>

示例 appenders/CountingConsoleAppender.java  自定义appender

全包名(最后一个.前的内容)
类名(最后一个.后的内容)
%logger  显示全包名.类名
%logger{0} 只显示类名
%logger{5} 包名.类名 缩短的长度,(包名.前至少1个字符,类名全部)

%.-1level  把INFO 变为  I


-------------------------------JSCH
jCraft的一个项目,是sftp实现

JSch jsch = new JSch();
jsch.getSession(username, host, port);
Session sshSession = jsch.getSession(username, host, port);

if(keyDir!=null)
	jsch.addIdentity(keyDir);//密钥验证,未测试
else
	sshSession.setPassword(password);

Properties sshConfig = new Properties();
sshConfig.put("StrictHostKeyChecking", "no");
sshSession.setConfig(sshConfig);
sshSession.connect();
Channel channel = sshSession.openChannel("sftp");
channel.connect();
ChannelSftp sftp = (ChannelSftp) channel;;

sftp.cd("directory");
sftp.put(new FileInputStream(file), "fileName");
sftp.get("downloadFile", new FileOutputStream(file));
sftp.rm("deleteFile");
sftp.ls("directory");
sftp.rename("/upload.txt.tmp", "/testDir/upload.txt");//同 linux mv 可重命名,也可移动文件


-------------------------------commons lang
implements Serializable{ 
    private static final long serialVersionUID = 1L;
	
public boolean equals(Object obj) 
{
	return  EqualsBuilder.reflectionEquals( this , obj);
		
//		if(this==obj)
//			return true;
//		if(! (obj instanceof Product)) //obj not null
//			return false;
//		Product p=(Product)obj;
//		return new EqualsBuilder().append(this.id ,p.id)
//		.append(this.name ,p.name)
//		.append(this.type ,p.type).isEquals();
}
public int hashCode() {
	 return  HashCodeBuilder.reflectionHashCode( this );
	//		 return new HashCodeBuilder()  
	//         .append(this.id)  
	//         .append(this.name)
	//         .append(this.type)
	//         .toHashCode();  
}
public String toString() {
		return ToStringBuilder.reflectionToString(this);
		//return ReflectionToStringBuilder.toString( this );
 //		return 	new  ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//		.append("id",this.id)
//		.append("name",this.name)
//		.append("type",this.type)
//		.toString();

}

org.apache.commons.lang.StringUtils  isBlank

-------------------------------commons logging
Log logger = LogFactory.getLog(getClass());

-------------------------------commons codec
---
import org.apache.commons.codec.digest.DigestUtils;

byte[] data="hello".getBytes(Charset.forName("UTF-8"));
byte[] sha1Byte=DigestUtils.sha1(data); 
String bytehexStr=MyByteArrayUtil.byteToHexString(sha1Byte);
String hexStr=DigestUtils.sha1Hex(data);
System.out.println(hexStr.equals(bytehexStr));

String sha1Hex=DigestUtils.sha1Hex(new FileInputStream(file));
System.out.println(sha1Hex);

String md5Hex=DigestUtils.md5Hex(new FileInputStream(file));
System.out.println(md5Hex);

---
import org.apache.commons.codec.binary.Base64;

byte[] data;
Base64 base64 = new Base64();
byte[] res= base64.encode(data);
res=base64.decode(data);

Base64.decodeBase64(data);

-------------------------------commons compress
//--tar.gz 解压
public static void decompressTarGzFile(String tarGzFile,String outDir) 
{
	ArchiveInputStream in = null;
	byte buffer[]= new byte[1024];
	try {
		 //GZIPInputStream is = new GZIPInputStream(new FileInputStream(tarGzFile));
		 //in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
		//二选一
		in =new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(tarGzFile), 1024)); 
		TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
		while (entry != null) 
		{
			String name = entry.getName();// 如果是目录以/结尾 ,手工建立目录
			if(name.endsWith("/"))
				new File(outDir+"/"+name).mkdir();
			else
			{
				FileOutputStream out = new FileOutputStream(outDir+"/"+name,false);//overwrite 
				int len=-1;
				while ((len = in.read(buffer)) != -1)
					out.write(buffer,0,len);
				out.flush();
				out.close();
			}
			entry = (TarArchiveEntry) in.getNextEntry();
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try { in.close();  } catch ( Exception e) { }
	}
}

//----tar.gz 压缩
org.apache.commons.compress.utils.IOUtils
org.apache.commons.io.IOUtils
 //只压缩一个文件 
protected static void compressOneTarGzFile(File srcFile, File destFile)
{  
	TarArchiveOutputStream out = null;  
	InputStream is = null;  
	try {  
		out = new TarArchiveOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(destFile)), 1024)); 
		
		is = new BufferedInputStream(new FileInputStream(srcFile), 1024);  
		TarArchiveEntry entry = new TarArchiveEntry(srcFile.getName());//这里不能传绝对目录的String,如果一个文件要getName(),不然会有上级目录名在包中
		entry.setSize(srcFile.length());  
		out.putArchiveEntry(entry);  
		IOUtils.copy(is, out);  
		out.closeArchiveEntry();  
	} catch(Exception ex)
	{
		ex.printStackTrace();
	}finally { 
		IOUtils.closeQuietly(is);  
		IOUtils.closeQuietly(out);  
	}  
}  
//递归压缩目录
public static void compressTarGzFromFileOrDir(String srcFile, String destFile)
{
	TarArchiveOutputStream out = null;  
	try {
		out = new TarArchiveOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(destFile)), 1024));
		compressWalkDir(new File(srcFile),out);
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}finally { 
		IOUtils.closeQuietly(out);  
	}  
}
private static void compressWalkDir(File from,TarArchiveOutputStream out) throws Exception
{
	if(from.isDirectory() )
	{
		TarArchiveEntry entry = new TarArchiveEntry(from);
		out.putArchiveEntry(entry);
		out.closeArchiveEntry();
		for(File fileItem:from.listFiles())
			compressWalkDir(fileItem,out);
	}else //file
	{
		InputStream  input = new BufferedInputStream(new FileInputStream(from), 1024);  
		TarArchiveEntry entry = new TarArchiveEntry(from);
		entry.setSize(from.length());  
		out.putArchiveEntry(entry);  
		IOUtils.copy(input, out); 
		out.closeArchiveEntry();
		IOUtils.closeQuietly(input);  
	}
}
 
while( org.apache.commons.compress.utils.IOUtils.readFully(socketInput, readBuffer) != 0 )//把socket 的输入流读满缓冲区才返回
 
IOUtils.LINE_SEPARATOR ,使用 PrintWriter的println实现

--------------------------------------Apache common net 的FTP 支持断点续传和中文文件 
//上传下载,测试OK	
//\commons-net-2.2-bin\apidocs\src-html\examples\ftp 下有HTML示例

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.PrintCommandListener;

FTPClient ftpClient = new FTPClient();
ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out))));
ftpClient.connect(hostname, port);
if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) 
{
	if (ftpClient.login(username, password)) 
		return true;
	
}else
	ftpClient.disconnect();
ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//二进制
ftpClient.enterLocalPassiveMode();//被动

ftpClient.sendCommand("mkdir test");
ftpClient.mkd("test");//就是mkdir
ftpClient.makeDirectory("test");
ftpClient.changeWorkingDirectory("..");

ftpClient.setRestartOffset(f.length());//断点,f已存在文件的长度
ftpClient.retrieveFile("remote.txt",new FileOutputStream(""));//下载

ins.skip(remoteSize); //ins InputStream
ftpClient.storeFile("newremote.txt",ins)//上传

tpClient.logout();
if (ftpClient.isConnected()) 
	ftpClient.disconnect();

ftpClient.setBufferSize(1000);


ftps =new FTPSClient("SSL");//与使用 FTPClient相同, 注意这里是ftps(ssl),vsftp支持SSL,不是sftp(ssh)

ANT 也有 ftp任务


-----------------------------文件上传 commons-fileupload--
request.setCharacterEncoding("UTF-8");


DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(20*1024*1024);//20M内存
factory.setRepository(new File("c:/temp"));//临时目录
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setSizeMax(200*1024*1024);//文件最大200M
List<FileItem> items=null;
try {
	items = upload.parseRequest(request);
} catch (FileUploadException e) 
{
	e.printStackTrace();
}
Iterator iter = items.iterator();
while (iter.hasNext())
{
	FileItem item = (FileItem) iter.next();

	if (item.isFormField())
	{	//有一个Filename=xxx的对
		String name = item.getFieldName();
		String value = item.getString();//浏览器对request.setCharacterEncoding("UTF-8");的设置无效
		System.out.println(name+"="+new String(value.getBytes("iso8859-1"),"UTF-8"));//对浏览器 OK
		System.out.println(name+"="+value);//对HttpClient UTF-8 OK
	} else {
			String fieldName = item.getFieldName();//form 的名字
			String pathName = item.getName();//只有IE 带C:/   浏览器可以和request.setCharacterEncoding("UTF-8");一起使用
			String correctName=pathName.substring(pathName.lastIndexOf(File.separator)+1);//浏览器对中文名OK
 			//String contentType = item.getContentType();
			//long sizeInBytes = item.getSize();

			//File uploadedFile = new File("d:/temp/"+correctName);//快速方式
			//item.write(uploadedFile);

			//为进度条方式
			FileOutputStream output=new FileOutputStream(new File("d:/temp/"+correctName));
			InputStream input = item.getInputStream();
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=input.read(buffer))!=-1 )
			{
				output.write(buffer,0,len);
			}
			output.close();
			input.close();
	}
}

//portlet 使用 org.apache.commons.fileupload.portlet.PortletFileUpload;
	
下载文件设置HTTP头,文件名
response.reset();
//response.setContentType("application/msexcel");
//response.setHeader("Content-disposition","inline;filename=workbook.xls");//定义文件名
//response.setContentType("application/x-msdownload");
 
response.addHeader("Content-Disposition","attachment; filename=workbook.xls");
response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));   
//text/plain,application/vnd.ms-excel(csv,xls),application/octet-stream(.xlsx)
response.getOutputStream();
	
<form method="post" action="xxx" enctype="multipart/form-data">

-----------------------------http client
public static void proxyTest() throws Exception //测试OK,注意看日志
{
	DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpHost targetHost = new HttpHost("cn.bing.com");
	//UsernamePasswordCredentials 	creds= new UsernamePasswordCredentials("APAC\476425", "password");//这个会报NTLM authentication error
	NTCredentials creds = new NTCredentials("476425", "password", "workstation", "APAC");
	httpclient.getCredentialsProvider().setCredentials(new AuthScope("172.52.17.184", 8080), creds);   
	HttpHost proxy = new HttpHost("172.52.17.184", 8080);//要设置两次IP
	httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy); 
	HttpGet httpget = new HttpGet("/");
	System.out.println("请求: " + httpget.getRequestLine());
	HttpResponse response = httpclient.execute(targetHost, httpget);
	HttpEntity entity = response.getEntity(); 
	// EntityUtils.toString(entity);
	System.out.println("响应状态:"+response.getStatusLine());
	// 显示结果
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
	String line = null;
	while ((line = reader.readLine()) != null) 
	{
	  System.out.println(line);
	}
	reader.close();
	httpClient.getConnectionManager().shutdown();
}


public static void postFormRequest(boolean isUpload) throws Exception //上传不支持中文
{
	DefaultHttpClient httpClient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(); //POST
	httppost.setHeader("user-agent","NX_Browser");//header中不能有中文
	if(isUpload)//要加 httpmime-4.2.1.jar
	{
		httppost.setURI(URI.create("http://127.0.0.1:8080/J_JavaEE/upload"));
		FileBody file = new FileBody(new File("c:/temp/中文名.txt"),"text/plain","UTF-8");//中文???????????   
		MultipartEntity reqEntity = new MultipartEntity(null,null,Charset.forName("UTF-8"));//显示在http头中
		//reqEntity.addPart("userFile", file);   
		reqEntity.addPart("comment", new StringBody("文件描述",Charset.forName("UTF-8")));//部分OK
		httppost.setEntity(reqEntity);
	}else
	{
		httppost.setURI(URI.create("http://127.0.0.1:8080/J_JavaEE/receiveForm"));
		//---方式1
//		    String encoded= URLEncoder.encode("李四","UTF-8");//不支持中文,要 URLEncoder.
//		    StringEntity reqEntity = new StringEntity("username="+encoded+"&password=123");
		//---方式2
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", "李四"));//支持中文
		formparams.add(new BasicNameValuePair("password", "value2"));
		UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		httppost.setEntity(reqEntity);
	}
	HttpResponse response = httpClient.execute(httppost);
	
	System.out.println("HttpClient到的请求头:");
	Header[] allHeader=  response.getAllHeaders();
	for (int i = 0; i < allHeader.length; i++) 
	{
		  System.out.println(allHeader[i].getName()+"="+allHeader[i].getValue());
	}
	List<Cookie> cookies = httpClient.getCookieStore().getCookies();//JSESSIONID,Session用
	
	HttpEntity respEntity = response.getEntity();
	System.out.println(response.getStatusLine());
	if (respEntity != null) {
	  System.out.println("Response content length: " + respEntity.getContentLength());
	}
  
	//System.out.println(EntityUtils.toString(respEntity));//和 respEntity.getContent()只可调用一个
	BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent(),"UTF-8"));
	//对应Server端response.getOutputStream().write("doPost的响应".getBytes("UTF-8"));
	String line = null;
	while ((line = reader.readLine()) != null)
	{
		System.out.println(line); 
	}
	reader.close();
	httpClient.getConnectionManager().shutdown();
}
public static void download() throws Exception
{
	DefaultHttpClient httpclient = new DefaultHttpClient();
	//HttpHost targetHost = new HttpHost("127.0.0.1",8080);
	HttpGet httpget = new HttpGet("http://127.0.0.1:8080/J_JavaEE/download");
	HttpResponse response =httpclient.execute(httpget);
	if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode())
	{
		
		String disposition= response.getHeaders("Content-Disposition")[0].getValue();//attachment; filename=xx
		String fileName=disposition.substring(disposition.indexOf('=')+1);
		String cnFileName=new String(fileName.getBytes("iso8859-1"),"GBK");
		
		HttpEntity entity = response.getEntity(); 
		System.out.println(entity.getContentType());
		InputStream input=entity.getContent();
		FileOutputStream output=new FileOutputStream(new File("c:/temp/"+cnFileName));
		byte[] buffer=new byte[1024];
		int len=0;
		while( (len=input.read(buffer)) != -1  )
		{
			output.write(buffer,0,len);
		}
		input.close();
		output.close();
	}
}
public static void async() throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,3000);//连接时间
		httpclient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,3000);//数据传送时间
		
		//HttpGet httpMehtod = new HttpGet("http://localhost:8080/J_JavaEE/");//POST
		HttpPost httpMehtod=new HttpPost("http://127.0.0.1:8080/J_JavaEE/receiveForm");
		ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>()
			{
			    public byte[] handleResponse(  HttpResponse response) throws ClientProtocolException, IOException 
			    {
			        HttpEntity entity = response.getEntity();
			        if (entity != null) {
			            return EntityUtils.toByteArray(entity);
			        } else {
			            return null;
			        }
			    }
			};
		byte[] response = httpclient.execute(httpMehtod, handler);
		System.out.println("返回="+new String(response,"UTF-8"));
		httpclient.getConnectionManager().shutdown();
	}
-----------------------------Quartz 2.1.x
public class MyQuartzJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    JobKey jobKey = context.getJobDetail().getKey();
	    String user_id=context.getJobDetail().getJobDataMap().getString("user_id");
		System.out.println("in MyQuartzJob ,key= "+jobKey+",user_id="+user_id);
	}
}
SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		JobDetail job = JobBuilder.newJob(MyQuartzJob.class)
			.withIdentity("myJob", "group1")
			.build();
  
		job.getJobDataMap().put("user_id", "zhangsan");
	
		
		//------------十一假期在要crond中除
		HolidayCalendar holiday=new HolidayCalendar();
		Calendar nationalDay= Calendar.getInstance();
		//nationalDay.set(2012,Calendar.OCTOBER, 1);
		nationalDay.set(2013,Calendar.JANUARY, 6);//只, 年月日 ,是有用的
		
		holiday.addExcludedDate(nationalDay.getTime());
		sched.addCalendar("nationalDay", holiday, true, false);// boolean replace, boolean updateTriggers
		//------------
		Trigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("myTrigger", "group1")
		//.startNow()
		.startAt(new java.util.Date(Calendar.getInstance().getTimeInMillis()+5000))
		
		.modifiedByCalendar("nationalDay") //设置假期
		
//		.withSchedule(SimpleScheduleBuilder.simpleSchedule()
//			.withIntervalInSeconds(3)
//			.withRepeatCount(10))
		      
		.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * 8-17 * * ?"))
/*
//cron表达式,空格分隔的顺序是
1.Seconds					0-59
2.Minutes					0-59
3.Hours						0-23
4.Day-of-Month				1-31
5.Month						between 0 and 11, or by using the strings JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV and DEC.
6.Day-of-Week				1 到 7 (1 = Sunday) ,也可用SUN, MON, TUE, WED, THU, FRI and SAT
7.Year (optional field)
-----
 '?' character is allowed for the day-of-month and day-of-week fields  ,"no specific value"
  'L' character is allowed for the day-of-month and day-of-week fields ,"last"
 		in the day-of-week field by itself, it simply means "7" or "SAT"
 		in the day-of-week field "6L" or "FRIL" both mean "the last friday of the month"
  '3/20' in the Minutes field, it would mean 'every 20th minute of the hour, starting at minute three' - or in other words it is the same as specifying '3,23,43' in the Minutes field
  "6#3" or "FRI#3" in the day-of-week field means "the third Friday of the month".
*/	  
			.build();
	
		  sched.scheduleJob(job, trigger);

//-------使用配置文件方式		  
//org/quartz/quartz.properties文件,可以被src\下的文件覆盖, 示例在quartz-2.1.6\examples\example10\quartz.properties
//org/quartz/xml/job_scheduleing_data_2_0.xsd
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //会读classpath 下的quartz.properties
scheduler.start();  //quartz.properties中配置去读 quartz_data.xml
scheduler.shutdown(); 

org.quartz.impl.jdbcjobstore.JobStoreCMT containerManageTransaction;
org.quartz.impl.jdbcjobstore.oracle.OracleDelegate oracle;


//--- quartz.properties 文件(放在类路径下) 
#如使用了Spring不会默认读 classpath下的quartz.properties , 要配置quartzProperties属性
#线程数配置 
org.quartz.threadPool.threadCount=3

org/quartz/quartz.properties 中的值是是以:分隔 ,也可以的
org.quartz.threadPool.threadCount: 10

动态配置cronExpression ,类	extends CronTriggerBean
	setCronExpression(cronExpression)

---------------------------------Mina 
Apache 项目基于java nio

sample中的 gettingstarted　是服务端

事件驱动
Handler 中处理响应事件
Filter,FilterChain //日志，压缩，数据转换，黑名单

Service
	Connector  客户
	Acceptor 服务


//服务端
IoAcceptor acceptor = new NioSocketAcceptor(); //UDP 用 NioDatagramAcceptor

acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
//acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
//acceptor.getFilterChain().addLast(  "codec", new ProtocolCodecFilter(  new SumUpProtocolCodecFactory(true)));//是自定义类  extends DemuxingProtocolCodecFactory 
 
acceptor.setHandler( new TimeServerHandler() );//自己的回调
acceptor.getSessionConfig().setReadBufferSize( 2048 );
acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );//空闲10秒后调用Handler sessionIdle方法 
acceptor.bind( new InetSocketAddress(9123));


//客户端
NioSocketConnector connector = new NioSocketConnector();// UDP 用 NioDatagramConnector
 connector.setConnectTimeoutMillis(3000);
   
//connector.getFilterChain().addLast("black",  new BlacklistFilter());
connector.getFilterChain().addLast("logger", new LoggingFilter());
//connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
//connector.getFilterChain().addLast(  "codec", new ProtocolCodecFilter(  new SumUpProtocolCodecFactory(false)));//是自定义类  extends DemuxingProtocolCodecFactory 
 
connector.setHandler(new ClientSessionHandler());//自己的回调
  
ConnectFuture future = connector.connect(new InetSocketAddress(9123));
future.addListener( new IoFutureListener<ConnectFuture>()
		{
            public void operationComplete(ConnectFuture future) //会第一次执行
            {
                if( future.isConnected() )
                {
                	IoSession session =future.getSession();
                    IoBuffer buffer = IoBuffer.allocate(8);
                    //buffer.putLong(85);
                    buffer.putChar('L');
                    buffer.putChar('E');
                    buffer.putChar('N');
                    buffer.flip();
                    session.write(buffer);//另一端不会立即收到请求，要在外部的session.write才写
                } else {
                   System.out.println("Not connected...exiting");
                }
            }
        });
future.awaitUninterruptibly();
IoSession session = future.getSession();
session.setAttribute(sessionCountKey,0);//session只可在自己这一端可以仿问
WriteFuture write= session.write("客户端第二次写Header");//这里才开始写ConnectFuture中 Listener的session.write 再和这里 一起到服务端的
write.addListener(new IoFutureListener<IoFuture>() 
{
	@Override
	public void operationComplete(IoFuture fure) {
		System.out.println("客户端第二次写Header完成");
	}
}) ;
 
session.getCloseFuture().awaitUninterruptibly();//阻， 另一端关闭时这里关闭
connector.dispose();

class ClientSessionHandler extends IoHandlerAdapter
{
	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	{
	    cause.printStackTrace();
	}
	public void messageReceived( IoSession session, Object message ) throws Exception
	{
	    int count=Integer.parseInt(session.getAttribute(MinaClient.sessionCountKey).toString());
		session.setAttribute(MinaClient.sessionCountKey , ++count);
		if(count > 3)
		{
			session.write("quit");
			return;
		}else
		{
			 String str = message.toString();
		    System.out.println("客户端 receive is:"+str);
		    session.write("hello 你好！");
		    System.out.println("客户端已经写了hello");
		}
	}
	public void messageSent(IoSession session, Object message) throws Exception {//调用了write方法调用这个,不一定发送
		 System.out.println("messageSent: session="+session.getId()+",message="+message);
	}
	public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	{
	    System.out.println( "IDLE " + session.getIdleCount( status ));
	}
}
public class SumUpProtocolCodecFactory extends DemuxingProtocolCodecFactory {
   public SumUpProtocolCodecFactory(boolean server) 
   {
	   if (server) {
		  super.addMessageDecoder(AddMessageDecoder.class);//implements MessageDecoder
	      super.addMessageEncoder(ResultMessage.class, ResultMessageEncoder.class);//自己的类T Serializable , implements MessageEncoder<T>
	  }
   }
}

---------------------------------Netty
JBoss的 NIO  很少类基于 java nio
NioServerSocketChannel , NioSocketChannel 用了 java nio
//---- Netty-4.1
netty-all-4.1.8.Final-sources.jar 里有example
 

---------------------------------JSON
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

JSONArray array = new JSONArray();
		
JSONObject obj = new JSONObject();
obj.put("id", 100);
obj.put("username",URLEncoder.encode("李","UTF-8"));
obj.put("password", 123);
array.add(obj);

JSONObject obj1 = new JSONObject();
obj1.put("id", 101);
obj1.put("username",true);
obj1.put("password", 123);
array.add(obj1);
System.out.println(array.toString());


JSONObject jsonObject = JSONObject.fromObject(ua);
System.out.println("java Object to json : "+ jsonObject); 

JSONArray jsonArrasy = JSONArray.fromObject(ua);
System.out.println("java Array to json : "+ jsonArrasy); 


-------------------------------DisplayTag 表格 分页
web.xml

<!--  
<filter>
	<filter-name>ResponseOverrideFilter</filter-name>
	<filter-class>org.displaytag.filter.ResponseOverrideFilter</filter-class>
</filter>

<filter-mapping>
	<filter-name>ResponseOverrideFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<filter-mapping>
	<filter-name>ResponseOverrideFilter</filter-name>
	<url-pattern>*.jsp</url-pattern>
</filter-mapping>
-->
 

 

id="codeTable 第一个作用是每个List的子对象的名，第二个生成<table id="">
 

<display:table name="blacklist" id="blacklistTable" defaultsort="1"   partialList="true" requestURI="/pages/sysInfoMgmt/codeMgmt/blacklist.do?method=query"
  size="resultSize" pagesize="${PAGE_SIZE}" >
	  <display:column property="number" title="${phoneNO}" style="text-align:center"  sortable="true" group="1"/>


加一个 group="1" 表示对第一列 不显示重复的 
sortable="true" 可排序的

<display:table defaultsort="1"  对第一列默认是排序的

有个问题是多次分页后 requestURI 后的参数会多次的重复




<display:table name="strategyList" requestURI="/pages/Notice/queryStrategy.do" pagesize="5" uid="al">
${al.strategeID}
也可以使用uid


uid 最好不要用,在和id一起使用没有办法得到正确的页号,编码参数错误
 

-----------classpath下的 displaytag_zh.properties  可以参考 org.displaytag.properties.displaytag.properties
basic.empty.showtable=true
basic.show.header=true
basic.msg.empty_list=没有可以显示的数据 
basic.msg.empty_list_row=<tr   align="center" class="empty"><td colspan="{0}">没有可以显示的数据</td></tr> 
paging.banner.onepage=<span class="pagelinks">[第一页/前一页] 									{0} 					[下一页/最后一页 ] 					</span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条	&nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.first=<span class="pagelinks">[第一页/前一页] 									{0} [ <a href="{3}">下一页</a>/ <a href="{4}">最后一页</a>] </span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.last=<span class="pagelinks">[<a href="{1}">第一页</a>/ <a href="{2}">前一页</a>] {0} 					[下一页/最后一页] 					</span>	&nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页<input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.full=<span class="pagelinks">[<a href="{1}">第一页</a>/ <a href="{2}">前一页</a>] {0} [ <a href="{3}">下一页</a>/ <a href="{4}">最后一页 </a>]</span> &nbsp; 每页显示<select id="idpagesize" onchange="javascript:dealfoward();"><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option></select>条 &nbsp;	到第 <input id="tz" class="pageTxt" size="3" type="text" value="{5}"/>/{6}页 <input class="searchButton" type="button" onclick="javascript:displaytagURL();" value="跳转"/><input id="hd" type="hidden" value="{1}"/>
paging.banner.placement = bottom
paging.banner.group_size=5
    
paging.banner.item_name=记录  
paging.banner.items_name=记录 
paging.banner.page.selected=当前第<strong>{0}</strong>页
paging.banner.no_items_found=<span   class="pagebanner">没找到{0}   .</span>   
paging.banner.one_item_found=<span   class="pagebanner">共找到1条{0},当前显示所有的{0}   .</span>   
paging.banner.all_items_found=<span   class="pagebanner">共找到{0}条{1},当前显示所有的{2}.</span> 
paging.banner.some_items_found=<span   class="pagebanner">共找到{0}条{1},当前显示{2}到{3}条.</span>   

export.banner=<div   class="exportlinks">数据:   {0}</div>   
export.banner.sepchar=|     
export.types=csv excel xml pdf
export.excel=true
export.csv=true
export.xml=true
export.pdf=true
export.excel.label=my export excel
export.excel.filename=the_exported_excel.xls
#export.excel.include_header=true
#export.excel.class=display_tag.ExcelView
#export.pdf.class=display_tag.PdfView


#locale.resolver=org.displaytag.localization.I18nStrutsAdapter
#locale.provider=org.displaytag.localization.I18nStrutsAdapter


项group_size 表示多页时中间最多8个链接页,数字后不要有空格

<display:table export="true"
	
	....
	<display:setProperty name="export.pdf" value="true" />
</display:table>





分页新加　每页x条，到x页
-----JS 
function displaytagURL() {  //for go page
	var reg = /-p=\d{0,}/;
	var url=document.getElementById("hd").value.replace(reg,"-p=" + document.getElementById("tz").value);
	window.location=url;
}

function loadSelect(inSize)
{
	var pagesizeSel = document.getElementById("idpagesize");
	for (var i=0; i<pagesizeSel.options.length; i++) 
	{
		if( pagesizeSel.options[i].value == inSize )
		{
			pagesizeSel.options[i].selected="selected";
			return ;
		}
	}
}
function dealfoward() //for change page size
{
	var value = document.getElementById("idpagesize").value;
	if(document.getElementById("hd"))
	{	
		var url=document.getElementById("hd").value.replace(/-p=\d{0,}/,"-p=1");
		if( /\&pageSize=/.test(url) )
			location.href = url.replace(/\&pageSize=\d{0,}/, "&pageSize=" + value);
		else
			location.href = url+"&pageSize=" + value;
	}else
	{
		//for one page show all the record
		var url = window.location.href;  //<FORM method="GET"
		//debugger;
		if( /\&pageSize=/.test(url) )
			url = url.replace(/\&pageSize=\d{0,}/, "&pageSize=" + value);
		else
			url = url+"&pageSize=" + value;
		
		if(/-p=\d{0,}/.test(url))
			url = url.replace(/-p=\d{0,}/,"-p=1");
		else
			url+="&-p=1";
		
		window.location.href=url;
	}
}


<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
 
<display:table name="myList" id="myTable"  partialList="true" requestURI="/tablePageServlet.ser?action=query"
	size="resultSize" pagesize="${sessionScope.SESSION_PAGE_SIZE}" >
	  <display:column title="${title_name}" property="name" style="text-align:center"  sortable="true" group="1"/>
	  <display:column title="日期" property="date" />
	  <display:column title="日期" > ${myTable.date}  </display:column>
	   
</display:table>

如去 partialList="true" 多加  commons-collections-3.2.1.jar
URL 总是有重复的,应该和requestURI中有参数的原因

<script type="text/javascript">
	loadSelect(${sessionScope.SESSION_PAGE_SIZE});
</script>

-----Java
@WebServlet("/tablePageServlet.ser")
public class TablePageServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println(request.getParameter("action"));//总是init
	 	int pageNo=getPageNO(request,"myTable");
		int pageSize=getSessionPageSize(request);
		List data=generateData(pageNo,pageSize);
		request.setAttribute("myList", data);
		request.setAttribute("resultSize",data.size());
		request.setAttribute("title_name","姓名");
		request.getRequestDispatcher("display_tag.jsp").forward(request, response);
	}
	public List  generateData(int pageNo,int pageSize)
	{
		List dataList=new ArrayList();
		for(int i=pageNo;i<pageNo + pageSize + 3 ;i++)
		{
			VO vo=new VO();
			vo.setName("名"+i);
			vo.setDate("2013年");
			dataList.add(vo);
		}
		return dataList;
	}
	//---放入基类中
	protected int  getPageNO(HttpServletRequest request,String tableId)
	{
		int pageNo=1;
		String name = new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE);//服务端接收传来的页号
		if(request.getParameter(name)!=null)
		{
			try{
				pageNo = Integer.parseInt(request.getParameter(name));//display tag 中分页按钮请求才有
			}catch(NumberFormatException e)
			{
				pageNo=1;
			}
		}
		return pageNo;
	}
	protected  int  getSessionPageSize(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		int pageSize=Constant.DEFAULT_PAGE_SIZE;
	    String reqSize=request.getParameter("pageSize");
	    if(reqSize!=null && ! "".equals(reqSize))
	    {
	    	pageSize=Integer.parseInt(reqSize.toString());
	    	session.setAttribute(Constant.SESSION_PAGE_SIZE,pageSize);
	    }else
	    {
	    	 Object sessionPage =session.getAttribute(Constant.SESSION_PAGE_SIZE);
	 	    if(sessionPage!=null)
	 	    	pageSize=Integer.parseInt(sessionPage.toString());
	 	    else
	 	    	session.setAttribute(Constant.SESSION_PAGE_SIZE,pageSize);
	    }
	    return pageSize;
	}
}

 
------全部数据的排序
新排序功能的实现方法：
<display:table  sort="external" defaultsort="1" defaultorder="descending"
	表示使用displaytag的外部排序功能，默认对第一列降序排列显示
	<display:column  sortable="true"   如加 sortName="xx" 就要在display:table中加 sort="external"
 
// 获取外部排序列 
String strSortName = new ParamEncoder("myTable").encodeParameterName(TableTagParameters.PARAMETER_SORT);
String sortName = request.getParameter(strSortName);
String strOrder = new ParamEncoder("myTable").encodeParameterName(TableTagParameters.PARAMETER_ORDER);
String order = request.getParameter(strOrder);//order为升序还是降序(1为升序  2为降序)
String dbOrder="";
if("1".equals(order))
	dbOrder="asc";
else if("2".equals(order))
	dbOrder="desc";
 
---------
因为displaytag和struts2一起使用导致的，由于displaytag生成的参数中带“-”，而struts2中接受的参数中默认又不允许有“-”，
只要将，devMode设置为false就不会报这个错了，这个的确可以解决该问题。


根据当前页的数据 List .size()是否为0 ,解决删除当前页所有的项,翻页的Bug


----------SVNANT是subeclipse项目,在ANT中使用SVN
---------------------------------Apache  Continuum   1.4.1 
apache-continuum-1.4.1\bin\continuum.bat console  启动 
http://127.0.0.1:8080/continuum/

continuum.bat install/remove 以管理员运行,安装为windows服务名为 "Apache Continuum"

$CONTINUUM_HOME/contexts/continuum.xml 配置SMTP,DB,可JNDI
可以安装到Tomcat  中做为一个项目,下载apache-continuum-1.4.1.war
但要有以下3个JNDI
mail/Session
jdbc/continuum
jdbc/users


<Context path="/continuum" docBase="/path/to/continuum-webapp-1.4.1.war">

  <Resource name="jdbc/users"
            auth="Container"
            type="javax.sql.DataSource"
            username="sa"
            password=""
            driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
            url="jdbc:derby:database/users;create=true" />

  <Resource name="jdbc/continuum"
            auth="Container"
            type="javax.sql.DataSource"
            username="sa"
            password=""
            driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
            url="jdbc:derby:database/continuum;create=true" />

  <Resource name="mail/Session"
            auth="Container"
            type="javax.mail.Session"
            mail.smtp.host="localhost"/>
</Context>


配置${appserver.base} 
-Dappserver.base=/path/to/continuum-base

如是Tomcat中加
  (export)set CATALINA_OPTS="-Dappserver.home=$CATALINA_HOME -Dappserver.base=$CATALINA_HOME"


---------------------------------zxing  
http://zxing.github.io/zxing/apidocs/allclasses-noframe.html

下载zxing-master解压后
mvn install 后生成 zxing-master\core\target\core-3.0.0-SNAPSHOT.jar
				  zxing-master\javase\target\javase-3.0.0-SNAPSHOT.jar
都有javadoc和source

//--生成 二维码
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

 public static void writeQR()
 {
	final int BLACK = 0xFF000000;
	final int WHITE = 0xFFFFFFFF;
	String content = "http://www.baidu.com";
	String fileName = "C:/test_QR.jpg";

	MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
	Map<EncodeHintType,String> hints = new HashMap<EncodeHintType,String>();
	hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	BitMatrix matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
	File file = new File(fileName);

	int width = matrix.getWidth();
	int height = matrix.getHeight();
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	for (int x = 0; x < width; x++) 
	{
		for (int y = 0; y < height; y++) 
		{
		 image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
		}
	}
	ImageIO.write(image, "jpg", file) ;
}


//--Java 解析二维码
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

MultiFormatReader formatReader = new MultiFormatReader();
String filePath = "C:/test_QR.jpg";
File file = new File(filePath);
BufferedImage image = ImageIO.read(file);
LuminanceSource source = new BufferedImageLuminanceSource(image);
Binarizer binarizer = new HybridBinarizer(source); //Hybrid 混合
BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
Map hints = new HashMap();
hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
Result result = formatReader.decode(binaryBitmap, hints);

System.out.println("resultFormat = " + result.getBarcodeFormat());
System.out.println("resultText = " + result.getText());

//--生成条形码
import java.nio.file.Paths;

 String imgPath = "c:/line.png";  
 String contents = "6923450657713";  // 益达无糖口香糖的条形码  
 
 int width = 105, height = 50;  
 int codeWidth = 3 + // start guard  
		 (7 * 6) + // left bars  
		 5 + // middle guard  
		 (7 * 6) + // right bars  
		 3; // end guard  
 codeWidth = Math.max(codeWidth, width);  
 BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,BarcodeFormat.EAN_13, codeWidth, height, null);
 
MatrixToImageWriter.writeToPath(bitMatrix, "png",Paths.get(new URI(imgPath)));
 
//---Java 解析条形码
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
String imgPath = "c:/line.png";  

BufferedImage  image = ImageIO.read(new File(imgPath));
LuminanceSource source = new BufferedImageLuminanceSource(image);
BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

Result result = new MultiFormatReader().decode(bitmap, null);
System.out.println(result.getText());  
==================图片识别
------------验证码识别
OCR  (Optical Character Recognition , 光学字符识别)  图片转文字
----asprise-ocr-java-5.01 ,windwos trail版本 (只可英文和数字)

	demoText.bat 示例内容  java -jar aocr.jar img/test.png text  测试过可以解析文字(路径不要有中文)

javadoc中的示例
	 //如果旋转一下图片中的字母就不行了???,像360是如何识别 12306的验证码???
	 //测试 OK ,windows asprise-ocr-java-5.01/aocr.jar大小62.5M
	import com.asprise.ocr.Ocr;
	 Ocr.setUp(); // one time setup
	 Ocr ocr = new Ocr();
	 ocr.startEngine("eng", Ocr.SPEED_FASTEST);
	 String s = ocr.recognize(new File[] {new File("test.jpg")}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT, 0, null);
	 System.out.println("RESULT: " + s);
	 // do more recognition here ...
	 ocr.stopEngine();

 
----tesseract ocr (支持中文)
code.google.com/p/tesseract-ocr
https://github.com/tesseract-ocr/tesseract


------------指纹识别  人脸识别
SURF (Speeded Up Robust Features, 加速稳健特征) 实现

openCV , Android人脸识别
openSURF  基于 OpenCV

---------------------------------Ehcache 2.9
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


URL url = MainEhCache.class.getResource("/ehcache/ehcache.xml");
CacheManager manager = CacheManager.newInstance(url);
//--------
Cache cache = manager.getCache("onlyMemoeryCache");
Element element = new Element("key1", "value1");
cache.put(element);//新建和更新


Element valElement = cache.get("key1");
Object value = valElement.getObjectValue();

int elementsInMemory = cache.getSize();//已经使用的内存大小
cache.remove("key1");

//-----
manager.shutdown();
CacheManager.getInstance().shutdown();

----ehcache.xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd" updateCheck="true"
         monitoring="autodetect" dynamicConfig="true">
		 
     <cache name="onlyMemoeryCache" maxElementsInMemory="100000"
           timeToLiveSeconds="1200" timeToIdleSeconds="1200" eternal="false"
           overflowToDisk="false" memoryStoreEvictionPolicy="LFU">
    </cache>
</ehcache>

清内存机制  Least(最少)  Recently (最近)
Least Recently   Used (LRU)  默认			淘汰最长时间未被使用的
Least Frequently Used (LFU)					淘汰一定时期内被访问次数最少的
First In First Out (FIFO)

-----Spring
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	<property name="configLocation">
		<value>classpath:cache/ehcache.xml</value>
	</property>
</bean>

<bean id="ehCache" class="org.springframework.cache.ehcache.EhCacheFactoryBean">
	<property name="cacheManager">
		<ref local="cacheManager"/>
	</property>
	<property name="cacheName">
		<value>onlyMemoeryCache</value>
	</property>
</bean>

import net.sf.ehcache.Ehcache;

ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("ehcache/applicationContext-cache.xml");
Ehcache  ehcache=ctx.getBean("ehCache",Ehcache.class);
Element element = new Element("key1", "value1");
ehcache.put(element);
Element valElement = ehcache.get("key1");
Object value = valElement.getObjectValue();

---------------------------------Memcache Java Client --- alisoft 阿里巴巴

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.MemcacheStatsSlab;
import com.alisoft.xplatform.asf.cache.memcached.MemcacheStatsSlab.Slab;

ICacheManager<IMemcachedCache>  manager = CacheUtil.getCacheManager(IMemcachedCache.class,MemcachedCacheManager.class.getName());
manager.setConfigFile("memcache_client/memcached1.xml");
manager.setResponseStatInterval(5*1000);//设置Cache响应统计间隔时间，不设置则不进行统计
manager.start();
IMemcachedCache cache = manager.getCache("mclient0");//配置文件中的,如不存在,返回null
cache.clear();// 调用后要sleep一会
cache.put("key1", "value1");
Set<String> keys = cache.keySet(false);
cache.remove("key1");
cache.storeCounter("counter", 20);
cache.incr("counter", 11);//减decr,
cache.addOrIncr("counter", 20);//没有值设置为20,有值加上20,相应的有 addOrDecr,原子操作


MemcacheStatsSlab[] result = cache.statsSlabs();
MemcacheStatsSlab node = result[i];
String hostAndPort=node.getServerHost();
Map<String,Slab> slabs = node.getSlabs();//有分配空间,命中率信息


MemcacheStats[] result = cache.stats();
MemcacheStats node = result[i];
node.getStatInfo()

cache.setStatisticsInterval(30);
MemcachedResponse response = cache.statCacheResponse();
response.getCacheName();//mclient0

cache.replace("key1", "value1")

cache.asynPut("key1", "value1");
cache.asynStoreCounter("key1", 100); //很多的asyncXxx 方法

cache.put("key1", "value1",calendar.getTime());//保存过期时间

manager.reload("memcache_client/memcached_cluster2.xml");//重新加载配置,cache client需要重新获取对象,服务端的数据不会删除


manager.stop();
----memcached1.xml
<memcached>
    <client name="mclient0" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool0">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>
	<socketpool name="pool0" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12000</servers>
    </socketpool> 
</memcached>

maintSleep 属性是后台线程管理SocketIO池的检查间隔时间，如果设置为0，则表明不需要后台线程维护SocketIO线程池，默认需要管理
socketTO 属性是Socket操作超时配置，单位ms
aliveCheck 属性表示在使用Socket以前是否先检查Socket状态
 

			
---分布式
<memcached>
    <client name="mclient" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool0">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>
    <client name="mclient1" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool1">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>
    <client name="mclient2" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool2">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>   
    <client name="mclient3" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool3">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>
    <client name="mclient4" compressEnable="true" defaultEncoding="UTF-8" socketpool="pool4">
        <errorHandler>com.alisoft.xplatform.asf.cache.memcached.MemcachedErrorHandler</errorHandler>
    </client>   
    
    <socketpool name="pool0" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12000</servers>
    </socketpool> 
    <socketpool name="pool1" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12001</servers>
    </socketpool> 
    <socketpool name="pool2" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12002</servers>
    </socketpool> 
    <socketpool name="pool3" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12003</servers>
    </socketpool> 
    <socketpool name="pool4" failover="true" initConn="5" minConn="5" maxConn="250" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>192.168.0.184:12004</servers>
    </socketpool>  
    
    <cluster name="cluster1" mode="active"> //mode = active,standby
        <memCachedClients>mclient1,mclient2</memCachedClients>
    </cluster>
    <cluster name="cluster2" mode="standby">  //mode = active,standby
        <memCachedClients>mclient3,mclient4</memCachedClients>
    </cluster>
</memcached>


manager.clusterCopy("mclient", "cluster1"); //从 mclient 复制 到  cluster1

---------------------------------Memcached Java Client--spymemcached
MemcachedClient c=new MemcachedClient( AddrUtil.getAddresses("192.168.0.184:12000"));
MemcachedClient mc = new MemcachedClient(new InetSocketAddress("192.168.0.184", 12000));  
Future<Boolean> theSetFuture = mc.set("myKey1", 900, "someObject");//key,timeout,value

if(theSetFuture.get().booleanValue()==true)
{  
	Future<Object> theGetFuture = mc.asyncGet("myKey1");
	Object obj=theGetFuture.get();
	 
	Future<Boolean> f = mc.replace("myKey1", 500, "MyValue1");  
	
	Collection<String> keys=new ArrayList<>();
	keys.add("myKey1");
	Map<String, Object> myBuilks=mc.getBulk(keys);
	
	Future<Map<String, Object>> theFutureBulk = mc.asyncGetBulk(keys);  
	Map<String, Object>   map = theFutureBulk.get(3,TimeUnit.SECONDS);
	
	 //del
	 Future<Boolean> theDelFuture = mc.delete("myKey1");
	 if(theDelFuture.get().booleanValue()==true)
	 {
		 theGetFuture = mc.asyncGet("myKey1");
		 obj=theGetFuture.get();
	 }
}
mc.delete("myAtomicNum");
Thread.sleep(200);
//Future<Boolean> numFuture = mc.add("myAtomicNum", 500, 20);//add 如已存在,返回false
long res=mc.incr("myAtomicNum",500,  1);//前先set不行的 
Object num=mc.get("myAtomicNum");
Future<Long> numAsyncFuture= mc.asyncIncr("myAtomicNum",10); 
Thread.sleep(200);
num=mc.get("myAtomicNum");

mc.shutdown(); 
//---为 Spring
MemcachedClientFactoryBean factoryBean=new MemcachedClientFactoryBean();
factoryBean.setServers("192.168.0.184:12000");
factoryBean.setOpTimeout(1000);//操作超时时间是1秒
factoryBean.setTimeoutExceptionThreshold(1998);//设置超时次数上限是1998次
MemcachedClient client=(MemcachedClient)factoryBean.getObject();
---------------------------------Redis client Jedis (spring使用这个)


<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
     <version>2.9.0</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>

Redis默认监听 6379 端口

//---单节点,测试OK
Jedis jedis=new Jedis(ip,6379);// 单机 或 cluster的master
jedis.auth("123456");//对配置 masterauth ,requirepass 
String keys = "name";  
if(jedis.exists(keys))
	jedis.del(keys);  
jedis.set(keys, "snowolf");  
System.out.println(jedis.get(keys));  

//---单节点,测试OK
JedisPool pool = new JedisPool(new JedisPoolConfig(), ip,port,2000);//timeout,可加passworld参数
		
Jedis jedis = pool.getResource();
jedis.set("foo", "bar");
String foobar = jedis.get("foo");

jedis.zadd("sose", 0, "car");//0是score
jedis.zadd("sose", 0, "bike"); 
Set<String> sose = jedis.zrange("sose", 0, -1);//score 范围
System.out.println(sose);
pool.returnResource(jedis); //将资源归还个连接池
pool.destroy();

//----transaction
jedis.watch("name");// 当前客户端监视该name键
//jedis.unwatch();    // 撤销监视

Transaction tran = jedis.multi();	// 开启事务状态

tran.set("name", "benson");	// 添加键值对
tran.set("job", "java");
Response<String> res= tran.get("job");
//tran.discard();		// 取消上述命令的执行
List<Object> list = tran.exec();	// 提交事务
System.out.println( res.get());//get在exec后执行

//一次性取全部,如是set命令结果为OK
for(Object resp : list) {
  System.out.println(resp.getClass().getName()+resp);
}

Transaction t = jedis.multi();
t.set("fool", "bar"); 
Response<String> result1 = t.get("fool");

t.zadd("foo", 1, "barowitch"); t.zadd("foo", 0, "barinsky"); t.zadd("foo", 0, "barikoviev");//是0
Response<Set<String>> sose = t.zrange("foo", 1, -1); 
List<Object> allResults =t.exec();

String foolbar = result1.get();
Set<String> set=sose.get();//如有错误,清数据    ,0分的只有第一个

//-------pipline
Pipeline p = jedis.pipelined();
p.set("fool", "bar"); 
p.zadd("foo", 1, "barowitch");  p.zadd("foo", 0, "barinsky"); p.zadd("foo", 0, "barikoviev");
Response<String> pipeString = p.get("fool"); // 先多次发送命令,过后再取结果,像transaction
Response<Set<String>> sose = p.zrange("foo", 0, -1);
p.sync(); 

int soseSize = sose.get().size();//如有错误,清数据
Set<String> setBack = sose.get();
System.out.println(setBack);

//-------
class MyListener extends JedisPubSub {
    public void onMessage(String channel, String message) {
    	System.out.println("onMessage:channel="+channel+" , msg="+message);
    }
    public void onSubscribe(String channel, int subscribedChannels) {
    	System.out.println("onSubscribe: channel="+channel+", subscribedChannels:"+subscribedChannels);
    }
    public void onUnsubscribe(String channel, int subscribedChannels) {
    	System.out.println("onSubscribe:channel="+channel+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPSubscribe(String pattern, int subscribedChannels) {
    	System.out.println("onSubscribe: pattern="+pattern+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    	System.out.println("onPUnsubscribe: pattern="+pattern+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPMessage(String pattern, String channel,  String message)
    {
    	System.out.println("onPMessage pattern="+pattern+",channel="+channel+" receive  :"+message);
    }
}
//jedis.psubscribe(listen, "a","b");//会一直阻塞,会根据参数个数n 调用n次JedisPubSub的onSubscribe方法
jedis.subscribe(listen, "foo");//会一直阻塞,会调用JedisPubSub的onSubscribe方法

jedis.publish("foo", "消息");//如已经有 subscribe(psubscribe无效)进程,则subscribe的进程会调用JedisPubSub的onMessage方法

//---集群的redis,依赖于 commons/poopl2
Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
jedisClusterNodes.add(new HostAndPort(node0Ip, node0Port));//只一个master节点OK
JedisCluster jc = new JedisCluster(jedisClusterNodes);
//jc.auth("123456");
jc.set("foo", "bar"); 
System.out.println(jc.get("foo"));
 

---------------------------------Redis client redisson	  分布式锁的实现 
//redisson  依赖于netty,fasterxml的jackson

Config config = new Config();
//--单机 
//SingleServerConfig singConfig= config.useSingleServer();
//singConfig.setAddress(ipPort);//ip:port

//--cluster配置
MasterSlaveServersConfig  msConfig=config.useMasterSlaveConnection();
msConfig.setMasterAddress(masterIPPort);
msConfig.addSlaveAddress(slaveIPPort);//可传多个node

//Redisson redisson = Redisson.create();//默认  127.0.0.1:6379
Redisson redisson = Redisson.create(config);

//---Distributed Object storage example
RBucket<AnyObject> bucket = redisson.getBucket("anyObject");
//bucket.set(new AnyObject());//单机OK,但cluster master 卡住???
bucket.setAsync(new AnyObject());//单机OK,但cluster master get时卡住???
AnyObject obj = bucket.get();

------------TDDL

------------ Hessian(二进制RPC协议) 可二进制做 webservice (dubbo 有用)
//--servlet server

import com.caucho.hessian.server.HessianServlet;

@WebServlet("/hessian/server")
public class BasicService extends HessianServlet implements BasicAPI 
{
//BasicAPI是自己定义的接口,不要重写doGet,doPost,只要实现自定义接口就OK
}

//--servlet client
import com.caucho.hessian.client.HessianProxyFactory;

String url = "http://127.0.0.1:8080/J_JavaEE/hessian/server";
HessianProxyFactory factory = new HessianProxyFactory();
BasicAPI basic = (BasicAPI) factory.create(BasicAPI.class, url);
System.out.println("====Hessian=====hello(): " + basic.hello());

MyRequest reqObj=new MyRequest(); //如传对象必须  implements  Serializable
reqObj.setSystemId("123");
reqObj.setSystemName("boss");
BigDecimal amt=BigDecimal.valueOf(22.35d);//要用BigDecimal.valueOf()不要用new BigDecimal()
reqObj.setAmt(amt);
basic.setObject(reqObj);
System.out.println("====Hessian=====server changed SystemId: " + basic.getObject().getSystemId());

spring-web集成
	org.springframework.remoting.caucho.HessianServiceExporter 
	
	
	
Hessian 自动配置BigDecimal, 
hessian.jar/META-INF/hessian/serializers 
java.math.BigDecimal=com.caucho.hessian.io.BigDecimalDeserializer

hessian.jar/META-INF/hessian/deserializers
java.math.BigDecimal=com.caucho.hessian.io.StringValueSerializer



------------dubbo   dubbo-admin-2.5.4.war
http://dubbo.io/
 一定tomcat-7版本,8不行的,JDK也不能用8
tomcat-7\webapps\dubbo\WEB-INF\dubbo.properties 配置zookeeper IP

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>dubbo</artifactId>
	<version>2.5.3</version>
</dependency>

--classpath下的 dubbo.properties
dubbo.application.name=MyProject1
dubbo.protocol.name=dubbo
dubbo.protocol.port=20884
#dubbo.protocol.heartbeat=60000
dubbo.registry.address=zookeeper://127.0.0.1:2181
#dubbo.registry.address=zookeeper://192.168.16.125:2181?backup=192.168.16.126:2181
dubbo.spring.config=classpath*:alibaba/dubbo/dubbo-client.xml,classpath*:alibaba/dubbo/dubbo-server.xml  #是所有spring中的配置

#for client
dubbo.reference.timeout=55000

#for server
pref.log.time.max.limit=500

#--only connect specical IP,only for dev enviroment 
dubbo.reference.dubboFacade.url= dubbo://127.0.0.1:20884


dubbo-client.xml  接口方法参数类 implements Serializable
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">
    <dubbo:reference id="dubboFacade"  interface="alibaba.dubbo.DubboFacade" check="false" retries="0"/> 
</beans>

<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">	
	
	<dubbo:service interface="alibaba.dubbo.DubboFacade" ref="dubboFacdeImpl"/>
	<bean id="dubboFacdeImpl"  class="alibaba.dubbo.DubboFacadeImpl" />
</beans>



------------dubbo    Thrift(代码生成)  
dubbo 协议适合   小数据量大并发,netty3.2.2 + hessian -3.2.1
    
./configure && make (要  bison version >= 2.5  ,xz -d -k bision-3.0.tar .xz )



<dependency>
  <groupId>org.apache.thrift</groupId>
  <artifactId>libthrift</artifactId>
  <version>0.9.2</version>
</dependency>



 //---Hello.thrift
 namespace java thrift.demo 
 service Hello{ 
  string helloString(1:string para) 
  i32 helloInt(1:i32 para) 
  bool helloBoolean(1:bool para) 
  void helloVoid() 
  string helloNull() 
 }
 
thrift --gen <language> <Thrift filename>
 -r  (recursivly)
	语言支持
	C++
	Java
	Cocoa

thrift  --gen java Hello.thrift    
生成Hello类,有以下内部类
 <方法名>_args
 <方法名>_result
 Hello.Client    生成有 send_<方法名> 方法,recv_<方法名> 方法
 Hello.AsyncClient
 Hello.Processor
 自己写类 HelloServiceImpl implements Hello.Iface
 

 
 
//---clients 
//TTransport transport = new TSocket("localhost", 7911); 	//阻塞式
TTransport transport = new TFramedTransport(new TSocket("localhost", 7911));//非阻塞式
transport.open();  

//TProtocol protocol = new TBinaryProtocol(transport); 
//TCompactProtocol protocol = new TCompactProtocol(transport);// 高效率的、密集的二进制编码格式进行数据传输 
//TJSONProtocol protocol = new TJSONProtocol(transport);
            
Hello.Client client = new Hello.Client(protocol); 
client.helloVoid(); //调用接口方法
client.helloNull();//返回null 报 TApplicationException，服务端也强迫关闭连接

transport.close(); 
//---客户端异步管理
TAsyncClientManager clientManager = new TAsyncClientManager(); 
TNonblockingTransport transport = new TNonblockingSocket("localhost", 10005); 
TProtocolFactory protocol = new TBinaryProtocol.Factory(); 
Hello.AsyncClient asyncClient = new Hello.AsyncClient(protocol,clientManager, transport); 
MethodCallback callBack = new MethodCallback(); 
asyncClient.helloString("Hello World", callBack); 
Object res = callBack.getResult(); 
while (res == null) { 
	res = callBack.getResult(); 
	System.out.println("wait...."); 
} 
System.out.println( ((Hello.AsyncClient.helloString_call) res)
					.getResult()); 
				
 public class MethodCallback implements AsyncMethodCallback 				
//----server

//Factory proFactory = new TBinaryProtocol.Factory(); 
TCompactProtocol.Factory proFactory = new TCompactProtocol.Factory();// 高效率的、密集的二进制编码格式进行数据传输 
//TJSONProtocol.Factory proFactory = new TJSONProtocol.Factory();
		  
TProcessor processor = new Hello.Processor(new HelloServiceImpl());            
 

//阻塞式
//TServerSocket serverTransport = new TServerSocket(7911); 
//TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor)
//					.protocolFactory(proFactory));
//TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor)
//					.protocolFactory(proFactory));//单线程

//非阻塞式
TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(7911); 
TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).processor(processor)
		.protocolFactory(proFactory)); 
		
System.out.println("Start server on port 7911..."); 
server.serve(); 
			
			


选择不同的传输协议和传输层而不用重新生成代码 
它提供阻塞、非阻塞、单线程和多线程的模式运行在服务器上



基本类型：

	bool：布尔值，true 或 false，对应 Java 的 boolean
	byte：8 位有符号整数，对应 Java 的 byte
	i16：16 位有符号整数，对应 Java 的 short
	i32：32 位有符号整数，对应 Java 的 int
	i64：64 位有符号整数，对应 Java 的 long
	double：64 位浮点数，对应 Java 的 double
	string：未知编码文本或二进制字符串，对应 Java 的 String

结构体类型：
	struct：定义公共的对象，类似于 C 语言中的结构体定义，在 Java 中是一个 JavaBean
容器类型：
	list：对应 Java 的 ArrayList
	set：对应 Java 的 HashSet
	map：对应 Java 的 HashMap
异常类型：
	exception：对应 Java 的 Exception
服务类型：
	service：对应服务的类
============XStream

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public static String objToXml(Object srcObj)
{
	XStream xStream = new XStream(new DomDriver());
	xStream.autodetectAnnotations(true);	// 使在Bean中的注解生效
	return  xStream.toXML(srcObj);
}

public static <T>  T xmlToObj(String xml,Class<T> clazz) 
{
	if(xml == null) 
		return null;
	XStream xStream = new XStream(new DomDriver());
	xStream.processAnnotations(clazz);//声明使用了注解
	Object obj = xStream.fromXML(xml);
	return (T)obj;
}


@XStreamAlias("Request") //因是最根级(上层)所以可以加在类前
public class MyRequest {
	@XStreamAlias("Head")
	private MyHead head;  // 在MyHead的类前面加@XStreamAlias 无效
	// @XStreamImplicit//隐式集合，只显示集合里的内容
	@XStreamImplicit(itemFieldName = "Body")
	private List<MyBody> body;
}

============ZkClient
<dependency>
	<groupId>com.101tec</groupId>
	<artifactId>zkclient</artifactId>
	<version>0.8</version>
</dependency>
	
	
String rootPath="/testZkClient";

ZkClient zkClient = new ZkClient("10.1.5.225:2581",10000,10000,new SerializableSerializer());
System.out.println("连接OK");

IZkChildListener childListender=	new IZkChildListener() {
	@Override
	public void handleChildChange(String parentPath, List<String> currentChild) throws Exception {
		System.out.println(parentPath+"的子节点改变了,现有子节点有 "+currentChild);
	}
} ;

zkClient.subscribeChildChanges(rootPath,childListender );//也可以监听不存在的节点,一但建立会收到

UserLogin session=new UserLogin();
session.setLastLogin(new Date());
session.setUserName("lisi");
//类要实现Serializable接口
String path=zkClient.create(rootPath, session, CreateMode.PERSISTENT);
System.out.println(path+"建立了");

 
Stat stat=new Stat();
UserLogin zkData=zkClient.readData(rootPath,stat);
System.out.println("读到了"+zkData );
System.out.println("读到 stat getAversion="+stat.getAversion());


boolean exist=zkClient.exists(rootPath);
System.out.println(rootPath+"存在? "+exist );

List<String> childNames=zkClient.getChildren(rootPath);
System.out.println(rootPath+"的子节点有"+childNames );


String childPath=zkClient.createPersistentSequential(rootPath.concat("/childOne"), "ChildOneVal");
System.out.println(childPath+"建立了");

IZkDataListener changeListender=new IZkDataListener() {
	@Override
	public void handleDataDeleted(String path) throws Exception {
		System.out.println(path+"数据删了");
	}
	
	@Override
	public void handleDataChange(String path, Object newVal) throws Exception {
		System.out.println(path+"数据修改了,新数为"+newVal);
	}
};
zkClient.subscribeDataChanges(childPath, changeListender);
zkClient.writeData(childPath, "new ChildOneVal");
Thread.sleep(10);//如不加,可能会只响应最后一次操作
zkClient.delete(childPath);

//		zkClient.delete(rootPath);//只可删没有子节点的
zkClient.deleteRecursive(rootPath); 

zkClient.unsubscribeDataChanges(childPath, changeListender);
zkClient.unsubscribeChildChanges(rootPath, childListender);

System.out.println("所有建立的节点删除了");

