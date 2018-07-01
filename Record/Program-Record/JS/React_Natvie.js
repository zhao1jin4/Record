
=============React 

 react 官方中文翻译网  https://doc.react-china.org/
 
http://www.runoob.com/react/react-tutorial.html
https://reactjs.org/
https://github.com/facebook/react/releases   目前版本是 16.2.0

UI框架有 
Material UI   组件比较多，日历像是面向移动的

AmazeUI React 国产的
react-toolbox  https://react.rocks/example/react-toolbox 有示例
material-components-web
Belle
React Bootstrap 
Elemental UI 
https://github.com/react-component  阿里 



npm init  会提示回车生成package.json文件
npm install --save browser-sync   //--save存在package.json文件中  安装 browser-sync,是一个服务器监控修改生效
 package.json 修改 "scripts": 区
	 "scripts": {
		"dev": "browser-sync start --server --files *.*"
	  },
  
npm run dev  会启动 http://localhost:3000

React Developer Tools (firefox ,chrome 插件)
如独立安装  npm install -g react-devtools  再 react-devtools
 

npm install -g create-react-app
create-react-app my-app
cd my-app    //生成的node_modules目录非常大,100多M，2万多个文件 不适用 , 基于 Webpack + ES6
rm -f src/*  // */


src/下建立文件 index.css  
src/下建立文件 index.js  
	import React from 'react';
	import ReactDOM from 'react-dom';
	import './index.css';
项目目录下 npm start 打开 http://localhost:3000 



<script src="react-16.2.0/react.production.min.js"></script>
<script src="react-16.2.0/react-dom.production.min.js"></script>
<script src="https://cdn.bootcss.com/babel-standalone/6.22.1/babel.min.js"></script>  
<!--
	babel.min.js可以将 ES6(ECMAScript 2016) 代码转为 ES5 代码，这样我们就能在目前不支持 ES6 浏览器上执行 React 代码
	内嵌了对 JSX 的支持 代码中嵌套多个 HTML 标签，需要使用一个 div 元素包裹 ,不需要一定使用 JSX
-->

<div id="hello"></div>
<div id="hello2"></div>

<div id="example"></div>

<div id="box"></div>

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
	ReactDOM.render( //style 引用 数字后自动添加 px
		<h1 style = {myStyle}>  Hello, world!
			{i == 1 ? 'True!' : 'False'}
			{arr /*可直接使用数组*/}
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
	   

	  
	 class Count extends React.Component {
		constructor(myprops)//构造器,可选的,可以加 props,把会标签的所有属性传进来
		{
			console.log("Id="+myprops.id)
			super(); //一定要super();
			this.state={count:0};
		}
		update()
		{
			this.setState({
				count:++this.state.count
			});
		}
		  render() {
				return( //根级只可一个无素用()   
					<div>
						<h1>Hello, World </h1>
						<h2>{this.state.count}</h2>
					</div>
				);
		  }
	}
	//浏览中调用  count.update()只更新h2这块,因为更新的是state下面的变量
	//默认 state 或者 props  变量修改才会re-render,如果render()依赖其它数据，可以调用forceUpdate()
	let count=ReactDOM.render(
        <Count id="one"></Count>,
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
		}.bind(this), 100); // function (){格式一定要bind(this),否则this.state找不到,不是React的this,而是JS的this,或者使用Lambda就不用bind(this)
	  }

	  render() {
		return ( 
		<div  style={{opacity: this.state.opacity /* 用双大括号*/}}>  
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
		return ( //可嵌套自定义标签
		<div>
			<Comment author="lisi"> this is lisi comment</Comment>
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

=============React Native 
写JS会生成 iOS 和 Android 的本地代码

npm install -g create-react-native-app
create-react-native-app AwesomeProject //生成的node_modules目录非常大,40多M，1万多个文件 不适用

cd AwesomeProject
npm start

