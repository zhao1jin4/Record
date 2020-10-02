Google所收购
谷歌2012年06月15日发布了一个全新的Web模板——AngularJS 1.0。 比react要早

https://angular.io/
https://angular.cn/

官方UI组件库	https://material.angular.cn/  
UI 框架 PrimeNG	https://www.primefaces.org/primeng/

falcor(有node服务端配套)一个 取数据的ajax框架,可以配合Rxjs一起用  https://netflix.github.io/falcor/



npm install -g @angular/cli  目前版本 9.1.8

ng new my-app

? Would you like to add Angular routing? (y/N)
? Which stylesheet format would you like to use? (Use arrow keys)
> CSS

cd my-app
ng serve --open
日志提示打开 http://localhost:4200/

ng serve --port 3000  --open

ng build 代码体积最大
ng build --aot=true 代码体积中等 aot(Ahead of Time)
ng build --prod=true  代码体积最小  --prod 是 --configuration=production 的简写
ng build --outputpath=../../target
	
VS Code 搜索Angular，目前Angular Snippets (Version 9) 下载量最大 ，安装后可以有代码提示，如 *ngFor

可以直接在Chrome中调试 top-> webpack:// 展开 ./src/app/ 选中.ts文件来调试
					Fiefox也有webpack 展开src/app/  选中.ts 文件来调试


browserslist 文件是支持浏览器列表  在  not IE 9-11  行可以去除not

src/index.html		入口
src/main.ts 		入口
src/polyfills.ts  在anjular加载之前加载这个
src/styles.css		入口

ng g 查所有可用命令(generate)
ng g component mycom/new   在项目的src\app目录下 创建mycom目录，建立new组件(new目录下有很多文件) 
	会修改app.module.ts 文件增加 import { NewComponent } from './mycom/new/new.component';
	@NgModule({
	  declarations: [ //declarations放组件
		AppComponent,
		NewComponent
	  ]
	  //...
	})
就可以在 app.component.html中使用 <app-new></app-new>  对应代码   @Component({ selector: 'app-new'})


ng new my-app2 --skip-install 不要下载依赖 

--header.component.ts  (ng g component mycom/header )
printHeader()
{
  console.log("这是header组件中的方法")
}

--new.component.css
.spanColor {
	color:red
}
.divColor {
	color:grey
}
.spanFont {
	font-size:25px
}

--new.component.ts  文件中在 NewComponent类中增加属性  
import { ViewChild } from '@angular/core';

export class NewComponent implements OnInit { 
	mytitle="这是属性" //不能用var ,默认是public 
	userInfo={
		username:"lisi",
		age:20
	}
	htmlContent="<h2>这里html内容</h2>"
	arr=["one","two","three"]
	//arr:Array<string>=["one","two","three"]//typestript
	//arr:any[]=["one","two","three"]//typestript
	
	hobbyEnCN=[{en:"",cn:"请选择","check":false},{en:"football",cn:"足球","check":true},{en:"basketball",cn:"蓝球","check":true}]
	hobbyCheck=[{en:"football",cn:"足球","check":true},{en:"basketball",cn:"蓝球","check":true}]
	
    student=[
		{
			fullname:'lisi',
			gender:'F',
			hobbies:["football","basketball"],
			firstHobby:"football"
		},
		{
			fullname:'wang',
			gender:'M',
			hobbies:["basketball"]
		}
	]
	baidu="http://www.baidu.com"
	showImg=false;
	toggleStyle=true;
	attrColor='yellow';
	now=new Date();
	keywords="front end"
	
	@ViewChild("viewChild1") vc1:any //找 #viewChild1  的元素
	@ViewChild("headerCom") headerCom:any//可以是子组件
  constructor() {
	  this.mytitle='构造器赋值'
   }

  ngOnInit(): void {//一般用于请求数据
	  console.log("当页面刷新时初始化"); 
	  let myTextArea:any=document.getElementById("myTextArea")
	  console.log(myTextArea);
	  myTextArea.style.color='red';
	//如果是要angular初始化的这里不能取到 
	 let checkbox:any=document.getElementById("check_0")
	 console.log("ngOnInit checkbox="+checkbox);
  }
  //全部初始化后
  ngAfterViewInit():void{ 
	 let checkbox:any=document.getElementById("check_0")
	 console.log("ngAfterViewInt checkbox="+checkbox);
	 
	 console.log("vc1=");
	 console.log(this.vc1);//可以看控制台有 nativeElement 属性 
	 this.vc1.nativeElement.style.background='red'
  }
  invokeChildComp()
  {
	this.headerCom.printHeader();//直接调用子组件的方法
  }
  
