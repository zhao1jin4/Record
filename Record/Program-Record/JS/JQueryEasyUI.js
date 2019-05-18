 http://www.jeasyui.net/ EasyUI 中文网
 有 themebuilder
 有 EasyUI for Angular
    EasyUI for Vue
Demo 有Mobile的
=================================================jQuery插件 easyUI-1.5.5.6 free
1.5 有免费版本带源码

有为AugularJS 和 Vue

GPL License,开源,布局,树,表格,表单(select可输入选择)


<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="${webRoot}/css/eayui-theme/icon.css" />
<script type="text/javascript" src="${webRoot}/js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="${webRoot}/js/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="${webRoot}/js/easyui-lang-zh_CN.js"></script>  <!-- 导入后验证等的信息就为中文了 -->
 
 

$.parser.parse(); // 解析整个页面
$.parser.parse('#cc'); // 解析某个具体节点




//---------表单  
$.extend($.fn.validatebox.defaults.rules, 
	{
		equals://自定义方法
		{
			validator: function(value,param)
			{
				return value == $(param[0]).val();
			},
			message: '两次输入的密码不相同'
		},
		maxLength: 
		{
			validator: function(value, param)
			{
				return value.length < param[0];
			},
			message: '最长 {0}个字符.'
		},
		mustNumber: 
		{
			validator: function(value, param)
			{
				if(value == "")
					return true;
				return /^([0-9])*$/.test(value);  
			},
			message: '必须是数字'
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
	$('form').form('submit' ,
		{   onSubmit:function()
			{
				return $(this).form('enableValidation').form('validate');
			}
		});
}

<div class="icon-tip" style="width:20px;height:20px"></div>灯图标
<div class="easyui-panel" title="New Topic">
  <form > <!-- 有form是为验证 -->
	username: <input class="easyui-validatebox" type="text" name="name" data-options="required:true,validType:'maxLength[30]'"  />
	password: <input id="pwd" name="pwd" type="password" class="easyui-validatebox" data-options="required:true"/>
	repassword:<input id="rpwd" name="rpwd" type="password" class="easyui-validatebox"  required="required" validType="equals['#pwd']"/>
	email:<input class="easyui-validatebox" type="text" name="email" data-options="required:true,validType:'email'" size="20" maxlength="30"/>
	age: <input class="easyui-validatebox" type="text" name="age" data-options="required:true,validType:'mustNumber'" size="20" maxlength="30"/>
	
	language:<select class="easyui-combobox" name="language"> <!-- 可以输入查询-->
			</select>
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">Submit</a>		
  </form>
 </div>
 
//---------日期
<input class="easyui-datetimebox" required style="width:150px">



$.fn.datebox.defaults.formatter = function(date){  //全部是今天的值????
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	m=(m<10)?'0'+m:m;
	//return m+'/'+d+'/'+y;
	return y+'='+m+'='+d;
}
 
var buttons = $.extend([], $.fn.datebox.defaults.buttons);
buttons.splice(1, 0, {
	text: '清除',
	handler: function(target){
	  $(target).combo('setText',''); 
	}
});
<input class="easyui-datebox" label="Date With 3 Buttons:" labelPosition="top" data-options="buttons:buttons" style="width:100%;">
  	

//--------- alert
	$.messager.alert({
		title: 'My Title',
		msg: 'Here is a message!',
		icon:'info', // error,question,info,warning
		fn: function(){
			console.log('after click button');
		}	
	});
	
	//中心显示,会自动消失
	 $.messager.show({
			title:'My Title',
			msg:'Message will be closed after 2 seconds.',
			timeout:1000,
			showType:'fade',
			style:{
				right:'', 
				bottom:''
			}
		});
//--------checkbox 
//做成 选中为true,不选为false ,SwitchButton不能 datagrid 的editor
function checkOnOff()
{
	if($("#myCheckBox").checkbox('options').checked)
	{
		$('#myCheckBox').checkbox('uncheck');
		$("#myCheckBox").checkbox('setValue',false);
	}
	else
	{
		$('#myCheckBox').checkbox('check');
		$("#myCheckBox").checkbox('setValue',true);
	}
	console.log($("#myCheckBox").val()); 
}

<input id="myCheckBox" class="easyui-checkbox" name="fruit" value="Apple" label="Apple:"> 
<button type="button" onclick="checkOnOff()">check开关</button> 

-----switchbutton 

function switchOnOff()
{
	if( $('#mySwitch').switchbutton('options').checked)
	{
		$('#mySwitch').switchbutton({
				// checked: false ,
			   value:false  //这样才行
			 });
	   $('#mySwitch').switchbutton('uncheck');//正常，也用修改checked属性方式
	   //$('#mySwitch').switchbutton('setValue',false);//setValue没用 ???
	}else
	{
		$('#mySwitch').switchbutton({
				// checked: true,
				 value:true
			 });
		$('#mySwitch').switchbutton('check');
		//$('#mySwitch').switchbutton('setValue',true); 
	} 
	console.log($('#mySwitch').switchbutton().val());
}


<input id="mySwitch" class="easyui-switchbutton" value="yes" data-options="onText:'开',offText:'关'">
<button type="button" onclick="switchOnOff()">switch开关</button>
//---------dialog	
//   要初始化 $(function (){ 才能正常显示  
	 $('#dlg2').dialog({
	 	    title: 'My Dialog',
	 	    width: 400,
	 	    height: 200,
	 	    modal: true,
	 	    toolbar:[{
	 			text:'Edit',
	 			iconCls:'icon-edit',
	 			handler:function(){alert('edit')}
	 		},{
	 			text:'Help',
	 			iconCls:'icon-help',
	 			handler:function(){alert('help')}
	 		}]
	  	}); 
	 $('#dlg2').dialog('close'); 
	 
//--------- tooltip

 <a href="#" 
       title="<div style='width:80px;background-color:orange'>This is the tooltip message. This is the tooltip message. This is the tooltip message.</div>" 
       class="easyui-tooltip" 
        >Hover me</a>
		
	//----------filebox
  只能选图片： <input id="fb" class="easyui-filebox" style="width:100%" data-options="prompt:'Choose a file...'"> <br/>
  只能选excel： <input id="excelFileBox" class="easyui-filebox" style="width:100%" data-options="prompt:'Choose a file...'"><br/>
  <input id="fb" class="easyui-linkbutton" onclick="validateFileBox()" value="检查"/>
  <script type="text/javascript">
  $(function (){
    $('#fb').filebox(
      {buttonAlign:'right',
      buttonText:"浏览",
      accept:"image/*"
      
     });
    $('#excelFileBox').filebox(
      {buttonAlign:'right',
      buttonText:"浏览",
      accept:"application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
      
     });
  });
     function validateFileBox()
     {
       if($('#fb').filebox('getText')=='')
        alert("请选择文件 ");
     }
  </script> 
//---------表格
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
	updateActions(index);//进入了edit模式，调用 updateRow 不会刷新format
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
				}
				$.messager.alert('提示','操作成功','info');//可用的有 error,question,info,warning.
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
 updateActions(index);//无效
	editIconVisible(row.id,true);  
}
function myCancelEdit(index,row)
{
	row.editing = false;
	updateActions(index);
}　
//--行级的
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}
function myEditrow(target){
	　var index=getRowIndex(target);
			$('#dg').datagrid('selectRow',index);
			var firstSel=$('#dg').datagrid('getSelected');
			editIconVisible(firstSel.id,false);
			$('#dg').datagrid('beginEdit', index);//调用 onBeforeEdit:myBeforeEdit
			console.log('这是调用myBeforeEdit后执行的') ; 
}
function mySaveUpdateRow(target){
	$('#dg').datagrid('endEdit', getRowIndex(target));//调用onAfterEdit:myAfterEdit
 //editIconVisible( ); //myAfterEdit中有 
} 
function myCancelUpdateRow(target){//调用   onCancelEdit: myCancelEdit
	$('#dg').datagrid('cancelEdit', getRowIndex(target));
 //editIconVisible( ); //myCancelEdit 中有 
}
function myInsert()
{
	var row = $('#dg').datagrid('getSelected');
	if (row){
		var index = $('#dg').datagrid('getRowIndex', row);//返回选中行的数据对象
	} else {
		index = 0;
	}
	$('#dg').datagrid('insertRow', {//也有appendRow
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
 // 加format中加  class="easyui-linkbutton"没效果 ???
 /*		
 //进入编辑模式后，再调用 updateRow 方法 调用后也不会触发format ??
 if (row.editing){
  var s = '<a href="#"  onclick="mySaveUpdateRow(this)">Save</a> ';
  var c = '<a href="#" onclick="myCancelUpdateRow(this)">Cancel</a>';
  return s+c;
 } else { 
  var e = '<a href="#"  class="easyui-linkbutton" onclick="myEditrow(this)">Edit</a> ';
  var d = '<a href="#"  class="easyui-linkbutton" onclick="myDeleterow(this)">Delete</a>';
  return e+d;
 }
 */ 
 var editBtn   = '<a href="#" id="editBtn'+row.id+'" class="easyui-linkbutton" onclick="myEditrow(this)">Edit</a> &nbsp;';//不可传row除非bind
 var saveBtm   = '<a href="#" id="saveBtn'+row.id+'" class="easyui-linkbutton" onclick="mySaveUpdateRow(this)" style="display:none" >Save</a>&nbsp; ';
 var cancelBtn = '<a href="#" id="cancelBtn'+row.id+'" class="easyui-linkbutton" onclick="myCancelUpdateRow(this)" style="display:none" >Cancel</a> &nbsp;';
 var deleteBtn = '<a href="#" id="deleteBtn'+row.id+'" class="easyui-linkbutton" onclick="myDeleterow(this)">Delete</a>';
 return editBtn+saveBtm+cancelBtn+deleteBtn; 
}		
function editIconVisible(id,visiable)
{
 if(visiable)
 {
  $("#editBtn"+id).show();
  $("#saveBtn"+id).hide();
  $("#cancelBtn"+id).hide();  
 }else
 {
  $("#editBtn"+id).hide();
  $("#saveBtn"+id).show();
  $("#cancelBtn"+id).show(); 
 }
}
function updateActions(index)
{
 $('#dg').datagrid('updateRow',{
  index: index,
  row:{}
 }); 
}
function myLangFormatter(value,row,index)
{
	for(var i in myLanguages)
	{
		if(myLanguages[i].langValue==value)
			return myLanguages[i].langLabel;
	} 
}
function mycomboBoxItemFormatter(row)
{
	var opts = $(this).combobox('options');
	var val=row[opts.valueField];
	var text=row[opts.textField];
	if(val=='C')
	{ 
		return '<span style="background-color: red;color: yellow">--'+text+'---</span>';
	}
	else
		return text;
}

function mySearch()
{
	 $('#dg').datagrid('load',{
		  selectStatus: selectedArray ,//可传数组，服务端用request.getParameterValues("selectStatus[]")
		date_from: $('#date_from').datebox('getValue'),//日期类型得值 
		date_to: $('#date_to').datebox('getValue'),
		 lang:$('#lang').val(),
		 user:$('#user').val()
		 });
	 
}
//-------Tool Bar
var myIndex=undefined;
function myClickRow(index)
{
	myIndex=index;
	//$('#dg').datagrid('selectRow', index);
}
function myBarEdit()//同 myEditrow
{
	$('#dg').datagrid('beginEdit',myIndex);
}
function myBarSave() // 同 mySaveUpdateRow
{
	$('#dg').datagrid('endEdit',myIndex);　
	var changes=$('#dg').datagrid('getChanges');//得到变化的,可传第二个参数 type,可选值 为inserted,deleted,updated 
	console.log(changes);
}
function myBarUndo() //同  myCancelUpdateRow
{
	$('#dg').datagrid('cancelEdit', myIndex);
}
function myBarRemove() //类似 myDeleterow 
{
	var row = $('#dg').datagrid('getSelected');
	if(row)
	{
		$.messager.confirm('确认','你真的要删除吗?',function(r)
		{
			if (r)
			{
				//调ajax
				$.messager.alert('提示','Remove操作ID='+row.id,'info');
				$('#dg').datagrid('deleteRow', myIndex);//放后面
			}
		});
	}
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
function myBarReload()
{
		 $('#dg').datagrid('loading','任务正在加处理中。。。');//可用进度条
			$('#dg').datagrid('load');//取消显示处理中
			// $('#dg').datagrid('reload');
}



所有的iconCls 的取值在icon.css中	
<div id="tb" style="padding:5px;height:auto">
	<div style="margin-bottom:5px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="myInsert()"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="myBarEdit()"></a> <!-- 加 plain="true" 按钮没有立体感 -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="myBarSave()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="myBarUndo()"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="myBarRemove()">删除高亮选中行</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="myBarRemoveByCheckBox()">删除CheckBox选中行</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="myBarReload()"></a>
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
		<tr> <!-- 老版本的   <th field="id">是不行的   -->
			<th data-options="field:'x',checkbox:true"></th> <!-- 选择的复选框 -->
			<th  width="80" data-options="field:'id'" 	>ID</th> <!-- field的值是JSON对象的属性名 -->
			<th  width="100" data-options="field:'username',editor:{type:'validatebox',options:{required:true}}">用户名</th>
			<th width="100" data-options="field:'language',editor:{
							type:'combobox',
							options:{
								valueField:'langValue',
								textField:'langLabel',
								data:myLanguages,
								required:true,
								formatter: mycomboBoxItemFormatter
							}
						},styler: function(value,row,index){
							if(value=='C')
								return 'background-color:#ffee00;color:red;';
						},formatter:myLangFormatter
						">用语言</th>
     <th width="100" data-options="field:'hobby',editor:{
							type:'combobox',
							options:{
								valueField:'value',
								textField:'name',
								url:'/S_jQueryEasyUI/easyUI/comboBoxEditor',
        method:'post',
								queryParams:{name:'queryHobby',value:'dictHobbyKey'}
							} 
						}		
					">业余,URL取下拉(不支持请求头是json)</th>
			<th width="80"  data-options="field:'salary',editor:{type:'numberbox',options:{precision:1,required:true}}">工资</th> <!--editor:'numberbox'  -->
			<th width="80" data-options="field:'isMan',formatter:myGenderFormatter,editor:{type:'checkbox',options:{on:'true',off:'false'}}">是否为男</th>
			<th width="90"  data-options="field:'birthday',editor:{type:'datebox',options:{required:true}}">生日</th>
			<th width="120"  data-options="field:'comment',editor:'textarea'">comment</th>
			<th width="120" data-options="field:'action',formatter:myActionFormatter" >操作</th>
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



JsonObjectBuilder root=bf.createObjectBuilder() ;
root.add("rows", array);//easyUI 固定
root.add("total",totalCount);//easyUI 固定

//DataGrid返回JSON格式
{"total":"28","rows":[  //固定
	{"id":101}
]}
response.setContentType("application/json;charset=UTF-8");

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

//--------------comboBox 继承自 combo
//-- 不可输入 
<input id="noInput" name="dept" value="aa">  
<script type="text/javascript">
//HTML id="cc" 要在JS前面
  $('#noInput').combobox({
      url:'../demo/comboBox/combobox_data1.json',
      valueField:'id',
      textField:'text',
     editable: false // editable: false是combo的属性 ,不可输入
  });
 </script>
 
 //----没有下箭头按钮,
<select id="noArrow" class="easyui-combobox" name="dept" style="width:200px;">  <br/>
       <option value="aa">aitem1</option>
       <option>bitem2</option>
       <option>bitem3</option>
   </select>   <br/>
<script type="text/javascript">
  $('#noArrow').combobox({
      url:'../demo/comboBox/combobox_data1.json',
      valueField:'id',
      textField:'text', 
      hasDownArrow:false //没有下箭头按钮,是combo的属性 
  });
 </script>
 
 
<input id="inputComboBox" class="easyui-combobox" data-options="
		valueField: 'label',
		textField: 'value',
		limitToList : true" /> 输入一半不选还在,使用 limitToList就可以了   <br/>
<input id="testComboBox" class="easyui-combobox" data-options="
		valueField: 'label',
		textField: 'value',
		onSelect:function(record){
              console.log('onSelect:' +record.text);
          },
          onChange:function(newValue,oldValue)
          {
          	console.log('changed newValue :'+newValue+',old value='+oldValue);
          },
          onHidePanel:myHidePanel " 
		 />  
 //  输入一半不选还在 要自定入onHidePanel的函数 <br/>
  <script type="text/javascript">
  $(function (){
	  var data=[{
			label: 'java',
			value: 'Java'
		},{
			label: 'c/c++',
			value: 'cpp'
		}];
	  $('#testComboBox').combobox('loadData',data);//可用url,method,queryParams请求服务，但返回一定要是数组,不支持请求头是json
	  $('#testComboBox').combobox('select','cpp');
	  
	  $('#inputComboBox').combobox('loadData',data);
	  $('#inputComboBox').combobox('select','cpp');
    
  });
  function myHidePanel()
  {
	  //jqCombox=$(this);
	  //var allSelectData= jqCombox.combobox('getValues');//为多选准备
	  var val = $(this).combobox("getValue");  
      var allData = $(this).combobox("getData"); 
      
      var valueField = $(this).combobox("options").valueField;
      var rightVal = false;  
      for (var i = 0; i < allData.length; i++)
      {
          if (val == allData[i][valueField])
		  {
        	  rightVal = true;
              break;
          }
      }
      if (!rightVal) {
          $(this).combobox("clear");
      } 
  }
  </script>
 //----JSONP 中文不按空格时也算输入？？？ <br/>
    <div class="easyui-panel" style="width:100%;max-width:400px;padding:30px 60px;">
        <div style="margin-bottom:20px">
            <input class="easyui-combobox" style="width:100%;" data-options="
                    loader: myloader,
                    mode: 'remote',
                    valueField: 'id',
                    textField: 'text',
                    label: 'State:',
                    labelPosition: 'top'
                    ">
        </div>
    </div>
    <script type="text/javascript">
        var myloader = function(param,mysuccess,myerror){
            var q = param.q || '';//param在用户输入的情况下有值 ，eayUI传入参数名为q
            if (q.length <  2) //至少2个字母请求服务器
             	return false;
            $.ajax({
            	//浏览器如是localhost,用127.0.0.1就是跨域
            	//url:'http://localhost:8080/S_jQueryEasyUI/easyUI/combbBoxJsonp',
            	url:'http://127.0.0.1:8080/S_jQueryEasyUI/easyUI/combbBoxJsonp',
            	data:{input:q},
                dataType: 'jsonp',
                jsonp: "callback",//默认为:callback，传到服务端的参数名
                jsonpCallback:"myFunc",//传到服务端的参数值,即函数名(也可不传jquery自动生成名字),JS端生成这个函数调用success
                success: function(data){
                    var items = $.map(data, function(item,index){
                        return {
                            id: item.value,
                            text: item.name
                        };
                    });
                    mysuccess(items);
                },
                error: function(){
                	myerror.apply(this, arguments);
                }
            });
        }
    </script>
	servlet端
		String searchName=request.getParameter("input"); 
		String callback=request.getParameter("callback"); 
		
		JsonBuilderFactory bf = Json.createBuilderFactory(null); 
		JsonArrayBuilder array=bf.createArrayBuilder();
		for(long i=1;i<=3;i++)
		{
			JsonObjectBuilder obj=bf.createObjectBuilder() ;
			obj.add("value", 100+i);
			obj.add("name",searchName+i);
			array.add(obj);
		}
		String respStr=callback+"( "+array.build().toString() +");"; 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(respStr);
		
//--------------tagBox 继承自 comboBox
function myTagBoxHidePanel() //输入一半不选就清除
  {
	  var allSelectData= $(this).tagbox('getValues');// 多选 
	  var allData = $(this).tagbox("getData"); 
	  $(this).tagbox("setValues", allSelectData);
	  //$(this).tagbox("clear");//会再次触发这个函数，不能用 
  }
  <input class="easyui-tagbox" label="Add a tag" style="width:100%" data-options="
		url: 'tagbox_data1.json',
		method: 'get',
		value: '3',
		valueField: 'id',
		textField: 'text',
		limitToList: true,
		onHidePanel:myTagBoxHidePanel,
		hasDownArrow: true,
		prompt: 'Select a Language'
		">
		
---ajax tagbox
    <input id="myTagBox" class="easyui-tagbox" label="Add a tag" style="width:100%" data-options="
                    valueField: 'id',
                    textField: 'text',
                    multiple:true,
                    hasDownArrow: true,
                    prompt : 'Select a Language',
                    limitToList: true,
                    
                     events : {input:myOnInput,
                    		compositionstart:myStart,
                    		compositionend:myEnd
                    		} ,
              		onSelect : myOnSelect ,
              		onRemoveTag : myOnRemoveTag 
                  
				
                    ">		
 var selectedTagBox={};
 var isEnd=true;
 function  myStart(e) //是对输入中文的情况下
 {
	 console.log('myStart'+e);
	 isEnd=false;
 }
 function  myEnd(e) //是对输入中文的情况下,表示中文输入完成
 {
	 console.log('myEnd'+e);
	 isEnd=true;
 }
 function  myOnInput(e)
 {
	 if(!isEnd)
		 return;
	var newValue= e.target.value;
	 console.log(newValue);
	   if (newValue.length <  2) //至少2个字母请求服务器
			return false;
		  
	 var root="/S_jQueryEasyUI";
	 $.ajax({
			method:'get',
			url:root+'/easyUI/tagBoxJson',
			 data:{input:newValue},
			  dataType: 'json', 
			  success: function(data)
			  {
				  // selectedTagBox -> data 带有老的选择的
				  $.each(selectedTagBox,function(id,obj){ 
					  data.push(obj);
				  });
				//tagbox,combobox都可以
				 $('#myTagBox').tagbox('loadData', data);
				 $("#myTagBox").tagbox('setText',newValue);//如不调用就清空输入的值 ，也变老的选择的,还要前面加载已经选择的
			  } 
		  });
 }

 function myOnSelect(record)
 {
	 console.log('myOnSelect：'+record);
	
	 if(!selectedTagBox[record.id])
		 selectedTagBox[record.id]=record;
 }
 function myOnRemoveTag(value)
 {
	 console.log('myOnRemoveTag'+value);
	 delete selectedTagBox[value]; // 还可这样删属性
 }
------
选人组件  删除选中的有问题？？？？？？？？？？？<br/>
<span style="width: 200px;">
<!-- 
<input class="easyui-tagbox" style="width: 100%;" type="text" id="SelectUser">
 -->
<input class="easyui-tagbox" style="width: 100%;" type="text" id="SelectUser" value="1" data-tagtext="用户1">
</span>
<script type="text/javascript" src="./selectUser.js"></script>
<script type="text/javascript">
//$("#SelectUser").prop("value",1).attr("data-tagtext","用户1");
initTagBoxWithSelectUser("SelectUser",false,"SelectUser",false);//自己的选人组件
</script>   
-----./selectUser.js

/**
 * 初始化选人控件
 * @param id            控件ID
 * @param multiple      是否多选,true：多选
 * @param name          生成姓名隐藏域,如需要后台保存姓名，可以传此属性
   @param required      是否可为空
 */
function initTagBoxWithSelectUser(id,multiple,name,required) {
    var value = $("#" + id).val();
    var comboData = [];
    var comboValue = [];
    if(value){
        comboValue = value.split(",");
        var text = $("#" + id).attr("data-tagtext");
        if(text){
            var textArr = text.split(",");
            for(var i=0; i < textArr.length; i++) {
                var idText = {id:comboValue[i],text:textArr[i]};
                comboData.push(idText);
            }

        }else{
        	 var root="/S_jQueryEasyUI";
            var params = {"input":value};
            $.ajax({
                type: "post",
            	url:root+'/easyUI/tagBoxJson',
                data: params,
                async: false,
                success: function (data) {
                    comboData = data; 
                }
            });
        }
    }
    var selectedTagBoxUser = {};
    if(multiple == null){
        multiple = false;
    }
    if(required == null){
        required = false;
    }
    var timeout = 0;
    var isInputed = true;
    $("#"+id).tagbox({ 
        limitToList:true,
        prompt: "",
        valueField: 'id',
        textField: 'text',
        value:comboValue,
        data:comboData,
        multiple:multiple,
        required: required,
        //panelWidth:"auto",//文档没有auto的值
        buttonIcon:("icon-man"),
        buttonAlign:"left",
        events:{
        	input: function (e) {
        		newValue=e.target.value;
                //在选择人名时，也会触发onChange事件，获取到value(value为主键ID)，如果继续执行searchForm方法，会将主键ID填入文本框
                //所以需要判断输入是汉字或字母才触发searchForm方法，避免上述问题
                if(!isInputed){
                    return;
                }
                clearTimeout(timeout);
                timeout = setTimeout(function () {
                    var trueValue = newValue;
                    // alert(trueValue)
                    if (isChinese(trueValue)) {
                        searchTagBoxForm("fullname", trueValue, id, selectedTagBoxUser);
                    }
                    else if (isChar(trueValue) && trueValue.length > 1) {
                        searchTagBoxForm("email", trueValue, id, selectedTagBoxUser);
                    }
                    $("#"+id).tagbox("panel").css("width","auto")//auto
                },300);
            },compositionstart:function(e){
                isInputed = false;
            },compositionend:function(e){
            	isInputed = true;
            }
        },
               
        onSelect:function(data){
            //保存已选择的Item
            if(! selectedTagBoxUser[data.id]){
                if(!multiple){
                    selectedTagBoxUser = {};
                }
                selectedTagBoxUser[data.id] = data;
            }
            //将textField的显示文本截取
            if(data.text.indexOf("(") != -1){
                data.text = data.text.substr(0,data.text.indexOf("("));
            }
            //将textField的显示文本截取
            if(data.text.indexOf(" ") != -1){
                data.text = data.text.substr(0,data.text.indexOf(" "));
            }
            if(name){
                updateNameTextHidden(id,selectedTagBoxUser,name,this);
            }

        },
        onRemoveTag:function(itemId){
            //删除已选中的记录
            delete selectedTagBoxUser[itemId];//只这样不行的??????????
            if(name){
                updateNameTextHidden(id,selectedTagBoxUser,name,this);
            }
        }

    });
}
function updateNameTextHidden(compomentId,selectedTagBoxUser,name,compom){
    var value = "";
    var length = 0;
    $.each(selectedTagBoxUser,function(i,param){
        value += param.text;
        if(length < Object.keys(selectedTagBoxUser).length - 1){
            value += ",";
        }
        length ++;
    });
    //增加
    if($("#"+compomentId+"_name").length == 0){
        var hiddenInput = "<input type=\"hidden\" id=\""+compomentId+"_name\" name=\""+name+"\" value=\""+value+"\" />";
        $(compom).append(hiddenInput);
    }else{
        $("#"+compomentId+"_name").val(value);
    }

} 
function searchTagBoxForm(field,value,compomentId,selectedTagBoxUser){ 
 	var data = {"input":value}; 
    var root="/S_jQueryEasyUI";
    $.post(root+'/easyUI/tagBoxJson',data,
        function(result){
            if(selectedTagBoxUser){
                $.each(selectedTagBoxUser,function(i,param){
                    result.push(param);
                });
            }
            $("#"+compomentId).combobox("loadData", result);
            $("#"+compomentId).combobox("setText", value);

        });
}
//检测是否为中文，true表示是中文，false表示非中文
function isChinese(str){
    if(/^[\u3220-\uFA29]+$/.test(str)){
        return true;
    }else{
        return false;
    }
}
function isChar(str){
    var i = /^(?!_)([A-Za-z ]+)$/;
    if(!i.test(str)){
        return false;
    }
    return true;
}
//--------------treeGrid
服务端加载数据，服务端分页


$.extend($.fn.validatebox.defaults.rules, 
		{
			maxLength: 
			{
				validator: function(value, param)
				{
					return value.length < param[0];
				},
				message: '最长 {0}个字符.'
			},
		});
	var editId=undefined;
	function myOnDblClickRow (row) //不同于 datagrid的参数
	{
		$(this).treegrid('beginEdit',row.myid)//idField的字段,不同于 datagrid是第索引
		editId=row.myid;
	}
	function myOnClickRow( row)//不同于 datagrid的参数
	{
		//行内编辑的验证 textbox 继承自validatebox
 		var validateOK=false;
 		$("#myTreegridForm").form('submit' ,
		{   onSubmit:function()
			{
				validateOK= $(this).form('enableValidation').form('validate');
				console.log(" in onSubmit validateOK="+validateOK);
				return false;//表单只为验证用，这里永远返回false
			}
		});
 		console.log(" in myOnClickRow validateOK="+validateOK);
 		//是同步执行
 		if(validateOK)
			$(this).treegrid('endEdit',editId) 
	}
	function myOnAfterEdit(row,changes)
 	{
 		$(this).treegrid('checkNode', row.myid);
 	}
	function myOnContextMenu(e, row)
	{
		e.preventDefault();
		$("#myTableContextMenu").menu('show', {
		  left: e.pageX,
		  top: e.pageY
		});
	}
 function  rightMenuEdit()
	{
		var selectArray=$("#myTreegrid").treegrid('getSelections');
		console.log(selectArray);
		var selectFirst=$("#myTreegrid").treegrid('getSelected');
		console.log(selectFirst);
		//myOnDblClickRow(selectFirst); //有用this
		$("#myTreegrid").treegrid('beginEdit',selectFirst.myid);
		editId=selectFirst.myid;
	}
	function  rightMenuEditCancel()
	{
		var selectFirst=$("#myTreegrid").treegrid('getSelected');
		$("#myTreegrid").treegrid('cancelEdit',selectFirst.myid);
	}
	function  rightMenuEditSave()
	{
		var selectFirst=$("#myTreegrid").treegrid('getSelected');
		$("#myTreegrid").treegrid('endEdit',selectFirst.myid) ;
	}
	function  rightMenuDelete()
	{
		var selectFirst=$("#myTreegrid").treegrid('getSelected');
		var childs=$("#myTreegrid").treegrid('getChildren',selectFirst.myid) ;
		if(childs.length>0)
		{
			$.messager.alert('提示','有子节点不能删除','error');
		}else
		{
			$.messager.confirm('删除','确定删除吗？',function (r){
				if(r)
					$("#myTreegrid").treegrid('remove',selectFirst.myid) ;
			});	
		}
	}
<div id="myTableContextMenu" class="easyui-menu" style="width:120px;"> 
    <div data-options="iconCls:'icon-add'" onclick="">增加同级</div> 
    <div data-options="iconCls:'icon-add'" onclick="">增加子级</div> 
    <div data-options="iconCls:'icon-edit'" onclick="rightMenuEdit()">Edit</div> 
    <div data-options="iconCls:'icon-save'" onclick="rightMenuEditSave()">Save Edit</div> 
    <div data-options="iconCls:'icon-undo'" onclick="rightMenuEditCancel()">Cancel Edit</div> 
    <div data-options="iconCls:'icon-remove'" onclick="rightMenuDelete()">delete</div> 
</div>
	<form id="myTreegridForm">
 	 <table id="myTreegrid"  title="Products" class="easyui-treegrid" style="width:700px;height:300px"
                data-options="
                    url: '/S_jQueryEasyUI/easyUI/treeGridPage',
                    rownumbers: true,
                    pagination: true,
                    pageSize: 10,
                    pageList: [2,10,20],
                    //checkbox: true,
					checkbox: function(row) //条件是否有复选框
				   {
						 if(row.myid%2==0)
							 return true;
						 else 
							 return false;
					},
                    idField: 'myid', 
					treeField: 'name2',
					onDblClickRow:myOnDblClickRow,
                    onClickRow:myOnClickRow,
					onAfterEdit:myOnAfterEdit,
       				onContextMenu:myOnContextMenu,
                    onBeforeLoad: function(row,param){
                        if (!row) {    // load top level rows
                            param.id = 0;    // set id=0, indicate to load new page rows
                        }
                    }
                "> 
			<!-- 
				,
				onBeforeLoad: function(row,param){
					if (!row) {    // load top level rows
						param.id = 0;    // set id=0, indicate to load new page rows
					}
				}
		
			
			treeField: 'name2' 一定是表格中存在的
			-->
            <thead>
                <tr>
                    <th field="name" width="250" formatter="nameFormater" >Name</th>
					<th field="name2" width="250">Name2</th>
					 <th field="nameValidate" width="250" data-options="editor:{type:'textbox',options:{validType:'maxLength[30]'}}"  >nameValidateLength30</th>
                    <th field="quantity" width="100" align="right" editor="numberspinner">Quantity</th>
                    <th field="price" width="150" align="right" formatter="formatDollar" editor="numberbox">Price</th>
                    <th field="total" width="150" align="right" formatter="formatDollar">Total</th>
                </tr>
            </thead>
        </table>
		</form>
		
        <script>
            function formatDollar(value){
                if (value){
                    return '$'+value;
                } else {
                    return '';
                }
            }
			
			var saveFunc=[];
			function nameFormater(value,row,index)
			{
				var key='btnClick'+row.myid;
				saveFunc[key]=rowClick.bind(this,row);//row也会存到数组中,第一个参数this必传
				var  strFunc="saveFunc['"+key+"'](this)";//这里this可以省略
				return '<button onclick="'+strFunc+'">'+value+'</button>';
				//return '<button onclick="rowClick(row)">'+value+'</button>';//这样单击时得到不row的值，要存起来
			}
			function rowClick(row)
			{
				console.log(row);
			}
			function addChange()
			{
				// update (beginEdit也不行) 后 getChanges 得不到变化的！！！,只能是更新UI
				$("#myTreegrid").treegrid('update',{
					 id:10,
					 row:{
						 myid:theId,
						 name:'李0001', 
						 name2: "张0001", 
						 price: "1000", 
						 quantity: "2", 
						 state: "closed", 
						 total: 2000
					 }			 
				}); 
			}
			function showChange()
			{
				var changes=$("#myTreegrid").treegrid('getChanges');//得到变化的,可传第二个参数 type,可选值 为inserted,deleted,updated 
				console.log(changes);
				var updated=$("#myTreegrid").treegrid('getChanges','updated');
				console.log(updated);
				
				//treegrid是getData，datagrid是getRows
				var newData=$("#myTreegrid").treegrid('getData' );//是修改后的值 
				//即使数据格式是　_parentId,这里得到的数据也是children格式,每一项数据也多加(如源数据没用)_parentId
				console.log(newData);
			}
			function showCheckedAndLevel()
			{
				var checkedArray=$("#myTreegrid").treegrid('getCheckedNodes');
				
				console.log(checkedArray);
				for(var i in checkedArray)
				{
					var row=checkedArray[i];
					var level=$("#myTreegrid").treegrid('getLevel',row.myid);//得到级别
					console.log("name2="+row.name2+",myId="+row.myid+",level="+level);
				} 
			}
        </script>


response.setContentType("application/json;charset=UTF-8");
response.setCharacterEncoding("UTF-8");

String reqPageNO=request.getParameter("page");//easyUI 固定
String reqPageSize=request.getParameter("rows");//easyUI 固定
String id=request.getParameter("id");//easyUI 固定,展开的ID, 并不是idField的值
//如页面没有设置为0，初始时(没展开时)这是null

JsonBuilderFactory bf = Json.createBuilderFactory(null); 
if("0".equals(id)   //0是 页面中设置的（当为空时设置为0），即第一次进入页时，没有手工展开
	 ||id ==null
)
	JsonArrayBuilder array=bf.createArrayBuilder();
	for(long i=start;i<=end*10;i+=10)
	{
		JsonObjectBuilder obj=bf.createObjectBuilder() ;
		obj.add("myid", 10+i+"");
		obj.add("name","李"+i);
		obj.add("name2","张"+i);
		if(i%20==0)
		{
			obj.add("state","closed");//表示文件夹关闭，否则是普通文件，open
		}else
		 {
			obj.add("checked",true); //表示是否复选
			obj.add("iconCls","icon-ok"); //图标
			int quantity=new Random().nextInt();
			double price=new Random().nextDouble();
			
			obj.add("quantity",quantity);
			obj.add("price",price); 
			obj.add("total",price*quantity);
		 }
		array.add(obj);
	}

	JsonObjectBuilder root=bf.createObjectBuilder() ;
	root.add("rows", array);//easyUI 固定
	root.add("total",totalCount);//easyUI 固定
	System.out.println(root.build().toString());

	response.getWriter().write(root.build().toString());
	response.flushBuffer();
//	response.getOutputStream().write(root.build().toString().getBytes());
}else
{ //展开请求
	int idVal=Integer.parseInt(id);
	JsonArrayBuilder array=bf.createArrayBuilder();
	for(long i=idVal+1;i<=idVal+5;i+=1)
	{
		JsonObjectBuilder obj=bf.createObjectBuilder() ;
		obj.add("myid", i);
		
		if(i%20==0)
			obj.add("state","closed");//表示文件夹关闭，否则是普通文件，open
		 else
			 obj.add("checked",true); //表示是否复选
		
		obj.add("name","李"+i);
		obj.add("name2","张"+i);
		int quantity=new Random().nextInt();
		double price=new Random().nextDouble();
		
		obj.add("quantity",quantity);
		obj.add("price",price); 
		obj.add("total",price*quantity);
		array.add(obj);
	} 
	//单独展开的请求不能返回rows属性 ,只能是数组
	response.getWriter().write(array.build().toString());
	response.flushBuffer();
}

