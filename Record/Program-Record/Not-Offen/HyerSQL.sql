﻿========================HyerSQL   HSQLDB

管理,开发界面 java -cp 			hsqldb.jar org.hsqldb.util.DatabaseManager 
			  java -classpath 	hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
启动hsql服务器 java -cp hsqldb.jar org.hsqldb.Server -database.0 F:\Program\hsqldb_1_8_1_2\hsqldb\lib\test -dbname.0 test -port 9002 


-database.0 指定目录
-dbname.0 是给数据库起的别名
-port 9002 端口

String url="jdbc:hsqldb:hsql://localhost/test"
Class.forName("org.hsqldb.jdbcDriver");


command->set  会显示很多的帮助信息

HSQL 主键自增
create table project (ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,pro_name varchar(55));

