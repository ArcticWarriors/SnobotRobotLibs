package com.team254.lib.trajectory.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Waypoint;
import com.team254.lib.trajectory.gen.TrajectoryGenerator.Config;
import com.team254.lib.trajectory.io.TextFileSerializer;

public class SnobotTrajectoryGen
{

    public Path generate(
            Config aTrajectoryConfig,
            List<Waypoint> aWaypointSequence, 
            File aOutputFile,
            String aPathName,
            double aWheelbasedWidth)
    {
        Path path = PathGenerator.makePath(aWaypointSequence, aTrajectoryConfig, aWheelbasedWidth, aPathName);
        if (aTrajectoryConfig.isBackwards)
        {
            path.makeBackwards();
        }

        // Outputs to the directory supplied as the first argument.
        TextFileSerializer js = new TextFileSerializer();
        String serialized = js.serialize(path);
        // System.out.print(serialized);
        String fullpath = aOutputFile.getAbsolutePath();
        if (!writeFile(fullpath, serialized))
        {
            System.err.println(fullpath + " could not be written!!!!");
            System.exit(1);
        }
        else
        {
            System.out.println("Wrote " + fullpath);
        }

        return path;
    }

    protected boolean writeFile(String path, String data)
    {
        try
        {
            File file = new File(path);

            // if file doesnt exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    protected String joinPath(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
