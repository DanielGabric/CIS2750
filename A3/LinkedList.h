/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/


#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include "ParameterList.h"

typedef struct n
{
	char * k;
	/*Pointer to Information instance in ParamMan.h*/
	param_t type;
    union param_value pval;
    int isParam;
	int isStr;
	
}Node;

typedef struct list
{
	/*Pointer to struct Node in Param.h*/
	Node * info;
	int required;
	struct list * next;

	int infoChecked;
}LinkedList;




void deleteList(LinkedList * list);
LinkedList * addToList(LinkedList * list,Node* data, int optional, int infoChecked);
LinkedList * createNode(Node * key);
int search(LinkedList * list, char * key);
int searchIsChecked(LinkedList * list, char * key);
Node * getFromList(LinkedList * list, char * key);
int isRequired(LinkedList * list);


