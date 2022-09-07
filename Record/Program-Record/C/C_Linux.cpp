http://pubs.opengroup.org/onlinepubs/007908775/  linux C 参考

对于内核来说，C++在运行时太大了


MySQL 源码用的boost C++ libraries 1.59 , 二进制文件名使用glibc  

Anjuta
Visual Studio Code  (Visual Studio Community )
eclipse CDT
CLion 基于Intellij Idea 


PATH=
命令行编译可不用设置下面环境变量
#LIBRARY_PATH 	=  C:\Program Files\mingw-w64\x86_64-8.1.0-posix-seh-rt_v6-rev0\mingw64\lib
#LD_LIBRARY_PATH 	=  C:\Program Files\mingw-w64\x86_64-8.1.0-posix-seh-rt_v6-rev0\mingw64\lib

#C_INCLUDEDE_PATH = C:\Program Files\mingw-w64\x86_64-8.1.0-posix-seh-rt_v6-rev0\mingw64\include
#CPLUS_INCLUDE_PATH =C:\Program Files\mingw-w64\x86_64-8.1.0-posix-seh-rt_v6-rev0\mingw64\include;C:\Program Files\mingw-w64\x86_64-8.1.0-posix-seh-rt_v6-rev0\mingw64\lib\gcc\x86_64-w64-mingw32\8.1.0\include

gcc -v -x c -E /dev/null 可以显示默认的搜索头文件目录 
	-x c 是C语言
	-x c++
	-E 只做预处理
显示有
 /usr/lib/gcc/x86_64-redhat-linux/11/include
 /usr/local/include
 /usr/include


复制mingw32-make.exe 为 make.exe

.cpp文件要用g++,而不能用gcc
 
 
ranlib 命令 效果同 ar s 命令   建索引 /// ar crs   (create replace ,add index) xx.a yy.o


windows(MinGW) 下dll转换成.a
	1.pexports libsqlapi.dll > libsqlapi.def
	2.dlltool --input-def libsqlapi.def --dllname libsqlapi.dll --output-lib libsqlapi.a -k 

	g++ -shared   -o xxx.dll yyy.cpp 来生成dll文件


生成动态链接库(linux)
	gcc -o libxxxx.so -shared -fPIC a.o b.o 
	使用时和静态库一样,加在一起编译,要加头文件的,文件名用全名就可以了(不用少前缀lib,不用少后缀dll)


------

/* 如果使用的是非GNU C, 那么就忽略__attribute__ */
　　#ifndef __GNUC__
　　# define __attribute__(x) /*NOTHING*/
　　#endif

_attribute__适用于函数的声明而不是函数的定义

__asm__		代替 asm 
__const__	代替 const, 
__inline__	代替 inline.


libstdc++6-devel-gcc9 

gcc-c++
gcc9-c++

-----------------------gcc
GNU Gerneral Public License (GNU GPL)
gcc -Wall (Warining all)

不些没有包含在-Wall   
-W
-Wconversion
-Wcast-qual
-Wwrite-stings
-Wtraditional

条件编译
#ifdef TEST   
	xxx
#endif
gcc -Wall -DTEST   (表示#define TEST=1)

cpp (Pre Processor预处理) 

cpp -dM /dev/null  打印出gcc已经定义的,都是以__开头的

-DNAME=VALUE  -DEBUG 
-DNUM="1+2"

-E 只做预处理 ,只做#include,#define

gcc -Wall -c -save-temps hello.c  会生成hello.i (预处理)->hello.s(汇编)->hello.o
gcc -MM main.c 生产一个依赖清单，如头文件，可用于makefile



ar crs xx.a xx.o xxx.o       //生成静态库 archive r=replacement,s add index,c=create
ar -crs libmy.a mylib.o //-可有可无

gcc -shared -fPIC -o libxx.so xx.o xx.o //-fPIC　或　-fpic 表示没有位置依赖,生成的是代码位置无关的,piｃ=position-independent code 
cpp 命令 同 gcc -E   预处理后停止，不编译　//gcc -E hello.c >hello01.c 或者用 cpp hello.c >hello01.c ,只展开#开头的
lint 废弃了
-g 生成调试信息
-S 生成汇编文件以.s结尾	//gcc -S hello.c ##-o hello.s可不用-o
-Os 优化大小
as 将.s -> .o　　// as hello.s  -o hello.o
-static 当同时发动态库和静态库时　,默认使用动态库,这个强制使用静态库
	测试下来是所有的要都使用静态库,-lstdc++,-lm,-c 找不到,只能安装　libstdc++-static.x86_64 只能解决-lstdc++,再安装glibc-static 解决-lm,-lc

替代LD_LIBRARY_PATH的另一个方式
/lib64/ld-linux-x86-64.so.2 --library-path ./ ./test #格式为　--library-path 查找路径　程序名,不用加-l参数　

ldd /bin/ls 命令打印现所依赖的共享

/etc/ld.so.conf 保存默认动态库的搜索路径，即LD_LIBRARY_PATH对应的值
	/usr/local/lib64
	/usr/local/lib
	include /etc/ld.so.conf.d/*.conf   (*/)
ldconfig	生成/etc/ld.so.cache文件, 修改ld.so.conf增加新的库路径后，需要运行一下 /sbin/ldconfig


ld 把所有的.o 和 .a 文件链接成 可执行文件 //  -lc libc.so 
ld -m elf_i386 -dynamic-linker  /lib/ld-linux.so.2  -o hello hello.o -lc   //cannot find entry symbol _start
用 ld --verbose 或 -V 选项 列出 有效的 模拟项

file hello  //Executable and Linking Format (ELF),Linux Standard Base (LSB) 有６４位的信息
objdump -hrt hello.o
ld -static -o hello -L`gcc -print-file-name=` /usr/lib/crt1.o /usr/lib/crti.o hello.o /usr/lib/crtn.o -lc -lgcc  -Bdynamic -lgcc_s OK
gcc -print-file-name=  结果是/usr/lib/gcc/i586-suse-linux/4.3/
# find hello.c hello.o hello -printf "%f\t%s\n"   //%f 去目录的文件名,%s是大小
nm hello  或者 objdump -d hello (--disassemble)
# strace -i ./hello > /dev/null
readelf -l hello
cat /proc/`ps -C hello -o pid=`/maps
strace -e trace=process -f sh -c "hello; echo $?" > /dev/null



C 语言的开头加一个 extern "C"
nm ~/eclipse_workspace/SynDB/egg_lib/libegg.so   来看so文件中有哪些方法
nm 可查看目标文件和程序库的符号地址 //有哪些方法   nm hello.o


gdb命令是linux的c的 debug环境
用gdb 必须在g++ 是用-g 选项
	>pwd 	//看当前目录
	>file xxxpro　//要debug的程序,也可开始时gdb ./xxxpro
	>break xx.c:20//设断点xx.c的20行  ,也可只 20,简写b
	>run
	>print i		//显示i变量值
	>po(print-object) xx
	>n(next)		//(不进函数内部)
	>step		//单步执行(进函数内部)
	>finish 	//从方法中出去
	>c(continue) //运行到下个断点
	>list		//附近代码
	>l(list) 10	//10行附近代码 ,l 10,20 显示10-20行的代码
	>set variable p=0	//改变变量p的值为0
	>frame			//打印当前帧,光标所要执行的位置的代码
	>bt(backtrace)		//显示当前在哪个文件的多少行,不显示代码，哪个函数
	>info break		//查看所有断点,有号 nfo b ,nfo breakpoints
	>delete 1	// 删第一个断点
	>break 21 if value==div //条件断点
	>condition 1 value==div //对已经设置的断点号,设置停止条件
	>info locals 		//显示所有的当前的局部变量
	>info threads
	>display i //每次停止时显示这个变量
	>disa(disable) 1 //禁用１号断点，没有数字就是全部断点
	>en(enable) 1//启用１号断点，
	>whatis //显示变量或表达式的类型，同ptype
	>ptype 
		
	>quit 
	clear FILENAME:NUM        删除断点。
ctrl+d 删除光标字符,ctrl+b向后,+ctrl+f向前,ctrl+a到头,ctrl+e到尾





ps -u 用户名
gdb  xxxpro 22(PID)
unlimit coredumpsize (csh/tcsh)

ulimit -c unlimited(bash/ksh) //开启，程序崩溃时存一个core文件，存失败时的函数，参数值，变量的值
gdb debugprogram(程序文件名) core.20472(生成的core文件)



g++的选项：


-O0
-O1
-O2
-O3
　　编译器的优化选项的4个级别，-O0表示没有优化,-O1为缺省值，-O3优化级别最高　 
-g
　　只是编译器，在编译的时候，产生调试信息。
-c
　　只激活预处理,编译,和汇编,也就是他只把程序做成obj文件
　　例子用法:
　　gcc -c hello.c
　　他将生成.o的obj文件 


-E
　　只激活预处理,这个不生成文件,你需要把它重定向到一个输出文件里面.
　　例子用法:
　　gcc -E hello.c > pianoapan.txt
　　gcc -E hello.c | more
　　慢慢看吧,一个hello word 也要预处理成800行的代码 



-MD -MP -MF


gcc test.cpp -o test `pkg-config --libs --cflags gtk+-2.0`    
才可用include<gtk/gtk.h> 要安装gtk2-devel才行

 
`g++ -print-prog-name=cc1plus` -v  或
/usr/lib64/gcc/x86_64-suse-linux/9/cc1plus -v 显示的 INCLUDE 都在CDT中



#define PI 3.14

#undef PI 表示后方PI无效

linux  下有
	#include <stdio.h>
	#include <stdlib.h>
	#include <string.h>

	#include <unistd.h> 　
	#include <errno.h>
	#include <netdb.h>
	#include <arpa/inet.h>
	#include <netinet/in.h>
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <sys/wait.h>　　 

