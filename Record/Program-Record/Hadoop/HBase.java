
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

--------Hbase
	
conf/hbase-site.xml 	 增加
	<property>
		<name>hbase.rootdir</name>
		<value>file:///opt/hbase-2.2.0/hdata</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.dataDir</name>
		<value>/opt/hbase-2.2.0/zkData</value>
	</property>

./conf/hbase-env.sh 中修改　export JAVA_HOME=/usr  
	# export HBASE_HEAPSIZE=1G 可修改为200m ,jvm内存的Xmx


./bin/start-hbase.sh 启动  ,会使用自带的zookeeper
./bin/stop-hbase.sh 停止

http://<hbase-server-ip>:16010  自动进入/master-status  ,HBase Configuration 页有显示所有配置(包括默认值)即地址/conf
http://localhost:16010

可以看到hadoop 版本，自带hadoop的包 (可替换jar包升级版本)
页面列标题 RIT 指 Region-In-Transition 


./bin/hbase shell
表名，行和列需要加引号
>help 有分多命令，按组分类,有些应该可以用过界面查看，或操作
>help "scan" 具体命令的帮助

> create 'test', 'cf'		--名为 test 的表，这个表只有一个 列族 为 cf
> list  				--所有的表
>put 'test', 'row1', 'cf:a', 'value1'		--key为row1, 列为 cf:a， 值是 value1
>put 'test', 'row1', 'cf:a', 'value11'	 -- 第二次是修改
>put 'test', 'row1', 'cf:a', 'valueNotShow',1575539786613  指定时间，如小于最新的，等于没加
>put 'test', 'row1', 'cf:b', 'bbbb'	  
>scan 'test'		--查所有，看结果是存储结果，row1,cf是重复存的 
>scan 'test' , {STARTROW => 'row1'}
>scan 'test' , {STARTROW => 'row1',STOPROW => 'row2'}
>scan 'test' , {RAW => true ,VERSIONS => 10 }  可以看到10个版本以内的删除(type=Delete)或修改覆盖的数据
>get 'test', 'row1' --指定rowkey
>get 'test', 'row1' ,'cf'  
>get 'test', 'row1' ,'cf:a'
>get 'test', 'row1' , {COLUMN => 'cf:a'} 
>status
>version
>describe 'test'
>delete  'test','row1','cf:a'		#删列数据,增加一条记录(type=Delete)时间和最近的一条记录的时间相同，如有老版本数据还是会显示的，和mysql不一样
>deleteall 'test' ,'row1'					#删行数据 每个列簇上显示 type=DeleteFamily  
>alter 'test',{NAME=>'cf',METHOD=>'delete'}  #删列簇
>exists 'test'   
>is_enabled 'test'
>is_disabled 'test'
>count 'test'   			#查 rowkey的行数
>truncat 'test'				#删所有数据,日志显示disable,drop,create
> disable 'test'
> drop 'test'  必须先disable
> exit
>t = create 'table1', 'f'
>t = get_table 'table1'
>t.put 'r', 'f', 'v'
>t.scan
>t.describe
>t.disable   #对应的有enable
>t.drop

>tables = list('t.*')			 所有表名以t开头的
>tables.map { |t| disable t ; drop  t}  全删它们|t|是迭代的中间变量


> import java.util.Date
> Date.new(1218920189000).toString()
> debug 		
 ./bin/hbase shell -d  打开debug更多的信息
 
namespace 像mysql 的database,默认有个default,hbase(内部使用)
> create_namespace 'my_ns'
> create 'my_ns:my_table', 'fam'  #在名称空间中建立表,后面是列簇
> list_namespace
> list_namespace_tables  'my_ns' #显示有my_table
> describe_namespace 'my_ns'
> describe_namespace 'my_ns'
> drop_namespace 'my_ns'  #只能删空的名称空间


describe 'test'
alter 'test' , {NAME => 'cf' , VERSIONS=>'3'}

put命令可加自定义时间，如字串，如删除时是未来的来时，再插件正常时间的数据就没用了，因只取最新的时间版本的数据








rowkey按字典排序,不同版本以timestamp区分，HBase 没有数据类型，只是byte[]

Store 指一个列簇的按rowkey分的一个分区，存在HDFS上
Region 就是全部列簇按rowkey分的一个分区

