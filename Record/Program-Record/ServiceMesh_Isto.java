service mesh 是Microservices的下一代  
Istio   由 Google、IBM 和 Lyft 联合开发，只支持 Kubernetes 平台
2020-04-23 稳定版本为 1.5.1
https://github.com/istio/istio/releases

Envoy是Istio数据平面核心组件



===================  Istio
 1.4 版本 支持 Kubernetes  1.15
Istio来自希腊语，英文意思是「sail」, 意为「启航」

https://github.com/istio/istio/releases 

install/kubernetes 目录是Kubernetes的YAML文件
samples/ 示例目录 
bin/istioctl  用来手动注入 Envoy 做为 sidecar proxy.

kubectl get svc -n istio-system
