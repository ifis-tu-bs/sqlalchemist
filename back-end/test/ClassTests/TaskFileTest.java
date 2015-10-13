package ClassTests;

import models.TaskFile;
import org.junit.BeforeClass;
import play.Logger;
import play.test.FakeApplication;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

/**
 * Created by fabiomazzone on 15/07/15.
 */
public class TaskFileTest {

    @BeforeClass
    public static void app(){
        FakeApplication fakeApplication = fakeApplication(inMemoryDatabase("test"));
        start(fakeApplication);
    }


    //@Test
    public void testTaskFileCreate(){
        TaskFile task = TaskFile.create(null, "<tasks></tasks>", false);

        assertTrue(task != null);

        Logger.info("TaskFileCreate successfull");
    }

    //@Test
    public void testTaskFileCreateDouble(){
        TaskFile task1 = TaskFile.create(null, "<tasks>xml</tasks>", false);
        TaskFile task2 = TaskFile.create(null, "<tasks>xlm</tasks>", false);

        assertTrue(task1 == task2);

        Logger.info("TaskFileCreateDouble successfull");
    }
}
