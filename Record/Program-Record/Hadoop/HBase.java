
---------------------------------hadoop 子项目 HBase  要使用基于hadoop2版本的
Log-Structured Merge-Trees (LSM)

要linux环境运行
构建在 Apache Hadoop和 Apache ZooKeeper之上

phphbaseadmin 


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

hbase-common-1.0.1.jar	 中有 hbase-default.xm

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
