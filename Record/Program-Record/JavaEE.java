https://javaee.github.io/glassfish/documentation Java EE 8 many
	https://javaee.github.io/tutorial/toc.html  不能下载 对应 代码在 https://github.com/javaee/tutorial-examples
	https://javaee.github.io/javaee-spec/javadocs/  JavaEE 8 API

<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>


==================================Servlet
HttpServlet//单实例,最好不要加属性,要用synchroized 或 final的常量
public void init(ServletConfig config) {//如不加load-on-startup,第一次请求时初始化 
	ServletContext contex=config.getServletContext();
	String contexParam=contex.getInitParameter("life-name");
}
public void destroy() {}
//service方法默认实现是来决定调用doGet还是doPost方法

//禁用Cookie,IE中tools->internet options->privacy->advanced->选中override automatic cookie handling,再选两个block
//对localhost无效,要使用本机IP
HttpSession session=request.getSession();
Object sessionObj=session.getAttribute("sessionObj");
if(sessionObj==null)
{
	MySessionUser u=new MySessionUser();
	session.setAttribute("sessionObj", u);
}
//如有禁用Cookie使用response.encodeURL("");会自动加jsessionid的参数
request.getRequestDispatcher(response.encodeURL("/ok.jsp")).forward(request, response);
<form action="<%=response.encodeURL("life")%>"   ,危险可以把链接发给其它去点
		
if("POST".equals(request.getMethod()))
	request.setCharacterEncoding("UTF-8");//对<form method="post"生效,如是get无效,

String username=request.getParameter("username");
if("GET".equals(request.getMethod()))
	username=new String(username.getBytes("iso8859-1"),"UTF-8");
//javac -encoding GBK XX.java

session.isNew();
session.getId();//jsessionid为键的值 ,<form action="abc;jsessionid=xxx"
session.getCreationTime();
session.getLastAccessedTime();
session.getMaxInactiveInterval();
session.invalidate();//注销
		
response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"系统繁忙");//SC=Server Code
response.sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION,"未授权");
response.sendError(HttpServletResponse.SC_NOT_FOUND,"找不到页");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"出现错误");
response.sendRedirect("/ok.jsp");
Cookie cookie = new Cookie("cookiename","cookievalue");
response.addCookie(cookie);

//动态网页中禁止缓存
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

request.getRequestDispatcher("/ok.jsp").forward(request, response);//后面的不会被执行
request.getRequestDispatcher("/ok.jsp").include(request, response);
request.getCookies();
	
PrintWriter writer =response.getWriter();
writer.println("<h2>中国</h2>");
writer.close();//如是被其它Servlet,调用request.getRequestDispatcher("/ok.jsp").include(),就不要关闭


其它的Listener
ServletContextListener
ServletContextAttributeListener
ServletRequestAttributeListener
ServletRequestListener
HttpSessionAttributeListener	attributeAdded,attributeRemoved,attributeReplaced
HttpSessionListener  sessionCreated,sessionDestroyed

Listener->Filter->Servlet(load-on-startup)加载顺序

==================================Servlet 3.0

@WebServlet(urlPatterns={"/page1","page2"}, initParams = {@WebInitParam(name = " default_market " ,  value = " NASDAQ " )})
//@WebInitParam(name = " default_market " , value = " NASDAQ " )
public class MyServlet3 extends HttpServlet {}

<servlet-class>
	<async-supported>true</async-supported>
或者
@WebServlet( asyncSupported=true

AsyncContext ctx=request.startAsync(); //走的Filter也要异步支持
ctx.addListener(new AsyncListener()
				{
					public void onComplete(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onComplete  ");
					}
					public void onError(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onError ");
					}
					public void onStartAsync(AsyncEvent event) throws IOException
					{
						System.out.println("MyAsyncServlet onStartAsync");
					}
					public void onTimeout(AsyncEvent event) throws IOException 
					{
						System.out.println("MyAsyncServlet onTimeout");
					}  
				});
context.complete();//会调用  Listener的complete方法



@WebListener
public class MyContxtListener3 implements ServletContextListener 
{
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context=event.getServletContext();
		
		//动态注册Servlet 
		ServletRegistration.Dynamic dynServ=context.addServlet("myServName", MyServlet3.class);
		dynServ.setInitParameter("myparam", "myvalue");//servlet重写init方法，用ServletConfig取
		dynServ.addMapping("/dynServ","/dynServ2");
		dynServ.setAsyncSupported(true);
		
		//动态注册Filter
		FilterRegistration.Dynamic  dynFilter=context.addFilter("myFileterName",MyFilter3.class); 
		dynFilter.setAsyncSupported(true);
		dynFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST) , true, "/async");//true=isMatchAfter 
		
 	}
}

@WebFilter(filterName = "myFilter", urlPatterns = { "/dynServ","/test1" },asyncSupported=true)
public class MyFilter3 implements Filter{}

文件上传的原生支持
@MultipartConfig(maxFileSize=50*1024*1024) //用于支持文件上传 enctype="multipart/form-data"

req.setCharacterEncoding("UTF-8");

Collection<Part> parts=req.getParts();//包括表单参数 <input type="file" multiple />
for(Part file1:parts) 
{
			String fname=file1.getSubmittedFileName();
			if(fname==null)//表单域
				continue;
}

