http://doc.javanb.com 中文JDK6API
https://docs.oracle.com/en/java/javase/11/

C:\ProgramData\Oracle\Java\javapath  目录中有java,javaw

apt	(Annotation Processing Tool)工具
extcheck 工具,检查jar的冲突

javac -encoding GBK XX.java
str.getBytes("GBK")  否则使用操作系统的默认编码,linux 优先使用echo $LANG , zh_CN.GBK 其次 /etc/sysconfig/i18n 
如getBytes都没有指定字符集 linux 和 windows 则是UTF-8

javac -d . *.java
javac TestHelloClient.java -Djava.ext.dirs=WEB-INF/lib
java  -Djava.ext.dirs=WEB-INF/lib TestHelloClient
java -DmyKey=myValue 在程序中使用 System.getProperty("myKey")取得


-Djava.ext.dirs=WEB-INF/lib  不但对java有效,也对javac有效

jar -tf aa.jar  //显示列表
jar -cvfm bb.jar META-INF/MANIFEST.MF * //建立  -C 指定的建立时使用的目录

javac -g -d . MainApp.java
jdb org.MainApp

>stop at hadoop.hbase.MainHBase:27
>clear  hadoop.hbase.MainHBase:27
>clear 显示所有的断点
>run
>use src //指定源码路径
>list  //附近代码
>locals
>set a=4556
>print admin.getTableNames()
>watch MainHBase.a
>threadgroups
>threads main

>step  跳入
>step up  跳出
>next 单行
>cont        从断点处继续执行  
>!!                          - 重复执行最后一个命令  
> <n> <命令>                  - 将命令重复执行 n 次   

use 或 sourepath 查/设源码路径


==========JVM 相关

在线 Techonolgies->Java Virtual Machine ->HotSpot VM  进入->Documentation 下的 HotSpot VM Command Line Options  -> 看JDK8的windows,linux(JDK7是通用的)
http://docs.oracle.com/javase/8/   -> Reference ->  Developer Guides -> 图中最低层的(本地也有) Java HotSpot Client and Server VM

jdk-8-apidocs/docs/technotes/tools/index.html  有jstat  有含意, java 命令的 -XX 选项
jdk-8u121-docs-all\docs\technotes\guides\vm\gctuning\index.html

静态内存和动态内存
编译时就能够确定的内存就是静态内存,如int类型
Object的finalize方法只会被调用一次,这一次有可能会复活对象

----每个线程有自己的
pc Register  线程轮流切换,保证线程切换回来后，还能恢复到原先状态(每条线程都有一个独立的程序计数器)
JVM Stacks    有 栈帧（Stack Frame） (线程私有的)
Native Method Stacks  

---线程共享的 (动态内存)
非堆区包括 Metaspace(即本地化的堆内存) , Code cache 和 Compress Class Space
 
方法区(所有线程共享)移至 Metaspace ，字符串常量移至 Java Heap

(PermanetGeneration jdk8中被删 以促进HotSpot JVM与 JRockit VM的融合) Metaspace,参数 -XX:MaxMetaspaceSize=256m 限制本地内存分配给类元数据的大小,默认无限制
   
	
Heap 堆区有 (非G1)

1. new(young) Generation
	一个Eden 空间,存放新生的对象,空间不足的时候，会把存活的对象转移到Survivor中
	两个Survivor Spaces，存放每次垃圾回收后存活下来的对象 (FromSpace 和 ToSpace是对称的，没先后关系，from和to是相对的,Copying算法)
	
	Copying算法 ,存活的对象，并复制到一块新的完全未使用的空间中 对应于新生代，就是在Eden和  FromSpace或ToSpace之间  copyFromSpace或ToSpace之间copy 
	新生代采用空闲指针的方式来控制GC触发，指针指向最后一个分配的对象的位置，当有新的对象要分配内存时，用于检查空间是否足够，不够就触发GC
	minor GC后(非常频繁，一般回收速度也比较快)，Eden内存块会被清空, 活下来的对象首先由Eden拷贝到某个Survivor Space
	
2. old Generation(tenured) 生命周期长的内存对象 ,Mark算法来进行回收,回收后的空间要么进行合并，要么标记出来便于下次进行分配
		老年代内存被占满时将会触发Full GC,回收整个堆内存
		Major GC (一般会比 Minor GC慢10倍以上) / Full GC
		
Survivor Space空间满了后, 剩下的live对象就被直接拷贝到tenured Generation中去,old 满后就会有Full GC
移动到 Survivor 空间中，并将对象年龄设为 1。对象在 Survivor 区中每熬过一次 Minor GC，年龄就增加 1 岁，当它的年龄增加到一定程度（默认为 15 岁）时，就会被晋升到老年代中。对象晋升老年代的年龄阈值，可以通过参数 -XX:MaxTenuringThreshold 来设置

Concurrent Mark Sweep (CMS)  Collector 
	使用 标记—清除
	1.初始标记(STW initial mark) STW(Stop The Word),描到能够和"根对象"直接关联的对象,暂停了整个JVM，但是很快就完成了
    2.并发标记(Concurrent marking)
    3.并发预清理(Concurrent precleaning)
    4.重新标记(STW remark)
    5.并发清理(Concurrent sweeping)
    6.并发重置(Concurrent reset) 
		

	
	留一部分内存空间提供并发收集时的程序运作使用。在默认设置下，CMS收集器在老年代使用了x%的空间时就会被激活，
	也可以通过参数-XX:CMSInitiatingOccupancyFraction 的值来提供触发百分比,默认-1 表示使用 -XX:CMSTriggerRatio,默认80%
	MinHeapFreeRatio  默认40%



jvm垃圾回收算法
	1.引用计数法:两个对象A和B，互相引用无法回收,老的
	
	2.搜索方法  GC Roots  从这些节点开始往下搜索，搜索通过的路径成为引用链（Reference Chain），当一个对象没有被GC Roots的引用链连接的时候，说明这个对象是不可用
		a) 虚拟机栈（栈帧中的本地变量表）中的引用的对象。局部变量
		b) 方法区域(Metaspace)中的类静态属性引用的对象。static
		c) 方法区域(Metaspace)中常量引用的对象。final
		d) 本地方法栈中JNI（Native方法）的引用的对象。
	3.标记—清除算法(Mark-Sweep)  产生大量的不连续空间
	4.复制算法(Copying) 			内存分成大小相等的两块，每次使用其中一块,把存活的对象复制到另一块上，然后把这块内存整个清理掉。内存的利用率不高,收集新生代 
	5.标记—整理算法(Mark-Compact) 是把存活对象往内存的一端移动，然后直接回收边界以外的内存。  内存的利用率，并且它适合在收集对象存活时间较长的老年代。
	 
 
逃逸分析   所有的对象都分配在堆上也渐渐变得不是那么“绝对”了。
线程共享的Java堆中可能划分出多个线程私有的分配缓冲区 Thread Local Allocation Block=TLAB 
	
jconsole 命令可以查看JVM 的性能　监控

set JAVA_OPTS=-Xss256K -Xms256m -Xmx1024m   -XX:NewSize=128m -XX:MaxNewSize=256m -XX:SurvivorRatio=8 -XX:NewRatio=2
	
 
G1收集器  JDK9的server默认
	heap被平均分成若干个相同大小的区域(region),可以不连续，每块区域可能是Old区，也有可能是yong区
	
	(每个heap区都是逻辑上连续的一段内存,virtual memory)  ,角色(eden, survivor, old), 但每个角色的区域个数都不是固定的
	
	Eden空间中，每一个线程都有一个固定的分区用于分配对象,即一个 TLAB.分配对象时，线程之间不再需要进行任何的同步。
	如果Eden空间无法容纳该对象，就只能在老年代中进行分配空间

    Young GC和Mixed GC，两种都是Stop The World(STW)的,还有FullGC
	server-style,目标多处理器,大内存(超过6G或更大),有GC暂停(0.5秒以下),大吞吐量  ,将来替代CMS
	
	是压缩,紧凑,致密(Compact)的, 根据配置的暂停时间确定回收区域数,压缩从一个或多个区域复制另一个单个区域

	G1跟踪各个Region获得其收集价值大小，在后台维护一个优先列表； 每次根据允许的收集时间，优先回收价值最大的Region（名称Garbage-First的由来）


	CMS 不做整理,G1并发整理是对整个heap
	
	一个对象大于分区(region)一半 ,认为是一个巨大对象,直接分配到老年代的巨大对象区,如果装不下，会找这些区是续的巨大对象区,有时候不得不启动Full GC 
	
	-XX:G1HeapRegionSize 设置每个Region大小 
	-XX:MaxGCPauseMillis
	G1 保证“每次GC停顿时间不会过长”的方式，是“每次只清理一部分而不是全部的Region”的增量式清理。那独立清理某个Region时 , 就需要有RememberSet来记录Region之间的对象引用关系， 这样就能依赖它来辅助计算对象的存活性而不用扫描全堆， RS通常占了整个Heap的20%或更高。

不算维护RemeberSet
	 1.初始标记(STW initial mark)  
    2.并发标记(Concurrent marking)
	 3.最终标记 同CMS重新标记，也要STW，修正RemeberSet
	 4.筛选回收
	 
----ZGC jdk11 experimental  
	https://wiki.openjdk.java.net/display/zgc/Main	
	
	STW的阶段 不包括GC堆里的对象指针，所以这个暂停就不会随着GC堆的大小而变化
	
	着色指针( colored pointers) 指针的64位使用了4位： finalizable， remap， mark0和mark1
	加载屏障(load barriers)
	
	ZGC没分代
	
	ZGC将堆划分为Region作为清理，移动，以及并行GC线程工作分配的单位。不过G1一开始就把堆划分成固定大小的Region，而ZGC 可以有2MB，32MB，N× 2MB 三种 
	256k以下的对象分配在Small Page， 4M以下对象在Medium Page，以上在Large Page。

	ZGC是Mark-Compact ，会将活着的对象都移动到另一个Region，整个回收掉原来的Region。
	
	

-----Epsilon GC 也是 jdk11 experimental 的 No-Op GC





 
===所有JVM标准实现选项
-agentlib:hprof=help  ( Heap and CPU Profiling Agent (JVMTI Demonstration Code))
-agentlib:hprof=cpu=samples,interval=20,depth=3   彩样CPU信息每20秒,栈深3,生成java.hprof.txt文件 ,参考java.lang.instrument 和 JVMTI

-agentlib		-agentpath
-Dproperty=value
-d32   -d64 
-disableassertions   -da
-disablesystemassertions -dsa
-enableassertions -ea
-enablesystemassertions -esa
-? -help
-jar
-javaagent:jarpath[=options]  参考java.lang.instrument 
-jre-restrict-search | -no-jre-restrict-search  在-help中提示未来版本移除
-server  64位JDK只持这个,-client 没用
-version  -showversion
-splash:imgname
-verbose:class   显示每个加载类
-verbose:gc	显示每个gc事件
-verbose:jni  显示使用的jni

===非标准 -X 用于 HotSpot  VM  对JDK8版本中有
-X 显示所有非标准的help
-Xbatch  同 -XX:-BackgroundCompilation 前台compilation,禁用后台compilation 默认是-XX:+BackgroundCompilation
-Xbootclasspath:path  
-Xbootclasspath/a:path    /a=append
-Xbootclasspath/p:path	  /p=prepend
-Xcheck:jni
-Xcomp			-X没有,但doc上有 ,第一次调用也编译,默认是-server是解释执行1万次方法才编译的,使用-XX:CompileThreshold修改次数
-Xdebug 没用,只为向前兼容
-Xdiag
-Xfuture 严格类文件检查,鼓励开启,未来默认开启
-Xint 解释运行,Compilation 到本代码被禁用,just in time (JIT) 编译器不会参与,会对频繁的被执,编译字节码为本地更高效的机器码
-Xinternalversion  -X没有,但doc上有
-Xloggc:filename  写到文件内容同-verbose:gc,会覆盖-verbose:gc
-Xmaxjitcodesize=size   -X没有,但doc上有,但测试不行, 为JTI编译指定最大 code cache size (in bytes),结尾可以有k,m,g,默认240 MB ,如配置-XX:-TieredCompilation,默认变为48 MB,同-XX:ReservedCodeCacheSize
-Xmixed  混合执行,非热方法用解释执行,热方法用编译执行,默认的
-Xnoclassgc       禁用类的GC,节省GC时间,有短的中止,容易出内存异常
-Xprof  不应在生产上使用,在标准输出显示信息,有Interpreted 和 native 的执行次数,在哪个Method上
-Xrs              减少 Java/VM 对操作系统信号的使用 ,没有thread dump产生 ,Shutdown hooks 
-Xshare:off       不尝试使用共享类数据(class data sharing (CDS) mode,有档上说64位VM,off是默认的)
-Xshare:auto      在可能的情况下使用共享类数据 (默认)
-Xshare:on        要求使用共享类数据, 否则将失败。
-Xshare:dump 	  文档上有dump,但java -X中没有,测试有,生成CDS归档
-XshowSettings:all
-XshowSettings:local
-XshowSettings:properties
-XshowSettings:vm
-Xusealtsigs  JVM内部信号,使用内部信号代替 SIGUSR1 和 SIGUSR2 ,同  -XX:+UseAltSigs 默认禁用
-Xverify:remote		验证class 字节,所有不被bootstrap 加载的字节,默认方式
-Xverify:all		验证所有的字节
-Xverify:none		不验证所有的字节


-Xss1m 表示栈最大空间 (thread stack size)	StackOverflowError  递归的层次太深, 同-XX:ThreadStackSize
-Xmx参数设置堆内存的最大值,字节, 要是1024的倍数,可用k,K,m,M,g,G  -XX:MaxHeapSize //Runtime.getRuntime().maxMemory(); 可取到
-Xms参数设置堆内存的最初始,字节,要是1024的倍数   -XX:InitialHeapSize  -XX:MinHeapSize
-Xmn参数设置堆内存的 新生代大小 等同 -XX:NewSize  


===运行时选项 -XX 控制HotSpot VM
-XX: +Param表示启用,-Param表示禁用,Param=value表示修改值,如数值可用k,m,g
-====== Behavioral  动作的组
-XX:+AllowUserSignalHandlers  启用signal handlers ,可以程序中捕获信号,默认是禁用的
-XX:-DisableExplicitGC		禁止在运行期显式地调用 System.gc(),默认是启用的
-XX:+FailOverToOldVerifier	新的Class校验器检查失败，则使用老的校验器,默认禁用的
-XX:-RelaxAccessControlCheck  默认是禁用的,如果用老版本字节码可以启用
-XX:+ScavengeBeforeFullGC     默认启用,不建议禁用,在做full GC 前做yong generation GC   ,  scavenge打扫
-XX:+UseBoundThreads		  只对Solaris有效,绑定用户级线程到内核线程
-XX:+UseConcMarkSweepGC    	  9.0 过时选项　默认禁用,为old generation 使用(CMS)收集,如-XX:+UseParallelGC 达不到效果使用这个,还可使用-XX:+UseG1GC,
								如启用 -XX:+UseParNewGC 也自动设置,不要禁用UseParNewGC,
-XX:+UseGCOverheadLimit		 限制GC的运行时间.如果GC耗时过长,就抛OutOfMemory,并行GC如果98%的时间在收集,少于2%的堆也抛OutOfMemory
-XX:+UseLWPSynchronization   只对Solaris,LWP-based 代替线程的同步
-XX:+UseParallelGC			 默认禁用,收集器是自动选择的,如启用-XX:+UseParallelOldGC 也会自动启用
-XX:+UseSerialGC  			 默认禁用,收集器是自动选择的,用于小应用 
-XX:+UseTLAB				默认启用在 young generation 区中启用线程本地分配块((TLABs=thread-local allocation blocks )
-XX:+UseThreadPriorities    使用本地线程优先级
-XX:+UseVMInterruptibleIO   只对Solaris



---(G1)  Garbage First 
-XX:+UseG1GC   					Garbage-First(G1) ,推荐堆6G或更大,暂停时间小于0.5秒
-XX:MaxGCPauseMillis=n 			最大暂停时间,默认没有最大
-XX:InitiatingHeapOccupancyPercent=n   默认45%,开始并发GC cycle
-XX:NewRatio=n
-XX:SurvivorRatio=n
-XX:MaxTenuringThreshold=n 默认15,在Survivor中存活15次(age)后,进入old generation
-XX:ParallelGCThreads=n
-XX:ConcGCThreads=n
-XX:G1ReservePercent=n    默认10 , 保留10%的空间,防止失败
-XX:G1HeapRegionSize=n    默认区的大小(统一) ,只可是2的次方，范围从1MB - 32MB , 默认根据堆大小分成约 2048 个regions
-XX:G1NewSizePercent=  experimental flag ，The default value is 5 percent 


---性能
-XX:+AggressiveOpts				性能优化,建议打开,未来默认
-XX:CompileThreshold=10000		调用多少次,编译成本地程序
-XX:LargePageSizeInBytes=4m	    对Solaris
-XX:MaxHeapFreeRatio=70			GC后，如果发现空闲堆内存占到整个预估上限值的70%，则收缩预估上限值。
-XX:MinHeapFreeRatio=40			GC后，如果发现空闲堆内存占到整个预估上限值的40%，则增大上限值 回收后堆最小可用百分比
-XX:MaxNewSize=size 			
-XX:NewRatio=2					新生代和年老代的堆内存占用比例 Ratio of old/new generation size   (名是分母)
-XX:NewSize=2m					同-Xmn
-XX:ReservedCodeCacheSize=32m	设置代码缓存的最大值 同 -Xmaxjitcodesize=size
-XX:SurvivorRatio=8  			Eden与Survivor的占用比例  Ratio of eden/survivor space size  (名是分母)
-XX:TargetSurvivorRatio=50		期望使用的survivor空间大小占比。默认是50%(拉圾回收)
-XX:ThreadStackSize=512			线程堆栈大小(KB) 同 -Xss (byte)
-XX:+UseBiasedLocking			调优	偏向锁只能在单线程下起作用,它提高了单线程访问同步资源的性能。
								标准的轻量级锁,默认开启
-XX:+UseFastAccessorMethods		优化原始类型的getter方法性能
-XX:+UseLargePages				默认禁用
   
-XX:AllocatePrefetchLines=5 	生成JIT 编码代码 默认1 只server VM
-XX:AllocatePrefetchStyle=1 	0 不生成 2 使用thread-local allocation block (TLAB)
-XX:+OptimizeStringConcat		sever VM 支持,默认启用,像是字串相加转为StringBuilder

---调试


-XX:-CITime						打印JIT编译器编译耗时
-XX:ErrorFile=./hs_err_pid<pid>.log  默认文件名在当前目录,可用  %p 表示 process,如果由于空间权限问题就放在/tmp目录中
-XX:-ExtendedDTraceProbes		只对Solaris 启用dtrace诊断,默认禁用
-XX:HeapDumpPath=./java_pid<pid>.hprof 　　,默认是java进程启动位置(Heap PROFling)
-XX:-HeapDumpOnOutOfMemoryError	 在OutOfMemory时，输出一个dump.core文件
-XX:OnError="<cmd args>;<cmd args>"
-XX:OnOutOfMemoryError="<cmd args>;<cmd args>"
-XX:-PrintClassHistogram		同jmap -histo <pid> 工具,或者 jcmd <pid> GC.class_histogram 
-XX:-PrintConcurrentLocks		同 jstack –l <pid>
-XX:-PrintCommandLineFlags		显示这个JVM所有配置的-XX:+ 选项
-XX:-PrintCompilation			当方法被编译就打印消息到控制台
-XX:-PrintGC				跟踪参数 
-XX:-PrintGCDetails
-XX:-PrintGCTimeStamps
-XX:-PrintTenuringDistribution		 打印对象的存活期限信息
								age 1:是从eden 到 survivor 1
								age 2:是从 survivor 1 到 survivor 2
								上次MinorGC后还存活的对象在这次MinorGC年龄都增加了1

-XX:-TraceClassLoading				打印class装载信息到stdout
-XX:-TraceClassLoadingPreorder		按class的引用/依赖顺序打印类装载信息到stdout
-XX:-TraceClassResolution			打印常量池,但结果显示很多
-XX:-TraceClassUnloading
-XX:-TraceLoaderConstraints
-XX:+PerfDataSaveToFile				 hsperfdata_<pid>, (正常)退出时保存jstat 二进制数据, 用jstat -class  file:///c:/tmp/hsperfdata_15276 , jstat -gc file:///c:/tmp/hsperfdata_15276
-XX:ParallelGCThreads=n
-XX:+UseCompressedOops			默认启用,compressed pointers ,当堆区小32 GB时使用32位指针代替64位指针,性能提升
								如果大于32GB也可能使用32位指针  

-XX:ObjectAlignmentInBytes 		默认8 ,取值必须是2的次方,最大256,可能会压缩指针，堆内存大小限制为4GB * ObjectAlignmentInBytes,
-XX:+AlwaysPreTouch				进入main方法前,在JVM初始化之前touch 每个页
-XX:AllocatePrefetchDistance=size  (in bytes) 只sever ,默认-1

-XX:+Inline  					默认启用
-XX:InlineSmallCode=size		默认1000(byte) 最大的代码空间(对编译方法应该inlined,)
-XX:MaxInlineSize=size			默认35(byte) ,不是编译方法
-XX:FreqInlineSize=n			频繁执行的方法做inlined,最大byte空间

-XX:LoopUnrollLimit=n
-XX:InitialTenuringThreshold=7  并行年轻代收集器,在年轻代存活次数
-Xloggc:gc.log    				输出到日志文件
-XX:-UseGCLogFileRotation  		当 -Xloggc开启时,gc日志做旋转
-XX:GCLogFileSize=8K			文件>=8K 时gc日志做旋转

--------
-verbose:gc  跟踪参数  使用jconsole等监视工具更直接

-XX:+PrintHeapAtGC	文档没有,有-XX:+G1PrintHeapRegions
-XX:+PrintGCApplicationStoppedTime
 
 
-XX:+CheckEndorsedAndExtDirs    检查 java.ext.dirs 或者 java.endorsed.dirs 变量, 	lib/endorsed目录存在并不为空 , 	lib/ext 目录有jar包
-XX:+DisableAttachMechanism   默认是禁用的,即能使用像jcmd,jstack,jmap,jinfo,设置后不能使用
-XX:-FlightRecorder
-XX:FlightRecorderOptions=parameter=value




-XX:+UseParallelGC   是 -server 默认 (64位 JDK 只支持 -server,也是隐式)会自动打开-XX:+UseParallelOldGC 
	 XX:MaxGCPauseMillis=<N>. 
	 -XX:GCTimeRatio=<N>, which sets the ratio of garbage collection time to application time to 1 / (1 + <N>).
	   默认 99, resulting in a goal of 1% of the time in garbage collection.
	 -XX:ParallelGCThreads=<N>
	 YoungGenerationSizeIncrement=<Y> for the young generation 
	 -XX:TenuredGenerationSizeIncrement=<T>  for the tenured generation


	 
-XX:ParallelGCThreads=n
-XX:-UseParallelOldGC  Enabling this option automatically sets -XX:+UseParallelGC
-XX:-UseSerialGC		使用串行垃圾收集器
-XX:+UseConcMarkSweepGC 老生代采用CMS收集器

-XX:+UseSpinning (新版本去除了)  多线程安全Lock,较短的时间内又必须重新调度回原线程的,线程进入OS互斥前，自旋一定的次数来检测锁的释放
-XX:PreBlockSpin=10
 
-XX:+UseThreadPriorities	使用本地线程的优先级


-XX:-RelaxAccessControlCheck  	Relax the access control checks in the verifier.
 
-XX:CompileCommand=exclude,java/lang/String.indexOf  把指定的方法做编译
-XX:+PrintCompilation 
-XX:+UnlockDiagnosticVMOptions  -XX:+LogCompilation (必须打开UnlockDiagnosticVMOptions)  默认目前目录下文件名hotspot_pid<nnn>.log -XX:LogFile 来修改文件名

Minor GC主要负责收集Young Generation
	因为-Xmn=10M,默认-XX:SurvivorRatio=8 ，则eden的空间大小为8M，当eden所有对象总共大小超过8M的时候就会触发Minor gc.
	如果一个对象eden大小,直接分配到了Old generation
Minor GC会把Eden中的所有活的对象都移到Survivor区域中，如果Survivor区中放不下，那么剩下的活的对象就被移到Old generation 中。

Full GC （或Major GC）对所有内存都做GC

jdk 10 对docker容器运行java 的改善, -XX:-UseContainerSupport 
 -XX:ActiveProcessorCount=count  使用CUP数
 jdk 10 的 并行 Full GC (G1) 
 
 
得到锁的顺序,偏向锁->轻量级锁->自旋锁->OS 互斥锁

-XX:+DoEscapeAnalysis  默认开启 only hotspot JVM

--------性能监视工具



jvisualvm  界面工具

JDK8 的jmc (Java Mission Control)->"飞行记录器"(即JFR)->提示对要监控的JVM要加参数  -XX:+UnlockCommercialFeatures -XX:+FlightRecorder 
记录器会在一段时间内做记录(一分钟),保存到 %HOMEPATH%\.jmc\5.3.0\xxx.jfr ,用于事后查看
JDK11去除jmc要单独下载


jps 命令显示所有Java进程的ID号 和 类名 ,像ps
jps 返回vmid 为了获得更好的效果，采用 -Dcom.sun.management.jmxremote 属性集启动 Java 进程(JDK 1.5 加, 1.6 )


jps  有VMID号
jps -v 输出虚似机进程启动时JVM参数 如有加-Xmx是看到的
jps -l 输出主类名
jps -m 传给主类的参数


jinfo  -sysprops VMID
jinfo  -flags VMID  显示这个VM的所有非默认的-XX选项,及启动的-X选项

jinfo -flag MaxHeapSize VMID
jinfo -flag InitialHeapSize  VMID
jinfo -flag MinHeapSize VMID (JDK8没有)
jinfo -flag MaxNewSize VMID	 	单位是byte
jinfo -flag NewSize VMID


jinfo -flag MaxDirectMemorySize   [进程ID]     (HotSpot VM参数,对nio分配置内存缓冲区)

jinfo -flag SurvivorRatio VMID
	-XX:SurvivorRatio=8
jinfo -flag NewRatio VMID
	-XX:NewRatio=2
 
 


jstat -options 显示所有可用选项 (可查API)
	S=survivor
	C=capacity
	E=Eden
	O=Old
	Y=young
	F=Full
	GC
	T=time
	L=last 
-t	加第一列显示自JVM启动后的时间
-h3 每三行显示一下标题
-J-Xms48m -J-Xmx64m  修改JVM加选项

jstat -gc [进程ID] 250 10					//每250毫秒一次,共10次,读gc的信息
	S0C Current survivor space 0 capacity (KB).
	S1C
    S0U : Survivor space 0 utilization (KB).
	S1U
	EC 	Current eden space capacity (KB).
	EU 	Eden space utilization (KB).
	OC 	Current old space capacity (KB).
	OU 	Old space utilization (KB).
	MC: Metaspace capacity (KB).
	MU: Metacspace utilization (KB).
	YGC Number of young generation GC Events.
	YGCT Young generation garbage collection time.
	FGC Number of full GC events.
	FGCT Full garbage collection time.
	GCT Total garbage collection time.

jstat -gcutil [进程号]  间隔毫秒  总数
	S0  — Heap上的 Survivor space 0 区已使用空间的百分比
	S1  — Heap上的 Survivor space 1 区已使用空间的百分比
	E   — Heap上的 Eden space 区已使用空间的百分比
	O   — Heap上的 Old space 区已使用空间的百分比
	M: Metaspace utilization as a percentage of the space' s current capacity.  '
	YGC — 从应用程序启动到采样时发生 Young GC 的次数
	YGCT– 从应用程序启动到采样时 Young GC 所用的时间(单位秒)
	FGC — 从应用程序启动到采样时发生 Full GC 的次数
	FGCT– 从应用程序启动到采样时 Full GC 所用的时间(单位秒)
	GCT — 从应用程序启动到采样时用于垃圾回收的总时间(单位秒)

jstat -class  [进程号]  间隔毫秒  总数   //看这个Java进程产生的类数
jstat -compiler pid:显示VM实时编译的数量等信息。
jstat -gccapacity   对象的使用和占用大小，

jstat –gccapacity VMID 	单位是KB   
	NGCMN Minimum new generation capacity (KB). 
	NGCMX Maximum new generation capacity (KB). 
	NGC: Current new generation capacity (KB).

	OGCMN Minimum old generation capacity (KB). 
	OGCMX Maximum old generation capacity (KB). 
	OC: Current old space capacity (KB).

	MCMN: Minimum metaspace capacity (KB).
	MCMX: Maximum metaspace capacity (KB).
	MC: Metaspace capacity (KB).
 

jstat -gcnew pid: new对象的信息
jstat -gcnewcapacity pid: new对象的信息及其占用量
jstat -gcold pid: old对象的信息。
jstat -gcoldcapacity pid:old对象的信息及其占用量
jstat -gcpermcapacity pid: perm对象的信息及其占用量。
jstat -printcompilation pid:当前VM执行的信息
jstat -gccause 


jcmd -l 类似jps,但显示类的路径
jcmd <PID> help 可以用help 查所有的命令
jcmd <pid> GC.class_histogram  显示目前类的实例数和使用空间

JFR.stop			FLR结束录制，jcmd 7824  JFR.stop recording=1
JFR.start			FLR开始录制，提示 Use JFR.dump recording=1 filename=FILEPATH to copy recording data to file (可用jmc打开来看)
JFR.dump
JFR.check			显示正在录制的recording值
VM.native_memory
VM.check_commercial_features   查是否开启FLR
VM.unlock_commercial_features  运行时开启FLR
ManagementAgent.stop
ManagementAgent.start_local
ManagementAgent.start
GC.rotate_log
Thread.print		查看 JVM 的Thread
GC.class_stats
GC.class_histogram
GC.heap_dump		可带文件名参数(MemoryAnalyzer来看吧)，如果只指定文件名，默认会生成在启动 JVM 的目录里，可绝对路径。
GC.run_finalization
GC.run
VM.uptime				查看 JVM 的启动时长
VM.flags  				查看 -X 和 -XX 的参数信息
VM.system_properties  	查看 JVM 的属性信息
VM.command_line 		查看 JVM 的启动命令行
VM.version 				查看 JVM 版本

解锁商业特性,可与 Java Flight Recorder (JFR)一起使用


jcmd <pid | main class> <command ...|PerfCounter.print|-f file>  
对-f 每个命令必须写在单独的一行。以"#"开头的行会被忽略,	stop 命令




jstack -- 如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack和native stack的信息,只有Linux/Unix

ps -mp <pid> -o THREAD,tid,time 命令查看该进程的线程情况 (-m 显示所有的线程 -p pid 进程使用cpu的时间)
jstack -l 进程ID (-l 看synchronizer lock) //查看,显示每个线程，有十六进制的tid,可看到线程正在执行代码堆栈


Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
//      maps.keySet();
Set<Map.Entry<Thread, StackTraceElement[]>> set=maps.entrySet();
//      set.iterator();
for(Map.Entry<Thread, StackTraceElement[]> entry: set)
{
	Thread thread=entry.getKey();
	System.out.println("Thread id:"+thread.getId()+",name:"+thread.getName()+",status:"+thread.getState());
	for(StackTraceElement ele: entry.getValue())
		System.out.println("\t"+ele);
}

jinfo -flag MaxNewSize 进程ID //可修改,查看进程的JVM参数
显示-XX:MaxNewSize= 
 


[+|-]<name>    to enable or disable the named VM flag
jinfo 修改有错误


jmap -histo 进程ID  //(histo=histogram柱状图)以文本的形式显示现在所有的类,的实例数和占用空间
jmap -dump:format=b,file=java_pid.hprof <进程ID> //(b是binary的缩写)进程的内存heap输出到heap.bin文件中,二进制文件  


JDK9中去了除了jhat
	//如文件过大,机器可用内存 可能 要大于文件大小
	jhat -J-mx768m -port <端口号:默认为7000> java_pid.hprof
	jhat java_pid.hprof 分析jmap导出的文件,启动服务 HTTP端口7000,http://localhost:7000/ ,界面中会按包名分类  

使用eclipse插件 MemoryAnalyzer分析jmap导出文件,插件认.hprof格式文件
eclipse性能测试插件 TPTP

java进程异常终止进产生 JavaCore 文件是关于CPU的　和　HeapDump(.hprof)文件是关于内存的


jstatd.all.policy文件内容	
	grant codebase "file:${java.home}/../lib/tools.jar" {
	 permission java.security.AllPermission;
	}; 
jstatd -J-Djava.security.policy=jstatd.all.policy    默认rmiregistry的1099端口 -p1099

远程启动jstatd(rmi协议)后,才能使用jvisualvm来连接

------------------jad反编译器使用,jdk7新特性目录不能很好的反编译
  jad -o -r -sjava -dsrc tree/**/*.class
UNIX要
  jad -o -r -sjava -dsrc 'tree/**/*.class'
  
 -s <ext> - output file extension (by default '.jad')
  -o       - overwrite output files without confirmation (default: off)
 -r       - restore package directory structrure
 -d <dir> - directory for output files (will be created when necessary)
  
反编译器 JD-GUI-1.6.6
JD-Eclipse-2.0.0
------------------
------------------JDBC

sun.jdbc.odbc.JdbcOdbcDriver
jdbc:odbc:db

org.h2.jdbcx.JdbcDataSource
org.h2.Driver
方式1: jdbc:h2:tcp://localhost:9092/test
方式2: jdbc:h2:tcp://localhost/~/test
方式3: jdbc:h2:mem


com.microsoft.sqlserver.jdbc.SQLServerDriver  //2005新的
jdbc:sqlserver://localhost:1433;databaseName=mydb;
 

Class.forName("com.ibm.db2.jcc.DB2Driver"); 
String remoteDB2Url ="jdbc:db2://10.1.5.226:8000/sample";
Connection con=DriverManager.getConnection(remoteDB2Url,"db2instl","123");


Oracle AL32UTF8 varchar2如中文在数据中占用三个字节,nvarchar2中文是两个字节
oracle.jdbc.xa.client.OracleXADataSource
oracle.jdbc.driver.OracleDriver
jdbc:oracle:thin:@127.0.0.1:1521:orcl    对  SID
jdbc:oracle:thin:@//127.0.0.1:1521/orcl   对  service Name


//  jdbc:postgresql://host:port/database
jdbc:postgresql://localhost:5432/test?user=fred&password=secret&ssl=true&currentSchema=public
Class.forName("org.postgresql.Driver");


jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword
Class.forName("org.mariadb.jdbc.Driver") 



<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId> 
	<version>8.0.15</version> <!--5.1.45, 8.0.15 -->
</dependency>
依赖于 protobuf-java 3.6.1
com.mysql.jdbc.jdbc2.optional.MysqlXADataSource //MySQL 5.x
com.mysql.cj.jdbc.MysqlXADataSource //MySQL 8

com.mysql.jdbc.Driver  //MySQL 5.x
com.mysql.cj.jdbc.Driver //MySQL 8

jdbc:mysql://localhost:3306/databasename
jdbc:mysql:///mydb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC    xml文件中用&amp;
jdbc:mysql://address=(protocol=tcp)(host=localhost)(port=3306)/mydb?useUnicode=true&characterEncoding=UTF-8
&zeroDateTimeBehavior=convertToNull 对于日期类型,如果从文件导入没有值会被认为0000-00-00,
&zeroDateTimeBehavior=CONVERT_TO_NULL (MySQL8 CONVERT_TO_NULL)
emptyStringsConvertToZero 默认是true
useSSL=true

connectTimeout  milliseconds

"jdbc:mysql:loadbalance://" +
        "localhost:3306,localhost:3310/test?" +
        "loadBalanceConnectionGroup=first&ha.enableJMX=true"


Properties props = new Properties(); 
// We want this for failover on the slaves
props.put("autoReconnect", "true"); 
// We want to load balance between the slaves
props.put("roundRobinLoadBalance", "true"); 
props.put("user", "foo");
props.put("password", "password");
Connection conn = DriverManager.getConnection("jdbc:mysql:replication://master,slave1,slave2,slave3/test",props);
//jdbc:mysql:replication://address=(type=master)(host=master1host),address=(type=master)(host=master2host),address=(type=slave)(host=slave1host)/database

	

//MySQL JDBC Driver可不指定DB
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&amp;characterEncoding=UTF-8","root","root");
Statement state=con.createStatement();
state.executeUpdate("use mydb");
PreparedStatement prepare=con.prepareStatement("select * from mydb.student");//也可直接查
ResultSet rs=prepare.executeQuery();


prepare.setString(1,1001);//列索引以1开始
resultSet.getString(1);//列索引以1开始

 
//MySQL 与 JDBC 支持的完全一致
con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);//Oracle 默认
con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);//MySQL 默认 
con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//Oracle 只支持  READ COMMITTED  和 SERIALIZABLE; 

