package model.util;

import list.PositionList;
import list.TrackingList;
import model.Position;
import model.Tracking;

public class Range2D {

	private double xmin = Double.MAX_VALUE;
	private double xmax = Double.MIN_VALUE;
	private double ymin = Double.MAX_VALUE;
	private double ymax = Double.MIN_VALUE;
	
	public Range2D(PositionList positions) {
		for (Position position : positions)
			add(position);
	}
	
	public Range2D(TrackingList trackings) {
		for (Tracking tracking : trackings) {
			join(new Range2D(tracking.getPositions()));
		}
	}
	
	public void add(Position position) {
		if (position.getX() < xmin)
			xmin = position.getX();
		if (position.getX() > xmax)
			xmax = position.getX();
		if (position.getY() < ymin)
			ymin = position.getY();
		if (position.getY() > ymax)
			ymax = position.getY();
	}
	
	public void join(Range2D range) {
		if (range.xmin < xmin)
			xmin = range.xmin;
		if (range.xmax > xmax)
			xmax = range.xmax;
		if (range.ymin < ymin)
			ymin = range.ymin;
		if (range.ymax > ymax)
			ymax = range.ymax;
	}
}
