/*

Notes: EOF == "End of file".

 */

#include <stdio.h>
#include <stdlib.h>

#define BUFFSIZE 4096

char* itoa(int num, char* str, int base);

int main(int argc, char *argv[]) {
   int number, base;

   if (argc != 3) {
      printf("Usage: ./Lab2 <number> <base>\n");
      return 1;
   }

   int res = sscanf(argv[1], "%d", &number);
   if (!res || res == EOF) {
      printf("Could not parse %s\n", argv[1]);
      return 1;
   }

   res = sscanf(argv[2], "%d", &base);
   if (!res || res == EOF) {
   } else if (!(2 <= base && base <= 36)) {
      printf("Base must be between 2 and 36\n");
      return 1;
   }

   char buf[BUFFSIZE];

   if (itoa(number, buf, base) != buf){
      printf("Failed to convert %d to base %d\n", number, base);

      return 1;
   }

   printf("Here is %d converted to base %d:\n%s\n", number, base, buf);

   return 0;
}

char* itoa(int num, char* str, int base) {
   int mod_val = 0;
   char temp[BUFFSIZE] = {'\0'};
   int neg_flag = 0;
   int i = 0;
   int t = 0;

   /* 
      Checks for whether the number is negative.
      If the number is zero, the value of num is changed to its absolute value and 
      the flag neg_flag is changed to 1. I tried using a bool but my computer yelled at me and 
      I didn't want to deal with it so I used 0 and 1 as false/true.
    */
   if(num < 0)
   {
      num = abs(num);
      neg_flag = 1;
   }
   // If the number is exactly zero, then it adds '0' to the str pointer and a null terminator, 
   // then returns the pointer
   if(num == 0)
   {
      str[0] = '0';
      str[1] = '\0';
      return str;
   }

   /*
      This is the main chunk of the function. The program finds the mod of num and base, then
      finds the next value of num by using integer division. If the value of mod_val is less than
      10, then it gets added to the char '0' which is the number 48, and if it's greater than or
      equal to 10, it gets added to the character 'a' which is the number 97. 
      If the value of num == 0, a null terminator is added to the temp array. If the neg_flag
      is 1, a - character is added to the end of the array before the null terminator.
    */
   while(num != 0)
   {

      mod_val = (num % base);
      num = num/base;

      if(mod_val < 10)
      {
         temp[i] = mod_val + '0';
      }

      else if(mod_val >= 10)
      {
         temp[i] = mod_val-10 + 'a';
      }

      else if(num == 0)
      {
         if(neg_flag == 1)
         {  
            temp[i] = '-';
            i++;
         }

         temp[i] = '\0';
      }

      i++;
   }

   // This part just reversed the array to the str pointer

   for(int j = i-1; j >= 0; j-- && t++)
   {
      str[t] = temp[j];
      if(j == 0)
      {
         str[t+1] = '\0';
      }
   }
      return str;

   }
