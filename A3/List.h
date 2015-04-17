/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/


#include <string.h>
#include <stdlib.h>
#include <stdio.h>

typedef struct lll
{
    int position;
    char * data;
    struct lll * next;
}List;

void deleteL(List * list);
List * addToL(List * list,char* data, int pos);
List * createN(char * key, int pos);
List * popL(List ** list);
int searchList(List * list, char * data);
char * searchFor(List * list, int position);