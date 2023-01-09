package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import data.GameData;
import input.TeamInput;
import list.GameList;
import list.PlayList;
import model.Game;
import model.Play;
import model.Team;

public class PlayListFrame extends JFrame implements TableModel, ListSelectionListener, ActionListener {

	public static void main(String[] args) {
		Game game = new GameData().randomGame();
		new PlayListFrame(game);
	}
	
	// UI references for table viewer
	private JTable table;
	private JScrollPane container;
	private JMenu playMenu;
	
	private static final String[] columns = { "quarter", "down", "clock", "offense", "result" };
	private static final int[] widths = { 60, 40, 100, 150, 100 };
	
	private static int selectedRow = -1;
	
	// Frame data
	private Game game;
	private PlayList plays;
	
	/**
	 * Initialize with the given list of games.
	 * @param games
	 */
	public PlayListFrame(Game game) {
		super(game.getName());
		this.game = game;
		this.plays = game.getPlays();
		
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
		
		JMenu filter = new JMenu("filter");
		filter.add(createMenuItem("by quarter", "filter_quarter"));
		filter.add(createMenuItem("by down", "filter_down"));
		bar.add(filter);
		
		playMenu = new JMenu("play");
		playMenu.setVisible(false);
		playMenu.add(createMenuItem("View", "view"));
		playMenu.add(createMenuItem("Players", "view_players"));
		bar.add(playMenu);
		
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
		
		table.setSelectionBackground(new Color(220, 220, 220));
		table.setSelectionForeground(new Color(40, 40, 40));
		
		table.setFont(new Font("Open Sans", 0, 16));
		
		table.setRowHeight(24);
	}

	private int initializeColumnModel() {
		TableColumnModel columnModel = table.getColumnModel();
//		columnModel.setColumnMargin(4);
		
		int totalWidth = 0;
		for (int c = 0 ; c < widths.length ; c++) {
			TableColumn column = columnModel.getColumn(c);
			int width = widths[c];
			
			column.setPreferredWidth(width);
			DefaultTableCellRenderer center = new DefaultTableCellRenderer();
			center.setHorizontalAlignment( JLabel.CENTER );
			column.setCellRenderer(center);
			totalWidth += width;
		}
		return totalWidth;
	}

	@Override
	public int getRowCount() {
		return plays.size();
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
//		switch (column) {
//		case 4: return Integer.class;
//		}
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int index, int column) {
		Play play = plays.get(index);
		
		switch (column) {
		case 0: return play.getQuarter();
		case 1: return play.getDown();
		case 2: return play.getClockString();
		case 3: return play.getOffenseTeam().getShortName();
		case 4: return play.getResult();
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
		playMenu.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == null) return;
		
		if (command.equals("players")) {
			
		}
		else if (command.equals("view")) {
			Runner.data.loadTrackingData(game);
			new PlayFrame(plays.get(selectedRow));
		}
	}
	
}
