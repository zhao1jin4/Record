
mysql优化配置，mysql集群，热备，故障切换，mysql服务器管理

ACID 
A: atomicity.
C: consistency.
I: isolation.
D: durability. 


不发出错误时的声音
登录的时候用 mysql   --no-beep  ( -b )
或者配置mysql   的my.ini 
或者“我的电脑”上点击右键－“属性”－“硬件”－“设备管理器”，然后点击“查看”，勾上“显示隐藏的设备”，然后在下面找到“beep”并双击，将其改成“不要在当前硬件配置文件中使用这个设备（停用）” 


linux下安装mysql在bin/mysqlaccess 18行改$PATH=指定安装的路径,最好是(/usr/local/mysql)
------------MySQL-8 windows zip 版
my.ini

[mysql]
#default-character-set=utf8
[mysqld]
character_set_server=utf8
default-storage-engine=INNODB
basedir=D:\\Program\\mysql-8.0.11-winx64\\
datadir=D:\\Program\\mysql-8.0.11-winx64\\data


#mysqld  --defaults-file=D:/Program/mysql-8.0.11-winx64/my.ini --initialize  # root 密码在.err日志里 
#sc create MySQL8  binpath= "\"D:/Program/mysql-8.0.11-winx64/bin/mysqld\" --defaults-file=\"D:/Program/mysql-8.0.11-winx64/my.ini\" MySQL8" type= share  start= auto displayname= "MySQL8"



MySQL8 安装后老的UI(DbVisualizer-10.0.15)工具报 Authentication plugin 'caching_sha2_password' cannot be loaded 的解决方式 
ALTER USER zh@localhost IDENTIFIED WITH mysql_native_password   BY '123';
ALTER USER zh@'%'  IDENTIFIED WITH mysql_native_password   BY '123';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';  

------------MySQL-5.7 windows zip 版
 mysqld启动服务，默认INNODB,默认字符集是latin1
  
mysqld --install MySQL57 --defaults-file=E:\\Program\\mysql-5.7.18-winx64\\my.ini  ###安装服务为MySQL 不能启动,--defaults-file前加任何参数,Bug!!!! 可在最后加 --console 来看启日志,
mysqld --remove MySQL57

##OK 
mysqld  --defaults-file=E:/Program/mysql-5.7.18-winx64/my.ini --initialize   ##这是5.7版本新的要做的,root 密码在.err日志里 
mysqld  --defaults-file=E:/Program/mysql-5.7.18-winx64/my.ini   --console  
 如果不指定 --console   输出到data_dir下 或者用 --log-error 

##OK    ,type= own  ,要在=后有一个空格,建立删除如不行,不要在注册表中选中,关闭services.msc
sc create MySQL57 binpath= "\"E:/Program/mysql-5.7.18-winx64/mysql-5.7.18-winx64/bin/mysqld\" --defaults-file=\"E:/Program/mysql-5.7.18-winx64/my.ini\" MySQL57" type= own start= auto displayname= "MySQL57"
sc delete MySQL57 删服务
 

---my.ini
[mysql]
#default-character-set=utf8
[mysqld]
character_set_server=utf8
default-storage-engine=INNODB
basedir=E:\\Program\\mysql-5.7.18-winx64\\mysql-5.7.18-winx64
datadir=E:\\Program\\mysql-5.7.18-winx64\\data
---
sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
max_connections=100			###  最大连接数 
port=3306

mysql -h 127.0.0.1 -P 3306 -u root -proot -D test


mysqladmin -uroot -proot shutdown  停止服务
mysqld  来启动


改密码：
mysqladmin -u用户名 -p旧密码 password 新密码  -h 主机
mysqladmin -uroot  -p password root  

create user root@'%' identified by 'root';  -- root用户可以用远程连接

create database mydb; /* default char set utf8  */
show variables like 'character_set_%'; 会显示客户端和服务端 不同 ,set names utf8 修改客户端
alter database mydb default char set utf8   ;

system pwd 调用Linux操作系统命令

create user user1 identified by 'user1';  /*只是建立host为 % 的mysql.usr表记录  ,还要为localhost的一条*/
create user user1@localhost identified by 'user1'; 

GRANT ALL   ON mydb.* TO 'user1'@'%';
Revoke all on mydb.*  from user1@'%';

1.create database bugs default character set utf8
2.GRANT all ON bugs.*  TO bugs@localhost IDENTIFIED BY 'bugs'; ###MySQL8不支持grant 带IDENTIFIED 
3.GRANT all ON bugs.*  TO bugs IDENTIFIED BY 'bugs'; ###linux源码安装这样是无密码？？？

SHOW GRANTS;
SHOW GRANTS FOR 'root'@'localhost';
SHOW GRANTS FOR CURRENT_USER;

REVOKE select ON mydb.* FROM 'user1'@'%';

grant select on mydb.*    to user1@'%'; 
grant drop on mydb.*    to user1@'%'; 
grant insert  on mydb.*    to user1@'%'; 
grant update  on mydb.*    to user1@'%'; 
grant delete  on mydb.*    to user1@'%'; 
grant lock tables on mydb.*    to user1@'%'; 


没有用户会自动创建,密码为123 ,''号必须要有,drop user bugs@localhhost  ,mysql -ubugs -p123

 create user zh  identified by 'zh';
 改密码：
	mysqladmin -uroot -proot password newpassword
	mysqladmin -u用户名 -p旧密码 password 新密码		
	root 改其它用户的  SET PASSWORD [FOR user] = PASSWORD('some password');##linux源码安装这样无效？？？？？0行影响
	update mysql.user set password=password('zh') where user='zh';
 
use mysql;
select user,host from user;  host字段为%表示可以在所有主机上登录mysql(看root用户有全部)



windows  还原 mysql的root密码
1.停止mysql服务
2.使用 mysqld --skip-grant-tables 启动mysql
3.root无密码登录,执行
	use mysql
	update user set password=password("new_password") where user="root";
	flush privileges; 

	
net start(stop) mysql 

==============linux MySQL  安装
/usr/local/mysql/bin/mysql_config --libs --cflags

---------mysql 源码安装

