
web.xml中
<filter>
	<filter-name>struts2</filter-name>
	<filter-class>org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter</filter-class> 
	<!-- 这里的配置可以覆盖 struts.properties => struts.xml => org/apache/struts2/default.properties 
        <init-param>
        	<param-name>struts.action.extension</param-name>
        	<param-value>ss2</param-value>
        </init-param>
    -->	
</filter>
<filter-mapping>
	<filter-name>struts2</filter-name>
	<url-pattern>/*</url-pattern>		*/
</filter-mapping>

<!DOCTYPE struts PUBLIC
	"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
	"http://struts.apache.org/dtds/struts-2.3.dtd">
<package name="xxxpattern" namespace="/xxx" extends="struts-default">
	
</package>	

写Action  继承自 com.opensymphony.xwork2.ActionSupport 继承自 com.opensymphony.xwork2.Action
		的execute()方法
		有getText("properties 文件中的KEY")
		String execute() {return ERROR, INPUT, LOGIN, NONE, SUCCESS}


===========浪曦视频
ActionSupport 的一个validate()方法子类去覆盖    addFieldError("属性"，"值")　<s:fieldError name="属性">

ognl包的 DefaultTypeConverter 实现了 TypeConverter 接口
	converterValue(Map context,Object value,Class toType) 
	{
		//当toType是String.class时,value对界面的字段时是String []类型的
	}
org.apache.struts2.util.StrutsTypeConverter 继承自 DefaultTypeConverter

<s:property value="password"  />
  
转换类型在的  同一个包中的建立 [Action类名]-conversion.properties文件
	对Action类的属性名转换 point=com.test.converter.PointConverter
	(<>范型)Element_selectedSkills＝java.lang.String　对属性selectedSkills类型List的中的元素类型
全局转换建立　xwork-conversion.properties根classes目录中,格式:  要转换全类名＝转换的全类名

表单采用point.x和point.y的方式(但是如果用javascript会有问题,可自定义id)，就不必使用转换器了


<constant name="struts.custom.i18n.resources" value="message"/>  国际化
//可从 org/apache/struts2/default.properites 中复制,也有struts.action.extension=action 配置
	org/apache/struts2/struts-message.properites有文件上传等的错误信息
native2ascii zh.txt zh.properties //可转整个文件
 
message.properties中写
xwork.default.invalid.fieldvalue={0} can not convert ,ERROR  	//是不能转换(如字串到数字)的错误消息,全局的,但{0}是表单名
//从com.opensymphony.xwork2.xwork-messsage.properties文件中复制

和Action类名 在同包下,建立 [Action类名].properties 部分国际化
invalid.fieldvalue.age=			//age 是属性


Action类中的validate[Action方法名],是对于  <action name="validateAction" class="struts2_validation.ValidateAction" method="test1"> 
validate 可修改名为validateExecute ,对应于execute 
1.validateXxx(),2.validate() 无论validateXxx是否成功,validate()总是会被调用

[Actioin名]-validation.xml
[Actioin名]-[xml文件中的actiion]-validation.xml  [根目录validators.xml 来自定义]

<s:actionError>  对应于 this.addActionError("test");

<!DOCTYPE validators PUBLIC
  		"-//Apache Struts//XWork Validator 1.0.3//EN"
  		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd"> 文件在xwork-core.jar中
<validators>
    <field name="username">
        <field-validator type="requiredstring">
            <message key="requiredstring"/>  //在com.opensymphony.xwork2.validator.validators中的default.xml
        </field-validator>
		  <field-validator type="stringlength">
			<param name="minLength">2</para>
			<message >must be moure ${minLength}</message>  
        </field-validator>
    </field>

<s:form theme="simple"> 表示可以自定义布局 ,并且不会输出过多的信息
<s:form validate="true"/>客户端验证,不可theme="simple"[会使用  Action-validation.xml文件生成JS来验证,布局很乱不推荐用,用多余的错误信息] 
用onsubmit="return xxx();" id的默认直为="表单名_字段名"，除非自定义ID

