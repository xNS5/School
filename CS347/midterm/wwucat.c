#include <fcntl.h>
#include <unistd.h>              
#include <string.h>
#include <stdio.h>

#define BUF_SIZE 32               /* Size of buffer */

#define FILE_NAME_LEN 200         /* Maximum length of file name */

/*
 * This function concatenates files 1...n added as command line arguments and prints to the console.
 * There are 5 checks done to ensure that the input is valid:
 * 1. Ensures that the program is run with at least 1 commend line argument.
 * 2. The inputted filename isn't too large (200+ length name).
 * 3. The inputted file(s) exist.
 * 4. The file is readable.
 * 5. The contents of the file(s) can be written to stdout.
 * Once all of the checks have been made, it iterates through the data at argv[i] and prints to stdout. Once that's
 * completed, it moves to the next argument.
 * I tested the program by changing the file permissions of the input files, and passing a nonexistent file as an argument.
 * */

int main(int argc, char *argv[])
{
    // If it has only 1 argument, being ./[compiled program].
    if(argc < 2){
        printf("Expecting 1 filename");
     } else {
        for(int i = 1; i < argc; i++){
            // If the filename is too large, the program terminates.
            if(strlen(argv[i]) > FILE_NAME_LEN){
                printf("Filename too large");
                return -1;
            } else {
                int oFile = open(argv[i], O_RDONLY);
                int file_data;
                // Checks to see if the file was able to be opened successfully.
                if (oFile != -1) {
                    if (read(oFile, &file_data, 1) != -1) {
                        //If the file input is valid
                        if(file_data != -1){
                            // Had to print the first char
                            printf("%c", file_data);
                            while (read(oFile, &file_data, 1)) {
                                // Continually checks for -1, which means error. Otherwise it prints out the value of the read char.
                                if(file_data == -1){
                                    printf("Error while writing to standard output\r\n");
                                    return -1;
                                } else {
                                    printf("%c", file_data);
                                }
                            }
                        }
                    } else {
                        printf("Error while reading the file\r\n");
                    }
                } else {
                    printf("Error opening the file for reading\r\n");
                }
            close(oFile);
        }
        }
    }
    return 0;

}

