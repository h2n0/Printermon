package uk.fls.main.core.entitys.printermon;

import java.util.HashMap;

import javax.print.attribute.standard.RequestingUserName;

import fls.engine.main.io.FileIO;

public class Printermon {

	public static Printermon[] globalPrintermon;
	
	private static HashMap<Integer, StatsManager> dexEntrys;
	
	private static int maxMon;
	
	public static void loadPrintermon(){
		int amt = 0;
		
		dexEntrys = new HashMap<Integer, StatsManager>();
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
					dexEntrys.put(amt, StatsManager.getByEntryNum(Integer.parseInt(id)));
					startLine = -1;
					amt++;
					inEntry = false;
				}
			}
		}
		
		maxMon = amt;
		System.out.println("Loaded: " + amt + " printermon");
	}
	
	public static Printermon getPrintermonByID(int id){
		//if(id < 0 || id >= maxMon)return getPrintermonByID(0);
		return new Printermon(StatsManager.getByEntryNum(id));
	}
	
	public StatsManager stats;
	
	public Printermon(StatsManager s){
		this.stats = s;
	}
	
	public String getName(){
		return this.stats.name;
	}
	
	public float getHealthPerc(){
		float ch = (float)this.stats.health;
		float mh = (float)this.stats.maxHealth;
		return ch/mh;
	}
	
	public boolean damage(int amt){
		this.stats.health -= amt;
		if(stats.health < 0)this.stats.health = 0;
		return this.stats.isAlive();
	}
	
	public void heal(int amt){
		this.stats.health += amt;
		if(this.stats.health > this.stats.maxHealth)this.stats.health = this.stats.maxHealth;
	}
	
	public Move getRandomMove(){
		Move res = this.stats.moves[0];
		for(int i = 0; i < 4; i++){
			Move nm = this.stats.moves[i];
			if(nm == null)continue;
			if(Math.random() > 0.4)return nm;
		}
		return res;
	}
	
	public int calculateDmg(Move om, StatsManager opp){
		/**
		float levelPart = (2 * this.stats.currentLevel() + 10) / 250;
		float dod = 4;//this.stats.strength / opp.defence; 
		
		int diff = om.getType().getDiffernece(om.getType(), opp.type);
		float mod = (om.getType()==stats.type?1.5f:1f) * om.getDmgMult(diff);
		
		float org = (levelPart * dod * om.getDmg() + 2) * genRandom(mod);
		return (int)org;**/
		
		float levelPart = (2 * this.stats.currentLevel() / 5 + 2);
		float dod = (10 * om.getDmg() / 10);
		float STAB = (om.getType()==stats.type?1.5f:1f);
		
		
		float dmg = (((levelPart * dod) / 50) + 2) * STAB * om.getDmgMult(om.getType().getDiffernece(om.getType(), opp.type)) * genRandom(100);
		return (int)(dmg/2.5f);
	}
	
	private float genRandom(float max){
		float min = (max/100) * 85;
		float rand = (float)(min + (Math.random() * (max-min)))/max;
		return rand;
	}
}
