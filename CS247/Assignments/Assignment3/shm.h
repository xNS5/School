// <Define an enum called StatusEnus with the enumerations "INVALID", "VALID" and "CONSUMED">
//
// <Define a typedef structure with the enum above and an "int" variable called "data">
#ifndef shm
#define shm
enum StatusEnus {INVALID, VALID, CONSUMED};

typedef struct Data{
    enum StatusEnus status;
    int data;
    int test;
  } Data;

#endif
