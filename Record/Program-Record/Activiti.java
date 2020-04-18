如何在流程图高亮当前节点，找到了可生成图
网页版本设计器放在自己网站
BPMN还有很多其它图标，如何用 (6 版本ReceiveTask如继续)

----------------Activiti Core 7 

Activiti 7 (2019-03-29) 开始有了Activiti Cloud(收费) ,Activiti Core (开源 支持SpringBoot2.x)

5和6 版本 原创作者是Tom  是JBPM4 分出来的, 但到不是Tom做的了, Tom又做了个 Flowable


https://github.com/Activiti/Activiti
https://github.com/Activiti/activiti-examples

https://activiti.gitbook.io/activiti-7-developers-guide/
 


-- Activiti Core 7 版本 变化 支持SpringBoot2.x , 强依赖了 Spring Security 
变为25张表
去除了
act_id_group
act_id_info
act_id_membership
act_id_user
增加了
act_ru_integration

engine.getIdentityService(); //方法去除了
engine.getFormService(); //方法去除了

新的API


BOM (Bill of Materials)
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.activiti.dependencies</groupId>
			<artifactId>activiti-dependencies</artifactId>
			<version>7.0.0.GA</version> <!-- 7.1.0.M6  有BUG -->
			<scope>import</scope>
			<type>pom</type>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring-boot-starter</artifactId> 
</dependency>

<!-- 这个还是有的 -->
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-image-generator</artifactId> 
</dependency>


<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
spring.datasource.url=jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=bpmn
spring.datasource.password=bpmn
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


日志显示 使用org/activiti/db/create/activiti.mysql.create.engine.sql  (activiti-engine-7.0.0.GA.jar)


import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.api.process.runtime.ProcessRuntime;


@RunWith(SpringRunner.class)
@SpringBootTest//如不指定class=,包名要和src/main/java里的包名一样，为了找到 @SpringBootApplication
public class ActivitiCore7Tests { 

	@Autowired
	private TaskRuntime taskRuntime;

	@Autowired
	private ProcessRuntime processRuntime;  //也可以不使用这个，用老的API也行
	//接口唯一的实现类ProcessRuntimeImpl 上有spring security的 @PreAuthorize("hasRole('ACTIVITI_USER')")

    @Autowired
    private SecurityUtil securityUtil;
	

//在执行任何代码前 会自动建立表,但没有act_hi_*表？？
// 自动部署src/main/resources/processes目录下的所有流程(如目录有变化，会再次部署,deployment表名为SpringAutoDeployment产生新版本，相同的流程定义以前就没用了)

	@Test
    public void testQueryDeployByPage()
	{
	//要求先登录系统,即使用SpringSecurity的SecurityContextHolder.setContext(xx),要求必须是ACTIVITI_USER角色
	  securityUtil.logInAs("system");
      Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10)); 
