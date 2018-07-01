


#sqlplus pin/pin@192.168.1.66:1521/orcl  OK
#sqlplus pin/pin@//192.168.1.66:1521/orcl  OK 

如使用关键字做表名,或者字段名,使用 ""引用

自定义数据类型--

 CREATE   OR   REPLACE   
  TYPE   "STRINGS_TABLE"     is   table   of   varchar2(500)   
  /

create or replace type addresstype
   as object
   (
   province varchar(20),
   city varchar(30),
  details varchar(40)
    ) ;
/
create table studentobj
   (
   stuname varchar(10),
   stuaddress addresstype
    );

insert into studentobj values('Tom',addresstype('河北省','邢台','南宫'));

to_date()----------
insert into student values(1,'张三',to_date('2007-02-05 05:00:30','yyyy-mm-dd hh24:mi:ss'),80);

YYYY-Q //季度

 
grant resource to hr 开发权限


with别名 as（select ..）当视图，??????????????????????????
 
drop user myuser cascade
 


B树索引：在B树的叶节点中存储索引字段的值与ROWID ,包括 唯一索引 , 不唯一索引

非聚簇索引	 顺序与数据物理排列顺序无关,叶节点是索引节点,有一个指针指向对应的数据块。

 一个表最多只能有一个聚簇索引
聚簇索引	 顺序就是数据的物理存储顺序,叶节点是数据节点.
如果一组表有一些共同的列，则将这样一组表存储在相同的数据库块中

CREATE CLUSTER emp_dept_cluster(deptno number(6))SIZE 1024; 
CREATE INDEX emp_dept_cluster_index ON CLUSTER emp_dept_cluster; 
 CREATE TABLE dept1(
               deptno NUMBER(6) PRIMARY KEY,
               dname VARCHAR2(50)
               )CLUSTER emp_dept_cluster(deptno);
 
 CREATE TABLE emp1(
               empno NUMBER PRIMARY KEY,
               ename VARCHAR2(50),
               sal NUMBER(5,2),
               deptno NUMBER(6),
               FOREIGN KEY(deptno) REFERENCES dept1(deptno)
        )CLUSTER emp_dept_cluster(deptno);
 这两个表的数据实际上存放在一个位置上
 
 

create table t as select username,password from dba_users;
create index i_t on t(username);
set autotrace trace explain
   
select * from t where username='SYSMAN';
 
查执行计划
EXPLAIN PLAN  for   select /*+ index(t i_t) */ * from t where username='SYSMAN';     --强制使用索引 , INDEX(表名,索引名称) 
-- commit;
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);  
 
Oracle 默认是 B-Tree (BTree) 索引
索引创建策略 
1.导入数据后再创建索引 
2.不需要为很小的表创建索引 
3.对于取值范围很小的字段（比如性别字段）应当建立位图索引 
4.限制表中的索引的数目 
5.为索引设置合适的PCTFREE值 
6.存储索引的表空间最好单独设定 

创建不唯一索引 
create index emp_ename on employees(ename) 
tablespace users 
storage(......) 
pctfree 0; 

默认是B-Tree索引

创建唯一索引 
create unique index emp_email on employees(email) 
tablespace users; 

 当约束列上没有索引时,在创建unique constraint 时，oracle 会自动创建unique index，并且该索引不能删除，当删除unique constraint 时，unique index 会自动删除。
	列上有索引时，在创建unique constraint时，Oracle 会重用之前的索引

	alter table MyTable add constraint U_MyTable_uid  unique (uid);
 
 alter TABLE  table_name ENABLE CONSTRAINT constraint_name; 也可DISABLE 
 
创建位图索引 
create bitmap index emp_sex on employees(sex) 
tablespace users; 

创建反序索引 
create unique index order_reinx on orders(order_num,order_date) 
tablespace users 
reverse; 

创建函数索引(函数索引即可以是普通的B树索引，也可以是位图索引) 
create index emp_substr_empno 
on employees(substr(empno,1,2)) 
tablespace users; 

修改索引存储参数(与表类似，INITIAL和MINEXTENTS参数在索引建立以后不能再改变) 
alter index emp_ename storage(pctincrease 50); 

由于定义约束时由oracle自动建立的索引通常是不知道名称的，对这类索引的修改经常是利用alter table ..using index语句进行的,而不是alter index语句 

利用下面的语句将employees表中primary key约束对应的索引的PCTFREE参数修改为5 
alter table employees enable primary key using index pctfree 5; 

