package ipc.model;

import java.lang.Math.*;
//Dieses Class rechnen die Datei NUR fur 2 Wertpapiere

public class Portfolio{

    private double Cov;
    public final double gamma=0.7;
    public final double Rf=0.02;

    private double nu_p;
    private double varianz_p;
    private double E;
    private double SP;

    public Portfolio(){

    }

    public Portfolio(double nu_p, double varianz_p, double cov){
        this.nu_p=nu_p;
        this.varianz_p=varianz_p; 
        this.Cov=cov;
    }
    

    public void setCov(double Cov){
        this.Cov=Cov;
    }
    public double getCov(){
        return this.Cov;
    }

    public double stdAbweichung(double varianz){
        return Math.sqrt(varianz);
    }

    public double correlation(double varianzx, double varianzy){
        double stdx = stdAbweichung(varianzx);
        double stdy = stdAbweichung(varianzy);

        double r = Cov/(stdx*stdy);
        return r;
    }

    public double nuP(double nu1, double w1, double nu2, double w2){
        this.nu_p=(nu1*w1) + (nu2*w2);
        return this.nu_p;
    }

    public double varianzP(double w1, double varianzx, double w2, double varianzy){
        double k1=Math.pow(w1,2)*varianzx;
        double k2=Math.pow(w2,2)*varianzy;
        double k3=2*w1*w2*Cov;

        this.varianz_p=k1+k2+k3;
        return this.varianz_p;
    }

    public double nutzenFkt(){
        this.E=this.nu_p-gamma*this.varianz_p; 
        return this.E;
    }

    public double sharpeRatio(){
        double sigma = stdAbweichung(varianz_p);
        this.SP=(nu_p-Rf)/sigma;
        return this.SP;  
    }

    public void print_Portfolio(){
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          Minimum-Varianz-Portfolio             ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.printf ("║ %-33s : %10.4f ║%n", "Erwartete Rendite (μ)", nu_p);
        System.out.printf ("║ %-33s : %10.4f ║%n", "Varianz (σ²)", varianz_p);
        System.out.printf ("║ %-33s : %10.4f ║%n", "Standardabweichung (σ)", stdAbweichung(varianz_p));
        System.out.printf ("║ %-33s : %10.4f ║%n", "Kovarianz", Cov);
        System.out.printf ("║ %-33s : %10.4f ║%n", "Nutzenfunktion E(u)", nutzenFkt());
        System.out.printf ("║ %-33s : %10.4f ║%n", "Sharpe Ratio (SP)", sharpeRatio());
        System.out.println("╚════════════════════════════════════════════════╝");
    }
//MVP 2 WP
    public static void findeMVP(Wertpapier wp1, Wertpapier wp2, double cov){
        double var1 =wp1.getVarianz();
        double var2 =wp2.getVarianz();

        double w1=(var2-cov)/(var1+var2-2*cov);
        double w2=1-w1;

        double Nu=w1*wp1.getNu()+w2*wp2.getNu();
        double var_p= (w1*w1*var1) + (w2*w2*var2)+ (2*w1*w2*cov);

        Portfolio p1 = new Portfolio(Nu, var_p, cov);

        System.out.println("<Minimum-Varianz-Portfolio>");
        p1.print_Portfolio();                                   //ausgabe

        System.out.printf("Gewicht WP1: %.4f%n", w1);
        System.out.printf("Gewicht WP2: %.4f%n", w2);

    } 

}
