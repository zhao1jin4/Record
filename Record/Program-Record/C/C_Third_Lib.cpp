﻿==========C openssl
windows 使用 
https://sourceforge.net/projects/openssl-for-windows/files/  是命令工具	OpenSSL-1.1.1h_win32.zip 	 	2020-12-04
 
https://slproweb.com/products/Win32OpenSSL.html  是开发包，有头文件 ，也有命令工具 Win64OpenSSL-1_1_1n.exe 和 源码最新版本一致

// -l crypto

#include <openssl/sha.h>
#include <openssl/md5.h>

void sha1(){
	SHA_CTX ctx;
	SHA1_Init(&ctx);//返回整数状态码

	const char * str="这是原始数据";
	size_t len=strlen(str);
	SHA1_Update(&ctx,str,len);//返回整数状态码
	//可多次udpate
	const char * str1="这是第二次数据";
	size_t len1=strlen(str1);
	SHA1_Update(&ctx,str1,len1);

	unsigned char * res=new unsigned char[SHA_DIGEST_LENGTH];

	SHA1_Final(res,&ctx);//返回整数状态码

	//结果转换
	char * hexRes=new char[SHA_DIGEST_LENGTH *2 +1];
	for(int i=0;i<SHA_DIGEST_LENGTH;i++){
		sprintf(&hexRes[i*2],"%02x",res[i]);
	}
	printf("SHA1 结果为=%s\n",hexRes);

}
void sha256(){
	const  char * str="这是原始数据";
	size_t len=strlen(str);
	unsigned char * res=new unsigned char[SHA256_DIGEST_LENGTH];
	//只可放一次数据
	SHA256((const unsigned char *)str,len,res);//返回指向结果的指针,强转不会改变数据

	//结果转换
	char * hexRes=new char[SHA256_DIGEST_LENGTH *2 +1];
	for(int i=0;i<SHA256_DIGEST_LENGTH;i++){
		sprintf(&hexRes[i*2],"%02x",res[i]);
	}
	printf("SHA256 结果为=%s\n",hexRes);
}
void md5(){
	const  char * str="这是原始数据";
	size_t len=strlen(str);
	unsigned char * res=new unsigned char[MD5_DIGEST_LENGTH]; 
	MD5((const unsigned char *)str,len,res);//强转不会改变数据

	//结果转换
	char * hexRes=new char[MD5_DIGEST_LENGTH *2 +1];
	for(int i=0;i<MD5_DIGEST_LENGTH;i++){
		sprintf(&hexRes[i*2],"%02x",res[i]);
	}
	printf("MD5 结果为=%s\n",hexRes);
}


/*分组密码模式
 * 1.ECB(最不建议用)
 * 2.CBC (常用，但要多一个初始化向量)
 * 3.CFB (要一个初始化向量)
 * 4.OFB (要一个初始化向量)
 * 5.CTR
*/
int des_ncbc(){
	 char *keystring = "this is my key";
	  DES_cblock key;
	  DES_key_schedule key_schedule;

	  //生成一个 key
	  DES_string_to_key(keystring, &key);
	  if (DES_set_key_checked(&key, &key_schedule) != 0) {
		  printf("convert to key_schedule failed.\n");
		  return -1;
	  }

	  //需要加密的字符串
	  unsigned char input[] = "this is a text being encrypted by openssl";
	  size_t len = (sizeof(input)+7)/8 * 8;// +7 表示不丢失原来的数据（向量和分组长相同，即8） ， 除8乘8的意思8的整数倍
	  	  
	  void  *outputTmp = malloc(len+1);
	  unsigned char *output =(unsigned char *)outputTmp;
	  //IV
	  DES_cblock ivec;//向量长度为8

	  //IV设置为0x0000000000000000
	  memset((char*)&ivec, 0, sizeof(ivec));

	  //加密
	  DES_ncbc_encrypt(input, output, sizeof(input), &key_schedule, &ivec, DES_ENCRYPT);//头文件注释说话 替代DES_cbc_encrypt

	  //输出加密以后的内容
	  printf("enc: ");
	  for (int i = 0; i < len; ++i)
		 printf("%02x", output[i]);
	  printf("\n");

	  memset((char*)&ivec, 0, sizeof(ivec));

	  //解密
	  DES_ncbc_encrypt(output, input, len, &key_schedule, &ivec, 0);

	  printf("dec : %s\n", input);

	  free(output);
}


