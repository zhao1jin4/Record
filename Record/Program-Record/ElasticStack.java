ELK= Elasticsearch , Logstash, Kibana
新的是ElstaicStack 多了一个Beat,核心是Elastic Search

================================Elastic Search    6.4
https://www.elastic.co/guide/index.html
支持 k8s https://www.elastic.co/guide/en/cloud-on-k8s/current/index.html
 

elasticsearch-7.4.1 要求用JDK11 
windows/linux 都是单独的包

 
vi config/elasticsearch.yml
	network.host: 0.0.0.0   可以接收任何地址  如不是127.0.0.1或localhost就认为是生产环境 
	#http.port: 9200
	#path.data:  <home>/data 
	#path.logs:  <home>/logs



ES使用 mmapfs 存储 mapping a file into memory (mmap)

--vi /etc/sysctl.conf
vm.max_map_count=262144

sysctl -p 立即生效

也可使用 sysctl -w vm.max_map_count=262144


vi /etc/security/limits.conf
* soft nofile 65535
* hard nofile 131071
 
ulimit -Sn 65535
ulimit -Hn 131071
ulimit -Hn 查看

centOS-7.6 本机和远程 就可以生效

----???
openSUSE-leap-15 本机不行，远程登录就可 ，重启没用 ???


/etc/ssh/sshd_config 中UsePAM 默认yes 
 
grep limit /etc/pam.d/*			*/
有common-session文件提示是被  pam-config生成的

vi  /etc/pam.d/login
加入 
session required  pam_limits.so 
#即 /lib64/security/pam_limits.so

openSUSE-leap-15重启 只root用户生效，普通用户还是不行???? 
 
---- 

vi config/jvm.options 
可以修改 1g 到 256M
-Xms1g
-Xmx1g
 


不能以root运行
bin\elasticsearch   启动   http://localhost:9200/ 有JSON 返回，包含版本信息的cluser_name,lucene_version
-d 后台启动


curl -X GET "localhost:9200/_cat/health?v&pretty"


中文支持，会在线下载
bin/elasticsearch-plugin install analysis-smartcn    
bin/elasticsearch-plugin remove analysis-smartcn

--------------集群 
#再启动一个实例自动加入第一个节点的 cluster ？？？？
#./elasticsearch -Epath.data=data2 -Epath.logs=log2
集群未成功？？？？


主节点可failvoer

----vi config/elasticsearch.yml
#network.host: 0.0.0.0
#http.port: 9200

放开
#cluster.name: my-application
#node.name: node-1
#discovery.seed_hosts: ["host1", "host2"]  #做修改可，只要一个
#cluster.initial_master_nodes: ["node-1", "node-2"]  #做修改可，只要一个

#path.data: /path/to/data
#path.logs: /path/to/logs

#老版本增加，新版本加了也没什么影响
node.master: true   #可以成为master,同时只有一个master
node.data: true
discovery.zen.ping.unicast.hosts:  ["host1", "host2"]
discovery.zen.minimum_master_nodes: 2   #要是unicast.hosts的个数/2+1，否则master挂了再启动后也不能加入集群，就有两个master就会出现Split Brain脑裂 

#增加为elasticsearch-head 跨域
http.cors.enabled: true
http.cors.allow-origin: "*"
----

curl -X GET "localhost:9200/_cluster/health?pretty" 


--------elasticsearch-head 配置工具
https://github.com/mobz/elasticsearch-head

git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install
npm run start
打开 http://localhost:9100/
如节点前是五角星，是主节点
粗的数字方块主，细的数字方块表示从

因有跨域问题 修改elastic search 的 config/elasticsearch.yml 
http.cors.enabled: true
http.cors.allow-origin: "*"


browser标签看数据 
-------
每个索引可以有一个或多个分片，每个分片可能多个复本
文档类型,像Mongo

建立索引后主分片的数量就不能修改。原因为 文档的存储在哪个数据节点上 =  hash(routing默认使用_id)%主分片的数量 

请求转发
分布式 类似数据库的commit，一个主数据节点在写完数据通知所有其它复本节点，如收到完成再返回到客户端，但在其它复本节点没有写完前，刚好查这条数据（可能到复本节点）就有可能是空的
分布式 分页读文档，一节点数据先在各个节点查_id，在内部队列中做排序，再次到各个节点取数据返回到客户端





