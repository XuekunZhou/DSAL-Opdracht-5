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
    private ArrayList<KandidaatRoute> KANDIDATEN;
    private KandidaatRoute elite;

    public RouteCalc() {
        KANDIDATEN = new ArrayList<>();
    }

    public RouteCalc(int epochs, int kandidaten, int situatie) {
        String file = situatie + ".txt";
        readSituation(file);
        KANDIDATEN = new ArrayList<>();
        startSituatie(kandidaten);
        epochTeller = 0;

        bepaalRoute();

        while (epochTeller < epochs) {
            epochTeller++;

            KANDIDATEN = mutaties(kandidaten);
            bepaalRoute();
        }
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

    public void bepaalRoute() {
        Collections.sort(KANDIDATEN);
        KandidaatRoute besteRoute = KANDIDATEN.get(0);

        if (elite == null) {
            elite = besteRoute;
        }
        else {
            if (besteRoute.compareTo(elite) == -1) {
                elite = besteRoute;
            }
        }

        System.out.println("De beste route is:");
        for (int punten : elite.get_route()) {
            System.out.print(punten + " ");
        }
        System.out.println();
        System.out.println("Score: " + elite.getScore());

    }

    public void evalueerKandidaat(KandidaatRoute kandidaatRoute) {
        int score = 0;
        int vorigePunt = 0;
        for (int huidigePunt : kandidaatRoute.get_route()) {
            int afstand = distances[vorigePunt][huidigePunt - 1];
            score += afstand;
            vorigePunt = huidigePunt - 1;
        }
        kandidaatRoute.setScore(score);
    }

    public void evalueerEpoch(){}

    private ArrayList<Integer> destinationList() {
        ArrayList<Integer> destinationList = new ArrayList<>();

        for (int i = 1; i < destinations.length; i++) {
            destinationList.add((destinations[i]));
        }

        return destinationList;
    }

    public KandidaatRoute randomKandidaat() {
        KandidaatRoute kandidaatRoute = new KandidaatRoute();
        ArrayList<Integer> destinationList = destinationList();
        Collections.shuffle(destinationList);

        int[] route = new int[destinations.length];
        route[0] = destinations[0];
        for (int i = 1; i < route.length; i++) {
            route[i] = destinationList.get(i - 1);
        }

        kandidaatRoute.set_route(route);
        return kandidaatRoute;
    }

    public void startSituatie(int kandidaten) {
        for (int kandidaat = 0; kandidaat < kandidaten; kandidaat++) {
            KandidaatRoute route = randomKandidaat();
            evalueerKandidaat(route);
            KANDIDATEN.add(route);
        }
    }

    public KandidaatRoute muteer(KandidaatRoute kandidaatRoute) {
        Random rand = new Random();

        ArrayList<Integer> array = new ArrayList<>();

        for (int punten : kandidaatRoute.get_route()) {
            array.add(punten);
        }

        int randIndex = 0;
        while (randIndex == 0) {
            randIndex = rand.nextInt(array.size() - 1);
        }

        array.add(rand.nextInt(randIndex), rand.nextInt(TOTALDEST -1) + 1);

        int[] nieuweRoute = new int[array.size()];

        for (int i = 0; i < nieuweRoute.length; i++) {
            nieuweRoute[i] = array.get(i);
        }

        kandidaatRoute.set_route(nieuweRoute);
        return kandidaatRoute;
    }

    private ArrayList<KandidaatRoute> mutaties(int kanditaten) {
        ArrayList<KandidaatRoute> _kandidaten = new ArrayList<>();

        for (int i = 0; i < kanditaten; i++) {
            KandidaatRoute nieuweKandidaat = new KandidaatRoute();
            nieuweKandidaat.set_route(elite.get_route());
            muteer(nieuweKandidaat);
            _kandidaten.add(nieuweKandidaat);
            evalueerKandidaat(nieuweKandidaat);
        }

        return _kandidaten;
    }

    public void volgendeEpoch() {}

    public void printDistances() {
        System.out.print("   ");
        for (int j = 1; j < 251; j++) {
            if (j < 10) {
                System.out.print(j + "   ");
            } else if (j < 100) {
                System.out.print(j + "  ");
            } else {
                System.out.print(j + " ");
            }
        }
        System.out.println();

        int rij = 1;
        for (int[] i : distances) {
            System.out.print(rij + ": ");
            for (int j : i) {
                if (j < 10) {
                    System.out.print(j + "   ");
                } else if (j < 100) {
                    System.out.print(j + "  ");
                } else {
                    System.out.print(j + " ");
                }
            }
            System.out.println();

            rij++;
        }
    }

}
