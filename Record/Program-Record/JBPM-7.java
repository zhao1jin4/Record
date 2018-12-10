http://www.omg.org/spec/BPMN/2.0/
Business Process Model and Notation (BPMN) 		Version 2.0

有 Docker images

----------------------jbpm-server-7.14.0  只有一个独立的包了，集成wildfly-14 和示例
	文档说可以做war包部署,SpringBoot
	主页有引用google,所以报错
	
----------------------JBPM-7.7 是有单独的bin包的
ORM 使用 Hibernate JPA 5.1 , 引擎核心 Drool

drool	流口水
guvnor	雇主,老板
http://www.jbpm.org/learn/documentation.html
https://docs.jboss.org/jbpm/release/7.5.0.Final/jbpm-docs/html_single/


下载 drools-distribution-7.5.0.Final.zip 中有示例
eclipse->preference ->Drools ->install Drools Runtimes-> 选择 drools-distribution-7.5.0.Final\binaries

jbpm-7.5.0.Final-examples.zip
jbpm-installer\sample\evaluation 的示例可以导入eclipse中去


lib/jboss-wildfly-10.1.0.Final.zip	 (JBoss AS 的新名字)
lib/org.drools.updatesite-7.5.0.Final.zip  可用于eclipse-4.7.2 (取消JBoss Runtime Drools Detector Developer 依赖org.jboss.tools.common.core 找不到 )
lib/jbpm-7.5.0.Final-bin.zip 解压 ,eclipse->preference ->jBPM ->installed jBPM Runtimes->Add... -> Create a new jBPM Runtime 目录指向即可

右击项目->Properties->jBPM->选中Enable project specific settings ,在jBPM Runtime 中选择工作区建立的
	引用库右击项目->Properties->drools>选中Enable project specific settings ,
或者新建drools项目时选择从网络下载示例

 
 正常会多 jBPM Library 或者  Drools Library
  eclipse 项目的.classpath文件中有	
		<classpathentry kind="con" path="DROOLS/Drools"/>
		<classpathentry kind="con" path="JBPM/jbpm"/>
	.project 文件中有  
		<buildCommand>
			<name>org.drools.eclipse.jbpmbuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.drools.eclipse.droolsbuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		
		
ant install.demo.noeclipse
ant start.demo.noeclipse     (可以用JDK8)
ant stop.demo
ant clean.demo  #clean 要再install

http://localhost:8080/jbpm-console/	 进入KIE workbench	 
用户名/密码  用 "krisv" / "krisv"  是从 auth\roles.properties   复制到wildfly\standalone\configuration
standalone.xml  <module-option name="rolesProperties"

http://localhost:8080/jbpm-casemgmt  新的
用户名/密码  用 "krisv" / "krisv" 




---修改默认数据库H2 到  MySQL
create database jbpm default character set utf8;
GRANT all ON jbpm.*  TO jbpm@localhost IDENTIFIED BY 'jbpm';
GRANT all ON jbpm.*  TO jbpm@'%' IDENTIFIED BY 'jbpm';

db/driver 放 mysql-connector-java-5.1.21-bin.jar
修改build.properties ,注释H2,打开MySQL部分
	db.name=mysql
	db.driver.module.prefix=com/mysql
	db.driver.jar.name=mysql-connector-java-5.1.21-bin.jar  为正确.jar
  
db/mysql_module.xml 修改正常.jar包  path="mysql-connector-java-5.1.21-bin.jar"
  
db/jbpm-persistence-JPA2.xml　	被复制到 jbpm-console-war/WEB-INF/classes/META-INF/persistence.xml (修改 MySQLDialect )

standalone-full.xml文件中的的 jbpmDS 数据源 , com.mysql.jdbc.jdbc2.optional.MysqlXADataSource ,示例
	<datasources>中修改jbpmDS
		<datasource jta="true" jndi-name="java:jboss/datasources/jbpmDS" pool-name="MySQLDS" enabled="true" use-java-context="true" use-ccm="true">
			<connection-url>jdbc:mysql://localhost:3306/jbpm</connection-url>
			<driver>mysql</driver>
			<security>
			   <user-name>jbpm</user-name>
			   <password>jbpm</password>
			</security>
		</datasource>
		
	<drivers>中加
		<driver name="mysql" module="com.mysql">
			<xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
		</driver>
	