数据存储于一个或多个索引中，索引是具有类似特性的文档的集合(类似mongo的collection)
索引相当于SQL中的一个数据库 (新版本相当于表)
Type 在6的版本中过时了 相当于“表”
索引由其名称(必须为全小写字符)进行标识


curl选项
-X, --request <command> a custom request method 
-i, --include (HTTP) Include the HTTP-header in the output
-H, --header <header>
-d, --data <data>  specified data in a POST request
--data-binary  @accounts.json

 

curl -X PUT "localhost:9200/customer/_doc/1?pretty" -H 'Content-Type: application/json' -d'
{
  "name": "John Doe"
}
' 
curl -X GET "localhost:9200/customer/_doc/1?pretty"
curl -X DELETE 'http://localhost:9200/customer/_doc/1'  
删除只是做了个删除标记，不会立即删除，后面增加时才会删（可能是覆盖）

--accounts.json 批量数据格式如下 https://github.com/elastic/elasticsearch/blob/master/docs/src/test/resources/accounts.json
{"index":{"_id":"1"}}
{"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"}
{"index":{"_id":"6"}}
{"account_number":6,"balance":5686,"firstname":"Hattie","lastname":"Bond","age":36,"gender":"M","address":"671 Bristol Street","employer":"Netagy","email":"hattiebond@netagy.com","city":"Dante","state":"TN"}

curl -H "Content-Type: application/json" -XPOST "localhost:9200/bank/_bulk?pretty&refresh" --data-binary "@accounts.json"
curl "localhost:9200/_cat/indices?v"


Elasticsearch Query DSL   (Domain Specific Language) 是使用json消息体做查询条件

curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ]
}
'
默认只返回10条 
vi查关键字的出现次数 :%s/account_number/&/gn

返回信息
took – 用了多长时间 milliseconds
timed_out – 是否超时
_shards – 多少分片成功，多少分片失败   
max_score – the score of the most relevant document found
hits.total.value – 发现多少个文档

highlight没有效果 ？？？
curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": { "match_all": {} },
  "highlight": { "fields": {"email":{}} },
  "sort": [
    { "account_number": "asc" }
  ]
}
'

分页请求 match_all
curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ],
  "from": 10,
  "size": 10
}
'
参数分页
curl -X GET "localhost:9200/bank/_search?pretty&from=10&size=10" 

查询 有多个值 match 是或的关系
curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": { "match": { "address": "mill lane" } }
}
'

全匹配 match_phrase 

curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": { "match_phrase": { "address": "mill lane" } }
}
'


must  和 must_not 

curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}
'

范围

curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must": { "match_all": {} },
      "filter": {
        "range": {
          "balance": {
            "gte": 20000,
            "lte": 30000
          }
        }
      }
    }
  }
}
'

组合 aggs terms 其中group_by_state是集合返回名字，按state字段分组显示文档数, .keyword 是固定写法

"terms" 下增加 "execution_hint": "map" 默认值为 global_ordinals
https://www.elastic.co/guide/en/elasticsearch/reference/7.5/search-aggregations-bucket-terms-aggregation.html#search-aggregations-bucket-terms-aggregation-execution-hint

curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      }
    }
  }
}
'
aggs avg  其中average_balance是字段显示名字
curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
'

分组排序
curl -X GET "localhost:9200/bank/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword",
        "order": {
          "average_balance": "desc"
        }
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
'
 
--------------
新建一个名叫weather的 Index

curl -X PUT 'http://localhost:9200/weather'
curl -X DELETE -i  http://localhost:9200/weather
-i 输出响应头

 
--PUT
向指定的 /Index/Type 发送 PUT 请求，就可以在 Index 里面新增一条记录

 curl -X PUT 'http://localhost:9200/accounts/person/1' -H 'Content-Type: application/json' -d '{
  "user": "张三",
  "title": "工程师",
  "desc": "数据库管理"
}' 

记录的 Id。它不一定是数字，任意字符串
"result":"created"


