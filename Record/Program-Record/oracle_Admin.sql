
官方网认证路径 Oracle University Home->Certification->Certification Paths->DataBase
Oracle Database 12c Administrator Certified Associate　　　(OCA)
Oracle Database 12c Administrator Certified Professional   (OCP)
Oracle Certified Master (OCM) --当前还没有课程要求



Oracle Metalink 帐户
url是 
metalink.oracle.com          E-Delivery  递送
username/password: zhanggl@shtel.com.cn/shanghaitel

oracle EBS产品下载网页
edelivery.oracle.com
E-Delivery
 

---- oracle-12.2在线文档
左则Task中 Development-> SQL and PL/SQL 组中的 SQL Language Reference(所有函数) , PL/SQL Language Reference 
左则Task中 Development-> SQL and PL/SQL 组中的 PL/SQL Packages and Types Reference 有package参考
左则Task中 Administration-> Most Popular 组中的 Error Messages  ,  Reference(初始参数,数据字典和动态性能视图 )


--只删建立的数据库,dbca报错时
oradim -delete -sid orcl,删SID服务
手工删数据文件,spfile,pwd
HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\KEY_OraDB12Home1\ORACLE_SID
安装时建立了oracle用户和很多的组,修改自动建立的oracle密码

orahomeuserctl list
orahomeuserctl updpwd  -user oracle

=================== Windows 下卸载Oracle
1.HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\ 删自己的产品(如有VirtualBox)
2.HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\下的Oracle开头的  对应services.msc 中的服务
还要删一些变量，PATH，PER5LIB(不能超过1023个字符)
也可以用安装程序来卸载
c:\program Files\Oracle 目录的有文件



=================== windows Oracle11g XE 

scott用户没了,hr用户还是在的
 
oracle xe 把HTTP端口从8080改到5500,立即生效,保存在配置文件中
call dbms_xdb.cfg_update(updateXML(dbms_xdb.cfg_get(),'/xdbconfig/sysconfig/protocolconfig/httpconfig/http-port/text()',5500))

SELECT dbms_xdb.getHTTPPort FROM DUAL;  --查端口

C:\oraclexe\app\oracle\product\11.2.0\server\config\scripts\postDBCreation.sql文件中有
	begin
	   dbms_xdb.sethttpport('8080');
	   dbms_xdb.setftpport('0');
	end;
所以也可调用 call  dbms_xdb.sethttpport('5500'); 来修改端口,立即生效,只在内存中修改,但重启后恢复原来的值


1.开始->程序->Oracle Database 11g Express Edition->入门 http://127.0.0.1:5500/apex/f?p=4950 不要登录 (修改端口),提示Oracle Database XE 11.2界面
	http://127.0.0.1:5500/apex/ Oracle Application Express界面中登录要填workspace, 在workspace标签的administration-> Application Express Internal Administration 进入的 登录不要workspace 
点Applicaton Express,没要workspace输入system用户及密码->
DataBase username中单选use existing 选择 hr
	Applicaton Express username中输入myhr
	Password中输入myhr
	Create Workspace铵钮

2. http://127.0.0.1:5500/apex  workspace写hr(数据库用户名),用户名(apex的用户)密码使用myhr就可登录,是apex的管理员,还可建立apex的普通用户


允许远程访问Oracle Application Express，用sqlplus执行 EXEC DBMS_XDB.SETLISTENERLOCALACCESS(FALSE); //文档上说的


=======================OracleXE-11gR2 linux x64 rpm

yum install bc.x86_64


也有系统要求
安装成功 后要以 root 运行  
/etc/init.d/oracle-xe configure   #设置em端口默认8080改5500,监听端口,sys密码,是否开机自启,创建数据库/u01/app/oracle/oradata/XE

 会自动建立oracle用户,dba组, 主目录 /u01/app/oracle/ 
 
chkconfig oracle-xe  off 

/u01/app/oracle/product/11.2.0/xe/bin/oracle_env.sh 有环境变量  


包括下面内容
export ORACLE_HOME=/u01/app/oracle/product/11.2.0/xe
export ORACLE_SID=XE
export NLS_LANG=`$ORACLE_HOME/bin/nls_lang.sh`
export PATH=$ORACLE_HOME/bin:$PAT

 
alias sqlplus='rlwrap sqlplus'
alias rman='rlwrap rman'

locale charmap 来读系统字符集

XE版只能有一个实例
chown -R oracle:oinstall /u01


/etc/init.d/oracle-xe start     提示用 systemctl
.  /u01/app/oracle/product/11.2.0/xe/bin/oracle_env.sh
sqlplus  sys/sys as sysdba
startup


ps -ef | grep ora有进程
时入sqlplus前检查 echo $ORACLE_SID

Oracle Linux 6 sqlplus不能进入,不能启动问题,先/etc/init.d/oracle-xe stop
sqlplus / as sysdba
create  pfile='/u01/app/oracle/product/11.2.0/xe/dbs/initxe.ora' from spfile='/u01/app/oracle/product/11.2.0/xe/dbs/spfileXE.ora'
startup  pfile='/u01/app/oracle/product/11.2.0/xe/dbs/initxe.ora'

lsnrctl start  有1521,5500端口监听  http://localhost:5500/apex 菜单中也有
用sqlplus system/sys@XE 可以登录

------ 
会建立oracle 帐户,要手工设密码才可用
只能系统用户为oracle的才可以dba登录

数据库服务器字符集select * from nls_database_parameters
客户端字符集环境select * from nls_instance_parameters
会话字符集环境 select * from nls_session_parameters
Startup nomount;
　　Alter database mount exclusive;
　　Alter system enable restricted session;
　　Alter system set job_queue_process=0;
　　Alter database open;
　　Alter database character set zhs16gbk;


选择只安装软件时->用来netca加监听->用dbca 来建立数据库->可以所有的用户使用相同的口令->安装一些sample(示例方案)
->(字符集选择AL32UTF8,如保持默认数据库字符集ZHS16GBK,国家字符集AL16UTF16)
可生成创建数据库脚本(/u01/app/oracle/admin/orcl/scripts)

禁用 selinux 
/etc/sysconfig/selinux
注释中有 SELINUX=disabled,也可修改为 permissive

