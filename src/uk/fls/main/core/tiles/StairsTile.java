package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.Entity;

public class StairsTile extends Tile {

	
	private boolean up;
	private int id;
	
	private int[] topRight;
	private int[] bottomLeft;
	private int[] bottomRight;
	public StairsTile(int id, boolean up) {
		super(id);
		this.up = up;
		this.canWalkThrough = true;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			int tx = up?4:6;
			this.frameData = sp.getData(tx, 6);
			this.topRight = sp.getData(tx+1,6);
			this.bottomLeft = sp.getData(tx, 7);
			this.bottomRight = sp.getData(tx+1, 7);
		}
		r.renderSection(this.frameData, x, y, 8);
		r.renderSection(topRight, x + 8, y, 8);
		r.renderSection(bottomLeft, x, y + 8, 8);
		r.renderSection(bottomRight, x + 8, y + 8, 8);
	}

	@Override
	public void tick(World w, int tx, int ty) {
		this.id = w.getData(tx, ty);
	}
	
	public void interact(Entity e, boolean step){
		if(step){
			e.w.travel(id);
		}
	}

}
