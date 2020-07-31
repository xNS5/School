# Author : Michael Kennedy
# Description : CSCI 141, Homework 3

# Date: 10/16/2017


def printSalutation(personName):
    print ("Welcome,", personName,"!", sep = "")
    print ("Let's practice addition, multiplication, and division!")
    print ("Once you start, type -1 to quit")


def addition():


while (additionSum != -1 and additionSum >= 0):

    for x in range (1):
        randInt_1 = random.randint(0,9)                                                 
        randInt_2 = random.randint(0,9)
        total = (randInt_1 + randInt_2)
                
    print ("What is", randInt_1, "and", randInt_2, "?", sep = " ", end= "")
    additionSum = int(input())


    if (additionSum == total):
        print("Good job. Correct!")

    elif (additionSum != total):
        print("Good try, but incorrect.")
        print("The correct answer was", total)

    elif (additionSum == -1):
        break




