=======================hadoop-2.7 JDK8

hadoop-2.x.0\share\hadoop\common\hadoop-common-2.x.0.jar 					中有 core-default.xml
hadoop-2.x.0\share\hadoop\hdfs\hadoop-hdfs-2.x.0.jar 						中有 hdfs-default.xml
hadoop-2.x.0\share\hadoop\mapreduce\hadoop-mapreduce-client-core-2.x.0.jar 	中有 mapred-default.xml
hadoop-2.x.0\share\hadoop\yarn\hadoop-yarn-common-2.x.0.jar					中有 yarn-default.xml
hadoop-2.x.0\share\doc\hadoop  是和使用版本对应的 doc
hbase-common-1.0.1.jar														中有 hbase-default.xm

一个NameNode, 多个DataNode

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
$ sbin/start-dfs.sh  启动 NameNode 和 DataNode   可能要密码  日志在$HADOOP_LOG_DIR 中(默认是 $HADOOP_HOME/logs),有secondarynamenode的日志
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

 
---------------------------------hadoop 子项目 zookeeper
zookeeper-3.4.6\conf\zoo_sample.cfg 修改为  zoo.cfg
bin/zkServer.sh start  / start-foreground / stop 
bin/zkServer.sh status 看是leader

----zoo.cfg
clientPort=2181  	监听端口
tickTime=2000 		心跳时间2秒
dataDir=/tmp/zookeeper

#集群配置
initLimit=5 	初始化连接时最长能忍受多少个心跳,表示leader服务器允许follow服务器启动时同步的最大时间()
syncLimit=2 	发送消息最长不能超过多少个心跳,表示leader服务器和follow服务器的心跳检查最大延时时间
server.1=192.168.211.1:2888:3888 
server.2=192.168.211.2:2888:3888
server.1=192.168.211.1:2888:3888    

#server.第几号服务器=IP:Leader的端口:Leader备份端口
#至少3台服务器,启动两台就服务 ,在dataDir配置项指定的目录中建立myid文件,中写自己的ID号,集群中多台机器各不相同,与server.1 等匹配

dataLogDir 			在写dataDir前,先写这个,为保证速度应该放在不同磁盘
globalOutstandingLimit    最大请求未处理队列数,默认1000
preAllocSize  			事务日志块的大小,默认64m
snapCount				多少次事务后 建立一次快照
maxClientCnxns			单台客服端(用IP) 和服务器的最大连接数,默认60,0表示没有限制
minSessionTimeout
maxSessionTimeout
fsync.warningthresholdms		当同步日志时间大于这个(milliseconds ),会有warn消息写到日志
autopurge.snapRetainCount		自动清理快照文件 和事务日志文件时,保留的数量,最小配置为3
autopurge.purgeInterval			0 不启用清理快照文件 和事务日志文件,单位小时
leaderServes				是否要leader处理客户端请求,默认yes
cnxTimeout					连接超时时间,默认5
forceSync					完成操作前是否时时写入日志到磁盘			
jute.maxbuffer 		必须用java system properties 设置才生效,必须每个服务和客户端都设置 ,表示一个znode最大存储储数据量,默认1m



bin/zkCli.sh -server 127.0.0.1:2181   可以选项 -timeout 0 毫秒  -r 表示只读，如有超过半数服务连接断开，就不处理客户端请求，但可以处理只读请求
] ls /
] create /zk_test my_data   可加-s 表示序列,节点名后加序列号,-e 表示临时
] create /acl_ip_test ip:10.1.5.225:crwda   			crwda=create,read,write,delete,admin
] create /acl_digest_test digest:myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=:crwda   中间myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=是使用DigestAuthenticationProvider.generateDigest("myuser:mypass")生成的
也可以使用 setAcl

] get /zk_test

] addauth digest user:pass  如有节点不能get ,可以加用户密码

] set /zk_test junk   可加版本号,必须是上一次返回的版本,可以实现乐观锁
] stat /zk_test