//treegrid中editor自定义为tagbox,为选人 
$.extend($.fn.treegrid.defaults.editors,{//新增的editor,系统中的editor还是存在的
		 SelectUser: {
              init: function(container, options){
                  var input = $('<input class="easyui-tagbox" style="width: 100%;" type="text" id="SelectUser">').appendTo(container);
                  return initTagBoxWithSelectUser("SelectUser",false,"SelectUser",false);//单独的组件
              },
              destroy: function(target){
                  $("#SelectUser").tagbox('destroy');
              },
              getValue: function(target){
            	  var userIdVal=$("#SelectUser").tagbox('getValue');
                  $("#myTreegrid").treegrid('find',editId).userid=userIdVal;
                  return $(".datagrid-editable .tagbox-label").text();
              },
              setValue: function(target, value){
				  //已 经有值,先调init后,再调setValue
            	  $("#SelectUser").tagbox('loadData',[{
            		  id:value,
            		  text:value//查DB？？？ 或者拿到row.         			
            	  }]);
                  $("#SelectUser").tagbox('setValue',value); 
              },
              resize: function(target, width){
                  var input = $("#SelectUser");
                  if ($.boxModel == true) {
                      input.width(width - (input.outerWidth() - input.width()));
                  } else {
                      input.width(width);
                  } 
          }} //editors
			
		
	});//extend

treegrid 再加 ,editor:"SelectUser"

//treegrid是getData，datagrid是getRows
var newData=$("#myTreegrid").treegrid('getData' );//是修改后的值



function expandLevel2(){
	var datas=$("#tg").treegrid('getData');//即使数据格式是　_parentId,这里得到的数据也是children格式,每一项数据也多加(如源数据没用)_parentId
	recursiveExpand(datas,2);
}
function recursiveExpand(datas,level)
{
	for(var i in datas)
	{
		if(datas[i].level==level)//数据中有level,id
		{
			$('#tg').treegrid('expandTo',datas[i].id).treegrid('select',datas[i].id);
			return;
		}else if(datas[i].children)
		{
			recursiveExpand(datas[i].children,level)
		}
	} 
}

//--------------comboTree


//--------------comboGrid

//--------------ComboTreeGrid


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