  changeData(){
	this.mytitle="修改后的数据"
	}
	myOnClick(){ //可以没有:void
		console.log("事件响应");
	}
	myOnKeyDown(evt)
	{
		console.log("myOnKeyDown,keyCode="+evt.keyCode);
		console.log("myOnKeyDown,input value="+evt.target.value);
	}
	
	changeKeyword()
	{
		this.keywords="修改的keyword"
	}
	getKeyword()
	{
		console.log("keywords="+this.keywords);
	}
	myOnCheckBoxChange()
	{
		console.log("myOnCheckBoxChange");
	}
}


--new.component.html 	文件中就可以直接使用属性 
<p>app-new 新闻组件 mytitle={{mytitle}}</p>
<p>  userInfo.username={{userInfo.username}}</p>
<div title={{mytitle}}>html attribute ,鼠标滑过看mytitle的值</div>

<div [title]="mytitle">html attribute ,鼠标滑过看mytitle的值,表示值是变量</div>
{{htmlContent}}  --- 这里属性中的HTML被escape显示在这, <br/>放在双引号中

<span [innerHtml]="htmlContent" class="spanColor"></span>
<br/>  1+2={{1+2}} 

<ul> 
	<li *ngFor="let item of arr;let i=index;"  >
		 <span *ngIf="i%2 ==0" style="background-color:blue">	{{i}} :  {{item}}</span>
		 <span *ngIf="i%2 !=0" style="background-color:grey">	{{i}} :  {{item}}</span>
	</li> 
	<li *ngFor="let item of arr;let i=index;"  >
		<span [ngClass]="{'divColor': i%2 ==0 ,'spanColor':i%2!=0}">{{i}} :  {{item}}</span>
	</li> 
	 
</ul>


一个标签只能写一个指令
<!--
<li *ngFor="let item of arr;let i=index;"  *ngIf="i%2 ==0"></li>
-->
<li *ngFor="let item of arr;let i=index;"  [hidden]="i%2 ==0">{{i}} :  {{item}}</li>

<ul> 
	<li *ngFor="let item of student">
		<h2>
			{{item.fullname}} :  
			<div [ngSwitch]="item.gender">
				<div *ngSwitchCase="'F'">女</div>
				<div *ngSwitchCase="'M'">男</div>
				<div *ngSwitchDefault>未知</div>
			</div>
		 </h2> 
		<ol>
			<li *ngFor="let hobby of item.hobbies">
				{{hobby}}
			</li>
		</ol>
	</li> 
</ul>

<a [href]="baidu">引用外部地址 百度</a> 
<div *ngIf="showImg"> 如showImg为false就不显示这个div
	<img src="assets/anjular.png" /> 引用图片目录是 相对 于项目根目录 <br/>
</div>
<div *ngIf="!showImg"> 
	不显示 ！
</div>

<div [ngClass]="{'spanColor': false,'spanFont':true}">ngClass返回true表示要某个样式</div>
<div [ngClass]="{'spanColor': toggleStyle,'spanFont':!toggleStyle}">ngClass变量控制</div>
<div [ngStyle]="{'color': 'red'}">ngStyle 常量值加''</div>
<div [ngStyle]="{'color': attrColor}">ngStyle 变量控制</div>


管道 格式化日期 {{now | date:'yyyy-MM-dd HH:mm:ss'}} ，还有其它管道方法，还可以自定义管道
<br/>
<button (click)="myOnClick()">事件</button> <br/>
<strong>{{mytitle}}</strong> 
<button (click)="changeData()">修改数据</button>
<input (keydown)="myOnKeyDown($event)"/>  input 的结束必须在第一个开始标签中<br/>

----- 双向绑定MVVM (也可是对象) <input type="text" [(ngModel)]='keywords'/> <br/>
 {{keywords}}
 <button (click)="changeKeyword()">修改Keyword</button>
 <button (click)="getKeyword()">get Keyword</button>
 <br/>  可以用DOM方式取元素(起ID)  <br/> 
 <input type="radio" value="M" name="sex" [(ngModel)]="student[0].gender" id="man"/> <label for ="man">男 </label>
 <input type="radio" value="F" name="sex" [(ngModel)]="student[0].gender" id="woman"/> <label for ="woman">女 </label>
 <br/>管道json =  {{student[0] | json}}

