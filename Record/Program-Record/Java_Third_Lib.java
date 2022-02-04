
==============================LDAP 相关知识
Entry:
	添加一个Entry时，该Entry必须属于一个或多个objectclass ,每一个objectclass 规定了该Entry中必须要包含的属性，以及允许使用的属性。Entry所属的类型由属性objectclass规定
	
	DN(distinguished name) 用于唯一的标志Entry在directory中的位置
		根在最后面如ou=People,dc=example,dc=com,类似Unix的 /dc=com/dc=example/ou=People
	RDN(相对的) 对应的就是 ou=People
	root Entry只是一个特殊的Entry，它包含了目录服务器的配置信息，通常情况下，并不用来存储信息
	任何一个节点都可以包含信息，同时也可以是一个容器
Attribute: 	
	Entry中的属性值的特殊字符，必须进行转义
		# 转义为 \#
		， 转义为 \，
		+ 转义为 \+
		“ 转义为 \”
		\ 转义为 \\
		<  转义为 \<
		>  转义为  \>
		;  转义为  \;

建别名Entry，该Entry的object class必须是alias。而且其属性aliasedObjectName的值必须是该Entry所指向的Entry的DN
被引用的Entry被删除的话，该Entry就会指向一个错误的结果,可以使用LDAP URL或referral代替Alias Entry

LDAP 的探索选项
	a.base object
	b.search scope (Sub包含顶节点在内的一棵子树,Base 只搜索包含一个节点,Onelevel 搜索表示的节点下的所有直接子节点)
	g.filter参数,如*是通配符,也可用>,<=,!,如(age>21)或者(!(age<=21)), (|(sn=smith*)(age>21)), 匹配属性sn的值以smith开始,特殊字符转义
		属性值的特殊字符，必须进行转义
		*	转义为	\2A
		(	转义为	\28
		)	转义为	\29
		\	转义为	\5C
		NULL转义为	\00
	h.	return attributes 空表示返回所有属性。
		
		
LDAPV3 实现了SASL安全框架,一些用户只能查看特定的Entry，而不能修改	
LDIF  文本文件，用来描述目录数据,可导入导出,即使这两个目录服务器内部使用的是不同的数据库格式
	第一种用来描述Directory目录数据的
	第二种包含更新语句，用于更新现有的Directory条目数据
	文件内容 以dn:为一个单独部分,
1. 增加Entry
	dn:xxxxx
	changetype: add
	yyyyyy
2. 删除Entry
	dn:xxxxx
	changetype: delete
3. 修改Entry
	1)添加属性
		dn:xxx
		changetype: modify 
		add: mail		//mail属性
		mail: abc@xyz.com
	2)删除属性
		dn:xxx
		changetype: modify 
		delete: mail
	3)修改属性
		dn:xxx
		changetype: modify 
		replace: mail
		mail:  one@two.com
		mail:  one123@two.com
	多条更新属性语句之间用“-”分开,就用每次加dn:xxx了

4. Entry重命名或移动
	dn: xxxx
　　changetype: moddn　
	[newsuperior:如果要移动一条Entry则该项表示一个新的节点的DN]
　　[deleteoldrdn: ( 0 | 1 )该项表示是否要删除修改以前的RDN　０不删除]
	[newrdn:Entry的新RDN]


schema 定义数据存储结构,每个字段都有一个类型，以及一些约束条件
LDAP目录服务器的用户而言，一般不需要自己定义 schema

Attribute types 属性名称
	c	两位国家代码 如：中国:CN,美国:US
	co	国家的全名
	cn	通用名称
	dc	域名组件
	sn	姓，别名
	gn	gavenName
	o	组织名称
	ou	部门名称
	...
Attribute syntaxes  所存储的信息如何组织

object classes 每个Entry都必须至少属于一个object class。规定了该Entry可以存储那些属性
	|ObjectClass		| 必须属性	|
	|-------------------|-----------|
	|top				|  			| 所有类的基类
	|dcObject			|  			|
	|organizationalPerson|  		| 继承person,增加进也会自动所基类persion 加入
	|organizationalUnit|  			|
	|ads-jbpmPartition|  			|继承ads-partition
   inetOrgPerson 继承自organizationalPerson 继承自 person继承自top
 
==============================LADP Server Apache Directory Server
openLDAP没有windows的
Softerra LDAP Administrator 2017 是一个客户端管理工具

Active Directory =LDAP服务器＋LDAP应用（Windows域控）

//-apache的LDAP服务器项目ApacheDS,纯Java实现,可嵌入到你的应用程序,可与Spring一起使用
 ------apacheDS-2.0.0-M7 解压版本	没有server.xml
	
	\instances\default\conf\config.ldif ,这个文件修改后要导入才行
	dn: ads-transportid=ldap,    下面显示对应的配置端口
	ads-systemport: 10389
	
	apacheDS-2.0 和 apacheDS-1.5 自带的用户
	Bind DN or user:uid=admin,ou=system
	Bind password:secret

//--Directory Studio使用	
修改密码使用
在ou=system/uid=admin级下 ,显示objectClass=person(structual),userPassword中修改密码

右击Root DSE->new Context Entry->选择create entry from scratch->在obejctClass中选择dcObject,organization->下拉选择已存在的dc=example,dc=com->输入o属性值->finish

file->import-> LDIF into LDAP  , 或者右击xxx->import 


---unknow
	admin密码
	ads-chgPwdServicePrincipal: kadmin/changepw@EXAMPLE.COM
	
	ads-partitionSuffix: dc=example,dc=com
---unknow	
		
		
javax.naming.directory 
javax.naming.ldap

-----apacheds-1.5 的配置 
Apache Directory Server\instances\default\conf\server.xml
 <transports>
      <tcpTransport   改端口 默认是10389 (unencrypted or StartTLS) and 10636 (SSL).

一个Partition有一个Entry Tree有多个Entries
多个Partition之间无影响,名字为partition suffix

默认分区的实现是基于 JDBM 项目的 B+Trees	功能

server.xml 中的<partitions>下加入 ,可以把  <systemPartition>下的复制,修改id,suffix
<jdbmPartition id="example" cacheSize="100" suffix="dc=example,dc=com" optimizerEnabled="true"
                 syncOnWrite="true">
    <indexedAttributes>
    ...
    </indexedAttributes>
</jdbmPartition>

windows 服务的启动脚本
apacheds.exe -s conf\apacheds.conf "set.INSTANCE_HOME=C:\Program Files\Apache Directory Server\instances" set.INSTANCE=default "set.APACHEDS_HOME=C:\Program Files\Apache Directory Server"

Studio 可以右击DIT->import/export LDIF

在 <apacheDS id="apacheDS">下加入
	 <ldifDirectory>sevenSeasRoot.ldif</ldifDirectory> 启动服务自动导入指定文件 



程序增加分区
apacheds-jdbm-partition-1.5.7.jar
apacheds-jdbm-store-1.5.7.jar
apacheds-xdbm-base-1.5.7.jar
apacheds-core-api-1.5.7.jar
shared-ldap-0.9.19.jar


禁用匿名用户仿问
  <defaultDirectoryService 
        中修改   allowAnonymousAccess="true"  为false
Spring LADP				
==============================SNMP4J
	服务端使用Net-SNMP
==============================Jetty

eclipse 插件 run-jetty-run  可以在eclipse中使用 jetty 做servlet容器

java -jar start.jar 启动服务器　
	webapps目录效果类似tomcat 
　
http://localhost:8080/  
.war包 放到webapps目录下

RunJettyRun-1.8 插件 当eclipe认为是web项目(有建立Servlet的界面)才可run as ->jetty
 


--------------------------iText
https://developers.itextpdf.com/   
	有 7 community API https://itextsupport.com/apidocs/itext7/latest/
	Examples
	Download 7 version

<dependency>
  <groupId>com.itextpdf</groupId>
  <artifactId>itextpdf</artifactId>
  <version>5.5.13</version>
</dependency>
<dependency>
  <groupId>com.itextpdf</groupId>
  <artifactId>itext-asian</artifactId>
  <version>5.2.0</version>
</dependency>

pdfHTML 基于HTML生成 7 版本是收费的 , 有Community

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SimpleBookmark;

Document document = new Document();
document.addTitle("title");
document.addAuthor("Author");
document.addKeywords("my ");
document.addCreator("myCreator ");

PdfWriter.getInstance(document, new FileOutputStream("C:\\HelloWorld.pdf"));
document.open();
Paragraph p1=new Paragraph("_Hello 你好_",font);
p1.setIndentationLeft(10f);
p1.setIndentationRight(10f);
document.add(p1);
document.newPage();   //换页 
document.close();
--表格
String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.BOLD,new GrayColor(0.9f));

PdfPCell title=new PdfPCell(new  Paragraph(10,"hello world",font))   
//new PdfPCell(new Phrase("Cell with rowspan 2"));
title.setColspan(2);
title.setRowspan(2);
title.setBackgroundColor(BaseColor.GREEN);

		
PdfPTable table=new PdfPTable(numColumns)
table.addCell(title);

PdfPCell  cell = new PdfPCell(new Phrase("cn 中文 ,row 1; cell 1",font));
cell.setUseAscender(true);
cell.setMinimumHeight(20f);
cell.setHorizontalAlignment(1);
cell.setVerticalAlignment(5);
cell.setNoWrap(false);
table.addCell(cell);

document.add(table);
---图片
document.newPage();   //换页   
 //可以是绝对路径，也可以是URL
String strImg="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/google.png";
 Image img = Image.getInstance(strImg);   
 // Image image = Image.getInstance(new URL(http://xxx.com/logo.jpg));   
 img.setAbsolutePosition(0, 0);   
 document.add(img);

 -----读PDF
PdfReader reader = new PdfReader("d:/temp/mybatis.pdf");//读已经存在PDF
if(reader.isEncrypted())
{
			System.out.println("pdf是加密的");
}
System.out.println(reader.getPdfVersion());
//---读写
PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("d:/temp/itext_out.pdf"));
//建立pdf后,ttf文件不存在也可以(可能是系统中有)
String ttf="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);
for (int i=1; i<=reader.getNumberOfPages(); i++)
{
	  //获得pdfstamper在当前页的上层打印内容。也就是说 这些内容会覆盖在原先的pdf内容之上。
	  PdfContentByte over = stamper.getOverContent(i);
	  //用pdfreader获得当前页字典对象。包含了该页的一些数据。比如该页的坐标轴信息。
	  PdfDictionary p = reader.getPageN(i);
	  //拿到mediaBox 里面放着该页pdf的大小信息。
	  PdfObject po =  p.get(new PdfName("MediaBox"));
	  System.out.println(po.isArray());
	  //po是一个数组对象。里面包含了该页pdf的坐标轴范围。
	  PdfArray pa = (PdfArray) po;
	  System.out.println(pa.size());
	  //看看y轴的最大值。
	  System.out.println(pa.getAsNumber(pa.size()-1));
	  //开始写入文本
	  over.beginText();
	  //设置字体和大小
	  over.setFontAndSize(font.getBaseFont(),10);
	  //设置字体的输出位置
	  over.setTextMatrix(107, 540);
	  //要输出的text
	  over.showText("我要加[终稿]字样 " + i);
	  over.endText();
	  //创建一个image对象。
	  String strImg="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/google.png";
	  Image image = Image.getInstance(strImg);
	  //设置image对象的输出位置pa.getAsNumber(pa.size()-1)。floatValue() 是该页pdf坐标轴的y轴的最大值
	  image.setAbsolutePosition(0,pa.getAsNumber(pa.size()-1).floatValue()-100);//0, 0, 841.92, 595.32
	  over.addImage(image);
	  //画一个圈。
	  over.setRGBColorStroke(0xFF, 0x00, 0x00);
	  over.setLineWidth(5f);
	  over.ellipse(250, 450, 350, 550);
	  over.stroke();

}
 
stamper.close();
//---
List<HashMap<String, Object>> list = SimpleBookmark.getBookmark ( reader ) ;
for ( Iterator<HashMap<String, Object>> i = list.iterator () ; i.hasNext () ; ) 
{
	showBookmark (i.next ()) ;
}
private static void showBookmark ( Map bookmark ) 
{
	System.out.println ( bookmark.get ( "Title" )) ;
	ArrayList kids = ( ArrayList ) bookmark.get ( "Kids" ) ;
	if ( kids == null )
		return ;
	for ( Iterator i = kids.iterator () ; i.hasNext () ; ) 
	{
		showBookmark (( Map ) i.next ()) ;
	}
}	
 
//-----

Map<String,String> param =new HashMap<>();
param.put("my_username", "张三");
param.put("my_age", "20");

//world 创建模板文件 另存为(libreOffice导出) pdf  -> Adobe Acrobat Reader Proc DC  再编辑 PDF 
//		->准备表单->工具栏上 添加"文本"域 ,拖出一个区域, 设置变量,itext程序就可以赋值 
//设置模板变量
AcroFields form=stamper.getAcroFields();
String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
form.addSubstitutionFont(chineseFont);
for(String key :param.keySet())
{
	form.setField(key, param.get(key));
}
stamper.close();
//----

String[] files=new String[] {"d:/temp/template_user_res.pdf","d:/temp/HelloWorld.pdf"};
String res="d:/temp/merge_res.pdf";
PdfReader pdfReader=new PdfReader(files[0]);
//pdf合并
Rectangle rect=pdfReader.getPageSize(1);
Document document = new Document(rect);
PdfCopy copy=new PdfCopy(document,new FileOutputStream(res));
document.open();
for(int i=0;i<files.length;i++)
{
	PdfReader  reader=new PdfReader(files[i]);
	int n=reader.getNumberOfPages();
	for(int j=1;j<=n;j++)
	{
		document.newPage();
		PdfImportedPage page=copy.getImportedPage(reader, j);
		copy.addPage(page);
	} 
	reader.close();
}
document.close();
pdfReader.close();
//---- 背景文字
String fromFile="d:/temp/merge_res.pdf";
String bgFile="d:/temp/merge_res_bg.pdf";
String markText="背景文字";

String ttf=dirPrefix+"/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);

PdfReader  reader=new PdfReader(fromFile);
PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(bgFile));
int n=reader.getNumberOfPages();
Phrase phrase=new Phrase(markText,font);
for(int i=1;i<=n;i++)
{
	//PdfContentByte over=stamper.getOverContent(i);//水印在文本之上
	PdfContentByte over=stamper.getUnderContent(i); //水印在文本之下
	over.saveState();
	PdfGState state=new PdfGState();
	state.setFillOpacity(0.2f);
	over.setGState(state);
	float beginPositionX=10,beginPositionY=70,distance=175;
	for(int i2=0;i2<4;i2++)
	{
		for(int j=0;j<4;j++)
		{
			ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
						phrase, beginPositionX+distance*i2, beginPositionY+distance*j, 25);
		}
	}
	over.restoreState();
}
stamper.close();
reader.close(); 


----web中应用

<%		
	String user = request.getHeader("User-Agent");
	if(user.indexOf("MSIE") != -1 && user.indexOf("Windows") != -1)
	  {
		  out.print("<body leftMargin=\"0\" topMargin=\"0\" scroll=\"no\">");
		    out.print("<EMBED src=\"PDFPreview.pdf?type=pdf\" "
		      + "width=\"100%\" height=\"100%\" "
		      + "fullscreen=\"yes\" type=\"application/pdf\">");

	  } else
	  {
	    out.print("<body leftMargin=\"0\" topMargin=\"0\" scroll=\"no\">");
	    out.print("<script>document.location = 'PDFPreview.pdf?type=pdf';</script>");
	  }
%>

----Servlet的内容:
response.setContentType("application/pdf");
Document document = new Document();
ByteArrayOutputStream buffer = new ByteArrayOutputStream();

PdfWriter.getInstance(document, buffer);
document.open();
document.add(new Paragraph("Hello World"));
document.close();

DataOutput output = new DataOutputStream(response.getOutputStream());
byte[] bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
for (int i = 0; i < bytes.length; i++)
{
	output.writeByte(bytes[i]);
}
//web结束






中文不显示方法一 OK
//建立pdf后,ttf文件不存在也可以(可能是系统中有)
String ttf="D:/Program/all_code_workspace/eclipse_java_workspace/J_JavaThirdLib/src/pdf_itext/simhei.ttf";
//String ttf="c:\\WINDOWS\\Fonts\\SIMHEI.TTF";
BaseFont chineseFont = BaseFont.createFont(ttf ,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
Font font = new Font(chineseFont, 12, Font.NORMAL);

中文不显示方法二
//要加对应版本的,iTextAsian.jar  也不行???
BaseFont bf = BaseFont.createFont("STSong-Light,Bold", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);//可不加Bold
document.add(new Paragraph("混沌之神", new Font(bf)));

JFreeChart 和 iText
将 DefaultFontMapper mapper = new DefaultFontMapper();语句替换为
   AsianFontMapper mapper = new AsianFontMapper("STSong-Light","UniGB-UCS2-H");


======================PdfBox=============================
依赖于commons-logging,fontbox
<dependency>
  <groupId>org.apache.pdfbox</groupId>
  <artifactId>pdfbox</artifactId>
  <version>2.0.11</version>
  <type>bundle</type>
</dependency>

要基于HTML生成，要支持中文 
---1.8 
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

---读
boolean sort = false;
String textFile = null;
String pdfFile = "D:/my/Spring_源码分析.pdf";
PDDocument document = PDDocument.load(new File(pdfFile));
if (pdfFile.length() > 4) {
	textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
}
// 文件输出流，写入文件到textFile
Writer output = new OutputStreamWriter(new FileOutputStream(textFile),"UTF-8");
// PDFTextStripper来提取文本
PDFTextStripper stripper = new PDFTextStripper();//可加GBK,但中文OK
stripper.setSortByPosition(sort);
stripper.setStartPage(1);
stripper.setEndPage(20);
// 调用PDFTextStripper的writeText提取并输出文本
stripper.writeText(document, output);
output.close();
document.close();
//---
PDFParser parser = new PDFParser(new RandomAccessFile(new File(pdfFile),"rw"));  
parser.parse();  
PDDocument pdfdocument = parser.getPDDocument();  
PDFTextStripper stripper = new PDFTextStripper();  
String result = stripper.getText(pdfdocument);  
System.out.println(result);  
---写
PDDocument document = new PDDocument();
PDPage page = new PDPage();
document.addPage( page );

// Create a new font object selecting one of the PDF base fonts
PDFont font = PDType1Font.HELVETICA_BOLD;//中文不行

// Create a new font object by loading a TrueType font into the document
//PDFont font = PDTrueTypeFont.loadTTF(document, "c:\\WINDOWS\\Fonts\\SIMHEI.TTF");//中文不正常

PDPageContentStream contentStream = new PDPageContentStream(document, page);

contentStream.beginText();
contentStream.setFont( font, 12 );
contentStream.newLineAtOffset( 100, 700 );
contentStream.showText( "Hello World_中__" );
contentStream.endText();
contentStream.close();
document.save( "d:/temp/Hello World.pdf");
document.close();
=================================Lucene-6.4================================
  
 https://lucene.apache.org/core/8_5_1/index.html 
 https://lucene.apache.org/core/8_5_1/grouping/index.html (分组功能，有lucene-grouping-8.3.1.jar) 
 https://lucene.apache.org/core/8_5_1/join/org/apache/lucene/search/join/package-summary.html
 join  功能
 spatial3d: 3D 的 geometry APIs 
 
 
 最新的 luke-src-4.0.0 最近更新是2012年7月 
是一个swing界面工具,可以查看索引目录内容(luke发音同look),目前在lucene(8.3)下载包中有luke目录 
 
 倒排索引， 从词出发，记载了这个词在哪些文档中出现过
 由两部分组成——词典和倒排表。
 
 官方带的中文分词器 analyzers-smartcn  ,lucene-analyzers-smartcn-6.4.0.jar  大小  3.43M
 
 
	core/lucene-core-6.4.0.jar
	analysis/common/lucene-analyzers-common-6.4.0.jar
	analysis/smartcn/lucene-analyzers-smartcn-6.4.0.jar
	queryparser/lucene-queryparser-6.4.0.jar
	
// http://lucene.apache.org/core/6_4_0/index.html          Lucene demo, its usage, and sources
	
//Document可以看作是 数据库的一行记录，Field可以看作是数据库的字段

--建立索引
//Directory dir = new RAMDirectory();//新版本8没有这个构造器，类过时,用MMapDirectory
Directory directory =new MMapDirectory(  Paths.get("/tmp"));//MMap=memory mapping 
Directory dir = FSDirectory.open(Paths.get("c:/tmp/testindex"));//是生成索引的目录名
Analyzer analyzer = new StandardAnalyzer();
IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
//每次建立索引文件名顺序号会增加,默认只保留最近的2个相邻的顺序号  (0-9a-z)
iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);//建立是 OpenMode.CREATE 
IndexWriter writer = new IndexWriter(dir, iwc);

 
Document doc = new Document();
// StringField不分词 查询时要输入完整的查询
// TextField是分词的

