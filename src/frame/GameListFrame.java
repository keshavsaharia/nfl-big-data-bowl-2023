package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import data.GameData;
import input.TeamInput;
import list.GameList;
import model.Game;
import model.Team;

public class GameListFrame extends JFrame implements TableModel, ListSelectionListener, ActionListener {

	
	
	// UI references for table viewer
	private JTable table;
	private JScrollPane container;
	private JMenu gameMenu;
	
	private static final String[] columns = { "week", "date", "time", "name", "plays" };
	private static final int[] widths = { 40, 120, 100, 500, 60 };
	
	private static int selectedRow = -1;
	
	// Frame data
	private GameList games;
	
	/**
	 * Initialize with the given list of games.
	 * @param games
	 */
	public GameListFrame(GameList games) {
		super(games.size() + " games");
		this.games = games;
		
		// Initialize the table and add to frame in scrolling container 
		initializeTable();
		initializeTableStyle();
		initializeMenu();
		int width = initializeColumnModel();		
		
		// Create the JFrame and show
		add(container);
		setSize(width, 600);
		setVisible(true);
	}
	
	private void initializeMenu() {
		JMenuBar bar = new JMenuBar();
		
		bar.add(new JMenu("open"));
		bar.add(new JMenu("find"));
		
		JMenu filter = new JMenu("filter");
		filter.add(createMenuItem("filter by team", "filter_team"));
		filter.add(createMenuItem("filter by weeks", "filter_week"));
		bar.add(filter);
		
		gameMenu = new JMenu("game");
		gameMenu.setVisible(false);
		gameMenu.add(createMenuItem("Open plays", "open_game"));
		bar.add(gameMenu);
		setJMenuBar(bar);
	}
	
	private JMenuItem createMenuItem(String name, String action) {
		JMenuItem item = new JMenuItem(name);
		item.setActionCommand(action);
		item.addActionListener(this);
		return item;
	}

	private void initializeTable() {
		table = new JTable(this);
		container = new JScrollPane(table);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
	}

	private void initializeTableStyle() {
		table.setBackground(new Color(240, 240, 240));
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		
		table.setFont(new Font("Open Sans", 0, 20));
		table.setRowHeight(30);
	}

	private int initializeColumnModel() {
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.setColumnMargin(5);
		
		int totalWidth = 0;
		for (int column = 0 ; column < widths.length ; column++) {
			totalWidth += widths[column];
			columnModel.getColumn(column).setPreferredWidth(widths[column]);
		}
		return totalWidth;
	}

	@Override
	public int getRowCount() {
		return games.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int index) {
		return columns[index];
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 4: return Integer.class;
		}
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int index, int column) {
		Game game = games.get(index);
		
		switch (column) {
		case 0: return game.getWeek();
		case 1: return game.getDate();
		case 2: return game.getTime();
		case 3: return game.getMatchup();
		case 4: return game.playCount();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		
		if (e.getFirstIndex() != selectedRow)
			selectedRow = e.getFirstIndex();
		else if (e.getLastIndex() != selectedRow)
			selectedRow = e.getLastIndex();
		System.out.println("e: " + selectedRow);
		gameMenu.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == null) return;
		
		if (command.equals("filter_team")) {
			Team team = new TeamInput().prompt();
			games = games.filterByTeam(team);
			System.out.println(games.size());
		}
		else if (command.equals("open_game") && selectedRow >= 0) {
			new PlayListFrame(games.get(selectedRow));
		}
	}
	
}
