国家企业信用信息公示系统
http://www.gsxt.gov.cn/index.html


-----------


-----------


int x=4;
System.out.println("value is " +((x>4)?99.9:9));
//  答案: 9.0   : 前面的是double,所以后面转为double
 
 
 //const是java的关键字,但是java没有实现它 
 
 short s1 = 1;
 //s1 = s1 + 1;//报错,要int->short
 s1 += 1;//
 
 //效率最高的方法,实现从1加到100.
 int sun = 0;
 for(int i = 1,j = 100 ; i <= 50 ; i++,j--){
	 sun = sun + i + j;
 }
 System.out.println("sun is " + sun );
 
 
 
 System.out.println(5.0942*1000);//5094.2
 System.out.println(5.0943*1000);//5094.299999999999
 System.out.println(5.0944*1000); //5094.400000000001   浮点数的计算机表示方式有关
 
System.out.println(5.00-4.90); //结果是0.09999999999999964
System.out.println(5.00f-4.90f);//结果是0.099999905
float ss = new BigDecimal(5.00).subtract(new BigDecimal(4.90)).floatValue(); //正确结果
System.out.println(ss);

--------------设计模式
设计模式六大原则（1）：单一职责原则   	不要存在多于一个导致类变更的原因,即一个类只负责一项职责。 

设计模式六大原则（2）：里氏替换原则  	所有引用基类的地方必须能透明地使用其子类的对象。

设计模式六大原则（3）：依赖倒置原则		 要针对接口编程,不要针对实现编程 ,抽象不应该依赖细节；细节应该依赖抽象。 

设计模式六大原则（4）：接口隔离原则		客户端不应该依赖它不需要的接口 , 
	
设计模式六大原则（5）：迪米特法则 		一个对象应该对其他对象保持最少的了解。 		降低类与类之间的耦合。

设计模式六大原则（6）：开闭原则 		一个软件实体如类、模块和函数应该对扩展开放，对修改关闭。 


 

简单工厂和工厂方法不一样的是没有抽像工厂类
工厂方法　　只一个抽像产品类(可多个实现),一个抽像工厂类和一个实现工厂类,抽像工厂类(一个方法)只能创建一种的抽像产品的实现
抽像工厂  　　多个抽像产品类(可多个实现),一个抽像工厂类和多个实现工厂类,抽像工厂类(多个方法)可以交叉创建不同的抽像产品的实现

对使用者实例化隐藏起来，

JTA 设计模式  应该是桥接 



JDK1.6 字符串拼接,编译优化自动生成 StringBuilder 



 
 
public void myMethod(Object o){
System.out.println("My Object");
}
public void myMethod(String s){
System.out.println("My String");
}

myMethod(null);//调用的是String 参数
 

synchronized 做用于 static 方法上,是对类一级的锁,即两个线程对一个类的两个方法 synchronized static 同时只有一个可会被执行

单例
class Singleton
{       
  private static Singleton single;//或者 =new 
  private Singleton(){} 
		 
  public static Singleton getSingle() //或加 synchronized
  {        
	if(single == null)
	{          
		synchronized (Singleton.class)   //(double check JDK7,8成立,老版本JDK可能有问题)
		{        
			if(single == null)            
				 single = new Singleton();               
		}       
	}         
	return single;
  }
}


final abstract 不能同时定义



-----集合相关
Enumeration<String> enumer= vector.elements(); 来遍历可以边遍历边删无素
vector.iterator();//不能加,删


Vector 如构造时不传扩容数,默认是一倍,实现是 Arrays.copyOf 建新的数组,再复制过去
ArrayList  一次扩容一半,实现是 Arrays.copyOf




http://www.cnblogs.com/liqizhou/archive/2012/09/27/java%E4%B8%ADtreemap%E5%92%8Ctreeset%E5%AE%9E%E7%8E%B0%E7%BA%A2%E9%BB%91%E6%A0%91.html
TreeMap   红黑树是一种自平衡排序二叉树, 添加元素、取出元素的性能都比 HashMap 低
红黑树并不是真正的平衡二叉树，但在实际应用中，红黑树的统计性能要高于平衡二叉树，但极端性能略差

TreeMap 红黑树 source code  ,lock ,ConcurrentSkipListMap  SortedMap

