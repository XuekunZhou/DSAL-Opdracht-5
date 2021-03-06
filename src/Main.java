import java.util.Random;

public class Main {

    public static void genereer(){
        Random rand = new Random();
        int[][] temp = new int[250][250];
        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 250; j++) {
                if (i==j){
                    temp[i][j]=0;
                }
                else if (i>j){
                    temp[i][j] = temp[j][i];
                }
                else{
                    temp[i][j] = rand.nextInt(100)+1;
                }
            }
        }

        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 250; j++) {
                    System.out.printf("%3d ",temp[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] situatie1 = new int[10][10];



        for (int i = 5; i < 11; i++) {
            System.out.println("Aantal epochs: " + i * 100);
            for (int j = 1; j < 11; j++) {
                System.out.println("Aantal kandidaten: " + j * 100);
                situatie1[i - 1][j - 1] = new RouteCalc(i * 100, j * 100, 3).getElite().getScore();
            }
        }

        for (int[] i : situatie1) {
            for (int j : i) {
                System.out.print(j + "    ");
            }
            System.out.println();
        }

//        alg.printDistances();
    }
}
