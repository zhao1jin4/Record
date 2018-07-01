=======================hadoop-2.7 JDK8
hadoop-3.1.0\share\doc\hadoop\index.html  很大一个doc

hadoop-3.1.0\share\hadoop\common\hadoop-common-3.1.0.jar 					中有 core-default.xml
hadoop-3.1.0\share\hadoop\hdfs\hadoop-hdfs-3.1.0.jar 						中有 hdfs-default.xml


hadoop-2.x.0\share\hadoop\mapreduce\hadoop-mapreduce-client-core-2.x.0.jar 	中有 mapred-default.xml
hadoop-2.x.0\share\hadoop\yarn\hadoop-yarn-common-2.x.0.jar					中有 yarn-default.xml
hadoop-2.x.0\share\doc\hadoop  是和使用版本对应的 doc



一个NameNode, 多个DataNode
HDFS不适合存储小文件的原因，每个文件都会产生元信息，当小文件多了之后元信息也就多了，对namenode会造成压力。


hostname 的值 不要有空格

Directed Acyclical Graphs (DAG)
 
===单机模式
要求已经安装 rsync 同步配置用的

etc/hadoop/hadoop-env.sh   配置JAVA_HOME 环境变量 ,最好不要把Java安装在有空格的目录中
	JAVA_HOME=/usr		redhatEL6.4安装openjdk-devel-7

	hostname的值要可以ping通

===本地模式 方便debug
bin/hadoop 有帮助
bin/hadoop version

mkdir input
cp etc/hadoop/*.xml input　　		 */
bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.x.0.jar grep input output 'dfs[a-z.]+'
cat output/*			*/ 显示的是output/part-r-00000文件, 内容为		1       dfsadmin

===单节点假分布 模式
--- etc/hadoop/core-site.xml
<property>
	<name>fs.defaultFS</name>
	<value>hdfs://localhost:9000</value>	 <!--为NameNode的IP 初始手工加-->
</property>

--- etc/hadoop/hdfs-site.xml
<property>
	<name>dfs.replication</name>
	<value>1</value> <!-- 有多少个DataNode 初始手工加 -->
</property>


要配置成不用输入密码登录  ssh localhost
$ ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
$ cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
$ export HADOOP_PREFIX=/home/zh/hadoop/hadoop-2.7.0
测试 ssh localhost 不用密码
 
vi etc/hadoop/hadoop-env.sh 设置JAVA_HOME=具体目录
  
$ bin/hdfs namenode -format  格式化文件系统
$ sbin/start-dfs.sh  启动 NameNode 和 DataNode   可能要密码  日志在$HADOOP_LOG_DIR 中(默认是 $HADOOP_HOME/logs),有secondaryNameNode的日志
对应的就有 sbin/stop-dfs.sh

http://localhost:50070/   NameNode的接口  Utilities->Browser the file system
http://localhost:50070/conf		中有所有的配置信息 ,没有链接进入, 看50070配置来源是programatically
 
$ bin/hdfs dfs -mkdir /input 
$ bin/hdfs dfs -ls / 也可以在 http://localhost:50070/ 中查看目录
$ bin/hdfs dfs -put etc/hadoop/*.xml  /input		//向hdfs上放文件	*/

$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.x.0.jar grep /input /output 'dfs[a-z.]+'
$ bin/hdfs dfs -cat /output/*							*/
/output/part-r-00000 内容为
1       dfsadmin
1       dfs.replication

或者 bin/hdfs dfs -get /output remote-output    //把hdfs的文件取下
	 cat remote-output/*				*/

---学习的
bin/hdfs dfs -rmdir /input
bin/hdfs dfs -rm -r -f /input
bin/hdfs dfs -appendToFile ./NOTICE.txt /user/NOTICE.txt   //本地的append到远程的

$ sbin/stop-dfs.sh 停止

-----单节点的YARN
MapReduce 基于 YARN 的

vi etc/hadoop/mapred-site.xml 增加
	<property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
---etc/hadoop/yarn-site.xml
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
	</property>

	
$ sbin/start-yarn.sh	启动 ResourceManager 和 NodeManager  ,使用jps可看到
http://localhost:8088/  ResourceManager  管理界面,看执行job, Tools ->Configuration 显示所有配置 
http://localhost:8088/conf 	  Tools->Configuration进入
	看到
	<property>
		<name>yarn.resourcemanager.webapp.address</name>
		<value>${yarn.resourcemanager.hostname}:8088</value>
		<source>yarn-default.xml</source>
	</property>

