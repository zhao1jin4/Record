 
最早版本v0.3.0 在 Jul 2, 2013 发布,
v15.0.0 (在Apr 8, 2016 发布)是在v0.14.8的下一版本的命名

react 官方中文翻译网  https://doc.react-china.org/
 
http://www.runoob.com/react/react-tutorial.html
https://reactjs.org/
https://github.com/facebook/react/releases   目前版本是 16.13.1

Flow 由 Facebook 开发,用于替代PropTypes  

React_Flux实现Facebook
https://facebook.github.io/flux/
http://www.ruanyifeng.com/blog/2016/01/flux.html
redux 是作者受  Flux 的影响



EasyUI for React 
ExtReact 收费(EXTJS)
https://reacteasyui.github.io/ 国产的 组件样式依赖 Bootstrap 

Material UI  适用移动端,实现google android 的Material风格(Storybook生成Material UI主题 ) 

react-bootstrap(显示Bootstrap 3 进入页是 4)   
https://github.com/react-bootstrap/react-bootstrap 有链接到  https://react-bootstrap.github.io/

reactstrap: Simple Bootstrap 4 components built with tether
https://reactstrap.github.io/

Semantic-ui:  组件全
https://react.semantic-ui.com/
 
 
Grommet:  https://grommet.io/

material-components-web
Belle
Elemental UI  饿了么的
Amaze UI React  国产 
Ant Design 蚂蚁金服的

react-toolbox 
https://react.rocks/example/react-toolbox 有示例

----------------------
默认的npm install很慢，可以使用国内的
npm config set registry https://registry.npm.taobao.org
//npm config set registry http://registry.cnpmjs.org 
npm config get registry  验证
npm install -g react react-dom   就很快了

也可安装淘宝镜像提供的cnpm工具
npm install -g cnpm --registry=https://registry.npm.taobao.org
cnpm install [name] 
注意：不要使用 cnpm！cnpm 安装的模块路径比较奇怪，packager 不能正常识别！

也可用bower工具安装  npm install -g bower
bowner install react

也可以使用yarn 包管理器   https://yarnpkg.com/zh-Hans/

npm install -g react react-dom 
npm install  react react-dom  --save 依赖保存到package.json (可选 npm install  react-scripts --save 要200M)

npm init  会提示回车生成 package.json文件

