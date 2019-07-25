

------------------------------windows下可以成功调用ipconfig等命令---------------------------------------------------------  
  CREATE   OR   REPLACE   AND   RESOLVE   JAVA   SOURCE   NAMED   "EvokeOsCmd"   AS  
  import   java.io.*;  
  import   java.lang.*;  
  public   class   EvokeOsCmd   extends   Object{  
      public   static   void   main(String   []   args){  
          EvokeOsCmd   oscmd   =   new   EvokeOsCmd();  
        oscmd.runOsCmd();  
   
        }  
        public   void   runOsCmd(){  
   
              try   {  
                  //   Execute   a   command   with   an   argument   that   contains   a   space  
                  //String[]   commands   =   new   String[]{"grep",   "hello   world",   "/tmp/f.txt"};  
                  //commands   =   new   String[]{"grep",   "hello   world",   "c:\\Documents   and   Settings\\f.txt"};  
                      System.out.println("Starting   run.sh");  
                  //String   command   =   "cmd.exe   /c   dir";  
                      String   command   =   "ipconfig";                 //String   command   =   "notepad";  
                  Process   p   =   Runtime.getRuntime().exec(command);  
   
                              int   bufSize=4096;  
                              BufferedInputStream   bis=  
                                              new   BufferedInputStream(p.getInputStream(),bufSize);  
                                              int   len;  
                                              byte   buffer[]=new   byte[bufSize];  
                                              while((len=bis.read(buffer,0,bufSize))!=-1)  
                                                                        System.out.write(buffer,0,len);  
   
                                              p.waitFor();  
                  System.out.println("Starting   run.sh   successfully   complete");  
                              }catch   (Exception   e)  
                              {  
                        e.printStackTrace();  
                        }  
      }  
   
   
  }  
  /  
  -----------------------------  
  CREATE   OR   REPLACE   PROCEDURE   RUN_OSCMD   as   language   java  
  name   'EvokeOsCmd.main   (java.lang.String[])';  
  /  
  -----------------------------  
  begin  
      dbms_java.grant_permission(USER,'java.io.FilePermission','*','execute,write,read');  
      --dbms_java.grant_permission(USER,'java.io.FilePermission','*','execute,write,read');  
      dbms_java.grant_permission(USER,'java.lang.RuntimePermission','*','writeFileDescriptor');  
  end;  
  /  
  -----------------------------  
  set   serveroutput   on   size   1000000  
  exec   dbms_java.set_output(1000000);  
  -----------------------------  
  exec   run_oscmd;  
  /  

=======
由于ps/sql是过程化的语言，它基本上不具备多态的概念，
供oracle使用的java方法必须申明为static，
所以在oracle中你无法使用java的动态特性，比如接口、反射等。


首先是创建一个java类，可在IDE里面编写,也可以直接在oracle的控制台里面创建
启动sql plus，执行如下命令

create or replace and compile java source named "hello_sp" as
package org.bromon.oracle;
public class Hello
{
  public static String say(String name)
  {
    return "你好,"+name;
  }
}

然后在oracle中把这个类导入成为一个函数，执行命令：

create or replace function hello_sp(name varchar2) return varchar2
as language java name
'org.bromon.oracle.Hello.say(java.lang.String) return java.lang.String';

现在可以调用该函数，执行：

select hello_sp('bromon') from dual;


如我们的java方法是没有参数的，比如：
public static String say()
  {
    return "你好";
  }
在创建函数的时候，函数名不应该有扩号：hello_sp

如果你的java类是在IDE里面编写的，那么只需要在oracle中加载编译过的class文件即可，方法是：

启动enterprise manage console，在“方案”下找到“源类型”，右击”java类”，选择”加载java“，选择对应的class文件即可。

 

------------------Oracle 自定义聚合函数strcat ,像MySQL的 GROUP_CONCAT 函数
create or replace type strcat_type as object
(
	currentstr varchar2(4000),
	currentseprator varchar2(8),
	static function ODCIAggregateInitialize(sctx IN OUT strcat_type) return number,
	member function ODCIAggregateIterate(self IN OUT strcat_type,value IN VARCHAR2) return number,
	member function ODCIAggregateTerminate(self IN strcat_type,returnValue OUT VARCHAR2, flags IN number) return number,
	member function ODCIAggregateMerge(self IN OUT strcat_type,ctx2 IN strcat_type) return number
);
/
  create or replace type body strcat_type is
      static function ODCIAggregateInitialize(sctx IN OUT strcat_type) return number is
      begin
        sctx := strcat_type('',',');
        return ODCIConst.Success;
      end;
      member function ODCIAggregateIterate(self IN OUT strcat_type, value IN VARCHAR2) return number is
      begin
        if self.currentstr is null then
           self.currentstr := value;
        else
          self.currentstr := self.currentstr ||currentseprator || value;
        end if;
        return ODCIConst.Success;
      end;
      member function ODCIAggregateTerminate(self IN strcat_type, returnValue OUT VARCHAR2, flags IN number) return number is
      begin
        returnValue := self.currentstr;
        return ODCIConst.Success;
      end;
      member function ODCIAggregateMerge(self IN OUT strcat_type, ctx2 IN strcat_type) return number is
      begin
        if ctx2.currentstr is null then
          self.currentstr := self.currentstr;
        elsif self.currentstr is null then
          self.currentstr := ctx2.currentstr;
        else
          self.currentstr := self.currentstr || currentseprator || ctx2.currentstr;
        end if;
        return ODCIConst.Success;
      end;
      end;
     