先把selinux 禁用,或者删除
 chcon -t textrel_shlib_t /u01/app/oracle/product/11.1.0/db_1/lib/libnnzll.so 或用 *
 chcon -t textrel_shlib_t /u01/app/oracle/product/11.1.0/db_1/lib/libclntsh.so.11.1(数字)


===================Oracle12c命令行安装 
安装目录response目录下db_install.rsp , dbca.rsp ,netca.rsp,先备份再修改

我使用db_install.rsp 的配置,INSTALL_DB_AND_CONFIG ,安装OK
未设Grid,未设Container database,不使用cloud,不使用recovery,不使用ASM,不使用support,不使用update(两个),只设GENERAL_PURPOSE

安装中要求 MYORACLESUPPORT_USERNAME 必须设置,可乱设,还要必须连网

建立/etc/oraInst.ora 会记录屏幕日志,不设置环境变量,建立配置中所有使用的目录,建用户
{ORACLE_BASE}/fast_recovery_area

./runInstaller -silent  -responseFile /home/oracle/Desktop/database/response/db_install.rsp   ##要使用绝对路径 
#会在后台运行,没有显示进程ID,ps -ef | grep java, 提示日志所在位置,错误直接显示在终端

./runInstaller -silent -debug -responseFile /home/oracle/Desktop/database/response/db_install.rsp  ##更多信息
./runInstaller -silent -noconfig -responseFile /home/oracle/Desktop/database/response/db_install.rsp    ##不能运行dbca,netca,只安装软件

终端提示root运行/opt/oracle/product/root.sh
提示oracle运行下面完成配置
/opt/oracle/product/cfgtoollogs/configToolAllCommands RESPONSE_FILE=<response_file>
做时看提示的日志v/opt/oracle/product/cfgtoollogs/oui/configActions<TIME>-PM.log说一个文件是没有权限,修改chown -R  oracle:oinstall  /opt/oracle/product/inventory
doc上说时建立.properties文件

dbca -silent -responseFile /home/oracle/Desktop/database/response/dbca.rsp  ##是前端运行,有进度
配置"createDatabase" ,EMCONFIGURATION=NONE 安装OK, 
配置"deleteDatabase" 也OK
配置EMCONFIGURATION=ALL, DBSNMP用户是为em用的, 

select dbms_xdb_config.gethttpsport() from dual; 查EM端口
---启用em
alter system set local_listener=oracl scope=both   ###orcl 是tnsnames.ora文件中的,要有正确的SERVICE_NAME
全局数据库名GDBNAME=orcl.localdomain 是 show parameter service_name的值
show parameter  dispatchers
dispatchers="(PROTOCOL=TCP)(SERVICE=<sid>XDB)"

exec DBMS_XDB_CONFIG.SETHTTPSPORT(5500);  后再lsnrctl status多了一行
(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=localhost)(PORT=5500))(Security=(my_wallet_directory=/opt/oracle/admin/orcl/xdb_wallet))(Presentation=HTTP)(Session=RAW))
https://localhost:5500/em/  

netca -silent -responsefile /home/oracle/Desktop/database/response/netca.rsp  ##也OK,可选,db_install.rsp 做完后就有了


=========================Oracle Linux6.4 安装Oracle12.1
doc上的 Quick Installation 
  
cpu信息
# grep "model name" /proc/cpuinfo

# grep MemTotal /proc/meminfo 
# grep SwapTotal /proc/meminfo

swap分区大小必须>=内存的大小,不够可以加

看doc安装依赖包
yum install gcc-c++ compat-libcap1 ksh sysstat
cd  /mnt/cdrom/Packages/
yum install  compat-libstdc++-33-3.2.3-69.el6.x86_64.rpm
yum install  libaio-devel-0.3.107-10.el6.x86_64.rpm

ulimit -a

ulimit -Sn   (-S soft,-H hard 软硬限制)
file descriptor 
$ ulimit -Sn
1024
$ ulimit -Hn
65536

number of processes 
$ ulimit -Su
2047
$ ulimit -Hu
16384

stack
$ ulimit -Ss
10240
$ ulimit -Hs
32768


最大线程数 cat /proc/sys/kernel/threads-max  
 
/etc/security/limits.conf
oracle          soft    nofile  1024
oracle          hard    nofile  65536
oracle          soft    nproc  2047
oracle          hard    nproc  16384
oracle          soft    stack  10240
oracle          hard    stack  32768
要求logout再login

/proc/sys/kernel/sem 的值和  sysctl -a |grep sem 一样的 ,要修改sysctl.confg文件
 
/proc/sys/net/core/rmem_max  最大TCP数据接收缓冲
/proc/sys/net/core/rmem_default 每个
/proc/sys/net/core/wmem_max 最大TCP数据发送缓冲
/proc/sys/net/core/wmem_default 每个

cat > /proc/sys/net/core/rmem_max <<EOF
4194304
EOF

/etc/sysctl.conf 
fs.aio-max-nr = 1048576
fs.file-max = 6815744
kernel.shmall = 2097152
#kernel.shmmax = 536870912  #默认配置是68719476736,要求1578323968
kernel.shmmni = 4096
kernel.sem = 250 32000 100 128
net.ipv4.ip_local_port_range = 9000 65500
net.core.rmem_default = 262144
net.core.rmem_max = 4194304
net.core.wmem_default = 262144
net.core.wmem_max = 1048586

/sbin/sysctl -p


---对于SUSE版本
/sbin/chkconfig boot.sysctl on
如oinstall的GID是501 # echo 501 > /proc/sys/vm/hugetlb_shm_group
/etc/sysctl.conf 多加的
vm.hugetlb_shm_group=501
重启
-----
 
mkdir -p /opt/oracle/product/12.1.0/dbhome_1
mkdir -p /opt/oracle/oradata
chown -R oracle:oinstall /opt/oracle

groupadd oinstall
groupadd dba
#groupadd oper
useradd -m -g oinstall -G dba oracle
passwd oracle

~/.bash_profile
export ORACLE_BASE=/opt/oracle
export ORACLE_HOME=/opt/oracle/product/12.1.0/dbhome_1
export ORACLE_SID=orcl
export PATH=$ORACLE_HOME/bin:$PATH

可建立 /etc/oraInst.loc
inventory_loc=/opt/oracle/oraInventory
inst_group=oinstall


./runInstaller
advance中字符集选择AL32UTF8,安装示例数据库,不要选择(Container database)pluggable database,
界面中save as response file
按提示 root运行 /opt/oracle/product/12.1.0/dbhome_1/root.sh
会建立 /etc/oratab
 


