package ClassTests;

import models.Avatar;

import play.Logger;
import play.test.FakeApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * @author fabiomazzone
 */
public class AvatarTest {

    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);
    }


    @Test
    public void testAvatarCreate(){
        Avatar avatar = Avatar.create("Test Avatar", "test", "asd", "qwe", true, 2, 3, 3, 4, 5);

        assertTrue(avatar != null);

        Logger.info("AvatarCreate successfull");
    }

    @Test
    public void testAvatarCreateDouble(){
        Avatar avatar1 = Avatar.create("Test Avatar1", "test", "asd", "qwe", true, 2,3,3,4,5);
        Avatar avatar2 = Avatar.create("Test Avatar1", "test", "asd", "qwe", true, 2,3,3,4,5);

        assertTrue(avatar1 == avatar2);

        Logger.info("AvatarCreateDouble successfull");
    }
}
