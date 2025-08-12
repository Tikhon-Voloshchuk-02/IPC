package ipc.logic;

import java.util.List;
import java.util.ArrayList;

public class CombinationWP{
    public static List<List<Double>> generateWCombination(int N, double step){
        List<List<Double>> res = new ArrayList<>();
        List<Double> current = new ArrayList<>();
        generateW(N, step, current, 0, res);
        return res;

    }

    public static void generateW(int N, double step, List<Double> current, double sum, List<List<Double>> res){
        if(current.size() == N-1){  //last Weight & check
            double lw=1-sum;
            if(lw>=0 && lw<=1){ //if ya - add to List
                List<Double> all = new ArrayList<>(current);
                all.add(lw);
                res.add(all);
            }
            return;
        }

        for(double w=0;w<=1;w+=step){
            double Sum=sum+w;
            if(Sum<=1){
                current.add(w);
                generateW(N, step, current, Sum, res);
                current.remove(current.size()-1);
            }
        }
        

    }
}
