
IBM Data Studio client-4.1.1(Free)基于Eclipse用于开发  可以debug 存储过程 1.5GB ,  还有DB2 Analytics Accelerator Studio(Free)也是1.5GB
Toad for DB2 	Freeware - 6.0
SQL Manager for DB2  Freeware 	2.1.0.3  有点老了2012	

在线文档(可以下载pdf )  https://www-304.ibm.com/support/docview.wss?uid=swg27009474  

db2 客户端  IBM Data Server Runtime Client 有  DB2 Command Line Processor (CLP)

-----------windows 安装
db2admin 操作系统用户
响应文件名: C:\Users\zhaojin\Documents\PROD_EXPC.rsp
实例名:DB2
服务名称:db2c_DB2
端口号:50000

C:\Windows\System32\drivers\etc\services中有了
db2c_DB2	50000/tcp

安装目录 C:\Program Files\IBM\SQLLIB\
 
工具的 DB2 用户添加至 DB2ADMNS 或DB2USER 组

C:\Program Files\IBM\SQLLIB\java下有jar包

用户名不要用db2admin 自己加新开一个系统用户,这样用JDBC才会正常

开始->IBM DB2 DBCOPY1->db2命令窗口->输入 db2 命令后进入 db2=> 提示符
开始->"db2命令窗口-administrator" 来运行 db2stop <force>
 
 运行 db2sampl 安装示例
----------- linux 安装 
./db2setup    界面安装软件
./db2_install 命令行安装软件 , 用root 安装
linux_x64_DB2 Express-C-10.5 依赖于 
	32位的libpam.so    	 ,yum search pam  再 yum install pam-devel.i686
	2位的 libstdc++.so.6 ,yum install libstdc++.so.6
默认安装在 /opt/ibm/db2/v10.5 目录下
如卸载用 ./db2_deinstall  -b /opt/ibm/db2/V10.5 -a    
--安装实例
useradd db2das
./instance/dascrt -u db2das  (das= DB2 Administration Server)
卸载用 ./instance/dasdrop

su - db2das  就可用 db2admin start/stop (~/das/bin/db2admin)
./instance/db2icrt  -p 8000 -u db2instl db2instl 

./instance/db2icrt -a server   -p 8000 -u db2instl db2instl  可以替代上面的两行 
----
useradd db2instl		--instl = install
# cd /opt/ibm/db2/V10.5 
# ./instance/db2icrt  -p 8000 -u db2instl db2instl   --看提示安装日志,  
	---格式  -u <fenced-user-id> <instance-name>  实例名要求和用户名相同!!!!, -p port , -a authentication-type ,icrt＝instance create 的缩写
卸载用 ./instance/db2idrop  db2instl
# ./cfg/db2ln			--在db2instl主目录中  ~/sqllib/bin/  是一个链接到　 /opt/ibm/db2/v10.5/bin) 
--  ./instance/db2idrop  db2instl   (idrop=instance drop ) 参数是实例名

--cat /etc/services  加   db2instl 8000/tcpip   --实例名

# su - db2instl
$ db2sampl  (~/sqllib/bin/db2sample) 会启动instance ,建立sample数据库,表,schema,最后停止instance
	保存位置/home/db2instl/db2instl/NODE0000/SAMPLE/
	 create database mydb on /db2_data 会在目录下建立　db2instl/NODE0000
$ db2start  启动
$ db2
connect to sample  --示例数据库 
select * from staff --示例表
list tables 
connect reset
create databse testdb

db2命令~/.bash_profile 找  ~/.bashrc 找 /home/db2instl/sqllib/db2profile 文件中写PATH环境变量的
如果写crontab任务,不能直接使用db2命令,报找不到命令,要指定路径~/sqllib/bin/db2,在此前要先执行 .  ~/sqllib/db2profile
	可能会丢失系统字符集变量,export csv时要指定字符集
db2 => 模式下
如不能使用backspace删,则可以用ctrl+backspace

 -------------
 不能使用 向上调出上次命令????
如何改／查　端口  ???
linux中db2指定系统用户名，时不可以登录，不指用户则可以 ???

