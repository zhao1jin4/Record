

C++语言编写

工具
官方的Mongo Compass (收费的,有windows,mac,ubuntu,redhat) 
	连接处点Fill in connection fields individually链接
	方便建立collection,index,查看,删除数据,Filter中写JSON,属性名可以是单引号
	但没有写命令语句的地方
	insert document写JSON属性名必须是双引号

Robo  Studio 3T  2018.2.5 (收费的)
Robo 3T  1.3.1 免费的zip(linux,mac)  支持MongoDB 4.0
NoSQL Manager for MongoDB-4.9.9.2 支持MongoDB 4.0  (只windows,下载freeware版本安装 )

#Toad Extension for Eclipse 带语法提示的,JS文件GBK,UTF8编码,有中文注释都有错???
#Toad for Eclipse-2.4.4 可以支持mongo-3.2版本,3.4版本打开js文件连接不成功,

MongoDB 页面管理工具: Rockmongo

TreeSoft 国产的 http://www.treesoft.cn/
	TreeDMS 页面管理工具 即可连接  MySQL 又可连接 MongoDB,  v2.2.7 连接mongo,如有数据某个字段是集合类型没办法显示 
	TreeNMS for Redis 
	


https://docs.mongodb.com/
https://docs.mongodb.com/manual/   最下方可下载离线版本 , The manual is also available as HTML tar.gz and EPUB

4.0 文档 epub 格式 ,Edge能打开,用Adobe Digital Editor-4.5.9(有Mac版本) 打开后,但不能复制代码,使用Calibre 就可以复制,还可前进后退,点右上角可以切换翻页和滚页模式
3.3 的html文档 有仿问google的js打不开

JSON (JavaScript Object Notation 标记法)
Mongo (humongous  巨大无比的)

用C++写的,支持分布式 (JSON,JS语言)  目前4.0 支持多文档事务
目前  MongoDB 4.0 add support for multi-document transactions

MongoDB 4.2  支持分布式事务(即多文档事务　 on sharded clusters and replica sets)
https://docs.mongodb.com/manual/release-notes/4.2/#distributed-transactions

Driver有 Scala C++  GO 版本

Win10 家庭版/win7旗靓版  安装 MongoDB 4.0.1 安取消选择install mongodb compass(一个图表界面工具,后面可以单独安装),否则安装一直卡着
安装后建立的服务是D:\Program\MongoDB\Server\4.0\bin\mongod.exe --config "D:\Program\MongoDB\Server\4.0\bin\mongod.cfg" --service


----- rockmongo
https://github.com/iwind/rockmongo   最后更新是2015年
使用PHP5开发的,不能放在php7的项目中不认mongo driver

http://pecl.php.net/package/mongo  有windows和linux版本
下载PHP5.6版本的(不支持PHP7) php_mongo-1.6.16-5.6-ts-vc11-x64.zip
  
解压 php_mongo.dll 放在php安装解压的ext目录中 
php.ini中增加  extension=php_mongo.dll

https://windows.php.net/downloads/releases/archives/
PHP5.6 最新版本为 php-5.6.39-Win32-VC11-x64.zip

可以进入页面,没有选择SHA-256,不支持Mongo4 ??? 除非修改源码，也应该可以支持PHP7 
extension=mongodb



-----------4.0 配置文件 mongod.cfg 可是YAML 格式配置变化,缩进不支持用tab,要用空格，也支持老的key=value式
storage:
  dbPath: D:\Program\MongoDB\Server\4.0\data
  journal:
    enabled: true
  
systemLog:
  destination: file
  logAppend: true
  path:  D:\Program\MongoDB\Server\4.0\log\mongod.log

net:
  port: 27017
  bindIp: 127.0.0.1

setParameter:
   enableLocalhostAuthBypass: false
   
replication:
   replSetName: "rs0" 
   
security:
  keyFile: <path-to-keyfile>   
// openssl rand -base64 756 > <path-to-keyfile>
// chmod 400 <path-to-keyfile>
-----------
 $./mongod --help
 
 启动单实例
$ mkdir /data/db  
$ ./mongod  默认找/data/db  


windows命令 md C:\mongodb\data\db 
启动服务 mongod.exe --dbpath c:\mongodb\data\db  --port 27017 //默认监听 27017  端口,如路径有空格 加" "


 
客户端工具
mongo.exe   --host localhost --port 27017 // 默认连接 27017 端口 
mongo   127.0.0.1:27017/admin   来建立管理员用户 
mongo -u zh -p 123  127.0.0.1:27017/reporting  ##/后面是数据名    默认不验证用户名要启动时加 --auth ,建立用户时不加--auth
-u [ --username ]     
-p [ --password ]  

>db.test.save( { a: 1 } )
>db.test.find()

mongo hello.js 

----windows 下安装服务

md c:\mongoData\log
md c:\mongoData\db

echo logpath=c:\mongoData\log\mongod.log> "c:\mongoData\mongod.cfg"
echo dbpath=c:\mongoData\db>> "c:\mongoData\mongod.cfg"
echo port=27017>>"c:\mongoData\mongod.cfg" 
echo auth=true >> "c:\mongoData\mongod.cfg"
--
logpath=c:\mongoData\log\mongod.log
dbpath=c:\mongoData\db
port=27017 
auth=true 


