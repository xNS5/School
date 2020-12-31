#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define FALSE (0)
#define TRUE  (1)

int main() {
    // I created two int variables, `word` and `c`; `word` gets changed to 1 or 0 depending on whether or not there is a word, and `c` gets the numeric value of the letter from the input file.
    // Comparing ints is faster than comparing chars (if I remember correctly).
	int tot_chars = 0, tot_lines = 0, tot_words = 0, word = FALSE, c = 1;
	//If I could import  file and save it to a variable, I could use regular expressions to achieve similar results.
	while(c != EOF){
	    c = getchar();
	    // Checking if the ASCII value of the character is a symbol or a letter.
        if(c >= 33 && c <= 126){
            word = TRUE;
            tot_chars++;
        }
	    // ASCII value for "space"
	    if(c == 32){
	        tot_chars++;
	        // If the space follows a letter, it can be assumed that the letters created a word.
	        if(word == TRUE){
                word=FALSE;
	            tot_words++;
	        }
	    }
	    // ASCII value for newline, carriage return, tab, or new page
	    if(c >= 10 && c <= 13){
            tot_lines++;
	        tot_chars++;
	        // If the newline follows a letter, it can also be assumed that the letters created a word.
	        if(word == TRUE){
                word = FALSE;
	            tot_words++;
	        }
	    }
	}
	printf("%d  %d %d\r\n", tot_lines, tot_words, tot_chars);
	return 0 ;
}