//顺序
1.[Action类名]-validation.xml
2.[Action类名]-[<action name=""的值]-validation.xml
3.[Action类名]的validate[Xxx] 方法
4.[Action类名]的validate方法 

 
ParentAction-validation.xml
ChildAction-validation.xml
ChildAction-[<action name=""的值]-validation.xml
子类覆盖父类的同名方法
子类的XML验证会覆盖父类XML验证的同一属性

<field-validtor short-ciruit="true">（短路）如果第一验证失败就不去验证第二个
 
getFieldErrors的Map中的值是一个ArrayList(),不可以向Map中加/改值,因为是新new出来的，不起做用的
先读XML文件，再validate()方法,如对同一个的属性做验证,会合并
 
 <!-- defaultStack ,struts-default 在struts2-core.jar/struts-default.xml中 -->
<interceptor name="" class="">     implements Interceptor
	<param name=""></param>会在init()方法前被赋值
</interceptor>

<interceptor-ref name="" > 可以对spring的进行拦截
	<param name=""></param>来改变初值
</interceptor-ref>


struts-default.xml中params 的interceptor用来实现setXXX,getXXX方法

Interceptor接口，AbstractInterceptor

MethodFilterInterceptor 对特定的方法来拦截
	参数includeMethods 多个方法用,分隔
	参数excludeMethods
	
	不可以用多个<param>否则后面的会覆盖前面的
	只要参数includeMethods中有就会被拦截,参数excludeMethods中再加无效(和linux DNS,apache同)

implements PreResultListener
{
	beforeResult(ActionInvocation invocation,String resultCode)//在Action执行后的，有了result,
}	
(在Interceptor 中的)ActionInvocation.addPreResultListener(impl);//要在invocation.invoke()之前注册监听

ActionContext context=invocation.getInvocationContext();
Map<String,Object> sessionMap=context.getSession();

<global-results>
	<result name="error" type="redirect">/error.jsp</result> <!--type="redirect"是在struts-default.xml中 定义的 ,默认dispatcher-->
</global-results>
 
<action name="upload" class="struts2_upload.MyUploadAction">
	<result name="success">ok.jsp </result>
	<result name="input">upload.jsp </result>
	<interceptor-ref name="fileUpload">
		<param name="maximumSize">2097152</param><!-- 2M -->
		<param name="allowedExtensions">ppt,pptx</param>
		<param name="allowedTypes">application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation</param>
		<!-- Tomcat的conf/web.xml有全部的mime-type -->
	</interceptor-ref>
	<interceptor-ref name="defaultStack" /><!-- 在放在fileUpload后面 -->
</action>
		

<s:feilderror/>, <s:actionerror />

org/apache/struts2/struts-message.properties中的
struts.messages.upload.error.SizeLimitExceededException=
struts.messages.error.content.type.not.allowed=
struts.messages.error.file.extension.not.allowed=

全局的根目录下的message.properties中
<constant name="struts.custom.i18n.resources" value="message"/>

表单同名的可以给List属性的

File photo;
String photoFileName;//注意格式,看源码
String photoContentType;
//---动态多文件方式
List<File> attachments;
List<String> attachmentsFileName;
List<String> attachmentsContentType;

<s:form enctype="multipart/form-data" method="post">
	<s:file name="photo" label="photo1"/>
	<s:file name="attachments" label="attachment1"/> <!-- 可使用JS动态增加 -->
 

文件下载时
<action name="download" class="struts2_download.DownloadAction">
	 <result type="stream" name="success"> <!-- type="stream"  redirect"是在struts-default.xml中 定义的 ,默认dispatcher-->
		<param name="contentType">application/octet-stream</param>
		<param name="contentDisposition">attachment;filename="${downloadFileName}"</param><!-- 对应于 String getDownloadFileName()方法 -->
		<param name="inputName">downloadFile</param><!-- 对应于InputStream getDownloadFile()方法-->
	</result>	
