package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class WallWithBlinds extends Tile {

	private int[] bottomHalf;
	public WallWithBlinds(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			this.frameData = sp.getData(4, 0);
			this.bottomHalf = sp.getData(4, 1);
		}
		
		r.renderSection(frameData, x, y, 8);
		r.renderSection(bottomHalf, x, y + 8, 8);
		r.renderSection(frameData, x + 8, y, 8, r.xFlip);
		r.renderSection(bottomHalf, x+8, y + 8, 8, r.xFlip);
	}

	@Override
	public void tick(World w, int tx, int ty) {
		
	}
	

}
