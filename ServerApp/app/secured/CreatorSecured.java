package secured;

import models.*;
import play.mvc.*;

/**
 * The Security Class to verify AppKey
 *
 * Created by fabiomazzone on 27/04/15.
 */
public class CreatorSecured extends Security.Authenticator {
    /**
     *
     * @param cxt
     * @return
     */
    @Override
    public String getUsername(Http.Context cxt) {
        UserSession session = UserSession.getSession(cxt.session());
        if( session != null && session.isValid(cxt.request().remoteAddress()) && session.getUser().getRole() >= User.ROLE_CREATOR ) {
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
        return forbidden("restricted page, you need higher permissions, than \"Creator\"");
    }
}