cZxid = 0x0			每一次写是一次事务,有一个ID标识,表示建立时的事务ID,即在哪个事务中建立的
ctime = Thu Jan 01 08:00:00 CST 1970	创建时间
mZxid = 0x0							    最后一次更新时的事务ID	
mtime = Thu Jan 01 08:00:00 CST 1970    修改时间
pZxid = 0x600000008						子节点里最后一次修改的事务ID (不包括修改子节点的数据内容,只是增删节点)
cversion = 2
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 2

] delete /zk_test
] ls2 /zk_test 		有stat的功能
] rmr /dir  		可以删有子级点的节点
] setquata -n 表示子级点的个数 -b 数据值的长度,包括子级节点 val path  ,加节点限制功能(不能修改,只能删了再建) ,如有有超出限制不会报错,只记录WARN日志
] listquota
] delquota  -n /-b path
] history   查历史
] redo history编号
] quit 
] connect 连接到其它机器   close 后无法再返回了


4个字母的命令
echo stat | nc  127.0.0.1 2181  也可以用telent
echo conf | nc  127.0.0.1 2181  查看配置
echo cons | nc  127.0.0.1 2181  查看所有客户端
echo crst | nc  127.0.0.1 2181   重置所有客户端的连接统计信息（Client Reset STatistics ）
echo srst | nc  127.0.0.1 2181   重置所有服务连接统计信息（Server Reset STatistics ）
echo dump | nc  127.0.0.1 2181    只用于leader ,显示会话信息
echo envi | nc  127.0.0.1 2181    
echo ruok | nc  127.0.0.1 2181   ,are you ok ,    当前服务是否在运行
echo srvr | nc  127.0.0.1 2181   服务器信息
echo wchs | nc  127.0.0.1 2181  显示watcher
echo wchc  | nc  127.0.0.1 2181  显示watcher,以sessoin列出
echo wchp  | nc  127.0.0.1 2181   by path
echo mntr  | nc  127.0.0.1 2181    键值输出信息


cd <zookeeper_home>/src/c 
./configure
make cli_mt		(multi-threaded) 
make cli_st		(single-threaded)
make install

cd src/c 
./cli_mt
./cli_st




ZooKeeper zk = new ZooKeeper("localhost:2181",2000, new Watcher() { 
	public void process(WatchedEvent event) { 
		System.out.println("已经触发了" + event.getType() + "事件！"); 
	} 
});


 ACL aclIp=new ACL(Perms.READ,new Id("ip","10.1.5.225"));
 ACL aclDigest=new ACL(Perms.READ,new Id("digest",DigestAuthenticationProvider.generateDigest("myuser:mypass")));
 ArrayList<ACL> aclList=new ArrayList<>();
 aclList.add(aclIp);
 aclList.add(aclDigest);
 zk.create("/testAclPath", "testAclPathVal".getBytes(),aclList, CreateMode.PERSISTENT); 
//		  //使用命令   getAcl /testAclPath  能看到
 zk.addAuthInfo("digest", "myuser:mypass".getBytes());//同命令  addauth digest  myuser:mypass
 System.out.println(new String(zk.getData("/testAclPath",false,null))); 
		 
		 
zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 

System.out.println(new String(zk.getData("/testRootPath",false,null))); 
System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]"); 
zk.setData("/testRootPath","modifyData".getBytes(),-1); 
zk.delete("/testRootPath",-1); 
zk.close();

extends ZooKeeperServerMain //类里有main方法
	this.initializeAndRun("/tmp/zoo.cfg")


CreateMode.EPHEMERAL //临时,客户端连接断会被删 ,ephemeral 短暂的
CreateMode.EPHEMERAL_SEQUENTIAL //临时,名字按序号自动加1 ,选 Master 时,以最小的号
 
// Ids.OPEN_ACL_UNSAFE 任何人都可以访问
// Ids.AUTH_IDS 创建者拥有访问权限
// Ids.READ_ACL_UNSAFE  任何人都可以读
//Ids.CREATOR_ALL_ACL //使用addAuthInfo信息,作为建立节点的acl信息

配置 JMX
修改 zkServer.sh   中
ZOOMAIN="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=10.1.5.226 -Dcom.sun.management.jmxremote.port=8899 -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.authenticate=false"

重新启动后，就可以用jconsole来连接   10.1.5.226:8899


Zabbix  ,exhibitor 来临控

exhibitor 下载源码后 
cd  exhibitor-master\exhibitor-standalone\src\main\resources\buildscripts\war(或者standalone)\maven  
mvn clean package
把生成的.jar放服务器上
java -jar exhibitor-1.5.6.jar -c file   --port 8888 启动用,打开 http://127.0.0.1:8888/