---browser-sync
npm install --save browser-sync   //--save存在package.json文件中  安装 browser-sync,是一个服务器监控修改生效
 package.json 修改 "scripts": 区，如没有在根的{下增加
	 "scripts": {
		"dev": "browser-sync start --server --files *.*"
	  },
npm run dev  会启动 http://localhost:3000
---

package.json文件中有 dependencies字段,表示依赖的模块, 用 npm install 就会安装所有的依赖


https://facebook.github.io/create-react-app/docs/proxying-api-requests-in-development

package.json 下增加如下设置代理，npm start就可debug,但必须是使用index.html中用ajax请求才行，如在浏览器无论输入什么地址还是进入index.html页？？
"proxy":"http://localhost:8080/S_ReactEasyUI",

vscode 调试node (react 的npm start )
  my-app/node_modules/react-scripts/bin/react-scripts.js 看脚本找下面文件
  my-app/node_modules/react-scripts/scripts/start.js  vscode工具打开文件  -> 打断点 -> debug视图中点debug按钮
  vscode工具 生成.vscode/launch.json（没有这个.vscode目录及文件也可debug）
   文件内容有
   "type": "node",
   "program": "${workspaceFolder}/start"

vscode 调试 react (create-react-app方式)
	安装 Extenstion为Debugger for Chrome
	Debug视图->上方的设置按钮(或下拉add configuration,chrome lanuch)会多一个Chrome的选择,生成launch.json
	修改"url": "http://localhost:3000" 和 npm start的地址一致,再npm start ,就可vscode打断点,chrome中操作
	launch.json
	{
		"type": "chrome",
		"request": "launch",
		"name": "Launch Chrome against localhost",
		"url": "http://localhost:3000",
		"webRoot": "${workspaceFolder}"
	}
	
	
React Developer Tools (firefox ,chrome 插件)
如独立安装  npm install -g react-devtools  再 react-devtools 显示界面，监听8097
 

---create-react-app  可以debug,firefox比chrome在调试jsx效果更好
//<node解压目录>\node_modules\create-react-app
npm install -g create-react-app
npm update -g create-react-app

create-react-app my-app  //也会下载react, react-dom(这两个8M多), and react-scripts(这个很大,3个共200M多)...很多东西 

//也可用 npx create-react-app react-node (不能使用大写字母),会安装cra-template

实际上是执行 node.exe <node解压目录>\node_modules\create-react-app\index.js .

cd my-app    //生成的node_modules目录非常大,100多M，2万多个文件 不适用 , 基于 Webpack + ES6

npm run build ( package.json有定义 "build": "react-scripts build", ) 生成 build 目录，生产级
	提示npm install -g serve
	    serve -s build
			http://localhost:5000
npm start  //开发级,看报错要ping通`hostname` 打开 http://localhost:3000  

生成的
node_modules/...
package.json
  中有 "main": "index.js" 
生成的index.js代码就有 
	import './index.css';
	import App from './App';

public/index.html
public/manifest.json

rm -f src/*  //原来有 index.js  index.css*/
src/下建立文件 index.css  
src/下建立文件 index.js  //index.html中没有引用index.js 
// 运行时有引用/static/js/xx.js 哪来的(webpack ?) ,npm run build 后才有引用
	import React from 'react';
	import ReactDOM from 'react-dom';
	import './index.css';
	
Visual Studio Code 使用create-react-app my-app  找到文件位置
 %HOMEPATH%\AppData\Local\Microsoft\TypeScript\3.6\node_modules\@types\react-dom
										   项目目录\node_modules\@types\react
Webstorm(IntellijIDEA不能有此功能)也是使用create-react-app my-app  但是用yarn做的
									   
项目目录下 npm start (start定义在package.json的 "scripts": 下   "start": "react-scripts start" ) 
如要修改端口 package.json 中scripts中的start 命令 修改如下
   "scripts": {
		"start": "set PORT=4000 && react-scripts start"
   }
	
npm run build ( package.json有定义 "build": "react-scripts build", )生成 build 目录，生产级
有HTML,JS压缩式 (Webpack工具) ,要用web服务式运行上下文为/ 才行
 
和jQuery一起使用			 
npm install -g jquery
npm install --save jquery
import $ from  'jquery'

------ 手工引用js    chrome,firefox不能debug
不能从node_modules复制.js  因有require语法
node_modules\react\cjs下有 react.production.min.js , react.development.js
node_modules\react-dom\cjs下有 react-dom.production.min.js , react-dom.development.js
 
script引入的js 最新为 16.13.0
https://www.bootcdn.cn/react/ 
https://cdn.bootcss.com/react/16.13.0/umd/react.development.js
https://cdn.bootcss.com/react/16.13.0/umd/react.production.min.js

https://www.bootcdn.cn/react-dom/
https://cdn.bootcss.com/react-dom/16.13.0/umd/react-dom.development.js
https://cdn.bootcss.com/react-dom/16.13.0/umd/react-dom.production.min.js

unpkg.com 官方使用的 
https://unpkg.com/react@16.13.1/umd/react.development.js
https://unpkg.com/react-dom@16.13.1/umd/react-dom.development.js

https://unpkg.com/react@16.13.1/umd/react.production.min.js
https://unpkg.com/react-dom@16.13.1/umd/react-dom.production.min.js

react-16.6.3 是官方最后提供的js下载可 <script src=""></script>方式使用
源码包中的内容是npm方式，即有require
<script src="react-16.6.3/react.production.min.js"></script>
<script src="react-16.6.3/react-dom.production.min.js"></script>

<!-- offical  
 <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script> 
 会跳转为下面版本
 <script src="https://unpkg.com/@babel/standalone@7.9.1/babel.min.js"></script> 
 <script src="https://unpkg.com/@babel/standalone@7.9.1/babel.js"></script>  
-->  
<!--
	babel.min.js可以将  ECMAScript 2016 代码转为 ES5 代码 
	内嵌了对 JSX 的支持 代码中嵌套多个 HTML 标签 ,不需要一定使用 JSX
	实时编辑会慢，集成了webpack
-->

<div id="hello"></div>
<div id="hello2"></div>

<div id="example"></div>

<div id="box"></div>
 <!--  type="text/babel" 方式chrome,firefox不能debug!!!! -->   
<script type="text/babel"> 
  
	var myStyle = {
		fontSize: 30,
		color: '#FF0000'
	};
	 const  i=0; //const 不可变的
	 var arr = [
	  <h1 key="1">数组元素1</h1>,
	  <h2 key="2">数组元素2</h2>,
	];
	//JSX 中不能使用 if else 语句，但可以使用 conditional (三元运算) 表达式来替代
	ReactDOM.render( //style 引用 数字后自动添加 px,也可不用中间变量就要用{{
		<span style = {myStyle}>  Hello, world!
			{i == 1 ? 'True!' : 'False'}
			{arr}
		</span>,
		document.getElementById('hello')
	);
   //---------
	  
 
	class Greeting extends React.Component {
	  render() {
		return <h1>Hello, World2</h1>;
	  }
	}
	ReactDOM.render(
        <Greeting></Greeting>,
        document.getElementById('hello2')
      );  
	 //--------- 	  
	 class Count extends React.Component 
	 { 
		constructor(myprops)//构造器,可选的,可以加 props,把会标签的所有属性传进来
		{
			console.log("Id="+myprops.id)
			super(); //一定要super();
			this.state={count:0};//修改state只有在构造器中才可以this.state=
			//其它方法要用this.setState()也应组件初始化后,如 componentDidMount
			this.clsName=myprops.className;
		}
		update()
		{
			this.setState({
				count:++this.state.count
			});
		}
		render() 
		{
			return( //根级只可一个元素,用(),这也用 className ,不然报警告
				<div  className={this.clsName}>
					<h1>Hi {this.props.content}</h1>
					<h2>{this.state.count}</h2>
				</div>
			);
		}
	}
	
	  //默认属性
	  Count.defaultProps = {
		content: 'Hello, World'
	  };
	
	//浏览中调用  count.update()只更新h2这块,因为更新的是state下面的变量
	//默认 state 或者 props  变量修改才会re-render,如果render()依赖其它数据，可以调用forceUpdate()
	//class是关键字，要用className, 对于for属性 如 <label for=""> 要用htmlFor
	let count=ReactDOM.render(
        <Count id="one" className="myCSS"></Count>,
        document.getElementById('example')
      );   
	//--------- 
 
	class Comment extends React.Component {
		constructor()
		{
			super();
			this.state={opacity:1.0};
		}
	  componentDidMount() {
		console.log("componentDidMount"); 
		//doAjax request json data
	
		this.timer = setInterval(function () { //function (){  或者使用Lambda格式  ()=>{ 就不用bind(this)
		  var opacity = this.state.opacity;
		  opacity -= .05;
		  if (opacity <0.1) {
			opacity = 1;
		  }
		  this.setState({
			opacity: opacity
		  });
		}.bind(this), 100); // 一定要bind(this),否则this.state找不到,不是React的this,而是JS的this,或者使用Lambda就不用bind(this)
	  }

	  render() {
		return ( 
		 // style用 {{ 双大括号  , props.children 是全部属性
		<div  style={{opacity: this.state.opacity }}>  
			<h4>{this.props.children}</h4>  
			<h6>{this.props.author}</h6> 
		</div>
		);//{this.props.author} 得到标签属性 {}中的内容是JS
	  }
	}
	
	class CommentForm extends React.Component 
	{
		handleClick(event) { 
			this.refs.myInput.focus();  // refs 属性引用 表单的ref属性 ,过时API
	    }
		render() {  
			return (
			  <div>
				<input type="text" ref="myInput" />
				<input
				  type="button"
				  value="点我输入框获取焦点"
				  onClick={
					event=>{this.handleClick(event)}
				  }
				/>
			  </div>
			);
	  }
  }
  
	let commentNodes=[
		{author:"zhao",content:"this is zhao content"},
		{author:"黄",content:"this is 黄 content"}
	]
	class CommentList extends React.Component {
	  render() {
		console.log("this.props.nodes=  "+this.props.nodes);
	
		//let mappedComment=this.props.nodes.map(function (comment){
		//	return <Comment author={comment.author}> {comment.content}</Comment> // 属性={} 不能有""
		//});
		
		let mappedComment=this.props.nodes.map( (comment,index) => { //Lamba 是 ECMAScript 6 功能
			return <Comment key={20+index} author={comment.author}> {comment.content}</Comment> // 属性={} 不能有""
		});
		var arrayComment=[];
		for(var i=0;i<3;i++)
		{
			var names="lisi"+i;
			//一个父组件里有多个相同的子组件时,每个子组件要有key属性,每个值不能相同
			arrayComment.push(<Comment key={10+i} author={names}> this is lisi {i} comment</Comment>)
		}
		return ( //可嵌套自定义标签
		<div>
			{arrayComment}
			<Comment key="90"  author="wang"> this is wang comment</Comment> 
			{mappedComment}
			<CommentForm></CommentForm>
		</div>
		); 
	  }
	}
	
	ReactDOM.render(
        <CommentList nodes={commentNodes}></CommentList> ,   //这只能一个组件
        document.getElementById('box')
      );  

 </script>


React.Component 的生命周期  will方法和did方法
1. Mounting  的方法 

    constructor()
    static getDerivedStateFromProps()   //UNSAFE_componentWillMount()
    render()
    componentDidMount()

2.Updating 当props or state属性修改调用的方法  

    static getDerivedStateFromProps()
    shouldComponentUpdate() 
    render()
    getSnapshotBeforeUpdate()    //UNSAFE_componentWillUpdate()
    componentDidUpdate() 如shouldComponentUpdate()返回false不会被调用

3.Unmounting 
    componentWillUnmount()  //will只有Unmount了

4.Error Handling
      static getDerivedStateFromError()
      componentDidCatch()

不使用JSX 
<div id="root"></div>
<script type="text/babel">

 const e = React.createElement;

 class Hello extends React.Component {
  render() {
	//return React.createElement('div', null, `Hello ${this.props.toWhat}`); 
	return e('div', null, `Hello ${this.props.toWhat}`); 
   
	//React.createElement(标签，属性，文本)  JS用`` ,可用 e()
	
  }
}

ReactDOM.render(
  //React.createElement(Hello, {toWhat: 'World'}, null),
	e(Hello, {toWhat: 'World'}, null),
  document.getElementById('root')
);
  
</script>
 

	function FormattedDate(props) {// props 是不可变的
  		return <h2>现在是 {props.date.toLocaleTimeString()}.</h2>;
	}
    class Clock extends React.Component {
      constructor(props) {
        super(props);
        this.state = {date: new Date()};
      }
      
 	 componentDidMount() { //在render后执行

		 /*
		this.timerID = setInterval(
			  () => this.tick(), //箭头函数有 this(和Vue相反)
			  1000
		 );
		*/
		//方式二
		let that = this;
		this.timerID = setInterval(function () {
		  return that.tick();
		},1000);

	  }
	 
	  componentWillUnmount() {
		clearInterval(this.timerID);
	  }
	 
	  tick() {
		//只需更新组件的 state，然后根据新的 state 重新渲染用户界面
		this.setState({
		  date: new Date()
		});
	  }

      render() { //在constructor后执行
        return (
          <div>
            <h1>Hello, world!</h1>
            <h2>现在是 {this.state.date.toLocaleTimeString()}.</h2>
   			<FormattedDate date={this.state.date} />
          </div>
        );
      }
    }
     
    ReactDOM.render(
      <Clock />,
      document.getElementById('example')
    );

//每当 Clock 组件第一次加载到 DOM 中的时候，我们都想生成定时器，这在 React 中被称为挂载。
//同样，每当 Clock 生成的这个 DOM 被移除的时候，我们也会想要清除定时器，这在 React 中被称为卸载。

//执行顺序 , ReactDOM.render 未完成->Clock构构器-> render->componentDidMount



function App() {
  return (
    <div>
      <Clock />
      <Clock />
      <Clock />
    </div>
  );
}
ReactDOM.render(<App />, document.getElementById('appDiv'));





class HelloMessage extends React.Component {
  render() {
    return (
      <h1>Hello, {this.props.name}</h1>
    );
  }
}
 
HelloMessage.defaultProps = { // defaultProps属性设置默认属性
  name: 'Runoob'
};
 
const element = <HelloMessage/>;
 
ReactDOM.render(
  element,
  document.getElementById('propsDiv')
);

 <div id="example"></div>
 appDiv<br/>
 <div id="appDiv"></div>
 
 props<br/>
 <div id="propsDiv"></div>


------------event

class Toggle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {isToggleOn: true};
 
    // 这边绑定是必要的，这样 `this` 才能在回调函数中使用
    this.handleClick = this.handleClick.bind(this);
  }
 
  handleClick() {
    this.setState(prevState => ({ //传一个函数，一个参数是老的state，返回对象
      isToggleOn: !prevState.isToggleOn
    }));
  }
 
  render() {
    return (
	//onClick命名是驼峰式, 这里handleClick后不能加()表示绑定this
      <button onClick={this.handleClick}>   
        {this.state.isToggleOn ? 'ON' : 'OFF'}
      </button>
    );
  }
}
 