--override PUT
更新记录就是使用 PUT 请求，重新发送一次数据,要求已有的数据不能少
 curl -X PUT 'http://localhost:9200/accounts/person/1' -H 'Content-Type: application/json' -d '{
    "user" : "张三",
    "title" : "工程师",
    "desc" : "数据库管理，软件开发"
}' 

"result":"updated"
 返回 "_version": 有变值
 



--POST
如为POST 可不用指定ID,生成长串ID  

curl -X POST 'http://localhost:9200/accounts/person/' -H 'Content-Type: application/json' -d '{
  "user": "张三",
  "title": "工程师",
  "desc": "数据库管理"
}' 


如果没有先创建 Index（这个例子是accounts），直接执行上面的命令，Elastic 也不会报错，而是直接生成指定的 Index。
所以，打字的时候要小心，不要写错 Index 的名称。

 

---  _update 某个值，而不是override
 
curl -X POST -H 'Content-Type: application/json' -i 'http://localhost:9200/accounts/person/1/_update?pretty' --data '{
  "doc": { "user": "zhangsan" }
}'


修改值和增加字段age
curl -X POST -H 'Content-Type: application/json' -i 'http://localhost:9200/accounts/person/1/_update?pretty' --data '{
  "doc": { "user": "zhangsan1","age":20 }
}'

修改值增加5
curl -X POST -H 'Content-Type: application/json' -i 'http://localhost:9200/accounts/person/1/_update?pretty' --data '{
  "script" : "ctx._source.age += 5"
}'

_source只返回指定字段
curl 'http://localhost:9200/accounts/person/_search?pretty=true&_source=user,title' 
 
curl -X DELETE 'http://localhost:9200/accounts/person/1'

https://www.elastic.co/guide/en/elasticsearch/reference/7.4/docs-bulk.html
 
curl -X POST 'http://localhost:9200/student/_bulk' -H 'Content-Type: application/json' -d '{
 {"create" : { "_index" : "student", "_id" : "1" } }
 {"stu_id": "1001", "name": "李四","age":21}
 {"create" : { "_index" : "student", "_id" : "2" } }
 {"stu_id": "1002", "name": "王五","age":22}
 
 '
必须要有空行结尾,属性名必须使用""


批量删除
curl -X POST 'http://localhost:9200/student/_bulk' -H 'Content-Type: application/json' -d '{
 {"delete" : { "_index" : "student", "_id" : "1" } } 
 {"delete" : { "_index" : "student", "_id" : "2" } } 
 
 '
 
同时插入两条数据 doc/_bulk
curl -X POST -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_bulk?pretty' --data '
{"index":{"_id":"1"}}
{"name": "John Doe" }
{"index":{"_id":"2"}}
{"name": "Jane Doe" }
'

curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_doc/_search?pretty'
生成的数据_type默认为_doc,有时可有可无
curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_search?pretty'

curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_doc/1?pretty'
 
/_source 不要元数据(_doc不能去)
 curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_doc/1/_source?pretty'

curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_doc/_search?q=name:Jane&pretty'
但不能中文


也可即更新又删除
curl -X POST -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_doc/_bulk?pretty' --data '
{"update":{"_id":"1"}}
{"doc": { "name": "John Doe becomes Jane Doe" } }
{"delete":{"_id":"2"}}
'
某一个命令执行出错，那么会继续执行后面的命令，最后会返回每个命令的执行结果

删索引
curl -X DELETE 'http://localhost:9200/customer' 

删全部数据


--GET

向/Index/Type/Id发出 GET 请求，就可以查看这条记录。
curl 'http://localhost:9200/accounts/person/1?pretty=true' 

 "found" : true,  表示查询成功    _source字段返回原始记录。

 curl 'http://localhost:9200/accounts/person/1?pretty=true' 
--- _search

 GET 方法，直接请求/Index/Type/_search，就会返回所有记录。
 结果的 took字段表示该操作的耗时（单位为毫秒）
 
 可中文和模糊匹配
GET _search  
{
    "query" : {
        "match" : {
            "user" : "张三"   
        }
    }
}

GET accounts/person/_search 
{
    "query" : {
        "bool": {
            "must": {
                "match" : {
                    "user" : "zhangsan" 
                }
            },
            "filter": {
                "range" : {
                    "age" : { "gt" : 10 } 
                }
            }
        }
    }
}