mongod --dbpath=c:\mongoData\db --logpath=c:\mongoData\log\db.log --auth   
mongod  --config=c:\mongoData\mongod.cfg
// mongod.exe 默认找 C:\data\db目录

mongod  --config=c:\mongoData\mongod.cfg  --install 会自动建立名字为MongoDB的服务 启动命令后加 --service
mongod  --remove  删除服务


 sc命令加服务,以管理员运行
sc  create MongoDB binPath= "\"C:\Program Files\MongoDB\Server\3.6\bin\mongod.exe\" --service --config=\"c:\mongoData\mongod.cfg\"" DisplayName= "MongoDB 3.6 Server" start= "auto"

net stop MongoDB   

sc  delete MongoDB  删除服务

注册表中 HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\MongoDB


----linux 下,只要放PATH环境变量 ,注意权限
 mongod-4.2.0 linux版本只有redhat7.0版本，tgz和rpm两种，tgz包可以运行在centOS7.6上 
 (tgz包运行在openSUSE-leap-15.1,报找不到libcrypto.so.10，而有libcrypto.so.1.1 即 libopenssl1_1)

 mongod --dbpath=/usr/local/mongdb/data --logpath=/usr/local/mongdb/db.log  --fork  启动   
 如要验证用户名,在--fork前加 --auth

mongod  --shutdown  --dbpath=/usr/local/mongdb/data  
也可以这样
mongod --config=./mongod.cfg  --shutdown  
最好不要使用kill -9 杀进程 ,可以用 pkill,killall


mongod --config=./mongod.cfg & 

use admin
db.shutdownServer(); 停服务
 


-----mongod.cfg  
logpath=c:\mongoData\log\mongod.log
dbpath=c:\mongoData\db
port=27017
auth=true
replSet=rs0
#fork=true 只linux才有

=========用户管理  
保存在 admin数据的 system.users集合中
use admin
show collections
db.system.users.find()//只在admin下有
show users;   //查看当前库下的用户

无密码进入mongoDB 默认是有root权限的
----建立管理员用户
use admin
db.createUser(
  {
    user: "siteUserAdmin",
    pwd: "password",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]   
  }
)

db.createUser(
    {
      user: "rootUser",
      pwd: "rootPass",
      roles: [
		 { role: "root", db: "admin" } 
	 ]
    }
) 

use log
db.createUser(
  {
    user: "logUserAdmin",
    pwd: "log",
    roles: [ { role: "userAdmin", db: "log" } ]  //可以createRole,grantRole
  }
)
db.auth("logUserAdmin","log")
db.createUser(
  {
    user: "log4j2",
    pwd: "log4j2",
    roles: [ { role: "readWrite", db: "log" } ]  //也可以创建这个DB的用户
  }
)

----建立 用户,建立时 配置文件关闭--auth
use reporting  
db.createUser(
    {
      user: "zh",
      pwd: "123",
      roles: [
	     { role: "readWrite", db: "reporting" } ,
		 { role: "readAnyDatabase", db: "admin" } //有权限可以show dbs 即listDatabases
	 ]
    }
) 


db.dropUser("zh", {w: "majority", wtimeout: 5000})
db.dropAllUsers() ; 删所有的用户及DB 


客户端 mongo --host 127.0.0.1 --port 27017  -u zh -p 123  reporting   
mongo --host 127.0.0.1 --port 27017  -u rootUser -p rootPass admin   
mongo --host 127.0.0.1 --port 27017  -u rootUser -p rootPass reporting   --authenticationDatabase  admin  
	--authenticationMechanism SCRAM-SHA-1   是3.x版本默认的
	--authenticationMechanism SCRAM-SHA-256  是4.0 新功能也是默认的 SCRAM是默认的 全称 Salted Challenge Response Authentication Mechanism  
	还有 x.509
验证密码,要服务端加--auth

在admin库下创建的帐号有仿问test库的权限,不能直接在test库做db.auth验证,要先切换到admin库db.auth后再切换test其它库(原因是建立用户的信息只在当前库中)

只有自己数据用户,才可以仿问自己的DB
db.auth("user","pass") 当前数据库切换登录为指定用户(数据库自身的用户才可)

非amdin 数据库用户不能执行 show dbs
db.system.users.find().pretty()  //可格式化良好的方式显示


 db.dropDatabase();  //删当前数据库

 一般数据角色 
 read  可以读非系统集合 system.js , ( system.namespaces 过时的用 listCollections )  , (system.indexes 过时的) 
 readWrite 可以写非系统集合
 dbAdmin,dbOwner,userAdmin,
 clusterAdmin,clusterManager,clusterMonitor,hostManager
 backup 可以使用 mongodump
 restore 可以使用 mongorestore 命令
 
 
 admin 数据库提供的所有角色,赋予用户所有数据库   readAnyDatabase   , readWriteAnyDatabase , userAdminAnyDatabase(可以建用户,没有对普通DB插入权限,可以show dbs)  , dbAdminAnyDatabase
 root 是 readWriteAnyDatabase , dbAdminAnyDatabase , userAdminAnyDatabase , clusterAdmin 的总合,不能仿问system.开头的集合
 内部角色 __system , 不要使用这个
===========================


db.help() 查所有命令,帮助 
db.stats()
db.xx.stats()
db.xx.help() 查集合所有的可用命令
db.eval("return mongod")