---
判断用户和组是否存在
grep dba /etc/group
useradd -g 主组 -G 属组  -m(建目录) 用户名


id oracle    #显示信息
cat  /proc/version  显示内核信息
useradd -g　主组　-G 辅组  用户   -d指定登录目录   -m 创建用户目录
id nobody   检查nodby用户是否存在
mkdir -p 如果存在不报错
如按退格楗产生^h,stty erase ^h  就会删,可放在.bash_profile文件中
安装时选择 advice 时,netca,dbca

sqlplus中!是把sqlplus放后台,进入系统命令行,再用exit又回到sqlplus
sqlplus 中 host  <系统命令> 或是用!  <系统命令> 

====================工具的使用
grant DEBUG CONNECT SESSION to hr; 
grant DEBUG ANY PROCEDURE  to hr; 
---------SQL Plus

oracle只支持这两个事务隔离级别
SET TRANSACTION ISOLATION LEVEL  READ COMMITTED;  --是Oracle 默认的
SET TRANSACTION ISOLATION LEVEL  SERIALIZABLE; 
ALTER SESSION SET ISOLATION_LEVEL=READ COMMITTED;
ALTER SESSION SET ISOLATION_LEVEL=SERIALIZABLE;
v$transaction
SET TRANSACTION READ ONLY;
 
好像没有查看当前事务隔离级别?????

 clear screen  命令在sqlplus 中清屏

help index 或者 ? index 显示命令
? set 显示 set的帮助
set sqlblanklines on ####可以在sqlplus 显示多个空行

select xx from wher id=&tt;  ##变量tt  同一个执行计划


l2 4 ###显示第2行到 第4行(list)

del 2 4  ###删第2行 到 4行

SAVE C:\filename.sql REPLACE  ###如文件存在会替换的

get c:\filename.sql  ####加载到缓冲区,并显示
col employee_name HEADING 'ename'  format a10  ## 显示宽度
col tname format a20;
col salary HEADING 'sal'  format 999,999 

set heading on/off
spool c:\a.txt
select ...
spool off

select 字段1 ||‘|’||字段2 from 表名

set colsep |	##列的分隔符 就可以用sqlldr
set heading off
set pagesize 20	


sqlplus 能生产xls的excel文件,其实是HTML文件,可以用excel打开保存为逗号分隔的csv文件 或者　是制表符的txt文件
connect / as sysdba;
SET NEWPAGE 0
SET SPACE 0
SET LINESIZE 80
SET PAGESIZE 0
SET ECHO OFF
SET FEEDBACK OFF
SET VERIFY OFF
SET HEADING OFF
SET MARKUP HTML OFF SPOOL OFF
set markup html on     ####会把SQL> 变成SQL&gt;　回车会有<br>,select 会有<tr>

spool c:abc.xls
select * from tab;
spool off;
 

@@ 会在脚本中嵌套调用其它文件

accept pwd hide ##密码输入,后可用&pwd 引用

accept myvar prompt '请输入变量'　　###显示提示  用&myvar

var no number
exec :no:=22
print no ##
 

set pagesize 20;(包括两行的标题，和下一行的空格)
show pagesize


set serveroutput on
exec dbms_output.put_line('hell');

set term off ##在执行@file.sql 时不会输出结果 
set timing on ##显示执行时间
set colsep | ##列的分隔符

### - 用来表示未完成

ttitle center '我的标题' skip 1-  
left '测试报表' right "页"-
format 999 SQL.PNO skip 2

###SQL.PNO 页号
 
title off

##以下两下一起用
break on colum_name skip 1  ##禁止显示重复的(只显示一个其它的为空,不会删行)
comp count label "计数" of 列名(显示数量) ON 列名(要统计的label)
 
save c:\xx.sql 保存文件


1.spool d:/out.txt
2.set heading off 
  select .....
3.spool off

sqlplus -s hr/hr @xe @d:\...sql文件  
	-s 表示不显示sql>一些提示

exec 是sqlplus的命令，只能在sqlplus中使用

break on department_id skip 1	--sqlplus工具　会根据deptno的不同值,空一行
select department_id,first_name,SALARY  from Employees   order by department_id;

SQLPlus 遇到空行就认为是语句结束了
可以使用set sqlBlanklines on 


长时间执行的存储过程,不能通过dbms_output.put_line,这是所有执行完成后才输出

dbms_output.put_line 不能显示存储过程的执行 ,如果 set serveroutput on
sqlplus登录环境没有设置buffer的大小，默认情况下是20000

1.exec dbms_output.enable(200000);
2.set serveroutput on size 1000000;
3.spool d:/test.txt;
	select * from epg_act;
spool off;


-----------上SQL Plus


sys,system,sysman,dbsnmp安装时的默认用户,为安装时的密码
hr 用户是lock ,有表的
 
 select * from tab; 显示所有用户的表,视图
  
启动实例可以用 oradim -startup -sid orcl
 
查询oracle server端的字符集
　　有很多种方法可以查出oracle server端的字符集，:SQL>select userenv('language') from dual;

客户端字符集:
	set NLS_LANG=SIMPLIFIED CHINESE_CHINA.AL32UTF8			window下是乱码的
	export  NLS_LANG='SIMPLIFIED CHINESE_CHINA.AL32UTF8'	linux 下OK的
	set NLS_LANG=SIMPLIFIED CHINESE_CHINA.ZHS16GBK			window下是OK的
	注册表的 HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\KEY_XE\NLS_LANG  的值 

数据库服务器字符集select * from nls_database_parameters，其来源于props$  ,可看NLS_NCHAR_CHARACTERSET和NLS_CHARACTERSET
客户端字符集环境select * from nls_instance_parameters,	其来源于v$parameter，
会话字符集环境 select * from nls_session_parameters，	其来源于v$nls_parameters，
 
oracle 改字符集
Startup nomount;
　　Alter database mount exclusive;
　　Alter system enable restricted session;
　　Alter system set job_queue_processes=0;
　　Alter database open;
　　Alter database character set zhs16gbk;   ###可能报错ORA-12712: new character set must be a superset of old character set

	###ALTER DATABASE CHARACTER SET INTERNAL_USE target_char_set; 

    SQL>STARTUP MOUNT;
