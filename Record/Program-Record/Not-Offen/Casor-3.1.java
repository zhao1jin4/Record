castor-1.3.jar
castor-1.3-core.jar
依赖于commons-logging





CD sessions = new CD("Sessions for Robert J", "Eric Clapton");
FileWriter writer = new FileWriter("cds.xml");
Marshaller.marshal(sessions, writer); //Class->XML

-----

FileReader reader = new FileReader("cds.xml");
CD cd = (CD) Unmarshaller.unmarshal(CD.class, reader);  // XML->Class


实体类里可以有 List<MyObject> 属性的形式

MyObject 等类要有默认构造方法

类的属性是int 的转成的是XML的属性不是标签,而且名都成一有 "-" 分隔的名字 











Castor 当前只支持两个值：vector（代表列表类型）和 array（代表数组类型）。
通过标准的 Java 集合 API（比如 next() 等调用）访问第一种集合；管理第二种集合的方法与 Java 数组相似





import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
Mapping mapping = new Mapping();
mapping.loadMapping("book-mapping.xml");
FileReader reader = new FileReader("book.xml");
Unmarshaller unmarshaller = new Unmarshaller(Book.class);
unmarshaller.setMapping(mapping);
Book book = (Book) unmarshaller.unmarshal(reader);


-----book-mapping.xml

<?xml version="1.0"?>
<!DOCTYPE mapping PUBLIC "-//EXOLAB/Castor Mapping DTD Version 1.0//EN" "http://castor.org/mapping.dtd">
<mapping>
	<class name="ibm.xml.castor.Book">
	  <map-to xml="book" />		//对应book标签
		<field name="Title" type="java.lang.String">	//对应Java的setTitile方法
			<bind-xml name="title" node="element" location="book-info" />	 //即<book-info>下的<title>标签
		</field>
		<field name="Isbn" type="java.lang.String">
			<bind-xml name="isbn" node="element" location="book-info" />
		</field>
		<field name="Authors" type="ibm.xml.castor.Author" collection="vector">	 //还有一种array
			<bind-xml name="author" />
		</field>
	</class>
	 <class name="ibm.xml.castor.Author">
		<field name="FirstName" type="java.lang.String">
			<bind-xml name="first" node="attribute" location="name" />	//对应的是XML属性
		</field>
		<field name="LastName" type="java.lang.String">
			<bind-xml name="last" node="attribute" location="name" />
		</field>
	</class>
</mapping>





来读-----book.xml 
<?xml version="1.0" encoding="UTF-8"?>
<book>
<author>
	<name first="Douglas" last="Preston" />
</author>
<author>
	<name first="Lincoln" last="Child" />
</author>
<book-info>
　<isbn>9780446618502</isbn>
　<title>The Book of the Dead</title>
</book-info>
</book>

http://tech.ddvip.com/2009-01/1232391569106396.html

CREATE TABLE dw_authors
(
	id  INT NOT NULL ,
	first_name  VARCHAR(50) NOT NULL ,
	last_name VARCHAR(50) NOT NULL ,
	PRIMARY KEY (id) 
);

CREATE TABLE  dw_books
(
	isbn  VARCHAR(13) NOT NULL ,
	title  VARCHAR(200) NOT NULL ,
	PRIMARY KEY (isbn)  
);

CREATE TABLE  dw_books_authors
(
	book_isbn  VARCHAR(13) NOT NULL ,
	author_id  INT NOT NULL ,
	PRIMARY KEY (book_isbn , author_id)
);


castor-3.1-jdo.jar   jta.jar



---jdo-conf.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jdo-conf PUBLIC "-//EXOLAB/Castor JDO Configuration DTD Version 1.0//EN" "http://castor.org/jdo-conf.dtd">
<jdo-conf>
	<database name="myJdo" engine="oracle">
		<driver class-name="oracle.jdbc.driver.OracleDriver" url="jdbc:oracle:thin:@//135.251.218.25:1521/hdmdb">
			<param name="user" value="dm" />
			<param name="password" value="dm" />
		</driver>
		<mapping href="sql-mapping.xml" />
	</database>
	<transaction-demarcation mode="local" />
</jdo-conf>

---sql-mapping.xml
<?xml version="1.0" encoding="UTF-8" ?>
<mapping>
	<class name="ibm.xml.castor.Author" identity="id">	//identify=
		<map-to table="dw_authors" />			//table=
		<field name="id" type="int">			//这里是小写,对应setId()方法
			<sql name="id" type="integer" />	//sql=   type=
		</field>
		<field name="firstName" type="string">
			<sql name="first_name" type="varchar" />
		</field>
		<field name="lastName" type="string">
			<sql name="last_name" type="varchar" />
		</field>
	</class>
</mapping>



JDOManager.loadConfiguration("jdo-conf.xml");
JDOManager jdoManager = JDOManager.createInstance("myJdo");
Database database = jdoManager.getDatabase();
database.begin();
Author author = new Author(1001, "Joseph", "Conrad");
database.create(author);	//加记录
Author lookup = (Author) database.load(Author.class, new Integer(1001));//查记录
System.out.println("Located author is named " + author.getFirstName() + " " + author.getLastName());
database.remove(lookup);	//删记录
database.commit();
database.close();


//多对多
<field name="authors" type="ibm.xml.castor.Author" collection="arraylist">
			<sql name="author_id" many-table="dw_books_authors" many-key="book_isbn" />
</field>


database.setAutoStore(true); //可级联保存





