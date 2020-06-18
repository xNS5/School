/*****************************************************************************************/
// To compile run: gcc -g Ass2_template.c -pthread -lrt
/*****************************************************************************************/
/*
  Jitter Analysis:
  The jitter is the deviation from the clock signal, which is calculated by the difference in timestamps minus the clock (which is 1000000, 2000000, and 4000000 for each thread).
  My jitter output ranged from 180 to -100, meaning that these threads have a very small difference between the desired and actual clock time, positive meaning the thread completed
  after the desired clock time and negative meaning that the thread finished before the desired clock time.
  On my first thread, the jitter was 136 and the delta was 1000136. (136/1000136) * 100 is 0.0135%, a very finite difference between the actual and desired exparation times.
  On my second thread, the jitter was -47 and the delta was 1999953, which becomes -0.00235%, which indicates that the thread completed its task ahead of the desired clock interval.
  On the last thread, The jitter was 84 and the delta was 4000084, which becomes 0.0209% the target value.
  Please note that these are just one of the three timestamps and jitters from my timestamp output.
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
#include <signal.h>
#include <errno.h>

#define	MAX_THREAD_COUNT		  3

#define LOW_THREAD_PRIORITY		50

#define STACK_SIZE				    0x400000
#define	MAX_TASK_COUNT			  3

//define error handling later on
#define handle_error_en(en, msg) \
   do {errno = en; perror(msg); exit(EXIT_FAILURE);} while (0)

// This is my own error handling function so I could maintain the one included in the original template. Instead of quitting, it just prints along with the strerror() function
#define handle_error_print(en, msg) \
  {int errnum = errno; perror(msg); strerror(errnum);}

/*****************************************************************************************/
pthread_mutex_t		g_DisplayMutex = PTHREAD_MUTEX_INITIALIZER;;
pthread_mutex_t 	g_SignalMutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct{
   int					        threadCount;
   pthread_t		        threadId;
   int					        threadPolicy;
   int					        threadPri;
   struct sched_param   param;
   long				          processTime;
   int64_t			        timeStamp[MAX_TASK_COUNT+1];
   time_t			          startTime;
   time_t			          endTime;
   int 				          signal_number;
   sigset_t 		        timer_signal;
   int 				          wakeups_missed_count;
   timer_t 		          timer_id;
   int 				          timer_Period;
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

void DisplayThreadSchdAttributes(pthread_t threadID, int policy, int priority, int missed)
{
   printf("\nDisplayThreadSchdAttributes:\n        threadID = 0x%lx\n        policy = %s\n        priority = %d\n        missed = %d\n",
         threadID,
         (policy == SCHED_FIFO)  ? "SCHED_FIFO" :
         (policy == SCHED_RR)	   ? "SCHED_RR" :
         (policy == SCHED_OTHER) ? "SCHED_OTHER" :
         "???",
         priority, missed);
}

/*****************************************************************************************/

void DisplayThreadArgs(ThreadArgs*	myThreadArg)
{
   int i,y;
   pthread_mutex_lock( &g_DisplayMutex );
   if( myThreadArg )
   {
      DisplayThreadSchdAttributes(myThreadArg->threadId, myThreadArg->threadPolicy, myThreadArg->threadPri, myThreadArg->wakeups_missed_count);
      printf("        startTime = %s ", ctime(&myThreadArg->startTime));
      printf("       endTime =   %s", ctime(&myThreadArg->endTime));
      printf("        TimeStamp %d [%"PRId64"]\n", 0, myThreadArg->timeStamp[0] );

      for(int y=1; y<MAX_TASK_COUNT+1; y++)
      {
         printf("        TimeStamp %d [%"PRId64"]   Delta [%"PRId64"]us     Jitter[%"PRId64"]us\n", y, myThreadArg->timeStamp[y],
               (myThreadArg->timeStamp[y]-myThreadArg->timeStamp[y-1]),
               (myThreadArg->timeStamp[y]-myThreadArg->timeStamp[y-1]-myThreadArg->timer_Period));
      }
   }
   pthread_mutex_unlock( &g_DisplayMutex );
}

/*****************************************************************************************/
/*
  I initialized all of my variables at the top of my function. Seconds, nanoseconds, and all of the return ints.
  I also added a check to make sure nanoseconds is less than 999999999 because itimerspec only takes values less than that.
  This function creates and arms a timer using timer_create and timer_settime. Sigemptyset initializes the signal set of info,
  and sigaddset adds the signal to the set. Then, I initialize all of the values in sigevent to create the timer in timer_create,
  then initialize all of the values in itimerspec to arm the timer with timer_settime. In order to catch all of the potential errors,
  I created a separate function at the top (lines 38 and 39) that simply prints out the error and the function that caused the error. I then
  put several calls to that function in an if statement that triggers if there is even 1 error return from the system calls. If there's one error, the function returns
  -1, pipes an ENOSYS (function not implemented) to the error handler, and quits.
*/
int CreateAndArmTimer(int unsigned period, ThreadArgs* info)
{
   struct sigevent mySignalEvent;
   struct itimerspec timerSpec;
   int seconds = period/1000000;
   int nanoseconds = (period - (seconds * 1000000))*1000;
   int ret, ret2, ret3, ret4 = 0;
   if(nanoseconds > 999999999)
   {
      printf("Nanoseconds is %d, which is greater than 999999999\r\nitimerspec requires a value less than 999999999\r\n", nanoseconds);
      handle_error_en(EOVERFLOW, "TimerSpec - Nanoseconds");
      /*
        One issue I was having was timerSpec wasn't taking seconds and nanoseconds correctly because nanoseconds was greater than 999999999.
        I implemented this check to explain that issue. Technically this wouldn't throw an EOVERFLOW error, but using EOVERFLOW as the error
        is more descriptive than "Unknown error".
      */
   }

   ret = sigemptyset(&info->timer_signal);
   ret2 = sigaddset(&info->timer_signal, info->signal_number);

   mySignalEvent.sigev_notify = SIGEV_SIGNAL;
   mySignalEvent.sigev_signo = info->signal_number;
   mySignalEvent.sigev_value.sival_ptr = (void *)&(info->timer_id);
   ret3 = timer_create(CLOCK_MONOTONIC, &mySignalEvent, &info->timer_id); // Have if statement, if success, do timer_settime. Else return -1 so
                                                                          // Don't have to check for other ret values. Potential to cause a crash
   if(!ret3)
   {
     timerSpec.it_interval.tv_sec = seconds;
     timerSpec.it_interval.tv_nsec = nanoseconds;
     timerSpec.it_value.tv_sec = seconds;
     timerSpec.it_value.tv_nsec = nanoseconds;
     ret4 = timer_settime(info->timer_id, 0, &timerSpec, NULL);
     return 0;
   }
   // if(ret || ret2 || ret3 || ret4)
   // {
   //    printf("Errors:\r\n");
   //    printf("========================\n");
   //    if(ret)
   //    {
   //      handle_error_print(ret, "Sig Empty Set");
   //    }
   //    if(ret2)
   //    {
   //       handle_error_print(ret2, "Sig Add Set");
   //    }
   //    if(ret3)
   //    {
   //      handle_error_print(ret3, "Timer Create");
   //    }
   //    if(ret4)
   //    {
   //      handle_error_print(ret4, "Set Timer");
   //    }
   //    printf("========================\r\n");
   //
   //    return -1;
   // }
   return -1;
}

/*****************************************************************************************/
/*
  This function waits on the thread until a signal in the set becomes pending.
  This function also counts the number of times the thread gets overrun, in which case it gets
  added to the thread's overrun count.
*/

static void wait_period (ThreadArgs *info)
{
   int ret, ret2;
   ret = sigwait(&info->timer_signal, &info->signal_number);
   ret2 = timer_getoverrun(info->timer_id);
   if(ret2 || ret)
   {
      if(ret){
         handle_error_en(ret, "Signal Wait");
      }
      if(ret2 == -1){
         handle_error_en(ret2, "Timer Get Overrun"); //If timer_getoverrun returns a positive value, that's the count of the overrun timers which would set off the if statement.
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

   if(myThreadArg->threadId != pthread_self() ){
      printf("mismatched thread Ids... exiting...\n");
      pthread_exit(arg);
   }

   else{
      retVal = pthread_setschedparam(pthread_self(), myThreadArg->threadPolicy, &myThreadArg->param);
      if(retVal != 0){
         handle_error_en(retVal, "pthread_setschedparam");
      }
      myThreadArg->processTime = 0;
   }

   retVal = CreateAndArmTimer(myThreadArg->timer_Period, myThreadArg);
   if(retVal){
      handle_error_en(ENOSYS, "Create and Arm Timer"); // I had it return ENOSYS when retVal = -1 so that the program doesn't continue past this point.
                                                       // My reasoning is that if the timer wasn't successfully created and armed, the program shouldn't continue.
   }

   myThreadArg->startTime = time(NULL);
   for(int i = 0; i < MAX_TASK_COUNT; i++){
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
   int fifoPri = 60;
   int period = 1;
   int retVal;
   timer_t timer;

   pthread_attr_t threadAttrib;
   retVal = pthread_attr_init(&threadAttrib); // I noticed that you create a pthread_attr_t variable but never used it, so I figured I'd initialize and implement it.
   if(retVal){
     handle_error_en(retVal, "Pthread Attribute Initializer");
   }

   sigset_t timer_signal;
   sigemptyset(&timer_signal);
   for (int i = SIGRTMIN; i <=SIGRTMAX; i++){
     err = sigaddset(&timer_signal, i);
     if(err){
        handle_error_en(err,"Sig Addset");
      }
    }
   err = sigprocmask(SIG_BLOCK, &timer_signal, NULL);
   if(err){
      handle_error_en(err, "Sigproc Mask");
   }

   InitThreadArgs();

   for (int i = 0; i < MAX_THREAD_COUNT; i ++){
      g_ThreadArgs[i].threadCount = i+1;
      g_ThreadArgs[i].threadPolicy = SCHED_FIFO;
      g_ThreadArgs[i].param.sched_priority = fifoPri++;
      g_ThreadArgs[i].threadPri = fifoPri++;
      g_ThreadArgs[i].timer_Period = (period << i)*1000000;
      g_ThreadArgs[i].signal_number = SIGRTMIN + i;
      retVal = pthread_create(&g_ThreadArgs[i].threadId, &threadAttrib, threadFunction, &g_ThreadArgs[i]);
      if(retVal != 0){
         handle_error_en(retVal, "pthread_create");
      }
   }

   for(int i = 0; i < MAX_THREAD_COUNT; i++){
      pthread_join(g_ThreadArgs[i].threadId, NULL);
   }

   printf("Main thread is exiting\n");

   return 0;
}

/*****************************************************************************************/
