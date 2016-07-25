package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class DirtTrackTile extends Tile {

	int[] data;

	public DirtTrackTile(int id) {
		super(id);
		canWalkThrough = true;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			this.frameData = sp.getData(1, 0);
		}
		
		draw2By2(r, x, y, frameData);
	}

	@Override
	public void tick(World w, int tx, int ty) {
		
	}

}
