package semantic;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.ArrayDeque;
import utilities.Printer;

public final class ReversePolish {

    private static final ReversePolish INSTANCE = new ReversePolish();

    // --------------------------------------------------------------------------------------------

    private int separations;
    private int polishNumber;
    private final List<Element> elements;
    private final List<String> temporalPolishes;
    private final Deque<Promise> stackedPromises;
    private final Deque<AggregatePoint> aggregatePoints;

    // --------------------------------------------------------------------------------------------

    private ReversePolish() {
        this.separations = 0;
        this.polishNumber = 0;
        this.elements = new ArrayList<>();
        this.aggregatePoints = new ArrayDeque<>();
        this.stackedPromises = new ArrayDeque<>();
        this.temporalPolishes = new ArrayList<>();
    }

    // --------------------------------------------------------------------------------------------

    public static ReversePolish getInstance() {
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------
    // Agregado de Polacas
    // --------------------------------------------------------------------------------------------

    public void addPolish(String symbol) {
        this.elements.add(new Polish(symbol, ++this.polishNumber));
    }

    // --------------------------------------------------------------------------------------------
    // Agregado de Separadores
    // --------------------------------------------------------------------------------------------

    public void addSeparation(String separationLabel) {
        this.separations++;
        this.elements.add(new Separator(separationLabel));
    }

    // --------------------------------------------------------------------------------------------
    // Construcción de la Polaca de Iteraciones
    // --------------------------------------------------------------------------------------------

    public void stackBifurcationPoint() {
        this.stackedPromises.push(new Promise(polishNumber + 1, this.separations));
    }

    // --------------------------------------------------------------------------------------------

    public void connectToLastBifurcationPoint() {
        Promise promise = this.stackedPromises.pop();
        this.elements.add(new BifurcationPoint(String.valueOf(promise.bifurcationPoint()), ++this.polishNumber));
    }

    // --------------------------------------------------------------------------------------------
    // Construcción de la Polaca de Selecciones
    // --------------------------------------------------------------------------------------------

    public void promiseBifurcationPoint() {
        // El agregado del elemento nulo es necesario para que no errores con el manejo
        // de los índices del arreglo cuando hay varios if-else anidados.
        // Se está diciendo: "Reservame un lugar que luego te prometo que lo lleno.".
        this.elements.add(null);
        this.stackedPromises.push(new Promise(++this.polishNumber + this.separations, this.separations));
    }

    // --------------------------------------------------------------------------------------------

    public Promise getLastPromise() {
        return this.stackedPromises.pop();
    }

    // --------------------------------------------------------------------------------------------

    public void fulfillPromise(Promise promise) {
        // Se debe remover el nulo que se agregó para realizar la promesa.
        // Los separadores agregados deben considerarse para ir al índice correcto,
        // ya que ocupan lugar en la lista.
        this.elements.remove(promise.bifurcationPoint() - 1);
        this.elements.add(promise.bifurcationPoint() - 1,
                new BifurcationPoint(String.valueOf(polishNumber + 1),
                        promise.bifurcationPoint() - promise.separations()));
    }

    // --------------------------------------------------------------------------------------------
    // Probando para las Lambda
    // --------------------------------------------------------------------------------------------

    public void setAggregatePoint() {
        // Comienza a partir del último elemento, con `polishNumber` como primer número
        // de polaca.
        this.aggregatePoints.push(new AggregatePoint(this.elements.size(), polishNumber));
    }

    // --------------------------------------------------------------------------------------------

    public void fillLastAggregatePoint(String... symbols) {
        AggregatePoint aggregatePoint = this.aggregatePoints.pop();

        int index = aggregatePoint.startIndex();
        int polishNumber = aggregatePoint.firstPolishNumber();

        for (String symbol : symbols) {
            this.elements.add(++index, new Polish(symbol, ++polishNumber));
        }

        // Incrementar a todos los elementos *posteriores* a los nuevos.
        this.elements.subList(aggregatePoint.startIndex() + symbols.length + 1, elements.size())
                .forEach(e -> e.increaseNumber(symbols.length));
    }

    // --------------------------------------------------------------------------------------------
    // Almacen Temporal de Polacas
    // --------------------------------------------------------------------------------------------

    /**
     * No siempre todos los factores deben agregarse a la polaca. Acá se guardarán
     * estos y, luego, de ser necesarios, se añaden.
     */

    public void addTemporalPolish(String polish) {

        this.temporalPolishes.add(polish);
    }

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    private abstract class Element {

        public abstract void print();

        public abstract void increaseNumber(int n);
    }

    // --------------------------------------------------------------------------------------------

    private class Polish extends Element {

        private int number;
        private String polishText;

        private Polish(String polishText, int number) {
            this.number = number;
            this.polishText = polishText;
        }

        @Override
        public void increaseNumber(int n) {
            this.number += n;
        }

        @Override
        public void print() {
            Printer.printFramed(number + " " + polishText);
        }

        protected String getPolishText() {
            return this.polishText;
        }

        protected void setPolishText(String newText) {
            this.polishText = newText;
        }
    }

    // --------------------------------------------------------------------------------------------

    private class BifurcationPoint extends Polish {

        private BifurcationPoint(String polishText, int number) {
            super(polishText, number);
        }

        @Override
        public void increaseNumber(int n) {
            super.increaseNumber(n);
            this.setPolishText(String.valueOf(Integer.valueOf(super.getPolishText()) + n));
        }
    }

    // --------------------------------------------------------------------------------------------

    private class Separator extends Element {

        private String separationLabel;

        private Separator(String separationLabel) {
            this.separationLabel = separationLabel;
        }

        @Override
        public void increaseNumber(int n) {
            // Intentionally empty...
        }

        @Override
        public void print() {
            Printer.printSeparator();
            Printer.printCentered(separationLabel);
            Printer.printSeparator();
        }
    }
}
