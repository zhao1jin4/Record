
ACID 
A: atomicity.
C: consistency.
I: isolation.
D: durability. 


不发出错误时的声音
登录的时候用 mysql   --no-beep  ( -b )
或者配置mysql   的my.ini 
或者“我的电脑”上点击右键－“属性”－“硬件”－“设备管理器”，然后点击“查看”，勾上“显示隐藏的设备”，然后在下面找到“beep”并双击，将其改成“不要在当前硬件配置文件中使用这个设备（停用）” 


------------MySQL-8 windows zip 版
my.ini

[mysql]
#default-character-set=utf8
[mysqld]
character_set_server=utf8   #新版本是 UTF8MB4
default-storage-engine=INNODB
basedir=D:\\Program\\mysql-8.0.11-winx64\\
datadir=D:\\Program\\mysql-8.0.11-winx64\\data
#log-error=D:\\Program\\mysql-8.0.11-winx64\\log\\mysql-error.log  #windows下没有输出控制台
root密码默认在datadir目录下<hostname>.err文件中

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
mysqld  --defaults-file=E:/Program/mysql-5.7.18-winx64/my.ini --initialize   ##这是5.7版本新的要做的,root 密码在.err日志里 ,--initialize 要放在--defaults-file之后(最后)
mysqld  --defaults-file=E:/Program/mysql-5.7.18-winx64/my.ini   --console  
 如果不指定 --console   输出到data_dir下 或者用 --log-error 

不建议生产环境用--initialize-insecure 因root密码为空

##OK    ,type= own  ,要在=后有一个空格,建立删除如不行,不要在注册表中选中,关闭services.msc
sc create MySQL57 binpath= "\"E:/Program/mysql-5.7.18-winx64/mysql-5.7.18-winx64/bin/mysqld\" --defaults-file=\"E:/Program/mysql-5.7.18-winx64/my.ini\" MySQL57" type= own start= auto displayname= "MySQL57"
sc delete MySQL57 删服务


---my.ini
[mysql]
#default-character-set=utf8
[mysqld]
character_set_server=utf8   #新版本是 UTF8MB4
default-storage-engine=INNODB
basedir=E:\\Program\\mysql-5.7.18-winx64\\mysql-5.7.18-winx64
datadir=E:\\Program\\mysql-5.7.18-winx64\\data
---
sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
max_connections=200			###  最大连接数  默认值 151

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
	#update mysql.user set password=password('zh') where user='zh';
	ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; 
 
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
/usr/local/mysql/bin/mysql_config --libs --cflags  C开发

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
#datadir=/usr/local/mysql/data

default-storage-engine=INNODB
default-character-set=utf8

有字符集UTF8的
建数据库默认也是UTF8的
SHOW CREATE DATABASE bugs

SHOW [STORAGE] ENGINES //如没有innodb  编译时加--with-plugins=innobase
mysqladmin variables -uroot -proot >variables.txt //如没有innodb ,
show variables like 'storage_engine'  
  
日志：vi /usr/local/mysql/var/[hostname].err


------------MariaDB linux 版安装

mariadb-10.5.8-linux-systemd-x86_64
bin目录很多mysql开头的命令都是链接到mariadb开头命令上

mariadb 10.4版本可以兼容MySQL-5.7
mariadb 10.5版本 和 MySQL-8 不兼容的地方如下
https://mariadb.com/kb/en/incompatibilities-and-feature-differences-between-mariadb-105-and-mysql-80/
说 not support the --initialize option. Use mysql_install_db

./scripts/mysql_install_db  --user=mysql --datadir=/opt/mariadb-10.5.8-linux-systemd-x86_64/data  报 libaio.so.1 找不到 yum install libaio
	不加datadir参数默认目录为/var/lib/mysql (可以看shell源码，或者echo 式debug)
 
提示  root@localhost 没有密码
还提示 如开机启动复制 ./support-files/mysql.server 文件到指定位置(也可不用它) 
	看./support-files/mysql.server文件
	提示 /etc/my.cnf, ~/.my.cnf  , 可以修改 basedir,datadir 
	如不设置 basedir默认为/usr/local/mysql , datadir默认为/usr/local/mysql/data
	提示复制 my_print_defaults 到 /usr/bin 才可./support-files/mysql.server start
--my.cnf
[mysql]
default-character-set=utf8 
[mysqld]
default-storage-engine=INNODB
character_set_server=UTF8MB4
basedir =/opt/mariadb-10.5.8-linux-systemd-x86_64
#目录事先存在
datadir =/opt/mariadb-10.5.8-linux-systemd-x86_64/data
 
mysql用户下启动(不能以root用户运行)
bin/mysqld    --defaults-file=./my.cnf  
  
-------------linux 二进制安装 mysql-5.7 或 8
mysql-8.0.18 二进制解压为tar 大小变2.6G


看doc
shell> groupadd mysql
-- shell> useradd -r -g mysql  mysql   #-r表示系统帐户没有/etc/shadow记录 -s /bin/false
shell> tar zxvf /path/to/mysql-VERSION-OS.tar.gz
shell> ln -s full-path-to-mysql-VERSION-OS /usr/local/mysql
shell> cd /usr/local/mysql     #mysql8 版本可以安装在/opt目录下
shell> mkdir mysql-files
shell> chmod 750 mysql-files
shell> chown -R mysql .
shell> chgrp -R mysql . 
shell> bin/mysqld --initialize --user=mysql #不指定配置默认在data目录 
--defaults-file=/zh/mysql-files/my.cnf   注意 --initialize 要放在--defaults-file之后
--explicit_defaults_for_timestamp --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data --log-error=/usr/local/mysql/mysql-files/mysql-error.log 
提示生成了临时的 root@localhost的密码

shell> bin/mysql_ssl_rsa_setup  #不指定--datadir默认在/usr/local/mysql/data 目录 
--datadir=/usr/local/mysql/data 
shell> chown -R root .
shell> chown -R mysql data mysql-files 
 
