package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class FencePostTile extends Tile {

	private int[] bottomHalf;

	public FencePostTile(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		this.frameData = sp.getData(1, 1);
		this.bottomHalf = sp.getData(1, 2);
		this.draw2BackToBack(r, x, y, bottomHalf, frameData);
	}

	@Override
	public void tick(World w, int x, int y) {
		
	}

}