doc.add(new StringField("path","c:/temp/file1.txt",Field.Store.YES)); //索引,但不分词,YES 表示后面可以用doc.get("path");来得到
String fName= file.getFileName().toString();
if(fName.contains("."))
	  fName=fName.substring(0,fName.lastIndexOf("."));
doc.add(new TextField("name",fName,Field.Store.YES));
doc.add(new TextField("id", Integer.toString(i++),Field.Store.YES));
doc.add(new  StoredField("size",  file.toFile().length()));  
//doc.add(new Field("contents",  "我来自中国" , TextField.TYPE_STORED));
doc.add(new LongPoint("modified", lastModified));//是index的,但不store
doc.add(new StoredField("createTime",new Date().getTime()));//store的
//field.setBoost(1.2f);//新版本8没有这个方法 //默认1.0,建立索引时(更新索引不可)在原有基础上加权,为某些特定的内容,分值高,可做优先显示
 
//建立
doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))); 
if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
	writer.addDocument(doc);
} else { 
	writer.updateDocument(new Term("path", file.toString()), doc); // 其实是先删,再加,要影响数量就会查会到numDeletedDocs的值,
}
 
 writer.deleteDocuments(new Term("path", file.toString()));//windows下路径用\\分隔,Term精确查找,可用Query,
 //delete后 reader.numDocs(); 比  reader.maxDoc(); 少1  
 //writer.forceMergeDeletes();//没用??数maxDoc也没变少,numDeletedDocs也有值,实际是否执行由MergePolicy决定,默认是TieredMergePolicy
  writer.forceMerge(1); //当有当删除个数大于指定数才有用
  //writer.deleteUnusedFiles();//没用??数maxDoc也没变少,numDeletedDocs也有值
writer.close();
dir.close();



--使用索引搜索
public class MyFasterQueryParser extends QueryParser {
	public MyFasterQueryParser(String f, Analyzer a) {
		super(f, a);
	}
	@Override
	protected Query getFuzzyQuery(String field, String termStr, float minSimilarity)
			throws ParseException {
		throw new ParseException("由于性能原因禁用 FuzzyQuery  ");
	}
	@Override
	protected  Query getWildcardQuery(String field, String termStr) throws ParseException {
		throw new ParseException("由于性能原因禁用 WildcardQuery  ");
	}
	/*
	@Override
	protected   Query getRangeQuery(String field, String part1, String part2, boolean startInclusive, boolean endInclusive)throws ParseException {
	   // if(field.equals("size"))//报错???
	   // 	return  LongPoint.newRangeQuery(field,Long.parseLong(part1),Long.parseLong(part2));
		return super.newRangeQuery(field, part1, part2, startInclusive, endInclusive);
	}
	*/
}

 Directory directory = FSDirectory.open(Paths.get("c:/tmp/testindex"));//刚刚生成的索引目录
DirectoryReader oldReader = DirectoryReader.open(directory);
IndexReader   reader=DirectoryReader.openIfChanged(oldReader);//如果索引发生的改变,返回一个新的Reader(测试无用？？？),否则返回null
if(reader==null) 
	reader=oldReader;

if(reader.hasDeletions())
{
	System.out.println("maxDoc : "+reader.maxDoc());
	System.out.println("numDocs : "+reader.numDocs());
	System.out.println("numDeletedDocs : "+reader.numDeletedDocs());
}

IndexSearcher searcher = new IndexSearcher(reader);
Analyzer analyzer = new StandardAnalyzer();
QueryParser parser = new QueryParser( "contents", analyzer);//"contents"是上面建立的字段
//MultiFieldQueryParser parser =new MultiFieldQueryParser(new String[]{"fieldname","contents"},new SmartChineseAnalyzer());
//MyFasterQueryParser parser = new MyFasterQueryParser(field, analyzer);//禁用性能低的查询功能,如~,以*开头的通配
 
//Query query = parser.parse(line);//要搜索的内容
//Query query =parser.parse("name:LongPoint");//改变搜索域
//Query query =parser.parse("name:Long*");//通配
parser.setAllowLeadingWildcard(true);
Query query =parser.parse("name:*Point");//默认不能以*开头的通配，因为效率低，要setAllowLeadingWildcard开启
	  
parser.setDefaultOperator(Operator.AND); //所有的词都要出现
Query query = parser.parse("Morphology KStemFilter");//有空格默认就是OR
Query query = parser.parse("Morphology AND KStemFilter");//也可这样用AND,OR
Query query = parser.parse("(Morphology AND KStemFilter) OR　name:Long*");//使用()做多个组合
 
//Query query =parser.parse("-name:LongPoint +Morphology");//前面-表示不能出现,+表示要出现
//Query query =parser.parse("NOT name:LongPoint AND Morphology"); //NOT同-,AND同+ ,功能同上

//Query query =parser.parse("id:[2 TO 4 ]");//TO 必须大写,id是符类型的,但显示结果为什么多??
//Query query =parser.parse("id:{2 TO 4 ]");//大括号开区间,中括号闭区间,同数学

//Query query =parser.parse("\"fuzzy searches\"");//必须是两个词紧挨着,用""
//Query query =parser.parse("\"jakarta apache\"~10");//两个词间距离小于10的
//文档上说的
//Query query =parser.parse("roam~");//模糊查会显示 foam 或者 roams 
//Query query =parser.parse("/[mb]oat/");//会显示  "moat" 或者 "boat" 

//Query query =new TermRangeQuery("fieldname",new BytesRef("text"),new BytesRef("text10"),true,true); 

Calendar c=Calendar.getInstance();
c.set(Calendar.YEAR, 2016);
//Query query =  LongPoint.newRangeQuery("modified",c.getTime().getTime(),Calendar.getInstance().getTimeInMillis());

PrefixQuery query=new PrefixQuery(new Term("path","C:\\My\\software\\_java\\lucene-6.4.0\\lucene-6.4.0\\docs\\core\\org\\apache\\lucene\\document\\Long"));
//PrefixQuery query=new PrefixQuery(new Term("contents","LongPoint"));//这样是没用的
WildcardQuery query=new WildcardQuery(new Term("path","*LongPoint*"));

BooleanQuery.Builder builder= new BooleanQuery.Builder();
Term term=new Term("path","C:\\My\\software\\_java\\lucene-6.4.0\\lucene-6.4.0\\docs\\core\\org\\apache\\lucene\\document\\LongPoint.html");
builder.add(new TermQuery(term),Occur.MUST);//有MUST,SHOULD,MUST_NOT
Calendar c=Calendar.getInstance();
c.set(Calendar.YEAR, 2016);
builder.add(LongPoint.newRangeQuery("modified",c.getTime().getTime(),Calendar.getInstance().getTimeInMillis()),Occur.MUST);
BooleanQuery query=builder.build();


PhraseQuery.Builder builder = new PhraseQuery.Builder();
builder.setSlop(1);// 表示跳1个词 ， 如有 see this message ,先see,再message就可 查到
builder.add(new Term("contents", "see"));
builder.add(new Term("contents", "message"));
PhraseQuery query = builder.build();

FuzzyQuery query=new FuzzyQuery(new Term("contents","make"));//对于  make 可查出  mike或者 jake,可能对于查不到结果时
   
searcher.search(query, 100);// 前100个命中
//searcher.search(query, 100,Sort.INDEXORDER);//排序,还有Sort.RELEVANCE 
searcher.search(query, 100,new Sort(new SortField("size",SortField.Type.LONG)));//没效果????
searcher.search(query, 100,new Sort(new SortField("name",SortField.Type.STRING,true)));//反向排序 ,Sort中可传多个SortField,没效果????
//searcher.search(query, Collector);//Collector 文档有警告可能不兼容后期版本，可自定义排序和过滤词,聚合,如何做????
 //新版本没有search的Filter参数,也不能自定义评分


TopDocs results = searcher.search(query, 5);//最多显示少个结果
ScoreDoc[] hits = results.scoreDocs;
					results.totalHits;//如果实际结果大于最多显示,totalHits存实际结果

 while(results.scoreDocs.length>0)
{
	System.out.println("--------------------");
	ScoreDoc lastScoreDoc=null;
	for(ScoreDoc scoreDoc:results.scoreDocs )
	{
		lastScoreDoc=scoreDoc;
		 Document doc = searcher.doc(scoreDoc.doc);
		 System.out.println("score="+scoreDoc.score+",id="+(doc.get("id")) + ",path=" + doc.get("path"));
	}
	results=searcher.searchAfter( lastScoreDoc, query, hitsPerPage);  //可以使用searchAfter来分页
}

TermQuery tQuery=new TermQuery(new Term("fieldname","text"));
hits = isearcher.search(tQuery,  1000).scoreDocs;
		
//遍历hits
Document doc = searcher.doc(hits[i].doc);//hits[i].doc是一个docID
System.out.println("score="+hits[i].score);//和boost加权相关的

doc.get("path");//上面建立的

reader.close();
directory.close();

-------分词
常用分词器
  //this is my football ,  I  very like it
  Analyzer analyzer = new StandardAnalyzer();  // (my)(football)(i)(very)(like)
  Analyzer analyzerSimple= new SimpleAnalyzer();//(this)(is)(my)(football)(i)(very)(like)(it)
  //Analyzer analyzerStop= new StopAnalyzer();//新版本8没有这个,(my)(football)(i)(very)(like)
  Analyzer analyzerWhite= new WhitespaceAnalyzer();//(this)(is)(my)(football)(,)(I)(very)(like)(it)
  Analyzer analyzerCN= new  SmartChineseAnalyzer() ;// 官方带的中文分词器  lucene-analyzers-smartcn-6.3.0.jar

Analyzer analyzerCN= new  SmartChineseAnalyzer() ; //可传停用词,看源码默认停用词只是一些标点,词典在coredict.mem是二进制文件(还有一个bigramdict.mem没找到源码)
//这是我的足球，我非常喜欢它
TokenStream stream=analyzer.tokenStream("contents", "this is my football ,  I  very like it" );

OffsetAttribute offsetAttr=stream.addAttribute(OffsetAttribute.class);
PositionIncrementAttribute posiIncrementAttr=stream.addAttribute(PositionIncrementAttribute.class);
TypeAttribute typeAttr=stream.addAttribute(TypeAttribute.class);
CharTermAttribute charAttr=stream.addAttribute(CharTermAttribute.class);

stream.reset();//按TokenStream javadoc顺序做
while(stream.incrementToken())
{
  System.out.print(posiIncrementAttr.getPositionIncrement()+",");
  System.out.print(offsetAttr.startOffset()+"-"+offsetAttr.endOffset());
  System.out.print(typeAttr.type());//StandardAnalyzer是 <IDEOGRAPHIC>，其它看源码默认是word
  System.out.println("("+charAttr+")");
}
stream.end();
stream.close();//放 finally  中 
analyzer.close();

Analyzer 的方法 TokenStream tokenStream(String fieldName, Reader reader)   
  
TokenStream 的子类有 TokenFilter, Tokenizer,
先把读到的文本分词转成Tokenizer,再用多个 TokenFilter 去无用的如标点, am,are ,an ,the 

//自定义Analyzer
public class MyStopAnalyzer extends Analyzer{
	private CharArraySet stopWords;
	public MyStopAnalyzer(String stopWord[])
	{
		stopWords=StopFilter.makeStopSet(stopWord,true);
		//stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);//新版本8没有ENGLISH_STOP_WORDS_SET
	}
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		//Tokenizer source = new LowerCaseTokenizer();//新版本8没有LowerCaseTokenizer
		Tokenizer source = new StandardTokenizer();
        return new TokenStreamComponents(source, new StopFilter(source, stopWords));
		 
	} 
}
Analyzer myAnalyzer =new MyStopAnalyzer(new String[]{"this","is"});
TokenStream stream=myAnalyzer.tokenStream("contents", "this is my football ,  I  very like it" );

//可以使用同义词搜索
public class MySameAnalyzer extends Analyzer{
	//参考SmartChineseAnalyzer 的实现
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new HMMChineseTokenizer();
        return new TokenStreamComponents(tokenizer, new MySameTokenFilter(tokenizer));
	} 
}
public class MySameTokenFilter extends TokenFilter {
	private CharTermAttribute charTermAttr = null;
	private AttributeSource.State state=null;
	private  PositionIncrementAttribute posiIncrementAttr=null;
	private Stack<String> sameWordStack=null;
	Map<String,String[]> maps = new HashMap<String,String[]>();
	protected MySameTokenFilter(TokenStream input) {
		super(input);
		charTermAttr = this.addAttribute(CharTermAttribute.class);
		posiIncrementAttr=this.addAttribute(PositionIncrementAttribute.class);
		sameWordStack=new Stack<>();
		maps.put("中国",new String[]{"天朝","大陆"});
		maps.put("我",new String[]{"咱","俺"});
	}
	@Override
	public boolean incrementToken() throws IOException {
		/*
		 if (!input.incrementToken())
			return false;
		if(charTermAttr.toString().equals("中国"))
		{
			charTermAttr.setEmpty();
			charTermAttr.append("大陆");
		}
		if(charTermAttr.toString().equals("我"))
		{
			charTermAttr.setEmpty();
			charTermAttr.append("唵");
		}
		*/
		//---------------
		while(sameWordStack.size()>0)
		{
			String word=sameWordStack.pop();
			restoreState(state);
			charTermAttr.setEmpty();
			charTermAttr.append(word);
			posiIncrementAttr.setPositionIncrement(0);
			return true;
		}
		 if (!input.incrementToken())
				return false;
		 if(maps.containsKey(charTermAttr.toString()))
		 {
			for(String word:maps.get(charTermAttr.toString()))
			{
				sameWordStack.add(word);
			}
			state=captureState();
		}
		return true;
	}
}
---- HighLiger

highlighter/lucene-highlighter-6.4.0.jar 依赖 memory/lucene-memory-6.4.0.jar

 
String txt="我来自中国，中国面积很大,人很多";
			
//TermQuery query=new TermQuery(new Term("f","中国"));

QueryParser parser=new QueryParser("f",new SmartChineseAnalyzer()); 
Query query=parser.parse("中国 面积");

QueryScorer scorer=new QueryScorer(query);
Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);

//Highlighter highLiter =new Highlighter(scorer);//默认是<B> </B>
			
Formatter formatter=new SimpleHTMLFormatter("<span style='color:red'>","</span>");
Highlighter highLiter =new Highlighter(formatter,scorer);

highLiter.setTextFragmenter(fragmenter);
String highTxt=highLiter.getBestFragment(new SmartChineseAnalyzer(),"f",txt);
System.out.println(highTxt);//变为        <B>中国</B>
 

---- tika  用于打开各种不同格式文档 DOCX,XLS, PDF,HTML
最新版本为1.24

java -jar tika-app-1.14.jar  有界面 -g
java -jar tika-app-1.14.jar  --help

//Maven 
  <dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>1.14</version>
  </dependency>
  <dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-parsers</artifactId>
    <version>1.14</version>
  </dependency>
//Gradle 
dependencies {
    runtime 'org.apache.tika:tika-parsers:1.14'
}

Parser parser=new AutoDetectParser();
InputStream input=null;
//String filePath="C:/tmp/spring-hateoas-reference-0.19.pdf";
String filePath="C:/_work/员工简历模板.xlsx";
try{
	input=new FileInputStream(new File(filePath));
	ParseContext context=new ParseContext();
	context.set(Parser.class, parser);
	BodyContentHandler handler=new BodyContentHandler();
	Metadata meta=new Metadata();
	meta.set(Metadata.RESOURCE_NAME_KEY, filePath);
	parser.parse(input ,handler , meta, context);
	String fileContent=handler.toString();
	for(String name:meta.names())
	{
		System.out.println(name +"," +meta.get(name));
	}
	System.out.println(fileContent);
}catch (Exception e) {
	e.printStackTrace();
}finally {
	IOUtils.closeQuietly(input);
}



//方式二，性能低
Tika tika =new Tika();
String fileContent=tika.parseToString(new FileInputStream(filePath),meta);


//doc.add(new TextField("contents",  new Tika().parse(new File(filePath))));//Lucene使用tika,不会Store
//doc.add(new Field("contents", new Tika().parse(new File(filePath)), TextField.TYPE_STORED));//报错,Reader不能使用Store
doc.add(new Field("contents", new Tika().parseToString(new File(filePath)), TextField.TYPE_STORED));

---- 数据被修改了,立即就可以查到
SearcherManager manager=new SearcherManager(directory,new SearcherFactory());//SearcherManager 官方doc说是线程安全的
IndexSearcher isearcher = manager.acquire();
manager.maybeRefresh();//官方doc说，应周期调用, 查询之前,在单独线程中,可以很反应到查询
//...
isearcher.search(query,  1000);
//...
manager.release(isearcher);//finally中做

--------------------------------- POI excel xls,xlsx 
 <dependency>
	<groupId>org.apache.poi</groupId>
	<artifactId>poi-ooxml</artifactId>
	<version>3.17</version>
</dependency>

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//写
FileOutputStream out =null;
boolean is2007=true;//true,false
if(is2007)
{
	//要多加poi-ooxml-3.8.x.jar,apache项目xmlbeans的xbean.jar,poi-ooxml-schemas-3.8-x.jar
	//大量数据时,写excel时
	//workbook=new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
	workbook=new XSSFWorkbook();
	out=new FileOutputStream("/tmp/workbook.xlsx");
}
else
{
	out = new FileOutputStream("/tmp/workbook.xls");
	workbook=new HSSFWorkbook();
}
Sheet sheet = workbook.createSheet();
workbook.setSheetName(0, "第一页");

sheet.createFreezePane(0, 1);//冻结第一行，如标题行 

int default_width=sheet.getColumnWidth(1);//default_width=2048
sheet.setColumnWidth(1, default_width*2);


CellStyle baseCellStyle = workbook.createCellStyle();  
Font font=workbook.createFont();
//font.setColor(Font.COLOR_RED );
font.setColor( IndexedColors.BLACK.getIndex());//文字颜色 (short)0xc 
//font.getBold(); 
  
baseCellStyle.setFont(font); 
baseCellStyle.setBorderTop(BorderStyle.THIN);
baseCellStyle.setBorderBottom(BorderStyle.THIN);
baseCellStyle.setBorderLeft(BorderStyle.THIN);
baseCellStyle.setBorderRight(BorderStyle.THIN);
baseCellStyle.setAlignment(HorizontalAlignment.CENTER);
baseCellStyle.setWrapText(true);//换行  

CellStyle numberCellStyle=workbook.createCellStyle(); 
numberCellStyle.cloneStyleFrom(baseCellStyle);
DataFormat df = workbook.createDataFormat();  
numberCellStyle.setDataFormat(df.getFormat("#,#0.00")); //小数点后保留两位 

//背景色样式
 CellStyle colorCellStyle=workbook.createCellStyle(); 
 colorCellStyle.cloneStyleFrom(baseCellStyle);
 colorCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
 colorCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 


//红色字体样式
Font redFont=workbook.createFont(); 
redFont.setColor( IndexedColors.RED.getIndex());

//日期样式
CellStyle dateCellStyle=workbook.createCellStyle(); 
dateCellStyle.cloneStyleFrom(baseCellStyle);
short shortDateFormat=workbook.createDataFormat().getFormat("yyyy-mm-dd"); 
dateCellStyle.setDataFormat(shortDateFormat);  

Row row = sheet.createRow(0);//如要写第二列，就要使用sheet.getRow(0)了,如果还createRow就会把第一列的内容清了
Cell cell0 = row.createCell(0);
cell0.setCellValue( 10000 );
cell0.setCellType(CellType.NUMERIC);
cell0.setCellStyle(baseCellStyle);

