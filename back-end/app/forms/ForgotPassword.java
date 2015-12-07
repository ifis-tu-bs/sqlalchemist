package forms;

import dao.UserDAO;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class ForgotPassword {
    private String email;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if(email.isEmpty()) {

            errors.add(new ValidationError("email", "This field is required"));

        } else if(!SignUp.isValidEmailAddress(email)) {

            errors.add(new ValidationError("email", "Valid email required"));

        } else if (UserDAO.getByEmail(email) == null) {

            errors.add(new ValidationError("email", "email address is unknown"));

        }

        return errors.isEmpty() ? null : errors;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
