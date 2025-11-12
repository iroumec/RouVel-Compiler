package assembler.operators.implementations;

import java.util.Deque;
import java.util.List;

import assembler.operators.Operator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

public class Label implements Operator {

    private Label() {
    }

    private static class Holder {
        private static final Label INSTANCE = new Label();
    }

    public static Label getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        StringBuilder code = new StringBuilder();

        List<Symbol> globales = SymbolTable.getInstance().get("MAIN", SymbolCategory.VARIABLE);

        for (Symbol symbol : globales) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("(local $%s i32)%n", symbol.getLexemaWithoutScope()));
        }

        return code.append("\n").toString();
    }

}
