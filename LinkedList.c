// JP Valdespino
// August 2021

// LinkedList.c
// ============

// +---------------------------+
// | Linked List               |
// |---------------------------|
// | Time Complexity:          |
// |---------------------------|
// | Insertion:           O(1) |
// | Deletion:            O(n) |
// | Search:              O(n) |
// +---------------------------+
// *Where n is the number of elements in the Tree.

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#define ERROR INT_MIN

typedef struct Node
{
	int data;
	struct Node *next;

} Node;

typedef struct LinkedList
{
	Node *head, *tail;
	int size;

} LinkedList;

Node *createNode(int data)
{
	Node *new_node = (Node*)malloc(sizeof(Node));
	if (new_node == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	new_node->data = data;
	new_node->next = NULL;

	return new_node;
}

LinkedList *createLinkedList(void)
{
	Node *new_node;

	LinkedList *new_LinkedList = malloc(sizeof(LinkedList));
	if (new_LinkedList == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	new_node = createNode(ERROR);
	if (new_node == NULL)
	{
		free(new_LinkedList);
		return NULL;
	}

	new_LinkedList->head = new_LinkedList->tail = new_node;
	new_LinkedList->size = 0;

	return new_LinkedList;
}

int add(LinkedList *list, int data)
{
	Node *new_node;

	if (list == NULL || list->head == NULL) return 0;

	new_node = createNode(data);
	if (new_node == NULL) return 0;

	if (list->head == list->tail)
	{
		list->head->next = list->tail = new_node;
	}
	else
	{
		list->tail = list->tail->next = new_node;
	}

	list->size++;

	return 1;
}

int push(LinkedList *list, int data) // Head insert.
{
	Node *new_node;

	if (list == NULL || list->head == NULL) return 0;

	new_node = createNode(data);
	if (new_node == NULL) return 0;

	if (list->head == list->tail)
	{
		list->head->next = list->tail = new_node;
	}
	else
	{
		new_node->next = list->head->next;
		list->head->next = new_node;
	}

	list->size++;

	return 1;
}

int insert(LinkedList *list, int data, int index)
{
	int i;
	Node *tmp, *new_node;

	if (list == NULL || list->head == NULL) return 0;

	if (index > list->size) return 0;

	if (index == list->size) return add(list, data);

	new_node = createNode(data);
	if (new_node == NULL) return 0;

	tmp = list->head;

	for (i = 0; i < index; i++)
		tmp = tmp->next;

	new_node->next = tmp->next;
	tmp->next = new_node;
	list->size++;
	return 1;
}

int delete(LinkedList *list, int data)
{
	Node *tmp, *prev;

	if (list == NULL || list->head == NULL) return 0;

	prev = list->head;
	tmp = list->head->next;

	while (tmp != NULL)
	{
		if (tmp->data == data)
		{
			if (tmp == list->tail)
			{
				list->tail = prev;
				free(tmp);
				list->tail->next = NULL;
			}
			else
			{
				prev->next = tmp->next;
				free(tmp);
			}

			list->size--;
			return 1;
		}
		else
		{
			tmp = tmp->next;
			prev = prev->next;
		}
	}

	return 0;
}

int deleteAll(LinkedList *list, int data)
{
	Node *tmp, *prev;
	int retval = 0;

	if (list == NULL || list->head == NULL) return retval;

	prev = list->head;
	tmp = list->head->next;

	while (tmp != NULL)
	{
		if (tmp->data == data)
		{
			if (tmp == list->tail)
			{
				list->tail = prev;
				free(tmp);
				list->tail->next = NULL;
			}
			else
			{
				prev->next = tmp->next;
				free(tmp);
			}

			tmp = prev->next;
			list->size--;
			retval = 1;
		}
		else
		{
			tmp = tmp->next;
			prev = prev->next;
		}
	}

	return retval;
}

int pop(LinkedList *list)
{
	Node *tmp;
	int retval;

	if (list == NULL || list->head == NULL || list->head->next == NULL)
		return ERROR;
	
	retval = list->head->next->data;
	if (list->head->next == list->tail) list->tail = list->head;

	tmp = list->head->next;
	list->head->next = list->head->next->next; // lolol
	
	free(tmp);
	list->size--;

	return retval;
	
}

int contains(LinkedList *list, int data)
{
	Node *tmp;

	if (list == NULL || list->head == NULL) return 0;

	tmp = list->head->next;

	while (tmp != NULL)
	{
		if (tmp->data == data)
			return 1;
		else
			tmp = tmp->next;
	}

	return 0;
}

int peek(LinkedList *list)
{
	if (list == NULL || list->head == NULL || list->head->next == NULL)
		return ERROR;
	else
		return list->head->next->data;
}

int isEmpty(LinkedList *list)
{
	return list == NULL || list->head == NULL || list->head->next == NULL;
}

LinkedList *cloneLinkedList(LinkedList *list)
{
	Node *tmp;
	LinkedList *clone;

	if (list == NULL) return NULL;

	clone = createLinkedList();
	if (clone == NULL) return NULL;

	if (list->head == NULL)
	{
		free(clone->head);
		return clone;
	}

	tmp = list->head->next;

	while(tmp != NULL)
		add(clone, tmp->data);

	return clone;
}

void reverseLinkedList(LinkedList *list)
{
	int data;
	LinkedList *tmp;

	if (list == NULL) return;

	tmp = createLinkedList();

	while(!isEmpty(list))
		push(tmp, pop(list));

	free(list->head);

	list->head = tmp->head;
	list->tail = tmp->tail;
	list->size = tmp->size;

	free(tmp);
}

LinkedList *destroyLinkedList(LinkedList *list)
{
	Node *tmp;
	
	if (list == NULL) return NULL;
	
	while (list->head != NULL)
	{
		tmp = list->head;
		list->head = list->head->next;
		free(tmp);
	}

	free(list);
	return NULL;
}

void displayList(LinkedList *list)
{
	int i;
	Node *tmp;

	if (list == NULL || list->head == NULL || list->head->next == NULL)
		printf("List: <__EMPTY_LIST__>\n");
	
	i = 1;
	tmp = list->head->next;

	printf("size: %d\n", list->size);
	printf("List: ");

	while (tmp != NULL)
	{
		printf("%d%s", tmp->data, (i++ < list->size) ? ", " : "\n");
		tmp = tmp->next;
	}
}

int main(int argc, char **argv)
{
	LinkedList *s = createLinkedList();
	
	add(s, 1);
	add(s, 1);
	add(s, 1);
	add(s, 2);

	displayList(s);

	pop(s);
	pop(s);
	pop(s);
	pop(s);
	pop(s);
	pop(s);

	displayList(s);
	
	push(s, 4);
	push(s, 4);
	push(s, 4);
	push(s, 4);
	push(s, 4);

	displayList(s);

	add(s, 1);
	add(s, 1);
	add(s, 1);
	add(s, 1);

	displayList(s);

	pop(s);

	insert(s, 7, 1);
	insert(s, 3, 0);
	add(s, 2);

	displayList(s);

	reverseLinkedList(s);

	displayList(s);

	add(s, 9);
	add(s, 9);
	add(s, 9);

	push(s, 4);
	push(s, 4);
	push(s, 4);

	displayList(s);
	
	s = destroyLinkedList(s);

	return 0;
}