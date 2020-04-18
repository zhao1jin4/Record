Vue(发音像view) 最新 v2.6.11

 最早在 0.6.0 版本 在 Dec 7, 2013 发布 晚于react发布5个多月的时间
 1.0.0-alpha.1   在Aug 31, 2015  发布
 v2.0.0-alpha.1 在 Jun 10, 2016 发布
 
轻量级,中国人开发的

weex 是阿里巴巴 开发的 VUE版的React Native，已成为 apache 项目，v0.26

Vuex 类似react的redux，是吸收了Redux的经验
Vue Router 像 react router
Vue CLI 3+ 内部使用了 webpack

UI框架 
	easyUI for Vue 
	
	Keen UI  (Material Design UI)
	Vue Material	 https://vuematerial.io/
	
	VueStrap
	BootstrapVue 	 https://bootstrap-vue.js.org/
	
	Ant Design of Vue  蚂蚁金服 
	element UI 饿了么 


Vue 比React简单 (没有用到class)

vue-js-devtools 对firefox addons 最新的是 vuejs_devtools-5.1.1-fx.xpi 
也有chrome的

https://cn.vuejs.org/js/vue.js
https://unpkg.com/vue@2.6.11/dist/vue.js
官方下载vue.js 或 vue.min.js (2.6.10版本大小 93.7K,相比react-16.8.6的react要小一点， react.production.min.js 大小12.7K,react-dom.production.min.js 大小108KB)
也可 npm install vue -g   (2.6.11 更新频率不如react快)

生产版本删除了警告
<script type="text/javascript" src="vue-2.6.10/vue.js"></script>

一个兼容 ES Module 的构建文件
<script type="module">
  import Vue from 'https://cdn.jsdelivr.net/npm/vue@2.6.11/dist/vue.esm.browser.js'
</script>
 



<div id="app">
  {{ message }}
</div>

<div id="app-2">
  <span v-bind:title="message">
   鼠标悬停几秒钟查看此处动态绑定的提示信息！
  </span>
</div>
<!-- 
 v-bind是VUE指令(directive)
-->
<div id="app-3">
  <span v-if="seen">Now you see me</span>
</div>
<!-- v-if  v-for -->
<div id="app-4">
  <ol>
    <li v-for="todo in todos">
      {{ todo.text }}
    </li>
  </ol>
</div>
<!-- v-on事件处理 -->
<div id="app-5">
  <p>{{ message }}</p>
  <button v-on:click="reverseMessage">Reverse Message</button>
</div>


<!-- v-model 双向绑定 表单输入值后(修改变量)立即显示  (比react强)-->
<div id="app-6">
  <p>{{ message }}</p>
  <input v-model="message">
</div>

<script type="text/javascript">
var app = new Vue({
	  el: '#app',
	  data: {
	    message: 'Hello Vue!'
	  }
	})
	
var app2 = new Vue({
	  el: '#app-2',
	  data: {
	    message: 'You loaded this page on ' + new Date().toLocaleString()
	  }
	})
//可以打开浏览器console 修改变量值 app.message=1111，页面立即刷新
var app3 = new Vue({
  el: '#app-3',
  data: {
    seen: true
  }
})
//放在data 中
//修改变量值 app3.seen=false ,数据绑定到DOM
var app4 = new Vue({
  el: '#app-4',
  data: {
    todos: [
      { text: 'Learn JavaScript' },
      { text: 'Learn Vue' },
      { text: 'Build something awesome' }
    ]
  }
})
//app4.todos.push({ text: 'New item' })

//methods传方法
var app5 = new Vue({
  el: '#app-5',
  data: {
    message: 'Hello Vue.js!'
  },
  methods: {
    reverseMessage: function () {
      this.message = this.message.split('').reverse().join('')
    }
  }
})

var app6 = new Vue({
  el: '#app-6',
  data: {
    message: 'Hello Vue!'
  }
})
</script>


<!-- 组件 连示例名todo都和react一样的 -->
 
<div id="app-7">
  <ol>
    <!--
      现在我们为每个 todo-item 提供 todo 对象
      todo 对象是变量，即其内容可以是动态的。
     2.2.0+ 的版本里，当在组件上使用 v-for 时，key是必须(也和react一样)
    -->
    <todo-item
      v-for="item in groceryList"
      v-bind:todo="item"
      v-bind:key="item.id"
    ></todo-item>
  </ol>
</div>
 
<script type="text/javascript">  

//定义名为 todo-item 的新组件， props(也和react类似)加自己的属性
Vue.component('todo-item', {
  props: ['todo'],
  template: '<li>{{ todo.text }}</li>'
})

var app7 = new Vue({
  el: '#app-7',
  data: {
    groceryList: [
      { id: 0, text: 'Vegetables' },
      { id: 1, text: 'Cheese' },
      { id: 2, text: 'Whatever else humans are supposed to eat' }
    ]
  }
})
</script>

 还在draf阶段 的 Web 组件规范  https://www.w3.org/wiki/WebComponents/



<div id="app">
  <p>{{ foo }}</p>
  <!-- freeze `foo` 不会更新 -->
  <button v-on:click="foo = 'baz'">Change it</button>
</div>

<div id="example">{{a}}</div>

<div id="test"> 
	<span v-once>v-once 如数据变，显示不会改变: {{ msg }} ,比react强(没有state中数据变不要UI变)</span>
	<p>Using mustaches: {{ rawHtml }} ,Vue默认是安全的</p>
	<p>Using v-html directive: <span v-html="rawHtml"></span></p>
	
	
	<p>number+1={{ number + 1 }} </p>
	<p>ok={{ ok ? 'YES' : 'NO' }}</p>
	<p>message reverse={{ message.split('').reverse().join('') }}</p>
	<div v-bind:id="'list-' + id">动态ID</div>
</div>

<script type="text/javascript"> 
var obj = { foo: 'bar' }
//是使用 Object.freeze()，这会阻止修改现有的属性，也意味着响应系统无法再追踪变化。
Object.freeze(obj) 
new Vue({
  el: '#app',
  data: obj
})
 
//-------- 
var data = { a: 1 }
var vm1 = new Vue({
  el: '#example',
  data: data
})

vm1.$data === data // => true
vm1.$el === document.getElementById('example') // => true

// $watch 是一个实例方法
vm1.$watch('a', function (newValue, oldValue) {
  // 这个回调将在 `vm1.a` 改变后调用
  console.log("newValue="+newValue+",oldValue="+oldValue)
})
//console中修改vm1.a=2 



//---- 生命周期 
 
new Vue({
  data: {
    a: 1
  },
  // created方法在实例被创建之后执行 
  	created: function () {
    // `this` 指向 vm 实例
    //不要使用箭头函数，比如 created: () => console.log(this.a) 箭头函数并没有 this(和react相反)
    console.log('a is: ' + this.a)
  }
})
// => "a is: 1"

/*  其它函数 (类似React)
    			beforeCreate -> created->
vm.$mount(el)-> beforeMount -> 建立vm.$el   ->mounted->
  					beforeUpdate->updated ->
vm.$destory() ->beforeDestroy-> destroyed->
*/

//--
var test= new Vue({
	  el: '#test',
	  data: {rawHtml:"<b>粗体不安全</b>",
		  	msg:"常量1",
			message:"正反",
			number:100,
			ok:undefined,
			id:22
		  }
	})
//console中 test.msg="aaa" 

</script>


 --- 2.6.0 新功能  动态参数 
 <a v-bind:[attributeName]="url"> ... </a>
 如Vue 实例有一个 data 属性 attributeName，其值为 "href"，那么这个绑定将等价于 v-bind:href

 <a v-on:[eventName]="doSomething"> ... </a>
 当 eventName 的值为 "focus" 时，v-on:[eventName] 将等价于 v-on:focus

 动态参数 要求结果是一个字符串，特殊的 null 值可以被显性地用于移除绑定

 不能有空格和引号，如<a v-bind:['foo' + bar]="value"> ... </a>  是不行的 

 <a v-bind:[someAttr]="value"> ... </a>  在 DOM 中使用模板时这段代码会被转换为 `v-bind:[someattr]` (全部强制转为小写)

 ---  修饰符 (modifier) 是以半角句号 . 
 <form v-on:submit.prevent="onSubmit">...</form> 
 表示 .prevent 修饰符告诉 v-on 指令对于触发的事件调用 event.preventDefault()


---构建由 Vue 管理所有模板的单页面应用程序 (SPA - single page application) 时 v-bind 和 v-on 提供了特定简写
<!-- 完整语法 -->
<a v-bind:href="url">...</a> 
<!-- 缩写 -->
<a :href="url">...</a>

<!-- 完整语法 -->
<a v-on:click="doSomething">...</a> 
<!-- 缩写 -->
<a @click="doSomething">...</a>




<div id="computeDiv"> 
  <p>Original message: "{{ message }}"</p>
  <p>Computed reversed message: "{{ reversedMessage }}"</p>
  <p>method reversed message: "{{ me_reversedMessage() }}"</p>
</div> 
<script type="text/javascript"> 
var vm = new Vue({
  el: '#computeDiv',
  data: {
    message: 'Hello'
  },
  computed: {
  	//计算属性 message 还没有发生改变，多次访问 reversedMessage 计算属性会立即返回之前的计算结果，而不必再次执行函数
    // 计算属性的 getter
    reversedMessage: function () {
      // `this` 指向 vm 实例
      return this.message.split('').reverse().join('')
    }
  },
  methods: {//不能和computed组函数同名
    me_reversedMessage: function () {
      return this.message.split('').reverse().join('')
    }
  }
})
</script>


watchDiv:<div id="watchDiv">{{ fullName }}</div>
<br/>
computeSetDiv:<div id="computeSetDiv"> {{ fullName }} </div>

<script type="text/javascript">
//这种方法不如computed方式好 
var vm2 = new Vue({
  el: '#watchDiv',
  data: {
    firstName: 'Foo',
    lastName: 'Bar',
    fullName: 'Foo Bar'
  },
  watch: {
    firstName: function (val) {
      this.fullName = val + ' ' + this.lastName
    },
    lastName: function (val) {
      this.fullName = this.firstName + ' ' + val
    }
  }
});
//console中修改 vm2.firstName='li' 

