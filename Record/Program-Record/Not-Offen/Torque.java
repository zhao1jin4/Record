apache DB 项目

torque-3.3.jar
village-3.3.jar
commons-configuration-1.4.jar
commons-collections-3.2.jar
commons-lang-2.3.jar
commons-beanutils-core-1.7.0.jar

commons-logging-1.1.jar

------


//-------------Torque.properties------------------------------

torque.database.default=my_myc3p0
#torque.database.default=my_jndi


torque.defaults.connection.driver =oracle.jdbc.driver.OracleDriver
torque.defaults.connection.url = jdbc:oracle:thin:@localhost:1521:xe
torque.defaults.connection.user =  hr
torque.defaults.connection.password =  hr

#----------------------JNDI---------------------no test
#torque.dsfactory.my_jndi.factory=org.apache.torque.dsfactory.JndiDataSourceFactory
#torque.dsfactory.my_jndi.jndi.path=jdbc/bookstore
#torque.dsfactory.my_jndi.jndi.java.naming.factory.initial = org.apache.naming.java.javaURLContextFactory
#torque.dsfactory.my_jndi.jndi.java.naming.factory.url.pkgs = org.apache.naming



#---------------------C3p0----------------------
torque.database.my_myc3p0.adapter=oracle

# # Using c3p0
torque.dsfactory.my_myc3p0.factory=org.zhaojin.My_TorqueC3P0DataSourceFactory
#<!--当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 --> 
torque.dsfactory.my_myc3p0.pool.acquireIncrement=2
#<!--定义在从数据库获取新连接失败后重复尝试的次数。Default: 30 --> 
torque.dsfactory.my_myc3p0.pool.acquireRetryAttempts=30
#<!--两次连接中间隔时间，单位毫秒。Default: 1000 --> 
#torque.dsfactory.ups.pool.acquireRetryDelay=1000
#<!--当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException,如设为0则无限期等待。
#单位毫秒。Default: 0 --> 
torque.dsfactory.my_myc3p0.pool.checkoutTimeout=30000
#<!--初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3 --> 
torque.dsfactory.my_myc3p0.pool.initialPoolSize=2
#<!--最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃。
#单位：秒 Default: 0 --> 
torque.dsfactory.my_myc3p0.pool.maxIdleTime=0
#<!--连接池中保留的最大连接数。Default: 15 --> 
torque.dsfactory.my_myc3p0.pool.maxPoolSize=15
#<!--JDBC的标准参数，用以控制数据源内加载的PreparedStatements数量。但由于预缓存的statements 
#属于单个connection而不是整个连接池。所以设置这个参数需要考虑到多方面的因素。 
#如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0--> 
torque.dsfactory.my_myc3p0.pool.maxStatements=150
#<!--maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0 --> 
torque.dsfactory.my_myc3p0.pool.maxStatementsPerConnection=0
#<!--c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能 
#通过多线程实现多个操作同时被执行。Default: 3--> 
torque.dsfactory.my_myc3p0.pool.numHelperThreads=3
#<!--每60秒检查所有连接池中的空闲连接。单位：秒 Default: 0 --> 
torque.dsfactory.my_myc3p0.pool.idleConnectionTestPeriod=600
#<!--定义所有连接测试都执行的测试语句。在使用连接测试的情况下这个一显著提高测试速度。注意： 
#测试的表必须在初始数据源的时候就存在。Default: null--> 
torque.dsfactory.my_myc3p0.pool.preferredTestQuery=select sysdate from dual
#db connnection
torque.dsfactory.my_myc3p0.connection.driverClass = oracle.jdbc.driver.OracleDriver
torque.dsfactory.my_myc3p0.connection.jdbcUrl = jdbc:oracle:thin:@localhost:1521:xe
torque.dsfactory.my_myc3p0.connection.user = hr
torque.dsfactory.my_myc3p0.connection.password = hr

//-------------------------------------------


package org.zhaojin;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.dsfactory.AbstractDataSourceFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class My_TorqueC3P0DataSourceFactory extends AbstractDataSourceFactory
{
	private ComboPooledDataSource ds = null;
	public DataSource getDataSource()
	{
		return ds;
	}
	public void initialize(Configuration configuration) throws TorqueException //parent
	{
		super.initialize(configuration);
		Configuration c = Torque.getConfiguration();
		ComboPooledDataSource c3p0DataSource = new ComboPooledDataSource();

		if (c == null || c.isEmpty())
		{
			System.out.println("Global Configuration not set");
		} else
		{
			Configuration conf = c.subset(DEFAULT_POOL_KEY);

			applyConfiguration(conf, c3p0DataSource);
		}
		// init 数据库连接
		Configuration connConf = configuration.subset(CONNECTION_KEY);
		applyConfiguration(connConf, c3p0DataSource);
		// init 数据库连接池
		Configuration conf = configuration.subset(POOL_KEY);
		applyConfiguration(conf, c3p0DataSource);

		this.ds = c3p0DataSource;
	}
	public void close() throws TorqueException	//parent
	{
		try
		{
			ds.close();
		} catch (Exception e)
		{
			throw new TorqueException(e);
		}
		ds = null;
	}
}




//----------------------------------------
package org.zhaojin;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.torque.Torque;
import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.LargeSelect;

import com.workingdogs.village.Record;
public class MyTestTorque
{
	public static void main(String[] args)throws Exception
	{
		InputStream torqueInput =MyTestTorque.class.getClassLoader().getResourceAsStream("Torque.properties");// 使用相对路径 找文件 src下
		PropertiesConfiguration config = new PropertiesConfiguration();
		config.load(torqueInput);
		Torque.init(config);//初始化 java.lang.InterruptedException: sleep interrupted
		String testSql = "select to_char(sysdate,'YYYY-MM-DD HH24:MI:SS') from dual";
		List list = BasePeer.executeQuery(testSql); //query
		Record record = (Record) list.get(0);
		String time = record.getValue(1).asString();
		System.out.println(time);
		java.sql.Connection conn=Torque.getConnection();//JDBC的Connection
		BasePeer.executeStatement("insert into departments(department_id,department_name)values(500,'lzj_dept')");//auto commit
		String defaultDB=Torque.getDefaultDB();
		System.out.println(defaultDB);
		
		//分页---------not test
		Criteria c=new Criteria();
		c.add("department_id", "500");
		BasePeer.doDelete(c,"departments",conn);
		int pageSize=20;
		int memoryPageLimit=5;//最多读到内在页数
		BasePeer.doSelect(c);
		LargeSelect largeSelect = new LargeSelect(c,pageSize,memoryPageLimit);
		List next = largeSelect.getNextResults();
		List previous = largeSelect.getPreviousResults();
		List page = largeSelect.getPage(2);
		//分页---------NO
	}
}



----可以配置xml 像hibernate 




