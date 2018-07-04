package org.snobot.lib;

import org.junit.jupiter.api.Test;
import org.snobot.lib.test.utilities.BaseTest;
import org.snobot.lib.test.utilities.MockSnobot;

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
    }

}
