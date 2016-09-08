package uk.fls.main.core.gui;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;

public class Font {

	private static SpriteParser font = new SpriteParser(8, FileIO.instance.readInternalFile("/font/font.art"));
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz0123456789i.";
	
	public static void drawString(Renderer r, String s, int x,int y){
		drawString(r, s, x, y, r.makeRGB(188, 188, 188));
	}
	
	public static void drawString(Renderer r, String s, int x, int y, int bColor){
		int defaultBacking = r.makeRGB(188, 188, 188);
		for(int i = 0; i < s.length(); i++){
			int pos = letters.indexOf(s.charAt(i));
			if(pos == -1)continue;
			int tx = pos % 8;
			int ty = pos / 8;
			int[] data = font.getData(pos);
			//r.renderSection(font.getData(tx, ty), x + (i * 8), y, 8);
			for(int j = 0; j < data.length; j++){
				int cx = j % 8;
				int cy = j / 8;
				int c = data[j];
				if(c == defaultBacking)c = bColor;
				r.setPixel(x + (i * 8) + cx, y + cy, c);
			}
		}
	}

	public static void drawStringWrap(Renderer r, String res, int x, int y, int numLetters) {
		drawStringWrap(r, res, x, y, r.makeRGB(188, 188, 188), numLetters);
	}
	
	public static void drawStringWrap(Renderer r, String res, int x, int y,int bColor, int numLetters) {
		if(res.isEmpty())return;
		String[] letters = res.split(" ");
		int currentLength = 0;
		int yo = 0;
		for(int i = 0; i < letters.length; i++){
			String let = letters[i];
			if(currentLength + let.length() >= numLetters){
				currentLength = 0;
				yo ++;
			}
			
			if(yo > 15)return;
			drawString(r, letters[i], x + currentLength * 8, y + yo * 8);
			currentLength += let.length() + 1;
		}
	}
}
