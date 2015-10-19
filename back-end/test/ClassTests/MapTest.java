package ClassTests;

import models.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * @author fabiomazzone
 */
public class MapTest {

    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);
    }


    @Test
    public void testMapCreate(){
        Map map = Map.create(1,"path", true);

        assertTrue(map != null);

        Logger.info("MapCreate successfull");
    }

    @Test
    public void testMapCreateDouble(){
        Map map = Map.create(2,"path", true);
        Map map1 = Map.create(2,"path", true);

        assertTrue(map == map1);

        Logger.info("MapCreateDouble successfull");
    }


}
