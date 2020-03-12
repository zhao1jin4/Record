
==============================Maven服务器 Nexus OSS 
2.x版本有跨平台的 -bundle.zip解压 ，目前是3.x版本见下

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
 
------Nexus Repository OSS 3.5.1
 3.x版本 测试支持PyPI,Docker,npm,yum(proxy),社区支持 apt,go,CocoaPods
有windows,unix,mac版本
没有 -bundle.zip了 unix版本 要求只能JDK 1.8 ,解压出现了sonatype-work，刚解压只有log,tmp, orient/plugins目录，启动后会生成很多文件

nexus-3.5.1-02/bin/nexus start  是后台运行，相应的有stop
tail -f sonatype-work/nexus3/log/nexus.log 初次启动时间较长

./nexus run 前台运行
 
直接仿问 http://127.0.0.1:8081/ 默认有一个用户 admin  密码 admin123     
		提示 max file descriptor至少65536(默认4096)
		 /etc/security/limits.conf (Ubuntu 除外)
			nexus - nofile 65536
		重启 Nexus
设置阿里代理仓库
设置按钮->Repository下的Repositories->点create repository按钮 -> 选择 maven2(proxy)->起名aliyun,输入地址 http://maven.aliyun.com/nexus/content/groups/public
->create repository按钮,再把建立的加入maven-public中即可

默认带的maven-snapshot仓库配置的Deployment policy为 Allow redeploy ,maven-release是Disable redeploy

配置用
http://127.0.0.1:8081/repository/maven-public/
http://127.0.0.1:8081/repository/maven-releases/
http://127.0.0.1:8081/repository/maven-snapshots/

 浏览包用
 http://127.0.0.1:8081/#browse/browse/components:maven-public 有目录级别
 http://127.0.0.1:8081/#browse/browse/assets:maven-public     子目录以/显示
 
 
 
	 
 可以运行在 Docker 上  
 docker pull sonatype/nexus3
-------------------------------Artifactory 
收费的 jfrog的 
https://www.jfrogchina.com/artifactory/

除为Maven 还可Cocoapods为iOS使用，Go,Python,Docker,PHP,Npm
Cocoapods 是iOS仓库管理 


----------------------------------Maven

设置PATH环境变量  

mvn -version
mvn -e		full stack trace of the errors
mvn clean install -e -U
-e详细异常，-U强制更新

mvn compile
mvn test-compile

如单元测试报错, 控制台没有原因,要进入target/surefire-report/中的txt文件 有错误 堆栈信息


http://search.maven.org/ 可以搜索 Maven 依赖包 
http://www.mvnrepository.com    会提示名字是老的哪个是新的 如jsp-api,log4j

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
默认仓库是位于 ${user.home}/.m2/repository/ 即 %HOMEPATH%\.m2  MAVEN_REPO 的变量来修改
可以修改配置<localRepository>/path/to/local/repo/</localRepository>
设置 proxy ,但没说什么协议,如没有办法设置 http://主机:端口/文件  形式的代理 , [文件]的部分没办法给,)

 
<server>
   <id>releases</id> <!-- id对应 pom.xml中的 <distributionManagement> 中的Id的值   -->
   <username>admin</username>
   <password>admin123</password>
 </server>
 <server>
   <id>snapshots</id>
   <username>admin</username>
   <password>admin123</password>
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
					http://central.maven.org/maven2
					http://repo.spring.io/libs-release/ 
					http://maven.aliyun.com/nexus/content/groups/public
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
 <localRepository>/mnt/vfat/MVN_REPO/</localRepository>
	<servers>
	 <server>
	   <id>releases</id> <!-- id对应 pom.xml中的 <distributionManagement> 中的Id的值   -->
	   <username>admin</username>
	   <password>admin123</password>
	 </server>
	 <server>
	   <id>snapshots</id>
	   <username>admin</username>
	   <password>admin123</password>
	 </server>
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
mvn dependency:tree 打印整个依赖树 
 
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
			<version>3.2.2</version>
			 <configuration>
				<packagingExcludes>**/dubbo.properties</packagingExcludes>
				<attachClasses>true</attachClasses><!-- 父项目,就会把classs打包会生成<project>-<version>-classes.jar (在不加<finalName>的情况下)-->
				<archiveClasses>true</archiveClasses>
			</configuration>
		</plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-war-plugin</artifactId>
			<version>3.2.2</version>
			<configuration>
			<!--  
			子项目中 先 dependency  <type>war</type> 
					再依赖于<type>jar</type>
   							<classifier>classes</classifier>
				这个插件把依赖的war和这个war合并打包,会生成<项目>/overlays目录 存放依赖war的解压
			-->
			  <overlays>  
				<overlay> 
				  <groupId>com.example.projects</groupId>
				  <artifactId>documentedprojectdependency</artifactId>
				  <excludes>
					<exclude>WEB-INF/classes/images/sampleimage-dependency.jpg</exclude>
				  </excludes>
				</overlay>
			  </overlays>
			</configuration>
		  </plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-compiler-plugin</artifactId>
			<version>3.8.0</version>
			<configuration>
			  <verbose>true</verbose>
			  <fork>true</fork>
			  <executable>${JAVA_HOME}/bin/javac</executable>
			  <compilerVersion>1.8</compilerVersion>
			</configuration>
			<!--
			  <configuration>
                <source>1.8</source>
                <target>1.8</target>
             </configuration>
			-->
		  </plugin>
		  
		  
	官方文档
	 <plugin>  <!-- OK -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>  
	  或者 只加  <properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	
	  
		  
		<!-- mvn package时 把所有依赖的jar解压放在一起,如果为.jar 指定 Main-Class: com.   ,如使用了spring,运行的机器又不能上网要加 AppendingTransformer  -->
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

	   <plugin> <!--打包方式2,依赖jar包会放在目录下 -->
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-assembly-plugin</artifactId>
			<version>2.4</version> 
			<configuration> 
				<descriptors>
					<descriptor>assembly.xml</descriptor>
				</descriptors>
			</configuration>
			<executions>
				<execution>
					<id>make-assembly</id>
					<phase>package</phase>
					<goals>
						<goal>single</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
		<plugin> <!--打包方式3 要在jar外有lib目录,  但没有生成？？？？  --> 
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-jar-plugin</artifactId>
			<configuration>
				<source>1.8</source>
				<target>1.8</target>
				<archive>
					<manifest>
						<mainClass>com.xx.MainApp</mainClass>
						<addClasspath>true</addClasspath>
					<classpathPrefix>lib/</classpathPrefix>
					</manifest> 
				</archive>
				<classesDirectory>
				</classesDirectory>
			</configuration>
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
			<version>2.7.1</version> <!-- 只能2.7.1 版本才不会报找不到org/apache/commons/lang3/StringUtils-->
			<configuration>
			  <includes>
				<include>test/coverage/RunAllCoverageTest.java</include> <!-- 指定入口类 @SuiteClasses -->
			  </includes>
			  <configuration>
				<skip>true</skip>  <!-- 配置这个 相当于-DskipTests=true -->
			  </configuration>
			</configuration>
      </plugin>
	  
	<plugin>
	  <groupId>org.eclipse.jetty</groupId>
	  <artifactId>jetty-maven-plugin</artifactId>
	  <version>9.4.27.v20200227</version>
	   <configuration>
		  <scanIntervalSeconds>10</scanIntervalSeconds>
		  <webApp>
			<contextPath>/test</contextPath>
		  </webApp>
		</configuration>
	</plugin>  <!-- 就可用 mvn jetty:run  但不认@WebServlet的注解式???-->
    
 
 <!--部署到tomcat,配置权限用户,  mvn cargo:redeploy (一定要先mvn package)
如tomat8,127.0.0.1就OK, 本机IP就不行,/conf/Catalina/localhost/目录下要加文件manager.xml （没有就新建） (CSRF) -->
	<plugin>
		    <groupId>org.codehaus.cargo</groupId>
		    <artifactId>cargo-maven2-plugin</artifactId>
		    <version>1.4.4</version> <!-- tomcat8 要1.4.4, tomcat9 要 1.5.1-->
		    <configuration>
		        <container>
		            <containerId>tomcat8x</containerId>
		            <type>remote</type>
		        </container>
		        <configuration>
		            <type>runtime</type>
		            <properties>
		                <cargo.tomcat.manager.url>http://127.0.0.1:8080/manager</cargo.tomcat.manager.url>
		                <cargo.remote.username>manager01</cargo.remote.username>
		                <cargo.remote.password>manager01</cargo.remote.password>
		                <cargo.servlet.port>8080</cargo.servlet.port>
		                <cargo.hostname>127.0.0.1</cargo.hostname>
		                <cargo.tomcat.ajp.port>8009</cargo.tomcat.ajp.port>
		            </properties>
		        </configuration>
		    </configuration>
		</plugin>
		
		
	<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-antrun-plugin</artifactId>
        <version>1.8</version>
        <executions>
          <execution>
            <id>compile</id>
            <phase>compile</phase>
            <configuration>
              <target>
                <property name="compile_classpath" refid="maven.compile.classpath"/>
                <property name="runtime_classpath" refid="maven.runtime.classpath"/>
                <property name="test_classpath" refid="maven.test.classpath"/>
                <property name="plugin_classpath" refid="maven.plugin.classpath"/>

                <ant antfile="${basedir}/build.xml">
                  <target name="antBuild"/>
                </ant>
              </target>
            </configuration>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
	  <--  
	   build.xml 
		<project name="test">
			<target name="antBuild">
			  <echo message="compile classpath: ${compile_classpath}"/>
			  <echo message="runtime classpath: ${runtime_classpath}"/>
			  <echo message="test classpath:    ${test_classpath}"/>
			  <echo message="plugin classpath:  ${plugin_classpath}"/>
			  <property name="fromProject" value="baseProject"/>
			  <property name="fromVersion" value="1.0.1"/>
			  
			  <copy  todir="${basedir}/target/classes"  overwrite="no" >  
				<fileset dir="${basedir}/../${fromProject}/target/classes"/>
			 </copy>
			 <copy  todir="${basedir}/target/${ant.project.name}"  overwrite="no" >  
				<fileset dir="${basedir}/../${fromProject}/target/${fromProject}-${fromVersion}"/>
			 </copy>
			</target>
		</project>
		更好的方案
			==父war项目 maven-war-plugin 插件用 ,就会把classs打包会生成<project>-<version>-classes.jar
			<attachClasses>true</attachClasses> 
			<archiveClasses>true</archiveClasses> 
			
			== 子war项目依赖父<type>war</type>一次,在package时可以把父项目的webapp复制过来,如有overlay插件修改父项目pom.xml时,clean时也要把overlays目录删除
					再第二次依赖父<type>jar</type>
   								 <classifier>classes</classifier>会依赖<project>-<version>-classes.jar及所有子依赖,再复制WEB-INF/lib下,可以解决编译依赖类问题
			如子项目与项目同名文件，子项目会覆盖父项目
	-->
	  
   <plugin> <!--  对 <type>bundle</type>要加这个才行 -->
			<groupId>org.apache.felix</groupId>  
		    <artifactId>maven-bundle-plugin</artifactId>  
		    <extensions>true</extensions>  
		</plugin>
  
	</plugins>
  </build>
<profiles>  <!-- intellij IDE maven视图可以动态切换环境 -->
	<profile>
		<id>local</id>
		<properties>
			<env>dev</env>
			<!-- 里面的是自己使用的自属性 spring 配置中可以使用${env} ,log4j.properties可用${loglevel}-->
			<loglevel>DEBUG,Console</loglevel>
		</properties>
		<activation>
			<activeByDefault>true</activeByDefault><!-- 项目目录下的 dev 目录成为classpath -->
		</activation>
	</profile>
	<profile>
		<id>test</id>
		<properties>
			<env>test</env>
			<loglevel>DEBUG,Console</loglevel>
		</properties>
	</profile>
</profiles>

<!--就可以不从官方下载jar,速度快很多-->
<repositories>  
    <repository>  
        <id>central</id>  
        <name>Maven Repository Switchboard</name>  
        <url>http://repo.spring.io/libs-release/</url>  
        <layout>default</layout>  
        <snapshots>  
            <enabled>false</enabled>  
        </snapshots>  
    </repository>  
</repositories>  
<pluginRepositories>
	<pluginRepository>
		<id>dev_nexus</id>
		<url>http://repo.spring.io/libs-release/</url>
		<releases>
			<enabled>true</enabled>
		</releases>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</pluginRepository>
</pluginRepositories>
 
	<distributionManagement> <!-- 为mvn deploy时用使用id做对应  -->
        <repository>
            <id>releases</id>  <!-- 对应settings.xml中<server>的id配置 -->
            <url>http://127.0.0.1:8081/repository/maven-releases/</url>
        </repository>
        <snapshotRepository>
            <id>snapshots</id>
            <url>http://127.0.0.1:8081/repository/maven-snapshots/</url>
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
		  <version>4.12</version> <!-- 现在有5的版本  可以不指定版本 在 <dependencyManagement> <dependencies> 中管理版本 -->
		  <scope>test</scope> <!-- 只my_app/src/test/java中类可访问到-->
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>4.0.1</version> <!-- 3.0.1,4.0.1-->
			<scope>provided</scope> <!-- 不会参与打包 ,默认是compile ,还有runtime不参与run -->
		</dependency> 
		<!-- A依赖B，B依赖C  当C是test或者provided时，C直接被丢弃，A不依赖C 也就会编译错误
			<type>pom</type>
			<scope>import</scope>
		-->
 
		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>javax.servlet.jsp-api</artifactId>
			<version>2.3.3</version>
			<scope>provided</scope>
		</dependency>
	<dependency>
	    <groupId>javax.websocket</groupId>
	    <artifactId>javax.websocket-api</artifactId>
	    <version>1.1</version>
	    <type>bundle</type>
	 </dependency>
		<dependency>
         <groupId>ldapjdk</groupId>
         <artifactId>ldapjdk</artifactId>
         <scope>system</scope>  <!-- 指定jar包在本地系统中 -->
         <version>1.0</version>
         <systemPath>${basedir}\src\lib\ldapjdk.jar</systemPath>
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
			<artifactId>javax.servlet-api</artifactId>
		 </dependency> 
		 
		 <dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>${jedis}</version>
			<optional>true</optional> <!-- 可选的 -->
		</dependency>
		
	<!-- 防止报  Missing artifact jdk.tools:jdk.tools:jar:1.6      -->
	 <dependency>  
		<groupId>jdk.tools</groupId>
		<artifactId>jdk.tools</artifactId>
		<version>1.8</version>
		<scope>system</scope>
		<systemPath>${JAVA_HOME}/lib/tools.jar</systemPath>
	</dependency>
	
	</dependencies>
 </project>



----assembly.xml
<?xml version="1.0" encoding="UTF-8"?>
<assembly>
    <id>bin</id>
    <includeBaseDirectory>false</includeBaseDirectory> 
    <formats>
        <format>zip</format>
    </formats>
 
    <dependencySets>
        <dependencySet> 
            <useProjectArtifact>false</useProjectArtifact>
            <outputDirectory>query/lib</outputDirectory>
            <unpack>false</unpack>
        </dependencySet>
    </dependencySets>

    <fileSets> 
        <fileSet>
            <directory>${deploy.dir}/classes/</directory>
            <outputDirectory>query/conf</outputDirectory>
            <includes>
                <include>*.xml</include>
                <include>*.properties</include>
            </includes>
        </fileSet> 
        <fileSet>
            <directory>${project.build.directory}</directory>
            <outputDirectory>/query</outputDirectory>
            <includes>
                <include>*.jar</include>
            </includes>
        </fileSet>
    </fileSets>
</assembly>

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
 --update-snapshots  更新snapshots的依赖包

mvn dependency:list  显示所有依赖

mvn archetype:generate  会提示输入groupId,groupId

会生成pom.xml做模板使用 ,也可以使用eclipse mavne插件,建立maven项目来选择
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

对web项目
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false
 
mvn -f xxx.pom   -s  settting.xml

 maven输出class文件位置做修改  
(maven3\lib下的 maven-model-builder-3.0.5.jar 中的 org/apache/maven/model/pom-4.0.0.xml)
<directory>${project.basedir}/target</directory>
<outputDirectory>${project.build.directory}/classes</outputDirectory>
<testOutputDirectory>${project.build.directory}/test-classes</testOutputDirectory>

pom.xml在<build>中加(mvn compile  test-compile是有效果的)
<directory>${project.basedir}/target1</directory>  
<outputDirectory>src/main/webapp/WEB-INF/classes</outputDirectory> 
<testOutputDirectory>src/main/webapp/WEB-INF/classes</testOutputDirectory> 


plugin方式 mvn compile  test-compile时directory没用??outputDirectory和testOutputDirectory配置没用??只对class文件有用，
<plugin>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.8.0</version>
  <configuration>
	<directory>${project.basedir}/target1</directory> 
	<outputDirectory>src/main/webapp/WEB-INF/classes</outputDirectory> 
	<testOutputDirectory>src/main/webapp/WEB-INF/classes</testOutputDirectory>
  </configuration>
</plugin>


---sonar 覆盖率maven插件
jenkins ->配置->构建后操作步骤->Sonar,Publish Coberutra Coverage Report
mvn clean package sonar:sonar 测试覆盖率报告(要单独的服务器)

---cobertura 覆盖率maven插件
mvn clean package cobertura:cobertura   单独生成测试覆盖率报告   target/site/cobertura/index.html ,适用于每个项目的test 目录,只测试这个项目的main中的类


可在父POM中增加,也可以不加
 
<build>
	<plugins>
	  <plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>cobertura-maven-plugin</artifactId>
		<version>2.7</version>
	  </plugin> 
	</plugins>
</build>
<reporting>
	<plugins>
	  <plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>cobertura-maven-plugin</artifactId>
		<version>2.7</version>
	  </plugin> 
	</plugins>
</reporting>
 

----------------------------------Gradle
下载地址 https://gradle.org/releases/
https://docs.gradle.org/current/dsl/index.html
http://avatarqing.github.io/Gradle-Plugin-User-Guide-Chinese-Verision/

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
			maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
			maven { url 'http://mirrors.163.com/maven/repository/maven-public/' }
			maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter'}
			maven { url "$rootDir/../node_modules/react-native/android" }  // react-native 的 android 版本 jar 包maven仓库位置
		}
	}
---上未何没用 可能因为自己的项目有 allprojects 的配置
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
IDEA,AndroiStudio 要配置系统级别环境变量 GRADLE_USER_HOME 
  
eclipse 配置gradle user home:　会在目录创建caches\modules-2\files-2.1

IDEA 的gradle视图(同maven)->展开tasks->build->双击jar/war
eclipse 的gradle tasks 视图->展开build->双击jar,在build/libs目录生成
 
分析依赖 
IDEA 的Gradle视图 <项目> -> Task -> help -> dependencies 执行后控制台显示

 
//wrapper
空项目目录下 gradle wrapper --gradle-version 5.4 会生成 gradlew 可执行文件和gradle/wrapper目录,下有gradle-wrapper.properties文件，包含下载gradle对应版本bin包的URL
就可以执行 ./gradlew build 来构建项目

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
        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
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
<dependency>
	<groupId>jaxen</groupId>
	<artifactId>jaxen</artifactId>
	<version>1.1.6</version>
</dependency>
	
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

<dependency>
    <groupId>xerces</groupId>
    <artifactId>xercesImpl</artifactId>
    <version>2.12.0</version>
</dependency>

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
<dependency>
    <groupId>xalan</groupId>
    <artifactId>xalan</artifactId>
    <version>2.7.2</version>
</dependency>

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
Softerra LDAP Administrator 2017 是一个客户端管理工具

Active Directory =LDAP服务器＋LDAP应用（Windows域控）

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
Spring LADP				
==============================SNMP4J
	服务端使用Net-SNMP
==============================Jetty

eclipse 插件 run-jetty-run  可以在eclipse中使用 jetty 做servlet容器

java -jar start.jar 启动服务器　
	webapps目录效果类似tomcat 
　
http://localhost:8080/  
.war包 放到webapps目录下

RunJettyRun-1.8 插件 当eclipe认为是web项目(有建立Servlet的界面)才可run as ->jetty

==============================Tomcat Embed

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

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.5.2</version>
    <scope>test</scope>
