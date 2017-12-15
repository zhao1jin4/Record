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


	
==============windows 打印功能 

会提示下载 CLodopPrint_Setup_for_Win32NT


<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
</object>

<div id="mycode1">
	这是打印内容 
</div>
<div id='myvehicleno'>车牌号</div>

 <button onclick="prn_Preview()">打印 </button>
				
function prn_Preview(){
	LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP.webskt && LODOP.webskt.readyState==1 && LODOP.VERSION){
		var top = 50;
		$($("div[id*='mycode']")).each(function(i){
			i = i + 1;
			LODOP.ADD_PRINT_TEXT(top-30,320,520,160,"远成集团车标码");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.ADD_PRINT_BARCODE(top,110,520,160,"128A",$.trim($(this).text()));
			LODOP.ADD_PRINT_TEXT(top+170,310,520,160,$.trim($("#myvehicleno").text()));
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			top = top + 110 + 150;
			if(i % 4 == 0){
				LODOP.NewPageA();
				top = 50;
			}
		});
		LODOP.PREVIEW();
	} else {
		alert("打印插件没准备好，请刷新页面后重试！");
	}
}




//====判断是否需要安装CLodop云打印服务器:====
function needCLodop() {
    try {
        var ua = navigator.userAgent;
        if (ua.match(/Windows\sPhone/i) != null) return true;
        if (ua.match(/iPhone|iPod/i) != null) return true;
        if (ua.match(/Android/i) != null) return true;
        if (ua.match(/Edge\D?\d+/i) != null) return true;

        var verTrident = ua.match(/Trident\D?\d+/i);
        var verIE = ua.match(/MSIE\D?\d+/i);
        var verOPR = ua.match(/OPR\D?\d+/i);
        var verFF = ua.match(/Firefox\D?\d+/i);
        var x64 = ua.match(/x64/i);
        if ((verTrident == null) && (verIE == null) && (x64 !== null)) return true;
        else if (verFF !== null) {
            verFF = verFF[0].match(/\d+/);
            if ((verFF[0] >= 42) || (x64 !== null)) return true;
        } else if (verOPR !== null) {
            verOPR = verOPR[0].match(/\d+/);
            if (verOPR[0] >= 32) return true;
        } else if ((verTrident == null) && (verIE == null)) {
            var verChrome = ua.match(/Chrome\D?\d+/i);
            if (verChrome !== null) {
                verChrome = verChrome[0].match(/\d+/);
                if (verChrome[0] >= 42) return true;
            };
        };
        return false;
    } catch(err) {
        return true;
    };
};

//====获取LODOP对象的主过程：====
function getLodop(oOBJECT, oEMBED) {
    var strHtmInstall = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtmUpdate = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtm64_Install = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtm64_Update = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtmFireFox = "<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    var strHtmChrome = "<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";
    var strCLodopInstall = "<br><font color='#FF00FF'>CLodop云打印服务(localhost本地)未安装启动!点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>执行安装</a>,安装后请刷新页面。</font>";
    var strCLodopUpdate = "<br><font color='#FF00FF'>CLodop云打印服务需升级!点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";
    var LODOP;
    try {
        var isIE = (navigator.userAgent.indexOf('MSIE') >= 0) || (navigator.userAgent.indexOf('Trident') >= 0);
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch(err) {};
            if (!LODOP && document.readyState !== "complete") {
                alert("C-Lodop没准备好，请稍后再试！");
                return;
            };
            if (!LODOP) {
                if (isIE) {
                	if(confirm("CLodop云打印服务(localhost本地)未安装启动!点击确定进行安装,安装后请刷新页面。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else {
                	if(confirm("CLodop云打印服务(localhost本地)未安装启动!点击确定进行安装,安装后请刷新页面。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                return;
            } else {

                if (CLODOP.CVERSION < "2.1.0.2") {
                    if (isIE) {
                    	if(confirm("CLodop云打印服务需升级!点击确定下载控件进行升级,升级后请刷新页面。")){
                    		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                    	} else {
                    		alert("下载失败!");
                    	}
                    }
                    else { 
	                	if(confirm("CLodop云打印服务需升级!点击确定下载控件进行升级,升级后请刷新页面。")){
	                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
	                	} else {
	                		alert("下载失败!");
	                	}
                    }
                };
                if (oEMBED && oEMBED.parentNode) oEMBED.parentNode.removeChild(oEMBED);
                if (oOBJECT && oOBJECT.parentNode) oOBJECT.parentNode.removeChild(oOBJECT);
            };
        } else {
            var is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
            //=====如果页面有Lodop就直接使用，没有则新建:==========
            if (oOBJECT != undefined || oEMBED != undefined) {
                if (isIE) LODOP = oOBJECT;
                else LODOP = oEMBED;
            } else if (CreatedOKLodop7766 == null) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE) LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodop7766 = LODOP;
            } else LODOP = CreatedOKLodop7766;
            //=====Lodop插件未安装时提示下载地址:==========
            if ((LODOP == null) || (typeof(LODOP.VERSION) == "undefined")) {
                if (navigator.userAgent.indexOf('Chrome') >= 0) {
                	if(confirm("如果此前正常，仅因浏览器升级或重安装而出问题，需卸载后重新进行安装。注卸载需重新启动")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                if (navigator.userAgent.indexOf('Firefox') >= 0) {
                	if(confirm("注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸载。在进行重新安装")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                if (is64IE) {
                	if(confirm("打印控件未安装!点击确定进行下载,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win64NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else if (isIE) {
                	if(confirm("打印控件未安装!点击确定下载控件,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else {
                	if(confirm("打印控件未安装!点击确定下载控件,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                return LODOP;
            };
        };
        if (LODOP.VERSION < "6.2.1.7") {
            if (needCLodop()) document.documentElement.innerHTML = strCLodopUpdate + document.documentElement.innerHTML;
            else if (is64IE) {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win64NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            else if (isIE) {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            else {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            return LODOP;
        };
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):===
        //===========================================================
        return LODOP;
    } catch(err) {
        alert("getLodop出错:" + err);
    };
};




















