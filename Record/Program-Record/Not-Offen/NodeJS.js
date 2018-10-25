
http://nodejs.gamesys.net/node-js

下载msi文件,安装后把bin放入Path中
如下.exe就一个文件 

node 命令
>console.log("Hello world")
node hello.js 命令

//分号也可没有
console.log(console)//有warn,error
console.log(process.version);
console.log(process.platform);//什么系统平台
console.log(process.env); //系统环境变量
console.log(process.argv);  //命令行参数

var args = process.argv
for(var i=0;i<args.length;i++) {
  console.log("arg%d : %s",i,args[i]) //0是node命令 1是js文件
}
console.dir(process)//显示process对象,有很多信息



NPM (像linux的 yum  )
npm install mongodb
npm install -g mongodb //安装为一个全局使用的模块,
默认安装在 C:\Users\zh\AppData\Roaming\npm\node_modules 
C:\Users\zh\AppData\Roaming\npm-cache  目录很多文件
//设置NODE_PATH环境变量为

npm init   会提示回车生成package.json文件
npm install --save browser-sync   //--save存在package.json文件中  安装 browser-sync,是一个服务器监控修改生效
 package.json 修改 "scripts": 区
 "scripts": {
    "dev": "browser-sync start --server --files *.*"
  },
  
npm run dev   会启动 http://localhost:3000

//----未测试
npm update packname
npm uninstall packname 
npm find socket
npm install -g socket.io   //-g 全局使用的模块,如没有当前目录下安装模块
npm info express


package.json文件中有 dependencies字段,表示依赖的模块

通过node_modules目录来加载
 
var myModule1 = require('./mymodeule');

//---circle.js 自定义模块
var PI = 3.14;
exports.perimeter = function (r){   //exports对象加暴露的方法
    return 2 * PI * r;
};
exports.area = function (r){
    return PI * r * r;
};

其它地方使用
var c = require('./circle.js'); //在同一目录下OK
console.dir(c.perimeter(5));

events.EventEmitter 
emitter.addListener(event, listener) 或者 emitter.on(event, listener)
emitter.once(event, listener) //只响应一次
emitter.removeListener(event, listener)
emitter.removeAllListeners([event])
emitter.setMaxListeners(n)
emitter.emit(event, [arg1], [arg2], [...])//产生事件
error事件如不做处理默认显示错误退出

var events = require('events');
var emitter = new events.EventEmitter();
emitter.on('buyTicket',function(from,to){
  console.log('Buy ticket: ' + 'From ' + from + ' To ' + to);
});
emitter.on('error',function(err){
  console.log('Error:' + err);
});
emitter.emit('buyTicket','Shenzhen','Changsha');

多个监听器按照绑定时的顺序依次触发,如果前面的事件抛出一个错误，那么后面的事件将不会被触发

//--继承 inherits call 
var util = require("util"); //导入util模块
var events = require("events"); //导入events模块
function MyStream() {
    events.EventEmitter.call(this);//call方法使得MyStream对象继承了EventEmitter对象上的方法
}
util.inherits(MyStream, events.EventEmitter);//使用inherits方法
MyStream.prototype.write = function(data) {//MyStream对象扩展了一个write方法，它可以使用emit触发事件
    this.emit("data", data);
}
var stream = new MyStream();//构建一个stream对象
console.log(stream instanceof events.EventEmitter);//判断stream对象是不是EventEmitter对象的实例，这里为true
console.log(MyStream.super_ === events.EventEmitter);//MyStream的父类是不是EventEmitter，这里为true
stream.on("data", function(data) {//stream调用了继承过来的on方法，定义了一个'data'事件并安装了一个监听器
    console.log('Received data: "' + data + '"');
})
stream.write("It works!"); //调用stream对象的write方法，结果：Received data: "It works!"

//---继承 类调用 call 
function Animal(name){     
    this.name = name;     
    this.showName = function(){     
       console.log(this.name); 
    }     
}     
 
function Cat(name){   
    Animal.call(this, name);//在Cat构造函数内部，使用Animal.call,这样Cat构造函数的实例就拥有了Animal类中定义的方法
}     
 
var cat = new Cat("Black Cat");    
cat.showName();//Cat本身没有showName方法，但通过call继承了Animal中的showName方法
 
//--继承 方法调用 call
function Animal(){
    this.name = "Animal";
    this.showName = function(){
         console.log(this.name);
    }
}
 
function Cat(){
    this.name = "Cat";
}
var animal = new Animal();
var cat = new Cat();
animal.showName.call(cat,",");//在animal对象的showName方法上应用call，并用cat对象替换了animal中this对象，

