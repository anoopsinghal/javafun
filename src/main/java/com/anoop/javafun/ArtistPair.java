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
 * This class uses maps to track the pairing count. This is useful when the number of users is large since number of artists is 
 * relatively small
 */
public abstract class ArtistPair {

	protected class ArtistPairCount {
		String artist1;
		String artist2;
		Integer count;
		
		ArtistPairCount(String artist1, String artist2) {
			this.artist1 = artist1;
			this.artist2 = artist2;
			this.count = 0;
		}
		
		ArtistPairCount(String artist1, String artist2, int count) {
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
	
	protected Integer minimumCount;
	
	protected List<ArtistPairCount> apcList = new ArrayList<ArtistPairCount>();
	protected Integer currentLineNumber;
	
	// pure virtual method
    protected abstract void parseLine(String line);
    protected abstract void populateArtistPairCountList();
    			
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
	protected String[] populateArtistNameMap(String line){
		String[] artists = line.split(",");
		
		for (int i = 0; i < artists.length; i++){
			String lcase = artists[i].trim().toLowerCase();
			this.lcaseToNameMap.put(lcase, artists[i]);
			artists[i] = lcase;
		}
		
		// sort the list of artists upfront, a n log n operation
		// we can force alphabetical order in partSetMap instead of sorting as we traverse the array
		// that will be O(n square) comparisons
		Arrays.sort(artists);
		
		return artists;
	}
	
	/*
	 * Iterate over the result set to print the results
	 */
	protected void printResults(String outFileName) throws IOException{
		// we are simply printing the counts for all the pairs in result set.
		// ofcourse we can get fancier and do sorting by names and counts etc. but avoiding that now
		
		this.populateArtistPairCountList();
		apcList.sort(new Comparator<ArtistPairCount>() {
			public int compare(ArtistPairCount o1, ArtistPairCount o2) {
				if (o2.count == o1.count){
					int artistComp = o2.artist1.compareTo(o1.artist1);
					
					if (artistComp == 0){
						artistComp = o2.artist2.compareTo(o1.artist2);						
					}
					
					return artistComp;
				}
				
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
	
	protected void parseFileAndPrintResults(String inFileName, String outFileName) throws IOException{
		Path path = FileSystems.getDefault().getPath(inFileName);
		if (java.nio.file.Files.exists(path) == false){
			System.out.println("file does not exist");
			return;
		}
		
		String line = "";
		BufferedReader reader = Files.newBufferedReader(path);
		
		this.currentLineNumber = 0;
		while ((line = reader.readLine()) != null){
			this.currentLineNumber++;
			this.parseLine(line);
		}
		reader.close();
		
		printResults(outFileName);		
	}
	
	/**
	 * @param args
	 * first parameter is the name of the input file
	 * second parameter is number on minimum matches
	 * @throws IOException 
	 */
	protected void process(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length < 3){
			System.out.println("Usage : com.anoop.javafun.ArtistPairMap <fully qualified input filename> <fully qualified output filename> <min match count>");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		this.minimumCount = Integer.parseInt(args[2]);
		
		this.parseFileAndPrintResults(fileName, args[1]);

		System.out.println("processed file " + fileName);
		System.out.println("output file " + args[1]);
	}

}
