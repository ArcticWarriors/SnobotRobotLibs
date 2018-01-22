package com.snobot.lib.adb;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class BaseAdbBridge implements IAdbBridge
{
    protected static final Logger sLOGGER = Logger.getLogger(BaseAdbBridge.class);

    protected final String mAppPackage;
    protected final String mAppActivity;

    public BaseAdbBridge(String aAppPackage, String aAppMainActivity)
    {
        mAppPackage = aAppPackage;
        mAppActivity = aAppMainActivity;
    }



    @Override
    public void restartAdb()
    {
        sLOGGER.log(Level.INFO, "Restarting ADB");
        stop();
        start();
    }

    @Override
    public void restartApp()
    {
        sLOGGER.log(Level.INFO, "Restarting App");

        runCommand("shell am force-stop " + mAppPackage);
        runCommand("shell am start -n \"" + mAppPackage + "/" + mAppActivity);
    }

    @Override
    public void portForward(int aLocalPort, int aRemotePort)
    {
        runCommand("forward tcp:" + aLocalPort + " tcp:" + aRemotePort);
    }

    @Override
    public void reversePortForward(int aRemotePort, int aLocalPort)
    {
        runCommand("reverse tcp:" + aRemotePort + " tcp:" + aLocalPort);
    }

    /**
     * Runs a command through the ADB.
     * 
     * @param aCommand
     *            The command to run. Does not include 'adb'
     * @return If the command was succesful
     */
    protected abstract boolean runCommand(String aCommand);

}
