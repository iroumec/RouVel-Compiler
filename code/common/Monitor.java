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

    public void replaceLastErrorWith(String errorMessage) {
        this.errorCollector.replaceLastWith(errorMessage);
    }

    public void addWarning(String warningMessage) {
        this.warningCollector.add(warningMessage);
    }

    public boolean hasErrorMessages() {
        return this.errorCollector.hasMessages();
    }

    public boolean hasWarningMessages() {
        return this.errorCollector.hasMessages();
    }

    public int getNumberOfErrors() {
        return this.errorCollector.getNumberOfMessages();
    }

    public int getNumberOfWarnings() {
        return this.warningCollector.getNumberOfMessages();
    }

    public void showErrors() {
        this.errorCollector.showMessages();
    }

    public void showWarnings() {
        this.warningCollector.showMessages();
    }
}
