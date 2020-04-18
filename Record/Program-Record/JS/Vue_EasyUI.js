2020-03-28   版本 1.2.6

http://www.jeasyui.net/vue

npm install vx-easyui -g  

建立项目
npm install -g @vue/cli	

vue create my-vue-project 提示选择,默认自动初始化git仓库
cd my-vue-project
npm install vx-easyui --save 只在dependencies中加了  "vx-easyui": "^1.2.6"

vue --version
显示 @vue/cli 4.2.3
 
报不认 DataGrid  原因是建立的项目结构用的vue-cli(即vue命令)是3.x版本,改用2.9就可以 
	生成的package.json 中部分  (webpack.dev.conf.js很多东西)
	"scripts": {
		"dev": "webpack-dev-server --inline --progress --config build/webpack.dev.conf.js",
	},
	"dependencies": {
		"vue": "^2.5.2",
		"vx-easyui": "^1.1.24"
	},
	"devDependencies": {
	 "webpack": "^3.6.0",
     "webpack-bundle-analyzer": "^2.9.0",
     "webpack-dev-server": "^2.9.1",
     "webpack-merge": "^4.1.0"
	}
	如版本升级  不行???
		删除node_modules目录中的vue和vx-easyui
		"vue": "^2.6.11",
		"vx-easyui": "^1.2.6"
		npm install 可能也升级了其它东西
		
如用  @vue/cli 4.2.3  不行??? 使用的是 
	"scripts": {
		"serve": "vue-cli-service serve",
	},
	"dependencies": {
		"core-js": "^3.6.4",
		"vue": "^2.6.11",
	}
    $mount('#app')写法 或  el: '#app'写法   
	报 You are using the runtime-only build of Vue where the template compiler is not available. Either pre-compile the templates into render functions, or use the compiler-included build.
	不认 <DataGrid> ????
	如版本降级 还是一样
		 删除node_modules目录中的vue和vx-easyui
		 "vue": "^2.5.2",
		 "vx-easyui": "^1.1.24"
		 再npm install

vue-cli 2.9 环境下设置vue.config.js代理  POST请求无效 ??? 
--vue.config.js (是Vue CLI 3 开始的)
module.exports = { 
	devServer: { 
		proxy: 'http://localhost:8080/S_VueEasyUI'
	}
}
--
--vue-cli 2.9 环境下设置config/index.js中的proxyTable代理 
测试OK,但要加前缀/api
proxyTable: {
	'/api':{
        target:'http://localhost:8080/S_VueEasyUI',
        changeOrigin:true,
        pathRewrite:{
            '^/api':''
        }
    }
} 
	
--src/main.js   
	import Vue from 'vue'  
    import App from './App.vue'
    
	
	//在下增加
    import 'vx-easyui/dist/themes/default/easyui.css';
    import 'vx-easyui/dist/themes/icon.css';
    import 'vx-easyui/dist/themes/vue.css';
    import EasyUI from 'vx-easyui';
    Vue.use(EasyUI);
    
    
new Vue({
  render: h => h(App),
}).$mount('#app')
/*
//这才是新写法
new Vue({
  el: '#app', 
  components: { App },
  template: '<App/>'
})
*/


---src/App.vue 

    <template>
        <div>
            <DataGrid :data="data" style="height:250px">
                <GridColumn field="itemid" title="Item ID"></GridColumn>
                <GridColumn field="name" title="Name"></GridColumn>
                <GridColumn field="listprice" title="List Price" align="right"></GridColumn>
                <GridColumn field="unitcost" title="Unit Cost" align="right"></GridColumn>
                <GridColumn field="attr" title="Attribute" width="30%"></GridColumn>
                <GridColumn field="status" title="Status" align="center"></GridColumn>
            </DataGrid>
        </div>
    </template>
    <script>
        export default {
            data() {
                return {
                    data: [
                        {"code":"FI-SW-01","name":"Koi","unitcost":10.00,"status":"P","listprice":36.50,"attr":"Large","itemid":"EST-1"},
                        {"code":"K9-DL-01","name":"Dalmation","unitcost":12.00,"status":"P","listprice":18.50,"attr":"Spotted Adult Female","itemid":"EST-10"},
                        {"code":"RP-SN-01","name":"Rattlesnake","unitcost":12.00,"status":"P","listprice":38.50,"attr":"Venomless","itemid":"EST-11"},
                        {"code":"RP-SN-01","name":"Rattlesnake","unitcost":12.00,"status":"P","listprice":26.50,"attr":"Rattleless","itemid":"EST-12"},
                        {"code":"RP-LI-02","name":"Iguana","unitcost":12.00,"status":"P","listprice":35.50,"attr":"Green Adult","itemid":"EST-13"},
                        {"code":"FL-DSH-01","name":"Manx","unitcost":12.00,"status":"P","listprice":158.50,"attr":"Tailless","itemid":"EST-14"},
                        {"code":"FL-DSH-01","name":"Manx","unitcost":12.00,"status":"P","listprice":83.50,"attr":"With tail","itemid":"EST-15"},
                        {"code":"FL-DLH-02","name":"Persian","unitcost":12.00,"status":"P","listprice":23.50,"attr":"Adult Female","itemid":"EST-16"},
                        {"code":"FL-DLH-02","name":"Persian","unitcost":12.00,"status":"P","listprice":89.50,"attr":"Adult Male","itemid":"EST-17"},
                        {"code":"AV-CB-01","name":"Amazon Parrot","unitcost":92.00,"status":"P","listprice":63.50,"attr":"Adult Male","itemid":"EST-18"}
                    ]
                }
            }
        }
    </script>
    

