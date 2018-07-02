package org.snobot.lib.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLatchedButton
{

    @Test
    public void testLatchedButton()
    {
        LatchedButton btn = new LatchedButton();

        Assertions.assertFalse(btn.getState());
        Assertions.assertFalse(btn.update(false));
        Assertions.assertTrue(btn.update(true));
        Assertions.assertTrue(btn.getState());

        // Continue to issue true
        Assertions.assertFalse(btn.update(true));
        Assertions.assertFalse(btn.getState());

        // Reset it back to false (released)
        Assertions.assertFalse(btn.update(false));
        Assertions.assertFalse(btn.getState());
    }
}
