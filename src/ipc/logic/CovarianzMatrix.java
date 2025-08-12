package ipc.logic;

public class CovarianzMatrix{
    private double[][] matrix;
    private int size;

    public CovarianzMatrix(int n){
        this.size=n;
        this.matrix=new double[n][n];
    }

    //setter
    public void setMatrix(int i, int j, double value){
        matrix[i][j]=value;
        matrix[j][i]=value;
    }

    //getter
    public double getMatrix(int i, int j){
        return matrix[i][j];
    }
    public int getSize(){
        return size;
    }

    public void printMatrix(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                System.out.printf("%8.4f", matrix[i][j]);
            }
            System.out.println();
        }
    }

}
