package com.team254.lib.trajectory.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Waypoint;

/**
 *
 * @author Jared341
 */
public class TextFileDeserializer implements IPathDeserializer
{

    public Path deserialize(String serialized)
    {
        StringTokenizer tokenizer = new StringTokenizer(serialized, "\n");

        String name = tokenizer.nextToken();

        List<Waypoint> waypoints = getWaypointConfig(tokenizer);

        int num_elements = Integer.parseInt(tokenizer.nextToken());

        // Flipped on purpose
        Trajectory left = new Trajectory(num_elements);
        Trajectory right = new Trajectory(num_elements);

        for (int i = 0; i < num_elements; ++i)
        {
            Trajectory.Segment segment = new Trajectory.Segment();
            StringTokenizer line_tokenizer = new StringTokenizer(tokenizer.nextToken(), " ");

            segment.pos = Double.parseDouble(line_tokenizer.nextToken());
            segment.vel = Double.parseDouble(line_tokenizer.nextToken());
            segment.acc = Double.parseDouble(line_tokenizer.nextToken());
            segment.jerk = Double.parseDouble(line_tokenizer.nextToken());
            segment.heading = Double.parseDouble(line_tokenizer.nextToken());
            segment.dt = Double.parseDouble(line_tokenizer.nextToken());
            segment.x = Double.parseDouble(line_tokenizer.nextToken());
            segment.y = Double.parseDouble(line_tokenizer.nextToken());

            right.setSegment(i, segment);
        }
        for (int i = 0; i < num_elements; ++i)
        {
            Trajectory.Segment segment = new Trajectory.Segment();
            StringTokenizer line_tokenizer = new StringTokenizer(tokenizer.nextToken(), " ");

            segment.pos = Double.parseDouble(line_tokenizer.nextToken());
            segment.vel = Double.parseDouble(line_tokenizer.nextToken());
            segment.acc = Double.parseDouble(line_tokenizer.nextToken());
            segment.jerk = Double.parseDouble(line_tokenizer.nextToken());
            segment.heading = Double.parseDouble(line_tokenizer.nextToken());
            segment.dt = Double.parseDouble(line_tokenizer.nextToken());
            segment.x = Double.parseDouble(line_tokenizer.nextToken());
            segment.y = Double.parseDouble(line_tokenizer.nextToken());

            left.setSegment(i, segment);
        }

        return new Path(name, waypoints, new Trajectory.WheelPair(left, right));
    }

    private List<Waypoint> getWaypointConfig(StringTokenizer tokenizer)
    {
        List<Waypoint> output = new ArrayList<>();

        int numWaypoints = Integer.parseInt(tokenizer.nextToken());

        for (int i = 0; i < numWaypoints; ++i)
        {
            StringTokenizer partsTokenizer = new StringTokenizer(tokenizer.nextToken(), ",");
            double x = Double.parseDouble(partsTokenizer.nextToken());
            double y = Double.parseDouble(partsTokenizer.nextToken());
            double angle = Double.parseDouble(partsTokenizer.nextToken());

            Waypoint waypoint = new Waypoint(x, y, angle);
            output.add(waypoint);

        }

        return output;
    }

    public Path deserializeFromFile(String aFilename)
    {
        File the_file = new File(aFilename);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(the_file));

            StringBuilder fileContents = new StringBuilder();

            String line;

            while ((line = br.readLine()) != null)
            {
                fileContents.append(line + "\n");
            }

            br.close();

            return deserialize(fileContents.toString());
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Could not find the path file.  It should be at : '" + the_file.getAbsolutePath() + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
