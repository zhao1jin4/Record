
===========kafka	高吞吐量的分布式发布订阅消息系统
使用scala语言写的


要先启动zookeeper

启动服务
bin/kafka-server-start.sh config/server.properties   & 

如windows 
cd bin/windows
kafka-server-start.bat ../../config/server.properties   &   默认监听 9092 端口，启动日志中有 port=9092

建立topic 名为test
$ kafka-topics  --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test  (kafka-topics.bat)

查看
$ kafka-topics  --list --zookeeper localhost:2181		

发消息  , 没有提示后可输入消息,server.properties 中有配置 port=9092,也有zookeeper端口
$ kafka-console-producer  --broker-list localhost:9092 --topic test

#处理消息
#bin/kafka-run-class.sh   WordCountDemo

收消息,可以收到启动前的消息
$ kafka-console-consumer  --zookeeper localhost:2181 --topic test --from-beginning

再启两个服务 (cluster,即主从和failover)
> cp config/server.properties config/server-1.properties 修改
    broker.id=1
    listeners=PLAINTEXT://:9093
    log.dir=/tmp/kafka-logs1 
 
 这里的log.dir存的是数据,log4j.properties配置也是这 
 
> cp config/server.properties config/server-2.properties 修改
	broker.id=2
    listeners=PLAINTEXT://:9094
    log.dir=/tmp/kafka-logs2
$ kafka-server-start  ../config/server-1.properties &
$ kafka-server-start  ../config/server-2.properties &	

已有3个服务,--replication-factor 3
$ kafka-topics  --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic

看哪个broker是leader,还有replaction和partition信息 
$  kafka-topics  --describe --zookeeper localhost:2181 --topic my-replicated-topic  如查topic名为test就一个
	isr(in-sync 表示可以成为leader的) ,leader,replicas中的值是 broker.id
	一种是服务挂了，另一种是从leader同步数据太慢了，导致数据不是最新，就不可用
$ kafka-console-producer  --broker-list localhost:9092 --topic my-replicated-topic  				指定接口上写

$ kafka-console-consumer  --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic  指定接口上读
可加--group <name> 实现 rabbit mq的topic exchange

如当前leader是0,kill它后,再查leader变成1了
ps -ef | grep server-1.properties

windows查进程命令
wmic process get processid,caption,commandline | find "java.exe"   
wmic process where "caption = 'java.exe' and commandline like '%server-1.properties%'" get processid
ProcessId
6016
> taskkill /pid 6016 /f

如再从1(目前的leader)收消息,是读到1所有未读的消息,即使0已经读过
bin/kafka-console-consumer.sh --bootstrap-server localhost:9093 --from-beginning --topic my-replicated-topic 

---server.properties
zookeeper.connect=localhost:2181 多台用，分隔


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
log.retention.bytes= 保留最大值,不能<最小日志段(按分区?)
log.segment.bytes=日志段大小,至少有一个当前在使用的日志段
message.max.bytes= 默认1MB,可修改5MB
default.replication.factor=2  默认1,消息多少个复本

log.segment.bytes=1073741824  日志文件最大值，切换新文件
#log.flush.interval.ms=1000
log.flush.offset.checkpoint.interval.ms

log.retention.check.interval.ms=300000   5分，多长时间检查下是否有文件达到 log.retention.hours 的时间
log.cleaner.enable=true

--consumer.properties
group.id=test-consumer-group  类似rabbitmq topic exchanage 的queue

--producer.properties
compression.type=none    可为 gzip, snappy, lz4

这些配置可程序覆盖最高,其次是.sh 参数来覆盖


一。磁盘顺序写比随机写快
二 。byte zero copy不用到用户空间

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





-- JAVA API 
	kafka-streams-1.1.0.jar
	kafka-clients-1.1.0.jar

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


	public class MyKafkaProduer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9094");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 10; i++)
			producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
		producer.close();
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
	     props.put("group.id", "group1");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Arrays.asList("test", "my-replicated-topic"));
	     while (true) {
	         ConsumerRecords<String, String> records = consumer.poll(100);//timeout millsecond 如指定时间没有数据就返回空集合,自动提交,这里返回的消息必须全部处理成功,否则就丢了
	         for (ConsumerRecord<String, String> record : records)
	             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
	     }
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
	     final int minBatchSize = 20;
	     List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
	     while (true) {
	         ConsumerRecords<String, String> records = consumer.poll(100);
	         for (ConsumerRecord<String, String> record : records) {
	             buffer.add(record);
	         }
	         if (buffer.size() >= minBatchSize) {
	        	 System.out.print("saveDB");
	             consumer.commitSync();//enable.auto.commit:false,如未处理完就断了,重启不会丢失
				  //但如果saveDB处理多条,部分成功,就down机就会重复消费,做幂等,比丢了好
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