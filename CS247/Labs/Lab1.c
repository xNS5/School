/*

		Course: CS 247 -- Winter 2018
		File Name: Lab1.c

		Preprocessor -- The preprocessor includes the <stdio.h> and <string.h> and includes them in this file before it gets shipped off to the compiler.
		Forward declarations -- In order for the compiler to cross check the parameters being passed to the function and the return value, I need to declare functions at the top of the program.
		Macros -- A macro is used whenever I use #define. That's defining a variable that substitutes for the value after the variable name whenever it's declared in the program.
		Escape character ('/') -- are used a couple of times in this program. \r is a Carriage Return which can be used to bring the cursor to the front of the line. \n is a new line.
							In this program, \" is used to add quotation marks in the strings without the program thinking "Oh, they're trying to end the string!".
		Operators -- sizeof() is an operator that is created when the program is compiled that retuns the size of whatever is passed to it. A few other operators are \, +, % that all execute some sort of
							mathmatical function.
		Keywords -- Keywords define variable types, e.g. "int x", "char c". In this program DataTypeSizes takes in void and returns void.
		printf -- In this program, printf is used to print out a string with %s and %ld. Printf is a function included in <stdio.h> that prints things to the console.


*/

#include <stdio.h>
#include <string.h>

#define CHAR_DATA_TYPE		"char"
#define INT_DATA_TYPE 		"int"
#define LONG_DATA_TYPE		"long int"
#define SHORT_DATA_TYPE		"short char"
#define U_LONG_DATA_TYPE 	"unsigned long int"
#define S_LONG_DATA_TYPE 	"signed long char"
#define FLOAT_DATA_TYPE "signed float"

void DataTypeSizes(void);
void DisplayString(const char*);
void DisplayArgToString(const char*, int);

int main(int argc, char* argv[])
{
		DataTypeSizes();
		char str[100];
		printf("String input: ");
		fgets(str, 100, stdin);
		DisplayString(str);
		printf("argc is: %i\r\n", argc);
		printf("Every element in Aargv is: ");

		for(int i = 0; i < argc; i++)
			{
				printf(" %s", argv[i]);
			}
			printf("\n");
		return 0;
}

void DataTypeSizes()
{
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",CHAR_DATA_TYPE, CHAR_DATA_TYPE, sizeof(char));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",INT_DATA_TYPE , INT_DATA_TYPE , sizeof(int));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",LONG_DATA_TYPE , LONG_DATA_TYPE	 , sizeof(long int));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",SHORT_DATA_TYPE , SHORT_DATA_TYPE , sizeof(unsigned char));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",U_LONG_DATA_TYPE , U_LONG_DATA_TYPE , sizeof(unsigned long int));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",S_LONG_DATA_TYPE , S_LONG_DATA_TYPE , sizeof(signed char));
	printf("\"%s\" is a standard C datatype. Size of a \"%s\" data type is %ld\r\n",FLOAT_DATA_TYPE , FLOAT_DATA_TYPE , sizeof(float));

}

void DisplayString(const char* str)
{
	printf("String is: %s\r\n", str);
}
