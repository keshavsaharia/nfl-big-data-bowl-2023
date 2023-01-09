package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import list.PlayList;

public class Game {
	private String id;
	private int week;
	private int season;
	
	private String date;
	private String time;
	
	Team homeTeam;
	String homeTeamId;
	Team awayTeam;
	String awayTeamId;
	
	private HashMap<String, Play> playMap;
	private PlayList playList;
	private int[] playQuarter;
	private boolean overtime = false;
	
	public Game(String id) {
		this.id = id;
		this.playMap = new HashMap<String, Play>();
		this.playList = new PlayList();
	}
	
	public Game(String[] row) {
		this(row[0]);
		
		this.season = Integer.parseInt(row[1]);
		this.week = Integer.parseInt(row[2]);
		this.date = row[3];
		this.time = row[4];
		this.homeTeamId = row[5];
		this.awayTeamId = row[6];
	}
	
	public String getId() {
		return this.id;
	}
	
	public boolean hasId(String id) {
		return this.id.equals(id);
	}
	
	public String toString() {
		return season + " season, week " + week + " (" + date + " at " + time + "): " + homeTeam.getName() + " vs " + awayTeam.getName();
	}
	
	public void addPlay(Play play) {
		this.playMap.put(play.getId(), play);
		this.playList.add(play);
	}

	private void loadPlayQuarters() {
		if (playQuarter != null)
			return;
		
		// Space for possible overtime (5th)
		playQuarter = new int[6];
		playQuarter[0] = 0;
		
		int current = 1;
		for (int i = 1 ; i < playList.size() ; i++) {
			if (playList.get(i).getQuarter() != current) {
				playQuarter[current] = i;
				current++;
			}
		}
		
		if (current == 5) {
			overtime = true;
		}
		playQuarter[current] = playList.size();
	}

	public Play getPlay(String id) {
		return this.playMap.get(id);
	}
	
	public int getIndex(Play play) {
		return this.playList.indexOf(play);
	}
	
	public Play randomPlay() {
		return this.playList.get((int) Math.floor(Math.random() * this.playList.size()));
	}

	public int getWeek() {
		return week;
	}

	public Play firstPlay() {
		return this.playList.get(0);
	}
	
	public PlayList getPlays() {
		return this.playList;
	}
	
	public List<Play> getQuarter(int quarter) {
		loadPlayQuarters();
		return this.playList.subList(playQuarter[quarter], playQuarter[quarter + 1]);
	}
	
	public int[] getQuarters() {
		if (overtime)
			return new int[] { 1, 2, 3, 4, 5 };
		return new int[] { 1, 2, 3, 4 };
	}
	
	public boolean wentToOvertime() {
		return overtime;
	}

	public int playCount() {
		return playList.size();
	}

	public Play getPlayIndex(int index) {
		return playList.get(index);
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append(homeTeam.getName()).append(" vs ").append(awayTeam.getName());
		return sb.toString();
	}
	
	public String getMatchup() {
		return homeTeam.getName() + " vs " + awayTeam.getName();
	}

	public static String getQuarterName(int quarter) {
		switch (quarter) {
		case 1: return "Q1";
		case 2: return "Q2";
		case 3: return "Q3";
		case 4: return "Q4";
		case 5: return "OT";
		}
		return "";
	}

	public boolean hasTracking() {
		for (Play p : playList) {
			if (! p.hasTracking())
				return false;
		}
		return true;
	}

	public int getPlayCount() {
		return playList.size();
	}

	public String getHomeTeamId() {
		return homeTeamId;
	}

	public void setTeams(Map<String, Team> team) {
		homeTeam = team.get(homeTeamId);
		awayTeam = team.get(awayTeamId);
	}
	
	public Team getTeam(String id) {
		if (id == null)
			return null;
		if (id.equals(homeTeamId))
			return homeTeam;
		if (id.equals(awayTeamId))
			return awayTeam;
		return null;
	}
	
	public Team getHomeTeam() {
		return homeTeam;
	}
	
	public Team getAwayTeam() {
		return awayTeam;
	} 
	
	public Team getOpponent(Team team) {
		return hasHomeTeam(team) ? awayTeam : homeTeam;
	}

	public boolean hasHomeTeam(Team team) {
		return homeTeam.getId().equals(team.getId());
	}
	
	public boolean hasAwayTeam(Team team) {
		return awayTeam.getId().equals(team.getId());
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		if (time == null)
			return "";
		return time.substring(0, time.length() - 3);
	}
}