--DELETE
 删除记录就是发出 DELETE 请求。


curl -X DELETE 'http://localhost:9200/accounts/person/1'
如无返回   "result":"not_found"


检查 cluster health, 使用 _cat API.
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' -i 'http://localhost:9200/_cat/health?v'
status
red：表示有些数据不可用
yellow：表示所有数据可用，但是备份不可用
green：表示一切正常


_mget 参数用ids,如有一个不存在，不影响其它的返回
curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:9200/customer/_mget?pretty' -d '
{
	"ids": ["1","2"]
}'



数据类型
	string(过时)，text(是被搜索的),keyword(不会被搜索，如email)
	byte,short,integer,long
	float,double
	boolean
	date

 
curl -X PUT -H 'Content-Type: application/json' -i 'http://localhost:9200/user' -d '
{
   "settings":
   {
    "index":
    {
      "number_of_shards":2,
      "number_of_replicas":2    
    }   
   },
   "mappings":
   { 
       "properties":
      {
        "name":
        {
          "type":"text"        
        },
        "age":
        {
          "type":"integer"        
        },
        "email":
        {
          "type":"keyword"        
        } 
      }   
   }
}'

curl -X GET 'http://localhost:9200/user/_mapping'


curl -X POST 'http://localhost:9200/user/_bulk' -H 'Content-Type: application/json' -d ' 
{"index" : { "_index" : "user", "_id" : "1" } }
{"name": "李四","age":21,"email":"aa@abc.com"}
{"index" : { "_index" : "user", "_id" : "2" } }
{"name": "王五","age":22,"email":"bb@abc.com"}

'

curl -X GET 'http://localhost:9200/user/_mapping'



curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "term":
    {
      "age":21   
    }
  } 
 }'
term用于精确匹配 ，字段不能是text类型

terms可数组做参数
curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "terms":
    {
      "age":[20,21,22]  
    }
  } 
 }'


exist是否有指定的字段

curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "exists":
    {
      "field":"hobby"
    }
  } 
 }'

should 是or的意思 ，bool里还可有must,must_not ,match可以是text类型

curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "bool":
    {
      "should":
      [
      	{"term":{"age":21}},
      	{"match":{"name":"王五"}}
      ]
    }
  } 
 }'

filter精确匹配  不做_score,会缓存查询结果

分词


curl -X POST 'http://localhost:9200/_analyze' -H 'Content-Type: application/json' -d ' 
 {
 	"analyzer":"standard",
 	"text":"hello world"
  }'

curl -X POST 'http://localhost:9200/_analyze' -H 'Content-Type: application/json' -d ' 
 {
 	"analyzer":"smartcn",
 	"text":"中国人"
  }'

IK 中文分词器 https://github.com/medcl/elasticsearch-analysis-ik


 
curl -X DELETE 'http://localhost:9200/user' 

curl -X PUT -H 'Content-Type: application/json' -i 'http://localhost:9200/user' -d '
{
   "settings":
   {
    "index":
    {
      "number_of_shards":2,
      "number_of_replicas":2    
    }   
   },
   "mappings":
   { 
       "properties":
      {
        "name":
        {
          "type":"text"        
        },
        "age":
        {
          "type":"integer"        
        },
        "email":
        {
          "type":"keyword"        
        },
        "hobby":
        {
        "type":"text" ,
        "analyzer":"smartcn"      
        }
      }   
   }
}'



curl -X POST 'http://localhost:9200/user/_bulk' -H 'Content-Type: application/json' -d ' 
{"index" : { "_index" : "user", "_id" : "1" } }
{"name": "李四","age":21,"email":"aa@abc.com","hobby":"喜欢足球，也爱篮球"}
{"index" : { "_index" : "user", "_id" : "2"} }
{"name": "王五","age":22,"email":"bb@abc.com" ,"hobby":"喜欢网球，也爱电脑"}

'


curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "match":
    {
      "hobby":"爱电脑"
    }
  } 
 }'
爱是一个词，电脑是一个词，会查出2条记录

使用"operator": "and" 默认是or

curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "match":
    {
      "hobby":
      {
      	"operator": "and",
      	"query":"电脑 网球"
      }
    }
  } 
 }'

