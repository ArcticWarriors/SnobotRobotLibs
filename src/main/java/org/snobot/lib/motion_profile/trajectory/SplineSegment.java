package org.snobot.lib.motion_profile.trajectory;

public class SplineSegment
{

    /** Position of the left wheel. */
    public double mLeftSidePosition;

    /** Velocity of the left wheel. */
    public double mLeftSideVelocity;

    /** Position of the right wheel. */
    public double mRightSidePosition;

    /** Velocity of the right wheel. */
    public double mRightSideVelocity;

    /** Heading of the robot, in degrees. */
    public double mRobotHeading;

    /** The X position between the left and right wheels. */
    public double mAverageX;

    /** The Y position between the left and right wheels. */
    public double mAverageY;

    public SplineSegment()
    {
        this(0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Constructor.
     * 
     * @param aLeftPos
     *            Left wheel position
     * @param aLeftVel
     *            Left wheel velocity
     * @param aRightPos
     *            Right wheel position
     * @param aRightVel
     *            Right wheel velocity
     * @param aHeading
     *            Robot heading
     * @param aAvgX
     *            Average X position
     * @param aAvgY
     *            Average Y Position
     */
    public SplineSegment(double aLeftPos, double aLeftVel, double aRightPos, double aRightVel, double aHeading, double aAvgX, double aAvgY)
    {
        this.mLeftSidePosition = aLeftPos;
        this.mLeftSideVelocity = aLeftVel;
        this.mRightSidePosition = aRightPos;
        this.mRightSideVelocity = aRightVel;
        this.mRobotHeading = aHeading;
        this.mAverageX = aAvgX;
        this.mAverageY = aAvgY;
    }
}