config 面板 Servers配置中S=Standard,O=observer  
对应zoo.cfg配置如 S:1:10.1.5.225,S:2:10.1.5.226,S:3:10.1.2.159,  每台机器运行jar才可以,
配置后Control Panel就会每台机器的例表,4LTR...按钮可以运行命令
提交配置会重写配置文件,小心

---------------------------------hadoop 子项目 HBase  要使用基于hadoop2版本的
Log-Structured Merge-Trees (LSM)

要linux环境运行
构建在 Apache Hadoop和 Apache ZooKeeper之上



conf/hbase-site.xml 	 增加
	<property>
		<name>hbase.rootdir</name>
		<value>file:///home/zhaojin/hbase-0.98.3-hadoop2/</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.dataDir</name>
		<value>/tmp/zookeeper</value>
	</property>
  
	zookeeper.znode.parent  默认是　/hbase
	hbase.master.info.port	默认是	60010
 
	
./conf/hbase-env.sh 中修改　export JAVA_HOME=/usr
./bin/start-hbase.sh 启动  

http://<hbase-server-ip>:60010  自动进入/master-status  ,HBase Configuration 页有显示所有配置(包括默认值)
http://localhost:60010/conf


./bin/hbase shell
表名，行和列需要加引号
> create 'test', 'cf'		--名为 test 的表，这个表只有一个 列族 为 cf
> list  				--所有的表
>put 'test', 'row1', 'cf:a', 'value1'		--key为row1, 列为 cf:a， 值是 value1
>scan 'test'		--查所有
>get 'test', 'row1' --指定行key
>get 'test', 'row1' ,'cf'  
>get 'test', 'row1' ,'cf:a'
>status
>version
>describe 'test'
>delete  'test','row1','cf:a'		#删列数据
>deleteall 'test' ,'row1'					#删行数据
>alter 'test',{NAME=>'cf',METHOD=>'delete'}  #删列
>exists 'test'  不对???
> is_enabled 'test'
>is_disabled 'user'
>count 'test'   			#查 rowkey的行数
>truncat 'test'				#删所有数据,日志显示disable,drop,create
> disable 'test'
> drop 'test'
> exit
>t = create 'table1', 'f'
>t = get_table 'table1'
>t.put 'r', 'f', 'v'
>t.scan
>t.describe
>t.disable
>t.drop

>tables = list('t.*')			 所有表名以t开头的
>tables.map { |t| disable t ; drop  t}  全删它们|t|是迭代的中间变量

> import java.util.Date
> Date.new(1218920189000).toString()
> debug 		
 ./bin/hbase shell -d  打开debug更多的信息
 
 
./bin/stop-hbase.sh 停止

linux xmllint 工具用来检查XML格式是否正确
linux rsync 工具用来同步文件

/etc/security/limits.conf 
hadoop  -       nofile  32768			 第一列是操作系统的用户名
hadoop soft/hard nproc 32000
 
	  
分单机模式 , 所有的服务和zooKeeper都运作在一个JVM中, hbase.rootdir 使用本地文件系统 file:///  
 
分布式模式 ,hbase.rootdir 使用 hdfs:// 
分布式Hadoop版本jar文件替换HBase lib目录下的Hadoop jar文件
hbase-env.sh 中修改 HBASE_CLASSPATH=/home/zhaojin/hadoop-2.4.0/etc/hadoop/  使用HDFS
ln -s /home/zhaojin/hadoop-2.4.0/etc/hadoop/hdfs-site.xml  /home/zhaojin/hbase-0.98.3-hadoop2/conf/

--hbase-site.xml
<property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
    <description>若为false,HBase和Zookeeper会运行在同一个JVM里面</description>
 </property>
  
<property>
    <name>hbase.zookeeper.quorum</name>
    <value>example1,example2,example3</value>
    <description> </description>
</property> 
默认是localhost,是给伪分布式用的。要修改才能在完全分布式的情况下使用。
如果在hbase-env.sh设置了HBASE_MANAGES_ZK,这些ZooKeeper节点就会和HBase一起启动
	
