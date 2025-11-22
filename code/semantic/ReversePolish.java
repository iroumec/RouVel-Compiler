package semantic;

import java.util.List;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ArrayDeque;

import common.Monitor;
import utilities.Printer;
import common.SymbolTable;
import common.SymbolDirector;

public final class ReversePolish implements Iterable<String> {

    private static final ReversePolish INSTANCE = new ReversePolish();

    // --------------------------------------------------------------------------------------------

    private boolean debug = false;

    // --------------------------------------------------------------------------------------------

    private int polishNumber;
    private final List<Element> elements;

    // --------------------------------------------------------------------------------------------

    // Es un polish number.
    private int lastSafeState;
    private int lastPolishNumber;

    // --------------------------------------------------------------------------------------------

    private int lambdaCounter;
    private Function functionCalled;
    private final List<Function> functions;

    // --------------------------------------------------------------------------------------------

    private final Deque<Integer> stackedPromises;

    // --------------------------------------------------------------------------------------------

    /**
     * Planteado únicamente con fines estéticos.
     * Coloca un separador previo a la impresión determinada por la clave.
     */
    private final Map<Integer, List<String>> separations;

    // --------------------------------------------------------------------------------------------

    private ReversePolish() {
        this.polishNumber = 0;
        this.lambdaCounter = 0;
        this.elements = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.separations = new HashMap<>();
        this.stackedPromises = new ArrayDeque<>();
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
            System.out.println(Monitor.getInstance().getLineNumber() + ": Polish added: " + symbol);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Agregado de Separadores
    // --------------------------------------------------------------------------------------------

    public void addSeparation(String separationLabel) {

        List<String> separations = this.separations.get(polishNumber);

        if (separations != null) {

            separations.add(separationLabel);
        } else {

            separations = new ArrayList<>();
            separations.add(separationLabel);
            this.separations.put(polishNumber, separations);
        }

        if (this.debug) {
            System.out.println("Separation added: " + separationLabel);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Construcción de la Polaca de Iteraciones
    // --------------------------------------------------------------------------------------------

    public void stackBifurcationPoint() {
        this.stackedPromises.push(polishNumber + 1);
    }

    // --------------------------------------------------------------------------------------------

    public void connectToLastBifurcationPoint() {
        int promise = this.stackedPromises.pop();
        this.elements.add(new BifurcationPoint(String.valueOf(promise), ++this.polishNumber));
    }

    // --------------------------------------------------------------------------------------------
    // Construcción de la Polaca de Selecciones
    // --------------------------------------------------------------------------------------------

    public void promiseBifurcationPoint() {
        // El agregado del elemento nulo es necesario para que no errores con el manejo
        // de los índices del arreglo cuando hay varios if-else anidados.
        // Se está diciendo: "Reservame un lugar que luego te prometo que lo lleno.".
        this.elements.add(null);
        this.polishNumber++;
        this.stackedPromises.push(this.elements.size());
    }

    // --------------------------------------------------------------------------------------------

    public int getLastPromise() {
        return this.stackedPromises.pop();
    }

    // --------------------------------------------------------------------------------------------

    public void fulfillPromise(int promise) {

        // Se debe remover el nulo que se agregó para realizar la promesa.
        // Los separadores agregados deben considerarse para ir al índice correcto,
        // ya que ocupan lugar en la lista.
        this.elements.remove(promise - 1);
        this.elements.add(promise - 1,
                new BifurcationPoint(String.valueOf(polishNumber + 1),
                        promise));
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

    // --------------------------------------------------------------------------------------------

    /**
     * Close If / If-Else.
     */
    public void closeSelection() {
        this.fulfillPromise(this.getLastPromise());
        this.addPolish("close-selection");
    }

    // --------------------------------------------------------------------------------------------

    public void discardSelection() {
        if (!this.stackedPromises.isEmpty()) {
            this.getLastPromise();
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Open Else.
     */
    public void openAlternative() {
        // Se obtiene la promesa del cuerpo then.
        int promise = this.getLastPromise();

        // Se promete un nuevo punto de bifurcación.
        this.promiseBifurcationPoint();
        this.addPolish("UB");

        // Se cumple la promesa obtenida al comienzo.
        // Es necesario que se realice así para respetar los índices de la polaca.
        this.fulfillPromise(promise);
    }

    // --------------------------------------------------------------------------------------------
    // Manejo de Funciones
    // --------------------------------------------------------------------------------------------

    public void startFunctionDeclaration(String functionName) {

        this.functions.add(new Function(functionName));

        this.addPolish(functionName);
        this.addPolish("open-function");
    }

    // --------------------------------------------------------------------------------------------

    public void addParameter(String id, String semantic) {

        this.functions.getLast().addParameter(id, semantic);
    }

    // --------------------------------------------------------------------------------------------

    public void closeFunctionDeclaration(String functionName) {

        this.addPolish(functionName);
        this.addPolish("close-function");
    }

    // --------------------------------------------------------------------------------------------

    public void discardFunctionDeclaration(String functionName) {

        this.functions.removeLast();
    }

    // --------------------------------------------------------------------------------------------

    public void startFunctionCall(String functionName) {

        boolean functionFound = false;

        Iterator<Function> iterator = this.functions.iterator();
        while (!functionFound && iterator.hasNext()) {

            Function currentFunction = iterator.next();

            if (currentFunction.getName().equals(functionName)) {
                this.functionCalled = currentFunction;
                this.addPolish(currentFunction.getName());
                functionFound = true;
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    public void addArgument(String parameter) {

        if (functionCalled != null) {
            functionCalled.addArgument(parameter, getRealParameters(functionCalled.getName()));
        }
    }

    // --------------------------------------------------------------------------------------------

    public List<String> getRealParameters(String stopPolish) {

        List<String> out = new ArrayList<>();

        while (!this.elements.isEmpty() && this.elements.getLast().getPolish() != null
                && !this.elements.getLast().getPolish().equals(stopPolish)) {

            out.addFirst(this.removeLastPolish());
        }

        return out;
    }

    // --------------------------------------------------------------------------------------------

    private String removeLastPolish() {

        this.polishNumber--;
        return this.elements.removeLast().getPolish();
    }

    // --------------------------------------------------------------------------------------------

    public void closeFunctionCall() {

        // Se elimina el nombre de la función, que se utilizó como delimitador.
        // TODO: ¿QUÉ PASA CON FUNCIÓN QUE SE LLAMA PASÁNDOLE A SÍ MISMA UN RESULTADO DE
        // SU PROPIA FUNCIÓN?
        // DEBE CAMBIARSE EL DELIMITADOR.
        this.removeLastPolish();

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
    // Manejo de Lambdas
    // --------------------------------------------------------------------------------------------

    public String startLambdaDeclaration(String scope) {

        String lambdaName = "lambda" + this.lambdaCounter++;
        String scopedLambdaName = lambdaName + ":" + scope;

        this.functions.add(new Function(scopedLambdaName));

        this.addPolish(scopedLambdaName);
        this.addPolish("open-lambda");

        SymbolTable.getInstance().addEntry(SymbolDirector.createNewFunction(scopedLambdaName));

        return lambdaName;
    }

    // --------------------------------------------------------------------------------------------

    public void closeLambdaDeclaration() {

        this.addPolish(this.functions.getLast().getName());
        this.addPolish("close-lambda");
    }

    // --------------------------------------------------------------------------------------------

    public void discardLambdaDeclaration() {

        this.functions.removeLast();
    }

    // --------------------------------------------------------------------------------------------

    public void startLambdaCall() {

        // Las funciones lambda siempre se invocan inmediatamente después de ser
        // declaradas, y no pueden contener declaraciones de funciones dentro, por lo
        // que será la última función en la lista de funciones.
        this.functionCalled = this.functions.getLast();
        this.addPolish(functionCalled.getName());
    }

    // --------------------------------------------------------------------------------------------

    public void closeLambdaCall() {

        // Se elimina el nombre de la función, que se utilizó como delimitador.
        // TODO: ¿QUÉ PASA CON FUNCIÓN QUE SE LLAMA PASÁNDOLE A SÍ MISMA UN RESULTADO DE
        // SU PROPIA FUNCIÓN?
        // DEBE CAMBIARSE EL DELIMITADOR.
        this.removeLastPolish();

        List<String> polishesGenerated = this.functionCalled.closeLambdaCall(this, "->");

        for (String polish : polishesGenerated) {
            this.addPolish(polish);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void discardLambdaCall() {

        this.discardFunctionCall();
    }

    // --------------------------------------------------------------------------------------------
    // Manejo de Loops
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
    // Manejo de Estado Seguro
    // --------------------------------------------------------------------------------------------

    public void recordSafeState() {
        this.lastSafeState = this.elements.size();
        this.lastPolishNumber = this.polishNumber;

        if (this.debug) {
            System.out.println("Safe state recorded in: " + this.lastSafeState);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void returnToLastSafeState() {
        // Se eliminan todos los elementos agregados luego del último estado seguro.
        elements.subList(lastSafeState, elements.size()).clear();
        this.polishNumber = this.lastPolishNumber;

        if (this.debug) {
            System.out.println("The reverse polished was restored to last safe state: " + this.lastSafeState);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void print() {

        Printer.printSeparator();
        Printer.printCentered("Polaca Inversa");
        Printer.printSeparator();

        int polishNumber = 0;
        for (Element element : this.elements) {

            List<String> separations = this.separations.get(polishNumber);
            if (separations != null) {

                for (String separation : separations) {

                    Printer.printSeparator();
                    Printer.printCentered(separation);
                    Printer.printSeparator();
                }
            }
            polishNumber++;

            if (element != null) {
                element.print();
            } else {
                Printer.printWrapped("null");
            }
        }

        // Separaciones de cierre.
        List<String> separations = this.separations.get(polishNumber);
        if (separations != null) {

            for (String separation : separations) {

                Printer.printSeparator();
                Printer.printCentered(separation);
                Printer.printSeparator();
            }
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
}
