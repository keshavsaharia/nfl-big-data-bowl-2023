package map;

import java.util.HashMap;

import list.TrackingList;
import model.Play;
import model.Player;
import model.Tracking;

public class TrackingMap extends HashMap<String, Tracking> {

	private Play play;
	private Tracking ball;
	private TrackingList tracking;
	private HashMap<String, TrackingList> teamTracking;
	
	public TrackingMap(Play play) {
		super();
		this.play = play;
		this.tracking = new TrackingList();
		this.teamTracking = new HashMap<String, TrackingList>(2);
	}
	
	public Tracking addBall() {
		if (ball != null)
			return ball;
		ball = new Tracking(play, null);
		return ball;
	}
	
	public boolean hasBall() {
		return ball != null;
	}
	
	public Tracking addPlayer(Player player) {
		if (containsKey(player.getId()))
			return get(player.getId());
		
		Tracking tracking = new Tracking(play, player);
		this.tracking.add(tracking);
		put(player.getId(), tracking);
		player.addTracking(tracking);
		
		return tracking;
	}
	
	public TrackingList getTeam(String team) {
		if (teamTracking.containsKey(team))
			return teamTracking.get(team);
		
		// Build tracking list from this array
		TrackingList players = new TrackingList();
		for (Tracking player : this.tracking)
			if (player.isOnTeam(team))
				players.add(player);
		
		// Cache and return tracking list
		teamTracking.put(team, players);
		return players;
	}
	
	public TrackingList getPlayers() {
		return tracking;
	}
}