MySQL XdevApI 见Java_Third_Lib
 
 

META-INF/MANIFEST.MF 文件中的Main函数不会找classpath环境变量要用Class-Path：
:后一定要有一个空格,:前不能有空格, 多个jar包用空格分隔,可以把jar包放在目录下也可以放在根下
引用的第三方的jar包只能放在本jar包外面( eclipse copy 选项)

Main-Class: org.zh.TestSWT
Class-Path: . lib/swt.jar mysql.jar

jar包里包含jar包,不行的,必须用eclipse->export->runnable jar->package required libraries
Rsrc-Main-Class: my.Base64SwingTextArea
Main-Class: org.eclipse.jdt.internal.jarinjarloader.JarRsrcLoader

eclipse 也可以把第三方jar包中class解包放到自己的jar中( extract 选项)

jar -m Manifest文件

wsimport -s c:/tmp/ws_code -p org.zh.ws -keep  http://localhost:8000/helloWorld?wsdl 生成webservice代码
JDK 11 去除了 wsimport
-----------------远程 调试
作为调试服务器的目标 VM
	-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8765        8453
作为调试客户机的目标 VM
	-Xdebug -Xrunjdwp:transport=dt_socket,address=127.0.0.1:8000

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000 -jar test.jar 	启动后等待连接
java -agentlib:jdwp=transport=dt_socket,server=y,address=8000 -jar test.jar  	要JDK5
会加载 jdwp.so 动态库文件
Java Debug Wire Protocol (JDWP) 

	
eclipse->debug configration...->可以建立 Remote Java Application, 
	复选Allow termination of remote VM,表示可以在eclispe中停止服务端(-server)的
	Connection Type: 选择Standard(Socket Attach)做客户端,连接Host(可远程IP),Port  ,要在eclipse中打断点
	Connection Type: 选择Standard(Socket Listen)做服务端
 
JDI  (Java Debug Interface)

StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
String className = stackTraceElement.getClassName();
stackTraceElement.getMethodName();

throwable.printStackTrace(new PrintStream( xxx  ));
---------------java.util.*
静态内部类   只有该外部类调用此内部类

window 目录 下的win.ini文件是map形式的
ArrayList 的后台是ojbect []  动态增长
		e有一个toArray方法返object[]
Arrays 类的static asList(object[]) 返回一个List是一个固定心寸的list

Iterator 当 ArrayList.iterator()时,不能增加,删除ArrayList的元素,iterator.remove方法是删除前一个对象(即前面next()出来的对象)，
Enumeration<String> enumer= vector.elements(); //Enumeration没 有remove方法，vector 可以删

Collections 的类全部方法是static sort(List ,Comparator接口)
											Comparator reverseOrder() 返序排列
											min(),max();
											binarySearch(); 要已经排序后的List
Collections.rotate(rotateList, 2);//把数组 最后2位 放在 最前面
LindedList
队列只能在队尾增加，队头删除
经常增加，删除操作用LinkedList
HashSet 用关键字计算hasCode();查找速度快，当散列表满时要再重新散列，到一个新的散列，（根据load factor 0.75,即到75%时重新散列）
HashSet 是基于HashMap实现的
要不要重复对象要，重写hasCode()和equals()

TreeSet()或用Comparator 来构造
对ArrayList 如要同步，Collections.synchronizedList(List) 相比较.Vector(要快一些,但要小心使用)
								unmodifiableSet()
HashMap不是同步的,可以为null，HashTable 是同步的，不可为null


LinkedHashMap	 构造时可以设置仿问排序和插入排序,性能和数据有关,而HashMap性能和容量有关
   * 第三个参数设置为true，代表linkedlist按访问顺序排序，可作为LRU缓存
   * 第三个参数设置为false，代表按插入顺序排序，可作为FIFO缓存
   
HashTable 方法加 synchronize,效果同Collections.synchronizedMap 所有方法用一个锁 

Stack 实现了Vector 有一个elementAt()方法，不适用
Properties 是对String类型 的键值对　load(InputStream)

LinkedHashSet
JDK 7新的排序 DualPivotQuicksort (在Arrays.sort方法中有使用)

--------------------------正则
str.replaceAll("\\p{Alpha}",""); //字母删除

 boolean a=Pattern.matches("^CB.*", "CB123");是否以什么开头要加.* ,而JS 不用加.*
 
 boolean b = Pattern.matches("a*b", "aaaaab");
 
Pattern SPACES = Pattern.compile("\\s+"); //空格和制表符
String[] parts=SPACES.split("this is a hello	text	file");

String matches(".") "."表示任何的一个字符  要匹配"\\."   []要中的一个,可以&&,^

eclipse 中ctrl+shift+/加注释,ctrl+shift+\取消注释
"\\".matches("\\\\")//后面的是表达式  "
\b匹配单词边界，如空格，换行
POSIX 表示可移植操作系统接口（Portable Operating System Interface ，缩写为 POSIX 是为了提高 UNIX 环境下应用程序的可移植性
Matcher对象m.find("xx");在匹配的字串中找子串，找到返回true
m.matchees();到不匹配的地方停止，匹配整个字符串
可以m.reset();,m.find();从刚才停止的地方开始
m.lookingAt();每次都从开头进行匹配
m.start()开始位置，m.stop()结束位置，要先能够find()
Pattern p=Pattern.compile("java",Pattern.CASE_INSENSITIVE )
Matcher m=p.matcher("java Java JAVA ");//或用m.replaceAll("xxx")
while(m.find())
	Sysytem.out.println(m.group());//找到的字串
m.appendReplacement(StringBuffer , "x ") 把找到的字符串用x替换，放到StringBuffer中，它是最后的结果，包括不匹配的部分，但有可没有尾部，可以appendTail(StringBuffer)

m.group(1)可以给一个参数是int index 对应的Pattern.compile("x(xx)x")中的() ,可以嵌套，
		第一个“(”是第一组//找到所有的第一组的
BufferReader .readLine();不读\n

//Pattern p=Pattern.compile("(.{3,10})[0-9]"); //如是(.{3,10})[0-9] 贪婪的Greedy ，显示的是aaaa5bbbb6(先全吃(10个)，再吐)
//Pattern p=Pattern.compile("(.{3,10}?)[0-9]");//如是 (.{3,10}?)[0-9] 勉强的（Reluctant ）显示的是aaaa5（先吃3wh ,再一个一个的吃）
Pattern p=Pattern.compile("(.{3,10}+)[0-9]");  //如是 (.{3,10}+)[0-9] 占有的(Possessive )全吃(10个)，但不吐，所以没有匹配的，但在尾加一个数可匹配
String s="aaaa5bbbb6"; 
Matcher m=p.matcher(s);
 while (m.find())
 {
	 System.out.println(m.group());  
 }

resXml.replaceFirst(startTag+"(.)*"+endTag ,  startTag+singStr+endTag);//修改标签的值

//appendReplacement java doc中的示例修改
 Pattern p = Pattern.compile("<(\\w+)></(\\w+)>"); // 把XML没有内容的两个标签一组修改为一个标签
 Matcher m = p.matcher("<person><name></name> <age></age></person>");
 StringBuffer sb = new StringBuffer();
 while (m.find()) {
	 System.out.println(m.group()); // 是整个表达式
	 System.out.println(m.group(0)); // 0组是整个表达式
	 System.out.println(m.group(1)); //我们只要捕获组1的数字 
	 m.appendReplacement(sb,"<"+m.group(1)+"/>");
 }
 m.appendTail(sb);
 System.out.println(sb.toString());
 
  
 
---------------太难了
 
 // (?=exp) 匹配exp前面的位置       , javaDoc 上说 (?=X) X, via zero-width positive lookahead  
 //p=Pattern.compile(".{3}(?=a)"); //输出444;  
   p=Pattern.compile("(?=a).{3}"); //输出是a66, (?=a)放前把自己带进去 a
  
  //(?!exp) 匹配后面跟的不是exp的位置   ,javaDoc 上说 (?<!X) X, via zero-width negative lookbehind 
// p=Pattern.compile(".{3}(?<!a)"); //输出444 和a66两组
	
  // (?<=exp) 匹配exp后面的位置  ,javaDoc 上说 (?<=X) X, via zero-width positive lookbehind 
  p=Pattern.compile(".{3}(?<=a)");// 输出是44a　,放前把自己带进去 a
  m=p.matcher("444a66b");
  while (m.find())
 {
	 System.out.println(m.group());  
 }
----------------

InputStream is=this.getClass().getResourceAsStream("mysqlJDBC.properties"); //以/开头表示从根找,否则当前包下找
Properties props=new Properties();
props.load(is);  //会过虑以#开头的注释 .properties文件

Enumeration<URL> resourceUrls = getClassLoader().getResources("testprop/config2.properties");//不以/开头
while (resourceUrls.hasMoreElements()) {
	URL url = resourceUrls.nextElement();
	System.out.println(url);
}

java -splash:Sunset.jpg com.xx.MainApp  启动显示图片
如是.jar包中加参数 SplashScreen-Image: Sunset.jpg  // :后有空格


javap （-s）来生成属性或者方法的签名，也可用来有哪些属性方法,
I 表示 int
S short
B byte
C char
F float
D double
---
L 表示类以;结束
[ 表示数组
Z 表示 boolean
J 表示 long


pack200 压缩.jar包
unpack200
rmiregistry 命令启动后,会监听1099 端口

eclipse启动时读取环境变量JAVA_HOME

---------instrument 

java.lang.instrument 

jar包的META-INF/MANIFEST.MF 文件必须有Premain-Class, 如Premain-Class: instrument.MyClassFileTransformer
用来监测和协助运行在 JVM 上的程序，甚至能够替换和修改某些类的定义, 可以虚拟机级别支持的 AOP 实现方式

在 main 函数运行之前执行
先找 	 public static void premain(String agentArgs, Instrumentation inst); 
如无再找 public static void premain(String agentArgs); 
		
	//每装载一个类，transform 方法就会执行一次
	可以使用Apache Commons BCEL 项目操作class字了码
 
 inst.addTransformer(new MyClassFileTransformer()); 
//--- 方式2
 byte[] bytes = 得到class的byte
ClassDefinition def = new ClassDefinition(TransClass.class,bytes); 
inst.redefineClasses(new ClassDefinition[] { def });
		
在 main 函数运行之后执行
jar包的META-INF/MANIFEST.MF 文件 中 Agent-Class
public static void agentmain(String agentArgs, Instrumentation inst); 
public static void agentmain(String agentArgs); 


//示例
class MyClassFileTransformer implements ClassFileTransformer
{
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) 
			throws IllegalClassFormatException 
	{
		//System.out.println("className="+className);
		if (!className.equals("instrument/TransClass")) {
			return null;
		}
		//System.out.println("-- transform");
		File file = new File("D:/eclipse_java_workspace/J_JavaSE/bin/instrument/TransClass.class.2");	// 修改TransClass返回2,保存.class文件为.class.2
		InputStream is  = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		is.read(bytes);
		is.close();
		return bytes;
	}
}

public class Premain 
{ 
    public static void premain(String agentArgs, Instrumentation inst)   { 
    	System.out.println("enter premain"); 
        //inst.addTransformer(new MyClassFileTransformer()); 
		//-----------方式2
	    byte[] bytes = 得到class的byte
		ClassDefinition def = new ClassDefinition(TransClass.class,bytes); 
        inst.redefineClasses(new ClassDefinition[] { def });
    } 
 }
 
META-INF/MANIFEST.MF
Premain-Class: instrument.Premain
Main-Class: instrument.InstrumentMain

//jar -cvfm myinsturment.jar META-INF/MANIFEST.MF  instrument   打包一定要加m
//jar -cvfm myinsturment.jar instrument/META-INF/MANIFEST.MF  instrument
// java -javaagent:myinsturment.jar  -jar myinsturment.jar   显示2

虚拟机启动之后来加载某些 jar 进入 bootclasspath,
注意加入到 classpath 的 jar 文件中不应当带有任何和系统的 instrumentation 有关的系统同名类
注意系统参数“java.class.path”，

Instrumentation类的
appendToBootstrapClassLoaderSearch(JarFile jarfile)  
appendToSystemClassLoaderSearch(JarFile jarfile) 

--------------------------JDK14 新特性
java.lang.Record 是Preview阶段 javac --enable-preview
ZGC 在 Windows/macOS 上是 experimental 阶段, 打开使用 -XX:+UnlockExperimentalVMOptions -XX:+UseZGC. 
G1的 NUMA(non-uniform memory access) 内存分配  -XX:+UseNUMA
switch增加不是Preview了

--删除的
CMS garbage collector has been removed.
pack200 and unpack200 tools  removed.  
--------------------------JDK13 新特性

ZGC  可把未使用的heap内存返回给操作系统,但不能小于-Xms的值，默认启用 可-XX:-ZUncommit
	如配置了-Xms的值等于-Xmx 则隐示关闭了ZUncommit 
 
-XX:ZUncommitDelay=<seconds> (defaults to 300 seconds)
 
ZGC由最大堆内存由4TB 到16TB
 
-XX:SoftMaxHeapSize=<bytes> 只当打开ZGC才生效 ,值不能超过 -Xmx,如不设置默认值为-Xmx的值
	堆会尽量不超这个值SoftMaxHeapSize，除非防止报OutOfMemoryError时才超出
	可动态配置，使用 jcmd VM.set_flag SoftMaxHeapSize <bytes> 

动态 CDS Archiving


switch 增强功能还是Preview阶段 javac --enable-preview
enum Week
{
	MONDAY, SATURDAY,SUNDAY ,
}
Week day=Week.MONDAY;
switch (day) {    
	case MONDAY  -> System.out.println(1);    
	case SATURDAY,SUNDAY -> System.out.println(0);    
}


--------------------------JDK12 新特性
idea-2019.1 可选到12的编译级别， eclipse-4.12.0(2019-06)可选到12编译
 

---switch 功能还是Preview阶段 javac 中增加 --enable-preview
case可多个enum,可没有break;
	
	enum Week{
		MONDAY, TUESDAY,THURSDAY,FRIDAY, SATURDAY,SUNDAY
	}
	Week day=Week.FRIDAY;
	int numLetters = switch (day) {
		case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
		case TUESDAY                -> System.out.println(7);
		case THURSDAY, SATURDAY     -> System.out.println(8);
		case WEDNESDAY              -> System.out.println(9);
	}
	
	String s="Foo";
	int result = switch (s) {
	case "Foo": 
		break 1;
	case "Bar":
		break 2;
	default:
		System.out.println("Neither Foo nor Bar, hmmm...");
		break 0;
	};

每个class文件有常量池

class data-sharing (CDS) archive
-Xshare:dump 
-Xshare:auto  JDK11默认是启用的
-Xshare:off

G1
	当 G1 垃圾回收器的回收超过暂停目标，则能中止垃圾回收过程。
	改进 G1 垃圾回收器，以便在空闲时自动将 Java 堆内存返回给操作系统
	
 ZGC 不使用的类可被卸载 默认启用，可 -XX:-ClassUnloading
--------------------------JDK11 新特性 LTS
Oracle JDK 11 是LTS（长期支持）版本

//javax.jws.WebService web;//JDK 11没有这个类 
//删java.xml.ws , java.xml.bind  ,java.xml.ws.annotation 
//删命令 wsimport,wsgen
//删Java Mission Control (JMC) ，JavaFx
//不推荐用  Nashorn JavaScript Engine(jjs,jrunscript)   (firefox官方有 Rhino，GraalVM 是替代方案)

GTK3 Is Now the Default on Linux/Unix 


    HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://foo.com/"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("file.json")))
                .build();


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("proxyIP", 80)))
                .authenticator(Authenticator.getDefault())
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());


ZGC 只用在 Linux/x64 实际阶段 XX:+UnlockExperimentalVMOptions  , 不兼容 Graal 
	-XX:+UseZGC  ,目标是暂停时间不能超过10ms(不管多大的堆都能保持在10ms以下）),堆内存可TB

Epsilon GC 实际阶段

Class Data Sharing (CDS) 支持在module path,即 --module-path选项


