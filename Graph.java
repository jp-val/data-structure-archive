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

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

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
		Graph g = new Graph("graph-undirected-petersen.in");
		
		g.displayAdjList();
		System.out.println();
		g.displayAdjMatrix();
	}
}