<br/>{{student[0].gender}}  
<br/>
 <select [(ngModel)]="student[0].firstHobby" >
	<option [value]="item.en" *ngFor="let item of hobbyEnCN">{{item.cn}}</option>
 </select> 

 select多选，值的个数是动态增加或减少的  <br/>
 <select multiple [(ngModel)]="student[0].hobbies">
	<option [value]="item.en" *ngFor="let item of hobbyEnCN">{{item.cn}}</option>
 </select>

 <br/>
 checkbox多选，值的个数是固定的，值true,false表示有没有选中  <br/>
 <span *ngFor="let item of hobbyCheck;let key=index;"> 
	 <input type="checkbox"   [id]="'check_'+key" [(ngModel)]="item.check" (change)="myOnCheckBoxChange()"/> <label [for]="'check_'+key">{{item.cn}} </label> --- {{item.cn}} <br/>
 </span> 
 <br/>管道json =  {{hobbyCheck | json}}
<textarea [(ngModel)]="htmlContent"></textarea>  HTML的内容可正常显示
<div #viewChild1 style="width:200px;">viewChild1</div> 
<app-header #headerCom></app-header>
<button (click)="invokeChildComp()">调用子组件方法</button>





组件如要使用公共的功能，使用Service(同级组件不能相互访问)

ng g service myservices/storage
---app.module.ts 入口模块中导入
import { StorageService } from './myServices/storage.service' //服务
增加到 providers组中，即  providers: [StorageService]


--- header.component.ts 组件中 使用模块  
import { StorageService } from '../../myServices/storage.service' //服务

//var storage=new StorageService();//不推荐这样用
//console.log(storage);


// constructor() {
constructor(public  storage:StorageService) {//相当于直接new 了,public可省
  storage.printAll();
}



-- 展开右边栏示例 
ng g component mycom/rightSide 

<button (click)="toggleRight()">展开收起右边栏</button>
<aside id="aside">
		<p>右边栏!</p>
</aside>


rigthOpen:boolean=false;
toggleRight()
{
  //var right=document.querySelector("#aside");//这个可能是多个吧，用style报错
  var right=document.getElementById("aside");
  if(this.rigthOpen) 
	  right.style.transform="translate(100%,0)";
   else 
	  right.style.transform="translate(0,0)";

  this.rigthOpen=!this.rigthOpen;
}

#aside{
	width: 200px;
	height:100%;
	position: absolute;
	right:0px;
	top:0px;
	background: black;
	color:white;
	transform: translate(100%,0); /*移动变形 ,向右称动自身宽度,出现滚动条，styles.css中body级加 width:100%;overflow-x:hidden*/
	transition: all 1s; /* 动画过渡 1秒完成*/
}

----父向子传 属性/方法(子回调父)
receiveNotify(msg)
{
	console.log("receiveNotify,msg="+msg);
}
	
父组件  <!-- 向子组件传名为newTitle值 为属性名(本身组件mytitle的值),可以传整个父子组件this(不建议用),可以是方法名,要求子知道父的方法签名   -->
<app-header   [newTitle]="mytitle" [newFunc]="receiveNotify"></app-header>


子组件要 import { Input } from '@angular/core';
@Input() newTitle:any //接收父子组传来的newTitle属性
invokeParent()
{
this.newFunc('子调用父的参数');
}


---子通知父方式二，子组件使用Output 
import { Output,EventEmitter } from '@angular/core'; 
@Output() notify=new EventEmitter() //子通知父

sendParent()
{
 this.notify.emit('来自子组件的通知数据')//应该只可一个参数(相当于方法签名被定义)
}
 

父中使用
<app-header  (notify)="receiveNotify($event)"  ></app-header>
	
---异步请求数据
可以使用回调函数，和Promise
var promise=new Promise(resolve => {
		setTimeout(() => {
		  resolve('promise_data');
		}, 2000);
	});
	
promise.then((data)->{
		console.log(data);
	});
	
	
----Rxjs
// 在service 中 
import {Observable} from 'rxjs'
import {map,filter} from 'rxjs/operators'

