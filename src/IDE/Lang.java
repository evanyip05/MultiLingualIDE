package IDE;

import IDE.Utils.Executor;

public enum Lang {
    JAVA('{','}', ';', 4, new String[] {
            "public", "private", "final", "default", "protected", "package",
            "class", "enum", "interface",
            "int", "char", "boolean", "double", "long"
    });

    public final String[] reserved;

    public final char oScope, cScope, end;
    public final int indent;

    Lang(char oScope, char cScope, char end, int indent, String[] reserved) {
        this.reserved = reserved;
        this.indent = indent;
        this.oScope = oScope;
        this.cScope = cScope;
        this.end = end;
    }

}
