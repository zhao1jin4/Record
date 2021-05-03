

========MySQL
https://github.com/Go-SQL-Driver/MySQL/

go get -u github.com/go-sql-driver/mysql  (-u = update  , use newer)

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
	//rows, _ := db.Query("select * from stu")
	stmt, _ = db.Prepare("select * from stu where id>?")
	rows, _ := stmt.Query(-1)
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




========Redis 



------go-redis
https://github.com/go-redis/redis

go env -w  GO111MODULE=on 
go mod init github.com/my/repo  这个是测试

go get github.com/go-redis/redis/v8   不能使用
go get github.com/go-redis/redis


go env -w  GO111MODULE=off 
go get github.com/go-redis/redis 
 依赖 go.opentelemetry.io/otel  被墙,可从 https://github.com/open-telemetry/opentelemetry-go 下载

放入 %USERPROFILE%/go/src/go.opentelemetry.io/otel



import (
	"context"
	"fmt"
	"time"

	"github.com/go-redis/redis"
)

var ctx = context.Background()

func main() {
	rdb := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		//Password: "", // no password set
		Password: "123",
		DB:       0, // use default DB
	})
	{
		err := rdb.Set(ctx, "key", "value", 0).Err()
		if err != nil {
			panic(err)
		}

		val, err := rdb.Get(ctx, "key").Result()
		if err != nil {
			panic(err)
		}
		fmt.Println("key", val)

		val2, err := rdb.Get(ctx, "key2").Result()
		if err == redis.Nil {
			fmt.Println("key2 does not exist")
		} else if err != nil {
			panic(err)
		} else {
			fmt.Println("key2", val2)
		}
		// Output: key value
		// key2 does not exist
	}
	{
		// SET key value EX 10 NX
		set, err := rdb.SetNX(ctx, "key", "value", 10*time.Second).Result()
		myPrint(set, err)

		//-- SET key value keepttl NX,   err为ERR syntax error ？？
		// set1, err := rdb.SetNX(ctx, "key", "value", redis.KeepTTL).Result() //KeepTTL=-1
		// myPrint(set1, err)

		// SORT list LIMIT 0 2 ASC
		vals, err := rdb.Sort(ctx, "list", &redis.Sort{Offset: 0, Count: 2, Order: "ASC"}).Result()
		myPrint(vals, err)

		// ZRANGEBYSCORE zset -inf +inf WITHSCORES LIMIT 0 2
		vals1, err := rdb.ZRangeByScoreWithScores(ctx, "zset", &redis.ZRangeBy{
			Min:    "-inf",
			Max:    "+inf",
			Offset: 0,
			Count:  2,
		}).Result()
		myPrint(vals1, err)
		// ZINTERSTORE out 2 zset1 zset2 WEIGHTS 2 3 AGGREGATE SUM
		vals2, err := rdb.ZInterStore(ctx, "out", &redis.ZStore{
			Keys:    []string{"zset1", "zset2"},
			Weights: []float64{2, 3},
		}).Result()
		myPrint(vals2, err)
		// EVAL "return {KEYS[1],ARGV[1]}" 1 "key" "hello"
		vals3, err := rdb.Eval(ctx, "return {KEYS[1],ARGV[1]}", []string{"key"}, "hello").Result()
		myPrint(vals3, err)
		// custom command
		res, err := rdb.Do(ctx, "set", "key", "value").Result()
		myPrint(res, err)
	}
}
func myPrint(res interface{}, err error) {
	if err != nil {
		fmt.Println("error")
		panic(err)
	} else {
		fmt.Println(res)
	}
}


------redigo
https://github.com/gomodule/redigo  
go get github.com/gomodule/redigo/redis


import (
	"fmt"
	"log"

	"github.com/gomodule/redigo/redis"
)

