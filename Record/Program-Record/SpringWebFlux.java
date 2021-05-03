
使用 Reactor 库做为react stream, 为JDK8,如JDK9就已经有Flow类了，也是react stream 

反应式编程
要求运行在Servlet 3.1+容器上,支持异步servlet

0..1 (Mono)    单声道的
0..N (Flux) 	熔化； 熔解 流出 [物]流量
对于复杂的应用来说，反应式编程和负压的优势会体现出来，可以带来整体的性能的提升 

@Configuration
@ComponentScan //index.html仿问不到??
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // ...
    }

}
public class MyInitializer extends AbstractReactiveWebInitializer {
	@Override
	protected Class<?>[] getConfigClasses() {
		return new Class[] {WebConfig.class};
	} 
}

可使用spring boot ,内部使用NettyWebServer,不需要tomcat-embed或Jetty容器
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

spring-webflux-5.2.1.RELEASE.jar 
	 spring-jcl-5.2.1.RELEASE.jar
	 reactor-core-3.3.0.RELEASE.jar
	 reactive-streams-1.0.3.jar
	 

import reactor.core.publisher.Mono;	 
@RestController
public class BasicController {
	 
//http://127.0.0.1:8080/hello_world
	
	/*
	POST  http://127.0.0.1:8080/user/create  
	{
		"id":1,
		"username":"lisi"
	}
	 
	 http://127.0.0.1:8080/user/2     如没数据就返回指定错误码
	  http://127.0.0.1:8080/user/list
	*/
	
    @GetMapping("/hello_world")
    public Mono<String> sayHelloWorld() { //可以返回Mono 
        return Mono.just("Hello World");
    }
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Resource not found======")
    @ExceptionHandler(ResourceNotFoundException.class)
    public void notFound() {
    }
	
	@GetMapping("/{id}")
    public Mono<User>getById(@PathVariable("id") final String id) { //返回类型包装在泛型中
        return this.userService.getById(id);
    }
    @PostMapping("/create") 
    public Mono<User> create(@RequestBody final User user) {
        return this.userService.createOrUpdate(user);
    }
	@GetMapping("/list")
    public Flux<User> list() {
        return this.userService.list();
    }
} 
@Service
class UserService {
    private final Map<String, User> data = new ConcurrentHashMap<>(); 
    Flux<User> list() {
        return Flux.fromIterable(this.data.values());
    } 
    Mono<User> getById(final String id) {
        return Mono.justOrEmpty(this.data.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }
 }
	 
	//---WebClient
  final User user = new User();
  user.setId("100");
  user.setName("Test");
  user.setEmail("test@example.org");
  //spring的reactive下的WebClient
  final WebClient client = WebClient.create("http://localhost:8080/user");
  final Mono<User> createdUser = client.post()
          .uri("/create")
          .accept(MediaType.APPLICATION_JSON)
          .body(Mono.just(user), User.class)
          .exchange()//请求服务端,返回Mono
          .flatMap(response -> response.bodyToMono(User.class));
  System.out.println(createdUser.block());
 
@RestController
@RequestMapping("/sse")
public class SseController {
    @GetMapping("/randomNumbers")
    //服务器推送事件(单向的)，返回ServerSentEvent
    public Flux<ServerSentEvent<Integer>> randomNumbers() {
        return Flux.interval(Duration.ofSeconds(1)) //返回每秒数，1,2,3向下传到seq变量
        			  //ThreadLocalRandom
                .map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))//向下传为Tupel2到data变量
                .map(data -> ServerSentEvent.<Integer>builder()
                        .event("random")
                        .id(Long.toString(data.getT1()))
                        .data(data.getT2())
                        .build());
    }
}
测试要用 curl   http://localhost:8080/sse/randomNumbers 

/*
/*
    	 java.lang.UnsatisfiedLinkError: no netty_transport_native_epoll_x86_64 in java.library.path
    	spring boot版本pom 中已经自带下面的，还是报错，但不影响使用
    	<dependency>
	      <groupId>io.netty</groupId>
	      <artifactId>netty-transport-native-epoll</artifactId>
	      <!--  <version>4.1.43.Final</version>  --> 
	     <classifier>linux-x86_64</classifier>
	    </dependency> 
    	 */
 	 */
 final WebClient client = WebClient.create();
        client.get()
                .uri("http://localhost:8080/sse/randomNumbers")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()//返回Mono,后全是reactor的方法
                //使用 flatMapMany 把 Mono<ClientResponse>转换成一个 Flux<ServerSentEvent>对象
                .flatMapMany(response -> 
                				response.body(
                						BodyExtractors.toFlux( 
                								//表未了响应消息流中的内容是 ServerSentEvent 对象
                								new ParameterizedTypeReference<ServerSentEvent<String>>() {}
                								)
                			 	)
                			)
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)//data函数没有参数,可以不接收？？？
                .buffer(10)//来获取前 10 条消息并输出
                .doOnNext(System.out::println)
                .blockFirst();