//      ProcessDefinition pd= processRuntime.processDefinition(processDefinitionId);
      for (ProcessDefinition pd : processDefinitionPage.getContent()) {
          System.out.println("查询到已经部署的流程定义 : " + pd);
      }
    } 
    @Test
    public void startInstance() 
    { 
	 securityUtil.logInAs("system");
    	  //启动流程实例 
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("myProcess")
                .withName("helloword Name")
//                .withBusinessKey(businessKey)
                 .withVariable("account", "erdemedeiros")
                .build());
        System.out.println("启动了流程实例"+processInstance);
    }
	@Test
    public void queryInstance()
    {
    	 securityUtil.logInAs("system");
    	GetProcessInstancesPayload payload=new GetProcessInstancesPayloadBuilder().withProcessDefinitionKey("myProcess").build();
    	Page<ProcessInstance>list=processRuntime.processInstances(Pageable.of(0, 10),payload);
    	for(ProcessInstance inst:list.getContent())
    	{
    		System.out.println("查询到流程实例"+inst);
    	}
    }
	@Test
    public void queryTask()
    {
    	 securityUtil.logInAs("salaboy");
    	 //GetTasksPayload payload=new GetTasksPayloadBuilder().withAssignee("salaboy").build();
    	 //没有历史 ,如没有指定人，组，候选人，查不到
    	 Page<Task>  tasks = taskRuntime.tasks(Pageable.of(0, 10));//可选第二个参数
		  //只查当前登录人的(同组的) ，SpringSeurity组名要加前缀GROUP_如GROUP_activitiTeam，在activiti中就要用activitiTeam
    	 for(Task task:tasks.getContent())
    	 {
    		  System.out.println("找到任务"+task);
    	 }
    }
	@Test
    public void completeTask()
    {
        securityUtil.logInAs("salaboy");
        Page<Task>  tasks = taskRuntime.tasks(Pageable.of(0, 10)); 
	  	 for(Task task:tasks.getContent())
	  	 {
	  		 Task t=taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
			 //	    			 .withVariable("x", "y")
			 .build());
	  	    System.out.println("完成任务"+t);
	  	 }
    }
	@Test
    public void claimTask() 
    {
    	 securityUtil.logInAs("ryandawsonuk");//同组里的人 
    	 Page<Task>  tasks = taskRuntime.tasks(Pageable.of(0, 10)); 
	  	 for(Task task:tasks.getContent())
	  	 {
	  		 Task t=taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
	         System.out.println("claim任务 : " + t);
	    	 t=taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
	  	    System.out.println("完成任务"+t); 
	  	    //驳回到上一节点，通过排它网关指向前一节点
	  	 }
    }
	 @Test
    public void  queryAllVar()
    {
    	 securityUtil.logInAs("erdemedeiros");
    	 Page<Task>  tasks = taskRuntime.tasks(Pageable.of(0, 10)); 
    	 String taskId=tasks.getContent().get(0).getId();
    	 
    	 List<VariableInstance> vars=taskRuntime.variables(TaskPayloadBuilder
                 .variables()
                 .withTaskId(taskId)
                 .build());
    	 for(VariableInstance var :vars)
    	 {
    		 System.out.println("任务中的流程变量: " + var.getName()+"="+var.getValue());
    	 }
    }
	 @Test
    public void deleteInstance() {
        securityUtil.logInAs("system");
        GetProcessInstancesPayload payload=new GetProcessInstancesPayloadBuilder().withProcessDefinitionKey("myProcess").build();
    	Page<ProcessInstance>list=processRuntime.processInstances(Pageable.of(0, 10),payload);
    	for(ProcessInstance inst:list.getContent())
    	{
    		ProcessInstance del = processRuntime.delete(ProcessPayloadBuilder.delete().withProcessInstanceId(inst.getId()).build());
    		 System.out.println("删除了流程实例 : " + del);
    	}
    }
			 
}


















----------------Activiti 6

Activiti (2017年12月版本是6.0,可能要翻墙)是基于JBPM-4 ,   ORM 使用 MyBatis3

activiti-engine-6.0.0.jar
activiti-image-generator-6.0.0.jar
juel-impl-2.2.7.jar
commons-lang3-3.5.jar
joda-time-2.9.9.jar

activiti-spring-6.0.0.jar 如需spring才要,使用的是spring-4.2.5
	 	activiti-process-validation-6.0.0.jar
		 activiti-dmn-api-6.0.0.jar
		 activiti-form-api-6.0.0.jar
		 activiti-bpmn-model-6.0.0.jar
		 
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-engine</artifactId>
    <version>6.0.0</version>
</dependency>
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring</artifactId>
    <version>6.0.0</version>
</dependency>


eclipse 插件 Activiti BPMN 2.0 designer 
http://activiti.org/designer/update/ 目前版本 5.18 (2015-08)
	2020-04-11还是5.18 版本，现在在线下载很慢,可能失败
