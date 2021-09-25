

==============================MongoDB 
 <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>3.12.2</version>
</dependency>
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongodb-driver-core</artifactId>
	<version>3.12.2</version>
</dependency>
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongodb-driver-async</artifactId>
	<version>3.12.2</version>
</dependency>
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>bson</artifactId>
    <version>3.12.2</version>
</dependency>

3.11.0 支持MongoDB 4.2 分布式事务
 
mongodb-driver-3.8.2.jar
mongodb-driver-async-3.8.2.jar
	mongodb-driver-core-3.8.2.jar
bson-3.8.2.jar

http://mongodb.github.io/mongo-java-driver/3.4/
	
package nosql.mongodb;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.ClientSession;

public class MongoTest
{
	public static void insert(MongoDatabase db)
	{
		MongoCollection<Document> coll = db.getCollection("bios");
		Document doc = new Document("name", "MongoDB").
                append("type", "database").
                append("count", 1).
                append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insertOne(doc);
		coll.createIndex(new Document("name", 1));
		
		db.createCollection("cappedCollection",
				  new CreateCollectionOptions().capped(true).sizeInBytes(0x100000));
	}
	public static void delete(MongoDatabase db) throws Exception
	{
		MongoCollection<Document> collection=db.getCollection("bios");
		DeleteResult rs=collection.deleteMany(new Document("name", "MongoDB"));//new ObjectId("");
		System.out.println("deleted rows:"+rs.getDeletedCount());
	}	
	public static void update(MongoDatabase db ) throws Exception
	{
		MongoCollection<Document> collection =db.getCollection("bios");
		UpdateResult rs=collection.updateOne(new Document("name", "MongoDB"), new Document("$set", new Document("count", 2)));
		System.out.println("update effect rows:"+rs.getModifiedCount());
	}
	public static void query(MongoDatabase db)
	{

		System.out.println("--test db all collection");
		MongoIterable<String> colls = db.listCollectionNames();//= show collections
		for (String s : colls) {
		    System.out.println(s);
		}
		
		System.out.println("-- bios collection   all Index");
		MongoCollection<Document> coll = db.getCollection("bios");
		ListIndexesIterable<Document> list=coll.listIndexes();
		for (Document o : list) {
		   System.out.println(o);
		}
		System.out.println("-- bios collection count:"+coll.countDocuments()); // coll.estimatedDocumentCount()
		
//		FindIterable<Document> cursor = coll.find();
//		Bson query = new BasicDBObject("name", "MongoDB");
		Bson query = new BasicDBObject("info.x",new BasicDBObject("$lte", 300) ).
										append("name", "MongoDB" );
		FindIterable<Document> iter = coll.find(query);
		MongoCursor<Document> cursor=iter.iterator();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} finally {
		   cursor.close();
		}
	}
	public static void transaction(  )
	{
 
//		Multi-document transactions are available for replica sets only. 
//		Transactions for sharded clusters are scheduled for MongoDB 4.2
	
		MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());
	
		//replication set 事务OK
		ServerAddress[] addrs=	new ServerAddress[] {
				new ServerAddress("127.0.0.1", 37017),
				new ServerAddress("127.0.0.1", 37018),
				new ServerAddress("127.0.0.1", 37019)};
		
		//单机不支持事务
//		ServerAddress[] addrs=	new ServerAddress[] { new ServerAddress("127.0.0.1", 27017) };
		MongoClientOptions opts= new MongoClientOptions.Builder().build();
	 	MongoClient mongoClient  = new MongoClient(Arrays.asList(addrs), credential,opts );  
	
//		db.employees.insert({employee:3,status:'none'})
//		db.events.insert({})
		MongoCollection<Document> employeesCollection = mongoClient.getDatabase("reporting").getCollection("employees");
		employeesCollection.drop();
		employeesCollection.insertOne(new Document("employee",3).append("status", "none"));
	    MongoCollection<Document> eventsCollection = mongoClient.getDatabase("reporting").getCollection("events");
	    eventsCollection.drop();
	    eventsCollection.insertOne(new Document());
	    ClientSession clientSession = mongoClient.startSession();
	    try   {
	        clientSession.startTransaction();

	        employeesCollection.updateOne(clientSession,
	                Filters.eq("employee", 3),
	                Updates.set("status", "Inactive"));
	        eventsCollection.insertOne(clientSession,
	                new Document("employee", 3).append("status", new Document("new", "Inactive").append("old", "Active")));

	        clientSession.commitTransaction();
	    
	    }catch(Exception e)
		{
	    	e.printStackTrace();
	    	clientSession.abortTransaction();
		}finally {
			clientSession.close();
		}
	}
	public static void main(String[] args) throws Exception
	{
		//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//		                                      new ServerAddress("localhost", 27018),
//		                                      new ServerAddress("localhost", 27019)));
		  
        MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());  
         MongoClientOptions opts= new MongoClientOptions.Builder().build();
        MongoClient mongoClient  = new MongoClient(Arrays.asList(new ServerAddress("10.1.5.226", 27017)), , credential,opts);  
		
		MongoDatabase db = mongoClient.getDatabase( "test" );//= use test
		 System.out.println("--all db");
		for (String s : mongoClient.listDatabaseNames()) {//= show dbs  ,如服务端打开 --auth 这里验证权限 
			   System.out.println(s);
		}
		 db.drop();
	   
		insert(db);
	   update(db);
	   query(db);
	   delete(db);
		
	  
		 
		 mongoClient.close();
	}
}
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.MongoClientSettings;

//MongoClient client = MongoClients.create();//default port 27017, default connection string "mongodb://localhost" 
//MongoClient client = MongoClients.create("mongodb://localhost");
//MongoClient client = MongoClients.create(new ConnectionString("mongodb://localhost"));
 
Block<Builder> block=new Block<Builder>(){
	@Override
	public void apply(Builder builder) {
		builder.hosts(Collections.singletonList((new ServerAddress("localhost",27017))));
	}
};
MongoClientSettings settings = MongoClientSettings.builder().applyToClusterSettings(block).build();
MongoClient client = MongoClients.create(settings);

//支持JDK8
collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));

Document document = new Document("x", 1);
collection.insertOne(document, new SingleResultCallback<Void>() {
   @Override
   public void onResult(final Void result, final Throwable t) {
	   System.out.println("Inserted!");
   }
});

document.append("x", 2).append("y", 3);
//模板是  UpdateResult
collection.replaceOne(Filters.eq("_id", document.get("_id")), document, 
	new SingleResultCallback<UpdateResult>() {
	   @Override
	   public void onResult(final UpdateResult result, final Throwable t) {
		   System.out.println(result.getModifiedCount());
	   }
   });	
//模板是  List<Document>
collection.find().into(new ArrayList<Document>(), 
	new SingleResultCallback<List<Document>>() {
		@Override
		public void onResult(final List<Document> result, final Throwable t) {
			System.out.println("Found Documents: #" + result.size());
		}
	});

Block<Document> printDocumentBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {//查询到的每个Document调用一次
			System.out.println(document.toJson());
		}
	};
SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
	@Override
	public void onResult(final Void result, final Throwable t) {//只在完成时调用一次
		System.out.println("Operation Finished!");
	}
};
collection.find().forEach(printDocumentBlock, callbackWhenFinished);