　　SQL>ALTER SYSTEM ENABLE RESTRICTED SESSION;
　　SQL>ALTER SYSTEM SET JOB_QUEUE_PROCESSES=0;
　　SQL>ALTER SYSTEM SET AQ_TM_PROCESSES=0;
　　SQL>ALTER DATABASE OPEN;
　　SQL>ALTER DATABASE CHARACTER SET ZHS16GBK;
　　SQL>ALTER DATABASE national CHARACTER SET ZHS16GBK;
　　SQL>SHUTDOWN IMMEDIATE;
　　SQL>STARTUP



stty erease ^h  使用backspace键生效　　(Oracle中)

select banner from sys.v_$version; 来查看Oracle数据库的版本



2.imp username/password file=export.dmp buffer=64000 commit=y feedback=10000
当缓冲64000的时候，commit一次导入，然后每导入10000行显示一次，这样就知道正在运行imp


2、用导入参数indexfile导入aa用户下的所有表，建表和索引的语句导出到文件,其中建表语句是加注释的。
IMP bb@instance_name FULL=Y FILE=path INDEXFILE=d:\altertablespace_table_index.SQL LOG=d:\altertablespace.LOG fromuseraa touser=bb
修改创建表时指定的表空间名，再执行创建表。
3、采用导入参数indexes=n和ignore=y将aa用户表数据导入库中
IMP aa@instance_name FULL=Y INDEXES=N FILE=path  IGNORE=Y 
4、创建索引
还有一种办法就是利用MV表空间的方法，麻烦的就是需要重建索引，表多的话

1）.“完全”增量导出（Complete） 
即备份整个数据库，比如： 
exp system/manager inctype=complete file=040731.dmp
2）、“增量型”增量导出 
备份上一次备份后改变的数据，比如： 
	exp system/manager inctype=incremental file=040731.dmp  
	可以远程连接
	exp system/manager@127.0.0.1:1521/XE rows=n  file=D:/temp/XE_oracle11.2_full.dmp


exp parfile=my.par  ##参数 存文件中
	userid=scott/tiger------连接的用户scott
	file=d:\back.dmp------导出的文件的名字
	full=y 
	-------
	owner=(scott) -------导出scott用户的所有对象
	--
	tables=(scott.emp,scott.dept,test.student)
	
	buffer=1024000 ---------缓冲
	rows=y ----------是否导出记录
	compress=y ---------extent是否压缩
	grants=y ----------grant语句是否导出
	indexes=y ----------导出索引
	full=y ----------全库导出
	feedback=3 ----------显示导出进度每3行

imp parfile=d:\test.par
	fromuser=scott
	touser=test
	ignore ----------忽略导入过程中的错误


如果有FOREIGN KEY约束正在引用UNIQUE或PRIMARY KEY约束，则无法禁用这些UNIQUE或PRIMARY KEY约束， 
这时可以先禁用FOREIGN KEY约束，然后再禁用UNIQUE或PRIMARY KEY约束；或者可以在ALTER TABLE...DISABLE 
语句中指定CASCADE关键字，这样将在禁用UNIQUE或PRIMARY KEY约束的同时禁用那些引用它们的FOREIGN KEY约束，如： 
alter table employees disable primary key cascade 

约束数据字典 
all_constraints/dba_constraints/user_constraints 约束的基本信息，包括约束的名称，类型，状态 
(约束类型：C(CHECK约束),P(主码约束),R(外部码约束),U(唯一码约束)) 
all_cons_columns/dba/user 约束对应的字段信息 
dba_cons_columns
user_cons_columns
 
----Oracle11gXE 
C:\oraclexe\app\oracle\product\11.2.0\server\dbs\SPFILEXE.ORA
C:\oraclexe\app\oracle\product\11.2.0\server\database\PWDXE.ora,initXE.ora

xe.__db_cache_size=230686720
xe.__java_pool_size=4194304
xe.__large_pool_size=4194304
xe.__oracle_base='C:\oraclexe\app\oracle'#ORACLE_BASE set from environment
xe.__pga_aggregate_target=318767104
xe.__sga_target=478150656
xe.__shared_io_pool_size=75497472
xe.__shared_pool_size=155189248
xe.__streams_pool_size=0
*.audit_file_dest='C:\oraclexe\app\oracle\admin\XE\adump'
*.compatible='11.2.0.0.0'
*.control_files='C:\oraclexe\app\oracle\oradata\XE\control.dbf'
*.db_name='XE'
*.DB_RECOVERY_FILE_DEST_SIZE=10G
*.DB_RECOVERY_FILE_DEST='C:\oraclexe\app\oracle\fast_recovery_area'
*.diagnostic_dest='C:\oraclexe\app\oracle\.'
*.dispatchers='(PROTOCOL=TCP) (SERVICE=XEXDB)'
*.job_queue_processes=4
*.memory_target=760M
*.open_cursors=300
*.remote_login_passwordfile='EXCLUSIVE'
*.sessions=20
*.shared_servers=4
*.undo_management='AUTO'
*.undo_tablespace='UNDOTBS1'
 

###可加 instance='other' 每个initSID文件不同,改其它的如db_name,control_file...
###启多个instance 要更改环境变量ORACLE_SID ,用不同用户(orapwd生成的)

--------instantclient 的tnsnames.ora
# set ORACLE_HOME=D:\instantclient_11_1
# set TNS_ADMIN=D:\instantclient_11_1
# set NLS_LANG=SIMPLIFIED CHINESE_CHINA.ZHS16GBK
# set LD_LIBRARY_PATH on Linux, or PATH on Windows
#sqlplus pin/pin@192.168.1.66:1521/orcl  OK
#sqlplus pin/pin@//192.168.1.66:1521/orcl  OK
# windows OK,linux or solaris　export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME OK

orcl=
(DESCRIPTION=
	(ADDRESS=(PROTOCOL=tcp)(HOST=192.168.1.66)(PORT=1521))
    (CONNECT_DATA=
#	(SERVER = DEDICATED)
	(SERVICE_NAME=orcl)
#	(INSTANCE_NAME=orcl)
    )
)

-----instantclient 的sqlnet.ora
NAMES.DIRECTORY_PATH= (TNSNAMES, ONAMES, HOSTNAME)
SQLNET.EXPIRE_TIME=10

默认只有
SQLNET.AUTHENTICATION_SERVICES = (NTS)

