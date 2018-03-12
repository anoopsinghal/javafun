package com.anoop.javafun;

import java.util.Arrays;

/*
 * Roll a lowercase alphabetic string according to a number of specified operations and print result
 *
 * Each operation is defined in a string as "<start index> <end index> R|L" where R => right and L => left
 * 
 * Rolling R means add one : 'a' rolls right to 'b' and so on. 'z' rolls right to 'a'
 * Rolling L means subtract one : 'b' rolls left to 'a' and so on. 'a' rolls left to 'z'
 */
public class RollingString {

	static char[] rollString(char[] strArray, String operation){
		String[] opVals = operation.split(" ");
		int startIndex = Integer.parseInt(opVals[0]);
		int endIndex = Integer.parseInt(opVals[1]);
		
		int direction = opVals[2].compareTo("R") == 0 ? 1 : -1;
		
		int index = startIndex;
		
		int lowerBound = (int)'a';
		int upperBound = (int)'z';
		while (index <= endIndex && index < strArray.length){
			char chr = strArray[index];
			int ascii = (int)chr;
			
			if (ascii >= lowerBound && ascii <= upperBound) {
				
				ascii += direction;
				if (ascii < lowerBound){
					ascii = upperBound;
				}
				
				if (ascii > upperBound) {
					ascii = lowerBound;
				}
				
				strArray[index] = (char)ascii;
			}
			
			index++;
		}
		
		return strArray;
	}
	
	  static String rollingString(String s, String[] operations) {
		  	char[] strArray  = s.toCharArray();
		  	
	        for (String op : operations){
	        	
	        		System.out.print("rolled " + s);
	            strArray = rollString(strArray, op);
	            
	            // creating a new string is not a high cost and worth the intermediate info
	            s = new String(strArray);
	            System.out.println(" with of " + op + " to " + s);
	        }
	        
	        return s;
	    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 2){
			System.out.println("Usage : com.anoop.javafun.RollingString <str to role> <op1> <op2> ... ");
			System.out.println("         op - <start index> <end index> R or L");
			System.out.println("         characters between 'a' and 'z' will be rolled. rest will be ignored");
			System.exit(0);
		}
		
		String result = rollingString(args[0], Arrays.copyOfRange(args, 1, args.length));
		
		System.out.println("final result - " + result);
	}

}
