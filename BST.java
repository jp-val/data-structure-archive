// JP Valdespino
// August 2021

// BST.java
// ========
// Implemented using Java Generics.

// +-----------------------------------------------------+
// | Binary Search Tree                                  |
// |-----------------------------------------------------|
// | Time Complexity: | Best Case | Worst Case | Average |
// |------------------+-----------+------------+---------|
// | Insertion:		  |   O(1)    |    O(n)    |  O(h)   |
// | Deletion:		  |   O(1)    |    O(n)    |  O(h)   |
// | Search:		  |   O(1)    |    O(n)    |  O(h)   |
// +-----------------------------------------------------+
// *Where h is the height of the Tree, theoretically O(log n).
// *Where n is the number of elements in the Tree.

class Node<AnyType extends Comparable<AnyType>>
{
	AnyType data;
	Node<AnyType> left, right;

	Node(AnyType data)
	{
		this.data = data;
	}
}

public class BST<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> root;
	private int size;

	public BST()
	{
		this.root = null;
		this.size = 0;
	}

	public void insert(AnyType data)
	{
		root = insert(root, data);
		this.size++;
	}

	private Node<AnyType> insert(Node<AnyType> root, AnyType data)
	{
		if (root == null) return new Node<>(data);

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue < 0)
			root.left = insert(root.left, data);
		else if (comparisonValue > 0)
			root.right = insert(root.right, data);

		return root;
	}

	public void delete(AnyType data)
	{
		root = delete(root, data);
	}

	private Node<AnyType> delete(Node<AnyType> root, AnyType data)
	{
		if (root == null) return null;

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue < 0)
			root.left = delete(root.left, data);
		else if (comparisonValue > 0)
			root.right = delete(root.right, data);
		else
		{
			if (root.left == null && root.right == null)
				return null;
			else if (root.left == null)
				return root.right;
			else if (root.right == null)
				return root.left;
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}

			this.size--;
		}

		return root;
	}

	private AnyType findMax(Node<AnyType> root)
	{
		while (root.right != null)
			root = root.right;

		return root.data;
	}

	public boolean contains(AnyType data)
	{
		return contains(root, data);
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

	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}

	private void inorder(Node<AnyType> root)
	{
		if (root == null)
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}

	private void preorder(Node<AnyType> root)
	{
		if (root == null)
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}

	private void postorder(Node<AnyType> root)
	{
		if (root == null)
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}

	public static void main(String [] args)
	{
		// Creates new instance of a BST.
		BST<Integer> myTree = new BST<>();

		// Inserts random values into BST.
		for (int i = 0; i < 5; i++)
		{
			int r = (int)(Math.random() * 100) + 1;
			System.out.println("Inserting " + r + "...");
			myTree.insert(r);
		}

		// Prints out BST node values.
		myTree.inorder();
		myTree.preorder();
		myTree.postorder();

	}
}
