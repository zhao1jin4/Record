 Go 语言说是为了替代Python
 有 Scrapy 库做爬虫  pip install Scrapy 
 
目前用于人工智能  TensorFlow (也有swift语言)深度学习
https://github.com/tensorflow/swift

基础教程
http://www.runoob.com/python3/python3-tutorial.html

 

----- tool
eclipse marketplace安装  Python 插件 PyDev-6.2 

官方看版本对应关系(Eclipse 4.7.2, Java 8: PyDev 6.2.0)

下载PyDev离线安装包 
https://sourceforge.net/projects/pydev/files/pydev/  可以dropsin安装 Eclipse 4.7.2可以dropsin 安装PyDev 6.2.0

可以debug 

可以开发Jython,IronPython
Jython : Python for the Java Platform
IronPython:开源实现Python语言.Net Framework
windows 要下载安版本
preferences->PyDev->Interpreters->Python Interpreter->选择python.exe所在路径-> 提示找到 DLLs ,lib目录,lib/site-packages
代码提示很好
shift+enter 自动换行缩进 ,ctrl+1有很多功能


PyCharm-2018.1.3(基于IntelliJ IDEA)
linux 破解 http://idea.toocruel.net 
Settings->Project:<项目名> -> Project Intercepter -> 可选择python版本

----- windows 

如果下载.exe安装版,.py文件可以双击执行
安装版本默认安装在 C:\Users\zh\AppData\Local\Programs\Python\Python36  有.chm文档,有Lib目录是源码

----linux 版源码安装 
CentOS只有python2

xz -d -k Python-3.6.4.tar.xz 
./configure
./configure --with-ensurepip=install --enable-optimizations 
make
make test
sudo make install

默认安装在  /usr/local/bin/python3



----安装pip命令  
linux PyPI

https://pypi.python.org/pypi/pip/
https://pip.pypa.io/en/stable/installing/ 下载pip-9.0.1.tar.gz  解压

python3 setup.py build
python3 setup.py install  默认安装在 /usr/local/bin/pip

就可以用 pip命令了 usr/local/bin/pip install PyMySQL  
pip install -r requirements.txt  	在requirements.txt中每行写一个依赖名,也可带版本如Django>=2.0,<3.0
提示要TLS/SSL 不影响使用
可不设置 PYTHONPATH=/usr/local/lib/python3.6/site-packages/PyMySQL-0.8.0-py3.6.egg


--windows pip安装 
C:\Users\zh\AppData\Local\Programs\Python\Python36\Scripts>easy_install.exe pip  
要设置 PYTHONPATH=C:\Users\zh\AppData\Local\Programs\Python\Python36\Lib\site-packages
pip3 install PyMySQL   
pip3 install Scrapy  				windows下要 Visual C++ 14.0 (VC2015)
pip3 install --upgrade tensorflow 	默认CPU 

python -m pip install --upgrade pip 来升级pip到10.0.1
pip install --upgrade pip

如报  module 'importlib._bootstrap' has no attribute 'SourceFileLoader' 
执行 sudo python3 -m ensurepip --upgrade  重新安装 setuptools 工具

---配置本地默认源

国内仓库是豆瓣源 https://pypi.doubanio.com/simple/

pip3 install -i https://pypi.doubanio.com/simple/ Django
pip install --index-url https://mirrors.aliyun.com/pypi/simple/  Django


$ mkdir ~/.pip
$ tee ~/.pip/pip.conf <<-'EOF'
[global]
index-url = https://mirrors.aliyun.com/pypi/simple/
[install]
trusted-host= mirrors.aliyun.com
EOF

pip uninstall Django

------------pip 私有仓库服务 Nexus
https://help.sonatype.com/repomanager3 
https://help.sonatype.com/repomanager3/formats/pypi-repositories
 
