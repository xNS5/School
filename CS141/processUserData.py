# Author: Michael Kennedy
#Description: CSCI141 Hw6
# This program imports from a text file and outputs one fo 5 things: keyword count of a specific word, calculate average coding experience,
# calculate average experience based on a specific word, calculate people familiar with for loops and calculate number of first generation students.
# Each individual task calculates a specific action. 
#
# Date: 12/06/17

# This function basically creates the list where the function is going to sit for the entirety of the program, separated by the "|" symbol.
def allMightyListCreator():
    mainList = []
    fileName = input("File name to parse: ")
    fileInput = open(fileName, "r")
    for line in fileInput:
        line = line.split("|")          
        mainList.append(line)
    fileInput.close()
    return mainList

# Simple options menu that returns a single integer to main()
def optionsMenu():
    print("============================================================")
    print("Please select an analysis option:")
    print()
    print("1. Perform a keyword count from the answers to question 3.")
    print("2. Calculate the average months of coding experience from among the survey responses")
    print("3. Calculate average months of coding experience among all students who have a specific keyword in their answer to question 3.")
    print("4. Calculate the percent of students familiar with for loops.")
    print("5. Calculate the percent of students who are first generation college students.")
    print("6. Quit")
    print()
    optionChoice = int(input("What analysis do you want to perform? "))
    return optionChoice


#Counts the number of people who said a specific word. For example, there are 6 people who mentioned anything about microsoft in this text file.
# The specific keyword is located in question 3, so position 2 in the sublist. If the program registers true for a specific word, then it'll increase the
# counter by one
def keywordCount(mainList, length):
    keyWord = 0
    word = str(input("What is they keyword in the response to question 3?  "))
    for x in range(length):
        if (word in mainList[x][2].lower()):
            keyWord = keyWord + 1
    if(keyWord == 1):
        print("There is no mention of the word", word,"in this text file.")
    else:
        print("This program has", keyWord, "mention(s) of", word)

#Calculates the average coding experience in months. If there is a non-zero integer in sublist position 0, then it'll be added to the months counter.
# If the position zero in [0] starts with a character, it passes and just increments the people counter.
def averageMonthsExp(mainList, length):
    months = 0
    people = 0
    print()
    for x in range(length):
        if (mainList[x][0][0].isalpha() or mainList[x][0][0] == 0):
            people += + 1
        elif(mainList[x][0][0].isdigit() and mainList[x][0][0] != 0):
            months += (float(mainList[x][0][0]))
            people += 1
    average = float(months/people)
    average = str(round(average,2))
    print("The average number months of coding is:",average, "months")


#This function calculates the average coding experience of people based on a specific word. So, any mention of the word will increase the months experience counter and the number of people counter,
# and if there is a character starting at position [0][0] and the word is not mentioned, just the counter increases.
def averagePlusKeyword(mainList, length):
    months = 0
    keyWord = 0
    people = 0
    word = str(input("What is they keyword in the response to question 3?  "))
    for x in range(length):
        if (word in mainList[x][1]):
            keyWord = keyWord + 1
        if (mainList[x][0][0].isalpha() or mainList[x][0][0] == 0 and word not in mainList[x][2]):
            people += + 1
        elif((mainList[x][0][0].isdigit() and mainList[x][0][0] != 0) and word in mainList[x][2]):
            months += (float(mainList[x][0][0]))
            people += 1
    average = float(months/people)
    average = str(round(average,2))
    print("The average number months of coding is:",average, "months")

# This function calculates the number of people who responded "yes" or some variation to whether they knew what a for loop whas.
# Every time the program finds a positive response, it increments the confirm counter and the people counter, whereas the other one just
# increases the people counter. The program finally averages the results then prints that to the screen.
def loopyLoopy(mainList, length):
    confirm = 0
    people = 0
    for x in range (length):
        if (mainList[x][1].lower() == "yes" or mainList[x][1].lower() == "yeah"):
            people += 1
            confirm += 1
        elif(mainList[x][1].lower() == "no" or mainList[x][1].lower() != "yes" ):
            people += 1

    averageConfirm = (confirm/people)
    averageConfirm = round(averageConfirm, 2)
    print("On average,",averageConfirm,"people know for for loops are")

# This function basically finds the number of people who included "yes" in their response to whether they were first generation students and increments both
# the confirm and people counter, whereas if there isn't "yes" in the list then it just increments the people counter. It finally calculates and prints the percent of students.
def firstGen(mainList, length):
    confirm = 0
    people = 0
    for x in range (length):
        if ("yes" in (mainList[x][3].lower())):
            people += 1
            confirm += 1
        elif(mainList[x][3].lower() != "yes"):
            people += 1
    averageConfirm = (confirm/people)*100
    averageConfirm = round(averageConfirm, 2)
    print("% Of respondents who are first generation college students:", averageConfirm,"%")


#Main function that calls the other functions based on user input. Once the function runs, it continues looping until the user inputs 6 which breaks the loop.
def main():
    mainList = allMightyListCreator()
    length = len(mainList)
    while (0 == 0):
        option = optionsMenu()
        if(option == 1):
            keywordCount(mainList, length)
        elif(option == 2):
            averageMonthsExp(mainList, length)
        elif(option == 3):
            averagePlusKeyword(mainList, length)
        elif(option == 4):
            loopyLoopy(mainList, length)
        elif(option == 5):
            firstGen(mainList, length)
        elif(option == 6):
            break
        

main()
