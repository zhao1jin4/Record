apache项目 ，可以不使用Hadoop,但推荐用  

下源码core是用java开发(runtime有java也有用scala开发)

alibaba 收购了开发公司,实时计算比alibaba的Storm,JStorm要快
也可批量任务

因用scala所以只可JDK1.8

主要用于实时计算 ，可以批处理 

Gelly: Flink Graph API
https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/libs/gelly/
新版本把ML去除了



服务组件 Job Managers, Task Managers, Clients
 
JobManager 会以 Slot 为单位调度 Task ，是Pipeline 的 Task

flink-1.9.1-bin-scala_2.12\flink-1.9.1\bin\start-cluster.bat  
提示 Starting a local cluster with one JobManager process and one TaskManager
对应就有 stop-cluster.sh
flink-1.9.1-bin-scala_2.12\flink-1.9.1\log 目录

http://localhost:8081/

---Flink 1.9.1 


netcat工具nc
nc -l 9000  #-l listen

./start-cluster.bat  

./bin/flink run examples/streaming/SocketWindowWordCount.jar --port 9000  #batch 目录下有WordCount.jar
./bin/flink run  				  examples/batch/WordCount.jar   --input README.txt  --output result.txt 

 
在nc终端中 输入字段，按ctrl+c结束输入， 看日志有结果 tail -f log/flink-*-taskexecutor-*.out
http://localhost:8081/#/task-manager  中也有stdOut可看

SocketWindowWordCount.jar/META-INF/MANIFEST.MF 中有 program-class: org.apache.flink.streaming.examples.socket.SocketWindowWordCount



conf目录下有masters文件(要带端口 可不是8081,如8082)和slaves文件,
每个节点都放相同的配置，全主机名都可相互通的(/etc/hosts),PATH,JAVA_HOME
在jobmanager 节点启动 start-cluster.sh  会提示输入每个节点的密码，可配置成不输密码(用户当前用户,即每个节点系统用户可能要相同)
http://localhost:8081/#/task-manager 显示的是全部的slaves文件中对应的IP(干活的节点) ,可以看服务器的日志

同Hadoop
要配置成不用输入密码登录  ssh localhost
$ ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
$ cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys 
测试 ssh localhost 不用密码


-- flink-conf.yaml
taskmanager.numberOfTaskSlots: 1 #一般设置cpu核数

jobmanager.rpc.address: localhost #默认值localhost 只能单机用,如使用 ./jobmanager.sh host参数会覆盖这里，如 start-cluster.sh 是使用 masters文件
jobmanager.rpc.port: 6123 	#默认值6123
#rest.port: 8081 				#默认值 8081
io.tmp.dirs: /data/flink/tmp 	#默认值  /tmp
web.tmpdir: /data/flink/webtmp  	#默认值  /tmp


-------reset api
http://localhost:8081/overview
http://localhost:8081/jobs



----jobmanager 高可用 

https://ci.apache.org/projects/flink/flink-docs-release-1.9/ops/jobmanager_high_availability.html

一个    leading JobManager 没有单点故障

自带zookeeper,要有分布式文件系统，如HDFS , Ceph

masters文件 加到共两个或多个节点 ，8082端口 (两个master节点都可使用，slaves没有记录)

修改 flink-conf.yaml 放开注释 (savepoints不用放开？？)
# state.backend: filesystem
# state.checkpoints.dir: hdfs://namenode-host:port/flink-checkpoints
# high-availability: zookeeper
# high-availability.storageDir: hdfs:///flink/ha/  #也可带hdfs://localhost:9000/flink/ha/ , 不写端口默认为8020
# high-availability.zookeeper.quorum: localhost:2181
# high-availability.zookeeper.client.acl: open


要事先启动 zookeeper

修改flink-1.9.1/conf/zoo.cfg
为zookeeper集群
server.0=localhost:2888:3888

自带的 bin/start-zookeeper-quorum.sh，也可用自己的zookeeper

