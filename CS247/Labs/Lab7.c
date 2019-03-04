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
  Which is weird, considering I have L1-icache-load-misses available. My analysis is based on dcache output because the icache-loads isn't supported on my computer.

  Analysis: When I ran my program using the dcache with computation method 1, as the array dimensions increased the number of misses went down. Using a 64 length array,
  I got 5.73% L1-dcache-load-misses. The lowest it got was 1.01% misses from 1024 using computation method 1. For computation method 2, the dcache miss percentage went down, then
  increased when the array size turned to 512. In most processors today, each core has a unified L2 cache which might explain why my computer is unable to see the L1-icache-loads.

  In C, arrays are organized in row major order. Meaning that it would be easier to go to one row, traverse it, then move onto the next row than it is to go to one column and
  jump from row to row. It's a lot easier to cache information that way, which explains why the number of misses for method 2 is much higher than that of method 1. In method 1,
  the cache utilizes the fact that all of the data elements are going to be +/- 1 of the current data element, so it's easier spatially to store and access elements
  than it is if I were traversing rows of a column. Traversing the rows of the column will result in more cache misses because the location of the next element isn't necessarily
  in the cache, which means that the computer will have to retrieve it from memory which takes a little more time.

  This means in order to ensure that a program involving 2D arrays functions properly, it needs to iterate over the columns first then move to the next row instead of the reverse.

  I can't explain the L1-icache-loads and misses because I don't have the loads to compare the misses to.

  perf output:

  64 1 --  5.63% of all L1-dcache hits, 0.504 CPUs utilized, 0 context switches
  64 1 --  22,556 L1-icache-misses, 0.587 CPUs utilized
  128 1 -- 3.26% of all L1-dcache hits, 0.545 CPUs utilized, 0 context switches
  128 1 -- 21,374 L1-icache-misses, 0.643 CPUs utilized
  256 1 -- 1.91% of all L1-dcache hits, 0.788 CPUs utilized, 0 context switches
  256 1 -- 23,316 L1-icache-misses, 0.699 CPUs utilized
  512 1 -- 1.20% of all L1-dcache hits, 0.873 CPUs utilized, 0 context switches
  512 1 -- 23,402 L1-icache-misses, 0.886 CPUs utilized
  1024 1 -- 1.00% of all L1-dcache hits 0.959 CPUs utilized, 0 context switches
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
      handle_error_en(EINVAL, "Input count"); // I thought Susheel's error handling function was really neat so I copied it over from Assignment2
    }

  int matrix_dimension = atoi(argv[1]);
  int computation_method = atoi(argv[2]);

  /*
    Checks to see if the computation method is valid. Even if you input a floating point number,
    the computation method will be floor(atoi(argv[2])), so if argv[2] were 2.9 it would be converted to 2.
  */
  if(computation_method != 1 && computation_method != 2)
  {
    printf("Valid computation methods: 1, 2\r\n");
    handle_error_en(EINVAL, "Computation Method");
  }

  /*
    Allocating memory for the array by multiplying the matrix dimension by the size of an integer pointer.
    Then, I populate the array with integer pointers with the matrix size times the size of an integer.
  */
  int** arr = (int**)malloc(matrix_dimension * sizeof(int*));
  for(int i = 0; i < matrix_dimension; i++)
    {
      arr[i] = (int*)malloc(matrix_dimension*sizeof(int));
    }

  /*
    I wanted to move it to a separate function for space. There's no real reason to do this, but then again there's no real reason not to.
    If you were concerned with runtime, I'm fairly certain there would be a minute difference in time of using a switch vs a function call.
    Once the function completes its task and prints out the accumulator, returns to the main function, frees the memory allocated by
    the array, then quits.
  */
  computationMethod(computation_method, matrix_dimension, arr);
  free(arr);

  return 0;
}

void computationMethod(int num, int matrix_dimension, int** arr)
{
  /*
    Two computation methods:
    1. The array iterates over each column, then shifts to the next row.
    2. The array iterates down each row, then shifts columns.
    The accumulator is initialized to zero, then adds each element of the array to itself
    then finally prints out the total value of the numbers stored in the array.
  */
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
  printf("Accumulator total: %d\r\n", accumulator);
}