Cell cell1 = row.createCell(1);
cell1.setCellValue( "中国我爱你" );
cell1.setCellStyle(colorCellStyle);

Cell cell2 =row.createCell(2, CellType.NUMERIC);
cell2.setCellValue(3.0);  
cell2.setCellStyle(baseCellStyle);

XSSFRichTextString rich=new XSSFRichTextString("中华人民共和国");
rich.applyFont(0, 3, redFont);
//rich.applyFont(font);
Cell cell3=row.createCell(3);
cell3.setCellValue(rich);  


Cell cell4=row.createCell(4); 
cell4.setCellStyle(dateCellStyle);
cell4.setCellValue(new Date());

workbook.write(out);
out.close();

 


//读，数据量大时可使用SAX解析
InputStream inp =file.getInputStream();
Workbook wb = WorkbookFactory.create(inp);
Sheet sheet = wb.getSheetAt(0);
int  total=sheet.getLastRowNum();
Iterator<Row> i=sheet.rowIterator();
while(i.hasNext())
{
	//Row row = sheet.getRow(i++);
	Row row=i.next(); 
 Cell cell = row.getCell(0);  
 System.out.println(readCellValue(cell));  
 System.out.println(readCellValue( row.getCell(1)));
 System.out.println(readCellValue( row.getCell(2)));
          
}

public  static Object readCellValue(Cell cell)
	{
		CellType type=cell.getCellType();
		Object res=null;
		switch (type)
    	{
    		case NUMERIC :
    			if (HSSFDateUtil.isCellDateFormatted(cell)) {//读日期格式
         res=cell.getDateCellValue(); 
         break;
    	  }  
    			res=cell.getNumericCellValue();
    			break; 
    		case  STRING:
    			res=cell.getStringCellValue();
       break;
    		case  FORMULA: //公式
    			//res=cell.getCellFormula();
       		res=cell.getStringCellValue();
    			break;
    	}
		return res;
	}
 
public static void wirteContraintData() throws Exception
	{
		Workbook 	workbook=new XSSFWorkbook();
		FileOutputStream out=new FileOutputStream("/tmp/workbook.xlsx"); 
		Sheet sheet = workbook.createSheet();  
		
		sheet.protectSheet("");//不可编辑1 保护工作表，的密码
		//如保允许增加，删除行？？？？
		
		CellStyle baseCellStyle = workbook.createCellStyle();  
		baseCellStyle.setLocked(false);//不可编辑2 默认值为true,保护工作表后生效
		//单元格锁定样式
	    CellStyle lockCellStyle=workbook.createCellStyle(); 
	    lockCellStyle.cloneStyleFrom(baseCellStyle); 
	    lockCellStyle.setLocked(true);//只读单元格 
	    int noEditCol=2;
		 sheet.setDefaultColumnStyle(noEditCol, lockCellStyle);//不可编辑3,列级别
				
		
		 DataValidationHelper help = new XSSFDataValidationHelper((XSSFSheet)sheet);
		 int maxRows=1048576 -1 ;//excel 2010 最长 1048576 -1 =0xFFFFF
		//int maxRows=0xFFFFF;
		 {//下拉列表
				int columnNum=0;
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) help.createExplicitListConstraint(new String[] {"未知","男","女"});
//				XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(new String[] {"未知","男","女"});
				CellRangeAddressList regions=new CellRangeAddressList(1,maxRows,columnNum,columnNum);//(int firstRow, int lastRow, int firstCol, int lastCol)
				DataValidation validation = help.createValidation(constraint, regions);
				 
				//如输入不合法，不可输入
			    validation.setEmptyCellAllowed(true); //可为空
		        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);  
		        validation.setShowPromptBox(true);
				validation.createErrorBox("Error", "请下拉选择值");
			    validation.setShowErrorBox(true);
//		      validation.setSuppressDropDownArrow(true);
		        
				sheet.addValidationData(validation);  
		 }
		 {
			 //数字范围 
			 int columnNum=1; 
//	        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) help.createNumericConstraint( DVConstraint.ValidationType.INTEGER, DVConstraint.OperatorType.BETWEEN, "10", "100" ) ; 
	        XSSFDataValidationConstraint dvConstraint= new XSSFDataValidationConstraint(DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0.00", "100.00");
			CellRangeAddressList addressList = new CellRangeAddressList(1, maxRows, columnNum, columnNum);
			XSSFDataValidation validation = (XSSFDataValidation) help.createValidation(dvConstraint, addressList);

			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);  
			validation.setShowPromptBox(true);
			validation.createErrorBox("Error", "小数范围从0-100");
			validation.setShowErrorBox(true);  
//			validation.setSuppressDropDownArrow(true);
			
			sheet.addValidationData(validation);  
		 }
		
		{//标题
			Row row = sheet.createRow(0); 
			Cell sexTitle = row.createCell(0);
			sexTitle.setCellValue( "姓别" );  
			
			Cell sexAge = row.createCell(1);
			sexAge.setCellValue( "年龄" ); 
		}
		{//数据
			//正常数据
			Row row1 = sheet.createRow(1); 
			Cell sexVal = row1.createCell(0);
			sexVal.setCellValue( "男" );
			sexVal.setCellStyle(baseCellStyle);
			
			Cell ageVal = row1.createCell(1);
			ageVal.setCellValue( 20 );
			ageVal.setCellStyle(baseCellStyle);
			
			Cell noEditVal = row1.createCell(2);
			noEditVal.setCellValue( "不可修改值" );
			noEditVal.setCellStyle(lockCellStyle);//不可编辑3,单元格级别
			//没有createCell列的也是不可编辑的
			
			//异常数据
			Row row2 = sheet.createRow(2); 
			sexVal = row2.createCell(0);
			sexVal.setCellValue(  "x" );//还是可以写入的
			sexVal.setCellStyle(baseCellStyle);
			
			ageVal = row2.createCell(1);
			ageVal.setCellValue( -10 );//还是可以写入的
			
//			ageVal.setCellStyle(baseCellStyle);
			ageVal.setCellType(CellType.STRING);//修改单元格内容后，还是会变数据，这种不会，影响生成的excel的单元格式，
			
			CellStyle numberStringStyle = workbook.createCellStyle(); 
			DataFormat  format = workbook.createDataFormat();
			numberStringStyle.setDataFormat(format.getFormat("@"));
			ageVal.setCellStyle(numberStringStyle);//生成的excel的单元格式是文本格式
			
		} 
		workbook.write(out);
		out.close();
	}
-------jpeg
BufferedImage buffImg=new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_RGB);//可缩放
Graphics2D g=buffImg.createGraphics();
g.drawImage(...);
g.dispose();
//CODEC的全称=Coder and Decoder ,apache

---------------------------------Log4j 2
 

    <dependencies>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.11.0</version>
      </dependency>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.11.0</version>
      </dependency>
	  
	<dependency>
	  <groupId>org.apache.logging.log4j</groupId>
	  <artifactId>log4j-slf4j-impl</artifactId>
	  <version>2.11.1</version>
	</dependency>

    </dependencies>
	
log4j-api-2.1.jar
log4j-core-2.1.jar
log4j-slf4j-impl-2.1.jar  如用slf4j
 

classpath下名为 log4j2.xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration xmlns:xi="http://www.w3.org/2001/XInclude"
	status="WARN" monitorInterval="30" > <!-- monitorInterval 修改自动加载,单位是秒,这个配置最小5 -->
	<!-- 
	<Properties>
		<Property name="filename">c:/tomcat_log/${sys:os.name}/log4j2_app.log</Property> <!--  也可用  $${sys:os.name} -->
		<Property name="LOG_HOME">c:/tomcat_log</Property>
	</Properties>
	 -->
	<xi:include href="log4j2-xinclude-props.xml" />
	     
	<Appenders>
		<Console name="Console" target="SYSTEM_OUT">
			<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
		</Console>
		<File name="File" fileName="${filename}">
			<PatternLayout>
				<pattern>%d %p %C{1.} [%t] %m%n</pattern>
			</PatternLayout>
		</File>
		<RollingFile name="RollingFile" fileName="${LOG_HOME}/${sys:os.name}/${date:yyyy-MM}_rollFile.log"
			filePattern="${LOG_HOME}/archive/${date:yyyy-MM}/app-%d{yyyy-MM-dd}-%i.log.gz">  <!-- $${date:yyyy-MM} 可以一个或者两个 $  -->
			<PatternLayout>
				<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
			</PatternLayout>
			<Policies>
				<TimeBasedTriggeringPolicy />
				<SizeBasedTriggeringPolicy size="10 MB" /> <!-- 大小 好像不准??? -->
			</Policies>
		</RollingFile>
		<NoSql name="mongodbAppender"> <!--加 log4j-nosql-2.1.jar mongo-java-driver-2.13.0-rc1.jar  -->
			<MongoDb databaseName="log4j2" collectionName="MyApp"
				server="127.0.0.1" username="log4j2Admin" password="log4j2" />
		</NoSql>
	</Appenders>
	<Loggers>
		<Logger name="apache_log4j2" level="TRACE"  additivity="false">
			<AppenderRef ref="Console"/>
			<AppenderRef ref="RollingFile"/>
		</Logger>
		<Root level="error">
			<AppenderRef ref="Console" />
		</Root>
	</Loggers>
</Configuration>

//%d 默认格式 同    %d{yyyy-MM-dd HH:mm:ss,SSS}
//%t thread
// %c{1.}  或  %logger{1.}  包名只用一个字母
// %p 或 %level{WARN=W, DEBUG=D, ERROR=E, TRACE=T, INFO=I}    

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

//代码指定配置文件位置 方式一
String configFile=new File("./src/apache_log4j2/log4j2.xml").getAbsolutePath();
System.setProperty("log4j.configurationFile", "file://"+configFile);

		
//代码指定配置文件位置 方式二
ConfigurationSource source = new ConfigurationSource(TestLog4j2.class.getResourceAsStream("/apache_log4j2/log4j2.xml"));  
Configurator.initialize(null, source);
//但 xi:include 容易找不到,必须为<xi:include href="src/apache_log4j2/log4j2-xinclude-props.xml" />

Logger logger = LogManager.getLogger(TestLog4j2.class);


	
 
---------------------------------SLF4J
替代 Spring 使用的 commons-logging 加 jcl-over-slf4j-1.7.6.jar
 
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.30</version>
</dependency>

使用SLF4J(Simple Logging Facade for Java)做日志,为多种日志框架,默认是log4j
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory; 

Logger logger = LoggerFactory.getLogger(My.class); 
logger.info("Today is {}, Temperature set to {}", new Object[]{new java.util.Date(), 20});

logger.error("文件找不到",new FileNotFoundException("/test.txt"));//传入Throwable,会在日志有异常堆栈

---------------------------------logback 依赖于 SLF4J
可以把 org.springframework.context 简化为o.s.c   ,INFO 变为 I , 日志量变少

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version> 
</dependency>

logback-classic 会自动依赖  logback-core 和 slf4j-api


依赖 slf4j-api-1.7.6.jar , logback-core-1.1.2.jar  ,logback-classic-1.1.2.jar  直接实现了SLF4J API
如果用<if condition ,要 Janino 库 janino-2.7.5.jar 依赖 commons-compiler-2.7.5.jar


级别包括：TRACE、DEBUG、INFO、WARN和ERROR
如果logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别
XML配置的级别还可以是 ALL和OFF。还可以是一个特殊的字符串“INHERITED”或其同义词“NULL”，表示强制继承上级的级别

logger的 additivity 为false,L的某个祖先P (即P.L) 设置叠加性标识为false，那么，L的输出会发送给
L与P之间（含P）的所有appender，但不会发送给P的任何祖先的appender

pattern,%thread 线程名 ,%-5level级别,%logger{32}名称, %msg日志,%n 换行

%line 行号
%M 方法名
控制台加颜色 %cyan(%logger{50}[%M]:%line)
日期加时区 %d{yyyy-MM-dd HH:mm:ss.SSS'Z',Etc/UTC}


%logger  显示全包名.类名
%logger{0} 只显示类名
%logger{5} 包名.类名 缩短的长度,(包名.前至少1个字符,类名全部)

%.-1level  把INFO 变为  I



classpath下 找 logback-test.xml 如果找不到 再找 logback.xml,也不存在使用 BasicConfigurator 
//java -Dlogback.configurationFile=/path/to/config.xml  默认classpath下的logback.xml
String currentDir=new File(".").getAbsolutePath();// X:/eclipse_java_workspace/J_JavaThirdLib/
System.setProperty("logback.configurationFile",currentDir+"/src/logback/logback.xml");//要使用绝对路径

ConsoleAppender 输出用模式为    %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n   的PatternLayoutEncoder进行格式化。还有，根logger默认级别是DEBUG

logback-1.1.2\logback-examples\src\main\java\chapters\configuration\sample0.xml

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

Logger logger = LoggerFactory .getLogger("my.biz");
Logger logger2 = LoggerFactory .getLogger("my.biz");
System.out.println(logger==logger2);//相同的对象

Object date = new java.util.Date(); 
logger.debug("today is: {} ", date);

//诊断与logback相关的问题时,MyApp2.java
LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
StatusPrinter.print(lc);//<configuration debug="true">时可打印详细信息

 <configuration scan="true" scanPeriod="30 seconds"> 如配置修改默认 每隔一分钟扫描一次
 
 
Logger rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);


//程序指定配置文件
JoranConfigurator configurator = new JoranConfigurator(); 
configurator.setContext(lc); 
lc.reset(); 
configurator.doConfigure(currentDir+"/src/logback/logback.xml");  
StatusPrinter.printInCaseOfErrorsOrWarnings(lc);


示例 onConsoleStatusListener.xml   中配置    <statusListener class="ch.qos.logback.core.status.OnConsoleStatusListener" />
示例 contextName.xml  中配置   <contextName>myAppName</contextName>  用于区分不同的项目  %contextName
示例 containingConfig.xml  格式
	<!-- <include file="src/logback/includedConfig.xml" />  -->
	<include resource="logback/includedConfig.xml" /> 内容以 <included>为根

示例 variableSubstitution3.xml
	
XML中配置中的变量 ${myName}形式,myName为系统属性 ,logback自带一个${HOSTNAME},
也可在XML中定义变量 <property name="LOG_HOME" value="/MyApp/logs" />
  
<property resource="logback/app.properties" /> <!--  resource 是从classpath路径中找文件,做为变量 -->

<!-- 也可用于DEV,SIT,UTA 环境的切换  ,日志报要 Janino 库 janino-2.7.5.jar 依赖 commons-compiler-2.7.5.jar -->
<if condition='property("os.name").contains("Windows")'>  
	<then>
		<property file="./src/logback/windows.properties" /><!--相对于 应用程序运行的当前目录 ,或者绝对目录,如在windows下如果路径/开头,则是C:下 -->  
	</then>
	<else>
		<property file="./src/logback/linux.properties" />
	</else>  
</if>

变量值可再引用变量   :- 表示变量如未赋值使用后的面的默认值 
${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao.log

<define name="APP_SERVICEOUT_HOME" class="logback.ServcieOutPropertyDefiner"> <!-- 变量值来自程序 -->
	 <logHome>${LOG_HOME}</logHome>
</define>
public class ServcieOutPropertyDefiner implements  PropertyDefiner {
	private String logHome;
	public void setLogHome(String logHome) {
		this.logHome = logHome;
	}
	//这个方法返回值,作为配置
	@Override
	public String getPropertyValue() {
		return logHome+"/performance/";
	}
	//...其它方法
}

SizeAndTimeBasedFNATP is deprecated. Use SizeAndTimeBasedRollingPolicy instead
https://logback.qos.ch/manual/appenders.html#SizeAndTimeBasedRollingPolicy

 <appender name="bizRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
	    <file>${APP_BIZ_HOME}/biz.log</file>
	    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
	      <fileNamePattern>${APP_BIZ_HOME}/biz-%d{yyyy-MM-dd}.%i.log</fileNamePattern> <!-- .zip 可有可无,压缩会节约20倍的空间-->
	       <maxFileSize>100MB</maxFileSize>    
	       <maxHistory>30</maxHistory>
	       <totalSizeCap>10GB</totalSizeCap>
	    </rollingPolicy>
	    <encoder>
	      <pattern>[%contextName] %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	    </encoder>
	 </appender>
  
	<!-- 变量值可再引用变量   :- 表示变量如未赋值使用后的面的默认值  -->
	 <appender name="daoRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
	    <file>${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao.log</file>
	    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
	      <fileNamePattern>${APP_DAO_HOME:-${LOG_HOME}/${CONTEXT_NAME}/dao}/dao-%d{yyyy-MM-dd}.%i.zip</fileNamePattern> 
	       <maxFileSize>100MB</maxFileSize>    
	       <maxHistory>30</maxHistory>
	       <totalSizeCap>10GB</totalSizeCap>
	    </rollingPolicy>
	    <encoder>
	      <pattern>[%contextName] %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	    </encoder>
	 </appender> 

<appender name="serviceOutRolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${APP_SERVICEOUT_HOME}/serviceout.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <fileNamePattern>${APP_SERVICEOUT_HOME}/serviceout_%d{yyyyMMdd}.log</fileNamePattern> 
       <maxFileSize>100MB</maxFileSize>    
       <maxHistory>30</maxHistory>
       <totalSizeCap>10GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
      <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] %msg%n </pattern>
    </encoder>
 </appender>
  
  
<logger name="${CONTEXT_NAME}.dao" level="INFO" additivity="false" >  <!-- Dao层的日志只写文件,不写其它的地方 -->
	<appender-ref ref="daoRolling" />
</logger>

关闭某个类的INFO日志
<logger name="org.hibernate.engine.internal.StatisticalLoggingSessionEventListener" level="WARN"  >   
	<appender-ref ref="STDOUT" />
</logger>


示例 appenders/CountingConsoleAppender.java  自定义appender

全包名(最后一个.前的内容)
类名(最后一个.后的内容)

logback可以把日志推送给logstash  
https://github.com/logstash/logstash-logback-encoder/blob/master/README.md

<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>6.1</version>
</dependency> 

import net.logstash.logback.argument.StructuredArguments;
 
		Map<String,String> myMap=new HashMap<>(); 
		myMap.put("name1", "value1");
		myMap.put("name2", "value2");
		StructuredArgument structArg=StructuredArguments.entries(myMap);
		System.out.println(structArg);//toString 就是正常的json对象
		if(LOG.isInfoEnabled())
		{
			//logback.xml中要使用 LogstashEncoder 或者 Composite Encoder加arguments参数
			LOG.info("define",structArg);//这种没有{}，即无参数，只JSON输出, 不格式化消息
			//调用后，Kibana日志就会有Map中的字段 
		}

<!--  logback可以把日志推送给 logstash  -->
<appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
 <writeBufferSize>16384</writeBufferSize> <!-- 默认缓存是8192 -->
 <destination>127.0.0.1:4560</destination><!-- 可以配置多个destination标签，或一个标签多个值用,分隔-->
  <keepAliveDuration>5 minutes</keepAliveDuration>
  
  <!-- LogstashEncoder输出( kibana查看)信息有，@timestamp时间 ,host主机，level级别,message日志信息，thread_name线程，logger_name(有时不是类名)
  	但没有类名，方法名，行号？？
 -->
 <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder">
 	<customFields>{"AppName":"myProject"}</customFields> <!-- 表示输出的json多加一个字段 -->
 </encoder>
 
 <!--  
 <arguments ></arguments>为代码中使用StructuredArgument用的
  LoggingEventCompositeJsonEncoder  %使用logback的PatternLayout格式  
		  "position": "%class.%method:%-3L",   // kibana显示？ %class{-20}也不行 不认？？
		    官方上的"line_str": "%line", 也不行？？？
		
		<encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
		  <providers>
		    <timestamp/>
		    <pattern>
		      <pattern>
		        {
		        "AppName": "myProject",  
		        "LO": "%logger",
		        "LE": "%level",
		        "TH": "%thread",
		        
		        "line_str": "%line",
		        "position": "%class{-20} . %method : %-3L",
		        
		        "ME": "%message"
		        }
		      </pattern>
		    </pattern>
		  </providers>
		</encoder>
	    -->	 
</appender>
 
 
 
logstash.conf 的(为logback) 输入配置 
input {
    tcp {
        port => 4560
        codec => json_lines
    }
}
output默认就是本机的elasticsearch



//这个类会在 启动 SpringBoot项目的 main方法前被执行
//这个类要配置在logback.xml中才生效 <contextListener class="logback.MyListener"/>
public class MyListener extends ContextAwareBase  implements LoggerContextListener,LifeCycle{
	private boolean started=false;
	@Override
	public boolean isStarted() { 
		return false;
	}
	@Override
	public void start() {
		if(started)
			return;
		Context context=getContext();
		context.putProperty("INIT_ROOT_LEVEL", "debug");
		//这个就可以放在logback.xml中${INIT_ROOT_LEVEL}
		started=true;
	}
}
-------------jCIFS   samba SMB
	apache commons VFS2 库的CIFS协议 其实是用 jCIFS 

-------------------------------JSCH
<dependency>
  <groupId>com.jcraft</groupId>
  <artifactId>jsch</artifactId>
  <version>0.1.55</version>
</dependency>

jCraft的一个项目,是sftp实现

JSch jsch = new JSch();  
Session sshSession = jsch.getSession(username, host, port);
 
String pemPrivateKey="C:\\cygwin64\\home\\dell\\.ssh\\id_rsa";
jsch.addIdentity(pemPrivateKey);//私钥只是使用 ssh-keygen -m PEM 生成的id_rsa 文件

//sshSession.setPassword(password);

Properties sshConfig = new Properties();
sshConfig.put("StrictHostKeyChecking", "no");
sshSession.setConfig(sshConfig);
sshSession.connect();
Channel channel = sshSession.openChannel("sftp");
channel.connect();
ChannelSftp sftp = (ChannelSftp) channel;;

sftp.cd("directory");
sftp.put(new FileInputStream(file), "fileName");
sftp.get("downloadFile", new FileOutputStream(file));
sftp.rm("deleteFile");
sftp.ls("directory");
sftp.rename("/upload.txt.tmp", "/testDir/upload.txt");//同 linux mv 可重命名,也可移动文件

--------------------------------------Apache common net 的FTP 支持断点续传和中文文件 
//上传下载,测试OK	
//\commons-net-2.2-bin\apidocs\src-html\examples\ftp 下有HTML示例

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.PrintCommandListener;

FTPClient ftpClient = new FTPClient();
ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out))));
ftpClient.connect(hostname, port);
if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) 
{
	if (ftpClient.login(username, password)) 
		return true;
	
}else
	ftpClient.disconnect();
ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//二进制
ftpClient.enterLocalPassiveMode();//被动