修改conf/regionservers 像 hadoop-2.4.0/etc/hadoop/slaves 一行写一个主机名
 
 
zookeeper.session.timeout			默认值是3分钟,调短,长时间的GC操作就可能导致超时
hbase.regionserver.handler.count	处理用户请求的线程数量。默认是10


 
尽量让你的列族数量少一些
如果列族A有100万行，列族B有10亿行，列族A可能被分散到很多很多区(及区服务器)。这导致扫描列族A低效
行键要尽量避免时间戳或者(e.g. 1, 2, 3)这样的key, 如日志可 OpenTSDB,的schema做法,升序排 [key][Long.MAX_VALUE - timestamp]
尽量使列族名小，最好一个字符,最好还是用短属性名


hbase org.apache.hadoop.hbase.io.hfile.HFile -v -f hdfs://<ip>/xfile 


//---
String tableName = "myTable";
Configuration config = HBaseConfiguration.create();
config.set("hbase.zookeeper.quorum", "localhost");
config.set("hbase.rootdir", "file:///home/zhaojin/hbase-0.98.3-hadoop2"); 
config.set("hbase.zookeeper.property.dataDir", "/home/zhaojin/hbase-0.98.3-hadoop2/data"); 
config.set("zookeeper.znode.parent", "/hbase"); // 默认是　/hbase
 
HBaseAdmin admin = new HBaseAdmin(config);   
HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));
HTable htable = new HTable(config,tableName);

HTableDescriptor [] tables = admin.listTables();  
if(tables.length>1 )
{
	byte [] byteTableName= htd.getName();
	for (HTableDescriptor tab:tables)
	{
		if(Bytes.equals(byteTableName,tab.getName())) 
		{
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("  table dropted");
			 break;
		}
	}
}
admin.createTable(htd);
admin.disableTable(tableName);
HColumnDescriptor cf1 = new HColumnDescriptor("cf");
admin.addColumn(tableName, cf1);      // adding new ColumnFamily
admin.enableTable(tableName); 
admin.close(); 

Put put = new Put(Bytes.toBytes("row8"));
put.add(Bytes.toBytes("cf"), Bytes.toBytes("attr"), Bytes.toBytes( "data8"));
htable.put(put);
Get get = new Get(Bytes.toBytes("row8"));
get.setMaxVersions(3);
Result r = htable.get(get);
byte[] b = r.getValue(Bytes.toBytes("cf"), Bytes.toBytes("attr"));
System.out.println("===="+ Bytes.toString( b));
List<Cell> kv = r.getColumnCells(Bytes.toBytes("cf"), Bytes.toBytes("attr"));//版本有3个,为什么没有老版本的???

Scan scan = new Scan();
scan.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("attr"));
//		scan.setStartRow( Bytes.toBytes("row"));                   // start key is inclusive
//		scan.setStopRow( Bytes.toBytes("row" +  (char)2));  // stop key is exclusive

//--filter
FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE);
SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
		Bytes.toBytes("cf"),
		Bytes.toBytes("attr"),
		CompareOp.EQUAL,
		//Bytes.toBytes("value1")
		//new RegexStringComparator("my.")
		new SubstringComparator("dat")
		);
list.addFilter(filter1);
list.addFilter(new ColumnPrefixFilter(Bytes.toBytes("att")));
//list.addFilter(new MultipleColumnPrefixFilter(new byte[][]{Bytes.toBytes("att"),Bytes.toBytes("myattr")}));
list.addFilter(new ColumnRangeFilter(Bytes.toBytes("attr"), true, Bytes.toBytes("myattr"), true));
scan.setFilter(list);
scan.addFamily(Bytes.toBytes("cf"));
ResultScanner rs = htable.getScanner(scan);
for (Result r = rs.next(); r != null; r = rs.next()) 
{
  Cell[] cells=r.rawCells();
  for(Cell cell:cells)
  {
	  System.out.println(new String(CellUtil.cloneValue(cell)));
  }
}
rs.close();
htable.close();

	
//---- mapred代表的是hadoop旧API , 而mapreduce代表的是hadoop新的API

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;

//依赖于 hbase-hadoop-compat-0.98.3-hadoop2.jar  很特别 (有hbase-hadoop2-compat-0.98.3-hadoop2.jar)
Configuration config = HBaseConfiguration.create();
Job job = Job.getInstance(config, "ExampleRead");

