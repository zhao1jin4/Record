========MySQL
https://github.com/Go-SQL-Driver/MySQL/

package main 
import (
	"fmt"
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
)func main() {
	//安装  go get github.com/go-sql-driver/mysql 会git clone到GOPATH下
	//导入 import (	_ "github.com/go-sql-driver/mysql")
	db, err := sql.Open("mysql", "zh:123@tcp(127.0.0.1:3306)/mydb?charset=utf8")
	if err ==nil {
		fmt.Println("连接成功",db)
	}else {
		fmt.Println("连接错误",err)
	}
	defer db.Close()

	drop:="drop  table  if exists stu"
	create:="create table stu( id int ,name varchar(30))"
	execDB(db,drop)
	execDB(db,create)

	//--插入 动态参数
	stu:=[2][2] string{{"3","lisi"},{"4","王"}}
	stmt,_:=db.Prepare("insert into stu values (?,?)")
	for _,s:=range stu{
		stmt.Exec(s[0],s[1])
	}
	//--查询 一行数据
	var id,name string
	row:=db.QueryRow("select * from stu where id=4")
	row.Scan(&id,&name)
	fmt.Println(id,"--",name)

	//---查询 多行查询
	rows,_:=db.Query("select * from stu")
	for rows.Next(){
		rows.Scan(&id,&name)
		fmt.Println(id,"--",name)
	}

}
//create ,update,delete
func execDB(db *sql.DB,sql string){
	result,err:=db.Exec(sql)
	if err !=nil {
		fmt.Println("Exec执行失败",err)
		return
	}
	effectRow,err:=result.RowsAffected();
	if err !=nil {
		fmt.Println("RowsAffected执行失败",err)
		return
	}
	fmt.Printf("sql=%s影响行数:%d\n",sql,effectRow)
}




 

========ProtoBuf
https://pkg.go.dev/mod/google.golang.org/protobuf

https://github.com/golang/protobuf
https://github.com/golang/protobuf/tree/master/protoc-gen-go

---person.proto
syntax = "proto3";
option go_package = ".;myproto";
package protobuf;
message Person {
    repeated   int32 id = 1; //1表示字段顺序
    //repeated(可为0次，相当go中的slice)
    string name = 2;
    string email = 3;
}
---
安装 protoc 安装在PATH中
protoc -I=. --go_out=. person.proto (生成 person.pb.go文件)依赖于 protoc-gen-go 插件 
	 -IPATH, --proto_path=PATH 
  
go get  github.com/golang/protobuf/protoc-gen-go  
翻墙 git install
下载了 protoc-gen-go.exe 在$GOPATH/bin目录下 就是  https://github.com/golang/protobuf/tree/master/protoc-gen-go
下载了 $GOPATH\pkg\mod\google.golang.org\protobuf@v1.22.0 

github.com/golang/protobuf 依赖导入 google.golang.org/protobuf/xxx (运行时也找不到)
	并且在github上是没有encoding和runtime目录的，依赖如下
	"google.golang.org/protobuf/encoding/prototext"
	"google.golang.org/protobuf/encoding/protowire"
	"google.golang.org/protobuf/runtime/protoimpl"
---方式1
翻墙下载的 $GOPATH\pkg\mod\google.golang.org\protobuf@v1.22.0 放在src目录下，没有版本 

----方式2
其实是 https://github.com/protocolbuffers/protobuf-go 项目的源码
go get github.com/protocolbuffers/protobuf-go  下载到 $GOPATH\pkg\mod\github.com\protocolbuffers\protobuf-go@v1.23.0
放在src目录下，没有版本 ,修改包名~\go\src\google.golang.org\protobuf

import (
	"fmt"
	"github.com/golang/protobuf/proto"
	"myproto"
)
func main(){
	 myId:=[] int32 { *proto.Int32(101) }
	 msg:=& myproto.Person{
	 	 Name: *proto.String("李四"),
		 Id:  myId,
	 	 Email: *proto.String("aa@bb.com"),
	 }
	 encByte,err:=proto.Marshal(msg) //序列化
	if err!=nil {
		fmt.Print("序列化错误=",err)
		return
	}

	 myPerson:=myproto.Person{}
	 err=proto.Unmarshal(encByte,&myPerson)
	if err!=nil {
		fmt.Print("返序列化错误=",err)
		return
	}
	fmt.Println("myPerson=",myPerson)
	fmt.Println("myPerson.Name=",myPerson.Name)
}


	
========gRPC-go
有负载均衡功能,跟踪，监控，身份验证
 
https://github.com/grpc/grpc-go  
go get google.golang.org/grpc 下不了就

go get -u github.com/grpc/grpc-go 或  下载下来 放到 $GOPATH/sc 下修改目录为google.golang.org/grpc

还依赖于 golang.org/x/net  就是  https://github.com/golang/net  下载master很快，再移动目录
		golang.org/x/text  就是  https://github.com/golang/text 下载master很慢
		
		google.golang.org/genproto/ 就是 https://github.com/googleapis/go-genproto 有时会多次失败
		git clone https://github.com/googleapis/go-genproto  
		go get  github.com/googleapis/go-genproto  再移动目录
		
如报 runnerw.exe: CreateProcess failed with error 216: This version of %1 is not compatible with the version of Windows you're running. Check your computer's system information to see whether you need a x86 (32-bit) or x64 (64-bit) version of the program, and then contact the software publisher.
是因为main方法所在文件的包名不是main包
		
		
---vi hello.proto
syntax = "proto3";

option go_package = "gen";
//option go_package = ".;gen"; //用这个生成的包名为__gen,日志提示用这个

// The greeting service definition.
service Greeter {
  // Sends a greeting
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
  string name = 1;
}

// The response message containing the greetings
message HelloReply {
  string message = 1;
}
 
---

protoc -I=. --go_out=. hello.proto  生成的hello.pb.go文件，没有 service 声明的方法SayHello
protoc -I=. --go_out=plugins=grpc:. hello.proto  就有service 方法了,这个和示例代码中的还是有区别的
 
----server.go
 
package main

import (
	"context"
	"log"
	"net"

	"google.golang.org/grpc"
	pb "my/gen"
)

const (
	port = ":50051"
)

// server is used to implement helloworld.GreeterServer.
type server struct {
	pb.UnimplementedGreeterServer
}

// SayHello implements helloworld.GreeterServer
func (s *server) SayHello(ctx context.Context, in *pb.HelloRequest) (*pb.HelloReply, error) {
	log.Printf("Received: %v", in.GetName())
	return &pb.HelloReply{Message: "Hello " + in.GetName()}, nil
}

func main() {
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterGreeterServer(s, &server{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
----client.go

package main

import (
	"context"
	"log"
	"os"
	"time"

	"google.golang.org/grpc"
	pb "my/gen"
)

const (
	address     = "localhost:50051"
	defaultName = "world"
)

func main() {
	// Set up a connection to the server.
	conn, err := grpc.Dial(address, grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()
	c := pb.NewGreeterClient(conn)

	// Contact the server and print out its response.
	name := defaultName
	if len(os.Args) > 1 {
		name = os.Args[1]
	}
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	r, err := c.SayHello(ctx, &pb.HelloRequest{Name: name})
	if err != nil {
		log.Fatalf("could not greet: %v", err)
	}
	log.Printf("Greeting: %s", r.GetMessage())
}

---


======== 