ftpClient.sendCommand("mkdir test");
ftpClient.mkd("test");//就是mkdir
ftpClient.makeDirectory("test");
ftpClient.changeWorkingDirectory("..");

ftpClient.setRestartOffset(f.length());//断点,f已存在文件的长度
ftpClient.retrieveFile("remote.txt",new FileOutputStream(""));//下载

ins.skip(remoteSize); //ins InputStream
ftpClient.storeFile("newremote.txt",ins)//上传

tpClient.logout();
if (ftpClient.isConnected()) 
	ftpClient.disconnect();

ftpClient.setBufferSize(1000);


ftps =new FTPSClient("SSL");//与使用 FTPClient相同, 注意这里是ftps(ssl),vsftp支持SSL,不是sftp(ssh)

ANT 也有 ftp任务


-------------------------------commons-lang
implements Serializable{ 
    private static final long serialVersionUID = 1L;
	
public boolean equals(Object obj) 
{
	return  EqualsBuilder.reflectionEquals( this , obj);
		
//		if(this==obj)
//			return true;
//		if(! (obj instanceof Product)) //obj not null
//			return false;
//		Product p=(Product)obj;
//		return new EqualsBuilder().append(this.id ,p.id)
//		.append(this.name ,p.name)
//		.append(this.type ,p.type).isEquals();
}
public int hashCode() {
	 return  HashCodeBuilder.reflectionHashCode( this );
	//		 return new HashCodeBuilder()  
	//         .append(this.id)  
	//         .append(this.name)
	//         .append(this.type)
	//         .toHashCode();  
}
public String toString() {
		return ToStringBuilder.reflectionToString(this);
		//return ReflectionToStringBuilder.toString( this );
 //		return 	new  ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//		.append("id",this.id)
//		.append("name",this.name)
//		.append("type",this.type)
//		.toString();

}

org.apache.commons.lang.StringUtils  isBlank
									join(listArray,",");
									
-------------------------------commons-lang3
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>

-------------------------------commons codec 
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>
import org.apache.commons.codec.digest.DigestUtils;

byte[] data="hello".getBytes(Charset.forName("UTF-8"));
byte[] sha1Byte=DigestUtils.sha1(data); 
String bytehexStr=MyByteArrayUtil.byteToHexString(sha1Byte);
String hexStr=DigestUtils.sha1Hex(data);
System.out.println(hexStr.equals(bytehexStr));

String sha1Hex=DigestUtils.sha1Hex(new FileInputStream(file));
System.out.println(sha1Hex);

String md5Hex=DigestUtils.md5Hex(new FileInputStream(file));
System.out.println(md5Hex);

---
import org.apache.commons.codec.binary.Base64;

byte[] data;
Base64 base64 = new Base64();
byte[] res= base64.encode(data);
res=base64.decode(data);

Base64.decodeBase64(data);


---------------------------------commons beanutils
import org.apache.commons.beanutils.BeanUtils;
 
PersonBean dest = new PersonBean();
UserBean orig =new UserBean();
orig.setAge(20);
orig.setBirthday(new Date());
orig.setName("lisi");
 
BeanUtils.copyProperties(dest, orig); // commons.beanutils 和 spring都有，双方不同类都有匹配不上的字段也可正常用
 	
 ConvertUtils.register(new Converter()
	 {
		  
		 @Override
		 public Object convert(Class arg0, Object arg1)
		 {
			 if(arg1 == null)
			 {
				 return null;
			 }
			 if(!(arg1 instanceof String))
			 {
				 throw new ConversionException("只支持字符串转换 !");
			 }
			  
			 String str = (String)arg1;
			 if(str.trim().equals(""))
			 {
				 return null;
			 }
			  
			 SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 try {
				return sd.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 return null;
		 }
		  
	 }, java.util.Date.class);//map要只字串，可转成日期
	 
	PersonBean person = new PersonBean();
	Map<String, Object> mp = new HashMap<String, Object>();
	mp.put("name", "Mike");
	mp.put("age", 25);
	mp.put("mN", "male");
	mp.put("birthday", "2017-08-20 14:20:10");	
	BeanUtils.populate(person, mp); //org.apache.commons.beanutils
  
  
//也可用 java.beans.Introspector 做  Map --> Bean , Bean -> Map
BeanUtils.populate(obj, map); // Map --> Bean 

-------------------------------commons-io
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.6</version>
</dependency>

FileUtils.copyInputStreamToFile(in, new File("E:/file.txt"));
 
------------------------------Bcrypt Java 实现 jBCrypt
Spring Security 使用bcrypt

http://www.mindrot.org/projects/jBCrypt/
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
// Hash a password for the first time
String password = "testpassword";
String hashed = BCrypt.hashpw(password, BCrypt.gensalt());//每次生成的结果不一样，但可以认证成功
System.out.println(hashed);
// gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt(12));
System.out.println(hashed2);
// Check that an unencrypted password matches one that has
// previously been hashed
String candidate = "testpassword";
// String candidate = "wrongtestpassword";
if (BCrypt.checkpw(candidate, hashed))
	System.out.println("It matches");
else
	System.out.println("It does not match");
------------------------------AES 增强的

/**
 * 微信的工具类 
 * AES采用CBC模式，数据采用PKCS#7填充至32字节的倍数；IV初始向量大小为16字节，取AESKey前16字节
 *  <dependency>
	    <groupId>org.bouncycastle</groupId>
	    <artifactId>bcprov-jdk15to18</artifactId>
	    <version>1.69</version>
	</dependency> 
 */
public class WxAESUtil { 
	static {
		  //导入支持AES/CBC/PKCS7Padding的Provider
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static byte[]  aesDecode(byte[] encryptBytes, byte[] aesKey) {  
		try{ 
			byte[] ivByte= Arrays.copyOf(aesKey,16);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");   
			AlgorithmParameters params = AlgorithmParameters.getInstance("AES");  
			params.init(new IvParameterSpec(ivByte));  
	 
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, "AES"),params);    
			byte[] decryptBytes = cipher.doFinal(encryptBytes);   
			return decryptBytes;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	 
	}
	//jdk8 版本至少为 1.8.0_161 ，如少于要官方下载JCE文件格覆盖JDK中的lib\security 目录文件  
	//加密自己写的企业微信加密码一直不对，官方示例代码可以  (foxinmy.weixin4j 项目的 MessageUtil.加密也不行,但解密可以)
	public static byte[] aesEncode(byte[] fromByte,  byte[] aesKey)   {  
		try{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
				IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
				SecretKeySpec keySpec= new SecretKeySpec(aesKey, "AES");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec,iv);
	//这两种结果是一样的
//	            byte[] ivByte= Arrays.copyOf(aesKey,16);
//	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
//	            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
//	            params.init(new IvParameterSpec(ivByte));
//	            SecretKeySpec keySpec= new SecretKeySpec(aesKey, "AES");
//	            cipher.init(Cipher.ENCRYPT_MODE, keySpec,params);
			return cipher.doFinal(fromByte);  
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
}
-------------------------------commons compress
//--tar.gz 解压
public static void decompressTarGzFile(String tarGzFile,String outDir) 
{
	ArchiveInputStream in = null;
	byte buffer[]= new byte[1024];
	try {
		 //GZIPInputStream is = new GZIPInputStream(new FileInputStream(tarGzFile));
		 //in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
		//二选一
		in =new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(tarGzFile), 1024)); 
		TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
		while (entry != null) 
		{
			String name = entry.getName();// 如果是目录以/结尾 ,手工建立目录
			if(name.endsWith("/"))
				new File(outDir+"/"+name).mkdir();
			else
			{
				FileOutputStream out = new FileOutputStream(outDir+"/"+name,false);//overwrite 
				int len=-1;
				while ((len = in.read(buffer)) != -1)
					out.write(buffer,0,len);
				out.flush();
				out.close();
			}
			entry = (TarArchiveEntry) in.getNextEntry();
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try { in.close();  } catch ( Exception e) { }
	}
}

//----tar.gz 压缩
org.apache.commons.compress.utils.IOUtils
org.apache.commons.io.IOUtils //closeQuietly 过时了
 //只压缩一个文件 
protected static void compressOneTarGzFile(File srcFile, File destFile)
{  
	TarArchiveOutputStream out = null;  
	InputStream is = null;  
	try {  
		out = new TarArchiveOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(destFile)), 1024)); 
		
		is = new BufferedInputStream(new FileInputStream(srcFile), 1024);  
		TarArchiveEntry entry = new TarArchiveEntry(srcFile.getName());//这里不能传绝对目录的String,如果一个文件要getName(),不然会有上级目录名在包中
		entry.setSize(srcFile.length());  
		out.putArchiveEntry(entry);  
		IOUtils.copy(is, out);  
		out.closeArchiveEntry();  
	} catch(Exception ex)
	{
		ex.printStackTrace();
	}finally { 
		IOUtils.closeQuietly(is);  
		IOUtils.closeQuietly(out);  
	}  
}  
//递归压缩目录
public static void compressTarGzFromFileOrDir(String srcFile, String destFile)
{
	TarArchiveOutputStream out = null;  
	try {
		out = new TarArchiveOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(destFile)), 1024));
		compressWalkDir(new File(srcFile),out);
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}finally { 
		IOUtils.closeQuietly(out);  
	}  
}
private static void compressWalkDir(File from,TarArchiveOutputStream out) throws Exception
{
	if(from.isDirectory() )
	{
		TarArchiveEntry entry = new TarArchiveEntry(from);
		out.putArchiveEntry(entry);
		out.closeArchiveEntry();
		for(File fileItem:from.listFiles())
			compressWalkDir(fileItem,out);
	}else //file
	{
		InputStream  input = new BufferedInputStream(new FileInputStream(from), 1024);  
		TarArchiveEntry entry = new TarArchiveEntry(from);
		entry.setSize(from.length());  
		out.putArchiveEntry(entry);  
		IOUtils.copy(input, out); 
		out.closeArchiveEntry();
		IOUtils.closeQuietly(input);  
	}
}
 
while( org.apache.commons.compress.utils.IOUtils.readFully(socketInput, readBuffer) != 0 )//把socket 的输入流读满缓冲区才返回
 
IOUtils.LINE_SEPARATOR ,使用 PrintWriter的println实现

-----------------------------文件上传 commons-fileupload--
request.setCharacterEncoding("UTF-8");


DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(20*1024*1024);//20M内存
factory.setRepository(new File("c:/temp"));//临时目录
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setSizeMax(200*1024*1024);//文件最大200M
List<FileItem> items=null;
try {
	items = upload.parseRequest(request);
} catch (FileUploadException e) 
{
	e.printStackTrace();
}
Iterator iter = items.iterator();
while (iter.hasNext())
{
	FileItem item = (FileItem) iter.next();

	if (item.isFormField())
	{	//有一个Filename=xxx的对
		String name = item.getFieldName();
		String value = item.getString();//浏览器对request.setCharacterEncoding("UTF-8");的设置无效
		System.out.println(name+"="+new String(value.getBytes("iso8859-1"),"UTF-8"));//对浏览器 OK
		System.out.println(name+"="+value);//对HttpClient UTF-8 OK
	} else {
			String fieldName = item.getFieldName();//form 的名字
			String pathName = item.getName();//只有IE 带C:/   浏览器可以和request.setCharacterEncoding("UTF-8");一起使用
			if("".equals(pathName))
			    continue;
			String correctName=pathName.substring(pathName.lastIndexOf(File.separator)+1);//浏览器对中文名OK
 			//String contentType = item.getContentType();
			//long sizeInBytes = item.getSize();

			//File uploadedFile = new File("d:/temp/"+correctName);//快速方式
			//item.write(uploadedFile);

			//为进度条方式
			FileOutputStream output=new FileOutputStream(new File("d:/temp/"+correctName));
			InputStream input = item.getInputStream();
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=input.read(buffer))!=-1 )
			{
				output.write(buffer,0,len);
			}
			output.close();
			input.close();
	}
}

//portlet 使用 org.apache.commons.fileupload.portlet.PortletFileUpload;
	
下载文件设置HTTP头,文件名
response.reset();
//response.setContentType("application/msexcel");
//response.setHeader("Content-disposition","inline;filename=workbook.xls");//定义文件名
//response.setContentType("application/x-msdownload");
 
response.addHeader("Content-Disposition","attachment; filename=workbook.xls");
response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));   
//text/plain,application/vnd.ms-excel(csv,xls),application/octet-stream(.xlsx)
response.getOutputStream();
	
<form method="post" action="xxx" enctype="multipart/form-data">

-----------------------------commons pool
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.6.0</version>
</dependency>

GenericObjectPoolConfig  (jedis使用这个)

GenericObjectPoolConfig<StringBuffer> config=new GenericObjectPoolConfig<StringBuffer>();

config.setMaxTotal(20);
config.setMaxIdle(10);
config.setMinEvictableIdleTimeMillis(300000);
config.setNumTestsPerEvictionRun(3);
config.setTimeBetweenEvictionRunsMillis(60000);
config.setTestOnBorrow(false);
config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//逐出连接的最小空闲时间 默认
config.setMinEvictableIdleTimeMillis(20*60*1000);//20分

GenericObjectPool<StringBuffer> pool=new GenericObjectPool<StringBuffer>(new StringBufferFactory(),config);
StringBuffer buf = pool.borrowObject();
//....
pool.returnObject(buf);

PooledObject<Connection> obj=new DefaultPooledObject<Connection>(conn);
Connection conn= obj.getObject();
obj.getObject().close();
-------------------------------commons cli
//commons-cli-1.4.jar

//java CommonsCli -c=CN -t

Options options = new Options();
options.addOption("t", false, "display current time"); //false表示没有参数值
options.addOption("c", true, "country code");

CommandLineParser parser = new DefaultParser();
CommandLine cmd = parser.parse( options, args);

if(cmd.hasOption("t")) {
	System.out.println("have t");
} 
String countryCode = cmd.getOptionValue("c");
if(countryCode != null)  {
	System.out.println("countryCode="+countryCode);
}

-----------------------------httpclient 新的是httpcomponents项目下的
httpcore -> httpclient

<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpmime</artifactId>
    <version>4.5.12</version>
</dependency>

<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpcore</artifactId>
	<version>4.4.10</version>
</dependency>

<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.5.6</version>
</dependency>
 

public static void postFormRequest(boolean isUpload) throws Exception //上传不支持中文
{
	CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpPost httppost = new HttpPost(); //POST
	    httppost.setHeader("user-agent","NX_Browser");//header中不能有中文
	    if(isUpload)//要加 httpmime-4.2.1.jar
	    {
	    	httppost.setURI(URI.create("http://127.0.0.1:8080/J_JavaEE/uploadServlet3"));
	    	ContentType textContentType= ContentType.create("text/plain", "UTF-8"); 
	    	
	    	FileBody file1 = new FileBody(new File("d:/tmp/my.jpg"),ContentType.IMAGE_JPEG,"my.jpg");
	    	
			FileBody file2 = new FileBody(new File("d:/tmp/中文.jpg"),textContentType ,"中文.jpg"); //文件名  中文不行 ???????????   
//			FileBody file3 = new FileBody(new File("d:/tmp/my.txt"),ContentType.TEXT_PLAIN ,"my.txt"); 
			 HttpEntity reqEntity = MultipartEntityBuilder.create()
					 .setCharset(Charset.forName("UTF-8"))
					 .addPart("attache1", file1)
					 .addPart("attache2", file2)
					 .addTextBody("departName", "部门名1",textContentType)//中文正常
					 .addPart("username", new StringBody("用户名1",textContentType))//中文正常
//					 .addPart("username", new StringBody("user01",ContentType.MULTIPART_FORM_DATA))
					.build();
			httppost.setEntity(reqEntity);
	}else
	{
		httppost.setURI(URI.create("http://127.0.0.1:8080/J_JavaEE/receiveForm"));
		//---方式1
//		    String encoded= URLEncoder.encode("李四","UTF-8");//不支持中文,要 URLEncoder.
//		    StringEntity reqEntity = new StringEntity("username="+encoded+"&password=123");
		//---方式2
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", "李四"));//支持中文
		formparams.add(new BasicNameValuePair("password", "value2"));
		UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		httppost.setEntity(reqEntity);
	}
	HttpResponse response = httpClient.execute(httppost);
	
	System.out.println("HttpClient到的请求头:");
	Header[] allHeader=  response.getAllHeaders();
	for (int i = 0; i < allHeader.length; i++) 
	{
		  System.out.println(allHeader[i].getName()+"="+allHeader[i].getValue());
	}
	List<Cookie> cookies = httpClient.getCookieStore().getCookies();//JSESSIONID,Session用
	
	HttpEntity respEntity = response.getEntity();
	System.out.println(response.getStatusLine());
	if (respEntity != null) {
	  System.out.println("Response content length: " + respEntity.getContentLength());
	}
  
	//System.out.println(EntityUtils.toString(respEntity));//和 respEntity.getContent()只可调用一个
	BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent(),"UTF-8"));
	//对应Server端response.getOutputStream().write("doPost的响应".getBytes("UTF-8"));
	String line = null;
	while ((line = reader.readLine()) != null)
	{
		System.out.println(line); 
	}
	reader.close();
	httpClient.getConnectionManager().shutdown();
}
public static void download() throws Exception
{
	CloseableHttpClient httpclient = HttpClients.createDefault(); 
	HttpGet httpget = new HttpGet("http://127.0.0.1:8080/J_JavaEE/download?filename="+URLEncoder.encode("文件1.txt","UTF-8"));
	HttpResponse response =httpclient.execute(httpget);
	if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode())
	{
		
		String disposition= response.getHeaders("Content-Disposition")[0].getValue();//attachment; filename=xx
		String fileName=disposition.substring(disposition.indexOf('=')+1);
		String cnFileName=new String(fileName.getBytes("iso8859-1"),"GBK");
		
		HttpEntity entity = response.getEntity(); 
		System.out.println(entity.getContentType());
		InputStream input=entity.getContent();
		FileOutputStream output=new FileOutputStream(new File("c:/temp/"+cnFileName));
		byte[] buffer=new byte[1024];
		int len=0;
		while( (len=input.read(buffer)) != -1  )
		{
			output.write(buffer,0,len);
		}
		input.close();
		output.close();
	}
}
public static void async() throws Exception
{
	CloseableHttpClient httpclient = HttpClients.createDefault(); 
	
	//HttpGet httpMehtod = new HttpGet("http://localhost:8080/J_JavaEE/");//POST
	HttpPost httpMehtod=new HttpPost("http://127.0.0.1:8080/J_JavaEE/receiveForm");
	ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>()
		{
			public byte[] handleResponse(  HttpResponse response) throws ClientProtocolException, IOException 
			{
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				} else {
					return null;
				}
			}
		};
	byte[] response = httpclient.execute(httpMehtod, handler);
	System.out.println("返回="+new String(response,"UTF-8"));
}
public static void proxyTest() throws Exception  
{
	String domainUserId="domain/userid";
	String domain ="domain";
	String userId="userid";
	String password="xxx";
	String proxyIp="172.52.17.184";
	int port=8080;
		
	
	// 设置代理HttpHost
	HttpHost proxy = new HttpHost(proxyIp, port, "http");
	// 设置认证
	CredentialsProvider provider = new BasicCredentialsProvider();
	provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(userId, password));

	CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

	RequestConfig config = RequestConfig.custom().setProxy(proxy)
			 .setConnectTimeout(10000)
			.setSocketTimeout(10000)
			.setConnectionRequestTimeout(3000)
			.build();

	HttpGet httpGet = new HttpGet("/");
	httpGet.setConfig(config);

	HttpHost target = new HttpHost("baidu.com", 80);
	CloseableHttpResponse response = httpClient.execute(target, httpGet);

	if (response.getStatusLine().getStatusCode() == 200)
	{
		System.out.println("OK");
	}
	HttpEntity entity = response.getEntity();
	System.out.println("响应状态:"+response.getStatusLine());
	// 显示结果
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
	String line = null;
	while ((line = reader.readLine()) != null) 
	{
	  System.out.println(line);
	}
	reader.close();
}


