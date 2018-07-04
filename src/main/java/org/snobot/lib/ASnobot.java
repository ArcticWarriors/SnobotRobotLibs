package org.snobot.lib;

import java.util.ArrayList;
import java.util.List;

import org.snobot.lib.logging.CsvLogger;
import org.snobot.lib.modules.IControllableModule;
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
    private final List<ISmartDashboardUpdaterModule> mSmartDashboardModules;

    private final CsvLogger mCsvLogger;

    // Autonomous
    protected CommandGroup mAutonCommand;

    /**
     * Constructor.
     */
    public ASnobot()
    {
        mUpdateableModules = new ArrayList<>();
        mControllableModules = new ArrayList<>();
        mSmartDashboardModules = new ArrayList<>();

        mCsvLogger = new CsvLogger();
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
        mCsvLogger.writeRow();

    }

    @Override
    public void autonomousInit()
    {
        if (mAutonCommand != null)
        {
            mAutonCommand.cancel();
        }

        mAutonCommand = createAutonomousCommand();

        if (mAutonCommand != null)
        {
            mAutonCommand.start();
        }
    }

    @Override
    public void robotPeriodic()
    {
        updateSmartDashboard();
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
        mCsvLogger.writeRow();
    }

    @Override
    public void disabledInit()
    {
        mCsvLogger.flush();
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

    protected abstract CommandGroup createAutonomousCommand();

}
