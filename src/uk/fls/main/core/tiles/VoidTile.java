package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class VoidTile extends Tile {

	public VoidTile(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		for(int xx = 0; xx < this.w; xx++){
			for(int yy = 0; yy < h; yy++){
				r.setPixel(x + xx, y + yy, 0);
			}
		}
	}

	@Override
	public void tick(World w, int tx, int ty) {
		
	}

}