show dbs   //显示所有的数据库,显示有一个local
use test //切换数据库,如没有建立
db   //查当前是哪个数据库
show collections //显示db.xx.insert中的xx  ,别名 show tables
db.xx.find()

Date()　　显示当前的时间 显示GMT+8
new Date　　构建一个格林尼治时间   可以看到正好和Date()相差8小时
ISODate()　　也是格林尼治时间

new Date("<YYYY-mm-dd>") 
new Date("<YYYY-mm-ddTHH:MM:ss>")
new Date("<YYYY-mm-ddTHH:MM:ssZ>") 

database -> collection -> document

如果集合bios不存在,第一次insert会创建,格式为BSON,B为Binary(像JSON),有null,undefined,true,false,/x/式的正则
//db.bios 表示当前的db建立collection名为bios
db.bios.insert(
   {
     _id: 1,   //如不指定_id列,系统会自动生成一个ObjectId 
     name: { first: 'John', last: 'Backus' },
     birth: new Date('Dec 03, 1924'),
     contribs: [ 'Fortran', 'ALGOL', 'Backus-Naur Form', 'FP' ],
     awards: [
               {
                 award: 'W.W. McDowell Award',
                 year: 1967,
                 by: 'IEEE Computer Society'
               },
               {
                 award: 'National Medal of Science',
                 year: 1975,
                 by: 'National Science Foundation'
               }
             ]
   }
)
db.bios.insert({
       _id: 4,
       name: { first: 'Kristen', last: 'Nygaard' },
       birth: new Date('Aug 27, 1926'),
       death: new Date('Aug 10, 2002'),
       contribs: [ 'OOP', 'Simula' ],
       awards: [
                 {
                   award: 'Rosing Prize',
                   year: 1999,
                   by: 'Norwegian Data Association'
                 },
                 {
                   award: 'Turing Award',
                   year: 2001,
                   by: 'ACM'
                 },
                 {
                   award: 'IEEE John von Neumann Medal',
                   year: 2001,
                   by: 'IEEE'
                 }
               ]
     }
	 )
	 
db.inventory.insert(
   {
     item: "ABC1",
     details: {
        model: "14Q3",
        manufacturer: "XYZ Company"
     },
     stock: [ { size: "S", qty: 25 }, { size: "M", qty: 50 } ],
     category: "clothing"
   }
)
 db.inventory.update({item:"ABC1"},{$set:{category: "clothing2222222"}});
 db.inventory.update({item:"ABC1"},{$set:{"stock.0.size": "LL"}});  //修改数组指定位置 
 db.inventory.update({"stock.qty":25},{$set:{"stock.$":{size: "S1", qty: 30 }}});//按查找修改数组,$必须是找到的 
 db.inventory.update({"stock.qty":{$ne:25}},{$push:{"stock": {size: "S0", qty: 25} }}); //不存在,新增
 db.inventory.update({item:"ABC1"},{$push:{"stock": {size: "L", qty: 70} }});//push数组末尾加项
 db.inventory.update({item:"ABC1"},{$push:{"stock": {size: "S1", qty: 30} }});
 db.inventory.update({item:"ABC1"},{$pop:{"stock":1}}); //pop数组最后删除
 db.inventory.update({item:"ABC1"},{$pop:{"stock":-1}});//数组开头删
 db.inventory.update({item:"ABC1"},{$pull:{"stock":{qty: 30}}});  //删除数组中间 $pull

	 
db.bios.createIndex( { "birth": 1 }, { name: "birthday", unique: true } )//1,表示升序,-1表示降序,唯一索引
db.bios.createIndex( { _id: "hashed" } )  //hash索引
db.bios.createIndex(
   { birth: 1, name: 1 },//复合索引
   { partialFilterExpression: { age: { $gt: 5 } }   }//分区索引    
) 
db.bios.find({birth:{$gt:new Date('Aug 27, 1906')}}).explain()//执行计划
db.bios.getIndexes() //得到所有的索引

db.contacts.createIndex(
   { name: 1 },
   { partialFilterExpression: { name: { $exists: true } } }
)
db.locations.createIndex({w:"2d"})

 
db.bios.dropIndex("birth" )
db.bios.dropIndexes() 从MongoDB 4.2 开始 可以删所有非 _id 索引
 
 

db.setProfilingLevel(1);//0是关闭,2是全部,1是慢操作  ,也可以在启动mongod 时加 --profile 1
db.setProfilingLevel(1,1000);//超过1秒认为是慢操作  , 也可以在启动mongod 时加 --slowms  1000
db.getProfilingLevel();
db.system.profile.find()//慢的操作会被记录在这里,是一个capped的
db.system.profile.stats()




db.locations.find({w:{$near:[1,1]}})
db.locations.find({w:{$near:[1,1],$maxDistance:10}})
db.locations.find({w:{$geoWithin:{$box:[[0,0],[3,3]]}}})
db.locations.find({w:{$geoWithin:{$center:[[0,0],3]}}})
db.locations.find({w:{$geoWithin:{$polygon:[[0,0],[1,2],[8,8],[6,5]]}}})

db.bios.count( { a: 1 } )
db.bios.distinct ( { a: 1 } )
db.runCommand(
   {
     distinct: "stu",
     key: "name" 
	} )
	.values
	
	