ant stop.demo
ant clean.demo
ant install.demo.noeclipse
ant start.demo.noeclipse 
----- 

---userguide 视频
jbpm-console  中 ->Project Authoring->二级下拉 jbpm-playground ->三级下拉 Evulation->展开 Business Processes->evaluation 打开流程设计器 查看
	展开Form Definitions->PerformanceEvaluation-taskform  查看
	Tools菜单->project Editor->Build & Deploy->提示保存到项目选择NO->提示successful ,顶级菜单 depoly->deployment但看
	顶级菜单 Process Management->Process Definitions对Evalution进行启动->显示form,名字要为krisv OK ->在侧面板view->process model  杳看状态
	顶级菜单 Task->task list->启动Self Evalution->再点complete->出form输入complete
	顶级菜单 Process Management->Process Instance-> 查看BPMN图
	顶级菜单 Dashboard->Process and TaskBoard->
	
	
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

extends JbpmJUnitBaseTestCase 
{
	RuntimeManager manager = createRuntimeManager("Evaluation.bpmn");
	RuntimeEngine engine = getRuntimeEngine(null);
	KieSession ksession = engine.getKieSession();
	KieRuntimeLogger log = KieServices.Factory.get().getLoggers().newThreadedFileLogger(ksession, "test", 1000);
	TaskService taskService = engine.getTaskService();
	// start a new process instance
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("employee", "krisv");//.bpmn文件中　#{employee},Action脚本中可以使用变量名employee
	params.put("reason", "Yearly performance evaluation");//Parameter Mapping中
	ProcessInstance processInstance = 
	ksession.startProcess("com.sample.evaluation", params); //对应.bpmn文件中 <process id="com.sample.evaluation" 
	
	// complete Self Evaluation
	List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("krisv", "en-UK");
	TaskSummary task = tasks.get(0);
System.out.println("'krisv' completing task " + task.getName() + ": " + task.getDescription());
	taskService.start(task.getId(), "krisv");
	Map<String, Object> results = new HashMap<String, Object>();
	results.put("performance", "exceeding");//Result Mapping中
	taskService.complete(task.getId(), "krisv", results);
	
	// john from HR
	tasks = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
	assertEquals(1, tasks.size());
	task = tasks.get(0);
System.out.println("'john' completing task " + task.getName() + ": " + task.getDescription());
	taskService.claim(task.getId(), "john");
	taskService.start(task.getId(), "john");
	results = new HashMap<String, Object>();
	results.put("performance", "acceptable");
	taskService.complete(task.getId(), "john", results);
		
	assertProcessInstanceCompleted(processInstance.getId(), ksession);
	log.close();
	manager.disposeRuntimeEngine(engine);
	manager.close();
}


使用hornetq(异步消息,支持cluster,JMS,像ActiveMQ)

request/ProcessTest.java
private static KieSession getKieSession() throws Exception 
{
	RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newEmptyBuilder()
		.addAsset(KieServices.Factory.get().getResources().newClassPathResource("request/requestHandling.bpmn"), ResourceType.BPMN2)
		.addAsset(KieServices.Factory.get().getResources().newClassPathResource("request/validation.drl"), ResourceType.DRL)
		.get();
	return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment).getRuntimeEngine(null).getKieSession();
}


================Drools
DroolsTest.java

KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl" , DroolsTest.class), ResourceType.DRL);
KnowledgeBuilderErrors errors = kbuilder.getErrors();
if (errors.size() > 0) {
	for (KnowledgeBuilderError error: errors) {
		System.err.println(error);
	}
	throw new IllegalArgumentException("Could not parse knowledge.");
}
KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test_123");
 
Message message = new Message();//自己的类
message.setMessage("Hello World_123");
message.setStatus(Message.HELLO);
ksession.insert(message);
ksession.fireAllRules();
logger.close();


-- Sample.drl 文件
package new_project_code
 
import new_project_code.DroolsTest.Message;
 
rule "Hello World_abc"
    when
        $message : Message($status : status == Message.HELLO)
    then
        System.out.println( "OK  "  + $message);
       
end

-- 新建项目 示例 
 
rule "Hello World"
    when
        m : Message( status == Message.HELLO, myMessage : message )
    then
        System.out.println( myMessage );
        m.setMessage( "Goodbye cruel world_in drl" );
        m.setStatus( Message.GOODBYE );
        update( m );//会再次触发符合条件的的规则
