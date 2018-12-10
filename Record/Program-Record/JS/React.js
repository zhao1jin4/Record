 


react 官方中文翻译网  https://doc.react-china.org/
 
http://www.runoob.com/react/react-tutorial.html
https://reactjs.org/
https://github.com/facebook/react/releases   目前版本是 16.2.0



React_Flux实现Facebook
https://facebook.github.io/flux/
http://www.ruanyifeng.com/blog/2016/01/flux.html

redux 是作者受  Flux 的影响

ExtReact 收费(EXTJS)


https://reacteasyui.github.io/ 国产的 组件样式依赖 Bootstrap 

免费UI框架有 https://reactjs.org/community/ui-components.html

Material UI  适用移动端,实现google android 的Material风格(Storybook生成Material UI主题 ) 


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


====后来页面上没有的项目
 react-bootstrap 项目(Bootstrap 4) 
 https://github.com/react-bootstrap/react-bootstrap
 
 
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


npm install  react react-dom  --save 依赖保存到package.json  

npm init  会提示回车生成package.json文件
npm install --save browser-sync   //--save存在package.json文件中  安装 browser-sync,是一个服务器监控修改生效
 package.json 修改 "scripts": 区，如没有在根的{下增加
	 "scripts": {
		"dev": "browser-sync start --server --files *.*"
	  },
  
npm run dev  会启动 http://localhost:3000
package.json文件中有 dependencies字段,表示依赖的模块, 用 npm install 就会安装所有的依赖


React Developer Tools (firefox ,chrome 插件)
如独立安装  npm install -g react-devtools  再 react-devtools
 


//D:\Program\node-v10.13.0-win-x64\node_modules\create-react-app
npm install -g create-react-app
create-react-app my-app  //也会下载react,react-dom ...很多东西 
实际上是执行 node.exe D:\Program\node-v10.13.0-win-x64\node_modules\create-react-app\index.js .

cd my-app    //生成的node_modules目录非常大,100多M，2万多个文件 不适用 , 基于 Webpack + ES6
生成的
package.json
node_modules/...
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
 %HOMEPATH%\AppData\Local\Microsoft\TypeScript\3.1\node_modules\@types\react-dom
										   项目目录\node_modules\@types\react
Webstorm(IntellijIDEA不能有此功能)也是使用create-react-app my-app  但是用yarn做的
									   
项目目录下 npm start (start定义在package.json中) 打开 http://localhost:3000 
如要修改端口 package.json 中scripts中的start 命令 修改如下
   "scripts": {
		"start": "set PORT=4000 && react-scripts start"
   }
	
npm run build 生成 build 目录HTML,JS 压缩式 (Webpack工具) ,要用web服务式运行上下文为/ 才行
 
 
https://github.com/facebook/react/releases 下载.js

<script src="react-16.6.1/react.production.min.js"></script>
<script src="react-16.6.1/react-dom.production.min.js"></script>
<!-- offical  
 <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
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
 <!--
 type="text/javascript"
  type="text/babel" -->   
<script type="text/babel">
  
	var myStyle = {
		fontSize: 30,
		color: '#FF0000'
	};
	 const  i=0; //const 不可变的
	 var arr = [
	  <h1>数组元素1</h1>,
	  <h2>数组元素2</h2>,
	];
	//JSX 中不能使用 if else 语句，但可以使用 conditional (三元运算) 表达式来替代
	ReactDOM.render( //style 引用 数字后自动添加 px,也可不用中间变量就要用{{
		<h1 style = {myStyle}>  Hello, world!
			{i == 1 ? 'True!' : 'False'}
			{arr}
		</h1>,
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
			return( //根级只可一个无素用()   
				<div  class={this.clsName}>
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
	//class就关键字，要用className
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
			<h8>{this.props.author}</h8> 
		</div>
		);//{this.props.author} 得到标签属性 {}中的内容是JS
	  }
	}
	
	class CommentForm extends React.Component 
	{
		handleClick(event) { 
			this.refs.myInput.focus();  // refs 属性引用 表单的ref属性
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
		
		let mappedComment=this.props.nodes.map( comment => { //Lamba 是 ECMAScript 6 功能
			return <Comment author={comment.author}> {comment.content}</Comment> // 属性={} 不能有""
		});
		var arrayComment=[];
		for(var i=0;i<3;i++)
		{
		   var names="lisi"+i;
			arrayComment.push(<Comment author={names}> this is lisi {i} comment</Comment>)
		}
		return ( //可嵌套自定义标签
		<div>
			{arrayComment}
			<Comment author="wang"> this is wang comment</Comment> 
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
    componentWillMount()
    render()
    componentDidMount()

2.Updating 当props or state属性修改调用的方法  

    componentWillReceiveProps()
    shouldComponentUpdate()
    componentWillUpdate()
    render()
    componentDidUpdate()

3.Unmounting 
    componentWillUnmount()

错误时调用   componentDidCatch()


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
			  () => this.tick(),
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

//order , ReactDOM.render 未完成->Clock构构器-> render->componentDidMount



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
        Click me
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
        Click me
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
                <p>hello</p>
                {/* 通过 bind() 方法传递参数。 */}
                <a href="https://reactjs.org" onClick={this.preventPop.bind(this,this.state.name)}>Click</a>
				<br/>				
				<button onClick={(e) => this.deleteRow(2, e)}>Delete Row</button>   //事件对象e要放在最后
          
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
      [name]: value
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
		{ /* 用 checked={this.state.male} 就不可修改，要用 defaultChecked */ }
 		<input
            name="male"
            type="checkbox"
            defaultChecked={this.state.male} />
		<br />
	年龄：
		{ /* 用 value={this.state.age}  就不可修改，要用 defaultValue */ }
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
        <li >
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
 

//forceUpdate()  方法调用后，会引发render()调用，跳过shouldComponentUpdate(),只有在在render()该用中读this.props 和 this.state时调用这 个方法 
 
/* 生命周期

UNSAFE_componentWillMount()
componentDidMount() //可以在里面调用 setState() 
UNSAFE_componentWillReceiveProps(nextProps)
shouldComponentUpdate(nextProps, nextState)
UNSAFE_componentWillUpdate(nextProps, nextState)
componentDidUpdate(prevProps, prevState, snapshot)
componentWillUnmount()
*/

class MyComponent extends React.Component {
  handleClick() {
    // 使用原生的 DOM API 获取焦点
    this.refs.myInput.focus();
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
 

