package uk.fls.main.core.extras;

import fls.engine.main.util.Point;

public class Warp {

	
	private String loc;
	private Point pos;
	
	public Warp(String loc, int x, int y){
		this.loc = loc;
		this.pos = new Point(x,y);
	}
	
	public Point getWarpLoc(){
		return this.pos;
	}
	
	public String getFile(){
		return this.loc;
	}
}
