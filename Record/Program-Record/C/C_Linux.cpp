http://pubs.opengroup.org/onlinepubs/007908775/  linux C 参考


http://www.linuxforum.net/books/
http://www.linuxforum.net/books/upfaq/book1.htm   问答 教程

http://book.51cto.com/art/200806/76015.htm		Linux企业集群		全
http://book.51cto.com/art/200910/158658.htm		嵌入式Linux基础教程 在线书 部分
http://book.51cto.com/art/200707/50861.htm		Linux服务器安全策略详解	 部分
http://book.51cto.com/art/200910/159525.htm		Luence

http://www.linuxforum.net/books/automake.html 
 

MySQL 源码用的boost C++ libraries 1.59 , 二进制文件名使用glibc  

SUNStuio基于Netbeans的
Anjuta
Visual Studio Code-1.26  for linux x64
eclipse CDT
CLion 基于Intellij Idea 


gcc -o libxxxx.so -shared -fPIC a.o b.o (生成动态链接库)
使用时和静态库一样,加在一起编译,要加头文件的,文件名用全名就可以了(不用少前缀lib,不用少后缀dll)
C_INCLUDE_PATH=c:\MinGW\include
CPLUS_INCLUDE_PATH=c:\MinGW\include\c++\3.4.5;c:\MinGW\include\c++\3.4.5\mingw32;c:\MinGW\include\c++\3.4.5\backward;c:\MinGW\include
LD_LIBRARY_PATH=c:\MinGW\lib
LIBRARY_PATH=c:\MinGW\lib
CLASSPATH=.;
PATH=

ranlib == ar s   建索引 /// ar crs   (create replace  symbol) xx.a yy.o


windows(MinGW) 下dll转换成.a
	1.pexports libsqlapi.dll > libsqlapi.def
	2.dlltool --input-def libsqlapi.def --dllname libsqlapi.dll --output-lib libsqlapi.a -k 

	g++ -shared   -o xxx.dll yyy.cpp 来生成dll文件


--------mingw-get-inst-20111118   选择gcc,g++自动带gdb 

set PATH=C:\MinGW\bin
set LIBRARY_PATH=C:\MinGW\lib
set C_INCLUDEDE_PATH=C:\MinGW\include
set CPLUS_INCLUDE_PATH=C:\MinGW\lib\gcc\mingw32\4.6.1\include;C:\MinGW\include
以下是CDT 自动识别的
C:\MinGW\lib\gcc\mingw32\4.6.1\include\c++\mingw32
C:\MinGW\lib\gcc\mingw32\4.6.1\include\c++
C:\MinGW\lib\gcc\mingw32\4.6.1\include\c++\backward
C:\MinGW\lib\gcc\mingw32\4.6.1\include-fixed


复制mingw32-make.exe 为 make.exe 就可以了！

 

.cpp文件要用g++,而不能用gcc


/* 如果使用的是非GNU C, 那么就忽略__attribute__ */
　　#ifndef __GNUC__
　　# define __attribute__(x) /*NOTHING*/
　　#endif

_attribute__适用于函数的声明而不是函数的定义

__asm__		代替 asm 
__const__	代替 const, 
__inline__	代替 inline.



======================Linux C
#include<unistd.h>
#include<stdio.h>
void testExeclp()
{
	//函数名l=link ,v=vector,e=enviroment,p=path ,表示参数
	//其它函数 execl,execv,execle,execve,execlp,execvp ,出错返回-1,设置 errno
	if(execlp("ps","ps","-ef",0)<0)// 到PATH中找ps,要替代新的进程,没有建立新进程,ID号相同 ,后面的代码不会执行
		perror("execlp error !");
}
void testExecle()
{
	char *envp[]={"PATH=/home/temp/bin","USER=zh"};
	if(execle("/usr/bin/env","env",NULL,envp)<0)//自己设置环境变量
		perror("execle error !");
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
		printf("当前进程 PID is %d\n",getpid());
		sleep(1);//切换进程,防止优先级高的一直执行下去
	}
	if(pid!=0) //父进程来执行
	{
		int stat_val;
		pid_t child_pid;
		child_pid=wait(&stat_val);//父进程来等侍子进程,状态信息存在stat_val里面
		printf("child has finished :PID=%d\n",child_pid);
		if(WIFEXITED(stat_val))//是否正常退出
			printf("child exited with code=%d",WEXITSTATUS(stat_val));//显示状态
		 else
			printf("child RUN ERROR EXIT");
	}
	exit(exit_code);
	//当子进程执行完成时,不会立即退出,父进程可以会从子进程读信息,(这个子进程是一个僵进程ps 显示<defunct>)
}

