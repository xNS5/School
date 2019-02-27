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
        do { errno = en; perror(msg); NULL; } while (0)

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
			printf("        startTime = %s ", ctime(&myThreadArg->startTime));
         printf("       endTime =   %s", ctime(&myThreadArg->endTime));
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

   /*
      1) Find way to initialized info->sig to have a unique value b/t SIGRTMIN and SIGRTMAX
      2) Ensure that this initialization is thread safe
      3) Initialize info->timer_Period to the argument period
      4) Use "sigemptyset" to initialize info->alarm_sig
      5) Use "signaddset" to add info->sig to info->alarm_sig
      6) Initialize local variable "mySignalEvent" and Call "timer_create" to create timer
      7) Initialize local variable "timerSpec" and call "timer_settime" to set the time out

      The info->sig is initialized in main. Because there is a mutex lock and unlock in thread function,
      I can assume that this function is thread safe. Sigemptyset and Sigaddset* are functioning and aren't returning values.
      Added additional return ints to catch errors.
   */
   struct sigevent mySignalEvent;
   struct itimerspec timerSpec;
   int seconds = period/1000000;
   int nanoseconds = (period - (seconds * 1000000))*1000;
   int ret, ret2, ret3;
   if(nanoseconds > 999999999)
   {
     printf("Nanoseconds is %d, which is greater than 999999999\r\n", nanoseconds); // For some reason my timer_settime wasn't working because the nanosecond was
                                                             // greater than 999999999. I fixed it and I put in this just to check.
     printf("itimerspec requires a value less than 999999999\r\n");
   }

   ret2 = sigemptyset(&info->timer_signal);
   ret3 = sigaddset(&info->timer_signal,info->signal_number);

   if(ret2 || ret3)
    {
      if(ret2)
        {
          handle_error_en(ret2, "Sig Empty Set");
        }
      if(ret3)
        {
          handle_error_en(ret3, "Sig Add Set");
        }
    }

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
    ret = timer_settime(info->timer_id, 0, &timerSpec, NULL);
    if(ret != 0)
     {
       handle_error_en(ret, "Timer Set");
     }

    return 0;
}

/*****************************************************************************************/

static void wait_period (ThreadArgs *info)
{
  int ret, ret2;
  /*
  1) Call "sigwait" to wait on info->alarm_sig
	2) Update "info->wakeups_missed" with the return from "timer_getoverrun"
  */
  ret = sigwait(&info->timer_signal, &info->signal_number);
  ret2 = timer_getoverrun(info->timer_id);
  if(ret2 || ret)
    {
      if(ret){
        handle_error_en(ret, "Signal Wait");
      }
      if(ret2 <= -1){
          handle_error_en(ret2, "Timer Get Overrun");
        }
    }
    else{
      info->wakeups_missed_count += ret2;
    }

}

/*****************************************************************************************/

void* threadFunction(void *arg)
{
  pthread_mutex_lock(&g_SignalMutex);
	ThreadArgs*	myThreadArg;
	struct timeval	t1;
	struct timespec tms;
	int retVal;

	myThreadArg = (ThreadArgs*)arg;

	if( myThreadArg->threadId != pthread_self() )
	{
		printf("mismatched thread Ids... exiting...\n");
		pthread_exit(arg);
	}
	else
	{
		retVal = pthread_setschedparam(pthread_self(), myThreadArg->threadPolicy, &myThreadArg->param); // Change param to a val < 60
		if(retVal != 0){
			handle_error_en(retVal, "pthread_setschedparam");
		}
		myThreadArg->processTime = 0;
	}

	CreateAndArmTimer(myThreadArg->timer_Period, myThreadArg);
	myThreadArg->startTime = time(NULL);
	//TODO: In a loop call "wait_period(myThreadArg);" taking a timestamp before and after using "clock_gettime(CLOCK_REALTIME, &tms);"
  for(int i = 0; i < MAX_TASK_COUNT; i++)
  {
    clock_gettime(CLOCK_REALTIME, &tms);
    myThreadArg->timeStamp[i] = tms.tv_sec *1000000;
    myThreadArg->timeStamp[i] += tms.tv_nsec/1000;
    if(tms.tv_nsec % 1000 >= 500 ) myThreadArg->timeStamp[i]++;

    wait_period(myThreadArg);

   clock_gettime(CLOCK_REALTIME, &tms);
	myThreadArg->timeStamp[i+1] = tms.tv_sec *1000000;
	myThreadArg->timeStamp[i+1] += tms.tv_nsec/1000;
  if(tms.tv_nsec % 1000 >= 500 ) myThreadArg->timeStamp[i+1]++;
  }
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
	int fifoPri = 20; // Change this
	int period = 1;
	int retVal;
  timer_t timer;

	pthread_attr_t threadAttrib;

   sigset_t timer_signal;
   sigemptyset(&timer_signal);
   for (int i = SIGRTMIN; i <=SIGRTMAX; i++){
   sigaddset(&timer_signal, i);
 }
   retVal = sigprocmask(SIG_BLOCK, &timer_signal, NULL);
   if(retVal)
      {
         handle_error_en(retVal, "Sigproc Mask");
      }

	InitThreadArgs();

   for (int i = 0; i < MAX_THREAD_COUNT; i ++)
   {
	  g_ThreadArgs[i].threadCount = i+1;
    g_ThreadArgs[i].threadPolicy = SCHED_FIFO;
    g_ThreadArgs[i].param.sched_priority = sched_get_priority_max(SCHED_FIFO);
	  g_ThreadArgs[i].timer_Period = (period << i)*1000000;
    g_ThreadArgs[i].signal_number = SIGRTMIN + i;
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
