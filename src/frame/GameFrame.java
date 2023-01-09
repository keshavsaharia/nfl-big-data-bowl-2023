package frame;

import javax.swing.JFrame;

import model.Game;

public class GameFrame extends JFrame {

	public GameFrame(Game game) {
		super(game.getName());
		
		
		setVisible(true);
	}
	
}
