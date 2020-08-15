
   
---------------------------------hadoop 子项目 ambari 是一个web工具 用于,监控,管理
 
只可对只定的64位linux 系统

源码安装依赖于 
	rpm-build(rpmbuild 命令) 在线下载安装 zypper install rpm-build
	npm(node.js的)
	brunch 			npm install -g brunch 在线安装
	python-setuptools ,官方下载 setuptools-0.6c11-py2.7.egg 并执行 sudo sh setuptools-0.6c11-py2.7.egg
	
Python2.7
g++
postgresql-server


---apache-ambari-2.7.3-src.tar.gz  
mvn versions:set -DnewVersion=2.7.3.0.0 
 
pushd ambari-metrics  (Adds a directory to the top of the directory stack,对应的有 popd )
mvn versions:set -DnewVersion=2.7.3.0.0
popd


提示要RHEL (CentOS 7) & SUSE (SLES 12 SP2 & SP3)
注意上面的依赖 
mvn -B clean install rpm:rpm -DnewVersion=2.7.3.0.0 -DbuildNumber=4295bb16c439cbc8fb0e7362f19768dde1477868 -DskipTests -Dpython.ver="python >= 2.6" 
会在线下载老的  node-v4.5.0-linux-x64.tar.gz,yarn-v0.23.2.tar.gz 

中间有报错？？？？

openSUSE-leap-15.1 下执行会生成
./ambari-project/target/rpm/ambari-project/RPMS/noarch/ambari-project-2.7.3.0-0.noarch.rpm
./target/rpm/ambari/RPMS/noarch/ambari-2.7.3.0-0.noarch.rpm

cd ambari-server/target/rpm/ambari-server/RPMS/noarch/     没这个目录(Ambari Server 项目被跳过了) 但会有  ambari-server/sbin/ambari-server 

zypper install ambari-server*.rpm    #This should also pull in postgres packages as well.

ambari-server setup

ambari-server start

cd ambari-agent/target/rpm/ambari-agent/RPMS/x86_64/
zypper install ambari-agent*.rpm

vi /etc/ambari-agent/ambari.ini 
[server]
hostname=localhost

ambari-agent start


http://<ambari-server-host>:8080   admin/admin
 
 
---apache-ambari-2.0.0-src.tar.gz

rpm -ivh ambari-server/target/rpm/ambari-server/RPMS/noarch/ambari-server-1.5.1-1.noarch.rpm
#yum(zypper SUSE用) install ambari-server*.rpm		会装 postgresql-server

禁用selinux
root 用户执行 ambari-server setup	 配置  PostgreSQL(默认用户 ambari/bigdata,数据库ambari) , JDK ,会启动postgresql
ambari-server start/stop

chkconfig ambari-server off

http://<ambari-server-host>:8080  初始用户名密码 admin / admin 


在 ambari-agent/target/rpm/ambari-agent/RPMS/x86_64/ 
yum(zypper) install ambari-agent*.rpm

--/etc/ambari-agent/ambari.ini 修改主机名
[server]
hostname=localhost

ambari-agent start
 
