package secured;

import models.User;
import models.UserSession;

import dao.UserSessionDAO;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;
import play.mvc.Result;

/**
 * The Security Class to verify
 *
 * @author fabiomazzone
 */
public class AdminSecured extends Authenticator {
    /**
     *
     * @param cxt http context
     * @return returns an Result
     */
    @Override
    public String getUsername(Context cxt) {

        Logger.info("Check for Admin: ");
      String sessionID = cxt.session().get("sessionID");
      UserSession session = UserSessionDAO.getBySessionID(sessionID);
        if( session != null && session.isValid(cxt.request().remoteAddress()) && session.getUser().getRole() >= User.ROLE_ADMIN ) {
            return session.getUser().getProfile().getUsername();
        }
        return null;
    }

    /**
     *
     * @param context http context
     * @return returns an Result
     */
    @Override
    public Result onUnauthorized(Context context) {
        return forbidden("restricted page, you need higher permissions, than ");
    }
}
