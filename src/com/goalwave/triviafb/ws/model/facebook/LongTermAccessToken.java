package com.goalwave.triviafb.ws.model.facebook;

import com.goalwave.triviafb.ws.model.Base;
import com.restfb.Facebook;

public class LongTermAccessToken extends Base {
	private static final long serialVersionUID = 1L;

	@Facebook("access_token")
	public String accessToken;

	@Facebook("expires_in")
	public String expiresIn;
	
	@Facebook("token_type")
	public String tokenType;
	
	
	
}