--------------------------JDK10 新特性
JDK版本规则开始变化 ，半年一个特性(大)版本
从JDK9之后，每6个月发布一个版本，每3年发布一个LTS版本

var str=new String("abc123");//var 类型推断
		
StringReader reader=new StringReader(str);
StringWriter writer=new StringWriter();
try {
	reader.transferTo(writer);//JDK 10 new 
} catch (IOException e) {
	e.printStackTrace();
}
System.out.println(writer.toString());
System.out.println(Runtime.version());//JDK 9
System.out.println(Runtime.version().feature());//输出JDK版本，JDK 10
System.out.println(Runtime.version().update());//输出JDK更新，JDK 10
System.out.println(Runtime.version().interim());
System.out.println(Runtime.version().patch());

var list=new ArrayList<String>();
list.add("one");
var cloneList=List.copyOf(list);//JDK 10

List<String> list2 = cloneList.stream()
		  // .map(Person::getName)
		   .collect(Collectors.toUnmodifiableList());//JDK 10
//list2.add("two");//报错

var set =new HashSet<String>();
set.add("one");
var cloneSet=Set.copyOf(set);//JDK 10
var set2=cloneSet.stream().collect(Collectors.toUnmodifiableSet() );//JDK 10 

var map =new HashMap<String,Object>();
map.put("one", "一");
var cloneMap=Map.copyOf(map);//JDK 10
		
//G1 并行化
//删 javah
//java doc 增强
//增强 Docker  默认打开，可禁用-XX:-UseContainerSupport  CPU数  -XX:ActiveProcessorCount=count
 // -XX:InitialRAMPercentage  -XX:MaxRAMPercentage -XX:MinRAMPercentage
 
StampedLock stampedLock=new StampedLock();
long stamp = stampedLock.writeLock();

StampedLock.isLockStamp(stamp);//JDK 10
StampedLock.isOptimisticReadStamp(stamp);
StampedLock. isReadLockStamp(stamp);
StampedLock. isWriteLockStamp(stamp);

 
StackWalker.getInstance( StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(System.out::println);//JDK9


--------------------------JDK9新特性
--module-path 如放JDK,为了兼容老版本的jar放 --class-path中(eclipse)

FileInputStream resource1 = new FileInputStream("c:/tmp/input.txt"); 
FileInputStream resource2 = new FileInputStream("c:/tmp/input2.txt"); 
//JDK 8
		{
//			try (FileInputStream rs1=resource1;
//					FileInputStream rs2=resource2 	){
//				
//			} catch (FileNotFoundException e) {
//				 
//				e.printStackTrace();
//			}
//			resource1.read();//流在这已经关闭
		}
		//JDK 9
		{ 
			try (resource1;resource2){
				
			} catch (FileNotFoundException e) {
				 
				e.printStackTrace();
			}
			resource1.read();//流在这已经关闭
		}

 
//--- module-info.java    JDK9   
module J_JavaSE
{
	requires java.base;
	requires java.desktop;
	requires java.sql;
	requires java.sql.rowset;
	requires java.rmi;
	requires java.instrument;
	requires java.naming;
	requires java.compiler;
	requires java.xml;//在javax.xml.parsers.DocumentBuilderFactory;模块 
}

import java.util.concurrent.SubmissionPublisher;
try (SubmissionPublisher<Long> pub = new SubmissionPublisher<>()) { 
	System.out.println("Subscriber Buffer Size: " + pub.getMaxBufferCapacity()); 
	subTask = pub.consume(System.out::println); 
	LongStream.range(1L, 6L).forEach(pub::submit);
}



import java.util.concurrent.Flow;
public class SimpleSubscriber implements Flow.Subscriber<Long> {    
    private Flow.Subscription subscription; 
    private String name = "Unknown"; 
    private final long maxCount; 
    private long counter;
    public SimpleSubscriber(String name, long maxCount) {
        this.name = name;
        this.maxCount = maxCount <= 0 ? 1 : maxCount;
    }
    public String getName() {
        return name;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.printf("%s subscribed with max count %d.%n", name, maxCount);      
        subscription.request(maxCount);// 设置最大接收消息，防止接收端收到太多的消息压力大，处理完成后可以再请求
    }
    @Override
    public void onNext(Long item) {
        counter++;
        System.out.printf("%s received %d.%n", name, item);
        if (counter >= maxCount) {//也可以不判断 因 subscription.request(maxCount); 已经设置最大接收消息
            System.out.printf("Cancelling %s. Processed item count: %d.%n", name, counter);            
            // Cancel the subscription
            subscription.cancel();//取消接收消息,也可以再请求
            
//            counter=0;
//            subscription.request(maxCount);
        }
    }
    @Override
    public void onError(Throwable t) {
        System.out.printf("An error occurred in %s: %s.%n", name, t.getMessage());
    }
    @Override
    public void onComplete() {
        System.out.printf("%s is complete.%n", name);
    }
}


  SubmissionPublisher<Long> pub = new SubmissionPublisher<>();
  SimpleSubscriber sub1 = new SimpleSubscriber("S1", 2);
   pub.subscribe(sub1);//会调用 Subscriber的onSubcribe方法
//后面订阅的，收不到前面的消息
 pub.submit(i);//会调用 Subscriber的onNext方法
 pub.close();//会调用 Subscriber的onComplete方法
  

jshell 命令是JDK9新功能,类似Scala
jshell> /help
jshell> /list  -start 显示import
jshell> /list -all   默认import的包 和 最近的代码
jshell>/imports 列出已经导入的包：//默认import的有
	import java.io.*;
	import java.math.*;
	import java.net.*;
	import java.nio.file.*;
	import java.util.*;
	import java.util.concurrent.*;
	import java.util.function.*;
	import java.util.prefs.*;
	import java.util.regex.*;
	import java.util.stream.*;
 
按tab键可以自动补全，也提示方法的参数
 
/vars  列出所有变量 

/methods 列出所有方法 
/也可以用tab
/edit 方法名 会打开JShell edit pad(java写的)编辑器

/save 
/open

/l -a 的全写是  /list -all



--------------JDK8 新特性
//@FunctionalInterface //即只可有一个未实现的方法,如不加这个默认就是
interface IntegerMath {
	int operation_(int a, int b);
}
//lambda表达式
IntegerMath subtraction = (a, b) ->{ return a - b;};//如使用lambda ,必须接口是@FunctionalInterface,类似于匿名内部类
IntegerMath addition = (c, d) -> c + d; //如实现只有一行，可省{}和return

double num=5;//这里可以不用加final
lambdaOneInterface one = x -> x*0.9-num;  //如果只一个形参可以省(),-> 只可以用于@FunctionalInterface的方法
//num=6;// 和内部类一样，引用外部变量必须为final,这里只是可以不写，如后面有修改就会报错

interface MethodRef
{
	void processStr(String s) ;
}
MethodRef instanceRef=  System.out::println ;//实例方法 引用:: ，要求接口中的方法参数和被引用方法的参数有相同类型和个数的
instanceRef.processStr("method instance ref string :: ");

interface MethodStaticRef 
{
	void processArray(int [] s) ;
}
MethodStaticRef staticRef=Arrays::sort;//类的静态方法引用 ::
int[] array=new int[]{6,9,4,5,8,1};
staticRef.processArray(array);
System.out.println(Arrays.toString(array));

interface MethodRefNoneStatic
{
	void processStr(PrintStream stream,String s) ;
}

MethodRefNoneStatic objectRef=PrintStream::println;//类的非静态方法引用 :: ,接口的唯一方法的第一个参数一定要是PrintStream类型
objectRef.processStr(System.out,"method noneStatic ref string");


interface MethodConstructRef
{
	void processStr(char[] s) ;
}

MethodConstructRef constructRef= String::new;//构造函数引用，接口方法与构造函数声明结构相同
constructRef.processStr(new char[]{'中','国'});
		
		
LocalDateTime dateAndTime = LocalDateTime.now();
LocalDate currentDate = LocalDate.from(dateAndTime); 
LocalTime timeToSet = LocalTime.of(hour, minute, second);//of表示修改
dateAndTime = LocalDateTime.of(currentDate, timeToSet);

LocalDate birthday;
birthday.until(IsoChronology.INSTANCE.dateNow())//返回  Period 
	.getYears();

ZoneId.systemDefault();
ZonedDateTime.of(dateAndTime, ZoneId.of("Asia/Shanghai"));//
 
TimeZone.getTimeZone("GMT+8");//方式一
TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));//方式二
String[] countryCities=TimeZone.getAvailableIDs();//所有 大州/市

java.time.ZonedDateTime.parse("2017-01-20T17:42:47.789+08:00[Asia/Shanghai]");
	
 
//接口中有方法实现,方法前加default或static
//default方法 能够添加新的功能到已经存在的接口，确保与采用老版本这些接口编写的代码的二进制兼容性
//比抽象类好处可以多重继承,一个类继承两个接口时,这个两个接口中如有相同的default方法,子类必须重写
//接口中定义的变量默认是public static final 型，且必须给其初值，所以实现类中不能重新定义，也不能改变其值;抽象类中的变量默认是 friendly 型，其值可以在子类中重新定义，也可以重新赋值
	

		 
命令 jdeps  <class,jar,目录>  显示所有依赖中的缺失(不使用eclipse也可以了)

javac 的-profile 选项
compact1< compact2 <  compact3 < JavaSE
javac -profile compact1 Hello.java
 
 Nashorn JS 引擎 ,使用 jjs 命令调用, JDK11不推荐用,未来删除
jjs 后可输入JS来测试,也可 jss  x.js
jjs> typeof java.lang.System == "function"
jjs> var intNum = 10
jjs> intNum.class
class java.lang.Integer

--x.js
var Run = Java.type("java.lang.Runnable");
var MyRun = Java.extend(Run, {   
    run: function() {
        print("Run in separate thread");
    }
});
var Thread = Java.type("java.lang.Thread");
var th = new Thread(new MyRun());


//java.util
Base64.Encoder base64Encoder=Base64.getEncoder();
byte[] encoded=base64Encoder.encode("这是一个中文".getBytes("UTF-8"));
System.out.println(new String(encoded));

Base64.Decoder base64Decoder=Base64.getDecoder();
byte[] decoded=base64Decoder.decode(encoded);
System.out.println(new String(decoded));

Base64.getUrlEncoder();

List<String> names=new ArrayList<>();
names.add("1");
names.add("2");
names.add("3");
System.out.println(String.join("-", names));

---Stream API
//--reduce
List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
Integer sumReduce = integers.stream().reduce(100, Integer::sum);//起始种子值
System.out.println(sumReduce);

List<String> strs = Arrays.asList("H", "E", "L", "L", "O");
String concatReduce = strs.stream().reduce("START_", String::concat);
System.out.println(concatReduce); 

String join= strs.stream().collect(Collectors.joining(", "));
System.out.println(join); 

Optional accResult = Stream.of(1, 2, 3, 4)
		.reduce((acc, item) -> { 
			acc += item;   
			return acc;
		});
System.out.println("accResult: " + accResult.get()); 


List<Integer> l = Arrays.asList(numbers);
List<Integer> r = l.stream() //Stream<Integer>
		.map(e -> new Integer(e))
		 //.parallel()//并行  内部使用ForkJoinPool,默认线程数是处理器数
		.filter(e -> e > 2)//参数为Predicate类型
		 //.sequential()//串行
		.distinct()
		.collect(Collectors.toList());
	
	
List<Integer> list1=Arrays.stream(numbers).mapToInt( Integer::parseInt).mapToObj(Integer::new).collect(Collectors.toList());
List<Integer> list2=Arrays.stream(numbers).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
	     
boolean isEmpty=Arrays.stream(emps).anyMatch(item -> item.getSalary()>3500);//.allMatch 

Map<String,List<Employee>> titleEmp=Arrays.stream(emps).collect(Collectors.groupingBy(Employee::getTitle));
		
// Accumulate names into a List
 List<String> list = people.stream()
   .map(Person::getName)
   .collect(Collectors.toList());

 // Accumulate names into a TreeSet
 Set<String> set = people.stream()
   .map(Person::getName)
   .collect(Collectors.toCollection(TreeSet::new));

 // Convert elements to strings and concatenate them, separated by commas
 String joined = things.stream()
   .map(Object::toString)
   .collect(Collectors.joining(", "));

 // Compute sum of salaries of employee
 int total = employees.stream()
   .collect(Collectors.summingInt(Employee::getSalary));

 // Group employees by department
 Map<Department, List<Employee>> byDept = employees.stream()
   .collect(Collectors.groupingBy(Employee::getDepartment));

 // Compute sum of salaries by department
 Map<Department, Integer> totalByDept = employees.stream()
   .collect(Collectors.groupingBy(Employee::getDepartment,
								  Collectors.summingInt(Employee::getSalary)));

 // Partition students into passing and failing
 Map<Boolean, List<Student>> passingFailing = students.stream()
   .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));
   
--------------------------JDK 7 新特性
G1拉圾收集器   -XX:+UseG1GC -Xms2g Xmx2g -XX:MaxGCPauseMillis=500 


File fileDire = new File("/home/test");// 在windows上是建立在,当C:盘上没有权限时,会D:盘上建立
boolean isOK = fileDire.mkdirs();
System.out.println("dir create :"+isOK);
try
{	 
InputStream  in=new FileInputStream("c:/temp/aa.txt"); //实现AutoCloseable接口的自动关闭
}catch( NullPointerException | FileNotFoundException e  ) // 多个异常用 | 
{
}
 
 try (BufferedReader br = new BufferedReader(new FileReader(""))) //try ()中可以加代码,后可没有catch和finally
 {  
  String a= br.readLine();  
}  
		
int billion=1_000_000_000;//在数字中使用下划线
int binary=0b1001_1001;  //0b是二进制

switch("one")  //switch可可char,byte ,int ,short,enum ,String是新版本,,不可用于long和小数

 Map<String, List<String>> myMap = new HashMap<>(); //可以简写
 
ForkJoinPool pool = new ForkJoinPool(); //Fork/Join 模式 ,默认是runtime.availableProcessors();CPU多少核的
pool.invoke(new MySortTask()); //会调用RecursiveAction 的 compute 方法
class MySortTask extends RecursiveAction //如使用RecursiveTask的compute方法可带返回值
{
	protected void compute()
	{
	//if(数量少)
	//		直接调用
	//else  数量多
	//		递归拆分多个,一般是二分法
	//		invokeAll(left,right); //invokeAll是ForkJoinTask的方法
	//		compute方法没有返回值,这里可以保存递归值
	
		.fork();//子任务的异步执行,每次调用增加一个线程,直到 runtime.availableProcessors()个线程为止,如果超过getSurplusQueuedTaskCount()返回值加1
		.join()//阻塞等待结果完成。
	}
}

//第二种带返回值的
RecursiveTask<Integer> task=new MyRecursiveTask (); 
pool.execute(task);//变execute
Integer result = task.get();

class MyRecursiveTask extends RecursiveTask<Integer> {  //变RecursiveTask
 public Integer compute() {
	 //可调用fork/join/compute
	return null;  
 }
}
//---------JDBC 
DatabaseMetaData dbMetaData= conn.getMetaData();
ResultSet typeRS=dbMetaData.getTableTypes();//有TABLE，VIEW，SYSTEM TABLE ，SYSTEM VIEW
while(typeRS.next())
{
	System.out.println(typeRS.getString(1));
}
//MySQL8报错
/*ResultSet tablesRS=dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
System.out.println("=======所有的表:");
while(tablesRS.next())
{
	System.out.println(tablesRS.getString("TABLE_NAME"));
}
*/

String dbName=dbMetaData.getDatabaseProductName();//Oracle
int dbMajor=dbMetaData.getDatabaseMajorVersion();//11

PreparedStatement prepare=conn.prepareStatement("select * from student");
ResultSetMetaData tableMetaData=prepare.getMetaData();
System.out.println("=======student表的列:");
int count=tableMetaData.getColumnCount();
for(int i=1;i<=count;i++) 
{
	System.out.println(tableMetaData.getColumnName(i)+":类型"+tableMetaData.getColumnTypeName(i));//从1开始
}
//--insert
prepare.addBatch();
prepare.executeBatch();
//--absolute
conn.prepareStatement("select * from student",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
rs.getRow();
rs.absolute(rowNum);
//---
CallableStatement call=conn.prepareCall("call myproc(?,?)");
call.registerOutParameter(2, Types.VARCHAR);
call.setInt(1, 123);
call.execute();
String result=call.getString(2);

//--RowSet jdk1.7新功能
RowSetFactory  rowSetFactory = RowSetProvider.newFactory();//用缺省的RowSetFactory 实现 
JdbcRowSet rowSet = rowSetFactory.createJdbcRowSet(); 
String url="jdbc:h2:tcp://localhost:9092/test";
rowSet.setUrl(url); 
rowSet.setUsername("sa"); 
rowSet.setPassword(""); 
rowSet.setCommand("SELECT * FROM EMPLOYEE where DEPARTMENT_ID  =? ");
rowSet.setInt(1, 10);
rowSet.execute(); 
while(rowSet.next())
{
  System.out.println(rowSet.getString("USERNAME"));
}
//一个中文字符在Java中占两个字节,在Oracle中AL32UTF8  , oracle -> java 一个中文占两个字节
//(java->oracle varchar2 时一个中文占3个字节) ( java->oracle nvarchar2  时一个中文占2个字节 )

//-----jdk6新性
public void printf(String format, Object ...args) //args在方法体中是一个Object的数组
{
	Object[] o=args;
}
 

//查Queue,BlockingQueue的API
//Queue队列满时,add    方法抛出异常,offer(可传超时时间)方法返回 false   ,put 方法会阻塞	,Insert操作
//Queue队列空时,remove 方法抛出异常 ,poll(可传超时时间)方法返回 null	,take方法会阻塞	, Remove操作
//Queue队列空时,element方法抛出异常 ,peek 			   方法返回 null   					,examine操作

 

ArrayBlockingQueue(2);//有容量限制
PriorityBlockingQueue //按自然排序或者传Comparator

//DelayQueue 队列中的元素必须实现新的 Delayed 接口
//添加可以立即返回，但是在延迟时间过去之前，不能从队列中取出元素。如果多个元素完成了延迟，那么最早失效(失效时间最长)的元素将第一个取出,不可放null, size返回所有(过期和未过期的)

// copy-on-write 模式,首先取得后台数组的副本，对副本进行更改，然后替换副本
//保证了在遍历自身更改的集合时，永远不会抛出,遍历集合会用原来的集合完成，而在以后的操作中使用更新后的集合。 
//最适合于读操作通常大大超过写操作的情况
CopyOnWriteArrayList,CopyOnWriteArraySet  ,	老的Set正在Iterator时,不能使用Collection的remove,add,但可以用Iterator的remove

ConcurrentLinkedQueue 线安全的
ConcurrentHashMap  key和value都不能为空
ConcurrentSkipListMap 是一个SortedMap

SynchronousQueue 是一个阻塞队列,每次的插入必须有取时才会插入,否则等有人来取,可以有多个来取
Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(64))


--------------------------JDK1.5新特性
class Persion<T1,T2 extends List>   //接口或类都用extends(T1 extends Object)
	Persion <String,ArraryList> p=new Persion <String,ArraryList> //只可以是ArraryList
class Persion<T>
      Persion<? extends List> x=null;   //?代表　一个变量可以指向List 所有子类 ，要先定义Persion<T> ，？只在使用中
	x=new Persion<ArrayList>
	x=new Persion<LinkedList>
	 Persion<? super List> x=null;　　只可是List的父类Object
		x=new Persion<Object>

Persion<?> 和Persin<? extends Object>效果相同  



Persion<String> p1=new Persion<String>();
Persion<?> p2=p1;
p2.setX(null);//对用?，? extends 的,只可以取得，设为null,不可以改为其它的,编译器不知道放进去的是什么类型，如要向转换，失去泛型的意义

public class Child<T1,T2,T3> extend Parent<T1,T2> //表子类的T1传给父类的T1 可以和定义时不同class Parent<X1,X2>


for (int x : array)
{
}

enum 对象　有values()和valueOf()

enum Color
{
red,bule;　　　//red是Color的实例  public static final Color red=new Color();
}

Color color
	
enum Color
{
    red(1), //1是给自己red 的实例
	bule("a") ,
	;  //这部分要放在最前面
	
    private int num;
    private String str;
    Color(int n)    //自己的构造方法,不能加public
    {
        this.num=n;
    }
    Color(String s)
    {
        this.str=s;
    }
}
全部继承自java.lang.Enum
compareTo()方法
Enum  .orinal() 返回当初声明属性的顺序的位置,从0开始


EnumSet<Color> cset=EnumSet.of(Color.red,Color.bules) 构造有两个元素的Set
EnumSet.complementOf(cset);返回一个 EnumSet<Color>里面是cset没有的
EnumSet.copyOf(Collection)
Enumset.noneOf(Color.class) //创建一个空的用指定的类别
	add(xx);


EnumMap<Color,String> map=new EnumMap(Color.class); //key是enum类型Color
会根据enum的顺序来排序Map

import static com.xxx.STANT 或是static 方法名
直接用STANT

int sum(int ... num)  //可变的函数参数 是三个点 左右空格可有,可无 ，这个参数必须放放在最后，只可有一个
{
   for (int i = 0; i < num.length; i++)
   {
	  System.out.println(num[i]);
   }
}
JDK内嵌的Annotation
@Override   可以正确的
@Deprecated 标明该方法是不被推荐使用的
@SuppressWarnings({"unchecked","deprecation"});  不显示警告


enum Color{ bule,yello,red};//定义一个enum类型

//默认annotation是信息是保存在.class文件中,JVM不读取,只被其它工具使用
@Retention(RetentionPolicy.RUNTIME)//默认是CLASS(放class文件中),RUNTIME(放JVM 可反射来读),SOURCE(不编译)
publci @interface MyInerface  //JVM 自动实现 Annotation 接口,,不能在extends,implements任何其它的
{
	String value();//定一个变量名是value 类型是String,(类型可是String ,基本,Class,Annotation,Enum及这些类型的一维数组)
	String[] name() default {"lisi","aa"};//有默认值,使用时就可以不赋值
	Color co() default Color.red;
}

@MyInterface(value="hello") //如果属性名是value,可以不加属性名,数组多个加{},一个可以不加{}

  
java.lang.relect.AnnotatedElement 接口 很多常用的反射类都实现了
Method m.isAnnotationPresent(MyAnnotation.class) //当前CLASS,METHOD类上是否有指定的Annotation
MyAnnotation a=	m.getAnnotation(MyAnnotation.class)//返回一个Annotation  有annotationType().getName()
a.value() ,a.hello() 来得到在@中定义的属性值
a.annotationType()返回一个 Class<? extends Annotation>对象,一定是Annotaion 的子类(即用@interface隐式声明的)
Class 的getAnnotations();返回所有的包括父类的
	getDeclareAnnotations 返回本类中所有的

@SuppressWarnings是 RetentionPolicy.SOURCE   所以  m.getAnnotations() 没有


java.lang.annotation.Target    value要一个ElementType[] .METHOD只可把@放在方法前面
默认是任何地方都可以的
@Target({ElementType.METHOD,ElementType.TYPE})放在 public @interface My 之前 表示My可以放在方法前,类,接口前




javadoc默认不会把自己写的类前, 方法前的@,生成javadoc文档中
eclipse中project->generate javadoc
把@Document 放在public @interface Xxx之前,就可以了

默认子类不是也继承父类的@,如要继承,则要把@Inherited 放在 public @interface Xx 前,就可以了( 只对放在类的前面可以继承,方法前也可以继承,) 也会被覆盖
子类可以覆盖父类的方法和@( @Override)
接口前加@xx ,是不可以继承@xx
Junit,Spring,Hibernate ,都有@的形式来开发



-------------------------------------------反射
Class a;
a.isAssignableFrom(Class b) 
如a是b父类(接口)返回true,

Field 有一个 setAccessible(true)  //对private 的安全性
Field[] Fields = object.getClass().getDeclaredFields();// 获取本类所有属性公有私有都能获取。但是不能获取继承的。
 for (Field field : Fields) 
 {
	String fieldName = field.getName();
	PropertyDescriptor pd = new PropertyDescriptor(fieldName, object.getClass());
	Method fieldGetMet = pd.getReadMethod();// 获得读方法
	String key = pd.getDisplayName();
	Object value = fieldGetMet.invoke(object);
}

getDeclaredConstructor()
if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))
				&& !ctor.isAccessible()) {
	ctor.setAccessible(true);//可以new private();的构造方法
	Object o=ctor.newInstance(); 
}

getClasses()		拥有继承的特点，可以获取父亲级定义的内部类，而不能访问定义为private的内部类；
getDeclaredClasses()刚好相反，
						可以访问定义为private的内部类，却无法获取父亲级定义的内部类



getGenericSuperclass()方法首先会判断是否有泛型信息，有那么返回泛型的Type，没有则返回Class
getSuperclass()直接返回父亲的Class。

