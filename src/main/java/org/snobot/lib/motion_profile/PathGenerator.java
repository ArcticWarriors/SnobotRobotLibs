package org.snobot.lib.motion_profile;

import java.util.ArrayList;
import java.util.List;

import org.snobot.lib.motion_profile.PathSetpoint.TrapezoidSegment;

/**
 * Class to generate a path, based on a {@link PathConfig}.
 * 
 * @author PJ
 *
 */
public class PathGenerator
{
    /**
     * Generates a list of setpoints for the given path config.
     * 
     * @param aConfig
     *            The config to use to generate the path
     * 
     * @return The path to run
     */
    public List<PathSetpoint> generate(PathConfig aConfig)
    {
        return generate(aConfig.mMaxVelocity, aConfig.mMaxAcceleration, aConfig.mEndpoint, aConfig.mExpectedDt);
    }

    private List<PathSetpoint> generate(double aMaxVelocity, double aMaxAccel, double aPosition, double aDt)
    {

        double t1 = aMaxVelocity / aMaxAccel;
        double t2 = aPosition / aMaxVelocity;
        double t3 = aMaxVelocity / aMaxAccel + aPosition / aMaxVelocity;

        if (t2 > t1)
        {
            return generateTrapeziodPath(t1, t2, t3, aMaxVelocity, aMaxAccel, aPosition, aDt);
        }
        else
        {
            return generateTrianglePath(aMaxAccel, aPosition, aDt);
        }

    }

    private List<PathSetpoint> generateTrianglePath(double aMaxAccel, double aPosition, double aDt)
    {
        double t1 = Math.sqrt(aPosition / aMaxAccel);
        double t2 = 2 * t1;

        double maxVelocity = t1 * aMaxAccel;

        ArrayList<PathSetpoint> output = new ArrayList<>();

        double pos = 0.0;
        double vel = 0.0;
        for (double t = 0; t < t1; t += aDt)
        {
            pos = .5 * aMaxAccel * (t * t);
            vel = aMaxAccel * t;

            PathSetpoint point = new PathSetpoint(TrapezoidSegment.Acceleration, aDt, pos, vel, aMaxAccel);
            output.add(point);
        }

        for (double t = t1; t < t2; t += aDt)
        {
            pos = aPosition - .5 * aMaxAccel * Math.pow(t - t2, 2);
            vel = maxVelocity - aMaxAccel * (t - t1);

            PathSetpoint point = new PathSetpoint(TrapezoidSegment.Deceleration, aDt, pos, vel, aMaxAccel);
            output.add(point);
        }

        return output;
    }

    private List<PathSetpoint> generateTrapeziodPath(double aT1, double aT2, double aT3, double aMaxVelocity, double aMaxAccel, double aPosition,
            double aDt)
    {
        ArrayList<PathSetpoint> output = new ArrayList<>();

        double pos = 0.0;
        double vel = 0.0;

        for (double t = 0; t < aT1; t += aDt)
        {
            pos = .5 * aMaxAccel * (t * t);
            vel = aMaxAccel * t;
            PathSetpoint point = new PathSetpoint(TrapezoidSegment.Acceleration, aDt, pos, vel, aMaxAccel);
            output.add(point);

        }

        for (double t = aT1; t < aT2; t += aDt)
        {
            pos = .5 * ((aMaxVelocity * aMaxVelocity) / aMaxAccel) + aMaxVelocity * (t - (aMaxVelocity / aMaxAccel));
            vel = aMaxVelocity;
            PathSetpoint point = new PathSetpoint(TrapezoidSegment.ConstantVelocity, aDt, pos, vel, 0);
            output.add(point);
        }

        for (double t = aT2; t < aT3; t += aDt)
        {
            pos = aPosition - .5 * aMaxAccel * Math.pow(t - (aT1 + aT2), 2);
            vel = aMaxAccel * ((aMaxVelocity / aMaxAccel) + (aPosition / aMaxVelocity) - t);
            PathSetpoint point = new PathSetpoint(TrapezoidSegment.Deceleration, aDt, pos, vel, -aMaxAccel);
            output.add(point);
        }

        if (output.get(output.size() - 1).mVelocity != 0)
        {
            output.add(new PathSetpoint(TrapezoidSegment.Deceleration, aDt, aPosition, 0, 0));
        }

        return output;
    }
}
