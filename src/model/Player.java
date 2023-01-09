package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import list.PlayList;

public class Player {
	
	private String id;
	
	// Tracking data for this player
	private Map<String, Play> play;
	private PlayList plays;
	
	private List<Tracking> tracking;
	
	// Height in inches, weight in pounds
	private int height;
	private int weight;
	
	// Player information
	private String name;
	private String position;
	private String birthday;
	private String college;
	
	// Computed from birthday if provided
	private int age;
	
	private int sack;
	private int sackAllowed;
	
	private int hurry;
	private int hurryAllowed;
	
	private int hit;
	private int hitAllowed;
	
	private int defended;
	
	// Cached and computed from 
	
	public Player(String id) {
		this.id = id;
		
		this.plays = new PlayList();
		this.tracking = new ArrayList<Tracking>();
	}
	
	public Player(String[] row) {
		this(row[0]);
		
		// Convert height to inches
		String[] height = row[1].split("-");
		this.height = Integer.parseInt(height[0]) * 12 + Integer.parseInt(height[1]);
		this.weight = Integer.parseInt(row[2]);
		
		// All players have name and position set
		this.name = row[6];
		this.position = row[5];
		// Ignore "NA" values
		if (! row[4].equals("NA"))
			this.college = row[4];
		if (! row[3].equals("NA"))
			this.birthday = row[3];
	}
	
	public int getAge() {
		if (age == 0 && birthday != null && ! birthday.equals("NA")) {
			
		}
		return age;
	}

	public String getId() {
		return this.id;
	}

	public void addTracking(Tracking tracking) {
		this.tracking.add(tracking);
		this.plays.add(tracking.getPlay());
	}
	
	public List<Tracking> getTracking() {
		return tracking;
	}

	public void computeScoutingTotals() {
		for (Tracking t : this.tracking) {
			this.sack += t.sack ? 1 : 0;
			this.sackAllowed += t.sackAllowed ? 1 : 0;
			this.hit += t.hit ? 1 : 0;
			this.hitAllowed += t.hitAllowed ? 1 : 0;
			this.hurry += t.hurry ? 1 : 0;
			this.hurryAllowed += t.hurryAllowed ? 1 : 0;
			this.defended += t.defended ? 1 : 0;
		}
	}

	public String getName() {
		return this.name;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWeight() {
		return weight;
	}

	public int getSacks() {
		return sack;
	}
	
	public int getSacksAllowed() {
		return sackAllowed;
	}

	public String getPosition() {
		return position;
	}
	
	public boolean hasPosition(String position) {
		return this.position.equals(position);
	}
	
	public int playCount() {
		return plays.size();
	}

	public PlayList getPlays() {
		return plays;
	}
}
