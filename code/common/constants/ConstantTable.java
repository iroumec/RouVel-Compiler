package common.constants;

import java.util.HashSet;
import java.util.Set;

public class ConstantTable {

    private static ConstantTable INSTANCE;
    private final Set<String> constants;

    private ConstantTable() {
        this.constants = new HashSet<>();
    }

    public static ConstantTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConstantTable();
        }

        return INSTANCE;
    }

    public void addEntry() {

    }

}
