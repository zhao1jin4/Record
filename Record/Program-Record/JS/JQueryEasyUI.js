EasyUI 主题包 Insdep Theme
https://www.insdep.com/document/page/easyui/index/init

Insdep UI-2.x For EasyUI 是基于EasyUI 1.5.x 的一款免费的UI工具库,EasyUI组件美化
https://www.insdep.com  国产的
=================================================jQuery插件 easyUI-1.3.5
1.5 有免费版本带源码

GPL License,开源,布局,树,表格,表单(select可输入选择)


<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/icon.css" />
<script type="text/javascript" src="${webRoot}/js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="${webRoot}/js/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="${webRoot}/js/easyui-lang-zh_CN.js"></script>  <!-- 导入后验证等的信息就为中文了 -->
 
//---表单  
$.extend($.fn.validatebox.defaults.rules, 
	{
		equals://自定义方法
		{
			validator: function(value,param)
			{
				return value == $(param[0]).val();
			},
			message: '两次输入的密码不相同'
		}
	});
$(function()
{
	 //JS动态增加的表单做验证
   $("<input type='text' name='other'/>").validatebox({
	required: true,
	validType: 'email'
	}).appendTo($('form'));
});

function submitForm(){
	$('form').form('submit');
}

<div class="icon-tip" style="width:20px;height:20px"></div>灯图标
<div class="easyui-panel" title="New Topic">
  <form id="ff"> <!-- 有form是为验证 -->
	password: <input id="pwd" name="pwd" type="password" class="easyui-validatebox" data-options="required:true">
	repassword:<input id="rpwd" name="rpwd" type="password" class="easyui-validatebox"  required="required" validType="equals['#pwd']">
	email:<input class="easyui-validatebox" type="text" name="email" data-options="required:true,validType:'email'">
	language:<select class="easyui-combobox" name="language"> <!-- 可以输入查询-->
			</select>
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">Submit</a>		
  </form>
 </div>
//---日期
<input class="easyui-datetimebox" required style="width:150px">
//---表格
var mytoolbar = [{
		text:'增加',
		iconCls:'icon-add',
		handler:myInsert
	},
	'-'//分隔线
	,{
		text:'刷新',
		iconCls:'icon-reload',
		handler:function(){alert('刷新')}
	}];

var myLanguages=
[{
	 langValue:"C",
	 langLabel:"C 语言"
 },
 {
	 langValue:"Java",
	 langLabel:"Java 语言"
 },
 {
	 langValue:"PLSQL",
	 langLabel:"PLSQL 语言"
 }];
 
$(function(){
	$('#dg').datagrid({
		queryParams: {
			my_custome_param: 'my-easyUI-value'
		}
	} );
	
	for(index in myLanguages)
	{
		var opt=$("<option>").val( myLanguages[index].langValue ).html( myLanguages[index].langLabel);
		$("#commonlang").append(opt);
	}
	//easyUI 的 <select>是不同的
	$("#lang").combobox({
			data:myLanguages,//url:xxx
			valueField:'langValue',
			textField:'langLabel'
		});
});

