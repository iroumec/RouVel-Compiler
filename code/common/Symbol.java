package common;

import java.math.BigDecimal;
import java.util.Arrays;

public class Symbol {

    // --------------------------------------------------------------------------------------------

    private static int stringCounter = 0;

    // --------------------------------------------------------------------------------------------

    private int references;
    private SymbolType type;
    private StringBuilder value; // Valor real
    private StringBuilder lexema; // Lexema.
    private SymbolCategory category;

    // --------------------------------------------------------------------------------------------

    public static Symbol createNewString(String lexema) {

        String finalLexema = lexema;

        if (!lexema.startsWith("\"")) {
            finalLexema = "\"" + finalLexema;
        }

        if (!lexema.endsWith("\"")) {
            finalLexema = finalLexema + "\"";
        }

        Symbol symbol = new SymbolBuilder(finalLexema).value(String.valueOf(stringCounter))
                .category(SymbolCategory.CONSTANT)
                .type(SymbolType.STRING).build();

        // Se incrementa el string counter de acuerdo al número de caracteres
        // en el string que se creo. Esto es útil para el assembler, para saber dónde
        // comenzar a gaurdar cada String y que estos no se pisen.
        stringCounter += finalLexema.length() - 2;

        return symbol;
    }

    // --------------------------------------------------------------------------------------------

    public static Symbol createNewUint(String value) {

        Symbol symbol = new SymbolBuilder(value + "UI").value(value).category(SymbolCategory.CONSTANT)
                .type(SymbolType.UINT).build();

        return symbol;
    }

    // --------------------------------------------------------------------------------------------

    public static Symbol createNewFloat(String value) {

        Symbol symbol = new SymbolBuilder(value).value(value).category(SymbolCategory.CONSTANT)
                .type(SymbolType.FLOAT).build();

        return symbol;
    }

    // --------------------------------------------------------------------------------------------

    Symbol(StringBuilder lexema, StringBuilder value, SymbolCategory category, SymbolType type, int references) {
        this.type = type;
        this.value = value;
        this.lexema = lexema;
        this.category = category;
        this.references = references;
    }

    // --------------------------------------------------------------------------------------------

    private Symbol(String lexema, String value, int references) {
        this.lexema = new StringBuilder(lexema);
        this.value = new StringBuilder(value);
        this.references = references;
    }

    // --------------------------------------------------------------------------------------------

    public Symbol(String lexema, String value, SymbolType type) {
        this.lexema = new StringBuilder(lexema);
        this.value = new StringBuilder(value);
        this.type = type;

        if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
            this.type = SymbolType.STRING;
        }

        if (this.type != null) {
            this.category = SymbolCategory.CONSTANT;
        }

        this.references = 0;
    }

    // --------------------------------------------------------------------------------------------

    public Symbol(String lexema, String value, SymbolCategory category, SymbolType type) {

        this.lexema = new StringBuilder(lexema);
        this.value = new StringBuilder(value);
        this.type = type;
        this.category = category;

        this.references = 0;
    }

    // --------------------------------------------------------------------------------------------

    public Symbol(String lexema, SymbolCategory category, SymbolType type) {

        if (type == SymbolType.STRING) {
            this.setValue(String.valueOf(stringCounter++));
        }

        this.type = type;
        this.category = category;

        this.references = 0;
    }

    // --------------------------------------------------------------------------------------------

    int getReferences() {
        return this.references;
    }

    // --------------------------------------------------------------------------------------------

    void incrementarReferencias() {
        this.references++;
    }

    // --------------------------------------------------------------------------------------------

    void decrementarReferencias() {
        this.references--;
    }

    // --------------------------------------------------------------------------------------------

    boolean hasNoReferences() {
        return references == 0;
    }

    // --------------------------------------------------------------------------------------------

    public BigDecimal getValueAsBigDecimal() {
        return new BigDecimal(this.value.toString());
    }

    // --------------------------------------------------------------------------------------------

    public String getLexema() {
        return this.lexema.toString();
    }

    // --------------------------------------------------------------------------------------------

    public String getValue() {
        return this.value.toString();
    }

    // --------------------------------------------------------------------------------------------

    public SymbolType getType() {
        return this.type == null ? null : this.type;
    }

    // --------------------------------------------------------------------------------------------

    void setType(SymbolType newType) {
        this.type = newType;
    }

    // --------------------------------------------------------------------------------------------

    void setCategory(SymbolCategory newCategory) {
        this.category = newCategory;
    }

    // --------------------------------------------------------------------------------------------

    public boolean isType(SymbolType type) {

        if (this.type == null) {
            return false;
        }

        return this.type.equals(type);
    }

    // --------------------------------------------------------------------------------------------

    public String getLexemaWithoutScope() {
        return this.lexema.toString().split(":")[0];
    }

    // --------------------------------------------------------------------------------------------

    public String getScope() {
        String[] parts = this.lexema.toString().split(":");
        if (parts.length <= 1) {
            return null;
        }
        return String.join(":", Arrays.copyOfRange(parts, 1, parts.length));
    }

    // --------------------------------------------------------------------------------------------

    SymbolCategory getCategory() {
        return this.category;
    }

    // --------------------------------------------------------------------------------------------

    public boolean isCategory(SymbolCategory category) {

        if (this.category == null) {
            return false;
        }

        return this.category.equals(category);
    }

    // --------------------------------------------------------------------------------------------

    boolean isEmpty() {
        return lexema.length() == 0 && value.length() == 0 && type.length() == 0;
    }

    // --------------------------------------------------------------------------------------------

    void setLexema(String lexema) {
        this.lexema.setLength(0);
        this.lexema.append(lexema);
    }

    // --------------------------------------------------------------------------------------------

    void setValue(String value) {

        if (this.value == null) {
            this.value = new StringBuilder(value);
        } else {
            this.value.setLength(0);
            this.value.append(value);
        }
    }

    // --------------------------------------------------------------------------------------------

    Symbol getNegative() {
        // TODO: únicamente debe poderse obtener el negativo de constantes numéricas.
        return new Symbol("-" + lexema, "-" + value, references);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return this.lexema.toString();
    }
}