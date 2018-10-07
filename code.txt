package com.javacourse;

import java.util.ArrayList;
import java.util.Scanner;

public class OddAndEvenNumbers {
    private Number[] numbers;

    public OddAndEvenNumbers(){
    }

    public void showOddAndEvenNumbersInConsole(){
        Interval interval = getIntervalFromUser();
        fillNumberArrayAndCheckIfEven(interval);
        showEvenFromStart();
        showOddFromEnd();
        showSumOfOddAndEvenNumbers();
    }

    private Interval getIntervalFromUser(){
        Scanner scanner = new Scanner(System.in);
        int a, b;//boundaries of the interval

        System.out.print("Enter start of the interval:");
        a = scanner.nextInt();
        System.out.print("Enter end of the interval:");
        b = scanner.nextInt();

        return new Interval(a,b);
    }

    private void fillNumberArrayAndCheckIfEven(Interval interval){
        numbers = new Number[interval.getLength()];
        for(int i=interval.getStart(), j=0; j<numbers.length; ++i, ++j){
            numbers[j] = new Number(i);
        }
    }

    private void showEvenFromStart(){
        System.out.println("Even numbers:");
        for(Number n: numbers){
            if(n.isEven())
                System.out.print(n.getValue()+" ");
        }
        if(numbers.length>0) System.out.println();
    }

    private void showOddFromEnd(){
        System.out.println("Odd numbers:");
        for(Number n: numbers){
            if(!n.isEven())
                System.out.print(n.getValue()+" ");
        }
        if(numbers.length>0) System.out.println();
    }

    private void showSumOfOddAndEvenNumbers(){
        int evenSum = 0, oddSum = 0;
        for(Number n: numbers){
            if(n.isEven())
                evenSum += n.getValue();
            else oddSum += n.getValue();
        }
        if(numbers.length>0){
            System.out.println("Even sum:"+evenSum);
            System.out.println("Odd sum:"+oddSum);
        }
    }
}
