Neo4j  NoSQL  图 数据库   是Java来开发实现的
Client (Driver) 有Python,JavaScript , .Net

工具 Neoclipse  4-5年没有维护了已经不能用了

在下载 Neo4j Desktop 版本 的下方有 下载 Neo4j Server Commnity  版本
解压  neo4j-community-3.4.8-windows.zip

有　docker版本

conf/neo4j.conf
	打开才能远程连接
	#dbms.connectors.default_listen_address=0.0.0.0
	默认监听下面端口
	#dbms.connector.http.listen_address=:7474
	#dbms.connector.https.listen_address=:7473
	#dbms.connector.bolt.listen_address=:7687
	
	#dbms.active_database=graph.db

启动命令：bin/neo4j  start/console/stop（启动/控制台/停止）
命令行：bin/cypher-shell


bin/neo4j.bat console 可以启动 

bin\neo4j install-service  后才可 bin\neo4j.bat start
 执行命令是 neo4j-community-3.4.8-windows\neo4j-community-3.4.8\bin\tools\prunsrv-amd64.exe //RS//neo4j 
bin\neo4j uninstall-service

http://localhost:7474/browser/
内部可以在输入 host 值为 bolt://localhost:7687 

也可以使用 Neo4j Desktop 中的 Neo4j Browser 工具host也可以输入 bolt://localhost:7687 
 
 
 :server connect
 
neo4j用户 默认密码是 neo4j 登录后要求修改

WARNING: Max 1024 open files allowed, minimum of 40000 recommended.
ulimit -n 最大打开文件数
ulimit -Sn  软
ulimit -Hn  硬 
/etc/security/limits.conf
user1   soft    nofile  1024
user1   hard    nofile  40000 

社交网络的应用

事务支持、高可用性和高性能
节点(Node接口)和关系(Relationship 接口) 都有属性,都继承自 PropertyContainer 接口
每个关系由起始节点、终止节点和类型(RelationshipType)等三个要素组成   



=== Cypher Query Language
命令区分大小写



CREATE语法
	CREATE (
	   <node-name>:<label-name>
	   { 	
		  <Property1-name>:<Property1-Value>
		  ........
		  <Propertyn-name>:<Propertyn-Value>
	   }
	)

CREATE (dept:Dept { deptno:10,dname:"Accounting",location:"Hyderabad" })
CREATE (emp:Employee{id:123,name:"Lokesh",sal:35000,deptno:10})
CREATE (emp:Employee{id:222,name:"姓名",sal:3000,deptno:10})

MATCH (dept: Dept) RETURN dept.deptno,dept.dname

MATCH (dept: Dept) RETURN dept



CREATE (m:Movie:Cinema:Film:Picture)

带关系 语法 
CREATE (<node1-name>:<label1-name>)-
	[(<relationship-name>:<relationship-label-name>)]
	->(<node2-name>:<label2-name>)
	
示例
CREATE (p1:Profile1)-[r1:LIKES]->(p2:Profile2)





MATCH (emp:Employee) 
WHERE emp.name = 'Lokesh' OR  emp.name = 'Abc'  
RETURN emp



CREATE (cust:Customer {id:"1001", name:"zhang" })
CREATE (cc:CreditCard{id:"5001",bal:3500})

创建关系
MATCH (cust:Customer),(cc:CreditCard) 
WHERE cust.id = "1001" AND cc.id= "5001" 
CREATE (cust)-[r:DO_SHOPPING_WITH{shopdate:"12/12/2014",price:55000}]->(cc)  有方向的->就是方向
RETURN r

查关系 ,双击图有线
MATCH ( cust: Customer)-[r]-()
RETURN cust
 
 
 
MATCH (e: Employee) RETURN e
 
MATCH (e: Employee) DELETE e

MATCH (cc:CreditCard)-[r]-(c:Customer)RETURN r     有值是双向关系??? , :前的名字也不一样

MATCH (cc: CreditCard)-[rel]-(c:Customer) 
DELETE cc,c,rel

DELETE操作用于删除节点和关联关系。
REMOVE操作用于删除标签和属性

CREATE (book:Book {id:122,title:"Neo4j Tutorial",pages:340,price:250}) 

MATCH (book { id:122 })
REMOVE book.price
RETURN book

MATCH (book { id:122 }) 
RETURN book  	在Graph视图中发现有<id>属性，每次建立系统自动从0开始每次+1 

SET 添加或更新属性值

MATCH (book { id:122 })
SET book.publisher='人民'
RETURN book
 

MATCH (emp:Employee)
RETURN emp.empid,emp.name,emp.salary,emp.deptno
ORDER BY emp.name

CREATE (dc:DebitCard{id:"5002",bal:3502})

UNION 两个查询要返回(RETURN)相同的列名(前缀也有影响用as)和数据类型
MATCH (cc:CreditCard) RETURN cc.id as id ,cc.bal as bal
UNION
MATCH (dc:DebitCard) RETURN dc.id  as id,dc.bal  as bal
 

UNION ALL 有重复行


MATCH (e: Employee) RETURN e 
SKIP 1 
LIMIT 2


MERGE命令在图中搜索给定模式
如果它不存在于图中，则它创建新的节点/关系并返回结果。
如果存在，不创建新

MERGE (gp2:GoogleProfile2{ Id: 201402,Name:"Nokia"})

MATCH  (gp2:GoogleProfile2) 
RETURN gp2.Id,gp2.Name


CREATE (e:Employee)  存的是null

MATCH (e:Employee) 
WHERE e.id IS NOT NULL
RETURN e.id,e.name,e.sal,e.deptno

 
MATCH (e:Employee) 
WHERE e.id IN [123,124]
RETURN e.id,e.name,e.sal,e.deptno

------------函数
MATCH (e:Employee) 
RETURN e.id,UPPER(e.name),e.sal,e.deptno



LOWER 
SUBSTRING(<input-string>,<startIndex> ,<endIndex>)

MATCH (e:Employee) RETURN COUNT(*)

MATCH (e:Employee) 
RETURN MAX(e.sal),MIN(e.sal),SUM(e.sal),AVG(e.sal)

 
查关系开始节点   ,双击图有线
MATCH (a)-[r:DO_SHOPPING_WITH]->(b) 
RETURN STARTNODE(r)

结束节点
MATCH (a)-[r:DO_SHOPPING_WITH]->(b) 
RETURN ENDNODE(r)

TYPE 返回关系名

MATCH (a)-[r:DO_SHOPPING_WITH]->(b) 
RETURN  ID(r),TYPE(r)


---index

CREATE INDEX ON :Customer (name)
DROP INDEX ON :Customer (name)


CREATE CONSTRAINT ON (cc:CreditCard)
ASSERT cc.id IS UNIQUE


CREATE (cc:CreditCard{id:"5001",bal:3500}) 重复报错

/* 必须先删关系才可删节点
MATCH (cc:CreditCard) 
WHERE cc.id = "5001"
DELETE cc
*/

DROP CONSTRAINT ON (cc:CreditCard)
ASSERT cc.id IS UNIQUE



----
 




















