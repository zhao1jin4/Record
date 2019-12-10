
===========kafka	高吞吐量的分布式发布订阅消息系统
kafka_2.12-2.3.1

使用scala语言写的
界面管理工具 https://github.com/yahoo/kafka-manager

broker -> topic (逻辑) -> partion(物理)->segment

每一个topic(只有这一种类型)都可以设置它的partition数量
每个Partition中的消息都是有序的，有自己的偏移量，各分区可能有相同的数据，不能保证全局多区有序，只能保证区内有序

一个消费者可能消费两个toptic,对一个topic 同一组中的consumer不能同时消费同一个分区 

发的数据 ，如是同一组中只能被一个消费者消费
#一个topic 可被每个组中一个消费者消费，即可同时被多个组的 每组中的一个消费者消费 ，可同时消费一个topic 

#一个partition只能被一个消费者消费（一个消费者可以同时消费多个partition）
#offset 是根据 消费者组，分区，topic,来确定的

 消费策略 有 range 和 RoundRibbon 默认是 range
 
 bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group --state 来查询 ASSIGNMENT-STRATEGY 
 bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
 bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group
 bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group --members
 bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group --members --verbose
 
 

多个分区中有leader,follow是备份

如分区有replication,每个分区中有leader

消费者可从上拉消息（还可broker推消息到consumer）

 写消息可传 partition id,如不传就传key使用key的hash决定哪个partition id,如也没传key首次生成随机数决定哪个partition，后面加1
 
 每个分区都要回复给发送者ack消息，如n台故障只要n+1台就可
 		acks= 1 等leader保存成功，不等flow保存，返回ack ，如在返回ack前,leader挂了，丢失数据
 		acks= -1 或 all  等leader,flow保存成功 ，返回ack ,可保护不丢数据，如在返回ack前,leader挂了，但可能有重复数据
 		
 leader,follow,每个有log end offset是自己记录的最大值，highwater mark是所有leader,follow都可读的最大值，也是消费者可取的最多值

 到少一次 去重 幂等性 ,生产者 发producer_session_id,partition_id,seqNumber 三个数据，如在broker中已经存在不写数据
 //事务是对发送者发到topic多个分区中,可能在写一个分区时生产者挂了
 //事务ID应该下次启动能恢复 ，(事务ID写死有没有影响？？，提交事务后，再次执行使用以前事务ID是否可以？？？？)
 //事务ID和会话ID 保存到broker中 如生产者挂了再启进根据事务ID在broker中找到会话ID，就可以实现幂等
		
controller用来选（谁先得到的选举方式）一个节点用来写zookeeper数据, 其它节点自动取zk数据


kafka-consumer-perf-test.sh kafka-producer-perf-test.sh 是用于性能测试的

----

要先启动zookeeper (linux版本可加 -daemon 日志在 <kafka_home>/logs/server.log )
bin/zookeeper-server-start.sh config/zookeeper.properties &
如windows  cd bin/windows
zookeeper-server-start.bat ../../config/zookeeper.properties   
 
启动服务 
bin/kafka-server-start.sh config/server.properties   & 

对应的有停止,不加配置文件参数是停全部
bin/kafka-server-stop.sh config/server.properties    

如windows  cd bin/windows
kafka-server-start.bat ../../config/server.properties      默认监听 9092 端口，启动日志中有 port=9092

#管理topic
建立topic  名为test
kafka-topics  --create --zookeeper localhost:2181	 --replication-factor 1 --partitions 1 --topic test  
#kafka-topics  --create --bootstrap-server localhost:9092	 --replication-factor 1 --partitions 1 --topic test  

查看topic
kafka-topics  --list --zookeeper localhost:2181	
kafka-topics  --list --bootstrap-server localhost:9092	
 
kafka-topics  --describe --zookeeper localhost:2181	 --topic test  
kafka-topics  --describe --bootstrap-server localhost:9092 	 --topic test 

删除topic 
kafka-topics  --delete --zookeeper localhost:2181	 --topic test  
kafka-topics  --delete --bootstrap-server localhost:9092   --topic test 
#只是做了个删除标记，提示 delete.topic.enable 设置为true
#删除后服务挂了???? 启动不了 AccessDeniedException: D:\tmp\kafka-logs\test-0 -> D:\tmp\kafka-logs\test-0.xxxxxxxxx-delete 删zk数据才行

#console命令主要用于测试
发消息  , 没有提示后可输入消息,server.properties 中有配置 port=9092,也有zookeeper端口
kafka-console-producer  --broker-list localhost:9092 --topic test

#处理消息(只linux)
#bin/kafka-run-class.sh   WordCountDemo

收消息,可以收到启动前的消息
kafka-console-consumer  --bootstrap-server localhost:9092 --topic test --from-beginning

