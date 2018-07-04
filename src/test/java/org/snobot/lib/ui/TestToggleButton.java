package org.snobot.lib.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestToggleButton
{

    @Test
    public void testToggleButton()
    {
        ToggleButton btn = new ToggleButton();

        Assertions.assertFalse(btn.getState());
        Assertions.assertFalse(btn.update(false));
        Assertions.assertTrue(btn.update(true));
        Assertions.assertTrue(btn.getState());

        // Continue to issue true
        Assertions.assertTrue(btn.update(true));
        Assertions.assertTrue(btn.getState());

        // Reset it back to false (released). It should still be toggled high
        Assertions.assertTrue(btn.update(false));
        Assertions.assertTrue(btn.getState());

        // Press it again, should now toggle low
        Assertions.assertFalse(btn.update(true));
        Assertions.assertFalse(btn.getState());

        // Continue to press it for multiple loops, should remain false
        Assertions.assertFalse(btn.update(true));
        Assertions.assertFalse(btn.getState());
    }
}
