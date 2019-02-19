/*
  Note to self: compile with -fstack-protector-all
  Single array
  Array manipulation
*/
#include <stdio.h>

int main()
{
   int arr[10];

   arr[3] = 0x17;
   arr[4] = 0x13;
   arr[5] = 0xc;
   arr[7] = 0x2;
   arr[8] = 0x3;
   arr[9] = 0x1;
   arr[2] =  0x0;

   arr[2] += (arr[3] * arr[7]);
   arr[3] += (arr[5] * arr[9]);
   arr[3] += (arr[6] * arr[10]);

   return 0;
}
