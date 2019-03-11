// <Define an enum called StatusEnus with the enumerations "INVALID", "VALID" and "CONSUMED">
//
// <Define a typedef structure with the enum above and an "int" variable called "data">
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

enum StatusEnus {VALID,INVALID,CONSUMED};

typedef struct ShmData{
    enum StatusEnus status;
    int data;
  }ShmData;

#endif
