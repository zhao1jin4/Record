
Freemarker 不能在群集上面发布应用



Configuration    setClassForTemplateLoading(java.lang.Class clazz, java.lang.String pathPrefix) 
									Template getTemplate(java.lang.String name) //ftl文件位置
          							Equivalent to getTemplate(name, thisCfg.getLocale(), thisCfg.getEncoding(thisCfg.getLocale()), true). 
          							
 Template   process(java.lang.Object rootMap, java.io.Writer out) Map类型

 
Configuration cfg= new Configuration();
cfg.setServletContextForTemplateLoading(getServletContext(),"WEB-INF/freemarkers");//放在WEB-INF下才安全
//cfg.setClassForTemplateLoading(MyClass.class,"/freemarkers");

Template t = cfg.getTemplate("test.ftl");
requestTemplate.setEncoding("UTF-8");

response.setContentType("text/html; charset=" + t.getEncoding());
Writer out = response.getWriter(); //或者使用 StringWriter()

Map<String,Object> root = new HashMap<>();
root.put("message", "Hello FreeMarker!");
List<String> allAnimals=new ArrayList<>();
allAnimals.add("猫");
allAnimals.add("狗 ");
root.put("animals",allAnimals);

t.process(root, out);
  
FreeMarker自带显示的方法
<#if myDate?exists>
   ${myDate?string("yyyy-MM-dd HH:mm:ss")}  
</#if>  
 
 &lt;   小于
 
转义
 \l	<  
