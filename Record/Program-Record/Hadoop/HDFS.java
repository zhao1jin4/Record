
=======================hadoop-3.2

hadoop-3.2.0\share\doc\hadoop\index.html  很大一个doc

hadoop-3.2.0\share\hadoop\common\hadoop-common-3.2.0.jar 				中有 core-default.xml
hadoop-3.2.0\share\hadoop\hdfs\hadoop-hdfs-3.2.0.jar 						中有 hdfs-default.xml
 

一个NameNode, 多个DataNode
HDFS不适合存储小文件的原因，每个文件都会产生元信息，当小文件多了之后元信息也就多了，对namenode会造成压力。
 
---Hadoop 2.x新功能 
集群间文件复制  bin/hadoop distcp hdfs://cluster1:9000/file hdfs://cluster2:9000/file 
小文件归档  bin/hadoop archive --archiveName xx.har  -p /inputDir /outDir  会启动一个mapreduce,要启动yarn
hadoop fs -ls  har:///outDir/xx.har 可以看到har里的内容

--安装
要求已经安装 rsync 同步配置用的
hostname 的值 不要有空格

etc/hadoop/hadoop-env.sh   配置JAVA_HOME 环境变量 ,最好不要把Java安装在有空格的目录中
	JAVA_HOME=/usr		redhatEL6.4安装openjdk-devel-7

hostname的值要可以ping通

===单机模式 方便debug
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
	<value>hdfs://localhost:9000</value>	 <!--localhost:9000不能远程仿问，要用主机名:9000,为NameNode的IP 初始手工加-->
</property>

<property>
	<name>hadoop.tmp.dir</name>
	<value>/data/hadoop3.2/standalone/tmp</value> <!-- 默认值 /tmp/hadoop-${user.name} -->
</property>

--- etc/hadoop/hdfs-site.xml
<property>
	<name>dfs.replication</name>
	<value>1</value> <!-- 有多少个DataNode 初始手工加 -->
</property>

dfs.datanode.max.transfer.threads

要配置成不用输入密码登录  ssh localhost
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys
  
测试 ssh localhost 不用密码

#可选的 export HADOOP_PREFIX=/opt/hadoop-3.2.0 
#可选的 vi etc/hadoop/hadoop-env.sh 设置JAVA_HOME=具体目录

----hadoop 支持 windwos 不方便
 不能format  要使用/方式分隔目录,  如以D:/ 开头 报   URI has an authority component 不行？？？ 
 路径必须是linux格式的以/开头才行,不能切换盘符
<name>hadoop.tmp.dir</name>
<value>/java_program/hadoop-3.2.0/tmp/</value>  
----	
	
$ bin/hdfs namenode -format  格式化文件系统
$ sbin/start-dfs.sh  启动 NameNode 和 DataNode   可能要密码  日志在$HADOOP_LOG_DIR 中(默认是 $HADOOP_HOME/logs),有secondaryNameNode的日志
对应的就有 sbin/stop-dfs.sh

http://localhost:9870  NameNode的接口  Utilities->Browser the file system
http://localhost:9870/conf		中有所有的配置信息 ,没有链接进入, 看50070配置来源是programatically
 
<property>
	<name>dfs.namenode.http-address</name>
	<value>0.0.0.0:9870</value> 
</property>

$ bin/hdfs dfs -mkdir /input 
$ bin/hdfs dfs -ls /  
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

----------secondaryNameNode 的作用 称为checkpoint
NameNode 把元数据保存到磁盘上的 
1.fsimage - 它是在NameNode启动时对整个文件系统的快照
2.edit logs - 它是在NameNode启动后，对文件系统的改动序列
只有在NameNode重启时，edit logs才会合并到fsimage文件中，从而得到一个文件系统的最新快照
但是在产品集群中NameNode是很少重启的，这也意味着当NameNode运行了很长时间后，edit logs文件会变得很大

NameNode的重启会花费很长时间
如果NameNode挂掉了，那我们就丢失了的改动应该是还在内存中但是没有写到edit logs的这部分


SecondaryNameNode 的职责是合并NameNode的edit logs到fsimage文件中。
首先，它定时到NameNode去获取edit logs，并更新到fsimage上。[笔者注：Secondary NameNode自己的fsimage]
一旦它有了新的fsimage文件，它将其拷贝回NameNode中。
NameNode在下次重启时会使用这个新的fsimage文件，从而减少重启的时间。

fs.checkpoint.period 控制周期，fs.checkpoint.size 日志文件超过多少大小时合并

----------HDFS NameNode HA 
使用 Quorum Journal Manager(qjournal) 就不用SecondaryNameNode了
自动切换HA 要使用zookeeper
https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HDFSHighAvailabilityWithQJM.html

