/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 1
*/
  
#include "ParameterManager.h"
/*Creates a new parameter manager object
PRE: size is a positive integer 
POST: Returns a new parameter manager object initialized to be empty (i.e. managing no parameters) on success, NULL otherwise (memory allocation failure)*/
ParameterManager * PM_create(int size)
{
    ParameterManager * root;
    root= PM_createNode();
    if(!(root) || size <= 0)
    {
        return NULL;
    }

    root->t = createHashTable(size);
    if(!root->t)
    {
        return NULL;
    }

    return root;
}

/*Allocates memory for a new parameter manager and returns the new parametermanager*/
ParameterManager * PM_createNode()
{
    ParameterManager * node;
    node = (ParameterManager *)malloc(sizeof(ParameterManager));
    if(!node)
    {
        return NULL;
    }
    return node;
}

/*Destroys a parameter manager object
PRE: n/a
POST: all memory associated with parameter manager p is freed; returns 1 on success, 0 otherwise*/
int PM_destroy(ParameterManager *p)
{
    if(p == NULL)
    {
        return 0;
    }
    deleteTable(p->t);
    free(p);
    return TRUE;
}

/*Extract values for parameters from an input stream
PRE: fp is a valid input stream ready for reading that contains the desired parameters
POST: All required parameters, and those optional parameters present, are assigned values 
that are consumed from fp, respecting comment as a "start of comment to end of line" character 
if not nul ('\0'); returns non-zero on success, 0 otherwise (parse error,memory allocation failure)*/
/*white space == ' ' && '\n' && '\t'*/
int PM_parseFrom(ParameterManager *p, FILE *fp, char comment)
{


    int i=0;
    int j=0;

    char in;
    BOOLEAN isComment = FALSE;
    BOOLEAN isAfterEquals = FALSE;
    /*Set to false initially*/
    BOOLEAN isAValidKey = TRUE;
    int n = INITIAL;
    int m = INITIAL;
    char * parameters = (char *)malloc(sizeof(char)*INITIAL);
    memset(parameters,0,INITIAL);
    BOOLEAN returnVal = TRUE;
    char * info = (char *)malloc(sizeof(char)*INITIAL);
    memset(info,0,INITIAL);
    param_t whichIsIt = NULL_TYPE;
    /*Information * ha;*/
    Node * this;
    /*LinkedList * list;*/
    BOOLEAN ignore = FALSE;
    BOOLEAN wasSpace = FALSE;
    BOOLEAN isChar = FALSE;


    if(fp == NULL || !parameters || !info)
    {

        return PARSE_ERROR;
    }
    
    while(1)
    {

        in = getc(fp);

        if(in == EOF&&!isAfterEquals)
        {
            returnVal = TRUE;
            break;
        }
        else if(in == EOF &&isAfterEquals)
        {
            returnVal = PARSE_ERROR;
            break;
        }

        /*Marks start of a comment*/
        if(in == comment)isComment = TRUE;
        /*Marks end of a comment*/
        if(in == '\n')isComment = FALSE;

        if(isSpace(in)&&isChar)
        {
            wasSpace = TRUE;
        }

        /*Skips iteration if whitespace or comment*/
        if((isSpace(in)||isComment)&&!ignore)
        {

            continue;
        }
        

        /*If not whitespace*/
        if(!isComment)
        {   

            /*If there are characters before an equals sign*/
            if(!isAfterEquals)
            {

                /*list = NULL;*/
                this = NULL;
                /*ha = NULL;*/

                if(!isGood(in))
                {

                    returnVal = PARSE_ERROR;
                    break;
                }
                if(wasSpace && isChar && in != '=')
                {
                    returnVal = PARSE_ERROR;
                    break;
                }
                isChar = TRUE;



                /*If an equals character is read check if the key that has been managed*/
                if(in=='=')
                {
                    
                    /*If the parameter has been managed proceed and set the isAValid key to true*/
                    if(hasBeenManaged(p,parameters))
                    {
                        isAValidKey = TRUE;

                        this = get(p->t,parameters);

                        whichIsIt = this->type;


                    }
                    /*printf("%s\n",parameters);*/
                    i=0;
                    isAfterEquals = TRUE;
                    isChar = FALSE;
                wasSpace = FALSE;
                    continue;
                }
                
                parameters[i++]=in;
                parameters[i]='\0';

                /*Extending memory in case of overflow*/
                if(i==INITIAL)
                {
                    n+=20;
                    parameters = (char *)realloc(parameters,n);
                }    

                
            }
            /*If there are characters after an equals sign*/

            else
            {
                if(!isSpace(in)&&!ignore&&(whichIsIt == STRING_TYPE||whichIsIt==LIST_TYPE))
                {
                    ignore = TRUE;
                }
                if(!isGoodAEquals(in)&&!ignore)
                {  
           
                    returnVal = PARSE_ERROR;
                    break;
                }
                
                /*If the input character is a semi colon, log data if it has a valid key*/
                if(in ==';')
                {
                    /*ignore = FALSE;
                    isAfterEquals = FALSE;*/

                   
                    if(!isAValidKey)
                    {
                        if((!isInt(info) && !isReal(info)&& !isBoolean(info)&&!isListType(info)&&!isString(info)))
                        {
                            returnVal = PARSE_ERROR;
                            break;
                        }
                        j=0;
                        ignore = FALSE;
                        isAfterEquals=FALSE;
                        continue;
                    }
                    else
                    {
                        
                        if(PM_hasValue(p,parameters))
                        {
                            
                            returnVal = PARSE_ERROR;
                            break;
                        }
                       /* printf("%s\n",info);*/
                        if(whichIsIt == INT_TYPE)
                        {

                            if(!isInt(info))
                            {
                                
                                returnVal = PARSE_ERROR;
                                break;
                            }
                            p->t->table[hashFunction(p->t,parameters)]=addToList(p->t->table[hashFunction(p->t,parameters)],this,-1,1);
                            this->pval.int_val = strToInt(info);

                        }
                        else if(whichIsIt == REAL_TYPE)
                        {
                            if(!isReal(info))
                            {
                                
                                returnVal = PARSE_ERROR;
                                break;
                            }
                            p->t->table[hashFunction(p->t,parameters)]=addToList(p->t->table[hashFunction(p->t,parameters)],this,-1,1);
                            this->pval.real_val = strToFloat(info);
                        }
                        else if(whichIsIt == BOOLEAN_TYPE)
                        {
                            /*printf("%s\n",parameters);*/
                            if(!isBoolean(info))
                            {
                      
                                returnVal = PARSE_ERROR;
                                break;
                            }
                            p->t->table[hashFunction(p->t,parameters)]=addToList(p->t->table[hashFunction(p->t,parameters)],this,-1,1);
                            this->pval.bool_val = strToBoolean(info);
                        }
                        else if(whichIsIt == STRING_TYPE)
                        {
                            int length = strlen(info)-1;
                            for(;length >=0;--length)
                            {
                                if(!isSpace(info[length]))
                                {
                                    break;
                                }
                                info[length]='\0';
                            }
                            if(!isString(info))
                            {
                      
                                returnVal = PARSE_ERROR;
                                break;
                            }
                            p->t->table[hashFunction(p->t,parameters)]=addToList(p->t->table[hashFunction(p->t,parameters)],this,-1,1);
                            this->pval.str_val = strToStr(info);
                            /*list->hidden = ha->pval.str_val;
*/
                        }
                        else if(whichIsIt == LIST_TYPE)
                        {
                            int length = strlen(info)-1;
                            for(;length >=0;--length)
                            {
                                if(!isSpace(info[length]))
                                {
                                    break;
                                }
                                info[length]='\0';
                            }
                            int size;
                            if(!(size=isListType(info)))
                            {

                                returnVal = PARSE_ERROR;
                                break;
                            }
                            p->t->table[hashFunction(p->t,parameters)]=addToList(p->t->table[hashFunction(p->t,parameters)],this,-1,1);
                            this->pval.list_val = strToList(info,size);
                           /* list->paramList = ha->pval.list_val;
                    */
                        }
                        isChar = FALSE;
                        wasSpace = FALSE;
                        isAfterEquals = FALSE;
                        /*whichIsIt = NULL_TYPE;
*/
                    }
                    /*Handle adding data*/
                    j=0;
                    memset(info,0,m);
                    memset(parameters,0,n);
                    isAValidKey = FALSE;
                    ignore = FALSE;
                    whichIsIt = NULL_TYPE;

                    /*If a semi-colon is hit clear the isAValidKey and ignore flags and continue the loop so no more characters are read*/
                    continue;
                }

                isChar = TRUE;
                if(isChar && wasSpace && whichIsIt != STRING_TYPE&&whichIsIt != LIST_TYPE)
                {
                    returnVal = PARSE_ERROR;
                    break;
                }
                info[j++]=in;
                info[j]='\0';
                /*Extending memory incase of overflow*/
                if(j>=INITIAL)
                {
                    m+=20;
                    info = (char *)realloc(info,m);
                }    
            }

        }
    
    }

    free(info);
    free(parameters);
    /*If there is a managed key that is required and has no data, PARSE_ERROR*/
    if(!searchInvalid(p->t))
    {
        /*PM_destroy(p);*/
        return PARSE_ERROR;
    }

    /*If the returnVal has been set to false destroy all memory*/
    /*if(!returnVal)
    {
        PM_destroy(p);
    }*/
    return returnVal;
    
}

