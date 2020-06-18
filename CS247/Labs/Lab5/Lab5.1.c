#include <stdio.h>

int main()
{
  int x = 0;
  int y = 0;
  y = y + x;
  x = x + y;
  y += x++;
  return 0;
}
