package uk.fls.main.core;

import java.util.HashMap;

import fls.engine.main.input.Input;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.Camera;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.entitys.Player;
import uk.fls.main.core.extras.Transition;
import uk.fls.main.core.extras.Warp;
import uk.fls.main.core.gui.DialogBox;
import uk.fls.main.core.tiles.Tile;

public class World {
	
	public Tile[][] tiles;
	public byte[][] data;
	public Player p;
	private HashMap<Integer, String[]> dialogs;
	private HashMap<Integer, Warp> travel;
	private HashMap<Integer, Integer> mons;
	private int w,h;
	private SpriteParser sp;
	
	private boolean popup;
	private DialogBox currentPopup;
	
	private boolean transition;
	private Transition trans;
	
	private boolean battle;
	private Battle bat;
	
	public World(){
		LevelGen g = LevelGen.loadLevel("/level.txt");
		this.w = g.w;
		this.h =  g.h;
		this.tiles = g.tiles;
		this.data = g.data;
		this.dialogs = g.dialogs;
		this.travel = g.travel;
		this.p = new Player(16,16);
		this.sp = new SpriteParser(8, FileIO.instance.readInternalFile("/overworld.art"));
		this.popup = false;
	}

	public void update(Camera c, Input i){
		
		if(this.popup){
			this.currentPopup.update(i);
			if(this.currentPopup.finished()){
				this.popup = false;
				this.currentPopup = null;
			}
		}else if(this.transition){
			this.trans.update();
			if(this.trans.finished()){
				this.transition = false;
			}
		}else if(battle){
			this.bat.update();
			
		}else{
			int sx = c.pos.getIX()/16;
			int sy = c.pos.getIY()/16;
			int cw = c.w/16;
			int ch = c.h/16;
			for(int x = sx-1; x <= sx + cw; x++){
				for(int y = sy-1; y <= sy + ch; y++){
					getTile(x, y).tick(this, x, y);
				}
			}
			
			this.p.update();
		}
	}
	
	public void render(Camera c, Renderer r){
		if(this.popup){
			r.setOffset(0, 0);
			this.currentPopup.render(r);
		}else if(this.transition){
			r.setOffset(0, 0);
			this.trans.render(r);
		}else if(this.battle){
			r.setOffset(0, 0);
			this.bat.render(r, 0, 0);
		}else{
			r.setOffset(-c.pos.getIX(), -c.pos.getIY());
			// Set offset in renderer
			int sx = c.pos.getIX()/16;
			int sy = c.pos.getIY()/16;
			int cw = c.w/16;
			int ch = c.h/16;
			for(int x = sx-1; x <= sx + cw; x++){
				for(int y = sy-1; y <= sy + ch; y++){
					getTile(x, y).render(sp, this, r, x * 16, y * 16);
				}
			}
			// Draw tiles
			// Draw entitys
			
			// Rest offset to 0,0
			//r.setOffset(0, 0);
			//r.setOffset(0, 0);
			this.p.render(r);
		}
	}
	
	public Tile getTile(int x, int y){
		if(!isValid(x,y) || this.tiles[x][y] == null)return Tile.voidTile;
		return this.tiles[x][y];
	}
	
	public boolean isValid(int x, int y){
		if(x < 0 || y < 0 || x >= w || y >= h) return false;
		return true;
	}
	
	public void setTile(Tile t, int x, int y){
		if(!isValid(x, y))return;
		this.tiles[x][y] = t;
		setData(x, y, 0);
	}
	
	public void setTile(Tile t, int x,int y, int val){
		setTile(t, x, y);
		setData(x, y, val);
	}
	
	public byte getData(int x, int y){
		if(!isValid(x, y))return 0;
		return this.data[x][y];
	}
	
	public void setData(int x, int y, int val){
		if(!isValid(x,y))return;
		this.data[x][y] = (byte)val;
	}
	
	public void displayPopup(int id){
		if(this.popup)return;
		this.popup = true;
		String[] dia = this.dialogs.get(id);
		if(dia == null)return;
		this.currentPopup = new DialogBox(8, 120, dia);
	}
	
	public void travel(int id){
		Warp pos = this.travel.get(id);
		if(pos == null)return;
		this.transition = true;
		this.trans = new Transition();
		LevelGen g = LevelGen.loadLevel(pos.getFile());
		this.w = g.w;
		this.h =  g.h;
		this.tiles = g.tiles;
		this.data = g.data;
		this.dialogs = g.dialogs;
		this.travel = g.travel;
		this.p.getPos().setPos(pos.getWarpLoc().getIX() * 16, pos.getWarpLoc().getIY() * 16);
		this.p.moveAgain();
	}
	
	public void battle(){
		
	}
}
