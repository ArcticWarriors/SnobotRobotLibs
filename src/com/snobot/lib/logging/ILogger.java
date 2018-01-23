package com.snobot.lib.logging;

import java.text.SimpleDateFormat;

public interface ILogger
{

    /**
     * Initializes the internal components of the loggers.
     */
    void initializeLogger();

    /**
     * Adds a new header to represent logged data.
     * 
     * @param aHeader
     *            Header for an entry
     */
    void addHeader(String aHeader);

    /**
     * Stops accepting new headers.
     */
    void endHeader();

    /**
     * Forces a flush of the file writer.
     */
    void flush();

    /**
     * Updates log information.
     * 
     * @param aEntry
     *            Entry to log
     */
    void updateLogger(String aEntry);

    /**
     * Updates log information.
     * 
     * @param aEntry
     *            Entry to log
     */
    void updateLogger(int aEntry);

    /**
     * Updates log information.
     * 
     * @param aEntry
     *            Entry to log
     */
    void updateLogger(double aEntry);

    /**
     * Updates log information.
     * 
     * @param aEntry
     *            Entry to log
     */
    void updateLogger(boolean aEntry);

    /**
     * Begins accepting new log entries.
     */
    void startRow();

    /**
     * Stops accepting log entries.
     */
    void endRow();

    /**
     * Starts logging to the given path.
     * 
     * @param aLogFormat
     *            The formatter to use with the current time
     * @param aLogPath
     *            The path of the file to create
     */
    void startLogging(SimpleDateFormat aLogFormat, String aLogPath);

}