ReactDOM.render(
  <Toggle />,
  document.getElementById('example')
);



class LoggingButton extends React.Component {
  // 这个语法确保了 `this` 绑定在  handleClick 中 
  handleClick = () => {
    console.log('this is:', this);
  }
 
  render() {
    return (
      <button onClick={this.handleClick}>
        Click me 1
      </button>
    );
  }
}
ReactDOM.render(
  <LoggingButton />,
  document.getElementById('btn1')
);


class LoggingButton2 extends React.Component {
  handleClick() {
    console.log('this is:', this);
  }
 
  render() {
    //  这个语法确保了 `this` 绑定在  handleClick 中
	//每次渲染的时候都会创建一个不同的回调函数
    return (
      <button onClick={(e) => this.handleClick(e)}>
        Click me 2
      </button>
    );
  }
}
ReactDOM.render(
  <LoggingButton2 />,
  document.getElementById('btn2')
);


class Popper extends React.Component{
    constructor(){
        super();
        this.state = {name:'Hello world!'};
    }
    
    preventPop(name, e){    //事件对象e要放在最后
        e.preventDefault();
        alert(name);
    }
    deleteRow(id)
	{
 		alert('delete'+id);
	}
    render(){
        return (
            <div>
                <p>通过 bind() 方法传递参数</p>
                {/* 通过 bind() 方法传递参数 */}
                <a href="https://reactjs.org" onClick={this.preventPop.bind(this,this.state.name)}>Click</a>
				<br/>				
				<button onClick={(e) => this.deleteRow(2, e)}>Delete Row</button>     {/* 事件对象e要放在最后 */}
          
			</div>
        );
    }
}
ReactDOM.render(
  <Popper />,
  document.getElementById('passVar')
);

