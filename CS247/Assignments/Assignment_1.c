
#include <time.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <inttypes.h>

//links taco

/*
	http://man7.org/linux/man-pages/man3/pthread_create.3.html
	http://man7.org/linux/man-pages/man7/sched.7.html
	http://man7.org/linux/man-pages/man2/sched_setscheduler.2.html

	Compile with -lpthread (e.g. gcc Assignment_1.c -lpthread)

	Thread policy numbers
	1- FIFO
	2- RR
	0- OTHER

*/

//#defines

#define MAX_TASK_COUNT 3
#define MAX_THREAD_COUNT 9

typedef struct{
	int threadCount;
	pthread_t threadId;
	int threadPolicy;
	int threadPri;
	long processTime;
	int64_t timeStamp[MAX_TASK_COUNT+1];
	time_t startTime;
	time_t endTime;
} ThreadArgs;

//Globals

//Try to change this to use a single condition variable
pthread_mutex_t g_ThreadMutex [MAX_THREAD_COUNT];
pthread_cond_t g_conditionVar [MAX_THREAD_COUNT]; //Used for the mutex
ThreadArgs g_ThreadArgs[MAX_THREAD_COUNT];
struct sched_param param;
struct timespec tspec;
ThreadArgs thread;
pthread_mutex_t mute = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t var;


void InitGlobals(void)
{
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
		{
			if(i < 3)
				{
					thread.threadPolicy = SCHED_FIFO;
					thread.threadPri = 4;
					param.sched_priority = sched_get_priority_max(SCHED_FIFO);
					pthread_setschedparam(thread.threadId, SCHED_FIFO, &param);
					g_ThreadArgs[i] = thread;
				}
			else if(i >=3 && i < 6)
				{
					thread.threadPolicy = SCHED_RR;
					thread.threadPri = 4;
					param.sched_priority = sched_get_priority_max(SCHED_RR);
					pthread_setschedparam(thread.threadId, SCHED_RR, &param);
					g_ThreadArgs[i] = thread;
				}
			else
			{
				thread.threadPolicy = SCHED_OTHER;
				thread.threadPri = 4;
				param.sched_priority = sched_get_priority_max(SCHED_OTHER);
				pthread_setschedparam(thread.threadId,SCHED_OTHER, &param);
				g_ThreadArgs[i] = thread;
			}
		}
}

void DisplayThreadSchdAttributes( pthread_t threadID, int policy, int priority )
{

	printf("\nDisplayThreadSchdAttributes:\n threadID = 0x%lx\n policy = %s\n priority = %d\n", (unsigned long)threadID,
									(policy == SCHED_FIFO) ? "SCHED_FIFO" :
									(policy == SCHED_RR)	? "SCHED_RR" :
									(policy == SCHED_OTHER) ? "SCHED_OTHER" :
									"???",
									priority);
}

void DisplayThreadArgs(ThreadArgs*	myThreadArg)
{
int i,y;

if( myThreadArg )
{
	DisplayThreadSchdAttributes(myThreadArg->threadId, myThreadArg->threadPolicy, myThreadArg->threadPri);
	printf(" startTime = %s endTime = %s", ctime(&myThreadArg->startTime), ctime(&myThreadArg->endTime));
	printf(" TimeStamp [%"PRId64"]\n", myThreadArg->timeStamp[0] );

	for(y=1; y<MAX_TASK_COUNT+1; y++)
	{
		printf(" TimeStamp [%"PRId64"] Delta [%"PRId64"]us\n", myThreadArg->timeStamp[y],
		(myThreadArg->timeStamp[y]-myThreadArg->timeStamp[y-1]));
	}
}
}

void DoProcess(void)
{
	unsigned int longVar =1 ;

	while(longVar < 0xffffffff) longVar++;
}


