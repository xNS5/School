#include <time.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <inttypes.h>

/*
	Author: Michael Kennedy
	Class: CS247
	Assignment 1

	Notes: When I tried running my program on my mac with the 'int err[n] = pthread...' it wouldn't run properly.
	So in order to fix that, I commented out the pthread functions that are assigned to an int value and replaced them with
	just function calls.
*/

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
ThreadArgs g_ThreadArgs[MAX_THREAD_COUNT];
struct sched_param param;																												// Needed to create this for sched_priority to work
struct timespec tspec;																													// Needed to get clock_gettime to work
pthread_mutex_t mute = PTHREAD_MUTEX_INITIALIZER;																// Mutex parameter
pthread_cond_t cond_var = PTHREAD_COND_INITIALIZER;															// Conditional parameter
ThreadArgs thread;

/*
	This function initializes all of the thread schedules and assigns them to a location
	in the ThreadArgs array. I use the pthread_setschedparam to change the schedule priority of the
	threads.
	Note: If for whatever reason the thread schedule policy isn't successfully changed,
	it'll print out the error number for each pthread_setschedparam function call.
*/
void InitGlobals(void)
{
	int err1, err2, err3 = 0;
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
		{
			if(i < 3)
				{
					thread.threadPolicy = SCHED_FIFO;
					thread.threadPri = sched_get_priority_max(SCHED_FIFO);
					param.sched_priority = sched_get_priority_max(SCHED_FIFO);
					err1 = pthread_setschedparam((pthread_self()), SCHED_FIFO, &param);
					g_ThreadArgs[i] = thread;
				}
			else if(i >=3 && i < 6)
				{
					thread.threadPolicy = SCHED_RR;
					thread.threadPri = sched_get_priority_max(SCHED_RR);
					param.sched_priority =sched_get_priority_max(SCHED_RR);
					err2 = pthread_setschedparam((pthread_self()), SCHED_RR, &param);
					g_ThreadArgs[i] = thread;
				}
			else
			{
				thread.threadPolicy = SCHED_OTHER;
				thread.threadPri = sched_get_priority_max(SCHED_OTHER);;
				param.sched_priority = sched_get_priority_max(SCHED_OTHER);
				err3 = pthread_setschedparam((pthread_self()), SCHED_OTHER, &param);
				g_ThreadArgs[i] = thread;
			}
		}
		printf("==========================================================\n");
		printf("ERRORS:\r\n FIFO ERROR: %d\r\n RR ERROR: %d\r\n OTHER ERROR: %d\r\n", err1, err2, err3);
		printf("==========================================================\n");
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



/*
	This function times how long a thread takes to execute DoProcess() in nanoseconds using the clock_gettime() function.
	In the for loop, it assigns each timestamp to a location in the array in the ThreadArgs struct. It uses mutex locks and unlocks
	to restrict the contents between the two mutexes to each thread. I use the pthread_cond_wait to wait until all of the threads have been created
	to run in the threadFunction.
	Note: I've commented out the int err1= pthread_cond_wait... becuase it affected usage on my Mac. It was hanging in weird places and I narrowed down
	the cause to the commented out pthread_cond_wait function calls.
*/
void* threadFunction(void *arg)
{
	ThreadArgs* thread = (ThreadArgs*)arg;
	pthread_mutex_lock(&mute);
	pthread_cond_wait(&cond_var, &mute);
	//int err1 = pthread_cond_wait(&cond_var, &mute);
	//printf("Set cond_wait var with error: %d\r\n", err1);
	for(int y = 0; y < MAX_TASK_COUNT; y++)
	{
		clock_gettime(CLOCK_REALTIME, &tspec);
		thread->timeStamp[y] = tspec.tv_sec *1000000;
		thread->timeStamp[y] += tspec.tv_nsec/1000;
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y]++;

		DoProcess();

		clock_gettime(CLOCK_REALTIME, &tspec);
		thread->timeStamp[y+1] = tspec.tv_sec *1000000;
		thread->timeStamp[y+1] += tspec.tv_nsec/1000;
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y+1]++;
	}
	//printf("Completed DoProcess, unlocking mutex\r\n");
	pthread_mutex_unlock(&mute);
	pthread_exit(0);
}


/*
	I have my pthread_create in a for loop so it populates all 9 indexes of the g_ThreadArgs array with a thread. Once the threads are generated
	and it tries to run the threadFunction(var) it will hit the pthread_cond_wait and wait until the program runs the pthread_join(var) function.
	I was having trouble with pthread_cond_broadcast(var) for some reason and I wasn't 100% sure how to resolve it. What was happening was
	at the last SCHED_OTHER printout it would just hang. I narrowed it down to the program waiting at the pthread_cond_wait(var, var) for some reason
	even though it had received the broadcast. I resolved that issue by instead using pthread_cond_signal(var) in the for loop with the pthread_join(var)
	function.
*/

int main (int argc, char *argv[])
{
	InitGlobals();
	int i = 0;
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
	{
			pthread_create(&g_ThreadArgs[i].threadId, NULL, threadFunction, &g_ThreadArgs[i]);
	}

	for(int i = 0; i < MAX_THREAD_COUNT; i++)
 	{
			pthread_cond_signal(&cond_var);
			//int err = pthread_cond_signal(&cond_var); I noticed that when I ran this on my mac with the error checking in place, the program just hung.
			//printf("Signal error: %d\r\n", err);
			pthread_join(g_ThreadArgs[i].threadId, NULL);
			//printf("Printing output\r\n");
			DisplayThreadArgs(&g_ThreadArgs[i]);
	}

	return 0;
}


/*

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
