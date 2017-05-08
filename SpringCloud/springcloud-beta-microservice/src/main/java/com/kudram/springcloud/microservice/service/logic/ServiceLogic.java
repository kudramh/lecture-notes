package com.kudram.springcloud.microservice.service.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kudram.springcloud.microservice.config.ServiceConfig;
import com.kudram.springcloud.microservice.vo.ServiceMessage;

@Component
public class ServiceLogic{
	
    @Autowired
    private ServiceConfig config;
    
    @Value("${spring.application.name}")
    private String appName;
    
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:SS");

	public ServiceMessage doLogic(String echoMessage){
		ServiceMessage serviceMsg = new ServiceMessage();
		serviceMsg.setMessagePayload(
			String.format("%s - %s :: %s",
				dateFormatter.format( new Date() ),
				config.getEchoMessagePrefix(),
				echoMessage
			)
		);
		serviceMsg.setRemoteServiceName(appName);
		return serviceMsg;
	}
}