collection.find(Filters.gt("i", 50)).forEach(printDocumentBlock, callbackWhenFinished);
collection.find(Filters.exists("i")).sort(Sorts.descending("i")).first(printDocument);
collection.find().projection(excludeId()).first(printDocument);//Projections.excludeId

collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)),
		new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				System.out.println(result.getModifiedCount());
			}
		});
collection.updateMany(lt("i", 100), new Document("$inc", new Document("i", 100)),
		new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				System.out.println(result.getModifiedCount());
			}
		});

//模板是DeleteResult
collection.deleteOne(eq("i", 110), new SingleResultCallback<DeleteResult>() {
	@Override
	public void onResult(final DeleteResult result, final Throwable t) {
		System.out.println(result.getDeletedCount());
	}
});

collection.deleteMany(gte("i", 100), new SingleResultCallback<DeleteResult>() {
	@Override
	public void onResult(final DeleteResult result, final Throwable t) {
		System.out.println(result.getDeletedCount());
	}
});


SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() 
		{
		    @Override
		    public void onResult(final BulkWriteResult result, final Throwable t) {
		    	if(result !=null  && result.getMatchedCount()>0 )
		    	{
		    		  System.out.println("共"+result.getMatchedCount()+"条记录match,delete数:" +result.getDeletedCount() + 
		    				  	",insert数:"+result.getInsertedCount() +
		    				  	",modified数:"+result.getModifiedCount());
		    	}else 
		    	{
		    		  System.out.println( "BulkWriteResult is null or result.getMatchedCount()=0");
		    	}
		    }
		};

collection.bulkWrite(
	  Arrays.asList(new InsertOneModel<>(new Document("_id", 4)),
					new InsertOneModel<>(new Document("_id", 5)),
					new InsertOneModel<>(new Document("_id", 6)),
					new UpdateOneModel<>(new Document("_id", 1),
										 new Document("$set", new Document("x", 2))),
					new DeleteOneModel<>(new Document("_id", 2)),
					new ReplaceOneModel<>(new Document("_id", 3),
										  new Document("_id", 3).append("x", 4))),
	  new BulkWriteOptions().ordered(false),//批量不按顺序做,不加这个参数默认是按顺序做的
	  printBatchResult
);
---------Querydsl MongoDB
http://www.querydsl.com/static/querydsl/latest/reference/html/
有Querying SQL(有maven插件生成代码),Querying Lucene, JPA,Querying Hibernate Search,Querying in Scala

<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-mongodb</artifactId>
    <version>4.3.1</version>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
     <artifactId>querydsl-core</artifactId>
    <version>4.3.1</version>
</dependency> 
<dependency>
  <groupId>com.mysema.commons</groupId>
  <artifactId>mysema-commons-lang</artifactId>
  <version>0.2.4</version>
</dependency>
<dependency>
    <groupId>org.mongodb.morphia</groupId>
    <artifactId>morphia</artifactId>
    <version>1.3.2</version>
</dependency>



生成代码才用的
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-apt</artifactId>
  <version>4.2.1</version> 
</dependency>
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.2</version>
</dependency>
<plugin>
    <groupId>com.mysema.maven</groupId>
    <artifactId>apt-maven-plugin</artifactId>
    <version>1.1.3</version>
    <executions>
      <execution>
        <goals>
          <goal>process</goal>
        </goals>
        <configuration>
          <outputDirectory>target/generated-sources/java</outputDirectory>
          <processor>com.querydsl.apt.morphia.MorphiaAnnotationProcessor</processor>
        </configuration>
      </execution>
    </executions>
  </plugin>
 
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference; 

@Entity ("mo_customer") 
public class Customer {
    @Id
    private String id;
 
    @Property("first_name")
    private String firstName; 
}

mvn clean install 在 target/generated-sources/java 目录生成 QCustomer.java
 
import javax.annotation.Generated;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.EntityPathBase;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {
  public final StringPath firstName = createString("firstName");
  public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);
  //....
}



@Entity("employees")
@Indexes(
		//@Index(value = "salary", fields = @Field("salary"))
		@Index( fields = {@Field("salary")})
)
class Employee {
    @Id
    private ObjectId id;
    private String name;
    private Integer age;
    @Reference
    private Employee manager;
    @Reference
    private List<Employee> directReports = new ArrayList<Employee>();
    @Property("wage")
    private Double salary;
}


 
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia; 

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress; 
import com.querydsl.mongodb.morphia.MorphiaQuery;

MongoCredential credential = MongoCredential.createCredential("zh", "reporting", "123".toCharArray());
ServerAddress[] addrs=	new ServerAddress[] { new ServerAddress("127.0.0.1", 27017) };
MongoClientOptions opts= new MongoClientOptions.Builder().build();
MongoClient mongoClient  = new MongoClient(Arrays.asList(addrs),  credential ,opts );  
	 	
	 	
Morphia morphia= new Morphia();
Datastore datastore = morphia.createDatastore(mongoClient,"reporting");
	  
  //--Morphia employee 

 datastore.ensureIndexes();

 final Employee elmer = new Employee("Elmer Fudd", 50000.0);
 datastore.save(elmer);

 final Employee daffy = new Employee("Daffy Duck", 40000.0);
 datastore.save(daffy);

 final Employee pepe = new Employee("Pepé Le Pew", 25000.0);
 datastore.save(pepe);

 elmer.getDirectReports().add(daffy);//保存的是directReports数据，0是{$ref:"",$id:""}
 elmer.getDirectReports().add(pepe);

 datastore.save(elmer);

 Query<Employee> query = datastore.find(Employee.class);
 final long employees = query.count();
  long underpaid = datastore.find(Employee.class)
                          .filter("salary <=", 30000)
                          .count();

  underpaid = datastore.find(Employee.class)
                     .field("salary").lessThanOrEq(30000)
                     .count();
  
  
 final Query<Employee> underPaidQuery = datastore.find(Employee.class)
                                                 .filter("salary <=", 30000);
 final UpdateOperations<Employee> updateOperations = datastore.createUpdateOperations(Employee.class)
                                                              .inc("salary", 10000);

final UpdateResults results = datastore.update(underPaidQuery, updateOperations);
final Query<Employee> overPaidQuery = datastore.find(Employee.class)
                                                     .filter("salary >", 100000);
datastore.delete(overPaidQuery);
  
 //----------- 
//        morphia.map(Customer.class);
//        morphia.mapPackage("com.hoo.entity");
		  
 Customer cust=	new Customer("li","si_dsl_morphie"); 
		datastore.save(cust);//会存一个className的字段
  
		DBObject dbObj=morphia.toDBObject(cust) ;
		System.out.println(dbObj);
		System.out.println("fromDBObject: " + morphia.fromDBObject(datastore,Customer.class, BasicDBObjectBuilder.start("lastName", "abc").get()));
		System.out.println("getMapper: " + morphia.getMapper());
		System.out.println("isMapped: " + morphia.isMapped(Customer.class));
		//QCustomer customer = new QCustomer("customer");
		QCustomer customer =   QCustomer.customer;
  //依赖于 mysema-commons-lang-0.2.4.jar
		MorphiaQuery<Customer> query = new MorphiaQuery<Customer>(morphia, datastore, customer);
		List<Customer> list = query
		    .where(customer.firstName.eq("li"))
      .limit(5).offset(1)//跳过一个 
		    .fetch();
		System.out.println(list);





//------querydsl-collections-4.2.1.jar
import static com.querydsl.collections.CollQueryFactory.*; //delete,update,from
import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;


