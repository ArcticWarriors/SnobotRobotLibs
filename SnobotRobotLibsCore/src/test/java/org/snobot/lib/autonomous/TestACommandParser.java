package org.snobot.lib.autonomous;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.lib.test.utilities.BaseTest;
import org.snobot.lib.test.utilities.MockSnobot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestACommandParser extends BaseTest
{
    private static class TestCommandA extends Command
    {
        private static TestCommandA parseCommand(List<String> aArgs, MockSnobot aRobotType)
        {
            Double.parseDouble(aArgs.get(1));
            Double.parseDouble(aArgs.get(2));
            Double.parseDouble(aArgs.get(3));

            return new TestCommandA();
        }

        @Override
        protected boolean isFinished()
        {
            return true;
        }
    }

    private static class TestCommandB extends Command
    {
        private static TestCommandB parseCommand(List<String> aArgs, MockSnobot aRobotType)
        {
            Double.parseDouble(aArgs.get(1));
            Double.parseDouble(aArgs.get(2));

            return new TestCommandB();
        }

        @Override
        protected boolean isFinished()
        {
            return true;
        }
    }

    private static class MockAutonCommandFactory extends AAutonomousCommandFactory<MockSnobot>
    {
        private MockAutonCommandFactory()
        {
            registerCreator("TestCommandA", TestCommandA::parseCommand);
            registerCreator("TestCommandB", TestCommandB::parseCommand);
        }

    }

    private static class TestableCommandParser extends ACommandParser<MockSnobot>
    {
        private TestableCommandParser(MockSnobot aSnobot)
        {
            super(NetworkTableInstance.getDefault().getTable("FakeTable").getEntry("text"),
                    NetworkTableInstance.getDefault().getTable("FakeTable").getEntry("parsed"),
                    NetworkTableInstance.getDefault().getTable("FakeTable").getEntry("filename"), 
                    " ", "#",
                    "WaitName", "ParallelName",
                    new MockAutonCommandFactory(), aSnobot);
        }
    }

    @Test
    public void testInvalidFile()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("doesnt_exist");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertFalse(parser.wasParsingSuccesful());
    }

    @Test
    public void testEmptyFile()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("testFiles/autonParser/empty.auto");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertTrue(parser.wasParsingSuccesful());
    }

    @Test
    public void testWaitCommand()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("testFiles/autonParser/wait.auto");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertTrue(parser.wasParsingSuccesful());
    }

    @Test
    public void testParallelCommand()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("testFiles/autonParser/parallel.auto");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertTrue(parser.wasParsingSuccesful());
    }

    @Test
    public void testUnknownCommandName()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("testFiles/autonParser/unknownCommandName.auto");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertFalse(parser.wasParsingSuccesful());
    }

    @Test
    public void testInvalidParameters()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        CommandGroup autonCommand = parser.readFile("testFiles/autonParser/testInvalidParameters.auto");

        Assertions.assertNotNull(autonCommand);
        Assertions.assertFalse(parser.wasParsingSuccesful());
    }

    @Test
    public void testSave()
    {
        MockSnobot snobot = new MockSnobot();
        TestableCommandParser parser = new TestableCommandParser(snobot);
        parser.readFile("testFiles/autonParser/parallel.auto");
        parser.saveAutonMode();
    }
}
