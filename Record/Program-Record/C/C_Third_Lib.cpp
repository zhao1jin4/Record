=========nanomsg是一个socket library

===========C连接MySQL Select
#include <windows.h>
//如是运行在windows 下MinGW还要加windows.h,//如是运行在windows 下MinGW还要加windows.h,对于libmysql.dll使用-l libmysql或者-lmysql,对于libmysql.lib只能使用-llibmysql,测试成功

//---C++连接Mysql Fedora-9成功,RedhatEL5成功----sparc-solaris 9 失败
//---g++ -o mysql_conn mysql_conn.cpp -L /usr/local/mysql/lib/mysql -l mysqlclient -I/usr/local/mysql/include/mysql ---------------
//# /usr/local/mysql/bin/mysql_config --libs --cflags

#include <mysql.h>
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
	char *user = "root", *pwd = "root", *dbname = "mysql";
	MYSQL mysql;
	MYSQL_RES *mysql_ret;
	MYSQL_ROW mysql_row;
	MYSQL_FIELD * fd;
	unsigned long num_rows;
	int ret;
	mysql_init(&mysql);
	
	unsigned int timeout=7;
    mysql_options(&mysql,MYSQL_OPT_CONNECT_TIMEOUT,(const char * )&timeout); //连接超时时间

	if (mysql_real_connect(&mysql, NULL, user, pwd, dbname, 0, NULL, 0))
	{
		printf("Connection   success!\n");
		ret = mysql_query(&mysql, "select   *   from   user");
		printf("Effect rows is %lu\n",(unsigned long) mysql_affected_rows(&mysql));//只对update,insert,delete有效
		if (!ret)
		{
			printf("Query   Success!\n");
			mysql_ret = mysql_store_result(&mysql);
			if (mysql_ret != NULL)
			{
				int cols=mysql_num_fields(mysql_ret);
				printf("===========have %d fileds\n",cols);
				int i;
				for (i = 0; fd = mysql_fetch_field(mysql_ret); i++)
					printf("|%s|\t",fd->name);
				
				printf("\n");
				
				num_rows = mysql_num_rows(mysql_ret);
				if (num_rows != 0)
				{
					printf("=======have %d rows\n", num_rows);
					while (mysql_row = mysql_fetch_row(mysql_ret))
					{
						printf("|%s|\t|%s|\t\t\n", mysql_row[0],mysql_row[1]);
					}
				} else
				{
					printf("mysql_num_rows   Failed!\n");
					exit(-1);
				}
				mysql_free_result(mysql_ret);
				exit(0);
			} else
			{
				printf("Store   Result   Failed!\n");
				exit(-1);
			}
		} else
		{
			printf("Query   Failed!\n");
			exit(-1);
		}
	} else
	{
		printf("Connection   Failed\n");
		printf("Errorno=%d and  Error=%s\n",mysql_errno(&mysql),mysql_error(&mysql))
		exit(-1);
	}

	mysql_close(&mysql);
}
//DDL就不用 mysql_store_result

===========C连接 MongoDB


===========C连接SQLite3
//MinGW要调试的话,必须把sqli3.dll放在PATH中可以找到才行
#include <stdlib.h>
#include <stdio.h>
#include <sqlite3.h>

//#pragma comment(lib, "sqlite.lib")//我把sqlite编译成了一个静态的lib文件。
// 源码安装sqlite 不要删make的文件夹，eclipse-debug时可以打开源码.c

//#define DB_FILE "/home/zhaojin/sqliteDB"
#define DB_FILE "c:/temp/sqliteDB"

void error_exit(sqlite3 *db, char *message)
{
	fprintf(stderr, "Error on %s: %s\n", message, sqlite3_errmsg(db));
	sqlite3_close(db);
	exit(EXIT_FAILURE);
}