var vm3 = new Vue({
  el: '#computeSetDiv',
  data: {
    firstName: 'Foo',
    lastName: 'Bar',
    //fullName: 'Foo Bar'//和computed方法同名报警告
  },
	computed: {
	  fullName: {
	    // getter
	    get: function () {
	      return this.firstName + ' ' + this.lastName
	    },
	    // setter
	    set: function (newValue) {
	      var names = newValue.split(' ')
	      this.firstName = names[0]
	      this.lastName = names[names.length - 1]
	    }
	  }
	}
});
//console中修改 vm3.fullName='li si' 

<!-- 属性是样式类名，值真/假表示是否要  这个样式类名 -->
<div id="innerClassDiv" class="static"  v-bind:class="{ active: isActive, 'text-danger': hasError }" >class样式 内部对象 </div>
<div id="outObjClassDiv" v-bind:class="classObject">class 样式 外部对象</div>
<div id="arrayClassDiv" >
	<div v-bind:class="[activeClass, errorClass]">class 样式 数组式，变量值是样式名
		<br/> 对象为{activeClass}写法时(react中有),全写为{activeClass:activeClass}  
	</div>
	<div v-bind:class="[isActive ? activeClass : '', errorClass]">class样式 数组中有?表达式</div>
	<div v-bind:class="[{ active: isActive }, errorClass]">class 样式 混合用法</div>
</div>
<div id="componentClass">
	<my-component class="baz boo"></my-component>  	<!--把所有class组合一起，结果为 <p class="foo bar baz boo">Hi</p> -->
	<my-component v-bind:class="{ active: isActive }"></my-component> <!-- 结果为 <p class="foo bar active">Hi</p> -->
</div>
<div id="styleDiv"> 
	<!--  v-bind:style 功能同 v-bind:class-->
	<div v-bind:style="{ color: activeColor, fontSize: fontSize + 'px' }">style样式 内部对象 驼峰式</div>
	<div v-bind:style="{ color: activeColor, 'font-size': fontSize + 'px' }">style样式 内部对象  短横线 式，外加''</div>
	<div v-bind:style="styleObject">style样式 外部对象</div>
	<div v-bind:style="[baseStyles, overridingStyles]">style样式 数组</div>
	<div :style="{ display: ['-webkit-box', '-ms-flexbox', 'flex'] }">2.3.0 版本开始，只数组中最后一个被浏览器支持的值(v-bind被省)</div>
 
</div>
<script type="text/javascript"> 
 new Vue({
  el: '#innerClassDiv', 
   data: {
    isActive: true,
    hasError: false
  } 
    
}); 
 new Vue({
	  el: '#outObjClassDiv',  
	data: {
		  classObject: {
		    active: true,
		    'text-danger': false
		  }
		}
	}); 
 new Vue({
	  el: '#arrayClassDiv',  
	  data: {
		  activeClass: 'active',
		  errorClass: 'text-danger',
		  isActive:true
		} 
	}); 

 Vue.component('my-component', {
	  template: '<p class="foo bar">class组件样式</p>'
	});
 new Vue({
	  el: '#componentClass',  
	  data: {
		    isActive: true 
	  }
	}); 
 
new Vue({
	  el: '#styleDiv',  
	  data: 
	  { 
	    activeColor:'#0000ff',
	    fontSize:20,
	    styleObject: {
	        color: 'red',
	        fontSize: '13px'
	      },
	      baseStyles:{
	    	  color: 'red',
		      fontSize: '13px'
	      },overridingStyles:
	      {
	    	  color: 'yellow',
	      }
	  }
	}); 
</script>
	



<div id="conditionDiv">
 	<h1 v-if="awesome">Vue is awesome!</h1>
	<h1 v-else>Oh no  </h1>
	
	<template v-if="ok">
	  <h1>Title</h1>
	  <p>Paragraph 1</p>
	  <p>Paragraph 2</p>
	</template>

	<div v-if="Math.random() > 0.5">
	  Now you see me
	</div>
	<div v-else>
	  Now you do not
	</div>
	<!-- v-else 元素必须紧跟在带 v-if 或者 v-else-if 的元素的后面，否则它将不会被识别。 -->
	
	<div v-if="type === 'A'">
	  A
	</div>
	<div v-else-if="type === 'B'">
	  B (v-else-if 2.1.0 新增)
	</div>
	<div v-else-if="type === 'C'">
	  C
	</div>
	<div v-else>
	  Not A/B/C
	</div>
	 切换 loginType 将不会清除用户已经输入的内容,因为两个模板使用了相同的input元素  <br/> 
	<template v-if="loginType === 'username'">
	  <label>Username</label>
	  <input placeholder="Enter your username">
	</template>
	<template v-else>
	  <label>Email</label>
	  <input placeholder="Enter your email address">
	</template>
	<button @click="toggleLoginType()">切换loginType</button>
	
	
	<br/>增加唯一key属性后,替换了组件,清除用户已经输入的内容<br/> 
	<template v-if="loginType === 'username'">
	  <label>Username</label>
	  <input placeholder="Enter your username" key="username-input">
	</template>
	<template v-else>
	  <label>Email</label>
	  <input placeholder="Enter your email address" key="email-input">
	</template>
	
	<!-- v-show 不支持 <template> 元素，也不支持 v-else 
	v-if 也是惰性的：如果在初始渲染时条件为假，则什么也不做——直到条件第一次变为真时，才会开始渲染条件块。
	-->
	<h3 v-show="ok">v-show元素始终会被渲染并保留在DOM中,只是简单地切换元素的CSS属性display, v-if第一次变为真时,才会开始渲染 </h3>
</div>
<div id="forDiv"> 
	 v-for可加在第二列加index   <br/>
	  <li v-for="(item, index) in items">
	    {{ parentMessage }} - {{ index }} - {{ item.message }}
	  </li>
	 
	 of 同 in   <br/>
	<div v-for="item of items">{{item.message}}</div>
	
	<!--  当不是数组，而是对象时，取的是属性的值  -->
	<li v-for="value in object">
		{{ value }}
	</li>
	
	  是对象时可选第二个参数是属性名 <br/>
	<div v-for="(value, name) in object">
		{{ name }}: {{ value }}
	</div>
	 是对象时可选第三个参数索引 <br/>
	<div v-for="(value, name, index) in object">
	  {{ index }}. {{ name }}: {{ value }}
	</div>
 	会按 Object.keys() 的结果遍历，不能保证顺序
 	建议尽可能在使用 v-for 时提供 key<br/>
 
 	<!-- 用自定义的函数even-->
	偶数<li v-for="n in even(numbers)">{{ n }}</li>
 
  1到10结果为：<span v-for="n in 10">{{ n }} </span>
  
   <br/>template上可以用for
	 <ul>
	  <template v-for="item in items">
	    <li>{{ item.message }}</li> 
	  </template>
	</ul>
	for和if一起用,同一节点v-for 的优先级比 v-if 更高
	<ol>
		<li v-for="item in items" v-if="item.message!='Foo'">
		  {{ item.message }}
		</li>
	</ol>
	动态增加属性age :{{object.age}}
</div>

<script type="text/javascript">
v=new Vue({
	  el: '#conditionDiv',
	  data: {
		  awesome:true,
		  ok:true,
		  type:'B',
		  loginType:'username'
	  },
	  methods:
	  {
		  toggleLoginType : function()
		  {
			  if(this.loginType == 'username')
			  {
				  this.loginType ='password'
			  }else
			  {
				  this.loginType  = 'username'
			  }
		  }
	  }
	}) 
	// console中 v.loginType='x' 
	 
new Vue({
	  el: '#forDiv',
	  data: {
	    parentMessage: 'Parent',
	    items: [
	      { message: 'Foo' },
	      { message: 'Bar' },
	      { message: 'Foo1' },
	      { message: 'Bar1' }
	    ],
		object: 
		{
	      title: 'How to do lists in Vue',
	      author: 'Jane Doe',
	      publishedAt: '2016-04-10'
	    },
	    numbers: [ 1, 2, 3, 4, 5 ]
	  }, 
	  methods: {
		  even: function (numbers) {
		    return numbers.filter(function (number) {
		      return number % 2 === 0
		    })
		  }
		} 
	});

	
</script>

/*
	JS原始方法 filter()、concat() 和 slice() 。它们不会改变原始数组，而总是返回一个新数组
	Vue 不能检测两种数组的变动(由于 JavaScript 的限制)，
	1.vm.items[indexOfItem] = newValue 
		解决方法 Vue.set(vm.items, indexOfItem, newValue) 或 vm.items.splice(indexOfItem, 1, newValue)
		 		vm.$set(vm.items, indexOfItem, newValue) //是全局方法 Vue.set 的一个别名
	2.修改数组的长度时 如vm.items.length = newLength
		解决方法 vm.items.splice(newLength)//测试无效??? JS 的 splice没有扩展数组的功能
	*/
	console.log([0,1,2,3,4].splice(1,1,11));//JS 的 splice没有第三参数的功能(不报错,只忽略),但vue有
/*
	//console中写入 
	//v1.items.filter(i=>i.message == 'Foo');//VUE不会重新渲染
	//v1.items.concat( [{message:'AA'},{message:'BB'}]);//VUE不会重新渲染
	//v1.items.length=1;//VUE不会重新渲染
	
	v1.items.splice(1);//从第1个位置删到尾 (js功能返回的数据做了删除)
	v1.items.splice(2, 1)//从第2个位置删1个(js功能返回的数据做了删除)
	v1.items.splice(1, 1, {message:'XXX'});//VUE会重新渲染(三参数是vue的方法)
	Vue.set(v1.items, 1, {message:'YYY'});
	v1.$set(v1.items, 1, {message:'ZZZ'});
*/	
	console.log([0,1,2,3,4].splice(8));//JS 的 splice没有扩展数组的功能 
	//v1.items.splice(8);//测试无效 ??? v1.items还是原来的长度 
	
	//v1.$set(v1.items, 4, {message:'AAA'});//但递增一个(位置要紧要最后)是可以的
		
	//Vue 不允许动态添加根级别的响应式属性,可以用 Vue.set(v1.object, 'age', 27) 或 v1.$set(v1.object, 'age', 27)
	//v1.object.age = 2 这种是不行的
	
	/*
	//用 Object.assign() 创建一个新的对象
	v1.object = Object.assign(
			{},   //第一个参数要为空才行
			v1.object, 
			{ age: 27 }); 
	//Object.assign(v1.object, {  age: 27  }); //这样不行的
	*/
	
	
	

