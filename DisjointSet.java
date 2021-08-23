// JP Valdespino
// August 2021

// DisjointSet.java
// ================
// Forest of trees implementation using an array.

import java.util.Arrays;

public class DisjointSet
{
	private int[] parent;

	public DisjointSet(int n)
	{
		this.parent = new int[n];
		for (int i = 0; i < n; i++)
			this.parent[i] = i;
	}

	public int find(int v)
	{
		if (this.parent[v] == v) return v;

		int res = find(this.parent[v]);
		this.parent[v] = res; // Attaches node directly to root node.

		return res;
	}

	public boolean union(int v1, int v2)
	{
		int rootv1 = find(v1);
		int rootv2 = find(v2);

		if (rootv1 == rootv2) return false;

		parent[rootv2] = rootv1;
		return true;
	}

	public void log()
	{
		System.out.println("Sets: " + Arrays.toString(this.parent));
	}

	public static void main(String[] args)
	{
		DisjointSet set = new DisjointSet(10);
		set.log();

		set.union(3, 4);
		set.union(4, 7);
		set.union(8, 7);
		set.log();

		System.out.println("parent of 7: " + set.find(7));
		set.log();

		System.out.println(set.union(4, 7));
		set.log();
	}
}