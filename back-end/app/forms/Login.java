package forms;

import play.data.validation.Constraints;
import service.ServiceUser;

/**
 * @author fabiomazzone
 */
public class Login {
    @Constraints.Required
    @Constraints.Email
    private String email;

    @Constraints.Required
    private String password;

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String validate() {
        if(ServiceUser.authenticate(this.email, this.password) == null){
            return "Invalid email or password";
        }
        return null;
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
