

    jdbc:postgresql:database
    jdbc:postgresql://host/database
    jdbc:postgresql://host:port/database



org.postgresql.Driver
--------------------linux

./configure
gmake
su
gmake install
adduser postgres
mkdir /usr/local/pgsql/data
chown postgres /usr/local/pgsql/data
su - postgres
/usr/local/pgsql/bin/initdb -D /usr/local/pgsql/data
/usr/local/pgsql/bin/postgres -D /usr/local/pgsql/data >logfile 2>&1 &
/usr/local/pgsql/bin/createdb test
/usr/local/pgsql/bin/psql test (postgres数据库) 登录
test=# \dt  查所有的表
test=# \q 退出

psql postgres(数据库) postgres(用户)  
psql -U用户 -W -hlocalhost -p5432  -d 资料库名称 (-h,-p后可有,可无空格) 
=#提示符用\q退出


linux  上默认的端口是5432( /usr/local/pgsql/data/postgresql.conf可以修改)
以远程登录:
$POSTGRES/data/pg_hba.conf 中加一行才可以远程登录
host    all         all         192.168.1.25/24         trust(密码错误也可以登录,建用户用enypted ,则用md5)
启动 postgresql 的时候，加上“-i”这个参数(启用TCP/IP连接)
/usr/local/pgsql/bin/postgres -i -D /usr/local/pgsql/data > ~/logfile 1>&2 &

select * from user 显示当前用户;
/usr/local/pgsql/bin/createuser -W(--password)  -s (--superuser) zhaojin
-P --pwprompt



./bin/createuser -d(--create-db) -P(--pwprompt) -A (addRole) bugs
bugzilla 中要在data目录,刚建的,里有一个pg_hdb.conf文件
 host all bugs 127.0.0.1 255.255.255.255 md5

This means that for TCP/IP (host) connections, allow connections from '127.0.0.1' to 'all' databases on this server from the 'bugs' user, and use password authentication (md5) for that user.




------------------------------windows下的no-installer

②　设置环境变量
C:> set PGHOME=C:\postgreSQL
C:> set PGDATA=%PGHOME%\data
C:> set PGLIB=%PGHOME%\lib
C:> set PGHOST=localhost
C:> set PATH=%PGHOME%\bin;%PATH%

③　initdb 初始化 数据库
	initdb  -E UTF8 --locale=C   (data目录会自动创建)

④　pg_ctl start 启动数据库
⑤　pg_ctl stop 停止数据库

注册windows服务
pg_ctl register
pg_ctl register -D d:\pgsql\data -N pgsql    增加服务名为pgsql
pg_ctl register -N PostgreSQL -U postgres -P pass -D E:\pgsql\data


⑧　使用"createdb 库名" 新建数据库		 -E, --encoding=UTF8,工具生成语法并不真的编码
⑨　使用"createuser 用户名"创建数据库帐号	Shall the new role be a superuser?
createuser -s (--superuser) -E (encrypted) -P (--pwprompt)  user01
mydb=# create user myuser with login [superuser | createdb ] [ ENCRYPTED | UNENCRYPTED ]  password 'password' 

createdb -E UTF8 mydb
mydb=# create database mydb encoding 'UTF8'  owner myuser ;(默认UTF8,是initdb  -E UTF8时)
 \c mydb  ----create db

ALTER DATABASE mydb OWNER TO myuser;
ALTER TABLE stu OWNER TO myuser;
select * from pg_shadow



⑩　使用"psql -h localhost -d 库名- U 用户名"进入数据库


data 目录下的postgresql.conf文件中#port = 5432	



\g 来执行查询 (或者以;semicolon 结尾)　
和mysql 一样可以用 set names 'gbk' 来解决显示乱码问题,



create table t(id serial not null,username char(20) not null);  //serial 自动增长




和mysql差不多,windows非安装版本,任何用户都可不要密码登录..................

