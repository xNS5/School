// <Define an enum called StatusEnus with the enumerations "INVALID", "VALID" and "CONSUMED">
//
// <Define a typedef structure with the enum above and an "int" variable called "data">

enum StatusEnus {INVALID, VALID, CONSUMED};

typedef struct {
    enum StatusEnus status;
    int data;
  } Data;