</action>
public class DownloadAction extends ActionSupport 
{
	String theName="中文名.txt";
	public InputStream getDownloadFile() throws Exception//名字对应于配置文件中 <param name="inputName">downloadFile</param>		
	{
		//InputStream input =ServletActionContext.getServletContext().getResourceAsStream("/tmp/xx");
		InputStream input =new FileInputStream("c:/temp/"+theName);
		return input;
	}
	public String getDownloadFileName()//对应于<param name="contentDisposition">attachment;filename="${downloadFileName}"</param>  
	{
		String res=new String(theName.getBytes(Charset.forName("UTF-8")),Charset.forName("iso8859-1"));//文件名有中文,对Tomcat默认设置,没有加filter编码 OK
//		String res=new String(theName.getBytes("UTF-8"),"iso8859-1");//必须try catche
		return res;
	}
	public String execute() throws Exception
	{
		return SUCCESS;
	}
}
---default.properties 有
struts.i18n.encoding=UTF-8  //解决乱码
struts.multipart.saveDir=	//上传临时目录
struts.multipart.maxsize 	//是单个文件上传的最大值
struts.action.extension=action 


取得Struts的constant配置    
DefaultSettings.get("struts.action.extension"); //可以是自己新加一些配置


struts2 对动态生成的文件做上传,是Action属性是List<File>,
List 和数组是同样的处理  List<File> file;
						  List<String>fileFileName;


如果使用了struts2就不能单独使用common-fileupload了,因为被struts 2拦截,处理过了


=====国际化 
<constant name="struts.custom.i18n.resources" value="basename"/>  是读src目录下的文件
<constant name="struts.i18n.encoding" value="gbk"/> 
 
<s:i18n name="basename"> 只对特指的文件去找，最好在根目录下，其它目录下 org.zh.message
	<s:text name="key">
		<s:param></s:param> 给{0}
	</s:text>
<s/i18n>
i18n的拦截器

<s:text name="key">

包级别的
	package_en_US.properties  (一定要这个名字)在一定的包下
	如果有和全局重复返回包级别的(会覆盖全局的 Map的key 不可重复的)
	
类级别的
	[类名]_en_US.properties  (RegisterAction_en_US.properties)



ActionSupport 有一个getText("key","default",String[] args)即Action,java代码中的国际化
this.addFieldError("username",  getText("canNotEmpty",new String[]{getText("username")}));
 
<s:property value="getText('username')"/> 	<br/>

<s:text name="canNotEmpty">
	<s:param value="%{getText('username')}"/>
</s:text>			<br/>

<s:textfield label="%{getText('username')}"/><br/>

<s:radio list="#{1:getText('male'),0:getText('female')}" name="sex" value="1" key="gender"/>

validation.xml国际化
XML 验证<message key="username"></message> 或者 <message>${getText("username")}</message>


国际化文件中可以使用${getText("key")}
username=username
canNotEmpty={0} can not empty
#canNotEmpty can not pass param {0} in XML validation
usernameCanNotEmpty=${getText("username")}  can not empty

<field name="username">
	<field-validator type="requiredstring">
		<param name="trim">true</param>
		<!-- 两者一样的
		<message key="usernameCanNotEmpty"></message> -->
		<message>${getText("usernameCanNotEmpty")}</message>
	</field-validator>
	<field-validator type="stringlength">
		<param name="minLength">6</param>
		<param name="maxLength">20</param>
		<message key="lengthMustBe"><!-- 不能传参????????? -->
			<param name="0">${getText("username")}</param>
			<param name="1">${minLength}</param>
			<param name="2">${maxLength}</param>
		</message>
	</field-validator>
