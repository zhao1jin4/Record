perl -e 'print "hello world\n"' # 执行一条语句要加 ''

仅当取变量时才用$


以分号结尾

#圆括号常常是可选的
** 乘方 ,% 求模
$x**=2 #相当于$s=$x**2
int(rand(6))#int取整
sqrt ,log,exp,sin
$num=3.14E2# 也可以用e   后可以是-2
 . (点 来连接字符串)

\u 将一下字符转换成大写
\l 将一下字符转换成小写

\U 将所有字符转换成大写
\L 将所有字符转换成小写

'' 单引号中的的变量不做解释,
x 小写x 以字符串是相乘的作用  '*' x 20 //是20个*
length  字串的长度

index("much be much","much") 第一次出现字串的位置,#第一参多串,第二参是少串
rindex 最后一次出现字串的位置

substr("/usr/bin/bash",2,5)  //要指定的字串的从2开始,5 的长度,如要替换中的内容,在后加 =
substr("/usr/bin/bash",4) ="local/bin/bash"

my 变量声明为局部的


my $name=<STDIN>  # 标准输入到变量

chomp($name) #删除未尾的换行符,也可chomp(my $name=<STDIN>)

数组
my @array=(1,3,"lisi",$name);
print $array[0];
print $#array;#最后一个索引,也可是 -1 做索引

print scalar @array  #数组大小

my @newArray="0"x 8 ;# 只会所有字符放在第一元素中
my @nums=1..10;
my @alphas=('A'..'Z');
my @lines=<STDIN>  #每一行是数组的一个元素  在新行ctrl+d结束

print @lines ;# 打印数组

print "$_\n" foreach  (@lines); 和  foreach (@lines){ print "$_\n";}  效果是一样的



push (@lines,"one","two");#向数据尾加元素
pop (@lines); #删除最后一个,并返回

shift(@lines)# 数组向左 推动,第一个删除
unshift(@lines,"first")# 数组向右 推动,增加一个元素为第一个

my @sub=@players[0,3,6..9];
@player[1,4]=(10,20);     
my ($x,$y,$z)=(.66,1.23,0);  #多变量赋值


sort(@lines) #排序数组  将数字作为字符处理,
my @newList=sort(3,25);
my @numList=sort($a<=>$b) @newList; #按数字排序

reverse #返排

 散列表  (%变量名)
 key 是字串
 my %homedir=("lisi","/home/lisi","wang","/home/wang"); # 一键(唯一的),一值
 $homedir{"lisi"}="/opt/lisi"  ##用{}
reverse #把key ,value 对换 (重复的key 会删)

keys 返回所有key #my @users=keys(%homedir);
values 返回所有value
delete 删一个 delete($homedir{"lisi"})

defined  #变量是否已经赋值   
		print "ERROR" if(! defined $input);   //unless (defined $input)
unless 和 ! 有相同的功能

exists #是否存在的key ,exists($homedir{"zhang"})

字符串比较
	eq
	ne
	gt
	lt
	ge
	le



while 循环和 until 相反，　until 　是条件不成立时执行

未显式的给$_指定值, 就来处<STDIN>的值 ,和标准输出
length() 返回 $_的长度


each 遍历 散列表　　
while(my ($mykey,$myvalue)=each %userinfo) { 	}

foreach $line (@list) {print $line;}  //遍历　　数组
foreach  (@list) {print $_;}  



sub 定义过程　
调用要在前加&


sub myprint 
{
	
		print "$_\n" foreache(@mydata);
}
&myprint;  //&myprint($n);

传递 存在@_ 数组中  ,默认返回最后一条语句的值,也可以用   return
sub myfac
{
my $n=shift(@_);
my $fac=1;
$fac *= $_ foreach (1..$n);
print "$n factorial is $fac .\n";
}
chomp (my $num=<STDIN>);
&myfac($num);


--------------------文件IO
STDIN
STDOUT
STDERR

print STDERR "Error: xxxx";