百分比
curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "match":
    {
      "hobby":
      {
      	"minimum_should_match":"80%", 
      	"query":"电脑 网球"
      }
    }
  } 
 }'

用should得高分 ,must_not 不参与评分
curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "bool":
    {
      "must":
      {
        "match":{
          "hobby":"爱"
        }
      },
      "should":
      {
        "match":{
          "hobby":"电脑"
        }
      }            
    }
  } 
 }'



权重 boost 

curl -X POST 'http://localhost:9200/user/_search?pretty' -H 'Content-Type: application/json' -d ' 
 {
  "query":
  {
    "bool":
    {
      "must":
      {
        "match":{
          "hobby":"爱"
        }
      },
      "should":
      [
      {
        "match":{
          "hobby":{
          	"query":"电脑",
          	"boost":2
          }
        }
      },
      {
        "match":{
          "hobby":{
          	"query":"篮球",
          	"boost":8
          }
        }
      } 
      ]            
    }
  } 
 }'


<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-client</artifactId>
    <version>7.4.2</version>
</dependency>

elasticsearch-rest-client-7.4.2.jar 
	httpasyncclient-4.1.4.jar 是httpclient
	httpcore-nio-4.4.11.jar


public static void main(String[] args) throws Exception {
		RestClientBuilder builder =RestClient.builder(
				new HttpHost("rhel7",9200,"http")
//				,new HttpHost("rhel7kube",9200,"http")
			);
	builder.setFailureListener(new FailureListener() {
		@Override
		public void onFailure(Node node) {
			System.out.println("出错"+node);
		}
	});
	RestClient restClient=builder.build();
	//-----
	Request request=new Request("GET","/_cluster/state");
	request.addParameter("pretty", "true");
	Response response=restClient.performRequest(request);
	System.out.println("response status="+response.getStatusLine());
	System.out.println(EntityUtils.toString(response.getEntity()));
	
	//-----
	restClient.close();

}

public static void search(RestClient restClient) throws Exception
	{
		Request request=new Request("POST","/books/_search");
		request.addParameter("pretty", "true");
		//javax.json
		JsonBuilderFactory bf = Json.createBuilderFactory(null);
		//JsonArray array=bf.createArrayBuilder()
		JsonStructure struct =bf.createObjectBuilder()
            .add("query", bf.createObjectBuilder()
                    .add("match", bf.createObjectBuilder()
                            .add("title", "java")
                     )
                 ).build();
			
		request.setJsonEntity(struct.toString());  
		Response response=restClient.performRequest(request);
		System.out.println("response status="+response.getStatusLine());
		String jsonStr=EntityUtils.toString(response.getEntity());
		System.out.println(jsonStr);
		 
	}

<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.4.2</version>
</dependency>

elasticsearch-rest-high-level-client-7.4.2.jar
	elasticsearch-7.4.2.jar
	elasticsearch-x-content-7.4.2.jar
	lang-mustache-client-7.4.2.jar  //elasticsearch plugin
	rank-eval-client-7.4.2.jar		//elasticsearch plugin
	elasticsearch-core-7.4.2.jar
	hppc-0.8.1.jar
	queries/lucene-queries-8.3.0.jar
	
