http://nodejs.cn/api/   Node.js 中文网  v12.16.1

下载msi文件,安装。也可下载.zip包解压 (最好下载LTS版本  Long Time Support)
LTS版本为 12.16.1 LTS  最后支持windows  7 的版本
LTS版本为 14.16.1 LTS ,14 版本不支持 windows 7 

Mac的pkg安装包默认安装npm,node命令到/usr/local/bin下，下载目录在/usr/local/lib/node_modules 要root权限

把bin放入Path中 如下.exe就一个文件 


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

默认的npm install (npm i)很慢，可以使用国内的
npm config set registry https://registry.npm.taobao.org 
//npm config set registry https://registry.npmjs.org

会在~/.npmrc 文件中写入
	registry=https://registry.npm.taobao.org

如npm install是报 failed, reason: selft signed certificate in certificate chain
执行 npm config set strict-ssl false 

npm config get registry  验证
npm install -g react react-dom   就很快了

如npm安装不行，可用 yarn 
	https://yarnpkg.com  
	https://classic.yarnpkg.com/en/docs/install 下载
	yarn install
	缓存目录 %USERPROFILE%\AppData\Local\Yarn\Cache
	

也可安装淘宝镜像提供的cnpm工具
npm install -g cnpm --registry=https://registry.npm.taobao.org
cnpm install [name] 

