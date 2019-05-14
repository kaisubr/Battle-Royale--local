
public class BSPTreeNode<T> {
	private BSPTreeNode<T> parent;
	private BSPTreeNode<T> left, right;
	private T data;
	
	public BSPTreeNode(T data) {
		this.data = data;
	}
	
	public void setData(T par) {
		data = par;
	}
	
	public void setLeft(BSPTreeNode<T> left) {
		this.left = left;
	}
	
	public void setRight(BSPTreeNode<T> right) {
		this.right = right;
	}
	
	public T getData() {
		return data;
	}
	
	public BSPTreeNode<T> getLeft() {
		return left;
	}
	
	public BSPTreeNode<T> getRight() {
		return right;
	}

	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return (left != null || right  != null);
	}
}