-----------------------------okhttp
支持HTTP/2 ， TLS 
Android,spring cloud kubernetes,selenium-java使用，主要是为Android,使用gradle构建
4 版本的包名为兼容也叫  okhttp3
依赖 Okio(是kotlin语言标准库)

implementation("com.squareup.okhttp3:okhttp:4.6.0")
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.6.0</version>
</dependency>
public static void getRequest( )  
{
	OkHttpClient client = new OkHttpClient();
	Request request = new Request.Builder()
			.get()
			.url("https:www.baidu.com")
			.build();
	Call call = client.newCall(request);
	//同步调用
	try
	{
		Response response = call.execute();
		System.out.println("同步调用返回"+response.body().string());
	}catch(Exception e )
	{
		e.printStackTrace();
	}
	/*
	//异步调用,并设置回调函数,执行完不会退出
	call.enqueue(new Callback() {
		@Override
		public void onFailure(Call call, IOException e) {
			System.out.println("异步调用失败"+e);
		}
		@Override
		public void onResponse(Call call, final Response response) throws IOException {
			final String res = response.body().string();
			System.out.println("异步调用返回"+res);
		}
	});
	*/
}
public static void postRequest() 
{
	FormBody formBody = new FormBody.Builder()
			.add("username", "用户名user")
			.add("password", "pass")
			.build(); 
	OkHttpClient client = new OkHttpClient();
	Request request = new Request.Builder()
			.post(formBody)
			.url("http://127.0.0.1:8080/J_JavaEE/receiveForm")
			.build();
	Call call = client.newCall(request);
	try
	{
		Response response = call.execute();
		System.out.println("同步调用返回"+response.body().string());
	}catch(Exception e )
	{
		e.printStackTrace();
	}
}
public static void uploadFile( ) //文件名 为中文乱码？？
{
	File file = new File("d:/tmp/my.jpg");
	File fileCn = new File("d:/tmp/中文.jpg");
	
	MediaType cnMedia=MediaType.parse("application/octet-stream");
	cnMedia.charset(Charset.forName("UTF-8"));
		
	RequestBody muiltipartBody = new MultipartBody.Builder()
			//一定要设置这句
			.setType(MultipartBody.FORM)
			.addFormDataPart("username", "用户名1")//中文可以
			.addFormDataPart("departName", "部门名2")
			.addFormDataPart("password", "pass") 
			.addFormDataPart("attache1", "my.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))
			 .addFormDataPart("attache2", "中文.jpg", RequestBody.create(cnMedia, fileCn))//服务端要转换为UTF-8才行	
			.build(); 
	//RequestBody requestBodyImg = RequestBody.create(MediaType.parse("application/octet-stream"), file);
	OkHttpClient client = new OkHttpClient();
	Request request = new Request.Builder()
			.post(muiltipartBody)
			.url("http://127.0.0.1:8080/J_JavaEE/uploadServlet3")
			.build();
	Call call = client.newCall(request);
	try
	{
		Response response = call.execute();
		System.out.println("同步调用返回"+response.body().string());
	}catch(Exception e )
	{
		e.printStackTrace();
	}
}
public static void downloadFile() throws Exception //不能拿到服务端 中文 文件名？？
{
	OkHttpClient client = new OkHttpClient();
	final Request request = new Request.Builder()
			.get()
			.url("http://127.0.0.1:8080/J_JavaEE/download?filename="+URLEncoder.encode("文件1.txt","UTF-8"))
			.build();
	Call call = client.newCall(request);
	call.enqueue(new Callback() 
	{
		@Override
		public void onFailure(Call call, IOException e) {
			 System.out.println("异步调用失败"+e);
		}
		@Override
		public void onResponse(Call call, Response response) throws IOException 
		{
			String disposition= response.header("Content-Disposition") ;//attachment; filename=xx
			String fileName=disposition.substring(disposition.indexOf('=')+1);
			String cnFileName=new String(fileName.getBytes("iso8859-1"),"UTF-8");//不能转换为正常的中文 UTF-8,GBK都不行？？？
			
			InputStream is = response.body().byteStream();
			int len = 0;
			File file  = new File("e:/tmp/result.txt");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[128];
			while ((len = is.read(buf)) != -1){
				fos.write(buf, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();
		}
	});
}
-----------------------------websocket Java Client(Java-WebSocket)
spring webflux 有 WebSocketClient

https://github.com/TooTallNate/Java-WebSocket
Java 1.6 and higher
Android 4.0 and higher

支持wss:协议(certificate)

<dependency>
    <groupId>org.java-websocket</groupId>
    <artifactId>Java-WebSocket</artifactId>
    <version>1.4.0</version>
</dependency>

//websocket 做客户端
WebSocketClient	cc = new WebSocketClient( new URI( "ws://localhost:8080/J_JavaEE/webSocket")  )
		{
			@Override
			public void onMessage( String message ) {
				System.out.println( "got: " + message + "\n" );
			}
			@Override
			public void onOpen( ServerHandshake handshake ) {
				System.out.println( "You are connected to ChatServer: " + getURI() + "\n" );
			}
			@Override
			public void onClose( int code, String reason, boolean remote ) {
				System.out.println( "You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n" );
			}
			@Override
			public void onError( Exception ex ) {
				System.out.println( "Exception occured ...\n" + ex + "\n" );
				ex.printStackTrace();
			}
		};
		cc.connect();
		Thread.sleep(2*1000);//等连接完成
		cc.send( "java_websocket 端 消息" );
		System.out.println( "已经向服务端发送消息");
		Thread.sleep(2*1000);//等服端回消息
		cc.close();


-----------------------------Quartz 
 <dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz</artifactId>
  <version>2.2.3</version>
</dependency>
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz-jobs</artifactId>
  <version>2.2.3</version>
</dependency>  

使用c3p0数据源


@DisallowConcurrentExecution
public class MyQuartzJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    JobKey jobKey = context.getJobDetail().getKey();
	    String user_id=context.getJobDetail().getJobDataMap().getString("user_id");
		System.out.println("in MyQuartzJob ,key= "+jobKey+",user_id="+user_id);
	}
}
SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		JobDetail job = JobBuilder.newJob(MyQuartzJob.class)
			//.usingJobData(dataKey, value)
			.withIdentity("myJob", "group1")
			.build();
  
		job.getJobDataMap().put("user_id", "zhangsan");
	
		
		//------------十一假期在要crond中除
		HolidayCalendar holiday=new HolidayCalendar();
		Calendar nationalDay= Calendar.getInstance();
		//nationalDay.set(2012,Calendar.OCTOBER, 1);
		nationalDay.set(2013,Calendar.JANUARY, 6);//只, 年月日 ,是有用的
		
		holiday.addExcludedDate(nationalDay.getTime());
		sched.addCalendar("nationalDay", holiday, true, false);// boolean replace, boolean updateTriggers
		//------------
		Trigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("myTrigger", "group1")
		//.startNow()
		.startAt(new java.util.Date(Calendar.getInstance().getTimeInMillis()+5000))
		
		.modifiedByCalendar("nationalDay") //设置假期
		
//		.withSchedule(SimpleScheduleBuilder.simpleSchedule()
//			.withIntervalInSeconds(3)
//			.withRepeatCount(10))//假期是当天就不能启动了
		      
		.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * 8-17 * * ?"))
		//.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(3))
		//.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16, 0))
		//.forJob("myJob", "group1");
/*
//cron表达式,空格分隔的顺序是,不同于的是,linux是没有秒的
1.Seconds					0-59
2.Minutes					0-59
3.Hours						0-23
4.Day-of-Month				1-31
5.Month						between 0 and 11, or by using the strings JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV and DEC.
6.Day-of-Week				1 到 7 (1 = Sunday) ,也可用SUN, MON, TUE, WED, THU, FRI and SAT
7.Year (optional field)
-----
 '?' character is allowed for the day-of-month and day-of-week fields  ,"no specific value"
  'L' character is allowed for the day-of-month and day-of-week fields ,"last"
 		in the day-of-week field by itself, it simply means "7" or "SAT"
 		in the day-of-week field "6L" or "FRIL" both mean "the last friday of the month"
  '3/20' in the Minutes field, it would mean 'every 20th minute of the hour, starting at minute three' - or in other words it is the same as specifying '3,23,43' in the Minutes field
  "6#3" or "FRI#3" in the day-of-week field means "the third Friday of the month".
*/	  
			.build();
	
		  sched.scheduleJob(job, trigger);

		  
		  
		  
//		  sched.addJob(job,true);
//		  sched.scheduleJob(trigger);//triggerBuild.forJob
		  
//		  TriggerKey triggerKey=new TriggerKey("myTrigger","group1");
//		  sched.unscheduleJob(triggerKey);//删任务 
		  
//		  JobKey jobKey=new JobKey("myJob","group1");
//		  sched.checkExists(jobKey);
		  
	  
	/*	  List<Trigger> triggers=(List<Trigger>)sched.getTriggersOfJob(jobKey);//查某个任务的所有定时
		  for(Trigger trig:triggers)
		  {
			  TriggerKey triggerKey=trig.getKey();
			  TriggerState triggerState=sched.getTriggerState(triggerKey);
			  System.out.println(triggerState);//enum ,COMPLETED
		  }
	  */
//		sched.triggerJob(jobKey);//立即启动任务
//		sched.resumeTrigger(triggerKey);//启用
//		sched.pauseTrigger(triggerKey);//暂停

	/*	  
		  GroupMatcher<JobKey> matcher=GroupMatcher.groupEquals("group1");//查所有存的任务
		  Set<JobKey> jobs=sched.getJobKeys(matcher);
		  for(JobKey key:jobs)
		  {
			  JobDetail detail=sched.getJobDetail(key);
			  detail.getClass();
			  detail.getDescription();
			  key.getName();
			  key.getGroup(); 
		  }
		*/
		  
//-------使用配置文件方式	
//分布式 配置 org.quartz.jobStore.isClustered = true
	  
//org/quartz/quartz.properties文件,可以被src\下的文件覆盖,示例在quartz-2.2.3\examples\example10\quartz.properties
//quartz.properties中插件配置去读 quartz_data.xml
//org/quartz/xml/job_scheduleing_data_2_0.xsd
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //会读classpath 下的quartz.properties
scheduler.start(); 
scheduler.shutdown(); 

org.quartz.impl.jdbcjobstore.JobStoreCMT containerManageTransaction;
org.quartz.impl.jdbcjobstore.oracle.OracleDelegate oracle;


//--- quartz.properties 文件(放在classpath路径下) 
#如使用了Spring不会默认读 classpath下的quartz.properties , 要配置quartzProperties属性

org/quartz/quartz.properties 中的值是是以:分隔 ,也可以的
org.quartz.threadPool.threadCount: 10

动态配置cronExpression ,类	extends CronTriggerBean
	setCronExpression(cronExpression)

--- quartz.properties JDBC存储 和 Clustering 配置 

#============================================================================
# Configure Main Scheduler Properties  
#============================================================================

org.quartz.scheduler.instanceName = MyClusteredScheduler
org.quartz.scheduler.instanceId: AUTO

org.quartz.scheduler.skipUpdateCheck: true

#============================================================================
# Configure ThreadPool  
#============================================================================

org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 25
org.quartz.threadPool.threadPriority = 5

#============================================================================
# Configure JobStore  
#============================================================================

org.quartz.jobStore.misfireThreshold: 60000
org.quartz.jobStore.class: org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate


org.quartz.jobStore.useProperties: false
org.quartz.jobStore.dataSource: myDS
org.quartz.jobStore.tablePrefix: QRTZ_ 

org.quartz.jobStore.isClustered = true
org.quartz.jobStore.clusterCheckinInterval = 20000
#============================================================================
# Configure Datasources  
#============================================================================

org.quartz.dataSource.myDS.driver: com.mysql.cj.jdbc.Driver
org.quartz.dataSource.myDS.URL: jdbc:mysql://localhost:3306/mydb
org.quartz.dataSource.myDS.user: user1
org.quartz.dataSource.myDS.password: user1 
org.quartz.dataSource.myDS.maxConnections: 5
org.quartz.dataSource.myDS.validationQuery=select 1

--------------------------------xxl 项目 国产的
xxl-job 定时任务带web管理界面的，大众点评使用

https://www.xuxueli.com/
下载项目源码，导入开发工具

 /xxl-job/doc/db/tables_xxl_job.sql  是MySQL脚本
 /xxl-job/xxl-job-admin/src/main/resources/application.properties 设置数据库连接，报警邮箱
 
 启动  xxl-job-admin (调度中心)项目 依赖于xxl-job-core
 http://localhost:8080/xxl-job-admin   默认登录账号 “admin/123456”, 前端使用bootstrap开发的
 
 
 
 
 
 
“执行器”项目：xxl-job-executor-sample-springboot
			  xxl-job-executor-sample-frameless (是没有springboot框架的)
 
 作用：负责接收“调度中心”的调度并执行；可直接部署执行器，也可以将执行器集成到现有业务项目中。
   <dependency>
		<groupId>com.xuxueli</groupId>
		<artifactId>xxl-job-core</artifactId>
		<version>2.3.0</version>
	</dependency>
	
/xxl-job/xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties

### 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin

#同一个执行器集群内appname需要保持一致
xxl.job.executor.appname=xxl-job-executor-sample  
#本机的外网地址
xxl.job.executor.ip=


启动


项目中使用下面代码，界面中建立任务，Bean模式，输入 demoJobHandler

/**
 * 1、简单任务示例（Bean模式）
 */
@XxlJob("demoJobHandler")
public void demoJobHandler() throws Exception {
	XxlJobHelper.log("XXL-JOB, Hello World.");

	for (int i = 0; i < 5; i++) {
		XxlJobHelper.log("beat at:" + i);
		TimeUnit.SECONDS.sleep(2);
	}
	// default success
}










---------------------------------Netty 4
Netty 4 是以io.netty开头的包
Netty 3 是以org.jboss.netty开头的包

Ratpack 基于 Netty (event-driven) 高性能的 零拷贝

JBoss的 NIO 框架 要求JDK6及以上版本， 很少有类基于 java nio
NioServerSocketChannel , NioSocketChannel 用了 java nio
//---- Netty-4.1
netty-all-4.1.8.Final-sources.jar 里有example
 <dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.43.Final</version>
</dependency>

jdk8是可以的，但jdk11启动示例报错

new NioEventLoopGroup(1);//线程数，每个NioEventLoopGroup有一个selector

ChannelPipeline 是 Handler （使用ChannelHandlerContext来串起来）的集合

 .option(ChannelOption.SO_BACKLOG, 100) //连接时 当服务端的线程池用完，用来设置队列的大小
 .childOption(ChannelOption.SO_KEEPALIVE,true)
 

 

Bootstrap 用于客户端 group函数要1个参数

ServerBootstrap 用于服务端 group函数要两个参数

ByteBuf ,可用 Unpooled.copiedBuffer("测试".getBytes(CharsetUtil.UTF_8))


ChannelHandlerContext .writeAndFlush()






 
public final class MyClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();//用于客户端
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LineBasedFrameDecoder (1024));//一行最多1024个
                     //LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) 基于数据包长度的拆包器 ,应用层协议中包含数据包的长度
                      //new LengthFieldBasedFrameDecoder(1024,0,4,0,4)//表示接受的数据中没有长度信息
                      
                     //FixedLengthFrameDecoder 每个应用层数据包的都拆分成都是固定长度的大小，比如 1024字节。
                     p.addLast(new MyClientHandler()); 
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

           // f.channel().writeAndFlush("client 写消息");
           // f.channel().writeAndFlush("\r\n"); 
            
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}

public class MyClientHandler extends ChannelInboundHandlerAdapter {
	//In是读，对应的有ChannelOutboundHandlerAdapter 
	//一般要重写这几个方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
       String line="\r\n";
       for(int i=0;i<50;i++)//连接发可能粘包,如用LineBasedFrameDecoder
        {
            ctx.writeAndFlush(Unpooled.copiedBuffer(("客户测试消息"+i+line).getBytes(CharsetUtil.UTF_8)));
        }
    } 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
       // ctx.write(msg);
//    	ctx.channel().writeAndFlush("aa"); 
    	ByteBuf buf=(ByteBuf)msg;
    	System.out.println("客户收到："+buf.toString(CharsetUtil.UTF_8));
    } 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       ctx.flush();
    } 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}


public final class MyServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//用于服务端 group函数要两个参数
            b.group(bossGroup, workerGroup)//两个线程组
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100) //连接时 当服务端的线程池用完，用来设置队列的大小
             .childOption(ChannelOption.SO_KEEPALIVE,true)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LineBasedFrameDecoder (1024));//一行最多1024个
                     p.addLast(new MyServerHandler());
                 }
             });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();
            
            System.out.println("server started");
            
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

public class MyServerHandler extends ChannelInboundHandlerAdapter {//In是读， 对应的有Out
 	//一般要重写这几个方法
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush(firstMessage);//客户端用
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	ByteBuf buf=(ByteBuf)msg;
    	System.out.println("服务收到："+buf.toString(CharsetUtil.UTF_8)); 
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
    	ctx.writeAndFlush(Unpooled.copiedBuffer("服务测试消息".getBytes(CharsetUtil.UTF_8)));
      }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}






---------------------------------zxing  
http://zxing.github.io/zxing/apidocs/allclasses-noframe.html

下载zxing-master解压后
mvn install 后生成 zxing-master\core\target\core-3.0.0-SNAPSHOT.jar
				  zxing-master\javase\target\javase-3.0.0-SNAPSHOT.jar
都有javadoc和source