end

rule "GoodBye"
    when
        Message( status == Message.GOODBYE, myMessage : message )
    then
        System.out.println( myMessage );
end


META-INF 目录下的 kmodule.xml
<?xml version="1.0" encoding="UTF-8"?>
<kmodule xmlns="http://jboss.org/kie/6.0.0/kmodule">
    <kbase name="rules" packages="rules">  <!-- packages值是目录名, rules目录找drl文件--> 
        <ksession name="ksession-rules"/>
    </kbase>
    <kbase name="process" packages="process">
        <ksession name="ksession-process"/>
    </kbase>
</kmodule>
  KieServices ks = KieServices.Factory.get();
KieContainer kContainer = ks.getKieClasspathContainer();
KieSession kSession = kContainer.newKieSession("ksession-rules");//对应kmodule.xml中的kession
//--读drl 或者 xls文件 
Message message = new Message();
message.setMessage("Hello World");
message.setStatus(Message.HELLO);
kSession.insert(message);
kSession.fireAllRules();
//--读bpmn 
 kSession.startProcess("com.sample.bpmn.hello");
 
 
 
  
import my_code.Message;
import java.util.List;
 
//salience 值越大优先级越高，若不设置，则规则随机执行】
rule "salience1"
	 salience 1
     when
     	eval(true)
     then
		System.out.println( kcontext.getRule().getName() );//salience1
		System.out.println("kcontext class:"+ kcontext.getClass().getName() );//org.drools.core.base.DefaultKnowledgeHelper
end


rule "salience2"
	 salience 2
     when
     	//eval(true)  //不写任何condition，，默认为true
     then
        System.out.println( kcontext.getRule().getName() );
end



//【默认为false】
rule "no-loop"
	no-loop true 
    when
    	$message : Message()
    then
    	update($message);	//【检查fact更新引起的引擎是否再次启动检查规则】
		System.out.println( "no-loop 测试成功" );
end

 
 
//【系统时间>= date-effective才会触发】
rule "date-effective"
date-effective "17-Oct-2016"    //"17-Aug-2016"
	when 
		eval(true)
    then
		System.out.println( "date-effective 测试成功" );
end




//【系统时间<= date-expires才会触发】 【"expires":"到期，失效"】
rule "date-expires"	
date-expires "18-Oct-2016"
	when 
		eval(true)
    then
		System.out.println( "date-expires 测试成功" );
end




//【默认值为true，设置为false则表示禁用该规则】
rule "enabled"
	enabled true
	when 
		eval(true)
    then
		System.out.println( "enabled 测试成功" );
end
 

rule "activation-group1"
	activation-group 'test' //同一组 只有一个会被执行 start
	 
	when 
    then
		System.out.println( "activation-group-1" );
end

rule "activation-group2"
	activation-group 'test'
	salience 5
	when 
    then
		System.out.println( "activation-group-2" );
end


			
rule "agenda-group1"
	agenda-group 'agenda-group1'
	auto-focus false    //需要得到focus才能执行,默认false
	when 
    then
		System.out.println( "agenda-group1" );
end

rule "agenda-group2"
	agenda-group 'agenda-group2'
	auto-focus true
	when 
    then
		System.out.println( "agenda-group2" );
end


--java 代码
kSession.getAgenda().getAgendaGroup("agenda-group1").setFocus();
kSession.getAgenda().getAgendaGroup("agenda-group2").setFocus();
 

rule "lock-on-active"
	agenda-group 'agenda-group'
	lock-on-active true   //增强版的no-loop,用在ruleflow-group或agenda-group属性
	when 
		$message : Message()
    then
    	update($message);
		System.out.println( "lock-on-active 测试成功" );
end




rule "dialect1"
	dialect 'mvel'
	when 
		$message : Message()
    then
		List list = ["001", "002", "003"];
		System.out.println("dialect= mvel," +  $message.message + ";" + list);
end


rule "dialect2"
	dialect 'java'
	when 
		$message : Message()
    then
		System.out.println("dialect= java," +  $message.getMessage());
end
 

 
 
 
 -- 示例
global java.util.List list; //对应于.java中 kSession.setGlobal("list", listData);
global my_code.header.GlobalService service;
 
rule "global"
    when
    	$message : Message(this.message == service.name())//可用this
    then
		list.add("helloworld");
		list.add(service.name());