/
  CREATE OR REPLACE FUNCTION strcat (input VARCHAR2) RETURN VARCHAR2 PARALLEL_ENABLE AGGREGATE USING strcat_type;
/
--------------strcat

----one SQL 
SELECT country,max(substr(city,2)) city
FROM
(SELECT country,sys_connect_by_path(city,',') city
   FROM
      (SELECT country,city,country||rn rchild,country||(rn-1) rfather
          FROM
          (SELECT  country ,city,row_number() over (PARTITION BY country ORDER BY city) rn
           FROM tmp2
          )
      )
    CONNECT BY PRIOR rchild=rfather START WITH rfather LIKE '%0'
)
GROUP BY country;


伪列是ROWNUM


-------wmsys.wm_concat 聚合函数 是使用,分隔的
select wmsys.wm_concat(ename) from emp group by DEPTNO;

-----行转列
使用sum(case when  then else end 或者decode函数), group by  (交叉查询)
select REPLACE(wmsys.wm_concat(ename), ',', '/')  from emp group by DEPTNO  
把,修改为/   可能是字段中的,



--列转行
使用MODEL
	分区列、维度列和度量列

SELECT SUBSTR(country,1,20) country, 
       SUBSTR(prod,1,15) prod, year, sales		--sales不存在视图中
FROM sales_view
WHERE country IN ('Italy','Japan')
   MODEL RETURN UPDATED ROWS				--将结果限制为在该查询中创建或更新的那些
     PARTITION BY (country) 
     DIMENSION BY (prod, year)				--下面顺序,2002年的数据是计算出来的,数据库中不存在的
     MEASURES (sale sales)				--sale字段存在,转换成sales
     RULES (
       sales['Bounce', 2002] = sales['Bounce', 2001] + sales['Bounce', 2000],
       sales['Y Box', 2002] = sales['Y Box', 2001],
       sales['2_Products', 2002] = sales['Bounce', 2002] + sales['Y Box', 2002])  --使用前面的
ORDER BY country, prod, year;




RULES (
   sales['Bounce', 2005] = 
   100 + max(sales)['Bounce', year BETWEEN 1998 AND 2002] )

CV() 函数
 RULES (
   sales['Bounce', year BETWEEN 1995 AND 2002] =
   sales['Mouse Pad', cv(year)] + 
   0.2 * sales['Y Box', cv(year)])		--从1995-2002的每一年

--CV() 函数只能在右侧单元格引用中使用。 
SELECT SUBSTR(country,1,20) country, 
   SUBSTR(prod,1,15) prod, year, sales, growth
FROM sales_view
WHERE country='Italy'
MODEL RETURN UPDATED ROWS 
   PARTITION BY (country) 
   DIMENSION BY (prod, year)
   MEASURES (sale sales, 0 growth)
   RULES (
   growth[prod in ('Bounce','Y Box','Mouse Pad'), year between 1998 and 2001] =    
   100* (sales[cv(prod), cv(year)] - 
   sales[cv(prod), cv(year) -1] ) / 
   sales[cv(prod), cv(year) -1] )
ORDER BY country, prod, year
/


growth[prod in ('Bounce','Y Box','Mouse Pad'), year between 1998 and 2001] =           ### FOR 变成  [ for prod in (''
没有 FOR 关键字，那么只会更新已存在的单元格，并且不会插入新单元格。


--Any使用
  RULES (
   growth[prod in ('Bounce','Y Box','Mouse Pad'), ANY] =
   100* (sales[cv(prod), cv(year)] - 
   sales[cv(prod), cv(year) -1] ) / 
   sales[cv(prod), cv(year) -1] )

---
sales['Mouse Pad', FOR year FROM 2005 TO 2012 INCREMENT 1]


--引用

