package com.theorydance.myone.gateway.entity;

import org.springframework.util.MultiValueMap;

import lombok.Data;

@Data
public class GatewayContext {

	public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";
	private String cacheBody;
	private MultiValueMap<String, String> formData;
	private MultiValueMap<String, String> queryParams;
	private String path;
	
}