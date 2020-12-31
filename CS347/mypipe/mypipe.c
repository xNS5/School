/*****************************************************************************
*Write a program that executes two commands using a pipe*
*The two commands should be entered by the user as arguments enclosed by " " and separated by |, e.g. ./mypipe "command1 | command2"
*If no arguments are entered by the user, the program will assume command 1 is ls -l and command 2 is sort.
*The correctness of both commands is totally at the discretion of the user                           *
*The program should execute  the commands in pipe and show the output (if any)
*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>               /* strsep, etc. */
#include <ctype.h>                /* isspace, etc. */

#define MAX_NUM_ARGS 20           /* Maximum number of arguments allowed */
#define MAX_STR_LEN 200           /* Maximum string length */

int isSpaces(const char* word);

int main(int argc, char * argv[])
{
       int fd[2];                 /* Two ends of the pipe */
       char * lhs = NULL;         /* Left hand side command of the pipe */
       char * rhs = NULL;          /* Right hand side command of the pipe */
       char * lhscommand = "ls";  /* Default command name on left hand side of pipe */
       char * rhscommand = "sort"; /* Default command name on right hand side of pipe */
       char * lhsargs[MAX_NUM_ARGS] = { "ls", "-l", NULL };   /* Default LHS args */
       char * rhsargs[MAX_NUM_ARGS] = { "sort", NULL };       /* Default RHS args */


       /*Parse the user input to extract the commands and their arguments*/
       /*Hint: read about strsep(3) */
       char* found;
       char* found2 = strchr(argv[1], '|');
       char* string = argv[1];


       /*
        * Error handling for 3 cases:
        * 1. Argc > 2, which means an extra argument is passed other than the required arguments
        * 2. There isn't a | character in the command sequence
        * 3. The size of argv[1] is greater than 200 characters.
        * */
       if(argc != 2 || found2 == NULL || strlen(argv[1]) >= MAX_STR_LEN){
           fprintf(stderr, "Usage:\r\n ./mypipe [\"<LHS-command>|<RHS-command>\"]\r\n");
           exit(1);
       }

       /* Create the pipe and checking the return value
        * fd[0] is read end, fd[1] is write end
        * */
       if(pipe(fd) < 0){
           exit(1);
       }

       /*
        * Using strsep on the "|" character. If both sides of the pipe are empty, the default commands are used.
        * If the one side of the pipe isn't all spaces and the size of the string isn't zero it splits `found`
        * by the space and adds each argument to an index in the *hsargs arrays.
        * If the left hand side of the pipe is null or a space, it increments the `count` variable and moves on to
        * parsing the other side. If the other side is also empty, increments the counter and doesn't overwrite the
        * default commands.
       */
       for(int count = 0; (found = strsep(&string, "|")) != NULL;) {
           //If the `found` string isn't all spaces and the length of the string is not zero
           if (isSpaces(found) == 0 && strlen(found) != 0) {
               int j = 0;
               while(1) {
                   found2 = strsep(&found, " ");
                   // if *found2 is 0, that means there's a space before or after the word
                   // If found2 is null that means it reached the end of the argument
                   if((found2 == NULL || *found2 == 0) && j != 0){
                       //If count == 0, adds NULL to lhsargs[j], if count == 1, adds NULL to rhsargs[j]
                       count == 0 ? (lhsargs[j] = NULL) : (rhsargs[j] = NULL);
                       count++;
                       break;
                   } else if(*found2 != 0){
                       //If count == 0, adds the word to lhsargs[j], if count == 1, adds the word to rhsargs[j]
                       count == 0 ? (lhsargs[j] = found2) : (rhsargs[j] = found2);
                       j++;
                   }else{
                       //If found2 is a space at the beginning of one of the sides.
                       continue;
                   }
               }
           } else{
               count++;
           }
       }

       /*
        * Do the forking
        * I'll try not to fork up
        * */
       switch(fork()){
           //Catches error in fork
           case -1:
               fprintf(stderr, "Error in fork");
               exit(1);
           // Child
           case 0:
               dup2(fd[0], 0);
               close(fd[1]);
               execvp(rhsargs[0], rhsargs);
               break;
           // Parent
           default:
               dup2(fd[1], 1);
               close(fd[0]);
               execvp(lhsargs[0],lhsargs);
               break;
       }

}


/*
 * This function checks for whether the input is all spaces.
 * If there's an alphanumeric character, it returns 0;
 * otherwise it returns 1.
 * */
int isSpaces(const char* word){
    for(int i = 0; word[i] != '\0'; i++){
        if(isalpha(word[i])){
            return 0;
        }
    }
    return 1;
}
