import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JLabel;

public class CollisionManager {
	
	public CollisionManager() {
		
	}
	
	/**
	 * Perform a collision detection using bsp tree 
	 * @param a
	 * @param ac 
	 * @param b
	 * @param bc 
	 * @param world
	 * @param partitionTree
	 * @return
	 */
	public static boolean isCollision(GameEntity a, Coordinate ac, GameEntity b, Coordinate bc, 
			Orientation direction, boolean world, PartitionGenerator pag) {
		
		if (!world) {
			return a.getHitbox().intersects(b.getHitbox());
		} else {
			//partitions deal with isoX and isoY only. cool thing is that isometric coordinates are pretty exact (eg not just integer values on 2d array)
			//find partition entity a is in
			double isoX = Math.max(ac.getIsoX(), 0);
			double isoY = Math.max(ac.getIsoY(), 0);
			BSPTreeNode<Partition> node = pag.getRoot();
			while (node.hasChildren()) {
				if (a.getHitbox().intersects(node.getLeft().getData().getRectangularPartition())) {
					node = node.getLeft();
				} else {
					node = node.getRight();
				}
			}
			
			//check against all walls in this partition for collision
			System.out.println((int)isoX + ", " + (int)isoY + " = " + pag.getMap()[(int)isoX][(int)isoY]);
			int wall = pag.getMap()[(int)isoX][(int)isoY];
			
			if (wall > 0) {
				if (wall == PartitionGenerator.WALL_N) {
					System.out.println("North wall, orientation " + direction);
					if (direction == Orientation.NORTH || direction == Orientation.NORTHEAST || direction == Orientation.EAST) {
						System.out.println("cant move " + direction);
						return true;
					}
				} else if (wall == PartitionGenerator.WALL_E) {
					if (direction == Orientation.SOUTHEAST || direction == Orientation.SOUTH || direction == Orientation.EAST || direction == Orientation.SOUTHEAST) {
						System.out.println("cant move " + direction);
						return true;
					}
				}
			}
			
//			
//			if (wall == PartitionGenerator.WALL_N) {
//				System.out.println("North wall, orientation " + direction);
//				if (direction == Orientation.NORTH || direction == Orientation.NORTHEAST || direction == Orientation.EAST) {
//					System.out.println("cant move " + direction);
//					return true;
//				} else {
//					
//				}
//			}
			
			return false;
		}
	}
	
	public static boolean walkableTo(Coordinate oldC, Orientation dir, PartitionGenerator pag) {
		int isoX = (int)Math.max(oldC.getIsoX(), 0);
		int isoY = (int)Math.max(oldC.getIsoY(), 0);
		
		switch (dir) {
		case EAST: isoX--; isoY++;
			break;
		case NORTH: isoX--; isoY--;
			break;
		case NORTHEAST: isoX--;
			break;
		case NORTHWEST: isoY--;
			break;
		case SOUTH: isoX++; isoY++;
			break;
		case SOUTHEAST: isoY++;
			break;
		case SOUTHWEST: isoX++;
			break;
		case WEST: isoX++; isoY--;
			break;
		default:
			break;
		}
		
		isoX = Math.max(isoX, 0); isoY = Math.max(isoY, 0);
		
		int wall = pag.getMap()[isoX][isoY];
		
		System.out.println("going to " + isoX + ", " + isoY + "; walkable " + wall);
		return (wall == 0);
		
	}
	
	public static boolean walkableHere(Coordinate c, PartitionGenerator pag) {
		int isoX = (int)Math.max(c.getIsoX(), 0);
		int isoY = (int)Math.max(c.getIsoY(), 0);
		
		int wall = pag.getMap()[isoX][isoY];
		System.out.println("at " + isoX + ", " + isoY + "; walkable " + wall);
		return (wall == 0);
	}

	/**
	 * If the player continues in their orientation from the coordinate, would they hit either an
	 * un-walkable tile or cross a tile border at which a wall is occupied? A tile occupied by a wall
	 * is considered walkable until they might pass the certain border.
	 * @param oldC
	 * @param orientation
	 * @param pag
	 * @return if the player should be able to move
	 */
	@Deprecated
	public static boolean isWalkable(Player pl, Coordinate oldC, Orientation orientation, PartitionGenerator pag) {
		// TODO Auto-generated method stub
		int isoX = (int)Math.max(oldC.getIsoX(), 0);
		int isoY = (int)Math.max(oldC.getIsoY(), 0);
		
		if (isoX > World.ROW || isoY > World.COL) return false;
		
		int tile = pag.getMap()[isoX][isoY];
		if (tile == 0) return true;
		else if (tile < 5) {
			//wall, so check borders.
			boolean walkable = true;
			
			HashSet<Tile> t = World.getTiles().get(World.cmap[isoX][isoY]);
			Tile wallTile = null;
			
			for (Tile v : t) if (v.isWall()) wallTile = v;
			
			Line2D ln = wallTile.getWallLineSegment();
			if (ln.intersects(pl.getImg().getBounds())) {
				return false;
			} else return true;
			
		} else {
			return false;
		}
	}
	
	public static boolean isWalkableBasic(Rectangle thisBounds, Coordinate oldC, PartitionGenerator pag) {
		int isoX = (int)Math.max(oldC.getIsoX(), 0);
		int isoY = (int)Math.max(oldC.getIsoY(), 0);
		
		if (isoX > World.ROW || isoY > World.COL) return false;
		
		for (int i = isoX; i < isoX + 1; i++) {
			for (int j = isoY; j < isoY + 1; j++) {
				if (i < World.ROW && j < World.COL && i > 0 && j > 0) {
					int tile = pag.getMap()[i][j];
					if (tile == 0) return true;
					else if (tile <= PartitionGenerator.MAX_WALL && tile >= PartitionGenerator.MIN_WALL) {
						//wall, so check borders.
						boolean walkable = true;
						
						HashSet<Tile> t = World.getTiles().get(World.cmap[i][j]);
						Tile wallTile = null;
						
						for (Tile v : t) if (v.isWall()) wallTile = v;
						
						Line2D ln = wallTile.getWallLineSegment();
						if (ln.intersects(thisBounds)) {
							return false;
						} else return true;
						
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return false;
		
	}
	
	/**
	 * This method does not check for walkable tiles.
	 * @param thisBounds
	 * @param oldC
	 * @param pag
	 * @return null if no Soldier hit. A Soldier if a soldier is hit.
	 */
	public static Soldier isSoldierHitBasic(Rectangle thisBounds, Coordinate oldC, ArrayList<Soldier> sol) {
		for (Soldier s : sol) {
			if (s.getImg().getBounds().intersects(thisBounds)) {
				return s;
			}
		}
		return null;
	}
}