Scan scan = new Scan();
scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
scan.setCacheBlocks(false);  // don't set to true for MR jobs
scan.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("attr"));
//scan.setStartRow( Bytes.toBytes("row"));                   // start key is inclusive
//scan.setStopRow( Bytes.toBytes("row" +  (char)0));  // stop key is exclusive


TableMapReduceUtil.initTableMapperJob(
		  "myTable",        // input HBase table name
		  scan,             // Scan instance to control CF and attribute selection
		  MyMapper.class,   // mapper
		  Text.class,         // mapper output key
		  IntWritable.class,  // mapper output value
		  job);
  
//-------方式一
job.setReducerClass(MyTableReducer.class);    // reducer class
job.setNumReduceTasks(1);    // at least one, adjust as required
FileOutputFormat.setOutputPath(job, new Path("/tmp/mr/mySummaryFile"));  // adjust directories as required
//-------方式二
//		TableMapReduceUtil.initTableReducerJob(
//		  "myTable",    
//		  MyTableReducer.class,  
//		  job);

job.setOutputFormatClass(NullOutputFormat.class);   // because we aren't emitting anything from mapper
job.setNumReduceTasks(0);

boolean b = job.waitForCompletion(true);
if (!b) {
  throw new IOException("error with job!");
} 

 class MyTableReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>  //<KEYIN,VALUEIN,KEYOUT>
 {
 	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    		int i = 0;
    		for (IntWritable val : values) {
    			i += val.get();
    		}
    		Put put = new Put(Bytes.toBytes(key.toString()));
    		put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(i));
    		context.write(null, put);
   	}
}
class MyMapper extends TableMapper<Text, IntWritable>  //<KEYOUT,VALUEOUT>
{
	private final IntWritable ONE = new IntWritable(1);
	private Text text = new Text();
	public void map(ImmutableBytesWritable row, Result value, Context context) 
			throws IOException, InterruptedException 
	{
			String val = new String(value.getValue(Bytes.toBytes("cf"), Bytes.toBytes("attr1")));
			text.set(val);     // we can only emit Writables...
			context.write(text, ONE);
			System.out.println("====value:"+value+",row===="+row);
	}
}

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



---------------------------------hadoop 子项目 Spark 比MapReduce 快
spark-1.0.0-bin-hadoop2.tgz
<dependency>
  <groupId>org.apache.spark</groupId>
  <artifactId>spark-core_2.10</artifactId>
  <version>1.0.0</version>
</dependency>


scala Maven

./bin/spark-shell	使用 Scala 语言, 提示SparkUI  http://127.0.0.1:4040 
scala> 

./bin/pyspark		使用 Python 语言
>>>exit();


scala>var rdd=sc.parallelize(List(1,2,3,4,5,6))    //在打sc.  (SparkContext)可以按tab键自动输出
scala>val mapRdd=rdd.map(2 * _)    //每个元素*2
scala>mapRdd.collect  // 执行collect方法,返回Array
scala>mapRdd   //看
scala>val filterRdd=mapRdd.filter( _>5) //过滤 >5 的
scala>val filterRdd2=sc.parallelize(List(1,2,3,4,5,6)).map(2 * _).filter( _>5) // 一行式写法，scala
scala>var rdd1=sc.textFile("/home/zh/world.txt") // 以空格分格的字母文件,路径如C:是不行的
scala>rdd1.cache
scala>rdd1.count
scala>val worldCount=rdd1.flatMap(_.split(' ')).mpa((_,1)).reduceByKey(_+_)	  
	// (_,1) 同 x=>(x,1) ,把文件中每个字母做为key,value为1
	// .reduceByKey(_+_) 表示把相同的字母的key的值相加
scala>val worldCount=rdd1.flatMap(_.split(' ')).mpa((_,1)).groupByKey  //相同的key的值，放在一个Array中	 
scala>worldCount.collect
scala>worldCount.saveAsTextFile("/home/zh/output.txt")
scala>
scala>var result = rdd union udd2   //可能要相同类型的
scala>var result = rdd join udd2   //可能要相同类型的
scala>
scala>
scala>
scala>
scala>
scala>
scala>
scala>
scala>
scala>






