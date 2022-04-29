Flowable 基于 activiti6   https://github.com/flowable 最新 flowable-engine 6.7.2 支持DMN，BPEL
	 flowable-designer( Eclipse plugin for BPMN editing) , flowable-mongodb , flowable-examples 
	 
也有商业版本，和Activiti 7 差不多
Flowable Work and Engage are built on top of the Flowable Open Source engines

https://www.flowable.com/getting-started-open-source

https://documentation.flowable.com/latest/develop/dbs/overview
https://www.flowable.com/getting-started-open-source
	https://www.flowable.com/open-source/docs/cmmn/ch02-Configuration
	https://www.flowable.com/open-source/docs/dmn/ch02-Configuration
	
 
	DMN =Decision Model and Notation  ,DMN 是OMG制定的
	BPEL=Business Process Execution Language
	CMMN (Case Management Model and Notation) ,是OMG制定的	

flowable-6.7.2\wars\flowable-ui.war  放 tomcat 的webapps目录下 http://localhost:8080/flowable-ui/  admin/test 前端使用anjular开发的
flowable-6.7.2\database\create\all\flowable.mysql.all.create.sql 
flowable-ui.war\WEB-INF\classes\flowable-default.properties 有连接数据库的配置

gear 齿轮

	界面中 
	Task App 是我的任务
	IDM App 中可以建立用户，建立组，权限 ,给权限时 workflow application 就是显示Task组
	Modeler App 组可以建立流程，
		Process菜单 ，在线设计流程 ,可以导入(前端做的支持拖入方式即立即上传)，导出bpmn文件
			人图标建立任务，双击任务修改名称，任务属性  Assignment 可修改哪个人或组，Form reference属性 来关联表单（没用？？）
			旁边网关按钮默认为exclusive ，如想修改为其它类型点下方的spanner板手按钮，有属性flow order是表达式计算的先后顺序
			网关上的线也可双击增加名，属性中flow condition可设置表达式，如${sizek > 2}
			上方的有为线增加点和删除点的按钮

		Form菜单(开源版本不维护了，已经商业) 可以对表单 设置表达式 如${continent}，Drop-down可以事先设置选项值
		
		App菜单(App definitions) Edit include models按钮选择建立的流程，建立后还要Publish, 最顶级(与Modeler App同级)显示了建立的App
		
		Decisions菜单(DMN) 

CREATE DATABASE flowable DEFAULT CHARSET UTF8MB4 COLLATE UTF8MB4_BIN;

CREATE USER flowable@localhost IDENTIFIED BY 'flowable';
CREATE USER flowable@'%' IDENTIFIED BY 'flowable';

GRANT ALL ON flowable.* TO flowable@localhost;
GRANT ALL ON flowable.* TO flowable@'%';	
		

flowable-examples-master\demo\demo-jax-2018\demo-hello-world 示例

 <dependency>
	<groupId>org.flowable</groupId>
	<artifactId>flowable-spring-boot-starter</artifactId>
	 <version>6.4.0</version> 
</dependency>
可用新版本 6.7.2 有使用liquibase，如启动过程中，直接关服务,再启动时，可能导致 linquibase等待锁释放 ，存在act_XXX_databasechangeloglock 表中

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/flowable?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=flowable
spring.datasource.password=flowable
spring.datasource.hikari.maximum-pool-size=10

#---same as activiti 7  
#default true
flowable.database-schema-update=true

#default true ,check resources/processes directory
flowable.check-process-definitions=false

#default true (activiti 7 default false)
flowable.db-history-used=true

flowable.history-level=full



<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.8.0</version>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId> 
	<version>8.0.15</version>
</dependency>

import org.flowable.engine.RuntimeService;

@Autowired
private RuntimeService runtimeService;

 runtimeService.createProcessInstanceBuilder()
		.processDefinitionKey("myProcess")
		.variable("someData", "Hello")
		.start()
		.getId();
		
		
//---DMN  配置文件叫 flowable.dmn.cfg.xml 不适用的Spring风格流程 
DmnEngineConfiguration dmnEngineConf = SpringDmnEngineConfiguration.createDmnEngineConfigurationFromResource("flowable.dmn.cfg.xml");
//DmnEngineConfiguration dmnEngineConf = StandaloneDmnEngineConfiguration.createStandaloneDmnEngineConfiguration();

//dmnEngineConf.setDataSource(hikariDatasource);
//或者 
dmnEngineConf.setJdbcUrl("jdbc:mysql://localhost:3306/databasename");
dmnEngineConf.setJdbcDriver("com.mysql.cj.jdbc.Driver");
dmnEngineConf.setJdbcUsername("user1");
dmnEngineConf.setJdbcPassword("user1");
dmnEngineConf.setDatabaseSchemaUpdate("true");

