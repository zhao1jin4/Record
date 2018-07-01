
---------------------------------zookeeper
version 是保存最后修改的次数

zookeeper-3.4.6\conf\zoo_sample.cfg 修改为  zoo.cfg

windows版本直接 zkServer.cmd  启动
bin/zkServer.sh start  / start-foreground / stop 
bin/zkServer.sh status 看是leader

----zoo.cfg
clientPort=2181  	监听端口
tickTime=2000 		心跳时间2秒
dataDir=/tmp/zookeeper

#集群配置
initLimit=5 	初始化连接时最长能忍受多少个心跳,表示leader服务器允许follow服务器启动时同步的最大时间()
syncLimit=2 	发送消息最长不能超过多少个心跳,表示leader服务器和follow服务器的心跳检查最大延时时间
server.1=192.168.211.1:2888:3888 
server.2=192.168.211.2:2888:3888
server.3=192.168.211.3:2888:3888    

#server.第几号服务器=IP:Leader的端口:Leader备份端口
最后一个端口　　表示万一leader挂了，使用这个端口来选出leader

#在dataDir配置项指定的目录中建立myid文件,中写自己的ID号,集群中多台机器各不相同,与server.1 等匹配
zookeeper 如未超半数机器挂，就可以提供服务，通常机器数是单数


dataLogDir 			     事务日志,在写dataDir前,先写这个,为保证速度应该放在不同磁盘
globalOutstandingLimit    最大请求未处理队列数,默认1000
preAllocSize  			事务日志块的大小,默认64m
snapCount				多少次事务后 建立一次快照
maxClientCnxns			单台客服端(用IP) 和单个服务器的最大连接数,默认60,0表示没有限制
clientPortAddress		客户端连接的地址,即哪个网卡
minSessionTimeout
maxSessionTimeout
fsync.warningthresholdms		当同步日志时间大于这个(milliseconds ),会有warn消息写到日志,默认1000
autopurge.snapRetainCount		自动清理快照文件 和事务日志文件时,保留的数量,最小配置为3
autopurge.purgeInterval			默认0 不启用清理快照文件 和事务日志文件,单位小时
electionAlg					默认3 TCP-based version of fast leader election. (0,1,2 过时)
leaderServes				是否要leader处理客户端请求,默认yes
cnxTimeout					连接超时时间(leader election notifications),默认5秒


forceSync					完成操作前是否时时写入日志到磁盘 (不安全选项)
jute.maxbuffer 		必须用java system properties 设置才生效 ,必须每个服务和客户端都设置 ,表示一个znode最大存储储数据量,默认1m(不安全选项)

skipACL		跳过acl检查		

bin/zkCli.sh -server 127.0.0.1:2181   可以选项 -timeout 0 毫秒  -r 表示只读，如有超过半数服务连接断开，就不处理客户端请求，但可以处理只读请求
也可以用 connect 127.0.0.1:2181 来连接
] h 显示所有命令
] ls /
] create /zk_test my_data    
] create -s -q /tmp my_tmp_data   可加-s 表示序列,节点名后加序列号,可做分布式主键生成器,-e 表示临时
] create /acl_ip_test ip:10.1.5.225:crwda   			crwda=create,read,write,delete,admin
] create /acl_digest_test digest:myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=:crwda   中间myuser:CmVSQ2nhuKrMPNW7BK6HrthawaY=是使用DigestAuthenticationProvider.generateDigest("myuser:mypass")生成的
也可以使用 setAcl  /acl_ip_test ip:10.1.5.225:crwda 

] get /zk_test  得到数据值

] addauth digest user:pass  如有节点不能get ,可以加用户密码

] set /zk_test junk   可加版本号,影响dataVersion,必须是上一次返回的版本,可以实现乐观锁
] stat /zk_test

