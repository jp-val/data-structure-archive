// JP Valdespino
// August 2021

// BloomFilter_v2.java
// ===================
// Version 2: Using bitmasks along with BigInteger, we can save a lot of memory compared 
// to using boolean arrays.
// +-----------------------------------------------------+
// | Bloom Filter                                        |
// |-----------------------------------------------------|
// | Time Complexity: | Best Case | Worst Case | Average |
// |------------------+-----------+------------+---------|
// | Insertion:       |   O(k)    |    O(k)    |  O(k)   |
// | Deletion:        |     *     |     *      |    *    |
// | Search:          |   O(k)    |    O(k)    |  O(k)   |
// +-----------------------------------------------------+
// *Where k is the length of the string (due to the hash function).
// *Bloom Filter's do not support deletion.

import java.util.Arrays;
import java.math.BigInteger;

public class BloomFilter_v2
{
	private int[] sizes = { 982451549, 982451567, 982451579, 982451581, 982451609, 982451629, 982451653 };
	private BigInteger[] table;
	private int size;

	public BloomFilter_v2()
	{
		table = new BigInteger[this.sizes.length];
		Arrays.fill(table, BigInteger.ZERO);
	}

	public void add(String str)
	{
		int hashValue = str.hashCode();
		for (int i = 0; i < this.sizes.length; i++)
		{
			int j = Math.abs(hashValue%this.sizes[i]);
			this.table[i] = this.table[i].setBit(j);
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
		for (int i = 0; i < this.sizes.length; i++)
		{
			int j = Math.abs(hashValue%this.sizes[i]);
			if (!this.table[i].testBit(j)) return false;
		}

		return true;
	}

	public static void main(String[] str)
	{
		int[] sizes =  { 5, 7, 11, 13, 17, 19, 23 };
		BloomFilter_v2 t = new BloomFilter_v2();

		t.add("university");
		t.add("flamethrower");

		System.out.println(t.contains("powerful"));
		System.out.println(t.contains("helpful"));
		System.out.println(t.contains("university"));
		System.out.println(t.contains("flamethrower"));
	}
}