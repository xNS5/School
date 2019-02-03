#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int CountSetBits(unsigned int);
int UniqueInteger(int, int*);
int ReverseBits(int);
bool OnlyOneBitSet(int);
bool OnlyOneBitSetInEvenPosition(int);
int ModWithoutModOperator(int, int);
int NumberOfOperationsRequired(int);
int SwapNibbles(int);

int main(int argc, char *argv[]) {

  // *Task 1*
  //printf("%d has %d signed bit(s)\r\n", x), CountSetBits(x);


  // *Task 2*
  /*int UniqueArr[argc-1];

  for(int i = 1; i < argc; i++){
      UniqueArr[i-1] = x;
    }*/
  //printf("%d is the unique element.\r\n", UniqueInteger(argc-1, UniqueArr));

  //*Task 3*
  //printf("%d is %s reversed.\r\n", ReverseBits(atoi(argv[1])), argv[1]);

  //*Task 4*
  //printf("When tested for whether %s has only one set bit, it returned %s\r\n",argv[1], OnlyOneBitSet(atoi(argv[1])) ? "true." : "false.");

  //*Task 5*
  //printf("%s\n", OnlyOneBitSetInEvenPosition(atoi(argv[1])) ? "true." : "false");

  //*Task 6*
  // if(argc != 3)
  //   {
  //     printf("This function requires 2 integers. Please try again.\r\n");
  //     return 1;
  //   }
  // int x = atoi(argv[1]), y = atoi(argv[2]);
  // printf("Test: %d\r\n", ModWithoutModOperator(x, y));
  // printf("Actual: %d\r\n", x%y);

  //*Task 7*
  //printf("%d\r\n",SwapNibbles(atoi(argv[1])));

  //*Task 8*
  //printf("%d\r\n", NumberOfOperationsRequired(atoi(argv[1])));

  return 0;
}

int CountSetBits(unsigned int arg) {
  int mod = 0;
  while(arg){
    mod += arg & 1;
    arg >>= 1;
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
  unsigned int bits = sizeof(var)*8;
  unsigned int reversed = 0, i, temp;

  for( i = 0; i < bits; i++)
    {
      temp = (var & (1 << i));
      if(temp)
        {
          reversed |= 1 << ((bits - 1) - i);
        }
    }
    return reversed;
}

bool OnlyOneBitSet(int var){
  if(CountSetBits(var) > 1)
    {
      return false;
    }
    return true;
  }

bool OnlyOneBitSetInEvenPosition(int var){
    unsigned int bits = sizeof(var)*8;
    if(CountSetBits(var) == 1)
      {
        for(int i = 0; i < bits; i++)
          {
            if(var & 1 && (i % 2 == 0))
              {
                return true;
              }
            var >>=1;
          }
      }
    return false;
}

int ModWithoutModOperator(int num, int denom){
  if(num < denom)
  {
    return num;
  }
  return (num & denom-1);
}

int SwapNibbles(int var){
  return ((var & 0x0F) << 4 | (var & 0xF0) >> 4);
}

int NumberOfOperationsRequired(int var){
  int counter = 1;
  int bits = sizeof(var)*8;
  unsigned int msb = 1 << bits-1;
  while (var > 1)
  {
    if(ModWithoutModOperator(var, 2) != 0)
      {
        for(int i = bits; i > 0; i--, msb >>=1)
        {
          if((var & msb) == msb)
          {
            var /= msb;
            break;
          }
        }
      }
    if(ModWithoutModOperator(var, 2) == 0)
      {
        var >>=2;
      }
      counter++;
  }
  return counter;
}