==============================Neo4j   JavaClient

<dependency>
    <groupId>org.neo4j.driver</groupId>
    <artifactId>neo4j-java-driver</artifactId>
    <version>1.6.3</version>
</dependency>

支持JDK8,不支持9

 NoSQL  图 数据库  Cypher Query Language


import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import static org.neo4j.driver.v1.Values.parameters;

public class HelloWorldExample implements AutoCloseable
{
    private final Driver driver;

    public HelloWorldExample( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                                     "SET a.message = $message " +
                                                     "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }//try
    }
    public static void main( String... args ) throws Exception
    {
        try ( HelloWorldExample greeter = new HelloWorldExample( "bolt://localhost:7687", "neo4j", "myneo4j" ) )
        {
            greeter.printGreeting( "hello, world" );
        }
    }
}

try (Transaction tx = session.beginTransaction())
{
	tx.run("MERGE (a:Person {name: {x}})", parameters("x", name));
	tx.success();  // Mark this write as successful.
}

 StatementResult result = session.run(
		"MATCH (a:Person) WHERE a.name STARTS WITH {x} RETURN a.name AS name",
		parameters("x", initial));
// Each Cypher execution returns a stream of records.
while (result.hasNext())
{
	org.neo4j.driver.v1.Record record = result.next(); 
	System.out.println(record.get("name").asString());
}


============================== neo4j-ogm 
<dependency>
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-core</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency> 
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-http-driver</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency> 
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-bolt-driver</artifactId>
    <version>3.1.4</version>
</dependency>
<dependency>
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-ogm-embedded-driver</artifactId>
    <version>3.1.4</version> 
</dependency> <!-- 运行依赖于 org.neo4j.graphdb.GraphDatabaseService -->

neo4j-ogm-core-3.1.4.jar
	neo4j-ogm-api-3.1.4.jar
	fast-classpath-scanner-2.18.1.jar
neo4j-ogm-bolt-driver-3.1.4.jar
neo4j-ogm-http-driver-3.1.4.jar


------ neo4j-ogm.properties
URI=http://neo4j:myneo4j@localhost:7474

#
#URI=http://localhost:7474
#username="neo4j"
#password="myneo4j"


String classpathFile="nosql_neo4j_ogm/neo4j-ogm.properties";//JDK 8 不能以/开头
Properties properties = new Properties();
InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathFile)   ;
properties.load(is);
is.close();
 
Configuration configuration = new Configuration.Builder()
		.uri("bolt://localhost")
		.credentials("neo4j", "myneo4j")
 //--
		 // .uri("http://neo4j:myneo4j@localhost:7474")
 //--
		 //.uri("file:///D:/software/neo4j/neo4j-community-3.4.8-windows/neo4j-community-3.4.8/data/databases/graph.db")
		 //ClassNotFoundException: org.neo4j.graphdb.GraphDatabaseService ???
		  
 .build();
//--------
//ConfigurationSource props = new ClasspathConfigurationSource(classpathFile);
//ConfigurationSource props = new FileConfigurationSource("D:\\NEW_\\eclipse_java_workspace/J_JavaThirdLib/src/nosql_neo4j_ogm/neo4j-ogm.properties");
//Configuration configuration = new Configuration.Builder(props).build();
SessionFactory sessionFactory = new SessionFactory(configuration, "nosql_neo4j_ogm.domain");//packages


/*
org.neo4j.driver.v1.Driver nativeDriver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "myneo4j") );
Driver ogmDriver = new BoltDriver(nativeDriver);
SessionFactory sessionFactory = new SessionFactory(ogmDriver, "nosql_neo4j_ogm.domain");//packages

 */		

 Session session = sessionFactory.openSession();

 
 
Movie movie = new Movie("The Matrix", 1999);
Actor keanu = new Actor("Keanu Reeves");
keanu.actsIn(movie);

session.save(movie); 
Movie matrix = session.load(Movie.class, movie.getId()); 
for(Actor actor : matrix.getActors()) {
	System.out.println("Actor: " + actor.getName());
}

package nosql_neo4j_ogm.domain;
@NodeEntity(label="lbl_Movie") 
public class Movie {
 
	@Id @GeneratedValue
	private Long id;

	@Property(name="one_title")
	private String title;
	
	 @Transient //不存储
	private int released;

	@Relationship(type = "ACTS_IN",  
			 direction=Relationship.INCOMING
			 )
	Set<Actor> actors=new HashSet<>();

	 
	public Movie() { //类必须有空的公有构造器
	}

	public Movie(String title, int year) {
		this.title = title;
		this.released = year;
	}
	//getter/setter
}
 
@NodeEntity
public class Actor {
 
	@Id @GeneratedValue
	private Long id;

   @Property(name="fullName")
   private String name ;

	@Relationship(type = "ACTED_IN", direction=Relationship.OUTGOING)
	private Set<Movie> movies = new HashSet<>();

	public Actor() {
	}

	public Actor(String name) {
		this.name = name;
	}

	public void actsIn(Movie movie) {
		movies.add(movie);
		movie.getActors().add(this);
	}
   //getter/setter
}
---------------------------------Redis client Jedis (spring使用这个)
是阻塞的

https://github.com/xetorthio/jedis

<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
     <version>2.9.0</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>

Redis默认监听 6379 端口

//---单节点,测试OK
Jedis jedis=new Jedis(ip,6379);// 单机 或 cluster的master
jedis.auth("123456");//对配置 masterauth ,requirepass 
String keys = "name";  
if(jedis.exists(keys))
	jedis.del(keys);  
jedis.set(keys, "snowolf");  
System.out.println(jedis.get(keys));  

//---单节点,测试OK
JedisPool pool = new JedisPool(new JedisPoolConfig(), ip,port,2000);//timeout,可加passworld参数
		
Jedis jedis = pool.getResource();
jedis.set("foo", "bar");//string
String foobar = jedis.get("foo");
//zset
jedis.zadd("sose", 0, "car");//0是score
jedis.zadd("sose", 0, "bike"); 
Set<String> sose = jedis.zrange("sose", 0, -1);//score 范围
System.out.println(sose);

jedis.sadd("myset","mysetval");//set
jedis.lpush("mylist", "one");//list
jedis.lpush("mylist", "two");
jedis.hset("myhashStuScore", "zhang", "A");//hash
jedis.hset("myhashStuScore", "lisi", "B");

jedis.close();//一定要close 
pool.destroy();

//----transaction
jedis.watch("name");// 当前客户端监视该name键
//jedis.unwatch();    // 撤销监视

Transaction tran = jedis.multi();	// 开启事务状态

tran.set("name", "benson");	// 添加键值对
tran.set("job", "java");
Response<String> res= tran.get("job");
//tran.discard();		// 取消上述命令的执行
List<Object> list = tran.exec();	// 提交事务
System.out.println( res.get());//get在exec后执行

//一次性取全部,如是set命令结果为OK
for(Object resp : list) {
  System.out.println(resp.getClass().getName()+resp);
}

Transaction t = jedis.multi();
t.set("fool", "bar"); 
Response<String> result1 = t.get("fool");

t.zadd("foo", 1, "barowitch"); t.zadd("foo", 0, "barinsky"); t.zadd("foo", 0, "barikoviev");//是0
Response<Set<String>> sose = t.zrange("foo", 1, -1); 
List<Object> allResults =t.exec();

String foolbar = result1.get();
Set<String> set=sose.get();//如有错误,清数据    ,0分的只有第一个