再启两个服务 (cluster,即主从和failover)
> cp config/server.properties config/server-1.properties 修改
    broker.id=1 #每个机器设置不相同的数
    listeners=PLAINTEXT://:9093
  	 #log.dir = /tmp/kafka-out
    log.dirs=/tmp/kafka-logs1  #是kafka暂存数据目录 ，不是打印日志目录 
	#zookeeper.connect=localhost:2181 
 这里的log.dir存的是数据,log4j.properties配置也是这 
 
> cp config/server.properties config/server-2.properties 修改
	broker.id=2
    listeners=PLAINTEXT://:9094
    log.dirs=/tmp/kafka-logs2
	#zookeeper.connect=localhost:2181 
$ kafka-server-start  ../config/server-1.properties &
如windows  cd bin/windows
kafka-server-start.bat ../../config/server-1.properties   

$ kafka-server-start  ../config/server-2.properties &	
如windows  cd bin/windows
kafka-server-start.bat ../../config/server-2.properties  


已有3个服务,--replication-factor 不能 > 3,但--partitions可以 > 3
$ kafka-topics  --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic

看哪个broker是leader,还有replaction和partition信息 
$  kafka-topics  --describe --zookeeper localhost:2181 --topic my-replicated-topic   
	leader,replicas中的值是 broker.id
	isr(in-sync 表示可以成为leader的) 
		一种是服务挂了，另一种是从leader同步数据太慢了，导致数据不是最新，就不可用

#测试
指定接口上写(不能写在zookeeper上，任何一个节点都可以写)
$ kafka-console-producer  --broker-list localhost:9092 --topic my-replicated-topic  				
指定接口上读(不能从zookeeper上读,如从9093节点读也是可以的)
$ kafka-console-consumer  --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic  
可加--group <name> 实现 rabbit mq的topic exchange

如当前leader是1,kill它后,再查leader变成其它的了(2)
ps -ef | grep server-1.properties

windows查进程命令
wmic process get processid,caption,commandline | find "java.exe"   
wmic process where "caption = 'java.exe' and commandline like '%server-1.properties%'" get processid
	ProcessId
	6016
taskkill /pid 6016 /f

如再从2(目前的leader)收消息,是读到2所有未读的消息,即使1已经读过 因 --from-beginning (还没有组)
bin/kafka-console-consumer.sh --bootstrap-server localhost:9093 --from-beginning --topic my-replicated-topic 

---导入、导出数据
从文件导入到topic,再topic导出成文件

 echo -e "foo\nbar" > test.txt
如 Windows: 
 echo foo> test.txt
 echo bar>> test.txt

bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
#connect-standalone  ../../config/connect-standalone.properties ../../config/connect-file-source.properties ../../config/connect-file-sink.properties

报
java.lang.ClassNotFoundException: org.osgi.framework.BundleListener
java.lang.ClassNotFoundException: jline.Completor
java.lang.ClassNotFoundException: org.osgi.framework.SynchronousBundleListener

正常会生成test.sink.txt文件
#看topic信息
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning

可再向文件中加内容
echo Another line>> test.txt

---server.properties
zookeeper.connect=localhost:2181 多台用,分隔


host.name= 配置已经过时
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600 防止内存超出OOM，序列化 ，不能超过栈区大小
 
 
log.dir=
log.dirs=可配置多个目录 ,新建topic时看哪个目录分区数少就写哪个
num.partitions=1

 
log.retention.hours=168  消息保留多长时间,7天  
	即/tmp/kafka-logs/my-replicated-topic-0/00000000000000000000.log ，index文件是存第个消息的偏移量，使用下个偏移量计算长度

log.retention.bytes= 保留最大值,不能<最小日志段(按分区?)
log.segment.bytes=1073741824   1G 日志段大小,超过1G就新建一文件，至少有一个当前在使用的日志段
message.max.bytes= 默认1MB,可修改5MB
default.replication.factor=2  默认1,消息多少个复本

log.segment.bytes=1073741824  日志文件最大值，切换新文件
#log.flush.interval.ms=1000
log.flush.offset.checkpoint.interval.ms

log.retention.check.interval.ms=300000   5分，多长时间检查下是否有文件达到 log.retention.hours 的时间
log.cleaner.enable=true

replica.lag.time.max.ms=10000 默认10秒 如果follower在指定时间没有处理完就从isr中去除 

--consumer.properties 
group.id=test-consumer-group   
各个consumer可以组成一个组，每个消息只能被组中的一个consumer消费
如果一个消息可以被多个consumer消费的话，那么这些consumer必须在不同的组
#类似rabbitmq fanout exchanage 的 queue (一个系统的多台机器使用同一个queue)

max.poll.records=500 默认500

exclude.internal.topics=true 默认不显示内部topic

