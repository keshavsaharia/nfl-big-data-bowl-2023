package frame;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.Timer;

import data.GameData;
import frame.panel.PlayControlPanel;
import frame.panel.PlayPanel;
import model.Game;
import model.Play;

public class PlayFrame extends JFrame implements ActionListener, KeyListener {

	public static void main(String[] args) {
		GameData data = new GameData();
		Game game = data.randomGame();
		data.loadTrackingData(game);
		new PlayFrame(game.randomPlay());
	}
	
	// Current play being viewed
	private Play play;
	
	// Animation timing
	private Timer timer;
	private static final int DEFAULT_SPEED = 100;
	private static final int WINDOW_BAR_HEIGHT = 22;
	
	// References to inner components
	private PlayPanel playPanel;
	private PlayControlPanel playControl;
	
	public PlayFrame(Play play) {
		super(play.getName());
		this.play = play;
		this.timer = new Timer(DEFAULT_SPEED, this);
		
		playPanel = new PlayPanel(play);
		playControl = new PlayControlPanel(this, playPanel);
		
		addMouseListener(playPanel);
		addMouseMotionListener(playPanel);
		
		addKeyListener(this);
		
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(playPanel);
		pane.add(playControl);
		
		setSize(playPanel.getWidth(), WINDOW_BAR_HEIGHT + playPanel.getHeight() + playControl.getHeight());
		setVisible(true);
		requestFocus();
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public void play() {
		if (! timer.isRunning()) {
			timer.start();
			playControl.setPlaying(true);
		}
	}
	
	public boolean isPlaying() {
		return timer.isRunning();
	}
	
	public void pause() {
		if (timer.isRunning()) {
			timer.stop();
			playControl.setPlaying(false);
		}
	}
	
	public void togglePlaying() {
		if (isPlaying())
			pause();
		else
			play();
	}
	
	public void changePlayFrame(int change) {
		play.addFrame(change);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			if (play.nextFrame()) {
				playPanel.repaint();
			}
			else {
				pause();
//				play.restartFrame();
//				play = play.getGame().randomPlay();
//				playPanel.setPlay(play);
			}
		}
	}
	
	

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("typed " + e.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			togglePlaying();
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			pause();
			if (play.previousFrame())
				playPanel.repaint();
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			pause();
			if (play.nextFrame())
				playPanel.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