完整的todo例子：
<my-component
  v-for="(item, index) in items"
  v-bind:item="item"
  v-bind:index="index"
  v-bind:key="item.id"
></my-component> 
<div id="todo-list-example">
  <form v-on:submit.prevent="addNewTodo">
    <label for="new-todo">Add a todo</label>
    <input
      v-model="newTodoText"
      id="new-todo"
      placeholder="E.g. Feed the cat"
    >
    <button>Add</button>
  </form>
  <ul>
 <!--  
 <li is="todo-item"  效果与 <todo-item>相同 ,但是可以避开一些潜在的浏览器解析错误  
 自定义 remove1 事件 
-->  
   <li
      is="todo-item"
      v-for="(todo, index) in todos"
      v-bind:key="todo.id"
      v-bind:title="todo.title"
      v-on:remove1="todos.splice(index, 1)"
    ></li>
  </ul>
</div> 
<script type="text/javascript">
 
Vue.component('todo-item', {
	 //定义产生 remove1 事件(使用$emit)
	  template: '\
	    <li>\
	      {{ title }}\
	      <button v-on:click="$emit(\'remove1\')">Remove</button>\
	    </li>\
	  ',
	  props: ['title']
	})

	new Vue({
	  el: '#todo-list-example',
	  data: {
	    newTodoText: '',
	    todos: [
	      {
	        id: 1,
	        title: 'Do the dishes',
	      },
	      {
	        id: 2,
	        title: 'Take out the trash',
	      },
	      {
	        id: 3,
	        title: 'Mow the lawn'
	      }
	    ],
	    nextTodoId: 4
	  },
	  methods: {
	    addNewTodo: function () {
	      this.todos.push({
	        id: this.nextTodoId++,
	        title: this.newTodoText
	      })
	      this.newTodoText = ''
	    }
	  }
	})
</script>
	
--事件
<div id="myEventDiv">
	<!--  访问原始的 DOM 事件 ,用特殊变量 $event，v-on:缩写为@  -->
	<button @click="warn('Form cannot be submitted yet.', $event)">
	  Submit
	</button>
</div>
	
 new Vue({
		  el: '#myEventDiv',
		  methods: {
			  warn: function (message, event) {//$event传来的原生事件对象 
			    if (event) event.preventDefault()
			    alert(message)
			  }
			}
	 });
/* 
.stop      (event.stopPropagation())
.prevent	(event.preventDefault())
.capture   (addEventListener 中的 capture 选项)
.self
.once     (addEventListener 中的 once 选项)
.passive  (addEventListener 中的 passive 选项 如为true表示不会调用 preventDefault())
 
v-on:click.prevent.self 会阻止所有的点击，而 v-on:click.self.prevent 只会阻止对元素自身的点击

<!-- 阻止单击事件继续传播 -->
<a v-on:click.stop="doThis"></a>

<!-- 提交事件不再重载页面 -->
<form v-on:submit.prevent="onSubmit"></form>

<!-- 修饰符可以串联 -->
<a v-on:click.stop.prevent="doThat"></a>

<!-- 只有修饰符 -->
<form v-on:submit.prevent></form>

<!-- 添加事件监听器时使用事件捕获模式 -->
<!-- 即元素自身触发的事件先在此处理，然后才交由内部元素进行处理 -->
<div v-on:click.capture="doThis">...</div>

<!-- 只当在 event.target 是当前元素自身时触发处理函数 -->
<!-- 即事件不是从内部元素触发的 -->
<div v-on:click.self="doThat">...</div>

<!-- 点击事件将只会触发一次 -->
<a v-on:click.once="doThis"></a>

addEventListener 中的 passive 选项(默认值是false)，VUE提供了.passive 修饰符
在文档级节点Window,Document,Document.body上，当 touchstart 和 touchmove事件时 Chrome 和 Firefox修改passive默认值为true
.passive 会告诉浏览器你不想阻止事件的默认行为。

<!-- 滚动事件的默认行为 (即滚动行为) 将会立即触发 -->
<!-- 而不会等待 `onScroll` 完成  -->
<!-- 这其中包含 `event.preventDefault()` 的情况 -->
<div v-on:scroll.passive="onScroll">...</div>

<!-- 只有在 `key` 是 `Enter` 时调用 `vm.submit()` -->
<input v-on:keyup.enter="submit">

<input v-on:keyup.page-down="onPageDown">
https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/key/Key_Values 
中的 PageDown 命令风格变为 page-down

可以使用过时的keyCode 
https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/keyCode
<input v-on:keyup.13="submit">

.ctrl
.alt
.shift
.meta 	在Mac对应 command 键，在 Windows 系统键盘Win键
.backspace
.delete
.insert

<!-- Alt + C -->
<input @keyup.alt.67="clear">

<!-- 即使 Alt 或 Shift 被一同按下时也会触发 -->
<button @click.ctrl="onClick">A</button>

<!-- 有且只有 Ctrl 被按下的时候才触发 -->
<button @click.ctrl.exact="onCtrlClick">A</button>

<!-- 没有任何系统修饰符被按下的时候才触发 -->
<button @click.exact="onClick">A</button>

鼠标按钮
.left
.right
.middle

*/


v-model 会忽略所有表单元素的 value，checked，selected  的初始值， 应使用组件的 data 声明初始值
<br/>
多行文本,pre-line表示多个空白合并成一个
<div id="multiDiv">
	<span>Multiline message is:</span>
	<p style="white-space: pre-line;">{{ message }}</p>
	<br>
	<textarea v-model="message" placeholder="add multiple lines">这里的内容被忽略</textarea>
</div>
<script type="text/javascript">
new Vue({
	  el: '#multiDiv',
	  data:{
		  message:''
	  }
	})
</script>

<div id="formDiv">
	<input type="checkbox" id="checkbox" v-model="checked">
	<label for="checkbox">{{ checked }}</label>
 <br/>
  <input type="checkbox" id="jack" value="Jack" v-model="checkedNames">
  <label for="jack">Jack</label>
  <input type="checkbox" id="john" value="John" v-model="checkedNames">
  <label for="john">John</label>
  <input type="checkbox" id="mike" value="Mike" v-model="checkedNames">
  <label for="mike">Mike</label>
  <br>
  <span>Checked names: {{ checkedNames }}</span>
<br/>
  <input type="radio" id="one" value="One" v-model="picked">
  <label for="one">One</label>
  <br>
  <input type="radio" id="two" value="Two" v-model="picked">
  <label for="two">Two</label>
  <br>
  <span>Picked: {{ picked }}</span>
<br/>
   <select v-model="selected">
    <option disabled value="">请选择</option>
    <option>A</option>
    <option>B</option>
    <option>C</option>
  </select>
  <span>Selected: {{ selected }}</span>
</div>
<script type="text/javascript">
new Vue({
	  el: '#formDiv',
	  data:{
		  checkedNames: [],
		  checked:false,
		  picked: '',
		  selected: ''
	  }
	})
</script>

<div id="multiSelectDiv">
按ctrl多选
  <select v-model="selected" multiple style="width: 50px;">
    <option>A</option>
    <option>B</option>
    <option>C</option>
  </select>
  <br>
  <span>Selected: {{ selected }}</span>
</div>

<script type="text/javascript">
	new Vue({
	  el: '#multiSelectDiv',
	  data: {
	    selected: []
	  }
	})
</script>

<div id="selectArrayDiv">
	<select v-model="selected">
	  <option v-for="option in options" v-bind:value="option.value">
	    {{ option.text }}
	  </option>
	</select>
	<span>Selected: {{ selected }}</span>
	<br/>
	click event:true-value 和 false-value 是Vue的功能
	<input
	  type="checkbox"
	  v-model="toggle"
	  true-value="yes"
	  false-value="no"
	  @click="myOncheck" > <!-- true-value 和 false-value 是Vue的功能 -->
	  
</div>
<script type="text/javascript">
	new Vue({
	  el: '#selectArrayDiv',
	  data: {
		    selected: 'A',
		    options: [
		      { text: 'One', value: 'A' },
		      { text: 'Two', value: 'B' },
		      { text: 'Three', value: 'C' }
		    ],
		    toggle:'yes',
		  },
		  methods:{
			  myOncheck:function(evt){
				  console.log(this.toggle)
			  }
		  }
	})
</script>


<div id="otherFormDiv">
	v-bind:value="变量名"  替代	 v-model 	<input type="radio" v-model="pick" v-bind:value="a"  @click="myOncheck" >
	<br/>
	v-bind:value="对象" 
	<select v-model="selected">  <!-- 内部是对象变量 -->
	  <option v-bind:value="{ number: 123 }">123</option>
	  <option v-bind:value="{ number: 456 }">456</option>
	</select>
	selected={{selected}} <br/>
	数字:<input v-model.number="age" type="number"> <br/>
	去空格:<input v-model.trim="msg"> <br/>
	失去焦点或回车键时才更新<input v-model.lazy="msg" > msg:{{msg}}
</div>
<script type="text/javascript">
//当选中时 vm.pick值被修改为 vm.a的值
	vm=new Vue({
	  el: '#otherFormDiv',
	  data: {
		    a: '1',
		    pick:'2',
		    selected:undefined,
		    msg:'a',
		    age:20
	  },
	  methods:{
		  myOncheck:function(evt){
			console.log(this.a)
			console.log(this.pick) 
		  }
	  }
	});
</script>

<div id="components-demo">
   如组件多次复用，data要是一个函数，每个组件才有自己的值
  <button-counter></button-counter>
  <button-counter></button-counter>
  <button-counter></button-counter> 
</div>
<script type="text/javascript">
	Vue.component('button-counter', { 
		  data:function () { //如组件多次复用，这要是一个函数，每个组件才有自己的值
		    return {
		      count: 0
		    }
		  },
		  template: `
		  	<button v-on:click="count++">
		  		You clicked me {{ count }} times.
		  	</button>` //使用`` 加字串，可以多行，但不被IE支持
	});
	new Vue({ el: '#components-demo' });
</script>


<div id="eventParamDiv"> 
	自定义事件传参数
	 <div :style="{ fontSize: postFontSize + 'em' }">