在jobmanager 节点启动 start-cluster.sh  
 	提示不支持hdfs 网方Optional components处 要下载Pre-bundled Hadoop 2.8.3包,也可maven下载(flink-shaded-hadoop-2-uber-2.8.3-7.0.jar)放lib目录
 	
dashboard 地址变以 http://masterIP:master文件中的端口 , 而不是flink-conf.yaml中的8081

如jobmanager 节点挂了，可单独启动这个节点 
 ./jobmanager.sh  start cluster master1-host 8082


./bin/flink run -m master1:8082 examples/batch/WordCount.jar --input hdfs://master1:9000/word.txt  --output hdfs://master1:9000/result.txt 
 	-c,--class <classname>   
   -m,--jobmanager <arg>  
	 
slave节点不能仿问8082，但可以提交任务到slave节的8082，可以在主节点看到任务

界面上显示 Available Task Slots 为0 不正常  ？？？
有配置 taskmanager.numberOfTaskSlots: 1,配置变单机版本也是为0？？,删安装目录重新解压就好了，可能是plugins目录 

----可以运行在yarn上

也支持 yarn HA

bin/yarn-session.sh
-d,--detached 
-s,--slots <arg>   Number of slots per TaskManager
-tm,--taskManagerMemory <arg>   Memory per TaskManager Container with optional unit (default: MB)
-n,--container <arg>            Number of YARN container to allocate (=Number of Task Managers)

-----

 

<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-java</artifactId>
  <version>1.9.1</version>
</dependency>
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-streaming-java_2.12</artifactId>
  <version>1.9.1</version>
</dependency>
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-clients_2.12</artifactId>
  <version>1.9.1</version>
</dependency>
加入上面就可IDE中开发
 

<!-- 为使用hdfs协议 放flink/lib目录下-->
 <dependency>
	<groupId>org.apache.flink</groupId>
	<artifactId>flink-shaded-hadoop-2-uber</artifactId>
	<version>2.8.3-7.0</version>
</dependency>

有依赖于akka

//flink-core-1.9.0.jar  
//flink-streaming-java_2.12-1.9.0.jar
//flink-java-1.9.0.jar

//如想IDE开发，引用的 flink-1.9.1/lib/flink-dist_2.12-1.9.1.jar

--实时开发示例
package hadoop.flink;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class SocketWindowWordCount {

    public static void main(String[] args) throws Exception {

        // the port to connect to
        final int port;
        try {
            final ParameterTool params = ParameterTool.fromArgs(args);
            port = params.getInt("port");
        } catch (Exception e) {
            System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'");
            return;
        }

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream("localhost", port, "\n");

        // parse the data, group it, window it, and aggregate the counts
        DataStream<WordWithCount> windowCounts = text
            .flatMap(new FlatMapFunction<String, WordWithCount>() {
                @Override
                public void flatMap(String value, Collector<WordWithCount> out) {
                    for (String word : value.split("\\s")) {
                        out.collect(new WordWithCount(word, 1L));
                    }
                }
            })
            .keyBy("word")
            .timeWindow(Time.seconds(5), Time.seconds(1))
            .reduce(new ReduceFunction<WordWithCount>() {
                @Override
                public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                    return new WordWithCount(a.word, a.count + b.count);
                }
            });

        // print the results with a single thread, rather than in parallel
        windowCounts.print().setParallelism(1);

        env.execute("Socket Window WordCount");//Stream式才需求execute
    }

    // Data type for words with count
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {}

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}

nc -l 9000
./bin/flink run -c hadoop.flink.SocketWindowWordCount ~/MyWordCount.jar   --port 9000


--批量开发示例
package hadoop.flink;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment; 
import org.apache.flink.util.Collector;