end

---- java 代码 
List listData = new ArrayList();
kSession.setGlobal("list", listData);//第一个参数会传给 .drl global的定义,fireRule后这值会配对的关联修改
//设置服务
GlobalService service = new GlobalService();
kSession.setGlobal("service", service);

kSession.fireAllRules();
System.out.println("global list=：" + listData);
listData = (List)kSession.getGlobal("list");
System.out.println("全局结果：" + listData);


function void sayHello() {
	System.out.println("hello everyone");
}

rule "function"
    when
    	eval(true)
    then
		sayHello();
end

 
 
 
query "queryDavid" () 
	msg:Message(message == 'david') //java代码中读msg做key,message是类的成员
end

query "queryDavid2" (String $attr) 
	message:Message(message == $attr)
end

---- java 代码 
Message message2 = new Message();
message2.setMessage("david2");
kSession.insert(message2);
  
QueryResults results = kSession.getQueryResults("queryDavid");
for(QueryResultsRow qr : results) {
	Message msg = (Message)qr.get("msg");//参数名对应drl的:前部分
	System.out.println("queryDavid = " + msg.getMessage());
}
QueryResults results2 = kSession.getQueryResults("queryDavid2", "david2");
for(QueryResultsRow qr : results2) {
	Message msg = (Message)qr.get("message");
	System.out.println("queryDavid2 = " + msg.getMessage());
}




declare Address
	city: String
	addressName: String
end



declare User
	@author(davidOne)
	@dateOfCreation(01-Feb-2009)
	username: String @maxLength(30)  //maxLength无用???
	userid: String @key
	birthday : java.util.Date
end

rule "declare"
    when
    	Address(city == 'wuhan')
    then
	 	Address address1 = new Address();
		address1.setCity("shenzhen");		
		System.out.println(address1.getCity());
		
		User user = new User();
		user.setUsername("davi22222222222222222222222222ddddddddddddddddddddddddddddddddddddddddddddddddddddddd22222d");
		user.setUserid("1");
		user.setBirthday(new java.util.Date());
		System.out.println(user);
end
---- java 代码 
ClassDefinition addressType = (ClassDefinition) kContainer.getKieBase("header").getFactType("com.sample.header", "Address");//kmodule配置base名可选
//ClassDefinition addressType = (ClassDefinition) kSession.getKieBase().getFactType("com.sample.header", "Address");//drl package名,declare名
Object obj = addressType.newInstance();
addressType.set( obj,"city","wuhan" );
kSession.insert(obj); 
String city = (String)addressType.get( obj,"city" );
System.out.println("获取子定义的类属性值：" + city);





//条件元素  and
rule "and element"
	when
		$customer : Customer(city == '北京') and $account : Account(name == '哈尔滨银行')
	then
		System.out.println("and element 测试成功");
end



//条件元素 or	【【then会执行两遍】】
rule "or element"
	when
		Customer( city == '上海') or Account(name == '浦发银行')
	then
		System.out.println("or element 测试成功");
end



//条件元素 exists  【【同时触发上面 or element的规则】】
rule "exists element"
	when
		exists Customer()  
	then
		System.out.println("exists element:存在 customer 类型");
end



//条件元素 not
rule "not element"
	when
		not Customer()  
	then
		System.out.println("not element:不存在 customer 类型");
end


 
rule "from element"
	when
		$customer : Customer()  
		Account(name == 'a2') from $customer.accounts  //from后是一个List类型的，匹配中所有的条件
	then
		System.out.println("from element 测试成功");
end



//条件元素 collect
rule "collect element"
	when
	    $customer : Customer()
	    $accounts : ArrayList( size >= 3 ) from collect( Account(status == 'Y') )
	then
   	    System.out.println("collect element 测试成功");
end



//条件元素 accumulate
rule "accumulate element"
	when
	    $total : Number( intValue > 400 )  from accumulate( Account( $num : num ),sum( $num ) )
	then
	    System.out.println("accumulate element 测试成功     "+$total);
end


//约束连接    &&/||
rule "&&/|| 约束"
	when
		$customer : Customer(age > 20 || (gender == 'male' && city == '深圳'))
	then
		//若符合年龄大于20 或者（性别为男，城市为深圳的就打印客户姓名）
		System.out.println("&&/| 约束测试成功");
end