//-------pipline
Pipeline p = jedis.pipelined();
p.set("fool", "bar"); 
p.zadd("foo", 1, "barowitch");  p.zadd("foo", 0, "barinsky"); p.zadd("foo", 0, "barikoviev");
Response<String> pipeString = p.get("fool"); // 先多次发送命令,过后再取结果,像transaction
Response<Set<String>> sose = p.zrange("foo", 0, -1);
p.sync(); 

int soseSize = sose.get().size();//如有错误,清数据
Set<String> setBack = sose.get();
System.out.println(setBack);

//-------
class MyListener extends JedisPubSub {
    public void onMessage(String channel, String message) {
    	System.out.println("onMessage:channel="+channel+" , msg="+message);
    }
    public void onSubscribe(String channel, int subscribedChannels) {
    	System.out.println("onSubscribe: channel="+channel+", subscribedChannels:"+subscribedChannels);
    }
    public void onUnsubscribe(String channel, int subscribedChannels) {
    	System.out.println("onSubscribe:channel="+channel+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPSubscribe(String pattern, int subscribedChannels) {
    	System.out.println("onSubscribe: pattern="+pattern+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    	System.out.println("onPUnsubscribe: pattern="+pattern+" receive subscribedChannels:"+subscribedChannels);
    }
    public void onPMessage(String pattern, String channel,  String message)
    {
    	System.out.println("onPMessage pattern="+pattern+",channel="+channel+" receive  :"+message);
    }
}
//jedis.psubscribe(listen, "a","b");//会一直阻塞,会根据参数个数n 调用n次JedisPubSub的onSubscribe方法
jedis.subscribe(listen, "foo");//会一直阻塞,会调用JedisPubSub的onSubscribe方法

jedis.publish("foo", "消息");//如已经有 subscribe(psubscribe无效)进程,则subscribe的进程会调用JedisPubSub的onMessage方法

//---集群的redis,依赖于 commons/poopl2
Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
jedisClusterNodes.add(new HostAndPort(node0Ip, node0Port));//只一个master节点OK
JedisCluster jc = new JedisCluster(jedisClusterNodes);
//jc.auth("123456");
jc.set("foo", "bar"); 
System.out.println(jc.get("foo"));
 

--shard 通过一致性哈希算法决定把数据存到哪台上,算是一种客户端负载均衡
JedisPoolConfig config=new JedisPoolConfig();
config.setMaxIdle(20);
config.setMaxTotal(30);
config.setMaxWaitMillis(5*1000);
config.setTestOnBorrow(false);
config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//逐出连接的最小空闲时间 默认
config.setMinEvictableIdleTimeMillis(20*60*1000);//20分



JedisShardInfo shardInfo =new JedisShardInfo(ip,port);//passwd
ShardedJedisPool shardedPool=new ShardedJedisPool(config,Arrays.asList(shardInfo)) ;
ShardedJedis shardedJedis=shardedPool.getResource();
//put-key
shardedJedis.set("user1", "用户1");
shardedJedis.expire("user1",20*60);//单位秒，20分
//setnx,incr
shardedJedis.set("user2", "用户2");

ShardedJedisPipeline pipeline = shardedJedis.pipelined();  
pipeline.set("trainNo1_SH", "20");  
pipeline.set("trainNo1_SZ", "10");  
List<Object> res = pipeline.syncAndReturnAll();
System.out.println(res);//[OK, OK]
//del-key
String delKeyLike="user";
Collection<Jedis> jedisCollect = shardedJedis.getAllShards();  
Iterator<Jedis> iter = jedisCollect.iterator();  
while (iter.hasNext()) 
{  
  Jedis jedis = iter.next();  
  Set<String> keys = jedis.keys(delKeyLike + "*");  
  jedis.del(keys.toArray(new String[keys.size()]));  
}   
shardedJedis.close();//一定要close



//官方说lua脚本的原子性 Atomicity of scripts,如脚本正在执行，其它命令或脚本不能执行
//lua脚本实现分布式锁，可以保存证setnx,expire两个操作的原子性 
//分布式锁,redis事务没有隔离性
@Test 
public void testRedisLuaDistributeLock()
{ 
	InputStream input=TestJedis.class.getResourceAsStream("/redis_jedis/lock.lua");
	
	StringBuilder strBuilder=new StringBuilder();
	 Scanner scanner =new Scanner(input);
	 while(scanner.hasNextLine())
	 {
	 	String line=scanner.nextLine();
	 	strBuilder.append(line).append("\n");
	 } 
	Object res=jedis.eval(strBuilder.toString(),1,"lockExport","user1","30");//参数 键名,值，超时秒
	System.out.println(res);
}

------lock.lua
-- 单行注释
--[[
 多行注释
没办法在这里暂停
 --]]  

 -- SETNX 成功设置返回1,失败设置返回0
local  isSet = redis.call('SETNX', KEYS[1], ARGV[1])
 if isSet == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[2]) 
   return "success"
 end
 return "fail"



---------------------------------Redis client  lettuce Spring用这个
 <dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>5.0.4.RELEASE</version>
</dependency>
使用reactor,netty 非阻塞的


//注意 jboss-client.jar了有netty的类
//		RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
//		RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
//		RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));

		RedisURI redisUri =RedisURI.Builder.redis("localhost", 6379).withPassword("").withDatabase(1)
				//.withSsl(true)
				.build();
		RedisClient redisClient = RedisClient.create(redisUri);
		
		//RedisAsyncCommands<String, String> commands = client.connect().async();
		//RedisFuture<String> future = commands.get("key");
		
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> syncCommands = connection.sync();

		syncCommands.set("key", "Hello, Redis!");

		connection.close();
		redisClient.shutdown();
		
public void testShowAllKeyValues() 
{
	Set<String> keys=jedis.keys("*");
	for(String key:keys)
	{
		String type=jedis.type(key);//set,zset,list,hash
		System.out.println("type:"+type+",key="+key);
		if("zset".equals(type))
		{
			Set<String> zsets=jedis.zrange(key, 0, -1); 
			System.out.println("\t value== \t ");
			for(String zset:zsets)
				System.out.println(" \t value="+zset+",score="+jedis.zcard(zset));
		}else if ("set".equals(type))
		{
			Set<String> values=jedis.smembers(key); 
			System.out.println("\t value== \t ");
			for(String value:values)
				System.out.println(" \t value="+value);
		}else if("list".equals(type))
		{
			List<String> values=jedis.lrange(key, 0, -1); 
			System.out.println("\t value== \t ");
			for(String value:values)
				System.out.println(" \t value="+value);
		}else if("hash".equals(type))
		{
			Set<String> hkeys=jedis.hkeys(key); 
			System.out.println("value== \t ");
			for(String hkey:hkeys)
				System.out.println(" \t \t "+hkey+"="+jedis.hget(key, hkey));
		}else if("string".equals(type))
		{
			String value=jedis.get(key); 
			System.out.println("\t value ="+value);
		}else
		{
			System.out.println("\t value =...." );
		}
	}
}
		
jedis.close();
jedis.shutdown();//会把redis服务器关了 
	
---------------------------------Redis client redisson	  分布式锁的实现 
 //JCache (JSR-107) 
<dependency>
  <groupId>org.redisson</groupId>
  <artifactId>redisson</artifactId>
  <version>3.15.0</version>
</dependency>

https://redis.io/topics/distlock 提到使用 redisson
https://github.com/redisson/redisson/wiki/ 有中文的文档

//redisson  依赖于netty 非阻塞的,fasterxml的jackson


Config config = new Config();
//--单机 
//SingleServerConfig singConfig= config.useSingleServer();
//singConfig.setAddress("redis://127.0.0.1:6379").setPassword(password);;

//--cluster配置
MasterSlaveServersConfig  msConfig=config.useMasterSlaveServers();
msConfig.setMasterAddress(masterIPPort);
msConfig.addSlaveAddress(slaveIPPort);//可传多个node

//RedissonClient redisson = Redisson.create();//默认 redis://127.0.0.1:6379
RedissonClient redisson = Redisson.create(config);
RKeys keys=redsson.getKeys();
 Iterable<String> iter=keys.getKeys();
 iter.forEach(new Consumer<String>()  //回调的要等才行
 {
	@Override
	public void accept(String key) {
		System.out.println("key="+key);  
	}
});

//---Distributed Object storage example
RBucket<AnyObject> bucket = redisson.getBucket("anyObject");
//bucket.set(new AnyObject());//单机OK,但cluster master 卡住???
bucket.setAsync(new AnyObject());//单机OK,但cluster master get时卡住???
AnyObject obj = bucket.get();

redisson.shutdown();

//分布式锁实现使用lua脚本，Semaphore来阻塞,redis的发布订阅来唤醒，hash数据结构key是线程ID
RLock lock = redisson.getLock("anyLock");
lock.lock(); 
lock.lock(10, TimeUnit.SECONDS);// acquire lock and automatically unlock it after 10 seconds

lock.unlock();

//如releaseTime不传，为-1，就会一个线程30秒的每1/3时间做一次续命，防止挂了一直锁 
lock.tryLock(waitTime, releaseTime, TimeUnit.SECONDS); 
// or wait for lock aquisition up to 100 seconds 
// and automatically unlock it after 10 seconds
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
if (res) {
   try {
     ...
   } finally {
       lock.unlock();
   }
}



现在Redis没有过期清空Map中的某个entry的功能，只能是清空Map所有的entry。Redission提供了这种功能。  测试成功

//加StringCodec.INSTANCE ,key是可读的字串，value也有部分是调用对象的toString()保存的， 从redis里再取也只有按String取
//RMapCache<String, SomeObject> keyExpireMap = redisson.getMapCache("keyExpireMap",StringCodec.INSTANCE);  

RMapCache<String, SomeObject> keyExpireMap = redisson.getMapCache("keyExpireMap",);  
keyExpireMap.put("key10", new SomeObject(), 10, TimeUnit.SECONDS);
keyExpireMap.put("key20", new SomeObject(), 20, TimeUnit.SECONDS);
 


@Configuration
public class RedissonConfig {
 @Value("${spring.redis.host}")
 private String host;
 @Value("${spring.redis.port}")
 private String port;
 @Value("${spring.redis.password}")
 private String password;
 @Bean
 public RedissonClient getRedisson(){
	 Config config = new Config();
	 config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
	 return Redisson.create(config);
 }
}
或用 
  	  <dependency>
         <groupId>org.redisson</groupId>
         <artifactId>redisson-spring-boot-starter</artifactId>
         <version>3.14.0</version>
     </dependency
 
============ZkClient
<dependency>
	<groupId>com.101tec</groupId>
	<artifactId>zkclient</artifactId>
	<version>0.10</version>
</dependency>
	
	
String rootPath="/testZkClient";

ZkClient zkClient = new ZkClient("10.1.5.225:2581",10000,10000,new SerializableSerializer());
System.out.println("连接OK");

IZkChildListener childListender=	new IZkChildListener() {
	@Override
	public void handleChildChange(String parentPath, List<String> currentChild) throws Exception {
		System.out.println(parentPath+"的子节点改变了,现有子节点有 "+currentChild);
	}
} ;

zkClient.subscribeChildChanges(rootPath,childListender );//也可以监听不存在的节点,一但建立会收到

UserLogin session=new UserLogin();
session.setLastLogin(new Date());
session.setUserName("lisi");
//类要实现Serializable接口
String path=zkClient.create(rootPath, session, CreateMode.PERSISTENT);
System.out.println(path+"建立了");

 
Stat stat=new Stat();
UserLogin zkData=zkClient.readData(rootPath,stat);
System.out.println("读到了"+zkData );
System.out.println("读到 stat getAversion="+stat.getAversion());


boolean exist=zkClient.exists(rootPath);
System.out.println(rootPath+"存在? "+exist );

List<String> childNames=zkClient.getChildren(rootPath);
System.out.println(rootPath+"的子节点有"+childNames );


String childPath=zkClient.createPersistentSequential(rootPath.concat("/childOne"), "ChildOneVal");
System.out.println(childPath+"建立了");

IZkDataListener changeListender=new IZkDataListener() {
	@Override
	public void handleDataDeleted(String path) throws Exception {
		System.out.println(path+"数据删了");
	}
	
	@Override
	public void handleDataChange(String path, Object newVal) throws Exception {
		System.out.println(path+"数据修改了,新数为"+newVal);
	}
};
zkClient.subscribeDataChanges(childPath, changeListender);
zkClient.writeData(childPath, "new ChildOneVal");
Thread.sleep(10);//如不加,可能会只响应最后一次操作
zkClient.delete(childPath);

//		zkClient.delete(rootPath);//只可删没有子节点的
zkClient.deleteRecursive(rootPath); 

zkClient.unsubscribeDataChanges(childPath, changeListender);
zkClient.unsubscribeChildChanges(rootPath, childListender);

System.out.println("所有建立的节点删除了");

============curator 
一个Zookeeper客户端

<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-framework</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-recipes</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
	<artifactId>curator-x-discovery</artifactId>
	<version>4.3.0</version>
</dependency>

curator-client-4.0.1.jar

String ip ="127.0.0.1";
String ipPort="127.0.0.1:2181";

String nodePath="/hello/world";
String nodeValue="123";




//		RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);//baseSleepTimeMs,  maxRetries 每次重试时间逐渐增加
//		RetryPolicy retryPolicy=new RetryNTimes(5,1000);//retryCount 最大重试次数，elapsedTimeMs
RetryPolicy retryPolicy=new RetryUntilElapsed(5000,1000);//maxElapsedTimeMs最多重试多长时间,   sleepMsBetweenRetries 每次重试时间间隔
//		CuratorFramework client=CuratorFrameworkFactory.newClient(ipPort,500,5000, retryPolicy);


List<AuthInfo> authInfos =new ArrayList<>();
AuthInfo auth=new AuthInfo("digest", "myuser:mypass".getBytes());
authInfos.add(auth);

CuratorFramework client= CuratorFrameworkFactory.builder().connectString(ipPort)
.sessionTimeoutMs(5000)
.connectionTimeoutMs(5000)
//		.authorization("digest", "myuser:mypass".getBytes()) //同命令  addauth digest  myuser:mypass
.authorization(authInfos)
.retryPolicy(retryPolicy)
.build();

client.start();


//		client.delete().deletingChildrenIfNeeded().forPath(nodePath);
//client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(1).forPath(nodePath); //可带withVersion
//guaranteed 如删除失败,会一直重试


 ACL aclIp=new ACL(Perms.READ,new Id("ip",ip));//Id构造器参数schema只可是ip(白名单)或digest(用户名密码)
 String userPwd=DigestAuthenticationProvider.generateDigest("myuser:mypass");
 ACL aclDigest=new ACL(Perms.READ|Perms.WRITE,new Id("digest",userPwd));
 ArrayList<ACL> aclList=new ArrayList<>();
//		 aclList.add(aclIp);
 aclList.add(aclDigest);


String path=client.create()
.creatingParentsIfNeeded() //如果一级不存会先创建再建二级目录
.withMode(CreateMode.PERSISTENT)
.withACL(aclList)
.forPath(nodePath,nodeValue.getBytes());

System.out.println(path);


Stat stat=new Stat();
byte[] data =client.getData().storingStatIn(stat).forPath(nodePath);
System.out.println("data= "+new String(data));
System.out.println("stat= "+stat);



List<String> children=client.getChildren().forPath(nodePath);
System.out.println("child have "+children);


stat=client.checkExists().forPath(nodePath);
System.out.println(nodePath+" = "+stat);//null 就不存

ExecutorService executorService= Executors.newFixedThreadPool(5);

 

client.setData().withVersion(stat.getVersion()).forPath(nodePath,"newData".getBytes());


//inBackground 转异步
 client.checkExists().inBackground(new BackgroundCallback() {
		@Override
		public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
			CuratorEventType type=event.getType();
			int resultCode=event.getResultCode(); //0成功
			System.out.println("processResult type= "+type);
			System.out.println("processResult resultCode= "+resultCode);
			System.out.println("processResult getContext= "+event.getContext());
			System.out.println("processResult getPath= "+event.getPath());
			System.out.println("processResult getChildren= "+event.getChildren());
			System.out.println("processResult data= "+new String(event.getData()));
		}
	},"contextVal",executorService).forPath(nodePath);

 
 
//监听 要 curator-recipes 包
NodeCache cache=new NodeCache(client,nodePath);
cache.start();
cache.getListenable().addListener(new NodeCacheListener() {
	@Override
	public void nodeChanged() throws Exception {
		byte[]data =cache.getCurrentData().getData();
		System.out.println("NodeCache data= "+new String(data));
	}
});


PathChildrenCache pathCache=new PathChildrenCache(client,nodePath,true);//true 子节点变化时，也取内容
pathCache.start();
pathCache.getListenable().addListener(new PathChildrenCacheListener() {
	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		switch(event.getType())
		{
			 case CHILD_ADDED :
				 System.out.println("CHILD_ADDED"+event.getData().getPath());
				 break;
			 case CHILD_UPDATED:
				 System.out.println("CHILD_UPDATED"+event.getData().getPath());
				  break;
			 case CHILD_REMOVED:
				 System.out.println("CHILD_REMOVED"+event.getData().getPath());
				  break;
					 
		}
	}
});
client.checkExists().watched().forPath(nodePath);
client.getCuratorListenable().addListener(new CuratorListener() {
	@Override
	public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception { 
		CuratorEventType type=event.getType();
		if(type==CuratorEventType.WATCHED)
		{
			WatchedEvent watchEvent=event.getWatchedEvent();//返回zookeeper的WatchedEvent
			EventType evtType=watchEvent.getType();
			System.out.println("watched path="+watchEvent.getPath());
			client.checkExists().watched().forPath(nodePath);//还要再次wached
		}
		//CuratorEventType.EXISTS ,CuratorEventType.DELETE ,CuratorEventType.CREATE
	}
});

