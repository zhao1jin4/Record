
================================031
oracle
	instance
		SGA （System Global Area）
			database buffer　cache,
			redo buffer cache,
			share pool(Libarary cache,data Dict cache)
			有两个可选的Large pool,Java pool


		后台进程：PMON,SMON,DBWR,LGWR,CKPT,Others
	database
		datafile 
		log file
		control file(用来连接INSTANCE 和 DATABASE)
		以上是必须的
		parameter file
		password file 
		archived log files





User Process-->Server process-->instance


shutdown immediate
startup nomount
alter database mount
调用了 control file
alter database open 对数据文件和日志文件的状态验证



instance --memory structure
			background process


database --data file
			log file
			control file







任何时候一个instance只能操作一个database
一个database可被多个instance操作



show sga; 显示内存大小信息

set wrap off
set linesize 200
select * from v$bgprocess 显示所有的可以使用的后台进程
			如PADDR字段有值(不是00)说明已启动的进程
			NAME 显示名字（如SMON..） DESCRIPTION 描述



必须的后台进程
			PMON，SMON，DRWR，LGWR，CKPT，REC0
			（DBWn ,PMON,CKPT,LGWR,SMON,RECO后的正确的）
select * from V$bgprocess where paddr<>'00'
	我的oracle10g没有REC0 有DBW0




Online redo log files 都可不只一个(最少有两个)
Data files (includes data dictionary)
Control files

select * from V$logfile；
select * from V$controlfile；
select * from V$datafile；
查看文件共有几条，存放位置


PGA（Program Global Area）
	不可共享的（SGA是共享的）


show parameter shared;  显示share_pool_size 
show parameter db_cache; 显示db_cache_size
show parameter log; 显示log_buffer


sga_max_size 设最大SGA大小

show parameter sga; 显示SGA_max_size大小
alter system set db_cache_size =20m; 如太大则超出了sga_max_size
alter session set nls_language=american;
show parameter nls;
alter system set shared_pool_size=64m;


1.share pool中Libary cache 存最近的SQL, PL/SQL执行计划

			data dictionary cache 存权限信息。。(又称row cache)验ＳＱＬ对象是否存在，语法分析，权限验证

两个不可单独设定，要用shared_pool_size设大小

2.database buffer cache 
show parameter db;   db_xxk_cache_size和db_cache_size
		

db_block_buffers老的参数　是块的数量 ，创建时的在块的大小在C:\oracle\product\10.2.0\admin\orcl\pfile\init 文件中的
							两个相乘是
基本单位是块大小；db_block_size 按块读数据大小到内存(如实际大小比块要小)
show parameter db;

db_cache_size 前面的sga 有过
db_keep_cahce_size
db_recycle_cache_size



db_cache_advice 让oracle自动设大小
aler system set db_cache_advice=on;设为on/off

3.Redo log buffer Cache
	主要的目的是recvery(恢复)log_buffer
	show parameter log_buffer;

4.Large Pool 可选的，设large_pool_size
5.java pool   java_pool_size


PGA
	dedicated serve专注的, 献身的
	shared server
user process:启动一个工具，不可直接操作数据库，要借助
server process
background process
 
必须的后台进程DBWn ,PMON,CKPT,LGWR,SMON,RECO正确的

archive log list
显示　为(数据库日志模式             非存档模式)

DBWn 把被改变的数据写到数据库
LGWR   把redo log buffer(SGA) 写到Redo log files(database) 里,如commit时　，写日志优先
SMON System Monitor 如需要recovery　会rollback uncommit;临时的清空
PMON Process Monitor进程监控　回滚用户强制终止的进程
CKPT　checkpoint会启动DBWn
ARcn Archiver 自动备份log

空间用完了extents分配一次性大小，按block
－－－－

conn system/manager as sysdba; 默认用户


开始中oracle univer installer和	database configuration Assistant
cmd中用dbca启动database configuration Assistant

cmd 中dbv 用于验证数据完整性,dbca

C:\oracle\product\10.2.0\oradata\orcl放oracle 必须的三类文件，*.ctl控制文件，*.dbf数据文件，*.log日志文件
即用select * from V$controlfile;
C:\oracle\product\10.2.0\db_1\RDBMS\ADMIN　　下存sql脚本，会提示要用什么用户运行这个脚本 如sys


C:\oracle\product\10.2.0\db_1\sqlplus\admin　下的脚本也可不同用户，如system


1.C:\oracle\product\10.2.0\db_1\NETWORK\ADMIN 下的sqlnet.ora文件 可通过系统ＯＳ　连接oracle （如可启动，关闭）

			本来就有的(加上)SQLNET.AUTHENTICATION_SERVICES= (NTS)　用conn / as sysdba　可连接数据库

2.口令文件　C:\oracle\product\10.2.0\db_1\database下的PWDorcl.ora(orcl是sid)文件删除　在cmd 下用orapwd命令
		直接orapwd提示　orapwd file=C:\oracle\product\10.2.0\db_1\database\PWDorcl.ora password=... entries=10 (除内部两个用户，还可最多加入的用户数系统管理员)
			重启服务器即可改密码：是在window下的重启services.msc命令 (原密码仍可用)
设 REMOTE_LOGIN_PAssWORDFILE 　为EXCLUSIVE C:\oracle\product\10.2.0\admin\orcl\pfile 的init.ora是设为这样的
								remote_login_passwordfile=EXCLUSIVE
								(或用show parameter remote_login_passwordfile)


UNIX and Linux口令文件是orapwORACLE_SID,windows是PWDORACLE_SID.ora


grant dba to hr 
grant sysdba to hr  
revoke sysdba from hr
select * from v$pwfile_users; 显示所有的sysdba权限的用户
cmd中oradim 命令　
sqlplusw /nolog

set autocommit on;


我的10g没有Oracle Enterprise Manager Console中以独立方式连接不可备份，要用oarcle manager server方式

--------

系统参数文件　spfile(System param file) 即spfilesid,或spfile
				老版本中是initsid.ora 在C:\oracle\product\10.2.0\db_1\database下并重转到C:\oracle\product\10.2.0\db_1\dbs


spfile 文件不可用文本修改，用alter system

pfile implicit 暗示的 (oralce default parameter 的值 )explicit 清楚的

	按顺序找文件spfilesid.ora--spfile.ora--initsid.ora
		
C:\oracle\product\10.2.0\admin\orcl\pfile\下的init（初始参数文件） 文件改
	core_dump_dest=c:\oracle\product\10.2.0/admin/orcl/cdump　里的timed_statitstics=false

conn sys/admin@fox as sysdba
shutdown abort 重启starup
select name,values from v$system_parameter where name =timed_statistics 查看值

initsid.ora可用文本编辑　要重启服务生效
如＝右边的值是多值要用(....,....)

alter system set ...=... scope =memory 不会影响spfile里，如是scope =memory，重启后会生效
						scope =both

show parameter undo_tablespace;
desc v$system_parameter ;
不是所有的参数都可用alter system来修改
有两个字段是isses_modifiable和issys_modifiable
select name,isses_modifiable from V$system_parameter where isses_modifiable='TRUE';
是所有在session可改的

