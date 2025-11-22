package common;

import java.math.BigDecimal;

class SymbolBuilder {

    private int references;
    private SymbolType type;
    private BigDecimal value;
    private StringBuilder lexema;
    private SymbolCategory category;

    // ============================================================================================

    SymbolBuilder(String lexema) {
        this.lexema = new StringBuilder(lexema);
    }

    // ============================================================================================

    SymbolBuilder references(int references) {
        this.references = references;
        return this;
    }

    // ============================================================================================

    SymbolBuilder type(SymbolType type) {
        this.type = type;
        return this;
    }

    // ============================================================================================

    SymbolBuilder value(BigDecimal value) {

        if (value != null) {
            this.value = new BigDecimal(value.toString());
        }
        return this;
    }

    // ============================================================================================

    SymbolBuilder category(SymbolCategory category) {
        this.category = category;
        return this;
    }

    // ============================================================================================

    Symbol build() {
        return new Symbol(lexema, value, category, type, references);
    }
}