db2=> 交互中 用  \   (空字符和\)来写多行SQL
linux下 db2=> 不可有backspace,使用ctrl+backspace

db2start  启动数据库管理器：
db2admin start 启动管理服务器：

db2 list applications  检查是否存在数据库的应用程序连接：
db2 list applications show detail
db2 force application all  如果存在，就强制断开。

db2stop  关闭数据库管理器 
db2admin stop  关闭管理服务器


连接到DB2服务端
connect to mydb user db2admin[用户名] using db2admin[密码] -- db2admin是系统用户 

db2=>connect reset 			-- 断开database连接
db2=>create database mydb
db2=>CREATE DATABASE dbname USING CODESET UTF-8 TERRITORY CN  -- 或者GBK 
db2=>connect to mydb
db2=>create table myTable(id integer,name varchar(20),birthday timestamp)		 -- 这里不要加;
db2=>connect reset 			 -- 断开database连接
db2=>drop database mydb

db2 ? OPTIONS
db2 list command options
db2 update command options using c off  关闭自动提交没用??

db2=> 中使用!调用操作系统命令
? 帮助
? OPTIONS 帮助
 
quit
history 显示历史有顺序号
runcmd  执行上一条的语句
runcmd  <顺序号>
edit   在linux 下打开vi(可手工修改 db2set db2_clp_editor="vi" ),windows下不能用
						db2set -all 显示 DB2_CLP_EDITOR=vi
edit <顺序号>

list db directory  显示当前被打开的数据库 ,已经被connect 过的
list database directory   同上

list active databases. 显示数据库的状态 ,即当前connect的是哪个
list tables  显示当前用户所有表
list tables for system/user  列出所有的系统表/用户表
list tablespaces   显示表空间、容器的定义
list database directory on  C  -- <盘符> 查看本地数据库目录(一定要在上一命令中有显示)
list node directory 列出可访问的结点


$ db2 "select * from syscat.bufferpools"  --双引号可有,可无  显示数据库缓冲池定义

select current schema from sysibm.dual 查当前schema  显示就是 db2instl
select schemaname from syscat.schemata  查所有schema
set current schema zhaojin  设置当前schema

SELECT distinct TABSCHEMA FROM SYSCAT.TABLES   --查所有存在的schema
select * from sysstat.TABLES  -- view 
select * from sysibm.TABLES    -- table  
select * from sysstat.columns where tabname='A'; 取字段 
select * from SYSIBM.SYSCOLUMNS where TBNAME="A"得到A中的字段。 


db2 get db cfg 查看数据配置 有显示		(Database code set)数据库代码集 = UTF-8

批量执行SQL 
db2cmd  初始环境
db2 connect to db_alias user username using password
db2 -tvf c:\temp\db.sql -l db.log    -- -t 表示以;表示结束一语句 ,-f 文件 ,-l log


create table myTable(id integer,name varchar(20),birthday timestamp)
insert into myTable(id,name,birthday)values(1,'李四',current timestamp) 
insert into myTable(id,name)values(2,'wang') 

导出为CSV格式 
db2=> export to "/temp/myTable.csv" of del select * from myTable  --除了数字,都是以""引用的数据,导出为UTF-8
EXPORT TO "/tmp/mytable.csv" OF DEL MODIFIED BY codepage=1208 COLDEL, MESSAGES "/tmp/db2_export_mytable.log" SELECT * FROM mytable
--codepage=1386 为GBK,1208 为UTF-8  
默认的字符分隔符是双引号,字段分隔符是逗号,0x22是双引号,0x2c是逗号,0x09 是\t ,0x20是空格
EXPORT TO "/tmp/mytable.csv" OF DEL MODIFIED BY codepage=1208 chardel0x22 coldel0x2c MESSAGES "/tmp/db2_export_mytable.log" SELECT * FROM mytable
EXPORT TO "/tmp/mytable.csv" OF DEL MODIFIED BY codepage=1208 nochardel coldel0x2c MESSAGES "/tmp/db2_export_mytable.log" SELECT * FROM mytable
如何以,\t 两个字符分隔  ???

