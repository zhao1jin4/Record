

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

3.11.0 ֧��MongoDB 4.2 �ֲ�ʽ����
 
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
	
		//replication set ����OK
		ServerAddress[] addrs=	new ServerAddress[] {
				new ServerAddress("127.0.0.1", 37017),
				new ServerAddress("127.0.0.1", 37018),
				new ServerAddress("127.0.0.1", 37019)};
		
		//������֧������
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
		for (String s : mongoClient.listDatabaseNames()) {//= show dbs  ,�����˴� --auth ������֤Ȩ�� 
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

//֧��JDK8
collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));

Document document = new Document("x", 1);
collection.insertOne(document, new SingleResultCallback<Void>() {
   @Override
   public void onResult(final Void result, final Throwable t) {
	   System.out.println("Inserted!");
   }
});

document.append("x", 2).append("y", 3);
//ģ����  UpdateResult
collection.replaceOne(Filters.eq("_id", document.get("_id")), document, 
	new SingleResultCallback<UpdateResult>() {
	   @Override
	   public void onResult(final UpdateResult result, final Throwable t) {
		   System.out.println(result.getModifiedCount());
	   }
   });	
//ģ����  List<Document>
collection.find().into(new ArrayList<Document>(), 
	new SingleResultCallback<List<Document>>() {
		@Override
		public void onResult(final List<Document> result, final Throwable t) {
			System.out.println("Found Documents: #" + result.size());
		}
	});

Block<Document> printDocumentBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {//��ѯ����ÿ��Document����һ��
			System.out.println(document.toJson());
		}
	};
SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
	@Override
	public void onResult(final Void result, final Throwable t) {//ֻ�����ʱ����һ��
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

//ģ����DeleteResult
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
		    		  System.out.println("��"+result.getMatchedCount()+"����¼match,delete��:" +result.getDeletedCount() + 
		    				  	",insert��:"+result.getInsertedCount() +
		    				  	",modified��:"+result.getModifiedCount());
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
	  new BulkWriteOptions().ordered(false),//��������˳����,�����������Ĭ���ǰ�˳������
	  printBatchResult
);
---------Querydsl MongoDB
http://www.querydsl.com/static/querydsl/latest/reference/html/
��Querying SQL(��maven������ɴ���),Querying Lucene, JPA,Querying Hibernate Search,Querying in Scala

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



���ɴ�����õ�
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

mvn clean install �� target/generated-sources/java Ŀ¼���� QCustomer.java
 
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

 final Employee pepe = new Employee("Pep�� Le Pew", 25000.0);
 datastore.save(pepe);

 elmer.getDirectReports().add(daffy);//�������directReports���ݣ�0��{$ref:"",$id:""}
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
		datastore.save(cust);//���һ��className���ֶ�
  
		DBObject dbObj=morphia.toDBObject(cust) ;
		System.out.println(dbObj);
		System.out.println("fromDBObject: " + morphia.fromDBObject(datastore,Customer.class, BasicDBObjectBuilder.start("lastName", "abc").get()));
		System.out.println("getMapper: " + morphia.getMapper());
		System.out.println("isMapped: " + morphia.isMapped(Customer.class));
		//QCustomer customer = new QCustomer("customer");
		QCustomer customer =   QCustomer.customer;
  //������ mysema-commons-lang-0.2.4.jar
		MorphiaQuery<Customer> query = new MorphiaQuery<Customer>(morphia, datastore, customer);
		List<Customer> list = query
		    .where(customer.firstName.eq("li"))
      .limit(5).offset(1)//����һ�� 
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

֧��JDK8,��֧��9

 NoSQL  ͼ ���ݿ�  Cypher Query Language


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
</dependency> <!-- ���������� org.neo4j.graphdb.GraphDatabaseService -->

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


