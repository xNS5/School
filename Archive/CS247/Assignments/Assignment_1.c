/*
	ANALYSIS:

	I noticed that when I ran 0xffffffff on my Mac computer, runtime for all of the threads were around ~6000000 ns.
	However when I ran it on my Linux machine, the delta was around ~8000000 ns. My mac has an i7 processor whereas my Linux machine has a
	i5 processor. My mac has a 2.7 GHz i7 Intel CPU, meaning that it has the ability to Hyper-thread which creates two logical cores inside each core.
	My Linux has an i5 CPU which doesn't include the hyper-threading feature. This explains the difference in runtime between the two computers.

	Sched_rr and sched_fifo are both real-time (RT) schedules. RR or "round-robin" is a schedule that allocates a slice of time
	for each process, and these processes are executed in a circular order. Fifo or "first in first out" and is nearly identical to RR
	except it runs first in first out without timeslices and it is never preempted -- meaning it'll leave the CPU when its completed it's task.
	Lastly, Sched_other is the normal process schedule that the computer runs on. RT processes that are RR or FIFO have greater importance than OTHER.
	The number of threads that can run on a CPU depends on how many cores the CPU has. So theoretically if the CPU has 4 cores it will run 4 threads at once,
	but because of the scheduling policies it can run multiple threads while having others on hold until the time slice is used up or the higher
	priority process is completed. RT priorities take precende over OTHER, so while a RT process is being run, no SCHED_OTHER will be allowed to run in the CPU.
*/

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
	Assignment 1 -- Thread Generation using Pthread
*/

#define MAX_TASK_COUNT 3
#define MAX_THREAD_COUNT 9

typedef struct{
	int threadCount, threadPolicy, threadPri;
	pthread_t threadId;
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
	ThreadArgs thread;
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
		printf("Note: please run as root user\r\n");
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
	// int err1 = pthread_cond_wait(&cond_var, &mute);
	// printf("Set cond_wait var with error: %d\r\n", err1);
	for(int y = 0; y < MAX_TASK_COUNT; y++)
	{
		clock_gettime(CLOCK_REALTIME, &tspec);
		thread->startTime = time(0);
		thread->timeStamp[y] = tspec.tv_sec *1000000;
		thread->timeStamp[y] += tspec.tv_nsec/1000;
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y]++;

		DoProcess();

		clock_gettime(CLOCK_REALTIME, &tspec);
		thread->endTime = time(0);
		thread->timeStamp[y+1] = tspec.tv_sec *1000000;
		thread->timeStamp[y+1] += tspec.tv_nsec/1000;
		if(tspec.tv_nsec % 1000 >= 500 ) thread->timeStamp[y+1]++;
	}
	// printf("Completed DoProcess, unlocking mutex\r\n");
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
	for(int i = 0; i < MAX_THREAD_COUNT; i++)
	{
		 pthread_create(&g_ThreadArgs[i].threadId, NULL, threadFunction, &g_ThreadArgs[i]);
	}

	for(int i = 0; i < MAX_THREAD_COUNT; i++)
 	{
		//pthread_cond_signal(&cond_var);
		int err = pthread_cond_signal(&cond_var); // I noticed that when I ran this on my mac with the error checking in place, the program just hung.
		printf("Signal error: %d\r\n", err);
		pthread_join(g_ThreadArgs[i].threadId, NULL);
		DisplayThreadArgs(&g_ThreadArgs[i]);
	}

	return 0;
}
