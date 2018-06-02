package drawingEntities;

public class Position {
    private double e;
    private double n;
    private double h;

    public double getE() {
        return e;
    }

    public double getN() {
        return n;
    }

    public double getH() {
        return h;
    }

    public Position(double e, double n, double h) {
        this.e = e;
        this.n = n;
        this.h = h;
    }
}