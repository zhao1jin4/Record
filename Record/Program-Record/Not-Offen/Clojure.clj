函数式编程 FP

clojure 编程语言 , 是LISP (LISt Processing)语言/方言 在JVM上实现 ,目前版本 1.8.0 
发音像 closure

storm 使用的语言 (Clojure DSL)

.clj扩展名 = C (C#), L (Lisp) and J (Java) 

REPL（read-eval-print-loop）
DSL 库

java -cp clojure-1.8.0.jar clojure.main
进入 user=> 提示附

user=> (+ 1 2 3)
6
user=> (javax.swing.JOptionPane/showMessageDialog nil "Hello World")
或者 java -cp clojure-1.8.0.jar clojure.main file.clj
 
构建工具 Leiningen  下载脚本执行 lein self-install 安装,会下文件到 C:\Users\zhaojin\.lein\self-installs\leiningen-2.7.1-standalone.jar
再执行 lein repl   会从中央仓库下载pom
控制台提示 nrepl://127.0.0.1:62423  端口不是固定的

$ lein new hello-world  命令建立项目,有project.clj文件,可用eclipse做
$ lein deps  命令可以下载project.clj文件中配置的依赖
	 [ring/ring-core "1.5.0"]
     [ring/ring-jetty-adapter "1.5.0"]
hello.clj
	(ns hello-world.core)
	(defn handler [request]
	{:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})
   
$ lein repl  命令启动一个交互式REPL,提示ctrl+D 退出,执行前存在.clj文件
=> (use 'ring.adapter.jetty)
=> (use 'hello-world.core)
=> (run-jetty handler {:port 3000})   ; handler 对应上面的定义,测试 http://localhost:3000/


clojure	 eclipse 插件 Counterclockwise ,有clojure菜单, 右击.clj文件 Run As-> clojure Application 即可运行
project.clj 文件中可添加依赖项,默认有
	[org.clojure/clojure "1.6.0"]   ;可修改为1.8.0

#_( 
	里面是多行注释
)

;创建了一个名为 divisible-by-3-or-5? 的函数,名字是以一个问号结尾的，用以表示此函数是一个断言，因它会返回 true 或 false, 函数只接受一个名为 num参数
(defn divisible-by-3-or-5? [num] (or (== (mod num 3) 0)(== (mod num 5) 0))) 

(println (reduce + (filter divisible-by-3-or-5? (range 1000))))
;range 函数 会创建一个由 0 开始，所有小于 1,000  的序列




;Fibonacci示例,会栈溢出
(defn fib [n] 
    (if (= n 0) 0 		;检查 n 是否为 0；如果是，就返回 0
        (if (= n 1) 1
            (+ (fib (- n 1)) (fib (- n 2))))))
			
(println ( fib 20) ) 



;惰性示例

(defn lazy-seq-fibo 
    ([] 									;像函数重载,无参数
        (concat [0 1] (lazy-seq-fibo 0 1))) ;获取序列 [0 1] 
    ([a b] 									;多参数
        (let [n (+ a b)] 					;let是变量赋值
            (lazy-seq 						;lazy-seq 宏被用来创建一个惰性序列
                (cons n (lazy-seq-fibo b n)))))) ; cons 函数接受一个元素和一个序列并通过将元素添加在序列之前来返回一个新序列,lazy-seq 宏确保了此函数(lazy-seq-fibo)将只在元素被访问的时候调用
			

(take 10 (lazy-seq-fibo)) ;显示前10个



(defn less-than-four-million? [n] (< n 4000000))

(println (reduce + 
    (filter even? 				;even测试一个数是否是偶数
        (take-while less-than-four-million? (lazy-seq-fibo)))))    ;take-while 当断言返回 false，它就停止从这个序列中获取



;闭包示例
(println (reduce + 
    (filter even? 
        (take-while (fn [n] (< n 4000000)) (lazy-seq-fibo)))))  ; fn 宏。这会创建一个匿名函数并返回此函数  




 

(println (reduce + 
    (filter even? 
        (take-while #(< % 4000000) (lazy-seq-fibo)))))  ; # 创建闭包,  函数的第一个参数使用了 % 符号,也可用 %1


;使用Java


(def big-num-str 					; def 宏来定义一个常量
    (str "73167176531330624919225119674426574742355
71636269561882670428252483600823257530420752963450"))




(def the-digits			
    (map #(Integer. (str %)) 			 ; str 函数来将这个 char 转变为字符串,java.lang.Integer 的构造函数 
        (filter #(Character/isDigit %) (seq big-num-str))))		;过滤器来消除换行 ,java.lang.Character 的静态方法 isDigit


(seq big-num-str)  ;显示值


(println (apply max 							;max 一般接受传递给它的多个元素，而不是单一一个序列; apply把序列转变为多个元素后 
    (map #(reduce * %)  							;每5个数做*
        (for [idx (range (count the-digits))] 				;变量 idx 绑定到一个从 0 到 N-1 的序列??????
            (take 5 (drop idx the-digits))))))			    ;take 生成一个新的序列,drop 两个参数都是序列??????
			
			
			
;; 定义自然数序列
(defn natuals []
 (iterate inc 1))

;; 定义奇数序列
(defn odds []
 (filter odd? (natuals)))

;; 定义偶数序列
(defn evens []
 (filter even? (natuals)))

;; 定义斐波那契数列
(defn fib []
 (defn fib-iter [a b]
 (lazy-seq (cons a (fib-iter b (+ a b)))))
 (fib-iter 0 1))


;; 打印前 10 个数
(println (take 10 (natuals)))
(println (take 10 (odds)))
(println (take 10 (evens)))
(println (take 10 (fib)))

;; 打印 1x2, 2x3, 3x4...
(println (take 10 (map * (natuals)
 (drop 1 (natuals))))) 
			
			
;------连接数据库 封装了JDBC  , DSL库 用 Korma  

project.clj 文件中添加依赖项
	[org.clojure/java.jdbc "0.3.6"]
	[mysql/mysql-connector-java "5.1.25"]
	[korma "0.3.0"]		
	
(ns mydb
(:use korma.db   ;引用 Korma
korma.core))		
			

(defdb korma-db (mysql {:db "test",
 :host "localhost",
 :port 3306,
 :user "user1",
 :password "user1"}))
 
 
(declare courses)    ;表名courses
(defentity courses)
 
 
 ;插入
(insert courses
 (values { :id "s-201", :name "SQL", :price 99.9, :online false, :days 30 }))
 
 
;查询
(select courses
 (where {:online false})
 (order :name :asc))
			
;------ Web开发	 (安全问题 如 XSS)	
Ring  把Servlet 接口转换为简单的函数接口

project.clj 文件中添加依赖项
 [ring/ring-core "0.3.4"]
 [ring/ring-jetty-adapter "0.3.4"]

 
 (ns myweb
 (:require [ring.adapter.jetty :as jetty]))

(defn handler [request]
 {:status 200,
 :headers {"Content-Type" "text/html"}
; :body "<h1>Hello, world.</h1>"
 :body (str request)   ;如果查看request内容
 })

(defn start-server []
 (jetty/run-jetty handler {:host "localhost",
 :port 3000}))

(start-server)
 
 ; 测试 http://localhost:3000
 
 
 
 
 
 