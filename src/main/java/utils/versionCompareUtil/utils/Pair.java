package utils.versionCompareUtil.utils;

public class Pair<E1, E2> {
    private E1 first;
    private E2 second;

    public E1 first() {
        return this.first;
    }

    public void setFirst(E1 first) {
        this.first = first;
    }

    public E2 second() {
        return this.second;
    }

    public void setSecond(E2 second) {
        this.second = second;
    }

    public Pair(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }
}
