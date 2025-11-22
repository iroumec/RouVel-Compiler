package assembler.operators.implementations;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Print implements AssemblerOperator {

    private Print() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Print INSTANCE = new Print();
    }

    // --------------------------------------------------------------------------------------------

    public static Print getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol operand = SymbolTable.getInstance().getSymbol(repository.popOperand());

        String code;
        String importCode;

        if (operand.isType(SymbolType.STRING)) {
            code = String.format("i32.const %s %n", operand.getValue());
            code += String.format("i32.const %d %n", operand.getLexema().length() - 2); // -2 ya que se restan las
                                                                                        // comillas.
            code += String.format("call $console_log_string %n");

            importCode = "(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))";
        } else if (operand.isCategory(SymbolCategory.CONSTANT)) {

            if (operand.isType(SymbolType.UINT)) {
                code = String.format("i32.const %s %n", operand.getValue());
                code += String.format("call $console_log_i32 %n");

                importCode = "(import \"console\" \"log_i32\" (func $console_log_i32 (param i32)))";

            } else { // Flotante.

                code = String.format("f32.const %s %n", operand.getValue());
                code += String.format("call $console_log_f32 %n");

                importCode = "(import \"console\" \"log_f32\" (func $console_log_f32 (param i32)))";
            }
        } else { // Es variable.
            code = String.format("local.get $%s %n", operand.getLexemaWithoutScope());
            code += String.format("call $console_log_i32 %n");

            importCode = "(import \"console\" \"log_i32\" (func $console_log_i32 (param i32)))";
        }

        repository.addCode(code);
        repository.addCode("\n");
        repository.addImport(importCode);
    }

}