运行 MapReduce Job
$ sbin/stop-yarn.sh


  
==========真分布 Non-Secure 模式
典型的cluster 应用 ,一个机器做 NameNode  和另一个(another)机器做 ResourceManager 是Master
 剩于的机器是DataNode 和 NodeManager 是Slave
 
*-site.xml 来覆盖 *-default.xml

HDFS daemons are NameNode, SecondaryNameNode, and DataNode. 
YARN damones are ResourceManager, NodeManager, and WebAppProxy  ,如有 MapReduce,则还有MapReduce Job History Server 

etc/hadoop/mapred-env.sh 和  etc/hadoop/yarn-env.sh 中的配置
Daemon 							Environment Variable 
--------------------------------------------------------
NameNode 						HADOOP_NAMENODE_OPTS
DataNode 						HADOOP_DATANODE_OPTS
Secondary NameNode 				HADOOP_SECONDARYNAMENODE_OPTS
ResourceManager 				YARN_RESOURCEMANAGER_OPTS
NodeManager 					YARN_NODEMANAGER_OPTS
WebAppProxy 					YARN_PROXYSERVER_OPTS
Map Reduce Job History Server 	HADOOP_JOB_HISTORYSERVER_OPTS 


/etc/profile 配置  export HADOOP_PREFIX=/path/to/hadoop
   
每个节点都使用相同的配置 rsync,sftp

---etc/hadoop/hadoop-env.sh 
HADOOP_LOG_DIR
HADOOP_PID_DIR 
配置中有各种以_OPTS的变量可对JVM参数做修改

HADOOP_HEAPSIZE MB为单位
以_HEAPSIZE结尾的


---etc/hadoop/yarn-env.sh
YARN_LOG_DIR
YARN_HEAPSIZE

---etc/hadoop/core-site.xml
	io.file.buffer.size 	131072

--- etc/hadoop/hdfs-site.xml
	<!--nameNode配置 -->
	<property>
		<name>dfs.namenode.name.dir</name>
		<value>file://${hadoop.tmp.dir}/dfs/name</value> 
		<source>hdfs-default.xml</source>
	</property>
	dfs.namenode.name.dir//逗号分隔多个目录,为redundancy
	dfs.hosts 			//允许的datenode
	dfs.hosts.exclude	//不允许的datenode
	dfs.blocksize	268435456		//256MB			
	dfs.namenode.handler.count	10 //namenode的线程数

	<!--dataNode配置 -->
	<property>
		<name>dfs.datanode.data.dir</name>  
		<value>file://${hadoop.tmp.dir}/dfs/data</value>  <!--${hadoop.tmp.dir}值为 /tmp/hadoop-[hostname]-->
		<source>hdfs-default.xml</source>
	</property>
	//逗号分隔多个目录,

	---学习的
	<property>
		<name>mapreduce.map.speculative</name> <!-- map 如果任务执行很慢,会在另一个节点启动一个相同的备份任务,先执行完的会kill另一个,保证任务尽快执行完成,这里的开关,是要不要开相同的任务-->
		<value>true</value>
		<source>mapred-default.xml</source>
	</property>
	<property>
		<name>mapreduce.reduce.speculative</name>
		<value>true</value>
		<source>mapred-default.xml</source>
	</property>
	<property>
		<name>mapreduce.job.jvm.numtasks</name> <!-- 单个JVM 运行任务最大数,可以减少JVM启动次数 ,-1 无限制 -->
		<value>1</value>
		<source>mapred-default.xml</source>
	</property>