vfork();//不从父进程复制,与父共享,修改时是CopyOnWrite的做法


int pipe(int fildes[2]);//建立一个管道,两个文件描述符,fildes[0]是用来读的文件描述符,而fildes[1]是用来写的文件描.返回 0 成功,-1失败,是在内存中的
//errno,的值EMFILE文件描述符已经在这个进程中打开 ,ENFILE 超过系统最大打开数
//可打开两管道,实现双向

FILE *pf=popen(const char *command, const char *mode);//stdio.h 打开管道,mode可为r或w
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
int mkfifo(const char *path, mode_t mode); //命名管道,是一种文件,专为某进程使用,可以使用open函数打开
	open函数的选项 
errno如为EEXIST表示文件已经存在	

const char * FIFO ="/tmp/myfifo";
if((mkfifo(FIFO,O_CREAT|O_EXCL)<0)&& (errno!=EEXIST))//open函数的选项 ,O_EXEC：如果使用了O_CREAT 而且文件已经存在,就会发生一个错误.
	printf("can not create fifoserver\n");
fd=open(FIFO,O_RDONLY|O_NONBLOCK,0);//O_WRONLY
unlink(FIFO);

---信号
#include <signal.h>
SIGINT  来自ctrl+c的信号
SIGTERM 来自kill -[n]
SIGHUP 终端发出的结束信号

两个信号不能被忽略SIGKILL和SIGSTOP
每一个信号有一个缺省的动作

kill(pid_t pid,int signo);//发送信号
raise(int signo);//向自身进程发信号,等同 pthread_kill(pthread_self(), sig);

alarm(int seconds);//时间到了后会发出SIGALRM信号,如参数为0取消以前的定时器,每个进程只能有一个定时器
pause,挂起,直到捕捉到一个信号

--信号集
sigemptyset(sigset_t * set);//初始化为空
sigfillset (sigset_t * set);//初始化为所有信号
sigaddset(sigset_t* set ,int signo);
sigdelset(sigset_t* set ,int signo);
sigismember(const sigset_t * set,int signo);//是否是成员


int sigprocmask(int how, const sigset_t *set, sigset_t *oset); 告诉内核不允许发生信号集中的信号
	oset非空,返回进程当前屏蔽信号,
	how :SIG_BLOCK ,和set中的并集
		SIG_UNBLOCK,和set中的交集
		SIG_SETMASK,新的信号屏蔽是set指向的


int sigaction(int sig, const struct sigaction *act,  struct sigaction *oact);//用于取代早期的 signal函数
	指定信号使用act来处理
	oact返回老的参数
	struct sigaction
	{
		void(*sa_handler) (int) ,//SIG_DFL, SIG_IGN or 或者自己的函数,同singal.
		sigset_t 	sa_mask ,//在执行信号处理函数,在有哪些信号时block住
		int 	sa_flags ,
		void(*sa_sigaction) (int, siginfo_t *, void *)//和sa_handler功能重叠,两个不能同时使用
	}
	sa_flags:  
	SA_NODEFER 不阻塞此信号
	SA_NOCLDSTOP 当子进程结束时,不要生成SIGCHLD 信号
	SA_RESTART
	SA_RESETHAND
 
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
void syslog(int priority, const char *message,...);
	priority :LOG_ERR,LOG_WARNING,LOG_NOTICE,LOG_INFO,LOG_DEBUG
void closelog(void);
 
//--共享内存,所有的进程都可以看到,映射的进程可以使用
#include <sys/shm.h>
int shmget(key_t key, size_t size, int shmflg); // 建立享内存,成功返回内存标识
	key 设置为 IPC_PRIVATE. 
errno 
	EINVAL 无效内存大小
	ENOENT 内存段不存在
	EACCES	无权限
void *shmat(int shmid, const void *shmaddr, int shmflg);  //attache 映射,返回内存地址
	shmflg:SHM_RND
int shmdt(const void *shmaddr); //deattach

ipcs命令

//--消息队列
#include <sys/msg.h>
int msgget(key_t key, int msgflg);//建立新的消息队列,返回id 
	key 要设置为 IPC_PRIVATE. 
	msgflg: IPC_CREAT
int msgsnd(int msqid, const void *msgp, size_t msgsz, int msgflg);
	struct mymsg {
		long int    mtype;       /* message type */
		char        mtext[1];    /* message text */
	}
	IPC_NOWAIT
