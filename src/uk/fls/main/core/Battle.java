package uk.fls.main.core;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.entitys.printermon.Printermon;

public class Battle {

	
	
	private Printermon playerCurrent;
	private Printermon opponentCurrent;
	
	private SpriteParser spp;
	private SpriteParser spo;
	
	private boolean win;
	private boolean lose;
	
	public Battle(Printermon[] player, Printermon[] opp){
		this.playerCurrent = player[getFirstAlive(player)];
		this.opponentCurrent = opp[getFirstAlive(opp)];
		
		this.spp = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/monsters/" + this.playerCurrent.getName() + "-back.art"));
		if(this.playerCurrent != null)this.spp = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/monsters/" + this.opponentCurrent.getName() + "-front.art"));
	}
	
	public void render(Renderer r, int x, int y){
		r.fill(184);
		r.renderSpriteParserSheet(spo, 10, 10);
	}
	
	public void update(){
		
	}
	
	public boolean isWin(){
		return this.win;
	}
	
	public boolean isLose(){
		return this.lose;
	}
	
	private int getFirstAlive(Printermon[] mon){
		for(int i = 0; i < mon.length; i++){
			if(mon[i].isAlive())return i;
		}
		return 0;
	}
}