//分布式锁应用,可以自己写一个aop拦截 @ClusterLock("/lock/order")来实现
InterProcessMutex lock=new InterProcessMutex(client,nodePath);
try 
{
	System.out.println("acquire lock ..."); 
	if(lock.acquire(10,TimeUnit.SECONDS))
	{
		System.out.println("geted lock");
		Thread.sleep(1000*3);//模拟使用时间  
	}else
	{
		System.out.println("get lock timeout");
	}
}finally {
	lock.release();
}

//要 curator-x-discovery-4.0.1.jar
//服务描述
ServiceInstanceBuilder<Map> service=ServiceInstance.builder();
service.address("127.0.0.1");
service.port(8080);
service.name(serviceName);//他创建zk节点
Map<String,String> payload=new HashMap<>();
payload.put("url","/api/v3/book");
service.payload(payload);//payload 放额外信息，可是任何类
ServiceInstance<Map> instance=service.build();
		
ServiceDiscovery  discovery=ServiceDiscoveryBuilder.builder(Map.class)
		.client(client)
		.serializer(new JsonInstanceSerializer<Map>(Map.class))
		.basePath("/service")
		.build();
//服务注册 
discovery.registerService(instance);
discovery.start();
//ls /service/book
//get /service/book/<uu-id>

//查找服务
ServiceDiscovery  discovery=ServiceDiscoveryBuilder.builder(Map.class)
		.client(client)
		.serializer(new JsonInstanceSerializer<Map>(Map.class))
		.basePath("/service")
		.build(); 
