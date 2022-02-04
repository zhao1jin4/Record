﻿
==============================Dom4j
---------Dom4j 写
Document document = DocumentHelper.createDocument();
Element root = document.addElement("root");
Element status=root.addElement("status");
status.addText("OK");

model.addAttribute("id","1")
	.addAttribute("name", "lisi");
System.out.println( document.asXML());//有<?xml 
System.out.println(root.asXML());//无<?xml  
root.write(response.getWriter());//是一行的,Servlet中
//以上　　没有<?xml头
 
  
FileWriter fileWriter = new FileWriter("c:/temp/hello.xml");  
//OutputFormat xmlFormat = new OutputFormat();  
OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
xmlFormat.setEncoding("GBK");  
xmlFormat.setIndent(true);//"或者是几个空格"
XMLWriter xmlWriter = new XMLWriter(fileWriter,xmlFormat);//是格式好的,response.getWriter()
xmlWriter.write(document);//有<?xml
//xmlWriter.write( root );//无<?xml
xmlWriter.close(); 

---------Dom4j SAX读
<dependency>
	<groupId>jaxen</groupId>
	<artifactId>jaxen</artifactId>
	<version>1.1.6</version>
</dependency>
	
Document document = DocumentHelper.parseText(xmlSoapResponse.toString());
Element soapenvNode = document.getRootElement().element("Body").element("respone1");//dom4j会保留名称空间,w3c的不会

FileReader in= new FileReader( "c:/temp/output.xml" );//或者使用StringReader
SAXReader reader=new SAXReader();
Document doc=reader.read(in);//无<?xml,有<?xml 都可解析
java.util.List list=root.selectNodes("//author[@name='James']");//要org/jaxen/

Element root=doc.getRootElement();
System.out.println("根节点名："+root.getName());
//root.elementByID("")
List<Element> authors=root.elements();
//可 root.element("子一级").element("子二级")
for(Iterator<Element> it= authors.iterator();it.hasNext();)
{
	Element author= it.next();
	Attribute attr=author.attribute("name");
	System.out.println("属性："+attr.getValue());
	System.out.println("文本："+author.getText());
	
}
in.close();
//-------dom4j DOM读
DOMReader domReader=new DOMReader();
org.dom4j.Document doc4j=domReader.read(doc);//org.w3c.dom.Document -> org.dom4j.Document
//doc4j.accept(new MyVisitor());//方式一
Element root=doc4j.getRootElement();//方式二
java.util.List list=root.selectNodes("//author[@name='James']");//要org/jaxen/
for(int i=0;i<list.size();i++)
{
	System.out.println(((Element)list.get(i)).getData());
}
class MyVisitor extends VisitorSupport
{
	public void visit(Attribute node) {
		System.out.println("属性："+node.getName()+"="+node.getValue());
	}
	public void visit(Element node) {
		if(node.isTextOnly())
			System.out.println("元素："+node.getName()+" > "+node.getText());
		else
			System.out.println("元素："+node.getName());
	}
	public void visit(ProcessingInstruction node) {//指令,如引用XSL
		System.out.println("头："+node.getTarget());
	}
}
==============================xerces

<dependency>
    <groupId>xerces</groupId>
    <artifactId>xercesImpl</artifactId>
    <version>2.12.0</version>
</dependency>

xercesImpl.jar/META-INF/services/javax.xml.parsers.DocumentBuilderFactory文件中记录着DocumentBuilderFactory实现类

---------xerces 读    //不能忽略缩进空白??????????
org.apache.xerces.parsers.DOMParser  parser = new DOMParser();//xerces 
InputStream input=TestXerces.class.getResourceAsStream("/testxml/rule.xml");
InputSource source=new InputSource(input);
parser.parse(source);//parser.parse("employees.xml");
org.w3c.dom.Document doc1=parser.getDocument();
  
---------xerces 写  
//输出格式良好,过时的的,推荐用DOM Level 3 LSSerializer 或者 JAXP's Transformation 
OutputFormat   outputFormat   =   new   OutputFormat("XML","gb2312",true);  
FileWriter   fileWriter=new   FileWriter(new File("test.xml"));  
XMLSerializer   xmlSerializer=new   XMLSerializer(fileWriter,outputFormat);  
xmlSerializer.asDOMSerializer();  
xmlSerializer.serialize(result.getDocumentElement());  
fileWriter.close();   

