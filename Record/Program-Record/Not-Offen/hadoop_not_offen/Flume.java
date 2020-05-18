

===========Flume	
被Flink替代

( 水槽) 分布式海量日志采集、聚合和传输 
一个agent有三个部分
	Source 接入数据
	Channel 暂存数据 
	Sink   输出数据


bin/flume-ng agent -n a1 -c conf -f conf/flume-conf.properties.template     #a1 是配置文件中的agent的名字
 
-----conf/flume-conf.properties  示例配置 
 # Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat    
#type可以为avro

a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1


 bin/flume-ng agent -n a1 -c conf -f conf/flume-conf.properties   -Dflume.root.logger=INFO,console   #a1 是配置文件中的agent的名字
 
本地浏览器仿问   http://127.0.0.1:44444 ,可以看到控制台显示请求的信息

conf/flume-env.sh 文件 FLUME_CLASSPATH 变量指向的目录中放jar包插件 
$FLUME_HOME/plugins.d  目录中的插件自动识别,插件目录的子级目录结构要有,lib目录,libext目录(放依赖的jar),native目录
 
plugins.d/custom-source-1/
plugins.d/custom-source-1/lib/my-source.jar
plugins.d/custom-source-1/libext/spring-core-2.5.6.jar
plugins.d/custom-source-2/
plugins.d/custom-source-2/lib/custom.jar
plugins.d/custom-source-2/native/gettext.so


$ bin/flume-ng avro-client -H localhost -p 44444 -F README   #把文件的内容使用avro 发送到Source类型为avro的channel上


可以有多个agent 串行连接,但Source和Sink 的type要为avro
如果是多个agent(Web Server) 连接到一个agent,要使用thrift 

一个Source可以指定多个Channel
一个Sink只能指定一个Channel