//evt.cancelBubble = true;//原始JS方法 没用
//以下两相可以同时用
evt.stopPropagation();//原始JS方法可以阻止父级DIV,但不能阻止document
evt.nativeEvent.stopImmediatePropagation();//react 可以阻止document事件,不能阻止父级DIV

//-------form

class Reservation extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isGoing: true,
      numberOfGuests: 2,
	  male:true,
	 age:18
    };
 
    this.handleInputChange = this.handleInputChange.bind(this);
  }
 
  handleInputChange(event) {
    const target = event.target;//target是原生的
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
 
    this.setState({
      [name]: value  //使用[] ,变量的值做字段名,类似eval
    });
  }
 
  render() {
    return (
      <form>
        <label>
          是否离开:
          <input
            name="isGoing"
            type="checkbox"
            checked={this.state.isGoing}
            onChange={this.handleInputChange} />
        </label>
        <br />
 
        <label>
          访客数:
          <input
            name="numberOfGuests"
            type="number"
            value={this.state.numberOfGuests}
            onChange={this.handleInputChange} />
        </label><br />

	是否男
		{ /*  要用 defaultChecked,否则报警告 */ }
 		<input
            name="male"
            type="checkbox"
            defaultChecked={this.state.male} />
		<br />
	年龄：
		{ /*  要用 defaultValue ,否则报警告*/ }
		<input
            name="age"
            type="text"
            defaultValue={this.state.age} />
		<br />	
      </form>
    );
  }
}

ReactDOM.render(
  <Reservation />,
  document.getElementById('multiForm')
);


//-----

function NumberList(props) 
{

/*  const numbers = props.numbers; 
 const listItems = numbers.map((number,index) =>  //第二个参数index可选
    <li >
      index:{index},value: {number}
    </li>
  );
  return (
      <ul>{listItems}</ul> 
  );
 */
//方式二
 var numbers;    //声明在外面是因为 {} 中不能出现var,const,let等这种关键字
    return (
    <ul>
      {
        numbers = props.numbers,   //注意这里要加逗号
        numbers.map((number,index) =>
        <li key={index} >
      		index:{index},value: {number}
    	</li>
      )}
    </ul>
    );
}
 
const numbers = [1, 2, 3, 4, 5];
ReactDOM.render(
  <NumberList numbers={numbers} />,
  document.getElementById('example')
);


//setState()并不一定是同步的，为了提升性能React会批量执行state和DOM	   
//setState()总是会触发一次组件重绘，除非在shouldComponentUpdate()返回false
 

//forceUpdate()  方法调用后，会引发render()调用，跳过shouldComponentUpdate()
//当state 发生变化时会调用组件内部的render()方法 
 
/* 生命周期

UNSAFE_componentWillMount()
componentDidMount() //可以在里面调用 setState() 
UNSAFE_componentWillReceiveProps(nextProps)
shouldComponentUpdate(nextProps, nextState)
UNSAFE_componentWillUpdate(nextProps, nextState)
componentDidUpdate(prevProps, prevState, snapshot)
componentWillUnmount()//其它的will都是UNSAFE
*/

class MyComponent extends React.Component {
  handleClick() {
    // 使用原生的 DOM API 获取焦点
    this.refs.myInput.focus();  //过时API
  }
  render() {
    //  当组件插入到 DOM 后，ref 属性添加一个组件的引用于到 this.refs
    return (
      <div>
        <input type="text" ref="myInput" />
        <input
          type="button"
          value="点我输入框获取焦点"
          onClick={this.handleClick.bind(this)}
        />
      </div>
    );
  }
}
 
 //新API
 
//--为 DOM 元素
class CustomTextInput extends React.Component {
  constructor(props) {
    super(props);
    // 创建一个 ref 来存储 textInput 的 DOM 元素
    this.textInput = React.createRef();
    this.focusTextInput = this.focusTextInput.bind(this);
  }

  focusTextInput() {
    // 直接使用原生 API 使 text 输入框获得焦点
    // 注意：我们通过 "current" 来访问 DOM 节点
    this.textInput.current.focus();
  }

  render() {
    // 告诉 React 我们想把 <input> ref 关联到
    // 构造器里创建的 `textInput` 上
    return (
      <div>
        <input
          type="text"
          ref={this.textInput} />

        <input
          type="button"
          value="Focus the text input"
          onClick={this.focusTextInput}
        />
      </div>
    );
  }
}
ReactDOM.render(
  <CustomTextInput />,
  document.getElementById('refDom')
);

//--为 class 组件
class AutoFocusTextInput extends React.Component {
  constructor(props) {
    super(props);
    this.textInput1 = React.createRef();
  }
  //页面加载后立即得到光标
  componentDidMount() {
	//这里的current为<CustomTextInput>,调用它的focusTextInput()方法
    this.textInput1.current.focusTextInput();
  }

  render() {
    //仅在 CustomTextInput 声明为 class 时加ref属性才有效
    return (
      <CustomTextInput ref={this.textInput1} />
    );
  }
} 
ReactDOM.render(
  <AutoFocusTextInput />,
  document.getElementById('refClass')
);


