/**
 *Kailash Subramanian, Gallatin
 */
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Recursively generates a map using binary space partitioning
 */ 
public class PartitionGenerator {
	int map[][];
	Partition fullPart;
	BSPTreeNode<Partition> root;
	private boolean experimental;
	
	public static final int WALL_W = 4, WALL_N = 2, WALL_E = 1, WALL_S = 3;//WALL_E = 2, WALL_N = 1, WALL_W = 3, WALL_S = 4;
	public static final int MAX_WALL = 4, MIN_WALL = 1;
	/**
	 * Constructs a partition generator.
	 * @param row the rows in partition
	 * @param col the columns in partition
	 * @param experimental if walls should be enabled
	 */
	public PartitionGenerator(int row, int col, boolean experimental) {
		map = new int[row][col];
		this.experimental = experimental;
		fullPart = new Partition(0, 0, row-1, col-1);
		root = new BSPTreeNode<>(fullPart);
		
	}
	
	/**
	 * return 2d array of the partitioned map 
	 * @return 2d array of the partitioned map
	 */
	public int[][] generate() {
		return partition(root);
	}
	
	/**
	 * return 2d array of the map
	 * @param parNode a parent node
	 * @return 2d array of the map
	 */
	public int[][] partition(BSPTreeNode<Partition> parNode) {
		if (experimental) return map;
		
		Partition par = parNode.getData();
		
		if (par.width() < 10 || par.height() < 10) {
			return map;
		}
		
		Partition a, b;
		
		boolean dir = (Math.random() > 0.5);
		if (dir) { //vertical
			int verx = rand(8, par.height());
			
			a = new Partition(par.getX0(), par.getY0(), par.getX0() + verx, par.getY1());
			b = new Partition(par.getX0() + verx, par.getY0(), par.getX1(), par.getY1());
//			
//			partition(a);
//			partition(b);
//			
//			//System.out.println("vertical? " + dir + " by " + verx);
//			//System.out.println("parent " + par);
//			//System.out.println(a + " and " + b);
			print();
//			//System.out.println(partTree);
			
		} else { //horizontal
			int hzy = rand(8, par.width());
			
			a = new Partition(par.getX0(), par.getY0(), par.getX1(), par.getY1() - hzy);
			b = new Partition(par.getX0() + hzy, par.getY0(), par.getX1(), par.getY1());
//			
//			partition(a);
//			partition(b);
			
//			//System.out.println("vertical? " + dir + " by " + hzy);
//			//System.out.println("parent " + par);
//			//System.out.println(a + " and " + b);
			//print();
//			//System.out.println(partTree);
			
		}
		
		parNode.setLeft(new BSPTreeNode<Partition>(a));
		parNode.setRight(new BSPTreeNode<Partition>(b));

		appendAll(a, b);
		print();
		
		partition(parNode.getLeft());
		partition(parNode.getRight());
		
		
		return map;
		
	}
	
	/**
	 * Prints the map
	 */
	private void print() {
		// TODO Auto-generated method stub
		for (int[] m : map) {
			for (int i : m) {
				//System.out.print (i + " " );
				switch (i) {
				case WALL_N: System.out.print("N"); break;
				case WALL_E: System.out.print("E"); break;
				case WALL_W: System.out.print("W");break;
				case WALL_S: System.out.print("S");break;
				default: System.out.print("~"); break;
				}
				System.out.print(" ");
			}
			//System.out.println();
		}
		//System.out.println();
	}
	
	/**
	 * Prints a given map
	 * @param map 2D array map
	 */
	public static void print(int[][] map) {
		for (int[] m : map) {
			for (int i : m) {
				//System.out.print (i + " " );
				switch (i) {
				case WALL_N: System.out.print("N"); break;
				case WALL_E: System.out.print("E"); break;
				case WALL_W: System.out.print("W");break;
				case WALL_S: System.out.print("S");break;
				default: System.out.print("~"); break;
				}
				System.out.print(" ");
			}
			//System.out.println();
		}
		//System.out.println();
	}

	/**
	 *  Appends parts to the map
	 * @param parts the parts of the partition
	 */
	private void appendAll(Partition... parts) {
		for (int j = 0; j < parts.length; j++) {
			Partition p = parts[j];
			//partTree.add(p);
			for (int i = p.getY1() - 3; i >= p.getY0(); i--) {
				//add wall E and W
				try {
					if (map[p.getX0()][i] == 0) map[p.getX0()][i] = WALL_N; //e n 
					if (map[p.getX1()][i] == 0) map[p.getX1()][i] = WALL_S; //w s 
				} catch (Exception e) {
					//suppress
				}
			}
			
			for (int i = p.getX0(); i < p.getX1() - 3; i++) {
				//add wall S and N
				try {
					if (map[i][p.getY0()] == 0) map[i][p.getY0()] = WALL_W; //s w 
					if (map[i][p.getY1()] == 0) map[i][p.getY1()] = WALL_E; //n e
				} catch (Exception e) {
					//suppress
				}
			}
		}
	}
	
	/**
	 * @param mn minimum
	 * @param mx maximum
	 * @return random value between mn and mx
	 */
	private int rand(int mn, int mx) {
		return mn + (int)(Math.random()*((mx - mn) + 1));
	}
	
	/**
	 * return the map
	 * @return the map
	 */
	public int[][] getMap() {
		return getMapDeepCopy();
	}

	/**
	 * return a deep copy of the map
	 * @return a deep copy of the map
	 */
	private int[][] getMapDeepCopy() {
		// TODO Auto-generated method stub
		int[][] map2 = new int[map.length][map.length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map2[i][j] = map[i][j];
			}
		}
		
		return map2;
	}
	
	/**
	 * return the root of the tree
	 * @return the root of the tree
	 */
	public BSPTreeNode<Partition> getRoot() {
		// TODO Auto-generated method stub
		return root;
	}
}