======================Linux C  

#include<unistd.h>//unix standard 出现在其它linux头文件的前面 POSIX其它头文件可能会被影响
#include<stdio.h>
void testExeclp()
{
	//函数名l=link ,v=vector,e=enviroment,p=path ,表示参数
	//其它函数 execl,execv,execle,execve,execlp,execvp,execvpe ,出错返回-1,设置 errno
	//这些函数是进入指定进程，后面的代码不会被执行,如果想等执行完再执行后面的代码使用system函数
	if(execlp("ps","ps","-ef",0)<0)//到PATH中找ps,要替代新的进程,没有建立新进程,ID号相同 ,后面的代码不会执行
		perror("execlp error !");
	printf("这的代码不会被执行.\n");
}
void testExecle()
{
	char *const envp[]={"PATH=/tmp","USER=david",NULL};//必须以NULL结尾
  if(execle("/usr/bin/env", "env", "--help",NULL, envp) == -1 )//参数也一定是以NULL结束
//if(execle("/usr/bin/env", "env", NULL, envp) == -1 )//第二个参数env是argv[0],进程名不能为空
	{
		perror("Execle error\n");
	}
	printf("这的代码不会被执行.\n");
}

#include<sys/types.h>
#include<unistd.h>
#include<stdio.h>
#include<stdlib.h>
#include <sys/wait.h>
int main(int argc, char* argv[])
{
	pid_t pid; //进程ID  pid_t在sys/types中定义
	pid=fork();//返回进程ID,如果 是已经在子进程中调用fork返回0, 如果是父进程 调用fork 返回正数表示子进程ID,返回-1 失败
	char *msg;
	int n;
	int exit_code;
	switch(pid)
	{
		case -1:
			perror("FORK　FAILED");//输出错误
			exit(1);//0表示正常退出,非0表示,非正常退出
		case 0:
			msg="this is the child";
			n=5;
			exit_code=37;
			break;
		default:
			msg="this is the Parent";
			n=3;
			exit_code=0;
			break;
	}
	for (;n>0;n--)
	{
		puts(msg);//输出
		printf("当前进程 PID is %d\n",getpid());// getppid() 可以得到父进进程的ID
		sleep(1);//切换进程,防止优先级高的一直执行下去
	}
	if(pid!=0) //父进程来执行
	{
		int stat_val;
		pid_t child_pid;
		child_pid=wait(&stat_val);//父进程来等侍子进程,状态信息存在stat_val里面
		
		//waitpid(pid,&stat_val,WNOHANG);//指定子进程PID(如参数是-1返回任意一个子进程和wait一样)
		//WNOHANG不阻塞等待,如线程没有结束,也不等,用于检查子进程是否执行完成,如子进程运行返回0,结束返回子进程pid,失败返回-1设置errno
		//失败包括没有子进程ECHILD,被信号中断EINTR,EINVAL无效参数



		printf("child has finished :PID=%d\n",child_pid);
		if(WIFEXITED(stat_val))//是否正常退出
			printf("child exited with code=%d",WEXITSTATUS(stat_val));//退出码,这里是37
		 else
			printf("child RUN ERROR EXIT");
	}
	exit(exit_code);
	//当子进程执行完成时,不会立即退出,父进程可以会从子进程读信息,(这个子进程是一个僵进程ps 显示<defunct>)
}

vfork();//不从父进程复制,与父共享,修改时是CopyOnWrite的做法



int pipe_fd[2];
//建立一个管道,两个文件描述符,输出,fildes[0]是用来读的文件描述符,而fildes[1]是用来写的文件描. 0(读) 1(写)
//数据是先进先出FIFO,单向传输，如要实现双向,可打开两个管道

if(pipe(pipe_fd)<0)//<unistd.h>中的,底层的,返回 0 成功,-1失败,是在内存中的
//errno,的值EMFILE文件描述符打开太多M＝many(限制了) ,ENFILE 超过系统最大打开数
//fork()  开了进程前面打开的文件描述符还是开的,execl方式替代当前进程的方式里面文件描述符也是打开的


//可打开两管道,实现双向
FILE *pf=popen(const char *command, const char *mode);//stdio.h 上层标准库的,打开管道,mode可为r或w
pclose(pf);
const int  BUFSIZE=1024;
int main(int argc, char* argv[])
{
	FILE * fp;
	char * cmd="ps -ef ";
	char buf[BUFSIZE];
	buf[BUFSIZE]='\0';
	if((fp=popen(cmd,"r"))==NULL)
		perror("popen error!");
	while((fgets(buf,BUFSIZE,fp))!=NULL)
	{
		printf("%s",buf);
	}
	pclose(fp);
}




#include <sys/types.h>
#include <sys/stat.h>	

//命名管道FIFO(好处是可用于不相关的两个进程),　有命令行 mkfifo /tmp/myfifo 或 mknod /tmp/myfifo2 p , 用ls -lF命令权限最前显示为 p,最后标识显示|
//可用unlink或rm命令删除

int mkfifo(const char *path, mode_t mode); //命名管道,是一种文件,专为某进程使用,可以使用open函数打开,但不能同时读和写,只能读或者写
	open函数的选项 
errno如为EEXIST表示文件已经存在	

//方式二
mknod ("/tmp/my_fifo", 0777|S_IFIFO, 0);


		 
 mkfifo("/tmp/my_fifo", 0777);//会和umask计算得出权限
 /*
 cat < /tmp/my_fifo 会阻塞,除非&
另一终端
echo "xx">  /tmp/my_fifo
*/
 

const char * FIFO ="/tmp/myfifo";
if((mkfifo(FIFO,O_CREAT|O_EXCL)<0)&& (errno!=EEXIST))//open函数的选项 ,O_EXEC(全称是exclusive)：如果使用了O_CREAT 而且文件已经存在,就会发生一个错误.
	printf("can not create fifoserver\n");
fd=open(FIFO,O_RDONLY|O_NONBLOCK,0);//O_WRONLY,如果没有以读打开命名管道,以非阻塞写(O_WRONLY|O_NONBLOCK)的方试的打开会失败
unlink(FIFO);

//如果以写阻塞(O_WRONLY)方式打开,会使用PIPE_BUF缓存,如数据未满要么不写,要么写
//写阻塞(O_WRONLY)每次写<=PIPE_BUF大小的数据,即使多个进程同时向一个管道写,系统也能保证不会交错在一起.
PIPE_BUF 在 <limits.h>

---信号 是某种错误产生
#include <signal.h>
SIGINT  按 ctrl+c的信号
SIGQUIT 按 ctrl+\ 产生  eclipse结束进程按钮
SIGTERM 来自kill -[n]
SIGHUP 终端发出的结束信号

两个信号不能被忽略SIGKILL和SIGSTOP
每一个信号有一个缺省的动作

void single_func (int param)
{
	if(SIGALRM==param)
	{
		print_type=2; //在信号处理函数中使用printf 不安全
	}
}
signal (SIGALRM,single_func);


pause();//挂起,直到捕捉到一个信号
if(print_type==2)
{
	printf ("alarm timer down\n");
}



kill(pid_t pid,int signo);//发送信号
raise(int signo);//向自身进程发信号,等同 pthread_kill(pthread_self(), sig);

alarm(int seconds);//时间到了后会发出SIGALRM信号,如参数为0取消以前的定时器,每个进程只能有一个定时器

--信号集
sigemptyset(sigset_t * set);//初始化为空
sigfillset (sigset_t * set);//初始化为已有信号
sigaddset(sigset_t* set ,int signo);
sigdelset(sigset_t* set ,int signo);
sigismember(const sigset_t * set,int signo);//是否是成员

sigsuspend(&pendset);//将被阻塞未处理的信号输出.也像pause();挂起直到信号集中一个信号到达

int sigprocmask(int how, const sigset_t *set, sigset_t *oset); 告诉内核不允许发生信号集中的信号,像action.sa_mask.
	oset非空,返回进程当前屏蔽信号,
	how :SIG_BLOCK  增加
		SIG_UNBLOCK  删除
		SIG_SETMASK (设置,即可替换已有的)


int sigaction(int sig, const struct sigaction *act,  struct sigaction *oact);//用于取代早期的 signal函数
	指定信号使用act来处理
	oact返回老的参数
	struct sigaction
	{
		void(*sa_handler) (int) ,//SIG_DFL, SIG_IGN or 或者自己的函数,同singal.
		sigset_t 	sa_mask ,//在执行信号处理函数,在有哪些信号时block住,可以防止信号处理函数没有处理完成又来信号
		int 	sa_flags ,
		void(*sa_sigaction) (int, siginfo_t *, void *)//和sa_handler功能重叠,两个不能同时使用
	}
	sa_flags:  
	SA_NODEFER 不阻塞此信号
	SA_NOCLDSTOP 当子进程结束时,不要生成SIGCHLD 信号
	SA_RESTART 默认情况下信号指定的函数只处理一次,下次再收到相同信号就是默认处理,加上SA_RESTART一直是指定的函数处理
	SA_RESETHAND 表示第二次调用signal()可重置到SIG_DFL
 
int sigpending(sigset_t *set); //检测信号,返回阻塞的信号集

--守护进程
进程的父进程都是init进程,以root运行
fork();//建立子进程
setsid();//建立一个新会话(session),成为一新进程组的首进程,脱离终端
chdir("/"); //修改当前目录为 /
umask(0);//子进程重新设置(继承父进程的)
for(i=0;i<FOPEN_MAX;i++)
	close(i);//子进程关闭文件描述符(继承父进程的)
 
 //open函数操作文件,基于非缓存的方式
 错误写入日志文件,不能使用printf,perror
#include <syslog.h>//写入到/var/log/message

