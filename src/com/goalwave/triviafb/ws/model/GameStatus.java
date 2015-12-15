package com.goalwave.triviafb.ws.model;


public class GameStatus extends Base {
	private static final long serialVersionUID = 1L;

	
	private int homeScore;
	
	private int awayScore;
	
	public GameStatus(int home, int away){
		homeScore = home;
		awayScore = away;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	
	
	
}
