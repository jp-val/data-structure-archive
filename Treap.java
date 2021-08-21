// JP Valdespino
// August 2021

// Treap.java
// ==========
// Implemented using Java Generics.

// +-----------------------------------------------------+
// | Treap                                               |
// |-----------------------------------------------------|
// | Time Complexity: | Best Case | Worst Case | Average |
// |------------------+-----------+------------+---------|
// | Insertion:       |   O(1)    |    O(n)    |  O(h)   |
// | Deletion:        |   O(1)    |    O(n)    |  O(h)   |
// | Search:          |   O(1)    |    O(n)    |  O(h)   |
// +-----------------------------------------------------+
// *Where h is the height of the Tree, theoretically O(log n).
// *Where n is the number of elements in the Tree.

import java.util.HashSet;
import java.util.Random;

class Node<AnyType extends Comparable<AnyType>>
{
	AnyType data;
	int priority = 0;
	Node<AnyType> left, right;

	Node(AnyType data, int priority)
	{
		this.data = data;
		this.priority = priority;
	}

	public int compareTo(Node<AnyType> that)
	{
		if (this.data == null && that.data == null)
			return 0;
		if (this.data == null)
			return -1;
		if (that.data == null)
			return 1;
		return this.data.compareTo(that.data);
	}

	public String toString()
	{
		return "{ D: " + data + ", P: " + priority + " }";
	}
}

public class Treap<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> root;
	private int size;

	private static Random randy = new Random();
	private HashSet<Integer> prioritySet = new HashSet<>();
	
	private int generatePriority()
	{
		int priority = randy.nextInt();

		while (this.prioritySet.contains(priority))
			priority = randy.nextInt();
		
		prioritySet.add(priority);
		return priority;
	}

	public void add(AnyType data) 
	{
		this.root = add(this.root, data, generatePriority());
	}

	public void add(AnyType data, int priority)
	{
		this.root = add(this.root, data, priority);
	}

	private Node<AnyType> add(Node<AnyType> root, AnyType data, Integer priority)
	{
		if (root == null)
		{
			this.size++;
			return new Node<AnyType>(data, priority);
		}

		int comparisonValue = data.compareTo(root.data);

		if (comparisonValue < 0)
		{
			root.left = add(root.left, data, priority);
			if (root.left.priority < root.priority)
				root = rotateRight(root);
		}
		else if (comparisonValue > 0)
		{
			root.right = add(root.right, data, priority);
			if (root.right.priority < root.priority)
				root = rotateLeft(root);
		}

		return root;
	}

	private Node<AnyType> rotateRight(Node<AnyType> exParent)
	{
		Node<AnyType> nodeToBeParent = exParent.left;
		exParent.left = nodeToBeParent.right;
		nodeToBeParent.right = exParent;

		return nodeToBeParent;
	}

	private Node<AnyType> rotateLeft(Node<AnyType> exParent)
	{
		Node<AnyType> nodeToBeParent = exParent.right;
		exParent.right = nodeToBeParent.left;
		nodeToBeParent.left = exParent;
		return nodeToBeParent;
	}

	public void remove(AnyType data)
	{
		if (contains(data))
		{
			this.root = remove(this.root, data);
			size--;
		}
	}

	private Node<AnyType> remove(Node<AnyType> root, AnyType data)
	{
		if (root == null)
			return null;
		else if (data.compareTo(root.data) < 0)
			root.left = remove(root.left, data);
		else if (data.compareTo(root.data) > 0)
			root.right = remove(root.right, data);
		else
		{
			if (root.left == null && root.right == null)
				return null;
			else if (root.right == null)
				return root.left;
			else if (root.left == null)
				return root.right;
			else
			{
				if (root.left.priority < root.right.priority)
					root = rotateRight(root);
				else
					root = rotateLeft(root);
				
				if (root != null)
					root = remove(root, data);
			}
		}

		return root;
	}


	public boolean contains(AnyType data)
	{
		return contains(this.root, data);
	}

	private boolean contains(Node<AnyType> root, AnyType data)
	{

		if (root == null)
			return false;
		
		int comparisonValue = data.compareTo(root.data);
		
		if (comparisonValue < 0)
			return contains(root.left, data);
		else if (comparisonValue > 0)
			return contains(root.right, data);
		else
			return true;
	}

	private boolean isEmpty(Node<AnyType> root) 
	{
		return this.root == null;
	}

	public int size()
	{
		return this.size;
	}

	public int height()
	{
		return height(this.root);
	}

	private int height(Node<AnyType> root)
	{
		if (root == null) return -1;
		
		int leftH = height(root.left);
		int rightH = height(root.right);

		return Math.max(leftH, rightH) + 1;
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
		Treap<Integer> t = new Treap<Integer>();

		t.add(0);
		t.add(1);
		t.add(2);
		t.add(3);
		t.add(4);
		t.add(5);
		t.add(6);
		t.add(7);
		t.add(8);
		t.add(9);

		System.out.println("size: " + t.size());
		System.out.println("height: " + t.height());

		t.preorder();
		t.postorder();
	}
}