//表格级的,刷新
function myBeforeEdit(index,row)
{
	row.editing = true;//自已新定义的属性
	updateActions(index);
}
function myAfterEdit(index,row)
{
	$.ajax
	({
		url:'../easyUI/updateData',
		type:"POST",
		dataType:"json",
		data:{
				command:(row.id)?"update":"save",
				id:row.id,
				username:row.username,
				language:row.language,
			}, 
		success:function(response)
		{
			if(response.statusCode==200)
			{
				if(! row.id) //save
				{
					row.id=response.additionObject;//newId
					updateActions(index);
				}
				$.messager.alert('提示','操作成功','info');
				/* jQueryUI
				$("<div>操作成功</div>").dialog({
					 modal: true,
					 buttons: {
						 Ok: function() {
							$( this ).dialog( "close" );
						 }
					 }
				 });
				*/
			}
		}
	});
	row.editing = false;
	updateActions(index);
}
function myCancelEdit(index,row)
{
	row.editing = false;
	updateActions(index);
}
function updateActions(index)
{
	$('#dg').datagrid('updateRow',{
		index: index,
		row:{}
	});
}
//--行级的
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}
function myEditrow(target){
	$('#dg').datagrid('beginEdit', getRowIndex(target));//调用onBeforeEdit:myBeforeEdit
}
function mySaveUpdateRow(target){
	$('#dg').datagrid('endEdit', getRowIndex(target));//调用onAfterEdit:myAfterEdit
}
function myCancelUpdateRow(target){
	$('#dg').datagrid('cancelEdit', getRowIndex(target));
}
function myInsert()
{
	var row = $('#dg').datagrid('getSelected');
	if (row){
		var index = $('#dg').datagrid('getRowIndex', row);//返回选中行的数据对象
	} else {
		index = 0;
	}
	$('#dg').datagrid('insertRow', {
		index: index,
		row:{
			isMan:"true",
			language:"Java",
			birthday:"2014-01-01"
		}
	});
	$('#dg').datagrid('selectRow',index);//选中指定行
	$('#dg').datagrid('beginEdit',index);
}
function myDeleterow(target){
	$.messager.confirm('确认','你真的要删除吗?',function(r)
	{
		if (r)
		{
			$.ajax
			({
				url:'../easyUI/updateData',
				type:"POST",
				dataType:"json",
				data:
				{
					id: $('#dg').datagrid('getRows')[ getRowIndex(target)].id , //getRows返回的是所有数据
					command:"delete",
				},
				success:function(response)
				{
					if(response.statusCode==200)
						$.messager.alert('提示','操作成功','info');
				}
			});
			$('#dg').datagrid('deleteRow', getRowIndex(target));//放后面
		}
	});
}
//--格式化显示
function myGenderFormatter(value,row,index)
{
	if (row.isMan){
		return "男";
	} else {
		return "女";
	}
}
function myActionFormatter(value,row,index)
{
	if (row.editing){//加  class="easyui-linkbutton"没效果 ???
		var s = '<a href="#" onclick="mySaveUpdateRow(this)">Save</a> ';
		var c = '<a href="#" onclick="myCancelUpdateRow(this)">Cancel</a>';
		return s+c;
	} else {
		var e = '<a href="#" onclick="myEditrow(this)">Edit</a> ';
		var d = '<a href="#" onclick="myDeleterow(this)">Delete</a>';
		return e+d;
	}
}

function mySearch()
{
	 $('#dg').datagrid('load',{
		date_from: $('#date_from').datebox('getValue'),//日期类型得值 
		date_to: $('#date_to').datebox('getValue'),
		 lang:$('#lang').val(),
		 user:$('#user').val()
		 });
	 
}
function myBarRemoveByCheckBox() //table 的 singleSelect:true
{
	var checkedItems = $('#dg').datagrid('getChecked');//得到所有的checkbox选择的行(是修改后的数据)
	var deleteIds = [];
	$.each(checkedItems, function(index, item){
		deleteIds.push(item.id);
	});               
	console.log(deleteIds.join(","));
}
所有的iconCls 的取值在icon.css中	
<div id="tb" style="padding:5px;height:auto">
	<div style="margin-bottom:5px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true"></a> <!-- 加 plain="true" 按钮没有立体感 -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"></a>
	</div>
	<div><!-- 因使用JSON不能用form -->
		User:	<input id="user" type="text">
		Date From:	<input id="date_from" type="text" style="width:90px" class="easyui-datebox">
		To: 		<input  id="date_to"  type="text" style="width:90px" class="easyui-datebox">
		Language: 
		<select id="lang" class="easyui-combobox" panelHeight="auto" style="width:100px">
			<option value="java">Java</option>
			<option value="c">C</option>
			<option value="plsql">PLSQL</option>
		</select>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="mySearch()" >搜索</a>
	</div>
</div>
 
