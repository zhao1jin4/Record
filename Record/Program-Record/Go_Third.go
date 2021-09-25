---kubernetes使用Makefile做构建

========Text   GBK字符集处理
https://github.com/golang/text 


go get -u golang.org/x/text  #不行被墙了 下载源码吧，放在%USERPROFILE%\go\src\golang.org/x/text 
如 go env -w  GO111MODULE=on 和 go env -w GOPROXY=https://goproxy.cn,direct 
如项目目录 go mod init go_thirdlib  就可以


---gbk2utf8.go
package main

import (
	"bufio"
	"fmt"
	"io"
	"os"

	"golang.org/x/text/encoding/simplifiedchinese"
)

func main() {
	var fromFile string = "D:/tmp/my_gbk.txt"
	from, err := os.OpenFile(fromFile, os.O_RDONLY, os.ModePerm)
	if err != nil {
		fmt.Println(err)
		return
	}
	defer from.Close()
	reader := bufio.NewReader(from)

	for {

		data, err := reader.ReadBytes('\n')
		if err == io.EOF {
			break
		}
		var mylen int = len(data) * 4
		decodeBytes := make([]byte, mylen, mylen)
		decodeBytes, err = simplifiedchinese.GB18030.NewDecoder().Bytes(data)
		if err != nil {
			break
		}
		var str string
		str = string(decodeBytes)
		fmt.Printf("转换为GBK后为=%s", str) 
	}
}

----http2
go get golang.org/x/net/http2

package main

import (
	"fmt"
	"net/http"

	"golang.org/x/net/http2"
)

type MyHandler struct{}

func (h *MyHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello World!")
}

