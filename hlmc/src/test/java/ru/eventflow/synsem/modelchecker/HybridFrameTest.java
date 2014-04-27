package ru.eventflow.synsem.modelchecker;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Lookups, additions, error handling
 */
public class HybridFrameTest {

    HybridFrame frame;

    @Before
    public void setUp() {
        frame = new HybridFrame("Tiny");

        frame.addWorld("w1");
        frame.addWorld("w2");
        frame.addWorld("w3");
        frame.addWorld("w4");

        frame.setNominalAssign("i", "w1");
        frame.setNominalAssign("j", "w3");
    }

    @Test
    public void testWorlds() {
        assertEquals(4, frame.getWorlds().size());
    }

    @Test
    public void testNominals() {
        frame.setNominalAssign("i2", "w1"); // ok
        frame.setNominalAssign("i", "w2");  // not ok
    }

    @Test
    public void testLookupWorldByNominal() {
        assertEquals("w1", frame.getNominalAssign("i"));
        assertNull(frame.getNominalAssign("k"));
    }


}
