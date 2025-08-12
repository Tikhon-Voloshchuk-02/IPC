package ipc.model;

public class Wertpapier{

    private String name;

    private double nu;
    private double varianz;
    private double w;

    //Konstruktor
    public Wertpapier(String name){
        this.name=name; 
    }
    
//Setter
    public void setNu(double nu){
        this.nu=nu;
    }

    public void setVarianz(double varianz){
        this.varianz=varianz;
    }

    public void setW(double w){
        this.w=w;
    }

//Getter
    public String getName(){
        return this.name;        
    }
    public double getNu(){
        return this.nu;
    }

    public double getVarianz(){
        return this.varianz;
    }

    public double getW(){
        return this.w;
    }
}
