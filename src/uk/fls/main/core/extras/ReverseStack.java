package uk.fls.main.core.extras;

import java.util.ArrayList;
import java.util.List;

public class ReverseStack {
	
	public List<String[]> data;
	
	public ReverseStack() {
		this.data = new ArrayList<String[]>();
	}
	
	public String[] peek(){
		if(this.data.isEmpty())return null;
		return this.data.get(this.data.size()-1);
	}
	
	public void push(String[] l){
		this.data.add(l);
	}
	
	public String[] pop(){
		if(this.data.isEmpty())return null;
		return this.data.remove(this.data.size()-1);
	}
}