反射 类中的模板 是什么clas
得到T.class  
//cglib 就不行了
Class  persistentClass=(Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

Type type=this.getClass().getGenericSuperclass();
Type requestType= ((ParameterizedType) type).getActualTypeArguments()[0];//MyParent<I,O>  其中[0]是第一个I
Class<AbstractImmediateRequest>  requestClass=(Class<AbstractImmediateRequest>)requestType;


this.entityType = getClass().getClassLoader().loadClass(entityType);


 ParameterizedType pt = (ParameterizedType)field.getGenericType(); // field 是List<String> 得到List范型
 ----
WeakReference
WeakHashMap的key被实现为一种WeakReference

 
四种引用,强弱依次为:Strong Reference > SoftReference > WeakReference > PhantomReference 虚引用

SoftReference (只当没有内存时回收) softreference指向的对象依然会被回收,用做Cache, 对 生存期相对较长而且重新创建的开销也不高的对象
r=new SoftReference<MyClass> ();
r.get();//如果被回收,返回null

WeakReference,只要内存回收时就会被回收(只要垃圾回收器在自己的内存空间中线程检测到了)

PhantomReference(没有任何引用一样，随时会被jvm当作垃圾进行回收)上调用 get() 总是会返回 null 。这是因为 PhantomReference 只用于跟踪收集

ReferenceQueue<Object> rq = new ReferenceQueue<>();  
WeakReference<Object> wr = new WeakReference<>(obj, rq); 
rq.poll() //是WeakReference

WeakHashMap
Deque  线性集合可以两边,增删



-------其它

Array  的使用方法.

只有UDP可以发送和接收广播地址


classpath环境变量在没有设置时会找当前目录,如果设置时未在尾部加";" 分号,则不会找当前目录,
如果设置时已在classpath值尾部加";" 分号,则会找当前目录,

环境变量 = 号左右不要有空格

classpath值不能有空格   document setting 

set xx=   来清空
set 显示所有的


java 可以直接编译成.exe 文件



运行jconsole 命令可以查看JVM 的性能　监控
在catalina.bat中

echo Using CATALINA_BASE:   %CATALINA_BASE% 前面加上下面一行
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port="9004" -Dcom.sun.management.jmxremote.authenticate="false" -Dcom.sun.management.jmxremote.ssl="false"


-------------------JNI-------------------------------

new->win32控制台应用程序->选择[DLL],再选择[空的项目] 就不用包含其它头文件了
选DLL时,在根目录下/debug 只一个.dll 文件,并以dll扩展名结尾  

在VC界面中复文件  只是一种链接的方式(右击标签->打开所在文件夹,可检查)
右击[头文件]文件夹->添加->现有项(最好是当前项目中的)  这样只是一种链接的方式(右击标签->打开所在文件夹,可检查)


---java 调用C/C++
public class TestJavaNative 
{
	public static String USERNAME="lisi李四";//static的
	public int property=10;
	public Parent person=new Child();
	public static String staticFunc(int i,double d,char c)//static的
	{
		System.out.println("Java side  funct:");
		return "OK";
	}
	public  boolean function (int foo,Date date,int[] arr,char c)
	{
		System.out.println("Java side Child function: foo="+foo+",date="+date+",arr="+arr+",c="+c);
		return true;
	}
	public native void sayHello();//Java 中声明native方法,表示在C/C++中实现
	public static void main(String[] args) 
	{
		//编译后,使用javah命令,生成 .h文件
		//  J_JavaSE\bin>javah jni.TestNative 生成 jni_TestNative.h
		//System.loadLibrary("TestCPPNative");//要把生成的TestCPPNative.dll放入PATH环境变量中,可能要重启eclipse
		Runtime.getRuntime().loadLibrary("TestCPPNative");
		TestJavaNative test=new TestJavaNative();
		test.sayHello();//调用C
		System.out.println("Java side:property="+test.property);
		System.out.println("Java side:USERNAME="+USERNAME);//OK
	}
}

编译后,使用javah命令,生成 .h文件
J_JavaSE\bin>javah jni.TestJavaNative 生成 jni_TestJavaNative.h.h

复制文件中的 声明 到C++程序中 ,并实现
//-TestCPPNative.cpp
#include <iostream>
#include "jni_TestJavaNative.h" //又使用了jni.h,在JDK安装目录的include下,还要一个jni_mod.h在win32目录
using namespace std;
JNIEXPORT void JNICALL Java_jni_TestJavaNative_sayHello (JNIEnv * env, jobject obj)//从头文件中复制的,加参数名
//Java开头_包名_类名_方法名,如方法被声明为static,则这里的jobject是一个jclass的引用
{
	jclass hello_clazz=env->GetObjectClass(obj);
	//属性操作,OK
	jfieldID fieldID_prop=env->GetFieldID(hello_clazz,"property","I");
	jint val=env->GetIntField(obj,fieldID_prop);
	cout<<"CPP side GetIntField property is:"<<val<<endl;
	cout<<"CPP side ++val is:"<<++val<<endl;
 	env->SetIntField(obj,fieldID_prop,++val);
	
	//构造Java中的类,OK
	jclass class_date=env->FindClass("java/util/Date");
	jmethodID method_date=env->GetMethodID(class_date,"<init>","()V");//构造方法是<init>,签名返回总是V,真实代码没有void
	jobject now_obj=env->NewObject(class_date,method_date);//如果有参数加后面
	//数组操作...
	
	//方法调用,OK  
	jmethodID methodID_func=env->GetMethodID(hello_clazz,"function","(ILjava/util/Date;[IC)Z");//boolean 是Z,对应public boolean function (int foo,Date date,int[] arr,char c)
	//使用javap生成签名, javap -s -private jni.TestJavaNative  
	jboolean res=env->CallBooleanMethod(obj,methodID_func,20L,now_obj,NULL,L't');//20L,Java中的int对应C/C++中的long型,java中的字符是Unicode两个字节,C/C++中使用宽字符
	cout<<"CPP side  CallBooleanMethod  function return is:"<<res<<endl; //显示不正常??
	//public String function (int foo,Date date,int[] arr,char c)
	
	
	//调用指定父类中的方法,OK  
	jfieldID fieldID_person=env->GetFieldID(hello_clazz,"person","Ljni/Parent;");
	jobject person_obj=env->GetObjectField(obj,fieldID_person);//
	jclass parent_clazz=env->FindClass("jni/Parent");
	jmethodID methodID_doSomething=env->GetMethodID(parent_clazz,"doSomething","()V");
	env->CallVoidMethod(person_obj,methodID_doSomething);
	cout<<"CPP side  CallBooleanMethod child function"<<endl; 
	env->CallNonvirtualVoidMethod(person_obj,parent_clazz,methodID_doSomething);//调用指定父类中的方法,而不是被覆盖的子类的方法
	cout<<"CPP side  CallBooleanMethod parent function "<<endl; 		
	
	//static属性操作
	jfieldID fieldID_staticProp=env->GetStaticFieldID(hello_clazz,"USERNAME","Ljava/lang/String;");
	jstring username_str=(jstring)env->GetStaticObjectField(hello_clazz,fieldID_staticProp);//参数是jclass,可以把jobject强转为jstring
	cout<<"CPP side GetStaticObjectField USERNAME is:"<<username_str<<endl;//返回字串 结果不对???
	//-字串操作,方式1
	//const jchar * jstr =env->GetStringChars(username_str,NULL);
	//wstring wstr((const wchar_t*)jstr);
	//env->ReleaseStringChars(username_str,jstr);// ReleaseStringChars 与 GetStringChars 成对使用
	//方式2
	//const jchar * jstr =env->GetStringCritical(username_str,NULL);
	//wstring wstr((const wchar_t*)jstr);
	//env->ReleaseStringCritical(username_str,jstr);//ReleaseStringCritical 与 GetStringCritical 成对使用(显示有点不正常????),这两个函数期间,绝对不能呼叫JNI其它函数,不要出现中断操作,会使垃圾回收器停止动作
	
	//方式3,把java中字符串内容拷贝到时C/C++字符数组中
	jsize len=env->GetStringLength(username_str);//还有一种GetStringUTFLength
	jchar * buffer=new jchar[len+1];
	buffer[len]=L'\0';
	env->GetStringRegion(username_str,0,len,buffer);//不用Release
	//GetStringUTFRegion //UTF-8 以 '/0' 结尾
	wstring wstr((const wchar_t*)buffer);
	cout<<"CPP side GetStringRegion copyed is:"<<buffer<<endl;//显示不正常????,jchar 是unsigned short
	delete[] buffer;
	
	std::reverse(wstr.begin(),wstr.end());//倒序排序
	jstring copyed_str=env->NewString((jchar*)wstr.c_str(),(jsize)wstr.size());//还有一种NewStringUTF
	env->SetStaticObjectField(hello_clazz,fieldID_staticProp,copyed_str);//设置字串OK
}
 
VC2010 设置环境变量
path=C:\Program Files\Microsoft Visual Studio 10.0\Common7\IDE;C:\Program Files\Microsoft Visual Studio 10.0\VC\bin;
lib=C:\Program Files\Microsoft SDKs\Windows\v7.0A\Lib;C:\Program Files\Microsoft Visual Studio 10.0\VC\lib
include=C:\Program Files\Microsoft SDKs\Windows\v7.0A\Include;C:\Program Files\Microsoft Visual Studio 10.0\VC\include

测试OK
cl /I "D:\program\Java\jdk1.7.0_05\include\win32" /I "D:\program\Java\jdk1.7.0_05\include" /c  TestCPPNative.cpp
只能用VC生成DLL
link  TestCPPNative.obj  /DLL /LIBPATH:"D:\program\Java\jdk1.7.0_05\lib"


---C/C++ 调用java
.h文件中的 JNIEnv * //代表JVM,可以调用 
	NewObject //创建Java对象
	NewString
	New<TYPE>Array
	Get/Set<TYPE>Fild  //如GetIntField() 在jni.h文件中
	Get/SetStatic<TYPE>Field
	Call<TYPE>Method   //方法返回的是<TYPE>类型的
	CallStatic<TYPE>Method
.h文件中的 jobject //代表java的Object 类,代表哪个对象与C/C++交互,如native方法是static 的那么是一个jclass

//在jni.h文件中有很多对应关系
jclass 对应java 中的Class 
jstring对应java 中的String


//在jni.h中的很多 typedef 
JNI定义类型(名字同Java)     java 类型				c/c++类型		
jint/jsize                  int						long			                         
jlong                       long					__int64			                         
jbye		                byte					singned char	                         
jboolean		            boolean					unsigned char	                         
jchar		                char					unsigned short	                         
jshort                      short					short			                         
jfoat                       float					float			                         
jdouble                     double					double			                         
joject	                   Object					__jobject *		                         


JNIEnv 的一些方法来得到Class
jclass FindClass("org/Xxx") 从CLASSPATH中找,完整类名,包用/来分隔

jclass GetObjectClass(jobject )
jclass GetSupperClass(jclass)

GetFieldID/GetStaticFieldID  //来得到jfieldID,表示Java中的属性和方法的引用
GetMethodID/GetStaticMethodID  //来得到jmethodID,

属性,方法的签名(如重载方法的区分):
如 void function(int) 的签名是 (I)V  //(I) 是参数类型int ,V 是返回类型void 

基本类型	签名
boolean		Z
byte		B
char		C
short		S
int			I
long		J,可以使用javap看,如是L与对象相同
float		F
double		D
void		V
object		L用/分隔的完整类名; 如	Ljava/lang/String; //以L开头,;结束
Array		[类型签名 ,如 [I,[Ljava/lang/Oject; 代表 Object[] //以[开头,后加类型的签名     
Method		(参数1的签名  参数2的签名....)返回类型的签名,如(DD)D 表示 double (double ,double)

]]]


javap -s 来生成 签名 ,用来生成有哪些属性,方法

-public  可对public 类型的输出
-protected 
-private 所有的
示例
javap -s -private jni.TestJavaNative  

Get/SetSatic<TYPE>Field
Get/Set<TYPE>Field  //取得对象属性值 



env->CallStatic<TYPE>Method
env->Call<TYPE>MethodV(.....va_list list)//<TYPE>是返回类型,会所list中的所有值做为参数传入
env->Call<TYPE>MethodA(jobject,jmethodID,jvalue * )//jvalue指针做参数传入,jvalue是一个union,里面有很多类型
jvalue * args=new jvalue[3];
args[0].i=20L;//jint  类型
args[1].d=12.4;

delete []args;

CallNonvirtual<TYPE>Method  //对virtual方法 想调用父类的方法 
(java 中没有办法做到Parent child =new Child();child.fun()是覆盖方法,想调用父类的fun()) 




jclass class_date=env->FindClass("java/util/Date");
jmethodID method_date=env->GetMethodID(class_date,"<init>","()V");//构造方法签名返回总是V,真实代码没有void
jobject now_obj=env->NewObject(class_date,method_date);//如果有参数加后面

--String 操作
AllocObject //用的很少,状态非初始化的,使用之前要 CallNonvirtualVoidMethod 调用建构函数,不成功的

jcharArray arg=env->NewCharArray(5)
env->SetCharArrayRegion(arg,0,4,L"我的字串");



 (Java中的用UTF-16 转换为C/C++ wchar_t*,或者是char* (UTF-8)) 
jchar * GetStringChars  //UTF-16 可以传入NULL ,第二个参数指定是否复制,JNI_FALSE,JNI_TRUE 
char*  GetStringUTFChars //UTF-8 
//如不是复制千万不要改java中的内容,破坏java是一个常量的规则
ReleaseStringChars(jstring Java端的, jchar * C++本地使用的)
ReleaseStringuTFChars 释放内存

wstring wstr(jstr);//wstring wstr((wchar_t * )jstr); ,c_str(),size()
std::reverse(wstr.begin(),wstr.end());//倒序排序

//以下直接指向Java 字符串的指针 ,Java用UTF-16
GetStringCritical
ReleaseStringCritical  //这两个函数期间,绝对不能呼叫JNI其它函数,不要出现中断操作,会使垃圾回收器停止动作


//把java中字符串内容拷贝到时C/C++字符数组中 
GetStringRegion(jstring str,jsize start,jsize len,char *buffer);//宽,不用ReleaseString
GetStringUTFRegion(const char* str)//UTF-8 以 '/0' 结尾


jstring NewString(const jchar * str ,jsize len);//创建Java端String
NewStringUTF(const char* str) //UTF8 编码,以\0结尾

jsize GetStringLength(jstring) 
jsize GetStringUTFLength(jstring)


--数组 操作
GetArrayLength
Get<TYPE>ArrayElements(<TYPE>Array arr,jboolean* isCopied) 
Release<TYPE>ArrayElements(<TYPE>Array  arr,<TYPE>* array,jint mode)
mode  取值 
0			对Java的数组进行更新,释放C/C++ 数组
JNI_COMMIT	对Java的数组进行更新,不释放C/C++ 数组
JNI_ABORT	对Java的数组不进行更新,释放C/C++ 数组

GetPrimitiveArrayCritical  (类似于 GetStringCritical )
ReleasePrimitiveArrayCritical

Get<TYPE>ArrayRegion  //(类似于 GetStringRegion )在C/C++中开新的内存,把Java端复制过来
Set<TYPE>ArrayRegion
New<TYPE>Array()


Get/SetObjectArrayElement  不用进行释放资源

--引用
1.局部引用,NewLocalRef(),会在native方法调用完成后自动释放,也可用 DeleteLocalRef() ,在C中不要把使用全局/static变量保存

2.全局引用,可以在多个native,跨多个线程,可能被java垃圾回收  函数中使用
env->NewGlobalRef()
env->DeleteGlobalRef();

3.弱全局引用,不会阻止垃圾回收器,回收这个引用所指向的对象
NewWeakGlobalRef()
DeleteWeakGlobalRef()

IsSameObject(jobject ,jobjcet)//两个引用是否指向同一个java对象


缓存jfieldID ,jmethodID,因为使用签名开销很大
1.第一次使用时缓存,C中使用static 声明变更做,//多线程不会有危险,因为返回的总是相同的
2.java 初始化时缓存,java static块中调用native


JNI 对Java的异常处理
JNI 对Java的多线程
C/C++ 如何改 JVM参数
---------------JConsole
端口: 12345($JAVA_ARGS中-Dcom.sun.management.jmxremote.port指定的端口)
用户名: xxx (jmxremote.password中指定的用户名)
密码: xxx(jmxremote.password中设置的密码)

hostname -i 指向正确的IP

1)被监控的服务器端增加启动参数
-Dcom.sun.management.jmxremote.port=8999
-Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false

如果不需要用户验证，不用输入用户名密码

Tomcat 中加JConsole 的catalina.sh:
JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9004 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"


还可以使用service:jmx:<protocol>:<sap>


=====================JMX=====================Java Management Extensions 
JConsole 是一个基于JMX的，用于连接正在运行的JVM，需要使用可管理的模式启动。
jvisualvm  还有工具 calibration	 标度

JMX client (mc4j 最后更新是/jconsole)

JDK 也有自动示例 \jdk-7u5-windows-x64-demos\jdk1.7.0_05\sample\jmx\jmx-scandir
http://download.java.net/jdk8/docs/technotes/guides/jmx/examples.html  有示例代码
在tutorial\tutorial\jmx\examples也有
http://download.java.net/jdk8/docs/technotes/guides/jmx/tutorial/tutorialTOC.html

tomcat 7 对JMX的支持 tomcat-users.xml 中加配置 <role rolename="manager-jmx"/>
Tomcat 7 仿问http://127.0.0.1:8080  ->Server Statas按钮->Complete Server Status
	在下面的列表中有 JMXProxy [ /jmxproxy/* ]  表示是一个 Servlet的配置(manager项目中的web.xml也可以看到)  */
JConsole 监视 tomcat 在MBean标签可以看到User->User和Role是tomcat-users.xml中的配置
JConsole 的选项"远程进程" 在JDK7的提示 用法 <hostname>:<port> 或 service:jmx:<protocol>:<sap>

Service Location Protocol (SLP)
Java Secure Socket Extension (JSSE)
Java Authentication and Authorization Service (JAAS)

JConsole为建立连接，需要在环境变量中设置mx.remote.credentials来指定用户名和密码从而进行授权。
java.lang.management.MemoryMXBean 接口
MBean 标签->Memomery->操作->可以调用gc ，对应 MemoryMXBean 接口

javax.management.ObjectName 类 定义MBean对象名字

//JMX有四种类型的MBean ,standard MBeans, dynamic MBeans, open MBeans and model MBeans.
//---------------standard MBean 
import java.lang.management.ManagementFactory
import javax.management.MBeanServer;  
import javax.management.MBeanServerFactory;  
import javax.management.ObjectName;  
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();//如果没有会自动 调用  MBeanServerFactory.createMBeanServer()来创建
ObjectName name = new ObjectName("com.example.mbeans:type=Hello");//:前面的部分是jconsole中显示的文件夹名(叫domain),type叫key-properties,Hello是显示在文件夹下的名字
//standard MBean 的接口名要以MBean结尾 ,如HelloMBean ,而实现类也必须是没有MBean 即Hello
//ObjectName的API有key 的说明,,是immutable的,domain中不能有"//"
mbs.registerMBean(mbean, name);//mbean是Hello实例
//所有的public属性(getter)和方法会在jconsole中看到,如果有public属性(setter)和方法,jconsole中可以修改属性值和调用方法

//------------发送 Notification
//MBean也能发通知,必须实现 NotificationBroadcaster 接口 或者 子接口 NotificationEmitter 或实现类 NotificationBroadcasterSupport 
//NotificationBroadcasterSupport.sendNotification(可以是Notification或子类AttributeChangedNotification)

DateFormat format=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);
//String x=format.format(new Date());
//System.out.println(x);//2012-8-15 11:46:19
Notification n =new AttributeChangeNotification(this,//哪个对象的属性
					    sequenceNumber++,//顺序号,显示在jconsole中[序列号]的数据列
					    System.currentTimeMillis(),//显示在jconsole中[时间截]的数据列
					    "CacheSize属性改变了", //消息
					    "CacheSize", //属性,getCacheSize()
					    "int",//属性类型
					    oldSize,//属性改变前的旧值
					    this.cacheSize);//新值 
//在 setXxx 中使用sendNotification(n);
覆盖NotificationBroadcasterSupport的 getNotificationInfo()//返回值显示在 Jconsole 中,
{	
	String[] types = new String[] {AttributeChangeNotification.ATTRIBUTE_CHANGE};//ATTRIBUTE_CHANGE常量常值是jmx.attribute.change
	MBeanNotificationInfo info = new MBeanNotificationInfo(types, "显示在树中的名字", "描述");
	return new MBeanNotificationInfo[] {info};
}
//jconsole的类中会多一个[通知],点Subscriber(对所有的通知),如果有属性发改变,[通知]中会显示发出的消息,有一个[序列号]的数据列

//------------MXBean 可被远程客户端使用
接口名叫SomethingMXBean ,和MBean不同实现类名可以不叫Something
使用@MXBean 的接口就用以MXBean做结尾了 ,即javax.management.MXBean是@

java.lang.management.MemoryUsage ,不是在javax包下的
java.lang.management.MemoryMXBean
javax.management.openmbean. 包

//get开头方法返回的是类,JConsole 中[属性]显示 javax.management.openmbean.CompositeDataSupport,是复杂数据类型,可双击展开
//如是MBean就显示为不可用

复杂对象的构造方法前加
@ConstructorProperties({"date", "size", "head"})//是为代码客户端使用的

//--不使用JConsole 而写程序调用服务端,未成功?????  ,可能是MBeanServer 得到的方式有问题,可能要连接才行,jconsole是这样的
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
ObjectName name = new ObjectName("com.example.mxbeans:type=QueueSampler"); //实现接口MXBean类
CompositeData queueSample = (CompositeData) mbs.getAttribute(name, "QueueSample"); //复杂类型,getXxx();
int size = (Integer) queueSample.get("size"); //getSize();
或者用代理
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
ObjectName name = new ObjectName("com.example.mxbeans:type=QueueSampler"); 
QueueSamplerMXBean proxy = JMX.newMXBeanProxy(mbs, name, QueueSamplerMXBean.class); //接口
QueueSample queueSample = proxy.getQueueSample(); 
int size = queueSample.getSize(); 


//---------------Description
在JDK6中只有Model MBean可以支持Description

MBean*Info 类和 OpenMBean*InfoSupport  类都有 getDescriptor() 

OpenMBeanParameterInfo 和 OpenMBeanAttributeInfo

javax.management.DescriptorKey;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {   //自已建立一个annotation
    @DescriptorKey("displayName")//jconsole中[描述符]中会新增加属性名 
    String value();
}

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    @DescriptorKey("author")
    String value();
}

@Author("Mr Bean")//在jconsole 类级的 [描述符]中会新增加属性author,原因是配置 @DescriptorKey("author")
public interface QueueSamplerMXBean {
    @DisplayName("GETTER: QueueSample")//在jconsole 类级的->[属性](因getter)-> [描述符]中会新增加属性displayName,原因是配置 @DescriptorKey("displayName")
    public QueueSample getQueueSample();
}

//-------------------连接  dynamic MBeans
//基于RMI,支持 Standard RMI transports, Java Remote Method Protocol (JRMP) and the Internet Inter-Object Request Broker (ORB) Protocol (IIOP).
//动态Bean 会在运行时,reveals 属性和方法
//Server
MBeanServer mbs = MBeanServerFactory.createMBeanServer();//MBeanServer创建方式不同以前
mbs.getDefaultDomain();//值是DefaultDomain
ObjectName mbeanObjectName =ObjectName.getInstance(mbs.getDefaultDomain() + ":type=" + mbeanClassName + ",name=1"; );//不是new的试,name 的值只是个标识
mbs.createMBean(mbeanClassName, mbeanObjectName);//mbeanClassName要是可以找到的类"包.类名",不是registerMBean了,先实例化再register

MBeanInfo info = mbs.getMBeanInfo(mbeanObjectName);
info.getClassName();
info.getDescription();
MBeanAttributeInfo[] attrInfo = info.getAttributes();
	attrInfo[i].getName();
	attrInfo[i].getDescription();
	attrInfo[i].getType();
	attrInfo[i].isReadable();
	attrInfo[i].isWritable();
MBeanConstructorInfo[] constrInfo = info.getConstructors();
	constrInfo[i].getSignature();
MBeanOperationInfo[] opInfo = info.getOperations();
MBeanNotificationInfo[] notifInfo = info.getNotifications();//这个返回信息是重写getNotificationInfo()的返回值
	String notifTypes[]=notifInfo[i].getNotifTypes();


mbs.getAttribute(mbeanObjectName, "State");//开头第一个是大写的,对应getState()
mbs.setAttribute(mbeanObjectName, new Attribute("State","new state"));//对setState(x)修改值 
	String name = attribute.getName(); 
	Object value = attribute.getValue();
	AttributeList

mbs.invoke(mbeanObjectName, "reset", null, null);//调用reset()方法

implements DynamicMBean 接口有getAttributes,setAttributes
			invoke:		 在 mbs.invoke调用时会调用此方法
			setAttribute: 在mbs.setAttribute调用时会调用此方法
			getAttribute: 在mbs.getAttribute调用时会调用此方法
			getMBeanInfo : 在createMBean()方法调用 和mbs.getMBeanInfo()方法调用,会调用此方法 . 暴露 属性,方法,通知
		
new MBeanInfo(this.getClass().getName(), //哪个类的
			   "",//描述
			   MBeanAttributeInfo[],
			   MBeanConstructorInfo[],
			   MBeanOperationInfo[],
			  MBeanNotificationInfo[]);//MBeanInfo构造时传了很其它的MBean*Info

//--启动 JMX Server 依赖于RMI
//JMXConnectorServer start()之前要调用 rmiregistry 9999 &
JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");//RMI OK

//JMXConnectorServer start()之前要 orbd -ORBInitialPort 7777 & JDK自带命令,会在当前目录生成文件orb.db目录,,第二次测试时要删除目录
JMXServiceURL url = new JMXServiceURL( "service:jmx:iiop:///jndi/iiop://localhost:7777/server");//CORBA OK
           
JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
//start()之前要 rmiregistry 9999 &  才能成功
cs.start();
...
cs.stop();

//--client
//JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");//RMI OK
JMXServiceURL url = new JMXServiceURL( "service:jmx:iiop:///jndi/iiop://localhost:7777/server");//CORBA OK

//CORBA时client端提示要配置log4j为(org.jboss.logging)
//org.apache.log4j.PropertyConfigurator.configure("D:/Program/eclipse_java_workspace/J_JavaSE/src/jmx_examples/connector/log4j.properties");//OK
org.apache.log4j.xml.DOMConfigurator.configure("D:/Program/eclipse_java_workspace/J_JavaSE/src/jmx_examples/connector/log4j.xml");//OK
            

JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
String domains[] = mbsc.getDomains();//有 JMImplementation 和 efaultDomain
String domain = mbsc.getDefaultDomain();
mbsc.createMBean("包.类名",new ObjectName(""),Object[] params, String[] signature);
mbsc.getMBeanCount();
	//getAttribute(),setAttribute()
Set names = mbsc.queryNames(null, null);//每个是(ObjectName) i.next());

ObjectName stdMBeanName =new ObjectName(mbsc.getDefaultDomain()+":type=SimpleStandard,name=2");//name提供一个相同type的另一个标识,为createMBean(),参数同getInstance();
mbsc.getAttribute(stdMBeanName, "State")//和JMX.newMBeanProxy效果一样
mbsc.setAttribute(stdMBeanName)
//client使用proxy方式
SimpleStandardMBean proxy=JMX.newMBeanProxy(mbsc, stdMBeanName, SimpleStandardMBean.class, true);//和mbsc.getAttribute效果一样

mbsc.addNotificationListener(stdMBeanName, listener, null, null);//listener是implements NotificationListener
mbsc.invoke(stdMBeanName, "reset", null, null);//reset中有sendNotification(acn);用于测试 NotificationListener
mbsc.removeNotificationListener(stdMBeanName, listener);

mbsc.unregisterMBean(stdMBeanName);
jmxc.close();

//-------------------查找服务

