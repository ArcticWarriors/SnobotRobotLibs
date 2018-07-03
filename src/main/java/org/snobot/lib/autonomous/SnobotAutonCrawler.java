package org.snobot.lib.autonomous;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SnobotAutonCrawler extends SimpleFileVisitor<Path>
{
    private static final Logger sLOGGER = LogManager.getLogger(SnobotAutonCrawler.class);

    private final List<Path> mPaths;
    private final String mIgnoreString;

    public SnobotAutonCrawler(String aIgnoreString)
    {
        mPaths = new ArrayList<Path>();
        mIgnoreString = aIgnoreString;
    }

    @Override
    public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException
    {
        sLOGGER.log(Level.DEBUG, "  Keeping file " + aFile);
        mPaths.add(aFile);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException
    {
        Path dirName = aDir.getFileName();
        if (dirName != null && dirName.startsWith(mIgnoreString))
        {
            sLOGGER.log(Level.INFO, " Skipping directory: " + dirName);
            return FileVisitResult.SKIP_SUBTREE;
        }
        else
        {
            sLOGGER.log(Level.INFO, " Processing directory: " + dirName);
            return FileVisitResult.CONTINUE;
        }
    }

    /**
     * This is just part of the auton crawler wee lifted from last year. It
     * makes a chooser out of all the files in a specified folder.
     */
    public ObservableSendableChooser<File> loadAutonFiles(String aDir)
    {
        return loadAutonFiles(aDir, null);
    }

    /**
     * Discovers all of the autonomous files in the given location, and
     * populates a sendable chooser with the [filename -> File()] mapping.
     * 
     * @param aDir
     *            The directory to recursively search
     * @param aDefaultName
     *            The name of the default command
     * @return The populated chooser
     */
    public ObservableSendableChooser<File> loadAutonFiles(String aDir, String aDefaultName)
    {
        ObservableSendableChooser<File> output = new ObservableSendableChooser<>();
        File autonDr = new File(aDir);

        if (autonDr.exists())
        {
            sLOGGER.log(Level.INFO, "Reading auton files from directory " + autonDr.getAbsolutePath());
            sLOGGER.log(Level.INFO, " Using filter : \"" + mIgnoreString + "\"");

            try
            {
                Files.walkFileTree(Paths.get(autonDr.toURI()), this);

                boolean isFirst = true;
                for (Path p : mPaths)
                {
                    Path filename = p.getFileName();
                    if (filename == null)
                    {
                        continue;
                    }

                    if ((isFirst && aDefaultName == null) || filename.toString().equals(aDefaultName))
                    {
                        output.addDefault(filename.toString(), p.toFile());
                        isFirst = false;
                    }
                    else
                    {
                        output.addObject(filename.toString(), p.toFile());
                    }
                }
            }
            catch (IOException ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Auton directory " + aDir + " does not exist!");
        }

        return output;
    }

}