<!-- 
		 v-on:xxx="" 以通过 $event 访问到被$emit抛出的事件参数 
	    <blog-post
	      v-for="post in posts"
	      v-bind:key="post.id"
	      v-bind:post="post"
	       v-on:enlarge-text="postFontSize += $event"
	    ></blog-post> 
 二选一
 -->
	    接收事件也可使用函数,函数参数 接收 事件参数 
		<blog-post
	      v-for="post in posts"
	      v-bind:key="post.id"
	      v-bind:post="post"
	     v-on:enlarge-text="onEnlargeText"
	    ></blog-post>
	  </div>
</div>
 <script type="text/javascript">
 Vue.component('blog-post', {
	  props: ['post'],
	  //$emit('enlarge-text', 0.1) 表示 生产事件带参数0.1
	  template: `
	    <div class="blog-post">
	      <h3>{{ post.title }}</h3>
	      <button v-on:click="$emit('enlarge-text', 0.1)">Enlarge text</button>
	      <div v-html="post.content"></div>
	    </div>`
	});
new Vue({ 
	 el: '#eventParamDiv' ,
	 data: {
	    posts: [{id:1,title:'标题1',content:'内容1'},{id:2,title:'标题2',content:'内容2'}],
	    postFontSize: 1
	  },
	methods: {
	  onEnlargeText: function (enlargeAmount) //方法式来接收$emit产生事件带的参数
	  {
	    this.postFontSize += enlargeAmount
	  }
	}
});

<div id="myComponentModelDiv">
 	v-model用于自定义组件(非自定义事件,$event是原生的JS event)<br/>
	<input v-model="searchText">
	等价于(react)：
	<input
	  v-bind:value="searchText"
	  v-on:input="searchText = $event.target.value"
	>
	<br/>
	自定义组件使用变形(自定义组件 以通过 $event 访问到$emit被抛出的事件参数 )
	<custom-input
	  v-bind:value="searchText"
	  v-on:input="searchText = $event"
	></custom-input>
	自定义组件 如也要 使用v-module(template中要用加value属性(props),加input事件)
	<custom-input v-model="searchText"></custom-input>
 </div>
 <script type="text/javascript">
	Vue.component('custom-input', {
	  props: ['value'],
	  template: `
	    <input
	      v-bind:value="value"
	      v-on:input="$emit('input', $event.target.value)"
	    >
	  `
	});
	new Vue({ 
		 el: '#myComponentModelDiv' ,
		 data: {
			 searchText:''
		 }
	});
</script>


is用法 
<table>
  <tr is="blog-post-row"></tr>
</table>

以下不存在这种限制
template: '...'
.vue 文件
<script type="text/x-template">


 <hr/> Props <br>
 HTML不区分大小写，浏览器会把所有大写字符解释为小写字符
 <h1>小写h1</h1>
 <H1>大写h1</H1>
camelCase (驼峰命名法) 的 prop 名需要使用其等价的 kebab-case (短横线分隔命名) 命名
<div id="propsDiv">
	<!-- 在 HTML 中是 kebab-case 的 -->
	<blog-post post-title="hello!"></blog-post>
</div>
<script type="text/javascript"> 
Vue.component('blog-post', {
  // 在 JavaScript 中是 camelCase 的
  props: ['postTitle'],
  template: '<h3>{{ postTitle }}</h3>'
});
new Vue({ el: '#propsDiv' }); 

</script>


<div id="propsDiv1">  
	<blog-post1  v-for="post in posts" 
      v-bind:key="post.id"
      v-bind:likes="post.likes"
	  v-bind:post-title="post.title + ' by ' + post.author.name"
	  v-bind:is-published="post.isPublished"
	  v-bind:comment-ids="[234, 266, 273]"
	  v-bind:author="{
		    name: 'Veronica',
		    company: 'Veridian Dynamics'
		  }"
	></blog-post1>
	传入一个对象的所有属性 等价于 (key属性对不上)：
	<blog-post1  v-for="post in posts"  v-bind="post" ></blog-post1>
</div>

<script type="text/javascript"> 
	Vue.component('blog-post1', { 
		//以数组式形式传多个值 
		//props: ['postTitle', 'likes', 'isPublished', 'commentIds', 'author'],
		//以对象形式，value是类型
		props: {
			  postTitle: String,
			  likes: Number,
			  isPublished: Boolean,
			  commentIds: Array,
			  author: Object,
			  callback: Function,
			  contactsPromise: Promise // or any other constructor
			}, 
		template: `<h3>{{ postTitle }}  likes: {{likes}} isPublished: {{isPublished}} 
			commentIds:{{commentIds}} author.name={{author.name}}</h3> `
	 });
	new Vue({ el: '#propsDiv1' ,
			data: {
			    posts: [
			      { id: 0, title: 'myTitle0',author:{name:'lisi'},likes:10,isPublished:true },
			      { id: 1, title: 'myTitle1' ,author:{name:'wang'},likes:20,isPublished:false },
			      { id: 2, title: 'myTitle2',author:{name:'zhang'},likes:30,isPublished:true},
			    ]
			}
		}); 
//父级 prop 的更新会向下流动到子组件中，但是反过来则不行,每次父级组件发生更新时，子组件中所有的 prop 都将会刷新为最新的值
//在 JavaScript 中对象和数组是通过引用传入的，所以对于一个数组或对象类型的 prop 来说，在子组件中改变这个对象或数组本身将会影响到父组件的状态。
/* 
 props: ['initialCounter'],
 data: function () {//data可以是一个函数
	  return {
	    counter: this.initialCounter//可以访问到props中的值(父级件)
	  }
	}
 */
</script>

<hr/>
验证props (类似react的propTypes,要单独安装,现在建议用Flow)
使用开发环境构建版本Vue,将会产生一个控制台的警告 
<div id="propsValid">
<!--  正确的 -->
<my-component  v-bind:prop-a="myNum"  prop-b="123" prop-c="abc" v-bind:prop-e="myObject"  prop-f="success" v-bind:author="myPersion"/>
 <!--  错误的   <my-component prop-a="abc"  v-bind:prop-b="myArray"  prop-f="su" />    -->
</div>

<script type="text/javascript">
	//type 可以是下列原生构造函数中的一个： String,Number,Boolean,Array,Object,Date,Function,Symbol
	function Person (firstName, lastName) {
	  this.firstName = firstName
	  this.lastName = lastName
	}
	Vue.component('my-component', {
	  props: {
	    // 基础的类型检查 (`null` 和 `undefined` 会通过任何类型验证)
	    propA: Number,
	    // 多个可能的类型
	    propB: [String, Number],
	    // 必填的字符串
	    propC: {
	      type: String,
	      required: true
	    },
	    // 带有默认值的数字
	    propD: {
	      type: Number,
	      default: 100
	    },
	    // 带有默认值的对象
	    propE: {
	      type: Object,
	      // 对象或数组默认值必须从一个工厂函数获取
	      default: function () {
	        return { message: 'hello' }
	      }
	    },
	    // 自定义验证函数
	    propF: {
	      validator: function (value) {
	        // 这个值必须匹配下列字符串中的一个
	        return ['success', 'warning', 'danger'].indexOf(value) !== -1
	      }
	    },
	    author: Person//自己的类型,通过 instanceof 来进行检查确认
	  },
	 template: `<span> props validation defaultValue={{propD}} , propE={{propE.message}} ,author={{author.firstName}} </span>`
	});
	new Vue({ el: '#propsValid' ,
		data: {
			myNum:123,
			myArray:[1,2,3],
			myObject:{message:'world'},
			myPersion:new Person('li','si'),
		}, 
	});
/*
	非 prop 的 attribute 
	如自定义组件<bootstrap-date-input> 组件，这个插件需要在其 <input> 上用到一个 data-date-picker attribute
	那么这样写<bootstrap-date-input data-date-picker="activated"></bootstrap-date-input>
	然后这个 data-date-picker="activated" attribute 就会自动添加到 <bootstrap-date-input> 的根元素上。
	
	大多数 attribute 来说，从外部提供给组件的值会替换掉组件内部设置好的值
	只有样式(class 和 style)，组件自己会合并父级的(如 父级传入的主题)
	
	可在组件的选项中设置 inheritAttrs: false (不会影响 style 和 class )
	$attrs 该属性包含了传递给这个组件的  attribute名 和 attribute值
*/
</script>

inheritAttrs 和 $attrs <br/>
<div id=attrsDiv>
	<base-form title="form提示" style="background-color:#00FF00;font-size:2rem">
		 v-model 的 input事件，都是自身组件发出，都是自身组件监听  见下面代码<br/>
		 slot做的嵌套组件<br/>
		<base-input
		  v-model="username"
		  required
		  placeholder="Enter your username"
		></base-input>
	</base-form> 
</div>
<script type="text/javascript">
//用后面的slot
Vue.component('base-form', {
	 template: `<form   v-bind="$attrs">
	 	 <slot></slot>
		</form>`
});
Vue.component('base-input', {
	   inheritAttrs: false,//不希望组件的根元素继承特性(label没有required),不会影响 style 和 class 的绑定
	  //inheritAttrs: true,//(label有required),base-form 的 title没继承下来
	  
	  props: ['label', 'value'],
	  
	  //$attrs  表示接收外部所有属性，即placeholder属性，required属性 
	  // v-model 的 input事件，都是自身组件发出，都是自身组件监听  见下面代码
	  template: `
	    <label>
	      {{ label }}
	      <input
	        v-bind="$attrs"
	        v-bind:value="value"
	        v-on:input="$emit('input', $event.target.value)"
	      >
	      the input value={{value}}
	    </label>
	  `
	}) 
new Vue({ el: '#attrsDiv' ,
	data: { 
		username:"lisi"
	}, 
});
</script>