</dependency>
import org.junit.jupiter.api.Test; //Junit 5    jupiter 木星；
import static org.junit.jupiter.api.Assertions.*;//Junit 5 
assertThrows(NumberFormatException.class,  ()->{
		Roman2IntUtil.covertRoman2Int("MIMIII");
	});
	
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
@After
public void destroy()
{
	System.out.println("junit after"); 
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
	
	 

边界问题 数组没有元素


测试应该没有依赖性,一个测试方法不会依赖于另一个方法
public static void main (String[] args) 
{
	junit.textui.TestRunner.run (suite()); 
}

public static junit.framework.Test suite() {   
    return new JUnit4TestAdapter(ListTest.class); 
}


public static Test suite() //use eclipse,junit 4 ,not need main method
{
	return new TestSuite(ListTest.class);
}

TestSuite 和TestCase 都实现了Test 接口 	



示例(带参数)
@RunWith(value = Parameterized.class)
public class CalculatorTest
{
	private int expected;
	private int para1;
	private int para2;
	
	@Parameterized.Parameters
	public static Collection<Integer[]> getParameters()//返回Collection
	{
		return Arrays.asList(new Integer[][] { 
			{ 3, 3, 2 }, //构造函数的参数类型必须全一样 
			{ 1, 1, 1 }  
		});
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
@SuiteClasses({CalculatorTest.class})//可传多个测试类
public class CalculatorTestSuit 
{

}
---------------------------JMockit 更强  

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
EasyMock.reset(mockDao);//不替换bean,直接 reset,BeanPostProcessor中已经是Mock的

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

-------------------------MockITO
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>2.23.4</version> <!-- 版本2.23.4 有依赖包有JDK9 module-info.class,2.22.0 就没有-->
    <scope>test</scope>
</dependency> 
依赖json-path

有android版本

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

//模拟返回值
MyServiceBean myServiceBean = mock(MyServiceBean.class);   
when(myServiceBean.queryData(any(Product.class))).thenReturn(dataSet); 
List<Product>  res=myServiceBean.queryData(new Product());
System.out.println(res);

//模拟抛异常
when(myServiceBean.insertData(ArgumentMatchers.anyList())).thenThrow(RuntimeException.class);
myServiceBean.insertData(Arrays.asList(new Product()));


---Spring 集成 mockito
import org.mockito.Mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)  
//@ActiveProfiles({"test"})
//@Transactional
@WebAppConfiguration //可以注入 WebApplicationContext
@ContextConfiguration(locations={"classpath:test_mockmvc/spring-mockmvc.xml"})
public class MockITO_MockMvcTest  
{
	//	@InjectMocks //会进入方法体中
	//	@Autowired

	@Mock
	private MyServiceBean myServiceBean;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testService() throws Exception
	{ 
		when(myServiceBean.queryData(any(Product.class))).thenReturn(dataSet); 
		List<Product>  res=myServiceBean.queryData(new Product());
		
		reset(myServiceBean);//重置指定的bean的所有录制  
	}
}
spring-mockmvc.xml 只有
	<context:component-scan base-package="test_mockmvc"></context:component-scan>
-------------------------TestNG
NG=Next Generation  (Not Good)

比Junit强的地方是支持 组,依赖,参数功能更强,支持listener
多支持下面的范围,JUnit-4 @BeforeClass 和 @AfterClass  必须声明静态方法 
	@BeforeSuite
	@AfterSuite
	@BeforeTest
	@AfterTest

 
<dependency>
  <groupId>org.testng</groupId>
  <artifactId>testng</artifactId>
  <version>6.10</version>
  <scope>test</scope>
</dependency>
会依赖于 jcommander-1.48.jar 



Eclipse Market Place 安装TestNG (或者下载离线版本(6.14.0) https://github.com/cbeust/testng-eclipse)
	安装后new file->有testng组
	
	项目目录下会生成 test-output 目录
	
IDEA  是自带TestNG的 (要加maven依赖)


import org.testng.Assert;
import org.testng.annotations.Test;
public class MyTestNG
{
	
	@BeforeTest
	public void beforeTest()// <test>
	{		
		System.out.println("@BeforeTest1");
	}
	@BeforeClass
	public void beforeClass() // <class>
	{		
		System.out.println("@BeforeClass2");
	}
	@AfterClass
	public void afterClass()
	{		
		System.out.println("@AfterClass3");
	}
	@AfterTest
	public void afterTest()
	{		
		System.out.println("@AfterTest4");
	}
	  
	@Test  //方法的返回类型一定要为void
	public void testMethod1() {
		String email="abc";
		Assert.assertNotNull(email);
		//Assert.assertEquals(email, "123@test.com");
	}
	@Test(enabled = true,expectedExceptions = {ArithmeticException.class},timeOut = 1000)
    public void divisionWithException() {
        int i = 1 / 0;
        System.out.println("After division the value of i is :"+ i);
    }
	
	//---group
	@Test(groups = "nosql")
	public void hbase() {
		System.out.println("hbase()");
	}
	@Test(groups = {"nosql","database"})//可同时在多个组中
	public void redis() {
		System.out.println("redis");
	}
	@Test(dependsOnGroups = { "database", "nosql" })//组依赖
	public void runFinal() {
		System.out.println("runFinal");
	}
	
	/*
	//运行 testng_group.xml
	@Test
	@Parameters({ "param1", "param2" })//参数是在xml配置类级传入
	public void testParam(String param1,String param2) {
		System.out.println("param1="+param1+",param2="+param2);
	}
	*/
	@Test(dataProvider = "provideNumbers")//provider参数形式 
    public void testProviderParam(int number, int expected) {
        Assert.assertEquals(number + 10, expected);
    }
    @DataProvider(name = "provideNumbers")
    public Object[][] provideData() {
        return new Object[][] { { 10, 20 }, { 100, 110 }, { 200, 210 } };
    }
	 //--一个provider用于多方法
    @Test(dataProvider = "dataProvider")
    public void test1(int number, int expected) {
        Assert.assertEquals(number, expected);
    }
    @Test(dataProvider = "dataProvider")
    public void test2(String email, String expected) {
        Assert.assertEquals(email, expected);
    }
    @DataProvider(name = "dataProvider")
    public Object[][] provideData(Method method)//一个provide为多个测试方法，参数是反射Method,得不到group
    {
        Object[][] result = null;
        if (method.getName().equals("test1")) {
            result = new Object[][] {
                { 1, 1 }, { 200, 200 }
            };
        } else if (method.getName().equals("test2")) {
            result = new Object[][] {
                { "test@gmail.com", "test@gmail.com" },
                { "test@yahoo.com", "test@yahoo.com" }
            };
        }
        return result;
    }
	@Test(invocationCount = 12, threadPoolSize = 3)//压力测试,可结合Selenium
    public void testThreadPools() {
    	System.out.printf("Thread Id : %s%n", Thread.currentThread().getId());
    }
  
  
}


public class SuiteConfig {

	@BeforeSuite
	public void beforeSuite() {
		System.out.println("@BeforeSuite0");
	}

	@AfterSuite
	public void afterSuite() {
		System.out.println(" @AfterSuite5");
	}
}
@Test(groups = "group1")//类级别的组
public class Group1 {
	
}

----testng_group.xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="GroupSuite" verbose="1" > 
   <test name="MyTest">
    <classes>
	    <class name="testng.SuiteConfig"/> 
        <class name="testng.MyTestNG">
		   <parameter name="param1" value="value1" />
       	   <parameter name="param2" value="22" />
        	<methods>
        		<!-- <include name=""></include>  -->	
        		<exclude name="hbase"></exclude>
        	</methods>
        </class>
    </classes>
  </test>
  <test name="GroupTest">
    <classes>
        <class name="testng.SuiteConfig"/>
     	<class name="testng.Group1"/> 
    </classes>
  </test>
  <!-- listener可以是 
		  IAnnotationTransformer
		  IAnnotationTransformer2
		  IHookable
		  IInvokedMethodListener
		  IMethodInterceptor
		  IReporter
		  ISuiteListener
		  ITestListener 
   -->
  <listeners>
	<listener class-name="testng.listener.MyTransformer" />
	<listener class-name="testng.listener.MyMethodInterceptor" />
	<listener class-name="testng.listener.DotTestListener" />
  </listeners>
</suite>
----testng_package.xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Suite1" verbose="1" >
  <test name="Page1"   >
    <packages>
      <package name="testng" /> <!-- 只找有 @Test注解的类/方法 -->
   </packages>
 </test> 
</suite>
 
java org.testng.TestNG testng1.xml [testng2.xml testng3.xml ...]

//如不在xml中配置listener也可在启动时指定 java org.testng.TestNG -listener testng.MyTransformer bin/testng/testng_group.xml
public class MyTransformer implements IAnnotationTransformer {
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if (testMethod!=null && "mongodb".equals(testMethod.getName())) {
			annotation.setInvocationCount(5);
		}
	}
}
public class MyMethodInterceptor implements IMethodInterceptor {
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<IMethodInstance> result = new ArrayList<IMethodInstance>();
		for (IMethodInstance m : methods) 
		{
			Test test = m.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
			if(test ==null)
			{
				test=m.getClass().getAnnotation(Test.class);
				if(test==null)
				{
					result.add(m);
					continue;
				}
			}
			Set<String> groups = new HashSet<String>();
			for (String group : test.groups()) {
				groups.add(group);
			}
			if (groups.contains("fast")) {//一个类中哪个先执行
				result.add(0, m);
			} else {
				result.add(m);
			}
		}
		return result;
	}
}
public class DotTestListener extends TestListenerAdapter  //是ITestListener的子类
{
  private int m_count = 0;
  @Override
  public void onTestFailure(ITestResult tr) {
    log("F");
  }
  @Override
  public void onTestSkipped(ITestResult tr) {
    log("S");
  }
  @Override
  public void onTestSuccess(ITestResult tr) {
    log(".");
  }
  private void log(String string) {
    System.out.print(string);
    if (++m_count % 40 == 0) {
      System.out.println("");
    }
  }
} 
----testng_provider.xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="ProviderSuite">
    <test name="providerTest">
        <groups>
            <run>
                <include name="groupA" /> <!-- groupB(方法test2)不会被测试 -->
            </run>
        </groups>
        <classes>
            <class name="testng.ProviderXmlTest" />
        </classes>
    </test>
</suite>
//只能用testng_provider.xml运行
public class ProviderXmlTest {
    @Test(dataProvider = "dataProvider1", groups = {"groupA"})
    public void test1(int number) {
        Assert.assertEquals(number, 1);
    }
    @Test(dataProvider = "dataProvider1", groups = "groupB")
    public void test2(int number) {
        Assert.assertEquals(number, 2);
    }
    @DataProvider(name = "dataProvider1")
    public Object[][] provideData(ITestContext context) {//一个provide为多个测试方法，参数是ITestContext
        Object[][] result = null;
        System.out.println(context.getName());//providerTest
        for (String group : context.getIncludedGroups()) { //<groups> 下的 <run>下的 <include
            System.out.println("group : " + group);
            if ("groupA".equals(group)) {
                result = new Object[][] { { 1 } };
                break;
            }
        }
        if (result == null) {
            result = new Object[][] { { 2 } };
        }
        return result;
    }
}

public class TestNGAndSelenium {
	WebDriver driver = new FirefoxDriver();

	@BeforeTest
	public void init()// TestNG可以结合Selenium一起用
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://www.baidu.com");
		driver.manage().window().maximize();
	}

	@AfterTest
	public void destory() {
		driver.close();
	}

	@Test
	public void testOper() {
		WebElement search_text = driver.findElement(By.id("kw"));
		WebElement search_button = driver.findElement(By.id("su"));

		search_text.sendKeys("Java");
		search_text.clear();
		search_text.sendKeys("Selenium");
		// search_button.click();//二选一
		search_text.submit();// 同输入回车
	}
}

Selenium and TestNG
----------Selenium  自动化测试
支持C# , Python,
也有 Android Driver 项目selendroid  是ebay的  http://selendroid.io/
也有 iOS Driver   是ebay的 http://ios-driver.github.io/ios-driver/

<dependency>
  <groupId>org.seleniumhq.selenium</groupId>
  <artifactId>selenium-java</artifactId>
  <version>3.141.59</version> <!-- 版本 3.141.59 有 META-INF/versions/9/module-info.class  版本3.14.0就没有-->
</dependency>
  
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>phantomjsdriver</artifactId>
    <version>1.4.4</version>
</dependency>

selenium-java-3.141.59.jar 空的 这个版本没有phantomjsdriver,  3.2.0 有 com.codeborne.phantomjsdriver 
	selenium-api-3.141.59.jar
	selenium-remote-driver-3.141.59.jar
	okhttp-3.11.0.jar
	okio-1.14.0.jar
	commons-exec-1.3.jar
	
	selenium-chrome-driver-3.141.59.jar
	selenium-firefox-driver-3.141.59.jar
	
	selenium-edge-driver-3.141.59.jar
	selenium-ie-driver-3.141.59.jar
	phantomjsdriver-1.4.4.jar (com.codeborne 组  	项目目录下会生成 .log文件)
	selenium-safari-driver-3.141.59.jar

selenium-server-standalone-3.9.1.jar  是为非maven项目，包括全部依赖在一个jar中


源码地址，有API
https://github.com/Selenium/selenium	
 
镜像站点 下载 Selenium
https://npm.taobao.org/mirrors/selenium/
3.9/selenium-server-standalone-3.9.1.jar  

---浏览器驱动
safari driver 
	High Sierra 和更新版本运行  safaridriver --enable 
	
chromedriver 镜像站点
https://npm.taobao.org/mirrors/chromedriver
72.0.3626.7

chromedriver 官方
http://chromedriver.storage.googleapis.com/index.html
72.0.3626.7/

firefox driver v0.23.0
https://github.com/mozilla/geckodriver/releases


edge 17134
https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

ie 3.9.0 (2018-02-05)
http://selenium-release.storage.googleapis.com/index.html

---Selenium IDE 地址官方有，可以录制页面
firefox 
https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/

chrome (chrome web store上)
https://chrome.google.com/webstore/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd


phantomjs 浏览器 
	可以生成页面截图  
	---./snapshot.js
		var page = require('webpage').create();
		var args = require('system').args;

		var url = args[1];
		var filename = args[2] || "snapshot.png";

		var t = new Date();
		page.open(url, function () {
			window.setTimeout(function () {
				page.render(filename);
			console.log('time: '+(new Date() - t));	
				phantom.exit();
			}, 1000);
		});
	//phantomjs.exe --ignore-ssl-errors=yes ./snapshot.js https://www.baidu.com/  c:/tmp/my.png  命令测试OK 
	
将下载的浏览器驱动文件目录放入PATH环境变量中

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
 
//对chrome 放在PATH环境 ,也要加webdriver.chrome.driver 系统属性
//java  -Dwebdriver.chrome.driver=D:\Program\seleniumDriver\chromedriver.exe
// WebDriver driver = new ChromeDriver();//OK
//WebDriver driver = new  FirefoxDriver();//OK firefox命令 要在PATH中可以找到
//WebDriver driver = new   EdgeDriver(); //OK
WebDriver driver = new PhantomJSDriver();//OK 用com.codeborne 组的 phantomjsdriver-1.4.4.jar
//WebDriver driver = new InternetExplorerDriver();//NO 报连接不上端口
//WebDriver driver = new SafariDriver();//未测试

driver.get("http://www.itest.info");
String title = driver.getTitle();
System.out.printf(title);


//		queryElement(driver);//自己的查元素
//		browserOper(driver);//自己的浏览器操作
//		manualInteract(driver);//自己的页面交互
	    waitAjax(driver);//等待,为ajax使用

driver.close();
public static void queryElement(WebDriver driver) 
{
	WebElement element=driver.findElement(By.id("head")); //document.getELementById()
	System.out.println("id="+element.getTagName());
	
	element=driver.findElement(By.name("ie")); //document.getELementByNames()
	System.out.println("name="+element.getTagName());
	
	element=driver.findElement(By.className("fm")); //document.getElementsByClassName()
	System.out.println("class="+element.getTagName());
	
	element=driver.findElement(By.tagName("b"));//document.getELementsByName()
	System.out.println("tag="+element.getTagName());
	
	
	element=driver.findElement(By.xpath("//form[@id='form']/input[@type='hidden' and @name='tn']"));
	System.out.println("xpath="+element.getTagName()); 
	
	//css找不到？？？ 
	
	element=driver.findElement(By.linkText("新闻"));
	System.out.println("linkText="+element.getTagName()); 
	
	element=driver.findElement(By.partialLinkText("地"));
	System.out.println("partialLinkText="+element.getTagName()); 
	
	WebElement size = driver.findElement(By.id("kw"));
	System.out.println("getSize="+size.getSize());

	WebElement text = driver.findElement(By.id("cp"));
	System.out.println("getText="+text.getText());
	
	WebElement ty = driver.findElement(By.id("kw"));
	System.out.println("getAttribute="+ty.getAttribute("type"));
	
	WebElement display = driver.findElement(By.id("kw"));
	System.out.println("isDisplayed="+display.isDisplayed());
		
}
public static void browserOper(WebDriver driver) throws Exception {

	driver.manage().window().maximize();
	Thread.sleep(2000);

	driver.get("https://m.baidu.cn");
	driver.manage().window().setSize(new Dimension(480, 800));
	Thread.sleep(2000);
	
	driver.get("https://www.baidu.com/");
	driver.findElement(By.linkText("新闻")).click();//单击
	System.out.printf("news url  %s \n", driver.getCurrentUrl());
	 
	driver.navigate().back();// 后退
	System.out.printf("back to %s \n", driver.getCurrentUrl());
	Thread.sleep(2000);
   
	driver.navigate().forward(); //前进
	System.out.printf("forward to %s \n", driver.getCurrentUrl());
	Thread.sleep(2000);
	
	driver.navigate().refresh();//刷新
	System.out.printf("refresh page \n" );

}
public static void manualInteract(WebDriver driver) throws Exception {
	driver.get("https://www.baidu.com/");

	WebElement search_text = driver.findElement(By.id("kw"));
	WebElement search_button = driver.findElement(By.id("su"));

	search_text.sendKeys("Java");
	search_text.clear();
	search_text.sendKeys("Selenium");
	//search_button.click();//二选一
	search_text.submit();//同输入回车
	
	
	//鼠标操作
	WebElement search_setting = driver.findElement(By.linkText("设置"));
	Actions action = new Actions(driver);
	action.clickAndHold(search_setting).perform();
/*
	 // 鼠标右键点击指定的元素
	 action.contextClick(driver.findElement(By.id("element"))).perform();

	 // 鼠标右键点击指定的元素
	 action.doubleClick(driver.findElement(By.id("element"))).perform();

	 // 鼠标拖拽动作， 将 source 元素拖放到 target 元素的位置。
	 WebElement source = driver.findElement(By.name("element"));
	 WebElement target = driver.findElement(By.name("element"));
	 action.dragAndDrop(source,target).perform();

	 // 释放鼠标
	 action.release().perform();
  */  
	
	
	//键盘操作
	WebElement input = driver.findElement(By.id("kw"));
	
	//删除多输入的一个
	input.sendKeys(Keys.BACK_SPACE);
	Thread.sleep(2000);

	//输入空格键+“教程”
	input.sendKeys(Keys.SPACE);
	input.sendKeys("教程");
	Thread.sleep(2000);

	//ctrl+a 全选输入框内容
	input.sendKeys(Keys.CONTROL,"a");
	Thread.sleep(2000);

	//ctrl+x 剪切输入框内容
	input.sendKeys(Keys.CONTROL,"x");
	Thread.sleep(2000);

	//ctrl+v 粘贴内容到输入框
	input.sendKeys(Keys.CONTROL,"v");
	Thread.sleep(2000);

	//通过回车键盘来代替点击操作
	input.sendKeys(Keys.ENTER);
	Thread.sleep(2000);

	 //获取第一条搜索结果的标题
	 WebElement result = driver.findElement(By.xpath("//div[@id='content_left']/div/h3/a"));
	 System.out.println(result.getText());

}

public static void waitAjax(WebDriver driver) throws Exception 
{
	 driver.get("https://www.baidu.com");
 
 //显式等待， 针对某个元素等待
	//超时秒数，间隔秒数
	WebDriverWait wait = new WebDriverWait(driver,10,1);//要selenium-support-3.141.59.jar

	wait.until(new ExpectedCondition<WebElement>(){
	  @Override
	  public WebElement apply(WebDriver text) {
			return text.findElement(By.id("kw"));
		  }
	}).sendKeys("selenium");

	driver.findElement(By.id("su")).click();
	Thread.sleep(2000);
	
	
}


--------------------------iText
https://developers.itextpdf.com/   
	有 7 community API https://itextsupport.com/apidocs/itext7/latest/
	Examples
	Download 7 version

<dependency>
  <groupId>com.itextpdf</groupId>
  <artifactId>itextpdf</artifactId>
  <version>5.5.13</version>
</dependency>
<dependency>
  <groupId>com.itextpdf</groupId>
  <artifactId>itext-asian</artifactId>
  <version>5.2.0</version>
</dependency>

pdfHTML 基于HTML生成 7 版本是收费的 , 有Community

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
Paragraph p1=new Paragraph("_Hello 你好_",font);
p1.setIndentationLeft(10f);
p1.setIndentationRight(10f);
document.add(p1);
document.newPage();   //换页 
document.close();
--表格
String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.BOLD,new GrayColor(0.9f));

PdfPCell title=new PdfPCell(new  Paragraph(10,"hello world",font))   
//new PdfPCell(new Phrase("Cell with rowspan 2"));
title.setColspan(2);
title.setRowspan(2);
title.setBackgroundColor(BaseColor.GREEN);

		
PdfPTable table=new PdfPTable(numColumns)
table.addCell(title);

PdfPCell  cell = new PdfPCell(new Phrase("cn 中文 ,row 1; cell 1",font));
cell.setUseAscender(true);
cell.setMinimumHeight(20f);
cell.setHorizontalAlignment(1);
cell.setVerticalAlignment(5);
cell.setNoWrap(false);
table.addCell(cell);

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
if(reader.isEncrypted())
{
			System.out.println("pdf是加密的");
}
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
List<HashMap<String, Object>> list = SimpleBookmark.getBookmark ( reader ) ;
for ( Iterator<HashMap<String, Object>> i = list.iterator () ; i.hasNext () ; ) 
{
	showBookmark (i.next ()) ;
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
 
//-----

Map<String,String> param =new HashMap<>();
param.put("my_username", "张三");
param.put("my_age", "20");

//world 创建模板文件 另存为(libreOffice导出) pdf  -> Adobe Acrobat Reader Proc DC  再编辑 PDF 
//		->准备表单->工具栏上 添加"文本"域 ,拖出一个区域, 设置变量,itext程序就可以赋值 
//设置模板变量
AcroFields form=stamper.getAcroFields();
String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
form.addSubstitutionFont(chineseFont);
for(String key :param.keySet())
{
	form.setField(key, param.get(key));
}
stamper.close();
//----

String[] files=new String[] {"d:/temp/template_user_res.pdf","d:/temp/HelloWorld.pdf"};
String res="d:/temp/merge_res.pdf";
PdfReader pdfReader=new PdfReader(files[0]);
//pdf合并
Rectangle rect=pdfReader.getPageSize(1);
Document document = new Document(rect);
PdfCopy copy=new PdfCopy(document,new FileOutputStream(res));
document.open();
for(int i=0;i<files.length;i++)
{
	PdfReader  reader=new PdfReader(files[i]);
	int n=reader.getNumberOfPages();
	for(int j=1;j<=n;j++)
	{
		document.newPage();
		PdfImportedPage page=copy.getImportedPage(reader, j);
		copy.addPage(page);
	} 
	reader.close();
}
document.close();
pdfReader.close();
//---- 背景文字
String fromFile="d:/temp/merge_res.pdf";
String bgFile="d:/temp/merge_res_bg.pdf";
String markText="背景文字";

String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);

PdfReader  reader=new PdfReader(fromFile);
PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(bgFile));
int n=reader.getNumberOfPages();
Phrase phrase=new Phrase(markText,font);
for(int i=1;i<=n;i++)
{
	//PdfContentByte over=stamper.getOverContent(i);//水印在文本之上
	PdfContentByte over=stamper.getUnderContent(i); //水印在文本之下
	over.saveState();
	PdfGState state=new PdfGState();
	state.setFillOpacity(0.2f);
	over.setGState(state);
	float beginPositionX=10,beginPositionY=70,distance=175;
	for(int i2=0;i2<4;i2++)
	{
		for(int j=0;j<4;j++)
		{
			ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
						phrase, beginPositionX+distance*i2, beginPositionY+distance*j, 25);
		}
	}
	over.restoreState();
}
stamper.close();
reader.close(); 


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
  <version>2.0.11</version>
  <type>bundle</type>
</dependency>

要基于HTML生成，要支持中文 
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
 
 倒排索引， 从词出发，记载了这个词在哪些文档中出现过
 由两部分组成——词典和倒排表。
 
 官方带的中文分词器 analyzers-smartcn  ,lucene-analyzers-smartcn-6.4.0.jar  大小  3.43M
 
 
	core/lucene-core-6.4.0.jar
	analysis/common/lucene-analyzers-common-6.4.0.jar
	analysis/smartcn/lucene-analyzers-smartcn-6.4.0.jar
	queryparser/lucene-queryparser-6.4.0.jar
	
// http://lucene.apache.org/core/6_4_0/index.html          Lucene demo, its usage, and sources
	
//Document可以看作是 数据库的一行记录，Field可以看作是数据库的字段

--建立索引
//Directory dir = new RAMDirectory();//新版本8没有这个构造器，类过时,用MMapDirectory
Directory directory =new MMapDirectory(  Paths.get("/tmp"));//MMap=memory mapping 
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
//field.setBoost(1.2f);//新版本8没有这个方法 //默认1.0,建立索引时(更新索引不可)在原有基础上加权,为某些特定的内容,分值高,可做优先显示
 
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
  //Analyzer analyzerStop= new StopAnalyzer();//新版本8没有这个,(my)(football)(i)(very)(like)
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
		//stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);//新版本8没有ENGLISH_STOP_WORDS_SET
	}
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		//Tokenizer source = new LowerCaseTokenizer();//新版本8没有LowerCaseTokenizer
		Tokenizer source = new StandardTokenizer();
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


==============================MongoDB 
 <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>3.8.2</version>
</dependency>
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongodb-driver-core</artifactId>
	<version>3.8.2</version>
</dependency>
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongodb-driver-async</artifactId>
	<version>3.8.2</version>
</dependency>
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>bson</artifactId>
    <version>3.8.2</version>
</dependency>


3.8 支持MongoDB 4.0 的事务
3.11.0支持MongoDB 4.2

Multi-document transactions are available for replica sets only. 
Transactions for sharded clusters are scheduled for MongoDB 4.2
 
