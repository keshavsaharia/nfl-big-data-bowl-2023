package frame;

import data.GameData;

public class Runner {
	
	public static GameData data;
	
	public static void main(String[] args) {
		data = new GameData();
		new GameListFrame(data.getGames());
	}
}