db.runCommand( { listIndexes: "bios"} )  //查看集合索引 ,db.system.indexes.find() 已经过时了
db.runCommand( { listCollections: "reporting"} ) 
db.runCommand( { listDatabases: 1} ) 
db.listCommands()
db.bios.find()

//db.bios.explain()
save()和insert()类似

db.getCollection("stu").insert({"name":"lisi"}) //同 db.stu.insert({"name":"lisi"})
 
db.users.find( { age: { $gt: 18 } }, { name: 1, address: 1 } ).limit(5)  //要name,address列 

db.order.aggregation([ {$match:{status:"A"}},
						{$group :{_id:"$cust_id",total:{$sum:"$amount"}}}  //新列名_id,total,"$cust_id"是列名在""中以$开头
						])

db.stu_score.insert({stu_id:1,english:88,math:98,class_id:1})
db.stu_score.insert({stu_id:2,english:87,math:97,class_id:1})
db.stu_score.insert({stu_id:3,english:68,math:58,class_id:2})
db.stu_score.insert({stu_id:4,english:46,math:68,class_id:2})

db.stu_score.group(
   {
     key: { class_id: 1 },//group by 后面的
     cond: { english: { $gt: 60} }, //是where后  
     reduce: function ( curr, result ) {  //如何聚合,即实现聚合函数,current是每一行数据变一次,result是每一组(按key)变一次
	 	result.count++;
		result.total+=curr.english
		},
     initial: { count:0,total:0},//初始变量,为每组的result里的变量
	 finalize: function(result) {//每组最后完成时
                     result.english_avg = Math.round(result.total / result.count);//新增字段
              }
   }
)
//group()不支持分布式,在3.4版本后不推荐,建议使用aggregate() 和 $group来代替 或者 使用mapReduce()

db.stu_score.aggregate( [
	{
		$group:{_id:'$class_id' ,  count:{$sum:1} ,math_sum:{$sum:'$math'}  }   //要起名为_id才行
	 },
	 {
	 	$sort:{_id:1}
	  }	/*,
	  {
	 	$limit:1
	  } ,
	  {
	 	$skip:1
      }	*/
	])
 
//只一个,数组可不加
db.stu_score.aggregate( {
				$group:{_id:'$class_id' ,  count:{$sum:1}   }
		})
		
db.stu_score.aggregate( {
				$group:{_id:null ,  count:{$sum:1}   }
		})//全部总行数
 
 db.stu_score.aggregate( [
	{
	   $match:{math:{$gt:60}}//$match放$group 前,相当于where
	} ,
	{
	  $group:{_id:'$class_id' ,  count:{$sum:1}   }
	},
	{
	   $match:{count:{$gt:1}}//$match放$group 后,相当于having
	} 
	]) 
 
 db.stu_score.aggregate( [ 
	{
	  $group:{_id:'$class_id' ,  avg_math:{$avg:'$math'}   }
	} 
 ])
 
  db.stu_score.mapReduce( 
	function(){
		//前面可以做些处理
		emit(this.class_id,this.math)
	} , //map
	function (class_id,all_math){
		//return Array.sum(all_math)
		return Array.sum(all_math)/all_math.length //平均数 也可用  Array.avg(all_math)
	} , //reduce
	{
		query:{math:{$gt:60}}, //查询条件
		out:"math_scores"	//结果存入集合变量名
	}
 )
  db.math_scores.find()
  
  
db.orders.mapReduce(
	function(){
			
			emit(this.cust_id,this.amount)
	} ,//map 只要这两列数据
	function (key,values){return Array.sum(values)} ,//对相同的key(cust_id)的所有值(amount)做加法
	{
		query:{status:"A"}, //查询条件
		out:"order_totals"	//结果存入集合变量名
	}
	)						
//如做何两级的group by ????????

var c = db.bios.find()
while ( c.hasNext() ) printjson( c.next() )  //如无{},就要在一行中写

var c = db.bios.find()
printjson( c [1] )    //以0开始

db.bios.find().limit(3)
db.bios.findOne()  //只看第一个

for (var i = 1; i <= 25; i++) db.testData.insert( { x : i } )

function insertData(dbName, colName, num) {  //就是JavaScript

  var col = db.getSiblingDB(dbName).getCollection(colName);

  for (i = 0; i < num; i++) {
    col.insert({x:i});
  }

  print(col.count()); //可用于debug

}

insertData("test", "testData", 400)


db.collection.update(
                      <query>,
                      <update>, //可用 $set 加列(设置新值),$unset删列,$inc,
                      {
                        upsert: <Boolean>, //默认是false,当true时,如果查询条件没有document找到时建立新的document
                        multi: <Boolean>, //默认是false ,当true时, 更新多个 documents ,使用updateOne(),updateMany()		
                      }
                    )
					
			replaceOne()
			
db.inventory.insert([
					{type:"book",qty:20},
					{type:"book",qty:20}
					])
					
db.inventory.update(
                     { type : "book" },
                     { $inc : { qty : -1 } },   //$inc 是加法
                     { multi: true }
                   )
				   
db.inventory.update(
                     { type : "note" },
                     { $set : { qty : 20 } },  
                     { upsert: true }
                   )	   

 db.stu.update({name:"lisi"},{$set:{age:20}});  //$set: 后面也新加字段