void openlog(const char *ident, int logopt, int facility);//ident是一个标识哪个程序的日志(前缀)
	logopt:
		LOG_CONS 日志写向控制台
		LOG_NDELAY
		LOG_PID 每个消息包含进程PID
	facility :
		LOG_DAEMON //是来自守护进程的消息
		
//默认写到/var/log/messages文件中， /etc/rsyslog.conf中有配置*.info /var/log/messages
void syslog(int priority, const char *message,...);//<syslog.h> ,%m 是errno转换后的消息，即　strerror(errno)
	priority :LOG_ERR,LOG_WARNING,LOG_NOTICE,LOG_INFO,LOG_DEBUG
void closelog(void);

int the_sys_log()
{
	FILE *f;　
	f = fopen("not_here","r");
	if(!f)
		//默认写到/var/log/messages文件中， /etc/rsyslog.conf中有配置*.info  /var/log/messages
		syslog(LOG_ERR|LOG_USER,"oops - %m\n");//<syslog.h> ,%m 是errno转换后的消息，即　strerror(errno)
		//在调用syslog前可不调用openlog
		
	int logmask;
	//第一个参数是每个日志的前缀，区分哪个程序，LOG_CONS,如果写日志失败如磁盘满，就发送到控制台
	openlog("logmask", LOG_PID|LOG_CONS, LOG_USER);
	syslog(LOG_INFO,"informative message, pid = %d", getpid());
	syslog(LOG_DEBUG,"debug message, should appear");//不显示，因/var/log/messages配置info
	logmask = setlogmask(LOG_UPTO(LOG_NOTICE));//LOG_UPTO;只记录>指定日志级别,LOG_NOTICE来记录，LOG_INFO就没有了
	syslog(LOG_NOTICE,"notice, record in file");//记录
	syslog(LOG_INFO,"info, should not appear");//不记录

	logmask = setlogmask(LOG_MASK(LOG_NOTICE));//也有LOG_MASK();只记录指定一个日志级别
	syslog(LOG_NOTICE,"notice, record in file");//记录
	syslog(LOG_WARNING,"WARNING, should not appear");//不记录
}





//--共享内存 share memory,所有的进程都可以看到,连接的进程可以使用,没有同步机制
#include <sys/shm.h>
 
   //key可传IPC_PRIVATE 只有当前进程可以仿问,说是不经常用的
int shmget(key_t key, size_t size, int shmflg); // 建立享内存,成功返回内存标识
	key 设置为 IPC_PRIVATE. 
errno 
	EINVAL 无效内存大小
	ENOENT 内存段不存在
	EACCES	无权限
void *shmat(int shmid, const void *shmaddr, int shmflg);  //at=attache  连接到进程的地址空间,返回内存地址,,第二个参数NULL表示让系统生成空间
	shmflg:SHM_RND
int shmdt(const void *shmaddr); //dt=detach,分离并没有删除

 //IPC_RMID删除一个正在连接的内存,是不确定的,还有IPC_SET设置权限,IPC_STAT查权限,第三个参数为shmid_ds类型
shmctl(shmid, IPC_RMID, 0) 
    	
ipcs命令可查Message Queues,Shared Memory,Semaphore的使用情况
inter-process communication (IPC)

ipcrm -m 0
	-M, --shmem-key
	-m, --shmem-id
   -S, --semaphore-key
   -s, --semaphore-id
   -Q, --queue-key
   -q, --queue-id
//--消息队列
#include <sys/msg.h>


int msgget(key_t key, int msgflg);//建立新的消息队列,返回id 
	key 可设置为 IPC_PRIVATE. 
	msgflg: IPC_CREAT

//man msgrcv 有消息格式
 struct my_msg_st {
	    long int my_msg_type;//消息类型,第一个必须是 long int类型,和long类型相同长度8
	    char some_text[BUFSIZ];
	};


//长度不能包括结体的第一个long int
int msgsnd(int msqid, const void *msgp, size_t msgsz, int msgflg);//如最后一个参数有IPC_NOWAIT,队列满时不会阻塞返回失败
	struct mymsg {
		long int    mtype;       /* message type */
		char        mtext[1];    /* message text */
	}
 
	
//长度不能包括结体的第一个long int
size_t msgrcv(int msqid, void *msgp, size_t msgsz, long int msgtyp,int msgflg);  //如最后一个参数有IPC_NOWAIT,没有消息时不会阻塞返回失败
	msgtyp : =0 只接收第一个可用的,>0是表示与(结体的第一个long)相同的第一个消息  ,<0 时表示比(结体的第一个long)小的第一个消息
	MSG_NOERROR

		
int msgctl(int msqid, int cmd, struct msqid_ds *buf);	
	cmd:
	IPC_STAT 取取消息存入msqid_ds结构中,
	IPC_SET 使用指定的msqid_ds修改,只用用户为 msg_perm.cuid 或者 msg_perm.uid 时和可执行
								只root可以修改msg_qbytes
		msqid_ds 的成员 
			struct ipc_perm msg_perm   operation permission structure
			msgqnum_t       msg_qnum   number of messages currently on queue
			msglen_t        msg_qbytes maximum number of bytes allowed on queue
			pid_t           msg_lspid  process ID of last msgsnd()
			pid_t           msg_lrpid  process ID of last msgrcv()
			time_t          msg_stime  time of last msgsnd()
			time_t          msg_rtime  time of last msgrcv()
			time_t          msg_ctime  time of last change
	IPC_RMID //Remove,权限同IPC_SET

//MSGMAX 是消息最大长度, MSGMNB 队列最大长度 定义在 <linux/msg.h>文件中,不能引入,报重复定义


void printTime(time_t rawtime){//time.h
	tm* timeinfo = localtime ( &rawtime );//转为当地时间,可以读年月日等
	char buffer [80];
	strftime (buffer,80,"%Y-%m-%d %H:%M:%S %Z",timeinfo);//把日期转字串,%Y-%m-%d %H:%M:%S  ,%X表示%H:%M:%S ,%Z 为CST或China Standard Time
	printf ("%s\n",buffer);
}
void printMsgQueueStat(int msgid){
	msqid_ds stat;
	msgctl(msgid, IPC_STAT, &stat);
	printf("message queue perm info : uid=%d,gid=%d,cuid=%d,cgid=%d,mode=%d\n", stat.msg_perm.uid, stat.msg_perm.gid, stat.msg_perm.cuid, stat.msg_perm.cgid, stat.msg_perm.mode);
	printf("number of messages currently on queue:%d\n",stat.msg_qnum);
	printf("max number of bytes allowed on queue:%d\n",stat.msg_qbytes);
	printf("pid of last msgsnd():%d\n",stat.msg_lspid);
	printf("pid of last msgrcv():%d\n",stat.msg_lrpid);

	printf("time of last msgsnd:");
	printTime(stat.msg_stime);

	printf("time of last msgsnd:");
	printTime(stat.msg_rtime);

	printf("time of last change:");
	printTime(stat.msg_ctime);
}


#include <sys/ipc.h>

key_t ftok(const char *path, int id);	 //ftok=file to key
	path必须是已经存在的(书上说必须是目录),id致少8位,要>0,
	返回IPC key ,为semget,msgget,shmget函数的第一个参数使用

//--进程用的信号量
//p(plus,pass进入临界区) 减1操作, 如为0挂起
//v 如有挂起就恢复运行,没有就加1操作

#include <sys/sem.h>
//第一个参数,唯一key,第二参数是数量.第三个权限,如加IPC_CREAT有已经建立过也不会报错,如要报错再|IPC_EXCL (exclusive)
//key可传IPC_PRIVATE 只有当前进程可以仿问,说是不经常用的
sem_id = semget((key_t)1234, 1, 0666 | IPC_CREAT);//后面都使用返回值(>0)操作



 union semun {
        int val;                    /* value for SETVAL */
        struct semid_ds *buf;       /* buffer for IPC_STAT, IPC_SET */
        unsigned short int *array;  /* array for GETALL, SETALL */
        struct seminfo *__buf;      /* buffer for IPC_INFO */
    };
 union semun sem_union;//senum 是自己定义的名字,X/Open要求程序员自己来做,man semctl里有的,直接复制(<linux/sem.h>里也有,不能include报重复定义)



 sem_union.val = 1;
 //第二个参数是编号,0是第一个.第三参数SETVAL 设置值,要求有val成员,IPC_RMID 删除不再用的
 semctl(sem_id, 0, SETVAL, sem_union) == -1 



 struct sembuf sem_b; //<sys/sem.h>
 sem_b.sem_num = 0;//编号,0表示第一个
 sem_b.sem_op = -1; // -1就是P操作,1就是V操作
 sem_b.sem_flg = SEM_UNDO;//SEM_UNDO表示如果进程没有释放信号量,就让操作系统自动释放
 if (semop(sem_id, &sem_b, 1) == -1) {//第三个参数是数组的个数
     fprintf(stderr, "semaphore_p failed\n");
     return(0);
 }
------lib_get_etc_name
    struct hostent *hostinfo; //<netdb.h>
    char myname[256];
    gethostname(myname, 255);
    
      //读/etc/hosts文件,还有byaddr
    hostinfo = gethostbyname(host); 
    printf("results for host %s:\n", host);
    printf("Name: %s\n", hostinfo -> h_name);
    printf("Aliases:");
    names = hostinfo -> h_aliases;
    while(*names) {
        printf(" %s", *names);
        names++;
    }
    printf("\n");
     addrs = hostinfo -> h_addr_list;
    while(*addrs) {
        printf(" %s", inet_ntoa(*(struct in_addr *)*addrs));//inet_ntoa 把in_addr转换为IPV4以点分隔的字串
        addrs++;
    }
    printf("\n");


    struct servent * servinfo= getservbyname("ssh","tcp");//读/etc/services 
    printf("ssh tcp port : %d",ntohs(servinfo->s_port));
