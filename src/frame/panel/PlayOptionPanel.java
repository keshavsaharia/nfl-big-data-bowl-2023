package frame.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayOptionPanel extends JPanel implements ActionListener {
	

	// Reference to parent control panel
	private PlayControlPanel control;
	
	public PlayOptionPanel(PlayControlPanel control) {
		this.control = control;
		
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setBackground(PlayPanel.darkGrassColor);
		add(createButton("matchup", "See matchups"));
		add(createButton("territory", "Show control"));
		add(createButton("pass", "Show pass"));
		add(createButton("speed", "Show speed"));
		add(createButton("route", "Show routes"));
		add(createButton("edit", "Show routes"));
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
		
		if (button.equals("route")) {
			
		}
	}
}
