/**
 *Kailash Subramanian, Gallatin
 */

/**
 * A node in a binary-search partition tree.
 * @param <T> typically a Partition
 */
public class BSPTreeNode<T> {
	private BSPTreeNode<T> parent;
	private BSPTreeNode<T> left, right;
	private T data;
	
	/** 
	 * Constructs node with data
	 * @param data the data
	 */
	public BSPTreeNode(T data) {
		this.data = data;
	}
	
	/**
	 * Constructs node 
	 * @param par the data
	 */
	public void setData(T par) {
		data = par;
	}
	
	/**
	 * Sets left child
	 * @param left the left child
	 */
	public void setLeft(BSPTreeNode<T> left) {
		this.left = left;
	}
	
	/**
	 * Sets right child
	 * @param right child
	 */
	public void setRight(BSPTreeNode<T> right) {
		this.right = right;
	}
	
	/**
	 * Returns data
	 * @return data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Returns left child
	 * @return left child
	 */
	public BSPTreeNode<T> getLeft() {
		return left;
	}
	
	/**
	 * Returns right child
	 * @return right child
	 */
	public BSPTreeNode<T> getRight() {
		return right;
	}

	/**
	 * Chkecs if node has children
	 * @return if has children
	 */
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return (left != null || right  != null);
	}
}
