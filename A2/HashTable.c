/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
  
#include "HashTable.h"

/*Hashes a string
  PRE: hashtable and string to be hashed
  POST: integer hash
*/
int hashFunction(HashTable * h, char * toBeHashed)
{
    int sum =0;
    int i;
    for(i=0;i<strlen(toBeHashed);++i)
    {
        sum+=(int)toBeHashed[i];
    }
    return sum%h->size;
}

/*Checks if all elements in hashtable that are required have data*/
int searchInvalid(HashTable * h)
{
    int i;
    
    int n = h->size;
    for(i=0;i<n;++i)
    {
        if(!isRequired(h->table[i]))
        {
            return 0;
        }
    }
    return 1;
}

/*Frees all malloced memory taken up by the hashtable*/
void deleteTable(HashTable * toBeDeleted)
{
    int i;
    if(toBeDeleted)
    {
        if(toBeDeleted->table)
        {
            for(i=0;i<toBeDeleted->size;++i)
            {
                if(toBeDeleted->table[i])
                {
                    deleteList(toBeDeleted->table[i]);
                }
            }
            free(toBeDeleted->table);
        }
        free(toBeDeleted);
    }

}

/*Adds an element to the hashtable*/
void addToTable(HashTable * h, Node * data, int optional, int infoChecked)
{
    h->table[hashFunction(h,data->k)] = addToList(h->table[hashFunction(h,data->k)],data,optional,infoChecked); 
}

/*Checks if a certain key is in the hashtable*/
int isAlready(HashTable * h, char * check)
{
    int hash = hashFunction(h,check);
    LinkedList * checkFor = h->table[hash];
    if(search(checkFor,check))
    {
        return 1;
    }
    return 0;
}

/*Checks if the element in the hastable with key check is complete*/
int isAlreadyType(HashTable * h, char * check)
{
    int hash = hashFunction(h,check);
    LinkedList * checkFor = h->table[hash];
    if(searchIsChecked(checkFor,check))
    {
        return 1;
    }
    return 0;
}

/*Creates a new hashtable of length size*/
HashTable * createHashTable(int size)
{
    HashTable * t = (HashTable *)malloc(sizeof(HashTable));
    int i;
    if(!t)
    {

        return NULL;
    }
    t->size = size;
    t->table = malloc(sizeof(LinkedList *)*size);
    for(i=0;i<size;++i)
    {
        t->table[i]=NULL;
    }

    if(!t->table)
    {
        free(t);
        return NULL;
    }

    return t;

}

/*Gets data from hashtable with key, key*/
Node * get(HashTable * h, char * key)
{
    LinkedList * list = h->table[hashFunction(h,key)];
    return getFromList(list,key);
}
