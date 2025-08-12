package ipc.model;

import java.lang.Math.*;
import java.util.List;
import java.util.ArrayList;
import ipc.logic.CombinationWP;
import ipc.logic.CovarianzMatrix;

public class PortfolioN{

    private List<Wertpapier> WP;
    private double[] w;
    private CovarianzMatrix matrix;

    private final double gamma=0.7;
    private final double Rf=0.02;

    public PortfolioN(List<Wertpapier> WP, double[]  w, CovarianzMatrix matrix){
        this.WP=WP;
        this.w=w;
        this.matrix=matrix;
    }

//Erwartungswert von P
    public double calculateNu(){
        double res = 0.0;

        for (int i=0; i<WP.size(); i++){
            res += w[i] * WP.get(i).getNu();
        }
        return res;
    }

//Varianz von P
    public double calculateVarianz(){
        double res =0.0;
        int n=w.length;

        for (int i=0; i<n; i++){

            for (int j=0; j<n; j++){

                res+=w[i]*w[j]*matrix.getMatrix(i,j);
            }
        }
        return res;
    }

//Standardabweichung
    public double calculateStdab(){
        return Math.sqrt(calculateVarianz());
    }

//Sharpe Ratio
    public double calculateSP(double Rf){

        double sigma = calculateStdab();
        if (sigma==0) { return 0; }

        double SP = (calculateNu()-Rf)/sigma;
        return SP;
    }

//Nutzenfkt
    public double calculateE(){
        double E = calculateNu() - gamma * calculateVarianz();
        return E;
    } 

//generate Combinationen
    public static List<PortfolioN> generatePortfolios(
                                                    List<Wertpapier> wertpapierList, 
                                                    CovarianzMatrix Matrix, 
                                                    double step ){

        List<PortfolioN> portfolios = new ArrayList<>();
        int N=wertpapierList.size();

        List<List<Double>> allW = CombinationWP.generateWCombination(N, step);

        for (List<Double> w: allW){
                double[] gewichte = new double[w.size()];

                for(int i=0; i<w.size(); i++){
                    gewichte[i] = w.get(i);
                }

                PortfolioN p = new PortfolioN( wertpapierList, gewichte, Matrix );
                portfolios.add(p);
        }
        
        return portfolios;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("╔════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║ %-36s :  %-8.4f   ║\n", "Erwartete Rendite (μ)", calculateNu()));
        sb.append(String.format("║ %-36s :  %-8.4f   ║\n", "Varianz (σ²)", calculateVarianz()));
        sb.append(String.format("║ %-36s :  %-8.4f   ║\n", "Standardabweichung (σ)", calculateStdab()));
        sb.append(String.format("║ %-36s :  %-8.4f   ║\n", "Nutzenfunktion E(u)", calculateE()));
        sb.append(String.format("║ %-36s :  %-8.4f   ║\n", "Sharpe Ratio (SP) ", calculateSP(Rf)));
        sb.append("╠════════════════════════════════════════════════════╣\n");

        StringBuilder weightsStr = new StringBuilder();

        for (int i = 0; i < w.length; i++) {
            String line = String.format("%s = %.4f", WP.get(i).getName(), w[i]);
            sb.append(String.format("║ %-50s ║\n", line));
            
        }

        sb.append("╚════════════════════════════════════════════════════╝\n");
        

    return sb.toString();
    }

}
