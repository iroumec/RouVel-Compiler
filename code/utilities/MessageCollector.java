package utilities;

import java.util.List;
import java.util.ArrayList;

public class MessageCollector {

    private List<String> messages;

    // --------------------------------------------------------------------------------------------------------------------

    public MessageCollector() {
        this.messages = new ArrayList<>();
    }

    // --------------------------------------------------------------------------------------------------------------------

    public void add(String message) {
        this.messages.add(message);
    }

    // --------------------------------------------------------------------------------------------------------------------

    public void removeLast() {
        this.messages.removeLast();
    }

    // --------------------------------------------------------------------------------------------------------------------

    public void replaceLastWith(String message) {
        this.messages.removeLast();
        this.messages.add(message);
    }

    // --------------------------------------------------------------------------------------------------------------------

    public int getNumberOfMessages() {
        return this.messages.size();
    }

    // --------------------------------------------------------------------------------------------------------------------

    public boolean hasMessages() {
        return this.messages.size() > 0;
    }

    // --------------------------------------------------------------------------------------------------------------------

    public void showMessages() {

        for (String message : messages) {
            Printer.printWrapped(message);
        }
    }
}