-----lib_inet_xxx
char addr1[]="192.168.1.1";	
struct in_addr ip;
err = inet_aton(addr1, &ip);//把字串的IP地址转换为in_addr类型参数输出, aton=ascii to net

ip.s_addr = inet_addr(addr1);//把字串的IP地址转换为in_addr_t返回
//返回值如为-1错误和255.255.255.255表示的值冲突的
	
//inet_network(addr1);//把字串的IP地址转换为in_addr_t返回,.h文件中和书上说网络字节序,和inet_addr()不是一样吗??? (man 说是主机节序???)
	
str = inet_ntoa(ip);//把in_addr类型转换为类字串的IP地址返回,ntoa=net to ascii


inet_lnaof(ip);//返回IP地址中的主机部分in_addr_t类型, lnaof=local network address of
inet_netof(ip);//得到网络部分,netof=net of



in_addr_t net=0x7F;//如0开头是8进制
in_addr_t host=0x1;
in_addr res_ip=inet_makeaddr(net,host);//网段+主机
char* str_ip=inet_ntoa(res_ip);//127.0.0.1
printf("网段+主机 IP=%s\n",str_ip);

------socket unix 协议
void server()
{
    int server_sockfd, client_sockfd;
    int server_len, client_len;
    struct sockaddr_un server_address;
    struct sockaddr_un client_address; 
    unlink("/tmp/server_socket");
    server_sockfd = socket(AF_UNIX, SOCK_STREAM, 0);//UNIX协议 

    server_address.sun_family = AF_UNIX;
    strcpy(server_address.sun_path, "/tmp/server_socket");//本地socket文件
   // ls -lF /tmp/server_socket显示权限是   s开头 ,文件名以=结尾 /tmp/server_socket=
	//ps -lx 显示进程状态STAT为S (sleep)

    server_len = sizeof(server_address);
    bind(server_sockfd, (struct sockaddr *)&server_address, server_len);
 
    listen(server_sockfd, 5);
    while(1) {
        char ch;

        printf("server waiting\n"); 

        client_len = sizeof(client_address);
        client_sockfd = accept(server_sockfd, 
            (struct sockaddr *)&client_address, (socklen_t *)&client_len); 
		 //accept阻塞,可用fcnctl(socket,F_SETFL,O_NONEBLOCK|flags)设置非阻塞	
        read(client_sockfd, &ch, 1);
        ch++;
        write(client_sockfd, &ch, 1);
        close(client_sockfd);
    }
}

void client()
{
    int sockfd;
    int len;
    struct sockaddr_un address; //_un=unix <sys/un.h>
    int result;
    char ch = 'A';
 

    sockfd = socket(AF_UNIX, SOCK_STREAM, 0);//UNIX协议 
    address.sun_family = AF_UNIX;
    strcpy(address.sun_path, "/tmp/server_socket");//本地socket文件
    len = sizeof(address);
 
    result = connect(sockfd, (struct sockaddr *)&address, len);

    if(result == -1) {
        perror("oops: client1");
        exit(1);
    } 
    write(sockfd, &ch, 1);
    read(sockfd, &ch, 1);
    printf("char from server = %c\n", ch);
    close(sockfd); 
}


    
-------socket
 https://www6.software.ibm.com/developerworks/cn/education/linux/l-sock/tutorial/l-sock-3-2.html


//client(TCP)
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define BUFFSIZE 32
void Die(char *mess)
{
	perror(mess);
	exit(1);
}
int main(int argc, char *argv[])
{

	int sock;
	struct sockaddr_in echoserver;//in=Internet  要<netinet/in.h>
	//sockaddr_in6 是为IPV6
		
	char buffer[BUFFSIZE];
	unsigned int echolen;
	int received = 0;

	if (argc != 4)
	{
		fprintf(stderr, "USAGE: TCPecho <server_ip> <word> <port>\n");
		exit(1);
	}
	//PF=protocol family
	if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)//PF_INET6是IPV6版本的
	//	PF_INET 只是意味着它使用 IP(您将总是使用它)； SOCK_STREAM 和 IPPROTO_TCP 配合用于创建 TCP socket	
	//创建失败，它将返回 -1
	{
		Die("Failed to create socket");
	}

	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; //sin=sock Internet,AF=Address families
	echoserver.sin_addr.s_addr = inet_addr(argv[1]); //s_=sock
	echoserver.sin_port = htons(atoi(argv[3])); /* server port */
	
	
	/* Establish connection */
	if (connect(sock, (struct sockaddr *) &echoserver, sizeof(echoserver)) < 0)
	{
		Die("Failed to connect with server");
	}

	/* Send the word to the server */
	echolen = strlen(argv[2]);
	if (send(sock, argv[2], echolen, 0) != echolen)
	{
		Die("Mismatch in number of sent bytes");
	}
	/* Receive the word back from the server */
	fprintf(stdout, "Received: ");
	while (received < echolen)
	{
		int bytes = 0;
		if ((bytes = recv(sock, buffer, BUFFSIZE - 1, 0)) < 1)
		{
			Die("Failed to receive bytes from server");
		}
		received += bytes;
		buffer[bytes] = '\0'; /* Assure null terminated string */
		fprintf(stdout, buffer);
	}

	fprintf(stdout, "\n");
	close(sock);
	exit(0);
}



       


//TCP要关闭连接 
//回显服务器(TCP)

#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define MAXPENDING 5    /* Max connection requests */
#define BUFFSIZE 32
void Die(char *mess)
{
	perror(mess);
	exit(1);
}

void HandleClient(int sock)
{
	char buffer[BUFFSIZE];
	int received = -1;
	/* Receive message */
	if ((received = recv(sock, buffer, BUFFSIZE, 0)) < 0)
	{
		Die("Failed to receive initial bytes from client");///有警告,不建议使用从字符串常量到‘char*’的转换
	}
	/* Send bytes and check for more incoming data in loop */
	while (received > 0)
	{
		//在写socket时,读的那一个端(客户端连接断了)关闭,会生产SIGPIPE信号
		/* Send back received data */
		if (send(sock, buffer, received, 0) != received)
		{
			Die("Failed to send bytes to client");
		}
		/* Check for more data */
		if ((received = recv(sock, buffer, BUFFSIZE, 0)) < 0)
		{
			Die("Failed to receive additional bytes from client");
		}
	}
	close(sock);
}