String classpathFile="nosql_neo4j_ogm/neo4j-ogm.properties";//JDK 8 ������/��ͷ
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
	
	 @Transient //���洢
	private int released;

	@Relationship(type = "ACTS_IN",  
			 direction=Relationship.INCOMING
			 )
	Set<Actor> actors=new HashSet<>();

	 
	public Movie() { //������пյĹ��й�����
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
---------------------------------Redis client Jedis (springʹ�����)
https://github.com/xetorthio/jedis

<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
     <version>2.9.0</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>

RedisĬ�ϼ��� 6379 �˿�

//---���ڵ�,����OK
Jedis jedis=new Jedis(ip,6379);// ���� �� cluster��master
jedis.auth("123456");//������ masterauth ,requirepass 
String keys = "name";  
if(jedis.exists(keys))
	jedis.del(keys);  
jedis.set(keys, "snowolf");  
System.out.println(jedis.get(keys));  

//---���ڵ�,����OK
JedisPool pool = new JedisPool(new JedisPoolConfig(), ip,port,2000);//timeout,�ɼ�passworld����
		
Jedis jedis = pool.getResource();
jedis.set("foo", "bar");//string
String foobar = jedis.get("foo");
//zset
jedis.zadd("sose", 0, "car");//0��score
jedis.zadd("sose", 0, "bike"); 
Set<String> sose = jedis.zrange("sose", 0, -1);//score ��Χ
System.out.println(sose);

jedis.sadd("myset","mysetval");//set
jedis.lpush("mylist", "one");//list
jedis.lpush("mylist", "two");
jedis.hset("myhashStuScore", "zhang", "A");//hash
jedis.hset("myhashStuScore", "lisi", "B");

jedis.close();//һ��Ҫclose 
pool.destroy();

//----transaction
jedis.watch("name");// ��ǰ�ͻ��˼��Ӹ�name��
//jedis.unwatch();    // ��������

Transaction tran = jedis.multi();	// ��������״̬

tran.set("name", "benson");	// ��Ӽ�ֵ��
tran.set("job", "java");
Response<String> res= tran.get("job");
//tran.discard();		// ȡ�����������ִ��
List<Object> list = tran.exec();	// �ύ����
System.out.println( res.get());//get��exec��ִ��

//һ����ȡȫ��,����set������ΪOK
for(Object resp : list) {
  System.out.println(resp.getClass().getName()+resp);
}

Transaction t = jedis.multi();
t.set("fool", "bar"); 
Response<String> result1 = t.get("fool");

t.zadd("foo", 1, "barowitch"); t.zadd("foo", 0, "barinsky"); t.zadd("foo", 0, "barikoviev");//��0
Response<Set<String>> sose = t.zrange("foo", 1, -1); 
List<Object> allResults =t.exec();

String foolbar = result1.get();
Set<String> set=sose.get();//���д���,������    ,0�ֵ�ֻ�е�һ��

//-------pipline
Pipeline p = jedis.pipelined();
p.set("fool", "bar"); 
p.zadd("foo", 1, "barowitch");  p.zadd("foo", 0, "barinsky"); p.zadd("foo", 0, "barikoviev");
Response<String> pipeString = p.get("fool"); // �ȶ�η�������,������ȡ���,��transaction
Response<Set<String>> sose = p.zrange("foo", 0, -1);
p.sync(); 

int soseSize = sose.get().size();//���д���,������
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
//jedis.psubscribe(listen, "a","b");//��һֱ����,����ݲ�������n ����n��JedisPubSub��onSubscribe����
jedis.subscribe(listen, "foo");//��һֱ����,�����JedisPubSub��onSubscribe����

jedis.publish("foo", "��Ϣ");//���Ѿ��� subscribe(psubscribe��Ч)����,��subscribe�Ľ��̻����JedisPubSub��onMessage����

//---��Ⱥ��redis,������ commons/poopl2
Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
jedisClusterNodes.add(new HostAndPort(node0Ip, node0Port));//ֻһ��master�ڵ�OK
JedisCluster jc = new JedisCluster(jedisClusterNodes);
//jc.auth("123456");
jc.set("foo", "bar"); 
System.out.println(jc.get("foo"));
 

--shard ͨ��һ���Թ�ϣ�㷨���������ݴ浽��̨��,����һ�ֿͻ��˸��ؾ���
JedisPoolConfig config=new JedisPoolConfig();
config.setMaxIdle(20);
config.setMaxTotal(30);
config.setMaxWaitMillis(5*1000);
config.setTestOnBorrow(false);
config.setBlockWhenExhausted(false);//���Ӻľ�ʱ�Ƿ�����, false���쳣,ture����ֱ����ʱ, Ĭ��true
//������ӵ���С����ʱ�� Ĭ��
config.setMinEvictableIdleTimeMillis(20*60*1000);//20��



JedisShardInfo shardInfo =new JedisShardInfo(ip,port);//passwd
ShardedJedisPool shardedPool=new ShardedJedisPool(config,Arrays.asList(shardInfo)) ;
ShardedJedis shardedJedis=shardedPool.getResource();
//put-key
shardedJedis.set("user1", "�û�1");
shardedJedis.expire("user1",20*60);//��λ�룬20��
//setnx,incr
shardedJedis.set("user2", "�û�2");

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
shardedJedis.close();//һ��Ҫclose



//�ٷ�˵lua�ű���ԭ���� Atomicity of scripts,��ű�����ִ�У����������ű�����ִ��
//lua�ű�ʵ�ֲַ�ʽ�������Ա���֤setnx,expire����������ԭ���� 
//�ֲ�ʽ��,redis����û�и�����
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
	Object res=jedis.eval(strBuilder.toString(),1,"lockExport","user1","30");//���� ����,ֵ����ʱ��
	System.out.println(res);
}

