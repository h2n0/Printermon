package uk.fls.main.core.entitys.printermon;

public class Move {
	
	
	// Status effects
	public static final byte none = 0;
	public static final byte jammed = 1;
	
	// Move declaration
	public static Move[] globalMoves = new Move[255];
	public static Move bash = new Move(0, "Bash", Type.Normal, 10, 25, none);
	
	public static Move getByName(String name){
		name = name.trim();
		name = name.substring(0,1).toUpperCase() + name.substring(1);
		System.out.println(name);
		for(int i = 0; i < globalMoves.length; i++){
			if(globalMoves[i] == null) continue;
			Move m = globalMoves[i];
			if(m.getName().equals(name))return m;
		}
		return null;
	}
	
	
	private final int id;
	private String name;
	private Type type;
	private int dmg;
	private byte special;
	private final int maxUses;
	
	public Move(int id, String name, Type type, int dmg, int max, byte special){
		this.id = id;
		this.name = name;
		this.type = type;
		this.dmg = dmg;
		this.special = special;
		this.maxUses = max;
		globalMoves[id] = this;
		System.out.println(this.name + " added to list of moves!");
	}
	
	public int getDmg(){
		return this.dmg;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
}

