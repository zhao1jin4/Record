国家企业信用信息公示系统
http://www.gsxt.gov.cn/index.html
http://sh.gsxt.gov.cn/index.html  上海 

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

六大设计原则(SOLID)
Single Responsibility Principle：单一职责原则
Open Closed Principle：开闭原则
Liskov Substitution Principle：里氏替换原则
Law of Demeter：迪米特法则
Interface Segregation Principle：接口隔离原则
Dependence Inversion Principle：依赖倒置原则
 
KISS原则(Keep It Simple,Stupid，Keep It Simple And Stupid) 
	（尽量保持简单）傻瓜 
	
YAGNI 原则的英文全称是：You Ain’t Gonna Need It 你不会需要它
	不要去设计当前用不到的功能，不要去编写当前用不到的代码，不要做过度设计

DRY原则，Don’t Repeat Yourself，
关注点分离（Separation of concerns，SOC）
Don’t Make Me Think  让我一眼就知道你要干什么，不要让我去思考你在干什么


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

select使用fdset,有大小限制，
poll不使用fdset,使用poolfd链接表结构，没有数量限制
epoll  但是select和poll在“醒着”的时候要遍历整个fd集合，而epoll在“醒着”的时候只要判断一下就绪链表是否为空就行了
select和poll (每次都是全部遍历fdset)

 伪异步IO 一个线程池和任务队列去处理所有客户端的请求,当有一个客户端的读取信息非常慢时，服务器对其的写操作时会很慢，甚至会阻塞很长时间，因为线程池中的线程是有限的，当有客户端需要分配线程时，就会导致新任务在队列中一直等待阻塞的客户端释放线程

只能由一方A传到另一方B，则称为单工
由A传到B，又能由B传A，但只能由一个方向上的传输存在，称为半双工传
在任意时刻，线路上存在A到B和B到A的双向信号传输


锁升级

Java逃逸(将堆分配转化为栈分配)  TLAB(Thread Local Allocation Buffer) 以及Java对象分配 
逃逸分析


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




 Mysql 将同一个表中的  数据按照某种条件拆分到多台数据库（主机）上面，这种切分称之为数据的水平（横向）切分
		按照不同的表（或者Schema）来切分到不同的数据库（主机）之上，这种切可以称之为数据的垂直（纵向）切分
自定义   序列化   Externalizable
 
 
 B+Tree数据只存放在叶子节点上,  叶子节点之间使用链表连接,索引和数据放在一起，是即全部数据，
	索引是主键索引，没有用唯一索引，如再没有自动建立一列rowNum，如一个表多个索引呢？？
 
 Hash索引 ， 范围查询效率低，模块匹配，只能全表扫描
 
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
平均时间复杂度也是O(nlog2n)

 http 2.0  , Servlet 4.0

Docker,kubernetes,
kafka,spark,flink,akka,HBase,Curator
spring cloud (consul,zk,etcd,config),elstic search(分布式) ,filebeat
netty,GC
mysql innodb cluster






Future(FutureTask 实现了Runable和Future) get  使用了LockSupport.park(暂停/阻塞当前线程) 使用 是用 UNSAFE.park
LockSupport.unpark 唤醒

基于Unsafe类开发 比如Netty Hadoop Kafka等
CAS 基于一个汇编指令cmpxchg保证原子，但如多核CPU 会有锁总线，现在最新的还有锁北桥信号，
ABA问题，就是 一个线程修改一个变量速度很快先修改为A再修改为B又修改为A，而一个线程再做CAS时还是拿自己的A去比较，但其实已经被修改过了，但不知道

使用 AtomicReference 如引用变了就不一样了，而不是原始类型
AtomicStampedReference 带版本号，可以解决ABA问题



偏向锁→轻量级锁→自旋锁(CAS)→重量级锁
--- 重量级锁 
	synchronized是通过对象内部的一个叫做监视器锁（monitor）来实现的。但是监视器锁本质又是依赖于底层的操作系统的Mutex Lock来实现的
	。而操作系统实现线程之间的切换这就需要从用户态转换到核心态，这个成本非常高
 
	锁可以从偏向锁升级到轻量级锁，再升级的重量级锁（但是锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级）
	默认是开启偏向锁和轻量级锁的，我们也可以通过-XX:-UseBiasedLocking来禁用偏向锁,jdk15 过时


