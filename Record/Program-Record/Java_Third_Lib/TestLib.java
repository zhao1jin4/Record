
----------------------------------Junit  4.0 

Junit 要求

1.必须是public void 无参方法 ,
2.必须以test开头


assertEquals(x, y);
Assert.fail();
Assert.assertEquals(x,y);

Assert.fail() 如果程序执行到这里,停止测试,测试失败,后面的不会被执行



TestCase 中有
	setUp() ,	在每一个testXxxx方法执行前去调用,可能会多次调用 
	tearDown(),	在每一个testXxx方法后被调用


这两个方法在抛出异常时也会被调用,测试失败也会的


JUnit 4.0 有 只执行一次初始方法,销毁方法 
import static org.junit.Assert.assertEquals; 
 类不必继承自 TestCase

@BeforeClass
public static void init()//必须是static
{
}
@AfterClass
public static void destory()
{
}

@Before  //不能是static的
public   void initObj() 
{
}
@After
public void destroy()
{
	System.out.println("junit after"); 
}
@Test(expected=Excpetion.class) //junit 4 
public void myTest()//不必以test开头
{
}


textui包  TestRunner类  run(Class testClass)   几个点,表示几个测试方法  
junit.textui.TestRunner.run(HelloTest.class);



java -cp .;junit-4.12.jar;hamcrest-core-1.3.jar  org.junit.runner.JUnitCore mytest.CalculatorTest  //会运行所有方法上有@Test

如使用 Main方法
Result result = JUnitCore.runClasses(CalculatorTest.class);
for (Failure failure : result.getFailures()) {
	System.out.println(failure.toString());
}
if (result.wasSuccessful()) {
	System.out.println("所有测试用列执行成功");
}
	
	 

边界问题 数组没有元素


测试应该没有依赖性,一个测试方法不会依赖于另一个方法
public static void main (String[] args) 
{
	junit.textui.TestRunner.run (suite()); 
}

public static junit.framework.Test suite() {   
    return new JUnit4TestAdapter(ListTest.class); 
}


public static Test suite() //use eclipse,junit 4 ,not need main method
{
	return new TestSuite(ListTest.class);
}

TestSuite 和TestCase 都实现了Test 接口 	



示例(带参数)
@RunWith(value = Parameterized.class)
public class CalculatorTest
{
	private int expected;
	private int para1;
	private int para2;
	
	@Parameterized.Parameters
	public static Collection<Integer[]> getParameters()//返回Collection
	{
		return Arrays.asList(new Integer[][] { 
			{ 3, 3, 2 }, //构造函数的参数类型必须全一样 
			{ 1, 1, 1 }  
		});
	}
	public CalculatorTest(int expected, int para1, int para2)//构造函数的参数类型必须全一样 
	{
		this.expected = expected;
		this.para1 = para1;
		this.para2 = para2;
	}
	@Test
	public void testPlus()
	{
		int acutal=Math.max(para1, para2);
		assertEquals(expected, acutal);
	}
}

//TestSuit 
@RunWith(Suite.class)
@SuiteClasses({CalculatorTest.class})//可传多个测试类
public class CalculatorTestSuit 
{

}
-----junit 5 

<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter</artifactId>
	<version>5.5.2</version>
	<scope>test</scope>
</dependency>
 
import org.junit.jupiter.api.Test; //Junit 5    jupiter 木星；
import static org.junit.jupiter.api.Assertions.*;//Junit 5 
assertThrows(NumberFormatException.class,  ()->{
		Roman2IntUtil.covertRoman2Int("MIMIII");
	});

//Junit5 API
@BeforeAll 用在static方法上
@BeforeEach 
@AfterEach
@AfterAll

---------------------------JMockit 更强  

---------------------------EasyMock  
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
//---实例
IMocksControl control = EasyMock.createControl();//使用多个 Mock 对象
ResultSet mockResultSet = control.createMock(ResultSet.class);//调用顺序是不进行检查

mockResultSet.next();
EasyMock.expectLastCall().andReturn(true).times(3);//对next方法调3次返回true
EasyMock.expectLastCall().andReturn(false).times(1);//再调一次返回false