bin/mysqld --verbose --help

可以mysqld启动不指定my.cnf

---my.cnf
[mysql]
default-character-set=utf8 
[mysqld]
default-storage-engine=INNODB
character_set_server=UTF8MB4
basedir=/opt/mysql-8.0.25-linux-glibc2.17-x86_64-minimal --默认值是/usr/local/mysql/
datadir=/opt/mysql-8.0.25-linux-glibc2.17-x86_64-minimal/data

port =3306
socket =/zh/mysql-files/mysql.sock  #默认/tmp/mysql.sock



#log_bin=ON
#server-id =1  			#变量是server_id(show variables like 'server_id'),命令行是 --server-id
#lc-messages-dir=/zh/mysql-files/share    
	-- 不加正常默认值为<basedir>/share/(但目录中有很多文件,但没有errmsg.sys)
	-- 加了报ERROR没有/zh/mysql-files/share/errmsg.sys文件,但能使用
log-error=/zh/mysql-files/mysql-error.log  #windows下没有输出控制台，加这个才知道root密码


#skip-grant-tables=ON
#explicit_defaults_for_timestamp=ON


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

-------

bin/mysqld  --defaults-file=/zh/mysql-files/my.cnf   --initialize --user=mysql   #日志中会提示有root临时密码
bin/mysql_ssl_rsa_setup  --datadir=/zh/mysql-files/data
 --defaults-file=/zh/mysql-files/my.cnf 
 
启动 mysql
su - mysql
bin/mysqld  --defaults-file=/zh/mysql-files/my.cnf 


bin/mysql -u root   -P 3306  -h localhost -S /zh/mysql-files/mysql.sock  #临时密码,对不知道root密码不能登录
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
bin/mysqladmin  -u root -p -P 3306 -S /zh/mysql-files/mysql.sock shutdown     提示密码过期,要修改,-h localhost 也要加-S ,除非-h 127.0.0.1(可能要能远程登录)
	  ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';  -- password expire never
	  SET PASSWORD FOR 'root'@'localhost' = PASSWORD('new_root');
	
 
---修改root密码
mysqladmin -uroot -p  password root   -S /zh/mysql-files/mysql.sock
mysqladmin -u用户名 -p旧密码 password 新密码  -h 主机 -S socket文件路径
mysqladmin -uroot  -p password root   -S /zh/mysql-files/mysql.sock

---- openSUSE 15 使用 mysql 客户端 要libtinfo.so.5 而实际上有libncurses6-6.1 ,zypper install libncurses5
#二进制解压 ldd bin/mysql openSUSE-leap-15.1报找不到  libtinfo.so.5 安装zypper install libncurses5 即可

--- fedora34 默认安装了　ncurses-libs-6.2-4.20200222.fc34.x86_64,　要　libncurses.so.5
 sudo dnf install ncurses-compat-libs

---innodb参数
innodb_fast_shutdown   0表示关闭，会缓冲区中数据写入硬盘，下次启动再读入内存，1(默认)打开
innodb_buffer_pool_size  134217728 即128M重要

innodb_stats_persistent		ON
innodb_stats_on_metadata	OFF

innodb_open_files
open_files_limit   --  cat /proc/sys/fs/file-max, /etc/security/limits.conf中nofile 

innodb_log_file_size	50331648	即48M 重要 
innodb_log_files_in_group	2   -- 文件个数
innodb_log_buffer_size

mysqladmin extended-status -r -i 10 | grep Innodb_os_log_written
mysqladmin -uroot -proot extended-status -r -i 10 | findstr Innodb_os_log_written
	 -r, --relative 
	 -i, --sleep=# 
表示日志写了多少，统计10/20秒内高峰每秒产生多少日志，
	再计算一小时产生日志大小，设置为 innodb_log_file_size 的值 
	也可设置 innodb_log_buffer_size 支持几秒

innodb_flush_log_at_trx_commit  不需要变
	如为0 大约每秒写一次日志缓存到日志文件中，但提交事务时不写缓存(可能丢失commit)
	如为1 默认值 ，每次提交事务，都写缓存到日志，再日志到文件
	如为2 每次提交事务，都写缓存到日志，每秒日志到文件
innodb_data_file_path		ibdata1:12M:autoextend 存的是 system表空间数据文件,格式为 file_name:file_size[:autoextend[:max:max_file_size]]
						autoextend 选项,当数据文件没有空间时默认自动扩大64MB,可用 innodb_autoextend_increment 参数修改
						可用;号分多个，如没有指定目录，默认在数据目录(datadir配置,也可使用innodb_data_home_dir配置覆盖)
						也可指定绝对目录即/开头，并且innodb_data_home_dir为空，可在不同硬盘上,但不是冗余备份
innodb_data_home_dir 	默认为空，表示为./即datadir配置
						
innodb_file_per_table	ON

innodb_purge_threads	4
innodb_purge_batch_size	300
innodb_doublewrite	ON  
	同个session同时写同一行，不是加锁，而是允许为这一行写多个版本(每个字段都有两个隐藏的值，一个是版本，一个是修改时间)
		mysql来决定最终使用哪个
sync_binlog	1  表示事务在提交之前 内存也写入binlog文件中 ，当为0时，mysql不会写binlog，而是依赖操作系统来写
	日志和数据可不放一个硬盘上
binlog_expire_logs_seconds	2592000 是30天 代替 expire_logs_days 过时参数 
	(当打开了binlog)如手工删除了日志，mysql在30天后删除不存在的日志就会报错,影响使用吗？？
max_allowed_packet   67108864   即64M

按连接来计算的参数要小心



==========MySQL NDB cluster 8.0.21  centos 7 ----OK   
MySQL 8.0.19 版本开始 可以使用NDB Cluster 8.0,
MySQL cluster 8.0.21版本linux版解压tar后大小变3.8G

