Name: Daniel Gabric
USER ID: ******
Assignment #: 2

Dialogc.java - This is the class that deals with the IDE for the pseudo - C
Layer.java/JNI_Layer.c/Layer.h - These deal with JNI, and serve as the layer in between C and Java
Compiler.java - This class just deals with the compiling of the pseudo - code to java, and compiling/running of that java code
ParameterManager.c/ParameterManager.h - ParameterManager as specified in the assignment description
ParameterList.c/ParameterList.h - ParameterList as specified in the assignment description, also other types defined because it is the base header in the chain
LinkedList.c/LinkedList.h - LinkedList to support chaining in the hashtable
HashTable.c/HashTable.h - Contains definition of hashtable type and operations on the hashtable
Boolean.h - contains Boolean enumeration
{AddListener.java 
 DeleteListener.java
 UpdateListener.java
 QueryListener.java
 }-these are just pre-implemented for the example on 

A1 - Notes:
-strings cannot have double quotes inside them
-string cannot have semicolons in them
-string can have comment character in it but it only if you continue real string on nextline
-parameters are alphanumeric with underscore
-parameters that are not alphanumeric and don't have underscores will give parse errors
-lists consist of strings delimitted by commas and have curly braces around the whole thing
-anything that deviates from the spec and my assumptions is a parse error

A2 - Notes:
- in the makefile change the jni path if you're not in the thorn lab
- Before you run java Dialogc you need to enter this in the shell 'export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:.'
- When you compile and run code and you have buttons, YOU the user need to implement them in the working directory or else it will prompt you with an error
-