cat.pl文件内容 
#!/usr/binperl -w 
use strict;
print "$_" while(<>);
以上脚本 和cat 命令作用一样的,可传文件名参数的

perl 将命令行参数 - 视为标准输入

grep " " myfiles |./cat.pl myheader - myfooter   ##先显示myheader文件内容,再grep 内容,再myfooter文件内容



@ARG 存程序的参数名   第一个参数是命令行的第一个参数    不是perl程序本身




#!/usr/bin/perl -p
是将脚本放入while(<>)中, 每次最后再 print $_

perl -pe

open MYFILE ,"myfile.txt" or die "文件不能打开,原因是$!";  
文件句柄通常大写  ,or die 退出前打印信息,$! 包含错误信息

$firstline=<MYFILE>

open MYFILE ,"< inputfile";
open MYFILE ,"> outputfile";
open MYFILE ,">>appendfile";

向文件写东西 print MYFILE "xxxx";

select MYFILE;
print $out;//写大量
select OUTPUT;//恢复默认

close MYFILE;

open MYFILE ,"ls |"
 
my @files =`ls`;  #`` 把命令结果给


if(! -e $xx)   //-e 是文件是否存在
{
	die "xx";
}

glob "mydir/*";   #返回一个，mydir 目录下的第一文件名
glob ("mydir/*");

-----------正则
$addr=~/mypattern/i　
#　=~ 用的方式　,i 忽略大小写  ,如果未指定字符串,就用$_;

\s 空白
\S非常空白
\d 数字
\D 非数字
\w 数字,字母,下划线
\W 非\w


$1 ,可以保存第一个()中的匹配

$string=~s/world/myworld/g   
#s 是替换,g 是全局的

$string=~s/(\d)+/$1*2/ge
#e 表示替换的是一个表达式,把计算结果,做为替换

$switch=~ tr/a-zA-Z/A-Za-z/; 
#tr 大小写的转换
$switch=~ tr/a-z/12345678901234567890123456/; 
#字母变数字


@password=split(/:/,$line);    
($myvar1,$myvar2)=split(/:/,$line);
#把$line中有":" 分隔成数组


join(':' ,@myarray);
join(':' ,$myvar1,$myvar2);
#数组的每一个元素用":" ,来连接成一个


grep (/[0-9]/,@mydata);
grep ($_<50,@numbers); 
#$_ 是@number 的每一个值
grep ($_*2,@numbers); 


www.cpan.org    
# perl 模块

find 'perl -e 'print "#INC"'' -name '*.pm' -print
查看系统的所有的perl 模块

use Math::Complex 
#使用perl 模块

perldoc socket   (大写的Socket)
#perl 文档

http://perldoc.perl.org
#在线文档
#CGI

FCGI (FastCGI)






CGI示例,结果是一个表单

#!/usr/bin/perl
use CGI qw/:standard/;
print header
	start_html("Report a Bug (Duckpond Software)"),
	b(i("Duckpond Software")),p,
	b("Report a Bug"),hr;
	

if(!param())
{
		print "File out this form adn click submit.",p,
		start_form,
			table(Tr([
				td([
					"Name",
					textfield(-name=>"name",-size=>34),
					]),td([
					"System",
					popup_menu(-name=>"system",
					-values=>["","UNIX Variant","MS Windows","Mac OS X"]),
					]),td([
					"Problem Discription",
					textarea (-name=>"descript",-cols=>30,-rows=>4),
					]),td([
					"",
					submit("Submit"),
					])
					
				])),
				end_form,p,"Thank you!";
			}
			else
			{
				print br,
				"Thank you for your sumission,",param("name"),".",br,
				"We will respond within 24 hours.",br,br,br,br;
				
			}
				print hr,
				a({-href=>"http://www.duckpond-software.com"},"Back to Home Page"),
				end_html;
					
			
	
	Fast CGI
	perl -d 用来调试perl 程序
	
	perl -MO=List  脚本名   
	用模块 B::Lint



要在首行加 print "Content-type: text/html\n\n"; 

	
	
	














































































































 







