mongodb-driver-3.8.2.jar
mongodb-driver-async-3.8.2.jar
	mongodb-driver-core-3.8.2.jar
bson-3.8.2.jar

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
import com.mongodb.client.ClientSession;

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
		System.out.println("-- bios collection count:"+coll.countDocuments()); // coll.estimatedDocumentCount()
		
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
	public static void transaction(  )
	{
 
//		Multi-document transactions are available for replica sets only. 
//		Transactions for sharded clusters are scheduled for MongoDB 4.2
	
		MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());
	
		//replication set 事务OK
		ServerAddress[] addrs=	new ServerAddress[] {
				new ServerAddress("127.0.0.1", 37017),
				new ServerAddress("127.0.0.1", 37018),
				new ServerAddress("127.0.0.1", 37019)};
		
		//单机不支持事务
//		ServerAddress[] addrs=	new ServerAddress[] { new ServerAddress("127.0.0.1", 27017) };
		MongoClientOptions opts= new MongoClientOptions.Builder().build();
	 	MongoClient mongoClient  = new MongoClient(Arrays.asList(addrs), credential,opts );  
	
//		db.employees.insert({employee:3,status:'none'})
//		db.events.insert({})
		MongoCollection<Document> employeesCollection = mongoClient.getDatabase("reporting").getCollection("employees");
		employeesCollection.drop();
		employeesCollection.insertOne(new Document("employee",3).append("status", "none"));
	    MongoCollection<Document> eventsCollection = mongoClient.getDatabase("reporting").getCollection("events");
	    eventsCollection.drop();
	    eventsCollection.insertOne(new Document());
	    ClientSession clientSession = mongoClient.startSession();
	    try   {
	        clientSession.startTransaction();

	        employeesCollection.updateOne(clientSession,
	                Filters.eq("employee", 3),
	                Updates.set("status", "Inactive"));
	        eventsCollection.insertOne(clientSession,
	                new Document("employee", 3).append("status", new Document("new", "Inactive").append("old", "Active")));

	        clientSession.commitTransaction();
	    
	    }catch(Exception e)
		{
	    	e.printStackTrace();
	    	clientSession.abortTransaction();
		}finally {
			clientSession.close();
		}
	}
	public static void main(String[] args) throws Exception
	{
		//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//		                                      new ServerAddress("localhost", 27018),
//		                                      new ServerAddress("localhost", 27019)));
		  
        MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());  
         MongoClientOptions opts= new MongoClientOptions.Builder().build();
        MongoClient mongoClient  = new MongoClient(Arrays.asList(new ServerAddress("10.1.5.226", 27017)), , credential,opts);  
		
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
import com.mongodb.MongoClientSettings;

//MongoClient client = MongoClients.create();//default port 27017, default connection string "mongodb://localhost" 
//MongoClient client = MongoClients.create("mongodb://localhost");
//MongoClient client = MongoClients.create(new ConnectionString("mongodb://localhost"));
 
Block<Builder> block=new Block<Builder>(){
	@Override
	public void apply(Builder builder) {
		builder.hosts(Collections.singletonList((new ServerAddress("localhost",27017))));
	}
};
MongoClientSettings settings = MongoClientSettings.builder().applyToClusterSettings(block).build();
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
---------Querydsl MongoDB
http://www.querydsl.com/static/querydsl/latest/reference/html/
有Querying SQL,Querying Lucene, Querying Hibernate Search,Querying in Scala

<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-mongodb</artifactId>
    <version>4.2.1</version>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
     <artifactId>querydsl-core</artifactId>
    <version>4.2.1</version>
</dependency> 
<dependency>
  <groupId>com.mysema.commons</groupId>
  <artifactId>mysema-commons-lang</artifactId>
  <version>0.2.4</version>
</dependency>
<dependency>
    <groupId>org.mongodb.morphia</groupId>
    <artifactId>morphia</artifactId>
    <version>1.3.2</version>
</dependency>



生成代码才用的
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-apt</artifactId>
  <version>4.2.1</version> 
</dependency>
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.2</version>
</dependency>
<plugin>
    <groupId>com.mysema.maven</groupId>
    <artifactId>apt-maven-plugin</artifactId>
    <version>1.1.3</version>
    <executions>
      <execution>
        <goals>
          <goal>process</goal>
        </goals>
        <configuration>
          <outputDirectory>target/generated-sources/java</outputDirectory>
          <processor>com.querydsl.apt.morphia.MorphiaAnnotationProcessor</processor>
        </configuration>
      </execution>
    </executions>
  </plugin>
 
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference; 

@Entity ("mo_customer") 
public class Customer {
    @Id
    private String id;
 
    @Property("first_name")
    private String firstName; 
}

mvn clean install 在 target/generated-sources/java 目录生成 QCustomer.java
 
import javax.annotation.Generated;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.EntityPathBase;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {
  public final StringPath firstName = createString("firstName");
  public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);
  //....
}



@Entity("employees")
@Indexes(
		//@Index(value = "salary", fields = @Field("salary"))
		@Index( fields = {@Field("salary")})
)
class Employee {
    @Id
    private ObjectId id;
    private String name;
    private Integer age;
    @Reference
    private Employee manager;
    @Reference
    private List<Employee> directReports = new ArrayList<Employee>();
    @Property("wage")
    private Double salary;
}


 
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia; 

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress; 
import com.querydsl.mongodb.morphia.MorphiaQuery;

MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());
ServerAddress[] addrs=	new ServerAddress[] { new ServerAddress("127.0.0.1", 27017) };
MongoClientOptions opts= new MongoClientOptions.Builder().build();
MongoClient mongoClient  = new MongoClient(Arrays.asList(addrs),  credential ,opts );  
	 	
	 	
Morphia morphia= new Morphia();
Datastore datastore = morphia.createDatastore(mongoClient,"reporting");
	  
  //--Morphia employee 

 datastore.ensureIndexes();

 final Employee elmer = new Employee("Elmer Fudd", 50000.0);
 datastore.save(elmer);

 final Employee daffy = new Employee("Daffy Duck", 40000.0);
 datastore.save(daffy);

 final Employee pepe = new Employee("Pepé Le Pew", 25000.0);
 datastore.save(pepe);

 elmer.getDirectReports().add(daffy);//保存的是directReports数据，0是{$ref:"",$id:""}
 elmer.getDirectReports().add(pepe);

 datastore.save(elmer);

 Query<Employee> query = datastore.find(Employee.class);
 final long employees = query.count();
  long underpaid = datastore.find(Employee.class)
                          .filter("salary <=", 30000)
                          .count();

  underpaid = datastore.find(Employee.class)
                     .field("salary").lessThanOrEq(30000)
                     .count();
  
  
 final Query<Employee> underPaidQuery = datastore.find(Employee.class)
                                                 .filter("salary <=", 30000);
 final UpdateOperations<Employee> updateOperations = datastore.createUpdateOperations(Employee.class)
                                                              .inc("salary", 10000);

final UpdateResults results = datastore.update(underPaidQuery, updateOperations);
final Query<Employee> overPaidQuery = datastore.find(Employee.class)
                                                     .filter("salary >", 100000);
datastore.delete(overPaidQuery);
  
 //----------- 
//        morphia.map(Customer.class);
//        morphia.mapPackage("com.hoo.entity");
		  
 Customer cust=	new Customer("li","si_dsl_morphie"); 
		datastore.save(cust);//会存一个className的字段
  
		DBObject dbObj=morphia.toDBObject(cust) ;
		System.out.println(dbObj);
		System.out.println("fromDBObject: " + morphia.fromDBObject(datastore,Customer.class, BasicDBObjectBuilder.start("lastName", "abc").get()));
		System.out.println("getMapper: " + morphia.getMapper());
		System.out.println("isMapped: " + morphia.isMapped(Customer.class));
		//QCustomer customer = new QCustomer("customer");
		QCustomer customer =   QCustomer.customer;
  //依赖于 mysema-commons-lang-0.2.4.jar
		MorphiaQuery<Customer> query = new MorphiaQuery<Customer>(morphia, datastore, customer);
		List<Customer> list = query
		    .where(customer.firstName.eq("li"))
      .limit(5).offset(1)//跳过一个 
		    .fetch();
		System.out.println(list);





//------querydsl-collections-4.2.1.jar
import static com.querydsl.collections.CollQueryFactory.*; //delete,update,from
import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;

==============================Neo4j   JavaClient

<dependency>
    <groupId>org.neo4j.driver</groupId>
    <artifactId>neo4j-java-driver</artifactId>
    <version>1.6.3</version>
</dependency>

支持JDK8,不支持9

 NoSQL  图 数据库  Cypher Query Language


import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import static org.neo4j.driver.v1.Values.parameters;

public class HelloWorldExample implements AutoCloseable
{
    private final Driver driver;

    public HelloWorldExample( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                                     "SET a.message = $message " +
                                                     "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }//try
    }
    public static void main( String... args ) throws Exception
    {
        try ( HelloWorldExample greeter = new HelloWorldExample( "bolt://localhost:7687", "neo4j", "myneo4j" ) )
        {
            greeter.printGreeting( "hello, world" );
        }
    }
}

try (Transaction tx = session.beginTransaction())
{
	tx.run("MERGE (a:Person {name: {x}})", parameters("x", name));
	tx.success();  // Mark this write as successful.
}

 StatementResult result = session.run(
		"MATCH (a:Person) WHERE a.name STARTS WITH {x} RETURN a.name AS name",
		parameters("x", initial));
// Each Cypher execution returns a stream of records.
while (result.hasNext())
{
	Record record = result.next(); 
	System.out.println(record.get("name").asString());
}




============================== neo4j-ogm 
<dependency>
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-core</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency> 
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-http-driver</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency> 
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-bolt-driver</artifactId>
    <version>3.1.4</version>
</dependency>
<dependency>
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-embedded-driver</artifactId>
    <version>3.1.4</version> 
</dependency> <!-- 运行依赖于 org.neo4j.graphdb.GraphDatabaseService -->

neo4j-ogm-core-3.1.4.jar
	neo4j-ogm-api-3.1.4.jar
	fast-classpath-scanner-2.18.1.jar
neo4j-ogm-bolt-driver-3.1.4.jar
neo4j-ogm-http-driver-3.1.4.jar


------ neo4j-ogm.properties
URI=http://neo4j:myneo4j@localhost:7474

#
#URI=http://localhost:7474
#username="neo4j"
#password="myneo4j"


String classpathFile="nosql_neo4j_ogm/neo4j-ogm.properties";//JDK 8 不能以/开头
Properties properties = new Properties();
InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathFile)   ;
properties.load(is);
is.close();
 
Configuration configuration = new Configuration.Builder()
		.uri("bolt://localhost")
		.credentials("neo4j", "myneo4j")
 //--
		 // .uri("http://neo4j:myneo4j@localhost:7474")
 //--
		 //.uri("file:///D:/software/neo4j/neo4j-community-3.4.8-windows/neo4j-community-3.4.8/data/databases/graph.db")
		 //ClassNotFoundException: org.neo4j.graphdb.GraphDatabaseService ???
		  
 .build();
//--------
//ConfigurationSource props = new ClasspathConfigurationSource(classpathFile);
//ConfigurationSource props = new FileConfigurationSource("D:\\NEW_\\eclipse_java_workspace/J_JavaThirdLib/src/nosql_neo4j_ogm/neo4j-ogm.properties");
//Configuration configuration = new Configuration.Builder(props).build();
SessionFactory sessionFactory = new SessionFactory(configuration, "nosql_neo4j_ogm.domain");//packages


/*
org.neo4j.driver.v1.Driver nativeDriver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "myneo4j") );
Driver ogmDriver = new BoltDriver(nativeDriver);
SessionFactory sessionFactory = new SessionFactory(ogmDriver, "nosql_neo4j_ogm.domain");//packages

 */		

 Session session = sessionFactory.openSession();

 
 
Movie movie = new Movie("The Matrix", 1999);
Actor keanu = new Actor("Keanu Reeves");
keanu.actsIn(movie);

session.save(movie); 
Movie matrix = session.load(Movie.class, movie.getId()); 
for(Actor actor : matrix.getActors()) {
	System.out.println("Actor: " + actor.getName());
}

package nosql_neo4j_ogm.domain;
@NodeEntity(label="lbl_Movie") 
public class Movie {
 
	@Id @GeneratedValue
	private Long id;

	@Property(name="one_title")
	private String title;
	
	 @Transient //不存储
	private int released;

	@Relationship(type = "ACTS_IN",  
			 direction=Relationship.INCOMING
			 )
	Set<Actor> actors=new HashSet<>();

	 
	public Movie() { //类必须有空的公有构造器
	}

	public Movie(String title, int year) {
		this.title = title;
		this.released = year;
	}
	//getter/setter
}
 
@NodeEntity
public class Actor {
 
	@Id @GeneratedValue
	private Long id;

   @Property(name="fullName")
   private String name ;

	@Relationship(type = "ACTED_IN", direction=Relationship.OUTGOING)
	private Set<Movie> movies = new HashSet<>();

	public Actor() {
	}

	public Actor(String name) {
		this.name = name;
	}

	public void actsIn(Movie movie) {
		movies.add(movie);
		movie.getActors().add(this);
	}
   //getter/setter
}
-------------------- Kafaka在hadoop中

-------------------------------------------- RabbitMQ  3.7.7
RabbitMQ有scala go,c++ 客户端, OpenStack使用这个有Python客户端

https://rabbitmq.github.io/rabbitmq-java-client/api/current/

版本　3.7.7　要 ERLang语言(为分布式,erlc 编译语言)　版本至少 20.3
 
RabbitMQ,启动停止可在services.msc中做也可使用命令启动
配置文件 是 rabbitmq.config  
D:\Program\RabbitMQ Server\rabbitmq_server-3.7.7\etc\rabbitmq.config.example 复制修改 (3.7.11版本就没这个文件了？)
#%HOMEPATH%\AppData\Roaming\RabbitMQ\rabbitmq.config.example
默认端口  {tcp_listeners, [5672]},

linux 下解压有sbin目录 rabbitmqctl 要 erl　命令  ,安装erlang不太容易 CentOS 7 下 yum install erlang 根本没有
centos 7
su -c 'rpm -Uvh http://download.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-10.noarch.rpm'
su -c 'yum install erlang' 就可以了

linux sbin目录下  	
./rabbitmq-server 前台启动
./rabbitmq-server  -detached    后台启动
./rabbitmqctl stop   停止
看界面默认配置文件 解压的 rabbitmq_server-3.7.7/etc/rabbitmq/rabbitmq.config  
看控制台默认日志在 解压的 rabbitmq_server-3.7.7/var/log/rabbitmq/rabbit@<hostname>.log
 看界面默认数据目录     rabbitmq_server-3.7.11/var/lib/rabbitmq/mnesia/rabbit@<hostname>
 
windows zip 设置 ERLANG_HOME=D:\Program\erl9.3\ 变量  
看控制台默认日志 %HOMEPATH%\AppData\Roaming\RabbitMQ\log  可配置环境变量 RABBITMQ_LOG_BASE
看界面默认配置文件 %HOMEPATH%/AppData/Roaming/RabbitMQ/rabbitmq.config
看界面默认数据目录 %HOMEPATH%\AppData\Roaming\RabbitMQ\db\RABBIT~1
看界面默认amp端口是 5672
看界面默认clustering端口是 25672
 
 
windows安装版本服务启动 Cookie
从 C:\Windows\System32\config\systemprofile\.erlang.cookie 复制到
C:\Users\%USERNAME%\.erlang.cookie 

右击RabbitMQ服务->登录->用户为安装时指定的管理员用户(日志文件也在这个管理员用户目录下)
保证当前用户和管理用户的.erlang.cookie 都是从systemprofile目录复制过来的

/var/lib/rabbitmq/.erlang.cookie (used by the server) 
$HOME/.erlang.cookie (used by CLI tools)

rabbitmqctl status
rabbitmqctl  add_user zh 123  创建用户名密码 
rabbitmqctl  list_users
rabbitmqctl  change_password  zh  456
rabbitmqctl  delete_user  zh
rabbitmqctl  set_user_tags  zh  administrator  就有权限远程登录了

rabbitmqctl  add_user mon 123 
rabbitmqctl  set_user_tags  mon  monitoring  就有权限远程登录了
(policymaker，management)
rabbitmqctl  list_user_permissions  mon
rabbitmqctl list_queues

rabbitmq-plugins  enable rabbitmq_management    开启网页管理界面 15672 端口 (windows要使用命令启动服务,才可仿问界面，用户名要用安装时的管理员) 

http://127.0.0.1:15672/api/index.html 有 Resetful 接口文档


rabbitmqctl add_vhost myVhost
rabbitmqctl delete_vhost myVhost
rabbitmqctl list_vhost
rabbitmqctl list_vhosts name tracing

#rabbitmqctl set_permissions [-p vhost] user conf write read #格式
rabbitmqctl set_permissions  -p myVhost zh ".*" ".*" ".*"
rabbitmqctl set_permissions  -p / zh ".*" ".*" ".*"
rabbitmqctl clear_permissions  -p myVhost  zh
rabbitmqctl list_permissions  -p myVhost 
rabbitmqctl list_user_permissions zh

#命令有 list_queues, list_exchanges, list_bindings and list_consumers
rabbitmqctl list_exchanges -p myVhost name type
#rabbitmqctl list_bindings -p myVhost exchange_name queue_name 格式
rabbitmqctl list_bindings -p myVhost
rabbitmqctl list_connections send_pend port
rabbitmqctl list_connections
rabbitmqctl list_channels connection messages_unacknowledged




http://127.0.0.1:15672/     guest/guest  只可localhost登录 可以建立Queue
还有其它工具
http://127.0.0.1:15672/api
http://127.0.0.1:15672/cli 

STOMP 插件
rabbitmq-plugins enable rabbitmq_stomp　　默认监听　61613　端口

修改端口　rabbitmq.conf　新的是sysctl格式,即properties格式　(3.7 以前版本是rabbitmq.config　是json格式)
stomp.listeners.tcp.1 = 12345


还要在界面中配置 Admin-> Virtual Hosts 配置/ 虚拟机的用户仿问权限

<dependency>
  <groupId>com.rabbitmq</groupId>
  <artifactId>amqp-client</artifactId>
  <version>5.2.0</version>
</dependency>

dependencies {
  compile 'com.rabbitmq:amqp-client:5.0.0'
}



String message = "Hello World!";
String EXCHANGE_NAME = "myExtchange";
String ROUTING_KEY = "routingKey"; 
String QUEUE_NAME = "myQueueName";

--sender
ConnectionFactory factory = new ConnectionFactory(); 
factory.setUsername("zh"); 
factory.setPassword("123"); 
factory.setVirtualHost("/"); 
factory.setHost("127.0.0.1"); 
factory.setPort(5672); 
Connection conn = factory.newConnection(); 
Channel channel = conn.createChannel(); 
//会按 名自动建立exchange
//direct可用 BuiltinExchangeType.DIRECT;
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); //boolean durable  持久化 


//AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().replyTo(callbackQueueName).build();//回调的Queue
AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; //当durable true时
		
//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,props,message.getBytes());
System.out.println(" [x] Sent '" + message + "'");

channel.close(); 
conn.close();

--receiver
//RabbitMQ同一时间发给消费者的消息不超过一条
//这样就能保证消费者在处理完某个任务，并发送确认信息后，RabbitMQ才会向它推送新的消息
//在此之间若是有新的消息话，将会被推送到其它消费者，若所有的消费者都在处理任务，那么就会等待。
int prefetchCount = 1;
channel.basicQos(prefetchCount);//RPC 放消费端       允许限制通道上的消费者所保持最大的未确认消息数量，如某台机器反应慢，

channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);  //boolean durable  持久化 
		
String queueName = channel.queueDeclare().getQueue(); //自动建立Queue,自动取名,是AD=AutoDelete,Excl=Exlusive
System.out.println("queueName="+queueName);

//自动建立Queue,D=durable
//String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments
channel.queueDeclare(QUEUE_NAME, true, false, false, null);//且持久化要和现有的配置相同,即如果Queue已经存在不能变durable了
System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

Consumer consumer = new DefaultConsumer(channel) {
	  @Override
	  public void handleDelivery(String consumerTag, Envelope envelope,
								 AMQP.BasicProperties properties, byte[] body)
		  throws IOException {
		String message = new String(body, "UTF-8");
		System.out.println(" [x] Received '" + message + "'");
		
		 //对于basicConsume  autoAck为false时
		//channel.basicAck(envelope.getDeliveryTag(),//消息标识
		//		false);//multiple 是否多个,即这个标识前面的一次性全部认为Ack收到了
		//还有Nack(可多个)不知道(未收到) 和 Reject (只可一个)
		
	  }
	};
channel.basicConsume(QUEUE_NAME, true, consumer);//boolean autoAck,true收到消息就自动应答,false要手工应答(在消息任务完成后)

----
//Get方式取消息 立即返回
do 
{
  GetResponse response=channel.basicGet(QUEUE_NAME, false); //参数queue, autoAck
  if(response==null)
  {
  Thread.sleep(2000);
  continue;
  }
  String message = new String(response.getBody(), "UTF-8");
  System.out.println("GET 立即返回 取消息结果为："+message);
  
  //对于 autoAck为false时
  channel.basicAck(response.getEnvelope().getDeliveryTag(),//消息标识
    false);//multiple 是否多个,即这个标识前面的一次性全部认为Ack收到了
  //还有Nack(可多个)不知道(未收到) 和 Reject (只可一个)
  
   //---相当于stack的peek
			//channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);//long deliveryTag, boolean multiple, boolean requeue,如为false变以为discarded/dead-lettered

    
}while(true);
----
Direct Exchange 将一个队列绑定到交换机上，要求该消息与一个特定的路由键routing key完全匹配
Fanout Exchange 个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上(忽略routing key)
Topic Exchange 队列名是一个模式上， 匹配有两种方式all和any 
			符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”的routeKey(未测试)
Headers exchange  不是使用routingkey去做绑定。而是通过消息headers的键值对匹配,发送者在发送的时候定义一些键值对，接收者也可以再绑定时候传入一些键值对
接收端必须要用键值"x-mactch"来定义。all代表定义的多个键值对都要满足，而any则代码只要满足一个就可以了。
---类型headers   sender
  
//(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.HEADERS,false,true,null);  
String message = new Date()+ " : log something";  
  
Map<String,Object> headers =  new Hashtable<String, Object>();  
headers.put("aaa", "01234");  
Builder properties = new BasicProperties.Builder();  
properties.headers(headers);  
  
// 指定消息发送到的转发器,绑定键值对headers键值对  
//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
channel.basicPublish(EXCHANGE_NAME, "",properties.build(),message.getBytes());  
channel.close();  
connection.close();

---类型headers  receiver 
 //(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.HEADERS,false,true,null);  

//String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments
channel.queueDeclare(QUEUE_NAME,false, false, true,null);  

Map<String, Object> headers = new Hashtable<String, Object>();  
headers.put("x-match", "any");//all any  
headers.put("aaa", "01234");  
headers.put("bbb", "56789");  
// 为转发器指定队列，设置binding 绑定header键值对  
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"", headers);  
Consumer consumer = new DefaultConsumer(channel) {
  @Override
  public void handleDelivery(String consumerTag, Envelope envelope,
							 AMQP.BasicProperties properties, byte[] body)
	  throws IOException {
	String message = new String(body, "UTF-8");
	System.out.println(" [x] Received '" + message + "'");
  }
}; 
// 指定接收者，第二个参数为自动应答，无需手动应答 
//(String queue, boolean autoAck, Consumer callback		
channel.basicConsume(QUEUE_NAME, true, consumer);   
----rabbitMQ 事务 (也是发送)