public class WordCount { 
	public static void main(String[] args) throws Exception {
		
		// Checking input parameters
		final ParameterTool params = ParameterTool.fromArgs(args);

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// make parameters available in the web interface
		env.getConfig().setGlobalJobParameters(params);

		// get input data
		DataStream<String> text;
		if (params.has("input")) {
			// read the text file from given input path
			text = env.readTextFile(params.get("input"));
		} else {
			System.out.println("Executing WordCount example with default input data set.");
			System.out.println("Use --input to specify file input.");
			// get default test text data
			text = env.fromElements(WordCountData.WORDS);
		}

		DataStream<Tuple2<String, Integer>> counts =
			// split up the lines in pairs (2-tuples) containing: (word,1)
			text.flatMap(new Tokenizer())
			// group by the tuple field "0" and sum up tuple field "1"
			.keyBy(0).sum(1);
			
		// emit result
		if (params.has("output")) {
			counts.writeAsText(params.get("output"),WriteMode.OVERWRITE);//可覆盖已有文件
		} else {
			System.out.println("Printing result to stdout. Use --output to specify output path.");
			counts.print();
		}

		// execute program
		env.execute("Streaming WordCount1");
	}  
	public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
			// normalize and split the line
			String[] tokens = value.toLowerCase().split("\\W+");

			// emit the pairs
			for (String token : tokens) {
				if (token.length() > 0) {
					out.collect(new Tuple2<>(token, 1));
				}
			}
		}
	}
}

./bin/flink run -c hadoop.flink.WordCount ~/MyWordCount.jar   --input README.txt  --output result.txt 


--table 开发示例

	flink-table-api-java-1.9.1.jar
	flink-table-api-java-bridge_2.12-1.9.1.jar

<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-table-api-java-bridge_2.12</artifactId>
  <version>1.9.1</version>
  <scope>provided</scope>
</dependency> 

//如要IDE开发再引入 flink-table_2.12-1.9.1.jar

public static void main(String[] args) throws Exception {

	// set up execution environment
	StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

	DataStream<Order> orderA = env.fromCollection(Arrays.asList(
		new Order(1L, "beer", 3),
		new Order(1L, "diaper", 4),
		new Order(3L, "rubber", 2)));

	DataStream<Order> orderB = env.fromCollection(Arrays.asList(
		new Order(2L, "pen", 3),
		new Order(2L, "rubber", 3),
		new Order(4L, "beer", 1)));

	// convert DataStream to Table
	Table tableA = tEnv.fromDataStream(orderA, "user, product, amount");
	// register DataStream as Table
	tEnv.registerDataStream("OrderB", orderB, "user, product, amount");

	// union the two tables
	Table result = tEnv.sqlQuery("SELECT * FROM " + tableA + " WHERE amount > 2 UNION ALL " +
					"SELECT * FROM OrderB WHERE amount < 2");

	tEnv.toAppendStream(result, Order.class).print();

	env.execute();
}
public static class Order {
		public Long user;
		public String product;
		public int amount;
}

