#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/*

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
  int UniqueArr[argc-1];

  for(int i = 1; i < argc; i++){
      UniqueArr[i-1] = atoi(argv[i]);
    }

  //printf("%d has %d signed bit(s)\r\n", atoi(argv[1]), CountSetBits(atoi(argv[1])));
  //printf("%d is the unique element.\r\n", UniqueInteger(argc-1, UniqueArr));

  return 0;
}

int CountSetBits(unsigned int arg) {
  int mod;
  while(arg){
    mod += arg & 1;
    arg >>=1;
  }
    return mod;
  }

int UniqueInteger(int count, int* arr)
{
  int xor = arr[0];
  for(int i = 1; i < count; i++){
    xor ^= arr[i];
  }
  return xor;
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
