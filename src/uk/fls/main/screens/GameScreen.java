package uk.fls.main.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.engine.main.util.Camera;
import fls.engine.main.util.Renderer;
import uk.fls.main.core.World;

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
		}
	}
	
	@Override
	public void update() {
		this.w.update(cam, this.input);
		
		if(this.input.isKeyHeld(this.input.w)){
			this.w.p.moveUp();
		}else if(this.input.isKeyHeld(this.input.s)){
			this.w.p.moveDown();
		}else if(this.input.isKeyHeld(this.input.a)){
			this.w.p.moveLeft();
		}else if(this.input.isKeyHeld(this.input.d)){
			this.w.p.moveRight();
		}else if(this.input.isKeyPressed(this.input.space)){
			this.w.p.action();
		}
		
		this.cam.center(this.w.p.getPos().getIX(), this.w.p.getPos().getIY(), 8, false);
	}

	@Override
	public void render(Graphics g) {
	//	this.r.fill(0);
		this.w.render(this.cam, this.r);
		this.r.finaliseRender();
	}

}