偏向锁    偏向锁只需要在置换ThreadID的时候依赖一次CAS原子指令，一旦出现多线程竞争的情况就必须撤销偏向锁，只有一个线程执行同步块时进一步提高性能
轻量级锁（自旋锁）  没有多线程竞争的前提下，减少传统的重量级锁使用产生的性能消耗。 如果存在同一时间访问同一锁的情况，就会导致轻量级锁膨胀为重量级锁。 获取及释放依赖多次CAS原子指令，为了在线程交替执行同步块时提高性能，



偏向锁的获取流程
查看Mark Word中偏向锁的标识，如是偏向锁，检查对应线程（不是当前线程）是否存在，如不存在，设置头为无锁，再用CAS设置线程
若为可偏向状态，则测试Mark Word中的线程ID是否与当前线程相同
	若不相同 当前线程通过CAS操作竞争锁，若竞争成功，则将Mark Word中线程ID设置为当前线程
	CAS竞争锁失败的情况下，说明有竞争。当到达全局安全点时之前获得偏向锁的线程被挂起，偏向锁升级为轻量级锁，


尽管Java1.6为Synchronized做了优化，增加了从偏向锁到轻量级锁再到重量级锁的过度，但是在最终转变为重量级锁之后，性能仍然较低


好多地方用到了 CAS( AbstractQueuedSynchronized) ,尤其是java.util.concurrent包下，比如 CountDownLatch、Semaphore、ReentrantLock 中
 
先进先出的阻塞队列, 
锁状态(int state)源子性即CAS ,操作状态使用这三个方法  getState,setState,compareAndSetState 
重写下面方法(可不同时支持独占和共享)
isHeldExclusively()：该线程是否正在独占资源
tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false。

AbstractOwnableSynchronizer 可以知道当前有锁的线程是哪一个，方便控制
在实现tryAcquire中 如 hasQueuedPredecessor()可以知道是否有前面排除，有值，tryAcquire返回false,来实现公平锁


ReentrantLock 
	公平锁时，线程在尝试获取锁之前进行一次CAS运算，性能好
	非公平锁时，线程在尝试获取锁之前进行两次CAS运算，性能差一点
	线程进入队列即进入waiting状态，相当于挂起，频繁挂起与唤醒是消耗资源的行为，因此非公平锁中线程更少的挂起唤醒可以提高性能，这也是ReentrantLock.lock()默认为非公平锁的原因。
	非公平锁可能会导致有些线程始终得不到执行



redis 分布式锁使用 lua保证原子性，但全局不能再执行命令

官方SETNX文档的锁方案
SETNX lock.foo <current Unix time + lock timeout + 1>如返回0表示失败,看时间是否超时(对前一个加锁的进程死了)
如0查是否>当时前,如过期(这也是一步操作,不能保证 这个和GETSET是原子的 )
GETSET lock.foo <current Unix timestamp + lock timeout + 1> 就算这个返回值是早于当前时间,取锁失败(被快的抢了),这时要等(但也已经设置了新值)
用完DEL lock.foo ,有一种情况是不知道会锁多长时间,设置锁2秒,但5秒才完事,此时就会被其它的取到锁...

--官方 分布式锁文档的方案(单实例),但set命令没说是单实例
设置key时指定超时时间(PX单位毫秒,EX单位是秒)，NX如不存在则设置，XX如存在则设置，成功返回OK,失败返回nil
SET resource_name my_random_value NX PX 30000
也存在相同问题，有一种情况是不知道会锁多长时间,设置锁2秒,但5秒才完事,此时就会被其它的取到锁???

随机值是为了安全释放锁
如要释放锁使用 lua
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end

--java 实现分布式锁 可用 redisson（Semaphore信号量的tryAcquire方法来阻塞,redis的发布、订阅来唤醒）
	 还是连接断，锁释放比较好
