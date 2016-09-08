package uk.fls.main.core.tiles;

import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.core.World;
import uk.fls.main.core.entitys.Entity;

public abstract class Tile {

	
	public static Tile[] tiles = new Tile[255];
	public static Tile voidTile = new VoidTile(0);
	public static Tile grassTile = new GrassTile(1);
	public static Tile dirtTrackTile = new DirtTrackTile(2);
	public static Tile woodFloorTile = new WoodFloorTile(3);
	public static Tile colorTile = new ColorTile(4);
	public static Tile simpleWall = new SimpleWall(5);
	public static Tile wallWithBlinds = new WallWithBlinds(6);
	public static Tile sign = new SignTile(7);
	public static Tile stairsDown = new StairsTile(8, false);
	public static Tile stairsUp = new StairsTile(9, true);
	public static Tile fencePost = new FencePostTile(10);
	public static Tile doorMat = new DoorMatTile(11);
	public static Tile houseDoor = new HouseDoorTile(12);
	public static Tile houseWindow = new HouseTile(13, true);
	public static Tile houseTile = new HouseTile(14, false);
	public static Tile houseCorner = new HouseCornerTile(15);
	public static Tile houseRoof = new HouseRoofTile(16);
	public static Tile pathTile = new PathTile(17);
	
	protected int w = 16;
	protected int h = 16;
	
	
	protected final byte id;
	protected boolean canWalkThrough = false;
	protected boolean spawnsPrintermon = false;
	protected boolean warpsPlayer = false;
	protected boolean givesDialog = false;
	
	protected int[] frameData;
	
	public Tile(int id){
		if(Tile.tiles[id] != null)throw new RuntimeException("Duplicate Tiles: " + id);
		Tile.tiles[id] = this;
		this.id = (byte)id;
	}
	
	public abstract void render(SpriteParser sp, World w, Renderer r, int x, int y);
	
	public abstract void tick(World w, int tx, int ty);
	
	protected void draw2By2(Renderer r, int x ,int y, int[] data){
		r.renderSection(data, x, y, 8);
		r.renderSection(data, x + 8, y, 8);
		r.renderSection(data, x, y + 8, 8);
		r.renderSection(data, x + 8, y + 8, 8);
	}
	
	protected void draw2BackToBack(Renderer r, int x, int y, int[] data1, int[] data2){
		r.renderSection(data2, x, y, 8);
		r.renderSection(data2, x + 8, y, 8, r.xFlip);
		r.renderSection(data1, x, y + 8, 8);
		r.renderSection(data1, x + 8, y + 8, 8, r.xFlip);
	}
	
	public boolean canWalkThrough(){
		return this.canWalkThrough;
	}
	
	public boolean spawnsPrintermon(){
		return this.spawnsPrintermon;
	}
	
	public boolean warpsPlayer(){
		return this.warpsPlayer;
	}
	

	public boolean givesDialog(){
		return this.givesDialog;
	}
	
	public void interact(Entity e, boolean step){
		
	}
	
	public int[] getFrameData(){
		return this.frameData;
	}
	
	protected void travelAtCurrentPos(Entity e){
		int x = e.getPos().getIX()/16;
		int y = e.getPos().getIY()/16;
		byte b = e.w.getData(x, y);
		e.w.travel(b);
	}
	
	
	public byte getId(){
		return this.id;
	}
}
