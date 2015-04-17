/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 1
*/

#include "ParameterList.h"

/*Obtain the next item in a parameter list
PRE: n/a
POST: Returns the next item in parameter list l, NULL if there are no items remaining in the list*/
char * PL_next(ParameterList *l)
{
	if(l->current == l->size)
	{
		return NULL;
	}
    return l->array[l->current++];
}

/*Obtain the next item in a parameter list
PRE: valid parameterlist
POST: Frees all memory associated with the parameterlist l*/
void PL_destroy(ParameterList *l)
{
	int i;

	if(l)
	{
		if(l->array)
		{
			
		    for(i=0;i<l->size;++i)
		    {
			    if(l->array[i])
			    {
				    free(l->array[i]);
			    }
		    }
		    free(l->array);
		}
		free(l);
	}
}

/*Creates a new ParameterList of length size*/
ParameterList * createPL(int size)
{
	int i;
	ParameterList * newPL = malloc(sizeof(ParameterList));
    newPL->size = size;
    newPL->current = 0;
    newPL->array = malloc(sizeof(char*)*size);
    for(i=0;i<size;++i)
    {
    	newPL->array[i]=NULL;
    }
	return newPL;
}