Resilient Distributed Dataset (RDD)

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class TestSpark {
 public static void main(String[] args) {
   String logFile = "/home/zhaojin/spark-1.0.0-bin-hadoop2/README.md";
   SparkConf conf = new SparkConf().setAppName("Simple Application");//如加.setMaster("local[4]");就可在eclipse中运行
   JavaSparkContext sc = new JavaSparkContext(conf);
   JavaRDD<String> logData = sc.textFile(logFile).cache();//textFile方法第二个参数可加配置分割文件数量,但不能小块大小,HDFS默认是64M

   long numAs = logData.filter(new Function<String, Boolean>() {//第一个传入数类型,最后一个返回参数类型,如两相参数用Function2
     public Boolean call(String s) { return s.contains("a"); }
   }).count();

   long numAsJdk8 = logData.filter((str)->str.contains("a")).count();//JDK8 版本
	
   long numBs = logData.filter(new Function<String, Boolean>() {
     public Boolean call(String s) { return s.contains("b"); }
   }).count();

   System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
   
 }
}
 
 public static void main(String[] args) throws Exception 
 {
	String logFile = "/home/zhaojin/spark-1.0.0-bin-hadoop2/README.md";
    SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local[4]");
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);
    JavaRDD<String> lines = ctx.textFile(logFile, 1);

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterable<String> call(String s) {
        return Arrays.asList(SPACE.split(s));
      }
    });

    JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
      @Override
      public Tuple2<String, Integer> call(String s) {
        return new Tuple2<String, Integer>(s, 1);
      }
    });

    JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
      @Override
      public Integer call(Integer i1, Integer i2) {
        return i1 + i2;
      }
    });

    List<Tuple2<String, Integer>> output = counts.collect();
    for (Tuple2<?,?> tuple : output) {
      System.out.println(tuple._1() + ": " + tuple._2());
    }
    ctx.stop();
}
 public static void main(String[] args) throws Exception 
 {
	SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL").setMaster("local[4]");
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);
    JavaSQLContext sqlCtx = new JavaSQLContext(ctx);
    JavaRDD<Person> people = ctx.textFile("/home/zhaojin/people.txt").map(
      new Function<String, Person>() {
        public Person call(String line) throws Exception {
			String[] parts = line.split(",");
			Person person = new Person();
			person.setName(parts[0]);
			person.setAge(Integer.parseInt(parts[1].trim()));
			return person;
        }
      });
    JavaSchemaRDD schemaPeople = sqlCtx.applySchema(people, Person.class);
    schemaPeople.registerAsTable("people");
    JavaSchemaRDD teenagers = sqlCtx.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");
    List<String> teenagerNames = teenagers.map(new Function<Row, String>() {
      public String call(Row row) {
        return "Name: " + row.getString(0);
      }
    }).collect();

    schemaPeople.saveAsParquetFile("/home/zhaojin/people.parquet");//会生成目录,当前不能已存在
    JavaSchemaRDD parquetFile = sqlCtx.parquetFile("/home/zhaojin/people.parquet");
}

List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
JavaRDD<Integer> distData = sc.parallelize(data);

Tuple2<String,Object> x=  new Tuple2<String,Object>("myKey", new Object()) ;
String key=x._1();
Object value= x._2();

JavaRDD<String> lines = sc.textFile("data.txt");
JavaPairRDD<String, Integer> pairs = lines.mapToPair(s -> new Tuple2<String, Integer>(s, 1));//返回是两个类型的用 Tuple2
JavaPairRDD<String, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
counts.sortByKey();




bin/spark-submit --class "SimpleApp"   --master local[4] ~/simple-project-1.0.jar
bin/spark-shell --master local[4] --jars use_for_classpath.jar,code.jar
bin/pyspark --master local[4] --py-files code.py,code.zip


------------------------- Spark MLIb 机器学习
machine learning
Mahout  使用 MapReduce 


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
 
===========kafka	高吞吐量的分布式发布订阅消息系统
使用scala语言写的


要先启动zookeeper

启动服务
bin/kafka-server-start.sh config/server.properties   & 

建立topic 名为test
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

查看
bin/kafka-topics.sh --list --zookeeper localhost:2181		

发消息  , 没有提示后可输入消息,server.properties 中有配置 port=9092,也有zookeeper端口
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

收消息,可以收到启动前的消息
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning

再启两个服务
> cp config/server.properties config/server-1.properties 修改
    broker.id=1
    port=9093
    log.dir=/tmp/kafka-logs-1
 
