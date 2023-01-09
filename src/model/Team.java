package model;

import java.awt.Color;

import list.GameList;

public class Team {
	private String id;
	
	private GameList games;
	
	private String name;
	private String shortName;
	private String conference;
	private String division;
	
	private Color color;
	private Color helmetColor;
	private String colorName;
	private Color colorAlternate;
	
	public Team(String id) {
		this.id = id;
		games = new GameList();
	}
	
	public Team(String[] row) {
		this(row[0]);
		
		name = row[1];
		shortName = name.substring(name.lastIndexOf(' ') + 1);
		conference = row[2];
		division = row[3];
		color = parseColor(row[4]);
		helmetColor = color.brighter().brighter();
		
		colorName = row[5];
		colorAlternate = parseColor(row[6]);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}
	
	private static Color parseColor(String hex) {
		String value = hex.toLowerCase();
		int red = parseColorPart(value.substring(1, 3));
		int green = parseColorPart(value.substring(3, 5));
		int blue = parseColorPart(value.substring(5, 7));
		return new Color(red, green, blue);
	}

	private static int parseColorPart(String part) {
		int upper = part.charAt(0) - 48;
		int lower = part.charAt(1) - 48;
		if (upper > 9)
			upper -= 39;
		if (lower > 9)
			lower -= 39;
		return upper * 16 + lower;
	}

	public void addGame(Game game) {
		games.add(game);
	}
	
	public GameList getGames() {
		return games;
	}
	
	public GameList getHomeGames() {
		GameList homeGames = new GameList();
		for (Game game : games) {
			if (game.hasHomeTeam(this))
				homeGames.add(game);
		}
		return homeGames;
	}
	
	public GameList getAwayGames() {
		GameList awayGames = new GameList();
		for (Game game : games) {
			if (game.hasAwayTeam(this))
				awayGames.add(game);
		}
		return awayGames;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(" (").append(getId()).append(")");
		return sb.toString();
	}

	public Color getColor() {
		return color;
	}
	
	public Color getHelmetColor() {
		return helmetColor;
	}

	public Color getColor(Game game) {
		// If same color family, give primary color to home team
		if (game != null && game.getOpponent(this).hasColorName(colorName) && game.hasAwayTeam(this)) {
			return colorAlternate;
		}
		return getColor();
	}

	public boolean hasColorName(String colorName) {
		return this.colorName != null && this.colorName.equals(colorName);
	}

	public String getShortName() {
		return shortName;
	}
	
	public Color getColorAlpha(Game game, double alpha) {
		Color c = getColor(game);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.floor(alpha * 255));
	}
	
	public Color getColorAlpha(double alpha) {
		return getColorAlpha(null, alpha);
	}
}