db.collection.remove(query, justOne)

db.inventory.remove(
                     { type : "note" }, true
                   );
					
db.bios.find( { _id : 4} )   //过虑条件,多个有,分隔,数值可用$lt,$gt,$in,如qty: { $gt: 100 }, price: { $lt: 9.95 },  type: { $in: [ 'food', 'snacks' ] } 
			$or: [ { qty: { $gt: 100 } },{ price: { $lt: 9.95 } } ]
			{ tags: [ 'fruit', 'food', 'citrus' ] } //数组必须按顺序匹配
			{ tags: 'fruit' } //数组要包含元素
			 
			{ 'tags.0' : 'fruit' } //数组第一个元素要是
			 { 'memos.0.by': 'shipping' }//数组第一个元素的子元素by要为
			 { 'memos.by': 'shipping' } //数组至少一个素的子元素by要为
			 
			 {
				 'memos.memo': 'on time',
				 'memos.by': 'shipping'
			 }同
			  memos: {
					  $elemMatch: {
							memo : 'on time',
							by: 'shipping'
					   }
                     }
  .find({array:{$elemMatch:{$ne:null}}}) //查数组不为空的
  .find({array:{$size:0}})
  
db.collection.find().sort( { age: -1 } ); //负数是降序,正数表示升序
db.posts.find( {}, { comments: { $slice: 5 } } )//对comments数组只取前5个
db.posts.find( {}, { comments: { $slice: -5 } } )//对comments数组只取后5个
db.posts.find( {}, { comments: { $slice: [ 20, 10 ] } } )//对comments数组从头20个开始,长度10个
db.posts.find( {}, { comments: { $slice: [ -20, 10 ] } } )//对comments数组从尾20个开始,长度10个

db.collection.createIndex( { orderDate: 1, zipcode: -1 } )//建立复合索引,orderDate升序,
db.inventory.find( { type: "food", item:/^c/ },
                   { item: 1, _id: 0 } )//结果列中只显示item列,_id为0表示不显示
				   
db.collection.find().explain()

				   
$ref 在document中引用 collection的名字
$id 在document中 字段 _id 的值
$db	在document中引用 db 的名字 

cursor.count()  
cursor.explain() 分析索引的使用
cursor.hint() 强制使用索引
cursor.limit()  
cursor.next()  
cursor.skip() Returns a cursor that begins returning results only after passing or skipping a number of documents.
cursor.sort() 

 
db.stu.find 可以查看find的源码，目的可以看参数含义
db.stu.count();
db.stu.find().sort({name:-1}); //降序
db.stu.find().limit(5); 
db.stu.find().skip(3).limit(5); 
db.stu.find({},3,5) //limit, skip
db.stu.find().sort({Page:-1}).skip(3).limit(5).count(false);   //count参数默认false ，不看前面的条件，true看前面条件

db.stu.update({name:"lisi"} ,{post:[1,2,3,4,5]} )
db.stu.find({post:{$all:[3,5,6]}})  //数据中全部包含给定的
db.stu.find({post:{$exists:true}})  //存在post字段的 
db.stu.find({age:{$mod:[4,3]}})  // %4 取余结果为 3的
db.stu.find({post:{$in:[3,6]}})
db.stu.find({
		$or:[	
		     	{name:"lisi"} ,
		     	{age:{$gt:18}}
		     ]
	}) 
	
$nin   ,非的$in
 $nor  即过滤这些   !(|| ||)
db.stu.find({post:{$size:4}}) 
db.stu.find({name:/wang*/i})  正则表达式
db.stu.distinct("name");
db.stu.insert({name:"lisi1" ,skill:[{name:"math"},{name:"english"}]} ) ;
db.stu.find({"skill.name":"math"} ) ;
db.stu.find({ 
		skill:{$elemMatch:{name:"math"}}
	}) ;

db.stu.find({"$where":function ()
	{
		var skill=this.skill ;
	  	if(this.name=='lisi1' )
		{
		  	for(var i=0;i<skill.length;i++)
			{
				if(skill[i].name=='math')
					return true;
			}
		}
	}
}); // 降低性能 

var cur=db.stu.find().skip(3).limit(5); 
cur.forEach(function(x){print(tojson(x))}); 
while(cur.hasNext()){
	print(tojson(cur.next()));
}
db.stu.find({},{post:{$slice:2},_id:false}) // find第二个参数只是如何做显示，对数组，前2个，-1最后一个 

db.stu.find({},{post:{$slice:2}}).toArray().length

 db.stu.find({},{_id:false,name:true})//_id不显示,name显示
--
db.stu.update({name:"wang8"},{name:"wang1"})  //如原来有 age,只样改会丢失age
db.stu.insert({name:"wang88",post:[33,44]})
 
db.stu.update({name:"wang77"},{name:"wang88"})//默认更新不到,不新增
db.stu.update({name:"wang88"},{$set:{age:22}}) //这样不丢失原来
db.stu.update({name:"wang77"},{$set:{age:35}},{  upsert: true,  multi: false }) //找不到新增一条,name,age都有值
 
db.stu.update({name:"wang88"},{$unset:{age:true}})//删字段

