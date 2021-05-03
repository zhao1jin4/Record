
-------------------------------------------- RabbitMQ  3.7.7
RabbitMQ有scala go,c++ 客户端, OpenStack使用这个有Python客户端

https://rabbitmq.github.io/rabbitmq-java-client/api/current/

版本　3.7.7　要 ERLang语言(为分布式,erlc 编译语言)　版本至少 20.3
 
RabbitMQ,启动停止可在services.msc中做也可使用命令启动
配置文件 是 rabbitmq.config  
C:\Program Files\RabbitMQ Server\rabbitmq_server-3.7.7\etc\rabbitmq.config.example 复制修改 (3.7.11版本就没这个文件了？)
#%HOMEPATH%\AppData\Roaming\RabbitMQ\rabbitmq.config.example
默认端口  {tcp_listeners, [5672]},

linux 下解压有sbin目录 rabbitmqctl 要 erl　命令  ,安装erlang不太容易 CentOS 7 下 yum install erlang 根本没有
centos 7
su -c 'rpm -Uvh http://download.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-10.noarch.rpm'
su -c 'yum install erlang' 就可以了

linux sbin目录下  	
./rabbitmq-server 前台启动
./rabbitmq-server  -detached    后台启动
./rabbitmqctl stop   停止
看界面默认配置文件 解压的 rabbitmq_server-3.7.7/etc/rabbitmq/rabbitmq.config  
看控制台默认日志在 解压的 rabbitmq_server-3.7.7/var/log/rabbitmq/rabbit@<hostname>.log
 看界面默认数据目录     rabbitmq_server-3.7.11/var/lib/rabbitmq/mnesia/rabbit@<hostname>
 
windows zip 设置 ERLANG_HOME=D:\Program\erl9.3\ 变量  
看控制台默认日志 %HOMEPATH%\AppData\Roaming\RabbitMQ\log  可配置环境变量 RABBITMQ_LOG_BASE
看界面默认配置文件 %HOMEPATH%/AppData/Roaming/RabbitMQ/rabbitmq.config
看界面默认数据目录 %HOMEPATH%\AppData\Roaming\RabbitMQ\db\RABBIT~1
看界面默认amp端口是 5672
看界面默认clustering端口是 25672
 
windows sbin/rabbitmq-server.bat 管理员安装普通用户启动会自己复制到主目录，提示日志和数据目录在主目录中
windows安装版本服务启动 Cookie
从 C:\Windows\System32\config\systemprofile\.erlang.cookie 复制到
C:\Users\%USERNAME%\.erlang.cookie 

右击RabbitMQ服务->登录->用户为安装时指定的管理员用户(日志文件也在这个管理员用户目录下)
保证当前用户和管理用户的.erlang.cookie 都是从systemprofile目录复制过来的

/var/lib/rabbitmq/.erlang.cookie (used by the server) 
$HOME/.erlang.cookie (used by CLI tools)

rabbitmqctl status
rabbitmqctl  add_user zh 123  创建用户名密码 
rabbitmqctl  list_users
rabbitmqctl  change_password  zh  456
rabbitmqctl  delete_user  zh
rabbitmqctl  set_user_tags  zh  administrator  就有权限远程登录了

rabbitmqctl  add_user mon 123 
rabbitmqctl  set_user_tags  mon  monitoring  就有权限远程登录了
(policymaker，management)
rabbitmqctl  list_user_permissions  mon
rabbitmqctl list_queues

rabbitmq-plugins  enable rabbitmq_management    开启网页管理界面 15672 端口 (windows要使用命令启动服务,才可仿问界面，用户名要用安装时的管理员) 

http://127.0.0.1:15672/api/index.html 有 Resetful 接口文档


rabbitmqctl add_vhost myVhost
rabbitmqctl delete_vhost myVhost
rabbitmqctl list_vhost
rabbitmqctl list_vhosts name tracing

#rabbitmqctl set_permissions [-p vhost] user conf write read #格式
rabbitmqctl set_permissions  -p myVhost zh ".*" ".*" ".*"
rabbitmqctl set_permissions  -p / zh ".*" ".*" ".*"
rabbitmqctl clear_permissions  -p myVhost  zh
rabbitmqctl list_permissions  -p myVhost 
rabbitmqctl list_user_permissions zh

#命令有 list_queues, list_exchanges, list_bindings and list_consumers
rabbitmqctl list_exchanges -p myVhost name type
#rabbitmqctl list_bindings -p myVhost exchange_name queue_name 格式
rabbitmqctl list_bindings -p myVhost
rabbitmqctl list_connections send_pend port
rabbitmqctl list_connections
rabbitmqctl list_channels connection messages_unacknowledged




