// JP Valdespino
// August 2021

// Heap.c
// ======
// Implemenetd a switchable min and max Heap.

// +------------------------------------------------------+
// | Heap                                                 |
// |------------------------------------------------------|
// | Time Complexity: | Best Case | Worst Case | Average  |
// |------------------+-----------+------------+----------|
// | Insertion:	      | O(log n)  |  O(log n)  | O(log n) |
// | Deletion:        | O(log n)  |  O(log n)  | O(log n) |
// | Search:          | O(log n)  |  O(log n)  | O(log n) |
// +------------------------------------------------------+
// *Where n is the number of elements in the Heap.

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#define TRUE 1
#define FALSE 0
#define ERROR INT_MIN
#define CAPACITY 20

typedef struct Heap
{
	int *table, isMinHeap, size, capacity;

} Heap;

Heap *createHeap(int isMinheap)
{
	Heap *new_Heap = (Heap*)malloc(sizeof(Heap));
	if (new_Heap == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	new_Heap->table = (int*)malloc(CAPACITY * sizeof(int));
	new_Heap->isMinHeap = isMinheap;
	new_Heap->size = 0;
	new_Heap->capacity = CAPACITY;

	return new_Heap;
}

int isFull(Heap *h)
{
	return h == NULL || h->table == NULL || h->size >= h->capacity;
}

int isEmpty(Heap *h)
{
	return h == NULL || h->table == NULL || h->size <= 0;
}

void swap(int *a, int *b)
{
	int tmp;

	tmp = *a;
	*a = *b;
	*b = tmp;
}

void perculateUp(Heap *h, int child)
{
	int parent, doSwap;

	if (h == NULL || h->table == NULL) return;

	while (TRUE)
	{
		parent = (child - 1) / 2;

		if (h->isMinHeap)
			doSwap = h->table[parent] > h->table[child];
		else
			doSwap = h->table[parent] < h->table[child];

		if (doSwap)
		{
			swap(&h->table[parent], &h->table[child]);
			child = parent;
		}
		else
		{
			break;
		}
	}
}

void perculateDown(Heap *h, int parent)
{
	int child, doSwap;

	if (h == NULL || h->table == NULL) return;

	while (TRUE)
	{
		if (parent*2+1 >= h->size)
			break;
		else if (parent*2+2 >= h->size)
			child = parent * 2 + 1;
		else if (h->isMinHeap && h->table[parent*2+1] < h->table[parent*2+2])
			child = parent * 2 + 1;
		else if (!h->isMinHeap && h->table[parent*2+1] > h->table[parent*2+2])
			child = parent * 2 + 1;
		else
			child = parent * 2 + 2;

		if (h->isMinHeap)
			doSwap = h->table[parent] > h->table[child];
		else
			doSwap = h->table[parent] < h->table[child];

		if (doSwap)
		{
			swap(&h->table[child], &h->table[parent]);
			parent = child;
		}
		else
		{
			break;
		}
	}
}

void add(Heap *h, int data)
{
	if (isFull(h)) return;

	h->table[h->size] = data;
	perculateUp(h, h->size);

	h->size++;
}

int poll(Heap *h)
{
	int retval;

	if (h == NULL || h->table == NULL) return ERROR;
	
	retval = h->table[0];
	h->table[0] = h->table[h->size-1];

	if (--h->size > 0) perculateDown(h, 0);

	return retval;
}

Heap *destroyHeap(Heap *h)
{
	if (h == NULL) return NULL;

	if (h->table != NULL)
		free(h->table);

	free(h);
	
	return NULL;
}

void display(Heap *h)
{
	int i;

	if (h == NULL || h->table == NULL)
	{
		printf("Heap is NULL.\n");
		return;
	}

	printf("%sHeap: %s", h->isMinHeap ? "Min" : "Max", (h->size > 0) ? "" : "<__EMPTY_>\n");

	for (i = 0; i < h->size; i++)
		printf("%d%s", h->table[i], (i < h->size-1) ? ", " : "\n");
}

int main(int argc, char **argv)
{
	int i;

	Heap *minheap = createHeap(TRUE); // TRUE for a min heap, FALSE for a max heap.

	for (i = 9; i >= 0; i--)
	{
		add(minheap, i);
		printf("%d: ", i);
		display(minheap);
	}

	printf("\n");

	while (!isEmpty(minheap))
	{
		printf("polled min: %d: ", poll(minheap));
		display(minheap);
	}

	minheap = destroyHeap(minheap);

	return 0;
}