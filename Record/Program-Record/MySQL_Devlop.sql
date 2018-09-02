
从5.7.21 windows安装版开始  自带samples and Examples 的目录 C:\Program Files (x86)\MySQL\Samples and Examples 5.7\Sample Databases中有 Sakila和World
https://dev.mysql.com/doc/index-other.html MySQL  Sample Database
https://dev.mysql.com/doc/employee/en/ Employees    https://launchpad.net/test-db  https://github.com/datacharmer/test_db

PHPMyAdmin
SQL Diagnostic Manager (www.idera.com)

SQLyog(MySQL GUI & Admin)	windows only
MySQLFront   				windows only
dbForge Studio for MySQL	windows only  debug

  用 employees 示例数据库
  https://dev.mysql.com/doc/employee/en/employees-installation.html
   -- 2001年 工资>80000的员工,及所在部门及领导 ,一个部门会有2个领导
   
  select concat(e.first_name,e.last_name ,' ') as emp_name   ,
        s.salary,s.from_date,d.dept_name   ,
        concat(m.mgr_first_name ,m.mgr_last_name,' ') as mgr_name  
  from    
   salaries  s 
    left join 
  employees  e  on e.emp_no=s.emp_no
     left join  
  dept_emp de on e.emp_no=de.emp_no
     left join
  departments d   on   de.dept_no=d.dept_no 
     left join 
     (
        select  m.dept_no, e.first_name as mgr_first_name , e.last_name as mgr_last_name
        from 
          dept_manager m   
             left join
          employees e on m.emp_no=e.emp_no  -- 一个部门会有2个领导,出2条记录
     ) m on  m.dept_no=d.dept_no
   where s.salary>80000 and s.from_date >'2001'
  --- === Nested Join Optimization 
   --- 简洁写法
  select concat(e.first_name,e.last_name ,' ') as emp_name   ,
        s.salary,s.from_date,d.dept_name    
  from    
  employees  e
     left join 
  ( salaries  s ,  dept_emp de , departments d)  -- 同 ( salaries  s  cross join   dept_emp de  cross join  departments d) -- INNER JOIN 也是一样的
    on (e.emp_no=s.emp_no and  e.emp_no=de.emp_no and de.dept_no=d.dept_no  )
    where s.salary>80000 and s.from_date >'2001'
 --- 简洁写法2
 select concat(e.first_name,e.last_name ,' ') as emp_name    
,  s.salary,s.from_date,d.dept_name 
       
  from    
  employees  e
     left join 
  (   dept_emp de  left join  departments d on     d.dept_no=de.dept_no  left join salaries  s  on s.emp_no=de.emp_no  )   
    on   e.emp_no=de.emp_no
     where s.salary>80000 and s.from_date >'2001'
	 
	 
 --- 简洁写法3
   select concat(e.first_name,e.last_name ,' ') as emp_name   ,
         d.dept_name    
  from    
  employees  e
     left join 
  (   dept_emp de  left join  departments d on     d.dept_no=de.dept_no   )   
    on   e.emp_no=de.emp_no
  -- 同 
  select concat(e.first_name,e.last_name ,' ') as emp_name   ,
         d.dept_name    
  from    
  ( employees  e
     left join 
     dept_emp de      on   e.emp_no=de.emp_no 
  )
  left join  departments d on     d.dept_no=de.dept_no     
 
	
	
---- 每门成绩的前2名  Oracle也通用，Oracle有 dense_rank()
create table sc
(sno int,
cno int,
score int);

insert into sc values (1,1,100);
insert into sc values (2,1,80);
insert into sc values (3,1,25);
insert into sc values (4,1,45);
insert into sc values (5,1,67);
insert into sc values (1,2,25);
insert into sc values (2,2,77);
insert into sc values (3,2,78);
insert into sc values (4,2,69);
insert into sc values (5,2,24);

select  sno,cno,score
from sc r1       
where  
(
  select count(1)
  from sc r2
  where r2.cno=r1.cno 
  and  r1.score <= r2.score
) <=2;  --如条件是=2 ,侧只要第2名

//--每个部门 (年龄/工资) 最大的前2个
 select d.dep_name,e.username,e.age
 from department  d , employee e 
 where e.department_id=d.id
 and  
 (        
        select  count(1) 
        from employee  e2
        where e.department_id=e2.department_id and  e.age <= e2.age 
 ) <= 2 --如条件是=2 ,侧只要第2名
 
 
 
-- 对现有的自连接数据，如员工表有一个上级经理，找出上级的上级到最顶级，(或者找出下级的下级到最低级,像oracle 的 start with ,connect ty )
show variables  like 'max_sp_recursion_depth';

create table sc
(sno int,
cno int,
score int);

insert into sc values (1,1,100);
insert into sc values (2,1,80);
insert into sc values (3,1,25);
insert into sc values (4,1,45);
insert into sc values (5,1,67);
insert into sc values (1,2,25);
insert into sc values (2,2,77);
insert into sc values (3,2,78);
insert into sc values (4,2,69);
insert into sc values (5,2,24);


delimiter //
drop function IF EXISTS getChildLst  //

-- SET GLOBAL log_bin_trust_function_creators = 1;  -- root run
CREATE FUNCTION `getChildLst`(rootId INT)
  RETURNS varchar(1000)
  BEGIN
    DECLARE sTemp VARCHAR(1000);
    DECLARE sTempChd VARCHAR(1000);

    SET sTemp = '$';
    SET sTempChd =cast(rootId as CHAR);

    WHILE sTempChd is not null DO
      SET sTemp = concat(sTemp,',',sTempChd);
     SELECT group_concat(id) INTO sTempChd FROM treeNodes where FIND_IN_SET(pid,sTempChd)>0;
    END WHILE;
    RETURN sTemp;
  END;
  //

