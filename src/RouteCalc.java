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
    private int KANDIDATEN;
    private ArrayList<KandidaatRoute> epochKandidaten;
    private KandidaatRoute elite;

    public RouteCalc() {
        epochKandidaten = new ArrayList<>();
    }

    public RouteCalc(int epochs, int kandidaten, int situatie) {
        String file = situatie + ".txt";
        readSituation(file);
        EPOCHS = epochs;
        KANDIDATEN = kandidaten;
        epochKandidaten = new ArrayList<>();
        startSituatie();
        epochTeller = 0;

        evalueerEpoch();
        printKandidaten();
        bepaalRoute();

        while (epochTeller < EPOCHS) {
            epochTeller++;

            volgendeEpoch();
            System.out.println("Epoch: " + epochTeller);
            evalueerEpoch();
            printKandidaten();
            bepaalRoute();

        }
    }

    private void printKandidaten() {
        int kandidaat = 0;
        System.out.println("De kandidaten zijn:");
        for (KandidaatRoute route : epochKandidaten) {
            if (kandidaat < 10) {
                System.out.print("Route " + kandidaat++ + ":   ");
            } else if (kandidaat < 100) {
                System.out.print("Route " + kandidaat++ + ":  ");
            } else {
                System.out.print("Route " + kandidaat++ + ": ");
            }
            for (int i : route.get_route()) {
                if (i < 10) {
                    System.out.print(i + "   ");
                } else if (i < 100) {
                    System.out.print(i + "  ");
                } else {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
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

    public void evalueerEpoch() {
        for (KandidaatRoute kandidaatRoute : epochKandidaten) {
            evalueerKandidaat(kandidaatRoute);
        }
    }

    public void evalueerKandidaat(KandidaatRoute kandidaatRoute) {
        int pakketscore = 25;
        int pakketpositie = 1;
        int score = 0;
        int vorigePunt = 0;

        for (int huidigePunt : kandidaatRoute.get_route()) {
            int afstand = distances[vorigePunt][huidigePunt - 1];
            score += afstand;

            for (DestinationPackage item : destinationPackageList()) {
                if (item.getDestination() == huidigePunt -1) {
                    score -= item.getPackages() * pakketscore / pakketpositie;
                }
            }

            pakketpositie++;
            vorigePunt = huidigePunt - 1;
        }
        kandidaatRoute.setScore(score);
    }

    public void bepaalRoute() {
        Collections.sort(epochKandidaten);
        KandidaatRoute besteRoute = epochKandidaten.get(0);

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
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private ArrayList<DestinationPackage> destinationPackageList() {
        ArrayList<DestinationPackage> destinationList = new ArrayList<>();

        for (int i = 0; i < destinations.length; i++) {
            DestinationPackage destinationPackage = new DestinationPackage();
            destinationPackage.setDestination(destinations[i]);
            destinationPackage.setPackages(packages[i]);
            destinationList.add(destinationPackage);
        }

        return destinationList;
    }

    public KandidaatRoute randomKandidaat() {
        KandidaatRoute kandidaatRoute = new KandidaatRoute();
        ArrayList<DestinationPackage> destinationList = destinationPackageList();
        Collections.shuffle(destinationList);

        int[] route = new int[destinations.length];
        route[0] = destinations[0];
        for (int i = 1; i < route.length; i++) {
            route[i] = destinationList.get(i - 1).getDestination();
        }

        kandidaatRoute.set_route(route);
        return kandidaatRoute;
    }

    public void startSituatie() {
        for (int kandidaat = 0; kandidaat < KANDIDATEN; kandidaat++) {
            KandidaatRoute route = randomKandidaat();
            epochKandidaten.add(route);
        }
    }

    public KandidaatRoute muteer(KandidaatRoute kandidaatRoute) {
        Random rand = new Random();

        ArrayList<Integer> array = new ArrayList<>();

        for (int punten : kandidaatRoute.get_route()) {
            array.add(punten);
        }

        array.add(rand.nextInt(array.size() - 2) + 1, rand.nextInt(TOTALDEST -1) + 1);

        int[] nieuweRoute = new int[array.size()];

        for (int i = 0; i < nieuweRoute.length; i++) {
            nieuweRoute[i] = array.get(i);
        }

        kandidaatRoute.set_route(nieuweRoute);
        return kandidaatRoute;
    }

    private void volgendeEpoch() {
        for (int i = 1; i < epochKandidaten.size(); i++) {
            if (i < KANDIDATEN / 2) {
                epochKandidaten.set(i, muteer(epochKandidaten.get(i)));
            } else {
                epochKandidaten.set(i, randomKandidaat());
            }
        }
    }


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

    private class DestinationPackage {
        private int destination;
        private int packages;


        public int getDestination() {
            return destination;
        }

        public void setDestination(int destination) {
            this.destination = destination;
        }

        public int getPackages() {
            return packages;
        }

        public void setPackages(int packages) {
            this.packages = packages;
        }
    }

}
