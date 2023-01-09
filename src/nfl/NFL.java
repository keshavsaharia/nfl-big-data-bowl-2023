package nfl;

import javax.swing.JFrame;

import data.GameData;
import model.Game;

public class NFL {

	public static void main(String[] args) {
		GameData data = new GameData();
		Game random = data.randomGame();
		data.loadTrackingData(random);
		
		NFLGame gameView = new NFLGame(random);
		JFrame viewer = new JFrame(random.getName());
		viewer.addKeyListener(gameView);
		viewer.add(gameView);
		viewer.setSize(gameView.getWidth(), gameView.getHeight() + 25);
		viewer.setResizable(false);
		viewer.setVisible(true);
	}

}