discovery.start(); 
Collection<ServiceInstance<Map>> all = discovery.queryForInstances(serviceName);
if(all.isEmpty())
	return null;
else
{
	//这里只要第一个服务
	ServiceInstance<Map>  service= new ArrayList<ServiceInstance<Map> >(all).get(0);
	System.out.println(service.getPayload());
	System.out.println(service.getAddress()); 
	return   service;
}

//选leader
LeaderSelectorListenerAdapter listener=new LeaderSelectorListenerAdapter() {
	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		//领导节点，方法结束后退出领导 。zk会再次重新选择领导
	}
};
LeaderSelector selector=new LeaderSelector(client,"/schedule",listener);
selector.autoRequeue();
selector.start(); 

System.in.read();
client.close();

-------------Hazelcast 缓存 替代  redis
https://hazelcast.org/  
Hazelcast IMDG 开源的 in-memory data grid

是多线程的，使用Java开发
有一个集群的领导，默认是最老的成员，管理数据是如何在系统间分布，但是，如果该节点当机，那么是下面一个旧节点接管
默认是271个分区，启动两次自动加入，显示有两个成员 

<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>4.0.1</version>
</dependency>  
服务端/客户端一样的 jar包,大小10MB
hazelcast-4.2\bin\start.bat 启动服务

hazelcast-4.2\management-center\start.bat  <端口号如8080> 启动管理界面 
http://127.0.0.1:8080 第一次启动 
		Settings->Current Security Provider: Dev Mode 下的Change按钮 
		要求注册 用户名/密码 (至少8个字符,数字,字母,特殊符号)如 hazelcast/HazelFree$ 
		
	Add Cluster Config按钮 cluster Name默认为dev(名与代码中的 config.setClusterName("myHazelInst") 对应),选中建立的,
	地址格式为 27.0.0.1:5701  回车后变为tag,可以输入多个，也可只输入集群中的一个节点
	可以看到很多信息,可以用程序连接
	console标签，可以输入命令,help帮助
默认存放目录 ~/hazelcast-mc

docker run hazelcast/hazelcast:$HAZELCAST_VERSION
docker run -e JAVA_OPTS="-Dhazelcast.local.publicAddress=<host_ip>:5701" -p 5701:5701 hazelcast/hazelcast:$HAZELCAST_VERSION
 

package cache_hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

public class DistributedTopic implements MessageListener<String> {

	static HazelcastInstance h;

	public static void main(String[] args) {

		Config config = new Config();

		h = Hazelcast.newHazelcastInstance(config);

		ITopic<String> topic = h.getTopic("my-distributed-topic");

		topic.addMessageListener(new DistributedTopic());

		topic.publish("Hello to distributed world");

	}