//(String exchange, String type,  boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);  

//(String queue,  boolean durable,  boolean exclusive,  boolean autoDelete,   Map<String,Object> arguments)
channel.queueDeclare(QUEUE_NAME, true, false, false, null);  
 
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);  
			
channel.txSelect();  
	//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
	channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_BASIC, ("第"+(i+1)+"条消息").getBytes("UTF-8")); 
channel.txCommit();   
channel.txRollback();   //catch中回滚操作
//阻塞，即回复后才可发下一条消息。


----TTL  DLX 
//---TTL
Map<String, Object>  argss = new HashMap<String, Object>();
argss.put("vhost", "/");
argss.put("username","root");
argss.put("password", "root");
argss.put("x-message-ttl",6000); //超过指定时时间如没有消费就不能消费了
//队列级别设置TTL  Time-To-Live  
channel.queueDeclare(queueName, durable, exclusive, autoDelete, argss);
不设置TTL,则表示此消息不会过期
如果将TTL设置为0，则表示除非此时可以直接将消息投递到消费者,否则该消息会被立即丢弃

//也可使用命令设置
// rabbitmqctl set_policy TTL ".*" '{"message-ttl":60000}' --apply-to queues

//消息级别设置TTL
AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
builder.deliveryMode(2);//持久化
builder.expiration("6000");
AMQP.BasicProperties  properties = builder.build();
channel.basicPublish(exchangeName,routingKey,mandatory,properties,"ttlTestMessage".getBytes());
 
//队列设置和消息都设置TTL较小的那个数值为准 
channel.exchangeDeclare("some.exchange.name", "direct");


//---Dead Letter Exchange  (DLX)
进入死信的情况
1.消息过期
2.消息被拒绝 Base.Reject/Base.Nack,并且requeue为false
3.队列达到最大长度

Map<String, Object> args = new HashMap<String, Object>();
args.put("x-dead-letter-exchange", "some.exchange.name");
channel.queueDeclare("myqueue", false, false, false, args);


args.put("x-dead-letter-routing-key", "some-routing-key");

rabbitmqctl set_policy DLX ".*" '{"dead-letter-exchange":"my-dlx"}' --apply-to queue
rabbitmqctl set_policy DLX ".*" "{""dead-letter-exchange"":""my-dlx""}" --apply-to queues //windows

界面中D=durable
DLK= x-dead-Letter-routing-Key

----ConfirmListener
 
//publish confirm模式  ，只能通道回复了即可发送下一条（Basic.Publish,Basic.Ack），比事务(Basic.Publish,Tx.Commit,Tx.Commit.OK)少发一条指令 如果消息是要持久化，都在存磁盘后回复
//publish confirm模式  ，只能通道回复了即可发送下一条（异步），事务是同步 阻塞，即回复后才可发下一条消息。
channel.confirmSelect();


//异步确认
/*
channel.addConfirmListener(new ConfirmListener() {
 @Override
 public void handleAck(long deliveryTag, boolean multiple) throws IOException {
  System.out.println("handleAck: deliveryTag="+deliveryTag+",multiple="+multiple);
 }
 @Override
 public void handleNack(long deliveryTag, boolean multiple) throws IOException {
  System.out.println("handleNack: deliveryTag="+deliveryTag+",multiple="+multiple);
 }});
*/

AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; //当durable true时
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); //durable 持久化  ,  BuiltinExchangeType.DIRECT 
//durable true 如果没有消费 ,重启后还有的，但持久化前是先缓存的

for(int i=0;i<2;i++)
{
  //(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
  channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,   props,
    (message+i).getBytes());
 System.out.println(" [x] Sent '" + message +i+ "'");
}
//同步确认
//只对channel.confirmSelect()后使用，可以发送多个消息后一次confirm
boolean confirmRes=channel.waitForConfirms();
System.out.println("confirmRes="+confirmRes); 

-----ReturnListener
channel.confirmSelect(); 
 
		//对失败的发送, 模拟方式使用错误的 routingKey
		//要求 basicPublish 带 mandatory 或者 immediate 标志
		/*
		channel.addReturnListener(new ReturnListener() {//如要使用lambda语法 使用 ReturnCallback
			@Override
			public void handleReturn(int replyCode,String replyText, String exchange, String routingKey, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println("ReturnListener replyCode="+replyCode
						+",exchange="+exchange
						+",routingKey="+routingKey
						//+",properties="+properties
						+",body="+new String(body) 
						);
				//只对失败，做重发
			}
		});
		*/
		channel.addReturnListener( new ReturnCallback() {
			@Override
			public void handle(Return returnMessage) {
				System.out.println("ReturnCallback replyCode="+returnMessage.getReplyCode()
				+",returnMessage.toString()="+ ToStringBuilder.reflectionToString(returnMessage));
				//只对失败，做重发
			}
		});
		AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; 
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);  
		for(int i=0;i<2;i++)
		{
			//(String exchange, String routingKey, boolean mandatory, boolean immediate, AMQP.BasicProperties props, byte[] body)
			//mandatory为true
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,  true,false, props, (message+i).getBytes());
		 	System.out.println(" [x] Sent '" + message +i+ "'");
		}  
  
----
String jsonStr = new com.rabbitmq.tools.json.JSONWriter().write(para);//Object(可Map)->JSON
Object obj = new com.rabbitmq.tools.json.JSONReader().read(jsonStr);//返回Object是一个HashMap

------RabbitMQ Cluster 

单台可满足每秒1000条消息吞吐

RAM 节点和　DISK节点

每台机器的cookie要是相同的文件做 ~/.erlang.cookie  存字串,集群中每个节点这个值是相同的,如不存在会创建 

多台机器分别单实例启动，以一个实例为基础，其它全部加入这个实例
其中一个实例
1. rabbitmqctl stop_app  不停止rabbitmq进程,而是停止erlang的内部进程
2. rabbitmqctl reset 重置内部数据
3. rabbitmqctl join_cluster rabbit@<hostname>   这个名字在管理界面的Admin->Cluster中可以修改的
4. rabbitmqctl start_app

rabbitmqctl cluster_status
[runing_nodes,[node1,node2]]

如全关闭后，再启动，应先启动最后关闭的，否则会等最后关闭的启动
rabbitmqctl forget_cluster_node nodex  从集群中去除节点


======================RocktMQ   alibaba 捐给了apache

控制台 
https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console
mvn spring-boot:run
或者
mvn clean package -Dmaven.test.skip=true
java -jar target/rocketmq-console-ng-1.0.0.jar  --server.port=8888 --rocketmq.config.namesrvAddr=127.0.01:9876;192.168.1.107:9876

http://127.0.0.1:8080/ 就有界面了  OPS 中有 NameSvrAddrList   地址 
消息标签 可以查看消息体，可以重发

<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-common</artifactId>
	   <version>4.2.0</version>
</dependency>
<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-client</artifactId>
	   <version>4.2.0</version>
</dependency>
<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-remoting</artifactId>
	   <version>4.2.0</version> 
</dependency>
 
4.2
环境变量  ROCKETMQ_HOME=
启动Name Server 
bin/mqnamesrv

启动Broker  
bin/mqbroker -n localhost:9876  autoCreateTopicEnable=true
  
停服务
bin/mqshutdown broker
bin/mqshutdown namesrv
 
String mqGroup = "myGroup";
String mqIP = "localhost:9876";
String mqTopic = "myTopic";
String mqTag = "myTag";
String key="123";
String msgStr="hello 小李";

String appName="app-1";

//服务/消费 端
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqGroup);
consumer.setConsumerGroup(mqGroup);
consumer.setNamesrvAddr(mqIP);
consumer.setVipChannelEnabled(false);
consumer.setConsumeThreadMax(10);//接收消息最多启10个线程处理，防止线程过多导致数据库连接用光
consumer.setConsumeThreadMin(5);
//设置广播消费  还有 MessageModel.CLUSTERING
//consumer.setMessageModel(MessageModel.BROADCASTING);//要对应用  MessageListenerConcurrently
//consumer.setInstanceName(appName);//如果是BROADCASTING的当一个app服务全部down机，再启动时就会丢失前面的消息，
//如一个app服务有2台都会收到相同的消息(这点不太好，接收方法要做幂等，JMS 持久化topic好像不是这样的)
 
//批量消费,每次拉取10条
//consumer.setConsumeMessageBatchMaxSize(10);

//如果非第一次启动，那么按照上次消费的位置继续消费
consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//consumer.subscribe(mqTopic, mqTag);
 
consumer.subscribe(mqTopic, "myTag || TagB || TagC || TagD || TagE"); //匹配多个用 || 或   * ,如传null同* 
MessageListenerOrderly messageListenerOrderly= new MessageListenerOrderly() //有顺序的
{
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext)
	{
		MessageExt msg=list.get(0);
		if(msg.getTags().contains(mqTag))//区分哪个tag,用于传送多种消息格式
			  System.out.println("这个tag是"+mqTag);
		System.out.println(new Date()+appName+"服务收到消息体："+new String(msg.getBody(),Charset.forName("UTF-8")));
		return ConsumeOrderlyStatus.SUCCESS;
	}
};

MessageListenerConcurrently messageListenerConcurrently= new MessageListenerConcurrently() //BROADCASTING
{
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) 
	{
		//要做幂等
		MessageExt msg=list.get(0);
		if(msg.getTags().contains(mqTag))//区分哪个tag,用于传送多种消息格式
			  System.out.println("这个tag是"+mqTag);
		System.out.println(new Date()+appName+"服务收到消息体："+new String(msg.getBody(),Charset.forName("UTF-8")));
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // CONSUME_SUCCESS
	}
};
consumer.registerMessageListener(messageListenerOrderly); 
//consumer.registerMessageListener(messageListenerConcurrently); //BROADCASTING

long iterval=this.getPullInterval();//0立即收到消息
consumer.start();
System.out.println("服务 启动");

//客户/生产端
DefaultMQProducer producer = new DefaultMQProducer(mqGroup);
producer.setNamesrvAddr(mqIP);
producer.start();
 
producer.setRetryTimesWhenSendAsyncFailed(0);
 for (int i = 0; i < 10; i++) 
 {
	final int index = i; 
	Message msg = new Message(mqTopic, mqTag, key, msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
	msg.setKeys("hello"+i);//要唯一
	msg.setDelayTimeLevel(3);//延迟收到消息,官方示例上说3对应的是10秒,防止消费端过快处理
	//  producer.sendOneway(msg);
	 //SendResult sendResult = producer.send(msg);//同步发
	producer.send(msg, new SendCallback() {//异步发
		@Override
		public void onSuccess(SendResult sendResult) {
			System.out.printf("%-10d OK %s %n", index,
				sendResult.getMsgId());
		}
		@Override
		public void onException(Throwable e) {
			System.out.printf("%-10d Exception %s %n", index, e);
			e.printStackTrace();
		}
	});
} 

producer.shutdown();


//事务发
TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();//自己的类
TransactionMQProducer producer = new TransactionMQProducer(mqGroup);
producer.setNamesrvAddr(mqIP);
producer.setCheckThreadPoolMinSize(2);
producer.setCheckThreadPoolMaxSize(2);
producer.setCheckRequestHoldMax(2000);
producer.setTransactionCheckListener(transactionCheckListener);//broker检查发送的回调吗
producer.start();

String[] tags = new String[] {mqTag, "TagB", "TagC", "TagD", "TagE"};
LocalTransactionExecuter tranExecuter = new TransactionExecuterImpl();//自己的类 
for (int i = 0; i < 100; i++) {
	Message msg =  new Message(mqTopic, tags[i % tags.length], "KEY" + i,
			("你好 RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
	SendResult sendResult = producer.sendMessageInTransaction(msg, tranExecuter, null);
	System.out.printf("%s%n", sendResult);
	Thread.sleep(10); 
}

public class TransactionExecuterImpl implements LocalTransactionExecuter {
    private AtomicInteger transactionIndex = new AtomicInteger(1);
    @Override
    public LocalTransactionState executeLocalTransactionBranch(final Message msg, final Object arg) {
        int value = transactionIndex.getAndIncrement();
        if (value == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
        	System.out.println("发送 commit msg"+msg);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }
}
public class TransactionCheckListenerImpl implements TransactionCheckListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);
    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        System.out.printf("server checking TrMsg %s%n", msg);
        int value = transactionIndex.getAndIncrement();
        if ((value % 6) == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }
}





 多个nameServer 服务中,只要有一个有效整个cluster就可用
 Broker 发送心跳到所有的nameServer
 
 broker master  可读写
 broker slave 只读

conf 目录 
2m-2s-async 两主，两从，同步复制数据的配置
2m-2s-sync 两主，两从，异步复制数据的配置
2m-noslave 两主,无从的配置

Broker分为Master与Slave，一个Master可以对应多个Slave，但是一个Slave只能对应一个Master，
Master与Slave的对应关系通过指定相同的BrokerName，不同的BrokerId来定义，BrokerId=0表示Master，>0的整数表示Slave
每个Broker与Name Server集群中的所有节点建立长连接

Producer 向提供Topic服务的Master建立长连接
Consumer 向提供Topic服务的Master、Slave建立长连接

一个topic下，我们可以设置多个queue


可以事务 
第一阶段发送Prepared消息拿到消息的地址，
第二阶段执行本地事物，
第三阶段通过第一阶段拿到的地址去访问消息，并修改消息的状态

如第三阶失败 向消息发送端(生产者)确认,如还失败 , 会根据发送端设置的策略来决定是回滚还是继续发送确认消息

Push and Pull model,
TAG 可以用同一个topic 发不同的消息



--------------------------------- POI excel xls,xlsx 
 <dependency>
	<groupId>org.apache.poi</groupId>
	<artifactId>poi-ooxml</artifactId>
	<version>3.17</version>
</dependency>

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
	//workbook=new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
	workbook=new XSSFWorkbook();
	out=new FileOutputStream("/tmp/workbook.xlsx");
}
else
{
	out = new FileOutputStream("/tmp/workbook.xls");
	workbook=new HSSFWorkbook();
}
Sheet sheet = workbook.createSheet();
workbook.setSheetName(0, "第一页");

sheet.createFreezePane(0, 1);//冻结第一行，如标题行 

int default_width=sheet.getColumnWidth(1);//default_width=2048
sheet.setColumnWidth(1, default_width*2);


CellStyle baseCellStyle = workbook.createCellStyle();  
Font font=workbook.createFont();
//font.setColor(Font.COLOR_RED );
font.setColor( IndexedColors.BLACK.getIndex());//文字颜色 (short)0xc 
//font.getBold(); 
  
baseCellStyle.setFont(font); 
baseCellStyle.setBorderTop(BorderStyle.THIN);
baseCellStyle.setBorderBottom(BorderStyle.THIN);
baseCellStyle.setBorderLeft(BorderStyle.THIN);
baseCellStyle.setBorderRight(BorderStyle.THIN);
baseCellStyle.setAlignment(HorizontalAlignment.CENTER);
baseCellStyle.setWrapText(true);//换行  

CellStyle numberCellStyle=workbook.createCellStyle(); 
numberCellStyle.cloneStyleFrom(baseCellStyle);
DataFormat df = workbook.createDataFormat();  
numberCellStyle.setDataFormat(df.getFormat("#,#0.00")); //小数点后保留两位 

//背景色样式
 CellStyle colorCellStyle=workbook.createCellStyle(); 
 colorCellStyle.cloneStyleFrom(baseCellStyle);
 colorCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
 colorCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 


//红色字体样式
Font redFont=workbook.createFont(); 
redFont.setColor( IndexedColors.RED.getIndex());

//日期样式
CellStyle dateCellStyle=workbook.createCellStyle(); 
dateCellStyle.cloneStyleFrom(baseCellStyle);
short shortDateFormat=workbook.createDataFormat().getFormat("yyyy-mm-dd"); 
dateCellStyle.setDataFormat(shortDateFormat);  

Row row = sheet.createRow(0);//如要写第二列，就要使用sheet.getRow(0)了,如果还createRow就会把第一列的内容清了
Cell cell0 = row.createCell(0);
cell0.setCellValue( 10000 );
cell0.setCellType(CellType.NUMERIC);
cell0.setCellStyle(baseCellStyle);

Cell cell1 = row.createCell(1);
cell1.setCellValue( "中国我爱你" );
cell1.setCellStyle(colorCellStyle);

Cell cell2 =row.createCell(2, CellType.NUMERIC);
cell2.setCellValue(3.0);  
cell2.setCellStyle(baseCellStyle);

XSSFRichTextString rich=new XSSFRichTextString("中华人民共和国");
rich.applyFont(0, 3, redFont);
//rich.applyFont(font);
Cell cell3=row.createCell(3);
cell3.setCellValue(rich);  


Cell cell4=row.createCell(4); 
cell4.setCellStyle(dateCellStyle);
cell4.setCellValue(new Date());

workbook.write(out);
out.close();

 


//读
InputStream inp =file.getInputStream();
Workbook wb = WorkbookFactory.create(inp);
Sheet sheet = wb.getSheetAt(0);
int  total=sheet.getLastRowNum();
Iterator<Row> i=sheet.rowIterator();
while(i.hasNext())
{
	//Row row = sheet.getRow(i++);
	Row row=i.next(); 
 Cell cell = row.getCell(0);  
 System.out.println(readCellValue(cell));  
 System.out.println(readCellValue( row.getCell(1)));
 System.out.println(readCellValue( row.getCell(2)));
          
}

public  static Object readCellValue(Cell cell)
	{
		CellType type=cell.getCellType();
		Object res=null;
		switch (type)
    	{
    		case NUMERIC :
    			if (HSSFDateUtil.isCellDateFormatted(cell)) {//读日期格式
         res=cell.getDateCellValue(); 
         break;
    	  }  
    			res=cell.getNumericCellValue();
    			break; 
    		case  STRING:
    			res=cell.getStringCellValue();
       break;
    		case  FORMULA: //公式
    			//res=cell.getCellFormula();
       		res=cell.getStringCellValue();
    			break;
    	}
		return res;
	}
 
public static void wirteContraintData() throws Exception
	{
		Workbook 	workbook=new XSSFWorkbook();
		FileOutputStream out=new FileOutputStream("/tmp/workbook.xlsx"); 
		Sheet sheet = workbook.createSheet();  
		
		sheet.protectSheet("");//不可编辑1 保护工作表，的密码
		//如保允许增加，删除行？？？？
		
		CellStyle baseCellStyle = workbook.createCellStyle();  
		baseCellStyle.setLocked(false);//不可编辑2 默认值为true,保护工作表后生效
		//单元格锁定样式
	    CellStyle lockCellStyle=workbook.createCellStyle(); 
	    lockCellStyle.cloneStyleFrom(baseCellStyle); 
	    lockCellStyle.setLocked(true);//只读单元格 
	    int noEditCol=2;
		 sheet.setDefaultColumnStyle(noEditCol, lockCellStyle);//不可编辑3,列级别
				
		
		 DataValidationHelper help = new XSSFDataValidationHelper((XSSFSheet)sheet);
		 int maxRows=1048576 -1 ;//excel 2010 最长 1048576 -1 =0xFFFFF
		//int maxRows=0xFFFFF;
		 {//下拉列表
				int columnNum=0;
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) help.createExplicitListConstraint(new String[] {"未知","男","女"});
//				XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(new String[] {"未知","男","女"});
				CellRangeAddressList regions=new CellRangeAddressList(1,maxRows,columnNum,columnNum);//(int firstRow, int lastRow, int firstCol, int lastCol)
				DataValidation validation = help.createValidation(constraint, regions);
				 
				//如输入不合法，不可输入
			    validation.setEmptyCellAllowed(true); //可为空
		        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);  
		        validation.setShowPromptBox(true);
				validation.createErrorBox("Error", "请下拉选择值");
			    validation.setShowErrorBox(true);
//		      validation.setSuppressDropDownArrow(true);
		        
				sheet.addValidationData(validation);  
		 }
		 {
			 //数字范围 
			 int columnNum=1; 
//	        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) help.createNumericConstraint( DVConstraint.ValidationType.INTEGER, DVConstraint.OperatorType.BETWEEN, "10", "100" ) ; 
	        XSSFDataValidationConstraint dvConstraint= new XSSFDataValidationConstraint(DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0.00", "100.00");
			CellRangeAddressList addressList = new CellRangeAddressList(1, maxRows, columnNum, columnNum);
			XSSFDataValidation validation = (XSSFDataValidation) help.createValidation(dvConstraint, addressList);

			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);  
			validation.setShowPromptBox(true);
			validation.createErrorBox("Error", "小数范围从0-100");
			validation.setShowErrorBox(true);  
//			validation.setSuppressDropDownArrow(true);
			
			sheet.addValidationData(validation);  
		 }
		
		{//标题
			Row row = sheet.createRow(0); 
			Cell sexTitle = row.createCell(0);
			sexTitle.setCellValue( "姓别" );  
			
			Cell sexAge = row.createCell(1);
			sexAge.setCellValue( "年龄" ); 
		}
		{//数据
			//正常数据
			Row row1 = sheet.createRow(1); 
			Cell sexVal = row1.createCell(0);
			sexVal.setCellValue( "男" );
			sexVal.setCellStyle(baseCellStyle);
			
			Cell ageVal = row1.createCell(1);
			ageVal.setCellValue( 20 );
			ageVal.setCellStyle(baseCellStyle);
			
			Cell noEditVal = row1.createCell(2);
			noEditVal.setCellValue( "不可修改值" );
			noEditVal.setCellStyle(lockCellStyle);//不可编辑3,单元格级别
			//没有createCell列的也是不可编辑的
			
			//异常数据
			Row row2 = sheet.createRow(2); 
			sexVal = row2.createCell(0);
			sexVal.setCellValue(  "x" );//还是可以写入的
			sexVal.setCellStyle(baseCellStyle);
			
			ageVal = row2.createCell(1);
			ageVal.setCellValue( -10 );//还是可以写入的
			
//			ageVal.setCellStyle(baseCellStyle);
			ageVal.setCellType(CellType.STRING);//修改单元格内容后，还是会变数据，这种不会，影响生成的excel的单元格式，
			
			CellStyle numberStringStyle = workbook.createCellStyle(); 
			DataFormat  format = workbook.createDataFormat();
			numberStringStyle.setDataFormat(format.getFormat("@"));
			ageVal.setCellStyle(numberStringStyle);//生成的excel的单元格式是文本格式
			
		} 
		workbook.write(out);
		out.close();
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
# %-15c{1}  15宽度左对齐,只要类名 %M 方法名
log4j.appender.console.Encoding=UTF-8
log4j.appender.dailyRollingFile=org.apache.log4j.DailyRollingFileAppender
log4j.appender.dailyRollingFile.file=${log_home}/dailyRollingFile.log
log4j.appender.dailyRollingFile.DatePattern='.'yyyy-MM-dd

log4j.appender.rollingFile=org.apache.log4j.RollingFileAppender
log4j.appender.rollingFile.File=${log_home}/rollingFile.log
log4j.appender.rollingFile.Append=true
log4j.appender.rollingFile.MaxFileSize=20MB
log4j.appender.rollingFile.MaxBackupIndex=10

zookeeper,kafka 是用log4j1版本

log4j.xml

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
 

    <dependencies>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.11.0</version>
      </dependency>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.11.0</version>
      </dependency>
	  
	<dependency>
	  <groupId>org.apache.logging.log4j</groupId>
	  <artifactId>log4j-slf4j-impl</artifactId>
	  <version>2.11.1</version>
	</dependency>

    </dependencies>
	
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


	
 
