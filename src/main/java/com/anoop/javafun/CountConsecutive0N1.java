package com.anoop.javafun;

/*
 * given a binary string, count the number of substring which have equal number of zeros and ones
 * E.g. 00110 had 3 such sub string 0011, 01 and 10
 */
public class CountConsecutive0N1 {
	static int counting(String s){
		int result = 0;
				
		char currentChar = s.charAt(0);
		char prevChar = s.charAt(0);
		int prevCount = 0;
		int currentCount = 1;
		
		for (int i = 1; i < s.length(); i++){
			currentChar = s.charAt(i);
			
			if (currentChar != '0' && currentChar != '1'){
				continue;
			}
			
			if (currentChar == prevChar){
				// same characters
				currentCount++;				
			} else {
				prevCount = currentCount;
				currentCount = 1;
			}
			
			if (currentCount <= prevCount){
				// we have as many in current state as prev state
				result++;
			}
			
			prevChar = currentChar;
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1){
			System.out.println("Usage : com.anoop.javafun.CountConsecutive0N1 <binary String>");
			System.exit(0);
		}
		
		System.out.println(counting(args[0]));
	}

}
