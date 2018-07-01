---------------oracle 1

sqlplus>!  回到命令行
Server process 启动时,分配 PAG
ipcs  命令有 Shared Memory 
show sga;

最大值 sag_max_size

SGA动态变化,单位是 granule   小粒, 颗粒, 细粒 
	如SGA小于128M,granule是4M
	如SGA大于128M,granule是16M


select component,granule_size from v$sga_dynamic_components;

db_cache_size
log_buffer
shared_pool_size  
large_pool_size
java_pool_size



11gR1 文档->Supporting Documentation->Reference ->Initailiazation Parameter 来查文档

指定了sag_target和sga_max_size 后其它的内存参数自动设置


shared_pool_size 
	Libary_chache,	最近解析后的SQL ,PLSQL解析结果 ,LRU(Least Recently Used) 算法
	data dictionary V$xx视图, 用户权限,数据文件,表,索引...
alter system set shared_pool_size =64m;

db_block_size
 (Database Buffer cache 最近的数据,对应数据文件,)
db_cache_size 
db_keep_cache_size
db_recycle_cache_size

alter system set db_cache_size=96m;


db_cache_advice   会收集信息,用V$db_cache_advice 来查询收集信息


log_buffer (Redo Log Buffer,对应重做日志文件,每一条记录叫redo entries)

large_pool_size  rman使用它 
parallel_automatic_tuning=TRUE
java_pool_size


一个Server Process ,开一个PAG

DBWn 负责写Database Buffer 到Datafile
LogW
Arc0
写日志先,CHKPT会在数据文件,控制文件头写,
PMON 是监控其它进程有没有死
SMON 是系统恢复,清内存

一些连续的block组成了一个extends-->segments>tablespace

 一个 extends只能在一个datafile中

一个tablespace,segments可由多个datafile组成



./runInstall -resonsefile myrespfile(自备文件 安装目录有范例文件response目录) -silent
可无界面安装


手工安装oracle时不指定口令
sys的默认口令是 chang_on_install
system 的默认口令是 manager


select * from v$parameter 初始参数的表
show paramter nls_


col name format a20


linux  下Oracle  用! 可以切换到SHELL环境


dbs(linux) database(window) 目录下有pfile(过老是8i中的) 只是startup时去读它

pfile  的值 db_name 要修改成SID的值 

oradata/$SID/*.dbf      //*/


spfile 保存在服务器端

create spfile from pfile

create spfile '...' from pfile '...'


strings spfileSID.ora | more  linux来看spfile文件内容

*.db_block_size=4096  * 来代表任一个SID


alter system set xx=xxx comment ='注释' deferred  scope=memory|spfile|both sid='sid|*'  来修改spfile的值

scope默认是both

comment ='注释'  存在V$parameter 表的update_comment 字段中
deferred 等下一次启才生效   v$parameter 表的issys_modifiable列='DEFERRED'

文件名一定要  spfileSID.ora  pfileSID.ora

alter system set(reset) undo_tablesspace=undo2;
alter system reset undo_tablesspace (scope=both) sid=*;   ----sid=*不可以省




startup  找文件顺序
	1.spfileSID.ora
	2.spfile.ora
	3.initSID.ora
	4.init.ora


startup pfile='pflieSID.ora'  ### startup 不可以和spfile使用




pfile 文件中可以引用spfile (spfile=文件路径)



alter database mount  控件文件被打开 


startup force | restrict|nomount|migate|mount|quiet

alter dadabase open read only


startup restrict  只有 restricted session 权限的用户可以连

alter system enable restricted session;


lsnrctl start 启动监听


v$session表的
select username,sid,serial# from v$sesion;

alter system kill session 'sid的值,serial#的值'   ###########


v$transaction;存还没有commit;的事务
/ 上一次SQL


background_dump_dest的位置有alter_SID.log  里有pfile的参数  注意路经要加''  control_files=('..','..',)
在用户执行错误user_dump_dest    max_dump_file_size


