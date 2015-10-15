package helper;

import models.User;
import play.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;

/**
 * @author Invisible
 */
public class MailSender {
    private Properties props;

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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");

        if (inputStream != null) {
            try {
                this.props.load(inputStream);
            } catch (IOException e) {
                Logger.warn("Could not find or read smtp configuration file!");
            }
        }
    }

    /**
     * get MailSender Instance
     */
    public static MailSender getInstance () {
        return mailSender;
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
            message.setSubject("Verify your Email for theSQLalchemist");
            message.setText("" +
                    "Dear User\n" +
                    "\n" +
                    "Thanks for registering at theSQLalchemist.\n" +
                    "Please verify your Email address by following the given link:\n" +
                    "http://sqlalchemist.ifis.cs.tu-bs.de/users/verify/" + verifyString + "\n" +
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
     * @param verifyString the Hex-verify-String.
     */
    public void sendResetEmail(String email, String verifyString) {

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
                    "Here is your password reset link for theSQLalchemist.\n" +
                    "If you did not order a reset simply ignore this Mail!\n" +
                    "http://sqlalchemist.ifis.cs.tu-bs.de/users/passwordreset/" + verifyString + "\n" +
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


    public void sendPasswordChanged(String email) {
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
            message.setSubject("Reset your Password for SQL-Alchemist");
            message.setText("" +
                    "Dear User\n" +
                    "\n" +
                    "Your Password for theSQLalchemist has been changed.\n" +
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
}
