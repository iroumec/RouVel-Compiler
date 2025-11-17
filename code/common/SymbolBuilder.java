package common;

class SymbolBuilder {

    private int references;
    private SymbolType type;
    private StringBuilder value;
    private StringBuilder lexema;
    private SymbolCategory category;

    SymbolBuilder(String lexema) {
        this.lexema = new StringBuilder(lexema);
    }

    SymbolBuilder references(int references) {
        this.references = references;
        return this;
    }

    SymbolBuilder type(SymbolType type) {
        this.type = type;
        return this;
    }

    SymbolBuilder value(String value) {
        this.value = new StringBuilder(value);
        return this;
    }

    SymbolBuilder category(SymbolCategory category) {
        this.category = category;
        return this;
    }

    Symbol build() {
        return new Symbol(lexema, value, category, type, references);
    }
}