//--生成 二维码
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

 public static void writeQR()
 {
	final int BLACK = 0xFF000000;
	final int WHITE = 0xFFFFFFFF;
	String content = "http://www.baidu.com";
	String fileName = "C:/test_QR.jpg";

	MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
	Map<EncodeHintType,String> hints = new HashMap<EncodeHintType,String>();
	hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	BitMatrix matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
	File file = new File(fileName);

	int width = matrix.getWidth();
	int height = matrix.getHeight();
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	for (int x = 0; x < width; x++) 
	{
		for (int y = 0; y < height; y++) 
		{
		 image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
		}
	}
	ImageIO.write(image, "jpg", file) ;
}


//--Java 解析二维码
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

MultiFormatReader formatReader = new MultiFormatReader();
String filePath = "C:/test_QR.jpg";
File file = new File(filePath);
BufferedImage image = ImageIO.read(file);
LuminanceSource source = new BufferedImageLuminanceSource(image);
Binarizer binarizer = new HybridBinarizer(source); //Hybrid 混合
BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
Map hints = new HashMap();
hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
Result result = formatReader.decode(binaryBitmap, hints);

System.out.println("resultFormat = " + result.getBarcodeFormat());
System.out.println("resultText = " + result.getText());

//--生成条形码
import java.nio.file.Paths;

 String imgPath = "c:/line.png";  
 String contents = "6923450657713";  // 益达无糖口香糖的条形码  
 
 int width = 105, height = 50;  
 int codeWidth = 3 + // start guard  
		 (7 * 6) + // left bars  
		 5 + // middle guard  
		 (7 * 6) + // right bars  
		 3; // end guard  
 codeWidth = Math.max(codeWidth, width);  
 BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,BarcodeFormat.EAN_13, codeWidth, height, null);
 
MatrixToImageWriter.writeToPath(bitMatrix, "png",Paths.get(new URI(imgPath)));
 
//---Java 解析条形码
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
String imgPath = "c:/line.png";  

BufferedImage  image = ImageIO.read(new File(imgPath));
LuminanceSource source = new BufferedImageLuminanceSource(image);
BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

Result result = new MultiFormatReader().decode(bitmap, null);
System.out.println(result.getText());  
==================图片识别
------------验证码识别
OCR  (Optical Character Recognition , 光学字符识别)  图片转文字
----asprise-ocr-java-5.01 ,windwos trail版本 (只可英文和数字)

	demoText.bat 示例内容  java -jar aocr.jar img/test.png text  测试过可以解析文字(路径不要有中文)

javadoc中的示例
	 //如果旋转一下图片中的字母就不行了???,像360是如何识别 12306的验证码???
	 //测试 OK ,windows asprise-ocr-java-5.01/aocr.jar大小62.5M
	import com.asprise.ocr.Ocr;
	 Ocr.setUp(); // one time setup
	 Ocr ocr = new Ocr();
	 ocr.startEngine("eng", Ocr.SPEED_FASTEST);
	 String s = ocr.recognize(new File[] {new File("test.jpg")}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT, 0, null);
	 System.out.println("RESULT: " + s);
	 // do more recognition here ...
	 ocr.stopEngine();

 
----tesseract ocr (支持中文)
https://github.com/tesseract-ocr/tesseract 
Tess4J


------------指纹识别  人脸识别
SURF (Speeded Up Robust Features, 加速稳健特征) 实现

openCV , Android人脸识别 (Face++)
openSURF  基于 OpenCV

------------hutool
https://gitee.com/dromara/hutool
国产的    Java工具类库

hutool-extra 有二维码识别  基于Zxing的二维码工具类
	byte[] bytes=cn.hutool.extra.qrcode.QrCodeUtil.generatePng(content, width, height);

hutool-captcha 	图片验证码实现
hutool-bloomFilter 	布隆过滤

---------------------------------Ehcache 2.9

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


URL url = MainEhCache.class.getResource("/ehcache/ehcache.xml");
CacheManager manager = CacheManager.newInstance(url);
//--------
Cache cache = manager.getCache("onlyMemoeryCache");
Element element = new Element("key1", "value1");
cache.put(element);//新建和更新


Element valElement = cache.get("key1");
Object value = valElement.getObjectValue();

int elementsInMemory = cache.getSize();//已经使用的内存大小
cache.remove("key1");

//-----
manager.shutdown();
CacheManager.getInstance().shutdown();

----ehcache.xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd" updateCheck="false"
         monitoring="autodetect" dynamicConfig="true">
		 
     <cache name="onlyMemoeryCache" maxElementsInMemory="100000"
           timeToLiveSeconds="1200" timeToIdleSeconds="1200" eternal="false"
           overflowToDisk="false" memoryStoreEvictionPolicy="LFU">
    </cache>
</ehcache>

清内存机制  Least(最少)  Recently (最近)
Least Recently   Used (LRU)  默认			淘汰最长时间未被使用的
Least Frequently Used (LFU)					淘汰一定时期内被访问次数最少的
First In First Out (FIFO)

-----Spring
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	<property name="configLocation">
		<value>classpath:cache/ehcache.xml</value>
	</property>
</bean>

<bean id="ehCache" class="org.springframework.cache.ehcache.EhCacheFactoryBean">
	<property name="cacheManager">
		<ref local="cacheManager"/>
	</property>
	<property name="cacheName">
		<value>onlyMemoeryCache</value>
	</property>
</bean>

import net.sf.ehcache.Ehcache;

ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("ehcache/applicationContext-cache.xml");
Ehcache  ehcache=ctx.getBean("ehCache",Ehcache.class);
Element element = new Element("key1", "value1");
ehcache.put(element);
Element valElement = ehcache.get("key1");
Object value = valElement.getObjectValue();

----------------ehcache 3.x (org.ehcache支持分布式)
 
<dependency>
  <groupId>org.ehcache</groupId>
  <artifactId>ehcache</artifactId>
  <version>3.6.1</version>
</dependency>

 //JCache (JSR-107)

 <dependency>
  <groupId>javax.cache</groupId>
  <artifactId>cache-api</artifactId>
  <version>1.1.0</version>
</dependency>
static void echcache3x()
{
	//classpath 中不能有ehcache 2.9
	 org.ehcache.CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
					  .withCache("preConfigured",
						   CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
														  ResourcePoolsBuilder.heap(100))
						   .build())
					  .build(true);

	 org.ehcache.Cache<Long, String> preConfigured
					  = cacheManager.getCache("preConfigured", Long.class, String.class);

	 org.ehcache.Cache<Long, String> myCache = cacheManager.createCache("myCache",
					  CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
													ResourcePoolsBuilder.heap(100)).build());
				  myCache.put(1L, "da one!");
				  String value = myCache.get(1L);

				  cacheManager.close();
}

 static void jcache()
{
	 //JCache (JSR-107) classpath中不有多个实现，去 redisson.jar
	CachingProvider provider = Caching.getCachingProvider();  
	
	//---CacheManager 方式一
	DefaultConfiguration defConfiguration = new DefaultConfiguration(provider.getDefaultClassLoader(),
			  new DefaultPersistenceConfiguration(new File("/tmp/ehcache_dist/"))); 
	javax.cache.CacheManager cacheManager =provider.getCacheManager(provider.getDefaultURI(),provider.getDefaultClassLoader());  
	 
	//---CacheManager 方式 二
	//javax.cache.CacheManager cacheManager = provider.getCacheManager();   
	
	
	MutableConfiguration<Long, String> configuration =
		new MutableConfiguration<Long, String>()  
			.setTypes(Long.class, String.class)   
			.setStoreByValue(false)   
			.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(javax.cache.expiry.Duration.ONE_MINUTE));  
	javax.cache.Cache<Long, String> cache = cacheManager.createCache("jCache", configuration); 
	cache.put(1L, "one"); 
	String value = cache.get(1L); 
}
-------------------------------caffeine  cache

 
compile 'com.github.ben-manes.caffeine:caffeine:2.6.2'
<dependency>
	<groupId>com.github.ben-manes.caffeine</groupId>
	<artifactId>caffeine</artifactId>
	<version>2.6.2</version>  
</dependency>

 扩展中也提供 JCache 实现
compile 'com.github.ben-manes.caffeine:jcache:2.6.2'
<dependency>
	<groupId>com.github.ben-manes.caffeine</groupId>
	<artifactId>jcache</artifactId>
	<version>2.6.2</version>  
</dependency>

LoadingCache<String, Object> graphs = Caffeine.newBuilder()
	.maximumSize(10_000)
	.expireAfterWrite(5, TimeUnit.MINUTES)
	.refreshAfterWrite(1, TimeUnit.MINUTES)
	.build(new  CacheLoader<String, Object>() {
		@Override
		public Object load(String arg0) throws Exception {
			
			return null;
		}
	});
	//.build(key -> createExpensiveGraph(key));
	
---------------------------------Guava 缓存部分
Caffeine is a Java 8 rewrite of Guava’s cache

<dependency>
	<groupId>com.google.guava</groupId>
	<artifactId>guava</artifactId>
	<version>27.0-jre</version>   <!--  27.0-jre , 27.0-android -->
</dependency>

LoadingCache<String,Object> dictCache = CacheBuilder.newBuilder()
	.maximumSize( 1000) 
	.expireAfterWrite(1, TimeUnit.DAYS)
	.concurrencyLevel( 5 )
	.build(new CacheLoader<String,Object>() {
		@Override
		public String load(String key) throws Exception {
			return null;
		}
	});
dictCache.put("key", "value");
Object o= dictCache.getIfPresent("key");
System.out.println(o);

dictCache.invalidate("key");

o= dictCache.getIfPresent("key");
System.out.println(o);
	

---------------------------------Guava  限流 RateLimiter


------------protobuf
  google跨语言的序列化协议,编译器使用C++开发
https://blog.csdn.net/u011518120/article/details/54604615
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.11.4</version> 
</dependency>
 
--persion.proto 文件  (vscode有非官方插件,eclipse marketplace中国人开发的插件,idea有GenProtobuf,Protobuf Support)
syntax = "proto3";
option java_outer_classname = "PersonEntity";//生成的数据访问类的类名
package protobuf;   
message Person {  
    int32 id = 1; 
    string name = 2; 
    string email = 3; 
}  
protoc -I=. --java_out=. persion.proto   
protoc.exe -I=proto的输入目录 --java_out=java类输出目录 proto的输入目录包含proto文件
	 -IPATH, --proto_path=PATH

PersonEntity.Person.Builder builder = PersonEntity.Person.newBuilder();
builder.setId(1);
builder.setName("ant");
builder.setEmail("ghb@soecode.com");
PersonEntity.Person person = builder.build();
System.out.println("before :"+ person.toString());

//模拟接收Byte[]，反序列化成Person类
byte[] byteArray =person.toByteArray();
PersonEntity.Person p2 = PersonEntity.Person.parseFrom(byteArray);
System.out.println("after :" +p2.toString());

-------------JSON  已经有 javax.json
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
	<classifier>jdk15</classifier>
    <version>2.4</version>
</dependency>

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

JSONArray array = new JSONArray();
		
JSONObject obj = new JSONObject();
obj.put("id", 100);
obj.put("username",URLEncoder.encode("李","UTF-8"));
obj.put("password", 123);
array.add(obj);

JSONObject obj1 = new JSONObject();
obj1.put("id", 101);
obj1.put("username",true);
obj1.put("password", 123);
array.add(obj1);
System.out.println(array.toString());


JSONObject jsonObject = JSONObject.fromObject(userObject);
System.out.println("java Object to json : "+ jsonObject); 

JSONArray jsonArrasy = JSONArray.fromObject(userObject);
System.out.println("java Array to json : "+ jsonArrasy); 


//String->Object
//如对象里有一个List<Order>不能正确转换成集合中的对象,Order要有默认构造器  
Map<String, Class<Order>> classMap = new HashMap<>();
classMap.put("orders", Order.class);
UserModel userModel = (UserModel) JSONObject.toBean(JSONObject.fromObject(strJsonObj), UserModel.class,classMap);
//方式二
JsonConfig jsonConfig = new JsonConfig();  
jsonConfig.setRootClass(UserModel.class);  
jsonConfig.setClassMap(classMap);
userModel = (UserModel) JSONObject.toBean(JSONObject.fromObject(strJsonObj),jsonConfig);
		
System.out.println("userModel: "+ userModel); 

//String->Map
Map userMap = (Map) JSONObject.toBean(JSONObject.fromObject(strJsonMap), Map.class);
System.out.println("userMap: "+ userMap); 
------------json-path


<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.4.0</version>
</dependency> 
依赖于 json-smart-2.3.jar
<dependency>
    <groupId>net.minidev</groupId>
    <artifactId>asm</artifactId>
    <version>1.0.2</version>
</dependency>

文档 https://goessner.net/articles/JsonPath/

import com.jayway.jsonpath.JsonPath;

String json="...";
 
//输出book[0]的author值
String author = JsonPath.read(json, "$.store.book[0].author"); 

//输出全部author的值，使用Iterator迭代
List<String> authors = JsonPath.read(json, "$.store.book[*].author");

//输出book[*]中category == 'reference'的book
List<Object> books = JsonPath.read(json, "$.store.book[?(@.category == 'reference')]");        

//输出book[*]中price>10的book
books = JsonPath.read(json, "$.store.book[?(@.price>10)]");

//输出book[*]中含有isbn元素的book
books = JsonPath.read(json, "$.store.book[?(@.isbn)]");

//输出该json中所有price的值
List<Double> prices = JsonPath.read(json, "$..price");
	
//可以提前编辑一个路径，并多次使用它
JsonPath path = JsonPath.compile("$.store.book[*]"); 
books = path.read(json);


============FasterXml JSON
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.9.6</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.6</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.9.6</version>
</dependency>

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

public class UserJson {
	
	@Id
	@JsonSerialize(using=MyJSonSerializer.class) 
	private ObjectId id; //mongo的
	
	@JsonProperty("userName")
    private String userName;
	
	@JsonProperty("joinDate")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")// timezone="GMT+8"
    private Date joinDate;
	
    @JsonIgnore
	//@JsonIgnore(value=true && false)//可以根据条件来做是否序列化JSON,对于同一实体有时Add/Detail显示多的字段，有时显示少getList,Edit的字段
    private String password;
    
    @JsonProperty("favorite")
	@JsonInclude(JsonInclude.Include.NON_NULL) 	 //字段范围,如果该属性为NULL,生成joson没有该字段 
    private List<String> favorite;

    @JsonProperty("order")
    private OrderJson order;
	
	//可BigDecimal类型
	
	//getter/setter
}
public class MyJSonSerializer extends JsonSerializer<ObjectId>{
	@Override
	public void serialize(ObjectId objId, JsonGenerator gen, SerializerProvider provider) throws IOException {
		 if(objId==null)
			 gen.writeNull();
		 else
			 gen.writeString(objId.toString());
	} 
}
//--- 对象 到 JSON字串
ObjectMapper mapper = new ObjectMapper();
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//全局范围，如果该属性为NULL,生成joson没有该字段 
String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
//String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(JsonNode);//通用对象

System.out.println(jsonString);

//--- JSON字串 到 对象
ObjectMapper mapper = new  ObjectMapper();
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//反序列化遇到未知属性不报异常
mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//允许使用未带引号的字段名
mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); //允许使用单引号
String str= json2String();
UserJson user=mapper.readValue(str, UserJson.class);
//JsonNode node=mapper.readTree( str);   //通用对象
============alibaba JSON  
https://github.com/alibaba/fastjson

有过远程代码执行安全漏洞

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>fastjson</artifactId>
	<version>1.2.75</version>
</dependency>

import com.alibaba.fastjson.JSON;

public class MyIgnoreObject {
	@JSONField(name="_userName")
    private String userName;
    
	@JSONField(name="_joinDate",format="yyyy-MM-dd HH:mm:ss") 
    private Date joinDate;

    @JSONField(serialize=false,deserialize=false)
    private String password;
}
String jsonString=JSON.toJSONString(user);  //Object ->JSON
UserJson user=JSON.parseObject(str,UserJson.class);//JSON ->Object

List<OrderJson> res=JSON.parseArray(jsonString, OrderJson.class);//JSON Array -> List<Object>
		
JSONObject  jsonObject = JSONObject.parseObject(jsonStr); //JSON->Map
Map<String,Object> map = (Map<String,Object>)jsonObject;

JSONObject json = new JSONObject(map); //Map->JSON




//不知道json字串对应的java类型，做格式化
JSONObject object = JSONObject.parseObject(jsonString);
String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, 
SerializerFeature.WriteDateUseDateFormat);

============google JSON
 <dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.8.9</version>
</dependency>

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public static String formatJSONByGoogle(String json) {
	JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	return gson.toJson(jsonObject);
}



public static <T> T json2Object(String json,Class<T> clazz){
	Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.serializeNulls().create();
	T obj=gson.fromJson(json, clazz);
	return obj;
}
 
public static String  object2Json(Object obj ){
	Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.serializeNulls().create();
	return gson.toJson(obj);
}
	

@SerializedName("join_date")
private Date joinDate;
	
	
@Expose(serialize = false, deserialize = false) //没用？？？
private String password;
    

============SnakeYaml
在线验证yaml的好工具
https://onlineyamltools.com/prettify-yaml


 最新 yaml-1.2 是2009年制定的  
 SnakeYaml 只支持 yaml-1.1 , SpringBoot使用这个
 
  <dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>1.25</version>
</dependency>

public static void readYaml( ) throws Exception {
	Yaml yaml = new Yaml();
	//yaml文件  :后一定有空格
	URL url = SnakeYamlMain.class.getResource("test.yaml");
	if (url != null) { 
		Map map = (Map) yaml.load(new FileInputStream(url.getFile()));
		Map map1 =   yaml.loadAs(new FileInputStream(url.getFile()),Map.class);
				
	}
}


============eo-yaml 
  支持 yaml-1.2  
 
 <dependency>
  <groupId>com.amihaiemil.web</groupId>
  <artifactId>eo-yaml</artifactId>
  <version>2.0.1</version>
</dependency>

-------------javassit 
可以没有接口
ClassPool pool=new ClassPool(true);
pool.insertClassPath(new LoaderClassPath(MainJavasssit.class.getClassLoader()));

CtClass targetClass=pool.makeClass("my.javassist.Hello");//动态代理生成新的class，比cglib慢
targetClass.addInterface(pool.get(IHello.class.getName()));

CtClass returnType=pool.get(void.class.getName());
String mname="sayHello";
CtClass[] parameters=new CtClass[] {pool.get(String.class.getName())};
CtMethod method=new CtMethod(returnType, mname, parameters, targetClass);
String src="{ System.out.println(\"hello \"+$1); }";
method.setBody(src);
targetClass.addMethod(method);
Class<IHello> clazz=targetClass.toClass();
IHello  hello=clazz.newInstance();
hello.sayHello("王");

-------------Jasypt 
可配置文件加密 密码 ENC(xxx)

<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt</artifactId>
    <version>1.9.2</version>
</dependency>

 Spring Framework 3.1 and newer 
<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt-spring31</artifactId>
    <version>1.9.2</version>
</dependency>

jasypt-springsecurity3 for Spring Security 3.x and newer 

<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt-springsecurity3</artifactId>
    <version>1.9.2</version>
</dependency>

//可配置文件加密 密码 ENC(xxx)


static String KEY="theKey";
//PBEWithMD5AndDES
public static String  encode(String thePass) {
	BasicTextEncryptor encryptor=new BasicTextEncryptor();
	encryptor.setPassword(KEY);
	String enc= encryptor.encrypt(thePass);
	return enc;
}
//PBEWithMD5AndDES
public static String  decode(String enc) {
//		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//	    encryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);//PBEWithMD5AndDES

	BasicTextEncryptor encryptor=new BasicTextEncryptor();
	encryptor.setPassword(KEY);
	String dec= encryptor.decrypt(enc);
	return dec;
}

public static void main(String[] args) {
	
	String enc=encode("thePass");
	System.out.println("enc="+enc);//DaarVCtqChPzfrYGDc/IYA==
	String dec=decode(enc);
	System.out.println("dec="+dec); 
}

spring
<!--  PBEWithMD5AndDES , PBEWithMD5AndTripleDES -->
<bean id="configurationEncryptor" class="org.jasypt.encryption.pbe.StandardPBEStringEncryptor">
	<property name="algorithm">
		<value>PBEWithMD5AndDES</value>
	</property>
	<property name="password">
		<value>theKey</value>
	</property>