cZxid = 0x0			每一次写是一次事务,有一个ID标识,表示建立时的事务ID,即在哪个事务中建立的
ctime = Thu Jan 01 08:00:00 CST 1970	创建时间
mZxid = 0x0							    最后一次更新时的事务ID	
mtime = Thu Jan 01 08:00:00 CST 1970    修改时间
pZxid = 0x600000008						子节点里最后一次修改的事务ID (不包括修改子节点的数据内容,只是增删节点)
cversion = 2							子节点版本
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0			创建该临时节点的事务ID
dataLength = 0
numChildren = 2

] delete /zk_test  只可为空时才可删
] ls2 /zk_test 		有stat的功能
] rmr /dir  		可以删有子级点的节点
] setquata -n 表示子级点的个数 -b 数据值的长度,包括子级节点 val path  ,加节点限制功能(不能修改,只能删了再建) ,如有有超出限制不会报错,只记录WARN日志
] listquota
] delquota  -n /-b path
] history   查历史
] redo history编号
] quit 
] connect 连接到其它机器   close 后无法再返回了


4个字母的命令  nc=net cat 
echo stat | nc  127.0.0.1 2181  也可以用 telnet 127.0.0.1 2181 后 stat  
	有信息 Mode: standalone,Mode:fllower,Mode:leader
echo conf | nc  127.0.0.1 2181  查看配置
echo cons | nc  127.0.0.1 2181  查看所有客户端
echo crst | nc  127.0.0.1 2181   重置所有客户端的连接统计信息（Client Reset STatistics ）
echo srst | nc  127.0.0.1 2181   重置所有服务连接统计信息（Server Reset STatistics ）
echo dump | nc  127.0.0.1 2181    只用于leader ,显示会话信息
echo envi | nc  127.0.0.1 2181    环境变量
echo ruok | nc  127.0.0.1 2181   ,are you ok ,    当前服务是否在运行,返回imok
echo srvr | nc  127.0.0.1 2181   服务器信息
echo wchs | nc  127.0.0.1 2181  显示watcher
echo wchc  | nc  127.0.0.1 2181  显示watcher,以sessoin列出
echo wchp  | nc  127.0.0.1 2181   by path
echo mntr  | nc  127.0.0.1 2181    监控cluster健康键值输出信息


cd <zookeeper_home>/src/c 
./configure
make cli_mt		(multi-threaded) 
make cli_st		(single-threaded)
make install

cd src/c 
./cli_mt
./cli_st


zookeeper不会删老的快照和日志文件

 java -cp zookeeper.jar:lib/slf4j-api-1.6.1.jar:lib/slf4j-log4j12-1.6.1.jar:lib/log4j-1.2.15.jar:conf 
 org.apache.zookeeper.server.PurgeTxnLog <dataDir> <snapDir> -n <count>

最少count配置3
3.4.0 以后配置可以通过配置 autopurge.snapRetainCount 和  autopurge.purgeInterval 来自动清除transaction logs和snapshots 


ZooKeeper zk = new ZooKeeper("localhost:2181",2000, new Watcher() { 
	public void process(WatchedEvent event) { 
	 	System.out.println("已经触发了" + event.getType() + "事件！"); 
		if(event.getState()==KeeperState.SyncConnected)
		{
			 System.out.println("SyncConnected事件！"); 
		}
		 if(event.getType()==EventType.NodeChildrenChanged)
	           System.out.println(event.getPath()+"已经修改了"); 
	} 
});

//		 Perms.ALL
//		 Perms.ADMIN|Perms.CREATE|Perms.DELETE|Perms.READ|Perms.WRITE
 ACL aclIp=new ACL(Perms.READ,new Id("ip","10.1.5.225"));//Id构造器参数schema只可是ip(白名单)或digest(用户名密码)
 ACL aclDigest=new ACL(Perms.READ|Perms.WRITE,new Id("digest",DigestAuthenticationProvider.generateDigest("myuser:mypass")));
 ArrayList<ACL> aclList=new ArrayList<>();
 aclList.add(aclIp);
 aclList.add(aclDigest);
 zk.create("/testAclPath", "testAclPathVal".getBytes(),aclList, CreateMode.PERSISTENT); 
//		  //使用命令   getAcl /testAclPath  能看到
 zk.addAuthInfo("digest", "myuser:mypass".getBytes());//同命令  addauth digest  myuser:mypass
 System.out.println(new String(zk.getData("/testAclPath",false,null))); 
		 
		 
zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
zk.create("/testRootPath2", "testRootData2".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,
		new AsyncCallback.StringCallback() {
			@Override
			public void processResult(int rc,  String path,  Object ctx,  String name) {
				//rc=0表示成功,ctx是传来的"创建"字符
				if(rc == KeeperException.Code.NODEEXISTS.intValue()  )
					System.out.printf("异步创建 path=%s 已经存在\n",path ); 
			
				 System.out.printf("异步创建rc=%d,path=%s,ctx=%s,name=%s\n",rc,path,ctx,name); 
			}
		},"创建"
	);
 zk.getChildren("/testRootPath", true, new AsyncCallback.Children2Callback() {//boolean watch
	@Override
	public void processResult(int rc,  String path,  Object ctx,  List<java.lang.String> children ,Stat stat) {
		if(rc == KeeperException.Code.NONODE.intValue()  )
			System.out.printf("异步getChildren  path=%s 不存在\n",path ); 
		else
			System.out.println("异步getChildren  "+children); 
	}
}, "传参ctx");
System.out.println(zk.getChildren("/testRootPath",true)); //子节点, boolean watch 是否观心子节点的变化
zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),  Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); //Watcher的NodeChildrenChanged

Stat reveStat=new Stat();
System.out.println(new String(zk.getData("/testRootPath",false,reveStat))); //boolean watch

 zk.getData("/testRootPath/testChildPathTwo",true,new DataCallback() {
		@Override
		public void processResult(int rc,  String path,  Object ctx, byte[] data, Stat stat) {
			System.out.println("异步getData  "+new String(data)); 
		}
	},"ctx value");

System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]"); //boolean watch
zk.exists("/testRootPath", true, new StatCallback() {
		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			System.out.println("异步exists  rc="+rc); 
		}}, "ctx");
		
zk.setData("/testRootPath","modifyData".getBytes(),-1); 
 zk.setData("/testRootPath/testChildPathTwo","modifyChildDataOne".getBytes(),-1,new StatCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, Stat stat) {
				 System.out.println("异步 setData rc="+rc); 
			}
		},"ctx"); 
zk.delete("/testRootPath/testChildPathTwo",-1,new VoidCallback() {
		@Override
		public void processResult(int rc, String path, Object ctx) {
			System.out.println("异步delete  rc="+rc); 
		}},"ctx val"); 
zk.delete("/testRootPath",-1); //-1表示任何版本
zk.close();

extends ZooKeeperServerMain //类里有main方法
	this.initializeAndRun("/tmp/zoo.cfg")


CreateMode.EPHEMERAL //临时,客户端连接断会被删 ,ephemeral 短暂的
CreateMode.EPHEMERAL_SEQUENTIAL //临时,名字按序号自动加1 ,选 Master 时,以最小的号
 
// Ids.OPEN_ACL_UNSAFE 任何人都可以访问
// Ids.AUTH_IDS 创建者拥有访问权限
// Ids.READ_ACL_UNSAFE  任何人都可以读
//Ids.CREATOR_ALL_ACL //使用addAuthInfo信息,作为建立节点的acl信息

--- 配置 JMX 管理Zookeeper

修改 zkServer.sh  在现有的 ZOOMAIN 配置中 com.sun.management.jmxremote.local.only=false  默认就是false
现有的  -Dcom.sun.management.jmxremote.port=$JMXPORT 
ZOOMAIN="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.port=8899 -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.authenticate=false"
windows 下是 set ZOOMAIN=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.port=8899 -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.authenticate=false org.apache.zookeeper.server.quorum.QuorumPeerMain
如是真集群每个都修改
重新启动后，就可以用jconsole 来连接  127.0.0.1:8899  ,端口即com.sun.management.jmxremote.port的值
MBeans标签->org.apache.ZookeeperService->下面显示的名字有myid的值
 
----Zabbix  ,  exhibitor 来临控 

exhibitor 下载源码后  https://github.com/soabase/exhibitor
要求 每台zookeeper机器运行exhibitor jar才可以,配置简单

mvn clean package
把生成的.jar放服务器上
java -jar exhibitor-1.5.6.jar -c file   --port 8888 启动用,打开 http://127.0.0.1:8888/

config 面板 Servers配置中S=Standard,O=observer  
对应zoo.cfg配置如 S:1:10.1.5.225,S:2:10.1.5.226,S:3:10.1.2.159,   
提交配置会重写配置文件,小心

配置后Control Panel就会每台机器的例表,4LTR...按钮可以运行4字命令, 有自动重启,日志清除 
Explorer标签 查看和修改 目录树



