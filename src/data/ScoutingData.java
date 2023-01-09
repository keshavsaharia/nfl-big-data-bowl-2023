package data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Player;
import model.Tracking;

public class ScoutingData extends CSV {

	private GameData gameData;
	private PlayerData playerData;
	
	private Set<String> roles;
	private String[] roleArray;
	
	private Set<String> positions;
	private String[] positionArray;
	
	private Set<String> blockTypes;
	private String[] blockTypeArray;
	
	
	public ScoutingData(GameData gameData, PlayerData playerData) {
		super("scouting");
		this.gameData = gameData;
		this.playerData = playerData;
		
		// Initialize string sets
		roles = new HashSet<String>();
		positions = new HashSet<String> ();
		blockTypes = new HashSet<String> ();
		
		this.read();
		
		for (Player player : playerData.getPlayers()) {
			player.computeScoutingTotals();
		}
	}

	@Override
	protected void process(String[] row) {
		Tracking tracking = gameData.getGame(row[0]).getPlay(row[1]).loadPlayer(row[2], playerData);
		
		String role = row[3];
		tracking.setRole(role);
		roles.add(role);
		
		String linedUp = row[4];
		tracking.setLinedUpPosition(linedUp);
		positions.add(linedUp);
		
		tracking.setHit(row[5].equals("1"), row[9].equals("1"));
		tracking.setHurry(row[6].equals("1"), row[10].equals("1"));
		tracking.setSack(row[7].equals("1"), row[11].equals("1"));
		tracking.setDefended(row[8].equals("1"));
		
		// TODO: create matchup object
		tracking.setBlockedPlayer(row[12]);
		
		String blockType = row[13];
		if (! blockType.equals("NA")) {
			tracking.setBlockType(blockType);
			blockTypes.add(blockType);
		}
		
		tracking.setBackfieldBlock(row[14].equals("1"));
	}
	
	public String[] getRoles() {
		if (roleArray == null) {
			roleArray = roles.toArray(new String[roles.size()]);
		}
		return roleArray;
	}
	
	public String[] getPositions() {
		if (positionArray == null) {
			positionArray = positions.toArray(new String[positions.size()]);
		}
		return positionArray;
	}
	
	public String[] getBlockTypes() {
		if (blockTypeArray == null) {
			blockTypeArray = blockTypes.toArray(new String[blockTypes.size()]);
		}
		return blockTypeArray;
	}

}
