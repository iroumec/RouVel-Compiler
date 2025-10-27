package semantic;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.ArrayDeque;
import utilities.Printer;

public final class ReversePolish {

    private static final ReversePolish INSTANCE = new ReversePolish();

    private List<String> polishes;
    private final List<String> temporalPolishes;
    private final Deque<Integer> stackedBifurcation;

    private ReversePolish() {
        this.polishes = new ArrayList<>();
        this.temporalPolishes = new ArrayList<>();
        this.stackedBifurcation = new ArrayDeque<>();
    }

    public static ReversePolish getInstance() {
        return INSTANCE;
    }

    public void addPolish(String symbol) {
        // this.polishes.add(new Polish(symbol));
        this.polishes.add(symbol);
    }

    public void addFalseBifurcation() {
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("FB");
    }

    public void addInconditionalBifurcation() {
        int incompletePolish = this.stackedBifurcation.pop() - 1;
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("IB");
        this.polishes.set(incompletePolish, Integer.toString(this.polishes.size() + 1));
    }

    public void completeSelection() {
        this.polishes.set(this.stackedBifurcation.pop() - 1, Integer.toString(this.polishes.size() + 1));
    }

    public void registerDoBody() {
        this.stackedBifurcation.push(this.polishes.size() + 1);
    }

    public void addTrueBifurcation() {
        this.stackedBifurcation.pop();
        this.polishes.set(this.polishes.size() - 2, Integer.toString(this.stackedBifurcation.pop()));
        this.polishes.set(this.polishes.size() - 1, "TB");
    }

    /**
     * No siempre todos los factores deben agregarse a la polaca. Acá se guardarán
     * estos y, luego,
     * de ser necesarios, se añaden.
     * 
     */

    public void addTemporalPolish(String polish) {

        this.temporalPolishes.add(polish);
    }

    public void makeTemporalPolishesDefinitive() {

        for (String polish : this.temporalPolishes) {
            polishes.add(polish);
        }

        this.emptyTemporalPolishes();
    }

    // --------------------------------------------------------------------------------------------

    public void emptyTemporalPolishes() {
        this.temporalPolishes.clear();
    }

    // --------------------------------------------------------------------------------------------

    public void print() {

        Printer.printSeparator();
        Printer.printCentered("Polaca Inversa");
        Printer.printSeparator();

        int nroPolaca = 0;
        for (String polish : this.polishes) {
            Printer.printFramed(++nroPolaca + " " + polish);
        }

        Printer.printSeparator();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();
        int nroPolaca = 1;
        // for (Polish polish : this.polishes) {
        for (String polish : this.polishes) {
            out.append(nroPolaca + " ").append(polish).append('\n');
            nroPolaca++;
        }

        return out.toString();
    }
}
