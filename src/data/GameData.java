package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import list.GameList;
import list.PlayList;
import list.PlayerList;
import model.Game;
import model.Play;
import model.Player;

public class GameData extends CSV {
	
	GameList games;
	HashMap<String, Game> game;
	
	PlayData playData;
	TeamData teamData;
	PlayerData playerData;
	TrackingData trackingData;
	ScoutingData scoutingData;
	
	public GameData() {
		super("games");
			
		// Create game mapping and array
		game = new HashMap<String, Game>();
		games = new GameList();
		
		// Read CSV file to populate game array and mapping
		this.read();
		System.out.println("Loaded " + this.games.size() + " games");
		
		// Load play, player, and scouting data
		playData = new PlayData(this);
		teamData = new TeamData(this);
		playerData = new PlayerData();
		scoutingData = new ScoutingData(this, playerData);
	}
	
	public void loadTrackingData() {
		for (int week = 1 ; week <= 8 ; week++) {
			loadTrackingData(week);
		}
	}
	
	public void loadTrackingData(int week) {
		new TrackingData(this, playerData, week);
		System.out.println("Loaded tracking data for week " + week);
	}
	
	/**
	 * Read tracking data for a specific game
	 * @param game
	 */
	public void loadTrackingData(Game game) {
		if (game.hasTracking())
			return;
		new TrackingData(this, playerData, game);
	}
	
	public void loadTrackingData(PlayList plays) {
		for (int week = 1 ; week <= 8 ; week++)
			new TrackingData(this, playerData, week, plays.getIds());
	}

	@Override
	protected void process(String[] row) {
		Game game = new Game(row);
		
		// Add to mapping/array
		this.game.put(game.getId(), game);
		this.games.add(game);
	}

	public Game getGame(String id) {
		return this.game.get(id);
	}

	public Game randomGame() {
		return this.games.get((int) Math.floor(Math.random() * this.games.size()));
	}

	public GameList getGames() {
		return this.games;
	}
	
	public PlayList getPlays() {
		return this.playData.getPlays();
	}
	
	public PlayerList getPlayers() {
		return this.playerData.getPlayers();
	}

	public PlayerList getPlayerPosition(String position) {
		return this.playerData.getPosition(position);
	}

	public TeamData getTeamData() {
		return teamData;
	}
}
