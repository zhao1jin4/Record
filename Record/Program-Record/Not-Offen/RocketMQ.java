
======================RocktMQ   alibaba 捐给了apache

控制台 
https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console
mvn spring-boot:run
或者
mvn clean package -Dmaven.test.skip=true
java -jar target/rocketmq-console-ng-1.0.0.jar  --server.port=8888 --rocketmq.config.namesrvAddr=127.0.01:9876;192.168.1.107:9876

http://127.0.0.1:8080/ 就有界面了  OPS 中有 NameSvrAddrList   地址 
消息标签 可以查看消息体，可以重发

<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-common</artifactId>
	   <version>4.2.0</version>
</dependency>
<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-client</artifactId>
	   <version>4.2.0</version>
</dependency>
<dependency>
	   <groupId>org.apache.rocketmq</groupId>
	   <artifactId>rocketmq-remoting</artifactId>
	   <version>4.2.0</version> 
</dependency>
 
4.2
环境变量  ROCKETMQ_HOME=
启动Name Server 
bin/mqnamesrv

启动Broker  
bin/mqbroker -n localhost:9876  autoCreateTopicEnable=true
  
停服务
bin/mqshutdown broker
bin/mqshutdown namesrv
 
String mqGroup = "myGroup";
String mqIP = "localhost:9876";
String mqTopic = "myTopic";
String mqTag = "myTag";
String key="123";
String msgStr="hello 小李";

String appName="app-1";

//服务/消费 端
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqGroup);
consumer.setConsumerGroup(mqGroup);
consumer.setNamesrvAddr(mqIP);
consumer.setVipChannelEnabled(false);
consumer.setConsumeThreadMax(10);//接收消息最多启10个线程处理，防止线程过多导致数据库连接用光
consumer.setConsumeThreadMin(5);
//设置广播消费  还有 MessageModel.CLUSTERING
//consumer.setMessageModel(MessageModel.BROADCASTING);//要对应用  MessageListenerConcurrently
//consumer.setInstanceName(appName);//如果是BROADCASTING的当一个app服务全部down机，再启动时就会丢失前面的消息，
//如一个app服务有2台都会收到相同的消息(这点不太好，接收方法要做幂等，JMS 持久化topic好像不是这样的)
 
//批量消费,每次拉取10条
//consumer.setConsumeMessageBatchMaxSize(10);

//如果非第一次启动，那么按照上次消费的位置继续消费
consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//consumer.subscribe(mqTopic, mqTag);
 
consumer.subscribe(mqTopic, "myTag || TagB || TagC || TagD || TagE"); //匹配多个用 || 或   * ,如传null同* 
MessageListenerOrderly messageListenerOrderly= new MessageListenerOrderly() //有顺序的
{
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext)
	{
		MessageExt msg=list.get(0);
		if(msg.getTags().contains(mqTag))//区分哪个tag,用于传送多种消息格式
			  System.out.println("这个tag是"+mqTag);
		System.out.println(new Date()+appName+"服务收到消息体："+new String(msg.getBody(),Charset.forName("UTF-8")));
		return ConsumeOrderlyStatus.SUCCESS;
	}
};

MessageListenerConcurrently messageListenerConcurrently= new MessageListenerConcurrently() //BROADCASTING
{
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) 
	{
		//要做幂等
		MessageExt msg=list.get(0);
		if(msg.getTags().contains(mqTag))//区分哪个tag,用于传送多种消息格式
			  System.out.println("这个tag是"+mqTag);
		System.out.println(new Date()+appName+"服务收到消息体："+new String(msg.getBody(),Charset.forName("UTF-8")));
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // CONSUME_SUCCESS
	}
};
consumer.registerMessageListener(messageListenerOrderly); 
//consumer.registerMessageListener(messageListenerConcurrently); //BROADCASTING

long iterval=this.getPullInterval();//0立即收到消息
consumer.start();
System.out.println("服务 启动");

