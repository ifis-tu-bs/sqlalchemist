package secured.group;

import dao.UserDAO;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author fabiomazzone
 */
public class CanUpdateGroup extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        User user = UserDAO.getBySession(context.session().get("session"));
        return (user != null && user.getRole().getGroupPermissions().canUpdate() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden("You have not the permissions to update groups");
    }
}
