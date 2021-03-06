package secured.role;

import dao.SessionDAO;
import dao.UserDAO;
import models.Session;
import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class CanReadRole extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        Session session = SessionDAO.getById(context.session().get("session"));
        User    user    = (session != null)? session.getOwner() : null;

        return (user != null && session.isActive() && !context.session().isDirty && user.getRole().getRolePermissions().canRead() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        User user = UserDAO.getBySession(context.session().get("session"));
        return ok(Json.toJson(user.getRole()));
    }
}
