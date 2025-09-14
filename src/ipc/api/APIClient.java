package ipc.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIClient{
    public static String getTimeSeriesDaily(String symbol){

        String apikey = "JOHT65HNZE1PYY0V";
        String urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" + "&symbol=" + symbol
            + "&outputsize=compact" + "&apikey=" + apikey;

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","application/json");

            BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
            StringBuilder res = new StringBuilder();
            String inputLine;

            while ( (inputLine = in.readLine()) != null ){ res.append(inputLine).append("\n"); }

            in.close();
            conn.disconnect();

            return res.toString();

        } catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }
}