/*Turns string into parameter list
  PRE: n/a
  POST: valid parameterlist object*/
ParameterList * strToList(char *c, int size)
{

    int i;
    int j;
    BOOLEAN isStr = FALSE;
    int n = strlen(c);
    char * temp = malloc(sizeof(char)*n); 
    memset(temp,'\0',n);
    ParameterList * p = createPL(size);
    if(n==2)
    {
        free(temp);
        return p;
    }

    int counter = 0;
    int count = 0;
    for(i=1;i<strlen(c);++i)
    {

        if(c[i]=='"')
        {
            temp[counter++]=c[i];
            temp[counter]='\0';
            isStr = !isStr;
            continue;
        }
        if(isStr)
        {
            /*printf("%c\n",c[i]);*/
            temp[counter++]=c[i];
            temp[counter]='\0';
    
        }
        else
        {
            if(isSpace(c[i]))continue;
            j=strlen(temp);
            p->array[count]=malloc(sizeof(char)*(j+1));
            
            strncpy(p->array[count],&temp[1],j);
            p->array[count][j-2]='\0';
            
            count++;
            counter=0;
            memset(temp,0,n);
        }
    }
    free(temp);
    return p;
}

/*allocates new memory for string and returns new address*/
char * strToStr(char *c)
{
    int size = strlen(c);
    char * newChar = malloc(sizeof(char)*(size+1));
    strncpy(newChar,&c[1],size);
    newChar[size-2]='\0';
    return newChar;
}

