package map;

import java.util.HashMap;

import list.PlayList;
import model.Play;

public class PlayMap extends HashMap<String, Play> {
	private PlayList plays;
	
	public PlayMap() {
		super();
		plays = new PlayList();
	}
	
	public Play add(Play play) {
		if (containsKey(play.getId()))
			return get(play.getId());
		put(play.getId(), play);
		return play;
	}
	
	public PlayList getPlays() {
		return plays;
	}
}