select getChildLst(1);//


drop function IF EXISTS  getParentLst  //
CREATE FUNCTION `getParentLst`(rootId INT)
  RETURNS varchar(1000)
  BEGIN
    DECLARE sTemp VARCHAR(1000);
    DECLARE sTempParent VARCHAR(1000);
    declare depth int;
    set depth=0;
    SET sTemp = '';
    SET sTempParent =rootId  ; -- cast(rootId as CHAR)

  --  WHILE sTempChd is not null DO
    WHILE sTempParent !=0 DO
      set depth=depth+1;
      SET sTemp = concat(sTempParent,'/',sTemp);
     -- SELECT group_concat(pid) INTO sTempParent FROM treeNodes where FIND_IN_SET(id,sTempParent)>0;
	  SELECT pid INTO sTempParent FROM treeNodes where  id=sTempParent ;
    END WHILE;
    RETURN  concat('/',sTemp,'$',depth);
  END;
  //

select getParentLst(7); //
select t.*,   getParentLst(t.id)  from treenodes t

select t.*, 
   @tmpStr:=getParentLst(t.id) as res,
  substr(@tmpStr,1,instr(@tmpStr,'$')-1  ) as path,
  substr(@tmpStr,locate('$',@tmpStr)+1  ) as depth
from treenodes t,(SELECT @tmpStr:='') r

delimiter;
delimiter  //


--  带name的

drop function if exists getOrgParentLst  //
CREATE FUNCTION `getOrgParentLst`(rootId INT)
  RETURNS varchar(1000)
  BEGIN
    DECLARE sIdRes VARCHAR(1000);
    DECLARE sIdParent VARCHAR(1000);
    DECLARE sNameRes VARCHAR(1000);
    DECLARE sNameParent VARCHAR(1000);
    declare depth int;
    set depth=0;
    SET sIdRes = '';
    SET sIdParent =rootId  ; -- cast(rootId as CHAR)
    set sNameRes='';
    set sNameParent='';
  --  WHILE sTempChd is not null DO
    WHILE sIdParent !=0 DO
      set depth=depth+1;
      SET sIdRes = concat(sIdParent,'.',sIdRes);
      -- SELECT group_concat(pid) INTO sTempParent FROM treeNodes where FIND_IN_SET(id,sTempParent)>0;
      SELECT pid,nodename INTO sIdParent ,sNameParent FROM treeNodes where  id=sIdParent ;
      SET sNameRes = concat(sNameParent,'/',sNameRes);

    END WHILE;
    RETURN  concat(sIdRes,'$','/',sNameRes,'$',depth);
  END;
  //
select getOrgParentLst(7);


select
   @tmpStr:=getOrgParentLst(t.id) as res,
  substr(@tmpStr,1,instr(@tmpStr,'$')-1  ) as path,
   substring_index(substring_index(@tmpStr, '$', 2  ),'$',-1) as pathname,
  substring_index(@tmpStr, '$', -1  ) as depth
from  treeNodes t, (SELECT @tmpStr:='') r;



--不用 group_concat 实现

CREATE table stu_score(id int ,stu_id int ,score int );
insert into stu_score  values(1,1001,80);
insert into stu_score values(2,1002,81);
insert into stu_score values(3,1001,82);
insert into stu_score values(4,1002,83);

select distinct t.stu_id,
concat(
        (select score from stu_score where stu_id=t.stu_id order by score desc limit 1 )
        ,',',
        (select score from stu_score  where stu_id=t.stu_id order by score asc limit 1)
) as res
from stu_score  t



--------------------------------------使用
\! 和system (linux 下) 是执行系统命令
\. 和source 是执行给定文件的sql
\T tee
\u use
\e edit (linux下)
\q exit 或 quit
\g go
\s status
select * from x \G   行转列

? contents  文档的根
? opt%   表达式形式的帮助
mysql>prompt MySQL \u@\h [\d]>  登录后使用prompt来修改提示符,可也启动加--prompt="MySQL \u@\h [\d]>" 
mysql> select * from myTable \G    -- 以卡片格式显示数据
mysql> pager cat > /tmp/sql_out.txt  把执行输出重定位到文件中
mysql> pager less  ; 执行后再 select 数据较多时就可翻页了
mysql> pager more 
> pager awk -F '|' '{print $6}'  | uniq -c    -- 去除重复的，并统计重复数
		| sort -r  -- 反高排序
		
>show processlist;
>pager 恢复默认输出到stdout
>pager /xxx.sh    加shell脚本

 mysqlslap  命令 MySQL 自动性能测试工具
 
mysql -h host -u user -p < batch-file
mysql -uroot -proot -D nine -e "select * from boss_functions " --default-character-set=utf8 >/root/xx.xx
-D 和--database 都可以,可以用空格或=
-e 和--execute= 都可以,可以用空格或=   执行SQL,使用""引用

如使用关键字做表名,或者字段名,使用 ``引用

数据类型 
	INYINT,SMALLINT,MEDIUMINT,INT 和 BIGINT -- int(5)好像没什么用的
	FLOAT, DOUBLE , DECIMAL	 
	varchar(30)  -- 可以存30个中文 , 字符集是UTF8
	decimal(5,2) -- 精度要求非常高的计算
	float(5,2)  -- 表示一共5位,其中小数2位,也就是整数3位(float,decimal一样的)
	
CHAR ,VARCHAR,BINARY,VARBINARY,BLOB,TEXT,ENUM , SET	 类型
	
 CREATE SCHEMA 是 CREATE DATABASE 的同义词
 
	