db.stu.update({name:"wang88"},{$push:{ post:33 }})//数组增加元素,(可以数组里即有元素又有子数组,太灵活反而不好)
db.stu.update({name:"wang88"},{$push:{ post:34 }})
db.stu.update({name:"wang88"},{$pop:{ post:1 }})//数组删除最后一个元素 ,-1第一个
db.stu.update({name:"wang88"},{$pushAll:{ post:[44,55] }})  //可以有重复的值
db.stu.update({name:"wang88"},{$addToSet:{ post: 44  }})  //不加重复
db.stu.update(	{name:"wang88"},
				{ 
					$addToSet:	{ post: {$each: [11,22,33]} }
				}
			 )
db.stu.update({name:"wang88"},{$pull:{ post:55 }})//数组删除值为44的值	 

db.stu.update({name:"wang88"},{$rename:{  post :"postNew" }}) //修改字段名

var w=db.stu.findOne( {name:"lisi"} );
db.stu.save(w);//如果有更新,如果没有增加
 
//3.2新功能   replaceOne 不能使用$set  
db.stu.replaceOne({name:"wang77"},{name:"wang88"}) //默认有找不做新增
db.stu.replaceOne({name:"wang77"},{name:"wang88"},{ upsert: true } ) 

//3.2新功能   updateOne 必须用$set  
db.stu.updateOne({name:"wang77"},{$set:{age:35}},{ upsert: true })

//3.2新功能 updateMany   必须用$set  ,如匹配可更新多行,如upsert: true,未匹配加一行
db.stu.updateMany({name:"wang22"}, {$set:{age:35}},{ upsert: true }  )

db.stu.insert({name:"lisi",books:[{name:"java设计模式",type:"java"},{name:"easyUI教程",type:"js"}]}) 
db.stu.update({name:"lisi","books.type":"js"},{$set:{"books.$.name":"easyUI新版教程" }}) //$引用索引到的数组下标,key有.要用""

res=db.runCommand(
	{ findAndModify : "stu" ,
		query:{"books.type": "java"  },
		update:{$set:{"books.$.name":"java设计模式新版" }}
	})  //有于返回修改前的老记录,只能修改一条记录

res.value

也可以用 
res=db.stu.findAndModify ({  
		query:{"name": "lisi"},
		update:{$set:{"auther":"Frank" }} 
	})

	
	
var cur=db.stu.find({ }).snapshot(); //使用 快照 ,可以使用高级查询
//如果不做快照,在遍历时 加大了元素,可能会打乱元素顺序,有可能一个元素遍历两次,有的无素没有遍历,使用快照就不会了


db.stu.insert({name:"wang22" ,age:null} ) ;
db.stu.insert({name:"wang33" } ) ;

db.stu.find({age:null})//也包括不存在age 的值
db.stu.find({age:{$type:10}}) // 10为null的



db.createCollection("col2") 
db.col2.drop() //删集合,DDL

db.createCollection("col2",{capped:true,size:20*1024}) //固定最多 20K 个字节,当达到20K时老的会删,像日志,capped:true必须设置size
db.createCollection("col2",{capped:true,size:20*1024,max:20 }) //固定最多 20 个 document

db.col2.stats();//看capped为true

db.col2.find() //显示最开始的数据消失了,默认按照插入顺序取
db.col2.find().sort({"$natural":1})//正序
db.col2.find().sort({"$natural":-1})//反序
db.runCommand({ convertToCapped:"stu",size:10*1024 });//把现有集合转换为固定的



每个document的修改操作是原子

$isolated  在　sharded clusters中无效　
(是对多个document写时,只在完成或者错误时，但不能回滚，其它进程才可仿问,可防止其它进程交叉写第一个document)
即对多个document加锁

db.products.remove( { qty: { $gt: 20 }, $isolated: 1 } )
db.foo.update(
    { status : "A" , $isolated : 1 },
    { $inc : { count : 1 } },
    { multi: true }
)


------Write Concern
因写是立即返回的,不知道是否真的成功
db.runCommand( { getLastError: 1} )  //为了知道上次写是否真的成功了
db.runCommand( { getLastError: 0} ) 一样的
 //返回有   "err" : null, "ok" : 1

======GridFS  存放大文件
   
	
mongofiles --hostname 127.0.0.1 --port 27017 -d testDB list				//linux格式 

mongofiles /host 10.1.5.226 /port 27017 /db reporting /u zh /p 123 list  //windows格式	
mongofiles /host 127.0.0.1 /port 27017 /db testDB  put help.txt 		//把这个文件传到服务器上
mongofiles /host 127.0.0.1 /port 27017 /db testDB list  				//查看所有文件
mongofiles /host 127.0.0.1 /port 27017 /db testDB list  h				//文件名开头的

mongofiles /host 127.0.0.1 /port 27017 /db testDB  get help.txt
mongofiles /host 127.0.0.1 /port 27017 /db testDB  delete help.txt 	//删服务上的文件
mongofiles /host 127.0.0.1 /port 27017 /db testDB  search  lp			//文件名只要包含字符就可以了

db.fs.files.find()
db.fs.chunks.find() ,是真正的文件内容,是binary 的字符编码,像是Base64


----------导入 导出

导出JSON格式
./bin/mongoexport -u zh -p 123 -h  10.1.5.226 -d reporting -c bios -o bios.json  //linux格式 
./bin/mongoimport -u zh -p 123 -h  10.1.5.226 -d reporting -c bios   bios.json  //linux格式 
 
 CSV格式
