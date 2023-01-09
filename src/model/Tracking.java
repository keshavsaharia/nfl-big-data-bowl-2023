package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import list.PositionList;

public class Tracking {
	
	private Game game;
	private Play play;
	
	// If null, represents the ball
	private Player player;
	
	// For players
	private String team;
	private short jersey;
	
	// Player lineup position (QB, RB, etc)
	private String role;
	private String linedUp;
	
	private boolean offense;
	private boolean defense;
	
	/**
	 * If player is a defensive player, indicator for whether they 
	 * are credited with recording a hit on this play.
	 */
	boolean hit;
	
	/**
	 *  If player is a blocking offensive player, indicator for whether they are responsible for a hit on the QB 
	 */
	boolean hitAllowed;
	
	/**
	 * If player is a defensive player, indicator for whether they 
	 * are credited with recording a hurry on this play.
	 */
	boolean hurry;
	
	/**
	 * If player is a blocking offensive player, indicator for whether they are responsible for a hurry on the QB 
	 */
	boolean hurryAllowed;
	
	/**
	 * If player is a defensive player, indicator for whether they 
	 * are credited with recording a sack on this play.
	 */
	boolean sack;
	
	/**
	 *  If player is a blocking offensive player, indicator for whether they are responsible for a sack on the QB
	 */
	boolean sackAllowed;
	
	/**
	 * If player is a blocking offensive player, indicator for whether 
	 * they are by a defender but was not charged for yielding a hit, hurry or sack
	 */
	boolean defended;
	
	/**
	 * If player is a blocking offensive player, the nflId of the first defender the offensive player blocked
	 */
	private String blockedPlayer;
	private boolean backfieldBlock;
	private String blockType;
	
	// Directory of play movement
	private boolean playLeft = false;
	
	private PositionList position;
	
	public Tracking(Play play) {
		this(play.getGame(), play, null);
	}
	
	public Tracking(Play play, Player player) {
		this(play.getGame(), play, player);
	}
	
	public Tracking(Game game, Play play) {
		this(game, play, null);
	}
	
	public Tracking(Game game, Play play, Player player) {
		this.game = game;
		this.play = play;
		this.player = player;
		this.position = new PositionList();
	}
	
	public void sortFrames() {
		position.sortByFrame();
	}
	
	public boolean isPlayer() {
		return player != null;
	}
	
	public boolean isBall() {
		return player == null;
	}

	public void addFrame(String[] row) {
		// If player details not loaded
		if (this.jersey == 0 && isPlayer() && !row[5].equals("NA")) {
			this.jersey = Short.parseShort(row[5]);
			this.team = row[6];
			this.playLeft = row[7].equals("left");
		}
		// Ball frame
		else if (isBall()) {
			this.playLeft = row[7].equals("left");
		}
		
		// Add to frame list
		this.position.add(new Position(row));
	}

	public Position getPosition(int frame) {
		if (frame > this.position.size())
			return this.position.get(this.position.size() - 1);
		return this.position.get(frame - 1);
	}
	
	public Position getPositionIndex(int frame) {
		return this.position.get(frame);
	}

	public boolean isOnTeam(String team) {
		return this.team != null && this.team.equals(team);
	}

	public int getFrames() {
		return position.size();
	}

	public boolean directionRight() {
		return ! this.playLeft;
	}
	
	/**
	 * Possible values:
		Coverage: Defensive player. Player whose initial goal is to play man or zone coverage
		Pass: Offensive player. Player identified as the passer
		Pass block: Offensive player. Anyone fully blocking a defender from the QB, or anyone in a clear pass block stance
		Pass route: Offensive player. Any player not identified as a Pass Blocker or Passer
		Pass rush: Defensive player. Any player whose initial intent is to rush the passer
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role.toLowerCase();
		
		// Set offense/defense flags
		if (role.equals("coverage") || role.equals("pass rush"))
			defense = true;
		else if (role.startsWith("pass"))
			offense = true;
	}

	public void setLinedUpPosition(String linedUp) {
		this.linedUp = linedUp;
	}

	public void setHit(boolean hit, boolean hitAllowed) {
		this.hit = hit;
		this.hitAllowed = hitAllowed;
	}

	public void setHurry(boolean hurry, boolean hurryAllowed) {
		this.hurry = hurry;
		this.hurryAllowed = hurryAllowed;
	}

	public void setSack(boolean sack, boolean sackAllowed) {
		this.sack = sack;
		this.sackAllowed = sackAllowed;
	}

	public void setDefended(boolean defended) {
		this.defended = defended;
	}

	public void setBlockedPlayer(String nflId) {
		if (! nflId.equals("NA")) {
			this.blockedPlayer = nflId;
		}
	}

	public void setBackfieldBlock(boolean backfieldBlock) {
		this.backfieldBlock = backfieldBlock;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public Play getPlay() {
		return play;
	}

	public boolean isLinedUp(String position) {
		return linedUp != null && linedUp.equals(position);
	}

	public Player getPlayer() {
		return player;
	}

	public boolean hasRole(String role) {
		return this.role != null && this.role.equals(role);
	}

	public PositionList getPositions() {
		return position;
	}

	public Position firstPosition() {
		return position.get(0);
	}

	public int positionCount() {
		return position.size();
	}
}
