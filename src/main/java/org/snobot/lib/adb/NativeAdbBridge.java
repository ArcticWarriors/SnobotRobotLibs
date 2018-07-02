package org.snobot.lib.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;

public class NativeAdbBridge extends BaseAdbBridge
{
    protected final Path mAdbLocation;
    protected final boolean mValidAdb;

    /**
     * Constructor. Kills the old ADB's and starts up a new one
     * 
     * @param aAdbLocation
     *            The absolute location of the ADB binary
     * @param aAppPackage
     *            The apps package, used to restart the app
     * @param aAppMainActivity
     *            The apps main activity, used to restart the app
     * @param aKillOldAdbs
     *            If set, this will kill all of the currently running ADB
     *            processes
     */
    public NativeAdbBridge(String aAdbLocation, String aAppPackage, String aAppMainActivity, boolean aKillOldAdbs)
    {
        super(aAppPackage, aAppMainActivity);

        mAdbLocation = Paths.get(aAdbLocation);
        mValidAdb = Files.exists(mAdbLocation);
        
        if (aKillOldAdbs)
        {
            killOldAdbs();
        }

        if (!mValidAdb)
        {
            sLOGGER.log(Level.ERROR, "ADB could not be found at '" + aAdbLocation + "'");
        }
    }

    private void killOldAdbs()
    {
        if (System.getProperty("os.name").startsWith("Windows"))
        {
            try
            {
                Process process = Runtime.getRuntime().exec("tasklist");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null)
                {

                    if (line.contains("adb"))
                    {
                        sLOGGER.log(Level.ERROR, "Found running ADB, killing it");
                        Runtime.getRuntime().exec("taskkill /F /IM adb.exe");
                        // killProcess.wait(1000);
                    }
                }
                reader.close();
                sLOGGER.info("Killed old ADB's");
            }
            catch (IOException ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }
    }

    @Override
    public void start()
    {
        sLOGGER.log(Level.INFO, "Starting ADB");
        runCommand("start-server");
    }

    @Override
    public void stop()
    {
        sLOGGER.log(Level.INFO, "Stoping ADB");
        runCommand("kill-server");
    }

    @Override
    protected boolean runCommand(String aArgs)
    {
        if (!mValidAdb)
        {
            sLOGGER.log(Level.ERROR, "ADB Location is not valid, cannot run commands!");
            return false;
        }

        Runtime runtime = Runtime.getRuntime();
        String cmd = mAdbLocation.toString() + " " + aArgs;

        boolean success = false;

        try
        {
            sLOGGER.log(Level.INFO, "Running ADB Command: " + cmd);

            Process process = runtime.exec(cmd);
            success = process.waitFor(10, TimeUnit.SECONDS);
        }
        catch (IOException | InterruptedException ex)
        {
            sLOGGER.log(Level.ERROR, "Could not run command: " + cmd, ex);
        }

        return success;
    }

}
