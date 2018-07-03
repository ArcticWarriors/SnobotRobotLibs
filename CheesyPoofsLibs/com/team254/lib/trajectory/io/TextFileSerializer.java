package com.team254.lib.trajectory.io;

import java.util.List;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Segment;
import com.team254.lib.trajectory.Waypoint;

/**
 * Serializes a Path to a simple space and CR separated text file.
 * 
 * @author Jared341
 */
public class TextFileSerializer
{

    /**
     * Format: PathName NumSegments LeftSegment1 ... LeftSegmentN RightSegment1
     * ... RightSegmentN
     * 
     * Each segment is in the format: pos vel acc jerk heading dt x y
     * 
     * @param path
     *            The path to serialize.
     * @return A string representation.
     */
    public String serialize(Path path)
    {
        String content = path.getName() + "\n";
        content += serializeWaypointConfig(path.getPathConfig());
        content += path.getLeftWheelTrajectory().getNumSegments() + "\n";
        content += serializeTrajectory(path.getLeftWheelTrajectory());
        content += serializeTrajectory(path.getRightWheelTrajectory());
        return content;
    }
    
    private String serializeWaypointConfig(List<Waypoint> aWaypointConfig)
    {
        String content = aWaypointConfig.size() + "\n";

        for (Waypoint waypoint : aWaypointConfig)
        {
            content += waypoint.x + "," + waypoint.y + "," + Math.toDegrees(waypoint.theta) + "\n";
        }
        
        return content;
    }

    private String serializeTrajectory(Trajectory trajectory)
    {
        String content = "";
        for (int i = 0; i < trajectory.getNumSegments(); ++i)
        {
            Segment segment = trajectory.getSegment(i);
            content += String.format("%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f\n", segment.pos, segment.vel, segment.acc, segment.jerk,
                    Math.toDegrees(segment.heading), segment.dt, segment.x, segment.y);
        }
        return content;
    }

}