导入CSV文件
LOAD CLIENT FROM "/tmp/mytable.csv" OF DEL  INSERT INTO mytable
LOAD CLIENT FROM "/tmp/mytable.csv" OF DEL MODIFIED BY CODEPAGE=1208 COLDEL, MESSAGES "/tmp/db2_load_mytable.log" INSERT INTO mytable

--备份
备份数据
db2move <数据库别名> export -u <user> -p <password>  会在目录中征生成db2move目录,中有db2move.lst(.msg和ixf对应关系),EXPORT.out(屏幕输出),tab<n>.msg(数据对应信息),tab<n>.ixf(存数据)
	-sn <schema_name> 
	-tn <table_name>　多个以,分隔
db2move <数据库别名> import  -u <user> -p <password> 不用建立表, 表不能有自增长的identity列(最好用sequence),但可以用db2 import 来导入
db2move <数据库别名> load  -u <user> -p <password> 之前要先建立表
--
db2look -d <数据库别名> -e -o the_ddl.sql -td @  -i <identiti_user> -w <password>
				-td 指定分隔符默认';' 当有 function 脚本时不能用';'
				 -e：抽取 DDL 文件，复制数据库需要此文件
				-t：对指定的表
				-z : schema
db2look -d sample -e -o myTable.sql -z zhaojin -t myTable
				
----------DB2客户端连接服务端的方法
$ db2set –all 显示概要文件注册表设置 
db2 update database manager configuration using svcename DB2_db2inst1
db2set DB2COMM=tcpip
 

->db2 catalog tcpip node <node_name> remote <hostname|ip_address> server 50000 ostype <NT|SUN||LINUX> (用list node directory 可看到新加的node)
->db2 catalog db <databaseName> at node <node_name>   ====或者====　db2 catalog database <db_name> as <db_alias> at node <node_name>
->db2 connect to <databaseName> user <db2> using <Pwd>    
->db2 uncatalog node <node_name>  . 取消节点编目

6. 编目数据库
db2 catalog database <db_name> as <db_alias> at node <node_name>

7. 取消数据库编目
db2 uncatalog database <db_name>

8. 测试远程数据库的连接
db2 connect to <db_alias> user <user_id> using <password>

--linux 连接windows OK

db2 get dbm cfg | findstr SVCENAME (windows)  db2c_DB2
C:\Windows\System32\drivers\etc\services (windows) 中配置  --表示监听端口
db2c_DB2	50000/tcp  

db2 catalog tcpip node zhNode remote   10.1.2.60 server 50000 ostype NT
db2 catalog db sample as zhSamp at node zhNode
db2 connect to zhSamp user zhaojin using abs
-------
db2look工具

db2 get dbm cfg | grep SVCENAME  -- 在linux 显示的是8000端口
cat /etc/services     -- 在inux无新增对应于8000的项 
 

db2 update dbm cfg using svcename 50000
db2 update database manager configuration using svcename db2c_DB2
db2set DB2COMM=tcpip
db2stop force
db2start

---远程连接 Toad工具成功 
db2 catalog tcpip node devNode remote 10.1.5.226 server 8000 --注册节点
db2 catalog database sample as devsampl at node devNode  --注册数据库  ,as 后的名最长8个
--提示 直到刷新目录高速缓存之后，目录更改才生效
--db2 catalog database sample            at  node devNode authentication server

如要删  db2 uncatalog node devNode --删除注册节点
如要删  db2 uncatalog database devsampl --删除注册数据库

LIST NODE DIRECTORY 查看到所有的catalog node,有 devNode 是tcpip 类型 IP和端口
list database directory 查看到所有的catalog database

使用Toad for DB2连接OK

 db2 connect to  devsampl user db2instl using 123   -- user后的db2 是OS用户


---------

$ db2level 显示当前DB2系统的版本号和补丁号 
$ db2ilist 显示操作系统中建立的所有实例名称 
$ db2 get instance  显示当前操作的实例名称 
$ db2 get dbm cfg 显示   数据库管理器配置   | grep -i service 显示的在/etc/services中有吗?  (端口)
$ db2 get admin cfg 显示 管理服务器配置 
$ db2 get db cfg for {database_name}  显示数据库配置：
$ db2set –all 显示概要文件注册表设置 
 
  
$ db2 get snapshot for dbm    获取整个数据库管理器的运行统计信息：