无共享存储设备 （Share Nothing）
要将所有索引装载在内存中

MySQL8的二进制包和cluster不能一起使用，两版本号现是相同做配对使用
NDB只支持Read Commit的隔离级别，而InnoDB 支持所有,不支持savePoint
  
NDB(Network DataBase)
节点(node)在这里含义是进程(process),一台机器可以有多个节点,节点所在的机器叫(cluster host)

一台(多台) 管理(MGM)节点:	启动其他节点之前首先启动这类节点,ndb_mgmd启动的 , 客户端ndb_mgm, 只要这两命令即可  
多台 数据节点:	用命令ndbd启动的 , ndbmtd (multi-threaded) ,只要这两个命令即可  
多台 SQL节点:	mysqld --ndbcluster --ndb-connectstring 启动的,或将ndbcluster添加到my.cnf后使用mysqld启动   ,NDBCLUSTER 存储引擎

ndb_restore是用来恢复备份

所有主机上的文件系统是等同的


自己的子网内运行MySQL簇，不与非簇机器共享该子网,点之间的通信未采用任何特殊加密或防护

管理节点，不需要安装mysqld可执行文件

数据存储均是在内存中进行,可以基于 磁盘的存储

源码编译 configure时，务必使用“--with-ndbcluster”选项 
也可以使用BUILD/compile-pentium-max创建脚本
 
-------- 
管理节点(MGM)	 mgmtHost	有ndb_mgmd 和 客户端ndb_mgm
SQL节点1(SQL1)	 sqlHost	安装MySQL
SQL节点2(SQL2)	 sqlHost2
数据节点1(NDBD1) dataHost1	有ndbd
数据节点2(NDBD2) dataHost2

https://dev.mysql.com/doc/refman/8.0/en/mysql-cluster-config-example.html
配置SQL节点  # vi /usr/local/mysql/my.cnf 
	[mysqld]
	basedir         = /usr/local/mysql/		#可其它目录
	datadir         = /usr/local/mysql/data  #这个配置项是就算不配置也有默认值，因此也要 mysqld --initialize 生成root密码
	user            = mysql
	port            = 3306
	socket          = /tmp/mysql.sock
	ndbcluster
	ndb-connectstring=mgmtHost
	[MYSQL_CLUSTER]
	ndb-connectstring=mgmtHost


配置存储节点(NDB节点) # vi /usr/local/mysql/my.cnf  	#可以不用
	[mysqld]
	ndbcluster
	ndb-connectstring=mgmtHost
	[MYSQL_CLUSTER]
	ndb-connectstring=mgmtHost

https://dev.mysql.com/doc/refman/8.0/en/mysql-cluster-install-configuration.html
配置管理节点	# vi /usr/local/mysql/config.ini  #不区分大小写
	[NDBD DEFAULT]
	NoOfReplicas=1	
	#DataMemory=98M #可设置存数据的内存大小
	#每一份数据被冗余存储在不同节点上面的份数，一般为2
	[TCP DEFAULT]
	#portnumber=3306 #5.7版本为ServerPort，到8.0版本就没了
	
	#设置管理节点服务器
	[NDB_MGMD]
	#NodeId=1 #可以没有
	hostname=mgmtHost
	#MGM上保存日志的目录
	datadir=/usr/local/mysql/data/
	
	#设置SQL节点服务器
	[MYSQLD]
	NodeId=2 #如只有一个SQL节点也可以没有
	hostname=sqlHost
	
	[MYSQLD]
	NodeId=3
	hostname=sqlHost2
	
	#设置存储节点服务器(NDB节点)
	[NDBD] 
	NodeId=4
	hostname=dataHost1
	datadir=/usr/local/mysql/data/  #目录要事先存
	
	[NDBD] 
	NodeId=5
	hostname=dataHost2
	datadir=/usr/local/mysql/data/


可以有多个　[NDBD]和[MYSQLD],　[NDB_MGMD]和[NDBD]和[MYSQLD]中也可以有 


合理的启动顺序是
1.启动管理节点	# /usr/local/mysql/bin/ndb_mgmd -f /usr/local/mysql/config.ini  
	-f 或--config-file
	cluster-8.21版本默认使用 /usr/local/mysql/mysql-cluster 目录,要事先存在,即--configdir 或 --config-dir是二进制配置
	
2.启动存储节点，如第一次启动ndbd进程的话 # /usr/local/mysql/bin/ndbd --initial 
		#--ndb-connectstring=mgmtHost (或 -c mgmtHost:1186 ,-c --ndb-connectstring  )可不用配置文件，也可加--defaults-file=my.cnf
		或在备份/恢复数据或配置文件发生变化后重启ndbd时使用--initial参数	
		不是第一次# /usr/local/mysql/bin/ndbd
3.启动SQL节点	# /usr/local/mysql/bin/mysqld  --defaults-file=/etc/my.cnf  如第一次也要加 --initialize 

如要加SQL节点，管理节点修改ini配置文件后还要重建 --configdir 指向的目录
管理节点服务器上 客户端 #/usr/local/mysql/ndb_mgm --ndb-connectstring=mgmtHost
			ndb_mgm> SHOW 显示所有连接上来的IP，数据节点一个有显示 * 号 ，一直在交互式，如有节点上或下会有日志

---firewalld
管理节点要有1186端口监听	ss  -an | grep 1186 可能要 systemctl firewalld stop 
	#firewall-cmd --zone=public --add-port=1186/tcp --permanent
	 允许 (网段有/24,单机没有/24) 的IP 仿问本机的1186端口
	 firewall-cmd --zone=public --add-rich-rule 'rule family="ipv4" source address="192.168.60.1/24" port port="1186" protocol="tcp" accept'
	 firewall-cmd --reload

SQL节点 3306 端口 开防火墙  
	firewall-cmd --zone=public --add-port=3306/tcp --permanent

