YUI (BSD license),不易带来许可的麻烦

==========================EXTJS 4.2.1========================    EXTJS 5 收费
GPL  License

Apatana 3.1
--eclipse插件Spket
preferences->Spket->JavaScript Prfiles->new...->输入 EXTJS4.1->选择EXTJS,->Add File 按钮->
选择 extjs-4.1.0\build\sdk.jsb3 文件->会列表支持的
->default按钮,对选择的EXTJS设置为默认,上面的链接"Configure project specific Settings..."->选择作用的项目

Alt+/ 提示好没有效果,按ctrl+单击Ext.onReady的函数后就有效果了
对.js文件open with ->spket javascript editor-> 可以按ctrl拖动放大缩小字体


<link rel="stylesheet" type="text/css" href="extjs4.1/resources/css/ext-all.css" />
<script type="text/javascript" src="extjs4.1/js/ext-all.js"></script><!--不能使用<script/>的简单形式-->
<script type="text/javascript" src="extjs4.1/js/ext-lang-zh_CN.js"></script> <!--表格的排序文字显示中／英文	-->

<script type="text/javascript">
	/*
		resources下的css目录和themes目录都不能修改名字,层级也能修改
		resources\css\ext-all.css  目前只要一个
		resources\images           目前只要一个
	*/
		Ext.onReady(function ()
		{
			//Ext.MessageBox.alert("标题","提示")//OK
			Ext.Msg.alert("标题","提示")//OK
		});
</script>


//有Ajax操作应和服务器在一个项目
Ext.onReady(function ()
{
	Ext.Ajax.timeout = 60000; // 60 seconds
		
	var root="/J_AjaxServer";
	//var root="http://127.0.0.1:8080/J_AjaxServer";//这样是不行的
	Ext.Ajax.request
	({
		method : 'POST',
	    url: root+'/jsp/extjs/hello.jsp?username=张三', //?username=张三    的方式对POST也有效,会覆盖params中相同的key
	    params: 
	    {
	     	username: "张三1",
	        username1: "李四"
	    },
	    success: function(response)
	    {
	        var text = response.responseText;
	        alert("响应结果:"+text);
	    },
	    failure:function(e)
	    {
      		alert("错误文本:"+e.statusText);
     	}   
	});
});	




 


Combox 的  triggerAction: 'all' 是输入一个字符,再点箭头时显示全部,而query只显示匹配的


Ext.onReady(回调函数); //加载执行

　new Ext.grid.ColumnModel([
	render:   //会调另一个function
	editor:new Ext.form.DateField({
			format : 'y/m/d'
			minValue '1970/01/01'
			disableDays:[0,6] //周六和周日无效
		})




ColumnModel　　的　defaultSortable=true 表格是否可以排序　



var User=Ext.data.Record.create([
			{name : 'id',type : 'int'},
			{name : 'sex',type : 'bool'},
			{name : 'email',type : 'string'},
			{name : 'birthday',type : 'date',mapping:'birth' dateFormat :'Y/m/d'}, //
			])
mapping 对就XML中的标签名

Ext.data.Store({
	url:'xxx.xml',
	reader: new Ext.data.XmlReader({recod: 'user'},User)//<user> 标签,存入上定义的User类型
})


new Ext.grid.EditorGridPanel({
store: myStore,
cm:mycm,
renderTo:mydiv-id,
title:'',
clicksToEdit:1 //点１次就可以改
selModel:new Ext.grid.RowSelectionModel({singleSelect:false}) //可以多选
//顶部工具栏
	tbar:[
	{text :'mybuttion',
	iconCls :'mycss'  //图标样式
	handler: function(){},回调
	},
	{}
	]
})


grid.stopEditing();//不可编辑
store.insert()
store.getSelectionModel().getSelected();//返回选择的
grid.startEditing(第几行,几列可以改)//0开始的

Ext.Msg.wat("提示","","")


// 单元格编辑后事件处理
grid.on("afteredit", afterEdit, grid);afteredit  事件调用afteredit 函数,传grid,有属性record被编辑的记录
																				.field  .value


grid.record.set(grid.field, grid.originalValue);
grid.record.get("name")

 Ext.grid.ColumnModel中  renderer: 'usMoney',
Ext.util.Format.usMoney


 
 
 Ext.util.Format.dateRenderer('m/d/Y')

 


Request Header传递的Content-Type值是application/json;utf-8。而这也是EXT调用asp.net ajax WebService的关键
如果使用POST请求，需要将用户传递参数当成对象序列化成JSON串



function CallHelloWorld() {
    Ext.Ajax.request({
        url: 'TestWebService.asmx/HelloWorld', // Webservice的地址以及方法名
        jsonData: { firstName: 'AAA', lastName: 'BBB' }, // json 形式的参数
        method: 'POST', // poste 方式传递
        success: onSuccess,
        failure: onFailure
    });
}
 
==========================sencha-touch-2.3.1-gpl======================== 
示例中carousel ,是带小点的翻页.nestedlist

