
目前最新版本 1.1.24 

http://www.jeasyui.net/vue

npm install vx-easyui -g

建立项目
npm install -g @vue/cli	

vue create my-vue-project
cd my-vue-project
npm install vx-easyui --save

--src/main.js  未成功？？？ ??? 报不认 DataGrid 
原因是建立的项目结构用的vue-cli(即vue命令)是3.x版本，用2.9就可以 （官方说是支持vue-1.1但用vue-2.5.2可以的）
 

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
  router,
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
    
    
    
    
    
    
    
    
    