session级
	alter session set sql_trace=true;(show parameter sql_trace)
	exec dbms_system.set_sql_trace_in_session(SID,SERIAL#,TRUE);  --SID,SERIAL#是v$session表中的


Instanace 级
	init参数sql_trace=TRUE  ##文件会很大



--------------4
tnsnames.ora 可以使用TCPS

show user

dba_user

orapwd file=.. password=.. entries=5(最多DBA的数量)

remort_login_password=exclusive  排外的,独占的


安装Xmanager 可以远程调用dbca来显示界面

   
SHELL脚本,设置环境变量

. ./sh.sh  多加一个点会修改变量


推荐用spfile,如是init.ora,没有指定control_files,在create database 时,会把control_files的参数写入spfile而不会写入init.ora


vi中 :%s/targe/so/g 把全部的target 替换成so
	:%s/*\.//g  删除*.         ---//*/


create database的undo_tablespace 要和参数文件中的一致





参数 remote_login_passwordfile='EXCLUSIVE' 开启口令文件的验证 

一个数据库软件可以用dbca安装多个DB,并可同时启动(实例)

文档是Administrator''s Guide ->2 Creating and Configuring an Oracle Database
SID最多12个字符,DataBase最多是8

orapwd file=PWDSID.ora password=123 entries=2 
oradim -NEW -SID sid -STARTMODE MANUAL -PFILE pfile    -syspwd pass (只对windows)	 

init.ora中只有db_name=xx 最好建spflie ,因在create database时会把control_files参数写入spfile
用sys连接未启动的oracle create spfile from pfile;
startup nomount 
create database xx;  三大文件创建在dbs目录下




create tablespace  xx logging datafile 'xx.dbf' size 500M autoextend on next 1280k maxsize unlimited extent management local;


@ 或 run
@? 代表ORACLE_HOME的值

@?/rdbms/admin/catalog.sql
@?/rdbms/admin/catproc.sql

每个脚本的功能 文档是在Reference-> SQL Scripts->



Oracle管理文件OMF ,参数db_create_file_dest,db_create_online_log_dest_1

-----------11gR2

db_cache_size=331350016
java_pool_size=4194304
large_pool_size=4194304
#oracle_base='/u01/oracle'#ORACLE_BASE set from environment
pga_aggregate_target=339738624
sga_target=507510784
#shared_io_pool_size=0
shared_pool_size=159383552
streams_pool_size=0
*.audit_file_dest='/u01/oracle/admin/xe/adump'
*.audit_trail='db'
*.compatible='11.2.0.0.0'
*.control_files='/u01/oracle/oradata/xe/control01.ctl','/u01/oracle/oradata/xe/control02.ctl'
*.db_block_size=8192
*.db_domain='localdomain'
*.db_name='xe'
*.db_recovery_file_dest='/u01/oracle/flash_recovery_area'
*.db_recovery_file_dest_size=4039114752
*.diagnostic_dest='/u01/oracle'
*.dispatchers='(PROTOCOL=TCP) (SERVICE=orclXDB)'
*.memory_target=847249408
*.open_cursors=300
*.processes=150
*.remote_login_passwordfile='EXCLUSIVE'
*.undo_tablespace='UNDOTBS1'

-----------11gR2
create database "xe"
maxdatafiles 500
maxinstances 8
maxlogfiles 32
character set "AL32UTF8"
national character set AL16UTF16
archivelog
datafile '/u01/oracle/oradata/xe/system.dbf' size 300m
extent management local
default temporary tablespace temp tempfile '/u01/oracle/oradata/xe/tempts01.dbf' size 100m
extent management local
undo tablespace "UNDOTBS1"
	datafile '/u01/oracle/oradata/xe/undotbs01.dbf' size 100m
sysaux 
	datafile '/u01/oracle/oradata/xe/sysaux.dbf' size 30m
logfile 
	group 1
		(
		'/u01/oracle/oradata/xe/redo01.rdo',
		'/u01/oracle/oradata/xe/redo02.rdo'
		)size 30m,
	group 2
		(
		'/u01/oracle/oradata/xe/redo11.rdo',
		'/u01/oracle/oradata/xe/redo12.rdo'
		)size 30m
;


文件应该autoextend on next  1m maxsize unlimited


11gR2没有pupbld.sql
如果ora-00845 MEMORY_TARGET not supported on this system 那么，同时启动两个
mount -o remount,size=4G /dev/shm

---------------------5

rdbms/admin/sql.bsq (bsp =basic sql)文件是在 create database时自动创建在system表空间的基表

dba_
all_
user_

dictionary 中列出全部的数据字典 ,里面也有V$开头的,但比 V$fixed_table中少几个

dict_columns
dba_tab_columns

GV$ 是global是在RAC 集群中用的

V$parameter　(内存中的）
V$spparameter  (Spfile中的）

V$ 来自内存和控制文件

文档中查reference->

脚本前缀
cat*.sql	catalog and data dictionary information
dbms*.sql	Database package specifications
prvt*.plb	Wrapped database package code  (private)
utl*.sql	Views and tables for database utilites



----------------------------6

启时找SpfileSID.ora ,SPfile.ora,PfileSID.ora
控制文件中有很多entries

mount时只读control_files的第一个文件
DB_CREATE_ONLINE_LOG_DEST_n 是OMF对控制文件的位置




控制文件在写时三个文件都一样, 如果任一个坏了,就不能工作,放在不同的磁盘上

desc V$controlfile_record_section;　控制文件中每个记录(section) 查文档的reference->
administrator guid->Managing Control Files->有create controlfile

select * from dba_free_space
-----------------------------------7

log sequence number (LSN)组里每个成员 ,每换一次文件加１
log switch 可以引发　checkpoint,CKPT进程写控制文件信息

写日志文件的条件
1.每３秒
2.commit;
3.redo log buffer 1/3满
4.redo log buffer 1M
5.在DBW之前

alter system switch logfile;  //强制,LSN+1
alter system checkpoint;	//强制

v$log  可看哪个是当前组

日志文件放两个磁盘上,致少两个组,推荐每个组的成员在不同的磁盘上
同一组中的文件中一样的

一个组只要有一个成员可用,数据库还可以工作

写了一个日志组,再下一个  建议日志组大一点,多一点,(防止一个日志组地switch的时候开DWR(checkpoint),在写一个日志组,返回第一个时(开新的线程写数据),而先前的DWR还没有写完)

fast_start_mttr_target=600   单位秒,数据crash时最多丢失600的,值小写的频率高
可以被 LOG_CHECKPOINT_INTERVAL (操作系统的块数)覆盖

增加组
alter database add logfile group 3 ('xx.dbf','yy.dbf') size 30m;
增加组成员
alter database add logfile memeber 'xxx.dbf' to group 1,'yyy.dbf' to group 2;



一个日志组如果只一个成员是有效的,那么这个成员不可以删除,当前的不可以
V$log;
v$logfile  的status列可有的值invalide,stale  (不新鲜的),不正确

操作数据之间最好做数据库全备份
alter database drop logfile memeber '...' //删组成员 ,只是改了控制文件, 再删物理文件,  

alter database drop logfile group 2;  //删组

1.shutdonw
2. 复制文件到目标,
3.starup mount下
4.alter database rename file 'xxx.log' to 'yy.log'  //目标要是已经存在的

清空日志
alter database clear logflie group 2;
alter database clear unarhived logfile group 2; //不要归档


V$log_history  有thread#列　，主要用在多实例环境下(RAC),多实例下每个有自己的日志组(即线程)

v$instance ,v$database的log_mode列可看归档模式

----------------------8
tune2fs -l /dev/sda1|grep Block //linux查块大小

fsutil fsinfo ntfsinfo C:   //windows 查块大小 (每个簇字节数 :4096=4k)
FAT32格式的区块根据情况不同有不同的大小
	分区为3 GB–7 GB 使用 4 KB的簇
	分区为8 GB–16 GB 使用 8 KB的簇
	分区为16 GB–32 GB 使用 16 KB的簇
	分区大于32 GB 使用 32 KB的簇


文档SQL reference-> CREATE SYNONYM to CREATE TRIGGER->CREATE TABLESPACE

不同和tablespace 可以不用的blocksize
 select * from dba_data_files; 可查表空间对应的数据文件,也可V$tablespace和V$datafile的TS#关联


如果老系统使用dictionary管理的表空间 ,可改

之前要全备份数据库
dbms_space_admin.tablespace_migrate_to_local('system');//从dictionary转换成local




temporary tablespace  (只有一个default全局数据库的,可以用alter database 来改) ,tempfile,
undo tablespace

create temporary tablespace temp02 tempfile 'C:\oracle\oradata\orcl\temp02.dbf' size 500m;
alter database default temporary tablespace temp02;

create undo  tablespace  undo02 datafile 'C:\oracle\oradata\orcl\undo02.dbf' size 200m;

database_properties表中,有DEFAULT_TEMP_TABLESPACE        TEMP ,来查全局的
dba_tablespace;表的CONTENTS列来区分类型

alter tablspace xx read only //可以drop table,不可truncate

extent management local uniform size 1m;//undo表空间不能指定uniform size
		AUTOALLOCATE  //temporay表空间不能指定 autoallocate


create user xx default tablespace yy temporary tablespace zz;

online 
offline normal/immediate/temporary



select user from dual; //当前用户 也可以使用 show user



查表空间使用情况
dba_data_files 的bytes数据文件的大小,  
dba_free_space 的bytes是可用的空间


dba_data_files表的AUTOEXTENSIBLE,MAXBYTES,INCREMENT_BY 看是否自动增长,

alter database vnetgbk datafile '/u01/app/oradata/vnetgbk/dbfile01.dbf' AUTOEXTEND on next  2m  MAXSIZE  UNLIMITED ;

alter database datafile 'xx' resize 200m;  //收回未使用的空间  reclaim收回

dba_temp_files//临时表空间

alter tablespace xx add datafile '' size 20m;

要先offile,复制到目标
alter tablespace xx rename datafile '' to '';
启动到mount
alter database rename file '' to ''

drop tablespace xx including contents and datafiles;

V$tempflie;

-------------------------------------

--SQL执行记录 ELAPSED_TIME /10^6(microseconds)

select b.SQL_TEXT,b.FIRST_LOAD_TIME,b.SQL_FULLTEXT,b.EXECUTIONS,b.DISK_READS,b.ELAPSED_TIME  ,b.CPU_TIME
from v$sqlarea b
where b.FIRST_LOAD_TIME >to_char(SYSDATE-1,'yyyy-mm-dd/hh24:mi:ss')
		AND PARSING_SCHEMA_NAME=UPPER('hr'); --and PARSING_SCHEMA_NAME != UPPER('sys')

--查过去的执行计划,如JDBC
select status, sql_id, sql_child_number from v$session where status='ACTIVE' and ....

select sql_id, child_number, sql_text ,EXECUTIONS from v$sql where  sql_text like '%关键字%';
 
为了获取缓存库中的执行计划， v$sql_plan    ,  v$sql_plan_statistics_all 
也可 select * from table(dbms_xplan.display_cursor('sql_id',child_number)); 

  
-----查哪人在做哪些
Application Server 和Oracle 之前一般用 Dedicate 方式

set transaction 事务属性

select addr form v$trasaction ; //正在运行的事务
commit;后,再查v$trasaction 没有记录

V$sql,V$process,v$session,v$transacction

select username ,status from v$session;

netstat -anp 找1521端口 连接的进程 ID ->V$processs

windows 用netstat -b 二进制的可执行的文件 


V$trasaction的ses_addr是V$sesession的saddr
V$session的paddr是V$process的addr 
V$process的spid 是系统的netstat
V$session的sql_address是V$SQL的address
V$session的pre_sql_address上一次的sql语句

V$session的process值的第一个值(:前的)进程是客户端的进程PID，program客户端程序名sqlplus

-----查哪人在做哪些



------------------DataGuard---------------
官方文档 High Availability->Data Guard->Data Guard Concepts and Administration 

 一个主库primary ,最多可能是9个 备库standby  ,各个库的版本相同
主库把redo log传入备库(物理备库,逻辑备库),备库可提供查询服务

物理备库 (block 对 block的复制)
11g 中,可同读(reporting),和recive and apply redo ,(data protection)

逻辑备库  (把转换的SQL转入备库在执行 ,SQL apply)  可以升级
快照备库  只接收数据


Vmware 装两个Linux  node1(192.168.0.188  primary ),node2(192.168.0.189  standby ),
网卡选择host noly (在本地连接属性->高级标签->选中 允许其它网络用户通过些计算机的Internet连接,下拉中选 VMnet1)

tnsname.ora中有要两个节点的配置, 两个Oracle可以相互连接 ,tnsnames.ora中为lsnode1 和lsnode2 
只安装Oracle软件 ,复制VM文件,修改VM名字,IP,MAC,不用改ORACLE_SID

ORACLE_SID=dgdemo
node1 上dbca建库 ,输入SID:dgdemo,启用归档
 
====物理示例
1.主库上ALTER DATABASE FORCE LOGGING
show parameter spfile 启动使用的文件

--主库上,初始参数文件中加  (示例可参考文档,查全部初始参数 在1 Initialization Parameters 前的箭头可以点开)
	DB_UNIQUE_NAME=unq_node1	
		//(任何名字),service_name 默认是和DB_NAME相同
	LOG_ARCHIVE_CONFIG='DG_CONFIG=(unq_node1,unq_node2)'   
		//DG_CONFIG的是DB_UNIQUE_NAME的值
	LOG_ARCHIVE_DEST_1='LOCATION=USE_DB_RECOVERY_FILE_DEST VALID_FOR=(ALL_LOGFILES,ALL_ROLES)  DB_UNIQUE_NAME=unq_node1'
	LOG_ARCHIVE_DEST_2='SERVICE=lsnode2 ASYNC  VALID_FOR=(ONLINE_LOGFILES,PRIMARY_ROLE)   DB_UNIQUE_NAME=unq_node2'       
		//本地是'LOCATION=  ',远端SERVICE= 是Oracle Net service name 即SERVICE的值
	LOG_ARCHIVE_DEST_STATE_1=ENABLE
	LOG_ARCHIVE_DEST_STATE_2=ENABLE
	
--主库上为备库的初始参数
	FAL_SERVER=lsnode2
	FAL_CLIENT=lsnode1
	DB_FILE_NAME_CONVERT='boston','chicago'
	LOG_FILE_NAME_CONVERT= '/arch1/boston/','/arch1/chicago/','/arch2/boston/','/arch2/chicago/'    
				//是两个不DB的路径转换,如路径相同,就不用了
	STANDBY_FILE_MANAGEMENT=AUTO

create spifle from pfile=''
启用归档 
STARTUP MOUNT;
ALTER DATABASE ARCHIVELOG;

主库上对备库建立控制文件
SQL> STARTUP MOUNT;
SQL> ALTER DATABASE CREATE STANDBY CONTROLFILE AS '/tmp/boston.ctl';  
SQL> ALTER DATABASE OPEN;

把主库上的,刚建的控制文件,参数文件,口令文件,数据文件,日志文件 全部传到备库,刚建的控制文件替换备库原来的控制文件
	OracleInventory 只是记录Oracle 装了哪些东西,

--修改备库的初始参数
	DB_UNIQUE_NAME=unq_node2
	LOG_ARCHIVE_DEST_2='SERVICE=lsnode1 ASYNC  VALID_FOR=(ONLINE_LOGFILES,PRIMARY_ROLE)   DB_UNIQUE_NAME=unq_node1'      
	FAL_SERVER=lsnode1
	FAL_CLIENT=lsnode2
	控制文件要要用主库生成的
	
create spifle from pfile=''
因已复制,备库口令文件的sys用户密码要和主库sys用户的密码一样

为备库建tempfile
备库startup mount
备库 ALTER DATABASE RECOVER MANAGED STANDBY DATABASE  DISCONNECT FROM SESSION; 来更新主库传来的日志文件写入自己的数据文件


验证
主库 ALTER SYSTEM SWITCH LOGFILE 或 ALTER SYSTEM archive log current;
备库SELECT SEQUENCE#, FIRST_TIME, NEXT_TIME,APPLIED  FROM V$ARCHIVED_LOG ORDER BY SEQUENCE#;  从主库来的日志,可以等一会才有 


如对physical备库startup readonly  ,但备库就不更新来自主库的日志文件,虽然文件也传来,但备库不更新的,总是老的数据

备库ALTER DATABASE RECOVER MANAGED STANDBY DATABASE CANCEL;  不向备库更新日志
备库mount下
备库alter database open;//select xxx
ALTER DATABASE RECOVER MANAGED STANDBY DATABASE DISCONNECT from session; //再更新日志 

如主库死了,备库变主
select name,database_role from V$databse;来看是主库还备库

手工主库上执行,变备库 
	alter database commit to switchover to physical standby with session shutdown;
	startup nomount
	alter database mount standby database;
	recover managed standby databsae discconect; //更新日志

手工备库上执行,变主库
	alter database commit to switchover to primary;
 
=================VMware 上Oracle  RAC (Real Application Clusters)
\vm\rac\rac1
\vm\rac\rac2
\vm\sharedstorage

在\vm\rac\rac1建立Oracle Enterprise Linux的虚拟机后
有sharedstorage 下建立四个虚拟 SCSI 硬盘 — ocfs2disk.vmdk (512MB) , asmdisk1.vmdk (3GB), asmdisk2.vmdk (3GB) 和 asmdisk3.vmdk (2GB)
	选择 Allocate all disk space now
	选择 Independent，针对所有共享磁盘选择 Persistent
	建立后advance->修改放在SCSI 1:0,1:1...

加第二块网卡选择host noly ,在linux中配置IP 地址：输入“10.10.10.31”/24(根据自己的网段可以先DHCP抄下IP再设为静态)
(windows下在本地连接属性->高级标签->选中 允许其它网络用户通过些计算机的Internet连接,下拉中选 VMnet1)
1.	选择ocfs-2-2.6.9-42.0.0.0.1ELsmp（SMP 内核驱动程序）。 
2.	选择 ocfs2-tools。 
3.	选择 ocfs2console。 
4.	选择oracleasm-2.6.9-42.0.0.0.1ELsmp（SMP 内核驱动程序）。 

OracleLinux-6.4只有ocfs2-tools-1.8.0,ocfs2-tools-devl-1.8.0,oracleasm-support-2.1.8


VMware Server (不是Workstation).vmx配置文件

disk.locking = "FALSE"
diskLib.dataCacheMaxSize = "0"
scsi1.sharedBus = "virtual"

scsi1:0.deviceType = "disk"		
scsi1:1.deviceType = "disk"
scsi1:2.deviceType = "disk"
scsi1:3.deviceType = "disk"
 
	
如果tar包解压运行vmware-install.pl来安装,安装后运行 vmware-toolbox-cmd timesync enable (status)
##如是rpm安装后运行vmware-config-tools.pl,安装后 vmware-toolbox 工具 , 选中Synchronize Time between virtural machine and your host 
就是在.vmx配置中加 tools.syncTime = "TRUE"	

---------
/boot/grub/grub.conf，并将选项 “clock=pit nosmp noapic nolapic”
	即root=LABEL=/ rhgb quiet clock=pit nosmp noapic nolapic

reboot重启

共享磁盘还存储 Oracle Cluster Registry 和 Voting Disk

集群就绪服务 (CRS) 

环境变量和内核参数
export ORA_CRS_HOME=$ORACLE_BASE/product/10.2.0/crs_1
$ORA_CRS_HOME/bin
ORACLE_SID=devdb1

# vi /etc/hosts
127.0.0.1               localhost
192.168.1.131           rac1				//192.168.1.131是VM中的linux IP
192.168.1.31            rac1-vip
10.10.10.31             rac1-priv			//是第二个内部网卡

192.168.1.132           rac2
192.168.1.32            rac2-vip
10.10.10.32             rac2-priv

##/etc/security/limits.conf 
##/etc/pam.d/login 
##session required /lib64/security/pam_limits.so

# vi /etc/modprobe.conf
options hangcheck-timer hangcheck_tick=30 hangcheck_margin=180


# modprobe -v hangcheck-timer  //要立即加载模块

OCFS2 和ASM分区

#fdisk /dev/sdb
#fdisk /dev/sdc
#fdisk /dev/sdd
#fdisk /dev/sde

m 是help  n-p-1-回车-w

安装软件包
libaio
openmotif
oracleasm-support
oracleasm
oracleasmlib


# vi /etc/sysconfig/rawdevices 
/dev/raw/raw1 /dev/sdc1
/dev/raw/raw2 /dev/sdd1
/dev/raw/raw3 /dev/sde1
 
# /sbin/service rawdevices restart  映射立即生效

# chown oracle:dba /dev/raw/raw*
# chmod 660 /dev/raw/raw*



# su – oracle
rac1->cd oradata/devdb				
rac1->ln -sf /dev/raw/raw1 ./asmdisk1
rac1->ln -sf /dev/raw/raw2 ./asmdisk2
rac1->ln -sf /dev/raw/raw3 ./asmdisk3


/etc/udev/permissions.d/50-udev.permissions
中为原始行“raw/*:root:disk:0660”添加注释，       ,,, */
然后添加一个新行“raw/*:oracle:dba:0660”。        ,,,*/


关闭第一个虚拟机，将 g:\vm\rac1 中的所有文件复制到 \vm\rac2，
改虚拟机名称：输入“rac2
rac1 关闭
启动 rac2 ->选择Create a new identifier
改rac2 的IP 为192.168.1.132
改rac2 Mac 可在图形界面prob 按钮  
还有另一网卡

改不同的ORACLE_SID=devdb2 

hostname改

启动rac1
为 ssh连接其它RAC机器不输口令(等效性) 要  

在 rac1 上执行
	#su - oracle
	rac1->mkdir ~/.ssh
	rac1->chmod 700 ~/.ssh
	rac1->ssh-keygen -t rsa
	rac1->ssh-keygen -t dsa
在 rac2 上再执行相同的
在 rac1 上执行
	rac1->cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
	rac1->cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
	rac1->ssh rac2 cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys   #远程文件加到本地，authorized_keys要四部分
	rac1->ssh rac2 cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
	rac1->scp ~/.ssh/authorized_keys rac2:~/.ssh/authorized_keys
在每个节点上测试连接
	ssh rac1 date
	ssh rac2 date
	ssh rac1-priv date
	ssh rac2-priv date
也要在rac2 上做测试要输入一次yes



# /etc/init.d/oracleasm configure  ->oracle->dba->y->y //rac1和rac2都要做




在任何一个(共享磁盘)创建 ASM 磁盘（root）  sdb1是OCFS2(Oracle Cluster File System)
# /etc/init.d/oracleasm createdisk VOL1 /dev/sdc1
# /etc/init.d/oracleasm createdisk VOL2 /dev/sdd1
# /etc/init.d/oracleasm createdisk VOL3 /dev/sde1
# /etc/init.d/oracleasm scandisks
# /etc/init.d/oracleasm listdisks
VOL1
VOL2
VOL3

要有三个软件包
ocfs2-tools-1.2.2-2
ocfs2console-1.2.2-2
ocfs2-2.6.9-42.0.0.0.1.ELsmp-1.2.3-2


# ocfs2console　　图形界面　(可用NX Client)
Cluster菜单->Configure Nodes->Add
	名称：rac1 
	IP 地址： 192.168.1.131
	IP 端口： 7777 
	
	名称：rac2
	IP 地址： 192.168.1.132 
	IP 端口： 7777
点Apply，　验证生成的配置文件# more /etc/ocfs2/cluster.conf　，可传文件到rac2 上，也可做相同配置
 Cluster-> Propagate Configuration 以将配置文件传播到 rac2

两节点都做
	#/etc/init.d/o2cb unload
	#/etc/init.d/o2cb configure  ->y->回车->61

# /etc/init.d/o2cb status　

RAC1节点上格式化文件系统(只一个) # ocfs2console-> Tasks->Format。
		可用设备：/dev/sdb1 
		卷标：oracle 
		集群大小：Auto 
		节点插槽数量： 4 
		块大小：Auto


两个节点上执行以下命令。 
	# mount -t ocfs2 -o datavolume,nointr /dev/sdb1 /ocfs  手工建
	# vi /etc/fstab
	/dev/sdb1 /ocfs ocfs2 _netdev,datavolume,nointr 0 0
 rac1 上执行 
# mkdir /ocfs/clusterware	在rac2上也能看到，挂载的同一个共享磁盘
# chown -R oracle:dba /ocfs 　　


10g要下载Oracle Clusterware Release 2 (10.2.0.1.0) for Linux x86
安装时要选择 /u01/app/oracle/product/10.2.0/crs_1 不要选择db_1 ,
 Add一个
	public Node Name:rac2
	专用节点名称：rac2-priv 
	虚拟主机名称：rac2-vip
更改一个网卡为public

Oracle Cluster Registry (OCR)
生产环境选择　选择 External Redundancy，测试选择Normal ,
/ocfs/clusterware/ocr
/ocfs/clusterware/votingdisk　(表决磁盘)
安装后root 用户身份按顺序执行
rac1 上执行 /u01/oracle/oraInventory/orainstRoot.sh
rac2 上执行 /u01/oracle/oraInventory/orainstRoot.sh
rac1 上执行 /u01/oracle/product/10.2.0/crs_1/root.sh
rac2 上执行 /u01/oracle/product/10.2.0/crs_1/root.sh

因为rac2上eth0不是public的，会报错，
在rac2上用# /u01/oracle/product/10.2.0/crs_1/bin/vipca　( virtual IP)　来改　也是图形界面不能用NX
选择两个网卡->在列表中输入rac1-vip,其它的自动补全

再dbca安装oracle软件到db_1下->advance安装->cluster,两个rac都选择 ,要使用ASM->全局数据库名称：devdb ,SID 前缀：devdb

磁盘组名称：DG1->Normal  冗余->选raw1,raw2
磁盘组名称:RECOVERYDEST -> External 冗余->选raw3
选择 Specify Flash Recovery Area->RECOVERYDEST

检查
ac1-> crs_stat -t
rac1-> srvctl status nodeapps -n rac1
rac1-> srvctl status nodeapps -n rac2
rac1-> srvctl status asm -n rac1
rac1-> srvctl status asm -n rac2
rac1-> srvctl status service -d devdb

srvctl status database -d devdb





rac1-> crsctl check crs
rac2-> crsctl check crs

SQL> select instance_name,host_name,archiver,thread#,status from gv$instance;

sqlplus system@devdb1
sqlplus system@devdb2
sqlplus system@devdb


SQL> select file_name,bytes/1024/1024 from dba_data_files;

SQL> select group#,member,is_recovery_dest_file from v$logfile order by group#;

SQL> parameter asm_disk
SQL>select group_number,name,allocation_unit_size alloc_unit_size,state,type,total_mb,usable_file_mb from v$asm_diskgroup;
select name,path,header_status,total_mb free_mb,trunc(bytes_read/1024/1024) read_mb,trunc(bytes_written/1024/1024) write_mb from v$asm_disk;

create tablespace test datafile '+DG1' size 10M
select * from v$recovery_file_dest;
 select * from v$flash_recovery_area_usage;

按步骤启动
rac1->srvctl start nodeapps -n rac1
rac1->srvctl start nodeapps -n rac2 
rac1->srvctl start asm -n rac1 
rac1->srvctl start asm -n rac2
rac1->srvctl start database -d devdb 
rac1->srvctl start service -d devdb 
rac1->crs_stat -t
	停止
rac1->srvctl stop service -d devdb
rac1->srvctl stop database -d devdb
rac1->srvctl stop asm -n rac2 
rac1->srvctl stop asm -n rac1 
rac1->srvctl stop nodeapps -n rac2 
rac1->srvctl stop nodeapps -n rac1
rac1->crs_stat –t




----------------------------TAF(Transparent Application Failover) 透明 故障转移Failover--
dbca-> Oracle Real Application Clusters database -> Services Management->ADD一个Service 名字叫CRM ,选择devdb1节点是perferred,devdb2是Available,
	TAF policy：选择 Basic


实际是在 tnsnames.ora 中创建以下 CRM 服务名项：
CRM =
  (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = rac1-vip)(PORT = 1521))
    (ADDRESS = (PROTOCOL = TCP)(HOST = rac2-vip)(PORT = 1521))
    (LOAD_BALANCE = yes)
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = CRM)
      (FAILOVER_MODE =
        (TYPE = SELECT)
        (METHOD = BASIC)
        (RETRIES = 180)
        (DELAY = 5)
      )
    )
  )

rac1 @devdb1和 rac2 @devdb2看不同,在devdb1
show parameter service_names

rac1 @crm的连接方式
select instance_number instance#,instance_name,host_name,status from v$instance; 
	devdb1,rac1
select failover_type,failover_method,failed_over from v$session where username='SYSTEM';

如failed_over,failover_type 是NONE,则tnsname.ora配置不正确
在rac1 @devdb1 中shutdown abort ,即出现故障,现在切换到devdb2 ,rac2
@crm来再查, 也可show parameter service_names
如果rac1恢复了,必须手工指定到rac1,
srvctl relocate  service -d devdb -s crm -i devdb2 -t devdb1  //从devdb2转到devdb1

emctl status dbconosle
emctl status agent  一个 OEM可以管理多个DB,每个数据库安装agent

//-------上 RAC


 




