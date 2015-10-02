package de.tu_bs.cs.ifis.sqlgame.exception;

import java.util.ArrayList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

/**
 * Class MyParserException.
 * 
 * Handles Errors and Exceptions that could arrise during the XML-syntaxcheck
 * 
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class MyParserExceptionHandler implements ErrorHandler {
    
    
    /**
     * Constructor MyParserException.
     */
    public MyParserExceptionHandler() {
        //Nothing to do.
    }
    
    /**
     * Method warning.
     * 
     * Print the warning with the line number, if the xml-file is not correct
     * 
     * @param e SAXParseException
     * @throws SAXException exception from parsing the document
     */
    @Override
    public void warning(SAXParseException e) throws SAXException {
        int zeile = e.getLineNumber();
        int spalte = e.getColumnNumber();
        MyXMLParserErrorHandler.addWarning("Achtung! Warnung: Überprüfe Zeile " + zeile + ", Spalte " + spalte + " " + e.getMessage());
    }
    
    /**
     * Method error.
     * 
     * Print the error with the line number, if the xml-file is not correct
     * 
     * @param e SAXParseException
     * @throws SAXException exception from parsing the document
     */
    @Override
    public void error(SAXParseException e) throws SAXException {
        int zeile = e.getLineNumber();
        int spalte = e.getColumnNumber();
        MyXMLParserErrorHandler.addError("Achtung! Fehler in Zeile " + zeile + ", Spalte " + spalte + " " + e.getMessage());
    }
    
    /**
     * Method fatalError.
     * 
     * Print the fatal error with the line number, if the xml-file is not correct
     * 
     * @param e SAXParseException
     * @throws SAXException exception from parsing the document
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        int zeile = e.getLineNumber();
        int spalte = e.getColumnNumber();
        MyXMLParserErrorHandler.addError("Achtung! Fataler Fehler in Zeile " + zeile + ", Spalte " + spalte + " " + e.getMessage());
    }
}
