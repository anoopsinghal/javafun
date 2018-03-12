package com.anoop.javafun;

/*
 * Please write a program that prints the numbers from 1 to 100. But for multiples of three print “Fizz” 
 * instead of the number and for the multiples of five print “Buzz”. 
 * For numbers which are multiples of both three and five print “FizzBuzz”.
 */
public class FizBuzz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		for (int i = 1; i <= 100; i++){
			String toPrint = "" + i;
			if (i % 15 == 0){
				toPrint = "FizzBuzz";
			} else if (i % 5 == 0) {
				toPrint = "Buzz";				
			} else if (i % 3 == 0) {
				toPrint = "Fizz";				
			}
			System.out.println(toPrint);
		}
	}

}
