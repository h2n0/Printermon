package uk.fls.main.core.extras;

public class LevelRange {

	
	private final int min,max;
	
	public LevelRange(final int min, final int max){
		this.min = min;
		this.max = max;
	}
	
	public int getMin(){
		return this.min;
	}
	
	public int getMax(){
		return this.max;
	}
}
