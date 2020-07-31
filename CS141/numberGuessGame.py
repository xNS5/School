# Author : Michael Kennedy
# Description : CSCI 141, Homework 3
# This program is another guessing game, however in this case the user is guessing 3 digits instead of
# 1. The program randomly generates 3 digits, and the user has to guess one of three each turn. The maximum
# number of tries the user is allowed to have has to be between 2 and 4. If the user guesses the digits, they get a
# congratulatory message. If they lose, they get a loser message. Both messages print out the digits as a 3 digit number.
# 
# Date: 10/16/2017

import random		                                                                            # Allows me to select random numbers													
print ("This is a number guessing game. The objective is to guess the number I'm thinking of",      # Dialogue describing the game
       "HOWEVER-- you will be guessing the digits in a three-digit-number.")                        # Dialogue pt. 2
tries = int(input("How many tries would you like? (enter a number from 2-4) "))                     # Inputs the number of tries the user wishes to have
                                                                                    
while (tries < 2 or tries > 4):							                    # If the user inputs a number outside of the required range, the program loops.
    print ("I'm sorry, that doesn't work. Please try again!")
    tries = int(input("How many tries would you like? (enter a number from 2-4) "))
								
            
else:                                                                                               # If the user inputs a value inside of the required range
    for x in range (1):										   
        randomInt_1 = random.randint(0,9)                                                           # First random integer generated between 0 and 9 
        randomInt_2 = random.randint(0,9)                                                           # Second random integer generated between 0 and 9
        randomInt_3 = random.randint(0,9)                                                           # Third random integer generated between 0 and 9   
        
    print ("Okay, I've chosen a number. Good luck!")                                                # Dialogue												


check_1 = False                                                                                     # Setting 3 bools for each integer -- all evaluating to false
check_2 = False             
check_3 = False
for x in range (0, tries):	                                                                    # Loops for x number of tries											         
    print ("Try number",x+1,":", end= " ")							    # Lists the try number and brings up the input to the same line
    guess = int(input())

    if (guess == randomInt_1 and check_1 == False):                                                 # If the guess equals the first generated integer and the bool flag evaluates to false
        print ("You guessed the first digit!")                                                      # Dialogue
        complete_1 = guess                                                                          # Assigns the guess to a variable
        check_1 = True                                                                              # Changes the bool to True so this message doesn't show again

    if (guess != randomInt_1 and check_1 == False):                                                 # If the guess isn't equal to the generated integer and the bool evaluates to false
        print ("The first digit is not", guess)                                                     # Prints that the value isn't the generated integer

    if (guess == randomInt_2 and check_2 == False):                                                 # If the guess equals the first generated integer and the bool flag evaluates to false
        print ("You guessed the second digit!")                                                     # Dialogue
        complete_2 = guess                                                                          # Assigns the guess to a variable
        check_2 = True                                                                              # Changes the bool to True so this message doesn't show again
        
    if (guess != randomInt_2 and check_2 == False):                                                 # If the guess isn't equal to the generated integer and the bool evaluates to false
        print ("The second digit is not", guess)                                                    # Prints that the value isn't the generated integer

    if (guess == randomInt_3 and check_3 == False):                                                 # If the guess equals the first generated integer and the bool flag evaluates to false
        print ("You guessed the third digit!")                                                      # Dialogue
        complete_3 = guess                                                                          # Assigns the guess to a variable
        check_3 = True

    if (guess != randomInt_3 and check_3 == False):                                                 # If the guess isn't equal to the generated integer and the bool evaluates to false
        print ("The third digit isn't", guess)                                                      # Prints that the value isn't the generated integer

if (check_1 == True and check_2 == True and check_3 == True):                                       # Win message, if all of the bools evaluate to True this will execute
    print("Win! You guessed my number ", complete_1, complete_2, complete_3, ".", sep="")           # Prints a win message as well as the numbers

elif (check_1 == False or check_2 == False or check_3 == False):                                    # Lose message, if even one of the flags evaluate to False this will execute
    print ("Whoops! Looks like you didn't guess my number. I chose ", randomInt_1, randomInt_2, randomInt_3, ".", sep="") # Prints out the error message with the actual number


     


    
 

    
    

        
    



        

    
        

            
            
        

        

    
    

    

        
    

        
    
     

        



        
    
