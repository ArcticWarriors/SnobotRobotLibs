package org.snobot.lib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.lib.PropertyManager.BooleanProperty;
import org.snobot.lib.PropertyManager.DoubleProperty;
import org.snobot.lib.PropertyManager.IntegerProperty;
import org.snobot.lib.PropertyManager.StringProperty;
import org.snobot.lib.test.utilities.BaseTest;

import edu.wpi.first.wpilibj.Preferences;


public class TestPropertyManager extends BaseTest
{
    private static final double DOUBLE_EPSILON = .00001;

    @Test
    public void testProperties()
    {

        BooleanProperty boolProp = new BooleanProperty("Boolean", false);
        Assertions.assertEquals("Boolean", boolProp.getKey());
        Assertions.assertEquals(boolProp.getValue(), false);
        Preferences.getInstance().putBoolean("Boolean", true);
        Assertions.assertEquals(boolProp.getValue(), true);

        DoubleProperty doubleProp = new DoubleProperty("Double", .1);
        Assertions.assertEquals("Double", doubleProp.getKey());
        Assertions.assertEquals(doubleProp.getValue(), .1, DOUBLE_EPSILON);
        Preferences.getInstance().putDouble("Double", .5);
        Assertions.assertEquals(doubleProp.getValue(), .5, DOUBLE_EPSILON);

        IntegerProperty intProp = new IntegerProperty("Integer", 174);
        Assertions.assertEquals("Integer", intProp.getKey());
        Assertions.assertEquals(intProp.getValue().intValue(), 174);
        Preferences.getInstance().putInt("Integer", 191);
        Assertions.assertEquals(intProp.getValue().intValue(), 191);

        StringProperty stringProp = new StringProperty("String", "Hello");
        Assertions.assertEquals("String", stringProp.getKey());
        Assertions.assertEquals(stringProp.getValue(), "Hello");
        Preferences.getInstance().putString("String", "World");
        Assertions.assertEquals(stringProp.getValue(), "World");
    }

    @Test
    public void testDefaultConstruction()
    {
        BooleanProperty boolProp = new BooleanProperty("Boolean2");
        StringProperty stringProp = new StringProperty("String2");

        Assertions.assertEquals(boolProp.getValue(), false);
        Assertions.assertEquals(stringProp.getValue(), "");
    }

}