alter session set ...=....
select name,issys_modifiable from V$system_parameter where issys_modifiable<>'FALSE';

 issys_midifiable列有　immediate　所改的值立即生效，会影已有的session 和以后成的session
	deferred　不可影响已有的session可影响以后的

create spfile from pfile　建到C:\oracle\product\10.2.0\db_1\database\spfileorcl.ora下
create spfile='C:\oracle\product\10.2.0\db_1\database\...ora' from pfile
创建一个文件可指定路径

create pfile ='..' from spfile
create tablespace name　报错

show parameter db_create;
 显示；db_create_file_dest　
		db_create_online_log_dest_n 最多５个

如设了db_create_file_dest的路径，没设db_create_online_log_dest_n 时，会使用db_create_file_dest的路径
alter system set db_create_file_dest='D:/temp/';
create tablespace name　不报错 默认100m大小

alter database add logfile group 6 也放到一起 也是100m
drop tablespace name;会把文件也删除　控制文件的信息
alter database drop logfile group 6 也会删文件
startup force

老方式　create tablespace test1 datafile 'd:\temp\aa.dbf' size 2m 
		再drop tablespace test1;不会删文件
		create tablespace test1 datafile 'd:\temp\aa.dbf' reuse
		drop tablespace test1 including contents and datafiles; 会删文件  //一定要先contents再datafiles

shutdown  时1.关闭database 2。卸载数据库 3. 关闭实例


open
mount
nomount  读初始参数文件，分配内在，启动后台进程，定位控制文件
shutdown

background_dump_dest=''所对应的路径如不存在会创建C:\oracle\product\10.2.0\admin\orcl\bdump
startup noumount时alter_sid.log文件如不存在会创建
select * from V$instance
select * from V$bgprocess
select * from V$sga;
select * from V$database 只可在alter database mount 时可用。打开控制文件
						select * from v$tablespace;
						select * from V$datafile;
						select * from V$log;
只有在alter database open 时才可select * from 表 
help shutdown 显示帮助 transactional

background_dump_dest=c:\oracle\product\10.2.0/admin/orcl/bdump
core_dump_dest  =    c:\oracle\product\10.2.0/admin/orcl/cdump
user_dump_dest  =    c:\oracle\product\10.2.0/admin/orcl/udump
 
startup [nomount|mount |open]
		pfile=
		restrict
		recovery
		force
startup force pfile=C:\oracle\product\10.2.0\admin\orcl\pfile\int...ora mount (指定文件不可用nomout)
alter database .. open read only 因上是force要Recovery
一但open 后不可再用read only。不可update表 或alter table ,grant...
可以 select ,alter databse datafile 8 online (offline  提示)
不可 alter tablespace users offline;


startup restrict 
alter system enable restricted session 后只restricted sesstion权限的用户可访问

shutdown 1.abort 2.immediate 3.transaction 4.normal
noraml会等用户断开连接
transaction 等所有的transaction ,不等用户断开连接
immediate  不等transaction，rollback


每天，1. altersid.log, 2.background trace files,
				(background_dump_dest=c:\oracle\product\10.2.0/admin/orcl/bdump)
	3.user trace files
				user_dump_dest  =    c:\oracle\product\10.2.0/admin/orcl/udump
				默认是关闭的要设
				alter session set sql_trace=true
				dbms_system.set_sql_trace_in_session
				初始参数文件设 sql_trace=true

－－－－－4 create database 

admin\oracl目录与db_1\admin\sample的结构相似
db_1\oradata\sample

1.用oracle databse configuration assistant（用命令 dbca） 可保存模板，供下一次建时不用再次配参数
2.create database

unix 下设环境变量oracle_base(最顶的),oracle_home(db_1),oracle_sid,ora_nls33
				path,library_path
				用setenv命令
window 下设oracle_sid 


data block sizing .helps to determine the SQL_Area_size

2.手工，sid,设字符集，设环境变量。设初始参数文件，start instance (onmount) 下create database
	
set oracle_sid=...FOXCONN
oradim -NEW -sid FOXCONN -initPWD pass -pfile  C:\oracle\product\10.2.0\db_1\database\initfoxconn.ora 
		后在services(服务中)可以看到新建的sid（foxconn） 在regedit中\HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\多了一内容
		在database 下生成PWDfoxonn.ora文件，
11g是-sysPWD

把db_1\admin\sample目录copy到admin\下  
		改名为sid（foxconn） ,改pfile目录的文件,重名为intsid(foxconn)  ***(不一定)要copy orcl目录下的新的initorcl.ora文件	
					
			***打开D:\oracle\product\10.1.0\admin\orcl\pfile，找到init.ora文件，把它拷贝到D:\oracle\product\10.1.0\bd_1\databse下，并将其改名

								db_home=foxconn(oracle sid)
			删%seed_control% 写control_files= ('C:\oracle\product\10.2.0\oradata\foxconn\control01.ctl','.....')控制文件路径
				db_block_size=2048
				undo_tablespace=UNDOTBS1 注意此处的“UNDOTBS1”要和建库脚步本中对应

		------	在C:\oracle\product\10.2.0\db_1\database下要手工建文件，可 copy已有的 initorcl.ora并改文件名和路径sid为新的路径（foxconn） 
		------											我的内容不一样我的是spfile='......'					
							

sqlplus (连接用sys/pass刚建的) 中 create spfile from pfile 生成在同一目录下(database下)
		如失败创建spfile  我是手工在C:\oracle\product\10.2.0\db_1\dbs\下copy
startup pfile= C:\oracle\product\10.2.0\admin\foxconn\pfile\initfoxconn.ora nomount（nomount状态下）
select * from V$instance;
create database db01
	logfile 
		group 1('.../oradata/.rdo') size 15m,
		group 2('.../oradata/.rdo') size 15m,
		group 3('.../oradata/.rdo') size 15m
	maxlogfiles 10
	maxdatafiles 1024
	maxinstances 2
	datafile '/.../oradata/**.dbf' size 50m(大于20m)
	undo tablespace UNDO
		datafile '...' size 40m
	default temporary tablespace TEMP
		tempfile '...' size ..m
		extent management local uniform size 238k
	character set AL32UTF8
	national character set AL16UTF16
	set time_zone ='America/New_York'
	;

--
演示的( logfile 中--不要少了逗号)
create database foxconn
	maxlogfiles 10
	maxdatafiles 1024
	maxinstances 2
datafile 'C:\oracle\product\10.2.0\oradata\foxconn\system01.dbf' size 50m
undo tablespace UNDOTBS1 datafile 'C:\oracle\product\10.2.0\oradata\foxconn\undtablespace.ora' size 40m
sysaux datafile 'c:\oracle\product\10.1.0\oradata\foxconn\sysaux01.dbf'  size 40m
logfile 
		group 1 'C:\oracle\product\10.2.0\oradata\foxconn\redo01.ora' size 15m,
		group 2 'C:\oracle\product\10.2.0\oradata\foxconn\redo02.ora' size 15m
