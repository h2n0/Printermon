package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class PathTile extends Tile {

	public PathTile(int id) {
		super(id);
		this.canWalkThrough = true;
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		if(this.frameData == null){
			this.frameData = sp.getData(7, 4);
		}
		
		r.renderSection(frameData, x, y, 8);
		r.renderSection(frameData, x + 8, y, 8, r.xFlip);
		r.renderSection(frameData, x, y + 8, 8);
		r.renderSection(frameData, x + 8, y + 8, 8, r.xFlip);
	}

	@Override
	public void tick(World w, int tx, int ty) {

	}

}
