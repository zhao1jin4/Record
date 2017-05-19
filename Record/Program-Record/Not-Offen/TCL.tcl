Active TCL

容易嵌入其它应用程序
可以处理异常



命令分隔以";" 或者空格
\ 可以分离成多行命令
\x 十六进制

\[ 转议

大小写是敏感的
注释使用 #

使用变量要加$

set iNum2 43; set iNum3 44  
set sMyvar "Hard Disk"
puts $sMyvar
#结果 => Hard Disk

set sAreaCode (213)
puts ${sAreaCode}555-1212
#==> (213)555-1212

[] 中的tcl单独执行,返回结果来替代[]
set iResult [ expr 2 + 2 ]


set iNum 99
puts "The value of iNum is $iNum"    
要做变量解析
#==> The value of iNum is 99

puts "Some text; and
more text"
#==> Some text; and
#==> more text

set iCount 99
puts {The value of iCount is $iCount}   
不做变量解析
#==> The value of iCount is $iCount

puts {hello \n hello; $more [text]}
==>hello \n hello; $more [text]
 


set iSize 2
incr iSize 2
puts $iSize
==>4


#append varName value ?value
append sPartName Memory " " 128 MB
puts $sPartName
==>Memory 128MB


#The iSize and sName variables are deleted
unset iSize sName

expr (10/2) + 3
==>8


format
regexp
regsub
scan
string compare
string first
string index
string last
string length
string match
string range
string tolower
string toupper
string trim
string trimleft
string trimright
 
 
string length "A test string"
==>13
 
string index "Hello World" 4
==>o
#第4个的字母,0开始

string range "A test string" 7 end
==>string
#从第7个到结尾

string first "!" "Help!Me!"
==>4
#第一个!的位置

string last "!" "Help!Me!"
==>7

string compare "First" "Second"
==>-1
# -1, 0,  1 分别表示第一个小于,等于,大于第二个

string match "*test*" "A test string"
==>1
#0表示不匹配

string tolower "A Test String"
==>a test string

string toupper "A Test String"
==>A TEST STRING

string trim " A test string !!!!" " !"
==>A test string
#删首和尾部的 !,默认是空格

string trimleft " A test string !!!!" " !"
==>A test string !!!!

string trimright " A test string !!!!" " !"
==> A test string

regexp "does|match$" "Does this match"
==>1
#0 表示不匹配

#regsub exp 	string 							substitute  	varName
regsub "match$" "match test - Does this match"  "**replaced**" sStr
==>1
puts $sStr
==> match test - Does this **replaced**

format "The %s number %d is %x in %s" \
decimal 23 23 hex
==>The decimal number 23 is 17 in hex

scan "Decimal 23 = 17" "%s %d = %x" a b c
==>3
#Variable a=Decimal, b=23 and c=23. Returns number of scanned  items (3).



set lComps [list Memory Mouse "System Box"]
==>Memory Mouse {System Box}

set lComps [concat $lComps [list Monitor Keyboard]]
==>Memory Mouse {System Box} Monitor Keyboard

#lappend varName value ?value
lappend lComps Monitor
==> Memory Mouse {System Box} Monitor

lindex $lComps 2
==>System Box

llength $lComps
==>5

lrange $lComps 0 2
==> Memory Mouse {System Box}

linsert $lComps 2 RAM
==> Memory Mouse RAM {System Box} Monitor Keyboard

#lreplace list first last ?value value …?
lreplace $lComps 3 3
==> Memory Mouse RAM Monitor Keyboard
#Item 3 of list lComps is replaced with nothing


#Returns the index of the first item in list(-1 if there are no matches)
lsearch -glob $lComps *r*
==>0
#which is Memory, item 0

#Options include -ascii, -integer, -real, -dictionary, -increasing, -decreasing, -index x, -command cmd .
# -real把列表元素转换成浮点数
# -command 使用command作为一个比较命令，比较两个元素。这个命令需要返回一个整数，表示小于、等于或大于0，分别对应第一个元素是小于、等于还是大于第二个元素。 
	
lsort -ascii $lComps
==> Keyboard Memory Monitor Mouse RAM

lsort -integer -index 1 /
		{{First 24} {Second 18} {Third 30}}

返回 {Second 18} {First 24} {Third 30}, 


lsort -index end-1  {{a 1 e i} {b 2 3 f g} {c 4 5 6 d h}}
返回 {c 4 5 6 d h} {a 1 e i} {b 2 3 f g},


#比较二维中的0,1 ,即i,e,o
lsort -index {0 1} {
       {{b i g} 12345}
       {{d e m o} 34512}
       {{c o d e} 54321}
    }
返回 {{d e m o} 34512} {{b i g} 12345} {{c o d e} 54321} 

split [list a b c] \n

join $lGroups "|"
==>HCC Corp|Sales|Manufacturing


数组,索引可以是字串,像Map
set aPerson(firstname) Tom
#The array aPerson is created

puts $aPerson(firstname)
=> Tom


array exists
array size
array names
array set
array get

array exists aPerson
==>1

array size aPerson
==>2

array names aPerson
==>firstname lastname

#从list创建
array set aPerson {name Joe job Boss}
#Creates array aPerson and associates name with Joe and job with Boss

array get aPerson
==>name Joe job Boss

#TCL 内部数组变量 env,
array get env

#TCL 内部数组变量 客户端,以tcl_ 开头
array get tcl_platform

#info keyword arg…
info vars s*
info exists sPart
info procs


break
continue
for
foreach
if
switch
while

# }{ 必须在一行上
if { $sPart == "Memory" } {
puts "Found Memory"
} elseif {$sPart == "Mouse"} {
puts "Found Mouse"
} else {
puts "Not Found"
}
#注意{的位置

#Options include -exact, -glob, -regexp
switch ?options? string {
value body
?default body …?
}

switch -exact $sType {
Memory {mql print Type $sType }
"System Box" -
Microcomputer {mql delete Type $sType}
CustOrder {mql icon type $sType verbose}
default {puts "Undefined Type" }
}

for {set iNum 0} {$iNum<3} {incr iNum} {
puts $iNum
}
=> 012

#while test body
while { $iNum < 10 } { incr iNum }



foreach sRole [split [mql list role] \n] {
puts \
[mql print role $sRole select name person dump]
}

#eval 命令
set sType "Monitor"
set cmd {mql print bus $sType select policy}
eval $cmd
==>Production

source file.tcl


catch { mql print type Mousx } sMessage
==>1
puts $sMessage
==>Error: #1900068: print type failed


errorInfo和errorCode只在错误异常中被设置

error message ?info? ?code?

Generates an error with the specified error message, aborts the
current Tcl script, and places info into the errorInfo variable and
code into the errorCode variable



Procedures 可以用户自定义命令
使用 "proc" 创建 ,调用只要 名字
global
proc
return
upvar


语法
proc name argList body

proc pMyprint { sText } { puts $sText }


#可对参数设置默认值
proc pAddOrder { sType sName sRev { sPol "Order" } } {
mql add bus $sType $sName $sRev \
policy $sPol
}


#args 可以得到参数个数
proc pNotify { sSubj sMsg args } {
foreach sPerson $args {
mql send mail to $sPerson subject $sSubj \
text $sMsg
}
}


调用
pNotify "Meeting" "Room B - 2:00 PM" Ni Joe


proc getPersonsFromRole { sRole } {
set lPersons \
[mql print role $sRole select person dump]
return $lPersons
}

proc 中的变量是局部变量

global iPersons  全局变量

proc getListLength { sList } {
global iPersons
set iPersons [llength $sList]
return $iPersons
}
set iPersons 0
getListLength "Joe Mary Ni"
==>3
puts $iPersons
==>3

upvar 传递引用,常用于数组

upvar sVarName1 myVarName1
?sVarName2 myVarName2 …?


set Memory(RAM) 100
set Memory(Price) 100
proc PrintArrayElement { aArrayVar sElement} {
upvar $aArrayVar aLocalArray
puts $aLocalArray($sElement)
}
PrintArrayElement Memory Price
==> 100

#打开文件,以只读方式,返回文件ID
set fInput [open "file.tcl" r]
==>file64

#读文件下一行(15 bytes)
gets $fInput sText
==>15




Reads and returns the next numBytes bytes of data in fileId
read fileId numBytes
read $fInput 50


read ?-nonewline? fileId 
读并返回fileId标识的文件中所有剩下的字节。如果没有nonewline开关，则在换行符处停止。 

eof fileId ,返回1,0
tell $fInput
==>199
current access position is at byte 199


seek fileId offset ?origin?
Origin may be start, current, or end, and defaults to start
seek $fInput 50 start


#向文件中写
puts $fOutput "This is a test"


flush fileId
close fileId


file atime
file copy
file delete
file dirname
file executable
file exists
file extension
file isdirectory
file isfile
file join
file lstat
file mkdir
file mtime
file owned
file readable
file readlink
file rename
file rootname
file size
file split
file stat
file tail
file type
file writable



file stat file.tcl aFileInfo
array get aFileInfo
==>mtime 853450007 atime 853945190 gid 0
nlink 1 mode -32320 type file
ctime 853450007 uid 0 ino 0
size 21932 dev 2

格式化日期,时间
clock format timevalue


glob pattern - Return a list of filenames
glob *.bat


调用外部程序 ,支持IO 重定向

exec
exit
open
pid

exec notepad &
