CREATE TABLE data_type
(
	my_bit bit, -- 只能0或1, 可以多位 
	my_blob blob,
	my_binary BINARY(3),
	my_enum ENUM('red','orange','black'),
	my_set  SET('a', 'b', 'c', 'd')  -- 一格中有相同值时,保存会去重复
);
一个 SET 列最多有 64 个不重复的值
insert into data_type(my_bit,my_blob,my_binary,my_enum,my_set)
values(1,1,0,'black','a,b,c');
	
text 类型对应的就有tinytext,mediumtext,longtext
	
date 类型没有时间,而datetime 和 timestamp  (如果修改了时区值也会自动变)类型是带日期和时间的
(建表,加列时,列的类型为 timestamp 如不指定default,自动加  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP )
 
 DATETIME 范围是 '1000-01-01 00:00:00' to '9999-12-31 23:59:59'.
 TIMESTAMP 范围是 '1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC.
 
YEAR ,TIME  类型     

如为date 类型，在insert 数据时可以使用格式  d{'2010-10-10'} 来表示一个日期

describe 表名
select database(); 当前用的数据库
show database
show tables 同 select table_name from information_schema.TABLES where table_schema='db_name'
show variables like 'group_concat_max_len'
 select user() 当前登录用户,或者  connect 命令
version()
PI()
sin(..)
 SELECT ROUND(150.126,2) ; --保留两位,四舍五入
 truncate(150.126,2) -- 截去,保留两位小数  150.12  
 floor(11.85); --去小数,变整
 
---日期函数
show variables like '%date_format%'
show variables like '%datetime_format%'

now() --当前日期和时间都带的
CURRENT_DATE  和 CURRENT_DATE() 是  curdate()的别名,是带日期
select CURRENT_TIMESTAMP ;

mysql,oracle 都有 current_date 和 current_timestamp
 
select date_format(now(),'%Y-%m-%d  %H:%i:%s') 
SELECT STR_TO_DATE('01,5,2013 12:22:32','%d,%m,%Y %H:%i:%s');

select ("11"+0)*3  // 33  字符向数字转换
SELECT CAST(123 AS CHAR); 
SELECT CAST("+111" AS UNSIGNED); 
  
select last_day(curdate())  --指定日期的当月最后一天的日期,如有的没有31日
select DATE_ADD(current_date,interval -day(current_date)+1 day) 当月第一天
select MONTH('1998-02-03');

date_add(now(), interval -1 day) --前一天
ADDDATE 是  date_add的别名

select DATE_ADD(DATE_ADD(current_date,INTERVAL 18 HOUR) ,INTERVAL 30 MINUTE) 
或者
select DATE_ADD(current_date,INTERVAL '18:30' HOUR_MINUTE);
select DATE_ADD(current_date,INTERVAL '18:30:00' HOUR_SECOND);

 select   DATE_ADD(date_format( last_day(DATE_SUB(current_date,interval 1 month))
								,'%Y-%m-%d')
                  , INTERVAL '18:30' HOUR_MINUTE )     

				  
select DATE_SUB(now(), interval 1 month)  --前一个月


SELECT FROM_DAYS(736153);  
select TO_DAYS('2015-07-07');
select TO_DAYS(now()); --返回从0年开始到指定日期的天数
SELECT To_Days(end_time)-To_Days(start_time)　 --两个日期 相差多少天 
select datediff('2008-08-08', '2008-07-30'); -- 9 第一个减第二个 
SELECT TIMESTAMPDIFF(MONTH,'2003-02-01','2003-05-01'); -- 是3 后面 减前面的 ,是按天,不是按月
SELECT TIMESTAMPDIFF(MONTH,'2003-02-02','2003-05-01'); ---是2 
SELECT PERIOD_DIFF('200401','200312')  -- 会自动转换为日期,相差的月数,日期只能精确到月,
	
SELECT PERIOD_DIFF(200802,200703); -- 第一个日期 减 第二个日期 相差多少个月
 
SELECT UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) as t;  -- 两日期/时间之间相差的秒数 　
SELECT TIME_TO_SEC(now())
 
 
 
SELECT FROM_UNIXTIME(UNIX_TIMESTAMP(current_timestamp));

---字串函数
varchar(20)的含义是20个中文或者英文字符,即中文也是占一个,可能是由于字符集是utf8原因
char_length('') 是字符数,
length('') ;是字节数,中文占3个字节,如果参数是一个数字,是按字符长度计


REPLACE(str,from_str,to_str)

SUBSTRING(str,pos)
SUBSTRING(str FROM pos) 
SUBSTRING(str,pos,len),pos从1开始
subString(date_time2,1,13)
 

select locate('$','/1/3/6/7/$4') //返回10 找字符下标

select substr('/1/3/6/7/$4',1,instr('/1/3/6/7/$4','$')-1)
select substr('/1/3/6/7/$4',locate('$','/1/3/6/7/$4')+1)

select substring_index('www.sqlstudy.com.cn', '.', 2);  -- 截取第二个 '.' 之前的所有字符。  
select substring_index('www.sqlstudy.com.cn', '.', -2);  -- 截取第二个 '.' （倒数）之后的所有字符。 

 select
   @tmpStr:='/1/3/6/7/$/a/b/c$4' as res,
   substr(@tmpStr,1,instr(@tmpStr,'$')-1  ) as path,
   substring_index(substring_index(@tmpStr, '$', 2  ),'$',-1) as pathname,
  substring_index(@tmpStr, '$', -1  ) as depth
from  (SELECT @tmpStr:='') r

 
CHARACTER_LENGTH(group_concat(dest_mobile  SEPARATOR  ';'))/14 
GROUP_CONCAT(busiType order by sTerm asc SEPARATOR ',')  中可加order by ,

