package org.snobot.lib.vision;

import org.snobot.lib.vision.MjpegReceiver.ImageReceiver;

public class MjpegForwarder implements ImageReceiver
{
    private final int mBindPort;

    public MjpegForwarder(int aBindPort)
    {
        mBindPort = aBindPort;
        MjpgServer.getInstance(mBindPort);
    }

    @Override
    public void onImage(byte[] aImageBytes)
    {
        MjpgServer.getInstance(mBindPort).update(aImageBytes);
    }

}
