插件开发 Mojo

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
 3.28 版本  还支持 npm,docker,helm ,yum,apt,go,PyPI 每个有proxy和hosted
 
有windows,unix,mac版本,没有 -bundle.zip了 unix版本
	要求只能JDK 1.8 ,解压出现了sonatype-work，刚解压只有log,tmp, orient/plugins目录，启动后会生成很多文件

nexus-3.5.1-02/bin/nexus start  是后台运行，相应的有stop
tail -f sonatype-work/nexus3/log/nexus.log 初次启动时间较长

./nexus run 前台运行 ,如是windows版本用 nexus.exe /run ,对应的有 /start /stop /restart
 
直接仿问 http://127.0.0.1:8081/ 默认有一个用户 admin  密码 admin123 ,新版本提示密码初始放在 nexus-3.21.1-01-win64/sonatype-work/nexus3/admin.password 文件中     
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
-pl --projects
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
					https://repo.spring.io/libs-release/ 
					http://maven.aliyun.com/nexus/content/groups/public
					https://mirrors.huaweicloud.com/repository/maven/ 
   --> 
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
			<directory>src/main/resources</directory> <!-- 这里可变量${env}  ,使用mvn install -Denv=dev来传递 -->
			<excludes>
				<exclude>dubbo.properties</exclude>
			</excludes>
			<!-- 如为 <filtering>true</filtering> 表示<directory>中的目录是要的-->
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
  
  <plugin>
		<groupId>com.spotify</groupId>
		<artifactId>docker-maven-plugin</artifactId>
		<version>1.2.2</version> <!-- 0.4.3 最新版本为1.2.2-->
		<configuration>
			<imageName>my/${project.artifactId}</imageName>
			<dockerDirectory>src/main/docker</dockerDirectory>
			 <!-- github官方推荐使用dockerfile-maven
				CentOS 7 
				打开/usr/lib/systemd/system/docker.service文件，修改ExecStart这行 
				ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
				(原来没-H参数，启动后就监听2375端口) 也可修改/etc/sysconfig/docker的DOCKER_OPTS的值
				
				mvn package docker:build  构建docker镜像,会仿问2375端, 要等一会才行
			  -->
			<dockerHost>http://127.0.0.1:2375</dockerHost>
			<resources>
				<resource>
					<targetPath>/</targetPath>
					<directory>${project.build.directory}</directory>
					<include>${project.build.finalName}.jar</include>
				</resource>
			</resources>
		</configuration>
	</plugin>
	
	<!--发布仓库上 https://github.com/spotify/dockerfile-maven 
	mvn package
	mvn dockerfile:build 本机的docker image 中就有了,Dockerfile和pom.xml文件同级
	mvn dockerfile:push  失败??? 是因为 docker push 失败 
	-->
	<plugin>
	  <groupId>com.spotify</groupId>
	  <artifactId>dockerfile-maven-plugin</artifactId>
	  <version>1.4.13</version>
	  <!-- 如果package时不想用docker打包
	  <executions>
	    <execution>
	      <id>default</id>
	      <goals>
	        <goal>build</goal>
	        <goal>push</goal>
	      </goals>
	    </execution>
	  </executions>
	  -->
	  <configuration> 
	     <username>admin</username>
        <password>Harbor12345</password> 
	    <repository>127.0.0.1/harbor/library/cloud-k8s</repository>
	    <tag>${project.version}</tag>
	    <buildArgs>
	    	<!--可以传给Dockerfile中的ARG JAR_FILE -->
	      <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
	    </buildArgs>
	  </configuration>
	</plugin>
  
   <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId> 
        <version>1.10.0</version> 
		<executions>
			<execution> 
				<id>install node and npm</id>
				<goals>
					<goal>install-node-and-npm</goal>
				</goals>
				<phase>generate-resources</phase> <!-- 默认是 "generate-resources" 可以加 -Dskip.npm 跳过 -->
			</execution>
		</executions>
		<configuration>
			<nodeVersion>v12.16.1</nodeVersion>
			<npmVersion>6.13.4</npmVersion> 
			<downloadRoot>https://nodejs.org/dist/</downloadRoot>
		    <workingDirectory>src/main/frontend</workingDirectory>
			<installDirectory>target</installDirectory>
		</configuration>
    </plugin>
	<plugin>
		<groupId>org.codehaus.gmaven</groupId>
		<artifactId>groovy-maven-plugin</artifactId>
		<version>2.0</version>
		<executions>
			<execution>
				<phase>package</phase>
				<goals>
					<goal>execute</goal>
				</goals>
				<configuration>
					<source>${project.basedir}/src/main/groovy/test.groovy</source>
				</configuration>
			</execution>
		</executions>
	</plugin>
	<!-- test.groovy
	def filePath = "version.txt"
//执行命令
def command = "git name-rev --name-only HEAD"
def process = command.execute()
println "process=" + process
process.waitFor()
def tags = process.text.tokenize()
def tagName = tags[0]
println "tagName=" + tagName
def dateTime = new Date().format("yyyyMMddHHmm")
def version = "$tagName-$dateTime".replace("/", "")
println "version=" + version
File file = new File(filePath)
println file.getAbsolutePath()
// 写入文件
file.write(version)
	-->
  
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
        <url>https://repo.spring.io/libs-release/</url>  
        <layout>default</layout>  
        <snapshots>  
            <enabled>false</enabled>  
        </snapshots>  
    </repository>  
</repositories>  
<pluginRepositories>
	<pluginRepository>
		<id>dev_nexus</id>
		<url>https://repo.spring.io/libs-release/</url>
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
	 <dependency> <!--  spring mail使用的类jdk14没有了 -->
	    <groupId>javax.activation</groupId>
	    <artifactId>javax.activation-api</artifactId>
	    <version>1.2.0</version>
	</dependency>
	<dependency><!--jdk 14去除的@Resource-->
	    <groupId>javax.annotation</groupId>
	    <artifactId>javax.annotation-api</artifactId>
	    <version>1.3.2</version>
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
			<exclusions>
				<exclusion>
					<groupId>com.xxx</groupId>
					<artifactId>*</artifactId> <!-- 支持* -->
				</exclusion>
			</exclusions>
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
	-Dtest=HelloTest -Dtest=\!IngoreTest,\!IngoreTest2
mvn install  也可 eclipse 中 run as ->maven install 会打新包到本地仓库
mvn clean compile   可以几个目标一起执行
mvn package -e 查看错误信息
mvn clean package -Dmaven.test.skip=true    跳过编译测试类,生成.war包中的/lib/没有重复的.jar
mvn install -DskipTests     跳过test的执行，但要编译  
	--update-snapshots  更新snapshots的依赖包
	-P ,--activate-profiles <arg> 默认激活的profile
	

mvn clean compile -e -U  其中 -e 显示错误，-U强制更新snapshots和缺少的release

mvn dependency:list  显示所有依赖 
mvn dependency:tree 打印整个依赖树   
mvn dependency:resolve 打印出已解决依赖的列表
mvn dependency:sources 下载依赖的源代码
mvn dependency:copy-dependencies  会把所有依赖复制到 .\target\dependency 目录中





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
 