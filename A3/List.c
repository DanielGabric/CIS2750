/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
#include "List.h"

/*Searches for a given string and returns the position of it*/
int searchList(List * list, char * data)
{
    List * temp = list;
    while(temp!=NULL)
    {
    	if(!strcmp(temp->data,data))
    	{
    		return temp->position;
    	}
    	temp = temp->next;
    }
    return -1;
}


  /*Frees all allocated memory that is occupied by the linkedlist*/
void deleteL(List * list)
{
    List * temp;

    temp = list;
    while(list != NULL)
    {
        list = list->next;
        if(temp->data){
            //free(temp->data);
        }
        //free(temp);
        temp = list;
    }
}

/*Searches for a certain position and returns string*/
char * searchFor(List * list, int position)
{
    List * temp = list;
    while(temp!=NULL)
    {
        if(position==temp->position)
        {
            return temp->data;
        }
        temp=temp->next;
    }
    return NULL;
}

/*Adds data to the list*/
List * addToL(List * list, char * data, int pos)
{
    List * temp = list;
    if(temp == NULL && data)
    {
        temp = createN(data,pos);
        temp->data=data;
        return temp;
    }
    while(temp->next != NULL)
    {
        temp = temp->next;
    }
    temp->next = createN(data,pos);
    return list;
}

/*Pops the first value off the list*/
List * popL(List **list)
{
    if((*list)==NULL)
    {
        return NULL;
    }
    List * next1 = (*list);
    next1->next = NULL;
    (*list)=(*list)->next;
    return next1;
}

/*Creates a node for the list with a string and position*/
List * createN(char * key, int pos)
{
    List * newNode = malloc(sizeof(List));
    int len = strlen(key);
    newNode->next = NULL;
    newNode->position = pos;
    newNode->data = malloc(sizeof(char)*(strlen(key)+1));
    strncpy(newNode->data,key,len);
    
    return newNode;
}