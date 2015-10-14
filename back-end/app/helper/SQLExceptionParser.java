package helper;

/**
 * @author fabiomazzone
 */
public class SQLExceptionParser extends ExceptionParser{
    public static String parse(int err) {
        String message = null;
        switch (err) {
            case 50004:
                message = "Unknown DataType";
                break;

            default:
                message = "Unknown Error";
                break;
        }


        return stringify(err, message);
    }
}
