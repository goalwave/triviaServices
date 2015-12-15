package com.goalwave.triviafb.ws.service;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.goalwave.triviafb.ws.model.AccessToken;
import com.goalwave.triviafb.ws.model.User;
import com.goalwave.triviafb.ws.model.facebook.LongTermAccessToken;
import com.goalwave.triviafb.ws.model.facebook.TokenInfo;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Path("/fbtoken")
public class FBTokenService {
	
	private final static String appIdProperty = "facebook_app_id";
	private final static String secretProperty = "facebook_secret";
	private static String appId = null;
	private static String secret = null;

	private static final Logger LOGGER = Logger.getLogger(FBTokenService.class.getName());

	public FBTokenService(){
		Config config = ConfigFactory.load();
		appId = config.getString(appIdProperty);
		secret = config.getString(secretProperty);
	}
	
	
	@GET
    @Path("/{stAccessToken}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AccessToken getUser(@PathParam("stAccessToken") String stAccessToken, @Context HttpServletRequest req) throws Exception{
		FacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_2_5);
		
		LongTermAccessToken longTermAT = facebookClient.fetchObject("oauth/access_token", LongTermAccessToken.class, 
				Parameter.with("grant_type", "fb_exchange_token"),
				Parameter.with("client_id", appId),
				Parameter.with("client_secret", secret),
				Parameter.with("fb_exchange_token", stAccessToken));
		
		String email = (String)req.getParameter("email");

		
		//let's find the user by their email address
		UserService us = new UserService();
		User persistedUser = us.getOrCreateUserByEmail(email);
		
		AccessToken at = new AccessToken(AccessToken.Type.FACEBOOK, longTermAT.accessToken, persistedUser.getEmail(), persistedUser.getUserId());
		
		// Let's persist the AccessToken...
		AccessTokenService ats = new AccessTokenService();
		ats.addOrUpdate(at);
		
		return at;
		
    }
	
	public TokenInfo getTokenInfo(AccessToken at) throws Exception{
		com.restfb.FacebookClient.AccessToken accessToken =
				new DefaultFacebookClient(Version.VERSION_2_5).obtainAppAccessToken(appId, secret);
		LOGGER.info("Token: " + accessToken.getAccessToken());
		LOGGER.info("user token" + at.getToken());
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_5);
		
		
		TokenInfo tokenInfo = facebookClient.fetchObject("debug_token", TokenInfo.class, 
				Parameter.with("input_token", at.getToken()));
		return tokenInfo;
	
	}
}
