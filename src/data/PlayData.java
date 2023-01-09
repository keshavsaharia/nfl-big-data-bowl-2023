package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import list.PlayList;
import model.Game;
import model.Play;

public class PlayData extends CSV {
	private GameData gameData;
	
	private Game currentGame;
	private String currentGameId;
	
	private PlayList plays;
	
	public PlayData(GameData gameData) {
		super("plays");
		this.gameData = gameData;
		
		plays = new PlayList();
		this.read();
	}

	@Override
	protected void process(String[] row) {
		// Cache and only change reference on ID change
		if (currentGameId == null || ! currentGameId.equals(row[0])) {
			currentGameId = row[0];
			currentGame = gameData.getGame(row[0]);
		}
		
		Play play = new Play(currentGame, row);
		plays.add(play);
		currentGame.addPlay(play);
	}

	public PlayList getPlays() {
		return plays;
	}
	
	public int getPlayCount() {
		return plays.size();
	}
}