------lock.lua
-- ����ע��
--[[
 ����ע��
û�취��������ͣ
 --]]  

 -- SETNX �ɹ����÷���1,ʧ�����÷���0
local  isSet = redis.call('SETNX', KEYS[1], ARGV[1])
 if isSet == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[2]) 
   return "success"
 end
 return "fail"



---------------------------------Redis client  lettuce Spring�����
 <dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>5.0.4.RELEASE</version>
</dependency>
ʹ��reactor,netty


//ע�� jboss-client.jar����netty����
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
jedis.shutdown();//���redis���������� 
	
---------------------------------Redis client redisson	  �ֲ�ʽ����ʵ�� 
 //JCache (JSR-107)
<dependency>
   <groupId>org.redisson</groupId>
   <artifactId>redisson</artifactId>
   <version>3.9.1</version>
</dependency>  

https://redis.io/topics/distlock �ᵽʹ�� redisson
https://github.com/redisson/redisson/wiki/ �����ĵ��ĵ�

//redisson  ������netty,fasterxml��jackson


Config config = new Config();
//--���� 
//SingleServerConfig singConfig= config.useSingleServer();
//singConfig.setAddress("redis://127.0.0.1:6379");

//--cluster����
MasterSlaveServersConfig  msConfig=config.useMasterSlaveServers();
msConfig.setMasterAddress(masterIPPort);
msConfig.addSlaveAddress(slaveIPPort);//�ɴ����node

//RedissonClient redisson = Redisson.create();//Ĭ�� redis://127.0.0.1:6379
RedissonClient redisson = Redisson.create(config);
RKeys keys=redsson.getKeys();
 Iterable<String> iter=keys.getKeys();
 iter.forEach(new Consumer<String>()  //�ص���Ҫ�Ȳ���
 {
	@Override
	public void accept(String key) {
		System.out.println("key="+key);  
	}
});

//---Distributed Object storage example
RBucket<AnyObject> bucket = redisson.getBucket("anyObject");
//bucket.set(new AnyObject());//����OK,��cluster master ��ס???
bucket.setAsync(new AnyObject());//����OK,��cluster master getʱ��ס???
AnyObject obj = bucket.get();

redisson.shutdown();
 
============ZkClient
<dependency>
	<groupId>com.101tec</groupId>
	<artifactId>zkclient</artifactId>
	<version>0.10</version>
</dependency>
	
	
String rootPath="/testZkClient";

ZkClient zkClient = new ZkClient("10.1.5.225:2581",10000,10000,new SerializableSerializer());
System.out.println("����OK");

IZkChildListener childListender=	new IZkChildListener() {
	@Override
	public void handleChildChange(String parentPath, List<String> currentChild) throws Exception {
		System.out.println(parentPath+"���ӽڵ�ı���,�����ӽڵ��� "+currentChild);
	}
} ;

zkClient.subscribeChildChanges(rootPath,childListender );//Ҳ���Լ��������ڵĽڵ�,һ���������յ�

UserLogin session=new UserLogin();
session.setLastLogin(new Date());
session.setUserName("lisi");
//��Ҫʵ��Serializable�ӿ�
String path=zkClient.create(rootPath, session, CreateMode.PERSISTENT);
System.out.println(path+"������");

 
Stat stat=new Stat();
UserLogin zkData=zkClient.readData(rootPath,stat);
System.out.println("������"+zkData );
System.out.println("���� stat getAversion="+stat.getAversion());


