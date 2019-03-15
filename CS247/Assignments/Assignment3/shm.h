#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#ifndef shm
#define shm
#define name "/shared_memory"

enum StatusEnum {VALID,INVALID,CONSUMED};

typedef struct ShmData{
    enum StatusEnum status;
    int data;
  }ShmData;

#endif
