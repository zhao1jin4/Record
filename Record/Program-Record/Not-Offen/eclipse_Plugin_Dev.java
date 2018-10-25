http://www.cnblogs.com/liuzhuo/
http://www.cnblogs.com/liuzhuo/archive/2010/08/13/eclipse_plugin_1_0_2.html


在SWT基础上又创建了一个更易用、功能强大的图形包JFace
OSGi 4.2 规范,eclipse 插件开发,有一个项目叫Equinox 
Open Services Gateway Initiative (OSGi)

JBoss 也有实现 OSGi
CXF 有分布式的 OSGi实现




插件为了自身能够对其他插件进行扩展而提出了扩展点的概念
plugin.xml
<extension point="被使用扩展点的ID">

Eclipse平台提供的扩展点有
    增加菜单项
    增加视图
    增加编辑器
等等，数量非常的多


//取得工作台
IWorkbench workbench = PlatformUI.getWorkbench();
//取得工作台窗口
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//取得工作台页面
IWorkbenchPage page = window.getActivePage();
//取得当前处于活动状态的编辑器窗口
IEditorPart part = page.getActiveEditor();


对工作区的访问需要使用org.eclipse.core.resources.ResourcesPlugin


//取得工作区的root
IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
//取得项目
IProject[] projects = wsroot.getProjects();









eclipse中建立 Plug-in Development/[Plug-in Project]  在project name:中输入org.zh.myplugin是插件ID->下一步
输入版本号->下一步->选择一个模板[HelloWorld]->下一步->输入包名,类名
会自动生成 META-INF/MANIFEST.MF,build.properties,pluin.xml

MANIFEST.MF文件overview标签中在testing区中单击[Lanch an Eclipse application],也可点最上方的启动按钮,会启动一个新的eclipse后多了一个菜单

有很多的视图



getDefault() 	取得插件类的实例的方法。插件类是单例的，所以这个方法作为一个静态方法提供。
start() 	插件开始时的处理。
stop() 	插件停止时的处理。
getLog() 	log输出时取得ILog用的方法。
getImageRegistry() 	取得管理插件内图像的ImageRegistry类。
getPerferenceStore() 	取得保存插件设定的IPerferenceStore类。
getDialogSettings() 	取得保存对话框设定的IDialogSettings类。
getWorkbench() 	取得IWorkbench的实例


-----------
插件开发 Helloworld示例,Jboss OSGi文档中的
eclipse new project->plug-in project->单择an OSGi Framework,再下拉选择standard->在templates中选择 Hello OSGi Bundle

生成类
package myplugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
public class MyActivator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello World!!");
	}
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye World!!");
	}
}
META-INF/MANIFEST.MF文件内容 
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: Myplugin
Bundle-SymbolicName: myplugin
Bundle-Version: 1.0.0.qualifier
Bundle-Activator: myplugin.MyActivator
Import-Package: org.osgi.framework;version="1.3.0"
Bundle-RequiredExecutionEnvironment: JavaSE-1.7

File->export ->plug-in development中的Deployable plugins and fragments ->选择导出目录,就可生成 plugins\myplugin_1.0.0.[时间].jar

--
==================Apache Felix 是OSGI容器
cd felix-framework-4.2.1  必须在这个目录中
java -jar bin/felix.jar  默认会在当前目录建立felix-cache目录 ,也在参数中加目录名

felix-framework-4.2.1\conf\config.properties 中也可打开 #felix.cache.rootdir=${user.dir}
help lb  查看帮助

felix:install file:D:/temp/osgi/example1.jar  安装bundle
felix:start file:D:/temp/osgi/example1.jar
felix:stop
felix:update file:D:/temp/osgi/example1.jar
felix:uninstall file:D:/temp/osgi/example1.jar



----
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceEvent;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceEvent;
public class ActivatorListener implements BundleActivator, ServiceListener //实现ServiceListener接口也会监听其它bundler的registerService
{
    public void start(BundleContext context)
    {
        System.out.println("Starting to listen for service events.");
        context.addServiceListener(this);
    }
    public void stop(BundleContext context)
    {
        context.removeServiceListener(this);//可以不做,框架会自动做
        System.out.println("Stopped listening for service events.");
    }
    public void serviceChanged(ServiceEvent event)
    {
        String[] objectClass = (String[])event.getServiceReference().getProperty("objectClass");//固定

        if (event.getType() == ServiceEvent.REGISTERED) //对应context.registerService(
        {
            System.out.println(
                "Ex1: Service of type " + objectClass[0] + " registered.");
        }
        else if (event.getType() == ServiceEvent.UNREGISTERING)
        {
            System.out.println(
                "Ex1: Service of type " + objectClass[0] + " unregistered.");
        }
        else if (event.getType() == ServiceEvent.MODIFIED)
        {
            System.out.println(
                "Ex1: Service of type " + objectClass[0] + " modified.");
        }
    }
}

--xx.jar/MEAT-INF/manifest.mf 
Bundle-Name: ActivatorListener example
Bundle-Description: A bundle that displays messages at startup and when service events occur
Bundle-Vendor: Apache Felix
Bundle-Version: 1.0.0
Bundle-Activator: osgi_felix.ActivatorListener
Import-Package: org.osgi.framework
这个文件最后一定要有一个空行(最后一行被忽略),Import-Package多个用,分隔




felix:install file:D:/temp/osgi/listener.jar
felix:start file:D:/temp/osgi/listener.jar  也可用 lb 看自己名字对应的ID ,再用  felix:start <ID号>
felix:update 12  ,可以看到stop方法的调用

public class ActivatorService implements BundleActivator
{ 
    public void start(BundleContext context)
    {
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");//为客户端查询使用
        ServiceRegistration  m_reg = context.registerService(  DictionaryService.class.getName(), new DictionaryImpl(), props); //
		//m_reg.unregister();
    }
}
package osgi_felix.service;
public interface DictionaryService
{
}
MANIFEST.MF文件中加
Export-Package: osgi_felix.service

----
import org.osgi.util.tracker.ServiceTracker;
public class ActivatorClient implements BundleActivator
{
     
    public void start(BundleContext context) throws Exception
    {
       ServiceReference[] refs = context.getServiceReferences( DictionaryService.class.getName(), "(Language=*)");
	   DictionaryService dictionary = (DictionaryService) context.getService(refs[0]);//客户端在使用时服务服务突然unregister会有问题,不好测试
	   //---可用
	   ServiceTracker  m_tracker = new ServiceTracker( m_context,
			m_context.createFilter( 
				"(&(objectClass=" + DictionaryService.class.getName() + ")" + "(Language=*))"
				),null);
        m_tracker.open();
		DictionaryService dictionary = (DictionaryService) m_tracker.getService();//客户端在使用时服务服务突然unregister会自动得到服务,不好测试
		if (dictionary == null)
		{
			System.out.println("No dictionary available.");
		}
				
	}
}
MANIFEST.MF文件中修改
Import-Package: org.osgi.framework,osgi_felix.service,org.osgi.util.tracker

BundleContext m_context;
ServiceReference m_ref;
m_context.addServiceListener(this, "(&(objectClass=" + DictionaryService.class.getName() + ")" + "(Language=*))"); //只对特定的服务做监听
m_context.ungetService(m_ref);//取消服务

================== 上Felix