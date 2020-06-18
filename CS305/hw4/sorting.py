from itertools import permutations
import math
import numpy

swap_q = 0
swap_i = 0


def partition(arr, low, high):
    global swap_q
    i = (low - 1)  # index of smaller element
    pivot = arr[high]  # pivot
    for j in range(low, high):
        if arr[j] <= pivot:
            swap_q += 1
            i = i + 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    swap_q += 1
    return i + 1


def quick_sort(arr, low, high):
    if low == high:
        return
    if low < high:
        pi = partition(arr, low, high)
        quick_sort(arr, pi + 1, high)
        quick_sort(arr, low, pi - 1)


def insertion_sort(arr):
    global swap_i
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        while j >= 0 and key < arr[j]:
            swap_i += 1
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key


def main():
    global swap_q, swap_i
    arr = [1, 2, 4, 5, 6, 8, 9]
    perms = math.factorial(len(arr))
    counter_i = 0
    counter_q = 0
    perm = permutations(arr)
    for i in list(perm):
        temp_arr = numpy.asarray(i)
        quick_sort(temp_arr, 0, len(temp_arr) - 1)
        insertion_sort(numpy.array(i))
        if swap_i - swap_q < 0:
            counter_i += 1
        else:
            counter_q += 1
        swap_q = 0
        swap_i = 0

    print(counter_i, counter_q)
    print("IS:", counter_i / perms)
    print("QS:", counter_q / perms)


main()