DH 两个创始人名的首字母
双方都先知道两个数(被其它看到也没关系)，双方各自生成大的随机数，再做做一个运算，发送给对方（传送时要防止伪造，被其它看到也没关系，可以知道计算公式），各收到后双方再计算都可得到一个相同的值
DH密钥至少需要1024位，才能保证有足够的中、长期安全。
int dh_main()//p,g开始共用的
{
	int key_bits = 768;
	int ret;
	// 构造DH数据结构
	DH *d1 = NULL;
	d1=DH_new();
	// 生成d密钥参数，该密钥参数是可以公开的
	ret=DH_generate_parameters_ex(d1,key_bits,DH_GENERATOR_2,NULL);
	if(ret!=1)
	{
		printf("DH_generate_parameters_ex err!\n");
		return -1;
	}
	ret=DH_generate_key(d1);
	if(ret!=1)
	{
		printf("DH_generate_key err!\n");
		return -1;
	}

	// 获取d1的p,g,pub_key,pri_key
	const BIGNUM *d1p = NULL, *d1g = NULL;
	DH_get0_pqg(d1, &d1p, NULL, &d1g);
	const BIGNUM *d1pub_key = NULL, *d1priv_key = NULL;
	DH_get0_key(d1, &d1pub_key, &d1priv_key); 

	//----另一方
	// 构造DH数据结构
	DH *d2 = NULL;
	d2=DH_new();

	// p 和 g 为公开的密钥参数，因此可以拷贝,通过p,g参数可以构造d2
	BIGNUM *t_p = NULL, *t_g = NULL;
	t_p=BN_dup(d1p);//复制
	t_g=BN_dup(d1g);
	DH_set0_pqg(d2, t_p, NULL, t_g);
	// 生成公私钥,用于测试生成共享密钥
	ret=DH_generate_key(d2);
	if(ret!=1)
	{
		printf("DH_generate_key err!\n");
		return -1;
	}

	const BIGNUM *d2pub_key = NULL, *d2priv_key = NULL;
	DH_get0_key(d2, &d2pub_key, &d2priv_key);

	// 计算共享密钥:d1+d2pub_key,d2+d1pub_key生成相同的共享密钥
	unsigned char sharekey1[1024] = {0};
	unsigned char sharekey2[1024] = {0};
	int len1=DH_compute_key(sharekey1,d2pub_key,d1);
	int len2=DH_compute_key(sharekey2,d1pub_key,d2);
	if(len1!=len2)
	{
		printf("生成共享密钥失败\n");
		return -1;
	}
	if(memcmp(sharekey1,sharekey2,len1)!=0)
	{
		printf("生成共享密钥失败\n");
		return -1;
	} 

	DH_free(d1);
	DH_free(d2);
	return 0;
}

static unsigned char dh_g_2[] = {0x02}; 
static unsigned char dh_pa_768[] = {...}; 
static unsigned char dh_xa_768[] = {...	}; 
static unsigned char dh_xb_768[] = {...}; 
static unsigned char dh_except_b_pubkey_768[] = { ...	};
int dh2_main()//从已有数据初始化
{
	int key_bits = 768;
	int ret;
	// 构造DH数据结构
	DH *d1 = NULL;
	d1=DH_new();
	BIGNUM *p, *g, *pri1;
	p  = BN_bin2bn(dh_pa_768, sizeof(dh_pa_768), NULL);//如果最后一个参数为NULL返回一个新的
	g = BN_bin2bn(dh_g_2, sizeof(dh_g_2), NULL);
	pri1 = BN_bin2bn(dh_xa_768, sizeof(dh_xa_768), NULL);
	BIGNUM *t_p = NULL, *t_g = NULL;
	t_p=BN_dup(p);
	t_g=BN_dup(g);
	DH_set0_pqg(d1, t_p, NULL, t_g);
	DH_set0_key(d1, NULL, pri1);

	ret=DH_generate_key(d1);
	if(ret!=1)
	{
		printf("DH_generate_key err!\n");
		return -1;
	}

	// 获取d1的p,g,pub_key,pri_key 
	const BIGNUM *d1pub_key = NULL, *d1priv_key = NULL;
	DH_get0_key(d1, &d1pub_key, &d1priv_key);
 

	DH *d2 = NULL;
	d2=DH_new();
	BIGNUM *pub2,*pri2;

	pub2 = BN_bin2bn(dh_except_b_pubkey_768, sizeof(dh_except_b_pubkey_768), NULL);
	pri2 = BN_bin2bn(dh_xb_768, sizeof(dh_xb_768), NULL);
	DH_set0_pqg(d2, t_p, NULL, t_g);
	DH_set0_key(d2, pub2, pri2);

	ret=DH_generate_key(d2);
	if(ret!=1)
	{
		printf("DH_generate_key err!\n");
		return -1;
	}

	const BIGNUM *d2pub_key = NULL, *d2priv_key = NULL; 
	//使用前面已经有的公钥，测试
	d2pub_key=pub2; 
	
	// 计算共享密钥:d1+d2pub_key,d2+d1pub_key生成相同的共享密钥
	unsigned char sharekey1[1024] = {0};
	unsigned char sharekey2[1024] = {0};
	int len1=DH_compute_key(sharekey1,d2pub_key,d1);
	int len2=DH_compute_key(sharekey2,d1pub_key,d2);
	if(len1!=len2)
	{
		printf("生成共享密钥失败\n");
		return -1;
	}
	if(memcmp(sharekey1,sharekey2,len1)!=0)
	{
		printf("生成共享密钥失败\n");
		return -1;
	}
	return 0;
}

