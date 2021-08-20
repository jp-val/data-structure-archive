// JP Valdespino
// August 2021

// Trie.java
// =========
// Time Complexity: O(k), where k is the length of the string.

import java.io.*;
import java.util.*;

class Node
{
	public static final int NUM_ALPHA = 26; // 26 letters in the alphabet.
	int count; // Will signify the number of accurances of the specific string.
	Node [] children;

	Node()
	{
		this.children = new Node[NUM_ALPHA];
		this.count = 0;
	}
}

public class Trie
{
	private Node root;

	public Trie()
	{
		this.root = new Node();
	}

	// Method does not insert non-alphabetic characters.
	public boolean insert(String str)
	{
		if (this.root == null || str == null)
			return false;

		str = str.toLowerCase();
		int strlen = str.length();
		Node wizard = this.root; // Temporary node variable.
		
		for (int i = 0; i < strlen; i++)
		{
			if (!Character.isLetter(str.charAt(i))) continue;
			int index = str.charAt(i) - 'a'; // Determines index of next node.
			
			if (wizard.children[index] == null)
				wizard.children[index] = new Node();

			wizard = wizard.children[index]; // Wizard jumps foward to next node.
		}

		wizard.count++;

		return true; // Returns true if insertion is successful.
	}

	public boolean delete(String str)
	{
		if (str == null)
			return false;

		if (!contains(str))
			return false;

		this.root = delete(this.root, str.toLowerCase(), str.length(), 0);
		return true;
	}

	private Node delete(Node wizard, String str, int strlen, int i)
	{
		if (wizard == null)
			return null;

		if (i == strlen)
		{
			wizard.count--;
			
			if (wizard.count > 0)
				return wizard;
			
			for (int j = 0; j < Node.NUM_ALPHA; j++)
				if (wizard.children[j] != null)
					return wizard;

			// If node does not have non-null children then we delete the node.
			return null;
		}
		
		while (!Character.isLetter(str.charAt(i)))
			i++;

		int index = str.charAt(i) - 'a';

		wizard.children[index] = delete(wizard.children[index], str, strlen, i+1);
		return wizard;
	}

	public boolean contains(String str)
	{
		if (this.root == null || str == null)
			return false;

		str = str.toLowerCase();
		Node wizard = this.root; // Temporary node variable.

		for (int i = 0; i < str.length(); i++)
		{
			if (!Character.isLetter(str.charAt(i))) continue;
			int index = str.charAt(i) - 'a';
			
			if (wizard.children[index] == null) return false;
			wizard = wizard.children[index];
		}

		return (wizard.count > 0) ? true : false;
	}

	// Count of a specific string.
	public int countSpecificString(String str)
	{
		if (str == null)
			return 0;

		str = str.toLowerCase();
		Node wizard = this.root; // Temporary node variable.

		for (int i = 0; i < str.length(); i++)
		{
			if (!(Character.isLetter(str.charAt(i)))) continue;
			int index = str.charAt(i) - 'a';
			
			if (wizard.children[index] == null) return 0;
			wizard = wizard.children[index];
		}

		return wizard.count;
	}

	// Counts the number of strings in the Trie, including duplicates.
	public int countNumStrings() // Wrapper Method.
	{
		return countNumStrings(this.root);
	}

	private int countNumStrings(Node wizard)
	{
		if (wizard == null)
			return 0;

		int count = wizard.count;

		for (int i = 0; i < Node.NUM_ALPHA; i++)
			count += countNumStrings(wizard.children[i]);

		return count;
	}

	// Counts the number of unique strings in the Trie.
	// (i.e. Counts duplicate strings as one).
	public int countUniqueStrings()
	{
		return countUniqueStrings(this.root);
	}

	private int countUniqueStrings(Node wizard)
	{
		if (wizard == null)
			return 0;

		int count = 0;

		if (wizard.count > 0)
			count++;

		for (int i = 0; i < Node.NUM_ALPHA; i++)
			count += countUniqueStrings(wizard.children[i]);

		return count;
	}

	// Prints all ths strings in the Trie.
	public void printTrie() // Wrapper Method.
	{
		StringBuilder str = new StringBuilder();
		printTrie(this.root, str, 0);
	}

	private void printTrie(Node wizard, StringBuilder str, int len)
	{
		if (wizard == null)
			return;

		if (wizard.count > 0)
			if (str.length() > 0)
				System.out.println(str + " (" + wizard.count + ")");
			else
				System.out.println("<__EMPTY_STRING__> (" + wizard.count + ")");

		for (int i = 0; i < Node.NUM_ALPHA; i++)
		{
			if (wizard.children[i] == null) continue;
			str.append((char)(i + 'a')); // Adds letter to string builder.
			printTrie(wizard.children[i], str, len+1); // Recursive descent.
			str.deleteCharAt(len); // Undos state change.
		}
	}

	public static void main(String [] args)
	{
		Trie lexicon = new Trie();

		String [] LOW = new String[] { "beast", "apple", "app", "", "starlord", "infinity", "sEXY", "PROGRAM", 
		"Apple", "apple", "aPplIcaTioN", "apple", "quid-pro-quo", "app", "linux", "alpha-male", "lip gloss", "quid pro quo" };

		String [] WNIOT = new String[] { "fire", "system", "fireball", "shipyard", "competitive" };

		for (int i = 0; i < LOW.length; i++)
		{
			System.out.println("Inserting... \"" + LOW[i] + "\"");
			lexicon.insert(LOW[i]);
		}
		System.out.println();

		System.out.printf("Trie word count: %3d\n", lexicon.countNumStrings());
		System.out.println("====================");

		lexicon.printTrie();
		System.out.println();

		System.out.println("Deleting words not in the Trie:");
		System.out.println("===============================");
		for (int i = 0; i < WNIOT.length; i++)
		{
			System.out.println("Deleting... \"" + WNIOT[i] + "\"");
			lexicon.delete(WNIOT[i]);
		}
		System.out.println();

		System.out.printf("Trie word count: %3d\n", lexicon.countNumStrings());
		System.out.println("====================");
		lexicon.printTrie();
		System.out.println();

		System.out.println("Deleting words in the Trie:");
		System.out.println("===========================");
		for (int i = 0; i < LOW.length; i++)
		{
			if (i%2 == 0)
			{
				LOW[i] = LOW[i].toLowerCase();
				System.out.println("Deleting... \"" + LOW[i] + "\"");
				lexicon.delete(LOW[i]);
			}
		}
		System.out.println();

		System.out.printf("Trie word count: %3d\n", lexicon.countNumStrings());
		System.out.println("====================");

		lexicon.printTrie();
		System.out.println();

		System.out.println("unique strings: " + lexicon.countUniqueStrings());
		System.out.println();

		System.out.println("number of times \"apple\" is in Trie: " + lexicon.countSpecificString("apple"));
		System.out.println();
	}
}