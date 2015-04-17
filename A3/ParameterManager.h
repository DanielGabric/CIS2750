/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stddef.h>
#include "HashTable.h"

#define INITIAL 5000
#define FALSE 0
#define TRUE 1
#define BOOLEAN int
#define PARSE_ERROR 0


typedef struct P
{
    HashTable * t;
}ParameterManager;

BOOLEAN isInt(char * c);
BOOLEAN isReal(char * c);
BOOLEAN isBoolean(char * c);
BOOLEAN isString(char * c);
BOOLEAN isListType(char * c);
BOOLEAN isGood(char c);
BOOLEAN isGoodAEquals(char c);
BOOLEAN hasBeenManaged(ParameterManager *p, char * key);
int strToInt(char * num);
Boolean strToBoolean(char * c);
char * strToStr(char *c);
ParameterList * strToList(char *c, int size);
float strToFloat(char * num);
ParameterManager * PM_createNode();
ParameterManager * PM_create(int size);
int PM_destroy(ParameterManager *p);
int PM_parseFrom(ParameterManager *p, FILE *fp, char comment);
int PM_manage(ParameterManager *p,char *pname, param_t ptype, int required);
int PM_hasValue(ParameterManager *p, char *pname);
union param_value PM_getValue(ParameterManager *p, char *pname);
BOOLEAN isSpace(char c);
