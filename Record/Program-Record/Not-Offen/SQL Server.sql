
http://msftdbprodsamples.codeplex.com/releases   有 SQL Server 2014 示例数据库  Adventure Works
	-- Management Studio 右击 databases目录->Aattach...->Add...按钮->找到文件.mdf
	AdventureWorks2014.bak 文件的恢复 读文档 执行
	
	USE [master]
	
	RESTORE DATABASE AdventureWorks2014
	FROM disk= 'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\Backup\AdventureWorks2014.bak'
	WITH MOVE 'AdventureWorks2014_data' TO 'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\AdventureWorks2014.mdf',
	MOVE 'AdventureWorks2014_Log' TO 'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\AdventureWorks2014.ldf'
	,REPLACE


--新版本2014不能使用2008的文件
CREATE DATABASE AdventureWorks2008 
ON (FILENAME = 'D\AdventureWorks2008_Database\AdventureWorks2008_Data.mdf'), (FILENAME = 'D:\AdventureWorks2008_Database\AdventureWorks2008_Log.ldf')
FOR ATTACH;

	
SQL Server 2014 安装时可选择evoluation或者express,有management Studio
  doNet4 应该在 redist\DotNetFrameworks\  下 dotNetFx40_Full_x86_x64.exe

 

安装时的用户必须是操作系统已有的用户
管理员用户sa


用户不能登录
	1.然后在‘安全性’-‘登录’-右键单击‘sa’-‘属性’，将默认数据库设置成master,可修改密码,左侧[状态]->登录中 选择[启用]

	2.右击SQL Server服务器的名称   ->"属性" ->"安全性"选项卡 ->"服务器身份验证"下，选择"SQL Server和 Windows"



－－－这个外网用的？？？
打开‘程序’－‘所有程序’－‘Microsoft SQL Server 2008 ’－‘配置工具’－‘SQL Server 配置管理器’，在弹出的窗体中，
找到‘SQL Server网络配置’，把‘MSSQLSERVER的协议’下的“Named Pipes”和“TCP/IP”启动，然后重新启SQL Server就可以了







21到30的记录
select * from 
(   
	select TOP 10 * FROM ( SELECT TOP 30   * from user_table   ORDER BY id ASC ) as aSysTable   ORDER BY id DESC 
) as bSysTable   ORDER BY id ASC






MCITP：数据库管理人员 SQL Server




工具－》SQL Server Profiler->连接后－》输入跟踪名称－》保存文件吗？，启用跟踪时间－》事件选择标签中－》
工具－》数据库引擎优化顾问－》


视图－》已注册的服务器
右击数据库引擎－》新建服务器组－》

右击数据库引擎－>新建服务器注册－》MY
右击刚才新建的My->





\90\Tools\Binn\sqlcmd







-----------SQL Server 2000

mdf 数据文件
ldf日志文件
create database mydb datafile
on   //文件组默认是 primary
(
name=student_dat,
filename='d:\student_dat.mdf',    //主数据文件,只能一个
size=5MB,
maxsize=50MB,
filegrowth=2MB
),   //可以一个,多个
(
name=student1_dat,
filename='d:\student_dat1.ndf',   //次数据文件,可无,可多
size=5MB,
maxsize=50MB,
filegrowth=2MB
)

log on
(
name=student_log,
filename='d:\student_log.ldf',
size=5MB,
maxsize=50MB,
filegrowth=2MB
)





alter database mydb 
add file 
(
vname=student_data2,
filename='d:\student_data2.ndf',
size=5MB
)




alter database mydb 
modify file 
(
vname=student_data2,
size=10MB   //只可比原始大
)











===========windows Server 2008  cluster SQL server2008 

DC和storage在一台上应该比较好,DC就应该不能在VM中了,也可再加入DNS(server 2003 装AD,配DNS,ping不通自己的域名)


一边DNS
两边都不能做dcpromo,加入同一域(dcpromo依赖于DNS),加入cluster节点，并以域用户登录windows，(故障转移群集安装)当前节点的存储4G

两边应该不要[Active Directory 域服务]
两加都要 failover 安装  依赖于[Active Directory 域服务],应该可以其它机器

强制性的域控降级方法去做，命令为：dcpromo /forceremoval,才可以删  (不要复选,删除)
日志文件Detail.txt位于%ProgramFiles%\Microsoft SQL Server\100\Setup Bootstrap\Log\20090316_112604


Standard Edition 支持2个节点
Enterprise Edition 支持16个节点

http://www.mssqltips.com/tip.asp?tip=1687  参考



1.(双节点都要)服务管理器下->add Role时->应用服程序务器->在Distrubted Transactions->选择Incoming Remote Trasactions 和Outcoming Remote Transactions

