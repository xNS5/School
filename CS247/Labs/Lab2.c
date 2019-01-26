/*
 Notes: EOF == "End of file".
 Implement counter like Ian's. That's pretty smart imo.

*/

#include <stdio.h>

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
		printf("Could not parse %s\n", argv[2]);
		return 1;
	} else if (!(2 <= base && base <= 36)) {
		printf("Base must be between 2 and 36\n");
		return 1;
	}

	char buf[BUFFSIZE];

	if (itoa(number, buf, base) != buf) {
		printf("Failed to convert %d to base %d\n", number, base);
		return 1;
	}

	printf("Here is %d converted to base %d:\n%s\n", number, base, buf);

	return 0;
}

char* itoa(int num, char* str, int base) {
  int mod_val = 100000;
	char temp[BUFFSIZE];
	char reversed[BUFFSIZE];
	int i = 0;
	int t = 0;
	int counter;

  while(num != 0)
	{
		mod_val = (num % base);
		num = num/base;

		if(mod_val < 10)
		{
			temp[i] = mod_val + '0';
		}

		else
		{
			temp[i] = mod_val-10 + 'a';
		}
		counter = i;
		i++;
	}


	for(int j = i; j >= 0; j--)
		{
			reversed[t] = temp[j];
			t++;
		}
	str = reversed;
	printf("%s", str);
	return str;

}