Part file1=req.getPart("attache1");//是<input type=file name="attache1" 
String name=file1.getName();//attache1 
String pairs=file1.getHeader("content-disposition");
String nameHeader="filename=";
String path=pairs.substring(pairs.indexOf(nameHeader)+nameHeader.length()+1,pairs.lastIndexOf("\""));
if(!"".equals(path))
{
	String filename=path.substring(path.lastIndexOf("\\")+1);//" 只IE是带C:\ 和req.setCharacterEncoding("UTF-8")中文 OK
	file1.write("d:/temp/"+filename);//一个Part要调用一次write
}

InputStream input=file2.getInputStream();
String param=req.getParameter("username");
System.out.println(new String(param.getBytes("iso8859-1"),"UTF-8"));//中文 OK
----无web.xml
public interface WebParameter {    
    public void loadInfo(ServletContext servletContext) throws ServletException;    
}  
public class ServletParameter implements WebParameter {    
    @Override    
    public void loadInfo(ServletContext servletContext) throws ServletException {    
        ServletRegistration.Dynamic testServlet=servletContext.addServlet("test","servlet3_new.nowebxml.TestServlet");    
        testServlet.setLoadOnStartup(1);    
        testServlet.addMapping("/nowebxml");    
    }    
}
//(不能是web项目的META-INF) 是classpath 或者 WEB-INF/lib/xxx.jar/META-INF/services/javax.servlet.ServletContainerInitializer 中写实现 implements ServletContainerInitializer 全类名
@HandlesTypes(WebParameter.class)    
public class WebConfiguration implements ServletContainerInitializer {    
    @Override    
    public void onStartup(Set<Class<?>> webParams, ServletContext servletCtx)throws ServletException 
   { //webParams 的值为项目中所有实现  @HandlesTypes(WebParameter 的类
        if (webParams != null)
        {    
            for (Class<?> paramClass : webParams) 
            {    
                if (!paramClass.isInterface() && !Modifier.isAbstract(paramClass.getModifiers()) &&    
                        WebParameter.class.isAssignableFrom(paramClass)) //是多余的判断
                {    
                    try {    
                        ((WebParameter) paramClass.newInstance()).loadInfo(servletCtx);    
                    }    
                    catch (Throwable ex) {    
                        throw new ServletException("Failed to instantiate WebParam class", ex);    
                    }    
                }    
            } 
        } 
    } 
}

x.jar/META-INF/resources/ 被视为web根目录,如下
x.jar/META-INF/resources/WEB-INF/web.xml
x.jar/META-INF/resources/WEB-INF/web-fragment.xml  即 web.xml的模块化
	根元素为<web-fragment>
	<name></name> <!-- 表示模块名称  -->
	  <ordering>
              <before>
                    <others/>   <!-- 表示第一个加载 -->
              </before>
			<!--  <after><name>A</name></after> 表示比A后面加载  -->
       </ordering>
	   
 

==================================Servlet 4 要 Tomcat 9
Tomcat 9 配置打开HTTP 2 

tomcat9/logs/localhost_access_log.x.txt  显示是 HTTP/2.0
Chrome network 标签打开protocol列值是h2
//Chrome工具 自带的工具 chrome://net-internals/  -> HTTP/2->点表格中的ID列的值->复选一行
 看报文    HTTP2_SESSION_RECV_PUSH_PROMISE 有 
	  --> id = 1
      --> promised_stream_id = 2
	  
//---PushBuilder
PushBuilder pushBuilder=req.newPushBuilder();//如果不支持返回null
if (pushBuilder != null)
{  //push 非阻塞
   pushBuilder.path("img/bing.png").push();
   pushBuilder.path("js/md5.js").push(); 
} 
getServletContext().getRequestDispatcher("/servlet4.jsp").forward(req, resp);

//servlet4.jsp 中有引用  img/bing.png 和 js/md5.js 文件

//---HttpServletMapping
@WebServlet({"/path/*", "*.ext"})

 HttpServletMapping mapping = request.getHttpServletMapping();
MappingMatch extension= MappingMatch.EXTENSION;
MappingMatch path= MappingMatch.PATH;
String map = mapping.getMappingMatch().name();
//如请求是 path/servlet4 值是PATH
//如请求是 servlet4.ext 值是EXTENSION

String value = mapping.getMatchValue();
//如请求是 path/servlet4 值是 servlet4
//如请求是 servlet4.ext  值是 servlet4

String pattern = mapping.getPattern();
//如请求是 path/servlet4 值是 /path/*
//如请求是 servlet4.ext 值是*.ext

String servletName = mapping.getServletName();//servlet4_new.Servlet4Mapping

--servlet 4 other
新加两个类 HttpFilter extends GenericFilter //看源码其实很简单


int sessionTimeout=req.getServletContext().getSessionTimeout();
req.getServletContext().setSessionTimeout(sessionTimeout+10);

String enc=req.getServletContext().getRequestCharacterEncoding();
req.getServletContext().setRequestCharacterEncoding("UTF-8");

req.getServletContext().addJspFile("servletName", "/jspFile");
ServletRegistration reg= req.getServletContext().getServletRegistration("servletName");//servlet 3
reg.getInitParameters();
reg.getMappings();





==================================HTTP 断点继传 (下载)
服务器返回的HTTP头有 Accept-Ranges=bytes
浏览器请求的HTTP头有 RANGE: bytes=2000070-
服务器返回的HTTP头 是206 ,Content-Range=bytes 2000070-106786027/106786028