</field>
=====
<include file=""/>
<bean
<package name="用来extends" namespace="/"
注namespace必须以"/"开头,如是"/xx",也要在url中加 "xx/y"或"/xx/y"
	<result 中路径可以使用相对的"..",也可是绝对的"/"

classpath根目录下的struts.properties可以覆盖struts.xml文件和default.properties

(最高的)web.xml中的->struts.properties -> struts.xml -> default.properties

1. web.xml  <param-name>  最高
2.struts.properties
3.struts.xml <constant> 
4.default.properties

#---dev 
struts.devMode = true
struts.i18n.reload=true
struts.configuration.xml.reload=true

会自动在classpath中查找文件,struts-default.xml,struts-plugin.xml,struts.xml  
struts2-spring-plugin-2.3.7.jar 文件的classpath中有struts-plugin.xml,会被自动查找

<package abstract="true">里面不可有<aciton>的配置

<action namespace="/hello" 要以/开头 <s:form action="/hello/point.action"> 要加"/"

如在/hello下没有指定Action会在默认空间去找Action

<include file=""/>


 
 extends ActionSupport implements ModleDriven<User> 接口，getModel() 方法中 return 的属性要是已经new 出来
//ModelDriven<T>在Action  自动调用getModel返回T <User>,页面中还是用username 会getModel().getUsername
<interceptor-ref name="modelDriven"/> 必须放在 staticParams 和 params 的前面

Action实现 Preparable 接口  prepare();  先做一些Action必要的初始化工作   <interceptor-ref name="prepare"/>

<s:fielderror>
	<s:param>username</s:param>
</s:fielderror>

<s:form theme="simple"> 是在 struts2-core.jar/template.simple/fielderror.ftl

-----token
token原理 在进入form之前(同一session两次请求产生相同的),在session中保存一个串,也显示在form的hidden中,在提交时如是比较值,
如相等是第一次提交,并把session中的删除,如session中空或不相同重复提交


<s:token name="helloToken"/> 放在<s:form>中来防止表单重复提交
会产生两个hidden
1.<input type="hidden" name="struts.token.name" value="helloToken"/>
2.<input type="hidden" name="helloToken" value="xyzaaaaaa"/>
第一次成功提交后,会把session的值删,如果不相同是已经提交

一定要加   <interceptor-ref name="token"/> (默认的是没有的),放 params 拦截器的后面 
还要加一个<result name="invalid.token">fail.jsp</result>  ,来显示重复提交时显示的错误页,错误信息用<s:actionerror/>
改显示的要国际化org.apaceh.struts2.struts-message.properties
	struts.messages.invalid.token=
	struts.internal.invalid.token=
	

ActionContext context = ActionContext.getContext();
context.put("myAttr", "myValue");//相当于request.setAttribute("myAttr", "myValue");

ActionContext.getContext().getValueStack()
HttpParameters paramsMap= ActionContext.getContext().getParameters();//参数

ActionContext.getContext().getSession();返回一个Map可单元测试

HttpServletRequest request = ServletActionContext.getRequest(); //得到原始的Request
HttpServletResponse response = ServletActionContext.getResponse(); 
HttpSession session = request.getSession();

ServletContextAware 接口
ServletRequestAware 接口 
ServletResponseAware 接口
request来得到Session

 
<s:form action="dynamic!method.action">  
	!前的dynamic对应的 <action name="dynamic" ,method是对应Action类中的方法名 (不是execute,不用配置method="")
	
<!-- jsp 可以放在/WEB-INF下, 如使用*,从1开始,要保证struts.enable.DynamicMethodInvocation = true-->
<action name="pattern_*" method="{1}" class="struts2_pattern.PatternAction">
	<result>/WEB-INF/pattern/{1}.jsp</result> <!-- jsp 可以放在/WEB-INF下 -->
</action>
 
<actioin name="*Login" calss="...." method="{1}"> 通配符,{1}对应第一个*,从1开始,
helloLogin.action可 会调用hello方法
 