不同于组件和 prop，事件名不存在任何自动化的大小写转换
v-on 事件监听器在 DOM 模板中会被自动转换为全小写 (因为 HTML 是大小写不敏感的)，所以 v-on:myEvent 将会变成 v-on:myevent<br/>


 v-model 默认会利用名为 value 的 prop 和名为 input 的事件
 这里的 lovingVue 的值将会传入这个名为 checked 的 prop。
 <div id="customEventDiv">
 	<base-checkbox v-model="lovingVue"></base-checkbox>
 </div>
 
 <script type="text/javascript">
 Vue.component('base-checkbox', {
	  model: { //model组 (v-model)
	    prop: 'checked',//默认是value，即props组中的属性名
	    event: 'change'//默认是input
	  },
	  props: {
		//checked 要和model组的prop值相同
	    checked: Boolean
	  },
	  template: `
	  	<span> 
		  <input
		      type="checkbox"
		      v-bind:checked="checked"
		      v-on:change="$emit('change', $event.target.checked)"
		    >
		  checked={{checked}}
		</span>
	  `
	});
 new Vue({ el: '#customEventDiv' ,
		data: {
			lovingVue:true
		}, 
	});
</script>
<br/> 
原始 checkbox事件: <input id="htmlCheckbox" type="checkbox"   checked />  <br/>
<script type="text/javascript">
 const selectElement = document.querySelector('#htmlCheckbox');
 selectElement.addEventListener('change', (event) => {
   console.log(`You like ${event.target.value} checked=${event.target.checked}`) ;//value默认on 可以改
 });
 </script>


监听一个原生事件,用 v-on 的 .native 修饰符
<base-input v-on:focus.native="onFocus"></base-input> 
如根元素不是 input(是label) 就不行了,
$listeners 属性，值是一个对象，里面包含了作用在这个组件上的所有监听器(如 focus: function (event) {})
 
 
<hr/>
slot 2.6版本新改进的功能  
<div id="slotDiv">
	<navigation-link url="/profile">
	  <!-- 使用子组件时取不到父组件props,但在定义子组件时也取不到-->
	  <font-awesome-icon name="user"></font-awesome-icon>
	  Your Profile,自身组件内容区 取不到 自身组件标签变量(但可以取到data中的url 不加报警告)  url= {{url}}
	</navigation-link>
	<my-submit></my-submit>
	<my-submit>save</my-submit> 
</div>

<script type="text/javascript">
Vue.component("navigation-link", {
	 props: ['url'], 
	//<slot></slot>表示在使用时 <navigation-link> 和 </navigation-link> 里的内容
	template: ` <a  v-bind:href="url"  class="nav-link" >
		  	<slot></slot>
		  </a> `
	});
	
Vue.component("font-awesome-icon", {
	 	props: ['name'], 
		   template: ` <span>{{name}} icon</span> `
		});
Vue.component("my-submit", { 
  // <slot>默认值</slot>
  template: ` <button type="submit">
	  <slot>Submit</slot>
	 </button>`
}); 
new Vue({ el: '#slotDiv' ,
		data: { 
			url:'init val in data'//不加报警告
		}, 
	});
</script>

<div id="layoutDiv">
	<base-layout>
	  多个slot按名字匹配,没有放在 template 带名标签 中的默认为default,v-slot只能用在template上(其它过时) 
	  (这点不如react好,子级件在使用时必须知道父组件所有slot名字,这里还一定是按顺序写的)
	  <template v-slot:header>
	    <h1>Here might be a page title</h1>
	  </template>
	
	  <p>A paragraph for the main content.</p>
	  <p>And another one.</p>
	<!-- 或者指名default
	<template v-slot:default>
		<p>A paragraph for the main content.</p>
		<p>And another one.</p>
	</template> 
	-->
	  <template v-slot:footer>
	    <p>Here  some contact info</p>
	  </template>
	</base-layout> 
	
</div>
<script type="text/javascript">
Vue.component("base-layout", { 
	  // <slot>默认的名字为default
	  template: `
		  <div class="container">
		  <header>
		    <slot name="header"></slot>
		  </header>
		  <main>
		    <slot></slot>
		  </main>
		  <footer>
		    <slot name="footer"></slot>
		  </footer>
		</div>
		`
	});
new Vue({ el: '#layoutDiv' ,
	data: { }, 
});
</script>

以下的slot 官方文档使用时没有加 v-bind:user="user"，但测试下来用组件都要加(对应定义中的 props的值 ),template不用,显示值全是一样的(li)?????
<hr/>
<div id="scopeDiv">
	(自身组件内容区 取 标签变量方法 )  作用域插槽 slotProps可以任意名=
	<current-user v-bind:user="user"> 
	  <template v-slot:default="slotProps"> <!-- 修改定义中<slot>中的默认内容, default可赋值, slotProps.user 对应 <slot v-bind:user="user"> --> -->
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
	
	<br/> v-slot:default="slotProps" 可缩写为 v-slot="slotProps",但如有用带名的slot就不能省default=
	<current-user v-bind:user="user"> 
	  <template v-slot="slotProps">
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
	 <br/> 
	v-slot用在使用组件上(被假定对应默认插槽),但如有用带名的slot,就只能用在template上=
	<current-user  v-bind:user="user" v-slot="slotProps">
	  {{ slotProps.user.firstName }}
	</current-user>  
	<br/> 
	 
    v-slot 值为对象{ user } ,结果=
	<current-user v-bind:user="user"  v-slot="{ user }">
	  {{ user.firstName }}
	</current-user>
	
	<br/> 
	 v-slot 值为对象{ user: person } ,结果(user 重命名为 person)= 
	 <current-user   v-bind:user="user" v-slot="{ user: person }">
	  {{ person.firstName }}
	</current-user> 
	 
	
	<br/> v-slot: 替换为字符 #， default不能省略，示例结果=
	 <current-user v-bind:user="user"> 
	  <template #default="slotProps">
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
	
	
</div>


<script type="text/javascript"> 
Vue.component("current-user", {  
	  props: ['user'], 
	  //<slot v-bind:属性 叫 插槽 prop，在使用组件时 就可以赋值一个名字slotProps，<template v-slot:default="slotProps">
	  template: ` 
			<span>
			  <slot v-bind:user="user">
			    {{ user.lastName }}
			  </slot>
			</span> 
		`
	});
new Vue({ el: '#scopeDiv' ,
	data: {
			user:{firstName:"li",lastName:"si"}
		}, 
});

</script>



<div id="slotTodoDiv">
	slot todo示例 
	<todo-list v-bind:todos="todos">  <!-- v-bind对应的是定义中的props里的 -->
	  <template v-slot:todo="{ todo }">
	    <span v-if="todo.isComplete">✓</span>
	    {{ todo.text }}
	  </template>
	</todo-list>

</div>
<script type="text/javascript">
Vue.component("todo-list", {   
	  props: ['todos'],
	  template: ` 
		  <ul>
		    <li
		      v-for="todo in todos"
		      v-bind:key="todo.id"
		    >  
		      <slot name="todo" v-bind:todo="todo"> 
		        {{ todo.text }}
		      </slot>
		    </li>
		  </ul>
		`
	});
new Vue({ el: '#slotTodoDiv' ,
	data: {
		todos: [
			      { id: 0, text: 'Vegetables' ,isComplete:true },
			      { id: 1, text: 'Cheese' },
			      { id: 2, text: 'Whatever else humans are supposed to eat' }
			    ]
		}, 
});
</script>

----
<keep-alive><!-- 失活的组件将会被缓存！  -->
	<!-- component 标签 可修改用 div  v-bind:is 的值是computed的函数名 返回一个组件名称（动态切换组件）-->
  	<component v-bind:is="currentTabComponent" class="dynamic-component-demo-tab"></component>
</keep-alive>

---
 //第二个参数 很像 JS的 Promise (的构造器) API
 Vue.component('async-example', function (resolve, reject) {
	  setTimeout(function () {
	    // 向 `resolve` 回调传递组件定义
	    resolve({
	      template: '<div>I am async!</div>'
	    })
	  }, 1000)
	})
	
Vue.component(
  'async-webpack-example',
  // 这个 `import` 函数会返回一个 `Promise` 对象。
  () => import('./my-async-component') // webpack 的异步 import
)

在每个 new Vue 实例的子组件中，其根实例可以通过 $root 属性进行访问 (不建议用 ,推荐使用 Vuex)<br/>
<div id="rootDiv">
 	{{  $root.foo }}
 	<button @click="$root.foo = 2">修改foo</button>
 	<button @click="baz">调用baz,修改foo</button>
</div>
 <script type="text/javascript"> 
	new Vue({
	  el: '#rootDiv',
	  data: {
	    foo: 1
	  },
	  computed: {
	    bar: function () { /* ... */ }
	  },
	  methods: {
	    baz: function () { 
			console.log("baz");
			this.$root.foo = 3
		}
	  }
	});
	 
</script>
<br/>

$parent 属性可以用来从一个子组件访问父组件的实例 (不建议用)，示例还有$children 

<firstchild ref="f1"></firstchild>
console.log(this.$refs.f1.msg);//和以前的react一样
$refs 只会在组件渲染完成之后生效，应该避免在模板或计算属性中访问 $refs


依赖注入(比react的context好的地方是,没有变更名是必须的写法),有耦合可用 Vuex
provide 选项允许我们指定我们想要提供给后代组件的数据/方法
任何后代组件里，我们都可以使用 inject 选项来接收指定的
<div id="depsInjectDiv"><test></test></div>
<script>
  Vue.component('child',{
    inject:['message'],
    template:'<p>{{message}}</p>'
  })
  Vue.component('test',{
    template:`<div><child></child></div>`
  })
  new Vue({
    el:'#depsInjectDiv',
    provide:{message:'Hello Vue!'}
  })
</script>

Vue 的事件系统不同于浏览器的
通过 $on(eventName, eventHandler) 侦听一个事件
通过 $once(eventName, eventHandler) 一次性侦听一个事件
通过 $off(eventName, eventHandler) 停止侦听一个事件

递归组件就可能导致无限循环 
name: 'stack-overflow',
template: '<div><stack-overflow></stack-overflow></div>'
请确保递归调用是条件性的 (例如使用一个最终会得到 false 的 v-if)

组件之间的循环引用
模块系统发现它需要 A，但是首先 A 依赖 B，但是 B 又依赖 A ....
1.当通过 Vue.component 全局注册组件的时候，这个会被自动解开
2.本地注册组件的时候，你可以使用 webpack 的异步 import：
components: {
  TreeFolderContents: () => import('./tree-folder-contents.vue')
}
 
 
当 inline-template 这个特殊的 attribute 出现在一个子组件上时，这个组件将会使用其里面的内容作为模板(不建议用)
优先选择 template 选项或 .vue 文件里的一个 <template> 元素来定义模板

