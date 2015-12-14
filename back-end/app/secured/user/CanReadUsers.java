package secured.user;

import dao.UserDAO;
import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;


public class CanReadUsers extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        User user = UserDAO.getBySession(context.session().get("session"));
        return (user != null && user.getRole().getUserPermissions().canRead() ) ? context.session().get("session") : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden(Json.parse("'message':'you have not the permissions to view all users'"));
    }
}
