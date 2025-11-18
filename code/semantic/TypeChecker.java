package semantic;

import java.math.BigDecimal;

import common.Symbol;
import common.Monitor;
import common.SymbolTable;
import common.SymbolCategory;

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
                && dividend.getValue().compareTo(BigDecimal.ZERO) == 0;
    }

    private static void notifyError(String errorMessage) {

        Monitor monitor = Monitor.getInstance();

        monitor.addError(String.format(
                "ERROR SEMÁNTICO: Línea %d: %s",
                monitor.getLineNumber(), errorMessage));
    }
}