----etc/hadoop/yarn-site.xml
	
	yarn.acl.enable 	默认值false
	yarn.admin.acl 		默认值* ,意思是 anyone. 如空格 no one 
	yarn.log-aggregation-enable  false
	
	--resource manager
	
	yarn.resourcemanager.address  					<hostname>:8032 		 客户端提交job的地址
	yarn.resourcemanager.scheduler.address 			<hostname>:8030
	yarn.resourcemanager.resource-tracker.address 	<hostname>:8031
	yarn.resourcemanager.admin.address				<hostname>:8033
	yarn.resourcemanager.webapp.address				${yarn.resourcemanager.hostname}:8088
	//以下的 如果为host:port那么主机会覆盖 yarn.resourcemanager.hostname 
	yarn.resourcemanager.scheduler.class		org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler
												//或 FairScheduler ,FifoScheduler
	yarn.scheduler.minimum-allocation-mb		1024
	yarn.scheduler.maximum-allocation-mb		8192
	yarn.resourcemanager.nodes.include-path
	yarn.resourcemanager.nodes.exclude-path
	
	--node manager
	yarn.nodemanager.resource.memory-mb			8192		可用物理内存
	yarn.nodemanager.vmem-pmem-ratio			2.1			virtual memory , physical memory 
	yarn.nodemanager.local-dirs					${hadoop.tmp.dir}/nm-local-dir  ,多路径可用逗号分隔
	yarn.nodemanager.log-dirs					${yarn.log.dir}/userlogs		,多路径可用逗号分隔
	yarn.nodemanager.log.retain-seconds			10800	只在 log-aggregation 禁用时
	yarn.nodemanager.remote-app-log-dir			/tmp/logs	只在 log-aggregation 启用时
	yarn.nodemanager.remote-app-log-dir-suffix  logs 只在 log-aggregation 启用时
	yarn.nodemanager.aux-services
	
	---History Serve
	yarn.log-aggregation.retain-seconds				-1 禁用
	yarn.log-aggregation.retain-check-interval-seconds 如设置为0或者-1,则是retain-seconds的10分之1
	
	---health-checker 
	yarn.nodemanager.health-checker.script.path  		一个 script 去周期的执行,如打印ERROR就是认为NodeManager是unhealthy的,会在ResourceManager的黑名单中
	yarn.nodemanager.health-checker.script.opts 	
	yarn.nodemanager.health-checker.script.interval-ms 	
	yarn.nodemanager.health-checker.script.timeout-ms		120000
	 不支持本地磁盘的坏检查,nodemanager有能力作这个事,检查 	yarn.nodemanager.local-dirs 和 yarn.nodemanager.log-dirs 当到达
	 yarn.nodemanager.disk-health-checker.min-healthy-disks 0.25   的值时,侧节点认为unhealthy 
	 
---etc/hadoop/mapred-site.xml 
	--学习的
	<property>
		<name>mapreduce.tasktracker.reduce.tasks.maximum</name>
		<value>2</value>
		<source>mapred-default.xml</source>
	</property>
	<property>
		<name>mapreduce.jobtracker.http.address</name>
		<value>0.0.0.0:50030</value>
	</property>
	<property>
		<name>mapreduce.jobtracker.address</name>
		<value>local</value>
	</property>
	
	mapreduce.framework.name		yarn
	mapreduce.map.memory.mb			1024 每个 map 任务
	mapreduce.map.java.opts		    会覆盖 mapred.child.java.opts -Xmx200m
	
	mapreduce.reduce.memory.mb     1024 每个 reduce 任务
	mapreduce.reduce.java.opts		会覆盖 mapred.child.java.opts
	mapreduce.task.io.sort.mb		100
	mapreduce.task.io.sort.factor   10 决定打开文件数
	mapreduce.reduce.shuffle.parallelcopies 5
	
	---MapReduce JobHistory Server
	mapreduce.jobhistory.address			默认0.0.0.0:10020
	mapreduce.jobhistory.webapp.address 	默认0.0.0.0:19888
	mapreduce.jobhistory.intermediate-done-dir ${yarn.app.mapreduce.am.staging-dir}/history/done_intermediate 中间完成目录
	mapreduce.jobhistory.done-dir			${yarn.app.mapreduce.am.staging-dir}/history/done 
	

etc/hadoop/slaves 文件中列出有的slave的主机名(每一行一个),只用于Helper scripts (ssh通信) , 不会用于Java-based Hadoop configuration

 HADOOP_CONF_DIR目录的文件同步到所有的机器上,所有机器的目录都一样,hdfs和yarn应该是独立的系统用户
 
[hdfs] $HADOOP_PREFIX/bin/hdfs namenode -format <cluster_name>
[hdfs] $HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode
[hdfs] $HADOOP_PREFIX/sbin/hadoop-daemons.sh --config $HADOOP_CONF_DIR --script hdfs start datanode  	
	也可用start-dfs.sh 读etc/hadoop/slaves文件
	
