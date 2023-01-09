package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import list.PlayList;
import map.PlayMap;

public class Foul {
	private String id;
	private String name;
	
	private PlayMap play;
	
	private static Map<String, Foul> map = new HashMap<String, Foul>();
	private static ArrayList<Foul> fouls = new ArrayList<Foul>();
	
	public static Foul get(String id, String name) {
		if (! map.containsKey(id)) {
			Foul foul = new Foul(id, name);
			fouls.add(foul);
			map.put(id, foul);
			return foul;
		}
		
		return map.get(id);
	}

	public Foul(String id) {
		this(id, null);
	}
	
	public Foul(String id, String name) {
		this.id = id;
		this.name = name;
		this.play = new PlayMap();
	}
	
	public Foul addPlay(Play play) {
		this.play.add(play);
		return this;
	}
	
	public PlayList getPlays() {
		return play.getPlays();
	}
}
