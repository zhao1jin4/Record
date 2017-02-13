
用Java写的,分布式,Key/Value

---客户端
java -jar kv-2.1.8/lib/kvclient.jar 显示版本



---服务端
配置
重命名 kv-2.1.8 为 KVHOME
mkdir -p KVROOT
java -jar KVHOME/lib/kvstore.jar makebootconfig -root KVROOT  -port 5000 -admin 5001 -host 127.0.0.1 -harange 5010,5020  -capacity 1 -num_cpus 0 -memory_mb 0
其中的-admin的5001端口是为web UI界面

linux 启动
nohup java -jar KVHOME/lib/kvstore.jar start -root KVROOT &  

jps -m 查看

http://127.0.0.1:5001/

java -jar KVHOME/lib/kvstore.jar runadmin -port 5000 -host 127.0.0.1  后出现 kv->提示符	
kv-> configure -name mystore

http://127.0.0.1:5001/ 就有变化

