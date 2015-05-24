/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 1
*/

#include "LinkedList.h"

/*Frees all allocated memory that is occupied by the linkedlist*/
void deleteList(LinkedList * list)
{
    LinkedList * temp;

    temp = list;
    while(list != NULL)
    {
        list = list->next;
        if(temp->info){

            if(temp->infoChecked)
            {
                /*deleting memory str_val embedded in union*/

                if(temp->info->isStr)
                {
                    /*printf("%s\n",temp->info->k);*/
                    free(temp->info->pval.str_val);
                }else
                /*Deleting parameter list*/
                if(temp->info->isParam)
                {

                    PL_destroy(temp->info->pval.list_val);
                }
                    
                
                
            }
            if(temp->info->k)
            {
                free(temp->info->k);
            }
            
            free(temp->info);
        }

        free(temp);
        temp = list;
    }
}

/*Gets a node from the LinkedList*/
Node * getFromList(LinkedList * list, char * key)
{

    LinkedList * temp = list;
    while(temp!=NULL)
    {
        if(!strcmp(temp->info->k,key))
        {
            return temp->info;
            /*return list->someVal;*/
        }
        temp = temp->next;
    }

    return NULL;
}

/*Allocates memory for a new linkedlist node, and initializes all values*/
LinkedList * createNode(Node * key)
{

    LinkedList * newNode = malloc(sizeof(LinkedList));

    newNode->next = NULL;
    newNode->infoChecked = 0;
    newNode->info = NULL;
    newNode->required = 0;
    return newNode;
}

/*Returns whether all required elements in a list have info*/
int isRequired(LinkedList * list)
{
    LinkedList * temp=list;
    while(temp!=NULL)
    {
        if(temp->required&&!temp->infoChecked)
        {
            return 0;
        }
        temp = temp->next;
    }
    return 1;
}

/*Adds data to the list*/
LinkedList * addToList(LinkedList * list, Node * data, int optional, int infoChecked)
{
    LinkedList * temp = list;
    if(temp == NULL && data)
    {
        if(optional>=0)
        {
            temp = createNode(data);
            temp->info=data;
            temp->required = optional;
        }
        if(optional <0)
        {
            temp->infoChecked = infoChecked;
        }
        
        return temp;
    }
    while(temp != NULL)
    {

        if(!strcmp(temp->info->k,data->k))
        {
            if(data)
            {
                temp->info = data;
            }
            if(optional >=0)
            {
                temp->required = optional;
            }
            if(optional <0)
            {
                temp->infoChecked = infoChecked;
            }
            /*if(type == 0)
            {
                temp->hidden = val;
            }
            else if(type == 1)
            {
                temp->paramList = List;
            }*/
            
            return list;
        }
        if(temp->next==NULL)
        {
            
            break;
        }
        temp = temp->next;
    }
    if(data)
    {
        if(optional >=0)
        {
            temp->next = createNode(data);
            temp->next->info = data;
            temp->next->required = optional;
            /*if(type == 0)
            {
                temp->next->hidden = val;
            }
            else if(type == 1)
            {
                temp->next->paramList = List;
            }*/
        }
        if(optional <0)
        {
            temp->next->infoChecked = infoChecked;
        }
    }
    return list;
}

/*checks if a certain key is located in a list*/
int search(LinkedList * list, char * key)
{

    LinkedList * temp = list;
    while(temp != NULL)
    {
        if(!strcmp(temp->info->k,key))
        {
            return 1;
        }
        temp = temp->next;
    }
    return 0;
}

/*Checks if data has been entered in a node with a certain key*/
int searchIsChecked(LinkedList * list, char * key)
{
    if(!search(list,key))
    {
        return 0;
    }
    LinkedList * temp = list;
    while(temp != NULL)
    {
        if(!strcmp(temp->info->k,key))
        {
            if(temp->infoChecked)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        temp = temp->next;
    }
    return 0;
}
