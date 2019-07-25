
---------------------------------hadoop 子项目 HBase  要使用基于hadoop2版本的
Log-Structured Merge-Trees (LSM)

要linux环境运行
构建在 Apache Hadoop和 Apache ZooKeeper之上

联合使用 Hive (像SQL)，但必须用MapReduce，行级事物支持 ，百万查询/每秒 （比数据库的数千查询/每秒 要快很多）

phphbaseadmin github上已经有6年没有更新，有中文
SQuirrel SQL Client (Swing)可以添加Hive的驱动，可以添加 apache Phoenix(凤凰) 驱动(像使用jdbc访问关系型数据库一些，访问NoSql数据库HBase)

----squirrelsql-3.9.1-standard.zip 这个版本不支持JDK12
./squirrel-sql.sh来启动

  下载 apache-phoenix-5.0.0-HBase-2.0-bin.zip (注意要和指定hbase版本一起用)
   
   把  phoenix-5.0.0-HBase-2.0-client.jar 放在squirrelsql/lib下
   
	点左则的Driver视图，Drvier 菜单-> Add Driver -> Name取名为 Phoenix, 在Example URL中输入 jdbc:phoenix:localhost ( 也可是jdbc:phoenix:localhost:2181:/hbase  )
		 格式为 jdbc:phoenix [ :<zookeeper quorum> [ :<port number> [ :<root node> [ :<principal> [ :<keytab file> ] ] ] ] ] 
		 即hbase-site.xml 中的配置 hbase.zookeeper.quorum, hbase.zookeeper.property.clientPort, and zookeeper.znode.parent  
		 Class Name的值写入 org.apache.phoenix.jdbc.PhoenixDriver
	点左则的Aliase视图,Aliases 菜单 ->new Aliases -> Name取个名字myHbase，Driver中选择刚建立的 Phoenix,User Name: anything, Password: anything

squirrelsql和hbase都用JDK8测试成功， 如hbase用jdk8,squirrelsql(使用JAVA_HOME环境变量)用jdk11是不行的
--squirrelsql 的hbase配置的 hbase-site.xml
<property>
		<name>hbase.rootdir</name>
		<value>file:///opt/hbase-2.0.5-bin/hbase-2.0.5/</value>
	</property>
 <!-- 有hbase.zookeeper.quorum 和 hbase.cluster.distributed为true ,则hbase.zookeeper.property.dataDir不需要了
	<property>
		<name>hbase.zookeeper.property.dataDir</name>
		<value>/tmp/zookeeper</value>
	</property>
	-->
 <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value> 
 </property>
  
<property>
    <name>hbase.zookeeper.quorum</name>
    <value>localhost</value>
    <description> </description>
</property> 


----- phoenix
下载 apache-phoenix-5.0.0-HBase-2.0-bin.zip (注意要和指定hbase版本一起用)
把包中的lib/phoenix-5.0.0-HBase-2.0-server.jar 放在所有  HBase region server 和 master 的lib下
把phoenix-5.0.0-HBase-2.0-client.jar  放在 Phoenix client中像SQuirrel SQL Client

 
bin 目录中看到文件 hbase-site.xml  

bin/sqlline.py localhost (用对应的HBase-2.0.x版本,第一次要慢一点)
或 bin/sqlline.py localhost:2181:/hbase

>!table 提示连接
>help 查看所有命令
>!quit
>!dbinfo


看示例有 INTEGER,BIGINT,DATE类型

如已经在hbase中建立了表student
phoenix>  create view "student" (username VARCHAR PRIMARY KEY, "info"."fullname" VARCHAR, "info"."age" INTEGER,"score"."english" INTEGER,"score"."math" INTEGER);
phoenix>  drop view "student";  
phoenix>  create view "student" (username VARCHAR PRIMARY KEY, "info"."fullname" VARCHAR, "info"."age" BIGINT,"score"."english" BIGINT,"score"."math" BIGINT);

 SQuirrel SQL 对数值显示不对

phoenix> create table tbl(id integer primary key ,name varchar);

phoenix> upsert into tbl(id,name) values(11,'lisi');
phoenix> upsert into tbl(id,name) values(2222,'王五');
----	
	
conf/hbase-site.xml 	 增加
	<property>
		<name>hbase.rootdir</name>
		<value>file://~/hbase-0.98.3-hadoop2/</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.dataDir</name>
		<value>/tmp/zookeeper</value>
	</property>
  
	zookeeper.znode.parent  默认是　/hbase
	hbase.master.info.port	以前默认是 60010, 在2.2 版本默认是 16010
	hbase.master.port  默认是 16000
	hbase.regionserver.port 默认值为  16020
	hbase.zookeeper.property.clientPort 默认是 2181
./conf/hbase-env.sh 中修改　export JAVA_HOME=/usr
./bin/start-hbase.sh 启动  

