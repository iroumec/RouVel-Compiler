package common;

import java.util.Map;

import utilities.Printer;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public final class SymbolTable {

    private static final SymbolTable INSTANCE = new SymbolTable();

    // ============================================================================================

    /**
     * Se utiliza el "LinkedHashMap" para que el agregado de los símbolos siga un
     * orden. Esto facilita la detección de parámetros y de una función y demás
     * cosas.
     */
    private final Map<String, Symbol> symbolTable = new LinkedHashMap<>();

    // ============================================================================================

    /**
     * Constructor.
     */
    private SymbolTable() {
    }

    // ============================================================================================

    /**
     * 
     * @return Una instancia de la tabla de símbolos.
     */
    public static SymbolTable getInstance() {
        return INSTANCE;
    }

    // ============================================================================================

    /**
     * Agrega un lexema a la tabla si no existe. Incrementa su referencia.
     */
    public void addEntry(Symbol newSymbol) {

        Symbol symbol = this.symbolTable.get(newSymbol.getLexema());

        if (symbol == null) {
            symbolTable.put(newSymbol.getLexema(), newSymbol);
            newSymbol.incrementarReferencias();
        } else {
            symbol.incrementarReferencias();
        }
    }

    // ============================================================================================

    public boolean isSymbol(String lexema, SymbolCategory category) {

        Symbol symbol = this.symbolTable.get(lexema);

        return symbol != null && symbol.isCategory(category);
    }

    // ============================================================================================

    public void removeEntry(String lexema) {
        Symbol symbol = this.symbolTable.get(lexema);
        this.decreaseReferences(lexema, symbol);
    }

    // ============================================================================================

    /**
     * Remplaza una entrada en la tabla por otra.
     * 
     * @param oldLexema Entrada a remplazar.
     * @param newLexema Entrada por la que se hará el remplazo.
     */
    public void replaceEntry(String oldLexema, String newLexema) {

        if (oldLexema == null || newLexema == null) {
            return;
        } else {
            Symbol symbol = this.symbolTable.get(oldLexema);
            this.decreaseReferences(oldLexema, symbol);

            Symbol newSymbol = symbol.getCopy(newLexema);

            this.addEntry(newSymbol);
        }
    }

    // ============================================================================================

    /**
     * Remplaza una entrada de la tabla de símbolos por su versión negativa.
     * 
     * @param lexema Lexema cuya entrada será remplazada.
     */
    public void switchEntrySign(String lexema) {

        Symbol entry = symbolTable.get(lexema);

        // Se decrementan las referencias del símbolo anterior.
        this.decreaseReferences(lexema, entry);

        // Alta de la tabla de símbolos.
        this.addEntry(entry.getNegative());
    }

    // ============================================================================================

    public String getProgramName() {

        return SymbolTable.getInstance().get("", SymbolCategory.PROGRAM).getFirst().getLexema();
    }

    // ============================================================================================

    /**
     * If the scope is null, it will return all global variables.
     * 
     * @param scope
     * @param category
     * @return
     */
    public List<Symbol> get(String scope, SymbolCategory category) {

        List<Symbol> out = new ArrayList<>();

        for (Symbol symbol : this.symbolTable.values()) {

            if (symbol.isCategory(category)
                    && symbol.getScope().equals(scope)) {
                out.add(symbol);
            }
        }

        return out;
    }

    // ============================================================================================

    /**
     * @param scope    Únicamente se buscan las variables previas a la declaración
     *                 de la función.
     * @param category
     * @return Una lista con las variables que pueden llegarse a usarse con
     *         prefijado.
     */
    public List<Symbol> getOutOfScopeVariables(Symbol function) {

        List<Symbol> out = new ArrayList<>();

        Iterator<Symbol> iterator = this.symbolTable.values().iterator();

        boolean search = true;
        while (search && iterator.hasNext()) {

            Symbol symbol = iterator.next();

            if (symbol.isCategory(SymbolCategory.FUNCTION)
                    && symbol.getLexema().equals(function.getLexema())) {

                // De llegar a la declaración de función, la búsqueda se detiene.
                // Las variables declaradas luego no deben ser pasadas.
                search = false;

            } else {

                // No se consideran variables auxiliares. Estas no pueden ser accedidas fuera
                // del ámbito de la función.
                if ((symbol.isCategory(SymbolCategory.VARIABLE) && !symbol.getLexema().startsWith("aux") ||
                        symbol.isCategory(SymbolCategory.CV_PARAMETER) ||
                        symbol.isCategory(SymbolCategory.CVR_PARAMETER))
                        && function.getScope().contains(symbol.getScope())
                        && !symbol.getScope().equals(function.getScope() + ":" + function.getLexemaWithoutScope())) {
                    // Esta condición creo que no es necesaria.

                    out.add(symbol);
                }
            }
        }

        return out;
    }

    // ============================================================================================

    /**
     * @param scope    Únicamente se buscan las variables previas a la declaración
     *                 de la función.
     * @param category
     * @return Una lista con las variables que pueden llegarse a usarse con
     *         prefijado.
     */
    public List<Symbol> getLocalVariablesOfUntil(Symbol ofFunction, Symbol untilFunction) {

        List<Symbol> out = new ArrayList<>();

        Iterator<Symbol> iterator = this.symbolTable.values().iterator();

        boolean search = true;
        while (search && iterator.hasNext()) {

            Symbol symbol = iterator.next();

            if (symbol.isCategory(SymbolCategory.FUNCTION)
                    && symbol.getLexema().equals(untilFunction.getLexema())) {

                // De llegar a la declaración de función, la búsqueda se detiene.
                // Las variables declaradas luego no deben ser pasadas.
                search = false;

            } else {

                // No se consideran variables auxiliares. Estas no pueden ser accedidas fuera
                // del ámbito de la función.
                if (((symbol.isCategory(SymbolCategory.VARIABLE) && !symbol.getLexema().startsWith("aux")) ||
                        symbol.isCategory(SymbolCategory.CVR_PARAMETER) ||
                        symbol.isCategory(SymbolCategory.CV_PARAMETER))
                        && symbol.getScope().equals(ofFunction.getScope() + ":" + ofFunction.getLexemaWithoutScope())) {
                    // Esta condición creo que no es necesaria.

                    out.add(symbol);
                }
            }
        }

        return out;
    }

    // ============================================================================================

    // Devuelve el lexema del símbolo tal como aparece en la tabla de símbolos
    public String getSymbolTableLexema(String scope) {

        int lastSeparatorIndex = scope.lastIndexOf(':');

        if (lastSeparatorIndex != -1) {
            // La función en la tabla de símbolos es nombreFunc : restoScope
            String prefix = scope.substring(0, lastSeparatorIndex);
            String lastElement = scope.substring(lastSeparatorIndex + 1);
            return lastElement + ":" + prefix;
        } else {
            // En este caso, el scope no tiene ':' y coincide con el nombre de programa.
            return scope;
        }
    }

    // ============================================================================================

    public Symbol getFunctionSymbol(String scope) {

        Iterator<Symbol> iterator = this.symbolTable.values().iterator();

        boolean found = false;
        Symbol funcionSymbol = null;

        // Entrada de la tabla de símbolos que se busca
        String symbolTableEntry = getSymbolTableLexema(scope);

        while (!found && iterator.hasNext()) {

            Symbol symbol = iterator.next();

            if ((symbol.isCategory(SymbolCategory.FUNCTION) || symbol.isCategory(SymbolCategory.PROGRAM)) &&
                    symbol.getLexema().equals(symbolTableEntry)) {

                funcionSymbol = symbol;
                found = true;
            }
        }

        return funcionSymbol;
    }

    // ============================================================================================

    public boolean existsFunction(String lexema) {
        Iterator<Symbol> iterator = this.symbolTable.values().iterator();

        boolean found = false;

        // Entrada de la tabla de símbolos que se busca
        String entryBegining = lexema + ":";

        while (!found && iterator.hasNext()) {

            Symbol symbol = iterator.next();

            // No hace falta contemplar PROGRAM ya que nunca se llama a esta función en un escenario donde eso es necesario.
            if (symbol.isCategory(SymbolCategory.FUNCTION) && symbol.getLexema().startsWith(entryBegining)) {

                found = true;
            }
        }

        return found;
    }

    // ============================================================================================

    public List<Symbol> getStrings() {

        List<Symbol> out = new ArrayList<>();

        for (Symbol symbol : this.symbolTable.values()) {

            if (symbol.isType(SymbolType.STRING)) {
                out.add(symbol);
            }
        }

        return out;
    }

    // ============================================================================================

    public List<Symbol> getParameters(String scope) {

        List<Symbol> out = new ArrayList<>();

        for (Symbol symbol : this.symbolTable.values()) {

            if ((symbol.isCategory(SymbolCategory.CV_PARAMETER)
                    || symbol.isCategory(SymbolCategory.CVR_PARAMETER))
                    && symbol.getScope().equals(scope)) {
                out.add(symbol);
            }
        }

        return out;
    }

    // ============================================================================================

    /**
     * Se decrementa, de no ser nulo, la cantidad de referencias del símbolo.
     * Si el número de referencias del símbolo llega a cero, se elimina de la tabla.
     * 
     * @param lexema Lexema del símbolo a decrementar sus referencias. Útil para
     *               mostrar mensajes de error en caso de que el símbolo sea nulo.
     * @param symbol Símbolo cuyas referencias serán decrementadas.
     */
    private void decreaseReferences(String lexema, Symbol symbol) {

        if (symbol != null) {
            symbol.decrementarReferencias();

            if (symbol.hasNoReferences()) {
                this.symbolTable.remove(symbol.getLexema());
            }
        } else {
            Printer.printWrapped(String.format(
                    "Error inesperado. Método \"decreaseReferences()\". Linea %s. Se intentó decrementar la referencia del lexema \"%s\", que no existe.",
                    Monitor.getInstance().getLineNumber(), lexema));
        }

    }

    // ============================================================================================

    public Symbol getSymbol(String lexema) {

        Symbol symbol = symbolTable.get(lexema);
        if (symbol == null) {
            Printer.printWrapped(String.format(
                    "Error inesperado. Método \"getSymbol()\". Linea %s. Se intentó acceder al símbolo del lexema \"%s\", que no existe.",
                    Monitor.getInstance().getLineNumber(), lexema));
        }
        return symbol;
    }

    // ============================================================================================

    public boolean entryExists(String entry) {
        return this.symbolTable.containsKey(entry);
    }

    // ============================================================================================

    public void print() {
        SymbolTablePrinter.getInstance().print(this.symbolTable.values());
    }

}