HashMap key 为何能为空 ，因如有判断如为null hashcode就为0,hashtable key为什么不能为空
HashTable 的key和value 都不能为空,key没有!=null的判断取hashCode,value有判断

ConcurrentHashMap 为什么快，  
JDK7 是分段锁
JDK8 是  transient volatile table，table数组＋单向链表＋红黑树的结构
sychrozie锁数组无素
个数超过8(默认值)的列表,并且总元素数>64时 jdk1.8 中采用了红黑树的结构 ，只锁定当前链表或红黑二叉树的首节点 ， CAS更新容量





使用ReentrantLock 分段加锁,可tryLock,newCondition多个，可lockInterruptibly();
CopyOnWriteArrayList最快的list
如果是接口实现的代理,接口的方法必须是public的  ,如cyglib 实现的代理，单独测试不是private即可



left join 退火算法,表连接查询

K-means算法理解及SparkCore实现

LinkHashMap,TreeMap 性能比较

---数据结构
图的存储,最可表示有向,权重
	邻接矩阵,如是无向图就是一个对称矩阵,数字可表示权重,或者0,1表示有元
	邻接表,首先是一维数组,每个元素可能是一个链表,权重存在链表中

图的最短(权重)路径,地铁最近票价

树的广度优先遍历,深度优先
平衡二叉树
二叉树中序遍历,写出
堆排序(二叉树)

排序 的时间复杂度,空间复杂度

---


---NIO 多线程 JVM
NIO，BIO 实现原理  （老的叫BIO）
服务端只需启动一个专门的线程来处理所有的 IO 事件，Selector允许单线程处理多个 Channel，得向Selector注册Channel，然后调用它的select()方法
通道与流的不同之处在于通道是双向的,UNIX 模型中，底层操作系统通道是双向的。
通道的读写只对Buffer来操作而不是数组，比按(流式的)字节处理数据要快得多

 伪异步IO 一个线程池和任务队列去处理所有客户端的请求,当有一个客户端的读取信息非常慢时，服务器对其的写操作时会很慢，甚至会阻塞很长时间，因为线程池中的线程是有限的，当有客户端需要分配线程时，就会导致新任务在队列中一直等待阻塞的客户端释放线程

只能由一方A传到另一方B，则称为单工
由A传到B，又能由B传A，但只能由一个方向上的传输存在，称为半双工传
在任意时刻，线路上存在A到B和B到A的双向信号传输


Java逃逸  TLAB 以及Java对象分配 

ByteBuffer 存在哪
深入理解 ByteBuffer

  
TCC 事务
 
java 线程,入口还是主线程

线程进入sleep 状态时,线程进入哪里去了
埋点
分布式  事务JTA ，日志Flume
主动
加密 SHA-1(在JDK9中要禁用了) , DES
高并发  导致的问题及解决,事务安全,锁,nginx 缓存静态页面,Redis缓存,读写分离,SQL优化,表优化索引,分表 , NIO类库,使用ThreadLocal, 有看书
哪块熟悉

java 建议线程数 CPU核数加1吗
threadpool gc


ThreadPoolExecutor   wait 作用


mongodb 比较的true和查询出来的true有何区别


mysql innodb index



yeild  wait

proxy 实现  是 extends 或 implements 吗

spring 配置事务方法,如果不是 pulic 的可以吗

一致性hash (和solr)  , 虚IP 
PV 并发   单点 500  

 DevOps  开发运营  是 Agile 的延伸  
 (自动化运维之SaltStack ,用 Python语言开发的,开源的,很容易管理上万台服务器 ,完成服务器配置管理的功能)
 Puppet,Ansible
微服务架构 spring boot,Spring Cloud 


银行小核心大外围



永道图

memcache数据类型只string,没有主从的


GC为什么要两个Suvivor, 避免了碎片化的发生



hession 如何改写 serializeable


MySQL 性能优化 
 函数    失效索引   (explan 时key有没有值)
 or 操作中一个没有索引,另一个有索引也会失效
 like 如开头不是%_等是可以使用索引的 (BTree)
 !=使用索引的
 ,in , not in使用索引
  BTree和hash索引 is not null ,is null 是使用索引的               对b-tree来说，where xx is null条件是不会利用索引的

