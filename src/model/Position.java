package model;

import data.CSV;

public class Position {
	
	int frame;
	
	// X and Y coordinates, and normalized to 120/53.3 range
	double x, y;
	double nx, ny;
	
	double speed;
	double acceleration;
	double distance;
	
	// Player orientation
	double orientation;
	// Angle of player motion
	double direction;
	
	private String event;
	
	public Position(String[] row) {
		frame = Integer.parseInt(row[3]);
		
		x = Double.parseDouble(row[8]);
		y = Double.parseDouble(row[9]);
		nx = x / 120.0;
		ny = 1.0 - y / 53.3;
		
		speed = Double.parseDouble(row[10]);
		acceleration = Double.parseDouble(row[11]);
		distance = Double.parseDouble(row[12]);
		orientation = Math.toRadians(CSV.parseDouble(row[13]));
		direction = Math.toRadians(CSV.parseDouble(row[14]));
		
		if (! row[15].equals("NA")) {
			event = row[15];
		}
	}
	
	public double getX() {
		return nx;
	}
	
	public int getX(double scale) {
		return (int) Math.round(x * scale);
	}
	
	public double getY() {
		return ny;
	}

	public int getY(double scale) {
		return (int) Math.round(y * scale);
	}
	
	public double getOrientation() {
		return this.orientation;
	}
	
	public int getDirectionX(int scale) {
		return (int) Math.round(x * scale + Math.sin(direction) * speed * scale / 2);
	}
	
	public int getDirectionY(int scale) {
		return (int) Math.round(y * scale + Math.cos(direction) * speed * scale / 2);
	}

	public int getFrame() {
		return frame;
	}
	
	public String getEvent() {
		return event;
	}
	
	public boolean isEvent(String event) {
		return this.event != null && this.event.equals(event);
	}

	
	public double getDirection() {
		return direction;
	}

	public double distanceTo(int x, int y, int width, int height) {
		double dx = nx - 1.0 * x / width;
		double dy = ny - 1.0 * y / height;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	/**
	 * Expected time (in seconds) for the player whose position this is, 
	 * to arrive at the given x, y coordinate, assuming no obstructions
	 * in the player's path.
	 * 
	 * @param x - the x position on the field (0 - 120)
	 * @param y - the y position on the field (0 - 53.3)
	 * @return expected time of arrival (in seconds)
	 */
	public double expectedTravelTime(double x, double y) {
		double dx = this.x - x;
		double dy = this.y - y;
		double angle = Math.atan2(dy, dx);
		// TODO: get player's top speed and acceleration
		
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
}
