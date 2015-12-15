package com.goalwave.triviafb.jax;

import org.glassfish.jersey.server.ResourceConfig;

public class JaxRsApplication extends ResourceConfig {
	public JaxRsApplication(){
		register(CORSResponseFilter.class);
	}
}
