package de.tu_bs.cs.ifis.sqlgame.exception;

import java.util.ArrayList;

/**
 * Class MyXMLParserErrorHandler
 * 
 * Error Handler for all errors that cant be thrwon as exception
 * 
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class MyXMLParserErrorHandler {
    
    private static boolean error = false;
    
    private static ArrayList<String> errorList = new ArrayList();
    
    private static ArrayList<String> warningList = new ArrayList();

    /*
     * Getter and setter
     */
    public static boolean isError() {
        return error;
    }

    public static void setError(boolean error) {
        MyXMLParserErrorHandler.error = error;
    }

    public static ArrayList<String> getErrorList() {
        return errorList;
    }

    public static void setErrorList(ArrayList<String> errorList) {
        MyXMLParserErrorHandler.errorList = errorList;
    }

    public static ArrayList<String> getWarningList() {
        return warningList;
    }

    public static void setWarningList(ArrayList<String> warningList) {
        MyXMLParserErrorHandler.warningList = warningList;
    }
   
    public static void addWarning(String s) {
        MyXMLParserErrorHandler.warningList.add(s);
    }
    
    public static void addError(String s) {
        MyXMLParserErrorHandler.errorList.add(s);
        error = true;
    }
    
    /**
     * Constructor MyXMLParserErrorHandler.
     * 
     * Set up defaults.
     * 
     * @param error boolean boolean if it is an error
     * @param errorList ArrayList list with the errors
     * @param warningList ArrayList list with the warnings
     */
    public MyXMLParserErrorHandler(boolean error, ArrayList<String> errorList, ArrayList<String> warningList) {
        MyXMLParserErrorHandler.error = error;
        MyXMLParserErrorHandler.errorList = errorList;
        MyXMLParserErrorHandler.warningList = warningList;
    }
    
    /**
     * Method resetThis.
     * 
     * Reset the object with its lists.
     */
    public static void resetThis() {
        MyXMLParserErrorHandler.error = false;
        MyXMLParserErrorHandler.warningList.clear();
        MyXMLParserErrorHandler.errorList.clear();
    }
    
}
