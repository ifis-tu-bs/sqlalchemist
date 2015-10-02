package secured;

import models.User;
import models.UserSession;

import dao.UserSessionDAO;

import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;
import play.mvc.Result;

/**
 * The Security Class to verify
 *
 * Created by fabiomazzone on 27/04/15.
 */
public class CreatorSecured extends Authenticator {
    /**
     *
     * @param cxt
     * @return
     */
    @Override
    public String getUsername(Context cxt) {
      String sessionID = cxt.session().get("sessionID");
      UserSession session = UserSessionDAO.getBySessionID(sessionID);
        if( session != null && session.isValid(cxt.request().remoteAddress()) && session.getUser().getRole() >= User.ROLE_CREATOR ) {
            return session.getUser().getProfile().getUsername();
        }
        return null;
    }

    /**
     *
     * @param context
     * @return
     */
    @Override
    public Result onUnauthorized(Context context) {
        return forbidden("restricted page, you need higher permissions, than \"Creator\"");
    }
}