清理索引碎片 
1.合并索引(只是简单的将B树叶结点中的存储碎片合并在一起，并不会改变索引的物理组织结构） 
alter index emp_pk coalesce; 

2.重建索引(不仅能够消除存储碎片,还可以改变索引的全部存储参数设置，并且可以将索引移动到其它的表空间中,重建索引 
实际上就是再指定的表空间中重新建立一个新的索引,然后删除原来的索引) 
alter index emp_pk rebuild; 

删除索引 
drop index emp_ename; 

如果索引中包含损坏的数据块，或者包含过多的存储碎片，需要首先删除这个索引，然后再重建它. 
如果索引是在创建约束时由oracle自动产生的,可以通过禁用约束或删除约束的方法来删除对应的索引. 
在删除一个表时,oracle会自动删除所有与该表相关的索引. 

索引数据字典 
all_indexes/dba_indexes/user_indexes 索引的基本信息 
all_ind_columns/dba_ind_columns/user_ind_columns 索引对应的字段信息 

analyze table tablename compute statistics for all indexes;  
analyze table tablename delete statistics 
查 dbms_stats 包中的函数


substr( string, start_position, [ length ] )
asciistr(user_name) -- 可判断中英文 如果是中文以\开头
substr(asciistr(user_name),0,1)  = '\';  ##'

create table score
( id  number    ,--primary key ,
  score number(10,2),
  cour_id numeric(10), 
  stu_id number,
  constraint FK_COUR FOREIGN KEY (cour_id)  references course(id),
  CONSTRAINT PK_SCORE PRIMARY KEY (id)
);
alter table score add  constraint FK_STU foreign key (stu_id ) referencing student(id); -- constraint FK_STU可省


建表时
 constraint auto_fk FOREIGN KEY (make,model,year)
    REFERENCES automobiles (make,model,year)
    ON DELETE (SET NULL)CASCADE
    );
    ON DELETE子串告诉ORACLE如果父纪录（parent record)被删除后
 

一,向oracle里加图片
---------------------------------
GRANT CREATE ANY DIRECTORY TO xxx;
GRANT drop any directory to xxx;

SQL> CREATE OR REPLACE DIRECTORY IMAGES AS 'C:\temp'; --图片目录Oracle Server所在机器的目录
 

SQL> CREATE TABLE IMAGE_LOB (T_ID VARCHAR2 (5) NOT NULL,T_IMAGE BLOB NOT NULL);

CREATE OR REPLACE PROCEDURE IMG_INSERT (TID VARCHAR2,FILENAME VARCHAR2) AS
	 F_LOB BFILE;
	 B_LOB BLOB;
BEGIN
	INSERT INTO IMAGE_LOB (T_ID, T_IMAGE) VALUES (TID,EMPTY_BLOB ()) RETURN T_IMAGE INTO B_LOB;
	F_LOB:= BFILENAME ('IMAGES', FILENAME);		--文件夹名(directory)
	DBMS_LOB.FILEOPEN (F_LOB, DBMS_LOB.FILE_READONLY);
	DBMS_LOB.LOADFROMFILE (B_LOB, F_LOB, DBMS_LOB.GETLENGTH (F_LOB));
	DBMS_LOB.FILECLOSE (F_LOB);
	COMMIT;
END;
/
 
-- 示例
SQL> EXEC IMG_INSERT('1','f_TEST.jpg');

表中 raw类型 列长度要求最大2000,是二进制图片 PLSQL Developer不能查看\
blob类型 PL/SQL Developer 可以查看图片

---------------------------------------Oracle 导出Blob 到文件

create or replace directory BLOB_DIR as '/tmp/blob_dir';
grant read,write on directory BLOB_DIR to zhaojin;

CREATE OR REPLACE PROCEDURE read_blob (db_name varchar2,new_name varchar2) IS
   l_file      UTL_FILE.FILE_TYPE;
   l_buffer    RAW(32767);
   l_amount    BINARY_INTEGER := 32767;
   l_pos       INTEGER := 1;
   l_blob      BLOB;
   l_blob_len INTEGER;
BEGIN
   select p.pkg_bin  into l_blob from Od_Publish_App_Pkg p where p.pkg_name=db_name;
    l_blob_len := DBMS_LOB.GETLENGTH(l_blob);
    l_file := UTL_FILE.FOPEN('BLOB_DIR',new_name,'wb', 32767);

    WHILE l_pos < l_blob_len LOOP
      DBMS_LOB.READ (l_blob, l_amount, l_pos, l_buffer);
      UTL_FILE.PUT_RAW(l_file, l_buffer, TRUE);
      l_pos := l_pos + l_amount;
    END LOOP;
    UTL_FILE.FCLOSE(l_file);
 EXCEPTION
    WHEN OTHERS THEN
      IF UTL_FILE.IS_OPEN(l_file) THEN
        UTL_FILE.FCLOSE(l_file);
      END IF;
      RAISE;
END;


---------------------------------------
 
grant read on directory  名 to 用户
 
 CREATE OR REPLACE DIRECTORY img AS 'C:\temp';
 create table lunar_test (product_id number, ad_id number, ad_graphic bfile );
 
 INSERT INTO lunar_test (product_id, ad_id, ad_graphic)
     VALUES (3000, 31001, bfilename('img', 'Sunset.jpg')); -- 用 bfilename('文件夹名','图片名');

alter user hr identified by hr
create user  用户名 identified by 密码

grant create session to  用户名
create（ drop）[public ]  synonym 短名 for 长名


select * from all_synonyms where owner=upper('hr') and SYNONYM_NAME=upper('');
 

select user from dual; //当前用户 也可以使用 show user

 



CREATE OR REPLACE FUNCTION F_LINK(P_STR VARCHAR2) RETURN VARCHAR2
  2  AGGREGATE USING T_LINK;   --T_link 是一个type body 自定义聚合函数


聚合函数  wmsys.wm_concat(以逗号分隔,官方文档查不到这个包和函数11g中有,12c网上说就没了) 同mysql的 group_concat 
可用 REPLACE(WMSYS.WM_CONCAT(xxx), ',', ';') 做替换
建议使用函数  listagg(bs.site_name,',')within group (order by bs.site_name)  


select regexp_replace('tj_12_45_123','([^[:digit:]])','') from dual;
1245123


