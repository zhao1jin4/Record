
Bootstrap4  Bootstrap.min.css 的体积减少了40%以上
Bootstrap4 放弃了对 IE8 以及 iOS 6 的支持，现在仅仅支持 IE9 以上 以及 iOS 7 以上版本的浏览器。如果对于其中需要用到以前的浏览器，那么请使用 Bootstrap3

https://www.runoob.com/bootstrap4/bootstrap4-tutorial.html

=============BootStrap  3.3.7 开源的
Hadoop 的UI使用Bootstrap
依赖于jQuery

Bootstrap 的响应式 CSS 能够自适应于台式机、平板电脑和手机。
Bootstrap 的源码是基于最流行的 CSS 预处理脚本 - Less 和 Sass 开发的

node.js 的npm管理依赖

<script src="${pageContext.request.contextPath}/jquery-3.2.1.min.js"></script>

<link rel="stylesheet"  href="${pageContext.request.contextPath}/bootstrap-3.3.7-dist/css/bootstrap.min.css"  >
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css" >
<script src="${pageContext.request.contextPath}/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>

<!-- 以上是公共的 -->

<link rel="stylesheet" href="${pageContext.request.contextPath}/toastr-2.1.3/toastr.min.css" >
<script src="${pageContext.request.contextPath}/toastr-2.1.3/toastr.min.js" ></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-daterangepicker-2.1.25/daterangepicker.css" >
<script src="${pageContext.request.contextPath}/bootstrap-daterangepicker-2.1.25/moment.min.js" ></script>
<script src="${pageContext.request.contextPath}/bootstrap-daterangepicker-2.1.25/daterangepicker.js" ></script>

<script src="${pageContext.request.contextPath}/bootbox-4.4.0/bootbox.min.js" ></script>
	
--- bootstrap
 <input type="button"   class="btn  btn-default" value="查询"/>
 
====插件

----Date Range Picker　依赖于  Bootstrap ,jQuery ,Moment.js　  测试OK 
$(document).ready(
	function() {
	
		   $('#singleDate').daterangepicker({
	            singleDatePicker: true,
	            startDate: moment(),
	            timePicker: true,
	            format: 'YYYY-MM-DD'  //无效?
	            		
	        }
		  /*, function(start, end, label) {
	        	alert( start.format('YYYY-MM-DD'))
	        	$('#singleDate').val( start.format('YYYY-MM-DD')) //无效?
			}
	        */
		  );
		 
		  $('#entryTime').daterangepicker({
	            locale : {
	                applyLabel : '确定',
	                cancelLabel : '取消',
	                fromLabel : '起始时间',
	                toLabel : '结束时间',
	                customRangeLabel : '自定义',
	                daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
	                monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
	                    '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	                firstDay : 1
	            }//format:'YYYY-MM-DD' , //无效?
		   	 "startDate":now,
		     "endDate": moment(),
		     "minDate":now,
		     "maxDate": moment()
	        }, function(start, end, label) {
	            console.log(start.toISOString(), end.toISOString(), label);
	        });

		  
		 $('#searchTimeStr').daterangepicker({
		        timePicker: true,
				  separator:'到',
		       // timePickerIncrement: 30,
		        format: 'YYYY/MM/DD hh:mm:ss'  //选择后无效?
		    }, function(start, end, label) {
		        console.log(start.toISOString(), end.toISOString(), label);
				  var now = new Date();
                   var ago=now;
                   ago.setDate(now.getDate()-31);
                   if(start < ago )
                        toastr.warning('签收时间最多查31天');
						
		    });
	});
  
日期范围 　<input type="text" id="searchTimeStr"  style="margin-left: 0" size='30'/> <br/>
日期范围中文 <input type="text" id="entryTime"  placeholder="2017-5-16"  size='30'> <br/>
单个日期     <input type="text"  id="singleDate"/> <br/>
---- toastr.min.js  也要jQuery(其实是BootStrap依赖于jQuery) 类似 android toast 测试OK 

 toastr.options.positionClass = 'toast-top-center';
 
 toastr.success('处理成功');
 toastr.warning('警告');
<button id="errorButtion" type="button" class="btn btn-default"  onclick=" toastr.error('查询失败!');">测试错误消息</button>

