import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;


public class Test {
	public static String generateURL(String movieName, String year){
		String url=null; //= "http://www.omdbapi.com/?t=The+Graduate&y=&plot=short&r=json"
		int l =0;
		int r =movieName.length()-1;
		while(movieName.charAt(l)==' ' || movieName.charAt(r)==' '){

			if(movieName.charAt(l)==' ') 
				l++;
			else if(movieName.charAt(r)==' ') 
				r--;
		}
		movieName= movieName.substring(l, r+1);
		
		movieName = movieName.replaceAll("\\s+", "+");
		movieName = movieName.replace(":", "%3A");
		movieName = movieName.replace(",", "%2C");
		
		
		if(year != null)
			url = "http://www.omdbapi.com/?t="+ movieName + "&y="+year+"&plot=short&r=json";
		else url = "http://www.omdbapi.com/?t="+ movieName + "&y=&plot=short&r=json";
		return url;
	}

	public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
		//String movie = "E.T.: The Extra-Terrestrial  ";
		String movie ="Captain Carey, U.S.A.";
		String movieName = null ;
		
		if(movie.charAt(0)=='\"'){
			StringBuffer bu = new StringBuffer();
			//movieName = "" + movie.charAt(1);
			int i=1;
			while(movie.charAt(i)!='\"'){
				bu.append(movie.charAt(i));
				i++;
			}
			movieName = bu.toString();
		}
		else{
			movieName = movie;
		}
//		movieName = movieName.replaceAll("\\s+", "+");
//		movieName = movieName.replace(":", "%3A");
//		movieName = movieName.replace(",", "%2C");
		//System.out.println(movieName);
		
		System.out.println(generateURL(movieName,"1950"));
		
		
		String rate = "9.1";
		double a = Double.parseDouble(rate);
		System.out.println(a);
		
		
	}
	
	
	

}
