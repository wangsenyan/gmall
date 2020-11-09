package com.dubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * 1. application.properties
 * 2. @EnableDubbo
 * 3. @DubboReference
 */
@EnableHystrix
@EnableDubbo
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");
//		OrderService orderService =  applicationContext.getBean(OrderService.class);
//		List<UserAddress> addressList =  orderService.initOrder("1");
//		for(UserAddress userAddress:addressList){
//			System.out.println("什么鬼" + userAddress.getName());
//		}
//		System.out.println("调用成功");
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		SpringApplication.run(ConsumerApplication.class, args);
	}

}
