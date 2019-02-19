/*
  Note to self: compile with -fstack-protector-all
  Single array
  Array manipulation
*/
#include <stdio.h>

int main()
{
   int arr[14];
   arr[4] = 0x17;
   arr[5] = 0x13;
   arr[6] = 0xc;
   arr[8] = 0x2;
   arr[9] = 0x3;
   arr[10] = 0x1;
   arr[3] =  0x0;

   arr[3] += (arr[4] * arr[8]);
   arr[3] += (arr[5] * arr[9]);
   arr[3] += (arr[6] * arr[10]);


   return 0;
}
