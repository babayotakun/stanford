package org.babayota.stanford.algorithms.work1

import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

/**
 * Merge sort and count of inversions in the given array.
 */
fun main(args: Array<String>) {
    val inputArray = loadData()
    val result = AtomicLong(0)
    println(recursiveStep(inputArray, result))
    println(result)
    println(bruteForce(inputArray))
}

private fun recursiveStep(array: List<Int>, counter: AtomicLong): List<Int> {
    if (array.size == 1) {
        return array
    }
    if (array.size == 2) {
        return mergeStep(array.slice(IntRange(0, 0)), array.slice(IntRange(1, 1)), counter)
    }
    val leftSorted = recursiveStep(array.slice(IntRange(0, array.size / 2 - 1)), counter)
    val rightSorted = recursiveStep(array.slice(IntRange(array.size / 2, array.size - 1)), counter)
    return mergeStep(leftSorted, rightSorted, counter)
}

private fun mergeStep(leftSorted: List<Int>, rightSorted: List<Int>, inversionsCounter: AtomicLong): List<Int> {
    val result = ArrayList<Int>()
    var leftCounter = 0
    var rightCounter = 0
    while (leftCounter < leftSorted.size || rightCounter < rightSorted.size) {
        if (leftCounter < leftSorted.size && (rightCounter == rightSorted.size || leftSorted[leftCounter] < rightSorted[rightCounter])) {
            result.add(leftSorted[leftCounter])
            leftCounter += 1
        } else if (rightCounter < rightSorted.size) {
            result.add(rightSorted[rightCounter])
            rightCounter += 1
            inversionsCounter.addAndGet(leftSorted.size.toLong() - leftCounter)
        }
    }
    return result
}

private fun loadData(): List<Int> {
    val fileContent = String::class.java.getResource("/IntegerArray.txt").readText()
    return fileContent.split("\r\n").stream()
            .map({ str -> Integer.parseInt(str) })
            .collect(Collectors.toList())
}

private fun bruteForce(array: List<Int>): Long {
    var inversionsCounter = 0L
    for (firstCounter in 0 until array.size) {
        for (secondCounter in firstCounter until array.size) {
            if (array[firstCounter] > array[secondCounter]) {
                inversionsCounter = inversionsCounter.inc()
            }
        }
    }
    return inversionsCounter
}