//约束操作符   Contains
rule "比较操作符-Contains"
	when
		$account : Account()
		$customer : Customer(name=='张三' && accounts contains $account)
	then
		System.out.println("比较操作符-Contains 测试成功");
end



//约束操作符    not contains
rule "比较操作符- not contains"
	when
		$account : Account()
		Customer(name=='张三'  && accounts not contains $account)
	then
		System.out.println("比较操作符-not contains 测试成功");
end




//约束操作符    memberOf
rule "比较操作符- memberOf"
	when
		$customer : Customer()
		Account(this memberOf $customer.getAccounts())  
	then
		System.out.println("比较操作符-memberOf 测试成功");
end



//约束操作符    not memberOf
rule "比较操作符- not memberOf"
	when
		$customer : Customer()
		Account(this not memberOf $customer.getAccounts())  
	then
		System.out.println("比较操作符-not memberOf 测试成功");
end



//约束操作符    matches
rule "比较操作符- matches"
	when
		$customer : Customer(name matches "matches.*"  )
	then
		System.out.println("比较操作符- matches 测试成功");
end
//约束操作符    matches
rule "比较操作符- not-matches"
	when
		$customer : Customer(name not matches "matches.*"  )
	then
		System.out.println("比较操作符-not matches 测试成功");
end

//when部分，调用外部方法   eval
rule "eval element"
	when
		$customer : Customer()
		eval($customer.getName() == '张三')
	then
		System.out.println("eval element 测试成功");
end



//when部分，this关键字调用外部方法   eval
rule "this keyword"
	when
		Customer(this.getName() == 'david_this')
	then
		System.out.println("this keyword 测试成功");
end




rule "update"
    when
        m : Message( status == Message.HELLO, myMessage : message ) //myMessage是调用了getMessage()的返回结果
    then
        System.out.println( myMessage ); 
        m.setStatus( Message.GOODBYE );
        modify(m){
			 setMessage("Goodbye Drools");//书写语法
		}
        
         update( m );
         update(drools.getWorkingMemory().getFactHandleByIdentity(m), m);  //作用不循环吗????
        
		// insert( m ); //什么作用呢？？？
        System.out.println("drools.getClass()="+drools.getClass().getName()); //org.drools.core.base.DefaultKnowledgeHelper
        System.out.println("message = "+ m.getMessage());
        System.out.println("status = "+ m.getStatus());
         return; //可有可无
end


rule "retract 回收fact"
    when
        m : Message( status == Message.HELLO, myMessage : message )
    then
        System.out.println( myMessage );
        m.setMessage( "Goodbye Drools" );
        m.setStatus( Message.GOODBYE );
        retract(m); //【retract的作用是回收该fact，后面的类型为Message的 rule不会再 判断 对象】
end

import function my_code.validation.ValidationService.error;  // import function导入java类的 static方法

================OLD

---swf视频
.java 文件，在 ksession.startProcess(   处断点，代码中的设置参数employee为krisv,图中Actors属性为#{employee}
eclipse中显示视图 jBPM task ->Human Task View 输入risv->点refresh 按钮，后就有了新的一行

debug界面中Variables标签中选择kession变量
eclipse中显示视图 Drools->Processs Instanaces 就有了一项，双击它，就会在新的Processs Instanace窗口中有流程图，显示当前走到哪步
jBPM task ->Human Task View 选中项 -> start按钮 -> complete 按钮,Processs Instanaces 中的那一项，重新双击它，发现流程走到一步了
 
Human Task View 输入 john ->点refresh 按钮，有结果，是因下步中有一个结点的ActorId属性设置的john


http://localhost:8080/drools-guvnor   会提示安装samples
	知识库(Knowledge Base)面板->包(Pakckages)->defaultPackage->右边的资产(Assets)标签->Processes区中就有刚刚加入的文件名，打开它可以修改设计
	Edit 标签->Build Package 按钮,才可以在jbpm-console中看到
	左侧 (Knowledge Base)下 新建(Create New)->New BPMN2 Process，可以导入到eclipse中

http://localhost:8080/jbpm-console
	用户名/密码  用 "krisv" / "krisv"  是从\auth\roles.properties   复制到.war中的
	在process面板->Process Overview->右侧就有刚刚做过的“Evaluation",单击它 -> start按钮->OK->输入用户名和理由
	右则多了一项,State为RUNNING,  点Diagram按钮也可以看到流程图当前到哪一步
---



eclipse  的Guvor  视图中-> Add a Guvor Repository connection,使用默认配置生成http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/webdav/
右击.bmpn文件 -> Guvor->Add...->使用上面的Repository放在 defaultPackage中，

	-----------
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.workitem.wsht.HornetQHTWorkItemHandler;

KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
kbuilder.add(ResourceFactory.newClassPathResource("Evaluation.bpmn"), ResourceType.BPMN2);
KnowledgeBase kbase =  kbuilder.newKnowledgeBase();

StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();//简单版本，下一行就是ksession.startProcess("id");
HornetQHTWorkItemHandler humanTaskHandler = new HornetQHTWorkItemHandler(ksession);//使用 HornetQ 是一个消息中间键，支持JMS1.1, 
//hornet( 大黄蜂,马蜂)
humanTaskHandler.setIpAddress("127.0.0.1");
humanTaskHandler.setPort(5153);	//ant start.human.task后就有监听,对应于eclipse->preferences->jBPM Task中的端口
ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);


KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, "test", 1000);
Map<String, Object> params = new HashMap<String, Object>();
params.put("employee", "krisv");//.bpmn文件中　#{employee},Action脚本中可以使用变量名employee
params.put("reason", "Yearly performance evaluation");
ksession.startProcess("com.sample.evaluation", params); //对应.bpmn文件中 <process id="com.sample.evaluation" 
System.out.println("Process started ...");
logger.close();




ProcessInstance processInstance = ksession.startProcess("id");
assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);

extends JbpmJUnitTestCase 
	

"End Event"节点， 有一个terminate属性设置为flase,图表上少了一个小圈,可以做为子流程(Embeded Sub-Process)使用
"Script Task"节点,Action属性写代码时 List l = new ArrayList(); ,要选择java
	
Drool是　Java规则引擎开源项目　　(Rule Engines) 　　　 .drl文件　

List<String> list = new ArrayList<String>();
ksession.setGlobal("list", list);//对应　　global java.util.List list;
ksession.startProcess("id");
ksession.fireAllRules();//执行drl文件
assertTrue(list.size() == 1);//drl文件多加了一个Executed
		
BPMN2-RuleTask.drl
	package com.sample;

	global java.util.List list;

	rule MyRule
	  ruleflow-group "myRules"
	  when
	  then
		list.add("Executed");
	end

"Manual Task"节点和"Service Task"节点，	都有Parameter Mapping用设置key,value,其中value可以是变量名，代码中传入
	
ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new SystemOutWorkItemHandler());//对应所有"Manual Task"节点(<userTask>标签) 
							  registerWorkItemHandler("XX",... )//对应tns:taskName="XX",tns是"http://www.jboss.org/drools"

MyWorkItemHandler implements WorkItemHandler 
{
　executeWorkItem(WorkItem workItem,...)//在ksession.startProcess()时　或者ksession.getWorkItemManager().completeWorkItem(workItem.getId(), mapParams);被调用
}
	
.bpmn2文件中　_2 表示属性id值为2　　　name="First"　和　<conditionExpression　　　　在eclipse工具中作不了

<sequenceFlow id="_2-_3" sourceRef="_2" targetRef="_3" name="First" >　　
  <conditionExpression xsi:type="tFormalExpression" >return x == "First";</conditionExpression>
</sequenceFlow>
    
	<conditionExpression xsi:type="tFormalExpression" language="http://www.jboss.org/drools/rule" >eval(true);</conditionExpression>
	

也可不使用<conditionExpression　在<sequenceFlow中　属性tns:priority="2"　，xmlns:tns="http://www.jboss.org/drools"

diverage	分开;分歧;  converge会于一点, 向一点会合
type=XOR图标是"X" 显示为 <exclusiveGateway id="_2"　 default="_2-_4" gatewayDirection="Diverging"　下一步多个分支时，默认是哪条路，只可一条，也可在eclipse属性中加　
type=OR 图标是"*" 显示为 <inclusiveGateway　下一步多个分支时可经过所有满足条件的
type=AND图标是"+" 显示为 <parallelGateway


"Manual Task"节点的属性Actor Id设置为john,
<laneSet>
  <lane name="MyLane" >
	<flowNodeRef>_2</flowNodeRef>
  </lane>
