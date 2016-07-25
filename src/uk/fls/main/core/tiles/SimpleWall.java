package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class SimpleWall extends Tile {

	public SimpleWall(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			this.frameData = sp.getData(3, 0);
		}
		draw2By2(r, x, y, frameData);
	}

	@Override
	public void tick(World w, int tx, int ty) {
		
	}

}