找离线版本
org.eclipse.emf.transaction_1.4.0.v20100331-1738.jar
org.eclipse.emf.validation_1.7.0.201306111341.jar
org.eclipse.emf.workspace_1.5.1.v20120328-0001.jar
把带的3个jar包有现成的，复制到eclipse的plugins目录中,重启  
eclipse-4.15(sts3最后版本)help->install ->选择zip包(取消复选 concat all xxx 和 show only lastest)  测试OK
	

	
tools->preferences ->Activiti->save Actions 选中 create process definition image when save the diagram

war目录下的三个 
activiti-admin.war   登录默认用户名 amdin/amdin  
	 activiti-admin\WEB-INF\classes\META-INF\activiti-admin\activiti-admin.properties 中有配置连接 activiti-app
	 也可在页面中的Configuration配置
activiti-app.war   登录默认用户名 amdin/test  ,KickStart App里面可以新建
	带有网页版本的流程设计器(Alfresco Activiti Editor),也有网页版本中表单设计器
	数据库配置在 activiti-app\WEB-INF\classes\META-INF\activiti-app\activiti-app.properties
activiti-rest.war
	登录默认用户名 kermit/kermit ,activiti-rest/docs  是swagger

https://www.activiti.org/userguide/


database目录中有建表的脚本(mysql,oracle)


import org.activiti.engine.ProcessEngineConfiguration;


ProcessEngineConfiguration config =ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
//config.setDataSource(dataSource);//也可指定数据源

config.setJdbcDriver("com.mysql.cj.jdbc.Driver");//mysql 8
config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
config.setJdbcUsername("bpmn");
config.setJdbcPassword("bpmn");
//String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP;//先删表再建,但报索引已存在
String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;//如不存在表，就建
config.setDatabaseSchemaUpdate(schemaType);
ProcessEngine engine = config.buildProcessEngine();

默认使用Mybatis连接数据库

//方式二
ProcessEngineConfiguration config =ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("classpath:org/zh/acitiviti/spring_activiti.xml");
ProcessEngine engine=config.buildProcessEngine();
		
//mysql 
create database activiti default character set utf8;
CREATE USER bpmn@'%'  IDENTIFIED BY 'bpmn';
CREATE USER bpmn@localhost  IDENTIFIED BY 'bpmn';
grant all on activiti.* to 'bpmn'@'%'  ;
grant all on activiti.* to bpmn@localhost ;

activiti-6.0.0\database\create  三个文件 
	activiti.mysql.create.engine.sql 
	activiti.mysql.create.history.sql
	activiti.mysql.create.identity.sql



//用 Spring  wars\activiti-rest.war\WEB-INF\classes\activiti-custom-context.xml  有 spring
//默认找id名为  processEngineConfiguration,可第二个参数修改
ProcessEngineConfiguration config =ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
 //可从spring配置中直接拿到processEngine
 
 <bean id="processEngineConfiguration" class="org.activiti.engine.ProcessEngineConfiguration" factory-method="createStandaloneProcessEngineConfiguration">
	 <!--  	<property name="dataSource" ref="dataSource"/> -->
	<property name="jdbcDriver" value="com.mysql.cj.jdbc.Driver"/>
	<property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC"/>>
	<property name="jdbcUsername" value="bpmn"/>
	<property name="jdbcPassword" value="bpmn"/>
 </bean>

或者 spring 数据源事务方式 

<bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
	<property name="dataSource" ref="dataSource" />
	<property name="transactionManager" ref="transactionManager" />
	<property name="databaseSchemaUpdate" value="true" />
</bean>

<bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
	<property name="processEngineConfiguration" ref="processEngineConfiguration" />
</bean>
  


bpmn 文件格式

<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"  
xmlns:activiti="http://activiti.org/bpmn"
xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
typeLanguage="http://www.w3.org/2001/XMLSchema"
expressionLanguage="http://www.w3.org/1999/XPath"
targetNamespace="http://www.activiti.org/test">

  <process id="myProcess">
		<startEvent id="startevent1" name="Start"></startEvent>
		<userTask id="usertask1" name="提交申请"></userTask>
		<userTask id="usertask2" name="经理审批"></userTask>
		<endEvent id="endevent1" name="End"></endEvent>
		<sequenceFlow id="flow1" sourceRef="startevent1" targetRef="usertask1"></sequenceFlow> 
	</process>
