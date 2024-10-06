package compiler;

import java.util.HashMap;

/**
 * <h1>SymbolTable</h1>
 * <p>
 * The SymbolTable class is used to store the symbol table for the Jack compiler.
 * It contains two hashmaps, one for the class scope and one for the subroutine scope.
 * The classTable hashmap stores the class level variables and the subroutineTable hashmap
 * stores the subroutine level variables.
 * </p>
 * <p>
 * The SymbolTable class contains methods to define a new entry to the symbol table, get the
 * number of variables of a certain kind, get the kind of a variable, get the type of a variable,
 * and get the index of a variable.
 * </p>
 */
public class SymbolTable {
    /** The Kind enum is used to store the kind of variable */
    protected enum Kind {
        STATIC("static"), FIELD("field"), ARG("argument"), LCL("local");
        private final String name;

        Kind(String s) { this.name = s; }

        @Override
        public String toString() { return this.name; }
    }

    /** The Data class is used to store the type, kind, and index of a variable */
    protected static class Data {
        protected String type;
        protected Kind kind;
        protected int index;

        Data(String type, Kind kind, int index) {
            this.type = type;
            this.kind = kind;
            this.index = index;
        }
    }

    private static int varCount = 0, argCount = 0, fieldCount = 0, staticCount = 0;
    private static final HashMap<String, Data> classTable = new HashMap<>();
    private static final HashMap<String, Data> subroutineTable = new HashMap<>();

    /**  Starts a new subroutine scope (i.e., resets the subroutine’s symbol table) */
    protected static void startSubroutine() {
        varCount = 0;
        argCount = 0;
        subroutineTable.clear();
    }

    /** Defines a new identifier of a given name, type, and kind and assigns it a running index */
    protected static void define(String name, String type, Kind kind) {
        if (kind == Kind.STATIC) {
            classTable.put(name, new Data(type, kind, staticCount++));
        } else if (kind == Kind.FIELD) {
            classTable.put(name, new Data(type, kind, fieldCount++));
        } else if (kind == Kind.ARG) {
            subroutineTable.put(name, new Data(type, kind, argCount++));
        } else {
            subroutineTable.put(name, new Data(type, kind, varCount++));
        }
    }

    /** Returns the number of variables of the given kind already defined in the current scope */
    protected static int varCount(Kind kind) {
        if (kind == Kind.STATIC) {
            return staticCount;
        } else if (kind == Kind.FIELD) {
            return fieldCount;
        } else if (kind == Kind.ARG) {
            return argCount;
        } else {
            return varCount;
        }
    }

    /** Returns the kind of the named identifier in the current scope. If the identifier is unknown in the current scope, returns null */
    protected static Kind kindOf(String name) {
        if (subroutineTable.containsKey(name)) {
            return subroutineTable.get(name).kind;
        } else if (classTable.containsKey(name)) {
            return classTable.get(name).kind;
        } else {
            return null;
        }
    }

    /** Returns the type of the named identifier in the current scope */
    protected static String typeOf(String name) {
        if (subroutineTable.containsKey(name)) {
            return subroutineTable.get(name).type;
        } else if (classTable.containsKey(name)) {
            return classTable.get(name).type;
        } else {
            return null;
        }
    }

    /** Returns the index assigned to the named identifier */
    protected static int indexOf(String name) {
        if (subroutineTable.containsKey(name)) {
            return subroutineTable.get(name).index;
        } else if (classTable.containsKey(name)) {
            return classTable.get(name).index;
        } else {
            return -1;
        }
    }
}
