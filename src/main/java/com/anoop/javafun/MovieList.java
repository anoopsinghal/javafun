package com.anoop.javafun;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.*;

/*
 * given a sub string of the movie title, this program list all the movies with the given substring
 * in the title in decreasing order to year and title
 */
public class MovieList {

	class Movie {
		String Poster;
        String Title;
        String Type;
        Integer Year;
        String imdbID;		
        
	    @Override
	    public String toString() {
	        return Year + " - " + Title;
	    }		
        
	}
	
	class MovieResponse {
		Integer page;
		Integer per_page;
		Integer total;
		Integer total_pages;
		List<Movie> data;
		
	    @Override
	    public String toString() {
	        return total + " - " + total_pages;
	    }		
	}
	
	static List<Movie> getMovies(String subStr) throws IOException{
		int pageNum = 1;
		int totalPages = Integer.MAX_VALUE;

		List<Movie> movies = new ArrayList<Movie>();
		
		Gson gson = new Gson();
		while (pageNum <= totalPages){
			String urlStr = String.format("https://jsonmock.hackerrank.com/api/movies/search/?Title=%s&page=%d", subStr, pageNum);

			URL url = new URL(urlStr);

			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("GET");

			Reader reader = new InputStreamReader(con.getInputStream());
            MovieResponse response = gson.fromJson(reader, MovieResponse.class);
            
            totalPages = response.total_pages;
            
            movies.addAll(response.data);
            
            pageNum++;
		}
		
		return movies;
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1){
			System.out.println("Usage : com.anoop.javafun.MovieList <title filter>");
			System.exit(0);			
		}

		List<Movie> movies = getMovies(args[0]);
		movies.sort(new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				if (o1.Year.intValue() == o2.Year.intValue()){
					return o2.Title.compareTo(o1.Title);
				}
				return o2.Year - o1.Year;
			}
		});
		
		for (Movie movie : movies){
			System.out.println(movie);
		}
	}

}
