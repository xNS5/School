///////////////////////////////////////////////////////////////////////////////////
// Author: Michael Kennedy                                                       //
// Class: CSCI140                                                                //
// Assignment: Week 9, Lab 8                                                     //
// Program description: Randomly generates 100 numbers of integers between 0 and //
// 19, then at the user's char input, either sorts from min to max, or vice      //
// versa.                                                                        //
// Input: Character input from the user, a, b or c.                              //
// Processing: randomly generates integers between 0 and 19                      //
// Output: Either numbers arranged from maximum to minimum, randomly generated,  //
// or from minimum to maximum.                                                   //
//////////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <stdlib.h> // library to get rand for generating random integers.
#include <time.h>
using namespace std;


int menuOptions(); // function for the menu
void action(char); // function for the action taken after user input
void numberArray(); // function for the array
void maxToMin(int[]); // Orders numbers from biggest to smallest
void minToMax(int[]); // Orders numbers from smallest to largest 
int numbers[100]; // size of the array
char choice;// declaring the choice as a char


int main()
{
        choice = menuOptions();
        action(choice);
}

////////////////////////////////
// Menu function 
//
///////////////////////////////

int menuOptions()
{
        char choice;
        cout << endl << endl; // double space because I can.
        cout << "This program sorts 100 random numbers generated between 1 and 20" << endl;
        cout << "Please choose an option:" << endl;
        cout << "A. Do not sort, output as is." << endl;
        cout << "B. Sort numbers greatest to smallest, then print." << endl;
        cout << "C. Sort numbers smallest to greatest, then print." << endl;
        cout << "D. Exit" << endl;
        cout << "Please select one: ";
        cin >>  choice;
        
        return choice;
}
/////////////////////////////////////////////////
//function that triggers other functions based on char input.
//
//
//
/////////////////////////////////////////////////
void action(char choice)
{ 

    int i;
    int numbers[100]; // creates the array of random integers.
    srand(time(NULL));
    for (int i = 0; i < 100; i++) //
        {
            numbers[i] = (rand() % 20);
        }
            cout << endl;
    
    if (choice == 'A' || choice == 'a') // outputs all of the random integers
        {
            
        for (int i = 0; i < 100; i++)
            {
                numbers[i] = (rand() % 20);
                cout << numbers[i] << " ";
            }
           
            main(); 
        }
    if (choice == 'B' || choice == 'b') // outputs the integers ordered from max to min
        {
            maxToMin(numbers);
            main();
        }
    if (choice == 'C' || choice == 'c')// orders the integers from min to max
        {
            minToMax(numbers);
            main();
        }
    if (choice == 'D' || choice == 'd')
        {
            cout << "Thank you! Have a good day." << endl;
        }
        

  
    
}

//////////////////////////////////
//function that arranges all of the numbers from min to max
//
//
//
///////////////////////////////
void minToMax(int numbers[100])
{
    int size = 100;
    int temp;
    int smallest;
    int firstPos;
                            
     for (int i = 0; i < size ; i ++)
    { 
        firstPos = i;      
        smallest = numbers[i];
        temp = firstPos;
        
        for (int j = i; j < size; j ++)
        {
            if (smallest > numbers[j]) 
            {
                smallest = numbers[j];
                temp = j;
            }
        }
        if (temp != firstPos)
        {      
            numbers[temp] = numbers[firstPos];
            numbers[firstPos] = smallest;
        }
    }
    for (int i = 0; i< size; i ++)
    {
        
        cout << numbers[i] << ", ";
    }
}
/////////////
// function that arranges numbers from max to min.
//
//
////////////

void maxToMin (int numbers[100])
{
    
    int size = 100;
    int temp;
    int max;
    int lastPos;

     for (int i = 0; i < size ; i ++)
    { 
        lastPos = i;      
        max = numbers[i];
        temp = lastPos;
        
        for (int j = i; j < size; j ++)
        {
            if (max <= numbers[j]) 
            {
                max = numbers[j];
                temp = j;
            }
        }
        if (temp != lastPos)
        {      
            numbers[temp] = numbers[lastPos];
            numbers[lastPos] = max;
        }
    }
    for (int i = 0; i < size; i ++)
    {
        
        cout << numbers[i] << ", ";
    }
}
                    

    
    