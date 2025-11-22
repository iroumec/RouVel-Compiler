package semantic;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.stream.Collectors;

import common.Monitor;
import common.SymbolCategory;
import common.SymbolTable;

class Function {

    // ============================================================================================
    // Atributos
    // ============================================================================================

    private String name;
    private String scope;

    // ============================================================================================

    protected List<Argument> arguments;
    protected List<Parameter> parameters;

    // ============================================================================================
    // Constructor
    // ============================================================================================

    Function(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
        this.parameters = new ArrayList<>();

        String[] parts = name.split("\\s*:\\s*");
        this.scope = this.name;

        // Se pasa el nombre de la función al final.
        // Si se tiene A:B:C:D, se obtiene B:C:D.

        if (parts.length > 1) {
            String result = String.join(":",
                    Arrays.copyOfRange(parts, 1, parts.length)) + ":" + parts[0];
            this.scope = result;
        }
    }

    // ============================================================================================
    // Añadido de Parámetros
    // ============================================================================================

    void addParameter(String id, String semantic) {

        this.parameters.add(new Parameter(id, semantic));
    }

    // ============================================================================================
    // Añadido de Argumentos
    // ============================================================================================

    void addArgument(String parameter, List<String> expression) {

        this.arguments.add(new Argument(parameter, expression));
    }

    // ============================================================================================
    // Generación de Polacas: Cierre de la Declaración
    // ============================================================================================

    List<String> closeDeclaration() {

        List<String> out = new ArrayList<>();

        for (Parameter parameter : this.parameters) {

            if (parameter.getSemantic() == "CVR") {

                String formalParameter = parameter.getID() + ":" + this.scope;

                // Se añaden de forma inversa para simplificar la asignación a los argumentos.
                out.addFirst("result");
                out.addFirst(formalParameter);
            }
        }

        return out;
    }

    // ============================================================================================

    private List<Argument> reorderArgumentsAccordingToParameters() {

        // Índice por reference para acceso O(1).
        Map<String, Argument> indexArgument = this.arguments.stream()
                .collect(Collectors.toMap(Argument::getParameter, argument -> argument));

        // Reconstruís B en el orden de A
        List<Argument> orderedArguments = this.parameters.stream()
                .map(parameter -> indexArgument.get(parameter.getID()))
                .collect(Collectors.toList());

        return orderedArguments;
    }

    // ============================================================================================
    // Generación de Polacas: Cierre de Invocación
    // ============================================================================================

    List<String> closeCall(ReversePolish polish, String operator) {

        List<Argument> orderedArguments = reorderArgumentsAccordingToParameters();

        List<String> out = new ArrayList<>();

        SymbolTable symbolTable = SymbolTable.getInstance();

        for (Argument argument : orderedArguments) {

            String formalParameter = argument.getParameter() + ":" + this.scope;

            out.addAll(argument.getExpression());
            out.add(formalParameter);
            out.add(operator);

            symbolTable.replaceEntry(argument.getParameter(), formalParameter);
        }

        out.add(this.name);
        out.add("call");

        Iterator<Parameter> iteratorParameter = parameters.iterator();
        Iterator<Argument> iteratorArguments = orderedArguments.iterator();

        while (iteratorParameter.hasNext() && iteratorArguments.hasNext()) {

            Parameter parameter = iteratorParameter.next();

            if (parameter.getSemantic() == "CVR") {
                Argument argument = iteratorArguments.next();

                List<String> expressions = argument.getExpression();

                if (expressions.size() > 1
                        || !symbolTable.isSymbol(argument.getExpression().getFirst(), SymbolCategory.VARIABLE)) {
                    notifyError(
                            "El argumento no es un objeto referenciable y, por lo tanto, no es válido para pasaje por CVR.");
                } else {

                    String formalParameter = argument.getParameter() + ":" + this.scope;

                    out.add(formalParameter);
                    out.add(expressions.getFirst());
                    out.add("<-");
                }
            } else {
                iteratorArguments.next();
            }
        }

        this.arguments.clear();

        out.add("read-return");

        return out;
    }

    // ============================================================================================
    // Getters
    // ============================================================================================

    String getName() {
        return this.name;
    }

    // ============================================================================================
    // Manejo de Errores
    // ============================================================================================

    private void notifyError(String errorMessage) {

        Monitor monitor = Monitor.getInstance();

        monitor.addError(String.format(
                "ERROR SEMÁNTICO: Línea %d: %s",
                monitor.getLineNumber(), errorMessage));
    }

    // ============================================================================================
    // Inner Classes
    // ============================================================================================

    protected class Parameter {

        private String id, semantic;

        private Parameter(String id, String semantic) {

            this.id = id;
            this.semantic = semantic;
        }

        private String getID() {
            return this.id;
        }

        private String getSemantic() {
            return this.semantic;
        }
    }

    // ============================================================================================

    protected class Argument {

        private String parameter;
        private List<String> expression;

        private Argument(String parameter, List<String> expression) {
            this.parameter = parameter;
            this.expression = new ArrayList<>(expression);
        }

        protected String getParameter() {
            return this.parameter;
        }

        protected List<String> getExpression() {
            return this.expression;
        }
    }
}