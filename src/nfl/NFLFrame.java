package nfl;

import javax.swing.JFrame;

import model.Game;
import model.Play;

public class NFLFrame extends JFrame {
	
	public NFLFrame(Game showGame) {
		super("NFL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		NFLPlay play = new NFLPlay(showGame.firstPlay());
		for (Play next : showGame.getPlays()) {
			play.addNext(next);
		}
		
		this.add(play);
		this.setSize(play.getWidth(), play.getHeight() + 25);
		this.setResizable(false);
		setVisible(true);
		
		
		
		play.start();
	}
	
	
}
