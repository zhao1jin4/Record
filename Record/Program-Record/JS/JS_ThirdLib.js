  
------------------ ESLint
一个JavaScript代码规范检查工具,支持 ES2015 语法,JSX

npm install eslint -g
npm install eslint --save-dev

目前最新版本 5.16.0


./node_modules/.bin/eslint --init   建立配置文件 如使用了-g安装就可直接用 eslint --init 
有交互提示的
框架是Reac还是Vue.js
Browser 还是 Node
配置文件 默认是javascript 格式的 (可以是 YAML,JSON)  生成文件名为 .eslintrc.js 

./node_modules/.bin/eslint src/*.js      支持*这种能配符 */
npx eslint src/*.js     也可以使用npx*/


---插件
npm install  eslint-plugin-react -g
npm install eslint-plugin-react-hooks  -g
npm install eslint-plugin-react-hooks --save-dev   来强制执行这Hook规则

 
============Baidu  echart 
免费的
版本  3.6.2 
   <script src="echarts.min.js"></script>
   
  <div id="main" style="width: 600px;height:400px;"></div>
  <!-- 柱状图 -->
    <script type="text/javascript"> 
        var myChart = echarts.init(document.getElementById('main')); 
        var option = {
            title: {
                text: 'ECharts 入门示例'
            },
            tooltip: {},
            legend: {
                data:['销量']
            },
            xAxis: {
                data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20]
            }]
        }; 
        myChart.setOption(option);
    </script>
    
--- 饼图

   option = {
    title : {
        text: '某站点用户访问来源',
        subtext: '纯属虚构',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
    },
    series : [
        {
            name: '访问来源',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {value:335, name:'直接访问'},
                {value:310, name:'邮件营销'},
                {value:234, name:'联盟广告'},
                {value:135, name:'视频广告'},
                {value:1548, name:'搜索引擎'}
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

 ---- 嵌套环形图 

        option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:['直达','营销广告','搜索引擎','邮件营销','联盟广告','视频广告','百度','谷歌','必应','其他']
            },
            series: [
                {
                    name:'访问来源',
                    type:'pie',
                    selectedMode: 'single',
                    radius: [0, '30%'],

                    label: {
                        normal: {
                            position: 'inner'
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:[
                        {value:335, name:'直达', selected:true},
                        {value:679, name:'营销广告'},
                        {value:1548, name:'搜索引擎'}
                    ]
                },
                {
                    name:'访问来源',
                    type:'pie',
                    radius: ['40%', '55%'],

                    data:[
                        {value:335, name:'直达'},
                        {value:310, name:'邮件营销'},
                        {value:234, name:'联盟广告'},
                        {value:135, name:'视频广告'},
                        {value:1048, name:'百度'},
                        {value:251, name:'谷歌'},
                        {value:147, name:'必应'},
                        {value:102, name:'其他'}
                    ]
                }
            ]
        };

--- 组合柱状图
    
<div id="multiLine" style="width: 600px;height:400px;"></div>.
 <script type="text/javascript"> 
    
 //   app.title = '折柱混合';

option = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            crossStyle: {
                color: '#999'
            }
        }
    },
    /*
	toolbox: {
        feature: {
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: true}
        }
    },
	*/
    legend: {
        data:['蒸发量','降水量','平均温度']
    },
    xAxis: [
        {
            type: 'category',
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
            axisPointer: {
                type: 'shadow'
            }
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '水量',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
                formatter: '{value} ml'
            }
        },
        {
            type: 'value',
            name: '温度',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
                formatter: '{value} °C'
            }
        }
    ],
    series: [
        {
            name:'蒸发量',
            type:'bar',
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
        },
        {
            name:'降水量',
            type:'bar',
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        },
       /* {
            name:'平均温度',
            type:'line',
            yAxisIndex: 1,
            data:[2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
        }
        */
    ]
};

var myChart = echarts.init(document.getElementById('multiLine'));
myChart.setOption(option);
   </script> 

		
---- 水平 柱状图
    
