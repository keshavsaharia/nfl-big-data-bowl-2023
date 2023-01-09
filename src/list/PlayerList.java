package list;

import java.util.ArrayList;
import java.util.Comparator;

import model.Player;

public class PlayerList extends ArrayList<Player> {

	public PlayerList() {
		super();
	}
	
	public PlayerList(PlayerList copy) {
		for (Player p : copy)
			this.add(p);
	}
	
	public PlayerList copy() {
		return new PlayerList(this);
	}
	
	public PlayerList sortByPlayCount() {
		PlayerList sorted = new PlayerList(this);
		sorted.sort(playCount);
		return sorted;
	}
	
	public PlayerList takeFirst(int first) {
		if (first > this.size())
			this.removeRange(first, this.size());
		return this;
	}
	
	public PlayerList filterPosition(String position) {
		PlayerList filtered = new PlayerList();
		for (Player p : this)
			if (p.hasPosition(position))
				filtered.add(p);
		return filtered;
	}
	
	private static final Comparator<Player> playCount = new Comparator<Player>() {
		public int compare(Player a, Player b) {
			return b.playCount() - a.playCount();
		}
	};
}
