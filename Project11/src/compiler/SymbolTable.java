package compiler;

import java.util.HashMap;
import compiler.Enums.*;

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
 * number of variables of a certain kind, get the kind of a variable, get the datatype of a variable,
 * and get the index of a variable.
 * </p>
 */
public class SymbolTable {
    /** The Data class is used to store the datatype, kind, and index of a variable */
    private static class Data {
        protected String datatype;
        protected Kind kind;
        protected int index;

        Data(String datatype, Kind kind, int index) {
            this.datatype = datatype;
            this.kind = kind;
            this.index = index;
        }
    }

    private static int varCount = 0, argCount = 0, fieldCount = 0, staticCount = 0;
    private static HashMap<String, Data> classTable = new HashMap<>();
    private static HashMap<String, Data> subroutineTable = new HashMap<>();

    /** Starts a new class scope (i.e., resets the class’s symbol table) */
    protected static void startClass() {
        fieldCount = 0;
        staticCount = 0;
        classTable = new HashMap<>();
    }

    /**  Starts a new subroutine scope (i.e., resets the subroutine’s symbol table) */
    protected static void startSubroutine() {
        varCount = 0;
        argCount = 0;
        subroutineTable = new HashMap<>();
    }

    /** Defines a new identifier of a given name, datatype, and kind and assigns it a running index */
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

    /** Returns the datatype of the named identifier in the current scope */
    protected static String typeOf(String name) {
        if (subroutineTable.containsKey(name)) {
            return subroutineTable.get(name).datatype;
        } else if (classTable.containsKey(name)) {
            return classTable.get(name).datatype;
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

    protected static Segment segmentOf(String name) {
        if (subroutineTable.containsKey(name)) {
            Kind kind = subroutineTable.get(name).kind;

            if (kind == Kind.ARG) {
                return Segment.ARG;
            } else if (kind == Kind.LCL) {
                return Segment.LCL;
            } else {
                return null;
            }
        } else if (classTable.containsKey(name)) {
            Kind kind = classTable.get(name).kind;

            if (kind == Kind.STATIC) {
                return Segment.STATIC;
            } else if (kind == Kind.FIELD) {
                return Segment.THIS;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