</definitions>

设计器中 选择任务，eclipse打开 properties视图，Main Config->Assignee 表示由一个人完成任务
	Condiate Users,Condiate Groups 由逗号分隔多个,表示是由多个人完成任务(如为多个，在act_hi_actinst表中的ASSIGEE_开始为空的，做完才有值)
设计器中 选空白处  properties视图，Id值 

表都以 ACT_开头,缩写全称
GE=General 
HI=History		对应java类	HistoryService
RE=Repository	对应java类	RepositoryService
ID=Identity		对应java类	IdentityService
RU=Runtime		对应java类  RuntimeService

ManagementService	引擎管理类
TaskService 		任务管理类



Activiti6 共28张表,都没有comment,有索引，但act_re_procdef有外键约束

act_evt_log
act_ge_bytearray
act_ge_property
act_hi_actinst
act_hi_attachment
act_hi_comment
act_hi_detail
act_hi_identitylink
act_hi_procinst
act_hi_taskinst
act_hi_varinst
act_id_group
act_id_info
act_id_membership
act_id_user
act_procdef_info
act_re_deployment
act_re_model
act_re_procdef
act_ru_deadletter_job
act_ru_event_subscr
act_ru_execution
act_ru_identitylink
act_ru_job
act_ru_suspended_job
act_ru_task
act_ru_timer_job
act_ru_variable


		engine.getRepositoryService();
		engine.getRuntimeService();
		engine.getTaskService();
		engine.getHistoryService();
		engine.getIdentityService();
		engine.getFormService();
		engine.getManagementService();
		
部署后 ACT_RE_DEPLOYMENT 表中有值
ACT_RE_PROCDEF 表存放的是 流程定义  
ACT_GE_BYTEARRAY 表存放图片
ACT_GE_PROPERTY 有初始值 



//(看源码)找classpath:activiti.cfg.xml,找id为processEngineConfiguration
ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();

//1.部署流程定义
RepositoryService repositoryService=processEngine.getRepositoryService();
DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
deploymentBuilder.addClasspathResource("hello.bpmn");
deploymentBuilder.addClasspathResource("hello.png");
Deployment deployment=deploymentBuilder.deploy();
 
 //简写版
 Deployment deployment=processEngine.getRepositoryService() 
				.createDeployment() 
				.name("helloword")
				
				//.addClasspathResource("org/zh/activiti/hello.bpmn") 
		        //.addClasspathResource("org/zh/activiti/hello.png") 
			   .addZipInputStream(new ZipInputStream(this.getClass().getResourceAsStream("/org/zh/activiti/hello.zip")))//从zip 
		       //从zip 是一样的，会自动解压
			   //如不用zip 在act_ge_bytearray表中显示为org/zh/activiti/hello.bpmn,用zip就没包路径
		       .deploy();
			   
			   
System.out.println(deployment.getId());//是数据库中act_re_deployment的id值 
System.out.println(deployment.getName()); //就是前面的name参数值
//有数据表,act_re_procdef 表,act_ge_bytearray表(存bpmn和png文件)都有数据


//一个  流程定义  可以有多个 流程实例  

//2.启动流程实例
String processDefinitionKey = "myProcess";//key对应 bpmn 文件中id的属性值

//使用key值启动，默认是按照最新版本的流程定义启动
ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey,"leave10009");
//act_ru_execution(ACT_ID_是任务的ID),act_hi_procinst 表中有 BUSINESS_KEY_ 可用来存自己的业务表的ID,可选第二个参数传BUSINESS_KEY_的值
//第三个参数为流程变量的Map,act_ru_variable表存变量,act_hi_identitylink表是替换变量后的值,act_ge_bytearray中的bpmn文件还是变量
//如bpmn图中的Assignee的值为${user0},使用的是JavaEE的UEL表达式，可以${user.birthday}或${user.getAge()}或选线${user.getAge()>10},类要实现Serializable接口
//这的变量范围是全流程实例的，如task中的可以覆盖这里的同名变量