int main(int argc, char *argv[])
{
	int serversock, clientsock;
	struct sockaddr_in echoserver, echoclient;

	if (argc != 2)
	{
		fprintf(stderr, "USAGE: echoserver <port>\n");
		exit(1);
	}
	/* Create the TCP socket */
	if ((serversock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
	{
		Die("Failed to create socket");
	}
	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	//echoserver.sin_addr.s_addr = htonl(INADDR_ANY); /* Incoming addr */
	echoserver.sin_addr.s_addr =inet_addr("127.0.0.1");//<arpa/inet.h>
	//arp=Address Resolution Protocol, 有arpaname 192.168.0.1命令
	
	echoserver.sin_port = htons(atoi(argv[1])); /* server port */
	//IPPORT_RESERVED //端口被保留最大值1024
	//IPPORT_USERRESERVED //5000
	
	//little endian 小端字节序：低字节存于内存低地址；高字节存于内存高地址  (intel全部是这个)
	//big endian 大端字节序：高字节存于内存低地址；低字节存于内存高地址 (网络字节序)
	
	/* Bind the server socket */
	if (bind(serversock, (struct sockaddr *) &echoserver, sizeof(echoserver))
	//看sockaddr_in源码最后一个属性是char[]类型,长度是根据sockaddr做减法,是可以强转的
			< 0)
	{
		Die("Failed to bind the server socket");
	}
	/* Listen on the server socket */
	if (listen(serversock, MAXPENDING) < 0)//第二个参数maximum length to which the queue of pending connections
	{
		Die("Failed to listen on server socket");
	}

	/* Run until cancelled */
	while (1)
	{
		unsigned int clientlen = sizeof(echoclient);
		/* Wait for client connection */
		if ((clientsock = accept(serversock, (struct sockaddr *) &echoclient,
				&clientlen)) < 0)
		{
			Die("Failed to accept client connection");
		}
		fprintf(stdout, "Client connected: %s\n",
				inet_ntoa(echoclient.sin_addr));
		HandleClient(clientsock);
	}
}
       



----client-(UPD)----

#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define BUFFSIZE 255
void Die(char *mess)
{
	perror(mess);
	exit(1);
}

int main(int argc, char *argv[])
{
	int sock;
	struct sockaddr_in echoserver;
	struct sockaddr_in echoclient;
	char buffer[BUFFSIZE];
	unsigned int echolen, clientlen;
	int received = 0;

	if (argc != 4)
	{
		fprintf(stderr, "USAGE: %s <server_ip> <word> <port>\n", argv[0]);
		exit(1);
	}

	// SOCK_DGRAM 和 IPPROTO_UDP 配合用于创建 UDP socket
	if ((sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0)
	{
		Die("Failed to create socket");
	}
	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	echoserver.sin_addr.s_addr = inet_addr(argv[1]);//s_=sock
	echoserver.sin_port = htons(atoi(argv[3]));//htons=host to net short
	/* Send the word to the server */
	echolen = strlen(argv[2]);
	//如果服务端没有启动,也能发出去,当前进程后面代码可以再收
	if (sendto(sock, argv[2], echolen, 0, //flag
		(struct sockaddr *) &echoserver, sizeof(echoserver)) != echolen)
	{
		Die("Mismatch in number of sent bytes");
	}
	fprintf(stdout, "Client Send:%s \n",argv[2]);
	/* Receive the word back from the server */
	fprintf(stdout, "Client Received: ");
	clientlen = sizeof(echoclient);
	if ((received = recvfrom(sock, buffer, BUFFSIZE, 0,//flag
			(struct sockaddr *) &echoclient, &clientlen)) != echolen)
	{
		Die("Mismatch in number of received bytes");
	}
	/* Check that client and server are using same socket */
	if (echoserver.sin_addr.s_addr != echoclient.sin_addr.s_addr)
	{
		Die("Received a packet from an unexpected server");
	}
	buffer[received] = '\0'; /* Assure null-terminated string */
	fprintf(stdout, buffer);
	fprintf(stdout, "\n");
	close(sock);
	exit(0);
}


-------UDP server ----


 #include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define BUFFSIZE 255
void Die(char *mess)
{
	perror(mess);//返回结果同调用 strerror(errno)
	exit(1);
}

int main(int argc, char *argv[])
{
	int sock;
	struct sockaddr_in echoserver;
	struct sockaddr_in echoclient;
	char buffer[BUFFSIZE];
	unsigned int echolen, clientlen, serverlen;
	int received = 0;

	if (argc != 2)
	{
		fprintf(stderr, "USAGE: %s <port>\n", argv[0]);
		exit(1);
	}

	// UDP 使用 SOCK_DGRAM, IPPROTO_UDP
	if ((sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0)
	{
		Die("Failed to create socket");
	}
	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	echoserver.sin_addr.s_addr = htonl(INADDR_ANY);//htonl=host to net long,INADDR_ANY 是0.0.0.0
	echoserver.sin_port = htons(atoi(argv[1])); /* server port */    

	/* Bind the socket */
	serverlen = sizeof(echoserver);
	if (bind(sock, (struct sockaddr *) &echoserver, serverlen) < 0)
	{
		Die("Failed to bind server socket");
	}

	/* Run until cancelled */
	while (1)
	{
		/* Receive a message from the client */
		clientlen = sizeof(echoclient);
		printf("server waiting...\n");
		//recvfrom调用会阻塞
		if ((received = recvfrom(sock, buffer, BUFFSIZE, 0,
				(struct sockaddr *) &echoclient, &clientlen)) < 0)
		{
			Die("Failed to receive message");
		}
		fprintf(stdout, "Client connected: %s\n",	inet_ntoa(echoclient.sin_addr));
		/* Send the message back to client */
		if (sendto(sock, buffer, received, 0, (struct sockaddr *) &echoclient,
				sizeof(echoclient)) != received)
		{
			Die("Mismatch in number of echo'd bytes");
		}
		//usleep(500);
	}
}


-------DNS 
#include <stdlib.h>
#include <stdio.h>          /* stderr, stdout */
#include <netdb.h>          /* hostent struct, gethostbyname() */
#include <arpa/inet.h>      /* inet_ntoa() to format IP address */
#include <netinet/in.h>     /* in_addr structure */

int main(int argc, char **argv)
{
	struct hostent *host; /* host information */
	struct in_addr h_addr; /* Internet address */
	if (argc != 2)
	{
		fprintf(stderr, "USAGE: nslookup <inet_address>\n");
		exit(1);
	}
	if ((host = gethostbyname(argv[1])) == NULL)
	{
		fprintf(stderr, "(mini) nslookup failed on '%s'\n", argv[1]);
		exit(1);
	}
	h_addr.s_addr = *((unsigned long *) host->h_addr_list[0]);
	fprintf(stdout, "%s\n", inet_ntoa(h_addr));
	exit(0);
}
------------------------pthread  线程---------POSIX----          

 g++ -o pthread pthread.cpp -lpthread (或者 -l pthread)

pthread_create(&thread_1, NULL, thread_func, NULL);
第一个参数为指向线程标识符的指针，第二个参数用来设置线程属性，第三个参数是线程运行函数的起始地址，最后一个参数是运行函数的参数
成功时，函数返回0

pthread_join(..)用来等待一个子线程的结束,返回值可是子线程的返回值
第一个参数为被等待的线程标识符，第二个参数为一个用户定义的指针，它可以用来存储被等待线程的返回值

pthread_exit( ..)参数为返回给主动线程的


pthread_create 函数的第二个参数pthread_attr_t 
	pthread_attr_init，这个函数必须在pthread_create函数之前调 
	线程绑定状态的函数为 pthread_attr_setscope()第一个是指向属性结构的指针，
		第二个是绑定类型  PTHREAD_SCOPE_SYSTEM(绑定的,被绑定的线程具有较高的响应速度，)和PTHREAD_SCOPE_PROCESS(非绑定的


线程以什么样的方式来终止自己(默认属性，即为非分离状态)
pthread_attr_setdetachstate(pthread_attr_t *attr, int detachstate)。第二个参数可选为PTHREAD_CREATE_DETACHED(分离线程)和 默认的PTHREAD _CREATE_JOINABLE(非分离线程)
//设置成了detach就不能join了
   
 pthread_attr_getinheritsched(&thread_attr);
 pthread_attr_setinheritsched(&thread_attr,PTHREAD_EXPLICIT_SCHED);//默认为PTHREAD_EXPLICIT_SCHED
 //还有PTHREAD_INHERIT_SCHED表示新线程沿用他线程的属性
 
 pthread_attr_setschedpolicy(&thread_attr, SCHED_OTHER);//默认的
 
一个线程为分离线程，而这个线程运行又非常快，它很可能在 pthread_create函数返回之前就终止了 ,
调用 pthread_cond_timewait函数//是 pthread_cond_timedwait  , pthread_cond_wait



优先级，它存放在结构sched_param中(成员 int sched_priority)。用函数pthread_attr_getschedparam和函数 pthread_attr_setschedparam进行存

//先取优先级，对取得的值修改后再存放回去
pthread_attr_t attr;
pthread_t tid;
sched_param param;
int newprio=20;

 //取优先级的范围
 max_priority = sched_get_priority_max(SCHED_OTHER);
 min_priority = sched_get_priority_min(SCHED_OTHER);
    
pthread_attr_init(&attr);
pthread_attr_getschedparam(&attr, &param);
param.sched_priority=newprio;//sched_param结构体中的sched_priority
pthread_attr_setschedparam(&attr, &param);//param设置优先级
pthread_create(&tid, &attr, (void *)myfunction, myarg);


在[ 进程 ]中共享的变量必须用关键字volatile来定
pthread_once_t once= PTHREAD_ONCE_INIT;

//它对线程外部的其它线程是不可见的
pthread_key_create (pthread_key_t  ,)第二个参,那么当每个线程结束时，系统将调用这个函数来释放绑定在这个键上的内存块。这个函数常和函数pthread_once 
pthread_once (pthread_once_t, 函数) //第一次调用pthread_once时它执行这个函数，以后的调用将被它忽略。
pthread_setpecific(pthread_key_t,x  ) //线程数据和一个键绑定在一起
pthread_getspecific(pthread_key_t key)

pthread_key_delete用来删除一个键，这个键占用的内存将被释放，但同样要注意的是，它只释放键占用的内存，并不释放该键关联的线程数据所占用的内存资源，而且它也不会触发函数pthread_key_create中定义的destructor函数


//pthread_mutex_xx  函数不设置errno

pthread_mutex_t mutex;
/* 用默认属性初始化一个互斥锁对象*/
pthread_mutex_init (&mutex,NULL);
特定属性的互斥锁，须调用函数pthread_mutexattr_init。函数

pthread_mutexattr_setpshared()	//PTHREAD_PROCESS_PRIVATE和PTHREAD_PROCESS_SHARED
pthread_mutexattr_settype()		//PTHREAD_MUTEX_NORMAL、PTHREAD_MUTEX_ERRORCHECK、PTHREAD_MUTEX_RECURSIVE和PTHREAD_MUTEX_DEFAULT


pthread_mutex_lock(&mutex);
pthread_mutex_unlock(&mutex);

struct timespec-> tv_sec 
struct timespec-> tv_nsec (Nano seconds)

函数pthread_mutex_trylock()

pthread_cond_t，函数pthread_cond_init()
pthread_condattr_t

pthread_cond_destroy(pthread_cond_t cond)
pthread_cond_wait(

pthread_cond_signal和函数pthread_cond_broadcast唤醒

#include<semaphore.h>中定 sem 开头的(线程用的信号量)

sem_t sem; 
sem_init (&sem, 0, 16);//最后一次参数是初始值

sem_post( sem_t *sem )//增加信号量,会使其中的一个线程不在阻塞
sem_wait( sem_t *sem )//减少信号量,被用来阻塞当前线程直到信号量sem的值大于0，解除阻塞后将sem的值减一，表明公共资源经使用后减少。
sem_trywait( sem_t *sem )和函数pthread_ mutex_trylock()起同样的作用,是函数sem_wait 的非阻塞版本，它直接将信号量sem的值减一
sem_destroy(sem_t *sem)

int semval=0;
sem_getvalue(&sem,&semval);


pthread_cancel(a_thread);//主线程对子线程发起取消命令

pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);//子线程设置可以被取消

pthread_setcanceltype(PTHREAD_CANCEL_DEFERRED, NULL);//子线程设置取消执行类型,
 //PTHREAD_CANCEL_DEFERRED 只在执行到某个函数(如 xxx_wait,thread_join,pthread_testcancel)时线程才会停止
 //PTHREAD_CANCEL_ASYNCHRONOUS 立即停止
 

<unistd.h> 中定义了sleep() 秒　和 usleep()微秒 microseconds
--------------

pthread_create 的返回值
EAGAIN
描述: 超出了系统限制，如创建的线程太多。
EINVAL
描述: tattr 的值无效。


pthread_join 的返回值
ESRCH
描述: 没有找到与给定的线程ID 相对应的线程。
EDEADLK
描述: 将出现死锁，如一个线程等待其本身，或者线程A 和线程B 互相等待。
EINVAL
描述: 与给定的线程ID 相对应的线程是分离线程



//pthread_detach(3C) 是pthread_join(3C) 的替代函数，可回收创建时detachstate 属性设
//置为PTHREAD_CREATE_JOINABLE 的线程的存储空间。
//在线程tid 终止时回收其存储空间。如果tid 尚未终止，pthread_detach() 不会终止该线程。
pthread_detach 的返回值
EINVAL
描述: tid 是分离线程。
ESRCH
描述: tid 不是当前进程中有效的未分离的线程。


pthread_key_create 返回值
EAGAIN
描述: key 名称空间已经用完。
ENOMEM
描述: 此进程中虚拟内存不足，无法创建新键。



pthread_key_delete 返回值
EINVAL
描述: key 的值无效。


pthread_setspecific 返回值
ENOMEM
描述: 虚拟内存不足。
EINVAL
描述: key 无效。



typedef unsigned long int pthread_t;

pthread_t pthread_self(void);


int pthread_equal(pthread_t tid1, pthread_t tid2);
如果tid1 和tid2 相等，pthread_equal() 将返回非零值，否则将返回零


pthread_once(&once_control, init_routine);
pthread_once 返回值
EINVAL
描述: once_control 或init_routine 是NULL。

 
linux下是没有 pthread_delay_np ,在Solaries下才有.

--------------
vi * 查找光标的单词

 sscanf()  // %*s) 加了星号 (*) 表示跳过此数据不读入

inittab文件是由ID为1 的init进程
 
#include<unistd.h>
pid_t getpid()//得到当前运行进程的ID
pid_t getppid() //可以得到父进进程的ID
#include <sys/types.h>; //uid_t ,gid_t
uid_t getuid(void); //<unistd> ,运行进程的用户实际ID
uid_t geteuid(void);//运行进程的用户有效 Effective ID
gid_t getgid(void);
git_t getegid(void);

对用户的其他信息 getpwuid
struct passwd my_info=getpwuid(getuid());

#include<pwd.h>
struct passwd
{
	char * pw_gecos;//Real name
};

#include<wait.h> #include<sys/wait.h> 
pid_t waitpid(

if(WIFSIGNALED(status))//因一个未捕获的信号退出返回非0值
	WTERMSIG(status) //如果WIFSIGNALED返回非0,这个返回信号代码 
if(WIFSTOPPED(status))//如意外中止返回非0值
	WSTOPSIG(status) //如果WIFSTOPPED返回非0,这个返回信号代码


 
#include <signal.h>;  //有kill定义
if(kill(getppid(),SIGTERM)==-1)//失败
  
#include <unistd.h>;
#include <sys/types.h>;
#include <sys/stat.h>;

int close(int fd);
int open(const char *pathname,int flags);
int open(const char *pathname,int flags,mode_t mode);
	flags 可以去下面的一个值或者是几个值
	O_RDONLY：以只读的方式打开文件.
	O_WRONLY：以只写的方式打开文件.
	O_RDWR：以读写的方式打开文件.
	O_APPEND：以追加的方式打开文件.
	O_CREAT：创建一个文件.
	O_EXEC：如果使用了O_CREAT 而且文件已经存在,就会发生一个错误.
	O_NOBLOCK：以非阻塞的方式打开一个文件.
	O_TRUNC：如果文件已经存在,则删除文件的内容.

	使用了O_CREATE 标志,那么我们要使用open 的第二种形式.还要指定mode 标志
	-------//这里的权限是和umask命令相结合的，如umask为0022组的写权限被禁止，这里给了也不行
	S_IRUSR 用户可以读 
	S_IWUSR 用户可以写
	S_IXUSR 用户可以执行 
	S_IRWXU 用户可以读写执行	
	-------
	S_IRGRP 组可以读 
	S_IWGRP 组可以写
	S_IXGRP 组可以执行 
	S_IRWXG 组可以读写执行
	-------
	S_IROTH 其他人可以读 
	S_IWOTH 其他人可以写
	S_IXOTH 其他人可以执行 
	S_IRWXO 其他人可以读写执行
	-------
	S_ISUID 设置用户执行ID 
	S_ISGID 设置组的执行ID

int creat(const char *path, mode_t mode);//相当于 open(path, O_WRONLY|O_CREAT|O_TRUNC, mode)

ssize_t write(int fildes, const void *buf, size_t nbyte);
ssize_t read(int fd, void *buffer,size_t count);


#include <fcntl.h> //file control
int fcntl(int fildes, int cmd, ...);
	F_DUPFD,复制文件描述符
	F_GETFD,F_SETFD//file description
	F_GETFL,F_SETFL//flag ,可修改的
	
	文件区域锁(说不能真正防止文件的读写，只是锁的记录方式，要程序去控制)　　不能同时使用fcntl(不能使用高级的fwrite,fread因为有缓存,要使用read,write) 和 lockf
	F_GETLK,F_SETLK,F_SETLKW//lock wait　会一直等下去,如果同一进程，直接能得到
	struct flock //为F_GETLK，F_SETLK,F_SETLKW使用
	{
		short l_type   //F_RDLCK, F_WRLCK, F_UNLCK(其它进程可以？)
		short l_whence //SEEK_SET, SEEK_CUR and SEEK_END
		off_t l_start  relative offset in bytes
		off_t l_len    size; if 0 then until EOF　0表示到文件尾
		pid_t l_pid    process ID of the process holding the lock; returned with F_GETLK
	}
　

lockf (int filedes,int function,off_t size_to_lock)	

 int fd;
 if ((fd = open("/tmp/lockf.txt", O_RDWR | O_CREAT | O_TRUNC )) < 0)
 	 perror("open error");
 lseek(fd,10,SEEK_SET);//原因为很早有seek函数，而后来的另一版本返回类型为long,现在已经是off_t
 //没有fcntl功能多，如读锁，但简单
 lockf(fd,F_TLOCK,20);//lockf 只能以文件当前位置开始，F_TLOCK先测试于再独占锁，还有F_LOCK独占锁，F_TEST
	
	
{
		int flags = -1;
		int accmode = -1;
		/*获得标准输入的状态的状态*/
		flags = fcntl(0, F_GETFL, 0);
	  if( flags < 0 ){
		/*错误发生*/
		printf("failure to use fcntl\n");
		return -1;
	  }
	  /*获得访问模式*/
	  accmode = flags & O_ACCMODE;
	  if(accmode == O_RDONLY)/*只读*/
		printf("STDIN READ ONLY\n");
	  else if(accmode == O_WRONLY)/*只写*/
		printf("STDIN WRITE ONLY\n");
	  else if(accmode ==O_RDWR)/*可读写*/
		printf("STDIN READ WRITE\n");
	  else/*其他模式*/
		printf("STDIN UNKNOWN MODE");

	  if( flags & O_APPEND )
		printf("STDIN APPEND\n");
	  if( flags & O_NONBLOCK )
		printf("STDIN NONBLOCK\n"); 

}
{
		int uid; 
		int fd = open("/tmp/test.txt", O_RDWR); 
		fcntl(fd, F_SETOWN,1000); //还有　F_DUPFD 复制文件描述符，功能和dup，dup2函数功能一样
		uid = fcntl(fd, F_GETOWN);
		printf("the SIG recv ID is %d\n",uid); 
	  close(fd);
	}	
	

	
int access(const char *pathname,int mode);
//R_OK 文件可以读,W_OK 文件可以写,X_OK 文件可以执行,F_OK 文件存在.
成功返回0,否则如果有一个条件不符时,返回-1.

off_t lseek(int fildes,off_t offset,int whence)//定位文件指针
	whence 取值
		SEEK_SET 绝对
		SEEK_CUR
		SEEK_END

		
#include <sys/stat.h>;
int stat(const char *file_name,struct stat *buf);
int fstat(int filedes,struct stat *buf);
stat 用来判断没有打开的文件,
fstat 用来判断打开的文件
lstat 返回链接本身的信息，stat是链接指向的文件信息

{
	char * filename="/tmp/records.dat"; 
	struct stat st;
  if( -1 == stat(filename, &st)){
  	printf("获得文件状态失败\n");
  	return -1;
  }

  printf("包含此文件的设备ID：%d\n",st.st_dev);
  printf("此文件的节点：%d\n",st.st_ino);
  printf("此文件的保护模式：%d\n",st.st_mode);
  printf("此文件的硬链接数：%d\n",st.st_nlink);
  printf("此文件的所有者ID：%d\n",st.st_uid);
  printf("此文件的所有者的组ID：%d\n",st.st_gid);
  printf("设备ID(如果此文件为特殊设备)：%d\n",st.st_rdev);
  printf("此文件的大小：%d\n",st.st_size);
  printf("此文件的所在文件系统块大小：%d\n",st.st_blksize);
  printf("此文件的占用块数量：%d\n",st.st_blocks);
  printf("此文件的最后访问时间：%d\n",st.st_atime);
  printf("此文件的最后修改时间：%d\n",st.st_mtime);
  printf("此文件的最后状态改变时间：%d\n",st.st_ctime);
} 

S_ISLNK(st_mode)：是否是一个连接.
S_ISREG 是否是一个常规文件
S_ISDIR 是否是一个目录
S_ISCHR 是否是一个字符设备
S_ISBLK 是否是一个块设备
S_ISFIFO 是否 是一个FIFO文件.
S_ISSOCK 是否是一个SOCKET 文件


#include <unistd.h>;
char *getcwd(char *buffer,size_t size);
我们提供一个size 大小的buffer,getcwd 会把我们当前的路径考到buffer 中.如果buffer
太小,函数会返回-1 和一个错误号.

#include <dirent.h>; // directory entries

struct stat 有一个->st_mode  S_ISDIR(statbuf.st_mode)
				->st_size
				->st_mtime
		ctime(&statbuf.st_mtime)//time.h


DIR *dirp=opendir("file");

struct dirent *direntp=readdir(dirp)//direntp->d_name  //文件名  ->d_namle文件名的长度(看头文件)


#include<iomanip>
#include<string>
#include<cmath>  //以c 开头
#include<cstdio>

int dup(int fd); //返回新的描述符和已经有描述符指向同一文件(通道),新的总是可用的最小值
int dup2(int oldfd,int newfd);  //如果newfd，前面已经打,先自动关闭.再从old复制到newfd

#include <sys/ioctl.h>
int ioctl(int fildes, int request, ... /* arg */); //向设备发送命令

{
  int fd = open("/dev/cdrom",O_RDONLY| O_NONBLOCK);//一定要加O_NONBLOCK ，否则报No medium found
 	if(fd < 0){
 		printf("打开CDROM失败\n");
 		char * reason=strerror(errno);//errno.h,string.h
		fprintf(stderr,"error! %s\n",reason);
		return -1;
 	}
 	if (!ioctl(fd, CDROMEJECT,NULL)){ //CDROMEJECT 在 linux/cdrom.h
 		printf("成功弹出CDROM\n");
 	}else{
 		printf("弹出CDROM失败\n");
 	}
}
		 	
#include <sys/time.h>

//FD_SETSIZE  是为 fd_set里最大数量
int select(int nfds, fd_set *readfds, fd_set *writefds,  fd_set *errorfds, struct timeval *timeout);
/* 第一个参数是测试从0到n-1个文件描述符,
* 第二参数测试是否可读的文件描述符集合,
* 第三个参数测试是否可写的文件描述符集合
* 第四个参数测试是否有错误的文件描述符集合
* 第五个参数超时时间,如参数为空会一直阻塞,如超时所有集合都会被清空,linux还会修改timeout为剩余超时时间
* 如果那三个集合中,如有一个测试可以操作,函数返回,并修改对就应的文件述符集合为正在操作的(和返回的一样),如三个集合中有一个为NULL对应的就不会被测试
*如超时返回0,－1错误,>0 是三个集合中的件描述符(和修改集合的一样)
*/


//fd_set 描述符集函数
void FD_CLR(int fd, fd_set *fdset);
int FD_ISSET(int fd, fd_set *fdset);
void FD_SET(int fd, fd_set *fdset);
void FD_ZERO(fd_set *fdset);


int nread;
ioctl(0,FIONREAD,&nread);//向0文件描述符发送命令和参数,FIONREAD就是返回缓冲区内有多少字节FIONREA=File IO N read
          
 
strerror() 定义在<string.h>

stdin
stdout
stderr
unistd.h中有定义STDIN_FILENO,STDOUT_FILENO,STDERR_FILENO

snprintf(buffer, BUFFER, "%s", argv[1]);

struct timeval  //定义在  sys/time.h
{
	long tv_sec; /* 秒数 */
	long tv_usec; /* 微秒数 */  //1秒等于1,000,000微秒
};

gettimeofday(struct timeval
difftime()

getpass 可以像linux输入密码一样


//==示例列目录
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/stat.h>
#include <dirent.h>
#include <string.h>
void printdir(char * dir ,int depth)
{
        DIR *dp;
        struct dirent * entry;
        struct stat statbuf;
        if(( dp=opendir(dir))==NULL)
        {
                fprintf(stderr,"cann't open ");
                return;
        }
        chdir (dir);
        while((entry=readdir(dp))!=NULL)
        {
        	   //int pos=telldir(dp);
           	//seekdir(dp,pos);
           	
                lstat(entry->d_name,&statbuf);//lstat 返回链接本身的信息，stat是链接指向的文件信息
                if(S_ISDIR(statbuf.st_mode))
                {
                        if(strcmp(".",entry->d_name)==0|| strcmp("..",entry->d_name)==0)
                                continue;

                        printf("%*s%s/\n",depth,"|",entry->d_name);//""
                        printdir(entry->d_name,depth+4);
                }
                else
                printf("%*s%s\n",depth,"|",entry->d_name);
        }
        chdir("..");
        closedir(dp);
}

int main()
{
printdir("/usr/local/jdk5",0);
exit(0);
}
//==示例 内存映射,一块内存可以两个或更多的程序读写
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/mman.h>
#include <fcntl.h>

typedef struct {
	int id;
	char str[24];

} RECORD;

#define NRECORDS (100)

int main() {
	RECORD record, *map;
	int i, f;
	FILE *fp;
	fp=fopen("records.dat", "w+");
	for (i=0; i<NRECORDS; i++) {
		record.id=i;
		sprintf(record.str, "RECORD%d", i);
		fwrite(&record, sizeof(record), 1, fp);

	}
	fclose(fp);

	fp=fopen("records.dat", "r+");
	fseek(fp, 43*sizeof(record), SEEK_SET);
	fread(&record, sizeof(record), 1, fp);
	record.id=143;
	sprintf(record.str, "RECORD%d", record.id);

	fseek(fp, 43*sizeof(record), SEEK_SET);
	fwrite(&record, sizeof(record), 1, fp);
	fclose(fp);

	///使用系统底层API不方便移值,返回文件描述符,fileno(fp)函数根据FILE*返回文件描述符
	f=open("redcords.dat", O_RDWR);//O_RDWR读写

	//sys/mman.h文件中 memory manipulate
	//第一个参数addr为NULL自动分配内存，MAP_SHARED修改写入磁盘,PROT_WRITE写权限,PROT=protect
	map=(RECORD*)mmap(NULL, NRECORDS*sizeof(record), PROT_READ|PROT_WRITE, MAP_SHARED, f, 0);

	if(map == MAP_FAILED)
	{
		char * reason=strerror(errno);//errno.h,string.h
		fprintf(stderr,"mmap error! %s\n",reason);
		return -1;
	}
	map[43].id=243;
	sprintf(map[43].str, "RECORD%d", map[43].id);
	msync((void*)map, NRECORDS*sizeof(record), MS_ASYNC);//MS_ASYNC异步写，MS=Memory_Sync
	munmap((void*)map, NRECORDS*sizeof(record));//memory unmap
	close(f);

	exit(0);
}
//---



编译加 -ldl
#include <dlfcn.h>
void * h=dlopen ("libegg.so", RTLD_LAZY);//加载so库
dlsym(h, "Egg_start"); //加载so库中的函数，返回指向函数的指针，void*来转
dlerror()//来得到dlsym调用错误

long long x;   //long long使用%lld格式化输出
unsigned long long x;
long long int x;
unsigned long long int x;

at least 64 bits wide Simply write long long int for a signed integer, or unsigned long long int for an unsigned integer

bzero和memset函数一样



-----国际化gettext-------------gettext_test.c---------OK
#include <stdio.h>
#include <locale.h>
#include <libintl.h> //intl= international

#define _(string) gettext(string)

int main (int argc, char *argv[])
{
	setlocale (LC_ALL, "");//LC_ALL全部的locale, LC_MESSAGES是本地语言的locale

	textdomain ("gettext_test");//告诉该程序到gettext_test.mo中去找其它语音的信息。 

	bindtextdomain ("gettext_test", "language");// 到当前目录中的language目录下去找 


	if(argc < 2)
	{
		printf(_("not enough parameter!\n"));
		printf(_("usage: %s username\n"), argv[0]);
		exit(1);
	}
    
  printf (_("welcome to world of gettext, %s!\n"), argv[1]);
  return 0;
}

xgettext --keyword=_ -o gettext_test.po gettext_test.c    生成gettext_test.po
	为
	msgid "usage: %s username\n" 
	msgstr "" ####写义中文不能少\n

mdkir -p language/zh_CN/LC_MESSAGES/

msgfmt -o language/zh_CN/LC_MESSAGES/gettext_test.mo gettext_test.po

mo==message object

------------- 正则表达式
#include <regex.h>
int regcomp(regex_t *preg, const char *pattern, int cflags);//编译,preg保存编译结果
int regexec(const regex_t *preg, const char *string, size_t nmatch, regmatch_t pmatch[], int eflags);
	//preg传regcomp的编译结果,把结果存放到pmatch中,最长为nmatch
	struct regmatch_t{
		//rm_so=reg match start offset
		regoff_t 	rm_so 	Byte offset from start of string to start of substring.
		regoff_t 	rm_eo 	Byte offset from start of string of the first character after the end of substring. 
	}
	返回 REG_NOMATCH 表示没有匹配结果
void regfree(regex_t *preg);//释放regcomp生成的结果

//-----
字符设备,不可以随机仿问,以字节为单位
块设备 ,可以随机仿问,以块为单位
独占设备,

//----串口编程
第一个串口的文件为/dev/ttyS0
#include <termios.h>
  struct termios {//至少要有
	tcflag_t  c_iflag     input modes
	tcflag_t  c_oflag     output modes
	tcflag_t  c_cflag     control modes
	tcflag_t  c_lflag     local modes
	cc_t      c_cc[NCCS]  control chars
  }

speed_t 波特率  的值都以B开头的数字

c_cflag的可用值 
	CREAD Enable receiver. 
	CLOCAL  Ignore modem status lines. //调制解调器
	CSIZE  Character size: 
	CSTOPB  Send two stop bits, else one. 
	HUPCL Hang up on last close. 
	PARENB Parity enable. //奇偶校
	PARODD  Odd parity, else even. //奇校
c_iflag	
	
int tcgetattr(int fildes, struct termios *termios_p);//取得参数
int tcsetattr(int fildes, int optional_actions, const struct termios *termios_p);

cfgetispeed
cfgetospeed
cfsetispeed
cfsetospeed


//------
--add.c

int global = 666;
int add(int a,int b)
{
  return (a + b);
}
//gcc -fPIC -shared calc.c -o libcalc.so

#include <stdio.h>
#include <dlfcn.h> 
//g++ -o test_calc test_calc.c  -l dl
int main()
{
  void *handle;
  char *error;
  typedef int (*FUNC)(int, int);
  FUNC func = NULL;
  //打开动态链接库
  handle = dlopen("./libcalc.so", RTLD_LAZY);
  //获取一个函数
  //*(void **) (&func) = dlsym(handle, "add");//dlsym返回void*,可以成功
  *((void **) (&func)) = dlsym(handle, "add");//就是func取地址，再强制为void ** 类型，再取值，再赋值(即先强转再赋值)

  char *perr = NULL;
  perr = dlerror();
  if(perr != NULL)
  {
	printf("dlsym add error:%s\n",perr);//一样的
	return 0;
  }

  printf("add: %d\n", (*func)(2,7));
  printf("add: %d\n", func(2,7));
   
  void *p = dlsym(handle, "global"); 
  printf("global is %d\n", *(int*)p);//强转为int*(不是int) 再取值*
     
  //关闭动态链接库
  dlclose(handle);
}

//------
//g++ -o test test.cpp mylib.cpp -DOS_LINUX　可以传到源码中的　#ifdef 　来使用


//export LD_LIBRARY_PATH=/usr/lib:$LD_LIBRARY_PATH　&& ./test 或者
// /lib64/ld-linux-x86-64.so.2 --library-path /usr/lib:. ./test

char* os="";
#ifdef OS_LINUX
	os="linux";
#else
	os="notlinux";
#endif
printf("os =%s\n",os);

//------

#include <sys/utsname.h>//uts=UNIX Time Sharing, UNIX分时操作系统
void pc_info(){
		uid_t uid=getuid();
		char * loiginUser=getlogin();
		printf("loiginUser=%s,uid=%d \n",loiginUser,uid);

		char computer[256];
		struct utsname uts;
		if(gethostname(computer, 255) != 0 || uname(&uts) < 0) {//uname(同命令unix name)在sys/utsname.h
			fprintf(stderr, "Could not get host information\n");
			exit(1);
		}
		printf("Computer host name is %s\n", computer);
		printf("System is %s on %s hardware\n", uts.sysname, uts.machine);//Linux on x86_64
		printf("Nodename is %s\n", uts.nodename);
		printf("Version is %s, %s\n", uts.release, uts.version);//kernel version
}
	
void print_error(int error_code){
		if(error_code == -1){
		   char * reason=strerror(errno);//errno.h,string.h
		   fprintf(stderr,"error! %s\n",reason);
		   exit(0);
		}
	}
void fs_cmd(){
		char * file="/tmp/test.txt";//必须是已经存在的
		int res=chmod(file,S_IRUSR|S_IWUSR|S_IRGRP);//mode同open
		print_error(res);

		res=chown(file,getuid(),getgid());
		print_error(res);

		res=link(file,"/tmp/link_test.txt");//是一个硬链接
		print_error(res);

		res=unlink("/tmp/link_test.txt");
		print_error(res);

		res=symlink(file,"/tmp/symlink_test.txt");//软链接
		print_error(res);

		res=unlink("/tmp/symlink_test.txt");
		print_error(res);

		res=mkdir("/tmp/empty_dir",S_IRUSR|S_IWUSR|S_IRGRP|S_IWGRP);//<sys/stat.h>
		print_error(res);

		res=chdir("/tmp/empty_dir");//cd 命令
		print_error(res);

		char current_idr[1024];
		getcwd(current_idr,sizeof(current_idr));//pwd命令，cwd=current work dir
		printf("current dir is =%s\n",current_idr);

		res=rmdir("/tmp/empty_dir");
		print_error(res);

	}
	void my_userinfo(){
		setpwent();//重定位到开头
		while(true){
			passwd * pw=getpwent();//ent=entry
			printf("name=%s, uid=%d, gid=%d, home=%s, shell=%s\n",
					pw->pw_name, pw->pw_uid, pw->pw_gid, pw->pw_dir, pw->pw_shell);
			if(pw->pw_uid== getuid()){
				printf("found myself\n");
				endpwent();//终止处理
				break;
			}
		}
		setuid(getuid());//对当前进程修改uid,getuid是真实的
		geteuid();//e=effective

	}
	int userinfo()
	{
		uid_t uid;
		gid_t gid;
		struct passwd *pw;
		uid = getuid();
		gid = getgid();
		printf("User is %s\n", getlogin());
		printf("User IDs: uid=%d, gid=%d\n", uid, gid);
		pw = getpwuid(uid);//<pwd.h>
		printf("UID passwd entry:\n name=%s, uid=%d, gid=%d, home=%s, shell=%s\n",
			pw->pw_name, pw->pw_uid, pw->pw_gid, pw->pw_dir, pw->pw_shell);
		pw = getpwnam("root");
		printf("root passwd entry:\n");
		printf("name=%s, uid=%d, gid=%d, home=%s, shell=%s\n",
			pw->pw_name, pw->pw_uid, pw->pw_gid, pw->pw_dir, pw->pw_shell);
	}
//-----limits.h
int the_limit()
{
	int int_max=INT_MAX; //<limits.h>
	int filename_max=NAME_MAX;//可能是特定系统的
	int filename_max2=pathconf("/tmp",_PC_NAME_MAX);
	int filename_max3=fpathconf(open("/tmp",O_RDONLY),_PC_NAME_MAX);// 参数是文件描述符

    struct rusage r_usage;
    struct rlimit r_limit;
    int priority;
    work();
    getrusage(RUSAGE_SELF, &r_usage);//r=resource,除了 RUSAGE_SELF　还有 RUSAGE_CHILDREN
    printf("CPU usage: User = %ld.%06ld, System = %ld.%06ld\n",
        r_usage.ru_utime.tv_sec, r_usage.ru_utime.tv_usec,
        r_usage.ru_stime.tv_sec, r_usage.ru_stime.tv_usec);

	//优先级范围-20到19,-20最高,19最低(nice,renice命令ps -el 有PRI列)，getpriority返回错误的-1重合,要使用errno判断才行
    priority = getpriority(PRIO_PROCESS, getpid());//还有　PRIO_PGRP 进程组,　PRIO_USER
    printf("Current priority = %d\n", priority);

    getrlimit(RLIMIT_FSIZE, &r_limit);//FSIZE=file size ,还有RLIMIT_NOFILE 打开文件数
    printf("Current FSIZE limit: soft = %ld, hard = %ld\n",
        r_limit.rlim_cur, r_limit.rlim_max);

    r_limit.rlim_cur = 2048;//软限制
    r_limit.rlim_max = 4096;//硬限制，可用ulimit命令
    printf("Setting a 2K file size limit\n");
    setrlimit(RLIMIT_FSIZE, &r_limit);
    work();
}

 printf("Compiled: " __DATE__ " at " __TIME__ "\n");
 printf("This is line %d of file %s\n", __LINE__, __FILE__);
 printf("function is: %s \n", __FUNCTION__ );
 assert(1+1==2); //<assert.h>

//------
可以通过 sizeof(指针) , 来得到程序是以多少位编译的
linux 操作系统32/64位,CPU是32/64位 如何判断???


====================kernel


--------编译成功示例
--- example.c
//#include <linux/init.h>
#include <linux/module.h> //这个里面有引入   <linux/init.h>
#include <linux/kernel.h>

MODULE_LICENSE("GPL");//版权信息,不加编译报错
MODULE_AUTHOR("abc");
MODULE_DESCRIPTION("A simple example Linux module.");
MODULE_VERSION("0.01");

//static 和 __init
static int __init  example_init(void) {
	printk(KERN_INFO "Hello, World!\n");//printk 函数, 参数间不带逗号,KERN_INFO 也有 KERN_DEBUG
	return 0;
}
//static 和 __exit
static void __exit  example_exit(void) {
	printk(KERN_INFO "Goodbye, World!\n");
}

module_init(example_init);//被始使用,内核只执行一次
module_exit(example_exit);//内核在称除时调用一次,回收空间
 
--- Makefile

obj-m += example.o
#obj-m ?= example.o
#example.c生成example.o后,再生成 example.ko

all:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) modules
	#-C 执行任何这前先进入这个目录 ,是一个链接到 /usr/src/kernels/$(shell uname -r)
	#M=
	#modules是make命令的目标(target)参数
clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean

--------

make 后生成文件有 example.o,example.mod.c,example.mod.o,example.ko,example.mod

sudo insmod example.ko #安装模块以.ko结尾的实际文件 
lsmod  | grep example  #也在最上面,也可以用 cat /proc/modules  | grep example,生成 /sys/module/example/ 目录

dmesg 可以看到代码的日志 
sudo rmmod  example  #卸载模块是名字,不以.ko结尾, 也有日志 






ls -l /dev/ c开头字符设置,b开头块设备
格式mknode name c/b 主版本  次版本,如 mknod /dev/mylp0 c 6 0

kernel-devel 软件包
ls /usr/src/kernels/$(uname -r)/include

内核编程下 
#include  <linux/module.h> 引用的是 /usr/src/kernels/$(uname -r)/include/linux/module.h 不是 /usr/include/linux/module.h
#include  <linux/kernel.h>	引用的是 /usr/src/kernels/$(uname -r)/include/linux/kernel.h

/usr/src/kernels/$(uname -r)include/
/usr/src/kernels/$(uname -r)arch/x86/include/  下有 asm/linkage.h 
/usr/src/kernels/$(uname -r)/arch/x86/include/generated/  下有asm/rwonce.h    有使用asm-generic/

也有
/usr/src/kernels/$(uname -r)/include/asm-generic/rwonce.h 

/usr/src/kernels/$(uname -r)/include/asm-generic/

#include  <linux/module.h> 
#include <linux/types.h>
#include <asm/posix_types.h>

//在32位系统和64位系统中数据类型(如int)长度是不一样的
__u8 len=0; //定义在 <linux/types.h> 或  <asm/posix_types.h>
__u32 size=0;//长度是固定的,不同平台做对应的

//KERN_DEBUG在<linux/kern_levels.h>里定义





