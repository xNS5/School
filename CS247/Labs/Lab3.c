#include <stdio.h>

/*
  Todo: methods 11-16

  Notes: Completed Method 10, tested and it works as intended.

*/

int CountSetBits(unsigned int);
int UniqueInteger(int, int*);
int ReverseBits(int);
bool OnlyOneBitSet(int);
bool OnlyOneBitSetInEvenPosition(int);
int ModWithoutModOperator(int, int);
int SwapNibbles(int);

int main(int argc, char *argv[]) {
  int num;
  sscanf(argv[1], "%d", &num);

  printf("%d has %d signed bits\r\n", num, CountSetBits(num));

  return 0;
}

int CountSetBits(unsigned int arg) {
    int bitCounter = 0;
    int mod;

    while(arg != 0){
      mod = arg%2;
      arg = arg/2;

      if (mod == 1) {
        bitCounter++;
      }
    }
    return bitCounter;
  }
