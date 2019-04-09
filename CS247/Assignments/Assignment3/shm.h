#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <string.h>
#include <errno.h>
#ifndef shm
#define shm

enum StatusEnus {VALID,INVALID,CONSUMED};

typedef struct ShmData{
    enum StatusEnus status;
    int data;
  }ShmData;

#endif