default temporary tablespace TEMP
		tempfile 'C:\oracle\product\10.2.0\oradata\foxconn\tempfile.ora' size 20m
		extent management local uniform size 238k
character set AL32UTF8
national character set AL16UTF16
set time_zone ='America/New_York'
--sysaux  是新加的 startup pfile= C:\oracle\product\10.2.0\admin\foxconn\pfile\initfoxconn.ora 要用orcl新的文件，改orcl为foxconn
--"UNDOTBS1" 要与initfoxconn.ora文件中一致
最基本的日志组有两个


C:\oracle\product\10.2.0\db_1\RDBMS\ADMIN 下有slq脚本

db_create_file_dest
db_create_online_dest_n

如已存在改 size 00m 为reuse

sys  的密码是change_on_install

----------
create database时orace服务创建
1.data dictionary tables 2.Dynamic performance tables



数据字典表是加密的，是只读的

1.DataDictionary
		1.基本表。	存储数据描述信息。在create database 时
		2.数据字典视图 catalog.sql 脚本 通过视图查看加密的基本表
不可通过DDL修改


select * from dba_user; 所有存在的用户ID
select * from dba_objects; 所有的数据对象

create(drop) table 时会对数据字典数据修改，如加或减 （grant select on 表 to hr  也会记录到数据字典中）

dba_XXXX 所有的最大范围
all_xxx  我可以访问的
user_xxx  只是我自已有的
select * from user_tables
select * from all_tables  比user_xx多一个owner列
select * from dba_tables 要有dba的权限

 select * from user_objects ubs where ubs.OBJECT_TYPE='TABLE'; 
 select * from user_objects ubs where ubs.OBJECT_TYPE='SEQUENCE'; 
 
2.Dynamic performance tables
时时被改变的
在mount来自控制文件的文件的信息   和nomount来自内在的动态信息  时有，而.ataDictionary 中可在open database时可以访问
不可DDL

在nomount 下可以访问select  * from v$instance; select  * from v$sga; 
不可select * from V$tablespace 要在mount时可以，也可select * from V$database
只在 open时可以select * from user_tables;

desc dictionary;(视图) 可以dict
desc v$fixed_table 所有的动态性能表

刚建的database 连接上不可select * from User_tables; execute dbms_output.put_line('test string') 也不可
clear screen(只可在sqlplusw); help clear

运行脚本中，包括，catalog.sql @C:\oracle\product\10.2.0\db_1\RDBMS\ADMIN\catalog.sql
运行后可以 select *..
catproc.sql 可以dbms.output.put_line
start   看C:\oracle\product\10.2.0\db_1\sqlplus\admin\help\hlpbld.sql中的注释


－－－－－Control File
控制文件是一个二进制文件

控制文件备份到不同的分区上，只要有一个可用就可运行
建库时，从initorcl.oar中设控制文件

mount时会读，只可与一个 dababase相关联
nomount 时可create controlfile 重建(要试 －－－－不全的)create controlfile  noresetlogs(resetlogs);

select * from V$database 记录在控制文件中
select * from V$tablespace 
select * from V$logfile
select * from V$log;可看sequence列，日志顺序号
select * from V$backup;哪些文件进入了备份状态
alter tablespace users begin backup; 后则有一条select * from V$backup;的status 是Active
select * from V$archived_log;
alter tablespace user end backup ;

一。备份控件文件
1.
show parameter spfile;
select * from V$controlfile;
alter system set control_files='C:\oracle\product\10.2.0\oradata\orcl\control01.ctl','..02','..03','.在原来的基础上增加一个.04' scope =spfile

show parameter control
shutdown immediate;
再copy控制文件改名为新加的文件(上的),备份时会同进会向这4个控件文件中写
starup 后，再select * from V$controlfile;显示多了文件 －－－－我的无效

2.
shutdonw
先copy控制文件，改pfile目录下的init(sid).ora文件，加一个control_files的值 
因spfile高于pfile，要startup pfile= '上改的文件位置'
3. oracle 运行时 alter database backup controlfile to '路径  xx.dat' ;二进制文件
 
 
用OMF， 如未设control_files ,会放在db_create_online_log_dest_n.指定的目录下，命名为alterSID.log
 
 
 
4. alter database backup controlfile to trace；返译创建控制文件的脚本
 会在C:\oracle\product\10.2.0\admin\orcl\udump\目录下生成一个文件，
 show parameter user_dump
 是create controlfile语法
STARTUP NOMOUNT
CREATE CONTROLFILE REUSE DATABASE "ORCL" RESETLOGS  NOARCHIVELOG
    MAXLOGFILES 16
    MAXLOGMEMBERS 3
    MAXDATAFILES 100
    MAXINSTANCES 8
    MAXLOGHISTORY 292
LOGFILE
  GROUP 1 'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\REDO01.LOG'  SIZE 50M,
  GROUP 2 'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\REDO02.LOG'  SIZE 50M,
  GROUP 3 'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\REDO03.LOG'  SIZE 50M
-- STANDBY LOGFILE
DATAFILE
  'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\SYSTEM01.DBF',
  'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\UNDOTBS01.DBF',
  'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\SYSAUX01.DBF',
  'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\USERS01.DBF',
  'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\EXAMPLE01.DBF'
CHARACTER SET ZHS16GBK
;

--网上的select * from dba_object_size


select * from V$parameter where name like 'control%';

show parameter control(control_files)  查看各自的信息
select * from v$controlfile_record_section
select * from V$archived_log
select * from V$log 日志组
select * from V$logfile
select * from V$tempfile

----七、redo log file
1.在线日志文件
2.归案日志文件 (即备份日志文件)用archive log list

recovery
如 commit 后数据在内在中，未写硬盘，断电，再启时会 redo
最少要两个group ,组1满后写组2,组2满了写组1(备份即 archive log list)
alter system archive log start (在启用备份时公,自动存档)


如组里有一个成员是好的，则可以使用，如全坏了，会报错
建议每个给不成员在不同的disk硬盘

LGWR进程 写下一个GROUP
alter system switch logfile; 即切换另一个日志组写
也会执行Check point 进程 写日志到文件，( 也写data file header ) 

alter system cheickpoint 强制让系统写一次到文件

alter tablespace users begin backup;和alter tablespace users offline ;和
drop table ,tuncate table ,会做时 oracle 自动执行alter system chekpoint

fast_start_mttr_target参数

mttr是mean(平均的意思) time to recovery  最长时间间隔去recovery，控制数据同步频率高，recovery时间短
show parameter fast 的fast_start_io_target 老的
show parameter log_checkpoint 的 log_checkpoint_interval和log_checkpoint_timeout


(我的无效，禁用模式)注：如设了fast_start_mttr_target会自动设fast_start_io_target，log_checkpoint_interval和log_checkpoint_timeout

alter database add logfile group 2('..存放位置','。。可以只一个成员') size 1m
 alter database add logfile group 5('C:\oracle\product\10.2.0\oradata\orcl\log8.log') size 4m;
