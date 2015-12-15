package com.goalwave.triviafb.ws.service;

import java.util.Arrays;
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
import com.goalwave.triviafb.ws.model.google.TokenInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


	
@Path("/gootoken")
public class GoogleTokenService {
	private final static String clientIdProperty = "google_client_id";
	private static String clientId = null;
	
	private static final Logger LOGGER = Logger.getLogger(FBTokenService.class.getName());

	public GoogleTokenService(){
		Config config = ConfigFactory.load();
		clientId = config.getString(clientIdProperty);
	}

	@GET
    @Path("/{idToken}")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AccessToken getUser(@PathParam("idToken") String idToken, @Context HttpServletRequest req) throws Exception{

		TokenInfo tokenInfo = getTokenInfo(idToken);
			
		
		LOGGER.info("Verified user");
		
		//let's find the user by their email address
		UserService us = new UserService();
		User persistedUser = us.getOrCreateUserByEmail(tokenInfo.getEmailAddress());
		
		AccessToken at = new AccessToken(AccessToken.Type.GOOGLE, idToken, persistedUser.getEmail(), persistedUser.getUserId());
		
		// Let's persist the AccessToken...
		AccessTokenService ats = new AccessTokenService();
		ats.addOrUpdate(at);
		
		return at;
		
	}

	public TokenInfo getTokenInfo(String idToken) throws Exception{
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory).setAudience(Arrays.asList(clientId)).build();

		TokenInfo tokenInfo = null;
		
		GoogleIdToken accessToken = verifier.verify(idToken);
		if (accessToken != null) {
			Payload payload = accessToken.getPayload();
			tokenInfo = new TokenInfo(payload.getEmail(), payload.getSubject());
		} else {
			throw new Exception("access token not valid!!!");
		}
		
		return tokenInfo;
		
	}
	
}