System.out.println("流程实例ID:"+instance.getId());//流程实例ID,是act_hi_procinst表的ID,看END_TIME_列为空
System.out.println("流程定义ID:"+instance.getProcessDefinitionId());//流程定义ID,是act_re_procdef表的ID
System.out.println("BUSINESS_KEY_:"+instance.getBusinessKey());
//有数据表,act_hi_identitylink表存截止当前任务参与的人,act_ru_task ,act_hi_taskinst,act_hi_actinst,act_ru_execution
	
@Test
public void testQueryMyTask(){
	queryMyTask("小李");
}
public String queryMyTask(String assignee){
	//3.查询当前人的个人任务(我的待办)
	String processDefinitionKey = "myProcess";
	//一个 流程实例  下 有多个任务 
	List<Task> list = processEngine.getTaskService() //就是act_ru_task(act_hi_taskinst)表的数据
					.createTaskQuery() 
					//where
						.taskAssignee(assignee)
						.processDefinitionKey(processDefinitionKey)//可指定哪个流程 
						//.taskCandidateUser(candidateUser) 
						//.processDefinitionId(processDefinitionId) 
						//.processInstanceId(processInstanceId) 
						//.executionId(executionId)
						//order by
						.orderByTaskCreateTime().desc()
						//返回
						.list();
					//.singleResult();
					//.count() 
					//.listPage(firstResult, maxResults);
					
	//数据做显示...
	return list.get(0).getId();
}

@Test
public void completeMyTask(){
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	//完成我的任务
	String taskId = queryMyTask("小李");
	processEngine.getTaskService().complete(taskId);//可选第二个参数传递Map变量
	//act_hi_taskinst表指定ID的记录END_TIME_有值 ,并增加一行为下一个任务
	//act_hi_actinst表指定TASK_ID_的记录END_TIME_有值 ,并增加一行为下一个任务
	//act_hi_identitylink表增加新的记录，下一任务的人
	//act_ru_task表把原来的做删除(ID_不能更新)，增加新的记录，记录当前任务的信息，如人
	
	
	System.out.println("完成任务：任务ID："+taskId);
}

/**查询流程定义*/
@Test
public void queryProcessDefinition(){
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
		
		List<ProcessDefinition> list = processEngine.getRepositoryService()
				.createProcessDefinitionQuery()//就是act_re_procdef表记录
				//where条件
//				.deploymentId(deploymentId) 
//				.processDefinitionId(processDefinitionId) 
//				.processDefinitionKey(processDefinitionKey) 
//				.processDefinitionNameLike(processDefinitionNameLike)
				//排序
				.orderByProcessDefinitionVersion().desc() 
//				.orderByProcessDefinitionName().asc()
				//返回
				.list(); 
//				.singleResult(); 
//				.count();
//				.listPage(firstResult, maxResults);
		for(ProcessDefinition pd:list){
			System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
			System.out.println("流程定义的名称:"+pd.getName());//对应 .bpmn文件中的name属性值
			System.out.println("流程定义的key:"+pd.getKey());//对应 .bpmn文件中的id属性值
			System.out.println("部署对象ID："+pd.getDeploymentId());//可用作删除
		}
 }
 
 
 //删除流程定义
 processEngine.getRepositoryService().deleteDeployment(deploymentId, true);//true 级联删除,即中途做废流程
	//如不传第二个参数只能流程中止才可删除,否则外键报错
 //act_re_deployment,act_re_procdef,act_ge_bytearray 表删除记录
 //当第二个参数为true 在act_hi_actinst,act_hi_taskinst中也没有记录了
 
 
 