HttpURLConnection  httpConnection.setRequestProperty("RANGE","bytes=2000070");
RandomAccessFile  seek(2020) ,read,write
--
下载文件设置HTTP头,文件名
response.reset();
response.setContentType("application/msexcel");//tomcat的conf/web.xml中
response.setHeader("Content-disposition","inline;filename=workbook.xls");//inline显示在浏览器中

response.setContentType("application/x-msdownload");
response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));//attachment会提示下载
ServletOutputStream output=response.getOutputStream();
output.flush();
output.close();//可能不要关闭

=================================java mail
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>javax.mail-api</artifactId>
    <version>1.6.2</version>
</dependency>

<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>javax.activation-api</artifactId>
    <version>1.2.0</version>
</dependency>

以上只是能编译，不能运行，如要运行，下面中的内容也包括上面的-api中的东西
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>javax.mail</artifactId>
    <version>1.6.2</version>
</dependency>

<dependency>
    <groupId>com.sun.activation</groupId>
    <artifactId>javax.activation</artifactId>
    <version>1.2.0</version>
</dependency>




发送邮件服务器
	SMTP:Simple Mail Transfer Protocol
接收邮件服务器 
	POP3 :  Post Office Protocol 3，不存放在服务器上，不可在线阅读邮件服务器上的邮件
	IMAP :Internet Message Access Protocol   可在线阅读邮件服务器上的邮件，存储在服务器上

	POP3默认端口110 SMTP默认端口25 IMAP默认端口143
	
	
cmd命令  telent smtp.163.com 25 
	以下不能输错,不能贴粘.
	
	HELO smtp.163.com 	命令 
	250 hz-b-163smtp1.163.com 9561591f-d7ff-4bd5-876a-9fefcf7846e5  返回250表示OK 
	auth login 			命令 
	334 VXNlcm5hbWU6 9561591f-d7ff-4bd5-876a-9fefcf7846e5 
	USER emhhbzFqaW40	 命令 是用户名的Base64 3
	PASS xxxxxx 		命令 是密码的Base64,这里就过不去了!!!!
	后面还有　 
	MAILFROM:XXX@163.COM 
	RCPTTO:XXX@163.COM 
	DATA 
	354 End data with .
	QUIT
	--------另一套
	 EHLO zhaopinpc
	 250-hz-b-163smtp2.163.com
	 250-mail
	 250-PIPELINING
	 250-8BITMIME
	 250-AUTH LOGIN PLAIN
	 250-AUTH=LOGIN PLAIN
	 250 SARTTLS 1ad55c7f-d850-4a65-8d61-3ba8f024113f
	 AUTH LOGIN

	 

javaMail中的URLName(String)
	格式为：   协议名称：//用户名：密码@邮件服务器/
pop3://username:password@163.com/
	 

	 
javax.mail.Session类 getStore("imap")//imaps,pop3,pop3s
下为抽象类
javax.mail.Store 有一个static getFolder()
		.connect(host,usrname,password)
javax.mail.Folder 
	getFolder("xx")
	open(Folder.READ_WRITE)

	getMessageCount() 共有邮件数量
	getUnreadMessageCount()//未读邮件

javax.mail.Message 的子类MimeMessage(Session ) 如mes
		setRecipients(Message.RecipientsType.TO,IternetAddress[] xx)	
				Message.RecipientsType.CC(抄送) BCC密送
		setFrom()
		setText()
		setSubject()
	Transport.send(mes)


javax.mail.Address 的子类在internet包下的 InternetAddress
javax.mail.Transport 发送 static send(Message)


BodyPart 的.getDispostion 如返回是Part.ATTACHMENT或是Part.INNLINE 说明是附件,如是null是正文


javax.mail.Authenticator 抽象类只有getPasswordAuthentication()方法可被 重写
	protected javax.mail.PasswordAuthentication getPasswordAuthentication()
	{ 
		return new javax.mail.PasswordAuthentication(user,password); 
	}


javax.mail.Session 的getDefalutInstance(Properties,Autenticatior)
			
			getnstance(Properties,Autenticatior)

MimeMessge构造方法(Session)
InternetAddress.parse(String 用逗号分隔（comma）如第二参数为true则可以用空格分隔) 返回一个InternetAddresss[]
MimeMessage的setRecipients(Message.RecipientType.TO,Address[]) Address的子类是InternetAddress
	     setRecipients(Message.RecipientType.TO,String address)
		setSubject("")
		setFrom(Address)
		setText("  ")



javax.mail.Transport是抽象类有一个static send(Message子类MimeMessage)

MimeBodyPart
可以通过Session对象的getTransport
java.util.ResourceBundle.getBundle("basename",Locale.SIMPLIFIED_CHINESE)


Javax.mail.internet.MimeMessage
	InternetAddress的.getType()返回在jar包下的javamail.default.address.map文件中的key  (值是协议)
	MerakMail软件
	
	  Properties  props = System.getProperties();
  props.put("mail.transport.protocol", "smtp");
  props.put("mail.store.protocol", "imap");
  props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
  props.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");
  props.put("mail.smtp.host", hostname);

  
  