void* threadFunction(void *arg)
{
	/*1.	Typecast the argument to a ThreadArgs* variable
	2.	Use the pthread_setscheduleparam API to set the thread policy
	3.	Init the Condition variable and associated mutex
	4.	Wait on condition variable
	5.	Once condition variable is signaled, use the time function and the �clock_gettime(CLOCK_REALTIME, &tms) to get timestamp
	6.	Call DoProcess to run your task
	7.	Use time and clock_gettime to find end time.
	8.	You can repeat steps 6 and 7 a few times if you wise*/
	ThreadArgs* thread = (ThreadArgs*) arg;
	clock_gettime(CLOCK_REALTIME, &tspec);
	pthread_mutex_lock(&mute);
	//pthread_cond_wait(&var, &mute);
	for(int y = 0; y < MAX_TASK_COUNT; y++)
	{
		thread->timeStamp[y] = tspec.tv_sec *1000000;
		thread->timeStamp[y] += tspec.tv_nsec/1000;
		printf("%lld", thread->timeStamp[y]);
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y+1];

		DoProcess();

		clock_gettime(CLOCK_REALTIME, &tspec);
		thread->timeStamp[y+1] = tspec.tv_sec *1000000;
		thread->timeStamp[y+1] += tspec.tv_nsec/1000;
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y+2];
	}
	pthread_mutex_unlock(&mute);
}


int main (int argc, char *argv[])
{
	/*1.	Call InitGlobals
	2.	Create a number of threads (start with 1 and increase to 9) using pthread_Create // COMPLETED
	3.	Assign 3 threads to SCHED_OTHER, another 3 to SCHED_FIFO and another 3 to SCHED_RR // COMPLETED
	4.	Signal the condition variable
	5.	Call pthread_join to wait on the pthread_join in separate loop
	*/
	InitGlobals();
	//int i = 0;
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
	{
			pthread_create(&g_ThreadArgs[i].threadId, NULL, threadFunction, &g_ThreadArgs[i]);
	}
				pthread_cond_broadcast(&var);
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
 	{
			pthread_join(g_ThreadArgs[i].threadId, NULL);

	}


	return 0;
}


/*>timeStamp[y]-myThreadArg

************* HINTS ******************

========================================================================================================================================================
Every time you run into issues with usage of an API, please look up samples on how that API is used here...

http://www.yolinux.com/TUTORIALS/LinuxTutorialPosixThreads.html

========================================================================================================================================================


Please check the return values from all system calls and print an error message in all error cases including the error code.. That will help catch errors quickly.
========================================================================================================================================================


You can use the following technique to pass the address of the element corresponding to a particular thread to the thread function...

	void* threadFunction(void *arg)
	{
		ThreadArgs*	myThreadArg;

		myThreadArg = (ThreadArgs*)arg;

	}


	int main (int argc, char *argv[])
	{

		while(threadCount < MAX_THREAD_COUNT)
		{
		...
			if( pthread_create(&(g_ThreadArgs[threadCount].threadId), &threadAttrib, &threadFunction, &g_ThreadArgs[threadCount]) )
		...

		}
	}
========================================================================================================================================================

Here is the usage for clock_gettime�

	clock_gettime(CLOCK_REALTIME, &tms);
	myThreadArg->timeStamp[y+1] = tms.tv_sec *1000000;
	myThreadArg->timeStamp[y+1] += tms.tv_nsec/1000;
	if(tms.tv_nsec % 1000 >= 500 ) myThreadArg->timeStamp[y+1]++;

========================================================================================================================================================

Here is how you wait on a condition event�

	pthread_mutex_lock ( &g_ThreadMutex[myThreadArg->threadCount] );
	pthread_cond_wait ( &g_conditionVar[myThreadArg->threadCount], &g_ThreadMutex[myThreadArg->threadCount] );
	pthread_mutex_unlock( &g_ThreadMutex[myThreadArg->threadCount] );

========================================================================================================================================================

Note that this sample is changing the policy of the current thread... so if you follow this sample, make sure you are making the call from the thread function.


	http://man7.org/linux/man-pages/man3/pthread_setschedparam.3.html

	if (main_sched_str != NULL) {
	if (!get_policy(main_sched_str[0], &policy))
		usage(argv[0], "Bad policy for main thread (-m)\n");
		param.sched_priority = strtol(&main_sched_str[1], NULL, 0);

	s = pthread_setschedparam(pthread_self(), policy, &param);
	if (s != 0)
		handle_error_en(s, "pthread_setschedparam");
	}

========================================================================================================================================================
For those confused about my comment on trying to using a single Condition variable instead of an array... please read the following...

http://pubs.opengroup.org/onlinepubs/9699919799/functions/pthread_cond_signal.html

You can use the broadcast API to wake multiple threads waiting on the same condition variable.

For those who really like to go deeper, know that you have access to the code for most of the Linux system APIs... here is the code pthread_cond_broadcast...

https://code.woboq.org/userspace/glibc/nptl/pthread_cond_broadcast.c.html

========================================================================================================================================================
*/