npm install -g nrm (NPM Registry manager)
nrm ls
nrm use taobao (上面显示的别名)
nrm use npm (默认的 https://registry.npmjs.org)




也可用bower工具安装  npm install -g bower
bowner install react
npm install --save  express@4.15.2 安装指定版本


cnpm 可以搭建公司私有npm服务 https://cnpmjs.org/ 国产的
Sinopia 搭建私有的npm仓库

npm info <express>


NPM (像linux的 yum  )
npm install mongodb
npm install -g mongodb //安装为一个全局使用的模块,
//设置NODE_PATH环境变量为
默认安装在 %HOMEPATH%\AppData\Roaming\npm\node_modules 
解压式目录 D:\Program\nodejs\node_modules\npm\node_modules 


npm init   会提示回车(如入口js 默认inex.js，修改后就有"main": "hello.js") 生成package.json文件 
npm install --save browser-sync   //--save存在package.json文件中  安装 browser-sync,是一个服务器监控修改生效
 package.json 修改 "scripts": 区
 "scripts": {
    "dev": "browser-sync start --server --files *.*"
  },
  
npm run dev   会启动 http://localhost:3000
 
package.json文件中有 dependencies字段,表示依赖的模块, 用 npm install 就会安装所有的依赖
有时提示	npm audit fix
通过node_modules目录来加载
如源码中没有node_modules目录 使用npm install (读package.json文件)来下载,可加参数 --ignore-scripts
npm cache clean --force  可能没有完成删除目录 ~\AppData\Roaming\npm_cache\_cache目录 (npm cache verify 提示的)
删除package-lock.json
npm install --verbose 有每个文件下载日志


 chrome 调试node
  node --inspect test-debug.js 监听9229 端口
  chrome地址栏输入 chrome://inspect/ -> 可以看到运行的文件，点击进入
    -> Source标签-> +add Folder to workspace 选择源码目录，就可以调试了

vs code 调试node (react 的npm start )
  my-app/node_modules/react-scripts/bin/react-scripts.js 看脚本找下面文件
  my-app/node_modules/react-scripts/scripts/start.js  vscode工具打开文件 -> 打断点 -> debug视图中点debug按钮
  vscode工具 生成.vscode/launch.json（没有这个.vscode目录及文件也可debug）
   文件内容有
   "type": "node",
   "program": "${workspaceFolder}/start"

npm -l 所有命令及使用 显示install有别名i
npm help list 显示man手册
npm run 是 npm run-script 的别名
npm list 显示所有依赖树
npm list sockjs 可以直接看哪个依赖了sockjs

npm list -g 显示树结构， 已经安装的包 ,list 别名有ls,ll, la
npm help install  
	显示install有别名i 
	选项 -D, --save-dev 会安装devDependencies中包(没有单独的--save)
		-P --save-prod 会安装dependencies中包
		
npm config set registry https://registry.npmjs.org
npm search swagger  索引包search别名是find

npm info swagger-ui  显示包信息

npx create-react-app my-app
npx 会自动查找当前依赖包中的可执行文件，如果找不到，就会去 PATH 里找。如果依然找不到，就会帮你安装！
 
//----未测试
npm update packname
npm uninstall packname  
npm install -g socket.io   //-g 全局使用的模块,如没有当前目录下安装模块

----------nexus 做 npm 私服
https://help.sonatype.com/repomanager3
https://help.sonatype.com/repomanager3/formats/npm-registry

还支持Docker, APT，YUM (linux的),P2(eclipse的）,go仓库,pypi(pip)仓库


上方第二个设置(Server Administration and Configuration)按钮 -> 左侧菜单 Repository->Repositories-> Create Repository 按钮->
npm(hosted) 可用于上传本私服
npm(proxy) 可配置公网 Remote Storage 填入	 	 
	https://registry.npmjs.org
npm(group) 上面建的两个放一起(最好hosted类型的放最上)
	如建的名为 npm-group 生成路径为 http://127.0.0.1:8081/repository/npm-group/
 
npm config set registry http://127.0.0.1:8081/repository/npm-group/
--vi ~/.npmrc 
registry = http://127.0.0.1:8081/repository/npm-group/

npm --loglevel info install grunt  测试成功，可以当前服务没有的东西从代理上下载，服务上就有东西了

---上传包到私服
配置登录
npm login --registry=http://127.0.0.1:8081/repository/npm-group/  会在 ~/.npmrc 文件中生成token

也可用
echo -n 'admin:admin123' | openssl base64
生成的base64编码 放入 ~/.npmrc 文件中
email=you@example.com
always-auth=true
_auth=YWRtaW46YWRtaW4xMjM= 


npm publish --registry http://127.0.0.1:8081/repository/npm-group/

或者 package.json  文件中配置
"publishConfig" : {
  "registry" : "http://localhost:8081/repository/npm-group/"
}

npm adduser 也是在~/.npmrc中生成token
npm login
npm whoami  

------- Sinopia 搭建私有的npm仓库
https://www.npmjs.com/package/sinopia

会建立config.yaml 
$ npm install -g sinopia
$ sinopia
  
$ npm set registry http://localhost:4873/
 
# if you use HTTPS, add an appropriate CA information 
# ("null" means get CA list from OS) 
$ npm set ca null

npm adduser --registry http://localhost:4873/ 
提示输入用户名密码，邮箱后就可以用建立的用户登录了
没有东西  提示 npm publish



Sinopia 有 docker image


------- 
var myModule1 = require('./mymodeule');

//---circle.js 自定义模块
module.exports = function(name, age) {
    this.name = name;
    this.age = age;
    this.about = function() {
        console.log(this.name +' is '+ this.age +' years old');
    };
};//如加module.exports=function 下面的exports.perimeter= 和 module.exports.name =就不能被使用了
module.exports.name = function() {
    console.log('the name function');
};
//exports是module.exports的别名，如果一个新的值被赋值给 exports，它就不再绑定到 module.exports
var PI = 3.14;
exports.perimeter = function (r){  
    return 2 * PI * r;
};
exports.area = function (r){
    return PI * r * r;
};

//---其它地方使用
var c = require('./circle.js'); //在同一目录下OK

var myObject=new c("lisi",25);//针对 module.exports = 
console.log(myObject.about());
//---上下二先一
//console.log(c.perimeter(5));//针对 exports.perimeter =
//console.log(c.name());//针对module.exports.name =


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
================================ express 一个node的web框架
http://expressjs.com/

npm init
npm install express --save
目前版本 4.17.1


const express = require('express')
const app = express()

app.use(express.static('public')) ;//public目录放html,js,css图片等
app.use("/static",express.static(__dirname+'/static'));//指定路径对应的目录


// 自定义跨域中间件
var allowCors = function(req, res, next) {
  res.header('Access-Control-Allow-Origin', req.headers.origin);
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  res.header('Access-Control-Allow-Credentials','true');
  next();
};
app.use(allowCors);//使用跨域中间件

//方式二
app.all('*',function(req, res, next) {
  res.header('Access-Control-Allow-Origin', req.headers.origin);//不能多个，如用xhr.withCredentials=true不使用*
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type,mycors,Cookie');
  res.header('Access-Control-Allow-Credentials','true');
  if(req.method=='OPTIONS') 
	res.sendStatus(200);
  else
	next();
});


//--跨域也可使用cors库
https://expressjs.com/en/resources/middleware/cors.html
npm install cors --save
//import cors form "cors"
var cors = require('cors')
app.use(cors({
	credentials:true,
	orign:["http://127.0.0.1:3000","http://localhost:3000"],
	methods["GET","POST"],
	allowedHeaders["Content-Type","mycors","Cookie"]
	}))

//---
app.get('/', (req, res) => res.send('Hello World!'))


const port = 3000
app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))


