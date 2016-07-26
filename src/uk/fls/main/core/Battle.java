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
	private SpriteParser gui;
	private SpriteParser font;
	
	private boolean win;
	private boolean lose;
	
	private final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz0123456789i.:";
	public Battle(Printermon[] player, Printermon... opp){
		this.playerCurrent = player[getFirstAlive(player)];
		this.opponentCurrent = opp[getFirstAlive(opp)];
		
		this.spp = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/monsters/" + this.playerCurrent.getName() + "-back.art"));
		this.spo = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/monsters/" + this.opponentCurrent.getName() + "-front.art"));
		this.gui = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/battlegui.art"));
		this.font = new SpriteParser(8, FileIO.instance.readInternalFile("/font/font.art"));
	}
	
	public void render(Renderer r, int x, int y){
		r.fill(r.makeRGB(188, 188, 188));
		r.renderSpriteParserSheet(spo, 96, 10);
		String opponentHealthString = "HP: " + this.opponentCurrent.getHealth() + "/" + this.opponentCurrent.getMaxHealth();
		int oppoenentHealthLength = opponentHealthString.length();
		int xo = 1;
		renderString(r, opponentHealthString, (xo + 1) * 8, 16);
		r.renderSection(gui.getData(0, 0), xo * 8, 16, 8);
		for(int i = 0; i < oppoenentHealthLength; i++){
			r.renderSection(gui.getData(1, 0), ((xo + 1) * 8) + i * 8, 16, 8);
		}
		r.renderSection(gui.getData(2, 0), (xo + oppoenentHealthLength + 1) * 8, 16, 8);
		
		
		

		r.renderSpriteParserSheet(spp, 16, 144 - 72 - 8 * 2);
		
		for(int yy = 0; yy < 32; yy++){
			for(int xx = 0; xx < 160; xx++){
				r.setPixel(xx, 144 - 32 + yy, 255 << 8);
			}
		}
	}
	private void renderString(Renderer r, String s, int x, int y){
		for(int i = 0; i < s.length(); i++){
			int index = s.charAt(i);
			int pos = this.letters.indexOf(index);
			if(pos == -1)continue;
			int dx = pos % 8;
			int dy = pos / 8;
			r.renderSection(font.getData(dx,dy), x + i * 8, y, 8);
		}
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
