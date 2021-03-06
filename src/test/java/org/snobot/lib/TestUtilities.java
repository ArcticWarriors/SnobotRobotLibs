package org.snobot.lib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUtilities
{
    private static final double DOUBLE_EPSILON = .00001;

    @Test
    public void testWrap()
    {
        Assertions.assertEquals(0, Utilities.wrap(0, 0, 360), DOUBLE_EPSILON);
        Assertions.assertEquals(0, Utilities.wrap(360, 0, 360), DOUBLE_EPSILON);
        Assertions.assertEquals(1, Utilities.wrap(361, 0, 360), DOUBLE_EPSILON);
        Assertions.assertEquals(359, Utilities.wrap(-1, 0, 360), DOUBLE_EPSILON);

        Assertions.assertEquals(-1, Utilities.wrap(-1, -180, 180), DOUBLE_EPSILON);
        Assertions.assertEquals(170, Utilities.wrap(-190, -180, 180), DOUBLE_EPSILON);
        Assertions.assertEquals(-170, Utilities.wrap(190, -180, 180), DOUBLE_EPSILON);
        Assertions.assertEquals(10, Utilities.wrap(730, -180, 180), DOUBLE_EPSILON);
    }

    @Test
    public void testDeadband()
    {
        Assertions.assertEquals(0.5, Utilities.stopInDeadband(0.5, 0.1), DOUBLE_EPSILON);
        Assertions.assertEquals(-0.5, Utilities.stopInDeadband(-0.5, 0.1), DOUBLE_EPSILON);
        Assertions.assertEquals(0, Utilities.stopInDeadband(.09, 0.1), DOUBLE_EPSILON);
        Assertions.assertEquals(0, Utilities.stopInDeadband(-.09, 0.1), DOUBLE_EPSILON);
    }

    @Test
    public void testBoundingMath()
    {
        Assertions.assertEquals(1, Utilities.boundAngle0to360Degrees(361), DOUBLE_EPSILON);
        Assertions.assertEquals(359, Utilities.boundAngle0to360Degrees(-361), DOUBLE_EPSILON);

        Assertions.assertEquals(1, Utilities.boundAngleNeg180to180Degrees(361), DOUBLE_EPSILON);
        Assertions.assertEquals(-1, Utilities.boundAngleNeg180to180Degrees(-361), DOUBLE_EPSILON);

        Assertions.assertEquals(.1, Utilities.boundAngle0to2PiRadians(Math.PI * 6 + .1), DOUBLE_EPSILON);
        Assertions.assertEquals(2 * Math.PI - .1, Utilities.boundAngle0to2PiRadians(-Math.PI * 6 - .1), DOUBLE_EPSILON);

        Assertions.assertEquals(.1, Utilities.boundAngleNegPiToPiRadians(Math.PI * 6 + .1), DOUBLE_EPSILON);
        Assertions.assertEquals(-.1, Utilities.boundAngleNegPiToPiRadians(-Math.PI * 6 - .1), DOUBLE_EPSILON);

    }

    @Test
    public void testAngleDiff()
    {
        // Degrees
        Assertions.assertEquals(10, Utilities.getDifferenceInAngleDegrees(0, 10), DOUBLE_EPSILON);
        Assertions.assertEquals(2, Utilities.getDifferenceInAngleDegrees(-1, 1), DOUBLE_EPSILON);
        Assertions.assertEquals(-2, Utilities.getDifferenceInAngleDegrees(1, -1), DOUBLE_EPSILON);

        // Radians
        Assertions.assertEquals(.1, Utilities.getDifferenceInAngleRadians(0, .1), DOUBLE_EPSILON);
        Assertions.assertEquals(.2, Utilities.getDifferenceInAngleRadians(-.1, .1), DOUBLE_EPSILON);
        Assertions.assertEquals(-.2, Utilities.getDifferenceInAngleRadians(.1, -.1), DOUBLE_EPSILON);

    }
}
