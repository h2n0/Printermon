package uk.fls.main.core.entitys.printermon;

public class Move {
	
	
	// Status effects
	public static final byte none = 0;
	public static final byte jammed = 1;
	
	// Move declaration
	public static Move[] globalMoves = new Move[255];
	public static Move bash = new Move(0, "Bash", Type.Normal, 10, none);
	
	public Move(int id, String name, Type type, int dmg, byte special){
		
	}
}
