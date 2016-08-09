package uk.fls.main.core.entitys;

import java.util.ArrayList;
import java.util.List;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.entitys.printermon.Printermon;
import uk.fls.main.core.item.Item;
import uk.fls.main.core.tiles.GrassTile;
import uk.fls.main.core.tiles.Tile;

public class Player extends Entity{

	private boolean canMove;
	private int interactTime;
	private boolean justWarped;
	private boolean justBattled;
	private boolean justSpawned;
	
	private List<Item> backpack;
	private SpriteParser sp;
	private int[] data;
	
	private Printermon[] team;
	
	
	public Player(int x, int y) {
		super(x,y);
		this.interactTime = 0;
		this.justSpawned = true;
		this.backpack = new ArrayList<Item>();
		this.sp = new SpriteParser(FileIO.instance.readInternalFile("/npc/player.art"));
		this.data = sp.getData(0, 0);
		this.team = new Printermon[6];
		this.team[0] = Printermon.getPrintermonByID(0);
	}

	public void update() {
		if(this.interactTime > 0)this.interactTime --;
		
		if(this.dir != this.pdir){
			updateSprite();
		}
		
		if(!hasMoved()){//Idle things
			if(!this.justWarped){
				this.currentTile.interact(this, true);
				this.justWarped = true;
			}
			
			if(this.currentTile instanceof GrassTile && !this.justBattled && !this.justSpawned){
				this.justBattled = true;
				this.w.wildBattle(this.team);
			}
		}else{
			if(!this.currentTile.warpsPlayer()){
				this.justWarped = false;
			}
			
			if(!this.currentTile.spawnsPrintermon()){
				this.justBattled = false;
			}
			
			this.justSpawned = false;
		}
	}

	public void render(Renderer r) {
		byte flip = (byte)0;
		if(this.dir == this.right)flip = r.xFlip;
		if(this.dir == this.up)flip = r.yFlip;
		r.renderSection(data, this.pos.getIX(), this.pos.getIY(), 16, flip);
	}
	
	public Point getPos(){
		return this.pos;
	}

	public void moveUp() {
		move(0, -1);
	}

	public void moveDown() {
		move(0, 1);
	}

	public void moveLeft() {
		move(-1, 0);
	}

	public void moveRight() {
		move(1, 0);
	}
	
	public void moveAgain(){
		int xo = 0;
		int yo = 0;
		if(this.dir == 0){
			yo = -1;
		}else if(this.dir == 1){
			xo = 1;
		}else if(this.dir == 2){
			yo = 1;
		}else if(this.dir == 3){
			xo  = -1;
		}
		this.move(xo, yo);
	}
	
	public void action(){
		if(this.interactTime > 0)return;
		if(!this.canMove){
			this.interact();
			this.interactTime = 10;
		}
	}
	
	public void setTile(){
		if(!this.canMove)return;
		int tx = this.pos.getIX() / 16;
		int ty = this.pos.getIY() / 16;
		this.w.setTile(Tile.colorTile, tx, ty);
	}
	
	private void updateSprite(){
		System.out.println("HUI");
		if(this.dir == up || this.dir == down){
			this.data = sp.getData(0,0);
		}else if(this.dir == left || this.dir == right){
			this.data = sp.getData(1,0);
		}
	}
}