<script type="text/x-template" id="hello-world-template">
  <p>Hello hello hello</p>
</script> 
Vue.component('hello-world', {
  template: '#hello-world-template'
}) 
x-template 需要定义在 Vue 所属的 DOM 元素外 (会将模板和该组件的其它定义分离开)

极少数的情况下需要手动强制更新，那么你可以通过 $forceUpdate 来做这件事



组件包含了大量静态内容。在这种情况下，你可以在根元素上添加 v-once attribute 以确保这些内容只计算一次然后缓存起来(不建议用，像<keep-alive>)
Vue.component('terms-of-service', {
  template: `
    <div v-once>
      <h1>Terms of Service</h1>
      ... a lot of static content ...
    </div>
  `
})



动画
<style type ="text/css">  
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter , .fade-leave-to{
  opacity: 0;
}
</style>
 <div id="demo">
     自带的transition (fade是指定类样式名的前缀，不太好) <br/>
  <transition name="fade">
    <p v-if="show">hello</p>
  </transition>
  <button v-on:click="show = !show">Toggle</button>
</div>
<script type="text/javascript">
new Vue({
	  el: '#demo',
	  data: {
	    show: true
	  }
	})
</script>


混入 (mixin) 
var myMixin = {
  created: function () {//create 生命周期
    this.hello()
  },
  methods: {
    hello: function () {
      console.log('hello from mixin!')
    }
  }
}
var Component = Vue.extend( //Vue.extend
	{  mixins: [myMixin] //mixins属性
	})
var component = new Component() //new 会调用 created

当组件和混入对象含有同名选项时，这些选项将以恰当的方式进行“合并”。
比如，数据对象在内部会进行递归合并，并在发生冲突时以组件数据优先。

像created生命周期 这样的 同名钩子函数将合并为一个数组，不被覆盖，先用调用混入的再组件的

全局注册。使用时格外小心,它将影响每一个之后创建的 Vue 实例 
Vue.mixin({
  created: function () {
    var myOption = this.$options.myOption
    if (myOption) {
      console.log(myOption)
    }
  }
})
new Vue({
  myOption: '全局hello!'
})


默认策略，即简单地覆盖已有值,自定义合并逻辑
Vue.config.optionMergeStrategies.myOption = function (toVal, fromVal) {
  // 返回合并后的值
}
于多数值为对象的选项，可以使用与 methods 相同的合并策略
var strategies = Vue.config.optionMergeStrategies
strategies.myOption = strategies.methods


自定义全局指令v-focus<br/>
<input > <br/>
<div id="simplest-directive-example"  >
  <input v-focus="">
</div> 
<script type="text/javascript">
Vue.directive('focus', {
  // 当被绑定的元素插入到 DOM 中时……
  inserted: function (el) {
    el.focus()
  }
})
new Vue({
  el: '#simplest-directive-example'
})
</script>




如果想注册局部指令v-focus1，组件中也接受一个 directives 的选项<br/>
<input > <br/>
<div id="componentDiv"  >
  <input v-focus1="">
</div> 
<script type="text/javascript"> 
new Vue({
  el: '#componentDiv',
  directives: {
    focus1: {
      inserted: function (el) {
        el.focus()
      }
    }
  }
}) 
</script>

除 inserted 函数外，还可有
bind：只调用一次，指令第一次绑定到元素时调用
update：
componentUpdated：
unbind：只调用一次，指令与元素解绑时调用



 
函数参数除了 el 之外，其它参数都应该是只读的，切勿进行修改。如果需要在钩子之间共享数据，建议通过元素的 dataset 来进行。
<div id="hook-arguments-example" v-demo:foo.a.b="message"></div>
<script type="text/javascript">
Vue.directive('demo', {
	  bind: function (el, binding, vnode)//el：指令所绑定的元素，可以用来直接操作 DOM
	  {
	    var s = JSON.stringify
	    el.innerHTML =
	      'name: '       + s(binding.name) + '<br>' + //指令名，不包括 v- 前缀
	      'value: '      + s(binding.value) + '<br>' +//指令的绑定值,如：v-my-directive="1 + 1" 中，绑定值为 2
	      'expression: ' + s(binding.expression) + '<br>' +//字符串形式的指令表达式。例如 v-my-directive="1 + 1" 中，表达式为 "1 + 1"。
	      'argument: '   + s(binding.arg) + '<br>' +//传给指令的参数，可选。例如 v-my-directive:foo 中，参数为 "foo"。
	      'modifiers: '  + s(binding.modifiers) + '<br>' +//修饰符的对象。例如：v-my-directive.foo.bar 中，修饰符对象为 { foo: true, bar: true }
	    								//oldValue：指令绑定的前一个值，仅在 update 和 componentUpdated 钩子中可用
	      'vnode keys: ' + Object.keys(vnode).join(', ')
	  }
	})
	new Vue({
	  el: '#hook-arguments-example',
	  data: {
	    message: 'hello!'
	  }
	})
</script>

动态指令 
<div id="dynamicexample">
  <h3>Scroll down inside this section ↓</h3>
  <p v-pin:[direction]="200">I am pinned onto the page at 200px to the left.</p>
</div>
<script type="text/javascript">
Vue.directive('pin', {
	  bind: function (el, binding, vnode) {
	    el.style.position = 'fixed'
	    var s = (binding.arg == 'left' ? 'left' : 'top')
	    el.style[s] = binding.value + 'px'
	  }
	})
	new Vue({
	  el: '#dynamicexample',
	  data: function () {
	    return {
	      direction: 'left'
	    }
	  }
	})
</script>

缩写
<div id="shortDiv">
	<div v-color-swatch="mygreen">你可能想在 bind 和 update 时触发相同行为，而不关心其它的钩子。可缩写为</div>
</div>
<script type="text/javascript">
Vue.directive('color-swatch', function (el, binding) {
  el.style.backgroundColor = binding.value
})
new Vue({
	  el: '#shortDiv',
	  data:{
		  mygreen:'green'
	  }
 })
</script>


传对象
<div id="objectDiv">
	<div v-demo1="{ color: 'white', text: 'hello!' }">指令可传对象</div>
</div>
<script type="text/javascript">
	Vue.directive('demo1', function (el, binding) {
	  console.log(binding.value.color) // => "white"
	  console.log(binding.value.text)  // => "hello!"
	})
	new Vue({
	  el: '#objectDiv' 
	})
</script>

render 渲染函数<br/>
:level  变量 level prop 
<div id="renderDiv"> 
	<anchored-heading :level=2>动态Hx标签 2</anchored-heading>
	<anchored-heading :level=5>动态Hx标签 5</anchored-heading>
</div>
<script type="text/javascript">
Vue.component('anchored-heading', {
	  render: function (createElement) {//render 渲染函数
	    return createElement(//虚拟 DOM
	      'h' + this.level,   // 标签名称
	      this.$slots.default // 子节点数组
	    )
	  },
	  props: {
	    level: {
	      type: Number,
	      required: true
	    }
	  }
	});
new Vue({  el: '#renderDiv' });
</script>
将 h 作为 createElement 的别名是 Vue 生态系统中的一个通用惯例，实际上也是 JSX 所要求的
可以去掉 (h) 参数

虚拟节点 (virtual node) 简写它为VNode
虚拟DOM 是我们对由 Vue 组件树建立起来的整个 VNode 树的称呼

<div id="renderAdvDiv">
	<anchored-heading :level=2> 链接1 <span>内部span2</span></anchored-heading>
</div>
<script type="text/javascript">
	var getChildrenTextContent = function (children) {
	  return children.map(function (node) {
	    return node.children
	      ? getChildrenTextContent(node.children)
	      : node.text
	  }).join('')
	}

	Vue.component('anchored-heading', {
	  render: function (createElement) {
	    // 创建 kebab-case 风格的 ID
	    var headingId = getChildrenTextContent(this.$slots.default)
	      .toLowerCase()
	      .replace(/\W+/g, '-') //非数字和字母
	      .replace(/(^-|-$)/g, '')

	    return createElement(
	      'h' + this.level,
	      [//第三个参数可选 ，{String | Array} ，子级虚拟节点 (VNodes)， createElement()建成，或字符串做 “文本虚拟节点”
	        //数组里不能有重复的VNode
			createElement('a', 
		        { //第二个参数可选，对象
		          attrs: { //普通的 HTML attribute ,还有很多其它选项，见https://cn.vuejs.org/v2/guide/render-function.html 深入数据对象
		            name: headingId,
		            href: '#' + headingId
		          }
		        }, this.$slots.default
		    ),
	      ]
	    )
	  },
	  props: {
	    level: {
	      type: Number,
	      required: true
	    }
	  }
	})
	new Vue({  el: '#renderAdvDiv' });
</script>


//apply继承，数组有20个元素,第一个参数 处于非严格模式下，则指定为 null 或 undefined 时会自动替换为指向全局对象
//var myArray=Array.apply(null, { length: 20 }); 
//var myArray=Array.apply( undefined, { length: 20 }); 

要重复元素的方法
<div id="renderRepeatDiv">
	<repeat-p></repeat-p>
</div>
<script type="text/javascript">
Vue.component('repeat-p', 
{ 
	render: function (createElement)
	 {
		/*
		var myParagraphVNode = createElement('p', 'hi'); 
		 return createElement('div', [
		    myParagraphVNode, myParagraphVNode // 文档说不行，但测试下来是可以的
  		])
  		*/
		return createElement('div',
			Array.apply(null, { length: 20 }) //apply继承，数组有20个元素
			.map(function () 
				{
				  return createElement('p', 'hi')
				})
		)
	}
})
 new Vue({  el: '#renderRepeatDiv' });
</script>

在模板中使用的 v-if 和 v-for 都可以在render渲染函数中用 JavaScript 的 if/else 和 map 来重写

props: ['items'],
render: function (createElement) 
{
  if (this.items.length) 
  {
	return createElement('ul', this.items.map(function (item) 
				{
				  return createElement('li', item.name)
				})
			)
  } else
  {
	return createElement('p', 'No items found.')
  }
}

插件
https://github.com/vuejs/awesome-vue#components--libraries  下有很多插件
Vue.use() 使用插件。它需要在你调用 new Vue() 启动应用之前完成
 

