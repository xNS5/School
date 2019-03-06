#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/shm.h>
#include <errno.h>

#define handle_error_en(en, msg) \
   do {int errnum = errno; perror(msg); strerror(errnum); exit(EXIT_FAILURE);} while (0)

int main(int argc, char* argv[])
{
  int          retVal = 0;
  char *name = "/shm.h";
  //Use the POSIX "shm_open" API to open file descriptor with  "O_RDWR" options and the "0666" permissions>
  
  //shm_open returns positive on success
  retVal = shm_open(name, O_RDWR, 0666);
  if(retVal == -1)
    {
      handle_error_en(retVal, "SHM_Open");
      exit(0);
    }

  //Use the "mmap" API to memory map the file descriptor

  printf("[Client]: Waiting for valid data ...\n");

  // while(shmPtr->status != VALID)
  //   {
  //     sleep(1);
  //   }
  //
  // printf("[Client]: Received %d\n",shmPtr->data);
  //
  // shmPtr->status = CONSUMED;

   //<use the "munmap" API to unmap the pointer>

  printf("[Client]: Client exiting...\n");

  return(retVal);

}
