package list;

import java.util.ArrayList;

import model.Game;
import model.Team;

public class GameList extends ArrayList<Game> {

	private GameList filteredFrom = null;
	
	public GameList() {
		super();
	}
	
	public GameList(GameList copy) {
		super();
		for (Game g : copy)
			this.add(g);
	}
	
	public GameList filterByTeam(Team team) {
		// If this is a filtered list, new filter queries are
		// performed on the source list that was used to filter by team
		if (filteredFrom != null)
			return filteredFrom.filterByTeam(team);
		
		// Create a new list with a reference to this list
		GameList filtered = new GameList();
		filtered.filteredFrom = this;
		
		// Filter games into the list if the team was either home or away
		for (Game game : this)
			if (game.hasHomeTeam(team) || game.hasAwayTeam(team))
				filtered.add(game);
		
		return filtered;
	}
	
	public GameList removeFilter() {
		return filteredFrom;
	}
}
