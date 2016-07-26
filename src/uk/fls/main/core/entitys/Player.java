package uk.fls.main.core.entitys;

import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import uk.fls.main.core.entitys.printermon.Printermon;
import uk.fls.main.core.tiles.GrassTile;
import uk.fls.main.core.tiles.Tile;

public class Player extends Entity{

	private boolean canMove;
	private int interactTime;
	private boolean justWarped;
	
	
	public Player(int x, int y) {
		super(x,y);
		this.interactTime = 0;
	}

	public void update() {
		if(!hasMoved()){//Idle things
			if(!this.justWarped){
				this.currentTile.interact(this, true);
				this.justWarped = true;
			}
			
			if(this.currentTile instanceof GrassTile){
				this.w.battle(new Printermon[]{Printermon.getPrintermonByID(0)}, Printermon.getPrintermonByID(0));
			}
		}else{
			if(!this.currentTile.warpsPlayer()){
				this.justWarped = false;
			}
		}
	}

	public void render(Renderer r) {
		for (int x = 0; x < 16; x++) {
			int height = 16;
			for (int y = 0; y < height; y++) {
				r.setPixel(pos.getIX() + x, pos.getIY() + y, 180);
			}
		}
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
			System.out.println("HUI");
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
}