mockResultSet.getString(1);//对getString(1)方法调用的返回
EasyMock.expectLastCall().andReturn("DEMO_ORDER_001").times(1);
EasyMock.expectLastCall().andReturn("DEMO_ORDER_002").times(1);
EasyMock.expectLastCall().andReturn("DEMO_ORDER_003").times(1);

mockResultSet.getDouble(2);
EasyMock.expectLastCall().andReturn(350.0).times(1);
EasyMock.expectLastCall().andReturn(1350.0).times(1);
EasyMock.expectLastCall().andReturn(5350.0).times(1);

control.replay();//调用后mockResultSet就有值可用了

//...使用生成的Mock对象

control.verify();//验证方法调用真的完成了,如调用次数

//----
ResultSet strickMockResultSet = EasyMock.createStrictMock(ResultSet.class);//会检查调用方法的顺序
IMocksControl strictControl = EasyMock.createStrictControl();
ResultSet strickMockResultSet1 = strictControl.createMock(ResultSet.class);//会检查调用方法的顺序

ResultSet mockResultSet = EasyMock.createMock(ResultSet.class);//只需要一个 Mock 对象,调用顺序是不进行检查
mockResultSet.close();
EasyMock.expectLastCall().times(3, 5);//3-5次 atLeastOnce(),anyTimes()

EasyMock.expectLastCall().andStubReturn(mockResultSet);//方法的调用总是返回一个相同的值
SQLException exception=new SQLException();
EasyMock.expectLastCall().andThrow(exception);//预期异常抛出
EasyMock.expectLastCall().andStubThrow(exception);//设定抛出默认异常
	
mockStatement.executeQuery("SELECT * FROM sales_order_table");//有大小写的问题
mockStatement.executeQuery( EasyMock.anyObject().toString() );

EasyMock.reportMatcher(new SQLEquals(in));//注册自定义,implements IArgumentMatcher ,实现matches方法

//---Replay 状态
EasyMock.replay(mockResultSet);
control.replay();
//...

//为了避免生成过多的 Mock 对象，EasyMock 允许对原有 Mock 对象进行重用,如要重新初始化，我们可以采用 reset 方法
EasyMock.reset(mockResultSet);
control.reset();
//再mock方法,可使用spring中的getBean(),可动态修改IOC中的bean
EasyMock.replay();
control.replay();
EasyMock.verify(mockResultSet);
control.verify();

{
	//匹配指定参数
	mockObject.getProductPrice("tomota");
	EasyMock.expectLastCall().andStubReturn(45.0f);

	//匹配所有参数 放在最后  OK
	mockObject.getProductPrice(EasyMock.isA(String.class));
	EasyMock.expectLastCall().andStubReturn(55.0f);
}


---Spring 中的Bean做Mock
@Resource(name="myServiceBean")
private MyServiceBean bean;
	
IMocksControl mocksControl = EasyMock.createStrictControl();
MyDao mockDao=mocksControl.createMock(MyDao.class);//是class 要 objenesis.jar

EasyMock.expect(mockDao.insertData(dataSet)).andReturn((long)dataSet.size());

ReflectionTestUtils.setField(bean, "dao", mockDao, MyDao.class);//用spring提供的方法注入aurowired的字段
EasyMock.reset(mockDao);//不替换bean,直接 reset,BeanPostProcessor中已经是Mock的

EasyMock.replay(mockDao);
EasyMock.verify(mockDao);

