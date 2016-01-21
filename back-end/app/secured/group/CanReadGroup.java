package secured.group;

import dao.SessionDAO;
import dao.UserDAO;
import models.Session;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class CanReadGroup extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        Session session = SessionDAO.getById(context.session().get("session"));
        User    user    = (session != null)? session.getOwner() : null;

        return (user != null && session.isActive() && user.getRole().getGroupPermissions().canRead() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("You have not the permissions to read groups");
    }
}
