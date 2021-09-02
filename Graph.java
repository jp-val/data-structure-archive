// JP Valdespino
// August 2021

// Graph.java
// ==========
// Best Graph representation methods:
// - Adjacency Matrix: Best for dense graphs.
// - Adjacency List: Best for sparse graphs, or for graphs with multiple edges
//                   from one vertex to another.

import java.io.File;
import java.io.IOException;

import java.util.Queue;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

// Edge object for graph representation utilizing Adjacency List.
class Edge implements Comparable<Edge>
{
	int vertex, weight;

	public Edge(int vertex, int weight)
	{
		this.vertex = vertex;
		this.weight = weight;
	}

	public int compareTo(Edge e)
	{
		return this.weight - e.weight;
	}

	public String toString()
	{
		return "{ v: " + this.vertex + ", w: " + this.weight + " }";
	}
}

public class Graph
{
	public static final int oo = (int)1e9;

	// These two strcutures represent the same graph.
	public int[][] adjMatrix;
	public ArrayList<ArrayList<Edge>> adjList;

	public Graph(String filename) throws IOException
	{
		Scanner stdin = new Scanner(new File(filename));

		int n = stdin.nextInt();

		this.adjMatrix = new int[n][n];
		for (int i = 0; i < n; i++)
			Arrays.fill(this.adjMatrix[i], Graph.oo);

		this.adjList = new ArrayList<>();
		for (int i = 0; i < n; i++)
			this.adjList.add(new ArrayList<Edge>());

		for (int i = 0; i < n; i++)
		{
			int numEdges = stdin.nextInt();
			for (int j = 0; j < numEdges; j++)
			{
				int vertex = stdin.nextInt();
				int weight = stdin.nextInt();
	 			
				this.adjMatrix[i][vertex] = weight;
				this.adjList.get(i).add(new Edge(vertex, weight));
			}
		}

		stdin.close();
	}

	public boolean containsCycle_forUndirectedGraphs()
	{
		boolean[] visited = new boolean[this.adjList.size()];
		return containsCycle_forUndirectedGraphs(visited, -1, 0);
	}

	private boolean containsCycle_forUndirectedGraphs(boolean[] visited, int parent, int vertex)
	{
		if (visited[vertex]) return true;

		visited[vertex] = true;

		for (Edge e: this.adjList.get(vertex))
			if (e.vertex != parent)
				if (containsCycle_forUndirectedGraphs(visited, vertex, e.vertex))
					return true;

		return false;
	}

	public static boolean containsCycle_forDirectedGraphs()
	{
		boolean[] visited = new boolean[this.adjList.size()];
		Stack<Integer> stack = new Stack<>();

		stack.push(0);

		while (!stack.isEmpty())
		{
			Integer vertex = stack.pop();

			if (visited[vertex]) return true;

			visited[vertex] = true;

			for (Edge e: this.adjList.get(vertex))
				stack.push(e.vertex);
			
			visited[vertex] = false;
		}
		
		return false;
	}

	public void displayAdjMatrix()
	{
		for (int i = 0; i < this.adjMatrix.length; i++)
			System.out.println(i + ": " + Arrays.toString(this.adjMatrix[i]));
	}

	public static void displayAdjMatrix(int[][] adjMatrix)
	{
		for (int i = 0; i < adjMatrix.length; i++)
			System.out.println(i + ": " + Arrays.toString(adjMatrix[i]));
	}

	public void displayAdjList()
	{
		int i = 0;
		for (ArrayList<Edge> v: this.adjList)
			System.out.println(i++ + ": " + Arrays.toString(v.toArray(new Edge[v.size()])));
	}

	public static void displayAdjList(ArrayList<ArrayList<Edge>> adjList)
	{
		int i = 0;
		for (ArrayList<Edge> v: adjList)
			System.out.println(i++ + ": " + Arrays.toString(v.toArray(new Edge[v.size()])));
	}

	public static void main(String[] args) throws IOException
	{
		Graph g1 = new Graph("graph-undirected-petersen.in");
		Graph g2 = new Graph("graph-undirected-tree.in");
		
		g1.displayAdjList();
		System.out.println();
		g1.displayAdjMatrix();
		System.out.println();

		System.out.println("contains cycle petersen graph: " + g1.containsCycle());
		System.out.println("contains cycle tree graph: " + g2.containsCycle());
	}
}