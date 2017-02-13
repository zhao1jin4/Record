PHP 5
<?
	Header("Content-type:image/jpeg");
	$arr=file("counter.txt");  //存数字,生成验证码
	$count=(int)$arr[0];
	$f=fopen("counter.txt","w");
	fputs($fp,++$count);
	fclose($fp);
	$im=ImageCreate(45,16);
	$white=ImageColorAllocate($im,255,255,255);
	$black=ImageColorAllocate($im,0,0,0);
	imagefill($im,1,1,$black);
	ImageString($im,5,0,0,sprintf("%05d",$count),$white);
	imageJpeg($im);
	imagedestroy($im);
?>


email,文件上传,连接oracle,生成PDF,excel,

IIS服务 用Com组件来发邮件(要有SMTP服务器--windows 组件安装的)
<?
$MailObject=new COM("CDONTS.NewMail");
$att_file="f:\\exam.mdb";
$f_name="test.mdb";
$MailObject->To="xx@xx.com";
$MailObject->From="xxx.@sdf.net";
$MailObject->Subject="how are you?"
$MailObject->Body="Hello!";
$MailObject->AttachFile($att_file,$f_name,1);//  
$MailObject->Send(0;
$MailObject=null;

?>


"3"===3 为假   ,"3"==3 为真

=== 会对类型检查



支持JavaBean组件,JavaBean+EJB

<?
$exam=new JAVA("test.Message");//javav类
echo $exam->getMessage();//调用java 方法


//必须加载  phpjava.dll   
//php.ini文件加      ;extension=php_java.dll 前加;去掉

;extension=php_imap.dll


?>


phpinfo();//如查显示的信息中有Java ,表示加载成功





<?
//===================================书中的

require("xxx.php");//解释时先读文件，通常放文件首部
include("xxx.php")//只当读到include 时才读文件
\$ 转义

//常用函数
gettype(变量);//攻取变量类型
settype(变量，“类型”);//"string"
is_array(xx);//是否是数组类型
is_object();//是否是对象类型
is_double();//is_float的别名
is_float();
is_int();
is_integer();//is_int的别名
is_long();
is_real();//is_float() 的别名
is_string()


$val1="你好"；
$Vval2=&$val1;//&引用 同一内存
.=  //和+=

函数外的变量，作用域是PHP文件,函数内不能使用它们，非声明为 global;
static 在函数调用后还保留原值
$val="app";
$$val=5;//表示$app=5;  echo "结果是${$val}"
define("USERNAME","Tom");//定义常数
echo USERNANE;	//使用时不用加$
//php中预定义常量
__FILE__   //php程序文件名
__LINE__   //php程序行数
PHP_VERSION
PHP_OS
TRUE
FALSE
E_ERROR 运行时错误
E_WARNING
E_PARSE 
E_NOTICE

E_开头的 error_reporting ()使用

and 或者用 &&
or  或者用 ||
xor 异或  不相同 为真
@echo $a; //隐藏错误信息
/* 
-> 对象
=>数组

定义函数时可以给默认值，默认按值传递，加& 传引用

函数名也可变
*/
function f1($arg="hello")//参数默认值
{ echo $arg;} 
$val="f1";
$val();//调用函数；

date(Y年m月d日);
$_POST["name"];        //得到POST请求参数name的值

$f=fopen("xxx.txt","a+")//附加方式
fwrite($f,"aaa");
fclose($f);
fread($f,filesize("xxx.txt"));

trim();
ltrim();
rtrim();
chop();//rtrim()的别名

addcslashes("select * from student where name='lisi'","'");//在'前加 "/"
stripcslashes("hello/world");//去 "/"

&amp;//&
&quot;//"
'&#039;'//'
&lt;//<
&gt;//>


htmlspecialchars()  //常用的 HTML转换成 &#
htmlentities()		//所有的
html_entity_decode("str",ENT_COMPAT,"gb2312")  //把&# 转换成HTML
ENT_COMPAT//只转换双引号
ENT_QUOTES //全转换(单引号和双引号)
ENT_NOQUOTES //全不转换

nl2br() //所换行 转换成<br>
get_html_translation_table()

split ()//和java 一样的
list()

preg_split()
explode();
implode();
chunk_split();
wordwrap();

sprintf();
printf();
sscanf();
fscanf();
vsprintf();
number_format();

substr();
substr_replace();

strpos();//
strlen();

chr(int x); //把ascii 转换成字母
ord(string str);//

strcmp()
strcasecmp()
strncmp();
strncasecmp();
strnatcasecmp();
strstr();
natsort();
natcasesort();


strtolower()//转换成小写
strtoupper();
ucfirst();//第一个字母大写
ucwords()//每个音讯 首字母大写


ereg("^[a-z]",$email) //正则
ereg_replace
eregi  //忽略大小写
eregi_replace
split
spliti
[[:space]]  //表示正则中的空格

index=>value
array(1,2,8=>6)//key可以是 "a"=>"lisi"
print_r($arr);//打印数组

array_walk($arr,"mywalk")  //mywalk(&$arr,$key)//会把下标入,不可以改数组

$info = array('coffee', 'brown', 'caffeine');
list($drink, , $power) = $info;//给一些变量赋值
each()//返回数组中当前的键／值对并将数组指针向前移动一步 


array_pad($arr,10,2);//增加数组大小
array_splice() //减少数组
array_intersect()//数组求交
array_merge()//合并数组
array_reverse()//反排值，
array_slice()//要数组中的一部分
sort()
rsort()//反排
usort()//自定义排序
$myarr["mykey"]="myval";//增加数组元素
unset ($myarr["mykey"])删数组元素/变量
isset()//数组key/变量存在吗？
in_array("myval",$myarr);//数组中有值吗？


类中->username   ->后不用加$

$stu=new Student; //$stu=new Student();也是OK的

子类中调用父类的方法,属性 parent::
parent::mymethod();

 const mycons = 'constant value'//类中的不可变的 self:mycons

预定义的函数 __set() 和__get()
__construct()//构造函数
__destruct()//析构函数
__clone()//复制对象
__clone中包含$this,$that
	$this 指向复本
	$that 指向原本


Student::get_count//是static 
self::get_count//self 来引用 static 的属性

如果调用了一个不存在的方法，会调用 __call($function_name,$args);

//如果类不存在的，调用__call
function __autoload($class_name)  
{
	include("$class_name"."php");
}

serialize(对象)//把对象转换为字串
unserialize

__sleep() //串行化前调用，返回数组,要串行化的属性
__wakeup() //返回对象时调用



$connect=mysql_connect("localhost","root","")
	or die("Could not connect: " . mysql_error());;
mysql_select_db("test",$connect);
mysql_query("insert into student(name) values('lisi')");
$result=mysql_query("select * from student");

mysql_num_rows($result)//结果集中行的数目

mysql_result($result,2，"name")//第三行的name字段
while($row=mysql_fetch_row($result))
{
}
while($row=mysql_fetch_array($result,MYSQL_ASSOC))//MYSQL_NUM, MYSQL_BOTH
{
}
while($row=mysql_fetch_object($result))
{
}
mysql_free_result($result);
mysql_close($connect);

mysql_pconnect() ；//连接池 

mysql_data_seek($result,2)//行指针移动到指定的行号

mysql_list_dbs();
mysql_list_tables();
mysql_list_fields("database1", "table1", $link);//mysql_field_name();
mysql_num_fields();

mysql_field_name();
mysql_field_table();
mysql_field_type();
mysql_field_flag();//not null;
mysql_field_len();
mysql_affected_rows();//update,delete,insert

mysql_error($connect);
mysql_errno($connect);

mysql_change_user()//改变用户
mysql_insert_id();//MySQL 内部的 SQL 函数 LAST_INSERT_ID() 来替代


require_once("xxx.yy")//可以不是.php
include_once("xxx.php");




$f=fopen("filename",string mode,"directory_path")
r,r+ //读写
w //清文件，可创建
w+//读写
a,a+

fclose($f);
readfile("http://    ftp://");//读整个文件,不用fopen
fread()//读当前到指定长度
fgets 读一行
fpassthru()//读当前到结尾，会关闭
rewind ()//文件指针到开头

feof();

fwrite($f,"xxx",int len)
print "";
file_exists();//文件存在吗？
fileperms()//显示文件权限
is_dir()
is_executable()
is_file()
is_link()
is_readable()
is_writeable()

file ()//返回数组，文件是每一行
copy (source,dest)
rename();
opendir();
closedir();
readdir();//while 每次返回目录中的文件名
chdir()//改变目录
getcwd()//当前目录
filectime()//创建时间
filemtime()//最后修改


session_start();
var $myvar="lisi";
session_register("myvar")//加session变量,没有$
session_is_registered("myvar");

session_unregister();
session_destroy();//注释所有Session 变量 

$_SESSION["myval"];

header("Location:http://www.sina.com");//页面跳转
		Content-Type:
		Status

php.ini文件中 取消注释  extension=php_gd2.dll 才可以画图像
imagegif()
imagejpeg()
imagepng()

imagecreatefromgif()
imagecreatefromjpeg()
imagecreatefrompng()

imagecopy()
imagecopyresized()
imagedestory();

getimagesize()//有四个单元的数组。索引 0 包含图像宽度的像素值，索引 1 包含图像高度的像素值。索引 2 是图像类型的标记：1 = GIF，2 = JPG，3 = PNG，4 = SWF，5 = PSD，6 = BMP，7 = TIFF(intel byte order)，8 = TIFF(motorola byte order)，9 = JPC，10 = JP2，11 = JPX，12 = JB2，13 = SWC，14 = IFF，15 = WBMP，16 = XBM。这些标记与 PHP 4.3.0 新加的 IMAGETYPE 常量对应。索引 3 是文本字符串，内容为“height="yyy" width="xxx"”，


getimagesizex();
getimagesizey();
sin()
pi();
imagedashedline()//画一虚线
imagepolygon();//多边形
imagearc() //弧

imagefilledarc()    //IMG_ARC_PIE 饼图，
					//IMG_ARC_CHORD  弦,只是用直线连接了起始和结束点
					//IMG_ARC_NOFILL ,IMG_ARC_EDGED  
imagefilledrectangle()
imagefilledploygon()




header("Content-type:image/png");
$im = imagecreate(100, 100);//创建图像
$red = imagecolorallocate($im, 255, 0, 0);
$blue = imagecolorallocate($im, 0, 0, 255);
imagefill($im, 0, 0, $red);
imagefilledrectangle($im, 10, 10,90,90, $blue);
imagestring($im, 5, 20, 20, "Hello world!", $red);// 第二个，如果 font 是 1，2，3，4 或 5，则使用内置字体。
imageline($im,0,20,100,20,$red);
imagesetpixel($im,50,50,$red);  //画点 ，可用它画曲线
imagepng($im);//输出给浏览器 ，也可给每二参数 ”文件名“
imagedestroy ($im);

imagefilltoborder ()



getdate();// 返回当前时间数组
$mydate=getdate();
echo $mydate["seconds"];//－－－看文档吧

gettimeofday()

date () //格式化日期 //－－－看文档吧 
parse_url()//返回各部分组成的数组 //－－－看文档吧
urlencode();
urldecode();



php.ini 是允许文件上传 file_uploads = On
						;upload_tmp_dir =
						upload_max_filesize = 2M
enctype="multipart/form-data"
<input type="file" name="userfile"/>
$_FILES["userfile"]["name"]  //客户端原文件的名称
$_FILES["userfile"]["type"]  //客户端原文件的类型
$_FILES["userfile"]["size"]  //客户端原文件的大小
$_FILES["userfile"]["tmp_name"]  //上传服务器端临时文件的名称
$_FILES["userfile"]["error"]  //客户端原文件的名称
							//0 UPLOAD_ERR_OK ，成功
							//1 UPLOAD_ERR_INI_SIZE  超upload_max_filesize大小
							//2 UPLOAD_ERR_FORM_SIZE 超表单的 MAX_FILE_SIZE 大小
							//3 UPLOAD_ERR_PARTIAL   只部分上传
							//4 UPLOAD_ERR_NO_FILE 没有文件上传

move_upload_file($_FILES["userfile"][tmp_name],"c:/" . $_FILES["userfile"]["name"]);
$_FILES["userfile"]["name"][0] //如是多个同名的文件

php预定义变量




$_SERVER['PHP_AUTH_USER']
$_SERVER['PHP_AUTH_PW']
$_SERVER['AUTH_TYPE']

$HTTP_SERVER_VARS 或者是 $_SERVER


header("WWW-Authentication: Basic realm='请先登录'");

fsockopen("www.sina.com",80,$errno,$errstr,30); //如果超过30秒,就把错误号,错误信息写入变量  ,udp://
//再用 fget,fput,来收发,

imap_open("mail.aa.com","username","password",int opt)//{MAIL_SERVER}  可以是NNTP,IMAP,POP3 ,登录邮件
	OP_READONLY  只读
	OP_ANONYMOUS 匿名
	OP_HALFOPEN  只连接,不打开
	CL_EXPUNGE	关闭时清除邮箱的邮件
imap_open($mail_server ."/pop3:110}INBOX","uid","pwd")  //协议和主机要用 "/" 分, 服务器部分用{}
imap_open($mail_server ."/nntp:119)comp.test","uid","pwd")
//多地址用逗号分隔


mail("xxx@sin.com","subject","message","From:yy@sina.com\r\n")//发送邮件
$myboundary=uuiqid("");
带附件的邮件(可以先看以发过的)  
头部信息中有  "Content-type:multipart/mixed;boundary=$myboundary\r\nFrom:xxx@sina.com\r\n"     是由一个以上部分来组成的
邮件消息中 -- 用来分隔   邮件的多部分


Content-disposition:attachment 提供下载链接
Content-disposition:inline;filename=xxx.html 显示在邮件中,


和上传一样要 enctype="multipart/form-data"

$Rboundary=uniqid("");
$myheader="Content-type:multipart/mixed;boundary=$boundary\r\n";

消息体中
$mime="application/unknown";   //$_FILES['upload']['type']


///=============mail 实例==
$myboundary=uniqid("");
$headerss="Content-type:multipart/mixed;boundary=$myboundary\r\nFrom:xxx@sina.com\r\n";
if(S_FILES['upload_file']['type'])

	$mime_type=S_FILES['upload_file']['type'];
else
	$mime="application/unknown";

$file_name=S_FILES['upload_file']['name'];
$file=S_FILES['upload_file']['tmp_name'];

$fp=fopen($file,"r");
$read=fread($fp,filesize($file));
$read=base64_encode($read);//base64 编码
$read=chunk_split($read);//字符串切成 76字符组成的小块

$body="--$myboundary
Content-type:text/plain;charset=iso-8859-1
Content-transfer-encoding: 8bit
文本消息内容
--$myboundary
Content-type:$mime;name=$file_name
Content-disposition:attachement;filename=$file_name
Content-transfer-encoding:base64
$read
--$myboundary
";
mail("xxx@sin.com","subject",$body,$headers)//发送邮件

///=============


$dom=new DomDocument();
$dom->loadXML("<?xml....");
$dom->save("xxx.xml");
$dom->load("xxx.xml");
$dom->saveXML();//将XML 输出到浏览器
$dom->createElement("name");
$dom->createTextNode("this is a text");
$dom->appendChild($xx);
$dom->documentElement;//根节点
$dom->getElemnetsByTagName("employee"); //返回节点数组 ,使用textContent 
$xp=new DomXPath($dom);  //DOMXPath
$xp->query("/employees/employee/username")

$dom->removeChild($xx);//->documentElement 根节点
$xp->query("//person[name='lisi']");//
->replaceChild($x,$y);
->item(0);
iconv();






php.ini 文件 中
display_errors=On/Off   //错误报告
error_reporting=E_ALL //错误级别,会显示变量未初始化   ,E_ALL & ~E_NOTICE ,不看到多的信息

如想phpeclipse 中调试要php.ini中有
display_errors=On
error_reporting=E_ALL

调试对话框中->Environment->Interpreter->点New->Browser->选择php.exe
 如不是windows系统 取消Open with DBG Session URL ininternal browser box

调试对话框中->Environment->Remote Debug->在Remote Source path中输入 php文件的绝对路径,不是web路径

die  print
foreach ($_GET as $key=>$i)
$_GET[$key]





//===================================上书中的


date(y)//当前年份 两位的 ，Y 是四位
date(n) //当前月份 ,没有前导零 ,m有前导零
date(j) //当前日,没有前导零 , d有前导零
date(w) //0（表示星期天）到 6（表示星期六）

//字符串的相加用 “.”


round()//四舍五入
floor()//小整数
ceil()//大整数
floatval ('122.34343The');//要其中的数字
doubleval
sin()
cos()


<input type="checkbox" name="my[]"/>  //要用[],否则只有最后一个
<input type="radio" name="you"/>
$_POST["you"];
$you;// 必须是 php.ini文件中 register_globals=On 才可以

linux 中locate php.ini 可以搜索

$_REQUEST[""] 包括$_GET和$_POST 和Cookie中的

php.ini文件中   
SMTP = localhost
smtp_port = 25
; For Win32 only.
;sendmail_from = me@example.com

; For Unix only.  You may supply arguments as well (default: "sendmail -t -i").
;sendmail_path =

mail(收信人，主题，消息,"From:lisi@sina.com\nReply-To:lisi@sina.com");
收信人 用"," 分多个

Cc:xx@sina.com   //抄送 Copy
Bcc:yy@sina.com   //暗送 Black


$_SERVER['SERVER_NAME']  //

如何发送附件？？？？？？？？？？？

-----------------------------------



















 ------------php连接oracle
  oci_connect()
 
 
 
 ./configure  --prefix=/usr/local/php5   --with-apxs2=/usr/local/apache2/bin/apxs 
 --enable-sigchild 
 --with-config-file-path=/usr/local/apache2/conf  改变PHP配置文件路径
--with-oci8=$ORACLE_HOME  											默认是$ORACLE_HOME


cp php.ini-dest /usr/local/apache2/conf/php.ini   ###可先不手复制它(php.ini-recommended是不行的)
 

要安装libxml2-devl
 
 php.ini 并执行下列操作：
   1. 把下面这一行解除注释（删除该行开头的分号）： extension=php_oci8.dll
oci8.persistent_timeout =20

AddType application/x-httpd-php  .php  放在最外面

phpinfo();如显示“OCI8 Support enabled” 表示OCI组件安装成功



oracle 自带的用户scott;也是锁定的,也有默认表存在


//localhost:1521/orcl" 当数据库名   Instant直接的 ,方便的,  Instant Client
 ORACLE_SID 或 ORACLE_HOME ,LD_LIBRARY_PATH 和 NLS_LANG环境变量

oci_connect ( string $username  , string $password [, string $db 
  							 [, string $charset  [, int $session_mode  ]]] )
  							
  OCI_DEFAULT, OCI_SYSOPER and OCI_SYSDBA. If either OCI_SYSOPER or OCI_SYSDBA 
  


当同一脚本中出现第二个 oci_connect() 时，将返回前一个高速缓存的连接。脚本完成时将清除该高速缓存。
Oci_new_connect() 提供了一个完全独立的连接。连接之间相互独立
  
  oci_connect()总是没有反应????????????????
  
 







 --enable-fastcgi
PHP自带的Zend,ext目录下有mysql,mssql,pgsql,oci8,sybase,relection,xml

-------------php 解析XML 例子

  $xml_file   =   'slashdot.xml';     					//   分析的内容     
  $type   =   'UTF-8';     											 //   定义字符集，默认是UTF-8    
  $xml_parser   =   xml_parser_create($type);    //   建立解析器      
  
  xml_parser_set_option($xml_parser,   XML_OPTION_CASE_FOLDING,   true);    
  xml_parser_set_option($xml_parser,   XML_OPTION_TARGET_ENCODING,   'UTF-8');    
   
  //   告诉PHP发现元素时要调用什么函数    
  //   这些函数同时也处理元素的属性    
  xml_set_element_handler($xml_parser,   'startElement','endElement');    
   
  //告诉PHP对字符数据调用什么函数    
  xml_set_character_data_handler($xml_parser,   'characterData');    
   
  if   (!($fp   =   fopen($xml_file,   'r')))   
  {    
          die("无法打开   $xml_file   文件进行解析!n");    
  }    
   
  //   通过循环来解析整个文件    
  while   ($data   =   fread($fp,   4096))   
  {    
          if   (!($data   =   utf8_encode($data)))   
          {    
                  echo   'ERROR'."n";    
          }    
          if   (!xml_parse($xml_parser,   $data,   feof($fp)))   
          {    
                  die(sprintf(   "XML   error:   %s   at   line   %dnn",    
                  xml_error_string(xml_get_error_code($xml_parser)),    
                  xml_get_current_line_number($xml_parser)));    
          }    
  }    
   
  xml_parser_free($xml_parser);  
  
   
 -----------手册有 PHP 解析XML
simplexml_load_string
simplexml_load_file

xml_parser_create
xml_parser_set_option
utf8_encode  将 ISO-8859-1 编码的字符串转换为 UTF-8 编码
xml_set_element_handler
xml_set_character_data_handler
xml_parse
xml_error_string
xml_get_current_line_number
xml_parser_free

  
-----------------手册有 PHP 反射
$class = new ReflectionClass('Counter');
$class->isInterface() 
$class->getProperties(), 
$class->getMethods(), 

    
-------------手册有 PHP FTP
$conn_id = ftp_connect($ftp_server);
$login_result = ftp_login($conn_id, $ftp_user_name, $ftp_user_pass);
ftp_put($conn_id, $destination_file, $source_file, FTP_BINARY);
ftp_close($conn_id);
----------------手册有 PHP PDF 库
pdf_new ( )

 -----------手册有 PHP Zend



快速排序
function quicksort($seq) {
  if (count($seq) > 1) {
    $k = $seq[0];
    $x = array();
    $y = array();
    for ($i=1; $i<count($seq); $i++) {
      if ($seq[$i] <= $k) {
        $x[] = $seq[$i];
      } else {
        $y[] = $seq[$i];
      }
    }
    $x = quicksort($x);
    $y = quicksort($y);
    return array_merge($x, array($k), $y);
  } else {
    return $seq;
  }
}





-------------PHP 5的 mysqli 扩展
$link = new mysqli('192.168.0.20', 'root', '', 'world'); //(SQL)节点
if( mysqli_connect_errno() )
	die("Connect failed: " . mysqli_connect_error());
$query = "SELECT Name, Population FROM City  ORDER BY Population DESC LIMIT 5";
if( $result = $link->query($query) )
{
	 while($row = $result->fetch_object())
	 {
		  $row->Name, $row->Population);
	 }
} 
else
    echo mysqli_error();
$result->close();
$link->close();













?>