http://127.0.0.1:15672/     guest/guest  只可localhost登录 可以建立Queue
还有其它工具
http://127.0.0.1:15672/api
http://127.0.0.1:15672/cli 

STOMP 插件 (所有可用插件文件位于plugins目录下)
rabbitmq-plugins enable rabbitmq_stomp　　默认监听　61613　端口

修改端口　rabbitmq.conf　新的是sysctl格式,即properties格式　(3.7 以前版本是rabbitmq.config　是json格式)
stomp.listeners.tcp.1 = 12345


还要在界面中配置 Admin-> Virtual Hosts 配置/ 虚拟机的用户仿问权限

<dependency>
  <groupId>com.rabbitmq</groupId>
  <artifactId>amqp-client</artifactId>
  <version>5.2.0</version>
</dependency>

dependencies {
  compile 'com.rabbitmq:amqp-client:5.0.0'
}



String message = "Hello World!";
String EXCHANGE_NAME = "myExtchange";
String ROUTING_KEY = "routingKey"; 
String QUEUE_NAME = "myQueueName";

--sender
ConnectionFactory factory = new ConnectionFactory(); 
factory.setUsername("zh"); 
factory.setPassword("123"); 
factory.setVirtualHost("/"); 
factory.setHost("127.0.0.1"); 
factory.setPort(5672); 
Connection conn = factory.newConnection(); 
Channel channel = conn.createChannel(); 
//会按 名自动建立exchange
//direct可用 BuiltinExchangeType.DIRECT;
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); //boolean durable  持久化 


//AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().replyTo(callbackQueueName).build();//回调的Queue
AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; //当durable true时
		
//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,props,message.getBytes());
System.out.println(" [x] Sent '" + message + "'");

channel.close(); 
conn.close();

--receiver
//RabbitMQ同一时间发给消费者的消息不超过一条
//这样就能保证消费者在处理完某个任务，并发送确认信息后，RabbitMQ才会向它推送新的消息
//在此之间若是有新的消息话，将会被推送到其它消费者，若所有的消费者都在处理任务，那么就会等待。
int prefetchCount = 1;
channel.basicQos(prefetchCount);//RPC 放消费端       允许限制通道上的消费者所保持最大的未确认消息数量，如某台机器反应慢，

channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);  //boolean durable  持久化 
		
String queueName = channel.queueDeclare().getQueue(); //自动建立Queue,自动取名,是AD=AutoDelete,Excl=Exlusive
System.out.println("queueName="+queueName);

//自动建立Queue,D=durable
//String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments
channel.queueDeclare(QUEUE_NAME, true, false, false, null);//且持久化要和现有的配置相同,即如果Queue已经存在不能变durable了
System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

Consumer consumer = new DefaultConsumer(channel) {
	  @Override
	  public void handleDelivery(String consumerTag, Envelope envelope,
								 AMQP.BasicProperties properties, byte[] body)
		  throws IOException {
		String message = new String(body, "UTF-8");
		System.out.println(" [x] Received '" + message + "'");
		
		 //对于basicConsume  autoAck为false时
		//channel.basicAck(envelope.getDeliveryTag(),//消息标识
		//		false);//multiple 是否多个,即这个标识前面的一次性全部认为Ack收到了
		//还有Nack(可多个)不知道(未收到) 和 Reject (只可一个)
		
	  }
	};
channel.basicConsume(QUEUE_NAME, true, consumer);//boolean autoAck,true收到消息就自动应答,false要手工应答(在消息任务完成后)

----
//Get方式取消息 立即返回
do 
{
  GetResponse response=channel.basicGet(QUEUE_NAME, false); //参数queue, autoAck
  if(response==null)
  {
  Thread.sleep(2000);
  continue;
  }
  String message = new String(response.getBody(), "UTF-8");
  System.out.println("GET 立即返回 取消息结果为："+message);
  
  //对于 autoAck为false时
  channel.basicAck(response.getEnvelope().getDeliveryTag(),//消息标识
    false);//multiple 是否多个,即这个标识前面的一次性全部认为Ack收到了
  //还有Nack(可多个)不知道(未收到) 和 Reject (只可一个)
  
   //---相当于stack的peek
			//channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);//long deliveryTag, boolean multiple, boolean requeue,如为false变以为discarded/dead-lettered

    
}while(true);
----
Direct Exchange 将一个队列绑定到交换机上，要求该消息与一个特定的路由键routing key完全匹配
Fanout Exchange 个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上(忽略routing key)
Topic Exchange 队列名是一个模式上， 匹配有两种方式all和any 
			符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”的routeKey(未测试)
Headers exchange  不是使用routingkey去做绑定。而是通过消息headers的键值对匹配,发送者在发送的时候定义一些键值对，接收者也可以再绑定时候传入一些键值对
接收端必须要用键值"x-mactch"来定义。all代表定义的多个键值对都要满足，而any则代码只要满足一个就可以了。
---类型headers   sender
  
