package uk.fls.main.core.gui;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;

public class Font {

	private static SpriteParser font = new SpriteParser(8, FileIO.instance.readInternalFile("/font/font.art"));
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz0123456789i.";
	
	public static void drawString(Renderer r, String s, int x,int y){
		for(int i = 0; i < s.length(); i++){
			int pos = letters.indexOf(s.charAt(i));
			if(pos == -1)continue;
			int tx = pos % 8;
			int ty = pos / 8;
			r.renderSection(font.getData(tx, ty), x + (i * 8), y, 8);
		}
	}
}