存储节点间要可相互通讯,信任网段内机器的所有端口通讯 ,不用加destination(允许发出)默认是可以的
	firewall-cmd --zone=public --add-rich-rule 'rule family="ipv4" source address="192.168.60.1/24" accept' #protocol="tcp"
	
---
SQL节点执行 show status like '%ndb%' 有显示
	Ndb_cluster_node_id		配置的
	Ndb_config_from_host	为 mgmtHost
	Ndb_config_from_port	为 1186
 
	
create table 必须用 ENGINE=NDB 或 ENGINE=NDBCLUSTER 选项创建,或用ALTER TABLE选项更改
如mysqldump导入表  添加ENGINE选项

每个NDB表必须有一个主键
 
安全停止
1.停止MGM节点	 # /usr/local/mysql/bin/ndb_mgm -e shutdown  也会把数据节点停,也可进入交互式执行
2.停止SQL节点的mysqld服务# /usr/local/mysql/bin/mysqladmin -uroot shutdown


每个SQL节点 (如第一次也要加 --initialize) 都要单独建立用户，单独grant database 权限(用root@localhost可以看到全部数据库)
测试 在一个 SQL 节点上，建表 ENGINE=NDB 插数据,另一个SQL节点也可以查到
jdbc:mysql:loadbalance://sqlHost1:3306,sqlHost2:3306/mydb?serverTimezone=UTC  

--排错
show engines 显示 ndbcluster  ，如失败也有显示
ndb_mgm 的show没有显示所有的IP
create table x(id int) ENGINE=NDB;错误后 show warnings;也有error信息
关闭所有防火墙试
--- NDB 建立 表空间
https://dev.mysql.com/doc/refman/8.0/en/create-tablespace.html#create-tablespace-ndb-examples

 CREATE LOGFILE GROUP myg1        ADD UNDOFILE 'myundo-1.dat'   ENGINE=NDB;
 #如失败show warnings 显示 只支持一个LOGFILE GROUP
 #drop  LOGFILE GROUP myg1    ENGINE=NDB;
 #查 SELECT *  FROM INFORMATION_SCHEMA.FILES WHERE LOGFILE_GROUP_NAME IS NOT NULL;
 
 CREATE TABLESPACE myts   ADD DATAFILE 'mydata-1.dat'  USE LOGFILE GROUP mylg   ENGINE=NDB; 
 #为何不成功？？有时提示文件名要以.ibd结尾
 
 
 CREATE TABLE mytable (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
      lname VARCHAR(50) NOT NULL,
      fname VARCHAR(50) NOT NULL,
      dob DATE NOT NULL,
      joined DATE NOT NULL,
      INDEX(last_name, first_name)
  ) TABLESPACE myts STORAGE DISK  ENGINE=NDB;

查
SELECT FILE_NAME, FILE_TYPE, LOGFILE_GROUP_NAME, STATUS, EXTRA
       FROM INFORMATION_SCHEMA.FILES
       WHERE TABLESPACE_NAME = 'myts';

--- INNODB  建立 表空间
CREATE TABLESPACE `ts1` ADD DATAFILE 'ts1.ibd' ENGINE=INNODB;

CREATE TABLE t1 (c1 INT PRIMARY KEY) TABLESPACE ts1 ROW_FORMAT=REDUNDANT;

CREATE TABLE t2 (c1 INT PRIMARY KEY) TABLESPACE ts1 ROW_FORMAT=COMPACT;

CREATE TABLE t3 (c1 INT PRIMARY KEY) TABLESPACE ts1 ROW_FORMAT=DYNAMIC;

CREATE TABLESPACE `ts2` ADD DATAFILE 'ts2.ibd' FILE_BLOCK_SIZE = 8192 Engine=InnoDB; 
CREATE TABLE t4 (c1 INT PRIMARY KEY) TABLESPACE ts2 ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=8;




=========Replication 
一个主(如主down了就不行了,就不能写了)，多个从（用来读）
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
#log_bin=mysql-log-bin #正常不用打开,也可从加 mysqld --gtid-mode=ON
server-id=2
relay_log=<host_name>-relay-bin --默认值空
relay_log_index=<host_name>-relay-bin.index  --默认值在data目录下



mysql> change master to 
->master_host='10.1.5.226',
->master_port=3308,
->master_user='repl',
->master_password='slavepass';
也可加下面的，不区分大小写
MASTER_LOG_FILE='recorded_log_file_name'
MASTER_LOG_POS=recorded_log_position

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

半同步  rpl_semi_sync_master_wait_point 取值 AFTER_SYNC(默认值) , AFTER_COMMIT




==========MySQL InnoDB cluster mysql-8.0.21 版本测试OK
mysql-8.0.21-linux-glibc2.12-x86_64.tar.xz  自带 mysqlrouer命令

表必须是innodb 引擎 

新的是客户端连接到MySQL Router 再连接到(可用 X Protocal ) InnoDB Cluster
MySQL Shell (使用JavaScript 或者 Python)可以交互(控制)MySQL Router
新的插件 DocumentStore/NoSQL , X DevAPI, X Protocal 


至少3个MySQL服务实例，每个实例运行 Group Replication 
客户端 -> MySQL Router -> Primary节点 -> 两个Secondary节点 
MySqlShell 管理 Primary节点(也可连接MySQL Router)

AdminAPI 
Time for Node Failure Recovery 要 30 seconds or longer 
支持 MVCC，Transactions 支持所有的,而NDB只支持 READ COMMITTED

打开防火墙端口，所有主机以主机名解析互通
先安装mysqld服务，x-devapi使用33060端口，只管理节点需要安装mysql-router

mysqlsh --log-level=DEBUG
参数格式 mysqlsh --uri root:root@mycentos1:3306/mydb
如已用 --uri登录 就可dba.checkInstanceConfiguration()
dba.checkInstanceConfiguration('root@mycentos1:3306')

红色ERROR 提示要可远程登录
create user root@'%' identified by 'root';