mysql-5.7.17 在 centOS-7.0
依赖 CMake (可yum安装) 下载  cmake-3.8.0-rc2(https://cmake.org) 运行./bootstrap --prefix= ,再make ,再make install
依赖 g++,make,gmake
依赖 boost C++ libraries 对应官方要求版本 (http://www.boost.org/)  cmake . -DWITH_BOOST=/usr/local/boost_1_59_0
#依赖 bison (如是development source tree)

CMAKE_INSTALL_PREFIX, MYSQL_TCP_PORT, and MYSQL_UNIX_ADDR 

cd mysql-5.7.17 
../cmake-3.8.0-rc2/bin/cmake  . -DWITH_BOOST=/zh/mysql-src/boost_1_59_0 -DCMAKE_INSTALL_PREFIX=/opt/mysql -DMYSQL_TCP_PORT=3308 -DMYSQL_UNIX_ADDR=/opt/mysql-files/mysql.sock 
如boost 版本不对应,会提示的
又报没有安装curses,yum没有??  yum install  ncurses-devel 也不行??




-------老版本 源码安装
SUSE-11 提示checking for termcap functions library... configure: error: No curses/termcap library found
rpm -ivh termcap-2.0.8-981.18.i586.rpm 没用的 ，只有ncurses-utils-5.6,要下载ncurses并安装OK


./configure --prefix=/usr/local/mysql   --with-plugins=innobase --with-charset=utf8 #gbk 
#默认--datadir=PREFIX/share
#默认--localstatedir=PREFIX/var
      --sysconfdir=DIR        read-only single-machine data [PREFIX/etc]
 

make #20分钟
make install

show plugins

groupadd mysql	 
useradd mysql -g mysql	 

cd /usr/local/mysql


chown -R mysql .			##--关键1
chgrp -R mysql .



##cp share/mysql/my-medium.cnf /etc/my.cnf     
  
./bin/mysql_install_db --user= mysql     ##--关键2
 
cp ./share/mysql/mysql.server /etc/rc.d/mysql  ##可以正常使用

 
[mysqld]
##datadir=/usr/local/mysql/data

default-storage-engine=INNODB
default-character-set=utf8

有字符集UTF8的
建数据库默认也是UTF8的
SHOW CREATE DATABASE bugs

SHOW [STORAGE] ENGINES //如没有innodb  编译时加--with-plugins=innobase
mysqladmin variables -uroot -proot >variables.txt //如没有innodb ,
show variables like 'storage_engine'  
  
日志：vi /usr/local/mysql/var/[hostname].err
--- linux 二进制安装 mysql-5.7.17

看doc
shell> groupadd mysql
-- shell> useradd -r -g mysql  mysql   #-r表示系统帐户没有/etc/shadow记录 -s /bin/false
shell> tar zxvf /path/to/mysql-VERSION-OS.tar.gz
shell> ln -s full-path-to-mysql-VERSION-OS /usr/local/mysql
shell> cd /usr/local/mysql
shell> mkdir mysql-files
shell> chmod 750 mysql-files
shell> chown -R mysql .
shell> chgrp -R mysql .
-- shell> bin/mysql_install_db --user=mysql # Before MySQL 5.7.6
shell> bin/mysqld --initialize --user=mysql # MySQL 5.7.6 and up  不指定配置默认在data目录 
--defaults-file=/zh/mysql-files/my.cnf   
--explicit_defaults_for_timestamp --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data --log-error=/usr/local/mysql/mysql-files/mysql-error.log 
提示生成了临时的 root@localhost的密码

shell> bin/mysql_ssl_rsa_setup # MySQL 5.7.6 and up
--datadir=/usr/local/mysql/data 
shell> chown -R root .
shell> chown -R mysql data mysql-files
-- shell> bin/mysqld_safe --user=mysql &
# Next command is optional
shell> cp support-files/mysql.server /etc/init.d/mysql.server   -- openSUSE-15不行


建立用户

5.7.20版本没有support-files/my-default.cnf 
#cp support-files/my-default.cnf  /zh/mysql-files/my.cnf  #文件内容比较空，

bin/mysqld --verbose --help

[mysqld]
#log_bin=ON
#server-id =1  			#变量是server_id(show variables like 'server_id'),命令行是 --server-id
#default-storage-engine=INNODB
#default-character-set=utf8

#lc-messages-dir=/zh/mysql-files/share    
	-- 不加正常默认值为<basedir>/share/(但目录中有很多文件,但没有errmsg.sys)
	-- 加了报ERROR没有/zh/mysql-files/share/errmsg.sys文件,但能使用
#log-error=/zh/mysql-files/mysql-error.log  #正常也写这,默认输出控制台
#log-warnings=/zh/mysql-files/mysql-warnings.log
#log-syslog=/zh/mysql-files/mysql-sys.log

#skip-grant-tables=ON
#explicit_defaults_for_timestamp=ON

basedir =/zh/mysql-5.7.17-linux-glibc2.5-x86_64  --默认值是/usr/local/mysql/
datadir =/zh/mysql-files/data
port =3308
socket =/zh/mysql-files/mysql.sock  #默认/tmp/mysql.sock

如一台机器有多个mysql 要设置如下参数
 --port 
 --socket 
--shared-memory-base-name=name
    This option is used only on Windows.

--pid-file=file_name
--general_log_file=file_name
--log-bin[=file_name]
--slow_query_log_file=file_name
--log-error[=file_name] 
--tmpdir=dir_name 

 
bin/mysqld  --defaults-file=/zh/mysql-files/my.cnf   --initialize --user=mysql   #日志中会提示有root临时密码
bin/mysql_ssl_rsa_setup  --datadir=/zh/mysql-files/data
 --defaults-file=/zh/mysql-files/my.cnf 
--启动 mysql
su - mysql
bin/mysqld  --defaults-file=/zh/mysql-files/my.cnf 

bin/mysql -u root   -P 3308  -h localhost -S /zh/mysql-files/mysql.sock  #临时密码,对不知道root密码不能登录
mysqladmin 默认读配置顺序/etc/my.cnf /etc/mysql/my.cnf /usr/local/mysql/etc/my.cnf ~/.my.cnf 

linux 还原 mysql 的 root密码方法	 
	bin/mysqld  启动时my.cnf中加 skip-grant-tables=ON
	无密码登录 #
	use mysql 

	UPDATE user SET authentication_string=PASSWORD('new_root')   -- 如root密码过期,下面没用 , password_expired='N'
	WHERE Host='localhost' AND User='root';
	
	SELECT User, Host, HEX(authentication_string) FROM mysql.user;
	
	停止数据库,再去skip-grant-tables 启动,测试OK
	create user root@'%' identified by 'root'; 
    grant all on mysql.* to root@'%';
	
--停止mysql
bin/mysqladmin  -u root -p -P 3308 -S /zh/mysql-files/mysql.sock shutdown     提示密码过期,要修改,-h localhost 也要加-S ,除非-h 127.0.0.1(可能要能远程登录)
	  ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';  -- password expire never
	  SET PASSWORD FOR 'root'@'localhost' = PASSWORD('new_root');
	
/etc/init.d/mysql  stop

---修改root密码
mysqladmin -uroot -p  password root   -S /zh/mysql-files/mysql.sock
mysqladmin -u用户名 -p旧密码 password 新密码  -h 主机 -S socket文件路径
mysqladmin -uroot  -p password root   -S /zh/mysql-files/mysql.sock

---- openSUSE 15 使用 mysql 客户端 要libtinfo.so.5 而实际上有libncurses6-6.1 ,zypper install libncurses5

==========Mycat 1.6
OSI PI 实时数据库
 
MySQL 本身支持表分区 

http://www.mycat.io/
基于阿里开源的Cobar产品
 
	数据库分库(不支持分表)中间件，集群基于ZooKeeper管理,支持前端作为MySQL通用代理，后端JDBC方式支持Oracle、DB2、SQL Server 、 mongodb 

	按照不同的表（或者Schema）来切分到不同的数据库（主机）之上，这种切可以称之为数据的垂直（纵向）切分  （不能join要接口解决，事务复杂）
	表中的数据的逻辑关系，将同一个表中的数据按照某种条件拆分到多台数据库（主机）上面，这种切分称之为数据的水平（横向）切分。
	
	用户ID求模  这样所有的数据查询join都会在单库内解决
    跨节点合并排序分页问题
	
linux：

./mycat start 启动  默认监听8066

./mycat stop 停止

./mycat console 前台运行

./mycat restart 重启服务

./mycat pause 暂停

./mycat status 查看启动状态
win：

直接运行startup_nowrap.bat，如果出现闪退，在cmd 命令行运行，查看出错原因。


conf/wrapper.conf  可以修改JVM配置参数

mysql -uroot -proot -P8066 -hlocalhost 

conf/server.xml
	<system> 下的属性
	<user> 下的属性
	
conf/schema.xml
conf/rule.xml
conf/zkconf/


==========MySQL NDB cluster 7.1  solaris----OK   现在已经有7.5.9版本了

无共享存储设备 （Share Nothing）
要将所有索引装载在内存中


新的是客户端连接到MySQL Router 再连接到(可用 X Protocal ) InnoDB Cluster
MySQL Shell (使用JavaScript 或者 Python)可以交互(控制)MySQL Router
新的插件 DocumentStore/NoSQL , X DevAPI, X Protocal 

NDB(Network DataBase)
节点(node)在这里含义是进程(process),一台机器可以有多个节点,节点所在的机器叫(cluster host)

一台(多台) 管理(MGM)节点:	启动其他节点之前首先启动这类节点,ndb_mgmd启动的 , 客户端ndb_mgm, 只要这两命令即可  
多台 数据节点:	用命令ndbd启动的 , ndbmtd (multi-threaded) ,只要这两个命令即可  
多台 SQL节点:	mysqld –ndbcluster --ndb-connectstring 启动的,或将ndbcluster添加到my.cnf后使用mysqld启动   ,NDBCLUSTER 存储引擎

ndb_restore是用来恢复备份

所有主机上的文件系统是等同的


自己的子网内运行MySQL簇，不与非簇机器共享该子网,点之间的通信未采用任何特殊加密或防护

管理节点，不需要安装mysqld可执行文件

数据存储均是在内存中进行,可以基于 磁盘的存储

运行configure时，务必使用“--with-ndbcluster”选项。
也可以使用BUILD/compile-pentium-max创建脚本


--------



管理节点(MGM)	 mgmtHost		不必须安装MySQL, 有ndb_mgmd和ndb_mgm
SQL节点1(SQL1)	 sqlHost	安装MySQL
数据节点1(NDBD1) dataHost1	
数据节点2(NDBD2) dataHost2

每个节点都要安装
cp support-files/my-small.cnf  /etc/my.cnf
./scripts/mysql_install_db --user=mysql,启动方式见下


安装存储节点,要ndbd　命令，要  cp  mysql-cluster-gpl-7.1.3-solaris10-sparc-64bit/bin/ndbd 
安装SQL节点 ,要使用cluster版的 ln -s  mysql-cluster-gpl-7.1.3-solaris10-sparc-64bit /usr/local/mysql


配置SQL节点  # vi /usr/local/mysql/my.cnf

	[mysqld]
	basedir         = /usr/local/mysql/
	datadir         = /usr/local/mysql/data
	user            = mysql
	port            = 3306
	socket          = /tmp/mysql.sock
	ndbcluster
	ndb-connectstring=mgmtHost
	[MYSQL_CLUSTER]
	ndb-connectstring=mgmtHost


配置存储节点(NDB节点) # vi /usr/local/mysql/my.cnf

	[mysqld]
	ndbcluster
	ndb-connectstring=mgmtHost
	[MYSQL_CLUSTER]
	ndb-connectstring=mgmtHost


安装管理节点  ndb_mgm和ndb_mgmd 
# cp mysql-cluster-gpl-7.1.3-solaris10-sparc-64bit/bin/ndb_mgm*  /usr/local/mysql/bin/
# chown -R mysql:mysql /usr/local/mysql



配置管理节点	# vi /usr/local/mysql/config.ini  ###
	[NDBD DEFAULT]
	NoOfReplicas=1			
	#每一份数据被冗余存储在不同节点上面的份数，一般为2
	[TCP DEFAULT]
	portnumber=3306
	
	#设置管理节点服务器
	[NDB_MGMD]
	id=1
	hostname=mgmtHost
	#MGM上保存日志的目录
	datadir=/usr/local/mysql/data/
	
	#设置SQL节点服务器
	[MYSQLD]
	id=2
	hostname=sqlHost

	#设置存储节点服务器(NDB节点)
	[NDBD]
	id=3
	hostname=dataHost1
	datadir=/usr/local/mysql/data/
	
	[NDBD]
	id=4
	hostname=dataHost2
	datadir=/usr/local/mysql/data/


可以有多个　[NDBD]和[MYSQLD],　[NDB_MGMD]和[NDBD]和[MYSQLD]中也可以有　Id=1,Id=2 ...


合理的启动顺序是
1.启动管理节点	# /usr/local/mysql/bin/ndb_mgmd -f /usr/local/mysql/config.ini
2.启动存储节点，如第一次启动ndbd进程的话 # /usr/local/mysql/bin/ndbd --initial
		或在备份/恢复数据或配置文件发生变化后重启ndbd时使用“--initial”参数	
		不是第一次# /usr/local/mysql/bin/ndbd
3.启动SQL节点	# /usr/local/mysql/bin/mysqld_safe --defaults-file=/etc/my.cnf &
	
管理节点服务器上 客户端 #/usr/local/mysql/ndb_mgm
			ndb_mgm> SHOW

管理节点要有1186端口监听	netstat -an | grep 1186

必须用 ENGINE=NDB 或 ENGINE=NDBCLUSTER 选项创建,或用ALTER TABLE选项更改
如mysqldump导入表  添加ENGINE选项

每个NDB表必须有一个主键


安全停止
1.停止MGM节点	 # /usr/local/mysql/bin/ndb_mgm -e shutdown  也会把数据节点停
2.停止SQL节点的mysqld服务# /usr/local/mysql/bin/mysqladmin -uroot shutdown



测试 在一个 SQL 节点上，建表 ENGINE=NDB 插数据，

JDBC连接SQL节点 OK,

==========MySQL InnoDB cluster
至少3个MySQL服务实例，每个实例运行 Group Replication,内建failover
 AdminAPI 
 Time for Node Failure Recovery 要 30 seconds or longer 
支持 MVCC，Transactions 支持所有的,而NDB只支持 READ COMMITTED




=========Replication 
默认是异步的,不是持续连接,可以指定数据,指定表

--主服务器的 my.ini

log_bin=mysql-log-bin
log_bin_index=/data/mysqld-bin.index  -- 默认是<datadir>/<log_bin>.index 
server-id=1


在主上建立用户(doc上的)
CREATE USER 'repl'@'%.mydomain.com' IDENTIFIED BY 'slavepass';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%.mydomain.com';

CREATE USER 'repl'@'%' IDENTIFIED BY 'slavepass';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';

show master status; 看log_bin文件名就是配置的
主上用 show processlist 可以看到都有哪些从

---从服务器的 my.ini 
#log_bin=mysql-log-bin #正常不用打开,Auto-Failover功能用 MySQL Utilities  ,也可从加 mysqld --gtid-mode=ON
server-id=2
relay_log=<host_name>-relay-bin --默认值空
relay_log_index=<host_name>-relay-bin.index  --默认值在data目录下



mysql> change master to 
->master_host='10.1.5.226',
->master_port=3308,
->master_user='repl',
->master_password='slavepass';

MASTER_LOG_FILE='recorded_log_file_name',
MASTER_LOG_POS=recorded_log_position;

start slave;  -- 主上有日志, stop slave;
-- 从上也有了repl用户
SHOW SLAVE STATUS;  -- 有权限执行, 可以看有错误日志,报错误可能是用户或者数据库已经存在
Slave_IO_Running 和 Slave_SQL_Running 要为YES

主从上用 show processlist  看
开始在主上测试,建立库,表,测试OK,slave 两台测试OK


备份master/salve 的做法
FLUSH TABLES WITH READ LOCK;
SET GLOBAL read_only = ON;
mysqldump --all-databases --master-data > dbdump.db
SET GLOBAL read_only = OFF;
UNLOCK TABLES;


--- Scale-Out
========= Group Replication
可以多个Master



========= MySQL Utilities-1.6.5
 包括 Fabric
解压 依赖python-2.7
cd mysql-utilities-1.6.5
python ./setup.py build
sudo python ./setup.py install  安装在/usr/local/bin下 mysql开头的一些工具

mysqlfailover 命令
	全局事务标识符(GTIDs) 
	gtid_mode=ON 
	--interval 设置间隔秒,使用ping来检查服务是否活(--ping),如果服务不可用 使用  --failover-mode 默认auto
	如果为elect 而从--candidates 中来选择(而不是全部)做为master
	--master-info-repository=TABLE
 要求所有的从设置 --report-host 和 --report-port  
 --exec-before 和 --exec-after 指定脚本返回0表示成功
 
 my.cnf 中加
gtid_mode=on
master_info_repository=TABLE
 
mysqlfailover --master=root@localhost:3331 --discover-slaves-login=root --log=log.txt

mysqlrplshow
mysqluc 命令
  

========= MySQL Router 2.1.4  ,8.0在开发中
是 InnoDB cluster 的一部分 ，不支持 NDB Cluster
 


=======远程DB的连接,federated引擎
SHOW ENGINES 有FEDERATED 默认不支持,要启用在启动mysql时加 --federated 或者 --federated=ON , 如是my.cnf中则是  federated=ON 
SHOW  STORAGE  ENGINES

CREATE TABLE federated_table (
id INT(20) NOT NULL AUTO_INCREMENT,
name VARCHAR(32),
 PRIMARY KEY (id)
)ENGINE=FEDERATED
DEFAULT CHARSET=utf8
CONNECTION='mysql://user1:pass1@10.1.5.226:3306/test/t_innodb_table';

--方式二
drop server dev;

CREATE SERVER dev
FOREIGN DATA WRAPPER mysql    
OPTIONS (USER 'user1', PASSWORD 'pass1',HOST '10.1.5.226',PORT 3306, DATABASE 'test');  --WRAPPER后的mysql是wrapper_name,只能用mysql

CREATE TABLE t (s1 INT) ENGINE=FEDERATED CONNECTION='dev';

======= BlackHole ：黑洞引擎
======= 备份 与 恢复
load data local infile '/clientDir/xx.xls' into table 表名 CHARACTER SET utf8 FIELDS TERMINATED BY ',\t'
 -- OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\'    
 LINES TERMINATED BY '\r\n' ;
 
 

load data local infile 'D:/home/creditpush/credit_col/20160308/input_HLW_pcc_hlw_loan_repay_all.del' 
into table    pcc_hlw_loan_repay 
CHARACTER SET gbk FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n' 
IGNORE 1 LINES  -- 为首行的标题
SET repay_date = if(repay_date='0000-00-00',null,repay_date), from_sys='COLLEGE' ,create_time=now()

-- 如有default 是 ON UPDATE 类型字段,使用load data 表自身设置值,要使用load data 的set 设置 
-- 对日期date类型,如果文件中没值,会变为0000-00-00 , 用SET 做设置
-- 对新的字段设置自己的值
JDBC driver  中加 ?zeroDateTimeBehavior=convertToNull    
-- 8.0 默认取值变为EXCEPTION, 可选 CONVERT_TO_NULL，ROUND
  
 如数据类型decimal的在文件中是空 ,JDBC 加 emptyStringsConvertToZero=true,含义就变了,0和null是不同的
 或者在 未尾加 SET EXCHANGERATE = if(EXCHANGERATE='',null,EXCHANGERATE) 

 
load data local infile '/clientDir/xx.xls' into table myTable CHARACTER SET utf8 FIELDS TERMINATED BY ',\t' LINES TERMINATED BY '\r\n'  
	SET name = if(name='NULL',null,name);
 
mysqlimport --local dbname /clientDir/tableName.txt  --fields-terminated-by='/' --lines-terminated-by='\r\n' --default-character-set=utf8  
  -h 127.0.0.1 -P 3306 -uuser1  -puser1 
  
mysqlimport 如在windows客户端使用  --fields-terminated-by=/ --lines-terminated-by=\r\n  #即不加'',gbk或utf8都可
  
  
  
  
mysql>source filename.sql
mysql -h host -u user -p < batch-file
 
 
 
--引出 .csv 文件,中文是UTF8字符集(MySQL是这个),因为是SQL,文件位置是MySQL 服务端所在位置,如是相对路径则是 datadir配置目录/<数据库名>/下
--用""号,可以防止数据中有"," , 导出csv 用excel 打开不会显示""
--为了避免excel科学计数法显示丢失精度,以 ,\t分隔字段
--但如果\t和"结合就会把"显示在excel中
select 'id','name' from dual 
union
SELECT  id,name 
	INTO OUTFILE '/tmp/db_name/tbl_name.csv' 
	FIELDS TERMINATED BY ',\t' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
FROM tbl_name;

drop user user2@localhost ;
drop user user2@'%';
create user user2@localhost identified by 'user2'; 
create user user2@'%' identified by 'user2'; 

--  INTO OUTFILE 要 file (privileges) on *.* 权限 ,连接用户必须重新连接才生效
grant file privileges on *.* to user2@localhost ;
grant file privileges on *.* to user2@'%';
FLUSH PRIVILEGES ;
 


--只是屏幕数据抓下,没有---+---形式的表格
select   *   from   users   into   outfile   'c:/temp/users.txt';  
mysql -h 127.0.0.1 -uuser1 -puser1 -D test -e "select * from myTable">c:/temp/myTable.txt  
--以上两者效果一样的 
来生成CSV,以\r\n结束   
mysql -h 127.0.0.1 -uuser1 -puser1 -D test -e "select * from myTable" | sed 's/\t/",\t"/g;s/^/\t"/g;s/$/"\r/g'> myTable.csv  --带""
mysql -h 127.0.0.1 -uuser1 -puser1 -D test <query.sql | sed 's/\t/,\t/g;s/^/\t/g;s/$/\r/g'> myTable.csv   --不带 ""


select *  into dumpfile '/tmp/myTable.dump' from myTable where id=1  -- 文件不能存在,只能导出一条数据,字段间没有分隔, null在vi中显示^@ , 用途不是很大
 
===== mysqldump 命令
把数据库 或某个表 导出sql文件,即生成数据库create table , insert into 脚本

--add-drop-database
--add-drop-table
--compatible=name  和哪种数据库相兼容,值可以为 postgresql ,oracle,mssql,db2

--extended-insert 默认开(如为true,很多数据使用一条insert来完成,导入时性能高) --skip-extended-insert  来禁用 
	show variables like '%max_allowed_packet%'   -- 即一条SQL的最大长度 默认4M
	show variables like '%bulk_insert_buffer_size%' 默认8M  
	set global bulk_insert_buffer_size=64*1024*1024 
	set session bulk_insert_buffer_size=64*1024*1024 
	
--complete-insert,-c	默认关,如为true,包含insert字段名,防止加字段导致不能导入备份数据

--default-character-set=charset   ,导出时设置为导入时客户端的字符集,如在windows导入设置为gbk
--disable-keys
	 在 INSERT 语句的开头和结尾增加 /*!40000 ALTER TABLE table DISABLE KEYS */  和 /*!40000 ALTER TABLE table ENABLE KEYS */ 
	 这能大大提高插入语句的速度,默认打开,使用 --skip-disable-keys 禁用

--hex-blob
--routines -R 		导出存储过程以及自定义函数。
--triggers  		同时导出触发器。该选项默认启用，用 --skip-triggers 禁用它
-q, --quick         Don't buffer query, dump directly to stdout. (Defaults to on; use --skip-quick to disable.)

--opt  同  --add-drop-table, --add-locks, --create-options, --quick, --extended-insert, --lock-tables, --set-charset, --disable-keys 
	  默认启动,--skip-opt 来禁用

--single-transaction 只对innodb engine

--set-charset  写 'SET NAMES default_character_set' ,默认开 ,--skip-set-charset 来禁用
 
--no-create-info，-t 
只导出数据，而不添加 CREATE TABLE 语句。
--no-data , -d 
不导出任何数据，只导出数据库表结构。

--lock-tables=false   默认true , 为 insert 前加,LOCK TABLES myTable WRITE; 再insert完成后加UNLOCK TABLES;  可 --skip-lock-tables
 --add-locks  可--skip-add-locks 
 -B , --databases 
 --tables  会覆盖  --databases 
 -A, --all-databases
 -d, --no-data  不要数据，只要表结构
--ignore-table=db_name.tbl_name,db_name2.tbl_name2
--set-gtid-purged=OFF

alter table large_table  disable keys 会停止更新索引,大量数据导入性能变高


单个用工运行mysqldump 下会生成 use  dbName; 语句 注意！！！ 增加项 --add-drop-database=false
如果使用linux sh 写命令就不会生成 use  dbName; 语句 

	
mysqldump -uroot -p -h 127.0.0.1 --add-drop-database=false  dbName table1 table2  > dbName_table1_table2.sql   

mysqldump -uroot -p -h 127.0.0.1  --databases  db1 db2 --ignore-table=db1.tbl_name,db2.tbl_name2 > db1_db2.sql  
mysqldump -uroot -p -h 127.0.0.1  --all-databases   > all_db.sql  
 
备份 Innodb 表  ,把--opt放在最前,后面会覆盖前面 ,linux下生成文件乱码,但用notpad++打开显示正常
mysqldump -h  -uuser1 -puser1  --opt --default-character-set=utf8   --complete-insert=true --lock-tables=false
--triggers -R --hex-blob --single-transaction db_name > db_name.sql
 
mysqldump -uroot -p --opt --default-character-set=utf8   --complete-insert=true --single-transaction   > db_name.sql

mysqldump  -uroot -p  -h 127.0.01   --complete-insert=true  --skip-lock-tables --add-drop-database=false  --default-character-set=gbk  db_name > db_name.sql
当在windows 导入时使用 mysqldump --default-character-set=gbk ,能解决在编辑器中显示正常,导入报乱码错误
 

在导入如不使用工具 ,很有可能中间的有脚本语法错误,而中断,使用 Navicat for MySQL导入,有错误也可继续




======= 
mysqlhotcopy 是一个perl脚本 ,只能用于相同数据库目录的机器,只用于unix机器 上,只支持   MyISAM  和 ARCHIVE 引擎的


create table isam_tbl
(id int ,name varchar(30))
engine=myisam;

MyISAM 的表备份,源库停止服务 或者   FLUSH TABLES isam_tbl WITH READ LOCK ,其它用户还可以查 , 直接复制MyISAM表的 *.frm , *.myd, *.myi 文件到目标库目录
源库 要 UNLOCK TABLES(表级锁),目标库不用重启mysql服务,就可以访问myisam的表了  

INNODB 不能使用复制文件的备份方式


如果你不停止mysql服务,在运行myisamchk之前,至少运行一次 mysqladmin flush-tables 

crontab 中 每周日的0点35分 做
35 0 * * 0 /path/to/myisamchk --fast --silent /path/to/datadir/*/*.MYI
 
mysqladmin proc stat

innodb引擎的文件 .ibd  ,可以create table 中使用 DATA DIRECTORY='/path/dir' 可让每张表在不同的目录,即可不同的硬盘上(如表频繁修改使用SSD)

关闭服务时使用 innodb_fast_shutdown =0选项  , 可以使用开源的 XtraBackup 可以避免关闭服务 , innodb_file_per_table必须打开 

====增量备份 
启动服务时加 --log-bin  <可选路径默认和数据目录相同>   ,my.cnf中打开 log-bin
show variables like 'log_bin%';
log_bin	ON
log_bin_index	/data/mysqld-bin.index

FLUSH LOGS (rotate the binary log 就是切日志文件SHOW BINARY LOGS来看)  或者  mysqldump --flush-logs 

show master status;  当前使用日志文件名


mysqlbinlog 工具 转换二进制到文本 mysqlbinlog binlog_files | mysql -u root -p

mysqlbinlog --skip-gtids binlog.000001 > /tmp/dump.sql
GTIDs ( Replication with Global Transaction Identifiers ) 


mysqlbinlog --stop-datetime="2005-04-20 9:59:59"  /var/log/mysql/bin.123456 | mysql 恢复到指定结束时间

PURGE BINARY LOGS TO 'mysql-bin.010';
PURGE BINARY LOGS BEFORE '2008-04-02 22:46:26';
RESET MASTER 删所有  BINARY LOG  建立空的   show master status  有position位置 ,可以用mysqlbinlog查看生成的SQL注释

flush logs; 会生成新的bin-log日志 

--------查看当前连接
mysqladmin -uroot -proot processlist   

如果有表级锁,会提示有table metadata lock,可以看到正在等待锁的SQL,
如果有行级锁,只能看到正在 updating 的SQL(待锁的),但没有Lock提示,但可以看time花费多少秒(同一个线程id)

show variables like 'innodb_lock_wait_timeout' 单位秒 ,行级锁的超时时间


show full processlist 时间单位秒
mysqladmin -uroot  kill <ID号>  杀查询时间很长的SQL
KILL [CONNECTION | QUERY] processlist_id
  
show status like '%Threads_connected%'; 
show status where variable_name ='Threads_connected'
show status where variable_name ='Max_used_connections'

show variables like '%max_connections%'
set global max_connections= 300
--------

======= SQL 执行历史日志,general_log=1和slow-query-log=1 开启,log-output如为FILE 是在data目录下
-- my.ini 配置(上层以-分隔,最下层以_分隔)  
log-output=NONE

general-log=0
general_log_file="mysql-gen.log"

slow-query-log=0
slow_query_log_file="mysql-query.log"
long_query_time=10

-- 命令中都是以_分隔
show variables like '%general_log%'   
set global general_log=ON;

show variables like '%slow_query_log%' 
set global slow_query_log=ON;
 
show variables like '%log_output%'    
set global log_output='TABLE';  -- root 执行
 
select * from mysql.slow_log   order by start_time desc
select * from mysql.general_log order by event_time desc  --记录多

truncate table mysql.general_log ;

======= 
-----------performance_schema  ,MySQL workbench 有一个 PERFORMANCE 的分组有很多功能
performance_schema.global_variables
performance_schema.session_variables
performance_schema.variables_by_thread
performance_schema.global_status
performance_schema.session_status
performance_schema.status_by_thread
performance_schema.status_by_account
performance_schema.status_by_host
performance_schema.status_by_user


-- select * from information_schema.GLOBAL_STATUS where    VARIABLE_NAME='Max_used_connections'; -- 未来版本会去除,在5.7中已经禁用了  
select * from performance_schema.global_status where    VARIABLE_NAME='Max_used_connections';
show global status   where VARIABLE_NAME='Max_used_connections'


-- select * from information_schema.GLOBAL_VARIABLES where VARIABLE_NAME='autocommit'; -- 未来版本会去除,在5.7中已经禁用了  
select * from performance_schema.global_variables where VARIABLE_NAME='autocommit'
show global variables where VARIABLE_NAME='autocommit' 


select * from mysql.db  显示每个数据库的,对应用户的权限 

select * from performance_schema.events_waits_current
  
  performance_schema.event* 是被写入的
  performance_schema.event_statement_*
  performance_schema.event_stage_*
  performance_schema.event_wait_*

SHOW STATUS LIKE 'perf%';

----------information_schema 
select table_name from information_schema.TABLES where table_schema='db_name'
同 show tables

select * from information_schema.CHARACTER_SETS  所有支持的字符集
select * from information_schema.ENGINES  所有支持的引擎

select * from information_schema.SCHEMA_PRIVILEGES   -- 所有的权限
select * from information_schema.SCHEMA_PRIVILEGES where SCHEMA_PRIVILEGES.GRANTEE like "'myuser1%"

======= 
SHOW STATUS LIKE 'com%'; //表示insert,update,select,delete语句执行次数， 默认是session的，可加 global
SHOW STATUS LIKE 'InnoDB_rows%'; 影响行数
  
linux 的mysql 的表名是区分大小写的, 则windows不区分,字段名都不区分
show variables like 'lower_case_table_names'
my.cnf 中增加 lower_case_table_names=0     --  其中0：区分大小写，1：不区分大小写  重启



mysqladmin -uroot -p create bookstore -- 来创建数据库(无database) 要输入密码
mysqladmin variables -uroot -proot  显示所有的变量
mysqladmin shutdwon -uroot -proot //停止服务
mysqladmin -uroot -proot drop pts //删数据库

select version(); 查看版本
 
mysqladmin flush-logs  关闭当前日志,打开新日志文件

 
show master status
show status

show status like 'Handler_read_key' -- 索引被读的,值高表示查询高效,索引建的好
show status like 'Handler_read_rnd_next'  -- 值高表示查询低效,可以建立索引



 
 
utf8mb4 是 utf8的父级
utf8 一个字符占 3-byte , utf8mb4 一个字符占 4-byte


MyISAM, MEMORY , MERGE 使用 table-level锁
InnoDB 使用 row-level  锁, SELECT ... FOR UPDATE

check table v_emp_mgr -- 可以检查出视图对应的表不存在

-------------表分区
 innodb只有.frm和.ibd文件 默认innodb_file_per_table 是打开的 , 分区就是一个表多个ibd 文件(格式 #P#p0.ibd),多一个 .par 文件

--  RANGE
CREATE TABLE employees (
id INT NOT NULL,
fname VARCHAR(30),
lname VARCHAR(30),
hired DATE NOT NULL DEFAULT '1970-01-01',
separated DATE NOT NULL DEFAULT '9999-12-31',
job_code INT NOT NULL,
store_id INT NOT NULL
)
PARTITION BY RANGE (store_id) (
PARTITION p0 VALUES LESS THAN (6),  -- 值必须是int类型
PARTITION p1 VALUES LESS THAN (11),
PARTITION p2 VALUES LESS THAN (16),
PARTITION p3 VALUES LESS THAN (21)
);


 SELECT PARTITION_NAME,TABLE_ROWS
  FROM INFORMATION_SCHEMA.PARTITIONS
 WHERE TABLE_NAME = 'employees';
 
--   LIST 
 CREATE TABLE employees (
id INT NOT NULL,
fname VARCHAR(30),
lname VARCHAR(30),
hired DATE NOT NULL DEFAULT '1970-01-01',
separated DATE NOT NULL DEFAULT '9999-12-31',
job_code INT,
store_id INT
)
PARTITION BY LIST(store_id) (
PARTITION pNorth VALUES IN (3,5,6,9,17),
PARTITION pEast VALUES IN (1,2,10,11,19,20),
PARTITION pWest VALUES IN (4,12,13,14,18),
PARTITION pCentral VALUES IN (7,8,15,16)
);

 -- RANGE COLUMNS
 CREATE TABLE rcx (
	 a INT,
	 b INT,
	 c CHAR(3),
	 d INT
 )
 PARTITION BY RANGE COLUMNS(a,d,c) (
	 PARTITION p0 VALUES LESS THAN (5,10,'ggg'),
	 PARTITION p1 VALUES LESS THAN (10,20,'mmmm'),
	 PARTITION p2 VALUES LESS THAN (15,30,'sss'),
	 PARTITION p3 VALUES LESS THAN (MAXVALUE,MAXVALUE,MAXVALUE)
 );  --  列的类型很多,数值型,日期,字串型,BINARY 和 VARBINARY
  

-- LIST COLUMNS
 CREATE TABLE customers_1 (
first_name VARCHAR(25),
last_name VARCHAR(25),
street_1 VARCHAR(30),
street_2 VARCHAR(30),
city VARCHAR(15),
renewal DATE
)
PARTITION BY LIST COLUMNS(city) (
PARTITION pRegion_1 VALUES IN('Oskarshamn', 'Högsby', 'Mönsterås'),
PARTITION pRegion_2 VALUES IN('Vimmerby', 'Hultsfred', 'Västervik'),
PARTITION pRegion_3 VALUES IN('Nässjö', 'Eksjö', 'Vetlanda'),
PARTITION pRegion_4 VALUES IN('Uppvidinge', 'Alvesta', 'Växjo')
);

---HASH
CREATE TABLE ti 
(	id INT, 
	amount DECIMAL(7,2), 
	tr_date DATE
)ENGINE=INNODB
PARTITION BY HASH( MONTH(tr_date) )
PARTITIONS 6;

---LINEAR HASH
CREATE TABLE employees (
id INT NOT NULL,
fname VARCHAR(30),
lname VARCHAR(30),
hired DATE NOT NULL DEFAULT '1970-01-01',
separated DATE NOT NULL DEFAULT '9999-12-31',
job_code INT,
store_id INT
)
PARTITION BY LINEAR HASH( YEAR(hired) ) -- 如果timestamp(insert时和update时会自动设置值)类型就不行,date,datetime类型可以
PARTITIONS 4;

-- KEY
CREATE TABLE k1 (
id INT NOT NULL PRIMARY KEY,
name VARCHAR(20)
)
PARTITION BY KEY()
PARTITIONS 2;
--使用 PRIMARY KEY的列 ,如没有使用带 UNIQUE KEY的列


CREATE TABLE members (
firstname VARCHAR(25) NOT NULL,
lastname VARCHAR(25) NOT NULL,
username VARCHAR(16) NOT NULL,
email VARCHAR(35),
joined DATE NOT NULL
)
PARTITION BY KEY(joined)  
PARTITIONS 6;


-- LINEAR KEY
CREATE TABLE tk (
col1 INT NOT NULL,
col2 CHAR(5),
col3 DATE
)
PARTITION BY LINEAR KEY (col1)
PARTITIONS 3;

 

 ---SUBPARTITIONS
 
 CREATE TABLE ts 
 (id INT, 
 purchased DATE
 ) PARTITION BY RANGE( YEAR(purchased) )
SUBPARTITION BY HASH( TO_DAYS(purchased) )
SUBPARTITIONS 2 ( 
PARTITION p0 VALUES LESS THAN (1990),
PARTITION p1 VALUES LESS THAN (2000),
PARTITION p2 VALUES LESS THAN MAXVALUE
);


------性能工具
desc  select ........
explain  select ........   //索引效率,执行计划
 如有 Range checked for each record (index map: 0x2)  就要在相应的表上加 where 条件，差别很大
 
explain extended    <sql> 后,再使用      SHOW WARNINGS 可以给出优化建议的SQL

 show variables like '%optimizer_trace%';
 set optimizer_trace="enabled=on";
 explain select .....;
 select * from information_schema.OPTIMIZER_TRACE;
 
SET profiling = 1;  --  或者ON
show profiles;   显示最近SQL 的过程 及执行时间	有  SET SQL_SELECT_LIMIT=25000

show profile for query 54;  -- 显示这个 SQL 的各个阶段所有时间

optimize table tbl_name; -- 支持分区表 ,如果有删列,大量删数据,可以整理空间,改善IO效率,比较耗时不适合正在使用

SET unique_checks=0;
... import operation ...
SET unique_checks=1;

SET autocommit=0;
... import operation ...
SET autocommit=1;


select  * from xx procedure analyse();//给出数据类型的建议


show engine innodb status;


show global status like '%tmp%';  
	Created_tmp_disk_tables			自动创建的临时表的数量,  增加tmp_table_size和max_heap_table_size的值,以减少在内存临时表被转换为磁盘上的表
	Created_tmp_files				创建的临时文件的数量
	Created_tmp_tables				自动创建的内存临时表的数量
	
	GROUP BY和ORDER BY 无法使用索引时 

在事务提交以后，binlog是先写入缓存，然后由操作系统决定何时刷新到磁盘上,如果事务大小超过定义的缓存 binlog_cache_size，则在磁盘上创建一个临时文件。
show global status like 'binlog_ca%';	
	Binlog_cache_disk_use			超过binlog_cache_size的值并使用临时文件来保存事务中的语句的事务数量
	Binlog_cache_use				缓存的事务数量

	
show global status like 'sort%';
	Sort_merge_passes			如果这个变量值较大，可以考虑增加sort_buffer_size变量的值。
 
show global status like 'Qcache%';  通常 query_cache_type = 0 禁用查询缓存。

show global status like 'table_locks%';
	Table_locks_immediate		产生表级锁定的次数
	Table_locks_waited			发生等待的次数
	-- 没有地查表级锁 等待 数量
show global status like '%row_lock%';
	Innodb_row_lock_current_waits		当前正在等待锁定的数量 -- 测试对的
	Innodb_row_lock_time				从系统启动到现在锁定总时间长度
	Innodb_row_lock_time_avg			每次等待所花平均时间
	Innodb_row_lock_time_max			等待最常的一次所花的时间  milliseconds
	Innodb_row_lock_waits				总共等待的次数 --测试对的,发生行级锁等待的次数
	
show global status like 'Open%tables';
	Opened_tables  已经打开的表的数量如果Opend_tables较大，则需要考虑加大table_open_cache的值。	
	Open_tables
 

show global status like 'threads%';
	在MySQL中每个连接即一个线程。通过thread_cache_size可以减少操作系统的线程创建/销毁，提高性能。
 
 
 
 
select * from time_zone;
select * from time_zone_name;
默认就是空表
show variables like '%time_zone%'  -- SYSTEM

mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root -p  mysql  导入时区相关表 ,windows要下载timezone_2018d_posix_sql.zip
mysql -u root -p mysql < D:/tmp/timezone_2018d_posix_sql/timezone_posix.sql 
显示下面语句
TRUNCATE TABLE time_zone;
TRUNCATE TABLE time_zone_name;
TRUNCATE TABLE time_zone_transition;
TRUNCATE TABLE time_zone_transition_type;
START TRANSACTION;
COMMIT;


