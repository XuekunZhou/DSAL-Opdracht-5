import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TooManyListenersException;

public class RouteCalc {

    final int TOTALDEST = 250;

    private int[] destinations;
    private int[] packages;
    private int[][] distances;

    public RouteCalc(){

    }

    public void readSituation(String file){
        File situationfile = new File (file);
        Scanner scan = null;
        try {
            scan = new Scanner(situationfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int size = scan.nextInt ();
        destinations = new int[size];
        packages = new int[size];
        distances = new int[TOTALDEST][TOTALDEST];

        for (int i = 0; i < size; i++) {
            destinations[i] = scan.nextInt();
        }
        for (int i = 0; i < size; i++) {
            packages[i] = scan.nextInt();
        }
        for (int i = 0; i < TOTALDEST; i++) {
            for (int j = 0; j < TOTALDEST; j++) {
                distances[i][j] = scan.nextInt();
            }
        }
    }

    public void printDestinations() {
        System.out.print("Desitinations: ");
        for (int i : destinations) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public void printPackages() {
        System.out.print("Packages: ");
        for (int i : packages) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public void printDistances() {
        System.out.println("Distances: ");
        for (int[] i: distances) {
            for (int j: i) {
                if (j < 10) {
                    System.out.print(j + "   ");
                }
                else if (j < 100 ){
                    System.out.print(j + "  ");
                }
                else {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
    }

    public void printNumberOfDestinations() {
        System.out.println(distances.length);
    }
}