聚簇索引,  数据和索引放在一起,innodb引擎,ibd文件就是聚簇索引文件,
非聚簇索引,数据和索引不放在一起,myisam引擎
  
  
  
  
  
 Oracle 和 MySQL 都是B-Tree 索引
 
MySQL 用 B-tree 索引 , 找到一行要花多少次的公式
log(row_count) /log(index_block_length / 3 * 2 / (index_length + data_pointer_length)) + 1 
MySQL 中, index_block_length 通常是1024 bytes,data_pointer_length 通常是4bytes ,index_length 就是索引字段数据类型的长度
如 500,000 行数据, index_length 3 个 bytes (即MEDIUMINT) 
log(500,000)/log(1024/3*2/(3+4)) + 1 = 4 次查找  -- log是10为底的对数
索引空间要500,000 * 7 * 3/2 = 5.2MB (假设索引缓存区使用比是 2/3))

如果更新数据,要4次查找哪放索引和2次查找为更新索引,一般是缓存的除非大表 ,如为MyISAM表,参数key_buffer_size


B-Tree （并不是二叉的） 
       1.定义任意非叶子结点最多只有M个儿子；且M>2；
       2.根结点的儿子数为[2, M]；  //根最少2个
       3.除根结点以外的非叶子结点的儿子数为[M/2, M]；  //如M为3,即可以1个
       4.每个结点存放至少M/2-1（取上整）和至多M-1个关键字；（至少2个关键字）
       5.非叶子结点的关键字个数=指向儿子的指针个数-1；
	  
       6.非叶子结点的关键字：K[1], K[2], …, K[M-1]；且K[i] < K[i+1]； //当前节点的多个关键字 左小,右大
       7.非叶子结点的指针：P[1], P[2], …, P[M]；其中P[1]指向关键字小于K[1]的子树，P[M]指向关键字大于K[M-1]的子树，其它P[i]指向关键字属于(K[i-1], K[i])的子树；//叶子是左下是小的,右下是大的排序的
      
	   8.所有叶子结点位于同一层；
     

 
 Oralce 索引 
 B树索引不存储索引列全为空的记录，而BITMAP索引，则存储NULL值，
 复合索引，由于存在着多个列，如果某一个索引列不为空，那么索引就会包括这条记录，即使其他所有的所有列都是NULL值  
 
 
 
 
 switch  不能使用long 类型 ,  JDK新版本可用String
 abstract class 中可以有main方法
 

 数据库优化及SQL优化  
 设计模式 工厂有好什么好处,DAO好处
 精通缓存、消息、大数据存储相关技术    
 


 
 熟悉大流量、高并发、高性能的分布式系统的设计及应用，擅长性能调优者优先 
 精通分布式、缓存、消息、搜索等机制  

 读写分离