void testOne()
{
	sqlite3 * handle;
	int status;
	sqlite3_stmt *stmt_sel;
	sqlite3_stmt *stmt_insert;


	char * error_msg;

	char * z;
	char ** resultSet;
	int nrow = 0, ncolumn = 0;

	status=	sqlite3_open(DB_FILE, &handle); //文件没有会自动创建的
	//status=sqlite3_open_v2("sqliteDB", &handle,SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE|SQLITE_OPEN_FULLMUTEX ,vfs_name);
	if (status != SQLITE_OK || handle == NULL)
		error_exit(handle,"open database");
/*
	//===========create table和insert into   OK
	status = sqlite3_exec(handle, "BEGIN EXCLUSIVE TRANSACTION", NULL, NULL, &error_msg);//独占
	while(1)
	{
		if (status == SQLITE_OK) break;
		if (status != SQLITE_BUSY) error_exit(handle, "BEGIN EXCLUSIVE TRANSACTION");
		sleep(1);
	}
	sqlite3_exec(handle,"create table student (id int,name varchar(20))",0,0,&error_msg);
	sqlite3_exec(handle,"insert into student(id,name)values(1,'zhaojin')",0,0,&error_msg);
	sqlite3_exec(handle,"insert into student(id,name)values(2,'李')",0,0,&error_msg);
	sqlite3_exec(handle,"insert into student(id,name)values(3,'王')",0,0,&error_msg);
	status = sqlite3_exec(handle, "COMMIT TRANSACTION", NULL, NULL, &error_msg);//提交，解锁
	if (status != SQLITE_OK) error_exit(handle, "COMMIT TRANSACTION");
	//===========OK
*/


/*
	//=============================insert 数据  OK
	define SQL_INSERT "insert into student values(:myid,:myname)"//;号可有可无，也可以用？
	status = sqlite3_prepare(handle,SQL_INSERT,strlen(SQL_INSERT),&stmt_insert,&z);
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_prepare");

	int	id_index = sqlite3_bind_parameter_index(stmt_insert, ":myid");//根据占位符号，得到对应的索引位置,:myid也可以用？
	int name_index = sqlite3_bind_parameter_index(stmt_insert, ":myname");

	//status=sqlite3_bind_int(stmt_sel,id_index,1);//不能是int--sqlite不认int ,只认字串
	status=sqlite3_bind_text(stmt_insert,id_index,"10",sizeof("10"),SQLITE_TRANSIENT);//text用SQLITE_TRANSIENT   blob用SQLITE_STATIC
	//sqlite3_bind_blob(stmt, t_index, value, strlen(value), SQLITE_STATIC);//BLOB类型,表示0从来不改变，从来不会被销毁
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_bind_text/mydi");


	status=sqlite3_bind_text(stmt_insert,name_index,"zhaojin",sizeof("zhaojin"),SQLITE_TRANSIENT);//对text
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_bind_text/myname");

	status=sqlite3_step(stmt_insert);
	if ( status!=SQLITE_DONE) error_exit(handle, "step");//SQLITE_DONE 说明没有查询结果，或者insert 成功
//----------
 */


	//=============================select 数据

	#define SQL_SEL "select id ,name from student where id=:myid and name=:myname;"

	//status = sqlite3_prepare_v2(handle,SQL_SEL,strlen(SQL_SEL),&stmt_sel,&z);
	status = sqlite3_prepare(handle,SQL_SEL,strlen(SQL_SEL),&stmt_sel,&z);
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_prepare");

	int	id_index = sqlite3_bind_parameter_index(stmt_sel, ":myid");
	int name_index = sqlite3_bind_parameter_index(stmt_sel, ":myname");

	int id_value=10; //=======num====== 如数据库中只插入过int，用int也可以查到
	//char * id_value="a001";

	char *  name_value="张";

	status=sqlite3_bind_int(stmt_sel,id_index,id_value);//=======num======
	//status=sqlite3_bind_text(stmt_sel,id_index,id_value,strlen(id_value),SQLITE_TRANSIENT);
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_bind_text/mydi");

	status=sqlite3_bind_text(stmt_sel,name_index,name_value,strlen(name_value),SQLITE_TRANSIENT);//对text
	if (status != SQLITE_OK) error_exit(handle, "sqlite3_bind_text/myname");

	//status=sqlite3_step(stmt_sel); //	第一次就DONE   --  debug用，FAIL
	//if (!(status == SQLITE_ROW || status==SQLITE_DONE)) error_exit(handle, "step");

	while(sqlite3_step(stmt_sel)==SQLITE_ROW)//
	{
			char * name=sqlite3_column_text(stmt_sel,1);

			int id=sqlite3_column_int(stmt_sel,0);//=======num=====
			printf("id=%d \t name=%s \n",id,name); //======num=====

			//char *   id=sqlite3_column_text(stmt_sel,0);//0开始的第一个段
			//printf("id=%s \t name=%s \n",id,name); //
	}

	sqlite3_finalize(stmt_sel);
	sqlite3_finalize(stmt_insert);
	sqlite3_free(error_msg);
	sqlite3_close(handle);

}
void testTwo()//OK
{
	sqlite3 * db;
		int result;
		char * errmsg = NULL;
		char **resultSet;
		int nRow, nColumn;
		int i, j;
		int index;

		result = sqlite3_open(DB_FILE, &db);
		if (result != SQLITE_OK)
		{
			return ;
		}

		result = sqlite3_get_table(db, "select * from student", &resultSet, &nRow, &nColumn, &errmsg);

		if (SQLITE_OK == result)
		{
		/*
			index = nColumn; //前面说过 dbResult 前面第一行数据是字段名称，从 nColumn 索引开始才是真正的数据
			printf("查到%d条记录\n", nRow);
			for (i = 0; i < nRow; i++) //   3
			{
				printf("第 %d 条记录\n", i + 1);
				for (j = 0; j < nColumn; j++)  //2
				{
					printf("字段名:%s  > 字段值:%s\n", resultSet[j], resultSet[index]);
					++index; // dbResult 的字段值是连续的，从第0索引到第 nColumn - 1索引都是字段名称，从第 nColumn 索引开始，后面都是字段值，它把一个二维的表（传统的行列表示法）用一个扁平的形式来表示
				}
				printf("-------\n");
			}
			*/
			int k;
			for (k = 1; k <= nRow; k++)
			{
				printf("%s \t %s\n",resultSet[k * nColumn],resultSet[k * nColumn + 1]);//SQLite好像没有数值概念只存字串
			}
		}

		sqlite3_free_table(resultSet);
		sqlite3_close(db);

}
void testThree()//OK
{
		int callback(void *NotUsed, int argc, char **values, char **col_name);
		sqlite3 *db;
		char *msg_error = 0;
		int rc;

		rc = sqlite3_open(DB_FILE, &db);
		if (rc)
		{
			fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
			sqlite3_close(db);
			exit(1);
		}
		rc = sqlite3_exec(db, "select * from student", callback, 0, &msg_error);
		if (rc != SQLITE_OK)
		{
			fprintf(stderr, "SQL error: %s\n", msg_error);
		}
		sqlite3_close(db);

}
int callback(void *NotUsed, int argc, char **values, char **col_name)
{
	int i;
	for (i = 0; i < argc; i++)
	{
		printf("%s = %s\n", col_name[i], values[i] ? values[i] : "NULL");
	}
	printf("\n");
	return 0;
	/*
	id = 1
	name = zhaojin

	id = 2
	name = 李
	*/
}



