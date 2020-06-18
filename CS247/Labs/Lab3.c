#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>

int CountSetBits(unsigned int);
int IdentifyUniqueInteger(int, int*);
int ReverseBits(int);
bool OnlyOneBitSet(int);
bool OnlyOneBitSetInEvenPosition(int);
int ModWithoutUsingModOperator(int, int);
int NumberOfOperationsRequired(int);
int SwapNibbles(int);
int msb(unsigned int);

/*
  Task 1: Counting the number of set bits.
  This task is accomplished by using the bitwise '&' operator to check for whether the value in the nth position is a 1.
  Even if the byte at the nth position isn't 1, it's still added to val -- which is the total number of set bits.
  Runtime: O(n).
*/
int CountSetBits(unsigned int var) {
  int set_bits = 0;

  while(var){
    set_bits += var & 1;
    var >>= 1;
    }
    return set_bits;
  }

/*
  Task 2: This function uses the exclusive OR (^) to find the number that doesn't contain a matching value. E.g. 5^5 will return 0, so 5^6^5 will return 6.
  Runtime: O(n).
*/
int IdentifyUniqueInteger(int count, int* arr)
{
  int xor = arr[0];

  for(int i = 1; i < count; i++){
    xor ^= arr[i];
  }
  return xor;
}

/*
    Task 3: This function reverses the bits of an integer. So for example, if the number 0010 (2) were inputted into this function, it would return 1073741824.
    It iterates through the number and moves the bit, whether it's signed or not, to the opposite end of the binary number. It uses the bitwise inclusive OR
    operator to move the 1's i indexes.
    Runtime: O(n).
*/
int ReverseBits(int var){
  unsigned int bits = sizeof(var)*8;
  unsigned int reversed = 0, temp;

  for(int i = 0; i < bits; i++)
    {
      temp = (var & (1 << i));
      if(temp)
        {
          reversed |= 1 << ((bits - 1) - i);
        }
    }
    return reversed;
}

/*
  Task 4: This function really only calls CountSetBits and determines if the number only has one set bit (which would also make it a power of 2).
  If the number only has 1 set bit, it returns true. Otherwise it returns false, meaning there is more than one set bit in this integer.
  Runtime: O(n) due to calling CountSetBits.
*/
bool OnlyOneBitSet(int var){
  if(CountSetBits(var) > 1)
    {
      return false;
    }
    return true;
  }

/*
  Task 5: This function determines if the number that has one set bit is in the even position. If it is, it returns true.
  It iterates through the bits of the number, and if it doesn't trigger the inner If statement, it returns false. If the number inputted
  has more than one set bit, it returns false.
  Runtime: O(n).
*/
bool OnlyOneBitSetInEvenPosition(int var){
    unsigned int bits = sizeof(var)*8;
    if(OnlyOneBitSet(var))
      {
        for(int i = 0; i < bits; i++)
          {
            if(var & 1 && ((i & 1) == 0))
              {
                return true;
              }
            var >>=1;
          }
      }
    return false;
}

/*
  Task 6: This function uses the bitwise and to calculate the modulus value of a number and it's denominator-1.
  Runtime: O(1).
*/
int ModWithoutUsingModOperator(int num, int denom){
  if(num < denom)
  {
    return num;
  }
  return (num & denom-1);
}

/*
  Task 7: This function returns the copied top and bottomn nibbles shifted right and left 4 places, then combined with the inclusive OR (|) operator.
  Runtime: O(1).
*/
int SwapNibbles(int var){
  return ((var & 0x0F) << 4 | (var & 0xF0) >> 4);
}

/*
  Task 8 & 9 This function:
  1. Determines if a number is a power of 2 by using the bitwise and (&) operator.
    1.2 If the number is a power of 2, then it finds the number of set bits and returns the number of set bits + the value of the counter.
        The reasoning behind this is say you're finding the difference between 0b1000 and 0b0001 (8 and 1). The value would be 7, or 0b111 and it would have
        3 set bits. That's how many times the program would run considering the number 8 is a power of 2.
  2. If the number is not a power of 2, this program iterates through the number to find the most significant bit, and returns a number of 2^n -- which represents
      the highest power of 2 that is less than the integer var. Then that number is subtracted from the initial integer, and the process is repeated.
  Runtime: O(n).
*/
int NumberOfOperationsRequired(int var){
  int counter = 0;

  if(var < 1)
    {
      printf("Argument has to be greater than 1\r\n");
      return 0;
    }
  while (var > 1)
  {
    if(var && (var & (var-1)) == 0)
      {
        //printf("%d taco Bell\r\n", (int)pow(2, floor(log2(var))));
        var -= msb(var); // pow(2, floor(log2(var)));
        //I wasn't sure whether I was supposed to use bit operations or if I could use math so I added both.
      }
    else
      {
        return CountSetBits(var-1) + counter;
      }
    counter++;
  }
  return counter;
}

/*
  This function just finds the most significant bit (hence msb).
*/
int msb(unsigned int var)
{
  unsigned int bits = (sizeof(var)*8);
  unsigned int x = 1 << (bits-1);

  for(int i = bits; i > 0; i--)
    {
      if(var & x)
      {
        return x;
      }
      x >>=1;
    }
  return 0;
}
