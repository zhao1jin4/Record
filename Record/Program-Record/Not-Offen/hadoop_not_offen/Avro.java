
=============Avro
支持C++ ,Python,C#

<dependency>
  <groupId>org.apache.avro</groupId>
  <artifactId>avro</artifactId>
  <version>1.8.2</version>
</dependency> 
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.13</version>
</dependency>

使用JSON定义 Avro schemas

原始数据类型 (null, boolean, int, long, float, double, bytes, and string) 
复杂数据类型 (record, enum, array, map, union, and fixed)

user.avsc:

{"namespace": "example.avro",
 "type": "record",
 "name": "User",
 "fields": [
     {"name": "name", "type": "string"},
     {"name": "favorite_number",  "type": ["int", "null"]},
     {"name": "favorite_color", "type": ["string", "null"]}
 ]
}
 
schemas 至少有   type ("type": "record"),   name ("name": "User")
全名是 example.avro.User
java -jar /path/to/avro-tools-1.8.2.jar compile schema <schema file> <destination>
java -jar /path/to/avro-tools-1.8.2.jar compile schema user.avsc .


// Construct via builder
User user3 = User.newBuilder()
			 .setName("Charlie")
			 .setFavoriteColor("blue")
			 .setFavoriteNumber(null)
			 .build();
					 
// Serialize user1, user2 and user3 to disk
DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
dataFileWriter.create(user1.getSchema(), file); 
dataFileWriter.append(user1);
dataFileWriter.append(user2);
dataFileWriter.append(user3);
dataFileWriter.close();//保存的文件内容就是JSON schema

 
// Deserialize Users from disk
DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
User user = null;
while (dataFileReader.hasNext()) { 
	user = dataFileReader.next(user);
	System.out.println(user);
}
//输出的就是JSON  

---DemoService.avdl
@namespace ("hadoop.avro.transfer")
protocol DemoService
{
    import schema "Person.avsc";
    import schema "QueryParameter.avsc";
    string ping();
    array<hadoop.avro.transfer.Person> getPersonList(hadoop.avro.transfer.QueryParameter queryParameter);
}
#转换 .avdl 文件到 .avpr 文件用
java -jar avro-tools-1.8.2.jar idl DemoService.avdl DemoService.avpr
java -jar avro-tools-1.8.2.jar compile protocol  DemoService.avpr  .
---Person.avsc
{
  "namespace": "hadoop.avro.transfer",
  "type": "record",
  "name": "Person",
  "fields": [
    {
      "name": "age",
      "type": "int"
    },
    {
      "name": "name",
      "type": "string"
    },
    {
      "name": "sex",
      "type": "boolean"
    },
    {
      "name": "salary",
      "type": "double"
    },
    {
      "name": "childrenCount",
      "type": "int"
    }
  ]
}
---QueryParameter.avsc
{
  "namespace": "hadoop.avro.transfer",
  "type": "record",
  "name": "QueryParameter",
  "fields": [
    {
      "name": "ageStart",
      "type": "int"
    },
    {
      "name": "ageEnd",
      "type": "int"
    }
  ]
}
--server
//        Server nettyServer = new NettyServer(new SpecificResponder(DemoService.class,
//                new DemoServiceImpl()),
//                new InetSocketAddress(65111));
        //二选 一
//
        Server saslSocketServer = new SaslSocketServer(new SpecificResponder(DemoService.class,
                new DemoServiceImpl()),
                new InetSocketAddress(10000));
 
--client
//NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
//二选 一
SaslSocketTransceiver client = new SaslSocketTransceiver(new InetSocketAddress(10000));

DemoService proxy = (DemoService) SpecificRequestor.getClient(DemoService.class, client);
System.out.println(proxy.ping());

QueryParameter parameter = new QueryParameter();
parameter.setAgeStart(5);
parameter.setAgeEnd(50);
proxy.getPersonList(parameter);

client.close();

 