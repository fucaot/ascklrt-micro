1. ssh免密

首先需要打开偏好设置->共享->远程登录，勾选所有用户

然后，终端执行ssh-keygen -t rsa，之后一路enter键

最后，执行cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys用于授权你的公钥到本地可以无需密码实现登录。

执行ssh localhost命令，无异常，设置成功。

2. `https://archive.apache.org/dist/hadoop/common/hadoop-3.2.0/`下载hadoop

`cd ~/Dev/env/hadoop`


3. 修改文件

进入 `env/hadoop/hadoop-3.2.0/etc/hadoop` 目录，进行文件修改。

#### core-site.xml

```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:8020</value>
    </property>
    <!--用来指定hadoop运行时产生文件的存放目录自己创建-->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>file:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/tmp</value>
    </property>
</configuration>
```


#### hdfs-site.xml
```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <!-- 配置端口 -->
    <!-- <property>
        <name>dfs.namenode.rpc-address</name>
        <value>localhost:9300</value>
    </property> -->
    <!--不是root用户也可以写文件到hdfs-->
    <property>
        <name>dfs.permissions</name>
        <value>false</value>
    </property>
    <!--把路径换成本地的name坐在位置-->
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/dfs/name</value>
    </property>
    <!--在本地新建一个存放hadoop数据的文件夹，然后将路径在这里配置一下-->
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/dfs/data</value>
    </property>
    
    <!-- 设置最大堆内存为 2GB，看情况指定，后续使用hive可能存在namenode内存不足的情况 -->
    <property>
       <name>dfs.namenode.java.opts</name>
       <value>-Xmx2048m</value>
    </property>
</configuration>
```

#### mapred-site.xml
```
<configuration>
    <!-- 指定mapreduce运行在yarn上 -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>

    <property>
        <name>mapred.job.tracker</name>
        <value>localhost:9010</value>
    </property>

    <!-- 新添加 -->
    <!-- 下面的路径就是你hadoop distribution directory -->
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>HADOOP_MAPRED_HOME=/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/libexec</value>
    </property>

    <property>
        <name>mapreduce.map.env</name>
        <value>HADOOP_MAPRED_HOME=/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/libexec</value>
    </property>

    <property>
        <name>mapreduce.reduce.env</name>
        <value>HADOOP_MAPRED_HOME=/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/libexec</value>
    </property>
</configuration>
```

#### yarn-site.xml
```
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>

    <property>
        <name>yarn.resourcemanager.address</name>
        <value>localhost:9000</value>
    </property>

    <property>
        <name>yarn.scheduler.capacity.maximum-am-resource-percent</name>
        <value>100</value>
    </property>

    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>

    <!-- 开启yarn日志聚合功能 -->
    <property>
        <name>yarn.log-aggregation-enable</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.log.server.url</name>
        <value>http://localhost:19888/jobhistory/logs/</value>
    </property>
    
    <!-- 这一条配置的value，需要执行在bin目录下执行 hadoop classpath命令，然后将结果作为值 -->
    <property>
        <name>yarn.application.classpath</name>
        <value>/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/etc/hadoop:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/common/lib/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/common/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/hdfs:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/hdfs/lib/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/hdfs/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/mapreduce/lib/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/mapreduce/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/yarn:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/yarn/lib/*:/Users/wangjiawei/Dev/env/hadoop/hadoop-3.2.0/share/hadoop/yarn/*</value>
    </property>
</configuration>
```


/etc/hadoop/hadoop-env.sh
```
# export JAVA_HOME=/Users/wangjiawei/Library/Java/JavaVirtualMachines/openjdk-19.0.2/Contents/Home
export JAVA_HOME=/Users/wangjiawei/Library/Java/JavaVirtualMachines/openjdk-1.8/Contents/Home

# 设置堆内存大小为 2GB，根据实际情况指定，后续使用hive可能出现namenode节点内存不足的情况
export HADOOP_HEAPSIZE=2048
```

---

4. 修改启动脚本

sbin/start-dfs.sh & sbin/stop-dfs.sh
```
HDFS_DATANODE_USER=root
HDFS_DATANODE_SECURE_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
```


sbin/start-yarn.sh & sbin/stop-yarn.sh
```
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```


5. 启动
启动主要组件（NameNode / SecondaryNameNode / DataNode / NodeManager / ResourceManager）：
```
./sbin/start-all.sh
```

启动日志服务：
```
./sbin/mr-jobhistory-daemon.sh start historyserver
```


6. URLS

- YARN ResourceManager: 8088（Web UI）：`http://localhost:8088/cluster`

- http://localhost:9870/

- http://localhost:19888/jobhistory/app