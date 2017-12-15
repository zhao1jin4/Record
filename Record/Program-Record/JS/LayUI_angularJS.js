
=============LayUI  2.1.5 免费  

--Java
String id=request.getParameter("id");
System.out.println("收参数请求 id="+id);
String reqPageNO=request.getParameter("page");
String reqPageSize=request.getParameter("limit");

response.setHeader("Content-Type","application/json; charset=utf-8");
JsonStructure root =bf.createObjectBuilder()
				  .add("code",0)
				  .add("msg","")
				  .add("count", totalCount)
	            .add("data",array)
	            .build();
		 
---HTML JS
 
<div class="demoTable">
  搜索ID：
  <div class="layui-inline">
    <input class="layui-input" name="id" id="demoReload" autocomplete="off">
  </div>
  <button class="layui-btn" data-type="reload">搜索</button>
</div>
 
<table class="layui-hide" id="LAY_table_user" lay-filter="user"></table> 
     
	 
 
 <script type="text/html" id="langTransfer">
    {{#  	if(d.language === 'C'){ 			}}
   				 <span>C/C++ 语言</span>
    {{#  	} else {							}}
   				  <a style="color: blue;" lay-event="edit">编辑</a>   
    {{#  	} 									}}
</script>   
layui.use('table', function(){
  var table = layui.table;
  
  //方法级渲染
  table.render({
    elem: '#LAY_table_user'
   	,method : 'get'
    ,url: '${pageContext.request.contextPath}/layUI/queryJsonData'
	//,data: [{}]
    ,cols: [[
      {checkbox: true, fixed: true}
      ,{field:'id', title: 'ID', width:80, sort: true, fixed: true}
      ,{field:'username', title: '用户名', width:80}
      ,{field:'language', title: '语言', width:80, sort: true,templet: '#langTransfer'}
      ,{field:'salary', title: '工资', width:80}
      ,{field:'isMan', title: '男人吗', sort: true, width:80}
      ,{field:'birthday', title: '生日', sort: true, width:80}
      ,{field:'comment', title: '说明 ', width:80} 
    ]]
    ,id: 'testReload'
    ,page: true
    ,height: 315
    , done:  function(res, curr, count){
        console.log('加载完成 隐藏加载中');
    }

  });
  
  var $ = layui.$, active = {
    reload: function(){
      var demoReload = $('#demoReload');
      
      table.reload('testReload', {
        where: {
        	  id: demoReload.val()  //服务端直接 request.getParameter("id");
        }
      });
    }
  };
  
  $('.demoTable .layui-btn').on('click', function(){
    var type = $(this).data('type'); //type 是 reload
    active[type] ? active[type].call(this) : '';
  });
});


----事件 

<table class="layui-table" lay-data="{height: 313, url:'${pageContext.request.contextPath}/layUI/queryJsonData'}" lay-filter="demoEvent">
  <thead>
    <tr>
      <th lay-data="{field:'id', width:80}">ID</th>
      <th lay-data="{field:'username', width:80}">用户名</th>
      <th lay-data="{field:'isMan', width:80}">性别</th>
      <th lay-data="{field:'city', width:80}">城市</th>
      <th lay-data="{field:'comment', width:177 }">说明</th>
      <th lay-data="{field:'salary', width:80 , event: 'setSalary', style:'cursor: pointer;'}">工资</th>
	   <th lay-data="{field:'language', width:80,templet: '#langTransfer'}">语言</th>
    </tr>
  </thead>
</table>  


 
layui.use('table', function(){
  var table = layui.table; 
  //监听单元格事件
  table.on('tool(demoEvent)', function(obj){
    var data = obj.data;
    if(obj.event === 'setSalary')
	{
      layer.prompt({
        formType: 2
        ,title: '修改 ID 为 ['+ data.id +'] 的工资'
        ,value: data.salary
      }, function(value, index){
        layer.close(index);
        
        //这里一般是发送修改的Ajax请求
        
        //同步更新表格和缓存对应的值
        obj.update({
        	salary: value
        });
      });
    }else if (obj.event === 'edit')
   	{
   		alert('收到edit事件');
   	}
  });
});

----tab切换
<input type="button" value="点选切换2" onclick="switchTab()" />

<div class="layui-tab layui-tab-brief" lay-filter="tab" >
    <ul class="layui-tab-title">
        <li lay-id="11" class="layui-this">标签1</li>
        <li id="layui1" lay-id="22" >标签2</li>
        <li id="layui2" lay-id="33" >标签3</li>
    </ul>
    <div class="layui-tab-content" >
        <div class="layui-tab-item layui-show">
         	11 
        <div class="layui-tab-item" >
			22
        </div>
        <div class="layui-tab-item" >
			33
        </div>
    </div>
</div>
 
 
 
layui.use('element', function(){
    var $ = layui.jquery ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块

});
function  switchTab()
{
	layui.element.tabChange('tab', '22'); //lay-id="22"
}






=============AnjularJS  1.6.4 有 免费 和 收费 版本  
有4.2.4 和 2.0 版本,但像是另一种编程语言 , TypeScript


