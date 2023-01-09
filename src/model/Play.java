package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import data.CSV;
import data.PlayerData;
import list.PlayerList;
import list.TrackingList;
import map.TrackingMap;

public class Play {
	String id;
	Game game;
	
	private Control control;
	
	// For play tracking and playback
	private int frame = 1;
	private int frameLength = -1;
	public Tracking trackBall;
	public TrackingMap trackPlayer;
	
	private PlayerList players;
	private List<Foul> fouls;
	
	String description;
	
	short quarter;
	short down;
	short yardsToGo;
	short totalYardsToGo;
	
	String offense;
	String defense;
	
	String yardline;
	short yardNumber;
	
	String clock;
	
	short homeScore;
	short awayScore;
	
	String passResult;
	short playResult;
	short playYards;
	short penaltyYards;
	
	String offenseFormation;
	String offenseIds;
	
	short defendersInBox;
	boolean playAction;
	String dropbackType;
	String passCoverage;
	String passCoverageType;
	
	public Play(Game game, String id) {
		this.game = game;
		this.id = id;
		
		trackPlayer = new TrackingMap(this);
		players = new PlayerList();
	}
	
	public Play(Game game, String[] row) {
		this(game, row[1]);
		
		description = CSV.parseString(row[2]);
		quarter = CSV.parseShort(row[3]);
		down = CSV.parseShort(row[4]);
		yardsToGo = CSV.parseShort(row[5]);
		
		offense = row[6];
		defense = row[7];
		
		yardline = CSV.parseString(row[8]);
		yardNumber = CSV.parseShort(row[9]);
		clock = CSV.parseString(row[10]);
		homeScore = CSV.parseShort(row[11]);
		awayScore = CSV.parseShort(row[12]);
		passResult = CSV.parseString(row[13]);
		penaltyYards = CSV.parseShort(row[14]);
		playYards = CSV.parseShort(row[15]);
		playResult = CSV.parseShort(row[16]);
		
		for (int i = 17 ; i <= 21 ; i += 2) {
			// Up to 3 penalties, NA signifies end of list
			if (row[i].equals("NA"))
				break;
			// Get the unique foul instance 
			addFoul(Foul.get(row[i], row[i + 1]));
		}
		
		totalYardsToGo = CSV.parseShort(row[23]);
		offenseFormation = row[24];
		// TODO: parse offensive personnel from row[25]
		// Format: # RB, # TE, # WR
		defendersInBox = CSV.parseShort(row[26]);
		// TODO: parse defensive personnel from row[27]
		// Format: # DL, # LB, # DB
		dropbackType = CSV.parseString(row[28]);
		playAction = row[29].equals("1");
		passCoverage = row[30];
		passCoverageType = row[31];
	}
	
	private void addFoul(Foul foul) {
		if (fouls == null)
			fouls = new ArrayList<Foul>();
		fouls.add(foul);
		foul.addPlay(this);
	}

	public Pocket getPocket() {
		return new Pocket(this);
	}

	public String getId() {
		return this.id;
	}
	
	public boolean hasId(String id) {
		return this.id.equals(id);
	}
	
	public boolean hasTracking() {
		return trackBall != null && trackBall.getFrames() > 0;
	}
	
	public Set<String> getPlayerIds() {
		return trackPlayer.keySet();
	}
	
	public Tracking getPlayer(String id) {
		return trackPlayer.get(id);
	}
	
	public boolean hasPlayer(String id) {
		return trackPlayer.containsKey(id);
	}
	
	public boolean hasPlayer(Player player) {
		return trackPlayer.containsKey(player.getId());
	}
	
	public Team getOffenseTeam() {
		return game.getTeam(offense);
	}
	
	public TrackingList getOffense() {
		return trackPlayer.getTeam(offense);
	}
	
	public Team getDefenseTeam() {
		return game.getTeam(defense);
	}
	
	public TrackingList getDefense() {
		return trackPlayer.getTeam(defense);
	}
	
	public boolean isOffense(String id) {
		return trackPlayer.get(id).isOnTeam(offense);
	}
	
	public boolean isOffense(Player player) {
		return trackPlayer.get(player.getId()).isOnTeam(offense);
	}
	
