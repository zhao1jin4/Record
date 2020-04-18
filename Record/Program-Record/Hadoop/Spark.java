
Flink
---------------------------------hadoop 子项目 Spark 比MapReduce 快
Spark 从  2.4.2 版本开始是使用Scala 2.12 构建的

可运行在hadoop yarn ,Kubernetes,可从HBase上读数据
spark streaming实时计算

MongoDB Spark Connector

spark-2.3.0-bin-hadoop2.7 
 
<dependency>
  <groupId>org.apache.spark</groupId>
  <artifactId>spark-core_2.11</artifactId>
  <version>2.3.1</version>
</dependency>

 

./bin/spark-shell	使用 Scala 语言, 提示SparkUI  http://127.0.0.1:4040 
scala> 

.bin/spark-shell --master spark://node:7077 连接远程
--executor-memory  1G   --executor-cores 2   --num-executors 2   --driver-memory 1024M  #driver客户端内存   

./bin/pyspark		使用 Python 语言
>>>exit();


scala>var rdd=sc.parallelize(List(1,2,3,4,5,6))    //在打sc.  (SparkContext)可以按tab键自动输出,parallelize并行化把数组转换为分布式rdd
rdd.aggregate(10)(_+_,_+_)  //看javaDoc,scalaDoc, 立即执行，第一个初始值，每个分区使用初始值，再合并结果时再使用初始值
rdd.aggregate(20)(math.max(_,_),_+_) //把每个分区的最大值(使用初始值做比较)相加

var pairRDD= sc.parallelize(List( ("cat",2),("cat",2),("dog",12),("mouse",2) ))
pairRDD.aggregateByKey(0)(_+_,_+_).collect  //把每个分区的相加(使用初始值) 再把每个分区的结果相加

pairRDD.combineByKey(List(_),(x:List[Int],y:Int )=>x:+y, (m:List[Int],n:List[Int])=>m++n )


var rdd1=rdd.repartition(3) //重新分区,就是调用的coalesce,传shuffle =true,可能会多机器间相互传数据，性能会低
rdd1.partitions.length
var rdd2=rdd.coalesce(3) //shuffle默认false ，不会在多机器间相互传数据，只在本机做分区的合并，可能使分区数变少，
rdd2.partitions.length

pairRDD.countByKey 
rdd.countByValue

var rdd=sc.parallelize(List(("e",5),("a",4),("f",11))  )



var rdd=sc.parallelize(List(("a","1 2"),("b","3"),("d","4 5"))  )
rdd.flatMapValues(_.split(" "))  //Array((a,1), (a,2), (b,3), (d,4), (d,5))
pairRDD.foldByKey(0)(_+_)


rdd.filterByRange("a","e").collect
rdd.keyBy(_.length) //传value生成 key
rdd.keys
rdd.values

rdd.take(2) 取前2个
rdd.first()
rdd.takeOrdered(2)
rdd.foreach() //在executor中执行，可在控制台中stdout看到 
scala>val mapRdd=rdd.map(2 * _)    //每个元素*2
scala>mapRdd.collect  // 执行collect方法,返回Array
scala>mapRdd   //看
scala>val filterRdd=mapRdd.filter( _>5) //过滤 >5 的
scala>val filterRdd2=sc.parallelize(List(1,2,3,4,5,6)).map(2 * _).filter( _>5) // 一行式写法，scala
scala>var rdd1=sc.textFile("~/world.txt") // 以空格分格的字母文件,路径如C:是不行的，不会立即读数据
scala>rdd1.cache
scala>rdd1.count
scala>val worldCount=rdd1.flatMap(_.split(' ')).map((_,1)).reduceByKey(_+_)	  
	// (_,1) 同 x=>(x,1) ,把文件中每个字母做为key,value为1
	// .reduceByKey(_+_) 表示把相同的字母的key的值相加
	.sortBy(_._2,false) //按值排，false降序
	.collect //前面没有执行，collect才真执行
scala>val worldCount=rdd1.flatMap(_.split(' ')).map((_,1)).groupByKey  //相同的key的值，放在一个Array中	 
scala> worldCount.map(t=>(t._1,t._2.sum))
scala> worldCount.mapValues(_.sum) 
scala>worldCount.collect
scala>worldCount.collect.toBuffer   //ArrayBuffer
scala>worldCount.saveAsTextFile("~/output_dir") //windows下不行，不能chmod
scala>
scala>var result = rdd union udd2   //可能要相同类型的
scala>var result = rdd join udd2   //可能要相同类型的
scala>var result = rdd zip udd2  (key,value)
scala>
scala>:quit
scala>
方法名和scala很像，但是在多台机器上运行的