func main() {
	handler := MyHandler{}
	server := http.Server{
		Addr:    "127.0.0.1:8080",
		Handler: &handler,
	}
	http2.ConfigureServer(&server, &http2.Server{})
	server.ListenAndServeTLS("cert.pem", "key.pem")
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
	会自动下载 https://github.com/golang/protobuf 项目  放 github.com/golang/protobuf 也可手动做
	

cd  $GOPATH/src/github.com/golang/protobuf/protoc-gen-go
go install 生成了 protoc-gen-go.exe 在$GOPATH/bin目录下
			  自动 依赖 生成/下载 了 $GOPATH\pkg\mod\google.golang.org\protobuf@v1.23.0  可使用这个目录 复制到 src目录下(去版本号)
			  也可事先手工单独下载ttps://github.com/protocolbuffers/protobuf-go 项目,放在 ~\go\src\google.golang.org\protobuf
	 

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
有负载均衡功能,跟踪，监控，身份验证，也有gRPC gateway
 
https://github.com/grpc/grpc-go  
go get google.golang.org/grpc 下不了就

go get -u github.com/grpc/grpc-go 或  下载下来 放到 $GOPATH/sc 下修改目录为google.golang.org/grpc

还依赖于 golang.org/x/net  就是  https://github.com/golang/net  下载master很快，再移动目录
		golang.org/x/text  就是  https://github.com/golang/text 下载master很慢
		golang.org/x/sys   就是  https://github.com/golang/sys
		
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
------------------ 



---中文转拼音
https://github.com/mozillazg/go-pinyin

go get -u github.com/mozillazg/go-pinyin

import (
	"fmt"

	"github.com/mozillazg/go-pinyin"
)

hans := "中国人"
// 默认
a := pinyin.NewArgs()
fmt.Println(pinyin.Pinyin(hans, a))
// [[zhong] [guo] [ren]]

// 声调用数字表示
a.Style = pinyin.Tone2
fmt.Println(pinyin.Pinyin(hans, a))
// [[zho1ng] [guo2] [re2n]]
	
---生成二维码
https://github.com/skip2/go-qrcode

go get github.com/skip2/go-qrcode


import (
	"image/color"
	"io/ioutil"
	"os"

	"github.com/skip2/go-qrcode"
)

func main() {
	var png []byte
	png, err := qrcode.Encode("https://example.org", qrcode.Medium, 256) //生成二进制数组

	err = ioutil.WriteFile("D:/tmp/qrcode.png", png, os.ModePerm)
	if err != nil {
		panic(err)
	}
	//直接写文件
	err = qrcode.WriteFile("https://example.org", qrcode.Medium, 256, "D:/tmp/qrcode2.png")

	//黑色背景，白色为码
	// err = qrcode.WriteColorFile("https://example.org", qrcode.Medium, 256, color.Black, color.White, "D:/tmp/qrcode3.png")
	err = qrcode.WriteColorFile("https://example.org", qrcode.Medium, 256, color.RGBA{0, 255, 0, 200}, color.RGBA{0, 0, 255, 200}, "D:/tmp/qrcode3.png")
	if err != nil {
		panic(err)
	}
}

---PDF gopdf 
https://github.com/signintech/gopdf   有1.2K的星

go get -u github.com/signintech/gopdf


import (
	"log"

	"github.com/signintech/gopdf"
)

func main() {
	//var ttf = "c:\\WINDOWS\\Fonts\\SIMHEI.TTF" //设置了字体，中文正常
	var ttf = "./SIMHEI.TTF"
	pdf := gopdf.GoPdf{}

	//pdf.Start(gopdf.Config{PageSize: *gopdf.PageSizeA4})//简单的

	//找开要密码，可以禁止复制
	pdf.Start(gopdf.Config{
		PageSize: *gopdf.PageSizeA4,
		Protection: gopdf.PDFProtectionConfig{
			UseProtection: true,
			Permissions:   gopdf.PermissionsPrint | gopdf.PermissionsCopy | gopdf.PermissionsModify,
			OwnerPass:     []byte("123456"),
			UserPass:      []byte("123456789")},
	})

	pdf.AddPage()
	err := pdf.AddTTFFont("times", ttf)
	if err != nil {
		log.Print(err.Error())
		return
	}
	pdf.Image("react.png", 200, 50, nil)
	err = pdf.SetFont("times", "", 14)
	if err != nil {
		log.Print(err.Error())
		return
	}

	pdf.SetX(30)
	pdf.SetY(40)
	pdf.Text("Link to example.com")
	pdf.AddExternalLink("http://example.com/", 27.5, 28, 125, 15)

	pdf.SetX(30)
	pdf.SetY(70)
	pdf.Text("到第二页")
	pdf.AddInternalLink("anchor", 27.5, 58, 120, 15)

	pdf.AddPage()
	pdf.SetX(30)
	pdf.SetY(100)
	pdf.SetAnchor("anchor")
	pdf.Text("Anchor position")

	//画线
	pdf.SetLineWidth(2)
	pdf.SetLineType("dashed")
	pdf.Line(10, 30, 585, 30)
	//画椭圆
	pdf.SetLineWidth(1)
	pdf.Oval(100, 200, 500, 500)
	//画多边形
	pdf.SetStrokeColor(255, 0, 0)
	pdf.SetLineWidth(2)
	pdf.SetFillColor(0, 255, 0)
	pdf.Polygon([]gopdf.Point{{X: 10, Y: 30}, {X: 585, Y: 200}, {X: 585, Y: 250}}, "DF")

	//旋转文字，如做水层式半透明背景水印？
	pdf.SetX(100)
	pdf.SetY(100)
	pdf.Rotate(270.0, 100.0, 100.0)
	pdf.Text("X公司...") 
	pdf.WritePdf("d:/tmp/hello.all.pdf")

}


---PDF gofpdf 
https://github.com/jung-kurt/gofpdf   有3.8K的星 项目已经 archived，已经不维护了

---Excel  excelize  只对2007版本以后的xml格式
https://github.com/360EntSecGroup-Skylar/excelize   360做的 有8.6K的星
要求 Go 版本 1.15 以后

go get github.com/360EntSecGroup-Skylar/excelize/v2

--写到文件
import (
	"fmt"

	"github.com/360EntSecGroup-Skylar/excelize/v2"
)

func main() {
	f := excelize.NewFile() 
	index := f.NewSheet("Sheet2") 
	f.SetCellValue("Sheet2", "A2", "Hello world.") //按sheet名字去定位，不太好，而不是对象
	f.SetCellValue("Sheet1", "B2", 100) 
	//如何写下拉外表的选项
	f.SetActiveSheet(index) 
	if err := f.SaveAs("d:/tmp/Book1.xlsx"); err != nil {
		fmt.Println(err)
	}
}
---从文件读
func main() {
	f, err := excelize.OpenFile("D:/tmp/Book1.xlsx")
	if err != nil {
		fmt.Println(err)
		return
	}
	cell, err := f.GetCellValue("Sheet1", "B2") //指定单元格
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Println(cell)
	rows, err := f.GetRows("Sheet1")
	for _, row := range rows {
		for _, colCell := range row {
			fmt.Print(colCell, "\t")
		}
		fmt.Println()
	}
}

---Excel  xlsx
https://github.com/tealeg/xlsx	有4.9K的星

---go-yaml
https://github.com/go-yaml/yaml

go get gopkg.in/yaml.v2


package main

import (
	"fmt"
	"log"

	"gopkg.in/yaml.v2"
)

var data = `
a: Easy!
b:
  c: 2
  d: [3, 4]
`

// 字段要大写字母开头才会做转换
type T struct {
	A string
	B struct {
		RenamedC int   `yaml:"c"`
		D        []int `yaml:",flow"` //这里的",flow" 应该是数组以 [],默认是-
	}
}

func main() {
	t := T{}

	err := yaml.Unmarshal([]byte(data), &t)
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	fmt.Printf("--- t:\n%v\n\n", t)

	d, err := yaml.Marshal(&t)
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	fmt.Printf("--- t dump:\n%s\n\n", string(d)) //这里的数组是 []

	m := make(map[interface{}]interface{})

	err = yaml.Unmarshal([]byte(data), &m)
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	fmt.Printf("--- m:\n%v\n\n", m)

	d, err = yaml.Marshal(&m)
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	fmt.Printf("--- m dump:\n%s\n\n", string(d)) //这的数组是-
}


---kubernetes 使用的 yaml
https://github.com/kubernetes-sigs/yaml 也是基于 go-yaml 做了包装

go get sigs.k8s.io/yaml

--yaml_struct
//https://github.com/kubernetes-sigs/yaml
package main

import (
	"fmt"

	"sigs.k8s.io/yaml"
)

type Person struct {
	Name string `json:"name"` // Affects YAML field names too.
	Age  int    `json:"age"`
}

func main() {
	// Marshal a Person struct to YAML.
	p := Person{"John", 30}
	y, err := yaml.Marshal(p)
	if err != nil {
		fmt.Printf("err: %v\n", err)
		return
	}
	fmt.Println(string(y))
	/* Output:
	age: 30
	name: John
	*/

	// Unmarshal the YAML back into a Person struct.
	var p2 Person
	err = yaml.Unmarshal(y, &p2)
	if err != nil {
		fmt.Printf("err: %v\n", err)
		return
	}
	fmt.Println(p2)
	/* Output:
	{John 30}
	*/
}
----yaml_json 

import (
	"fmt"

	"sigs.k8s.io/yaml"
)

func main() {
	j := []byte(`{"name": "John", "age": 30}`)
	y, err := yaml.JSONToYAML(j)
	if err != nil {
		fmt.Printf("err: %v\n", err)
		return
	}
	fmt.Println(string(y))
	/* Output:
	age: 30
	name: John
	*/
	j2, err := yaml.YAMLToJSON(y)
	if err != nil {
		fmt.Printf("err: %v\n", err)
		return
	}
	fmt.Println(string(j2))
	/* Output:
	{"age":30,"name":"John"}
	*/
}
----------kubernetes 使用的 pflag
用来替代  flag 包
https://github.com/spf13/pflag
go get github.com/spf13/pflag


import (
	"fmt"

	flag "github.com/spf13/pflag"
)

var flagvar int

func init() {
	flag.IntVar(&flagvar, "files", 1234, "help message for flagname")
}

func main() {
	// go run  .\src\k8s\k8s_pflag.go  --flagname 11 --files 22
	//混合用 go run  .\src\k8s\k8s_pflag.go  --flagname=11 --files 22 -p 888 --auth
	var ip *int = flag.Int("flagname", 1234, "help message for flagname")

	var port *int = flag.IntP("port", "p", 8081, "port number")
	var authVar bool
	flag.BoolVarP(&authVar, "auth", "a", false, "is set password")

	flag.Parse()

	fmt.Println("ip has value ", *ip)
	fmt.Println("flagvar has value ", flagvar)

	fmt.Println("port ", *port)
	fmt.Println("authVar ", authVar)

}

-----------------restful 框架   go-restful 是 kubernetes  使用的 

https://github.com/emicklei/go-restful
go get github.com/emicklei/go-restful/v3 ,3版本支持Go module


https://github.com/emicklei/go-restful/blob/v3/examples/openapi/restful-openapi.go
的示例中使用了 github.com/emicklei/go-restful-openapi/v2 (支持openAPI2.0版本，最新是3.1版本)
kubernates中使用google的 openapiv2

go-restful-openapi 依赖于 
    go-restful
    go-openapi		 kubernates中使用

---restful-openapi.go

import (
	"log"
	"net/http"

	restfulspec "github.com/emicklei/go-restful-openapi/v2"
	restful "github.com/emicklei/go-restful/v3"
	"github.com/go-openapi/spec"
)

type UserResource struct {
	// normally one would use DAO (data access object)
	users map[string]User
}

func (u UserResource) WebService() *restful.WebService {
	ws := new(restful.WebService)
	ws.
		Path("/users").
		Consumes(restful.MIME_XML, restful.MIME_JSON).
		Produces(restful.MIME_JSON, restful.MIME_XML) // you can specify this per route as well

	tags := []string{"users"}

	ws.Route(ws.GET("/").To(u.findAllUsers).
		// docs
		Doc("get all users").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Writes([]User{}).
		Returns(200, "OK", []User{}))

	ws.Route(ws.GET("/{user-id}").To(u.findUser).
		// docs
		Doc("get a user").
		Param(ws.PathParameter("user-id", "identifier of the user").DataType("integer").DefaultValue("1")).
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Writes(User{}). // on the response
		Returns(200, "OK", User{}).
		Returns(404, "Not Found", nil))

	ws.Route(ws.PUT("/{user-id}").To(u.updateUser).
		// docs
		Doc("update a user").
		Param(ws.PathParameter("user-id", "identifier of the user").DataType("string")).
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Reads(User{})) // from the request

	ws.Route(ws.PUT("").To(u.createUser).
		// docs
		Doc("create a user").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Reads(User{})) // from the request

	ws.Route(ws.DELETE("/{user-id}").To(u.removeUser).
		// docs
		Doc("delete a user").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Param(ws.PathParameter("user-id", "identifier of the user").DataType("string")))

	return ws
}

// GET http://localhost:8080/users
//
func (u UserResource) findAllUsers(request *restful.Request, response *restful.Response) {
	list := []User{}
	for _, each := range u.users {
		list = append(list, each)
	}
	response.WriteEntity(list)
}

// GET http://localhost:8080/users/1
//
func (u UserResource) findUser(request *restful.Request, response *restful.Response) {
	id := request.PathParameter("user-id")
	usr := u.users[id]
	if len(usr.ID) == 0 {
		response.WriteErrorString(http.StatusNotFound, "User could not be found.")
	} else {
		response.WriteEntity(usr)
	}
}

// PUT http://localhost:8080/users/1
// <User><Id>1</Id><Name>Melissa Raspberry</Name></User>
//
func (u *UserResource) updateUser(request *restful.Request, response *restful.Response) {
	usr := new(User)
	err := request.ReadEntity(&usr)
	if err == nil {
		u.users[usr.ID] = *usr
		response.WriteEntity(usr)
	} else {
		response.WriteError(http.StatusInternalServerError, err)
	}
}

// PUT http://localhost:8080/users/1
// <User><Id>1</Id><Name>Melissa</Name></User>
//
func (u *UserResource) createUser(request *restful.Request, response *restful.Response) {
	usr := User{ID: request.PathParameter("user-id")}
	err := request.ReadEntity(&usr)
	if err == nil {
		u.users[usr.ID] = usr
		response.WriteHeaderAndEntity(http.StatusCreated, usr)
	} else {
		response.WriteError(http.StatusInternalServerError, err)
	}
}

// DELETE http://localhost:8080/users/1
//
func (u *UserResource) removeUser(request *restful.Request, response *restful.Response) {
	id := request.PathParameter("user-id")
	delete(u.users, id)
}

func main() {
	u := UserResource{map[string]User{}}
	restful.DefaultContainer.Add(u.WebService())

	config := restfulspec.Config{
		WebServices:                   restful.RegisteredWebServices(), // you control what services are visible
		APIPath:                       "/apidocs.json",
		PostBuildSwaggerObjectHandler: enrichSwaggerObject}
	restful.DefaultContainer.Add(restfulspec.NewOpenAPIService(config))

	// Optionally, you can install the Swagger Service which provides a nice Web UI on your REST API
	// You need to download the Swagger HTML5 assets and change the FilePath location in the config below.
	// Open http://localhost:8080/apidocs/?url=http://localhost:8080/apidocs.json
	http.Handle("/apidocs/", http.StripPrefix("/apidocs/", http.FileServer(http.Dir("/Users/emicklei/Projects/swagger-ui/dist"))))

	// Optionally, you may need to enable CORS for the UI to work.
	cors := restful.CrossOriginResourceSharing{
		AllowedHeaders: []string{"Content-Type", "Accept"},
		AllowedMethods: []string{"GET", "POST", "PUT", "DELETE"},
		CookiesAllowed: false,
		Container:      restful.DefaultContainer}
	restful.DefaultContainer.Filter(cors.Filter)

	log.Printf("Get the API using http://localhost:8080/apidocs.json")
	log.Printf("Open Swagger UI using http://localhost:8080/apidocs/?url=http://localhost:8080/apidocs.json")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func enrichSwaggerObject(swo *spec.Swagger) {
	swo.Info = &spec.Info{
		InfoProps: spec.InfoProps{
			Title:       "UserService",
			Description: "Resource for managing Users",
			Contact: &spec.ContactInfo{
				Name:  "john",
				Email: "john@doe.rp",
				URL:   "http://johndoe.org",
			},
			License: &spec.License{
				Name: "MIT",
				URL:  "http://mit.org",
			},
			Version: "1.0.0",
		},
	}
	swo.Tags = []spec.Tag{spec.Tag{TagProps: spec.TagProps{
		Name:        "users",
		Description: "Managing users"}}}
}

// User is just a sample type
type User struct {
	ID   string `json:"id" description:"identifier of the user"`
	Name string `json:"name" description:"name of the user" default:"john"`
	Age  int    `json:"age" description:"age of the user" default:"21"`
}
---

 
kubernetes  使用 "github.com/emicklei/go-restful/log"
 
----单元测试/Mock  
https://github.com/golang/mock/

kubernate中有使用  "github.com/golang/mock/gomock"  是mockgen生成的


Go version < 1.16 
GO111MODULE=on go get github.com/golang/mock/mockgen@v1.5.0

Go 1.16+  (要求 GO111MODULE=o)
go install github.com/golang/mock/mockgen@v1.5.0

会 $GOPATH\bin 下生成mockgen命令

文档
go doc github.com/golang/mock/gomock


mockgen database/sql/driver Conn,Driver  #导入路径  接口 
	提示 go get github.com/golang/mock/mockgen/model,输出到标准输出上


#go env -w  GO111MODULE=off 后 go test 才认$GOPATH下的目录
vscode 运行要以  "program": "${workspaceFolder}" 方式
项目中有 go.mod (go mod init xx,set GO111MODULE=on) 代码可不在$GOPATH下,使用 go get github.com/golang/mock/mockgen/model
代码放在 mygomock 目录中,目录叫gomock不行的
---foo.go
package mygomock

import "fmt"

type Foo interface {
	Bar(x int) int
}

func SUT(f Foo) {
	// ...
	var res = f.Bar(99)
	fmt.Println(res)


	res = f.Bar(101)
	fmt.Println(res)
} 
---
生成命令 
cd src/gomock

mocken . Foo   #也可用.当前目录 
mockgen -source=foo.go -destination=gen.go   Foo
生成代码手工移动当前目录下的gen目录(建立)
---gen/gen.go
 // Code generated by MockGen. DO NOT EDIT.
// Source: foo.go

// Package mock_mygomock is a generated GoMock package.
package gen

import (
	reflect "reflect"

	gomock "github.com/golang/mock/gomock"
)

// MockFoo is a mock of Foo interface.
type MockFoo struct {
	ctrl     *gomock.Controller
	recorder *MockFooMockRecorder
}

// MockFooMockRecorder is the mock recorder for MockFoo.
type MockFooMockRecorder struct {
	mock *MockFoo
}

// NewMockFoo creates a new mock instance.
func NewMockFoo(ctrl *gomock.Controller) *MockFoo {
	mock := &MockFoo{ctrl: ctrl}
	mock.recorder = &MockFooMockRecorder{mock}
	return mock
}

// EXPECT returns an object that allows the caller to indicate expected use.
func (m *MockFoo) EXPECT() *MockFooMockRecorder {
	return m.recorder
}

// Bar mocks base method.
func (m *MockFoo) Bar(x int) int {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "Bar", x)
	ret0, _ := ret[0].(int)
	return ret0
}

// Bar indicates an expected call of Bar.
func (mr *MockFooMockRecorder) Bar(x interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "Bar", reflect.TypeOf((*MockFoo)(nil).Bar), x)
}