//客户/生产端
DefaultMQProducer producer = new DefaultMQProducer(mqGroup);
producer.setNamesrvAddr(mqIP);
producer.start();
 
producer.setRetryTimesWhenSendAsyncFailed(0);
 for (int i = 0; i < 10; i++) 
 {
	final int index = i; 
	Message msg = new Message(mqTopic, mqTag, key, msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
	msg.setKeys("hello"+i);//要唯一
	msg.setDelayTimeLevel(3);//延迟收到消息,官方示例上说3对应的是10秒,防止消费端过快处理
	//  producer.sendOneway(msg);
	 //SendResult sendResult = producer.send(msg);//同步发
	producer.send(msg, new SendCallback() {//异步发
		@Override
		public void onSuccess(SendResult sendResult) {
			System.out.printf("%-10d OK %s %n", index,
				sendResult.getMsgId());
		}
		@Override
		public void onException(Throwable e) {
			System.out.printf("%-10d Exception %s %n", index, e);
			e.printStackTrace();
		}
	});
} 

producer.shutdown();


//事务发
TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();//自己的类
TransactionMQProducer producer = new TransactionMQProducer(mqGroup);
producer.setNamesrvAddr(mqIP);
producer.setCheckThreadPoolMinSize(2);
producer.setCheckThreadPoolMaxSize(2);
producer.setCheckRequestHoldMax(2000);
producer.setTransactionCheckListener(transactionCheckListener);//broker检查发送的回调吗
producer.start();

String[] tags = new String[] {mqTag, "TagB", "TagC", "TagD", "TagE"};
LocalTransactionExecuter tranExecuter = new TransactionExecuterImpl();//自己的类 
for (int i = 0; i < 100; i++) {
	Message msg =  new Message(mqTopic, tags[i % tags.length], "KEY" + i,
			("你好 RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
	SendResult sendResult = producer.sendMessageInTransaction(msg, tranExecuter, null);
	System.out.printf("%s%n", sendResult);
	Thread.sleep(10); 
}

public class TransactionExecuterImpl implements LocalTransactionExecuter {
    private AtomicInteger transactionIndex = new AtomicInteger(1);
    @Override
    public LocalTransactionState executeLocalTransactionBranch(final Message msg, final Object arg) {
        int value = transactionIndex.getAndIncrement();
        if (value == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
        	System.out.println("发送 commit msg"+msg);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }
}
public class TransactionCheckListenerImpl implements TransactionCheckListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);
    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        System.out.printf("server checking TrMsg %s%n", msg);
        int value = transactionIndex.getAndIncrement();
        if ((value % 6) == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }
}





 多个nameServer 服务中,只要有一个有效整个cluster就可用
 Broker 发送心跳到所有的nameServer
 
 broker master  可读写
 broker slave 只读

conf 目录 
2m-2s-async 两主，两从，同步复制数据的配置
2m-2s-sync 两主，两从，异步复制数据的配置
2m-noslave 两主,无从的配置

Broker分为Master与Slave，一个Master可以对应多个Slave，但是一个Slave只能对应一个Master，
Master与Slave的对应关系通过指定相同的BrokerName，不同的BrokerId来定义，BrokerId=0表示Master，>0的整数表示Slave
每个Broker与Name Server集群中的所有节点建立长连接

Producer 向提供Topic服务的Master建立长连接
Consumer 向提供Topic服务的Master、Slave建立长连接

一个topic下，我们可以设置多个queue


可以事务 
第一阶段发送Prepared消息拿到消息的地址，
第二阶段执行本地事物，
第三阶段通过第一阶段拿到的地址去访问消息，并修改消息的状态

如第三阶失败 向消息发送端(生产者)确认,如还失败 , 会根据发送端设置的策略来决定是回滚还是继续发送确认消息

Push and Pull model,
TAG 可以用同一个topic 发不同的消息
