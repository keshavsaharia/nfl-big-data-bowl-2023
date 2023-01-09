package frame.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import frame.PlayFrame;

public class PlayControlPanel extends JPanel {
	
	public static void main(String[] args) {
		JFrame test = new JFrame("test control");
		test.setSize(1000, 100);
		test.add(new PlayControlPanel(null, null));
		test.setVisible(true);
	}
	
	// UI parameters
	public static final int BUTTON_SIZE = 50;
	public static final int INSET = 5;
	
	// Component references
	private PlayFrame frame;
	private PlayPanel play;
	
	private GameOptionPanel gameOption;
	private PlayFrameStepPanel frameStep;
	private PlayOptionPanel playOption;
	
	public PlayControlPanel(PlayFrame frame, PlayPanel play) {
		this.frame = frame;
		this.play = play;
		
		// Left, center, and right columns
		setLayout(new GridLayout(1, 3));
		add(gameOption = new GameOptionPanel(this));
		add(frameStep = new PlayFrameStepPanel(this));
		add(playOption = new PlayOptionPanel(this));
		setBackground(PlayPanel.darkGrassColor);
	}
	
	public PlayFrame getFrame() {
		return frame;
	}
	
	public void setPlaying(boolean playing) {
		frameStep.setPlaying(playing);
	}
	
	public int getHeight() {
		return 50;
	}
	
	public static JButton getButton(String name) {
		return getButton(name, null);
	}
	
	public static JButton getButton(String name, String tooltip) {
		JButton button = new JButton(getIcon(name));
		button.setMargin(new Insets(INSET, INSET, INSET, INSET));
		button.setBorder(null);
		
		if (tooltip != null)
			button.setToolTipText(tooltip);
		
		return button;
	}
	
	public static ImageIcon getIcon(String name) {
		try {
			BufferedImage image = ImageIO.read(new File("icon/" + name + ".png"));
			int imageSize = BUTTON_SIZE - INSET * 2;
			return new ImageIcon(image.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH));
		}
		catch (IOException e) {
			System.err.println("Icon " + name + " not found");
			return null;
		}
	}

	
}