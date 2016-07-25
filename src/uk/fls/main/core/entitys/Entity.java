package uk.fls.main.core.entitys;

import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import uk.fls.main.core.World;
import uk.fls.main.core.tiles.Tile;

public abstract class Entity {

	
	protected Point pos;
	private Point targetPos;
	private boolean canMove;
	protected Tile currentTile;
	
	public World w;
	protected byte dir;
	protected byte pdir;
	
	private final int up = 0;
	private final int down = 2;
	private final int left = 1;
	private final int right = 3;
	
	public Entity(int x, int y){
		this.pos = new Point(x,y);
		this.targetPos = this.pos;
		this.canMove = true;
	}
	
	public abstract void render(Renderer r);
	public abstract void update();
	
	protected void move(int dx, int dy) {
		if(!this.canMove) return;
		
		int nx = (this.pos.getIX()/16) + dx;
		int ny = (this.pos.getIY()/16) + dy;
		if(!this.w.getTile(nx, ny).canWalkThrough()) return;
		
		float ntx = nx;
		float nty = ny;
		
		if(ntx < 0)ntx = 0;
		if(nty < 0)nty = 0;
		
		if(dx < 0){
			this.dir = right;
		}else if(dx > 0){
			this.dir = left;
		}
		
		if(dy < 0){
			this.dir = up;
		}else if(dy > 0){
			this.dir = down;
		}
		
		
		canMove = false;
		this.targetPos = new Point(ntx, nty);
		this.pdir = this.dir;
	}
	
	protected boolean hasMoved(){
		boolean moving = !this.canMove;
		if(moving){
			float cx = this.pos.x / 16;
			float cy = this.pos.y / 16;
			
			float speed = (7f/60f)/2f;
			
			if(Math.abs(cx - this.targetPos.x) > 0.1){
				if(cx < this.targetPos.x){
					cx += speed;
				}
				
				if(cx > this.targetPos.x){
					cx -= speed;
				}
				
				this.pos.x = cx * 16;
			}else{
				this.pos.x = this.targetPos.x * 16;
			}
			
			if(Math.abs(cy - this.targetPos.y) > 0.1){
				if(cy < this.targetPos.y){
					cy += speed;
				}
				
				if(cy > this.targetPos.y){
					cy -= speed;
				}
				
				this.pos.y = cy * 16;
			}else{
				this.pos.y = this.targetPos.y * 16;
			}
			
			if(this.pos.x == this.targetPos.x * 16 && this.pos.y == this.targetPos.y * 16){
				this.currentTile = this.w.getTile(this.pos.getIX() / 16, this.pos.getIY() / 16);
				this.canMove = true;
			}
			return true;
		}else{
			this.currentTile = this.w.getTile(this.pos.getIX() / 16, this.pos.getIY() / 16);
			return false;
		}
	}
	
	protected void interact(){
		int xo = 0;
		int yo = 0;
		if(dir == up){
			yo = -1;
		}else if(dir == left){
			xo = 1;
		}else if(dir == down){
			yo = 1;
		}else if(dir == right){
			xo = -1;
		}
		
		
		int px = (int)(Math.ceil(this.pos.x));
		int py = (int)(Math.ceil(this.pos.y));
		int tx = (px/16) + xo;
		int ty = (py/16) + yo;
		System.out.println(tx + " : " + ty);
		System.out.println(this.w.getTile(tx, ty));
		this.w.getTile(tx, ty).interact(this, false);
	}
	
	public Point getPos(){
		return this.pos;
	}
	
	
}