格式化显示内部topic
./kafka-console-consumer.sh   --bootstrap-server localhost:9092  --from-beginning --consumer.config ../config/consumer.properties --topic  __consumer_offsets  --formatter  "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter"
 


--producer.properties
compression.type=none    可为 gzip, snappy, lz4


这些配置可程序覆盖最高,其次是.sh 参数来覆盖


一 磁盘顺序写比随机写快
二  byte zero copy(零字节复制)不用到用户空间 
	一般过程
	1.  从硬盘 读到 内核空间缓存
	2.  从内核空间缓存 读到 用户空间缓存
	3  数据 写套接字缓存
	4  套接字缓存 到 网卡缓存

kafka 用 send file (Java FileChannel.transferTo ) 只有 1，4步，省去2，3步

生产时要经过一次 page cache,因可消费多次，就可直接从内存读

topic->partition->segment

日志目录中的文件名格式 <消息数即offset.index>，index文件中记录第几条消息和消息在log中的位置

对topic增加分区数
$ kafka-topics   --zookeeper localhost:2181 --alter --partitions 2 --topic  test (2表示增加后的数量，不能减少)

官方文档的 3.2 Topic-Level Configs 的那些是可以修改的

leader平衡机制，leader不常变
--describe 信息中replicas中的第一个叫做 preferred leader

如没有leader时用
$ kafka-preferred-replica-election   --zookeeper localhost:2181

auto.leader.rebalance.enable=true 默认true


---分区日志迁移  （增加机器）
按topic迁移,写json文件 
cat topics-to-move.json
	{"topics": [{"topic": "foo1"},
				{"topic": "foo2"}],
	"version":1
	}
$ kafka-reassign-partitions --zookeeper localhost:2181 --topics-to-move-json-file topics-to-move.json --broker-list "1" --generate
 只是生成了计划，第一组Current partition replica assignment 输出有目前要移动地topic在哪个partition上(最好备份，如迁移成功，可以回滚)
 
 将Proposed partition reassignment configuration输出内容写入  expand-cluster-reassignment.json  文件 
 
 开始做移动
$ kafka-reassign-partitions  --zookeeper localhost:2181 --reassignment-json-file  expand-cluster-reassignment.json    --execute

再查看指定的t1是否移动了
$kafka-topics  --describe --zookeeper localhost:2181 --topic t1

验证是否成功
$ kafka-reassign-partitions  --zookeeper localhost:2181 --reassignment-json-file  expand-cluster-reassignment.json --verify

如要只迁移分区，只要修改expand-cluster-reassignment.json 文件即可，里面有分区，再次--execute就可




--监控 kafka offset monitor(功能简单) 和 kafka manager (可监控多个集群,可创建topic)

----Kafka 监控工具  Kafka Eagle
https://github.com/smartloli/kafka-eagle
https://docs.kafka-eagle.org/2.env-and-install/2.installing
也可创建topic





-- JAVA API 
	kafka-streams-2.3.1.jar
	kafka-clients-2.3.1.jar
	
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-streams</artifactId>
    <version>2.3.1</version>
</dependency>


--WordCountApplication	
//要 rocksdbjni-5.7.3.jar
Properties config = new Properties();
config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream("TextLinesTopic");//<key ,value>读入topic

//        JDK 8
/*
        KTable<String, Long> wordCounts = textLines
            .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
            .groupBy((key, word) -> word)
            .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
*/
 //JDK 7
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String textLine) {
                        return Arrays.asList(textLine.toLowerCase().split("\\W+"));
                    }
                })
                .groupBy(new KeyValueMapper<String, String, String>() {
                    @Override
                    public String apply(String key, String word) {
                        return word;
                    }
                })
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
				
wordCounts.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.Long()));//输出topic

KafkaStreams streams = new KafkaStreams(builder.build(), config);
streams.start();

