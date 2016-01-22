package secured;

import dao.SessionDAO;
import models.Session;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class UserAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        Session session = SessionDAO.getById(context.session().get("session"));
        return (session != null && session.getOwner() != null && !context.session().isDirty && session.isActive()) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("you are not signed in");
    }
}
