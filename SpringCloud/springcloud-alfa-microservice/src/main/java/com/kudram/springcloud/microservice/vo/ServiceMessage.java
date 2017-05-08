package com.kudram.springcloud.microservice.vo;

public class ServiceMessage {
	
	private String remoteName;
	private String clientName;
	private String msgPayload;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getMessagePayload() {
		return msgPayload;
	}
	public void setMessagePayload(String msgPayload) {
		this.msgPayload = msgPayload;
	}

	public void setRemoteServiceName(String remoteName){
		this.remoteName = remoteName;
	}	
	public String getRemoteServiceName(){
		return remoteName;
	}

}
