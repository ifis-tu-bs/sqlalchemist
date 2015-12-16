package forms;

import play.data.validation.Constraints;

/**
 * @author fabiomazzone
 */
public class ChangePassword {
    public String oldPassword;
    @Constraints.Required
    public String newPassword;
}