[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager
[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemons.sh --config $HADOOP_CONF_DIR start nodemanager
[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start proxyserver  
	也可以使用 $HADOOP_PREFIX/sbin/start-yarn.sh 读etc/hadoop/slaves文件
	
[mapred] $HADOOP_PREFIX/sbin/mr-jobhistory-daemon.sh --config $HADOOP_CONF_DIR start historyserver
	MapReduce JobHistory Server 	http://localhost:19888/
停止顺序同启动顺序

etc/hadoop/log4j.properties

------Rack Awareness

--- etc\hadoop\capacity-scheduler.xml
	学习的
	<property>
		<name>yarn.scheduler.capacity.root.default.capacity</name>  <!-- default 是名字 对应其它地方的配置-->
		<value>100</value>
		<description>Default queue target capacity.</description>
	</property>


NameNode 的metadata信息在启动后加载到内存,对应磁盘文件是fsimage
/tmp/hadoop-zhaojin/dfs/name/current/ 有fsimage_* 开头的文件 和 edits_* 开对的文件
/tmp/hadoop-zhaojin/dfs/data/current/BP-[nn]-[hostname]-[nn]/current/rbw/ 下有blk_* 开头的文件(存放数据的),及.meta结尾的文件



bin/hdfs dfsadmin -safemode get 查NameNode是否在安全模式下
bin/hdfs dfsadmin -safemode enter  进入安全模式
bin/hdfs dfsadmin -safemode leave   离开安全模式
bin/hdfs dfsadmin -report


==========真分布 Secure 模式

Hadoop  动态增加/删除DataNode节点

-------------------------------Java API
Configuration conf=new Configuration();
FileSystem fs=  FileSystem.get(conf);
Path path=new Path("/java") 
fs.delete(path,true);//recursive
fs.mkdirs();
fs.copyFromLocalFile(new Path("/home/zhaojin/.ssh/known_hosts"), path);
DistributedFileSystem dfs =(DistributedFileSystem)fs;//要使用hadoop jar 运行才可
DatanodeInfo[] infos=dfs.getDataNodeStats();//所有群集中的节点
for(DatanodeInfo info:infos)
{
	System.out.println("----host:"+info.getHostName());
}

DistributedFileSystem dfs =(DistributedFileSystem)fs;
FileStatus status=fs.getFileStatus(new Path(path,"known_hosts"));
System.out.println("modification time:"+status.getModificationTime());

BlockLocation[] locations=fs.getFileBlockLocations(status, 1, 10);
for(int i=0;i<locations.length;i++)
{
	System.out.println("----block "+i+" in "+Arrays.asList( locations[i].getHosts() ));
}
System.out.println("----block-size:"+status.getBlockSize());


打成.jar 带 Main-Class: 
(启动yarn)bin/hadoop jar  xx.jar 	执行后就会建立目录(bin/hdfs dfs -ls / 查看),如jar包中没有Main-Class: ,要在xx.jar后加全类名


// bin/hdfs dfs -rm -r -f /user/zhaojin/myout
//eclipse 带参数 hdfs://127.0.0.1:9000/user/zhaojin/myin hdfs://127.0.0.1:9000/user/zhaojin/myout
// hadoop jar xx.jar myin myout
ToolRunner.run(new Configuration(),new MainMapReduce1(), args);//调用TestHDFS2的run方法

public class MainMapReduce1 extends Configured implements Tool
{
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf=new Configuration();
		String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length!=2)
		{
			System.err.println("Usage world count <in> <out>");
			System.exit(2);
		}
		Job job= Job.getInstance(conf,"world count");//Job名字
		job.setJarByClass(MainMapReduce1.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);//--
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);//要与Mapper后两个模板对应
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		return job.waitForCompletion(true)?0:1;
	}
}

public class TokenizerMapper extends Mapper<Object,Text,Text,IntWritable>
{
	enum Count{
		Error
	}
	private final static IntWritable one=new IntWritable(1);
	private Text word=new Text();
	@Override
	protected void map(Object key, Text value,
			Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException 
	{//value 是一行的内容
		StringTokenizer token=new StringTokenizer(value.toString());
		while(token.hasMoreElements())
		{
			try{
				word.set(token.nextToken());
				context.write(word, one);//对应Mapper后两个模板
			}catch(Exception e)
			{
				context.getCounter(Count.Error)//自己定义的enum
								.increment(1);
			}
		}
	}
}
 
public  class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable>
{
	private final static IntWritable result=new IntWritable(1);
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		int sum=0;
		for(IntWritable value:values)
		{
			sum+=value.get();
		}
		result.set(sum);
		context.write(key, result);
	}
}


