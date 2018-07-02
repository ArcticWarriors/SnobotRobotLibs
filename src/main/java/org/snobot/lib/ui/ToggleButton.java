package org.snobot.lib.ui;

public class ToggleButton
{
    private boolean mSwitchState;
    private boolean mLastSwitchState;

    public ToggleButton()
    {
        this(false);
    }

    public ToggleButton(boolean aDefaultState)
    {
        mSwitchState = aDefaultState;
    }

    /**
     * Updates the button. Should be called once per loop.
     * 
     * @param aCurrentState
     *            If the button is currently being pressed
     * @return True if the button is in its "high" state
     */
    public boolean update(boolean aCurrentState)
    {
        if (aCurrentState && !mLastSwitchState)
        {
            mSwitchState = !mSwitchState;
        }

        mLastSwitchState = aCurrentState;
        return mSwitchState;

    }

    /**
     * Gets if the state is currently "high".
     * 
     * @return If the button is toggled
     */
    public boolean getState()
    {
        return mSwitchState;
    }

    /**
     * Sets the toggled state.
     * 
     * @param aState
     *            The new state
     */
    public void setState(boolean aState)
    {
        mSwitchState = aState;
        mLastSwitchState = false;
    }

}
