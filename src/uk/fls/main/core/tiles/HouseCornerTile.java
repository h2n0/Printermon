package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class HouseCornerTile extends Tile {

	private int[] bottomHalf;
	private int[] otherPart;
	public HouseCornerTile(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {
		
		int tx = x / 16;
		int ty = y / 16;
		boolean tl = w.getTile(tx - 1, ty) instanceof HouseTile || w.getTile(tx - 1, ty) instanceof HouseDoorTile;
		boolean tr = w.getTile(tx + 1, ty) instanceof HouseTile || w.getTile(tx + 1, ty) instanceof HouseDoorTile;
		boolean td = w.getTile(tx, ty+1) instanceof HouseCornerTile;
		boolean tu = w.getTile(tx, ty-1) instanceof HouseCornerTile;
		
		this.frameData = sp.getData(6, 1);
		this.bottomHalf = sp.getData(6, 0);
		this.otherPart = sp.getData(7, 0);
		
		if(tl && !td){
			r.renderSection(bottomHalf, x+8, y, 8, r.xFlip);
			r.renderSection(otherPart, x, y, 8, r.xFlip);
			r.renderSection(frameData, x+8, y+8, 8, r.xFlip);
			r.renderSection(otherPart, x, y + 8, 8, r.xFlip);
		}else if(tr && !td){
			r.renderSection(bottomHalf, x, y, 8);
			r.renderSection(otherPart, x+8, y, 8);
			r.renderSection(frameData, x, y+8, 8);
			r.renderSection(otherPart, x+8, y+8, 8);
		}
		
		if(tl && td){
			r.renderSection(bottomHalf, x+8, y, 8, r.xFlip);
			r.renderSection(otherPart, x, y, 8, r.xFlip);
			r.renderSection(bottomHalf, x+8, y+8, 8, r.xFlip);
			r.renderSection(otherPart, x, y + 8, 8, r.xFlip);
		}else if(tr && td){
			r.renderSection(bottomHalf, x, y, 8);
			r.renderSection(otherPart, x+8, y, 8);
			r.renderSection(bottomHalf, x, y+8, 8);
			r.renderSection(otherPart, x+8, y+8, 8);
		}
		
	}

	@Override
	public void tick(World w, int tx, int ty) {

	}

}