<div id="HLine" style="width: 600px;height:400px;"></div>.

    <script type="text/javascript">
   // app.title = '世界人口总量 - 条形图';

    option = {
        title: {
            text: '世界人口总量',
            subtext: '数据来自网络',
			x:'center' //手工调整的
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['2011年', '2012年'],
			x: 'left',   //手工调整的
            orient: 'vertical',//手工调整的
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: ['巴西','印尼','美国','印度','中国','世界人口(万)']
        },
        series: [
            {
                name: '2011年',
                type: 'bar',
                data: [18203, 23489, 29034, 104970, 131744, 630230]
            },
            {
                name: '2012年',
                type: 'bar',
                data: [19325, 23438, 31000, 121594, 134141, 681807]
            }
        ]
    };
    var myChart = echarts.init(document.getElementById('HLine'));
    myChart.setOption(option);
    
    </script>
	
	
	
	
	     水平 柱状图  (加载数据前也有显示)
    <input type="button" value="getData" onclick="getDate()"/>
    <div id="arriveUnloadBarDiv"  style="width: 1000px;height:400px;"></div>
    <script>
	    var unloadBarChart = echarts.init(document.getElementById('arriveUnloadBarDiv'));
	    optionUnload = {
	        title: {
	            text: '已到未卸车',
	            subtext: '按转运中心',
	            x:'center'
	        }
	    };
	    unloadBarChart.setOption(optionUnload);
	    
	    function getDate()
	    {
	    	var optionUnload=
	    	{
	    	  tooltip: {
	              trigger: 'axis',
	              axisPointer: {
	                  type: 'shadow'
	              }
	          },
	          legend: {
	              data: ['2011年', '2012年'],
	  			x: 'left',   //手工调整的
	              orient: 'vertical',//手工调整的
	          },
	          grid: {
	              left: '3%',
	              right: '4%',
	              bottom: '3%',
	              containLabel: true
	          },
	          xAxis: {
	              type: 'value',
	              boundaryGap: [0, 0.01]
	          },
	          yAxis: {
	              type: 'category',
	              data: ['巴西','印尼','美国','印度','中国','世界人口(万)']
	          },
	          series: [
	              {
	                  name: '2011年',
	                  type: 'bar',
	                  data: [18203, 23489, 29034, 104970, 131744, 630230]
	              },
	              {
	                  name: '2012年',
	                  type: 'bar',
	                  data: [19325, 23438, 31000, 121594, 134141, 681807]
	              }
	          ]
	    	}; 
	    	 unloadBarChart.setOption(optionUnload);
	    }
    </script>
    
	
--- 组合线性图
  option = {
        	    title: {
        	        text: '堆叠区域图'
        	    },
        	    tooltip : {
        	        trigger: 'axis',
        	        axisPointer: {
        	            type: 'cross',
        	            label: {
        	                backgroundColor: '#6a7985'
        	            }
        	        }
        	    },
        	    legend: {
        	        data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
        	    },
        	    toolbox: {
        	        feature: {
        	            saveAsImage: {}
        	        }
        	    },
        	    grid: {
        	        left: '3%',
        	        right: '4%',
        	        bottom: '3%',
        	        containLabel: true
        	    },
        	    xAxis : [
        	        {
        	            type : 'category',
        	            boundaryGap : false,
        	            data : ['周一','周二','周三','周四','周五','周六','周日']
        	        }
        	    ],
        	    yAxis : [
        	        {
        	            type : 'value'
        	        }
        	    ],
        	    series : [
        	        {
        	            name:'邮件营销',
        	            type:'line',
        	            stack: '总量',
        	            areaStyle: {normal: {}},
        	            data:[120, 132, 101, 134, 90, 230, 210]
        	        },
        	        {
        	            name:'联盟广告',
        	            type:'line',
        	            stack: '总量',
        	            areaStyle: {normal: {}},
        	            data:[220, 182, 191, 234, 290, 330, 310]
        	        },
        	        {
        	            name:'视频广告',
        	            type:'line',
        	            stack: '总量',
        	            areaStyle: {normal: {}},
        	            data:[150, 232, 201, 154, 190, 330, 410]
        	        } 
        	    ]
        	};
			
			
	--- 线性 Ajax  放大 缩小
    <script src="jquery-3.2.1.min.js"></script>
	
