#  Name: Daniel Gabric
#  USER ID: ******
#  Assignment #: 1
CFLAGS = -g -Wall
all: ParameterManager
devbuild: libraries 
clean:
	@rm *.o libpm.a #runMe #run

# Assignments
ParameterManager: libpm.a
	
	
# Libraries
libraries: libpm.a
libpm.a: ParameterManager.o ParameterList.o HashTable.o LinkedList.o
	ar cr libpm.a ParameterManager.o ParameterList.o HashTable.o LinkedList.o
	ranlib libpm.a

# Test programs
#tests: testHash 
#testHash: testHash.c libraries
#	gcc $(CFLAGS) testHash.c -o runMe -L. -lpm
#a1example: a1example.c libraries
#	gcc $(CFLAGS) a1example.c -o run -L. -lpm

# libpm object files
ParameterManager.o: ParameterManager.c ParameterManager.h ParameterList.h Boolean.h HashTable.h
	gcc $(CFLAGS) ParameterManager.c -c
ParameterList.o: ParameterList.c ParameterList.h
	gcc $(CFLAGS) ParameterList.c -c
HashTable.o: HashTable.c HashTable.h LinkedList.h
	gcc $(CFLAGS) HashTable.c -c
LinkedList.o: LinkedList.c LinkedList.h
	gcc $(CFLAGS) LinkedList.c -c