示例
	static Properties prop=	new Properties();
	static{
		try {
			prop.load(new FileInputStream("C:/temp/mail.properties"));
		} catch ( Exception e) {
			e.printStackTrace();
		}  
	}
	public static final String username=prop.getProperty("username");  // 是@sina.com 前面的部分
	public static final String password=prop.getProperty("password"); 
	public static final String mailTo =prop.getProperty("mailTo");   // 带@的
	public static final String filterFromMailAddr=prop.getProperty("filterFrom");//xx@sina.com


	//public static final String fromAddrHost="163.com";
	//public static final String smtpHost="smtp.163.com";
	
	public static final String pop3Host="pop3.163.com";
	public static final String imapHost="imap.163.com";
	
		//用于发件人 user1@ 后面部分
	public static final String fromAddrHost="dell-pc.domain.com";//dell-pc.domain.com是成功的，但改为domain.com能发送（两个证书是按domain.com做），但收不到？
	//看postfix的日志tail -f /var/log/mail，是因为没有DNS解析domain.com （使用公网的是错的）的MX记录为 dell-pc.domain.com 
	
	
	public static final String smtpHost="dell-pc.domain.com";
	
	
	public static void receivePop3Mail() throws Exception
	{
		Properties props = new Properties();
		Session recesession = Session.getInstance(props, null);
		recesession.setDebug(true);
		Store store = recesession.getStore("pop3");
		store.connect(pop3Host, username, password);
		
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);
		int count = inbox.getMessageCount();

	 
		// 设置过滤规则，对接收的e-mail进行过滤，
		SearchTerm st = new OrTerm(new SubjectTerm("笔记"), new FromStringTerm(filterFromMailAddr));//OrTerm 是或的原则
		Message[] filtermsg = inbox.search(st);
		// 对被过滤出的e-mail设置删除标志
		for (int i = 0; i < filtermsg.length; i++) 
		{
			Message msg = filtermsg[i];
			if (msg != null) 
			{
				// 得到被过滤出的e-mail的标题
				String filterTitle = msg.getSubject();
				System.out.println("被过滤的邮件:" + filterTitle);
				
				// 设置删除标记
				msg.setFlag(Flags.Flag.DELETED, true);//修改进入[已删除]列表,但不是在邮箱中是已删除???
			}
		}
		System.out.println("收件箱中总共有" + (count - filtermsg.length) + "封e-mail");
		 
		
		//列表显示出来
	    for(int j=1;j<=count;j++)
	    {
		      Message message=inbox.getMessage(j);
		      //如果不是待删除的e-mail就显示出来
		      if(message.isSet(Flags.Flag.DELETED))
		    	  continue;
		      
	         String title=message.getSubject();
	         System.out.println("---------邮件标题:"+title);
	         //------------邮件细节
	         Address[] address=message.getFrom();//javax.mail.internet.InternetAddress
	         if(address!=null)
	        	  for(int i=0;i<address.length;i++)
	            	 System.out.print("发件人:"+((InternetAddress)address[i]).getAddress());
	         
	         Date sentdate=message.getSentDate();
	         if(sentdate!=null)
	        	 System.out.print("发出的时间:"+sentdate.toString());
	         
	         address=message.getRecipients(Message.RecipientType.TO);
	         if(address!=null)
	            for(int i=0;i<address.length;i++)
	            	 System.out.print("收件人:"+address[i]);
	         
	         address=message.getRecipients(Message.RecipientType.CC);
	         if(address!=null)
	            for(int i=0;i<address.length;i++) 
	            	System.out.print("抄送人:"+address[i]);
	         
	        //如果是一个多部分内容的e-mail
	        if(message.isMimeType("multipart/*"))
	        {
	           //获得代表该e-mail的多部分内容的Multipart对象
	           Multipart multipart = (Multipart)message.getContent();
	           //依次获取Multipart对象的每个部分
	           for(int i = 0;i < multipart.getCount();i++)
	           {
	             //得到每个部分的属性
	             Part p = multipart.getBodyPart(i);
	             String disposition = p.getDisposition();
	             
	             //如果该部分中是附件内容，则输出该附件的下载链接
	             if ((disposition != null) &&(disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE)))
	             {
	                String filename=p.getFileName();
	                filename=javax.mail.internet.MimeUtility.decodeText(filename);//中文OK
	                System.out.println("符件:"+filename+",mime:"+p.getContentType());
	   	         
	                int num=message.getMessageNumber();
	                System.out.println("邮件索引:"+num);

	                InputStream input=p.getInputStream();//下载附件
	              }else if(disposition==null)
                 {
                    if(p.isMimeType("text/plain"))
                    {
                    	  System.out.print("只处理的文本:"+p.getContent());
                    }else   if(p.isMimeType("text/html"))
                    {   
                    	System.out.println("=====HTMLcontent:"+p.getContent());
                    }else//如type:multipart/alternative
                    {
                    	System.out.println("=====not text/plain or text/html ===type:"+p.getContentType()+"\n----content:"+p.getContent());
                    }
                 }
               }
	        }else if(message.isMimeType("text/plain"))  //如果是普通文本形式的e-mail，则显示其详细内容
            {
            	System.out.print("邮件文本:"+message.getContent());
            }else//几乎没很少有这种情况
            {
            	System.out.print("========other ContentType:"+message.getContentType());
            }
         } 
 		inbox.close(true);
 		store.close();
	}
	public static void receiveIMAPMail() throws MessagingException 
	{
		Properties props = System.getProperties();
		Session sess = Session.getInstance(props, null);
		sess.setDebug(true);
		Store st = sess.getStore("imap");//还可是 imaps
		st.connect(imapHost, username, password);
		
		Folder fol = st.getFolder("INBOX");
		if (fol.exists())
		{
			for (Folder f : fol.list()) 
			{
				System.out.printf("-----box:%s", f.getName());//只有一个INBOX
			}
			fol.open(Folder.READ_ONLY);
			Message[] msgs=fol.getMessages();
			for (Message m : msgs) 
			{
				System.out.printf("-----/n来自%s /n标题%s/n大小%d/n",convertAddress2String(m.getFrom()), m.getSubject(), m.getSize());
			}
			fol.close(false);
		} 
		st.close();
	}
	public static void sendHTMLAttachmentMail() throws MessagingException // TestOK
	{
		String subject = "subject from javamail 标题";
		String bodyText = "hello !,this is java mail test body 正文.";
		String attachment = "c:/temp/图片.jpg";
		boolean isSendAttach=true;

		
		Properties props = new Properties();
		Session sendsession = Session.getInstance(props, null);
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.auth", "true");// 设置SMTP服务器需要权限认证
		
		/*
---自己的postfix
/etc/postfix/master.cf 文件中放开 
smtps     inet  n       -       n       -       -       smtpd 
  -o smtpd_tls_wrappermode=yes
  -o smtpd_sasl_auth_enable=yes

还要在 /etc/services 中增加 
smtps              465/tcp  

再重启postfix就有465端口监听了

---
生成根证书

	1.首先生成私钥
	openssl genrsa -out root.key 2048
	2.生成根证书签发申请文件(csr文件) , #CN的值要与请求地址相同的域名
	openssl req -new -key root.key -out root.csr  -subj /C=CN/ST=Shanghai/L=JiaDing/O=DevOps/CN=domain.com 
	3.自签发根证书(cer文件)
	openssl x509 -req -days 3000 -sha1 -extensions v3_ca -signkey root.key -in root.csr -out root.cer

使用根证书签发服务端证书
	mkdir private
	1.生成服务端私钥(可以给商户客户)
	openssl genrsa -out private/server-key.pem 2048
	2.生成证书请求文件
	openssl req -new -key private/server-key.pem -out private/server.csr -subj /C=CN/ST=Shanghai/L=JiaDing/O=DevOps/CN=domain.com 
	3.用根证书签发服务端证书(此时证书只有公钥，没有私钥)
	openssl x509 -req -days 3000 -sha1 -extensions v3_req -CA root.cer -CAkey root.key -CAserial ca.srl -CAcreateserial -in private/server.csr -out private/server.cer

#cer转换为der格式
openssl x509 -outform der -in test-private/server.cer -out private/publicserver.ccertificate.der

#转换为p12/pfx格式
openssl pkcs12 -export -in private/server.cer -inkey private/server-key.pem -out server.pfx  #要设置密码123

#私钥导出公钥
openssl rsa -in private/server-key.pem -pubout -outform PEM -out server-key-pub.pem
---
vi /etc/postfix/main.cf  TLS修改

smtpd_use_tls = yes  #开关 
#是smtpd服务端
#是rsa
smtpd_tls_key_file = /home/dell/Documents/postfix_tls/private/server-key.pem   
smtpd_tls_cert_file = /home/dell/Documents/postfix_tls/private/server.cer
smtpd_tls_CAfile = /home/dell/Documents/postfix_tls/root.cer
smtpd_tls_CApath =


转换 pfx 到 jks 
keytool -importkeystore -v  -srckeystore  /home/dell/Documents/postfix_tls/server.pfx   -srcstoretype pkcs12 -srcstorepass 123   -deststoretype jks  -destkeystore serverTruststore.jks   -deststorepass servertruststorepass
 	
 测试成功 连接自己的postfix,配置了tls成功
		*/
		props.put("mail.smtp.ssl.enable", "true");//TLS
		props.setProperty("mail.transport.protocol", "smtps");
		//启动服务器时 -D
		System.setProperty("javax.net.ssl.trustStore",			"/home/dell/Documents/postfix_tls/serverTruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword",	"servertruststorepass");
		
		
		//mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
		//mail.smtp.socketFactory.port=
		sendsession.setDebug(true);
		Message message = new MimeMessage(sendsession);
		message.addHeader("Content-type", "text/html");//对HTML格式的邮件
		
		message.setFrom(new InternetAddress(username + "@" + fromAddrHost));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
		message.setSubject(subject);
		message.setSentDate(new Date());

		if (isSendAttach) 
		{
			// 建立第一部分：文本正文
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8><div style='color:red;font-size:50px'>" + bodyText+"</div>", "text/html;charset=UTF-8");//对HTML格式的邮件
			// 建立多个部分Multipart实例
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			
			
			// 建立第二部分：附件
			messageBodyPart = new MimeBodyPart();
			// 获得附件
			DataSource source = new FileDataSource(attachment);
			// 设置附件的数据处理器
			messageBodyPart.setDataHandler(new DataHandler(source));
			// 设置附件文件名
			
			String fileName=source.getName(); 
			try {
				fileName=MimeUtility.encodeText(fileName);//在收件箱中的中文OK
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			messageBodyPart.setFileName(fileName); 
			
			
			// 加入第二部分
			multipart.addBodyPart(messageBodyPart);
			// 将多部分内容放到e-mail中
			message.setContent(multipart);
		}  
		
		message.saveChanges();
		Transport transport = sendsession.getTransport("smtp");
		transport.connect(smtpHost, username, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}



==================webSocket
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/myMsgWebSocket",
		decoders = {MyMessageDecoder.class}, //implements Decoder.Text<T> 请求时如@OnMessage方法的第一个参数是自定义类型,把String->T
		encoders = { MyMessageEncoder.class  }) //implements Encoder.Text<T>   T->String
public class MyMessageWebSocket { 
   private static Set<Session> peers = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());
    @OnOpen
    public void onOpen(Session session) {
        peers.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        peers.remove(session);
    }
    @OnMessage
    public void shapeCreated(MyMessage message, Session client) //如第一个参数是自定义类型会使用decoders和encoders,如String就不用
				throws IOException, EncodeException 
	{
		Set<Session> opendSessions=client.getOpenSessions();//所有打开的
        for (Session otherSession : peers) 
		{
            if ( otherSession.equals(client)) 
			{
                //otherSession.getBasicRemote().sendText(message);//发String消息到客户端
            	otherSession.getBasicRemote().sendObject(message);//要Encoder ,T->String
				client.getUserProperties().put("key",obj);//是个Map
				Object stored=client.getUserProperties().get("key");
            }
        }
    }
}

