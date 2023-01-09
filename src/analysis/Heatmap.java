package analysis;

import java.util.ArrayList;

import list.TrackingList;
import model.Tracking;

public class Heatmap {
	
	private int[][] value;
	private int originX, originY;
	private Tracking center;
	
	
	private TrackingList heating;
	private TrackingList cooling;
	
	public Heatmap(Tracking center) {
		this(center, 800, 800);
	}
	
	public Heatmap(Tracking center, int width, int height) {
		this.center = center;
		
		value = new int[width][height];
		originX = width / 2;
		originY = height / 2;
		
		heating = new TrackingList();
		cooling = new TrackingList();
	}
	
	public void addHeating(Tracking heatingSource) {
		heating.add(heatingSource);
	}
	
	public void addCooling(Tracking coolingSource) {
		cooling.add(coolingSource);
	}
	
	public void compute() {
		
	}
}