http://<hbase-server-ip>:16010  自动进入/master-status  ,HBase Configuration 页有显示所有配置(包括默认值)
http://localhost:16010/conf


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
 

--多列族 ，每个列族单独存放在一个区间,当有时一个列族没有数据时就不会被存储
只能一列一列的添加，不能同时添加多列 ，每put一次物理存诸是一行

 
尽量让你的列族数量少一些
如果列族A有100万行，列族B有10亿行，列族A可能被分散到很多很多区(及区服务器)。这导致扫描列族A低效
行键要尽量避免时间戳或者(e.g. 1, 2, 3)这样的key, 如日志可 OpenTSDB,的schema做法,升序排 [key][Long.MAX_VALUE - timestamp]
尽量使列族名小，最好一个字符,最好还是用短属性名

> create 'student', 'info','score'
>put 'student', 'lisi', 'info:fullname', '张三'
>put 'student', 'lisi', 'info:age', 25
>put 'student', 'lisi', 'score:english', 80
>put 'student', 'lisi', 'score:math', 70
>scan 'student'
 get 'student', 'lisi' ,'info'  
 
 



./bin/stop-hbase.sh 停止

linux xmllint 工具用来检查XML格式是否正确
linux rsync 工具用来同步文件

/etc/security/limits.conf 
hadoop  -       nofile  32768			 第一列是操作系统的用户名
hadoop soft/hard nproc 32000

vi /etc/systemd/system.conf   (如修改普通用户)
DefaultLimitNOFILE=32768
DefaultLimitNPROC=32000
	  
分单机模式 , 所有的服务和zooKeeper都运作在一个JVM中, hbase.rootdir 使用本地文件系统 file:///  
 
分布式模式 ,hbase.rootdir 使用 hdfs:// 
分布式Hadoop版本jar文件替换HBase lib目录下的Hadoop jar文件
hbase-env.sh 中修改 HBASE_CLASSPATH=~/hadoop-2.4.0/etc/hadoop/  使用HDFS
ln -s ~/hadoop-2.4.0/etc/hadoop/hdfs-site.xml  ~/hbase-0.98.3-hadoop2/conf/

hbase-common-2.2.0.jar	 中有 hbase-default.xm

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


hbase org.apache.hadoop.hbase.io.hfile.HFile -v -f hdfs://<ip>/xfile 

-- 权限  （不能建立用户 远程连接？？？）
grant 'rest_server', 'RWCA' 
Read (R)  Write (W)  Execute (X)  Create (C)  Admin (A)  
--事务

--分布式2.4. Advanced - Fully Distributed
 HBase 的集群需要运行在 HDFS 之上
 主要由 Master 和 Region Server 组成，以及 Zookeeper 
  
--
HBase Master 用于协调多个 Region Server，侦测各个 Region Server 之间的状态，并平衡 Region Server 之间的负载
				 还有一个职责就是负责分配 Region 给 Region Server。HBase 允许多个 Master 节点共存，但是这需要 Zookeeper 的帮助。
				不过当多个 Master 节点共存时，只有一个 Master 是提供服务的，其他的 Master 节点处于待命的状态。

对于一个 Region Server 而言，其包括了多个 Region ， 每个 Region 中会关联多个存储的单元（Store）
Zookeeper 是作为 HBase Master 的 HA 解决方案 ， 负责 Region 和 Region Server 的注册

当一个 Client 需要访问 HBase 集群时，Client 需要先和 Zookeeper 来通信，然后才会找到对应的 Region Server。
	每一个 Region 都只存储一个 Column Family 的数据，（按 Row 的区间分成多个 Region）
	Region 所能存储的数据大小是有上限的，当达到该上限时（Threshold），Region 会进行分裂，数据也会分裂到多个 Region 中

每个 Store 包含一个 MemStore，和一个或多个 HFile
MemStore 便是数据在内存中的实体，并且一般都是有序的。当数据向 Region 写入的时候，会先写入 MemStore。
当 MemStore 中的数据需要向底层文件系统 Dump 时（例如 MemStore 中的数据体积到达 MemStore 配置的最大值），Store 便会创建 StoreFile
而 StoreFile 就是对 HFile 一层封装， HFile 都存储在 HDFS 之中

 HLog 机制是 WAL 的一种实现，而 WAL（一般翻译为预写日志）是事务机制中常见的一致性的实现方式

每个 Region Server 中都会有一个 HLog 的实例，（如 Put，Delete）先记录到 WAL（也就是 HLog）中，然后将其写入到 Store 的 MemStore (都是先写日志)

如果有上亿或上千亿行数据，HBase 才会是一个很好的备选



直接在 Ambari 的 WEB 中，重启整个 HBase

  
//---
String tableName = "myTable";
Configuration config = HBaseConfiguration.create();
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.rootdir", "file:///opt/hbase-2.2.0");  
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.dataDir", "/opt/hbase-2.2.0/data"); 
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
