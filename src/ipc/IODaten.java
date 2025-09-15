package ipc;

import ipc.model.*;
import ipc.logic.*;
import ipc.api.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class IODaten {

// Console for User
    public static void start(){
        Scanner IOscan = new Scanner(System.in); 
        boolean run=true;
        while(run){
            printSplash();

            System.out.println("[ 1 ] - Manuall Combination 🔢");
            System.out.println("[ 2 ] - MVP berechnen 📈");
            System.out.println("[ 3 ] - Borse 🌐");
            System.out.println("[ 4 ] - Help 📜");
            System.out.println("[ 5 ] - Exit 🔙");
            System.out.print("➤ Ihre Auswahl:");

            int auswahl;
            try{
                auswahl = Integer.parseInt(IOscan.nextLine());
            } catch (NumberFormatException e){
                System.out.println("\n⚠ Ungültige Auswahl – bitte 1, 2 oder 3 wählen");
                continue;
            }

            if (auswahl == 1){
                System.out.println("\n🟢 Starte manualle WP...\n");   
                manuellVersion(IOscan);
                System.out.println();
            }
            else if (auswahl == 2){
                System.out.println("\n🟢 Starte MVP-Modus...\n");   
                handleMVP(IOscan);
                System.out.println();
            }
            else if (auswahl == 3){
                System.out.println("\n🟢 Starte Borse-Modus...\n");   
                handleBorse(IOscan);
                System.out.println();
            }
            else if (auswahl == 4){
                System.out.println("\n📜 Starte Help-Mod...\n");   
                printHelp();
                System.out.println();
                System.out.println("tapp 1 -> continue");
                int tapp = IOscan.nextInt();
                IOscan.nextLine();

                if (tapp==1){ continue; }
            } 
 
            else if (auswahl == 5){
                System.out.println("Programm beendet"); 
                break;
            }
        }
        IOscan.close();
        
    }

//MVP Bearbeitung
    public static void handleMVP(Scanner IOscan){
        System.out.println("==== MVP Version ====");

        ArrayList<Wertpapier> wpList = new ArrayList<>();
        Portfolio port = new Portfolio();

        System.out.print("Anzahl der WP: ");
        int n = IOscan.nextInt();
        IOscan.nextLine();

        if( n!=2){
            System.out.println("Error - only 2 WP ");
            return;
        }
        for (int i = 0; i<n; i++ ){
            System.out.print("Name: ");
            String name = IOscan.nextLine();

            Wertpapier wpr = new Wertpapier(name); //LIST

            System.out.print("Erwartungswert: ");
            double nu = IOscan.nextDouble();
            wpr.setNu(nu);

            System.out.print("Varianz: ");
            double varianz = IOscan.nextDouble();
            wpr.setVarianz(varianz);

            IOscan.nextLine();
            wpList.add(wpr);

        } 

        System.out.print("Covarianz: ");
        CovarianzMatrix matrix = IOCovarianz(n, IOscan, wpList);
        double cov= matrix.getMatrix(0,1);            

        port.findeMVP(wpList.get(0), wpList.get(1), cov);  // in Portfolio.java
         
    }

//Covarianz einschreiben
    public static CovarianzMatrix IOCovarianz(int n, Scanner IOscan, List<Wertpapier> wertpapierList) {
        CovarianzMatrix cov = new CovarianzMatrix(n);
        System.out.println("---Covarianz schreiben---");
        for (int i=0; i<n; i++){
            for (int j=i; j<n; j++){
                double value = wertpapierList.get(i).getVarianz();

                if(i==j){
                    System.out.println("Varianz einschreiben "+ (i+1)+" = "+value+ " (autocomplete) ");
                }
                else{
                    System.out.print("Cov(WP"+ (i+1)+ ", WP "+ (j+1)+ "):) ");
                    value=IOscan.nextDouble();
                    IOscan.nextLine();
                }
                cov.setMatrix(i, j, value);
                cov.setMatrix(j, i, value);
            }
        }
        return cov;
    }

//N-wertpapiere Bearbeitung

    public static void manuellVersion(Scanner IOscan){

        System.out.println("----IPC-Start----");     
        final double Rf = 0.02;

        ArrayList <Wertpapier> wertpapierList = new ArrayList<>();
    
//Daten eingabe        
   
        System.out.println();
        System.out.print("Die Anzahl der WP: ");
        int n=IOscan.nextInt();
        IOscan.nextLine();
        
        int count=0;
        double gewichtSum=0.0;
        double epsilon = 1e-9;

        while(count<n) {

            System.out.print("Name: "); 
            String name = IOscan.nextLine(); 

            Wertpapier wp = new Wertpapier(name);
            count++;

//Erwartungswert
            System.out.print("Erwartungswert: "); 
            double nu=0.0;
            while (true) {
                if ( IOscan.hasNextDouble() ){
                    nu = IOscan.nextDouble(); 
                    break;
                }
                else {
                    System.out.println("Error");
                    IOscan.next();
                }
            }
            wp.setNu(nu);
            IOscan.nextLine();
//Varianz 
            System.out.print("Varianz (>=0): "); 
            double varianz=0.0;
            while(true){
                if ( IOscan.hasNextDouble() ){

                    varianz=IOscan.nextDouble();
                    if (varianz >= 0) { break; }
                    else { System.out.println("Error: var - negativ"); }
                } 
                else {
                    System.out.println("Error");
                    IOscan.next();
                }
            }
            wp.setVarianz(varianz);
            IOscan.nextLine();

            wertpapierList.add(wp);
           
        } 

//Covarianz - benutzen Method <IOCovarianz>
        CovarianzMatrix matrix = IOCovarianz(n, IOscan, wertpapierList );
        System.out.println("Wertpapiere - insgesamt: "+n);
        System.out.println();
        
//Tabelle ausgabe
        System.out.printf("%-10s %-10s %-10s %-10s %-10s\n", "Name", "μ", "Var", "sigma", "w");

        for (Wertpapier i : wertpapierList) {
            double varianz = i.getVarianz();
            System.out.printf( "%-10s %-10.4f %-10.4f %-10.4f\n",
                i.getName(),
                i.getNu(),
                varianz,
                i.getW() );
        }

//Berechnet eine Kombinationen vom WP mit STEP
        
        List<PortfolioN> combination = PortfolioN.generatePortfolios(wertpapierList, matrix, 0.1);

        PortfolioN best = null;
        double bestSP = -1000.0;
        int N = combination.size();

        for (PortfolioN P: combination){
            System.out.println(P);

            double SP = P.calculateSP(Rf);

            if (Double.isFinite(SP) && SP > bestSP){

                bestSP = SP;
                best = P;
            }
        }

        System.out.println();
        System.out.println("Anzahl der Combinationen: "+N);
        System.out.println("Risikolose Zinssatz: Rf="+Rf*100+"%");
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║         * Bestes Portfolio nach Sharpe (SP)        ║");
        System.out.println("╠════════════════════════════════════════════════════╣");

        if (best != null) { System.out.println(best); }
}

    public static Wertpapier createWertpapierFromAPI(String ticker, String Borseid ,int tage){

        String symbol = ticker+Borseid;

        String json = APIClient.getTimeSeriesDaily(symbol);
        List<Double> prices = JSONParser.getClosePrices(json, tage);

        if (prices.size() < 2){
            System.out.println("Ungenuge Daten");
            return null;
        }

        List<Double> renditen = FinanceUtils.calculateRendite(prices);

        double nu = FinanceUtils.calculateAvg(renditen);

        double varianz = FinanceUtils.calculateVarianz(renditen);

        Wertpapier wp = new Wertpapier(symbol);
        wp.setNu(nu);
        wp.setVarianz(varianz);

        return wp;        
    }

    public static void handleBorse(Scanner IOscan){
        System.out.println("Wie viele WP mochtest du eingeben?");
        int n;
        try{
            n=Integer.parseInt(IOscan.nextLine()); 
        } catch (NumberFormatException e){
            System.out.println("Error: ⚠ Ungültige Zahl");
            return;
        }

        List<Wertpapier> wpList = new ArrayList<>();

        for (int i=0; i<n; i++){
            System.out.printf("WP %d:\n", i+1);

            System.out.print("  ➤ Ticker: ");
            String ticker = IOscan.nextLine().trim();

            System.out.println("➤ Börsen-Suffix (z.B. .DE, .MX, leer lassen für US):");
            String borse = IOscan.nextLine().trim();

            Wertpapier wp = createWertpapierFromAPI(ticker, borse, 10);

            if (wp != null){
                wpList.add(wp);
                System.out.printf("✅ %s hinzugefügt.\n", wp.getName());
            }
            else{
                System.out.println("⚠  Fehler beim Abrufen"); 
                i--;
            }
        }

        System.out.println("\nAlle Wertpapiere:");
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║             📊 Alle Wertpapiere            ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf ("║ %-10s │ μ: %-8.4f│ σ²: %-8.4f     ║\n",
                           "Name", 0.0, 0.0); 
        System.out.println("╟────────────┼────────────┼──────────────────╢");

        for (Wertpapier wp : wpList) {
        System.out.printf("║ %-10s │ %-10.4f │ %-11.4f      ║\n",
                      wp.getName(), wp.getNu(), wp.getVarianz());
        }
        System.out.println("╚════════════════════════════════════════════╝");
        
// Diese Daten bearbeiten

        final double Rf = 0.02;
 
        CovarianzMatrix matrix = IOCovarianz(n, IOscan, wpList );
        System.out.println("Wertpapiere - insgesamt: "+n);
        System.out.println();

       
        List<PortfolioN> combination = PortfolioN.generatePortfolios(wpList, matrix, 0.1);

        PortfolioN best = null;
        double bestSP = -1000.0;
        int N = combination.size();

        for (PortfolioN P: combination){
            System.out.println(P);

            double SP = P.calculateSP(Rf);

            if (Double.isFinite(SP) && SP > bestSP){

                bestSP = SP;
                best = P;
            }
        }

        System.out.println("\nAlle Wertpapiere:");
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║             📊 Alle Wertpapiere            ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf ("║ %-10s │ μ: %-8.4f│ σ²: %-8.4f     ║\n",
                           "Name", 0.0, 0.0); 
        System.out.println("╟────────────┼────────────┼──────────────────╢");

        for (Wertpapier wp : wpList) {
        System.out.printf("║ %-10s │ %-10.4f │ %-11.4f      ║\n",
                      wp.getName(), wp.getNu(), wp.getVarianz());
        }
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println();
        System.out.println("Anzahl der Combinationen: "+N);
        System.out.println("Risikolose Zinssatz: Rf="+Rf*100+"%");
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║         * Bestes Portfolio nach Sharpe (SP)        ║");
        System.out.println("╠════════════════════════════════════════════════════╣");

        if (best != null) { System.out.println(best); }

    }

    public static void printHelp() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              📜 Anleitung – Investment Portfolio Calculator        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ [ 1 ] – Manuall Combination                                        ║");
        System.out.println("║   • Manuelle Eingabe der Daten: Ticker, μ, σ², Gewicht, Kovarianz  ║");
        System.out.println("║   • Berechnung: Rendite, Risiko, σ, Nutzen, Sharpe Ratio           ║");
        System.out.println("║   • Ausgabe: Alle möglichen Kombinationen mit Schrittweite 0.1     ║");
        System.out.println("║   + Bestes Portfolio nach Sharpe Ratio                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣"); 
        System.out.println("║ [ 2 ] – MVP berechnen                                              ║"); 
        System.out.println("║   • Automatische Suche nach dem Portfolio mit minimalem Risiko     ║"); 
        System.out.println("║   • Es werden nur die Asset-Daten eingegeben, Gewichte berechnet   ║");
        System.out.println("║     das Programm automatisch                                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ [ 3 ] – Börse                                                      ║");
        System.out.println("║   • Abruf von Asset-Daten über API                                 ║");
        System.out.println("║   • Ausgabe von erwarteter Rendite und Varianz für jedes Asset     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ [ 5 ] – Exit                                                       ║");
        System.out.println("║   • Programm beenden                                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Ausgegebene Kennzahlen:                                            ║");
        System.out.println("║   μ – erwartete Rendite   σ² – Varianz   σ – Standardabweichung    ║");
        System.out.println("║   E(u) – Nutzenfunktion   SP – Sharpe Ratio                        ║");
        System.out.println("║   Asset-Gewichte – Anteil jedes Assets am Portfolio                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }

    public static void printSplash() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.println("║   ██╗             ██████╗                 ╔██████╗                 ║");
        System.out.println("║   ██║             ██╔══██╗                ██╔═══██╗                ║");
        System.out.println("║   ██║             ██████╔╝                ██║                      ║");
        System.out.println("║   ██║             ██╔═══╝                 ██║                      ║");
        System.out.println("║   ██║             ██║                     ╚██████╔╝                ║");
        System.out.println("║   ╚═╝             ╚═╝                      ╚═════╝                 ║");
        System.out.println("║                                                                    ║");
        System.out.println("║   📊 INVESTMENT PORTFOLIO CALCULATOR (IPC)     Version 1.1         ║");
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
