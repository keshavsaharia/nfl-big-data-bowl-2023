package script;

import java.util.Comparator;
import java.util.List;

import data.GameData;
import model.Game;
import model.Player;

public class PlayerRankings {

	public static void main(String[] args) {
		GameData data = new GameData();
		
		data.getPlayers().sort(new Comparator<Player>() {
			public int compare(Player a, Player b) {
				return b.getSacksAllowed() - a.getSacksAllowed();
			}
		});
		
		for (Player player : data.getPlayers()) {
			if (player.getSacksAllowed() > 0) {
				System.out.print(player.getName());
				System.out.print(": ");
				System.out.println(player.getSacksAllowed());
			}
			
		}
	}

}