==========C 处理 json(DB返回数据做转换)
https://github.com/DaveGamble/cJSON


==========C 处理 yml

===========C 连接 MongoDB

=========== Redis C client  hiredis 
hiredis是官方的客户端
https://github.com/redis/hiredis


===========C连接MySQL Select 
https://dev.mysql.com/doc/index-connectors.html

g++ -o mysql_conn C_MySQLSelect_OK.C  -I ~/mysql-8.0.18-linux-glibc2.12-x86_64/include/ -L ~/mysql-8.0.18-linux-glibc2.12-x86_64/lib/  -l mysqlclient
运行就找不到库 export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:~/mysql-8.0.18-linux-glibc2.12-x86_64/lib  就可以了

//C 连接 MySQL OpenSUSE-leap-15.2 测试成功


//#include <windows.h> //如是运行在windows 下MinGW还要加windows.h
//对于libmysql.dll使用-l libmysql或者-lmysql,对于libmysql.lib只能使用-llibmysql,测试成功
//g++ -o mysql_conn C_MySQLSelect_OK.C  -I D:\Application\mysql-8.0.15-winx64\include -L D:\Application\mysql-8.0.15-winx64\lib   -llibmysql
//g++ -o mysql_conn C_MySQLSelect_OK.C  -I D:\Application\mysql-8.0.15-winx64\include -L D:\Application\mysql-8.0.15-winx64\lib  -l mysql
//windows 下用-l mysqlclient 不行的,win7 64位(mysql是64位的) 运行时报 找不到 LIBEAY32.dll, 网上说是 OpenSSL  1.0 之前的

//# /usr/local/mysql/bin/mysql_config --libs --cflags

---C_MySQLSelect_OK.C
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
		printf("Errorno=%d and  Error=%s\n",mysql_errno(&mysql),mysql_error(&mysql));
		exit(-1);
	}

	mysql_close(&mysql);
}

---C_CRUD.C
void my_ddl(MYSQL * my_connection,char * sql)
{
	int res = mysql_query(my_connection, sql);
	  if (!res) {
		 printf("DDL  %lu rows\n", (unsigned long)mysql_affected_rows(my_connection));
	  } else {
		 fprintf(stderr, "DDL error %d: %s\n", mysql_errno(my_connection),
											  mysql_error(my_connection));
	  }
}
void my_query_each(MYSQL * my_connection,char * sql){
	int res = mysql_query(my_connection, sql);
	  if (res) {
		 printf("SELECT error: %s\n", mysql_error(my_connection));
	  } else {
		   MYSQL_RES *res_ptr= mysql_use_result(my_connection);//一次返回一行结果用mysql_use_result
		     //就不能和mysql_data_seek,mysql_row_seek,mysql_row_tell一起使用了
		 if (res_ptr) {
			 MYSQL_ROW sqlrow;
			while ((sqlrow = mysql_fetch_row(res_ptr))) {
			   unsigned int field_count = 0;
			   while (field_count < mysql_field_count(my_connection)) {
			        printf("%s ", sqlrow[field_count]);
			        field_count++;
			   }
			   printf("\n");

			}
			mysql_free_result(res_ptr);
		 }
	  }
}
void my_display_header(MYSQL_RES * res_ptr) {
   MYSQL_FIELD *field_ptr;
   printf("Column details:\n");
   while ((field_ptr = mysql_fetch_field(res_ptr)) != NULL) {
      printf("\t Name: %s\n", field_ptr->name);
      printf("\t Type: ");
      if (IS_NUM(field_ptr->type)) {
         printf("Numeric field\n");
      } else {
         switch(field_ptr->type) {
            case FIELD_TYPE_VAR_STRING:
               printf("VARCHAR\n");
            break;
            case FIELD_TYPE_LONG:
               printf("LONG\n");
            break;
            default:
              printf("Type is %d, check in mysql_com.h\n", field_ptr->type);
         }
      }
      printf("\t Max width %ld\n", field_ptr->length);
      if (field_ptr->flags & AUTO_INCREMENT_FLAG)
         printf("\t Auto increments\n");
      printf("\n");
   }
}
void my_query_all_header(MYSQL * my_connection,char * sql){
	int res = mysql_query(my_connection, sql);
	  if (res) {
		 fprintf(stderr, "SELECT error: %s\n", mysql_error(my_connection));
	  } else {
		  MYSQL_RES * res_ptr = mysql_store_result(my_connection);//mysql_store_result 一次查所有记录
		 if (res_ptr) {
			my_display_header(res_ptr);
			MYSQL_ROW sqlrow;
			while ((sqlrow = mysql_fetch_row(res_ptr))) {
				 unsigned int field_count  = 0;
				   while (field_count < mysql_field_count(my_connection)) {
					  if (sqlrow[field_count])
						  printf("%s ", sqlrow[field_count]);
					  else
						  printf("NULL");
					  field_count++;
				   }
				   printf("\n");
			}
			if (mysql_errno(my_connection)) {
			 fprintf(stderr, "Retrive error: %s\n",
								mysql_error(my_connection));
			}
		 }
		 mysql_free_result(res_ptr);
	  }
}

