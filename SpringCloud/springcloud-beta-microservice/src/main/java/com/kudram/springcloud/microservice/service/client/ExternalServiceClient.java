package com.kudram.springcloud.microservice.service.client;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kudram.springcloud.microservice.config.ServiceConfig;
import com.kudram.springcloud.microservice.vo.ServiceMessage;

@Component
public class ExternalServiceClient {
	
    @Autowired
    private ServiceConfig config;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	public ServiceMessage getRemoteServiceByDiscoveryClient(String echoMessage){
		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> instances = discoveryClient.getInstances( config.getEchoMessageRemoteService() );
		if (instances.size()==0) {
			return null;
		}
		String serviceUri = String.format("%s/v1/echo/message", 
			instances.get(0).getUri().toString(),
			echoMessage
		);
		ResponseEntity<ServiceMessage> restExchange = restTemplate.exchange(
			serviceUri,
			HttpMethod.POST,
			null, 
			ServiceMessage.class, 
			echoMessage
		);
		return restExchange.getBody();		
	}

	@Autowired
	RestTemplate lbRestTemplate;
	public ServiceMessage getRemoteServiceByRibbonClient(String echoMessage){
		ResponseEntity<ServiceMessage> restExchange = lbRestTemplate.exchange(
			String.format("http://%s/v1/echo/message", config.getEchoMessageRemoteService()),
			HttpMethod.POST,
			null, 
			ServiceMessage.class, 
			echoMessage
		);
		return restExchange.getBody();		
	}
	
	@Autowired
	ExternalFeignClient feignClient;
	public ServiceMessage getRemoteServiceByFeignClient(String echoMessage){
		return feignClient.getRemoteServiceByFeign(echoMessage);
	}

}