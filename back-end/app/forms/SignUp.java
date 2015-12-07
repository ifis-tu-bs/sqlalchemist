package forms;

import dao.UserDAO;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class SignUp {
    @Constraints.Required
    private String username;

    @Constraints.Required
    @Constraints.Email
    private String email;

    @Constraints.Required
    private String password;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if(UserDAO.getByEmail(email) != null) {
            errors.add(new ValidationError("email", "This e-mail is already registered."));
        }
        if(UserDAO.getByUsername(username) != null) {
            errors.add(new ValidationError("username", "This username is already taken."));
        }
        return errors.isEmpty() ? null : errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