还支持Docker, APT，YUM (linux的),P2(eclipse的）,go仓库,pypi(pip)仓库

上方第二个设置(Server Administration and Configuration)按钮 -> 左侧菜单 Repository->Repositories-> Create Repository 按钮->
pypi(hosted) 可用于上传本私服
pypi(proxy) 可配置公网 Remote Storage 填入	
	 https://pypi.org/ 
	 
pypi(group) 上面建的两个放一起(最好hosted类型的放最上)，如这里起名为my-pypi-group,生成地址为 http://127.0.0.1:8081/repository/my-pypi-group/

--- vi ~/.pip/pip.conf
[global]
index = http://localhost:8081/repository/my-pypi-group/pypi/
index-url = http://localhost:8081/repository/my-pypi-group/simple/

打印配置
pip config list -v
测试成功，可以当前服务没有的东西从代理上下载,服务上就有东西了

--- vi ~/.pypirc  即 twine
[distutils]
index-servers =
pypi
[pypi]
repository: http://localhost:8081/repository/pypi-internal/
username: admin
password: admin123

#pypi-internal 是配置的hosted名字，但group中查不到，如更换group就上传不了

python setup.py sdist upload -r local 上传后，目录就有东西了(setup.py参考下面内容)


pip install  magetool 发现是从localhost下载的，中间报错，检查后发现版本升级是否从官方下的，不对？？

------------------pip 私有仓库服务 pypiserver (小型的) 

https://github.com/pypiserver/pypiserver
https://wiki.python.org/moin/PyPiImplementations 有几种实现

直接在线安装  
sudo pip install pypiserver

离线安装
下载 pypiserver-1.3.0-py2.py3-none-any.whl
pip install ~/Downloads/pypiserver-1.3.0-py2.py3-none-any.whl


mkdir /opt/pip-repo
cd /opt/pip-repo/

下载要用到的包(如mako包) 到仓库目录
pip download -d /opt/pip-repo/ mako
	-d, --dest

启动 pypiserver 服务
pypi-server -p 8080  /opt/pip-repo/ &

就可仿问 http://127.0.0.1:8080/simple/  
下载地址为 http://127.0.0.1:8080/packages/


修改pip客户端 index-url 指向私服地址 ， extra-index-url 指向公网
tee ~/.pip/pip.conf <<-'EOF'
[global]
index-url = http://10.31.194.124:8080/simple
extra-index-url = https://mirrors.aliyun.com/pypi/simple/
[install]
trusted-host = 10.31.194.124
EOF


客户端下载也只能服务端配置目录有的才行，不像maven一样，如服务端没有自动在服务端增加

配置上传密码 Apache htpasswd 
pip install passlib （可能已经安装）
htpasswd -sc htpasswd.txt user1 提示输入密码，生成文件
pypi-server -p 8080 -P htpasswd.txt  /opt/pip-repo/ & 

=====上传包方法
示例 https://github.com/pypa/sampleproject/blob/master/setup.py

--setup.py
from setuptools import setup, find_packages
setup(
 name = "magetool",
 version = "0.1.0",
 keywords = ("pip", "pathtool","timetool", "magetool", "mage"),
 description = "time and path tool",
 long_description = "time and path tool",
 license = "MIT Licence",
 url = "http://www.abc.com",
# author = "mage",
# author_email = "mage@xx.com",
 packages = find_packages(),
 include_package_data = True,
 platforms = "any",
 install_requires = []
)
--
 python3 setup.py bdist_egg   #生成 dist/magetool-0.1.0-py3.6.egg 支持 easy_install 

 python3 -m pip install --user --upgrade setuptools wheel
 python3 setup.py sdist bdist_wheel  #生成 dist/magetool-0.1.0-py3-none-any.whl 文件
 
 python3 setup.py sdist     # 生成类 dist/magetool-0.1.0.tar.gz，支持 pip

--~/.pypirc  即用 setuptools
[distutils]
index-servers =
  local
  
[local]
repository: http://127.0.0.1:8080
username: user1
password: user1
 
---    
python setup.py sdist upload -r local 上传后，目录就有东西了

客户端使用 pip3 install magetool  --user 不是从这个服务上下载，不行？？？
–user参数的意思是安装给当电脑的当前用户，

 pip3 install   --index-url http://10.31.194.124:8080/simple magetool   ，不行？？？ 
---配置 easy_install 仓库 
建立 ~/.pydistutils.cfg 文件 
[easy_install]
index_url = http://10.31.194.124:8080/simple

--- 可以使用docker镜像  
docker run -p 80:8080 -v ~/packages:/data/packages pypiserver/pypiserver:latest
-v :前是主机目录，:后是容器中的目录
-p 格式 本机端口:docker端口
 

-----

可以 python test.py  但文件内容要为UTF-8的编码才行

编译
python -O -m py_compile test.py    会生成__pycache__目录中有.pyc二进制文件,可以保护代码, python .pyc 来执行
-O    optimize

也可写脚本来编译 comiple.py , 如果双击.py文件执行则只会在相同目录生成test.pyc文件
import py_compile
print('begin compile')
py_compile.compile('c:/tmp/test.py')    
print('end compile')

批量编译 都是生成__pycache__目录
import compileall
compileall.compile_dir(r'c:\tmp\pyDir')

或者  python -m compileall c:\tmp\pyDir


变量  _  表示上次打印的数字

>>>2*5
显示 10
>>>j=2
>>>j+_  #显示12,_是10
>>> print(3)  #这个不保存到 _ 中


>>> word = 'Python'
>>> word[0:2]   #子字串
>>> word[:2] + word[2:]   # Py   thon
>>> len(word)
>>> word[-2:]  #显示 on



>>> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g']
>>> #替换
>>> letters[2:5] = ['C', 'D', 'E']
>>> letters
>>> ['a', 'b', 'C', 'D', 'E', 'f', 'g']
>>> letters[2:5] = [] #删除,是List,不会有中间空的
>>> letters
['a', 'b', 'f', 'g']
>>> letters[:]  #全部的
>>> a = ['a', 'b', 'c']
>>> n = [1, 2, 3]
>>> x = [a, n]
>>> x
[['a', 'b', 'c'], [1, 2, 3]]
>>> i = 256*256
>>> print('The value of i is', i)

>>>2**3   #表示的2的3次方
 
>>> a, b = 0, 1  #同a=0 和 b=1
>>>while b < 10: #不能有缩进
    print(b, end=',') #前面一定要有tab(但是不能被复制上)或者空格,后面的代码都要和这行的缩进一样
    a, b = b, a+b   #a和b 同时赋值= 后面的, 即相当于a=b,后a还是原来的a,b=a+b
#结束循环一个回车

#输出 1,1,2,3,5,8, 

#第1次执行
1
#a=b=1    b=a+b=0+1=1    #给b赋值时使用的a还是老值，这行给a新的值不会立即被使用

#第2次执行
1
#a=b=1   b=a+b=1+1=2

 
x = int(input("Please enter an integer: "))
会提示 Please enter an integer: 42
if x < 0:
  x = 0
  print('Negative changed to zero')
elif x == 0:
  print('Zero')
elif x == 1:
  print('Single')
else:
  print('More')


words = ['cat', 'window', 'defenestrate']
for w in words:  
#for w in words[:]: 
  print(w, len(w))

for i in range(5): 
   print(i)

range(5, 10)   表示从 5 到 9
range(0, 10, 3) 表示从 0 到 9 步长为3
print(range(10))  #同range(0,10)


list(range(5))


10//3 取整除法

while True:
   pass  # Busy-wait for keyboard interrupt (Ctrl+C)


def ask_ok(prompt, retries=4, reminder='Please try again!'):
    while True:
        ok = input(prompt)
        if ok in ('y', 'ye', 'yes'):
            return True
        if ok in ('n', 'no', 'nop', 'nope'):
            return False
        retries = retries - 1
        if retries < 0:
            raise ValueError('invalid user response')
        print(reminder)
        
#ask_ok('accept deault ?',3)

print('\n')
i = 5

def f(arg=i): #如使用默认值,只会使用前面的,不会使用后同的
    print(arg)

i = 6
f()  #输出5

#-------------
'''    #多行注释
def f(a, L=[]):  #默认值只第一次初始化
    L.append(a)
    return L

print(f(1))
print(f(2))
print(f(3))

'''
#-------------
def f(a, L=None):
    if L is None:
        L = []
    L.append(a)
    return L

---书中的
#!/usr/local/bin/python3
#/usr/bin/python  是2 版本
'''
多行注释
控制台退出使用exit()  或ctrl+Z
'''
from sys import stderr

for i in range(5): 
   print(i)  #utf-8 file ,必须缩进   大小写严格 
print('hello \nworld') #可用' ' ,也可用 " "
print ((6-4j)*(6+4j)) #复数
print ( 7/2)
print ( 7.0/2) 


#n=n+1 #变量使用前必须初始化 
n=2
#n++ #没有 ++ --

#转换
print( float(26))
print( int(5.2))
print( hex(26))

(x,y)=(10.0,50)#多变量赋值
print(x)
print(y)


import math
print(math.pi,math.e) # , 打印出是空格分隔
print(math.sqrt(3**2 + 4**2)) #** 是平方

import cmath #得数
import random
print ("random=",random.random()) #0到1

#-----------string

print ("first="+str(20) )#必须str转换
print ("first=", 20  ,) #最后一个,没有空格
print ("string=%s num=%d" % ('hello',20 )) # %像函数

name="wang"
print ("name=%s  " % name) #  

print ("_" *5) #显示5个_
print ( len("hello"))
print (  "hello"[1])

worlds= "   left and right strip  "
print ( worlds.strip())
print ( worlds.lstrip())
print ( worlds.rstrip())
print ( worlds.center(100))

print ( worlds.split())#默认空白,可传,返回list
print ( "name,20".split(",")) 

print( "-".join(worlds.split()) )

print( "/usr/bin/bash".find("/") )
print( "/usr/bin/bash".rfind("/") )
print( "/usr/bin/bash".replace("/", "\\") ) 
print( "/usr/bin/bash".count("/") ) #出现次数
#----------list



myList=[3,"my list item",2.345,n] #任意类型
print(myList[1]) 
myList[0]+=3
print(myList[0])
print(myList[-1]) #最后一个元素
print("len=",len(myList)) #最后一个元素


#----range
numList=range(10) 
sublist=numList[2:6]
print("sublist=",sublist)
sublistHead=numList[:6]
sublistTail=numList[5:]
print(sublist)


print( numList[-1])
print("numList=",numList[-1])
#---list methond

strList=["10","5","a","x"]
strList[1:4]=["55","aa"]
strList.sort()
strList.reverse()
strList.append(99)
print(" append strList=",strList )

oneList=["one","tow","three"]
oneList.extend(strList)
print("oneList=",oneList )
oneList.insert(2,"2222") #后面的元素向后推
print("after insert oneList=",oneList )

delElement=oneList.pop(2);
print("  oneList=",oneList )
oneList.pop() #删最后一个,并返回
print("  oneList=",oneList )


#-------dict

dict={} #空的
dict['1']="Monday"
dict['2']=['Tuesday',"two"]
dict['2'].append("二")

print(dict)

weekDict={
    "one":"monday",
    "two":"tuesday",
    "three":"wedsday"
    }
print(weekDict)
print(weekDict.keys())#dict_keys(['one', 'two', 'three'])
print(weekDict.__contains__("one")) 
print(weekDict.__len__()  ) 
print(len(weekDict))

weekDict.__delitem__("one") 
print(weekDict)
 
#------------function ,for, if
def countWorld(lines,mapWord): #后要有:
    for num in lines: # 后要有:
        if num == 3 : # 后要有: == 可用于任可相同的类型
            print("three")
        elif num ==4: # 后要有:
            print("four")
        else:  # 后要有:
            print("other=",num)
    print("map len=",len(mapWord))

countWorld(range(0,5),{})         
print("123"=="123")      

inputList=["1","2",".","3"]
while True:  #True 首字母大字 关键字
    print( inputList.pop(0))
    if inputList[0]==".":
        break
#------------IO 
''' 
print("please input a lot of world,会把命行参数做为文件去读内容,如没有就从stdin读入内容")
import fileinput,re
for line in fileinput.input():
    print(fileinput.filename()+" first line is ")#<stdin>
    print(line)
    worlds=re.findall(r"\w+",line.lower())
    print(worlds)
    break

 '''
import time
print("准备写文件" )
fileOut=open("output.txt","a") #w a
fileOut.write(time.asctime()+"\n")#write不会在文件未尾增加空行
#fileOut.close()

filein=open("input.txt","r") #当前目录 中
'''
print(filein.readline())#readline包括换行符
print(filein.readline().rstrip())
print(filein.readline().rstrip())
filein.close()
'''

'''
for line in filein.readlines():  #readlines返回list
    print(line.rstrip().center(80))

'''
 
print( filein.read() )
print("end of read file" )

import sys
#print("please input something")
#stdinMsg=sys.stdin.read() #结束输入 按ctrl+Z

sys.stderr.write("this is my error message") # 不一定是按顺序打印的

print("program file name =%s ,args=%s" % (sys.argv[0],sys.argv[1:] ))

#fileOut.write(filein.read()) #复制文件

#getopt模块 ,linux shell也有这个功能

#----------OS command

import os,sys
'''
if not os.path.isfile("c:/tmp/input1.txt"):
    print("file input1 not exist")
    
if not os.path.isdir("c:/temp"):
    print("dir temp not exist")

for filename in os.listdir(os.getcwd()): #cwc当前目录
    print(filename)
    
if not os.path.isdir("c:/temp"):
    os.mkdir("c:/temp")
if not os.path.isfile("c:/temp/input_new.txt"):  
    os.renames("c:/tmp/input.txt", "c:/temp/input_new.txt")#移动重命名
    
import shutil
shutil.copy("c:/temp/input_new.txt", "c:/tmp/input.txt")

os.remove("c:/temp/input_new.txt")
os.removedirs("c:/temp")  #目录必须是空的
shutil.copytree("c:/tmp/", "c:/temp/")


os.system("where python") #结果写到标准输出上
'''
#---------- pipe
'''
readpipe=os.popen("netstat -an ","r")
for line in readpipe.readlines():
    print (line.rstrip())


#netstat -an | find "LISTENING"
readpipe=os.popen("netstat -an ","r")
writepipe=os.popen('find  "LISTENING"',"w")
writepipe.write(readpipe.read())
'''
#---------- regular expression
import re
maillist=["lisi@sina.com","wang@163.com}","sun@163.com"]
mailRe=re.compile(r"sina")
for email in maillist:
    if mailRe.search (email):
        print (email,"is match")
 
worlds="knock the knote"
regexp=re.compile(r"kn" ,re.M|re.I )#re.I忽略大小写
if regexp.match(worlds): #只从开头开始找
    groupRes=regexp.match(worlds).group()
    print(groupRes)

mailReg=re.compile(r"(.*)@(.*)")
(username,hostname)=mailReg.search("lisi@sina.com").groups()
print(username,hostname)


regSplit=re.compile(r",")
splitList=regSplit.split("lisi,30")
print(splitList)


 
hello="Hello World"
hellRe=re.compile(r"World")
newhello=hellRe.sub("sailor",hello ) #替换 r=replace?
print(newhello)
#写法二
newhello=re.sub(r"World","sailor",hello)
print(newhello)

numReg=re.compile(r"\d")
res=numReg.sub('X','input 123,password 455')
print(res)

#-----------class
#class MyClass(ParentClass):
class MyClass():
    def method1(self):
        self.x=1024
    def method2(self,newx):
        self.x=newx   
    def method3(self):
        return self.x    
obj=MyClass()
obj.method1()
obj.method2(333)
print(obj.method3())

#-----------Exception

try:
    filein=open("file1.txt","r")
except IOError:
    sys.stderr.write("can not open file file1.txt")
    #如何打印错误消息
    sys.exit(2)
    
#命令行调试程序  python -m pdb firstPython.py  
---上 书中的  