<div id="main" style="width: 800px;height:800px;"></div>  <!--  一定要放在 document.getElementById('main') 前才行-->
 <script type="text/javascript">
 var myChart = echarts.init(document.getElementById('main'));
 $.get('lineAjax.json', function (data) {
	    myChart.setOption(option = {
	        title: {
	            text: 'Beijing AQI'
	        },
	        tooltip: {
	            trigger: 'axis'
	        },
	        xAxis: {
	            data: data.map(function (item) {
	                return item[0];
	            })
	        },
	        yAxis: {
	            splitLine: {
	                show: false
	            }
	        },
	        toolbox: {
	            left: 'center',
	            feature: {
	                dataZoom: {
	                    yAxisIndex: 'none'
	                },
	                restore: {},
	                saveAsImage: {}
	            }
	        },
	        dataZoom: [{
	            startValue: '2014-06-01'
	        }, {
	            type: 'inside'
	        }],
	        visualMap: {
	            top: 10,
	            right: 10,
	            pieces: [{
	                gt: 0,
	                lte: 50,
	                color: '#096'
	            }, {
	                gt: 50,
	                lte: 100,
	                color: '#ffde33'
	            }, {
	                gt: 100,
	                lte: 150,
	                color: '#ff9933'
	            }, {
	                gt: 150,
	                lte: 200,
	                color: '#cc0033'
	            }, {
	                gt: 200,
	                lte: 300,
	                color: '#660099'
	            }, {
	                gt: 300,
	                color: '#7e0023'
	            }],
	            outOfRange: {
	                color: '#999'
	            }
	        },
	        series: {
	            name: 'Beijing AQI',
	            type: 'line',
	            data: data.map(function (item) {
	                return item[1];
	            }),
	            markLine: {
	                silent: true,
	                data: [{
	                    yAxis: 50
	                }, {
	                    yAxis: 100
	                }, {
	                    yAxis: 150
	                }, {
	                    yAxis: 200
	                }, {
	                    yAxis: 300
	                }]
	            }
	        }
	    });
	});
 