</bean>
 
 
<bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
	<property name="locations">
		<list>
			<value>classpath:/spring_jasypt/dev.properties</value>
			<value>classpath:/spring_jasypt/devOver.properties</value> <!-- 相同属性后面的会覆盖前面的 -->
		</list>
	</property>
</bean>
<bean id="propertyConfigurer" class="org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer">
	<constructor-arg ref="configurationEncryptor" />
	<property name="properties" ref="configProperties"></property>
	<!-- 
	<property name="locations">
		<list>
			<value>classpath:/spring_jasypt/dev.properties</value> 
		</list>
	</property>
	 -->
</bean>

@Value("${redis.password}")
private String redisPass;//是解密的thePass
@Value("#{configProperties.redis_password}") //自己的key不能有点
private String redisPass2;
	
---dev.properties 加密的
redis.password=ENC(DaarVCtqChPzfrYGDc/IYA==)
redis_password=P

---devOver.properties 
redis_password=overP_


--------- RESTEasy



<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>resteasy-jaxrs</artifactId>
    <version>3.8.1.Final</version>
</dependency>  
<dependency>
	<groupId>org.jboss.spec.javax.ws.rs</groupId>
	<artifactId>jboss-jaxrs-api_2.1_spec</artifactId>
	 <version>1.0.3.Final</version>
</dependency>
        
<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>jaxrs-api</artifactId>
    <version>3.0.12.Final</version>
</dependency>
 
#示例版本
resteasy-jaxrs-3.1.4.Final.jar

#Could not find MessageBodyWriter for response object of type:
resteasy-jettison-provider-3.1.4.Final.jar
resteasy-jaxb-provider-3.1.4.Final.jar
jettison-1.3.3.jar (org/codehaus/jettison/jettison/1.3.3)


#新版本测试也可 
 <dependency>
  <groupId>org.jboss.resteasy</groupId>
  <artifactId>resteasy-core</artifactId>
  <version>4.6.0.Final</version>
</dependency>
<dependency>
  <groupId>org.jboss.resteasy</groupId>
  <artifactId>resteasy-core-spi</artifactId>
  <version>4.6.0.Final</version>
</dependency>
	
resteasy-core-4.1.1.Final.jar 
resteasy-core-spi-4.1.1.Final.jar
microprofile-config-api-1.3.jar
smallrye-config-1.3.6.jar

jboss-jaxrs-api_2.1_spec-1.0.3.Final.jar  
reactive-streams-1.0.2.jar 

#Could not find MessageBodyWriter for response object of type
resteasy-jackson2-provider-4.1.1.Final.jar
resteasy-jaxb-provider-4.1.1.Final.jar
jackson-jaxrs-json-provider-2.9.9.jar
jackson-jaxrs-base-2.9.9.jar
json-patch-1.9.jar
jackson-module-jaxb-annotations-2.9.9.jar



servlet 3.0容器用
<dependency>
	<groupId>org.jboss.resteasy</groupId>
	<artifactId>resteasy-servlet-initializer</artifactId>
	<version>${resteasy.version}</version>
</dependency>

老的servlet容器用 web.xml
<servlet>
    <servlet-name>Resteasy</servlet-name>
    <servlet-class>org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher</servlet-class>
    <init-param>
      <param-name>javax.ws.rs.Application</param-name>
  		<param-value>swagger2_oas3_resteasy.MyApplication</param-value>
    </init-param>
 </servlet> 
  <servlet-mapping>
    <servlet-name>Resteasy</servlet-name>
    <url-pattern>/sample/*</url-pattern>   */
  </servlet-mapping>
  <!--如果servlet-mapping不是/*  就要加配置 */ -->
  <context-param>
    <param-name>resteasy.servlet.mapping.prefix</param-name>
    <param-value>/sample</param-value>
  </context-param>
  

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import swagger2_oas3_resteasy.user_store.PetStoreResource;
import swagger2_oas3_resteasy.user_store.UserResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/sample") //和web.xml对应
//在swagger-ui中请示是 http://localhost:8080/sample/user/user1 
//如何设置webContext???
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Stream.of(PetResource.class, 
        		PetStoreResource.class,UserResource.class,
						OpenApiResource.class//就可以请求${pageContext.request.contextPath}/sample/openapi.json(.yaml) 为swagger-ui使用 
        		      ).collect(Collectors.toSet());
    }
}


---Swagger CodeGen 3.x 
<dependency>
  <groupId>io.swagger.codegen.v3</groupId>
  <artifactId>swagger-codegen-cli</artifactId>
  <version>3.0.30</version>
</dependency>


java -jar swagger-codegen-cli-3.0.21.jar -h 
java -jar swagger-codegen-cli-3.0.21.jar  langs 显示支持的语言

java -jar swagger-codegen-cli-3.0.21.jar  generate -h
java -jar swagger-codegen-cli-3.0.21.jar  config-help -l java

java -jar swagger-codegen-cli-3.0.21.jar   generate -l  java -o out_dir -i xxx.yaml 或 xxx.json  

如语言为java 有生成gradle 和 maven 配置的，vscode可提示用哪个
java -jar swagger-codegen-cli-3.0.4.jar generate   -i http://petstore.swagger.io/v2/swagger.json -l java   -o /var/tmp/java_api_client
是生成客户端代码,使用okhttp 和　google的gson
 
java -jar swagger-codegen-cli-3.0.30.jar  generate -l  spring -o out_dir -i https://petstore.swagger.io/v2/swagger.json 
3.0.30 生成的是 springfox 版本为 3.0.0 使用的是 swagger-annotation-2.1.2.jar 里面的包名为  io.swagger.v3.oas.annotations.Operation;


https://petstore.swagger.io/v2/swagger.json

 如是java语言是java client代码，有buil.gradle,build.sbt,pom.xml  依赖于swagger-annotations是2的版本


<plugin>
	    <groupId>io.swagger.codegen.v3</groupId>
	    <artifactId>swagger-codegen-maven-plugin</artifactId>
	    <version>3.0.21</version>
	    <!-- 
		<executions>
			<execution>
				<goals>
					<goal>generate</goal>
				</goals>
			</execution>
		</executions>
		 -->
		 <configuration>
		 	<inputSpec>c:/tmp/swagger.json</inputSpec> <!-- eclipse可以提示参数名 -->
		 	<language>java</language>
		 	<output>${project.build.directory}/generate-source/swagger</output>
		 </configuration>
	</plugin>
mvn swagger-codegen:help 
mvn swagger-codegen:help -Ddetail=true 可以看所有参数及说明
mvn swagger-codegen:help -Ddetail=true -Dglobal=<global-name>
mvn swagger-codegen:generate 生成代码
 
-------------Swagger  
类似的有 RAML(RESTful API Modeling Language)

新版本使用OpenAPI
OpenAPI最新 3.0 版本 OpenAPI Specification (OAS)  可以用YAML或JSON编写 JAX-RS (CXF) 
有用 OAuth2

---Swagger-Core
CXF 3.3.2  也支持OpenApi 3.0.x
https://github.com/swagger-api/swagger-core
Swagger Core 2.X produces OpenApi 3.0 definition files
2.0.8

使用 Jackson 库

RESTEasy-4.1.1.Final (jBoss项目)实现了 JAX-RS 2.1 规范 
Jersey  (毛线衫) 扩展 JAX-RS  2.0 规范，可以和2.1版本的sse(Server Send Event)功能一起用
javax.ws.rs.sse包(Server Send Event)是JAX-RS 2.1的功能

swagger-jersey2-jaxrs.jar 包 支持 JAX-RS 2.0

<!-- 因jdk11版本删 javax.xml.bind ,javax.ws.rs, javax/activation/DataSource -->  
<dependency>
  <groupId>org.jboss.spec.javax.xml.bind</groupId>
  <artifactId>jboss-jaxb-api_2.3_spec</artifactId>
  <version>2.0.1.Final</version>
</dependency>
 
<dependency>
  <groupId>org.jboss.spec.javax.ws.rs</groupId>
  <artifactId>jboss-jaxrs-api_2.1_spec</artifactId>
  <version>2.0.1.Final</version> 
</dependency>
<dependency>
  <groupId>org.jboss.resteasy</groupId>
  <artifactId>resteasy-jaxb-provider</artifactId>
  <version>4.6.0.Final</version>
</dependency>

<dependency>
  <groupId>javax.activation</groupId>
  <artifactId>activation</artifactId>
  <version>1.1.1</version>
</dependency>
	 

- 
 
 <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-jaxrs2</artifactId>
      <version>2.0.8</version>
    </dependency>
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-jaxrs2-servlet-initializer</artifactId>
      <version>2.0.8</version>
    </dependency>

    <dependency>
    <groupId>io.github.classgraph</groupId>
    <artifactId>classgraph</artifactId>
    <version>4.8.43</version>
</dependency>

------swagger 2.0.8  OAS-3.0 deps    
jboss-logging-3.3.0.Final.jar
commons-lang3-3.5.jar
jackson-dataformat-yaml-2.9.5.jar 

jaxrs-api-3.0.12.Final.jar
swagger-jaxrs2-2.0.8.jar
swagger-jaxrs2-servlet-initializer-2.0.8.jar
swagger-annotations-2.0.8.jar
swagger-integration-2.0.8.jar
swagger-core-2.0.8.jar
swagger-models-2.0.8.jar


RESTEasy 配置web.xml  (配置 javax.ws.rs.Application 实现类)
 

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
  static PetData petData = new PetData();

  @GET
  @Path("/{petId}")
  @Operation(summary = "Find pet by ID",
    tags = {"pets"},
    description = "Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
    responses = {
            @ApiResponse(description = "The pet", content = @Content(
                    schema = @Schema(implementation = Pet.class)
            )),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
  public Response getPetById(
      @Parameter(
              description = "ID of pet that needs to be fetched",
              schema = @Schema(
                      type = "integer",
                      format = "int64",
                      description = "param ID of pet that needs to be fetched",
                      allowableValues = {"1","2","3"}
              ),
              required = true)
      @PathParam("petId") Long petId) throws io.swagger.sample.exception.NotFoundException //@PathParam的值对应于上面的@Path的值
  {
    Pet pet = petData.getPetById(petId);
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new io.swagger.sample.exception.NotFoundException(404, "Pet not found");
    }
  }



}

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Pet")
public class Pet {
  @Schema(required = true, example = "[2, 3]")
  private int[] lorem;
  
  @XmlElement(name = "status")
  @Schema(description = "pet status in the store", allowableValues = "available,pending,sold")
  public String getStatus() {
    return status;
  }
  
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  public List<Tag> getTags() {
    return tags;
  }
  
}

Http头  
Accept : application/json 返回json
Accept : application/xml 返回xml 

http://127.0.0.1:8080/J_ThirdLibWeb/sample/pet/1
 
---- swagger-editor
下载源码 v3.6.31  2019-07-24

npm install 
npm run build  就可以双击index.html查看在线实时编辑，实时显示效果

#如要构建docker镜像
#docker build -t swagger-editor
#docker run -d -p 80:8080 swagger-editor
# 查看 http://localhost

可以打开 OpenAPI的json或者yaml (File -> Import File 或 File -> Import URL)


---docker版本 swagger-editor
docker pull swaggerapi/swagger-editor
docker run -d -p 80:8080 swaggerapi/swagger-editor
 



-------------Swagger-UI 3.x  OpenAPI-3.x  
https://github.com/swagger-api/swagger-ui   (v3.20.6) 下载项目把dist目录里的东西复制到项目里  
http://localhost:8080/M_SwaggerWeb/swagger-ui-v2/swagger-ui-dist/index.html 
最上方地址中输入	http://localhost:8080/M_SwaggerWeb/sample/openapi.json -> explorer 会显示出信息
	增加一个sample.json 文件 
	http://localhost:8080/M_SwaggerWeb/swagger-ui-v2/swagger-ui-dist/sample.json
 
为了记住地址，可修改index.html中的 url: "https://petstore.swagger.io/v2/swagger.json" 为这个地址

swagger-ui-react

<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>swagger-ui</artifactId>
	<version>3.47.1</version>
</dependency>
jar包中的/META-INF/resources/目录下
http://localhost:8080/M_SwaggerWeb/webjars/swagger-ui/3.47.1/index.html

	
源码 版本v3.23.1  2019-07-24 
--- 方式1  测试OK
npm init 
npm install swagger-ui-dist --save
npm install express --save 

-- vi swagger-ui-server.js
const express = require('express')
const pathToSwaggerUi = require('swagger-ui-dist').absolutePath()

const app = express()

app.use(express.static(pathToSwaggerUi))

app.listen(3000)

启动服务 node swagger-ui-server.js 就可仿问  http://127.0.0.1:3000/
但不可在Explore地栏写系统路径,
如输入 http://127.0.0.1:8080/M_SwaggerWeb/my-swagger-ui/swagger.json 报 cross-origin (CORS) 响应头要求加 'Access-Control-Allow-*' 
 


写个通用的Filter
 res.addHeader("Access-Control-Allow-Origin", "*");
 res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
 res.addHeader("Access-Control-Allow-Headers", "Content-Type");
    
--- 测试OK
node_modules/swagger-ui-dist 的内容 (或者源码目录/dist)直接复制到自己的项目目录中，修改index.html中配置json文件
 	const ui = SwaggerUIBundle({
       //url: "https://petstore.swagger.io/v2/swagger.json",
        //localhost 和 127.0.0.1 不同是cross-origin (CORS)
        //url:"http://localhost:8080/J_ThirdLibWeb/swagger-ui-v2/swagger-ui-dist/sample.json",
        // url:"sample.json",
         //url:"http://localhost:8080/J_ThirdLibWeb/sample/openapi.json",
        //url:"../../sample/openapi.json",
        url:"../../sample/openapi.yaml",
     }

跨域 请求json使用fetch api, 返回没有响应头？？？


--- 方式 2   无法试 ???
npm install swagger-ui --save 
为 Webpack

import SwaggerUI from 'swagger-ui'
// or use require, if you prefer
const SwaggerUI = require('swagger-ui')

SwaggerUI({
  dom_id: '#myDomId'
})



---docker版本 swagger-ui
3.18.3

docker pull swaggerapi/swagger-ui
docker run -p 80:8080 swaggerapi/swagger-ui 
会使用nginx 启动，带有 Swagger UI,就可仿问 http://127.0.0.1/ 默认地址栏是 https://petstore.swagger.io/v2/swagger.json 可以下载到/home/dell/Documents目录下

docker run -p 80:8080 -e SWAGGER_JSON=/foo/swagger.json -v /home/dell/Documents:/foo swaggerapi/swagger-ui  
启动后 Explore中地址显示为./swagger.json ,Explore按钮是立即解析json,不是浏览选择文件，只能复制粘贴地址


docker run -p 80:8080 -e BASE_URL=/swagger -e SWAGGER_JSON=/foo/swagger.json -v /home/dell/Documents:/foo swaggerapi/swagger-ui
仿问 http://127.0.0.1/ 报404 ??? ？？？  http://127.0.0.1/swagger 也不行 ??? ？？？ 
This will serve Swagger UI at /swagger instead of /.



 

=======================pinyin4j
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public static String getPingYin(String src)
{

	char[] t1 = null;
	t1 = src.toCharArray();
	String[] t2 = new String[t1.length];
	HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	//t3.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
	t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	String t4 = "";
	int t0 = t1.length;
	try
	{
		for (int i = 0; i < t0; i++)
		{
			// 判断是否为汉字字符
			if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
			{
				t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
				t4 += t2[0]+" ";
			} else
				t4 += java.lang.Character.toString(t1[i])+" ";
		}
		// System.out.println(t4);
		return t4;
	} catch (BadHanyuPinyinOutputFormatCombination e1)
	{
		e1.printStackTrace();
	}
	return t4;
}
String cnStr = "中华人民共和国";
System.out.println(getPingYin(cnStr));


-------------Reactor 
https://projectreactor.io/

<dependency>
	<groupId>io.projectreactor</groupId>
	<artifactId>reactor-core</artifactId>
	<version>3.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.4.2</version>
</dependency>
<dependency>
    <groupId>com.goldmansachs</groupId>
    <artifactId>gs-collections-api</artifactId>
    <version>7.0.3</version>
</dependency>
<dependency>
    <groupId>com.goldmansachs</groupId>
    <artifactId>gs-collections</artifactId>
    <version>7.0.3</version>
</dependency>
jsr166e-1.0.jar

reactor.util.function包中
Tuple2 tuple2= Tuples.of("one","two");

//--------------3版本的新代码,1或2版本老代码见Java_Not_Offen
// https://www.infoq.com/articles/reactor-by-example/
public class Reactor3Example {
  private static List<String> words = Arrays.asList(
        "the",
        "quick",
        "brown",
        "fox",
        "jumped",
        "over",
        "the",
        "lazy",
        "dog"
        );

  @Test
  public void simpleCreation() {
     Flux<String> fewWords = Flux.just("Hello", "World");
     Flux<String> manyWords = Flux.fromIterable(words);

     fewWords.subscribe(System.out::println);
     System.out.println();
     manyWords.subscribe(System.out::println);
  }
  
  
  @Test
  public void findingMissingLetter() {
    Flux<String> manyLetters = Flux
          .fromIterable(words)
          .flatMap(word -> Flux.fromArray(word.split("")))
          .distinct()
          .sort()
          .zipWith(Flux.range(1, Integer.MAX_VALUE),
                (string, count) -> String.format("%2d. %s", count, string));

    manyLetters.subscribe(System.out::println);
  }
  @Test
  public void restoringMissingLetter() {
    Mono<String> missing = Mono.just("s");
    Flux<String> allLetters = Flux
          .fromIterable(words)
          .flatMap(word -> Flux.fromArray(word.split("")))
          .concatWith(missing)
          .distinct()
          .sort()
          .zipWith(Flux.range(1, Integer.MAX_VALUE),
                (string, count) -> String.format("%2d. %s", count, string));

    allLetters.subscribe(System.out::println);
  }
  @Test
  public void shortCircuit() {
    Flux<String> helloPauseWorld = 
                Mono.just("Hello")
                    .concatWith(Mono.just("world") );

    helloPauseWorld.subscribe(System.out::println);
  }
  
  @Test
  public void blocks() {
    Flux<String> helloPauseWorld = 
      Mono.just("Hello")
          .concatWith(Mono.just("world") );

    helloPauseWorld.toStream()
                   .forEach(System.out::println);
  }
 
}

-------------Reactor  上

--------------camel

  <dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-spring-boot-dependencies</artifactId>
            <version>3.4.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>


<dependency>
	<groupId>org.apache.camel.springboot</groupId>
	<artifactId>camel-spring-boot-starter</artifactId>
</dependency> 

自动依赖了 
 <dependency>
	<groupId>org.apache.camel</groupId>
	<artifactId>camel-core</artifactId>
	<version>3.4.0</version>
</dependency>
	   
  
支持DSL(domain-specific languages)，如Java DSL， Spring DSL

https://github.com/apache/camel-examples/tree/master/examples/camel-example-kafka
https://camel.apache.org/manual/latest/walk-through-an-example.html  上github上 下载示例代码

2.24版本到3.0变化 SimpleRegistry类所在包变化，使用bind方法代替put方法，或者 使用DefaultRegistry类，增加camel-bean-3.3.0.jar

public class TestMain {

	private static final long DURATION_MILIS = 10000;
	private static final String SOURCE_FOLDER = "src/test/source-folder";
	private static final String DESTINATION_FOLDER = "src/test/destination-folder";

	public static void main(String[] args) throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
//		ProducerTemplate template = camelContext.createProducerTemplate();
//		template.sendBody("endpoint","body");

		
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				//?noop=true表示不删或者移动源文件
				from("file://" + SOURCE_FOLDER + "?delete=true") //参数参考 https://camel.apache.org/components/latest/file-component.html
				.process(new FileProcessor())
						.to("file://" + DESTINATION_FOLDER);//复制到这个目录中
			}
		});
		camelContext.start();
		Thread.sleep(DURATION_MILIS);
		camelContext.stop();
	}

}
public class FileProcessor implements Processor {
	public void process(Exchange exchange) throws Exception {
		String originalFileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String changedFileName = dateFormat.format(date) + originalFileName;
		exchange.getIn().setHeader(Exchange.FILE_NAME, changedFileName);
	}
}
	
	
camel-api-3.3.0.jar
camel-base-3.3.0.jar
camel-core-3.3.0.jar
camel-core-engine-3.3.0.jar
camel-util-3.3.0.jar
camel-support-3.3.0.jar
camel-core-languages-3.3.0.jar
camel-file-3.3.0.jar 用来处理file://协议
camel-bean-3.3.0.jar 用来处理bean://协议