int main(int argc, char **argv)
{
	//linux sqlite2版本中可用 四方向键来操作，而sqlite3就不行了
	testOne();
	//testTwo();
	//testThree();
	return 1;
}

/*
	drop table student;
	create table student(id varchar(20),name varchar(20));
	insert into student values(10,'张');
*/



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




=========== Redis C client  hiredis 


------------------------JVM TI
Java HotSpot VM 
JPDA(Java Platform Debugger Architecture)
	JVM TI(JVM Tool Interface)
	JDWP(Java Debug Wire Protocol)调试器和被调试的应用之间通信的协议
	JDI(Java Debug Interface)调试客户端可以使用的接口，使用Java语言实现


JVM TI is implemented by a VM and the client is the JPDA back-end.

front-end (in the debugger process) and the back-end (in the debuggee process)
front-end位于tools.jar

被调试应用：就是debuggee,内部嵌入一个back-end




JDK 1.6  的 JVMTI =JVM Tool Interface  
JVMTI 前身JVM DI (debug)/JVM PI (profile)

jdk-8u121-windows-x64-demos\jdk1.8.0_121\demo\jvmti 示例
-agentlib:hprof=help  ( Heap and CPU Profiling Agent (JVMTI Demonstration Code))
java.lang.instrutment包

一个JVM TI的客户端程序为Agent，Agent与JVM位于同一个进程内
Agent启动：JVM TI Agent有两种启动方式，一种是与JVM一起启动，另一种是在JVM启动之后，通过Attach到该JVM
#include <jvmti.h>

