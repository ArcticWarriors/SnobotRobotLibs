package org.snobot.lib.test.utilities;

import org.snobot.lib.ASnobot;
import org.snobot.lib.modules.IControllableModule;
import org.snobot.lib.modules.ISmartDashboardUpdaterModule;
import org.snobot.lib.modules.IUpdateableModule;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MockSnobot extends ASnobot
{
    public boolean mReturnAutonCommand;

    /**
     * Constructor.
     */
    public MockSnobot()
    {
        TestSDModule sd = new TestSDModule();
        addModule(sd);

        TestControllable control = new TestControllable();
        addModule(control);

        TestUpdatable updatable = new TestUpdatable();
        addModule(updatable);
    }

    @Override
    protected CommandGroup createAutonomousCommand()
    {
        if (mReturnAutonCommand)
        {
            return new CommandGroup("Doesn't do anything");
        }
        return null;
    }

    private static class TestSDModule implements ISmartDashboardUpdaterModule
    {

        @Override
        public void updateSmartDashboard()
        {
            // Nothing to do
        }

    }

    private static class TestControllable implements IControllableModule
    {

        @Override
        public void control()
        {
            // Nothing to do
        }

        @Override
        public void stop()
        {
            // Nothing to do
        }
    }

    private static class TestUpdatable implements IUpdateableModule
    {

        @Override
        public void update()
        {
            // Nothing to do
        }
    }
}