//调用
Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));

	
//也可使用BeanPostProcessor
public class DaoPostProcessor implements BeanPostProcessor 
{
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException 
	{
		if("myDao".equals(beanName) ||bean instanceof  MyDao)
		{
			IMocksControl mocksControl = EasyMock.createStrictControl();
			MyDao clientMock=mocksControl.createMock(MyDao.class);
			
			//---List中的Product要重写equals方法
			//EasyMock.expect(clientMock.insertData(dataSet)).andReturn((long)dataSet.size());
			//EasyMock.expect(clientMock.queryData(param)).andReturn(dataSet);
			
			//--不用重写equals方法
			clientMock.insertData(EasyMock.isA(List.class));//有isNull(List.class),基本类型EasyMock.anyLong()
			//clientMock.queryData(EasyMock.anyObject(Product.class));//anyObject匹配传入null,isA不能匹配null
//			clientMock.queryData(
//						(Product) EasyMock.or(EasyMock.isA(Product.class),
//									EasyMock.isNull() )
//						);
			EasyMock.expectLastCall().andStubReturn((long)dataSet.size());
			
			clientMock.queryData(EasyMock.isA(Product.class));
			EasyMock.expectLastCall().andStubReturn(dataSet);
			
			EasyMock.replay(clientMock);
			return clientMock;
		}else
			return bean;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		return bean;
	}
}
//调用
//---Product要重写equals方法
//Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));
//Assert.assertEquals(dataSet, bean.queryData(param));
//---Product不用重写equals方法
Assert.assertEquals(dataSet.get(0).getName(), bean.queryData(param).get(0).getName());
Assert.assertEquals(dataSet.size(), bean.insertData(dataSet));

-------------------------MockITO
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>2.23.4</version> <!-- 版本2.23.4 有依赖包有JDK9 module-info.class,2.22.0 就没有-->
    <scope>test</scope>
</dependency> 
依赖json-path

有android版本

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

//模拟返回值
MyServiceBean myServiceBean = mock(MyServiceBean.class);   
when(myServiceBean.queryData(any(Product.class))).thenReturn(dataSet); 
List<Product>  res=myServiceBean.queryData(new Product());
System.out.println(res);

//模拟抛异常
when(myServiceBean.insertData(ArgumentMatchers.anyList())).thenThrow(RuntimeException.class);
myServiceBean.insertData(Arrays.asList(new Product()));


---Spring 集成 mockito ,在 SpringMVC.java 中也有

import org.mockito.Mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)  
//@ActiveProfiles({"test"})
//@Transactional
@WebAppConfiguration("file:WebContent/") //可以注入 WebApplicationContext
@ContextConfiguration(locations={"classpath:test_mockmvc/spring-mockmvc.xml"})
public class MockITO_MockMvcTest  
{
	//	@InjectMocks //会进入方法体中
	//	@Autowired

	@Mock
	private MyServiceBean myServiceBean;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testService() throws Exception
	{ 
		when(myServiceBean.queryData(any(Product.class))).thenReturn(dataSet); 
		List<Product>  res=myServiceBean.queryData(new Product());
		
		reset(myServiceBean);//重置指定的bean的所有录制  
	}
}
spring-mockmvc.xml 只有
	<context:component-scan base-package="test_mockmvc"></context:component-scan>
-------------------------TestNG
NG=Next Generation  (Not Good)

比Junit强的地方是支持 组,依赖,参数功能更强,支持listener
多支持下面的范围,JUnit-4 @BeforeClass 和 @AfterClass  必须声明静态方法 
	@BeforeSuite
	@AfterSuite
	@BeforeTest
	@AfterTest

 
<dependency>
  <groupId>org.testng</groupId>
  <artifactId>testng</artifactId>
  <version>6.10</version>
  <scope>test</scope>
</dependency>
会依赖于 jcommander-1.48.jar 
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest-core</artifactId>
    <version>2.2</version>
    <scope>test</scope>
</dependency>