RMI使用下列三种外部目录
	1. RMI Registry 使用 Java Remote Method Protocol (JRMP) 
			$ rmiregistry  9999 &命令 , 示例 "rmi://localhost:9999"
			JDK API 在 javax.management.remote.rmi 下有说明, RMI stub是一个 RMIServer 类型的对象,远程连接使用
			external directory 是可以被JNDI识别的
	2. CORBA Naming Service ,RMI使用使用默认的 Internet Inter-ORB Protocol (IIOP)
		Object Request Broker (ORB)
		$ orbd -ORBInitialPort 7777 & JDK自带命令,会在当前目录生成文件orb.db目录 ,示例 "iiop://localhost:7777"
	3. LDAP 可以传IIOP和JRMP
		示例 "ldap://127.0.0.1:10389"

	
java -Dx=y  My //可以使用System.getProperty("x");
//----LDAP server
import javax.naming.directory.DirContext;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

//参见Java第三方库中的LDAP知识 和  ApacheDS

file->import-> LDIF into LDAP   ,  或者右击xxx->import 
//右击Root DSE->new Context Entry->选择create entry from scratch->在obejctClass中选择dcObject,organization->下拉选择已存在的dc=example,dc=com->输入o属性值->finish
HashMap env = new HashMap();//或者使用Properties,或者把这些写入jni.properties文件中,再去读
env.put(RMIConnectorServer.JNDI_REBIND_ATTRIBUTE,"true");//JMX专用的,值可是"true"
env.put(Context.INITIAL_CONTEXT_FACTORY,		"com.sun.jndi.ldap.LdapCtxFactory");//LDAP的JNDI
env.put(Context.PROVIDER_URL,					"ldap://127.0.0.1:10389/dc=example,dc=com");
env.put(Context.SECURITY_PRINCIPAL,				"uid=admin,ou=system");//apacheDS 默认自带的用户
env.put(Context.SECURITY_CREDENTIALS,			"secret");//apacheDS 默认密码

JMXServiceURL jurl=new JMXServiceURL("service:jmx:rmi:///jndi/ldap://127.0.0.1:10389/dc=example,dc=com");//可以和 PROVIDER_URL 做连接
									//或者用 service:jmx:iiop:///jndi/ldap://127.0.0.1:10389/dc=example,dc=com"//IIOP也可以连接
"service:jmx:rmi://"
"service:jmx:rmi:///jndi/rmi://localhost:9999/server"
"service:jmx:rmi:///jndi/ldap://localhost:10389/cn=x,dc=Test" 
"service:jmx:iiop://"
"service:jmx:iiop:///jndi/iiop://localhost:7777/server"
"service:jmx:iiop:///jndi/ldap://localhost:10389/cn=x,dc=Test" 

jurl.getProtocol()//返回rmi或者iop

JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(jurl, env, mbs);//mbs=MBeanServerFactory.createMBeanServer()
server.start();//只可在叶子节点上
JMXServiceURL address = server.getAddress();

InitialContext root = new InitialLdapContext(env,null);
//DirContext dirContext=(DirContext)(root.lookup(""));//tutorial中是这种,如果Directory Studio建立的是Context Entry测试不行的
DirContext dirContext=root;//OK

Attributes attrs = new BasicAttributes();

Attribute attr = new BasicAttribute("objectclass");//不区分大小写的,objectClass
attr.add("top");//所有的objectClass必须是已经在LADP服务器上存在的,可以选属性,Direcotry Studio工具都可以做
attr.add("person");
attr.add("organizationalPerson");
attr.add("inetOrgPerson");//inetOrgPerson 继承自organizationalPerson 继承自 person继承自top

attrs.put(attr);
attrs.put("cn", "zh");//cn和sn属性  是inetOrgPerson必须的,显示为粗体
attrs.put("sn", "li");
attrs.put("userpassword", "123456");//不是粗体,表示可选属性,是userPassword 的可选属性

dirContext.createSubcontext("cn=zh",attrs);//创建节点,cn=zh是显示的目录名,或cn=zh,ou=Account(前提是ou=Account要已存在)
dirContext.getAttributes("cn=zh")//就是attrs

Attributes newattrs = new BasicAttributes();
newattrs.put("...","...");
dirContext.modifyAttributes("cn=zh",DirContext.REPLACE_ATTRIBUTE,newattrs);//属性操作
					//DirContext.REPLACE_ATTRIBUTE
					//DirContext.ADD_ATTRIBUTE
					//DirContext.REMOVE_ATTRIBUTE


//LDAP client 
SearchControls ctrls = new SearchControls();
ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
					// SUBTREE_SCOPE: 子树中查找
					// ONLEVEL_SCOPE: 节点下的所有直接子节点
					// OBJECT_SCOPE:  返回指定对象
NamingEnumeration<SearchResult> results=dirContext.search("ou=Account", "(&(a=b))", ctrls);

 while (results.hasMore()) 
{
	SearchResult r = (SearchResult) results.nextElement();
	r.getName();
	r.getAttributes();
}
JMXConnector c1 = JMXConnectorFactory.newJMXConnector(new JMXServiceURL("..."),null);
c1.connect(env);
MBeanServerConnection conn =  c1.getMBeanServerConnection();//和前面的一样
conn.getDefaultDomain();
Set<ObjectName> names = conn.queryNames(null,null);
//...可以做遍历
c1.close();

//删除节点
dirContext.destroySubcontext("cn=zh");
//--Corba

//------Jini 没用的
//------JMX security 
//--simple 测试失败
//Server
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

HashMap env = new HashMap();
SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE,csf);
env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE,ssf);

env.put("jmx.remote.x.password.file",  root+"/password.properties"); //password.properties文件中只有一行  username password


//对于mx.remote.x.password.file的另一种方法是,未测试????
//env.put(JMXConnectorServer.AUTHENTICATOR, JMXAuthenticator 的实现);//AUTHENTICATOR="jmx.remote.authenticator" 
 env.put(JMXConnectorServer.AUTHENTICATOR, new JMXAuthenticator(){
				public Subject authenticate(Object credentials) {
					return null;
				}
            });

env.put("jmx.remote.x.access.file", root+"/access.properties"); //使用 MBeanServerForwarder 接口实现
//access.properties文件中只有一行  username readwrite

System.setProperty ("javax.net.ssl.keyStore", root+"/keystore");//使用keytool -genkey生成的文件,相当于 java -Dx=y
System.setProperty ("javax.net.ssl.keyStorePassword","password");//仿问keystore文件的密码
			
JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
MBeanServer mbs = MBeanServerFactory.createMBeanServer();
JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
cs.start();//之前要rmiregistry 9999,最好在正确的classpath下

//client
String[] credentials = new String[] { "username" , "password" };//是已经在服务端有的用户密码
env.put("jmx.remote.credentials", credentials);//JMXConnector.CREDENTIALS=jmx.remote.credentials

System.setProperty ("javax.net.ssl.trustStore", root+"/truststore");//使用keytool -import 生成的文件
System.setProperty ("javax.net.ssl.trustStorePassword", "trustword");//仿问truststore文件的密码
JMXServiceURL url = new JMXServiceURL( "service:jmx:rmi:///jndi/rmi://localhost:9999/server");

//connect时要配置log4j为(org.jboss.logging)
JMXConnector jmxc = JMXConnectorFactory.connect(url, env);//这里总是过不去????

//---subject 测试失败
//Server
System.setProperty ("java.security.policy", root+"/java.policy");//相当于 java -Dx=y

env.put("jmx.remote.x.password.file",  root + "/password.properties");//password.properties文件中只有一行  username password
env.put("jmx.remote.x.access.file",root+"/access.properties");
//access.properties文件中有两行
//username readwrite
//delegate readwrite

JMXServiceURL url = new JMXServiceURL( "service:jmx:rmi:///jndi/rmi://localhost:9999/server");
JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
cs.start();//之前要rmiregistry 9999,最好在正确的classpath下

//java.policy文件
grant codeBase "file:D:/program/eclipse_java_workspace/J_JavaSE/bin" {
//grant codeBase "file:server" {
    permission javax.management.remote.SubjectDelegationPermission "javax.management.remote.JMXPrincipal.delegate";
};
grant principal javax.management.remote.JMXPrincipal "username" {
    // Grant the JMXPrincipal "username" the right to act on behalf of a JMXPrincipal "delegate".
    permission javax.management.remote.SubjectDelegationPermission "javax.management.remote.JMXPrincipal.delegate";
};

//Client
import javax.security.auth.Subject;

String[] credentials = new String[] { "username" , "password" };
env.put("jmx.remote.credentials", credentials);//JMXConnector.CREDENTIALS=jmx.remote.credentials
JMXServiceURL url = new JMXServiceURL(  "service:jmx:rmi:///jndi/rmi://localhost:9999/server");
JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
Subject delegationSubject = new Subject(true,//readOnly
			Collections.singleton(new JMXPrincipal("delegate")),//principals  用户名
			Collections.EMPTY_SET,
			Collections.EMPTY_SET);
MBeanServerConnection mbsc =  jmxc.getMBeanServerConnection(delegationSubject);
mbsc.createMBean("jmx_examples.security_subject.SimpleStandard", //这里报错???
//---fine gain

==================================policy文件 
Java Authentication and Authorization Service (JAAS)
Authentication 认证 ->Authorization 授权

http://download.java.net/jdk8/docs/technotes/guides/security/index.html


1. 操作系统级的policy文件 java.home\lib\security\java.policy   //System.getProperty("java.home");
2. 用户级policy文件文件,  user.home\.java.policy
JRE内置的policy, java.home\lib\security\java.security 文件内容 policy.url.n配置了上面的加载顺序,也配置了security.provider.1

keystore 是存private key,格式
	keystore "some_keystore_url", "keystore_type", "keystore_provider";
	keystorePasswordURL "some_password_url"; 
	//keystore和keystorePasswordURL,只可指定一个

	keystore "文件路径是相对于policy路径的"
	keystore_type默认是"JKS"
grant 语法
  grant signedBy "signer_names", codeBase "URL",
        principal principal_class_name "principal_name",
        principal principal_class_name "principal_name",
     {

      permission permission_class_name "target_name", "action", 
          signedBy "signer_names";
      permission permission_class_name "target_name", "action", 
          signedBy "signer_names";
      ...
	};
 codeBase, signedBy,principal 是选的,出现顺序无关

signedBy 的值是keystore的别名,逗号分开多个别名
target_name 可以直接指定类名（可以是绝对或相对路径），目录名，也可以是下面的通配符： 
	directory/*    当前目录的所有文件  */
	directory/-目录下的所有文件，包括子目录
 principal_class_name  可以使用通配符*
示例
permission java.io.FilePermission 		"-", "read";
permission java.io.FilePermission 		"${user.home}", "read";
permission java.util.PropertyPermission "user.dir", "read";
permission java.lang.RuntimePermission 	"modifyThread";
permission java.net.SocketPermission	 "-", 	"connect";

grant principal javax.security.auth.x500.X500Principal * {
      permission java.io.FilePermission "/tmp", "read, write";
  };

  
keystore "http://foo.example.com/blah/.keystore";
grant principal "alice" {//如果只一个串,会认为是keystore的别名,设keystore有X509证书,DN(distinguished name)为"cn=Alice", alice"会自动变为 javax.security.auth.x500.X500Principal "cn=Alice",如果找不到X509证书就忽略这项
      permission java.io.FilePermission "/tmp/games", "read, write";
  };


keystore "http://foo.example.com/blah/.keystore";
grant principal "duke" {//会自动变为 javax.security.auth.x500.X500Principal "cn=Duke",设duke是keystore的别名
    permission BarPermission "... ${{self}} ...";
};
 //${{self}}只对有 principle 的 ,${{self}}会自动变为 javax.security.auth.x500.X500Principal "cn=Duke"

 
keystore "http://foo.example.com/blah/.keystore";
grant codebase "www.example.com" {
    permission BarPermission "... ${{alias:duke}} ...";
};
//假设duke 的证书 -dname 设置为"o=dukeOrg, cn=duke",那么${{alias:duke}} 会自动变为 javax.security.auth.x500.X500Principal "o=dukeOrg, cn=duke". 


---------------SSL ,keytool命令
JKS(JavaKeysotre)格式  和  PFX/p12(PKCS12) 
PFX常用于Windows IIS服务器 ,JKS常用语JAVA类的WEB服务器

Certificate Authorities=CA 是由第三方机构下发的，可以对私钥签名附加上机构信息。浏览器就可以在https协议中通过该证书信任第三分站点。
Certificate Signing Request (CSR)
 
根据私钥pfx生成公钥crt
openssl pkcs12 -in myssl.pfx -nodes -out server.pem (像Base64的明文)
openssl rsa -in server.pem -out server.key
openssl x509 -in server.pem -out server.crt (.crt是像Base64的明文的公钥文件)

1.创建根证私钥
openssl genrsa -out root-key.key 1024

2.创建根证书请求文件
openssl req -new -out root-req.csr -key root-key.key -keyform PEM    (对-key参数,默认PEM,可选DER)  
#有交互Country Name(C) ,State or Province(ST) ,City,Organization(O),Organizational Unit( ),Common Name(CN),Email , 最小4位challenge password,company name

3.自签根证书
openssl x509 -req -in root-req.csr -out root-cert.cer -signkey root-key.key -CAcreateserial -days 3650   
(-signkey  self signed , .cer 是像Base64的明文的公钥文件)

4.导出p12格式根证书
openssl pkcs12 -export -clcerts -in root-cert.cer -inkey root-key.key -out root.p12   
#输入密码,有确认的
#-clcerts only output client certificates (not CA certificates)


5.生成root.jks文件
keytool -import -v -trustcacerts -storepass 123456 -alias root -file root-cert.cer -keystore root.jks 


如要二制的.cer文件,要转换pem到der
openssl x509 -in root-cert.cer -inform PEM -out root-cert_b.cer -outform DER 

查看证书内容
openssl x509 -noout -text -in root-cert.cer
openssl rsa -noout -text -in root-key.key
----------------方式二
1) 创建私钥
openssl genrsa -out private_key.pem 1024  (像Base64的明文)

2) 创建证书
openssl req -new -out req.csr -key private_key.pem  -keyform PEM  (对-key参数,默认PEM,可选DER)  
#有交互Country Name ,Province ,City,Organization,Organizational Unit,Common Name,Email , 最小4位challenge password,company name

3) 自签署证书
openssl x509 -req -in req.csr -out public_key.der -outform der -signkey private_key.pem -days 3650

以上三步也可简化为一个命令
openssl req -x509 -out public_key.der -outform der -new -newkey rsa:1024 -keyout private_key.pem -days 3650 
----


/etc/pki/tls/openssl.cnf 中默认配置
	dir   = /etc/pki/CA
	database        = $dir/index.tx
	erial          = $dir/serial
	
touch /etc/pki/CA/index.txt
echo 01 > /etc/pki/CA/serial

openssl genrsa -des3 -out /etc/pki/CA/private/cakey.pem 2048					 要设置密码,以后用就要输入 
openssl req -new -days 365 -key  /etc/pki/CA/private/cakey.pem -out careq.pem    要输入很多CN等
openssl ca -selfsign -in careq.pem -out cacert.pem 								 要cakey.pem的密码 , 提示输y
# 以上两步可以合二为一
openssl req -new -x509 -days 365 -key  /etc/pki/CA/private/cakey.pem -out cacert.pem
  
  
public static boolean verifyCert(X509Certificate userCert, X509Certificate rootCert)throws Exception
{
	PublicKey rootKey = rootCert.getPublicKey();
	userCert.checkValidity();
	userCert.verify(rootKey);
	if (!userCert.getIssuerDN().equals(rootCert.getSubjectDN()))
		return false;
	boolean isNotExpire =  new java.util.Date().before(userCert.getNotAfter());
	return isNotExpire;
}




jdk-8u20-docs-all\docs\technotes\guides\security\jsse\JSSERefGuide.html

Java Secure Socket Extension (JSSE)
Secure Sockets Layer (SSL)
Transport Layer Security (TLS)
IETF 已经把SSL更名为TLS ,TLS 1.0 表示 SSL 3.1

//keytool -help 显示所有的command
//keytool -genkeypair -help 显示command对应的option(可缩写为-genkey)
//keystore可以理解为一个数据库,仿问数据库要密码,库中可存放很多用户和密码

keytool -genkey -alias server -keyalg RSA -keystore C:/temp/serverKeystore   //全称-genkeypair,可加-keyalg RSA
//少选项会交互提示输入一些信息
Enter keystore password:				//如 serverkeystorepass
Re-enter new password:
What is your first and last name?
  [Unknown]:  zh
What is the name of your organizational unit?
  [Unknown]:  TCS
What is the name of your organization?
  [Unknown]:  TATA
What is the name of your City or Locality?
  [Unknown]:  Harbin
What is the name of your State or Province?
  [Unknown]:  HeiLongJian
What is the two-letter country code for this unit?
  [Unknown]:  CN
Is CN=zh, OU=TCS, O=TATA, L=Harbin, ST=HeiLongJian, C=CN correct?  
//CN=Common name, OU=organizational unit,o=organization,L=Locality,ST=state,C=country,
还要  key password			//如 serverkeypass

//全的参数,就不提示了
还可以在同一个keystore文件中再加其它的alias,会要输入keystore密码才可增加,可以有不同的key password
keytool -genkey -alias lisi     -keystore C:/temp/clientKeystore.jks -dname "CN=lisi,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN"     -keypass lisikeypass     -storepass clientkeystorepass  -keyalg RSA  -sigalg SHA1withRSA
keytool -genkey -alias zhangsan -keystore C:/temp/clientKeystore.jks -dname "CN=zhangsan,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN" -keypass zhangsankeypass -storepass clientkeystorepass  -keyalg RSA  -sigalg SHA1withRSA
keytool -genkey -alias server   -keystore C:/temp/serverKeystore.jks -dname "CN=server,OU=tcs,O=tata,L=Harbin,ST=HeiLongJian,C=CN"   -keypass serverkeypass -storepass serverkeystorepass    -keyalg RSA  -sigalg SHA1withRSA

keytool -list -v -keystore C:/temp/clientKeystore.jks  -storepass clientkeystorepass //可以看到有两个别名(Keystore type: JKS,每个叫entry)

keytool -export -alias server   -keystore C:/temp/serverKeystore.jks -file c:/temp/server.cer   -storepass serverkeystorepass //全称是-exportcert,要 keystore密码,生成.cer文件
keytool -export -alias lisi     -keystore C:/temp/clientKeystore.jks -file c:/temp/lisi.cer     -storepass clientkeystorepass
keytool -export -alias zhangsan -keystore C:/temp/clientKeystore.jks -file c:/temp/zhangsan.cer -storepass clientkeystorepass

keytool -import -alias server   -keystore C:/temp/clientTruststore.jks -file c:/temp/server.cer   -storepass clienttruststorepass -noprompt //在客户端文件 trust(加) 服务器 
//使用生成的.cer文件(对应其它中的alias),生成一个keystore文件,要新的keystore password,提示Trust this certificate?
keytool -import -alias lisi     -keystore C:/temp/serverTruststore.jks -file c:/temp/lisi.cer     -storepass servertruststorepass -noprompt//在服务器端文件 trust(加) 客户端
keytool -import -alias zhangsan -keystore C:/temp/serverTruststore.jks -file c:/temp/zhangsan.cer -storepass servertruststorepass -noprompt//使用相同文件,要keystore密码
//可参考JavaEE中weblogic中使用keytool

转换 pfx 到 jks 
keytool -importkeystore -v  -srckeystore  C:\_work\mpmt\BIS密钥\privateKey.pfx -srcstorepass paic1234 -srcstoretype pkcs12  -deststoretype jks  -destkeystore dest.jks   -deststorepass dest1234
keytool -list -v -keystore dest.jks  -storepass dest1234 


//启动服务器时 -D
System.setProperty("javax.net.ssl.keyStore", 			root+"/serverKeystore.jks");
System.setProperty("javax.net.ssl.keyStorePassword",	"serverkeystorepass");//是自己设的密码
System.setProperty("javax.net.ssl.trustStore",			root+"/serverTruststore.jks");
System.setProperty("javax.net.ssl.trustStorePassword",	"servertruststorepass");

//启动客户端时 -D
System.setProperty("javax.net.ssl.keyStore", 			root+"/clientKeystore.jks");
System.setProperty("javax.net.ssl.keyStorePassword",	"clientkeystorepass");
System.setProperty("javax.net.ssl.trustStore",			root+"/clientTruststore.jks");
System.setProperty("javax.net.ssl.trustStorePassword",	"clienttruststorepass");


KeyManagerFactory	//对应keyStore
TrustManagerFactory //对应trustStroe


SSLContext sslContext= SSLContext.getInstance("TLS");//SSL
SSLContext.setDefault(sslContext); 

// First initialize the key and trust material.
KeyStore ksKeys = KeyStore.getInstance("JKS");
ksKeys.load(new FileInputStream(root+"/serverKeystore"), "serverkeystorepass".toCharArray());

KeyStore ksTrust = KeyStore.getInstance("JKS");
ksTrust.load(new FileInputStream(root+"/serverTruststore"), "servertruststorepass".toCharArray());
// KeyManager's decide which key material to use.
KeyManagerFactory kmf =  KeyManagerFactory.getInstance("SunX509");//KeyManagerFactory.getDefaultAlgorithm();
kmf.init(ksKeys, "serverkeypass".toCharArray());

// TrustManager's decide whether to allow connections.
TrustManagerFactory tmf =  TrustManagerFactory.getInstance("SunX509");
tmf.init(ksTrust);
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

SSLEngine engine = sslContext.createSSLEngine("127.0.0.1",4567);//SSLContext得到SSLEngine
engine.setUseClientMode(true);
SSLSession session=engine.getSession();//SSLEngine 得到 SSLSession

SSLSocketFactory socketFactory=	sslContext.getSocketFactory();//SSLContext得到SSLSocketFactory
SSLSocket soket=(SSLSocket)socketFactory.createSocket();
SSLSession session1=soket.getSession();//SSLSocket 得到 SSLSession

engine.setUseClientMode(false);
SSLServerSocketFactory serverSocketFactory= sslContext.getServerSocketFactory();//SSLContext得到SSLServerSocketFactory
SSLServerSocket serverSocket= (SSLServerSocket)serverSocketFactory.createServerSocket();

SSLServerSocketFactory serverFactory2= (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
//先读ssl.ServerSocketFactory.provider属性,没用则会自动调用SSLContext.getDefault().getServerSocketFactory(). 

SSLEngine的API中的图,要发送到网络的数据要wrap一些ssl东西,收到网络的数据要unwrap

ByteBuffer myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
ByteBuffer myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
engine.beginHandshake();
SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
if(hs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)//NEED_WRAP,NEED_TASK
{
	SSLEngineResult r = engine.unwrap(myNetData, myAppData);
	if(r.getStatus() == SSLEngineResult.Status.OK ) //BUFFER_UNDERFLOW(source) ,BUFFER_OVERFLOW(destination不够大)
	{}
}else if (hs == SSLEngineResult.HandshakeStatus.NEED_TASK) 
{
	Runnable task=engine.getDelegatedTask();
	new Thread(task).start();
}

//有 HttpsURLConnection

CertPathParameters pkixParams = new PKIXBuilderParameters(someKeystore,  new X509CertSelector());
ManagerFactoryParameters trustParams =  new CertPathTrustManagerParameters(pkixParams);
TrustManagerFactory factory = TrustManagerFactory.getInstance("PKIX");//PKIX 算法
factory.init(trustParams);



---------------------------------oracle JDBC
双机模式的jdbc驱动
jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=OLTP_A-vip)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=OLTP_B-vip)(PORT=1521))(LOAD_BALANCE=on)(FAILOVER=on))(CONNECT_DATA=(SERVICE_NAME=bst)))  
jdbc驱动是ojdbc6.jar

//如oracle　的某表有数据改动，通知JDBC
OracleDriver dr = new OracleDriver();
Properties prop = new Properties();
prop.setProperty("user","scott");
prop.setProperty("password","tiger");
//@后只可这样，对双机模式的jdbc驱动
OracleConnection con　 = (OracleConnection)dr.connect("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=xe)))",prop);


//
OracleDataSource ods = new OracleDataSource();
ods.setUser("hr");
ods.setPassword("hr");
//ods.setURL("jdbc:oracle:thin:@l27.0.0.1:1521:xe");
ods.setDriverType("oci8");//放对应数据库版本的.jar包,10.2XE的ojdbc4.jar找ocijdbc10.dll文件，要加-Djava.library.path=C:\oraclexe\app\oracle\product\10.2.0\server\BIN
ods.setNetworkProtocol("ipc");//可不加setURL();

//OCI连接池
OracleOCIConnectionPool cpool = new OracleOCIConnectionPool("hr", "hr", url, null);
Properties p  = new Properties();
p.put (OracleOCIConnectionPool.CONNPOOL_MIN_LIMIT, Integer.toString(cpool.getMinLimit()));
p.put (OracleOCIConnectionPool.CONNPOOL_MAX_LIMIT,Integer.toString(cpool.getMaxLimit() * 2)) ;
p.put (OracleOCIConnectionPool.CONNPOOL_INCREMENT,Integer.toString(cpool.getConnectionIncrement()));
cpool.setPoolConfig(p);
OracleOCIConnection conn1 = (OracleOCIConnection)cpool.getConnection("hr", "hr");

//thin 连接池
OracleConnectionPoolDataSource ocpds =new OracleConnectionPoolDataSource();
String url = "jdbc:oracle:oci8:@";
ocpds.setURL(url);
ocpds.setUser("hr");
ocpds.setPassword("hr");
PooledConnection pc  = ocpds.getPooledConnection();
Connection conn = pc.getConnection();
//


//-----NO 
 Properties prop = new Properties()
 prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS,"true");//可以得到 改变的ROWID

DatabaseChangeRegistration dcr = conn.registerDatabaseChangeNotification(prop);
dcr.addListener(implements DatabaseChangeListener);
((OracleStatement)stmt).setDatabaseChangeRegistration(dcr);


String[] tableNames = dcr.getTables(); //是dept，如它改变，会调用DatabaseChangeListener的方法

conn.unregisterDatabaseChangeNotification(dcr);

//如有其它地方改变,新connection
Statement stmt2 = conn2.createStatement();
stmt2.executeUpdate("insert into dept (deptno,dname) values ('45','cool dept')",Statement.RETURN_GENERATED_KEYS); //返回insert时的rownum
ResultSet autoGeneratedKey = stmt2.getGeneratedKeys();//得到ROWID
if(autoGeneratedKey.next())
  System.out.println("inserted one row with ROWID="+autoGeneratedKey.getString(1));  




