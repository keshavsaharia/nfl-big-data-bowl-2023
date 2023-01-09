package frame.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameOptionPanel extends JPanel implements ActionListener {
	

	// Reference to parent control panel
	private PlayControlPanel control;
	
	public GameOptionPanel(PlayControlPanel control) {
		this.control = control;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(PlayPanel.darkGrassColor);
		add(createButton("football", "Track football"));
		add(createButton("quarter1", "First quarter"));
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