------------------
tnsnames.ora文件中的orcl别名前不能有空格

监听程序日志文件  c:\oracle\diag\tnslsnr\lizhaojin\listener\alert\log.xml

netca 可以创建NETWORK\ADMIN下的三个文件

tnsping orcl
lsnrctl status

tnsping 127.0.0.1
tnsping localhost
alter system register

  COMMENT   ON   COLUMN   TABLE_NAME.COL_NAME   IS   '列注释';  
  COMMENT   ON   TABLE   TABLE_NAME   IS   '表注释'; 

select * from user_col_comments where Table_Name=upper('T_myTable') ;
select * from user_tab_comments;
 
host 执行系命令

  

1、命令启动：
  $ dbstart
  
    停止命令：
  $ dbshut

su - oracle -c "dbstart" 
su - oracle -c "dbshut"
su - oracle -c "lsnrctl start"

dbhome 命令显示ORACLE_HOME的值
 
imp vnet/passpass commit=y buffer=200 full=y file=cdn.dmp 
200条,commit一次,防止防止回滚段不够
create   public   rollback   segment   RB400   tablespace SYSTEM storage   (initial   4M   maxextents   unlimited  );

   
lsnrctl start LISTENER

---热备份,一定要日志归案, 恢复时要用归案文件 路径  recovery_file_dest

启动归案
alter system set log_archive_start =true scope=spfile   (过时)
shutdown immediate
startup mount
alter database archvielog
atter database open
archive log list ##显示已经归案

alter tablespace xx begin backup
数据文件复制走(备份)
alter tablespace xx end backup

alter system archive log current ##归案当前日志

alter system switch logfile ;  (共2次,对3个日志文件)

shutdown immediate

删文件(startup 会有问题)

select * from $recover_file;   
			有一个 NOT FOUND 6
alter database datafile 6 offline drop
alter database open
恢复删除文件后(不可以select 表)
recover datafile 6 ;  ->auto
alter database datafile 6 online   ###select ...后有了

show parameter db_recovery_file_dest

---备份控制文件
连机时用alter database backup controlfile to trace ;//生成在udump目录,要复制出来(否则oracle会改的)
(有注释信息,要删****开头的,最好也删--),

shutdown immediate
删控制文件
  

SELECT *  FROM database_properties   WHERE property_name = 'DEFAULT_TBS_TYPE';
默认是 smallfile
ALTER DATABASE SET DEFAULT bigfile TABLESPACE;
CREATE BIGFILE TABLESPACE................
BIGFILE类型的,
1.每个表空间只能包含一个数据文件
2.只有自动段空间管理的 LMT (locally managed tablespaces ) 
3.SELECT DBMS_ROWID.rowid_block_number (ROWID, 'BIGFILE')  from dual;



启动服务 要/etc/oratab对应实例是Y
$ dbstart $ORACLE_HOME
$ dbshut   $ORACLE_HOME
su - oracle -c "lsnrctl start"



lock table xx in lockmode__  mode [nowait]  (以后也要commit,rollback)

lockmode__ 有以下:
ROW SHARE
ROW EXCLUSIVE
SHARE UPDATE  (See ROW SHARE.)
SHARE  (锁定整个表,其它用户可也锁定这个表(share),)禁止其他用户插入、更新和删除行

V$LOCK  BLOCK为1表示在锁状态 ,LMODE 为6 表示exclusive (X) ,TYPE为TX 表示 Transaction enqueue

V$LOCKED_OBJECT  的LOCKED_MODE含义
	0：none
	1：null 空
	2：Row-S 行共享(RS)：共享表锁，sub share
	3：Row-X 行独占(RX)：用于行的修改，sub exclusive
	4：Share 共享锁(S)：阻止其他DML操作，share
	5：S/Row-X 共享行独占(SRX)：阻止其他事务操作，share/sub exclusive
	6：exclusive 独占(X)：独立访问使用，exclusive
	还有SESSION_ID (V$session的sql_address是V$SQL的address)ORACLE_USERNAME

select ADDRESS,SQL_TEXT ,ELAPSED_TIME from V$sql where module='JDBC Thin Client' and  ELAPSED_TIME>500000 ;--0.5秒

SHARE ROW EXCLUSIVE(其它用户不可锁这个表(share))
EXCLUSIVE(其它用户可select,如回锁,改要等 )


unlimited tablespace 权限 
select * from user_sys_privs ;显示当前用户有哪些系统权限 

select * from user_tab_privs ;显示当前用户有哪些对象(表)权限 

对象权限 一个用户是可以另一个用户的表....
grant select on mytable to hr;//对象权限,拥有者可以制授权,,,,sys用户可以吗?
grant update(mycol) on mytable to hr;
to public 对所有的人

不以所unlimited tablespace  给role  //有些系统权限

grant delete any table to myrole

sys用户使用 首先使用操作系统验证(密码错误也可,windows有一个组ora_dba里有本机用户)和密码文件验证,普通用户使用数据验证
先 lsnrctl start ->sqlplus sys/ as sydba ->startup

PWDorcl.ora文件
orapwd file= xx password = xx entries=3
select * from v$pwfile_users;//密码文件中的所有SYSDBA,SYSOPER用户

drop user cascade //删除用户和用户下所有的对象


加"" 当要使用oracle  关键字时
手工create database ,是建用户要 sqlplus/PUPBLD.SQL as SYSTEM
@/u01/app/oracle/rdbms/admin/catproc.sql
@/u01/app/oracle/rdbms/admin/catalog.sql
@/u01/app/oracle/sqlplus/admin/pupbld.sql  system用户


imp user/pass commit=y buffer=200 full=y file=cdn.dmp 
200条,commit一次,防止防止回滚段不够
create   public   rollback   segment   RB400   tablespace SYSTEM storage   (initial   4M   maxextents   unlimited  );

查看dmp文件字符集
cat   login.dmp   |od   -x|head   -1|awk   '{print   $2   $3}'|cut   -c   3-6   
0345
select   nls_charset_name(to_number('0345','xxxx'))   from   dual;   
select   to_char(nls_charset_id('ZHS16GBK'),   'xxxx')   from   dual;  

