package ClassTests;

import models.Potion;
import play.Logger;
import play.test.FakeApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * @author fabiomazzone
 */
public class PotionTest {

    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);
    }


    @Test
    public void testPotionCreate(){
        Potion potion = Potion.create("name", 1, 3, 4);

        assertTrue(potion != null);

        Logger.info("PotionCreate successfull");
    }

    @Test
    public void testPotionCreateDouble(){
        Potion potion = Potion.create("name1", 1, 3, 4);
        Potion potion1 = Potion.create("name1", 1, 3, 4);

        assertTrue(potion == potion1);

        Logger.info("PotionCreateDouble successfull");
    }

}
