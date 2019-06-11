package com.theorydance.myone.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
		SpringApplication.run(CloudGatewayApplication.class, args);
	}
	
	@Bean
	public PreGatewayFilterFactory preGatewayFilterFactory() {
		return new PreGatewayFilterFactory();
	}
	
	@Bean
	public GlobalFilter a() {
		return new GatewayContextFilter();
	}
	
}
