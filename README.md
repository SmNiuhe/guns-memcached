[TOC]



# 目标

* 掌握主流开发框架 Guns 开发和使用
* 掌握 SpringBoot 配置使用流程
* 掌握主流内存应用之 Memcached
* 掌握 Tomcat + Memcached 的分布式搭建和优化
* 掌握 Tomcat + Redis 的分布式搭建和优化

### 

## Guns 知识点概览

* guns-admin 分页、事务
* 前后端分离安全之 JWT 和签名机制

* Guns-rest 代码生成器
* 线上环境部署

## Tomcat 知识点概览

* 虚拟机优化
* 运行模式优化
* MSM

  ### 

## Memcache 知识点概览

* Memcached
  * Slab Allocator
  * XMemcached
  * 分布式搭建
  * 内存级优化
  * 实战场景分析

## Redis 知识点概览

* 掌握 Redis 安装部署
* 掌握 Jedis 基本开发流程
* 了解 Redis 集群代理之 Codis

* 掌握 Codis 安装和部署

  ### 
* 掌握 Codis 的经常应用

## Guns 进阶

#### Guns 分页

* 逻辑分页
  * 原理
    * 1、查询出所有数据
    * 2、数据传入前端框架【Bootstrap Table】
  * Guns 的默认实现
  * 实现方式
    * table.setPaginationType("client")

* 物理分页
  * 原理
    * 1、按照要求查询数据
    * 2、数据返回前端
      * rows
      * total
  * 实现方式
    * table.setPaginationType("server")
    * 加入数据库分页语句
      * Page 对象

#### Guns REST 开发

##### 使用场景

1. App 开发
2. 前段端分离开发

   ### 

##### 模拟演示前后端分离开发

1. Restlet 工具、FeHelper 模拟前端访问

2. Controller 

   业务逻辑处理

不使用 jwt 和签名的 rest 开发

 	1. 修改配置文件 application.yml
      	1. rest
           	1. Auth-open：false
           	2. Sign-open：false
     	2. spring
         	1. datasource -> 修改为自己的数据源

#### 生成代码工具路径：

```shell
/Users/smniuhe/IdeaProjects/guns-memcached/guns-rest/src/test/java/cn/stylefeng/guns/rest/generator/EntityGenerator.java


1. 准备数据
2. 修改生成代码的个性化配置
	// 全局配置[代码输出目录、作者]
	// 数据源配置
	// strategy.setInclude 
	// 包配置
```

#### Guns 签名机制

##### Jwt 必备三部分

* head（两个部分）
  * jwt 类型，jwt 的编码格式（base64）组成
* playload（多个部分）
  * jwt 过期时间，jwt 的唯一标示，服务器信息组成
* signature（head+playload+随机码）

##### Guns Rest的签名机制：【客户端】

1. 去服务端获取 ”钥匙“
2. 进行业务处理
3. 对即将传入服务器的数据进行 ”签名“
4. 将签名以后的内容传入服务器

```json
{
    "randomKey": "e5u263",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJlNXUyNjMiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTU1MDA1MDE5NCwiaWF0IjoxNTQ5NDQ1Mzk0fQ.6fR_tyGdFLyD227mtGx6nhhZwP8bitISGezFQQ4bVjZ-nw9sOCn7ErNQqb__ugIZOgN5CdOGvwzZhyq1wjrwDA"
} 
{    		 "object":"eyJob3VzZUFkZHJlc3MiOiJqd3RBZGRyZXNzIiwiaG91c2VEZXNjIjoiand0RGVzYyIsImhvdXNlVXNlciI6IuW8oOS4iSJ9","sign":"23f63642a5547ef246fbaffddbcb7ef9"
}
```



#### 事务：

1. 保证数据操作的一致性，完整性能...
2. ACID
3. 隔离级别
4. 属性



## Tomcat 优化

### 线程池优化【Connector优化】

* maxConnections - 最大连接数(maxThreads+acceptCount )

* maxThreads - 最大线程数

* acceptCount - 最大排队等待数(一般建议<= maxThreads)

* ulimit -a

  ```shell
  [root@iZuf6iq8e7ya9v3ix71k0pZ ~]# ulimit -a
  core file size          (blocks, -c) 0
  data seg size           (kbytes, -d) unlimited
  scheduling priority             (-e) 0
  file size               (blocks, -f) unlimited
  pending signals                 (-i) 15087
  max locked memory       (kbytes, -l) 64
  max memory size         (kbytes, -m) unlimited
  open files                      (-n) 65535
  pipe size            (512 bytes, -p) 8
  POSIX message queues     (bytes, -q) 819200
  real-time priority              (-r) 0
  stack size              (kbytes, -s) 8192
  cpu time               (seconds, -t) unlimited
  max user processes              (-u) 15087
  virtual memory          (kbytes, -v) unlimited
  file locks                      (-x) unlimited
  [root@iZuf6iq8e7ya9v3ix71k0pZ ~]# vim /etc/security/limits.conf
  ...
  ...
  ...
  #        - rss - max resident set size (KB)
  #        - stack - max stack size (KB)
  #        - cpu - max CPU time (MIN)
  #        - nproc - max number of processes
  #        - as - address space limit (KB)
  #        - maxlogins - max number of logins for this user
  #        - maxsyslogins - max number of logins on the system
  #        - priority - the priority to run user process with
  #        - locks - max number of file locks the user can hold
  #        - sigpending - max number of pending signals
  #        - msgqueue - max memory used by POSIX message queues (bytes)
  #        - nice - max nice priority allowed to raise to values: [-20, 19]
  #        - rtprio - max realtime priority
  #
  #<domain>      <type>  <item>         <value>
  #
  
  #*               soft    core            0
  #*               hard    rss             10000
  #@student        hard    nproc           20
  #@faculty        soft    nproc           20
  #@faculty        hard    nproc           50
  #ftp             hard    nproc           0
  #@student        -       maxlogins       4
  
  # End of file
  * soft nofile 65535
  * hard nofile 65535
  ...
  [root@iZuf6iq8e7ya9v3ix71k0pZ ~]#
  
  ```

  

