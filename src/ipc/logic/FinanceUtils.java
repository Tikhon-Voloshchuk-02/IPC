package ipc.logic;

import java.util.ArrayList;
import java.util.List;

public class FinanceUtils{
    public static List<Double> calculateRendite(List<Double> prices){
        List<Double> list = new ArrayList<>();

        for(int i=0;i<prices.size()-1;i++){
            double heute = prices.get(i);
            double morgen = prices.get(i+1);
            double r = (heute-morgen)/morgen;

            list.add(r); 
        }
        return list;
    }       
    
    public static double calculateAvg(List<Double> values){
        if(values.size()==0){ return 0.0; }
            
        double sum =0.0;
        for(double v: values){
            sum+=v;
        }

        return sum/values.size();
    }

    public static double calculateVarianz(List<Double> values){
        if(values.size() ==0){ return 0.0; }

        double n = calculateAvg(values);
        double sum = 0.0;

        for(double v:values){
            double d= v-n;
            sum+=d*d;
        }
        return sum/values.size();
    }


}
