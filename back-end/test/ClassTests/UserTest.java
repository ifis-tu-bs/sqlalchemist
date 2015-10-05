package ClassTests;

import models.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
//import play.Application;
import play.Logger;
import play.test.*;


import static play.test.Helpers.*;

/**
 * Created by gabriel on 6/25/15.
 */
public class UserTest {


    @BeforeClass
    public static void app(){

        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);

        Avatar.init();
        ShopItem.init();

    }


    @Test
    public void testUserCreate(){
        boolean testSuccessful= true;
        try {
            User.create("UserCreate", "UserCreate@local.de", "empty");
        } catch (UsernameTakenException | EmailTakenException e) {
            testSuccessful = false;
        }

        assertTrue(testSuccessful);
        Logger.info("UserCreate successfull");
    }

    @Test(expected = UsernameTakenException.class)
    public void testUserCreateNameTaken() throws EmailTakenException, UsernameTakenException {
        try {
            User.create("UserTaken", "UserTaken1@local.de", "empty");
            User.create("UserTaken", "UserTaken2@local.de", "empty");
        } catch (UsernameTakenException | EmailTakenException e) {
            Logger.info("UserNameTaken successfull");
            throw e;
        }

    }

    @Test(expected = EmailTakenException.class)
    public void testUserCreateEmailTaken() throws EmailTakenException, UsernameTakenException {
        try {
            User.create("EmailTaken1", "EmailTaken@local.de", "empty");
            User.create("EmailTaken2", "EmailTaken@local.de", "empty");
        } catch (UsernameTakenException | EmailTakenException e) {
            Logger.info("EmailTaken successfull");
            throw e;
        }
    }
}
