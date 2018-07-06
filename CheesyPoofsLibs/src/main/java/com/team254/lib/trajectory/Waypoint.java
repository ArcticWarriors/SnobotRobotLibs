package com.team254.lib.trajectory;


public class Waypoint
{

    public Waypoint(double x, double y, double theta)
    {
        // Switched on purpose, poofs code wants it this way
        this.x = y;
        this.y = x;
        this.theta = Math.toRadians(theta);
    }

    public Waypoint(Waypoint tocopy)
    {
        this.x = tocopy.x;
        this.y = tocopy.y;
        this.theta = tocopy.theta;
    }

    public double x;
    public double y;
    public double theta;

    @Override
    public String toString()
    {
        return "Waypoint [x=" + x + ", y=" + y + ", theta=" + Math.toDegrees(theta) + "]";
    }

}