//-------Oracle的数据源 ??????????????
String connect_string = "jdbc:oracle:thin:hr/hr@//localhost:1521/xe";　//也可把用户放在一起
OracleDataSource ods = new OracleDataSource();
ods.setURL(connect_string); 

ods.setPassword("tiger");// 也可单独设用户
ods.setUser("scott");
Connection conn = ods.getConnection ();


(DatabaseChangeEvent e )//DatabaseChangeListener接口方法中
{
QueryChangeDescription [] changes = e.getQueryChangeDescription();
QueryChangeDescription change = changes[0];
TableChangeDescription [] tableChanges = change.getTableChangeDescription();
TableChangeDescription tableChange = tableChanges[0];
RowChangeDescription[] rowChanges = tableChange.getRowChangeDescription();
RowChangeDescription rowChange = rowChanges[0];
oracle.sql.ROWID rowid = rowChange.getRowid();
((OraclePreparedStatement)getPstmt).setROWID( 1, rowid );
}
//-----NO 



//PersonRef Demo
/*
create type STUDENT as object (name VARCHAR (30), age NUMBER)
create table student_table of STUDENT
insert into student_table values ('John', 20);
*/

java.sql.Connection conn = ods.getConnection();//
ResultSet rs = stmt.executeQuery("select ref (s) from student_table s");
rs.next ();
REF ref = (REF) rs.getObject (1);
STRUCT student = (STRUCT) ref.getValue ();
Object attributes[] = student.getAttributes();//返回所有的值
((BigDecimal) attributes[1]).intValue();


//示例　
	stmt.execute ("create or replace package java_refcursor as " +
       " type myrctype is ref cursor return EMPLOYEES%ROWTYPE; " +
       " function job_listing (j varchar2, k varchar2) return myrctype; " +
       "end java_refcursor;");

      stmt.execute ("create or replace package body java_refcursor as " +
       " function job_listing (j varchar2, k varchar2) return myrctype is " +
       "   rc myrctype; " +
       " begin " +
       "   open rc for select * from employees where job_id = j or job_id = k;" +
       "   return rc; " +
       "  end; " +
       "end java_refcursor;");
CallableStatement call =conn.prepareCall ("{ ? = call java_refcursor.job_listing (?, ?)}");
call.registerOutParameter (1, OracleTypes.CURSOR);
call.setString (2, "SA_REP");
call.setString (3, "SA_MAN");
call.execute ();
ResultSet rset = (ResultSet)call.getObject (1);
while (rset.next ())
      System.out.println (rset.getString ("FIRST_NAME") + "  "
                          + rset.getString ("LAST_NAME") + "  "
                          + rset.getString ("JOB_ID"));
      


//JDBC 批量更新
PreparedStatement pre=x;
pre.setInt(1);
pre.addBatch();

pre.setInt(2);
pre.addBatch();
pre.executeBatch();//


JDBC  的Blob
先向oracle中insert 使用EMPTY_BLOB()函数,再select
oracle.sql.BLOB blob = (oracle.sql.BLOB)rs.getBlob("BLOBCOL");
blob.getBufferSize();

new BufferedInputStream(blob.getBinaryStream());
new BufferedOutputStream(blob.getBinaryOutputStream());


PreparedStatement
	setBlob(int parameterIndex, Blob x) 
	setBlob(int parameterIndex, InputStream inputStream)  
	prepare.setObject(1, new byte[]{1,2,3});// 1是Blob列,OK
	
	resultset.getBinaryStream("T_IMAGE");得到blob字段返回InputStream
         .getBlob(""或id ) 得到Ｂlob 再getBinaryStream();
				Blob.length()返回long 型		

//javax.sql.rowset.serial.SerialBlob  (byte[] b) //不行的 ,不能强转成oracle.sql.BLOB

java.sql.Blob imgBlob;//如要修改查询必须加锁
OutputStream writeStream=imgBlob.setBinaryStream(0);
InputStream readStream =imgBlob.getBinaryStream();

---------------------DES 加密，解密 1998 废止
//Security.addProvider(new com.sun.crypto.provider.SunJCE()); 
//Security.getProviders();
//-------随机生成 Key 密钥
//SecureRandom random = new SecureRandom();// SecureRandom.getInstance("SHA1PRNG");
//KeyGenerator keyGenerator = KeyGenerator.getInstance ("DES" ); 
//keyGenerator.init (random);
//SecretKey key = keyGenerator.generateKey();				
//------或者用    生成 Key 密钥
//DESKeySpec keySpec = new DESKeySpec ("12345678".getBytes("UTF-8"));//一定要是长度8
//SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES" );
//SecretKey key = keyFactory.generateSecret(keySpec);
//------或者用    生成 Key 密钥
Key key = new javax.crypto.spec.SecretKeySpec("12345678".getBytes("UTF-8"), "DES");//RC4 生成密钥,一定要是长度8的byte[]
//接口 SecretKey继承自接口Key
//--------下面是成功示例
Cipher en_cipher = Cipher.getInstance( "DES" ); //RC4
en_cipher.init( Cipher.ENCRYPT_MODE, key ); //加密
//当使用了SecureRandom时,用重载方法,即三个参数SecureRandom
byte[] en_byte=en_cipher.doFinal("helloWorld".getBytes("UTF-8"));

System.out.println("解码前:"+byteToHexString(en_byte));

Cipher de_cipher = Cipher.getInstance( "DES" );
de_cipher.init( Cipher.DECRYPT_MODE, key ); //解密
byte[] de_byte=de_cipher.doFinal(en_byte);
System.out.println("解码后:"+new String(de_byte));

---------------------AES 加密，解密  DES的取代者
 public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
	KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	kgen.init(128, new SecureRandom(encryptKey.getBytes()));  

	Cipher cipher = Cipher.getInstance("AES");  
	cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
	  
	return cipher.doFinal(content.getBytes("UTF-8"));  
}  
public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
	KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	kgen.init(128, new SecureRandom(decryptKey.getBytes()));  
	  
	Cipher cipher = Cipher.getInstance("AES");  
	cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
	byte[] decryptBytes = cipher.doFinal(encryptBytes);  
	  
	return new String(decryptBytes,"UTF-8");  
}  
String content = "我爱你";  
System.out.println("加密前：" + content);  
String key = "123456";  
System.out.println("加密密钥和解密密钥：" + key);  

Base64.Encoder base64Encoder=Base64.getEncoder();
byte[] encoded=base64Encoder.encode(aesEncryptToBytes(content, key));
System.out.println("加密后："+new String(encoded));

Base64.Decoder base64Decoder=Base64.getDecoder();
byte[] decoded=base64Decoder.decode(encoded);
System.out.println("解密后：" + aesDecryptByBytes(decoded, key));


-------------MD5

MessageDigest md5 = MessageDigest.getInstance("MD5");// 确定计算方法  SHA256

BASE64Encoder base64en = new BASE64Encoder();//Base64 编码  Sun过时了,
newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));// newstr加密后的字符串,str是要加密的
-------------MD5 加密文件
 
public static String encryptFile(File file)
{
	MessageDigest messagedigest=null;
	try
	{
		messagedigest = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 10];
		int len = 0;
		while ((len = in.read(buffer)) >0)
		{
			messagedigest.update(buffer, 0, len);
		}
		in.close();
		
	} catch (Exception e)
	{
		e.printStackTrace();
	}
	

	return byteToHexString(messagedigest.digest()); //
}

// byte[] tool
public static String byteToHexString(byte bytes[]) 
{
	//char HEX[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	int len=bytes.length;
	StringBuffer buffer = new StringBuffer(2 * len);
	for (int i = 0;i < len; i++) 
	{	
		String c0 =Integer.toHexString( (bytes[i] & 0xf0) >> 4 );  //>>>是无符号右移,左补0
		String c1 =Integer.toHexString(  bytes[i] & 0x0f       );
		//或者用
		//char c0 = HEX[ (bytes[i] & 0xf0) >> 4 ];
		//char c1 = HEX[  bytes[i] &  0x0f ];
		buffer.append(c0);
		buffer.append(c1);
	}
	return buffer.toString();
}

 public static byte[] hexStringToBtye(String hex) 
 {
	  byte[] hexBuf = hex.getBytes("UTF-8");
	  int len = hexBuf.length;
	  byte[] resultBuf = new byte[len / 2];
	  for (int i = 0; i < len; i = i + 2) 
	  {
		   String two = new String(hexBuf, i, 2);//每次取两个
		   resultBuf[i / 2] = (byte) Integer.parseInt(two, 16);
	  }
	 //int len = hex.length();//可能会是奇数   /2 是错误的
	  return resultBuf;
}

public static int  byteArrayToInt( byte[] buf) throws Exception 
{
	if(buf.length!=4)
		throw new Exception("字节数组 转int 长度不为4");
	ByteArrayInputStream byteInput = new ByteArrayInputStream(buf);
	DataInputStream dataIntput = new DataInputStream(byteInput);
	return  dataIntput.readInt();
}//int res=byteArrayToInt(new byte[] {0,0,5,9}); //十六进制509=1289

public static int byteArrayToInt(byte[] b, int offset) throws Exception 
{
	if(b.length - offset !=4 )
		throw new Exception("字节数组 转int 长度不为4");
   int value= 0;
   for (int i = 0; i < 4; i++) 
   {
	   int shift= (4 - 1 - i) * 8;
	   value +=(b[i + offset] & 0x000000FF) << shift;
   }
   return value;
 }
//----
public static byte[] intToByteArray2(int i) throws Exception
{
  ByteArrayOutputStream buf = new ByteArrayOutputStream();  
  DataOutputStream out = new DataOutputStream(buf);  
  out.writeInt(i);  
  byte[] b = buf.toByteArray();
  out.close();
  buf.close();
  return b;
}//根据返回byte[] 可以查看下标为0的是高位

public static byte[] intToByteArray1(int i) {  
	  byte[] result = new byte[4];  
	  result[0] = (byte)((i >> 24) & 0xFF);
	  result[1] = (byte)((i >> 16) & 0xFF);
	  result[2] = (byte)((i >> 8) & 0xFF);
	  result[3] = (byte)(i & 0xFF);
	  return result;
}


----------和上关联 MD5 加密字串 2
public static String getMD5String(String s) {
   return getMD5String(s.getBytes("UTF-8"));
}

public static String getMD5String(byte[] bytes) {
   messagedigest.update(bytes);
   return byteToHexString(messagedigest.digest());
}

http://www.xmd5.org MD5解密,可数字和字母,但如特殊字符

-----安全散列算法1 (SHA1),测试OK,API使用和MD5一样的

//SHA-1,SHA-256(比SHA-1和MD5要安全),MD5
MessageDigest.getInstance("SHA-1");
MessageDigest.getInstance("sha-1");
MessageDigest.getInstance("Sha-1"); 是一样的。

已经有md6和sh-3
------Bcrypt
-------DSA
public static final String KEY_ALGORITHM="DSA";  
public static final String SIGNATURE_ALGORITHM="SHA1withDSA";  
/** 
 * DSA密钥长度，RSA算法的默认密钥长度是1024 
 * 密钥长度必须是64的倍数，在512到1024位之间 
 * */  
private static final int KEY_SIZE=1024;  
//生成Key
KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);  
SecureRandom secureRandom = new SecureRandom();   
secureRandom.setSeed("651332".getBytes("UTF-8"));
keyPairGenerator.initialize(KEY_SIZE,secureRandom);  
KeyPair keyPair=keyPairGenerator.generateKeyPair();  
DSAPublicKey publicKey=(DSAPublicKey) keyPair.getPublic();  
DSAPrivateKey privateKey=(DSAPrivateKey) keyPair.getPrivate();  

System.out.println("公钥：\n"+Base64.encodeBase64String(publicKey.getEncoded()));//apache的codec
System.out.println("私钥：\n"+Base64.encodeBase64String(privateKey.getEncoded()));  
String str="这是要加密的密文abc123";
System.out.println("原文:"+str);  

//Private生成签名 
PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(privateKey.getEncoded()); //使用生成的Key
KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
PrivateKey priKey=keyFactory.generatePrivate(pkcs8KeySpec); //内部的Key
Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
signature.initSign(priKey);  
signature.update(str.getBytes("UTF-8"));  
byte[] signeData=signature.sign();  
System.out.println("产生签名："+Base64.encodeBase64String(signeData));  

//Public验证签名
X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(publicKey.getEncoded());//使用生成的Key
PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);  //内部的Key
signature.initVerify(pubKey);  
signature.update(str.getBytes("UTF-8"));  
boolean result= signature.verify(signeData);  
System.out.println("验证结果:"+result);.

------RSA
String str = "encryptText测试123";

KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
keyPairGen.initialize(1024);
KeyPair keyPair = keyPairGen.generateKeyPair();

RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

//加密
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, privateKey);//或publicKey
byte[]encryptData = cipher.doFinal(str.getBytes("UTF-8"));

//解密
//Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, publicKey);//或privateKey
byte[]decryptData =cipher.doFinal(encryptData);
System.out.println("解密结果:" + new String(decryptData));


--从文件中的内容转换成PublicKey,PrivateKey 做验证签名 和 生成签名
//---私钥
String pfxPassword="paic1234";
String pfxFile="C:/_work/BIS密钥/privateKey.pfx";//或者.p12
String data="这是一个测试";

KeyStore inputKeyStore = KeyStore.getInstance("PKCS12");  
inputKeyStore.load(new FileInputStream(pfxFile), pfxPassword.toCharArray()); 
Enumeration<String> enums = inputKeyStore.aliases();  
String keyAlias =  enums.nextElement();  
Key key = inputKeyStore.getKey(keyAlias, pfxPassword.toCharArray());  
String alg=key.getAlgorithm();//RSA

inputKeyStore.getCertificate(keyAlias).getPublicKey();//私钥中也可使用公钥

PrivateKey privateKey=(PrivateKey)key;
//--或者用
//			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(key.getEncoded());
//			KeyFactory keyf = KeyFactory.getInstance("RSA");
//			PrivateKey privateKey = keyf.generatePrivate(priPKCS8);

Signature signature =  Signature.getInstance("SHA1withRSA");//SHA256withRSA
signature.initSign(privateKey);
signature.update(data.getBytes("UTF-8"));

singByte=signature.sign();


//---公钥
String cerFile="C:/_work/BIS密钥/pubKey.cer";
String data="这是一个测试";


CertificateFactory certificateFactory=CertificateFactory.getInstance("X.509");
FileInputStream in=new FileInputStream(cerFile);
java.security.cert.Certificate certificate=certificateFactory.generateCertificate(in);
in.close();

System.out.println(certificate.toString());  // 显示证书 有 SHA1withRSA
java.security.cert.X509Certificate x509=( java.security.cert.X509Certificate) certificate;
System.out.println("版本号 "+x509.getVersion());
System.out.println("序列号 "+x509.getSerialNumber().toString(16));
System.out.println("全名 "+x509.getSubjectDN());
System.out.println("签发者全名"+x509.getIssuerDN());
System.out.println("有效期起始日 "+x509.getNotBefore());
System.out.println("有效期截至日 "+x509.getNotAfter());
x509.checkValidity(new Date());//无返回值,抛异常
System.out.println("签名算法 "+x509.getSigAlgName());
byte[] sig=x509.getSignature();
System.out.println("签名n"+new BigInteger(sig).toString(16));

PublicKey pk=x509.getPublicKey();
byte[ ] pkenc=pk.getEncoded();
System.out.println("公钥");
System.out.println(byteToHexString(pkenc));


//PublicKey  pubKey= certificate.getPublicKey();
//--或者用
X509EncodedKeySpec pubKeySpec = new  X509EncodedKeySpec(pkenc);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

Signature signatureChecker = Signature.getInstance("SHA1withRSA");//SHA1withRSA,SHA256withRSA
signatureChecker.initVerify(pubKey);
signatureChecker.update(data.getBytes("UTF-8"));

boolean isOK= signatureChecker.verify(singByte);


//JKS
FileInputStream  in=new FileInputStream("C:/temp/clientKeystore.jks");
KeyStore ks=KeyStore.getInstance("JKS");
ks.load(in,"clientkeystorepass".toCharArray()); //   -keypass lisikeypass     -storepass clientkeystorepass  

PrivateKey pk=(PrivateKey)ks.getKey("lisi","lisikeypass".toCharArray());
//java.security.cert.Certificate cert=ks.getCertificate("lisi");//alias为条目的别
//if( ks.containsAlias("lisi"))
//	ks.deleteEntry("lisi"); 

Enumeration<String> e=ks.aliases();
while(e.hasMoreElements())
{
 java.security.cert.Certificate cert=ks.getCertificate(e.nextElement());
 PublicKey  pubKey=  cert.getPublicKey();
 System.out.println( pubKey.getAlgorithm());
 System.out.println("输出证书信息:\n"+cert.toString());
}
 
----------CRC 循环冗余码  校验
计算机网络书中的
T(x)=X的r次方* k(x)+R(x)
X的r次方 是 G(x)的最高次,G(x)事先约定
R(x) 是 X的r次方* k(x) 除 G(x)的余  (除时按异域,即相同是0,不同是1)
--
T(X)  除 G(x)如余式是0 表示无差错



java.util.zip.CRC32 
CRC32 crc=new CRC32();
crc.update(byte[] bin);
long result=crc.getValue();
 
----------读写zip文件
还有其它的如
java.util.zip.GZIPInputStream
java.util.zip.GZIPOutputStream
java.util.jar.JarInputStream

//解压zip
public static void unZipFile(String zipFile,String outputDir ) throws IOException//OK
	// outputDir以/结束
{
	File root=new File(outputDir);
	if( ! root.exists())
		root.mkdir();
	BufferedInputStream bufferInput=new BufferedInputStream(new FileInputStream(zipFile));
	Charset  gbk=Charset.forName("GBK");
	Charset  def=Charset.defaultCharset();//windows下是GBK
	ZipInputStream zipInput = new ZipInputStream(bufferInput,gbk);//如不传第二参数,zip中的文件名中文只可是UTF8
	ZipEntry entry;
	while ((entry = zipInput.getNextEntry()) != null)
	{
		System.out.println("Extracting: " + entry);//如文件夹尾带/,如mydir/
		File file=new File(outputDir + entry.getName());
		if(entry.isDirectory())
		{
			file.mkdir();
			continue;
		}
		int BUFFER_SIZE = 1024;
		byte data[] = new byte[BUFFER_SIZE];
		BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
		int count;
		while ((count = zipInput.read(data, 0, BUFFER_SIZE)) != -1) 
		{
			dest.write(data, 0, count);
		}
		dest.flush();
		dest.close();
	}
	zipInput.close();
}
//创建zip文件
public static void writeZipFile(String sourceDir,String outZipFile) throws IOException 
{
	FileOutputStream fos=new FileOutputStream(new File(outZipFile),false);//append
	ZipOutputStream zipOutput=new ZipOutputStream(new BufferedOutputStream(fos,BUFFER_SIZE));//可以不加Charset.defaultCharset()
	File inDir=new File(sourceDir);
	loopDirIntoZip(inDir,inDir,zipOutput);
	zipOutput.close();
}
private static void  loopDirIntoZip(File baseDir,File sourceFile,ZipOutputStream zipOutput) throws IOException
{
	//zipOutput.putNextEntry( new ZipEntry("a/b/c"));
	//不能以/开头,也不能使用"",可一次性加多级子目录,也可以一级一级的加
	String zipPair=sourceFile.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
	if(sourceFile.isDirectory())//这里是一级一级的进入,复杂点
	{	
		if(! zipPair.equals(""))//第一次zipPair是"",第一级目录
		{
			zipPair=zipPair.substring(1);//去开头/
			zipPair+=File.separator; //加尾部/
			ZipEntry entry= new ZipEntry(zipPair);
			zipOutput.putNextEntry(entry);
			System.out.println("Compress dir: "+entry);
		}
		File dirContents[]=sourceFile.listFiles();
		for(File f:dirContents)
			loopDirIntoZip(baseDir,f,zipOutput);//做递归
	}else
	{
		BufferedInputStream itemInput = new BufferedInputStream(new FileInputStream(sourceFile),BUFFER_SIZE);
		byte data[]= new byte[BUFFER_SIZE];
		zipPair=zipPair.substring(1);//去开头/
		ZipEntry entry= new ZipEntry(zipPair);
		System.out.println("Compress file: "+entry);
		zipOutput.putNextEntry(entry);
		//在向ZIP输出流写入数据之前，必须首先使用 putNextEntry(entry); 方法安置压缩条目对象 
		int count;
		while((count=itemInput.read(data,0,BUFFER_SIZE))!=-1)
			zipOutput.write(data,0,count);
		itemInput.close();
	}
}
-----------
File.separator
System.getProperties().get("line.separator").toString()
System.getProperties().get("os.name").toString()



//方式二
//每次在使用HttpURLConnection时,工具类来解析http响应头中的Set-Cookie，把cookie存在一个全局的地方，以后就直接从这里取				
CookieManager manager = new CookieManager();
manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);//设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
CookieHandler.setDefault(manager);//这几句，要在URL .openConnection之前
 

URL url=new URL("http://127.0.0.1");
HttpURLConnection http=(HttpURLConnection)url.openConnection();
//http.setReadTimeout(10000);//设置读取超时时间          
//http.setConnectTimeout(10000);//设置连接超时时间    
http.setRequestMethod("POST");
http.setRequestProperty("Content-type","application/json;charset=UTF-8");
http.setDoOutput(true);//如要先写要调用这个
OutputStream out = http.getOutputStream();
out.write(xmlStr.getBytes("UTF-8"));
out.flush();
http.connect();//这个可有，可无
code = http.getResponseCode();//这里才真正的发起请求
http.getInputStream();

//读cookie在响应头中
Map<String, List<String>> maps = http.getHeaderFields();
String cookieskey = "Set-Cookie";//响应头Set-Cookie
List<String> coolist = maps.get(cookieskey);
Iterator<String> it = coolist.iterator();
while(it.hasNext()){
	String val=it.next();//loginId=lisi; Max-Age=1800; Expires=Mon, 20-Apr-2020 14:04:55 GMT 
} 

//方式二,比自己解析字串强多了
CookieStore cookieJar = manager.getCookieStore();
List<HttpCookie> storeCookies = cookieJar.getCookies();
for(HttpCookie c:storeCookies){
	System.out.println("store cookie: "+c);
} 


-------------URL的FTP-------type=b,传图片OK
URL url = new 　URL(ftp://user01:pass1234@ftp.foo.com/README.txt;type=i); //i=ascii,b=binary
URLConnection urlc = url.openConnection();
InputStream is = urlc.getInputStream(); // 下载
OutputStream os = urlc.getOutputStream(); // 上传

//passive 被 动FTP 服务????????????


--------------------------------
>>>
右移，左边空出的位以0填充 ；无符号右移 


最高数称为“符号位”。为1时，表示该数为负值，为0时表示为正值。 

负数是正数的补码(取反加１),如负数转正数则是减１取反


# <<是左移符号,列x<<1,就是x的内容左移一位(x的内容并不改变)　　　右边始终补0
x<<几位，结果就是x乘2的几次方
Math.pow(2,3)//2的3次方，或者3的2次幂
Math.round() 结果是整数
/ 结果是整数 
BigDecimal   b   =   new   BigDecimal(0.032); 
double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  

# >>是带符号位的右移符号,x>>1就是x的内容右移一位,如果开头是1则补1,是0责补0,(x的内容并不改变).
# >>>是不带符号位的右移,x>>>1就是x的内容右移一位,开头补0(x的内容并不改变)

位异或( ^ ) 
十六进制相关转换
String   hexString   =   Integer.toHexString(1234567);
String   str   =   Integer.toString(Integer.parseInt(hexString,16));

汉字的Unicode编码范围为\u4E00-\u9FA5


java.text.DecimalFormat   df   =   new   java.text.DecimalFormat("#,##0.00"); //小数点会四舍五入的
String   strValue   =   df.format(doubleValue);


String s=String.format("%2d,%3s",12,"abc");//和C一样了
String res=String.format("%-50s", "abcd");//左对齐,长度50个右补空格

Calendar c =   Calendar.getInstance();
String s = String.format("today: %1$tY-%1$tm-%1$td", c); //%1$是后参数的第一个 
String.format("today: %1$tY-%<tm-%<td", c);//%< 表示使用和前一个相同,即%1$
String.format("now=%1$tY-%<tm-%<td %<tH:%<tM:%<tS.%<tL ",new java.util.Date());

c.getActualMaximum(Calendar.DAY_OF_MONTH);//得到当月的最后一天

System.currentTimeMillis();
 

Java中是没有逗号表达式的


	

守护线程则是用来服务用户线程的,setDaemon(boolean on)


----------线程池 
java.util.concurrent包 
ThreadPoolExecutor  中的doc
	execute(Runnable )

	ExecutorService exec =Executors.newCachedThreadPool(); //如创建的线程60秒未使用，则从cache中删
	Semaphore semp = new Semaphore(5);// 只能5个线程同时访问,如超过阻塞  ,如一个线程用完了，归还后阻塞线程中可进一个     
	exec.execute( Runnable )//没有返回结果
	semp.availablePermits();//还有几个线程可进入
	Future f =exec.submit(Callable )//Callable 可以得到执行结果
	f=exec.submit(Runable ,T result )//Runnable只能转入执行完成后的返回结果
	f=exec.submit(Runnable task);//如成功执行后,f.get()返回null
	  exec.execute(Runable) //没有返回结果,当线程数小于corePoolSize进会建立线程
							//当executor shutown,线程和队列达到饱和时,会调用 RejectedExecutionHandler.rejectedExecution
	f.get()//阻塞,直到返回线程处理结果

	semp.acquire(); //获取许可 
	semp.release();  //访问完后，释放  
	
	exec.shutdown();  
	
ThreadPoolExecutor waitTermination(timeout, unit)//isTerminated(); 必须调用 shutodwn


//如一个有异常,有时会影响其它线程的???,如果两个都有异常,有时会只有报一个异常的错误,只能在每个任务中try???
//ThreadPoolExecutor 中用 ThreadGroup 捕没异常没用的???,thread.setUncaughtExceptionHandler没用的  ???, 如何捕??

//线程池名字没用,因为线程会不停的重建
private  ThreadPoolExecutor  waitingExecutor = new ThreadPoolExecutor(2,3, 10,TimeUnit.SECONDS,
						new ArrayBlockingQueue<Runnable>(30), new ThreadPoolExecutor.CallerRunsPolicy());//如果队列满,新加入的使用当前线程调用run而不是start运行,相当于未开线程
 private ExecutorCompletionService<Object>  waitingComplete =new ExecutorCompletionService<Object>(waitingExecutor);
for (int i = 0; i < tasks; ++i) {
	Furture future=waitingComplete.take();
	Object res=future.get(); //如用 Callable submit 可以得到执行结果
}
waitingExecutor.shutdown();


public   void waitingThread()  
{
	try {
		for (int i = 0; i < tasks; ++i) {
			Future<Object> futrue=waitingComplete.take();
			Object obj=futrue.get();//可以得到执行结果
		}
		
	} catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	}
	waitingExecutor.shutdown();
}