---------------------------------SLF4J
替代 Spring 使用的 commons-logging 加 jcl-over-slf4j-1.7.6.jar

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.7.25</version>
</dependency>

使用SLF4J(Simple Logging Facade for Java)做日志,为多种日志框架,默认是log4j
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory; 

Logger logger = LoggerFactory.getLogger(My.class); 
logger.info("Today is {}, Temperature set to {}", new Object[]{new java.util.Date(), 20});

logger.error("文件找不到",new FileNotFoundException("/test.txt"));//传入Throwable,会在日志有异常堆栈

---------------------------------logback 依赖于 SLF4J
可以把 org.springframework.context 简化为o.s.c   ,INFO 变为 I , 日志量变少

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version> 
</dependency>


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

SizeAndTimeBasedFNATP is deprecated. Use SizeAndTimeBasedRollingPolicy instead
https://logback.qos.ch/manual/appenders.html#SizeAndTimeBasedRollingPolicy

 <appender name="bizRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
	    <file>${APP_BIZ_HOME}/biz.log</file>
	    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
	      <fileNamePattern>${APP_BIZ_HOME}/biz-%d{yyyy-MM-dd}.%i.log</fileNamePattern> <!-- .zip 可有可无,压缩会节约20倍的空间-->
	       <maxFileSize>100MB</maxFileSize>    
	       <maxHistory>30</maxHistory>
	       <totalSizeCap>10GB</totalSizeCap>
	    </rollingPolicy>
	    <encoder>
	      <pattern>[%contextName] %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	    </encoder>
	 </appender>
  
	<!-- 变量值可再引用变量   :- 表示变量如未赋值使用后的面的默认值  -->
	 <appender name="daoRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
	    <file>${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao.log</file>
	    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
	      <fileNamePattern>${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao-%d{yyyy-MM-dd}.%i.zip</fileNamePattern> 
	       <maxFileSize>100MB</maxFileSize>    
	       <maxHistory>30</maxHistory>
	       <totalSizeCap>10GB</totalSizeCap>
	    </rollingPolicy>
	    <encoder>
	      <pattern>[%contextName] %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	    </encoder>
	 </appender> 

<appender name="serviceOutRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${APP_SERVICEOUT_HOME}/serviceout.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <fileNamePattern>${APP_SERVICEOUT_HOME}/serviceout_%d{yyyyMMdd}.log</fileNamePattern> 
       <maxFileSize>100MB</maxFileSize>    
       <maxHistory>30</maxHistory>
       <totalSizeCap>10GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
      <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] %msg%n </pattern>
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




logback可以把日志推送给logstash  
https://github.com/logstash/logstash-logback-encoder/blob/master/README.md

<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>6.1</version>
</dependency> 

import net.logstash.logback.argument.StructuredArguments;
 
		Map<String,String> myMap=new HashMap<>(); 
		myMap.put("name1", "value1");
		myMap.put("name2", "value2");
		StructuredArgument structArg=StructuredArguments.entries(myMap);
		System.out.println(structArg);//toString 就是正常的json对象
		if(LOG.isInfoEnabled())
		{
			//logback.xml中要使用 LogstashEncoder 或者 Composite Encoder加arguments参数
			LOG.info("define",structArg);//这种没有{}，即无参数，只JSON输出, 不格式化消息
			//调用后，Kibana日志就会有Map中的字段 
		}

<!--  logback可以把日志推送给 logstash  -->
<appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
 <writeBufferSize>16384</writeBufferSize> <!-- 默认缓存是8192 -->
 <destination>127.0.0.1:4560</destination><!-- 可以配置多个destination标签，或一个标签多个值用,分隔-->
  <keepAliveDuration>5 minutes</keepAliveDuration>
  
  <!-- LogstashEncoder输出( kibana查看)信息有，@timestamp时间 ,host主机，level级别,message日志信息，thread_name线程，logger_name(有时不是类名)
  	但没有类名，方法名，行号？？
 -->
 <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder">
 	<customFields>{"AppName":"myProject"}</customFields> <!-- 表示输出的json多加一个字段 -->
 </encoder>
 
 <!--  
 <arguments ></arguments>为代码中使用StructuredArgument用的
  LoggingEventCompositeJsonEncoder  %使用logback的PatternLayout格式  
		  "position": "%class.%method:%-3L",   // kibana显示？ %class{-20}也不行 不认？？
		    官方上的"line_str": "%line", 也不行？？？
		
		<encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
		  <providers>
		    <timestamp/>
		    <pattern>
		      <pattern>
		        {
		        "AppName": "myProject",  
		        "LO": "%logger",
		        "LE": "%level",
		        "TH": "%thread",
		        
		        "line_str": "%line",
		        "position": "%class{-20} . %method : %-3L",
		        
		        "ME": "%message"
		        }
		      </pattern>
		    </pattern>
		  </providers>
		</encoder>
	    -->	 
</appender>
 
 
 
logstash.conf 的(为logback) 输入配置 
input {
    tcp {
        port => 4560
        codec => json_lines
    }
}
output默认就是本机的elasticsearch

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


-------------------------------commons-lang
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
									join(listArray,",");
									
-------------------------------commons-lang3
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.8.1</version>
</dependency>

-------------------------------commons logging 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
Log logger = LogFactory.getLog(XXX.class);

-------------------------------commons codec 

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


---------------------------------commons beanutils
import org.apache.commons.beanutils.BeanUtils;
 
PersonBean dest = new PersonBean();
UserBean orig =new UserBean();
orig.setAge(20);
orig.setBirthday(new Date());
orig.setName("lisi");
 
BeanUtils.copyProperties(dest, orig); // commons.beanutils 和 spring都有，双方不同类都有匹配不上的字段也可正常用
 	
 ConvertUtils.register(new Converter()
	 {
		  
		 @Override
		 public Object convert(Class arg0, Object arg1)
		 {
			 if(arg1 == null)
			 {
				 return null;
			 }
			 if(!(arg1 instanceof String))
			 {
				 throw new ConversionException("只支持字符串转换 !");
			 }
			  
			 String str = (String)arg1;
			 if(str.trim().equals(""))
			 {
				 return null;
			 }
			  
			 SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 try {
				return sd.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 return null;
		 }
		  
	 }, java.util.Date.class);//map要只字串，可转成日期
	 
	PersonBean person = new PersonBean();
	Map<String, Object> mp = new HashMap<String, Object>();
	mp.put("name", "Mike");
	mp.put("age", 25);
	mp.put("mN", "male");
	mp.put("birthday", "2017-08-20 14:20:10");	
	BeanUtils.populate(person, mp); //org.apache.commons.beanutils
  
  
//也可用 java.beans.Introspector 做  Map --> Bean , Bean -> Map
BeanUtils.populate(obj, map); // Map --> Bean 

 
 
------------------------------Bcrypt Java实现 jBCrypt
http://www.mindrot.org/projects/jBCrypt/
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
// Hash a password for the first time
String password = "testpassword";
String hashed = BCrypt.hashpw(password, BCrypt.gensalt());//每次生成的结果不一样，但可以认证成功
System.out.println(hashed);
// gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt(12));
System.out.println(hashed2);
// Check that an unencrypted password matches one that has
// previously been hashed
String candidate = "testpassword";
// String candidate = "wrongtestpassword";
if (BCrypt.checkpw(candidate, hashed))
	System.out.println("It matches");
else
	System.out.println("It does not match");

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
org.apache.commons.io.IOUtils //closeQuietly 过时了
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
			if("".equals(pathName))
			    continue;
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

-----------------------------commons pool
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.6.0</version>
</dependency>

GenericObjectPoolConfig  (jedis使用这个)

GenericObjectPoolConfig<StringBuffer> config=new GenericObjectPoolConfig<StringBuffer>();

config.setMaxTotal(20);
config.setMaxIdle(10);
config.setMinEvictableIdleTimeMillis(300000);
config.setNumTestsPerEvictionRun(3);
config.setTimeBetweenEvictionRunsMillis(60000);
config.setTestOnBorrow(false);
config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//逐出连接的最小空闲时间 默认
config.setMinEvictableIdleTimeMillis(20*60*1000);//20分

GenericObjectPool<StringBuffer> pool=new GenericObjectPool<StringBuffer>(new StringBufferFactory(),config);
StringBuffer buf = pool.borrowObject();
//....
pool.returnObject(buf);

PooledObject<Connection> obj=new DefaultPooledObject<Connection>(conn);
Connection conn= obj.getObject();
obj.getObject().close();

-----------------------------httpclient 新的是httpcomponents项目下的
httpcore -> httpclient

<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpcore</artifactId>
	<version>4.4.10</version>
</dependency>

<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.5.6</version>
</dependency>

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
-----------------------------websocket Java Client(Java-WebSocket)
spring webflux 有 WebSocketClient

https://github.com/TooTallNate/Java-WebSocket
Java 1.6 and higher
Android 4.0 and higher

支持wss:协议(certificate)

<dependency>
    <groupId>org.java-websocket</groupId>
    <artifactId>Java-WebSocket</artifactId>
    <version>1.4.0</version>
</dependency>

//websocket 做客户端
WebSocketClient	cc = new WebSocketClient( new URI( "ws://localhost:8080/J_JavaEE/webSocket")  )
		{
			@Override
			public void onMessage( String message ) {
				System.out.println( "got: " + message + "\n" );
			}
			@Override
			public void onOpen( ServerHandshake handshake ) {
				System.out.println( "You are connected to ChatServer: " + getURI() + "\n" );
			}
			@Override
			public void onClose( int code, String reason, boolean remote ) {
				System.out.println( "You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n" );
			}
			@Override
			public void onError( Exception ex ) {
				System.out.println( "Exception occured ...\n" + ex + "\n" );
				ex.printStackTrace();
			}
		};
		cc.connect();
		Thread.sleep(2*1000);//等连接完成
		cc.send( "java_websocket 端 消息" );
		System.out.println( "已经向服务端发送消息");
		Thread.sleep(2*1000);//等服端回消息
		cc.close();


-----------------------------Quartz 
 <dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz</artifactId>
  <version>2.2.3</version>
</dependency>
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz-jobs</artifactId>
  <version>2.2.3</version>
</dependency>  

使用c3p0数据源


@DisallowConcurrentExecution
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
			//.usingJobData(dataKey, value)
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
//			.withRepeatCount(10))//假期是当天就不能启动了
		      
		.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * 8-17 * * ?"))
		//.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(3))
		//.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16, 0))
		//.forJob("myJob", "group1");
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

		  
		  
		  
//		  sched.addJob(job,true);
//		  sched.scheduleJob(trigger);//triggerBuild.forJob
		  
//		  TriggerKey triggerKey=new TriggerKey("myTrigger","group1");
//		  sched.unscheduleJob(triggerKey);//删任务 
		  
//		  JobKey jobKey=new JobKey("myJob","group1");
//		  sched.checkExists(jobKey);
		  
	  
	/*	  List<Trigger> triggers=(List<Trigger>)sched.getTriggersOfJob(jobKey);//查某个任务的所有定时
		  for(Trigger trig:triggers)
		  {
			  TriggerKey triggerKey=trig.getKey();
			  TriggerState triggerState=sched.getTriggerState(triggerKey);
			  System.out.println(triggerState);//enum ,COMPLETED
		  }
	  */
//		sched.triggerJob(jobKey);//立即启动任务
//		sched.resumeTrigger(triggerKey);//启用
//		sched.pauseTrigger(triggerKey);//暂停

	/*	  
		  GroupMatcher<JobKey> matcher=GroupMatcher.groupEquals("group1");//查所有存的任务
		  Set<JobKey> jobs=sched.getJobKeys(matcher);
		  for(JobKey key:jobs)
		  {
			  JobDetail detail=sched.getJobDetail(key);
			  detail.getClass();
			  detail.getDescription();
			  key.getName();
			  key.getGroup(); 
		  }
		*/
		  
//-------使用配置文件方式	
//分布式 配置 org.quartz.jobStore.isClustered = true
	  
//org/quartz/quartz.properties文件,可以被src\下的文件覆盖,示例在quartz-2.2.3\examples\example10\quartz.properties
//quartz.properties中插件配置去读 quartz_data.xml
//org/quartz/xml/job_scheduleing_data_2_0.xsd
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //会读classpath 下的quartz.properties
scheduler.start(); 
scheduler.shutdown(); 

org.quartz.impl.jdbcjobstore.JobStoreCMT containerManageTransaction;
org.quartz.impl.jdbcjobstore.oracle.OracleDelegate oracle;


//--- quartz.properties 文件(放在classpath路径下) 
#如使用了Spring不会默认读 classpath下的quartz.properties , 要配置quartzProperties属性

org/quartz/quartz.properties 中的值是是以:分隔 ,也可以的
org.quartz.threadPool.threadCount: 10

动态配置cronExpression ,类	extends CronTriggerBean
	setCronExpression(cronExpression)

--- quartz.properties JDBC存储 和 Clustering 配置 

#============================================================================
# Configure Main Scheduler Properties  
#============================================================================

org.quartz.scheduler.instanceName = MyClusteredScheduler
org.quartz.scheduler.instanceId: AUTO

org.quartz.scheduler.skipUpdateCheck: true

#============================================================================
# Configure ThreadPool  
#============================================================================

org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 25
org.quartz.threadPool.threadPriority = 5

#============================================================================
# Configure JobStore  
#============================================================================

org.quartz.jobStore.misfireThreshold: 60000
org.quartz.jobStore.class: org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate


org.quartz.jobStore.useProperties: false
org.quartz.jobStore.dataSource: myDS
org.quartz.jobStore.tablePrefix: QRTZ_ 

org.quartz.jobStore.isClustered = true
org.quartz.jobStore.clusterCheckinInterval = 20000
#============================================================================
# Configure Datasources  
#============================================================================

org.quartz.dataSource.myDS.driver: com.mysql.cj.jdbc.Driver
org.quartz.dataSource.myDS.URL: jdbc:mysql://localhost:3306/mydb
org.quartz.dataSource.myDS.user: user1
org.quartz.dataSource.myDS.password: user1 
org.quartz.dataSource.myDS.maxConnections: 5
org.quartz.dataSource.myDS.validationQuery=select 1



---------------------------------Netty 4
Netty 4 是以io.netty开头的包
Netty 3 是以org.jboss.netty开头的包

Ratpack 基于 Netty (event-driven) 高性能的 零拷贝

JBoss的 NIO 框架 要求JDK6及以上版本， 很少有类基于 java nio
NioServerSocketChannel , NioSocketChannel 用了 java nio
//---- Netty-4.1
netty-all-4.1.8.Final-sources.jar 里有example
 <dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.43.Final</version>
</dependency>

jdk8是可以的，但jdk11启动示例报错

new NioEventLoopGroup(1);//线程数，每个NioEventLoopGroup有一个selector

ChannelPipeline 是Handler （使用ChannelHandlerContext来串起来）的集合

 .option(ChannelOption.SO_BACKLOG, 100) //连接时 当服务端的线程池用完，用来设置队列的大小
 .childOption(ChannelOption.SO_KEEPALIVE,true)
 
 	SimpleChannelInboundHandler
 extends ChannelInboundHandlerAdapter { //In是读，对应的有Out
 	//一般要重写这几个方法
  	@Override
    public void channelActive(ChannelHandlerContext ctx) {
        //  ctx.writeAndFlush(firstMessage);//客户端用
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
        cause.printStackTrace();
        ctx.close();
    }
 }


Bootstrap 用于客户端 group函数要1个参数

ServerBootstrap 用于服务端 group函数要两个参数

ByteBuf ,可用 Unpooled.copiedBuffer("测试".getBytes(CharsetUtil.UTF_8))


ChannelHandlerContext .writeAndFlush()







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
https://github.com/tesseract-ocr/tesseract 
Tess4J


------------指纹识别  人脸识别
SURF (Speeded Up Robust Features, 加速稳健特征) 实现

openCV , Android人脸识别 (Face++)
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
         xsi:noNamespaceSchemaLocation="ehcache.xsd" updateCheck="false"
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

----------------ehcache 3.x (org.ehcache支持分布式)
 
<dependency>
  <groupId>org.ehcache</groupId>
  <artifactId>ehcache</artifactId>
  <version>3.6.1</version>
</dependency>

 //JCache (JSR-107)

 <dependency>
  <groupId>javax.cache</groupId>
  <artifactId>cache-api</artifactId>
  <version>1.1.0</version>
</dependency>
static void echcache3x()
{
	//classpath 中不能有ehcache 2.9
	 org.ehcache.CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
					  .withCache("preConfigured",
						   CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
														  ResourcePoolsBuilder.heap(100))
						   .build())
					  .build(true);

	 org.ehcache.Cache<Long, String> preConfigured
					  = cacheManager.getCache("preConfigured", Long.class, String.class);

	 org.ehcache.Cache<Long, String> myCache = cacheManager.createCache("myCache",
					  CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
													ResourcePoolsBuilder.heap(100)).build());
				  myCache.put(1L, "da one!");
				  String value = myCache.get(1L);

				  cacheManager.close();
}

 static void jcache()
{
	 //JCache (JSR-107) classpath中不有多个实现，去 redisson.jar
	CachingProvider provider = Caching.getCachingProvider();  
	
	//---CacheManager 方式一
	DefaultConfiguration defConfiguration = new DefaultConfiguration(provider.getDefaultClassLoader(),
			  new DefaultPersistenceConfiguration(new File("/tmp/ehcache_dist/"))); 
	javax.cache.CacheManager cacheManager =provider.getCacheManager(provider.getDefaultURI(),provider.getDefaultClassLoader());  
	 
	//---CacheManager 方式 二
	//javax.cache.CacheManager cacheManager = provider.getCacheManager();   
	
	
	MutableConfiguration<Long, String> configuration =
		new MutableConfiguration<Long, String>()  
			.setTypes(Long.class, String.class)   
			.setStoreByValue(false)   
			.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(javax.cache.expiry.Duration.ONE_MINUTE));  
	javax.cache.Cache<Long, String> cache = cacheManager.createCache("jCache", configuration); 
	cache.put(1L, "one"); 
	String value = cache.get(1L); 
}
-------------------------------caffeine  cache

 
compile 'com.github.ben-manes.caffeine:caffeine:2.6.2'
<dependency>
	<groupId>com.github.ben-manes.caffeine</groupId>
	<artifactId>caffeine</artifactId>
	<version>2.6.2</version>  
</dependency>

 扩展中也提供 JCache 实现
compile 'com.github.ben-manes.caffeine:jcache:2.6.2'
<dependency>
	<groupId>com.github.ben-manes.caffeine</groupId>
	<artifactId>jcache</artifactId>
	<version>2.6.2</version>  
</dependency>

LoadingCache<String, Object> graphs = Caffeine.newBuilder()
	.maximumSize(10_000)
	.expireAfterWrite(5, TimeUnit.MINUTES)
	.refreshAfterWrite(1, TimeUnit.MINUTES)
	.build(new  CacheLoader<String, Object>() {
		@Override
		public Object load(String arg0) throws Exception {
			
			return null;
		}
	});
	//.build(key -> createExpensiveGraph(key));
	
---------------------------------Guava 缓存部分
Caffeine is a Java 8 rewrite of Guava’s cache

<dependency>
	<groupId>com.google.guava</groupId>
	<artifactId>guava</artifactId>
	<version>27.0-jre</version>   <!--  27.0-jre , 27.0-android -->
</dependency>

LoadingCache<String,Object> dictCache = CacheBuilder.newBuilder()
	.maximumSize( 1000) 
	.expireAfterWrite(1, TimeUnit.DAYS)
	.concurrencyLevel( 5 )
	.build(new CacheLoader<String,Object>() {
		@Override
		public String load(String key) throws Exception {
			return null;
		}
	});
dictCache.put("key", "value");
Object o= dictCache.getIfPresent("key");
System.out.println(o);

dictCache.invalidate("key");

o= dictCache.getIfPresent("key");
System.out.println(o);
	

---------------------------------Guava  限流 RateLimiter

---------------------------------Redis client Jedis (spring使用这个)
https://github.com/xetorthio/jedis

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
jedis.set("foo", "bar");//string
String foobar = jedis.get("foo");
//zset
jedis.zadd("sose", 0, "car");//0是score
jedis.zadd("sose", 0, "bike"); 
Set<String> sose = jedis.zrange("sose", 0, -1);//score 范围
System.out.println(sose);

jedis.sadd("myset","mysetval");//set
jedis.lpush("mylist", "one");//list
jedis.lpush("mylist", "two");
jedis.hset("myhashStuScore", "zhang", "A");//hash
jedis.hset("myhashStuScore", "lisi", "B");

jedis.close();//一定要close 
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
 

--shard 通过一致性哈希算法决定把数据存到哪台上,算是一种客户端负载均衡
JedisPoolConfig config=new JedisPoolConfig();
config.setMaxIdle(20);
config.setMaxTotal(30);
config.setMaxWaitMillis(5*1000);
config.setTestOnBorrow(false);
config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//逐出连接的最小空闲时间 默认
config.setMinEvictableIdleTimeMillis(20*60*1000);//20分



JedisShardInfo shardInfo =new JedisShardInfo(ip,port);//passwd
ShardedJedisPool shardedPool=new ShardedJedisPool(config,Arrays.asList(shardInfo)) ;
ShardedJedis shardedJedis=shardedPool.getResource();
//put-key
shardedJedis.set("user1", "用户1");
shardedJedis.expire("user1",20*60);//单位秒，20分
//setnx,incr
shardedJedis.set("user2", "用户2");

ShardedJedisPipeline pipeline = shardedJedis.pipelined();  
pipeline.set("trainNo1_SH", "20");  
pipeline.set("trainNo1_SZ", "10");  
List<Object> res = pipeline.syncAndReturnAll();
System.out.println(res);//[OK, OK]
//del-key
String delKeyLike="user";
Collection<Jedis> jedisCollect = shardedJedis.getAllShards();  
Iterator<Jedis> iter = jedisCollect.iterator();  
while (iter.hasNext()) 
{  
  Jedis jedis = iter.next();  
  Set<String> keys = jedis.keys(delKeyLike + "*");  
  jedis.del(keys.toArray(new String[keys.size()]));  
}   
shardedJedis.close();//一定要close



//官方说lua脚本的原子性 Atomicity of scripts,如脚本正在执行，其它命令或脚本不能执行
//lua脚本实现分布式锁，可以保存证setnx,expire两个操作的原子性 
//分布式锁,redis事务没有隔离性
@Test 
public void testRedisLuaDistributeLock()
{ 
	InputStream input=TestJedis.class.getResourceAsStream("/redis_jedis/lock.lua");
	
	StringBuilder strBuilder=new StringBuilder();
	 Scanner scanner =new Scanner(input);
	 while(scanner.hasNextLine())
	 {
	 	String line=scanner.nextLine();
	 	strBuilder.append(line).append("\n");
	 } 
	Object res=jedis.eval(strBuilder.toString(),1,"lockExport","user1","30");//参数 键名,值，超时秒
	System.out.println(res);
}

------lock.lua
-- 单行注释
--[[
 多行注释
没办法在这里暂停
 --]]  

 -- SETNX 成功设置返回1,失败设置返回0
local  isSet = redis.call('SETNX', KEYS[1], ARGV[1])
 if isSet == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[2]) 
   return "success"
 end
 return "fail"



---------------------------------Redis client  lettuce Spring用这个
 <dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>5.0.4.RELEASE</version>
</dependency>
使用reactor,netty