Eclipse Market Place 安装TestNG (或者下载离线版本(6.14.0) https://github.com/cbeust/testng-eclipse)
	安装后new file->有testng组
	
	项目目录下会生成 test-output 目录
	
IDEA  是自带TestNG的 (要加maven依赖)


import org.testng.Assert;
import org.testng.annotations.Test;
public class MyTestNG
{
	
	@BeforeTest
	public void beforeTest()// <test>
	{		
		System.out.println("@BeforeTest1");
	}
	@BeforeClass
	public void beforeClass() // <class>
	{		
		System.out.println("@BeforeClass2");
	}
	@AfterClass
	public void afterClass()
	{		
		System.out.println("@AfterClass3");
	}
	@AfterTest
	public void afterTest()
	{		
		System.out.println("@AfterTest4");
	}
	  
	@Test  //方法的返回类型一定要为void
	public void testMethod1() {
		String email="abc";
		Assert.assertNotNull(email);
		//Assert.assertEquals(email, "123@test.com");
	}
	@Test(enabled = true,expectedExceptions = {ArithmeticException.class},timeOut = 1000)
    public void divisionWithException() {
        int i = 1 / 0;
        System.out.println("After division the value of i is :"+ i);
    }
	
	//---group
	@Test(groups = "nosql")
	public void hbase() {
		System.out.println("hbase()");
	}
	@Test(groups = {"nosql","database"})//可同时在多个组中
	public void redis() {
		System.out.println("redis");
	}
	@Test(dependsOnGroups = { "database", "nosql" })//组依赖
	public void runFinal() {
		System.out.println("runFinal");
	}
	
	/*
	//运行 testng_group.xml
	@Test
	@Parameters({ "param1", "param2" })//参数是在xml配置类级传入
	public void testParam(String param1,String param2) {
		System.out.println("param1="+param1+",param2="+param2);
	}
	*/
	@Test(dataProvider = "provideNumbers")//provider参数形式 
    public void testProviderParam(int number, int expected) {
        Assert.assertEquals(number + 10, expected);
    }
    @DataProvider(name = "provideNumbers")
    public Object[][] provideData() {
        return new Object[][] { { 10, 20 }, { 100, 110 }, { 200, 210 } };
    }
	 //--一个provider用于多方法
    @Test(dataProvider = "dataProvider")
    public void test1(int number, int expected) {
        Assert.assertEquals(number, expected);
    }
    @Test(dataProvider = "dataProvider")
    public void test2(String email, String expected) {
        Assert.assertEquals(email, expected);
    }
    @DataProvider(name = "dataProvider")
    public Object[][] provideData(Method method)//一个provide为多个测试方法，参数是反射Method,得不到group
    {
        Object[][] result = null;
        if (method.getName().equals("test1")) {
            result = new Object[][] {
                { 1, 1 }, { 200, 200 }
            };
        } else if (method.getName().equals("test2")) {
            result = new Object[][] {
                { "test@gmail.com", "test@gmail.com" },
                { "test@yahoo.com", "test@yahoo.com" }
            };
        }
        return result;
    }
	@Test(invocationCount = 12, threadPoolSize = 3)//压力测试,可结合Selenium
    public void testThreadPools() {
    	System.out.printf("Thread Id : %s%n", Thread.currentThread().getId());
    }
  
  
}


public class SuiteConfig {

	@BeforeSuite
	public void beforeSuite() {
		System.out.println("@BeforeSuite0");
	}

	@AfterSuite
	public void afterSuite() {
		System.out.println(" @AfterSuite5");
	}
}
@Test(groups = "group1")//类级别的组
public class Group1 {
	
}

----testng_group.xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="GroupSuite" verbose="1" > 
   <test name="MyTest">
    <classes>
	    <class name="testng.SuiteConfig"/> 
        <class name="testng.MyTestNG">
		   <parameter name="param1" value="value1" />
       	   <parameter name="param2" value="22" />
        	<methods>
        		<!-- <include name=""></include>  -->	
        		<exclude name="hbase"></exclude>
        	</methods>
        </class>
    </classes>
  </test>
  <test name="GroupTest">
    <classes>
        <class name="testng.SuiteConfig"/>
     	<class name="testng.Group1"/> 
    </classes>
  </test>
  <!-- listener可以是 
		  IAnnotationTransformer
		  IAnnotationTransformer2
		  IHookable
		  IInvokedMethodListener
		  IMethodInterceptor
		  IReporter
		  ISuiteListener
		  ITestListener 
   -->
  <listeners>
	<listener class-name="testng.listener.MyTransformer" />
	<listener class-name="testng.listener.MyMethodInterceptor" />
	<listener class-name="testng.listener.DotTestListener" />
  </listeners>
</suite>
----testng_package.xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Suite1" verbose="1" >
  <test name="Page1"   >
    <packages>
      <package name="testng" /> <!-- 只找有 @Test注解的类/方法 -->
   </packages>
 </test> 
</suite>
 
java org.testng.TestNG testng1.xml [testng2.xml testng3.xml ...]

//如不在xml中配置listener也可在启动时指定 java org.testng.TestNG -listener testng.MyTransformer bin/testng/testng_group.xml
public class MyTransformer implements IAnnotationTransformer {
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if (testMethod!=null && "mongodb".equals(testMethod.getName())) {
			annotation.setInvocationCount(5);
		}
	}
}
public class MyMethodInterceptor implements IMethodInterceptor {
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<IMethodInstance> result = new ArrayList<IMethodInstance>();
		for (IMethodInstance m : methods) 
		{
			Test test = m.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
			if(test ==null)
			{
				test=m.getClass().getAnnotation(Test.class);
				if(test==null)
				{
					result.add(m);
					continue;
				}
			}
			Set<String> groups = new HashSet<String>();
			for (String group : test.groups()) {
				groups.add(group);
			}
			if (groups.contains("fast")) {//一个类中哪个先执行
				result.add(0, m);
			} else {
				result.add(m);
			}
		}
		return result;
	}
}
public class DotTestListener extends TestListenerAdapter  //是ITestListener的子类
{
  private int m_count = 0;
  @Override
  public void onTestFailure(ITestResult tr) {
    log("F");
  }
  @Override
  public void onTestSkipped(ITestResult tr) {
    log("S");
  }
  @Override
  public void onTestSuccess(ITestResult tr) {
    log(".");
  }
  private void log(String string) {
    System.out.print(string);
    if (++m_count % 40 == 0) {
      System.out.println("");
    }
  }
} 
----testng_provider.xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="ProviderSuite">
    <test name="providerTest">
        <groups>
            <run>
                <include name="groupA" /> <!-- groupB(方法test2)不会被测试 -->
            </run>
        </groups>
        <classes>
            <class name="testng.ProviderXmlTest" />
        </classes>
    </test>
