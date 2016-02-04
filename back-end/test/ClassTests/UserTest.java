package ClassTests;

import bootstrap.ShopItemBootstrap;
import dao.UserDAO;
import models.*;

import play.Logger;
import play.test.*;


import static play.test.Helpers.*;

/**
 * Created by gabriel on 6/25/15.
 *
 * @author gabriel
 */
public class UserTest {


    @BeforeClass
    public static void app(){

        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);

        AvatarBootstrap.init();
        ShopItemBootstrap.init();

    }


    @Test
    public void testUserCreate(){
        User user  = UserDAO.create("UserCreate", "UserCreate@local.de", "empty", 0);


        assertTrue(user != null);
        Logger.info("UserCreate successfull");
    }
}
