package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class HouseTile extends Tile {

	private int[] bottomHalf;
	private boolean dec;

	public HouseTile(int id, boolean dec) {
		super(id);
		this.dec = dec;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if (this.frameData == null || this.frameData != null) {
			this.frameData = sp.getData(7, 0);
			if (this.dec) {
				this.bottomHalf = sp.getData(7, 1);
			} else {
				this.bottomHalf = this.frameData;
			}
		}
		
		draw2BackToBack(r, x, y, frameData, bottomHalf);
	}

	@Override
	public void tick(World w, int tx, int ty) {

	}

}
