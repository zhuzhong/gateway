#网关系统

##为什么要开发网关系统
为了统一前端系统调用后端服务接口，以及为了更好的解决前后端开发分离的问题.

##功能

目前只支持GET,POST方法请求，所使用的协议及数据格式为http+json.


##关于鉴权
鉴权需要调用gateway_register工程的服务进行鉴权。下一步开发gateway_register工程中关于api注册及app订阅api功能。

##deviceToken获取调用方式

##appToken获取调用方式




##业务方法调用方式
前端请求统一的url地址/gateway/service.htm,然后根据请求方法的不同拼接不同的url参数或提交相应的请求体。
###业务方法的公共请求参数：
	appId   String  16  是   打包app的唯一标识  not null    
	apiId   String  64  是   API编码即api的唯一标识 not null 
	apiVersion  String  8   是   API版本号 not null 
	appToken    String  32  是   app授权令牌,用于授权  not null      
	timeStamp   String  19  是   时间戳,对应时间的毫秒数 not null    
	signMethod  String  8   是   生成服务请求签名字符串所使用的算法类型，目前仅支持MD5， 
	sign    String  32  是   服务请求的签名字符串  not null 
	deviceToken String  16  否   硬件标识token,app首次安装时发放的硬件唯一性标识
	userToken   String  16  否   用户token

###GET请求

对于get请求方法，则直接将相应的业务参数及公共的请求参数，串在url地址后面，类似这样的形式:http://localhost:8080/gateway/serviceInner.htm?appId=test&apiId=2&apiVersion=1.1.0&appToken=token&timeStamp=123456789&signMethod=md5&sign=223&deviceToken=444&userToken=66&format=json&testa=0000000 其中testa=000000为业务参数其他参数为公共的请求参数

###POST请求
   对于post请求，则相应格式如下：
   { "publAttrs":{}, "busiAttrs":{} }，其只publAttrs中放的是公共请求参数，busiAttrs为业务请求参数，并且格式为application/json，目前也只支持这种请求格式


