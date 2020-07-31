#Author: Michael Kennedy
#Description: This program creates a DJ name based on the user's first and last name.
# First thing this program does is it checks for whether there is a space in the string, and if the user tried
# to put only their first name and added a space afterword. If the end of the string is a character or anything that isn't a space
# the program returns True. Then the program splits the first name and the last name and assigns them to individual variables.
# The program then splits up the two strings and combines them based on how long the strings are.
#
# Date: 11/19/17


def isInputValid():                                                                                                 # Custom function that checks whether the input is valid

    done = False                                                                                                    # Sets a false bool flag

    fullName = str(input("What is your first and last name? "))                                                     # User inputted string
    while (done == False):                                                                                          # While loop that only executes while the flag remains False
        
        if ( " " not in fullName and fullName[len(fullName)-1] != " "):                                             # If a space isn't in the full name and the end of the string isn't a space, the bool remains False
            print("Incorrect input. A single space is required. Please try again.")
            done == False
        elif ( " " in fullName and fullName[len(fullName)-1] == " "):                                               # If a space is in the full name and the end of the string is a space, then the bool remains False
            print("Incorrect input. Please enter a full name, without a space at the end. Please try again.")
            done == False
        elif(" " in fullName and fullName[len(fullName)-1] != " "):                                                 # If the space is in the full name and the end of the string isn't a space, the bool turns to True
            done == True
            break                                                                                                   # Breaks the loop.
       
        
        fullName = str(input("What is your first and last name? "))                                                 # Allows the user to re-input the string

    return fullName                                                                                                 # Returns the string


def generateDjName(fullName):                                                                                       # Function that generates the Dj name

    space = fullName.index(" ")                                                                                     # Finds and indexes the space in the fullName string
    firstName = fullName[0:space]                                                                                   # The first name will be before the space and it assigns that to the variable firstName
    lastName = fullName[space+1: len(fullName)]                                                                     # The last name will be the remainder of the string, so it goes from the space to the end of the string

    if ((len(firstName) % 2 != 0) and (len(lastName) % 2 != 0)):                                                    # If the length of the first and last name was odd
        djName_1 = firstName[0:len(firstName)//2]                                                                   # The first part of the DJ name goes from 0 to the integer 1/2 the first name
        djName_2 = lastName[(len(lastName)//2):len(lastName)]                                                       # The latter part of the DJ name goes from the integer 1/2 point of the last name to the end of the last name
    elif((len(firstName) % 2 == 0) and (len(lastName) % 2 != 0)):                                                   # If the first name is even and the last name is odd                                            
        djName_1 = firstName[0:(len(firstName)//2)]                                                                 # The first part of the DJ name is the first hald of the word
        djName_2 = lastName[(len(lastName)//2):len(lastName)]                                                       # The latter part of the DJ name is the integer 1/2 of the name to the very end of the string
    elif((len(firstName) % 2 == 0) and (len(lastName) % 2 == 0)):                                                   # If both the first and last names are both even length strings
        djName_1 = firstName[0:(len(firstName)//2)]                                                                 # The first part of the Dj name is 1/2 of the first name 
        djName_2 = lastName[(len(lastName)//2):len(lastName)]                                                       # The latter part of the Dj name is the last half of the last name
    elif((len(firstName) % 2 != 0) and (len(lastName) % 2 == 0)):                                                   # If the first part of the Dj name is odd and the last name is even
        djName_1 = firstName[0:len(firstName)//2]                                                                   # The first part of the DJ name is the first integer half of the first name
        djName_2 = lastName[(len(lastName)//2):len(lastName)]                                                       # The latter part of the DJ name is the last half of the last name to the end of the string. 
        

    print("Your Dj name is:", djName_1 + djName_2 + "Sizzle")                                                       # Prints the final Dj name

            
def main():                                                                                                         # Main function
    fullName = isInputValid()
    generateDjName(fullName)

main()