java -agentlib:jdwp=help   表示在windows 下加载PATH路径中的 jdwp.dll　,Solaris 下LD_LIBRARY_PATH路径中jdwp.so
java -agentlib:hprof=help

-agentpath: 绝对路径


JVM的调试agent启动    jdwp


与JVM启动的Agent，JVM会回调 Agent_OnLoad 对Agent进行初始化

大部分平台只支持本地进程Attach，Sun Solaris系统下的JVM支持Remote Attach


Attach启动,JVM会回调 Agent_OnAttach 对Agent进行初始化

jvmtiEnv代表了与JVM的连接,所有的功能都可以通过jvmtiEnv指针来获得
vmtiEnv *jvmti;  
 ...  
(*jvm)->GetEnv(jvm, &jvmti, JVMTI_VERSION_1_0);  

在JVM退出的时候，会回调Agent的结束接口 Agent_OnUnload

------------------------JDI


=============Firefox 插件开发
NPAPI 是 Netscape Plugin Application Programming Interface的缩写,可用于Chrome,Safari,Opera跨浏览器的插件

add-on 包括 plugin 和 extension(使用XUL的标签,js开发)

https://developer.mozilla.org/en-US/docs/Plugins

Gecko Plugin API Reference (NPAPI)
Scripting plugins (npruntime) 
Gecko SDK 就是 xulrunner-19.0.1.en-US.win32.sdk.zip

XPIDL
XPCOM 过时

http://www.cppblog.com/epubcn/archive/2008/11/11/66612.html
https://developer.mozilla.org/en-US/docs/Compiling_The_npruntime_Sample_Plugin_in_Visual_Studio

建立win32 Project->选择DLL,Empty ,生成的dll名字必须以np开头

右击项目->Include  中加xulrunner的include目录
右击项目->Configration Properites -> C/C++ ->Preprocessor->在Preprocessor Definitions项目中要有下面项
	WIN32;_WINDOWS;XP_WIN32;MOZILLA_STRICT_API;XPCOM_GLUE;XP_WIN;_X86_;NPSIMPLE_EXPORTS
右击项目->Configration Properites-> C/C++ ->PreCompliled Headers->Precompiled Header项目中选择Not Using
右击项目->Configration Properites-> Linkers->Input->Module Definition File项中输入相对项目的 .def 文件路径

firefox地址栏中输入　about:plugins

https://developer.mozilla.org/en-US/docs/Plugins
https://developer.mozilla.org/en-US/docs/Gecko_Plugin_API_Reference

NPP 开头 Plugin 
NPN 开头 Netscape 
NP 开头 数据结构以

NP_Initialize  首次
NPP_New		建立一个实例时
NPP_Destroy
NP_Shutdown 最后一次实例关闭时


注册插件
HKLM/Software/MozillaPlugins/<插件名>,如Adobe Reader
中要有
Descripton: REG_SZ "Description of the Plugin"
Path: REG_SZ "C:\..Path to the plugin.dll"
ProductName: REG_SZ "The Plugin Name"
Vendor: REG_SZ "The Plugin Author/Vendor"
Version: REG_SZ "0.5.whatever plugin version string"

----插件检查顺序
--Windows

MOZ_PLUGIN_PATH环境变量
%APPDATA%
安装目录
HKEY_CURRENT_USER\Software\MozillaPlugins\*
HKEY_LOCAL_MACHINE\Software\MozillaPlugins\*

--Mac OS X 目录中多数文件以.plugin结尾的
~/Library/Internet Plug-Ins.
/Library/Internet Plug-Ins.
/System/Library/Frameworks/JavaVM.framework/Versions/Current/Resources.