func main() {
	setdb := redis.DialDatabase(2)
	setPasswd := redis.DialPassword("123")

	c1, err := redis.Dial("tcp", "127.0.0.1:6379", setdb, setPasswd)
	if err != nil {
		log.Fatalln(err)
	}
	defer c1.Close()
	c2, err := redis.DialURL("redis://127.0.0.1:6379", setdb, setPasswd)
	if err != nil {
		log.Fatalln(err)
	}
	defer c2.Close()

	rec1, err := c1.Do("Get", "name")
	if rec1 != nil {
		fmt.Printf("---------  ")
		fmt.Println(rec1)
	}

	//pipline 管道,redis管道可以用来一次性执行多个命令
	//receive一次只从结果中拿出一个send的命令进行处理
	c2.Send("SET", "foo", "bar")
	c2.Send("GET", "foo")
	c2.Flush()
	v, err := c2.Receive()
	fmt.Println(v) //OK
	v2, err := c2.Receive()
	fmt.Println(string(v2.([]byte)))
	
	c2.Send("Get", "name")
	c2.Flush()
	rec2, err := c2.Receive()
	if err != nil {
		panic(err)
	}
	if rec2 != nil {
		fmt.Printf("---------  ")
		fmt.Println(string(rec2.([]byte)))
	}

	resset, err := redis.String(c1.Do("SET", "my_test", "redigo"))
	if err != nil {
		fmt.Println("set err")
	} else {
		fmt.Println(resset)
	}

	//获取value并转成字符串
	account_balance, err := redis.String(c1.Do("GET", "my_test"))
	if err != nil {
		fmt.Println("err while getting:", err)
	} else {
		fmt.Println(account_balance)
	}

	//对已有key设置5s过期时间
	n, err := redis.Int64(c1.Do("Expire", "my_test", 5))
	if err != nil {
		fmt.Println(n)
	} else if n != int64(1) {
		fmt.Println("failed")
	}

	n2, err := redis.Int64(c2.Do("TTL", "my_test"))
	if err != nil {
		fmt.Println(n2)
	} else if n != int64(1) {
		fmt.Print(err)
		fmt.Println("failed")
	}

	//删除key
	res2, err := c1.Do("DEL", "my_test")
	if err != nil {
		fmt.Print(err)
		fmt.Println("del err")
	} else {
		fmt.Println(res2)
	}

}



package main

import (
	"time"

	"github.com/gomodule/redigo/redis"
)

func main() {
	setdb := redis.DialDatabase(2)
	setPasswd := redis.DialPassword("123")

	var pool *redis.Pool = &redis.Pool{
		MaxIdle:     3,
		IdleTimeout: 240 * time.Second,
		// Dial or DialContext must be set. When both are set, DialContext takes precedence over Dial.
		Dial: func() (redis.Conn, error) { return redis.Dial("tcp", ":6379", setdb, setPasswd) },
	}
	//使用池
	pool.Close()
}



