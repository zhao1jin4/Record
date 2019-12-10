Django   Python的 Web 框架

=====================Scrapy
windows下目前版本是 Scrapy-1.5.0  
pip3 install Scrapy  				windows下要 Visual C++ 14.0 (VC2015)
windows下安装成功

linux (CentOS7) 下报 pip is configured with locations that require TLS/SSL, however the ssl module in Python is not available.
yum install openssl-devel 
重新源码安装Python3.6.4
./configure --with-ensurepip=install --enable-optimizations 
再下载Scrapy又报 Could not find a version that satisfies the requirement Twisted>=13.1.0 (from Scrapy) (from versions: )
	No matching distribution found for Twisted>=13.1.0 (from Scrapy)
 
	pip3 install Twisted 报版本不足
	https://pypi.python.org/pypi/ 中输入 Twisted 搜索 有Twisted 17.9.0
	cd Twisted-17.9.0
	python3 setup.py install  会在线下安装一些东西
linux (CentOS7) 下安装成功



=====================TensorFlow
windows下目前版本是 tensorflow-1.5.0
pip3 install --upgrade tensorflow 	默认CPU 
pip3 install --upgrade tensorflow-gpu

 
验证安装 python>>>
>>> import tensorflow as tf  
#windows  安装gpu导入都不行了??
 Could not find 'cudart64_90.dll'. TensorFlow requires that this DLL be installed in a directory that is named in your %PATH% environment variable. Download and install CUDA 9.0 from this URL: https://developer.nvidia.com/cuda-toolkit
 
 
 https://developer.nvidia.com/cuda-downloads  目前最新的是9.1版本,windows,linux,mac不同系统的都有,windows 10的版本有1.4G
 
 
>>> hello = tf.constant('Hello, TensorFlow!')
>>> sess = tf.Session()   #windows 下只安装CPU，这里报 TensorFlow binary was not compiled to use: AVX AVX2
>>> print(sess.run(hello))
>>> a = tf.constant(10)
>>> b = tf.constant(32)
>>> sess.run(a + b)
42
>>> sess.close()


--linux  (CentOS7) 源码安装 TensorFlow
https://github.com/tensorflow/tensorflow/releases  有1.5源码

要 bazel 是Google内部用来构建自己的服务器端软件的工具
	yum-config-manager   --add-repo    https://copr.fedorainfracloud.org/coprs/vbatts/bazel/repo/epel-7/vbatts-bazel-epel-7.repo
	yum install bazel   会依赖安装 java-1.8.0-openjdk

	https://github.com/bazelbuild/bazel/releases  下也有二进制包，如windows


./configure





-----------------------------kafka-python

可python 3.6
https://pypi.org/project/kafka-python/
pip install kafka-python


=====================Selenium  自动化测试