boolean exist=zkClient.exists(rootPath);
System.out.println(rootPath+"����? "+exist );

List<String> childNames=zkClient.getChildren(rootPath);
System.out.println(rootPath+"���ӽڵ���"+childNames );


String childPath=zkClient.createPersistentSequential(rootPath.concat("/childOne"), "ChildOneVal");
System.out.println(childPath+"������");

IZkDataListener changeListender=new IZkDataListener() {
	@Override
	public void handleDataDeleted(String path) throws Exception {
		System.out.println(path+"����ɾ��");
	}
	
	@Override
	public void handleDataChange(String path, Object newVal) throws Exception {
		System.out.println(path+"�����޸���,����Ϊ"+newVal);
	}
};
zkClient.subscribeDataChanges(childPath, changeListender);
zkClient.writeData(childPath, "new ChildOneVal");
Thread.sleep(10);//�粻��,���ܻ�ֻ��Ӧ���һ�β���
zkClient.delete(childPath);

//		zkClient.delete(rootPath);//ֻ��ɾû���ӽڵ��
zkClient.deleteRecursive(rootPath); 

zkClient.unsubscribeDataChanges(childPath, changeListender);
zkClient.unsubscribeChildChanges(rootPath, childListender);

System.out.println("���н����Ľڵ�ɾ����");

============curator 
һ��Zookeeper�ͻ���

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




//		RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);//baseSleepTimeMs,  maxRetries ÿ������ʱ��������
//		RetryPolicy retryPolicy=new RetryNTimes(5,1000);//retryCount ������Դ�����elapsedTimeMs
RetryPolicy retryPolicy=new RetryUntilElapsed(5000,1000);//maxElapsedTimeMs������Զ೤ʱ��,   sleepMsBetweenRetries ÿ������ʱ����
//		CuratorFramework client=CuratorFrameworkFactory.newClient(ipPort,500,5000, retryPolicy);


List<AuthInfo> authInfos =new ArrayList<>();
AuthInfo auth=new AuthInfo("digest", "myuser:mypass".getBytes());
authInfos.add(auth);

CuratorFramework client= CuratorFrameworkFactory.builder().connectString(ipPort)
.sessionTimeoutMs(5000)
.connectionTimeoutMs(5000)
//		.authorization("digest", "myuser:mypass".getBytes()) //ͬ����  addauth digest  myuser:mypass
.authorization(authInfos)
.retryPolicy(retryPolicy)
.build();

client.start();


//		client.delete().deletingChildrenIfNeeded().forPath(nodePath);
//client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(1).forPath(nodePath); //�ɴ�withVersion
//guaranteed ��ɾ��ʧ��,��һֱ����


 ACL aclIp=new ACL(Perms.READ,new Id("ip",ip));//Id����������schemaֻ����ip(������)��digest(�û�������)
 String userPwd=DigestAuthenticationProvider.generateDigest("myuser:mypass");
 ACL aclDigest=new ACL(Perms.READ|Perms.WRITE,new Id("digest",userPwd));
 ArrayList<ACL> aclList=new ArrayList<>();
//		 aclList.add(aclIp);
 aclList.add(aclDigest);


String path=client.create()
.creatingParentsIfNeeded() //���һ��������ȴ����ٽ�����Ŀ¼
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
System.out.println(nodePath+" = "+stat);//null �Ͳ���

ExecutorService executorService= Executors.newFixedThreadPool(5);

 

client.setData().withVersion(stat.getVersion()).forPath(nodePath,"newData".getBytes());


//inBackground ת�첽
 client.checkExists().inBackground(new BackgroundCallback() {
		@Override
		public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
			CuratorEventType type=event.getType();
			int resultCode=event.getResultCode(); //0�ɹ�
			System.out.println("processResult type= "+type);
			System.out.println("processResult resultCode= "+resultCode);
			System.out.println("processResult getContext= "+event.getContext());
			System.out.println("processResult getPath= "+event.getPath());
			System.out.println("processResult getChildren= "+event.getChildren());
			System.out.println("processResult data= "+new String(event.getData()));
		}
	},"contextVal",executorService).forPath(nodePath);

 
 