<table id="dg" title="表格数据" style="width:1200px;height:300px" 
	data-options="
			rownumbers:true,
			singleSelect:true,
			autoRowHeight:false,
			collapsible:true,
			pagination:true,
			pageSize:20,
			pageList:[10,20,30,50],
			url:'../easyUI/queryJsonData',
			method:'get',
			onBeforeEdit:myBeforeEdit,
			onAfterEdit:myAfterEdit,
			onCancelEdit:myCancelEdit,
			toolbar:'#tb'"> <!-- toolbar:mytoolbar 读全局变量  mytoolbar ,toolbar:'#tb' 引用 DIV   -->
	<thead>
		<tr>
			<th data-options="checkbox:true"></th> <!-- 选择的复选框 -->
			<th field="id" width="80">ID</th> <!-- field的值是JSON对象的属性名 -->
			<th field="username" width="100" data-options="editor:{type:'validatebox',options:{required:true}}">用户名</th>
			<th field="language" width="100" data-options="editor:{
						type:'combobox',
						options:{
							valueField:'langValue',
							textField:'langLabel',
							data:myLanguages,
							required:true
						}
					}">用语言</th>
			<th field="salary" width="80"  data-options="editor:{type:'numberbox',options:{precision:1,required:true}}">工资</th> <!--editor:'numberbox'  -->
			<th field="isMan" width="80" data-options="formatter:myGenderFormatter,editor:{type:'checkbox',options:{on:'true',off:'false'}}">是否为男</th>
			<th field="birthday" width="90"  data-options="editor:{type:'datebox',options:{required:true}}">生日</th>
			<th field="comment" width="120"  data-options="editor:'textarea'">comment</th>
			<th field="action"  width="120" data-options="formatter:myActionFormatter" >操作</th>
		</tr>
	</thead>
</table>
Selection Mode:
<select onchange="$('#dg').datagrid({singleSelect:(this.value==0)})">
	<option value="0">Single Row</option>
	<option value="1">Multiple Rows</option>
</select><br/>
SelectOnCheck: <input type="checkbox" checked onchange="$('#dg').datagrid({selectOnCheck:$(this).is(':checked')})"><br/>
CheckOnSelect: <input type="checkbox" checked onchange="$('#dg').datagrid({checkOnSelect:$(this).is(':checked')})">

--服务代码
String my_custome_param=request.getParameter("my_custome_param");

int pageNO=Integer.parseInt(request.getParameter("page"));
int pageSize=Integer.parseInt(request.getParameter("rows"));
long start=(pageNO-1)*pageSize+1;//pageNO*pageSize-pageSize+1
long end=pageNO*pageSize;
//DataGrid返回JSON格式
{"total":"28","rows":[  //固定
	{"id":101}
]}
--
JSONObject obj = new JSONObject();
obj.put("statusCode", 200);
obj.put("statusMessage","成功");

if("save".equals(command))
{
	long newId=500+(long)(400*Math.random());//500-900
	obj.put("additionObject",newId);
}
response.getWriter().write(obj.toString());
=================================================eastUI Extension
-----detailView
<script type="text/javascript" src="datagrid-detailview.js"></script>

$(function(){
$('#dg').datagrid({
	view: detailview,
	detailFormatter:function(index,row){
		return '<div class="ddv"></div>';
	},
	onExpandRow: function(index,row){
		var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');//getRowDetail扩展方法
		ddv.panel({
			border:false,
			cache:true,
			href:'dataGrid_rowEditDetail.jsp?index='+index,
			onLoad:function(){
				$('#dg').datagrid('fixDetailRowHeight',index);//fixDetailRowHeight扩展方法
				$('#dg').datagrid('selectRow',index);
				$('#dg').datagrid('getRowDetail',index).find('form').form('load',row);
			}
		});
		$('#dg').datagrid('fixDetailRowHeight',index);
	}
});
});
		
		
//form中的保存时调用的方法
function saveItem(index){
	var row = $('#dg').datagrid('getRows')[index];
	var url = row.isNewRecord ? webRoot+'/easyUI/updateData?command=extensionSaveNew' :  webRoot+'/easyUI/updateData?command=extensionSaveEdit&id='+row.id;
	$('#dg').datagrid('getRowDetail',index).find('form').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(data){
			data = eval('('+data+')');
			data.isNewRecord = false;
			$('#dg').datagrid('collapseRow',index);
			$('#dg').datagrid('updateRow',{
				index: index,
				row: data
			});
		}
	});
}