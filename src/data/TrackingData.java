package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import model.Game;
import model.Play;
import model.Player;
import model.Tracking;

public class TrackingData extends CSV {
	private GameData gameData;
	private PlayerData playerData;
	
	// For sorting frames
	private HashSet<Tracking> tracking;
	
	// If only loading tracking for a specific game
	private String targetGame = null;
	private Set<String> targetPlay = null;
	
	private Game currentGame;
	private Play currentPlay;
	private Player currentPlayer;
	
	public TrackingData(GameData gameData, PlayerData playerData, int week) {
		this(gameData, playerData, week, null, null);
	}
	
	public TrackingData(GameData gameData, PlayerData playerData, Game game) {
		this(gameData, playerData, game.getWeek(), game.getId(), null);
	}
	
	public TrackingData(GameData gameData, PlayerData playerData, int week, Set<String> targetPlay) {
		this(gameData, playerData, week, null, targetPlay);
	}
	
	private TrackingData(GameData gameData, PlayerData playerData, int week, String targetGame, Set<String> targetPlay) {
		super("tracking/week" + week);
		this.gameData = gameData;
		this.playerData = playerData;
		this.targetGame = targetGame;
		this.targetPlay = targetPlay;
		
		// Cache all modified tracking objects
		this.tracking = new HashSet<Tracking>();
		this.read();
		
		// Sort all frames
		for (Tracking t : tracking)
			t.sortFrames();
	}

	@Override
	protected void process(String[] row) {
		// Cache and only change reference on ID change
		String gameId = row[0];
		
		// If only loading a specific game's tracking data
		if (targetGame != null && ! gameId.equals(targetGame))
			return;
		
		if (currentGame == null || ! currentGame.getId().equals(gameId))
			currentGame = gameData.getGame(gameId);
		
		// Load play ID and update object reference if changed
		String playId = row[1];
		
		// If only loading specific plays
		if (targetPlay != null && ! targetPlay.contains(playId))
			return;
		
		if (currentPlay == null || ! currentPlay.getId().equals(playId))
			currentPlay = currentGame.getPlay(playId);
		
		// Either player or ball position
		String nflId = row[2];
		if (nflId.equals("NA")) {
			Tracking ball = currentPlay.getBallTracking(); 
			ball.addFrame(row);
			tracking.add(ball);
		}
		else {
			if (currentPlayer == null || ! currentPlayer.getId().equals(nflId)) {
				currentPlayer = playerData.getPlayer(nflId);
			}
			Tracking player = currentPlay.getTracking(currentPlayer);
			player.addFrame(row);
			tracking.add(player);
		}
	}
	
	
}