</suite>
//只能用testng_provider.xml运行
public class ProviderXmlTest {
    @Test(dataProvider = "dataProvider1", groups = {"groupA"})
    public void test1(int number) {
        Assert.assertEquals(number, 1);
    }
    @Test(dataProvider = "dataProvider1", groups = "groupB")
    public void test2(int number) {
        Assert.assertEquals(number, 2);
    }
    @DataProvider(name = "dataProvider1")
    public Object[][] provideData(ITestContext context) {//一个provide为多个测试方法，参数是ITestContext
        Object[][] result = null;
        System.out.println(context.getName());//providerTest
        for (String group : context.getIncludedGroups()) { //<groups> 下的 <run>下的 <include
            System.out.println("group : " + group);
            if ("groupA".equals(group)) {
                result = new Object[][] { { 1 } };
                break;
            }
        }
        if (result == null) {
            result = new Object[][] { { 2 } };
        }
        return result;
    }
}

public class TestNGAndSelenium {
	WebDriver driver = new FirefoxDriver();

	@BeforeTest
	public void init()// TestNG可以结合Selenium一起用
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://www.baidu.com");
		driver.manage().window().maximize();
	}

	@AfterTest
	public void destory() {
		driver.close();
	}

	@Test
	public void testOper() {
		WebElement search_text = driver.findElement(By.id("kw"));
		WebElement search_button = driver.findElement(By.id("su"));

		search_text.sendKeys("Java");
		search_text.clear();
		search_text.sendKeys("Selenium");
		// search_button.click();//二选一
		search_text.submit();// 同输入回车
	}
}

Selenium and TestNG
----------Selenium  自动化测试
支持C# , Python,
也有 Android Driver 项目selendroid  是ebay的  http://selendroid.io/
也有 iOS Driver   是ebay的 http://ios-driver.github.io/ios-driver/

<dependency>
  <groupId>org.seleniumhq.selenium</groupId>
  <artifactId>selenium-java</artifactId>
  <version>3.141.59</version> <!-- 版本 3.141.59 有 META-INF/versions/9/module-info.class  版本3.14.0就没有-->
</dependency>
  
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>phantomjsdriver</artifactId>
    <version>1.4.4</version>
</dependency>