--Linux
MOZ_PLUGIN_PATH环境变量
~/.mozilla/plugins
/usr/lib/mozilla/plugins (/usr/lib64/mozilla/plugins)

----检查插件是否安装的JS代码

var mimetype = navigator.mimeTypes["application/x-shockwave-flash"];
if (mimetype) {
   var plugin = mimetype.enabledPlugin;
   if (plugin) {
      // Yes, so show the data in-line
      document.writeln("Here\'s a movie: <object data='mymovie.swf' height='100' width='100'></object>");
   } else {
      // No, so provide a link to the data
      document.writeln("<a href='mymovie.swf'>Click here</a> to see a movie.");
   }
} else {
   document.writeln("Sorry, can't show you this movie.");
}

embed标签，可以使用其hidden属性
object标签，由于它没有hidden属性，你可以用CSS来
object {
  visibility: visible;
}
object.hiddenObject {
  visibility:   hidden !important;
  width:        0px    !important;
  height:       0px    !important;
  margin:       0px    !important;
  padding:      0px    !important;
  border-style: none   !important;
  border-width: 0px    !important;
  max-width:    0px    !important;
  max-height:   0px    !important;
}
用户没有安装插件时，提示他进行安装，那么就应该选择用embed,object是W3C标准



1.第一次打开含量有插件的页面时,先调用NP_GetEntryPoints作为调用插件的入口,此方法也只在第一次加载插件时调用，有默认实现，可不写
2. NP_Initialize初始化插件,配对的是NP_Shutdown
3. NPP_New来创建一个插件实例，每打开一个页面都会调用NPP_New一次来创建一个插件实例,对的是NPP_Destory
可选 NPP_SetWindow来调置窗口

/*
//这个部分可以省略
NPError NP_GetEntryPoints(NPPluginFuncs* pluginFuncs)
{
    pluginFuncs->version = 11;
    pluginFuncs->size = sizeof(pluginFuncs);
    pluginFuncs->newp = NPP_New;
    pluginFuncs->destroy = NPP_Destroy;
    pluginFuncs->setwindow = NPP_SetWindow;
    pluginFuncs->newstream = NPP_NewStream;
    pluginFuncs->destroystream = NPP_DestroyStream;
    pluginFuncs->asfile = NPP_StreamAsFile;
    pluginFuncs->writeready = NPP_WriteReady;
    pluginFuncs->write = (NPP_WriteProcPtr)NPP_Write;
    pluginFuncs->print = NPP_Print;
    pluginFuncs->event = NPP_HandleEvent;
    pluginFuncs->urlnotify = NPP_URLNotify;
    pluginFuncs->getvalue = NPP_GetValue;
    pluginFuncs->setvalue = NPP_SetValue;
    
    return NPERR_NO_ERROR;
}
*/

//argn是name,argv是value,HTML中传递的参数
NPError NPP_New(NPMIMEType pluginType, NPP instance, uint16_t mode, int16_t argc, char* argn[], char* argv[], NPSavedData* saved)
{
	instance->pdata = obj;//pdata是plugin data


	for (int16_t i = 0; i < argc; i++) {
        if (strcasecmp(argn[i], "movieurl") == 0) {  //argn是name,argv是value,HTML中传递的参数
            NSString *urlString = [NSString stringWithUTF8String:argv[i]];
            if (urlString) 
                obj->movieURL = [[NSURL URLWithString:urlString] retain];
            break;
        }  
    }
}

NPError NPP_SetWindow(NPP instance, NPWindow* window)
{
    PluginObject *obj = instance->pdata;
    obj->window = *window;

    return NPERR_NO_ERROR;
}
NPError NPP_NewStream(NPP instance, NPMIMEType type, NPStream* stream, NPBool seekable, uint16* stype)//stype是输出参数
{
    *stype = NP_ASFILEONLY;//流到本地文件,完成后调用NPP_StreamAsFile传路径
    return NPERR_NO_ERROR;
}