//下载bpmn和图片文件
@Test
public void downloadFile() throws IOException
{
	String deploymentId = queryProcessDefinition();
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	List<String> reslist = processEngine.getRepositoryService()
			.getDeploymentResourceNames(deploymentId);//是人act_ge_bytearray表中下载
	for(String resourceName:reslist)
	{
		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
		FileUtils.copyInputStreamToFile(in, new File("D:/"+resourceName));
	}
	//方式二
	List<ProcessDefinition> list = processEngine.getRepositoryService() .createProcessDefinitionQuery().list(); 
	for(ProcessDefinition pd:list){
		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, pd.getResourceName());//图片
		FileUtils.copyInputStreamToFile(in, new File("E:/"+pd.getResourceName()));
		
		InputStream in2 = processEngine.getRepositoryService().getResourceAsStream(deploymentId, pd.getDiagramResourceName());//bpmn
		FileUtils.copyInputStreamToFile(in2, new File("E:/"+pd.getDiagramResourceName()));
	}
}

/* 挂起/激活 流程定义(全部流程实例)*/
@Test
public void suspendActivateAllProcessDef()
{
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();

	RepositoryService repositoryService=processEngine.getRepositoryService();
	ProcessDefinition pd= repositoryService.createProcessDefinitionQuery().singleResult();
	if(pd.isSuspended())
	{
		//第三个参数为时间，如为null则立即执行
		repositoryService.activateProcessDefinitionById(pd.getId(),true,null);//激活流程定义
		//act_re_procdef表的SUSPENSION_STATE_为1表示激活
	}else
	{
		repositoryService.suspendProcessDefinitionById(pd.getId(),true,null);//挂起流程定义
		//act_re_procdef表的SUSPENSION_STATE_为2表示挂起
	}
}
/*   挂起/激活 某个流程实例 */
@Test
public void suspendActivateProcessInstance()
{
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	String processDefinitionKey = "myProcess";
	
	RuntimeService runtimeService=processEngine.getRuntimeService();
	ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).singleResult();
	//挂起的流程实例不能再进行完成Task的,会报异常
	if(pi.isSuspended())
	{
		//act_ru_execution表的SUSPENSION_STATE_为1表示激活
		runtimeService.activateProcessInstanceById(pi.getId());
	}else
	{
		//act_ru_execution表的SUSPENSION_STATE_为2表示挂起
		runtimeService.suspendProcessInstanceById(pi.getId());
	}
}

//按名查询Deployment
Deployment deploy=processEngine.getRepositoryService().createDeploymentQuery()
		.deploymentName("varFlow").singleResult();
		
//选线，Main Config->Condition中写条如${leave.days<=3}，在任务完成时传递,如流程分支的线的条件可能同时满足，那么就一起进入两个任务
 Task  task = processEngine.getTaskService() .createTaskQuery() 
		//where 
		.taskAssignee("小王")
		.processDefinitionKey(processDefinitionKey).singleResult();
Map<String,Object> vars=new HashMap<>();
vars.put("leave",new Leave(3.5f,"回家"));
processEngine.getTaskService().complete(task.getId(),vars);//可选第二个参数传递Map变量,可以覆盖流程实例级的变量


/**查询流程变量的历史表*/
@Test
public void findHistoryProcessVariables(){
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	List<HistoricVariableInstance> list = processEngine.getHistoryService()
					.createHistoricVariableInstanceQuery()//就是act_hi_varinst表
					//.variableName("leave")
					.list();
	for(HistoricVariableInstance hvi:list){
		System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());
	}
}
//act_ru_variable表的数据
	public void setAndGetVariables(){
		ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
		/**流程变量 流程实例级 （正在执行）*/
		RuntimeService runtimeService = processEngine.getRuntimeService();
		/**流程变量 任务级（正在执行）*/
		TaskService taskService = processEngine.getTaskService();
		
		/**设置流程变量*/
//		runtimeService.setVariable(executionId, variableName, value)//表示使用执行对象ID， 一次只能设置一个值 
//		runtimeService.setVariables(executionId, variables)//Map集合设置 （一次设置多个值）
		
//		taskService.setVariable(taskId, variableName, value)//表示使用任务ID（一次只能设置一个值）
//		taskService.setVariables(taskId, variables)// Map集合设置流程变量（一次设置多个值）
		
//		runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时，用Map集合
//		taskService.complete(taskId, variables)//完成任务的同时，用Map集合
		
		/**获取流程变量*/
//		runtimeService.getVariable(executionId, variableName);//使用执行对象ID和流程变量的名称
//		runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量,返回Map
//		runtimeService.getVariables(executionId, variableNames);//使用执行对象ID，变量的名称存放到集合中
		
//		taskService.getVariable(taskId, variableName);//使用任务ID和流程变量的名称 
//		taskService.getVariables(taskId);//使用任务ID， 将流程变量放置到Map集合中 
//		taskService.getVariables(taskId, variableNames);//使用任务ID， 名称存放到集合中 
	}


