import oracle.toplink.essentials.exceptions.ExceptionHandler;
import oracle.toplink.essentials.expressions.Expression;
import oracle.toplink.essentials.expressions.ExpressionBuilder;
import oracle.toplink.essentials.queryframework.ReadObjectQuery;
import oracle.toplink.essentials.sessions.DatabaseLogin;
import oracle.toplink.essentials.sessions.DatabaseSession;
import oracle.toplink.essentials.sessions.Project;


Project project = new Project();
project.getLogin().setUserName("hr");
project.getLogin().setPassword("hr");
project.getLogin().setDriverClassName("com.o");
DatabaseSession session = project.createDatabaseSession();
session.login();







DatabaseLogin loginInfo = new DatabaseLogin();
loginInfo.useJDBCODBCBridge();
loginInfo.useSQLServer();
loginInfo.setUserName("");
loginInfo.setPassword("");
Project project1 = new Project(loginInfo);
DatabaseSession session1 = project.createDatabaseSession();
session.login();
....
session.logout(); 






session.setExceptionHandler(new ExceptionHandler(){...});

 session.readObject(Employee.class, new ExpressionBuilder().get("lastName").equal("Smith")); 

 Vector employees = session.readAllObjects(Employee.class,  new ExpressionBuilder().get("salary").greaterThan("1000")); 




 ReadObjectQuery query = new ReadObjectQuery();
   query.setReferenceClass(Student.class); 
query.setSelectionCriteria(Expression);
 query.setArguments(vector);
 session.executeQuery(query,vectory);



Toplink-11g\toplink_111100_en\utils\workbench\jlib\help    目录只有一个

大的文件文件在\utils\workbench\jlib\help目录多了很国际包而已（zh_CN）



要modules/xmlparserv2.jar

\utils\workbench  有图形界面的工作台，如加JDBC包要在　./bin/setenv 中设置set DRIVER_CLASSPATH=




-----------------------------
import oracle.toplink.indirection.ValueHolder;
import oracle.toplink.indirection.ValueHolderInterface;  
import oracle.toplink.sessions.DatabaseSession;
import oracle.toplink.sessions.UnitOfWork;
import oracle.toplink.tools.sessionconfiguration.XMLSessionConfigLoader;
import oracle.toplink.tools.sessionmanagement.SessionManager;

new ValueHolder(new Vector());//实体中有对就关系的类用ValueHolderInterface,一对多
new ValueHolder(); //一对一
//getter/settter方法内用setValue(xx),getValue();

XMLSessionConfigLoader loader = new XMLSessionConfigLoader("META-INF/sessions.xml");
SessionManager mgr = oracle.toplink.tools.sessionmanagement.SessionManager.getManager();
DatabaseSession session = (DatabaseSession)mgr.getSession(loader, "SessionName", Thread.currentThread().getContextClassLoader(), true, true);


UnitOfWork uow = session.acquireUnitOfWork();
uow.deleteAllObjects(xx);//List
uow.registerNewObject(address1);//insert
uow.readAllObjects(Address.class);//select

uow.commit();
uow.release();



SessionManager.getManager().destroyAllSessions();



--------Spring 与　Toplink集成　

  <bean id="toplinkSessionFactory" 
        class="org.springframework.orm.toplink.SessionFactoryBean">
    <property name="sessionsConfig">
      <value>sessions.xml</value>
    </property>
    <property name="sessionName">
      <value>mySession"</value>
    </property>
  </bean>



extends TopLinkDaoSupport
getTopLinkTemplate().

org.springframework.orm.toplink．TopLinkTransactionManager



使用外部事务
<bean name="transactionManager" 
	class="org.springframework.transaction.jta.OC4JJtaTransactionManager" >
</bean>
<bean name="transactionManager"
	class="org.springframework.transaction.jta.JtaTransactionManager" >
<bean>




本地事务
<bean name="transactionManager"
	class=" org.springframework.orm.toplink. TopLinkTransactionManager " >
<bean>
-----------------


官方中文Toplink,Spring教程
http://www.oracle.com/technology/global/cn/pub/articles/dikmans-spring-toplink.html
http://www.oracle.com/technology/global/cn/products/ias/toplink/doc/index.html
































