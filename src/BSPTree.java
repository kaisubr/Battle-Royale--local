
public class BSPTree<T> {
	private BSPTreeNode<T> parent;
	
	public BSPTree(T par) {
		parent = new BSPTreeNode<>(null);
		parent.setData(par);
	}
}
