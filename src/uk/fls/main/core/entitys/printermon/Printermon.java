package uk.fls.main.core.entitys.printermon;

import fls.engine.main.io.FileIO;

public class Printermon {

	public static Printermon[] globalPrintermon;
	
	public static void loadPrintermon(){
		int amt = 0;
		String fileData = FileIO.instance.readInternalFile("/mon/dex.txt");
		String[] lines = fileData.split("\n");
		for(String line : lines){
			if(line.startsWith("#")){
				amt++;
			}
		}
		globalPrintermon = new Printermon[amt];
		amt = 0;
		
		boolean inEntry = false;
		String id = "";
		int startLine = -1;
		
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			if(!inEntry){
				if(line.startsWith("[")){ // This is a new entry
					inEntry = true;
					line = line.substring(1);
					line = line.substring(0, line.indexOf("]"));
					id = line.trim();
					startLine = i + 1;
				}
			}else{
				if(line.startsWith("#")){ // End of current entry
					int diff = i - startLine;
					String[] data = new String[diff];
					for(int j = 0; j < diff; j++){
						data[j] = lines[startLine + j];
					}
					startLine = -1;
					globalPrintermon[amt] = new Printermon(data);
					globalPrintermon[amt].setId(amt);
					amt++;
					inEntry = false;
				}
			}
		}
		
		System.out.println("Loaded: " + amt + " printermon");
	}
	
	public static Printermon getPrintermonByID(int id){
		if(id < 0 || id >= globalPrintermon.length)return null;
		return globalPrintermon[id];
	}
	
	
	private String name;
	private String desc;
	private Type type;
	private String moves;
	private int[] levels;
	private boolean evolves;
	private int evolveLevel;
	private int evolveId;
	private int id;
	
	private int health;
	private int maxHealth;
	
	public Printermon(String... data){
		this.evolves = false;
		this.evolveId = -1;
		this.evolveLevel = -1;
		for(int i = 0; i < data.length; i++){
			String line = data[i];
			String key = line.substring(0, line.indexOf(":")).trim();
			String value = line.substring(line.indexOf(":") + 1).trim();
			if(key.equals("Name")){ // The name of the printermon.
				this.name = capitalise(value);
			}else if(key.equals("Desc")){ // The description of the printermon.
				this.desc = capitalise(value);
			}else if(key.equals("Type")){ // The type of the printermon.
				this.type = Type.getTypeById(Integer.parseInt(value));
			}else if(key.equals("Move")){ // The moves that this printermon can learn.
				this.moves = value;
			}else if(key.equals("Lvls")){ // The levels that the printermon will learn a new move.
				String[] nums = value.split(",");
				this.levels = new int[nums.length];
				for(int j = 0; j < nums.length; j++){
					this.levels[j] = Integer.parseInt(nums[j]);
				}
			}else if(key.equals("Evol")){ // The level at which the printermon evolves.
				this.evolveLevel = Integer.parseInt(value);
			}else if(key.equals("Emon")){ // The id of the printermon that it evolves into.
				this.evolveId = Integer.parseInt(value);
			}
		}
		if(this.evolveLevel != -1 || this.evolveId != -1)this.evolves = true;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public int getMaxHealth(){
		return this.maxHealth;
	}
	
	public boolean isAlive(){
		return this.health > 0;
	}
	
	public String getName(){
		return this.name;
	}
	
	public float getHealthPerc(){
		float ch = (float)this.health;
		float mh = (float)this.maxHealth;
		return ch/mh;
	}
	
	
	private String capitalise(String s){
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public void printValues(){
		System.out.println(this.name);
		System.out.println(this.desc);
		System.out.println(this.type);
		System.out.println(this.moves);
		System.out.println(this.levels);
		System.out.println(this.evolveLevel);
		System.out.println(this.evolveId);
	}
}
