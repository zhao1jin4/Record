
ʹ�� Reactor ����Ϊreact stream, ΪJDK8,��JDK9���Ѿ���Flow���ˣ�Ҳ��react stream 

��Ӧʽ���
Ҫ��������Servlet 3.1+������,֧���첽servlet

0..1 (Mono)    ��������
0..N (Flux) 	�ۻ��� �۽� ���� [��]����
���ڸ��ӵ�Ӧ����˵����Ӧʽ��̺͸�ѹ�����ƻ����ֳ��������Դ�����������ܵ����� 

@Configuration
@ComponentScan //index.html���ʲ���??
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

��ʹ��spring boot ,�ڲ�ʹ��NettyWebServer,����Ҫtomcat-embed��Jetty����
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
	 
	 http://127.0.0.1:8080/user/2     ��û���ݾͷ���ָ��������
	  http://127.0.0.1:8080/user/list
	*/
	
    @GetMapping("/hello_world")
    public Mono<String> sayHelloWorld() { //���Է���Mono 
        return Mono.just("Hello World");
    }
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Resource not found======")
    @ExceptionHandler(ResourceNotFoundException.class)
    public void notFound() {
    }
	
	@GetMapping("/{id}")
    public Mono<User>getById(@PathVariable("id") final String id) { //�������Ͱ�װ�ڷ�����
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
  //spring��reactive�µ�WebClient
  final WebClient client = WebClient.create("http://localhost:8080/user");
  final Mono<User> createdUser = client.post()
          .uri("/create")
          .accept(MediaType.APPLICATION_JSON)
          .body(Mono.just(user), User.class)
          .exchange()//��������,����Mono
          .flatMap(response -> response.bodyToMono(User.class));
  System.out.println(createdUser.block());
 
@RestController
@RequestMapping("/sse")
public class SseController {
    @GetMapping("/randomNumbers")
    //�����������¼�(�����)������ServerSentEvent
    public Flux<ServerSentEvent<Integer>> randomNumbers() {
        return Flux.interval(Duration.ofSeconds(1)) //����ÿ������1,2,3���´���seq����
        			  //ThreadLocalRandom
                .map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))//���´�ΪTupel2��data����
                .map(data -> ServerSentEvent.<Integer>builder()
                        .event("random")
                        .id(Long.toString(data.getT1()))
                        .data(data.getT2())
                        .build());
    }
}
����Ҫ�� curl   http://localhost:8080/sse/randomNumbers 

/*
/*
    	 java.lang.UnsatisfiedLinkError: no netty_transport_native_epoll_x86_64 in java.library.path
    	spring boot�汾pom ���Ѿ��Դ�����ģ����Ǳ�������Ӱ��ʹ��
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
                .exchange()//����Mono,��ȫ��reactor�ķ���
                //ʹ�� flatMapMany �� Mono<ClientResponse>ת����һ�� Flux<ServerSentEvent>����
                .flatMapMany(response -> 
                				response.body(
                						BodyExtractors.toFlux( 
                								//��δ����Ӧ��Ϣ���е������� ServerSentEvent ����
                								new ParameterizedTypeReference<ServerSentEvent<String>>() {}
                								)
                			 	)
                			)
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)//data����û�в���,���Բ����գ�����
                .buffer(10)//����ȡǰ 10 ����Ϣ�����
                .doOnNext(System.out::println)
                .blockFirst();


-------websocket
import org.springframework.web.reactive.socket.WebSocketHandler;
@Component
//webSocket˫���
public class EchoHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        return session.send(  //��������Ϊ Publisher<WebSocketMessage> 
                session.receive()//����Mono<WebSocketMessage>
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
  
//webSocket�ͻ���
  final WebSocketClient client = new ReactorNettyWebSocketClient();
  //execute��������������
  client.execute(URI.create("ws://localhost:8080/echo"),
        		session ->
        			session.send(Flux.just(session.textMessage("Hello")))
                        .thenMany(
                        		//receive �������ȴ��������˵���Ӧ�������take(1)�������ǿͻ���ֻ��ȡ�������˷��͵ĵ�һ����Ϣ��
		                        session.receive().take(1)
                        		.map(WebSocketMessage::getPayloadAsText)//getPayloadAsText����û�в���,���Բ����գ�����
                        		)
                        .doOnNext(System.out::println)
                        .then()
               ).block(Duration.ofMillis(5000));



// http://localhost:8080/calculator?operator=add&v1=4&v2=5
// http://localhost:8080/calculator?operator=xxx&v1=4&v2=5
// http://localhost:8080/calculator?operatorxxx=xxx&v1=4&v2=5

//����ʽ���
@Configuration
public class Config {
    @Bean
    @Autowired
    //RouterFunction���͵�Bean��ע��URL��ַ��
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
			                        	 {   //���ǰ��ķ������ڣ�justOrEmpty���ģ�������������򲻵�������
			                        		 return (Mono<ServerResponse>) ReflectionUtils.invokeMethod(method, calculatorHandler, request);
			                        	 }
			                        }) 
		                        .switchIfEmpty(ServerResponse.noContent().build())//operator����ֵ����(�粻��add)
		                        .onErrorResume(ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())//???
                           ).orElse(ServerResponse.status(HttpStatus.NOT_ACCEPTABLE) .build() ) //û�д�operator����
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


//��Ԫ����,spring-test�д�webflux
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
 
 
 