多列簇 ，每个列族单独存放在一个区间/目录 ,当有时一个列族没有数据时就不会被存储
只能一列一列的添加，不能同时添加多列 ，每put一次物理存诸是一行
建表时要事先指定列簇，列是动态增加

尽量让你的列族数量少一些 ,如果列族A有100万行，列族B有10亿行，列族A可能被分散到很多很多区(及区服务器)。这导致扫描列族A低效
行键要尽量避免时间戳或者(e.g. 1, 2, 3)这样的key, 
尽量使列族名小，最好一个字符,最好还是用短属性名

> create 'student', 'info','score'
>put 'student', 'lisi', 'info:fullname', '张三'
>put 'student', 'lisi', 'info:age', 25
>put 'student', 'lisi', 'score:english', 80
>put 'student', 'lisi', 'score:math', 70
>scan 'student'
 get 'student', 'lisi' ,'info'  
 
 观察 HDFS上的数据/hbase/data/<namespace>/<table>/<regionUUID>/<column_family>/XX__UUID  ,什么UUID对应管理界面中查

-- 权限  （不能建立用户 远程连接？？？）
grant 'rest_server', 'RWCA' 
Read (R)  Write (W)  Execute (X)  Create (C)  Admin (A)  
--事务
 HBase的事务是行级事务
 
 
--


linux xmllint 工具用来检查XML格式是否正确
linux rsync 工具用来同步文件

/etc/security/limits.conf 
hadoop  -      nofile  32768		 第一列是操作系统的用户名
hadoop hard 	nproc 32000        第二列 - 表示全部，可soft或hard

vi /etc/systemd/system.conf   (如修改普通用户)
DefaultLimitNOFILE=32768
DefaultLimitNPROC=32000
	  
分单机模式 , 所有的服务和zooKeeper都运作在一个JVM中, hbase.rootdir 使用本地文件系统 file:///  
----真分布式
分布式模式 ,hbase.rootdir 使用 hdfs:// 

为了让HBase 知道HDFS的配置 (HDFS Client)
hbase-env.sh 中修改 HBASE_CLASSPATH=/opt/hadoop-3.2.0/etc/hadoop/  即 HADOOP_CONF_DIR 的值
ln -s /opt/hadoop-3.2.0/etc/hadoop/hdfs-site.xml  /opt/hbase-2.2.0/conf/ 也可使用复制方式
小的HDFS变动，可修改hbase-site.xml

hbase-common-2.2.0.jar	 中有 hbase-default.xm
--hbase-site.xml
<property>
    <name>hbase.rootdir</name>
    <value>hdfs://namenode.example.org:9000/hbase</value>
</property>

<!-- 当HB=ASE_MANAGES_ZK=false时 这个可不用
<property>
	<name>hbase.zookeeper.property.dataDir</name>
	<value>/tmp/zookeeper</value> 
</property> 
-->

<property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
    <description>若为false,HBase和Zookeeper会运行在同一个JVM里面</description>
 </property>
  
<property>
    <name>hbase.zookeeper.quorum</name>
    <value>node-a.example.com,node-b.example.com,node-c.example.com</value>
</property> 
<property>
    <name>hbase.zookeeper.property.clientPort</name>
    <value>2181</value>
</property> 
	 
zookeeper.znode.parent  默认是　/hbase
hbase.master.port  默认是 16000
hbase.master.info.port	默认是 16010
hbase.regionserver.port 默认值为  16020

http://<regionserver-ip>:16030

hbase.zookeeper.quorum  假的是 localhost

#如使用hbase配套的zookeeper,以hbase.zookeeper.property开头的配置后面的值就是zoo.cfg是的配置
hbase.zookeeper.property.dataDir
hbase.zookeeper.property.clientPort 默认是 2181
hadoop.registry.zk.quorum  默认是 localhost:2181

zookeeper.session.timeout			默认值是10000
hbase.regionserver.handler.count	默认是30, 处理用户请求的线程数量
hbase.client.write.buffer    默认是  2097152即2MB 是客户端的缓存
hbase.client.scanner.caching 默认是  2147483647 即2GB



conf/hbase-env.sh 文件中有 #export HB=ASE_MANAGES_ZK=true ,默认值是true，即使用hbase配套的zookeeper，所有节点修改为false
	