SELECT INSTR('CORPORATE FLOOR','OR', 3, 2) from dual ;14
第三个开始,第二次出现的位置

如表名用关键字可以加 ""做
 
insert into 
       (select employee_id,last_name,email,hire_date,job_id,salary,department_id 
       from employees
       where department_id=50 
       )
  values (444,'ssss','dsdfs',to_date('02-04-08','mm-dd-yy'),'AD_VP',5000,50);
YYYY-Q //季度
 
========================PL/SQL

---------------视频
<<repeat_loop>>

goto repeat_loop   
以上实现循环


exit when x>3; 与下相同
if(x>3)
	exit;
end if;
 

exception 
when .. then..
when others

NO_DATA_FOUND   (select xx into vv 没有)
TOO_MANY_ROWS   ()
CASE_NOT_FOUND  (CASE 没有ELSE, //END)
END

myexcept EXCEPTION;
PRAGMA EXCEPTION_INIT(myexcept,-2222);   ### 定义异常和系统关联,-2222 是看的

RAISE myexcept; ##向上抛出异常



create or replace procedure test4debug as  
   TYPE xx_record_type IS RECORD   --相当于表的一行,
    (
      name employees.first_name%TYPE,
      SALARY number
    );
    xx xx_record_type;
    -- xx  employees%ROWTYPE;  --表名或者视图名,未测试
begin
   select first_name,SALARY  into xx from employees where employee_id=100;
    dbms_output.put_line(xx.salary);
  --update employees set ROW=xx where employee_id=100;  --ROW 是一行,,未测试
end;


单列多行集合
索引表		个数无限制,		下标可以负值		只可用PL/SQL,不可用表的列
------------------------------------------------------------------------------------------
充许的下标类型 BINARY_INTEGER,PLS_INTEGER,VARCHAR2

create or replace procedure test4debug as  
--begin
  --Type  my_type  is table of employees.first_name%TYPE INDEX BY BINARY_INTEGER;
  Type  my_type  is table of employees.first_name%TYPE INDEX BY  varchar2(20);
  my my_type;
 begin
   --select first_name into my(-1) from employees where employee_id=100; --圆括号,可负数
   select first_name into my('a') from employees where employee_id=100; --圆括号
   --dbms_output.put_line(concat('my(-1):',my(-1) ));
   dbms_output.put_line(concat('my("a"):',my('a') ));
   dbms_output.put_line('my.firs:'||my.first);
   dbms_output.put_line('my.last:'||my.last);
   dbms_output.put_line('my.count:'||my.count);--总数
end;
 /

嵌套表		个数无限制,		下标1开始		可用表的列,必须初始化
------------------------------------------------------------------------------------------
create or replace type o_tmp as object 
(
  userid varchar2(10),
  username varchar2(20)
);
create or replace type tmp_array is table of o_tmp
/


declare obj_arr tmp_array;
  tmp_obj o_tmp;
  test_arr tmp_array := tmp_array();
  n number := 1;
begin
  loop  exit when n > 5;
    tmp_obj := o_tmp('test1' || n, 'fuyue' || n);
   test_arr.extend;--可以扩展
    test_arr(n) := tmp_obj;--以()仿问
    n := n + 1;
  end loop;
  obj_arr := test_arr;--return
  for n in obj_arr.first .. obj_arr.last loop  --这个n不同于上一个n,.firts ,.last
    tmp_obj := obj_arr(n);
    dbms_output.put_line(tmp_obj.userid || ' --- ' || tmp_obj.username);
  end loop;
end;
/


--嵌套表在表中用必须 
CREATE TYPE phone_type IS TABLE OF VARCHAR2(20);
create table nest_table (phone phone_type)NESTED TABLE phone STORE AS phone_table --这个表不可仿问,个数没限制
insert into nest_table (phone) values(phone_type('AA','BB'));

变长数组	个数有限制		下标从1开始		可以用表中。必须构造初始化
------------------------------------------------------------------------------------------
TYPE ename_varray_type IS VARRARY(20) OF emp.ename%TYPE;
ename_varray ename_varray_type=ename_varray_type('A','B');

--变长数组 在表中用
CREATE TYPE var_phone_type IS VARRAY(20) OF VARCHAR2(20);
create table var_table (phone var_phone_type);
insert into var_table (phone) values(var_phone_type('AA','BB'));




集合方法只在PLSQL中使用，不可在SQL，
	EXTEND，TRIM，只能嵌套表和VARRY，不能用索引表
	extend 一个null,extend(n),n个null, extend(n,i)n个i  ,trim 删尾一个  trim(n)删尾尾n个，
	
	delete 只对嵌套表,索引表   delete删全部,delete(n)删第n个，delete(n，m)删第n到第m个，
	
	.first   .last   .count   .limit 
	exist(1)是否 ，(  .prior(5)  .next(5) 下标是5的，前／后一个下标值 )

	
集合赋值
	以下只可对嵌套表
		set(xx) 去除嵌套表(只可) 重复值  
		multiset union ［distinct］ 也只可用嵌套表，multiset intersect,multiset except
		cardinality（xx） 元素个数 ,xx member of yy_set ,xx is a set 是否包含重复, 
		xx submultiset of yy_set ,


dbms_utility.get_time /100是秒


forall 批量插入，不是循环
	forall i in 1..1000  insert into xx values(id_type(i));//id_type是一个集合，已有很多值，速度快



命名规范

变量	v_  
常量	c_
游标		 _cursor
例外	e_	
表类型		_table_type
表变量		_table
记录类型	_record_type
记录变量	_record


要使用 ' ,可以用两个' ,还可[],{} ,<>  ,要在外加'',前加q
			如string_var:=q'[xxx'xx]'				


 ** 幂,E ,e   
