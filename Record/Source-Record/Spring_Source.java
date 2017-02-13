

------
Properties pro=new Properties();
pro.load(LoadProperties2.class.getResourceAsStream("config2.properties"));//文件在类所在包中
//pro.loadFromXML

Array.newInstance(elementClass, 0);//反射数据实例化
Thread.currentThread().getContextClassLoader();
 
System.identityHashCode(obj);

javax.inject.Qualifier
org.springframework.beans.factory.annotation.Qualifier


XX.class.cast();
-------

 AbstractEnvironment 的构造器中 对propertySources属性初始化
 ContextLoader的391行,wac.setConfigLocation();是 AbstractRefreshableConfigApplicationContext中的 调用了resolvePath调用了getEnvironment()

 spring-bean.jar/META-INF/spring.handlers中有配置各名称空间配置的处理类 

org.springframework.cglib.的子包,部分没有源码,这个类是和cglib 中同名的类,包放在spring下了(spring做了变体改变)
AnnotationConfigWebApplicationContext		
XmlWebApplicationContext
ClassPathXmlApplicationContext
AnnotationConfigApplicationContext
eclipse 中右击类名->open type hierarchy   ,可双向 

																							AbstractApplicationContext
																										|
																									/		\
																	AbstractRefreshableApplicationContext	  \
																				|							   \
																(BeanNameAware, InitializingBean)				 \
															AbstractRefreshableConfigApplicationContext			   \
																				|									 \
																			  /   \									  \
								(ConfigurableWebApplicationContext, ThemeSource)	AbstractXmlApplicationContext	  GenericApplicationContext
								 AbstractRefreshableWebApplicationContext 					|							 \
										|													|							  \
									  /	   \												|							   \
AnnotationConfigWebApplicationContext		XmlWebApplicationContext				ClassPathXmlApplicationContext		AnnotationConfigApplicationContext

 

<context-param>
	<param-name>locatorFactorySelector</param-name>
	<param-value>classpath*:beanRefContext.xml</param-value> <!-- 默认值 ,是为一个容器中有两个war包 使用共同的Spring配置 -->
</context-param>
<context-param>
	<param-name>parentContextKey</param-name>
	<param-value>factoryBeanId</param-value>
</context-param>
	
	

DefaultListableBeanFactory 的方法  destroySingleton(String beanName) {
		super.destroySingleton(beanName);// supper 多层继承是 DefaultSingletonBeanRegistry 
DefaultListableBeanFactory 继承自 BeanDefinitionRegistry	   
XmlWebApplicationContext  继承自 ResourceLoader


XmlBeanDefinitionReader  reader.loadBeanDefinitions(configLocation)  用来解析XML配置的bean
	DefaultBeanDefiniationDocumentReader 中用 BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());//readerContext中存reader,reader中存registry即DefaultListableBeanFactory
	
	BeanDefinitionReaderUtils.createBeanDefinition();建立的是 GenericBeanDefinition
	
XmlBeanDefinitionReader 委托 DefaultBeanDefinitionDocumentReader 委托 BeanDefinitionParserDelegate  委托   相应的Parser

parseCustomElement()方法是解析带名称空间的XML,如<mvc: , <contex: 等

	DefaultNamespaceHandlerResolver resolve 方法,	
		Enumeration<URL> urls=classLoaderToUse.getResources("META-INF/spring.schemas");//会读所有.jar包中的有指定的资源URL
			jar:file:/C:/tomcat-7.0.42/webapps/J_Spring4Src-portlet/WEB-INF/lib/spring-aop-4.0.6.RELEASE.jar!/META-INF/spring.schemas
			
			URLConnection con=url.openConnection();
				con.setUseCaches(tru);
 
		
		
		




----个人理解
IOC容器就是 DefaultListableBeanFactory
BeanDefinition 就是保存Bean实例
BeanDefinitionReader 从 Resource  读 转 BeanDefinition 存入 DefaultListableBeanFactory 

Bean 实例化
refresh->finishBeanFactoryInitialization->beanFactory.preInstantiateSingletons()
BeanWrapperImpl来封装bean实例

