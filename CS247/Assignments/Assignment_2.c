/*****************************************************************************************/
// To compile run: gcc -g Ass2_template.c -pthread -lrt
/*****************************************************************************************/
#include <time.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <inttypes.h>
#include <signal.h>
#include <errno.h>

#define	MAX_THREAD_COUNT		  3

#define LOW_THREAD_PRIORITY		50

#define STACK_SIZE				    0x400000
#define	MAX_TASK_COUNT			  3

//define error handling later on
#define handle_error_en(en, msg) \
        do { errno = en; perror(msg); exit(EXIT_FAILURE); } while (0)

/*****************************************************************************************/
pthread_mutex_t		g_DisplayMutex = PTHREAD_MUTEX_INITIALIZER;;
pthread_mutex_t 	g_SignalMutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct{
  	int					        threadCount;
  	pthread_t		        threadId;
  	int					        threadPolicy;
  	int					        threadPri;
	  struct sched_param 	param;
  	long				        processTime;
  	int64_t			        timeStamp[MAX_TASK_COUNT+1];
  	time_t			        startTime;
  	time_t			        endTime;
  	int 				        signal_number;
  	sigset_t 		        timer_signal;
  	int 				        wakeups_missed_count;
  	timer_t 		        timer_id;
  	int 				        timer_Period;
} ThreadArgs;

ThreadArgs g_ThreadArgs[MAX_THREAD_COUNT];

/*****************************************************************************************/

void InitThreadArgs(void)
{
	for(int i=0;i<MAX_THREAD_COUNT;i++)
	{
		g_ThreadArgs[i].threadCount = 0;
		g_ThreadArgs[i].threadId =0;
		g_ThreadArgs[i].threadPri = 0;
		g_ThreadArgs[i].processTime =0;
		for(int y=0; y<MAX_TASK_COUNT+1; y++)
		{
			g_ThreadArgs[i].timeStamp[y] = 0;
		}
	}

	pthread_mutex_init ( &g_DisplayMutex, NULL);
}

/*****************************************************************************************/

void DisplayThreadSchdAttributes( pthread_t threadID, int policy, int priority )
{

	   printf("\nDisplayThreadSchdAttributes:\n        threadID = 0x%lx\n        policy = %s\n        priority = %d\n",
			threadID,
		   (policy == SCHED_FIFO)  ? "SCHED_FIFO" :
		   (policy == SCHED_RR)	   ? "SCHED_RR" :
		   (policy == SCHED_OTHER) ? "SCHED_OTHER" :
		   "???",
		   priority);
}

/*****************************************************************************************/

void DisplayThreadArgs(ThreadArgs*	myThreadArg)
{
	int i,y;

	pthread_mutex_lock( &g_DisplayMutex );
	if( myThreadArg )
	{
			DisplayThreadSchdAttributes(myThreadArg->threadId, myThreadArg->threadPolicy, myThreadArg->threadPri);
			printf("        startTime = %s        endTime = %s", ctime(&myThreadArg->startTime), ctime(&myThreadArg->endTime));
			printf("        TimeStamp [%"PRId64"]\n", myThreadArg->timeStamp[0] );

			for(int y=1; y<MAX_TASK_COUNT+1; y++)
			{
				printf("        TimeStamp [%"PRId64"]   Delta [%"PRId64"]us     Jitter[%"PRId64"]us\n", myThreadArg->timeStamp[y],
							(myThreadArg->timeStamp[y]-myThreadArg->timeStamp[y-1]),
							(myThreadArg->timeStamp[y]-myThreadArg->timeStamp[y-1]-myThreadArg->timer_Period));
			}

	}
	pthread_mutex_unlock( &g_DisplayMutex );
}

/*****************************************************************************************/