</laneSet>
<userTask　id="_2" 　
	<potentialOwner>
		<resourceAssignmentExpression>
		  <formalExpression>john</formalExpression>
		</resourceAssignmentExpression>
	</potentialOwner>
代码中　workItem.getParameter("ActorId")
ksession.getWorkItemManager().completeWorkItem(workItem.getId(),  mapParam);//使用了<laneSet>后，mapParam才可以被传入



processInstance.getState() == ProcessInstance.STATE_ACTIVE//流程还没有结束之前
ksession.signalEvent("Yes", "YesValue", processInstance.getId());//第一个对应Event Type，第二个参数的值赋给VariableName定义的变量名(如x)


"Single Event"节点的Event Type属性自己起名(如Yes),VariableName属性设置保存的变量名(如设x),后面的流程节点就可以仿问x变量
	<intermediateCatchEvent>
		...
		<targetRef>x</targetRef>
		...
		<signalEventDefinition signalRef="Yes"/>
	 </intermediateCatchEvent>
"Timer Event"节点的Timer Delay属性设置500ms延迟半秒再向后
	<intermediateCatchEvent  >
      <timerEventDefinition>
        <timeDuration xsi:type="tFormalExpression">500ms</timeDuration>
      </timerEventDefinition>
    </intermediateCatchEvent>


eclipse中没有的节点,drool-guvnor中的设计器中有，但建立的bpmn文件生成的xml中的id很乱，导入的文件会被修改id和标签
<intermediateCatchEvent id="_4" name="condition" >
      <conditionalEventDefinition>
        <condition xsi:type="tFormalExpression" language="http://www.jboss.org/drools/rule">org.jbpm.examples.junit.Person(name == "Jack")</condition>
      </conditionalEventDefinition>
</intermediateCatchEvent>
Person jack = new Person();
jack.setName("Jack");
ksession.insert(jack);



<intermediateCatchEvent
	<messageEventDefinition　在eclipse的显示和<signalEventDefinition相同,使用也基本相同


		
		
ReceiveTaskHandler receiveTaskHandler = new ReceiveTaskHandler(ksession);
receiveTaskHandler.setKnowledgeRuntime(ksession);
ksession.getWorkItemManager().registerWorkItemHandler("Receive Task", receiveTaskHandler);//对应所有<receiveTask标签也是"Receive Task"节点
receiveTaskHandler.messageReceived("YesMessage", "YesValue");
"Receive Task"节点属性MessageId设置为YesMessage,Result Mapping属性设置为{Message=x}, 则变量x的值是YesValue
<receiveTask messageRef="YesMessage" 
	<ioSpecification>
        <dataOutput id="_3_result" name="Message" />
	...
   <dataOutputAssociation>
	<sourceRef>_3_result</sourceRef>
	<targetRef>x</targetRef>
  </dataOutputAssociation>
</receiveTask>


--parent.bpmn2
<itemDefinition id="_xItem" />
<process id="ParentProcess"
	<property id="x" itemSubjectRef="_xItem"/>			定义变量，用于把程序传入的值，再传传入子流程
	<callActivity id="_2"  calledElement="SubProcess" > 调用另一个bpmn文件中的子流程 
		<ioSpecification>
			<dataInput id="_2_subXInput" name="subX" />  子流程要读变量subX  ,id内部使用,name外部使用
			<dataOutput id="_2_subYOutput" name="subY" />子流程要写变量subY
			<inputSet>  			可以只用<inputSet/> 
			  <dataInputRefs>_2_subXInput</dataInputRefs> 
			</inputSet>
			<outputSet> 			可以只用<outputSet/> 
			  <dataOutputRefs>_2_subYOutput</dataOutputRefs>
			</outputSet>
		</ioSpecification>
      <dataInputAssociation>
        <sourceRef>x</sourceRef>
        <targetRef>_2_subXInput</targetRef>
      </dataInputAssociation>
      <dataOutputAssociation>
        <sourceRef>_2_subYOutput</sourceRef>
        <targetRef>y</targetRef>
      </dataOutputAssociation>
	</callActivity>
	
在eclipse中callActivity只要Parameter In Mapping属性为{subX=x},Parameter Out Mapping属性为{subY=y},
		 不选择节点时的属性Variables加x,y

--child.bpmn2
<process id="SubProcess"
	"script task"节点中Action属性kcontext.setVariable("subY", "new value");//选择mvel语言，子流程修改变量，为上一级使用