修改conf/regionservers (是很像 hadoop-2.4.0/etc/hadoop/slaves) 一行写一个主机名 ， 例如写入node-b.example.com 和 node-c.example.com
配置ssh， Master连接每一个RegionServer不用输入密码,例如nod-a 连接node-b和node-c不用输密码
	regionServer要修改hbase-env.sh中的JAVA_HOME(不会执行/etc/profile中的export )
	ssh-keygen -t rsa  
每台机器的时间要一致(差要小于30000ms,注意每个机器的时区)

在conf目录 建立文件名backup-masters 例如写入node-b.example.com，即Master节点可有一个备用的

node-a,node-b ,node-c 的conf目录相同(应只是hbase-site.xml,如backup-masters,regionserver，hdfs-site.xml从是可不要吗？？)
在node-a(master)节点启动 bin/start-hbase.sh

bin/hbase-daemon.sh start master
bin/hbase-daemon.sh start regionserver （是在本机启动regionserver，不是在master节点执行，是在从节点执行）

 主节点jps显示有
 HMaster
 
 从节点jps显示有
 HRegionServer
 
---问题
 http://<主节点IP>:16010/   如显示的regionserver不对 ？？？？  zookeeper，hdfs清数据也是一样的？？？
 http://<region节点IP>:16030/ 可仿问 UI显示还是master主机名？？？
 主日志显示一直连接主节点的16020端口（regionserver)，只能在master上启动regionserver,才可用
 是hbase.zookeeper.quorum只配置为主节点吗？zookeeper 独立使用不行？？ zk数据目前没指不行？？？
 conf/regionservers 启动是有用的，但连接没用？？？？，如修改了没用，还是使用老的，可能只第一次用，后使用zk上的数据
 

 
 
---------- 
bin/hbase master stop
bin/hbase master start  前台启动

Hlog 记录操作日志 ， 没有写入HDFS前在内存中（MemStore），写内存前先写日志 ，从内存到文件叫flush
每一次flush产生一个文件 ，如内存小，产生很多小文件要合并，大文件要拆
Region里Store里StoreFile（存在HDFS) 里是HFile
客户端如读写数据->zookeeper->regionserver(如master挂了也不影响读写数据)

HBase写数据流程 
在老版本中还有-ROOT-表，防止meta表做了分区，新版本meta表不做切分,没了-ROOT-
1.请求zookeeper查meta表在哪个regionserver 如node-01
2.到再node-01上找要写的表test所在的regionserver如node-02 (对元数据做缓存)
3.再向node-02上写目录（wal)再写memstore(源码逻辑也是，pom.xml中加hbase-server 在HRegion类的doMiniBatchMutate方法中)


zkCli.sh中
> get /hbase/meta-region-server  (也可在管理界面中看System Table标签中hbase:meta链接 显示所在regionserver)
hbase shell中
>scan 'hbase:meta' 打 info:server 列的值(rowkey为表名的) ， (也可在管理界面中看) ,可能为多条记录，数据大点会在分区(region),也就是说一个表的多个region可有口跨regionserver

---hbase-site.xml 参数说明见 hbase-common-2.2.0.jar/hbase-default.xml

hbase.hregion.memstore.flush.size  默认 134217728=128MB
hbase.hstore.flusher.count  默认 2
hbase.regionserver.optionalcacheflushinterval   默认 3600000 即1小时，如为0自动刷新
--

查看Hfile类型文件内容的方法 
bin/hbase org.apache.hadoop.hbase.io.hfile.HFile -v -f hdfs://<ip>/xfile 
-p --printkv

bin/hbase org.apache.hadoop.hbase.io.hfile.HFile -v -p -f  /hbase/data/<namespace>/<table>/<regionUUID>/<column_family>/XX__UUID  (可在界面中复制)
 
>flush 'test'  产生新的hfile

HBase读数据流程 (HBase的读比写慢,因每次都要读磁盘，因可能block cache中的数据不是最新时间版本)
1和2步同写流程
3.再向node-02上读  memstore和storeFile 放在一起做合并返回  （storeFile中的内容放在block cache 中）
	 下次再读如block cache中这个文件有就直接用，和memestore合并返回，但block cache不可能存下全部数据(全部hfile,每次flush产生新的)，所以每次读都要查硬盘的hfile

