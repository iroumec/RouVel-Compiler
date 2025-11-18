package assembler;

public class SelectionManager {

    private static SelectionManager INSTANCE;

    private SelectionManager() {}

    public static SelectionManager getInstance() {
        if (INSTANCE == null) 
            INSTANCE = new SelectionManager();
        return INSTANCE;
    }

    private int selectionLevel = 0;
    private int outAmount = 0;
    private int thenAmount = 0;
    private int elseAmount = 0;
    private int closers = 0;

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
        increaseClosers();
        return ++this.outAmount;
    }

    public int getOutValue() {
        return this.outAmount;
    }

    public int obtainThenValue() {
        increaseClosers();
        return ++this.thenAmount;
    }

    public int getThenValue() {
        return this.thenAmount;
    }

    public int obtainElseValue() {
        increaseClosers();
        return ++this.elseAmount;
    }

    public int getElseValue() {
        return this.elseAmount;
    }

    public int getClosers() {
        return this.closers;
    }

    public void increaseClosers() {
        this.closers++;
    }

    public void decreaseClosers() {
        this.closers--;
    }

}