> cp config/server.properties config/server-2.properties 修改
	broker.id=2
    port=9094
    log.dir=/tmp/kafka-logs-2
bin/kafka-server-start.sh config/server-1.properties &
bin/kafka-server-start.sh config/server-2.properties &	

已有3个服务,--replication-factor 3
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic

查看是3,第一行是总的,次级一个partition一行
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
	isr(in-sync) ,leader,replicas中的值是 broker.id

如当前leader是1,kill它后,再查leader变了,如再收消息,会收到这个新leader节点所有未收过的消息
ps -ef | grep server-1.properties

--发消息
props.put("metadata.broker.list", "localhost:9092");
props.put("serializer.class", "kafka.serializer.StringEncoder");
props.put("partitioner.class", "hadoop.kafka.SimplePartitioner");
props.put("request.required.acks", "1");
 
ProducerConfig conf=new ProducerConfig(props);
Producer<String, String> producer=new Producer<String, String> (conf);

Random rnd = new Random();
long runtime = new Date().getTime();
String ip = "192.168.2." + rnd.nextInt(255);
String msg = runtime + ",www.example.com," + ip;

KeyedMessage<String, String> data = new KeyedMessage<String, String>("my-replicated-topic", ip, msg);
producer.send(data);
producer.close();

----ConsumerConnector 收消息
Properties props = new Properties();
props.put("zookeeper.connect", "localhost:2181");
props.put("group.id", a_groupId);
props.put("zookeeper.session.timeout.ms", "400");
props.put("zookeeper.sync.time.ms", "200");
props.put("auto.commit.interval.ms", "1000");
String topic="my-replicated-topic";

ConsumerConnector consumer=Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
topicCountMap.put(topic, new Integer(a_numThreads));//格式topic, #streams 
Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
 
ExecutorService executor = Executors.newFixedThreadPool(a_numThreads);
 for (final KafkaStream stream : streams) {
	executor.submit(new ConsumerRunnable(stream));
 }
executor.shutdown();
consumer.shutdown();

class ConsumerRunnable implements Runnable  中 run 方法
{
	ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
	while (it.hasNext())
		System.out.println("---------" + new String(it.next().message()));
}

---- SimpleConsumer 收消息
int partition = 0;
String topic = "my-replicated-topic";
String clientName = "Client_" + topic + "_" + partition;

SimpleConsumer consumer = new SimpleConsumer("127.0.0.1", 9092, 100000, 64 * 1024, "leaderLookup");
TopicMetadataResponse resp = consumer.send(new TopicMetadataRequest(Collections.singletonList(topic)));
List<TopicMetadata> metaData = resp.topicsMetadata();
PartitionMetadata returnMetaData=null;
for (TopicMetadata item : metaData) {
	for (PartitionMetadata part : item.partitionsMetadata()) {
		if (part.partitionId() == partition) {
			returnMetaData = part;
		}
	}
}
if (returnMetaData==null || returnMetaData.leader() == null) {
	return;
}
String leadBroker = returnMetaData.leader().host();
consumer.close();

consumer = new SimpleConsumer(leadBroker, 9092, 100000, 64 * 1024,clientName);
Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
requestInfo.put( new TopicAndPartition(topic, partition), 
			new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1) );
OffsetRequest request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
OffsetResponse response = consumer.getOffsetsBefore(request);
long readOffset = response.offsets(topic, partition)[0];

kafka.api.FetchRequest req = new FetchRequestBuilder().clientId(clientName)
		.addFetch(topic, partition, readOffset, 100000).build();
FetchResponse fetchResponse = consumer.fetch(req);

for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partition) ) 
{
	messageAndOffset.nextOffset();
	ByteBuffer payload = messageAndOffset.message().payload();
	byte[] bytes = new byte[payload.limit()];
	payload.get(bytes);
	System.out.println(String.valueOf(messageAndOffset.offset()) + ": "+ new String(bytes, "UTF-8"));
}
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

===========Storm   1.0.2
Twitter的开源技术


使用 thrift 技术 生成代码,可以使用其它语言如 Python 

 distributed realtime computation  分布式实时计算
 
一个主节点运行一个Nimbus进程 ,分配代码 ,布置任务，故障检测
每个工作节点运行一个 supervisor进程，接受Nimbus任务，启动停止自己管理的Worker