--etcd 分布式锁
--数据库分布式锁

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

 


& 如前为false也会执行&后的代码


hystrix 关开，开，关，什么时候切换，如失败，失败数+1,达到某个阀值，打开断路器，经过一段时间是半开状态，如再请求失败再打开断路器，如再请求成功关闭断路器


redis 分片的定位方法  按key取模？？？一致性hash????
mysql 索引的定位方法 BTree

缓存击穿 （指数据库中有，但缓存中没有，热点Key失效，数据压力大，热点数据的超时时间）
缓存穿透（用户请求的数据全部不在缓存中，数据库也没有，如id不存在的数据，数据库压力大，解决方安案id<0拦截，不存在的数据也缓存null）

缓存雪崩 （大批量缓存一起期，同时查数据库压力大，解决方案，过期时间随机范围，热点数据永不过期,config set maxmemory-policy  allkeys-lru），读写分离的策略

雪崩 ，一个服务不可用导致一连串的服务不可用

 
 

领域驱动设计(DDD:Domain-Driven Design)   统一了分析和设计编程，微服务的粒度


HashMap为何初始容量是16（2的次方），16-1= 二进制1111要位与操作可以hash均匀分布，降低hash碰撞的几率

LinkedHashMap保存了记录的插入顺序
LRU 使用集合类 
链表每次新插入数据的时候将新数据插到链表的头部；每次缓存命中(存在)（即数据被访问），则将数据移到链表头部；那么当链表满的时候，就将链表尾部的数据丢弃。

ThreadLocal 里存在线程里的（Thread.currentThread()得到）中的一个属性threadLocals里类型为ThreadLocalMap类型
	(内部是一个初始16长度的Entry数组,如不够用会扩容的,如一个线程使用多ThreadLocal对象个使用this区分)
	值Entry类型是WeakReference， Entry构造器是把key(即threadLocal this)引用调用父类WeakReference(threadLocal是弱引用，如threadLocal=null就会被回收),value不是,没有被回收内存溢出
		线程一直在,threadLocal，一直在，如threadLocal只存在一个的强引用， 下次再set冲前面的，即使用不调用remove()没问题 

如每次即每次产生新的线程 ，再销毁也没问题，里面的ThreadLocalMap也销毁
如	线程一直在,每次产生新的new ThreadLocal(),其它构造器什么也没干（只在get/set时拿当前线程)，用完一定最要用remove()


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


BASE 
Basically Available基本可用。	可以部分服务不可用，但核心服务要可用。
Soft state软状态。 				状态可以有一段时间不同步，异步，如状态为支付中。
Eventually consistent最终一致。 可以一断时间内不一致，如写主，从不能及时看到最新，但等一会即好，而不是强一致。


CAP  任何分布式系统只可同时满足二点，没法三者兼顾。
Consistency(一致性), 数据一致更新，所有数据变动都是同步的
Availability(可用性), 好的响应性能
Partition tolerance(分区容错性) 可靠性


CAP原则，指的是在一个分布式系统中，一致性（Consistency）、可用性（Availability）、分区容错性（Partition tolerance）
一致性(Consistency) (所有节点在同一时间具有相同的数据)
可用性(Availability) (保证每个请求不管成功或者失败都有响应)
分隔容忍(Partition tolerance) (系统中任意信息的丢失或失败不会影响系统的继续运作)

eureka 是 AP(一致性弱) ，Consul，zooKeeper，etcd 都是 CP(牺牲可用性)


notify ,notifyAll


Restful字段命名（多个微服务统一，都叫user_id，可能哪天读所有系统的user_id）
防盗链refer,timestamp(token)...还有。。。
基于角色的访问控制（RBAC）是

mysql slow_query 或者 show processlist



如果用公开密钥对数据进行加密，只有用对应的私有密钥才能解密；
如果用私有密钥对数据进行加密，那么只有用对应的公开密钥才能解密。
因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。

私钥加签   用Hash函数，生成信件的摘要（digest），使用私钥，对这个摘要加密，生成"数字签名"（signature）。
		   签名，附在信件下面，一起发给客户端 