/*returns boolean enum value depending on the value of string c*/
Boolean strToBoolean(char * c)
{
    if(!strcmp(c,"true"))
    {
        return true;
    }
    else 
    {
        return false;
    }
}

/*Self explanatory*/
BOOLEAN isSpace(char c)
{
    return c == '\n'||c ==' '|| c=='\t'||c=='\r';
}

/*Self explanatory*/
BOOLEAN isInt(char * c)
{
    int i;
    if(c[0]=='+'||c[0]=='-')
    {
        i=1;
    }
    else
    {
        i=0;
    }
    for(;i<strlen(c);++i)
    {
        if(c[i]<'0'||c[i]>'9')
        {
            return FALSE;
        }
    }
    return TRUE;
}

/*Self explanatory*/
BOOLEAN isReal(char * c)
{
    int numDots=0;
    int i;
    if(c[0]=='+'||c[0]=='-')
    {
        i=1;
    }
    else
    {
        i=0;
    }
    for(;i<strlen(c);++i)
    {
        if(c[i]=='.'&&numDots > 0)
        {
            return FALSE;
        }
        if(c[i]=='.')
        {
            numDots++;
        }
        if((c[i]>'9'||c[i]<'0')&&c[i]!='.')
        {
            return FALSE;
        }

    }
    return TRUE;
}

/*Self explanatory*/
BOOLEAN isString(char * c)
{

    if(c[0]!='\"'||c[strlen(c)-1]!='\"')
    {
        return FALSE;
    }
    int i=1;
    for(;i<strlen(c)-1;++i)
    {
        if(c[i]=='\"')
        {
            return FALSE;
        }
    }
    return TRUE;
}

/*Self explanatory*/
BOOLEAN isBoolean(char * c)
{
    if(strcmp(c,"false")&&strcmp(c,"true"))
    {
        return FALSE;
    }
    return TRUE;
}