======================上==PL/SQL
 
DCL 数据控件语言  grant ,revoke

 
强if
decode(sex,
	'T',1, 
	'F',0
	,1);
	
	最后一个为default ,如sex的值是T返回1,如.... 

nullif(expr1,expr2)//相等返回null,否expr1
nvl(expr1,expr2) 第一个为null,返回第二个,否第一个          ##如是null最后一个
nvl2(expr1,expr2,expr3) 第一个为null,返回第三个,否第二个   ##如是null最后一个	
coalesce(expr1,expr2,...) 返回第一个非null的值

用" " 来使用关键字做为名字(表)
|| 连接

set serverouput on size 2000			###最多可输出字符,最多100万
dbms_output.put('ok');
prompt 'ok'
pro 'ok'
dbms_output.new_line;

支持string(20) 的类型

if ..then 
elsif then   ##elsif
..
else ...
end if;


case 
when ..then 
else ...
end case;


:= 赋值   =  判断


%FOUND
%ISOPEN
%NOTFOUND
%ROWCOUNT



cursor curor_name(id number) is select ...  where myid=id  

可加for update,在fetch 可以用update .. where CURRENT OF  curor_name ###游标当前行

open curor_name(20);

fetch curor_name into var1, var2... 
fetch curor_name bulk collect into var1, var2...  [limit __rows] ##一次可取结果的所有数据

close curor_name;

COLLECT 聚合函数 要和type一起使用
CREATE TYPE warehouse_name_t AS TABLE OF VARCHAR2(35);
/
SELECT CAST(COLLECT(warehouse_name ORDER BY warehouse_name)
       AS warehouse_name_t) "Warehouses"
   FROM warehouses;

   
参数不用给长度,只给类型

for xx in curor_name(20) loop

end loop;
my_cursor%NOTFOUND (如没有做fetch,用my_cursor%NOTFOUND IS　NULL)


for cur in (select ...) loop ###隐式游标
end loop;

create or replace procedure myproc (id IN varchar2)
is /as 


show errors procedure myrpoc ##显示部分错误


create view 时加with check option ,在插入时,会检查数据是否,和where 条件符合

with read only ###视图是只读的
 如果视图中有group by,distinct,也是只读的


create [public] synonym dep for hr.department;

create sequence xx start with 1 increment by 1 nomaxvalue
order
nocycle;
xx.nextval
xx.currval


ALTER TABLE tab_large add col_new Number(9) Default 1; 
(11g)对大表会很慢,相当于update 操作,可以将该语句改为
ALTER TABLE tab_large add col_new Number(9);
ALTER TABLE tab_large modify col_new number(9) default 1;


ALTER TABLE T_myTable add new_col varchar2(20);
ALTER TABLE T_myTable   MODIFY (id DEFAULT 1);
ALTER TABLE T_myTable   MODIFY (id number(10)); 
ALTER TABLE T_myTable drop column    new_col;
alter table T_myTable rename column    new_col to col;


atomicity原子性  (对多表的 成功,失败, 两个都成功,失败 )
consistency一致性
isoaltion隔离性
durability永久性  提交后不可回滚


create or replace trigger xx after delete on department 
for each row
begin
delete from employee where department_id=:old.id  ###:old,也有:new
end;

insert 只有:new ,delete 只有:old

raise_application_error(-20000,"aa") ##-20000  -29999

触发器可以对id 自增的sequence  (before insert,手工加入ID 的值 是无效的)

instead of insert on 视图  for each row###对多表的视图实现插入操作

对象授权 with grant option
系统授权 with admin option
 


grant unlimited tablespace to hr;

alter table xx add contraint yy check(sex ='T' or sex='F')

dba_contraints


对于性别字段建索引  bitmap index(取值较少)   ,unique index   唯一约束是唯一索引,唯一索引不一定是 唯一约束 (约束是子,索引是父)
 

ed
ed x
@x  执行刚才的生成的文件 (start)


----------
select * from (select * from employees where rownum<20)
minus
select * from (select * from employees where rownum<10)


rownum也只可以用=1,
//  从第10行开始,13条结束,要3 条记录   OK
select *   from (select e.*, rownum row_num from employees e where rownum <= 13) emp  where emp.row_num >= 10; 

// 从第3行开始, 13条结束 要10 条记录  OK
select *   from (select e.*, rownum row_num from employees e where rownum <= 13) emp  where emp.row_num >= 3; 

String page_sql="select * from (select tmp.*, rownum row_num from ("+sqlOrTable+") tmp where rownum <= " + pageNo * pageSize +") page  where page.row_num >" + (pageNo * pageSize - pageSize);//通用的就要多一个select

sql 中如果有id>0的情况,就必须加 order by id ,否则第一页和最后一页会出现相同的记录,

可以用 rownum != 10 同 rownum <10 ,是返回前9条数据
可以用 rownum =1 第一条,rownum>0所有的
可以使用     rownum = 1 ,不能使用 rownum = 2
不能单个使用 rownum > 10 rownum 