ssize_t msgrcv(int msqid, void *msgp, size_t msgsz, long int msgtyp,int msgflg);
	 msgtyp=0,只接收第一个,>0是表示 第一个与mtype相等的 ,<0时表示,第一个小于绝对值的
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

	
#include <sys/ipc.h>
key_t ftok(const char *path, int id);	//generate an IPC key 

//--信号量
#include <semaphore.h>













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
  



ar crs xx.a xx.o xxx.o       //生成静态库 archive
gcc -shared -fPIC -o libxx.so xx.o xx.o
cpp 命令 同 gcc -E   预处理后停止，不编译　//gcc -E hello.c >hello01.c 或者用 cpp hello.c >hello01.c ,只展开#开头的
lint 废弃了
-g 生成调试信息
-S 生成汇编文件以.s结尾	//gcc -S hello.c ##-o hello.s可不用-o
-Os 优化大小
as 将.s -> .o　　// as hello.s  -o hello.o


ld 把所有的.o 和 .a 文件链接成 可执行文件 //  -lc libc.so 
ld -m elf_i386 -dynamic-linker  /lib/ld-linux.so.2  -o hello hello.o -lc   //cannot find entry symbol _start
用 ld --verbose 或 -V 选项 列出 有效的 模拟项

file hello  //Executable and Linking Format (ELF),Linux Standard Base (LSB)
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
nm /home/zh/eclipse_workspace/SynDB/egg_lib/libegg.so   来看so文件中有哪些方法
nm 可查看目标文件和程序库的符号地址 //有哪些方法   nm hello.o


gdb命令是linux的c的 debug环境
用gdb 必须在g++ 是用-g 选项
	>pwd 	//看当前目录
	>file xxxpro (要debug的程序)
	>breake xx.c:20//设断点xx.c的20行  ,也可只 20
	>run
	>print i		//显示i变量值
	>po(print-object) xx
	>n(next)		//(不进函数内部)
	>step		//单步执行(进函数内部)
	>finish 	//从方法中出去
	>c(continue) //运行到下个断点
	>list		//附近代码
	>l(list) 10	//10行附近代码
	>set variable p=0	//改变变量p的值为0
	>frame			//打印当前帧,光标所要执行的位置
	>bt(backtrace)		//显示当前在哪个文件的多少行，哪个函数
	>info break		//查看所有断点,有号
	>delete 1	// 删第一个断点
	>break 21 if value==div //条件断点
	>condition 1 value==div //对已经设置的断点号,设置停止条件
	>info locals 		//显示所有的当前的局部变量
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
	linux下都有的

#define PI 3.14

#undef PI 表示后方PI无效
 
---------------
socket   https://www6.software.ibm.com/developerworks/cn/education/linux/l-sock/tutorial/l-sock-3-2.html
//Fedora 9 上执行 OK)(TCP) ,solaris 上不可以编译成功
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
	struct sockaddr_in echoserver;
	char buffer[BUFFSIZE];
	unsigned int echolen;
	int received = 0;

	if (argc != 4)
	{
		fprintf(stderr, "USAGE: TCPecho <server_ip> <word> <port>\n");
		exit(1);
	}
	/* Create the TCP socket */
	if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
	//	PF_INET 只是意味着它使用 IP（您将总是使用它）； SOCK_STREAM 和 IPPROTO_TCP 配合用于创建 TCP socket	
	//创建失败，它将返回 -1
	{
		Die("Failed to create socket");
	}

	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	echoserver.sin_addr.s_addr = inet_addr(argv[1]); /* IP address */
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
//Fedora 9 上执行 OK)(TCP)
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
	echoserver.sin_addr.s_addr = htonl(INADDR_ANY); /* Incoming addr */
	echoserver.sin_port = htons(atoi(argv[1])); /* server port */

	/* Bind the server socket */
	if (bind(serversock, (struct sockaddr *) &echoserver, sizeof(echoserver))
			< 0)
	{
		Die("Failed to bind the server socket");
	}
	/* Listen on the server socket */
	if (listen(serversock, MAXPENDING) < 0)
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
       



