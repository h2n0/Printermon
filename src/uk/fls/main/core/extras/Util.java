package uk.fls.main.core.extras;

public class Util {

	
	public static float getRandom(float min, float max){
		return (float)(min + Math.random() * (max-min + 1));
	}
}
