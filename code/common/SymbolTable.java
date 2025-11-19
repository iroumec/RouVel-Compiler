package common;

import java.util.Map;
import utilities.Printer;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class SymbolTable {

    private static final SymbolTable INSTANCE = new SymbolTable();

    // --------------------------------------------------------------------------------------------

    /**
     * Se utiliza el "LinkedHashMap" para que el agregado de los símbolos siga un
     * orden. Esto facilita la detección de parámetros y de una función y demás
     * cosas.
     */
    private final Map<String, Symbol> symbolTable = new LinkedHashMap<>();

    // --------------------------------------------------------------------------------------------

    /**
     * Constructor.
     */
    private SymbolTable() {
    }

    // --------------------------------------------------------------------------------------------

    /**
     * 
     * @return Una instancia de la tabla de símbolos.
     */
    public static SymbolTable getInstance() {
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------

    public boolean isSymbol(String lexema, SymbolCategory category) {

        Symbol symbol = this.symbolTable.get(lexema);

        return symbol != null && symbol.isCategory(category);
    }

    // --------------------------------------------------------------------------------------------

    public void removeEntry(String lexema) {
        Symbol symbol = this.symbolTable.get(lexema);
        this.decreaseReferences(lexema, symbol);
    }

    // --------------------------------------------------------------------------------------------

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

            Symbol newSymbol = symbol.getCopy();
            newSymbol.setLexema(newLexema);

            this.addEntry(newSymbol);
        }
    }

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------

    /**
     * Modifica el tipo de un símbolo.
     * 
     * @param lexema Lexema que mapea al símbolo.
     * @param type   Nuevo tipo del símbolo.
     */
    public void setType(String lexema, SymbolType type) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setType(type);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void setValue(String lexema, String value) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setValue(value);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void setCategory(String lexema, SymbolCategory category) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setCategory(category);
        }
    }

    // --------------------------------------------------------------------------------------------

    public String getProgramName() {

        return SymbolTable.getInstance().get("", SymbolCategory.PROGRAM).getFirst().getLexema();
    }

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------

    public List<Symbol> getStrings() {

        List<Symbol> out = new ArrayList<>();

        for (Symbol symbol : this.symbolTable.values()) {

            if (symbol.isType(SymbolType.STRING)) {
                out.add(symbol);
            }
        }

        return out;
    }

    // --------------------------------------------------------------------------------------------

    public void setScope(String lexema, String scope) {

        String newLexema = lexema + ":" + scope;
        Symbol symbol = this.symbolTable.get(lexema);

        // Se decrementan las referencias de la entrada sin ámbito.
        this.decreaseReferences(lexema, symbol);

        symbol.setLexema(newLexema);
        addEntry(symbol);

    }

    // --------------------------------------------------------------------------------------------

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
                    "Error inesperado. Se intentó decrementar la referencia del lexema \"%s\", que no existe.",
                    lexema));
        }

    }

    // --------------------------------------------------------------------------------------------

    public Symbol getSymbol(String lexema) {

        Symbol symbol = symbolTable.get(lexema);
        if (symbol == null) {
            Printer.printWrapped(String.format(
                    "Error inesperado. Se intentó obtener el símbolo asociado al lexema \"%s\", que no existe.",
                    lexema));
        }
        return symbol;
    }

    // --------------------------------------------------------------------------------------------

    public boolean entryExists(String entry) {
        return this.symbolTable.containsKey(entry);
    }

    // --------------------------------------------------------------------------------------------

    public void print() {
        SymbolTablePrinter.getInstance().print(this.symbolTable.values());
    }

}
