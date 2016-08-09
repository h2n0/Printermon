package uk.fls.main.core.entitys.printermon;

import fls.engine.main.io.FileIO;

public class StatsManager {

	
	public String name;
	public String desc;
	public Type type;
	public Move[] moves;
	public int[] levels;
	public boolean evolves;
	public int evolveLevel;
	public int evolveId;
	public int id;
	
	public int health;
	public int maxHealth;
	
	private int currentLevel;
	private int currentExp;
	
	public static StatsManager getByEntryNum(int tid){
		
		
		boolean isCorrectEntry = false;
		String id = "";
		int startLine = -1;
		String fileData = FileIO.instance.readInternalFile("/mon/dex.txt");
		
		String[] lines = fileData.split("\n");
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			if(!isCorrectEntry){
				if(line.startsWith("[")){ // This is a new entry
					line = line.substring(1);
					line = line.substring(0, line.indexOf("]"));
					id = line.trim();
					if(Integer.parseInt(id) == tid){
						isCorrectEntry = true;
						startLine = i + 1;
					}
				}
			}else{
				if(line.startsWith("#")){ // End of current entry
					int diff = i - startLine;
					String[] data = new String[diff];
					for(int j = 0; j < diff; j++){
						data[j] = lines[startLine + j];
					}
					StatsManager res = new StatsManager(tid, data);
					return res;
				}
			}
		}
		return null;
	}
	
	public StatsManager(int id, String...data){
		this.id = id;
		this.evolves = false;
		this.evolveId = -1;
		this.evolveLevel = -1;
		this.maxHealth = 10;
		this.health = 10;
		this.currentLevel = 1;
		this.moves = new Move[4];
		
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
				String[] names = value.split(",");
				for(int j = 0; j < 4; j++){
					Move m = Move.getByName(names[j]);
					this.moves[j] = m;
				}
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
	
	public int getHealth(){
		return this.health;
	}
	
	public int getMaxHealth(){
		return this.maxHealth;
	}
	
	public boolean isAlive(){
		return this.health > 0;
	}
	
	private String capitalise(String s){
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	private String getLevel(){
		return ""+this.currentLevel;
	}
}
