windows安装版本自带pgAdmin4工具 ,会提示设置master的密码

界面工具 DbVisualizer,Navicat,dbeaver(eclipse),SQuirrel(Swing)
还有 phpPgAdmin(类似phpMyAdmin) 在openSUSE-leap-15.1的yast上有rpm包

pgAdmin4(官方linux版只有redhat版和源码,有docker版本) 在openSUSE-leap-15.1的yast上有rpm包,使用python3开发
	/etc/apache2/conf.d/pgadmin4.conf: Cannot load modules/mod_wsgi.so 

pgAdmin4 命令，是启动一个web服务器 ,自动打浏览器 http://127.0.0.1:xxxx/browser/ 随机端口 是一个web应用，有一个界面 pgAdmin4-> copy server URL
有实时监控的图


jdbc:postgresql:database
jdbc:postgresql://host/database
jdbc:postgresql://host:port/database?

org.postgresql.Driver
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.2.18</version>
</dependency>

sonarqube 和 kong 只能用 PostGreSQL

--------------------linux

./configure
gmake
su
gmake install

linux二进制版本也可安装到/opt目录下

adduser postgres
mkdir /usr/local/pgsql/data
chown postgres /usr/local/pgsql/data
su - postgres
bin/postgres --version
/usr/local/pgsql/bin/initdb -D /usr/local/pgsql/data   

linux二进制版本 提示使用 bin/pg_ctl -D /usr/local/pgsql/data/ -l logfile start (是后台运行 与下二选一)
bin/pg_ctl -D /usr/local/pgsql/data/ -l logfile restart
 
/usr/local/pgsql/bin/postgres -D /usr/local/pgsql/data >logfile 2>&1 &



------------------------------windows下的no-installer

设置环境变量
C:> set PGHOME=C:\postgreSQL
C:> set PGDATA=%PGHOME%\data
C:> set PGLIB=%PGHOME%\lib
C:> set PGHOST=localhost
C:> set PATH=%PGHOME%\bin;%PATH%

 initdb 初始化 数据库
	initdb  -E UTF8 --locale=C   (data目录会自动创建)

如不设置环境变量用,initdb -D C:\Application\postgresql-12.5.1-xx\pgsql\data  -E UTF8 --locale=zh
	-U --username 超级用户名
	-W --pwprompt 超级用户提示输入口令
提示使用命令  bin/pg_ctl -D ^"C^:^\Application^\postgresql^-12^.5^.1-xx^\pgsql^\data^" -l logfile  start
	#特殊符号前加^,总是自动停止,建立用 pg_ctl register

 pg_ctl start 启动数据库
 pg_ctl stop 停止数据库

注册windows服务
pg_ctl register
pg_ctl register -D d:\pgsql\data -N pgsql    在servcies.msc中增加服务名为pgsql
pg_ctl register -N PostgreSQL -U postgres -P pass -D E:\pgsql\data


----------------------


bin/createdb test 
bin/psql test (postgres数据库) 登录
test=# \dt  查所有的表
test=# \q 退出

bin/psql
CREATE USER kong;
ALTER USER kong WITH password 'kong';
CREATE DATABASE kong OWNER kong;
 alter database kong owner kong；
 

bin/createdb sonarqube
bin/psql sonarqube
	CREATE USER sonarqube;
	ALTER USER sonarqube WITH password 'sonarqube';
	-- CREATE DATABASE kong OWNER kong;
	alter database sonarqube owner sonarqube;
	
	如不使用默认的public schema,就新建
	create schema mySonarSchema; 
	grant usage on schema mySonarSchema to sonarqube;
	grant all on schema mySonarSchema to sonarqube;
	ALTER USER sonarqube  set search_path to sonarqube;
	
	jdbc:postgresql://localhost:5432/sonarqube?currentSchema=mySonarSchema
	