因为ROWNUM是对结果集加的一个伪列，即先查到结果集之后再加上去的一个列 (强调：先要有结果集)
因为 rownum 是在查询到的结果集后加上去的，它总是从1开始



根层次为1
select lpad(' ',3*(LEVEL-1))||last_name name  , lpad(' ',3*(LEVEL-1))||job_id job
from employees
where job_id<>'CLERK' START WITH manager_id is null
connect by manager_id=PRIOR employee_id


关联子查询　父返回每一行，就一次子查询
select last_name ,salary,department_id
from employees outer
where salary >(select avg(salary) from employees where department_id=outer.department_id);


select last_name ,salary,
	(select department_name from department where id=outer.department_id)
	as department_name
from employees outer 
 
----------- oracle 式的关联更新 1
update boss_customer set total_balance=(select money_remain from users where user_id=boss_customer.user_id) 
//子查询是在父每移动一行，就执行一次子查询

update t_op_bd_loading_code_log l
set l.op_user=
	(select e.EMPLOYEE_NAME 
	from T_BASE_EMPLOYEE e 
	where e.EMPLOYEE_CODE =l.op_user_code 
	)  
where substr(asciistr(l.op_user),0,1)!='\';    -- '可能关联不到值，返回 null，性能底
 
----------- oracle 式的关联更新 2
视图方式
update 
(
	select l.op_user, e.EMPLOYEE_NAME
	from t_op_bd_loading_code_log l , T_BASE_EMPLOYEE e
	where e.EMPLOYEE_CODE =l.op_user_code
) 
set  op_user=  EMPLOYEE_NAME 
where substr(asciistr(op_user),0,1)!='\';  -- ' 关联不到的会过滤掉，性能高



 

alter session set nls_lanugage='american'  会影响
select * from V$NLS_PARAMETERS where parameter = 'NLS_CHARACTERSET';  的值

select regexp_replace('tj_12_45_123','([^[:digit:]])','') from dual;
1245123



如表名用关键字可以加 ""做
  



表分区  1.range  2.hash  3列(按列的取值)  4,复合(range和hash)
create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)partition by range(stu_sco)
(
	partition p1 values less than (60) TABLESPACE tbs_1, (表空间)
	partition p2 values less than (70),
	partition p3 values less than (80),
	partition p4 values less than (maxvalue)
);


(hash分区,每个分区的大小相似)
create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)
PARTITION BY HASH (stu_sco) 
PARTITIONS 4					 ( 分区个数需设置成2的幂次)
[ STORE IN (tbs_1, tbs_2, tbs_3, tbs_4) ]; (每个分区的名字)

或者用
create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)PARTITION BY HASH (stu_sco) 
(
partition p1  [ tablespace tbs_01 ],
partition p2  [ tablespace tbs_02 ],
)




create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)PARTITION BY list (stu_sco) 
(
	partition p1 values (60,65) TABLESPACE tbs_1, (表空间)
	partition p2 values (70,75),
	partition p3 values (80,85),
	partition p4 values (default)
)

create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)PARTITION BY list (stu_name) 
(
	partition p1 values ('张','李') TABLESPACE tbs_1, (表空间)
	partition p2 values ('孙','王'),
	partition p3 values (default)
)

复合分区
create table stu_score
(stu_id integer not null,
stu_name varchar2(20),
stu_sco number(4)
)PARTITION BY RANGE (stu_sco)
SUBPARTITION BY HASH (stu_name)
(PARTITION pp1 VALUES LESS THAN (60),
 PARTITION p2 VALUES LESS THAN (80)
      SUBPARTITIONS 2,																
 PARTITION p3 VALUES  LESS THAN (MAXVALUE)
      SUBPARTITIONS 3
);

///////stu_name,stu_sco 不能改(8i中)


PARTITION BY RANGE (time_id)
SUBPARTITION BY HASH (channel_id)
(PARTITION SALES_Q1_1998 VALUES LESS THAN (TO_DATE('01-APR-1998','DD-MON-YYYY')),
 PARTITION SALES_Q2_2000 VALUES LESS THAN (TO_DATE('01-JUL-2000','DD-MON-YYYY'))
      SUBPARTITIONS 8,																表示对某一个(SALES_Q2_2000)的RANGE分区,再分8个,自动命名
   PARTITION SALES_Q3_2000 VALUES LESS THAN (TO_DATE('01-OCT-2000','DD-MON-YYYY'))  表示对某一个(SALES_Q3_2000)的RANGE分区,再分5个,指定名字
     (SUBPARTITION ch_c,
      SUBPARTITION ch_i,
      SUBPARTITION ch_p,
      SUBPARTITION ch_s,
      SUBPARTITION ch_t),
   PARTITION SALES_Q4_2000 VALUES LESS THAN (MAXVALUE)
      SUBPARTITIONS 4)
;


PARTITION BY RANGE (credit_limit)
   SUBPARTITION BY LIST (nls_territory)


select * from stu_score partition (p1);
delete from stu_score partitoin (p1);

alter table stu_score add partition p4 values less than (150);
alter table stu_score drop partition p4 ;
alter table stu_score truncate partition p4 ; //索引失效状态
alter table stu_score merge partitions p1,p2 into partition pp;
alter table stu_score split partition pp partition p1 at (80) into (partition p1  tablespace tbs1 , partition p1);


set sqlprompt system>

select * from xx for update 把所的选择的行加锁,要用 commit,rollback

