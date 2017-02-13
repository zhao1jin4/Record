

这三种驱动的程序加载和连接建立方法如下：
//Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
//Class.forName("COM.ibm.db2.jdbc.net.DB2Driver");
//Class.forName("com.ibm.db2.jcc.DB2Driver"); 


String url="jdbc:db2://localhost:5000/sample"; 
String dbUrl ="jdbc:db2://localhost:6789/sample"; 

DB2SQLProceduralLangs-db2plsc1050.pdf
DB2AdminConfig-db2dac1051.pdf
DB2MessageRefVol1 .pdf 是错误码

如使用关键字做表名,或者字段名,使用  "" 引用

显示表的定义和结构：
$ db2 describe table {table_name}
$ db2 describe table {table_name} show detail


显示表上索引的定义和结构：
$ db2 describe indexes for table {table_name}
$ db2 describe indexes for table {table_name} show detail


单选注释  使用 --  
多行注释 


在 DB2 中可以用两种方法自动生成一个数字序列：

1.定义带有 IDENTITY 属性的列。 
2.创建 SEQUENCE 对象。 

create schema db2instl;

CREATE TABLE stu
 (
   order_id   INT NOT NULL       
        GENERATED ALWAYS 
      AS IDENTITY 
      (START WITH 1 
       INCREMENT BY 1 
       MINVALUE 1 
       NO MAXVALUE 
       NO CYCLE
       NO CACHE
       ORDER),
   name varchar(20)
);	

varchar 类型一个中文占3个位,即3个字节

INSERT INTO stu  VALUES  ( DEFAULT,'李');

 
------函数
只当前日期
SELECT current date FROM sysibm.sysdummy1;    -- 2015/8/4
 
select char(current date, ISO) as result from sysibm.sysdummy1;   --2015-08-04
SELECT current date +1  DAYS from sysibm.dual
SELECT current time + 1 HOURS from sysibm.dual
SELECT TO_CHAR( SYSDATE, 'YYYY-MM-DD') from sysibm.dual
只当前日期
SELECT current time FROM sysibm.sysdummy1;  
 '14:04:04'
 
 当前日期日期
SELECT current timestamp FROM sysibm.sysdummy1;  
  '2012-09-20 14:04:11.343000'

SELECT TO_CHAR(current timestamp, 'YYYY-MM-DD HH24:mi:ss')  as now from sysibm.dual
 
--分页查询
SELECT * FROM
 (Select EMPNO,FIRSTNME,HIREDATE,
	rownumber() over(ORDER BY EMPNO ASC) AS rn
	from employee
 ) AS a1
WHERE a1.rn  >= 10  and  a1.rn < 30   -- 第一条记录是10开始,不是1


SELECT * FROM
 (Select EMPNO,FIRSTNME,HIREDATE,
	rownumber()  over()  AS rn
	from employee
 ) AS a1
WHERE a1.rn  >= 10  and  a1.rn < 30 


select rownumber() over() as row_num , e.* from employee e  

只查前 n条 
select * from  DB2INSTL.EMPLOYEE  fetch first 10 rows only 
 


CREATE SEQUENCE orderseq
START WITH 1
INCREMENT BY 1
NOMAXVALUE
NOCYCLE
CACHE 50


INSERT INTO order(orderno, custno)
VALUES (NEXT VALUE FOR orderseq, 123456);

可 NEXTVAL 和 PREVVAL 来替代 NEXT VALUE 和 PREVIOUS VALUE
sequence-name.NEXTVAL 替代 NEXT VALUE FOR sequence-name
sequence-name.CURRVAL 替代 PREVIOUS VALUE FOR sequence-name


CREATE table  T_TABLE     ( 
 ID BIGINT  GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1 NO CACHE) primary key ,       
 START_TIME TIMESTAMP,
 END_TIME TIMESTAMP, 
   CONSTRAINT P_KEY_1 PRIMARY KEY (ID)
)  ; 
COMMENT ON TABLE T_TABLE IS  '测试表' 
COMMENT ON COLUMN T_TABLE.START_TIME IS  '请求开始时间' 

COMMENT ON   T_TABLE 
(
	START_TIME IS  '请求开始时间' ,
	END_TIME IS  '请求结束时间' 
)

select column_name, type_name, remarks from "SYSIBM"."SQLCOLUMNS" where table_name='T_TABLE' order by table_name;
 
 
查表的索引

通过 SYSCAT.INDEXES JOIN SYSCAT.INDEXCOLUSE
 
SYSCAT 只读视图
select * from syscat.indexes where tabschema=upper('zhaojin') and tabname =upper('department')

select index_name,uniqueness from USER_INDEXES where table_name =upper('department')  --兼容 Oracle
select constrant_name,contraint_type,r_constrant_name from  user_constraints  where table_name =upper('department')  --兼容 Oracle ,R=reference,P=primary,C=check
 
查 表的约束：  唯一约束与唯一索引的区别是：唯一索引允许且仅允许一个null值。

可通过 SYSCAT.TABCONST JOIN SYSCAT.KEYCOLUSE来查询

带 default 的列,工具在DDL中可以看到
db2look -d sample -e -o myTable.sql -z zhaojin -t myTable


COALESCE ( expression, expression [ , ...] )返回列表中的第一个非空表达式。 