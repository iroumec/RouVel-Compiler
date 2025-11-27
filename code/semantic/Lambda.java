package semantic;

import java.util.ArrayList;
import java.util.List;

final class Lambda extends Function {

    // ============================================================================================
    // Constructor
    // ============================================================================================

    Lambda(String name) {
        super(name);
    }

    // ============================================================================================
    // Generación de Polacas: Cierre de Invocación
    // ============================================================================================

    @Override
    List<String> closeCall(ReversePolish polish, String operator) {

        List<String> out = new ArrayList<>();

        for (Argument argument : this.arguments) {

            out.addAll(argument.getExpression());
            out.add(argument.getParameter());
            out.add(operator);
        }

        out.add(this.getName());
        out.add("call");

        this.arguments.clear();

        return out;
    }

}
