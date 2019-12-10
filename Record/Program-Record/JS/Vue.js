Vue(发音像view) 最新 v2.6.10

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


Vue 比React简单 (没有用的class)

vue-js-devtools 对firefox addons 最新的是 vuejs_devtools-5.1.1-fx.xpi 
也有chrome的

官方下载vue.js 或 vue.min.js (2.6.10版本大小 93.7K,相比react-16.8.6的react要小一点， react.production.min.js 大小12.7K,react-dom.production.min.js 大小108KB)
也可 npm install vue -g

<script type="text/javascript" src="vue-2.6.10/vue.js"></script>



<div id="app">
  {{ message }}
</div>

<div id="app-2">
  <span v-bind:title="message">
    Hover your mouse over me for a few seconds
    to see my dynamically bound title!
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


<!-- v-model 双向绑定 表单输入值后(修改变量)立即显示-->
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
     2.2.0+ 的版本里，当在组件上使用 v-for 时，key是必须
    -->
    <todo-item
      v-for="item in groceryList"
      v-bind:todo="item"
      v-bind:key="item.id"
    ></todo-item>
  </ol>
</div>
 
<script type="text/javascript">  

//定义名为 todo-item 的新组件， props加自己的属性
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
	<span v-once>v-once 如数据变，显示不会改变: {{ msg }}</span>
	<p>Using mustaches: {{ rawHtml }}</p>
	<p>Using v-html directive: <span v-html="rawHtml"></span></p>
	
	
	<p>number+1={{ number + 1 }} </p>
	<p>ok={{ ok ? 'YES' : 'NO' }}</p>
	<p>message reverse={{ message.split('').reverse().join('') }}</p>
	<div v-bind:id="'list-' + id"></div>
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
var vm = new Vue({
  el: '#example',
  data: data
})

vm.$data === data // => true
vm.$el === document.getElementById('example') // => true

// $watch 是一个实例方法
vm.$watch('a', function (newValue, oldValue) {
  // 这个回调将在 `vm.a` 改变后调用
  console.log("newValue="+newValue+",oldValue="+oldValue)
})
//console中修改vm.a=2 



//---- 生命周期 
 
new Vue({
  data: {
    a: 1
  },
  // created方法在实例被创建之后执行 
  	created: function () {
    // `this` 指向 vm 实例
    //不要使用箭头函数，比如 created: () => console.log(this.a) 箭头函数并没有 this
    console.log('a is: ' + this.a)
  }
})
// => "a is: 1"