//注意 jboss-client.jar了有netty的类
//		RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
//		RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
//		RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));

		RedisURI redisUri =RedisURI.Builder.redis("localhost", 6379).withPassword("").withDatabase(1)
				//.withSsl(true)
				.build();
		RedisClient redisClient = RedisClient.create(redisUri);
		
		//RedisAsyncCommands<String, String> commands = client.connect().async();
		//RedisFuture<String> future = commands.get("key");
		
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> syncCommands = connection.sync();

		syncCommands.set("key", "Hello, Redis!");

		connection.close();
		redisClient.shutdown();
		
public void testShowAllKeyValues() 
{
	Set<String> keys=jedis.keys("*");
	for(String key:keys)
	{
		String type=jedis.type(key);//set,zset,list,hash
		System.out.println("type:"+type+",key="+key);
		if("zset".equals(type))
		{
			Set<String> zsets=jedis.zrange(key, 0, -1); 
			System.out.println("\t value== \t ");
			for(String zset:zsets)
				System.out.println(" \t value="+zset+",score="+jedis.zcard(zset));
		}else if ("set".equals(type))
		{
			Set<String> values=jedis.smembers(key); 
			System.out.println("\t value== \t ");
			for(String value:values)
				System.out.println(" \t value="+value);
		}else if("list".equals(type))
		{
			List<String> values=jedis.lrange(key, 0, -1); 
			System.out.println("\t value== \t ");
			for(String value:values)
				System.out.println(" \t value="+value);
		}else if("hash".equals(type))
		{
			Set<String> hkeys=jedis.hkeys(key); 
			System.out.println("value== \t ");
			for(String hkey:hkeys)
				System.out.println(" \t \t "+hkey+"="+jedis.hget(key, hkey));
		}else if("string".equals(type))
		{
			String value=jedis.get(key); 
			System.out.println("\t value ="+value);
		}else
		{
			System.out.println("\t value =...." );
		}
	}
}
		
jedis.close();
jedis.shutdown();//会把redis服务器关了 
	
---------------------------------Redis client redisson	  分布式锁的实现 
 //JCache (JSR-107)
<dependency>
   <groupId>org.redisson</groupId>
   <artifactId>redisson</artifactId>
   <version>3.9.1</version>
</dependency>  

https://github.com/redisson/redisson/wiki/ 有中文的文档

//redisson  依赖于netty,fasterxml的jackson


Config config = new Config();
//--单机 
//SingleServerConfig singConfig= config.useSingleServer();
//singConfig.setAddress("redis://127.0.0.1:6379");

//--cluster配置
MasterSlaveServersConfig  msConfig=config.useMasterSlaveServers();
msConfig.setMasterAddress(masterIPPort);
msConfig.addSlaveAddress(slaveIPPort);//可传多个node

//RedissonClient redisson = Redisson.create();//默认 redis://127.0.0.1:6379
RedissonClient redisson = Redisson.create(config);
RKeys keys=redsson.getKeys();
 Iterable<String> iter=keys.getKeys();
 iter.forEach(new Consumer<String>()  //回调的要等才行
 {
	@Override
	public void accept(String key) {
		System.out.println("key="+key);  
	}
});

//---Distributed Object storage example
RBucket<AnyObject> bucket = redisson.getBucket("anyObject");
//bucket.set(new AnyObject());//单机OK,但cluster master 卡住???
bucket.setAsync(new AnyObject());//单机OK,但cluster master get时卡住???
AnyObject obj = bucket.get();

redisson.shutdown();
 

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


-----------------apache dubbo 2.7.x
(包名org.apache.dubbo开头)
https://github.com/apache/dubbo
https://github.com/apache/dubbo-admin
http://dubbo.apache.org/en-us/  
http://start.dubbo.io  里面用的还是com.alibaba 不是最新的 (2019-11-20)

有使用Unsafe类，如JDK11报 because module java.base does not export jdk.internal.misc  要用JDK1.8
 
	
	
 <dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-config-spring</artifactId>
    <version>2.7.4.1</version>
</dependency>
 <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-registry-zookeeper</artifactId>
   <version>2.7.4.1</version>
</dependency>

<dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-rpc-dubbo</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  
  <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-remoting-netty4</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  
  <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-serialization-hessian2</artifactId>
   <version>2.7.4.1</version>
  </dependency>
  <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo-configcenter-zookeeper</artifactId>
      <version>2.7.4.1</version>
  </dependency>
  <dependency>
   <groupId>org.apache.dubbo</groupId>
   <artifactId>dubbo-serialization-hessian2</artifactId>
   <version>2.7.4.1</version>
  </dependency>

//--common
package apache_dubbo27;
public interface GreetingService {
    String sayHi(String name);
}
//--server

如使用spring.xml 不能使用 dubbo-2.7.4.1.jar (xml名称空间为alibabatech)
 dubbo-config-api-2.7.4.1.jar
	dubbo-common-2.7.4.1.jar
	dubbo-rpc-api-2.7.4.1.jar
		dubbo-rpc-dubbo-2.7.4.1.jar
		dubbo-remoting-api-2.7.4.1.jar
		dubbo-rpc-injvm-2.7.4.1.jar
		dubbo-monitor-api-2.7.4.1.jar
		dubbo-remoting-netty4-2.7.4.1.jar
		dubbo-serialization-api-2.7.4.1.jar
		dubbo-serialization-hessian2-2.7.4.1.jar
			hessian-lite-3.2.6.jar(alibaba的hession2)
	dubbo-registry-api-2.7.4.1.jar
		dubbo-registry-zookeeper-2.7.4.1.jar
	dubbo-configcenter-api-2.7.4.1.jar
	dubbo-configcenter-zookeeper-2.7.4.1.jar
	dubbo-remoting-zookeeper-2.7.4.1.jar
	dubbo-cluster-2.7.4.1.jar
	
package apache_dubbo27_server;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig; 
import apache_dubbo27.GreetingService; 
import java.util.concurrent.CountDownLatch;
public class Application {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");
    public static void main(String[] args) throws Exception {
        ServiceConfig<GreetingService> service = new ServiceConfig<>();
        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        service.setInterface(GreetingService.class);
        service.setRef(new GreetingsServiceImpl());
        service.export();
        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}

package apache_dubbo27_server;
import apache_dubbo27.GreetingService;
public class GreetingsServiceImpl implements GreetingService {
    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}
//--client
package apache_dubbo27_client;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import apache_dubbo27.GreetingService;
public class Application { 
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");
    public static void main(String[] args) {
        ReferenceConfig<GreetingService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingService.class);
        GreetingService service = reference.get();
        String message = service.sayHi("dubbo");
        System.out.println(message);
    }
}

如使用spring 加 dubbo-config-spring-2.7.4.1.jar

QoS，全称为Quality of Service, 于动态的对服务进行查询和控制(对服务进行动态的上下线 ) 默认端口是22222

可telnet仿问也 http仿问 curl -i http://localhost:22222/ls
---dubbo.properties文件 
dubbo.application.qos.enable=true
dubbo.application.qos.port=33333
dubbo.application.qos.accept.foreign.ip=false   #是否允许远程访问

如xml配置方式
  <dubbo:application name="demo-provider">
    <dubbo:parameter key="qos.enable" value="true"/>
    <dubbo:parameter key="qos.accept.foreign.ip" value="false"/>
    <dubbo:parameter key="qos.port" value="33333"/>
  </dubbo:application>
  
注释@使用方式 如使用放 dubbo-metadata-report-api-2.7.4.1.jar

public class SpringAnnoDubboServer {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
        System.in.read();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "apache_dubbo27_server.anno")
    @PropertySource("classpath:/apache_dubbo27_server/anno/dubbo-provider.properties")
    static class ProviderConfiguration {
        @Bean
        public RegistryConfig registryConfig() {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress("zookeeper://127.0.0.1:2181");
            return registryConfig;
        }
    }
}
-- dubbo-provider.properties
dubbo.application.name=dubbo-demo-annotation-provider
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880





public class SpringAnnoDubboClient {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        DemoService service = context.getBean("demoServiceComponent", DemoServiceComponent.class);
        String hello = service.sayHello("world");
        System.out.println("result :" + hello);
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "apache_dubbo27_client.anno")
    @PropertySource("classpath:/apache_dubbo27_client/anno/dubbo-consumer.properties")
    @ComponentScan(value = {"apache_dubbo27_client.anno"})
    static class ConsumerConfiguration {

    }
} 
@Component("demoServiceComponent")
public class DemoServiceComponent implements DemoService {
    @Reference
    private DemoService demoService;

    @Override
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }

    @Override
    public CompletableFuture<String> sayHelloAsync(String name) {
        return null;
    }
}  
---dubbo-consumer.properties
dubbo.application.name=dubbo-demo-annotation-consumer
dubbo.registry.address=zookeeper://127.0.0.1:2181

--- Dubbo容错
Failover Cluster 模式  失败自动切换，当出现失败，重试其它服务器。(缺省)
	通常用于读操作,通过retries=”2”来设置重试次数(不含第一次) 
Failfast Cluster 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
Failsafe Cluster 失败安全，出现异常时，直接忽略。 通常用于写入审计日志等操作。
Failback Cluster 失败自动恢复，后台记录失败请求，定时重发。
Forking Cluster 并行调用多个服务器，只要一个成功即返回。 可通过forks=”2”来设置最大并行数。
Broadcast Cluster 广播调用所有提供者，逐个调用，任意一台报错则报错

------------alibaba dubbo  2.6.0   

2.6.0 版本(包名alibaba开头)的Dubbo ops组里的 dubbo-admin-2.0.0\WEB-INF\dubbo.properties 默认值如下
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.admin.root.password=root
dubbo.admin.guest.password=guest

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>dubbo</artifactId>
	<version>2.6.0</version>
</dependency>

----2.6.0 版本的Dubbo ops组里的 dubbo-springboot
 
----
zkclient
javassist
依赖 netty 3(org.jboss.netty) 和 4(io.netty.) 版本 ,mina

Dubbo将自动加载classpath根目录下的dubbo.properties，
可以通过JVM启动参数：-Ddubbo.properties.file=xxx.properties 改变缺省配置位置。

System.setProperty("dubbo.properties.file", "alibaba/dubbo/client/dubbo.properties");


---client/dubbo.properties

dubbo.application.name=MyProject1
dubbo.protocol.name=dubbo 

dubbo.registry.address=zookeeper://127.0.0.1:2181
#dubbo.registry.address=zookeeper://192.168.16.125:2181?backup=192.168.16.126:2181
#dubbo.spring.config=classpath*:alibaba/dubbo/dubbo-client.xml,classpath*:alibaba/dubbo/dubbo-server.xml

#for client
dubbo.reference.timeout=55000

#--only connect specical IP,only for dev enviroment   or url="127.0.0.1:20884" 

dubbo.reference.dubboFacade.url= dubbo://127.0.0.1:20884
 
---server/dubbo.properties
dubbo.application.name=MyProject1
dubbo.protocol.name=dubbo
dubbo.protocol.port=20884
dubbo.protocol.serialization=hessian2	
# dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json ,hessian2 序列化不支持反序列化 java.util.EnumSet ,可用kryo

#dubbo.protocol.heartbeat=60000

dubbo.registry.address=zookeeper://127.0.0.1:2181
#dubbo.registry.address=zookeeper://192.168.16.125:2181?backup=192.168.16.126:2181
#dubbo.spring.config=classpath*:alibaba/dubbo/dubbo-client.xml,classpath*:alibaba/dubbo/dubbo-server.xml

#for server
pref.log.time.max.limit=500


dubbo-client.xml  
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">
    <dubbo:reference id="dubboFacade"  interface="alibaba.dubbo.server.DubboFacade" check="false" retries="0"/> 
	<!-- 
  	 直连的单个配置 ,和dubbo.properties不同的是,只对某个有效
	 <dubbo:reference id="dubboFacade"  interface="alibaba.dubbo.server.DubboFacade" check="false" retries="0" url="127.0.0.1:20884" />
	-->	
	
	<dubbo:reference id="dubboGroupVersionFacade"  interface="alibaba.dubbo.server.DubboGroupVersionFacade" check="false" retries="0" 
	 group="group1"  version="1.0"
	 />
	 
   <dubbo:application name="AppNameInXml"/>
   <!-- 同  dubbo.application.name  -->
   
   <dubbo:registry address="zookeeper://127.0.0.1:2181" />
   <!-- 同  dubbo.registry.address=zookeeper://127.0.0.1:2181    官方文档还可以注册到redis上  
			register="true" 是否注册上zookeeper上,通过直连 
			file="dubboregistry/op-baseinfo-provider.properties"  Dubbo缓存文件
			 check="false" 半闭注册中心启动时检查  ,如果服务端没有启动,客户端不能能启动 
   -->
   
</beans>

dubbo-server.xml 接口方法参数类 implements Serializable
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo 
	http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">	
	
 
	<dubbo:service interface="alibaba.dubbo.server.DubboFacade" ref="dubboFacdeImpl"/>
	<!-- registry="$RegisterId" -->
	
	<bean id="dubboFacdeImpl"  class="alibaba.dubbo.server.DubboFacadeImpl" />
	
	<dubbo:service interface="alibaba.dubbo.server.DubboGroupVersionFacade" ref="dubboGroupVersionFacadeImpl"
 	 group="group1"  version="1.0"/>
 	 <!--  group="group1" 可 相同的接口多个不同的实现，用group区分
	 version="1.0.0" 当一个接口的实现，出现不兼容升级时(即删改属性，加是兼容的)，可以用版本号过渡，版本号不同的服务相互间不引用
	 -->
	<bean id="dubboGroupVersionFacadeImpl"  class="alibaba.dubbo.server.DubboGroupVersionFacadeImpl" />
 	
	
	
	<!--  
 	dubbo.protocol.name=dubbo
	dubbo.protocol.port=20884
    dubbo.protocol.serialization=hessian2	
    # dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json ,hessian2 序列化不支持反序列化 java.util.EnumSet 
	kryo(官方版本报错,pingan版本不报错)
	
  	<dubbo:protocol name="dubbo" port="20884"  serialization="kryo"/>
  -->
  
</beans>

可以使用 telent 127.0.0.1 28004 连接上用 ls看所有提供服务

dubbo consumer 负载均衡策略
1.Random  随机　按权重
2.RoundRobin 轮循 按权重
3.LeastActive  最少活跃调用数,相同活跃数的随机  ,调用前后计数差,慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大
4.ConsistentHash  一致性Hash  缺省只对第一个参数Hash，
	如果要修改，请配置<dubbo:parameter key="hash.arguments" value="0,1" />
	缺省用160份虚拟节点，如果要修改，请配置<dubbo:parameter key="hash.nodes" value="320" />
界面上有随机，轮询，最少并发


dubbo协议使用默认Hessian二进制序列化(netty),也可使用 kryo,通讯使用mina
Hessian协议   Dubbo缺省内嵌Jetty作为服务器实现
Thrift是Facebook捐给Apache
 
 
dubbo main 方法 com.alibaba.dubbo.container.Main 可实现安全关机,用JDK的ShutdownHook,
如kill 不带-9 Provider方可以先不接收请求,如有任务等待完成,Consumer方,如有请求没有返回的等待
eclipse启动用  com.alibaba.dubbo.container.Main 参数传 -Ddubbo.properties.file=alibaba/dubbo/server/dubbo.properties -Ddubbo.spring.config=classpath:alibaba/dubbo/server/dubbo-server.xml

------------dubbo    Thrift(跨语言,代码生成)  
dubbo 协议适合   小数据量大并发,netty3.2.2 + hessian -3.2.1
    
./configure && make (要  bison version >= 2.5  ,xz -d -k bision-3.0.tar .xz )



<dependency>
  <groupId>org.apache.thrift</groupId>
  <artifactId>libthrift</artifactId>
  <version>0.11.0</version>
</dependency>



 //---Hello.thrift
 namespace java apache_thrift.hello 
 service Hello{ 
  string helloString(1:string para) 
  i32 helloInt(1:i32 para) 
  bool helloBoolean(1:bool para) 
  void helloVoid() 
  string helloNull() 
 }
 
thrift --gen <language> <Thrift filename>
 -r  (recursivly)
	多语言支持
	C++
	Java
	Cocoa
	Python
	C#
	
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
	
------------protobuf
  google跨语言的序列化协议
https://blog.csdn.net/u011518120/article/details/54604615
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.5.1</version>
</dependency>
 
--persion.proto 文件 
syntax = "proto3";
option java_outer_classname = "PersonEntity";//生成的数据访问类的类名
package protobuf;   
message Person {  
    int32 id = 1; 
    string name = 2; 
    string email = 3; 
}  
protoc -I=. --java_out=. persion.proto   
protoc.exe -I=proto的输入目录 --java_out=java类输出目录 proto的输入目录包括包括proto文件


PersonEntity.Person.Builder builder = PersonEntity.Person.newBuilder();
builder.setId(1);
builder.setName("ant");
builder.setEmail("ghb@soecode.com");
PersonEntity.Person person = builder.build();
System.out.println("before :"+ person.toString());

//模拟接收Byte[]，反序列化成Person类
byte[] byteArray =person.toByteArray();
PersonEntity.Person p2 = PersonEntity.Person.parseFrom(byteArray);
System.out.println("after :" +p2.toString());

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


-------------JSON  已经有 javax.json
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
	<classifier>jdk15</classifier>
    <version>2.4</version>
</dependency>

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


JSONObject jsonObject = JSONObject.fromObject(userObject);
System.out.println("java Object to json : "+ jsonObject); 

JSONArray jsonArrasy = JSONArray.fromObject(userObject);
System.out.println("java Array to json : "+ jsonArrasy); 


//String->Object
//如对象里有一个List<Order>不能正确转换成集合中的对象,Order要有默认构造器  
Map<String, Class<Order>> classMap = new HashMap<>();
classMap.put("orders", Order.class);
UserModel userModel = (UserModel) JSONObject.toBean(JSONObject.fromObject(strJsonObj), UserModel.class,classMap);
//方式二
JsonConfig jsonConfig = new JsonConfig();  
jsonConfig.setRootClass(UserModel.class);  
jsonConfig.setClassMap(classMap);
userModel = (UserModel) JSONObject.toBean(JSONObject.fromObject(strJsonObj),jsonConfig);
		
System.out.println("userModel: "+ userModel); 

//String->Map
Map userMap = (Map) JSONObject.toBean(JSONObject.fromObject(strJsonMap), Map.class);
System.out.println("userMap: "+ userMap); 
------------json-path
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.4.0</version>
</dependency> 
依赖于 json-smart-2.3.jar
<dependency>
    <groupId>net.minidev</groupId>
    <artifactId>asm</artifactId>
    <version>1.0.2</version>
</dependency>

import com.jayway.jsonpath.JsonPath;

String json="...";
 
//输出book[0]的author值
String author = JsonPath.read(json, "$.store.book[0].author"); 

//输出全部author的值，使用Iterator迭代
List<String> authors = JsonPath.read(json, "$.store.book[*].author");

//输出book[*]中category == 'reference'的book
List<Object> books = JsonPath.read(json, "$.store.book[?(@.category == 'reference')]");        

//输出book[*]中price>10的book
books = JsonPath.read(json, "$.store.book[?(@.price>10)]");

//输出book[*]中含有isbn元素的book
books = JsonPath.read(json, "$.store.book[?(@.isbn)]");

//输出该json中所有price的值
List<Double> prices = JsonPath.read(json, "$..price");
	
//可以提前编辑一个路径，并多次使用它
JsonPath path = JsonPath.compile("$.store.book[*]"); 
books = path.read(json);


============FasterXml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.9.6</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.6</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.9.6</version>
</dependency>

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

public class UserJson {
	
	@Id
	@JsonSerialize(using=MyJSonSerializer.class) 
	private ObjectId id; //mongo的
	
	@JsonProperty("userName")
    private String userName;
	
	@JsonProperty("joinDate")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date joinDate;
	
    @JsonIgnore
    private String password;
    
    @JsonProperty("favorite")
	@JsonInclude(JsonInclude.Include.NON_NULL) 	 //字段范围,如果该属性为NULL,生成joson没有该字段 
    private List<String> favorite;

    @JsonProperty("order")
    private OrderJson order;
	
	//可BigDecimal类型
	
	//getter/setter
}
public class MyJSonSerializer extends JsonSerializer<ObjectId>{
	@Override
	public void serialize(ObjectId objId, JsonGenerator gen, SerializerProvider provider) throws IOException {
		 if(objId==null)
			 gen.writeNull();
		 else
			 gen.writeString(objId.toString());
	} 
}
//--- 对象 到 JSON字串
ObjectMapper mapper = new ObjectMapper();
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//全局范围，如果该属性为NULL,生成joson没有该字段 
String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
System.out.println(jsonString);

//--- JSON字串 到 对象
ObjectMapper mapper = new  ObjectMapper();
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//反序列化遇到未知属性不报异常
mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//允许使用未带引号的字段名
mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); //允许使用单引号
String str= json2String();
UserJson user=mapper.readValue(str, UserJson.class);

============alibaba JSON 用 FasterXml
https://github.com/alibaba/fastjson

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>fastjson</artifactId>
	<version>1.2.38</version>
</dependency>

import com.alibaba.fastjson.JSON;

public class MyIgnoreObject {
	@JSONField(name="_userName")
    private String userName;
    
	@JSONField(name="_joinDate",format="yyyy-MM-dd HH:mm:ss") 
    private Date joinDate;

    @JSONField(serialize=false,deserialize=false)
    private String password;
}
String jsonString=JSON.toJSONString(user);  //Object ->JSON
UserJson user=JSON.parseObject(str,UserJson.class);//JSON ->Object

List<OrderJson> res=JSON.parseArray(jsonString, OrderJson.class);//JSON Array -> List<Object>
		
JSONObject  jsonObject = JSONObject.parseObject(jsonStr); //JSON->Map
Map<String,Object> map = (Map<String,Object>)jsonObject;

JSONObject json = new JSONObject(map); //Map->JSON

============SnakeYaml
在线验证yaml的好工具
https://onlineyamltools.com/prettify-yaml


 最新 yaml-1.2 是2009年制定的  
 SnakeYaml 只支持 yaml-1.1 , SpringBoot使用这个
 
  <dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>1.25</version>
</dependency>

public static void readYaml( ) throws Exception {
	Yaml yaml = new Yaml();
	//yaml文件  :后一定有空格
	URL url = SnakeYamlMain.class.getResource("test.yaml");
	if (url != null) { 
		Map map = (Map) yaml.load(new FileInputStream(url.getFile()));
		Map map1 =   yaml.loadAs(new FileInputStream(url.getFile()),Map.class);
				
	}
}


============eo-yaml 
  支持 yaml-1.2  
 
 <dependency>
  <groupId>com.amihaiemil.web</groupId>
  <artifactId>eo-yaml</artifactId>
  <version>2.0.1</version>
</dependency>

============ZkClient
<dependency>
	<groupId>com.101tec</groupId>
	<artifactId>zkclient</artifactId>
	<version>0.10</version>
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
============curator 
一个Zookeeper客户端

<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-framework</artifactId>
	<version>4.0.1</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-recipes</artifactId>
	<version>4.0.1</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-x-discovery</artifactId>
	<version>4.0.1</version>
</dependency>

curator-client-4.0.1.jar

String ip ="127.0.0.1";
String ipPort="127.0.0.1:2181";

String nodePath="/hello/world";
String nodeValue="123";