* 添加 maxConnections 的配置
  * 对 CPU 要求更高时，建议不要配置过于大
  * 对 CPU 要求不是特别高时（对 IO 要去比较高），建议配置在 3000 左右 

* 配置的地方

  * server.xml -> Connector

  ```xml
  <Connector port="8081" protocol="org.apache.coyote.http11.Http11AprProtocol"
                  maxConnections="2000"
                  maxThreads="500"
                  acceptCount="500"
                  compression="true"
                  compressionMinSize="2048"
                  minSpareThreads="100"
                  connectionTimeout="20000"
                  redirectPort="8443" />
  ```

  

### Gzip 相关设置【Connector优化】

* compression - 设置开启 Gzip 压缩

* compressableMimeType - 压缩类型
* compressionMinSize - 压缩后输出内容大小
* 【其他项配置】
* enableLookups - 开启反查域名
* connectionTimeout -网络连接超时阈值
* minSpareThreads - 最小空闲线程数

### Tomcat 三种模式【Connector优化】

* BIO：最稳定最老的一个连接器，使用阻塞形式处理 Request 请求

* NIO：使用 Java 的异步 IO 技术，进行非阻塞刑事处理 Request 请求

  ```shell
  ## tomcat 8 9 默认是 NIO
  
  [root@iZuf6iq8e7ya9v3ix71k0pZ apache-tomcat-9.0.14]# tail logs/catalina.out
  Parsed mapper file: 'file [/opt/install/apache-tomcat-9.0.14/webapps/ROOT/WEB-INF/classes/com/stylefeng/guns/modular/system/dao/mapping/RoleMapper.xml]'
  Parsed mapper file: 'file [/opt/install/apache-tomcat-9.0.14/webapps/ROOT/WEB-INF/classes/com/stylefeng/guns/modular/system/dao/mapping/TblHouseMapper.xml]'
  Parsed mapper file: 'file [/opt/install/apache-tomcat-9.0.14/webapps/ROOT/WEB-INF/classes/com/stylefeng/guns/modular/system/dao/mapping/UserMapper.xml]'
  06-Feb-2019 21:01:41.128 信息 [main] org.apache.jasper.servlet.TldScanner.scanJars 至少有一个JAR被扫描用于TLD但尚未包含TLD。 为此记录器启用调试日志记录，以获取已扫描但未在其中找到TLD的完整JAR列表。 在扫描期间跳过不需要的JAR可以缩短启动时间和JSP编译时间。
  06-Feb-2019 21:01:41.164 信息 [main] org.apache.catalina.startup.HostConfig.deployWAR Deployment of web application archive [/opt/install/apache-tomcat-9.0.14/webapps/ROOT.war] has finished in [18,853] ms
  06-Feb-2019 21:01:41.165 信息 [main] org.apache.catalina.startup.HostConfig.deployDirectory 部署 web 应用程序目录 [/opt/install/apache-tomcat-9.0.14/webapps/guns-logs]
  06-Feb-2019 21:01:41.199 信息 [main] org.apache.catalina.startup.HostConfig.deployDirectory Deployment of web application directory [/opt/install/apache-tomcat-9.0.14/webapps/guns-logs] has finished in [34] ms
  06-Feb-2019 21:01:41.202 信息 [main] org.apache.coyote.AbstractProtocol.start 开始协议处理句柄["http-apr-8081"]
  06-Feb-2019 21:01:41.253 信息 [main] org.apache.coyote.AbstractProtocol.start 开始协议处理句柄["ajp-nio-8010"]
  06-Feb-2019 21:01:41.257 信息 [main] org.apache.catalina.startup.Catalina.start Server startup in [19,263] milliseconds
  ```

  

* APR：原生 C 语言编写的非阻塞I/O，目前性能最理想（不能做到平台无关）

  | **Recommended releases**                                     |
  | ------------------------------------------------------------ |
  | The recommended releases of the several Apache Portable Runtime libraries are |

  * APR 1.6.5, released September 14, 2018

  * APR-util 1.6.1, released October 22, 2017

  * APR-iconv 1.2.2, released October 22, 2017

  ###### 安装顺序

  ```shell
  # 下载APR前的依赖包
  yum install -y expat expat-devel
  
  安装步骤
  1. apr
     1. 解压至安装目录(# tar -xvf apr-1.6.5.tar.gz -C ../install/)
     2. ./configure -prefix=/usr/local/apr
     3. make
     4. make install
  2. apr-iconv
     1. 解压至安装目录(# tar -xvf apr-iconv-1.2.2.tar.gz -C ../install/)
     2. ./configure -prefix=/usr/local/apr-iconv --with-apr=/usr/local/apr
     3. make
     4. make install
  3. apr-util
     1. 解压至安装目录(# tar -xvf apr-util-1.6.1.tar.gz -C ../install/)
     2. ./configure -prefix=/usr/local/apr-util --with-apr=/usr/local/apr --with-apriconv=/usr/local/apr-iconv
     3. make
     4. make install
  ```

  

  ###### Native 解压配置

  ```shell
  [root@iZuf6iq8e7ya9v3ix71k0pZ apr-util-1.6.1]# cd /opt/install/apache-tomcat-9.0.14/bin
  [root@iZuf6iq8e7ya9v3ix71k0pZ bin]# ls
  bootstrap.jar		      configtest.bat	  makebase.bat	    tomcat-juli.jar
  catalina.bat		      configtest.sh	  makebase.sh	    tomcat-native.tar.gz
  catalina.sh		      daemon.sh		  setclasspath.bat  tool-wrapper.bat
  catalina-tasks.xml	      digest.bat	  setclasspath.sh   tool-wrapper.sh
  ciphers.bat		      digest.sh		  shutdown.bat	    version.bat
  ciphers.sh		      guns-logs		  shutdown.sh	    version.sh
  commons-daemon.jar	      guns-rest.log	  startup.bat
  commons-daemon-native.tar.gz  hs_err_pid6741.log  startup.sh
  [root@iZuf6iq8e7ya9v3ix71k0pZ bin]# tar -zxf tomcat-native.tar.gz
  [root@iZuf6iq8e7ya9v3ix71k0pZ bin]# cd tomcat-native-1.2.19-src/
  [root@iZuf6iq8e7ya9v3ix71k0pZ tomcat-native-1.2.19-src]# cd native/
  [root@iZuf6iq8e7ya9v3ix71k0pZ native]# ls
  build	    build-outputs.mk  include	       Makefile.in	  os		tcnative.pc.in
  buildconf   config.layout     libtcnative.dsp  NMAKEmakefile	  src		tcnative.spec
  build.conf  configure	      libtcnative.dsw  NMAKEmakefile.inc  srclib
  BUILDING    configure.in      LICENSE.bin.win  NOTICE.bin.win	  tcnative.dsp
  
  
  [提示 java home 找不到]
  [root@iZuf6iq8e7ya9v3ix71k0pZ native]# ./configure --with-apr=/usr/local/apr
  checking build system type... x86_64-pc-linux-gnu
  checking host system type... x86_64-pc-linux-gnu
  checking target system type... x86_64-pc-linux-gnu
  checking for a BSD-compatible install... /usr/bin/install -c
  checking for working mkdir -p... yes
  Tomcat Native Version: 1.2.19
  checking for chosen layout... tcnative
  checking for APR... yes
  configure: APR 1.6.5 detected.
    setting CC to "gcc"
    setting CPP to "gcc -E"
    setting LIBTOOL to "/usr/local/apr/build-1/libtool"
  checking for JDK location... configure: error: Java Home not defined. Rerun with --with-java-home=... parameter
  [root@iZuf6iq8e7ya9v3ix71k0pZ install]# vim /etc/profile
  export JAVA_HOME=/usr/java/jdk1.8.0_111
  export PATH=$JAVA_HOME/bin:$PATH
  [root@iZuf6iq8e7ya9v3ix71k0pZ install]# source /etc/profile
  [root@iZuf6iq8e7ya9v3ix71k0pZ install]# echo $JAVA_HOME
  /usr/java/jdk1.8.0_111
  
  [root@iZuf6iq8e7ya9v3ix71k0pZ native]# ./configure --with-apr=/usr/local/apr - -with-java-home=/usr/java/jdk1.8.0_111
  
  [root@iZuf6iq8e7ya9v3ix71k0pZ native]# make & make install
  
  
  2. 修改 catalina.sh
  LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/apr/lib export LD_LIBRARY_PATH
  
  3. 修改 server.xml
  protocol="org.apache.coyote.http11.Http11AprProtocol"
  ```

  



