package common;

import java.math.BigDecimal;
import java.util.Arrays;

public class Symbol {

    // ============================================================================================
    // Attributes
    // ============================================================================================

    private int references;
    private SymbolType type;
    private BigDecimal value; // Valor real
    private StringBuilder lexema; // Lexema.
    private SymbolCategory category;

    // ============================================================================================
    // Constructors
    // ============================================================================================

    Symbol(StringBuilder lexema, BigDecimal value, SymbolCategory category, SymbolType type, int references) {
        this.type = type;
        this.value = value;
        this.lexema = lexema;
        this.category = category;
        this.references = references;
    }

    // ============================================================================================
    // References Managements
    // ============================================================================================

    void incrementarReferencias() {
        this.references++;
    }

    // ============================================================================================

    void decrementarReferencias() {

        this.references--;
    }

    // ============================================================================================

    boolean hasNoReferences() {
        return references == 0;
    }

    // ============================================================================================
    // Getters
    // ============================================================================================

    SymbolCategory getCategory() {
        return this.category;
    }

    // ============================================================================================

    SymbolType getType() {
        return this.type == null ? null : this.type;
    }

    // ============================================================================================

    int getReferences() {
        return this.references;
    }

    // ============================================================================================
    // Versionado
    // ============================================================================================

    /**
     * @param newLexema
     * @return Una copia del símbolo con el nuevo lexema especificado.
     */
    Symbol getCopy(String newLexema) {

        return new SymbolBuilder(newLexema).value(this.value).category(this.category)
                .type(this.type).build();

    }

    // ============================================================================================

    Symbol getNegative() {

        return new SymbolBuilder("-" + this.lexema).value(this.value.negate()).category(this.category)
                .type(this.type)
                .references(this.references).build();
    }

    // ============================================================================================
    // Public Methods
    // ============================================================================================

    public String getLexema() {
        return this.lexema.toString();
    }

    // ============================================================================================

    public String getLexemaWithoutScope() {
        return this.lexema.toString().split(":")[0];
    }

    // ============================================================================================

    public BigDecimal getValue() {

        return this.value;
    }

    // ============================================================================================

    public boolean isCategory(SymbolCategory category) {

        if (this.category == null) {
            return false;
        }

        return this.category.equals(category);
    }

    // ============================================================================================

    public boolean isType(SymbolType type) {

        if (this.type == null) {
            return false;
        }

        return this.type.equals(type);
    }

    // ============================================================================================

    public String getScope() {
        String[] parts = this.lexema.toString().split(":");
        if (parts.length <= 1) {
            return "";
        }
        return String.join(":", Arrays.copyOfRange(parts, 1, parts.length));
    }

    // ============================================================================================
    // Override
    // ============================================================================================

    @Override
    public String toString() {
        return this.lexema.toString();
    }
}