合并(compact) 分为 minor compact （几个文件合并为一个文件 ）和 major compact（所有文件合并为一个文件）
有compact,major_compact命令

---hbase-site.xml
hbase.hregion.majorcompaction 默认 604800000  即7天后合并， 可能到时硬盘操作太忙 如设为0表示关闭
hbase.hregion.majorcompaction.jitter   0.50   jitter 抖动
hbase.hstore.compactionThreshold    3 如Store里有(多于,源码是>=)3个StoreFile(flush产生的),做合并
---
如有3个版本数据compat和major_compact是相同的

>compact 'test' 生成新文件时，不立即删除原文件（minor compact） 

>flush 时
	如有两次相同的put的修改，会删除老版本的数据（只对存在内存中的数据），如存在文件中的老版本数据会在major compact时删除 
	type=Delete的不会被去除

>major_compact 'test'  有type=Delete会删除
---hbase-site.xml
hbase.hregion.max.filesize  10737418240 即10G  当列簇的文件到了X（X最大到10G）时做分割，按rowkey
	x=region个数^2 * hbase.hregion.memstore.flush.size即128M	,会出现很多请求在一部分上，推荐使用预分区

如一个表多个列簇，建议每个列簇数据量相近，如一个列簇数据量很大，一个列簇数据量很小，在分割文件时，小列簇就会分成很多小文件（这样不好）


--------
分布式的消费的一致性

两军问题
拜占庭将军问题

-------- 预分区
create 't1', 'f1', SPLITS => ['10', '20', '30', '40']

4个数组，分5个区（点表名链接看界面），开始为负无穷，结尾为正无穷
进哪个区比较按 前几位（字典）比较，rowkey的长度就大
create 't2', 'f2', {NUMREGIONS => 15, SPLITALGO => 'HexStringSplit'}     看界面 start key,end key
create 't3', 'f3', SPLITS_FILE => 'splits.txt'  当前目录下的splits.txt文件中多行定义 
--splits.txt  顺序不能写错
aa
bb
cc 
dd
--

未来规化要保证每个分区大小 <10G ，防止自动分区  

----- 批量导入数据
使用mapreduce,要启动yarn , 配置xml,再sbin/start-yarn.sh

--vi inputfile 上传为 hdfs://localhost/inputfile
row1	c1	c2
row2	c1	c2
row3	c1	c2
row4	c1	c2
row5	c1	c2
row6	c1	c2
row7	c1	c2
row8	c1	c2
row9	c1	c2
row10	c1	c2
--
bin/hdfs dfs -put inputfile  /inputfile 


表名叫 datatsv，一个列簇d,下有两列c1,c2 
> create 'datatsv','d'

export HADOOP_HOME=/opt/hadoop-3.2.0
export HBASE_HOME=/opt/hbase-2.2.0
export HADOOP_CLASSPATH=${HADOOP_CLASSPATH}:${HBASE_HOME}/lib/hbase-server-2.2.0.jar:${HBASE_HOME}/lib/hbase-zookeeper-2.2.0.jar
${HADOOP_HOME}/bin/hadoop jar ${HBASE_HOME}/lib/hbase-mapreduce-2.2.0.jar importtsv -Dimporttsv.columns=HBASE_ROW_KEY,d:c1,d:c2 -Dimporttsv.bulk.output=hdfs://localhost/storefileoutput datatsv hdfs://localhost/inputfile  
还报找不到org.apache.hadoop.hbase.util.MapReduceExtendedCell??

或用 
bin/hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=HBASE_ROW_KEY,d:c1,d:c2 -Dimporttsv.bulk.output=hdfs://localhost:9000/storefileoutput datatsv hdfs://localhost:9000/inputfile
这里hdfs协议默认连接端口8020,注意hdfs监听端口所使用的IP
可用选项   
-Dimporttsv.skip.bad.lines=false  
'-Dimporttsv.separator=|'  替代tab键

上面只是建表结果，没有数据，一直卡住？？？？


开始导入数据 
${HADOOP_HOME}/bin/hadoop jar ${HBASE_HOME}/lib/hbase-mapreduce-2.2.0.jar completebulkload   hdfs://localhost:9000/storefileoutput  datatsv