alter database vnetgbk datafile '/u01/app/oradata/vnetgbk/dbfile01.dbf' AUTOEXTEND on next  2m  MAXSIZE  UNLIMITED ;
create tablespace mytab datafile 'D:\app\oracle\oradata\orcl\mytab'  size 50m  AUTOEXTEND on next  2m  MAXSIZE  UNLIMITED;
create user c##myuser identified by myuser default tablespace mytab;  --从12c 开始用户名前必须加c## 或 C##
grant create session,resource to c##myuser;

如果把一个表改另一用户的主,只能用synonym 建同义字,同义字名不可以用 "." ,可以考虑使用另一个用户登录

如果数据库所在分区空间不够,用alter database rename file '/u01/xxx' to '/u02/xxx'
select * from V$tempfile;
-----------------------
select * from v$fixed_table
select * from v$system_parameter ;


tune2fs -l /dev/sda1|grep Block //linux查块大小

fsutil fsinfo ntfsinfo C:   //windows 查块大小 (每个簇字节数 :4096=4k)
FAT32格式的区块根据情况不同有不同的大小
	分区为3 GB–7 GB 使用 4 KB的簇
	分区为8 GB–16 GB 使用 8 KB的簇
	分区为16 GB–32 GB 使用 16 KB的簇
	分区大于32 GB 使用 32 KB的簇





select current_scn from V$database;  //当前SCN号
v_xx constant number(3,2):=0.03 定义变量是常量


 
 
--------oracle备份与恢复 书
----expdp,impdp 
exp ,imp 是工具,服务器端和客户端都可以用
expdp,impdp 只是服务器端工具  (10g新功能 ,不能和exp一起使用)
directory=x  是create or replace directory生成的
如full=y (不会导出sys...等的schema) 必须要用 EXP_FULL_DATABASE角色   schemas=xx,yy 也要  

status=5 每隔5秒显示一次状态
create  directory my_dir as 'd:\dump'
grant read,write on directory my_dir to scott;

impdp 的estimate=blocks/satistics 必须和network_link同时使用
impdp 的network_link 用于把网络数据导入到本地数据库，不能和dumpfile一起使用
impdp 的remap_datafile='DB1$:tbs6.f':'/db1/hrdata/tbs6.f'  用在不同平台
	remap_schema=scott:system
	remap_tablespace=users01:users02
	reuse_datafiles=y   表示在create tablespace 会覆盖已存在的文件 ？？？？？？？？？？？？？？
	skip_unusable_inidexes=y
	sqlfile=     只是DDL
	streams_configuration=y 是BLOB
	table_exists_action=skip|append|replace
	transform=segment_attributes:n:table    是否包含段属性  还有storeage

	transport_full_check 必须与network_link同时使用

select platform_name from V$transportable_platform ;//可以交互平台，expdp,impdp,但必须相同的数据库字符集和民族字符集 nls_database_parmeters
	nls_characterset(数据库字符集),nls_nchar_characterset(民族字符集)
	不能般system表空间和sys用户对象
	dbms_tts.transport_set_check('users01表空间',true)检查表空间集合是否为自包含的  ，写信息到表 transport_set_violations，如无，表明是自包含的(索引和表是否在同一表空间)
	必须有execute_catalog_roel 角色


生成文件的DB的block是8k,要导入的DB是4k, 要alter system set db_4k_cache_size=4m;

----
 
安装oracle 时主机名最好不要以数字开头 ,安装em 失败,可能lsnrctl start 失败,
 

如果在数据正在用时,删了数据文件,就不能再启动,使用下面命令,
alter database datafile '/opt/oracle/oradata/xxx_tb.dbf' offline drop;


user1 imp 导入user2 备份的后,user1的表还存放在user2所使用的表空间,如user2删除了这个表空间,则user1的数据也被删除??? 
 
select * from session_privs where Privilege like concat('%',concat(upper('view'),'%'));
select * from session_privs where Privilege like  '%'|| upper('view')||'%';
 

 [$ORACLE_HOME]/rdbms/admin
SQL> connect / as sysdba
SQL> @spcreate  会创建 perfstat 用户,SYSAUX表空间

SQL> @sppurge  清除perfstat 用户内容

SQL>desc statspack;

statspack.snap



create public database link 数据库链接名
connect to　用户名　IDENTIFIED BY 密码
using '(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=135.251.208.142)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=mdmdb)))';

SELECT …… FROM 表名@数据库链接名; 

dbms_lock.sleep(0.5); 
@%ORACLE_HOME%\RDBMS\ADMIN\dbmslock.sql  SYS用户
@%ORACLE_HOME%\RDBMS\ADMIN\userlock.sql


dbms_application_info.SET_CLIENT_INFO('So far');//超过64字符会被截断
select client_info from v$session where client_info like 'So far%';
 

要用system用户才可以
DBMS_SYSTEM.KSDWRT (dest IN BINARY_INTEGER,tst IN VARCHAR2); 写alert log and/or trace file

1 - Write to trace file.  /udump/
2 - Write to alertlog.	/bdump/
3 - Write to both. 

select sid,serial#,username from v$session where username is not null; 
exec dbms_system.set_sql_trace_in_session(9,11,true); //对xx用户进行跟踪,9,11是sid,serial# ,
那个用户做一些事后,
exec dbms_system.set_sql_trace_in_session(9,11,false);//取消跟踪
/udump/

exec dbms_system.set_bool_param_in_session(9,11,'timed_statistics',true);
exec dbms_system.set_int_param_in_session(9,11,'max_dump_file_size',2147483647);

select * from dict;所有的数据字典如ALL_XX,DBA_XX,USER_XX

参数 memory_target ,MEMORY_MAX_TARGET
在Oracle11g中SHARED_POOL_SIZE、DB_CACHE_SIZE、LARGE_POOL_SIZE、JAVA_POOL_SIZE、STREAMS_POOL_SIZE,PGA_AGGREGATE_TARGET,SGA_TARGET都是自动调整的

select * from 　　V$MEMORY_DYNAMIC_COMPONENTS：描述当前所有内存组件的状态
select * from 　　V$MEMORY_RESIZE_OPS：循环记录最后800次的内存大小调整请求 OPS=operations


result_cache_max_size  缓存SQL结果
client_result_cache_size
V$system_parameter 中显示 result_cache_max_size  ISSYS_MODIFIABLE  为IMMEDIATE

 
------在哪了????
$AGENT_HOME
 $ /u01/app/oracle/Middleware/agent/agent_inst/bin/emctl start agent
 