//---CMMN   配置文件叫 flowable.cmmn.cfg.xml 不适用的Spring风格流程 
//CmmnEngineConfiguration cmmnEngineConf = CmmnEngineConfiguration.createCmmnEngineConfigurationFromResource("flowable.cmmn.cfg.xml");
//CmmnEngineConfiguration cmmnEngineConf = CmmnEngineConfiguration.createStandaloneCmmnEngineConfiguration();
CmmnEngineConfiguration cmmnEngineConf = SpringCmmnEngineConfiguration.createCmmnEngineConfigurationFromResource("flowable.cmmn.cfg.xml");

//cmmnEngineConf.setDataSource(hikariDatasource);
//或者 
cmmnEngineConf.setJdbcUrl("jdbc:mysql://localhost:3306/databasename");
cmmnEngineConf.setJdbcDriver("com.mysql.cj.jdbc.Driver");
cmmnEngineConf.setJdbcUsername("user1");
cmmnEngineConf.setJdbcPassword("user1");
cmmnEngineConf.setDatabaseSchemaUpdate("true");


 
//---form 开源版本不维护了，已经商业
FormEngineConfiguration formEngineConfiguration=SpringFormEngineConfiguration.createFormEngineConfigurationFromResource("");
FormEngineFactoryBean factory=new FormEngineFactoryBean();
factory.setFormEngineConfiguration(formEngineConfiguration);
FormEngine formEngine=factory.getObject();

//FormEngine formEngine = FormEngines.getDefaultFormEngine(); 

FormRepositoryService formRepositoryService = formEngine.getFormRepositoryService();
FormService formService = formEngine.getFormService();
FormManagementService formManagementService = formEngine.getFormManagementService();
//查询
List<FormDeployment> formDeployments = formRepositoryService.createDeploymentQuery()
		.deploymentNameLike("deployment%")
		.orderByDeploymentTime()
		.list();
 
long count = formRepositoryService.createNativeDeploymentQuery()
		.sql("SELECT count(*) FROM " + formManagementService.getTableName(FormDeploymentEntity.class) + " D1, "
			+ formManagementService.getTableName(FormDefinitionEntity.class) + " D2 "
			+ "WHERE D1.ID_ = D2.DEPLOYMENT_ID_ "
			+ "AND D1.ID_ = #{deploymentId}")
		.parameter("deploymentId", deployment.getId())
		.count();
			



		
		
//---6.5新的，流程设计器中的 start event组中的 注册开始事件(start event registry event),非BPMN标准
//属性只用到id,name设置为名myEvent

//spring boot 方式，配置数据源，就可以用
@Autowired
ProcessEngine processEngine;

//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//找 flowable.cfg.xml 文件

//1.部署流程
processEngine.getRepositoryService().createDeployment().addClasspathResource("processes/new65Event.bpmn20.xml").deploy();
//成功 act_ge_bytearray 表

//2.部署事件
EventRegistryEngineConfiguration eventConfig=	(EventRegistryEngineConfiguration)processEngine.getProcessEngineConfiguration().getEngineConfigurations().get(EngineConfigurationConstants.KEY_EVENT_REGISTRY_CONFIG);//6.5新功能  
EventRegistryEngine eventRegistryEngine= eventConfig.buildEventRegistryEngine();
EventRepositoryService eventRepositoryService=eventRegistryEngine.getEventRepositoryService();

eventRepositoryService.createEventModelBuilder()
.key("myEvent")//对应id的值 
.resourceName("myEvent.event")
.correlationParameter("customerId", EventPayloadTypes.STRING)
.payload("payload1",EventPayloadTypes.STRING)
.deploy();
 //FLW_EVENT_DEFINITION  表,中有列 DEPLOYMENT_ID_ 对应 FLW_EVENT_DEPLOYMENT表的ID_ ,新版本为uuid格式
 
 
//3.部署管道
//FLW_CHANNEL_DEFINITION 中有列 DEPLOYMENT_ID_ 对应 FLW_EVENT_DEPLOYMENT表表ID_
eventRepositoryService.createInboundChannelModelBuilder()
.key("testChannel")
.resourceName("test.channel")
.jmsChannelAdapter("test-consumer")
.eventProcessingPipeline()
.jsonDeserializer()
.detectEventKeyUsingJsonField("type")
.jsonFieldsMapDirectlyToPayload()
.deploy();


//4.发送事件
ObjectMapper objectMapper=new ObjectMapper();
ObjectNode json=objectMapper.createObjectNode();
json.put("type","myEvent");//关联事件
json.put("payload1","Hello World");//带数据 
String jsonStr="";
try {
	 jsonStr=objectMapper.writeValueAsString(json);
} catch (JsonProcessingException e) { 
	e.printStackTrace();
}

EventRegistryEngineConfiguration eventConfig=	(EventRegistryEngineConfiguration)processEngine.getProcessEngineConfiguration().getEngineConfigurations().get(EngineConfigurationConstants.KEY_EVENT_REGISTRY_CONFIG);//6.5新功能  
EventRegistryEngine eventRegistryEngine= eventConfig.buildEventRegistryEngine();
EventRepositoryService eventRepositoryService=eventRegistryEngine.getEventRepositoryService();
	  
InboundChannelModel channelModel=(InboundChannelModel)eventRepositoryService.getChannelModelByKey("testChannel");//对应前面的Key

