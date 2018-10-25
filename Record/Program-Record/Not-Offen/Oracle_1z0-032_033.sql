===========================032
Oracle Internet Directory 使用 LDAP
show parameter mts; 配置共享服务器

Connection Manager 可以配置哪个IP 可以连接Oracle   默认是没有安装的?????????????????

Oracle 与SQL Server 的互联  使用透明网关 (heterogeneous 不同种类的)

PLSQL调用Java 要配置Listener  (ADDRESS = (PROTOCOL = IPC)(KEY = EXTPROC1521))
exteranl procedure call, listener.ora 文件中的EXPROC0 实现外部调用 ,如Java语言




Sqlplus ->(OCCI 接口) TTC->Oracle Net->OPS->Protocol 
Java Application->JDBC Driver(OCI)-> TTC->Oracle Net->OPS->Protocol 
TTC (Two Task Command)  数据类型,字符集的转换
Oracle Net 打包 逻辑包  
OPS ( Oracle Protocal Stack/适配器) ,所oracle 包转换成可网络传输的包(根据协议的不同)
IIOP   Internet Inter-ORB Protocal 

Java Applet ->JDBC Thin Driver ->JavaTTC->Java Net ->TCP/IP
服务端 TCP/IP->OPS->Oracle Net->TTC->oracle DB

Browser ->HTTP,IIOP->TCP/IP  服务端 TCP/IP->OPS->HTTP,IIOP->Oracle DB


sqlnet.ora和tnsname.ora是客户端
listener.ora 是服务端

sqlnet.ora服务器端/客户端都是要的


动态增加监听,可以没有listener.ora文件 使用PMON进程
一但监听通过了请求,就会分给Dispatcher ,不再经由监听分发
listener可以使用NMP 协议命名管道

SNMP(Simple Network Management Protocol)

一个Listener  可以服务多个DB,也可多个Listener服务一个DB


----listener.ora中	
LISTENER =
  (DESCRIPTION_LIST =
    (DESCRIPTION =
      (ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))
    )
  )
SID_LIST_LISTENER =   #####LISTENER是对应上面的,中要加多个(SID_LIST =
  (SID_LIST =
     (SID_DESC =
       (GLOBAL_DBNAME = orcl3)		###任何取名 别名  匹配tnsanme.ora的 (SERVICE_NAME = orcl3),也可无
       (ORACLE_HOME = c:\oracle\product\10.1.0\db_1)  
       (SID_NAME = orcl)
     )
  )

 ----tnsnames.ora中
 ORCL2=
  (DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVICE_NAME = orcl3)		####匹配listener.ora的GLOBAL_DBNAME ---OK
    )
  )

--

SID_LIST_LISTENER1 =
  (SID_LIST =
     (SID_DESC =
       (GLOBAL_DBNAME = zh)
       (ORACLE_HOME = c:\oracle\product\10.1.0\db_1)  
       (SID_NAME = orcl)
     )
     (SID_DESC =
       (GLOBAL_DBNAME = orcl3)  #####多个GLOBAL_DBNAME的配置方式 OK 
       (ORACLE_HOME = c:\oracle\product\10.1.0\db_1)  
       (SID_NAME = orcl)
     )
  )

---上OK

initSID.ora初始参数文件中
	service_names=('orcl3','zh')	#相当于listener.ora中 如上的多个GLOBAL_DBNAME的配置
	instance_name=orcl3			#相当于listener.ora中   (SID_NAME = orcl)
	##以上OK必须用LISTENER做名,否则定义local_listener参数
	local_listener='(ADDRESS = (PROTOCOL=TCP)(HOST=localhost)(PORT=1521))'	##如果监听不是LISTENER , 指定它的值, Listener启动后要等一会才能连上OK
	
						
使用初始参数文件 启动数据库   就可以在tnsnames.ora中使用 (SERVICE_NAME = orcl3)或者(SERVICE_NAME = orcl3)来连接服务器
lsnrctl start/stop Listener1  ###默认是 Listener,

show parameter service_names;	
show parameter instance_name;  
alter system set service_names=zh,orcl
    

参数  dispatchers=	##共享服务器