/*Self explanatory*/
BOOLEAN isListType(char * c)
{
   
    int i;
    int size = 0;
    int amountOfStrings = 0;
    if(strlen(c)<2)
    {

        return FALSE;
    }

    if(c[0]!='{'||c[strlen(c)-1]!='}')
    {

        return FALSE;
    }


    BOOLEAN isStr = FALSE;
    BOOLEAN isComma = FALSE;
    int m=0;

    for(i=1;i<strlen(c)-1;++i)
    {
    
        if(c[i]=='"')
        {
            isComma = FALSE;
            isStr = !isStr;
            if(!isStr)
            {

                amountOfStrings++;
            }
            else
            {
                if(!m&&amountOfStrings)
                {
               
                    return FALSE;
                }
            }
            continue;
        }

        if(isStr)
        {
            m=0;
            continue;
        }
        else
        {
            if(c[i]!=','&&!isSpace(c[i]))
            {
               
               return FALSE;
            }
            
            if(c[i]==','&&!amountOfStrings)
            {
               
                return FALSE;
            }

            if(c[i]==',')
            {
                isComma = TRUE;
                size++;
                m++;
            }

            if(m>1)
            {
               
                return FALSE;
            }
        }

    }
    
    if(isStr || isComma)
    {
       
        return FALSE;
    }
    return size+2;
}

/*Checks if input is good before equals*/
BOOLEAN isGood(char c)
{
    return (c>='a'&&c <='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')||c=='_'||c=='=';
}

/*Checks if input is good after equals*/
BOOLEAN isGoodAEquals(char c)
{
    return isGood(c)||(c==';'||c=='"'||c=='{'||c=='}'||c==','||c=='+'||c=='-'||c=='.');
}

/*Checks whether a key has been managed*/
BOOLEAN hasBeenManaged(ParameterManager *p, char * key)
{
    if(isAlready(p->t,key))
    {
        return TRUE;
    }
    return FALSE;
}

/*Self explanatory*/
int strToInt(char *num)
{
    int i;
    int sum = 0;
    int multiplier = 1;
    if(num[0]=='-')
    {
        i=1;
        multiplier = -1;
    }
    else if(num[0]=='+')
    {
        i=1;
    }
    else
    {
        i=0;
    }
    for(;i<strlen(num);++i)
    {
        sum *=10;
        sum+=(num[i]-'0');
    }
    return multiplier*sum;
}

/*Self explanatory*/
float strToFloat(char * num)
{
    float real_val=0;
    float multiplier = num[0]=='-'?-1.0:1.0;
    if(num[0]=='-'||num[0]=='+')
    {
        real_val=atof(&num[1]);
    }
    else
    {
        real_val=atof(&num[0]);
    }
    
    return multiplier*real_val;
}

/*Register parameter for management
PRE: pname does not duplicate the name of a parameter already managed
POST: Parameter named pname of type ptype is registered with p as a parameter; 
if required is zero the parameter will be considered optional, otherwise it will be considered required; 
returns 1 on success, 0 otherwise (duplicate name, memory allocation failure)*/
int PM_manage(ParameterManager *p, char *pname, param_t ptype, int required)
{
    HashTable * h = p->t;
    int len = strlen(pname);


    if(isAlready(h,pname))
    {
        return FALSE;
    }

    Node * n = malloc(sizeof(Node));

    /*Information * l = malloc(sizeof(Information));
*/

    n->pval.str_val = NULL;
    n->type = ptype;
    n->isParam = 0;
    n->isStr=0;
    if(ptype == STRING_TYPE)
    {
        n->isStr=1;
    }
    else if(ptype == LIST_TYPE)
    {
        n->isParam = 1;
    }
    /*n->information = l;
*/
    n->k = malloc(sizeof(char)*(len+1));


    strncpy(n->k,pname,len);
    n->k[len]='\0';

    addToTable(h,n,required,0);
    h->table[hashFunction(h,pname)]=addToList(h->table[hashFunction(p->t,pname)],n,-1,0);
    return TRUE;
}

/*Test if a parameter has been assigned a value
PRE: pname is currently managed by p
POST: Returns 1 if pname has been assigned a value, 0 otherwise (no value, unknown parameter)*/
union param_value PM_getValue(ParameterManager *p, char *pname)
{
    HashTable * h = p->t;
    Node * temp = get(h,pname);
    /*Information * val = temp->information;*/
    return temp->pval;
}

/*Obtain the value assigned to pname
PRE: pname is currently managed by p and has been assigned a value
POST: Returns the value assigned to pname; result is undefined if pname has not been assigned a value or is unknown*/
int PM_hasValue(ParameterManager *p, char *pname)
{
    HashTable * h = p->t;
    if(isAlreadyType(h,pname))
    {
        return 1;
    }
    return 0;
}


/*Hashtable that maps, variable names to its parameters/values*/
