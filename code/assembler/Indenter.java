package assembler;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Indenter {

    static String indent(String text, String indentation) {
        return Arrays.stream(text.split("\n"))
                .map(line -> indentation + line)
                .collect(Collectors.joining("\n"))
                + "\n";
    }

    static String indent(String text, StringBuilder indentation) {
        return indent(text, indentation.toString());
    }

    static String indent(StringBuilder text, String indentation) {
        return indent(text.toString(), indentation);
    }

    static String indent(StringBuilder text, StringBuilder indentation) {
        return indent(text.toString(), indentation.toString());
    }
}