public  Future<Object> submitWaitingTask(Callable task)//要求父线程退出前,等待子线程运行完成,如要结果使用 Callable
{
	 tasks++;
	  Future<Object> f= waitingComplete.submit(task);
	  return f;
}


CyclicBarrier cyclic =new CyclicBarrier(3);//3个线程
cyclic.await();//3个线程调用进入后,这些线程才可一起继续执行，是在每个线程中阻塞，而CountDownLatch是在主线程中阻塞
cyclic.reset()
cyclic.getNumberWaiting();

Exchanger<String> exchanger=new Exchanger<String> ();//两个线程同时个自执行到exchange方法时交换数据,如一个线程先到要等待
String data2=exchanger.exchange(data1);//第一个线程
String data1=exchanger.exchange(data2);//第二个线程



ExecutorService x=	Executors.newFixedThreadPool(int) 
	Executors.newSingleThreadExecutor()//只是一个线程,好处是一个线程使用完成后,会自动建立,还可再继续使用
	Executors.newCachedThreadPool()//根据需求增加线程，不需要时自动过60s就会减少线程
ScheduledExecutorService ScheduledExecutor=Executors.newScheduledThreadPool();//定时
ScheduledExecutor.schedule(callable,5,TimeUnit.SECONDS); //5 秒后启动线程
	Executors.defaultThreadFactory()// same ThreadGroup and with the same NORM_PRIORITY priority and non-daemon status
	
 
ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
					BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler)			
	
	如池中当线程数大于corePoolSize,如超出线程等待时间超过keepAliveTime会被终止
	
	TimeUnit.MILLISECONDS  10^-3 秒
	NANOSECONDS  10^-9 秒	
	MICROSECOND  10^-6 秒	
	
	//workQueue长度决定了能够缓冲的最大数量 
	new ArrayBlockingQueue<Runnable>(int capacity) //如果队列满，空，阻塞
	
	handler可为 new ThreadPoolExecutor.AbortPolicy();
				1. CallerRunsPolicy ：这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
				2. AbortPolicy ：对拒绝任务抛弃处理，并且抛出异常。
				3. DiscardPolicy ：对拒绝任务直接无声抛弃，没有异常信息。
				4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。 
execute(Runnable command) 			

--
CompletionService complete=new ExecutorCompletionService();//哪个线程先执行完成,取哪个结果
complete.submit(Callable);
complete.take().get();//take()返回Future,

FutureTask

//在一个对象中m如果一个线程进入synchronized one 方法 还没有返回,则另一个线程也不能进入synchronized two 方法 , 想当于synchronized(this)
//如果 static 方法 加synchronized,相当于 synchronized(类名),也是如果一个线程进入 static synchronized one 方法未返回,另一个线程static synchronized two 方法会阻塞
//同一个线程可以多次得到同一个synchronized锁(在释放锁之前,如递归调用 )

reentrantLock.lockInterruptibly();//如阻塞在这里进不了,可以在另个线程中调用   这个线程的对象的 .interupt()，来中断这个线程,会抛出 InterruptedException

ReentrantLock  可以使用 isHeldByCurrentThread() 和 getHoldCount()   
//同一个ReentrantLock ,多线程时只可一个线程进入lock区,同一线程可lock多次, getHoldCount()返回当前线程多少次
如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。必须在 finally 块中释放


ReentrantLock.isLocked();
//		Thread.holdsLock(obj);//当前线程 否是synchronize锁这个对象


if (lock.tryLock(2,TimeUnit.SECONDS)) {//如果已经被lock，则立即返回false不会等待 ，对多线程来说的
	  try {
		 //操作
		  System.out.println("in tryLock");
	  } finally {
		  lock.unlock();
	  }
}
  
 //Reentrant再进去re entrant,一个线程可多次试图获取它所占有的锁请求会成功
private ReentrantLock pauseLock = new new ReentrantLock(false);
//默认是 false 不公平,如为true 选择等待时间最长的线程进入
//前一个线程进入lock()还没有退出unlock(),后一个线程不可以进入lock(),除非前一个线程进入.newCondition().await时,后一个线程才可进入(可重入)


//	多个线程可同时得到读的Lock，但只有一个线程能得到写的Lock,必须等读锁完成
//	而且写的Lock被锁定后，任何线程都不能得到Lock
    ReadWriteLock rwlock = new ReentrantReadWriteLock();
	//在没有释放读锁的情况下，就去申请写锁，这属于锁升级，ReentrantReadWriteLock是不支持的
	//ReentrantReadWriteLock支持锁降级
	Lock rlock= rwlock.readLock();
	Lock wlock= rwlock.writeLock();
	
//好处是可以有多个Condition(只ReentrantLock)，文档上有的示例就是ArrayBlockingQueue的源码
private Condition unpaused = pauseLock.newCondition();
pauseLock.lock(); 

//中间的相当于 外面加了synchronized

// Object 的(wait, notify and notifyAll),必须先synchronized,应该也可以用于生产者,消费者
unpaused.await()//等待 Condition的await可能假醒，要使用循环和状态综合做判断
unpaused.signalAll();//通知不再等待，但要在unlock以后才有效
unpaused.signal();
pauseLock.unlock();//最好放在finally块中


ReadWriteLock rwl=new ReentrantReadWriteLock();//在读的时候不加锁,写时要等所有的读锁完成才能写,在写的时候,读锁不能进入
rwl.readLock().lock();
...
rwl.readLock().unlock();//finally
rwl.writeLock().lock();
...
rwl.writeLock().unlock();//finally

TimeUnit.SECONDS.sleep(5);//有convert 方法

Thread 类的方法	isInterrupted()　不影响" interrupted status "
Thread 类的静态方法	interrupted() 影响" interrupted status "，如果连续调用两次，第二次会返回flase



ThreadLocal  其实是采用哈希表的方式来为每个线程都提供一个变量的副本。从而保证各个线程间数据安全。每个线程的数据不会被另外线程访问和破坏。
			所变量保存在ThreadLocal中，只有set,get,remove 
			
ThreadLocal 不能声明为static(因是多个线程共享),如一个线程，只会使用一个ThreadLocal实例，可不用remove,如多个实例一定要remove(),否则可能内存溢出

CountDownLatch countDownLatch = new CountDownLatch(threadNumber);//主线程等所有线程完成
子线程结束前调用countDownLatch.countDown();  
主线程的最后用countDownLatch.await();  


ServerSocket server=new ServerSocket(4700);
Socket  client=server.accept();//阻塞
 client.setSoTimeout(2000);//两端都可以设置 read() timeout 
 
InputStream input=client.getInputStream();
OutputStream output=client.getOutputStream();

output.write(str.getBytes("UTF-8"));//不必调flush
//---
BufferedOutputStream bufferOut=new BufferedOutputStream(output);
bufferOut.write(str.getBytes("UTF-8")); //这里可以多次write 
bufferOut.write("\n".getBytes());//另一端是 BufferedReader 以换行符为结束
bufferOut.flush();//BufferedOutputStream 不调flush不会立即写

//==

input.read(buffer);//另一端close 才返回-1,和文件的读不一样的是这里每一次读时不一定读满才返回
//---
BufferedReader bufreader=new BufferedReader(new InputStreamReader(input, Charset.forName("GBK")));
//windows下 如是记事本另为UTF-8的文件,开始有多余的UTF8 BOM头,不要生成带BOM头的UTF8文件,使用notepad++新建文件保存就是UTF-8
String line=bufreader.readLine();//如果没有读到'\n'会一直卡住,
public static boolean isUTF8BOMFile(File file) throws Exception 
{
	InputStream in = new FileInputStream(file);
	byte[] b = new byte[3];
	in.read(b);
	in.close();
	if (b[0] == -17 && b[1] == -69 && b[2] == -65)	//EF  BB  BF
	{	
		System.out.println(file.getName() + "：编码为UTF-8 BOM");
		return true;
	}
	return false;
}

FileReader reader =  new  FileReader( file);//没有指定父类中 InputStreamReader 中的编码,按系统默认的字符集来编码
String encoding=reader.getEncoding();//所以始终是GBK(系统默认的字符集来编码)

server.close();
client.close();

// Socket socket=new Socket("127.0.0.1",4700);
Socket socket=new Socket();
socket.connect(new InetSocketAddress("127.0.0.1",4700), 2000);
OutputStream output=socket.getOutputStream();
InputStream input= socket.getInputStream();


===UDP server
DatagramPacket pack=new DatagramPacket(buff,buff.length);
DatagramSocket rev=new DatagramSocket(6000);
rev.receive(pack);//阻塞
				
DatagramPacket sendpack=new DatagramPacket(str.getBytes(),str.getBytes().length,
				pack.getAddress(),pack.getPort());//发来的地址
rev.send(sendpack);
rev.close();

===UDP  client
DatagramPacket packet=new DatagramPacket(str.getBytes(),str.length(),InetAddress.getByName("localhost"),6000);//client带地址
DatagramSocket socket=new DatagramSocket();
socket.send(packet);
			
DatagramPacket receive=new DatagramPacket(buff,1000);
socket.receive(receive);//可能阻塞
socket.close();


---------nio
 Scanner scanner =new Scanner(System.in);
 while(scanner.hasNextLine())
 {
 	String line=scanner.nextLine();
 }
是同步非阻塞的

java.nio.ByteBuffer;			Position<=Limit<=Capacity
java.nio.FileChannel; 线程安全的

GatheringByteChannel extends WritableByteChannel 可以把多个内存中的ByteBuffer 写出(外部)的能力  Gather  ,  	write(ByteBuffer[] srcs) 
ScatteringByteChannel extends ReadableByteChannel   把(外部)同时往几个 ByteBuffer 读入到内存的能力      , 	read(ByteBuffer[] dsts)
 
 .getShort(0)//getXxx就不用flip了 
 
 
 while (channel.write (bs) > 0) { //ByteBuffer [] bs
 
 }
ByteBuffer buffer = ByteBuffer.allocate(1024); 
ByteBuffer.wrap("The"); 


FileChannel fc=FileInputStream 的 getChannel() 返回;  
MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());//虚拟内存映射


fc.read(buffer);  
buffer.flip();//limit=position , position=0//用于读数据
	rewind() // position = 0
	compact() //(因为前的数据已经写出,没用了)position=0,limit=capacity()
	header.position(18);
	put(data);
	
byte[] data1=new byte[]{48,48,48,56,77};
ByteBuffer byteBuffer=  ByteBuffer.wrap(data1,2,3);
buf.position();//是 2
byteBuffer.array();//返回的数组没有变化,还是data中全部内容,
byte[] dest=new byte[buf.limit()-buf.position()] ;
byteBuffer.get(dest);//会把目标数组加满,如加不满报错,从当前position开始加
byteBuffer.putLong(9);//设置当前postion值
Arrays.copyOfRange(data,start,end)
Arrays.fill(data,(byte)0);

ByteBuffer to=ByteBuffer.wrap(new byte[]{48,56});
ByteBuffer.wrap(data,2,2).compareTo(to);//返回0 相等
System.arraycopy(srcArray, srcPos,destArray, destPos, len);

ByteBuffer x.compare()
Arrays.equals()

//-compact示例
	 buf.clear(); 
	 while (in.read(buf) >= 0 || buf.position != 0) {
		 buf.flip();
		 out.write(buf);//可能一次没有把buf中数据写完
		 buf.compact();// (因为前的数据已经写出,没用了)position=0,limit=capacity()
	 }
	
Charset charset = Charset.forName("GBK");
charset.encode("");//返回ByteBuffer

//charset.newDecoder().decode();//或者用
charset.decode(ByteBuffer)//返回CharBuffer 实现了CharSequence可以println
SocketChannel.open(InetSocketAddress ); //.close();
buffer.clear();


Selector 异步 IO 的核心类，它能检测一个或多个通道 (channel) 上的事件
---server
ServerSocketChannel ssc = ServerSocketChannel.open();//.close();

InetAddress.getLocalHost().getHostAddress();//获得本机IP,只对windows有用，linux总是127.0.0.1
//linux下多网卡
Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
InetAddress ip = null;
while (allNetInterfaces.hasMoreElements())
{
	NetworkInterface netInterface =  allNetInterfaces.nextElement();
	if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
		continue;
	}
	//System.out.println(netInterface.getName());
	Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
	while (addresses.hasMoreElements())
	{
		ip =  addresses.nextElement();
		if (ip != null && ip instanceof Inet4Address)
		{
			System.out.printf("本机 网卡%s 的 IP是 %s %n" ,netInterface.getName(), ip.getHostAddress());
		}
	}
}

ssc.configureBlocking(false); 
InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), port);
ssc.socket().bind(isa);

Selector acceptSelector =SelectorProvider.provider().openSelector();
//Selector acceptSelector = Selector.open();//不同打开方式

SelectionKey acceptKey =ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);//ServerSocketChannel注册到Selector上 , 是interest set , 可以传第三个参数也可以传attachement
//也可第三个参数做附件
// SelectionKey 的 interestOps(x);
acceptKey.attach(xxxx);//可传对象参数,可为 本 端的其它步(read,write)使用
  
acceptSelector.select();//有多少channel可以进行IO操作,阻塞的,通知我们我们感兴趣的事件发生了
//返回已经ready的个数 , 可以指定毫秒时间，如果到时间调用 selector.wakeup,不阻塞,手工调用wakeup() 后可以调用selectNow()

Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
 iter.remove(); //每次要删除才行，防止重复处理
SelectionKey sk   //.isAcceptable()    接收请求,只有ServerSocketChannel支持 ,   validOps() 所支持的操作集合
				 //.attach(xx)
				 //.attachment();得到参数对象
ServerSocketChannel nextReady =(ServerSocketChannel) sk.channel();//register时的类型 
SocketChannel channel = nextReady.accept();//因上OP_ACCEPT,这里accept()
Socket s = channel.socket();//可以转成原始的Socket

---
ServerSocketChannel newServerChannel =ServerSocketChannel.open();
newServerChannel.bind(new InetSocketAddress(9999));
//newServerChannel.configureBlocking(true);//这里可以设置为true,也是默认值
newServerChannel.configureBlocking(false);
SocketChannel newClientChannel =newServerChannel.accept();//当configureBlocking(false)时,如无连接 会立即返回null
---
boolean isConnected=clientSocketChannel.connect(new InetSocketAddress(9999));
//如 configureBlocking(false) 未连接会立即返回false,而不会报错
		
---client
SocketChannel client = SocketChannel.open();//这里也可传IP
client.configureBlocking(false);//使用selector必须是false
Selector selector = Selector.open();
client.register(selector, SelectionKey.OP_CONNECT);//客户端也不是用Server
client.connect(ip);//连接后，服务器端的select()继续，isAcceptable
while(true)
{
	selector.select();//第一次OP_CONNECT不阻塞,第二次写出后,要等务端读完,并已经有channel.write()
	Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
	while (iter.hasNext())
	{
		SelectionKey key = iter.next();
		iter.remove();
		if (key.isConnectable()) 
		{
		  SocketChannel channel = (SocketChannel) key.channel();
		  if (channel.isConnectionPending())
				channel.finishConnect();//如服务端没有监听这里才出错
		  channel.write(encoder.encode(CharBuffer.wrap("Hello from " + index)));//写后服务端就可以读，isReadable
		  channel.register(selector, SelectionKey.OP_READ); 
		} else if (key.isReadable()) 
		{
			 channel.read(clientBuffer);
		}
	}
}
 
 
 
 
FileLock lock= fileChannel.lock();
FileLock lock= fileChannel.lock(10,3,true);//pos,size,isShared
  
lock.release();//finally中

fileChannelIn.transferTo(0, fileChannelIn.size(), fileChannelOut);//文件复制
//对应的也有transferFrom
//fileChannelOut.transferFrom(fileChannelIn, 0, fileChannelIn.size());

//---------监视目录的变化
Path path=Paths.get("c:/temp");//目录
WatchService  watcher = FileSystems.getDefault().newWatchService(); 
path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
while(true)
{ 
	WatchKey key = watcher.take(); //这里会阻塞
	for(WatchEvent<?> event : key.pollEvents())
	{ 
		WatchEvent.Kind kind = event.kind(); 
		if(kind == StandardWatchEventKinds.OVERFLOW)//事件可能lost or discarded 
			continue; 
		WatchEvent<Path> e = (WatchEvent<Path>)event; 
		Path fileName = e.context(); 
		System.out.printf("Event %s has happened,which fileName is %s%n"  ,kind.name(),fileName); 
		// 创建文件　kind.name()返回  ENTRY_CREATE　,path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE
		// 修改文件　kind.name()返回  ENTRY_MODIFY
		// 删除文件　kind.name()返回  ENTRY_DELETE
	} 
	if(!key.reset())
		break; 
} 
//-------仿问者遍历目录
Files.walkFileTree(Paths.get("c:/temp"), new FileVisitorTest());
class FileVisitorTest extends SimpleFileVisitor<Path>  //FileVisitor
{ 
    public FileVisitResult visitFile(Path file,BasicFileAttributes attrs)//不用像父类一样要声明异常
    { 
        System.out.println("file:"+file.getParent()+"/"+file.getFileName());
        //attrs.isSymbolicLink()对windows的快捷方式无法识别
        //attrs.isRegularFile();
		
		//FileVisitResult.SKIP_SIBLINGS;
		//FileVisitResult.SKIP_SUBTREE;
		//ileVisitResult.TERMINATE;
        return FileVisitResult.CONTINUE; 
    } 
    public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes attrs)
    {
    	System.out.println("进入目录:"+dir.getFileName());
        return FileVisitResult.CONTINUE; 
    } 
    public FileVisitResult visitFileFailed(Path file,IOException e){
    	System.out.println(file.getFileName()+"仿问失败");
        return FileVisitResult.CONTINUE; 
    } 
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
	{
		System.out.println("离开目录:"+dir.getFileName());
		 return FileVisitResult.CONTINUE; 
	}
}
-----异步NIO , AIO文件 
NIO是同步非阻塞的
AIO是异步非阻塞的

 AsynchronousFileChannel afc = AsynchronousFileChannel.open(Paths.get(""));
 ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);  
 afc.read(byteBuffer, 0, null, new CompletionHandler<Integer, Object>() {  
	            @Override  
	            public void completed(Integer result, Object attachment) {  
	            	System.out.println("byteBuffer = " + new String(byteBuffer.array(),Charset.forName("UTF-8")));  
	    	        System.out.println("byteBuffer length= " + result);  
 	            }  
	            @Override  
	            public void failed(Throwable exc, Object attachment) {  
	                System.out.println(exc.getCause());  
	            }  
	        });  
Future<Integer> result = afc.write(buffer, 0);//开始位置
			
Future<FileLock> featureLock = asynchronousFileChannel.lock();
----异步NIO 网络 server
ExecutorService executorService = Executors .newCachedThreadPool(Executors.defaultThreadFactory());
AsynchronousChannelGroup  threadGroup =  AsynchronousChannelGroup.withCachedThreadPool(executorService, 3);
		 
try ( AsynchronousServerSocketChannel asynchronousServerSocketChannel =  AsynchronousServerSocketChannel.open(threadGroup))
{
	asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
	asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
	asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
	
	//方式1
	Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel .accept();	
	 AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();//get()阻塞直到连接上来
	 asynchronousSocketChannel.getRemoteAddress();
	 asynchronousSocketChannel.read() //write
	//方式2 Fail
	asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>(){
	});
}
-------client
AsynchronousSocketChannel client = AsynchronousSocketChannel.open(); 
client.connect(new InetSocketAddress(IP,DEFAULT_PORT)).get(); 
System.out.println("\n connected");
client.write(ByteBuffer.wrap("test".getBytes())).get(); 
client.close();
//方式2 OK
client.connect(new InetSocketAddress(IP,DEFAULT_PORT),null,new CompletionHandler<Void,Void>()
{ 
	@Override 
	public void completed(Void result, Void attachment) { 
		try { 
			Integer writed=client.write(ByteBuffer.wrap("test".getBytes())).get();
			System.out.println("client writed bytes:"+writed);
			
			ByteBuffer buf=ByteBuffer.allocate(1024);
			int readed=    client.read(buf).get();
			System.out.println("client receive data : "+new String(buf.array()));
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		} 
	} 

	@Override 
	public void failed(Throwable exc, Void attachment) { 
		exc.printStackTrace(); 
	} 
});
		
---
Pipe pipe=Pipe.open( );
Pipe.SourceChannel source=pipe.source();//ReadableByteChannel
Pipe.SinkChannel sink=pipe.sink();//WritableByteChannel
//Pipe.SourceChannel（管道负责读的一端）和Pipe.SinkChannel（管道负责写的一端）

WritableByteChannel out = Channels.newChannel (System.out);
-----
PipedOutputStream output=new PipedOutputStream();
PipedInputStream input=new PipedInputStream();
output.connect(input);
int len = -1;  
byte[] buffer = new byte[1024];  
 //若没有数据可读,则让读进程等待(见read()函数)   //可用于生产者和消费者
 while ((len = input.read(buffer)) != -1) 
 {  
	 System.out.println(new String(buffer, 0, len));  
 }

 
 
CharsetDecoder 不是线程安全的

Socket(Proxy proxy)  //可以指定代理

StringBuilder 是线程不安全的
StringBuffer 是线程安全的 清空调用setLength(0);
System.out.printf("char\t|__%-10s|\n","abc"); //left align 
System.out.printf("number__%10d",123);		//right align
System.out.printf("number__%10.2f",123.12);	


>native2ascii  -reverse -encoding UTF-8  aa_zh_CN.properties normal.txt  

-------- ClassLoader   
class放在非文件系统中,如DB,网络

Java ClassLoader 就是用 Java 语言编写的
有一个ClassLoader不是用java语言所编写的，而是JVM实现的一部分，这个ClassLoader就是bootstrap classloader（启动类加载器），jdk_home/lib目录下的核心api 或 -Xbootclasspath 选项

一个是ExtClassLoader，这个ClassLoader是用来加载java的扩展API的,jdk_home/lib/ext目录下的jar包或 -Djava.ext.dirs 指定
一个是AppClassLoader  java -classpath/-Djava.class.path所指的目录下	ClassLoader.getSystemClassLoader()

自定义ClassLoader都必须继承ClassLoader这个抽象，有一个getParent()方法，这个方法用来返回当前ClassLoader的parent，注意，这个parent不是指的被继承的类，而是在实例化该ClassLoader时指定的一个ClassLoader，如果这个parent为null，那么就默认该ClassLoader的parent是bootstrap classloader

双亲委托模式 先检查自定义ClassLoader有没有再向上,父有没有,到bootstrap 没有ClassNotFoundException,子ClassLoader试着加载,
//好处是安全,对java.lang.Object总是由父类加载器,存到当前加载器
线程通过setContextClassLoader方法来指定一个合适的classloader作为这个线程的context classloader，默认的是system classloader

除根外,其它的加载器只有一个父加载器

ClassLoader.getSystemClassLoader();//sun.misc.Launcher$AppClassLoader@19821f
ClassLoader loader1=new MyClassLoader();//参数默认是ClassLoader.getSystemClassLoader()做为父
ClassLoader loader2=new MyClassLoader(loader1); //ClassLoader的构造方法中,loader1是loader2的父加载器
MyClassLoader extends ClassLoader{
	public Class<?> findClass(String name) //重写ClassLoader的方法 ,loadClass 方法会自动调用这个方法
	{
		 byte[] b = loadClassData(name);//从特指的目录,...
	     return defineClass(name, b, 0, b.length);//调用ClassLoader方法
	}
 }
 
 
//  以/结束是目录,否则是.jar
ClassLoader urlLoader=URLClassLoader.newInstance(new URL[]{new URL("file:///d:/temp/")},null);//第二个参数是parent,
urlLoader.loadClass("pack.TestObject");

Proxy.newProxyInstance(ClassLoader )

只在第一次使用类时才去加载,

----static块在什么时候执行  
1.implicit隐式,即利用实例化才载入的特性来动态载入class
2.explicit显式方式,又分两种方式: 
1).java.lang.Class的forName()方法	
2).java.lang.ClassLoader的loadClass()方法


类的初始化顺序  static 属性或块按书写的顺序 (有父类先父类,再子类)  -> 1. {}块  , 初始化非static有值字段 按书写的顺序来 ,2.构造方法 (有父类先父类,再子类)

static final 属性如果编译就可以计算出结果,使用final属性时,类不会被初始化,即不会执行static块 


用户定义的类java.lang.Spy  和 java.lang.* 是不同的类加载器,是不同的运行时包,Spy 不能仿问java.lang.*中的包中可见成员(安全)

transient 关键字修饰属性就不会被序列化
public class Person implements java.io.Serializable 
{  
	private static final long serialVersionUID = 1L;//用于标识版本,客户端与服务端版本
	private String name;  
	private transient int age; 
}
 
 
 
volatile
线程可以把变量保存在本地内存（比如机器的寄存器）中，而不是直接在主存中进行读写
一个线程在主存中修改了一个变量的值，而另外一个线程还继续使用它在寄存器中的变量值的拷贝，造成数据的不一致。 
把该变量声明为volatile（不稳定的）即可，这就指示JVM，这个变量是不稳定的，每次使用它都到主存中进行读取

当要访问的变量已在synchronized代码块中，或者为常量时，不必使用。 

volatile屏蔽掉了VM中必要的代码优化，所以在效率上比较低


strictfp 即 strict float point (精确浮点) ,可应用于类、接口或方法
想让你的浮点运算更加精确，而且不会因为不同的硬件平台所执行的结果不一致的话，可以用关键字strictfp.
strictfp不能放在接口方法前,也不能放在构造函数前

AtomicInteger   c.incrementAndGet(); 相当于  ++c; 可以保证 ++ 和 取 两个操作是安全的,
			看源码是使用compareAndSet实现的
			上次看到这个变量之后其他线程修改了它的值，那么更新就失败,再一次循环做,就不会被阻塞,