//���� Ҫ curator-recipes ��
NodeCache cache=new NodeCache(client,nodePath);
cache.start();
cache.getListenable().addListener(new NodeCacheListener() {
	@Override
	public void nodeChanged() throws Exception {
		byte[]data =cache.getCurrentData().getData();
		System.out.println("NodeCache data= "+new String(data));
	}
});


PathChildrenCache pathCache=new PathChildrenCache(client,nodePath,true);//true �ӽڵ�仯ʱ��Ҳȡ����
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
			WatchedEvent watchEvent=event.getWatchedEvent();//����zookeeper��WatchedEvent
			EventType evtType=watchEvent.getType();
			System.out.println("watched path="+watchEvent.getPath());
			client.checkExists().watched().forPath(nodePath);//��Ҫ�ٴ�wached
		}
		//CuratorEventType.EXISTS ,CuratorEventType.DELETE ,CuratorEventType.CREATE
	}
});

//�ֲ�ʽ��Ӧ��,�����Լ�дһ��aop���� @ClusterLock("/lock/order")��ʵ��
InterProcessMutex lock=new InterProcessMutex(client,nodePath);
try 
{
	System.out.println("acquire lock ..."); 
	if(lock.acquire(10,TimeUnit.SECONDS))
	{
		System.out.println("geted lock");
		Thread.sleep(1000*3);//ģ��ʹ��ʱ��  
	}else
	{
		System.out.println("get lock timeout");
	}
}finally {
	lock.release();
}

//Ҫ curator-x-discovery-4.0.1.jar
//��������
ServiceInstanceBuilder<Map> service=ServiceInstance.builder();
service.address("127.0.0.1");
service.port(8080);
service.name(serviceName);//������zk�ڵ�
Map<String,String> payload=new HashMap<>();
payload.put("url","/api/v3/book");
service.payload(payload);//payload �Ŷ�����Ϣ�������κ���
ServiceInstance<Map> instance=service.build();
		
ServiceDiscovery  discovery=ServiceDiscoveryBuilder.builder(Map.class)
		.client(client)
		.serializer(new JsonInstanceSerializer<Map>(Map.class))
		.basePath("/service")
		.build();
//����ע�� 
discovery.registerService(instance);
discovery.start();
//ls /service/book
//get /service/book/<uu-id>

//���ҷ���
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
	//����ֻҪ��һ������
	ServiceInstance<Map>  service= new ArrayList<ServiceInstance<Map> >(all).get(0);
	System.out.println(service.getPayload());
	System.out.println(service.getAddress()); 
	return   service;
}

//ѡleader
LeaderSelectorListenerAdapter listener=new LeaderSelectorListenerAdapter() {
	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		//�쵼�ڵ㣬�����������˳��쵼 ��zk���ٴ�����ѡ���쵼
	}
};
LeaderSelector selector=new LeaderSelector(client,"/schedule",listener);
selector.autoRequeue();
selector.start(); 

System.in.read();
client.close();

-------------Hazelcast ���� ���  redis
https://hazelcast.org/  
Hazelcast IMDG ��Դ�� in-memory data grid

�Ƕ��̵߳ģ�ʹ��Java����
��һ����Ⱥ���쵼��Ĭ�������ϵĳ�Ա�����������������ϵͳ��ֲ������ǣ�����ýڵ㵱������ô������һ���ɽڵ�ӹ�
Ĭ����271�����������������Զ����룬��ʾ��������Ա 

<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>4.0.1</version>
</dependency>  
�����/�ͻ���һ���� jar��,��С10MB
hazelcast-4.0.1\bin\start.bat ��������

hazelcast-4.0.1\management-center\start.bat ����������� 
http://127.0.0.1:8080 ��һ������Ҫ��ע�� �û���/���� (����8���ַ�,����,��ĸ,�������)�� hazelcast/HazelFree$
	Add Cluster Config��ť cluster NameĬ��Ϊdev,ѡ�н�����,���Կ����ܶ���Ϣ,�����ó�������
	console��ǩ��������������,help����

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
 mapLock.putIfAbsent("record1","value1");//��һ�߳������У���������,���Java���̼�Ҳ��һ����
 mapLock.lock("record1");
 System.out.println("locking");
 mapLock.unlock("record1");
 
