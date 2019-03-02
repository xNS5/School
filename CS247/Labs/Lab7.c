
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <string.h>
#include <time.h>

#define handle_error_en(en, msg) \
  {errno = en; int errnum = errno; perror(msg); strerror(errnum); exit(EXIT_FAILURE);}

void computationMethod(int, int, int**);

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
  return 0;
}

void computationMethod(int num, int matrix_dimension, int** arr)
{
  struct timespec start, end;
  float difference;

  clock_gettime(CLOCK_MONOTONIC, &start);

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
  clock_gettime(CLOCK_MONOTONIC, &end);
  printf("Accumulator total:%d\r\n", accumulator);
}
