使用mysql做元数据安装hive，**请确保已经根据 `mysql/env`中的教程安装过mysql。**

使用hadoop-hdfs作为底层文件存储，**请确保已经根据bigdata/hadoop/env中的教程安装过hadoop。**

### 一、首先修改hadoop中hdfs配置（hive使用hadoop中的hdfs作为底层的分布式文件存储组件。

---

`hadoop-3.2.0/etc/hadoop/hdfs-site.xml` 添加配置：
```
<!-- 允许代理任何主机 -->
<property>
    <name>hadoop.proxyuser.hadoop.hosts</name>
    <value>*</value></property>
<property>
    <name>hadoop.proxyuser.hadoop.groups</name>
    <value>*</value>
</property>
```

### 二、安装hive本体

1. 首先去 `https://archive.apache.org/dist/hive/` 下载hive，根据自己hadoop版本进行选择，笔者使用`hadoop-3.2.0`，因此选择`hive-3.1.3`.
    - 如果网太卡，可以试试腾讯镜像网站：`https://mirrors.cloud.tencent.com/apache/`


### 三、配置mysql元数据 

从：https://central.sonatype.com/artifact/mysql/mysql-connector-java/versions 下载mysql驱动包，本处选择8.0.26版本。

将驱动包放入hive中：
```
cp mysql-connector-java-8.0.26.jar ~/Dev/env/hive/apache-hive-3.1.3-bin/lib
```

初始化元数据库，执行SQL：
```
CREATE DATABASE hive CHARSET UTF8;
```

### 四、修改配置

#### hive-env.xml

创建hive-env.sh：
```
$ cd hive-3.1.3/conf
$ cp hive-env.sh.template hive-env.sh
```

加入配置（若hadoop_home已经在其他地方配置，则不用配置）：
```
# export HADOOP_HOME=/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/bin
export HIVE_CONF=/User/wangjiawei/Dev/env/apache-hive-3.1.3-bin/conf
export HIVE_AUX_JARS_PATH=/User/wangjiawei/Dev/env/apache-hive-3.1.3-bin/lib
```

#### hive-site.xml

创建hive-site.xml，`vim hive-site.xml`，写入：
```
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
   <property>
      <name>javax.jdo.option.ConnectionURL</name>
      <value>jdbc:mysql://localhost:3306/hive?useSSL=false&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Shanghai</value>
   </property>
   
   <!-- jdbc 连接的 Driver-->
   <property>
      <name>javax.jdo.option.ConnectionDriverName</name>
      <value>com.mysql.jdbc.Driver</value>
   </property>
   
   <!-- jdbc 连接的 username-->
   <property>
      <name>javax.jdo.option.ConnectionUserName</name>
      <value>root</value>
   </property>
   
   <!-- jdbc 连接的 password -->
   <property>
      <name>javax.jdo.option.ConnectionPassword</name>
      <value>root1234</value>
   </property>
   
   <!-- 是否进行 Hive 元数据存储版本的验证 -->
   <property>
      <name>hive.metastore.schema.verification</name>
      <value>false</value>
   </property>
   
   <!-- 是否启用元数据存储授权 -->
   <property>
      <name>hive.metastore.event.db.notification.api.auth</name>
      <value>false</value>
   </property>
   
   <!-- Hive 默认在 HDFS 的工作目录 -->
   <property>
      <name>hive.metastore.warehouse.dir</name>
      <value>/user/hive/warehouse</value>
   </property>
   
   <!-- 显示表头 -->
   <property>
      <name>hive.cli.print.header</name>
      <value>true</value>
   </property>
   
   <!-- 显示当前库 -->
   <property>
      <name>hive.cli.print.current.db</name>
      <value>true</value>
   </property>
   
   <!-- 配置元数据远程连接地址 -->
   <property>
      <name>hive.metastore.uris</name>
      <value>thrift://localhost:9083</value>
   </property>
</configuration>
```

执行元数据库初始化命令：
```
$ ./bin/schematool -initSchema -dbType mysql -verbos
```

### 启动

确保hadoop启动的情况下，执行：

启动元数据管理服务：
```
# 前台启动
./bin/hive --service metastore

# 后台启动
nohup ./bin/hive --service metastore >> logs/metastore.log 2>&1 &
```

启动客户端：
```
# hive shell 方式（可以直接写sql）
./bin/hive

# hive ThriftServer方式（不可直接写SQL，需要外部客户端链接使用）
./bin/hive --service hiveserver2
```