org.w3c.dom.Document myDoc=new org.apache.xerces.dom.DocumentImpl();//建立

---------xalan
<dependency>
    <groupId>xalan</groupId>
    <artifactId>xalan</artifactId>
    <version>2.7.2</version>
</dependency>

TransformerFactory   tFactory=TransformerFactory.newInstance();
System.getProperty("javax.xml.transform.TransformerFactory");//org.apache.xalan.processor.TransformerFactoryImpl
//xx.jar/META-INF/services/javax.xml.transform.TransformerFactory文件
java org.apache.xalan.xslt.Process -IN student.xml -XSL student.xsl -OUT student.html




============XStream

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public static String objToXml(Object srcObj)
{
	XStream xStream = new XStream(new DomDriver());
	//xstream.alias("xmlRoot", srcObj.getClass());//xml根标签名字，类可以没有任何注解
	xStream.autodetectAnnotations(true);	// 使在Bean中的注解生效
	return  xStream.toXML(srcObj);
}

public static String objToXmlByXStream(Object srcObj,String root) {
	if(srcObj==null)
		return null;
	//XStream xStream = new XStream();
	//对于Java转换Xml生成字串String要以"<![CDATA[  ]]> 包含
	NameCoder nameCoder = new NoNameCoder();
	XStream xStream = new XStream(new XppDomDriver(nameCoder) {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out, nameCoder) {
				boolean isUseCDATA = false; 
				@Override
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
					if (clazz == String.class) 
						isUseCDATA =true;
					else
						isUseCDATA =false;
				}
				@Override
				public void writeText(QuickWriter writer, String text) {
					if (isUseCDATA) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	xStream.alias(root, srcObj.getClass()); //xml根标签名字，类可以没有任何注解
	xStream.autodetectAnnotations(true);	// 使在Bean中的注解生效
	return xStream.toXML(srcObj);  
}
public static <T>  T objToXmlByXStream(String xml,Class<T> clazz) 
{
	if(xml == null) 
		return null;
	//XStream xStream = new XStream(new DomDriver());
	
  XStream xStream = new XStream(new DomDriver()){
            //避免xml出现多余字段报错
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next){
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                       if(definedIn==Object.class){
                            try{
                                return this.realClass(fieldName) !=null;
                            }catch (Exception e){
                                return false;
                            }
                       }else{
                           return super.shouldSerializeMember(definedIn,fieldName);
                       }
                    }
                };
            }
        };

	xStream.processAnnotations(clazz);//声明使用了注解
	xStream.allowTypes(new Class[]{clazz}); //新版必须加
	Object obj = xStream.fromXML(xml);
	return (T)obj;
}

@XStreamAlias("Request") //因是最根级(上层)所以可以加在类前
public class MyRequest {
	@XStreamAlias("Head")
	private MyHead head;  // 在MyHead的类前面加@XStreamAlias 无效
	//@XStreamImplicit//隐式集合，只显示集合里的内容
	//这是出现多个重复的<Body>元素,没有外层的<body>(itemFieldName = "Body"是替代MyBody类的注解)
	@XStreamImplicit(itemFieldName = "Body")
	private List<MyBody> body;
	

}
 
 
TextMessage txt=new TextMessage();
txt.setContent("内容<a>");//会把<转换为&lt;
txt.setCreateTime(System.currentTimeMillis());
txt.setFromUserName("张三");
String xml2=XmlUtil.objToXml(txt,"xml");
System.out.println(xml2);
 
List<MyBody> listBody=new ArrayList<MyBody>();
//List<MyBody> listBody= Arrays.asList(body,body);//不能使用Arrays生产List(是内部类),生成XML问题
//List<MyBody> listBody= Collections.singletonList(body);//不能使用Collections生产List(是内部类),生成XML问题
//可用 mapstruct 的 Collections.newArrayList(...)


