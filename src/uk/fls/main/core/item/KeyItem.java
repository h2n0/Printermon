package uk.fls.main.core.item;

public class KeyItem implements Item{
	
	public static KeyItem[] keyItems = new KeyItem[255];
	
	public static KeyItem runningShoes = new KeyItem(0, "Running Shoes");
	public static KeyItem bike = new KeyItem(1, "Fast Bike", true);
	
	
	private final byte id;
	private final String name;
	private boolean outsideUse;
	
	public KeyItem(int id, String name){
		if(keyItems[id] != null)throw new RuntimeException("Duplicate Ids: " + id);
		this.id = (byte)id;
		this.name = name;
		this.outsideUse = false;
	}
	
	public KeyItem(int id, String name, boolean outside){
		this(id, name);
		this.outsideUse = outside;
	}
	
	public void useItem() {
		
	}

}
