import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Pathfinder {
	
	private static final int[][] MVTS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };//{{0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}};
	
	private int[][] map;
	private int start, end; //indicators, like the number "-2"
	private final int VISITED = PartitionGenerator.MAX_WALL+1, NOT_VISITED = PartitionGenerator.MAX_WALL+2;
	private SimpleCoordinate startc, endc;
	
	private LinkedList<SimpleCoordinate> path;
	
	/**
	 * Determines path from starting character to ending character in map.
	 * Note that walls by default are indicated by PartitionGenerator.MAX_WALL and MIN_WALL.
	 * @param map the 2d world
	 * @param start the character indicating the start
	 * @param end the character indicating the end
	 */
	public Pathfinder(int[][] map, int start, int end) {
		this.map = map;
		this.start = start;
		this.end = end;
		
		int strx = 0, stry = 0, endx = 0, endy = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == start) {
					strx = i; stry = j;
				} else if (map[i][j] == end) {
					endx = i; endy = j;
				} else if (!isWall(i, j)) {
					setVisited(i, j, false);
				}
			}
		}
		
		startc = new SimpleCoordinate(strx, stry);
		endc = new SimpleCoordinate(endx, endy);
	}
	

	public Pathfinder(int[][] map, SimpleCoordinate start, SimpleCoordinate end) {
		this.map = map;
		this.startc = start;
		this.endc = end;
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (!isWall(i, j)) {
					setVisited(i, j, false);
				}
			}
		}
	}
	
	public List<SimpleCoordinate> solve() {
		return bfsSolve(map); //breadth first search
	}
	
	@Deprecated
	private List<SimpleCoordinate> rSolve(int[][] map) {
		List<SimpleCoordinate> path = new ArrayList<>();
		if (explore(map, startc.getX(), startc.getY(), path)) return path;
		return Collections.emptyList();
	}

	private List<SimpleCoordinate> bfsSolve(int[][] map) {
		path = new LinkedList<>();
		path.add(startc);
		
		boolean skip = false;
		while (path.size() > 0) {
			SimpleCoordinate c = path.remove();
			
			if (!isValid(c.getX(), c.getY()) || isVisited(c.getX(), c.getY())) {
				//System.out.println(80 + ", " + isValid(c.getX(), c.getY()) + " " + isVisited(c.getX(), c.getY()));
				continue;//skip = true;
			}
			
			if (isWall(c.getX(), c.getY())) {
				setVisited(c.getX(), c.getY(), true);
				//System.out.println(86);
				continue;//skip = true;
			}
			
			if (isReached(c.getX(), c.getY())) {
				System.out.println(91);
				return reverse(backtrack(c));
			} 

			if (true) {
				for (int[] mvt : MVTS) {
					//System.out.println(97);
					SimpleCoordinate nxtC = new SimpleCoordinate(c.getX() + mvt[0], c.getY() + mvt[1], c);
					path.add(nxtC);
					setVisited(c.getX(), c.getY(), true);
					//print();
				}
			}
		}
		
		//if (explore(map, startc.getX(), startc.getY(), path)) return path;
		return Collections.emptyList();
	}
	
	/**
	 * Backtracks & returns a list of coordinates from t to the root (start) 
	 * @param t the child coordinate 
	 * @return list backtracking node to root
	 */
	private List<SimpleCoordinate> backtrack(SimpleCoordinate t) {
		List<SimpleCoordinate> path = new ArrayList<>();
		SimpleCoordinate nxt = t;

		while (nxt != null) {
			path.add(nxt);
			nxt = nxt.getFrom();
		}
		return path;
	}
	
	private List<SimpleCoordinate> reverse(List<SimpleCoordinate> backtrackedVersion) {
		Collections.reverse(backtrackedVersion);
		return backtrackedVersion;
		
	}
	
	private boolean explore(int[][] map, int x, int y, List<SimpleCoordinate> path) {
		// TODO Auto-generated method stub
		//print();
		
		if (!isValid(x, y) || isVisited(x, y) || isWall(x, y)) {
			//System.out.println(isValid(x,y) + " " /*i+ sVisited(x,y) + " " + isWall(x,y)*/);
			
			return false;
		}
		path.add(new SimpleCoordinate(x, y));
		setVisited(x, y, true);

		if (isReached(x, y)) return true;
		
		for (int[] mvt : MVTS) {
			SimpleCoordinate c = stepMovement(x, y, mvt);
			if (explore(map, c.getX(), c.getY(), path)) return true;
		}
		
		path.remove(path.size() -1 );
		return false;
	}

	private SimpleCoordinate stepMovement(int r, int c, int[] mvt) {
		return new SimpleCoordinate(r + mvt[0], c + mvt[1]);
	}
	
	private boolean isWall(int x, int y) {
		return map[x][y] <= PartitionGenerator.MAX_WALL && map[x][y] >= PartitionGenerator.MIN_WALL;
	}
	
	private boolean isVisited(int x, int y) {
		return map[x][y] == VISITED;
	}
	
	private void setVisited(int x, int y, boolean v) {
		map[x][y] = v? VISITED : NOT_VISITED;
	}
	
	private boolean isReached(int x, int y) {
		return (x == endc.getX() && y == endc.getY());
	}
	
	/**
	 * In bounds and not a wall means it is valid
	 * @param x
	 * @param y
	 * @return if valid location
	 */
	private boolean isValid(int x, int y) {
		try {
			int i = map[x][y];
			return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
		//return ((x < map.length && y < map.length && x >= 0 && y >= 0) && (!isWall(x, y)));
	}
	
	private void print() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