//编程式部署，放入其它的启动初始化方法中
ServerEndpointConfig.Builder.create(MyServerEchoEndpoint.class, "/echo").build();

---websocket java 做 client
public class MyClientEndpoint extends Endpoint  
{
	@Override
	public void onOpen(final Session session, EndpointConfig config) 
	{
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(String message) {
				System.out.println("客户端收到消息:"+message);	
			}
		}); 
	}
}

//代码最好也运行在容器中
WebSocketContainer container = ContainerProvider.getWebSocketContainer();//实现 META-INF/services/javax.websocket.ContainerProvider 
Session session=container.connectToServer(MyClientEndpoint.class,   //收到服务端消息的回调类
		ClientEndpointConfig.Builder.create().build(),
		new URI("ws://localhost:8080/J_JavaEE/myMsgWebSocket"));
session.getBasicRemote().sendText("obj_type123:data_123客户端发送的Test消息");
//session.getBasicRemote().sendObject(new MyMessage("obj_type","123_data"));//报错 因使用的 Decoder.Text 
Thread.sleep(3*1000);
session.close();
	

	
	
WebSocket 基于 HTTP协议 
请求头有 
Upgrade: websocket
Sec-WebSocket-Extensions:
Sec-WebSocket-Key:
Sec-WebSocket-Version: 13