公钥验签   取下数字签名，用公钥解密，得到信件的摘要。再对信件本身使用Hash函数，将得到的结果，与上一步得到的摘要进行对比
	
数字证书其实就是CA私钥加密过的网站公钥，CA公钥内置在浏览器中			
https浏览器生成随机的对称秘钥 ，如果网站生成会话秘钥，用他的私钥加密，那所有人都有公钥，所有人都能解开了。



微服务 熔断、降级
	熔断   如请求超时、后台服务无响应、后台服务异常等， 通过容断机制直接返回统一处理结果,并对下次请求进行同样处理，直到后台服务功能正常
	降级    弃卒保帅 ,保证核心服务可用
雪崩 A的链路上某个或几个被调用的子服务不可用或延迟较高，则会导致调用A服务的请求被堵住



分布式事务 二阶段 提交 

spring的循环依赖 三个缓存，先找singletonObjects，再找 earlySingletonObjects，再从singletonFactories如有getObject创建Bean实例，放在earlySingletonObjects中（还没有属性注入和init初始化）

主键ID生成方案--类snowflake（雪花算法）
https://github.com/twitter-archive/snowflake  现在没了

动态规划法  (保存以前的计算结果 ，)
	找钱算法  ,如要找14块（目前有10块2张，7块2张，2块2张），简单做法10和2张2块 ，但要3张，如何能让程序找出可用的最少张数， 即2张7块就可以满足要求
		 如要找15块（目前有100块1张，10块1张，7块10张，2块10张,1块），2张7块+1张1块（3张）  而不是4张		 10+2+2+1  难道要把所有3张可能(100块的直接不参与)都列出看有没有匹配的  
		  
贪心算法



一个表中可以有多个唯一性索引，但只能有一个主键。
主键列不允许空值，而唯一性索引列允许空值。



秒杀系统
 异步处理  
 缓存 
 
 熔断
 限流：
   
 可扩展：服务集群，服务可扩展，这样不仅可以扩大系统的并发量，还可以避免单点故障

MVCC 多版本并发控制 MySQL InnoDB中的实现主要是为了提高数据库并发性能，用更好的方式去处理读-写冲突
	为每个修改保存一个版本，版本与事务时间戳关联 快照
	
每行数据隐藏字段 db_trx_id、db_roll_pointer（回滚指针，指向这条记录的上一个版本（存储于rollback segment里））、db_row_id。

Repeatable Read隔离级别下，则是同一个事务中的第一个快照读才会创建Read View, 之后的快照读获取的都是同一个Read View，之后的查询就不会重复生成了，所以一个事务的查询结果每次都是一样的。
Read Committed 是每个快照读都会生成并获取最新的Read View；


redis 的一个键的值很大 (MB),大key,lazy-free

HashMap 容量为什么要是2^n ，原因为，数组下标为 2^n-1 ，转换为二制 全为1，在做hash 时会避免hash冲突

Hystrix 熔断一般是某个服务（下游服务）故障引起
		服务降级一般是从整体负荷考虑

zookeeper ZAB 协议作为数据一致性的算法

的强一致性 Paxos 算法,raft更好的

redis,zookeeper分布式锁的区别 
	redisre主写成功立即返回到客户端，再写从，有可能挂了就不行了，
	zookeeper主写成功写从超半数成功返回到客户端
	
	redis在有锁时也在阻塞

redis RedLock  多个节点有超过半数就可以

@MyAutowired ，和@SpringBootApplication中有@ComponentScan
  InheritableThreadLocal,线程池的threadlocal

rabbitMq 客户端挂了,死信队列很大，怎么办？？？,TTL

Netty 粘包,发送缓冲区，可能一次是多个数据包，拆包，一个包被分成两次发送
MySQL 8 的主从 replay log,不是binlog
Mycat

