
==============================Dom4j
---------Dom4j д
Document document = DocumentHelper.createDocument();
Element root = document.addElement("root");
Element status=root.addElement("status");
status.addText("OK");

model.addAttribute("id","1")
	.addAttribute("name", "lisi");
System.out.println( document.asXML());//��<?xml 
System.out.println(root.asXML());//��<?xml  
root.write(response.getWriter());//��һ�е�,Servlet��
//���ϡ���û��<?xmlͷ
 
  
FileWriter fileWriter = new FileWriter("c:/temp/hello.xml");  
//OutputFormat xmlFormat = new OutputFormat();  
OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
xmlFormat.setEncoding("GBK");  
xmlFormat.setIndent(true);//"�����Ǽ����ո�"
XMLWriter xmlWriter = new XMLWriter(fileWriter,xmlFormat);//�Ǹ�ʽ�õ�,response.getWriter()
xmlWriter.write(document);//��<?xml
//xmlWriter.write( root );//��<?xml
xmlWriter.close(); 

---------Dom4j SAX��
<dependency>
	<groupId>jaxen</groupId>
	<artifactId>jaxen</artifactId>
	<version>1.1.6</version>
</dependency>
	
Document document = DocumentHelper.parseText(xmlSoapResponse.toString());
Element soapenvNode = document.getRootElement().element("Body").element("respone1");//dom4j�ᱣ�����ƿռ�,w3c�Ĳ���

FileReader in= new FileReader( "c:/temp/output.xml" );//����ʹ��StringReader
SAXReader reader=new SAXReader();
Document doc=reader.read(in);//��<?xml,��<?xml ���ɽ���
java.util.List list=root.selectNodes("//author[@name='James']");//Ҫorg/jaxen/

Element root=doc.getRootElement();
System.out.println("���ڵ�����"+root.getName());
//root.elementByID("")
List<Element> authors=root.elements();
//�� root.element("��һ��").element("�Ӷ���")
for(Iterator<Element> it= authors.iterator();it.hasNext();)
{
	Element author= it.next();
	Attribute attr=author.attribute("name");
	System.out.println("���ԣ�"+attr.getValue());
	System.out.println("�ı���"+author.getText());
	
}
in.close();
//-------dom4j DOM��
DOMReader domReader=new DOMReader();
org.dom4j.Document doc4j=domReader.read(doc);//org.w3c.dom.Document -> org.dom4j.Document
//doc4j.accept(new MyVisitor());//��ʽһ
Element root=doc4j.getRootElement();//��ʽ��
java.util.List list=root.selectNodes("//author[@name='James']");//Ҫorg/jaxen/
for(int i=0;i<list.size();i++)
{
	System.out.println(((Element)list.get(i)).getData());
}
class MyVisitor extends VisitorSupport
{
	public void visit(Attribute node) {
		System.out.println("���ԣ�"+node.getName()+"="+node.getValue());
	}
	public void visit(Element node) {
		if(node.isTextOnly())
			System.out.println("Ԫ�أ�"+node.getName()+" > "+node.getText());
		else
			System.out.println("Ԫ�أ�"+node.getName());
	}
	public void visit(ProcessingInstruction node) {//ָ��,������XSL
		System.out.println("ͷ��"+node.getTarget());
	}
}
==============================xerces

<dependency>
    <groupId>xerces</groupId>
    <artifactId>xercesImpl</artifactId>
    <version>2.12.0</version>
</dependency>

xercesImpl.jar/META-INF/services/javax.xml.parsers.DocumentBuilderFactory�ļ��м�¼��DocumentBuilderFactoryʵ����

---------xerces ��    //���ܺ��������հ�??????????
org.apache.xerces.parsers.DOMParser  parser = new DOMParser();//xerces 
InputStream input=TestXerces.class.getResourceAsStream("/testxml/rule.xml");
InputSource source=new InputSource(input);
parser.parse(source);//parser.parse("employees.xml");
org.w3c.dom.Document doc1=parser.getDocument();
  
---------xerces д  
//�����ʽ����,��ʱ�ĵ�,�Ƽ���DOM Level 3 LSSerializer ���� JAXP's Transformation 
OutputFormat   outputFormat   =   new   OutputFormat("XML","gb2312",true);  
FileWriter   fileWriter=new   FileWriter(new File("test.xml"));  
XMLSerializer   xmlSerializer=new   XMLSerializer(fileWriter,outputFormat);  
xmlSerializer.asDOMSerializer();  
xmlSerializer.serialize(result.getDocumentElement());  
fileWriter.close();   

org.w3c.dom.Document myDoc=new org.apache.xerces.dom.DocumentImpl();//����

---------xalan
<dependency>
    <groupId>xalan</groupId>
    <artifactId>xalan</artifactId>
    <version>2.7.2</version>
</dependency>

TransformerFactory   tFactory=TransformerFactory.newInstance();
System.getProperty("javax.xml.transform.TransformerFactory");//org.apache.xalan.processor.TransformerFactoryImpl
//xx.jar/META-INF/services/javax.xml.transform.TransformerFactory�ļ�
java org.apache.xalan.xslt.Process -IN student.xml -XSL student.xsl -OUT student.html




============XStream

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public static String objToXml(Object srcObj)
{
	XStream xStream = new XStream(new DomDriver());
	xStream.autodetectAnnotations(true);	// ʹ��Bean�е�ע����Ч
	return  xStream.toXML(srcObj);
}

public static <T>  T xmlToObj(String xml,Class<T> clazz) 
{
	if(xml == null) 
		return null;
	XStream xStream = new XStream(new DomDriver());
	xStream.processAnnotations(clazz);//����ʹ����ע��
	Object obj = xStream.fromXML(xml);
	return (T)obj;
}


@XStreamAlias("Request") //���������(�ϲ�)���Կ��Լ�����ǰ
public class MyRequest {
	@XStreamAlias("Head")
	private MyHead head;  // ��MyHead����ǰ���@XStreamAlias ��Ч
	// @XStreamImplicit//��ʽ���ϣ�ֻ��ʾ�����������
	@XStreamImplicit(itemFieldName = "Body")
	private List<MyBody> body;
}