selenium-java-3.141.59.jar 空的 这个版本没有phantomjsdriver,  3.2.0 有 com.codeborne.phantomjsdriver 
	selenium-api-3.141.59.jar
	selenium-remote-driver-3.141.59.jar
	okhttp-3.11.0.jar
	okio-1.14.0.jar
	commons-exec-1.3.jar
	
	selenium-chrome-driver-3.141.59.jar
	selenium-firefox-driver-3.141.59.jar
	
	selenium-edge-driver-3.141.59.jar
	selenium-ie-driver-3.141.59.jar
	phantomjsdriver-1.4.4.jar (com.codeborne 组  	项目目录下会生成 .log文件)
	selenium-safari-driver-3.141.59.jar

selenium-server-standalone-3.9.1.jar  是为非maven项目，包括全部依赖在一个jar中


源码地址，有API
https://github.com/Selenium/selenium	
 
镜像站点 下载 Selenium
https://npm.taobao.org/mirrors/selenium/
3.9/selenium-server-standalone-3.9.1.jar  

---浏览器驱动
safari driver 
	High Sierra 和更新版本运行  safaridriver --enable 
	
chromedriver 镜像站点
https://npm.taobao.org/mirrors/chromedriver
72.0.3626.7

chromedriver 官方
http://chromedriver.storage.googleapis.com/index.html
72.0.3626.7/

firefox driver v0.23.0
https://github.com/mozilla/geckodriver/releases


edge 17134
https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

ie 3.9.0 (2018-02-05)
http://selenium-release.storage.googleapis.com/index.html

---Selenium IDE 地址官方有，可以录制页面
firefox 
https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/

chrome (chrome web store上)
https://chrome.google.com/webstore/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd


phantomjs 浏览器 
	可以生成页面截图  
	---./snapshot.js
		var page = require('webpage').create();
		var args = require('system').args;

		var url = args[1];
		var filename = args[2] || "snapshot.png";

		var t = new Date();
		page.open(url, function () {
			window.setTimeout(function () {
				page.render(filename);
			console.log('time: '+(new Date() - t));	
				phantom.exit();
			}, 1000);
		});
	//phantomjs.exe --ignore-ssl-errors=yes ./snapshot.js https://www.baidu.com/  c:/tmp/my.png  命令测试OK 
	
将下载的浏览器驱动文件目录放入PATH环境变量中

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
 
//对chrome 放在PATH环境 ,也要加webdriver.chrome.driver 系统属性
//java  -Dwebdriver.chrome.driver=D:\Program\seleniumDriver\chromedriver.exe
// WebDriver driver = new ChromeDriver();//OK
//WebDriver driver = new  FirefoxDriver();//OK firefox命令 要在PATH中可以找到
//WebDriver driver = new   EdgeDriver(); //OK
WebDriver driver = new PhantomJSDriver();//OK 用com.codeborne 组的 phantomjsdriver-1.4.4.jar
//WebDriver driver = new InternetExplorerDriver();//NO 报连接不上端口
//WebDriver driver = new SafariDriver();//未测试

driver.get("http://www.itest.info");
String title = driver.getTitle();
System.out.printf(title);


//		queryElement(driver);//自己的查元素
//		browserOper(driver);//自己的浏览器操作
//		manualInteract(driver);//自己的页面交互
	    waitAjax(driver);//等待,为ajax使用