//-----ref  回调函数方式
class CustomTextInput extends React.Component {
  constructor(props) {
    super(props);

    this.textInput = null;

    this.setTextInputRef = element => {
      this.textInput = element;
    };

    this.focusTextInput = () => {
      // 使用原生 DOM API 使 text 输入框获得焦点
      if (this.textInput) this.textInput.focus();
    };
  }

  componentDidMount() {
    // 组件挂载后，让文本框自动获得焦点
    this.focusTextInput();
  }

  render() {
    // 使用 `ref` 的回调函数将 text 输入框 DOM 节点的引用存储到 React
    // 实例上（比如 this.textInput）
    return (
      <div>
        <input
          type="text"
          ref={this.setTextInputRef}
        />
        <input
          type="button"
          value="Focus the text input"
          onClick={this.focusTextInput}
        />
      </div>
    );
  }
}
//在组件挂载时，会调用 ref 回调函数并传入 DOM 元素，当卸载时调用它并传入 null
//在 componentDidMount 或 componentDidUpdate 触发前
----- 高级指引中的 Refs 转发
//React.forwardRef函数,转发外部引用指向内部,给外部引用赋值为<button>
//只在使用 React.forwardRef 定义组件时存在第二个参数 ref
const FancyButton = React.forwardRef((props, outRef) => (
  <button ref={outRef} className="FancyButton">
    {props.children}
  </button>
));
//Ref 转发不仅限于 DOM 组件，你也可以转发 refs 到 class 组件实例中
//你的库可能会有明显不同的行为（例如 refs 被分配给了谁，以及导出了什么类型）,可认为是破坏性更改

class MyComponent extends React.Component 
{
   constructor(props) 
	{
 	   super(props);
	   this.innerRef = React.createRef();
     this.myHandle=this.handleClick.bind(this);
	} 
  handleClick() {
    //innerRef.current 将指向 <button> DOM 节点。
     this.innerRef.current.innerHTML="新的文本";
  } 
 render() { 
    return (
    <div> 
      {/* 可以直接获取 DOM button(内部)  的 ref(外部组件 操作 子组件)  */}
      <FancyButton ref={this.innerRef}>
        这里的onClick无效 
      </FancyButton> <br/>
      <button onClick={this.myHandle} > 修改前面的文本 </button>
      <button onClick={this.handleClick.bind(this)} > 修改前面的文本 </button> 
    </div>  
    );
  }
}
ReactDOM.render(
  <MyComponent />,
  document.getElementById('example')
);


<div id="example"></div>  
--高阶组件 HOC 看转发
//特殊属性 ref 和 key是不会被props传递的 

//高阶组件 返回是一个forwardRef
function logProps(Component) {
  class LogProps extends React.Component {
    componentDidUpdate(prevProps) {
      console.log('old props:', prevProps);
      console.log('new props:', this.props);
    }

    render() {
      const {forwardedRef, ...rest} = this.props;

      // 将自定义的 prop 属性 “forwardedRef” 定义为 ref
      return <Component ref={forwardedRef} {...rest} />;
    }
  }

  // 注意 React.forwardRef 回调的第二个参数 “ref”。
  // 我们可以将其作为常规 prop 属性传递给 LogProps，例如 “forwardedRef”
  // 然后它就可以被挂载到被 LogProps 包裹的子组件上。 
  /*
  return React.forwardRef((props, ref) => {
    return <LogProps {...props} forwardedRef={ref} />;
  });
*/
/*
  //react developer tools  会显示myFunction名代替上面的anonymous
  return React.forwardRef(function myFunction(props, ref)  {
    return <LogProps {...props} forwardedRef={ref} />;
  });
*/
 function forwardRef(props, ref) {
    return <LogProps {...props} forwardedRef={ref} />;
  }
  const name = Component.displayName || Component.name;//Component.name的值是类名
  forwardRef.displayName = `logProps(${name})`;//displayName属性设置reactDeveloperTools显示名字
  return React.forwardRef(forwardRef);
  
}


----- 高级指引中的 Context
一种在组件之间共享此类值的方式，而不必显式地通过组件树的逐层传递 props。
Context 主要应用场景在于很多不同层级的组件需要访问同样一些的数据。请谨慎使用，因为这会使得组件的复用性变差。


// 为当前的 theme 创建一个 context（“light”为默认值）。
const ThemeContext = React.createContext('light'); 

class App extends React.Component {
  render() {
    // 使用一个 Provider 来将当前的 theme 传递给以下的组件树。
    // 无论多深，任何组件都能读取这个值。
    // 在这个例子中，我们将 “dark” 作为当前的值传递下去。
    return (
      <ThemeContext.Provider value="dark">
        <Toolbar />
      </ThemeContext.Provider>
    );
  }
}

// 中间的组件再也不必指明往下传递 theme 了。
function Toolbar(props) {
  return (
    <div>
      <ThemedButton />
    </div>
  );
}

class ThemedButton extends React.Component {
  // 指定 contextType 读取当前的 theme context。
  // React 会往上找到最近的 theme Provider，然后使用它的值。
  // 在这个例子中，当前的 theme 值为 “dark”。
  static contextType = ThemeContext; //必须是  static contextType 
  render() {
    //取值用 this.context
    return <button theme={this.context} >主题{this.context}按钮</button>;
  }
}
-----组合composition 非继承
推荐使用组合而非继承来实现组件间的代码重用


 function FancyBorder(props) {
	//props.children 是所有的子无素 
  return (
    <div className={'FancyBorder FancyBorder-' + props.color}>
      {props.children}
    </div>
  );
}
function WelcomeDialog() {
  return (
    <FancyBorder color="blue">
      <h1 className="Dialog-title">
        Welcome
      </h1>
      <p className="Dialog-message">
        Thank you for visiting our spacecraft!
      </p>
    </FancyBorder>
  );
}

