package com.goalwave.triviafb.ws.model.facebook;

import java.util.List;

import com.goalwave.triviafb.ws.model.Base;
import com.restfb.Facebook;

public class TokenInfoData extends Base {

		private static final long serialVersionUID = 1L;
	
	@Facebook("app_id")
	public String appId;

	@Facebook("application")
	public String application;
	
	@Facebook("is_valid")
	public boolean isValid;
	
	@Facebook("scopes")
	public List<String> scopes;
	
	@Facebook("user_id")
	public String userId;

	
	public TokenInfoData(){}
	
	
	
}
