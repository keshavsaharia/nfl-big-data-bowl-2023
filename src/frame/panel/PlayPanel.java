package frame.panel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import list.TrackingList;
import model.Control;
import model.Play;
import model.Position;
import model.Team;
import model.Tracking;

public class PlayPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	
	private static final int DEFAULT_WIDTH = 1600;
	
	// Distances in yards
	private static final double FIELD_WIDTH = 120.0;
	private static final double FIELD_HEIGHT = 53.3;
	private static final double FIELD_MARGIN = 5.0;
	private static final double LINE_THICK = 0.666;
	private static final double LINE_SIZE = 0.4;
	private static final double LINE_THIN = 0.25;
	private static final double LINE_TICK = 0.2;
	
	// Total yard calculations
	private static final double YARD_WIDTH = FIELD_WIDTH + FIELD_MARGIN * 2;
	private static final double YARD_HEIGHT = FIELD_HEIGHT + FIELD_MARGIN * 2;
	private static final double YARD_FONT_SIZE = 5.0;
	
	public static final Color grassColor = new Color(83, 123, 63);
	public static final Color shadeGrassColor = new Color(73, 113, 53);
	
	public static final Color endzoneColor = new Color(69, 100, 56);
	public static final Color darkGrassColor = new Color(53, 93, 33);
	public static final Color yardlineColor = new Color(245, 245, 245);
	public static final Color tickmarkColor = new Color(230, 230, 230);
	public static final Color footballColor = new Color(123, 64, 59);
	public static final Color scrimmageLineColor = Color.CYAN;
	public static final Color firstDownLineColor = Color.YELLOW;
	
	// Reference to current play
	private Play play;
	private Control control;
	
	// Flags for toggling UI drawing
	private boolean showRoute = false;
	private boolean showYardNumbers = true;
	
	// Viewport size
	private int width, fieldWidth;
	private int height, fieldHeight;
	private double scale;	// ratio of yards to pixels (i.e. scale * yards = pixels)
	
	// Frame index
	
	private double zoom = 1;
	private int centerX, centerY;
	
	public PlayPanel() {
		this(DEFAULT_WIDTH, null);
	}
	
	public PlayPanel(Play play) {
		this(DEFAULT_WIDTH, play);
	}
	
	public PlayPanel(int width) {
		this(width, null);
	}
	
	public PlayPanel(int width, Play play) {
		setWidth(width);
		setPlay(play);
	}
	
	public void setPlay(Play play) {
		this.play = play;
		this.control = play.getControl(fieldWidth, fieldHeight);
		repaint();
	}
	
	public void setWidth(int width) {
		this.scale = width / YARD_WIDTH;
		setSize(width, (int) Math.round(scale * YARD_HEIGHT));
		this.fieldWidth = yardPx(FIELD_WIDTH);
		this.fieldHeight = yardPx(FIELD_HEIGHT);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		super.setSize(width, height);
		setPreferredSize(new Dimension(width, height));
	}
	
	public void paint(Graphics g) {
		paintBackground(g);
		paintControl(g);
		paintPlay(g);
	}
	

	public void paintBackground(Graphics g) {
		paintBoundary(g);
		paintEndzones(g);
		paintYardLines(g);
		paintYardTickmarks(g);
		paintYardNumbers(g);
		paintScrimmageYards(g);
	}
	
	private void paintScrimmageYards(Graphics g) {
		int scrimmage = fieldX(play.getScrimmageYard() / FIELD_WIDTH);
		int firstDown = fieldX(play.getFirstDownYard() / FIELD_WIDTH);
		int lineY = fieldY(0);
		int lineWidth = yardPx(LINE_SIZE);
		int lineHeight = yardPx(FIELD_HEIGHT);
		
		g.setColor(scrimmageLineColor);
		g.fillRect(scrimmage, lineY, lineWidth, lineHeight);
		g.setColor(firstDownLineColor);
		g.fillRect(firstDown, lineY, lineWidth, lineHeight);
	}

	public void paintBoundary(Graphics g) {
		int lineThick = yardPx(LINE_THICK);
		int outside = fieldX(0, -LINE_THICK);
		
		int lineWidth = width - outside * 2;
		int lineHeight = yardPx(FIELD_HEIGHT + LINE_THICK * 2);

		// Green background
		g.setColor(grassColor);
		g.fillRect(0, 0, width, height);
		
		// White field boundary
		g.setColor(Color.WHITE);
		g.fillRect(outside, outside, lineWidth, lineThick);
		g.fillRect(outside, outside, lineThick, lineHeight);
		g.fillRect(fieldX(1), outside, lineThick, lineHeight);
		g.fillRect(outside, fieldY(1), lineWidth, lineThick);
	}
	
	private void paintEndzones(Graphics g) {
		int endzoneY = fieldY(0);
		int innerHeight = yardPx(FIELD_HEIGHT);
		int yard10 = yardPx(10);

		// Endzone rectangles
		g.setColor(endzoneColor);
		g.fillRect(fieldX(0), endzoneY, yard10, innerHeight);
		g.fillRect(fieldX(1, -10), endzoneY, yard10, innerHeight);
	}
	
	private void paintYardLines(Graphics g) {
		int margin = fieldY(0);
		int lineSize = yardPx(LINE_SIZE);
		int lineThin = yardPx(LINE_THIN);
		int innerHeight = yardPx(FIELD_HEIGHT);
		
		// 10 yard lines
		g.setColor(yardlineColor);
		for (int x = 10 ; x <= 110 ; x += 10) {
			g.fillRect(fieldX(x / FIELD_WIDTH) - lineSize / 2, margin, lineSize, innerHeight);
		}
		
		for (int x = 15 ; x <= 105 ; x += 10) {
			g.fillRect(fieldX(x / FIELD_WIDTH) - lineThin / 2, margin, lineThin, innerHeight);
		}
	}
	
	private void paintYardNumbers(Graphics g) {
		g.setFont(new Font("Arial", Font.BOLD, yardPx(YARD_FONT_SIZE)));
		g.setColor(new Color(255, 255, 255, 200));
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform original = g2d.getTransform();
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.PI, 0, 0);
		
		int offsetLeft = yardPx(3.2);
		int offsetRight = yardPx(0.5);
		int fontHeight = yardPx(YARD_FONT_SIZE);
		int upperY = fieldY(0.2);
		int lowerY = fieldY(0.9);
		int upperRotatedY = fontHeight - upperY;
		
		for (int x = 10 ; x <= 50 ; x += 10) {
			String digit = "" + (x / 10);
			int leftX = fieldX(x / FIELD_WIDTH, 10);
			int rightX = fieldX((110 - x) / FIELD_WIDTH);
			
			g2d.setTransform(original);
			g.drawString(digit, leftX - offsetLeft, lowerY);
			g.drawString("0", leftX + offsetRight, lowerY);
			
			if (x != 50) {
				g.drawString(digit, rightX - offsetLeft, lowerY);
				g.drawString("0", rightX + offsetRight, lowerY);
			}
			
			g2d.setTransform(transform);
			g.drawString(digit, -leftX - offsetLeft, upperRotatedY);
			g.drawString("0", -leftX + offsetRight, upperRotatedY);
			
			if (x != 50) {
				g.drawString(digit, -rightX - offsetLeft, upperRotatedY);
				g.drawString("0", -rightX + offsetRight, upperRotatedY);
			}
		}
		
		// Reset original transform
		g2d.setTransform(original);
	}

	private void paintYardTickmarks(Graphics g) {
		// 1 yard tick marks
		int lineThin = yardPx(LINE_TICK);
		int tickHeight = yardPx(2);
		int tickOffset = tickHeight / 2;

		int outerUpper = fieldY(0.03) - tickOffset;
		int innerUpper = fieldY(0.3) - tickOffset;
		int innerLower = fieldY(0.7) - tickOffset;
		int outerLower = fieldY(0.97) - tickOffset;
		
		g.setColor(tickmarkColor);
		for (int x = 11 ; x <= 109 ; x++) {
			if (x % 5 == 0) continue;
			int fx = fieldX(x / FIELD_WIDTH) - lineThin / 2;
			g.fillRect(fx, outerUpper, lineThin, tickHeight);
			g.fillRect(fx, innerUpper, lineThin, tickHeight);
			g.fillRect(fx, innerLower, lineThin, tickHeight);
			g.fillRect(fx, outerLower, lineThin, tickHeight);
		}
	}
	
	private void paintControl(Graphics g) {
		if (control == null) return;
		
		int margin = yardPx(FIELD_MARGIN);
		int frame = play.getFrame();
		Color offense = play.getOffenseTeam().getColorAlpha(play.getGame(), 0.5);
		Color defense = play.getDefenseTeam().getColorAlpha(play.getGame(), 0.5);
		
		for (int x = 0 ; x < fieldWidth ; x++) {
			for (int y = 0 ; y < fieldHeight ; y++) {
				if (control.isOffense(frame, x, y)) {
					g.setColor(offense);
					g.fillRect(x + margin, y + margin, 1, 1);
				}
				else if (control.isDefense(frame, x, y)) {
					g.setColor(defense);
					g.fillRect(x + margin, y + margin, 1, 1);
				}
			}
		}
	}
	
	private void paintPlay(Graphics g) {
		if (play == null) return;
		
		// Get offense/defense teams
		Team offenseTeam = play.getOffenseTeam();
		Team defenseTeam = play.getDefenseTeam();
		
		// Get offense/defense tracking
		TrackingList offense = play.getOffense();
		TrackingList defense = play.getDefense();
		
		if (showRoute) {
			paintTeamRoute(g, offenseTeam, offense);
			paintTeamRoute(g, defenseTeam, defense);
		}
		
		paintBall(g, play.getBallTracking());
		paintTeam(g, play.getOffenseTeam(), play.getOffense());
		paintTeam(g, play.getDefenseTeam(), play.getDefense());
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Open Sans", 0, 20));
		g.drawString(play.getDescription(), fieldX(0), fieldY(1) + 40);
	}
	
	

	private void paintBall(Graphics g, Tracking ball) {
		// If showing full route, needs to paint all positions
		Position position = ball.getPosition(play.getFrame());
		int x = fieldX(position.getX());
		int y = fieldY(position.getY());
		int w = yardPx(2);
		int lace = yardPx(0.01);
		
		g.setColor(footballColor);
		g.fillArc(x - w / 2, y - w / 4, w, w, 30, 120);
		g.fillArc(x - w / 2, y - w * 3 / 4, w, w, 210, 120);
		
		g.setColor(Color.WHITE);
		g.fillRect(x - w / 3, y - lace / 2, w * 2 / 3, lace);
		g.fillRect(x - w / 4, y - w / 8, lace, w / 4);
		g.fillRect(x, y - w / 8, lace, w / 4);
		g.fillRect(x + w / 4, y - w / 8, lace, w / 4);
	}
	
	

	private void paintTeam(Graphics g, Team team, TrackingList players) {
		for (Tracking player : players) {
			paintPlayer(g, team, player);
		}
	}

	private void paintPlayer(Graphics g, Team team, Tracking player) {
		// If showing full route, needs to paint all positions
		Position position = player.getPosition(play.getFrame());
		int x = fieldX(position.getX());
		int y = fieldY(position.getY());
		
		g.setColor(team.getColor(play.getGame()));
		drawRectangle(g, x, y, yardPx(1.6), yardPx(0.8), position.getOrientation());
		g.setColor(team.getHelmetColor());
		paintPlayerHelmet(g, x, y, 0.4, position.getOrientation());
	}
	
	private void paintTeamRoute(Graphics g, Team team, TrackingList players) {
		for (Tracking player : players) {
			paintPlayerRoute(g, team, player);
		}
	}
	
	private void paintPlayerRoute(Graphics g, Team team, Tracking player) {
		if (player.positionCount() <= 1)
			return;
		
		g.setColor(team.getColor().brighter());
		Position start = player.firstPosition();
		for (int i = 2 ; i <= player.positionCount() ; i++) {
			Position next = player.getPosition(i);
			g.drawLine(
				fieldX(start.getX()), fieldY(start.getY()), 
				fieldX(next.getX()), fieldY(next.getY()));
			
			start = next;
		}
	}
	
	private void paintPlayerHelmet(Graphics g, int x, int y, double radius, double angle) {
		int r = yardPx(radius);
		int d = r * 2;
		int dir = (int) Math.toDegrees(-angle + Math.PI / 4);
		while (dir < 0)
			dir += 360;
		
		g.fillOval(x - r, y - r, d, d);
		g.setColor(Color.WHITE);
		g.drawArc(x - r, y - r, d, d, dir, 90);
	}
	
	private void drawRectangle(Graphics g, double x, double y, double width, double height, double angle) {
		g.fillPolygon(new int[] {
			rotatedX(x, -width / 2, -height / 2, angle),
			rotatedX(x, -width / 2, height / 2, angle),
			rotatedX(x, width / 2, height / 2, angle),
			rotatedX(x, width / 2, -height / 2, angle)
		},
		new int[] {
			rotatedY(y, -width / 2, -height / 2, angle),
			rotatedY(y, -width / 2, height / 2, angle),
			rotatedY(y, width / 2, height / 2, angle),
			rotatedY(y, width / 2, -height / 2, angle)
		}, 4);
	}
	
	// Used in rotation calculations.
	private static int rotatedX(double origin, double x, double y, double angle) {
		return (int) Math.ceil(origin + x * Math.cos(angle) - y * Math.sin(angle));
	}

	// Used in rotation calculations.
	private static int rotatedY(double origin, double x, double y, double angle) {
		return (int) Math.ceil(origin + x * Math.sin(angle) + y * Math.cos(angle));
	}
	
	private int fieldX(double x) {
		return yardPx(FIELD_MARGIN + FIELD_WIDTH * x); 
	}
	
	private int fieldX(double x, double offset) {
		return yardPx(FIELD_MARGIN + FIELD_WIDTH * x + offset); 
	}
	
	private int fieldY(double y) {
		return yardPx(FIELD_MARGIN + FIELD_HEIGHT * y); 
	}
	
	private int fieldY(double y, double offset) {
		return yardPx(FIELD_MARGIN + FIELD_HEIGHT * y + offset); 
	}
	
	private int yardPx(double yards) {
		return (int) Math.ceil(yards * scale);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println("mouse: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key: " + e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("key: " + e.getKeyCode());
	}
}