### JVM 优化建议

| 参数                 | 参数作用       | 优化建议            |
| -------------------- | -------------- | ------------------- |
| -server              | 启动Server     | 服务端建议开启      |
| -Xms                 | 最小内存       | 建议与-Xmx相同      |
| -Xmx                 | 最大内存       | 建议到可用内存的80% |
| -XX:MetaspaceSize    | 元空间初始值   |                     |
| -XX:MaxMetaspaceSize | 元空间最大内存 | 默认无限            |
| -XX:MaxNewSize       | 新生代最大内存 | 默认16M             |

##### tomcat/bin/catalina.sh

```shell
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# cd /opt/install/apache-tomcat-9.0.14/
[root@iZuf6iq8e7ya9v3ix71k0pZ apache-tomcat-9.0.14]# vim bin/catalina.
catalina.bat  catalina.sh
[root@iZuf6iq8e7ya9v3ix71k0pZ apache-tomcat-9.0.14]# vim bin/catalina.sh

#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# -----------------------------------------------------------------------------

JAVA_OPTS="-server -Xms128m -Xmx128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:MaxNewSize=32m"

...
...
...



[JPS 名称: jps - Java Virtual Machine Process Status Tool]
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# jps
2333 Bootstrap
2349 Jps
[jmap]
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# ps -ef|grep tomcat
root      2229  2213  0 19:58 pts/1    00:00:00 vim apache-tomcat-9.0.14/conf/server.xml
root      2333     1 66 21:01 pts/0    00:00:31 /usr/java/jdk1.8.0_111/bin/java -Djava.util.logging.config.file=/opt/install/apache-tomcat-9.0.14/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -server -Xms128m -Xmx128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:MaxNewSize=32m -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -classpath /opt/install/apache-tomcat-9.0.14/bin/bootstrap.jar:/opt/install/apache-tomcat-9.0.14/bin/tomcat-juli.jar -Dcatalina.base=/opt/install/apache-tomcat-9.0.14 -Dcatalina.home=/opt/install/apache-tomcat-9.0.14 -Djava.io.tmpdir=/opt/install/apache-tomcat-9.0.14/temp org.apache.catalina.startup.Bootstrap start
root      2528  2283  0 21:02 pts/0    00:00:00 grep --color=auto tomcat
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# jmap -heap 2333
Attaching to process ID 2333, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.111-b14

using thread-local object allocation.
Parallel GC with 2 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 134217728 (128.0MB)
   NewSize                  = 33554432 (32.0MB)
   MaxNewSize               = 33554432 (32.0MB)
   OldSize                  = 100663296 (96.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 134217728 (128.0MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 134217728 (128.0MB)
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 30408704 (29.0MB)
   used     = 4240416 (4.043975830078125MB)
   free     = 26168288 (24.956024169921875MB)
   13.944744241648706% used
From Space:
   capacity = 1572864 (1.5MB)
   used     = 1050712 (1.0020370483398438MB)
   free     = 522152 (0.49796295166015625MB)
   66.80246988932292% used
To Space:
   capacity = 1572864 (1.5MB)
   used     = 0 (0.0MB)
   free     = 1572864 (1.5MB)
   0.0% used
PS Old Generation
   capacity = 100663296 (96.0MB)
   used     = 74858928 (71.39103698730469MB)
   free     = 25804368 (24.608963012695312MB)
   74.36566352844238% used

31119 interned Strings occupying 3555328 bytes.
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]#
```



## Tomcat 集群

单个 Tomcat 存在的问题

### Tomcat 集群方式

* Apache Tomcat Clustering

  ```
  http://tomcat.apache.org/tomcat-9.0-doc/cluster-howto.html
  ```

  