----client-(UPD)----//Fedora 9 上OK

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

	/* Create the UDP socket */
	if ((sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0)
	{
		Die("Failed to create socket");
	}
	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	echoserver.sin_addr.s_addr = inet_addr(argv[1]); /* IP address */
	echoserver.sin_port = htons(atoi(argv[3])); /* server port */
	/* Send the word to the server */
	echolen = strlen(argv[2]);
	if (sendto(sock, argv[2], echolen, 0, (struct sockaddr *) &echoserver,
			sizeof(echoserver)) != echolen)
	{
		Die("Mismatch in number of sent bytes");
	}

	/* Receive the word back from the server */
	fprintf(stdout, "Received: ");
	clientlen = sizeof(echoclient);
	if ((received = recvfrom(sock, buffer, BUFFSIZE, 0,
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


-------UDP server ----//Fedora 9 上OK


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
	unsigned int echolen, clientlen, serverlen;
	int received = 0;

	if (argc != 2)
	{
		fprintf(stderr, "USAGE: %s <port>\n", argv[0]);
		exit(1);
	}

	/* Create the UDP socket */
	if ((sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0)
	{
		Die("Failed to create socket");
	}
	/* Construct the server sockaddr_in structure */
	memset(&echoserver, 0, sizeof(echoserver)); /* Clear struct */
	echoserver.sin_family = AF_INET; /* Internet/IP */
	echoserver.sin_addr.s_addr = htonl(INADDR_ANY); /* Any IP address */
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
		if ((received = recvfrom(sock, buffer, BUFFSIZE, 0,
				(struct sockaddr *) &echoclient, &clientlen)) < 0)
		{
			Die("Failed to receive message");
		}
		fprintf(stderr, "Client connected: %s\n",
				inet_ntoa(echoclient.sin_addr));
		/* Send the message back to client */
		if (sendto(sock, buffer, received, 0, (struct sockaddr *) &echoclient,
				sizeof(echoclient)) != received)
		{
			Die("Mismatch in number of echo'd bytes");
		}
	}
}


-------DNS//fedora 9 OK
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

 g++ -o pthread pthread.cpp -lpthread

pthread_create(&id, NULL, thread, NULL);
第一个参数为指向线程标识符的指针，第二个参数用来设置线程属性，第三个参数是线程运行函数的起始地址，最后一个参数是运行函数的参数
成功时，函数返回0
pthread_join(..)用来等待一个线程的结束,
第一个参数为被等待的线程标识符，第二个参数为一个用户定义的指针，它可以用来存储被等待线程的返回值

pthread_exit(NULL)


pthread_create函数的第二个参数
pthread_attr_t，
pthread_attr_init，这个函数必须在pthread_create函数之前调 
线程绑定状态的函数为 pthread_attr_setscope()第一个是指向属性结构的指针，
	第二个是绑定类型  PTHREAD_SCOPE_SYSTEM（绑定的,被绑定的线程具有较高的响应速度，）和PTHREAD_SCOPE_PROCESS（非绑定的


线程以什么样的方式来终止自己(默认属性，即为非分离状态)
pthread_attr_setdetachstate（pthread_attr_t *attr, int detachstate）。第二个参数可选为PTHREAD_CREATE_DETACHED（分离线程）和 PTHREAD _CREATE_JOINABLE（非分离线程）

一个线程为分离线程，而这个线程运行又非常快，它很可能在 pthread_create函数返回之前就终止了 ,
调用 pthread_cond_timewait函数//是 pthread_cond_timedwait  , pthread_cond_wait



优先级，它存放在结构sched_param中(成员 int sched_priority)。用函数pthread_attr_getschedparam和函数 pthread_attr_setschedparam进行存

//先取优先级，对取得的值修改后再存放回去
pthread_attr_t attr;
pthread_t tid;
sched_param param;
int newprio=20;

pthread_attr_init(&attr);
pthread_attr_getschedparam(&attr, &param);
param.sched_priority=newprio;
pthread_attr_setschedparam(&attr, &param);
pthread_create(&tid, &attr, (void *)myfunction, myarg);


在[ 进程 ]中共享的变量必须用关键字volatile来定
pthread_once_t once= PTHREAD_ONCE_INIT;

//它对线程外部的其它线程是不可见的
pthread_key_create (pthread_key_t  ,)第二个参,那么当每个线程结束时，系统将调用这个函数来释放绑定在这个键上的内存块。这个函数常和函数pthread_once 
pthread_once (pthread_once_t, 函数) //第一次调用pthread_once时它执行这个函数，以后的调用将被它忽略。
pthread_setpecific(pthread_key_t,x  ) //线程数据和一个键绑定在一起
pthread_getspecific(pthread_key_t key)

pthread_key_delete用来删除一个键，这个键占用的内存将被释放，但同样要注意的是，它只释放键占用的内存，并不释放该键关联的线程数据所占用的内存资源，而且它也不会触发函数pthread_key_create中定义的destructor函数

pthread_mutex_t mutex;
　/* 用默认属性初始化一个互斥锁对象*/
　pthread_mutex_init (&mutex,NULL);
特定属性的互斥锁，须调用函数pthread_mutexattr_init。函数

pthread_mutexattr_setpshared(  //PTHREAD_PROCESS_PRIVATE和PTHREAD_PROCESS_SHARED
pthread_mutexattr_settype(    //PTHREAD_MUTEX_NORMAL、PTHREAD_MUTEX_ERRORCHECK、PTHREAD_MUTEX_RECURSIVE和PTHREAD_MUTEX_DEFAULT


pthread_mutex_lock(&mutex);
pthread_mutex_unlock(&mutex);

struct timespec-> tv_sec 
struct timespec-> tv_nsec (Nano seconds)

函数pthread_mutex_trylock(

pthread_cond_t，函数pthread_cond_init（）
pthread_condattr_t

pthread_cond_destroy（pthread_cond_t cond）
pthread_cond_wait(

pthread_cond_signal和函数pthread_cond_broadcast唤醒

/usr/include/semaphore.h中定 sem 开头的
sem_init(sem_t )
sem_post（）//增加信号量,会使其中的一个线程不在阻塞
sem_wait（）//减少信号量,
sem_trywait（）和函数pthread_ mutex_trylock（）起同样的作用
sem_destroy(sem_t *sem)


sem_wait( sem_t *sem )被用来阻塞当前线程直到信号量sem的值大于0，解除阻塞后将sem的值减一，表明公共资源经使用后减少。
sem_trywait ( sem_t *sem )是函数sem_wait（）的非阻塞版本，它直接将信号量sem的值减一。



<unistd.h> 中定义了sleep()和 usleep()微秒 microseconds
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

if(WIFSIGNALED(status))
WTERMSIG(status)


 
#include <signal.h>;  //有kill定义
if(kill(getppid(),SIGTERM)==-1)//失败
  
#include <unistd.h>;
#include <sys/types.h>;
#include <sys/stat.h>;

#include <fcntl.h> //file control
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
	-------
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

int fcntl(int fildes, int cmd, ...);
	F_DUPFD,复制文件描述符
	F_GETFD,F_SETFD//file description
	F_GETFL,F_SETFL//flag ,可修改的
	F_GETLK,F_SETLK,F_SETLKW//lock wait
	
	struct flock //文件加锁
	{
		short l_type   //F_RDLCK, F_WRLCK, F_UNLCK
		short l_whence //SEEK_SET, SEEK_CUR and SEEK_END
		off_t l_start  relative offset in bytes
		off_t l_len    size; if 0 then until EOF
		pid_t l_pid    process ID of the process holding the lock; returned with F_GETLK
	}
	//在文件中部分区域加锁l_whence,l_start,l_len,
	
	
	
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
stat 用来判断没有打开的文件,而fstat 用来判断打开的文

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

int dup2(int oldfd,int newfd);  //所有向newfd 操作都转到oldfd 上面

#include <sys/ioctl.h>
int ioctl(int fildes, int request, ... /* arg */);

#include <sys/time.h>
int select(int nfds, fd_set *readfds, fd_set *writefds,  fd_set *errorfds, struct timeval *timeout);
//fd_set 描述符集函数
void FD_CLR(int fd, fd_set *fdset);
int FD_ISSET(int fd, fd_set *fdset);
void FD_SET(int fd, fd_set *fdset);
void FD_ZERO(fd_set *fdset);




 
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
                lstat(entry->d_name,&statbuf);
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
//==示例 动态内存
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

	f=open("redcords.dat", O_RDWR);
	map=(RECORD*)mmap(0, NRECORDS*sizeof(record), PROT_READ|PROT_WRITE, MAP_SHARED, f, 0);

	map[43].id=243; //不能仿问？？
	sprintf(map[43].str, "RECORD%d", map[43].id);
	msync((void*)map, NRECORDS*sizeof(record), MS_ASYNC);
	munmap((void*)map, NRECORDS*sizeof(record));
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
#include <libintl.h>

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
-------------

//---正则表达式
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
可以通过 sizeof(指针) , 来得到程序是以多少位编译的
linux 操作系统32/64位,CPU是32/64位 如何判断???