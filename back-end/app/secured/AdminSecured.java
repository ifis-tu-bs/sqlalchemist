package secured;

import models.*;
import play.mvc.*;

/**
 * The Security Class to verify AppKey
 *
 * @author fabiomazzone
 */
public class AdminSecured extends Security.Authenticator {
    /**
     *
     * @param cxt http context
     * @return returns an Result
     */
    @Override
    public String getUsername(Http.Context cxt) {
        UserSession session = UserSession.getSession(cxt.session());
        if( session != null && session.isValid(cxt.request().remoteAddress()) && session.getUser().getRole() >= User.ROLE_ADMIN ) {
            return session.getSessionID();
        }
        return null;
    }

    /**
     *
     * @param context http context
     * @return returns an Result
     */
    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("restricted page, you need higher permissions, than ");
    }
}