//		RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);//baseSleepTimeMs,  maxRetries 每次重试时间逐渐增加
//		RetryPolicy retryPolicy=new RetryNTimes(5,1000);//retryCount 最大重试次数，elapsedTimeMs
RetryPolicy retryPolicy=new RetryUntilElapsed(5000,1000);//maxElapsedTimeMs最多重试多长时间,   sleepMsBetweenRetries 每次重试时间间隔
//		CuratorFramework client=CuratorFrameworkFactory.newClient(ipPort,500,5000, retryPolicy);


List<AuthInfo> authInfos =new ArrayList<>();
AuthInfo auth=new AuthInfo("digest", "myuser:mypass".getBytes());
authInfos.add(auth);

CuratorFramework client= CuratorFrameworkFactory.builder().connectString(ipPort)
.sessionTimeoutMs(5000)
.connectionTimeoutMs(5000)
//		.authorization("digest", "myuser:mypass".getBytes()) //同命令  addauth digest  myuser:mypass
.authorization(authInfos)
.retryPolicy(retryPolicy)
.build();

client.start();


//		client.delete().deletingChildrenIfNeeded().forPath(nodePath);
//client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(1).forPath(nodePath); //可带withVersion
//guaranteed 如删除失败,会一直重试


 ACL aclIp=new ACL(Perms.READ,new Id("ip",ip));//Id构造器参数schema只可是ip(白名单)或digest(用户名密码)
 String userPwd=DigestAuthenticationProvider.generateDigest("myuser:mypass");
 ACL aclDigest=new ACL(Perms.READ|Perms.WRITE,new Id("digest",userPwd));
 ArrayList<ACL> aclList=new ArrayList<>();
//		 aclList.add(aclIp);
 aclList.add(aclDigest);


String path=client.create()
.creatingParentsIfNeeded() //如果一级不存会先创建再建二级目录
.withMode(CreateMode.PERSISTENT)
.withACL(aclList)
.forPath(nodePath,nodeValue.getBytes());

System.out.println(path);


Stat stat=new Stat();
byte[] data =client.getData().storingStatIn(stat).forPath(nodePath);
System.out.println("data= "+new String(data));
System.out.println("stat= "+stat);



List<String> children=client.getChildren().forPath(nodePath);
System.out.println("child have "+children);


stat=client.checkExists().forPath(nodePath);
System.out.println(nodePath+" = "+stat);//null 就不存

ExecutorService executorService= Executors.newFixedThreadPool(5);

 

client.setData().withVersion(stat.getVersion()).forPath(nodePath,"newData".getBytes());


//inBackground 转异步
 client.checkExists().inBackground(new BackgroundCallback() {
		@Override
		public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
			CuratorEventType type=event.getType();
			int resultCode=event.getResultCode(); //0成功
			System.out.println("processResult type= "+type);
			System.out.println("processResult resultCode= "+resultCode);
			System.out.println("processResult getContext= "+event.getContext());
			System.out.println("processResult getPath= "+event.getPath());
			System.out.println("processResult getChildren= "+event.getChildren());
			System.out.println("processResult data= "+new String(event.getData()));
		}
	},"contextVal",executorService).forPath(nodePath);

 
 
//监听 要 curator-recipes 包
NodeCache cache=new NodeCache(client,nodePath);
cache.start();
cache.getListenable().addListener(new NodeCacheListener() {
	@Override
	public void nodeChanged() throws Exception {
		byte[]data =cache.getCurrentData().getData();
		System.out.println("NodeCache data= "+new String(data));
	}
});


PathChildrenCache pathCache=new PathChildrenCache(client,nodePath,true);//true 子节点变化时，也取内容
pathCache.start();
pathCache.getListenable().addListener(new PathChildrenCacheListener() {
	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		switch(event.getType())
		{
			 case CHILD_ADDED :
				 System.out.println("CHILD_ADDED"+event.getData().getPath());
				 break;
			 case CHILD_UPDATED:
				 System.out.println("CHILD_UPDATED"+event.getData().getPath());
				  break;
			 case CHILD_REMOVED:
				 System.out.println("CHILD_REMOVED"+event.getData().getPath());
				  break;
					 
		}
	}
});
client.checkExists().watched().forPath(nodePath);
client.getCuratorListenable().addListener(new CuratorListener() {
	@Override
	public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception { 
		CuratorEventType type=event.getType();
		if(type==CuratorEventType.WATCHED)
		{
			WatchedEvent watchEvent=event.getWatchedEvent();//返回zookeeper的WatchedEvent
			EventType evtType=watchEvent.getType();
			System.out.println("watched path="+watchEvent.getPath());
			client.checkExists().watched().forPath(nodePath);//还要再次wached
		}
		//CuratorEventType.EXISTS ,CuratorEventType.DELETE ,CuratorEventType.CREATE
	}
});

//分布式锁应用,可以自己写一个aop拦截 @ClusterLock("/lock/order")来实现
InterProcessMutex lock=new InterProcessMutex(client,nodePath);
try 
{
	System.out.println("acquire lock ..."); 
	if(lock.acquire(10,TimeUnit.SECONDS))
	{
		System.out.println("geted lock");
		Thread.sleep(1000*3);//模拟使用时间  
	}else
	{
		System.out.println("get lock timeout");
	}
}finally {
	lock.release();
}

//要 curator-x-discovery-4.0.1.jar
//服务描述
ServiceInstanceBuilder<Map> service=ServiceInstance.builder();
service.address("127.0.0.1");
service.port(8080);
service.name(serviceName);//他创建zk节点
Map<String,String> payload=new HashMap<>();
payload.put("url","/api/v3/book");
service.payload(payload);//payload 放额外信息，可是任何类
ServiceInstance<Map> instance=service.build();
		
ServiceDiscovery  discovery=ServiceDiscoveryBuilder.builder(Map.class)
		.client(client)
		.serializer(new JsonInstanceSerializer<Map>(Map.class))
		.basePath("/service")
		.build();
//服务注册 
discovery.registerService(instance);
discovery.start();
//ls /service/book
//get /service/book/<uu-id>

//查找服务
ServiceDiscovery  discovery=ServiceDiscoveryBuilder.builder(Map.class)
		.client(client)
		.serializer(new JsonInstanceSerializer<Map>(Map.class))
		.basePath("/service")
		.build(); 
discovery.start(); 
Collection<ServiceInstance<Map>> all = discovery.queryForInstances(serviceName);
if(all.isEmpty())
	return null;
else
{
	//这里只要第一个服务
	ServiceInstance<Map>  service= new ArrayList<ServiceInstance<Map> >(all).get(0);
	System.out.println(service.getPayload());
	System.out.println(service.getAddress()); 
	return   service;
}

//选leader
LeaderSelectorListenerAdapter listener=new LeaderSelectorListenerAdapter() {
	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		//领导节点，方法结束后退出领导 。zk会再次重新选择领导
	}
};
LeaderSelector selector=new LeaderSelector(client,"/schedule",listener);
selector.autoRequeue();
selector.start(); 

System.in.read();
client.close();

============Shiro (安全) 

<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-core</artifactId>
  <version>1.3.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-web</artifactId>
  <version>1.3.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-spring</artifactId>
  <version>1.3.2</version>
</dependency> 
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-ehcache</artifactId>
  <version>1.3.2</version>
</dependency>


<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-spring-boot-web-starter</artifactId>
  <version>1.4.2</version>
</dependency>


UsernamePasswordToken token = new UsernamePasswordToken("user", "pass");
Subject currentUser = SecurityUtils.getSubject();
currentUser.login(token);//当使用外部系统验证成功后告诉Shiro已经登录



//iniLogin("classpath:shiro_main/shiro.ini");
//iniLogin("classpath:shiro_main/shiro-realm.ini");
//iniLogin("classpath:shiro_main/shiro-cryptography.ini");
//--- 
//iniLogin("classpath:shiro_main/shiro-permisson.ini");
//hasRole(); 
//hasPermission();//没有缓存，每次都重新取数据
//---
iniLogin("classpath:shiro_main/shiro-permisson-realm.ini");
hasRole();
hasPermission();
 

public static void iniLogin(String configFile) {
	Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(configFile);
	org.apache.shiro.mgt.SecurityManager securitManger = factory.getInstance();
	SecurityUtils.setSecurityManager(securitManger);
	Subject subject = SecurityUtils.getSubject();
	
	org.apache.shiro.session.Session  session=subject.getSession();
	session.setAttribute("myAttr", "myVal");
	System.out.println(session.getAttribute("myAttr"));
	
	UsernamePasswordToken token = new UsernamePasswordToken("lisi", "123");
	token.setRememberMe(true);
	
	try {
		subject.login(token);// 看源码
	} catch (UnknownAccountException userError) {
		System.err.println("user not exits");
	} catch (IncorrectCredentialsException passError) {
		System.err.println("password error ");
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println("login OK?" + subject.isAuthenticated());
	subject.logout();
	System.out.println("login OK?" + subject.isAuthenticated());
}
public static void hasRole()
{  
	Subject subject = SecurityUtils.getSubject();
	System.out.println(subject.hasRole("role1"));//看源码
	List<String> list=new ArrayList<>();
	list.add("role1");
	list.add("role2");
	list.add("role3");
	System.out.println(Arrays.toString(subject.hasRoles(list))); //返回boolean数组
	System.out.println(subject.hasAllRoles(list));
	//subject.checkRole("role3");//如没角色报错
}
public static void hasPermission()
{
	Subject subject = SecurityUtils.getSubject();
	System.out.println(subject.isPermitted("user:delete"));//看源码
	System.out.println(subject.isPermittedAll("user:delete","user:update"));
	subject.checkPermission("user:query");//如没 报错
}
 
public static void encPassword() {
	String password="123";
	Md5Hash md5Hash=new Md5Hash(password);
	System.out.println(md5Hash);//散列算法有MD5 和SHA
	
	Md5Hash md5Hash1=new Md5Hash(password,"saltKey");//加盐
	System.out.println(md5Hash1);
	
	Md5Hash md5Hash2=new Md5Hash(password,"saltKey",3);//散列3次
	System.out.println(md5Hash2);//3e751882a57e7f803dcc9c47eeda7be2
	
	/* ini config file
	org.apache.shiro.authc.credential.HashedCredentialsMatcher credntialMatcher=new HashedCredentialsMatcher();
	credntialMatcher.setHashAlgorithmName("md5");
	credntialMatcher.setHashIterations(3);
	
	EncPasswordRealm realm=new EncPasswordRealm();
	realm.setCredentialsMatcher(credntialMatcher);
	*/
} 
---shiro_main/shiro.ini
[users]
#comment
lisi=123
wang=456


---shiro_main/shiro-realm.ini
myRealm=shiro_main.MyRealm
securityManager.realms=$myRealm


// AuthorizingRealm 带认证和制授权 
public class MyRealm extends AuthorizingRealm
{
	@Override
	public String getName() {
		return "MyRealm";
	}
	// 授权  (对于配置 shiro-realm.ini 文件)
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//String username=(String)principals.getPrimaryPrincipal();//可是放session中的对象 ,这里用户名
		UserInfo userInfo=(UserInfo)principals.getPrimaryPrincipal();
		
		//模拟查数据库为用户加角色，权限,不是使用配置文件方式
		
		List<String> roles=new ArrayList<>();
		roles.add("role1");
		
		List<String> permissions=new ArrayList<>();
		permissions.add("user:create");
		
		SimpleAuthorizationInfo auInfo=new SimpleAuthorizationInfo();
		auInfo.addRoles(roles);
		auInfo.addStringPermissions(permissions);//有addObjectPermissions
		return auInfo;
	}
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("MyRealm,"+token);
		String username=(String)token.getPrincipal();
		//查数据库
		if(!"lisi".equals(username))//模拟无此用户
			return null;
		
		// (对于配置 shiro.ini 文件)
		String password="123"; 
		UserInfo userInfo=new UserInfo();
		userInfo.setUserAccount(username);
		userInfo.setFullName("李四");
		userInfo.setMobilePhone("130111122222");
		userInfo.setUserId("1001");
		SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(userInfo,password,getName());
		//SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(username,password,getName());//username可是放session中的对象 ,这里用户名
		
		// (对于配置 shiro-cryptography.ini 文件)
		String saltKey="saltKey";
		Md5Hash md5Hash2=new Md5Hash(password,saltKey,3);//散列3次
		String saltPassword=md5Hash2.toString();//模拟数据库取出的
		
		SimpleAuthenticationInfo authInfo=new SimpleAuthenticationInfo(username,
				saltPassword,ByteSource.Util.bytes(saltKey), //盐是什么要告诉shiro,3次在配置文件中
				getName());
				
		return authInfo;
	}
}

----shiro-cryptography.ini
[main]
credntialMatcher=org.apache.shiro.authc.credential.HashedCredentialsMatcher
credntialMatcher.hashAlgorithmName=md5
credntialMatcher.hashIterations=3

myRealm=shiro_main.EncPasswordRealm
myRealm.credentialsMatcher=$credntialMatcher
securityManager.realms=$myRealm

----shiro-permisson.ini
[users]
#comment
lisi=123,role1,role2
wang=456,role1

[roles]
#表示对资源user有create权限
role1=user:create,user:update
role2=user:delete
role3=user:*
role4=user:update:101
role5=user:*:101
#第三列101表示实例,像某条记录
 
----shiro web

web.xml
  <context-param>
  	<param-name>shiroEnviromentClass</param-name>
  	<param-value>org.apache.shiro.web.env.IniWebEnvironment</param-value>
  </context-param>
   <context-param>
  	<param-name>shiroConfigLocations</param-name>
  	<param-value>classpath:shiro_web/shiro.ini</param-value>
  </context-param>
   <!--  读上面两个param-name,调用IniWebEnvironment的 init 方法会创建SecurityManager 放 servletContext 中供Filter使用 -->
  <listener>
  	<listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
  </listener>
  
  <filter>
  	<filter-name>shiro_web</filter-name>
  	<filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>shiro_web</filter-name>
  	<url-pattern>/*</url-pattern>  */
	<dispatcher>REQUEST</dispatcher>
    <dispatcher>FORWARD</dispatcher>
    <dispatcher>INCLUDE</dispatcher>
    <dispatcher>ERROR</dispatcher>
  </filter-mapping>

  
// anon 定义在 DefaultFilter 中
   
---shiro_web/shiro.ini  
[main]
authc.loginUrl=/login.ser
#如没登录跳转页,要和登录表单提交地址一样才知道从哪取到用户名密码
#authc就是 FormAuthenticationFilter  页中用户名要为username,密码为passsword,修改方法(类中有) 
authc.usernameParam=j_username
authc.passwordParam=j_password
authc.rememberMeParam=j_rememberMe

roles.unauthorizedUrl=/noRole.jsp
#如没有要求的角色跳转页
perms.unauthorizedUrl=/noPerm.jsp
#如没有权限的角色跳转页
logout.redirectUrl=/web/login.jsp
#退出后的跳转页

#perms.enabled=false
#表示不使用这个过滤器

[users] 
lisi=123,adminRole
wang=456,queryRole

[roles]
adminRole=employee:*
queryRole=employee:query

[urls]
#=后是过滤器的顺序 ，路径也是从上到下的顺序匹配
/js/**=anon
/img/**=anon
/web/main.jsp=anon
#/web/login.jsp=anon 
#可以不用配置 logout.redirectUrl已经做了

#anon 是shiro的匿名过滤器
#authc 过滤器 表示必须要登录 ,roles 角色过滤器 

/employee/create.ser=authc,roles[adminRole]
/employee/query.ser=authc,roles[queryRole]
/web/query.jsp=authc,roles[adminRole]
#.jsp也能拦截的,web.xml配置了FORWARD拦截不到

/employee/delete.ser=perms[employee:delete]
#perms 权限过滤器

/logout.ser=logout
#logout 退出过滤器,会清session,页面直接请求这个地址,不用自己实现

#登录表单提交地址要authc
/login.ser=authc
#/**=authc



 

*/


@WebServlet("/login.ser")
public class LoginServlet extends HttpServlet 
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		/*
		#登录表单提交地址要authc
		/login.ser=authc
		#/**=authc
		*/
		String exceptClassName=(String) request.getAttribute("shiroLoginFailure");
		if(exceptClassName!=null)
		{
			if(UnknownAccountException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","用户名不存在");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","密码错误");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptClassName))
			{
				request.setAttribute("error","系统异常");
			}
		}
		//如登录成功会直接跳到登录前的页面，如没有登录前的页面默认请求/
		request.getRequestDispatcher("/web/login.jsp").forward(request, response);
	}		
}
----- /web/login.jsp
<form action="<%=request.getContextPath()%>/login.ser" method="post">
	<section>
		<label for="j_username">Username</label> <input  name="j_username" type="text" />
	</section>
	<section>
		<label for="j_password">Password</label> <input  name="j_password" type="password" />
	</section>
	<section>
		<label for="j_rememberMe">remeber me?</label> <input  name="j_rememberMe" type="checkbox" />
	</section>
	<section>
		<input type="submit" value="Login" />
	</section>
</form>

----main.jsp
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<shiro:guest>
	你没有登录，点击 <a href="<%=request.getContextPath()%>/login.ser">这里</a> 登录
</shiro:guest>  <br/>

<a href="${pageContext.request.contextPath}/logout.ser">logout   </a> <br/>

Hello, <shiro:principal/> <br/>
Hello, <%= SecurityUtils.getSubject().getPrincipal()  %> <br/>

<shiro:hasPermission name="employee:delete">
	<a href="<%=request.getContextPath()%>/employee/delete.ser">delete employee ,Permission employee:delete</a> <br/>
</shiro:hasPermission>
<shiro:lacksPermission name="employee:delete">
	你没有 employee:delete 权限
</shiro:lacksPermission>

<shiro:hasRole name="adminRole">
	<a href="<%=request.getContextPath()%>/employee/create.ser">add new employee , adminRole</a> <br/>
</shiro:hasRole>

----shiro spring  

--web.xml
 <filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <init-param>
        <param-name>targetFilterLifecycle</param-name>
        <param-value>true</param-value>
    </init-param>
 </filter>
   
  <filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/*</url-pattern>   */
  </filter-mapping>

  <servlet>
    <servlet-name>spring_mvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring_mvc.xml</param-value><!--  MVC相关的配置  -->
	</init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>spring_mvc</servlet-name>
    <url-pattern>*.mvc</url-pattern>
  </servlet-mapping>
    
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring_shiro.xml</param-value><!-- Shiro相关的Bean注入配置  -->
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

 ---spring_mvc.xml
    <context:annotation-config/>
    <context:component-scan base-package="shiro_spring" />
    <mvc:annotation-driven/> 
    <mvc:default-servlet-handler/>
	<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/WEB-INF/jsp/" p:suffix=".jsp" />
	<bean  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
		<property name="exceptionMappings">
			<props>
				<prop key="org.apache.shiro.authz.UnauthorizedException">redirect:/noPerm.jsp</prop>
				<prop key="org.apache.shiro.authz.UnauthenticatedException">redirect:/login.mvc</prop>  
			</props>
		</property>
	</bean>
	<!-- 使用  @RequiresRoles,@RequiresPermissions  生效   -->
	<bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/> 
 	<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" depends-on="lifecycleBeanPostProcessor">
 		<property name="proxyTargetClass" value="true"></property>
 	</bean>
  <!-- 方式二  对spring aop版本有要求
  	<aop:config proxy-target-class="true"></aop:config>
   -->

---spring_shiro.xml
<bean id="hashedCredentialsMatcher" class ="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
	<property name="hashAlgorithmName" value="md5"></property>
	<property name="hashIterations" value="3"></property> 
</bean>
<bean id="mySpringRealm" class="shiro_spring.MySpringRealm" >
	<property name="credentialsMatcher" ref="hashedCredentialsMatcher"></property>
</bean>
<bean id="mySpringRealm2" class="shiro_spring.MySpringRealm2" >
	<property name="credentialsMatcher" ref="hashedCredentialsMatcher"></property>
</bean>
<bean id="myModularAuthen" class="org.apache.shiro.authc.pam.ModularRealmAuthenticator">
	<property name="authenticationStrategy" >
		<bean class="org.apache.shiro.authc.pam.AllSuccessfulStrategy"></bean>
		<!-- 默认 AtLeastOne 只要一个realm成功就算成功
		<bean class="org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy"></bean>
		<bean class="org.apache.shiro.authc.pam.FirstSuccessfulStrategy"></bean>
		 -->
	</property>
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
</bean>
<bean id="myModularAuthor" class="org.apache.shiro.authz.ModularRealmAuthorizer">
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
</bean>

<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
	<property name="cacheManager" ref="cacheManager"></property>
	<!-- 
	<property name="realm" ref="mySpringRealm" />
	 -->
	 <!-- 
	<property name="realms" >
		<list >
			 <ref bean="mySpringRealm"/>
			 <ref bean="mySpringRealm2"/>
		</list>
	</property>
	 -->
	 <property name="authenticator" ref="myModularAuthen"></property>
	 <property name="authorizer" ref="myModularAuthor"></property>  
</bean>

<bean id ="cacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
	<property name="cacheManager" ref="ehcacheManager" ></property>
</bean>

<bean id="ehcacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	<property name="configLocation" value="classpath:shiro_spring/ehcache.xml"></property>
	<property name="shared" value="true"></property>
</bean>

<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
	<property name="securityManager" ref="securityManager" />
	<property  name="loginUrl" value="/login.mvc"/> 
	<property name="successUrl" value="/main.mvc"/> 
	<property name="unauthorizedUrl" value="/noPerm.jsp"/>
	<property name="filters"> 
		<util:map> 
			 <entry key="authc" value-ref="formAuthenticationFilter" />
		</util:map> 
	</property> 
	<property name="filterChainDefinitions">
		<value>
			#=后是过滤器的顺序 ，路径也是从上到下的顺序匹配
			/js/**=anon
			/img/**=anon
			/main.mvc=anon
			/logout.mvc=logout
			#logout 退出过滤器,会清session,页面直接请求这个地址,不用自己实现
			
		<!-- /employee/create.mvc=authc,roles[adminRole]
			/employee/query.mvc=authc,roles[queryRole]
			/employee/delete.mvc=perms[employee:delete]
		-->
			#登录表单提交地址要authc
			/login.mvc=authc
			#/**=authc
		</value>
		*/
	</property>
</bean>

 
<bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
	<property name="securityManager" ref="securityManager"/>
</bean>
	
<bean id="formAuthenticationFilter"    class="org.apache.shiro.web.filter.authc.FormAuthenticationFilter">
	<property name="usernameParam" value="j_username" />
	<property name="passwordParam" value="j_password" />
	<property name="rememberMeParam" value="j_rememberMe"/> 
</bean> 
 

--controller
@RequiresAuthentication
@RequiresRoles({"adminRole"})
//结合不显示按钮一起用更更安全(对未配置这个地址)
//也可用于service方法,如没权限报异常,要配置SimpleMappingExceptionResolver (unauthorizedUrl没用)
@RequestMapping(value = "/create" )
public void create( HttpServletRequest requset,HttpServletResponse response) throws Exception {
	UnauthorizedException exp;
	response.getWriter().write("employee/create");

}
@RequiresAuthentication
@RequiresPermissions({"employee:delete"})
@RequestMapping(value = "/delete" )
public void delete(  HttpServletResponse response ) throws Exception {
	response.getWriter().write("employee/delete");
}
//@RequiresGuest 


//spring把@Controller中的所有的@RequestMapping的方法
Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();
for(HandlerMethod method:methods.values())
{
	RequiresAuthentication auth=method.getMethodAnnotation(RequiresAuthentication.class);
	if(	auth!=null   )
		System.out.println("must be login");
	RequiresPermissions perm=method.getMethodAnnotation(RequiresPermissions.class);
	if(	perm!=null   )
		System.out.println("has perm "+Arrays.toString(perm.value()));
	RequiresRoles role=method.getMethodAnnotation(RequiresRoles.class);
	if(	role!=null   )
		System.out.println("has role "+Arrays.toString(role.value()));
}
--- MySpringRealm

