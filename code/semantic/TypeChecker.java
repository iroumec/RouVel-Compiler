package semantic;

import common.Monitor;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public final class TypeChecker {

    public static void checkDivisionByZero(String operator, String dividend) {

        if (operator == "/") {

            Symbol dividendSymbol = SymbolTable.getInstance().getSymbol(dividend);

            if (isDividendZero(dividendSymbol)) {

                notifyError("El dividendo no puede ser cero.");
            }
        }

    }

    private static boolean isDividendZero(Symbol dividend) {
        return dividend.isCategory(SymbolCategory.CONSTANT)
                && ((dividend.isType(SymbolType.UINT) && Integer.valueOf(dividend.getValue()) == 0)
                        || (dividend.isType(SymbolType.FLOAT) && Float.valueOf(dividend.getValue()) == 0.0f));
    }

    private static void notifyError(String errorMessage) {

        Monitor monitor = Monitor.getInstance();

        monitor.addError(String.format(
                "ERROR SEMÁNTICO: Línea %d: %s",
                monitor.getLineNumber(), errorMessage));
    }
}