---java client
ClientConfig clientConfig = new ClientConfig();
clientConfig.setClusterName("dev");
//��start.bat��������hazelcast����
clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701", "127.0.0.1:5702");
HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
IMap<Integer, String> mapCustomers = client.getMap("MyMap"); //creates the map proxy

mapCustomers.put(1, "one");
//���������ݺ�management-center�����UI http://127.0.0.1:8080 ����Storage->Maps���� MyMap-> Map Browser ��ť->������Ϊ1������ѡ��integer, �鿴ֵ
IQueue<String> clusterQueue=client.getQueue("MyQueue");

clusterQueue.add("element1");
System.out.println(clusterQueue.size());
System.out.println(clusterQueue.poll());
System.out.println(clusterQueue.size());



--- �Դ�hazelcast-spring.jar
com.hazelcast.spring.cache.HazelcastCacheManager


--- spring session hazelcast �� SpringMVC
--- spring boot hazelcast �� SpringBoot

==============ZeroMQ
�����м��,û��broker,��ûһ����������ֻһ����,֧�ֺܶ�������
zero latency

��netty����֧��0������epollʵ�� 

zero broker 
zero cost ���
zero administration.

Libzmq expose C-API and implemented in C++. (low-level library )

--JavaAPI������,JeroMQ,JZMQ,JCZMQ ������ JeroMQ

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
                byte[] reply = socket.recv(0);//����

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

            while (!Thread.currentThread().isInterrupted()) {//һֱ��
                // Write two messages, each with an envelope and content
                publisher.sendMore("A");//�൱�ڱ���
                publisher.send("We don't want to see this");//�൱�����ݣ�Ҫ�ͱ���һ����
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
            subscriber.subscribe("B".getBytes(ZMQ.CHARSET));//��B��ͷ����Ϣ

            while (!Thread.currentThread().isInterrupted()) {
                // Read envelope with address
                String address = subscriber.recvStr();//����ֱ��������
                // Read message contents
                String contents = subscriber.recvStr();
                System.out.println(address + " : " + contents);
            }
        }
    }
}

-------------MySQL XDevApi Table �� 
JavaScript �汾�� MySQL_Developer.sql
//X DevAPI  �첽API ���� X Protocol,������com.google.protobuf

import com.mysql.cj.xdevapi.*;

	public static void main(String[] args)throws Exception
	{
		String baseUrl="mysqlx://localhost:33060/mydb?user=zh&password=123";
		//Session mySession = new SessionFactory().getSession(baseUrl);
		//���ӳ�
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
		  .bind("param", "��").execute(); //����ʹ��%ͨ���
		
		while(res.hasNext())//����JDBC
		{
			Row row=res.next();
			System.out.println(row.getString(0) + row.getInt(1));//��0��ʼ,��JDBC��ͬ
			System.out.println(row.getString("username") + row.getInt("age"));
			//��������
		}
		SqlResult result = session.sql("SELECT username, age " +
		  "FROM employee " +
		  "WHERE username like ? " +
		  "ORDER BY username").bind("��").execute(); //sql��������Ҫ��?
		 
		List<Row> rows1=result.fetchAll();//��һ�ַ�ʽ
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
		  .values(2002, "��2",32)
		  .values(2003, "��3",33)
		  .execute();
		  
		//setSavepointǶ������
		mySession.setSavepoint("level1");
		
		//mySession.sql("update   employee set  age=? where id=? ").bind(25).bind(2003).execute();
		
		//set��:�������У�����
		//OK
		employeeTable.update().set("age", 25).set("username", "��3").where("id= :id")
			.bind("id",2003).execute();
		 
		mySession.setSavepoint("level2");
		
		employeeTable.delete().where("id= :id").bind("id",2003);
		
		mySession.rollbackTo("level2");
		
		mySession.rollbackTo("level1");
		mySession.commit();
	}
-------------MySQL XDevApi NoSQL Collection
//��ʵNoSQL���� ���� �� ֻ�������ֶ�  ,һ��_id����Ϊvarbinary(32) ,һ��doc ����ΪJSON
{
	Schema db=mySession.getSchema("mydb");
	Table my_collection = db.getCollectionAsTable("my_collection");
	//my_collection.insert("doc").values("{\"username\": \"Ana\"}").execute();//����??
}

