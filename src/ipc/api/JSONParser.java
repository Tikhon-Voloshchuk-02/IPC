package ipc.api;

import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;

public class JSONParser{
    public static List<Double> getClosePrices(String json, int days){
        List<Double> prices = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(json);
            JSONObject timeSeries = obj.getJSONObject("Time Series (Daily)");

            List<String> dates = new ArrayList<>(timeSeries.keySet());
            Collections.sort(dates, Collections.reverseOrder());

            for(int i=0; i<Math.min(days, dates.size());i++){
                String date = dates.get(i);
                JSONObject dayData = timeSeries.getJSONObject(date);

                double close = dayData.getDouble("4. close");
                prices.add(close);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return prices;
    }

}
