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
  const char*   name = argv[1];
  int           retVal;
  int           fd = 0;
  struct stat   st;
  size_t        size;
  void*         map;
  Data*         shmPtr;

  if(argc != 2)
    {
      printf("Usage: ./<filename>   <shared memory object file>\r\n");
      exit(EXIT_FAILURE);
    }

  //Use the POSIX "shm_open" API to open file descriptor with  "O_RDWR" options and the "0666" permissions>
  //shm_open returns positive on success, -1 on error.
  fd = shm_open(name, O_CREAT | O_RDWR, 0666);
  if(fd == -1)
    {
      perror("SHM_Open");
      exit(EXIT_FAILURE);
    }

  retVal = stat(name, &st); // to get the offset for mmap
  if(retVal)
    {
      perror("Stat");
      exit(EXIT_FAILURE);
    }
  size = st.st_size;
  //Use the "mmap" API to memory map the file descriptor
  map = mmap(NULL, size, PROT_READ, MAP_SHARED, fd, 0);
  if(map == MAP_FAILED)
  {
    perror("mmap");
    exit(EXIT_FAILURE);
  }

  printf("[Client]: Waiting for valid data ...\n");

  // while(shmPtr->status != VALID)
  //   {
  //     sleep(1);
  //   }

  // printf("[Client]: Received %d\n",shmPtr->data);
  //
  // shmPtr->status = CONSUMED;

   //<use the "munmap" API to unmap the pointer>

  printf("[Client]: Client exiting...\n");

  return(retVal);

}
