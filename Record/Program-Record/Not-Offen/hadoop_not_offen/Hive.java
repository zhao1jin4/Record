 

---------------------------------hadoop 子项目 Hive
SQuirrel 界面工具可以仿问Hive
http://lxw1234.com/archives/2015/09/519.htm

将sql语句转换为MapReduce任务


export HADOOP_HOME=~/hadoop-2.4.0
启动 $HADOOP_HOME/sbin/start-dfs.sh

$HADOOP_HOME/bin/hadoop fs -mkdir       /tmp
$HADOOP_HOME/bin/hadoop fs -mkdir       /user/hive/warehouse
$HADOOP_HOME/bin/hadoop fs -chmod g+w   /tmp
$HADOOP_HOME/bin/hadoop fs -chmod g+w   /user/hive/warehouse

bin/hive 命令行工具,要启动dfs
hive>show tables;     ##像MySQL
hive> CREATE TABLE pokes (foo INT, bar STRING);
hive> DESCRIBE pokes;
hive>ALTER TABLE pokes ADD COLUMNS (new_col INT);
hive>DROP TABLE pokes;

--可以不启动服务
export HIVE_HOME=~/apache-hive-0.13.0-bin
cp conf/hive-default.xml.template  conf/hive-site.xml
	<property>
	  <name>hive.exec.mode.local.auto</name>
	  <value>false</value>
	</property>
mkdir -p $HIVE_HOME/hcatalog/var/log	
hcatalog/sbin/hcat_server.sh  start   就是使用 ./hive  --service metastore 启动 (失败???)
hcatalog/bin/hcat  -e 'create table mytable(a int);'    ## 提示HIVE_HOME未设置,-f 指定文件中有DDL

hcatalog/sbin/webhcat_server.sh  start (OK)


cp conf/hive-log4j.properties.template conf/hive-log4j.properties

修改配置 可
	bin/hive -hiveconf x1=y1
	环境变量HIVE_OPTS="-hiveconf x1=y1"
	hive>set -v 查所有的设置
	hive>set x=y

bin/hive -hiveconf hive.root.logger=INFO,console   在 hive-log4j.properties 有配置 



hive>set mapreduce.jobtracker.address=local;
hive> set -v  查全部 ,有 hive.exec.mode.local.auto.inputbytes.max , hive.exec.mode.local.auto.input.files.max
hive.mapred.local.mem

 

