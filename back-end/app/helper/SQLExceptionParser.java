package helper;

/**
 * @author fabiomazzone
 */
public class SQLExceptionParser extends ExceptionParser{
    public static String parse(int err) {
        String message;
        switch (err) {
            case 0:
                return "";
            case 50004:
                message = "Unknown DataType ( Or one of columns have a name with an space )";
                break;

            default:
                message = "Unknown Error";
                break;
        }


        return stringify(err, message);
    }
}
