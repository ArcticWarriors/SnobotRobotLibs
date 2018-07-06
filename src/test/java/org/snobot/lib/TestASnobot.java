package org.snobot.lib;

import org.junit.jupiter.api.Test;
import org.snobot.lib.test.utilities.BaseTest;
import org.snobot.lib.test.utilities.MockSnobot;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.MatchType;

public class TestASnobot extends BaseTest
{

    @Test
    public void testASnobot()
    {
        MockSnobot snobot = new MockSnobot();

        snobot.robotPeriodic();
        snobot.disabledInit();
        snobot.disabledPeriodic();
        snobot.autonomousInit();
        snobot.autonomousPeriodic();
        snobot.teleopInit();
        snobot.teleopPeriodic();
        snobot.testInit();
        snobot.testPeriodic();
        snobot.stop();

        snobot.mReturnAutonCommand = true;
        snobot.autonomousInit();
        snobot.autonomousPeriodic();
        snobot.teleopInit();
        snobot.teleopPeriodic();

        snobot.mReturnAutonCommand = false;
        snobot.autonomousInit();

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setMatchInfo("", MatchType.Qualification, 3, 0, "");

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().waitForNextUpdateLoop();

        // Run for a little bit to get the driver station to take the data
        try
        {
            Thread.sleep(50);
        }
        catch (InterruptedException ex)
        {
            // Ignore
        }

        snobot.disabledPeriodic();
    }

}
