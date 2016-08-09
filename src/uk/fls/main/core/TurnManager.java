package uk.fls.main.core;

public class TurnManager {

	
	public boolean playerMoved;
	public boolean playerHealthAnimated;
	public boolean playerAttackUsed;
	public boolean playerAttackShown;
	public boolean playerAttackMutlShown;
	public boolean playerDone;
	public boolean playerActive;
	
	public boolean opponentMoved;
	public boolean opponentHealthAnimated;
	public boolean opponentAttackUsed;
	public boolean opponentAttackMutlShown;
	public boolean opponentDone;
	public boolean opponentAttackShown;
	public boolean opponentActive;
	
	
	
	public void flush(){
		this.playerMoved = false;
		this.playerHealthAnimated = false;
		this.playerAttackMutlShown = false;
		this.playerAttackUsed = false;
		this.playerDone = false;
		this.playerAttackShown = false;
		
		this.opponentAttackMutlShown = false;
		this.opponentAttackUsed = false;
		this.opponentDone = false;
		this.opponentHealthAnimated = false;
		this.opponentMoved = false;
		this.opponentAttackShown = false;
	}
	
	public void playerMoving(){
		this.playerActive = true;
	}
	
	public void playerResting(){
		this.playerActive = false;
	}
	
	public void opponentMoving(){
		this.opponentActive = true;
	}
	
	public void opponentResting(){
		this.opponentActive = false;
	}
}
