MessageResources =Action.getResource(request)
 getMessage(java.lang.String key) 



<action path="/action1"
	type="org.easyzone.web.MyDispatchAction" parameter="method"/>	
在写多个DispatchAction 对应一个path时 paramter=""的值必须都 一样

MyDispatchAction类不要重写execute方法
继承看ValidatorActionForm  (对就action 的path),ValidatorForm(对应action的name或者是form-bean的name)不要重写 validate方法 

<jsp:inclue/> <%@page include%>
web.xml
config用，，，，
DispachAction param=""

MappingDispatchAction 传递的方法名parameter ="方法名"  <a href="">不用加？,要多个Action
ActionForward(mapping.getInput()));   getInput返回String
ActionForward getInputForward() 
ForwardAction 
IncludeAction 


LookupDispatchAction    一个表有多个submit同名按钮   ---要继续
		只有一个abstract 方法protected abstract  java.util.Map getKeyMethodMap() 
 


SwitchAction


request.removeAttribute(mapping.getAttribute());
删除request范围的form-bean的属性名字
 
/org/apache/struts/validator/validator-rules.xm 
 validator框架实现多表单验证
 	struts-config.xml    中的Action 为  scope="request" validate="false"
		在自定的Acation 中用转换为ValidatorForm 用validate(mapping,request)来验证返回的ActionMessages
		在第一个表单中只多一个page的hidden，在第二个表单中的有第一个表单中的所有的hidden
		两表单submit同一个Action 
		
	在validation.xml  中为第一个 field 加上一个page 属性

 	validaton.xml中的<field><arg position="0" Key="xxx" resource="true" name="required" bundle=""/>
 		resource="false"表示不从资源文件中读，bundle=""的值是在struts-config.xml为<message-resources parameter="/config/Resources" kye="global"/>中的key的值
 				
 客户端验证在<html:form中加上	onsubmit="return validateRegisterForm(this)																							
 在<html:form>外部加上<html:javascript formName="registerForm" page="1"/> 	
 
 (registerForm 是<form-bean name=xx>的值(validation.xml中)  用ValidatorForm时)
 如是ValiatorActionForm 在validation.xml要是为path="/xx"的值



 		出错信息同样用<html:errors property/>来显示																									
 																											
多模型时，在web.xml多加   
<init-param>
  <param-name>config/模型名</param-name>//模型名（my），对应的根目录下的同名目录（my）,下面的目录可与这个不一样
  <param-value>/WEB-INF/配置文件的目录struts-config.xml</param-value>
</init-param>
<html:link module="/my" page="xx.jsp"/>或者 action="/zz.do"
<action path="/myswitch" type="org.apache.struts.actions.SwitchAction">
<html:link  action="myswitch?prefix=/m1&page=/test1Action.do">test</html:link> prefix=&..prefix=空是默认的

 
DaynActionForm 可以在<form-bean 下加<form-property name="" type=""			/>是path
<html:errors property="xx"/> xx是在ActionForm中的errors.add("xx",new ActionMessage("yy")); 																						
							yy是资源文件中的键	<message-resources parameter="message.message"/>有点来分目录不加properties


struts 处理日期: ActionForm 的构造中加入
	DateConvert implements org.apache.commons.beanutils.Converter;
	ConvertUtils.register(new DateConvert(),java.util.Date.class);

<exception 
	key=""
	path=""
	scope=""
	type=""

/>





=====================
validation.xml
<form-validation language="zh">
	<formset>
		<form name="whenForm">
					<field property="firstName" ></field>
					<field property="lastName" depends="validwhen">
						<var>
							<var-name>test</var-name>
							<var-value>((firstName==null) or (*this*!=null))</var-value>
						</var>
					</field>
					<field property="min" depends="short"></field>
					<field property="max" depends="validwhen,short">
					<var>
							<var-name>test</var-name>
							<var-value>(*this* &gt; min)</var-value>
						</var>
					</field>
					
		</form>

		<field property="lastName" page="1" depends="required">
				<arg position="0" key="lastname"  resource="true" name="required"  bundle="my"/>无name属性对depends的所有的{0} 都有效
		</field>
		
		###my是在struts-config.xml中的	<message-resources parameter="message/my" key="my"></message-resources>
									Action中的	validate默认是"true"
		
		
		<field property="age" page="1" depends="required,intRange">
				<arg position="0" key="${var:min}"  resource="true" name="required"  bundle="my"/>引用下面的min
				<var>
					<var-name>min</var-name>
					<var-value>20</var-value>
				</var>

		</field>

ValidatorForm   关联form-bean 的   name
ValidatorActionForm关联 form-bean 的path属性(都要加/)   一个Form给了两个Action(有不同的验证,)

<msg name="mask" 为mask指定错误时的信息来替换默认的信息(在/org/apache/struts/validator/validator-reules.xml中注释有的)

resource  默认是true(arg,msg中)
对于ValidatorForm,要在
	<html:javascript formName="userForm"/> ###userForm   validation.xml中和form-beans 的配置
	<html:form action="/addUser" onsubmit="return validateUserForm(this)">
	

<msg>不可用　${var:min}的方式，只对<arg
depends ="minlength,maxlength,mask,intRange"
<var>
	<var-name>minlength</var-name>  maxlength,mask(^\w(+)$) ,min & max (mask,文档中说的)


"request".equals(mapping.getScope())


==================

	<plug-in className="org.apache.struts.tiles.TilesPlugin">
	 <set-property property="definitions-config" value="/WEB-INF/tiles-defs.xml"/>
	 <set-property property="definitions-debug" value="2"/>
	</plug-in>



	<plug-in className="net.sf.navigator.menu.MenuPlugIn">
		<set-property property="menuConfig"
			value="/WEB-INF/menu-config.xml" />
	</plug-in>	



struts 
<logic:match parameter="name"  value="xyz" location="1"></logic:match>
检查名为name的请求参数是否是xyz的子串,但子串必须从xyz的索引的第一位
<logic:present role (user)>

<html:rewrite/>创建没有锚点标记的URI


<logic:iterate id="" collection ="" type=""
	offse="开始的位置" length="结束的长度"></logic:iterate

<logic:forward name =""/> 只一个属性

Action 类的saveErrors(request(session),AcitonMessages)//有's' 
ActionErrors对象的 .add("name",	new ActionMessage(" appllicaionResource中上的key"));
	
java.util.Locale.US 和java.util.ResourceBundle  与<bean:message bundle捆>
javax.text.MessageFormat

Locale userLocale = (Locale) request.getSession().getAttribute("org.apache.struts.action.LOCALE");
用户当前选择的语言




org.apache.struts.upload.FormFile 类型做为ActionForm的属性,可以直接拿到用户上传的文件 
inputFile.getInputStream();
inputFile.getContentType();
String ContentType=inputFile.getContentType();
//text/plain,application/vnd.ms-excel(csv,xls),application/octet-stream(.xlsx)

<html:options collection="allMan"  labelProperty="label"  property="value" />
manList.add( new org.apache.struts.util.LabelValueBean( "namexx","valuexx"));



<global-exceptions>
		<!--  404 ,500 -->
		<exception  type="java.lang.Exception"  path="/error.jsp" key="xx"/>
</global-exceptions>

默认是org/apache/struts/action/ActionResources_zh.properties

