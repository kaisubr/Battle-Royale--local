/**
 *Kailash Subramanian, Gallatin
 */

/**
 * Represents a binary-space partition tree
 * @param <T> typically a Partition
 */
public class BSPTree<T> {
	private BSPTreeNode<T> parent;
	
	/**
	 * Constructs tree with a parent node
	 * @param par the parent node
	 */
	public BSPTree(T par) {
		parent = new BSPTreeNode<>(null);
		parent.setData(par); 
	}
}
