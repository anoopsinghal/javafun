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
import java.util.BitSet;
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
 * This class uses bitsets to track the lines for an artist This is useful when the number of users is small
 * This class has a huge cost in cloning the bitmap before doing an "and" operation to get common lines for the artist. 
 *   
 */

public class ArtistPairBitset {

	private class ArtistPairCount {
		String artist1;
		String artist2;
		Integer count;
		
		ArtistPairCount(String artist1, String artist2, Integer count) {
			this.artist1 = artist1;
			this.artist2 = artist2;
			this.count = count;
		}
	}
	
	// this map stores the lowercase representations of name to original names
	// we convert all name to lower case for comparison purposes to avoid issues with mixed case name
	// of the same artist later in the file
	// this map will hold the lower case map to LAST occurence of artist name in file when the name is printed
	Map<String, String> lcaseToNameMap = new HashMap<String, String>();
	
	// this map hold the number of instances seen so far for a given pair. The key of the container map is
	// alphabatically earlier to the key in value map. This ordering avoids duplicate pairs
	Map<String, BitSet> artistBitsetMap = new HashMap<String, BitSet>();
	
	int numLines = 0;
	
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
	private void parseLine(String line, int lineNum){
		String[] artists = line.split(",");
		
		for (int i = 0; i < artists.length; i++){
			String lcase = artists[i].trim().toLowerCase();
			
			if (lcase.length() > 0){
				this.lcaseToNameMap.put(lcase, artists[i]);
				artists[i] = lcase;
			}
		}
		
		// loop over all the artists
		for (int i = 0; i < artists.length; i++){
			String iArtist = artists[i];
			
			if (iArtist.length() == 0){
				continue;
			}
			
			// find the map of second artists to count
			BitSet bitset = (this.artistBitsetMap.containsKey(iArtist)) ? this.artistBitsetMap.get(iArtist)
																				: new BitSet();
			
			bitset.set(lineNum);
			
			// update the map for this artist
			this.artistBitsetMap.put(iArtist, bitset);
		}
	}
	
	/*
	 * Iterate over the result set to print the results
	 */
	private void printResults(String outFileName, List<ArtistPairCount> apcList) throws IOException{
		// we are simply printing the counts for all the pairs in result set.
		// ofcourse we can get fancier and do sorting by names and counts etc. but avoiding that now
		
		apcList.sort(new Comparator<ArtistPairCount>() {
			@Override
			public int compare(ArtistPairCount o1, ArtistPairCount o2) {
				return o2.count - o1.count;
			}
		});
		
		Path path = FileSystems.getDefault().getPath(outFileName);
		BufferedWriter writer = Files.newBufferedWriter(path);
		
		for (ArtistPairCount apc : apcList){
			writer.write(String.format("%s, %s : %d", apc.artist1, apc.artist2, apc.count));
			writer.newLine();
		}
		
		writer.flush();
		writer.close();
	}
	
	private List<ArtistPairCount> getArtistPairCountList() {
		List<ArtistPairCount> apcList = new ArrayList<ArtistPairCount>();
		
		String[] keys = this.artistBitsetMap.keySet().toArray(new String[this.artistBitsetMap.keySet().size()]);
		
		Arrays.sort(keys);
		long start = System.currentTimeMillis();
		int bitsetTime = 0;
		int innerLoopTime = 0;
		int bitsetGetTime = 0;
		
		for (int i = 0; i < keys.length; i++){
			String iArtist = keys[i];
			BitSet iSet = this.artistBitsetMap.get(keys[i]);
			String iArtistOrigName = this.lcaseToNameMap.get(iArtist);
			long innerLoopStart = System.currentTimeMillis();
			for (int j = i + 1; j < keys.length; j++){
				long beforeBitSet = System.currentTimeMillis();
				String jArtist = keys[j];
				BitSet jSet = this.artistBitsetMap.get(keys[j]);
				
				bitsetGetTime += (System.currentTimeMillis() - beforeBitSet);
				beforeBitSet = System.currentTimeMillis();
				BitSet newSet = new BitSet();
				newSet.or(jSet);
				
				newSet.and(iSet);
				
				int count = newSet.cardinality();
				
				bitsetTime += (System.currentTimeMillis() - beforeBitSet);
				
				if (count > 0) {
					ArtistPairCount apc = new ArtistPairCount(iArtistOrigName,  this.lcaseToNameMap.get(jArtist), count);
					apcList.add(apc);
				}
			}
			
			innerLoopTime += (System.currentTimeMillis() - innerLoopStart);
			
		}
		
		long total = System.currentTimeMillis() - start;
		System.out.println("time in bitset get = " + bitsetGetTime);
		System.out.println("time in bitset comparison = " + bitsetTime);
		System.out.println("time in innerloop = " + innerLoopTime);
		System.out.println("Total time in bitset comparison = " + total);
		return apcList;
	}
	
	public void parseFileAndPrintResults(String inFileName, String outFileName) throws IOException{
		Path path = FileSystems.getDefault().getPath(inFileName);
		if (java.nio.file.Files.exists(path) == false){
			System.out.println("file does not exist");
			return;
		}
		
		long start = System.currentTimeMillis();
		String line = "";
		int lineNum = 0;
		BufferedReader reader = Files.newBufferedReader(path);
		while ((line = reader.readLine()) != null){
			this.parseLine(line, lineNum);
			lineNum++;
		}
		
		this.numLines = lineNum;
		
		reader.close();
		
		List<ArtistPairCount> apcList = getArtistPairCountList();
		
		long total = System.currentTimeMillis() - start;
		System.out.println("Total Time - " + total);
		
		printResults(outFileName, apcList);
	}
	
	/**
	 * @param args
	 * first parameter is the name of the input file
	 * second parameter is number on minimum matches
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length < 2){
			System.out.println("Usage : com.anoop.javafun.ArtistPairBitset <fully qualified input filename> <fully qualified output filename>");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		ArtistPairBitset apm = new ArtistPairBitset();
		apm.parseFileAndPrintResults(fileName, args[1]);

		System.out.println("processed file " + fileName);
		System.out.println("output file " + args[1]);
	}

}
