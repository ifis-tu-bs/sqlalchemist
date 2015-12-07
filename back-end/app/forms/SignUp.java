package forms;

import dao.UserDAO;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class SignUp {
    private String email;

    private String username;

    private String password;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if(email.isEmpty()) {
            errors.add(new ValidationError("email", "This field is required"));
        } else if(!isValidEmailAddress(email)) {
            errors.add(new ValidationError("email", "Valid email required"));
        } else if (UserDAO.getByEmail(email) != null) {
            errors.add(new ValidationError("email", "already taken"));
        }

        if(username.isEmpty()) {
            errors.add(new ValidationError("username", "This field is required"));
        } else if(UserDAO.getByUsername(username) != null) {
            errors.add(new ValidationError("username", "already taken"));
        }


        if(password.isEmpty()) {
            errors.add(new ValidationError("password", "This field is required"));
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

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
