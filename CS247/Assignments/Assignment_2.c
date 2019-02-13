//Add following elements to Thread structure in Assignment 1
int signal_number;
sigset_t timer_signal;
int missed_signal_count;
timer_t timer_Id;
int timer_Period;
//Add following new functions
CreateAndArmTimer(int unsigned period, ThreadArgs* threadInfo)
{
//Create a static int variable to keep track of the next available signal number
//Initialize the thread structure elements
//Assign the next available Real-time signal to thread “signal_number”
//Create the signal mask corresponding to the chosen signal_number in “timer_signal”
//Use “sigemptyset” and “sigaddset” for this
//Use timer_Create to create a timer – See code in background section
//Arm Timer – See code in background section
}
WaitForTimer(ThreadArgs* threadInfo)
{
//Use sigwait function to wait on the “timer_signal”
//update missed_signal_count by calling “timer_getoverrun”
}
