// JP Valdespino
// August 2021

// HashMap.c
// =========
// Collision resolution: Linked chaining.

// +-----------------------------------------------------+
// | HashMap                                             |
// |-----------------------------------------------------|
// | Time Complexity: | Best Case | Worst Case | Average |
// |------------------+-----------+------------+---------|
// | Insertion:		  |   O(k)    |  O(k + n)  |  O(k)   |
// | Deletion:		  |   O(k)    |  O(k + n)  |  O(k)   |
// | Search:		  |   O(k)    |  O(k + n)  |  O(k)   |
// +-----------------------------------------------------+
// *Where k is the length of the string being inserted.
// *Where n is the number of elements in the HashMap.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#define TABLE_SIZE 23 // Should be a large value.
#define HASH_CONSTANT 37

typedef struct Node
{
	char *key;
	int value;
	struct Node *next;

} Node;

typedef struct HashMap
{
	Node **table;
	int size;

} HashMap;

Node *createNode(char *key, int value)
{
	Node *new_node = (Node*)malloc(sizeof(Node));
	if (new_node == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	new_node->key = key;
	new_node->value = value;
	new_node->next = NULL;

	return new_node;
}

HashMap *createHashMap(void)
{
	HashMap *new_HashMap = (HashMap*)malloc(sizeof(HashMap));
	if (new_HashMap == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	new_HashMap->table = (Node**)calloc(TABLE_SIZE, sizeof(Node*));
	new_HashMap->size = 0;

	return new_HashMap;
}

int hash(char *str)
{
	int i, hval, len;

	if (str == NULL) return -1;

	hval = 0;
	len = strlen(str);

	for (i = 0; i < len; i++)
		hval = (hval * HASH_CONSTANT + (int)str[i]) % TABLE_SIZE;

	return hval;
}

int put(HashMap *map, char *key, int value)
{
	int hval;
	Node *new_node, *tmp;

	if (map == NULL || map->table == NULL || key == NULL) return 0;

	hval = hash(key);

	new_node = createNode(key, value);
	if (new_node == NULL) return 0;

	if (map->table[hval] == NULL)
	{
		map->table[hval] = new_node;
		map->size++;
	}
	else
	{
		tmp = map->table[hval];
		while (tmp->next != NULL && strcmp(tmp->key, key) != 0)
		{
			tmp = tmp->next;
		}

		if (strcmp(tmp->key, key) == 0)
		{
			tmp->value = value;
			free(new_node);
		}
		else
		{
			new_node->next = map->table[hval];
			map->table[hval] = new_node;
			map->size++;
		}
	}

	return 1;
}

int delete(HashMap *map, char *key)
{
	int hval;
	Node *tmp, *prev;

	if (map == NULL || map->table == NULL || key == NULL) return 0;

	hval = hash(key);

	if (map->table[hval] != NULL && strcmp(map->table[hval]->key, key) == 0)
	{
		tmp = map->table[hval];
		map->table[hval] = map->table[hval]->next;
		
		free(tmp);
		map->size--;

		return 1;
	}

	prev = map->table[hval];
	tmp = map->table[hval]->next;

	while (tmp != NULL)
	{
		if (strcmp(tmp->key, key) == 0)
		{
			prev->next = tmp->next;
			
			free(tmp);
			map->size--;

			return 1;
		}
		tmp = tmp->next;
	}

	return 0;
}

int contains(HashMap *map, char *key)
{
	int hval;
	Node *tmp;

	if (map == NULL || map->table == NULL || key == NULL) return 0;

	hval = hash(key);

	tmp = map->table[hval];

	while (tmp != NULL)
	{
		if (strcmp(tmp->key, key) == 0) return 1;
		
		tmp = tmp->next;
	}

	return 0;
}

int getValue(HashMap *map, char *key)
{
	int hval;
	Node *tmp;

	if (map == NULL || map->table == NULL || key == NULL) return INT_MIN;

	hval = hash(key);

	tmp = map->table[hval];

	while (tmp != NULL)
	{
		if (strcmp(tmp->key, key) == 0)
			return tmp->value;
		
		tmp = tmp->next;
	}

	return INT_MIN;
}

HashMap *destroyHashMap(HashMap *map)
{
	int i;
	Node *tmp;

	if (map == NULL) return NULL;
	
	if (map->table == NULL)
	{
		free(map);
		return NULL;
	}
	
	for (i = 0; i < TABLE_SIZE; i++)
	{
		while (map->table[i] != NULL)
		{
			tmp = map->table[i];
			map->table[i] = map->table[i]->next;
			free(tmp);
		}
	}

	free(map->table);
	free(map);

	return NULL;
}

void display(HashMap *map)
{
	int i;
	Node *tmp;

	if (map == NULL)
	{
		printf("HashMap is NULL\n");
		return;
	}

	printf("Size: %d\n", map->size);

	for (i = 0; i < TABLE_SIZE; i++)
	{
		printf("%d: ", i);
		
		tmp = map->table[i];
		while (tmp != NULL)
		{
			printf("{ k: %s, v: %d } ", tmp->key, tmp->value);
			tmp = tmp->next;
		}
		printf("\n");
	}
}

int main(int argc, char **argv)
{
	HashMap *map = createHashMap();

	put(map, "one", 1);
	put(map, "two", 2);
	put(map, "three", 3);
	put(map, "four", 4);
	put(map, "five", 5);
	put(map, "six", 6);

	put(map, "two", 6);
	put(map, "six", 7);

	display(map);

	delete(map, "four");
	delete(map, "three");
	delete(map, "two");

	display(map);

	put(map, "two", 2);
	put(map, "six", 6);

	display(map);

	printf("contains %s: %d\n", "one", contains(map, "one"));
	printf("contains %s: %d\n", "two", contains(map, "two"));
	printf("contains %s: %d\n", "seven", contains(map, "seven"));
	printf("contains %s: %d\n", "forty", contains(map, "fourty"));
	
	printf("getValue %s: %d\n", "six", getValue(map, "six"));
	printf("getValue %s: %d\n", "five", getValue(map, "five"));

	map = destroyHashMap(map);

	return 0;
}