Map<String, Object> params = new HashMap<String, Object>();
params.put("x", "oldValue");
ProcessInstance processInstance = ksession.startProcess("ParentProcess", params);
(WorkflowProcessInstance) processInstance).getVariable("y")//子级设置后的值，父级也可得到




ksession.addEventListener(new DefaultProcessEventListener() 
	{
		public void afterProcessStarted(ProcessStartedEvent event) {
			System.out.println(event);
		}
		public void beforeVariableChanged(ProcessVariableChangedEvent event) {//kcontext.setVariable("x", "Hello");时
			System.out.println(event);
		}
		public void afterVariableChanged(ProcessVariableChangedEvent event) {
			System.out.println(event);
		}
	});


"Embeded SubProcess"节点  <subProcess>... </subProcess> ,可以单独设置属性Variables容器自己的变量,一个文件中，可以对多个流程做"组"


"Multiple Instances"节点的多余部分
<subProcess> 
	<ioSpecification>
		<dataInput id="_2_input"/>
		<inputSet/>
		<outputSet/>
	</ioSpecification>
	<dataInputAssociation>
		<sourceRef>list</sourceRef>//java 传来的集合数据
		<targetRef>_2_input</targetRef>//自己变量
	</dataInputAssociation>
	<multiInstanceLoopCharacteristics> //容器内部流程的重复次数，根据_2_input，即list的长度，第一项给变量item供容器内节点使用
		<loopDataInputRef>_2_input</loopDataInputRef>
		<inputDataItem id="item" itemSubjectRef="_2_multiInstanceItemType"/>//item是容器的变量
	</multiInstanceLoopCharacteristics>

eclilpse设置CollectionExpress为list,Variable Name为item

Map<String, Object> params = new HashMap<String, Object>();
List<String> myList = new ArrayList<String>();
myList.add("First Item");
params.put("list", myList);



eclipse中的图标很多是不对的，drools-guvnor上应该都是对的
不懂的
<escalation id="MyEscalation" escalationCode="MyEscalation" />
<process
	<subProcess id="_2">
		...
		<endEvent   name="EscalationEvent" >
		  <escalationEventDefinition escalationRef="MyEscalation" />
		</endEvent>
	
	</subProcess>
	<boundaryEvent name="EscalationEvent" attachedToRef="_2" >
		<escalationEventDefinition escalationRef="MyEscalation" />
	</boundaryEvent>



----------------------Drools 6
drool 流口水
 文件通常以.drl 结尾 ,如果是从多文件扩展建议文件名.rule
 
关键字
true
false
null

其它关键字
lock-on-active
date-effective
date-expires
no-loop
auto-focus
activation-group
agenda-group
ruleflow-group
entry-point
duration
package
import
dialect
salience
enabled
attributes
rule
extend
when
then
template
query
declare
function
global
eval
not
in
or
and
exists
forall
accumulate
collect
from
action
reverse
result
end
over
init



	
# 和 // 都是单行注释
/*
这是多行注释
*/
 package nesting; //定义包名
import org.drools.Person;//导入包
global java.util.List myGlobalList; //global 来定义别名

rule "Using a global"
	salience -100  //特定属性,salience表示优先级,也可赋于变量 
	timer (cron:* 0/15 * * * ?) //标准Unix表达式
	//timer ( int: 30s 5m ) //<initial delay> <repeat interval>, int: 表示Interval
	dialect "java"	
when
    eval( true ) //#如果多行是 and 操作
	//$p : Person()//找所有的Person 放入变量,变量名可以不用加$
	//$p :Person( age == 100 )//按条件找Person,age是Person的属性,也可调用方法,但方法必须每次调用返回相同的值,不能是时时变的,字符型数值和数字型会自动转换
	//Person( $firstAge : age ) // binding
	//Person( age == $firstAge ) // constraint expression
	//Person( age * 2 < 100, $age : age ) 
	//可用 memberOf,contains,matches另每个前加not ,str[startsWith],str[endsWith],str[length],in,eval(...),
then
    myGlobalList.add( "Hello World" );
end

function String hello(String name) {
    return "Hello "+name+"!";
}

declare Address //定义类
   number : int
   streetName : String
   city : String
end

declare Person
end

declare Student extends Person  //继承
    school : String
end