public static void main(String[] args) throws Exception {

	// set up execution environment
	ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
	BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);

	DataSet<WC> input = env.fromElements(
		new WC("Hello", 1),
		new WC("Ciao", 1),
		new WC("Hello", 1));

	// register the DataSet as table "WordCount"
	tEnv.registerDataSet("WordCount", input, "word, frequency");

	// run a SQL query on the Table and retrieve the result as a new Table
	Table table = tEnv.sqlQuery(
		"SELECT word, SUM(frequency) as frequency FROM WordCount GROUP BY word");

	DataSet<WC> result = tEnv.toDataSet(table, WC.class);

	result.print();
}
public static void main(String[] args) throws Exception {
	ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
	BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);

	DataSet<WC> input = env.fromElements(
			new WC("Hello", 1),
			new WC("Ciao", 1),
			new WC("Hello", 1));

	Table table = tEnv.fromDataSet(input);

	Table filtered = table
			.groupBy("word")
			.select("word, frequency.sum as frequency")
			.filter("frequency = 2");

	DataSet<WC> result = tEnv.toDataSet(filtered, WC.class);

	result.print();
}
public static class WC {
		public String word;
		public long frequency;
}
//=================批量API
//--map
public static void main(String[] args) throws Exception {
	final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	Tuple2<Integer,Integer> num1=	new Tuple2<>(1,2) ;
	Tuple2<Integer,Integer> num2=	new Tuple2<>(3,5) ;
	Tuple2<Integer,Integer>  [] array=new Tuple2 [] {num1,num2} ;
	DataStream<Tuple2<Integer,Integer>>  text = env.fromElements(array);
	DataStream<Integer> counts = text.map(new Plus()) ;
	counts.print(); 
	env.execute("MapFunctionTest");
}
public static final class Plus implements  MapFunction<Tuple2<Integer, Integer>,Integer> {
	@Override
	public Integer  map(Tuple2<Integer, Integer> value) throws Exception {
		return   value.f0+value.f1;
	}
}
//-filter
public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<Integer>  text = env.fromElements(new Integer[] {1,3,4,5,0,-3,-5});
		DataStream<Integer> counts = text.filter(new FilterFunction<Integer>() {
			@Override
			public boolean filter(Integer value) throws Exception {
				return value>0;
			}
		}) ;
		counts.print(); 
		env.execute("FilterFunctionTest");
	} 
//-----mapPartition
public static void main(String[] args) throws Exception {
		final  ExecutionEnvironment env =  ExecutionEnvironment.getExecutionEnvironment();
//		DataSource<String>  text = env.fromElements(WordCountData.WORDS);
		//------
		DataSet<Long> numbers = env.generateSequence(1, 100);
		DataSet<String> text = numbers.map(new MapFunction<Long,String>(){
			@Override
			public String map(Long value) throws Exception {
				return value+"'";
			}
		}); 
		 DataSet< Long> mapPartition = text.mapPartition(new MapPartitionFunction<String, Long>() {
			  public void mapPartition(Iterable<String> values, Collector<Long> out) {
			    long c = 0;
			    for (String s : values) {
			      c++;
			    }
			    out.collect(c);
			  }
			});
		 mapPartition.print();
		//env.execute("MyMapPartitionFunctionTest");
	} 
	
//---groupby
public static void main(String[] args) throws Exception {
		final  ExecutionEnvironment env =  ExecutionEnvironment.getExecutionEnvironment();
		//DataSource<WC>  worlds = env.fromElements(new WC[] {new WC("hello",1),new WC("world",1),new WC("world",2)});
		DataSource<WC>  worlds = env.fromElements( new WC("hello",1),new WC("world",1),new WC("world",2) );
		ReduceOperator<WC> reduce = worlds.groupBy( new KeySelector<MyGroupBy.WC, String>() {
			@Override
			public String getKey(WC value) throws Exception {
				return value.word;
			}
		})
		.reduce(new ReduceFunction<MyGroupBy.WC>() {
			@Override
			public WC reduce(WC w1, WC w2) throws Exception {
				return new WC(w2.word,w1.frequency+w2.frequency);
			}
		});
		reduce.print();
//		env.execute("MyGroupByFunctionTest");
	} 
	
//---broadcast
public static void main(String[] args) throws Exception {
		final  ExecutionEnvironment env =  ExecutionEnvironment.getExecutionEnvironment();
		// 1. The DataSet to be broadcast
		DataSet<Integer> toBroadcast = env.fromElements(1, 2, 3);
		DataSet<String> data = env.fromElements("a", "b");
		MapOperator<String, String> result = data.map(new RichMapFunction<String, String>() {
		    @Override
		    public void open(Configuration parameters) throws Exception {
		      // 3. Access the broadcast DataSet as a Collection
		      Collection<Integer> broadcastSet = getRuntimeContext().getBroadcastVariable("broadcastSetName");
		      System.out.println("取到广播"+broadcastSet);
		    }
		    @Override
		    public String map(String value) throws Exception {
		        return value;
		    }
		}).withBroadcastSet(toBroadcast, "broadcastSetName"); // 2. Broadcast the DataSet
		//广播出去的变量存在每个taskManager的内存中，不应过大
		result.print(); 
//		env.execute("MyBroadcastVarTest");
	}
