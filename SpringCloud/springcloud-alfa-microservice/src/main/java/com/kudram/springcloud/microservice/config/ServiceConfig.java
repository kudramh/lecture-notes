package com.kudram.springcloud.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

  @Value("${echo.message.preffix}")
  private String echoMessagePrefix="";

  @Value("${echo.message.remote.service}")
  private String echoMessageRemoteService="";
  
  public String getEchoMessagePrefix(){
    return echoMessagePrefix;
  }
  
  public String getEchoMessageRemoteService(){
	return echoMessageRemoteService;	  
  }
  
}