select * from V$log 后显示增加的 unused
建议每组大小要相同(可以不同)建议个数也相同
但是一个组中的每个成员必须相等
alter database add logfile member '' to group 2 ;不可加size 

 alter database drop logfile group 5; 物理文件没有删除

当前日志组，活动日志组，未归案的日志组不可删

select * from V$log ;status列的有一值为current,
一个组最少有一个日志成员：
alter databse drop logfile member '文件路径...' 
select * from V$logfile 的member列的路径

clear  logfile 重新初始化。
alter database clear logifle group 2 当前的不可
alter database clear logifle '文件路径' 当前的不可
alter database clear unarchived logfile group 5 当前的不可

copy物理文件 改名
alter database rename file 'C:\oracle\product\10.2.0\oradata\orcl\REDO01.LOG' to 'C:\oracle\product\10.2.0\oradata\orcl\redoaa.log'；改的名为已有的文件
OMF (oracle managed files)show parameter db_create;最多5个文件的多功
alter system set db_create_onlie_log_dest_1='...'
alter database add logfile group 5;大小由oracle 在上的路径中，默认100m
并且会在alter database drop logfile group 5时会自动删除对应的文件(用OMF)
用alter database add logfile group 4 '....' size 10m ,后再alter database drop logfile group 4 ;不会删除物理文件
select *from V$log;查看bytes,members,arc(是否归档),statuts(active,inactive,unuse)


-----备份数据文件
默认是非归档模式
alter system set log_archive_start=true scope=spfile;

干净关闭数据库 shutdown immidiate(transactional) 
startup pfile='' mount;
alter database archivelog/noarchivelog 改归档模式或非归档模式
alter database open
改了归档模式：要做全备份数据库 ：以前的备份已经不可用现在的历史来恢复
show parameter log_archive_start    

自动归档：archive log start 手工归档:　//存放在USER_DB_RECOVERY_FILE_DEST所指定的目录中
alter tablespace xx begin backup ;

手式复制备份操作系统文件
alter system archive log all;/current 将当前日志归档，
alter system archive switch logfile 切换多次回到原来那个
停止数据库删文件，再startup,报文件号6找不到，select * from V$recovery_file;//可以看文件号对应的文件
alter database datafile 6 offline drop;
alter database open;
再把备份文件复制回来
recover  datafile 6;=>选择auto //恢复
alter database datafile 6　online;
-----备份

-----丢失日志文件的解决
recover database until cancel
alter database open resetlogs
----


alter system archive log　start to '?/dbs/arch'
alter system archive log　stop
alter system archive log　sequence 052

select * from V$archive_log; 这个表来自控制文件(其它来自内在) 当前系统有些什么样的归案日志文件,显示文件路径..

如日志组写满了，还没有归档，不可以重写
show parameter log_archive; 

===1Z0-032的课程

log_archive_dest_n='LOCATION=/archive1 MANDATORY REOPEN=600' 日志写错误重试的时间
log_archive_dest_n='SERVICE=standby_db1 OPTIONAL' 不会重试
log_archive_min_success_dest=2
alter system set log_archive_log_state_2=enable/defer
log_archive_format=ARC%S.%T 保存文件的格式
V$archvied_log,
V$archive_dest
v$log_history
v$database
v$archive_processes

 显示log_archive_dest_n;和log_archive_max_processes(最大归案进程到10 select * from  v$bgprocess　ARC0-9)和log_archive_format

logmnr
1.设目录, 存日志文件
看初始参数文件utl_file_dir=''的目录在cdump 
--我的无([不要alter system set不可行的从V$system_parameter ]utl_file_dir='C:\oracle\product\10.2.0\admin\orcl\cdump')也不可,则要改init文件，用文件启动startup pfile=...

2.重启oracle
3.建目录 dbms_logmnr_d.build;
	desc dbms_output;  desc dbms_logmnr; 重启后没有可start C:\oracle\product\10.2.0\db_1\RDBMS\ADMIN\catproc.sql 运行脚本
	desc dbms_logmnr_d;
	如对表改数据，select * from V$log看当前日志
	execute dbms_logmnr_d.build('abs.ora','C:\oracle\product\10.2.0\admin\orcl\cdump');路径是init文件中的utl_file_dir 的路径
	
	执行后在目录下生成了文件
4. 添加日志文件到目录(被分析的)
	execute dbms_logmnr.add_logfile('..当前的日志文件的路径用V$log查看',dbms_logmnr.new);
	execute dbms_logmnr.add_logfile('C:\oracle\product\10.2.0\oradata\orcl\redoaa.log',dbms_logmnr.new);
	删除是：dbms_logmnr.remove_logfile
