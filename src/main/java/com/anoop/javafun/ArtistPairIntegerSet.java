/**
 * 
 */
package com.anoop.javafun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anoop.javafun.MovieList.Movie;


/**
 * @author anoop
 * 
 * the input text file contains the favorite musical artists of users from Some Popular Music Review Website. Each line is a list of up to 50 artists, formatted as follows:
 * Radiohead,Pulp,Morrissey,Delay s,Stereophonics,Blur,Suede,Sle eper,The La's,Super Furry Animals,Iggy Pop\n
 * Band of Horses,Smashing Pumpkins,The Velvet Underground,Radiohead,The Decemberists,Morrissey,Televis ion\n
 * etc.
 * Write a program that, using this file as input, produces an output file containing 
 * a count of list of pairs of artists which appear TOGETHER to the output file in decreasing order  
 * 
 * For example, in the above sample, Radiohead and Morrissey appear together twice, but every other pair appears only once. Your solution should be a cvs, with each row being a pair. For example:
 * Morrissey,Radiohead\n
 *
 * This class uses Integer sets to store line numbers for artists. 
 *   
 */

public class ArtistPairIntegerSet extends ArtistPair {

	// this map hold the number of instances seen so far for a given pair. The key of the container map is
	// alphabatically earlier to the key in value map. This ordering avoids duplicate pairs
	Map<String, Set<Integer>> artistIntegerSetMap = new HashMap<String, Set<Integer>>();
	
	/*
	 * this method with parse the file and print the results of the opearion on command line
	 * basic algorithm :
	 * 	open the file
	 *  for every line in file
	 *  	   parse line to have individual artists
	 *     for every artist in list - update lowercase map
	 *     sort the list of artists in lowercase
	 *  close file
	 *  print result set
	 */
	
	/*
	 * this funtion parses the given line and updates all the maps with artist maps, 
	 * result set map as needed and the pairsetMap
	 */
	protected void parseLine(String line){
		int lineNum = this.currentLineNumber;
		
		String[] artists = super.populateArtistNameMap(line);
		
		// loop over all the artists
		for (int i = 0; i < artists.length; i++){
			String iArtist = artists[i];
			
			if (iArtist.length() == 0){
				continue;
			}
			
			// find the map of second artists to count
			Set<Integer> set = (this.artistIntegerSetMap.containsKey(iArtist)) ? this.artistIntegerSetMap.get(iArtist)
																				: new HashSet<Integer>();
			
			set.add(lineNum);
			
			// update the map for this artist
			this.artistIntegerSetMap.put(iArtist, set);
		}
	}
	
    protected void populateArtistPairCountList(){		
		String[] keys = this.artistIntegerSetMap.keySet().toArray(new String[this.artistIntegerSetMap.keySet().size()]);
		
		Arrays.sort(keys);
		long start = System.currentTimeMillis();
		int bitsetTime = 0;
		int innerLoopTime = 0;
		int bitsetGetTime = 0;
		
		for (int i = 0; i < keys.length; i++){
			String iArtist = keys[i];
			String iArtistOrigName = this.lcaseToNameMap.get(iArtist);
			
			Set<Integer> iSet = this.artistIntegerSetMap.get(keys[i]);
			long innerLoopStart = System.currentTimeMillis();
			for (int j = i + 1; j < keys.length; j++){
				long beforeBitSet = System.currentTimeMillis();
				String jArtist = keys[j];
				Set<Integer> jSet = this.artistIntegerSetMap.get(keys[j]);
				
				bitsetGetTime += (System.currentTimeMillis() - beforeBitSet);
				beforeBitSet = System.currentTimeMillis();
				
				Set<Integer> intersection = new HashSet<Integer>(iSet);
				intersection.retainAll(jSet);
				
				int count = intersection.size();
				
				bitsetTime += (System.currentTimeMillis() - beforeBitSet);
				
				if (count > 0) {
					ArtistPairCount apc = new ArtistPairCount(iArtistOrigName,  this.lcaseToNameMap.get(jArtist), count);
					apcList.add(apc);
				}
			}
			
			innerLoopTime += (System.currentTimeMillis() - innerLoopStart);
			
		}
		
		long total = System.currentTimeMillis() - start;
		System.out.println("time in integer set get = " + bitsetGetTime);
		System.out.println("time in integer set intersection = " + bitsetTime);
		System.out.println("time in innerloop = " + innerLoopTime);
		System.out.println("Total time in bitset comparison = " + total);
	}
	
	/**
	 * @param args
	 * first parameter is the name of the input file
	 * second parameter is number on minimum matches
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ArtistPairIntegerSet apis = new ArtistPairIntegerSet();
		apis.process(args);
	}

}
