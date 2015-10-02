package de.tu_bs.cs.ifis.sqlgame.exception;

/**
 * Class MySQLAlchemistException.
 *
 * Handles Errors and Exceptions that could arrise during the execution of
 * SQL-Statements or during the parsing of the xml-file
 *
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class MySQLAlchemistException extends Exception {

    /**
     * Exception that is thrown.
     */
    Exception exception;
    
    /**
     * Message of the error or warning.
     */
    String message;

    /**
     * Constructor for the exception
     *
     * @param message detailed message from the error
     * @param exception catched Exception
     */
    public MySQLAlchemistException(String message, Exception exception) {

        this.message = message;
        this.exception = exception;
    }

    /**
     * Getter for the message of this exception and the wrapped exception
     *
     * @return string from the message
     */
    public String getMyMessage() {
        if (exception != null) {
            if (exception.getClass() == MySQLAlchemistException.class) {
                MySQLAlchemistException ex = (MySQLAlchemistException) exception;
                return message + ex.getMyMessage();
            }
            return message + exception.getMessage();
        } else {
            return message;
        }
}
}
