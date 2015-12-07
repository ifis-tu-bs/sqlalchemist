package secured;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class SessionAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        return (context.session().get("session") != null) ? context.session().get("session") : null ;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("you are not containing a session");
    }
}
