/* prime-number finding program
Originally by Norman Matloff, UC Davis
http://wwwcsif.cs.ucdavis.edu/~davis/40/gdb_Tutorial.htm
Modified by Mark Ardis, RIT, 11/1/2006

Will report a list of all primes which are less than
or equal to the user-supplied upper bound.
WARNING: There are bugs in this program! */

#include <stdio.h>

/* the plan:  see if J divides K, for all values J which are
themselves prime (no need to try J if it is non-prime), and
less than or equal to sqrt(K) (if K has a divisor larger
than this square root, it must also have a smaller one,
so no need to check for larger ones) */

void check_prime(int k, int primes[]) {
    int j = 2;
    while (j * j <= k) {
        if (primes[j] == 1){
            if (k % j == 0)  {
                primes[k] = 0;
                return;
            } /* if (k % j == 0) */
        } /* if (Prime[j] == 1) */
        j++;
    } /* while (j*j <=k) */

    /* If we get here, then there were no divisors of K, so it is prime */
    primes[k] = 1;

}  /* CheckPrime() */

int main() {
    int upper_bound; /* check all numbers up through this one for primeness */
    printf("Enter upper bound:\n");
    scanf("%d", &upper_bound);

    int primes[upper_bound];  /* prime[i] will be 1 if i is prime, 0 otherwise */
//    upper_bound = 50 ; // If UpperBound isn't commented out, the inputted value will get overwritten.
    primes[1] = 1, primes[2] = 1;
    for (int i = 3; i <= upper_bound; i += 2) {
        check_prime(i, primes);
        if (primes[i]) {
            printf("%d is a prime\n", i);
        } /* if (Prime[i]) */
    } /* for (i = 3; i <= UpperBound; i += 2) */
    return 0;
} /* main() */