SELECT FIND_IN_SET('b','a,b,c,d');
-> 2

SELECT INSTR('foobarbar', 'bar');
-> 4

SUBSTR() 是 SUBSTRING 的别名
right(str,len); -- 从右向左取最多长度的字串
RPAD(str,len,padstr)
RTRIM(str) -- 去右边的空格

REVERSE('abc');-- 返向
UPPER('Hej');--大写

select 'David!' like 'David_'   -- _是通配
select 'David!' like '%D%v%'  -- %是通配
select 'David_' like 'David\_'  -- \转义为普通字符
select 'David_' like 'David|_' escape '|'

 SELECT 'Monty!' REGEXP '.*';
 SELECT 'new*\n*line' REGEXP 'new\\*.\\*line';
  SELECT 'a' REGEXP 'A', 'a' REGEXP BINARY 'A';
-> 1 0
  SELECT 'a' REGEXP '^[a-d]';
  1
 
 select rand() 0...1 间随机取数
 select * from t order by rand(); //随机排序
----
 
 CREATE TABLE `test1` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(10) default NULL,
  `modifyTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB, AUTO_INCREMENT=12 ,DEFAULT CHARSET=utf8

SELECT last_insert_id (); -- 是对 auto_increment的列,但不知道哪个表的,是对连接相关,
	-- 如果insert时手工指定了auto_increment的列的新值,那计数就从刚刚指定的新值开始计数,建立数据类型加UNSIGNED
当表为MyISAM 并且 auto_increment在第二列,它的取值是在第一列的值相同时做计数
insert select 和 load file 会对insert产生阻塞(因表相关的排它锁,范围是语句因不知道行数),分布式的全局唯一递增（不保证连续）

设置自增ID的开始值
alter table t auto_increment = 3;
alter table users modify column id  int auto_increment  primary key ; -- modify column 或者 modify

alter table users drop  primary key ;
alter table users add  primary key (user_id,class_id)


alter table 表名　engine =innodb
show create table 表名
SHOW CREATE DATABASE bugs
修改mysql.ini文件
在skip_innodb 加#注释
default-storage-engine=innodb去注释


delimiter ;为结束符号



insert into table_one(id) values(1),(2)一次插入多条记录
mysql 用 limit n  来实现top n 技术
select * from customers order by id  limit  2,1;从第3行开始,取1行
limit 0,1 是从第一行取1行

如limit 后只有一个数字,只显示前n 行 (top n)


limit 后不能用表达式 ,下面可以,但如果SQL复杂就难用了
set @sql=CONCAT("select * from myTable where status='2' limit ",2-1,',1');  
select @sql;
prepare stml from @sql; 
execute stml;

limit 中不用order by 速度会快
limit 官方文档上说要order by ,sort_buffer_size.

如有
select * from v(
	select * from a 
		union 
	select * from a
)limit 2000,1000  这样和不使用limit 执行时间一样

要这样用
( select * from a  limit 2000,1000 )
	union 
( select * from a limit 2000,1000  )


-- limit分页性能问题
select *
from employees 
where birth_date >'1952-02-01' 
limit  300000 ,24 -- 216 ms

id 顺序+1的好办 
-- 511 ms 更慢？？？？
select *
from employees
where  birth_date >'1952-02-01'
 and emp_no in
(
  select emp_no
  from employees
  where birth_date >'1952-02-01'
)
limit  300000 ,24  --  limit不能在子查询中

-- 223ms 
-- 要求id是递增的
select * from employees 
where birth_date >'1952-02-01' and
	emp_no >= (select emp_no from employees 
			where birth_date >'1952-02-01' 
			limit 300000,1) 
limit 24;
--   可以解决mybatis <collection> 式查询,麻烦最好换做法
select * from employees 
where birth_date >'1952-02-01'
and
	emp_no >= (select emp_no from employees 
			where birth_date >'1952-02-01' 
			limit 300000,1)
	and 
	(
		not exists(
			select emp_no from employees 
			where birth_date >'1952-02-01' 
			limit 300010,1
		)
		or
		emp_no < (
			select emp_no from employees 
			where birth_date >'1952-02-01' 
			limit 300010,1
		)
		
	) 


多表关联的条件要加索引,on后面的

java 中
int page=1;
int skipRow=(page-1)*CommonConstant.PAGE_SIZE;



SET [SESSION | GLOBAL] group_concat_max_len = val;是对group_concat()函数的返回的字符串的结果的最大长度，默认1024
约束的被 max_allowed_packet 参数 Default value is 16MB

select concat('a','b',123) //字串拼接,可用字段

select @@max_allowed_packet;

SET [SESSION | GLOBAL] group_concat_max_len = 3600000;	// 30万条短信
//  60万条短信	
set GLOBAL max_allowed_packet= 7200000;		
SET GLOBAL group_concat_max_len = 7200000;

show variables like 'storage_engine'  
 
 
show variables like 'character_set_%';
show variables like 'autocommit'  同session的
show  global variables like 'autocommit' 
show  session variables like 'autocommit' 
set global  autocommit=off      有些是 readonly的
set session  autocommit=off    


START TRANSACTION;  或者用  BEGIN  END
--
COMMIT; -- rollback

 BEGIN WORK; -- 是  START TRANSACTION;   别名
 
rollback WORK;  -- COMMIT WORK;

set names gb2312 或是gbk  就可以插入中文了（只对当前session有效)
set names gbk ;设置 mysql客户端终端字符集为gbk,如cmd中

于下三个相同
SET character_set_client = gb2312;
SET character_set_results = gb2312;
SET character_set_connection = gb2312

mysql 的表和列都有自己的字符集

只要是对的用以下命令都可以正常显示
alter table xxx CONVERT TO CHARACTER SET charset_name 
  | [DEFAULT] CHARACTER SET charset_name 
alter table test modify column name varchar(20) character set 'utf8';
  

select task_id,  count(*) from mt_sms  where user_id=257 and date_time2>'2007-12-18' 
group by task_id
WITH ROLLUP
-- 不能和order by 一起使用，(group by 2 列)会出现一个NULL值的聚合

聚合函数  bit_and  ,bit_or
=============

 -- mysql ,db2 都支持在select 的字段级中增加子级select ,关联外级表 (对一每记录,单独做一次查询的情况)
select dep_name,
(
        select max(raise_salary)  from employee e
        where  e.department_id=d.id
) as max_raise
from department d



//子查询是在父每移动一行，就执行一次子查询
SELECT
    t.loanAcNo,
    t.bal,
    t.loanAmount,
    (
        SELECT
            SUM(s.rCapi)
        FROM
            t_return_state s
        WHERE
            s.loanAcNo = t.loanAcNo
     ) AS returnAmount
FROM t_loan_ledger t
WHERE
    bal <> t.loanAmount -
    (
        SELECT
            SUM(s.rCapi)
        FROM
            t_return_state s
        WHERE
            s.loanAcNo = t.loanAcNo
     );  --  也可在where 中


---update 关联多表  
update boss_customer set total_balance=(select money_remain from users where user_id=boss_customer.user_id) 
where exists(select 1 from users where user_id=boss_customer.user_id)
//子查询是在父每移动一行，就执行一次子查询
 
  
UPDATE items,month SET items.price=month.price
WHERE items.id=month.id;

-- update 关联多表 方式2
 update customers a  
set city_name=(select b.city_name from tmp_cust_city b where b.customer_id=a.customer_id)
where exists (select 1
from tmp_cust_city b
where b.customer_id=a.customer_id
) 

这种速度慢
update credit_his.pcc_hlw_customer_his c
set c.from_sys=(
                 select max( subString(l.listId,1,3) )  from credit_acct.t_loan_ledger l where  subString(c.cus_id,3) =l.custId
         )
where exists(
        select 1   from credit_acct.t_loan_ledger l where  subString(c.cus_id,3) =l.custId  -- 这里没更多条件，可以去除这部分
)  


  
优化速度
 update creditpush.pcc_hlw_customer c ,
(       
        select custId,max( subString(listId,1,3) ) as from_sys 
         from credit_acct.t_loan_ledger  
         group by custId
) l
set c.from_sys=l.from_sys
where  subString(c.cus_id,3) =l.custId


-- 多表关联更新
update creditpush.pcc_hlw_loan c
	left join 
	credit_acct.t_loan_ledger l   on  c.loan_id =l.loanAcNo
set c.from_sys=subString(l.listId,1,3) ;
 或者
update creditpush.pcc_hlw_loan c , credit_acct.t_loan_ledger l  
set c.from_sys=subString(l.listId,1,3) 
 where  c.loan_id =l.loanAcNo
 
  
 
无论什么方式 ,都是update的子句中,  都会锁两张表

mysql 不支持 update一个表时，在where中又查询该表
update tableA 
set name='x'
where id in 
(
	select id from tableA  //这里不能再次使用 tableA
)


---

alter table boss_agent add [column] total_balance(新列) int after balance(老列)  [或者first]

alter table boss_agent modify [column] total_balance bigint comment '' after balance  
ALTER TABLE  customer_his MODIFY  [column] create_time timestamp DEFAULT current_timestamp ;  -- 修改整列中带默认值,值是函数只能这样用

ALTER TABLE users MODIFY  [column] create_time timestamp DEFAULT now() COMMENT '创建时间'; --必须指定类型
 -- 如列为 timestamp 类型,不设置default,也自动加 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,当 add column时也会加值
alter table users MODIFY column create_time timestamp null comment '创建时间' ; -- 会改default为null

alter table boss_agent change [column] total_balance  total_balance bigint after balance  -- change [column] 只能用重命名
alter table boss_agent ALTER [column] total_balance {SET DEFAULT literal | DROP DEFAULT} -- ALTER [COLUMN]  只能用于drop default,     SET DEFAULT 测试无效????
 
 
 
 CREATE TABLE inventory (
 )ENGINE=InnoDB DEFAULT CHARSET=utf8;  --或者  DEFAULT character set 'utf8'  --utf8可有可无''
 
 
GROUP_CONCAT(DISTINCT test_score  ORDER BY test_score DESC SEPARATOR '; ')
SET [SESSION | GLOBAL] group_concat_max_len = 3600000;// 30万
set GLOBAL max_allowed_packet= 3600000;
SET GLOBAL group_concat_max_len = 3600000;

/etc/my.cnf文件中[mysqld]中加入
max_allowed_packet=1M

fulltext 索引只用于 MyISAM
unique 索引
普通索引
SPATIAL 索引(5.7新功能 MyISAM 和 InnoDB都支持, 空间数据类型如point和geometry等,列必须非NULL ) 在where 中  MBRContains() 或者 MBRWithin() , 会建立一个  R-tree 索引
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX  ,或者 alter table t1 add index ind (col)
USING {BTREE | HASH}  //InnoDB 和 MyISAM 都是BTree

MyISAM 引擎中 ft_min_word_len和ft_max_word_len 表示indicate  minimum and maximum word length for FULLTEXT indexes
InnoDB 引擎中  innodb_ft_min_token_size 和 innodb_ft_max_token_size  

B-Tree 索引  LIKE '%string%'  , string多于3个字符 MySQL  Turbo Boyer-Moore 算法搜索会更快


show variables like 'group_concat_max_len'
show variables like 'max_allowed_packet'


SHOW VARIABLES  显示所有的变量
SHOW [GLOBAL | SESSION] VARIABLES [LIKE 'pattern'
8bit(位)=1Byte(字节)
1024Byte(字节)=1KB   一个英文字的空间    2K是一个汉字



CONCAT_WS(separator,str1,str2,...)  它与 group_concat 不同的是 不会对有 null的值而返回null

 
SELECT student_name,
    ->     GROUP_CONCAT(test_score)//聚合时会把字串相加，（concat）
    ->     FROM student
    ->     GROUP BY student_name;
	
MySQL在使用Group by时注意 , 没有出现在group by 中的字段,在 select 中也没有聚合函数的字段,也可以出现在select中,并且取第一条记录,这点和 Oracle 不同,注意!!!


SELECT student_name,
    ->     GROUP_CONCAT(DISTINCT test_score
    ->               ORDER BY test_score DESC SEPARATOR ' ')--用空格分隔
    ->     FROM student
    ->     GROUP BY student_name;
select GROUP_CONCAT(m.dest_mobile SEPARATOR ';') ,u.username from mt_sms m ,users u where u.user_id=m.user_id group by u.username,DATE_FORMAT(m.date_time2, '%Y-%m-%d')







size ENUM('x-small', 'small', 'medium', 'large', 'x-large') --ENUM 数据类型
CREATE TABLE myset (col SET('a', 'b', 'c', 'd'));  --SET 数据类型

INSERT INTO myset (col) VALUES
  ('a,d'), ('d,a'), ('a,d,a'), ('a,d,d'), ('d,a,d'); -- 插入5行 ,查询所有的只有a,d 即不会有重复的
SHOW ERRORS;
SHOW WARNINGS;

select @@tx_isolation;　默认 REPEATABLE-READ
可设置的值为
READ-UNCOMMITTED
READ-COMMITTED
REPEATABLE-READ
SERIALIZABLE



show variables like 'tx_isolation%';　 同 session 

set global/session transaction isolation level read committed   -- read uncommitted
set global/session  transaction isolation level  repeatable read 　 --  SERIALIZABLE　




create table new_table comment='' select * from old_table where 1=2  -- 复制表  as 可有可无  , 会丢失 索引和主键 , 但有null约束,不会带default
create table temp as SELECT * FROM table1  -- 复制表结构 和 数据  
insert into  temp   select * from  table1  -- 复制表数据




create temporary table xx  与服务器交互后自动删除


MySQL 删除重复记录方法1,是重复的全删
delete from mytable where id in
( 
 select id from mytable
 group by main_id,sub_id having count(*)>1 
);

MySQL 删除重复记录方法2,不用in,重复的只要一条
delete a from user a left join 
(
  select userId from user
  group by user_Name,password
)b
on a.userId=b.userId 
where b.userId is null;

DROP TABLE IF EXISTS `myTable`;

--表,字段加comment
CREATE TABLE groups( 
  gid INT PRIMARY KEY AUTO_INCREMENT COMMENT '设置主键自增',
  gname VARCHAR(200) COMMENT '列注释'
) COMMENT='表注释';
--修改comment
ALTER TABLE groups COMMENT '修改表注释';
ALTER TABLE groups MODIFY COLUMN gname VARCHAR(100) COMMENT '修改列注释'; -- 不能只修改COMMENT
--查看comment
SHOW FULL COLUMNS FROM groups
SHOW  CREATE TABLE groups
SELECT table_name,table_comment  FROM information_schema.tables  WHERE table_schema = 'test' AND table_name ='groups'


select  1+2  from dual where 0=2;  //也有dual

RENAME TABLE current_db.tbl_name TO other_db.tbl_name;
没有重命名数据库的功能 

select count(expr) from mytable  //只报expr 不是null做计数,count(*)全要
--------语法

高---低
本地变量 --  参数--表的列
内块本地变量 ---外块本地变量 

 存储过程日志输出 select '日志' ;

mysql> PREPARE stmt1 FROM 'SELECT SQRT(POW(?,2) + POW(?,2)) AS hypotenuse';
mysql> SET @a = 3;
mysql> SET @b = 4;
mysql> EXECUTE stmt1 USING @a, @b;


mysql> SET @s = 'SELECT SQRT(POW(?,2) + POW(?,2)) AS hypotenuse';
mysql> PREPARE stmt2 FROM @s;
mysql> SET @a = 6;
mysql> SET @b = 8;
mysql> EXECUTE stmt2 USING @a, @b;

DEALLOCATE PREPARE stmt2;

while x>3 do

....
end while
---
repeat
..
until x>3
end repeat
--
label1: LOOP
....
    SET p1 = p1 + 1;
    IF p1 < 10 THEN ITERATE label1; END IF;
    LEAVE label1;
END LOOP label1;

 
 所有与null的操作运算都返回null

IF(expr1,expr2,expr3)
如果 expr1 是真 (expr1 <> 0 and expr1 <> NULL) 返回expr2 否则返回expr3 
if(p.industryTypeId is null,'Z',substr(p.industryTypeId,1,1)) //用is null

if()中的逻辑判断可以用 && , ||, ! 也可用 and ,or ,not 也可混用

IFNULL(expr1,expr2)
如果 expr1不是null返回 expr1;否则返回 expr2
如果 expr1是null 返回 expr2,否则返回 expr1

create   table tmp (a int, b int ,c int); 
insert into tmp values(1,2,3);

 


select sum(case when c = '1' then A else B end) from tmp   --case when x
SELECT CASE WHEN 10*2=30  and 2*2=4 THEN '30 correct' -- 可以or 或者 and ,&&
   WHEN 10*2=40 THEN '40 correct'
   ELSE 'Should be 10*2=20'
END;

SELECT CASE WHEN 10*2=30  && 2*2=4 THEN '30 correct'  
   WHEN 10*2=20 THEN  case  when 10*2=30 then 'sub' else 'sub not'end    -- case when 可嵌套
   ELSE 'Should be 10*2=20'
END as name;


SELECT CASE 10*2   
   WHEN 20 THEN '20 correct'
   WHEN 30 THEN '30 correct'
   WHEN 40 THEN '40 correct'
END;


case x when null then 0  --不行,null的判断要用 is null,不是=null
end

case when x  is null then 0  --OK
end
  
case when end 也可以用在select 的where 部分

case  when l.prodId='100' or l.prodId='200'  then '学生'   end  -- OK
case l.prodId when '100' or '200' then '学生'   end    -- or 这样用不行的 
        
无论是case x when 1 end 还是case when x=1 then 都是相当于else if 或 java 的带break 的 switch,即最多一个成立
		
MySQL中实现rownum
select @rownum:=@rownum+1 AS rownum,   
	p.class_id
from mytable t,(SELECT @rownum:=0) r
order by t.class_id asc;
   
  
----PROCEDURE function 

create function xx(a1 varchar(20)) returns varchar(20) -- 与 oracle不同

mysql> delimiter //   ## 也可以用  \d //
mysql> CREATE PROCEDURE simpleproc (OUT param1 INT)
  BEGIN
  SELECT COUNT(*) INTO param1 FROM t;
  END//

 
call simpleproc(@y);


delimiter  //
drop  FUNCTION if exists hello //
CREATE FUNCTION hello (s CHAR(20))
  RETURNS CHAR(50) DETERMINISTIC  -- Function dbForge一定要有DETERMINISTIC
  BEGIN
    DECLARE res VARCHAR(30);
   SET res =CONCAT('Hello, ',s,'!');
  RETURN  res;
    END;//
delimiter  ;


delimiter  //
drop  PROCEDURE if exists  demo_inout_parameter ; //
CREATE PROCEDURE demo_inout_parameter (inout cnt int)
BEGIN 
  SET cnt=cnt+1; 
 END//
delimiter  ;

set @cnt=10
call demo_inout_parameter(@cnt);
select @cnt

 SHOW CREATE PROCEDURE   demo_inout_parameter;
SHOW   PROCEDURE   status;

 CREATE PROCEDURE p24 ()
BEGIN
DECLARE `Constraint Violation`
CONDITION FOR 1582;    --1582错误代码是前面的如是（）里的要换成sqlstate '32000'
DECLARE EXIT(continue) HANDLER FOR
`Constraint Violation` ROLLBACK;　　--名字也可替换为1582 或　sqlstate '32000' 　后面要做的一些语句
START TRANSACTION;
INSERT INTO t2 VALUES (1);
INSERT INTO t2 VALUES (1);
COMMIT;
END;


CREATE DEFINER=`root`@`localhost` PROCEDURE `insertLog`()
begin
declare  i int default 1;
while i<10 do
insert into boss_log(agent_name,oper_type,function_type) values('xxx','???????','?????????');
set i=i+1;
end while;
end;



 
CREATE PROCEDURE curdemo()
BEGIN
	DECLARE done INT DEFAULT FALSE;
	DECLARE a CHAR(16);
	DECLARE b, c INT;
	DECLARE cur1 CURSOR FOR SELECT id,data FROM test.t1;   -- cursor 中的select不能有into
	DECLARE cur2 CURSOR FOR SELECT i FROM test.t2;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	OPEN cur1;
	OPEN cur2;
	read_loop: LOOP
		FETCH cur1 INTO a, b;
		FETCH cur2 INTO c;
		IF done THEN
			LEAVE read_loop;
		END IF;
		IF b < c THEN
			INSERT INTO test.t3 VALUES (a,b);
		ELSE
			INSERT INTO test.t3 VALUES (a,c);
		END IF;
	END LOOP;
	CLOSE cur1;
	CLOSE cur2;
END;

批量删表,只能通过linux shell来做
echo "select concat('drop table ',  table_name ,';') from information_schema.TABLES where TABLE_SCHEMA ='acctbak';" | mysql -umysql -pmysql -h  10.1.5.226 | sed -n '2,$p' |  mysql -umysql -pmysql -h  10.1.5.226 -Dacctbak
 
-------------trigger 
show triggers;

--sakila-schema.sq 示例代码  可 (BEFORE) AFTER INSERT   , AFTER DELETE 
DELIMITER ;;
CREATE TRIGGER `upd_film` AFTER UPDATE ON `film` FOR EACH ROW 
BEGIN
    IF (old.title != new.title) or (old.description != new.description)
    THEN
        UPDATE film_text
            SET title=new.title,
                description=new.description,
                film_id=new.film_id
        WHERE film_id=old.film_id;
    END IF;
  END;;
  
 -----view
CREATE VIEW customer_list
AS
SELECT  ... from ... where ...  -- 视图中的查询不能有子查询
;

  

-------------Event
mysql定时每隔一分钟执行一次语句
CREATE EVENT e_totals
       ON SCHEDULE AT '2006-02-10 23:59:00'
        DO INSERT INTO test.totals VALUES (NOW());

CREATE EVENT e_hourly
    ON SCHEDULE 
      EVERY 1 HOUR
    COMMENT 'Clears out sessions table each hour.'
    DO
      DELETE FROM site_activity.sessions


CREATE EVENT myevent
    ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR
    DO
      UPDATE myschema.mytable SET mycol = mycol + 1;
	
ALTER EVENT myevent
    DISABLE;


show events;
show  variables like 'event_scheduler';
SET GLOBAL event_scheduler = 1; 

-------------

alter table myTable drop primary key  ; -- 如是auto_increment的不能删主键，好像也没办法只删auto_incremnt,但可删列
	   
drop index INX_depId on myTable;
alter table myTable drop index INX_depId;

CREATE INDEX part_of_name ON customer (name(10)); 可以只对前几个字符做索引
create UNIQUE index INX_PK  on myTable(dep_id,emp_id)  using BTREE
mysql 不支持函数索引,如 subString(l.listId,1,3) 

innodb 和 MyISAM 只支持 BTREE 索引

因为MySQL不支持FULL JOIN,下面是替代方法 left join + union(可去除重复数据)+ right join

SHOW INDEX FROM mytable FROM mydb;
SHOW INDEX FROM mydb.mytable;  查询有哪些索引,主键,
show keys from mydb.mytable; 
 
show global status like 'Innodb_row_lock_current_waits' 当前正在等待锁定的数量
show  processlist 显示当前正在执行SQL语句

set session innodb_lock_wait_timeout=50  默认50秒 行级锁的超时时间
show global status like 'Innodb_row_lock_waits' -- 发生行级锁等待的次数

select  后可加 LOCK IN SHARE MODE   , 另一个session可以读,如要写必须在这个事务结束后
两个都是要在事务中才有效果
 select @@tx_isolation;
 --  表级锁
	UNLOCK TABLES;
-- 行级锁
select  后可加 for update 对innoDB的表行级锁 ,也对索引加锁,和 tx_isolation 有关, 

    select * from myTable for update 如不加where条件就是表级锁,不能 insert ,注意!!!
	
查询哪些表,在锁中(行级锁)	show OPEN TABLES where In_use > 0;




SET autocommit=0;
show session variables where VARIABLE_NAME='autocommit';

REPEATABLE-READ
Session-1 查询
Session-2 查询修改了,已经提交，Session-1 再查还是看到数据不变，即可以重复读
如同一记录Session-1做了修改未提交， Session-2做了修改会锁等Session-1，如Session-1提交，Session-2等完成提示更新为0条，查询查件也未变
如不是同一记录也是锁等，因条件未做索引,就是表锁, 注意!!! ,新版本MySQL-5.7.19就是这样,MySQL做的差的,如条件是索引就是行级锁

SERIALIZABLE 同一条记录 Session-1 查询了也加锁， Session-2做不可以改(REPEATABLE-READ可以)，



-- 

强制索引
select * from table force index(PRI,ziduan1_index) limit 2;(强制使用索引"PRI和ziduan1_index")
--也可用 use index 只要放表后就可以，join也行
select * from mytable 
left join 
youTable  use index(PRI,ziduan1_index)
		on xx=yy

-- 也可加  USE index for join(PRIMARY) ,还有 for order by  和 for group by 
select * from mytable 
left join 
youTable  USE index for join(PRIMARY)
		on xx=yy
		
		
MySQL性能注意问题，如果left join 连续使用两个关联三张表 (最后面的大表又没有任何过滤条件时)，会导致索引失效，加 force index 无用
(看 explain select ... 是否有 Range checked for each record (index map: 0x1) 的说明,
时也没有这个说明,加了包含所记录的条件后,type的值由ref变到All ,rows值变多了,说明rows的参考价值失效了???)

解决方法，要么使用临时视图方式做成嵌套查询,要么表加过滤条件,可包含所记录的条件, 也可考虑使用 inner join 


关联三张表 
其中一个表(可能最后一个) 有个关联条件, 建立复合索引有用,看Cardinality的值变小,影响join( 未少时使用analyze table 无用???) (但还是很慢??? ), 如建立两个独立地索引无用

count(*) 可以保证数据正确,如果count(col) 如果某行col的值为null不会被计算,MySQL和Oracle都是这样的


select sleep(3);  //单位秒


select replace(UUID(),'-','')
	
	
	
SET @skip=1; SET @numrows=5;
PREPARE STMT FROM 'SELECT * FROM tbl LIMIT ?, ?';   
EXECUTE STMT USING @skip, @numrows;

---- queryLargeTable.sql

delimiter ;
drop table if exists res; 
create table  res(name varchar(80),cnt int);  
drop PROCEDURE queryLargeTable ; 
delimiter //

CREATE  PROCEDURE queryLargeTable ( IN  dbName varchar(20) )
BEGIN   
		DECLARE done INT DEFAULT FALSE;
		 
		DECLARE tableName varchar(40);
		DECLARE cur1 CURSOR FOR select table_name  from information_schema.TABLES where table_schema=dbName;
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
		OPEN cur1;
		read_loop: LOOP
				FETCH cur1 INTO tableName; 
				IF done THEN
						LEAVE read_loop;
				END IF; 
				SET  @cnt = 0;
				SET  @tbl_name = CONCAT(dbName,".", tableName);
				SET @STMT := CONCAT("SELECT COUNT(*) INTO @cnt FROM ", @tbl_name, ";");  -- 表名变量
				  PREPARE STMT FROM @STMT;
				  EXECUTE STMT; 


				IF @cnt > 100 THEN
				   insert into res values( @tbl_name,@cnt);
				END IF;
				
		END LOOP;
		CLOSE cur1;		
 END//
 delimiter ;
call queryLargeTable('AKAPP');
select * from res;
  
  
 --- JSON functions  是 MySQL 5.7.8 新加的
	