lsnrctl > status / help
	>set current_listener listener1   ###默认是 Listener,
	>set trc_level ADMIN  ##日志级别listener.ora的示例中值有OFF/USER/ADMIN/SUPPORT/0-16
	>show connect_timeout
	>help set 显示可用的命令,也可以看listener.ora的示例，和set对应的，除password不可以show
	>change_password
	>set password
	>save_config
	>help show


http://localhost:5560/sqlplus
http://localhost:5560/isqlplus/dba    oracle 11g 好像没有了isqlplus  但在em中 sys 登录后 下方有 "SQL 工作表"

emctl dbconsole start 显示网页为 
https://localhost:1158/em/console/aboutApplication
https://localhost:1158/em/就可以  11G
db_1/HOSTNAME_orcl/sysman/config/emkey.ora中是对Enterprise Manager数据进行加密密钥,务必备份些文件





Hostname方式 必须是:1521 端口,的TCP协议 ,不可用Connection Manager,不可使用安全选项
sqlnet.ora中
	names.directory_path=(HOSTNAME)  ###如是tnsname ,使用文件tnsnames.ora来解析@后的
	sqlnet.authentication_services=(NTS)

	zhHost 是主机名 ,以下必须是相同的 要/etc/hosts ,来解析IP
	sqlplus hr/hr@"zhHost"    
	listener.ora 中的SID_LIST_LISTENER中必须是GLOBAL_DBNAME=zhHost  ##也可是 zhHost.test.com,但要加在
	C:\WINDOWS\system32\drivers\etc
	192.168.1.108      	zhHost.test.com	 zhHost	

	要用这样来连接 sqlplus hr/hr@zhHost/orcl  或者 sqlplus hr/hr@192.168.1.108/orcl //OK
	names.directory_path=(HOSTNAME,TNSNAMES),listener.ora 中的SID_LIST_LISTENER多加一个  (SID_DESC =  (GLOBAL_DBNAME = zh),就可使用两种方式  //OK


col xx format a20

tnsnames.ora中配置
 (CONNECT_DATA =
      (SERVER = DEDICATED)

DEDICATED
SHARED   ##sysdba用户必须用独占服务模式,要有Dispatcher,不同的协议 不同的Dispatcher 
	服务器端启用Dispatcher,  alter system set dispatchers='(protocol=tcp)(dispatchers=3)'  
	
	如不能连接要(SERVICE_NAME = zh)改为  (SID= orcl)DEDICATED也可

共享服务 Dispatcher把请求放入请求队列,多个共享服务器进程去读并处理,把结果送入响应队列,Dispatcher再把结果送入用户
所有人共享一个请求队列,每个用户有一个响应队列


独占:PAG中有[Cursor state],	Stack Space,User Session data
共享:PAG中有[Cursor state]  , 而Stack Space,User Session data,放到SAG中了

dispatcher和shared_servers是必须的

以下可选的,不可以动态改变,加scope =spfile
show parameter max_dispatchers;		
show parameter max_shared_servers   最多的共享服务数,处理请求队列的
shared_server_sessions  共享的最大连接数,保持一些连接给独占 =   总的sessions - shared_server_sessions
circuits   虚拟线路数, 


alter system set dispatchers='(protocol=tcp)(dispatchers=3)(protocal=ipc)(dispatchers=2)'##要listner.ora中有IPC协议

large_pool_size  原来在SGA的Stack Space,User Session data,可以放在这

lsnrctl services   有了一些共享,独占

 select * from V$circuit; //有多个共享连接,应该是在线share用户数

V$session; 总的
V$shared_server 
V$dispatcher
V$shared_server_monitor
V$queue  //请求队列,呼应队列

----------------1z0-032 6课




Mean-Time-To-Recover   mttr	如果有错,必须在多长时内恢复
Mean-Time-Between-Failures mtbf 可以多长时间出一次错
最小的数据丢失



alter user xx quota 1m on indx;

imp help=y 有一个参数  resumable = Y 遇到错误时挂起
			resumable_timeout 等待管理员处理时间 

select * from V$session;  //SID,SERIAL#  是13,84

alter system kill session '13,84' 



show parameter undo_retention;  //保留多长时间

dbms_flashback.enable_at_time 
dbms_flashback.disable


show parameter bgprocess;
		db_writer_process; //DBW进程数

查询时 server　process进程把数据读入到Data Buffer Cache

LGWR 只有一个
同一日志组的是相同大小的  alter database add logfile member  'xx' to group 2



alter system checkpoint

show parameter	 log_checkpoint_interval  //多少事务量,事务块大小
		 log_checkpoint_timeout   //多长时间

fast_start_io_target
fast_start_mtt_target  //单位秒

V$archive_processes;  //归档进程  

recover_parallelism  //并行

fast_start_parallel_rollback //可用值FALSE,LOW(2*CPU),HIGH(4*CPU)

V$fast_start_servers;
V$fast_start_transactions;


aler tablespae users begin backup;

aler tablespae users end backup;



1.shutdown normal/immediate   不能用force
2.startup mount
3.alter database archivelog/noarchivelog
4.alter database open
5.全备份数据库       ###日志模式的改变，先前的备份以后已经没用了



初始参数log_archive_start = false 自动归档日志，推荐用自动，false是自动,也是在 archive log list中的值??????????
10g中log_archive_start过时了,只要启动归档就是自动归档

alter system set log_archive_max_processes=3 //最多归档进程数


archive log stop/start //停止自动归档

alter system archive log start/stop  ;// start to '/user/xxx'


先用archive log list看
alter system archive log sequeue 52; //手动归档　　52号的日志
alter system archive log current; //当前日志
alter system archive log all;     //全部归档

log_archive_duplex_dest 是8i的参数 ;不要用它
log_archive_dest	是8i的参数

log_archive_dest_n  //归档目标  值是"location=d:/"   或者是 "SERVICE=standby" 其它机上的  standby的配置????????????????????
log_archive_dest_1="location=/archive MANDATORY REOPEN" 
log_archive_dest_1="location=/archive MANDATORY REOPEN=600" //出错重试时间
log_archive_dest_1="location=/archive OPTIONAL"             //可以不归档

log_archive_duplex_dest 和 log_archive_dest// 老版本的



log_archive_min_succeed_dest=2 至少要2个dest成功,当有OPTIONAL时,要>=MANDATORY的数时才有意义


alter system set log_archive_dest_state_3=DEFER  推迟/ ENABLE 


log_archive_format %S %T   (sequence,thread)

V$archive_log  //已经产生的
V$archive_dest  //
V$archive_processes;
V$log_history;
V$database;


//-------------------9
recovery manager (rman ),从控制文件读数据库结构,可以管理多个数据库,可以把多个数据的结构读入catalog目录中

rman nocatalog  target sys/sys@orcl   //使用nocatalog ,读控制文件
表空间中分配的100M,只用了50M,冷备浪费空间
可以存经常的备份,恢复操作,要用 catalog

rman catalog  xx/yy@zz target sys/sys@orcl  ###catalog DB的用户名xx,target是要备份的数据库

channel使用磁盘 和磁带(MML Media Management Library是磁带的Driver)
目标数据库 通过channel备份的数据 叫备份集 backup set,可能有多个文件,每个叫backup piece


RMAN> run {
	allocate channel d1 type disk;
	backup tablespace system  ;
	release channel d1 ;
	}		必须是archive log模式
	

参数 control_file_record_keep_time   ## 7 单位是天,当rman使用nocatalog时,备份信息存放在control文件的时间
					控制文件 不能保存rman 的脚本


 create script tt1    ##只能对catalog
 {
	allocate channel d1 type disk;	//sbt_tape是磁带,默认是disk,可以多个channel对应不同的硬盘
	backup database ;
	release channel d1;
 }


如使用磁带 sbt_tape ,要用MML,,,磁盘disk就不用了
RMAN>show all    显示所有的默认值

	configure default device type to disk;		//sbt,sbt_tape,
	configure device type  disk parallelism 3;	//自动分配,释放通道,并行数3
	configure channel  device type disk format= '/u01/back/%U';   //默认保存文件位置
	configure channel  device type disk maxpiecesize 2G;

会开启oracle server 进程 

run{
allocate channel c1 type disk format ='/xx/xx.bak';表示这个通道的备份的目标
backup datafile  '/cc/cc.dbf';
}

rman> connect target sys/sys@orcl  只有sys有权限备份

Media mangement library  MML


set oracle_sid=orcl
rman target sys/sys ##不用@ ,因oracle_sid,默认是nocatalog


list backup of tablespace users; 备份的记录
backup tablespace users;即可单独,也可在run{ }中

report schema;
report need backup days 5;   5天内需要备份的
report need backup redundancy 3;  //冗余少于3个,报告
report need backup incremental 3; //恢复时需要超过3增量的文件,3个连续的文件才可以恢复

CONFIGURE MAXSETSIZE TO UNLIMITED; 备份集大小无限制

configure backup optimization  on;
configure retention policy to recovery window of 7 days;

configure retention policy to redundancy 2; //2份的冗余
configure datafile backup copies for device type disk to 2;
使用show all可以看到的  show  retention policy 


configure retention policy clear; ##clear 还原默认值
configure channel device type sbt clear;  //disk clear可以清format


show retention policy

list backup/copy of database;
list backup/copy of tablespace   system;
list backup/copy of datafile    'd:\oracle\oradata\orcl\users.dbf'; 这个文件的copy记录
	copy 是像操作复制一样

rman是调用一些PL/SQL的包的一些过程来完成的

--------------10
全备:备份所有的数据文件,控制文件
部分备:表空间,数据文件,控制文件

口令文件,参数文件,可以建,备份是个好习惯

select * from dba_data_file
热备份,必须打开归档日志

host 调用系统命令


alter tablespace uesrs begin backup;  //非归档时不可使用
手工复制多个表空间对应的数据文件(host cp xx ,xx),再end backup
alter tablespace uesrs end backup;

end backup后建议做一次日志的switch
alter system switch logfile;


V$backup; //是否进入备份即是否begin backup  ,列file# 和v$datafile的 file#对应,在v$datafile TS#对应到V$tablespace  TS#
V$datafile_header;  列TS# V$tablespace ,FUZZY文件是冻洁,无效啊????


断电/忘记end backup时,再启动时会报错,(如 startup force)
alter database datafile 4 end backup ;

alter tablespace xxx read only; //只做一次备份,就可以了 ,如改成可写,也要备份控制文件(记录表空间是读,还是写)
SCN号不会变,系统改变号


create table/index 是可加 logging/nologging  选项的

create table test(id int) tablespace users nologging; //对表加载数据时不会日志,不可全恢复

备份控制文件  
alter database backup controlfile to '/back/control.bak'
alter database backup controlfile to trace; 
###user_dump_dest的值C:\oracle\diag\rdbms\orcl\orcl\trace目录中生成create controlfile 脚本,按日期排,不能打开,记录在alert_orcl.log中的文件名

create pfile from spfile;备份spfile



验证备份是否有出错 dbverify工具 dbv help=y

dbv blocksize=8192 file=d:\xx.dbf logfile=xx.log

注意blocksize=8192要和数据库一样  ,logfile=不要加'     ####'
要验证的文件可以在线,离线的,如归档,备份的都可以


dbv 好像不能验证rman backup出来的文件

--------------11

rman  必须在mount下运行,来读控制文件信息

>rman target sys/sys nocatalog

run{
copy datafile 'xx' to 'xx';   ###copy 是像操作复制一样,backup是对有效空间
}
一个备份集中可能有多个文件,backup生成的

backup database format '/xx/df_%d_%s_%p.bus'  filesperset=2; ####每个结果集中有2个piece


设置双工模式时格式中需要有 %c  
CONFIGURE DATAFILE BACKUP COPIES FOR DEVICE TYPE DISK TO 2 两份数据文件

run{
allocate channel t1 type 'sbt_tape' maxpiecesize=3G;   #####最大块
backup format 'df_%t_%s_%p' filesperset 3 (tablespace users);
release channel t1;
}

%t time stamp
%s backup set number
%p piece number
%c copy number of the backup piece within a set of duplexed backup pieces
%d name of the database
%U system-generated unique filename (default). 


备份归档(只可全备) backup format '/disk/backup/ar_%t_%s_%p' archivelog all  [delete [all] input];

rman 不支持非归档

run{
	copy current controlfile to 'xxx';
	copy datafile 'xx' to 'xx' tag=DF3, archivelog 'xx' to 'xx';   ###tag是用于标识的
	copy datafile 3 to 'xx';	#####V$datafile 中的FILE#

report schema 来全部数据库

list copy of datafile 20;//每次的copy记录
list copy of controlfile;
list backup of tablespace system;

差异备份 (如前面有一个坏了,后面的全不能用)/ cumulative累积的备份

noarchivelog 必须全备 
backup database filesperset 3

CONFIGURE CONTROLFILE AUTOBACKUP ON/OFF //自动备份控制文件,SPFILE

###tag是用于标识的

V$archived_log;
V$backup_corruption; backup时有出现坏
$copy_corruption;
V$backup_datafile;
V$backup_redolog;
v$backup_set;
V$backup_piece;


监控
V$session;
V$process;

V$session_longops;


-----------------12 Uer-Managed Complete Recovery

恢复时,非归档日志模式,只能全恢复,只能恢复到备份的时间 (如没有日志文件可重建)
1.shutdown immedate
2.复制回全部 数据文件,控制文件 
3.startup mount
  recover database until cancel; 
4. alter database open resetlogs; 重建日志文件



恢复时,归档日志模式 
1) 完全恢复 ,要全的归档日志,在线日志
	恢复到数据库失败处 
	1. alter database datafile 8 offline
	2.只复制回损坏对应的备份文件,
	3.recover datafile 8;  #如果要日志文件会提示
	4.alter database datafile 8 online

	V$_recover_file 需要修复的文件有哪些
	V$_recovery_log 修复文件要用哪些归档日志


	recover database	全修复
	recover  datafile 'xxx.dbf'
	reocver tablespace users; 可对表空间
	recover datafile  3

	
提示日志文件,改变archive 的位置,alter system archive log  ... to 'location'
之前用sqlplus工具中的 set autorecovery on 
提示中输入auto,
使用recovery automatic 

alter database rename file '' to 'xx'  改变文件位置,会改控制文件,
recover datafile 'xx' (8)

如果数据文件没有备份过,要求日志是全的,坏的部分不是System表空间

对从来没有备份的数据文件
	alter tablespace users add datafile 'xx.dbf' size 10m;
	alter table hr.employees allocate extent (size 100k datafile 'xx.dbf');

	坏的数据文件可先offline,再open database
	alter database create datafile '新路径.dbf' as  '老路径.dbf'   //重建空数据文件 

	recover datafile '新路径.dbf'	 //从日志中修复

	online

备份是readonly 表空间恢复时是readwrite 要重建控制文件



2)不完全恢复
	

-----------------13---Rman Complete Recovery 
非归档时  改用>run {sql 'alter database noarchivelog';}
rman target /
>startup mount		中可以使用sqlplus命令
>restore database	rman把文件复制回来
>recover database	#就一个数据文件坏,也要恢复整个DB
 >也可以用shutdown immedate
>run {sql 'alter databasae open resetlogs'}

非归档 ,在databse open时不能用  backup database format 'c:\%U.bak',数据库open时,只能在归档时才行
非归档	mount下可使用backup,和restore,recover,




	
归档时 
rman>startup mount 如恢复整个数据库也要在mount下,如SYSTEM表空间坏,则不能open
rman>restore database	
rman>recover database	

归档时 
rman>run { set newname for datafile 'old.dbf' to 'new.dbf'    //恢复时想把某一文件恢复到其它目录
rman> restore tablespace users  //也可针对tablespace,而不是database
rman> switch datafile 'new.dbf';  //把改动写入控制文件
rman> recover tablespace users ;}




-----------------14--User-Managed Incomplete Recovery
alter database rename file  也可用alter tablespace users rename  datafile 

如控制文件结构和备份时不一样,也要用备份的控制文件,
系统改变号SCN ,在alter 文件中找,也有错误信息,drop table的记录

在做不完整恢复之前(防止第一次不成功,日志没用了)和之后,都要加数据库在全备,归档日志的删除(如恢复了,以前日志没用了)
可以通过logmnr, 来查看什么时间drop table,alert查drop tablespace


//不完整恢复
1.把以前备份全部数据文件复制回来(数据库数据文件的完整备份)
2.startup mount
3.recover database until time '2001-10-10:12:50:50' (只能对整个DB)
4.alter database open resetlog 清除所有日志,之前的日志没用了  (要再做全备份数据库)
archive log list以1开始



1.alter database backup controlfile to 'c:\xx'
2.drop tablespace xx;//记录时间
3.shutdown immediate
4.还是只恢复全部的数据文件
5.startup mount
6.recover database until time '2001-10-10:12:50:50' using backup controlfile ; 如果现在控制文件和备份时控制文件不一致改变过,如tablespace
可能要指定日志文件(archive log list ,select * from V$log,看Current对应GROUP#)
7.alter database open resetlogs;
仿问表是,datafile 还要重命名(alter datafile rename file '' to ''),V$datafile的状态是recover,

不成功?????应该是until cancel


recover database until cancel

init.ora中加入 _allow_resetlogs_corruption=TRUE  (隐含参数)所有的日志文件丢失


------------------15 RMan -Incomplete Recovery

要mount状态,全备份
until time,
until sequences	(desc V$datafile中的controlfile_sequence#) 
until scn	(desc V$datafile中的archivelog_change#)
open resetlog 

set NLS_LANG =american  环境变量 
set NLS_DATE_FORMAT='YYYY-MM-DD:HH24:MI:SS'  环境变量
run {sql 'alter session set nls_language=american';}

run{
//可以用allocate channel c1 type disk;
restore database;  //整个数据库
set until time='2000-12-09:11:44:00';
recover databae;
sql "alter database open resetlogs";
}

run{
set until sequence 120 thread 1;   //是<120的序列号
alter databse mount;
restore database;
recover database;
sql "alter database open resetlogs";
}

---------------------16 Rman Maintenance

rman>crosscheck backup;  //检查已经的备份 backup命令

rman>crosscheck copy;    //检查copy 命令 
rman>crosscheck copy of datafile 1;
rman>copy datafile 1 to 'c:\xx.dbf';

rman> bakcup tablespace users formate 'c:\oracle\xx_%U.bak'
rman> crosschek backup of tablespace users;


rman>delete copy of datafile 1;
rman>delete backupset 102;//标记
rman>delete noprompt expired backup of tablespace useres;   //resetlog
rman>delete obsolete   //废弃


rman>backup archivelog all delete input //备份归档,删除原来的


rman>copy datafile 'source.dbf' to 'dest.dbf'
rman>list copy of datafile 'source.dbf'		[S(status)]那列是U/A (available)

rman>change  copy of datafile 'source.dbf' unavailable   
rman>change  copy of datafile 'source.dbf' available

rman>change datafilecopy   'dest.dbf' available		//是已经备份出来的目标
rman>change copy  of datafile   'source.dbf' available //是备份的原

rman>change backup of controlfile unavailable //所有的控制文件都无效了
rman>change backup of archivelog all unavailable

rman>change backup of archivelog sequence between 230 and 240 unavailable

rman>change backupset 123 keep forever nologs;//增加保留时间   ,nokeep
rman>change datafilecopy 'dest.dbf' keep until time 'SYSDATE+60';// 已更改,但后有错误??? 当前时间相加60天

rman>catalog datafilecopy 'dest.dbf'  //对手工的备份(alter tabalespac users begin backup ;host copy xx,yy),记录在rman中
rman>catalog archivelog 'xx','yy';
rman>catalog controlfilecopy 'xx'; //对alter database backup controlfile to 'c:\xx';

rman>change archivelog ...uncatalog ;//不记录在rman中
rman>change datfilecopy  'dest.dbf' uncatalog ;

//-------------------------17 Recovery Catalog Creation and Maintenance
nocatalog 不可以保存脚本
rman>create script tt1{
	allocate channel d1 type disk;
	backup database;
	release channel d1;}

数据库备份时也要把catalog数据库进行备份
如果target DB有结构变化如加表空间，catalog　DB也要同步


catalog DB可以和target DB在一起，也可以独立

create tablespace rman1 datafile 'c:\oracle\oradata\orcl\rman1.dbf' size 30m;  //rman关键字
create user rman identified by rman default tablespace rman1 quota unlimited on rman1 temporary tablespace temp;
grant connect ,recovery_catalog_owner to rman ;// connect角色,RECOVERY_CATALOG_OWNER角色有 CREATE TABLE 

rman catalog rman/ramn
rman>create catalog tablespace rman1 //rman是关键字,不能用,建了很多表和视图
rman>connect target sys/sys  //连目录数据库 
rman>register database		//读目录数据库信息,写入catalog
rman>report schema

drop tablespace rman1
rman>再report shcema就没有rman1表空间了

rman>resync catalog  同步


rman> reset database;  //如果target DB　，open resetlogs;

rman>report need backup days 3
rman>report need backup redundancy 3; 冗余少于3个的备份

catalog 数据库用户 的 rc_database,rc_datafile,
rc_stored_script (保存的脚本名),rc_stored_script_line(脚本内容),rc_tablespace


rman>create script Level0Backup{
	backup
	incremental level 0
	format '/u01/%d_%s_%p' 
	fileperset 5
	(database include current controlfile);
	sql 'alter database archive log current';
	}

rman>run {execute script Level0Backup;} //执行
rman>replace script Level0Backup {...} //修改
rman>print script Level0Backup //显示
rman>delete scipt Level0Backup

备份catalog DB



//-------------------------18 Transporting Data Between Databases
不同版本,不同平台,exp/imp
exp 交互模式,提示进E(完整的数据库)  //表示除sys以外的

---par.txt内容
	userid=hr/hr
	file=c:\exp.dmp
	tables="employees"
	//可加 direct=y
exp parfile='c:\par.txt'    //不要加;
 

TABLESPACES 按表空间
OWNER=hr  //(hr,scott) ,或者 hr,scott  但要有权限	
TABLES		//必须有权限
exp hr/hr file=c:\bak.dmp tables=employees query='where employee_id">"150' //solaris可能用\> ,只表的一部分

exp 'sys/sys as sysdba' full=y file='xx.dmp'  不包括sys的对象

ROWS      导出数据行 (Y)
dirct=y	 //表示跳过Buffer Cache和 Evaluating Buffer,客户端服务端字符集必须相同,buffer参数没有用,如有LOB,BFILE,REF数据类型不能使用
INCTYPE=cumulative    累积的 增量导出类型 
TRANSPORT_TABLESPACE=y 表空间的定义信息，还必须传物理文件  ,tablespaces=(users)
log=xx.log


如果数据库的物理损坏 exp不行的

imp system/manager file=C:\exp.dm fromuser="" touser=""  tables=""


多字节字符集导出时，在导入时也要相同字符集

不能只使用exp 来备份，只是备份对象，但系统信息没有

//-------------------------1９　Loading Data into a Database
有第三方工具和SQL Server,MySQL

高水印　High-water mark ，表的最高数据量的位置
create table emp as select * from employees;
insert /*+append */ into emp nologging select * from employees; //不会使用高水印之间的没用空间　
/*+append */是优化提示	


alter session enable parallel dml; 可以并行insert,要用temporary segment

insert /*+parallel(hr.employees,2) */ into emp nologging select * from employees; 


sqlldr可以把control文件和数据文件放在一起

sqlldr hr/hr control=xx.ctl log=xx.log direct=y  //不会有cache,insert触发无效,只有primary key ,unique,not null约束,独占这张表,其它人不可改变
也是放在高水印以下的

controlfile注释用--  ,大小写不区分
	里可以有 badfile 'xx'
		discardfile 'yy'

数据文件里可以有变长记录,数据流记录



my.ctl 文件中内容是
	LOAD DATA
	INFILE 'c:\Data.txt'
	APPEND INTO TABLE student
	FIELDS TERMINATED BY WHITESPACE  
	(id,name)


c:\Data.txt有两行(xls excel 文件也可打开)
	1	李
	3	王


使用方法
sqlldr hr/hr@127.0.0.1:1521/XE control=my.ctl

如有数据内容不符合会生成Data.bad文件

如oracle有日期类型,如date(只有时间) ,timestamp(有时间)也是OK 用FIELDS TERMINATED BY '|'
用select  出的日期 有空格

c:\Data.xls有下面
   5 李    02-12月-08 03.34.21.000000 下午 |

就可以用---OK
LOAD DATA
INFILE 'c:\Data.xls'
APPEND INTO TABLE student
(
id   char   terminated   by   WHITESPACE,
name char   terminated   by   WHITESPACE,
birthday timestamp   terminated   by   '|'
)


---InstantClient-12.2 sqlldr 成功示例

LOAD DATA
INFILE 'c:\temp\Data.csv'  "str '\r\n'"
APPEND INTO TABLE T_OP_LOADING_CODE 
FIELDS TERMINATED BY ',' optionally enclosed by '"'
(ID,TYPE,LOADING_CODE,
CREATE_TIME DATE "yyyy-mm-dd hh24:mi:ss" ,
OP_TIME DATE "yyyy-mm-dd hh24:mi:ss" , 
OP_USER_CODE,OP_USER,STATUS)

//	如是CSV 文件 FIELDS TERMINATED BY ',' optionally enclosed by '"'
// 如有日期类型 CREATE_TIME DATE "yyyy-mm-dd hh24:mi:ss" ,
// windows换行符\r\n使用  INFILE 'c:\Data.txt' "str '\r\n'"

11516,01,ZC1720330628926,2017-07-04 13:41:19,2017-07-04 13:41:19,userCode,User,01


==========================================033
-------------------------1 数据库调优
rollback segment是老版本
undo 是新的

-------------------------2
alert.log

log_checkpoints_to_alert=true 参数 向alert.log文件中写checkpoint信息

alter session set sql_trace=true;  开启用户跟踪
execute dbms_system.set_sql_trace_in_session();


RDBMS\ADMIN\utlbstat.sql	//begin
RDBMS\ADMIN\utlestat.sql	//end


dmbs_stats包

dba_tables,dba_tab_columns
dba_clusters
dba_indexes,index_stats,index_histograms(索引)

select * from dba_segments;


pct_increase (percent百分比)

dba_ind_columns; 索引


timed_statistics 参数是 true
 select * from V$open_cursor;
 V$session_wait
 V$parameter  ,同 V$system_parameter


V$system_event的event 
V$event_name 的name	


V$sysstat  的　physical reads列 是物理读次数，不有命中
		db block gets列和consistent gets 的相加


select name ,value from V$sysstat where name='physical reads' or name='db block gets' or name='consistent gets'
select 1-(physical reads/(db block gets+consistent gets)) hit from V$sysstat //命中率


db_block_buffers 老参数, 新db_cache_size 
select * from v$sgastat where pool='shared pool' and name='free memory'; 
查可用内存


select count(distinct name ) from V$event_name; //事件个数

select sum(pins-reloads)/sum(pins) "lib cache" from V$librarycache;  //库缓存命中率
pins 命中和不命中的数量,reload没有命中的


select sum(gets-getmisses) /sum(gets) from v$rowcache; //数据字典的 命中率

v$buffer_pool_statistics
v$db_cache_advice
V$pagstat;
v$filestat; v$tempstat   
v$waitstat 锁
V$latch;
V$latch_Children; 子锁存器
V$latch_parent;
v$Lock;

V$rollstat

v$statname 的statistic# 与 V$sesstat的statistic#,sid又与V$session的sid关联
V$session_event的sid与V$session_wait的sid,
V$session_event的event 与eventname的name 与session_wait的event	 

安装 spcreate.sql  (sp 是 Stackpack) @C:\oracle\product\11.1.0\db_1\RDBMS\ADMIN\spcreate.sql
使用 exec statspack.snap 手工抓 ，要每隔几分钟
    

timed_statistics＝True 参数的
job_queue_process 同时的工作数

select * from dba_data_files;

设置oracle_home环境变量后　 @?rdbms/admin/spauto　　　@?是引用oracle_Home ,自动抓 默认是一个小时一次 ,可以改脚本

select job,log_user,last_date,next_date,interval from user_jobs;  //下次抓时间，间隔

select snap_id from satats$snpshot; //抓了哪些

execute dbms_job.remove(29)    user_jobs中的job

spreport.sql	 报告

alter system set  cursor_sharing=similar