//发送事件ACT_RU_JOB 有数据  ，测试没有？？？可能是JMS不通
eventRegistryEngine.getEventRegistry().eventReceived(channelModel, jsonStr);////ACT_RU_JOB 有数据 


//5. 执行job任务 ,ACT_RU_EXECUTION,,ACT_RU_TASK 表有记录
ManagementService managementService = processEngine.getManagementService(); 
List<Job> jobs = managementService.createJobQuery().list();
for(Job job:jobs) {
	System.out.println("执行任务:"+job.getId());
	managementService.executeJob(job.getId());
} 
	




//---6.5新的， 流程设计器中的 activities 组中的 send event  task,非BPMN标准

//---6.5新的， 流程设计器中的 Boundary 组中的 Boundary event registry event,非BPMN标准

//---6.5新的， 第一版本的流程，只有一个任务，升级到第二版的流程变成两个任务，如想要执行第一版本的所有实例 切换 为第二版 使用ProcessMigrationService

//1.部署
processEngine.getRepositoryService().createDeployment().addClasspathResource("processes/请假.bpmn20.xml").deploy();

//2.启动,可多次
//ACT_RU_TASK (流程中的任务节点)表的PROC_DEF_ID 列 ：： 分隔3列，中间为版本1
processEngine.getRuntimeService().startProcessInstanceByKey("leave");

//3.部署版本2
processEngine.getRepositoryService().createDeployment().addClasspathResource("processes/请假2.bpmn20.xml").deploy();

//4.流程实例的移值
String targetDefinitionKey="leave2";
Integer targetDefinitionVersion=Integer.valueOf(1);

String sourceProcessInstanceId="3e062c2c-a5b8-11ec-8de3-bc77376878aa";//act_ru_actinst的PROC_INST_ID_  
        	

String fromActivityId="sid-BE6371DC-41F8-49C0-897F-3F2176BDF910";
String toActivityId="sid-BE6371DC-41F8-49C0-897F-3F2176BDF910";


ProcessMigrationService processMigrationService = processEngine.getProcessMigrationService();
ProcessInstanceMigrationDocumentImpl mig =new ProcessInstanceMigrationDocumentImpl();
mig.setMigrateToProcessDefinition(targetDefinitionKey,targetDefinitionVersion);

List<ActivityMigrationMapping> list=new ArrayList<>();
list.add(ActivityMigrationMapping.createMappingFor(fromActivityId,toActivityId));//act_ru_actinst(流程图中每一个节点)表有ACT_ID_,也对应于   <userTask 的id=""
mig.setActivityMigrationMappings(list);
processMigrationService.migrateProcessInstance(sourceProcessInstanceId, mig);//实时做，act_ru_actinst 表任务名变化
//processMigrationService.batchMigrateProcessInstancesOfProcessDefinition(sourceProcessInstanceId, mig);//批量做，插入到表FLW_RU_BATCH,FLW_RU_BATCH_PART,默认开一定时器在ACT_RU_TIMER_JOB
//迁移功能 不如Camunda




如把流程放在 processes 目录下，测试下来启动springboot 会自动部署  act_re_procdef 就已经有数据了
#default true ,check resources/processes directory
flowable.check-process-definitions=false



@Configuration
public class MyListenerConfiguration  implements ApplicationListener {

	@Autowired
	private SpringProcessEngineConfiguration  config;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		//runtimeService.addEventListener(null); 
		 
		config.getEventDispatcher().addEventListener( new AbstractFlowableEngineEventListener() {
			@Override
			protected void taskCreated(FlowableEngineEntityEvent event) {
				 
				//可以做消息页面websocket推送通知
				event.getProcessDefinitionId();
				event.getProcessInstanceId();
				
				FlowableEntityEventImpl	eventImpl=(FlowableEntityEventImpl)event;
				TaskEntity entity=(TaskEntity)eventImpl.getEntity();
				System.out.println("AbstractFlowableEngineEventListener : entity= "+entity);
				
			} 
		});
	} 
}

config.setJdbcMaxActiveConnections(10);

----------------------以下和Activiti是一样的API，只包名换一下
ProcessEngine 的生成方式也可使用以前的方式


ProcessEngineConfiguration config =ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
//config.setDataSource(dataSource);//也可指定数据源

config.setJdbcDriver("com.mysql.cj.jdbc.Driver");//mysql 8
config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
config.setJdbcUsername("flowable");
config.setJdbcPassword("flowable");config.setJdbcMaxActiveConnections(10);
//String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP;//先删表再建,但报索引已存在
String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;//如不存在表，就建
config.setDatabaseSchemaUpdate(schemaType);
ProcessEngine processEngine = config.buildProcessEngine();

	
			
 
//1.部署流程定义
RepositoryService repositoryService=processEngine.getRepositoryService();
DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
deploymentBuilder.addClasspathResource("hello.bpmn");
deploymentBuilder.addClasspathResource("hello.png");
Deployment deployment=deploymentBuilder.deploy();
		















		
		
