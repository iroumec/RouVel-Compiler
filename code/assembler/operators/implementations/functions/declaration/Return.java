package assembler.operators.implementations.functions.declaration;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Return implements AssemblerOperator {

    private Return() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Return INSTANCE = new Return();
    }

    // --------------------------------------------------------------------------------------------

    public static Return getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol operand = SymbolTable.getInstance().getSymbol(repository.popOperand());

        // En WebAssembly, el retorno de una función es el valor en el tope de
        // la pila.

        String out;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format("i32.const %s %n", operand.getValue());
            } else {
                out = String.format("f32.const %s %n", Float.valueOf(operand.getValue()).toString());
                out += String.format("i32.trunc_f32_u %n");
            }
        } else {
            out = String.format("local.get $%s %n", operand.getLexemaWithoutScope());
        }

        // No es necesario hacer explícito el 'return', pero se incluyó
        // ya que se creé que aporta más legibilidad.
        out += "return \n";

        repository.addCode(out);
    }
}
