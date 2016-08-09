package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;

public class HouseRoofTile extends Tile {

	private int[] bottomHalf;
	private int[] otherPart;

	public HouseRoofTile(int id) {
		super(id);
	}

	@Override
	public void render(SpriteParser sp, World w, Renderer r, int x, int y) {

		int tx = x / 16;
		int ty = y / 16;
		boolean tl = w.getTile(tx - 1, ty) instanceof HouseRoofTile;
		boolean tr = w.getTile(tx + 1, ty) instanceof HouseRoofTile;
		boolean tu = w.getTile(tx, ty - 1) instanceof HouseRoofTile;
		boolean td = w.getTile(tx, ty + 1) instanceof HouseRoofTile;

		this.otherPart = sp.getData(5, 4);
		this.frameData = sp.getData(6, 3);
		
		draw2By2(r, x, y, frameData);

		if (tl && tr) {
			this.frameData = sp.getData(5, 3);
			this.bottomHalf = sp.getData(5, 3);

			if (!td)this.bottomHalf = sp.getData(5, 4);
			if (!tu)this.frameData = sp.getData(5, 2);
			r.renderSection(this.frameData, x, y, 8);
			r.renderSection(this.frameData, x + 8, y, 8);
			r.renderSection(this.bottomHalf, x, y + 8, 8);
			r.renderSection(this.bottomHalf, x + 8, y + 8, 8);
		}else if(td && tu){
			this.bottomHalf = sp.getData(6, 3);
			this.frameData = sp.getData(5, 3);
			
			if(tl){
				r.renderSection(frameData, x, y, 8);
				r.renderSection(bottomHalf, x+8, y, 8, r.xFlip);
				r.renderSection(frameData, x, y+8, 8);
				r.renderSection(bottomHalf, x+8, y+8, 8, r.xFlip);
			}else if(tr){
				r.renderSection(frameData, x+8, y+8, 8);
				r.renderSection(bottomHalf, x, y+8, 8);
				r.renderSection(frameData, x+8, y, 8);
				r.renderSection(bottomHalf, x, y, 8);
			}
		} else {
			if(tl){
				this.frameData = sp.getData(6, 3);
				this.bottomHalf = sp.getData(6, 3);
				if(!td)this.bottomHalf = sp.getData(6, 4);
				if(!tu)this.frameData = sp.getData(6, 2);
				r.renderSection(this.frameData, x + 8, y, 8, r.xFlip);
				r.renderSection(this.bottomHalf, x + 8, y + 8, 8, r.xFlip);
				
				if(!tu){
					r.renderSection(sp.getData(5, 2), x, y, 8);
					r.renderSection(sp.getData(5, 3), x, y + 8, 8);
				}else if(!td){
					r.renderSection(sp.getData(5, 3), x, y, 8);
					r.renderSection(sp.getData(5, 4), x, y + 8, 8);
					
				}
			}else if(tr){
				this.frameData = sp.getData(6, 3);
				this.bottomHalf = sp.getData(6, 3);
				if (!td)this.bottomHalf = sp.getData(6, 4);
				if (!tu)this.frameData = sp.getData(6, 2);
				r.renderSection(this.frameData, x, y, 8);
				r.renderSection(this.bottomHalf, x, y + 8, 8);
				
				if(!tu){
					r.renderSection(sp.getData(5, 2), x + 8, y, 8);
					r.renderSection(sp.getData(5, 3), x + 8, y + 8, 8);
				}else if(!td){
					r.renderSection(sp.getData(5, 3), x + 8, y, 8);
					r.renderSection(sp.getData(5, 4), x + 8, y + 8, 8);
					
				}
			}
		}

	}

	@Override
	public void tick(World w, int tx, int ty) {

	}

}