select * from xx for update of columns [ wait n ! nowait]
select * from stu_score partition (p2) for update of stu_id,stu_name nowait  //wait 5 单位秒,
 


select to_char(0.12345*100,'99D99')||'%' from dual;  显示％

alter session set nls_date_format='YYYY-mm-dd  HH24:mi:ss';//大小无关

new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Java方式

select to_char(sysdate,'YYYY-MM-DD HH24:mi:ss')  from dual;

date,timestamp timestamp(6)都是有秒的

双机模式的jdbc驱动
jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=OLTP_A-vip)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=OLTP_B-vip)(PORT=1521))(LOAD_BALANCE=on)(FAILOVER=on))(CONNECT_DATA=(SERVICE_NAME=bst)))  
jdbc驱动是ojdbc6.jar
  

以下要一起使用
WITH m_summary as (select dname,SUM(sal) as dept_total from emp,dept where emp.deptno=dept.deptno group by dname)
SELECT dname, dept_total
FROM m_summary
WHERE dept_total > (SELECT SUM (dept_total) * 1 / 3 FROM m_summary)



select path(1) ,depth(2) from resource_view where under_path(res,'/sys/schemas/PUBLIC',1)=1
and under_path(res,'/sys/schemas/PUBLIC',2)=1;


SQL> select nls_charset_name(852) from dual;

NLS_CHAR
--------
ZHS16GBK

SQL> select nls_charset_id('ZHS16GBK') from dual;

NLS_CHARSET_ID('ZHS16GBK')
--------------------------
                       852
 
exception when dup_val_on_index 
  

外键约束时可加ON DELETE CASCADE
 
CREATE TABLE salary
(
emp_id  numeric(10)       not null,

CONSTRAINT fk_empid  FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
	 ON DELETE CASCADE
);
   
select type,name,line,text from user_source ;//查procedure,function,trigger,package的源码

--查有哪些表的列外键指向 OD_OPERATOR 表的OPERATORID列,用r_constraint_name
--create or replace view v_ref_tabe as --视图不能传参数,要有create view权限
select foreign_t.table_name as foreign_table ,foreign_c.column_name as foreign_column,
       main_t.table_name    as main_table,    main_c.column_name    as main_column
from user_constraints foreign_t, user_cons_columns foreign_c ,
     user_constraints main_t,    user_cons_columns main_c
where foreign_t.constraint_name=foreign_c.constraint_name 
and   main_t.constraint_name=main_c.constraint_name
and   foreign_t.r_constraint_name=main_t.constraint_name
and   foreign_t.constraint_type='R' 
and   main_t.table_name='OD_OPERATOR' and main_c.column_name='OPERATORID';
--with read only;
主键 P 

 
----对select count(*)   from 的性能,不支持加 where,不支持事务
create table OD_ROW_COUNT
(
       id number(10) not null primary key ,
       table_name varchar2(50) unique,
       row_count number(10) default 0
);
insert into OD_ROW_COUNT(id,table_name) values(1,'OD_CODE_INFO');
commit;

create or replace trigger OD_TRIGGER_CODE_INFO_COUNT
 AFTER INSERT or DELETE  ON OD_CODE_INFO
FOR EACH ROW 
DECLARE 
tab_name OD_ROW_COUNT.table_name%TYPE;
BEGIN
 tab_name:='OD_CODE_INFO';
 IF INSERTING THEN
    update OD_ROW_COUNT set row_count=row_count+1 where upper(table_name)=upper(tab_name);
 ELSIF DELETING THEN
    update OD_ROW_COUNT set row_count=decode(row_count,0,0, row_count-1) where upper(table_name)=upper(tab_name);  
 END IF;  
END;
/

----


正则表达式 REGEXP_开头的函数
REGEXP_INSTR
REGEXP_SUBSTR ，
REGEXP_LIKE 可以check约束
REGEXP_REPLACE

===动态SQL  dbms_sql包
v_id NUMBER;
v_stat NUMBER;
v_cursor NUMBER;

v_sql := 'SELECT id,qan_no,sample_date FROM "tblno" WHERE id > :sid and sample_date < :sdate';
s_id := 3000;

v_cursor := dbms_sql.open_cursor; --打开游标；
dbms_sql.parse(v_cursor, v_sql, dbms_sql.native);
dbms_sql.bind_variable(v_cursor, ':sid', s_id); --设参数
//不能对主键直接使用数字,必须用sequence,或 循环因子,来生成值  ##10gR2
//也不能绑定 null 值,可以把null入一变量中,如varchar2(1)

dbms_sql.define_column(v_cursor, 1, v_b);##对select 第一列,保存在变量中
//果需要返回查询语句的结果,则必须在exec之前使用define_column函数定义返回字段
row_process := dbms_sql.execute(v_cursor);

if dbms_sql.fetch_rows(v_cursor) > 0 then   ##循环
	dbms_sql.column_value(v_cursor, 2, v_b);
end if;

DBMS_SQL.DEFINE_ARRAY(v_cursor, 1, n_tab, 9, indx);//第1列是返回的是 DBMS_SQL.NUMBER_TABLE,每次9行,从第index行开始
dbms_sql.bind_array( 使用dbms_sql.Number_Table类型 )

dbms_sql.variable_value(cursor_name, 'r', r);//得到DML操作returning的结果集,'insert into demo values (:a,:b) returning :a*:b into :r',

