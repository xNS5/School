#include <stdio.h>
#include <stdbool.h>

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
int NumberOfOperationsRequired(int);
int SwapNibbles(int);

int main(int argc, char *argv[]) {
  int num;
  int counter;
  sscanf(argv[1], "%d", &num);

  printf("%d has %d signed bit(s)\r\n", num, CountSetBits(num));

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

int UniqueInteger(int count, int* arr)
{
  return 0;
}

int ReverseBits(int var){
  return 0;
}

bool OnlyOneBitSet(int var){
  return false;
}

bool OnlyOneBitSetInEvenPosition(int var){
  return false;
}

int ModWithoutModOperator(int num, int denom){
  return 0;
}

int SwapNibbles(int var){
  return 0;
}

int NumberOfOperationsRequired(int var){
  return 0;
}
