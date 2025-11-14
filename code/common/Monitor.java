package common;

import utilities.MessageCollector;

public class Monitor {

    private int lineNumber = 0;
    private MessageCollector errorCollector, warningCollector;

    private Monitor() {
        this.errorCollector = new MessageCollector();
        this.warningCollector = new MessageCollector();
    }

    private static class Holder {
        private static final Monitor INSTANCE = new Monitor();
    }

    public static Monitor getInstance() {
        return Holder.INSTANCE;
    }

    public void increaseLineNumber() {
        this.lineNumber++;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void addError(String errorMessage) {
        this.errorCollector.add(errorMessage);
    }

    public void addWarning(String warningMessage) {
        this.warningCollector.add(warningMessage);
    }
}
