http://www.omg.org/spec/BPMN/2.0/
Business Process Model and Notation (BPMN) 		Version 2.0

----------------------JBPM-5.4\
drool	流口水
guvnor	雇主,老板
 
按照install.html中说明

看build.xml,在lib/目录中加入 eclipse-java-juno-SR2-win32.zip
jbpm-6 是 eclipse-java-juno-SR2 (4.2),有 jbpm-6.0.0.CR2-examples

lib/org.drools.updatesite-6.0.0.Final.zip 是eclipse插件 可用于eclipse-4.2.2(有 依赖 org.jboss.tools.common.core)

或者
ant install.demo.noeclipse
ant start.demo.noeclipse   

ant stop.demo

会把 jbpm-installer\conf\guvnor.preferences.properties 文件打包到 drools-guvnor.war/WEB-INF/classes/preferences.properties
	
	jbpm-installer\conf 所有文件 文件打包到 jbpm-gwt-console-server-war/WEB-INF/classes ,也会复制数据库驱动 
	db/jbpm-persistence.xml 			文件打包到 jbpm-gwt-console-server-war/WEB-INF/classes/META-INF/persistence.xml
	db/ProcessInstanceInfoMapping.xml	文件打包到 jbpm-gwt-console-server-war/WEB-INF/classes/META-INF/ProcessInstanceInfoMapping.xml
	...
	
	


修改数据库H2到其它的　(看build.xml)
	db/persistence.xml　	被复制到　 jbpm-gwt-console-server-war/WEB-INF/classes/META-INF/persistence.xml
	task-service/resources/META-INF/persistence.xml　 　 
	standalone.xml	修改　<subsystem xmlns="urn:jboss:domain:datasources:1.0">　部分


eclipse->preference ->jBPM ->installed jBPM Runtimes->Add... -> Create a new jBPM Runtime 目录指向即D:\program\jbpm-5.4.0.Final-bin
右击项目->Properties->jBPM->选中Enable project specific settings ,在jBPM Runtime 中选择工作区建立的

eclipse的preferences有jbpm-task,配置5153,启动后就有5153端口监听,可能是启动JBoss时间过长,部署失败,可以手工重新启动JBoss,使用hornetq而不是mina
手工动时控制台有提示  /jbpm-console , /drools-guvnor 

---swf视频
bpm-5.4.0.Final-installer-full\jbpm-installer\sample\evaluation 的示例可以导入eclipse中去

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

kbuilder.add(ResourceFactory.newClassPathResource("junit/BPMN2-RuleTask.drl"), ResourceType.DRL);
...
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

