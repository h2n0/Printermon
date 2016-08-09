package uk.fls.main.core.extras;

import fls.engine.main.util.Renderer;

public class Transition {

	
	private boolean finished;
	private int cw;
	private int del;
	private int limit;
	
	private final boolean hoz;
	public Transition(){
		this.finished = false;
		this.del = 0;
		this.limit = 999999;
		this.hoz = Math.random() > 0.5;
	}
	
	public void render(Renderer r){
		
		if(hoz){			
			int ext = 20;
			for(int i = 0; i < cw; i++){
				for(int j = 0; j < r.getHeight(); j++){
					r.setPixel(i, j, 0);
				}
			}
			for(int i = cw; i < cw + ext; i++){
				int diff = i - cw;
				int c = (255/ext) * (ext-diff);
				for(int j = 0; j < r.getHeight(); j++){
					r.setPixel(i, j, r.makeRGB(c, c, c));
				}
			}
		}else{
			for(int i = 0; i < r.getWidth(); i++){
				for(int j = 0; j < cw; j++){
					r.setPixel(i, j, 0);
				}
			}
		}
		
		if(this.limit == 999999){
			this.limit = r.getWidth();
		}
	}
	
	public void update(){
		if(this.del > 0){
			this.del--;
			return;
		}
		
		this.cw += 12;
		this.del = 1;
		if(this.cw > this.limit){
			this.finished = true;
		}
	}
	
	public boolean finished(){
		return this.finished;
	}
}
