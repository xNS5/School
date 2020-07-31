///////////////////////////////////////////////////////////////////////////////////
// Author: Michael Kennedy                                                       //
// Class: CSCI140                                                                //
// Assignment: Week 8, Lab 7                                                     //
// Program description: 2 players compete to see who will be the winner by       //
// deducting rocks from 1 of 3 piles, each pile containing a different number of //
// rocks.                                                                        //
// Input: The users input their names, which pile of rocks they wish to deduct   //
// rocks from, and the number of rocks they wish to remove.                      // 
// Processing: Removes the number of rocks from the chosen pile, switches player,//
// then removes rocks from player 2's pile.                                      // 
// Output: Outputs number of remaining rocks in pile and the player name. When   //
// all piles are zero, the program declares the player who deducted the last     //
// rock from the pile to be the loser, and the other player                      //
///////////////////////////////////////////////////////////////////////////////////


#include <iostream> //input/output
#include <string>  //Allows the program to take names as input/output
using namespace std;


void printRocks(int piles[3]); // Prints the number of rocks left in the pile
int main()
{
    string player1, player2; //allows the users to input their names/allows the program to print their names
    int pile=0, rocks=0, initPile=3, count = 0; // setting piles, rocks, initializing pile and count to integers
    bool flag = false;
    int piles[3];
    string players[2]; // Array of two strings
    
   
cout << endl;      
cout << "Player 1 please enter your name:  ";
cin >> players[0]; //First player name
cout << "Player 2 please enter your name:  ";
cin >> players[1]; // second player name
     
for (int i =0; i < 3; i++) // creating 3 piles
{
   piles[i] = initPile;
   initPile = initPile + 2 + i;
}
printRocks(piles); // prints the number of piles and the number of rocks in that pile

do 
{
   if ( piles[0] > 0 || piles[1] > 0 || piles[2] > 0)
   {
       
    cout << players[count % 2] << ", it's your turn" << endl;
    	cout << endl << "Choose a pile: " ;
	cin >> pile; 
        
        if (pile < 0 || pile > 3) // If the user chooses a pile that doesn't exist, this if statement loops until they select a valid pile.
        {
            do
                {
                    
                    cout << endl << "Sorry, that pile doesn't exist." << endl;
                    printRocks(piles);
                    cout << endl << "Choose a pile: " ;
                    cin >> pile;
                }while (pile < 0 || pile > 3);
        }
        
	cout << endl << "Number of rocks: ";
	cin >> rocks; 
        
        switch (pile) // switch function subtracting rocks from the array
        {
            
            case 1:
                    piles[0] = piles[0]-rocks; // subtracting rocks from pile 1
                    cout << "You took away " << rocks << " rocks from " << "Pile 1." << endl << endl;
                    break;
            case 2:
                    piles[1] = piles[1] - rocks; // subtracting roks from pile 2
                    cout << "You took away " << rocks << " rocks from " << "Pile 2." << endl << endl;
                    break;
            case 3:
                    piles[2] = piles[2] - rocks; // subtracting rocks from pile 3
                    cout << "You took away " << rocks << " rocks from " << "Pile 3." << endl << endl;
                    break;
                    
        }
////////////
// If the number of rocks goes below 0, the rocks in that
// specific pile get re-initialized to zero.
////////////
        
         if (piles[0] < 0)
            {
                cout << "Sorry, you cannot take any more rocks from this pile!" << endl;
                piles[0] = 0;
            }
        
        if (piles[1] < 0)
            {
                cout << "Sorry, you cannot take any more rocks from this pile!" << endl;
                piles[1] = 0;
            }
        if (piles[2] < 0)
            {
                cout << "Sorry, you cannot take any more rocks from this pile!" << endl;
                piles[2] = 0;
            }
      

    printRocks(piles); // prints the remaining rocks in the piles
    
     count = count + 1;     // repeats the loop
     } 
     flag = false;
  }while(piles[0] > 0 || piles[1] > 0 || piles[2] > 0); // loops while all of the arrays are greater than zero
        
cout << players[count % 2] << " is the winner!" << endl; // declaring the user who didn't take the last rock as the winner. 
                 
return 0;
}     
   
///////////////////////
// This is an array that outputs the number of rocks per pile.
//
//
//////////////////////
void printRocks (int piles[3])
{
        cout << endl;
        cout << "Pile 1 = " << piles[0] << " rocks." << endl;
        cout << "Pile 2 = " << piles[1] << " rocks." << endl;
        cout << "Pile 3 = " << piles[2] << " rocks." << endl;
        
}


            
        
    