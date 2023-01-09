package frame.panel;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayFrameStepPanel extends JPanel implements ActionListener {
	
	// Reference to parent control panel
	private PlayControlPanel control;
	
	private JButton previous;
	private JButton play;
	private JButton pause;
	private JButton next;
	
	public PlayFrameStepPanel(PlayControlPanel control) {
		this.control = control;
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setBackground(PlayPanel.darkGrassColor);
		add(previous = createButton("previous", "Back 0.1s"));
		add(play = createButton("play", "Play"));
		add(pause = createButton("pause", "Pause"));
		add(next = createButton("next", "Forward 0.1s"));
		
		setPlaying(true);
	}
	
	public void setPlaying(boolean playing) {
		if (playing) {
			if (play.isVisible()) {
				play.setVisible(false);
				pause.setVisible(true);
			}
		}
		else {
			if (pause.isVisible()) {
				pause.setVisible(false);
				play.setVisible(true);
			}
		}
	}
	
	private JButton createButton(String name, String tooltip) {
		JButton button = PlayControlPanel.getButton(name, tooltip);
		button.setActionCommand(name);
		button.addActionListener(this);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String button = e.getActionCommand();
		if (button == null) return;
		
		if (button.equals("previous")) {
			control.getFrame().pause();
			control.getFrame().changePlayFrame(-1);
		}
		else if (button.equals("next")) {
			control.getFrame().pause();
			control.getFrame().changePlayFrame(1);
		}
		else if (button.equals("play")) {
			control.getFrame().play();
		}
		else if (button.equals("pause")) {
			control.getFrame().pause();
		}
	}
}
