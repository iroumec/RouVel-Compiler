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

    public int getNumberOfMessages() {
        return this.messages.size();
    }

    // --------------------------------------------------------------------------------------------------------------------

    public void showMessages() {

        for (String message : messages) {
            Printer.printWrapped(message);
        }
    }
}
