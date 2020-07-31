////////////////////////////////////////////////////
// AUthor: Michael Kennedy
// Class: CS140
// Assignment: Week 2, Program 1
// Program Description: Intro "Hello World!" Program
//     Input: Name of the user
//     Processing: Inserts name of user into program
//     Output: displays "Hello World! My name is <user>"
////////////////////////////////////////////////////
 
#include <iostream>      // for Input/Output 
using namespace std;     // using standard namespace

int main()
{
    cout << "Hello World! My name is Michael Kennedy!";    
    return 0;                                         
}

// I deleted the semicolon before namespace std, then compiled it.
// The terminal returned with an error saying that it expected a ";"
// before "int". I also added a semicolon before <iostream> and it returned
// saying that there was "extra tokens at end of #include directive."