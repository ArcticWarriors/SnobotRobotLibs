package org.snobot.lib.autonomous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public abstract class ACommandParser
{
    protected static final Logger sLOGGER = LogManager.getLogger("ACommandParser");

    protected final NetworkTableEntry mAutonSdTableTextName;
    protected final NetworkTableEntry mAutonSdTableParsedTextName;

    protected final String mDelimiter;
    protected final String mCommentStart;

    protected String mErrorText;
    protected boolean mSuccess;

    /**
     * Constructor.
     * 
     * @param aAutonSdTableTextName
     *            The property used to show the autonomous script
     * @param aAutonSdTableParsedTextName
     *            The property used to show if the script was succesfully parsed
     *            (boolean)
     * @param aDelimiter
     *            The delimeter used between key words
     * @param aCommentStart
     *            The characters representing a line comment start
     */
    public ACommandParser(
            NetworkTableEntry aAutonSdTableTextName, NetworkTableEntry aAutonSdTableParsedTextName,
            String aDelimiter, String aCommentStart)
    {
        mAutonSdTableTextName = aAutonSdTableTextName;
        mAutonSdTableParsedTextName = aAutonSdTableParsedTextName;
        mDelimiter = aDelimiter;
        mCommentStart = aCommentStart;

        mErrorText = "";
        mSuccess = false;
    }

    protected void addError(String aError)
    {
        // Put the '#' so we can pretend like the error text is a comment
        mErrorText += mCommentStart + aError + "\n";
        mSuccess = false;
    }

    protected void initReading()
    {
        mSuccess = true;
        mErrorText = "";
    }

    /**
     * Specialization wrapper for a command group. Simply will print out that
     * the command group has finished
     * 
     * @param aName
     *            Name of the command group
     * @return The newly created command group
     */
    protected CommandGroup createNewCommandGroup(String aName)
    {
        return new CommandGroup(aName)
        {
            @Override
            protected void end()
            {
                super.end();
                sLOGGER.log(Level.INFO, "Command group '" + aName + "' finished!");
            }
        };
    }

    /**
     * Interprets a line as a Command and adds it to mCommands.
     * 
     * @param aGroup
     *            The command group to add the new command to
     * @param line
     *            Line of text
     * @param aAddParallel
     *            If the command should be added in parallel or series
     */
    protected void parseLine(CommandGroup aGroup, String aLine, boolean aAddParallel)
    {
        String line = aLine.trim();
        if (line.isEmpty() || line.startsWith(mCommentStart))
        {
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(line, mDelimiter);

        List<String> args = new ArrayList<>();

        while (tokenizer.hasMoreElements())
        {
            args.add(tokenizer.nextToken());
        }

        Command newCommand = parseCommand(args);

        if (newCommand == null)
        {
            mSuccess = false;
        }
        else
        {
            if (aAddParallel)
            {
                aGroup.addParallel(newCommand);
            }
            else
            {
                aGroup.addSequential(newCommand);
            }
        }
    }

    /**
     * Parses a parallel command (commands separated by '|').
     * 
     * @param aArgs
     *            The list of arguments
     * @return The command group for the parallel command
     */
    protected CommandGroup parseParallelCommand(List<String> aArgs)
    {
        StringBuilder parallelLine = new StringBuilder();
        for (int i = 1; i < aArgs.size(); ++i)
        {
            parallelLine.append(aArgs.get(i)).append(' ');
        }

        String[] splitCommands = parallelLine.toString().split("\\|");
        CommandGroup parallelCommands = new CommandGroup();

        for (String thisLine : splitCommands)
        {
            parseLine(parallelCommands, thisLine, true);
        }

        return parallelCommands;
    }

    protected Command parseWaitCommand(List<String> aArgs)
    {
        double time = Double.parseDouble(aArgs.get(1));
        return new WaitCommand(time);
    }

    /**
     * Reads the given file into autonomous commands.
     * 
     * @param aFilePath
     *            The path to the file to read
     * @return The constructed command group to run
     */
    public CommandGroup readFile(String aFilePath)
    {
        initReading();

        CommandGroup output = createNewCommandGroup(aFilePath);

        StringBuilder fileContents = new StringBuilder();

        File file = new File(aFilePath);

        if (file.exists())
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(aFilePath));

                String line;
                while ((line = br.readLine()) != null)
                {
                    this.parseLine(output, line, false);
                    fileContents.append(line).append('\n');
                }

                br.close();
            }
            catch (Exception ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }
        else
        {
            addError("File " + aFilePath + " not found!");
        }

        publishParsingResults(fileContents.toString());

        return output;
    }

    protected void publishParsingResults(String aCommandString) // NOPMD
    {
        if (!mErrorText.isEmpty())
        {
            aCommandString += "\n\n# There was an error parsing the commands...\n#\n";
            aCommandString += mErrorText;
        }

        mAutonSdTableTextName.setString(aCommandString);
        mAutonSdTableParsedTextName.setBoolean(mSuccess);
    }

    protected abstract Command parseCommand(List<String> aArgs);
}
