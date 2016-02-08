package helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * @author Invisible
 */
public class MailSender {
    private final Properties props;

    /**
     * MailSender for the whole Application.
     */
    private static final MailSender mailSender = new MailSender();



    /**
     * Constructor File should be inside conf/
     */
    public MailSender() {
        this.props = new Properties();

        // For testin purpose I currently use another email account
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
        Config conf = ConfigFactory.load();
        Map config = conf.getObject("mailAccount").unwrapped();

        this.props.putAll(config);

        /*if (inputStream != null) {
            try {
                this.props.load(inputStream);
            } catch (IOException e) {
                Logger.warn("Could not find or read smtp configuration file!");
            }
        }*/
    }

    /**
     * get MailSender Instance
     */
    public static MailSender getInstance () {
        return mailSender;
    }



    public static void resetPassword(String email, String newPassword) {
        MailSender.getInstance().sendResetEmail(email, newPassword);
    }

    /**
     * Send verify Email. Should be Used by User.class
     * @param email email.TO
     * @param verifyString the Hex-verify-String.
     */
    public void sendVerifyEmail(String email, String verifyString) {

        Session session = Session.getInstance(this.props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                MailSender.this.props.getProperty("username"),
                                MailSender.this.props.getProperty("password")
                                );
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("thesqlalchemist@ifis.cs.tu-bs.de"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Verify your Email for the SQLalchemist");
            message.setText("" +
                    "Dear User\n" +
                    "\n" +
                    "Thanks for registering at the SQLalchemist.\n" +
                    "Please verify your Email address by following the given link:\n" +
                    "http://sqlalchemist.ifis.cs.tu-bs.de/API/Validate/Email/" + verifyString + "\n" +
                    "\n" +
                    "Have fun training your SQL skills.\n" +
                    "\n" +
                    "Greetings, your SQLalchemist Admin\n" +
                    "");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send verify Email. Should be Used by User.class
     * @param email email.TO
     * @param newPassword the Hex-verify-String.
     */
    public void sendResetEmail(String email, String newPassword) {

        Session session = Session.getInstance(this.props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                MailSender.this.props.getProperty("username"),
                                MailSender.this.props.getProperty("password")
                        );
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("thesqlalchemist@ifis.cs.tu-bs.de"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Reset your Password for theSQLalchemist");
            message.setText("" +
                    "Dear User\n" +
                    "\n" +
                    "Here is your password new password for the SQL Alchemist.\n" +
                    "If you did not order a new password please send us a mail to: sqlalchemist@ifis.cs.tu-bs,de !\n" +
                    "Your new password is: " + newPassword + ", please change it\n" +
                    "\n" +
                    "Have fun training your SQL skills.\n" +
                    "\n" +
                    "Greetings, your SQL Alchemist Admin\n");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send verify Email. Should be Used by User.class
     * @param user all Userinformation in this Object
     */
    public void sendPromoteMail(User user) {
        String email = user.getEmail();

        Session session = Session.getInstance(this.props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                MailSender.this.props.getProperty("username"),
                                MailSender.this.props.getProperty("password")
                        );
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("thesqlalchemist@ifis.cs.tu-bs.de"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Promoted on SQL-Alchemist");
            message.setText("" +
                    "Dear User\n" +
                    "\n" +
                    "You have been promoted!\n" +
                    "You now have new Features, that you may use in our AdminTool.\n" +
                    "\n" +
                    "Check your new Abilities at:\n" +
                    "http://sqlalchemist.ifis.cs.tu-bs.de/admin\n" +
                    "\n" +
                    "Have fun training your SQL skills.\n" +
                    "\n" +
                    "Greetings, your SQL-Alchemist Admin\n" +
                    "");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