经色ERROR 提示要很多权限，也可简单用 grant all on *.* to root@'%' 即所有权限,select * from mysql.user where user='root' \G 看还变Y
	如使用提示的命令在建立好cluster环境下，在主个执行也会同步到从

绿色NOTE 提示要修改配置即my.cnf,也可用dba.configureInstanc()命令
enforce_gtid_consistency 	要为 ON
gtid_mode 					要为 ON
server_id 					要为 唯一
直到提示OK (可能要重新mysqld)

默认有的配置 (文档的Replication Framework区)
log_bin=binlog
log_slave_updates=ON
binlog_format=ROW
master_info_repository=TABLE
relay_log_info_repository=TABLE
transaction_write_set_extraction=XXHASH64
binlog_checksum=CRC32



dba.configureLocalInstance('root@mycentos1:3306') 提示 report_host变量
dba.checkInstanceConfiguration()
以上操作在多个节点（至少3个）操作成功后，在任一个节点执行下面 (如连接mycentos1,\connect root@mycentos1:33060)
var cluster=dba.createCluster('dev')提示group replication使用33061端口，提示使用Cluster.addInstance(),至少3个
cluster.addInstance('root@mycentos2:3306'); 交互提示 please select a recovery method [C]lone/[A]bort:输C,等一会 (errant 不定的)
	提示重启超时，可配置shell.options["dba.restartWaitTimeout"],重启后使用<cluster>.rescan()
	cluster.rescan(),如在打日志时间内手工重启就不用这步
cluster.addInstance('root@mycentos3:3306'); 	

var cluster = dba.getCluster()
cluster.status()显示
	primary: "mycentos1:3306"
	status:ok
	statusText:Cluster is ONLINE,and can tolerance up to ONE failure
	主节点为mode:R/W
	从节点为mode:R/O

关闭主节点服务，看主节点切，换测试OK 

mysqlrouter --bootstrap root@mycentos2 要为主节点， -B或  --bootstrap
	如连接到从节点，提示参数 super_read_only 要为 ON
	提示生成配置文件mysqlrouter.conf，启动使用
mysqlrouter -c  <mysql-router-home>/mysqlrouter.conf
	提示 MySQL 或 MySQL X 的读和写 使用的端口，共4个以 644xx 开头

innodb cluster(group replication) 要求每个表必须要有主键,如没有在insert时报错,#comply 遵守，顺从

JDBC测试加 useSSL=false 
select group_replication_switch_to_multi_primary_mode() 热机情况下切换到多主模式，mysqlsh要重新rescan，即 dba.getCluster().rescan() 提示输入y
select group_replication_switch_to_single_primary_mode()

mysql-router 测试成功,停主节点，自动切换， JDBC使用相同地址继续写
 
日志显示有 CHAGE MASTER TO FOR CHANNEL 'group_replication_recover' executed
		   Setting super_read_only=OFF

Group Replication笔记中
    auto_increment_increment=1   
	auto_increment_offset=2
	
	group_replication_single_primary_mode=off 表示多主节点 
	group_replication_bootstrap_group=OFF;  表示启动完成
	
	select @@read_only
	SELECT * FROM performance_schema.replication_group_members;  可以看哪个是主  是否在线
	
	Show plugins; 显示有group-replication
	SHOW BINLOG EVENTS;
	show master status 显示binlog的文件名和位置

	
========= MySQL Shell 8
XDevApi见MySQL_Dev

bin/mysqlsh
MySQL  JS > \connect user1@127.0.0.1?connect-timeout=2000
bin/mysqlsh  user1@127.0.0.1:33060/mydb


\quit
\status
\history
\use mydb
\js  切换到js
\sql 切换到sql
\source xx.js
\warnings
\nowarnings

sql>warnings  每次执行都显示信息，而不用show warnings;

bin/mysqlsh  user1@127.0.0.1:33060/mydb   --import file collection       
                                          --import file table [column] 
        如file为-表示从标准输入
  两个都是指定数据库
  -D, --schema=name     
  --database=name    


mysqlsh js > dba.help('getCluster')

也是BSON

全局变量 session,db,dba (InnoDB Cluster),shell,util
 
 
========= MySQL Router   8
是 InnoDB cluster 的一部分 ，不支持 NDB Cluster
MySQLRouter安装在和应用相同的机器，再连接InnoDB Cluster
 做failover
 
========= Group Replication 未测试
---双主，一个主收到数据除了 传给slave数据外，还要写到另一个主上
两相主加log_bin配置，每个都要有server-id

auto_increment_increment=2  主键自增的步长 对双主的情况
auto_increment_offset=1 	主键自增初始值
当一个主节点的表id生成了1，3，5后，另一个主节点的id直接生成6的数据,不是2,性能高，不用向后移动索引位置

