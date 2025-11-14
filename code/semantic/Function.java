package semantic;

import java.beans.Expression;
import java.lang.foreign.SymbolLookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.Monitor;
import common.SymbolTable;

class Function {

    private String name;

    private List<Argument> arguments;
    private List<Parameter> parameters;

    Function(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public List<String> addParameter(String id, String type, String semantic) {

        this.parameters.add(new Parameter(id, type, semantic));

        List<String> out = new ArrayList<>();

        out.add(id + ":" + this.name);
        out.add("parameter");

        return out;
    }

    public void addArgument(String parameter, List<String> expression) {
        this.arguments.add(new Argument(parameter, expression));
    }

    public List<String> closeDeclaration() {

        List<String> out = new ArrayList<>();

        for (Parameter parameter : this.parameters) {

            if (parameter.getSemantic() == "CVR") {

                String formalParameter = parameter.getID() + ":" + this.name;

                // Se añaden de forma inversa para simplificar la asignación a los argumentos.
                out.addFirst("result");
                out.addFirst(formalParameter);
            }
        }

        return out;
    }

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

    List<String> closeCall(ReversePolish polish, String operator) {

        List<Argument> orderedArguments = reorderArgumentsAccordingToParameters();

        List<String> out = new ArrayList<>();

        SymbolTable symbolTable = SymbolTable.getInstance();

        for (Argument argument : orderedArguments) {

            String formalParameter = argument.getParameter() + ":" + this.name;

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

                if (expressions.size() > 1) {
                    notifyError(
                            "El argumento no es un objeto referenciable y, por lo tanto, no es válido para pasaje por CVR.");
                } else {

                    String formalParameter = argument.getParameter() + ":" + this.name;

                    out.add(formalParameter);
                    out.add(expressions.getFirst());
                    out.add("<-");
                }
            } else {
                iteratorArguments.next();
            }
        }

        this.arguments.clear();

        return out;
    }

    String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o instanceof Function) {
            return ((Function) o).name.equals(this.name);
        }

        return false;
    }

    private class Parameter {

        private String id, type, semantic;

        private Parameter(String id, String type, String semantic) {

            this.id = id;
            this.type = type;
            this.semantic = semantic;
        }

        private String getID() {
            return this.id;
        }

        private String getSemantic() {
            return this.semantic;
        }
    }

    private class Argument {

        private String parameter;
        private List<String> expression;

        private Argument(String parameter, List<String> expression) {
            this.parameter = parameter;
            this.expression = new ArrayList<>(expression);
        }

        private String getParameter() {
            return this.parameter;
        }

        private List<String> getExpression() {
            return this.expression;
        }
    }

    private void notifyError(String errorMessage) {

        Monitor.getInstance().addError(String.format(
                "ERROR SEMÁNTICO: Línea %d: %s",
                Monitor.getInstance().getLineNumber(), errorMessage));
    }
}