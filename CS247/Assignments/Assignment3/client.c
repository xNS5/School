
#include "shm.h"

<<<<<<< HEAD
/*
  Because these use the same memory object, I thought I'd move the header files to the shm.h file
*/
=======
// File name in header file with a #define in it? Okay cool I guess lol

>>>>>>> 832c40244bbe8ee1eb24bed9d77a1d820a237c99

#define handle_error_mod(msg) \
   perror(msg); strerror(errno); exit(EXIT_FAILURE);

<<<<<<< HEAD
int main(int argc, char* argv[]){
  int                   retVal = 0, counter = 0, size = sizeof(struct ShmData), fd = 0;
  const char*           name = "/shared_memory";
=======
int main (int argc, char* argv[]){
  int                   retVal = 0, counter = 0, fd = 0;
>>>>>>> 832c40244bbe8ee1eb24bed9d77a1d820a237c99
  struct ShmData        *shmPtr;

  /*
  The program doesn't really use any command line arguments, so I just check for whether
  the number of arguments exceeds 1, which in this case is just ./<filename>
  */

  if(argc != 1){
      printf("Usage: ./<filename>\r\n");
      exit(EXIT_FAILURE);
    }

  /*
    I have the shm_open in a while loop to check for whether the server created a shared object
    If it hasn't been created by the server (seeing as the client only has O_RDWR permissions
    instead of O_CREAT | O_RDWR -- which means if the shared memory object doesn't exist it creates one),
    it loops and waits for the server to create the shared memory object
  */
  fd = shm_open(name, O_RDWR, 0666);
  while(fd == -1)
  {
    fd = shm_open(name, O_RDWR, 0666);
    printf("Waiting on server...\r\n");
    counter++;
    sleep(1);
    if(counter == 30 && fd == -1)
      {
        printf("Client is unable to access the shared memory file. Exiting...\r\n");
        exit(EXIT_FAILURE);
      }

    }
  counter = 0; // Re-initializes the counter to zero.

  /*
    Casts the return value of mmap to the shared memory pointer
  */
  shmPtr =(ShmData*)mmap(NULL, size, PROT_WRITE, MAP_SHARED, fd, 0);
  if(shmPtr == MAP_FAILED){
      handle_error_mod("Mmap");
    }

  printf("[Client]: Waiting for valid data ...\n");

  /*
    If the client doesn't receive VALID in 30 seconds, it unmaps and quits.
  */
  while(shmPtr->status != VALID){
      printf("Server inactive\r\n");
      counter++;
      sleep(1);
      if(counter == 30 && shmPtr->status != VALID){
          printf("Client has been waiting for server for 30 seconds\r\nPlease check the status of the server\r\n");
          printf("Exiting...\r\n");
          retVal = munmap(shmPtr, size);
          if(retVal){
               handle_error_mod("Munmap");
             }
          exit(EXIT_FAILURE);
        }
  }
  printf("[Client]: Received %d\n",shmPtr->data);

  shmPtr->status = CONSUMED;

<<<<<<< HEAD
   retVal = munmap(shmPtr, size);
=======
   retVal = munmap(shmPtr, sizeof(struct ShmData));
>>>>>>> 832c40244bbe8ee1eb24bed9d77a1d820a237c99
   if(retVal){
        handle_error_mod("Munmap");
      }

  printf("[Client]: Client exiting...\n");

 /*
  I have my own error handling in this function which will exit if one of the system calls
  returns an error. Otherwise, retVal will remain 0 and will return normally.
 */

  return(retVal);
}
