#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include "shm.h"


int main (int argc, char* argv[])
{
  char*        name = "/shm";
  int          retVal = 0;
  int*         map;
  size_t       page_size = (size_t) sysconf (_SC_PAGESIZE);
  Data*         shmPtr;


  //Use the POSIX "shm_open" API to open file descriptor with  "O_RDWR" options and the "0666" permissions>
  //shm_open returns positive on success, -1 on error.
  retVal = shm_open(name, O_CREAT | O_RDWR, 0666);
  printf("retVal: %d\r\n", retVal);
  if(retVal == -1)
    {
      perror("SHM_Open");
      exit(EXIT_FAILURE);
    }

  //Use the "mmap" API to memory map the file descriptor
  map = mmap(0, page_size, PROT_READ, MAP_SHARED, retVal, 0);
  if(map == MAP_FAILED)
  {
    perror("mmap");
    exit(EXIT_FAILURE);
  }

  printf("[Client]: Waiting for valid data ...\n");

  while(shmPtr->status != VALID)
    {
      sleep(1);
    }

  printf("[Client]: Received %d\n",shmPtr->data);

  shmPtr->status = CONSUMED;

   //<use the "munmap" API to unmap the pointer>

  printf("[Client]: Client exiting...\n");

  return(retVal);

}
