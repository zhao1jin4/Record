
------------ Thrift(跨语言,代码生成)   
Apache(Facebook)的
dubbo 协议适合   小数据量大并发,netty3.2.2 + hessian -3.2.1
    
./configure && make (要  bison version >= 2.5  ,xz -d -k bision-3.0.tar .xz )



<dependency>
  <groupId>org.apache.thrift</groupId>
  <artifactId>libthrift</artifactId>
  <version>0.11.0</version>
</dependency>



 //---Hello.thrift
 namespace java apache_thrift.hello 
 service Hello{ 
  string helloString(1:string para) 
  i32 helloInt(1:i32 para) 
  bool helloBoolean(1:bool para) 
  void helloVoid() 
  string helloNull() 
 }
 
thrift --gen <language> <Thrift filename>
 -r  (recursivly)
	多语言支持
	C++
	Java
	Cocoa
	Python
	C#
	
thrift  --gen java Hello.thrift  
  
生成Hello类,有以下内部类
	 <方法名>_args
	 <方法名>_result
	 Hello.Client    生成有 send_<方法名> 方法,recv_<方法名> 方法
	 Hello.AsyncClient
	 Hello.Processor
 
 自己写类 HelloServiceImpl implements Hello.Iface
 

 
 
//---clients 
//TTransport transport = new TSocket("localhost", 7911); 	//阻塞式
TTransport transport = new TFramedTransport(new TSocket("localhost", 7911));//非阻塞式
transport.open();  

//TProtocol protocol = new TBinaryProtocol(transport); 
//TCompactProtocol protocol = new TCompactProtocol(transport);// 高效率的、密集的二进制编码格式进行数据传输 
//TJSONProtocol protocol = new TJSONProtocol(transport);
            
Hello.Client client = new Hello.Client(protocol); 
client.helloVoid(); //调用接口方法
client.helloNull();//返回null 报 TApplicationException，服务端也强迫关闭连接

transport.close(); 
//---客户端异步管理
TAsyncClientManager clientManager = new TAsyncClientManager(); 
TNonblockingTransport transport = new TNonblockingSocket("localhost", 10005); 
TProtocolFactory protocol = new TBinaryProtocol.Factory(); 
Hello.AsyncClient asyncClient = new Hello.AsyncClient(protocol,clientManager, transport); 
MethodCallback callBack = new MethodCallback(); 
asyncClient.helloString("Hello World", callBack); 
Object res = callBack.getResult(); 
while (res == null) { 
	res = callBack.getResult(); 
	System.out.println("wait...."); 
} 
System.out.println( ((Hello.AsyncClient.helloString_call) res)
					.getResult()); 
				
 public class MethodCallback implements AsyncMethodCallback 				
//----server

//Factory proFactory = new TBinaryProtocol.Factory(); 
TCompactProtocol.Factory proFactory = new TCompactProtocol.Factory();// 高效率的、密集的二进制编码格式进行数据传输 
//TJSONProtocol.Factory proFactory = new TJSONProtocol.Factory();
		  
TProcessor processor = new Hello.Processor(new HelloServiceImpl());            
 

//阻塞式
//TServerSocket serverTransport = new TServerSocket(7911); 
//TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor)
//					.protocolFactory(proFactory));
//TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor)
//					.protocolFactory(proFactory));//单线程

//非阻塞式
TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(7911); 
TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).processor(processor)
		.protocolFactory(proFactory)); 
		
System.out.println("Start server on port 7911..."); 
server.serve(); 
			
			


选择不同的传输协议和传输层而不用重新生成代码 
它提供阻塞、非阻塞、单线程和多线程的模式运行在服务器上



基本类型：

	bool：布尔值，true 或 false，对应 Java 的 boolean
	byte：8 位有符号整数，对应 Java 的 byte
	i16：16 位有符号整数，对应 Java 的 short
	i32：32 位有符号整数，对应 Java 的 int
	i64：64 位有符号整数，对应 Java 的 long
	double：64 位浮点数，对应 Java 的 double
	string：未知编码文本或二进制字符串，对应 Java 的 String

结构体类型：
	struct：定义公共的对象，类似于 C 语言中的结构体定义，在 Java 中是一个 JavaBean
容器类型：
	list：对应 Java 的 ArrayList
	set：对应 Java 的 HashSet
	map：对应 Java 的 HashMap
异常类型：
	exception：对应 Java 的 Exception
服务类型：
	service：对应服务的类

------------ Hessian(二进制RPC协议) 可二进制做 webservice (dubbo 有用)
//--servlet server

import com.caucho.hessian.server.HessianServlet;

@WebServlet("/hessian/server")
public class BasicService extends HessianServlet implements BasicAPI 
{
//BasicAPI是自己定义的接口,不要重写doGet,doPost,只要实现自定义接口就OK
}

//--servlet client
import com.caucho.hessian.client.HessianProxyFactory;