Oracle Enterprise Manager Cloud Control  12c 下载安装(6G)
 
db_1/HOSTNAME_orcl/sysman/config/emkey.ora中是对Enterprise Manager数据进行加密密钥,务必备份些文件
 
如重新系统变了主机名,没有修改Oracle, listener.ora,tnsnames.ora文件
emctl start dbconsole 不能使用,最好修改/etc/hosts文件
 
sysman用户
dbsnmp用户  emca -config dbcontrol db
 
[oracle@zone2 ./install]$cat portlist.ini
Enterprise Manager Console HTTP Port (MDMDB) = 5560
Enterprise Manager Agent Port (MDMDB) = 3938
 

创建一个EM资料库
emca -repos create

重建一个EM资料库
emca -repos recreate

删除一个EM资料库
emca -repos drop

配置数据库的 Database Control
emca -config dbcontrol db

删除数据库的 Database Control配置
emca -deconfig dbcontrol db

重新配置db control的端口，默认端口在 5560
emca -reconfig ports
emca -reconfig ports -dbcontrol_http_port 1160	
emca -reconfig ports -agent_port 3940
注：查看端口号可查如下路径。<oracle_Home/install/portlist.ini>

先设置ORACLE_SID环境变量后，启动EM console服务
emctl start dbconsole

先设置ORACLE_SID环境变量后，停止EM console服务
emctl stop dbconsole

先设置ORACLE_SID环境变量后，查看EM console服务的状态
emctl status dbconsole

配置dbconsole的步骤
emca -repos create
emca -config dbcontrol db
emctl start dbconsole

重新配置dbconsole的步骤
emca -repos drop
emca -repos create
emca -config dbcontrol db
emctl start dbconsole

--------------------

-------------------Oracle ASM
下载安装ASMlib2.0 ,如是Oracle Linux6 只要安装光盘自带的oracleasm-support-2.1.8-1.el6.x86_64.rpm
##还要下载oracleasmlib-2.0.4-1.el6.x86_64.rpm 并安装

root用户做
/etc/init.d/oracleasm configure  配置
##/etc/init.d/oracleasm disable
/etc/init.d/oracleasm enable

使用fdisk分区sdb1
/etc/init.d/oracleasm createdisk VOL1 /dev/sdb1
/etc/init.d/oracleasm createdisk VOL2 /dev/sdc1 
/etc/init.d/oracleasm createdisk VOL3 /dev/sdd1 
/etc/init.d/oracleasm createdisk VOL4 /dev/sde1 
/etc/init.d/oracleasm listdisks  显示所有的磁盘,即VOL 1-4

---如createdisk失败
##dd if=/dev/zero of=/dev/sdb1 bs=4096 count=1
/usr/sbin/oracleasm configure  查结果
oracleasm status
more /proc/filesystems  | grep asm
df -ha 有oracleasmfs
/var/log/oracleasm  ###提示没权限,原因是被selinux禁用
---

如是RAC环境下,要在其它的机器上运行/etc/init.d/oracleasm scandisks 来检查磁盘变化
安装Oracle只装软件

cd $ORACLE_HOME/dbs
###oracle SID 是orcl ,ASM实例是+ASM
写参数文件
vi init+ASM.ora  --注意大小写
*.asm_diskstring='ORCL:VOL*'
*.background_dump_dest='/opt/oracle/admin/+ASM/bdump'
*.core_dump_dest='/opt/oracle/admin/+ASM/cdump'
*.instance_type='ASM'
*.large_pool_size=12M
*.remote_login_passwordfile='shared'
*.user_dump_dest='/opt/oracle/admin/+ASM/udump'

建密码文件 
orapwd file=orapwd+ASM password=dba  生成在当前目录下放到dbs目录下

mkdir -p $ORACLE_BASE/admin/+ASM/udump
mkdir -p $ORACLE_BASE/admin/+ASM/bdump
mkdir -p $ORACLE_BASE/admin/+ASM/cdump

export ORACLE_SID=+ASM  当前使用ASM启动 ,注意大小写
sqlplus / as sysdba
>startup
报no diskgroup mounted
>create spfile from pfile;   ---失败???
>shutdown immediate
>startup
>create diskgroup dgroup1 normal redundancy failgroup fgroup1 disk 'ORCL:VOL1' ,'ORCL:VOL2'   failgroup fgroup2 disk 'ORCL:VOL3','ORCL:VOL4';

redundancy 多余
>select name,state from V$asm_diskgroup; 查磁盘组 Satat是 mounted,也用下手工mount
>alter diskgroup dgroup1 mount;   
>show parameter asm_diskgroups

dbca来创建库asmdemo,要选择ASM,而不是file system

stty erase ^H
启动数据前先启动ASM,即先ORACLE_SID=+ASM , 再ORACLE_SID=orcl启动

测试时VMWare中增加两个磁盘,fdisk分区,再/etc/init.d/oracleasm createdisk VOL6 /dev/sdf1 ,再加另一个,启动ASM,启动DB

ORACLE_SID=+ASM 切换到ASM中,看视图V$asm_operation ,V$asm_disk , V$asm_diskgroup

V$asm_disk 中可以看到新增加的磁盘

//向已有的磁盘组中加新的磁盘
alter diskgroup dgroup1 add failgroup fgroup1 disk 'ORCL:VOL5'  failgroup fgroup2 disk 'ORCL:VOL6' ;

select group_number ,opteration,state,est_work,sofar,est_rate,est_minutes from V$asm_operation;
查到一条数据      REBAL(reblance) RUN		
再查一次发现 sofar,est_rate已增加,直到查不到数据,表示数据分配工作在进行(对新加磁盘)

select name,allocation_unit_size ,total_mb  from V$asm_diskgroup
				总共大小
				
ORACLE_SID=orcl 切换到数据实例
create tablespace ts_test datafile '+dgroup1' size 200m ;//在ASM上建立表空间,前有"+"表示ASM

ORACLE_SID=+ASM 切换到ASM中
alter diskgroup dgroup1 drop disk VOL4; //删磁盘,再看V$asm_operation,直到没有数据
再查V$asm_diskgroup 发现VOL4还在,要做
alter diskgroup dgroup1 rebalance; //对删了的磁盘,自动平衡数据
----------------上--Oracle ASM
---------------data gurad
物理备库,只是把文件复制
主库把日志文件redo log通过网络传给另一个数把库(逻辑备库),转换为SQL执行,备库做查询服务使用


