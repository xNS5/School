//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//   may buddha protect this code from bug HAHAHAHAHA

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
   float_bits var = 0xc4410000;
   // Please uncomment as needed to test. Or don't, it's your life.
   // printf("The inputted value is: 0x%4x\r\n", var);
   // printf("The float class is: %d\r\n", floatClass(var));
   // printf("The negated value is: 0x%04x\n\r", float_negate(var));
   // printf("The absolute value is: 0x%04x\n\r", float_absval(var));
   // printf("Double the value is: 0x%04x\n\r", float_twice(var));
   // printf("Half of the value is: 0x%04x\n\r", float_half(var));
   return 0;
}

/*
  FloatClass determines what kind of float the number is, whether it's +/- 0 (meaning it's really small but close to zero), +/- Infinity,
  and whether the number is normalized/denormalized.
*/
int floatClass(float_bits num)
{
  unsigned pos_infinity = 0x7f800000;
  unsigned neg_infinity = 0xff800000; // This needed to be a signed int because otherwise I wouldn't be able to check for whether num < negative infinity.
  if((0x80000000^num) == 0) // Checks for whether the number is -0.0 or 0 using XOR.
    {
      return 0;
    }
  else if((pos_infinity^num) == 0 || (neg_infinity^num) == 0) // Checks for whether the number is positive or negative infinity using XOR.
    {
      return 1;
    }
  else if(num > pos_infinity && (num & (1<<31))) // Checks for whether num > infinity and the sign bit is set.
    {
      return 2;
    }
  /*
    This last else statement checks for whether a number is normalized or denormalized. It checks over the 8 bits in the exponent for whether the number
    has bits set. If the bits are set, that means that the number is normalized. Otherwise, once the loop exits the function returns that
    the number is a denormalized number
  */
  else
    {
      unsigned check = 1<<31;
      for(int i = 0; i < 8; i++)
        {
          if(check & num)
            {
              return 4; // Meaning this is a Normalized number.
            }
            check >>=1; // Shifts the bits left 1.
        }
        return 3; // Meanint this is a de-normalized number.
    }
}

/*
  This negates the value of num. I wasn't sure what it meant to "negate" a value, so I interpreted it as making it negative.
  The first thing this function does is determine if the sign bit is set. If it is, that means that this is a negative number and
  instead of making a negative number negative again, I'd just make it positive.
*/
float_bits float_negate(float_bits num)
  {
    if(num & (1 << 31))
      {
        return (num-= (1<<31));
      }
    return (num += (1<<31));
  }

/*
  The absolute value of a negative number is its positive equivalent. Instead of adding a bit shifted to the right, it subtracts the bit.
  If the sign bit it set, it flips the sign bit and returns the value. If it isn't set, it just returns the input it received.
*/
float_bits float_absval(float_bits num)
  {
    if(num & (1<<31))
      {
        num -= (1 << 31);
        return num;
      }
    return num;
  }

/*
    All this requires is a bit shifted once to the right.
*/
float_bits float_twice(float_bits num)
  {
    return (num << 1);
  }

/*
  This just requires a single bit shift to the left.
*/
float_bits float_half(float_bits num)
{
  return (num >> 1);
}