过滤器 即 | 符号,后是过滤器名字, 可用于{{ }}  和 v-bind 表达式中 <br/>
<div id="example-1" class="demo">
  <input type="text" v-model="message">
  <p>{{ message | capitalize }}</p>
</div>
<script>
//创建 Vue 实例之前全局定义过滤器：
Vue.filter('capitalize', function (value) //参数是|前面的值
{
  if (!value) return ''
  value = value.toString()
  return value.charAt(0).toUpperCase() + value.slice(1)
})
new Vue({
  el: '#example-1',
  data: function () {
    return {
      message: 'john'
    }
  },
  /*
  filters: { // filters 选项
    capitalize: function (value) 
    {
      if (!value) return ''
      value = value.toString()
      return value.charAt(0).toUpperCase() + value.slice(1)
    }
  }*/
  
})
</script>
{{ message | filterA | filterB }}  会将 filterA 的结果传递到 filterB 中 <br/>
{{ message | filterA('arg1', arg2) }} filterA 被定义为接收三个参数的过滤器函数 <br/>


路由之前
 router=new VueRouter({
		  routes: [ 
			    	{ 
			    		path: '/user/:id', 
			    		component:  { template: '<div>User {{ $route.params.id }}</div>'},
			    		//路由独享的守卫
			    		beforeEnter: (to, from, next) => 
			    		{
			    			if(!logined && 
			    					//to.matched返回所有配置的地址,只要一个匹配就是true
			    					to.matched.some( item => item.path.startsWith('/user') )//item.path值 为/user/:id
			    			)
			    	     		next('/login');//跳转到登录,
			    	     	else
			    	     		next();//正常跳转
			    	    }
			    	},{ 
			    		path: '/login', 
			    		component:  { template: '<div>login </div>'}
			    	}
				]
		});
		
		
//全局前置守卫
router.beforeEach((to, from, next) => {
	if(!logined && to.path.startsWith("/user"))
		next('/login');//跳转到登录,
		//next(false)//不跳转
	else
		next();//正常跳转
})

//全局后置守卫
router.afterEach((to, from) => {
// 
})


=============Vue Router
https://cn.vuejs.org/js/vue.js
https://unpkg.com/vue-router 					会自动跳 https://unpkg.com/vue-router@3.1.6/dist/vue-router.js
https://unpkg.com/vue-router/dist/vue-router.js 会自动跳 https://unpkg.com/vue-router@3.1.6/dist/vue-router.js

https://unpkg.com/vue-router@3.1.6/dist/vue-router.min.js

<script type="text/javascript" src="../vue-2.6.10/vue.js"></script>
<script type="text/javascript" src="../unpkg_vue-router-3.1.6/vue-router.js"></script>

<!-- 
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
 -->
 
npm install vue-router -g

import Vue from 'vue'
import VueRouter from 'vue-router' 
Vue.use(VueRouter) //如果使用模块化机制编程，导入Vue和VueRouter，要调用 Vue.use(VueRouter)
 
路由 (类似react的hash路由,也是这个影响浏览器的返回功能)
 
 
 <style type ="text/css">  
	.router-link-active
	{
		color: yellow;
		background-color: gray;
	}
</style>
<div id="app">
  <h1>Hello App!</h1>
  <p>
	<!-- <router-link> 默认会被渲染成一个 `<a>` 标签
	当 <router-link> 对应的路由匹配成功，将自动设置 class 属性值 .router-link-active
	-->
	<router-link to="/foo">Go to Foo</router-link>
	<router-link to="/bar">Go to Bar</router-link>
  </p>
  <!-- 路由匹配到的组件将渲染在这里 -->
  <router-view></router-view>
</div>
<script type="text/javascript">
	// 0. 如果使用模块化机制编程，导入Vue和VueRouter，要调用 Vue.use(VueRouter)
	const Foo = { template: '<div>foo</div>' }
	const Bar = { template: '<div>bar</div>' }

	//  2. 定义路由(类似react的hash路由)
	// 每个路由应该映射一个组件。 其中"component" 可以是
	// 通过 Vue.extend() 创建的组件构造器
	const routes = [
	  { path: '/foo', component: Foo },
	  { path: '/bar', component: Bar }
	]
	// 3. 创建 router 实例，然后传 `routes` 配置 
	const router = new VueRouter({
	  routes // (缩写) 相当于 routes: routes
	})
	// 4. 创建和挂载根实例 
	const app = new Vue({
	  router
	}).$mount('#app') 
</script> 
//------带参数
	<div id="app">
	  <p>
	    <router-link to="/user/li">Go to li</router-link> <br/>
	    <router-link to="/user/si">Go to si</router-link> <br/>
		<router-link :to="{ path: '/user/abc'}" replace>v-bind值是对象，属性path匹配定义的，加replace无浏览器历史记录</router-link><br/>
			加replace会调用 router.replace() 而不是 router.push()，导航后不会留下 history 记录<br/>
		<router-link to="/post/aaa">Go to post/aaa 取匹配*部分 </router-link> <br/>
	    <router-link to="/xxx/yyy">Go to/xxx/yyy 最后的找不到 </router-link> <br/>
	  </p>
	  <router-view></router-view> 
	</div>
	<script type="text/javascript"> 
	const User = {
	  //$route.params得到参数
	  template: '<div>User {{ $route.params.id }}</div>',
	  	watch: {
		    '$route' (to, from) { //'$route' 后没有 : 
		 		console.log("watch路由"+from.path+"变"+to.path);
		    }
	  	},
	  	 beforeRouteUpdate (to, from, next) {//2.2新功能
	  		console.log("beforeRouteUpdate对路由"+from.path+"变"+to.path);
	  	     next();//一定调用这个
	  	  }
	 }
	const Post = { 
			  template: '<div>Post {{ $route.params.pathMatch  }}</div>',
	}
	const routes = [
		 // 动态路径参数 以冒号开头(同react-router)，路径支持*通配
	    { path: '/user/:id', component: User },
	    { path: '/post/*', component: Post },
	    { path: '*', component: { template: '<div>{{ $route.params.pathMatch }} 没有找到 !</div>'} }
	] 
	const router = new VueRouter({
	  routes  
	})  
	const app = new Vue({
	  router
	}).$mount('#app')  
	</script>
	
编程式路由
<div id="programRouterDiv">
	点两次报错？？(两次push一样的)
  <p>
	<button @click="goLisi">内部的编程式路由</button>
	<button @click="goBack">后退</button>
	<button @click="goForward">前进</button>
  </p>
  <router-view></router-view> <!-- 如果 router-view 没有设置名字，那么默认为 default。 -->
  <router-view name="outSection"></router-view>
</div>
<script type="text/javascript"> 
   router=new VueRouter({
		  routes: [ 
			    	{
			    		name:'userPath',//起名
			    		path: '/user/:id', 
			    		//component:  { template: '<div>User {{ $route.params.id }}</div>'}
			    		components: {//尾有s ，两时影响两个 router-view
			    			default:{ template: '<div>User {{ $route.params.id }}</div>'},
			    			outSection:	{ template: '<div>outSection {{ $route.params.id }}</div>'}
			    		}
			    	}
			     ]
		});
	const vm = new Vue(
	{
	  	router,
		methods:
		{
			goLisi:function()
			{
				//在Vue实例内部，你可以通过 $router 访问路由实例。因此你可以调用 this.$router.push
				//this.$router.push("/user/lisi");
				//this.$router.push({ path: '/user/lisi'})
				this.$router.replace({ name: 'userPath',params:{id: '123' }}) //如对象式传参数一定要有名字
			},
			goBack:function()
			{
				router.go(-1)// 后退一步记录 等同于 history.back() (如是router.replace 不能回到上一步操作)
			},
			goForward:function()
			{
				router.go(1)// 前进一步等同于 history.forward()
			}
		}
	}).$mount('#programRouterDiv');
	
	function outGoLisi()
	{
		 router.replace("/user/lisi")//Vue实例外部用法,这里的router是 new VueRouter实例
	}
	</script> 
	<button onclick='outGoLisi()'>外部的编程式路由</button> 

元数据
router=new VueRouter({
		  routes: [ 
			    	{ 
			    		path: '/user/:id', 
			    		component:  { template: '<div>User {{ $route.params.id }}</div>'},
			    		meta:{ //元数据
			    			mustLogin:true
			    		},
			    		//路由独享的守卫
			    		beforeEnter: (to, from, next) => 
			    		{
			    			if(!logined &&  
			    					to.matched.some( item => item.meta.mustLogin) //得到元数据
			    			)
			    	     		next('/login'); 
			    	     	else
			    	     		next();
			    	    }
			    	}
				]
			});
			
过渡  
<style type ="text/css">  
	.fade-enter-active, .fade-leave-active {
	  transition: opacity .5s;
	}
	.fade-enter, .fade-leave-to{
	  opacity: 0;
	}
</style> 
<div id="app">
  <h1>Hello App!</h1>
  <p>
    <router-link to="/foo">Go to Foo</router-link>
    <router-link to="/bar">Go to Bar</router-link>
  </p>
  <router-view></router-view>
</div>
<script type="text/javascript">
//fade是指定类样式名的前缀
const Foo = { template: '<transition name="fade"> <div>foo</div>  </transition>' }
const Bar = { template: '<transition name="fade"> <div>bar</div>  </transition>' }
const routes = [
  { path: '/foo', component: Foo },
  { path: '/bar', component: Bar }
]
const router = new VueRouter({
  routes  
}) 
const app = new Vue({
  router
}).$mount('#app') 
</script>


路由懒加载
const Foo = () => Promise.resolve({ template: '<div>foo</div>' })
//const Foo = () => import('./Foo.vue')
	
const router = new VueRouter({
  routes  ,
  //mode: 'history',// 默认 hash 模式,  去上下文和文件名 不会丢失,请求这个地址不会404
  //,当history模式时, URL就像  http://127.0.0.1:8080/foo (上下文和文件名 会丢失 ? ?,当请求这个地址就404 不太好)
}) 





============Vuex
vuex-3.1.1
https://github.com/vuejs/vuex/tree/dev/dist 下载 vuex.min.js  vuex.js

借鉴了Redux
如果您不打算开发大型单页应用，使用 Vuex 可能是繁琐冗余的
如果您的应用够简单，您最好不要使用 Vuex
就是 store（仓库）, store 中的状态发生变化，那么相应的组件也会相应地得到高效更新
你不能直接改变 store 中的状态。改变 store 中的状态的唯一途径就是显式地提交 (commit) mutation