---AppDataGridServer.vue 
<template>
    <div>
        <h2>Pagination - Lazy Load</h2>
        <div style="margin-bottom:10px">
            <Label for="c1">Pager on: </Label>
            <ComboBox inputId="c1" style="width:120px"
                    :data="pageOptions" 
                    v-model="pagePosition" 
                    :editable="false"
                    :panelStyle="{height:'auto'}">
            </ComboBox>
        </div>
        <DataGrid style="height:250px"
                :pagination="true"
        :lazy="true"
                :data="data"
                :total="total"
        :loading="loading"
        :pageNumber="pageNumber"
                :pageSize="pageSize"
                :pagePosition="pagePosition"
        @pageChange="onPageChange($event)"

		:dblclickToEdit="true"  editMode="row"  
		> <!-- 
			:clickToEdit="true"  selectionMode="cell" editMode="cell"
		 -->
            <GridColumn field="id" title="id"  ></GridColumn>
            <GridColumn field="username" title="Name"
				:editable="true" :editRules="['required']" > <!--,'length[3,15]'-->
			</GridColumn>
            <GridColumn field="hobby" title="hobby" align="right" 
			:editable="true"> 
				<template slot="edit" slot-scope="scope">
					<ComboBox  v-model="scope.row.hobby" 
						:data='[{ value: "F", text: "Football" },
								{ value: "B", text: "Basketball" }]'></ComboBox>
				</template> 
			</GridColumn>
            <GridColumn field="salary" title="salary" align="right"
			:editable="true" editRules="required"
			>
				<template slot="edit" slot-scope="scope">
					<NumberBox v-model="scope.row.salary" :precision="1"></NumberBox>
				</template>
			</GridColumn>
            <GridColumn field="isMan" title="isMan" align="right"  :editable="true">
				<template slot="body" slot-scope="scope">
					{{scope.row.isMan ? '男' : '女'}}
				</template>
				<template slot="edit" slot-scope="scope">
					<SwitchButton v-model="scope.row.isMan" style="max-width:80px" onText="true" offText="false"></SwitchButton>
				</template>
			</GridColumn>
            <GridColumn field="birthday" title="birthday"></GridColumn>
        </DataGrid>
    </div>
</template>
 
<script>
var ctx="/api"
export default {
  data() {
    return {
      total: 0,
      pageNumber: 1,
      pageSize: 20,
      data: [],
      loading: false,
      pagePosition: "bottom",
      pageOptions: [
        { value: "bottom", text: "Bottom" },
        { value: "top", text: "Top" },
        { value: "both", text: "Both" }
      ]
    };
  },
  created() {
    this.loadPage(this.pageNumber, this.pageSize);
  },
  methods: {
    onPageChange(event) {
      this.loadPage(event.pageNumber, event.pageSize);
    },
    loadPage(pageNumber, pageSize) {
	  this.loading = true;
	  
	  var that=this;
	  //这时 Content-Type默认为 text/plain;charset=UTF-8 要修改为  application/x-www-form-urlencoded 
		var data = "page="+pageNumber+"&rows=20&my_custome_param=123"; 
		//方式二 		 
		// var data = new URLSearchParams();  // Content-Type默认为 application/x-www-form-urlencoded
		// data.set("my_custome_param",123);
		// data.set("page",pageNumber);
		// data.set("rows",pageSize); 
		 
		fetch(ctx+'/easyUI/queryJsonData', 
			{ method: "POST",
			headers: {
				//"Content-Type": "application/json;charset=UTF-8",  
				"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" 
			},
			body:data
			}
		).then(response => 
			response.json()//如debug报body stream is locked
		).then(function(result) 
		{ 
			console.log(result);
			console.log(result.rows); 
			//这里不能调用 this.setState 
			 that.data = result.rows;
			that.total = result.total;
			//that.pageNumber = result.pageNumber; 
			
			 that.loading = false;
		
		}).catch(error => 
			console.error(error)
		); 

    },
    
  }
};
</script>
---

    
    
    
    