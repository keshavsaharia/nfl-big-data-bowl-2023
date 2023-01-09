package model;

import list.TrackingList;

/**
 * @class 	Control
 * @author 	keshav
 *
 */
public class Control {

	private Play play;
	
	private int width;
	private int height;
	private int frame;
	
	private boolean[][][] offense;
	private boolean[][][] defense;
	
	public Control(Play play, int width, int height) {
		this.play = play;
		this.width = width;
		this.height = height;
		this.frame = play.getFrames();
		
		offense = new boolean[frame][width][height];
		defense = new boolean[frame][width][height];
		
		compute();
	}

	private void compute() {
		TrackingList players = play.getTracking();
		
		for (int f = 0 ; f < frame ; f++) {
			for (int x = 0 ; x < width ; x++) {
				for (int y = 0 ; y < height ; y++) {
					
					Tracking player = closest(players, f, x, y);
					
					if (player.isOnTeam(play.offense))
						offense[f][x][y] = true;
					else
						defense[f][x][y] = true;
				}
			}
		}
		
	}
	
	private Tracking closest(TrackingList players, int frame, int x, int y) {
		Tracking closest = players.random();
		double distance = closest.getPositionIndex(frame).distanceTo(x, y, width, height);
		 
		for (Tracking player : players) {
			if (player == closest) continue;
			
			double compare = player.getPositionIndex(frame).distanceTo(x, y, width, height);
			if (compare < distance) {
				closest = player;
				distance = compare;
			}
		}
		
		return closest;
	}

	public boolean isOffense(int frame, int x, int y) {
		return offense[frame - 1][x][y];
	}
	
	public boolean isDefense(int frame, int x, int y) {
		return defense[frame - 1][x][y];
	}
}