//---- 
function Contacts() {
  return <div className="Contacts" >Contacts</div>;
}
function Chat() {
  return <div className="Chat" >Chat</div>;
} 
function SplitPane(props) {
  return (
    <div className="SplitPane">
      <div className="SplitPane-left">
        {props.left}
      </div>
      <div className="SplitPane-right">
        {props.right}
      </div>
    </div>
  );
}
function App() {
 // props 可以传组件 使用{}包含  
  return (
    <SplitPane
      left={
        <Contacts />
      } 
      right={
        <Chat />
      } /> 
  );
}

------高级指引中的 Render Props

class Cat extends React.Component {
  render() {
    const mouse = this.props.mouse;
    return (
      <img src="./cat.jpeg" style={{ position: 'absolute', left: mouse.x, top: mouse.y }} />
    );
  }
}

class Mouse extends React.Component {
  constructor(props) {
    super(props);
    this.handleMouseMove = this.handleMouseMove.bind(this);
    this.state = { x: 0, y: 0 };
  }

  handleMouseMove(event) {
    this.setState({
      x: event.clientX,
      y: event.clientY
    });
  }

  render() {
    return (
      <div style={{ height: '100%' }} onMouseMove={this.handleMouseMove}>

        {/* 
          这里直接调用props中的函数，这里不用写死Cat
        */}
        {this.props.render1(this.state)}
      </div>
    );
  }
}

class MouseTracker extends React.Component {
  render() {
    return (
      <div>
        <h1>移动鼠标333!</h1>
        <Mouse render1={mouse => (
          <Cat mouse={mouse} />
        )}/> {/* props中定义的属性是一个函数，使用{}包起来，这样Cat组件就不用写死在Mouse的类中了*/}
      </div>
    );
  }
}

------高级指引中的  高阶组件
Higher-Order Components(HOC)
高阶组件   参数为组件，返回值为新组件的函数
		 一个函数  参数为组件，返回值为新组件   
 return class extends React.Component {  //匿名类
	...
 }

不要使用prototype的方式修改输入组件

// 过滤掉非此 HOC 额外的 props，且不要进行透传
const { extraProp, ...passThroughProps } = this.props;

如 参数为多个 即 多个组件，返回值是函数 的情况下，可改进


<div id="hocDiv"></div> 
 // 此函数接收一个组件，返回组件 
const toUpperCaseHoc = function(WrappedComponent) {
  return class  extends React.Component { //匿名类
    render() {
      const { text } = this.props;
      const text2Upper = text.toUpperCase();
      return <WrappedComponent text={text2Upper} />;
    }
  };
};
 
class HelloWorld extends React.Component {
  render() {
    return this.props.text;
  }
}  
const HelloWorld2Upper = toUpperCaseHoc(HelloWorld); 
ReactDOM.render(<HelloWorld2Upper text="hello,world!" />, document.querySelector('#hocDiv'));

------Fragment
<div id="tableDiv"></div> 

class Table extends React.Component {
  render() {
    return (
	  <table>
		<tbody>
        <tr>
          <Columns />
		</tr>
		</tbody>
      </table>
    );
  }
}
//使用<React.Fragment> 或  <> </> 不支持 key 属性
/*
class Columns extends React.Component {
  render() {
    return (
      <React.Fragment>
        <td>Hello</td>
        <td>World</td>
      </React.Fragment>
    );
  }
}
*/
class Columns extends React.Component {
  render() {
    return (
      <>
        <td>Hello</td>
        <td>World</td>
      </>
    );
  }
}
 
ReactDOM.render(
  <Table />,
  document.getElementById('tableDiv')
);
------Hooks 是 React 16.8的新功能
<script src="unpkg_react-16.8.4/react.development.js"></script>
<script src="unpkg_react-16.8.4/react-dom.development.js"></script>
 这种引入的方式不行，报useState is not define
 
 Hook可在组件之间复用状态逻辑
 
---AppHook.js
import React, {useState,useEffect} from 'react'; 
function App()  //Hook在类中不能工作(不推荐修改现有的代码，修改为不使用类，但可以新代码使用hook不使用类)，可以保存值的函数，函数组件
{ 
 const [count, setCount] = useState(0); //useState 返回两参数(pair)，当前状态值和一个函数用于更新值,参数是是初始状态 
  //可以多次调用 useState 来保存多个值
  
  
  // 等同于 componentDidMount, componentDidUpdate,  componentWillUnmount
  //可用多次使用 useEffect,按照 effect 声明的顺序依次调用
  //useLayoutEffect
  useEffect(() => { 
    document.title = `You clicked ${count} times`;
     //useEffect可选的返回一个无参函数 用于清除，组件卸载的时候执行清除操作
  });
  
  
  return (
    <div>
      <p>You clicked {count} times</p>   {/* 直接用，(符合react的数据变 联动 界面） */}
      <button onClick={() => setCount(count + 1)}>
        Click me
      </button>
    </div>
  );
}

使用Hook的规则
  只在最顶层使用 Hook ,不要在循环，条件或嵌套函数中调用 Hook
  只在 React 函数中调用 Hook

eslint   ECMAScript语法规则和代码风格的检查工具 ,intellij idea 带这个插件

npm install eslint-plugin-react-hooks --save-dev   来强制执行这Hook规则

自定义hook要求函数名字必须以use开头,可带一个参数，函数内部可以调用useState,useEffect .可以实现代码共享

两个组件中使用相同的 自定义Hook 会共享 state 吗?不会

Hook API 还有很多其它的use方法
基础 Hook
 useContext
 接收一个 context 对象(React.createContext 的返回值)并返回该 context 的当前值，
 调用了 useContext 的组件总会在 context 值变化时重新渲染
 
  const locale = useContext(LocaleContext);
  const theme = useContext(ThemeContext);
  
额外的 Hook
 useReducer
 useCallback
 useMemo
 useRef
 useImperativeHandle
 useDebugValue
 useLayoutEffect
 


-----------redux 是作者受  Flux 的影响
官方
https://redux.js.org/

http://cn.redux.js.org/

官方文档有建议用  redux 中的 reducer 来编写

npm install -g redux
npm install --save redux
redux@4.0.5


npm install -g  redux-devtools
npm install --save-dev redux-devtools
redux-devtools@3.5.0