	@Override
	public void onMessage(Message<String> message) {
		System.out.println("Got message " + message.getMessageObject());

		h.shutdown();

	}

}

package cache_hazelcast;

import java.util.concurrent.ConcurrentMap;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class DistributedMap {

	public static void main(String[] args) {

		Config config = new Config();

		HazelcastInstance h = Hazelcast.newHazelcastInstance(config);

		ConcurrentMap<String, String> map = h.getMap("my-distributed-map");

		map.put("key", "value");

		// Concurrent Map methods

		map.putIfAbsent("somekey", "somevalue");

		map.replace("key", "value", "newvalue");

		map.forEach((k, v) -> System.out.println(k + " => " + v));

		h.shutdown();

	}

}

 IMap<String, String> mapLock = h.getMap("my-map-lock");
 mapLock.putIfAbsent("record1","value1");//上一线程在锁中，这里阻塞,多个Java进程间也是一样的
 //mapLock.lock("record1");
 mapLock.tryLock("record1",5,TimeUnit.SECONDS);//tryLock类似redisson
 System.out.println("locking");
 mapLock.unlock("record1");
 
---java client
ClientConfig clientConfig = new ClientConfig();
clientConfig.setClusterName("dev");
//先start.bat启动两个hazelcast服务
clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701", "127.0.0.1:5702");
HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
IMap<Integer, String> mapCustomers = client.getMap("MyMap"); //creates the map proxy
//客户端缓存变化事件
mapCustomers.addEntryListener(new MapListenerAdapter<String,String> () {
	@Override
	public void entryAdded(EntryEvent<String, String> event) {
		System.out.println(event);
	}
	@Override
	public void entryEvicted(EntryEvent<String, String> event) {
		System.out.println(event);
	}
	@Override
	public void entryRemoved(EntryEvent<String, String> event) {
		System.out.println(event);
	}
	@Override
	public void mapCleared(MapEvent event) {
		System.out.println(event);
	}
	@Override
	public void mapEvicted(MapEvent event) {
		System.out.println(event);
	}
},  true);


mapCustomers.put(1, "one");
//当存了数据后，management-center服务后UI http://127.0.0.1:8080 可以Storage->Maps进入 MyMap-> Map Browser 按钮->键输入为1，类型选择integer, 查看值
IQueue<String> clusterQueue=client.getQueue("MyQueue");

clusterQueue.add("element1");
//客户端缓存变化事件
clusterQueue.addItemListener(new ItemListener<String>() {
	@Override
	public void itemRemoved(ItemEvent<String> event) {
		System.out.println(event);
	}
	@Override
	public void itemAdded(ItemEvent<String> event) {
		System.out.println(event);
	}
}, true);

System.out.println(clusterQueue.size());
System.out.println(clusterQueue.poll());
System.out.println(clusterQueue.size());


//EntryXxxListener类名同redisson名字
class MyListener implements EntryAddedListener<String,String>,EntryRemovedListener<String,String>,EntryUpdatedListener<String,String>
							, EntryEvictedListener<String,String>,MapEvictedListener,MapClearedListener
{

}
---java Server
Config config = new  Config();
config.setClusterName("myHazelCluster");

NetworkConfig network=config.getNetworkConfig();
System.out.println(network.getPort());//默认5701
network.setPort(6701);//修改监听端口，如被占用+1，直到可用
		
HazelcastInstance server = Hazelcast.newHazelcastInstance(config);



--- 自带hazelcast-spring.jar
com.hazelcast.spring.cache.HazelcastCacheManager


--- spring session hazelcast 见 SpringMVC
--- spring boot hazelcast 见 SpringBoot

==============ZeroMQ
不是中间件,没有broker,即没一个服务器，只一个库,支持很多种语言
zero latency

和netty类似支持0拷贝，epoll实现 

zero broker 
zero cost 免费
zero administration.

Libzmq expose C-API and implemented in C++. (low-level library )

--JavaAPI有三个,JeroMQ,JZMQ,JCZMQ 这里用 JeroMQ

 <dependency>
  <groupId>org.zeromq</groupId>
  <artifactId>jeromq</artifactId>
  <version>0.5.2</version>
</dependency>

jeromq-0.5.2.jar
	jnacl-1.0.0.jar
	
public class hwserver
{
    public static void main(String[] args) throws Exception
    {
        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");

            while (!Thread.currentThread().isInterrupted()) {
                // Block until a message is received
                byte[] reply = socket.recv(0);//阻塞

                // Print the message
                System.out.println(
                    "Received: [" + new String(reply, ZMQ.CHARSET) + "]"
                );

                // Send a response
                String response = "Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }
}


public class hwclient
{
    public static void main(String[] args)
    {
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            System.out.println("Connecting to hello world server");

            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                String request = "Hello";
                System.out.println("Sending Hello " + requestNbr);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                System.out.println(
                    "Received " + new String(reply, ZMQ.CHARSET) + " " +
                    requestNbr
                );
            }
        }
    }
};
public class version
{
    public static void main(String[] args)
    {
        String version = ZMQ.getVersionString();
        int fullVersion = ZMQ.getFullVersion();

        System.out.println(
            String.format(
                "Version string: %s, Version int: %d", version, fullVersion
            )
        );
    }
} 

/**
 * Pubsub envelope publisher
 */
public class psenvpub
{

    public static void main(String[] args) throws Exception
    {
        // Prepare our context and publisher
        try (ZContext context = new ZContext()) {
            Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5563");

            while (!Thread.currentThread().isInterrupted()) {//一直发
                // Write two messages, each with an envelope and content
                publisher.sendMore("A");//相当于标题
                publisher.send("We don't want to see this");//相当于内容，要和标题一起发送
                publisher.sendMore("B1");
                publisher.send("We would like to see this");
            }
        }
    }
}


/**
 * Pubsub envelope subscriber
 */

public class psenvsub
{

    public static void main(String[] args)
    {
        // Prepare our context and subscriber
        try (ZContext context = new ZContext()) {
            Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5563");
            subscriber.subscribe("B".getBytes(ZMQ.CHARSET));//订B开头的消息

            while (!Thread.currentThread().isInterrupted()) {
                // Read envelope with address
                String address = subscriber.recvStr();//阻塞直到有内容
                // Read message contents
                String contents = subscriber.recvStr();
                System.out.println(address + " : " + contents);
            }
        }
    }
}

-------------MySQL XDevApi Table 表 
JavaScript 版本见 MySQL_Developer.sql
//X DevAPI  异步API 基于 X Protocol,依赖于com.google.protobuf

