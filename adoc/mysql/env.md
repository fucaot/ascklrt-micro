一、下载：https://dev.mysql.com/downloads/mysql/ ，选择自己对应的版本，此处我选择macos-mysql-8.2.0


二、自行配置root密码，暂时设置为`root1234`

三、进入mysqlbin，尝试
```
cd /usr/local/mysql/bin

./mysql -u root -p
```


四、添加环境变量（根据情况酌情考虑）

.zshrc
```
export MYSQL_HOME=/usr/local/mysql/bin
export PATH=$PATH:$HADOOP_HOME:$SACLA_HOME:$MYSQL_HOME
```


MySQL 8.0 8.0.x 适用于MySQL 8.0及以上版本

MySQL 5.7 5.1.x, 5.5.x, 5.6.x 适用于MySQL 5.7及以上版本

MySQL 5.6 5.1.x, 5.5.x, 5.6.x 适用于MySQL 5.6及以上版本

MySQL 5.5 5.1.x, 5.5.x 适用于MySQL 5.5及以上版本

MySQL 5.1 5.1.x, 5.4.x 适用于MySQL 5.1及以上版本