
##  dubbo + zookeeper 实现微服务

### zookeeper创建集群（一台服务器实现）
1. 官网下载zookeeper
2. 在zookeeper_multi创建3个文件夹 mkdir /home/zk_multi/{2888,2889,2890}
3. 解压zookeeper到三个目录下,并创建data文件夹
4. 复制zoo_sample.cfg `cp zoo_sample.cfg zoo.cfg`
5. 编辑zoo.cfg 
```cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/zk_multi/2888/data
clientPort=2188
server.1=0.0.0.0:2888:3888
server.2=0.0.0.0:2889:3889
server.3=0.0.0.0:2890:3890
admin.serverPort=8888
```
6. 其余2个目录下的配置文件类似,不同的地方(仅仅对同一台服务器)
  - clientPort
  - admin.serverPort (默认为8080)
7. 执行 `./bin/zkServer.sh start`
8. `./bin/zkServer.sh status` 查看状态,如下表示成功
```
Client port found: 2188. Client address: localhost. Client SSL: false.
Mode: follower
```
### dubbo项目配置
1. 项目结构
---dubbo-provider  //生产者
---dubbo-consumer  //消费者
---dubbo-interface //公共接口
2. pom.xml (dubbo-provider , dubbo-consumer)
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
    <!--公共接口-->
    <dependency>
			<groupId>com.dubbo</groupId>
			<artifactId>dubbo-interface</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
    <!--dubbo-->
		<dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-recipes</artifactId>
			<version>2.8.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.dubbo</groupId>
			<artifactId>dubbo-spring-boot-starter</artifactId>
			<version>2.7.8</version>
		</dependency>
    <!--hystrix容错-->
    		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
			<version>2.2.5.RELEASE</version>
		</dependency>
    
```
3. application.properties (provider)
```properties 
dubbo.application.name=dubbo-provider

dubbo.registry.address=140.143.153.238:2188,140.143.153.238:2189,140.143.153.238:2190
dubbo.registry.protocol=zookeeper
dubbo.registry.timeout=10000
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880

dubbo.monitor.protocol=registry
```

### dubbo-admin
1. github搜索dubbo-admin,然后把项目中的dubbo-admin的application.properties的dubbo.registry.address改为
```
dubbo.registry.address=zookeeper://140.143.153.238:2188?backup=140.143.153.238:2189,140.143.153.238:2190
```
2. 打包成jar后,运行
```sh
java -jar dubbo-admin-0.0.1-SNAPSHOT.jar --server.port=8001  > log.file  2>&1 &
```
3. 浏览器打开 ip:8001
