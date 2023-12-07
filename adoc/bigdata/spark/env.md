需要安装hadoop和hive

### 一、基础下载、安装

https://archive.apache.org/dist/spark/ 

下载对应hive版本的spark，在hive源码的pom文件中可以看spark版本。

配置环境变量：
```
export SPARK_HOME=/Users/wangjiawei/Dev/env/spark/spark-2.3.0-bin-without-hadoop
export PATH=$PATH:$SPARK_HOME/bin
```

### 二、修改配置

#### spark

首先创建配置文件
```
cd spark-2.3.0/conf
cp spark-env.sh.template spark-env.sh
```

写入spark-env.sh：
```
# 命令行执行 hadoop classpath查看
export SPARK_DIST_CLASSPATH=$hadoop classpath
```

#### hive

hive将spark作为执行引擎，修改hive配置

创建`hive-3.1.3/conf/spark-defaults.conf`：
```
# 指定提交到 YARN 运行
spark.master                   = yarn

# 开启日志存储到 HDFS
spark.eventLog.enabled         = true
# 指定 Spark 事件日志存储的 HDFS 目录
spark.eventLog.dir             = hdfs://localhost:8020/spark-logs

# 指定每个执行器内存
spark.executor.memory          = 1g
# 指定每个调度器内存
spark.driver.memory            = 1g
```

创建log目录，shell中执行：
```
hdfs dfs -mkdir hdfs://localhost:8020/spark-logs
```

hive-3.1.1/conf/hive-site.xml
```
<!-- 指定spark作为执行引擎代替MapReduce，3.X开始hive开始淘汰MR-->
<property>
    <name>spark.yarn.jars</name>
    <value>hdfs://localhost:8020/spark-jars/*</value>
</property>
<property>
    <name>hive.execution.engine</name>
    <value>spark</value>
</property>
<!-- 任务提交超市时间，单位ms -->
<property>
    <name>hive.spark.client.connect.timeout</name>
    <value>5000</value>
</property>
```