</script>

 --- 线性     动态图

 <div id="main" style="width: 600px;height:400px;"></div>
   
 <script type="text/javascript">
     function randomData() {
    	    now = new Date(+now + oneDay);
    	    value = value + Math.random() * 21 - 10;
    	    return {
    	        name: now.toString(),
    	        value: [
    	            [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'),
    	            Math.round(value)
    	        ]
    	    }
    	}

    	var data = [];
    	var now = +new Date(1997, 9, 3);
    	var oneDay = 24 * 3600 * 1000;
    	var value = Math.random() * 1000;
    	for (var i = 0; i < 1000; i++) {
    	    data.push(randomData());
    	}

    	option = {
    	    title: {
    	        text: '动态数据 + 时间坐标轴'
    	    },
    	    tooltip: {
    	        trigger: 'axis',
    	        formatter: function (params) {
    	            params = params[0];
    	            var date = new Date(params.name);
    	            return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
    	        },
    	        axisPointer: {
    	            animation: false
    	        }
    	    },
    	    xAxis: {
    	        type: 'time',
    	        splitLine: {
    	            show: false
    	        }
    	    },
    	    yAxis: {
    	        type: 'value',
    	        boundaryGap: [0, '100%'],
    	        splitLine: {
    	            show: false
    	        }
    	    },
    	    series: [{
    	        name: '模拟数据',
    	        type: 'line',
    	        showSymbol: false,
    	        hoverAnimation: false,
    	        data: data
    	    }]
    	};
    	 var myChart = echarts.init(document.getElementById('main'));
    	 myChart.setOption(option);
    	 
    	setInterval(function () {

    	    for (var i = 0; i < 5; i++) {
    	        data.shift();
    	        data.push(randomData());
    	    }

    	    myChart.setOption({
    	        series: [{
    	            data: data
    	        }]
    	    });
    	}, 1000);

	
</script> 


	


===================SyntaxHighlighter  只是显示高亮
可以在 页面中显示像开发工具中的代码,关键字带颜色,并可以正常复制,支持多种语言,如Java,C,JS,AS3

<link type="text/css" rel="stylesheet" href="css/shCore.css"/>
<link type="text/css" rel="stylesheet" href="css/shThemeDefault.css"/>
<script type="text/javascript" src="js/shCore.js" ></script>
<script type="text/javascript" src="js/shBrushJava.js" ></script>
<script type="text/javascript">
	SyntaxHighlighter.all() 
</script>
<!--  auto-links: true; collapse: false; first-line: 1; gutter: true; html-script: false; light: false; ruler: false; smart-tabs: true; tab-size: 4; toolbar: true; -->	
<pre class="brush: java;">
/**
 * author
 */
package test;
import java.io.FileInputStream;
public static void main(String args[])
{
	int a=10;//test
	String str="abc";
	System.out.println("hello");
}
</pre>
===================CodeMirror  在线编辑 代码 语法高亮
codemirror-5.20.2\mode\sql  有示例
 
<script src="js/codemirror.js"></script>
<script src="js/mode/sql.js"></script>

<link rel="stylesheet" href="css/codemirror.css">
<style type="text/css">
.CodeMirror {
	border-top: 1px solid black;
	border-bottom: 1px solid black;
	border-right: 1px solid black;
}
</style>
            
<script type="text/javascript" >
	//$(function(){   });
	window.onload = function() 
	{
		var mime = 'text/x-mariadb';
		 window.editor = CodeMirror.fromTextArea(document.getElementById('sql'), {
				mode: mime,
				lineNumbers: true,
				autofocus: true
		  });
	}
	
	var sql=window.editor.getValue();
	
	
</script>
 SQL  :  <textarea   id="sql"> select table_name from information_schema.TABLES where table_schema='db_name'  </textarea>
   

==============ckeditor 5 
classic -11



<script type="text/javascript" src="classic/ckeditor.js"></script>
<script type="text/javascript" src="classic/zh-cn.js"></script>
 
 CKEditor5-classic 11 version <br/>
<textarea name="content" id="editor">
    &lt;p&gt;Here goes the initial content of the editor.&lt;/p&gt;
</textarea>

<script type="text/javascript"> <!-- 要放在<textarea id="editor">后面 -->
ClassicEditor.create( document.querySelector( '#editor' ) )
	.then( editor => {
	    console.log( editor );
	} )
	.catch( error => {
	    console.error( error );
	} );
</script>



=============axios 类似于 fetch
是一个基于 Promise 的 HTTP 客户端

npm install axios -g

<!-- 
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
 -->
 <script src="./axios-0.19.0/axios.min.js"></script>
 <script type="text/javascript"> 
 
 var context="/S_ThirdLib";
 
 axios.get(context+'/jsonGet', {
	    params: {
	        id: 12345
	      }
	    })
   .then(function (response) { 
     console.log(response);
   })
   .catch(function (error) { 
     console.log(error);
   })
   .finally(function () { 
   });
   
 axios.post(context+'/jsonPost', {
	    firstName: 'Fred',
	    lastName: 'Flintstone'
	  })
	  .then(function (response) {
	    console.log(response);
	  })
	  .catch(function (error) {
	    console.log(error);
	  });
 
 axios({
	  method: 'post',
	  url: context+'/jsonPost',
	   headers: {"Content-Type": 'application/json'},
	   //options are: 'arraybuffer', 'document', 'json', 'text', 'stream'
	   //browser only: 'blob'
	   responseType: 'json', // default 
	   responseEncoding: 'utf8', // default
	   timeout: 5000, // default is `0` (no timeout)
	//get方式	 
	 params: {
    id: 12345
  },
  	//Post方式
	  data: {
	    firstName: 'Fred',
	    lastName: 'Flintstone'
	  }
	}).then(function (response) {
	    console.log(response); 
	    console.log(response.data);
	    console.log(response.headers);
	    console.log(response.status);//200 
	    console.log(response.statusText);//测试下来是空串，不是OK 
	});
 
 
 

============bignumber
0.1+0.2 返回精度不准的解决方案

https://github.com/MikeMcl/bignumber.js

<script type="text/javascript" src="bignumber.min.js"></script>
<script type="text/javascript">
	//0.3 - 0.1                           // 0.19999999999999998
	x = new BigNumber(0.3)
	x.minus(0.1)                        // "0.2"
	console.log("x="+x);                                   // "0.3"
	console.log("0.1+0.2="+ new BigNumber(0.1).plus(0.2));      
</script>
===========lodash 
有集合的一些操作方法，像filter,map 
也支持node
<script type="text/javascript" src="lodash.min.js"></script>
<script type="text/javascript">
	 var users=[{'user':'lisi','age':36},
		 {'user':'wang','age':25},
		 {'user':'lisi','age':10},];
	 
	var younest= _.chain(users).filter(function(o){return o.age<35})
	 .sortBy('age').map(function(o){
		 return o.user+":"+o.age;
	 }).first().value();
	 console.log(younest);
</script>
===========bluebird
http://bluebirdjs.com/docs/getting-started.html
也支持Node，
Promise增强 Bluebird 支持取消

<script type="text/javascript" src="bluebird-3.7.2/bluebird.min.js"></script>
<style type="text/css">
  #dialog {
    width:      200px;
    margin:     auto;
    padding:    10px;
    border:     thin solid black;
    background: lightgreen;
  }
  .hidden {
    display: none;
  }
