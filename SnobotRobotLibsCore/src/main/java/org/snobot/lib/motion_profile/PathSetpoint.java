package org.snobot.lib.motion_profile;

public class PathSetpoint
{

    /**
     * Lists the segments that are acheived during a trapezoidal motion profile.
     */
    public enum TrapezoidSegment
    {
        Acceleration, ConstantVelocity, Deceleration
    }

    /** The segment of the profile this segment is in. */
    public TrapezoidSegment mSegment;

    /** The desired position for this setpoint. */
    public double mPosition;

    /** The desired velocity for this setpoint. */
    public double mVelocity;

    /** The desired acceleration for this setpoint. */
    public double mAcceleration;

    public PathSetpoint()
    {
        this(TrapezoidSegment.Acceleration, .02, 0, 0, 0);
    }

    /**
     * Constructor.
     * 
     * @param aSegment
     *            Segment
     * @param aDt
     *            Time Delta
     * @param aPosition
     *            Position
     * @param aVelocity
     *            Velocity
     * @param aAccel
     *            Acceleration
     */
    public PathSetpoint(TrapezoidSegment aSegment, double aDt, double aPosition, double aVelocity, double aAccel)
    {
        mSegment = aSegment;
        mPosition = aPosition;
        mVelocity = aVelocity;
        mAcceleration = aAccel;
    }

    @Override
    public String toString()
    {
        return "PathSetpoint [mSegment=" + mSegment + ", mPosition=" + mPosition + ", mVelocity=" + mVelocity + ", mAcceleration=" + mAcceleration
                + "]";
    }

}
