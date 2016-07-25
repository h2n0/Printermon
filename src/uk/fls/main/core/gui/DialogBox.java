package uk.fls.main.core.gui;

import fls.engine.main.input.Input;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;

public class DialogBox {

	
	private String[] lines;
	private int inputDelay;
	private int maxLineIndex;
	
	private byte time;
	private int lineIndex;
	private Point pos;
	private boolean done;
	private SpriteParser gui;
	private SpriteParser font;
	
	
	private final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789z.'abcdefghijklmnopqrstuvwxyz";
	public DialogBox(int x, int y, String...lines){
		this.lines = lines;
		this.pos = new Point(x, y);
		this.lineIndex = 0;
		this.inputDelay = 10;
		this.maxLineIndex = -1;
		this.time = (byte)0;
		
		this.gui = new SpriteParser(8, FileIO.instance.readInternalFile("/gui.art"));
		this.font = new SpriteParser(8, FileIO.instance.readInternalFile("/font.art"));
	}
	
	public void render(Renderer r){
		int w = 18;
		int h = 2;
		int c = 188;
		for(int i = 0; i < (w * 8) * (h * 8); i++){
			int dx = i % (w * 8);
			int dy = i / (w * 8);
			r.setPixel(this.pos.getIX() + dx, this.pos.getIY() + dy, r.makeRGB(c, c, c));
		}
		
		for(int x = -1; x <= w; x++){
			for(int y = -1; y <= h; y++){
				byte flip = (byte)0;
				int dx = 0;
				if(x == -1)flip |= r.xFlip;
				if(y == -1)flip |= r.yFlip;
				if(x == -1 || y == -1 || x == w || y == h)dx = 0;
				if(x >= 0 && x < w && y >= 0 && y < h) continue;
				if((x == -1 || x == w) && y >= 0 && y < h)dx = 2;
				if(x >= 0 && x < w)dx = 1;
				r.renderSection(gui.getData(dx, 0), this.pos.getIX() + (x * 8), this.pos.getIY() + (y * 8), 8, flip);
			}
		}
		


		if(this.lineIndex < this.maxLineIndex-1){
			r.renderSection(this.gui.getData(3, 1), this.pos.getIX() + (w * 8)-8, this.pos.getIY()+(h * 8)-8 + (this.time % 64 > 32?1:0), 8);
		}
		int yo = 0;
		int currentLength = 0;
		for(int i = 0; i < this.lines.length; i++){
			String word = this.lines[i];
			if(currentLength + word.length() >= 17){
				currentLength = 0;
				yo ++;
			}

			for(int j = 0; j < word.length(); j++){
				String currentLetter = word.substring(j,j+1);
				if(currentLetter.equals(" "))continue;
				int pos = letters.indexOf(currentLetter);
				if(pos == -1)continue;
				int dx = pos % 8;
				int dy = pos / 8;
				int tx = this.pos.getIX() + (currentLength*8) + (j * 8);
				int ty = this.pos.getIY() + (yo * 8);
				
				if(yo - this.lineIndex < 2 && yo - this.lineIndex >= 0){
					if(yo - this.lineIndex == 0){//Last line
						r.renderSection(this.font.getData(dx, dy), tx, ty - (this.lineIndex * 8), 8);
					}else{// Current Line
						int ntx = this.pos.getIX() + (currentLength*8) + (j * 8);
						r.renderSection(this.font.getData(dx, dy), ntx, ty - (this.lineIndex * 8), 8);
					}
				}
			}
			currentLength += word.length() + 1;
		}
		if(this.maxLineIndex == -1){
			this.maxLineIndex = yo;
		}
	}
	
	public void update(Input i){
		this.time = (byte)((this.time+1) % 64);
		if(this.inputDelay > 0){
			this.inputDelay--;
			return;
		}
		if(i.isKeyPressed(i.space)){
			this.lineIndex++;
			if(this.lineIndex >= this.maxLineIndex){
				this.done = true;
			}
			this.inputDelay = 10;
		}
	}
	
	public boolean finished(){
		return this.done;
	}
}