//--cookie-parser 
npm install cookie-parser --save

var cookieParser=require("cookie-parser");
app.use(cookieParser()); 
--- npm install body-parser  

var express = require('express')
//获取模块
var bodyParser = require('body-parser')

var app = express()

// 创建 application/json 解析
var jsonParser = bodyParser.json()

// 创建 application/x-www-form-urlencoded 解析
var urlencodedParser = bodyParser.urlencoded({ extended: false })

// POST /login 获取 URL编码的请求体
app.post('/login', urlencodedParser, function (req, res) {
  if (!req.body) return res.sendStatus(400)
  res.send('welcome, ' + req.body.username)
})

// POST /api/users 获取 JSON 编码的请求体
app.post('/api/users', jsonParser, function (req, res) { 
  console.log(req.body)
  if (!req.body)
	  return res.sendStatus(200)
  return res.sendStatus(400)
});


app.listen(3000);
//---express 结合body-parser 示例  cookie-parser

const express = require('express')
const app = express()

var bodyParser = require('body-parser')
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }))
 
 
app.get('/', (req, res) => res.send('Hello World!'))

app.post('/news', (req, res) => { 
	console.log(req.body);//请求要加content-type : application/json
	console.log("request cookie loginId:"+req.cookies.loginId);//是cookie-parser的作用
	res.cookie("loginId","abc");
	res.json({"msg":"from express"});
})

const port = 3000
app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))
================================falcor 基于express,有angular前端版本配套
https://netflix.github.io/falcor/

mkdir falcor-app-server
cd falcor-app-server
npm init

npm install falcor-router --save
npm install express --save
npm install falcor-express --save

----index.js
var falcorExpress = require('falcor-express');
var Router = require('falcor-router');

var express = require('express');
var app = express();

//这个是虚拟的JSON资源
app.use('/model.json', falcorExpress.dataSourceRoute(function (req, res) {
  // create a Virtual JSON resource with single key ("greeting")
  return new Router([
    {
      // match a request for the key "greeting"    
      route: "greeting",
      // respond with a PathValue with the value of "Hello World."
      get: function() {
        return {path:["greeting"], value: "Hello World"};
      }
    }
  ]);
}));

// serve static files from current directory
app.use(express.static(__dirname + '/'));

var server = app.listen(3000);

----index.html 
下载解压包中的 falcor/dist/falcor.browser.min.js 放 和 index.html 同级目录