String url = "http://127.0.0.1:8080/J_JavaEE/hessian/server";
HessianProxyFactory factory = new HessianProxyFactory();
BasicAPI basic = (BasicAPI) factory.create(BasicAPI.class, url);
System.out.println("====Hessian=====hello(): " + basic.hello());

MyRequest reqObj=new MyRequest(); //如传对象必须  implements  Serializable
reqObj.setSystemId("123");
reqObj.setSystemName("boss");
BigDecimal amt=BigDecimal.valueOf(22.35d);//要用BigDecimal.valueOf()不要用new BigDecimal()
reqObj.setAmt(amt);
basic.setObject(reqObj);
System.out.println("====Hessian=====server changed SystemId: " + basic.getObject().getSystemId());

spring-web集成
	org.springframework.remoting.caucho.HessianServiceExporter 
	
	
	
Hessian 自动配置BigDecimal, 
hessian.jar/META-INF/hessian/serializers 
java.math.BigDecimal=com.caucho.hessian.io.BigDecimalDeserializer

hessian.jar/META-INF/hessian/deserializers
java.math.BigDecimal=com.caucho.hessian.io.StringValueSerializer

===============gRPC
 C 基于的RPC ，支持各种语言做客户端,支持移动端
gRPC 框架为HTTP/2而设计,使用 protobuf 进行序列化
比Thrift好的是 HTTP协议可以根据URL或者HTTP头做(复用nginx或spring cloud) 路由


 <dependency>
	<groupId>com.google.protobuf</groupId>
	<artifactId>protobuf-java</artifactId>
	<version>3.11.4</version>
</dependency>

<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>io.grpc</groupId>
			<artifactId>grpc-bom</artifactId>
			<version>1.29.0</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-protobuf</artifactId>
</dependency>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-stub</artifactId>
</dependency>

运行时才要的
 <dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-netty-shaded</artifactId>
  <scope>runtime</scope>
</dependency>
 
grpc-api-1.29.0.jar
grpc-stub-1.29.0.jar
grpc-protobuf-1.29.0.jar
grpc-context-1.29.0.jar
grpc-netty-shaded-1.29.0.jar  	运行时才要的
grpc-core-1.29.0.jar    		运行时才要的
grpc-protobuf-lite-1.29.0.jar  	运行时才要的
perfmark-api-0.19.0.jar   		运行时才要的

	
https://repo1.maven.org/maven2/com/google/protobuf/protoc/3.11.4/ 下有 protoc-3.11.4-windows-x86_64.exe    
---hello.proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "grpc.gen.helloworld";
option java_outer_classname = "HelloWorldProto";
option objc_class_prefix = "HLW";

package helloworld;

// The greeting service definition.
service Greeter {
    // Sends a greeting
    rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
    string name = 1;
}

// The response message containing the greetings
message HelloReply {
    string message = 1;
}
---
protoc --java_out=. hello.proto   -I=. 	    没有生成 service 代码 
	-IPATH, --proto_path=PATH  proto的输入目录

https://github.com/grpc/grpc-java/tree/master/compiler
搜索 protoc-gen-grpc-java
https://mvnrepository.com/artifact/io.grpc/protoc-gen-grpc-java/1.29.0  点 Files 边的 View All 下载 protoc-gen-grpc-java-1.29.0-windows-x86_64.exe
linux和mac的文件名也是.exe???
	
protoc  --plugin=protoc-gen-grpc-java=D:\Program\java_win64_gRPC\protoc-gen-grpc-java-1.29.0-windows-x86_64.exe  --grpc-java_out=. --proto_path =.  hello.proto 
才会生成servcie代码 

//官方示例代码 
public class HelloWorldServer 
{
  private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());
  private Server server;
  
  private void start() throws IOException 
  {
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new GreeterImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() 
    {
      @Override
      public void run() 
      {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
          HelloWorldServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException 
  {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }
 
  private void blockUntilShutdown() throws InterruptedException 
  {
    if (server != null) {
      server.awaitTermination();
    }
  } 
  public static void main(String[] args) throws IOException, InterruptedException 
  {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  static class GreeterImpl extends GreeterGrpc.GreeterImplBase 
  {
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) 
    {
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }
}


public class HelloWorldClient
{
  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());
  private final GreeterGrpc.GreeterBlockingStub blockingStub;
  public HelloWorldClient(Channel channel)
  { 
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }
 
  public void greet(String name) 
  {
    logger.info("Will try to greet " + name + " ...");
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("Greeting: " + response.getMessage());
  }
 
  public static void main(String[] args) throws Exception 
  {
    String user = "world"; 
    String target = "localhost:50051";
     
     //Channel是线程安全的，可重用的，默认使用TLS,
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target) 
        .usePlaintext()//不使用TLS
        .build();
    try {
      HelloWorldClient client = new HelloWorldClient(channel);
      client.greet(user);
    } finally { 
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}





