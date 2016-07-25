package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.Entity;

public class HouseDoorTile extends Tile {

	
	private int[] bottomHalf;
	public HouseDoorTile(int id) {
		super(id);
		this.canWalkThrough = true;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			this.frameData = sp.getData(7, 2);
			this.bottomHalf = sp.getData(7, 3);
		}
		
		r.renderSection(frameData, x, y, 8);
		r.renderSection(bottomHalf, x, y + 8, 8);
		r.renderSection(frameData, x + 8, y, 8, r.xFlip);
		r.renderSection(bottomHalf, x + 8, y + 8, 8, r.xFlip);
	}

	@Override
	public void tick(World w, int tx, int ty) {

	}
	
	public void interact(Entity e, boolean step){
		if(step){
			travelAtCurrentPos(e);
		}
	}

}
