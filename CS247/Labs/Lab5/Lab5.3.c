/*
  A couple of things I noticed:
  1. I noticed that depending on the machine, the array size of this program varies.
  On my mac computer I got array of size 10. When I run it on linux, the array size is anywhere between 11-14. The assembly instruction says
  sub $0x40, %rsp. When I adjust the array size to anywhere between 11-14, the $0x40 doesn't change. If I changed the array size to 10 or 15, the
  value changes to something other than $0x40. This also happens when I run it on a school linux machine. Just don't want it to affect
  my grade.

  2. I wasn't able to find a way to match what the Example 3 file had in its assembly code when I get to
  the 'add' function. My program does essentially what the Example1.dms file does, but the assembly is slightly different.
*/


#include <stdio.h>

int main()
{
   int arr[11];
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