</style>
</head>
<body>
	<div id="dialog" class="hidden">
	  <div class="message">foobar</div>
	  <input type="text">
	  <div>
	    <button class="ok">Ok</button>
	    <button class="cancel">Cancel</button>
	  </div>
	</div>
	<p>The current time is <span id="time-stamp"></span>.</p>
	<p>Your name is <span id="prompt"></span>.</p>
	<button id="action">Set Name</button>
</body>
<script type="text/javascript">
document.addEventListener('DOMContentLoaded', function() {
    var time = document.getElementById('time-stamp');
    clockTick();
    setInterval(clockTick, 1000);
    function clockTick() {
      time.innerHTML = new Date().toLocaleTimeString();
    }
  });
var noop = function() {
	  return this;
	};
	function UserCanceledError() {
	  this.name = 'UserCanceledError';
	  this.message = 'User canceled dialog';
	}
	UserCanceledError.prototype = Object.create(Error.prototype);

	function Dialog() {
	  this.setCallbacks(noop, noop);
	}
	Dialog.prototype.setCallbacks = function(okCallback, cancelCallback) {
	  this._okCallback     = okCallback;
	  this._cancelCallback = cancelCallback;
	  return this;
	};
	Dialog.prototype.waitForUser = function() {
	  var _this = this;
	  return new Promise(function(resolve, reject) {
	    _this.setCallbacks(resolve, reject);
	  });
	};
	Dialog.prototype.cancel = function() {
	  this._cancelCallback(new UserCanceledError());
	};
	Dialog.prototype.show = noop;
	Dialog.prototype.hide = noop;

	function PromptDialog() {
	  Dialog.call(this);
	  this.el           = document.getElementById('dialog');
	  this.inputEl      = this.el.querySelector('input');
	  this.messageEl    = this.el.querySelector('.message');
	  this.okButton     = this.el.querySelector('button.ok');
	  this.cancelButton = this.el.querySelector('button.cancel');
	  this.attachDomEvents();
	}
	PromptDialog.prototype = Object.create(Dialog.prototype);
	PromptDialog.prototype.attachDomEvents = function() {
	  var _this = this;
	  this.okButton.addEventListener('click', function() {
	    _this._okCallback(_this.inputEl.value);
	  });
	  this.cancelButton.addEventListener('click', function() {
	    _this.cancel();
	  });
	  
	};
	PromptDialog.prototype.show = function(message) {
	  this.messageEl.innerHTML = '' + message;
	  this.el.className = '';
	  return this;
	};
	PromptDialog.prototype.hide = function() {
	  this.el.className = 'hidden';
	  return this;
	};

	document.addEventListener('DOMContentLoaded', function() {
	  var button = document.getElementById('action');
	  var output = document.getElementById('prompt');
	  var prompt = new PromptDialog();
	  button.addEventListener('click', function() {
	    setTimeout(function() {
	      prompt.cancel();//这个是扩展的功能
	    }, 5000);
	    
	    prompt.show('What is your name?')
	      .waitForUser()
	      .then(function(name) {
	        output.innerHTML = '' + name;
	      })
	      .catch(UserCanceledError, function() {
	        output.innerHTML = '¯\\_(ツ)_/¯';
	      })
	      .catch(function(e) {
	        console.log('Unknown error', e);
	      })
	      .finally(function() {
	        prompt.hide();
	      });
	  });
	});
</script>
	 


------------YUI Compressor 压缩JS
============video.js

==============国产 ZCell   免费和收费 页面中excel

==============国产 plusproject 收费 普加 甘特图  
	基于自己(普加)的 jQuery MiniUI 开发
	使用 phantomjs 打印生成页面截图

==============highcharts Gantt 甘特图  