<result type="dispatcher"> 定义中是<result-type name="dispatcher" class="..." default="true"/> 默认
		redirect
		chain
		freemarker
		redirectAction 
		stream
		xlst
		plainText
		httpheader 要加参数status属性(200，404) ，不用加页面 

 <result type="chain">
        <param name="actionName">dashboard</param>
        <param name="namespace">/secure</param>
        <param name="method">execute</param>
 </result>
 拦截器 chain：将前一个执行结束的Action属性设置到当前的Action中。
	它被用在ResultType为“chain”指定结果的Action中，
	该结果Action对象会从OgnlValueStack中获得前一个Action对应的属性，
	它实现Action链之间的数据传递；
 
<global-exception-mappings>
	<exception-mapping result="login" exception="java.lang.Exception"/>  如抛出指定异常会到指定的result 的视图
</global-exception-mappings>
##result 先在自己的Action是里找，找不到再到　global-results　里找
<global-results>
	<result name="error" type="redirect">/error.jsp</result>
</global-results>


页面中可以<s:property value="exception.message"/>
			<s:property value="exceptionStack"/> 
exception拦截器　

也可以用　${exception} 或者${exception.message} 


==================
//--得到struts.properties,<constant name="gloabName"中的值
//struts.xml中有<constant name="gloabName" value="我爱带呆3"></constant> 
Dispatcher dispatch = (Dispatcher)Dispatcher.getInstance();
Container container = dispatch.getContainer();
String global = container.getInstance(String.class, "gloabName");

Configuration cfg=container.getInstance(Configuration.class);
PackageConfig defaultPkg= cfg.getPackageConfig("default");
defaultPkg.getNamespace();
defaultPkg.getLocation();
defaultPkg.getInterceptorConfigs();//本包的
defaultPkg.getAllInterceptorConfigs();//有struts-default.xml中的
defaultPkg.getGlobalResultConfigs();
defaultPkg.getGlobalExceptionMappingConfigs();
 
//----
 <action>下如要<param>则这个Action必须实现接口Parameterizable接口
要有staticParams的拦截器
//----标签
Map reqMap=(Map)ActionContext.getContext().get("request");
List<String> hobbys=new ArrayList<String> ();
hobbys.add("蓝球");
hobbys.add("足球");
reqMap.put("allHobby", hobbys);

allHobby 有<br/>
<s:iterator value="#request['allHobby']" id="hobby">
	<s:property value="#hobby"/> <br/>
</s:iterator>

s:set测试 有<br/>
<s:set name="myAliase" value="#request['allHobby']" ></s:set>
<s:iterator value="#myAliase" id="hobby"><!-- 引用时,#别名 -->
	<s:property value="#hobby"/> <br/>
</s:iterator>

allProvinces 是List<Province>类型的
<s:select list="#request['allProvinces']" name="province" listKey="id" listValue="name" />

构造Map 
<s:set name="allCity" value="#{'shanghai':'上海', 'beijing':'北京'}" />
<s:select list="allCity" name="city" listKey="key" listValue="value" /> <br/>
<s:iterator value="allCity" id="city" status="s"> <!--s是IteratorStatus的实例,有index,count,odd,even,last,first -->
	<s:property  value="#s.index"/> 
	<s:if test="#s.odd==true">
		<span style="color:gray">
	</s:if>
	<s:else>
		<span style="color:blue">
	</s:else>
		<s:property value="#city.key"/> == <s:property value="#city.value"/></span> <br/>
</s:iterator>
 
<s:set name="age" value="29"/>
<s:if test="#age > 60}">老年人</s:if>
<s:elseif test="#age > 15">青年人</s:elseif>
<s:else>少年</s:else>

<s:generator separator="," val="'lisi,zhangsan,wangwu,zhaoliu'"> 
	<s:iterator> <!-- 使用s:generator的值 -->
		<s:property/><br/>
	</s:iterator>
