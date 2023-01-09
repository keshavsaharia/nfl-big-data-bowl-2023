package input;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import data.GameData;
import data.TeamData;
import model.Team;

public class TeamInput {
	private TeamData data;
	private Component parent;
	
	public static void main(String[] args) {
		Team team = new TeamInput().prompt();
		System.out.println(team);
	}
	
	public TeamInput() {
		this(new TeamData(), null);
	}
	
	public TeamInput(GameData data) {
		this(data.getTeamData(), null);
	}
	
	public TeamInput(TeamData data) {
		this(data, null);
	}
	
	public TeamInput(TeamData data, Component parent) {
		this.data = data;
		this.parent = parent;
	}
	
	public Team prompt() {
		ImageIcon icon = new ImageIcon("icon/nfl.svg");
		String[] teamNames = data.getTeamNames();
		
		Object result = JOptionPane.showInputDialog(
				parent, "Select NFL team", "Add team", JOptionPane.QUESTION_MESSAGE, 
				icon, teamNames, teamNames[0]);
		
		if (result == null)
			return null;
		
		// Find a matching team name and get the Team instance at the
		// corresponding index from the data source
		for (int i = 0 ; i < teamNames.length ; i++) {
			if (result == teamNames[i]) {
				return data.getTeamIndex(i);
			}
		}
		return null;
	}
}