def func1(index:Int,iter:Iterator[Int]):Iterator[String] = {
   iter.toList.map(x=> "[id:"+index+",value:"+x+"]").iterator
}

rdd.mapPartitionsWithIndex(func1).collect 看每个分区有什么
rdd.mapPartitions(it=>{it.map(x=>x*10)})
rdd.foreachPartition(it=>it.foreach(println)) //立即执行

var lst=List(2)
lst:+3 //生成新的list
var myPart=new MyPartition
partitionBy(myPart)

//自定义分区
class MyPartition  extends Partitioner
{
	private int nums;
	public MyPartition(int nums)
	{
	 	super();
		this.nums = nums;
	}
	@Override //决定在哪个分区
	public int getPartition(Object obj)
	{
		Integer val=Integer.parseInt(obj.toString());
		return val.intValue()%nums; 
	}
	@Override //返回分区总数
	public int numPartitions()
	{
		return this.nums;
	}
}
class MyPartition extends Partitioner //spark
{
	
	override def numPartitions:Int=
	override def 
}

----Standalone  集群


cp spark-env.sh.template spark-env.sh

SPARK_MASTER_HOST=node1
SPARK_MASTER_PORT=7077


cp slaves.template slaves  哪些主机做子worker节点，每行一个主机

只能linux机器上运行了  
./sbin/start-all.sh 主节点上，其实是先spark-config.sh ，再start-master.sh，再start-slaves.sh
./sbin/start-master.sh  主节点上web UI (http://localhost:8080  
./sbin/start-slave.sh <master-spark-URL>

多Master要用zookeeper
spark-env.sh中配置 SPARK_DAEMON_JAVA_OPTS=-Dspark.deploy.recoveryMode=ZOOKEEPER -Dspark.deploy.zookeeper.url=zk1,zk2 -Dspark.deploy.zookeeper.dir=/spark
第二个master上启动./sbin/start-master.sh   (里面其实启动了spark-daemon.sh) 看管理界面，有一个状态是standby

spark-shell --master=spark://master1:7077,spark://master2:7077 会启动一个sparkSubmit进程，向master提交任务，而其它slave进程(Worker)启动CoarseGainedExectutorBackend (Executor)进程 
可加 
  --executor-memory 512mb 每个executor使用的内存 默认1G
 --total-executor-cores 4
 
rdd.partitions.length 看rdd有几个分区，值对应于  --total-executor-cores
sc.parallelize(List(1,2,3,4,5,6),2); //可以指定分区数2
rdd=sc.textFile("hdfs://namenode:9000/dir") //如果每个文件<128M 按文件数做分区，如> 128M切成多个
rdd=sc.textFile("hdfs://namenode:9000/dir",2) //可以指定分区数

 yarn 对比 spark
 Yarn ResourceManager ---- spark master
 yarn nodeManager --- spark workder
 yarn yarchild ---- spark executor 
 yarn  appMaster   --- spark submit
 
 
 
 transforming 函数是lazy的，如flatMap,reduceByKey
 action函数才开始执行，如collect,count,first
 
----hadoop   
textFile("hdfs://ip:port/dir")  
saveAsTextFile("hdfs://ip:port/dir")

如果多台executor使用的路径都是/而不是hdfs那么文件写在每个executor自己的机器目录上，不方便查看，建议用hdfs

  
Resilient Distributed Dataset (RDD)

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class TestSpark {
 public static void main(String[] args) {
   String logFile = "~/spark-1.0.0-bin-hadoop2/README.md";
   SparkConf conf = new SparkConf().setAppName("Simple Application");//如加.setMaster("local[4]");表示本地用4个线程跑,就可在eclipse中运行
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
	String logFile = "~/spark-1.0.0-bin-hadoop2/README.md";
    SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local[4]");
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);
    JavaRDD<String> lines = ctx.textFile(logFile, 1);

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterator<String> call(String s) {
        return Arrays.asList(SPACE.split(s)).iterator();
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

还有其它方式来提交任务

df = spark.read.json("logs.json") 
df.where("age > 21")  
	.select("name.first").show()


------------------------- Spark SQL

 public static void main(String[] args) throws Exception 
 {
  SparkSession spark = SparkSession
      .builder()
      .appName("Java Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate();
	 Dataset<Row> df = spark.read().json("examples/src/main/resources/people.json");
    df.show();
	df.select(col("name"), col("age").plus(1)).show();
	Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
    sqlDF.show();
	  
} 
------------------------- Spark streaming 
使用 DStreams API  老的API
 
SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaWordCount").setMaster("local[4]"); 
JavaStreamingContext jsc = new JavaStreamingContext(sparkConf, new Duration(2000));


------------------------- Spark Structured Streaming 
使用  Datasets 和 DataFrames API 比 DStreams API 要新 