int CreateAndArmTimer(int unsigned period, ThreadArgs* info)
{
   //TODO:

   /*
    1. Create static int variable to keep track of next available signal signal_number
    2. Initialize thread structure elements
    3. Assign next real time signal to thread signal_number
    4. Create signal mask corresponding to the chosen signal_number in "timer_signal".
      use sigemptyset and sigaddset
    5. Use timer_Create to create timer
    6. Arm timer
   */
   struct sigevent mySignalEvent;
   struct itimerspec timerSpec;
   static int next_signal;
   int ret;
   int seconds = period/1000000;
   int nanoseconds = period*1000; // Ask about sec

   mySignalEvent.sigev_notify = SIGEV_SIGNAL;
   mySignalEvent.sigev_signo = info->signal_number;
   mySignalEvent.sigev_value.sival_ptr = (void *)&(info->timer_id);
   ret = timer_create(CLOCK_MONOTONIC, &mySignalEvent, &info->timer_id);
   if(ret != 0)
    {
      handle_error_en(ret, "Timer Create");
    }

    timerSpec.it_interval.tv_sec = seconds;
    timerSpec.it_interval.tv_nsec = nanoseconds;
    timerSpec.it_value.tv_sec = seconds;
    timerSpec.it_value.tv_nsec = nanoseconds;
    ret = timer_settime(&info->timer_id, 0, &timerSpec, NULL);
    if(ret != 0)
     {
       handle_error_en(ret, "Timer Set");
     }







}

/*****************************************************************************************/

static void wait_period (ThreadArgs *info)
{
	//TODO:

  /*
    1. use sigwait function to wait on timer_signal
    2. update missed_signal_count by calling timer_getoverrun
  */
}

/*****************************************************************************************/

void* threadFunction(void *arg)
{
  pthread_mutex_lock(&g_SignalMutex);
	ThreadArgs*	myThreadArg;
	struct timeval	t1;
	struct timespec tms;
	int y, retVal;

	myThreadArg = (ThreadArgs*)arg;

	if( myThreadArg->threadId != pthread_self() )
	{
		printf("mismatched thread Ids... exiting...\n");
		pthread_exit(arg);
	}
	else
	{
		retVal = pthread_setschedparam(pthread_self(), myThreadArg->threadPolicy, &myThreadArg->param);		//SCHED_FIFO, SCHED_RR, SCHED_OTHER
		if(retVal != 0){
			handle_error_en(retVal, "pthread_setschedparam");
		}
		myThreadArg->processTime = 0;
	}

	CreateAndArmTimer(myThreadArg->timer_Period, myThreadArg);


	myThreadArg->startTime = time(NULL);

	//TODO: In a loop call "wait_period(myThreadArg);" taking a timestamp before and after using "clock_gettime(CLOCK_REALTIME, &tms);"

	time_t tmp;
	tmp = time(NULL);
	myThreadArg->endTime = time(NULL);

	DisplayThreadArgs(myThreadArg);

  pthread_mutex_unlock(&g_SignalMutex);

	pthread_exit(NULL);


	return NULL;
}

/*****************************************************************************************/

int main (int argc, char *argv[])
{
	int threadCount = 0;
  int err, i;
	int fifoPri = 60;
	int period = 1;
	int retVal;

	pthread_attr_t threadAttrib;

   sigset_t timer_signal;
   sigemptyset(&timer_signal);
   for (int i = SIGRTMIN; i <=SIGRTMAX; i++){
   sigaddset(&timer_signal, i);
   sigprocmask(SIG_BLOCK, &timer_signal, NULL);
 }


	InitThreadArgs();

   for (int i = 0; i < MAX_THREAD_COUNT; i ++)
   {
	  g_ThreadArgs[i].threadCount = i+1;
    g_ThreadArgs[i].threadPolicy = SCHED_FIFO;
    g_ThreadArgs[i].param.sched_priority = fifoPri++;
	  g_ThreadArgs[i].timer_Period = (period << i)*1000000;

    retVal = pthread_create(&g_ThreadArgs[i].threadId, NULL, threadFunction, &g_ThreadArgs[i]);
	  if(retVal != 0)
	   {
		     handle_error_en(retVal, "pthread_create");
	   }

   }

	for(int i = 0; i < MAX_THREAD_COUNT; i++)
	 {
      pthread_join(g_ThreadArgs[i].threadId, NULL);
   }

	printf("Main thread is exiting\n");

    return 0;
}

/*****************************************************************************************/
