package uk.fls.main.core;

import java.util.Arrays;
import java.util.HashMap;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.Point;
import uk.fls.main.core.extras.LevelRange;
import uk.fls.main.core.extras.Warp;
import uk.fls.main.core.tiles.Tile;

public class LevelGen {

	public Tile[][] tiles;
	public byte[][] data;
	public HashMap<Integer, String[]> dialogs;
	public HashMap<Integer, Warp> travel;
	public int[] monsters;
	public int w,h;
	public LevelRange levelRange;
	
	public boolean inside;
	public boolean safe;
	
	private LevelGen(Tile[][] t, byte[][] d,HashMap<Integer, String[]> con,HashMap<Integer, Warp> tra, int[] mons, LevelRange lvlRange, int w, int h, boolean inside, boolean safe){
		this.tiles = t;
		this.data = d;
		this.dialogs = con;
		this.travel = tra;
		this.w = w;
		this.h = h;
		this.inside = inside;
		this.safe = safe;
		this.monsters = mons;
		this.levelRange = lvlRange;
	}
	
	public static LevelGen loadLevel(String name){
		String[] lines = FileIO.instance.readInternalFile("/rooms"+name).split("\n");
		int cx = 0;
		int cy = 0;
		
		int mx = 0;
		int my = 0;
		
		boolean inSide = true;
		boolean isSafe = false;
		boolean inLevel = false;
		
		if(!lines[0].startsWith("/")){ // Not immediately in level data
			if(lines[0].startsWith("?")){ // An extra bit of data
				String l = lines[0].substring(1);
				String[] parts = l.split(":");
				String k = parts[0].trim();
				String v = parts[1].trim();
				if(k.equals("inside")){ //Critical part
					inSide = Boolean.parseBoolean(v);
				}
				
				if(k.equals("safe")){
					isSafe = Boolean.parseBoolean(v);
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
			for(int j = 0;j < my; j++){
				data[i][j] = (byte)-1;
			}
		}
		
		inLevel = false;
		boolean finishedWithLevel = false;
		boolean dialog = false;
		boolean warps = false;
		boolean monsters = false;
		HashMap<Integer, String[]> convos = new HashMap<Integer, String[]>();
		HashMap<Integer, Warp> travel = new HashMap<Integer, Warp>();
		int[] mons = new int[1];
		String lastCMDLetter = "";
		
		LevelRange lvlRange = null;
		
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
				
				if(l.startsWith("P")){
					monsters = true;
					lastCMDLetter = "P";
					continue;
				}else if(l.startsWith("/") && lastCMDLetter.equals("P")){
					monsters = false;
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
			}else if(monsters){
				String line = lines[i];
				if(!line.startsWith("~")){
					line = line.substring(1).trim();
					String[] ids = line.split(",");
					for(String id : ids){
						mons = addMonsters(mons, Integer.parseInt(id.trim()));
					}
				}else{
					line = line.substring(1).trim();
					String[] vals = line.split(",");
					lvlRange = new LevelRange(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
				}
			}
		}
		return new LevelGen(res, data, convos, travel,  mons, lvlRange, mx, my, inSide, isSafe);
	}
	
	public static LevelGen genLevel(String[] sdata){
		String[] lines = sdata;
		int cx = 0;
		int cy = 0;
		
		int mx = 0;
		int my = 0;
		
		
		boolean inSide = false;
		boolean isSafe = false;
		boolean inLevel = false;
		
		if(!lines[0].startsWith("/")){ // Not immediately in level data
			if(lines[0].startsWith("?")){ // An extra bit of data
				String l = lines[0].substring(1);
				String[] parts = l.split(":");
				String k = parts[0].trim();
				String v = parts[1].trim();
				if(k.equals("inside")){ //Critical part
					inSide = Boolean.parseBoolean(v);
				}
				
				if(k.equals("safe")){
					isSafe = Boolean.parseBoolean(v);
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
		boolean monsters = false;
		HashMap<Integer, String[]> convos = new HashMap<Integer, String[]>();
		HashMap<Integer, Warp> travel = new HashMap<Integer, Warp>();
		String lastCMDLetter = "";
		
		LevelRange lvlRange = null;
		
		int[] mons = new int[1];
		
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
				
				if(l.startsWith("P")){
					monsters = true;
					lastCMDLetter = "P";
					continue;
				}else if(l.startsWith("/") && lastCMDLetter.equals("P")){
					monsters = false;
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
			}else if(monsters){
				String line = lines[i];
				if(!line.startsWith("~")){
					line = line.substring(1).trim();
					String[] ids = line.split(",");
					for(String id : ids){
						mons = addMonsters(mons, Integer.parseInt(id.trim()));
					}
				}else{
					line = line.substring(1).trim();
					String[] vals = line.split(",");
					lvlRange = new LevelRange(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
				}
			}
		}
		return new LevelGen(res, data, convos, travel, mons, lvlRange, mx, my, inSide, isSafe);
	}
	
	private static int[] addMonsters(int[] mons, int id){
		int[] res = new int[mons.length+1];
		for(int i = 0; i < mons.length; i++){
			res[i] = mons[i];
		}
		res[mons.length] = id;
		return res;
	}
}
