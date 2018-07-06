package org.snobot.lib.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CsvLogger
{

    protected static final Logger sLOGGER = LogManager.getLogger(CsvLogger.class);

    private boolean mIsActive;
    private FileWriter mLogWriter;
    private SimpleDateFormat mLogDateFormat;

    private final Map<String, CsvLogEntry> mLogEntries;

    public CsvLogger()
    {
        mLogEntries = new LinkedHashMap<>();
    }
    
    /**
     * Adds an column to the log file.
     * 
     * @param aEntry
     *            The column entry
     */
    public void addEntry(CsvLogEntry aEntry)
    {
        if (mLogEntries.containsKey(aEntry.getName()))
        {
            sLOGGER.log(Level.ERROR, "You have already allocated a log entry for '" + aEntry.getName() + "'");
        }
        else
        {
            mLogEntries.put(aEntry.getName(), aEntry);
        }
    }

    /**
     * Writes all of the log entries into a csv row.
     */
    public void writeRow()
    {
        if (!mIsActive)
        {
            return;
        }

        StringBuilder builder = new StringBuilder(128);

        String timeString = mLogDateFormat.format(new Date());
        builder.append(timeString);

        for (CsvLogEntry entry : mLogEntries.values())
        {
            builder.append(',').append(entry.getLastValue());
        }
        builder.append('\n');

        writeToFile(builder.toString());
    }

    /**
     * Starts writing the log file.
     * 
     * @param aFilename
     *            The file name to use
     * @param aLogFormat
     *            The time format for the logger
     * @param aLogPath
     *            The log directory
     */
    public void startLogging(String aFilename, SimpleDateFormat aLogFormat, String aLogPath)
    {
        mIsActive = true;
        mLogDateFormat = aLogFormat;

        try
        {
            File dir = new File(aLogPath);
            mIsActive = dir.exists();
            if (mIsActive)
            {
                mLogWriter = new FileWriter(aLogPath + "/" + aFilename);
                writeHeaders();
            }
            else
            {
                sLOGGER.log(Level.ERROR, "ERROR CREATING LOGGER: Path to '" + aLogPath + "' does not exist.  Bailing");
            }

        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
            stop();
        }
    }

    private void writeHeaders()
    {
        StringBuilder builder = new StringBuilder(128);
        builder.append("Date and Time");
        for (String header : mLogEntries.keySet())
        {
            builder.append(',').append(header);
        }
        builder.append('\n');

        writeToFile(builder.toString());
    }

    private void writeToFile(String aText)
    {
        try
        {
            if (mLogWriter != null)
            {
                mLogWriter.write(aText);
            }

        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
            this.stop();
            mLogWriter = null;
        }
    }

    /**
     * Flushes the file writer.
     */
    public void flush()
    {
        try
        {
            if (mLogWriter != null)
            {
                mLogWriter.flush();
            }
        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
            this.stop();
            mLogWriter = null;
        }
    }

    /**
     * Closes file-stream.
     */
    private void stop()
    {
        mIsActive = false;
        try
        {
            if (mLogWriter != null)
            {
                mLogWriter.close();
            }
        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
            mLogWriter = null;
        }
    }

}
