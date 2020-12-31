#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <regex.h>

int main(int argc, char** argv) {
    // Checks to see if the command is -d or -e. If it's neither, it returns true and exits.
    if (argc != 3 || strcmp(argv[1], "-e") != 0 && strcmp(argv[1], "-d") != 0) {
        printf("Error: incorrect format. Please use \"-e\" or \"-d\".\r\nExample: ./[compiled program] -d [file name]_enc.txt\r\n");
        return -1;
    } else {
        // Assigning arguments to variables
        char outputFile[128];
        char *command = argv[1];
        char *file = argv[2];
        int pFile = open(file, O_RDONLY); // Open the file
        char *delim; // Declaring the delimiter as a char*
        regex_t regex; // creating a regex pattern
        int file_input; // getting the file input

        // If the file cannot be found, exits.
        if (pFile == -1) {
            printf("Error: invalid input file. Please ensure the input file is spelled correctly");
            return -1;
        }

        // Creating a regex to see whether the inputted file matches the pattern, determining the delimiter.
        regcomp(&regex, "_", 0);
        if(regexec(&regex, file,  0, NULL, 0) == 0){
            delim = "_";
        } else {
            delim = ".";
        }
        // I moved this outside of both if statement blocks to reduce repeating code
        strcpy(outputFile, file);
        strtok(outputFile, delim);

        // Encrypting the file
        if (strcmp(command, "-e") == 0) {
            strcat(outputFile, "_enc.txt");
            int oFile = open(outputFile, O_CREAT | O_RDWR , 0744); // Opening the file with 0744 permissions because I want the creator to have read + write + execute, but the rest will only have read.

            // While it still gets input from the file, repeat the loop
            while (read(pFile, &file_input, 1) == 1) {
                char scrambled = (char) file_input + 100; // Casting the file_input value + 100 as a char
                write(oFile, &scrambled, 1);
            }
            close(oFile);
            close(pFile);
        }

        // Decrypting the file
        if (strcmp(command, "-d") == 0) {
            strcat(outputFile, "_dec.txt");
            int oFile = open(outputFile, O_CREAT | O_RDWR, 0744);

            // Repeats the loop while it still gets input.
            while (read(pFile, &file_input, 1) == 1) {
                char scrambled = (char) file_input - 100; // Casting the file_input value - 100 as a char
                write(oFile, &scrambled, 1);
            }
            close(oFile);
            close(pFile);
        }
    }
    return 0;
}