----Bootbox.js

	 function confirmDelete()
	 {
		 /*   bootbox.confirm({
		        message: "确认吗删除吗?",
		        buttons: {
		            confirm: {
		                label: '是',
		                className: 'btn-success'
		            },
		            cancel: {
		                label: '否',
		                className: 'btn-danger'
		            }
		        },
		        callback: function (result) {
		            console.log('选择了: ' + result);
		        }
		    });
		   */
		 bootbox.confirm("确认吗删除吗?", function (result){
			 console.log('选择了: ' + result);
		 });
	 }
<button id="confirmButtion" type="button" class="btn btn-default"  onclick=" bootbox.alert('操作成功'); ">提示成功 </button>
<button id="confirmButtion" type="button" class="btn btn-default"  onclick=" confirmDelete() ">确认吗</button>

   bootbox.alert(res.result,function(){
				   $('#newForm')[0].reset() ;
				   $('#addModal').modal('hide') ;// $('#addModal').modal('show') 
			   });
			   

---- datatables

<link rel="stylesheet" href="${pageContext.request.contextPath}/DataTables-1.10.15/media/css/jquery.dataTables.min.css" >
<script  type="text/javascript" src="${pageContext.request.contextPath}/DataTables-1.10.15/media/js/jquery.dataTables.min.js" ></script>

	 $('#myServerArrayTable').DataTable( {
		        "processing": true,
		        "serverSide": true,
		        "ajax": "${pageContext.request.contextPath}/datatablesArrayServlet",
		        
		        "oLanguage" : { // 汉化
		        	"sProcessing" : "正在加载数据...",
		        	"sLengthMenu" : "显示_MENU_条 ",
		        	"sZeroRecords" : "没有您要搜索的内容",
		        	"sInfo" : "从_START_ 到 _END_ 条记录——总记录数为 _TOTAL_ 条",
		        	"sInfoEmpty" : "记录数为0",
		        	"sInfoFiltered" : "(全部记录数 _MAX_  条)",
		        	"sInfoPostFix" : "",
		        	"sSearch" : "搜索",
		        	"sUrl" : "",
		        	"oPaginate" : {
			        	"sFirst" : "第一页",
			        	"sPrevious" : " 上一页 ",
			        	"sNext" : " 下一页 ",
			        	"sLast" : " 最后一页 "
			        	}
		        },
		        "iDisplayLength" : 30,// 每页显示行数
		        
		    } );
		
		 $('#myServerJsonTable').DataTable( {
		        "processing": true,
		        "serverSide": true,
		        ajax:   "${pageContext.request.contextPath}/datatablesJsonServlet", 
				"columns": [
			            { "data": "stuid" },
			            { "data": "name" },
			            { "data": "score" },
			            { "data": "birthday" } 
			        ]
		 } );

 <table id="myServerArrayTable" class="display" width="100%" cellspacing="0">
	 <thead>
		<tr>
			<th>stuid</th>
			<th>name</th>
			<th>score</th>
			<th>birthday</th>
		</tr>
	</thead>
  </table>

  
  <table id="myServerJsonTable" class="display" width="100%" cellspacing="0">
	 <thead>
		<tr>
			<th>stuid</th>
			<th>name</th>
			<th>score</th>
			<th>birthday</th>
		</tr>
	</thead>
  </table>

问题 总是请求中     ,页数30时,选择中没有30

----  dialog    modal.js 在bootstrap.js中有的


  <a href="#" class="add" data-toggle="modal" data-target="#addModal">新建</a>
      
    <!--新增弹窗  如要dialog上再显示dialog class中去fade
		 tabindex="-1" -->
    <div class="modal fade" id="addModal"  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       				<h4 class="modal-title"  >新建e</h4>
                </div>
                <div class="modal-body">
                    <div class="main-form-box clearfix">
                        <div class="main-form-control fl">
                            <label>姓名：</label>
                            <input type="text" name="name" onclick="showChildDialog()" />
                        </div>
                        <div class="main-form-control fl">
                            <label>日期：</label>
                            <input type="text"   name="departTime" id="newDepartTime"/>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <a href="#"   aria-label="Close"  >确认</a>
                    <a href="#"   data-dismiss="modal" aria-label="Close">取消</a>
					 <a href="#"    onclick="$('#addModal').modal('hide') ">消失</a>
                </div>
            </div>
        </div>
    </div>
 
 
 
  function showChildDialog()
 {
	 //$('#childDialog').show();
	 $('#childDialog').modal('show');
	 
 }
 
   <!-- 子弹窗   如要dialog上再显示二级子dialog，要在 class中去fade   不用加 style="z-index:30" 
    aria-hidden="true"表示初始是隐藏的就不用 style="display: none;"
   -->
  <div class="modal " id="childDialog"  role="dialog"   aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="min-width: 1000px"> <!-- 控制窗口宽度 --> 
                <div class="modal-header">
                   header
                </div>
                <div class="modal-body" style="max-height: 500px;overflow: auto"> <!-- 控制内容滚动条，限高--> 
                     body
					 <input type="text" value="如要在这里可以输入值，要在第一个框去 tabIndex"/>
                </div>
                <div class="modal-footer">
                    footer
                      <a href="#"    onclick="$('#childDialog').hide(); ">消失</a>
                </div>
            </div>
        </div>
    </div>
	