//--distrube cache
public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//		env.registerCachedFile("hdfs:///path/to/your/file", "hdfsFile");
		env.registerCachedFile("file:///etc/fstab", "localExecFile", false);//最后一参数是否可执行
		DataSource<String> text = env.fromElements("11","22");
		DataSet<String> input =  text.map(new MyMapper());
		input.print();
//		env.execute("MyDistributeCacheTest");
}
class MyMapper extends RichMapFunction<String, String> {
    @Override
    public void open(Configuration config) {
      File myFile = getRuntimeContext().getDistributedCache().getFile("localExecFile");
      System.out.println("拿到缓存文件大小为"+myFile.getUsableSpace()+"内容为:");
      Scanner scanner;
      try {
		scanner = new Scanner( new FileInputStream(myFile));
		 while(scanner.hasNextLine())
	     {
	      	String line=scanner.nextLine();
	      	System.out.println(line);
	     }
      } catch (FileNotFoundException e) {
			e.printStackTrace();
      }
    }
    @Override
    public String map(String value) throws Exception {
     return value;
    }
}

---------流式 kafka 
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-kafka-0.11_2.12</artifactId>
    <version>1.9.1</version>
</dependency>
0.11表示为kafak版本

//	flink-connector-kafka-0.11_2.12-1.9.1.jar
//	间接引用 	flink-connector-kafka-base_2.12-1.9.1.jar ,flink-runtime_2.12-1.9.1.jar
  
public static void main(String[] args) throws Exception {
	    // https://ci.apache.org/projects/flink/flink-docs-release-1.9/getting-started/tutorials/datastream_api.html 尾
        //IDE开发测试没有效果 ??? ，原因可能是kafak版本要为0.11才行，可能要运行在flink cluster 环境上
     final int port;
     try {
         final ParameterTool params = ParameterTool.fromArgs(args);
         port = params.getInt("port");
     } catch (Exception e) {
         System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'");
         return;
     }
     final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
      //自定义数据源使用 StreamExecutionEnvironment.addSource(function)//实现SourceFunction接口，或 extends RichSourceFunction类
       //env.addSource(new FlinkKafkaConsumer011<>(topic,deserializer,props));
        
     DataStream<String> text = env.socketTextStream("localhost", port, "\n");
     DataStream<WordWithCount> windowCounts = text
        .flatMap(new FlatMapFunction<String, WordWithCount>() {
             @Override
             public void flatMap(String value, Collector<WordWithCount> out) {
                 for (String word : value.split("\\s")) {
                     out.collect(new WordWithCount(word, 1L));
                 }
             }
         })
        .keyBy("word")
        .timeWindow(Time.seconds(5), Time.seconds(1))//每5秒执行一次
        .reduce(new ReduceFunction<WordWithCount>() {
            @Override
            public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                return new WordWithCount(a.word, a.count + b.count);
            }
        });
     
    windowCounts.map( new MapFunction<WordWithCount , String>() {
        @Override
        public String map( WordWithCount  value) {
            return value.toString();
        }
    })
     //自定义数据输出Sink调用StreamExecutionEnvironment.addSink(function)//实现SinkFunction接口，或 extends RichSinkFunction类
     .addSink(new FlinkKafkaProducer011<>("localhost:9092", "wiki-result", new SimpleStringSchema()));
      env.execute("Flink Connect to Kafka Test");
 }
 
 
----------- planner-blink 是1.9新的 
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-table-planner-blink_2.12</artifactId>
  <version>1.9.1</version>
  <scope>provided</scope>
</dependency>



