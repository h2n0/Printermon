package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.Entity;

public class SignTile extends Tile {

	private int[] bottomHalf;
	private byte d;

	public SignTile(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if (this.frameData == null) {
			this.frameData = sp.getData(5, 0);
			this.bottomHalf = sp.getData(5, 1);
		}

		r.renderSection(frameData, x, y, 8);
		r.renderSection(bottomHalf, x, y + 8, 8);
		r.renderSection(frameData, x + 8, y, 8, r.xFlip);
		r.renderSection(bottomHalf, x + 8, y + 8, 8, r.xFlip);
	}

	@Override
	public void tick(World w, int tx, int ty) {
		this.d = w.getData(tx, ty);
	}

	public void interact(Entity e, boolean step) {
		if(step)return;
		System.out.println(d);
		e.w.displayPopup(d);
	}

}