./bin/mongoexport -u zh -p 123 -h  10.1.5.226 -d reporting -c data  --type csv   -o /tmp/data_utf8.csv -f id,accountNO,accountName,comment,enabled
./bin/mongoimport -u zh -p 123 -h  10.1.5.226 -d reporting -c data  --type csv  --file /tmp/data_utf8.csv --headerline 


导出BSON
./bin/mongodump    -u zh -p 123 -h  10.1.5.226 -d reporting -c bios -o dump_dir    //会生成 dump_dir/reporting/bios.bson , bios.metadata.json
./bin/mongorestore -u zh -p 123 -h  10.1.5.226 -d reporting -c bios  dump_dir/reporting/bios.bson   

----  性能监控  未测试成功
mongosniff 3.4 被删除,使用 mongoreplay (目前linux版本有这个命令,windows版本没有)

可以把一个mongo实例录制下来,再播放到另一个实例中

mongoreplay record -i eth0 -e "port 27017" -p ~/recordings/playback      //报错 panic: runtime error: invalid memory address or nil pointer dereference

如果基于现有的pcap文件继续录
tcpdump -i eth0 -n "port 27017" -w traffic.pcap
mongoreplay record -f traffic.pcap -p ~/recordings/playback

mongoreplay play -p ~/recordings/playback --report ~/reports/replay_stats.json --host mongodb://192.168.0.4:27018

mongostat
mongotop

----
======= wiredTiger 3.0新版本的存储引擎
md  c:\mongodb\data\wiredTigerdb
mongod --storageEngine wiredTiger --dbpath c:\mongodb\data\wiredTigerdb   //默认引擎是 MMAPv1,在mongodb-4.2中被删除了，用 WiredTiger Storage Engine 
3.2 版本开始 wiredTiger 是默认引擎
--wiredTigerCacheSizeGB
 
--wiredTigerCollectionBlockCompressor 默认snappy,可none,zlib


===========================Replication Set 自动failover
oplog (operations log)
启动前用 replication.oplogSizeMB设置大小 

md c:\mongodb\data\rs0_one
mongod --replSet "rs0"  --dbpath c:\mongodb\data\rs0_one  --port  37017 --smallfiles --oplogSize 128 //rs0是名字  默认端口 27017, --smallfiles为开发机
//也可在linux,windows间做,以指定--keyFile 长度6 到 1024 字付,may only contain characters in the base64 set
	//设置权限chmod 600,两个实例用不同的keyFile文件
再建立一个
md c:\mongodb\data\rs0_two
mongod --replSet "rs0"  --dbpath c:\mongodb\data\rs0_two   --port 37018  --smallfiles --oplogSize 128
 
md c:\mongodb\data\rs0_three
mongod --replSet "rs0"  --dbpath c:\mongodb\data\rs0_three   --port 37019  --smallfiles --oplogSize 128


mongo --port  37017	启动后用客户端连上
>rsconf = {
	_id: "rs0",
	members: [{
		_id: 0,
		host: "localhost:37017"  //可为127.0.0.1:37017
	}]
}
>rs.initiate( rsconf )  	//后提示符变为rs0:PRIMARY>  如为 rs0:OTHER>要再按回车刷新

复杂点为
rsconf = {
	_id: "rs0",
	members: [{
		_id: 0,
		host: "localhost:37017" //可为127.0.0.1:37017
		,priority:1
	}
	,
	{  
		_id: 1,
		host: "localhost:37018" //可为127.0.0.1:37017
		,priority:2
	}
	]
}
rs.initiate( rsconf )
//>rs.initiate() 			//使用默认配置

rs0:PRIMARY> rs.conf()     //显示只一个, 如启动加 --keyFile ,不能执行(show collections/dbs也不行),报not authorized on test to execute command?????
rs0:PRIMARY> rs.status()	//  "stateStr" : "PRIMARY", 显示哪个primary,哪个SECONDARY ,临时可能为STARTUP2
rs0:PRIMARY> rs.add("localhost:37018")  //可为127.0.0.1:37017  
rs0:PRIMARY> rs.add("localhost:37019")
rs0:PRIMARY> rs.conf()		//变两个

//如连37018 提示符变为 rs0:SECONDARY>

rs0:PRIMARY> rs.remove("localhost:37018")  //必须在primary中做 
//删的节点,最好在线,如果删时不在线,那么被删节点再启动时会一直找其它节点,   被删节点提示符变为rs0:OTHER>

重新配置
cfg = rs.conf()
cfg.members[1].host = "localhost:37018"   
rs.reconfig(cfg)
 
rs0:SECONDARY> 从节点没有读权限,不能执行 show dbs,要先执行 rs.slaveOk() 或者用  db.getMongo().setSlaveOk() 就可以了

 
三个实例 测试停primary,secondary 会转换为primary  , 要等一会查看

===========================Replication Set  ( master/slave　)

主节点
mongod --master --dbpath c:\mongodb\data\masterdb   	//监听 27017
	
	use local
	show collections 有oplog.$main
	rs.printReplicationInfo()  
	db.adminCommand({shutdown : 1, force : true})  停master和所有的slave,     测试好像slave不能停???