Schema  db= mySession.getSchema("mydb");
Collection myColl = db.getCollection("my_collection");
//Collection myColl = db.getCollection("my_collection",true);//�ڶ�������requireExists���粻���ڱ��쳣
if(DatabaseObject.DbObjectStatus.NOT_EXISTS == myColl.existsInDatabase())
{
	myColl = db.createCollection("my_collection");
	DbDocImpl doc=new DbDocImpl()  ;
	doc.add("age",  new JsonNumber().setValue("18"));
	doc.add("name",  new JsonString().setValue("��"));
	myColl.add(doc).execute();

	myColl.add("{\"name\":\"Laurie\", \"age\":19}").execute();	
	myColl.add("{\"name\":\"Nadya\", \"age\":54}" ,"{\"name\":\"Lukas\", \"age\":32}").execute();
		
	
}
DocResult docs = myColl.find("name like :name or age < :age")
		.bind("name", "L%").bind("age", 20).execute();//ͨ���%��MySQLһ��
while(docs.hasNext())//ͬJDBC
{
	DbDoc doc=docs.next();
	JsonValue val=new JsonString().setValue("2020");//��JsonDate
	System.out.println(doc.get("name") + doc.getOrDefault("birdthday",val ).toString());
}

myColl.modify("true").set("age", 19).execute(); //expr( "age + 1") ���д���ʽ����

Map<String, Object> params = new HashMap<>();
params.put("name", "Nadya");
myColl.modify("name = :name").set(".age", 25).bind(params).execute();//.age
		
		
DbDoc  ds=	docs.fetchOne();//docs.fetchAll()
String id=ds.get("_id").toFormattedString().replace("\"", "");//��β��"

DbDoc doc2 = myColl.getOne(id); //��ͬ��  myColl.find("_id = :id").bind("id", id).execute().fetchOne()
System.out.println(doc2);//��������??
myColl.replaceOne(id, "{\"name\":\"��1\", \"age\":11}");
//myColl.addOrReplaceOne("101", "{\"name\":\"��\", \"age\":33}");//����ʱ�����ֹ�ָ��id
myColl.removeOne(id);//�൱�� myColl.remove("_id = :id").bind("id", id).execute()


//�������� 
myColl.createIndex("age", "{\"fields\":[{\"field\": \"$.age\", \"type\":\"INT\", \"required\":true}]}");
// {fields: [{field: '$.age', type: 'INT'},{field: '$.username', type: 'TEXT(10)'}]}
SqlResult myResult =mySession.sql("SHOW INDEX FROM mydb.my_collection").execute();
for(Row row : myResult.fetchAll() )
{
	System.out.println(row.getString("Key_name")+","+row.getString(2));
	
}
//����������Ҫ��8.0.17�汾�Ժ�
//		collection.createIndex("emails_idx",  
//			    {fields: [{"field": "$.emails", "type":"CHAR(128)", "array": true}]});
myColl.dropIndex("age");


		
db.dropCollection("my_collection");

//��������NoSQL�͹����ͱ� ��һ��������
public static void transaction(Session mySession) {
	Schema  db= mySession.getSchema("mydb");
	Table employeeTable = db.getTable("employee");
	Collection myColl = db.getCollection("my_collection");
	
	mySession.startTransaction(); 
	
	employeeTable.insert("id", "username","age")
	  .values(2004, "��2",22)
	  .execute();
	
	mySession.setSavepoint("level1");

	DbDocImpl doc=new DbDocImpl()  ;
	doc.add("age",  new JsonNumber().setValue("28"));
	doc.add("name",  new JsonString().setValue("��"));
	myColl.add(doc).execute();
	
	mySession.setSavepoint("level2");
	
	myColl.add("{\"name\":\"lisi\", \"age\":29}").execute();
	
	mySession.rollbackTo("level1");
	mySession.commit();
}
-------------
https://kubernetes.io/docs/reference/using-api/client-libraries/
https://github.com/kubernetes-client/java  ʾ��������������kubectl�Ĺ��� 

�ͻ���7.0 ֧�ֵ� k8s-1.15 �汾

<dependency>
    <groupId>io.kubernetes</groupId>
    <artifactId>client-java</artifactId>
    <version>8.0.0</version>
</dependency>
