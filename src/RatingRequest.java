import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.*;

public class RatingRequest {

	static HashMap<String, String> map = new HashMap<String,String>();
	static HashMap<String, Double> rank = new HashMap<String, Double>();
	static HashMap<String, String> failMatch = new HashMap<String, String>();
	static int count = 0;
	
	private static String generateURL(String movieName, String year){
		String url=null; //= "http://www.omdbapi.com/?t=The+Graduate&y=&plot=short&r=json"
		int l =0;
		int r =movieName.length()-1;
		while(movieName.charAt(l)==' ' || movieName.charAt(r)==' '){

			if(movieName.charAt(l)==' ') l++;
			else if(movieName.charAt(r)==' ') r--;
		}
		movieName= movieName.substring(l, r+1);
		
		movieName = movieName.replaceAll("\\s+", "+");
		movieName = movieName.replace(":", "%3A");
		movieName = movieName.replace(",", "%2C");
		
		if(year != null){
			url = "http://www.omdbapi.com/?t="+ movieName + "&y="+year+"&plot=short&r=json";
		}
		else{
			url = "http://www.omdbapi.com/?t="+ movieName + "&y=&plot=short&r=json";
		}
		//System.out.println(year);
		return url;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	private static JSONObject jsonReader(String movieName, String year) throws MalformedURLException, IOException, JSONException{
		String url = generateURL(movieName, year);
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));	      
			String jsonText = readAll(rd);
			System.out.println(jsonText);
			JSONObject json = new JSONObject(jsonText);
			//System.out.println(json);
			//System.out.println("get );
			return json;
		} finally {
			is.close();			
		}
	}
	
	private static void fixFailMatch(){
		
	}
	
	 
	public static String getRating(String movieName, String year) throws JSONException, MalformedURLException, IOException{
		JSONObject obj = jsonReader(movieName, year);
		String rating = null;

		if(!obj.isNull("imdbRating")&& obj.getString("Type").equals("movie") ){
			//if(obj.getString("Type").equals("movie"))
			rating = obj.getString("imdbRating");
			double movieRank = Double.parseDouble(rating);		
			rank.put(movieName, movieRank);
		}
		else{
			failMatch.put(movieName,year);
		}
		//System.out.println("getrating");

		//TODO: fix failMatch problem
		
		
		
		
		return rating;
	}
	public static void readCSV(String fileName){

		String csvFile = fileName;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String url = null;
		int count = 1;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String movieName = null;
				String releaseYear = null;
				// use comma as separator
				if(line.charAt(0)=='\"'){
					StringBuffer name = new StringBuffer();
					//movieName= ""+line.charAt(1);
					int i=1;
					while(line.charAt(i)!='\"'){
						name.append(line.charAt(i));
						i++;
					}
					movieName = name.toString();
				}
				String[] movie = line.split(cvsSplitBy);
				
				movieName = (movieName == null)?movie[0]:movieName;
				
				if(movie.length==2) releaseYear = movie[1];
				else if(movie.length==3) releaseYear = movie[2];


//				System.out.println("Movie [name= " + movieName 
//						+ " , year=" + releaseYear + "]"+count);
				map.put(movieName, releaseYear);

				count++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}
	
	
	public static void movieRank() throws MalformedURLException, JSONException, IOException{
		String rank = null;
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,String> pair =(Map.Entry<String,String>) it.next();
			//String url = generateURL(pair.getKey(),pair.getValue());
			
			rank = getRating(pair.getKey(),pair.getValue());
			//System.out.println("=="+rank+"   "+count);
			//count++;
//			if(count == 26){
//				System.out.println("here");
//			}
		}	
	}
	public static void tableSort(){
		Set<Entry<String, Double>> set = rank.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        for(Map.Entry<String, Double> entry:list){
            System.out.println(entry.getKey()+" -- "+entry.getValue());
        }
	}

	public static void run(String file) throws MalformedURLException, JSONException, IOException{
		readCSV(file);
		movieRank();
		
//		Iterator it = failMatch.iterator();
//		while(it.hasNext()){
//			System.out.println(it.next().toString());
//		}
	}

	public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
		// TODO Auto-generated method stub
		String file = "moviestest.csv";
		run(file);
		//String res = getRating(jsonReader("http://www.omdbapi.com/?t=The+Graduate&y=&plot=short&r=json"));
		//System.out.println(res);
		tableSort();
	}

}