响应码101 表示 服务器将会切换到在Upgrade 消息头中定义的那些协议

	
=============================JSON

<dependency>
    <groupId>javax.json</groupId>
    <artifactId>javax.json-api</artifactId>
    <version>1.1.4</version>
</dependency>

<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.1.4</version>
</dependency>

mvnrepository 上提示使用jarkata的(最近有更新),search.maven.org上没有提示 ,类名都一样，只是包名不一样

 <dependency>
	<groupId>jakarta.json</groupId>
	<artifactId>jakarta.json-api</artifactId>
	<version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.eclipse.parsson</groupId>
    <artifactId>jakarta.json</artifactId>
    <version>1.1.0</version>
</dependency>


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonStructure;
//--生成
JsonBuilderFactory bf = Json.createBuilderFactory(null);
JsonStructure struct =bf.createObjectBuilder()
            .add("firstName", "John")//如值为null报空指针错误
            .add("age", 25)
            .add("address", bf.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
			).build();
			
JsonArray array=bf.createArrayBuilder()
	.add(bf.createObjectBuilder()
		.add("type", "home")
		.add("number", "212 555-1234"))
	.add(bf.createObjectBuilder()
		.add("type", "fax")
		.add("number", "646 555-4567"))
	.build();
System.out.println(struct.toString());  //{...}
System.out.println(array.toString());//[{...},{...}]
//--解析
String inStr="[{\"type\":\"home\",\"number\":\"212 555-1234\"},{\"type\":\"fax\",\"number\":\"646 555-4567\"}]";
JsonParser parser = Json.createParser(new StringReader(inStr));
while(parser.hasNext()) 
{
	Event e = parser.next();
	if (e == Event.KEY_NAME)
	{
		if (parser.getString().equals("number")) 
		{
			parser.next();
			System.out.println(parser.getString());
		}  
	}
}


					
=============================Tomcat
tomcat 10 修改 javax.* 到 jakarta.*.   ，即会使用 jakarta.servlet.Filter 
	<dependency>
	  <groupId>jakarta.servlet</groupId>
	  <artifactId>jakarta.servlet-api</artifactId>
	  <version>5.0.0</version>
	</dependency>
	

tomcat 9 版本不支持JDK11 (docker上有 tomcat:9.0.34-jdk11)

catalina.bat jpda start/run 启动8000 可远程调试

catalina.bat run 或 catalina.bat start
startup.bat

 
tomcat 8  tomcat-users.xml 中加配置
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<role rolename="manager-jmx"/>
	<role rolename="manager-status"/>
	<user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx,manager-status"/>
 
默认的安全只能使用 127.0.0.1 或者 localhost 如是本机IP就不行 cross-site request forgery (CSRF),
到tomcat8/conf/Catalina/localhost/目录下,打开manager.xml文件（没有就新建）添加下面内容  
<Context privileged="true" antiResourceLocking="false"   
         docBase="${catalina.home}/webapps/manager">  
             <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="^.*$" />  
</Context> 


web.xml修改也会被reload

