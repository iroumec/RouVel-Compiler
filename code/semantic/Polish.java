package semantic;

public class Polish {

    private static int nroPolacas = 0;

    private final int nroPolaca;
    private final String symbol;

    public Polish(String symbol) {
        this.nroPolaca = ++nroPolacas;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.nroPolaca + " " + this.symbol;
    }

}
