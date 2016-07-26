package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.Entity;

public class DoorMatTile extends Tile {

	
	private int[] bottomHalf;
	public DoorMatTile(int i) {
		super(i);
		this.canWalkThrough = true;
		this.warpsPlayer = true;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		int tx = x / 16;
		int ty = y / 16;
		boolean tu = w.getTile(tx, ty-1) == voidTile;
		boolean td = w.getTile(tx, ty+1) == voidTile;
		boolean tl = w.getTile(tx-1, ty) == voidTile;
		boolean tr = w.getTile(tx+1, ty) == voidTile;
		
		
		if(this.frameData == null || this.frameData != null){
			this.frameData = sp.getData(3, ((tl||tr)?2:1));
			this.bottomHalf = sp.getData(2,0);
		}
		
		if(tu){
			r.renderSection(this.frameData, x, y, 8);
			r.renderSection(this.frameData, x + 8, y, 8);
			r.renderSection(this.bottomHalf, x + 8, y+8, 8);
			r.renderSection(this.bottomHalf, x, y+8, 8);
		}else if(td){
			r.renderSection(this.bottomHalf, x, y, 8);
			r.renderSection(this.bottomHalf, x + 8, y, 8);
			r.renderSection(this.frameData, x + 8, y+8, 8);
			r.renderSection(this.frameData, x, y+8, 8);
		}else if(tr){
			r.renderSection(this.bottomHalf, x, y, 8);
			r.renderSection(this.frameData, x + 8, y, 8);
			r.renderSection(this.frameData, x + 8, y+8, 8);
			r.renderSection(this.bottomHalf, x, y+8, 8);
		}else if(tl){
			r.renderSection(this.frameData, x, y, 8);
			r.renderSection(this.bottomHalf, x + 8, y, 8);
			r.renderSection(this.bottomHalf, x + 8, y+8, 8);
			r.renderSection(this.frameData, x, y+8, 8);
		}
	}

	@Override
	public void tick(World w, int tx, int ty) {
		
	}
	
	public void interact(Entity e, boolean step){
		if(step){
			e.w.travel(e.w.getData(e.getPos().getIX()/16, e.getPos().getIY()/16));
		}
	}

}