bin/hdfs dfs -text /java/program/known_hosts		显示文件内容

bin/hdfs fsck /
 
bin/mapred job -list

 sbin/start-balancer.sh 增加了新的datanode,运行这个脚本,可使每个节点数据多少相似

   

---------------------------------hadoop 子项目 Hive
export HADOOP_HOME=/home/zhaojin/hadoop-2.4.0
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
export HIVE_HOME=/home/zhaojin/apache-hive-0.13.0-bin
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

---------------------------------hadoop 子项目 Pig
<dependency>
  <groupId>org.apache.pig</groupId>
  <artifactId>pig</artifactId>
  <version>0.13.0</version>
</dependency>

可以把命令转换为MapReduce Job

export JAVA_HOME=/usr
bin/pig -x mapreduce #Mapreduce 模式(默认)
bin/pig -x local     #本地模式
java -cp pig-0.12.1.jar org.apache.pig.Main -x local ,使用java启动本地模式

Pig Latin 语句 以;结尾,可以保存为.pig文件,以/* */做为多行注释,--做单行注释,可在任意位置
$ pig -x local xx.pig

grunt> 交互命令以;结尾
grunt>quit
grunt> A = load '/etc/passwd' using PigStorage(':'); 
grunt> B = foreach A generate $0 as id; 
grunt> dump B; 
显示 (root)  行被() 包着
 
>A = LOAD 'student' USING PigStorage() AS (name:chararray, age:int, gpa:float); --默认以tab分隔,可'\t'
>B = FOREACH A GENERATE name;
> describe A;

--script1-local.pig
REGISTER ./tutorial.jar; 
raw = LOAD 'excite-small.log' USING PigStorage('\t') AS (user, time, query);
clean1 = FILTER raw BY org.apache.pig.tutorial.NonURLDetector(query);  --类  extends FilterFunc 
houred = FOREACH clean1 GENERATE user, org.apache.pig.tutorial.ExtractHour(time) as hour, query;  
ngramed1 = FOREACH houred GENERATE user, hour, flatten(org.apache.pig.tutorial.NGramGenerator(query)) as ngram; --类是extends EvalFunc<DataBag> 返回DataBag
ngramed2 = DISTINCT ngramed1;
hour_frequency1 = GROUP ngramed2 BY (ngram, hour);
hour_frequency2 = FOREACH hour_frequency1 GENERATE flatten($0), COUNT($1) as count; -- COUNT参数是bag中
hour00 = FILTER hour_frequency2 BY hour eq '00';
hour12 = FILTER hour_frequency2 BY hour eq '12';
same = JOIN hour00 BY $0, hour12 BY $0;--类似数组合并
STORE same INTO '/tmp/tutorial-join-results' USING PigStorage();//保存在/tmp/tutorial-join-results/part-r-00000




 
DataBag bag = DefaultBagFactory.getInstance().newDefaultBag();
Tuple t1 = TupleFactory.getInstance().newTuple(3);
t1.set(0, "word");//子元素可以是基本类型,也可以是Bag
t1.set(1, "02");
t1.set(2, 2);// Tuple 是(,,) 如 (word,02,2)
bag.add(t1);//Bag 是{,,} 如 {(word,02,2)}
Iterator<Tuple> it = bag.iterator();

extends FilterFunc {
 public Boolean exec(Tuple arg0) throws IOException {
	arg0.get(0);
 }
}
extends EvalFunc<T> {
    public T exec(Tuple input) throws IOException {
	}
}



---------------------------------hadoop 子项目 ambari 是一个web工具 用于,监控,管理
 
只可对只定的64位linux 系统

源码安装依赖于 
	rpm-build(rpmbuild命令),
	npm(node.js的)
	brunch 			npm install -g brunch 在线安装
	postgresql-server
	python-setuptools
	
Python2.7
git
---2.0 版本 apache-ambari-2.0.0-src.tar.gz
mvn versions:set -DnewVersion=2.0.0.0
pushd ambari-metrics
mvn versions:set -DnewVersion=2.0.0.0 #报错？？？
popd