log-slave-updates （是双主节点）表示从另一节点(另一个主)传来的数据要不要生成bin-log再传给子,(如传给原来的主节点，会根据server-id知道是自己的数据做忽略）
sync_binlog=1 事务提交几次，同步bin-log
	
初始启动两个主时，show master status 可能两个使用的bin-log文件和位置不一致，用reset master（不是必须的）

stop slave 
reset slave


要求
  表是Innodb引擎
  表要有主键
  机器内网 从8.0.14版本后支持IPv6
配置要求
 --log-bin[=log_file_name] 默认启用
 --log-slave-updates  双主节点都设置这个，这样一主接受到另一主的数据才能给自己的从节点
 --binlog-format=row
 --gtid-mode=ON
 --master-info-repository=TABLE 
 --relay-log-info-repository=TABLE
 --transaction-write-set-extraction=XXHASH64
  
   slave_parallel_workers=4  线程数 0表示禁用
   slave_preserve_commit_order=1 
   slave_parallel_type=LOGICAL_CLOCK （ 要求 slave_preserve_commit_order=1 ）
    
限制
 多主节点不支持SERIALIZABLE的事务隔离级别
 多主节点如有select ...for update 可死锁
 不可大事务 group_replication_transaction_size_limit 默认约143 MB
 多主节点不支持cascade外键
  
 。。。。
 

2n + 1个节点，那么允许n个节点失效，这个GR仍然能够对外提供服务

single-primary模式下，组内只有一个节点负责写入，读可以从任意一个节点读取  ，当primary节点意外宕机或者下线，在满足大多数节点存活的情况下，group内部发起选举，选出下一个可用的读节点，提升为primary节点。
  在切换primary期间，mysql group不会处理应用重连接到新的主，这需要应用层自己或者由另外的中间件层（proxy or router）去保证。
 
multi-primary模式即为多写方案，即写操作会下发到组内所有节点，组内所有节点同时可读可写
   当不同实例并发对同一行发起修改，在同个组内广播认可时，会出现并发冲突，那么会按照先执行的提交，后执行的回滚
   
一组中只可用一种模式
一组中最多9个成员

 
 
group_replication_single_primary_mode=off 表示多主节点

select @@read_only;


可以多个Master
SELECT MEMBER_HOST, MEMBER_ROLE FROM performance_schema.replication_group_members;

cd /opt/mysql-8.0.15-linux-glibc2.12-x86_64
mkdir group-repl-data
bin/mysqld --initialize-insecure --basedir=$PWD  --datadir=$PWD/group-repl-data/s1   
bin/mysqld --initialize-insecure --basedir=$PWD  --datadir=$PWD/group-repl-data/s2
bin/mysqld --initialize-insecure --basedir=$PWD --datadir=$PWD/group-repl-data/s3

不建议生产环境用--initialize-insecure 因root密码为空  --initialize --user=mysql   #日志中会提示有root临时密码

vi $PWD/group-repl-data/s1/s1.cnf
 [mysqld]
 #mysql config
 datadir=/opt/mysql-8.0.15-linux-glibc2.12-x86_64/group-repl-data/s1
 basedir=/opt/mysql-8.0.15-linux-glibc2.12-x86_64
 port=24801
 socket=/opt/mysql-8.0.15-linux-glibc2.12-x86_64/group-repl-data/s1/s1.sock
 
 #replication config
 report_host=127.0.0.1
 server_id=1
 gtid_mode=ON
 enforce_gtid_consistency=ON
 binlog_checksum=NONE
 
 #group replication config
 transaction_write_set_extraction=XXHASH64   这个是默认值
 #开始不认这个选项前加loose-前缀可启动，后面安装插件后就可去除loose-前缀
 plugin_load_add='group_replication.so'
 group_replication_group_name="8cb03f62-5ad5-11e9-9a7d-588a5a3bf786"   使用SELECT UUID()生成值   
 group_replication_start_on_boot=off
 group_replication_local_address= "127.0.0.1:24901"   这个端口每个文件不一样是seeds中的值
 group_replication_group_seeds= "127.0.0.1:24901,127.0.0.1:24902,127.0.0.1:24903"
 group_replication_bootstrap_group=off

#附加配置
mysqlx_port=33061
mysqlx_socket=/opt/mysql-8.0.15-linux-glibc2.12-x86_64/group-repl-data/s1/mysqlx.sock

#log-bin=mylog-gin
#log-slave-updates
#binlog-format=row
#master-info-repository=TABLE
#relay-log-info-repository=TABLE
#slave_parallel_workers=4
#slave_preserve_commit_order=1
#slave_parallel_type=LOGICAL_CLOCK

启动 
bin/mysqld --defaults-file=group-repl-data/s1/s1.cnf 

登录mysql
bin/mysql -uroot -P24081 -S /opt/mysql-8.0.15-linux-glibc2.12-x86_64/group-repl-data/s1/s1.sock

 SET SQL_LOG_BIN=0;
 CREATE USER rpl_user@'%' IDENTIFIED BY 'password';
 GRANT REPLICATION SLAVE ON *.* TO rpl_user@'%';
 FLUSH PRIVILEGES;
 SET SQL_LOG_BIN=1;

CHANGE MASTER TO MASTER_USER='rpl_user', MASTER_PASSWORD='password' FOR CHANNEL 'group_replication_recovery';

安装 group-replication 插件，后才可识别group_replication_group_name配置
INSTALL PLUGIN group_replication SONAME 'group_replication.so';
SHOW PLUGINS;

SET GLOBAL group_replication_bootstrap_group=ON;  --只加第一个实例用
START GROUP_REPLICATION;
SET GLOBAL group_replication_bootstrap_group=OFF;  --只加第一个实例用


SELECT * FROM performance_schema.replication_group_members;  可以看哪个是主  是否在线
//建库，建表，加数据  --只加第一个实例用

SHOW BINLOG EVENTS;

--再重复上面步骤增加第二个实例，配置修改两个端口和目录，server_id修改,group_name不变 ， 看不能查到前面建的表

 
当在 START GROUP_REPLICATION; 时失败？？？原因是24901,34903没有监听？？？
systemctl stop firewalld

 

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

--hex-blob          (BINARY, VARBINARY, BLOB类型以十六进制字串导出) 数据格式为 0xFF00000000000000
可用HEX函数
	SELECT X'616263', HEX('abc'), UNHEX(HEX('abc'));
	SELECT HEX(255), CONV(HEX(255),16,10);

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
 --tables  空格加参数 会覆盖  --databases 
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


LOCK TABLES employee  read ,department write;
show open tables; -- In_user 为1是写锁
UNLOCK TABLES



myisam_356.sdi
isam_tbl.MYI 是索引文件
isam_tbl.MYD 是数据文件

聚簇索引,   物理顺序和逻辑顺序是一致的,一个表只可有一个，默认主键
非聚簇索引, 物理顺序和逻辑顺序没有必然联系

innodb引擎,数据和索引放在一起,ibd文件就是聚簇索引文件,
myisam引擎,数据和索引不放在一起,

唯一索引 值可以为null，主键索引不能为null,一个表只可一个
InnoDB是行级锁，
MyIsam是表级锁

SHOW ENGINES
B+Tree 数据只存放在叶子节点上，叉节点上的数据只用于查找分界用
所有叶节点具有相同的深度，等于树高
 
  
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
--------当前运行的所有事务
SELECT * FROM INFORMATION_SCHEMA.INNODB_TRX;
	tx_started 事务开始时间
	tx_query
	tx_isolation_level
	trx_mysql_thread_id 可以用做 kill <thread_id>
	
当前出现的锁,查不到？权限问题？
SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCKS;



======= SQL 执行历史日志,general_log=1(全局日志,记录所有SQL,无执行时间)和slow-query-log=1(慢查询) 开启,log-output如为FILE 是在data目录下
-- my.ini 配置(上层以-分隔,最下层以_分隔)  
log-output=NONE

general-log=0
general_log_file="mysql-gen.log"

slow-query-log=0
slow_query_log_file="mysql-query.log"
long_query_time=10 
-- set global long_query_time=5; 实时修改后重新登录后生效 ,测试可用select sleep(6)

-- 命令中都是以_分隔
show variables like '%general_log%'   
set global general_log=ON;

show variables like '%slow_query_log%' 
set global slow_query_log=ON; -- 可以随时打开
 
show variables like '%log_output%'    
set global log_output='TABLE';  -- root 执行
 
select * from mysql.slow_log   order by start_time desc
select * from mysql.general_log order by event_time desc   

truncate table mysql.general_log ;

======= 
也可使用 mysqldumpslow.pl 只是快速查找文件内容的一个工具
-s 按什么排序
	r 记录数
	c 仿问次数
	t 查询时间
-r 反序
-l 锁定时间
-g  正则表达式
-t 前几个

mysqldumpslow.pl  -s r -l -t 3  xxx.log 
mysqldumpslow.pl  -s c  -t 3 xxx.log 
mysqldumpslow.pl  -s t  -t 3 -g 'join' xxx.log 



-----profile  类似 general_log
show variables like '%profiling%'
SET profiling = 1;  --  或者ON
show profiles;   显示最近SQL 的过程 及总执行时间	有  SET SQL_SELECT_LIMIT=25000

show profile for query 54;  -- 显示这个 SQL 的各个阶段所有时间
show profile all for query 2; -- 显示更多的列
show profile cpu , block io for query 2; -- 显示这两组

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

查表外键约束
select
TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME
from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
where CONSTRAINT_SCHEMA ='mydb' AND
REFERENCED_TABLE_NAME = 'jpa_student';
 
 
 查所有索引  ,  不同的表, 可以有相同的索引名字

SELECT a.TABLE_SCHEMA,
a.TABLE_NAME,
a.index_name,
GROUP_CONCAT(column_name ORDER BY seq_in_index) AS `Columns`
FROM information_schema.statistics a
GROUP BY a.TABLE_SCHEMA,a.TABLE_NAME,a.index_name


select 
table_schema as '数据库',
table_name as '表名',
table_rows as '记录数',
truncate(data_length/1024/1024, 2) as '数据容量(MB)',
truncate(index_length/1024/1024, 2) as '索引容量(MB)'
from information_schema.tables
order by data_length desc, index_length desc; 



--------- sys schema
SELECT * FROM sys.version;

select * from sys.session;
select * from sys.processlist;
 select * from sys.innodb_lock_waits ;


======= 
SHOW STATUS LIKE 'com%'; //表示insert,update,select,delete语句执行次数， 默认是session的，可加 global
SHOW STATUS LIKE 'InnoDB_rows%'; 影响行数
  
linux 的mysql 的表名是区分大小写的, 则windows不区分,字段名都不区分
show variables like 'lower_case_table_names'
my.cnf 中增加 lower_case_table_names=0     --  其中0：区分大小写，1：不区分大小写  重启　　,测试下来新表名脚本为大写,建好了并不是小写



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


create database zabbix character set utf8 collate utf8_bin;

collate utf8_bin 和 collate utf8_general_ci (ci为case insensitive的缩写，即大小写不敏感) 过时了，建议用 utf8mb4;

utf8 一个字符占 3-byte , utf8mb4 一个字符占 4-byte(mb=max bytes),是mysql 8 的默认字符集



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
 
 解析sql过程(复合索引按这个顺序) from...join...on...where...group by...having...select distinct ... order by ...limit
 
	explain 会自动优化 小表放前面 再关联 大表
			表关联 where/on  小表字段=大表字段 (手工优化写法)

	一般是左连接，在左表加索引
	
	id列 相同按顺序执行，id大的优先查询，先执行嵌套子查询 
	select_type列 值有PRIMARY和SUBQUERY,Simple,Derived(用到了临时表), from子查询中如是多表连接,那么第一个表是Derived,第二个表是union
			还有一种union result 
	table 列如有<derived2> 其中2表示id为2的那个表,如有<union2,3>表表示id为2和3的表做union结果
	type列 system>const>eq_ref>ref>range>index>all  要有索引才行
		   system 表示只有一条数据的系统表 或 Derived表只有一条数据的主查询
		   const 只能查出一条数据，并且 条件是用于primary key或unique 索引 的列
		   eq_ref 作用于unique 索引列,并且和关联表的第行只有一条匹配数据,不能没有关联到,也不能多于一条,如一对一的外键
		   ref 非唯一的索引 (比较常用的)
		   range 如使用 between and  ,>,< ,in(有时可以,有时不行????用or代替,放在最后)
		   index 查询全部索引中的数据
		   all 查询全部表中的数据
	possibale_keys列
	keys列
	key_len 列 是索引所在字段char长度 如是utf8字符集要*3(gbk是*2) 如列是可为空+1 ,如varchar类型+2,int 为4个字节
		如为复合索引就是 多个字段 相加，用于做复合索引是否使用/失效了	   
	ref列 如为const表示常数，如key列为PRIMARY或索引名时 ,值为指向主键列的 数据库.表.字段 (外键要加索引,因要关联查询)
	rows列	
	Extra列 如为 using filesort 排序会比较慢(表示要单独再查询一次数据，基于这个数据再排序,如 where 字段1='' order by 字段2，解决方法使用复合索引 )
		create index inx_emp_3 on employee(username,password,age)
		explain select * from employee where username='lisi' order by password --  order by也按复合索引顺序用,没有using filesort 
		explain select * from employee where username='lisi' and age=2 order by password,age
		explain select * from employee where username='lisi' and password!='' order by age --   != 不会使用索引
		explain select * from employee where username='lisi' or age =22 -- 跳着不能使用复合索引 
		explain select * from employee where  password ='xx' and username='lisi' --写顺序没关系 会自动优化
		explain select * from employee where  username ='lisi' and age=23 -- Using index condition
		explain select * from employee where   password='123' and username ='lisi'  -- 顺序会自动优化
	  如为 using temporary 会比较慢，用到了临时表，一般出现在group by中 ,解决方法 分组的字段出现在where 中
		 create index inx_emp_deptid on employee(department_id)
		 drop  index inx_emp_deptid on employee;

		 explain select department_id ,max(age) from  employee where id in (101,102) group by department_id
	  还有using index ,using where ,impossiable where 如 where id=1 and id=2
		 using join buffer,mysql给做了优化
	
索引字段加函数或计算会失效,!= , not null,not in ,or组合字段有未索引字段和索引字段,那么索引字段失效
 有时(其实是优化级变低)in,>,between失效

like以通配符开头会索引失效
select username from employee where username like '%li%'; -- select中使用了 like字段会使用索引
select 不要使用*

唯一索引 比普通索引性能 高  show session status like 'Handler_read_next'; 唯一索引当找到一条数据就必须再找后面的数据了
如没有索引  show session status like 'Handler_read_rnd_next'; (rnd=read next datafile)的值会大量增加,表示读硬盘中的下一行
--没有找到规律的 >
alter table employee add index inx_emp_username  (username )
explain select * from employee where username='s' and department_id=232 --如where 后两个字段都加了独立索引，但只会使用一个

explain select * from employee where  username > 'lisi'  and password='s'  -- 如开始字段有> 看key_len为null,复合索引全失效
explain select * from employee where  username = 'lisi' and password > '123'  and age=22  -- 中间字段有>，复合索引后面字段失效
explain select * from employee where  username = 'lisi'  and password>'s' -- 复合索引全用了 
explain select * from employee where  username < 'lisi'  and password='s' -- 看key_len只用了复合索引一个字段

--
执行计划也分析字段值和建立索引对比的

如子查询数据量大使用 exists 代替 in
如主查询数据量大使用 in

外键 字段 和 order by 字段 最好加索引
max_length_for_sort_data 排序区的大小

innodb 引擎如update/delete 的 where 字段全没有使用索引会锁全表(太傻了，为何不使用主键/rowid呢)

如果update 的 where 字段 用到了主键索引，mysql会锁定主键索引
如果用到了非主键索引，msyql会先锁定非主键索引，再锁定主键索引,两步就容易造成死锁(太傻了)
如果用到了主键索引，会先在主键索引上加锁，然后在其他索引上加锁,两步就容易造成死锁(太傻了)


(第一个update where是非主键索引锁住后，在锁定主键索引前，已经被这条记录的第二个 update where非主键索引锁住，还没有开始 set， 刚好set字段是第一个update已经锁的非主键索引,set的字段也要求不能被锁 )

乐观锁 最好条件不是范围 ,会比较慢 ，容易被锁
可能和REPETABLE READ 
 

update employee set age=20 where username=11; 
-- username有索引，这种类型转换，会使用索引失效，变为表锁，如另一个会话其它行在事务中，这会阻塞，另一个结束这里报错，影响不大
update employee set age=20 where id>103 and id<200; 未提交事务,如中间id没有108，insert into id为108也是阻塞 (太傻了，为何不使用主键/rowid呢)

explain extended    <sql> 后,再使用      SHOW WARNINGS 可以给出优化建议的SQL


 show variables like '%optimizer_trace%';
 set optimizer_trace="enabled=on";
 explain select .....;


 
 select * from information_schema.OPTIMIZER_TRACE;
 




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
show variables like '%time_zone%' 
|  system_time_zone | CST  |
| time_zone   		  | SYSTEM |

修改时区
> set global time_zone = '+8:00'; ##修改mysql全局时区为北京时间，即我们所在的东8区
> set time_zone = '+8:00'; ##修改当前会话时区
> flush privileges; #立即生效

方法二：通过修改my.cnf配置文件来修改时区
vim /etc/my.cnf 
##在[mysqld]区域中加上
default-time_zone = '+8:00'


SELECT CONVERT_TZ('2013-07-22 18:41:37','+08:00','+00:00') as UTC;         


mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root -p  mysql  导入时区相关表 ,windows要下载timezone_2018d_posix_sql.zip
mysql -u root -p mysql < D:/tmp/timezone_2018d_posix_sql/timezone_posix.sql 
显示下面语句
TRUNCATE TABLE time_zone;
TRUNCATE TABLE time_zone_name;
TRUNCATE TABLE time_zone_transition;
TRUNCATE TABLE time_zone_transition_type;
START TRANSACTION;
COMMIT;


innodb_autoinc_lock_mode  MySQL 8之前默认值为1，MySQL8开始默认值为2
 0 (“traditional” lock mode) 	     所有的insert都是 表级锁
 1 (“consecutive” lock mode) 连继的, 批量插入(insert ..select,load data)时是表级锁，
 2 (“interleaved” lock mode) 交替的, 速度快，但当使用bin log 做replication 和replay 时不安全













