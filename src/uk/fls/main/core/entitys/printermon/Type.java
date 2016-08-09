package uk.fls.main.core.entitys.printermon;

public class Type {

	private enum TypeName {
		Ink, Laser, Paper, Reinforced
	}

	public static Type Ink = new Type(TypeName.Ink); // Basic type equivalent to
														// Normal
	public static Type Laser = new Type(TypeName.Laser);
	public static Type Paper = new Type(TypeName.Paper);
	public static Type Reinforced = new Type(TypeName.Reinforced);

	public static Type getTypeById(int id) {
		if (id == 0)
			return Ink;
		else if (id == 1)
			return Laser;
		else if (id == 2)
			return Paper;
		else if (id == 3)
			return Reinforced;
		else
			return Ink;
	}

	private TypeName type;

	public Type(TypeName t) {
		this.type = t;
	}

	/**
	 * Returns an int to represent the correct multiplier that should be applied.
	 * 
	 * @param moveType
	 * @param monsterType
	 * @return An int either 0, 1, -1. 0 is returned if the move against the
	 *         current type is average. 1 is returned if the move is strong
	 *         against the current type. -1 is return if the move is weak against
	 *         the current type
	 */
	public int getDiffernece(Type moveType, Type monsterType) {
		TypeName move = moveType.type;
		TypeName monster = monsterType.type;

		int weak = -1;
		int strong = 1;
		int neutral = 0;

		if (monster == TypeName.Ink) {// If the monster is an Ink type
			if (move == TypeName.Laser) return weak;
			else if (move == TypeName.Paper) return strong;
		} else if (monster == TypeName.Laser) {// If the monster is a Laser type
			if (move == TypeName.Ink) return weak;
		} else if (monster == TypeName.Paper) {// If the monster is a Paper type
			if (move == TypeName.Laser) return strong;
		} else if (monster == TypeName.Reinforced) {// if the monster is a Reinforced type
			if(move == TypeName.Laser)return weak;
		}

		return neutral;
	}
}