//(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.HEADERS,false,true,null);  
String message = new Date()+ " : log something";  
  
Map<String,Object> headers =  new Hashtable<String, Object>();  
headers.put("aaa", "01234");  
Builder properties = new BasicProperties.Builder();  
properties.headers(headers);  
  
// 指定消息发送到的转发器,绑定键值对headers键值对  
//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
channel.basicPublish(EXCHANGE_NAME, "",properties.build(),message.getBytes());  
channel.close();  
connection.close();

---类型headers  receiver 
 //(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.HEADERS,false,true,null);  

//String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments
channel.queueDeclare(QUEUE_NAME,false, false, true,null);  

Map<String, Object> headers = new Hashtable<String, Object>();  
headers.put("x-match", "any");//all any  
headers.put("aaa", "01234");  
headers.put("bbb", "56789");  
// 为转发器指定队列，设置binding 绑定header键值对  
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"", headers);  
Consumer consumer = new DefaultConsumer(channel) {
  @Override
  public void handleDelivery(String consumerTag, Envelope envelope,
							 AMQP.BasicProperties properties, byte[] body)
	  throws IOException {
	String message = new String(body, "UTF-8");
	System.out.println(" [x] Received '" + message + "'");
  }
}; 
// 指定接收者，第二个参数为自动应答，无需手动应答 
//(String queue, boolean autoAck, Consumer callback		
channel.basicConsume(QUEUE_NAME, true, consumer);   
----rabbitMQ 事务 (也是发送)

//(String exchange, String type,  boolean durable, boolean autoDelete, Map<String,Object> arguments)
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);  

//(String queue,  boolean durable,  boolean exclusive,  boolean autoDelete,   Map<String,Object> arguments)
channel.queueDeclare(QUEUE_NAME, true, false, false, null);  
 
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);  
			
channel.txSelect();  
	//(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
	channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_BASIC, ("第"+(i+1)+"条消息").getBytes("UTF-8")); 
channel.txCommit();   
channel.txRollback();   //catch中回滚操作
//阻塞，即回复后才可发下一条消息。


----TTL  DLX 
//---TTL
Map<String, Object>  argss = new HashMap<String, Object>();
argss.put("vhost", "/");
argss.put("username","root");
argss.put("password", "root");
argss.put("x-message-ttl",6000); //超过指定时时间如没有消费就不能消费了
//队列级别设置TTL  Time-To-Live  
channel.queueDeclare(queueName, durable, exclusive, autoDelete, argss);
不设置TTL,则表示此消息不会过期
如果将TTL设置为0，则表示除非此时可以直接将消息投递到消费者,否则该消息会被立即丢弃

//也可使用命令设置
// rabbitmqctl set_policy TTL ".*" '{"message-ttl":60000}' --apply-to queues

//消息级别设置TTL
AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
builder.deliveryMode(2);//持久化
builder.expiration("6000");
AMQP.BasicProperties  properties = builder.build();
channel.basicPublish(exchangeName,routingKey,mandatory,properties,"ttlTestMessage".getBytes());
 
//队列设置和消息都设置TTL较小的那个数值为准 
channel.exchangeDeclare("some.exchange.name", "direct");


//---Dead Letter Exchange  (DLX)
进入死信的情况
1.消息过期
2.消息被拒绝 Base.Reject/Base.Nack,并且requeue为false
3.队列达到最大长度

Map<String, Object> args = new HashMap<String, Object>();
args.put("x-dead-letter-exchange", "some.exchange.name");
channel.queueDeclare("myqueue", false, false, false, args);


args.put("x-dead-letter-routing-key", "some-routing-key");

rabbitmqctl set_policy DLX ".*" '{"dead-letter-exchange":"my-dlx"}' --apply-to queue
rabbitmqctl set_policy DLX ".*" "{""dead-letter-exchange"":""my-dlx""}" --apply-to queues //windows

界面中D=durable
DLK= x-dead-Letter-routing-Key

----ConfirmListener
 
//publish confirm模式  ，只能通道回复了即可发送下一条（Basic.Publish,Basic.Ack），比事务(Basic.Publish,Tx.Commit,Tx.Commit.OK)少发一条指令 如果消息是要持久化，都在存磁盘后回复
//publish confirm模式  ，只能通道回复了即可发送下一条（异步），事务是同步 阻塞，即回复后才可发下一条消息。
channel.confirmSelect();


