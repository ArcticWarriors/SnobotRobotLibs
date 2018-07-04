package org.snobot.lib;

/**
 * This class holds a bunch of static methods and variables needed for
 * mathematics.
 */
public final class Utilities
{

    private Utilities()
    {
    }

    public static double getDifferenceInAngleRadians(double aFrom, double aTo)
    {
        return boundAngleNegPiToPiRadians(aTo - aFrom);
    }

    public static double getDifferenceInAngleDegrees(double aFrom, double aTo)
    {
        return boundAngleNeg180to180Degrees(aTo - aFrom);
    }

    public static double boundAngle0to360Degrees(double aAngle)
    {
        return wrap(aAngle, 0, 360);
    }

    public static double boundAngleNeg180to180Degrees(double aAngle)
    {

        return wrap(aAngle, -180, 180);
    }

    public static double boundAngle0to2PiRadians(double aAngle)
    {
        return wrap(aAngle, 0, 2 * Math.PI);
    }

    public static double boundAngleNegPiToPiRadians(double aSngle)
    {
        return wrap(aSngle, -Math.PI, Math.PI);
    }

    /**
     * Wraps a value between the given boundaries.
     * 
     * @param aValue
     *            The input value to wrap
     * @param aMin
     *            The minimum value the output should not be below
     * @param aMax
     *            The maximum value the output should not be above
     * @return The altered value
     */
    public static double wrap(double aValue, double aMin, double aMax)
    {
        double diff = aMax - aMin;
        double output = aValue;
        while (output >= aMax)
        {
            output -= diff;
        }

        while (output < aMin)
        {
            output += diff;
        }

        return output;
    }

    /**
     * Clamps a value if it is within the given deadband.
     * 
     * @param aInValue
     *            The input value
     * @param aDeadband
     *            The deadband (+/-)
     * @return The clamped value
     */
    public static double stopInDeadband(double aInValue, double aDeadband)
    {
        if (aInValue <= aDeadband && aInValue >= -aDeadband)
        {
            return 0;
        }
        return aInValue;
    }
}