-------spring mvc
入口类  spring-web.jar/META-INF/services/javax.servlet.ServletContainerInitializer 文件中写的类是 SpringServletContainerInitializer
	@HandlesTypes(WebApplicationInitializer.class) 表示所有实现接口的类都 传入方法 onStartup(Set<Class<?>> webAppInitializerClasses 	
	
	共有3个,回全是abstract的不做任何事
	AbstractDispatcherServletInitializer
	AbstractAnnotationConfigDispatcherServletInitializer
	AbstractContextLoaderInitializer

	ContextLoaderListener 类的 contextInitialized方法  (portlet也要 web.xml  配置ContextLoaderListener)

	
	
 
org.springframework.web.context.ContextLoader 类的
	 String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
	//String CONTEXT_CLASS_PARAM = "contextClass";
	
	 
	 String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";  
	//org.springframework.web.context.ContextLoader.properties 文件中配置 XmlWebApplicationContext
	ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, ContextLoader.class);
	

		String LOCATOR_FACTORY_SELECTOR_PARAM = "locatorFactorySelector";
		String LOCATOR_FACTORY_KEY_PARAM = "parentContextKey";
				
		String locatorFactorySelector = servletContext.getInitParameter(LOCATOR_FACTORY_SELECTOR_PARAM);
		String parentContextKey = servletContext.getInitParameter(LOCATOR_FACTORY_KEY_PARAM);
		
		String CONTEXT_ID_PARAM = "contextId";
		sc.getInitParameter(CONTEXT_ID_PARAM);
		
		GLOBAL_INITIALIZER_CLASSES_PARAM="globalInitializerClasses"  //要求是  ApplicationContextInitializer<ConfigurableApplicationContext>
		servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM)
		
		CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses"   // 要求是 ApplicationContextInitializer 接口
		servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);
		 
		
		 ServletContext    sc.getInitParameter(CONFIG_LOCATION_PARAM);//CONFIG_LOCATION_PARAM = "contextConfigLocation";
---		 wac.refresh(); 到 AbstractApplicationContext 的refresh() 有很多步 是ClassPathXmlApplicationContext 的父类 
		

	
	
<context-param>
	<description>FrameworkServlet applyInitializers use this</description>
	<param-name>globalInitializerClasses</param-name>
	<param-value>_read.spring.test.web.MyApplicationContextInitializer</param-value>
</context-param>

public class MyApplicationContextInitializer implements  ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		System.out.println("-------MyApplicationContextInitializer-----"+applicationContext.getClass().getName()); //XmlWebApplicationContext
		for( String name : applicationContext.getBeanDefinitionNames())
		{
			System.out.println(name);
		}
	}
}


------portlet spring mvc 


------junit test  
入口类 SpringJUnit4ClassRunner extends BlockJUnit4ClassRunner 调用顺序
	1. SpringJUnit4ClassRunner(Class<?> clazz)构造器	
			TestContextManager 构造器注册了很多的Listener,  DependencyInjectionTestExecutionListener
	2. run(RunNotifier notifier) //是否运行,对类级配置@IfProfileValue
	3. withBeforeClasses(Statement statement)
	4. withAfterClasses(Statement statement)
	5. 自己的测试类 @BeforeClass
	
	//--多个
	6. runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) //是否运行,方法级对配置@Ignore,@IfProfileValue
	7. methodBlock(FrameworkMethod frameworkMethod)  //注册了createTest()方法
		createTest()  ----执行注册的Listener, 调用了refresh()方法
		possiblyExpectingExceptions(
		withBefores(
		withAfters(
	    withPotentialTimeout(
	8. 自己的测试方法
    //--多个
	
 	9. 自己的测试类 @AfterClass
	
	
	DependencyInjectionTestExecutionListener
		DelegatingSmartContextLoader 委托 SmartContextLoader 决定是XML的 (GenericXmlContextLoader)  还是Annotation的 (AnnotationConfigContextLoader)
		
	
AnnotatedBeanDefinitionReader 的构造器中有 registerAnnotationConfigProcessors 注册了很多 PostProcessors
	ConfigurationClassPostProcessor用来处理@Configuration标记的类

