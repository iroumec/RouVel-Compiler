public enum TokenType {
    ID(1),
    CTE(2),
    STR(3),
    ASIG(4),
    DASIG(5),
    GE(6),
    LE(7),
    EQ(8),
    NEQ(9),
    FLECHA(10),
    IF(11),
    ELSE(12),
    EIF(13),
    PRNT(14),
    RET(15),
    UINT(16),
    FLOAT(17),
    DO(18),
    WHILE(19);

    private int number;

    private TokenType(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean requiereLexema() {
        return this == ID || this == CTE || this == STR;
    }
}
