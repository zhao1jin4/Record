
Activiti (2017年12月版本是6.0,可能要翻墙)是基于JBPM-4 ,   ORM 使用 MyBatis3

activiti-engine-6.0.0.jar
activiti-image-generator-6.0.0.jar
juel-impl-2.2.7.jar
commons-lang3-3.5.jar
joda-time-2.9.9.jar

activiti-spring-6.0.0.jar 如需spring才要
	 	activiti-process-validation-6.0.0.jar
		 activiti-dmn-api-6.0.0.jar
		 activiti-form-api-6.0.0.jar
		 activiti-bpmn-model-6.0.0.jar
		 
eclipse 插件 Activiti BPMN 2.0 designer 
http://activiti.org/designer/update/ 目前版本 5.18

tools->preferences ->Activiti->save Actions 选中 create process definition image when save the diagram

war目录下的三个 
activiti-admin.war  
activiti-app.war   登录默认用户名 amdin/test  
	数据库配置在 activiti-app\WEB-INF\classes\META-INF\activiti-app\activiti-app.properties
activiti-rest.war


https://www.activiti.org/userguide/


database目录中有建表的脚本(mysql,oracle)

import org.activiti.engine.ProcessEngineConfiguration;


ProcessEngineConfiguration config =ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
config.setJdbcDriver("com.mysql.jdbc.Driver");
config.setJdbcUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&amp;characterEncoding=UTF-8");
config.setJdbcUsername("bpmn");
config.setJdbcPassword("bpmn");
//String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP;//先删表再建
String schemaType=ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;//如不存在表，就建
config.setDatabaseSchemaUpdate(schemaType);
ProcessEngine engine = config.buildProcessEngine();

默认使用Mybatis连接数据库

//方式二
ProcessEngineConfiguration config =ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("classpath:org/zhaojin/acitiviti/spring_activiti.xml");
ProcessEngine engine=config.buildProcessEngine();
		
//mysql 
create database activiti default character set utf8;
grant all on activiti.* to 'bpmn'@'%'  identified by 'bpmn';
grant all on activiti.* to bpmn@localhost  identified by 'bpmn';

activiti-6.0.0\database\create  三个文件 
	activiti.mysql.create.engine.sql 
	activiti.mysql.create.history.sql
	activiti.mysql.create.identity.sql

表都以 ACT_开头,共28张表
ACT_RU是runtime
ACT_HI是history
ACT_GE是 
ACT_ID


//用 Spring  wars\activiti-rest.war\WEB-INF\classes\activiti-custom-context.xml  有 spring
ProcessEngineConfiguration config =ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
 <bean id="processEngineConfiguration" class="org.activiti.engine.ProcessEngineConfiguration" factory-method="createStandaloneProcessEngineConfiguration">
	<property name="jdbcDriver" value="com.mysql.jdbc.Driver"/>
	<property name="jdbcUrl" value="jdbc:mysql://172.16.35.35:3306/activiti?useUnicode=true&amp;characterEncoding=UTF-8"/>
	<property name="jdbcUsername" value="bpmn"/>
	<property name="jdbcPassword" value="bpmn"/>
 </bean>

或者

<bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
	<property name="dataSource" ref="dataSource" />
	<property name="transactionManager" ref="transactionManager" />
	<property name="databaseSchemaUpdate" value="true" />
</bean>

<bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
	<property name="processEngineConfiguration" ref="processEngineConfiguration" />
</bean>
  


ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();//源码中找classpaht:activiti.cfg.xml
RepositoryService repositoryService=processEngine.getRepositoryService();
DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
deploymentBuilder.addClasspathResource("hello.bpmn");
deploymentBuilder.addClasspathResource("hello.png");
Deployment deployment=deploymentBuilder.deploy();
 
 //简写版
 Deployment deployment=processEngine.getRepositoryService() 
				.createDeployment() 
				.name("helloword")
				.addClasspathResource("org/zhaojin/activiti/hello.bpmn") 
		       .addClasspathResource("org/zhaojin/activiti/hello.png") 
		       .deploy();
			   
			   
System.out.println(deployment.getId());
System.out.println(deployment.getName());

共28张表名缩写全称
GE=General 
HI=History
RE=Repository
ID=Identity
RU=Runtime

部署后 ACT_RE_DEPLOYMENT 表中有值
ACT_RE_PROCDEF 表存放的是 流程定义
ACT_GE_BYTEARRAY 表存放图片
ACT_GE_PROPERTY 存