SELECT SUBSTR(country,1,20) country, year, localsales, dollarsales
         FROM sales_view
         WHERE country IN ( 'Canada', 'Brazil')
         GROUP BY country, year
         MODEL RETURN UPDATED ROWS 
 REFERENCE conv_refmodel ON (
 SELECT country, exchange_rate AS er FROM dollar_conv)
 DIMENSION BY (country) MEASURES (er) IGNORE NAV
 MAIN main_model 
 DIMENSION BY (country, year)
 MEASURES (SUM(sale) sales, 0 localsales, 0 dollarsales) IGNORE NAV
 RULES (
 /* assuming that sales in Canada grow by 22% */
 localsales['Canada', 2005] = sales[cv(country), 2001] * 1.22,
 dollarsales['Canada', 2005] = sales[cv(country), 2001] * 1.22 *
 conv_refmodel.er['Canada'],
 /* assuming that economy in Brazil grows by 34% */
 localsales['Brazil', 2005] = sales[cv(country), 2001] * 1.34,
 dollarsales['Brazil', 2005] = sales['Brazil', 2001] * 1.34 * er['Brazil']
 )
 /
--ITERATE 子句


-----Oracle 消息

@%ORACLE_HOME%\RDBMS\ADMIN\dbmspipe.sql

dbms_pipe.create_pipe('name')
dbms_pipe.pack_message('MSG')
dbms_pipe.send_message('name')


dbms_pipe.receive_message('name',15)
dbms_pipe.unpack_message(val);//接收值
dbms_pipe.next_item_type;
dbms_pipe.remove_pipe('name');

UTL_TCP包

DECLARE
  c  utl_tcp.connection; 
  ret_val pls_integer;
BEGIN
  c := utl_tcp.open_connection(remote_host => '135.251.218.25',
                               remote_port =>  7001,
                               charset     => 'US7ASCII'); 
  ret_val := utl_tcp.write_line(c, 'GET /mdm HTTP/1.0');
  ret_val := utl_tcp.write_line(c);
  BEGIN
    LOOP
      dbms_output.put_line(utl_tcp.get_line(c, TRUE)); 
    END LOOP;
  EXCEPTION
    WHEN utl_tcp.end_of_input THEN
      NULL; -- end of input
  END;
  utl_tcp.close_connection(c);
END;




data VARCHAR2(256);

utl_tcp.available(c);
utl_tcp.read_text(c, data, 256);

UTL_TCP.CLOSE_ALL_CONNECTIONS;
UTL_TCP.CLOSE_CONNECTION (c);
UTL_TCP.FLUSH (c);


包
UTL_SMTP
UTL_URL
UTL_HTTP

JMS 调用Java   Advanced Queuing (AQ)  
%ORACLE_HOME%\RDBMS\ADMIN\dbmsaqad.sql   ad=administrative
dbms_aqadm包

Alter system set AQ_TM_PROCESSES=6
Alter system set JOB_QUEUE_PROCESSES=10


------

--------job定时
dbms_scheduler包

参数：job_queue_processes=4  (默认4) 最多可同时运行几个job

select job,next_date,next_sec,failures,broken  from user_jobs 

select * from dba_jobs_running;

variable job4 number; 

dbms_job.submit(:job4,'my_proc;', next_date =>sysdate+1,interval  => 'sysdate+1/2');
dbms_job.run(:job4);   
dbms_job.remove(:job4);   

dbms_job.broken(21,true);//停止一个job,

dbms_job.what(21,'new_proc;');//修改某个job的procedure名
dbms_job.next_date(v_job,sysdate);  //dbms_job.INTERVAL

SELECT job, what FROM USER_JOBS;

初始参数　open_cursors=300 ,desc  V$open_cursor;

--------------Oracle XML
CREATE TABLE property_tab (
  property_id VARCHAR2(20) PRIMARY KEY,
  propertyxml XMLType ) ;
 
 XMLType 就是以Clob存入数据库的
 
 INSERT INTO property_tab VALUES ('215435783',
  XMLType.CreateXML('<?xml version=''1.0''?>
  <PROPERTY>
    <LOCATION>
      <ADDRESS>768, Blue Coast Drive, Northwood</ADDRESS>
      <CITY>Half Moon Bay</CITY>
      <STATE>CA</STATE>
      <ZIP>98623</ZIP>
    </LOCATION> 
  </PROPERTY>'));


select  NLS_CHARSET_ID('UTF8') from dual;
select extract(PROPERTYXML,'/PROPERTY/LOCATION/ADDRESS') from property_tab;
	返回<ADDRESS>768, Blue Coast Drive, Northwood</ADDRESS>


declare
  xml_str            clob;
  xml_file           Utl_File.file_type;
  offset             NUMBER              := 1;
  buffer             varchar2(4000);
  buffer_size        number              := 2000;
begin
  xml_file := Utl_File.fopen('TEMP_DIR','new.xml','W');		写文件
  xml_str := DBMS_XMLGEN.getXML('select * from property_tab');
  while(offset < dbms_lob.getlength(xml_str))
  loop
     buffer := dbms_lob.substr(xml_str,buffer_size,offset);
     utl_file.put(xml_file,buffer);
     utl_file.fflush(xml_file);
     offset := offset + buffer_size;
  end loop;
  utl_file.fclose(xml_file);
  dbms_lob.freetemporary(xml_str);
end; 


set serveroutput on


-------