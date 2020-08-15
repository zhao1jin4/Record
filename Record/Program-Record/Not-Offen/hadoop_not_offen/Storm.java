被Flink替代
===========Storm   1.0.2
Twitter的开源技术，使用 Clojure 语言

alibaba的jStorm使用java语言改写

使用 thrift 技术 生成代码,可以使用其它语言如 Python 

 distributed realtime computation  分布式实时计算
 
一个主节点运行一个Nimbus进程 ,分配代码 ,布置任务，故障检测
每个工作节点运行一个 supervisor进程，接受Nimbus任务，启动停止自己管理的Worker

与Hadoop对比
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
