package com.snobot.lib.ui;

public class LatchedButton
{
    private boolean mLastState;
    private boolean mCurrentState;

    public LatchedButton()
    {
        mLastState = false;
    }

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

    public boolean getState()
    {
        return mCurrentState;
    }

}