redis 很多的key,比如何知道一个key是否是数据中存在的，比hash更快的 
redis 布隆过滤器  BloomFilter  主要用于判断一个元素是否在一个集合中，数据库防止穿库，路由在哪个数据库
布隆过滤器 ：固定大小的二进制向量或者位图（bitmap）和一系列映射函数组成的。当有变量被加入集合时，通过 K 个映射函数将这个变量映射成位图中的 K 个点，把它们置为 1
查询某个变量的时候我们只要看看这些点 
    如果这些点有任何一个 0，则被查询变量一定不在；
    如果都是 1，则被查询变量很可能存在
只有在布隆过滤器中，才去查询缓存，如果没查询到，则穿透到db。如果不在布隆器中，则直接返回。

redis 中怎么快速定位key,Hash?

MySQL B+Tree  innodb_page_size 默认是16K

Nio 多路复用 poll,epoll,锁升级，gateway ,zuul,openfeign,aio,nio,volitale

mvcc 高可用，群集
redis keys命令很慢，用scan

数据不能丢失，kafka (acks=all),redis,mysql 

在Client端按一定规则缓存并批量发送。在这期间，如果客户端发生死机等情况，都会导致消息的丢失
系统会先将数据流写入缓存中，至于什么时候将缓存的数据写入文件中是由操作系统自行决定。 
commit再处理消息。如果在处理消息的时候异常了


Quartz  多节点，会不会重复执行，数据库，感觉乐观锁更新为正在执行，加当前节点为条件，（两节点时间一致）


线程池实现原理,包装一个Work,从阻塞队列里取任务，run里面是调用Runnable .run()方法，每个work有Lock串行执行,volatile corePoolSize

kafka  offset ,按时间设置offset ，有API的 
kafka 分区规则，写消息可传 partition id,如不传就传key使用key的hash决定哪个partition id,如也没传key首次生成随机数决定哪个partition，后面加1
kafka 一个分区被组内多成员消费

 Kafka 内部存在两种默认的分区分配策略（组）：Range 和 RoundRobin 交叉轮循分配
	Range 如果有余，则表明有的消费线程之间分配的分区不均匀，那么这个多出来的分区会给前几个消费线程处理
	partition.assignment.strategy 默认是 org.apache.kafka.clients.consumer.RangeAssignor
						org.apache.kafka.clients.consumer.RoundRobinAssignor
 
 
spring cloud gateway 限流 RequestRateLimiter，redis-rate-limiter  Redis和lua脚本实现了令牌桶的方式
令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，当桶里没有令牌可取时，则拒绝服务。

漏桶算法思路很简单，请求先进入到漏桶里，漏桶以固定的速度出水，也就是处理请求，当水加的过快，则会直接溢出，也就是拒绝请求，

consule 也可做  key/value 存储 ,configurations,健康检查
eureka 如没有心跳，默认一分钟才下线 

线程池数 占CPU比较多最大一般是cpu核数+1 ，如果IO读写比较忙，cpu核数*2，如是有很多阻塞*10

redis 	
		set可以用intset或者字典(拉链法)实现。(只有当数据全是整数值，而且数量少于512个时，才使用intset)
		zset 数据结构 数据少时，使用ziplist,ziplist占用连续内存，每项元素都是（数据+score）的方式连续存储，按照score从小到大排序,ziplist为了节省内存，每个元素占用的空间可以不同，对于大的数据（long long），就多用一些字节来存储，而对于小的数据（short），就少用一些字节来存储。因此查找的时候需要按顺序遍历。ziplist省内存但是查找效率低
					 数据多时，使用字典+跳表 ,字典用来根据数据查score，跳表用来根据score查找数据（查找效率高）
					 
					 跳表是基于一条有序单链表构造的，通过构建索引提高查找效率，空间换时间，查找方式是从最上面的链表层层往下查找，最后在最底层的链表找到对应的节点：
					 跳表查找的时间复杂度为O(log(n))。索引占用的空间复杂度为 O(n)。
					 按照区间查找数据这个操作，红黑树的效率没有跳表高
					 插入、删除时跳表只需要调整少数几个节点，红黑树需要颜色重涂和旋转，开销较大
					 
					dict的保存key/value，便于通过key(元素)获取score(分值)。
					zskiplist保存有序的元素列表
		hash就是 字典，希冲突的方法是拉链法			
		list 底层使用双向链表
		string 结构体 简单动态字符串（SDS）,记录长度
		
		redis 数据库一致，只能更新缓存，延迟更新数据库
		redis 主从同步是 aof文件？
		redis 淘汰数据  默认为  noeviction （不驱逐）,
			allkeys-lru,			 LRU( Least Recently Used) 清最老的数据
			volatile-lru (volatile对有过期时间的)
			allkeys-random,
			volatile-random,
			
			volatile-ttl（过期时间的数据集中挑选将要过期的数据淘汰）
			
			Redis 4.0 新的 LFU (Least Frequently Used) 将访问频率最少的键值对淘汰。 
		  volatile-lfu
		  allkeys-lfu 
		   惰性删除：当读/写一个已经过期的key时，会触发惰性删除策略，直接删除掉这个过期key（无法保证冷数据被及时删掉）
			定期删除：Redis会定期主动淘汰一批已过期的key（随机抽取一批key检查）
			内存淘汰机制：当前已用内存超过maxmemory限定时，触发主动清理策略
			
	redis 主从同步，主从刚刚连接的时候，进行全量同步（发送快照）；全同步结束后，进行增量同步（aof）
		首先会尝试进行增量同步，如不成功，要求从机进行全量同步
		