// Serializers/deserializers (serde) 
//是异步发送，不会一定等有返回结果再发下一个
	public class MyKafkaProduer {
	public static void main(String[] args) {
		Properties props = new Properties();
		//props.put("bootstrap.servers", "localhost:9094");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , "localhost:9094");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);//到达大小就写到buffer.memory
		props.put("linger.ms", 1);//等待时间，如没有到batch.size的大小，这么长时间就发送
		
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"hadoop.kafka.CustomPartition");//自己的类
//		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,"hadoop.kafka.CustomProducerInterceptor");//自己的类
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 10; i++)
		{
			//producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
			
			//带回调  ProducerRecord类也可传分区,就不会使用自定义分区
			Future<RecordMetadata> future=producer.send(new ProducerRecord<String, String>("test", 0,Integer.toString(i), Integer.toString(i)),new Callback() {
				//可用lamda表达式
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if(exception==null)
					{
						System.out.println("发送了topic="+metadata.topic()+",parition="+metadata.partition()+",offset="+metadata.offset()+",ValueSize="+metadata.serializedValueSize());
					}else
					{
						exception.printStackTrace();
					}
				}
			}); 
			
		}			
			
		producer.close();
	}
}
//自定义分区,对于参数不传分区的情况，可参考kafka中已经实现的
public class CustomPartition implements Partitioner{
	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public void close() {
	}	
	@Override
	public int 	partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster)
	{	
		System.out.println("到0号分区");
		return 0;
	}
}
//自定义拦截器
public class CustomProducerInterceptor implements ProducerInterceptor{
	@Override
	public void configure(Map<String, ?> configs) {
	}
	@Override
	public void close() {
	}
	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if(metadata!=null)
		{
			System.out.println("时间"+new Date()+",topic:"+metadata.topic()+" 收到确认");
		}else
		{
			exception.printStackTrace();
		}
	}
	@Override
	public ProducerRecord onSend(ProducerRecord record) {
		//当send方法没有callback参数时这个才有用
		System.out.println("时间"+new Date()+",topic:"+record.topic()+"已经发出");//record.value()
		return record;
	}
}

public class MyKafkaProducerTrans {
	public static void main(String[] args) {
		 Properties props = new Properties();
		 props.put("bootstrap.servers", "localhost:9092");
		 props.put("transactional.id", "my-transactional-id");
		 Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
		 producer.initTransactions();

		 try {
		     producer.beginTransaction();
		     for (int i = 0; i < 100; i++)
		         producer.send(new ProducerRecord<>("test", Integer.toString(i), Integer.toString(i)));
		     producer.commitTransaction();
		 } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
		     // We can't recover from these exceptions, so our only option is to close the producer and exit.
		     producer.close();
		 } catch (KafkaException e) {
		     // For all other exceptions, just abort the transaction and try again.
		     producer.abortTransaction();
		 }
		 producer.close();
	}
}


public class MyKafkaConsumer {
	public static void main(String[] args)
	{
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "localhost:9092");
	      //props.put("group.id", "group1");
	     props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
 
        //认值 latest （ 取最新的值），还有 earliest 可选 
	     //只有没有初始化（换组的时候）或没有数据了（过了7天的数据删了）才会生效
	     String doc=ConsumerConfig.AUTO_OFFSET_RESET_DOC; //是文档
	     props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	     
	     
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Arrays.asList("test", "my-replicated-topic"));//可多个topic
	     while (true) {
	         //ConsumerRecords<String, String> records = consumer.poll(100);//timeout millsecond 
	         //如指定时间没有数据就返回空集合,自动提交,这里返回的消息必须全部处理成功,否则就丢了
				 ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));       
	         for (ConsumerRecord<String, String> record : records)
	             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			 	
			 	System.out.println("sleep 3 秒");
	         Thread.sleep(1000);	     
	     }
	     //consumer.close();
	}
}

public class MyKafkaConsumerManualOffset {
	public static void main(String[] args) {
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "localhost:9092");
	     props.put("group.id", "group1");
	     props.put("enable.auto.commit", "false");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Arrays.asList("test", "my-replicated-topic"));
	     /*
	     //offset可存数据库中
	     consumer.subscribe(Arrays.asList("test", "my-replicated-topic"),new ConsumerRebalanceListener() {
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			}
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			}
	     });
	     */
	     final int minBatchSize = 20;
	     List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
	     while (true) {
	         //ConsumerRecords<String, String> records = consumer.poll(100);
	         ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); 
	         for (ConsumerRecord<String, String> record : records) {
	             buffer.add(record);
	         }
	         if (buffer.size() >= minBatchSize) {
	        	 System.out.print("saveDB");
	             consumer.commitSync();//enable.auto.commit:false,如未处理完就断了,重启不会丢失
				  //但如果saveDB处理多条,部分成功,就down机就会重复消费,做幂等,比丢了好
				  //如DB commit成功，还没来的急Kafka commit就挂了，下次就会有重复数据
				  //方式二 异步 
	             /*
	              consumer.commitAsync(new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {

					}} );
	             */
	             
	             buffer.clear();
	         }
	     }
	}
}

	
public class MyKafkaConsumerPartition {
	public static void main(String[] args) {
		 boolean running=true;
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "localhost:9092");
	     props.put("group.id", "group1");
	     props.put("enable.auto.commit", "false");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     try {
	         while(running) {
	             ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
	             for (TopicPartition partition : records.partitions()) { //按partition提交offset
	                 List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
	                 for (ConsumerRecord<String, String> record : partitionRecords) {
	                     System.out.println(record.offset() + ": " + record.value());
	                 }
	                 long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
	                 consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
	             }
	         }
	     } finally {
	       consumer.close();
	     }
	}
}

// Shutdown hook which can be called from a separate thread
     public void shutdown() {
         closed.set(true);
         consumer.wakeup();
     }

