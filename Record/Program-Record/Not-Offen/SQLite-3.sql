工具有 DBeaver

事务  ACID (atomic 原子,consistent一致的,isolated隔离的,durable持久的,)

支持TB  GB的String,BLOB   (适合手机),单用户

手机数据库
不支持并发,支持事务
运行在其它进程里

一个数据文件跨平台,windows ,linux...,32bit ,64bit

替换fopen()函数


sqlite3 mydata.db  //没有会创建,有mydata.db会使用它,如不加文件名,就在内存里

sqlite_master 是系统表,只能查
sqlite_temp_master 是临时表,如排序

.help可以看见所有的
.exit 和 .quit一样


.mode html   //以HTML标签来显示
.mode column 
.mode csv   //以,来分隔
.mode insert//显示成insert语句
.mode line  //以字段名=value的形式
.mode list//和.mode csv
.mode tabs //以tab,和.mode column
.mode tcl //用"" 括住值

.output file  可以输出到文件，默认到屏幕 使用 stdout

.separator |   //每列默认用|

sqlite3 mydata.db "select * from mytable" | awk 'BEGIN {RS="|"}{print $1}'
//以|分隔的


.show   
.header on  可以显示字段名
.nullvalue NULL //显示null的字符

.read C:\\data.sql  //执行文件中SQL
.import FILE　TABLE //从文件把数据导入表中

.tables 显示所有的表  ，是一个复杂的
	select name from sqlite_master
	where type in('table','view') and name not like 'sqlite_%'
	union all
	select name   from sqlite_temp_master
	where type in('table','view')
	order by 1;
.indices TABLE_NAME 所有的  (索引)

.schema 显示表的create语句

.database   第一个数据库是 main,第二是temp

.dump 生成SQL语句


数据类弄不检查吗?int,varchar

echo ".dump" | sqlite3 mydb | gzip -c >mybak.gz
zcat mybak.gz | sqlite3 mydb2

.explain 执行计划
1) .explain 
2) explain select .....

oracle 中有一个explain

.timeout 不支持多用户并发,会锁定,超时时间 


sqlite3 mydb "select * from stu" 会把执行结果到stdout ,然后退出  ,
		可以用go ,或者用/     区分;


-----Perl 使用SQLit  
是否有SQLite DBI ->DBD(Database Driver)
Windows的Perl是OK的


#!/usr/bin/perl
use DBI 
@driver_names=DBI->available_driver();
print "drivers installed :\n";
foreache my $dn (@driver_names)
{
	print "-->$dn\n";
}
--------
#!/usr/bin/perl
use DBI 
use strict;  #//所有的变量要声明
my $dbargs={AutoCommint=>0,PrintError=>1};
my $dbh=DBI->connect("dbi:SQLite:dbname=demo.db","","",$dbargs);
$dbh->do("insert into t values(1,'lisi')");
if($dbh->err()){die "$DBI:errstr\n";}
dbh->commit();
dbh->disconnect();
##----perl select 
my $id ;
my $name;
my $sql=qq{select * from t}   ##qq  相当于""
my $sth=$dbh->prepare($sql);
$sth->execute();
$sth->bind_columns(undef,\$id,\$name);
while($sth->fetch())
{
	print "$id--$name\n";
}
$sth->finish();
$sth->disconnect();
exit;






C/C++ 使用SQLite

对象:
	sqlite3
	sqlite3_stmt

#include <sqlite3.h>
sqlite3 *db;
char * error=0;
if(sqlite3_open("mydb",&db)) sqlite3_errmsg(db);
sqlite3_exec(db,"select...",mycallback,0,&error);
sqlite3_close(db);

int mycallback( void *NotUsed,int argc,char **argv,char **colname)
{
	int i=0;
	for(i=0;i<argc;i++)
	{
		printf("%s--%s\n",colname[i],argv[i]?argv[i]:"NULL");
	}
}


sqlite3_prepare()
sqlite3_step()
sqlite3_column()
sqlite3_finalize()





不支持外键, alter table支持部分,不能 join,grant ,






















