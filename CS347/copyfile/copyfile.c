#include <stdio.h>                /* Standard library for file operations */
#include <string.h>               /* String functions (strcat) */
#define BUF_SIZE 32               /* Size of buffer */


 
int main(int argc, char *argv[])
{
       FILE * file_to_read;        // File to be copied
       FILE * file_to_write;       // Copy of the file
       
       char buf[BUF_SIZE];        // Buffer to hold characters read
       size_t num_rec;            // Number of records to be read/written

       // Check if correct number of arguments are supplied
       if(argc < 2 || argc > 3){
           printf("Error: Incorrect number of arguments.\r\nFormat: ./[executable] [input file] [optional output file]");
           return -1;
       } else {
           // Open source file in read-only mode
           // Prepare the source file name
           file_to_read = fopen(argv[1], "rb");

           if(file_to_read == NULL){
               printf("Error: File does not exist.\r\nPlease try again.");
               return -1;
           } else {
               // Prepare the target file name
               // Open target file in write mode
               file_to_write = fopen((argc == 3 ? argv[2] : strcat(argv[1], ".output")), "wb");
               if(file_to_write == NULL){
                   printf("Null!\r\n");
                   return -1;
               }
               while(1){
                   num_rec = fread(buf, 1, BUF_SIZE, file_to_read);
                   if(num_rec == 0){
                       break;
                   } else {
                       fwrite(buf, 1, num_rec, file_to_write);
                   }

               }
               fclose(file_to_read);
               fclose(file_to_write);
               }
       }
       return 0;
}