------------ 分布式锁Go实现使用 Redsync 
(Redis官方项目https://redis.io/topics/distlock)
https://github.com/go-redsync/redsync
	会安装两个驱动，go-redis，redigo，但你只使用一个即可，都在Redis官方有带黄星的项目

#go env -w  GO111MODULE=on 
#go get github.com/go-redsync/redsync/v4

go env -w  GO111MODULE=off
go get github.com/go-redsync/redsync

//------redlock的实现，goredis
package main 
import (
	//goredislib "github.com/go-redis/redis/v8"
	// "github.com/go-redsync/redsync/v4"
	// "github.com/go-redsync/redsync/v4/redis/goredis/v8"

	goredislib "github.com/go-redis/redis"
	"github.com/go-redsync/redsync"
	"github.com/go-redsync/redsync/redis/goredis/v8"
)
//redlock的实现 goredis,官方示例,测试启两个，都可以得到锁？？？
func main() {
	// Create a pool with go-redis (or redigo) which is the pool redisync will
	// use while communicating with Redis. This can also be any pool that
	// implements the `redis.Pool` interface.
	client := goredislib.NewClient(&goredislib.Options{
		Addr:     "localhost:6379",
		Password: "123",
		DB:       0,
	})
	pool := goredis.NewPool(client) // or, pool := redigo.NewPool(...)

	// Create an instance of redisync to be used to obtain a mutual exclusion
	// lock.
	rs := redsync.New(pool)

	// Obtain a new mutex by using the same name for all instances wanting the
	// same lock.
	mutexname := "my-global-mutex"
	mutex := rs.NewMutex(mutexname)

	// Obtain a lock for our given mutex. After this is successful, no one else
	// can obtain the same lock (the same mutex name) until we unlock it.
	if err := mutex.Lock(); err != nil {
		panic(err)
	}

	// Do your work that requires the lock.

	// Release the lock so other processes or threads can obtain a lock.
	if ok, err := mutex.Unlock(); !ok || err != nil {
		panic("unlock failed")
	}
}
//-----redlock的实现，redigo
package main

import (
	"time"

	"github.com/go-redsync/redsync"
	"github.com/go-redsync/redsync/redis/redigo"
	"github.com/gomodule/redigo/redis"
)

//redlock的实现 redigo,测试启两个，都可以得到锁？？？
func main() {
	setdb := redis.DialDatabase(2)
	setPasswd := redis.DialPassword("123")
	var innerPool *redis.Pool = &redis.Pool{
		MaxIdle:     3,
		IdleTimeout: 240 * time.Second,
		// Dial or DialContext must be set. When both are set, DialContext takes precedence over Dial.
		Dial: func() (redis.Conn, error) { return redis.Dial("tcp", ":6379", setdb, setPasswd) },
	}

	pool := redigo.NewPool(innerPool)
	rs := redsync.New(pool)
	// Obtain a new mutex by using the same name for all instances wanting the
	// same lock.
	mutexname := "my-global-mutex"
	mutex := rs.NewMutex(mutexname)

	// Obtain a lock for our given mutex. After this is successful, no one else
	// can obtain the same lock (the same mutex name) until we unlock it.
	if err := mutex.Lock(); err != nil {
		panic(err)
	}

	// Do your work that requires the lock.

	// Release the lock so other processes or threads can obtain a lock.
	if ok, err := mutex.Unlock(); !ok || err != nil {
		panic("unlock failed")
	}
}


========Etcd Go
API好像没有分布式锁 ？

https://etcd.io/docs/current/integrations/#go
https://github.com/etcd-io/etcd/tree/master/client/v3 是官方维护的

go get go.etcd.io/etcd/client/v3

etcd v3 uses gRPC for remote procedure calls  "grpc-go"

设置PROXY 被墙的依赖
google.golang.org/genproto/googleapis/api/annotations  (gRPC-go里有依赖 genproto)
google.golang.org/grpc 和下面的很多

package main

import (
	"context"
	"fmt"
	"time"

	clientv3 "go.etcd.io/etcd/client/v3"
)

/*
如单机只要./etcd即可
./etcdctl  del sample_key
./etcdctl get sample_key

集群式
TOKEN=token-01
CLUSTER_STATE=new
NAME_1=machine-1
NAME_2=machine-2
NAME_3=machine-3
HOST_1=localhost
HOST_2=localhost
HOST_3=localhost
CLUSTER=${NAME_1}=http://${HOST_1}:2380,${NAME_2}=http://${HOST_2}:2381,${NAME_3}=http://${HOST_3}:2382


# For machine 1
THIS_NAME=${NAME_1}
THIS_IP=${HOST_1}
etcd --data-dir=data.etcd1 --name ${THIS_NAME} \
	--initial-advertise-peer-urls http://${THIS_IP}:2380 --listen-peer-urls http://${THIS_IP}:2380 \
	--advertise-client-urls http://${THIS_IP}:2379 --listen-client-urls http://${THIS_IP}:2379 \
	--initial-cluster ${CLUSTER} \
	--initial-cluster-state ${CLUSTER_STATE} --initial-cluster-token ${TOKEN}

类似再加两节点，建立集群

# For machine 2
THIS_NAME=${NAME_2}
THIS_IP=${HOST_2}
etcd --data-dir=data.etcd2 --name ${THIS_NAME} \
	--initial-advertise-peer-urls http://${THIS_IP}:2381 --listen-peer-urls http://${THIS_IP}:2381 \
	--advertise-client-urls http://${THIS_IP}:22379 --listen-client-urls http://${THIS_IP}:22379 \
	--initial-cluster ${CLUSTER} \
	--initial-cluster-state ${CLUSTER_STATE} --initial-cluster-token ${TOKEN}

# For machine 3
THIS_NAME=${NAME_3}
THIS_IP=${HOST_3}
etcd --data-dir=data.etcd3 --name ${THIS_NAME} \
	--initial-advertise-peer-urls http://${THIS_IP}:2382 --listen-peer-urls http://${THIS_IP}:2382 \
	--advertise-client-urls http://${THIS_IP}:32379 --listen-client-urls http://${THIS_IP}:32379 \
	--initial-cluster ${CLUSTER} \
	--initial-cluster-state ${CLUSTER_STATE} --initial-cluster-token ${TOKEN}


ENDPOINTS=http://$HOST_1:2379,http://$HOST_2:22379,http://$HOST_3:32379
./etcdctl --endpoints=$ENDPOINTS get sample_key
./etcdctl --endpoints=$ENDPOINTS ls
*/

 
func main() {
	cli, err := clientv3.New(clientv3.Config{
		Endpoints: []string{"localhost:2379", "localhost:22379", "localhost:32379"},
		//Endpoints:   []string{"localhost:2379"},
		DialTimeout: 5 * time.Second,
	})
	if err != nil {
		// handle error!
		panic(err)
	}

	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	resp, err := cli.Put(ctx, "sample_key", "sample_value") 
	defer cancel()
	if err != nil {
		// handle error!
		panic(err)
	}
	fmt.Println(resp)
	// use the response

	defer cli.Close()
}



========MongoDB Go
https://docs.mongodb.com/drivers/go/
https://github.com/mongodb/mongo-go-driver#usage

mkdir goproj
cd goproj
#要求 go env -w  GO111MODULE=on
go mod init goproj 生成 go.mod (是依赖) 和 go.sum (是依赖每个go.mod文件的hash码)
go get go.mongodb.org/mongo-driver/mongo  下载到了$GOPATH/pkg下

在这个目录下建立src/mongo_crud.go文件(项目目录有 go.mod 和 go.sum )

package main

import (
	"context"
	"fmt"
	"time"

	"go.mongodb.org/mongo-driver/mongo" //vscode中ctrl+点击 提示是否打开外部链接，但有弹窗显示在$GOPATH/pkg下,可以运行
	"go.mongodb.org/mongo-driver/mongo/options"
	"go.mongodb.org/mongo-driver/mongo/readpref"
)

func main() {
	// Replace the uri string with your MongoDB deployment's connection string.
	//uri := "mongodb+srv://<username>:<password>@<cluster-address>/test?w=majority"
	uri := "mongodb://zh:123@127.0.0.1:27017/reporting"
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	client, err := mongo.Connect(ctx, options.Client().ApplyURI(uri))
	if err != nil {
		panic(err)
	}
	defer func() {
		if err = client.Disconnect(ctx); err != nil {
			panic(err)
		}
	}()
	// Ping the primary
	if err := client.Ping(ctx, readpref.Primary()); err != nil {
		panic(err)
	}
	fmt.Println("Successfully connected and pinged.")

	//insert
	collection := client.Database("reporting").Collection("numbers")
	ctx, cancel = context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	res, err := collection.InsertOne(ctx, bson.D{{"name", "pi"}, {"value", 3.14159}})
	id := res.InsertedID
	fmt.Println("inserted id=", id)
	//query 多行
	ctx, cancel = context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	cur, err := collection.Find(ctx, bson.D{})
	if err != nil {
		log.Fatal(err)
	}
	defer cur.Close(ctx)
	for cur.Next(ctx) {
		var result bson.D
		err := cur.Decode(&result)
		if err != nil {
			log.Fatal(err)
		}
		// do something with result....
		fmt.Println(result)
	}
	if err := cur.Err(); err != nil {
		log.Fatal(err)
	}
	//query 一行
	var result struct {
		Value float64
	}
	filter := bson.D{{"name", "pi"}}
	ctx, cancel = context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	err = collection.FindOne(ctx, filter).Decode(&result)
	if err == mongo.ErrNoDocuments {
		// Do something when no record was found
		fmt.Println("record does not exist")
	} else {
		log.Fatal(err)
	}
	fmt.Println(result)
}



========rabbitmq  Go
https://www.rabbitmq.com/devtools.html
https://github.com/streadway/amqp  另一个是http式的，就不先不用了

go get github.com/streadway/amqp 如有 go.mod 文件会自动增加的

https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/中的示例

--queue_send.go
package main

import (
	"log"

	"github.com/streadway/amqp"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"hello", // name
		false,   // durable
		false,   // delete when unused
		false,   // exclusive
		false,   // no-wait
		nil,     // arguments
	)
	failOnError(err, "Failed to declare a queue")

	body := "Hello World!"
	err = ch.Publish(
		"",     // exchange
		q.Name, // routing key
		false,  // mandatory
		false,  // immediate
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(body),
		})
	failOnError(err, "Failed to publish a message")
	log.Printf(" [x] Sent %s", body)
}