static void handleMouseUp(PluginObject *obj, NPCocoaEvent *event)
{
    CGPoint point = CGPointMake(event->data.mouse.pluginX, 
                                // Flip the y coordinate
                                obj->window.height - event->data.mouse.pluginY);
}

static int handleKeyDown(PluginObject *obj, NPCocoaEvent *event)
{
    NSString *characters = (NSString *)event->data.key.characters;
    
    if ([characters length] == 1 && [characters characterAtIndex:0] == ' ') {
         
        return 1;
    }
     return 0;
}

static int handleScrollEvent(PluginObject *obj, NPCocoaEvent *event)
{
    double delta = event->data.mouse.deltaY;
    if (delta < 0)
       
    else
       
    return 0;
}
int16_t NPP_HandleEvent(NPP instance, void* event)
{
    PluginObject *obj = instance->pdata;
    NPCocoaEvent *cocoaEvent = event;
    
    switch (cocoaEvent->type) {
        case NPCocoaEventMouseDown:  
            return 1; 
        case NPCocoaEventMouseUp:
       	  handleMouseUp(obj, cocoaEvent);
            return 1;
        case NPCocoaEventMouseDragged:
            return 1; 
        case NPCocoaEventMouseEntered:
            return 1;
       case NPCocoaEventMouseExited:
            return 1;
       case NPCocoaEventKeyDown:
            return handleKeyDown(obj, cocoaEvent);
        
        case NPCocoaEventScrollWheel:
            return handleScrollEvent(obj, cocoaEvent);
        default:
            return 0;
    }
    return 0;
}

enum {
    ID_PLAY,
    ID_PAUSE,
    NUM_METHOD_IDENTIFIERS
};

static NPIdentifier methodIdentifiers[NUM_METHOD_IDENTIFIERS];
static const NPUTF8 *methodIdentifierNames[NUM_METHOD_IDENTIFIERS] = {
    "play", //应该是JS中的方法名
    "pause",
};

static bool movieNPObjectHasMethod(NPObject *obj, NPIdentifier name)
{ for (int i = 0; i < NUM_METHOD_IDENTIFIERS; i++) {
        if (name == methodIdentifiers[i])
            return true;
    }
 return false;
}

static bool movieNPObjectInvoke(NPObject *npObject, NPIdentifier name, const NPVariant* args, uint32_t argCount, NPVariant* result)
{
    MovieNPObject *movieNPObject = (MovieNPObject *)npObject;

    if (name == methodIdentifiers[ID_PLAY]) {
        
        return true;
    }

    if (name == methodIdentifiers[ID_PAUSE]) {
        
        return true;
    }

    return false;
}

static NPClass movieNPClass = {
    NP_CLASS_STRUCT_VERSION,//以下每个参数都函数指针
    movieNPObjectAllocate, // NP_Allocate
    movieNPObjectDeallocate, // NP_Deallocate
    0, // NP_Invalidate
    movieNPObjectHasMethod, // NP_HasMethod,方法名是否存在
    movieNPObjectInvoke, // NP_Invoke,调用自定义方法
    0, // NP_InvokeDefault
    0, // NP_HasProperty
    0, // NP_GetProperty
    0, // NP_SetProperty
    0, // NP_RemoveProperty
    0, // NP_Enumerate
    0, // NP_Construct
};

NPError NPP_GetValue(NPP instance, NPPVariable variable, void *value)
{
    PluginObject *obj = instance->pdata;

    switch (variable) {
        case NPPVpluginScriptableNPObject: //插件自定义方法被调用时
           
           browser->createobject(instance, &movieNPClass);

            return NPERR_NO_ERROR;

        default:
            return NPERR_GENERIC_ERROR;
    }
}





-----官方文档中的示例

char* myData = "Here is some saved data.\n";
 
int32 myLength = strlen(myData) + 1;
 
*save = (NPSavedData*) NPN_MemAlloc(sizeof(NPSavedData));
 
(*save)->len = myLength;
 
(*save)->buf = (void*) NPN_MemAlloc(myLength);
 
strcpy((*save)->buf, myData);






/* Define global variable to hold the user agent string. */
static char* userAgent = NULL;
 
