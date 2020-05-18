 
---------------------------------hadoop 子项目 Pig
<dependency>
  <groupId>org.apache.pig</groupId>
  <artifactId>pig</artifactId>
  <version>0.13.0</version>
</dependency>

Pig是MapReduce的一个抽象

可以把命令转换为MapReduce Job

export JAVA_HOME=/usr
bin/pig -x mapreduce #Mapreduce 模式(默认)
bin/pig -x local     #本地模式
java -cp pig-0.12.1.jar org.apache.pig.Main -x local ,使用java启动本地模式

Pig Latin 语句 以;结尾,可以保存为.pig文件,以/* */做为多行注释,--做单行注释,可在任意位置
$ pig -x local xx.pig

grunt> 交互命令以;结尾
grunt>quit
grunt> A = load '/etc/passwd' using PigStorage(':'); 
grunt> B = foreach A generate $0 as id; 
grunt> dump B; 
显示 (root)  行被() 包着
 
>A = LOAD 'student' USING PigStorage() AS (name:chararray, age:int, gpa:float); --默认以tab分隔,可'\t'
>B = FOREACH A GENERATE name;
> describe A;

--script1-local.pig
REGISTER ./tutorial.jar; 
raw = LOAD 'excite-small.log' USING PigStorage('\t') AS (user, time, query);
clean1 = FILTER raw BY org.apache.pig.tutorial.NonURLDetector(query);  --类  extends FilterFunc 
houred = FOREACH clean1 GENERATE user, org.apache.pig.tutorial.ExtractHour(time) as hour, query;  
ngramed1 = FOREACH houred GENERATE user, hour, flatten(org.apache.pig.tutorial.NGramGenerator(query)) as ngram; --类是extends EvalFunc<DataBag> 返回DataBag
ngramed2 = DISTINCT ngramed1;
hour_frequency1 = GROUP ngramed2 BY (ngram, hour);
hour_frequency2 = FOREACH hour_frequency1 GENERATE flatten($0), COUNT($1) as count; -- COUNT参数是bag中
hour00 = FILTER hour_frequency2 BY hour eq '00';
hour12 = FILTER hour_frequency2 BY hour eq '12';
same = JOIN hour00 BY $0, hour12 BY $0;--类似数组合并
STORE same INTO '/tmp/tutorial-join-results' USING PigStorage();//保存在/tmp/tutorial-join-results/part-r-00000




 
DataBag bag = DefaultBagFactory.getInstance().newDefaultBag();
Tuple t1 = TupleFactory.getInstance().newTuple(3);
t1.set(0, "word");//子元素可以是基本类型,也可以是Bag
t1.set(1, "02");
t1.set(2, 2);// Tuple 是(,,) 如 (word,02,2)
bag.add(t1);//Bag 是{,,} 如 {(word,02,2)}
Iterator<Tuple> it = bag.iterator();

extends FilterFunc {
 public Boolean exec(Tuple arg0) throws IOException {
	arg0.get(0);
 }
}
extends EvalFunc<T> {
    public T exec(Tuple input) throws IOException {
	}
}

 


