-----
eclipse marketplace安装  Python 插件 PyDev-5.4 ,安装时不会提示版本兼容,当使用时提示要eclipse-4.6 才行

官方看版本对应关系(Eclipse 4.5, Java 8: PyDev 5.2.0)
下载PyDev离线安装包 
https://sourceforge.net/projects/pydev/files/pydev/  可以dropsin安装

可以debug 

可以开发Jython,IronPython
Jython : Python for the Java Platform
IronPython:开源实现Python语言.Net Framework

preferences->PyDev->Interpreters->Python Interpreter->选择python.exe所在路径->提示stdlib source找不到
代码提示很好
shift+enter 自动换行缩进 ,ctrl+1有很多功能
-----


Python-3.5.2  下载win .zip 解压,放PATH环境变量中,没有源码,没有Doc

如果下载.exe安装版,.py文件可以双击执行
安装版本默认安装在 C:\Users\zhaojin\AppData\Local\Programs\Python\Python35  有.chm文档,有Lib目录是源码


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






