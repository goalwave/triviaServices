package com.goalwave.triviafb.jax;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import javax.ws.rs.ext.Provider;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;

import com.goalwave.triviafb.ws.model.AccessToken;
import com.goalwave.triviafb.ws.model.facebook.TokenInfo;
import com.goalwave.triviafb.ws.service.AccessTokenService;
import com.goalwave.triviafb.ws.service.FBTokenService;
import com.goalwave.triviafb.ws.service.GoogleTokenService;
import com.goalwave.triviafb.ws.util.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JaxRsFilterAuthentication implements ContainerRequestFilter {
	private static final Logger LOGGER = Logger.getLogger(JaxRsFilterAuthentication.class.getName());

	public static final String AUTHENTICATION_HEADER = "Authorization";
	public static final String USERNAME_HEADER = "X-Username";
	
	@Context
	private HttpServletRequest servletReq;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws WebApplicationException {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = 
           requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        String usernameHeader = requestContext.getHeaderString(USERNAME_HEADER);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        if(usernameHeader == null){
        	throw new NotAuthorizedException("X-Username header must be provided");
        }
        
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
        
        try {

            // Validate the token
            validateToken(token, usernameHeader);

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }	

        servletReq.setAttribute("emailAddress", usernameHeader);

	}
	
	   private void validateToken(String token, String username) throws Exception {
		   // Check if it was issued by the server and if it's not expired
		   // Throw an Exception if the token is invalid
		   AccessTokenService ats = new AccessTokenService();	
		   AccessToken at = ats.getByEmailAddress(username); 

		   if(!at.getEmail().equals(username)){
			   throw new Exception("X-Username does not match user email");
		   }
		   	
		   if(!at.getToken().equals(token)){
			   throw new Exception("Bearer token does not match");
		   }
		   
		   
		   switch(at.getType()){
		   		case FACEBOOK:
		   			validateFBToken(at);
		   			break;
		   		case GOOGLE:
		   			validateGoogleToken(at);
		   			break;
		   		default:	
		   			LOGGER.info("Received an unexpected access type????");
		   			throw new Exception("Received an unexpected access type????");
		   }
	    }

	   private void validateFBToken(AccessToken at) throws Exception{
		   FBTokenService fbts = new FBTokenService();
		   TokenInfo tokenInfo = fbts.getTokenInfo(at);
		   
		   if(!tokenInfo.data.isValid)
			   throw new Exception("Token is not valid");
		   //Perform other checks once I know what they might be!!!		   
	   }
	   
	   private void validateGoogleToken(AccessToken at) throws Exception{
		   GoogleTokenService gts = new GoogleTokenService();
		   com.goalwave.triviafb.ws.model.google.TokenInfo tokenInfo = gts.getTokenInfo(at.getToken());

		   LOGGER.info("verified user " + tokenInfo.getEmailAddress());
		   //Perform other checks once I know what they might be!!!!
	   }
	   

}
