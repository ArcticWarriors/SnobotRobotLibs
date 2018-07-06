package org.snobot.lib.adb;

import org.apache.logging.log4j.Level;
import org.spectrum3847.RIOdroid.RIOdroid;

public class RioDroidAdbBridge extends BaseAdbBridge
{
    public RioDroidAdbBridge(String aAppPackage, String aAppMainActivity)
    {
        super(aAppPackage, aAppMainActivity);
    }

    @Override
    protected boolean runCommand(String aCommand)
    {
        boolean success = false;
        
        String command = "adb " + aCommand;

        try
        {
            sLOGGER.log(Level.INFO, "Running command: " + command);
            RIOdroid.executeCommand(command);
            success = true;
        }
        catch (Exception ex)
        {
            sLOGGER.log(Level.WARN, "Could not run command: " + command, ex);
        }

        return success;
    }

    @Override
    public void start()
    {
        sLOGGER.log(Level.INFO, "Starting ADB");
        RIOdroid.initUSB();
    }

    @Override
    public void stop()
    {
        sLOGGER.log(Level.INFO, "Stoping ADB");
    }

}
