package ClassTests;

import models.Potion;
import models.Scroll;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * Created by fabiomazzone on 15/07/15.
 */
public class ScrollTest {

    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);
    }


    @Test
    public void testMapCreate(){
        Scroll scroll = Scroll.create(58, "Scales of a dragon", Scroll.TYPE_SCROLL_DEFENSE, null);

        assertTrue(scroll != null);

        Logger.info("ScrollCreate successfull");
    }

    @Test
    public void testScrollCreateDouble(){
        Scroll scroll1 = Scroll.create(59, "Scales of a drago1n", Scroll.TYPE_SCROLL_HEALTH, null);
        Scroll scroll2 = Scroll.create(59, "Scales of a dragon1", Scroll.TYPE_SCROLL_HEALTH, null);

        assertTrue(scroll1 == scroll2);

        Logger.info("ScrollCreateDouble successfull");
    }
}
