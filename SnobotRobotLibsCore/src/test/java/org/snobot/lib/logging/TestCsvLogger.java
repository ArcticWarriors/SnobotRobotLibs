package org.snobot.lib.logging;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCsvLogger
{
    private static final String sIGNORE_TEXT = "XXX";

    @Test
    public void testLogger()
    {
        CsvLogger logger = new CsvLogger();

        CsvLogEntry stringEntry = new CsvLogEntry("StringHeader");
        CsvLogEntry intEntry = new CsvLogEntry("IntHeader");
        CsvLogEntry doubleEntry = new CsvLogEntry("DoubleHeader");
        CsvLogEntry booleanEntry = new CsvLogEntry("BooleanHeader");

        logger.addEntry(stringEntry);
        logger.addEntry(intEntry);
        logger.addEntry(doubleEntry);
        logger.addEntry(booleanEntry);

        logger.startLogging("TestLog_Valid.csv", new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault()), ".");

        // Update each entry and write the row
        stringEntry.update("String Value");
        intEntry.update(174);
        doubleEntry.update(1.91);
        booleanEntry.update(false);
        logger.writeRow();

        // Update some of the entries
        stringEntry.update("Another String");
        booleanEntry.update(true);
        logger.writeRow();

        // Flush to verify with tests
        logger.flush();

        verifyFile("TestLog_Valid.csv", new String[][] {
            new String[] {"Date and Time", "StringHeader", "IntHeader", "DoubleHeader", "BooleanHeader"},
            new String[] {sIGNORE_TEXT, "String Value", "174", "1.91", "0"},
            new String[] {sIGNORE_TEXT, "Another String", "UNSET", "UNSET", "1"},
        });

        // Restart log with different name
        logger.startLogging("TestLog_NewName.csv", new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault()), ".");

        // Update each entry and write the row
        stringEntry.update("New String Value");
        intEntry.update(111);
        doubleEntry.update(0.45);
        booleanEntry.update(true);
        logger.writeRow();

        // Flush to verify with tests
        logger.flush();

        verifyFile("TestLog_NewName.csv", new String[][] {
            new String[] {"Date and Time", "StringHeader", "IntHeader", "DoubleHeader", "BooleanHeader"},
            new String[] {sIGNORE_TEXT, "New String Value", "111", "0.45", "1"},
        });
    }

    @Test
    public void testLoggerWithBadDirectory()
    {
        CsvLogger logger = new CsvLogger();
        logger.startLogging("TestLog_Invalid.csv", new SimpleDateFormat("", Locale.getDefault()), "does_not_exist");
        logger.flush();
    }

    private void verifyFile(String aFilename, String[][] aExpectedResults) // NOPMD
    {
        try (BufferedReader br = new BufferedReader(new FileReader(aFilename)))
        {
            String line;
            int rowCtr = 0;
            while ((line = br.readLine()) != null)
            {
                String[] tokens = line.split(",");
                
                for (int i = 0; i < tokens.length; ++i)
                {
                    if (!sIGNORE_TEXT.equals(aExpectedResults[rowCtr][i]))
                    {
                        Assertions.assertEquals(aExpectedResults[rowCtr][i], tokens[i]);
                    }
                }
                
                ++rowCtr;
            }
        }
        catch (IOException ex)
        {
            Assertions.assertFalse(true, "Error reading file");
        }
    }
}
