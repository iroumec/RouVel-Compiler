package semantic;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;

import lexer.token.TokenType;

public final class ReversePolish {

    private static final ReversePolish INSTANCE = new ReversePolish();

    private List<String> polishes;
    private final List<String> temporalPolishes;
    private final Deque<Integer> stackedBifurcation;

    private ReversePolish() {
        this.polishes = new ArrayList<>();
        this.temporalPolishes = new ArrayList<>();
        this.stackedBifurcation = new ArrayDeque<>();
    }

    public static ReversePolish getInstance() {
        return INSTANCE;
    }

    public void addPolish(String symbol) {
        // this.polishes.add(new Polish(symbol));
        this.polishes.add(symbol);
    }

    public void addFalseBifurcation() {
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("FB");
    }

    public void addInconditionalBifurcation() {
        int incompletePolish = this.stackedBifurcation.pop() - 1;
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("IB");
        this.polishes.set(incompletePolish, Integer.toString(this.polishes.size() + 1));
    }

    public void completeSelection() {
        this.polishes.set(this.stackedBifurcation.pop() - 1, Integer.toString(this.polishes.size() + 1));
    }

    public void registerDoBody() {
        this.stackedBifurcation.push(this.polishes.size() + 1);
    }

    public void addTrueBifurcation() {
        this.stackedBifurcation.pop();
        this.polishes.set(this.polishes.size() - 2, Integer.toString(this.stackedBifurcation.pop()));
        this.polishes.set(this.polishes.size() - 1, "TB");
    }

    public void rearrangePairs() {
        int lastIndex = polishes.size() - 1;
        int i = lastIndex - 2;
        while (i >= 0 && TokenType.fromSymbol(polishes.get(i)) == TokenType.CTE)
            i--;
        int pairAmount = lastIndex - 2 - i + 1;
        int indexFirstVariable = lastIndex - (pairAmount * 2) + 1;

        String firstVariable = this.polishes.remove(lastIndex - 1);
        this.polishes.add(indexFirstVariable, firstVariable);

        ArrayList<String> multipleAsignation = new ArrayList<>(
                this.polishes.subList(indexFirstVariable, lastIndex + 1));
        List<String> constants = multipleAsignation.subList(pairAmount, pairAmount * 2);
        polishes.addAll(Collections.nCopies(pairAmount, null));

        for (int j = 0; j < pairAmount; j++) {
            polishes.set(j * 3 + indexFirstVariable, multipleAsignation.get(j));
            polishes.set(j * 3 + indexFirstVariable + 1, constants.get(j));
            polishes.set(j * 3 + indexFirstVariable + 2, ":=");
        }
    }

    /**
     * No siempre todos los factores deben agregarse a la polaca. Acá se guardarán
     * estos y, luego,
     * de ser necesarios, se añaden.
     * 
     */

    public void addTemporalPolish(String polish) {

        this.temporalPolishes.add(polish);
    }

    public void makeTemporalPolishesDefinitive() {

        for (String polish : this.temporalPolishes) {
            polishes.add(polish);
        }

        this.emptyTemporalPolishes();
    }

    // --------------------------------------------------------------------------------------------

    public void emptyTemporalPolishes() {
        this.temporalPolishes.clear();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();
        int nroPolaca = 1;
        // for (Polish polish : this.polishes) {
        for (String polish : this.polishes) {
            out.append(nroPolaca + " ").append(polish).append('\n');
            nroPolaca++;
        }

        return out.toString();
    }
}