MQ 顺序  ，rabbitmq关闭autoack，prefetchCount=1，每次只消费一条信息，处理过后进行手工ack ,kafka 确保顺序消息发送到同一个partition
   积压，死信TTL
 	一致/丢失(ack)，send方事务
		
		
区别系列，consul,zookeeper,
	    shiro,spring security
Pod 生命周期， Pod 被调度到某节点	   ，阶段Pending，Running，Succeeded，Failed，  Unknown

	    	如果 Pod 的 Init 容器失败，kubelet 会不断地重启该 Init 容器直到该容器成功为止。 然而，如果 Pod 对应的 restartPolicy 值为 "Never"，Kubernetes 不会重新启动 Pod。
			如果为一个 Pod 指定了多个 Init 容器，这些容器会按顺序逐个运行。 每个 Init 容器必须运行成功，下一个才能够运行。当所有的 Init 容器运行完成时， Kubernetes 才会为 Pod 初始化应用容器并像平常一样运行。

如一个处理任务在5分钟还没处理完，就取消kill任务,清资源,线程启动记录Map<Thread,startime>，另一个线程



Shiro 和 spring security 的区别，shiro 可以支持非web应用 

RabbitMQ 消息可以不经过 Exchange ，直接到Queue

MySQL Innodb CLuster 的Group Replication是否能保证数据不丢失
MongoDB 索引类型,MongoDB使用B-Tree


New/Old区比是 1：2 




cgroups ,systemd 面试 
cgroups 的全称是 Linux Control Groups，属于Linux内核提供的一个特性,主要作用是限制、记录和隔离进程组（process groups）使用的物理资源（cpu、memory、IO 等）
Systemd也是对于Cgroup接口的一个封装。systemd以PID1的形式在系统启动的时候运行

flink window 窗口
jprofile ,eclipse Memory Analyziser

logback+logstash的性能是否有问题。
mysql innodb  非主键索引存在哪里


zuul 和 gateway 区别  gateway 很好的支持异步，而zuul仅支持同步,gateway对比zuul多依赖了spring-webfluxgateway对比zuul多依赖了spring-webflux

redis 数据类型的存储结构 skip table ,dict原理  
mysql 2千万数据量，性能 


http 1.1 服务器推  ，JS EventSource (看network监控 像是ajax多次请求)，Java text/event-stream,可替代websocket?


Eureka  自我保护机制,不可用， 默认1分钟才清除
spring cloud gateway 默认线程数,并发高，多了也没用，CPU有关，最大连接数 spring.cloud.gateway.httpclient.pool.max-connections 没有默认值 ，
gateway中做filter登录验证行吗，servlet记得不用请求到的

websocket可以跨域


rabbitmq 消息有序,本身基于Queue就是有顺序的

Redis  双写一致性



 