useRxjsQuery(){
	  var cnt=1;
	  return new Observable((observer)=>{
		var inter=setInterval(() => { 
			observer.next(cnt++);
			//observer.next("rxjs_data" + cnt++); //可以不止一次，一直发数据
			//observer.error("rxjs_fail");
		}, 1000);	

		setTimeout(()=>{
			console.log("7秒后取消发送")
			clearInterval(inter);
		},7000);

	  });
  }
	
// 在组件中 
import {map,filter} from 'rxjs/operators'

asyncInvokeRxjsService()
{
	var rxjxData=storage.useRxjsQuery();
	// var sub=rxjxData.subscribe((data)=>{ 
	// 	console.log(data);
	// });
	
	//filter,map其实可以自己处理的
	var sub=rxjxData.pipe(filter((value:any)=>{
			if(value % 2==0)
				return true; 
			return false;
	})).pipe(map((value:any)=>{
		return 100+value;
	})).subscribe((data)=>{ 
		console.log(data);
	});
	

	setTimeout(()=>{
		sub.unsubscribe();//取消异步执行
		console.log('5.5秒后取消异步调用')
	},5500)
}
-------------httpclient
app.module.ts中
import {HttpClientModule} from '@angular/common/http'

@NgModule( 中的
  imports: [ 加入 //imports放模块
	HttpClientModule
 ])
在要使用组件的地方
import {HttpClient} from '@angular/common/http'

constructor(public  httpClient:HttpClient)

var url="http://127.0.0.1:8080/S_jQuery/jQueryAjaxJsonList"  
//var url="asset/serverData.json";//不行
//是ajax请求

// this.httpClient.get(url).subscribe((resp:any)=>{
// 	console.log(resp);
// 	this.serverArray=resp; 
// });

import {HttpClient, HttpHeaders} from '@angular/common/http'
//post
var httpOption={
	headers:new HttpHeaders({
		'Content-Type':'application/json'
	})
}
var url="http://127.0.0.1:8080/S_HTML5CSS3/corsComplex" 
//var url="http://127.0.0.1:3000/news" 
this.httpClient.post(url,{username:'lisi'},httpOption).subscribe((resp:any)=>{
	console.log(resp); 
});

-------------也可以使用axios 结合Promise 请求数据
------------ 路由 建立项目是选择Route
有app-routing.module.ts  文件，有在app.module.ts中被引用
---app.component.html  
<a routerLink ="/new" routerLinkActive="activeClass" [queryParams] ="{userId:'1'}" >跳到new组件，不刷新页,参数显示在地址栏上，值可是变量</a> <br/>
<a [routerLink] ="['/header/',100]" routerLinkActive="activeClass" >跳到header组件，不刷新页,参数/header/100显示在地址栏上，值可是变量</a>

<router-outlet></router-outlet> <!-- 这里显示的是动态组件-->

/*
.activeClass{
	color:red
}
*/
--app-routing.module.ts 文件中修改 const routes: Routes = [];

const routes: Routes = [
	{path:'new' ,component:NewComponent}, //如请求路径为 http://localhost:4200/new 就显示NewComponent组件(动态增加)
	{path:'header/:studentId' ,component:HeaderComponent},
	//{path:'**' ,redirectTo:'new'}, //如前面匹配不到，到指定的默认路径 
];

--接收参数的组件中
import { ActivatedRoute } from '@angular/router' //用于接收路由参数

constructor(public activeRoute:ActivatedRoute) {
} 
	
 this.activeRoute.queryParams.subscribe((data)=>{//queryParams 是一个Rxjs对象
	 console.log("new 接收到route传来的参数userId="+data.userId); 
  });
  
 
this.activeRoute.params.subscribe((data)=>{//params 是一个Rxjs对象,来接收 /:studentId  
		console.log("header接收到route传来的参数studentId="+data.studentId); 
	 });
  
---JS控制的跳转
import { Router,NavigationExtras } from '@angular/router';
  constructor(public router:Router){ 
  }
  
jumpNew(){
  var extra:NavigationExtras={ //typescript语法的:NavigationExtras可不加
	queryParams:{"userId":1}, 
  }
this.router.navigate(['/new'],extra); 
}
jumpHeader(){
  this.router.navigate(['/header/',100]);
}
-----------嵌套路由
ng g component nestcom
ng g component nestcom/my
ng g component nestcom/my/favo
ng g component nestcom/my/order

