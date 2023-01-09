package list;

import java.util.ArrayList;
import java.util.Comparator;

import model.Position;

public class PositionList extends ArrayList<Position> {

	public PositionList() {
		super();
	}
	
	public PositionList(PositionList copy) {
		super();
		for (Position p : copy)
			this.add(p);
	}
	
	public void sortByFrame() {
		this.sort(frameSort);
	}
	
	private static final Comparator<Position> frameSort = new Comparator<Position>() {
		public int compare(Position a, Position b) {
			return a.getFrame() - b.getFrame();
		}
	};
}
