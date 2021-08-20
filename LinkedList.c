// JP Valdespino
// August 2021

// LinkedList.c
// ============
// Insertion: O(1)
// Deletion: O(n), where n is the number of elements in the Linked List.
// Search: O(n), where n is the number of elements in the Linked List.

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

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
	LinkedList *new_LinkedList = calloc(1, sizeof(LinkedList));
	if (new_LinkedList == NULL)
	{
		printf("Error: out of memory.\n");
		return NULL;
	}

	return new_LinkedList;
}

int add(LinkedList *list, int data)
{
	Node *new_node;

	if (list == NULL)
	{
		return 0;
	}
	else if (list->head == NULL)
	{
		list->head = list->tail = createNode(data);
	}
	else
	{
		new_node = createNode(data);
		if (new_node == NULL) return 0;

		list->tail->next = new_node;
		list->tail = list->tail->next;
	}

	list->size++;
	return 1;
}

int push(LinkedList *list, int data) // Head insert.
{
	Node *new_node;

	if (list == NULL)
	{
		return 0;
	}
	else if (list->head == NULL)
	{
		list->head = list->tail = createNode(data);
	}
	else
	{
		new_node = createNode(data);
		if (new_node == NULL) return 0;

		new_node->next = list->head;
		list->head = new_node;
	}

	list->size++;
	return 1;
}

int insert(LinkedList *list, int data, int index)
{
	int i;
	Node *tmp, *new_node;

	if (list == NULL)
		return 0;

	if (index > list->size)
		return 0;

	if (index == 0)
		return push(list, data);

	tmp = list->head;

	for (i = 0; i < index-1; i++)
		tmp = tmp->next;

	new_node = createNode(data);
	if (new_node == NULL) return 0;

	new_node->next = tmp->next;
	tmp->next = new_node;
	list->size++;
	return 1;
}

int delete(LinkedList *list, int data)
{
	Node *tmp, *prev;

	if (list == NULL || list->head == NULL) return 0;
	
	if (list->head->data == data)
	{
		tmp = list->head;
		list->head = list->head->next;
		
		free(tmp);
		list->size--;
		
		return 1;
	}

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
	
	while (list->head != NULL && list->head->data == data)
	{
		tmp = list->head;
		list->head = list->head->next;
		
		free(tmp);
		list->size--;

		retval = 1;
	}

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

	if (list == NULL || list->head == NULL)
	{
		return INT_MIN;
	}
	else
	{
		retval = list->head->data;
		
		tmp = list->head;
		list->head = list->head->next;
		
		free(tmp);
		list->size--;

		return retval;
	}
}

int contains(LinkedList *list, int data)
{
	Node *tmp;

	if (list == NULL) return 0;

	tmp = list->head;

	while (tmp != NULL)
	{
		if (tmp->data == data)
		{
			return 1;
		}
		else
		{
			tmp = tmp->next;
		}
	}

	return 0;
}

int peek(LinkedList *list)
{
	if (list == NULL || list->head == NULL)
	{
		return INT_MIN;
	}
	else
	{
		return list->head->data;
	}
}

int isEmpty(LinkedList *list)
{
	if (list == NULL || list->head == NULL)
	{
		return 1;
	}
	else
	{
		return 0;
	}
}

LinkedList *cloneLinkedList(LinkedList *list) // TODO
{
	Node *tmp;
	LinkedList *clone = createLinkedList();
	if (clone == NULL) return NULL;

	tmp = list->head;

	while(tmp != NULL)
	{
		add(clone, tmp->data);
	}

	return clone;
}

void reverseLinkedList(LinkedList *list)
{
	int data;
	LinkedList *new;

	if (list == NULL) return;

	new = createLinkedList();

	while(!isEmpty(list))
	{
		push(new, pop(list));
	}

	list->head = new->head;
	list->tail = new->tail;
	list->size = new->size;

	free(new);
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

void display(LinkedList *list)
{
	int i;
	Node *tmp;

	if (list == NULL || list->head == NULL)
		printf("<__EMPTY_LIST__>\n");
	
	i = 1;
	tmp = list->head;

	printf("size: %d\n", list->size);

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

	display(s);

	pop(s);
	pop(s);

	display(s);

	add(s, 1);
	add(s, 1);
	add(s, 1);
	add(s, 1);

	display(s);

	pop(s);

	insert(s, 7, 1);
	insert(s, 3, 0);
	add(s, 2);

	display(s);

	reverseLinkedList(s);

	display(s);

	add(s, 9);
	add(s, 9);
	add(s, 9);

	push(s, 4);
	push(s, 4);
	push(s, 4);

	display(s);
	
	return 0;
}