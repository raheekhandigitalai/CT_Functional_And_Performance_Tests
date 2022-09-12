package tests;

import com.experitest.reporter.testng.Listener;
import org.testng.annotations.*;

@Listeners(Listener.class)
public class DebugTest {

    @BeforeMethod
    public void setUp() {}

    @Test
    public void debug_test() {}

    @AfterMethod
    public void tearDown() {}

}