//异步确认
/*
channel.addConfirmListener(new ConfirmListener() {
 @Override
 public void handleAck(long deliveryTag, boolean multiple) throws IOException {
  System.out.println("handleAck: deliveryTag="+deliveryTag+",multiple="+multiple);
 }
 @Override
 public void handleNack(long deliveryTag, boolean multiple) throws IOException {
  System.out.println("handleNack: deliveryTag="+deliveryTag+",multiple="+multiple);
 }});
*/

AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; //当durable true时
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); //durable 持久化  ,  BuiltinExchangeType.DIRECT 
//durable true 如果没有消费 ,重启后还有的，但持久化前是先缓存的

for(int i=0;i<2;i++)
{
  //(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
  channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,   props,
    (message+i).getBytes());
 System.out.println(" [x] Sent '" + message +i+ "'");
}
//同步确认
//只对channel.confirmSelect()后使用，可以发送多个消息后一次confirm
boolean confirmRes=channel.waitForConfirms();
System.out.println("confirmRes="+confirmRes); 

-----ReturnListener
channel.confirmSelect(); 
 
		//对失败的发送, 模拟方式使用错误的 routingKey
		//要求 basicPublish 带 mandatory 或者 immediate 标志
		/*
		channel.addReturnListener(new ReturnListener() {//如要使用lambda语法 使用 ReturnCallback
			@Override
			public void handleReturn(int replyCode,String replyText, String exchange, String routingKey, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println("ReturnListener replyCode="+replyCode
						+",exchange="+exchange
						+",routingKey="+routingKey
						//+",properties="+properties
						+",body="+new String(body) 
						);
				//只对失败，做重发
			}
		});
		*/
		channel.addReturnListener( new ReturnCallback() {
			@Override
			public void handle(Return returnMessage) {
				System.out.println("ReturnCallback replyCode="+returnMessage.getReplyCode()
				+",returnMessage.toString()="+ ToStringBuilder.reflectionToString(returnMessage));
				//只对失败，做重发
			}
		});
		AMQP.BasicProperties props =MessageProperties.PERSISTENT_TEXT_PLAIN; 
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);  
		for(int i=0;i<2;i++)
		{
			//(String exchange, String routingKey, boolean mandatory, boolean immediate, AMQP.BasicProperties props, byte[] body)
			//mandatory为true
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,  true,false, props, (message+i).getBytes());
		 	System.out.println(" [x] Sent '" + message +i+ "'");
		}  
  
----
String jsonStr = new com.rabbitmq.tools.json.JSONWriter().write(para);//Object(可Map)->JSON
Object obj = new com.rabbitmq.tools.json.JSONReader().read(jsonStr);//返回Object是一个HashMap

-------delay message 
https://www.rabbitmq.com/community-plugins.html
 
https://github.com/rabbitmq/rabbitmq-delayed-message-exchange
rabbitmq_delayed_message_exchange-3.8.0.ez (支持rabbitmq-3.7-3.8.4)文件放 于plugins目录下

rabbitmq-plugins enable rabbitmq_delayed_message_exchange

// ... elided code ...
Map<String, Object> args = new HashMap<String, Object>();
args.put("x-delayed-type", "direct");
channel.exchangeDeclare("my-exchange", "x-delayed-message", true, false, args);
// ... more code ...


// ... elided code ...
byte[] messageBodyBytes = "delayed payload".getBytes("UTF-8");
Map<String, Object> headers = new HashMap<String, Object>();
headers.put("x-delay", 5000);
AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
channel.basicPublish("my-exchange", "", props.build(), messageBodyBytes);

byte[] messageBodyBytes2 = "more delayed payload".getBytes("UTF-8");
Map<String, Object> headers2 = new HashMap<String, Object>();
headers2.put("x-delay", 1000);
AMQP.BasicProperties.Builder props2 = new AMQP.BasicProperties.Builder().headers(headers2);
channel.basicPublish("my-exchange", "", props2.build(), messageBodyBytes2);
// ... more code ...

------RabbitMQ Cluster 

单台可满足每秒1000条消息吞吐

RAM 节点和　DISK节点

每台机器的cookie要是相同的文件做 ~/.erlang.cookie  存字串,集群中每个节点这个值是相同的,如不存在会创建 

多台机器分别单实例启动，以一个实例为基础，其它全部加入这个实例
其中一个实例
1. rabbitmqctl stop_app  不停止rabbitmq进程,而是停止erlang的内部进程
2. rabbitmqctl reset 重置内部数据
3. rabbitmqctl join_cluster rabbit@<hostname>   这个名字在管理界面的Admin->Cluster中可以修改的
4. rabbitmqctl start_app

rabbitmqctl cluster_status
[runing_nodes,[node1,node2]]

如全关闭后，再启动，应先启动最后关闭的，否则会等最后关闭的启动
rabbitmqctl forget_cluster_node nodex  从集群中去除节点