//--继承加自己的方法
util = require('util');
var EventEmitter = require('events').EventEmitter;
 
var MyClass = function(){
//创建一个空的MyClass对象
}
util.inherits(MyClass,EventEmitter);
//让MyClass继承EventEmitter
 
MyClass.prototype.someMethod = function(arg1,arg2){
//扩展一个私有的someMethod方法,这个方法必须在inherits之后，否则会被覆盖
  this.emit("event1",arg1,arg2);
};
 
var myObj = new MyClass();
myObj.on('event1', function(arg1,arg2) {
  console.log('emit event: arg1->%s and arg2->%s',arg1,arg2);
});
 
myObj.someMethod('one','two');
 
//-----path
var path = require('path');

var nor = path.normalize('F:\\wamp\\www\\Node\\.');
console.log(nor);//输出：F:\wamp\www\Node

var jo = path.join('F:\\','wamp','\\www','\\Node','\\.');
console.log(jo);//输出：F:\wamp\www\Node

var res = path.resolve('F:\\wamp\\www\\Node','./Test');
console.log(res);//输出：F:\wamp\www\Node\Test

var res = path.resolve('www','Node');//以当前目录所在的路径为起点
console.log(res);

var dir = path.dirname('F:\\wamp\\www\\Node\\normalize.js');
console.log(dir);//输出：F:\wamp\www\Node

var base = path.basename('F:\\wamp\\www\\Node\\normalize.js');
console.log(base);//输出：normalize.js

var base = path.basename('F:\\wamp\\www\\Node\\normalize.js','.js');
console.log(base);//输出：normalize

var ext = path.extname('F:\\wamp\\www\\Node\\normalize.js');
console.log(ext);//输出：.js

是否存在文件系统上，在这里使用path.existsSync方法，这个方法是synchronousv即同步版本下的，
而异步版本里的要使用fs.exists方法



new Buffer(str, [encoding])//可支持utf8,base64,hex,binary
 
//-Buffer
var str = 'node.js,您好';
var len = str.length;
buf = new Buffer(len);
buf.write(str,'utf8');
console.log("-------Buffer中文内容:"+buf.toString('utf8',0,len)); //GBK的windows中文不显示???


//-----File System  回调
var fs = require('fs');
fs.open('c:/boot.ini','r',function(err,fd){ 
  if(err) { throw err }
  //...
});

//read
var fs = require('fs');
fs.open('d:/temp/test.txt','r',function(err,fd)
{
	if(err) 
		 throw err;
	var readBuff = new Buffer(1024),
	bufferOffset = 0,
	bufferLen = readBuff.length,
	position = 0;
	fs.read(fd,readBuff,bufferOffset,bufferLen,position,function(err,bytesRead,buffer)
	{
		if(err) 
			throw err;
		if(bytesRead)
		    console.log(buffer.toString('utf8',0,bytesRead));
		fs.close(fd,function(err)
		{
		  console.log('Now,close file...' + [fd]);
		});
	});  
});

//write
fs.open('d:/temp/test.txt','a',function(err,fd)
{
	if(err)
		throw err;
	cTime = new Date();
	cTime = cTime.getFullYear()+"-"+cTime.getMonth();
	var wBuff = new Buffer('Start log: ' + cTime + "\r\n");
	buffPos = 0;
	buffLen = wBuff.length;
	filePos = 0;

	fs.write(fd,wBuff,buffPos,buffLen,filePos,function(err,wbytes,data)
	{
		if(err)
			throw err;
		console.log('wrote ' + wbytes + ' bytes');
		fs.close(fd);
	});
});


var fs = require('fs');
fs.readFile('d:/temp/test.txt',function(err,data)
{
  if(err) 
	throw err;
 
  console.log(data.toString());
});

//-----File System 事件

可读流支持的事件有四种：data，end,error,close。

var fs = require('fs');
var opt = {
  flags:'r',
  encoding:'utf8',
  fd:null,
  mode:0666,
  bufferSize:64 * 1024,
  start:0,
  end:99
};
st = fs.createReadStream('d:/temp/test.txt',opt);
//st.pause();//暂停data事件的触发
//st.resume();//恢复data事件
var text = "";
st.on('data',function(data){
  text += data;
});
st.on('end',function(close){
  console.log("reading 99 bytes:\n" + text);
});
st.on('error',function(error){
  console.log('An error occurred:' + error);
});
st.on('close',function(){
  console.log('The file descriptor has been closed.');
});
st.on('open',function(fd){
  console.log('Current fd number: ' + fd);
});
 