---foo_test.go
package mygomock

import (
	gen "test/mygomock/gen" //包名以test/开头,就可以找到当前项目的目录
	"testing"
	"time"

	"github.com/golang/mock/gomock"
)

//生成命令 cd src/gomock && mocken . Foo
//mockgen -source=foo.go -destination=gen.go Foo
// vscode 运行要以  "program": "${workspaceFolder}" 方式
func TestFoo(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	m := gen.NewMockFoo(ctrl)

	// Does not make any assertions. Executes the anonymous functions and returns
	// its result when Bar is invoked with 99.
	m.
		EXPECT().
		Bar(gomock.Eq(99)).
		DoAndReturn(func(_ int) int {
			time.Sleep(1 * time.Second)
			return 101
		}).
		AnyTimes()

	// Does not make any assertions. Returns 103 when Bar is invoked with 101.
	m.
		EXPECT().
		Bar(gomock.Eq(101)).
		Return(103).
		AnyTimes()

	SUT(m)
}
---

--kubernetes 定时任务  实现 只是封装了timer包中的 

---日志文件滚动,参考kubernetes 实现
---日志收集，查看
logstash(ELK)官方没有go客户端
https://github.com/tsaikd/gogstash
https://github.com/heatxsink/go-logstash


Fluentd  https://docs.fluentd.org/quickstart  在 User case -> Application Logs 有java，但没有go (https://docs.fluentd.org/language-bindings/)


---security （login，jsonwebtoken）? 

 
---websocket
kuberntest 使用 golang.org/x/net/websocket  说有很多bug

教程 https://blog.51cto.com/xvjunjie/2509035
https://www.bookstack.cn/read/topgoer/0c28e578ae4624ce.md

 
https://github.com/gorilla/websocket
 


---数据库框架
Github上有 beego orm、gorm、sqlx、gorp、xorm
书中有 gorm、sqlx
 




---工作流 BPMN  
很多国人开发的

---- liquibase 好像没有go客户端
可以嵌入到构建工具中，如Jenkins