---queue_receive.go
package main

import (
	"log"

	"github.com/streadway/amqp"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"hello", // name
		false,   // durable
		false,   // delete when unused
		false,   // exclusive
		false,   // no-wait
		nil,     // arguments
	)
	failOnError(err, "Failed to declare a queue")

	msgs, err := ch.Consume(
		q.Name, // queue
		"",     // consumer
		true,   // auto-ack
		false,  // exclusive
		false,  // no-local
		false,  // no-wait
		nil,    // args
	)
	failOnError(err, "Failed to register a consumer")

	forever := make(chan bool)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever
}

---fanout_send.go
package main

import (
	"log"
	"os"
	"strings"

	"github.com/streadway/amqp"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

//go run emit_log.go hello world
//go run src/rabbitmq_conn/fanout_send.go hello world
func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare(
		"logs",   // name
		"fanout", // type
		true,     // durable
		false,    // auto-deleted
		false,    // internal
		false,    // no-wait
		nil,      // arguments
	)
	failOnError(err, "Failed to declare an exchange")

	body := bodyFrom(os.Args)
	err = ch.Publish(
		"logs", // exchange
		"",     // routing key
		false,  // mandatory
		false,  // immediate
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(body),
		})
	failOnError(err, "Failed to publish a message")

	log.Printf(" [x] Sent %s", body)
}