可以使用 vue ui 工具建立项目，选择vuex会自动生成基本代码
使用 Vuex 并不意味着你需要将所有的状态放入 Vuex
	
<div id="app"></div>
<script type="text/javascript">
// 如果在模块化构建系统中，请确保在开头调用了 Vue.use(Vuex)
const store = new Vuex.Store({
  state: { //定全局共享数据，命名类似react,但这里可以所有子组件使用
	count: 10
  },
  mutations: { //修改store数据的函数,可以方便查找谁修改了变量,这里不要执行异步操作
	increment  (state) {//参数为store中的state,后面为传来的参数
		state.count++; 
	},
	incrementByStep (state,step) { 
	  state.count+=step 
	}
  }
});
const Counter = {
  template: `<div> {{$store.state.count}},mapState count:{{count}},{{countAlias}},{{countPlusLocalState}} </div>`, //{{ countCmp }} 
  //模板中仿问的是computed中的函数
  /*
  computed: {
	countCmp () {
	  return store.state.count;//Vue实例外取到数据方法
	   //return this.$store.state.count//Vue实例内取到数据方法
	},
  }
  */
  
  //方式二 import { mapState } from 'vuex' //只能用npm方式 ,取store中的值另一种方式
  //在单独构建的版本中辅助函数为 Vuex.mapState
 /*
  computed: Vuex.mapState([
	"count"
  ])
  */
 computed: Vuex.mapState({
	// 箭头函数可使代码更简练
	count: state => state.count, 
	// 传字符串参数 'count' 等同于 `state => state.count`
	countAlias: 'count', 
	// 为了能够使用 `this` 获取局部状态，必须使用常规函数
	countPlusLocalState (state) {
	  return state.count + this.localState
	}
  }),
  data:function(){
	  return {localState:10}
  }
}
const Add = {
		  template: `<div> <button @click="addStore">add</button>
							<button @click="incrementByStep(3)">add传参数</button> 
					</div>`,
		  methods:{
			  //addStore:function(){
			  //	  this.$store.commit('incrementByStep',2);//commit的参数是store中mutations的函数名,第二为参数可选
			  //}
			//方式二  import { mapMutations } from 'vuex' 只用于npm方式
			 //在单独构建的版本中辅助函数为 Vuex.mapMutations 
			 ...Vuex.mapMutations([
				'increment', // 将 `this.increment()` 映射为 `this.$store.commit('increment')`
				'incrementByStep'//能传参数 
			 ]),
			...Vuex.mapMutations({
				addStore: 'increment' // 将 `this.addStore()` 映射为 `this.$store.commit('increment')`
			})
		  }
	  }
const app = new Vue({
	  el: '#app',
	  store,  // 把 store 对象提供给 “store” 选项，这可以把 store 的实例注入所有的子组件
	  components: { Counter ,Add},
	  template: `
		<div class="app">
		  <counter></counter>
		  <add></add>
		</div>
	  `
	})
</script>

可以通过 vue.js devtools看以看 vuex的状态值 
 

 
 action异步
 const store = new Vuex.Store({
	  state: {  
	    count: 10
	  },
	  mutations: { //修改store数据的函数,可以方便查找谁修改了变量,	这里不要执行异步操作
		increment  (state) { 
		    state.count++; 
		},
	    incrementByStep (state,step) 
	    { 
		  if(typeof step =='number')
	      	state.count+=step 
	      else if (step instanceof Object)
	    	state.count+=step.amount
	    }
	  },
	  actions: //可异步操作,调用 mutations中方法，不能修改state
	  {
		  incrementAsync ({ commit })  //context里的commit属性
		  {  
			  setTimeout(() => {
		      	commit('increment')//context.commit('increment');//是mutations中的方法名
		     }, 1000)
		  },
		  incrementByStepAsync (context,step)  //带参数 
		  {  
			  setTimeout(() => {
				  context.commit('incrementByStep',step) 
		     }, 1000)
		  }
	  }
 });
  const Add = {
	  template: `<div> 
					<button @click="addAsync">add Async</button>
					<button @click="addByStepAsync">add by Step Async</button>
					<button @click="aliasAddAsyn">add  use mapActions </button>
					<button @click="incrementByStepAsync(5)">add by Step Async  use mapActions</button>
				</div>`,
	  methods:{ 
		  addAsync:function()
		  {
			  this.$store.dispatch('incrementAsync')//你在组件中使用 ,对应store中actions中的函数
		  },
		  addByStepAsync:function()
		  {
			 //this.$store.dispatch('incrementByStepAsync',3)//传参数,可为对象
			  //store.dispatch('incrementByStepAsync',3)//组件外使用
			  //以对象形式分发
			  store.dispatch({
				type: 'incrementByStepAsync',
				amount: 10
			  })
		  },
		  //方式二import { mapActions } from 'vuex' //只对npm方式
		  ...Vuex.mapActions({
			  aliasAddAsyn: 'incrementAsync' // 将 `this.aliasAddAsyn()` 映射为 `this.$store.dispatch('incrementAsync')`
		   }),
		   //可以传参数
		   ...Vuex.mapActions([
			  'incrementByStepAsync' // 将 `this.incrementByStepAsync(amount)` 映射为 `this.$store.dispatch('incrementByStepAsync', amount)`
		  ]),
	  }
	}

Getters 包装store中的数据
<div id="app"></div>
 <script type="text/javascript">
const store = new Vuex.Store({
	  state: {  
	    count: 10
	  },
	  mutations: {  
		  incrementByStep (state,step) { 
		      state.count+=step 
		  }
	  },
	  getters:{ //只包装store中的数据,不修改,如store中数据变化,这里也变化,类似compute
		  formatNumber:function(state)
		  {
			  return '$'+state.count;
		  }
	  }
 });
const Counter = {//组件内取值 方法$store.getters.xxx
	 template: `<div>$store显示getters中的值为: {{$store.getters.formatNumber}} <br/>
					mapGetters显示getter的值(computed): {{formatNumber}} </div>`, 
	 computed:{
		//方式二 import { mapGetters } from 'vuex' 只对npm方式
		  ...Vuex.mapGetters(['formatNumber']), //formatNumber是store 中 getters中方法名
	  }
} 
const MyButton = {
		  template: `<div> 
		  				<button @click="showFomatter">外部显示getter的值</button> 
		  				<button @click="addStore">增加2</button>
			  		</div>`,
		  methods:{ 
			  showFomatter:function()
			  {
				//组件外store.getters.xxx 
				console.log(store.getters.formatNumber);//formatNumber是store 中 getters中方法名
			  },
			 addStore:function(){
				this.$store.commit('incrementByStep',2); 
			 }
		  }
	  }
 const app = new Vue({
	  el: '#app',
	  store,  
	  components: { Counter,
		  			mybutton:MyButton},
	  template: `
	    <div class="app">
	      <counter></counter> 
	      <mybutton></mybutton> 
	    </div>
	  `
	})
 </script>
 
v-model在vuex中的使用,有点麻烦
 <div id="app"></div>
 <script type="text/javascript">
 const store = new Vuex.Store({
	  state: {  
		  obj: {
			  message:"one"
		  }
	  },
	  mutations: {  
		  updateMessage (state,msg) { 
		      state.obj.message=msg 
		  }
	  } 
 }); 
 const app = new Vue({
	  el: '#app',
	  store,   
	  template: `
	    <div class="app">
		  <label>{{message}}</label><br/>
		  <input v-model="message"> 
	    </div>
	  `,
	 computed: 
	 {
		  message: 
		  {
		    get () {
		      return this.$store.state.obj.message
		    },
		    set (value) {
		      this.$store.commit('updateMessage', value)
		    }
		  }
	 }
 })
 </script>
 
============vue-cli
v3.10.0  
类似 create-react-app 功能 ,但有管理界面

安装
npm install -g @vue/cli

建立项目用
vue create my-vue-project 提示选择,默认自动初始化git仓库,生成node_modules目录
	有src/main.js , src/App.vue ,public/index.html文件 
cd my-vue-project
npm install (是对没有node_modules目录的情况下,来下载)
npm run serve   提示地址  http://<hostname>:8080/ 如端口被占用会自动+1方式使用新端口 
		(serve配置的是执行vue-cli-service serve,源码在./node_modules/@vue/cli-service/bin/vue-cli-service.js) 
npm run build 为生产环境

vue ui  会打开 http://localhost:8000/project/select 可以界面方式创建项目(这个很好,比react强)
  建立时可选vuex,建立后自动下载依赖,会比较慢 ,Task标签->Serve-> run Task按钮,启动项目后 可点open App按钮
  相当于npm run serve 

创建的项目有使用eslint


全局 CLI 配置
home 目录下一个名叫 .vuerc 的 JSON 文件
vue config 来查看


vscode 打开 .vue文件 提示推荐安装 Vetur 扩展，才可高亮显示
idea 也要单独安装Vue.js 插件
eclipse要marketplace安装React::CodeMix 3 , Vue.js:CodeMix 3 插件 


如要快速开发，使用vue serve 和 vue build 命令对单个 *.vue 文件进行快速原型开发，安装
npm install -g @vue/cli-service-global

vue serve MyComponent.vue
vue build MyComponent.vue

-----vue.config.js 
是Vue CLI 3 开始的
https://cli.vuejs.org/zh/config/#vue-config-js

vue.config.js 是一个可选的配置文件，如果项目的 (和 package.json 同级的) 根目录中存在这个文件，那么它会被 @vue/cli-service 自动加载
//----vue.config.js 
module.exports = { 
  publicPath:'/myContext',  //默认上下文是  /  
  //baseUrl从 Vue CLI 3.3 起已弃用
  outputDir:'dist' , //默认是dist目录
  indexPath:'index.html', //默认首页是 index.html 
}

可这样访问
http://127.0.0.1:8082/myContext/index.html

//POST请求测试成功
module.exports = { 
	devServer: { 
		proxy: 'http://localhost:8080/S_VueEasyUI'
	}
}


============Vue Loader 
是一个 webpack 的 loader
可以把.vue文件 输出成组件

每个 *.vue 文件都包括三部分 <template>, <script> 和 <style>

使用webpack的 style-loader