\g	>
\a	&
\{	{


${r"${foo}"}
${r"C:\foo\bar"}  
输出的结果是：
${foo}
C:\foo\bar 
<#if (x > y)>
另一种替代的方法是，使用lt、lte、gt和gte来替代<、<=、>和>=

#{expr; format}形式可以用来格式化数字，format可以是：
  mX：小数部分最小X位
  MX：小数部分最大X位



 <#local x = "local">局部变量
<#set x="r{foo}">
 <#assign x = "plain"> 全局变量
 <#import " xx.ftl" as 别名1>
 <#assign x="xx" in 别名1>
 ${.globals.user}  访问数据模型中的同名变量，使用特殊变量global

 <#list allUser as temp>
						
	<td> 
	${temp_index}得到索引
	<#if allUser?size==5>是5</#if> 调用方法用？

<#assign seq = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j']> 
	<#list seq?chunk(4) as row> 




<#assign users = [{"name":"Joe",        "hidden":false},
                  {"name":"James Bond", "hidden":true},
                  {"name":"Julia",      "hidden":false}]>
<#list users as user>
  <#if !user.hidden>
	user["hidden"] //可以动态属性像JS
  
<#noparse>
其中的内容不解析对<#>形式的
</#noparse>





------freemarker
Freemarker 不支持集群

<h1>Welcome ${user!"Anonymous"}!</h1>       	 如果user的值为null  就为它指定默认值
<#if user??><h1>Welcome ${user}!</h1></#if>   ?? 如果user 变量不存在(为null)的话将会忽略整个问候代码

<#if item.gender == 'M'> 男
<#elseif item.gender == 'F'>女
<#else>未知
</#if>

<#switch item.gender>
	<#case "M">男<#break>  <!--可以是字串 -->
	<#case "F"> 女<#break>
	<#default> 未知
</#switch>

animals.python.price!0，仅当animals.python 存在而仅仅最后一个子变
(animals.python.price)!0

animals.python.price??对比(animals.python.price)??

seq 存储序列"a","b", "c", "d", "e", "f"，
seq[3..]将是含有"d", "e", "f"的序列。
seq[1..4]

${1.1?int}  的整数部分

字符串文字 <#include "/footer/${company}.html">   中使用
一个典型的错误使用是<#if ${isBig}>Wow!</#if> ， 这是语法上的错误。
只能这么来写： <#if isBig>Wow!</#if>，
<#if "${isBig}">Wow!</#if>来写也是错误的

内建函数
?html: 字符串中所有的特殊HTML 字符都需要用实体引用来代替（比如<代
替&lt;）。
? cap_first:字符串的第一个字母变为大写形式
? lower_case:字符串的小写形式
? upper_case:字符串的大写形式
? trim:去掉字符串首尾的空格
? 序列使用的内建函数：
? size：序列中元素的个数
? 数字使用的内建函数：
? int:数字的整数部分（比如-1.9?int 就是-1）

如test 存储”Tom & Jerry
${test?html}
${test?upper_case?html}	
输出
Tom &amp; Jerry
TOM &amp; JERRY


${x!1 + y}会被FreeMarker 误解为${x!(1 + y)}，而真实的意义是${(x!1) + y}。

对于freemarker ${num} 形式显示数字时带","分隔, {num?string('#')}  或 <#setting number_format="#" />

number_format
time_format , date_format 和datetime_format
<#setting locale="it_IT">
<#setting number_format="0.####" >


<#escape x as x?html>
...
<p>Title: ${book.title}</p>
<p>Description:
<#noescape>${book.description}</#noescape></p>
<h2>Comments:</h2>
<#list comments as comment>
<div class="comment">
${comment}
</div>
</#list>
...
</#escape>



”married”（假设它是布尔值），那么可以这么来写: ${married?string("yes","no")}


<#list 1..count as x>





<#macro do_thrice>
<#nested 1>
<#nested 2>
<#nested 3>
</#macro>
<@do_thrice ; x> <#-- 用户自定义指令 使用";"代替"as" -->
${x} Anything.
</@do_thrice>

输出
1 Anything.
2 Anything.
3 Anything.



[#ftl] 

 

<#if allClazz?exists>
<#-- _has_next , _index-->
<#list allClazz as item> 
	<#if item_has_next> 
		正常数据行
	 <#else>
		在最后一行
	 </#if>
	 ,
	 <#if item_index % 2 == 0>
		在偶数行
	<#else>
		在奇数行
	</#if>
	,${item}<br/>
</#list>
</#if>

 
@SessionAttributes({SessionKey.PAGE_SIZE})//将ModelMap中的  PAGE_SIZE 放在session中
public abstract class PageController 
{
	public void initPage(ModelMap model)
	{
		model.addAttribute("pageNO", 1);
		model.addAttribute("pageCount", 1);
		model.addAttribute("allPageSize",this.generatePageSize());
		this.getSessionPageSize(0,model);//存储session PAGE_SIZE
	}
	//调用submitPage取session要在方法参数中有 ModelMap
	public long[] submitPage(int reqPageNO,int reqPageSize,long totalCount,ModelMap model)
	{
		int pageSize=this.getSessionPageSize(reqPageSize,model); //存取 session PAGE_SIZE
		int pageCount=(int)(totalCount/pageSize) + 1;
		int pageNO=1;
		if(reqPageNO > 0)
			pageNO= reqPageNO;
		if(pageNO>pageCount)
			pageNO=pageCount;
		model.addAttribute("pageNO", pageNO);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("allPageSize",generatePageSize());
		//为Oracle传参数
		long start=(pageNO-1)*pageSize+1;//pageNO*pageSize-pageSize+1
		long end=pageNO*pageSize;
		long[] range=new long[2];
		range[0]=start;
		range[1]=end;
		return range;
	}
	
    private int  getSessionPageSize(int reqPageSize,ModelMap model)
	{
    	 int pageSize=DEF_PAGE_SIZE;
    	//优先使用reqPageSize,
	    if(reqPageSize > 0 )//传了
	    {
	    	pageSize=reqPageSize;
	    	model.addAttribute(SessionKey.PAGE_SIZE,reqPageSize);
	    } else//其次使用session的
	    {
	    	Object sessionPage =model.get(SessionKey.PAGE_SIZE);
	 	    if(sessionPage!=null)
	 	    	pageSize=Integer.parseInt(sessionPage.toString());
	 	    else
	 	    	model.addAttribute(SessionKey.PAGE_SIZE,pageSize);
	    }
	    return pageSize;
	}
    private List<Integer> generatePageSize()
	{
		List<Integer> allPageSize=new ArrayList<Integer>();
		allPageSize.add(DEF_PAGE_SIZE-10);
		allPageSize.add(DEF_PAGE_SIZE);
		allPageSize.add(DEF_PAGE_SIZE+10);
		allPageSize.add(DEF_PAGE_SIZE+30);
		return allPageSize;
	}
}
 
<script type="text/javascript">
function goPage(pageNO)
{
	if(/^([0-9])+$/.test(pageNO))
	{	
		//$("form")[0].submit();
		document.getElementsByName('pageNO')[0].value=pageNO;
		document.getElementsByTagName("form")[0].submit();
	}else
		alert('请输入正整数');
}
</script>
<div>
	<span>
		<#if pageNO == 1>
				首页   &nbsp; 上一页    
		<#elseif pageNO &lt;=  pageCount> 
				<input type="button" onclick="goPage(1)" value="首页"/>  &nbsp;
				<input type="button" onclick="goPage(${pageNO-1})" value="上一页"/> 
		</#if>
	</span>
	&nbsp;
	<span>
		<#list -3..3 as i>
			<#if pageNO+i gt 0 && pageNO+i lte pageCount>
		 		<#if i==0>
		 			<font color="yellow" style="background-color:grey">&nbsp; ${pageNO} &nbsp;</font>
		 		<#else>
					&nbsp;  <a href="#" onclick="goPage(${pageNO+i})" > ${pageNO+i} </a>    &nbsp;
		 		</#if>
		 	</#if>
		 </#list>
	</span>
	&nbsp;
	<span>
		<#if  pageNO == pageCount> 
				下一页    &nbsp; 尾页
		<#elseif  pageNO lt pageCount>
			 	<input type="button" onclick="goPage(${pageNO+1})" value="下一页"/>   &nbsp;
			 	<input type="button" onclick="goPage(${pageCount})" value="尾页"/>
		</#if>
	</span>
	&nbsp;&nbsp;&nbsp;
	<span>
		到第<input type="text" name="pageNO" value="${pageNO}" size="2" maxlength="2" class="text ui-widget-content ui-corner-all" /> /${pageCount}页     &nbsp;
	  	 <input type="button" onclick="goPage(document.getElementsByName('pageNO')[0].value)" value="跳页">  &nbsp; 
	</span>
	&nbsp;
	<span>
		每页  <select name="pageSize" class="text ui-widget-content ui-corner-all" onchange="document.getElementsByTagName('form')[0].submit()" >
			<#list allPageSize as item> 	
				 <option value="${item}"  <#if item == PAGE_SIZE> selected="selected" </#if> >${item}</option>
			</#list>
			</select>条
	</span>
</div>

 
 
 
 
 
 
 