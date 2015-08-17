package secured;

import models.*;
import play.Logger;
import play.mvc.*;

/**
 * The Security Class to verify AppKey
 *
 * Created by fabiomazzone on 27/04/15.
 */
public class UserSecured extends Security.Authenticator {
    /**
     *
     * @param cxt
     * @return
     */
    @Override
    public String getUsername(Http.Context cxt) {
        UserSession session = UserSession.getSession(cxt.session());
        if( session != null && session.isValid(cxt.request().remoteAddress()) ) {
            return session.getSessionID();
        }
        return null;
    }

    /**
     *
     * @param context
     * @return
     */
    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("restricted page, you need User permissions");
    }
}
