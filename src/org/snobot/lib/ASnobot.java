package org.snobot.lib;

import java.util.ArrayList;
import java.util.List;

import org.snobot.lib.logging.ILogger;
import org.snobot.lib.logging.Logger;
import org.snobot.lib.modules.IControllableModule;
import org.snobot.lib.modules.ILoggableModule;
import org.snobot.lib.modules.ISmartDashboardUpdaterModule;
import org.snobot.lib.modules.ISubsystem;
import org.snobot.lib.modules.IUpdateableModule;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public abstract class ASnobot extends IterativeRobot implements ISubsystem
{

    private final List<IUpdateableModule> mUpdateableModules;
    private final List<IControllableModule> mControllableModules;
    private final List<ILoggableModule> mLoggableModules;
    private final List<ISmartDashboardUpdaterModule> mSmartDashboardModules;

    private final Logger mLogger; // NOPMD

    // Autonomous
    private CommandGroup mAutonCommand;

    /**
     * Constructor.
     */
    public ASnobot()
    {
        mUpdateableModules = new ArrayList<>();
        mControllableModules = new ArrayList<>();
        mLoggableModules = new ArrayList<>();
        mSmartDashboardModules = new ArrayList<>();

        mLogger = new Logger();
    }

    protected void addModule(Object aModule)
    {
        if (aModule instanceof IUpdateableModule)
        {
            mUpdateableModules.add((IUpdateableModule) aModule);
        }
        if (aModule instanceof IControllableModule)
        {
            mControllableModules.add((IControllableModule) aModule);
        }
        if (aModule instanceof ILoggableModule)
        {
            mLoggableModules.add((ILoggableModule) aModule);
        }
        if (aModule instanceof ISmartDashboardUpdaterModule)
        {
            mSmartDashboardModules.add((ISmartDashboardUpdaterModule) aModule);
        }

    }

    @Override
    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();

        update();
        updateSmartDashboard();
        updateLog();

    }

    @Override
    public void autonomousInit()
    {
        mAutonCommand = createAutonomousCommand();

        if (mAutonCommand != null)
        {
            mAutonCommand.start();
        }
    }

    @Override
    public void teleopInit()
    {
        if (mAutonCommand != null)
        {
            mAutonCommand.cancel();
            Scheduler.getInstance().run();
        }
    }

    @Override
    public void teleopPeriodic()
    {
        update();
        control();
        updateSmartDashboard();
        updateLog();
    }

    @Override
    public void disabledInit()
    {
        mLogger.flush();
    }

    @Override
    public void disabledPeriodic()
    {
        updateSmartDashboard();
    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.initializeLogger();
        for (ILoggableModule subsystem : mLoggableModules)
        {
            subsystem.initializeLogHeaders();
        }
        mLogger.endHeader();
        mAutonCommand = createAutonomousCommand();
    }

    @Override
    public void update()
    {
        for (IUpdateableModule subsystem : mUpdateableModules)
        {
            subsystem.update();

        }
    }

    @Override
    public void control()
    {
        for (IControllableModule subsystem : mControllableModules)
        {
            subsystem.control();
        }
    }

    @Override
    public void updateLog()
    {
        if (mLogger.logNow())
        {
            mLogger.startRow();

            for (ILoggableModule subsystem : mLoggableModules)
            {
                subsystem.updateLog();
            }
            mLogger.endRow();
        }

    }

    @Override
    public void updateSmartDashboard()
    {
        for (ISmartDashboardUpdaterModule subsystem : mSmartDashboardModules)
        {
            subsystem.updateSmartDashboard();
        }
    }

    @Override
    public void stop()
    {
        for (IControllableModule subsystem : mControllableModules)
        {
            subsystem.stop();
        }
    }

    protected ILogger getLogger()
    {
        return mLogger;
    }

    protected abstract CommandGroup createAutonomousCommand();

}
