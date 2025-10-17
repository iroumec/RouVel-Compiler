package semantic;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList; 

public final class ReversePolish {

    private static final ReversePolish INSTANCE = new ReversePolish();

    //private List<Polish> polishes;
    private ArrayList<String> polishes;

    private Stack<Integer> stackedBifurcation;

    private ReversePolish() {
        this.polishes = new ArrayList<>();
        this.stackedBifurcation = new Stack<>();
    }

    public static ReversePolish getInstance() {
        return INSTANCE;
    }

    public void addPolish(String symbol) {
        //this.polishes.add(new Polish(symbol));
        this.polishes.add(symbol);
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();
        int nroPolaca = 1;
        //for (Polish polish : this.polishes) {
        for (String polish : this.polishes) {
            out.append(nroPolaca + " ").append(polish).append('\n');
            nroPolaca++;
        }

        return out.toString();
    }

    public void addFalseBifurcation() {
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("FB");
    }

    public void addInconditionalBifurcation() {
        int incompletePolish = this.stackedBifurcation.pop()-1;
        this.polishes.add(" ");
        this.stackedBifurcation.push(this.polishes.size());
        this.polishes.add("IB");
        this.polishes.set(incompletePolish,Integer.toString(this.polishes.size()+1));
    }

    public void completeSelection() {
        this.polishes.set(this.stackedBifurcation.pop()-1,Integer.toString(this.polishes.size()+1));
    }

    public void registerDoBody() {
        this.stackedBifurcation.push(this.polishes.size()+1);
    }

    public void addTrueBifurcation() {
        this.stackedBifurcation.pop();
        this.polishes.set(this.polishes.size()-2,Integer.toString(this.stackedBifurcation.pop()));
        this.polishes.set(this.polishes.size()-1,"TB");
    }

}