</s:generator>

<s:url action="addStudent" id="good">
	<s:param name="student.name" value="'zhansan'"> </s:param>
	<s:param name="student.birthday" value="'2008-11-01'"> </s:param>
</s:url>
<s:a href="%{#good}" >struts2URL增加学生</s:a>


<s:debug/> 一个可以展开的DIV,可以显示StackContext 和ValueStack中的值


public class MyDecider implements SubsetIteratorFilter.Decider{
	public boolean decide(Object obj) throws Exception 
	{
		if(obj.toString().equals("22")) //22 不要加入
			return false;
		return true;
	}
}
<s:bean name="struts2_other.MyDecider" id="mydecider" />
<s:subset source="{22,11,33,55,44}" start="1" count="3" decider="mydecider">
	<s:iterator status="st">
	 <s:if test="#st.odd">ODD,</s:if>
	   <s:property/>  <br/>
	</s:iterator>
</s:subset>

<s:bean id="mycomparator" name="struts2_other.MyComparator" />
<s:sort source="#request.allProvinces"  comparator="#mycomparator" >
	<s:iterator>
		<s:property value="name"/> <br/>
	</s:iterator>
</s:sort>

<s:form>
	<!-- s:doubleselect 必须放在s:form中,是选择父,后子后做相应的变化
	doubleList属性中的top是指的list属性中的选择的对象,是JS效果没有请求 -->
	<s:doubleselect  name="provinceId"	list="provinceList" 		listKey="id" 			listValue="name"
   					doubleName="cityId" doubleList="cityMap.get(top.id)" doubleListKey="id" doubleListValue="name"/>
</s:form>

两边的框,可左移,右移等
<s:optiontransferselect name="my_left" list="{'aa','bb','cc'}"  doubleName="my_right"  doubleList="{'11','22','33'}"/>	

可做include
<div>
  <s:action name="pattern_select"  namespace="/pattern"  executeResult="true" />  
</div>

--------myappend<br/>
	<s:append id="myappend">
		<s:param value="{11,22}"/>
		<s:param value="{111,222}"/>
	</s:append>
	<s:iterator value="#myappend">
		   <s:property/>  <br/>
	</s:iterator>
--------mymerge <br/>
	<s:merge id="mymerge"> <!-- 会排序 -->
		<s:param value="{11,22}"/>
		<s:param value="{111,222}"/>
	</s:merge>
	<s:iterator value="#mymerge">
		   <s:property/>  <br/>
	</s:iterator>
	
	<s:set name="now" value="new java.util.Date()"/>
	<s:date name="now" format="yyyy-MM-dd HH:mm:ss" />
	
	<s:push value="first_name">诸葛</s:push> <!-- value的值是在ValueStatck中已有的,即是Action中有的属性名 -->
//----
 
#的三种应用
1. # 相当于ActionContext.getContext() 有属性parameters,request,session,application,attr
	attr 用于按request > session > application顺序访问其属性（attribute） #attr.userName
	#request.userName相当于request.getAttribute("userName") 

<s:property value="#parameters.userName" />

2.构造Map
<s:set name="foobar" value="#{'foo1':'bar1', 'foo2':'bar2'}" />


3.投影选择
投影（projection）就是选出集合中每个元素的相同属性组成新的集合，
语法为 collection.{XXX}
	group.userList.{username}
选择（selection)就是过滤满足selection 条件的集合元素，
? 选择满足条件的所有元素
^ 选择满足条件的第一个元素
$ 选择满足条件的最后一个元素