public static void main(String[] args) throws Exception 
{
	RestClientBuilder builder =RestClient.builder(
			new HttpHost("rhel7",9200,"http")
//				,new HttpHost("rhel7kube",9200,"http")
		); 
	RestHighLevelClient restClient= new RestHighLevelClient(builder);

	//	save(restClient);  
//	saveAsync(restClient);  
//	search(restClient);
//	getExist(restClient);
//	update(restClient); 
//	deleteDoc(restClient);
	
	restClient.close();
} 
public static void save(RestHighLevelClient restClient) throws Exception
{
	Map<String,Object> data=new HashMap<>();
	data.put("title", "java 书");
	data.put("price", 38.8);
	//会自建立索引
	IndexRequest request=new IndexRequest("books").id("1").source(data);
	 
	IndexResponse response=restClient.index(request,RequestOptions.DEFAULT);
	System.out.println("_id="+response.getId());
	System.out.println("index="+response.getIndex());
	System.out.println("shardInfo="+response.getShardInfo());
}
public static void saveAsync(RestHighLevelClient restClient) throws Exception
{
	Map<String,Object> data=new HashMap<>();
	data.put("title", "java Async书");
	data.put("price", 48.8);
	IndexRequest request=new IndexRequest("books").id("2").source(data);
	restClient.indexAsync(request,RequestOptions.DEFAULT,new ActionListener<IndexResponse>() {
		@Override
		public void onResponse(IndexResponse response) {
			System.out.println("_id="+response.getId());
			System.out.println("index="+response.getIndex());
			System.out.println("shardInfo="+response.getShardInfo());
		}
		@Override
		public void onFailure(Exception exception) {
			System.err.println("error="+exception);
		}
	});
}
public static void search(RestHighLevelClient restClient) throws Exception
{
	SearchRequest request=new SearchRequest("books");
	SearchSourceBuilder builder=new SearchSourceBuilder();
	builder.query(QueryBuilders.matchQuery("title", "java"));
	builder.from(1);
	builder.size(1);
	builder.timeout(new TimeValue(1,TimeUnit.SECONDS));
	request.source(builder);
	SearchResponse response=restClient.search(request, RequestOptions.DEFAULT);
	System.out.println("hit="+response.getHits().getTotalHits());
	SearchHits hits=response.getHits();
	for(SearchHit hit: hits)
	{
		System.out.println("json ="+hit.getSourceAsString());
	} 
}
public static void getExist(RestHighLevelClient restClient) throws Exception
{
	GetRequest request=new GetRequest("books","1");
	request.fetchSourceContext(new FetchSourceContext(false));
	boolean isExists=restClient.exists(request,  RequestOptions.DEFAULT);
	System.out.println("isExists ="+isExists);
}
public static void update(RestHighLevelClient restClient) throws Exception
{
	UpdateRequest request=new UpdateRequest("books","1");
	Map<String,Object> data=new HashMap<>();
	data.put("title", "java 书update");
//	data.put("price", 38.8);
	request.doc(data);
	UpdateResponse response=restClient.update(request,  RequestOptions.DEFAULT);
	System.out.println("update version="+response.getVersion());
}
public static void deleteDoc(RestHighLevelClient restClient) throws Exception
{
	DeleteRequest request=new DeleteRequest("books","2");
	restClient.delete( request,  RequestOptions.DEFAULT);
	System.out.println("delete _id=2");
}


 

================================Logstash
stash(隐藏)
https://www.elastic.co/guide/en/logstash/current/index.html

7.0 版本 只能用 JDK8 或 jdk11 两个版本(可OracleJDK 或  openJDK)
windows/linux是一个通用的包  有.zip, rpm 和 Docker 版本

数据采集不如 Beat产品，为了是可以 filter

Docker镜像
docker pull docker.elastic.co/kibana/kibana:6.6.0
docker pull docker.elastic.co/logstash/logstash:7.0.0


rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch 
增加如下文件
/etc/yum.repos.d/logstash.repo
	[logstash-6.x]
	name=Elastic repository for 6.x packages
	baseurl=https://artifacts.elastic.co/packages/6.x/yum
	gpgcheck=1
	gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
	enabled=1
	autorefresh=1
	type=rpm-md

sudo yum install logstash


做数据收集的，有实时管道能力，再推向Elastic Search
Input->filter->output


cd logstash-7.0.0/bin
logstash -e 'input { stdin { } } output { stdout {} }'  日志提示 API endpoint 监听 9600 端口 
 
---config/logstash-sample.conf
# comment

input { stdin { } }

filter {
 
}

output {
  elasticsearch { hosts => ["localhost:9200"] }
  stdout { codec => rubydebug }
}
这个配置会输出到elasticsearch和stdout上
output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    #index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}" 
    index => "my-sys-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "changeme"
  }
}
 这里的index的值(不能有大写)，要在kibina建立索引时用
 
---logstash.conf 的(为logback) 输入配置 
input {
    tcp {
        port => 4560
        codec => json_lines
    }
}

bin\logstash -f config\logstash-sample.conf --config.test_and_exit 验证配置文件正确性
bin\logstash -f config\logstash-sample.conf --config.reload.automatic 自动加载配置 