AtomicIntegerArray 对数组
AtomicIntegerFieldUpdater是对对象里的属性修改 newUpdater(方法

AtomicReference<X> head = new AtomicReference<X>();
do {
	oldHead = head.get(); //要重新取
	newHead.next = oldHead;
} while (!head.compareAndSet(oldHead, newHead));


Integer i3= 128; 
Integer i4= 128;
System.out.println(i3==i4);  //false ???  大坑
//对于Integer var=?在-128至127之间的赋值，Integer对象是在IntegerCache.cache产生，会复用已有对象，这个区间内的Integer值可以直接使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生



//其它人写的线程，有异常，并没有try,我在main方法中如何try,
//使用ThreadGroup类，重写uncaughtException方法，建立自己的线程时带入ThreadGroup
class Thread1 implements Runnable
{
	public void run() 
	{
		String x=null;
		x.equals("a");

	}
}

//异常捕获 ,优级高到低为 thread.setUncaughtExceptionHandler ->  ThreadGroup -> Thread.setDefaultUncaughtExceptionHandler(全局)
class MyThreadGroup extends ThreadGroup {
	public MyThreadGroup() 
	{
		super("MyThreadGroup");
	}
	public void uncaughtException(Thread thread, Throwable exception) {
		System.out.println("抓到了异常" + exception);
		// exception.printStackTrace();//example, print stack trace
	}
}

public static void main(String[] args) 
{
	Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {//全局异常捕获OK
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.err.print("global setUncaughtExceptionHandler---catch error in thread:"+t.getName()  +" have error :"+e.getMessage());
			}
		});

		
		Thread thread=new Thread(group, new Runnable(){
			@Override
			public void run() {
				System.out.print("在子线程中执行");
				throw new RuntimeException("have exception");
			}
			
		},"myThread-01");//线程带名字OK
		thread.start();
}

线程状态
Thread thread;
if (thread.getState() == Thread.State.NEW)//线程还没有调用start来启动前
if (thread.getState() == Thread.State.TERMINATED)//线程执行完成后，不能再用start()

Thread.State.RUNNABLE
Thread.State.TIMED_WAITING   　sleep(带参)
Thread.State.BLOCKED		synchronized 等侍中
Thread.State.WAITING		join()不带数

synchronized(obj)//得到对像的Monitor,synchronized来得到，才可以调wait,notify
//jconosle可以看到调用堆栈方法 卡到wait,jvisualvm 可以到线程等待多长时间,,,,如是死锁，死偱环如何知道
{
	while(isSomthing)//官方文档上说,有时可能是spurious(假的) wakeup,要放在一个while循环中
		obj.wait();
}


=========================XML
查找顺序
1.设置系统属性javax.xml.parsers.DocumentBuilderFactory的值,如System.getProperties,或java -D
2.JRE/lib/建立jax.properties文件,写javax.xml.parsers.DocumentBuilderFactory=实现org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
3.classpath下找/META-INF/services/javax.xml.parsers.DocumentBuilderFactory文件中写实现类
xercesImpl.jar/META-INF/services/javax.xml.parsers.DocumentBuilderFactory文件中记录着DocumentBuilderFactory实现类
JDK默认使用com.sun.org.apache.xerces 和 xalan
替换JDK默认的在JRE/lib/下建立endorsed子目录将实现的.jar包放入,或者设置系统属性java.endorsed.dirs的值为目录所在位置
		
---------XML JAXP DOM  读
private Node ignoreXMLSpace(Node node)
{
	if(node!=null)
		while(node.getNodeType()!=Node.ELEMENT_NODE)
		{
			node=node.getNextSibling();
			continue;
		}
	return node;
}

javax.xml.parsers.DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//javax
factory.setValidating(true);//打开XML的DTD验证,后面的setIgnoringElementContentWhitespace才有
//factory.setSchema()//schema验证
factory.setIgnoringElementContentWhitespace(true);//忽略缩进空白,无效果??????????
DocumentBuilder db = factory.newDocumentBuilder();
db.setEntityResolver(entityResolver);
db.setErrorHandler(new ErrorHandler(){ //当 setValidating(true)时有错误,但不会抛异常
			@Override
			public void error(SAXParseException e) throws SAXException {
				System.out.println("error======="+e.getMessage());
			}
			@Override
			public void fatalError(SAXParseException e) throws SAXException {
				System.out.println("fatalError======="+e.getMessage());
			}
			@Override
			public void warning(SAXParseException e) throws SAXException {
				System.out.println("warning======="+e.getMessage());
			}});
org.w3c.dom.Document dom = db.parse(XXX.class.getResourceAsStream("/employees.xml"));//db.parse("employees.xml");调用设置的EntityResolver
//<?xml 可有可无

//XPath
 XPath xpath = XPathFactory.newInstance().newXPath();
 XPathExpression expression = xpath.compile("//SQL[@Type='Value']");
 NodeList nodeList = (NodeList)expression.evaluate(doc,XPathConstants.NODESET);
for(int i = 0; i < nodeList.getLength(); i++){
	System.out.println(nodeList.item(i).getNodeName()+"="+nodeList.item(i).getTextContent());
}

Element root = dom.getDocumentElement();


NodeList nodeList=root.getElementsByTagName("Employee");//w3c的要带名称空间 如soapenv:Body,dom4j的可不用带

node.getAttributes().getNamedItem("Type").getTextContent();//属性也可以用getNodeValue()



---------XML JAXP  DOM 写   
TransformerFactory   tFactory=TransformerFactory.newInstance();
System.getProperty("javax.xml.transform.TransformerFactory");//org.apache.xalan.processor.TransformerFactoryImpl

Transformer   transformer=tFactory.newTransformer();//new StreamSource("")可传文件,可把Stream->Source
//<xsl:output 的属性
transformer.setOutputProperty( "encoding", "UTF-8"); //OutputKeys.ENCODING
transformer.setOutputProperty(OutputKeys.INDENT, "yes");//只换行不缩进
transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");//不要XML声明
transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");//对于使用使用Xalan-J,要和indent=yes一起使用

javax.xml.parsers.DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//javax
DocumentBuilder db = factory.newDocumentBuilder();
Document doc=db.newDocument();//建立
Element root=doc.createElement("root");
Attr  attr=doc.createAttribute("name");
attr.setValue("lisi"); 
root.setAttributeNode(attr);
root.appendChild(doc.createElement("person"));//是<person/>格式
doc.appendChild(root);
		
DOMSource   source=   new   DOMSource(doc);//如写Element 会丢失namespace,dom4j会保留namespace
StreamResult   stream   =   new   StreamResult( "test.xml");//参数可以为OutputStream
transformer.transform(source,stream); 

---------XML SAX1  读
//xercesImp.jar/META-INF/services/java.xml.parsers.SAXParserFactory 文件中记录实现类
SAXParserFactory spf = SAXParserFactory.newInstance();//org.xml包
SAXParser sp = spf.newSAXParser();
sp.parse(new InputSource(SAX.class.getResourceAsStream("/xml/dom/rule.xml")), new MyContenttHandler());

class MyContenttHandler extends DefaultHandler 
{
	//覆盖startDocument(),endDocument(),startElement(),endElement()
}

---------XML StAX  解析 (Streaming API for XML) JDK1.6新

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

List<Employee> empList = null;
Employee currEmp = null;
String tagContent = null;
XMLInputFactory factory = XMLInputFactory.newInstance();
XMLStreamReader reader =  factory.createXMLStreamReader(
	ClassLoader.getSystemResourceAsStream("xml/employee.xml"));
	 
while(reader.hasNext())
{
  int event = reader.next();
  switch(event)
  {
	case XMLStreamConstants.START_ELEMENT:
	  if ("employee".equals(reader.getLocalName()))
	  {
		currEmp = new Employee();
		currEmp.id = reader.getAttributeValue(0);
	  }
	  if("employees".equals(reader.getLocalName()))
	  {
		empList = new ArrayList<>();
	  }
	  break;
	   
	case XMLStreamConstants.CHARACTERS:
	  tagContent = reader.getText().trim();
	  break;
	   
	case XMLStreamConstants.END_ELEMENT:
	  switch(reader.getLocalName())
	  {
		case "employee":
		  empList.add(currEmp);
		  break;
		case "firstName":
		  currEmp.firstName = tagContent;
		  break;
		case "lastName":
		  currEmp.lastName = tagContent;
		  break;
		case "location":
		  currEmp.location = tagContent;
		  break;
	  }
	  break;
		 
	case XMLStreamConstants.START_DOCUMENT:
	  empList = new ArrayList<>();
	  break;
  }
}
---------XML StAX  生成
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

XMLOutputFactory xof = XMLOutputFactory.newInstance();
StringWriter writerStr = new StringWriter();
XMLStreamWriter xmlsw = xof.createXMLStreamWriter(writerStr);
 
//			PrintWriter writerXml = new PrintWriter(new OutputStreamWriter(
//					new FileOutputStream("d:/StAX.xml"), "utf-8"));  //创建文件指定字符集
//			XMLStreamWriter xmlsw = xof.createXMLStreamWriter(writerXml);

xmlsw.writeStartDocument("UTF-8", "1.0");
xmlsw.writeCharacters("\n");
xmlsw.writeStartElement("employees");
xmlsw.writeComment("省和城市信息");
xmlsw.writeCharacters("\n");

for (Employee po : list)
{
	xmlsw.writeCharacters(" ");
	xmlsw.writeStartElement("employee");
	xmlsw.writeCharacters("\n");
	
	//xmlsw.writeAttribute("id", String.valueOf(po.id));//报错???
	
	// 添加<id>节点
//				xmlsw.writeCharacters("   ");
//				xmlsw.writeStartElement("id");
//				xmlsw.writeCharacters(String.valueOf(po.id));
//				xmlsw.writeEndElement();
//				xmlsw.writeCharacters("\n");
	
	// 添加<name>节点
	xmlsw.writeCharacters("   ");
	if(po.firstName == null || po.firstName.trim().equals(""))
	{
		xmlsw.writeEmptyElement("name");
	}else
	{
		xmlsw.writeStartElement("name");
		xmlsw.writeCharacters(po.firstName);
		xmlsw.writeEndElement();
	}
	xmlsw.writeCharacters("\n");

	//end employee
	xmlsw.writeCharacters(" ");
	xmlsw.writeEndElement();
	xmlsw.writeCharacters("\n");
}
// 结束<employees>节点
xmlsw.writeEndElement();
xmlsw.writeCharacters("\n");

// 结束 XML 文档
xmlsw.writeEndDocument();
xmlsw.flush();
xmlsw.close();

xmlStr = writerStr.getBuffer().toString();
writerStr.close();

------------------DNS MX
Hashtable<String,String> env = new Hashtable<>();
env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
env.put("java.naming.provider.url", "dns://210.22.70.225");// DNS Server IP
DirContext dirContext = new InitialDirContext(env);
Attributes attrs = dirContext.getAttributes("163.com",new String[]{"MX"});//null全部的，new String[]{"MX"} 在DSN中只找MX记录
NamingEnumeration allAttr = attrs.getAll(); 
while (allAttr != null && allAttr.hasMoreElements()) //这只有一个MX记录，如果是传null,这里是多个
{
	System.out.println("----");
	Attribute attr = (Attribute) allAttr.next();
	NamingEnumeration e = attr.getAll();
	while (e.hasMoreElements())
	{
		String element = e.nextElement().toString();
		System.out.println(element);
	}
}
---------------动态创建类
//权限控制.   不可访问 网络,DB,文件(用policy文件吗)
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
Iterable<? extends JavaFileObject> sourcefiles = fileManager.getJavaFileObjects(JAVA_SOURCE_FILE);//一个存在的.java文件(可用程序生成)
compiler.getTask(null, fileManager, null, null, null, sourcefiles).call();
fileManager.close();
Process proc =Runtime.getRuntime().exec("cmd /c copy "+JAVA_CLASS_FILE+" bin/"+PACKAGE); //调用 系统命令
	 proc.waitFor();//如果命令运行时间较长,要用这个等待命令完成,linux下使用 a.sh >> a.log 1>&2 写日志没用效果
//Class x=Thread.currentThread().getContextClassLoader().loadClass(PACKAGE+"."+JAVA_CLASS_NAME);
 Class.forName(PACKAGE+"."+JAVA_CLASS_NAME);
 --- 
 
try (InputStream fis = new FileInputStream(source);
	OutputStream fos = new FileOutputStream(target))
{//try() 必须实现 AutoCloseable 接口 就不用在 finally中close()了

------------------
StringTokenizer tokenizer=new StringTokenizer("hello,world~abc","~,");
while(tokenizer.hasMoreElements())
{
	//System.out.println(tokenizer.nextToken());
	System.out.println(tokenizer.nextElement());//nextElement()和nextToken()效果相同
}

ResourceBundle bundle=ResourceBundle.getBundle("message",new Locale("zh","CN"));

ObjectOutputStream .writeObject(xx);
ObjectInputStream  .(DSAPublicKey)in.readObject();//反序列化
Beans.instantiate(classloader,"org.MyClass");



ResultSet .getTimestamp反加Timestamp,SimpleDateFormate  format( timestamp)有日期，有时间
Timestamp 的valueOf(String s)  返回一个Timestamp
GregorianCalendar(TimeZone zone)

TimeZone 的方法
	TimeZone getTimeZone(String ID)
	TimeZone getTimeZone(ZoneId zoneId) 
	static String[] getAvailableIDs()  


SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//不是同步，不是线程安全的,如要写static工具方法,要每次new
DateFormat formatMedium=DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM, Locale.CHINA);
 System.out.println(formatMedium.format(new Date()));// 2012-7-31 10:44:09
		 
format和parse方法
alter session set nls_date_format='yyyy-mm-dd hh24:mi:ss'//oracle大小写无关

Timer timer=new Timer();
timer.schedule(new TimerTask(){
	public void run() {
		System.out.println("这是定时里的任务");
	}
}, 3000,1000);//delay第一次开始延时间,period以后每次任务的执行时间
//主线程不会立即退出,而是一直运行着,复杂的用 Qutarz

//在类中是匿名构造方法,会在构造方法前被调用 ,在方法中只是一个区域
{
	int a=0;
	a++;
}

自定义   序列化   Externalizable  extends Serializable
实现方法  writeExternal()与readExternal()

Cloneable 接口没有方法,Object的clone方法要使用,必须实现Cloneable接口

 
 
-------- 
public class TestMain<T>
{
	List<T> search()
	{
		return null;
	}

	public static void main(final String[] args)
	{
		final TestMain a = new TestMain();
		final List<File> list = a.<File> search();
	}
}


PropertyChangeSupport changes    = new PropertyChangeSupport(this);
changes.addPropertyChangeListener(listener);
changes.firePropertyChange("userName", oldName, userName);

	/**
     * 等额本息
     * @param totalPrincipal 总货款额，全部本金
     * @param periods 贷款期限
     * @param interestRate 贷款利率
     * @return 每期还款额
     */
    public static double calcTotalEqual(double totalPrincipal, int periods,  double interestRate)  
    {
        if(periods <=0 || totalPrincipal <=0 )
            return 0;
        
        if (interestRate == 0)    
            return totalPrincipal/periods;
         else if(interestRate <0)
            return -1;
        
        double baseRate = 1 + interestRate;
        double b=Math.pow(baseRate, periods);
        
        double amt = totalPrincipal * interestRate * b / (b - 1);
        return amt;
    }	
	
常量池
String类的 intern() 

String a="abc";
String b=new String ("abc");
String i=b.intern();//i==a 是true;

Thread shutdownHook = new Thread() {
	@Override
	public void run() {
		 System.out.println("before JVM exits ,do something..");
	}
};
Runtime.getRuntime().addShutdownHook( shutdownHook);

Introspector.decapitalize("OuterClazz.InnerClazz");//当前两个字母不全是大写时,首字母小写

abstract class A<T> {
    public abstract T method2();
}
 
class B extends A<String> {
    public String method2() {
       return "abc";
    }
}

Method[] method=B.class.getMethods();
System.out.println(method[0].isBridge());//B继承abstract A,A有模板,B使用了A的模板,B生成的字节码多了brige方法,目的是为了可以覆盖A中的方法


UUID.fromString("c67f50c2-ac25-483b-a008-f982e3354a66");
String uuid=UUID.randomUUID().toString() ;//不能用于数据主键,只能重复
System.out.println(uuid); //算'-'共34位,不算'-'共31位
-----------Bean Validation
public class Order 
{
	@NotNull
    @Size(min=1)
	private List<Product> products;
	
	@Size(min=2,max=20 ,message="订单号必须为2-20的长度")
	private String orderId;
	
	@Length(min=2,max=20)//hiberante
	private String customer;
	
	@NotBlank 
	@Email  
	private String email;
	
	@NotNull(message="建立日期为null")
	@NotEmpty(message="建立日期不能为空串") 
	private String createDate;
	
	@Pattern(regexp="^[0-9]{13}$" ,message="手机号必须是13位数字")
	private String telepone;
	
	@javax.validation.constraints.Pattern(regexp="^(Y|N){1}$" ,message="isPay必须是Y或N")
    private String isPay;
	
	 
	@Digits(integer=4,fraction=2) //整数最多4位，小数最多2位
	@DecimalMax("5555.32")
	private float price;
	
	@Status(message="状态应只可是 'created', 'paid', shipped', closed'") //自定义验证
	private String status;  
	
	@Null(message="下一个必须为空")
	private Order nextOrder;
	
	@AssertTrue(message="有效必须为true")  // @AssertFalse 
	private boolean available;
	
	 @Valid   // 嵌套验证
	 private User user;
}
 @Constraint(validatedBy = {StatusValidator.class}) 
 @Documented 
 @Target( { ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD }) 
 @Retention(RetentionPolicy.RUNTIME) 
 public @interface Status { 
	 String message() default ""; 
	 Class<?>[] groups() default {}; 
	 Class<? extends Payload>[] payload() default {}; 
 }

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 public class StatusValidator implements ConstraintValidator<Status, String> 
{
	private final String[] ALL_STATUS = { "created", "paid", "shipped", "closed" };
	public void initialize(Status status) {
	}
	public boolean isValid(String value, ConstraintValidatorContext context) 
	{
		if (Arrays.asList(ALL_STATUS).contains(value))
			return true;
		return false;
	}
}


@EqualAttributes(message="{validation.passwordNotSame}",value={"password","rePassword"})//自定义验证
public class User {
	@Past(message="出生日期必须是过去的日期")   //@Future
	private Date birthday;
	
	@Min(value=18, message="年龄要>18岁")
	@Max(value=65, message="年龄要<64岁")
	//@Range(min=18,max=65, message="年龄要在18-65岁")//hiberane 的只能整数
	private int age;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "密码要有大小写字母和数字,3-20长度")
	private String password;
	private String rePassword;
}
 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualAttributesValidator.class)//自这定义的类
@Documented
public @interface EqualAttributes
{
	String message();
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String[] value();
}
public class EqualAttributesValidator implements ConstraintValidator<EqualAttributes, Object>
{
	private String firstAttribute;
	private String secondAttribute;
	@Override
	public void initialize(final EqualAttributes constraintAnnotation)
	{
		firstAttribute = constraintAnnotation.value()[0];
		secondAttribute = constraintAnnotation.value()[1];
	}
	@Override
	public boolean isValid(final Object object, final ConstraintValidatorContext constraintContext)
	{
		if (object == null)
			return true;
		try
		{
			final Object first = PropertyUtils.getProperty(object, firstAttribute);
			final Object second = PropertyUtils.getProperty(object, secondAttribute);
			return new EqualsBuilder().append(first, second).isEquals();
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.12.Final</version>
</dependency>

<dependency>
	<groupId>javax.validation</groupId>
	<artifactId>validation-api</artifactId>
	<version>2.0.1.Final</version>
</dependency>

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
Validator validator = factory.getValidator(); 
Set<ConstraintViolation<Order>> violations = validator.validate(order); 
 if(violations.size()> 0) 
 {
	 StringBuffer buf = new StringBuffer(); //可以做一个方法返回一个Map
	 ResourceBundle bundle = ResourceBundle.getBundle("bean_validation/message",Locale.CHINESE); 
	 for(ConstraintViolation<Order> violation: violations)
	 { 
		//bundle.getString(violation.getMessageTemplate() );//国际化的Key,或是自己的key
		buf.append( violation.getPropertyPath().toString()); 
		buf.append(violation.getMessage() ) .append("<BR>\n");
	 }
	 System.out.println(buf);
 }
--------
运行时加配置  -Djava.security.manager 则 System.getSecurityManager();反回非null

File currentDir=new File("./");
System.out.println("currentDir="+currentDir.getAbsolutePath());//\eclipse_java_workspace\J_JavaSE\.
		
SecurityManager sm = System.getSecurityManager();
if (sm != null) {
	FilePermission fp= new FilePermission("c:\\autoexec.bat", "read");
	sm.checkPermission(fp);
}

java -Djava.security.manager  -Djava.security.policy=bin/security/mytest/test.policy security.mytest.SecurityMainApp

jdk1.8.0\jre\lib\security 有示例
---policytool 命令有界面 生成policy 文件 test.policy
//grant {
  //permission java.security.AllPermission;
//};
grant {
  permission java.io.FilePermission "<<ALL FILES>>", "read";
  permission java.io.FilePermission "<<ALL FILES>>", "write";
  permission java.io.FilePermission "<<ALL FILES>>", "execute";
};
grant {
  permission java.util.PropertyPermission "user.dir", "read";
};


@Repeatable 用在了Spring的 PropertySource

//得到本机网卡 
Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
while (networkInterfaces.hasMoreElements()) {
	NetworkInterface networkInterface = networkInterfaces.nextElement();
	System.out.println("name=" + networkInterface.getName() + ",DisplayName=" + networkInterface.getDisplayName());
	Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
	while (inetAddresses.hasMoreElements()) {
		InetAddress inetAddress = inetAddresses.nextElement();
		if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
			String ip = inetAddress.getHostAddress();
			System.out.println(ip);
		}
	}
}


@FunctionalInterface  //如果接口只有一个未实现的方法(包括继承的)默认就是函数式接口,如果加这个,最多只能有一个未实现的接口
interface InterfaceJDK8
{
	static String CHAR_SET="UTF-8"; //接口的static变量会子类被继续下来
	
	default void objectMethodWithBody() //接口带方法实现,如对象方法必须以default声明
	{
		System.out.println(" in InterfaceJDK8  objectMethodWithBody");
	}
	
	static void staticMethodWithBody() //接口带方法实现,static方法不会被继续下来
	{
		System.out.println(" in  staticMethodWithBody");
	}
	void abstractMethod();
}
interface InterfaceJDK8Child extends InterfaceJDK8,ParentInterface 
{
	@Override
	default void objectMethodWithBody() {
		InterfaceJDK8.super.objectMethodWithBody();//super用法
	}
}

-- @Repeatable
 @Target( ElementType.TYPE )
   @Retention( RetentionPolicy.RUNTIME )
   public @interface Filters {
       Filter[] value();
   }
 
   @Target( ElementType.TYPE )
   @Retention( RetentionPolicy.RUNTIME )
   @Repeatable( Filters.class )
   public @interface Filter {
       String value();
   };
 
 
   @Filter( "filter1" )
   @Filter( "filter2" )
   public interface Filterable {
 
   }
   
  for( Filter filter: Filterable.class.getAnnotationsByType( Filter.class ) ) {
           System.out.println( filter.value() );
       }
	   
ElementType.TYPE_USE 和ElementType.TYPE_PARAMETER 

-- CompletableFuture

public class TheImplFutureClass implements Runnable {
	CompletableFuture<Integer> re = null;

	public TheImplFutureClass(CompletableFuture<Integer> re) {
		this.re = re;
	}

	@Override
	public void run() {
		int myRe = 0;
		try {
			myRe = re.get() * re.get();// 依赖于等待另一个线程计算完成
		} catch (Exception e) {
		}
		System.out.println(myRe);
	}

	public static Integer calc(Integer para) {
		try {
			Thread.sleep(1000); // 模拟一个长时间的执行
		} catch (InterruptedException e) {
		}
		return para * para;
	}

	public static void main(String[] args) throws   Exception
	{
		final CompletableFuture<Integer> future = new CompletableFuture<Integer>();
		new Thread(new TheImplFutureClass(future)).start();
		Thread.sleep(1000);
		future.complete(60); // 告知完成结果

		final CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> calc(50));
		System.out.println(future1.get());
		
		CompletableFuture<Void> fu = CompletableFuture .supplyAsync(() -> calc(50)) 
				.thenApply((i) -> Integer.toString(i))
				.thenApply((str) -> "\"" + str + "\"")
				.thenAccept(System.out::println); //像scala
		 fu.get();
		 
		  CompletableFuture<Void> fu1 = CompletableFuture.supplyAsync(() -> calc(50))
				//把上一个计算结果 传入下面的i
				 .thenCompose(
						 		(i) -> CompletableFuture.supplyAsync(() -> calc(i))
						 )
				 .thenApply((str) -> "\"" + str + "\"")
				 .thenAccept(System.out::println);
		 fu1.get();
		 
	}
}



StampedLock sl = new StampedLock();

 
//---java.beans
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
public static void transMap2Bean(Map<String, Object> map, Object obj) throws Exception// Map->Bean
{
	BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
	PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	for (PropertyDescriptor property : propertyDescriptors) {
		String key = property.getName();
		if (map.containsKey(key) && !"class".equals(key)) { //转换的map有class属性,key也有
			Object value = map.get(key);
			Method setter = property.getWriteMethod(); // 得到property对应的setter方法
			setter.invoke(obj, value);
		}
	}
	return;
}

public static Map<String, Object> transBean2Map(Object obj) throws Exception // Bean->Map
{
	if (obj == null) {
		return null;
	}
	Map<String, Object> map = new HashMap<String, Object>();
	BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());// java.beans.Introspector
	PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	for (PropertyDescriptor property : propertyDescriptors) {
		String key = property.getName();
		Method getter = property.getReadMethod(); // 得到property对应的getter方法
		Object value = getter.invoke(obj);
		map.put(key, value);
	}
	return map;
}
----SPI
SPI的全名为Service Provider Interface
import java.util.ServiceLoader;
//找所有classpath路径下 /META-INF/services/<接口全类名>   文件，内容为全类名的实现类
ServiceLoader<Developer> serviceloader = ServiceLoader.load(Developer.class);
//implements Iterable
for (Developer dev : serviceloader) {
	System.out.println("find ." + dev.getPrograme());
}


