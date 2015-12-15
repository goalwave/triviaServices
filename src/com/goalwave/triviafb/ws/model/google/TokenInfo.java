package com.goalwave.triviafb.ws.model.google;

public class TokenInfo {
	private String emailAddress;
	
	private String id;
	
	public TokenInfo(String email, String pId){
		emailAddress = email;
		id = pId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