/* Initialize function. */
NPError NP_Initialize(void)
{
  /* Get the user agent from the browser. */
  char* result = NPN_UserAgent();
  if (result == NULL) return NPERR_OUT_OF_MEMORY_ERROR;

  /* Allocate some memory so that you can keep a copy of it. */
  userAgent = (char*) NPN_MemAlloc(strlen(result) + 1);
  if (userAgent == NULL) return NPERR_OUT_OF_MEMORY_ERROR;

  /* Copy the string to your memory. */
  strcpy(userAgent, result);

  return NPERR_NO_ERROR;
}
 
/* Shutdown function */
NPError NP_Shutdown(void)
{
  /* Delete the memory you allocated. */
  if (userAgent != NULL)
    NPN_MemFree(userAgent);

  return NPERR_NO_ERROR;
}

--Mac
NPBool supportsCG = false;
NPError error = browser->getvalue(instance,
    NPNVsupportsCoreGraphicsBool,
    &supportsCG);
    
    
    
if (err == NPERR_NO_ERROR && supportsCG) {
    error = browser->setvalue(instance,
        NPNVpluginDrawingModel,
        (void *)NPDrawingModelCoreGraphics);
    if (err == NPERR_NO_ERROR && supportsCG) {
        /* Set state flags as needed to
           tell your own code to use Core Graphics
           drawing in the future. */
    }
} 
    
    
    
    
NPError setwindow_cb(NPP instance, NPWindow* npw) {
    ...
 
    NP_CGContext *npcontext = npw.window;
    CGContextRef context = npcontext.context;
    CGRect boundingBox = CGContextGetClipBoundingBox(context);
    
    
    
NPBool supportsCA = false;
NPError error = browser->getvalue(instance,
    NPNVsupportsCoreAnimationBool,
    &supportsCA);

    
NPN_MemFlush

---

char* myData = "\nHi Fred, this is a message from my plug-in!";
 
uint32 myLength = strlen(myData) + 1;
  
err = NPN_PostURLNotify(instance, "mailto:fred@example.com", NULL, myLength, myData, FALSE);



char* myData = "file:///c\/myDirectory/myFileName";
 
uint32 myLength = strlen(myData) + 1;
  
err = NPN_PostURL(instance, "ftp://fred@ftp.example.com/pub/", "response", myLength, myData, TRUE);


char* pPostData = "Content-Type:\tapplication/x-www-form-urlencoded\nContent-Length:\t17\n\nname=aaashun@gmail.com\n";
uint32 nPostDataLen = (uint32)strlen(pPostData);
NPN_PostURL(npInstance, "http://www.baidu.com","_blank", nPostDataLen, pPostData, FALSE);


--on MS Windows and Mac OS X
int plugin_major, plugin_minor;
   int netscape_major, netscape_minor;
   
   /* Find the version numbers. */
   NPN_Version(&plugin_major, &plugin_minor,
      &netscape_major, &netscape_minor);
   
   /* Use the netscape_minor version number: */
   /* Does this version support the windowless feature? */
   if (netscape_minor < NPVERS_HAS_WINDOWLESS) {
      /* Plug-in is running in a version of the Navigator */
      /* that does not support windowless plug-ins. */
      return FALSE;
   }
   else
      /* Plug-in is running in a Navigator version */
      /* that has windowless support */
      return TRUE;
      
      
NPBool supportsWindowless = FALSE;

NPError ret = NPN_GetValue(instance, NPNVSupportsWindowless, &supportsWindowless);

return ret == NPERR_NO_ERROR && supportsWindowless;     
 
 
 
void NPN_ReloadPlugins(NPBool reloadPages);


void NPN_Status(NPP instance, const char *message);//display your message on the status lin

 
 
 
 
 NPStream* stream;
char* myData = "<html><head><title>This is a message from my plug-in!</title></head><body><p><strong>This is a message from my plug-in!</strong></p></body>/html>";
int32 myLength = strlen(myData) + 1;
 
/* Create the stream. */
err = NPN_NewStream(instance, "text/html", "_blank", &stream);
 
/* Push data into the stream. */
err = NPN_Write(instance, stream, myLength, myData);
 
/* Delete the stream. */
err = NPN_DestroyStream(instance, stream, NPRES_DONE);
 
 
 
 
 
 
NPN_CreateObject()
NPN_HasMethod()
NPN_Invoke()



http://blog.csdn.net/zougangx/article/category/693231

=========Cordova-7

windows 10 要用 Visual Studio 2015 

npm install -g cordova
cordova create E:/tmp/VS2015_Cordova7 org.zhaojin VS2015_Cordova7
cd E:/tmp/VS2015_Cordova7
cordova platform add  windows      

VS2015打开CordovaApp.sln 

日志文件中报错 The source completed without providing data to receive.
警告: 程序集绑定日志记录被关闭。
要启用程序集绑定失败日志记录，请将注册表值 [HKLM\Software\Microsoft\Fusion!EnableLog] (DWORD)设置为 1   ，修改为1也没用


修改代码要在CordovaApp项目下，要先cordova plugin add 再写自己的代码，否则自己的代码会丢失！！！
 
windows 10 下
右击 win8.1 项目 -> set as startup project  (如选择 windows 10 (UAP) 就不行???)

 
----win10 支持的插件 
 

cordova plugin add cordova-plugin-camera 
cordova plugin add cordova-plugin-geolocation
cordova plugin add cordova-plugin-globalization
cordova plugin add cordova-plugin-battery-status
cordova plugin add cordova-plugin-contacts
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-file-transfer
cordova plugin add cordova-plugin-inappbrowser
cordova plugin add cordova-plugin-media
cordova plugin add cordova-plugin-media-capture
cordova plugin add cordova-plugin-network-information
cordova plugin add cordova-plugin-splashscreen
cordova plugin add cordova-plugin-vibration
cordova plugin add cordova-plugin-device 


cordova plugin ls
 
----cordova plugin add cordova-plugin-device 
	document.addEventListener("deviceready", onDeviceReady, false);
	function onDeviceReady() {
		var element = document.getElementById('deviceProperties');
		element.innerHTML = 'Device Cordova: '  + device.cordova + '<br />' +   //3.5.0
							'Device Platform: ' + device.platform + '<br />' +  //windows8
							'Device UUID: '     + device.uuid     + '<br />' +  //
							'Device Model: '    + device.model     + '<br />' +  //Win64
							'Device Version: '  + device.version  + '<br />';   //6.3.xxx
	}

-----cordova plugin add cordova-plugin-dialogs
	function alertDismissed() {
		out("you clicked the button");
	}

	function showAlert( )
	{
	  navigator.notification.alert(
				'You are the winner!',  // message
				alertDismissed,         // callback
				'Game Over',            // title
				'Done'                  // buttonName
			);
    }
	
-----cordova plugin add cordova-plugin-network-information
	document.addEventListener("offline", onOffline, false);//当3G,或者wifi网络打开,或者关闭时
    function onOffline() {
    	out("offline");
    }
    document.addEventListener("online", onOnline, false);
    function onOnline() {
    	out("online");
    }
    function checkConnection() {
        var networkState = navigator.connection.type;
        var states = {};
        states[Connection.UNKNOWN]  = 'Unknown connection';
        states[Connection.ETHERNET] = 'Ethernet connection'; 
        states[Connection.WIFI]     = 'WiFi connection';
        states[Connection.CELL_2G]  = 'Cell 2G connection';
        states[Connection.CELL_3G]  = 'Cell 3G connection';
        states[Connection.CELL_4G]  = 'Cell 4G connection';
        states[Connection.CELL]     = 'Cell generic connection';
        states[Connection.NONE]     = 'No network connection';
        out('Connection type: ' + states[networkState]);
    }
-----cordova plugin add cordova-plugin-camera


-----cordova plugin add cordova-plugin-geolocation





 如使用jquery.js 报用innerHTML不安全,要求使用toStaticHTML或createElement
 实际是appendChild()在断点处停止，这样jqueryMobile等插件就用不了
https://msdn.microsoft.com/en-us/library/windows/apps/hh849625.aspx

=========MPICH2 并行计算
 
 