group.userList.{? #this.name != null}

group.userList.size().(#this+1).toString()
在这个例子中，#this其实就是group.userList.size()的计算结构。


ActionContext.getContext().getValueStack();
ActionContext包括StackContext和ValueStack
	StackContext中有session,request,application,parameters,attr,
	ValueStack ,每一次Action请求创建对应的	ValueStack,把所有的Action 的属性存放在Valuestack中 ,再传到页面
	
	<s:property value="uid" />到ValueStack中查找
	<s:property value="#uid" />到StackContext中查找
	
	<s:property value="#request.obj.sayHello('jack')" /> 调用方法
	<s:property value="@request.obj@SayHello('jack')" /> 调用静态方法
	
	struts2的标签中<s: 不能使用EL表达式$()
 
response.getBufferSize()
文件上传时File对象转换失败,Invalid field value for xxx(在com/opensymphony/xwork2/xwork-messages.properties文件中)


<s:property value="message" escape="false"/>

<result name="cancel" type="redirectAction">Welcome</result>

<package name="example" namespace="/example" ..在ULR要加/exapmle

  
如果RegisterAction中的是一个模型User   user
Regtister-register-validation.xml
	<field name =user>
		<field-validator type="visitor">
			<param name="context">userContext</param>   文件名中的部分User-userContext-validation.xml 中加入各属性的验证
			<param name="appendPrefix">true</param> 是否在错误消息前加,前缀 (对多个模型)
			<message>用户的:</message>前缀消息是什么
 测试visitor是OK的


	
 转换器校验    conversion
<param name="repopulateField">true</param>转换发生错误是否显示原来输入的信息

fieldexpression  传 expression 值 pass==passwd
regex		 传expression值 <![CDDATA[  ( \w{4-25} ) ]]>


#parameters['name'] 或者#parameters.name  同request.getParameter
#request['name']   或者 #request.name
#session['name']   或者 #session.name
#application['name']   或者 #application.name
#attr['name'] #attr.name   pageContext.getAttribute();

<s:property value="#session.AutoId.autoId"/> 和<s:property value="#session['AutoId']['autoId']"/>相同结果AutoId是session 中的一个对象


{e1,e1,e3}  生成List
#{key1:value1,key2:value2} 来生成Map
mod ,in 或者not in 来判断
	<s:if test="'java' in {'java',jsp}" >
? 所有符合条件的
^第一个符合条件
$最后一个符合条件的


<url-pattern>*.action</url-pattern>   只能配置*.action 才行,*.htm *.do 都不行的
			/*     //  */   对.jsp 中对有<s: OK
				   //	如是 *.action   对.jsp 中对有<s: 报  The Struts dispatcher cannot be found,一定要Action跳过才行

=======Freemarker 视图
Freemarker 不能在群集上面发布应用

对Freemarker ftl文件编码用UTF-8
HTML中要有  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<constant name="struts.i18n.encoding" value="UTF-8"/> 

<result type="freemarker" name="success">
    <param name="location">/pages/Person/view.ftl</param>
    <param name="contentType"> text/html;charset=UTF-8</param> 
</result>

classpath上放置一个文件 freemarker.properties ,加入
default_encoding=UTF-8
locale=zh_CN 

//---在FreeMarker中使用Struts2标签,JSTL标签
web.xml
<servlet>
  <servlet-name>JSPSupportServlet</servlet-name>
  <servlet-class>org.apache.struts2.views.JspSupportServlet</servlet-class>
  <load-on-startup>0</load-on-startup>
</servlet> 
	
<#--要把struts2-core.jar/META-INF/struts-tags.tld 中复制到WEB-INF中-->
<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]> 

<#-- 要把standard.jar/META-INF/xxx.tld 中复制到WEB-INF中 -->
<#assign c=JspTaglibs["/WEB-INF/tld/c.tld"] />
<#assign fn=JspTaglibs["/WEB-INF/tld/fn.tld"] />
<#assign fmt=JspTaglibs["/WEB-INF/tld/fmt.tld"] />
<#assign sql=JspTaglibs["/WEB-INF/tld/sql.tld"] />
<#assign x=JspTaglibs["/WEB-INF/tld/x.tld"] />


国际化的username结果是:
<@fmt.setBundle basename="message"/>
<@fmt.setLocale value="zh_CN"/>
<@fmt.message key="username" />  <br/>

所有的actionmessage <br/>
<@s.actionmessage/>

所有的actionerror <br/>
<@s.actionerror/>

所有的fielderror <br/>
<@s.fielderror/>
	
<@s.form action="freemarkerSubmitForm.action" mehtod="post" theme="simple">
   Fullname:<@s.textfield  name="user.fullname"/> <br/>
   username:<@s.textfield   name="user.username"/> <br/>
   password:<@s.password    name="user.password"/> <br/>
   age:		<@s.textfield   name="user.age"/> <br/>
   joinDate:<@s.textfield   name="user.joinDate"/> <br/>
   
Struts2方式遍历 <br/>
  <@s.iterator value="#request['allProvinces']" id="province">
 	 <@s.property value="#province.name"/>    <br/>
 </@s.iterator>   
 所在省:Struts2方式选择<br/>
  <@s.select list="#request['allProvinces']" name="user.province" listKey="id" listValue="name"/> <br/>
    所在省:Freemarker方式
   <select name="user.province" >
   	 <#if Request.allProvinces?exists>
		<#list allProvinces as province > 
			<option value="${province_index}">${province.name}</option><!-- 是  _index -->
		</#list> 
	</#if>
   </select> <br/>
   photo 1:<@s.file  name="user.photos"/> <br/>
   photo 2:<@s.file  name="user.photos"/> <br/>
    <@s.token name="myToken"/>
    <@s.submit value="提交" /> <br/>
</@s.form>
 
 
 <#if Request.user.joinDate?exists>
    ${user.joinDate?string("yyyy-MM-dd HH:mm:ss")}  
</#if>
 
 

<#if Application.atributeName?exists>   ############Session/Request/Parameter也可以用request
	${Application.attributeName}
</#if>

<@s.property value="%{#application.attributeName}"/>###request /request/parameters

${action.username }action 代表过去的Action 对象

${base}  请求的上下文(/项目名称)

${stack} 表示OgnlValueStack对象

========================jfreechart plugin
jcommon-1.0.2.jar
jfreechart-1.0.9.jar
 与struts2集成加入 struts2-jfreechart-plugin-2.3.7.jar 里面有struts-plugin.xml

<s:checkbox lableposition="top/left"/>

<s:checkboxlist list="#{'computer':'计算机','me':'我'}" name="" lable="xx" lableposition="left"/>
//显示在一行上,无法自定义



decoupling  解耦

Action 中加属性 
JFreeChart chart;//属性名一定要chart
JFreeChart getChart()//硬编码的,会框架调用对应<result type="chart",可以使用页面传入的数据,要返回一个画好JFreeChart对象
  
如果一个package使用多个plug-in,<package extends="struts-default,jfreechart-default">

<package extends ="jfreechart-default">
<result type="chart">
	<param name="height">300</param>
	<param name="height">300</param>
</result>

========================Spring集成
struts2-spring-plugin-2.3.7.jar

struts-plugin.xml 中有 struts.objectFactory = spring

default.properties 中有 struts.objectFactory.spring.autoWire= name  
	表示Action 的属性名 和 spring 的id 是按名称自动注入的(如xxService)

<package  extends="struts-default,spring-default"> <!-- 多继承 -->
	<action class="springId"> 可是spring中ID,也可以是全类名

<beans>
	<bean id="springId" scope ="prototype"> 因与struts2集成,要每一次请求生成一个新实例,要加scope ="prototype"
 
struts.xml可以覆盖struts-plugin.xml
 
========================Struts 2 Portlet
JSR 168 (Portlet 1.0)
JSR 286 (Portlet 2.0)
Struts2 中有 PortletResultHelperJSR286 ,PortletResultHelperJSR168类

========================Struts2 的Annotation标注

