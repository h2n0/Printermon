package uk.fls.main.core;

import fls.engine.main.input.Input;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.entitys.printermon.Printermon;

public class Battle {

	private Printermon playerCurrent;
	private Printermon opponentCurrent;

	private SpriteParser spp;
	private SpriteParser spo;
	private SpriteParser gui;
	private SpriteParser gui2;
	private SpriteParser font;

	private boolean win;
	private boolean lose;

	private boolean fight;
	private boolean run;
	private boolean mons;
	private boolean items;

	private String[] options;
	private int currentOption;
	
	private int currentFightOption;

	private int del;

	private final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz0123456789i./";

	public Battle(Printermon[] player, Printermon... opp) {
		this.del = 30;
		this.playerCurrent = player[getFirstAlive(player)];
		this.opponentCurrent = opp[getFirstAlive(opp)];

		this.spp = new SpriteParser(8,
				FileIO.instance.readInternalFile("/mon/monsters/" + this.playerCurrent.getName() + "-back.art"));
		this.spo = new SpriteParser(8,
				FileIO.instance.readInternalFile("/mon/monsters/" + this.opponentCurrent.getName() + "-front.art"));
		this.gui = new SpriteParser(8, FileIO.instance.readInternalFile("/mon/battlegui.art"));
		this.gui2 = new SpriteParser(8, FileIO.instance.readInternalFile("/gui.art"));
		this.font = new SpriteParser(8, FileIO.instance.readInternalFile("/font/font.art"));

		this.currentOption = 0;
		setOptions("Fight", "Items", "Monst", "Run");
	}

	public void render(Renderer r, int x, int y) {
		r.fill(r.makeRGB(188, 188, 188));
		r.renderSpriteParserSheet(spo, 96, 10);

		// Opponent side rendering
		String opponentHealthString = "HP " + this.opponentCurrent.stats.getHealth() + "/"
				+ this.opponentCurrent.stats.getMaxHealth();
		int oppoenentHealthLength = opponentHealthString.length();
		int xo = 1;
		renderString(r, opponentHealthString, (xo + 1) * 8, 16);
		r.renderSection(gui.getData(0, 0), xo * 8, 16, 8);
		for (int i = 0; i < oppoenentHealthLength; i++) {
			r.renderSection(gui.getData(1, 0), ((xo + 1) * 8) + i * 8, 16, 8);
		}
		r.renderSection(gui.getData(2, 0), (xo + oppoenentHealthLength + 1) * 8, 16, 8);

		int healthBarLength = 10 * 8 - 4;
		float healthPerc = this.opponentCurrent.getHealthPerc();
		int barColor = r.makeRGB((int) ((1 - healthPerc) * 255), (int) (255 * healthPerc), 0);
		int barLength = 10;

		for (int i = 0; i < (int) (healthBarLength * healthPerc); i++) {
			for (int j = 0; j < 4; j++) {
				r.setPixel(9 + xo + i, 16 + j + 10, barColor);
			}
		}

		for (int i = 0; i < barLength; i++) {
			int dx = 4;
			if (i == 0 || i == barLength - 1)
				dx--;
			r.renderSection(gui.getData(dx, 0), 8 + (i * 8), 16 + 8, 8, i == barLength - 1 ? r.xFlip : (byte) 0);
		}
		/////

		// Player side rendering
		r.renderSpriteParserSheet(spp, 16, 144 - 72 - 8);
		String playerCurrentHelath = "HP " + this.playerCurrent.stats.getHealth() + "/"
				+ this.playerCurrent.stats.getMaxHealth();
		int length = playerCurrentHelath.length();

		xo = 9;

		int yo = 10;
		renderString(r, playerCurrentHelath, (xo + 1) * 8, yo * 8);
		r.renderSection(gui.getData(0, 0), (xo + length + 1) * 8, yo * 8, 8, r.xFlip);
		for (int i = 0; i < length; i++) {
			r.renderSection(gui.getData(1, 0), ((xo + 1) * 8) + i * 8, yo * 8, 8);
		}
		r.renderSection(gui.getData(2, 0), xo * 8, yo * 8, 8, r.xFlip);

		healthPerc = this.playerCurrent.getHealthPerc();
		barColor = r.makeRGB((int) ((1 - healthPerc) * 255), (int) (255 * healthPerc), 0);
		for (int i = 0; i < (int) (healthBarLength * healthPerc); i++) {
			for (int j = 0; j < 4; j++) {
				r.setPixel(2 + (xo * 8) + i, 8 + j + (yo * 8) + 2, barColor);
			}
		}

		for (int i = 0; i < barLength; i++) {
			int dx = 4;
			if (i == 0 || i == barLength - 1)
				dx--;
			r.renderSection(gui.getData(dx, 0), (xo * 8) + (i * 8), 8 + (yo * 8), 8,
					i == barLength - 1 ? r.xFlip : (byte) 0);
		}
		/////

		int dxo = 8;
		int dyo = 144 - 32;
		int w = 18;
		int h = 3;
		drawBox(r, dxo, dyo, w, h);
		if (fight) {// Show moves
			drawBox(r, 11 * 8, 13 * 8, 8, 4);
			System.out.println(this.playerCurrent.stats.moves.length);
			for(int i = 0; i < this.playerCurrent.stats.moves.length; i++){
				String cMove = "";
				if(this.playerCurrent.stats.moves[i] == null)cMove = "////";
				else cMove = this.playerCurrent.stats.moves[i].getName();
				renderString(r, cMove, 12 * 8, 13 * 8);
				if(i == this.currentOption){
					r.renderSection(gui2.getData(3, 0), 11 * 8, 13 * 8 + (i * 8), 8);
				}
			}
		} else if (items) {// Show items

		} else if (mons) {// Show mons

		} else {// Show options
			for (int i = 0; i < this.options.length; i++) {
				String c = this.options[i];
				int dx = i % 2;
				int dy = i / 2;
				int tx = 16 + (dx * 9 * 9 + 8) - (dx == 1 ? 8 : 0);
				int ty = 144 - 32 + (dy * 8 + 4);
				renderString(r, c, tx, ty);
				if (i == this.currentOption) {
					r.renderSection(gui2.getData(3, 0), tx - 8, ty, 8);
				}
			}
		}

	}

