package secured.role;

import dao.SessionDAO;
import models.Session;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class CanUpdateRole extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        Session session = SessionDAO.getById(context.session().get("session"));
        User    user    = (session != null)? session.getOwner() : null;

        return (user != null && session.isActive() && !context.session().isDirty && user.getRole().getRolePermissions().canUpdate() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("You have not the permissions to update Roles");
    }
}
