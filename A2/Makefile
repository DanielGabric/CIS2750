#  Name: Daniel Gabric
#  USER ID: ******
#  Assignment #: 1
CC = gcc
CFLAGS =  -Wall -c
all: Dialogc
devbuild: libraries 
clean:
	@rm *.o *.class libpm.a libJNI_Layer.so

Dialogc: ParameterManager Dialog.java
# Assignments
ParameterManager: libraries

#Dependency:
#	export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:.
	
Dialog.java: Dialogc.java Compiler.java Layer.java
	javac Dialogc.java Compiler.java Layer.java
	
# Libraries
libraries: libpm.a JNI.so
#libJNI_Layer.so
libpm.a: ParameterManager.o ParameterList.o HashTable.o LinkedList.o
	ar cr libpm.a ParameterManager.o ParameterList.o HashTable.o LinkedList.o
	ranlib libpm.a 
#THIS IS FOR LAB COMPUTERS
#JNI.so: JNI_Layer.o
#	gcc -shared -Wl,-soname,libJNI_Layer.so -I/usr/lib/jvm/java-1.6.0-openjdk/include -I/usr/lib/jvm/java-1.6.0-openjdk/include/linux -o libJNI_Layer.so JNI_Layer.o -L. -lpm -fPIC
#JNI.so: JNI_Layer.o
#	gcc -shared -Wl,-soname,libJNI_Layer.so -I/System/Library/Frameworks/JavaVM.framework/Headers -I/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/include -o libJNI_Layer.so JNI_Layer.o
JNI.so: JNI_Layer.c Layer.h libpm.a
	gcc JNI_Layer.c -shared -Wl,-soname,libJNI_Layer.so -I/usr/lib/jvm/java-6-openjdk-amd64/include -I/usr/lib/jvm/java-6-openjdk-amd64/include/linux -o libJNI_Layer.so -L. -lpm -fPIC
#JNI.so: JNI_Layer.o libpm.a
#	gcc -shared -Wl,-soname,libJNI_Layer.so -I/usr/lib/jvm/java-6-openjdk-amd64/include -I/usr/lib/jvm/java-6-openjdk-amd64/include/linux -Iinclude  -o libJNI_Layer.so -L. -lpm JNI_Layer.o 

#libJNI_Layer.so:

# Test programs
#tests: testHash 
#testHash: testHash.c libraries
#	gcc $(CFLAGS) testHash.c -o runMe -L. -lpm
#a1example: a1example.c libraries
#	gcc $(CFLAGS) a1example.c -o run -L. -lpm

# libpm object files
ParameterManager.o: ParameterManager.c ParameterManager.h ParameterList.h Boolean.h HashTable.h
	gcc -c -Wall ParameterManager.c -fPIC
ParameterList.o: ParameterList.c ParameterList.h
	gcc -c -Wall ParameterList.c -fPIC
HashTable.o: HashTable.c HashTable.h LinkedList.h
	gcc -c -Wall HashTable.c -fPIC
LinkedList.o: LinkedList.c LinkedList.h
	gcc -c -Wall LinkedList.c -fPIC
JNI_Layer.o: JNI_Layer.c Layer.h 
	gcc -c -Iinclude -I/usr/lib/jvm/java-6-openjdk-amd64/include -I/usr/lib/jvm/java-6-openjdk-amd64/include/linux JNI_Layer.c -L. -lpm -fPIC
#JNI_Layer.o: JNI_Layer.c Layer.h 
#	gcc -c -Iinclude -I/System/Library/Frameworks/JavaVM.framework/Headers -I/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/include JNI_Layer.c -L. -lpm -fPIC
#THIS IS FOR LAB COMPUTERS
#JNI_Layer.o: JNI_Layer.c Layer.h 
#	gcc -c -Iinclude -I/usr/lib/jvm/java-1.6.0-openjdk/include -I/usr/lib/jvm/java-1.6.0-openjdk/include/linux JNI_Layer.c -L. -lpm -fPIC