https://unpkg.com/browse/redux@4.0.5/dist/ 可下拉选择版本

https://unpkg.com/browse/redux@4.0.5/dist/redux.js  显示是html要手工复制
https://unpkg.com/redux@4.0.5/dist/redux.js
https://unpkg.com/redux@4.0.5/dist/redux.min.js



<script src="https://unpkg.com/redux@4.0.5/dist/redux.js"></script>
//加了<script>应不用import了,但还是找不到,import 时报  require is not defined
//import { createStore } from './unpkg_redux-4.0.1/redux.js' 


import { createStore } from 'redux'

//reducer函数 state可以是对象，但不能修改这个对象，要返回新的对象
function counter(state = 0, action) {
  switch (action.type) {//必须命名为type
    case 'INCREMENT':
      return state + 1
    case 'DECREMENT':
      return state - 1
    default:
      return state
  }
} 
let store = createStore(counter)  //store里面存储state
store.subscribe(() => console.log(store.getState())) 
store.dispatch({ type: 'INCREMENT' }) //必须命名为type

//------- 官方count示例  redux模块
//--index.js
import React from 'react'
import ReactDOM from 'react-dom'
import { createStore } from 'redux'

import Counter from './components/Counter'
import counter from './reducers'//如目录下有index.js就可不用加默认导入

const store = createStore(counter)//store里面存储state
const rootEl = document.getElementById('root')

const render = () => ReactDOM.render(
  <Counter
    value={store.getState()}//必须是getState(),相当于react的state,如数据变化,使用的地方会动刷新
    onIncrement={() => store.dispatch({ type: 'INCREMENT' })}//必须命名为type
    onDecrement={() => store.dispatch({ type: 'DECREMENT' })}
  />,
  rootEl
)

render()
store.subscribe(render)

//--reducer/index.js
export default (state = 0, action) => { //state名可变
  switch (action.type) { //必须命名为type
    case 'INCREMENT':
      return state + 1
    case 'DECREMENT':
      return state - 1
    default:
      return state
  }
}

//--components/Counter.js
import React, { Component } from 'react'
class Counter extends Component {
  constructor(props) {
    super(props);
    this.incrementAsync = this.incrementAsync.bind(this);
    this.incrementIfOdd = this.incrementIfOdd.bind(this);
  }

  incrementIfOdd() {
    if (this.props.value % 2 !== 0) {
      this.props.onIncrement()
    }
  }

  incrementAsync() {
    setTimeout(this.props.onIncrement, 1000)
  }

  render() {
    const { value, onIncrement, onDecrement } = this.props
    return (
      <p>
        Clicked122: {value} times
        {' '}
        <button onClick={onIncrement}>
          +
        </button>
        {' '}
        <button onClick={onDecrement}>
          -
        </button>
        {' '}
        <button onClick={this.incrementIfOdd}>
          Increment if odd
        </button>
        {' '}
        <button onClick={this.incrementAsync}>
          Increment async
        </button>
      </p>
    )
  }
} 
export default Counter



//-----------react-redux 
官方 https://react-redux.js.org/


npm install -g  react-redux
npm install --save react-redux
react-redux@7.2.0

//示例
//--index.jsx
import React, {Component} from 'react';
import ReactDOM, {render} from 'react-dom';
import {Provider} from 'react-redux';

import store from './Redux/Store/Store.jsx'
import ControlPanel from './Component/ControlPanel.jsx' 
render(
    <Provider store={store}>   {/* 子标签可以拿到store原理是使用了context */}
    <ControlPanel />
    </Provider>,
    document.body.appendChild(document.createElement('div'))
);
//---./Redux/Store/Store.jsx
import {createStore} from 'redux'

import reducer from '../Reducer' 
const initValue={
    'First':0,
    'Second':10,
    'Third':20
}
const store=createStore(reducer,initValue)//可带第二个参数，默认状态
export default store

//---./Reducer/index.js
import {Increment,Decrement} from '../Action'
export default(state,action)=>{
    const {counterCaption}=action
    console.log("state[counterCaption]="+state[counterCaption]);//能取到值
    switch (action.type){
        case Increment:
			 //return {...state,[counterCaption]:state[counterCaption]+1}//逻辑同下方
			var clonedObj={...state};//复制对象
			clonedObj[counterCaption]= state[counterCaption]+1;
			return clonedObj;
        case Decrement:
			return {...state,[counterCaption]:state[counterCaption]-1}
		default:
			return state
    }
}

//---./Action/index.js
export const Increment='increment'
export const Decrement='decrement'

export const increment=(counterCaption)=>({
    type:Increment,
    counterCaption //这里可以没有:
  }
)
export const decrement=(counterCaption)=>({
    type:Decrement,
    counterCaption
})

//---./Component/ControlPanel.jsx
import React, { Component } from 'react'

import Counter from './Counter.jsx'
import Summary from './Summary.jsx'
const style = {
    margin: "20px"
}

class ControlPanel extends Component {
    render() {
        return (
            <div style={style}>
                <Counter caption="First" />
                <Counter caption="Second"/>
                <Counter caption="Third" />
                <hr/>
                <Summary/>
            </div>
        )
    }
}
export default ControlPanel

//---./Component/Counter.jsx
import {connect} from 'react-redux'
import React, { Component } from 'react'

import {increment,decrement} from '../Redux/Action'

const buttonStyle = {
    margin: "20px"
}

function Counter({caption, Increment, Decrement, value}){
 //caption是props属性,其它3个为下再两函数返回放入props中
    return (
            <div>
                <button style={buttonStyle} onClick={Increment}>+</button>
                <button style={buttonStyle} onClick={Decrement}>-</button>
                <span>{caption} count :{value}</span>
            </div>
        )
}
function mapStateToProps(state,ownProps){//负责输入逻辑，即将state映射到 UI 组件的参数（props）
    return{
        value:state[ownProps.caption]
    }
}