//eclipse properties标签中有listener标签，new一个 event可选create(task创建),assigment(task分配),all,complete(idea工具没有),(idea工具还有delete)
//如type为class 要求实现 TaskListener接口

//	Condiate Users,Condiate Groups 由逗号分隔多个,表示是由多个人完成任务
//在act_hi_actinst表中的ASSIGEE_开始为空的，做完才有值,act_ru_task中ASSIGEE_，act_hi_identitylink表TYPE_有participant(单人),condiate(多人)
//processEngine.getTaskService().claim(taskId, userId); //组中或多候选人(也可以是非组任务的成员) 成员来做任务
//processEngine.getTaskService().setAssignee(taskId, null);//回退到组任务，前提，之前一定是个组任务
//processEngine.getTaskService().setAssignee(taskId, userId);//把任务指派给其它人办理,业务上保证当前登录人是目前经办人,也可按角色认领任务

//	processEngine.getTaskService().addCandidateUser(taskId, userId);//task中增加候选人
//	processEngine.getTaskService().deleteCandidateUser(taskId, userId);//task中删除候选人
//	processEngine.getTaskService().getIdentityLinksForTask(taskId);//查询
//	processEngine.getHistoryService().getHistoricIdentityLinksForProcessInstance(processInstanceId)

//--网关
//如流程分支的线的条件可能同时满足(或都没有设置条件)，那么就一起进入两个任务
// exclusiveGateway 排它网关 后如有两个分支，如条件可能同时满足，那么就会进id值小的那一个任务,如所有条件不满足就报错，控制出，不控制入 
// parallelGateway  并行网关 多个同时满足才能向下走 ，控制出(忽略后面分支线上的条件)，不控制入 
// includeGateway   包含网关 多个同时满足才能向下走 ，    出(使用后面分支线上的条件)  ，


如何在指定Task时，在图的边框高亮选中 org.activiti.image

---ReceiveTask
流程在收到特定消息(signalEventReceive)后继续向下走，这种没有办理人
不是userTask在 act_ru_task 表和 act_hi_taskinst 表没有数据,act_hi_actinst表有数据
@Test
public void deployAndStart()
{
	Deployment deployment=processEngine.getRepositoryService() 
			.createDeployment() 
			.name("nextMonthTarget")
			//另一种方式增加资源
			.addInputStream("nextMonthTarget.bpmn", this.getClass().getResourceAsStream("nextMonthTarget.bpmn"))
			.addInputStream("nextMonthTarget.png",  this.getClass().getResourceAsStream("nextMonthTarget.png"))
			.deploy();
	String processDefinitionKey = "nextMonthTargetProcess"; 
	ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
}

Execution execution1 = processEngine.getRuntimeService() 
				.createExecutionQuery() 
				.processInstanceId(pi.getId()) 
				.activityId("receivetask1")//对应.bpmn文件中的ReceiveTask节点id的属性值,存在ACT_ID_字段中
				.singleResult();
 
processEngine.getRuntimeService() //RuntimeService传变量
				.setVariable(execution1.getId(), "汇总当日销售额", 21000);
				
//流程继续执行
//5版本的signal(executionId)没有了，6版本如何做??)
processEngine.getRuntimeService().signal(execution1.getId());	
	
 
Integer value = (Integer)processEngine.getRuntimeService()//RuntimeService取变量
				.getVariable(execution2.getId(), "汇总当日销售额");//不同的execution可以拿到???





