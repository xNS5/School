
from copy import deepcopy

template = ["name",["works"],["coins"],["bills"],["misc"]]
stats = [ ]
counter = 0

while True :

    fileName = input("File name to parse ")
    
    if fileName == "done" or fileName == "Done" :
        break

    fileInput = open(fileName,'r')

    for line in fileInput:

        words = line.split(" ")
            
        if words[0] == "works" :
            workTitle = " ".join(words[1:])
            stats[counter][1].append(workTitle)
            
        elif words[0] == "coins" :
            stats[counter][2].append(int(words[1]))
            stats[counter][2].append(int(words[2]))
            stats[counter][2].append(int(words[3]))
            stats[counter][2].append(int(words[4]))

        elif words[0] == "bills" :
            stats[counter][3].append(int(words[1]))
            stats[counter][3].append(int(words[2]))
            stats[counter][3].append(int(words[3]))
            stats[counter][3].append(int(words[4]))
            stats[counter][3].append(int(words[5]))
            stats[counter][3].append(int(words[6]))
            
        elif words[0] == "misc" :
            miscItem = " ".join(words[1:])
            stats[counter][4].append(miscItem)

        elif len(words[0]) > 0 :
            newUser = deepcopy(template)
            stats.append(newUser)
            stats[counter][0] = words[0]
            
    counter = counter + 1

# TO DO 4 : calculate and print the total "money" among all people info
# whose files have been processed
#
# WRITE YOUR CODE HERE - fewer than 20 lines

totalMoney = 0

for x in range (counter):

    pennies = stats[x][2][1] * 0.01
    nickels = stats[x][2][2] * 0.05
    dimes = stats[x][2][3] * 0.1
    quarters = stats[x][2][4] * 0.25
    one = stats[x][3][1] * 1
    five = stats[x][3][2] * 5
    ten = stats[x][3][3] * 10
    twenty = stats[x][3][4] * 20
    fifty = stats[x][3][5] * 50
    hundred = stats[x][3][6] * 100


    totalMoney = totalMoney + pennies + nickels + dimes + quarters + one + five + ten + twenty + fifty + hundred
print("Coins and Bills: ", totalMoney)

# TO DO 5 : retreive from the user list (and sublists) the names of
# the work(s) that have been authored/created by the person(s) whose
# data files were processed, separated by commas.
#
# WRITE YOUR CODE HERE - fewer than 5 lines

works = []
print("Works: ", end = " ")
for x in range (counter):
    works.append(stats[x][1][1])
print(", ".join(works), end = " ")
    
    
# TO DO 6 : retreive from the user list (and sublists)
# the misc items of the 4 people. Output the list of misc items,
# separated by commas.
#
# WRITE YOUR CODE HERE - fewer than 5 lines


    