/*  其它函数
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
			ok:undefined
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
computeSetDiv:<div id="computeSetDiv"> {{ fullName }} </div>

<script type="text/javascript">
//这种方法不如computed方式好 
var vm = new Vue({
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
//console中修改 vm.firstName='li'
var vm = new Vue({
  el: '#computeSetDiv',
  data: {
    firstName: 'Foo',
    lastName: 'Bar',
    fullName: 'Foo Bar'
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
//console中修改 vm.fullName='li si' 


<!-- 属性是样式名，值真/假表示是否要 -->
<!-- 属性是样式类名，值真/假表示是否要 -->
<div id="innerClassDiv" class="static"  v-bind:class="{ active: isActive, 'text-danger': hasError }" >class样式 内部对象 </div>
<div id="outObjClassDiv" v-bind:class="classObject">class 样式 外部对象</div>
<div id="arrayClassDiv" >
	<div v-bind:class="[activeClass, errorClass]">class 样式 数组式，变量值是样式名</div>
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
		  errorClass: 'text-danger'
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
	 切换 loginType 将不会清除用户已经输入的内容 
	<!--
	因为两个模板使用了相同的元素，<input> 不会被替换掉——仅仅是替换了它的 placeholder
	-->
	<template v-if="loginType === 'username'">
	  <label>Username</label>
	  <input placeholder="Enter your username">
	</template>
	<template v-else>
	  <label>Email</label>
	  <input placeholder="Enter your email address">
	</template>
	增加key属性后
	<!--  如果需要替换组合，要增加 唯一值的 key 属性 --> 
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
	
	v-for 具有比 v-if 更高的优先级 (不推荐同时使用这两个)
	 -->
	<h1 v-show="ok">v-show元素始终会被渲染并保留在 DOM 中， 只是简单地切换元素的 CSS 属性 display</h1>
</div>
<div id="forDiv"> 
	<!-- v-for可加在第二列加index  -->
	  <li v-for="(item, index) in items">
	    {{ parentMessage }} - {{ index }} - {{ item.message }}
	  </li>
	 
	<!--  of 同 in  -->
	<div v-for="item of items">{{item.message}}</div>
	
	<!--  当不是数组，而是对象时，取的是属性的值  -->
	<li v-for="value in object">
		{{ value }}
	</li>
	
	<!--  是对象时可选第二个参数是属性名  -->
	<div v-for="(value, name) in object">
		{{ name }}: {{ value }}
	</div>
	<!--  是对象时可选第三个参数索引  --> 
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
	
</div>

<script type="text/javascript">
v=new Vue({
	  el: '#conditionDiv',
	  data: {
		  awesome:true,
		  ok:true,
		  type:'B',
		  loginType:'username'
	  }
	}) 
	// console中 v.loginType='x' 
	 
new Vue({
	  el: '#forDiv',
	  data: {
	    parentMessage: 'Parent',
	    items: [
	      { message: 'Foo' },
	      { message: 'Bar' }
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
	filter()、concat() 和 slice() 。它们不会改变原始数组，而总是返回一个新数组,VUE也会重新渲染
	Vue 不能检测两种数组的变动(由于 JavaScript 的限制)，
	1.vm.items[indexOfItem] = newValue 
		解决方法 Vue.set(vm.items, indexOfItem, newValue) 或 vm.items.splice(indexOfItem, 1, newValue)
		 		vm.$set(vm.items, indexOfItem, newValue) //是全局方法 Vue.set 的一个别名
	2.修改数组的长度时 如vm.items.length = newLength
		解决方法 vm.items.splice(newLength)//测试无效
	*/
	console.log([0,1,2,3,4].splice(1,1,11));//JS 的 splice没有三参数的功能，但vue有
/*
	//console中写入 
	v1.items.splice(1, 1, {message:'XXX'});
	Vue.set(v1.items, 1, {message:'YYY'});
	v1.$set(v1.items, 1, {message:'ZZZ'});
*/	
	console.log([0,1,2,3,4].splice(8));//JS 的 splice没有扩展数组的功能
	//v1.items.splice(4);//测试无效 
	//v1.$set(v1.items, 3, {message:'AAA'});//但递增一个是可以的
		
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
中的 pageDown 命令风格变为 page-down

可以使用过时的keyCode 
<input v-on:keyup.13="submit">

.ctrl
.alt
.shift
.meta 	在Mac对应 command 键，在 Windows 系统键盘Win键

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
多行文本
<div id="multiDiv">
	<span>Multiline message is:</span>
	<p style="white-space: pre-line;">{{ message }}</p>
	<br>
	<textarea v-model="message" placeholder="add multiple lines"></textarea>
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
	<br/>click event:
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
	other Div:	<input type="radio" v-model="pick" v-bind:value="a"  @click="myOncheck" >
	<br/>值是对象: 
	<select v-model="selected">  <!-- 内部是对象变量 -->
	  <option v-bind:value="{ number: 123 }">123</option>
	  <option v-bind:value="{ number: 456 }">456</option>
	</select>
	selected={{selected}} <br/>
	数字:<input v-model.number="age" type="number"> <br/>
	去空格:<input v-model.trim="msg"> <br/>
	输入完成时才更新<input v-model.lazy="msg" > msg:{{msg}}