5.启动 logmnr
	execute dbms_logmnr.start_logmnr(dictfilename=>'C:\oracle\product\10.2.0\admin\orcl\cdump\abs.ora刚才建的cdump目录下的文件位置'
	dbms_logmnr.start_logmnr
6. select * from V$logmnr_contents;
		两个列：sqlredo/sqlundo


看命令desc dbms_logmnr
	desc V$logmnr_contents;
	 select * from V$logmnr_contents(v$logmnr_dictionary,V$logmnr_parameters， V$logmnr_logs);
	 execute dbms_logmnr.end_logmnr;



--------8 表空间和数据文件
逻辑上的database->tablespace->segment->extent->oracle block都是一对多的关系
		segment只有四类:table/index/temp/rollback只有这四类可申请占用存储空间
		extent  分配是块的整数倍
		oracle  以块(block)存数据；
物理上的Data file,OS block(tablespace->data file;datafile->extent;datafile-os block;Oracle block->os block);
一个tablespace可放在多个datafile
一个segment 必须放在一个tablespace  但可放不同的datafile里 
extent 不可放不同的datatfile
select * from V$tablespace;

select t1.name,t2.name from V$tablespace t1,V$datafile t2 where t1.ts#=t2.ts#;查看表空间对应的文件位置
ts#表空间号

alter tablespace users add datafile '....' size 10m 单位可以是k　可以向表空间中加多个数据文件
system 表空间
select * from dba_rollback_segs;系统回滚段在系统表空间里


把只读的表，和读写的数据分开，只备份读写

alter user hr quota 10m on users;限制用户hr在users的表空间上只有10m的使用空间

create tablespace xx [datafile '...' ]OMF
[size kb/mb]
[extent management local/dictionary]如有local 则 default storage　不可使用
[default storage(xxx) intitial/next/pcincrease/min extents...]

create tablespace mytablespace datafile 'C:\oracle\product\10.2.0\oradata\table.dbf' size 10m autoextend on next 5m maxsize 200m;





create tablespace kong datafile 
'C:\oracle\product\10.2.0\db_1\database\talbe.dbf' size 5m
－－extent management dictionary--这句话不要是可以执行的(默认的)
default storage
	(initial 100k 
		next 100k 
		pctincrease 10)--增量10%
offline--脱机

********没有试过：可以设 块大小，要先设块的(像db_block_buffers)





本地管理的自动分配(autoallocate)或统一大小(uniform)

create tablespace kong datafile 'C:\oracle\product\10.2.0\db_1\database\talbe.dbf' size 10m
extent management local uniform size 1m
 
 默认是字典管理dictionary 推荐用Local(用bit value 表示是自由的还是使用的)
create tablespace kong1 datafile 'C:\oracle\product\10.2.0\db_1\database\talbe1.dbf' size 10m
extent management local autoallocate;

ALTER TABLESPACE kong1 NOLOGGING;LOGGING　只对local用效,并且只可改这个


create tablespace test1 datafile  'C:\oracle\product\10.2.0\db_1\database\test1.dbf' size 10m
－－extent management dictionary－－去掉这行是可以的
default storage(initial 1m next 1m);
	因system空间是local所以不行，只能安装时指定－－网上的


*****alter tablespace userdata minimum extent 24m;    对dictionary
******alter tablespace xx  default storage(initial 1m next 1m maxextents 999 pctincrease 20);    对dictionary

数据字典管理的修改为加文件
ALTER TABLESPACE test03 
    ADD DATAFILE 'C:\oraclexe\app\oracle\product\10.2.0\server\database\tablett.dbf'
    SIZE 40m
    AUTOEXTEND ON
    NEXT 1m
    MAXSIZE 100m;



Local不可改变信息


undo tablespace 只可放undo segments，不可放表

create undo tablespace undo1 datafile 'C:\oracle\product\10.2.0\db_1\database\testundo.dbf' size 10m;
create undo tablespace undo2 datafile 'C:\oracle\product\10.2.0\db_1\database\testundo2.dbf' size 10m extent management local;只可local管理

只可指定datafile,extent management
show parameter undo;的undo_management 的值是AUTO
		      undo_tablespace 为了auto管理设的表空间
create table tt (id int ) tablespace undo1;不可在undo tablespace 放其它对象(其它table space 是可以的)

create temporary tablespace temp11 tempfile 'C:\oracle\product\10.2.0\db_1\database\testtemp.dbf' size 40m extent management local uniform size 10m;建议local 管理


也不可把表放在temporary tablspace 中
create database时的表空间是local管理的

建用户时，不指tempoarary tablespace 会找system 表空间，会使用default tempoaray tablespace;

alter  database  default temporary tablespace temp11;
alter tablespace temp11 offline ;alter tablespace temp11 read only;是不可的,也不可变成永久的
drop tablespace temp11 不可
不可删除(drop)，offline,read onley 默认临时表空间；

active 的undo 不可offline

alter tablespace kong online;

system 的表空间/default temporay tablespace/ 不可offline
alter tablespace kong read only; 是可以删除表的
alter tablespace kong read write;


drop tablespace userdata including contents and datafiles;

alter database datafile 'C:\oracle\product\10.2.0\db_1\database\talbe.dbf'   autoextend on next 1m maxsize 50m
文件是已存在的并是前面建过的

alter database datafile 'C:\oracle\product\10.2.0\db_1\database\talbe.dbf'   resize 50m;可大也可小
alter tablespace kong add datafile 'C:\oracle\product\10.2.0\db_1\database\add.dbf' size 20m;

先offline
alter tablespace kong rename datafile '   ' to  '   ';要tablespace 必须offline 目标数据必须存在，只是改控制文件内容，不会改物理文件
alter tablespace kong rename datafile 'C:\oracle\product\10.2.0\db_1\database\add.dbf' to 'C:\oracle\product\10.2.0\db_1\database\add01.dbf';

如系统表空间：要shutdown,startup mount,目标必须存在
alter database rename file 'C:\oracle\product\10.2.0\oradata\orcl\system01.dbf' to 'C:\oracle\product\10.2.0\oradata\orcl\system02.dbf';

select t1.name,t2.name from V$tablespace t1,V$datafile t2 where t1.ts#=t2.ts#;查看表空间对应的文件位置

OMF：alter system set db_create_file_dest='...';
create tablespace xxx datafile size 20m;默认100m;


tablespace信息用：dba_tablespaces,V$tablesapce
datafile 信息用：dba_data_files,V$datafile
temp file 信息用：dba_temp_files,V$tempfile


10g新的：create temporary tablespace temp01 tempfile 'C:\oracle\product\10.2.0\oradata\test\temp.dbs' size 40m
			tablespace group tempgroup_a;
		select * from dba_tablespace_groups;可以查看到
		alter tablespace temp01 tablespace group tempgroup_a;
		alter user hr temporary tablespace tempgroup_a;
--------------9

 temporay segment 用来sort
bootstrap segment用来初始化实例，创建数据库时自动产生，是oracle 启动时用的。，用完后自动消失，不用去管理
＝＝＝create tablespace xx datafile '' size 10m default storage(...);
create table hr.aa (id int )tablespace users storage(initial 100k next 10k)

如未指定storage ,会使用，(最高优先级)segment,tablepace ,oracle default
create tablespace kong datafile 'C:\oracle\product\10.2.0\oradata\test\kong.dbf'  size 10m;
create table xx (id int )tablespace kong;

alter table xx allocate extent (size 1m datafile 'C:\oracle\product\10.2.0\oradata\test\kong.dbf');
段 可以跨datafile 但不可以跨tablespace,文件只能是同表空的文件
truncate table xx;再drop table xx;表空间释放

alter table xx deallocate unused;keep 2m/k　　释放没用到的表空间

create tablespace  kong3 datafile 'C:\oracle\product\10.2.0\oradata\test\kong3.dbf' size 10m blocksize 10k 
提示无效

查show parameter db_ 没有指定db_8k_cache_size ,db_cache_size默认块大小
alter system set db_cache_size =10m;　是默认的块小 ，大于db_8k_cache_size
alter system set db_8k_cache_size=10m;(****不可以修改_8k_)

 create tablespace my datafile  'C:\oraclexe\app\oracle\product\10.2.0\server\database\my.dbf' size 10m blocksize 8k;
 8k 表示使用前面定义的db_8k_cache_size;(只有定义了db_8k_cache_size才可以用 blocksize 8k)

block结构：header (从上到下) ,free,data(从下向上)

initrans (1),maxtrans(255),pcfreee,pctused


（block）用bitmap来跟踪是使用的还是空闲的

自动：create tablespace data02 datafile 'C:\oracle\product\10.2.0\oradata\test\mytablespace1.dbf' size 5m extent management local uniform size 64k segment space management auto;


手工：pctfree,pctused,freelist
pctfree =20(即是20%)是为block留的自由空间，而pcused是最少使用的空间,如果空间到了指定的值，表示可以在使用

自动管理的段，不可用Blobs 

select * from dba_Extents;
	select sum(bytes) from dba_extents where owner='kong' and segment_name='authors'  表名所占用的空间
select * from dba_free_space;
select * from dba_tablepaces;
select * from dba_segments;
dba_data_files;

dba_tablespaces 
		1.dba_segments     dba_extendsts  逻辑
		2.dba_datafiles    dba_free_space 物理  数据文件的自由空间


-------------------10
show parameter undo 显示undo_management是auto

show user;
undo 不可脏读
system undo放在system表空间

手工：private(只可一个instance操作),pulbic 


alter tablespace users offline normal;(temporary 做checkpoing不能确定写入
alter tablespace users offline immediate;(不做checkpoint在online进要recovery) ，

恢复数据文件：recover datafile 'C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\USERS01.DBF' ;


alter tablespace users offline temporary;

 select * from dba_rollback_segs;
undo_management
show parameter undo_talbespace 可有多个(至少一个)，同一时刻只有一个可以激活，多个undo 段只可能来自一个unod tablespace
改init 文件，undo_management=AUTO为manual(不可aler system ,session)

alter system set undo_tablespace =undo01(是一个undo tablespace)
select * from dba_rollback_segs; (仍用rollback)以前叫rollbck，现在叫undo

alter tablespace undo01 add datafile '..' size 2m --或加autoextend on;
在create database 时undo tablespace undo01 datafile '' size 20m autoextend on

一个instance 中能指定一个undo tablespace
被使用的不可删（不可offline ,read only）

create undo tablespace 时，可reuse
如删使用的 undo tablespace 1.要等事务完成，2.设alter system set undo_tablespace =另一个，再删

******－－ 我的无show parameter undo_suppress_errors;是false 即会报错；

undo_retention,把commit;的数据，再找回，并在undo 中的保留时间

desc dbms_flashback; 
execute dbms_flashback.enable_at_time('26-JAN-04:12:17:00 pm')；不可以sys用户使用（sys用户删除其它用户的表，其它用户来执行）
alter session set nls_language=american
找回指定时间以后所做的修改 只是查看内容，并没有恢复
execute dbms_flashback.disable;再查就没有了
execute dbms_flashback.enale_at_time('07-JUN-07:01:20:00 pm')

 select * from V$undostat

V$rollname
V$rollstat
V$undostat
V$session
V$transactions 

手工管理： V$transactions 的transactions，transactions_per_rollback_segment

		show paramter rollback的,rollback_segments
		private 和 public 
startup	pfile= 	改init文件，undo_management=manual;不可以delete table01 ;因没有回滚段
		create rollback segment xx tablesapce yy;(要是undo tablespace )可以是其它新建的undo tablespace

			show parameter rollback;
		create public rollback segment xx tablespace yy; 默认是offline;
		###如有错误可以使用system表空间
		alter rollback segment xx online;
		 
要改init文件，加rollback_segments=('','')放已经建立的私有rollback segment的名字， 会在启动时自动online(也可以是public 的)
																		也可是不同的undo tablespace,一个参数即可是'.,.' 也可是('...')
				下两个参数来计算需要的回滚段的数目，如比上设的数目少会找公共回段，如还是不足，则仍然可以启动，有可能发生数据争用的问题
				transactions=100
				transactions_per_rollback_segs=10


------
 nchar,nvarchar 来支持unicode

char  最大2000
varchar2()最大4000
lob (larg object)
blob,clob

rowid 行的 id 
desc dbms_rowid
select rowid ,dbms_rowid.rowid_block_number(rowid),dbms_rowid.rowid_row_number(rowid)//行数

create table tt( dd date default sysdate);

create global temporary table hr.temp as select * from hr.employees


create global temporary table hr.temp  on commit delete rows as select * from hr.employees
	select 时无值
create global temporary table hr.temp  on commit preserve rows as select * from hr.employees
	select  时有值
on commit delete rows
on commit preserve rows临时表与普通表的不同的是ＤＭＬ不做日志，不上锁
只是在一个事务或者一个会话中存放数据
建议在建表时 pctincrease 0 

做ＤＭＬ不做日志，不上锁

create table test2(id int)tablespace users storage(
	initial 100k next 100k pctincrease 0 minextents 1 maxextents 10) pctfree 10 pctused 40 
可以把storage放最后,但是不可以修改storage参数
或 alter table test2 pctfree 20 pctused 30




一个表放在多个数据文件（同一表空间上的）上可提高性能




alter table hr.employees allocate extent (size 500k datafile ' 当前表空间文件之一');

 默认表删记录不会回收空间

alter table hr.employees deallocate unused   释放未使用的表空间
alter table hr.employees deallocate unused　keep 2k;

高水印 high water mark 是一张表的数据最多的时候，不断的删除数据，其它表不会使用到这段空间
要释放    alter table hr.employees deallocate unused   释放未使用的表空间

可是我的不用rebuild index在移动表空间时

alter table hr.employees move tablespace users;
create index test1 on hr.employees(first_name) tablespace example;
移动后保存index，  但在另还空间上建index ( create index xx tablespace xxx),再移回来，是不可用，要重建
alter index xx rebuild;
truncate 是ＤＤＬ　会清空间

drop table hr.employees cascade constraints;

1.alter table hr.departments  drop  column department_name  cascade constraints checkpoint 1000
2.alter table hr.departments set unused column xx cascade constraints;
	alter table hr.departments drop unused columns checkpoint 1000
	alter table hr.departments drop column  continue checkpoint 1000(对删除过程中的停止)

 select * from DBA_tables,
 select * from dba_objects;


-----------12 index
b-tree(非叶，叶)　默认的

bitmap index 对取值很少的如性别；
creat bitmap index xx on table(field)
where 中用or 不会用index ,但　bitmap是用的


create index xx on hr.employees(last_name) pctfree 30 storage(initial 200k next 200k pctincrease 0 maxextent 50 )tablespace indx
	不可用pctused
(nologging) 不做日志
建议index 在不同tablespace　，用uniform extent size

index 的initrans不可小于table的initrans

show parameter create_bitmap_area_size;(bitmap index )

reverse在最后
alter index xx pctfree 20 storage( next 200k pctincrease 20)
//pctfree在整个alter index  中不可以改变

alter index xx allocate extent(size 20k datafile '...同一表空间的文件');
alter index xx deallocate unused;

alter index xx rebuild tablespace xx;

b-tree与reverse转换　alter index xx rebuild reverse(online)
alter index xx coalesce;整理碎片

analyze index xx validate structure;对表index_stats更新,desc index_stats
select * from user_indexes
alter index xxx monitoring usage;
alter index xx nomoonitoring usage;

dba_indexes,dba_ind_columns,dba_ind_expressions,V$object_usage
all_indexes

函数索引：
create index xx on orers(oraderdate-requireddate);表达式

select ....where (oraderdate-requireddate)>....

---------13
alter table xx add constraint ud_xx unique(colmun1,coumun2) 

alter table xx add constraint ud_xx primary ey (colmun1,coumun2) 
alter table xx add constraint ud_xx foreign key (colmun) references table(column)
alter table xx add constraint ud_xx check (colmun like 'B%') enable novalidate;表示对已有的数据不做验证，对以后的数据进行验证

disable novalidate　新加的也不验证
disable validate    (validate表示对已有的数据验证，disable 表示以新加数据不做验证)
enable novalidate  启用，新加的不验证
disable validate

alter table add constraint xx check(pqt>10) initially deferred
alter table modify constraint xx initially deferred; (延期的)如违反约束的插入和修改是可以的，但commit；时报错
alter table modify constraint xx initially immediate;修改时直接验证　默认的
alter session set constraints= deferred (immediate)

drop table xx cascade constraints
alter table disable constraint xxx

create table employee_temp(id number(7) constraint aa_id primary key deferrable using index storage(initial 100k next 100k) tablespace ts_temp,
last_name varchar(24) constraint xx not null ) tablespace users;
表和索引放在不同的表空间，使用bulk是建议用非唯一索引  外键为空
主键和唯一约束必须使用非唯一索引
alter table xx enable novalidate constraint xx

alter table xx add constraint xx check(..) enable validate exceptions into exeptions
对约束违反的记录插到
运行　utlexpt1.sql 脚本建表exceptions
select * from user_constraints;
select * from user_cons_columns;



--------------------14
alter user xx account lock;unlock
alter user xx password expire;
概要文件
create profile profile2 limit
 failed_login_attempts 3  password_lock_time 1/1440(或unlimited) 如时间会自动解锁;--单位是天
或
create profile profile2 limit 
   password_lock_time 1/1440 failed_login_attempts 3;--３次后再lock帐户

alter user kong profile profile1;
不设定相关参数是继承自 一个default的profile
alter profile profile1 limit password_life_time 5（单位是天)　--过期后的存活期
		password_grace_time 3 --第一次登录的过期时间


password_reuse_time  多少天以前的密码可以使用
--或是password_reuse_max(只可其中的一个指其中一个另一个变unlimited,可以修改)
－－口令最多可以重复使用的次数

alter profile profile1 limit password_reuse_time 3 password_reuse_max 3　显示的更改是假的
unlimited
用验证函数:(PASSWORD_VERIFY_FUNCTION)
	1.必须是sys登录　，必须是以下的格式
	2.返回boolean值
	function_name(
		userid_parameter in varchar2(30),
		password_parameter in varchar2(30),
		old_password_parameter in varchar2(30))
	return boolean 

运行utlpwdmg.sql(mg，manager)会有verify_function并会修改default的profile
最小4个，不可与名同，一个字母,一个数字,一个特殊,与以前密码有３个以上的不同字符

alter user kong profile default;
直接password可以改当前用户口令

create profile gg limit password_verify_function verify_function;
drop profile xx (cascade)如指定了用户要用cascade　用户的profile 变成了default 
	但是在当前的会话中不起作用,   新建的profile要下一次会话才有效


资源限制:
alter system set resource_limit=true;默认是false，或设init 文件

cpu_per_session(单位是百分之一秒,第个session使用cpu的时间),
sessions_per_user(一个用户可同时连接多少次),
connect_time(单位是分钟),
idle_time(分钟)
logical_reads_per_session(多少块),
private_sga(可用k,m)
cpu_per_call(百分之一秒每个call)
logical_reads_per_call
alter profile gg limit cpu_per_session 10000 connect_time 60 idle_time　5 

desc dbms_resource_manager(需要***我的无此amdinister_resource_manager权限)
select * from dba_users;  有列expiry_date ,profile, lock_time,account_satus,profile哪个profile
select * from dba_profiles; 有limit

-------------15
create user xx indentified by xx

grant connect ,crate table to xx
alter user xx account lock;
alter user xx quota 10m on users;(表空间)没有权限users, quota

schema是一个命名对象的集合(表,索引,视力,触发器...)

schema等于userid(username),userid不等于schema(对于一些用户没有数据库对象的)
如一些用户没有数据对象不称为schema
用户定义的数据类型
desc dba_users; 在用户建表没有指定表空间,会使用当前用户的默认的表空间,即dba_users中的default_tablespace(用户级)

select username ,default_tablespace from dba_users;
select * from all_users;
临时表空间用来排序

create user c##myuser identified by user_001   ---从12c开始用户前必须加c##,或C##
	default tablespace users
	temporary tablespace temp11
	quota 14m on users
	password expire;
是没有顺序的（在identitfied by 后面的）

--(系统级),未对用用户指定临时表空，会使用alter database default( temporary) tablespace temp 对应temporary

show parameter tablespace

create user xx identified externally --用操作系统用户验证　连接时用conn / 就可以拉*********我的无效

在数据库里默认前缀是ops$(****我的无值),如操作系统是kong,而在oracle里是ops$kong要创建的用户
show parameter os_authent_prefix 是ops$

全局验证，安全服务器(global验证也是只检测是否是合法用户，password由oraclesecurity server验证。)
CREATE USER scott IDENTIFIED GLOBALLY AS "CN=scott,OU=divisional,O=sybex,C=US"

alter user testuser2 quota 5m on users;临时表空间不可设quota，undo表空间不可设quota
如quota 0为不可使用表空间，对已有的数据不变，不可加新的数据　

 drop user xx 正在连接在用户不可删除
如用户有数据对象要，加cascade
dba_usrs; dba_ts_quotas;在表空间上的配额
------------------------16
dba不可把其它人表权限给第三人,只可owner(除with grant option) 
any表示在任何的 schema都有权限

create table 权限　(如当前用不是hr)，不可用create table hr.xx
crate any table 权限，可以把表给其它用户，可以create table hr.xx，但不可grant create table to xx

grant create session to xx

无truncate (any)table权限(隐在drop any table权限中);
无create index权限 ,但有create any index权限 
　
unlimited tablespace 权限，create session ,alter session ,restricted session
grant create session,create table to hr,public with admin option;(系统权限)　可以把此权限给其它人
public 角色，是对所有的用户
sysdba, 有recover database until,alter database begin/end backup,restrict session
sysoper,有recover database
grant sysdba to xxx;(sysoper)　。grant update(name) on myuser.my to hr;
select * from V$pwdfile_users 显示所有的sysdba用户

有select any table 权限，默认是不可访问系统表 select * from sys.aud$;审计表
show parameter o7(字母o); O7_DICTIONARY_ACCESSIBILITY为false 
alter system set O7_DICTIONARY_ACCESSIBILITY=true scope=spfile 下次启动生效(对不可以修改的)

revoke create session,create table from  hr,public

grant execute on xx(存储过程或是包名) to hr　with grant option  (对象权限(with grant option)只有owner可以)
grant update(column..)... on xx表 to ....　－－grant update(name) on myuser.my to hr;
(默认)sys 不可把其它用户对象的权限给其它人，grant select ,update on kong.auhoers to hr;不可
grant select 不可指列
revoke 会级联
revoke select on 表 from sys;

dba_sys_privs;　如ceate xx,sys权限 给用户
session_privs;表记录我自己的权限
dba_tab_privs ,user_tab_privs
dba_col_privs,列的权限

审计默认是关闭的,不可记录数值,记录发了哪些语句
show parameter audit_trail NONE/DB/OS  要修初始参数文件
audit select on kong.authors by session;(access)审计
	by session 是在一个session 中发相同的命令只记录一次，
	by access  是发一次命令记录一次
	whenever sucessful/not successful只记录成功的/失败的
audit update on myuser.my whenever successful;
audit table(语句); audit create any trigger; audit select on hr.emp(对象)

desc dbms_fga;(package) 
noaudit select on kong.authors;
all_def_audit_opts,默认的
dba_stmt_audit_opts,statement词句
dba_priv_audit_opts,权限
dba_obj_audit_opts，对象

dba_audit_trail 对已audit select on myuser.my;进行记录在表内
dba_audit_exists
dba_audit_object
dba_audit_session
dba_audit_statement

----------------17
select * from dba_roles; 所有的role, 有系统自带的role 如connect ,resource
select * from role_sys_privs; 哪些系统权限给了 role  (role有哪些系统权限)
select * from role_tab_privs;对象权限给了role (role有哪些对象权限)
set role xxx 激活role 使之生效 (当前用户的role重新加了新的权限要激活一次才生效)
drop role xxx cascade********;
create role xx identified by xx
create role xx identified exernally(global)
alter role xx identified by xx ;再 set role xx identified by xx才可以激活

系统定义的角色，connect ,resource ,dba
	exp_full_database,imp_full_database(对imp和exp) 如exp hr/hr@orcl file=c:\ss.txt
	delecte_catalog_role,execute_catalog_role,select_catalog_role; 对数据字典表

set serveroutput on;
select * from ts$;
alter role xx not identified;不要密码
grant upate(col,col2) on table01 to role01; 
可以把role 给另一个role，但不可循环指定role,不可直接或间接给自己,
grant role1 to role1 with admin option(--role不可是 with grant option,不能把对象权限让role级联)
对象权限和系统权限不可一性给 role,要分开给

所有的role没有Schema,没有拥用者,谁都可以用role但要权限create ,grant ...

alter user xx default role role1,role2 ,[all],[all except role3,role3]
(create user不可以)
alter user xxx default role none;
 
 create role admin_role indentified using hr.employees( sys.dbms_output)
set role xx identifed by passxxx;对要密码的role
default role 会自动激活,如有口令，也不要验证

set role role1 [identified by  xx],role2,xxx, [none] [all]|[all except role1,role2....]
dba_roles,  所有的role
dba_role_privs,哪些role有哪些权限
role_role_privs 哪些 role给其它 role
dba_sys_privs
role_sys_privs
role_tab_privs
session_roles  当前sesion有哪些role

--------------18
SELECT * FROM v$nls_valid_values; 显示所有有效的，可以设置的值
nchar,nvarchar2,nclob 

1.初始参数是Server端的
2.环境变量,Client端的
3.Session(ALTER SESSION ),Client端的
Unicode,(AL32UFT8,AL16UTF16,UTF8)
1。数据库字符集:可以存变长的 char ,varchar2,clob,long (用操作系统)
2。国际字符集:可以unicode编码, nchar,nvarchar2,nclob( 可以用UTF8或AL16UTF16)
建数据库时没有指定国际字符集时，AL16UTF16是默认的

数据库的字符集是依赖OS
select parameter ,value from nls_database_parameters;
   NLS_NCHAR_CHARACTERSET  的值是   国际    AL16UTF16(是默认的)
   NLS_characterset     是字符集  AL32UTF8（ZHS16GBK）
SELECT * FROM v$nls_valid_values where parameter='CHARACTERSET';//LANGUAGE


国际字符集
1.AL16UTF16 ；固定长度，两字节，性能上有优势
2.UTF8 ；可变宽度，在1－3个字节间，空间上的优势


windows 的客户端对utF-16支持更好
初始参数－>环境变量－>alter session命令(优先级 )

alter session set nls_language='SIMPLIFIED CHINESE' ;要大写 引号可有可无 要设过'AMERICAN'  才生效,而且没有在cmd 中set ...

cmd 中set nls_lang=AMERICAN_AMERICA.US7ASCII(7位的ASCII) 再sqlplus 登录时是英文的--OK的
					语言_区域.字符集
	(LINUX 中NLS_LANG要大写，对的)


select parameter,value from nls_instance_parameters;
	改	NLS_LANGUAGE 和NLS_TERRITORY(领土) 会影响其它参数
NLS_LANGUAG	1.月和日的显示
			2.显示消息的语言
			3.A.M,P.M的符号
			4.默认的排序语言
NLS_TERRITORY 
			1.日和周的数字
			2.日期格式,小数字符,分隔符,本地货币符号


		NLS_LANGUAGE====NLS_CURRENCY列 ,NLS_DATE_LANGUAGE列
查select * from nls_session_parameters; 和show paramter nls_可能有不一样的
					


alter session set NLS_TERRITORY=CHINA; 会响货币符号 ,可有可无引号,可以小写
alter session set nls_currency='$';


nls_language			==american
	nls_data_language	==american
	nls_sort			==binary
nls_territory			 == american
	nls_currenty		==$
	nls_iso_currency	==america
	nls_date_format		==DD-Mon_rr
	nls_numeric_characters== ,.


alter session set nls_date_format='yyyy.mm.dd';可大写
alter session set nls_date_format='YYYY-mm-dd  HH24:mi:ss';//大小无关

alter session set nls_timestamp_format='YYYY-mm-dd HH24:mi:ss' 

select to_char(sysdate,'YYYY-MM-DD HH24:mi:ss')  from dual;

客户与服务端不同会自动转换
cmd 中 set nls_date_format='yyyy=mm=dd' ********不可用，
服务器在create database 时character set ...national character set ...
客户 nls_lang=<language>_<territory>.<charset> , nls_nchar=<ncharset>  注意oracle是UTF8  不是UTF-8 (solaris是LANG=en_US.UTF-8,linux是en_US.utf8)

execute dbms_session.set_nls('NLS_DATE_FORMAT','''DD.MM.YYYY'''); 三个引号

execute dbms_session.set_nls('nls_date_format','''dd.mm.yyyy''');
 alter session set nls_sort='binary';

select to_char(sysdate,'dd.mon.yyyy','NLS_Date_language=french')from dual;
TRADITIONAL CHINESE  
***无效的select to_char(sysdate,'dd.mon.yyyy','NLS_Date_language=simplified chinese')from dual;
create index xx on hr.departments (nlssort(manager_id,'NLS_SORT=French_M'));
select to_char(salary,'9G999D99','NLS_numeric_characters='',.''') from employees where employee_id=150;
				nls_database_parameters;
				nls_instance_parameter;
select * from   nls_session_parameters;是继承自操作系统的设置

select * from V$nls_valid_values;设置sort,characterset，territory,language可以取的值

select * from V$nls_valid_values where parameter='CHARACTERSET' and value like '%UTF8%'; //10g 中有UTF8字符集

select * from V$nls_parameters;(session级的) 和select * from nls_session_parameters;很相似


UPDATE sys.PROPS$ SET VALUE$=‘SIMPLIFIED CHINESE’  WHERE NAME=‘NLS_LANGUAGE’;  还不行的

dba_source 看源码
 
 
 
 