* JWT 等类似机制

* MSM、Tomcat+Redis 等统一 Session 管理



## Memcached 基础及底层机制剖析

### 核心知识点

* Memcached 入门
* 基础命令与原理
* Memcached 与 Java 集合
* Memcached 集群
* 使用场景与优化建议

### Memcached 概念

* Memcached 是一款高性能、分布式的内存对象缓存系统
* Memcached 可以有效分担数据库负载
* Memcached 基于 libevent 事件处理实现无阻塞通信

### Memcached 安装部署

```shell
[libevent-2.1.8-stable.tar.gz][memcached-1.5.12.tar.gz]
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# tar zxf libevent-2.1.8-stable.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# tar zxf memcached-1.5.12.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# ls /opt/software/
apache-tomcat-9.0.14.tar.gz  guns-rest-0.0.1.war
apr-1.6.5.tar.gz             libevent-2.1.8-stable
apr-iconv-1.2.2.tar.gz       libevent-2.1.8-stable.tar.gz
apr-util-1.6.1.tar.gz        memcached-1.5.12
guns-admin-1.0.0.war         memcached-1.5.12.tar.gz


libevent:
	安装
		1、解压缩
		2、./configure --prefix=/opt/install/libevent
		3、make
		4、make install
		
		
memcached:
	安装
		1、解压缩
		2、./configure --prefix=/opt/install/memcached --with-libevent=/opt/install/libevent
		3、make
		4、make install
```

### Memcached 启动参数

| 参数 | 参数作用                        |
| ---- | ------------------------------- |
| -d   | 启动一个守护进程                |
| -m   | 分配给 Memcached 使用的内存数量 |
| -u   | 运行 Memcached 的用户           |
| -I   | 监听的服务器 IP 地址            |
| -p   | 监听的端口                      |
| -c   | 最大运行的并发连接数            |
| -P   | 设置保持 Memcahced 的 pid 文件  |

```shell
启动参数：
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# ls /opt/install/memcached
bin/memcached -u root -l 172.19.106.154 8088 -c 128 -m 100 -P myPid
bin/memcached -d -u root -l 172.19.106.154 -p 8088 -c 128 -m 100 -P myPid
[root@iZuf6iq8e7ya9v3ix71k0pZ memcached]# more myPid
2979


[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# telnet 172.19.106.154 8088
貌似只能通过内网地址去开启 memcached，XMemcached 才能通过 外网地址连接上
```

### Memcached 常见命令

| 命令          | 命令作用                         |
| ------------- | -------------------------------- |
| set           | 用于向缓存添加新的键值对         |
| add           | 当缓存中不存在键时则写入         |
| replace       | 当键已经存在时进行替换           |
| append        | 在已有结果上追加数据             |
| prepend       | 在已有数据前补充数据             |
| cas           | 检查和更新，通常与 gets 一起使用 |
| get/gets      | 获取数据/数据+版本号             |
| delete        | 删除数据                         |
| incr/decr     | 增加/减少数值                    |
| **flush_all** | 清空所有缓存（慎用）             |

```shell
Memcached 入门讲解
	命令操作
		准备工作
			telnet
				yum install -y telnet
				telnet ip地址 端口号
        新增操作
        	set
        		无论什么情况，都可以插入
        		set key flags exTime length -> value
        	add
        		只有当 key 不存在的情况下，才可以插入
        		add key flags exTime length -> value
        修改操作
        	replace
        		替换内容
        		replace key flags exTime length -> value
        		注意
        			1、只修改已存在 Key 的 value 值
        			2、如果 key 不存在，则不会进行操作	
        	append
        		追加后面内容
        		append key flags exTime length -> value	
        		注意
        			1、length 标示是追加的长度而不是总长度
        			2、如果key不存在，则不会进行操作
        	prepend
        		追加前面内容
			 	prepend key flags exTime length -> value	
        		注意
        			1、length 标示是追加的长度而不是总长度
        			2、如果key不存在，则不会进行操作	
        查询操作
        	get
        		get key
        检查更新
        	cas
        		check and set
        		流程
        			1、输入待修改的数据+版本号
        			2、Memcached 检测版本号是否正确
        			3、如果正确，则修改数据
                cas key flags exTime length version -> value
            gets
            	gets key
        删除操作
        	delete
        		delete key
        		注意事项
        			1、delete 操作并不会真正删除数据
        			2、将数据打一个删除标记
        增减操作
        	incr
        		incr key 增加偏移量
        	decr
        		desc key 减少偏移量
        	注意事项
        		1、incr 和 decr 只能操作能转换为数值的 value
        		2、desr 不能将数字减少至 0 以下	
    Slab Allocator
    	Slab
    		内存容器
    	Page
    		小版的内存容器
    	Chunk
    		"保险箱"
    		注意
    			1、Chunk 是预分配大小的
    			2、Chunk 的大小是 80 Byte
    			3、不同 Slab 的 Chunk 大小不一样
    			4、相同 Slab 的 Chunk 大小固定
    			5、宁可内存不被整除浪费，Chunk 大小也不会变
    	自增长因子
    		`1.25`(Chunk 增长)
    	Slab Class
    		存储 Slab 一些元数据
    		存储样式
    			80=Slab1,Slab2,Slab3
                100=Slab4
                125=Slab5,Slab6
    注意事项
    	1、Memcached 是 key/value 键值对形式存储
    	2、key 的样子：key flags exTime length
     寻找 Chunk 的经历
     	Slot[垃圾桶]，
     		1、delete 后，将 Chunk 标识放入 Slot
     		2、数据过期，将 Chunk 标识放入 Slot
     	LRU
     		最近最少使用
     	流程
     		1、在 Slot 中寻找不用的 Chunk
     		2、使用空闲的 Chunk
     		3、触发 LRU 流程
     	注意事项
     		1、不会因为其他 Slab 空闲，就不触发 LRU 流程
     	




通过 telnet 操作 memcached(文本协议的内容) 命令
[root@iZuf6iq8e7ya9v3ix71k0pZ ~]# yum install -y telnet
[root@iZuf6iq8e7ya9v3ix71k0pZ memcached]# telnet 127.0.0.1 8088
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
## set
set k1 0 0 2
v1
STORED
## add
add k2 0 0 2
v2
STORED
set k1 0 0 3
v11
STORED
add k2 0 0 3
v22
NOT_STORED
## replace
replace k2 0 0 5
v5555
STORED
replace k3 0 0 2
v2
NOT_STORED
## gets+cas
gets k1
VALUE k1 0 3 3
v11
END
cas k1 0 0 10 3
helloworld
STORED
get k1
VALUE k1 0 10
helloworld
EN
VALUE k1 0 10
helloworld
END
## append
append k1 0 0 5
12345
STORED
## prepend
prepend k1 0 0 5
abcde
STORED
get k1
VALUE k1 0 20
abcdehelloworld12345
END
## delete
delete k2
DELETED
get k2
END
## incr/decr
incr k5 2
52
incr k5 -2
CLIENT_ERROR invalid numeric delta argument
incr k5 8
60
decr k5 2
58
```



