package com.kudram.springcloud.microservice.service.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kudram.springcloud.microservice.vo.ServiceMessage;

@FeignClient( name = "${echo.message.remote.service}")
public interface ExternalFeignClient {
    
	@RequestMapping(
	    method= RequestMethod.POST,
	    value="/v1/echo/message/service",
	    consumes="application/json"
    )
    public ServiceMessage getRemoteServiceByFeign(@RequestBody String echoMessage);

}
