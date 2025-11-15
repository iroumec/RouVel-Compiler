package semantic;

import java.util.List;

import common.Monitor;

import java.util.Deque;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ArrayDeque;

import utilities.MessageCollector;
import utilities.Printer;

public final class ReversePolish implements Iterable<String> {

    private static final ReversePolish INSTANCE = new ReversePolish();

    // --------------------------------------------------------------------------------------------

    private boolean debug = false;

    // --------------------------------------------------------------------------------------------

    private int separations;
    private int polishNumber;

    // Es un polish number.
    private int lastSafeState;
    private Function functionCalled;

    // --------------------------------------------------------------------------------------------

    private final List<Element> elements;
    private final List<Function> functions;
    private final List<String> temporalPolishes;

    // --------------------------------------------------------------------------------------------

    private final Deque<Promise> stackedPromises;
    private final Deque<AggregatePoint> aggregatePoints;

    // --------------------------------------------------------------------------------------------

    private ReversePolish() {
        this.separations = 0;
        this.polishNumber = 0;
        this.elements = new ArrayList<>();
        this.functions = new ArrayList<>();
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

        if (this.debug) {
            System.out.println("Polish added: " + symbol);
        }
    }

    // --------------------------------------------------------------------------------------------

    private void addPolishes(List<String> polishes) {

        for (String polish : polishes) {
            this.addPolish(polish);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Agregado de Separadores
    // --------------------------------------------------------------------------------------------

    public void addSeparation(String separationLabel) {
        this.separations++;
        this.elements.add(new Separator(separationLabel));

        if (this.debug) {
            System.out.println("Separation added: " + separationLabel);
        }
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
        this.stackedPromises.push(new Promise(this.elements.size(), this.separations));
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
            // It would be more efficient adding the polishes directly to "elements".
            // But it was preferred to be done like this so there is only one point of
            // adding to the list.
            this.addPolish(polish);
        }

        this.emptyTemporalPolishes();
    }

    // --------------------------------------------------------------------------------------------

    public void emptyTemporalPolishes() {
        this.temporalPolishes.clear();
    }

    // --------------------------------------------------------------------------------------------
    // Manejo de Estado Seguro
    // --------------------------------------------------------------------------------------------

    public void startFunctionDeclaration(String functionName) {

        this.functions.add(new Function(functionName));

        this.addPolish(functionName);
        this.addPolish("open-function");
    }

    // --------------------------------------------------------------------------------------------

    public void addParameter(String id, String type, String semantic) {

        this.functions.getLast().addParameter(id, type, semantic);
    }

    // --------------------------------------------------------------------------------------------

    public void closeFunctionDeclaration(String functionName) {

        // this.functions.getLast().closeDeclaration();

        this.addPolish(functionName);
        this.addPolish("close-function");
    }

    // --------------------------------------------------------------------------------------------

    public void discardFunctionDeclaration(String functionName) {

        this.functions.removeLast();
    }

    // --------------------------------------------------------------------------------------------

    public void startFunctionCall(String functionName) {

        Function functionCalled = null;

        for (Function function : this.functions) {

            if (function.getName().equals(functionName)) {
                this.functionCalled = function;
                break; // TODO: mejorar esto.
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    public void addArgument(String parameter) {

        functionCalled.addArgument(parameter, this.temporalPolishes);

        this.temporalPolishes.clear();
    }

    // --------------------------------------------------------------------------------------------

    public void closeFunctionCall() {

        List<String> polishesGenerated = this.functionCalled.closeCall(this, "->");

        for (String polish : polishesGenerated) {
            this.addPolish(polish);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void discardFunctionCall() {

        this.functionCalled = null;
    }

    // --------------------------------------------------------------------------------------------

    public void openLoop() {

        this.stackBifurcationPoint();
        this.addPolish(String.valueOf(this.polishNumber + 1));
        this.addPolish("open-loop");
    }

    // --------------------------------------------------------------------------------------------

    public void closeLoop() {

        this.connectToLastBifurcationPoint();
        this.addPolish("TB");
        this.addPolish("close-loop");
    }

    // --------------------------------------------------------------------------------------------
    // Selecciones
    // --------------------------------------------------------------------------------------------

    /**
     * Open If / IF-Else.
     */
    public void openSelection() {
        this.promiseBifurcationPoint();
        this.addPolish("FB");
    }

    /**
     * Close If / If-Else.
     */
    public void closeSelection() {
        this.fulfillPromise(this.getLastPromise());
    }

    /**
     * Open Else.
     */
    public void openAlternative() {
        // Se obtiene la promesa del cuerpo then.
        Promise promise = this.getLastPromise();

        // Se promete un nuevo punto de bifurcación.
        this.promiseBifurcationPoint();
        this.addPolish("IB");

        // Se cumple la promesa obtenida al comienzo.
        // Es necesario que se realice así para respetar los índices de la polaca.
        this.fulfillPromise(promise);
    }

    // --------------------------------------------------------------------------------------------
    // Manejo de Estado Seguro
    // --------------------------------------------------------------------------------------------

    public void recordSafeState() {
        this.lastSafeState = this.elements.size();

        if (this.debug) {
            System.out.println("Safe state recorded in: " + this.lastSafeState);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void returnToLastSafeState() {
        // Se eliminan todos los elementos agregados luego del último estado seguro.
        elements.subList(lastSafeState, elements.size()).clear();

        if (this.debug) {
            System.out.println("The reverse polished was restored to last safe state: " + this.lastSafeState);
        }
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

    /**
     * De esta forma, se permite recorrer la lista de polacas sin exponerla.
     */
    @Override
    public Iterator<String> iterator() {

        return new Iterator<String>() {

            private final Iterator<Element> it = elements.iterator();
            private String nextPolish = findNext();

            private String findNext() {
                while (it.hasNext()) {
                    String polish = it.next().getPolish();
                    if (polish != null) {
                        return polish;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextPolish != null;
            }

            @Override
            public String next() {
                String current = nextPolish;
                nextPolish = findNext();
                return current;
            }
        };
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    private abstract class Element {

        public abstract void print();

        public abstract String getPolish();

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

        @Override
        public String getPolish() {
            return this.polishText;
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

        @Override
        public String getPolish() {
            return null;
        }
    }

    // --------------------------------------------------------------------------------------------
    // Inner Records
    // --------------------------------------------------------------------------------------------

    private record AggregatePoint(int startIndex, int firstPolishNumber) {
    }

    /**
     * Este 'record' se justifica en que los puntos de bifurcación que se prometen
     * pueden completarse en un futuro en el que la cantidad actual de separaciones
     * no es la que había al momento en el que se realizó la promesa. Como
     * resultado, el índice de la polaca podría ser erróneo.
     * 
     * Lo mismo con los AggregatePoints.
     */
    private record Promise(int bifurcationPoint, int separations) {

    }

}
