// JP Valdespino
// August 2021

// BloomFilter_v1.java
// ================
// Version 1: Fast implimentation, but heavy on memory. Instead of having different 
// hash functions, I used mutiple tables of different lengths.

import java.util.*;
import java.math.*;

public class BloomFilter_v1
{
	private boolean[][] table; // Our Bloom Filter.
	private int size; // The number of strings that have been inserted.

	public BloomFilter_v1()
	{
		// Given these table sizes, we can insert about 500 thousand strings before we start getting
		// too many collisions which would intefere with the data strcuture's effectiveness.
		int[] sizes = { 1000003, 1000033, 1000037, 1000039, 1000081, 1000099, 1000117 };

		this.table = new boolean[sizes.length][];

		for (int i = 0; i < this.table.length; i++)
			this.table[i] = new boolean[sizes[i]];

		this.size = 0;
	}

	public BloomFilter_v1(int[] sizes)
	{
		this.table = new boolean[sizes.length][];

		for (int i = 0; i < this.table.length; i++)
			this.table[i] = new boolean[sizes[i]];

		this.size = 0;
	}

	public void add(String str)
	{
		int hashValue = str.hashCode(); // Integer overflow occurs, but it doesn't matter.

		for (int i = 0; i < this.table.length; i++)
		{
			int j = Math.abs(hashValue%this.table[i].length);
			this.table[i][j] = true;
		}

		this.size++;
	}

	// Bloom Filters do not support deletion.
	public boolean remove(String str)
	{
		return false;
	}

	public boolean contains(String str)
	{
		int hashValue = str.hashCode();

		for (int i = 0; i < this.table.length; i++)
		{
			int j = Math.abs(hashValue%this.table[i].length);
			if (!this.table[i][j]) return false;
		}

		return true;
	}

	public static void main(String[] args)
	{
		BloomFilter_v1 t = new BloomFilter_v1();
		
		t.add("university");
		System.out.println(t.contains("university"));
		System.out.println(t.contains("powerful"));

		t.add("flamethrower");
		System.out.println(t.contains("flamethrower"));
		System.out.println(t.contains("firepower"));
	}
}