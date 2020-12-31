/*
 * Implementation of functions that find values in strings.
 *****
 * YOU MAY NOT USE ANY FUNCTIONS FROM <string.h>
 *****
 */

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>

#include "find.h"

#define NOT_FOUND (-1)	// integer indicator for not found.

/*
 * Return the index of the first occurrence of <ch> in <string>,
 * or (-1) if the <ch> is not in <string>.
 * find_ch_index takes in a char array and a single char as parameters, searching for the first instance of `ch` in `string[]`.
 * This is a pretty simple function, where instead of checking for i less than some value (usually the length of the string)
 * this function checks for whether string[i] is the terminating character in the string array.
 */
int find_ch_index(char string[], char ch) {
    for(int i = 0; string[i] != '\0'; i++){
        if(string[i] == ch){
            return i;
        }
    }
	return NOT_FOUND;
}

/*
 * Return a pointer to the first occurrence of <ch> in <string>,
 * or NULL if the <ch> is not in <string>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 * find_ch_ptr takes in a pointer and a single char, accomplishing the same task as find_ch_index.
 * This function accomplishes the same task as find_ch_index, except instead of using an array it uses a pointer to compare to `ch`.
 */
char *find_ch_ptr(char *string, char ch) {
    while(*string){
        if(*string == ch) {
            return string;
        } else {
            string++;
        }
    }
    return NULL;	// placeholder
}

/*
 * Return the index of the first occurrence of any character in <stop>
 * in the given <string>, or (-1) if the <string> contains no character
 * in <stop>.
 *
 *
 */
int find_any_index(char string[], char stop[]) {
   for(int i = 0; string[i] != '\0'; i++){
       if(find_ch_index(stop, string[i]) != -1){
           return i;
       }
   }
	return NOT_FOUND;
}

/*
 * Return a pointer to the first occurrence of any character in <stop>
 * in the given <string> or NULL if the <string> contains no characters
 * in <stop>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 * find_any_ptr takes in two pointers, `string` and `stop`.
 * This function checks whether there is an occurrence of `stop` within `string`. I essentially accomplished this task
 * in find_ch_ptr, so instead of making extra variables and another while loop I opted to simply call find_ch_ptr
 */
char *find_any_ptr(char *string, char* stop) {
    while(*string){
        if(find_ch_ptr(stop, *string) != NULL){
            return string;
        }
        string++;
    }
	return NULL ;	// placeholder
}

/*
 * Return a pointer to the first character of the first occurrence of
 * <substr> in the given <string> or NULL if <substr> is not a substring
 * of <string>.
 * Note: An empty <substr> ("") matches *any* <string> at the <string>'s
 * start.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 * find_substr takes in two pointers, `string` and `substr`, trying to find whether there exists a substring `substr` within `string`.
 * This function checks to see if `string` contains `substr`. I first used find_ch_ptr again to locate the starting point of the substring within `string`.
 * Then, it checks for whether `substr` is empty. It then compares the start of `substr` and  a location within `string`. Finally, it checks to
 * see if the `substr` pointer has reached the end of the string. If it has, then `string` contains `substring` and the `start` pointer is returned.
 * Otherwise it returns NULL.
 */
char *find_substr(char *string, char* substr) {
    char *start= find_ch_ptr(string, *substr), *ptr1 = start;
    if(*substr == '\0') { // checking whether substr is empty
        return string;
    }
    while(*substr && *substr == *ptr1){ // continues if ptr2 isn't null and if the values of ptr1 and 2 match. Using substr to reduce memory allocation.
        ptr1++, substr++;
    }
    if(!*substr){ // If ptr2 has reached the end of substr
        return start;
    }
    return NULL;
}
