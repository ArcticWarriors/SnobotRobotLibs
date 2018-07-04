package org.snobot.lib.motion_profile;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPathGenerator
{

    @Test
    public void testTrapezoidalGeneration()
    {
        PathGenerator pathGenerator = new PathGenerator();

        PathConfig pathConfig = new PathConfig(3, 35, 1000, .02);
        List<PathSetpoint> setpoints = pathGenerator.generate(pathConfig);

        String serializedPath = IdealPlotSerializer.serializePath(setpoints);
        Assertions.assertEquals("0,0,0.2,20,0.6125,35,1.3125,35,2.0125,35,2.3875,35,2.8875,15,3,0,", serializedPath);
    }

    @Test
    public void testTriangleGeneration()
    {
        PathGenerator pathGenerator = new PathGenerator();

        PathConfig pathConfig = new PathConfig(3, 350, 1000, .02);
        List<PathSetpoint> setpoints = pathGenerator.generate(pathConfig);

        String serializedPath = IdealPlotSerializer.serializePath(setpoints);
        Assertions.assertEquals("0,0,0.2,20,0.8,40,1.5,54.7723,2.3954,34.7723,2.8909,14.7723,", serializedPath);
    }

    @Test
    public void testDeserialize()
    {
        List<PathSetpoint> serializedPath = IdealPlotSerializer.deserializePath("0,0,0.2,20,0.8,40,1.5,54.7723,2.3954,34.7723,2.8909,14.7723,");
        Assertions.assertEquals(6, serializedPath.size());

        List<PathSetpoint> invalidPath = IdealPlotSerializer.deserializePath("0,0,0.2,20,0.8,40,1.5,54.7723,2.3954,34.7723,2.8909,14.77x23,");
        Assertions.assertEquals(5, invalidPath.size());
    }
}