<html>
  <head>
    <script src="falcor.browser.min.js"></script>
    <script>
      var model = new falcor.Model({source: new falcor.HttpDataSource('/model.json') });
      
      // retrieve the "greeting" key from the root of the Virtual JSON resource
      model.get("greeting").
        then(function(response) {
          document.write(response.json.greeting);
        });
    </script>
  </head>
  <body>
  </body>
</html>
---
node index.js 后访问 http://localhost:3000/index.html 显示 Hello World

请求为 http://127.0.0.1:3000/model.json?paths=[["greeting"]]&method=get
	
响应为 {"jsonGraph":{"greeting":"Hello World"}}

================================stompit
https://www.npmjs.com/package/stompit

//activeMQ 的新版本 Artemis,实现了 STOMP=Simple (or Streaming) Text Orientated Message Protocol
node.js 版本的STOMP客户端

npm i stompit

//示例代码开两个客户端可以连接artermis做测试成功
const stompit = require('stompit');
 
const connectOptions = {
  'host': 'localhost',
  'port': 61613,
  'connectHeaders':{
    'host': '/',
    'login': 'input',
    'passcode': 'input',
    'heart-beat': '5000,5000'
  }
};
 
stompit.connect(connectOptions, function(error, client) {
  
  if (error) {
    console.log('connect error ' + error.message);
    return;
  }
  
  const sendHeaders = {
    'destination': '/queue/test',
    'content-type': 'text/plain'
  };
  
  const frame = client.send(sendHeaders);
  frame.write('hello');
  frame.end();
  
  const subscribeHeaders = {
    'destination': '/queue/test',
    'ack': 'client-individual'
  };
  
  client.subscribe(subscribeHeaders, function(error, message) {
    
    if (error) {
      console.log('subscribe error ' + error.message);
      return;
    }
    
    message.readString('utf-8', function(error, body) {
      
      if (error) {
        console.log('read message error ' + error.message);
        return;
      }
      
      console.log('received message: ' + body);
      
      client.ack(message);
      
      client.disconnect();
    });
  });
});


================================zeromq
https://www.npmjs.com/package/zeromq

npm i zeromq


---producer.js
const zmq = require("zeromq")
 
async function run() {
  const sock = new zmq.Push
 
  await sock.bind("tcp://127.0.0.1:3000")
  console.log("Producer bound to port 3000")
 
  while (true) {
    await sock.send("some work")
    await new Promise(resolve => setTimeout(resolve, 500))
  }
}
 
run()

---worker.js
const zmq = require("zeromq")
 
async function run() {
  const sock = new zmq.Pull
 
  sock.connect("tcp://127.0.0.1:3000")
  console.log("Worker connected to port 3000")
 
  for await (const [msg] of sock) {
    console.log("work: %s", msg.toString())
  }
}
 
run()
 
 
---publisher.js
const zmq = require("zeromq")
 
async function run() {
  const sock = new zmq.Publisher
 
  await sock.bind("tcp://127.0.0.1:3000")
  console.log("Publisher bound to port 3000")
 
  while (true) {
    console.log("sending a multipart message envelope")
    await sock.send(["kitty cats", "meow!"])
    await new Promise(resolve => setTimeout(resolve, 500))
  }
}
 
run()

---subscriber.js
const zmq = require("zeromq")
 
async function run() {
  const sock = new zmq.Subscriber
 
  sock.connect("tcp://127.0.0.1:3000")
  sock.subscribe("kitty cats")
  console.log("Subscriber connected to port 3000")
 
  for await (const [topic, msg] of sock) {
    console.log("received a message related to:", topic, "containing message:", msg)
  }
}
 
run()
=================redis
https://www.npmjs.com/package/redis

npm install redis



const redis = require("redis");
//const client = redis.createClient();
const client = redis.createClient({
	"host": '127.0.0.1',
	"port": 6379,
	"password": "123",
	"db": 1
});  //enable TLS use  rediss://
 
client.on("error", function(error) {
  console.error(error);
});
 
client.set("key", "value", redis.print);
client.get("key", redis.print);


