package uk.fls.main.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.engine.main.util.Camera;
import fls.engine.main.util.Renderer;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.printermon.Printermon;

public class GameScreen extends Screen {

	private boolean loaded = false;
	private Renderer r;
	private World w;
	private Camera cam;
	
	public void postInit(){
		if(!loaded){// Used to prevent reloading of certain aspects when switching from another screen to this screen
			this.r = new Renderer(this.game.getImage());
			this.cam = new Camera(160,144);
			this.w = new World();
			this.cam.ww = 408 * 16;
			this.cam.wh = 400 * 16;
			this.w.p.w = this.w;
			this.loaded = true;
			Printermon.loadPrintermon();
		}
	}
	
	@Override
	public void update() {
		this.w.update(cam, this.input);
		
		
		boolean up = this.input.isKeyHeld(this.input.w) || this.input.isKeyHeld(this.input.up);
		boolean down = this.input.isKeyHeld(this.input.s) || this.input.isKeyHeld(this.input.down);
		boolean left = this.input.isKeyHeld(this.input.a) || this.input.isKeyHeld(this.input.left);
		boolean right = this.input.isKeyHeld(this.input.d) || this.input.isKeyHeld(this.input.right);
		boolean action = this.input.isKeyHeld(this.input.space) || this.input.isKeyHeld(this.input.z);

		
		if(!this.w.inBattle()){
			if(up){
				this.w.p.moveUp();
			}else if(down){
				this.w.p.moveDown();
			}else if(left){
				this.w.p.moveLeft();
			}else if(right){
				this.w.p.moveRight();
			}else if(action){
				this.w.p.action();
			}
		this.cam.center(this.w.p.getPos().getIX(), this.w.p.getPos().getIY(), 8, false);
		}
	}

	@Override
	public void render(Graphics g) {
	//	this.r.fill(0);
		this.w.render(this.cam, this.r);
		this.r.finaliseRender();
	}

}