	private void renderString(Renderer r, String s, int x, int y) {
		for (int i = 0; i < s.length(); i++) {
			int index = s.charAt(i);
			int pos = this.letters.indexOf(index);
			if (pos == -1)
				continue;
			int dx = pos % 8;
			int dy = pos / 8;
			r.renderSection(font.getData(dx, dy), x + i * 8, y, 8);
		}
	}

	public void update(Input i) {
		boolean up = i.isKeyPressed(i.w) || i.isKeyPressed(i.up);
		boolean down = i.isKeyPressed(i.s) || i.isKeyPressed(i.down);
		boolean left = i.isKeyPressed(i.a) || i.isKeyPressed(i.left);
		boolean right = i.isKeyPressed(i.d) || i.isKeyPressed(i.right);

		boolean select = i.isKeyPressed(i.space) || i.isKeyPressed(i.z);
		boolean back = i.isKeyPressed(i.shift) || i.isKeyPressed(i.x);

		boolean baseMenu = fight || items || mons;
		int amt = 0;
		if (up)
			amt -= !baseMenu?2:1;
		else if (down)
			amt += !baseMenu?2:1;
		else if (left && !baseMenu)
			amt--;
		else if (right && !baseMenu)
			amt++;
		
		
		while(amt != 0){
			int off = amt>0?1:amt<0?-1:0;
			this.currentOption += off;
			if(this.currentOption > 3)this.currentOption = 0;
			if(this.currentOption < 0)this.currentOption = 3;
			amt += -off;
		}

		if(select){
			if (fight) {// Show moves
				
			} else if (items) {// Show items
	
			} else if (mons) {// Show mons
	
			} else {// Show options
				if(currentOption == 3){
					this.run = true;
				}else if(currentOption == 0){
					this.fight = true;
				}
			}
		}else if(back){
			if (fight) {// Show moves
				this.fight = false;
			} else if (items) {// Show items
	
			} else if (mons) {// Show mons
	
			}
		}
	}

	public boolean isWin() {
		return this.win;
	}

	public boolean isLose() {
		return this.lose;
	}

	public boolean run() {
		return this.run;
	}

	private int getFirstAlive(Printermon[] mon) {
		for (int i = 0; i < mon.length; i++) {
			if (mon[i].stats.isAlive())
				return i;
		}
		return 0;
	}

	private void drawBox(Renderer r, int x, int y, int w, int h) {
		int voidCol = r.makeRGB(184, 184, 184);
		for (int xx = -1; xx <= w; xx++) {
			for (int yy = -1; yy <= h; yy++) {
				byte flip = (byte) 0;
				int dx = 0;
				if (xx == -1)
					flip |= r.xFlip;
				if (yy == -1)
					flip |= r.yFlip;
				if (xx == -1 || yy == -1 || xx == w || yy == h)
					dx = 0;
				if ((xx >= 0 && yy >= 0) && (xx < w && yy < h)){
					for(int i = 0; i < 8 * 8; i++){
						int cx = i % 8;
						int cy = i / 8;
						r.setPixel(x + (xx * 8) + cx, y + (yy * 8) + cy, voidCol);
					}
					continue;	
				}
				if ((xx == -1 || xx == w) && yy >= 0 && yy < h)
					dx = 2;
				if (xx >= 0 && xx < w)
					dx = 1;
				r.renderSection(gui2.getData(dx, 0), x + (xx * 8), y + (yy * 8), 8, flip);
			}
		}
	}

	private void setOptions(String o1, String o2, String o3, String o4) {
		this.options = new String[4];
		this.options[0] = o1;
		this.options[1] = o2;
		this.options[2] = o3;
		this.options[3] = o4;
	}
}
