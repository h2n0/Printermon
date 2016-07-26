package uk.fls.main.core.entitys.printermon;

public class Type {

	public static Type Normal = new Type(0);
	
	
	public static Type getTypeById(int id){
		if(id == 0)return Normal;
		
		
		return Normal;
	}
	
	private int type;
	public Type(int t){
		this.type = t;
	}
	
	public boolean isWeakAgainst(Type atk){ // Printermon atk is passed in here.
		switch(type){
		
		}
		return false;
	}
	
	public boolean isStringAgainst(Type atk){// Printermon type is passed in here.
		switch(type){
		
		}	
		return false;
	}
}
