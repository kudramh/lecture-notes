package com.kudram.springcloud.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kudram.springcloud.microservice.service.client.ExternalServiceClient;
import com.kudram.springcloud.microservice.service.logic.ServiceLogic;
import com.kudram.springcloud.microservice.vo.ServiceMessage;

@RestController
@RequestMapping(value="v1/echo/message")
public class ServiceController {
    
	@Autowired
    private ServiceLogic serviceLogic;
	
	@Autowired
	private ExternalServiceClient client;

    @RequestMapping(value="/service",method = RequestMethod.POST)
    public ServiceMessage echoService( @PathVariable("echoMessage") String echoMessage) {    	
    	ServiceMessage message = serviceLogic.doLogic(echoMessage);    	
        return message;
    }

    @RequestMapping(value="/client/discovery",method = RequestMethod.GET)
    public ServiceMessage echoDiscoveryClient( @PathVariable("echoMessage") String echoMessage) {    	
    	return client.getRemoteServiceByDiscoveryClient(echoMessage);
    }

    @RequestMapping(value="/client/ribbon",method = RequestMethod.GET)
    public ServiceMessage echoRibbonClient( @PathVariable("echoMessage") String echoMessage) {
    	return client.getRemoteServiceByRibbonClient(echoMessage);
    }

    @RequestMapping(value="/client/feign",method = RequestMethod.GET)
    public ServiceMessage echoFeignClient( @PathVariable("echoMessage") String echoMessage) {
    	return client.getRemoteServiceByFeignClient(echoMessage);
    }
    
}
