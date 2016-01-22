package secured;

import dao.SessionDAO;
import models.Session;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class SessionAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        Session session = (context.session().get("session") != null)?  SessionDAO.getById(context.session().get("session")) : null;
        return (session != null && !context.session().isDirty && session.isActive()) ? context.session().get("session") : null ;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("you are not containing a session");
    }
}
