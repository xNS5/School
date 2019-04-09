#include "shm.h"

/*
  Because these use the same memory object, I thought I'd move the header files to the shm.h file
*/

#define handle_error_mod(msg) \
   perror(msg); strerror(errno); exit(EXIT_FAILURE);

int main(int argc, char* argv[]){
	int 								  retVal = 0, counter = 0, size = sizeof(struct ShmData), fd = 0;
	const char* 				  name = "/shared_memory";
	struct ShmData		    *shmPtr;

/*
  First, the function checks for whether the input is a valid integer.
  If atoi receives a char or string, it'll return zero.
  Assuming atoi returns 0 and the length of the string is greater than or equal to one
  it'll simply error out.
  I wasn't sure whether I should be checking for hex input, so I erred on the side of caution
  and made it strictly integer values.
*/
	if(argc != 2 || (atoi(argv[1]) == 0 && strlen(argv[1])>=1)){
			printf("Usage: ./<filename>   <int data>\r\n");
			exit(EXIT_FAILURE);
		}

	fd = shm_open(name, O_CREAT | O_RDWR, 0666);
  if(fd == -1){
      handle_error_mod("Shm_open");
    }

	retVal = ftruncate(fd, size);
	if(retVal){
			handle_error_mod("Ftruncate");
		}

  /*
  Casts the return from mmap to the shared memory pointer.
  */
	shmPtr=(ShmData*)mmap(NULL, size, PROT_WRITE, MAP_SHARED, fd, 0);
	if(shmPtr == MAP_FAILED){
			handle_error_mod("Mmap");
	}
  shmPtr->status = INVALID;
  shmPtr->data = atoi(argv[1]);
	shmPtr->status = VALID;

  printf("[Server]: Server data Valid... waiting for client\n");

  /*
    In the while loop, the server waits for the client. If the server is inactive for
    30 seconds, it quits.
  */
  while(shmPtr->status != CONSUMED){
      printf("Client inactive\r\n");
      counter++;
      sleep(1);
      if(counter == 30 && shmPtr->status != CONSUMED){
          printf("Server has been waiting for the client for 30 seconds\r\nPlease check the status of the client\r\n");
          printf("Exiting...\r\n");
          /*
          If for some reason client isn't active to receive the data, it will unmap and unlink
          all of the files and pointers. If this didn't happen, the client would still be able to
          see the leftover input from the server side.
          */
          retVal = munmap(shmPtr, size);
        	if(retVal){
        			handle_error_mod("Mmunmap");
        	}
        	retVal = close(fd);
        	if (retVal){
        			handle_error_mod("Close");
        	}
        	retVal = shm_unlink(name);
        	if(retVal){
        			handle_error_mod("Unlink");
        	}
          exit(EXIT_FAILURE);
        }
    }

  printf("[Server]: Server Data consumed!\n");

	retVal = munmap(shmPtr, size);
	if(retVal){
			handle_error_mod("Mmunmap");
	}
	retVal = close(fd);
	if (retVal){
			handle_error_mod("Close");
	}
	retVal = shm_unlink(name);
	if(retVal){
			handle_error_mod("Unlink");
	}
  printf("[Server]: Server exiting...\n");
  retVal = 0;

  return(retVal);
}
