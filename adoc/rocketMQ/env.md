一、首先`RocketMQ`官网进行下载：https://rocketmq.apache.org/download/

二、解压

```
cd env/rocketmq/

unzip rocketmq-all-5.1.4-bin-release.zip 
```

三、修改配置：

rocketmq使用版本为5.1.4，在5.0引入了新模块Proxy，使用endpoint连接的便是proxy，默认端口为8081，需要修改。

conf/rmq-proxy.json添加以下配置：
```
{
  "grpcServerPort": "9878"
}
```

四、、启动nameserver

```
➜  rocketmq-all-5.1.4-bin-release sh ./bin/mqnamesrv

or

➜  rocketmq-all-5.1.4-bin-release nohup sh bin/mqnamesrv &
```

五、启动proxy和broker

```
➜  rocketmq-all-5.1.4-bin-release nohup sh bin/mqbroker -n localhost:9876 --enable-proxy &
```


参考：https://rocketmq.apache.org/zh/docs/quickStart/01quickstart/

> Proxy是RocketMQ5.X引入的新模块，使用新版本客户端`rocketmq-client-java`所填写的接入点endPoint便是Proxy的对外hosts，默认为8081。