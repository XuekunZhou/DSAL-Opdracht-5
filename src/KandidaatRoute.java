public class KandidaatRoute implements Comparable{

    private int _score;
    private int[] _route;

    @Override
    public int compareTo(Object o) {
        if (o instanceof KandidaatRoute) {
            if (getScore() == ((KandidaatRoute) o).getScore()) {
                return 0;
            } else if (getScore() > ((KandidaatRoute) o).getScore()) {
                return 1;
            }
        }
        return -1;
    }

    public int getScore() {
        return _score;
    }

    public void setScore(int score) {
        _score = score;
    }

    public int[] get_route() {
        return _route;
    }

    public void set_route(int[] route) {
        _route = route;
    }
    public void printKandidaat() {
        for (int i : _route) {
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
}
