package nfl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Game;
import model.Play;
import model.Position;

public class NFLGame extends JPanel implements ActionListener, KeyListener {
	
	private static final int YARD = 8;
	private static final int PLAYER_WIDTH = 10;
	private static final int PLAYER_HEIGHT = 6;
	private static final int YARD_2 = YARD / 2;
	private static final int FOOT = YARD / 3;
	private static final int LINE_SIZE = 3;
	private static final int WIDTH = 120 * YARD;
	private static final int HEIGHT = (int) Math.round(53.2 * YARD);
	
	private static final int LEFT_ENDZONE = 10 * YARD;
	private static final int WIDTH_10YD = 10 * YARD;
	private static final int INNER_WIDTH_10YD = WIDTH_10YD - FOOT;
	private static final int INNER_HEIGHT = 53 * YARD;
	private static final int RIGHT_ENDZONE = 110 * YARD;

	// The play and frame being shown
	private Game game;
	private Play play;
	private int playIndex = 0;
	private int frame = 1;
	
	// Timer for animations
	private Timer timer;
	
	public NFLGame(Game game) {
		this.game = game;
		this.play = game.firstPlay();
		this.timer = new Timer(100, this);
	}
	
	public void nextPlay() {
		if (playIndex < game.getPlayCount()) {
			stop();
			playIndex++;
			frame = 1;
			this.play = game.getPlayIndex(playIndex);
			repaint();
		}
		
	}
	
	public void previousPlay() {
		if (playIndex > 0) {
			stop();
			playIndex--;
			frame = 1;
			this.play = game.getPlayIndex(playIndex);
			repaint();
		}
	}
	
	public void start() {
		timer.start();
	}
	
	public void restart() {
		frame = 1;
		start();
	}
	
	public boolean playing() {
		return timer.isRunning();
	}
	
	public void stop() {
		timer.stop();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.GREEN);
		for (int x = LEFT_ENDZONE ; x < RIGHT_ENDZONE ; x += WIDTH_10YD) {
			g.fillRect(x + FOOT, LINE_SIZE, INNER_WIDTH_10YD, INNER_HEIGHT);
		}
		
		drawYardline(g, play.getScrimmageYard(), Color.CYAN);
		drawYardline(g, play.getFirstDownYard(), Color.PINK);
		drawBall(g);
		drawPlayers(g);
	}
	
	private void drawBall(Graphics g) {
		g.setColor(Color.RED);
		Position ball = play.getBallPosition(frame);
		if (ball == null) return;
		
		g.fillOval(ball.getX(YARD) - 4, HEIGHT - ball.getY(YARD) - 4, 8, 8);
	}

	private void drawYardline(Graphics g, int yardline, Color color) {
		g.setColor(color);
		g.fillRect(LEFT_ENDZONE + yardline * YARD - LINE_SIZE / 2, LINE_SIZE, LINE_SIZE, INNER_HEIGHT);
	}
	
	private void drawPlayers(Graphics g) {
		for (String id : play.getPlayerIds()) {
			drawPlayer(g, id);
		}
	}
	
	private void drawPlayer(Graphics g, String id) {
		Position position = play.getPosition(id, frame);
		g.setColor(play.isOffense(id) ? Color.ORANGE : Color.BLUE);
		
		int playerX = position.getX(YARD);
		int playerY = HEIGHT - position.getY(YARD);
		
		drawRectangle(g, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, position.getOrientation());
		
		g.setColor(Color.BLACK);
		g.drawLine(playerX, playerY, position.getDirectionX(YARD), HEIGHT - position.getDirectionY(YARD));
	}
	
	private void drawRectangle(Graphics g, int x, int y, int width, int height, double angle) {
		g.fillPolygon(new int[] {
			x + rotatedX(- width / 2, - height / 2, angle),
			x + rotatedX(-width / 2, height / 2, angle),
			x + rotatedX(width / 2, height / 2, angle),
			x + rotatedX(width / 2, -height / 2, angle)
		},
		new int[] {
			y + rotatedY(-width / 2, -height / 2, angle),
			y + rotatedY(-width / 2, height / 2, angle),
			y + rotatedY(width / 2, height / 2, angle),
			y + rotatedY(width / 2, -height / 2, angle)
		}, 4);
	}
	
	// Used in rotation calculations.
	private static int rotatedX(int x, int y, double angle) {
		return (int) (x * Math.cos(angle) - y * Math.sin(angle));
	}

	// Used in rotation calculations.
	private static int rotatedY(int x, int y, double angle) {
		return (int) (x * Math.sin(angle) + y * Math.cos(angle));
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		frame++;
		if (frame > play.getFrames()) {
			timer.stop();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			stop();
			frame--;
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			stop();
			frame++;
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			if (timer.isRunning()) {
				timer.stop();
			}
			else {
				timer.start();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			nextPlay();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			previousPlay();
		}
	}
}
