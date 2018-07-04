package org.snobot.lib.test.utilities;

import org.junit.jupiter.api.BeforeEach;

import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseTest
{
    private static boolean INITIALIZED = false;

    /**
     * Called before each unit test is run.
     */
    @BeforeEach
    public void setup()
    {
        if (!INITIALIZED)
        {
            DefaultDataAccessorFactory.initalize();
            RobotBase.initializeHardwareConfiguration();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().setLogLevel(SnobotLogLevel.DEBUG);

            INITIALIZED = true;
        }
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
    }
}
