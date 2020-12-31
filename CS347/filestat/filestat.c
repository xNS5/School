#include <unistd.h>        /* F_OK, STDIN_FILENO, STDERR_FILENO, etc. */
#include <sys/stat.h>      /* struct stat */
#include <sys/types.h>     /* S_IFMT */
#include <stdio.h>         /* fprintf, printf, sprintf, etc. */
#include <stdlib.h>        /* exit, etc. */
#include <time.h>          /* ctime */

int main(int argc, char *argv[])
{
    mode_t file_perm;          /* File permissions */
    struct stat file_details;  /* Detailed file info */

    if(argc != 2){
        printf("Incorrect number of arguments.\r\n");
        return -1;
    }

    /* Retrieve the file details */
    stat(argv[1], &file_details);
    file_perm = file_details.st_mode; // setting the file permissions to a variable so I don't have to keep having to access the struct.
    
    /* Get the file type */
    printf("File type: %s\r\n",(S_ISDIR(file_perm) ? "Directory" : "Regular")); // Printing out whether it's a directory or a regular file

    /* Get the time of last access of the file */
    printf("File access time: %s", ctime(&file_details.st_atime));

    /* Get the file permissions */
    // Checks for S_IR[group], S_IW[grop], and S_IX[group] for all user types.
    printf("File Permissions:\n");
    printf("User: %s, %s, %s\r\n", (file_perm & S_IRUSR ? "Readable" : "Not readable"), (file_perm & S_IWUSR ? "Writable" : "Not writable"), (file_perm & S_IXUSR ? "Executable" : "Not executable"));
    printf("Group: %s, %s, %s\r\n", (file_perm, S_IRGRP ? "Readable" : "Not readable"), (file_perm & S_IWGRP ? "Writable" : "Not writable"), (file_perm & S_IXGRP ? "Executable" : "Not executable"));
    printf("Other: %s, %s, %s\r\n", (file_perm & S_IROTH ? "Readable" : "Not readable"), (file_perm & S_IWOTH ? "Writable" : "Not writable"), (file_perm & S_IXOTH ? "Executable" : "Not executable"));

    
}