client.set("foo", "bar"); 
client.get("foo", function(err, reply) {
  // reply is null when the key is missing
  console.log(reply);
});

=================socket.io
是一个WebSocket库,包括了客户端的js和服务器端的nodejs
数据推送到客户端

https://socket.io/
https://github.com/socketio/socket.io

npm install socket.io

---node 端的js 
const io = require('socket.io')();
io.on('connection', client => { console.log('client connected') });
io.listen(3000);

---node 端的js 使用htp

const server = require('http').createServer();
const io = require('socket.io')(server);
io.on('connection', client => {
  client.on('event', data => { console.log('client event') });
  client.on('disconnect', () => { console.log('client disconnect') });
});
server.listen(3000);

---node 端的js 使用express
npm install express

const app = require('express')();
const server = require('http').createServer(app);
const io = require('socket.io')(server);


app.get('/', (req, res) => {
  //res.send('<h1>Hello world</h1>');
   res.sendFile(__dirname + '/index.html');
});
app.get('/client/socket.io.js', (req, res) => { 
	 res.sendFile(__dirname + '/client/socket.io.js');
  });
app.get('/client/jquery-3.5.0.min.js', (req, res) => { 
	res.sendFile(__dirname + '/client/jquery-3.5.0.min.js');
 });
io.on('connection', (socket) => { 
	 console.log('a user connected');  
	 socket.on('disconnect', () => {
		console.log('user disconnected');
	  });  
	  socket.on('chat message', (msg) => {//接收客户端的事件
		console.log('message: ' + msg); 
		io.emit('chat message', msg);//向所有客户端发送事件
	  });
}); 
server.listen(3000);

----客户端 
可以在 node_modules/socket.io-client/dist/socket.io.js 中找到
<script src="client/socket.io.js"></script> 
<script src="client/jquery-3.5.0.min.js"></script>
<script>
  
$(function () {
    var socket = io();
    $('form').submit(function(e) {
      e.preventDefault(); // prevents page reloading
      socket.emit('chat message', $('#m').val()); //向服务端发送事件带参数
      $('#m').val('');
      return false;
	});
	socket.on('chat message', function(msg){ //接受服务端发来的事件
      $('#messages').append($('<li>').text(msg));
	});
	
  });
</script>
  </head>
  <body>
    <ul id="messages"></ul>
    <form action="">
      <input id="m" autocomplete="off" /><button>Send</button>
    </form>
  </body>
</html>
会有 socket.io/?EIO=3&transport=polling  和  socket.io/?EIO=3&transport=websocket 这样的请求 

//-----cluster 
const cluster = require('cluster');
const http = require('http');
const numCPUs = require('os').cpus().length;

if (cluster.isMaster) {
  console.log(`主进程 ${process.pid} 正在运行`);

  // 衍生工作进程。
  for (let i = 0; i < numCPUs; i++) {
    cluster.fork();
  }

  cluster.on('exit', (worker, code, signal) => {
    console.log(`工作进程 ${worker.process.pid} 已退出`);
  });
} else {
  // 工作进程可以共享任何 TCP 连接。
  // 在本例子中，共享的是 HTTP 服务器。
  http.createServer((req, res) => {
	res.setHeader('content-type', 'text/html;charset=UTF-8');
	res.writeHead(200); 
    res.end('你好世界\n');
  }).listen(8000);

  console.log(`工作进程 ${process.pid} 已启动`);
}
---------bluebird
http://bluebirdjs.com/docs/getting-started.html
可用于浏览器的Promise增强， Bluebird 支持取消

npm install bluebird
var Promise = require("bluebird");
import * as Promise from "bluebird";
示例见JS_ThirdLib

//development
Promise.config({
    longStackTraces: true,
    warnings: true  
})
/*
//production
Promise.config({
    longStackTraces: false,
    warnings: false
})
*/

----cron
https://www.npmjs.com/package/cron

npm install cron

