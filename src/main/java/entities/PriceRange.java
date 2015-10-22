package entities;

public class PriceRange {

    private int lowBound;
    private int highBound;

    public PriceRange(int lowBound, int highBound) {
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    public int getLowBound() {
        return lowBound;
    }

    public int getHighBound() {
        return highBound;
    }
}
