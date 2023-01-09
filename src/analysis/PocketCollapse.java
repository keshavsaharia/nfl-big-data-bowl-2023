package analysis;

import data.GameData;
import list.PlayList;
import model.Game;
import model.Play;
import model.Player;
import model.Pocket;

public class PocketCollapse {
	
	public static void main(String[] args) {
		GameData data = new GameData();
		data.loadTrackingData();
		
		for (Player quarterback : data.getPlayerPosition("QB").sortByPlayCount().takeFirst(5)) {
			System.out.println(quarterback.getName() + " - " + quarterback.playCount());
			
			for (Play play : quarterback.getPlays()) {
				
				Pocket pocket = play.getPocket();
				
			}
		}
		
//		Game game = data.randomGame();
//		data.loadTrackingData(game);
//		
//		System.out.println(game);
//		System.out.println("Picking random play");
//		
//		Play play = game.randomPlay();
//		Pocket pocket = new Pocket(play);
	}
	
}