function mapDispatchToProps(dispatch,ownProps){//输出逻辑  负责将用户操作转化为Action,功能函数映射到展示组件的this.props
    return {
        Increment:()=>{
			//dispatch是store的方法
            dispatch(increment(ownProps.caption)) //增加和减少的动作派发给Store
        },
        Decrement:()=>{
            dispatch(decrement(ownProps.caption))
        }

    }
}
//可以省略mapStateToProps参数,store更新就不会触发展示组件重绘了
//第二个参数mapDispatchToProps也可是个对象
export default connect(mapStateToProps,mapDispatchToProps)(Counter)//返回一个新的容器组件，带逻辑的，里面是负责显示的组件

//---./Component/Summary.jsx
import React from 'react';
import {connect} from 'react-redux';
function Summary({value}){
        return (
            <div>Total Count: {value}</div>
        );
}
function mapStateToProps(state){
    let sum=0
    for (const key in state) {
        if (state.hasOwnProperty(key)) {
          sum += state[key];
        }
      }
    return {value: sum};
}
export default connect(mapStateToProps)(Summary)

//----redux 官方 todos 示例 有使用react-redux 有使用 combineReducers
git clone https://github.com/reduxjs/redux.git

cd redux/examples/todos
npm install
npm start




//----redux 官方undo 示例 有引用新库
可以像word一样做撤消和重作， 不常用，比较复杂 
cd redux/examples/todos-with-undo
 
 

-----------react-router ,可结合 Redux一起使用
<script src="https://unpkg.com/react-router@5.1.2/umd/react-router.js"></script>
<script src="https://unpkg.com/react-router@5.1.2/umd/react-router.min.js"></script>

//import { BrowserRouter  , Route, Link } from "react-router-dom";
//import { BrowserRouter  , Route, Link } from "../unpkg_react-router-5.1.2/react-router.js";

加import 报 require is not defined 
不加import 报 BrowserRouter is not defined
	

https://github.com/ReactTraining/react-router
有web和native指南

npm install react-router -g         react-router@5.1.2
npm install react-router-dom  -g    react-router-dom@5.1.2

//--官方web 基本示例 
import React from "react";
import { BrowserRouter , Route, Link } from "react-router-dom";
//BrowserRouter影响浏览器的返回
function Index() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}

function AppRouter() {
  return (
    <BrowserRouter>
      <div>
        <nav>
          <ul>
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/about/">About</Link>
            </li>
            <li>
              <Link to="/users/">Users</Link>
            </li>
          </ul>
        </nav>

        <Route path="/" exact component={Index} />  {/*  exact 表示精确匹配，如不指定则跳转地址只要以配置的path开头即可 */}
        <Route path="/about/" component={About} />
        <Route path="/users/" component={Users} />
      </div>
    </BrowserRouter>
  );
}

export default AppRouter;

//--官方web 嵌套路由示例
import React from "react";
import { BrowserRouter , Route, Link } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <div>
        <Header />

        <Route exact path="/" component={Home} />
        <Route path="/about" component={About} />
        <Route path="/topics" component={Topics} />
      </div>
    </BrowserRouter>
  );
}

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Topic({ match }) {
  return <h3>Requested Param: {match.params.myid}  path= {match.path} ,url= {match.url}</h3>; 
   {/*match.params.xxx 得到变量名,match.path为 /topics/:myid  match.url 为 /topics/2323 */}
}

function Topics({ match }) {  /* match参数 */
  return (
    <div>
      <h2>Topics</h2>

      <ul>
        <li>
        	{/* ${match.url} 的值就是/topics  */}
          <Link to={`${match.url}/components`}>Components</Link>
        </li>
        <li>
          <Link to={`${match.url}/props-v-state`}>Props v. State</Link>
        </li>
      </ul>

      <Route path={`${match.path}/:myid`} component={Topic} /> 
        {/*Link的 match.url 对应这的 match.path，BrowserRouter浏览器地址栏上会显示对应的地址
          myid是变量名，以：开头 
          component 指定子组件 */}
      <Route
        exact
        path={match.path}
        render={() => <h3>Please select a topic.</h3>}  
      />  {/* render函数 */}
    </div>
  );
}

function Header() {
  return (
    <ul>
      <li>
        <Link to="/">Home</Link>
      </li>
      <li>
        <Link to="/about">About</Link>
      </li>
      <li>
        <Link to="/topics">Topics</Link>
      </li>
	  <li>
        <Link to="/topics/123">Topics param</Link>
      </li>
    </ul>
  );
}

export default App;


BrowserRouter 使用HTML5的 window.history.pushState() ,   window.history.replaceState() ,history.state
          浏览器地址栏上会显示对应的地址
		影响浏览器的返回
HashRouter  使用HTML5的 window.location.hash  即#的锚点
		影响浏览器的返回
<HashRouter   basename="/calendar"> {/* basename可以不加  , 显示为  #/calendar/users/  */}
</HashRouter>
 
 
MemoryRouter 可用于React Native(不会影响浏览器的返回)
   <MemoryRouter   initialEntries={[ '/', '/about', { pathname: '/users' } ]}
    initialIndex={0}>
  </MemoryRouter>
  
  
{/*NavLink 可带选中的样式 也有 activeClassName */}
<NavLink
  to="/users/"
  activeStyle={{
    fontWeight: "bold",
    color: "red"
  }}
>
  Users with Nav
</NavLink>


-----------Flow 由 Facebook 开发,用于替代PropTypes 
https://flow.org/en/docs/getting-started/
https://github.com/facebook/flow

用来称除@flow，代码转换
npm install -g @babel/core @babel/cli @babel/preset-flow  

npm install -g flow-bin

项目级安装
npm init 
npm install --save-dev @babel/core @babel/cli @babel/preset-flow  
npm install --save-dev flow-bin

flow 命令路径 node_modules/.bin/flow  

进入项目目录 
flow init 生成.flowconfig文件

--check.js
// @flow
function square(n: number): number {
  return n * n;
}

//square("2"); // Error!
square(2);  
---

flow 检查语法

flow check

---编译
建立 .babelrc
{
  "presets": ["@babel/preset-flow"]
}
./node_modules/.bin/babel src/ -d lib/  把src目录下的文件编译转换后放在指定目录
也可放package.json文件中
"scripts": {
    "com-fow": "babel src/ -d lib/",
}