	public boolean isDefense(String id) {
		return trackPlayer.get(id).isOnTeam(defense);
	}
	
	public boolean isDefense(Player player) {
		return trackPlayer.get(player.getId()).isOnTeam(defense);
	}
	
	public Tracking loadPlayer(String id, PlayerData playerData) {
		if (! trackPlayer.containsKey(id))
			return trackPlayer.addPlayer(playerData.getPlayer(id));
		else
			return trackPlayer.get(id);
	}
	
	public Position getBallPosition(int frame) {
		if (trackBall == null)
			return null;
		return trackBall.getPosition(frame);
	}
	
	public Position getPosition(String id, int frame) {
		return trackPlayer.get(id).getPosition(frame);
	}
	
	public Tracking getBallTracking() {
		return getTracking(null);
	}
	
	public Tracking getTracking(Player player) {
		if (player == null) {
			if (trackBall == null)
				trackBall = new Tracking(this.game, this);
			return this.trackBall;
		}
		else {
			return trackPlayer.addPlayer(player);
		}
	}
	
	public boolean directionRight() {
		if (trackBall != null) {
			return trackBall.directionRight();
		}
		return false;
	}

	public int getScrimmageYard() {
		// Special case
		if (yardNumber == 50)
			return 60;
		
		if (directionRight()) {
			if (yardline.equals(offense))
				return 10 + yardNumber;
			else
				return 110 - yardNumber;
		}
		else {
			if (yardline.equals(offense))
				return 110 - yardNumber;
			else
				return 10 + yardNumber;
		}
	}
	
	public int getFirstDownYard() {
		if (directionRight()) {
			return getScrimmageYard() + yardsToGo;
		}
		else 
			return getScrimmageYard() - yardsToGo;
	}
	
	public int getFrames() {
		if (trackBall != null)
			return trackBall.getFrames();
		return 0;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public int frameSize() {
		if (frameLength >= 0)
			return frameLength;
		if (trackBall != null)
			return frameLength = trackBall.getFrames();
		return 0;
	}
	
	public boolean restartFrame() {
		return setFrame(1);
	}
	
	public boolean setFrame(int frame) {
		this.frame = Math.max(1, Math.min(frame, frameSize()));
		return this.frame == frame;
	}
	
	public boolean addFrame(int change) {
		return setFrame(frame + change);
	}
	
	public boolean nextFrame() {
		return setFrame(frame + 1);
	}
	
	public boolean previousFrame() {
		return setFrame(frame - 1);
	}
	
	public Control getControl(int width, int height) {
		if (control != null)
			return control;
		return control = new Control(this, width, height);
	}

	public String getClockString() {
		return clock;
	}

	public int getQuarter() {
		return quarter;
	}

	public boolean resultedInSack() {
		return false;
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append(game.getName());
		return sb.toString();
	}

	public Game getGame() {
		return game;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(game.toString()).append("\n");
		sb.append(game.homeTeam).append(" - ").append(homeScore).append("    ")
		  .append(game.awayTeam).append(" - ").append(awayScore).append("\n");
		
		sb.append(description).append("\n");
		sb.append("Offense: ").append(offense).append(" at ")
		  .append(yardline).append(" ").append(yardNumber)
		  .append(", quarter ").append(quarter).append(" down ").append(down)
		  .append(", needs ").append(yardsToGo).append("\n");
		
		return sb.toString();
	}

	public String getDescription() {
		return description;
	}

	public int getDown() {
		return down;
	}
	
	public String getPassResult() {
		return passResult;
	}
	
	public int getPenaltyYards() {
		return penaltyYards;
	}

	public int getResult() {
		return playResult;
	}

	public TrackingList getTracking() {
		return trackPlayer.getPlayers();
	}
	
	public Play nextPlay() {
		int index = game.getIndex(this);
		if (index >= 0 && index < game.playCount()) {
			return game.getPlayIndex(index + 1);
		}
		return null;
	}
	
	public Play previousPlay() {
		int index = game.getIndex(this);
		if (index > 0) {
			return game.getPlayIndex(index - 1);
		}
		return null;
	}

}
