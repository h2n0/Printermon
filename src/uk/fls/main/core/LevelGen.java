package uk.fls.main.core;

import java.util.Arrays;
import java.util.HashMap;

import fls.engine.main.io.FileIO;
import uk.fls.main.core.extras.Warp;
import uk.fls.main.core.tiles.Tile;

public class LevelGen {

	public Tile[][] tiles;
	public byte[][] data;
	public HashMap<Integer, String[]> dialogs;
	public HashMap<Integer, Warp> travel;
	public int w,h;
	
	public boolean inside;
	
	private LevelGen(Tile[][] t, byte[][] d,HashMap<Integer, String[]> con,HashMap<Integer, Warp> tra, int w, int h, boolean inside){
		this.tiles = t;
		this.data = d;
		this.dialogs = con;
		this.travel = tra;
		this.w = w;
		this.h = h;
		this.inside = inside;
	}
	
	public static LevelGen loadLevel(String name){
		String[] lines = FileIO.instance.readInternalFile("/rooms"+name).split("\n");
		int cx = 0;
		int cy = 0;
		
		int mx = 0;
		int my = 0;
		
		
		boolean inSide = false;
		boolean inLevel = false;
		
		if(!lines[0].startsWith("/")){// Not immediately in level data
			if(lines[0].startsWith("?")){// An extra bit of data
				String l = lines[0].substring(1);
				String[] parts = l.split(":");
				String k = parts[0].trim();
				String v = parts[1].trim();
				if(k.equals("inside")){//Crutial part
					inSide = Boolean.parseBoolean(v);
					System.out.println(inSide);
				}
			}
		}
		for(int i = 0; i < lines.length; i++){
			if(lines[i].startsWith("#") || lines[i].isEmpty())continue;
			if(lines[i].startsWith("/")){//Command line
				String l = lines[i];
				l = l .substring(1).trim();
				if(l.startsWith("L")){
					inLevel = true;
				}else if(l.startsWith("/")){
					inLevel = false;
				}
			}
			
			if(!inLevel)continue;
			String[] parts = lines[i].split(",");
			for(int j = 0; j < parts.length; j++){
				cx ++;
				if(cx > mx)mx = cx;
			}
			cy++;
			if(cy > my)my = cy;
			cx = 0;
		}
		cy = 0;
		Tile[][] res = new Tile[mx][my];
		byte[][] data = new byte[mx][my];
		for(int i = 0; i < mx; i++){
			Arrays.fill(data[i], (byte)-1);
		}
		inLevel = false;
		boolean finishedWithLevel = false;
		boolean dialog = false;
		boolean warps = false;
		HashMap<Integer, String[]> convos = new HashMap<Integer, String[]>();
		HashMap<Integer, Warp> travel = new HashMap<Integer, Warp>();
		String lastCMDLetter = "";
		
		int dialogId = -1;
		String currentDialogOut = "";
		
		int travelId = -1;
		String travelLoc = "";
		int travelLocX = 0, travelLocY = 0;
		for(int i = 0; i < lines.length; i++){
			if(lines[i].startsWith("/")){//Command line
				String l = lines[i];
				l = l .substring(1).trim();
				if(l.startsWith("L")){
					inLevel = true;
					lastCMDLetter = "L";
					continue;
				}else if(l.startsWith("/") && lastCMDLetter.equals("L")){
					if(inLevel)finishedWithLevel = true;
					inLevel = false;
				}
				
				if(l.startsWith("D")){
					dialog = true;
					lastCMDLetter = "D";
					continue;
				}else if(l.startsWith("/") && lastCMDLetter.equals("D")){
					dialog = false;
				}
				
				if(l.startsWith("T")){
					warps = true;
					lastCMDLetter = "T";
					continue;
				}else if(l.startsWith("/") && lastCMDLetter.equals("T")){
					warps = false;
				}
			}
			
			if(!finishedWithLevel){
				if(!inLevel)continue;
				if(lines[i].startsWith("#") || lines[i].isEmpty())continue;
				String[] parts = lines[i].split(",");
				for(int j = 0; j < parts.length; j++){
					int val = -1;
					int dFlag = -1;
					if(parts[j].indexOf(":")!=-1){// Data set flag
						dFlag = Integer.parseInt(parts[j].substring(parts[j].indexOf(":")+1));
						val = Integer.parseInt(parts[j].substring(0,parts[j].indexOf(":")));
					}else{
						val = Integer.parseInt(parts[j]);
					}
					res[cx][cy] = Tile.tiles[val];
					if(dFlag != -1){
						data[cx][cy] = (byte)dFlag;
						dFlag = -1;
					}
					cx ++;
				}
				cy++;
				cx = 0;
			}else if(dialog){//Dialog assignment
				String line = lines[i];
				if(line.isEmpty())continue;
				if(line.startsWith("/")){
					line = line.substring(1);
					if(line.startsWith("[")){// Dialog id
						line = line.substring(1);
						line = line.substring(0, line.indexOf("]")).trim();
						int id = Integer.parseInt(line);
						dialogId = id;
					}else if(line.startsWith("#")){// End of dialog
						currentDialogOut = currentDialogOut.trim();
						convos.put(dialogId, currentDialogOut.split(" "));
						currentDialogOut = "";
						
					}
				}else if(line.startsWith("#")){
					line = line.substring(1).trim();
					currentDialogOut += line + " ";
				}
			}else if(warps){// Travel assignment
				String line = lines[i];
				if(line.isEmpty())continue;
				if(line.startsWith("/")){
					line = line.substring(1);
					if(line.startsWith("[")){
						line = line.substring(1);
						line = line.substring(0, line.indexOf("]")).trim();
						int id = Integer.parseInt(line);
						travelId = id;
					}else if(line.startsWith("#")){
						Warp p = new Warp(travelLoc, travelLocX, travelLocY);
						travel.put(travelId, p);
					}
				}else if(line.startsWith("#")){
					line = line.substring(1).trim();
					travelLoc = line;
				}else if(line.startsWith("~")){
					line = line.substring(1).trim();
					String[] parts = line.split(",");
					int x = Integer.parseInt(parts[0].trim());
					int y = Integer.parseInt(parts[1].trim());
					travelLocX = x;
					travelLocY = y;
				}
			}
		}
		return new LevelGen(res, data, convos, travel,  mx, my, inSide);
	}
}
