package com.theorydance.myone.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import com.theorydance.myone.gateway.entity.GatewayContext;

public class PreGatewayFilterFactory extends AbstractGatewayFilterFactory<PreGatewayFilterFactory.Config> {
	private final static String X_TOKEN = "X-Token";
	public PreGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			System.out.println(exchange.getRequest().getPath().toString());
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath(); // resource path
            String method = request.getMethodValue();
            
            GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
            // 得到请求资源路径、请求参数
            MultiValueMap<String, String> params = gatewayContext.getFormData();            
            params.putAll(gatewayContext.getQueryParams());
            
            // 1、判断是否登陆接口（登陆接口不需要权限即可访问），可能不同产品的登陆接口参数不一样
            if("/p1upgrade/sysManage/userCheck121".equals(path)) {
            	// 判断是否含有指定的参数
            	if("POST".equalsIgnoreCase(method)) {
            		if(gatewayContext.getFormData().keySet().contains("passport")&&
            				gatewayContext.getFormData().keySet().contains("pwd")) {
            			return chain.filter(exchange);
            		}
            	}
            	if(gatewayContext.getQueryParams().keySet().contains("passport")&&
        				gatewayContext.getQueryParams().keySet().contains("pwd")) {
        			return chain.filter(exchange);
        		}
            }else { // 非登陆接口
            	// 兼容原来的模式（在cookie中或请求参数中含有token），将其权限标志规范会到请求头中
            	MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            	HttpHeaders headers = request.getHeaders();
            	if(headers.keySet().contains(X_TOKEN)) {
                	return chain.filter(exchange);
                }else if(params.keySet().contains("apiKey")){
                	try {
                		// 得到token的值，将其放置到请求头中
						String token = params.getFirst("apiKey");
						request = request.mutate().header(X_TOKEN, token).build();
						return chain.filter(exchange.mutate().request(request).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
                }else if(params.keySet().contains("token")){
                	try {
                		// 得到token的值，将其放置到请求头中
						String token = params.getFirst("token");
						request = request.mutate().header(X_TOKEN, token).build();
						return chain.filter(exchange.mutate().request(request).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
                }else if(cookies.keySet().contains("token")){
                	try {
                		// 得到token的值，将其放置到请求头中
						String token = cookies.getFirst("token").getValue();
						request = request.mutate().header(X_TOKEN, token).build();
						return chain.filter(exchange.mutate().request(request).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            }
            // 不是登陆接口，或没有访问权限，返回固定信息
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
		};
	}

	public static class Config {}

}