主数据库必须是 atler database  force logging 和 archivelog

LOG_ARCHIVE_DEST_n 和 FAL_SERVER 初始参数 
 
在主库上alter database create standby controlfile as 'd:\standby1\control01.ctl';

oradim -new -sid standby1  -intpwd oracle 建立口令文件，服务
配置服务器SID

如从库和主库在同一台机器上，必须配置初始参数 db_file_name_convert 和 log_file_name_convert
DB_FILE_NAME_CONVERT = 'string1' , 'string2' , 'string3' , 'string4' , ...
      string1 is the pattern of the primary database filename
      string2 is the pattern of the standby database filename


主库参数文件中 (所有数据文件在 d:\demo)
db_unique_name=demo
log_archive_dest_1='location=d:\demo\archive valid_for=(all_logfiles,all_roles) db_unique_name=demo'
log_archive_dest_2='service=standby1 valid_for=(online_logfiles,primary_role) db_unique_name=standby1'
log_archive_config='dg_config=(demo,standby1)' 
fal_server=standby1
fal_client=demo
standby_file_management=AUTO


FAL (Fetch Archive Log)

备库参数 (所有数据文件在 d:\standby1)
db_unique_name=standby1
service_names=standby1
instance_name=standby1
control_files=d:\standby1\control01.ctl		#用是create 生成的 也要有数据文件,日志文件，来启动只读
log_archive_dest_1='location=d:\standby1\archive valid_for=(all_logfiles,all_roes) db_unique_name=standby1'
log_archive_config='db_config=(demo,standby1)'
fal_server=demo
fal_client=standby1
db_file_name_convert='d:\demo','d:\standby1'
log_file_name_convert='d:\demo','d:\standby1','c:\demo','d:\standby1'
standby_file_management=AUTO
standby_archive_dest='d:\standby1\archive'
background_dump_dest='d:\standby1\bdump'
user_dump_dest='d:\standby1\udump'

备库没有临时表空间，也就不能排序
---------

------oracle 12c
Oracle12c 中只要lsnrctl start 就有5500监听,有一会延迟,没有启动方法是嵌入到数据库中
init.ora文件中一定要有local_listener=oracl ###orcl 是tnsnames.ora文件中的,要有正确的SERVICE_NAME
alter system set local_listener=oracl scope=both
show parameter dispatcher
dispatchers="(PROTOCOL=TCP)(SERVICE=<sid>XDB)"
XDB的Schema是专为EM的
select dbms_xdb_config.gethttpsport() from dual; --Oracle12C 查EM端口
exec dbms_xdb_config.sethttpsport (5500);--修改端口立即生效 ,再lsnrctl status多了一行
(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=localhost)(PORT=5500))(Security=(my_wallet_directory=/opt/oracle/admin/orcl/xdb_wallet))(Presentation=HTTP)(Session=RAW))

Oracle Enterprise Manager database Express 12c
https://localhost:5500/em/   12c 即 12.1的 要安装flash插件, 远程连接关闭iptables

oracle 12c  多了pdbadmin(plugin database)

create user c##myuser identified by myuser default tablespace mytab;--必须以C##或c##开头

--Container Plugin DB
CDB_xx (多了一列CON_ID)->DBA_xx->ALL_xx->USER_xx

show pdbs 同 select * from V$PDBS;有CON_ID
alter session set container=pdborcl;--pdborcl是show pdbs的结果
每个container有唯一的端口

show con_name  当前连接的PDB,默认的CDB$ROOT
select sys_context ('Userenv','Con_Name')  from dual;


CREATE DATABASE ... ENABLE PLUGGABLE DATABASE
dbca 可以建plugin数据库，生成脚本
CREATE PLUGGABLE DATABASE pdb1 ADMIN USER mydpd1 IDENTIFIED BY mydpd1 ROLES=(CONNECT) --默认从PDB$SEED复制,可加nocopy
CREATE PLUGGABLE DATABASE salespdb FROM hrpdb --复制
alter pluggable database pdb1 open 状态变Read(Write),close后变为mount

非容器DB,执行DBMS_PDB.DESCRIBE ;



--windows版12c开启EM
 
 
 
------oracle 12c RAC
Grid home包括Oracle Clusterware 和 Oracle ASM
Oracle Management Service(OMS) 有 OPMN  进程
Oracle Cluster Registry (OCR) voting 
Automatic Storage Management Cluster File System (ACFS) 基于ASM
Oracle ASM Dynamic Volume Manager (Oracle ADVM)
single client access name (SCAN) 


hosts 或 nodes
cluster
server pool 所有的node
Free pool 默认的池名
policy-managed database
administrator-managed database

两个node的cluter至少3个投标磁盘


---VMWware Server (不是Workstation) 的.vmx配置文件
disk.locking = "FALSE"
diskLib.dataCacheMaxSize = "0"
scsi1.sharedBus = "virtual"

scsi1:0.deviceType = "disk"		
scsi1:1.deviceType = "disk"
scsi1:2.deviceType = "disk"
scsi1:3.deviceType = "disk"
---




一、查询执行最慢的sql 
	
select *
 from (select sa.SQL_TEXT,
        sa.SQL_FULLTEXT,
        sa.EXECUTIONS "执行次数",
        round(sa.ELAPSED_TIME / 1000000, 2) "总执行时间",
        round(sa.ELAPSED_TIME / 1000000 / sa.EXECUTIONS, 2) "平均执行时间",
        sa.COMMAND_TYPE,
        sa.PARSING_USER_ID "用户ID",
        u.username "用户名",
        sa.HASH_VALUE
     from v$sqlarea sa
     left join all_users u
      on sa.PARSING_USER_ID = u.user_id
     where sa.EXECUTIONS > 0
     order by (sa.ELAPSED_TIME / sa.EXECUTIONS) desc)
 where rownum <= 50;

二、查询次数最多的 sql 
	
select *
 from (select s.SQL_TEXT,
        s.EXECUTIONS "执行次数",
        s.PARSING_USER_ID "用户名",
        rank() over(order by EXECUTIONS desc) EXEC_RANK
     from v$sql s
     left join all_users u
      on u.USER_ID = s.PARSING_USER_ID) t
 where exec_rank <= 100;
 
 