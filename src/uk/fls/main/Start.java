package uk.fls.main;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import uk.fls.main.screens.GameScreen;

@SuppressWarnings("serial")
public class Start extends Init{

	public static int w = 160;
	public static int h = 144;
	public static int s = 3;
	
	public Start(){
		super("Printermon prototype", w * s, h * s);
		useCustomBufferedImage(w, h, false);
		setInput(new Input(this, Input.KEYS));
		setScreen(new GameScreen());
		skipInit();
	}
	
	public static void main(String[] args){
		new Start().start();
	}
}