psql postgres(数据库) postgres(用户)  
psql -U用户 -W -hlocalhost -p5432  -d 资料库名称 (-h,-p后可有,可无空格) 
=#提示符用\q退出


默认监听的端口是5432(  data/postgresql.conf可以修改)
 
以远程登录:
data/pg_hba.conf 中加一行才可以远程登录
host    all         all         192.168.1.25/24         trust(密码错误也可以登录,建用户用enypted ,则用md5)
启动 postgresql 的时候，加上“-i”这个参数(启用TCP/IP连接)
bin/postgres -i -D /usr/local/pgsql/data > ~/logfile 1>&2 &

select * from user 显示当前用户;
bin/createuser -W(--password)  -s (--superuser) zh
-P --pwprompt



./bin/createuser -d(--create-db) -P(--pwprompt) -A (addRole) bugs
bugzilla 中要在data目录,刚建的,里有一个pg_hdb.conf文件
 host all bugs 127.0.0.1 255.255.255.255 md5

This means that for TCP/IP (host) connections, allow connections from '127.0.0.1' to 'all' databases on this server from the 'bugs' user, and use password authentication (md5) for that user.



使用"createdb 库名" 新建数据库		 -E, --encoding=UTF8,工具生成语法并不真的编码
使用"createuser 用户名"创建数据库帐号	Shall the new role be a superuser?
createuser -s (--superuser) -E (encrypted) -P (--pwprompt)  user01

bin/createuser --pwprompt zabbix
bin/dropuser zabbix
bin/createdb -O zabbix -E Unicode -T template0 zabbix
bin/dropdb zabbix

mydb=# create user myuser with login [superuser | createdb ] [ ENCRYPTED | UNENCRYPTED ]  password 'password' 

createdb -E UTF8 mydb
mydb=# create database mydb encoding 'UTF8'  owner myuser ;(默认UTF8,是initdb  -E UTF8时)
 \c mydb  ----create db

ALTER DATABASE mydb OWNER TO myuser;
ALTER TABLE stu OWNER TO myuser;
select * from pg_shadow



psql -h localhost -p 端口 -d 库名 -U 用户名   -W 密码







create table t(id serial not null,username char(20) not null);  //serial 自动增长




列出所有数据库
bin/psql -l

切换数据库 
\c dbname

列出所有表
zabbix=>\dt
SELECT tablename FROM pg_tables WHERE tablename NOT LIKE 'pg%' AND tablename NOT LIKE 'sql_%' ORDER BY tablename;

表结构
\d myTable

\du 列出所有用户

创建表时，如果没有指定schema,则表会自动被归属到一个叫做 public 中

schema 一个放多个表的地方

create schema demo_schema; 
CREATE TABLE demo_schema.mytable (  
id int  
);
 
 
DBNAME=# 提示符表示superuser
DBNAME=> 提示符表示一般用户 

Postgresql开启远程访问


zabbix=#show hba_file;  客户端授权配置文件

./data/pg_hba.conf
# TYPE  DATABASE        USER            ADDRESS                 METHOD
host		 all			 all 					127.0.0.1/32				trust  
#增加如下
#host		 all			 all 					192.168.1.8/24				md5
host		 all			 all 					0.0.0.0/0				md5


zabbix=# show config_file;
./data/postgresql.conf
#listen_addresses = 'localhost'    修改为
listen_addresses = '*' 

bin/pg_ctl -D ./data/ -l log/logfile  restart
多了  0.0.0.0:5432  的端口监听


登录用户建立的表  
SELECT * FROM pg_catalog.pg_tables  
tableowner 是操作系统用户 表示超级用户

alter table  users owner to zabbix;

grant usage on schema public to zabbix;
grant all on schema public to zabbix;
 
-- 同 mysql
SELECT * FROM information_schema.tables; 
\g 来执行查询 (或者以;semicolon 结尾)　
set names 'gbk' 来解决显示乱码问题,

windows非安装版本,任何用户都可不要密码登录..................