import com.mysql.cj.xdevapi.*;

	public static void main(String[] args)throws Exception
	{
		String baseUrl="mysqlx://localhost:33060/mydb?user=zh&password=123";
		//Session mySession = new SessionFactory().getSession(baseUrl);
		//连接池
		ClientFactory cf = new ClientFactory(); 
		Client cli = cf.getClient(baseUrl, "{\"pooling\":{\"enabled\":true, \"maxSize\":8,\"maxIdleTime\":30000, \"queueTimeout\":10000} }");
		Session mySession = cli.getSession();
		
		mysqlProcedure(mySession);
		showDatabases(mySession);
		mysqlTable(mySession);
		
		mySession.close();
		cli.close();
		
	}
	public static void mysqlProcedure(Session mySession) {
		mySession.sql("USE mydb").execute();
		mySession.sql("CREATE PROCEDURE my_add_one_procedure " + " (INOUT incr_param INT) " + "BEGIN " + "  SET incr_param = incr_param + 1;" + "END")
		        .execute();
		mySession.sql("SET @my_var = ?").bind(10).execute();
		mySession.sql("CALL my_add_one_procedure(@my_var)").execute();
		mySession.sql("DROP PROCEDURE my_add_one_procedure").execute();
		SqlResult myResult = mySession.sql("SELECT @my_var").execute();
		Row row = myResult.fetchOne();
		System.out.println(row.getInt(0));
	}
	public static void showDatabases(Session mySession)
	{
		List<Schema> schemaList = mySession.getSchemas();
		System.out.println("Available schemas in this session:");
		for (Schema schema : schemaList) {
		System.out.println(schema.getName());
		}
	}
 
	public static void mysqlTable(Session session) throws Exception
	{
		Schema  db= session.getSchema("mydb");
		
		// New method chaining used for executing an SQL SELECT statement
		// Recommended way for executing queries
		Table employees = db.getTable("employee");

		RowResult res = employees.select("username, age")
		  .where("username like :param")
		  .orderBy("username")
		  .bind("param", "李").execute(); //可以使用%通配符
		
		while(res.hasNext())//类似JDBC
		{
			Row row=res.next();
			System.out.println(row.getString(0) + row.getInt(1));//从0开始,和JDBC不同
			System.out.println(row.getString("username") + row.getInt("age"));
			//中文乱码
		}
		SqlResult result = session.sql("SELECT username, age " +
		  "FROM employee " +
		  "WHERE username like ? " +
		  "ORDER BY username").bind("王").execute(); //sql方法参数要用?
		 
		List<Row> rows1=result.fetchAll();//另一种方式
		for(Row row:rows1)
		{
			System.out.println(row.getString(0) + row.getInt(1)); 
			System.out.println(row.getString("username") + row.getInt("age"));
		}
		
	}
	public static void transaction(Session mySession) {
		Schema  db= mySession.getSchema("mydb");
		Table employeeTable = db.getTable("employee");
		
		mySession.startTransaction(); 
		employeeTable.insert("id", "username","age")
		  .values(2002, "张2",32)
		  .values(2003, "张3",33)
		  .execute();
		  
		//setSavepoint嵌套事务
		mySession.setSavepoint("level1");
		
		//mySession.sql("update   employee set  age=? where id=? ").bind(25).bind(2003).execute();
		
		//set用:变量不行？？？
		//OK
		employeeTable.update().set("age", 25).set("username", "张3").where("id= :id")
			.bind("id",2003).execute();
		 
		mySession.setSavepoint("level2");
		
		employeeTable.delete().where("id= :id").bind("id",2003);
		
		mySession.rollbackTo("level2");
		
		mySession.rollbackTo("level1");
		mySession.commit();
	}
-------------MySQL XDevApi NoSQL Collection
//其实NoSQL集合 就是 表 只有两个字段  ,一个_id类型为varbinary(32) ,一个doc 类型为JSON
{
	Schema db=mySession.getSchema("mydb");
	Table my_collection = db.getCollectionAsTable("my_collection");
	//my_collection.insert("doc").values("{\"username\": \"Ana\"}").execute();//不行??
}

Schema  db= mySession.getSchema("mydb");
Collection myColl = db.getCollection("my_collection");
//Collection myColl = db.getCollection("my_collection",true);//第二个参数requireExists，如不存在报异常
if(DatabaseObject.DbObjectStatus.NOT_EXISTS == myColl.existsInDatabase())
{
	myColl = db.createCollection("my_collection");
	DbDocImpl doc=new DbDocImpl()  ;
	doc.add("age",  new JsonNumber().setValue("18"));
	doc.add("name",  new JsonString().setValue("王"));
	myColl.add(doc).execute();

	myColl.add("{\"name\":\"Laurie\", \"age\":19}").execute();	
	myColl.add("{\"name\":\"Nadya\", \"age\":54}" ,"{\"name\":\"Lukas\", \"age\":32}").execute();
		
	
}
DocResult docs = myColl.find("name like :name or age < :age")
		.bind("name", "L%").bind("age", 20).execute();//通配符%和MySQL一样
while(docs.hasNext())//同JDBC
{
	DbDoc doc=docs.next();
	JsonValue val=new JsonString().setValue("2020");//无JsonDate
	System.out.println(doc.get("name") + doc.getOrDefault("birdthday",val ).toString());
}

myColl.modify("true").set("age", 19).execute(); //expr( "age + 1") 如何写表达式？？

Map<String, Object> params = new HashMap<>();
params.put("name", "Nadya");
myColl.modify("name = :name").set(".age", 25).bind(params).execute();//.age
		
		
DbDoc  ds=	docs.fetchOne();//docs.fetchAll()
String id=ds.get("_id").toFormattedString().replace("\"", "");//首尾带"

DbDoc doc2 = myColl.getOne(id); //等同于  myColl.find("_id = :id").bind("id", id).execute().fetchOne()
System.out.println(doc2);//中文乱码??
myColl.replaceOne(id, "{\"name\":\"中1\", \"age\":11}");
//myColl.addOrReplaceOne("101", "{\"name\":\"国\", \"age\":33}");//增加时可以手工指定id
myColl.removeOne(id);//相当于 myColl.remove("_id = :id").bind("id", id).execute()


//创建索引 
myColl.createIndex("age", "{\"fields\":[{\"field\": \"$.age\", \"type\":\"INT\", \"required\":true}]}");
// {fields: [{field: '$.age', type: 'INT'},{field: '$.username', type: 'TEXT(10)'}]}
SqlResult myResult =mySession.sql("SHOW INDEX FROM mydb.my_collection").execute();
for(Row row : myResult.fetchAll() )
{
	System.out.println(row.getString("Key_name")+","+row.getString(2));
	
}
//数组索引，要求8.0.17版本以后
//		collection.createIndex("emails_idx",  
//			    {fields: [{"field": "$.emails", "type":"CHAR(128)", "array": true}]});
myColl.dropIndex("age");


		
db.dropCollection("my_collection");

//两种数据NoSQL和关联型表 在一个事务中
public static void transaction(Session mySession) {
	Schema  db= mySession.getSchema("mydb");
	Table employeeTable = db.getTable("employee");
	Collection myColl = db.getCollection("my_collection");
	
	mySession.startTransaction(); 
	
	employeeTable.insert("id", "username","age")
	  .values(2004, "陈2",22)
	  .execute();
	
	mySession.setSavepoint("level1");

	DbDocImpl doc=new DbDocImpl()  ;
	doc.add("age",  new JsonNumber().setValue("28"));
	doc.add("name",  new JsonString().setValue("赵"));
	myColl.add(doc).execute();
	
	mySession.setSavepoint("level2");
	
	myColl.add("{\"name\":\"lisi\", \"age\":29}").execute();
	
	mySession.rollbackTo("level1");
	mySession.commit();
}
-------------
https://kubernetes.io/docs/reference/using-api/client-libraries/
https://github.com/kubernetes-client/java  示例代码是类似于kubectl的功能 

客户端7.0 支持到 k8s-1.15 版本

<dependency>
    <groupId>io.kubernetes</groupId>
    <artifactId>client-java</artifactId>
    <version>8.0.0</version>
</dependency>