mvn -B clean install package rpm:rpm -DnewVersion=2.0.0.0 -DskipTests -Dpython.ver="python >= 2.6"


rpm -ivh ambari-server/target/rpm/ambari-server/RPMS/noarch/ambari-server-1.5.1-1.noarch.rpm
#yum(zypper SUSE用) install ambari-server*.rpm		会装 postgresql-server

禁用selinux
root 用户执行 ambari-server setup	 配置  PostgreSQL(默认用户 ambari/bigdata,数据库ambari) , JDK ,会启动postgresql
ambari-server start/stop

chkconfig ambari-server off

http://<ambari-server-host>:8080  初始用户名密码 admin / admin



-------------
[INFO] --- copy-maven-plugin:0.2.5:copy (create-archive) @ ambari-agent ---
[WARNING] Error injecting: com.github.goldin.plugins.copy.CopyMojo

java.lang.NoClassDefFoundError: Lorg/sonatype/aether/RepositorySystem;

/.m2/repository/org/sonatype/aether/aether-api/1.13.1/ 没有.jar,加了jar 还是报???

Ambari Agent

[ERROR] Failed to execute goal com.github.goldin:copy-maven-plugin:0.2.5:copy (create-archive) on project ambari-agent: Execution create-archive of goal com.gi         thub.goldin:copy-maven-plugin:0.2.5:copy failed: A required class was missing while executing com.github.goldin:copy-maven-plugin:0.2.5:copy: Lorg/sonatype/aether/RepositorySystem;

Caused by: java.lang.ClassNotFoundException: org.sonatype.aether.RepositorySystem
-------------

在 ambari-agent/target/rpm/ambari-agent/RPMS/x86_64/ 
yum(zypper) install ambari-agent*.rpm

--/etc/ambari-agent/ambari.ini 修改主机名
[server]
hostname=localhost

ambari-agent start



===========Sqoop   工具 为Hadoop  和 关系型数据库 导入导出 , Quest  Data Connector for Oracle and Hadoop 是为Sqoop的插件

./server/conf/catalina.properties  文件 
	common.loader= 中多加如下配置项 /home/zhaojin/hadoop-2.4.0/share/hadoop/common/*.jar,
	/home/zhaojin/hadoop-2.4.0/share/hadoop/common/lib/*.jar,
	/home/zhaojin/hadoop-2.4.0/share/hadoop/mapreduce/*.jar							*/

cp hadoop-2.4.0/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.4.0.jar  sqoop-1.99.3-bin-hadoop200/server/webapps/sqoop/WEB-INF/lib
cp hadoop-2.4.0/share/hadoop/yarn/*.jar  sqoop-1.99.3-bin-hadoop200/server/webapps/sqoop/WEB-INF/lib/				*/


./server/conf/sqoop.properties 修改值 为/etc/hadoop/conf/为正确有路径 /home/zhaojin/hadoop-2.4.0/etc/hadoop/

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
 


===========Flume	( 水槽) 分布式海量日志采集、聚合和传输
一个agent有三个部分
	Source 接入数据
	Channel 暂存数据 
	Sink   输出数据


bin/flume-ng agent -n a1 -c conf -f conf/flume-conf.properties.template     #a1 是配置文件中的agent的名字
 
-----conf/flume-conf.properties  示例配置 
 # Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat    
#type可以为avro

a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1


 bin/flume-ng agent -n a1 -c conf -f conf/flume-conf.properties   -Dflume.root.logger=INFO,console   #a1 是配置文件中的agent的名字
 
本地浏览器仿问   http://127.0.0.1:44444 ,可以看到控制台显示请求的信息

conf/flume-env.sh 文件 FLUME_CLASSPATH 变量指向的目录中放jar包插件 
$FLUME_HOME/plugins.d  目录中的插件自动识别,插件目录的子级目录结构要有,lib目录,libext目录(放依赖的jar),native目录
 
plugins.d/custom-source-1/
plugins.d/custom-source-1/lib/my-source.jar
plugins.d/custom-source-1/libext/spring-core-2.5.6.jar
plugins.d/custom-source-2/
plugins.d/custom-source-2/lib/custom.jar
plugins.d/custom-source-2/native/gettext.so


$ bin/flume-ng avro-client -H localhost -p 44444 -F README   #把文件的内容使用avro 发送到Source类型为avro的channel上


