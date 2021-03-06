package ClassTests;

import models.Inventory;
import models.Potion;
import models.User;
import play.test.FakeApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;


/**
 * @author gabriel on 6/25/15.
 */
public class InventoryTest {
    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);

        Potion.init();
    }


    @SuppressWarnings("UnusedAssignment")
    @Test
    public void testInventoryCreate(){
        Profile profile;
        try {
            profile = User.create("fabiomazzone", "fabio.mazzone@me.com", "test").profile;
        } catch (UsernameTakenException | EmailTakenException e) {
            e.printStackTrace();
        }

        Potion potion = Potion.getById(1L);

        Inventory inv = Inventory.create(profile, potion);

        assertTrue(inv != null);

    }
}
