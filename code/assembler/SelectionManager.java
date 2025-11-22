package assembler;

import java.util.Stack;

public class SelectionManager {

    // -----------------------------------------------------------------------------------------

    private static SelectionManager INSTANCE;

    private SelectionManager() {}

    public static SelectionManager getInstance() {
        if (INSTANCE == null) 
            INSTANCE = new SelectionManager();
        return INSTANCE;
    }

    // -----------------------------------------------------------------------------------------

    private int selectionLevel = 0;
    private int outAmount = 0;
    private int thenAmount = 0;
    private int elseAmount = 0;
    private Stack<Integer> levelStack = new Stack<>();

    public void increaseSelectionLevel() {
        this.selectionLevel++;
    }

    public void decreaseSelectionLevel() {
        this.selectionLevel--;
    }

    public int getSelectionLevel() {
        return this.selectionLevel;
    }

    public int obtainOutValue() {
        return ++this.outAmount;
    }

    public int getOutValue() {
        return this.outAmount;
    }

    public int obtainThenValue() {
        return ++this.thenAmount;
    }

    public int getThenValue() {
        return this.thenAmount;
    }

    public int obtainElseValue() {
        return ++this.elseAmount;
    }

    public int getElseValue() {
        return this.elseAmount;
    }

    public void pushSelectionLevel(int level) {
        this.levelStack.push(level);
    }

    public void decreaseClosers() {
        int closers = this.levelStack.pop();
        closers--;
        this.levelStack.push(closers);
    }

    public int popLevel() {
        return this.levelStack.pop();
    }

}