driver.close();
public static void queryElement(WebDriver driver) 
{
	WebElement element=driver.findElement(By.id("head")); //document.getELementById()
	System.out.println("id="+element.getTagName());
	
	element=driver.findElement(By.name("ie")); //document.getELementByNames()
	System.out.println("name="+element.getTagName());
	
	element=driver.findElement(By.className("fm")); //document.getElementsByClassName()
	System.out.println("class="+element.getTagName());
	
	element=driver.findElement(By.tagName("b"));//document.getELementsByName()
	System.out.println("tag="+element.getTagName());
	
	
	element=driver.findElement(By.xpath("//form[@id='form']/input[@type='hidden' and @name='tn']"));
	System.out.println("xpath="+element.getTagName()); 
	
	//css找不到？？？ 
	
	element=driver.findElement(By.linkText("新闻"));
	System.out.println("linkText="+element.getTagName()); 
	
	element=driver.findElement(By.partialLinkText("地"));
	System.out.println("partialLinkText="+element.getTagName()); 
	
	WebElement size = driver.findElement(By.id("kw"));
	System.out.println("getSize="+size.getSize());

	WebElement text = driver.findElement(By.id("cp"));
	System.out.println("getText="+text.getText());
	
	WebElement ty = driver.findElement(By.id("kw"));
	System.out.println("getAttribute="+ty.getAttribute("type"));
	
	WebElement display = driver.findElement(By.id("kw"));
	System.out.println("isDisplayed="+display.isDisplayed());
		
}
public static void browserOper(WebDriver driver) throws Exception {

	driver.manage().window().maximize();
	Thread.sleep(2000);

	driver.get("https://m.baidu.cn");
	driver.manage().window().setSize(new Dimension(480, 800));
	Thread.sleep(2000);
	
	driver.get("https://www.baidu.com/");
	driver.findElement(By.linkText("新闻")).click();//单击
	System.out.printf("news url  %s \n", driver.getCurrentUrl());
	 
	driver.navigate().back();// 后退
	System.out.printf("back to %s \n", driver.getCurrentUrl());
	Thread.sleep(2000);
   
	driver.navigate().forward(); //前进
	System.out.printf("forward to %s \n", driver.getCurrentUrl());
	Thread.sleep(2000);
	
	driver.navigate().refresh();//刷新
	System.out.printf("refresh page \n" );

}
public static void manualInteract(WebDriver driver) throws Exception {
	driver.get("https://www.baidu.com/");

	WebElement search_text = driver.findElement(By.id("kw"));
	WebElement search_button = driver.findElement(By.id("su"));

	search_text.sendKeys("Java");
	search_text.clear();
	search_text.sendKeys("Selenium");
	//search_button.click();//二选一
	search_text.submit();//同输入回车
	
	
	//鼠标操作
	WebElement search_setting = driver.findElement(By.linkText("设置"));
	Actions action = new Actions(driver);
	action.clickAndHold(search_setting).perform();
/*
	 // 鼠标右键点击指定的元素
	 action.contextClick(driver.findElement(By.id("element"))).perform();

	 // 鼠标右键点击指定的元素
	 action.doubleClick(driver.findElement(By.id("element"))).perform();

	 // 鼠标拖拽动作， 将 source 元素拖放到 target 元素的位置。
	 WebElement source = driver.findElement(By.name("element"));
	 WebElement target = driver.findElement(By.name("element"));
	 action.dragAndDrop(source,target).perform();

	 // 释放鼠标
	 action.release().perform();
  */  
	
	
	//键盘操作
	WebElement input = driver.findElement(By.id("kw"));
	
	//删除多输入的一个
	input.sendKeys(Keys.BACK_SPACE);
	Thread.sleep(2000);

	//输入空格键+“教程”
	input.sendKeys(Keys.SPACE);
	input.sendKeys("教程");
	Thread.sleep(2000);

	//ctrl+a 全选输入框内容
	input.sendKeys(Keys.CONTROL,"a");
	Thread.sleep(2000);

	//ctrl+x 剪切输入框内容
	input.sendKeys(Keys.CONTROL,"x");
	Thread.sleep(2000);

	//ctrl+v 粘贴内容到输入框
	input.sendKeys(Keys.CONTROL,"v");
	Thread.sleep(2000);

	//通过回车键盘来代替点击操作
	input.sendKeys(Keys.ENTER);
	Thread.sleep(2000);

	 //获取第一条搜索结果的标题
	 WebElement result = driver.findElement(By.xpath("//div[@id='content_left']/div/h3/a"));
	 System.out.println(result.getText());

}

public static void waitAjax(WebDriver driver) throws Exception 
{
	 driver.get("https://www.baidu.com");
 
 //显式等待， 针对某个元素等待
	//超时秒数，间隔秒数
	WebDriverWait wait = new WebDriverWait(driver,10,1);//要selenium-support-3.141.59.jar

	wait.until(new ExpectedCondition<WebElement>(){
	  @Override
	  public WebElement apply(WebDriver text) {
			return text.findElement(By.id("kw"));
		  }
	}).sendKeys("selenium");

	driver.findElement(By.id("su")).click();
	Thread.sleep(2000);
	
	
}
