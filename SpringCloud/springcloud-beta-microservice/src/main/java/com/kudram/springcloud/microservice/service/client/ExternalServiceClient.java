package com.kudram.springcloud.microservice.service.client;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

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
		String serviceUri = String.format("%s/v1/echo/message/service", 
			instances.get(0).getUri().toString()
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
			String.format("http://%s/v1/echo/message/service", config.getEchoMessageRemoteService()),
			HttpMethod.POST,
			null, 
			ServiceMessage.class, 
			echoMessage
		);
		return restExchange.getBody();		
	}
	
	@Autowired
	ExternalFeignClient feignClient;
//	@HystrixCommand(
//		fallbackMethod = "getRemoteServiceFallback",			
//		commandProperties={
//			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000")
//		}
//	)
    @HystrixCommand(	
    	fallbackMethod = "getRemoteServiceFallback",
	    threadPoolKey = "remoteServiceThreadPool",
	    threadPoolProperties =
	            {@HystrixProperty(name = "coreSize",value="30"),
	             @HystrixProperty(name="maxQueueSize", value="10")},
	    commandProperties={
	    		 @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
	             @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
	             @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
	             @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
	             @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
	             @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}
	)
	public ServiceMessage getRemoteServiceByFeignClient(String echoMessage){
		ServiceMessage responseMessage = feignClient.getRemoteServiceByFeign(echoMessage);
		responseMessage.setClientName( config.getServiceName() );
		return responseMessage;
	}

	public ServiceMessage getRemoteServiceFallback(String echoMessage){
		ServiceMessage responseMessage = new ServiceMessage();
		responseMessage.setClientName( config.getServiceName() );
		responseMessage.setMessagePayload(
			String.format("%s - %s :: %s",
				new SimpleDateFormat("yyyy/MM/dd HH:mm:SS").format( new Date() ),
				config.getEchoMessagePrefix(),
				echoMessage
			)
		);
		responseMessage.setRemoteServiceName(config.getServiceName()+" Fallback Method" );
		return responseMessage;
	}
		
}