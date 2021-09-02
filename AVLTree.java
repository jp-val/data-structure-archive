// JP Valdespino
// August 2021

// AVLTree.java
// ============
// The self-balancing binary search tree.

class Node<AnyType extends Comparable<AnyType>>
{
	public AnyType data;
	public Node<AnyType> left, right;
	public int height;

	public Node(AnyType data)
	{
		this.data = data;
		this.left = this.right = null;
		this.height = 0;
	}
}

public class AVLTree<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> root;
	private int size;

	public AVLTree()
	{
		this.root = null;
		this.size = 0;
	}

	public int getHeight()
	{
		return (this.root == null) ? -1 : this.root.height;
	}

	private int getHeight(Node<AnyType> root)
	{
		return (root == null) ? -1 : root.height;
	}

	private int balanceFactor(Node<AnyType> root)
	{
		return getHeight(root.left) - getHeight(root.right);
	}

	private Node<AnyType> rotateLeft(Node<AnyType> root)
	{
		// root's right child is going to become the new root
		Node<AnyType> temp = root.right;

		// Root is moving down to the left and losing its right child. It can take
		// the new root's orphaned left child as its own right child.
		root.right = temp.left;

		// Right child now moves up to take the place of the root.
		temp.left = root;

		// Update tree heights.
		root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
		temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));

		return temp;
	}

	private Node<AnyType> rotateRight(Node<AnyType> root)
	{
		Node<AnyType> temp = root.left;
		root.left = temp.right;
		temp.right = root;

		root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
		temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));

		return temp;
	}

	private Node<AnyType> balanceAVLTree(Node<AnyType> root)
	{
		int bf = balanceFactor(root);

		if (bf == -2)
		{
			if (balanceFactor(root.right) > 0)
				root.right = rotateRight(root.right);
			
			root = rotateLeft(root);
		}
		else if (bf == 2)
		{
			if (balanceFactor(root.left) < 0)
				root.left = rotateLeft(root.left);
			
			root = rotateRight(root);
		}

		return root;
	}

	public void add(AnyType data)
	{
		this.root = add(this.root, data);
		this.size++;
	}

	private Node<AnyType> add(Node<AnyType> root, AnyType data)
	{
		if (root == null) return new Node<>(data);

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue < 0)
			root.left = add(root.left, data);
		else if (comparisonValue > 0)
			root.right = add(root.right, data);
		else
			return root; // Disallows the insertion of duplicate values.
		
		root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

		root = balanceAVLTree(root);

		return root;
	}

	public void remove(AnyType data)
	{
		this.root = remove(this.root, data);
		this.size--;
	}

	private Node<AnyType> remove(Node<AnyType> root, AnyType data)
	{
		Node<AnyType> temp = null;

		if (root == null) return null;

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue == 0)
		{
			// if no children, get rid of node
			if (root.left == null && root.right == null)
			{
				return null;
			}
			// if only right child exists, move that child up
			else if (root.left == null)
			{
				return root.right;
			}
			// if only left child exists, move that child up
			else if (root.right == null)
			{
				return root.left;
			}
			else
			{
				// move max data from left subtree up here
				root.data = findMax(root.left);

				// now delete that node from the left subtree
				root.left = remove(root.left, root.data);
			}
		}
		// delete from left subtree if data < root->data
		else if (comparisonValue < 0)
		{
			root.left = remove(root.left, data);
		}
		// delete from right subtree if data > root->data
		else
		{
			root.right = remove(root.right, data);
		}

		// update height of the subtree rooted here
		root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

		// restore the balance if necessary
		root = balanceAVLTree(root);

		return root;
	}

	private AnyType findMax(Node<AnyType> root)
	{
		if (root.right == null)
			return root.data;
		else
			return findMax(root.right);
	}

	public boolean contains(AnyType data)
	{
		return contains(this.root, data);
	}

	private boolean contains(Node<AnyType> root, AnyType data)
	{
		if (root == null) return false;

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue < 0)
			return contains(root.left, data);
		else if (comparisonValue > 0)
			return contains(root.right, data);
		else
			return true;
	}

	public int getSize()
	{
		return this.size;
	}

	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(this.root);
		System.out.println();
	}

	private void inorder(Node<AnyType> root)
	{
		if (root == null) return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(this.root);
		System.out.println();
	}

	private void preorder(Node<AnyType> root)
	{
		if (root == null) return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(this.root);
		System.out.println();
	}

	private void postorder(Node<AnyType> root)
	{
		if (root == null) return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}

	public static void main(String[] args)
	{
		AVLTree<Integer> t = new AVLTree<>();

		System.out.println("size: " + t.getSize());

		for (int i = 0; i < 10; i++)
			t.add(i);
		
		System.out.println("size: " + t.getSize());
		System.out.println("height: " + t.getHeight());

		t.inorder();
		t.preorder();
		t.postorder();

		t.remove(1);
		t.remove(2);

		System.out.println("size: " + t.getSize());
		System.out.println("height: " + t.getHeight());

		t.inorder();
		t.preorder();
		t.postorder();

		System.out.println("contains 9: " + t.contains(9));
		System.out.println("contains 0: " + t.contains(0));
		System.out.println("contains 7: " + t.contains(7));
		System.out.println("contains 1: " + t.contains(1));
		System.out.println("contains 2: " + t.contains(2));
	}
}