bin/hbase org.apache.hadoop.hbase.tool.LoadIncrementalHFiles hdfs://localhost:9000/storefileoutput datatsv


-----


HBase Master 用于协调多个 Region Server，侦测各个 Region Server 之间的状态，并平衡 Region Server 之间的负载
				 还有一个职责就是负责分配 Region 给 Region Server。
				 HBase 允许多个 Master 节点共存，但是这需要 Zookeeper 的帮助。不过当多个 Master 节点共存时，只有一个 Master 是提供服务的，其他的 Master 节点处于待命的状态。

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


Write Ahead Log (WAL) 
-----
bin/hbase hbck 
	报 
	  WARN Unable to load native-hadoop library for your platform. 
     Exception  No FileSystem for scheme: hdfs
 
//---
String tableName = "myTable";
Configuration config = HBaseConfiguration.create();
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.rootdir", "file:///opt/hbase-2.2.0/hdata");  
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.dataDir", "/opt/hbase-2.2.0/zkData"); 
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

	
//---- 包名mapred代表的是hadoop旧API , 而mapreduce代表的是hadoop新的API

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


------------hbase 新版（2.2）API
 /*
	  	<dependency>
		    <groupId>org.apache.hbase</groupId>
		    <artifactId>hbase-client</artifactId>
		    <version>2.2.2</version>
		</dependency>
		hbase-client-2.2.0.jar 
		hbase-common-2.2.0.jar 
		hbase-protocol-2.2.0.jar
			protobuf-java-3.5.1.jar
	-- hbase 运行时要的jar包	
		hbase-shaded-miscellaneous-2.2.1.jar
		commons-configuration-1.6.jar
		hadoop-auth-2.8.5.jar 
		hbase-protocol-shaded-2.2.0.jar 
		hbase-shaded-protobuf-2.2.1.jar
		hbase-shaded-netty-2.2.1.jar
		metrics-core-3.2.6.jar
		htrace-core4-4.2.0-incubating.jar
	 */
	public static void main(String[] args) throws Exception
	{
		//日志提示HADOOP_HOME or hadoop.home.dir are not set.
		System.setProperty("hadoop.home.dir", "/opt/hadoop-3.2.0");
				
		Configuration configuration = HBaseConfiguration.create();
		//客户端只要zookeeper信息即可
		configuration.set("hbase.zookeeper.quorum", "localhost");
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		//configuration.set("zookeeper.znode.parent", "/hbase"); // 默认是　/hbase  
		
		Connection connection = ConnectionFactory.createConnection(configuration);
		Admin admin = connection.getAdmin();
		//admin.compact(tableName);//admin很多功能类似命令行
		
		String nsName="myns";
		String colFamily="cf";
		//没有方法来判断 名称空间 是否存在
		NamespaceDescriptor[] nameSpaces=admin.listNamespaceDescriptors();
		for(NamespaceDescriptor ns: nameSpaces)
		{
			if(nsName.equals(ns.getName()))
			{
				List<TableDescriptor>  tables=admin.listTableDescriptorsByNamespace(Bytes.toBytes(nsName));
				for(TableDescriptor table: tables)
				{
					admin.disableTable(table.getTableName());
					admin.deleteTable(table.getTableName());
				}
				admin.deleteNamespace(nsName);//必须是已经存在的,必须没有表
			}
		}
		NamespaceDescriptor nameSpace=  NamespaceDescriptor.create(nsName).build();
		admin.createNamespace(nameSpace);
		NamespaceDescriptor getNameSpace=admin.getNamespaceDescriptor(nsName);//必须是已经存在的
		
		TableName tableName=TableName.valueOf(nsName+":myTable");
		if(admin.tableExists(tableName)) //如果存在要创建的表，那么先删除，再创建
		{
			if(!admin.isTableDisabled(tableName)) 
				admin.disableTable(tableName);
		    admin.deleteTable(tableName);
		}
		ColumnFamilyDescriptor cf= ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(colFamily)).build();
		List<ColumnFamilyDescriptor> cols= Arrays.asList(cf);
		TableDescriptor tableDescriptor=TableDescriptorBuilder.newBuilder(tableName)
		   .setColumnFamilies(cols)
		   .build();
		//admin.createTable(tableDescriptor);
		//可以多传参数做分区
		admin.createTable(tableDescriptor, new byte[][] {Bytes.toBytes("100"),Bytes.toBytes("500"),Bytes.toBytes("900")});
		//admin.createTable(tableDescriptor,Bytes.toBytes("100"),Bytes.toBytes("900"),10);
		Table table = connection.getTable(tableName); 
		
		for(int i=1;i<=3;i++)
		{
			Put row = new Put (Bytes.toBytes("row"+i));
			row.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("name"), Bytes.toBytes("lisi"+i));
			row.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("age"), Bytes.toBytes("20"+i));
			table.put(row);//方法对应命令行
		}

		Get get = new Get(Bytes.toBytes("row1"));
		//get.addFamily(Bytes.toBytes(colFamily));//只要部分列
		get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("name"));
		
		//get.readAllVersions();
		get.readVersions(5);//describe 'myTable' 显示的VERSION的值是取多的历史版本数
		/*
 		alter 'myns:myTable' , {NAME => 'cf' , VERSIONS=>'3'} 
		put 'myns:myTable' ,'row1','cf:name', '1111'
		put 'myns:myTable' ,'row1','cf:name', '2222'
		put 'myns:myTable' ,'row1','cf:name', '3333'
		scan  'myns:myTable',{RAW=>true,VERSIONS=>10}
		*/
		Result result=table.get(get);
		myPrintResult(result);
		
		Scan scan =new Scan();
		//scan.addFamily(Bytes.toBytes(colFamily)); 
		scan.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes("name"));//可多个列族，多个列

		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);//MUST_PASS_ONE，MUST_PASS_ALL
		SingleColumnValueFilter filter1 =new SingleColumnValueFilter(
				Bytes.toBytes(colFamily),Bytes.toBytes("name")
				, CompareOperator.EQUAL, Bytes.toBytes("lisi1"));
		filterList.addFilter(filter1);

		//ColumnPrefixFilter filter2=new  ColumnPrefixFilter(Bytes.toBytes("name"));//效果和前面的scan.addColumn类似
		//如有两个列簇，都有列名为name，会全部匹配
		//filterList.addFilter(filter2);
		//MultipleColumnPrefixFilter filter3=new MultipleColumnPrefixFilter(new byte[][] {Bytes.toBytes("name")});
		//filterList.addFilter(filter3);
		
		RowFilter filter4=new RowFilter( CompareOperator.EQUAL,new RegexStringComparator("row."));//rowkey正则表达式
		filterList.addFilter(filter4);
		
		scan.setFilter(filterList);//设置多个条件
		//scan.setFilter(filter1);//设置一个条件		
		
		ResultScanner scanner = table.getScanner(scan);
		//ResultScanner scanner = table.getScanner(Bytes.toBytes(colFamily));//只可一个列族或列
		//ResultScanner scanner = table.getScanner(Bytes.toBytes(colFamily),Bytes.toBytes("name"));
		
		Iterator<Result> iterator=scanner.iterator();
		while(iterator.hasNext())
		{
			Result res=iterator.next();
			myPrintResult(res);
		}
		
		Delete delete=new Delete(Bytes.toBytes("row1"));
		//如不加addColumn相当于deleteall
		//delete.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("name"));//删除最新版本的， 加时间戳表示删指定时间版本
		//delete.addColumns() //删全部版本， 加时间戳表示删<=指定时间版本
		table.delete(delete);
		
		
		
		admin.close();
		connection.close();
	}
	private static void myPrintResult(Result result)
	{
		Cell[] cells=result.rawCells();
		for(Cell cell:cells)
		{
			String family=Bytes.toString(cell.getFamilyArray());//取不到值
			String family2=Bytes.toString(CellUtil.cloneFamily(cell));
			String col=Bytes.toString(CellUtil.cloneQualifier(cell));
			String rowkey=Bytes.toString(CellUtil.cloneRow(cell));
			String value=Bytes.toString(CellUtil.cloneValue(cell));
			System.out.println( "rowkey="+rowkey+"__"+family2+":"+col+",value="+value+",timestamp="+cell.getTimestamp());
		}
	}
	
	
