
========================H2
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.214</version> 
</dependency>


\h2-2012-04-08\h2\bin\h2.bat 双击它,会弹出IE,打开仿问数据的界面
----Embedded内存运行方式
Saved Settings:中默认选择Generic H2(Embedded)
User Name:默认sa
Password :默认为空
点Connect按钮

--TCP 服务的运行方式
启动服务器
java -cp h2*.jar org.h2.tools.Server  会弹出IE,saved Settings:中默认选择Generic H2(Server)
CMD中提示监听:9092端口
网页中也有提示URL,DriverClass
http://127.0.0.1:8082/

文件保存在 用户主目录下.XP是C:\Documents and Settings\xxx\test.h2.db


Class.forName("org.h2.Driver");
Connection conn=DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa","");//test是数据库名
		//jdbc:h2:tcp://localhost:9092/test 和 jdbc:h2:tcp://localhost/~/test 是不一样的
		//jdbc:h2:tcp://localhost/test
		//jdbc:h2:mem
java -cp h2*.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9092 停止
-- 2.x版本  BIGINT和INT不能加(20)
create table IF NOT EXISTS  student
(
	id INTEGER auto_increment NOT NULL PRIMARY KEY,
	name varchar(55),
	age int,
	birthday date
);
insert into student (name,age,birthday) values('lisi李',25,'2012-10-22');

create sequence IF NOT EXISTS SEQ_student_ID start with 1 ; 
select NEXT VALUE FOR SEQ_student_ID
select CURRENT VALUE FOR SEQ_student_ID



