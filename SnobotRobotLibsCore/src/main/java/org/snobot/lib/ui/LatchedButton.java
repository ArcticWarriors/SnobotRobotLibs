package org.snobot.lib.ui;

public class LatchedButton
{
    private boolean mLastState;
    private boolean mCurrentState;

    public LatchedButton()
    {
        mLastState = false;
    }

    /**
     * Updates the button. Should be called once per loop
     * 
     * @param aCurrentState
     *            If the button is currently being pressed
     * @return True if the button has just been latched
     */
    public boolean update(boolean aCurrentState)
    {
        mCurrentState = false;
        if (!mLastState && aCurrentState)
        {
            mCurrentState = true;
        }

        mLastState = aCurrentState;

        return mCurrentState;

    }

    /**
     * Gets the current latched stat of the button.
     * 
     * @return The latched state
     */
    public boolean getState()
    {
        return mCurrentState;
    }

}