func bodyFrom(args []string) string {
	var s string
	if (len(args) < 2) || os.Args[1] == "" {
		s = "hello"
	} else {
		s = strings.Join(args[1:], " ")
	}
	return s
}


---fanout_receive.go
package main

import (
	"log"

	"github.com/streadway/amqp"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare(
		"logs",   // name
		"fanout", // type
		true,     // durable
		false,    // auto-deleted
		false,    // internal
		false,    // no-wait
		nil,      // arguments
	)
	failOnError(err, "Failed to declare an exchange")

	q, err := ch.QueueDeclare(
		"",    // name
		false, // durable
		false, // delete when unused
		true,  // exclusive
		false, // no-wait
		nil,   // arguments
	)
	failOnError(err, "Failed to declare a queue")

	err = ch.QueueBind(
		q.Name, // queue name
		"",     // routing key
		"logs", // exchange
		false,
		nil)
	failOnError(err, "Failed to bind a queue")

	msgs, err := ch.Consume(
		q.Name, // queue
		"",     // consumer
		true,   // auto-ack
		false,  // exclusive
		false,  // no-local
		false,  // no-wait
		nil,    // args
	)
	failOnError(err, "Failed to register a consumer")

	forever := make(chan bool)

	go func() {
		for d := range msgs {
			log.Printf(" [x] %s", d.Body)
		}
	}()

	log.Printf(" [*] Waiting for logs. To exit press CTRL+C")
	<-forever
}



========Neo4j Go
https://github.com/neo4j/neo4j-go-driver
https://neo4j.com/developer/go/
go get github.com/neo4j/neo4j-go-driver/v4


import (
	"fmt"

	"github.com/neo4j/neo4j-go-driver/v4/neo4j"
)

//https://github.com/neo4j/neo4j-go-driver 的示例
//查询用 match (x:Item) return x
func main() {
	dbUri := "neo4j://localhost:7687"
	driver, err := neo4j.NewDriver(dbUri, neo4j.BasicAuth("neo4j", "myneo4j", ""))
	if err != nil {
		panic(err)
	}
	defer driver.Close()
	item, err := insertItem(driver)
	if err != nil {
		panic(err)
	}
	fmt.Printf("%v\n", item)
}

func insertItem(driver neo4j.Driver) (*Item, error) {
	session := driver.NewSession(neo4j.SessionConfig{})
	defer session.Close()
	result, err := session.WriteTransaction(createItemFn)
	if err != nil {
		return nil, err
	}
	return result.(*Item), nil
}

func createItemFn(tx neo4j.Transaction) (interface{}, error) {
	records, err := tx.Run("CREATE (n:Item { id: $id, name: $name }) RETURN n.id, n.name", map[string]interface{}{
		"id":   1,
		"name": "Item 1",
	})
	if err != nil {
		return nil, err
	}
	record, err := records.Single()
	if err != nil {
		return nil, err
	}
	return &Item{
		Id:   record.Values[0].(int64),
		Name: record.Values[1].(string),
	}, nil
}

type Item struct {
	Id   int64
	Name string
}


 
//https://neo4j.com/developer/go/ 示例
//查询用 match(a:Greeting) return a
func helloWorld(uri, username, password string) (string, error) {
	driver, err := neo4j.NewDriver(uri, neo4j.BasicAuth(username, password, ""))
	if err != nil {
		return "", err
	}
	defer driver.Close()

	session := driver.NewSession(neo4j.SessionConfig{AccessMode: neo4j.AccessModeWrite}) //写模式
	defer session.Close()

	greeting, err := session.WriteTransaction(func(transaction neo4j.Transaction) (interface{}, error) {
		result, err := transaction.Run(
			"CREATE (a:Greeting) SET a.message = $message RETURN a.message + ', from node ' + id(a)",
			map[string]interface{}{"message": "hello, world"})
		if err != nil {
			return nil, err
		}

		if result.Next() {
			return result.Record().Values[0], nil
		}

		return nil, result.Err()
	})
	if err != nil {
		return "", err
	}

	return greeting.(string), nil
}


========hazelcast
https://hazelcast.org/imdg/clients-languages/go/

go get github.com/hazelcast/hazelcast-go-client



========Kafka 官方没有Go客户端
https://github.com/Shopify/sarama
https://github.com/segmentio/kafka-go



---Kubernetes client Go
如读 config,secret

https://github.com/kubernetes/client-go




