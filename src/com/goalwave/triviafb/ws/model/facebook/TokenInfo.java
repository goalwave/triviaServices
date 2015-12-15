package com.goalwave.triviafb.ws.model.facebook;

import com.goalwave.triviafb.ws.model.Base;
import com.restfb.Facebook;

public class TokenInfo extends Base {

	
	private static final long serialVersionUID = 1L;

	@Facebook("data")
	public TokenInfoData data;
	
}