</div>
<script type="text/javascript">
//当选中时 vm.pick值被修改为 vm.a的值
	vm=new Vue({
	  el: '#otherFormDiv',
	  data: {
		    a: '1',
		    pick:'2',
		    selected:undefined,
		    msg:'a'
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
	事件传参数
	 <div :style="{ fontSize: postFontSize + 'em' }">
<!-- 
	    <blog-post
	      v-for="post in posts"
	      v-bind:key="post.id"
	      v-bind:post="post"
	       v-on:enlarge-text="postFontSize += $event"
	    ></blog-post>
	     以通过 $event 访问到被抛出的0.1 
 二选一
 -->
	    <blog-post
	      v-for="post in posts"
	      v-bind:key="post.id"
	      v-bind:post="post"
	     v-on:enlarge-text="onEnlargeText"
	    ></blog-post>
	    接收事件使用方法处理
	  </div>
</div>
 <script type="text/javascript">
 Vue.component('blog-post', {
	  props: ['post'],
	  //$emit('enlarge-text', 0.1) 表示 生产事件带参数0.1
	  template: `
	    <div class="blog-post">
	      <h3>{{ post.title }}</h3>
	      <button v-on:click="$emit('enlarge-text', 0.1)">
			  Enlarge text
			</button>
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
 	v-model用于自定义组件
	<input v-model="searchText">
	等价于：
	<input
	  v-bind:value="searchText"
	  v-on:input="searchText = $event.target.value"
	>
	<br/>
	自定义组件使用变形
	<custom-input
	  v-bind:value="searchText"
	  v-on:input="searchText = $event"
	></custom-input>
	自定义组件使用v-module(template中要用加value属性，加input事件)
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

<table>
  <tr is="blog-post-row"></tr> 
  is用法 ，应该是可以把组件最外层标签修改为tr 
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
	传入一个对象的所有属性 等价于：
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
	    counter: this.initialCounter//可以访问到props中的值
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
</script>

inheritAttrs 和 $attrs <br/>
<div id=attrsDiv>
	<base-form  style="background-color:#00FF00;font-size:2rem">
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
	  //inheritAttrs: false,//不希望组件的根元素继承特性，  不会影响 style 和 class 的绑定
	  inheritAttrs: true,//什么东西会继承？？ ??
	  props: ['label', 'value'],
	  //$attrs  表示接收外部所有属性，即placeholder属性，required属性 
	  template: `
	    <label>
	      {{ label }}
	      <input
	        v-bind="$attrs"
	        v-bind:value="value"
	        v-on:input="$emit('input', $event.target.value)"
	      >
	      value={{value}}
	    </label>
	  `
	}) 
	 new Vue({ el: '#attrsDiv' ,
		data: { 
			username:"lisi"
		}, 
	});
</script>

v-on 事件监听器在 DOM 模板中会被自动转换为全小写 (因为 HTML 是大小写不敏感的) ,推荐你始终使用 kebab-case 的事件名 

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

<hr/>
slot 2.6版本新改进的功能  
<div id="slotDiv">
	<navigation-link url="/profile">
	  <!-- 嵌套组件， 这能能访问url变量，本给自身用，不会传给子组件的--> 
	  <font-awesome-icon name="user"></font-awesome-icon>
	  Your Profile
	</navigation-link>
	<my-submit></my-submit>
	<my-submit>save</my-submit> 
</div>

<script type="text/javascript">
Vue.component("navigation-link", {
	 props: ['url'], 
	//<slot></slot>表示在<navigation-link> 和 </navigation-link> 里的内容
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
		data: { }, 
	});
</script>

<div id="layoutDiv">
	<base-layout>
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

<div id="scopeDiv">
	作用域插槽  slotProps可以任意名: 
	<current-user v-bind:user="user"> 
	  <template v-slot:default="slotProps">
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
	
	<br/> v-slot:default="slotProps" 可缩写为 v-slot="slotProps" :
	<current-user v-bind:user="user"> 
	  <template v-slot="slotProps">
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
	 <br/> 代码：
	<current-user v-bind:user="user"  v-slot="{ user }">
	  {{ user.firstName }}
	</current-user>
	
	<br/> v-slot: 替换为字符 #， default不能省略，示例结果：
	 <current-user v-bind:user="user"> 
	  <template #default="slotProps">
	    {{ slotProps.user.firstName }}
	  </template> 
	</current-user>
</div>
<script type="text/javascript"> 
Vue.component("current-user", {  
	  props: ['user'], 
	  // <slot v-bind:
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
			user:{firstName:"li",lastName:"si"},
			//dynamicSlotName:'user',
		}, 
});

</script>
<div id="slotTodoDiv">
	slot todo示例 
	<todo-list v-bind:todos="todos">
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



=============Vue Router
<script type="text/javascript" src="../vue-2.6.10/vue.js"></script>
<script type="text/javascript" src="../vue-router-3.0.7_unpkg/vue-router.js"></script>
<!-- 
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
 -->
<div id="app">
	  <h1>Hello App!</h1>
	  <p>
	    <!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
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
//当 <router-link> 对应的路由匹配成功，将自动设置 class 属性值 .router-link-active
	
//------带参数
	<div id="app">
	  <p>
	    <router-link to="/user/li">Go to li</router-link>
	    <router-link to="/user/si">Go to si</router-link>
	  </p>
	  <router-view></router-view>
	</div>
	<script type="text/javascript"> 
	const User = {
	  //得到参数
	  template: '<div>User {{ $route.params.id }}</div>',
	  	watch: {
		    '$route' (to, from) {
		 		console.log("对路由"+from.path+"变"+to.path);
		    }
	  	},
	  	 beforeRouteUpdate (to, from, next) {//2.2新功能
	  		console.log("beforeRouteUpdate对路由"+from.path+"变"+to.path);
	  	     next();//一定调用这个
	  	  }
	 }
	const routes = [
		 // 动态路径参数 以冒号开头(同react-router)，路径支持*通配
	    { path: '/user/:id', component: User }
	] 
	const router = new VueRouter({
	  routes  
	})  
	const app = new Vue({
	  router
	}).$mount('#app')  
	</script>
	
============Vuex
vuex-3.1.1
https://github.com/vuejs/vuex/tree/dev/dist 下载 vuex.min.js  vuex.js

借鉴了Redux
如果您不打算开发大型单页应用，使用 Vuex 可能是繁琐冗余的
如果您的应用够简单，您最好不要使用 Vuex
就是 store（仓库）, store 中的状态发生变化，那么相应的组件也会相应地得到高效更新
你不能直接改变 store 中的状态。改变 store 中的状态的唯一途径就是显式地提交 (commit) mutation

<div id="app">
	</div>
	<script type="text/javascript">
	// 如果在模块化构建系统中，请确保在开头调用了 Vue.use(Vuex)
	// 在单独构建的版本中辅助函数为 Vuex.mapState
	//import { mapState } from 'vuex'
	const store = new Vuex.Store({
	  state: {
	    count: 0
	  },
	  mutations: {
	    increment (state) {
	      state.count++
	    }
	  }
	});
	
	const Counter = {
	  template: `<div>{{ count }}</div>`,
	  computed: {
	    count () {
	      //return store.state.count;
	       return this.$store.state.count
	    }
	  }
	}
	const app = new Vue({
		  el: '#app',
		  // 把 store 对象提供给 “store” 选项，这可以把 store 的实例注入所有的子组件
		  store,
		  components: { Counter },
		  template: `
		    <div class="app">
		      <counter></counter>
		    </div>
		  `
		})
	//使用 Vuex 并不意味着你需要将所有的状态放入 Vuex
	</script>
	
============vue-cli
v3.10.0  
类似 create-react-app 功能 

安装
npm install -g @vue/cli	

建立项目用
vue create my-vue-project 有src/main.js , src/App.vue ,public/index.html文件 
cd my-vue-project
npm install (应该是做过了)
npm run serve   提示地址  http://<hostname>:8080/ 如端口被占用会自动+1方式使用新端口 
		(serve配置的是执行vue-cli-service serve,源码在./node_modules/@vue/cli-service/bin/vue-cli-service.js) 
npm run build 为生产环境

vue ui  会打开 http://localhost:8000/project/select 可以界面方式创建项目


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
https://cli.vuejs.org/zh/config/#vue-config-js

vue.config.js 是一个可选的配置文件，如果项目的 (和 package.json 同级的) 根目录中存在这个文件，那么它会被 @vue/cli-service 自动加载
//----vue.config.js 
module.exports = { 
  publicPath:'/myContext',  //默认上下文是  /
  outputDir:'dist' , //默认是dist目录
  indexPath:'index.html', //默认首页是 index.html 
}

可这样访问
http://127.0.0.1:8082/myContext/index.html


============Vue Loader 
是一个 webpack 的 loader
可以把.vue文件 输出成组件

每个 *.vue 文件都包括三部分 <template>, <script> 和 <style>

使用webpack的 style-loader