var fs = require('fs');
var stream = fs.createWriteStream(__dirname + '/out.txt');
var str = 'i=[';
for (var i=0;i<10;i++){
  stream.write(str + i + ']\r\n');
}
stream.end('this is the end\n');//写最后一行
stream.on('finish', function() {
  console.error('all writes are now complete.');
});
 
 
//---pipe
var fs = require('fs');
var writeStream = fs.createWriteStream('./out.txt',{ flags:'w'});
var readStream = fs.createReadStream('d:/temp/test.txt');
 
readStream.pipe(writeStream);
writeStream.on('close',function(){
    console.log('All done!');
});

writeStream.on('error',function(error){
  console.log('An error occurred:' + error);
});
 
//-----Http
//---client
var http = require('http');

var options = {
  hostname: 'www.google.com',
  port: 80,
  path: '/upload',
  method: 'POST'
};

var req = http.request(options, function(res) {
  console.log('STATUS: ' + res.statusCode);
  console.log('HEADERS: ' + JSON.stringify(res.headers)); //JSON
  res.setEncoding('utf8');
  res.on('data', function (chunk) {
    console.log('BODY: ' + chunk);
  });
});

req.on('error', function(e) {
  console.log('problem with request: ' + e.message);
});

// write data to request body
req.write('data\n');
req.write('data\n');
req.end();

//---server
var http = require('http');

var server = http.createServer(function (req, res)
{
  // req is an http.IncomingMessage, which is a Readable Stream
  // res is an http.ServerResponse, which is a Writable Stream
  var body = '';
  req.setEncoding('utf8');
  req.on('data', function (chunk) {
    body += chunk;
  })
  req.on('end', function () {
    try {
      var data = JSON.parse(body);
    } catch (er) {
      res.statusCode = 400;
      return res.end('error: ' + er.message);
    }
    res.write(typeof data);
    res.end();
  })
})
server.listen(1337);

// $ curl localhost:1337 -d '{}'
// object
// $ curl localhost:1337 -d '"foo"'
// string


//-----Net
//--server
var net = require('net');
var server = net.createServer(function(c) { //'connection' listener
  console.log('server connected');
  c.on('end', function() {
    console.log('server disconnected');
  });
  c.write('hello\r\n');
  c.pipe(c);
});
server.listen(8124, function() { //'listening' listener
  console.log('server bound');
});

//--client
var net = require('net');
var client = net.connect({port: 8124},
    function() { //'connect' listener
  console.log('client connected');
  client.write('world!\r\n');
});
client.on('data', function(data) {
  console.log(data.toString());
  client.end();
});
client.on('end', function() {
  console.log('client disconnected');
});

//-----nodeJs mongoDB模块
npm install -g mongodb //安装为一个全局使用的模块,设置NODE_PATH环境变量为C:\Users\zh\AppData\Roaming\npm\node_modules
//linux 在	/usr/local/lib/node_modules/brunch/node_modules/
//  		~/.npm/
npm install mongodb   //在线安装到当前目录,当前js文件同目录下要有node_modules\mongodb 
https://github.com/mongodb/node-mongodb-native  文档



var MongoClient = require('mongodb').MongoClient
 ,format = require('util').format;    

MongoClient.connect('mongodb://127.0.0.1:27017/test', function(err, db) 
{
	if(err)
		throw err;

	 var collection = db.collection('test_insert');
	 collection.insert({a:2}, function(err, docs)
	 {
		   collection.count(function(err, count) 
			{
			   console.log(format("count = %s", count));
			});
		
		   // Locate all the entries using find
		   collection.find().toArray(function(err, results) 
			{
			   console.dir(results);
 			   //db.close();
		   });      
	 });
	 
	 
	 //----
	 var idString = '52281c394b20e9c400000001';
	  var ObjectID = require('mongodb').ObjectID;
	  collection.find({_id: new ObjectID(idString)}).toArray(function(err, results) 
				{
		   console.dir(results);
			//db.close();
	   });      
	 
	 //---
	  collection.update({name: 'lisi'}, {$set: {name: 'wang'}}, {upsert:true,multi:true }, function(err)
		{
	      if (err) 
	    	  console.warn(err.message);
	      else
	    	  console.log('successfully updated');
	    });
	  //---第二个参数是排序
	  collection.findAndModify({a: 88}, [['_id','asc']], {$set: {hi: 'there'}}, {upsert:false}, function(err, object) {
	      if (err) console.warn(err.message);
	      else console.dir(object);  // undefined if no matching object exists.
	    });
	   
});
//-----JSON
//-----cluster
//-----客户端用 webSocket


