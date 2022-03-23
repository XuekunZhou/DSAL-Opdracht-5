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


    public RouteCalc(int epochs, int kandidaten, int situatie) {
        String file = situatie + ".txt";
        readSituation(file);
        EPOCHS = epochs;
        KANDIDATEN = kandidaten;
        epochKandidaten = new ArrayList<>();
        startSituatie();
        epochTeller = 0;

        evalueerEpoch();
//        printEpochKandidaten();
        bepaalRoute();

        while (epochTeller < EPOCHS) {
            epochTeller++;

            volgendeEpoch();
            System.out.println("Epoch: " + epochTeller);
            evalueerEpoch();
//            printEpochKandidaten();
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

    public void evalueerEpoch() {
        for (KandidaatRoute kandidaatRoute : epochKandidaten) {
            evalueerKandidaat(kandidaatRoute);
        }
    }

    public void evalueerKandidaat(KandidaatRoute kandidaatRoute) {
        int pakketscore = 100;
        int vermistPakket = 400;
        int valseStart = 10000;
        int pakketpositie = 1;
        int score = 0;
        int vorigePunt = 1;
        ArrayList<Integer> bestemmingenGeweest = new ArrayList<>();

        if (kandidaatRoute.get_route()[0] != 1 ) {
            score += valseStart;
        }

        for (DestinationPackage item : destinationPackageList()) {
            if (!kandidaatRoute.get_routeAsArrayList().contains(item.destination)) {
                score += item.packages * vermistPakket;
            }
        }

        for (int huidigePunt : kandidaatRoute.get_route()) {
            int afstand = distances[vorigePunt - 1][huidigePunt - 1];
            score += afstand;

            if (!bestemmingenGeweest.contains(huidigePunt)) {
                for (DestinationPackage item : destinationPackageList()) {
                    if (item.getDestination() == huidigePunt) {
                        score -= item.getPackages() * pakketscore / pakketpositie;
                    }
                }
            }

            bestemmingenGeweest.add(huidigePunt);
            pakketpositie++;
            vorigePunt = huidigePunt;
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
        elite.printKandidaat();
        System.out.println();
        System.out.println("Score: " + elite.getScore());
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private ArrayList<DestinationPackage> destinationPackageList() {
        ArrayList<DestinationPackage> destinationList = new ArrayList<>();

        for (int i = 1; i < destinations.length; i++) {
            DestinationPackage destinationPackage = new DestinationPackage();
            destinationPackage.setDestination(destinations[i]);
            destinationPackage.setPackages(packages[i]);
            destinationList.add(destinationPackage);
        }

        return destinationList;
    }

    public KandidaatRoute randomKandidaat() {
        KandidaatRoute kandidaatRoute = new KandidaatRoute();

        Random rand = new Random();

        int routeGrootte = rand.nextInt(15) + 5;

        int[] route = new int[routeGrootte];

        for (int i = 0; i < routeGrootte; i++) {
            route[i] = rand.nextInt(TOTALDEST - 1) + 1;
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
        int randIndex = rand.nextInt(array.size() - 2) + 1;
        int randDestination = rand.nextInt(TOTALDEST -1) + 1;

        while (kandidaatRoute.get_route()[randIndex] == randDestination || kandidaatRoute.get_route()[randIndex - 1] == randDestination) {
            randIndex = rand.nextInt(array.size() - 2) + 1;
            randDestination = rand.nextInt(TOTALDEST -1) + 1;
        }

        array.add(randIndex, randDestination);

        int[] nieuweRoute = new int[array.size()];

        for (int i = 0; i < nieuweRoute.length; i++) {
            nieuweRoute[i] = array.get(i);
        }

        kandidaatRoute.set_route(nieuweRoute);
        return kandidaatRoute;
    }

    private void volgendeEpoch() {
        ArrayList<KandidaatRoute> Mutaties = new ArrayList<>();
        for (int i = 0 ; i < KANDIDATEN * 0.45; i++) {
            KandidaatRoute kr = epochKandidaten.get(i).createCopy();
            Mutaties.add(muteer(kr));
        }
        int j = 0;
        for (int i = (int) (KANDIDATEN * 0.45); i < KANDIDATEN; i++) {
            if (i <= KANDIDATEN * 0.9 - 1) {
                epochKandidaten.set(i, Mutaties.get(j));
                j++;

            } else {
                epochKandidaten.set(i, randomKandidaat());
            }
        }
    }

    private void printEpochKandidaten() {
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
            route.printKandidaat();
        }
        System.out.println();
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

    public KandidaatRoute getElite() {
        return elite;
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
