alibaba openapi
https://api.aliyun.com/#/?product=schedulerx2
 

https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples


<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.6.RELEASE</version>   
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

 nacos 支持Go客户端
 https://github.com/nacos-group/nacos-sdk-go
--------Nacos 服务注册发现
功能类似Dubbo的Zookeeper
也是provider向注册到Nacos,consumer向Nacos取可用服务，直接连接provider而不是Nacos做通讯
客户端负载均衡

同时支持AP和CP模式,他根据服务注册选择临时和永久来决定走AP模式还是CP模式

下载  nacos-server-1.2.1.zip
linux 启动 ./startup.sh -m standalone
windows 启 startup.cmd -m standalone
#还带mysql的脚本

http://127.0.0.1:8848/nacos/  默认用户名密码是 nacos/nacos
Service Managment->Service List
界面里可以管理登录UI的用户

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
 自动依赖于 spring-cloud-starter-netflix-ribbon (已经被Loadbalancer替代了)

#关闭ribbon，但就不能用了，这个开关有什么呢？ 拉圾
ribbon.nacos.enabled
 
-----服务端
server.port=8081
spring.application.name=nacos-provider
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
management.endpoints.web.exposure.include=*


@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderDemoApplication.class, args);
    }
    @RestController
    public class EchoController {
        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return "Hello Nacos Discovery " + string;
        }
    }
}

-----客户端
server.port=8082
spring.application.name=nacos-consumer
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
management.endpoints.web.exposure.include=*

