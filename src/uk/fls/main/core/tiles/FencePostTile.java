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
		byte d = w.getData(x / 16, y / 16);
		if (this.frameData == null || this.frameData != null) {
			if (d == 1) {
				this.frameData = sp.getData(2, 1);
				this.bottomHalf = sp.getData(2, 2);
			} else {
				this.frameData = sp.getData(1, 1);
				this.bottomHalf = sp.getData(1, 2);
			}
		}

		if (d < 2) {
			r.renderSection(frameData, x, y, 8);
			r.renderSection(bottomHalf, x, y + 8, 8);
			r.renderSection(frameData, x + 8, y, 8, r.xFlip);
			r.renderSection(bottomHalf, x + 8, y + 8, 8, r.xFlip);
		} else {
			r.renderSection(frameData, x, y, 8);
			r.renderSection(bottomHalf, x, y + 8, 8);
			r.renderSection(frameData, x + 8, y, 8, r.xFlip);
			r.renderSection(bottomHalf, x + 8, y + 8, 8, r.xFlip);
		}
	}

	@Override
	public void tick(World w, int x, int y) {
		boolean l = w.getTile(x - 1, y) == Tile.fencePost;
		boolean r = w.getTile(x + 1, y) == Tile.fencePost;
		if (l && r) {
			w.setData(x, y, 1);
		}
	}

}