<Context privileged="true" antiResourceLocking="false"
         docBase="${catalina.home}/webapps/manager">
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.0\.0\.1" />
</Context>



server.xml中

http://tomcat.apache.org/tomcat-9.0-doc/config/http.html
	maxConnections 如超过也会继续接受连接，根据acceptCount设置，如队列满拒绝连接 如NIO and NIO2 默认值是 10000  如 APR/native 默认值是 8192 
	acceptCount 连接请求最大队列长度，默认100
	maxThreads 最大线程数，默认是200 
	minSpareThreads 最少运行线程数  默认10
	 connectionTimeout  单位是milliseconds The default value is 60000 (60 seconds) 
	
	<Connector port="8080"  protocol="HTTP/1.1"
		 maxThreads="200" minSpareThreads="30"   maxConnections="1000"
	<Host name="localhost"  appBase="webapps" 这个webapps可以改成其它路径

ulimit -n  默认值1024 
每个进程必然打开标准输入，标准输出，标准错误，服务器监听 socket,进程间通讯的unix域socket等文件
那么剩下的可用于客户端socket连接的文件数就只有大概1024-10=1014个左右
也就是说缺省情况下，基于Linux的通讯程序最多允许同时1014个TCP并发连接。

--------Tomcat/conf/web.xml中加修改listings 为true　就可以列目录了(默认是不可以的)
<init-param>
	<param-name>listings</param-name>
	<param-value>true</param-value>
</init-param>
--------	


在tomcat-7\conf\Catalina\localhost\建立 SpringPortlet.xml文件,内容
<Context  docBase="D:/program/eclipse_java_workspace/J_SpringPortlet/WebContent"
		 path="/SpringPortlet" reloadable="true"  crossContext="true">
	<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4" />
</Context>

如想对整个tomcat下应用做热部署 修改apache-tomcat-9.0.10\conf\context.xml 
的<Context>标签增加属性reloadable="true" 即	<Context reloadable="true">



跨域 cross-origin (CORS) 参考 Ajax_JS

META-INF目录下加context.xml文件 (为Tomcat使用 新版本测试没用)
<Context crossContext="true" /> 

conf/server.xml  <Context  crossContext="true"  /> 这个是为一个tomcat有两个应用的跨域

https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#CORS_Filter
当使用tomcat  web.xml 中加 <filter-class>org.apache.catalina.filters.CorsFilter</filter-class> 
源码中这个类上没有@,最好自己写，就不会绑定tomcat了
 


部署方法 
$CATALINA_BASE/conf/[enginename]/[hostname]/context.xml		named [webappname].xml 
$CATALINA_BASE/webapps/[webappname]/META-INF/context.xml
[enginename]是Catalina,[hostname]是localhost
 
 

---------配置DataSource ,jdbc.jar放在tomcat的lib目录下
方法一,要2步
	1) 在Tomcat 根目录下的conf\server.xml 配置Resource：
	<GlobalNamingResources>
		<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		description="DB Connection"  auth="Container"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4"/>
	</GlobalNamingResources>

	2) 在Tomcat 根目录下的conf\context.xml 配置:
	<ResourceLink name="myjdbc" global="jdbc/mydatasource" type="javax.sql.DataSourcer" />

	ctx.lookup("java:comp/env/myjdbc");
方法二
	整合在conf\context.xml 中配置:
	<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
	driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
	maxIdle="2" maxWait="5000" maxActive="4" />

	ctx.lookup("java:comp/env/jdbc/mydatasource");

server.xml 中 在文件未尾的</Host>之前加
	<Context docBase="D:/program/eclipse_java_workspace/J_SpringPortlet/WebContent"  
			 path="/J_SpringPortlet" reloadable="true"  crossContext="true">
		<!-- 
		<Resource name="jdbc/mydatasource" type="javax.sql.DataSource"
		driverClassName="org.h2.Driver" url="jdbc:h2:tcp://localhost/~/test" username="sa" password=""
		maxIdle="2" maxWait="5000" maxActive="4" />
		-->
	</Context>


web.xml中
<resource-ref>
 <res-ref-name>jdbc/Modeling</res-ref-name>
 <res-type>javax.sql.DataSource</res-type>
 <res-auth>Container</res-auth>
</resource-ref>  

Context initCtx = new InitialContext();

Context envCtx = (Context) initCtx.lookup("java:comp/env");
DataSource dataSource = (DataSource) envCtx.lookup("jdbc/Modeling");
con = dataSource.getConnection();
//--------
//DataSource dataSource = (DataSource) initCtx.lookup("java:comp/env/jdbc/Modeling");
//con = dataSource.getConnection();
 
 
---------启用Tomcat 的HTTPS协议
keytool -genkey -alias tomcat -keyalg RSA -keystore C:/temp/.keystore
提示输入密码,可以使用Tomcat的默认值changeit,一些其它的信息也要写
修改tomcat目录下的server.xml文件，去掉以下注释
<!--
<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
		   maxThreads="150" scheme="https" secure="true"
		   clientAuth="false" sslProtocol="TLS" />   // Transport Layer Security安全传输层协议(TLS),Secure Sockets Layer (SSL),
-->
并加入keystoreFile="C:/temp/.keystore"
默认是读用户主目录下的.keystore文件
	
HTTP/1.1起，默认使用长连接，会在响应头加入 Connection:keep-alive
	但是对每个请求仍然要单独发 header，Keep-Alive不会永久保持连接，它有一个保持时间，这种长连接是一种“伪链接”