## 深入使用 XMemcached

### Memcached 客户端

* 注意事项
  * Memcached 是 C 语言编写的
  * Memcached 提供了诸多主流语言的客户端
* Java 客户端
  * java_memcached[danga]
  * spymemcached
  * xmemcached（偏向）
* Memcached 集群
  * 自身通过算法保障数据唯一性
  * 集群形式对用户和 Memcached 都是透明的
  * Memcached 的集群是通过客户端实现的
  * Memcached 服务端相互不认识
* Hash 算法
  * 余数 Hash
    * 过程
      * 将传入的 key 转换为 Hash 值
      * 获取服务器数量列表
      * Hash 值 % 服务器数量
      * 通过余数来选择具体存放/查询的服务器
    * 优缺点
      * 简便易理解
      * 增加/减少节点，会造成 “灾难”

## Memcached 分布式

### Memcached 分布式原理

* Memcached 的分布式很特殊：客户端分布式
* Memcached 每一个服务器端都不互相通信
* Memcached 客户端通过算法保证数据的唯一性

### 常见的分布式算法

* 余数 Hash
  * 过程
    - 将传入的 key 转换为 Hash 值
    - 获取服务器数量列表
    - Hash 值 % 服务器数量
    - 通过余数来选择具体存放/查询的服务器
  * 优缺点
    - 简便易理解
    - 增加/减少节点，会造成 “灾难”
* 一致性 Hash
  * 过程
    * 将服务器列表+虚拟节点分布在 0 -2^32 的一个圆上
    * 将传入的 key 转换为 Hash 值
    * 通过余数来选择具体存放/查询的服务器
    * 如果余数命中虚拟节点，则会顺时针寻找真实的 Memcached 服务
  * 面试题
    * Memcached 的两端 Hash
      * 客户端通过 Hash 算法寻找存储/查询节点
      * 进入 Memcached 以后，通过 hash 算法，寻找具体 Chunk



```shell
XMemcachedClientBuilder xMemcachedClientBuilder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses("106.15.191.27:8088 106.15.191.27:8089"));
## 分布式
[root@iZuf6iq8e7ya9v3ix71k0pZ memcached]# bin/memcached -d -u root -l 172.19.106.154 -p 8088 -c 128 -m 100 -P myPid
[root@iZuf6iq8e7ya9v3ix71k0pZ memcached]# bin/memcached -d -u root -l 172.19.106.154 -p 8089 -c 128 -m 100 -P myPid
```

### MemcachedClientBuilder 选项

| 命令                      | 命令作用                            |
| ------------------------- | ----------------------------------- |
| authInfoMap               | 配置身份验证信息                    |
| bufferAllocator           | 设置 NIO ByteBuffer 适配器          |
| **commandFactory**        | 设置协议模式，默认是文本协议        |
| configuration             | 设置与 Session 有关的诸多参数       |
| **connectionPoolSize**    | 设置 NIO 连接池链接数量             |
| connectionTimeout         | 设置超时时间                        |
| enableHealSession         | 是否开启健康检查                    |
| failureModel              | 是否开启 failure 模式，默认是 false |
| healSessionInterval       | 健康检查间隔，默认是 2 秒           |
| keyProvider               | Key 转换器，是否进行转义            |
| maxQueueNoReplyOperations | 设置最大的 noreply 数量             |
| name                      | 设置缓存实例名                      |
| **opTimeout**             | 设置默认操作超时时间                |
| sanitizeKeys              | 开启/关闭对 URL 进行的 encode       |
| **sessionLocator**        | 设置客户端 Hash 方式                |
| socketOption              | 设置 TCP 访问的相关参数             |
| stateListeners            | 设置 state 的监听器                 |
| transcoder                | 设置序列转换器                      |



## Memcached 服务级调优

### Memcached 调优思路

* 提高内存命中率
* 减少内存浪费
* 增加内存重复利用率

### Memcached 辅助调优命令

* stats（statistics 统计） 命令：查看服务器的运行状态和内部数据
* stats settings：查看服务器设置
* stats items/slabs：数据项统计/区块统计 

### stats 命令核心参数

* 分析命中率（**缓存命中率**）

  * 相关参数
    - cmd_gets
    - get_hits
    - get_misses
  * (get_hits/cmd_gets) * 100%

* 分析 LRU 的频率

  * curr_items（当前存储的记录数）

  - total_items（当前存储的总记录数）

  - evictions（当前删除的记录数）

* 分析字节数流量

  * bytes（当前的字节数流量）
  * bytes_read（总的读的字节量数流量）
  * bytes_written（总共写入的字节量数流量）

* 分析 CPU 占用情况

  * rusage_use（当前进程执行多少时间）
  * rusage_system（memcached 占用率）

* 分析连接情况

  * curr_connections
  * total_connections

### stats settings 核心参数

| 参数            | 参数作用                         |
| --------------- | -------------------------------- |
| maxbytes        | 最大字节数限制，0 表示无限制     |
| maxconns        | 允许最大连接数                   |
| growth_factor   | 自增长因子                       |
| chunk_size      | key+value+flags 大小             |
| reqs_per_event  | 最大 IO 吞吐量                   |

