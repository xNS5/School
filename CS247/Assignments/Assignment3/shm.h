// <Define an enum called StatusEnus with the enumerations "INVALID", "VALID" and "CONSUMED">
//
// <Define a typedef structure with the enum above and an "int" variable called "data">

enum StatusEnus {INVALID = -1, VALID = 0, CONSUMED = 1};

typedef struct ShmData{
    enum StatusEnus status;
    int data;
  } Data;
