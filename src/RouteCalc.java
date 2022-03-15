import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RouteCalc {

    final int TOTALDEST = 250;

    private int[] destinations;
    private int[] packages;
    private int[][] distances;

    private int EPOCHS;
    private int epochTeller;
    private ArrayList<KandidaatRoute> KANDIDATEN = new ArrayList<>();

    public RouteCalc(){
    }

    public RouteCalc(int epochs, int kandidaten){
        readSituation("1.txt");

        for (int i = 0; i < kandidaten; i++) {
            KandidaatRoute kandidaat = randomKandidaat();
            evalueerKandidaat(kandidaat);
            KANDIDATEN.add(kandidaat);
        }

        bepaalRoute();
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

        //

        KandidaatRoute route = randomKandidaat();
        evalueerKandidaat(route);

    }

    public void bepaalRoute() {
        Collections.sort(KANDIDATEN);
        System.out.println("Beste route score: " + KANDIDATEN.get(0).getScore());
    }

    public void evalueerKandidaat(KandidaatRoute kandidaatRoute) {
        int score = 0;
        int vorigePunt = 1;

        for (int huidigePunt : kandidaatRoute.get_route()) {
            int afstand = distances[vorigePunt][huidigePunt];

            score += afstand;
        }

        System.out.println(score);
        System.out.println();
        kandidaatRoute.setScore(score);
    }

    public void evalueerEpoch(){}

    public KandidaatRoute randomKandidaat() {
        Random rand = new Random();
        KandidaatRoute route = new KandidaatRoute();
        ArrayList<Integer> routeArray = new ArrayList<>();

        for (int i : destinations) {
            routeArray.add(i);
        }

        routeArray.remove(0);

        for (int i = 0; i < 10; i++) {
            routeArray.add(rand.nextInt(TOTALDEST - 1) + 1);
        }

        Collections.shuffle(routeArray);

        int[] arr = new int[routeArray.size() + 1];

        for (int i = 1; i < arr.length; i++) {
            arr[i] = routeArray.get(i - 1);
        }

        arr[0] = 1;
        route.set_route(arr);


        return route;
    }

    public void startSituatie() {}

    public KandidaatRoute muteer(KandidaatRoute kandidaatRoute) {
        return null;
    }

    public void volgendeEpoch() {}

}
