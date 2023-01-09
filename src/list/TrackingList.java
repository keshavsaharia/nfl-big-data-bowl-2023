package list;

import java.util.ArrayList;

import model.Tracking;
import model.util.Range2D;

public class TrackingList extends ArrayList<Tracking> {

	public TrackingList() {
		super();
	}
	
	public Range2D getRange() {
		return new Range2D(this);
	}

	public TrackingList filterTeam(String team) {
		TrackingList filtered = new TrackingList();
		for (Tracking player : this)
			if (player.isOnTeam(team))
				filtered.add(player);
		System.out.println("filtered: " + filtered.size());
		return filtered;
	}

	public Tracking random() {
		return get((int) Math.floor(Math.random() * size()));
	}
}
