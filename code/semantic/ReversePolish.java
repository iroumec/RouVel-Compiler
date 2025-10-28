package semantic;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.ArrayDeque;
import utilities.Printer;

public final class ReversePolish {

    private static final ReversePolish INSTANCE = new ReversePolish();

    private static final String ENTRY_TEXT = "Entering scope";
    private static final String EXIT_TEXT = "Leaving scope";

    private int polishNumber;
    private final List<Element> elements;
    private final List<String> temporalPolishes;
    private final Deque<Integer> stackedBifurcation;

    private ReversePolish() {
        this.polishNumber = 0;
        this.elements = new ArrayList<>();
        this.temporalPolishes = new ArrayList<>();
        this.stackedBifurcation = new ArrayDeque<>();
    }

    public static ReversePolish getInstance() {
        return INSTANCE;
    }

    public void addPolish(String symbol) {
        this.elements.add(new Polish(symbol, ++this.polishNumber));
    }

    public void addEntrySeparation(String separationLabel) {
        this.addSeparation(separationLabel, ENTRY_TEXT);
    }

    public void addExitSeparation(String separationLabel) {
        this.addSeparation(separationLabel, EXIT_TEXT);
    }

    private void addSeparation(String separationLabel, String prefixLabel) {
        this.elements.add(new Separator(separationLabel, prefixLabel));
    }

    public void addFalseBifurcation() {
        this.stackedBifurcation.push(++this.polishNumber);
        this.elements.add(new Polish("FB", ++polishNumber));
    }

    public void addInconditionalBifurcation() {
        // int incompletePolish = this.stackedBifurcation.pop() - 1;
        // this.polishes.add(" ");
        this.stackedBifurcation.push(++polishNumber);
        // this.polishes.add("IB");
        // this.polishes.set(incompletePolish, Integer.toString(this.polishes.size() +
        // 1));
    }

    public void completeSelection() {
        // this.polishes.set(this.stackedBifurcation.pop() - 1,
        // Integer.toString(this.polishes.size() + 1));
    }

    public void registerDoBody() {
        // this.stackedBifurcation.push(this.polishes.size() + 1);
    }

    public void addTrueBifurcation() {
        this.stackedBifurcation.pop();
        // this.polishes.set(this.polishes.size() - 2,
        // Integer.toString(this.stackedBifurcation.pop()));
        // this.polishes.set(this.polishes.size() - 1, "TB");
    }

    /**
     * No siempre todos los factores deben agregarse a la polaca. Acá se guardarán
     * estos y, luego, de ser necesarios, se añaden.
     */

    public void addTemporalPolish(String polish) {

        this.temporalPolishes.add(polish);
    }

    public void makeTemporalPolishesDefinitive() {

        for (String polish : this.temporalPolishes) {
            this.elements.add(new Polish(polish, ++polishNumber));
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

        for (Element element : this.elements) {
            element.print();
        }

        Printer.printSeparator();
    }

    private abstract class Element {

        public abstract void print();

    }

    private class Polish extends Element {

        private int number;
        private String polishText;

        private Polish(String polishText, int number) {
            this.number = number;
            this.polishText = polishText;
        }

        @Override
        public void print() {
            Printer.printFramed(number + " " + polishText);
        }

    }

    private class Separator extends Element {

        private String prefixLabel;
        private String separationLabel;

        private Separator(String separationLabel, String prefixLabel) {
            this.prefixLabel = prefixLabel;
            this.separationLabel = separationLabel;
        }

        @Override
        public void print() {
            Printer.printSeparator();
            Printer.printCentered(String.format("%s '%s'", prefixLabel, separationLabel));
            Printer.printSeparator();
        }
    }
}
