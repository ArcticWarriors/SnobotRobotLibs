package org.snobot.lib.motion_profile;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStaticSetpointIterator
{

    @Test
    public void testIteratorWithConfig()
    {
        PathConfig pathConfig = new PathConfig(3, 350, 1000, .02);
        StaticSetpointIterator iterator = new StaticSetpointIterator(pathConfig);

        for (int i = 0; i < 5; ++i)
        {
            PathSetpoint setpoint = iterator.getNextSetpoint(0, 0, 0);
            Assertions.assertNotNull(setpoint);
            Assertions.assertFalse(iterator.isFinished());
        }

        // Do the last point
        PathSetpoint setpoint = iterator.getNextSetpoint(0, 0, 0);
        Assertions.assertNotNull(setpoint);
        Assertions.assertTrue(iterator.isFinished());

        // Further calls should return null
        setpoint = iterator.getNextSetpoint(0, 0, 0);
        Assertions.assertNull(setpoint);
    }

    @Test
    public void testIteratorWithPath()
    {
        PathConfig pathConfig = new PathConfig(3, 350, 1000, .02);
        List<PathSetpoint> setpoints = new PathGenerator().generate(pathConfig);

        StaticSetpointIterator iterator = new StaticSetpointIterator(setpoints);

        Assertions.assertEquals(setpoints, iterator.getIdealPath());

        for (int i = 0; i < 5; ++i)
        {
            PathSetpoint setpoint = iterator.getNextSetpoint(0, 0, 0);
            Assertions.assertNotNull(setpoint);
            Assertions.assertFalse(iterator.isFinished());
        }

        // Do the last point
        PathSetpoint setpoint = iterator.getNextSetpoint(0, 0, 0);
        Assertions.assertNotNull(setpoint);
        Assertions.assertTrue(iterator.isFinished());

        // Further calls should return null
        setpoint = iterator.getNextSetpoint(0, 0, 0);
        Assertions.assertNull(setpoint);
    }

}
