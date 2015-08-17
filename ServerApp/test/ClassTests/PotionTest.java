package ClassTests;

import models.Potion;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;

import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.PARTIAL_CONTENT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * Created by fabiomazzone on 15/07/15.
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
