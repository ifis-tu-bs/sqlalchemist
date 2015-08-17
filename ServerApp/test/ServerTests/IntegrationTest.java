package ServerTests;

import org.junit.Test;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    //@Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/");
            //assertTrue(browser.manage().ime());
        });
    }

}