### stats items 核心参数
| 参数            | 参数作用                         |
| --------------- | -------------------------------- |
| number          | 该 slab 中对象数，不包含过期时间 |
| age             | LRU 队列中最老对象的过期时间     |
| evicted         | LRU 释放对象数                   |
| evicted_nonzero | 设置了非 0 时间的 LRU 释放对象数 |
| evicted_time    | 最后一次 LRU 秒数，监控频率      |
| outofmemory     | 不能存储对象次数                 |
| tailrepairs     | 修复 slabs 次数                  |
| reclaimed       | 使用过期对象空间存储对象次数     |

### stats slabs 核心参数
| 参数           | 参数作用                |
| -------------- | ----------------------- |
| chunk_size     | chunk 的大小            |
| chunk_per_page | 单个 page 的 chunk 数量 |
| total_pages    | 总 page                 |
| total_chunks   | 总 chunk 数             |
| get_hits       |                         |
| userd_chunks   |                         |
| free_chunks    |                         |
| mem_requested  | 存储的数据占用多少字节  |
| active_slabs   | 活跃的 Slab 数量        |

### Slab Allocator

```
#### 存储计算
items
    key
    value
    flags
    数据结构 32 字节
    suffix 16-17 字节
    空间 10 字节
延迟过期
```

### Memcached 性能调优

- 提高内存命中率

- 减少内存浪费

  - 问题
    - 存不满 chunk
    - 热点数据的堆积
    - slab 不能被 page 整除
    - page 不能被 chunk 整除

  ```
  [example 1]
  chunk 96  1.25
  96 117 140
  主要的数据是 60字节和120字节
  ->60
  ->120
  96 会存储 60 字节，从而导致浪费
  117 未被初始化，但是并没有存储值，从而导致浪费
  
  [example 2]
  slab 109b
  page 10b
  chunk 4b
  
  ```

  * 思路
    * 调整 chunk 大小
    * 调整自增长因子
  * 场景分析
    * MSM
      * 特点
        * 数据长度集中在某几个区域内
        * 非均匀分布
      * 思路
        * 集中在哪几个区域【90 100 110】
        * 能不能将 Chunk 调整成一致大小
        * 调整 Chunk 大小和自增长因子、slab 大小
    * 等长数据
      * 特点
        * 数据长度集中在一个区域内
        * 很极端非均匀分布
      * 思路
        * 集中在哪几个区域【90 100 110】
        * 将 Chunk 调整成一致大小
      * 方案
        * 将 Chunk 调整成与业务数据长度保持一致，或略有冗余
        * 调整自增长因子 1.01

- 增加内存重复利用率

### Memcached 典型使用场景

* 分布式应用
* DB 前端缓存
* 变化和查询频繁，又不需要入库
* 查询需求大，数据变更不频繁

## Xmemcached 实战示例

### Tomcat 集群终极版

#### 1、 单 Tomcat 优化集成

- JVM 优化

- 运行模式优化

  ```shell
  [root@iZuf6iq8e7ya9v3ix71k0pZ apache-tomcat-9.0.14]# vim bin/catalina.sh
  JAVA_OPTS="-server -Xms128m -Xmx128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:MaxNewSize=32m"
  
  LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/apr/lib export LD_LIBRARY_PATH
  
  # JAVA_OPTS 是针对 jVM 优化配置
  # LD_LIBRARY_PATH 是针对 APR 的，(需要参考前面的 apr 环境搭建)
  
  # 修改 connector 的运行协议
  [root@iZuf6iq8e7ya9v3ix71k0pZ apache-tomcat-9.0.14]# vim conf/server.xml
  <Connector port="8081" protocol="org.apache.coyote.http11.Http11AprProtocol"
                  maxConnections="2000"
                  maxThreads="500"
                  acceptCount="500"
                  compression="true"
                  compressionMinSize="2048"
                  minSpareThreads="100"
                  connectionTimeout="20000"
                  redirectPort="8443" />
  ```

#### 2、Tomcat 集群

#### 3、Memcached 集群【省略】

