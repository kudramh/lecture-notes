package com.kudram.springcloud.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

  @Value("${echo.message.preffix}")
  private String echoMessagePrefix="";

  @Value("${echo.message.remote.service}")
  private String echoMessageRemoteService="";

  @Value("${spring.application.name}")
  private String serviceName="";
  
  public String getEchoMessagePrefix(){
    return echoMessagePrefix;
  }
  
  public String getEchoMessageRemoteService(){
	return echoMessageRemoteService;	  
  }

  public String getServiceName(){
	return serviceName;	  
  }
  
}