public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext(); 
		
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				 from("direct:start")//camel-direct-3.3.0.jar
				 .process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						String inBody=exchange.getIn().getBody(String.class);
						inBody =inBody +" add in process";
						exchange.getIn().setBody(inBody);//getOut()过时了
					}
				 }) 
				 .to("seda:end"); //camel-seda-3.3.0.jar  seda: 和redirect: 的区别是 这个是异步的，即开新的线程才执行
				 
			}
		});
		
		context.start();
		
		ProducerTemplate producerTmp=context.createProducerTemplate();
		producerTmp.sendBody("direct:start", "hello everyone");
		
		ConsumerTemplate consumeTmp=context.createConsumerTemplate();
		String res=consumeTmp.receiveBody("seda:end", String.class);
		System.out.println(res);
		 
	}
	
<!-- 依赖camel版本为 2.24      -->
<dependency>
    <groupId>org.apache.activemq</groupId> 
    <artifactId>activemq-camel</artifactId>
    <version>5.15.13</version>
</dependency>
public static void main(String[] args) throws Exception {
	CamelContext context = new DefaultCamelContext(); 
	ConnectionFactory connectionFactory=new ActiveMQConnectionFactory();
	
	JmsComponent com= JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
	context.addComponent("jms",com);
	
	context.addRoutes(new RouteBuilder() {
		@Override
		public void configure() throws Exception {
			 from("file:src/main/java/apache_camel/from?noop=true") 
			 .to("activemq:queue:my_queue"); //ativemq会自动创建Queue
		}
	});
	 context.start();
}
public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext(); 
		ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory();
		//connectionFactory.setTrustAllPackages(true);
		
		JmsComponent com= JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
		context.addComponent("jms",com);
		
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				 from("direct:start")
				 .to("activemq:queue:my_queue"); //ativemq会自动创建Queue
			}
		});
		 context.start();
		
		ProducerTemplate producerTmp=context.createProducerTemplate();
		producerTmp.sendBody("direct:start", new Date());//activemq提示不是可信的类
		//>activemq start -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*
	}


public static void main(String[] args) throws Exception {
	CamelContext context = new DefaultCamelContext();  
	context.addRoutes(new RouteBuilder() {
		@Override
		public void configure() throws Exception {
			 from("direct:start")
			 .to("class:apache_camel.HelloService?method=sayHello");  
		}
	});
	context.start();
	ProducerTemplate producerTmp=context.createProducerTemplate();
	producerTmp.sendBody("direct:start", "hello everyone");
}
public class HelloService{
	public void sayHello(String msg)
	{
		System.out.println(msg);
	}
}
public static void main(String[] args) throws Exception {
	HelloService service=new HelloService();
	
	//2.24版本到3.0变化 SimpleRegistry类所在包变化，  使用DefaultRegistry类，增加camel-bean-3.3.0.jar
	DefaultRegistry registry=new DefaultRegistry(); //或者 SimpleRegistry
	registry.bind("helloService", service);//2.24版本put方法，3.0版本bind方法
	  
	CamelContext context = new DefaultCamelContext(registry);  
	context.addRoutes(new RouteBuilder() {
		@Override
		public void configure() throws Exception {
			 from("direct:start")
			 .to("bean:helloService?method=sayHello");  
		}
	});
	context.start();
	ProducerTemplate producerTmp=context.createProducerTemplate();
	producerTmp.sendBody("direct:start", "hello everyone");
}


/*
 版本应和camel版本相同
	<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-jdbc</artifactId>
    <version>2.24.3</version>
</dependency>
*/
public static void main(String[] args) throws Exception {
	MysqlDataSource ds=new MysqlDataSource();
	ds.setUrl("jdbc:mysql://localhost:3306/mydb");
	ds.setUser("zh");
	ds.setPassword("123");
	
	SimpleRegistry registry=new SimpleRegistry();
	registry.bind("myDataSource", ds); 
	
	CamelContext context = new DefaultCamelContext(registry);  
	context.addRoutes(new RouteBuilder() {
		@Override
		public void configure() throws Exception {
			 from("direct:start")
			 .to("jdbc:myDataSource")
			 .bean(new ResultHandler(),"processResult");  //第二个参数方法名
		}
	});
	context.start();
	ProducerTemplate producerTmp=context.createProducerTemplate();
	producerTmp.sendBody("direct:start", "select table_name,TABLE_ROWS from information_schema.TABLES where table_schema='mydb'");
}
public class ResultHandler {
	public void processResult(List  list)
	{
		for( Object item: list)
			System.out.println(item);
	}
}

REST见SpringBoot
------------jsonwebtoken
https://github.com/jwtk/jjwt
 
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.2</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>

jjwt-api-0.11.2.jar
jjwt-impl-0.11.2.jar
jjwt-jackson-0.11.2.jar


如果用JDK11还支持 RSASSA-PSS (PS256, PS384, PS512)  算法

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

// We need a signing key, so we'll create one just for this example. Usually
// the key would be read from your application configuration instead.
Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);//两次调用产生不同的key,可以使用私钥

String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();

Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();//Joe



-------------Hawtio
用来代理  前端 和 Jolokia端点(activeMQ有用)
 
hawtio-default-2.10.0.war
hawtio-app-2.10.0.jar
 
java -jar hawtio-app-x.y.z.jar
http://localhost:8080/hawtio

界面中点add  Connection按钮，可连接到ActiveMQ ( http://0.0.0.0:8161/api/jolokia/) 密码就是activeMQ的admin/admin
就可以看到 ActiveMQ 的JMX,可以看到Queue,Topic,可以purge某个queue 

Artemis (http://localhost:8161/console/jolokia)密码就是Artemis的   可以看到queue ，但看不到数据（Artemis控制台也是JMX的）

		


-------------Jolokia 
ActiveMQ有使用
是一个 JMX-HTTP 桥

 
-----------Dozer 
复制Bean属性
https://github.com/DozerMapper/dozer/
<dependency>
    <groupId>com.github.dozermapper</groupId>
    <artifactId>dozer-core</artifactId>
    <version>6.5.0</version>
</dependency>
dozer-core-6.5.0.jar


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;


SourceClassName sourceObject = new SourceClassName();
sourceObject.setName("Dozer");
sourceObject.setBirthday(new Date());

BeanMappingBuilder builder=new BeanMappingBuilder() {
	@Override
	protected void configure() {
		//从哪个类，复制为什么类时，排除哪个字段,如JPA lazy字段
		mapping(NestPair.class, NestPair.class, TypeMappingOptions.mapNull(false)).exclude("second");
	}
};
Mapper mapper = DozerBeanMapperBuilder.create().withMappingBuilder(builder).build();
//Mapper mapper = DozerBeanMapperBuilder.buildDefault(); 
DestinationClassName destObject = mapper.map(sourceObject, DestinationClassName.class);

System.out.println(destObject.getBirthday().equals(sourceObject.getBirthday()));

 
 -------MapStruct 
 复制Bean属性
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <org.mapstruct.version>1.4.2.Final</org.mapstruct.version>
  </properties>

  <dependencies>
 	 <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter</artifactId>
	</dependency>
	
      <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency> 
	
   <dependency>
         <groupId>org.mapstruct</groupId>
         <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>  <!-- 如加这个就不用 maven-compiler-plugin 了 -->

  </dependencies>
  <build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>  
                <target>1.8</target> 
                <annotationProcessorPaths>  <!-- 对没有加mapstruct-processor依赖时 -->
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path> 
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
	
	
 //  依赖于  type org.springframework.stereotype.Component (spring boot)
//mvn compile在target下生成源码
Car car = new Car( "Morris", 5, CarType.SEDAN );
CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );
System.out.println(carDto);


public enum CarType {
	SEDAN 
}

@Mapper 
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );  
    @Mapping(source = "numberOfSeats", target = "seatCount")
	@Mapping(target = "createDate", expression = "java(new java.util.Date())")
    CarDto carToCarDto(Car car);  
}


public class CarDto {
    private String make;
    private int seatCount;
    private String type;
	//...
}
public class Car {
    private String make;
    private int numberOfSeats;
    private CarType type;
//...
}

@Mapper
public interface MenuConverterMapStruct{
	MenuConverterMapStruct INSTANCE = Mappers.getMapper( MenuConverterMapStruct.class );
	 
	 public   List<Button> requestConvertRecursive(List<MenuItem> menuItems) ;
	 
	 public   List<MenuItem>   responseConvertRecursive(List<Button> buttons) ;
	 
	 
		 
	public List<NameValuePair> shortResponseConvert(List<WxMaShopAccountGetBrandListItem>  req);
	//  @Mappings({//老版本有这个
		@Mapping(source = "brandId", target = "value")  
		@Mapping(source = "brandWording", target = "name") 
	//})
	public NameValuePair  wxMaShopAccountGetBrandListItemToNameValuePair(WxMaShopAccountGetBrandListItem item);

}
@Mapper
public interface AddressMapper {
	
	AddressMapper INSTANCE = Mappers.getMapper( AddressMapper.class );
	//多参组合
    @Mapping(source = "person.description", target = "description")
    @Mapping(source = "hn", target = "houseNumber") 
    DeliveryAddressDto personAndAddressToDeliveryAddressDto(Person person, Integer hn);
    

    //enum转换
    @ValueMappings({
        @ValueMapping(source = "EXTRA", target = "SPECIAL"),
        @ValueMapping(source = "STANDARD", target = "DEFAULT"),
        @ValueMapping(source = "NORMAL", target = "DEFAULT")
    })
    ExternalOrderType orderTypeToExternalOrderType(OrderType orderType);
    
    //List中是不同类型，是JDK类型
    @IterableMapping(numberFormat = "$#.00")
    List<String> prices(List<Integer> prices); 

    @IterableMapping(dateFormat = "dd.MM.yyyy")
    List<String> stringListToDateList(List<Date> dates);
    

    @MapMapping(valueDateFormat = "dd.MM.yyyy")
    Map<String, String> longDateMapToStringStringMap(Map<Long, Date> source);
    
    //Stream参数
    Set<String> integerStreamToStringSet(Stream<Integer> integers); 
    
    //Stream参数，List中是不同自己类型类型
    //名字以s结尾 ，生成的代码中会调用 carToCarDto()
    List<CarDto> carsToCarDtos(Stream<Car> cars);
    
    @Mapping(source = "numberOfSeats", target = "seatCount")
    CarDto carToCarDto(Car car); 
    
    @InheritInverseConfiguration
    Car CarDtoToCar(CarDto car);   
}


@Mapper(componentModel = "spring"))//Spring来实例化进容器中,后就可以@Autowired 
public interface SpringCarMapper { 
    CarDto carToCarDto(Car car);
}

生成的代码有注解
import javax.annotation.Generated; 
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-13T13:57:05+0800",
    comments = "environment: Java 1.8.0_291"
)

-----------liquibase
https://www.liquibase.org
https://github.com/liquibase/liquibase
 
Pro 版本是收费的
<dependency>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-core</artifactId>
  <version>4.0.0</version>
</dependency>

 
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <version>4.0.0</version>
    <configuration>
        <propertyFileWillOverride>true</propertyFileWillOverride>
        <propertyFile>src/main/resources/liquibase.properties</propertyFile>
    </configuration>
	 <executions>  
		 <execution>  
		   <phase>process</phase>                                                                    
		   <goals>  
		   <goal>update</goal>  
		   </goals>  
		 </execution>  
	   </executions>  
</plugin>

mvn help:describe -DgroupId=org.liquibase -DartifactId=liquibase-maven-plugin   -Dfull=true 显示帮助

mvn liquibase:update
mvn liquibase:tag -Dliquibase.tag=myTag
mvn liquibase:rollback  -Dliquibase.tag=myTag
mvn liquibase:rollback  -Dliquibase.rollbackCount=2
 

--liquibase.properties 必须放在  classpath下
contexts:  /MyApp   
changeLogFile: Release004.sql
driver:  com.mysql.cj.jdbc.Driver
url:  jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8
username:  zh
password:  123
verbose:  true   
dropFirst:  false 


如果需要在父项目中配置子项目共享的LiquiBase配置，而各个子项目可以定义自己的配置，并覆盖父项目中的配置，
则只需要在父项目的pom中将propertyFileWillOverride设置为true即可

https://docs.liquibase.com/tools-integrations/maven/home.html


===Release004.sql 文件内容(MySQL)
--comment: 这是注释 ，每sql个文件必须以 liquibase formatted sql开始
--liquibase formatted sql

--comment: changeset 格式为 --changeset author:id attribute1:value1 attribute2:value2 可以设置endDelimiter(默认为;) https://docs.liquibase.com/concepts/basic/sql-format.html
--changeset gpl:Release0004-1
CREATE TABLE table2 (
  id int(11) NOT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM;
--rollback drop table table2;
--comment: rollback 为回滚

--changeset gpl:Release0004-3
ALTER TABLE  table2 CHANGE  name  firstname VARCHAR( 255 );
--rollback ALTER TABLE  table2 CHANGE  firstname  name VARCHAR( 255 );
 
--changeset gpl:Release0004-4
INSERT INTO table2 (id, firstname) VALUES (NULL, 'name1'),(NULL, 'name2'), (NULL, 'name3');
--rollback DELETE FROM table2 WHERE firstname IN('name1','name2','name3');


内容可以是一个SQL文件多个changeset、一个changeset多个SQL

liquibase --changeLogFile=\tmp\Release004.sql --driver=com.mysql.cj.jdbc.Driver  --url="jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8"  --username=zh --password=123  update
liquibase --changeLogFile=\tmp\Release004.sql --driver=com.mysql.cj.jdbc.Driver  --url="jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8"  --username=zh --password=123  tag v1.0
liquibase --changeLogFile=\tmp\Release004.sql --driver=com.mysql.cj.jdbc.Driver  --url="jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8"  --username=zh --password=123  rollback v1.0
liquibase --changeLogFile=\tmp\Release004.sql --driver=com.mysql.cj.jdbc.Driver  --url="jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8"  --username=zh --password=123  rollbackCount 1

liquibase --changeLogFile=\tmp\Release004.sql  --driver=com.mysql.cj.jdbc.Driver  --url="jdbc:mysql://localhost/mydb?useUnicode=true&characterEncoding=UTF-8" --username=zh --password=123 rollbackToDate 2013-07-16T16:55:37

可加  --classpath=D:\Program\liquibase-4.0.0\lib\mysql-connector-java-8.0.15.jar 或放lib目录下
可加  --logLevel=debug
 
默认记录在  DATABASECHANGELOG 和  DATABASECHANGELOCK  表中
 
 
//spring boot 的 DataSourceBuilder 优先使用hikari
DataSource datasource=DataSourceBuilder.create().url("").username("").password("").build();
batchUpdate(datasource);
public static void batchUpdate(DataSource datasource) throws Exception {
	Connection connection=datasource.getConnection();
	JdbcConnection jdbcConnection=new JdbcConnection(connection);
	Database database=DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
	database.setDatabaseChangeLogTableName("T_CHANGE_LOG");
	database.setDatabaseChangeLogLockTableName("T_LOCK_CHANGE_LOG");

	if(!database.getRanChangeSetList().isEmpty())
	{
		StandardChangeLogHistoryService service=new StandardChangeLogHistoryService();
		List<Map<String, ?>> changeLogTable = service.queryDatabaseChangeLogTable(database);

		List<String> changeLogs=new ArrayList<>();
		changeLogTable.stream().forEach(o->changeLogs.add(o.get("FILENAME").toString()));
		
		List<String> updates=new ArrayList<>();
		//spring的类
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource  resouces[]=resolver.getResources("classpath*:db/sql/*.sql");
		for(Resource resource:resouces) 
		{
			String fileName="db/sql/"+resource.getFilename();
			if(!changeLogs.contains(fileName))
				updates.add(fileName);
		}
		Collections.sort(updates);
		for(String update:updates)
		{
			boolean rollback=true;
			Liquibase liquibase=new Liquibase(update,new ClassLoaderResourceAccessor(),database);
			System.out.println(update+"文件的SQL");
			//为打日志
			for(ChangeSet changeSet:liquibase.getDatabaseChangeLog().getChangeSets())
			{
				rollback=rollback & changeSet.supportsRollback(database);
				changeSet.getChanges().forEach(change->{
					Arrays.stream(change.generateStatements(database)).forEach(
								s -> {
									System.out.println("SQL-"+s.toString());
								}
							);
					
					try {
						Arrays.stream(change.generateRollbackStatements(database)).forEach(
								s ->{
									System.out.println("Rollback SQL-"+s.toString());
								}
							);
					} catch (RollbackImpossibleException e) {
						System.out.println("Rollback SQL Fail");
					}
				});
			}
			if(rollback) {
				liquibase.updateTestingRollback(null);
			}else
			{
				liquibase.update((String)null);
			}
			liquibase.close();
		}
		database.commit();
	}
}
 
--------------CNCF Fluentd Java Client

有Java版本，但没有Go
https://github.com/fluent/fluent-logger-java

 <dependency>
    <groupId>org.fluentd</groupId>
    <artifactId>fluent-logger</artifactId>
    <version>0.3.4</version>
  </dependency>
fluent-logger-0.3.4.jar
	msgpack-0.6.8.jar
	
import org.fluentd.logger.FluentLogger; 

//连接本机，这个应该只调用一次
//	private static FluentLogger LOG = FluentLogger.getLogger("debug"); //这个是tagPrefix 
// 如是连接远程
private static FluentLogger LOG = FluentLogger.getLogger("debug", "127.0.0.1", 24224);

 public static void main (String ...args) {
     // ...
     Map<String, Object> data = new HashMap<String, Object>();
     data.put("from", "userA");
     data.put("to", "userB");
     LOG.log("test", data);//第一个参数是tag ,不太容易定位到类(传this反射可做)的某个方法多少行上，
    //FluentLogger.close();
        /* 对应配置为 
				<match debug.**>
				  @type stdout
				  @id stdout_output
				</match> 
	         */
	         
 }

-------------akka



-------------OAuth 2.0   见Spring Security
Open Authorization

resource owner  最终用户
resource server	 是API服务器 使用access token,返回保护的资源
client			应用
authorization server 保存用户密码的服务器
---client sparklr2
 

----FIX 
 FIX的英文全称为Financial Information eXchange（金融信息交换协议）
https://www.fixtrading.org

---quickfix 
http://quickfixengine.org/
FIX Protocol Implementation
有很多语言的实现 ,java 语言实现 为 QuickFIX/J https://www.quickfixj.org/
 
--Philadelphia 
https://github.com/paritytrading/philadelphia
Financial Information Exchange (FIX) engine for the JVM.

 
--------------------微信开源项目，比较全/新,缓存支持Redisson
https://gitee.com/binary/weixin-java-tools
<dependency>
	<groupId>com.github.binarywang</groupId>
	<artifactId>weixin-java-miniapp</artifactId>
	<version>4.2.0</version>
</dependency>


/*一般使用方法
Config config = new Config();
// MasterSlaveServersConfig  msConfig=config.useMasterSlaveServers();
// msConfig.setMasterAddress(masterIPPort);
// msConfig.addSlaveAddress(slaveIPPort);
SingleServerConfig singConfig= config.useSingleServer();
singConfig.setDatabase(database);
singConfig.setAddress("redis://"+redisHost+":"+redisPort);
singConfig.setPassword(redisAuth); 
RedissonClient redisson = Redisson.create(config);

WxMaRedissonConfigImpl wxConfig=new WxMaRedissonConfigImpl (redisson); 
wxConfig.setAppid(channelWeiXinAccount.getAppId());
wxConfig.setSecret(channelWeiXinAccount.getSecret());
wxMaService=new WxMaServiceImpl();
wxMaService.addConfig(channelWeiXinAccount.getAppId(), wxConfig);

WxMaShopCatService service = new WxMaShopCatServiceImpl(wxMaService);
resp = service.getCat();
*/


 