var CronJob = require('cron').CronJob;
var job = new CronJob('* * * * * *', function() {
  console.log('You will see this message every second');
}, null, true, 'America/Los_Angeles');
job.start();


同Java的Quartz，但不同于linux是没有秒的
Seconds: 0-59
Minutes: 0-59
Hours: 0-23
Day of Month: 1-31
Months: 0-11 (Jan-Dec)
Day of Week: 0-6 (Sun-Sat)

---- winstone 日志  
https://www.npmjs.com/package/winston
npm install winston

const winston = require('winston');
 
const logger = winston.createLogger({
  level: 'info',
  format: winston.format.json(),
  defaultMeta: { service: 'user-service' },
  transports: [
    //
    // - Write all logs with level `error` and below to `error.log`
    // - Write all logs with level `info` and below to `combined.log`
    //
    new winston.transports.File({ filename: 'error.log', level: 'error' }),
    new winston.transports.File({ filename: 'combined.log' }),
  ],
});
 
//
// If we're not in production then log to the `console` with the format:
// `${info.level}: ${info.message} JSON.stringify({ ...rest }) `
//
if (process.env.NODE_ENV !== 'production') {
  logger.add(new winston.transports.Console({
    format: winston.format.simple(),
  }));
}
/*
可用的日志级别
const levels = { 
  error: 0,
  warn: 1,
  info: 2,
  http: 3,
  verbose: 4,
  debug: 5,
  silly: 6
};
*/
 
logger.log({
  level: 'info',
  message: 'Hello distributed log files!'
});
 
logger.info('Hello again distributed logs');
logger.error('Hello error logs');

---日志滚文件
npm install winston-daily-rotate-file
https://www.npmjs.com/package/winston-daily-rotate-file

 var winston = require('winston');
  require('winston-daily-rotate-file');
 
  var transport = new (winston.transports.DailyRotateFile)({
    filename: 'application-%DATE%.log',
    datePattern: 'YYYY-MM-DD-HH',
    zippedArchive: true,
    maxSize: '20m',
    maxFiles: '14d'
  }); //最多保留14天的
 
  transport.on('rotate', function(oldFilename, newFilename) {
    // do something fun
  });
 
  var logger = winston.createLogger({
    transports: [
      transport
    ]
  });
 
  logger.info('Hello World!');


----jsonwebtoken
https://www.npmjs.com/package/jsonwebtoken

npm install jsonwebtoken
 
require('jsonwebtoken')

//Synchronous Sign with default (HMAC SHA256)

var jwt = require('jsonwebtoken');
var token = jwt.sign({ foo: 'bar' }, 'shhhhh');
 

----lodash
https://lodash.com
npm i  lodash --save
有集合的一些操作方法，像filter,map 
示例见JS_ThirdLib

---moment-timezone 时区库
https://momentjs.com/timezone/
npm i moment-timezone  --save 
虽然有浏览器版本，但测试不行，只能node

var moment = require('moment-timezone');

var zone = "America/Los_Angeles";
var res=moment.tz('2013-06-01T00:00:00+00:00', zone).format(); // 2013-05-31T17:00:00-07:00
console.log(res);

res=moment('2013-06-01T00:00:00+00:00',"America/Los_Angeles");
console.log(res.format());

var timestamp = 1403454068850,
date = new Date(timestamp);
res=moment.tz(timestamp, "America/Los_Angeles").format(); // 2014-06-22T09:21:08-07:00
console.log(res); 


-----Electron
 Electron( JavaScript、HTML 和 CSS 构建桌面应用程序的框架),内部有使用Chrome相关的东西
 Medis 有使用这个库
https://github.com/electron/electron

npm init
npm install --save-dev electron 会下载 electron-v13.1.2-win32-x64.zip
	包中有libGLESv2.dll
	libEGL.dll   EGL 是 OpenGL ES 渲染 API 和本地窗口系统(native platform window system)之间的一个中间接口层
	
release 页有 chromedriver下载




