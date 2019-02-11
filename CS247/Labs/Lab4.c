#include <stdio.h>
#include <math.h>

typedef unsigned float_bits;

int floatClass(float_bits f);
float_bits float_negate(float_bits);
float_bits float_absval(float_bits);
float_bits float_twice(float_bits);
float_bits float_half(float_bits);

int main()
{
   float_bits var = 0xce400000;
   //printf("%d\r\n", floatClass(var));
   //printf("%04x\n\r", float_negate(var));
   //printf("%04x\n\r", float_absval(var));
   //printf("%04x\n\r", float_twice(var));
   printf("%04x\n\r", float_half(var));
   return 0;
}

int floatClass(float_bits num)
{
  if((0x80000000^num) == 0 || (-0x80000000^num) == 0) // Checks for whether the number is -0.0 or 0 using XOR.
    {
      return 0;
    }
  else if((0x7f800000^num) == 0 || (-0x7f800000^num) == 0) // Checks for whether the number is positive or negative infinity.
    {
      return 1;
    }
  else if((0x7f800000 < num ) || (0xff800000 < num )) // Checks for whether a number is NaN
    {
      return 2;
    }
  else // checks for whether a number is normalized or denormalized. It checks over the 8 bits in the exponent checking for whether the  number
       // has bits set. If the bits are set, that means that the number is normalized. Otherwise, once the loop exits the function returns that
       // the number is a denormalized number
    {
      unsigned check = 1<<31;
      for(int i = 0; i < 8; i++)
        {
          if(check & num)
            {
              return 4;
            }
            check >>=1;
        }
        return 3;
    }
}

float_bits float_negate(float_bits num)
  {
    return (num + (1 << 31));
  }

float_bits float_absval(float_bits num)
  {
    return (num - (1 << 31));
  }

float_bits float_twice(float_bits num)
  {
    return (num << 1);
  }

float_bits float_half(float_bits num)
{
  return (num >> 1);
}