bin\logstash -f config\logstash-sample.conf  启动 
提示tcp输入监听 4560 (配置的)端口  ,提示 API endpoint监听 9600 端口  http://localhost:9600/ 有json返回，有版本信息


input 插件 
filter 插件 
output 插件 
codec 插件支持 https://www.elastic.co/guide/en/logstash/current/codec-plugins.html
	json
	avro (hadoop中的)
	protobuf(google跨语言的序列化协议)
 


=======================================Kibana
与elastic search一起工作，做分析
Kibana windows/linux 都是单独的包
cd  kibana-7.1.1-linux-x86_64/bin

 kibana.bat   启动   要求先启动 Elasticsearch 
 config/kibana.yml 
	#server.port: 5601 
	#server.host: "localhost"  #对外暴露服务的地址
	#elasticsearch.hosts: ["http://localhost:9200"]
	

 http://127.0.0.1:5601/   有界面
左侧菜单点management -> index management 显示已有的索引
左侧菜单点management -> index patterns -> 点 Create index pattern->选择索引名字，可以用*做通配->选择时间字段@timestamp
左侧菜单点Discover ->
	 首次时入 提示 Create index pattern,要先有数据写入后，才能建，即在logstash配置ouput组中index的值, my-sys-* 还要选择日期字段名 默认有@timestamp
 	 下拉可以选择my-sys-* 前缀开头的日志, 过滤条件可以选择日期范围，可选今天
 
	 索引 _index:值为pdpm-log-2018...  
		
    左侧的Available Fields 有很多字段，滑上点add按钮就会只显示选中的列，可以使用Save按钮把查询保存起来，下次使用时用Open按钮
    
左侧菜单点 Visulize ->Create New Visualization->选哪种图如柱状图->选择数据来源 可是在Discover中建立的Index Parttern的名字(filebeat,metribat的实时数据)
		 ->Metrics中默认是Y-axis Count,Bucekts中点+Add -> X-axis ->选一个如date histogram->点击播放按钮 ->可以Save保存

左侧菜单点Dashboard -> Create new dashboard ->提示This dashboard is empty 处点add链接 ->查询自己建立的Visulize-》可以拖动大小->可以Save保存
	再进来搜索到自己的Dashboard 就可以看了，也可点Edit做修改
 
 
 Lucene 查询: 搜索文本框输入 level:error && message:IAM LoginDone  可以查日志
 
 界面也支持Query DSL查询

 左侧菜单点 DevTools有Console  可以发送命令,带代码提示功能, 默认查全部的有 , 参数可有可无
 GET _search
{
  "query": {
    "match_all": {}
  }
}

# index a doc
PUT index/type/1
{
  "body": "here"
}

# and get it ...
GET index/type/1

查行数
 GET _count
{
  "query": {
    "match_all": {}
  }
}

==============================Beat
https://www.elastic.co/guide/en/beats/libbeat/current/index.html

可要用来收集日志写入Elastic Search 也可以写入LogStash
由 PacketBeat(网络),FileBeat(文件，取代logstash forwardder),MetricBeat (监控)，winlogBeat(windows日志),AuditBeat,HeartBeat,FunctionBeat

==================FileBeat
https://www.elastic.co/guide/en/beats/filebeat/7.4/index.html

docker 镜像的filebeat带setup命令

docker run  docker.elastic.co/beats/filebeat:7.4.2 \
	setup -E setup.kibana.host=kibana:5601 \
	-E output.elasticsearch.hosts=["elasticsearch:9200"]

---- vi console.yml
filebeat.inputs:
- type: stdin
  enable: true
setup.template.setttins:
  index.number_of_shards: 3
output.console:
  pretty: true
  enable: true
----- 
./filebeat -e -c console.yml  启动 控制台输入 hello加车，返回有 "message":"hello"
 -e, --e                    Log to stderr and disable syslog/file output


---- vi file-tag.yml
filebeat.inputs:
- type: log
  enable: true
  paths:
  - /var/log/nginx/*.log			#*/
tags: ["nginx"] #做分类标识
fields:  #自定义字段名和值
  env: sit
fields_under_root: true  #是否显示在要节点如为true,就没有fields节点，子节点向前提一级
setup.template.setttins:
  index.number_of_shards: 3
