/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/

#include "LinkedList.h"

typedef struct tab
{
    int size;
    LinkedList ** table;

}HashTable;

HashTable * createHashTable(int size);
void deleteTable(HashTable * toBeDeleted);
int hashFunction(HashTable * h, char * toBeHashed);
void addToTable(HashTable * h, Node * data, int optional, int infoCheck);
int isAlready(HashTable * h, char * check);
int isAlreadyType(HashTable * h, char * check);
Node * get(HashTable * h, char * key);
int searchInvalid(HashTable * h);
