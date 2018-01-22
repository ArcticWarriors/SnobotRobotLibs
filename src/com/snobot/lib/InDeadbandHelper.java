package com.snobot.lib;

public class InDeadbandHelper
{
    private final int mLoopsRequired;
    private int mLoopsSatisfied;

    public InDeadbandHelper(int aLoopsRequired)
    {
        mLoopsRequired = aLoopsRequired;
    }

    /**
     * Checks if enough loops have been satisfied.
     * 
     * @param aInRange
     *            If the measurement is currently in range
     * @return True if the target has been in range for enough loops
     */
    public boolean isFinished(boolean aInRange)
    {
        if (aInRange)
        {
            mLoopsSatisfied++;
        }
        else
        {
            mLoopsSatisfied = 0;
        }

        return mLoopsSatisfied >= mLoopsRequired;
    }

    /**
     * Gets the loops the target has been inside an acceptable range.
     * 
     * @return number of loops
     */
    public int inDeadband()
    {
        return mLoopsSatisfied;
    }
}
