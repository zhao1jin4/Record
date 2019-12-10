
===========Sqoop   工具 为Hadoop  和 关系型数据库 导入导出 , Quest  Data Connector for Oracle and Hadoop 是为Sqoop的插件

./server/conf/catalina.properties  文件 
	common.loader= 中多加如下配置项 ~/hadoop-2.4.0/share/hadoop/common/*.jar,
	~/hadoop-2.4.0/share/hadoop/common/lib/*.jar,
	~/hadoop-2.4.0/share/hadoop/mapreduce/*.jar							*/

cp hadoop-2.4.0/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.4.0.jar  sqoop-1.99.3-bin-hadoop200/server/webapps/sqoop/WEB-INF/lib
cp hadoop-2.4.0/share/hadoop/yarn/*.jar  sqoop-1.99.3-bin-hadoop200/server/webapps/sqoop/WEB-INF/lib/				*/


./server/conf/sqoop.properties 修改值 为/etc/hadoop/conf/为正确有路径 ~/hadoop-2.4.0/etc/hadoop/

./bin/sqoop.sh server start 启动提示 (stop) 
	Setting SQOOP_HTTP_PORT:     12000
	Setting SQOOP_ADMIN_PORT:     12001
可在 server/bin/setenv.sh 中配置 

http://127.0.0.1:12000/sqoop/ 要可以仿问,如不行,看tomcat日志

bin/sqoop.sh client  后可加脚本,#开头是注释
sqoop:000>set server --host 127.0.0.1 --port 12000 --webapp sqoop
sqoop:000>set option --name verbose --value true  		
	##如有错误会显示堆栈(@LOGDIR@\sqoop.log)
sqoop:000>show version --all
sqoop:000>show connector --all
sqoop:000>show job --all


sqoop:000>create connection --cid 1 	 
	##会提示输名字,JDBC url,className ,要把jdbc-driver.jar放入 server/lib ,记得放入.jar后,有重新启动过服务
sqoop:000>create job --xid 1 --type import   
	##提示输入SQL示例"select * from student WHERE ${CONDITIONS}" 
	##提示输入partition column可是主键列,是导入到hadoop系统中 ,只建立Job,未执行
sqoop:000>start job --jid 2 ( H2和MySQL 都卡住不动???  curl --request POST  http://127.0.0.1:12000/sqoop/v1/submission/action/1 )

>update job --jid 1
>update connection --xid 1

>delete connection --xid 1
>delete job --jid 1
 
