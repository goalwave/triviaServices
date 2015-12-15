package com.goalwave.triviafb.ws.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.goalwave.triviafb.ws.model.GameStatus;
import com.goalwave.triviafb.ws.util.Secured;

@Secured
@Path("game")
public class GameService {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
	public List<GameStatus> getAllGames() throws Exception{
		GameStatus gs = new GameStatus(14, 7);
		
		List<GameStatus> gsl = new ArrayList<GameStatus>();
		gsl.add(gs);
		return gsl;
	
	}
	
}