v_stat := dbms_sql.execute(v_cursor); 
dbms_sql.close_cursor(v_cursor); --关闭游标。
DBMS_SQL.IS_OPEN(v_cursor);
===

SELECT 
FROM A  left join B ON a.x=b.y
	left join C ON a.x=b.z

mysql,oracle 都有 current_date 和 current_timestamp
select current_date from dual;和sysdate 相同,
trunc(sysdate) 只要日期，不要时间
current_timestamp
select systimestamp from dual;

select to_char(sysdate,'sssssss') from dual; 得到时间截

TZ_OFFSET(sessiontimezone)//中国是 GMT +08:00

TO_DATE('19700101','yyyymmdd') + in_number/86400 
in_date - TO_DATE('19700101','yyyymmdd'))*86400 


select dbms_utility.get_time from dual;
select * from v$timer;
select to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss,ff3') from dual;//毫秒ff3
			     yyyy-mm-dd hh24.mi.SSxFF  //秒.6位毫秒

alter session set nls_date_format='YYYY-mm-dd HH24:mi:ss'
alter session set nls_timestamp_format='YYYY-mm-dd HH24:mi:ss' 
alter system set nls_date_format='YYYY-mm-dd HH24:mi:ss'  scope=spfile;
alter system set nls_timestamp_format='YYYY-mm-dd HH24:mi:ss'  scope=spfile;

set NLS_LANG=american_america.AL32UTF8
set nls_date_format=YYYY-mm-dd HH24:mi:ss　　　//date类型
set nls_timestamp_format=YYYY-mm-dd HH24:mi:ss 　//timestamp类型



//to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS')  //Oracle 不区分大小写
new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//Java 严格区分大小写

select * from v$version;


--------------物化视图
grant create materialized view to zhaojin; 
create materialized view my_view as select ....  
   as
  select ... from ...


analyze table my_view compute statistics;
select tl.table_name,tl.num_rows from user_tables tl where tl.table_name =upper('my_view');//是在user_tables中的

select  segment_name ,bytes, blocks from user_segments where segment_name=upper('my_view');
select * from user_mviews where MVIEW_NAME =upper('my_view');


create materialized view my_view 
REFRESH FAST START WITH SYSDATE   #fast 不能对复杂查询,可选的有 COMPLETE,Force是默认
NEXT  SYSDATE + 1/48
WITH PRIMARY KEY		#WITH 可选的有 ROWID ,默认是PRIMARY KEY
AS SELECT....

 Rowid物化视图只有一个单一的主表,不能包括 Distinct,聚合函数,子查询,连接


#当fast 必须创建基于主表的视图日志,用来发送主表已经修改的数据行到物化视图中  (增量刷新)
CREATE MATERIALIZED VIEW LOG ON（主表名） ##表要有主键
增量刷新选项,如子查询中存在分析函数,则物化视图不起作用


on demand 手工通过 DBMS_MVIEW.REFRESH 等方法刷新,(job定时)
on commit:当主表中有数据提交的时候，立即刷新MV中的数据

默认值是FORCE ON DEMAND

create materialized view log on OD_manufacture ; 
create table mv_test as select * from OD_manufacture where 1=2; 
create materialized view mv_test on prebuilt table refresh fast as   ##名字已存在用 prebuilt
  select * from OD_manufacture 

exec dbms_mview.refresh('MV_TEST');
call dbms_mview.refresh('MV_TEST');

call 是sql命令，任何工具都可以使用

DBMS_UTILITY.uncl_array
type uncl_array IS table of VARCHAR2(227) index by BINARY_INTEGER;


declare
begin
  EXECUTE IMMEDIATE  'INSERT INTO DEPT VALUES( :1, :2, :3 )'
			USING 80,'product_dep','shanghai';
  COMMIT;
end;
/

 
使用 UNION ALL 把合并的SQL结果全部显示出来,包括重复的,UNION 不显示重复的
 
select JOB_ID,max(SALARY),
	  rank() over(order by max(SALARY) desc ) as rank,
dense_rank() over(order by max(SALARY) desc ) as dense_rank
from Employees
group by JOB_ID;

rank　　　如有重复或相同的大小1,1,3
dense_rank如有重复或相同的大小1,1,2   密集的

--SALARY第二大的
select JOB_ID from (
  select JOB_ID,  dense_rank() over(order by max(SALARY) desc ) as dense_rank
  from Employees 
  group by JOB_ID
) where dense_rank = 2

--所有部门薪水第二大的员工记录,不使用max()有重复的会显示出,使用　distinct
select distinct  department_id,SALARY   
from   (select   EMPLOYEES.*,
          dense_rank() over(partition by department_id order by SALARY desc) as  my_dense_rank
         from   EMPLOYEES) 
where   my_dense_rank = 2


 --查询每个部门中员工薪水最高的员工ID和薪水
select distinct   B.department_id,B.SALARY   --(B.employee_id 可能一个部门两个员工都是最高10000,重复) 
from  employees B , 
    (select  department_id,   max(salary) max_sal 
    from employees  group by department_id) A
where   B.department_id=A.department_id 
	and B.salary=A.max_sal

 
 
默认递减排序中将空值指定为最高排名1  NULLS FIRST

SELECT dense_rank(5100) within group( order by SALARY) FROM Employees
类似
SELECT count( distinct SALARY) from Employees where SALARY<5100　

SELECT rank(5100) within group( order by SALARY) FROM Employees
类似
SELECT count(SALARY) from Employees where SALARY<5100　
 




