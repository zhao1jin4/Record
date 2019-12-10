
就这一个，不像react有两个bootstrap版本

https://bootstrap-vue.js.org

------------直接引入 
<!-- 
<link type="text/css" rel="stylesheet" href="https://unpkg.com/bootstrap/dist/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="https://unpkg.com/bootstrap-vue@latest/dist/bootstrap-vue.min.css" />
 
<script src="https://unpkg.com/vue@latest/dist/vue.min.js"></script>
<script src="https://unpkg.com/bootstrap-vue@latest/dist/bootstrap-vue.min.js"></script>
 -->
 <link type="text/css" rel="stylesheet" href="bootstrap-4.3.1_unpkg/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="bootstrapvue-2.0.0-rc.27_unpkg/bootstrap-vue.min.css" />
 <script src="vue-2.6.10/vue.min.js"></script>
 <script src="bootstrapvue-2.0.0-rc.27_unpkg/bootstrap-vue.min.js"></script>

---button 测试成功
	<div id="buttonDiv">
	  <b-button>Button</b-button>
	  <b-button variant="danger">Button</b-button>
	  <b-button variant="success">Button</b-button>
	  <b-button variant="outline-primary">Button</b-button>
	</div>
	<script type="text/javascript">
	var app = new Vue({
		  el: '#buttonDiv',
		  data: { }
		})
	</script>
---alert 测试成功
 <div id="alertDiv">
    <b-alert show>Default Alert</b-alert>

    <b-alert variant="success" show>Success Alert</b-alert>

    <b-alert v-model="showDismissibleAlert" variant="danger" dismissible>
      Dismissible Alert!
    </b-alert>

    <b-alert
      :show="dismissCountDown"
      dismissible
      variant="warning"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
    >
      <p>This alert will dismiss after {{ dismissCountDown }} seconds...</p>
      <b-progress
        variant="warning"
        :max="dismissSecs"
        :value="dismissCountDown"
        height="4px"
      ></b-progress>
    </b-alert>

    <b-button @click="showAlert" variant="info" class="m-1">
      Show alert with count-down timer
    </b-button>
    <b-button @click="showDismissibleAlert=true" variant="info" class="m-1">
      Show dismissible alert ({{ showDismissibleAlert ? 'visible' : 'hidden' }})
    </b-button>
  </div>
	<script type="text/javascript">
	var app = new Vue({
		  el: '#alertDiv', 
		 	data() {
		      return {
		        dismissSecs: 10,
		        dismissCountDown: 0,
		        showDismissibleAlert: false
		      }
		    },
		    methods: {
		      countDownChanged(dismissCountDown) {
		        this.dismissCountDown = dismissCountDown
		      },
		      showAlert() {
		        this.dismissCountDown = this.dismissSecs
		      }
		    }
		})
	</script>

------------模块式

bootstrap@4.3.1 
bootstrap-vue@2.0.0-rc.27 

npm install vue bootstrap-vue bootstrap -g
npm install vue bootstrap-vue bootstrap --save

 
---main.js
import Vue from 'vue'
//import App from './App.vue' 
import App from './AppButton.vue' 
//import App from './AppAlert.vue' 

import BootstrapVue from 'bootstrap-vue'
//import { LayoutPlugin } from 'bootstrap-vue'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(BootstrapVue)

//Vue.use(LayoutPlugin)

Vue.config.productionTip = false

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

--- AppButton.vue 测试成功
<template>
<div>
  <b-button>Button</b-button>
  <b-button variant="danger">Button</b-button>
  <b-button variant="success">Button</b-button>
  <b-button variant="outline-primary">Button</b-button>
</div>
</template>

<script> 
export default {
  name: 'app',
  components: { 
  }
}
</script>

----AppAlert.vue 测试成功
<template> 
 <div>
    <b-alert show>Default Alert</b-alert>

    <b-alert variant="success" show>Success Alert</b-alert>

    <b-alert v-model="showDismissibleAlert" variant="danger" dismissible>
      Dismissible Alert!
    </b-alert>

    <b-alert
      :show="dismissCountDown"
      dismissible
      variant="warning"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
    >
      <p>This alert will dismiss after {{ dismissCountDown }} seconds...</p>
      <b-progress
        variant="warning"
        :max="dismissSecs"
        :value="dismissCountDown"
        height="4px"
      ></b-progress>
    </b-alert>

    <b-button @click="showAlert" variant="info" class="m-1">
      Show alert with count-down timer
    </b-button>
    <b-button @click="showDismissibleAlert=true" variant="info" class="m-1">
      Show dismissible alert ({{ showDismissibleAlert ? 'visible' : 'hidden' }})
    </b-button>
  </div>

</template>

<script>  
export default {
    data() {
      return {
        dismissSecs: 10,
        dismissCountDown: 0,
        showDismissibleAlert: false
      }
    },
    methods: {
      countDownChanged(dismissCountDown) {
        this.dismissCountDown = dismissCountDown
      },
      showAlert() {
        this.dismissCountDown = this.dismissSecs
      }
    }
  }

</script>
 
 