第一次 namenode格式化后，创建fsimage和的edits文件
edits  记录增加，删除，等操作，像mysql的binlog
fsImage  完整镜像，保存了最新的元数据检查点

journalNode 负责存edits文件 和 zookeeper通讯，抢(zookeeper的临时节点)谁是active,但在变为active这前要杀上一个active的namenode使用fuse
但如果杀前一个，前一个挂了(除非过一会又恢复了)，连不上，就会一直重试，一直切换不成功 ，宁可服务不可用，也不能有错误数据(两个namenode 对一数据记录的元数据是不一样的，不知道哪个是对的)

---core-site.xml
<property>
	<name>fs.defaultFS</name>
	<value>hdfs://myCluster</value>	 <!--对应下面的-->
</property>
<property>
	<name>hadoop.tmp.dir</name>
	<value>/data/hadoop3.2/ha</value> <!-- 默认值 /tmp/hadoop-${user.name} -->
</property>
---hdfs-site.xml 

<property>
  <name>dfs.nameservices</name>
  <value>mycluster</value>
</property>
<property>
  <name>dfs.ha.namenodes.mycluster</name>
  <value>nn1,nn2, nn3</value> <!-- 这3个值对应后配置的别名,也可配置2个-->
</property>


<property>
  <name>dfs.namenode.rpc-address.mycluster.nn1</name>
  <value>machine1.example.com:8020</value>
</property>
<property>
  <name>dfs.namenode.rpc-address.mycluster.nn2</name>
  <value>machine2.example.com:8020</value>
</property>
<property>
  <name>dfs.namenode.rpc-address.mycluster.nn3</name>
  <value>machine3.example.com:8020</value>
</property>


<property>
  <name>dfs.namenode.http-address.mycluster.nn1</name>
  <value>machine1.example.com:9870</value>
</property>
<property>
  <name>dfs.namenode.http-address.mycluster.nn2</name>
  <value>machine2.example.com:9870</value>
</property>
<property>
  <name>dfs.namenode.http-address.mycluster.nn3</name>
  <value>machine3.example.com:9870</value>
</property>

<property>
  <name>dfs.namenode.shared.edits.dir</name>
  <value>qjournal://node1.example.com:8485;node2.example.com:8485;node3.example.com:8485/mycluster</value>
  <!-- 8485 就是  journal 监听的端口 -->
</property>

<property>
  <name>dfs.client.failover.proxy.provider.mycluster</name>
  <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
</property>


 <property>
   <name>dfs.ha.fencing.methods</name>
   <value>sshfence</value> 
   <!-- 多个namede同时只一个写元数据，fuser来杀另一个namenode，要配置不输密码连接ssh另一个namenode 必须配置dfs.ha.fencing.ssh.private-key-files 
 		也可配置脚本 <value>shell(/path/to/my/script.sh arg1 arg2 ...)</value>   
   -->
 </property>
 <property>
   <name>dfs.ha.fencing.ssh.private-key-files</name>
   <value>/home/exampleuser/.ssh/id_rsa</value>
</property>

<property>
  <name>dfs.journalnode.edits.dir</name>
  <value>/path/to/journal/node/local/data</value>
</property>>
 
<property>
	<name>dfs.permissions.enabled</name>
	<value>false</value>  <!-- -->
</property>

配置文件放在各个节点上，目录正确 

sbin/hadoop-daemons.sh start journalnode  #是带s的命令 如日志没有显示多台,只能每台单独做
	提示命令过时用 "hdfs --workers --daemon start"  (即 bin/hdfs --workers --daemon start journalnode )
 jps后有 JournalNode 进程(很轻，生产上直接和namenode放一台机器上就可，至少3个JournalNode) , 监听8485 , 每台都要做（都要连接所有namenode不输密码） , 启动后才能格式化


格式化只在一个节点做一次
bin/hdfs namenode -format 

启动namenode
sbin/hadoop-daemon.sh start namenode  
	提示令过时用 "hdfs --daemon start"  (即 bin/hdfs  --daemon start  namenode )
	jps 有NameNode

在另一个namenode 上同步数据
	bin/hdfs namenode -bootstrapStandby 但提示 	Configuration has multiple addresses that match local node's address. Please configure the system with dfs.nameservice.id and dfs.ha.namenode.id ,'
	hdfs-site.xml中加  
        <property>
                <name>dfs.ha.namenode.id</name>
                <value>nn1</value>
        </property> 
        提示要连接本机IP的8020端口(NameNode)连接不上 ?????
	另一个namenode上启动
	sbin/hadoop-daemon.sh start namenode
	