output.console:
  pretty: true
  enable: true
----- 
要保证 /var/log/nginx/*.log	 有读的权限  */

测试在返回的json有  
"tags": ["nginx"] 
#当fields_under_root: 为false默认值 
 "fields": {
    "env": "sit"
  },
 
#当fields_under_root: 为true
"env": "sit"

----filebeat.yml默认就有
output.elasticsearch: 
  hosts: ["localhost:9200"]
  
setup.template.settings:
  index.number_of_shards: 1

- type: log

修改type下的 enable为true 和 paths路径

----
./filebeat 启动 就默认读这个配置文件  日志记录在log/filebeat下

elasticsearch 下就有filebeat-<版本号>-<日期>-000001 (6位数字) 格式命名的index, 内容有log.file.path字段,log.offset字段，message字段

由两部分组成
 prospector(勘探者 ) 负责管理 harvester，为每一个文件启动一个harvester ，支持两种log,stdin . 
 harvester(收割机)	负责读单个文件/stdin （重命名也可跟踪）

每个harvester 要保存文件最后读取的offset,保存在 data/registry/filebeat/data.json 文件中，缓存在prospector的内存中


./filebeat -d "标识"  带调试信息

./filebeat  modules list
./filebeat  modules enable nginx  #启动
./filebeat  modules disable nginx  #启动

vi modules.d/nginx.yml 如果被禁用文件名以.disabled结尾
  可以配置
  access:
    enabled: true
    var.paths: /var/log/nginx/access.log*   #要以*结尾

  error:
    enabled: true
    var.paths: /var/log/nginx/error.log* 

--filebeat.yaml 中默认就有

filebeat.config.modules:
  # Glob pattern for configuration loading
  path: ${path.config}/modules.d/*.yml   #只读取到有效的文件*/ 
  reload.enabled: false 
  #reload.period: 10s

再测试前要把已经有的index删除，结果是解析后的，有多个字段

--filebeat.yaml 默认有
setup.kibana: 
  #host: "localhost:5601"
  
--
./filebeat -c filebeat.yml setup  #要求kibana运行中
./filebeat -e  -c filebeat.yml 启动 

进入kibana 的dashboard左侧按钮就有很多图可看, 过虑nginx，就有[Filebeat Nginx] Overview ECS

---可以配置output到redis,kafka中


==================MetricBeat  性能数据

Moudle 收集的对象如mysql,nginx
MetricSet 收集哪些性能数据  如cpu,memory,network,disk

 
--metricbeat.yaml 中默认就有
path: ${path.config}/modules.d/*.yml   #只读取到有效的文件 同filebeat */ 
#默认有一个system.yml ，文件名结尾没有disabled，cpu等，有间隔时间
output.elasticsearch: 
  hosts: ["localhost:9200"] 
---

./metricbeat -e  启动

生成在elasticsearch上的文件名格式类似filebeat,格式为metricbeat-7.4.2-2019.11.06-000001

./metricbeat  modules list



redis info命令


nginx -V 显示要带有  --with-http_stub_status_module
--nginx.conf
 location /nginx-status
 {
	stub_status on;
	access_log off; 
 }
--
http://127.0.0.1/nginx-status 返回数据
Active connections: 1   			#表示当前正在连接的个数
server accepts handled requests
 18 18 11 								
 #第1个server数字表示启动为止处理了多少个连接 ，第2个accepts数字表示启动为止多少次握手，第3个handled requests数字表示启动为止多少个请求
 #握手-连接 =0表示没有丢失
Reading: 0 Writing: 1 Waiting: 0  #当前


./metricbeat  modules enable nginx 

vi modules.d/nginx.yml
  #默认配置  
  hosts: ["http://127.0.0.1"] 
  #修改  
  server_status_path: "nginx-status"

--
elasticsearch 中 event.module 列找 nginx 的值(不是system)


--metricbeat.yaml 
setup.kibana: 
  #host: "localhost:5601"

./metricbeat  setup --dashboards  #要求kibana必须运行中
./metricbeat 启动
进入kibana 的dashboard左侧按钮就有很多图可看, 过虑nginx 就有[Metricbeat Nginx] Overview ECS
==================

 