2.(双节点都要)add feature->failover clustering

	最好做 验证 
4.(一个节点做)在安装机器上再create cluster 要一个名字IP,  (机器不在域中不能创建cluster)
	右击建立的cluster->more action..->configure cluster quorum，最小512M

5.cluster 中 右击Server and Applications->configure service or application->Distributed Transaction Coordinator 协调器(DTC)  要 名字 和 DTC的IP
	这里要一个存储
	
6.(一节点)安装 SQL server2008  时选择New SQL Server failover cluster installation (故障转移群集安装)->
	安装过程中要一个可用的存储(2G不够,3.8G可以,要是在当前节点初始化,格式化)
				
	OK---检查错误,3.规则"Microsoft 群集服务(MSCS)群集验证错误"失败,参阅KB953748 
		http://support.microsoft.com/kb/953748
		* For an integrated failover setup, run the following command on each node that is being added:
		----	Setup /SkipRules=Cluster_VerifyForErrors /Action=InstallFailoverCluster
		* For an advanced or enterprise installation, run the following command:
		      Setup /SkipRules=Cluster_VerifyForErrors /Action=CompleteFailoverCluster

	http://support.microsoft.com/kb/955963 是对 网络绑定顺序的 警告,


要输入一个[SQL Server 网络名称]，xe
群集磁盘"sql" 不归本地节点所有，第一次初始化和建立卷时在本节点操作(加入域的)
要两个未被使用的IP
［服务器配置］中要输入域用户名，test.com/administrator 

安装完成后会在[服务和应用程序]下多了一个SQL Server(MSSQLSERVER)




非cluster的SQL Server登录时不能使用IP,使用主机名，如lizhaojin\sqlexpress(对非cluster要有\),完整版默认实例是MSSQLSERVER
本节点连接cluster时,只用xe(是安装输入的[SQL Server 网络名称])
测试客户端要加入域只用xe,也可用xe.test.com
不加域用,只用xe.test.com(设DNS) 也可以,直接输入解析的IP也可以(但IP可能会变)

两节点ping xe结果不同，正在服务的节点ping xe 返回是4,另一个节点ping xe返回是5,(安装时4,5)





删时,setup removenode 或者 在维护->从SQL　Server 故障转移群集中删除节点，不能直接在cluster中先删，

---单点测试OK



7.安装 SQL server2008 选择 Add Node to SQL Server failover cluster	　要一个IP


Add Node to SQL Server failover cluster 安装方式也不可以DC上安装，DC和storage在一台上？




在cluster management中右击刚建的SQL Server(MSSQLSERVER)->move this service or application to another node->xxx










----------------
http://technet.microsoft.com/zh-cn/library/ms179530.aspx　
http://technet.microsoft.com/zh-cn/library/ms144259.aspx  命令行安装SQL Server

	
第二节点安装 "cluster share disk available check   failed " 表示内存不足

http://www.supesoft.com/ArticleDisp.asp?ID=4813

1、 私有网卡只安装【TCP/IP协议】，其它去掉。

2、 “【高级】->【DNS】”：【在DNS中注册次链接的地址】需要去掉

3、 “【高级】->【Wins】”：选择【禁用TCP/IP上的NetBIOS】

4、 至于网卡速度，我是没有改，因为自适应网卡似乎没有这个选项


===========windows Server 2008  load balance SQL server2008 

路由器支持多播模式

windows Server 2008  加功能 ->[网络负载均衡]
在网络负载平衡管理器中->聚集->新建





筛选模式
	a:多个主机:
		无相似性:NLB会将第1个请求交由第1部服务器来处理，第2个请求交由第2部服务器来处理,必须将session状态集中储存在state或database server中,
		单一相似性：客户机的服务请求会固定分配到群集内的某一部服务器

	b.单一主机：若选择此选项，该端口范围内的所有请求都将由一台主机来进行处理
	 c. 禁用此端口范围：



http://www.fuancn.cn/html/B/Balanc/20091127/6828.html
Resource Governor 资源调控器

CREATE RESOURCE pool LowPriorityAppspool WITH (MAX_CPU_PERCENT = 20);
CREATE RESOURCE pool MediumPriorityAppspool WITH (MAX_CPU_PERCENT = 60);
CREATE RESOURCE pool HighPriorityAppspool WITH (MAX_CPU_PERCENT = 100);




CREATE WORKLOAD GROUP UnidentnifiedApplications		USING LowPriorityAppspool;

CREATE WORKLOAD GROUP WellBehavedAccessApplications	USING MediumPriorityAppspool;

CREATE WORKLOAD GROUP PoorlyBehavedAccessApplications	USING LowPriorityAppspool;

CREATE WORKLOAD GROUP WellBehavedExcelApplications	USING MediumPriorityAppspool;

