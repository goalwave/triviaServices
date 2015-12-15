package com.goalwave.triviafb.ws.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccessToken extends Base {
	
	
	public static enum Type{
		FACEBOOK, 
		GOOGLE
	}
	
	private static final long serialVersionUID = 1L;

	private Type type;
	
	private String token;
	
	private String email;
		
	@Id
	private int userId;
	
	public AccessToken(Type t, String tok, String em, int usrId){
		this.token = tok;
		this.email = em;
		this.userId = usrId;
		this.type = t;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String em) {
		this.email = em;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	

}
