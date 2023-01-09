package list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Play;

public class PlayList extends ArrayList<Play> {

	public PlayList() {
		super();
	}
	
	public PlayList(PlayList copy) {
		for (Play p : this)
			this.add(p);
	}
	
	public PlayList copy() {
		return new PlayList(this);
	}
	
	public PlayList filterSacks() {
		PlayList filtered = new PlayList();
		for (Play play : this)
			if (play.resultedInSack())
				filtered.add(play);
		return filtered;
	}
	
	public Set<String> getIds() {
		Set<String> ids = new HashSet<String>();
		for (Play play : this)
			ids.add(play.getId());
		return ids;
	}
}