CREATE WORKLOAD GROUP CriticalCorporateApplications	USING HighPriorityAppspool;



SELECT * FROM sys.dm_resource_governor_configuration;

ALTER RESOURCE GOVERNOR RECONFIGURE。运行这条命令之后，is_reconfiguration_pending的值就变成0
classifier_function_id的值为0，表示没有分配分类器函数。分类器函数用于决定一个会话应该放在哪个工作负载组中。



SELECT * FROM sys.dm_resource_governor_resource_pools;
SELECT * FROM sys.dm_resource_governor_workload_groups;

APP_NAME()
HOST_NAME()、SUSER_NAME()、SUSER_SNAME()、IS_SRVROLEMEMBER()和IS_MEMBER()。






====================
一、sqlserver2000下的命令行执行
C:>osql -S 服务器名\实例名 -U sa回车会出现下面提示，输入密码。

SQL Browser 侦听 UDP 端口 1434。
JDBC 是1433

SELECT @@servername。


http://msdn.microsoft.com/zh-cn/sqlserver/aa336270
http://msdn.microsoft.com/zh-cn/library/bb500155.aspx	开发
http://msdn.microsoft.com/zh-cn/library/bb510741.aspx	Transact-SQL 参考

//代码示例
create    DATABASE   test   
use test

create table student(id int,name varchar(20));
insert into student(id,name) values(1,'lisi');
insert into student(id,name) values(2,'zhangsan');
insert into student(id,name) values(3,'zhang');


create table student1(id int,name varchar(20));
insert into student1(id,name) values(11,'aa');
insert into student1(id,name) values(22,'bb');


create table score(student_id int ,course_id int ,score decimal)
insert into score (student_id,course_id,score) values(1,1,80.5)
insert into score (student_id,course_id,score) values(2,1,90)
insert into score (student_id,course_id,score) values(3,1,55)

insert into score (student_id,course_id,score) values(1,2,77)
insert into score (student_id,course_id,score) values(2,2,88)
insert into score (student_id,course_id,score) values(3,2,99)

select * from score
select * from student


declare @myid int,@myname varchar(20)
select @myid,@myname
select @myid=id ,@myname=name from student;


go
--select @myid,@myname




declare @n varchar(20)
set @n='lisi'

select * from student where name=@n


if 1=0
	print 'true'
else 
	begin
		print 'false'
	   print 'in begin and end'
	end

declare @i int 
set @i=30;
while @i<40
begin 
	set @i=@i+5
	if @i>35
	  break
	print 'the i='+str(@i) 
end


goto lable
print 'one'
lable: 
	print 'two'

create table #mytemp(id int,name varchar(20));

if not exists(select * from tempdb.dbo.sysobjects where name='##mytemptable')
	create table ##mytemptable(id int,name varchar(20));


declare @tableVar table(id int,name varchar(20));
insert into @tableVar (id,name) values(2,'zhangsan');
select id,name from @tableVar



--under test->Security->Users->zhaojin
--under test->Security->Schemas->myschema
create schema myschema authorization zhaojin
drop  schema myschema -- cascade or restrict无效

--create domain persion_name varchar(20)
select * from student
alter table student modify id real

alter table student  alter   column name varchar(30)




with result(avg_score) as 
(
select avg(score)  from score group by student_id
)
select * from result 


------SQL Server

create procedure proc2 @myid int ,@myname varchar(20) output
as
select @myname=name from student where id=@myid

declare @name1 varchar(20)
exec proc2 2,@name1 output
select @name1


create trigger trig1 on student
for insert 
as 
	if (select count(*) from inserted where inserted.name like 'li%' )>0  --inserted is new ,deleted is old
	begin
		raiserror('can not start with li',10,1)
		rollback transaction
	end

insert into student (id,name) values(33,'lili')




create trigger trig2 on student
instead of delete
as 
	raiserror('can not delete ,due to the instead of delete trigger',10,1)

delete from student where name like 'li%';
select * from student


drop trigger trig3
go 
create trigger trig3 on student
for update
as 
if update (id)
	raiserror('prompt due to update (id) trigger',10,1)

update  student set id=35 where name like 'li%';
select * from student




drop function func1
go
create function func1 (@id int,@name varchar(20) )
returns varchar(30)
--[as] 
begin --have begin
	declare @str varchar(30)
	set @str= str(@id) +' '+ @name
	return @str
end

select dbo.func1(3,'name') 



create function func2 (@name varchar(20) )
returns  table 
--[as] --no begin end
return (select * from student where name like @name+'%')


select * from func2('zhang')





create function func3 ()
returns @my table (id int,name varchar(20))
--[as] 
begin
	insert into @my(id,name ) select id,name from student
	return 
end

select * from func3()