----   bsSuggest

	<script src="${pageContext.request.contextPath}/hplus/bootstrap-suggest.js" ></script>
	
//要放     $(function(){}中
        $("#testNoBtn").bsSuggest({
            url:'${pageContext.request.contextPath}/suggestJsonServlet',
            effectiveFieldsAlias:{name: "姓名",stuid:'学号'},
            ignorecase: true,
            showHeader: true,
            showBtn: false,
            delayUntilKeyup: true,
            idField: "name",  
            keyField: "stuid",//选中使用的
            clearable: true
        }) ;

   <div class="input-group">
        <input type="text" name="stuid" id="testNoBtn"/>
        <div class="input-group-btn">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right" role="menu">
            </ul>
        </div>
        <!-- /btn-group -->
    </div>
             

----   Popover


	<script src="${pageContext.request.contextPath}/bootstrap-3.3.7/js/tooltip.js" ></script>
	<script src="${pageContext.request.contextPath}/bootstrap-3.3.7/js/popover.js" ></script>
	
$(function () {
	  $('[data-toggle="popover"]').popover();
	})
	
<c:if test="${item.opContent.length() > 50 }">
	<button  
		data-toggle="popover" data-trigger="click" 
		data-placement="bottom" 
		data-content="${item.opContent}"  >
		${fn:substring(item.opContent,0,50)} ...
	</button>
</c:if>
<c:if test="${item.opContent.length() <= 50 }"> ${item.opContent}</c:if>
	
	
----   Tab

	<ul id="myTab" class="nav nav-tabs">
	<li class="active">
		<a href="#home" data-toggle="tab">
			 菜鸟教程
		</a>
	</li>
	<li><a href="#ios" data-toggle="tab">iOS</a></li>
	<li class="dropdown">
		<a href="#" id="myTabDrop1" class="dropdown-toggle" 
		   data-toggle="dropdown">Java 
			<b class="caret"></b>
		</a>
		<ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
			<li><a href="#jmeter" tabindex="-1" data-toggle="tab">jmeter</a></li>
			<li><a href="#ejb" tabindex="-1" data-toggle="tab">ejb</a></li>
		</ul>
	</li>
</ul>
<div id="myTabContent" class="tab-content">
	<div class="tab-pane fade in active" id="home">
		<p>菜鸟教程是一个提供最新的web技术站点，本站免费提供了建站相关的技术文档，帮助广大web技术爱好者快速入门并建立自己的网站。菜鸟先飞早入行——学的不仅是技术，更是梦想。</p>
	</div>
	<div class="tab-pane fade" id="ios">
		<p>iOS 是一个由苹果公司开发和发布的手机操作系统。最初是于 2007 年首次发布 iPhone、iPod Touch 和 Apple 
			TV。iOS 派生自 OS X，它们共享 Darwin 基础。OS X 操作系统是用在苹果电脑上，iOS 是苹果的移动版本。</p>
	</div>
	<div class="tab-pane fade" id="jmeter">
		<p>jMeter 是一款开源的测试软件。它是 100% 纯 Java 应用程序，用于负载和性能测试。</p>
	</div>
	<div class="tab-pane fade" id="ejb">
		<p>Enterprise Java Beans（EJB）是一个创建高度可扩展性和强大企业级应用程序的开发架构，部署在兼容应用程序服务器（比如 JBOSS、Web Logic 等）的 J2EE 上。
		</p>
	</div>
</div>