RANK()		OVER (ORDER BY SUM(sal) DESC NULLS LAST) 
CUME_DIST()	OVER (ORDER BY SUM(sal) DESC) 
PERCENT_RANK()	OVER (ORDER BY SUM(sal) DESC)
ROW_NUMBER()	OVER (ORDER BY SUM(sal) DESC) 


可能多行，因为值可能相等
SELECT col,
MIN(value) KEEP(DENSE_RANK FIRST ORDER BY col) "Min Value",
MAX(value) KEEP(DENSE_RANK LAST	 ORDER BY col) "Max Value"
FROM tmp1
GROUP BY col


SELECT CUME_DIST(120) WITHIN GROUP (ORDER BY value) FROM TMP1

GROUPING(col) cube、rollup子句的辅助函数
--
add_months(sysdate,1)  //-1
extract(year from sysdate)　//,month,day

months_between
select sysdate, sysdate+numtodsinterval(1,'hour') from dual ; //日期加1小时
    'DAY'
    'HOUR'
    'MINUTE'
    'SECOND'

   
length()函数,有效数据的长度,字串的个数，中文也是一个
VSIZE(x)返回的是内部存储所占用的字节数




select TRIGGER_NAME from user_triggers where TABLE_NAME=upper('Table_name');//所有在表上的触发器
user_trigger_cols;有列

 
create table xx as select * from y;
 
 

select * from people
where peopleId in (select   peopleId from   people group by   peopleId having count(peopleId) > 1)
使用 exists 代替 in

最高效的删除重复记录方法 ( 因为使用了ROWID)例子：
　　DELETE FROM EMP E WHERE E.ROWID > 
		( SELECT MIN(X.ROWID)
	　　FROM EMP X WHERE X.EMP_NO = E.EMP_NO);

TRUNCATE是DDL不是DML

set autotrace on
	recursve call 表示递归调用  ,select count(*) 第一次有值,第二次就是0了
	Cost(%cpu)列

	
Oracle SQL 优化
	最大数量记录的条件必须写在Where子句的末尾
	Oracle从右到左处理From子句中的表名
	不用“<>”或者“!=”操作符。会造成全表扫描，可以用“<” or “>”代替
	多利用内部函数提高Sql效率 ,decode,nvl
	in 可以考虑将or子句分开 (用EXISTS替代IN)
	用EXISTS替换DISTINCT：
	“>=”和“<=”比较符来等价的代替BETWEEN操作符
	SELECT子句中避免使用 ‘ * ‘：
	用TRUNCATE替代DELETE：
	用Where子句替换HAVING子句：
	sql语句用大写的
	存储过程中，采用临时表优化查询
	多使用COMMIT
	连接多个表时, 请使用表的别名
	SQL语句一样,多余的空格,使用PreparedStatement,?
	子查询
	索引

	count(*) 可以保证数据正确,如果count(col) 如果某行col的值为null不会被计算,MySQL和Oracle都是这样的
	如果col有索引会快点,如果not null;
	
	
临时表
CREATE GLOBAL TEMPORARY TABLE QCUI_Temp_Trans
ON COMMIT DELETE ROWS   -- ON COMMIT PRESERVE ROWS ,关闭了Session 或 Log Off 后,自动删Truncate
AS
SELECT * FROM t_Department;

CREATE GLOBAL TEMPORARY TABLE flight_schedule (
startdate DATE,
enddate DATE,
cost NUMBER)
ON COMMIT PRESERVE ROWS;


执行计划
explain plan for select  * from employees;
select * from table(dbms_xplan.display);

show parameters optimizer_mode ;

alter session set optimizer_capture_sql_plan_baselines=true;
 
select signature,sql_handle,plan_name,origin,enabled,accepted, autopurge
from dba_sql_plan_baselines -- where sql_text like

 
 
 
 INSERT ALL
WHEN order_total < 100000 THEN
INTO small_orders
WHEN order_total > 100000 AND order_total < 200000 THEN
INTO medium_orders
ELSE
INTO large_orders
SELECT order_id, order_total, sales_rep_id, customer_id
FROM orders;



数据复制
create table xx as select * from yy;
CREATE TABLE copyTable AS SELECT * FROM myTable;
INSERT INTO copyTable SELECT * FROM myTable

--using 后可以使用视图或者(select)
MERGE INTO products p  USING newproducts np   
	ON (p.product_id = np.product_id)   
WHEN MATCHED THEN  
	 UPDATE SET p.product_name = np.product_name,  p.category = np.category
WHEN NOT MATCHED THEN  
	 INSERT  (PRODUCT_ID,PRODUCT_NAME,CATEGORY)
	 VALUES (np.product_id, np.product_name, np.category);    
	 
	 
	 
字段只对部分数据做索引,
create unique index MPMTDATA.IX_BTPAY_MC_BUSI_S on MPMTDATA.T_MPMT_PAY_TRANS_DETAIL 
	(CASE STATUS WHEN '000' THEN MERACHANT_NO WHEN '101' THEN MERACHANT_NO WHEN '105' THEN MERACHANT_NO ELSE NULL END,
	 CASE STATUS WHEN '000' THEN MC_BUSINESS_NO WHEN '101' THEN MC_BUSINESS_NO WHEN '105' THEN MC_BUSINESS_NO ELSE NULL END);

	 
	 
	 
事务
 savepoint my_a;
 
 rollback to my_a;



