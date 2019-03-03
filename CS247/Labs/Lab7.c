#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <string.h>
#include <time.h>

#define handle_error_en(en, msg) \
  {errno = en; int errnum = errno; perror(msg); strerror(errnum); exit(EXIT_FAILURE);}

void computationMethod(int, int, int**);

/*
  Note: On my computer I wasn't able to use L1-icache-loads for some reason. From what I can gather, the perf hardware events depend on what hardware I'm on.
  Which is weird, considering I have L1-icache-load-misses available. My analysis is based on dcache output because the icache isn't supported on my computer. I can see the
  icache misses, but not the loads for some reason.

  Analysis: When I ran my program using the dcache with computation method 1, as the array dimensions increased the number of misses went down. Using a 64 length array,
  I for a 5.73% L1-dcache-load-misses. The lowest it got was 1.01% misses from 1024 using computation method 1. For computation method 2, the dcache miss percentage went down but
  increased when the array size turned to 512. In most processors today, each core has a unified L2 cache which might explain why my computer is unable to see the L1-icache-loads.
  For the first computation method, as the array size increased, the number of cache misses decreased.

  In the case of Method 2, it keeps shifting rows instead of columns so it can't necessarily rely on the cached data

  64 1 --  5.63% of all L1-dcache hits, 0.504 CPUs utilized, 0 context-switches\
  64 1 --  22,556 L1-icache-misses, 0.587 CPUs utilized
  128 1 -- 3.26% of all L1-dcache hits, 0.545 CPUs utilized, 0 context switches
  128 1 -- 21,374 L1-icache-misses, 0.643 CPUs utilized
  256 1 -- 1.91% of all L1-dcache hits, 0.788 CPUs utilized, 0 context switches
  256 1 -- 23,316 L1-icache-misses, 0.699 CPUs utilized
  512 1 -- 1.20% of all L1-dcache hits, 0.873 CPUs utilized, 0 context switches
  512 1 -- 23,402 L1-icache-misses, 0.886 CPUs utilized
  1024 1 -- 1.00% of all L1-dcache hits 0.959 CPUs utilized
  1024 1 -- 29,676 L1-icache-misses, 0.956 CPUs utilized

  64 2 --  5.19% of all L1-dcache hits, 0.614 CPUs utilized, 0 context switches
  64 2 --  23,302 L1-icache-misses, 0.542 CPUs utilized
  128 2 -- 3.60% of all L1-dcache hits, 0.643 CPUs utilized, 0 context switches
  128 2 -- 22,142 L1-icache-misses, 0.580 CPUs utilized
  256 2 -- 1.92% of all L1-dcache hits, 0.719 CPUs utilized, 0 context switches
  256 2 -- 22,516 L1-icache-misses, 0.722 CPUs utilized
  512 2 -- 7.52% of all L1-decache hits, 0.872 CPUs utilized, 0 context switches
  512 2 -- 23,166 L1-icache-misses, 0.825 CPUs utilized
  1024 2 --7.79% of all L1-decache hits, 0.951 CPUs utilized, 0 context switches
  1024 2 --28,753 L1-icache-misses, 0.968 CPUs utilized
*/

int main(int argc, char* argv[])
{
  if(argc != 3)
    {
      printf("Usage: ./<filename>   <int matrix size>   <int computation method>\r\n");
      handle_error_en(EINVAL, "Input count");
    }

  int matrix_dimension = atoi(argv[1]);
  int computation_method = atoi(argv[2]);

  if(computation_method != 1 && computation_method != 2)
  {
    printf("Valid computation methods: 1, 2\r\n");
    handle_error_en(EINVAL, "Computation Method");
  }

  int** arr = (int**)malloc(matrix_dimension * sizeof(int*));
  for(int i = 0; i < matrix_dimension; i++)
    {
      arr[i] = (int*)malloc(matrix_dimension*sizeof(int));
    }

  computationMethod(computation_method, matrix_dimension, arr);
  free(arr);

  return 0;
}

void computationMethod(int num, int matrix_dimension, int** arr)
{
  struct timespec start, end;
  float difference;

  int accumulator = 0;
  if(num == 1)
    {
      for(int i = 0; i < matrix_dimension; i++)
        {
          for(int j = 0; j < matrix_dimension; j++)
            {
              arr[i][j]= i + j;
              accumulator += arr[i][j];
            }
        }
    }
  else if(num == 2)
    {
      for(int j = 0; j < matrix_dimension; j++)
        {
          for(int i = 0; i < matrix_dimension; i++)
            {
              arr[i][j]= i + j;
              accumulator += arr[i][j];
            }
        }
    }
  printf("\r\n");
  printf("Accumulator total: %d\r\n", accumulator);
}