* [MSM](https://github.com/magro/memcached-session-manager/wiki/SetupAndConfiguration#example-for-sticky-sessions--kryo) -> non-sticky

```shell
# 准备 apache-tomcat-8.0.8.tar.gz
# 解压重命名成 msm-tomcat-A， msm-tomcat-B
# MSM 依赖 jar 包，直接 copy 到 msm-tomcat-A/lib 下
# TODO: 感觉jar包的版本还是偏低，考虑后续jar的更新
asm-3.2.jar
kryo-1.04.jar
kryo-serializers-0.11.jar
memcached-session-manager-1.8.1.jar
memcached-session-manager-tc8-1.8.1.jar
minlog-1.2.jar
msm-kryo-serializer-1.8.1.jar
reflectasm-1.01.jar
spymemcached-2.11.1.jar

# 配置 msm-tomcat-A/config/context.xml
<Manager className="de.javakaffee.web.msm.MemcachedBackupSessionManager"
    memcachedNodes="n1:106.15.191.27:8088,n2:106.15.191.27:8089"
    sticky="false"
    sessionBackupAsync="false"
    lockingMode="auto"
    requestUriIgnorePattern=".*\.(ico|png|gif|jpg|css|js)$"
    transcoderFactoryClass="de.javakaffee.web.msm.serializer.kryo.KryoTranscoderFactory"
    />



# copy msm-tomcat-A 至 msm-tomcat-B
# 修改 msm-tomcat-B/config/server.xml 的相关端口即可

```

#### 4、集 成 guns 系统

```shell
# shiro 默认是有个session 和 tomcat 的session没有关系，所以需要配置spring-session开关
guns-admin(guns-v5.1) resouces/application.xml
spring-session-open: true      #是否开启spring session,如果是多机环境需要开启(true/false)

# maven clean skp-test package 打包

# 可以删除 tomcat/webapp/*

# 将打好包的 ROOT.war(guns-admin.war) 直接 copy 到 msm-tomcat-A,msm-tomcat-B 下的 webapp 下

# 将 msm-tomcat-A,msm-tomcat-B 压缩成 zip 包，上传到服务器上

# mkdir /opt/workspace

# unzip msm-tomcat-A.zip
# unzip msm-tomcat-B.zip
# rm -rf *.zip

之前是安装的 tomcat9 的 APR 环境，所以需要安装 tomcat8

# cd msm-tomcat-A
# tar -zxf tomcat-native.tar.gz
# cd tomact-nativeo-1.1.30-src
# cd native
# ./configure --with-apr=/usr/local/apr
# make & make install

# 启动 msm-tomcat-A (bin/start.sh)，直接登录
# 启动 msm-tomcat-B (bin/start.sh) 由于是共享 session，此时就不需要登录了

还是有缺陷的，考虑引入 OpenRestry

# OpenRestry 安装
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# tar -zxf openresty-1.13.6.2.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# cd openresty-1.13.6.2
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty-1.13.6.2]#
# ngnix openRestry 依赖包
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# yum install -y pcre-devel openssl-devel gcc curl postgresql-devel

# 组件全部安装
./configure --prefix=/opt/install/openresty \
--with-luajit \
--with-http_iconv_module \
--with-http_postgres_module \
--without-http_redis2_module
./configure --prefix=/opt/install/openresty \
--with-luajit \
--with-http_iconv_module \
--with-http_postgres_module \
--without-http_redis2_module
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty-1.13.6.2]# ./configure --prefix=/opt/install/openresty --with-luajit --with-http_iconv_module --with-http_postgres_module --without-http_redis2_module
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty-1.13.6.2]# make & make install

# 配置
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty-1.13.6.2]# cd /opt/install/
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# cd openresty/
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty]# ls
bin  COPYRIGHT  luajit  lualib  nginx  pod  resty.index  site
[root@iZuf6iq8e7ya9v3ix71k0pZ openresty]# cd nginx/
[root@iZuf6iq8e7ya9v3ix71k0pZ nginx]# ls
conf  html  logs  sbin
[root@iZuf6iq8e7ya9v3ix71k0pZ nginx]# cd conf/
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# ls
fastcgi.conf            koi-utf             nginx.conf           uwsgi_params
fastcgi.conf.default    koi-win             nginx.conf.default   uwsgi_params.default
fastcgi_params          mime.types          scgi_params          win-utf
fastcgi_params.default  mime.types.default  scgi_params.default
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# vim nginx.conf

# add
upstream msm.smniuhe.com {
    server localhost:8080;
    server localhost:8081;
}

# update, 按 / 规则匹配，交给 upstream "" 处理
location / {
    proxy_pass http://msm.smniuhe.com;
}

# 校验 ngnix 配置文件是ok，没有语法错误
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# cd ../sbin/
[root@iZuf6iq8e7ya9v3ix71k0pZ sbin]# ls
nginx
[root@iZuf6iq8e7ya9v3ix71k0pZ sbin]# ./nginx -t
nginx: the configuration file /opt/install/openresty/nginx/conf/nginx.conf syntax is ok
nginx: configuration file /opt/install/openresty/nginx/conf/nginx.conf test is successful
# 启动 ngnix 服务
[root@iZuf6iq8e7ya9v3ix71k0pZ sbin]# ./nginx
[root@iZuf6iq8e7ya9v3ix71k0pZ sbin]# ps -ef|grep nginx
```



## 使用 Redis 构建集群并优化

* 了解 Redis 介绍和安装部署
* 了解 Redis 客户端选择和优劣势分析
* **掌握 Redis 集群搭建**
* 掌握 Tomcat + Redis 集群环境部署（并不是特别好的选择，更倾向于 Tomcat + Memcached）

尬聊 Redis

* Redis 服务器程序采用单进程模型处理客户端请求
* Redis 基于内存并且支持多大六中数据类型
* Redis 是一款非常优秀的 NoSQL 数据库
* Redis 可以与 Lua 脚本结合爆发出强大的功能点 



```shell
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# tar -zxf redis-4.0.12.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# ls
apache-tomcat-9.0.14.tar.gz  guns-rest-0.0.1.war           openresty-1.13.6.2
apr-1.6.5.tar.gz             libevent-2.1.8-stable         openresty-1.13.6.2.tar.gz
apr-iconv-1.2.2.tar.gz       libevent-2.1.8-stable.tar.gz  redis-4.0.12
apr-util-1.6.1.tar.gz        memcached-1.5.12              redis-4.0.12.tar.gz
guns-admin-1.0.0.war         memcached-1.5.12.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# cd redis-4.0.12
# Makefile 存在这个问题，就不需要 ./configure --prefix=... 这步，直接 make && make install
[root@iZuf6iq8e7ya9v3ix71k0pZ redis-4.0.12]# ls
00-RELEASENOTES  COPYING  Makefile   redis.conf       runtest-sentinel  tests
BUGS             deps     MANIFESTO  runtest          sentinel.conf     utils
CONTRIBUTING     INSTALL  README.md  runtest-cluster  src
[root@iZuf6iq8e7ya9v3ix71k0pZ redis-4.0.12]# make
# 默认会安装到 usr/local 目录下
[root@iZuf6iq8e7ya9v3ix71k0pZ redis-4.0.12]# make install PREFIX=/opt/install/redis
[root@iZuf6iq8e7ya9v3ix71k0pZ redis-4.0.12]# cd /opt/install/redis/
[root@iZuf6iq8e7ya9v3ix71k0pZ redis]# ls
bin
# copy 配置文件
[root@iZuf6iq8e7ya9v3ix71k0pZ redis]# cp /opt/software/redis-4.0.12/redis.conf /opt/install/redis/
[root@iZuf6iq8e7ya9v3ix71k0pZ redis]# ls
bin  redis.conf
```

Redis 之 Java 客户端

* 官方推荐之 Jedis
* 优秀的第三方客户端，比如：Redisson
* 优秀的第三方代理，比如 Twemproxy、Codis

Codis 介绍

* Codis 是豌豆荚开发的分布式 Redis 解决方案
* 线上动态配置实现无限扩容、插槽分配和漂移
* 单节点和集群操作对客户端透明
* 线上可以在一定程度上达到一个资源漂移的效果

Codis 组成部分

| 命令            | 命令作用                         |
| --------------- | -------------------------------- |
| Codis Dashboard | Codis 集群管理工具               |
| Codis Proxy     | 集群代理，代理多个 Codis Server  |
| Codis Server    | 替代了传统的 Redis-Server        |
| Codis Group     | 组的概念，可以让集群出现更多用法 |
| Codis Admin     | 集群管理的命令行工具             |
| Codis FE        | 图形界面处理                     |

```shell
# 上传文件
sftp> put /Users/smniuhe/Downloads/codis/* /opt/software/bak
Uploading /Users/smniuhe/Downloads/codis/codis3.2.2-go1.9.2-linux.tar.gz to /opt/software/bak/codis3.2.2-go1.9.2-linux.tar.gz
/Users/smniuhe/Downloads/codis/codis3.2.2-go1.9.2-l 100%   31MB   3.9MB/s   00:08
Uploading /Users/smniuhe/Downloads/codis/glibc-2.22.tar.gz to /opt/software/bak/glibc-2.22.tar.gz
/Users/smniuhe/Downloads/codis/glibc-2.22.tar.gz    100%   25MB   3.4MB/s   00:07
Uploading /Users/smniuhe/Downloads/codis/go1.11.5.linux-amd64.tar.gz to /opt/software/bak/go1.11.5.linux-amd64.tar.gz
/Users/smniuhe/Downloads/codis/go1.11.5.linux-amd64 100%  134MB   3.3MB/s   00:40
Uploading /Users/smniuhe/Downloads/codis/zookeeper-3.4.13.tar.gz to /opt/software/bak/zookeeper-3.4.13.tar.gz
/Users/smniuhe/Downloads/codis/zookeeper-3.4.13.tar 100%   35MB   2.6MB/s   00:13
sftp>
[root@iZuf6iq8e7ya9v3ix71k0pZ bak]# ls
codis3.2.2-go1.9.2-linux.tar.gz  go1.11.5.linux-amd64.tar.gz
glibc-2.22.tar.gz                zookeeper-3.4.13.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ bak]# pwd
/opt/software/bak

# 安装 go 环境
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# mv bak/ condis
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# ls
apache-tomcat-9.0.14.tar.gz  libevent-2.1.8-stable.tar.gz
apr-1.6.5.tar.gz             memcached-1.5.12
apr-iconv-1.2.2.tar.gz       memcached-1.5.12.tar.gz
apr-util-1.6.1.tar.gz        openresty-1.13.6.2
condis                       openresty-1.13.6.2.tar.gz
guns-admin-1.0.0.war         redis-4.0.12
guns-rest-0.0.1.war          redis-4.0.12.tar.gz
libevent-2.1.8-stable
[root@iZuf6iq8e7ya9v3ix71k0pZ software]# cd condis/
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# ls
codis3.2.2-go1.9.2-linux.tar.gz  go1.11.5.linux-amd64.tar.gz
glibc-2.22.tar.gz                zookeeper-3.4.13.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# tar -zxf go1.11.5.linux-amd64.tar.gz -C /opt/install/
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# ls
apache-tomcat-9.0.14  apr-iconv-1.2.2  libevent   openresty
apr-1.6.5             apr-util-1.6.1   memcached  redis
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# ls
apache-tomcat-9.0.14  apr-iconv-1.2.2  go       libevent   openresty  tmp
apr-1.6.5             apr-util-1.6.1   gocache  memcached  redis
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# mkdir go
mkdir: 无法创建目录"go": 文件已存在
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# mkdir go1.11.5
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# ls
apache-tomcat-9.0.14  apr-iconv-1.2.2  go        gocache   memcached  redis
apr-1.6.5             apr-util-1.6.1   go1.11.5  libevent  openresty  tmp
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# mv go go1.11.5/
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# ls go1.11.5/
go
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# mv gocache/ go1.11.5/
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# go tmp go1.11.5/
-bash: go: 未找到命令
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# mv tmp go1.11.5/
[root@iZuf6iq8e7ya9v3ix71k0pZ install]# ls go1.11.5/
go  gocache  tmp
[root@iZuf6iq8e7ya9v3ix71k0pZ go]# vim /etc/profile
...
...
# GO_HOME
export GOROOT=/opt/install/go1.11.5/go
export GOPATH=/opt/install/go_path/codis
export PATH=$GOROOT/bin:$PATH
[root@iZuf6iq8e7ya9v3ix71k0pZ go]# source /etc/profile



# glibc 安装
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# tar -zxf glibc-2.22.tar.gz
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# cd glibc-2.22
[root@iZuf6iq8e7ya9v3ix71k0pZ glibc-2.22]# mkdir build
[root@iZuf6iq8e7ya9v3ix71k0pZ glibc-2.22]# cd build/
[root@iZuf6iq8e7ya9v3ix71k0pZ build]# ../configure --prefix=/usr
[root@iZuf6iq8e7ya9v3ix71k0pZ build]# make -j2
...
...
巨大耗时时长
[root@iZuf6iq8e7ya9v3ix71k0pZ build]# make install

# zookeeper 安装
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# tar -zxf zookeeper-3.4.13.tar.gz -C ../../install/
[root@iZuf6iq8e7ya9v3ix71k0pZ condis]# cd ../../install/zookeeper-3.4.13/
[root@iZuf6iq8e7ya9v3ix71k0pZ zookeeper-3.4.13]# cd conf
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# ls
configuration.xsl  log4j.properties  zoo_sample.cfg
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# mv zoo_sample.cfg zoo.cfg
[root@iZuf6iq8e7ya9v3ix71k0pZ conf]# cd ../bin/
[root@iZuf6iq8e7ya9v3ix71k0pZ bin]# ls
README.txt    zkCli.sh   zkServer.cmd         zkTxnLogToolkit.sh
zkCleanup.sh  zkEnv.cmd  zkServer.sh
zkCli.cmd     zkEnv.sh   zkTxnLogToolkit.cmd
[root@iZuf6iq8e7ya9v3ix71k0pZ bin]# ./zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/install/zookeeper-3.4.13/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED



```