void my_transaction_cn(MYSQL * my_connection){
	  printf("character set =%s\n",mysql_character_set_name(my_connection));
	  if (!mysql_set_character_set(my_connection, "utf8"))
	  {
	      printf("New client character set: %s\n",
	             mysql_character_set_name(my_connection));
	  }
	 mysql_autocommit(my_connection, 0);
	 my_ddl(my_connection,"INSERT INTO children(fname, age) VALUES('li',1)"); //因SQL是char*所不支持中文？
	 mysql_rollback(my_connection);
	 my_ddl(my_connection,"INSERT INTO children(fname, age) VALUES('wang', 2)");
	 mysql_commit(my_connection);
}
int main(int argc, char *argv[]) {
   MYSQL *conn_ptr;
   conn_ptr = mysql_init(NULL);//如果参数是NULL返回新的
   if (!conn_ptr) {
      fprintf(stderr, "mysql_init failed\n");
      return EXIT_FAILURE;
   }
   unsigned int timeout=7;
    mysql_options(conn_ptr,MYSQL_OPT_CONNECT_TIMEOUT,(const char * )&timeout); //连接超时时间
    mysql_options(conn_ptr,MYSQL_SET_CHARSET_NAME,"utf8");

    //报  Plugin caching_sha2_password could not be loaded:  的解决方式
    //ALTER USER zh@'%' IDENTIFIED WITH mysql_native_password   BY '123';
	char *host="127.0.0.1",*user = "zh", *pwd = "123", *dbname = "mydb";
	unsigned int port=3306;
    //char * sock="/tmp/mysql.sock"; //如果host值为localhost就会使用sock连接
    char * sock=NULL;

    MYSQL* conn_res = mysql_real_connect(conn_ptr, host, user, pwd,
    		dbname, port, sock, 0);
   if (conn_res) {
      printf("Connection success\n");
   } else {
      printf("Connection failed\n");
      if (mysql_errno(conn_ptr)) {
		   fprintf(stderr, "Connection error %d: %s\n", mysql_errno(conn_ptr), mysql_error(conn_ptr));
	   }
   }

   my_ddl(conn_ptr," drop tables if exists children;");

   	char * create_sql="CREATE TABLE children (\
   	   childno int(11)  NOT NULL auto_increment,\
   	   fname varchar(30),\
   	   age int(11),\
   	   PRIMARY KEY (childno)\
   	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
   my_ddl(conn_ptr,create_sql);

   my_ddl(conn_ptr,"INSERT INTO children(fname, age) VALUES('Ann', 3)");

   my_query_each(conn_ptr, "SELECT LAST_INSERT_ID()");
   my_query_all_header(conn_ptr, "SELECT childno, fname, age FROM children WHERE age > 2");
   my_transaction_cn(conn_ptr);//不支持中文？
   //SQL没有动态绑定参数


   mysql_close(conn_ptr);
   return EXIT_SUCCESS;
}


---C_MySQLTrans.C

===========C连接SQLite3
//MinGW要调试的话,必须把sqli3.dll放在PATH中可以找到才行
#include <stdlib.h>
#include <stdio.h>
#include <sqlite3.h>

//#pragma comment(lib, "sqlite.lib")//我把sqlite编译成了一个静态的lib文件。
// 源码安装sqlite 不要删make的文件夹，eclipse-debug时可以打开源码.c

//#define DB_FILE "~/sqliteDB"
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
	sqlite3_exec(handle,"insert into student(id,name)values(1,'lisi')",0,0,&error_msg);
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


	status=sqlite3_bind_text(stmt_insert,name_index,"zh",sizeof("zh"),SQLITE_TRANSIENT);//对text
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
			const unsigned char* name=sqlite3_column_text(stmt_sel,1);

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
	name = zh

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

==========zlib 处理 zip 压缩

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

 