$ db2 get snapshot for all databases  获取所有数据库的运行统计信息：

$ db2 get snapshot for all applications 获取所有应用程序的运行统计信息：

$ db2 get snapshot for all bufferpools 获取所有缓冲池的运行统计信息：

$ db2 get snapshot for database on {database_name}获取单个数据库的运行统计信息：

$ db2 get snapshot for applications on {database_name}获取单个数据库中所有应用程序的运行统计信息：

$ db2 get snapshot for tables on {database_name} 获取单个数据库中所有表的运行统计信息：

$ db2 get snapshot for tablespaces on {database_name} 获取单个数据库中所有表空间的运行统计信息：

$ db2 get snapshot for locks on {database_name} 获取单个数据库中所有锁的运行统计信息：

$ db2 get snapshot for bufferpools on {database_name} 获取单个数据库中所有缓冲池的运行统计信息：

$ db2 get snapshot for dynamic sql on {database_name} 获取单个数据库中所有动态SQL语句的运行统计信息：

$ db2 reset monitor all  需要时，可以复位统计信息，重新开始以上的信息收集：



CREATE BUFFERPOOL BP3 SIZE 2000 PAGESIZE 8K
SELECT * FROM SYSCAT.BUFFERPOOLS

CREATE TABLESPACE USERSPACE3
	PAGESIZE 8K
	MANAGED BY SYSTEM
	USING ('/opt/ibm/db2/userspace3_cont1', '/opt/ibm/db2/userspace3_cont2')
	EXTENTSIZE 64
	PREFETCHSIZE 32
	BUFFERPOOL BP3
	OVERHEAD 24.1
	TRANSFERRATE 0.9

create table myTable(id int ) in USERSPACE3



----------
create bufferpool cdcbuffer size 3000 pagesize 32k   --  pagesize可 4k,8k,16k,32k
create regular tablespace  tablespace1 pagesize 32k  managed by database using(file '/home/db2instl/tablespace1' 1g) bufferpool cdcbuffer

create regular tablespace  tablespace1 pagesize 32k  managed by database using(file '/home/db2instl/tablespace1' 1g)  extentsize 32  prefetchsize automatic bufferpool cdcbuffer  no file system caching DROPPED TABLE RECOVERY OFF

create table cdcTable(id int ) in tablespace1

list tablespaces 	
list tablespaces show detail   --查看 Total pages =(1g/32k),Used pages,Free pages
LIST TABLESPACE CONTAINERS FOR 7 SHOW DETAIL  -- 7 是tablespace ID ,可以显示对应的文件路径
 
ALTER TABLESPACE tablespace1 ADD (file '/home/db2instl/tablespace1_0' 10000 )  -- 增加 10000 页

PREFETCHSIZE 必须是 EXTENTSIZE 的倍数

CREATE TABLESPACE 时指定 DROPPED TABLE RECOVERY OFF  来禁用已删除表的恢复功能

日志满
修改日志文件大小：DB2 "update db cfg for <dbname> using LOGFILSIZ 4096"
修改主日志文件个数：DB2 "update db cfg for <dbname> using LOGPRIMARY 6"
修改辅助日志文件个数：DB2 "update db cfg for <dbname> using LOGSECOND 10"



create schema my 

---复制表

1)创建表结构
db2 "create table myuser_copy like myuser"   -- 不会带comment和contraint,但会带 not null,default(建时直接写在字段后的那种)
2)插入数据
db2 "insert into myuser_copy select * from myuser"

方式二
create table myuser_copy2 as ( select * from myuser ) data initially deferred  refresh deferred
refresh table myuser_copy2
--在工具中看不到表????

--只创建表结构
create table myuser_copy2  as  (select * from myuser)  definition only  ---- 不会带comment 和 contraint,但会带not null,不带default

---DB2 没有truncate 
 ALTER TABLE table_name ACTIVATE NOT LOGGED INITIALLY WITH EMPTY TABLE;
	 
--查所有的表名
select * from user_tables
select tabname from syscat.tables where tabschema = current schema ;
select name  from sysibm.systables where type='T'  and creator='DB2INSTL' 