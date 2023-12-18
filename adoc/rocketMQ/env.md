一、首先`RocketMQ`官网进行下载：https://rocketmq.apache.org/download/

二、解压

```
cd env/rocketmq/

unzip rocketmq-all-5.1.4-bin-release.zip 
```

三、启动nameserver

```
➜  rocketmq-all-5.1.4-bin-release sh ./bin/mqnamesrv

or

➜  rocketmq-all-5.1.4-bin-release nohup sh bin/mqnamesrv &
```

四、启动proxy和broker

```
➜  rocketmq-all-5.1.4-bin-release nohup sh bin/mqbroker -n localhost:9876 --enable-proxy &
```


参考：https://rocketmq.apache.org/zh/docs/quickStart/01quickstart/