从节点
md  c:\mongodb\data\slavedb 
mongod --slave --source  127.0.0.1:27017 --dbpath c:\mongodb\data\slavedb  --port 27018 //--source指定主的IP:port

	use local
	db 
	db.sources.find()  				可看master是谁
	rs.printSlaveReplicationInfo()　　可以看到source,即master是谁,Oplog大小 


测试在master中insert,slave中可find()到
db.isMaster()
===========================shard
为增大吞吐量,可同时向多台机器写
sh.status()  显示是否share启动了
sh.help()
Sharded Cluster的三个组件
1.Shards			是collection的子集数据,可以是单个 mongod进程,在生产中, 所有的 shards 是 replica sets.
2.Config Servers	存元数据,映射 chunks 到  shards (数据分片设置).在生产中应该部署三个Config Servers实例,每个实例在单独机器上,测试环境可以一台机器上
3.Routing Instances 是mongos实例,把应用的读写请求转到Shards


Primary Shard 存所有未shard的collection,如要修改使用movePrimary 命令 


启动三个 config servers ,默认端口 27019 ,默认目录/data/configdb
md c:\mongodb\data\configdb
md c:\mongodb\data\configdb2
md c:\mongodb\data\configdb3
mongod --replSet "rscfg"  --configsvr --dbpath c:\mongodb\data\configdb --port 27019   
mongod --replSet "rscfg" --configsvr --dbpath c:\mongodb\data\configdb2 --port 27029
mongod --replSet "rscfg" --configsvr --dbpath c:\mongodb\data\configdb3 --port 27039

要有节点配置为primary
mongo --port 27019
>rsconf = {
	_id: "rscfg",
	members: [{
		_id: 0,
		host: "localhost:27019"
	}]
}
>rs.initiate( rsconf )
>rs.add("localhost:27029")
>rs.add("localhost:27039")


启动路由实例(mongos)   默认 mongos 实例运行在 27017 端口 ( --port 27017 )
//mongos 在 3.2 版本设值变化为 <replicasetName>/<config1>,<config2>
mongos --configdb rscfg/localhost:27019,localhost:27029,localhost:27039

mongo --host localhost --port 27017  //连接到 运行mongos的 进程 提示符是mongos>
使用replicationSet 格式   <replicationSet的名>/<primary主机>:<端口>
	mongos> sh.addShard( "rs0/localhost:37017" )   //这些启动要加  --shardsvr 
	mongos> sh.addShard( "rs0/localhost:37018" ) 
	mongos> sh.addShard( "rs0/localhost:37019" ) 
	md c:\mongodb\data\rs1_one
	mongod --replSet "rs1"  --shardsvr --dbpath c:\mongodb\data\rs1_one   --port 47017  --smallfiles --oplogSize 128
	md c:\mongodb\data\rs1_two
	mongod --replSet "rs1"  --shardsvr --dbpath c:\mongodb\data\rs1_two   --port 47018  --smallfiles --oplogSize 128

	mongo --port  47017	启动后用客户端连上
		>rsconf = {
			_id: "rs1",
			members: [{
				_id: 0,
				host: "localhost:47017"
			}]
		}
		>rs.initiate( rsconf )  
		>rs.add("localhost:47018") 

	mongos> sh.addShard( "rs1/localhost:47017" ) 
	mongos> sh.addShard( "rs1/localhost:47018" ) 

不使用replicationSet的形式 ,加 --shardsvr  启动  
	mongod  --shardsvr --dbpath c:\mongodb\data\shardDB  --port  47017 
	mongod  --shardsvr --dbpath c:\mongodb\data\shardDB1  --port  47018 
	mongos> sh.addShard( "localhost:47017" )  //对单机的  
	mongos> sh.addShard( "localhost:47018" )  

mongos> sh.enableSharding("test")  //test是db名,或者  db.runCommand( { enableSharding: "test" } )
 
mongos> sh.shardCollection("test.bios", { "_id": "hashed" })  //db.collection,shard-key-pattern ,使用hash
//应该一个collection只可shard一个

连接路由插入数据测试,也可以单独连接两个 shardsvr 查看数据

db.printShardingStatus() 或者  sh.status() 




如在一个chunk 大小超设置值 ,会被split两个,如果chunk的数据在一个shard上过多,会被balance到其它的shard上
db.settings.save( { _id:"chunksize", value:64 } )  //单位MB,默认是64MB,
db.settings.find() //写了之后才可查到,存放在configsvr中



sh.status()
预先分好10个片,当i的值在100,200... 处分片,防止当增加机器时,移动分片产生大量的IO 
for (var i=1;i<=10;i++){ 
	sh.splitAt({"test.bios", { "_id":i*100  }})
}

sh.status()  // chunks:没有变化?????
 
 
for (var i=1;i<=500;i++){ 
	 db.bios.insert({ "_id":i , name:"lisi"+i })
}
 ==========Transaction 4.0 新功能 多文档事务
 
 Multi-document transactions are available for replica sets only. 
 Transactions for sharded clusters are scheduled for MongoDB 4.2
 
  maxTransactionLockRequestTimeoutMillis 默认 5
 db.adminCommand( { setParameter: 1, maxTransactionLockRequestTimeoutMillis: 20 } )
 mongod --setParameter maxTransactionLockRequestTimeoutMillis=20  
 
 
 
 
 
 
 
 
