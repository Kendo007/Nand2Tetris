package compiler;

public class Enums {
    private Enums() {}

    /** The Kind enum is used to store the kind of variable */
    protected enum Kind {
        STATIC("static"), FIELD("field"), ARG("argument"), LCL("local");
        private final String name;

        Kind(String s) { this.name = s; }

        @Override
        public String toString() { return this.name; }
    }

    /** The Segment enum is used to store the segment of a variable */
    protected enum Segment {
        CONST("constant"), ARG("argument"), LCL("local"), STATIC("static"), THIS("this"), THAT("that"), POINTER("pointer"), TEMP("temp");
        private final String name;

        Segment(String s) { this.name = s; }

        @Override
        public String toString() { return this.name; }
    }

    /** The Command enum is used to store the command type */
    protected enum Command {
        ADD("add"), SUB("sub"), NEG("neg"), EQ("eq"), GT("gt"), LT("lt"), AND("and"), OR("or"), NOT("not");
        private final String name;

        Command(String s) { this.name = s; }

        @Override
        public String toString() { return this.name; }
    }
}
