

import random

def generator(animal, position, number ):

    number = str(number)
    
    if (animal == 1):
        animal = "monkey"
    elif (animal == 2):
        animal = "dragon"
    elif (animal == 3):
        animal = "snake"
    if (position == 1):
        string = animal + number
    elif (position == 2):
        string = number + animal

    return string

def checker(lotteryString, word, number):

    done = False

    while (done == False):

        if ((str(number) == word[0]) and (word[1:len(word)] == lotteryString[1:len(lotteryString)])):
            print ("You guessed the word! Congratulations, you win the big bucks!")
            break
        elif ((str(number) == word[len(word)-1]) and (word[0:(len(word)-1)] == lotteryString[0:(len(lotteryString)-1)])):
            print ("You guessed the word! Congratulations, you win the big bucks!")
            break
        elif ((str(number) in word and str(number) != word[0]) and (word[1:len(word)] == lotteryString[1:len(lotteryString)])):
            print ("You guessed the right word and number but in the wrong order!")
            done = False
        elif (str(number) in word and (str(number) != word[len(word)-1] and (word[1:len(word)-1] == lotteryString[1:len(lotteryString)]))):
            print ("You guessed the right word and number but in the wrong order!")
            done = False
        elif ((str(number) == word[0]) and (word[1:len(word)] != lotteryString[1:len(lotteryString)])):
            print ("You've guessed the right number, but the wrong word!")
            done = False
        elif ((str(number) == word[len(word)-1]) and (word[0:(len(word)-1)] != lotteryString[0:(len(lotteryString)-1)])):
            print ("You've guessed the right number, but the wrong word!")
            done = False
        elif ((str(number) != word[0]) and (word[1:len(word)-1] != lotteryString[1:len(lotteryString)-1])):
            print ("I'm sorry, both number and animal were wrong. Please try again!")
            done = False
        elif ((str(number) != word[len(word)-1]) and (word[0:(len(word)-1)] != lotteryString[0:(len(lotteryString)-1)])):
            print ("I'm sorry, both number and animal were wrong. Please try again!")
            done == False
        elif (word == 'done' or word == 'Done'):
              break
        word = str(input("What word/number combination did you select? "))
            
def main():

    animal = random.randint(1,3)
    number = random.randint(1,3)
    position = random.randint(1,2)
    
    print ("Lottery pic checker V4.20. Let's see if you've won a fabulous prize.")
    print ("The word choices were monkey, dragon, and snake, and the digit choices were 1, 2, or 3.")
    print (" The winning pick is a word and a digit, in any order.")
    lotteryString = generator(animal, position, number)
    print (lotteryString)
    word = str(input("What word/number combination did you select? (please type 'done' to quit) "))

    if (word == 'done' or word == 'Done'):
        print("goodbye!")
    else:
        checker(lotteryString, word, number)
    
    
    
        

main()