启动全部datanode节点
sbin/hadoop-daemons.sh start datanode

http://localhost:9870  看页面哪个是active

因没有zookeeper只能手工激活一个HA节点 
bin/hdfs haadmin -transitionToActive nn1

配置自动切换HA 
先停 sbin/stop-dfs.sh

hdfs-site.xml  增加
 <property>
   <name>dfs.ha.automatic-failover.enabled</name>
   <value>true</value>
 </property>

core-site.xml   增加zookeeper
 <property>
   <name>ha.zookeeper.quorum</name>
   <value>zk1.example.com:2181,zk2.example.com:2181,zk3.example.com:2181</value>
 </property>

配置传所有节点上

删所有节点的 数据和log，重头来
zookeeper已经启动
sbin/hadoop-daemons.sh start journalnode
bin/hdfs namenode -format 
bin/hdfs zkfc --formatZK  #FC=Failvoer Controller
日志提示建立了 /hadoop-ha/myCluster父节点
 (谁先占用zookeeper上的临时节点，谁就是active)
sbin/start-dfs.sh



JournalNode 里面只有edits文件，HA的 NameNode有fsimage文件 ，非HA上有edits文件 



----hdfs-site.xml
dfs.journalnode.edits.dir =  	/tmp/hadoop/dfs/journalnode/
dfs.provided.aliasmap.inmemory.leveldb.dir = /tmp

---------
NameNode的存文件数 inode ???
NameNode的block size是128 MB
data node上的块大小默认是64MB






(是etc/hadoop/workers 文件指定datanode节点主机名 )
========== 全集群 cluster 模式
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



/etc/profile 配置  export HADOOP_PREFIX=/path/to/hadoop
   
每个节点都使用相同的配置 rsync,sftp 工具

---etc/hadoop/hadoop-env.sh 
HADOOP_LOG_DIR
HADOOP_PID_DIR 
配置中有各种以_OPTS的变量可对JVM参数做修改

export HADOOP_HEAPSIZE_MIN=
export HADOOP_HEAPSIZE_MAX=   MB为单位  对 NameNode,SecondaryNameNode,DataNode 同时生效


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

	
etc/hadoop/slaves 文件中列出有的slave的主机名(每一行一个),只用于Helper scripts (ssh通信) , 不会用于Java-based Hadoop configuration

 HADOOP_CONF_DIR目录的文件同步到所有的机器上,所有机器的目录都一样,hdfs和yarn应该是独立的系统用户
 
[hdfs] $HADOOP_PREFIX/bin/hdfs namenode -format <cluster_name>
[hdfs] $HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode
[hdfs] $HADOOP_PREFIX/sbin/hadoop-daemons.sh --config $HADOOP_CONF_DIR --script hdfs start datanode  	
	也可用start-dfs.sh 读etc/hadoop/slaves文件
	

停止顺序同启动顺序

etc/hadoop/log4j.properties

------Rack Awareness



NameNode 的metadata信息在启动后加载到内存,对应磁盘文件是fsimage
/tmp/hadoop-zh/dfs/name/current/ 有fsimage_* 开头的文件 和 edits_* 开对的文件
/tmp/hadoop-zh/dfs/data/current/BP-[nn]-[hostname]-[nn]/current/rbw/ 下有blk_* 开头的文件(存放数据的),及.meta结尾的文件


bin/hadoop dfsadmin -safemode get (过时方式)
bin/hdfs dfsadmin -safemode get 查NameNode是否在安全模式下
bin/hdfs dfsadmin -safemode enter  进入安全模式
bin/hdfs dfsadmin -safemode leave   离开安全模式
bin/hdfs dfsadmin -report

---
hdfs fsck /
hdfs fsck -delete 
========== 

Hadoop  动态增加/删除DataNode节点

bin/hadoop fs -getmerge /NTY result  会把/NTY目录下的part-0000,part-0001....全部文件合并生成一个result文件 


-------------------------------HDFS Java API
Configuration conf=new Configuration();
FileSystem fs=  FileSystem.get(conf);
Path path=new Path("/java") 
fs.delete(path,true);//recursive
fs.mkdirs();
fs.copyFromLocalFile(new Path("~/.ssh/known_hosts"), path);
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

public static void printNodeInfo() throws IOException 
{
	Configuration conf=new Configuration();
	FileSystem fs=  FileSystem.get(conf);
	DistributedFileSystem dfs =(DistributedFileSystem)fs;//要使用hadoop jar 运行才可
	DatanodeInfo[] infos=dfs.getDataNodeStats();//所有群集中的节点
	for(DatanodeInfo info:infos)
	{
		System.out.println("----host:"+info.getHostName());
	}
}