//清除登录用户的角色权限信息，不是登录信息，要手动调用 
public void myClearCache() 
{
	PrincipalCollection principalCollection=SecurityUtils.getSubject().getPrincipals();
	super.clearCache(principalCollection);//super=AuthorizingRealm
}


<shiro:authenticated> <!-- 如不加 进入页时 报nullpoint -->
	Hello, <shiro:principal type="shiro_main.UserInfo" property="fullName"/>  相当于取session对象的属性名 	<br/>
</shiro:authenticated>

login.mvc 代码与 login.ser 代码相同
<form action="<%=request.getContextPath()%>/login.mvc" method="post">
</form>

也可以用 
<form action="<%=request.getContextPath()%>/submitLoginNoFilter.mvc" method="post">
</form>
Subject subject = SecurityUtils.getSubject();
UsernamePasswordToken token = new UsernamePasswordToken(username, password);
subject.login(token);//只能自己控制跳转页

------------shiro spring session


------------shiro 自身不支持 oautho2 ,支持JWT，手机端不支持存cookie
  {
	 DefaultWebSecurityManager manager =new DefaultWebSecurityManager();
//	 manager.setRealm(realm);
	 
	 //关闭session为手机端没有cookie，未测试 
	 DefaultSubjectDAO subjectDAO =new DefaultSubjectDAO();
	 DefaultSessionStorageEvaluator sessionStorageEvaluator=new DefaultSessionStorageEvaluator();
	 sessionStorageEvaluator.setSessionStorageEnabled(false);
	 subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
	 manager.setSubjectDAO(subjectDAO);
	 manager.setSubjectFactory(subjectFactory());//DefaultSubjectFactory
	}
	public   DefaultSubjectFactory subjectFactory( ) {
		//不确定还有什么 ？？
		 DefaultSubjectFactory factory=new DefaultSubjectFactory();
		return  factory;
	}
	
//为JWT，未测试 
//放到ShiroFilterFactoryBean中的filter属性中 把/**=authc 修改为这个jwt,其它所有用到的拦截器都要修改/增加新的
//已有的代码 Subject currentUser = SecurityUtils.getSubject().getPrincipal();无效？？？
// 手机端token， 持浏览器cookie只能分开接入？？？
public class NoSessionFilter extends BasicHttpAuthenticationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) 
	{
		//		String[] values=(String[])mappedValue;//当配置 /xx=role[read] 时
		HttpServletRequest req=(HttpServletRequest)request;
		String token =req.getParameter("token");//也可放在http header中
		Subject userInfo=null;//检查token在内存中有吗？登录成功后放Map(Redis带失效时间/DB)中key为随机生成/UUID的token值，value为subject 
		if(token==null)
		{
			System.out.println("未登录");
			return false;
		}
		return true;
	}
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		System.out.println("无权限");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write("{\"code\":503}");
		return false;
	}
}



-------------javassit 
可以没有接口
ClassPool pool=new ClassPool(true);
pool.insertClassPath(new LoaderClassPath(MainJavasssit.class.getClassLoader()));

CtClass targetClass=pool.makeClass("my.javassist.Hello");//动态代理生成新的class，比cglib慢
targetClass.addInterface(pool.get(IHello.class.getName()));

CtClass returnType=pool.get(void.class.getName());
String mname="sayHello";
CtClass[] parameters=new CtClass[] {pool.get(String.class.getName())};
CtMethod method=new CtMethod(returnType, mname, parameters, targetClass);
String src="{ System.out.println(\"hello \"+$1); }";
method.setBody(src);
targetClass.addMethod(method);
Class<IHello> clazz=targetClass.toClass();
IHello  hello=clazz.newInstance();
hello.sayHello("王");

-------------Jasypt 
可配置文件加密 密码 ENC(xxx)

<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt</artifactId>
    <version>1.9.2</version>
</dependency>

 Spring Framework 3.1 and newer 
<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt-spring31</artifactId>
    <version>1.9.2</version>
</dependency>

jasypt-springsecurity3 for Spring Security 3.x and newer 

<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt-springsecurity3</artifactId>
    <version>1.9.2</version>
</dependency>

//可配置文件加密 密码 ENC(xxx)


static String KEY="theKey";
//PBEWithMD5AndDES
public static String  encode(String thePass) {
	BasicTextEncryptor encryptor=new BasicTextEncryptor();
	encryptor.setPassword(KEY);
	String enc= encryptor.encrypt(thePass);
	return enc;
}
//PBEWithMD5AndDES
public static String  decode(String enc) {
//		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//	    encryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);//PBEWithMD5AndDES

	BasicTextEncryptor encryptor=new BasicTextEncryptor();
	encryptor.setPassword(KEY);
	String dec= encryptor.decrypt(enc);
	return dec;
}

public static void main(String[] args) {
	
	String enc=encode("thePass");
	System.out.println("enc="+enc);//DaarVCtqChPzfrYGDc/IYA==
	String dec=decode(enc);
	System.out.println("dec="+dec); 
}

spring
<!--  PBEWithMD5AndDES , PBEWithMD5AndTripleDES -->
<bean id="configurationEncryptor" class="org.jasypt.encryption.pbe.StandardPBEStringEncryptor">
	<property name="algorithm">
		<value>PBEWithMD5AndDES</value>
	</property>
	<property name="password">
		<value>theKey</value>
	</property>
</bean>
 
 
<bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
	<property name="locations">
		<list>
			<value>classpath:/spring_jasypt/dev.properties</value>
			<value>classpath:/spring_jasypt/devOver.properties</value> <!-- 相同属性后面的会覆盖前面的 -->
		</list>
	</property>
</bean>
<bean id="propertyConfigurer" class="org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer">
	<constructor-arg ref="configurationEncryptor" />
	<property name="properties" ref="configProperties"></property>
	<!-- 
	<property name="locations">
		<list>
			<value>classpath:/spring_jasypt/dev.properties</value> 
		</list>
	</property>
	 -->
</bean>

@Value("${redis.password}")
private String redisPass;//是解密的thePass
@Value("#{configProperties.redis_password}") //自己的key不能有点
private String redisPass2;
	
---dev.properties 加密的
redis.password=ENC(DaarVCtqChPzfrYGDc/IYA==)
redis_password=P

---devOver.properties 
redis_password=overP_


--------- RESTEasy
<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>resteasy-jaxrs</artifactId>
    <version>3.8.1.Final</version>
</dependency> 
<dependency>
	<groupId>org.jboss.spec.javax.ws.rs</groupId>
	<artifactId>jboss-jaxrs-api_2.1_spec</artifactId>
</dependency>
        
<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>jaxrs-api</artifactId>
    <version>3.0.12.Final</version>
</dependency>
 
#示例版本
resteasy-jaxrs-3.1.4.Final.jar

#Could not find MessageBodyWriter for response object of type:
resteasy-jettison-provider-3.1.4.Final.jar
resteasy-jaxb-provider-3.1.4.Final.jar
jettison-1.3.3.jar (org/codehaus/jettison/jettison/1.3.3)


#新版本测试也可 
resteasy-core-4.1.1.Final.jar 
resteasy-core-spi-4.1.1.Final.jar
microprofile-config-api-1.3.jar
smallrye-config-1.3.6.jar

jboss-jaxrs-api_2.1_spec-1.0.3.Final.jar  
reactive-streams-1.0.2.jar 

#Could not find MessageBodyWriter for response object of type
resteasy-jackson2-provider-4.1.1.Final.jar
resteasy-jaxb-provider-4.1.1.Final.jar
jackson-jaxrs-json-provider-2.9.9.jar
jackson-jaxrs-base-2.9.9.jar
json-patch-1.9.jar
jackson-module-jaxb-annotations-2.9.9.jar



servlet 3.0容器用
<dependency>
	<groupId>org.jboss.resteasy</groupId>
	<artifactId>resteasy-servlet-initializer</artifactId>
	<version>${resteasy.version}</version>
</dependency>

老的servlet容器用 web.xml
<servlet>
    <servlet-name>Resteasy</servlet-name>
    <servlet-class>org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher</servlet-class>
    <init-param>
      <param-name>javax.ws.rs.Application</param-name>
  		<param-value>swagger2_oas3_resteasy.MyApplication</param-value>
    </init-param>
 </servlet> 
  <servlet-mapping>
    <servlet-name>Resteasy</servlet-name>
    <url-pattern>/sample/*</url-pattern>   */
  </servlet-mapping>
  <!--如果servlet-mapping不是/*  就要加配置 */ -->
  <context-param>
    <param-name>resteasy.servlet.mapping.prefix</param-name>
    <param-value>/sample</param-value>
  </context-param>
  

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import swagger2_oas3_resteasy.user_store.PetStoreResource;
import swagger2_oas3_resteasy.user_store.UserResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/sample") //和web.xml对应
//在swagger-ui中请示是 http://localhost:8080/sample/user/user1 
//如何设置webContext???
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Stream.of(PetResource.class, 
        		PetStoreResource.class,UserResource.class,
						OpenApiResource.class//就可以请求${pageContext.request.contextPath}/sample/openapi.json(.yaml) 为swagger-ui使用 
        		      ).collect(Collectors.toSet());
    }
}



-------------Swagger 
类似的有 RAML(RESTful API Modeling Language)

新版本使用OpenAPI
OpenAPI最新 3.0 版本 OpenAPI Specification (OAS)
有用 OAuth2

---Swagger-Core
CXF 3.3.2  也支持OpenApi 3.0.x
https://github.com/swagger-api/swagger-core
Swagger Core 2.X produces OpenApi 3.0 definition files
2.0.8

使用 Jackson 库

RESTEasy-4.1.1.Final (jBoss项目)实现了 JAX-RS 2.1 规范 
Jersey  (毛线衫) 扩展 JAX-RS  2.0 规范，可以和2.1版本的sse(Server Send Event)功能一起用
javax.ws.rs.sse包(Server Send Event)是JAX-RS 2.1的功能

swagger-jersey2-jaxrs.jar 包 支持 JAX-RS 2.0


 
 <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-jaxrs2</artifactId>
      <version>2.0.8</version>
    </dependency>
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-jaxrs2-servlet-initializer</artifactId>
      <version>2.0.8</version>
    </dependency>

    <dependency>
    <groupId>io.github.classgraph</groupId>
    <artifactId>classgraph</artifactId>
    <version>4.8.43</version>
</dependency>

------swagger 2.0.8  OAS-3.0 deps   可以和 swagger1 一起用
jboss-logging-3.3.0.Final.jar
commons-lang3-3.5.jar
jackson-dataformat-yaml-2.9.5.jar 

jaxrs-api-3.0.12.Final.jar
swagger-jaxrs2-2.0.8.jar
swagger-jaxrs2-servlet-initializer-2.0.8.jar
swagger-annotations-2.0.8.jar
swagger-integration-2.0.8.jar
swagger-core-2.0.8.jar
swagger-models-2.0.8.jar


RESTEasy 配置web.xml  (配置 javax.ws.rs.Application 实现类)
 

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
  static PetData petData = new PetData();

  @GET
  @Path("/{petId}")
  @Operation(summary = "Find pet by ID",
    tags = {"pets"},
    description = "Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
    responses = {
            @ApiResponse(description = "The pet", content = @Content(
                    schema = @Schema(implementation = Pet.class)
            )),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
  public Response getPetById(
      @Parameter(
              description = "ID of pet that needs to be fetched",
              schema = @Schema(
                      type = "integer",
                      format = "int64",
                      description = "param ID of pet that needs to be fetched",
                      allowableValues = {"1","2","3"}
              ),
              required = true)
      @PathParam("petId") Long petId) throws io.swagger.sample.exception.NotFoundException //@PathParam的值对应于上面的@Path的值
  {
    Pet pet = petData.getPetById(petId);
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new io.swagger.sample.exception.NotFoundException(404, "Pet not found");
    }
  }



}

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Pet")
public class Pet {
  @Schema(required = true, example = "[2, 3]")
  private int[] lorem;
  
  @XmlElement(name = "status")
  @Schema(description = "pet status in the store", allowableValues = "available,pending,sold")
  public String getStatus() {
    return status;
  }
  
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  public List<Tag> getTags() {
    return tags;
  }
  
}

Http头  
Accept : application/json 返回json
Accept : application/xml 返回xml 

http://127.0.0.1:8080/J_ThirdLibWeb/sample/pet/1

//测试下来，这个没用？？？？
@Provider
public class SampleExceptionMapper implements ExceptionMapper<Exception> {
}
//好像没有什么用
@Provider
public class JsonProvider implements ContextResolver<ObjectMapper> {
}


--- Swagger-Inflector
Mock responses for any unimplemented methods, based on your OAS definition.

---- swagger-editor
下载源码 v3.6.31  2019-07-24

npm install 
npm run build  就可以双击index.html查看在线实时编辑，实时显示效果

#如要构建docker镜像
#docker build -t swagger-editor
#docker run -d -p 80:8080 swagger-editor
# 查看 http://localhost

可以打开 OpenAPI的json或者yaml (File -> Import File 或 File -> Import URL)


---docker版本 swagger-editor
docker pull swaggerapi/swagger-editor
docker run -d -p 80:8080 swaggerapi/swagger-editor

---swagger-ui
源码 版本v3.23.1  2019-07-24


--- 方式1  测试OK
npm init 
npm install swagger-ui-dist --save
npm install express --save 

-- vi swagger-ui-server.js
const express = require('express')
const pathToSwaggerUi = require('swagger-ui-dist').absolutePath()

const app = express()

app.use(express.static(pathToSwaggerUi))

app.listen(3000)

启动服务 node swagger-ui-server.js 就可仿问  http://127.0.0.1:3000/
但不可在Explore地栏写系统路径,
如输入 http://127.0.0.1:8080/J_ThirdLibWeb/my-swagger-ui/swagger.json 报 cross-origin (CORS) 响应头要求加 'Access-Control-Allow-*' 

写个通用的Filter
 res.addHeader("Access-Control-Allow-Origin", "*");
 res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
 res.addHeader("Access-Control-Allow-Headers", "Content-Type");
    
--- 测试OK
node_modules/swagger-ui-dist 的内容 (或者源码目录/dist)直接复制到自己的项目目录中，修改index.html中配置json文件
 	const ui = SwaggerUIBundle({
       //url: "https://petstore.swagger.io/v2/swagger.json",
        //localhost 和 127.0.0.1 不同是cross-origin (CORS)
        //url:"http://localhost:8080/J_ThirdLibWeb/swagger-ui-v2/swagger-ui-dist/sample.json",
        // url:"sample.json",
         //url:"http://localhost:8080/J_ThirdLibWeb/sample/openapi.json",
        //url:"../../sample/openapi.json",
        url:"../../sample/openapi.yaml",
     }

跨域 请求json使用fetch api, 返回没有响应头？？？


--- 方式 2   无法试 ???
npm install swagger-ui --save 
为 Webpack

import SwaggerUI from 'swagger-ui'
// or use require, if you prefer
const SwaggerUI = require('swagger-ui')

SwaggerUI({
  dom_id: '#myDomId'
})



---docker版本 swagger-ui
3.18.3

docker pull swaggerapi/swagger-ui
docker run -p 80:8080 swaggerapi/swagger-ui 
会使用nginx 启动，带有 Swagger UI,就可仿问 http://127.0.0.1/ 默认地址栏是 https://petstore.swagger.io/v2/swagger.json 可以下载到/home/dell/Documents目录下

docker run -p 80:8080 -e SWAGGER_JSON=/foo/swagger.json -v /home/dell/Documents:/foo swaggerapi/swagger-ui  
启动后 Explore中地址显示为./swagger.json ,Explore按钮是立即解析json,不是浏览选择文件，只能复制粘贴地址


docker run -p 80:8080 -e BASE_URL=/swagger -e SWAGGER_JSON=/foo/swagger.json -v /home/dell/Documents:/foo swaggerapi/swagger-ui
仿问 http://127.0.0.1/ 报404 ??? ？？？  http://127.0.0.1/swagger 也不行 ??? ？？？ 
This will serve Swagger UI at /swagger instead of /.


---codegen
<dependency>
    <groupId>io.swagger.codegen.v3</groupId>
    <artifactId>swagger-codegen-maven-plugin</artifactId>
    <version>3.0.8</version>
</dependency>

http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.7/swagger-codegen-cli-2.4.7.jar
 
java -jar swagger-codegen-cli-2.4.7.jar help
java -jar swagger-codegen-cli-2.4.7.jar  langs 显示支持的语言

java -jar swagger-codegen-cli-2.4.7.jar help generate
java -jar swagger-codegen-cli-2.4.7.jar  config-help -l java

java -jar swagger-codegen-cli-2.4.7.jar generate -l  java -o out_dir -i xxx.yaml 或 xxx.json  
 如是java语言是java client代码，有gradle,maven,AndroidManifest.xml,依赖于swagger-annotations是1.5的版本
 如是spring用的是 springfox-swagger2，swagger-annotations 还是1.5版本
 


=======================pinyin4j
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public static String getPingYin(String src)
{

	char[] t1 = null;
	t1 = src.toCharArray();
	String[] t2 = new String[t1.length];
	HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	//t3.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
	t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	String t4 = "";
	int t0 = t1.length;
	try
	{
		for (int i = 0; i < t0; i++)
		{
			// 判断是否为汉字字符
			if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
			{
				t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
				t4 += t2[0]+" ";
			} else
				t4 += java.lang.Character.toString(t1[i])+" ";
		}
		// System.out.println(t4);
		return t4;
	} catch (BadHanyuPinyinOutputFormatCombination e1)
	{
		e1.printStackTrace();
	}
	return t4;
}
String cnStr = "中华人民共和国";
System.out.println(getPingYin(cnStr));


-------------Reactor 
https://projectreactor.io/

<dependency>
	<groupId>io.projectreactor</groupId>
	<artifactId>reactor-core</artifactId>
	<version>3.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.4.2</version>
</dependency>
<dependency>
    <groupId>com.goldmansachs</groupId>
    <artifactId>gs-collections-api</artifactId>
    <version>7.0.3</version>
</dependency>
<dependency>
    <groupId>com.goldmansachs</groupId>
    <artifactId>gs-collections</artifactId>
    <version>7.0.3</version>
</dependency>
jsr166e-1.0.jar

//---1 或 2 版本的老代码 ??????????????
Environment env = new Environment();
Reactor reactor = Reactors.reactor()
		  .env(env)  
		  .dispatcher(Environment.EVENT_LOOP) // BlockingQueueDispatchers ,事件到达时先存储在一个Blockingqueue中，再由统一的后台线程一一顺序执行 
		  .get(); 
//$("parse") 同 Selectors.object("parse"),Tuple可以传多个参数
Registration reg=reactor.on($("parse"), new Consumer<Event<String>>() 
		{
		  @Override
		  public void accept(Event<String> ev) {
		    System.out.println("Received event with data: " + ev.getData());
		  }
		});
reg.pause(); //暂停后,再notify无用的
reactor.notify("parse", Event.wrap("data"));
Thread.sleep(1000);

reg.resume();
reactor.notify("parse", Event.wrap("data"));
Thread.sleep(500);

reactor-core-1.1.3.BUILD-SNAPSHOT.jar\META-INF\reactor\default.properties
	reactor.dispatchers.default = ringBuffer  ## eventLoop
	
java -Dreactor.profiles.default=production  会使用 META-INF/reactor/production.properties文件


Deferred<String, Stream<String>> deferred = Streams.<String>defer()
		  .env(env)
		  .dispatcher(Environment.RING_BUFFER)
		  .get();
Stream<String> stream = deferred.compose();
//---	
Stream<String> filtered = stream   
		.map(new Function<String, String>() 
		{
			public String apply(String s) {
			  return s.toLowerCase();
			}
		  })
		  .filter(new Predicate<String>()
		{
			public boolean test(String s) {
			  // test String
			  return s.startsWith("nsprefix:");
			}
		  });
//---			
		
// consume values
stream.consume(new Consumer<String>() {//如用 filtered.consume() 会使用过滤规则
  public void accept(String s) {
	  System.out.println("accepted :"+s);
  }
});

// producer calls accept
deferred.accept("Hello World!");

//------Promise
Deferred<String, Promise<String>> deferred1 = Promises.<String>defer()
		  //.env(env).dispatcher(Environment.RING_BUFFER) //加这行 deferred1.accept 不会等待执行完成,不加会等待
		  .get();
//Promise<String> p1 = Promises.success("12333").get();//作用不大
Promise<String> p1=deferred1.compose(); 

// Transform the String into a Float using map()
Promise<Float> p2 = p1.map(new Function<String, Float>() {
		public Float apply(String s) {
		return Float.parseFloat(s);
		}
		}).filter(new Predicate<Float>() {
		public boolean test(Float f) {
			return f > 100f;
		  }
		});

//p2.then(onSuccess, onError)
p2.onSuccess(new Consumer<Float>() { //p1.onSuccess
	public void accept(Float f) {
		Thread.sleep(3000);
		System.out.println("---promise Float:"+f);
	}
});
deferred1.accept("182.2");
//--------------3版本的新代码

// https://www.infoq.com/articles/reactor-by-example/
public class Reactor3Example {
  private static List<String> words = Arrays.asList(
        "the",
        "quick",
        "brown",
        "fox",
        "jumped",
        "over",
        "the",
        "lazy",
        "dog"
        );

  @Test
  public void simpleCreation() {
     Flux<String> fewWords = Flux.just("Hello", "World");
     Flux<String> manyWords = Flux.fromIterable(words);

     fewWords.subscribe(System.out::println);
     System.out.println();
     manyWords.subscribe(System.out::println);
  }
  
  
  @Test
  public void findingMissingLetter() {
    Flux<String> manyLetters = Flux
          .fromIterable(words)
          .flatMap(word -> Flux.fromArray(word.split("")))
          .distinct()
          .sort()
          .zipWith(Flux.range(1, Integer.MAX_VALUE),
                (string, count) -> String.format("%2d. %s", count, string));

    manyLetters.subscribe(System.out::println);
  }
  @Test
  public void restoringMissingLetter() {
    Mono<String> missing = Mono.just("s");
    Flux<String> allLetters = Flux
          .fromIterable(words)
          .flatMap(word -> Flux.fromArray(word.split("")))
          .concatWith(missing)
          .distinct()
          .sort()
          .zipWith(Flux.range(1, Integer.MAX_VALUE),
                (string, count) -> String.format("%2d. %s", count, string));

    allLetters.subscribe(System.out::println);
  }
  @Test
  public void shortCircuit() {
    Flux<String> helloPauseWorld = 
                Mono.just("Hello")
                    .concatWith(Mono.just("world") );

    helloPauseWorld.subscribe(System.out::println);
  }
  
  @Test
  public void blocks() {
    Flux<String> helloPauseWorld = 
      Mono.just("Hello")
          .concatWith(Mono.just("world") );

    helloPauseWorld.toStream()
                   .forEach(System.out::println);
  }
 
}

-------------Reactor  上

-------------akka



-------------OAuth 2.0  
Open Authorization

resource owner  最终用户
resource server	 是API服务器 使用access token,返回保护的资源
client			应用
authorization server 保存用户密码的服务器
---client sparklr2
 


-------------jCIFS   samba SMB
	apache commons VFS2 库的CIFS协议 其实是用 jCIFS 
	
  