可以有多个agent 串行连接,但Source和Sink 的type要为avro
如果是多个agent(Web Server) 连接到一个agent,要使用thrift 

一个Source可以指定多个Channel
一个Sink只能指定一个Channel


=============Avro
支持C++ ,Python,C#

<dependency>
  <groupId>org.apache.avro</groupId>
  <artifactId>avro</artifactId>
  <version>1.8.2</version>
</dependency> 
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.13</version>
</dependency>

使用JSON定义 Avro schemas

原始数据类型 (null, boolean, int, long, float, double, bytes, and string) 
复杂数据类型 (record, enum, array, map, union, and fixed)

user.avsc:

{"namespace": "example.avro",
 "type": "record",
 "name": "User",
 "fields": [
     {"name": "name", "type": "string"},
     {"name": "favorite_number",  "type": ["int", "null"]},
     {"name": "favorite_color", "type": ["string", "null"]}
 ]
}
 
schemas 至少有   type ("type": "record"),   name ("name": "User")
全名是 example.avro.User
java -jar /path/to/avro-tools-1.8.2.jar compile schema <schema file> <destination>
java -jar /path/to/avro-tools-1.8.2.jar compile schema user.avsc .


// Construct via builder
User user3 = User.newBuilder()
			 .setName("Charlie")
			 .setFavoriteColor("blue")
			 .setFavoriteNumber(null)
			 .build();
					 
// Serialize user1, user2 and user3 to disk
DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
dataFileWriter.create(user1.getSchema(), file); 
dataFileWriter.append(user1);
dataFileWriter.append(user2);
dataFileWriter.append(user3);
dataFileWriter.close();//保存的文件内容就是JSON schema

 
// Deserialize Users from disk
DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
User user = null;
while (dataFileReader.hasNext()) { 
	user = dataFileReader.next(user);
	System.out.println(user);
}
//输出的就是JSON  

---DemoService.avdl
@namespace ("hadoop.avro.transfer")
protocol DemoService
{
    import schema "Person.avsc";
    import schema "QueryParameter.avsc";
    string ping();
    array<hadoop.avro.transfer.Person> getPersonList(hadoop.avro.transfer.QueryParameter queryParameter);
}
#转换 .avdl 文件到 .avpr 文件用
java -jar avro-tools-1.8.2.jar idl DemoService.avdl DemoService.avpr
java -jar avro-tools-1.8.2.jar compile protocol  DemoService.avpr  .
---Person.avsc
{
  "namespace": "hadoop.avro.transfer",
  "type": "record",
  "name": "Person",
  "fields": [
    {
      "name": "age",
      "type": "int"
    },
    {
      "name": "name",
      "type": "string"
    },
    {
      "name": "sex",
      "type": "boolean"
    },
    {
      "name": "salary",
      "type": "double"
    },
    {
      "name": "childrenCount",
      "type": "int"
    }
  ]
}
---QueryParameter.avsc
{
  "namespace": "hadoop.avro.transfer",
  "type": "record",
  "name": "QueryParameter",
  "fields": [
    {
      "name": "ageStart",
      "type": "int"
    },
    {
      "name": "ageEnd",
      "type": "int"
    }
  ]
}
--server
//        Server nettyServer = new NettyServer(new SpecificResponder(DemoService.class,
//                new DemoServiceImpl()),
//                new InetSocketAddress(65111));
        //二选 一
//
        Server saslSocketServer = new SaslSocketServer(new SpecificResponder(DemoService.class,
                new DemoServiceImpl()),
                new InetSocketAddress(10000));
 
--client
//NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
//二选 一
SaslSocketTransceiver client = new SaslSocketTransceiver(new InetSocketAddress(10000));

DemoService proxy = (DemoService) SpecificRequestor.getClient(DemoService.class, client);
System.out.println(proxy.ping());

QueryParameter parameter = new QueryParameter();
parameter.setAgeStart(5);
parameter.setAgeEnd(50);
proxy.getPersonList(parameter);

client.close();
===========Tez   替代 MapReduce 也是基于YARN


===========Oozie [u:zi] 驯象人
	a workflow scheduler system to manage Apache Hadoop jobs
	 
===========Helix


===========MRUnit  MapReduce 的单元测试
=============Greenplum  基于 PostgreSQL
=============Nutch  搜索引擎   全文搜索和Web爬虫
