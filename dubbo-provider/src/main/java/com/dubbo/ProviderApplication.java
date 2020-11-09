package com.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import java.io.IOException;
//--add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true
/**
 * 1. 导入依赖
 *    1） 导入dubbo-starter
 *    2) 导入dubbo的其他依赖
 *
 * SpringBoot与dubbo整合的三种方式
 * 1） 导入dubbo-starter,在application.properties配置属性,
 *     使用@Service暴露服务,使用@Reference引用服务 @EnableDubbo(dubbo.scan.base-packages=)
 * 2)  暴露dubbo的xml配置文件
 *     导入dubbo-starter 使用@ImportResource(locations = "classpath:provider.xml") + @SpringBootApplication
 * 3） 使用注解API方式
 *     将每一个组件手动创建到容器中 DubboConfig.java
 *     让dubbo扫描组件 + @DubboComponentScan(basePackages = "com.dubbo.service")
 *
 *一。dubbo属性加载顺序
 *   1. java  e.g. -Ddubbo.protocol.port=20880
 *   2. dubbo.xml  <dubbo:protocol port="30880">
 *   3. dubbo.properties dubbo.protocol.port=40880
 *二。启动检测
 *   1. (消费端)启动时检测
 *      - <dubbo:reference interface="",check="false"></dubbo:reference>
 *      - @DubboReference(check = false)
 *      - application.properties:  dubbo.consumer.check=false  所有都不检查
 *三。超时检查
 *   1. (消费者) 默认使用dubbo:consumer的timeout 1000ms
 *     - <dubbo:reference interface="",timeout="3000"></dubbo:reference> 单位ms
 *     - @DubboReference(timeout = 6000)
 *     - application.properties:  dubbo.consumer.timeout=2000
 *   2. 配置的覆盖关系 - 精确优先,消费优先
 *     a. <dubbo:reference interface="">     //方法级别
 *           <dubbo:method name="" timeout="1000"/>
 *        </dubbo:reference>
 *     b. <dubbo:service interface="">
 *           <dubbo:method name="" timeout="2000"/>
 *        </dubbo:service>
 *     c. <dubbo:reference interface="" timeout="3000"/> //接口级别
 *     d. <dubbo:service interface=" timeout="4000"/>
 *     e. <dubbo:consumer timeout="5000"/> //统一设置
 *     f. <dubbo:provider timeout="6000"/> //统一设置
 *四。重试次数
 *  1. 消费方
 *    - @DubboReference(retries = 2) 调用总共3次
 *  2. 幂等(设置重试次数)【查询，删除，修改】 非幂等(不能设置重试参数)【新增】
 *五。多版本 - 灰度发布
 *  1. 在提供方 @DubboService(version = "2.0.0")
 *  2. 在服务方 @DubboReference(version = "2.0.0")
 *  3. @DubboReference(version = "*") 随机调用
 *
 * 六。本地存根(本地进行校验)
 *  1. 使用
 *    - @DubboReference(stub = "com.dubbo.service.impl.UserServiceStub") 对接口
 *    - <dubbo:reference interface="" stub = "com.dubbo.service.impl.UserServiceStub"/>
 *
 *七。高可用
 *   1. zookeeper宕机与dubbo直连
 *     注册中心全部宕掉后,服务提供者和服务消费者仍能通过本地缓存通信
 *   2. 直连
 *八。负载均衡
 *  - 分类
 *  1. Random LoadBalance 权重随机 random
 *  2. RoundRobin LoadBalance 权重轮询 roundrobin
 *  3. LeastActive LoadBalance 最少活跃数 leastactive
 *  4. ConsistentHash LoadBalance 一致性哈希  consistenthash
 *  - 设置
 *  1.     @DubboReference(loadbalance = "roundrobin") 消费者设置轮询方式
 *  2.     @DubboService(weight = 100) 生产者设置权重
 *
 *九。服务降级
 * - 强制降级 mock=force:return+null (在控制台 屏蔽)
 * - 失败降级 mock=fail:return+null  (在控制台 容错)
 *十。服务容错
 * - 模式
 *	 1. Failover Cluster
 *	 2. FailFast Cluster
 *	 3. Failsafe Cluster
 *	 4. Failback Cluster
 *	 5. Forking  Cluster
 *	 6. Broadcast Cluster
 * - 设置 generate->dependency
 *   <dubbo:service cluster="failsafe"/>
 *   <dubbo:reference cluster="failsafe"/>
 * - hystrix（spring-cloud-starter-netflix-hystrix）
 *  1. 具备拥有回退机制和断路器功能的线程和信号隔离，
 *     请求缓存和请求打包,以及监控和配置等功能
 *  2. @EnableHystrix //开启服务容错 provider
 *     @HystrixCommand //  provider
 *
 * 十一。RPC原理
 * 1. 服务消费方(client)调用以本地方法调用服务
 *  1） client stub接收到调用后负责将方法、参数等组装成能够进行网络传输的消息体
 *  2） client stub找到服务地址,并将消息发送到服务端
 *  3） server stub收到消息后进行解码
 *  4） server stub根据解码结果调用本地的服务
 *  5） 本地服务执行并将结果返回给server stub
 *  6） server stub将返回结果打包成消息并发送至消费方
 *  7） client stub接收消息,并进行解码
 * 2. 服务消费方得到最终结果
 * 八。dubbo原理
 *   1. DubboBeanDefinitionParser
 *   2. DubboNamespaceHandler
 *   3. ServiceBean
 *     - InitializingBean -> afterPropertiesSet
 *     -
 * RPC原理
 *  1. dubbo
 *  2. netty
 * Dubbo框架
 *  1. DubboBeanDefinitionParser
 * 打包自动上传 https://blog.csdn.net/Appleyk/article/details/87367281
 *  2.docker https://blog.csdn.net/qq_33562996/article/details/80447618
 *
 *
 */
//@ImportResource(locations = "classpath:provider.xml")
//@SpringBootApplication

@EnableHystrix //开启服务容错
@EnableDubbo
@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
//		ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("provider.xml");
//		ioc.start();
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		SpringApplication.run(ProviderApplication.class, args);
	}

}
