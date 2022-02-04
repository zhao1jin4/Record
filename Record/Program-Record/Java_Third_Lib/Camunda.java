
---------Camunda-7.16 工作流
替代activiti,基于activiti的分支,使用MyBatis,有社区和收费 两个版本
网上说 ，从Activii5迁移到camunda基本上毫无差异,flowable没有pvm了,所以迁移工作量更大

Camunda Platform 有run(使用spring boot-2.5的)版本,Tomcat版本(9),也有SpringBoot maven依赖版本

 <dependency>
  <groupId>org.camunda.bpm.springboot</groupId>
  <artifactId>camunda-bpm-spring-boot-starter</artifactId>
  <version>7.16.0</version>
</dependency>

生成的表也是act开头的

也有docker版本 docker pull camunda/camunda-bpm-platform:run-latest

run		版本启动用 start.bat	占8080端口
	http://localhost:8080 默认进入 
	http://localhost:8080/camunda/app/   看default.yml登录用 deom/demo 不带示例
	http://localhost:8080/swaggerui/

tomcat	版本启动用 start-camunda.bat
	http://localhost:8080/camunda-welcome 有示例

Camunda Modeler是一个设计工具，支持Mac,Linux 目前版本为 4.11.1，有点像PostMan一样，里面可能是网页界面
	新建BPMN图只有很少组件,可以点Chnage Type(板子)按钮，可以看到生成的XML
	 点菱形是网关,再Chnage Type切换类型如 exclusiveGateway,parallelGateway,includeGateway




