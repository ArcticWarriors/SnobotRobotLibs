package com.snobot.lib.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class LogFormatter extends Formatter
{
    private static final Logger sLOGGER = Logger.getLogger("ALogger");

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord aRecord)
    {
        StringBuilder sb = new StringBuilder();

        Date date = new Date(aRecord.getMillis());
        SimpleDateFormat df = new SimpleDateFormat("y/M/d H:m:s", Locale.getDefault());

        sb.append(df.format(date)).append("\t ").append(aRecord.getLevel().getLocalizedName()).append("   \t: ")
                .append(formatMessage(aRecord)).append(LINE_SEPARATOR);

        if (aRecord.getThrown() != null)
        {
            try
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                aRecord.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception ex)
            {
                sLOGGER.log(Level.SEVERE, "", ex);
            }
        }

        return sb.toString();
    }
}