与Haddop对比
Hadoop 			Storm
---------------------------
JobTracker		Nimbus		运行 命令 bin/storm nimbus &  是一个Thrift 服务
TaskTracker		Supervisor   运行命令 bin/storm supervisor &  , 在supervisor上 bin/storm ui & 可以仿问 http://<supervisor-IP>:8080  (显示 I am apk)
Child			Worker  (是一个进程)
Job				Topology (执行完毕后一直存在)
Mapper/Reducer	Spout/Bolt


Worker (执行Topology中的一个子集)中每一个Spout/Bolt的线程称为一个Task

Spout 产生数据流 Storm会不停的调用 nextTuple()函数( BaseRichSpout 有的)
Bolt 处理数据流 有execute(Tuple input)函数  (BaseRichBolt有的)
Tuple 是一个消息基本单元

杀 Nimbus , Supervisor 进程,Work进程依然运行

--- conf/storm.yaml 的配置 

storm.zookeeper.servers:
  - "zookeeper-host-1"  
  - "zookeeper-host-2"
  
storm.zookeeper.port: 2181 			#默认2181
#所有默认值在 https://github.com/apache/storm/blob/v1.0.2/conf/defaults.yaml ,即源码包中的conf/defaults.yaml
storm.zookeeper.root: "/storm"		#默认值 
nimbus.thrift.port: 6627
nimbus.task.timeout.secs: 30
nimbus.supervisor.timeout.secs: 60
nimbus.monitor.freq.secs: 10
ui.port: 8080


nimbus.host: "master-host"

storm.local.dir: "storm-local"  ### 每个节点那要有这个目录,可读写,默认是$STORM_HOME/storm-local
#yaml storm.local.dir: "C:\\storm-local"  #windows环境这样写

supervisor.slots.ports:		## 多少个worker运行在这个台机器上,第一个占用一个端口号,默认值 
    - 6700
    - 6701
    - 6702
    - 6703
	Config.TOPOLOGY_WORKERS  设置work 进程数量
	
nimbus.seeds: ["host1", "host2", "host3"]  ##在客户端中指定storm-cluster的机器 ,可写在文件 ~/.storm/storm.yaml 中
 
在nimbus 上执行  bin/storm  jar  examples/storm-starter/storm-starter-topologies-1.0.2.jar  org.apache.storm.starter.WordCountTopology
所有依赖都打在一个jar包中，不应该包含storm的class,如使用Maven可在  <dependencySet>中加入
<excludes>
	<exclude>org.apache.storm:storm-core</exclude>
</excludes>

也可使用<scope>provided</scope>

<dependency>
  <groupId>org.apache.storm</groupId>
  <artifactId>storm-core</artifactId>
  <version>1.0.2</version>
  <scope>provided</scope>
</dependency>

bin/storm  kill <submitTopology的名字>

LocalCluster cluster = new LocalCluster();
cluster.submitTopology("word-count", conf, builder.createTopology());


===========Tez   替代 MapReduce 也是基于YARN


===========Oozie [u:zi] 驯象人
	a workflow scheduler system to manage Apache Hadoop jobs
	 
===========Helix


===========MRUnit  MapReduce 的单元测试
===========cloudStack
apache CloudStack的目标是提供高度可用的、高度可扩展的能够进行大规模虚拟机部署和管理的开放云平台
用Java语言写的,支持MySQL 

 虚拟化技术 支持没有openStack的多
	
regions -> zone->Pod, secondary storage ->cluster->host, primary storage

===========openStack
cloudStack/openStack 实现  IaaS(Infrastructure as a Service ) 层 , 
			Hadoop     实现	 PaaS (Platform as a service)层  ,
							 SaaS(Software-as-a- Service)

cloudStack(上层) , openStack(底层,HP基于它,Solaris-11.2 使用它)
 	
中国银联采用 SUSE 部署 OpenStack ,目前已是全球第二大开源软件项目，仅次于Linux。 OpenStack能支持所有的 x86架构服务器
社区活越度大,用python语言写的,未来支持Python 3 ,支持PostgreSQL,MySQL,SQLite 

=============Greenplum  基于 PostgreSQL
=============Nutch  搜索引擎   全文搜索和Web爬虫
=============Avro