JMeter 压测
 
 Compare And Swap         CAS    AtomicInteger   sun.misc.Unsafe.getAndAddInt(x) ,getAndSetInt(),compareAndSwapInt.　无源码反编译都是native的 
 	地址，旧值，新值三个参数，使用地址的值和旧值比较，如相同则指新值这些操作是cpu的一个指令完成，当前系统是否为多核系统，如果是就给总线加锁，所以同一芯片上的其他处理器就暂时不能通过总线访问内存
 	如果CAS不成功，则会原地自旋 (自旋是谁做的？？？新值计算（+1）从哪来??C++？？？）
  
 机器学习基本理论及悉常见数据挖掘及机器学习算法，如回归、分类、聚类等 
 Activiti(snake)
 nginx
 Kafka  
 spark MLib
 flink 
 mysql innodb cluster
  机器学习技术和应用：Spark MLLib,GraphX


子类extends 父类 implements Serializable  //父类  没有 实现Serializable,父类中的属性不会被存储



目录中文件最大的前n个  
FileItem2 item2 =Collections.max(remList,comparator);//size比较
boolean isOK=remList.remove(item2);//使用equals(根据path)方法删



单向链表,还有一个节点是随机指向,写程序深度复制 
RandomListNode *copyRandomList(RandomListNode *head) {  
        //第一步原有的链表中每个中间增加了一个节点 ，前后链上(新的random是原来的random 可有，可无）
        RandomListNode *p = head;  
        RandomListNode *dest, *t = NULL;  
        while (p != NULL) {  
            t = new RandomListNode(p->label);  
            t->next = p->next;   
            t->random = p->random;  //方式二，t->random可没有值 
            p->next = t;  
            p = t->next; //取得源链表中的下一个结点  
        }  
       //第二步新给random赋值，根据原链表的radom->next
        p = head;  
        while (p != NULL) {  
            t = p->next;  
			   //---方式二 t->random 没有值 
				if(p->random!=NULL)
				{
					t->random=p->random->next;
				}				
				//---
            if (t->random != NULL) {  // 此处需要判断源节点的random是否为空，如果不为空才需要更新  
                t->random = t->random->next;  
            }  
            p = t->next;  
        }  
         //第三步，原链next还原和新链的next修正
        p = head;  
        dest = p->next;  
        while (p != NULL) {  
            t = p->next;  
            p->next = t->next; //源链表 还原
            p = t->next;  
            if (p != NULL) { //源链表如没有到尾
                t->next = p->next;  //新链表修改指向源的下一个（即新链表节点）  
            }  
        }  
          
        return dest;  
    }

多线程的实现方式，也包括ForkJoinPool，Callable

BASE 
Basically Available基本可用。支持分区失败(e.g. sharding碎片划分数据库)
Soft state软状态 状态可以有一段时间不同步，异步。
Eventually consistent最终一致，最终数据是一致的就可以了，而不是时时高一致。


CAP  任何分布式系统只可同时满足二点，没法三者兼顾。
Consistency(一致性), 数据一致更新，所有数据变动都是同步的
Availability(可用性), 好的响应性能
Partition tolerance(分区容错性) 可靠性




 Mysql 将同一个表中的  数据按照某种条件拆分到多台数据库（主机）上面，这种切分称之为数据的水平（横向）切分
		按照不同的表（或者Schema）来切分到不同的数据库（主机）之上，这种切可以称之为数据的垂直（纵向）切分
自定义   序列化   Externalizable
 
 
 B+Tree数据只存放在叶子节点上
 
 索引怎么实现的  Btree  B 通常认为是Balance的简称怎么存储的  
http://www.aikaiyuan.com/1809.html

  

R-tree (mysql spatial index ,MyISAM 和 InnoDB都支持 )
 lucene  原理
 

  
画 UML  图
 
 mysql5.7 新功能 Spatial Indexes
 
 
 构造器不能被继承，因此不能被重写，但可以被重载。
 
switch 只能是byte、short、char、int,enum , String 

s1 = s1 + 1;由于1是int类型，因此s1+1运算结果也是int 型,需要强制转换类型才能赋值
short s1 = 1; s1 += 1;可以正确编译
抽象类 和接口 
抽象类中可以定义构造器,而接口中不能定义构造器
象类中的成员可以是private、默认、protected、public的，而接口中的成员全都是public的,属性全是public static final
对象的持久化 能够用于对象的深度克隆

zookeeper 原理
http://cailin.iteye.com/blog/2014486/


有N 个CPU 通常使用N+1个线程  170页
 

System.out.println("理想".getBytes("UTF-8").length); //6
System.out.println("理想".getBytes("GBK").length); //4

  
大数据做报表慢 ，主从的读写分离，分区，分表，
限流 AtomicInteger 记录当前节点正在执行线程数（可能每个请求处理时间不一样），如分布式
session/redis写mysql
分布式事务 JTA ,事务是可以回滚的 , 用 Spring Nested 和 JTA 实现是依赖于数据库 （JDBC 驱XADataSource）
分布式日志  
多模块的权限控制在一个系统  Spring Security
工作流 Activiti

mysql 嵌套事务 
	start transaction 这条SQL会自动执行commit,不希望这样可以set autocommit =0

垂直拆分：原来一个表的信息，拆分到两个或者多个表中， 通过主键来进行关联。 
水平切分：把一个表的数据按照某种规则划分到不同表或数据库里。（水平拆分行，行数据拆分到不同表中）  mongodb 的 shard切片




如果一个任务由10个子任务组成，每个子任务单独执行需1小时，则在一台服务器上执行改任务需10小时。

采用分布式方案，提供10台服务器，每台服务器只负责处理一个子任务，不考虑子任务间的依赖关系，执行完这个任务只需一个小时。(这种工作模式的一个典型代表就是Hadoop的Map/Reduce分布式计算模型）
（每个节点可再做集群，不同的代码 在不同的机器）

而采用集群方案，同样提供10台服务器，每台服务器都能独立处理这个任务。假设有10个任务同时到达，10个服务器将同时工作，10小后，10个任务同时完成，这样，整身来看，还是1小时内完成一个任务！
（相同的代码在不同的机器）

取模机器数分隔不好，如加机器就难办了，一致性hash就是取模2的32次方

分布式   垂直 水平  ，就是一台机器提供多个服务 和 多台机器提供同一个服务
  
 
java.util.Date日期格式为：年月日时分秒
java.sql.Date日期格式为：年月日[只存储日期数据不存储时间数据，是专门针对sql设计]
java.sql.Time日期格式为：时分秒
java.sql.Timestamp日期格式为：年月日时分秒纳秒（毫微秒） 




该问题出现在ORACLE 数据库，其他数据库没有试过。

如果JAVA中的属性为DATE,而数据库中是DATE类型的话。mybatis会默认将JAVA中DATE属性映射到数据库的
Timestamp类型。这时就会有这样的查询
where create_time >= v_time

左边为DATE , 右边为Timestamp.类型不一致，ORACLE会隐式将小的转成大的 
where to_Timestamp(create_time) >= v_time
这样导致左边的列用到函数的。索引列上使用函数后会导致索引失效。

 //如oracle 是date 类型,使用java.sq.Date 会使用索引,而util.Date不会 !!!
 java.sql.Date 是针对SQL语句使用的，它只包含日期而没有时间部分
 
 
 
 
 
cglib代理实现原理    //通过字节码技术动态创建子类实例  



wait/yield/sleep/join方法的区别 
wait()使当前线程进入停滞状态时，还会释放当前线程所占有的“锁标志”，
yield()法只能让同优先级的线程有执行的机会



ReentrantLock  的好处是可以tryLock 加超时时间，或者事先检查 isHeldByCurrentThread（）
接口ReadWriteLock和它的实现ReentrantReadWriteLock 升级，降级

偏向锁 
JDK 8 ConcurentHashMap
CopyOnWriteArrayList 多线程时是产生多个复本吗？

多线程 组合先后执行 
hash


 
SoftReference (只当没有内存时回收),WeakReference,只要内存回收时就会被回收

Http1.1  HTTP管道的意思是 客户端送HTML和CSS请深圳市，服务端同时处理2个请求，但有要有顺序返回，先返回HTML,再CSS 。为了减少连接次数，多个图片为一个，用大的JS，或者放HTML中
Http 2.0 二进制传 ，head帧,data帧 
帧 通讯的最小单位，每个帧有帧首部 ，标识出所属流(哪个请求) 
双向数据流，乱序发送 ，最后再在另一端把它们重新组合起来，很多流可以并行地在同一个TCP连接上交换消息
		减少网络延迟，还有助于提高吞吐量
每个流都可以带有一个31比特的优先值  ，浏览器明明在等关键的CSS和JavaScript，服务器却在发送图片，从而造成渲染阻塞。
压缩首部元数据，可以针对之前的首部数据只编码发送差异数据  （首部表在 HTTP2.0的连接存续期内始终存在,）
对一个域名，只需要开启一条 TCP 连接，之前的合并 JS、CSS 文件技巧，没有效果
Chrome 等浏览器强制要求使用 HTTP/2.0 必须要用上 SSL

服务器推送，客户端还可以缓存起来，甚至可以由不同的页面共享

 
快速排序  选一个轴点，把所有小于这个节点放在左边，大于放在右边，这个轴点的数位置就确定，再相同的递归做左右两边


 http 2.0  , Servlet 4.0

Docker,kubernetes,
kafka,spark,flink,akka,HBase,Curator
spring cloud (consul,zk,etcd,config),elstic search(分布式) ,filebeat
netty,GC
mysql innodb cluster






Future(FutureTask 实现了Runable和Future) get  使用了LockSupport.park(暂停/阻塞当前线程) 使用 是用 UNSAFE.park
LockSupport.unpark 唤醒

基于Unsafe类开发 比如Netty Hadoop Kafka等


偏向锁→轻量级锁→自旋锁(CAS)→重量级锁
--- 重量级锁 
	synchronized是通过对象内部的一个叫做监视器锁（monitor）来实现的。但是监视器锁本质又是依赖于底层的操作系统的Mutex Lock来实现的
	。而操作系统实现线程之间的切换这就需要从用户态转换到核心态，这个成本非常高
 
	锁可以从偏向锁升级到轻量级锁，再升级的重量级锁（但是锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级）
	默认是开启偏向锁和轻量级锁的，我们也可以通过-XX:-UseBiasedLocking来禁用偏向锁


偏向锁    偏向锁只需要在置换ThreadID的时候依赖一次CAS原子指令，一旦出现多线程竞争的情况就必须撤销偏向锁，只有一个线程执行同步块时进一步提高性能
轻量级锁  没有多线程竞争的前提下，减少传统的重量级锁使用产生的性能消耗。 如果存在同一时间访问同一锁的情况，就会导致轻量级锁膨胀为重量级锁。 获取及释放依赖多次CAS原子指令，为了在线程交替执行同步块时提高性能，


尽管Java1.6为Synchronized做了优化，增加了从偏向锁到轻量级锁再到重量级锁的过度，但是在最终转变为重量级锁之后，性能仍然较低


好多地方用到了 CAS(应该是 AbstractQueuedSynchronized) ,尤其是java.util.concurrent包下，比如 CountDownLatch、Semaphore、ReentrantLock 中
AbstractQueuedSynchronized（AQS） 有公平锁，和不公平锁 


公平锁时，线程在尝试获取锁之前进行一次CAS运算
非公平锁时，线程在尝试获取锁之前进行两次CAS运算
线程进入队列即进入waiting状态，相当于挂起，频繁挂起与唤醒是消耗资源的行为，因此非公平锁中线程更少的挂起唤醒可以提高性能，这也是lock()默认为非公平锁的原因。
非公平锁可能会导致有些线程始终得不到执行



redis 分布式锁使用 lua保证原子性，但全局不能再执行命令

官方SETNX文档的锁方案
SETNX lock.foo <current Unix time + lock timeout + 1>如返回0表示失败,看时间是否超时(对前一个加锁的进程死了)
如0查是否>当时前,如过期(这也是一步操作,不能保证 这个和GETSET是原子的 )
GETSET lock.foo <current Unix timestamp + lock timeout + 1> 就算这个返回值是早于当前时间,取锁失败(被快的抢了),这时要等(但也已经设置了新值)
用完DEL lock.foo ,有一种情况是不知道会锁多长时间,设置锁2秒,但5秒才完事,此时就会被其它的取到锁...
------
对于复合索引:Mysql从左到右的使用索引中的字段，一个查询可以只使用索引中的一部份，但只能是最左侧部分。例如索引是key index (a,b,c). 可以支持a | a,b| a,b,c 3种组合进行查找，但不支持 b,c进行查找
create table test(
a int,
b int,
c int 
);
create index inx_test on test(a,b,c);

insert into test values(1,2,3); 
insert into test values(11,22,33);
insert into test values(10,20,30); 


 explain select  * from test where a=120 and b=10;
+----+-------------+-------+------------+------+---------------+----------+---------+-------------+------+----------+-------------+
| id | select_type | table | partitions | type | possible_keys | key      | key_len | ref         | rows | filtered | Extra       |
+----+-------------+-------+------------+------+---------------+----------+---------+-------------+------+----------+-------------+
|  1 | SIMPLE      | test  | NULL       | ref  | inx_test      | inx_test | 10      | const,const |    1 |   100.00 | Using index |
+----+-------------+-------+------------+------+---------------+----------+---------+-------------+------+----------+-------------+

显示 ref字段为两个const

 explain select  * from test where b=10 and c=10   ;
+----+-------------+-------+------------+-------+---------------+----------+---------+------+------+----------+--------------------------+
| id | select_type | table | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra                    |
+----+-------------+-------+------------+-------+---------------+----------+---------+------+------+----------+--------------------------+
|  1 | SIMPLE      | test  | NULL       | index | inx_test      | inx_test | 15      | NULL |    1 |   100.00 | Using where; Using index |
+----+-------------+-------+------------+-------+---------------+----------+---------+------+------+----------+--------------------------+
1 row in set, 1 warning (0.00 sec)

 显示 ref字段为 NULL
 
 mysql 如update where条件中没有索引字段，会锁全表
------
webflux 基于 reactive stream ，已经有flow可以实现
akka

stream().parallel();
jetcache(alibaba.可远程redis)
JWT(Json Web Token) ,Oauth2
spring session ,requestWrapper ，为何不能用继承，因要为所有容器可用


spring boot 源码


& 如前为false也会执行&后的代码


船按吨拉货，输入根吨货数量如100, 返回拆分成几个来运，如拆分成10,30,60(算法目前不确定，有复杂的算法，生产3个也不是确定的) ，总和要等于根数100,可以把其它两个（多个）合并在一起(应该也不可不合并，或者合并多个)，如结果为40(为10+30),60
要求根100可以修改，自动计算出拆分数


hystrix 关开，开，关，什么时候切换，如失败，失败数+1,达到某个阀值，打开断路器，经过一段时间是半开状态，如再请求失败再打开断路器，如再请求成功关闭断路器


redis 分片的定位方法  取模？？？一致性hash????
mysql 索引的定位方法 BTree

缓存击穿 （指数据库中有，但缓存中没有，）
缓存穿透（用户请求的数据全部不在缓存中，如id不存在的数据，数据库压力大，解决方安案id<0拦截，不存在的数据也缓存null）
缓存雪崩 （大批量缓存一起期，同时查数据库压力大，解决方案，过期时间随机范围，热点数据永不过期,config set maxmemory-policy  allkeys-lru）
雪崩 ，一个服务不可用导致一连串的服务不可用


出错的定位方法



领域驱动设计(DDD:Domain-Driven Design)   统一了分析和设计编程


HashMap为何初始容量是16（2的次方），16-1= 二进制1111要位与操作可以hash均匀分布，降低hash碰撞的几率

LinkedHashMap保存了记录的插入顺序
LRU 使用集合类 
链表每次新插入数据的时候将新数据插到链表的头部；每次缓存命中(存在)（即数据被访问），则将数据移到链表头部；那么当链表满的时候，就将链表尾部的数据丢弃。

ThreadLocal 里存在线程里的（Thread.currentThread()得到）中的一个属性threadLocals里类型为ThreadLocalMap类型
	(内部是一个初始16长度的Entry数组,如不够用会扩容的,如一个线程使用多ThreadLocal对象个使用this区分)
	值Entry类型是WeakReference， Entry构造器是把key(即threadLocal this)引用调用父类WeakReference(threadLocal是弱引用，如threadLocal=null就会被回收),value不是,没有被回收内存溢出
		线程一直在,threadLocal，一直在，如threadLocal只一个一存在的强引用， 下次再set冲前面的，即使用不调用remove()没问题 
如每次调用new ThreadLoca()，用完一定最要用remove（）


公私钥加签，验签 

分布式的消费的一致性 ,两军问题,拜占庭将军问题

Thread isInterupted()isXX只读，不清除中断状态，可多次调用返回相同结果,
		interupted()清除中断状态，第一次中断，第二次就返回非中断


MyISAM引擎使用B+Tree作为索引结构，叶节点的data域存放的是数据记录的地址
InnoDB也使用B+Tree作为索引结构，InnoDB的数据文件本身就是索引文件,叶节点data域保存了完整的数据记录,key是数据表的主键,
InnoDB要求表必须有主键,如果没有显式指定，则MySQL系统会自动选择一个可以唯一标识数据记录的列作为主键，如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键

System.out.println((double)782/4); //如不加(double)会丢失小数点的值
	
n为  1,2,3,4....n
给个m ,
求所有相加等于m的情况


CAP原则，指的是在一个分布式系统中，一致性（Consistency）、可用性（Availability）、分区容错性（Partition tolerance）

eureka是 AP(一致性弱) ，Consul，zooKeeper，etcd 都是 CP(牺牲可用性)


notify ,notifyAll


Restful字段命名（多个微服务统一，都叫user_id，可能哪天读所有系统的user_id）
防盗链refer,timestamp(token)...还有。。。
基于角色的访问控制（RBAC）是

mysql slow_query 或者 show processlist