-------websocket
import org.springframework.web.reactive.socket.WebSocketHandler;
@Component
//webSocket双向的
public class EchoHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        return session.send(  //参数类型为 Publisher<WebSocketMessage> 
                session.receive()//返回Mono<WebSocketMessage>
                        .map(msg -> session.textMessage("ECHO -> " + msg.getPayloadAsText())));
    }
}

import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class WebSocketConfiguration {
    
	@Autowired
    @Bean
    public HandlerMapping webSocketMapping(final EchoHandler echoHandler) {
        final Map<String, WebSocketHandler> map = new HashMap<>(1);
        map.put("/echo", echoHandler);
 
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }
 
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
  
//webSocket客户端
  final WebSocketClient client = new ReactorNettyWebSocketClient();
  //execute函数与服务端连接
  client.execute(URI.create("ws://localhost:8080/echo"),
        		session ->
        			session.send(Flux.just(session.textMessage("Hello")))
                        .thenMany(
                        		//receive 方法来等待服务器端的响应并输出。take(1)的作用是客户端只获取服务器端发送的第一条消息。
		                        session.receive().take(1)
                        		.map(WebSocketMessage::getPayloadAsText)//getPayloadAsText函数没有参数,可以不接收？？？
                        		)
                        .doOnNext(System.out::println)
                        .then()
               ).block(Duration.ofMillis(5000));



// http://localhost:8080/calculator?operator=add&v1=4&v2=5
// http://localhost:8080/calculator?operator=xxx&v1=4&v2=5
// http://localhost:8080/calculator?operatorxxx=xxx&v1=4&v2=5

//函数式编程
@Configuration
public class Config {
    @Bean
    @Autowired
    //RouterFunction类型的Bean会注册URL地址，
    public RouterFunction<ServerResponse>routerFunction(final CalculatorHandler calculatorHandler) {
        return RouterFunctions.route(
        		RequestPredicates.path("/calculator"), 
        		request ->
                		request.queryParam("operator").map
                			(
                				operator ->
		                			Mono.justOrEmpty(ReflectionUtils.findMethod(CalculatorHandler.class, operator, ServerRequest.class))
		                        	//.flatMap(method ->  (Mono<ServerResponse>) ReflectionUtils.invokeMethod(method, calculatorHandler, request))
			                        .flatMap(new Function<Method,Mono<ServerResponse>>()
			                        {
			                        	 public Mono<ServerResponse> apply(Method method)
			                        	 {   //如果前面的方法存在（justOrEmpty做的），调用这里，否则不调用这里
			                        		 return (Mono<ServerResponse>) ReflectionUtils.invokeMethod(method, calculatorHandler, request);
			                        	 }
			                        }) 
		                        .switchIfEmpty(ServerResponse.noContent().build())//operator参数值错误(如不是add)
		                        .onErrorResume(ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())//???
                           ).orElse(ServerResponse.status(HttpStatus.NOT_ACCEPTABLE) .build() ) //没有传operator参数
	        );
    }
}


@Component
public class CalculatorHandler {
    public Mono<ServerResponse> add(final ServerRequest request) {
        return calculate(request, (v1, v2) -> v1 + v2);
    }
    public Mono<ServerResponse> subtract(final ServerRequest request) {
        return calculate(request, (v1, v2) -> v1 - v2);
    }
    public Mono<ServerResponse>  multiply(final ServerRequest request) {
        return calculate(request, (v1, v2) -> v1 * v2);
    }
    public Mono<ServerResponse> divide(final ServerRequest request) {
        return calculate(request, (v1, v2) -> v1 / v2);
    }
    private Mono<ServerResponse> calculate(final ServerRequest request,
                                           final BiFunction<Integer, Integer, Integer> calculateFunc) {
        final Tuple2<Integer, Integer> operands = extractOperands(request);
        return ServerResponse
                .ok()
                .body(Mono.just(calculateFunc.apply(operands.getT1(), operands.getT2())), Integer.class);
    }
    private Tuple2<Integer, Integer> extractOperands(final ServerRequest request) {
        return Tuples.of(parseOperand(request, "v1"), parseOperand(request, "v2"));
    }
    private int parseOperand(final ServerRequest request, final String param) {
        try {
            return Integer.parseInt(request.queryParam(param).orElse("0"));
        } catch (final NumberFormatException e) {
            return 0;
        }
    }
}


//单元测试,spring-test中带webflux
 private final WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
 @Test
 public void testCreateUser() throws Exception {
     final User user = new User();
     user.setId("200");
     user.setName("Test");
     user.setEmail("test@example.org");
     client.post().uri("/user/create")
             .contentType(MediaType.APPLICATION_JSON)
             .body(Mono.just(user), User.class)
             .exchange()
             .expectStatus().isOk()
             .expectBody().jsonPath("name").isEqualTo("Test");
 }
 
 
 
