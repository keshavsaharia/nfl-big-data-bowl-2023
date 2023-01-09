package data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Game;
import model.Team;

public class TeamData extends CSV {

	private List<Team> teams;
	private Map<String, Team> team;
	
	private String[] teamNames;
	
	public TeamData() {
		this(null);
	}
	
	public TeamData(GameData gameData) {
		super("teams");
		
		teams = new ArrayList<Team>();
		team = new HashMap<String, Team>();
		read();
		
		// Sort into alphabetical order and generate name array
		teams.sort(teamNameComparator);
		teamNames = new String[teams.size()];
		for (int i = 0 ; i < teams.size() ; i++)
			teamNames[i] = teams.get(i).getName();
		
		// Associate games with teams if reference provided
		if (gameData != null)
			for (Game game : gameData.getGames()) {
				game.setTeams(team);
				game.getHomeTeam().addGame(game);
			}
	}

	public String[] getTeamNames() {
		return teamNames;
	}
	
	@Override
	protected void process(String[] row) {
		Team team = new Team(row);
		
		this.teams.add(team);
		this.team.put(team.getId(), team);
	}
	
	private static final Comparator<Team> teamNameComparator = new Comparator<Team>() {
		public int compare(Team a, Team b) {
			return a.getName().compareTo(b.getName());
		}	
	};

	public Team getTeamIndex(int index) {
		return teams.get(index);
	}
}
