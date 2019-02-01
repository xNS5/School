#include <stdio.h>

int CountSetBits(unsigned int);
int Converter(unsigned int);

int main(int argc, char *argv[]) {
  int num;
  sscanf(argv[1], "%d", &num);

  printf("%d has %d signed bits\r\n", num, CountSetBits(num));

  return 0;
}

int CountSetBits(unsigned int arg) {
    int bitCounter = 0;
    char
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
