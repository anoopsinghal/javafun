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

import com.anoop.javafun.ArtistPair.ArtistPairCount;
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
public class ArtistPairMap extends ArtistPair{


	// this map hold the number of instances seen so far for a given pair. The key of the container map is
	// alphabatically earlier to the key in value map. This ordering avoids duplicate pairs
	Map<String, Map<String, ArtistPairCount>> pairSetMap = new HashMap<String, Map<String, ArtistPairCount>>();
	
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
		String[] artists = super.populateArtistNameMap(line);
		
		// loop over all the artists
		for (int i = 0; i < artists.length; i++){
			String iArtist = artists[i];
			// find the map of second artists to count
			Map<String, ArtistPairCount> iMap = (this.pairSetMap.containsKey(iArtist)) ? this.pairSetMap.get(iArtist)
																				: new HashMap<String, ArtistPairCount>();
			//loop over rest of the list																	
			for (int j = i + 1; j < artists.length; j++){
				String jArtist = artists[j];
				
				// get count of iArtist jArtist pair. initialize if not there
				ArtistPairCount apc = (iMap.containsKey(jArtist)) ? iMap.get(jArtist) 
																  : new ArtistPairCount(lcaseToNameMap.get(iArtist), lcaseToNameMap.get(jArtist));
				
				// increment count
				apc.count += 1;
				
				if (apc.count == this.minimumCount){
					this.apcList.add(apc);
				}
				
				// update in map
				iMap.put(jArtist, apc);								
			}
			
			// update the map for this artist
			this.pairSetMap.put(iArtist, iMap);
		}
	}

    protected void populateArtistPairCountList() {
    		// nothig to do. Already populated
    }

	/**
	 * @param args
	 * first parameter is the name of the input file
	 * second parameter is number on minimum matches
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ArtistPairMap apm = new ArtistPairMap();
		apm.process(args);
	}

}
