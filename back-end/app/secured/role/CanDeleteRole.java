package secured.role;

import dao.UserDAO;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class CanDeleteRole extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        User user = UserDAO.getBySession(context.session().get("session"));
        return (user != null && user.getRole().getRolePermissions().canDelete() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("You have not the permissions to delete Roles");
    }
}
