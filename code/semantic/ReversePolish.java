package semantic;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

public class ReversePolish {

    private List<Polish> polishes;

    private Stack<Integer> stackedBifurcation;

    public ReversePolish() {
        this.polishes = new ArrayList<>();
        this.stackedBifurcation = new Stack<>();
    }

    public void addPolish(String symbol) {
        this.polishes.add(new Polish(symbol));
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();

        for (Polish polish : this.polishes) {
            out.append(polish).append('\n');
        }

        return out.toString();
    }

}
