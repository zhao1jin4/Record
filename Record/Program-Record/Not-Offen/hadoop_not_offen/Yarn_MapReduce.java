
Yarn不常用,mapReduce被spark替代 ,只HDFS就可以了
hadoop-2.x.0\share\hadoop\mapreduce\hadoop-mapreduce-client-core-2.x.0.jar 	中有 mapred-default.xml
hadoop-2.x.0\share\hadoop\yarn\hadoop-yarn-common-2.x.0.jar					中有 yarn-default.xml
hadoop-2.x.0\share\doc\hadoop  是和使用版本对应的 doc

包名mapred代表的是hadoop旧API , 而mapreduce代表的是hadoop新的API

======== 假分布 的YARN
MapReduce 基于 YARN 的

vi etc/hadoop/mapred-site.xml 增加
	<property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
vi etc/hadoop/yarn-site.xml
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
	</property>

	
$ sbin/start-yarn.sh	 启动 ResourceManager 和 NodeManager  ,使用jps可看到(hadoop-3.2 要用JDK8，11不行的)
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



-------------etc/hadoop/mapred-env.sh 和  etc/hadoop/yarn-env.sh 中的配置

ResourceManager 				YARN_RESOURCEMANAGER_OPTS
NodeManager 					YARN_NODEMANAGER_OPTS
WebAppProxy 					YARN_PROXYSERVER_OPTS
Map Reduce Job History Server 	HADOOP_JOB_HISTORYSERVER_OPTS 



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

---etc/hadoop/yarn-env.sh
YARN_LOG_DIR
YARN_HEAPSIZE
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
	



[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager
[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemons.sh --config $HADOOP_CONF_DIR start nodemanager
[yarn] $HADOOP_YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start proxyserver  
	也可以使用 $HADOOP_PREFIX/sbin/start-yarn.sh 读etc/hadoop/slaves文件
	
[mapred] $HADOOP_PREFIX/sbin/mr-jobhistory-daemon.sh --config $HADOOP_CONF_DIR start historyserver
	MapReduce JobHistory Server 	http://localhost:19888/



--- etc\hadoop\capacity-scheduler.xml
	学习的
	<property>
		<name>yarn.scheduler.capacity.root.default.capacity</name>  <!-- default 是名字 对应其它地方的配置-->
		<value>100</value>
		<description>Default queue target capacity.</description>
	</property>
	
	
	
=======MapReduce Java API



// bin/hdfs dfs -rm -r -f /user/zh/myout
//eclipse 带参数 hdfs://127.0.0.1:9000/user/zh/myin hdfs://127.0.0.1:9000/user/zh/myout
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

bin/start-balancer.sh 增加了新的datanode,运行这个脚本,可使每个节点数据多少相似

   
   
   
   
   