websocket的长连接，是一个真的全双工。长连接第一次tcp链路建立之后，后续数据可以双方都进行发送，不需要发送请求头
	
HTTP/1.1 可以同时在同一个tcp链接上发送多个请求,但是只有响应是有顺序的

---- Tomcat 9 打开HTTP 2(只能在https下运行)  
下载 tomcat-native-1.2.18-openssl-1.1.1-win32-bin.zip 解压后的bin/x64/下的 tcnative-1.dll 放PATH中能找到的地方(如Tomcat/bin下)
bin/openssl 命令可用 (Cygwin也有)
    openssl genrsa -out server.key 2048
    openssl rsa -in server.key -out server.key
    openssl req -new -x509 -key server.key -out ca.crt -days 3650
	
	浏览器认为是不可信的
	
把生成的文件放tomcat/conf目录
修改tomcat 9 server.xml中打开
<Connector port="8443" protocol="org.apache.coyote.http11.Http11AprProtocol"
               maxThreads="150" SSLEnabled="true" >
	<UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
	<SSLHostConfig>
		<Certificate certificateKeyFile="conf/localhost-rsa-key.pem"
					 certificateFile="conf/localhost-rsa-cert.pem"
					 certificateChainFile="conf/localhost-rsa-chain.pem"
					 type="RSA" />
	</SSLHostConfig>
</Connector>
修改证书路径 为
	    <Certificate certificateKeyFile="conf/server.key"
                         certificateFile="conf/ca.crt"/>

https://localhost:8443/ 访问项目就是HTTP2协议了,  注意是https ,端口是8443
Chrome network 标签打开protocol列值是h2
Firefox network 标签打开protocol列值是HTTP/2.0


tomcat9/logs/localhost_access_log.x.txt  显示是 HTTP/2.0
----
---tomcat路径文件名支持中文
<Connector port="8080" protocol="HTTP/1.1" 
    connectionTimeout="20000" redirectPort="8443" URIEncoding="utf-8"  />
以上URIEncoding="utf-8"是新加的


---tomcat路径不区分大小写
<Context  path="/mytest" docBase="D:\\Program\\apache-tomcat-6.0.18\\apache-tomcat-6.0.18\\webapps\\test" 
	caseSensitive="false"  reloadable="true"/>
注意这里的caseSensitive="false"

----web.xml <auth-method>FORM</auth-method> ,FORM和BASIC 的密码保存在数据库 tomcat/docs/realm-howto.html
	create table users (
	  user_name         varchar(15) not null primary key,
	  user_pass         varchar(15) not null
	);
	create table user_roles (
	  user_name         varchar(15) not null,
	  role_name         varchar(15) not null,
	  primary key (user_name, role_name)
	);
	
	jdbc.jar 放入tomcat/lib
	
	<Realm className="org.apache.catalina.realm.JDBCRealm"
	      driverName="org.h2.Driver" connectionURL="jdbc:h2:tcp://localhost/~/test" connectionName="sa" connectionPassword =""
	       userTable="users" userNameCol="user_name" userCredCol="user_pass"
	   userRoleTable="user_roles" roleNameCol="role_name"/>
	   
	insert into users(user_name,user_pass) values('lisi','123');
	insert into users(user_name,user_pass) values('zhang','123');
	insert into user_roles(user_name,role_name) values('lisi','develop');
	insert into user_roles(user_name,role_name) values('zhang','market');

	
windows控制台乱码 conf/logging.properties
	#java.util.logging.ConsoleHandler.encoding = UTF-8
	java.util.logging.ConsoleHandler.encoding = GBK

Tomcat 日志文件格式
server.xml中在<Host>下有配置,并有提示文档在  /docs/config/valve.html
	   <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />
	%h - Remote host name (or IP address if enableLookups for the connector is false)
	%l - Remote logical username from identd (always returns '-')
	%u - Remote user that was authenticated (if any), else '-'	
	%t - Date and time, in Common Log Format
	%r - First line of the request (method and request URI)
	%s - HTTP status code of the response
	%b - Bytes sent, excluding HTTP headers, or '-' if zero
	
	示例
	35.8.33.21 - - [06/Jul/2016:00:00:00 +0800] "POST /loan-acct-web/remoting/madeLoanProcessQuerySimpleFacadehession HTTP/1.0" 200 987

linux 下Tomcat会一直向catalina.out这个文件写(windows下就不会)
catalian.sh 脚本中　对 CATALINA_OUT 的说明,
手工设置　CATALINA_OUT="$CATALINA_BASE"/logs/catalina.`date +"%Y-%m-%d"`.out　在判断后加,为了CATALINA_BASE存在值 ,这样只能在重启时是新文件

set CATALINA_OPTS=-Xms512M -Xmx512M -Xss1024k -XX:MaxPermSize=256M
export CATALINA_OPTS="-Xms128M -Xmx512M -Xss1024k -XX:+UseG1GC"
--优化


maxThreads 客户请求最大线程数
 
compression="on"   打开压缩功能

关闭 AJP

下载apr ,Http11AprProtocol
HttpNioAprProtocol

内核参数

--安全
<Host  
	unpackWARs="true" autoDeploy="true"> 
关闭war自动部署 unpackWARs="false" autoDeploy="false"
隐藏tomcat版本
	解压catalina.jar之后按照路径\org\apache\catalina\util\ServerInfo.properties找到文件
修改listings 为 false

