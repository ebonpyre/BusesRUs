package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test the StopManager
 */
public class StopManagerTest {

    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
        assertEquals(null, StopManager.getInstance().getSelected());
    }

    @Test
    public void testDistAndGet() {
        Stop s1 = new Stop(1, "My house", new LatLon(0, 0));
        Stop s2 = new Stop(2, "My house", new LatLon(1000, 200));
        Stop s3 = new Stop(3, "My house", new LatLon(-50, -100));
        Stop s4 = new Stop(4, "My house", new LatLon(10000, 10000));

        Stop r1 = StopManager.getInstance().getStopWithNumber(1, "My house", new LatLon(0, 0));
        assertEquals(s1, r1);
        Stop r4 = StopManager.getInstance().getStopWithNumber(1);
        assertEquals(s1, r4);
        Stop r5 = StopManager.getInstance().getStopWithNumber(1, "My house", new LatLon(0, 0));
        assertEquals(s1, r5);

        Stop r2 = StopManager.getInstance().getStopWithNumber(2, "My house", new LatLon(1000, 200));
        assertEquals(s2, r2);

        Stop r3 = StopManager.getInstance().getStopWithNumber(3, "My house", new LatLon(-50, -100));
        assertEquals(s3, r3);

        Stop r6 = StopManager.getInstance().getStopWithNumber(4, "My house", new LatLon(10000, 10000));
        assertEquals(s4, r6);

        assertEquals(4, StopManager.getInstance().getNumStops());
        assertEquals(s1, StopManager.getInstance().findNearestTo(new LatLon(0.0001, 0.001)));
        assertEquals(s3, StopManager.getInstance().findNearestTo(new LatLon(-50, -100.001)));
        assertEquals(s2, StopManager.getInstance().findNearestTo(new LatLon(1000, 200)));
        assertEquals(null, StopManager.getInstance().findNearestTo(new LatLon(0.09, 0)));
    }

    @Test
    public void testSelect() {
        Stop s1 = new Stop(1, "My house", new LatLon(0, 0));
        Stop s2 = new Stop(2, "My house", new LatLon(1000, 200));
        try {
            StopManager.getInstance().setSelected(null);
        } catch (StopException e) {
            fail("Not expected");
        }
        try {
            StopManager.getInstance().setSelected(s1);
            fail("Not expected");
        } catch (StopException e) {
            // expected
        }
        StopManager.getInstance().getStopWithNumber(2, "My house", new LatLon(1000, 200));
        try {
            StopManager.getInstance().setSelected(s2);
        } catch (StopException e) {
            fail("Not expected");
        }
        assertEquals(s2, StopManager.getInstance().getSelected());
        StopManager.getInstance().clearSelectedStop();
        assertEquals(null, StopManager.getInstance().getSelected());
        for (Stop s : StopManager.getInstance()) {
            if (s == s2) {
                fail ("Not expected");
            }
        }
    }
}
