package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import list.PlayerList;
import model.Player;

public class PlayerData extends CSV {

	
	HashMap<String, Player> player;
	PlayerList players;
	
	Map<String, PlayerList> position;
	
	public PlayerData() {
		super("players");
		
		this.player = new HashMap<String, Player>();
		this.players = new PlayerList();
		
		// Position caching
		this.position = new HashMap<String, PlayerList>();
		
		this.read();
	}

	@Override
	protected void process(String[] row) {
		Player player = new Player(row);
		this.player.put(player.getId(), player);
		this.players.add(player);
		
		// Add to global lineup position map
		if (! position.containsKey(player.getPosition()))
			position.put(player.getPosition(), new PlayerList());
		position.get(player.getPosition()).add(player);
	}

	public Player getPlayer(String id) {
		return player.get(id);
	}

	public PlayerList getPlayers() {
		return this.players;
	}
	
	public Set<String> getPositions() {
		return this.position.keySet();
	}
	
	public PlayerList getPosition(String position) {
		// Empty list if not recognized
		if (! this.position.containsKey(position))
			return new PlayerList();
		
		return this.position.get(position);
	}

}