ng g component nestcom/product
ng g component nestcom/product/food
ng g component nestcom/product/cloth
--app-routing.module.ts
{
	path:'my' ,component:MyComponent,
	children:[
		{path:'favo' ,component:FavoComponent},
		{path:'order' ,component:OrderComponent},
	]
}, 
{
	path:'product' ,component:ProductComponent,
	children:[
		{path:'cloth' ,component:ClothComponent},
		{path:'food' ,component:FoodComponent},
	
	]
},
---nestcom.component.html
<p>nestcom works!</p>
<a routerLink ="/my"   >我的个人中心</a> &nbsp;  &nbsp; <a routerLink ="/product"   >产品中心</a> <br/>
<!-- 以上是快速路径 -->
<a routerLink ="/my/favo"   >我的收藏</a> <br/>
<a routerLink ="/my/order"   >我的订单</a> <br/>

<a routerLink ="/product/cloth"   >产品/衣服</a> <br/>
<a routerLink ="/product/food"   >产品/食品</a> <br/>

<div>
	<router-outlet></router-outlet>  
</div>
---my.component.html
<p>my works!</p> 
<a routerLink ="/my/favo"   >我的收藏</a> <br/>
<a routerLink ="/my/order"   >我的订单</a> <br/>
<div>
	<router-outlet></router-outlet>  
</div>
---product.component.html
<p>product works!</p>
<a routerLink ="/product/cloth"   >产品/衣服</a> <br/>
<a routerLink ="/product/food"   >产品/食品</a> <br/>
<div>
	<router-outlet></router-outlet>  
</div>

-----------自定义模块
ng g module mymodule/user  --routing  自动增加模块级路由
#ng g module mymodule/article  --routing
ng g component mymodule/user
#ng g component mymodule/article
ng g component mymodule/user/com/profile


如要外部模块使用 本模块中的组件要exports(像java)
即在 @NgModule({ 中
	exports:[UserComponent]
})

使用自定义模块必须导入,可不导入组件


-----------父模块路由实现模块延迟加载子模块 
子模块自身 user-routing.module.ts 要加
{ 
	path:'' ,component:UserComponent,  
}

就不用在app.module.ts中引入子模块了
--app-routing.module.ts中
{
	//loadChildren 模块延迟加载
	path:'user' ,loadChildren:'./mymodule/user/user.module#UserModule',
}

--app.component.html中
<a [routerLink] ="['/user']"  >跳到user组件</a>

 
--------国际化

在assets 新建文件夹 i18n,在i18n文件下下新建zh.json 和 en.json 文件
---zh.json
{
  "hello": "你好 {{name}}",
  "header": {
   "author": "早上好"
  }
}
---en.json
{
  "hello": "Hello {{name}}",
  "header": {
   "author": "Good morning"
  }
}
 
npm install @ngx-translate/core  --save
npm install @ngx-translate/http-loader  --save　

---app.module.ts

import {HttpClient} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
  
export function createTranslateHttpLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule  下的    imports 中加入
	
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateHttpLoader),
        deps: [HttpClient]
      }
    })
	

---app.component.ts 

import {OnInit} from '@angular/core';//为国际化
import {TranslateService} from '@ngx-translate/core';

修改 implements OnInit 
	
	languageBtn;//为选择语言
    language; 
	
	constructor(public translateService: TranslateService) {
	}
	 
	ngOnInit() { 
		this.translateService.addLangs(['zh', 'en']);
		this.translateService.setDefaultLang('zh');
		const browserLang = this.translateService.getBrowserLang();
		this.translateService.use(browserLang.match(/zh|en/) ? browserLang : 'zh');
		
		 this.settingBtn(browserLang);//为选择语言
	}
   //以下为选择语言
   settingBtn(language: string) {
    if (language === 'zh') {
      this.languageBtn = 'English';
      this.language = 'en';
    } else {
      this.languageBtn = '中文';
      this.language = 'zh';
    }
  } 
  changeLanguage(lang: string) {
    console.log(lang);
    this.translateService.use(lang);
    this.settingBtn(lang);
  }
  
--app.component.html
<h1>{{'hello' | translate:{name:"lisi"}}}</h1>
<h1>{{'header.author' | translate}}</h1>   
 选择语言
 <button (click)="changeLanguage(language)">{{ languageBtn}}</button>


------<ng-template> 类似HTML5的 <template>
<br/> ng-template内容默认不显示,如 [ngIf]="true"会显示
<ng-template [ngIf]="true">
	<p> ngIf with a ng-template.</p>
</ng-template>






