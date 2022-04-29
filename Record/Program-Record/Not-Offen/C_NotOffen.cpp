


---------------------autoconf,automake
也叫Autotools,比cmake难用多了

(自动化工具)automake ,autoconf ,m4宏,libtool

1.autoscan 命令,多了一个configure.scan文件,
	#######AC_CONFIG_HEADER([config.h])修改为：AM_CONFIG_HEADER(config.h),######这行是不能在autoconf时改的，要在automake是改
	在AC_INIT宏之后加入AM_INIT_AUTOMAKE(hello, 1.0)，hello是你的软件名称，1.0是版本号,
	最后一行AC_OUTPUT宏填写完整变成AC_OUTPUT(Makefile)，表明autoconf和automake最终将生成Makefile文件,把configure.scan文件改名为configure.in
AC_OUTPUT([Makefile
                src/lib/Makefile
                src/ModuleA/apple/core/Makefile
                src/ModuleA/apple/shell/Makefile
                ])


“config.h.in” is created by `autoheader	#####AC_CONFIG_HEADERS

4) 在project目录下新建NEWS、 README、 ChangeLog 、AUTHORS,INSTALL,COPYING文件  可以用automake --help来看,
5) 将/usr/share/automake-1.X/目录下的depcomp和complie文件拷贝到本目录下

2.aclocal命令，ls会发现多了一个aclocal.m4
3.autoconf命令，ls将发现生成了一个可执行的configure
4.编辑一个Makefile.am文件
	AUTOMAKE_OPTIONS=foreign
	bin_PROGRAMS=hello
	hello_SOURCES=main.c ....
5. automake --add-missing 命令   就会有depcomp文件 #####--copy
6. ./configure命令



automake选项
 -a, --add-missing      add missing standard files to package
      --libdir=DIR       directory storing library files


在configure.in中  (后来新版本改为configure.ac，但configure.in也可以)
	AC_PROG_CC：选择 C 编译器。如果在环境中不设置 CC 的话，则检测 gcc。
	AC_PROG_CXX：选择 C++ 编译器。

	看后有AC_CHECK_HEADERS([fcntl.h limits.h ...
		AC_CHECK_FUNCS([bzero floor 
	AC_OUTPUT(最后要生成的Makefile , 包括 子目录中的，中间用空格隔开) ,
	    例如 AC_OUTPUT(Makefile  subdir/Makefile subdir1/Makefile)

	如果在configure.ac中定义了一些特殊的宏，比如AC_PROG_LIBTOOL，它会调用libtoolize


	在 configure.in 中，有一些被 autoconf 宏预先定义的变量，重要的有如下几个：
	    bindir：安装可执行文件的目录。
	    includedir：C 头文件目录。
	    infodir：info 页安装目录。
	    mandir：安装手册页的目录。
	    sbindir：为管理员运行该该程序提供的安装路径。
	    srcdir：为 Makefile 提供的源代码路径。
	    top_srcdir：源代码的最上层目录。
	    LIBS：给连接程序的 -l 选项
	    LDFLAGS：给连接程序的 stripping（-s）和其他一些选项。
	    DEFS：给 C 编译器的 -D 选项。
	    CFLAGS：给 C 编译器的 debug 和优化选项。当调用了 AC_PROG_CC 才有效。
	    CPPFLAGS：头文件搜索路径（-I)和给 C 预处理器和编译器的其他选项。
	    CXXFLAGS：给 C++ 编译器的 debug 和优化选项。当调用了 AC_PROG_CXX 才有效。

	同一个目录下编译多个程序的话， AC_CONFIG_SUBDIRS(DIR....)



Makefile.am中可用的全局变量
	
	LDADD    所有的库
	LDFLAGS 所有的选项
	EXTRA_DIST
	SUBDIRS 
	INCLUDES = -I/include
	LIBS = -lm -lcrypt

	
Some variables are inherited from Autoconf; these are CC, CFLAGS, CPPFLAGS, DEFS,LDFLAGS, and LIBS.


Makefile.am含义
	AUTOMAKE_OPTIONS  否存在标准GNU软件包中应具备的各种文件，例如AUTHORS、ChangeLog、NEWS等文件。我们将其设置成foreign时，
	bin_PROGRAMS	要产生的可执行文件的文件名。多个用空格隔开。
	helloworld_SOURCES  产生"helloworld"时所需要的源代码。多个源文件用空格隔开
		如果你在bin_PROGRAMS定义了多个可执行文件，则对应每个可执行文件都要定义相对的filename_SOURCES。
      
	AUTOMAKE_OPTIONS=foreign 就不会提示少了文件README NEWS AUTHORS ChangeLog,不加报错时会自动生成COPYING,INSTALL两文件 
		  foreign:Automake 将只检查绝对必须的东西.
		    gnu:Automake 将尽可能多的检查以适应GNU标准, 这是默认项.

	


	xxx=$(shell /bin/pwd)

       SUBDIRS  = dir1   这个目录没有要编译的文件 ，只包含了子目录，则只写个
	 EXTRA_DIST= \
	  a.h \
	  b.h
	 # 不用编成.o，但生成target myprogram也需要给编译器处理的头文件放这里

	myprogram_LDADD = libsub1.a 这个_LDADD是关键字，
	 # 最后生成myprogram这个执行文件，还要link src/sub1这个目录中的内容编成的一个lib :libsub1.a，
	 
	 myprogram_LDFLAGS = -lpthread -lglib-2.0 -L/usr/bin $(all_libraries)
	 # myprogram还要link系统中的动态so，以此类推，需要连自编译的so,也写到这个关键字 _LDFLAGS后面就好了。
	  
	 AM_CXXFLAGS = -D_LINUX
	 # 传递给g++编译器的一些编译宏定义，选项，

	 INCLUDES=-IPassport -Isub1/ -I/usr/include/glib-2.0\
	    -I/usr/lib/glib-2.0/include $(all_includes)
	 #  传递给编译器的头文件路径。


	noinst_LIBRARIES = libprotocol.a
	# 不是生成可执行文件，而是静态库，target用noinst_LIBRARIES
	 libprotocol_a_SOURCES = \
				 alib.cpp
	“noinst”前缀专门指定有问题的对象不被安装。
	“check”前缀表示有问题的对象知道make check命令被执行猜被创建
	 可用的主变量是 'PROGRAMS','LIBRARIES','LISP','SCRIPTS','DATA','HEADERS','MANS'和'TEXINFOS'

'##'开头的行将被Automake 完全忽略.
	xs = a.c b.c
    foo_SOURCES = c.c $(xs)
    它将使用文件：'a.c','b.c'和'c.c' 作为foo_SOURCES的内容.

EXTRA_PROGRAMS

$(prefix)
$(top_srcdir)



 Makefile.in是由automake生成的

config.h is created by `configure` from “config.h.in”

如修改源码autoscan ,autoreconf

 
autoheader命令是autoconf软件包中的



生成的make目标
make clean
make dist 	产生发布软件包文件(即distribution)打包成一个PACKAGE-VERSION.tar.gz压缩的   不会所config.log等打进去
make distcheck  自动把压缩包文件解开，然后执行configure命令，并且执行make，来确认编译不出现错误
make distclean　　类似make clean，但同时也将configure生成的文件全部删除掉，包括Makefile。 

 
=============================SQLAPI==============
SQL API++
	MinGW用libsqlapi.dll
	linux上要libdl.so
	
	
(const char*)cmd.Field("fvarchar20").asString());   SAString 强转成 const char*



SAString cc ;
cc.GetLength();
cc.format("xx%d",1);

typedef void *HANDLE;


SAConnection con; 
SACommand cmd;
con.Connect("192.168.1.25@asss", "db2", "db2", SA_DB2_Client);
cmd.setConnection(&con);
cmd.setCommandText("Create table test_tbl(fid integer, fvarchar20 varchar(20), fblob blob)");
cmd.Execute();
cmd.setCommandText("Insert into test_tbl(fid, fvarchar20) values (:1, :2)");
cmd.Param(1).setAsLong() = 2;
cmd.Param(2).setAsString() = "Some string (2)";
cmd.Execute();
cmd << (long)3 << "Some string (3)";//另一种插入一行的方式
cmd.Execute();
con.Commit();//con.Rollback();
SAException &x
	(const char*)x.ErrText()



SACommand cmd(&con,"Select fid, fvarchar20 from test_tbl");
cmd.Execute();
while(cmd.FetchNext())
{
	printf("Row fetched: fid = %ld, fvarchar20 = '%s'\n", cmd.Field("fid").asLong(),(const char*)cmd.Field("fvarchar20").asString());
}


//Update test_tbl set fblob = :fblob where fid =:1
cmd.Param("fblob").setAsBLob()== xxfunction("xxparam");//xxfunction 可返回是SAString,(stdio.h 中的FILE)
	SAException::throwUserException(-1,"error message");
	SAString 可用+=
	SAString(sBuf, size);//sBuf是char* [] 里有字串,

cmd.Param("fblob").setAsBLob(
			FromFileWriter,	//哪个函数名
			10*1024,		// 每个piece多大
			(void*)"C:\\sqlapi\\samples\\test.doc");	//other

unsigned int FromFileWriter(   //返回数据长度
				SAPieceType_t &x,   //piece_type
				void *pBuf,//返回数据内容
				unsigned int nLen,//piece长度
				void *pAddlData)//other


x == SA_FirstPiece//只对第一次调用打开文件,SA_LastPiece
	FILE *pFile =fopen(sFilename, "rb");
int nRead = fread(pBuf, 1, nLen, pFile);






SAString s = cmd.Field("fblob").asBLob();
cmd.Field("fblob").setLongOrLobReaderMode(SA_LongOrLobReaderManual);

if(!cmd.Field("fblob").isNull())
	cmd.Field("fblob").ReadLongOrLob(
					IntoFileReader,//哪个函数名
					10*1024,		// 每个piece多大
					(void*)(const char*)sFilename	// other
					);

void IntoFileReader(
		SAPieceType_t ePieceType,
		void *pBuf,    //当前传来的数据
		unsigned int nLen,//当前传来数据的长度
		unsigned int nBlobSize,//共传了多长的数据
		void *pAddlData)//other
	{

	fwrite(pBuf, 1, nLen, pFile);




#include <conio.h> //getch();
#include <process.h>
typedef void *HANDLE; //MinGW

SAClient_t是一个enum 有各种数据库

HANDLE hConsoleOut=GetStdHandle(STD_OUTPUT_HANDLE);//得到控制台
char [10] buf
sprintf(buf,"xx")//向数据中填值
SetConsoleTitle(buf)　　//修改控制的标题
HANDLE hQueryMutex = CreateMutex(NULL, FALSE, NULL); //---- if(GetLastError()==ERROR_ALREADY_EXISTS) ,不允许同时运行两个相同的程序(互斥)
CloseHandle(hConsoleOut);//关闭控制台

WaitForSingleObject(hQueryMutex, 75L) == WAIT_TIMEOUT  //WaitForSingleObject(xx,INFINITE)
ReleaseMutex(hQueryMutex);

 _getch();
 _beginthread(
============================上=SQLAPI==============

---------------------berkeley db 教程__未试---------------------

Berkeley DB 4.8.24    


每个 Key/Data 对构成一条记录

它的安装很简单。

cd build_unix
../dist/configure
make
make install

#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
//only this head should include for use bdb.
#include <db.h>  
#define DATABASE "yangjian.db"

int main()
{
    DB_ENV *myEnv;
    DB *dbp;
    DBT key, data;
    int ret,t_ret;
    u_int32_t env_flags;
    //........... Create an environment object and initialize it for error reporting
    ret = db_env_create(&myEnv, 0);
    if (ret != 0)
    {
          fprintf(stderr, "Error creating env handle: %s\n", db_strerror(ret));
          return -1;
    }
    //........If the environment does not exist create it. Initialize the in-memory cache.
    env_flags = DB_CREATE | DB_INIT_MPOOL;
    //........Open the environment.
    ret = myEnv->open(myEnv,"/home/yangbin1/yangjian/my/db/testevn",env_flags,0);
    if (ret != 0)
    {
          fprintf(stderr, "Environment open failed: %s", db_strerror(ret));
          return -1;
    }

    if ((ret = db_create(&dbp, myEnv, 0)) != 0)
    {
          fprintf(stderr, "db_create: %s\n", db_strerror(ret));
          exit (1);
    }

    if ((ret = dbp->open(dbp, NULL, DATABASE, NULL, DB_BTREE, DB_CREATE, 0664)) != 0)
    {
          dbp->err(dbp, ret, "%s", DATABASE);
          exit (1);
    }
    memset(&key, 0, sizeof(key));
    memset(&data, 0, sizeof(data)); key.data = "sport";
    key.size = sizeof("sport");
    data.data = "football";
    data.size = sizeof("football");
/*
    //......put data
    if ((ret = dbp->put(dbp, NULL, &key, &data, 0)) == 0)
    {
          printf("db: %s: key stored.\n", (char *)key.data);
    }
      else
    {
          dbp->err(dbp, ret, "DB->put");
    }
*/

    //........put data NOOVERWRITE
    if ((ret = dbp->put(dbp, NULL, &key, &data, DB_NOOVERWRITE)) == 0)
    printf("db: %s: key stored.\n", (char *)key.data);
    else dbp->err(dbp, ret, "DB->put");

    //.......get data
    if ((ret = dbp->get(dbp, NULL, &key, &data, 0)) == 0)
    printf("db: %s: key retrieved: data was %s.\n", (char *)key.data, (char *)data.data);
    else
    dbp->err(dbp, ret, "DB->get");

    //......del data
    if((ret = dbp->del(dbp, NULL, &key, 0)) == 0)
    printf("db: %s: key was deleted.\n", (char *)key.data);
    else
    dbp->err(dbp, ret, "DB->del");

    //.........close, only when the db successful closed,the data can real write to the disk.
    //if ((t_ret = dbp->close(dbp, 0)) != 0 && ret == 0)
    //ret = t_ret;
    //exit(ret);
    if (dbp != NULL)
    dbp->close(dbp, 0);
    //.........close evn
    //........When you are done with an environment, you must close it.
    //........Before you close an environment, make sure you close any opened databases
    if (myEnv != NULL)
    myEnv->close(myEnv, 0);

    return 0;
}
---------------------上 berkeley db 教程__未试---------------------


---------------------C连接PostGreSQL__未试.c-----------------------------
    PGconn * conn = PQconnectdb("host=127.0.0.1 user=root password=root dbname=ggtong port=5432");
    if (PQstatus(conn) == CONNECTION_OK)
    {

        PGresult * res = NULL;
        /*
        res = PQexec(conn, "insert into kline_min  (mark)  values ( 1 )");
        if (PQresultStatus(res) != PGRES_COMMAND_OK)
        {
            printf("pgsql failed: %s", PQerrorMessage(conn));
            PQclear(res);

        }
        PQclear(res);
        */

        //PQsetClientEncoding(conn, "UNICODE");
        //PQsetClientEncoding(conn, "GBK");

        //PGresult * res = PQexec(conn, sql.c_str());
        res = PQexec(conn, sql.c_str());
        printf("sql:%s\r\n", sql.c_str());
        if (res != NULL)
        {
            int status = PQresultStatus(res);
            if (status == PGRES_COMMAND_OK || status == PGRES_TUPLES_OK)
            {
                //这个应该是取字段的值
                //char * res_userid = PQgetvalue(res, 0, 0);
                //char * res_password = PQgetvalue(res, 0, 1);
                //....
            }
            else
            {
                printf("pgsql failed: %s", PQerrorMessage(conn));
                PQclear(res);            
            }
            PQclear(res);
        }
    }
    PQfinish(conn);

代码一般都不困难的,第一次难的一般都是如何 include lib 等等.
1. #include <libpq-fe.h>//pgsql 并且要添加相应的搜索路径.
2. 链接  libpq.lib ,同样要添加相应的搜索路径.

------------------------上 C连接PostGreSQL__未试.c-------------------------




===========C++ 连接Oracle OCCI
OCI Shared Library (libociei.so on Solaris and oraociei10.dll on Windows); 
OCCI Library (libocci.so.10.1 on Solaris and oraocci10.dll on Windows)

Client Code Library (libclnstsh.so.10.1 on Solaris and oci.dll on Windows)
Security Library (libnnz10.so on Solaris and orannzsbb10.dll on Windows)

oraociei10.dll library can be copied from the ORACLE_HOME\instantclient


g++ -o xx -I ./lib -l occi -l lclntcsh  才可以编译
  libocci.so,libclntcsh.so　(编译)


看OCCi示例

#include <occi.h>
using namespace oracle::occi;

Environment *env = Environment::createEnvironment(Environment::OBJECT);
Connection *conn = env->createConnection("scott","tiger");
Statement *stmt = conn->createStatement();
				//conn->createStatement ("INSERT INTO journal_tab (jid, jname) VALUES (:x, :y)");//如是select
				//ResultSet *rset = stmt->executeQuery ();
				// while (rset->next ())   {rset->getInt (1) ....
try
{
	stmt->setSQL("begin OCCIDEMO1.GetBookDetails(:1, :2); end;");//存储过程
	stmt->setNumber(1, 10);//1 - IN - Id
	stmt->registerOutParam(2, OCCISTRING, 100); //2 - OUT
	stmt->execute();

    string title = stmt->getString(2)


   conn->terminateStatement(stmt);

   env->terminateConnection(conn);
   Environment::terminateEnvironment(env);
} catch (SQLException &ex)
{
  cout << "Error running demo : " << ex.getMessage() << endl;
}

--------------- java oci
javal 连接oracle,使用oci性能高,
而使用oci时则要安装oracle的客户端。 
jdbc:oracle:thin:@10.1.1.2:1521:shdb 改成 jdbc:oracle:oci8:@shdb   //或是oci  @后的最oracle 客户端中的配置
-----------------
=============OCI
OCI Instant Client 中编译OCI成功
linux-sm8v:/usr/local/instantclient_11_1/sdk/demo # 
gcc -o cdemo81 cdemo81.c -I /usr/local/instantclient_11_1/sdk/include -L /usr/local/instantclient_11_1/  -lclntsh  -l nnz11

Linux and UNIX 	Description for Linux and UNIX 		Windows 		Description for Windows

libclntsh.so.11.1  Client Code Library				oci.dll		Forwarding functions that applications link with
libociei.so		OCI Instant Client Data Shared Library	oraociei11.dll	Data and code
libnnz11.so		Security Library					orannzsbb11.dll	Security Library


//MinGW对Oracle11gR2 XE测试也OK,C:\oraclexe\app\oracle\product\11.2.0\server\oci\lib\MSVC   -l只加oci就OK

//gcc 对instantclient 加-l clntsh -lnnz11

#include <oci.h>
#include <stdlib.h>
#include <stdio.h>
//--OCI
static OCIEnv *oci_env;
static OCIError *oci_error;
static text *oci_username = (text *) "hr"; //通过选项来做
static text *oci_password = (text *) "hr";
static text *oci_sid = (text*) "xe";
static sword oci_status;

OCIDefine *ocidefine_uid = (OCIDefine *) 0;//u_id
OCIDefine *ocidefine_mac = (OCIDefine *) 0;	//mac
OCIDefine *ocidefine_clientID = (OCIDefine *) 0;//client_id

void checkerr(OCIError *oci_error, sword status);
int isAttach = TRUE;// 只是一个是否 sid 错误
int main(int argc, char* argv[])
{
	OCISession *oci_session = (OCISession *) 0;
	OCIServer *oci_server;
	OCISvcCtx *oci_SvcCtx;
	OCIStmt *stmt_sel;

	char oci_mac[30],oci_clientID[30];
	int  oci_uid ;

	text *sel = (text *) "SELECT u_id, mac,client_id FROM auth";// where u_id=100
	//不能有;

	sword errcode = 0;
	errcode = OCIEnvCreate((OCIEnv **) &oci_env, (ub4) OCI_DEFAULT, (dvoid *) 0, (dvoid * (*)(dvoid *, size_t)) 0, (dvoid * (*)(dvoid *, dvoid *, size_t)) 0, (void(*)(dvoid *, dvoid *)) 0, (size_t) 0, (dvoid **) 0);

	if (errcode != 0)
	{
		(void) printf("OCIEnvCreate failed with errcode = %d.\n", errcode);
		exit(1);
	}

	(void) OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &oci_error, OCI_HTYPE_ERROR, (size_t) 0, (dvoid **) 0);
	(void) OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &oci_server, OCI_HTYPE_SERVER, (size_t) 0, (dvoid **) 0);
	(void) OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &oci_SvcCtx, OCI_HTYPE_SVCCTX, (size_t) 0, (dvoid **) 0);
	int tmp_tatus= OCIServerAttach(oci_server, oci_error, (OraText*) oci_sid, (sb4) strlen((char *)oci_sid), 0);//OraText*  , SID ERROR
	if (tmp_tatus == OCI_ERROR)
		isAttach = FALSE;
	checkerr(oci_error,tmp_tatus);


	(void) OCIAttrSet((dvoid *) oci_SvcCtx, OCI_HTYPE_SVCCTX, (dvoid *) oci_server, (ub4) 0, OCI_ATTR_SERVER, (OCIError *) oci_error);
	(void) OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &oci_session, (ub4) OCI_HTYPE_SESSION, (size_t) 0, (dvoid **) 0);
	(void) OCIAttrSet((dvoid *) oci_session, (ub4) OCI_HTYPE_SESSION, (dvoid *) oci_username, (ub4) strlen((char *) oci_username), (ub4) OCI_ATTR_USERNAME, oci_error);
	(void) OCIAttrSet((dvoid *) oci_session, (ub4) OCI_HTYPE_SESSION, (dvoid *) oci_password, (ub4) strlen((char *) oci_password), (ub4) OCI_ATTR_PASSWORD, oci_error);

	checkerr(oci_error, OCISessionBegin(oci_SvcCtx, oci_error, oci_session, OCI_CRED_RDBMS, (ub4) OCI_DEFAULT));//OCI_SYSDBA
	(void) OCIAttrSet((dvoid *) oci_SvcCtx, (ub4) OCI_HTYPE_SVCCTX, (dvoid *) oci_session, (ub4) 0, (ub4) OCI_ATTR_SESSION, oci_error);

	checkerr(oci_error, OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &stmt_sel, OCI_HTYPE_STMT, (size_t) 0, (dvoid **) 0));


	checkerr(oci_error, OCIStmtPrepare(stmt_sel, oci_error, sel, (ub4) strlen((char *) sel), (ub4) OCI_NTV_SYNTAX, (ub4) OCI_DEFAULT));//语法Native

	checkerr(oci_error, OCIDefineByPos(stmt_sel, &ocidefine_uid, 		oci_error,1, (dvoid *)& oci_uid, 		(sb4) sizeof(oci_uid), SQLT_INT, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, OCI_DEFAULT));
	checkerr(oci_error, OCIDefineByPos(stmt_sel, &ocidefine_mac, 		oci_error,2, (dvoid *) oci_mac, 			(sword) sizeof(oci_mac), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, OCI_DEFAULT));
	checkerr(oci_error, OCIDefineByPos(stmt_sel, &ocidefine_clientID, oci_error,3, (dvoid *) &oci_clientID, (sword) sizeof(oci_clientID), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, OCI_DEFAULT));

	//	int linesize=sizeof(oci_mac)+sizeof(oci_clientID)+sizeof(int);
	//	OCIDefineArrayOfStruct(ocidefine_uid,		oci_error,linesize,0,0,0);
	//	OCIDefineArrayOfStruct(ocidefine_mac,		oci_error,linesize,0,0,0);
	//	OCIDefineArrayOfStruct(ocidefine_clientID,oci_error,linesize,0,0,0);


	checkerr (oci_error, OCIStmtExecute(oci_SvcCtx, stmt_sel, oci_error, (ub4) 0, (ub4) 0, (CONST OCISnapshot *) NULL, (OCISnapshot *) NULL, OCI_DEFAULT));//select 一行iters就是0
	fflush(stdout);
	while(OCIStmtFetch(stmt_sel,oci_error,1,OCI_FETCH_NEXT,OCI_DEFAULT)!=OCI_NO_DATA)//移动指针，就可以多条记录的取了
	{
		printf("oci_uid=%d \t oci_mac=%s \t oci_clientID=%s\n",oci_uid,oci_mac,oci_clientID);
		printf("-------------------------------------------\n");
	}
	//==========================Insert OK
	int myid=105;

	//char * mymac="11:99:88";
	char mymac[10]="11:99:88";
	char myclient[10]="zhao";

//以下可以不加
//	mymac[sizeof(mymac)-1]='\0';
//	myclient[sizeof(myclient)-1]='\0';
//
//	strcat(mymac,"\0");
//	strcat(myclient,"\0");


	OCIBind *bind_uid = (OCIBind *) 0;
	OCIBind *bind_mac = (OCIBind *) 0;
	OCIBind *bind_client = (OCIBind *) 0;

	OCIStmt *stmt_insert;
	text *insert = (text *) "insert into auth( u_id, mac,client_id) values(:myid,:mymac,:myclient)";

	checkerr(oci_error, OCIHandleAlloc((dvoid *) oci_env, (dvoid **) &stmt_insert, OCI_HTYPE_STMT, (size_t) 0, (dvoid **) 0)); //insert

	checkerr(oci_error, OCIStmtPrepare(stmt_insert, oci_error, insert, (ub4) strlen((char *) insert), (ub4) OCI_NTV_SYNTAX, (ub4) OCI_DEFAULT));

	//mymac前有没有＆ 都行 ，myid是int型一定要有＆
//	checkerr (oci_error,OCIBindByName(stmt_insert, &bind_uid, oci_error, (text *)":myid", strlen(":myid"), 			(dvoid *) &myid,		sizeof(myid), SQLT_INT, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));
//	checkerr (oci_error,OCIBindByName(stmt_insert, &bind_mac, oci_error, (text *)":mymac", strlen(":mymac"), 		(dvoid *)mymac,		 sizeof(mymac), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));
//	checkerr (oci_error,OCIBindByName(stmt_insert, &bind_client, oci_error, (text *)":myclient", strlen(":myclient"), (dvoid *)myclient, sizeof(myclient), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));

	checkerr (oci_error,OCIBindByPos(stmt_insert, &bind_uid, oci_error, 				1,				 				(dvoid *) &myid, (sword) sizeof(myid), SQLT_INT, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));
	checkerr (oci_error,OCIBindByPos(stmt_insert, &bind_mac, oci_error, 				2,				 				(dvoid *) &mymac, (sword) sizeof(mymac), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));
	checkerr (oci_error,OCIBindByPos(stmt_insert, &bind_client, oci_error, 			3, 							(dvoid *) &myclient, (sword) sizeof(myclient), SQLT_STR, (dvoid *) 0, (ub2 *) 0, (ub2 *) 0, (ub4) 0, (ub4 *) 0, OCI_DEFAULT));

	oci_status = OCIStmtExecute(oci_SvcCtx, stmt_insert, oci_error, (ub4) 1, (ub4) 0, (CONST OCISnapshot *) NULL, (OCISnapshot *) NULL, OCI_DEFAULT);
		//非select,执行次数等于iters减去rowoff的值,iters不能为0

	checkerr(oci_error, oci_status);


	//==========================Destory Order

	if (stmt_insert != NULL || stmt_sel != NULL)
	{
		checkerr(oci_error, OCITransCommit(oci_SvcCtx, oci_error, 0));
		checkerr(oci_error, OCISessionEnd(oci_SvcCtx, oci_error, oci_session, OCI_DEFAULT));
		checkerr(oci_error, OCIHandleFree((dvoid *) oci_session, OCI_HTYPE_SESSION));
	}

	if (isAttach)//sid error
	{
		checkerr(oci_error, OCIServerDetach(oci_server, oci_error, OCI_DEFAULT));
	}


	checkerr(oci_error, OCIHandleFree((dvoid *) oci_server, OCI_HTYPE_SERVER));

	//username/pwd  error
	if (stmt_insert != NULL) checkerr(oci_error, OCIHandleFree((dvoid *) stmt_insert, OCI_HTYPE_STMT));

	if (stmt_sel != NULL) checkerr(oci_error, OCIHandleFree((dvoid *) stmt_sel, OCI_HTYPE_STMT));

	checkerr(oci_error, OCIHandleFree((dvoid *) oci_error, OCI_HTYPE_ERROR));
	checkerr(oci_error, OCIHandleFree((dvoid *) oci_env, OCI_HTYPE_ENV));

	return 1;

}


void checkerr(oci_error, status)
OCIError *oci_error;
sword status;
{
  text errbuf[512];
  sb4 errcode = 0;

  switch (status)
  {
  case OCI_SUCCESS:
    break;
  case OCI_SUCCESS_WITH_INFO:
    (void) printf("Error - OCI_SUCCESS_WITH_INFO\n");
    break;
  case OCI_NEED_DATA:
    (void) printf("Error - OCI_NEED_DATA\n");
    break;
  case OCI_NO_DATA:
    (void) printf("Error - OCI_NODATA\n");
    break;
  case OCI_ERROR:
    (void) OCIErrorGet((dvoid *)oci_error, (ub4) 1, (text *) NULL, &errcode, errbuf, (ub4) sizeof(errbuf), OCI_HTYPE_ERROR);
    (void) printf("Error - %.*s\n", 512, errbuf);
    break;
  case OCI_INVALID_HANDLE:
    (void) printf("Error - OCI_INVALID_HANDLE\n");
    break;
  case OCI_STILL_EXECUTING:
    (void) printf("Error - OCI_STILL_EXECUTE\n");
    break;
  case OCI_CONTINUE:
    (void) printf("Error - OCI_CONTINUE\n");
    break;
  default:
    break;
  }
}

/*
drop table auth;
create table auth(
	u_id int primary key,
	mac varchar2(17),
	client_id varchar2(20)
	);

insert into auth values(100,'00:1C:23:F8:1E:95','lisi');
insert into auth(u_id,mac,client_id) values(101,'00:1C:23:F8:1E:96','wangwu');
 */

 
------------------binutil 生成.bin 安装文件------------
solaris 控制台运行.bin文件显示彩色文字

 


-----------------------groff   不常用
man -w 显示man 搜索路径
man -w fork 
man2html ./xxx.1 > xxx.html
groff －Thtml -mandoc ld.1 > ld.html
2、测试显示效果
$ groff -Tascii -man hello.1   

man --path ,manpath  和man -w 一样的效果,所有 man 的查找目录,配置在/etc/man.conf中的MANPATH
man xxx.file 要加./的路径

-mandoc or -m mandoc. 会检测是 -m man(传统的 -man or -m man 使用man 7 groff_man) 还是 -m mdoc (BSD风格  -mandoc or -m mandoc 使用man 7 groff_mdoc )

-m www  HTML   ( man 7 groff_www)


.TH title section [extra1 [extra2 [extra3]]]   ###标题头extra1在页脚中间 ,extra2在页脚左 , 偶数页左,奇数页右,extra3在头中心
.TH LS "1" "June 2006" "ls 5.97" "User Commands"



man groff_man		

.SH NAME		#小节头
ls \- list directory contents   ##小节的子内容 \是转义-  ,可在-前加多个man命令



.B ls		###加粗ls
.I ls		###下划线ls
[\fIOPTION\fR]  ##\fI xxx \fR 表示加下划线从O到R

.PP		###一个段落

.TP		###下的第一行标签值不缩进,第二行缩进
\fB\-a\fR, \fB\-\-all\fR	###\fB xxx \fR  把xxx加粗,标签值		
do not ignore entries starting with .

.IP 下面的一部分缩进一段
.br #换行

.\" 注释

.BI "FILE *fopen(const char *" path ", const char *" mode );  ##第一参数加粗,每参数下划线,交替的
.BR open (2),   用空格交替粗体和正常
.IR  交替斜体(其实是下划线)和罗马字(正常)

.SS 加粗,Section


.sp ##空行吧
 man -w execle 和  man -w execlp 都是同一文件,因man后
 NAME
       execl, execlp, execle, execv, execvp - 


man system 显示  .nf   .fi 中加程序代码(nf 是no-fill模式,)


help2man 工具   要可执行文件加--help有输出  ,看ls --help的输出
help2man ./xxx >xxx.1  ## 可./xxx --help
-L zh


view /etc/man.config   有
MANPATH /usr/share/man
MANPATH_MAP     /usr/local/bin          /usr/local/share/man

man国际化  /usr/share/man/zh_CN/   试过的OK,但一定要写两次man文件,

-------
