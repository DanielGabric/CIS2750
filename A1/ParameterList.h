/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 1
*/

#include <stdlib.h>
#include "Boolean.h"
#include <stdio.h>


/*Typedefs in ParameterList for simplicity*/
typedef enum
{
    INT_TYPE,
    REAL_TYPE,
    BOOLEAN_TYPE,
    STRING_TYPE,
    LIST_TYPE,
    NULL_TYPE
}param_t;



typedef struct paramList
{
    int current;
    int size;
    char ** array;
}ParameterList;

union param_value
{
    int int_val;
    float real_val;
    Boolean bool_val;
    char *str_val;
    ParameterList *list_val;
};


void PL_destroy(ParameterList *l);
char * PL_next(ParameterList *l);
ParameterList * createPL(int size);