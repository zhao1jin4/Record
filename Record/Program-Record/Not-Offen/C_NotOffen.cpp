

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
