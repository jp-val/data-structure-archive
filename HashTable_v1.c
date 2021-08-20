// JP Valdespino
// August 2021

// Hashtable_v1.c
// ==============
// Collision resolution: Quadradic Probing
// Average runtime: O(1)
// Worst case runtime: O(n), where n is the table size.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TABLE_SIZE 13 // Should be a prime number like 33, 37, or 997.
#define HASH_CONSTANT 37
#define DIRTY_BIT "<__D_B__>"

typedef struct HashTable
{
	char **table;
	
	int size; // Number of elements in the Hash Table.
	int dirtyBitCnt;

} HashTable;

HashTable *createHashTable()
{
	HashTable *t = (HashTable*)malloc(sizeof(HashTable));
	
	t->table = (char**)calloc(TABLE_SIZE, sizeof(char*));
	
	t->size = 0;
	t->dirtyBitCnt = 0;

	return t;
}

int hash(char *str)
{
	int i, hval = 0, len = strlen(str);
	
	for (i = 0; i < len; i++)
		hval = (hval * HASH_CONSTANT + (int)str[i]) % TABLE_SIZE;

	return hval;
}

void rehash(HashTable *t)
{
	int i, j, hval, index;
	char **tmp, **new_table = (char**)calloc(TABLE_SIZE, sizeof(char*));

	for (i = 0; i < TABLE_SIZE; i++)
	{
		if (t->table[i] != NULL && strcmp(t->table[i], DIRTY_BIT) != 0)
		{
			j = 0;
			hval = hash(t->table[i]);
			
			do
			{
				index = (hval + j * j) % TABLE_SIZE;
				j++;
			}
			while (new_table[index] != NULL);

			new_table[index] = t->table[i];
		}
	}

	tmp = t->table;
	t->table = new_table;
	free(tmp);

	t->dirtyBitCnt = 0;
}

int add(HashTable *t, char *str)
{
	int i = 0, hval, index;

	if (t == NULL || t->table == NULL || str == NULL)
		return 0;

	if (t->dirtyBitCnt > t->size && t->dirtyBitCnt + t->size > TABLE_SIZE / 2)
		rehash(t);

	hval = hash(str);

	do
	{
		index = (hval + i * i) % TABLE_SIZE;
		if (i++ > TABLE_SIZE) return 0;
	}
	while (t->table[index] != NULL && strcmp(t->table[index], DIRTY_BIT) != 0);

	if (t->table[index] != NULL)
	{
		free(t->table[index]);
		t->dirtyBitCnt--;
	}

	t->table[index] = (char*)malloc((strlen(str) ) * sizeof(char));
	strcpy(t->table[index], str);

	t->size++;
	return 1;
}

int delete(HashTable *t, char *str)
{
	int i = 0, hval, index;

	if (t == NULL || t->table == NULL || str == NULL)
		return 0;

	hval = hash(str);

	while (i < TABLE_SIZE)
	{
		index = (hval + i * i) % TABLE_SIZE;

		if (t->table[index] == NULL)
		{
			return 0;
		}
		else if (strcmp(t->table[index], str) == 0)
		{
			free(t->table[index]);
			t->table[index] = (char*)malloc(strlen(DIRTY_BIT) * sizeof(char));

			strcpy(t->table[index], DIRTY_BIT);
			t->dirtyBitCnt++;
			t->size--;

			return 1;
		}

		i++;
	}

	return 0;
}

int contains(HashTable *t, char *str)
{
	int i = 0, hval, index;

	if (t == NULL || t->table == NULL || str == NULL)
		return 0;

	hval = hash(str);

	while (i < TABLE_SIZE)
	{
		index = (hval + i * i) % TABLE_SIZE;

		if (t->table[index] == NULL)
		{
			return 0;
		}
		else if (strcmp(t->table[index], str) == 0)
		{
			return 1;
		}

		i++;
	}

	return 0;
}

void display(HashTable *t)
{
	int i;

	if (t == NULL || t->table == NULL) return;

	for (i = 0; i < TABLE_SIZE; i++)
		printf("%s%s", (t->table[i] == NULL) ? "NULL" : t->table[i], (i < TABLE_SIZE-1) ? ", " : "\n");
}

int main(int argc, char **argv)
{
	HashTable *t = createHashTable();

	char *str = (char*)malloc(10 * sizeof(char));
	strcpy(str, "powerful");

	add(t, "powerful");
	add(t, "firefight");
	add(t, "waterpark");
	add(t, str);

	display(t);

	printf("%d\n", contains(t, "hello"));
	printf("%d\n", contains(t, "powerful"));
	printf("%d\n", contains(t, "firefight"));
	printf("%d\n", contains(t, "pokemon"));
	printf("%d\n", contains(t, "waterpark"));

	delete(t, "hello");
	delete(t, "powerful");
	delete(t, "firefight");
	delete(t, "pokemon");
	delete(t, "waterpark");
	delete(t, str);

	display(t);

	add(t, "pokemon");
	add(t, "hello");
	add(t, "firefight");
	add(t, "waterpark");

	display(t);

	rehash(t);

	display(t);

	return 0;
}