@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumerApp {
    @RestController
    public class NacosController{
        @Autowired
        private LoadBalancerClient loadBalancerClient;
        @Autowired
        private RestTemplate restTemplate;
        @Value("${spring.application.name}")
        private String appName;

        @GetMapping("/echo/app-name")
        public String echoAppName(){
            //Access through the combination of LoadBalanceClient and RestTemplate
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-provider");
            String path = String.format("http://%s:%s/echo/%s",serviceInstance.getHost(),serviceInstance.getPort(),appName);
            System.out.println("request path:" +path);
            return restTemplate.getForObject(path,String.class);
        }
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static void main(String[] args) {
    	//http://127.0.0.1:8082/echo/app-name
        SpringApplication.run(NacosConsumerApp.class,args);
    }
}


--------Nacos 配置 
 https://github.com/nacos-group/nacos-examples
 
	<dependency>
		<groupId>com.alibaba.cloud</groupId>
		 <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
	</dependency>
	 
	@RestController
	@RequestMapping("/config")
	@RefreshScope
	public class ConfigController {

		@Value("${useLocalCache:false}")
		private boolean useLocalCache;
		
		@Value("${fromDate:2020-10-01}")//在配置中一定要为  fromDate: '2020-10-01' ,即加'',如没有''是按Date类型接收再toString就不是这个格式
		private String dateStr;
	}

	配置加载dataId格式为
	${spring.application.name}.${file-extension:properties} 
	${spring.application.name}-${profile}.${file-extension:properties}
	
	#默认为 DEFAULT_GROUP,必须在bootstrap.properties
	#spring.cloud.nacos.config.group=DEVELOP_GROUP
	
	#dataids配置多个data id以, 分隔 
	spring.cloud.nacos.config.shared-dataids=bootstrap-common.properties,all-common.properties
	spring.cloud.nacos.config.refreshable-dataids=bootstrap-common.properties

	#可关闭自动刷新
	#spring.cloud.nacos.config.refresh.enabled=false
	
	spring.cloud.nacos.config.server-addr=127.0.0.1:8848 
	#支持 properties,yaml,yml,默认为 properties
	spring.cloud.nacos.config.file-extension=properties
	#spring.cloud.nacos.config.file-extension=yaml
 
--------Seata 分布式事务
Hmily 高性能异步分布式事务TCC框架 (Try,Confirm,Cancel)
tcc-transaction 分布式事务TCC框架
JTA事务管理器 Atomikos   Bitronix  
#Easy-Transaction 国产分布式事物框架 

2PC 两阶段提交(Prepare准备阶段， Commit提交阶段),依赖数据库XA实现 ，锁的时间较长

http://seata.io/
强一致性  AT   模式 ，使用锁，不是高性能的，牺牲了可用性,有使用undo_log表做补偿回退
弱一致性  Saga 模式 ,一个参与者失败则补偿前面已经成功的参与者

(内部用Dubbo,外部用Restful)

TC 事务协调者，即Seata Server  
TM 事务管理者，即示例的bussiness,有 @GlobalTransaction
RM 资源管理者，即示例的order,storage,account 

TM 向TC 申请 生成 XID 全局事务ID，XID的所有的TM中传播
TM 向TC 发起XID是提交还是回滚，TC向协调所有涉及到的TM做提交或回滚

---Seata 服务器

https://github.com/seata/seata/releases 下载 seata-server-1.2.0.zip

启动 seata-server.bat -p 8091 -m file
 -p监听端口
 -m file或者db ,当为db 修改 conf/file.conf 中数据库连接

driverClassName = "com.mysql.cj.jdbc.Driver"  #可为MySQL8的
 
create database seata default character set utf8;
create user seata@localhost identified by 'seata'; 
create user seata@'%' identified by 'seata';  
ALTER USER seata@localhost IDENTIFIED WITH mysql_native_password   BY 'seata';
ALTER USER seata@'%'  IDENTIFIED WITH mysql_native_password   BY 'seata';
grant all on seata.* to seata@localhost ;
grant all on seata.* to seata@'%'; 

建立表 global_table、branch_table和 lock_table
https://github.com/seata/seata/blob/develop/script/server/db/mysql.sql
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(96),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

启动  
seata-server.bat -p 8091 -m db  
也可不加-m db 在file.conf中修改mode.mode默认值为"db"  , 如不指定端口默认为8091
日志显示使用了registry.type 文件

registry.conf 中 registry.type 或 config.type 默认为 file ,可修改为指定的注册中心,如Nacos
	registry.type = "nacos"  
		还要修改 serverAddr = "localhost:8848"
		启动Seata后在Nacos界面的服务列中有seata-server
	config.type = "nacos"
		https://github.com/seata/seata/blob/develop/script/config-center/config.txt
			
			修改内容		
			service.vgroupMapping.my_test_tx_group=default #可增加多行,可能要对应项目中
		
			把 store.mode=file 修改为 store.mode=db
			#还有
			store.db.driverClassName=com.mysql.cj.jdbc.Driver
			store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true
			store.db.user=seata
			store.db.password=seata
		https://github.com/seata/seata/tree/develop/script/config-center/nacos  下有 nacos-config.py 和 nacos-config.sh 
			python  nacos-config.py localhost:8848 #参数是 <nacosAddr> 
			读上层目录的config.txt 文件,执行后查询Nacos有很多以SEATA_GROUP为组的配置项,日志提示启动seata服务
			
			sh nacos-config.sh localhost:8848
			
			
----
每个业务节点上的数据库要建立表undo_log   (对于用AT模式)
https://github.com/seata/seata/blob/develop/script/client/at/db/mysql.sql
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT 'increment id',
    `branch_id`     BIGINT(20)   NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(100) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME     NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME     NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table

示例代码的readme-zh.md 中还有的脚本有ext字段??
 


<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid-spring-boot-starter</artifactId>
	<version>1.1.10</version>
</dependency>


GlobalTransactionAutoConfiguration源码中 如spring.cloud.alibaba.seata.txServiceGroup 没有配置默认为applicationName + "-fescar-service-group" 
debug调试可以取到名为 spring.cloud.alibaba.seata.tx-service-group的值



分析源码使用，新版本不可这么用
@Bean
public DataSourceProxy dataSourceProxy(DataSource datasource)
{
	return new DataSourceProxy(datasource);//用于本地事物和外部系统一起做分布式事务时，对本地事务的拦截用的
}
	
@GlobalTransactional
@GlobalLock 是会判断lock_table表中我要读的数据，说明有人在操作，我要等，分布式的防止脏读

一条个更新操作，多生成3步操作, 查询修改前镜像，修改后镜，插入undo_log

可批量操作

https://github.com/seata/seata/tree/develop/script/client/conf 里有Seata的客户端要用的配置文件
file.conf文件里有service配置区,使用的 vgroupMapping ??都对不上，拉圾！，对于file的配置 可能 要放在seata-server/conf目录下


------示例代码
https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples/seata-example 

seata服务器conf目录下的file.conf和registry.conf (默认都是file模式) 
	如为file模式 要复制到 相关的项目classpath下,即src/main/resources目录下(用nacos和db 测试成功)

registry.conf 文件的 registry组 和 config组 中当 type="file" 配置中默认使用 file.conf

file.conf中的store区，有注释说只为seata-server使用


 <!-- spring-cloud-starter-alibaba-seata 的父 spring-cloud-alibaba-dependencies 2.2.1.RELEASE 对应seata-all-1.1.0 
 最好使用和服务一样的版本 ,不能排除seata-all使用新的1.2.0 报类找不到 -->
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-starter-alibaba-seata</artifactId> 
</dependency>

 
示例代码要修改 seata-server-1.2.0/seata/conf/file.conf 顶部增加
service {
# vgroup_mapping.<起的组名> 对应于 spring.cloud.alibaba.seata.tx-service-group=business-service  ,如配置为file还要复制到本地项目中
 vgroup_mapping.business-service = "default" 
 vgroup_mapping.account-service = "default" 
 default.grouplist = "127.0.0.1:8091"
 enableDegrade = false
 disableGlobalTransaction = false
}
如要复制到项目中，可能只要一个 vgroup_mapping 
使用 config.txt导入到Nacos,即 python  nacos-config.py localhost:8848  #读上层目录的config.txt 文件

 

business-service 使用feign, 使用Nacos 
只有account的配置不一样 的组名为 account-service 其它组名为 business-service
bussiness  
	-> storage (-2) 
	-> order (增加一条) -> 再调用account -4 (account随机出错) 后 -> order随机出错 
	
http://127.0.0.1:18081/seata/feign  服务端使用1.2版本,使用nacos和db ,测试模板出错成功回滚（刚启动时报timeout）
http://127.0.0.1:18081/seata/rest  服务端使用1.2版本,使用nacos和db ,测试模板出错成功回滚

 

---更多示例
https://github.com/seata/seata-samples/tree/master/seata-spring-boot-starter-samples    测试成功
在Readme中说  seata-spring-boot-starter默认开启数据源自动代理，用户若再手动配置DataSourceProxy将会导致异常。

这个是spring boot 的和上个示例用的spring cloud 不一样 
<dependency>
	<groupId>io.seata</groupId>
	<artifactId>seata-spring-boot-starter</artifactId>
	<version>1.2.0</version>
</dependency> 
依赖的是seata-all-1.2.0

seata-server 配置 registry.conf 文件的 registry.type="file" 为 nacos ,  file.conf配置成 db 
不用复制这两个文件到项目中

python导入config.txt 修改为 （对应file.conf中也加上）
service.vgroupMapping.account-service-seata-service-group=default
service.vgroupMapping.business-service-seata-service-group=default
service.vgroupMapping.order-service-seata-service-group=default
service.vgroupMapping.storage-service-seata-service-group=default

有报 transaction_service_group 字段值太长的错，上面名字取的有35个的，你给示例代码啊，傻逼啊，
脚本好几个地方都有乱啊,git上的长为32，readme中又是64,国产的东西真他妈的不规范啊
业务表有单独的在源码中


--------Sentinel 流控防护组件,限流

下载 Sentinel Dashboard
https://github.com/alibaba/Sentinel/releases

java -jar sentinel-dashboard-1.8.2.jar  http://127.0.0.1:8080/ 默认用户名密码  sentinel/sentinel
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.2.jar 


 

 
 
 
 

